/**
* @author Alejandro Galue <agalue@opennms.org>
* @copyright 2016 The OpenNMS Group, Inc.
*/

'use strict';

const angular = require('vendor/angular-js');
const bootbox = require('vendor/bootbox-js');
require('lib/onms-http');

const defaultConfig = require('./config.json');

angular.module('onms-assets', [
  'onms.http',
  'ui.bootstrap',
  'angular-growl'
])

.config(['growlProvider', function(growlProvider) {
  growlProvider.globalTimeToLive(3000);
  growlProvider.globalPosition('bottom-center');
}])

.config(['$uibTooltipProvider', function($uibTooltipProvider) {
  $uibTooltipProvider.setTriggers({
    'mouseenter': 'mouseleave'
  });
  $uibTooltipProvider.options({
    'placement': 'right',
    'trigger': 'mouseenter'
  });
}])

.controller('NodeAssetsCtrl', ['$scope', '$http', '$q', 'growl', 'uibDateParser', function($scope, $http, $q, growl, uibDateParser) {

  $scope.blackList = [ 'id', 'lastModifiedDate', 'lastModifiedBy', 'lastCapsdPoll', 'createTime' ];
  $scope.infoKeys = [ 'sysObjectId', 'sysName', 'sysLocation', 'sysContact', 'sysDescription', 'macAddress', 'provisioned', 'ssid', 'radioMode', 'opMode', 'bandwidth', 'channel' ];
  $scope.dateKeys = [ 'dateInstalled', 'leaseExpires', 'maintContractExpiration' ];

  $scope.dateFormat = 'yyyy-MM-dd';

  $scope.config = {};
  $scope.master = {};
  $scope.asset = {};
  $scope.suggestions = {};
  /*
  $scope.nodeId;
  $scope.nodeLabel;
  $scope.foreignSource;
  $scope.foreignId;
  */

  $scope.init = function(nodeId) {
    $scope.nodeId = nodeId;
    $scope.config = defaultConfig;
    $http.get('rest/nodes/' + $scope.nodeId)
      .success(function(node) {
        $scope.nodeLabel = node.label;
        $scope.foreignSource = node.foreignSource;
        $scope.foreignId = node.foreignId;
        angular.forEach($scope.dateKeys, function(key) {
          node.assetRecord[key] = uibDateParser.parse(node.assetRecord[key], $scope.dateFormat);
        });
        $scope.master = angular.copy(node.assetRecord);
        $scope.asset = angular.copy(node.assetRecord);
        angular.forEach($scope.infoKeys, function(k) {
          $scope.asset[k] = node[k];
        });
      })
      .error(function(msg) {
        growl.error(msg);
      });
    $http.get('rest/assets/suggestions')
      .success(function(suggestions) {
        $scope.suggestions = suggestions
      })
      .error(function(msg) {
        growl.error(msg);
      });
  };

  $scope.getSuggestions = function(field) {
    if ($scope.suggestions[field]) {
      return $scope.suggestions[field].suggestion;
    }
    return [];
  };

  $scope.reset = function() {
    $scope.asset = angular.copy($scope.master);
    $scope.assetForm.$setPristine();
  };

  $scope.save = function() {
    var target = {};
    for (var k in $scope.asset) {
      if ($scope.infoKeys.indexOf(k) === -1 && $scope.blackList.indexOf(k) === -1 && $scope.asset[k] !== '' && $scope.asset[k] !== null) {
        target[k] = $scope.dateKeys.indexOf(k) === -1 ? $scope.asset[k] : uibDateParser.filter($scope.asset[k], $scope.dateFormat);
      }
    }
    //console.log('Assets to save: ' + angular.toJson(target));
    $http({
      method: 'PUT',
      url: 'rest/nodes/' + $scope.nodeId + '/assetRecord',
      headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      data: $.param(target)
    }).success(function() {
      growl.success('The asset record has been successfully updated.');
      $scope.checkRequisition(target);
    }).error(function(msg) {
      growl.error('Cannot update the asset record: ' + msg);
    });
  };

  $scope.checkRequisition = function(assets) {
    if ($scope.foreignSource && $scope.foreignId) {
      bootbox.confirm('This node belongs to the requisition ' + $scope.foreignSource + '.<br/> It is recommended to update the requisition with your asset fields, but all the existing fields will be overriden.<br/> Do you want to do that ?', function(ok) {
        if (ok) {
          $scope.updateRequisition(assets);
        }
      });
    }
  };

  $scope.updateRequisition = function(assets) {
    var assetFields = [];
    for (var key in assets) {
      if (assets.hasOwnProperty(key)) {
        assetFields.push({ name: key, value: assets[key] });
      }
    }
    $http.get('rest/requisitions/' + $scope.foreignSource + '/nodes/' + $scope.foreignId)
      .success(function(node) {
        node.asset = assetFields;
        $http.post('rest/requisitions/' + $scope.foreignSource + '/nodes', node)
          .success(function() {
            growl.success('Requisition ' + $scope.foreignSource + ' has been updated.');
          })
          .error(function() {
            growl.error('Cannot update requisition ' + $scope.foreignSource);
          });
      })
      .error(function() {
        growl.error('Cannot obtain node data from requisition ' + $scope.foreignSource);
      });
  };

}])

