

dashboard.controller("ManagementController", ['$rootScope', '$scope', '$http', '$state', '$location', 'dashboardService', 'Flash',
  function ($rootScope, $scope, $http, $state, $location, dashboardService, Flash) {
    var vm = this;

    $scope.limit = 10;
    $scope.vm.nodes = [];
    $scope.vm.totalNodesCount = 0;
    $scope.vm.activeNodesCount = 0;

    $scope.init = function() {
      $scope.buildTree();
      $scope.loadNodes("");
      $scope.loadActiveNodesCount();
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
      $http.get(url, config)
        .then(function(response) {
          if (response.data) {
            $scope.vm.totalNodesCount = response.data.totalCount;
            $scope.vm.nodes = response.data.node;
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
        if (nodeType === 'root') {
          $scope.loadTreeRegions().then(function(data) {
            $scope.mytree.currentNode.children = data;
          });
        } else if (nodeType === 'region') {
          $scope.loadTreeLocations($scope.mytree.currentNode.data.id).then(function(data) {
            $scope.mytree.currentNode.children = data;
          });
        } else if (nodeType === 'location') {
          $scope.loadNodes($scope.mytree.currentNode.data.id);
        }
      }
    }, false);

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


