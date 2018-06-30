/**
 * Created by Omar on 30-May-17.
 */
/**
 * Populate Courses Dropdown List based on selection of Activity Dropdown List

function populateCoursesDropdownList() {
    //the value of the selection which is the activity ID.
    var selectedActivityIds = $("#selectActivities").val();
    console.log(selectedActivityIds);
    //console.log(courses);
    //add first Option child to the dropdown list
    $('#selectCourses').html("<option value='0' text='Select Courses'>Select Courses</option>");
    /** Courses variable comes from thymeleaf template that defined in script tag with attr th:inline.
     * Here we loop over the map of activity and its courses (i.e. Map<Integer, Map<Integer, String>>).
     * where the key is the activity ID and the value is map of key:value pair with key is the courseId and the value is
     * course name plus activity name like :
     * Map<ActivityID, Map<CourseID, TextToBeDisplayed 'might be course name'>> -> Map<1, Map<2 , "courseName : activityName">>
     * the teacher with his ID and name only, so we can avoid StackOverFlow error, that occurred when we send the Set
     * with the original class of Teacher with its dependencies of other classes.
     * 
    for (var a in courses) {
        console.log("a: " + a);
        //console.log(selectedActivityId);
        //loop over selected values of multiple dropdown list
        for (var activityId in selectedActivityIds) {
            console.log("activityId: " + activityId);
            console.log(selectedActivityIds[activityId]);

            if (a == selectedActivityIds[activityId]) {
                console.log(a);
                //enable the courses dropdown list
                $('#selectCourses').removeAttr('disabled');
                //add Option childes with the data of teachers
                $.each(courses[a], function (i, item) {
                    console.log("value: " + i);
                    console.log("text: " + item);
                    $('#selectCourses').append($('<option>', {
                        value: i,       //the id of the course
                        text: item      //the name of the course name plus activity name
                    }));
                });


            }
        }
    }
}
 */