var test = "";
window.addEventListener("load", () => {
  let links = document.getElementsByClassName("testStarter");
  links = [...links];
  links.forEach(link  => {


    link.addEventListener("click", (e) => start(e));
  });
});

function getInfo(regex, text) {
    let newText = text.split("-");
    newText[1] = newText[1].replace(regex, " ");
    return newText;
}

function start(e) {

  // i know it looks ugly
  let url = "http://localhost:8080/test/randomText";
  let textInfo = [];
  let regex = /\\n/;
  fetch(url).then(resp =>{
    if(resp.ok) {
        // first get text
        resp.text().then(text => {
            textInfo = getInfo(regex, text);
            let time = configElements(e);

            console.log("antes", textInfo[1]);

            test = new Test(time, new OutputManager(textInfo[1]));
            test.start();
            console.log("despues");

            // add event listener to go back and restart buttons
            let gobackButton = document.getElementById("goback");
            let restartButton = document.getElementById("restart");
            gobackButton.addEventListener("click", goBack);
            restartButton.addEventListener("click", restartTest);

        });
    }
  });
}

function restartTest() {
  test.restart();
}

function goBack() {

  test.restartStats(); 
  document.getElementById("start-div").style.display = "flex";
  document.getElementById("test-div").style.display = "none";
}

function configElements(event) {
  let link = event.target;
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
  return time;
}