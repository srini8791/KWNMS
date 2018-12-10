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

import org.opennms.netmgt.dao.api.MonitoringLocationDao;
import org.opennms.netmgt.dao.api.NodeDao;
import org.opennms.netmgt.dao.api.RegionDao;
import org.opennms.netmgt.model.OnmsFacility;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.model.OnmsRegion;
import org.opennms.netmgt.model.monitoringLocations.OnmsMonitoringLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.io.Serializable;
import java.util.*;


/**
 * Basic Web Service using REST for rendering tree view
 */
@Component
@Path("treeview")
public class TreeViewRestService {

    private static final Logger LOG = LoggerFactory.getLogger(TreeViewRestService.class);

    @Autowired
    private NodeDao nodeDao;

    @Autowired
    private RegionDao regionDao;

    @Autowired
    private MonitoringLocationDao locationDao;

    @GET
    @Path("/regions")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getRegions(@Context final SecurityContext securityContext, @Context final UriInfo uriInfo) {
        List<TreeNodeDTO> regionNodes = new ArrayList<>();
        List<OnmsRegion> regions = regionDao.findAll();
        if (regions != null && !regions.isEmpty()) {
            for (OnmsRegion region : regions) {
                TreeNodeDTO regionNode = new TreeNodeDTO(region.getName());
                regionNode.addData("id", region.getId());
                regionNode.addData("type", "region");
                regionNodes.add(regionNode);
            }
        }
        return Response.ok().entity(regionNodes).build();
    }

    @GET
    @Path("/regions/{regionId}/locations")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getRegionLocations(@Context final SecurityContext securityContext, @Context final UriInfo uriInfo,
                               @PathParam("regionId") final Integer regionId) {
        List<TreeNodeDTO> regionLocations = new ArrayList<>();
        OnmsRegion region = regionDao.get(regionId);
        if (region != null) {
            Set<OnmsFacility> locations = region.getFacilities();
            if (locations != null && !locations.isEmpty()) {
                for (OnmsFacility location : locations) {
                    TreeNodeDTO locationNode = new TreeNodeDTO(location.getName());
                    locationNode.addData("id", location.getId());
                    locationNode.addData("type", "location");
                    locationNode.addData("lat", location.getLatitude());
                    locationNode.addData("long", location.getLongitude());
                    regionLocations.add(locationNode);
                }
            }
        }
        return Response.ok().entity(regionLocations).build();
    }

    @GET
    @Path("/facility/{facilityId}/nodes")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getFacilityNodes(@Context final SecurityContext securityContext, @Context final UriInfo uriInfo,
                               @PathParam("facilityId") final Integer facilityId) {
        List<TreeNodeDTO> facilityNodes = new ArrayList<>();
        List<OnmsNode> nodes = nodeDao.findByFacilityId(facilityId);
        if (nodes != null && !nodes.isEmpty()) {
            for (OnmsNode node : nodes) {
                TreeNodeDTO locationNode = new TreeNodeDTO(node.getPrimaryIP());
                locationNode.addData("id", node.getId());
                locationNode.addData("type", "node");
                locationNode.addData("lat", node.getAssetRecord().getLatitude());
                locationNode.addData("long", node.getAssetRecord().getLongitude());
                facilityNodes.add(locationNode);
            }
        }
        return Response.ok().entity(facilityNodes).build();
    }

    @GET
    @Path("/location/{location}/nodes")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getLocationNodes(@Context final SecurityContext securityContext, @Context final UriInfo uriInfo,
                               @PathParam("location") final String location) {
        List<TreeNodeDTO> locationNodes = new ArrayList<>();
        List<OnmsNode> nodes = nodeDao.findByLocation(location);
        if (nodes != null && !nodes.isEmpty()) {
            for (OnmsNode node : nodes) {
                TreeNodeDTO locationNode = new TreeNodeDTO(node.getPrimaryIP());
                locationNode.addData("id", node.getId());
                locationNode.addData("type", "node");
                locationNode.addData("lat", node.getAssetRecord().getLatitude());
                locationNode.addData("long", node.getAssetRecord().getLongitude());
                locationNodes.add(locationNode);
            }
        }
        return Response.ok().entity(locationNodes).build();
    }

    @GET
    @Path("/{location}/nodes")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getNodes(@Context final SecurityContext securityContext, @Context final UriInfo uriInfo,
                               @PathParam("location") final Integer facilityId) {
        List<OnmsNode> nodes = nodeDao.findByFacilityId(facilityId);
        return Response.ok().entity(nodes).build();
    }


    private Map<String, String> prepareErrorObject(String errorMessage) {
        Map<String, String> object = new HashMap<>();
        object.put("errorMessage", errorMessage);
        return object;
    }


    /**
     * {'text': 'South', 'data': {'id': '1', 'type': 'region', 'lat': '0', 'long': '0'}, 'children': []}
     */
    class TreeNodeDTO implements Serializable {
        private String text;
        private List children = new ArrayList();
        private Map<String, Object> data = new HashMap<>();

        public TreeNodeDTO(String text) {
            this.text = text;
        }

        public void addData(String key, Object value) {
            data.put(key, value);
        }

        public String getText() {
            return text;
        }

        public List getChildren() {
            return children;
        }

        public void addChild(Object child) {
            children.add(child);
        }

        public Map<String, Object> getData() {
            return data;
        }
    }
}
