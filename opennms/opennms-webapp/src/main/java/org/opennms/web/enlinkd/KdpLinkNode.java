/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2014 The OpenNMS Group, Inc.
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

package org.opennms.web.enlinkd;


public class KdpLinkNode implements Comparable<KdpLinkNode> {


    private String m_kdpLocalPort;
    private String m_kdpLocalPortUrl;

    private String m_kdpCreateTime;
    private String m_kdpLastPollTime;

    public String getKdpLocalPort() {
        return m_kdpLocalPort;
    }

    public void setKdpLocalPort(String kdpLocalPort) {
        this.m_kdpLocalPort = kdpLocalPort;
    }

    public String getKdpLocalPortUrl() {
        return m_kdpLocalPortUrl;
    }

    public void setKdpLocalPortUrl(String kdpLocalPortUrl) {
        this.m_kdpLocalPortUrl = kdpLocalPortUrl;
    }

    public String getKdpCreateTime() {
        return m_kdpCreateTime;
    }

    public void setKdpCreateTime(String kdpCreateTime) {
        this.m_kdpCreateTime = kdpCreateTime;
    }

    public String getKdpLastPollTime() {
        return m_kdpLastPollTime;
    }

    public void setKdpLastPollTime(String kdpLastPollTime) {
        this.m_kdpLastPollTime = kdpLastPollTime;
    }

    @Override
    public int compareTo(KdpLinkNode o) {
        return m_kdpLocalPort.compareTo(o.m_kdpLocalPort);
    }

}
