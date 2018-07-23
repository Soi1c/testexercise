var app = angular.module('testexercise', []);
app.controller('SignUpController', function($scope, $http, $location) {
    $scope.submitForm = function(){
        var url = $location.absUrl() + "/signUp/submit";

        var config = {
            headers : {
                'Accept': 'text/plain'
            }
        }
        var data = {
            email: $scope.email,
            password: $scope.password
        };

        $http.post(url, data, config).then(function (response) {
            $scope.postResultMessage = response.data;
        }, function error(response) {
            $scope.postResultMessage = "Error with status: " +  response.statusText;
        });

        $scope.email = "";
        $scope.password = "";
    }
});