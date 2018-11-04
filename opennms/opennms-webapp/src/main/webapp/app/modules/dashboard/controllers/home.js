
dashboard.controller("HomeController", ['$rootScope', '$scope', '$state', '$location', 'dashboardService', 'Flash',
function ($rootScope, $scope, $state, $location, dashboardService, Flash) {
    var vm = this;

    vm.showDetails = true;
    vm.dashboard = {};
    
        vm.dashboard.chart = [
        {
          chartnumber: "1"
        },        
        {
          chartnumber: "2"
        },
        {
          chartnumber: "3"
        }




        ];

    vm.dashboard.notification = [
        {
            userimage: "images/users/1.jpg",
            username: "sadle",
            time: "today 5.21 PM",
            message: "Ram on device Jame_3  has been over 90% for over 10 seconds"            
        },
        { 
            userimage: "images/users/1.jpg",
            username: "sadle",
            time: "today 5.21 PM",
            message: "Ram on device Jame_3  has been over 90% for over 10 seconds"  
        },
        {
            userimage: "images/users/1.jpg",
            username: "sadle",
            time: "today 5.21 PM",
            message: "Ram on device Jame_3  has been over 90% for over 10 seconds"            
        },
        { 
            userimage: "images/users/1.jpg",
            username: "sadle",
            time: "today 5.21 PM",
            message: "Ram on device Jame_3  has been over 90% for over 10 seconds"  
        },
        {
            userimage: "images/users/1.jpg",
            username: "sadle",
            time: "today 5.21 PM",
            message: "Ram on device Jame_3  has been over 90% for over 10 seconds"            
        },
        { 
            userimage: "images/users/1.jpg",
            username: "sadle",
            time: "today 5.21 PM",
            message: "Ram on device Jame_3  has been over 90% for over 10 seconds"  
        },
        {
            userimage: "images/users/1.jpg",
            username: "sadle",
            time: "today 5.21 PM",
            message: "Ram on device Jame_3  has been over 90% for over 10 seconds"  
        }
    ];

    vm.dashboard.mainData = [
        {   
            icon: "mdi-access-point",
            title: "PTP",
            total: "10",
            down: "2",
            up: "8"
            
        },
        {
            icon: "mdi-bluetooth-connect",
            title: "PTMP",
            total: "10",
            down: "2",
            up: "8"
        },
        {
            icon: "mdi-wifi",
            title: "Wifi",
            total: "10",
            down: "2",
            up: "8"
        }
    ];

    // ALARMS
    vm.dashboard.alarms_s7 = "0";
    vm.dashboard.alarms_s6 = "0";

    var alarmSource = new EventSource('api/v2/dashboard/alarms');
    alarmSource.onmessage = function(event) {
        if (event.data) {
            var alarmsObj = JSON.parse(event.data);
            $scope.$apply(function() {
                if (alarmsObj.s6) {
                  vm.dashboard.alarms_s6 = alarmsObj.s6;
                }
                if (alarmsObj.s7) {
                  vm.dashboard.alarms_s7 = alarmsObj.s7;
                }
            });
        }
    };

    // EVENTS
    vm.dashboard.events = [];

    var eventSource = new EventSource('api/v2/dashboard/events/1');
    eventSource.onmessage = function(event) {
        if (event.data) {
            var eventsArr = JSON.parse(event.data);
            $scope.$apply(function() {
                if (eventsArr) {
                  vm.dashboard.events = eventsArr;
                }
            });
        }
    };

    vm.dashboard.activeNodes = "0";

    var nodesSource = new EventSource('api/v2/dashboard/nodes/active');
    nodesSource.onmessage = function(event) {
        if (event.data) {
            var nodesObj = JSON.parse(event.data);
            $scope.$apply(function() {
              vm.dashboard.activeNodes = nodesObj.active;
            });
        }
    };


}]);

