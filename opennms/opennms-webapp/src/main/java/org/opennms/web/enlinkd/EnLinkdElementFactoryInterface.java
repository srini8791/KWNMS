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

import java.util.Collection;
import java.util.List;

public interface EnLinkdElementFactoryInterface {

	List<BridgeElementNode> getBridgeElements(int nodeId);

	Collection<BridgeLinkNode> getBridgeLinks(int nodeId);

	Collection<NodeLinkBridge> getNodeLinks(int nodeId);

	IsisElementNode getIsisElement(int nodeId);
	
	List<IsisLinkNode> getIsisLinks(int nodeId);

	LldpElementNode getLldpElement(int nodeId);
	
	List<LldpLinkNode> getLldpLinks(int nodeId);
	
	OspfElementNode getOspfElement(int nodeId);
	
	List<OspfLinkNode> getOspfLinks(int nodeId);

        CdpElementNode getCdpElement(int nodeId);
	        
	List<CdpLinkNode> getCdpLinks(int nodeId);

	KdpElementNode getKdpElement(int nodeId);

	List<KdpLinkNode> getKdpLinks(int nodeId);
}