.controller('ProfileCtrl', ['$scope', '$http', 'growl', function($scope, $http, growl) {

  $scope.profiles = [];
  $scope.newProfile = {
    'name': '',
    'ssid': '',
    'opMode': {
      'id': ''
    },
    'bandwidth': {
      'id': ''
    },
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
        growl.error(msg);
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
          growl.success('Successfully retrieved profile to server.');
        }
      })
      .catch(function(response) {
        if (response.data['errorMessage']) {
          growl.error(response.data['errorMessage']);
        } else {
          growl.error('Error retrieving profile.');
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
          growl.error(response.data['errorMessage']);
        } else {
          growl.error('Error retrieving profile.');
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
      growl.success('The profile has been created successfully.');
      $scope.newProfile = {};
      $scope.loadProfiles();
    }).error(function(msg) {
      growl.error('Cannot create the profile: ' + msg);
    });
  };

  $scope.loadNodes = function() {
    $http.get('api/v2/nodes')
      .success(function(result) {
        $scope.nodes = result.node;
      })
      .error(function(msg) {
        growl.error(msg);
      });
  };

  $scope.apply = function() {
    if ($scope.profileToApply === '' || $scope.nodesToApply.length === 0) {
      growl.error('Select a profile and nodes to apply');
      return;
    }

    $http({
      method: 'POST',
      url: 'api/v2/profiles/' + $scope.profileToApply + '/applyToNodes',
      headers: {'Content-Type': 'application/json'},
      data: $scope.nodesToApply
    }).success(function() {
      growl.success('The profile has been initiated to apply for the selected nodes.');
      $scope.profileToApply = '';
      $scope.nodesToApply = [];
    }).error(function(msg) {
      growl.error('Cannot apply the profile: ' + msg);
    });
  };



}])

.controller('InventoryCtrl', ['$scope', '$http', 'growl', function($scope, $http, growl) {

  $scope.nodes = [];
  $scope.limit = 10;
  $scope.totalNodesCount = 0;
  $scope.activeNodesCount = 0;

  $scope.init = function() {
    $scope.loadNodes();
    $scope.loadActiveNodesCount();
  };

  $scope.loadNodes = function() {
    var configObj = {
      'params': {
        'limit': $scope.limit
      }
    };

    $http.get('api/v2/nodes', configObj)
      .success(function(result) {
        $scope.totalNodesCount = result.totalCount;
        $scope.nodes = result.node;
      })
      .error(function(msg) {
        growl.error(msg);
      });
  };

  $scope.loadActiveNodesCount = function() {
    $http.get('api/v2/nodes/active/count')
      .success(function(result) {
        $scope.activeNodesCount = result;
      })
      .error(function(msg) {
        growl.error(msg);
      });
  };

}]);


