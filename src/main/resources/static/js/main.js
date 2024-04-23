$(".clickable-row").click(function() {
	window.location = $(this).data("href");
});

$(document).ready(function() {
    google.charts.load('current', {
        packages : [ 'corechart', 'bar' ]
    });
    google.charts.setOnLoadCallback(drawColumnChart);
    google.charts.setOnLoadCallback(drawPieChart);
    google.charts.setOnLoadCallback(drawLineChart);
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

function drawLineChart() {
    var data = new google.visualization.DataTable();
    data.addColumn('string', 'Month');
    data.addColumn('number', 'Balance');
    Object.keys(real_data).forEach(function(key) {
        data.addRow([ key, real_data[key] ]);
    });
    var options = {
        title : 'Balances for last 12 months',
        fontName: 'Bree Serif, cursive',
        legend: {position: 'none'},
        height: 300,
        vAxis: { format: 'currency' }
    };
    var chart = new google.visualization.LineChart(document.getElementById('line_chart_div'));
    chart.draw(data, options);
}

$(document).ready(function(){
	$('#myTable').DataTable({ 
		order: [[0, "desc"]] 
	});
});