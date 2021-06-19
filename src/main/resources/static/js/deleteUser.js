window.addEventListener("load", () =>{
    let delButton = document.querySelector("#delete-account");
    let delDiv = document.querySelector("#delete-form div");
    let rejButton = document.querySelector("#reject-del");
    delButton.addEventListener("click", () => {
        delDiv.style.display = "flex";
    });

    rejButton.addEventListener("click", () => {
        delDiv.style.display = "none";
    });
});