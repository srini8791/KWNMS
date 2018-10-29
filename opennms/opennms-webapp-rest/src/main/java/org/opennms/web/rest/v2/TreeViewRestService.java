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
import org.opennms.netmgt.dao.api.RegionDao;
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

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;


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

    @GET
    @Path("/regions")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getRegions(@Context final SecurityContext securityContext, @Context final UriInfo uriInfo) {
        List<TreeNodeDTO> regionNodes = new ArrayList<>();
        List<OnmsRegion> regions = regionDao.findAll();
        if (regions != null && !regions.isEmpty()) {
            for (OnmsRegion region : regions) {
                TreeNodeDTO regionNode = new TreeNodeDTO(region.getName(), true);
                regionNode.addData("id", region.getId());
                regionNode.addData("type", "region");
                regionNodes.add(regionNode);
            }
        }
        return Response.ok().entity(regionNodes).build();
    }

    private Map<String, String> prepareErrorObject(String errorMessage) {
        Map<String, String> object = new HashMap<>();
        object.put("errorMessage", errorMessage);
        return object;
    }


    /**
     * {'text': 'South', 'children': true, 'data': {'type': 'region'}}
     */
    class TreeNodeDTO implements Serializable {
        private String text;
        private boolean children = false;
        private Map<String, Object> data = new HashMap<>();

        public TreeNodeDTO(String text) {
            this.text = text;
        }

        public TreeNodeDTO(String text, boolean children) {
            this.text = text;
            this.children = children;
        }

        public void addData(String key, Object value) {
            data.put(key, value);
        }

        public String getText() {
            return text;
        }

        public boolean isChildren() {
            return children;
        }

        public Map<String, Object> getData() {
            return data;
        }
    }
}
