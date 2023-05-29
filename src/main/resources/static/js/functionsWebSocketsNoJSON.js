var webSocketTimer=setInterval(init, 1000);

function init() {
    generateData();
    drawChart(dataPointsParam);
}

window.addEventListener("load", init, false);

var dataPointsParam=[];

function generateData() {
    for (i = 0; i < 50; i++) {
        dataPointsParam[i] = {label: i, y: Math.floor(Math.random()*100)};
    }
}

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