window.addEventListener("load", () => {
    document.getElementById("sign-up").addEventListener("click", () =>{
        let passwordValue = document.getElementById("passwd-inp").value;
        if (passwordValue.length < 8) {
            document.getElementById("passwd-alert").style.display = "block";
        }
    });
});