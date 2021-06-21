var test = "";
window.addEventListener("load", () => {
  // if there's any sign remove it passed 2secs
  if (document.getElementById("conf-pass") != null) {
    setTimeout(function(){
        document.getElementById("conf-pass").style.display = "none";
    }, 5000);
  }

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

  let regex = /\\n/;
  fetch(url).then(resp =>{
    if(resp.ok) {
        // first get text
        resp.text().then(text => {
            let time = configElements(e);

            // arr containing all texts with their respective info (name, content, author)
            let texts = JSON.parse(text);


            test = new Test(time, new OutputManager(texts, 0));
            test.start();

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
  new OutputManager([[]], 0).resetStyles();
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