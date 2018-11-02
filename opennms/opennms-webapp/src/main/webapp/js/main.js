
/**
 * Main AngularJS Web Application
 */
var app = angular.module('keywestWebApp', [
  'ngRoute'
]);

/**
 * Configure the Routes
 */
app.config(['$routeProvider', function ($routeProvider) {
  $routeProvider
    // Home
    .when("/", {templateUrl: "partials/login.html", controller: "PageCtrl"})
    // Pages
    .when("/dashboard", {templateUrl: "partials/dashboard.html", controller: "PageCtrl"})
    .when("/management", {templateUrl: "partials/management.html", controller: "PageCtrl"})
    .when("/settings", {templateUrl: "partials/settings.html", controller: "PageCtrl"})
    .when("/reports", {templateUrl: "partials/reports.html", controller: "PageCtrl"})
    .when("/events", {templateUrl: "partials/events.html", controller: "PageCtrl"})    
    // else 404
    .otherwise("/404", {templateUrl: "partials/404.html", controller: "PageCtrl"});
}]);

/**
 * Controls the Blog
 */
app.controller('BlogCtrl', function (/* $scope, $location, $http */) {
  console.log("Blog Controller reporting for duty.");
});

/**
 * Controls all other Pages
 */
app.controller('PageCtrl', function (/* $scope, $location, $http */) {
  console.log("Page Controller reporting for duty.");

  // Activates the Carousel
  $('.carousel').carousel({
    interval: 5000
  });

  // Activates Tooltips for Social Links
  $('.tooltip-social').tooltip({
    selector: "a[data-toggle=tooltip]"
  })
});