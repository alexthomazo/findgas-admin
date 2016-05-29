angular.module('admin', [
	'admin.comments',
	'admin.patchs',
	'ui.bootstrap',
	'restangular'
]).config(function(RestangularProvider) {
	RestangularProvider.setBaseUrl('./api');
	RestangularProvider.setDefaultHeaders({ 'x-requested-with': 'XMLHttpRequest' });
});