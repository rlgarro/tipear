class OutputManager {

  constructor (text) {
    this.text = text;
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

  outputTestResult(writingInput) {
    writingInput.readOnly = true;
    console.log("executing")

    // hide old test info
    document.getElementById("text").style.display = "none";
    document.getElementById("writing-div").style.display = 'none';
    document.getElementById("info-div").style.display = "none";

    // show results
    document.getElementById("restart").innerHTML = "New test";
    document.getElementById("test-result").style.display = "flex";
    document.getElementById("test-div").style.height = "500px";


   }

}