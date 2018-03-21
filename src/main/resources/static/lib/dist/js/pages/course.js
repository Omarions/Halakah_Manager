/**
 * Created by Omar on 30-May-17.
 */
/**
 * Populate Teachers Dropdown List based on selection of Activity Dropdown List
 */
function populateTeachersDropdownList() {
    //the value of the selection which is the activity ID.
    var selectedActivityId = $("#selectActivity").val();

    /** Teachers variable comes from thymeleaf template that defined in script tag with attr th:inline.
     * Here we loop over the map of activity and its teachers (i.e. Map<Integer, Set<ActivityTeacher>>).
     * where the key is the activity ID and the value is set of objects of ActivityTeacher which is a model to represent
     * the teacher with his ID and name only, so we can avoid StackOverFlow error, that occurred when we send the Set
     * with the original class of Teacher with its dependencies of other classes.
     * */
    for (var a in teachers) {
        //check the selected activity with each key in the map
        if (a == selectedActivityId) {
            //enable the teachers dropdown list
            $('#selectTeacher').removeAttr('disabled');
            //add first Option child to the dropdown list
            $('#selectTeacher').html("<option value='0' text='Select Teacher'>Select Teacher</option>");
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

/**
 * Populate Teachers Dropdown List based on the Activity of the course
 */
function populateActivityTeachers(id) {
    //the value of the selection which is the activity ID.
    var selectedActivityId = id; //$("#selectActivity").val();

    /** Teachers variable comes from thymeleaf template that defined in script tag with attr th:inline.
     * Here we loop over the map of activity and its teachers (i.e. Map<Integer, Set<ActivityTeacher>>).
     * where the key is the activity ID and the value is set of objects of ActivityTeacher which is a model to represent
     * the teacher with his ID and name only, so we can avoid StackOverFlow error, that occurred when we send the Set
     * with the original class of Teacher with its dependencies of other classes.
     * */
    for (var a in teachers) {
        //check the selected activity with each key in the map
        if (a == selectedActivityId) {
            //enable the teachers dropdown list
            $('#selectTeacher').removeAttr('disabled');
            //add first Option child to the dropdown list
            $('#selectTeacher').html("<option value='0' text='Select Teacher'>Select Teacher</option>");
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

