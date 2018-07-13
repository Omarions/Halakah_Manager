//Preview the selected image file from the File Browser in the image element.
function previewPhoto(input, imgElement) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            imgElement.attr('src', e.target.result);
        };

        reader.readAsDataURL(input.files[0]);
    }
}

/*
Student Photo
 */
//Simulate 'input-photo' click event when 'img-photo' image is clicked..
$('#img-photo').click(function () {
    $('#input-photo').click();
});

//to preview the selected image file in student image element.
$("#input-photo").change(function () {
    var img_photo = $("#img-photo");
    previewPhoto(this, img_photo);
});

/*
Certificate Photo
 */
//Simulate 'input-cert-photo' click event when 'img-cert-photo' image is clicked
$('#img-cert-photo').click(function () {
    $('#input-cert-photo').click();
});

//to preview the selected image file in certificate image element
$("#input-cert-photo").change(function () {
    var img_cert_photo = $("#img-cert-photo");
    previewPhoto(this, img_cert_photo);
});

/**
 * To format 'Comments' Cell in the DataTable
 * @param objArray  the objects array that comes from the server
 * @param tblId   the table id attribute to process its cells
 */
function formatCommentsCell(objArray, tblId) {
    for (oInd in objArray) {
        //create cell id
        var tdCommentsId = 'tdComments_' + objArray[oInd]['id'];
        var comments = ' ';
        if (objArray[oInd]['comments'] == null) {
            comments = ' ';
        }  else{
            comments = objArray[oInd]['comments'];
        }
        $(tblId + ' td[id=' + tdCommentsId + ']').html(comments);
    }
    
    $(tblId).DataTable()
        .columns.adjust()
        .responsive.recalc();

}

