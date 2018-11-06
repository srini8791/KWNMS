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

import org.json.JSONArray;
import org.json.JSONObject;
import org.opennms.core.utils.InetAddressUtils;
import org.opennms.core.utils.WebSecurityUtils;
import org.opennms.netmgt.dao.api.*;
import org.opennms.netmgt.model.OnmsEvent;
import org.opennms.netmgt.model.OnmsRegion;
import org.opennms.netmgt.model.OnmsSeverity;
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
@Path("dashboard")
public class DashboardRestService {

    private static final Logger LOG = LoggerFactory.getLogger(DashboardRestService.class);

    @Autowired
    private AlarmDao alarmDao;

    @Autowired
    private EventDao eventDao;

    @Autowired
    private NodeDao nodeDao;

    @Autowired
    private OutageDao outageDao;

    @GET
    @Path("/alarms")
    @Produces({"text/event-stream"})
    public Response getAlarmCounts(@Context final SecurityContext securityContext, @Context final UriInfo uriInfo) {
        List<Object[]> objects = alarmDao.getSeverityCountsForPast24Hours();
        StringBuilder buffer = new StringBuilder("data: ");
        buffer.append("{");
        for (int i=0; i<objects.size(); i++) {
            if (i > 0) {
                buffer.append(", ");
            }
            Object[] obj = objects.get(i);
            buffer.append("\"s").append(obj[0]).append("\": ");
            buffer.append("\"").append(obj[1]).append("\"");
        }
        buffer.append("}\n\n");
        return Response.ok().entity(buffer.toString()).build();
    }

    @GET
    @Path("/events/{timeframe}")
    @Produces({"text/event-stream"})
    public Response getEvents(@Context final SecurityContext securityContext,
                              @Context final UriInfo uriInfo,
                              @PathParam("timeframe") Integer timeframe) {
        Calendar calendar = Calendar.getInstance();
        // move the time to 48 hours before, if url is called as /events/48
        calendar.add(Calendar.HOUR_OF_DAY, (timeframe * -1));
        Date date = calendar.getTime();
        List<OnmsEvent> latestEvents = eventDao.getEventsAfterDate(date);
        StringBuilder buffer = new StringBuilder("data: ");
        buffer.append("[");
        int counter = 0;
        for (OnmsEvent event : latestEvents) {
            if (counter != 0) {
                buffer.append(",");
            }
            buffer.append("{");
            buffer.append("  \"id\": \"").append(event.getId()).append("\"");
            if (event.getNodeLabel() == null) {
                String ipAddress = event.getIpAddr() == null ? "" : InetAddressUtils.toIpAddrString(event.getIpAddr());
                buffer.append(", \"ipaddress\": \"").append(ipAddress).append("\"");
            } else {
                buffer.append(", \"ipaddress\": \"").append(event.getNodeLabel()).append("\"");
            }
            // remove milliseconds from time
            String time = event.getEventTime().toString();
            time = time.substring(0, time.length()-4);
            buffer.append(", \"time\": \"").append(time).append("\"");
            buffer.append(", \"severity\": \"").append(event.getEventSeverity()).append("\"");
            String eventLogMessage = event.getEventLogMsg();
            eventLogMessage = eventLogMessage.trim();
            eventLogMessage = eventLogMessage.replaceAll("\\<p\\>", "");
            eventLogMessage = eventLogMessage.replaceAll("\\</p\\>", "");
            eventLogMessage = eventLogMessage.replaceAll("\n", "");
            eventLogMessage = eventLogMessage.replaceAll("\"", "\\\\\"");
            buffer.append(", \"message\": \"").append(eventLogMessage).append("\"");
            buffer.append("}");
            counter = 1;
        }
        buffer.append("]");
        buffer.append("\n\n");
        return Response.ok().entity(buffer.toString()).build();
    }


    @GET
    @Path("/nodes/counts_summary")
    @Produces({"text/event-stream"})
    public Response getNodeCounts(@Context final SecurityContext securityContext, @Context final UriInfo uriInfo) {
        Integer outagesCount = outageDao.currentOutageCount();
        Number[] counts = nodeDao.getNodeStatusSummary();
        StringBuilder buffer = new StringBuilder("data: {");
        buffer.append("\"outages\": \"").append(outagesCount).append("\", ");
        buffer.append("\"active\": \"").append(counts[0]).append("\", ");
        buffer.append("\"inactive\": \"").append(counts[1]).append("\", ");
        buffer.append("\"unprovisioned\": \"").append(counts[2]).append("\"");
        buffer.append("}\n\n");
        return Response.ok().entity(buffer.toString()).build();
    }

    @GET
    @Path("/nodes/productcodes_summary")
    @Produces({"text/event-stream"})
    public Response getProductStatusSummary(@Context final SecurityContext securityContext, @Context final UriInfo uriInfo) {
        List<Object[]> productCounts = nodeDao.getProductStatusSummary();
        StringBuilder buffer = new StringBuilder("data: {");
        int wifitotal = 0;
        int wifiup = 0;
        int wifidown = 0;
        int counter = 0;
        for (Object[] array : productCounts) {
            if ("outdoorap".equals(array[0]) || "indoorap".equals(array[0])) {
                wifitotal += Integer.parseInt(array[1].toString());
                wifiup += Integer.parseInt(array[2].toString());
                wifidown += Integer.parseInt(array[3].toString());
            } else {
                if (counter != 0) {
                    buffer.append(",");
                }
                buffer.append("\"").append(array[0]).append("\": [");
                buffer.append("\"").append(array[1]).append("\", ");
                buffer.append("\"").append(array[2]).append("\", ");
                buffer.append("\"").append(array[3]).append("\"");
                buffer.append("]");
                counter = 1;
            }
        }
        // add for wifi
        buffer.append(",");
        buffer.append("\"wifi\": [");
        buffer.append("\"").append(wifitotal).append("\", ");
        buffer.append("\"").append(wifiup).append("\", ");
        buffer.append("\"").append(wifidown).append("\"");
        buffer.append("]");

        buffer.append("}\n\n");
        return Response.ok().entity(buffer.toString()).build();
    }

}
