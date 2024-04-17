$(".clickable-row").click(function() {
	window.location = $(this).data("href");
});

var real_data = /*[[${chartData}]]*/'noValue';
$(document).ready(function() {
    google.charts.load('current', {
        packages : [ 'corechart', 'bar' ]
    });
    google.charts.setOnLoadCallback(drawColumnChart);
    google.charts.setOnLoadCallback(drawPieChart);
});

function drawColumnChart() {
    var data = new google.visualization.DataTable();
    data.addColumn('string', 'Groups');
    data.addColumn('number', 'Amount Left');
    Object.keys(real_data).forEach(function(key) {
        data.addRow([ key, real_data[key] ]);
    });
    var options = {
        //title : 'Blog stats',
        hAxis : {
            slantedText:true,
            slantedTextAngle: 60
        },
        vAxis : {
            title : 'Amount Left'
        },
        colors: ['#24a0ed'],
        fontName: 'Segoe UI',
        height: 400,
        //width: 800,
        bar: {groupWidth: "50%"}
    };
    var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));
    chart.draw(data, options);
}

function drawPieChart() {
    var data = new google.visualization.DataTable();
    data.addColumn('string', 'Group');
    data.addColumn('number', 'Amount');
    Object.keys(real_data).forEach(function(key) {
        data.addRow([ key, real_data[key] ]);
    });
    var options = {
        title : 'Blog stats',
        is3D: true
    };
    var chart = new google.visualization.PieChart(document
            .getElementById('piechart'));
    chart.draw(data, options);
}