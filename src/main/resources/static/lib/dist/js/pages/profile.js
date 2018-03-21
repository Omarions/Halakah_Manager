/**
 * Created by Omar on 26-May-17.
 * Used in Activity Profile Page.
 */
$(function () {

    "use strict";

    //Make the dashboard widgets sortable Using jquery UI
    $(".connectedSortable").sortable({
        placeholder: "sort-highlight",
        connectWith: ".connectedSortable",
        handle: ".box-header, .nav-tabs",
        forcePlaceholderSize: true,
        zIndex: 999999
    });
    $(".connectedSortable .box-header, .connectedSortable .nav-tabs-custom").css("cursor", "move");

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
            if (typeof visitorsData[code] != "undefined") {
                el.html(el.html() + ': ' + visitorsData[code] + ' students');
                //send the code of the country that user pointed to to the function of plotting the chart
                //so the bars re-plotted according the new code.
                plotBars(code);
            } else {
                plotBars("undefined")
            }
        }
    });

    /*
     * BAR CHART
     * ---------
     * Function to draw the bar chart according to the country code
     */
    function plotBars(countryCode) {
        //define empty array for chart data.
        var ar = [];
        //get the object of country that contains all statuses with its students count
        if (countryCode != "undefined") {
            var statuses = countryStudents[countryCode];
            //loop over the object properties (i.e. statuses, e.g. WAITING, STUDYING,..etc.)
            for (var d in statuses) {
                //get the student count of the status
                var studentCount = countryStudents[countryCode][d];
                //add the status and its student count to the chart data array
                ar.push([d, studentCount]);
            }
        }
        //define the chart data variable with data evaluated previous according to the country that user pointed to.
        var bar_data = {data: ar, color: "#3c8dbc"};
        //draw the chart with the data.
        $.plot("#bar-chart", [bar_data], {
            grid: {
                borderWidth: 1,
                borderColor: "#f3f3f3",
                tickColor: "#f3f3f3"
            },
            series: {
                bars: {
                    show: true,
                    barWidth: 0.5,
                    align: "center"
                }
            },
            xaxis: {
                mode: "categories",
                tickLength: 0
            }
        });
        /* END BAR CHART */
    }
});

