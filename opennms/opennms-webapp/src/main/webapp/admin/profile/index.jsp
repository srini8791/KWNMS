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
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

--%>
<jsp:include page="/includes/bootstrap.jsp" flush="false">
    <jsp:param name="norequirejs" value="true" />

    <jsp:param name="title" value="Profile Management" />
    <jsp:param name="headTitle" value="Profile Management" />
    <jsp:param name="breadcrumb" value="<a href='admin/index.jsp'>Admin</a>" />
    <jsp:param name="breadcrumb" value="Profile Management" />
</jsp:include>

<jsp:include page="/assets/load-assets.jsp" flush="false">
  <jsp:param name="asset" value="onms-assets" />
</jsp:include>


<div class="container-fluid" ng-app="onms-assets" ng-controller="ProfileCtrl">

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
                  <input id="input_profileName" name="profileName" ng-model="profile.name" class="form-control" />
                </div>
              </div>
            </div>
            <div class="form-group">
              <div class="row">
                <label for="input_ssid" class="col-sm-4 control-label">SSID</label>
                <div class="col-sm-8">
                  <input id="input_ssid" name="ssid" ng-model="profile.ssid" class="form-control" />
                </div>
              </div>
            </div>
            <div class="form-group">
              <div class="row">
                <label for="input_opMode" class="col-sm-4 control-label">Operation Mode</label>
                <div class="col-sm-8">
                  <select id="input_opMode" name="opMode" ng-model="profile.opMode" class="form-control">
                    <option value="1">Outdoor Base</option>
                    <option value="2">Outdoor Subscriber</option>
                    <option value="3">Access Point</option>
                    <option value="4">Client</option>
                  </select>
                </div>
              </div>
            </div>
            <div class="form-group">
              <div class="row">
                <label for="input_bandwidth" class="col-sm-4 control-label">Bandwidth</label>
                <div class="col-sm-8">
                  <input id="input_bandwidth" name="bandwidth" ng-model="profile.bandwidth" class="form-control" />
                </div>
              </div>
            </div>
            <div class="form-group">
              <div class="row">
                <label for="input_channel" class="col-sm-4 control-label">Channel</label>
                <div class="col-sm-8">
                  <input id="input_channel" name="channel" ng-model="profile.channel" class="form-control" />
                </div>
              </div>
            </div>
            <div class="form-group">
              <div class="row">
                <label for="input_ipAddress" class="col-sm-4 control-label">IP Address</label>
                <div class="col-sm-8">
                  <input id="input_ipAddress" name="ipAddress" ng-model="profile.ipAddress" class="form-control" />
                </div>
              </div>
            </div>
          </form>
        </div> <!-- panel-body -->
        <div class="panel-footer">
          <button type="button" class="btn btn-default" ng-click="save()" id="save-profile" ng-disabled="profileForm.$invalid">
            Create Profile&nbsp;&nbsp;&nbsp;<span class="glyphicon glyphicon-save"><span>
          </button>
        </div> <!-- panel-footer -->
      </div> <!-- panel -->
    </div> <!-- column -->
    <div class="col-md-6">
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
          <a href="javascript:retrieveProfile()">Retrieve Profile >>></a>
        </div> <!-- panel-footer -->
      </div> <!-- panel -->
      <div class="panel panel-default">
        <div class="panel-heading">
          <h3 class="panel-title">Apply Profile to Device</h3>
        </div>
        <div class="panel-body">
          <form method="post" name="applyProfile" onsubmit="return applyProfile();">
            <div class="form-group">
              <div class="row">
                <label for="input_profileName" class="col-sm-4 control-label">Select Profile</label>
                <div class="col-sm-8">
                  <select id="input_profileName" name="profileName" class="form-control" required>
                    <option value="9100-52mbps">9100-52mbps</option>
                    <option value="9100R-4mbps">9100R-4mbps</option>
                  </select>
                </div>
              </div>
            </div>
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
          <a href="javascript:applyProfile()">Apply Profile >>></a>
        </div> <!-- panel-footer -->
      </div> <!-- panel -->
    </div> <!-- column -->
  </div>

</div>

<jsp:include page="/includes/bootstrap-footer.jsp" flush="false"/>
