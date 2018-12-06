

dashboard.controller("UserController", ['$rootScope', '$scope', '$mdDialog', '$http', '$state', '$location', 'dashboardService', 'Flash',
function ($rootScope, $scope, $mdDialog, $http, $state, $location, dashboardService, Flash) {
  var vm = this;

    $scope.users = [];

    $scope.newUser = {
    };

    $scope.init = function() {
      $scope.loadUsers();
    };


    $scope.loadUsers = function() {
      var paramObj = { 'params': {'limit': 0} };
      $http.get('rest/users', paramObj)
        .then(function(response) {
          if (response.data) {
            $scope.users = response.data.user;
            console.log('users = ' + $scope.users);
          }
        }
      );
    }

    $scope.saveUser = function() {
      $http({
        method: 'POST',
        url: 'rest/users',
        headers: {'Content-Type': 'application/json'},
        data: $scope.newUser
      }).success(function() {
        $scope.showAlert('success', 'The user has been created successfully.');
        $scope.newUser = {};
        $scope.loadUsers();
      }).error(function(msg) {
        $scope.showAlert('error', 'Error creating the user: ' + msg);
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


}]);