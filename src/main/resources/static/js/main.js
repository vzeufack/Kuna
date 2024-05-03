$(".clickable-row").click(function() {
	window.location = $(this).data("href");
});

$(document).ready(function() {
    google.charts.load('current', {
        packages : [ 'corechart', 'bar' ]
    });
    
    google.charts.setOnLoadCallback(drawLineChart);
    
    $('#myTable').DataTable({ 
		order: [[0, "desc"]] 
	});
});

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
        height: 350,
        vAxis: { format: 'currency' },
        hAxis : {
            slantedText:true,
            slantedTextAngle: 60
        }
    };
    var chart = new google.visualization.LineChart(document.getElementById('line_chart_div'));
    chart.draw(data, options);
}

$(function () {
	$("button[id*='create-budget']").click(function () {
		var year = $('#year').val();
		var month = $('#month').val();
		
		$.ajax({
	        type: "POST",
	        contentType: "application/json",
	        url: "/budget/create",
	        data: JSON.stringify({"month": month, "year": year}),
	        dataType: 'json',
	        cache: false,
	        timeout: 600000,
	        success: function (data) {
				document.getElementById("add-budget-error").style.display = "none";
			    window.location.href = '/budget/view/' + data.id;		
	        },
	        error: function (e) {
			    alert(e.id);
				document.getElementById("add-budget-error").style.display = "block";		
	        }
	    });
	});
	
	$("button[id*='delete_budget_btn']").click(function () {
		var budgetId = $(this).prop('id').split('_')[3];
		window.location.href = '/budget/delete/' + budgetId;
	});
});

$(function () {
	$('#create-group-btn').click(function (e) {
        e.preventDefault();        
	    
        $.post({
            url: create_group_url,
            data: $('#createGroupForm').serialize(),
            success: function (res) {
                if (res.validated) {
					location.reload();
                } else {
                    $.each(res.errorMessages, function (key, value) {
                        $('input[name=' + key + ']').after('<div class="alert alert-warning text-center mt-3 mb-0">' + value + '</div>');
                    });
                }
            }
        })
    });
});