
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
    $scope.facilities = [];
    $scope.newRegion = {
      'name': ''
    };

    $scope.newFacility = {
      'region': {
        'id': ''
      }
    };

    $scope.init = function() {
      $scope.loadRegions();
      $scope.loadFacilities();
    };

    $scope.loadRegions = function() {
      var paramObj = { 'params': {'limit': 0} };
      $http.get('api/v2/regions', paramObj)
        .then(function(response) {
          if (response.data) {
            $scope.regions = response.data.region;
            //console.log('regions = ' + $scope.regions);
          }
        }
      );
    }

    $scope.loadFacilities = function() {
      var paramObj = { 'params': {'limit': 0} };
      $http.get('api/v2/facilities', paramObj)
        .then(function(response) {
          if (response.data) {
            $scope.facilities = response.data.facility;
            //console.log('facilities = ' + $scope.facilities);
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

    $scope.deleteRegion = function(regionId,regionName) {
        if (regionName != 'Default') {
            $http({
                    method: 'DELETE',
                    url: 'api/v2/regions/' + regionId
                  }).success(function() {
                    $scope.showAlert('success', 'The region has been deleted successfully.');
                    $scope.loadRegions();
                    $scope.loadFacilities();
                  }).error(function(msg) {
                    $scope.showAlert('error', 'Error deleting the region: ' + msg);
                  });
        } else {
            $scope.showAlert('error', 'Unable to delete region: ' + regionName);
        }

    }

    $scope.disableValue = function(variable) {
        if (variable == 'Default') {
            return false;
        } else {
            return true;
        }
    }

    $scope.saveFacility = function() {
          $http({
            method: 'POST',
            url: 'api/v2/facilities',
            headers: {'Content-Type': 'application/json'},
            data: $scope.newFacility
          }).success(function() {
            $scope.showAlert('success', 'The facility has been created successfully.');
            $scope.newFacility = {};
            $scope.loadFacilities();
          }).error(function(msg) {
            $scope.showAlert('error', 'Error creating the facility: ' + msg);
          });
        };

    $scope.deleteFacility = function(facilityId,facilityName) {
        if (facilityName != 'Default') {
            $http({
                    method: 'DELETE',
                    url: 'api/v2/facilities/' + facilityId
                  }).success(function() {
                    $scope.showAlert('success', 'The facility has been deleted successfully.');
                    $scope.loadFacilities();
                  }).error(function(msg) {
                    $scope.showAlert('error', 'Error deleting the facility: ' + msg);
                  });
        } else {
            $scope.showAlert('error', 'Unable to delete facility: ' + facilityName);
        }
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

  $scope.deleteRegionConfirm = function(regionId, regionName, ev) {
    var confirm = $mdDialog.confirm()
          .title('Are you sure you want to delete this region?')
          .textContent('Facilities associated with this region will also be deleted.')
          .targetEvent(ev)
          .ok('Yes')
          .cancel('No');

    $mdDialog.show(confirm).then(function() {
      $scope.deleteRegion(regionId,regionName);
    });
  };

  $scope.deleteFacilityConfirm = function(facilityId,facilityName, ev) {
    var confirm = $mdDialog.confirm()
          .title('Are you sure you want to delete this facility?')
          .targetEvent(ev)
          .ok('Yes')
          .cancel('No');

    $mdDialog.show(confirm).then(function() {
      $scope.deleteFacility(facilityId,facilityName);
    });
  };

}]);
