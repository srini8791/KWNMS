/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2008-2014 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2014 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.web.rest.v2;

import org.apache.cxf.jaxrs.ext.search.SearchBean;
import org.opennms.core.config.api.JaxbListWrapper;
import org.opennms.core.criteria.CriteriaBuilder;
import org.opennms.core.resource.Vault;
import org.opennms.core.utils.InetAddressUtils;
import org.opennms.netmgt.config.SnmpConfigAccessService;
import org.opennms.netmgt.dao.api.NodeDao;
import org.opennms.netmgt.dao.api.ProfileDao;
import org.opennms.netmgt.events.api.EventConstants;
import org.opennms.netmgt.events.api.EventProxy;
import org.opennms.netmgt.model.*;
import org.opennms.netmgt.model.events.EventBuilder;
import org.opennms.netmgt.model.events.EventUtils;
import org.opennms.netmgt.snmp.SnmpAgentConfig;
import org.opennms.netmgt.snmp.SnmpObjId;
import org.opennms.netmgt.snmp.SnmpValue;
import org.opennms.netmgt.snmp.proxy.LocationAwareSnmpClient;
import org.opennms.netmgt.snmp.snmp4j.Snmp4JValueFactory;
import org.opennms.netmgt.xml.event.Event;
import org.opennms.web.rest.support.Aliases;
import org.opennms.web.rest.support.RedirectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;

//import org.opennms.netmgt.model.events.EventUtils;
//import org.opennms.netmgt.xml.event.Event;

/**
 * Basic Web Service using REST for {@link OnmsProfile} entity
 */
