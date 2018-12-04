

dashboard.controller("ReportsController", ['$rootScope', '$scope', '$http', '$state', '$location', 'dashboardService', 'Flash',
  function ($rootScope, $scope, $http, $state, $location, dashboardService, Flash) {
    var vm = this;

    $scope.limit = 10;
    $scope.vm.nodes = [];
    $scope.vm.totalNodesCount = 0;
    $scope.vm.activeNodesCount = 0;

    $scope.init = function() {
      $scope.loadNodes();
      $scope.loadActiveNodesCount();
    }

    $scope.loadNodes = function() {
      var config = {
        params: {
          'limit': $scope.limit
        }
      };

      $http.get('api/v2/nodes', config)
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


    $scope.reportColumns = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,14];
    $scope.removedColumns = [];
    $scope.reportColumnNames = ["System Name", "IP Address", " Serial Number", " MAC Address", " Model", " Firmware", " Product Type", " Radio Mode"," Op Mode", " Bandwidth", " Channel", "Frequency"," Ethernet Speed", "Latitude", "Longitude"];
    $scope.removedColumnNames = [];
    $scope.itemToAddBack = '';

    $scope.removeColumn = function(item) {
      var index = $scope.reportColumns.indexOf(item);
      $scope.reportColumns.splice(index, 1);
      $scope.removedColumns.push(item);
      $scope.removedColumnNames.push($scope.reportColumnNames[item]);
    }

    $scope.showColumn = function(item) {
      var index = $scope.reportColumns.indexOf(item);
      if (index >= 0) {
        return true;
      }
      return false;
    }

    $scope.addColumn = function() {
      var index = $scope.reportColumnNames.indexOf($scope.itemToAddBack);
      $scope.reportColumns.push(index);
      $scope.reportColumns.sort();
      $scope.removedColumns.splice(index, 1);
      index = $scope.removedColumnNames.indexOf($scope.itemToAddBack);
      $scope.removedColumnNames.splice(index, 1);
    }

  }

]);


