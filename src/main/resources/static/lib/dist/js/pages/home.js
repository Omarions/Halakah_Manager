/*
 * Author: Abdullah A Almsaeed
 * Date: 4 Jan 2014
 * Description:
 *      This is a demo file used only for the main dashboard (index.html)
 **/

/**
 * Modified by Omar Fawzy
 */
$(function () {
    "use strict";
    var user_status;
    //assign update status of the user to the click event of the button_online
    $('#button_online').click(function (e) {
        user_status = 'online';
        updateStatus(userEmail, user_status);
    });
    //assign update status of the user to the click event of the button_offline
    $('#button_offline').click(function (event) {
        user_status = 'offline';
        updateStatus(userEmail, user_status);
    });

    function updateStatus(uEmail, newStatus) {
        $.ajax({
            url: "/admin/user/updateStatus",
            type: "GET",
            data: {
                email: uEmail,
                userStatus: newStatus
            },
            dataType: "html",
            success: function (data) {
                location.reload();
            }
        });
    }

    var areaChartData = {
        labels: countriesLabels,
        datasets: [
            {
                label: "Males",
                fillColor: "rgba(210, 214, 222, 1)",
                strokeColor: "rgba(210, 214, 222, 1)",
                pointColor: "rgba(210, 214, 222, 1)",
                pointStrokeColor: "#c1c7d1",
                pointHighlightFill: "#fff",
                pointHighlightStroke: "rgba(220,220,220,1)",
                data: malesValues
            },
            {
                label: "Females",
                fillColor: "rgba(60,141,188,0.9)",
                strokeColor: "rgba(60,141,188,0.8)",
                pointColor: "#3b8bba",
                pointStrokeColor: "rgba(60,141,188,1)",
                pointHighlightFill: "#fff",
                pointHighlightStroke: "rgba(60,141,188,1)",
                data: femalesValues
            }
        ]
    };

    //-------------
    //- BAR CHART -
    //-------------
    var barChartCanvas = $("#barChart").get(0).getContext("2d");
    var barChart = new Chart(barChartCanvas);
    var barChartData = areaChartData;
    barChartData.datasets[1].fillColor = "#00a65a";
    barChartData.datasets[1].strokeColor = "#00a65a";
    barChartData.datasets[1].pointColor = "#00a65a";
    var barChartOptions = {
        //Boolean - Whether the scale should start at zero, or an order of magnitude down from the lowest value
        scaleBeginAtZero: true,
        //Boolean - Whether grid lines are shown across the chart
        scaleShowGridLines: true,
        //String - Colour of the grid lines
        scaleGridLineColor: "rgba(0,0,0,.05)",
        //Number - Width of the grid lines
        scaleGridLineWidth: 1,
        //Boolean - Whether to show horizontal lines (except X axis)
        scaleShowHorizontalLines: true,
        //Boolean - Whether to show vertical lines (except Y axis)
        scaleShowVerticalLines: true,
        //Boolean - If there is a stroke on each bar
        barShowStroke: true,
        //Number - Pixel width of the bar stroke
        barStrokeWidth: 2,
        //Number - Spacing between each of the X value sets
        barValueSpacing: 5,
        //Number - Spacing between data sets within X values
        barDatasetSpacing: 1,
        //String - A legend template
        legendTemplate: "<ul class=\"<%=name.toLowerCase()%>-legend\">" +
        "<% for (var i=0; i<datasets.length; i++){%>" +
        "<li>" +
        "<span style=\"background-color:<%=datasets[i].fillColor%>\"></span>" +
        "<%if(datasets[i].label){%><%=datasets[i].label%><%}%>" +
        "</li>" +
        "<%}%>" +
        "</ul>",
        //Boolean - whether to make the chart responsive
        responsive: true,
        maintainAspectRatio: true
    };

    barChartOptions.datasetFill = false;
    barChart.Bar(barChartData, barChartOptions);


    //jvectormap data
    //defined in index.html with thymleaf script
    // var visitorsData = $('#visitorsData');
    //World map by jvectormap
    $('#world-map').vectorMap({
        map: 'world_mill_en',
        backgroundColor: "transparent",
        regionStyle: {
            initial: {
                fill: '#e4e4e4',
                "fill-opacity": 1,
                stroke: 'none',
                "stroke-width": 0,
                "stroke-opacity": 1
            }
        },
        series: {
            regions: [{
                values: visitorsData,
                scale: ["#92c1dc", "#ebf4f9"],
                normalizeFunction: 'polynomial'
            }]
        },
        onRegionLabelShow: function (e, el, code) {
            if (typeof visitorsData[code] != "undefined")
                el.html(el.html() + ': ' + visitorsData[code] + ' students');
        }
    });

    //bootstrap WYSIHTML5 - text editor
    $(".textarea").wysihtml5();

    /* jQueryKnob */
    $(".knob").knob();

    //The Calender
    $("#calendar").datepicker();

    //SLIMSCROLL FOR CHAT WIDGET
    $('#chat-box').slimScroll({
        height: '250px'
    });

    /* Morris.js Charts */
    // Sales chart
    /* var area = new Morris.Area({
         element: 'revenue-chart',
         resize: true,
         data: [
             {y: '2011 Q1', item1: 2666, item2: 2666},
             {y: '2011 Q2', item1: 2778, item2: 2294},
             {y: '2011 Q3', item1: 4912, item2: 1969},
             {y: '2011 Q4', item1: 3767, item2: 3597},
             {y: '2012 Q1', item1: 6810, item2: 1914},
             {y: '2012 Q2', item1: 5670, item2: 4293},
             {y: '2012 Q3', item1: 4820, item2: 3795},
             {y: '2012 Q4', item1: 15073, item2: 5967},
             {y: '2013 Q1', item1: 10687, item2: 4460},
             {y: '2013 Q2', item1: 8432, item2: 5713}
         ],
         xkey: 'y',
         ykeys: ['item1', 'item2'],
         labels: ['Item 1', 'Item 2'],
         lineColors: ['#a0d0e0', '#3c8dbc'],
         hideHover: 'auto'
     });
     var line = new Morris.Line({
         element: 'line-chart',
         resize: true,
         data: [
             {y: '2011 Q1', item1: 2666},
             {y: '2011 Q2', item1: 2778},
             {y: '2011 Q3', item1: 4912},
             {y: '2011 Q4', item1: 3767},
             {y: '2012 Q1', item1: 6810},
             {y: '2012 Q2', item1: 5670},
             {y: '2012 Q3', item1: 4820},
             {y: '2012 Q4', item1: 15073},
             {y: '2013 Q1', item1: 10687},
             {y: '2013 Q2', item1: 8432}
         ],
         xkey: 'y',
         ykeys: ['item1'],
         labels: ['Item 1'],
         lineColors: ['#efefef'],
         lineWidth: 2,
         hideHover: 'auto',
         gridTextColor: "#fff",
         gridStrokeWidth: 0.4,
         pointSize: 4,
         pointStrokeColors: ["#efefef"],
         gridLineColor: "#efefef",
         gridTextFamily: "Open Sans",
         gridTextSize: 10
     });
 */
    //Donut Chart
    /*  var donut = new Morris.Donut({
          element: 'sales-chart',
          resize: true,
          colors: ["#3c8dbc", "#f56954", "#00a65a"],
          data: [
              {label: "Download Sales", value: 12},
              {label: "In-Store Sales", value: 30},
              {label: "Mail-Order Sales", value: 20}
          ],
          hideHover: 'auto'
      });*/


    //-------------
    //- PIE CHART -
    //-------------
    // Get context with jQuery - using jQuery's .get() method.
    // var pieChartCanvas = $("#pieChart").get(0).getContext("2d");
    //var pieChart = new Chart(pieChartCanvas);
    //defined at index.html in script tag so we can use thymeleaf objects
    /*var PieData = [
      {
        value: 700,
        color: "#f56954",
        highlight: "#f56954",
        label: "Chrome"
      },
      {
        value: 500,
        color: "#00a65a",
        highlight: "#00a65a",
        label: "IE"
      },
      {
        value: 400,
        color: "#f39c12",
        highlight: "#f39c12",
        label: "FireFox"
      },
      {
        value: 600,
        color: "#00c0ef",
        highlight: "#00c0ef",
        label: "Safari"
      },
      {
        value: 300,
        color: "#3c8dbc",
        highlight: "#3c8dbc",
        label: "Opera"
      },
      {
        value: 100,
        color: "#d2d6de",
        highlight: "#d2d6de",
        label: "Navigator"
      }
    ];
    */
    /*var pieOptions = {
        //Boolean - Whether we should show a stroke on each segment
        segmentShowStroke: true,
        //String - The colour of each segment stroke
        segmentStrokeColor: "#fff",
        //Number - The width of each segment stroke
        segmentStrokeWidth: 1,
        //Number - The percentage of the chart that we cut out of the middle
        percentageInnerCutout: 50, // This is 0 for Pie charts
        //Number - Amount of animation steps
        animationSteps: 100,
        //String - Animation easing effect
        animationEasing: "easeOutBounce",
        //Boolean - Whether we animate the rotation of the Doughnut
        animateRotate: true,
        //Boolean - Whether we animate scaling the Doughnut from the centre
        animateScale: false,
        //Boolean - whether to make the chart responsive to window resizing
        responsive: true,
        // Boolean - whether to maintain the starting aspect ratio or not when responsive, if set to false, will take up entire container
        maintainAspectRatio: false,
        //String - A legend template
        legendTemplate: "<ul class=\"<%=name.toLowerCase()%>-legend\"><% for (var i=0; i<segments.length; i++){%><li><span style=\"background-color:<%=segments[i].fillColor%>\"></span><%if(segments[i].label){%><%=segments[i].label%><%}%></li><%}%></ul>",
        //String - A tooltip template
        tooltipTemplate: "<%=value %> <%=label%> student"
    };*/
    //Create pie or douhnut chart
    // You can switch between pie and douhnut using the method below.
    //pieChart.Doughnut(PieData, pieOptions);
    //-----------------
    //- END PIE CHART -
    //-----------------

    //Fix for charts under tabs
    $('.box ul.nav a').on('shown.bs.tab', function () {
        area.redraw();
        donut.redraw();
        line.redraw();
    });

    /* The todo list plugin */
    /* $(".todo-list").todolist({
         onCheck: function (ele) {
             window.console.log("The element has been checked");
             return ele;
         },
         onUncheck: function (ele) {
             window.console.log("The element has been unchecked");
             return ele;
         }
     });*/
});
