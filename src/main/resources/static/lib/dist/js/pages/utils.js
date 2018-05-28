function previewPhoto(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            $('#img-photo').attr('src', e.target.result);
        };

        reader.readAsDataURL(input.files[0]);
    }
}

$('#img-photo').click(function () {
    $('#input-photo').click();
});

$("#input-photo").change(function () {
    previewPhoto(this);
});