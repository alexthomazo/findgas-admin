angular.module('admin.comments', ['ngRoute', 'restangular'])
.config(function($routeProvider) {
	$routeProvider.when('/', {
		templateUrl: 'app/comments.html',
		controller: 'CommentsCtrl',
		resolve: {
			comments: function(Restangular) { return Restangular.all('comments').getList(); }
		}
	});
})
.controller('CommentsCtrl', function($scope, comments) {
	$scope.comments = comments;
});