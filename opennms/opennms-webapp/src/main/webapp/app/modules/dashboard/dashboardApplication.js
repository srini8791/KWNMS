
var dashboard = angular.module('dashboard', ['ui.router', 'ngMaterial', 'angularTreeview']);


dashboard.config(["$stateProvider", function ($stateProvider) {

    //dashboard home page state
    $stateProvider.state('app.dashboard', {
        url: '/dashboard',
        templateUrl: 'app/modules/dashboard/views/dashboard.html',
        controller: 'HomeController',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Dashboard'
        }
    });

    //Management page state
    $stateProvider.state('app.topology', {
        url: '/topology',
        templateUrl: 'app/modules/dashboard/views/topology.html',
        controller: 'ManagementController',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Topology'
        }
    });

     //Management page state
     $stateProvider.state('app.discovery', {
        url: '/discovery',
        templateUrl: 'app/modules/dashboard/views/discovery.html',
        controller: 'ManagementController',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Discovery'
        }
    });

    //Settings page state
    $stateProvider.state('app.profiles', {
        url: '/profiles',
        templateUrl: 'app/modules/dashboard/views/profiles.html',
        controller: 'ProfileController',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Profile'
        }
    });

     //Settings page state
     $stateProvider.state('app.regions', {
        url: '/regions',
        templateUrl: 'app/modules/dashboard/views/regions.html',
        controller: 'SettingsController',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Region'
        }
    });
    //Settings page state
    $stateProvider.state('app.users', {
        url: '/users',
        templateUrl: 'app/modules/dashboard/views/users.html',
        controller: 'UserController',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Users'
        }
    });

    //reports page state
    $stateProvider.state('app.reports', {
        url: '/reports',
        templateUrl: 'app/modules/dashboard/views/reports.html',
        controller: 'ReportsController',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Reports'
        }
    });

    //Events Projects page state
    $stateProvider.state('app.events', {
        url: '/events',
        templateUrl: 'app/modules/dashboard/views/events.html',
        controller: 'EventsController',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Events'
        }
    });

    //Search page state
    $stateProvider.state('app.search', {
        url: '/search',
        templateUrl: 'app/modules/dashboard/views/search.html',
        controller: 'appCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Search'
        }
    });

}]);

