
dashboard.controller("EventsController", ['$rootScope', '$scope', '$state', '$http', '$location', 'dashboardService', 'Flash',
function ($rootScope, $scope, $state, $http, $location, dashboardService, Flash) {
    var vm = this;

    $scope.vm.events = [];

    $scope.loadEvents = function() {
      var config = {
        params: {
          '_s': 'eventDisplay==Y'
        }
      };

      $http.get('api/v2/events', config)
        .then(function(response) {
          if (response.data) {
            $scope.vm.events = response.data.event;
          }
        }
      );
    }

}]);

