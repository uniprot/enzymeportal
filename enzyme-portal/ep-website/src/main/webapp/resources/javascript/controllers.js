/**
*  Module
*
* Description
*/
var enzymeApp = angular.module('enzyme-portal-app',['ui.bootstrap']);

enzymeApp.controller('TypeAheadController',['$scope','$http',

    function($scope, $http){

        $scope.getPathways = function(val) {
            return $http.get('http://localhost:8081/enzymeportal/service/pathways', {
                params: {
                    name: val
                }
            }).then(function(response){
                return response.data;
            });
        };

        $scope.getDiseases = function(val) {
            return $http.get('http://localhost:8081/enzymeportal/service/diseases', {
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
        };

    }
]);