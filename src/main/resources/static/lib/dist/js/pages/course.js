var tableTag = "<table id=\"all_students_dt\" class=\"table table-bordered table-striped dataTable dt-responsive\"  role=\"grid\" style=\"width: 100%;\"  aria-describedby=\"all_students_dt_info\">";

var head = "<thead><tr role=\"row\">" +
    "<th class=\"sorting_asc\" tabindex=\"0\" aria-controls=\"all_students_dt\" rowspan=\"1\" colspan=\"1\" aria-sort=\"ascending\" aria-label=\"ID: activate to sort column descending\" style=\"width: 130.4px;\">ID</th>" +
    "<th class=\"sorting\" tabindex=\"0\" aria-controls=\"all_students_dt\" rowspan=\"1\" colspan=\"1\" aria-label=\"Name: activate to sort column ascending\" style=\"width: 266.4px;\">Name</th>" +
    "<th class=\"sorting\" tabindex=\"0\"  aria-controls=\"all_students_dt\" rowspan=\"1\" colspan=\"1\" aria-label=\"Birth Date: activate to sort column ascending\" style=\"width: 160.2px;\">Birth Date</th>" +
    "<th class=\"sorting\" tabindex=\"0\" aria-controls=\"all_students_dt\"rowspan=\"1\" colspan=\"1\" aria-label=\"Country: activate to sort column ascending\" style=\"width: 266.2px;\">Country</th>" +
    "<th class=\"sorting\" tabindex=\"0\" aria-controls=\"all_students_dt\" rowspan=\"1\" colspan=\"1\"  aria-label=\"Tel: activate to sort column ascending\" style=\"width: 266.2px;\">Tel</th>" +
    "<th class=\"sorting\" tabindex=\"0\"  aria-controls=\"all_students_dt\" rowspan=\"1\" colspan=\"1\" aria-label=\"Email: activate to sort column ascending\" style=\"width: 266.2px;\">Email</th>" +
    "<th id=\"hd-register-date\" class=\"sorting\" tabindex=\"0\"  aria-controls=\"all_students_dt\" rowspan=\"1\" colspan=\"1\" aria-label=\"Register Date: activate to sort column ascending\" style=\"width: 266.2px;\">Register Date</th>" + "<th id=\"hd-start-date\" class=\"sorting\" tabindex=\"0\"  aria-controls=\"all_students_dt\" rowspan=\"1\" colspan=\"1\" aria-label=\"Start Date: activate to sort column ascending\" style=\"width: 266.2px;\">Start Date</th>" +
    "<th id=\"hd-certificates\" class=\"sorting\" tabindex=\"0\"  aria-controls=\"all_students_dt\" rowspan=\"1\"" +
    " colspan=\"1\" aria-label=\"Certificates: activate to sort column ascending\" style=\"width: 266.2px;\">Certificates</th>" +
    "<th class=\"sorting\" tabindex=\"0\" aria-controls=\"all_students_dt\" rowspan=\"1\" colspan=\"1\" aria-label=\"Comments: activate to sort column ascending\" style=\"width: 266.2px;\">Comments</th></tr></thead>";

var footer = "<tfoot>" +
    "<tr role=\"row\">" +
    "<th rowspan=\"1\" colspan=\"1\">ID</th>" +
    "<th rowspan=\"1\" colspan=\"1\">Name</th>" +
    "<th rowspan=\"1\" colspan=\"1\">Birth Date</th>" +
    "<th rowspan=\"1\" colspan=\"1\">Country</th>" +
    "<th rowspan=\"1\" colspan=\"1\">Tel</th>" +
    "<th rowspan=\"1\" colspan=\"1\">Email</th>" +
    "<th id=\"ft-register-date\" rowspan=\"1\" colspan=\"1\">Register Date</th>" +
    "<th id=\"ft-start-date\" rowspan=\"1\" colspan=\"1\">Start Date</th>" +
    "<th id=\"ft-certificates\" rowspan=\"1\" colspan=\"1\">Certificates</th>" +
    "<th rowspan=\"1\" colspan=\"1\">Comments</th>" +
    "</tr>" +
    "</tfoot>";

