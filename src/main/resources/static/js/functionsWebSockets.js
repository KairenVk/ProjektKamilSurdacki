var webSocketTimer=setInterval(doSend, 1000);
var wsUri = getRootUri() + "/webSocketEndPointJSON";
function getRootUri() {
    var wsUriHostPort = (location.protocol === "http:" ? "ws://":"wss://");
    return wsUriHostPort + (document.location.hostname === "" ?
            "localhost" :
            document.location.hostname) + ":" +
            (document.location.port === "" ? "8080" :
            document.location.port);
}

function init() {
    drawChart(dataPointsParam);
    initWebSocket();
}

function initWebSocket() {
    websocket = new WebSocket (wsUri);
    websocket.onopen = function (evt) {
        onOpen(evt);
    };
    websocket.onmessage = function (evt) {
        onMessage(evt);
    };
    websocket.onerror = function (evt) {
        onError(evt);
    };
}

function onOpen(evt) {
    writeToScreen("CONNECTED");
}
function onMessage(evt) {
    writeToScreen("RECEIVED: " + evt.data);
    var dataArrayJSON = JSON.parse(evt.data);
    for (i = 0; i < dataArrayJSON.length; i++) {
        dataPointsParam[i]={label:i,y: dataArrayJSON[i]};
    }
    drawChart(dataPointsParam);
}

function onError(evt) {
    writeToScreen('<span style="color: red;">ERROR:</span>' +
            evt.data);
}
function doSend(message) {
    writeToScreen("SENT: " + message);
    if (message) (websocket.send(message))
    else (websocket.send("x"))
}

function writeToScreen(message) {
    var messageField = document.getElementById("messageStatusID");
    messageField.value=message;
}

window.addEventListener("load", init, false);

var dataPointsParam=[];

function drawChart(dataPointsParam) {
    var chart = new CanvasJS.Chart("chartContainer", {
        title:{
            text: "Wykres CanvasJS"
        },
        axisY:{
            title: "Opis osi Y",
            maximum: 100
        },
        data: [
            {
                type: "column",
                dataPoints: dataPointsParam
            }
        ]
    });
    chart.render();
}