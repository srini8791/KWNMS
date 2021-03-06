<%--
/*******************************************************************************
* This file is part of OpenNMS(R).
*
* Copyright (C) 2018-2018 The OpenNMS Group, Inc.
* OpenNMS(R) is Copyright (C) 1999-2018 The OpenNMS Group, Inc.
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
*     OpenNMS(R) Licensing
<license@opennms.org>
*     http://www.opennms.org/
*     http://www.opennms.com/
*******************************************************************************/

--%>

<%@ page import="org.opennms.netmgt.model.Bandwidth" %>
<%@ page import="org.opennms.netmgt.model.OpMode" %>

<jsp:include page="/includes/bootstrap.jsp" flush="false">
  <jsp:param name="norequirejs" value="true"/>

  <jsp:param name="title" value="Profile Management"/>
  <jsp:param name="headTitle" value="Profile Management"/>
  <jsp:param name="breadcrumb" value="<a href='admin/index.jsp'>Admin</a>"/>
  <jsp:param name="breadcrumb" value="Profile Management"/>
</jsp:include>

<jsp:include page="/assets/load-assets.jsp" flush="false">
  <jsp:param name="asset" value="onms-assets"/>
</jsp:include>

<script type="text/javascript" >

  function addNodes() {
    $("#availableNodes option:selected").each(function(idx, obj) {
      $("#nodesToApply").append("<option value='" + obj.value + "'>" + obj.text + "</option>");
      $(this).remove();
    });
    selectAllOptions();
  }

  function removeNodes() {
    $("#nodesToApply option:selected").each(function(idx, obj) {
      $("#availableNodes").append("<option value='" + obj.value + "'>" + obj.text + "</option>");
      $(this).remove();
    });
    selectAllOptions();
  }

  function selectAllOptions() {
    $("#nodesToApply option").each(function() {
      $(this).prop("selected", true).trigger('change');
    });
  }

  function applyProfile() {
    angular.element($("#profCtrl")).scope().apply();
    removeNodes();
  }

</script>

