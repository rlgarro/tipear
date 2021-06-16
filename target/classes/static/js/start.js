var test = "";
window.addEventListener("load", () => {
  let links = document.getElementsByClassName("testStarter");
  links = [...links];
  links.forEach(link  => {


    link.addEventListener("click", (e) => startTest(e));
  });
});

function startTest(e) {

  let link = e.target;
  let time = 60;

  if (link.id === "60") {
    time = 60; 
  }
  else if (link.id === "180") {
    time = 180; 
  }
  else if (link.id === "300") {
    time = 300;
  }

  document.getElementById("start-div").style.display = "none";
  document.getElementById("test-div").style.display = "block";

  test = new Test(time);
  test.start();
  
  // add event listener to go back and restart buttons
  let gobackButton = document.getElementById("goback");
  let restartButton = document.getElementById("restart");
  gobackButton.addEventListener("click", goBack);
  restartButton.addEventListener("click", restartTest);
}

function restartTest() {
  test.restart();
}

function goBack() {

  test.restartStats(); 
  document.getElementById("start-div").style.display = "flex";
  document.getElementById("test-div").style.display = "none";
}

