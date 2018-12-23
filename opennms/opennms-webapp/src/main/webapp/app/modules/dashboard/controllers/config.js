

dashboard.controller("ConfigController", ['$rootScope', '$scope', '$mdDialog', '$http', '$state', '$location', 'dashboardService', 'Flash',
function ($rootScope, $scope, $mdDialog, $http, $state, $location, dashboardService, Flash) {
  var vm = this;

  $scope.systemProperties = [];

  $scope.newSystemProperty = {
  };

  $scope.currentSystemProperty = {
  };

  $scope.init = function() {
    $scope.loadSystemProperties();
  };

  $scope.loadSystemProperties = function() {
    var paramObj = { 'params': {'limit': 0} };
    $http.get('api/v2/systemprops', paramObj)
      .then(function(response) {
        if (response.data) {
          console.log(response.data);
          $scope.systemProperties = response.data.systemprop;
        }
      }
    );
  }

  $scope.saveSystemProperty = function() {
    $http({
      method: 'POST',
      url: 'api/v2/systemprops',
      headers: {'Content-Type': 'application/json'},
      data: $scope.newSystemProperty
    }).success(function() {
      $scope.showAlert('success', 'The system property has been created successfully.');
      $scope.newSystemProperty = {};
      $scope.loadSystemProperties();
    }).error(function(msg) {
      $scope.showAlert('error', 'Error creating a system property: ' + msg);
    });
  };

  $scope.updateSystemProperty = function(propId) {
    $http({
      method: 'PUT',
      url: 'api/v2/systemprops/' + propId,
      headers: {'Content-Type': 'application/json'},
      data: $scope.currentSystemProperty
    }).success(function() {
      $scope.showAlert('success', 'The system property has been updated successfully.');
      $scope.currentSystemProperty = {};
      $scope.loadSystemProperties();
    }).error(function(msg) {
      $scope.showAlert('error', 'Error updating the system property: ' + msg);
    });
  };

  $scope.editSystemProperty = function(propId) {
    $scope.tab = 2;
    $http.get('api/v2/systemprops/' + propId)
      .then(function(response) {
        if (response.data) {
          $scope.currentSystemProperty = response.data;
        }
      }
    );
  }

  $scope.deleteSystemProperty = function(propId) {
    $http({
      method: 'DELETE',
      url: 'api/v2/systemprops/' + propId
    }).success(function() {
      $scope.showAlert('success', 'The system property has been deleted successfully.');
      $scope.loadSystemProperties();
    }).error(function(msg) {
      $scope.showAlert('error', 'Error deleting the system property: ' + msg);
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

  $scope.deletePropertyConfirm = function(propId, ev) {
    var confirm = $mdDialog.confirm()
          .title('Are you sure you want to delete this property?')
          .targetEvent(ev)
          .ok('Yes')
          .cancel('No');

    $mdDialog.show(confirm).then(function() {
      $scope.deleteSystemProperty(propId);
    });
  };



}]);