app.controller('SignUpController', ['vcRecaptchaService', function($scope, $http, vcRecaptchaService) {
	$scope.publicKey = "6LfaHiITAAAAAAgZBHl4ZUZAYk5RlOYTr6m2N34X";
    $scope.submitForm = function(){
        var url = serverAddress + "/signup/submit";
		if(vcRecaptchaService.getResponse() === ""){
			alert("Please resolve the captcha and submit!")
		} else {
			var data = {
				'email': $scope.email,
				'password': $scope.password,
				'g-captcha-response': vcRecaptchaService.getResponse()
			};

        $http.post(url, data).then(function (response) {
            $scope.postResultMessage = response.data;
        }, function error(response) {
            $scope.postResultMessage = "Error with status: " +  response.statusText;
        });

        $scope.email = "";
        $scope.password = "";
    }
}}]);