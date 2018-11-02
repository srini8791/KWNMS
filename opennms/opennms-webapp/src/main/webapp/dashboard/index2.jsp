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
<%@page import="java.util.Collection"%>
<%@page import="org.opennms.web.navigate.PageNavEntry"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.opennms.core.soa.ServiceRegistry"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="org.opennms.netmgt.config.NotifdConfigFactory"%>


<style>
  .panel-stats {
      border: 1px solid #c0c0c0;
      box-shadow: 2px 2px 2px;
  }
  
  .stats {
    font-weight: bold;
    font-size: 12px;
    cursor: pointer;
  }
  
  .stats-active {
    color: green;
  }

  .stats-down {
    color: red;
  }
  
  .stats-prov {
    color: blue;
  }

  .notice {
    color: #333;
    margin-bottom: 4px;
    padding-left: 4px;
  }

  .notice-critical {
    border-left: 4px solid rgb(255, 0, 0);
  }

  .notice-major {
    border-left: 4px solid rgb(215, 153, 0);
  }

  .notice-minor {
    border-left: 4px solid rgb(255, 204, 51);
  }

  .table-summary td {
    text-align: right;
    border-top: none !important;
  }

  .table-summary td.title {
    text-align: right;
    border-top: 1px dotted #c0c0c0 !important;
    border-bottom: 1px dotted #c0c0c0;
  }

  .menu {
    margin-bottom: 20px;
  }

</style>
<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.2/Chart.js"></script>


<div class="row">
  <div class="col-md-1">
    <div id="menubar" class="container">
      <div class="menu">
        <a href="#">
          <i class="fa fa-tachometer fa-4x"></i>
          <br/><span>Dashboard</span>
        </a>
      </div>
      <div class="menu">
        <a href="#">
          <i class="fa fa-sitemap fa-4x"></i>
          <br/><span>Topology</span>
        </a>
      </div>
      <div>
        <a href="#">
          <i class="fa fa-bar-chart fa-4x"></i>
          <br/><span>Reports</span>
        </a>
      </div>
    </div>
  </div>
  <div class="col-md-11">

    <jsp:include page="/includes/bootstrap2.jsp" flush="false" >
        <jsp:param name="title" value="Dashboard" />
        <jsp:param name="headTitle" value="Dashboard" />
        <jsp:param name="location" value="dashboard" />
        <jsp:param name="breadcrumb" value="Dashboard" />
        <jsp:param name="quiet" value="true" />
    </jsp:include>

    <div class="row">
        <div class="col-md-3">
            <div class="panel panel-stats">
                <div class="panel-heading">
                    <h3 class="panel-title">Nodes</h3>
                </div>
                <div class="panel-body">
                    <table class="table table-summary">
                        <tr>
                            <td class="stats" colspan="4">Total Nodes</td>
                            <td class="stats stats-total">477</td>
                        </tr>
                        <tr>
                            <td class="title"></td>
                            <td class="title"><b>Active</b></td>
                            <td class="title"><b>Down</b></td>
                            <td class="title"><b>Provisioned</b></td>
                            <td class="title"><b>Total</b></td>
                        </tr>
                        <tr>
                            <td class="stats">BSU</td>
                            <td class="stats stats-active">40</td>
                            <td class="stats stats-down">20</td>
                            <td class="stats stats-prov">28</td>
                            <td class="stats stats-total">88</td>
                        </tr>
                        <tr>
                            <td class="stats">SU</td>
                            <td class="stats stats-active">60</td>
                            <td class="stats stats-down">40</td>
                            <td class="stats stats-prov">48</td>
                            <td class="stats stats-total">148</td>
                        </tr>
                        <tr>
                            <td class="stats">AP</td>
                            <td class="stats stats-active">80</td>
                            <td class="stats stats-down">60</td>
                            <td class="stats stats-prov">64</td>
                            <td class="stats stats-total">204</td>
                        </tr>
                        <tr>
                            <td class="stats">Others</td>
                            <td class="stats stats-active">20</td>
                            <td class="stats stats-down">6</td>
                            <td class="stats stats-prov">11</td>
                            <td class="stats stats-total">37</td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="panel panel-stats">
                <div class="panel-heading">
                    <h3 class="panel-title">Monitoring</h3>
                </div>
                <div class="panel-body">
                    <div class="row">
                        <div class="col-md-6">
                            <canvas id="myIssues"></canvas>
                        </div>
                        <div class="col-md-6">
                            <canvas id="myDevices"></canvas>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <canvas id="myPerformance"></canvas>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="panel panel-stats">
                <div class="panel-heading">
                    <h3 class="panel-title">Alerts</h3>
                </div>
                <div class="panel-body">
                    <div class="notice notice-minor">
                        <span class="notice date">11/09/18&nbsp;16:27:41</span><br/>
                        <div class="notice message">The SSH outage on interface 192.168.0.33 has been cleared. Service is restored.</div>
                    </div>
                    <div class="notice notice-critical">
                        <span class="notice date">11/09/18&nbsp;16:26:35</span>
                        <div class="notice message">SSH outage identified on interface 192.168.0.33.</div>
                    </div>
                    <div class="notice notice-major">
                        <span class="notice date">11/09/18&nbsp;16:25:35</span>
                        <div class="notice message">Node System-Name is down.</div>
                    </div>
                    <div class="notice notice-critical">
                        <span class="notice date">11/09/18&nbsp;16:26:35</span>
                        <div class="notice message">SSH outage identified on interface 192.168.0.33.</div>
                    </div>
                    <div class="notice notice-major">
                        <span class="notice date">11/09/18&nbsp;16:25:35</span>
                        <div class="notice message">Node System-Name is down.</div>
                    </div>
                    <div class="notice notice-critical">
                        <span class="notice date">11/09/18&nbsp;16:26:35</span>
                        <div class="notice message">SSH outage identified on interface 192.168.0.33.</div>
                    </div>
                    <div class="notice notice-major">
                        <span class="notice date">11/09/18&nbsp;16:25:35</span>
                        <div class="notice message">Node System-Name is down.</div>
                    </div>
                </div>
            </div>
        </div>
    </div>

  </div>
