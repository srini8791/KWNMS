
var dashboard = angular.module('dashboard', ['ui.router', 'ngAnimate','ngMaterial']);


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
    $stateProvider.state('app.management', {
        url: '/management',
        templateUrl: 'app/modules/dashboard/views/management.html',
        controller: 'ManagementController',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Management'
        }
    });

    //Settings page state
    $stateProvider.state('app.settings', {
        url: '/settings',
        templateUrl: 'app/modules/dashboard/views/settings.html',
        controller: 'SettingsController',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Settings'
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

