

dashboard.controller("ManagementController", ['$rootScope', '$scope', '$mdDialog', '$http', '$state', '$location', 'dashboardService', 'Flash',
  function ($rootScope, $scope, $mdDialog, $http, $state, $location, dashboardService, Flash) {
    var vm = this;

    $scope.limit = 10;
    $scope.vm.nodes = [];
    $scope.vm.totalNodesCount = 0;
    $scope.vm.activeNodesCount = 0;

    // variables related to map
    // start
    $scope.map = null;
    $scope.markers = [];
    var infoWindow = new google.maps.InfoWindow();
    // end



    $scope.init = function() {
      $scope.loadProfiles();
      $scope.buildTree();
      $scope.loadAllNodes();
      $scope.loadManagementNodes();
      $scope.loadActiveNodesCount();
      $scope.prepareMap();
    }

    $scope.loadTreeRegions = function() {
      return $http.get('api/v2/treeview/regions')
        .then(function(response) {
          if (response.data) {
            return response.data;
          }
        }
      );
    }

    $scope.loadTreeLocations = function(regionId) {
      return $http.get('api/v2/treeview/regions/' + regionId + '/locations')
        .then(function(response) {
          if (response.data) {
            return response.data;
          }
        }
      );
    }

    $scope.loadTreeNodes = function(location) {
      return $http.get('api/v2/treeview/location/' + location + '/nodes')
        .then(function(response) {
          if (response.data) {
            return response.data;
          }
        }
      );
    }



    // functions related to Map start
    $scope.prepareMap = function() {
        var mapOptions = {
            zoom : 9,
            center : new google.maps.LatLng('17.4938439','78.428827'),
            mapTypeId : google.maps.MapTypeId.ROADMAP
        }
        $scope.map = new google.maps.Map(document.getElementById('map'), mapOptions);
    }

    $scope.showNodesOnMap = function() {
        if ($scope.vm.totalNodesCount > 0) {
            for(var i = 0; i<$scope.vm.totalNodesCount;i++) {
                $scope.addNodeOnMap($scope.vm.nodes[i]);
            }
        }
    }


    $scope.clearMarkers = function() {
        $scope.setMapOnAll(null);
    }

    $scope.setMapOnAll = function(map) {
        for (var i = 0; i < $scope.markers.length; i++) {
          $scope.markers[i].setMap(map);
        }
    }

    $scope.setMapOnSingleMarker = function(nodeId) {

        for (var i = 0; i < $scope.markers.length; i++) {
            var nId = $scope.markers[i].id;
            if (nodeId == $scope.markers[i].id) {
                $scope.markers[i].setMap($scope.map);
                google.maps.event.trigger($scope.markers[i], 'click');
            }

        }
    }

    $scope.addNodeOnMap = function(nodeObj) {
            if (nodeObj != null) {
                var latitude = nodeObj.assetRecord.latitude;
                var longitude = nodeObj.assetRecord.longitude;
                if ((latitude != undefined || latitude != null)&& (longitude != undefined || longitude != null)) {
                    var myLatlng = new google.maps.LatLng(latitude, longitude);
                                    var marker = new google.maps.Marker({
                                        map : $scope.map,
                                        id : nodeObj.id,
                                        position: myLatlng,
                                        title: nodeObj.primaryIP,
                                        ssid:nodeObj.ssid
                                    });
                                    google.maps.event.addListener(marker,'click',function() {
                                        infoWindow.setContent( '<b>' + marker.title + '</b>'
                                            +'<p><b>Address:</b>'
                                            + marker.title
                                            + '<br/><b>SSID:</b>' + marker.ssid
                                            + '</p>'
                                            );
                                        infoWindow.open($scope.map,marker);

                                    });
                                    //marker.setMap($scope.map);
                                    $scope.markers.push(marker);
                }
                /*if (latitude == 0) {
                    latitude = 0.0;
                }
                if (longitude == 0) {
                    longitude = 0.0;
                }*/

            }
        }

    $scope.nodes = [];
    $scope.loadAllNodes = function() {
      var config = {
        params: {
          'limit': $scope.limit,
          '_s': 'productCode==ptp,productCode==ptmp,productCode==indoorap,productCode==outdoorap'
        }
      };

      var url = 'api/v2/nodes';
      $http.get(url, config)
        .then(function(response) {
          if (response.data) {
            $scope.nodes = response.data.node;
          }
        }
      );
    }


    $scope.showNode = function(nodeId,lastView) {
            $state.go('app.nodeView',{ "nodeId": nodeId,"lastView":lastView});
    }

        // functions related to Map end

    $scope.loadNodeById = function(nodeId) {
      var url = 'api/v2/nodes/' + nodeId;
      return $http.get(url)
        .then(function(response) {
          if (response.data) {
            return response.data;
          }
        }
      );
    }

    $scope.loadFacilityNodes = function(facilityId) {
      var url = url = 'api/v2/treeview/facility/' + facilityId + '/nodes';
      return $http.get(url)
        .then(function(response) {
          if (response.data) {
            return response.data;
          }
        }
      );
    }

    $scope.loadNodes = function(loc) {
      var config = {
        params: {
          'limit': $scope.limit,
          '_s': 'productCode==ptp,productCode==ptmp,productCode==indoorap,productCode==outdoorap'
        }
      };

      var url = 'api/v2/nodes';
      if (loc && loc != '') {
        url = 'api/v2/treeview/' + loc + '/nodes';
      }
      return $http.get(url, config)
        .then(function(response) {
          if (response.data) {
            /*$scope.vm.totalNodesCount = response.data.length;
            $scope.vm.nodes = response.data;*/
            return response.data;

          }
        }
      );
    }

    $scope.loadManagementNodes = function() {
      var config = {
        params: {
          'limit': $scope.limit,
          '_s': '(productCode==ptp,productCode==ptmp,productCode==indoorap,productCode==outdoorap)'
        }
      };

      var url = 'api/v2/nodes';

      $http.get(url, config)
        .then(function(response) {
          if (response.data) {
            $scope.vm.totalNodesCount = response.data.count;
            $scope.vm.nodes = response.data.node;
          }
        });
    }

    $scope.loadActiveNodesCount = function() {
      $http.get('api/v2/nodes/active/count')
        .then(function(response) {
          if (response.data) {
            $scope.vm.activeNodesCount = response.data;
          }
        }
      );
    }

    $scope.buildTree = function() {
      $scope.loadTreeRegions();
      $scope.treedata = [
        { 'text' : 'Global', 'data' : {'id': '-1', 'type': 'root'}, 'children': [] }
      ];
    }

    $scope.$watch('mytree.currentNode', function( newObj, oldObj ) {
      if( $scope.mytree && angular.isObject($scope.mytree.currentNode) ) {
        var nodeType = $scope.mytree.currentNode.data.type;
        if (nodeType === 'root') { // Global
          $scope.loadTreeRegions().then(function(data) {
            $scope.mytree.currentNode.children = data;
          });
        } else if (nodeType === 'region') { // area location -- for eg: geometrical boundary
          $scope.loadTreeLocations($scope.mytree.currentNode.data.id).then(function(data) {
            $scope.mytree.currentNode.children = data;
          });
        } else if (nodeType === 'location') { // location -- facility or locations under region
        $scope.clearMarkers();
        $scope.markers = [];
         $scope.loadFacilityNodes($scope.mytree.currentNode.data.id).then(function(data) {
                     $scope.mytree.currentNode.children = data;
         });
         $scope.loadNodes($scope.mytree.currentNode.data.id).then(function(data) {
            $scope.vm.nodes = data;
            //$scope.vm.totalNodesCount = data.length;
            $scope.showNodesOnMap();
         });
        } else if (nodeType === 'node') {
            $scope.clearMarkers();
            var id = $scope.mytree.currentNode.data.id;
            $scope.setMapOnSingleMarker(id);
          /*$scope.loadNodeById($scope.mytree.currentNode.data.id).then(function(data) {
            $scope.addNodeOnMap(data);
          });*/
        }
      }
    }, false);

/* PROFILES -- START */
  $scope.profiles = [];
  $scope.newProfile = {
    'name': '',
    'ssid': '',
    'opMode': '',
    'bandwidth': '',
    'channel': '',
    'minimumFirmware': '',
    'nmsServerAddress':'',
    'tftpServerAddress':''
  };
  $scope.nodeProfile = {};
  $scope.profileToApply = '';
  $scope.nodesToApply = [];

  $scope.loadProfiles = function() {
    var paramObj = { 'params': {'limit': 0} };
    $http.get('api/v2/profiles', paramObj)
      .success(function(result) {
        $scope.profiles = result.profile;
      })
      .error(function(msg) {
        $scope.showAlert('error', msg);
      });
  };

  $scope.retrieveProfile = function(nodeId) {
    var configObj = {
      'params': {
        'nodeId': nodeId
      }
    };

    $http.get('api/v2/profiles/retrieveProfile', configObj)
      .then(function(response) {
        if (response.status === 200) {
          $scope.showAlert('success', 'Successfully retrieved profile to server.');
        }
      })
      .catch(function(response) {
        if (response.data['errorMessage']) {
          $scope.showAlert('error', response.data['errorMessage']);
        } else {
          $scope.showAlert('error', 'Error retrieving profile.');
        }

      });
  };

  $scope.populateFormWithProfile = function(nodeId) {
    var configObj = {
      'params': {
        'nodeId': nodeId
      }
    };

    $http.get('api/v2/profiles/extractProfile', configObj)
      .then(function(response) {
        if (response.data) {
          $scope.newProfile.ssid = response.data['ssid'];
          $scope.newProfile.opMode = response.data['opModeId'];
          $scope.newProfile.bandwidth = response.data['bandwidthId'];
          $scope.newProfile.channel = response.data['channel'];
        }
      })
      .catch(function(response) {
        if (response.data['errorMessage']) {
          $scope.showAlert('error', response.data['errorMessage']);
        } else {
          $scope.showAlert('error', 'Error retrieving profile.');
        }
      });
  };

  $scope.saveProfile = function() {
    $http({
      method: 'POST',
      url: 'api/v2/profiles',
      headers: {'Content-Type': 'application/json'},
      data: $scope.newProfile
    }).success(function() {
      $scope.showAlert('success', 'The profile has been created successfully.');
      $scope.newProfile = {};
      $scope.loadProfiles();
    }).error(function(msg) {
      $scope.showAlert('error', 'Error creating the profile: ' + msg);
    });
  };

  $scope.apply = function() {
    if ($scope.profileToApply === '' || $scope.nodesToApply.length === 0) {
      $scope.showAlert('error', 'Select a profile and nodes to apply');
      return;
    }

    $http({
      method: 'POST',
      url: 'api/v2/profiles/' + $scope.profileToApply + '/applyToNodes',
      headers: {'Content-Type': 'application/json'},
      data: $scope.nodesToApply
    }).success(function() {
      $scope.showAlert('success', 'The profile has been initiated to apply for the selected nodes.');
      $scope.profileToApply = '';
      $scope.nodesToApply = [];
    }).error(function(msg) {
      $scope.showAlert('error', 'Cannot apply the profile: ' + msg);
    });
  };


  $scope.deleteProfile = function(profileId) {
    $http({
      method: 'DELETE',
      url: 'api/v2/profiles/' + profileId
    }).success(function() {
      $scope.showAlert('success', 'The profile has been deleted successfully.');
      $scope.loadProfiles();
    }).error(function(msg) {
      $scope.showAlert('error', 'Error deleting the profile: ' + msg);
    });
  };


  $scope.showAlert = function(msgType, msg, ev) {
    $mdDialog.show(
      $mdDialog.alert()
        .parent(angular.element(document.querySelector('#popupContainer')))
        .clickOutsideToClose(true)
        .title(msgType)
        .textContent(msg)
        .ariaLabel('Alert')
        .ok('OK')
        .targetEvent(ev)
    );
  };


  $scope.deleteConfirm = function(profileId, ev) {
    var confirm = $mdDialog.confirm()
          .title('Are you sure you want to delete this profile?')
          .targetEvent(ev)
          .ok('Yes')
          .cancel('No');

    $mdDialog.show(confirm).then(function() {
      $scope.deleteProfile(profileId);
    });
  };

/* PROFILES -- END */



  }

]);

dashboard.filter('showProductType', function() {
  return function(item) {
    if (item == undefined) {
      return 'Unknown';
    } else if (item === 'indoorap' || item === 'outdoorap') {
      return 'Access Point';
    } else {
      return item.toUpperCase();
    }
  }
});


