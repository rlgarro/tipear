function isValid() {
  let passwordValue = document.getElementById("passwd-inp").value;
  if (passwordValue.length < 8) {
      document.querySelector("#passwd-alert").style.display = "block";
      return false;
    }
 }
