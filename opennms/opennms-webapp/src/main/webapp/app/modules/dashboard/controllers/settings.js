﻿
dashboard.controller("SettingsController", ['$rootScope', '$scope', '$mdDialog', '$http', '$state', '$location', 'dashboardService', 'Flash',
  function ($rootScope, $scope, $mdDialog, $http, $state, $location, dashboardService, Flash) {
    var vm = this;

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

    $scope.regions = [];
    $scope.newRegion = {
      'name': ''
    };

    $scope.nodes = [];
    $scope.profileToApply = '';
    $scope.nodesToApply = [];

    $scope.init = function() {
      $scope.loadRegions();
      $scope.loadProfiles();
      $scope.loadNodes();
    };

    $scope.loadRegions = function() {
      $http.get('api/v2/regions')
        .then(function(response) {
          if (response.data) {
            $scope.regions = response.data.region;
            console.log('regions = ' + $scope.regions);
          }
        }
      );
    }

    $scope.loadProfiles = function() {
      $http.get('api/v2/profiles')
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

    $scope.saveRegion = function() {
      $http({
        method: 'POST',
        url: 'api/v2/regions',
        headers: {'Content-Type': 'application/json'},
        data: $scope.newRegion
      }).success(function() {
        $scope.showAlert('success', 'The region has been created successfully.');
        $scope.newRegion = {};
        $scope.loadRegions();
      }).error(function(msg) {
        $scope.showAlert('error', 'Error creating the region: ' + msg);
      });
    };

    $scope.loadNodes = function() {
      $http.get('api/v2/nodes')
        .success(function(result) {
          $scope.nodes = result.node;
        })
        .error(function(msg) {
          $scope.showAlert('error', msg);
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



  }
]);
