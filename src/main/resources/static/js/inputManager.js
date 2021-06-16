class InputManager {

  constructor (testTimer) {
    this.outputManager  = new OutputManager();
    this.wordsToCompare = this.getOutputWords(this.outputManager);
    this.restarted = false;

    this.testVariables = {
      timer: testTimer,
      keyPressCount:0,
      currentWordIndex:0, 
      wordsTyped:0,
      lastWordIndex:21,
      word: [],
    }
  }

  startListening(timer) {
    let input = document.querySelector("#writing-input");
    let text  = document.querySelector("#text");
    
    this.testVariables["timer"] = timer;
    input.addEventListener("keydown", (event) => this.mainLogic(event, input, text, this.testVariables));
  } 

  mainLogic(event, input, text, vars) {

    if(this.restarted) {
      vars = this.testVariables;
    }

    let wordLength = vars["word"].length;
    
    // start the timer only on first keypress
    if (vars["keyPressCount"] === 0) {
      vars["timer"].startTimer(input, text);
      vars["keyPressCount"] = 1;
      input.placeholder = "";
    }
    
    // restart array if Control+Backspace
    else if (event.key == "Backspace" && vars["word"][wordLength-1] == "Control") {
      vars["word"] = [];
    }
    
    // pop letter from word when backspacing
    else if (event.key == "Backspace" && wordLength > 0) {
      vars["word"].pop();
    }
    
    // add letter to word array and compare it to output word
   else if(event.key === " " && wordLength > 0) {

      // get written word and rows of text
      let finalWord = vars["word"].join("");
      let actualRow = this.getActualRow();
      let originalRow = this.wordsToCompare.slice(vars["lastWordIndex"]-21, vars["lastWordIndex"]-10);
      let actualRowArr = actualRow.innerHTML.split(" ");
      let nextRow  =  document.querySelector("#next-row");

      let wordsMatch = this.wordsMatch(finalWord, originalRow[vars["currentWordIndex"]])

      // update index
      vars["currentWordIndex"] += 1;  

      // index on text without styles
      let inTextIndex = actualRowArr.indexOf(originalRow[vars["currentWordIndex"]]);

      let isLastWord = originalRow[vars["currentWordIndex"]] == originalRow[originalRow.length];
      
      // style related variables
      let styleManager = new TestCursor();
      let wordContent = originalRow[vars["currentWordIndex"]-1];
      let actualWord = actualRowArr[inTextIndex-1];

      if(wordsMatch) {

        // change color of previous word to green
        actualRowArr[inTextIndex-1] = styleManager.changeColor(actualWord, wordContent, true);
        vars["wordsTyped"] += 1;
      } 

      else if(wordsMatch === false) {
        
        // change color of previous word to red
        actualRowArr[inTextIndex-1] = styleManager.changeColor(actualWord, wordContent, false);

      }
      
      if(isLastWord) {

        // update both rows and index in original text
        actualRow.innerHTML = nextRow.innerHTML;
        vars["lastWordIndex"] += 11;
        actualRowArr = actualRow.innerHTML.split(" ");
        nextRow.innerHTML = this.outputManager.getRowOfWords(this.wordsToCompare, vars["lastWordIndex"]-10, vars["lastWordIndex"]+1);
        
        vars["currentWordIndex"] = 0;
        styleManager.changeFocus(actualRow, actualRowArr, 0);

      } else if(!isLastWord){
        
        // put focus on actual word
        styleManager.changeFocus(actualRow, actualRowArr, inTextIndex);
      }

      // send actual amount of words typed
      vars["timer"].setWordsTyped(vars["wordsTyped"]);
      vars["word"] = [];
      input.value = "";
   }
    
    // special characters ain't letters but their key contains letters
   else if(this.itsLetter(event.key) && !this.itsSpecialCharacter(event.key)) {
      vars["word"].push(event.key);
    }
  }


  wordsMatch(writtenWord, word) {
    if(writtenWord === word) {
      return true;
    }

    return false;

  }

  itsLetter(letter) {

    if (/[\w\W]/.test(letter)) {
      return true;
    }
    return false;
  }
  
  itsSpecialCharacter(word) {

    let specialCharactersList = [
      "Backspace",
      "Shift",
      "Enter",
      "Alt",
      "ArrowUp",
      "ArrowLeft",
      "ArrowDown",
      "ArrowRight",
      " "
    ];

    if (specialCharactersList.includes(word)) {
      return true;
    }
    return false;
  }

  getOutputWords(manager) {

    let outputManager = manager;
    let text = outputManager.getText();
    let words = text.split(" ");
    return words;
  }

  getActualRow() {
    return document.querySelector("#actual-row");
  }

  getNextRow() {
    return document.querySelector("#next-row");
  }

  updateRow(row, newRow) {
    row.innerHTML = newRow.innerHTML;
  }

  getWPM(wordsTyped, timeInSeconds) {
    let wpm = Math.floor(wordsTyped / (timeInSeconds/60));
  }

  reset() {

    let input = document.querySelector("#writing-div input");
    let inputClone = input.cloneNode(true);
    input.parentNode.replaceChild(inputClone, input);
    this.restarted = true;

    this.testVariables["timer"].restart();
    this.testVariables["keyPressCount"] = 0;
    this.testVariables["currentWordIndex"] = 0;
    this.testVariables["wordsTyped"] = 0;
    this.testVariables["lastWordIndex"] = 21;
    this.testVariables["word"] = [];

    let actRow = this.getActualRow();
    let nextRow = this.getNextRow();
    let text = this.outputManager.getText().split(" ");
    actRow.innerHTML = this.outputManager.getRowOfWords(text, 0, 11);
    nextRow.innerHTML = this.outputManager.getRowOfWords(text, 11, 22);
  }

}
