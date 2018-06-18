function previewPhoto(input, imgElement) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            imgElement.attr('src', e.target.result);
        };

        reader.readAsDataURL(input.files[0]);
    }
}

$('#img-photo').click(function () {
    $('#input-photo').click();
});

$("#input-photo").change(function () {
    var img_photo = $("#img-photo");
    previewPhoto(this, img_photo);
});

$('#img-cert-photo').click(function () {
    $('#input-cert-photo').click();
});

$("#input-cert-photo").change(function () {
    var img_cert_photo = $("#img-cert-photo");
    previewPhoto(this, img_cert_photo);
});