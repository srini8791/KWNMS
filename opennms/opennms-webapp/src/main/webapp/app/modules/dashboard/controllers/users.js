

dashboard.controller("UserController", ['$rootScope', '$scope', '$mdDialog', '$http', '$state', '$location', 'dashboardService', 'Flash',
function ($rootScope, $scope, $mdDialog, $http, $state, $location, dashboardService, Flash) {
  var vm = this;

    $scope.users = [];
    $scope.regions = [];

    $scope.newUser = {
    };

    $scope.init = function() {
      $scope.loadRegions();
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

    $scope.loadRegions = function() {
      var paramObj = { 'params': {'limit': 0} };
      $http.get('api/v2/regions', paramObj)
        .then(function(response) {
          if (response.data) {
            $scope.regions = response.data.region;
            console.log('regions = ' + $scope.regions);
          }
        }
      );
    }

    $scope.saveUser = function() {
      var roleData = 'ROLE_ADMIN';
      if ($scope.newUser.readOnly != undefined) {
        roleData = 'ROLE_READONLY';
      }
      $http({
        method: 'POST',
        url: 'rest/users',
        headers: {'Content-Type': 'application/xml'},
        data: '<user>' +
                '<user-id>' + $scope.newUser.username + '</user-id>' +
                '<password>' + $scope.newUser.password + '</password>' +
                '<full-name>' + $scope.newUser.fullName + '</full-name>' +
                '<region-id>' + $scope.newUser.regionId + '</region-id>' +
                '<email>' + $scope.newUser.email + '</email>' +
                '<role>' + roleData + '</role>' +
                '</user>'
      }).success(function() {
        $scope.showAlert('success', 'The user has been created successfully.');
        $scope.newUser = {};
        $scope.loadUsers();
      }).error(function(msg) {
        $scope.showAlert('error', 'Error creating the user: ' + msg);
      });
    };

    $scope.deleteUser = function(userName) {
      $http({
        method: 'DELETE',
        url: 'rest/users/' + userName
      }).success(function() {
        $scope.showAlert('success', 'The user has been deleted successfully.');
        $scope.loadUsers();
      }).error(function(msg) {
        $scope.showAlert('error', 'Error deleting the user: ' + msg);
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

    $scope.deleteUserConfirm = function(userName, ev) {
      var confirm = $mdDialog.confirm()
            .title('Are you sure you want to delete this user?')
            .targetEvent(ev)
            .ok('Yes')
            .cancel('No');

      $mdDialog.show(confirm).then(function() {
        $scope.deleteUser(userName);
      });
    };



}]);