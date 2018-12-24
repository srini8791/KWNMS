

dashboard.controller("NodeViewController", ['$rootScope', '$scope','$mdDialog', '$http', '$state', 'dashboardService', 'Flash',
  function ($rootScope, $scope, $mdDialog, $http, $state,  dashboardService, Flash) {
    var vm = this;
    $scope.vm.node = {};

     $scope.init = function() {
           $scope.loadNodeById($state.params.nodeId);
           console.log($scope.vm.node);
     }

    $scope.loadNodeById = function(nodeId) {
          var url = 'api/v2/nodes/' + nodeId;
          return $http.get(url)
            .then(function(response) {
              if (response.data) {
                    $scope.vm.node = response.data;
                return response.data;
              }
            }
          );
        }

    $scope.rescanNode = function(node) {
      //alert(node);
      $http({
            method: 'POST',
            url: 'api/v2/nodes/rescanNode',
            headers: {'Content-Type': 'application/json'},
            data: node
          }).success(function() {
            $scope.showAlert('success', 'Rescanning of the node initiated');
            //$state.go($state.params.lastView);
          }).error(function(msg) {
            $scope.showAlert('error', 'Failed to resan the node: ' + msg);
          });
    }


    $scope.deleteNode = function(node) {
        var confirm = $scope.deleteConfirm(node);
        $mdDialog.show(confirm).then(function() {
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
                   $scope.showAlert('success', 'Node successfully deleted.');
                   $state.go($state.params.lastView);
               }, function(response) {
                   //fail case
                   console.log(response);
                   $scope.showAlert('error', 'Failed to delete the Node.')
                   //$scope.message = response;
               });
        });

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

        return confirm;
      };

      $scope.confirm = function(nodeId, ev) {
          var confirm = $mdDialog.confirm()
                .title('Are you sure you want to delete this Node?')
                .targetEvent(ev)
                .ok('Yes');


          return confirm;
      };


}]);


