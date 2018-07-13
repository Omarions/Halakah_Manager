var tableTag = "<table id=\"all_students_dt\" class=\"table table-bordered table-striped dataTable dt-responsive\"  role=\"grid\" style=\"width: 100%;\"  aria-describedby=\"all_students_dt_info\">";

var head = "<thead><tr role=\"row\">" +
    "<th class=\"sorting_asc\" tabindex=\"0\" aria-controls=\"all_students_dt\" rowspan=\"1\" colspan=\"1\" aria-sort=\"ascending\" aria-label=\"ID: activate to sort column descending\" style=\"width: 130.4px;\">ID</th>" +
    "<th class=\"sorting\" tabindex=\"0\" aria-controls=\"all_students_dt\" rowspan=\"1\" colspan=\"1\" aria-label=\"Name: activate to sort column ascending\" style=\"width: 266.4px;\">Name</th>" +
    "<th class=\"sorting\" tabindex=\"0\"  aria-controls=\"all_students_dt\" rowspan=\"1\" colspan=\"1\" aria-label=\"Birth Date: activate to sort column ascending\" style=\"width: 160.2px;\">Birth Date</th>" +
    "<th class=\"sorting\" tabindex=\"0\" aria-controls=\"all_students_dt\"rowspan=\"1\" colspan=\"1\" aria-label=\"Country: activate to sort column ascending\" style=\"width: 266.2px;\">Country</th>" +
    "<th class=\"sorting\" tabindex=\"0\" aria-controls=\"all_students_dt\" rowspan=\"1\" colspan=\"1\"  aria-label=\"Tel: activate to sort column ascending\" style=\"width: 266.2px;\">Tel</th>" +
    "<th class=\"sorting\" tabindex=\"0\"  aria-controls=\"all_students_dt\" rowspan=\"1\" colspan=\"1\" aria-label=\"Email: activate to sort column ascending\" style=\"width: 266.2px;\">Email</th>" +
    "<th id=\"hd-course\" class=\"sorting\" tabindex=\"0\"  aria-controls=\"all_students_dt\" rowspan=\"1\" colspan=\"1\" aria-label=\"Course: activate to sort column ascending\" style=\"width: 266.2px;\">Course</th>" +
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
    "<th id=\"ft-course\" rowspan=\"1\" colspan=\"1\">Course</th>" +
    "<th id=\"ft-register-date\" rowspan=\"1\" colspan=\"1\">Register Date</th>" +
    "<th id=\"ft-start-date\" rowspan=\"1\" colspan=\"1\">Start Date</th>" +
    "<th id=\"ft-certificates\" rowspan=\"1\" colspan=\"1\">Certificates</th>" +
    "<th rowspan=\"1\" colspan=\"1\">Comments</th>" +
    "</tr>" +
    "</tfoot>";

/**
 * Populate the table based on the status of the track
 */
function populateStudentsTableByStatus(students, status) {
    var statusStudents = [];

    for (a in students) {
        var tracks = students[a].studentTracks;
        for (trIndex in tracks) {
            console.log(tracks[trIndex]["course"]);
            if (tracks[trIndex]["course"]["activity"]["id"] == activityId && tracks[trIndex].status == status) {
                statusStudents.push(students[a]);
            }
        }
    }

    console.log(status + " Students list:-");
    console.log(statusStudents);

    pupolateStudentsTable(statusStudents, status);
}