@Component
@Path("profiles")
@Transactional
public class ProfileRestService extends AbstractDaoRestService<OnmsProfile, SearchBean, Integer, String> {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileRestService.class);

    @Autowired
    private ProfileDao m_dao;

    @Autowired
    private NodeDao nodeDao;

    @Autowired
    private LocationAwareSnmpClient m_locationAwareSnmpClient;


    @Autowired
    private SnmpConfigAccessService m_accessService;

    @Autowired
    @Qualifier("eventProxy")
    private EventProxy m_eventProxy;

    @Override
    protected ProfileDao getDao() {
        return m_dao;
    }

    @Override
    protected Class<OnmsProfile> getDaoClass() {
        return OnmsProfile.class;
    }

    @Override
    protected Class<SearchBean> getQueryBeanClass() {
        return SearchBean.class;
    }

    @Override
    protected CriteriaBuilder getCriteriaBuilder(UriInfo uriInfo) {
        final CriteriaBuilder builder = new CriteriaBuilder(OnmsProfile.class, Aliases.profile.toString());
        return builder;
    }

    @Override
    protected JaxbListWrapper<OnmsProfile> createListWrapper(Collection<OnmsProfile> list) {
        return new OnmsProfileList(list);
    }

    @POST
    @Path("/{profileId}/applyToNodes")
    @Transactional
    public Response applyProfiles(@Context final SecurityContext securityContext, @Context final UriInfo uriInfo,
                                  @PathParam("profileId") Integer id,
                                  @RequestBody List<Integer> nodesToApply) {
        writeLock();
        try {
            OnmsProfile profile = getDao().findProfileById(id);
            LOG.debug("applyingProfiles: profileId = {}, nodesToApply = {}", id, nodesToApply);
           Integer nId = null;
            Set<OnmsNode> nodesSet = new HashSet<>();
            for (Integer nodeId : nodesToApply) {
                OnmsNode node = nodeDao.load(nodeId);
                if (nId == null) {
                    nId = node.getId();
                }
                nodesSet.add(node);
                Event event = EventUtils.createNodeProfileApplyEvent("ReST",nodeId,profile.getId());
                sendEvent(event);
            }
            EventBuilder builder = new EventBuilder(EventConstants.NODES_PROFILE_APPLY_EVENT_UEI, "Provisiond");
            builder.setNodeid(nId);
            builder.addParam(EventConstants.PARAM_PROFILE_NAME,profile.getName());
            sendEvent(builder.getEvent());
            profile.setNodes(nodesSet);
            getDao().update(profile);
            LOG.debug("applyingProfiles: successful");

            return Response.accepted().build();
        } finally {
            writeUnlock();
        }
    }



    public String createProfileOnDevice(Integer nodeId) {
        OnmsNode  node = nodeDao.get(nodeId);
        final InetAddress addr = InetAddressUtils.addr(node.getPrimaryIP());
        LOG.debug("createProfileOnDevice: nodeId = {}, addr = {}", nodeId, addr);
        final SnmpAgentConfig config = m_accessService.getAgentConfig(addr, node.getLocation().getLocationName());
        SnmpObjId oid = SnmpObjId.get(".1.3.6.1.4.1.841.1.1.2.5.2.0");
        SnmpObjId[] oids = {oid,SnmpObjId.get(".1.3.6.1.4.1.841.1.1.2.5.3.0"),SnmpObjId.get(".1.3.6.1.4.1.841.1.1.2.1.4.9.0"),SnmpObjId.get(".1.3.6.1.4.1.841.1.1.2.1.4.10.0")};
        Snmp4JValueFactory factory = new Snmp4JValueFactory();
        SnmpValue[] values = {
                factory.getOctetString(node.getPrimaryIP().getBytes()),
                factory.getInt32(7),
                factory.getGauge32(3),
                factory.getGauge32(1)
        };
        String errorMessage = "";
        final StringBuilder builder = new StringBuilder();
        try {

            CompletableFuture<List<SnmpValue>> future =  m_locationAwareSnmpClient.set(config,Arrays.asList(oids),Arrays.asList(values)).execute();
            List<SnmpValue> resultValues = future.whenComplete((m,ex) -> {
                if(ex != null) {
                    builder.append(ex.getMessage());
                } else {
                    if(m == null || m.size() != oids.length) {
                        builder.append("Error Occurred while PDU set");
                    }
                }
            }).get();
            errorMessage = builder.toString();
        } catch(Exception ex) {
            errorMessage = ex.getMessage();
        }

        return errorMessage;
    }



    @GET
    @Path("/extractProfile")
    @Transactional
    public Response extractProfile(@Context final SecurityContext securityContext, @Context final UriInfo uriInfo) {
        MultivaluedMap<String, String> params = uriInfo.getQueryParameters();
        Integer nodeId = null;
        if (params.containsKey("nodeId")) {
            String nodeIdStr = params.getFirst("nodeId");
            params.remove("nodeId");
            if (nodeIdStr != null) {
                nodeId = Integer.parseInt(nodeIdStr);
            }
        } else {
            return Response.status(Status.BAD_REQUEST).entity(prepareErrorObject("Missing parameter: nodeId")).build();
        }

        OnmsNode  node = nodeDao.get(nodeId);
        String primaryIP = node.getPrimaryIP();
        Properties properties = readProfileProperties(primaryIP);
        if (properties == null) {
            return Response.status(Status.NOT_FOUND).entity(prepareErrorObject("Profile not retrieved for this device. Please retrieve the profile from the device.")).build();
        } else if (properties.isEmpty()) {
            return Response.status(Status.NOT_FOUND).entity(prepareErrorObject("No profile properties found for this device.")).build();
        } else {
            return Response.ok().entity(properties).build();
        }
    }

    @GET
    @Path("/retrieveProfile")
    @Transactional
    public Response retrieveProfile(@Context final SecurityContext securityContext, @Context final UriInfo uriInfo) {
        MultivaluedMap<String, String> params = uriInfo.getQueryParameters();
        Integer nodeId = null;
        if (params.containsKey("nodeId")) {
            String nodeIdStr = params.getFirst("nodeId");
            params.remove("nodeId");
            if (nodeIdStr != null) {
                nodeId = Integer.parseInt(nodeIdStr);
            }
        } else {
            return Response.status(Status.BAD_REQUEST).entity(prepareErrorObject("Missing parameter: nodeId")).build();
        }

        String errorMessage = createProfileOnDevice(nodeId);
        if (errorMessage.length() > 0) {
            return Response.serverError().entity(prepareErrorObject(errorMessage)).build();
        }

        String tftpAddress = Vault.getProperty("org.opennms.tftp.address");


        LOG.info("retreiveProfile: nodeId = {}, tftpAddress = {}", nodeId, tftpAddress);

        OnmsNode  node = nodeDao.get(nodeId);
        String ipAddress = node.getPrimaryIP().replaceAll("\\.","_") + ".cfg";
        final InetAddress addr = InetAddressUtils.addr(node.getPrimaryIP());
        final SnmpAgentConfig config = m_accessService.getAgentConfig(addr, node.getLocation().getLocationName());
        SnmpObjId oid = SnmpObjId.get(".1.3.6.1.4.1.841.1.1.2.5.6.0");
        SnmpObjId[] oids = {oid,SnmpObjId.get(".1.3.6.1.4.1.841.1.1.2.5.2.0"),SnmpObjId.get(".1.3.6.1.4.1.841.1.1.2.5.3.0"),SnmpObjId.get(".1.3.6.1.4.1.841.1.1.2.5.4.0")};
        Snmp4JValueFactory factory = new Snmp4JValueFactory();
        SnmpValue[] values = {
                factory.getOctetString(tftpAddress.getBytes()),
                factory.getOctetString(ipAddress.getBytes()),
                factory.getInt32(7),
                factory.getInt32(1)
        };

        final StringBuilder builder = new StringBuilder();
        try {

            CompletableFuture<List<SnmpValue>> future =  m_locationAwareSnmpClient.set(config,Arrays.asList(oids),Arrays.asList(values)).execute();
            List<SnmpValue> resultValues = future.whenComplete((m,ex) -> {
                if(ex != null) {
                    builder.append(ex.getMessage());
                } else {
                    if(m == null || m.size() != oids.length) {
                        builder.append("Error Occurred while PDU set");
                    }
                }
            }).get();
            errorMessage = builder.toString();
        } catch(Exception ex) {
            errorMessage = ex.getMessage();
        }

        if (errorMessage.length() > 0) {
            return Response.serverError().entity(prepareErrorObject(errorMessage)).build();
        }
        CompletableFuture<SnmpValue> futureStatus = asynRerun(config);
        try {
            futureStatus.whenComplete((m,ex)-> {
                if(ex != null) {
                    builder.append(ex.getMessage());
                }
            }).get();
            errorMessage = builder.toString();
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }

        if (errorMessage.length() > 0) {
            return Response.serverError().entity(prepareErrorObject(errorMessage)).build();
        }

        return Response.ok().build();
    }

    private CompletableFuture<SnmpValue> asynRerun(final SnmpAgentConfig config) {
        SnmpObjId statusOid = SnmpObjId.get(".1.3.6.1.4.1.841.1.1.2.5.5.0");
        CompletableFuture<SnmpValue> future = m_locationAwareSnmpClient.get(config,statusOid).execute();
        SnmpValue value = null;
        try {
            value = future.get();

        } catch (Exception e) {
            future.completeExceptionally(e);
            return future;
        }
        if (value.toLong() == 9) {
            return asynRerun(config);
        } else if(value.toLong() == 11) {
            future.completeExceptionally(new Exception("failed to download profile"));
            return future;
        } else {
            CompletableFuture<SnmpValue> response = new CompletableFuture<>();
            response.complete(value);
            return response;
        }
    }

    @POST
    @Path("/{nodeId}/testprofile")
    @Transactional
    public Response testProfile(@Context final SecurityContext securityContext, @Context final UriInfo uriInfo,
                                          @PathParam("nodeId") Integer nodeId) {
        OnmsNode  node = nodeDao.get(nodeId);
        final InetAddress addr = InetAddressUtils.addr(node.getPrimaryIP());
        final SnmpAgentConfig config = m_accessService.getAgentConfig(addr, node.getLocation().getLocationName());
        SnmpObjId oid = SnmpObjId.get(".1.3.6.1.4.1.841.1.1.2.5.2.0");
        SnmpObjId[] oids = {oid,SnmpObjId.get(".1.3.6.1.4.1.841.1.1.2.5.3.0"),SnmpObjId.get(".1.3.6.1.4.1.841.1.1.2.1.4.9.0"),SnmpObjId.get(".1.3.6.1.4.1.841.1.1.2.1.4.10.0")};
        Snmp4JValueFactory factory = new Snmp4JValueFactory();
        SnmpValue[] values = {
                factory.getOctetString("testFile.fil".getBytes()),
                factory.getInt32(7),
                factory.getGauge32(3),
                factory.getGauge32(1)
        };
        String errorMessage = "";
        final StringBuilder builder = new StringBuilder();
        try {

            CompletableFuture<List<SnmpValue>> future =  m_locationAwareSnmpClient.set(config,Arrays.asList(oids),Arrays.asList(values)).execute();
            List<SnmpValue> resultValues1 = future.whenComplete((resultValues,ex) -> {
                if(ex != null) {
                    //return null;
                    builder.append(ex.getMessage());
                } else {
                    if(resultValues == null || resultValues.size() != oids.length) {
                        builder.append("Error Occurred while PDU set");
                    }
                }
                //return resultValues;
            }).get();
            errorMessage = builder.toString();
        } catch(Exception ex) {
            errorMessage = ex.getMessage();
        }

        if (errorMessage.length() > 0) {
            return Response.serverError().entity(prepareErrorObject(errorMessage)).build();
        }
        return Response.ok().build();
    }

    @Override
    public Response doCreate(final SecurityContext securityContext, final UriInfo uriInfo, final OnmsProfile profile) {
        if (profile == null) {
            throw getException(Status.BAD_REQUEST, "Profile object cannot be null");
        }
        final Integer id = getDao().save(profile);
        return Response.created(RedirectHelper.getRedirectUri(uriInfo, id)).build();
    }

    @Override
    protected Response doUpdate(SecurityContext securityContext, UriInfo uriInfo, Integer key, OnmsProfile profile) {
        if (profile == null) {
            throw getException(Status.BAD_REQUEST, "Profile object cannot be null");
        }
        getDao().saveOrUpdate(profile);
        return Response.noContent().build();
    }

    @Override
    protected void doDelete(SecurityContext securityContext, UriInfo uriInfo, OnmsProfile profile) {
        getDao().delete(profile);
    }

    @Override
    protected OnmsProfile doGet(UriInfo uriInfo, String id) {
        return getDao().get(Integer.parseInt(id));
    }


    // private utility methods
    /*
        $scope.newProfile.ssid (wireless.@wifi-iface[1].ssid)
        $scope.newProfile.opMode.id (wireless.wifi1.hwmode)
        $scope.newProfile.bandwidth.id (wireless.wifi1.htmode)
        $scope.newProfile.channel (wireless.wifi1.channel)
     */
    private static final Map<String, String> PROFILE_PROPS_MAP = new HashMap<>();

    static {
        PROFILE_PROPS_MAP.put("wireless.@wifi-iface[1].ssid", "ssid");
        PROFILE_PROPS_MAP.put("wireless.wifi1.hwmode", "opModeId");
        PROFILE_PROPS_MAP.put("wireless.wifi1.htmode", "bandwidthId");
        PROFILE_PROPS_MAP.put("wireless.wifi1.channel", "channel");
    }

    private Properties readProfileProperties(String nodePrimaryIP) {
        Properties properties = new Properties();
        String tftpRootPath = Vault.getProperty("org.opennms.tftp.rootpath");
        String filePath = tftpRootPath + File.separator + nodePrimaryIP.replaceAll("\\.", "_") + ".cfg";
        boolean profileExists = Files.exists(Paths.get(filePath));
        if (!profileExists) {
            return null;
        }

        try {
            List<String> props = Files.readAllLines(Paths.get(filePath));
            for (String prop : props) {
                int delimiterIndex = prop.indexOf("=");
                if (delimiterIndex == -1) {
                    continue;
                }
                String key = prop.substring(0, delimiterIndex).trim();
                String value = prop.substring(delimiterIndex+1).trim();
                value = value.substring(1, value.length()-1); // stripping single quotes
                if (PROFILE_PROPS_MAP.containsKey(key)) {
                    String convertedValue = getConvertedValue(key, value);
                    properties.put(PROFILE_PROPS_MAP.get(key), convertedValue);
                }
            }
        } catch (FileNotFoundException e) {
            LOG.error("readProfileProperties: No file found at: {}", filePath);
        } catch (IOException e) {
            LOG.error("readProfileProperties: Unable to load profile content as properties.");
        }
        return properties;
    }


    // utility methods
    private String getConvertedValue(String key, String value) {
        String convertedValue = value;
        if (key.equals("wireless.wifi1.hwmode")) {
            OpMode mode = OpMode.get(value);
            convertedValue = String.valueOf(mode.getId());
        } else if (key.equals("wireless.wifi1.htmode")) {
            String numericalValue = value.substring(2);
            Bandwidth bandwidth = Bandwidth.get(numericalValue + "MHz");
            convertedValue = String.valueOf(bandwidth.getId());
        } else if (key.equals("wireless.wifi1.channel")) {
            if (value.equals("auto")) {
                convertedValue = String.valueOf("-1");
            }
        }
        return convertedValue;
    }


    private Map<String, String> prepareErrorObject(String errorMessage) {
        Map<String, String> object = new HashMap<>();
        object.put("errorMessage", errorMessage);
        return object;
    }
}
