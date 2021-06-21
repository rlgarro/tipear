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

  // array that holds all the texts info the test can use
  // (arr with more arrs where each one has a band name, a song name and the song)
  let textArraysInfo = [];
  let regex = /\\n/;
  fetch(url).then(resp =>{
    if(resp.ok) {
        // first get text
        resp.text().then(text => {
            //textInfo = getInfo(regex, text);
            let time = configElements(e);
            let texts = JSON.parse(text);
            console.log(texts[1]["content"]);


            // hardcoded arr info
            textArraysInfo = [
                ["song one", "song_one_band", "song one song one song one song one song one song one song one song one song one song one song one song one song one song one song one song one song one song one song one song one song one song one song one song one"],
                ["song two", "song_two_band", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum ac interdum mi, suscipit porttitor est. Aliquam luctus nisl sed libero fringilla, in euismod diam luctus. Maecenas eu urna risus. Integer vel libero ultrices, sagittis lorem sed, vulputate turpis. Nunc sagittis sodales purus, tincidunt blandit turpis malesuada vitae. Maecenas imperdiet nisi non mi lacinia malesuada. Ut vel scelerisque felis. Quisque vehicula finibus lacus, vel dictum leo porttitor vel. Aliquam sed euismod mi, sit amet sodales nisi. Nunc lacinia pretium mattis. Etiam quis felis sodales, luctus quam ornare, molestie odio. Fusce tempor, ligula quis finibus fermentum, quam purus condimentum enim, ultricies pulvinar ligula quam id augue. Duis mi turpis, interdum sodales luctus id, aliquet vitae ligula. Sed in turpis vel mauris commodo interdum in vel mi. Morbi ac elit sed leo commodo imperdiet nec non metus."],
                ["song three", "song_three_band", "song three song three song three"],
                ["song four", "song_four_band", "song four song four song four"],
                ["song five", "song_five_band", "song five song five song five"],
                ["song six", "song_six_band", "song six song six song six"]
            ];

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