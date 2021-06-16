class OutputManager {

  constructor () {
    this.text = this.fetchText();
  }

  // get text from api
  fetchText() {
    
    // hardcoded text for NOW
    let text = 
      `Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent vitae magna gravida, maximus massa vel, suscipit tortor. Donec porttitor augue non ipsum sodales auctor. Proin sodales non leo sit amet ullamcorper. Vivamus ac maximus arcu. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Morbi posuere ex ultricies, dapibus purus nec, ultrices tellus. Integer imperdiet ac mi ut vulputate. Donec ornare, lectus in eleifend interdum, nunc nunc rhoncus ipsum, a ultricies velit orci eget libero. Proin nec consectetur quam, facilisis vestibulum tortor.`
    return text;
  }

  // outputs content when page loads
  outputContent(timer) {

    // set view to default
    document.querySelector("#result").style.display = "none";
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
    let secondRow = this.getRowOfWords(editedText, 11, 22);

    editedText[0] = "<span id='change-bg'>" + editedText[0] + "</span>";
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

}
