function calcLightState(oldState){
    return oldState;
}

function interpretMsg(msg){
    if (msg.startsWith("green")) {
        return "green";
    } else if (msg.startsWith("red")){
        return "red";
    } else {
        return "yellow";
    }
}

function Controller($scope){        
    $scope.light = null;

    $scope.$watch('light', function(newValue, oldValue){
        if(!newValue) {
            angular.forEach(['green', 'red', 'yellow'], function(col){
                angular.element(document.getElementById(col)).removeClass("lit");
            });
        } else {
            var elm = interpretMsg(newValue);
            angular.element(document.getElementById(elm)).addClass("lit");
        }
    });

    $scope.sendRequest = function(){
        $scope.light = null;
    };
    
    var ws = new WebSocket("ws://localhost:8080/ws");
    ws.onmessage = function(msg){
        $scope.$apply(function(){
            $scope.light = calcLightState(msg.data);
        });
    };
}

