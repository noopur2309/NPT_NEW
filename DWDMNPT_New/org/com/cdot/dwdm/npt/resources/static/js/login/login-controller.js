var app=angular.module("NptHome",[]);

app.controller("loginController",function($scope,$http){
	$scope.username="admin";
	$scope.password="admin";
	$scope.formInvalid=false;
	$scope.passwordSignUp="";
	$scope.userNameSignUp="";
	$scope.email="";
	$scope.signUpStatus=false;
	$scope.signUpStatusFailure=false;
	
	$scope.loginRequest=function(){
		var loginPostData=new Object();
		loginPostData.username=$scope.username;
		loginPostData.password=$scope.password;
		
		console.log("Login Post Data::",loginPostData);
        
        $http({
            method: "post",
            url: "/login",
            data: loginPostData
        }).success(function(response) {
                setTimeout(function() {
                    $scope.$apply(function() {

                    	console.log("Login Response data :",response);
        				        				
        				if(response==1)
        					{
        					localStorage.setItem("UserName",$scope.username);
        					document.location.href = "NewDashboard.html";
        					}
        				else
        					{
        					$scope.formInvalid=true;
        					}
                    });
                }, 200);
                //$scope.hideProcessingIcon();
            }).error(function(error) {
                //$scope.hideProcessingIcon();
            });
	}
	
	$scope.registerUser=function()
	{
		 console.log("Inside Register User");
		 var registerPostData=new Object();
		 registerPostData.username=$scope.userNameSignUp;
		 registerPostData.password=$scope.passwordSignUp;
		 registerPostData.email=$scope.email;
		 console.log(registerPostData);
		 $http({
	            method: "post",
	            url: "/registerUser",
	            data: registerPostData
	        }).success(function(response) {
	                setTimeout(function() {
	                    $scope.$apply(function() {

	                    	console.log("Sign Up Response:",response);
	        				
	        				if(response==3)
	        					{
	        					$scope.signUpStatusFailure=true;
	        					}
	        				else if(response==1)
	        					{
	        					$scope.signUpStatus=true;
	        					//document.location.href = "NewDashboard.html";
	        					}
	                    });
	                }, 200);
	                //$scope.hideProcessingIcon();
	            }).error(function(error) {
	                //$scope.hideProcessingIcon();
	            });
		
	}
	
	
});