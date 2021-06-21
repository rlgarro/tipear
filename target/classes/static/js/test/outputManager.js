class OutputManager {

  constructor (textArraysInfo, actualArrIndex) {
    this.textsInfo = textArraysInfo;
    this.actualArr = this.textsInfo[actualArrIndex];
    this.actualArrIndex = actualArrIndex;
    this.wordsPerRow = 15;
    // actual text being used
    this.text = this.actualArr["content"];
  }

  updateActualArr() {
    if (this.actualArrIndex === this.textsInfo.length-1) {
        this.actualArrIndex = 0;
    } else {
        this.actualArrIndex += 1;
    }
    this.actualArr = this.textsInfo[this.actualArrIndex];
    this.text = this.actualArr["content"];
  }

  /* we'll call this function when the test ends
  // so we can display that info and if the user wants save it onto the db.
  getActualArr() {
    return this.actualArr;
  }*/

  resetStyles() {
    // hide old test info
    document.getElementById("test-result").style.display = "none";

    // show new test
    document.getElementById("text").style.display = "block";
    document.getElementById("writing-div").style.display = 'flex';
    document.getElementById("info-div").style.display = "block";
    document.getElementById("test-div").style.height = "50%";
  }

  // outputs content when page loads
  outputContent(timer) {

    this.resetStyles();

    // set view to default
    let rows = [...document.getElementsByClassName("row")];
    rows.forEach((row) => { row.style.display = "block"});

    // reset input config
    let inp = document.querySelector("#writing-input");
    inp.value = "";
    inp.placeholder = "Type to start the test...";
    inp.style.display = 'block';
    inp.readOnly = false;

    // output text
    let editedText = this.text.split(" ");
    let firstWord = editedText[0];
    let secondRow = this.getRowOfWords(editedText, this.wordsPerRow, this.wordsPerRow*2);

    editedText[0] = "<span id='change-bg'>" + firstWord + "</span>";
    let firstRow = this.getRowOfWords(editedText, 0, this.wordsPerRow);

    let firstRowDiv = document.querySelector("#actual-row");
    let secondRowDiv = document.querySelector("#next-row");

    firstRowDiv.innerHTML = firstRow;
    secondRowDiv.innerHTML = secondRow;

    // output time
    timer.outputTimer(timer.initialTime);

  }

  // returns first 13 words
  getRowOfWords (textArr, start, end) {
     
    return textArr.slice(start, end).join(" ");
  }

  getText() {
    return this.text;
  }


  /*
      this function gets executed when the test ends
      called either by the inputManager when typed the whole text
      or the timer when time ends
  */
  outputTestResult(writingInput, score) {
    writingInput.readOnly = true;

    // hide old test info
    document.getElementById("text").style.display = "none";
    document.getElementById("writing-div").style.display = 'none';
    document.getElementById("info-div").style.display = "none";

    // show results
    document.getElementById("test-result").style.display = "flex";
    document.getElementById("test-div").style.height = "500px";
    document.getElementById("song-title").innerHTML = this.actualArr["title"];
    document.getElementById("band-name").innerHTML = "By " + this.capitalize(this.actualArr["author"]);
    document.getElementById("score-span").innerHTML = score.toString() + "WPM";


   }

   capitalize(words) {
        let arr = words.split(" ");
        words = [];
        arr.forEach(word => {
            word = word.charAt(0).toUpperCase() + word.slice(1);
            words.push(word);
        });
        return words.join(" ");
   }

}