<div ng-app="onms-assets">

  <div id="profCtrl" class="container-fluid" ng-controller="ProfileCtrl" ng-init="init()">

  <div growl></div>

  <div class="row">
    <div class="col-md-6">
      <div class="panel panel-default">
        <div class="panel-heading">
          <h3 class="panel-title">Create Profile</h3>
        </div>
        <div class="panel-body">
          <form method="post" name="profileForm" novalidate>
            <div class="form-group">
              <div class="row">
                <label for="input_nodeIp" class="col-sm-4 control-label">Load Profile from Device</label>
                <div class="col-sm-5">
                  <select class="form-control" id="nodeIp" ng-model="nodeId">
                    <option ng-repeat="node in nodes" value="{{node.id}}">{{node.label}}</option>
                  </select>
                </div>
                <div class="col-sm-3">
                  <button class="btn btn-default" ng-click="populateFormWithProfile(nodeId)">Populate Form</button>
                </div>
              </div>
            </div>
            <div class="form-group">
              <div class="row">
                <label for="input_profileName" class="col-sm-4 control-label">Profile Name</label>
                <div class="col-sm-8">
                  <input id="input_profileName" name="profileName" ng-model="newProfile.name" class="form-control" ng-required="true"/>
                </div>
              </div>
            </div>
            <div class="form-group">
              <div class="row">
                <label for="input_ssid" class="col-sm-4 control-label">SSID</label>
                <div class="col-sm-8">
                  <input id="input_ssid" name="ssid" ng-model="newProfile.ssid" class="form-control"/>
                </div>
              </div>
            </div>
            <div class="form-group">
              <div class="row">
                <label for="input_opMode" class="col-sm-4 control-label">Operation Mode</label>
                <div class="col-sm-8">
                  <select id="input_opMode" name="opMode" ng-model="newProfile.opMode.id" class="form-control">
                    <option value="0">Select Operation Mode</option>
                    <%
                      for (OpMode opMode : OpMode.values()) {
                        if (opMode.getId() == 0) {
                          continue;
                        }
                    %>
                    <option value="<%= opMode.getId() %>"><%= opMode.getLabel() %></option>
                    <% } %>
                  </select>
                </div>
              </div>
            </div>
            <div class="form-group">
              <div class="row">
                <label for="input_bandwidth" class="col-sm-4 control-label">Bandwidth</label>
                <div class="col-sm-8">
                  <select id="input_bandwidth" name="bandwidth" ng-model="newProfile.bandwidth.id" class="form-control">
                    <option value="0">Select Bandwidth</option>
                    <%
                      for (Bandwidth bandwidth : Bandwidth.values()) {
                        if (bandwidth.getId() == 0) {
                          continue;
                        }
                    %>
                    <option value="<%= bandwidth.getId() %>"><%= bandwidth.getLabel() %></option>
                    <% } %>
                  </select>
                </div>
              </div>
            </div>
            <div class="form-group">
              <div class="row">
                <label for="input_channel" class="col-sm-4 control-label">Channel</label>
                <div class="col-sm-8">
                  <select id="input_channel" name="channel" ng-model="newProfile.channel" class="form-control">
                    <option value="">Select Channel</option>
                    <option value="-1">auto</option>
                    <%
                      for (int channelNumber = 1; channelNumber <= 255; channelNumber++) {
                    %>
                    <option value="<%= channelNumber %>"><%= channelNumber %></option>
                    <% } %>
                  </select>
                </div>
              </div>
            </div>
            <div class="form-group">
              <div class="row">
                <label for="input_minimumFirmware" class="col-sm-4 control-label">Minimum Firmware Version</label>
                <div class="col-sm-8">
                  <input id="input_minimumFirmware" name="minimumFirmware" ng-model="newProfile.minimumFirmware" class="form-control"/>
                </div>
              </div>
            </div>
          </form>
        </div> <!-- panel-body -->
        <div class="panel-footer">
          <button type="button" class="btn btn-default" ng-click="save()" id="save-profile"
                  ng-disabled="profileForm.$invalid">
            Create Profile&nbsp;&nbsp;&nbsp;<span class="glyphicon glyphicon-plus"><span>
          </button>
        </div> <!-- panel-footer -->
      </div> <!-- panel -->


      <div class="panel panel-default">
        <div class="panel-heading">
          <h3 class="panel-title">Retrieve Profile from Device</h3>
        </div>
        <div class="panel-body">
          <form method="post" name="downloadProfile">
            <div class="form-group">
              <div class="row">
                <label for="input_nodeId" class="col-sm-4 control-label">Select Device</label>
                <div class="col-sm-8">
                  <select id="input_nodeId" name="nodeId" ng-model="profileToRetrieve" class="form-control" required>
                    <option ng-repeat="node in nodes" value="{{node.id}}">{{node.label}}</option>
                  </select>
                </div>
              </div>
            </div>
          </form>
        </div> <!-- panel-body -->
        <div class="panel-footer">
          <button type="button" class="btn btn-default" ng-click="retrieveProfile(profileToRetrieve)" id="download-profile">
            Retrieve Profile&nbsp;&nbsp;&nbsp;<span class="glyphicon glyphicon-save"><span>
          </button>
        </div> <!-- panel-footer -->
      </div> <!-- panel -->


      <div class="panel panel-default">
        <div class="panel-heading">
          <h3 class="panel-title">Apply Profile to Devices</h3>
        </div>
        <div class="panel-body">
          <form method="post" name="applyProfileForm">
            <div class="form-group">
              <div class="row">
                <label for="input_profileName" class="col-sm-4 control-label">Select Profile</label>
                <div class="col-sm-8">
                  <select id="input_profileName" name="profileName" ng-model="profileToApply" class="form-control" required>
                    <option ng-repeat="profile in profiles" value="{{profile.id}}">{{profile.name}}</option>
                  </select>
                </div>
              </div>
            </div>
            <div class="form-group">
              <div class="row">
                <label class="col-sm-4 control-label">Select Devices:</label>
                <div class="col-sm-4">
                  <label class="control-label">Available Devices</label>
                  <select class="form-control" id="availableNodes" multiple="multiple" size="10">
                    <option ng-repeat="node in nodes" value="{{node.id}}">{{node.label}}</option>
                  </select>
                  <button type="button" class="btn btn-default" id="nodes.doAdd" onClick="javascript:addNodes()">&nbsp;&#155;&#155;&nbsp; Add</button>
                </div>
                <div class="col-sm-4">
                  <label class="control-label">Selected Devices</label>
                  <select class="form-control" id="nodesToApply" multiple="multiple" ng-model="nodesToApply" size="10">
                  </select>
                  <button type="button" class="btn btn-default" id="nodes.doRemove" onClick="javascript:removeNodes()">&nbsp;&#139;&#139;&nbsp; Remove</button>
                </div>
              </div>
            </div>
          </form>
        </div> <!-- panel-body -->
        <div class="panel-footer">
          <button type="button" class="btn btn-default" onClick="javascript:applyProfile()" id="apply-profile">
            Apply Profile&nbsp;&nbsp;&nbsp;<span class="glyphicon glyphicon-paste"><span>
          </button>
        </div> <!-- panel-footer -->
      </div> <!-- panel -->
    </div> <!-- column -->
    <div class="col-md-6">
      <table class="profiles-table table table-condensed table-bordered">
        <thead>
        <tr>
          <th width="4%">&nbsp;</th>
          <th width="20%">Name</th>
          <th width="20%">SSID</th>
          <th width="12%">Operation<br/>Mode</th>
          <th width="12%">Bandwidth</th>
          <th width="12%">Channel</th>
          <th width="20%">IP Address</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-repeat="profile in profiles">
          <td><input type="checkbox" value="{{profile.id}}"/></td>
          <td>{{profile.name}}</td>
          <td>{{profile.ssid}}</td>
          <td>{{profile.opMode.label}}</td>
          <td>{{profile.bandwidth.label}}</td>
          <td>{{profile.channel}}</td>
          <td>{{profile.ipAddress}}</td>
        </tr>
        </tbody>
      </table>
    </div> <!-- column -->
  </div>

  </div> <!-- controller ends -->

</div> <!-- app ends -->

<script type="text/javascript" >
    var m1 = document.applyProfileForm.availableNodes;
    var m2 = document.applyProfileForm.selectedNodes;
</script>

<jsp:include page="/includes/bootstrap-footer.jsp" flush="false"/>
