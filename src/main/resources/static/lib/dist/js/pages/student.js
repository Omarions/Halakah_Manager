/**
 * Created by Omar on 30-May-17.
 */
var student;
var studentTracks = [];


/**
 * Populate Tracks table based on selection of Activity Dropdown List
 */
function populateTracksTable() {
    //the value of the selection which is the activity ID.
    var selectedActivityId = $("#select-activity").val();
    var statuses = ["WAITING", "STUDYING", "CERTIFIED", "TEMP_STOPPED", "FINAL_STOPPED"];
    console.log("activity ID: " + selectedActivityId);
    var url = "/admin/students/student/activity/" + selectedActivityId + "/courses";
    $.ajax({
            url: url,
            dataType: 'json',
            success: function (data) {
                console.log(data);
                $("tr:has(td)").remove();

                $.each(data, function (index, course) {

                    console.log(course);

                    $("#tracks-data").append($('<tr>')
                        .append($('<td>').html('<div class="checkbox icheck">' +
                            '<label>' +
                            '<input type="checkbox" id="cb-select-row-' + course.id + '" value="' + course.id + '"/>' +
                            '</label>' +
                            '</div></td>'))
                        .append($('<td>').html('<label type="text" id="course_name" class="control-label">' +
                            course.name + ' </label>'))
                        .append($('<td>').html('<div class="input-group date">' +
                            '<div class="input-group-addon">' +
                            '<i class="fa fa-calendar"></i></div>' +
                            '<input type="text" class="datepicker form-control pull-right" ' +
                            'id="register-datepicker' + index + '"/></div>'))
                        .append($('<td>').html('<select size="1" id="track_status" name="track_status"> ' +
                            '<option value="WAITING" selected="selected"> WAITING </option> ' +
                            '<option value="STUDYING"> STUDYING </option> ' +
                            '<option value="CERTIFIED"> CERTIFIED </option> ' +
                            '<option value="TEMP_STOP"> TEMP_STOP </option> ' +
                            '<option value="FINAL_STOP"> FINAL_STOP </option> ' +
                            '<option value="FIRED"> FIRED </option> ' +
                            '</select></td>'))
                        .append($('<td>').html('<textarea type="text" id="track_comments" ' +
                            'class="control_label" placeholder="Comments" style="border: none; width: 100%; -webkit-box-sizing: border-box; -moz-box-sizing: border-box; box-sizing: border-box;"></textarea> </td>')
                        ));

                });
            },
            error: function (data, status, er) {
                for (var obj in data) {
                    console.log("course: " + data[obj]);

                }
                console.log("Data: " + data);
                console.log("Status: " + status);
                console.log("Error:" + er);
                alert("error: " + data + " status: " + status + " er:" + er);
            }
        }
    ).done(function () {
        $('.datepicker').datepicker({
            format: 'yyyy-mm-dd',
            todayBtn: true,
            todayHighlight: true,
            autoclose: true
        }).datepicker("setDate", new Date());
        $('input').iCheck({
            checkboxClass: 'icheckbox_square-blue',
            radioClass: 'iradio_square-blue',
            increaseArea: '20%' // optional

        });
        $('input').on('ifChecked', function (event) {

            console.log(event.type + ' callback');
            $('#student-tracks tbody tr').click();
        });
    });


}

function populateCreatedTracksTable(track) {
    $("#tracks-data").append($('<tr>')
        .append($('<td>').html('<input type="hidden" name="id" value="0"/></td>'))
        .append($('<td>').html('<input class="form-control" name="course" value="' + track.courseId + '" text="' + track.courseName + '" disabled/></td>'))
        .append($('<td>').html('<input class="form-control" name="status" value="' + track.status + '" text="' + track.status + '" disabled/></td>'))
        .append($('<td>').html('<input class="form-control" name="registerDate" value="' + track.registerDate + '" text="' + track.registerDate + '" disabled/></td>'))
        .append($('<td>').html('<input class="form-control" name="startDate" value="' + track.startDate + '" text="' + track.startDate + '" disabled/></td>'))
        .append($('<td>').html('<input class="form-control" name="evaluation" value="' + track.evaluation + '" text="' + track.evaluation + '" disabled/></td>'))
        .append($('<td>').html('<input class="form-control" name="comments" value="' + track.comments + '" text="' + track.comments + '" disabled/></td>'))
        .append($('<td>').html('<div class="tools"> <a href="#"><i class="fa fa-trash-o fa-2x"></i></a></div></td>'))
    );
}

