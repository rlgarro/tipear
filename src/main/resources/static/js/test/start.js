var test = "";
window.addEventListener("load", () => {

  // if there's any sign remove it passed 5secs
  if (document.getElementById("conf-pass") != null) {
    setTimeout(function(){
        document.getElementById("conf-pass").style.display = "none";
    }, 5000);
  }

  let menuLinks = document.getElementsByClassName("testStarter");
  menuLinks = [...menuLinks];

  menuLinks.forEach(link  => {
    link.addEventListener("click", (e) => start(e));
  });
});

function getInfo(regex, text) {
    let newText = text.split("-");
    newText[1] = newText[1].replace(regex, " ");
    return newText;
}

function start(e) {

  let url = "http://localhost:8080/test/text";

  let regex = /\\n/;
  fetch(url).then(resp =>{
    if(resp.ok) {
        // first get text
        resp.text().then(text => {
            let time = configElements(e);

            // arr containing all texts
            let texts = JSON.parse(text);
            console.log(texts);
            texts = this.shuffleArray(texts);

            test = new Test(time, new OutputManager(texts, 0));
            test.start();

            // menu buttons
            let gobackButton = document.getElementById("goback");
            let restartButton = document.getElementById("restart");
            gobackButton.addEventListener("click", goBack);
            restartButton.addEventListener("click", restartTest);

            // buttons after test ends
            let replayButton = document.getElementById("retry-test");
            replayButton.addEventListener("click", replayTest);

        });
    }
  });
}

function replayTest() {
  test.restart(false);
}

function restartTest() {
  test.restart(true);
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

function shuffleArray(array) {
  // Fisher-yates algorithm
  for (let i = array.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    const temp = array[i];
    array[i] = array[j];
    array[j] = temp;
  }
  return array;
}