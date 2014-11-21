/**
*  Module
*
* Description
*/
var enzymeApp = angular.module('enzyme-portal-app',['ui.bootstrap']);

enzymeApp.controller('TypeAheadController',['$scope','$http',

    function($scope, $http){
    
                $scope.searchForEnzymes = function(val) {
            return $http.get('/enzymeportal/service/search', {
                params: {
                    name: val
                }
            }).then(function(response){    
            return response.data;
            });
        };
        
        

        
        $scope.getPathways = function(val) {
            return $http.get('/enzymeportal/service/pathways', {
                params: {
                    name: val
                }
            }).then(function(response){    
            return response.data;
            });
        };

        $scope.getDiseases = function(val) {
            return $http.get('/enzymeportal/service/diseases', {
                params: {
                    name: val
                }
            }).then(function(response){
                return response.data;
            });
        };

        $scope.onSelect = function($model) {
            console.log($model);
            $scope.selectedItem = $model;
            //submit form
            
//            	$http.post('/enzymeportal/search/pathways', $scope.selectedItem)
//		.success(function(data) {
//		//do somthing	with data
//		}
//                        
//                );
        
        
        };

    }
]);