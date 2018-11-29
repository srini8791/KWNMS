
app.controller("appCtrl", ['$rootScope', '$scope', '$state', '$location', 'Flash','appSettings',
function ($rootScope, $scope, $state, $location, Flash,appSettings) {
  
    var vm = this;

    //Main menu items of the dashboard
    vm.menuItems = [
        {
            title: "Dashboard",
            icon: "home",
            state: "dashboard"
        },
        {
            title: "Management",
            icon: "users",
            state: "management",
            sub: [{
                title: "Discovery",
                state: "discovery"
            },
            {
                title: "Topology",
                state: "topology"
            }]
        },
        {
            title: "Reports",
            icon: "suitcase",
            state: "reports"
        },
        {
            title: "Events",
            icon: "folder",
            state: "events"
        },
        {
            title: "Settings",
            icon: "cog",
            state: "settings",
            sub: [{
                title: "Profiles",
                state: "profiles"

            },
            {
                title: "Regions",
                state: "regions"

            },
            {
                title: "Users",
                state: "users"

            }]
        }
    ];

    $(function () {
        $('[data-toggle="tooltip"]').tooltip()
      })
    //controll sidebar open & close in mobile and normal view

    // Toggle the side navigation

  $("#sidebarToggle").on('click',function(e) {
    e.preventDefault();
    $("body").toggleClass("sidebar-toggled");
    $(".sidebar").toggleClass("toggled");
  });


    vm.sideBar = function (value) {
        if($(window).width()<=767){
        if ($("body").hasClass('sidebar-open'))
            $("body").removeClass('sidebar-open');
        else
            $("body").addClass('sidebar-open');
        }
        else {
            if(value==1){
            if ($("body").hasClass('sidebar-collapse'))
                $("body").removeClass('sidebar-collapse');
            else
                $("body").addClass('sidebar-collapse');
            }
        }
    };


    //navigate to search page
    vm.search = function () {
        $state.go('app.search');
    };

    console.log('getting in to the app controller');

}]);
