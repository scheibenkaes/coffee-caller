function calcLightState(oldState){
    return oldState;
}

function Controller($scope){        
    $scope.light = null;

    $scope.$watch('light', function(newValue, oldValue){
        if(!newValue) {
            angular.forEach(['green', 'red', 'yellow'], function(col){
                document.getElementById(col).className = null;
            });
        } else {
            angular.element('#' + newValue).addClass("lit");
//            document.getElementById(newValue).className = "lit";
        }
    });

    $scope.sendRequest = function(){
        $scope.light = "red";
    };
    
    var ws = new WebSocket("ws://localhost:8080/ws");
    ws.onmessage = function(msg){
        console.log(msg.data);
        $scope.light = calcLightState(msg.data.trim());
        $scope.$digest();
    };
}
