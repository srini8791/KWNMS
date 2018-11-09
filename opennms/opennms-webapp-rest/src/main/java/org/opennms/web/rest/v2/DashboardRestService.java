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
import org.opennms.netmgt.dao.api.AlarmDao;
import org.opennms.netmgt.dao.api.EventDao;
import org.opennms.netmgt.dao.api.NodeDao;
import org.opennms.netmgt.dao.api.OutageDao;
import org.opennms.netmgt.model.OnmsEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
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
        JSONArray eventsArray = new JSONArray();
        int counter = 0;
        for (OnmsEvent event : latestEvents) {
            JSONObject eventObj = new JSONObject();
            eventObj.put("id", event.getId());
            if (event.getNodeLabel() == null) {
                String ipAddress = event.getIpAddr() == null ? "" : InetAddressUtils.toIpAddrString(event.getIpAddr());
                eventObj.put("ipaddress", ipAddress);
            } else {
                eventObj.put("ipaddress", event.getNodeLabel());
            }
            // remove milliseconds from time
            String time = event.getEventTime().toString();
            time = time.substring(0, time.length()-4);
            eventObj.put("time", time);
            eventObj.put("severity", event.getEventSeverity());
            eventObj.put("message", event.getEventLogMsg());
            eventsArray.put(eventObj);
            counter = 1;
        }
        buffer.append(eventsArray.toString());
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
        StringBuilder buffer = new StringBuilder("data: ");
        Map<String, List<Integer>> map = new HashMap<>();
        map.put("ptp", Arrays.asList(0, 0, 0));
        map.put("ptmp", Arrays.asList(0, 0, 0));
        map.put("wifi", Arrays.asList(0, 0, 0));
        for (Object[] array : productCounts) {
            String keyStr = String.valueOf(array[0]);
            String key = "outdoorap".equals(keyStr) || "indoorap".equals(keyStr) ? "wifi" : keyStr;
            if (map.containsKey(key)) {
                List<Integer> values = map.get(key);
                values.set(0, values.get(0) + Integer.parseInt(String.valueOf(array[1])));
                values.set(1, values.get(1) + Integer.parseInt(String.valueOf(array[2])));
                values.set(2, values.get(2) + Integer.parseInt(String.valueOf(array[3])));
            }
        }
        buffer.append(new JSONObject(map));
        buffer.append("\n\n");

        return Response.ok().entity(buffer.toString()).build();
    }

}
