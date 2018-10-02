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
    m1len = m1.length;
    for (i = 0; i < m1len; i++) {
      if (m1.options[i].selected == true) {
        m2len = m2.length;
        m2.options[m2len] = new Option(m1.options[i].text, m1.options[i].value);
      }
    }
    for (i = (m1len - 1); i >= 0; i--) {
      if (m1.options[i].selected == true) {
        m1.options[i] = null;
      }
    }
  }

  function removeNodes() {
    m2len = m2.length;
    for (i = 0; i < m2len; i++) {
      if (m2.options[i].selected == true) {
        m1len = m1.length;
        m1.options[m1len] = new Option(m2.options[i].text, m2.options[i].value);
      }
    }
    for (i = (m2len - 1); i >= 0; i--) {
      if (m2.options[i].selected == true) {
        m2.options[i] = null;
      }
    }
  }


</script>

<div class="container-fluid" ng-app="onms-assets" ng-controller="ProfileCtrl" ng-init="init()">

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
                <label for="input_profileName" class="col-sm-4 control-label">Profile Name</label>
                <div class="col-sm-8">
                  <input id="input_profileName" name="profileName" ng-model="profile.name" class="form-control"/>
                </div>
              </div>
            </div>
            <div class="form-group">
              <div class="row">
                <label for="input_ssid" class="col-sm-4 control-label">SSID</label>
                <div class="col-sm-8">
                  <input id="input_ssid" name="ssid" ng-model="profile.ssid" class="form-control"/>
                </div>
              </div>
            </div>
            <div class="form-group">
              <div class="row">
                <label for="input_opMode" class="col-sm-4 control-label">Operation Mode</label>
                <div class="col-sm-8">
                  <select id="input_opMode" name="opMode" ng-model="profile.opMode.id" class="form-control">
                    <option value="0">Select Operation Mode</option>
                    <option value="1">11g</option>
                    <option value="2">11ng</option>
                    <option value="3">11a</option>
                    <option value="4">11na</option>
                    <option value="5">11ac</option>
                  </select>
                </div>
              </div>
            </div>
            <div class="form-group">
              <div class="row">
                <label for="input_bandwidth" class="col-sm-4 control-label">Bandwidth</label>
                <div class="col-sm-8">
                  <select id="input_bandwidth" name="bandwidth" ng-model="profile.bandwidth" class="form-control">
                    <option value="0">Select Bandwidth</option>
                    <option value="1">20MHz</option>
                    <option value="2">40MHz</option>
                    <option value="3">80MHz</option>
                  </select>
                </div>
              </div>
            </div>
            <div class="form-group">
              <div class="row">
                <label for="input_channel" class="col-sm-4 control-label">Channel</label>
                <div class="col-sm-8">
                  <input id="input_channel" name="channel" ng-model="profile.channel" class="form-control"/>
                </div>
              </div>
            </div>
            <div class="form-group">
              <div class="row">
                <label for="input_ipAddress" class="col-sm-4 control-label">IP Address</label>
                <div class="col-sm-8">
                  <input id="input_ipAddress" name="ipAddress" ng-model="profile.ipAddress" class="form-control"/>
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
          <form method="post" name="downloadProfile" onsubmit="return downloadProfile();">
            <div class="form-group">
              <div class="row">
                <label for="input_nodeId" class="col-sm-4 control-label">Select Device</label>
                <div class="col-sm-8">
                  <select id="input_nodeId" name="nodeId" class="form-control" required>
                    <option value="12">192.168.0.2</option>
                    <option value="15">192.168.0.5</option>
                  </select>
                </div>
              </div>
            </div>
          </form>
        </div> <!-- panel-body -->
        <div class="panel-footer">
          <button type="button" class="btn btn-default" ng-click="download()" id="download-profile">
            Retrieve Profile&nbsp;&nbsp;&nbsp;<span class="glyphicon glyphicon-save"><span>
          </button>
        </div> <!-- panel-footer -->
      </div> <!-- panel -->
      <div class="panel panel-default">
        <div class="panel-heading">
          <h3 class="panel-title">Apply Profile to Devices</h3>
        </div>
        <div class="panel-body">
          <form method="post" name="applyProfile">
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
                  <select class="form-control" multiple="multiple" name="availableNodes" size="10">
                    <option ng-repeat="node in nodes" value="{{node.id}}">{{node.label}}</option>
                  </select>
                  <button type="button" class="btn btn-default" id="nodes.doAdd" onClick="javascript:addNodes()">&nbsp;&#155;&#155;&nbsp; Add</button>
                </div>
                <div class="col-sm-4">
                  <label class="control-label">Selected Devices</label>
                  <select class="form-control" multiple="multiple" name="selectedNodes" ng-model="nodesToApply" size="10">
                  </select>
                  <button type="button" class="btn btn-default" id="nodes.doRemove" onClick="javascript:removeNodes()">&nbsp;&#139;&#139;&nbsp; Remove</button>
                </div>
              </div>
            </div>
          </form>
        </div> <!-- panel-body -->
        <div class="panel-footer">
          <button type="button" class="btn btn-default" ng-click="apply()" id="apply-profile">
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
          <td>{{profile.bandwidth}}</td>
          <td>{{profile.channel}}</td>
          <td>{{profile.ipAddress}}</td>
        </tr>
        </tbody>
      </table>
    </div> <!-- column -->
  </div>

</div>

<script type="text/javascript" >
    var m1 = document.applyProfile.availableNodes;
    var m2 = document.applyProfile.selectedNodes;
</script>

<jsp:include page="/includes/bootstrap-footer.jsp" flush="false"/>
