

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
      var roleData = 'ROLE_USER';
      console.log($scope.newUser.readOnly);
      if ($scope.newUser.readOnly != undefined) {
        roleData += ',ROLE_READONLY';
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