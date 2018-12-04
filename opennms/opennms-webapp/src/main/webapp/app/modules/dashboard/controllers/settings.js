
dashboard.controller("SettingsController", ['$rootScope', '$scope', '$mdDialog', '$http', '$state', '$location', 'dashboardService', 'Flash',
  function ($rootScope, $scope, $mdDialog, $http, $state, $location, dashboardService, Flash) {
    var vm = this;

    vm.tab= function () {
      this.tab = 1;

      this.setTab = function (tabId) {
        this.tab = tabId;
      };

      this.isSet = function (tabId) {
        return this.tab === tabId;
      };
    };

    $scope.regions = [];
    $scope.newRegion = {
      'name': ''
    };

    $scope.init = function() {
      $scope.loadRegions();
    };

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

    $scope.saveRegion = function() {
      $http({
        method: 'POST',
        url: 'api/v2/regions',
        headers: {'Content-Type': 'application/json'},
        data: $scope.newRegion
      }).success(function() {
        $scope.showAlert('success', 'The region has been created successfully.');
        $scope.newRegion = {};
        $scope.loadRegions();
      }).error(function(msg) {
        $scope.showAlert('error', 'Error creating the region: ' + msg);
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



  }
]);
