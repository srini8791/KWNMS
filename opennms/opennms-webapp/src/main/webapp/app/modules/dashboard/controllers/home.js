
dashboard.controller("HomeController", ['$rootScope', '$scope', '$state', '$location', 'dashboardService', 'Flash',
function ($rootScope, $scope, $state, $location, dashboardService, Flash) {
    var vm = this;

    vm.showDetails = true;
    vm.dashboard = {};
    
    vm.dashboard.chart = [
        {
          "icon": "mdi-access-point",
          "title": "PTP",
          "chartnumber": "1"
        },
        {
          "icon": "mdi-bluetooth-connect",
          "title": "PTMP",
          "chartnumber": "2"
        },
        {
          "icon": "mdi-wifi",
          "title": "WiFi",
          "chartnumber": "3"
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
            icon: "fa-random",
            title: "PTP",
            total: "0",
            down: "0",
            up: "0"

        },
        {
            icon: "fa-tachometer-alt",
            title: "PTMP",
            total: "0",
            down: "0",
            up: "0"
        },
        {
            icon: "fa-wifi",
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

    // CHARTS

    // {1, 3, 5, 2, 3, 5, 1, 2, 5, 2, 1, 3} // dummy data
    vm.chart_ptp = {};
    vm.chart_ptmp = {};
    vm.chart_wifi = {};
    vm.channelCounts = [1, 3, 5, 2, 3, 5, 1, 2, 5, 2, 1, 3];

    var chartsSource = new EventSource('api/v2/nodes/counts/bychannel');
    chartsSource.onmessage = function(event) {
        if (event.data) {
            var countsArray = JSON.parse(event.data);
            $scope.$apply(function() {
              // ptp
              vm.chart_ptp.axes[0].bands[1].endValue = countsArray[0];
              vm.chart_ptp.axes[0].bands[1].balloonText = countsArray[0];
              vm.chart_ptp.axes[0].bands[3].endValue = countsArray[1];
              vm.chart_ptp.axes[0].bands[3].balloonText = countsArray[1];
              vm.chart_ptp.axes[0].bands[5].endValue = countsArray[2];
              vm.chart_ptp.axes[0].bands[5].balloonText = countsArray[2];
              vm.chart_ptp.axes[0].bands[7].endValue = countsArray[3];
              vm.chart_ptp.axes[0].bands[7].balloonText = countsArray[3];
              // ptmp
              vm.chart_ptmp.axes[0].bands[1].endValue = countsArray[4];
              vm.chart_ptmp.axes[0].bands[1].balloonText = countsArray[4];
              vm.chart_ptmp.axes[0].bands[3].endValue = countsArray[5];
              vm.chart_ptmp.axes[0].bands[3].balloonText = countsArray[5];
              vm.chart_ptmp.axes[0].bands[5].endValue = countsArray[6];
              vm.chart_ptmp.axes[0].bands[5].balloonText = countsArray[6];
              vm.chart_ptmp.axes[0].bands[7].endValue = countsArray[7];
              vm.chart_ptmp.axes[0].bands[7].balloonText = countsArray[7];
              // wifi
              vm.chart_wifi.axes[0].bands[1].endValue = countsArray[8];
              vm.chart_wifi.axes[0].bands[1].balloonText = countsArray[8];
              vm.chart_wifi.axes[0].bands[3].endValue = countsArray[9];
              vm.chart_wifi.axes[0].bands[3].balloonText = countsArray[9];
              vm.chart_wifi.axes[0].bands[5].endValue = countsArray[10];
              vm.chart_wifi.axes[0].bands[5].balloonText = countsArray[10];
              vm.chart_wifi.axes[0].bands[7].endValue = countsArray[11];
              vm.chart_wifi.axes[0].bands[7].balloonText = countsArray[11];
            });
        }
    };



}]);

