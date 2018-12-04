

dashboard.controller("ManagementController", ['$rootScope', '$scope', '$http', '$state', '$location', 'dashboardService', 'Flash',
  function ($rootScope, $scope, $http, $state, $location, dashboardService, Flash) {
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
      $scope.loadNodes("");
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

    $scope.addNodeOnMap = function(nodeObj) {
            if (nodeObj != null) {
                var latitude = nodeObj.assetRecord.latitude;
                var longitude = nodeObj.assetRecord.longitude;
                if (latitude == 0) {
                    latitude = 0.0;
                }
                if (longitude == 0) {
                    longitude = 0.0;
                }
                var myLatlng = new google.maps.LatLng(latitude, longitude);
                var marker = new google.maps.Marker({
                    map : $scope.map,
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
        }

        // functions related to Map end

    $scope.loadNodes = function(location) {
      var config = {
        params: {
          'limit': $scope.limit
        }
      };

      var url = 'api/v2/nodes';
      if (location && location != '') {
        url = 'api/v2/treeview/' + location + '/nodes';
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
          $scope.loadNodes($scope.mytree.currentNode.data.id).then(function(data) {
            $scope.vm.nodes = data;
            $scope.vm.totalNodesCount = data.length;
            $scope.showNodesOnMap();
          });

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
    'minimumFirmware': ''
  };
  $scope.nodeProfile = {};
  $scope.nodes = [];
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


