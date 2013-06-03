function calcLightState(oldState){
    return oldState.noWhitespace();
}

String.prototype.noWhitespace = function(){
    return this.match(/(\w+)/)[0];
};

function Controller($scope){        
    $scope.light = null;

    $scope.$watch('light', function(newValue, oldValue){
        if(!newValue) {
            angular.forEach(['green', 'red', 'yellow'], function(col){
                angular.element(document.getElementById(col)).removeClass("lit");
            });
        } else {
            angular.element(document.getElementById(newValue)).addClass("lit");
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

