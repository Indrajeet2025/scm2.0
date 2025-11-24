console.log("admin script loaded888....");

const fileInput = document.querySelector("#contactImageInput");
const previewImg = document.querySelector("#upload_image_preview");

if (fileInput && previewImg) {
    fileInput.addEventListener('change', function (event) {
        const file = event.target.files[0];
        if (!file) return;

        const reader = new FileReader();
        reader.onload = function (e) {
            previewImg.setAttribute("src", e.target.result);
        };
        reader.readAsDataURL(file);
    });
}
