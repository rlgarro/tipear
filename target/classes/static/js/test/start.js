var test = "";
window.addEventListener("load", () => {

  let time = document.getElementById("timeSpan").innerHTML;
  // if there's any sign remove it passed 5secs
  if (document.getElementById("conf-pass") != null) {
    setTimeout(function(){
        document.getElementById("conf-pass").style.display = "none";
    }, 5000);
  }
  start(time);

});

function getInfo(regex, text) {
    let newText = text.split("-");
    newText[1] = newText[1].replace(regex, " ");
    return newText;
}

function start(time) {

  let url = "http://localhost:8080/texts/all";

  let regex = /\\n/;
  fetch(url).then(resp =>{
    if(resp.ok) {
        // first get text
        resp.text().then(text => {
            let vars = config(time);
            let finalTime = vars[0];
            let wordsPerRow = vars[1];

            // arr containing all texts
            let texts = JSON.parse(text);
            texts = this.shuffleArray(texts);
            console.log(texts);

            test = new Test(finalTime, new OutputManager(texts, 0, wordsPerRow, null, true), false);
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
  new OutputManager(null, null, null, null, false).resetStyles();
}

function config(time) {
  let finalTime = time;
  let wordsPerRow = 15;

  if(window.screen.width <= 368) {
    let select = document.getElementById("time-select");
    finalTime = select.value;
    wordsPerRow = 5;
  }

  return [finalTime, wordsPerRow];
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