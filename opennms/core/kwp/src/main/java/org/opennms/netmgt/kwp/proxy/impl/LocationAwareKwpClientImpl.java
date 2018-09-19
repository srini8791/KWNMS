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

import org.opennms.core.rpc.api.RpcClient;
import org.opennms.core.rpc.api.RpcClientFactory;
import org.opennms.netmgt.kwp.*;
import org.opennms.netmgt.kwp.proxy.KwpRequestBuilder;
import org.opennms.netmgt.kwp.proxy.LocationAwareKwpClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class LocationAwareKwpClientImpl implements LocationAwareKwpClient, InitializingBean {

    @Autowired
    private RpcClientFactory rpcClientFactory;

    private RpcClient<KwpGetRequestDTO, KwpGetResponseDTO> delegate;

    public LocationAwareKwpClientImpl() {}


    public LocationAwareKwpClientImpl(RpcClientFactory rpcClientFactory) {
        this.rpcClientFactory = Objects.requireNonNull(rpcClientFactory);
        afterPropertiesSet();
    }

    @Override
    public void afterPropertiesSet() {
        delegate = rpcClientFactory.getClient(KwpProxyRpcModule.INSTANCE);
    }


    @Override
    public KwpRequestBuilder<KwpPacket> get(String host, byte id, byte type, byte subType) {

        return null;
    }

    @Override
    public KwpRequestBuilder<KwpPacket> get(String host, KwpPacketHeader header) {
        //KwpGetRequestDTO getRequestDTO = new KwpGetRequestDTO();
        //getRequestDTO.buildGetRequest(header);
        return new KwpRequestBuilderImpl<KwpPacket>(this,host,header).buildRequest();
    }

    @Override
    public KwpRequestBuilder<KwpPacket> get(String host, KwpPacketHeader header, List<KwpLTVPacket> ltvs) {
        try {
            InetAddress address = InetAddress.getByName(host);
            return new KwpRequestBuilderImpl<KwpPacket>(this,address,header,ltvs).buildRequest();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public KwpRequestBuilder<KwpPacket> get(InetAddress host, KwpPacketHeader header) {

        return new KwpRequestBuilderImpl(this,host,header).buildRequest();
    }

    public CompletableFuture<KwpGetResponseDTO> execute(KwpGetRequestDTO request) {
        return delegate.execute(request);
    }
}
