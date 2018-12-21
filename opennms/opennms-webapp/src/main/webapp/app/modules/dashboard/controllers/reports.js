

dashboard.controller("ReportsController", ['$rootScope', '$scope', '$mdDialog','$http', '$state', '$location', 'dashboardService', 'Flash',
  function ($rootScope, $scope, $mdDialog, $http, $state, $location, dashboardService, Flash) {
    var vm = this;

    $scope.limit = 10;
    $scope.vm.nodes = [];
    $scope.vm.totalNodesCount = 0;
    $scope.vm.activeNodesCount = 0;
    $scope.currentViewNodeId = '';
    //$scope.nodeView.data = {};

    $scope.init = function() {
      $scope.loadNodes();
      $scope.loadActiveNodesCount();
    }

    $scope.loadNodes = function() {
      var config = {
        params: {
          'limit': $scope.limit,
          '_s': 'productCode==ptp,productCode==ptmp,productCode==indoorap,productCode==outdoorap'
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


        $scope.rescanNode = function(node) {
              alert(node);
              $http({
                    method: 'POST',
                    url: 'api/v2/nodes/rescanNode',
                    headers: {'Content-Type': 'application/json'},
                    data: node
                  }).success(function() {
                    $scope.showAlert('success', 'Rescanning of the node initiated');
                  }).error(function(msg) {
                    $scope.showAlert('error', 'Cannot apply the profile: ' + msg);
                  });
         }

         $scope.deleteNode = function(node) {
                var nodeCheck = [];
                var nodeData = [];
                nodeCheck.push(node);
                nodeData.push(node);
                var cData = 'nodeCheck=' + nodeCheck+ '&nodeData=' + nodeData;
                 $http({
                     url : 'admin/deleteSelNodes',
                     method : "POST",
                     data : cData
                     ,headers: {
                               'Content-Type': 'application/x-www-form-urlencoded'
                           }
                 }).then(function(response) {
                     console.log(response.data);
                     $scope.message = response.data;
                     $scope.loadNodes();
                 }, function(response) {
                     //fail case
                     console.log(response);
                     $scope.message = response;
                 });

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

    $scope.showNode = function(nodeId,lastView) {
        $state.go('app.nodeView',{ "nodeId": nodeId,"lastView":lastView});

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


      $scope.deleteConfirm = function(nodeId, ev) {
        var confirm = $mdDialog.confirm()
              .title('Are you sure you want to delete this Node?')
              .targetEvent(ev)
              .ok('Yes')
              .cancel('No');

        $mdDialog.show(confirm).then(function() {
          $scope.deleteNode(nodeId);
        });
      };

  }

]);


