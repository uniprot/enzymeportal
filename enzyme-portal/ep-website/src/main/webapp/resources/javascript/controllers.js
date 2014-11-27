/**
*  Module
*
* Description
*/
var enzymeApp = angular.module('enzyme-portal-app',['ui.bootstrap']);

enzymeApp.controller('TypeAheadController',['$scope','$http','$location',
    function($scope, $http, $location){

        $scope.idMappings = [];

        $scope.searchForEnzymes = function(val) {
            return $http.get('/enzymeportal/service/search', {
                params: {
                    name: val
                }
            }).then(function(response){
            console.log(response);    
                return $.map(response.data, function(s,i){
                   
                    return s.suggestion;
                });   
                
            });
        };
         
        $scope.getPathways = function(val) {
            return $http.get('/enzymeportal/service/pathways', {
                params: {
                    name: val
                }
            }).then(function(response){
                return $.map(response.data, function(d,i){
                    $scope.idMappings[d.pathwayName] = d.pathwayId;
                    return d.pathwayName;
                });
            });
        };

        $scope.getDiseases = function(val) {
            return $http.get('/enzymeportal/service/diseases', {
                params: {
                    name: val
                }
            }).then(function(response){
                return $.map(response.data, function(d,i){
                    $scope.idMappings[d.name] = d.id;
                    return d.name;
                });
            });
        };

        $scope.onSelectPathways = function($model) {
            var name = $model;
            var id = $scope.idMappings[$model];

            $scope.selectedItem = $model;
            var url = '/enzymeportal/search-pathways?entryid=' + id + '&entryname=' + name + '&AMP;searchparams.type=KEYWORD&searchparams.previoustext=' + name + '&searchparams.start=0&searchparams.text=' + name;

            window.location.href = url;
        };

        $scope.onSelectDiseases = function($model) {
            var name = $model;
            var id = $scope.idMappings[$model];

            $scope.selectedItem = $model;
            var url = '/enzymeportal/search-disease?entryid=' + id + '&entryname=' + name + '&AMP;searchparams.type=KEYWORD&searchparams.previoustext=' + name + '&searchparams.start=0&searchparams.text=' + name;

            window.location.href = url;
        };


    }
    ]);
