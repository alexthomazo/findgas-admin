angular.module('admin.patchs', ['ngRoute', 'restangular', 'leaflet-directive'])
.config(function($routeProvider) {
	$routeProvider.when('/patchs', {
		templateUrl: 'app/patchs.html',
		controller: 'PatchsCtrl',
		resolve: {
			patchs: function(Restangular) { return Restangular.all('patchs').getList(); }
		}
	});
})
.controller('PatchsCtrl', function($scope, $timeout, Restangular, patchs) {
	$scope.patchs = patchs;

	var mymap = L.map('map', { minZoom: 5 }).setView([47.9099, -2.5653], 9);
	var patchMk, stationMk;
	var stations = Restangular.all('stations');

	initMap();

	$scope.edit = function(patch) {
		$scope.selected = patch;
		setMap();

		if (patch.station) {
			stations.get(patch.station).then(function(station) {
				$scope.curStation = station;
				setMapStation();
			});
		} else {
			$scope.curStation = {};
		}
	};

	$scope.validatePatch = function() {
		var station = $scope.curStation.id ? $scope.curStation : {};
		var patch = $scope.selected;
		station.name = patch.stationName;
		station.address = patch.address;
		station.cp = patch.cp;
		station.city = patch.city;
		station.location = patch.location;

		if (station.id) {
			station.save().then($scope.donePatch);
		} else {
			stations.save(station).then($scope.donePatch);
		}
	};

	$scope.deleteStation = function() {
		$scope.curStation.remove().then($scope.donePatch);
	};

	$scope.donePatch = function() {
		$scope.selected.customOperation("patch", "done", {}, {}, true).then(function() {
			delete $scope.selected;
			delete $scope.curStation;
			//delay to let ES re-index
			$timeout(reloadPatchs, 1000);
		});
	};

	function initMap() {
		L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
			maxZoom: 19,
			attribution: '<a href="about.html">A propos de ce site</a> &copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
		}).addTo(mymap);
	}

	function setMap() {
		if (patchMk) {
			mymap.removeLayer(patchMk);
			patchMk = undefined;
		}
		if (stationMk) {
			mymap.removeLayer(stationMk);
			stationMk = undefined;
		}

		var geo = $scope.selected.location.split(',');
		mymap.setView([geo[0], geo[1]], 17);
		patchMk = L.marker([geo[0], geo[1]], { draggable: true }).addTo(mymap);
		patchMk.on('dragend', function() {
			var latlng = patchMk.getLatLng();
			$scope.selected.location = latlng.lat + "," + latlng.lng;
			$scope.$apply();
		});
	}

	function setMapStation() {
		var geo = $scope.curStation.location.split(',');
		stationMk = L.circle([geo[0], geo[1]], 8, { color: 'red', fillColor: '#FF0000', fillOpacity: 0.5 }).addTo(mymap);
	}

	function reloadPatchs() {
		Restangular.all('patchs').getList().then(function(patchs) {
			$scope.patchs = patchs;
		});
	}
});