function populateCourses() {
    //the value of the selection which is the activity ID.
    var selectedActivityId = $("#select-activity").val();
    console.log(selectedActivityId);
    var url = "/admin/students/student/activity/" + selectedActivityId + "/courses";
    $.ajax({
            url: url,
            dataType: 'json',
            success: function (data) {

                console.log(data);
                /*
                    $("#select-course").removeAttr('disabled');
                    $("#select-course").empty();
                    $("#select-status").removeAttr('disabled');
                    $("#register-datepicker").removeAttr('disabled');
                    $("#start-datepicker").removeAttr('disabled');
                    $("#input-evaluation").removeAttr('disabled');
                    $("#track-comments").removeAttr('disabled');
                */
                $.each(data, function (index, course) {

                    console.log(course);

                    $("#select-course").append('<option value="' + course.id + '" text="' + course.name + '">' + course.name + '</option>');

                });
            },
            error: function (data, status, er) {
                for (var obj in data) {
                    console.log("course: " + data[obj]);

                }
                console.log("Data: " + data);
                console.log("Status: " + status);
                console.log("Error:" + er);
                alert("error: " + data + " status: " + status + " er:" + er);
            }
        }
    ).done(function () {
        $('.datepicker').datepicker({
            format: 'yyyy-mm-dd',
            todayBtn: true,
            todayHighlight: true,
            autoclose: true
        }).datepicker("setDate", new Date());
        $("#select-course").select2({
            placeholder: "Select Course"
        });
        $("#select-status").select2({
            placeholder: "Select Status"
        });
    });
}

$('#student-tracks tbody').on('click', 'tr', function () {
    $(this).toggleClass('selected');
});

$('#coursesDT tbody').on('click', 'tr', function () {
    $(this).toggleClass('selected');
});

function createTracks() {
    var tableTracks = $('#student-tracks').DataTable();
    tableTracks.rows().every(function (rowIdx, tableLoop, rowLoop) {
        var data = this.data().length;
        console.log("i= " + rowIdx);
        console.log("row: " + data);
    });
}

function createTrack() {
    var courseId = $('#select-course').val();
    var courseName = $('#select-course').children(':selected').text();
    var trackStatus = $('#select-status').val();
    var registerDate = $('#register-datepicker').val();
    var startDate = $('#start-datepicker').val();
    var evaluation = $('#input-evaluation').val();
    var comments = $('#track-comments').val();

    var track = {
        courseId: courseId,
        courseName: courseName,
        status: trackStatus,
        registerDate: registerDate,
        startDate: startDate,
        evaluation: evaluation,
        comments: comments
    };

    studentTracks.push(track);

    return track;
}

function createStudent() {
    var id = 0;
    var name = $('#input-name').val();
    var country = $('#select-country').val();
    var gender = $('input[name=gender]:checked').val();
    var tel = $('#input-tel').val();
    var email = $('#input-email').val();
    var photo = $('#input-photo').val();
    var education = $('#input-education').val();
    var job = $('#input-job').val();
    var comments = $('#input-comments').val();

    student = {
        id: id,
        name: name,
        gender: gender,
        country: country,
        tel: tel,
        email: email,
        education: education,
        job: job,
        photo: photo,
        comments: comments,
        studentTracks: studentTracks
    };
}

/*
$('#btn-register').click(function () {
    createStudent();
    console.log(student);
    $.ajax({
        url: '/admin/students/student',
        type: 'post',
        data: student,
        success: function () {
            console.log("sent student");
        },
        error: function (data, status, er) {
            for (var key in data) {
                console.log("data[key: " + key + ", data: " + data[key] + "]");
            }

            console.log("Status: " + status);
            console.log("Error:" + er);

        }
    });
});
*/

$('#save-track').click(function () {
    var track = createTrack();

    console.log("StudentTrack: " + track);
    var table = $('#table-tracks').DataTable();
    var tools = '<div class="tools"> <a href="#"><i class="fa fa-trash-o fa-2x"></i></a></div>';

    table.row.add([track.courseId, track.courseName, track.status, track.registerDate, track.startDate, track.evaluation, track.comments, tools]).draw(false);

    //populateCreatedTracksTable(createTrack());
});





