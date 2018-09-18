/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2011-2014 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.kwp.proxy.impl;

import org.opennms.netmgt.kwp.KwpGetResponseDTO;
import org.opennms.netmgt.kwp.KwpLTVPacket;
import org.opennms.netmgt.kwp.KwpPacketHeader;
import org.opennms.netmgt.kwp.proxy.KwpRequestBuilder;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractKwpRequestBuilder<T> implements KwpRequestBuilder<T> {

    protected LocationAwareKwpClientImpl m_client;
    protected KwpPacketHeader header;
    protected InetAddress host;
    protected List<KwpLTVPacket> ltvPacketList;

    public AbstractKwpRequestBuilder(LocationAwareKwpClientImpl client, InetAddress host, KwpPacketHeader header) {
        this.m_client = client;
        this.host = host;
        this.header = header;
    }

    public AbstractKwpRequestBuilder(LocationAwareKwpClientImpl client, String host, KwpPacketHeader header) {
        this.m_client = client;
        try {
            this.host = InetAddress.getByName(host);
        } catch (UnknownHostException ex) {

        }
        this.header = header;
    }

    public AbstractKwpRequestBuilder(LocationAwareKwpClientImpl client, InetAddress host, KwpPacketHeader header,List<KwpLTVPacket> ltvPacketList) {
        this.m_client = client;
        this.host = host;
        this.header = header;
        this.ltvPacketList = ltvPacketList;
    }

    @Override
    public KwpRequestBuilder withLocation(String location) {
        return null;
    }

    @Override
    public KwpRequestBuilder buildRequest() {
        return null;
    }

    @Override
    public CompletableFuture execute() {
        return null;
    }

    protected abstract T processResponse(KwpGetResponseDTO response);
}
