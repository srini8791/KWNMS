

dashboard.controller("ManagementController", ['$rootScope', '$scope', '$http', '$state', '$location', 'dashboardService', 'Flash',
  function ($rootScope, $scope, $http, $state, $location, dashboardService, Flash) {
    var vm = this;

    $scope.limit = 10;
    $scope.vm.nodes = [];
    $scope.vm.totalNodesCount = 0;
    $scope.vm.activeNodesCount = 0;

    $scope.vm.tregions = [];
    $scope.vm.tlocations = [];
    $scope.vm.tnodes = [];

    $scope.init = function() {
      $scope.loadTreeRegions();
      $scope.loadNodes();
      $scope.loadActiveNodesCount();
    }

    $scope.loadTreeRegions = function() {
      $http.get('api/v2/treeview/regions')
        .then(function(response) {
          if (response.data) {
            $scope.vm.tregions = response.data;
          }
        }
      );
    }

    $scope.loadTreeLocations = function(regionId) {
      $http.get('api/v2/treeview/regions/' + regionId + '/locations')
        .then(function(response) {
          if (response.data) {
            $scope.vm.tlocations = response.data;
          }
        }
      );
    }

    $scope.loadTreeNodes = function(location) {
      $http.get('api/v2/treeview/location/' + location + '/nodes')
        .then(function(response) {
          if (response.data) {
            $scope.vm.tnodes = response.data;
          }
        }
      );
    }


    $scope.loadNodes = function() {
      var config = {
        params: {
          'limit': $scope.limit
        }
      };

      $http.get('api/v2/nodes', config)
        .then(function(response) {
          if (response.data) {
            $scope.vm.totalNodesCount = response.data.totalCount;
            $scope.vm.nodes = response.data.node;
          }
        }
      );
    }

    $scope.loadActiveNodesCount = function() {
      $http.get('api/v2/nodes/active/count')
        .then(function(response) {
          if (response.data) {
            $scope.vm.activeNodesCount = response.data;
          }
        }
      );
    }

  }

]);

dashboard.filter('showProductType', function() {
  return function(item) {
    if (item == undefined) {
      return 'Unknown';
    } else if (item === 'indoorap' || item === 'outdoorap') {
      return 'Access Point';
    } else {
      return item.toUpperCase();
    }
  }
});

$.fn.extend({
	treeview:	function() {
		return this.each(function() {
			// Initialize the top levels;
			var tree = $(this);
			
			tree.addClass('treeview-tree');
			tree.find('li').each(function() {
				var stick = $(this);
			});
			tree.find('li').has("ul").each(function () {
				var branch = $(this); //li with children ul
				
				branch.prepend("<i class='tree-indicator glyphicon glyphicon-chevron-right'></i>");
				branch.addClass('tree-branch');
				branch.on('click', function (e) {
					if (this == e.target) {
						var icon = $(this).children('i:first');
						
						icon.toggleClass("glyphicon-chevron-down glyphicon-chevron-right");
						$(this).children().children().toggle();
					}
				})
				branch.children().children().toggle();
				
				/**
				 *	The following snippet of code enables the treeview to
				 *	function when a button, indicator or anchor is clicked.
				 *
				 *	It also prevents the default function of an anchor and
				 *	a button from firing.
				 */
				branch.children('.tree-indicator, button, a').click(function(e) {
					branch.click();
					
					e.preventDefault();
				});
			});
		});
	}
});

/**
 *	The following snippet of code automatically converst
 *	any '.treeview' DOM elements into a treeview component.
 */
$(window).on('load', function () {
	$('.treeview').each(function () {
		var tree = $(this);
		tree.treeview();
	})
})

