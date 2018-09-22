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

package org.opennms.netmgt.kwp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.CompletableFuture;

public class KwpAsyncClient {
    private String host;
    private int port = 7861;
    private DatagramChannel client = null;

    public KwpAsyncClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public KwpAsyncClient() {
    }


    public CompletableFuture<KwpGetResponseDTO> sendAndReceive(KwpGetRequestDTO request){
        this.host = request.getHost();
        CompletableFuture<KwpGetResponseDTO> result = new CompletableFuture<>();

        try {
            client = DatagramChannel.open();
            //client.setOption()
            client.bind(null);
        } catch (IOException e) {
            result.completeExceptionally(e);
        }
        byte[] bytes = null;
        try {
            bytes = request.getPacket().toByteArray();
        } catch (IOException e) {
            result.completeExceptionally(e);
        }

        if (client != null && client.isOpen()) {
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            InetSocketAddress serverAddress = new InetSocketAddress(this.host, this.port);

            try {
                client.connect(serverAddress);
                if (client.isConnected()) {
                    client.send(buffer, serverAddress);
                    buffer.clear();
                    client.socket().setSoTimeout(3000);
                    client.receive(buffer);
                    buffer.flip();
                    int limits = buffer.limit();
                    bytes = new byte[limits];
                    buffer.get(bytes, 0, limits);
                    KwpGetResponseDTO responseDTO = new KwpGetResponseDTO(bytes);
                    result.complete(responseDTO);
                    client.disconnect();
                }
            } catch (IOException e) {
                result.completeExceptionally(e);
            }
        }


        return result;
    }

    public CompletableFuture<KwpGetResponseDTO> send(KwpGetRequestDTO request) {
        return null;
    }
}
