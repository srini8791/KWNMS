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
    <jsp:param name="title" value="Tree View" />
    <jsp:param name="headTitle" value="Tree View" />
    <jsp:param name="location" value="TreeView" />
    <jsp:param name="breadcrumb" value="Tree View" />
</jsp:include>


<jsp:include page="/assets/load-assets.jsp" flush="false">
  <jsp:param name="asset" value="treeview/dist/themes/default/style.css"/>
  <jsp:param name="asset-type" value="css"/>
</jsp:include>

<jsp:include page="/assets/load-assets.jsp" flush="false">
  <jsp:param name="asset" value="onms-assets"/>
</jsp:include>

<div ng-app="onms-assets">
  <div id="treeviewCtrl" class="container-fluid" ng-controller="TreeViewCtrl" ng-init="init()">
    <div growl></div>
    <div class="row">
      <div class="col-md-12">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">Node Tree</h3>
            </div>
            <div class="panel-body">
              <div class="row">
                <div class="col-md-4">
                  <div id="nodetree"></div>
                </div>
                <div class="col-md-8">
                  <span id='nodeinfo'></span>
                </div>
              </div>
            </div>
        </div> <!-- panel ends -->
      </div>
    </div>
  </div>
</div>
<hr />

<script src="treeview/dist/jstree.js"></script>
<script>
$(document).ready(function() {

  $('#nodetree').jstree({
    'core': {
      'multiple': false,
      'data': function(node, callback) {
        if (node.id === '#') {
          callback([{'text': 'Global', 'id': '0', 'children': true, 'data': {'type': 'global'}}]);
        } else {
          var regions = [
                                {'text': 'South', 'id': '1', 'children': true, 'data': {'type': 'region'}},
                                {'text': 'North', 'id': '2', 'children': true, 'data': {'type': 'region'}}
                            ];
          callback(regions);
        }
      }
    }
  });

  $('#nodetree').on("open_node.jstree", function (e, data) {
    var nodeType = data.node.data ? data.node.data.type : 'global';
    var info = 'id: ' + data.node.id + ', type: ' + nodeType;
    $('#nodeinfo').text(info);
  });

  $('#nodetree').on("changed.jstree", function (e, data) {
    var nodeType = data.node.data ? data.node.data.type : 'global';
    var info = 'id: ' + data.node.id + ', type: ' + nodeType;
    $('#nodeinfo').text(info);
  });

});
</script>

<jsp:include page="/includes/bootstrap-footer.jsp" flush="false" />



