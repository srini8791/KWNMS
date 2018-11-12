﻿
dashboard.controller("EventsController", ['$rootScope', '$scope', '$state', '$http', '$location', 'dashboardService', 'Flash',
function ($rootScope, $scope, $state, $http, $location, dashboardService, Flash) {
    var vm = this;

    $scope.vm.eventsFilter = 'events';
    $scope.vm.pageSize = 10;
    $scope.vm.events = [];

    $scope.loadEvents = function() {
      var filterVal = 'eventDisplay==Y';
      if ($scope.vm.eventsFilter === 'syslogs') {
        filterVal = 'eventDisplay==Y;eventSource==syslogd';
      }
      var config = {
        params: {
          '_s': filterVal,
          'limit': $scope.vm.pageSize
        }
      };

      $http.get('api/v2/events', config)
        .then(function(response) {
          if (response.status == 204) {
            $scope.vm.events = [];
          } else if (response.status == 200 && response.data) {
            $scope.vm.events = response.data.event;
          }
        }
      );
    }

}]);

