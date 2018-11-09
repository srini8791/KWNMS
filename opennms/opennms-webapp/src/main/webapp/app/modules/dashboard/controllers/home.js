
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

    vm.dashboard.productStatus = [
        {   
            icon: "mdi-access-point",
            title: "PTP",
            total: "0",
            down: "0",
            up: "0"
            
        },
        {
            icon: "mdi-bluetooth-connect",
            title: "PTMP",
            total: "0",
            down: "0",
            up: "0"
        },
        {
            icon: "mdi-wifi",
            title: "Wifi",
            total: "0",
            down: "0",
            up: "0"
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

    var eventSource = new EventSource('api/v2/dashboard/events/24');
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

    // NODES
    vm.dashboard.outagesCount = "0";
    vm.dashboard.activeNodes = "0";
    vm.dashboard.inactiveNodes = "0";
    vm.dashboard.unprovisionedNodes = "0";

    var nodesSource = new EventSource('api/v2/dashboard/nodes/counts_summary');
    nodesSource.onmessage = function(event) {
        if (event.data) {
            var nodesObj = JSON.parse(event.data);
            $scope.$apply(function() {
              vm.dashboard.outagesCount = nodesObj.outages;
              vm.dashboard.activeNodes = nodesObj.active;
              vm.dashboard.inactiveNodes = nodesObj.inactive;
              vm.dashboard.unprovisionedNodes = nodesObj.unprovisioned;
            });
        }
    };

    // NODES
    vm.dashboard.ptpStatus = [];
    vm.dashboard.ptmpStatus = [];
    vm.dashboard.wifiStatus = [];

    var productsSource = new EventSource('api/v2/dashboard/nodes/productcodes_summary');
    productsSource.onmessage = function(event) {
        if (event.data) {
            var productsObj = JSON.parse(event.data);
            $scope.$apply(function() {
              vm.dashboard.productStatus[0].total = productsObj.ptp[0];
              vm.dashboard.productStatus[0].up = productsObj.ptp[1];
              vm.dashboard.productStatus[0].down = productsObj.ptp[2];

              vm.dashboard.productStatus[1].total = productsObj.ptmp[0];
              vm.dashboard.productStatus[1].up = productsObj.ptmp[1];
              vm.dashboard.productStatus[1].down = productsObj.ptmp[2];

              vm.dashboard.productStatus[2].total = productsObj.wifi[0];
              vm.dashboard.productStatus[2].up = productsObj.wifi[1];
              vm.dashboard.productStatus[2].down = productsObj.wifi[2];
            });
        }
    };



}]);