/**
 * Created by Omar on 30-May-17.

 * Populate Teachers Dropdown List based on selection of Activity Dropdown List
 *
 function populateTeachersDropdownList() {
    //the value of the selection which is the activity ID.
    var selectedActivityId = $("#selectActivity").val();

    /!** Teachers variable comes from thymeleaf template that defined in script tag with attr th:inline.
     * Here we loop over the map of activity and its teachers (i.e. Map<Integer, Set<ActivityTeacher>>).
     * where the key is the activity ID and the value is set of objects of ActivityTeacher which is a model to represent
     * the teacher with his ID and name only, so we can avoid StackOverFlow error, that occurred when we send the Set
     * with the original class of Teacher with its dependencies of other classes.
     * *!/

    for (var a in teachers) {
        //check the selected activity with each key in the map
        if (a == selectedActivityId) {
            //enable the teachers dropdown list
            $('#selectTeacher').removeAttr('disabled');
            //add first Option child to the dropdown list
            $('#selectTeacher').html("<option value="
            0
            " text="
            Select
            Teacher
            ">Select Teacher</option>"
        )
            ;
            //add Option childes with the data of teachers
            $.each(teachers[a], function (i, item) {
                console.log("value: " + i);
                console.log("text: " + item);
                $('#selectTeacher').append($('<option>', {
                    value: i,   //the id of the teacher
                    text: item   //the name of the teacher
                }));
            });


        }
    }
}
 */

/*
/!**
 * Populate Teachers Dropdown List based on the Activity of the course

function populateActivityTeachers(id) {
    //the value of the selection which is the activity ID.
    var selectedActivityId = id; //$("#selectActivity").val();

    /!** Teachers variable comes from thymeleaf template that defined in script tag with attr th:inline.
     * Here we loop over the map of activity and its teachers (i.e. Map<Integer, Set<ActivityTeacher>>).
     * where the key is the activity ID and the value is set of objects of ActivityTeacher which is a model to represent
     * the teacher with his ID and name only, so we can avoid StackOverFlow error, that occurred when we send the Set
     * with the original class of Teacher with its dependencies of other classes.
     * *!/
    for (var a in teachers) {
        //check the selected activity with each key in the map
        if (a == selectedActivityId) {
            //enable the teachers dropdown list
            $('#selectTeacher').removeAttr('disabled');
            //add first Option child to the dropdown list
            $('#selectTeacher').html("<option value="
            0
            " text="
            Select
            Teacher
            ">Select Teacher</option>"
        )
            ;
            //add Option childes with the data of teachers
            $.each(teachers[a], function (i, item) {
                console.log("value: " + i);
                console.log("text: " + item);
                var teacher = {id: i, name: item};
                $('#selectTeacher').append($('<option>', {
                    value: teacher.id,   //the id of the teacher
                    text: teacher.name   //the name of the teacher
                }));
            });


        }
    }
}
*/

/**
 * Populate the table based on the status of the track
 */
function populateStudentsTableByStatus(students, status) {
    var statusStudents = [];

    for (a in students) {
        var tracks = students[a].studentTracks;
        for (trIndex in tracks) {
            if (tracks[trIndex].course.id == courseId && tracks[trIndex].status == status) {
                statusStudents.push(students[a]);
            }
        }
    }

    console.log(status + " Students list:-");
    console.log(statusStudents);

    pupolateStudentsTable(statusStudents, status);
}

