/**
 * Catch all angular events on fixed HTML elements in NPT.
 */
app.controller('mainController', function($scope, $http) {
    console.log("+++++++++++mainController++++++++++++++");

    $scope.dragLock=false;

    $scope.fDragLock=function (){
        $scope.dragLock=!$scope.dragLock;
        console.log("$scope.dragLock",$scope.dragLock);
    }

    // $scope.fPaperMouseMove=function(event){
    //     console.log(" isDraggable:",isDraggable," dragStartPosition::",dragStartPosition," $scope.dragLock",$scope.dragLock,isDraggable == 1 && dragStartPosition && !$scope.dragLock);
    //     if (isDraggable == 1 && dragStartPosition && !$scope.dragLock){            
    //         paper.setOrigin(
    //             event.offsetX - dragStartPosition.x,
    //             event.offsetY - dragStartPosition.y);
    //     }        
    // }

    console.log("Initial $scope.dragLock",$scope.dragLock);


}); 