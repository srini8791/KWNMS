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

import org.opennms.core.rpc.api.RpcModule;
import org.opennms.core.rpc.api.RpcRequest;
import org.opennms.core.rpc.api.RpcResponse;
import org.opennms.netmgt.kwp.KwpAsyncClient;
import org.opennms.netmgt.kwp.KwpGetRequestDTO;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class KwpProxyRpcModule<S extends RpcRequest,T extends RpcResponse> implements RpcModule<S, T> {

    public static final KwpProxyRpcModule INSTANCE = new KwpProxyRpcModule();
    public static final String KDP_MODULE_ID = "KDP";


    @Override
    public String getId() {
        return null;
    }

    @Override
    public String marshalRequest(S request) {
        return null;
    }

    @Override
    public S unmarshalRequest(String request) {
        return null;
    }

    @Override
    public String marshalResponse(T response) {
        return null;
    }

    @Override
    public T unmarshalResponse(String response) {
        return null;
    }

    @Override
    public T createResponseWithException(Throwable ex) {
        return null;
    }

    @Override
    public CompletableFuture<T> execute(S request) {
        KwpGetRequestDTO req = (KwpGetRequestDTO) request;
        req.getHost();
        req.getPacket();
        KwpAsyncClient client = new KwpAsyncClient();
        try {
            return (CompletableFuture<T>) client.sendAndReceive(req);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
