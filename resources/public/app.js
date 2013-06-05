var message2Light = {
    "REQ": "red",
    "GO": null,
    "CANCEL": null,
    "OK": "yellow",
    "OK+": "green"
};

var colors = ['green', 'red', 'yellow'];

function calcLightState(msg, curState){
    var m = msg.noWhitespace().splitMessage();
    var state = curState;
    if (m) {
        if (m == 'OK') {
            state.cnt += 1;
            state.light = message2Light[state.cnt > 1 ? "OK+" : "OK"];
        } else {
            state.light = message2Light[m];
        }
        if (m == 'GO' || m == 'CANCEL') {
            state.cnt = 0;
        }
    } else {
        console.log("Unknown msg; " + msg);
    }
    return state;
}

String.prototype.noWhitespace = function(){
    return this.match(/([\w,.-]+)/)[0];
};

String.prototype.splitMessage = function(){
    return this.split(",")[1];
};

function Controller($scope){        
    $scope.light = null;

    $scope.uuid = Date.now().toString() + Math.random().toString();
    
    $scope.curState = {cnt: 0, light: null};

    $scope.$watch('light', function(newValue, oldValue){
        if(!newValue) {
            angular.forEach(colors, function(col){
                angular.element(document.getElementById(col)).removeClass("lit");
            });
        } else {
            var on = newValue;
            var off = colors.filter(function(i){
                return i != on;
            });
            angular.forEach(off, function(c){
                angular.element(document.getElementById(c)).removeClass("lit");                
            });
            angular.element(document.getElementById(newValue)).addClass("lit");
        }
    });

    $scope.sendRequest = function(){
        $scope.light = null;
    };
    
    var ws = new WebSocket("ws://localhost:8080/ws");
    ws.onopen = function(){
        console.log("opened WebSocket connection");
    };

    ws.onmessage = function(msg){
        $scope.$apply(function(){
            $scope.curState = calcLightState(msg.data,
                                             $scope.curState);
            $scope.light = $scope.curState.light;
        });
    };
}