/**
 * populate and moderate students based on students list and status
 * @param students list of students to be displayed
 * @param status   the status of students
 */
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
                    bodyTag += "<td>" + getCourse(students[aI]["studentTracks"]) + "</td>";
                    bodyTag += "<td>" + getRegisterDate(students[aI]["studentTracks"]) + "</td>";
                    break;
                case "CERTIFIED":
                    bodyTag += "<td>" + getCertficates(students[aI]["certificates"]) + "</td>";
                    break;
                case "STUDYING":
                case "TEMP_STOP":
                case "FINAL_STOP":
                case "FIRED":
                    bodyTag += "<td>" + getCourse(students[aI]["studentTracks"]) + "</td>";
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
                bodyTag += "<td></td>";     //course cell
                bodyTag += "<td></td>";     //register date cell
                break;
            case "CERTIFIED":
                bodyTag += "<td></td>";     //certificates cell
                break;
            default:
                bodyTag += "<td></td>";     //course cell
                bodyTag += "<td></td>";     //register date cell
                bodyTag += "<td></td>";     //start date cell
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
            $("#all_students_dt th[id=hd-course]").remove();
            $("#all_students_dt th[id=ft-course]").remove();
            $("#all_students_dt th[id=hd-register-date]").remove();
            $("#all_students_dt th[id=ft-register-date]").remove();
            $("#all_students_dt th[id=hd-start-date]").remove();
            $("#all_students_dt th[id=ft-start-date]").remove();
            $("#all_students_dt th[id=hd-certificates]").remove();
            $("#all_students_dt th[id=ft-certificates]").remove();
            break;
        case "WAITING":
            $("#all_students_dt th[id=hd-start-date]").remove();
            $("#all_students_dt th[id=ft-start-date]").remove();
            $("#all_students_dt th[id=hd-certificates]").remove();
            $("#all_students_dt th[id=ft-certificates]").remove();
            break;
        case "CERTIFIED":
            $("#all_students_dt th[id=hd-course]").remove();
            $("#all_students_dt th[id=ft-course]").remove();
            $("#all_students_dt th[id=hd-register-date]").remove();
            $("#all_students_dt th[id=ft-register-date]").remove();
            $("#all_students_dt th[id=hd-start-date]").remove();
            $("#all_students_dt th[id=ft-start-date]").remove();
            break;
        case "STUDYING":
        case "TEMP_STOP":
        case "FINAL_STOP":
        case "FIRED":
            $("#all_students_dt th[id=hd-register-date]").remove();
            $("#all_students_dt th[id=ft-register-date]").remove();
            $("#all_students_dt th[id=hd-certificates]").remove();
            $("#all_students_dt th[id=ft-certificates]").remove();
            break;
    }

    //make the table responsive
    $("#all_students_dt").DataTable()
        .columns.adjust()
        .responsive.recalc();
}

/**
 * Get track start date
 * @param tracks array of tracks
 * @returns {date} track start date
 */
function getCourse(tracks) {
    var ulTag = "<ul class=\"dtt\">";
    var liTag = " ";
    for (tI in tracks) {
        if (tracks[tI]["course"]["activity"]["id"] == activityId) {
            liTag += "<li class=\"primary\">" +
                "<div><a href=\"/admin/courses/course/" + tracks[tI]["course"]["id"] + "\"><span th:class=\"";
            if (tracks[tI]["course"]["isFull"]) {
                liTag += "label label-danger";
            } else {
                liTag += "label label-success";
            }
            liTag += "\">" + tracks[tI]["course"]["name"] + " | Free: " + tracks[tI]["course"]["freePlaces"];
            text = "${course.name}" + "</span></a></div>";
        }
    }


    ulTag += liTag;
    ulTag += "</ul>";

    return ulTag;
}

/**
 * Get track register date
 * @param tracks array of tracks
 * @returns {date} track register date
 */
function getRegisterDate(tracks) {
    for (tI in tracks) {
        if (tracks[tI]["course"]["activity"]["id"] == activityId) {
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
        if (tracks[tI]["course"]["activity"]["id"] == activityId) {
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
        var m_activity_id = certificates[cI]["studentTrack"]["course"]["activity"]["id"];
        if (m_activity_id == activityId) {
            liTag += "<li class=\"primary\"><a href=\"/admin/certificates/certificate/" + certificates[cI]["id"] + "\"><span class=\"label label-success\">" + certificates[cI]["name"] + "</span></a></li>";
        }
    }


    ulTag += liTag;
    ulTag += "</ul>";

    return ulTag;
}
