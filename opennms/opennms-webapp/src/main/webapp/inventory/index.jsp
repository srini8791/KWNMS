<%--
/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2002-2014 The OpenNMS Group, Inc.
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

--%>

<%@page language="java"
	contentType="text/html"
	session="true"
%>


<%@page import="org.opennms.core.resource.Vault"%>
<jsp:include page="/includes/bootstrap.jsp" flush="false">
    <jsp:param name="title" value="Inventory" />
    <jsp:param name="headTitle" value="Inventory" />
    <jsp:param name="location" value="inventory" />
    <jsp:param name="breadcrumb" value="Inventory" />
</jsp:include>

<jsp:include page="/assets/load-assets.jsp" flush="false">
  <jsp:param name="asset" value="onms-assets"/>
</jsp:include>

<div ng-app="onms-assets">
  <div id="inventoryCtrl" class="container-fluid" ng-controller="InventoryCtrl" ng-init="init()">
  <div growl></div>
  <div class="row">
    <div class="col-md-12">
      <div class="panel panel-default">
          <div class="panel-heading">
              <h3 class="panel-title">List of Inventory</h3>
          </div>
          <div class="panel-body" id="onms-search">
              <div class="row">
                <div class="col-md-8"></div>
                <div class="col-md-4" align="right">
                  <select ng-model="limit" ng-change="loadNodes()">
                    <option>10</option>
                    <option>20</option>
                    <option>50</option>
                    <option>100</option>
                  </select>
                </div>
              </div>
              <div class="row">
                  <div class="col-md-12">
                      <table class="table table-condensed table-bordered">
                        <thead>
                          <tr>
                            <th>System Name</th>
                            <th>IP Address</th>
                            <th>Serial Number</th>
                            <th>MAC Address</th>
                            <th>Model</th>
                            <th>Firmware</th>
                            <th>Channel</th>
                            <th>Bandwidth</th>
                            <th>Ethernet Speed</th>
                            <th>I/O Bandwidth Limit</th>
                            <th>Modulation</th>
                            <th>Operation Mode</th>
                          </tr>
                        </thead>
                        <tbody>
                          <tr ng-repeat="node in nodes">
                            <td>{{node.sysName}}</td>
                            <td>{{node.label}}</td>
                            <td>{{node.assetRecord.serialNumber}}</td>
                            <td>{{node.macAddress}}</td>
                            <td>{{node.assetRecord.modelNumber}}</td>
                            <td>{{node.assetRecord.firmware}}</td>
                            <td>{{node.channel}}</td>
                            <td>{{node.bandwidth.label}}</td>
                            <td>{{node.assetRecord.ethernetSpeed}}</td>
                            <td>{{node.assetRecord.ioBandwidthLimit}}</td>
                            <td>{{node.assetRecord.modulation}}</td>
                            <td>{{node.opMode.label}}</td>
                          </tr>
                        </tbody>
                      </table>
                  </div>
              </div>
          </div>
      </div> <!-- panel ends -->
    </div>
  </div>
</div>
<hr />

<jsp:include page="/includes/bootstrap-footer.jsp" flush="false" />
