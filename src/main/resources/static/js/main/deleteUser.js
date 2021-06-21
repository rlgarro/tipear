window.addEventListener("load", () =>{
    let delButton = document.querySelector("#delete-account");
    let delDiv = document.querySelector("#delete-div");
    let delDivButtons = document.querySelector("#delete-buttons-div");
    let rejButton = document.querySelector("#reject-del");
    delButton.addEventListener("click", () => {
        delDiv.style.display = "flex";
        delDivButtons.style.display = "flex";
    });

    rejButton.addEventListener("click", () => {
        delDiv.style.display = "none";
    });
});