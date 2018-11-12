
dashboard.controller("SettingsController", ['$rootScope', '$scope', '$http', '$state', '$location', 'dashboardService', 'Flash',
  function ($rootScope, $scope, $http, $state, $location, dashboardService, Flash) {
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

    $scope.nodes = [];
    $scope.profileToApply = '';
    $scope.nodesToApply = [];

    $scope.init = function() {
      $scope.loadProfiles();
      $scope.loadNodes();
    };

    $scope.loadProfiles = function() {
      $http.get('api/v2/profiles')
        .success(function(result) {
          $scope.profiles = result.profile;
        })
        .error(function(msg) {
          $scope.showMessage('error', msg);
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
            $scope.showMessage('success', 'Successfully retrieved profile to server.');
          }
        })
        .catch(function(response) {
          if (response.data['errorMessage']) {
            $scope.showMessage('error', response.data['errorMessage']);
          } else {
            $scope.showMessage('error', 'Error retrieving profile.');
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
            $scope.newProfile.opMode.id = response.data['opModeId'];
            $scope.newProfile.bandwidth.id = response.data['bandwidthId'];
            $scope.newProfile.channel = response.data['channel'];
          }
        })
        .catch(function(response) {
          if (response.data['errorMessage']) {
            $scope.showMessage('error', response.data['errorMessage']);
          } else {
            $scope.showMessage('error', 'Error retrieving profile.');
          }
        });
    };

    $scope.save = function() {
      $http({
        method: 'POST',
        url: 'api/v2/profiles',
        headers: {'Content-Type': 'application/json'},
        data: $scope.newProfile
      }).success(function() {
        $scope.showMessage('success', 'The profile has been created successfully.');
        $scope.newProfile = {};
        $scope.loadProfiles();
      }).error(function(msg) {
        $scope.showMessage('error', 'Cannot create the profile: ' + msg);
      });
    };

    $scope.loadNodes = function() {
      $http.get('api/v2/nodes')
        .success(function(result) {
          $scope.nodes = result.node;
        })
        .error(function(msg) {
          $scope.showMessage('error', msg);
        });
    };

    $scope.apply = function() {
      if ($scope.profileToApply === '' || $scope.nodesToApply.length === 0) {
        $scope.showMessage('error', 'Select a profile and nodes to apply');
        return;
      }

      $http({
        method: 'POST',
        url: 'api/v2/profiles/' + $scope.profileToApply + '/applyToNodes',
        headers: {'Content-Type': 'application/json'},
        data: $scope.nodesToApply
      }).success(function() {
        $scope.showMessage('success', 'The profile has been initiated to apply for the selected nodes.');
        $scope.profileToApply = '';
        $scope.nodesToApply = [];
      }).error(function(msg) {
        $scope.showMessage('error', 'Cannot apply the profile: ' + msg);
      });
    };


    $scope.showMessage = function(msgType, msg) {
      var bgColor = msgType === 'error' ? 'red' : 'blue';
      $('#msgBox').html('');
      $('#msgBox').html(msg).css('background-color', bgColor);
      $("#msgBox").show().delay(2000).fadeOut();
    };

  }
]);
