angular.module('admin', [
	'admin.comments',
	'ui.bootstrap',
	'restangular'
]).config(function(RestangularProvider) {
	RestangularProvider.setBaseUrl('./api');
	RestangularProvider.setDefaultHeaders({ 'x-requested-with': 'XMLHttpRequest' });
});