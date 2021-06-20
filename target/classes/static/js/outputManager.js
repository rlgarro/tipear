class OutputManager {

  constructor (textArraysInfo, actualArrIndex) {
    this.textsInfo = textArraysInfo;
    this.actualArr = this.textsInfo[actualArrIndex];
    this.actualArrIndex = actualArrIndex;
    // actual text being used
    this.text = this.actualArr[2];
  }

  updateActualArr() {
    if (this.actualArrIndex === this.textsInfo.length-1) {
        console.log(`EN ESTE MOMENTO LLEGO A 0 VERIFICA: TAIL: ${this.textsInfo.length} AAI: ${this.actualArrIndex}`)
        this.actualArrIndex = 0;
    } else {
        this.actualArrIndex += 1;
    }
    this.actualArr = this.textsInfo[this.actualArrIndex];
    this.text = this.actualArr[2];
  }

  // we'll call this function when the test ends
  // so we can display that info and if the user wants save it onto the db.
  getActualArr() {
    return this.actualArr;
  }

  // outputs content when page loads
  outputContent(timer) {

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
    console.log("FW: " + firstWord);
    let secondRow = this.getRowOfWords(editedText, 11, 22);

    editedText[0] = "<span id='change-bg'>" + firstWord + "</span>";
    let firstRow = this.getRowOfWords(editedText, 0, 11);

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
    console.log("executing")
    console.log("SCORE ON OM: " + score);

    // hide old test info
    document.getElementById("text").style.display = "none";
    document.getElementById("writing-div").style.display = 'none';
    document.getElementById("info-div").style.display = "none";

    // show results
    document.getElementById("test-result").style.display = "flex";
    document.getElementById("test-div").style.height = "500px";
    document.getElementById("song-title").innerHTML = this.actualArr[0];
    document.getElementById("band-name").innerHTML = this.actualArr[1];
    document.getElementById("score-span").innerHTML = score.toString();


   }

}