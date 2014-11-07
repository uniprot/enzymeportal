/**
*  Module
*
* Description
*/
var enzymeApp = angular.module('enzyme-portal-app',['ui.bootstrap']);


enzymeApp.controller('pathwayTypeAheadController',function($scope, $http){
	
	$scope.selected = undefined;

	$scope.getPathways = function($viewValue) {
		console.log(val);
		return 
		// return $http.get('http://localhost:8081/enzymeportal/pathways', {
		// 	params: {
		// 		name: $viewValue
		// 	}
		// }).then(function(response){
		// 	console.log("res:" + response);
		// 	return response.data.results;
  //     // return response.data.results.map(function(item){
  //     //   return item.formatted_address;
  //     // });
		// });
	};
	
});