function pupolateStudentsTable(students, status) {
    $("#students-table-div").empty();
    $("#students-table-div").html(tableTag);
    $("#all_students_dt").append(head);
    var bodyTag = "<tbody>";

    if (students.length > 0) {
        //loop over the list of status-students to fill the data-cells
        for (aI in students) {
            bodyTag += "<tr role=\"row\">" +
                "<td>" + students[aI]["id"] + "</td>" +
                "<td>" + students[aI]["name"] + "</td>" +
                "<td>" + students[aI]["birthDate"] + "</td>" +
                "<td>" + students[aI]["country"]["arabicName"] + "</td>" +
                "<td>" + students[aI]["tel"] + "</td>" +
                "<td>" + students[aI]["email"] + "</td>";
            switch (status) {
                case "ALL":
                    break;
                case "WAITING":
                    bodyTag += "<td>" + getRegisterDate(students[aI]["studentTracks"]) + "</td>";
                    break;
                case "CERTIFIED":
                    bodyTag += "<td>" + getCertficates(students[aI]["certificates"]) + "</td>";
                    break;
                case "STUDYING":
                case "TEMP_STOP":
                case "FINAL_STOP":
                case "FIRED":
                    bodyTag += "<td>" + getStartDate(students[aI]["studentTracks"]) + "</td>";
                    break;
            }
            bodyTag += "<td>" + students[aI]["comments"] + "</td>" +
                "</tr>";
        }
    } else {
        bodyTag += "<tr role=\"row\">" +
            "<td></td>" +
            "<td></td>" +
            "<td></td>" +
            "<td></td>" +
            "<td></td>" +
            "<td></td>";
        switch (status) {
            case "ALL":
                break;
            case "WAITING":
                bodyTag += "<td></td>";
                break;
            case "CERTIFIED":
                bodyTag += "<td></td>";
                break;
            default:
                bodyTag += "<td></td>";
                bodyTag += "<td></td>";
                break;
        }
        bodyTag += "<td></td>" +
            "</tr>";
    }

    bodyTag += "</body>";
    $("#all_students_dt").append(bodyTag);

    $("#all_students_dt").append(footer);

    //remove columns based on the status
    switch (status) {
        case "ALL":
            $("table th[id=hd-register-date]").remove();
            $("table th[id=ft-register-date]").remove();
            $("table th[id=hd-start-date]").remove();
            $("table th[id=ft-start-date]").remove();
            $("table th[id=hd-certificates]").remove();
            $("table th[id=ft-certificates]").remove();
            break;
        case "WAITING":
            $("table th[id=hd-start-date]").remove();
            $("table th[id=ft-start-date]").remove();
            $("table th[id=hd-certificates]").remove();
            $("table th[id=ft-certificates]").remove();
            break;
        case "CERTIFIED":
            $("table th[id=hd-register-date]").remove();
            $("table th[id=ft-register-date]").remove();
            $("table th[id=hd-start-date]").remove();
            $("table th[id=ft-start-date]").remove();
            break;
        case "STUDYING":
        case "TEMP_STOP":
        case "FINAL_STOP":
        case "FIRED":
            $("table th[id=hd-register-date]").remove();
            $("table th[id=ft-register-date]").remove();
            $("table th[id=hd-certificates]").remove();
            $("table th[id=ft-certificates]").remove();
            break;
    }

    //make the table responsive
    $("#all_students_dt").DataTable({
        responsive: true
    });
}

/**
 * Get track register date
 * @param tracks array of tracks
 * @returns {date} track register date
 */
function getRegisterDate(tracks) {
    for (tI in tracks) {
        if (tracks[tI]["course"]["id"] == courseId) {
            return tracks[tI]["registerDate"];
        }
    }

}

/**
 * Get track start date
 * @param tracks array of tracks
 * @returns {date} track start date
 */
function getStartDate(tracks) {
    for (tI in tracks) {
        if (tracks[tI]["course"]["id"] == courseId) {
            return tracks[tI]["startDate"];
        }
    }

}

/**
 * Get list tag of certificates of student for the course.
 * @param certificates
 */
function getCertficates(certificates) {
    var ulTag = "<ul class=\"dtt\">";
    var liTag = " ";
    for (cI in certificates) {
        var m_course_id = certificates[cI]["studentTrack"]["course"]["id"];
        console.log(m_course_id);
        if (m_course_id == courseId) {
            liTag += "<li class=\"primary\"><a href=\"/admin/certificates/certificate/" + certificates[cI]["id"] + "\"><span class=\"label label-success\">" + certificates[cI]["name"] + "</span></a></li>";
        }
    }


    ulTag += liTag;
    ulTag += "</ul>";

    return ulTag;
}