</div>



<script>
  $(document).ready(function() {
    // ISSUES PIE CHART
    var ctx = document.getElementById("myIssues").getContext('2d');
    var myIssues = new Chart(ctx, {
			type: 'pie',
			data: {
				datasets: [{
					data: [80, 40, 240],
					backgroundColor: [
						'rgb(255, 0, 0)',
						'rgb(215, 153, 0)',
						'rgb(255, 204, 51)'
					],
					label: 'By Criticality'
				}],
				labels: [
					'Critical',
					'Major',
					'Minor'
				]
			},
			options: {
			  title: {
			    display: true,
			    text: 'Issues by Severity'
			  }
			}
	  });

    // DEVICE TYPES PIE CHART
    var ctx = document.getElementById("myDevices").getContext('2d');
    var myDevices = new Chart(ctx, {
			type: 'pie',
			data: {
				datasets: [{
					data: [322, 155],
					backgroundColor: [
						'rgb(255, 99, 132)',
						'rgb(255, 205, 86)'
					],
					label: 'By Device Type'
				}],
				labels: [
					'KeyWest',
					'Others'
				]
			},
			options: {
			  title: {
			    display: true,
			    text: 'Devices by Type'
			  }
			}
	  });

    // SYSTEM PERFORMANCE LINE CHART
    var timeFormat = 'MM/DD/YYYY HH:mm';
    var ctx = document.getElementById("myPerformance").getContext('2d');
    var myPerformance = new Chart(ctx, {
			type: 'line',
			data: {
				datasets: [{
				  label: 'Heap Memory',
					data: [121, 145, 213, 254, 62, 81, 101, 127, 198, 68],
					fill: false
				}],
				labels: [
				  '02:00',
				  '04:00',
				  '06:00',
				  '08:00',
				  '10:00',
				  '12:00',
				  '14:00',
				  '16:00',
				  '18:00',
				  '20:00'
				]
			},
			options: {
			  title: {
					text: 'NMS Memory Usage',
					display: true
				},
				scales: {
					xAxes: [{
						scaleLabel: {
							display: true,
							labelString: 'Time'
						}
					}],
					yAxes: [{
						scaleLabel: {
							display: true,
							labelString: 'Memory'
						}
					}]
				},
			}
	  });

  });
</script>
