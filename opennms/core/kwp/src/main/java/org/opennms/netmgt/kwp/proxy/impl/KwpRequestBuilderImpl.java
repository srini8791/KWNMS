/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2007-2014 The OpenNMS Group, Inc.
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
import org.opennms.netmgt.kwp.KwpPacket;
import org.opennms.netmgt.kwp.KwpPacketHeader;
import org.opennms.netmgt.kwp.proxy.KwpRequestBuilder;

import java.net.InetAddress;
import java.util.List;

public class KwpRequestBuilderImpl<T> extends AbstractKwpRequestBuilder<T> {


    public KwpRequestBuilderImpl(LocationAwareKwpClientImpl client, InetAddress host, KwpPacketHeader header) {
        super(client,host,header);
    }

    public KwpRequestBuilderImpl(LocationAwareKwpClientImpl client, String host, KwpPacketHeader header) {
        super(client,host,header);
    }

    public KwpRequestBuilderImpl(LocationAwareKwpClientImpl client, InetAddress host, KwpPacketHeader header,List<KwpLTVPacket> ltvPacketList) {
        super(client,host,header,ltvPacketList);
    }

    @Override
    public KwpRequestBuilder withLocation(String location) {
        return null;
    }

    @Override
    public KwpRequestBuilder buildRequest() {
        return this;
    }

    @Override
    protected T processResponse(KwpGetResponseDTO response) {

        if (response.getErrorMessage() != null) {
            KwpPacket packet = response.getPacket();
        }

        return null;
    }
}
