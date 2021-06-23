class InputManager {

  constructor (testTimer) {
    this.outputManager  = testTimer.getOutputManager();
    this.wordsToCompare = this.getOutputWords(this.outputManager);
    this.indexOfLastWordInText = this.wordsToCompare.length-1;
    this.wordsPerRow = this.outputManager.wordsPerRow;
    this.restarted = false;

    this.testVariables = {
      timer: testTimer,
      keyPressCount:0,
      currentWordIndex:0, 
      wordsTyped:0,
      lastWordIndex: (this.wordsPerRow * 2) -1,
      currentWordIndexInText:0,
      word: [],
    }
  }

  startListening(timer) {
    let input = document.querySelector("#writing-input");
    let text  = document.querySelector("#text");
    
    this.testVariables["timer"] = timer;

    let isMobile = this.userIsOnMobile();
    if (isMobile) {
        isMobile = true;
        input.addEventListener("input", (event) => this.mainLogic(event, input, text, this.testVariables, isMobile));
    } else {
        input.addEventListener("keydown", (event) => this.mainLogic(event, input, text, this.testVariables, isMobile));
    }
  }

  mainLogic(event, input, text, vars, isMobile) {

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

   let lastIsSpaceOnMob = false;
   let lastIsSpace = true;
    if (isMobile === false) {
        lastIsSpace = event.key === " ";
        // restart array if Control+Backspace
        if (event.key == "Backspace" && vars["word"][wordLength-1] == "Control") {
          vars["word"] = [];
        }
        // pop letter from word when backspacing
        else if (event.key == "Backspace" && wordLength > 0) {
          vars["word"].pop();
        }
    } else { lastIsSpaceOnMob = vars["word"].charAt(wordLength-1) === " "; }

   // add letter to word array and compare it to output word
   if((lastIsSpace && wordLength > 0) || (lastIsSpaceOnMob && /[a-zA-Z]/.test(vars["word"]))) {

       let finalWord = "";
       if(isMobile === false) {
          // get written word and rows of text
           finalWord = vars["word"].join("");
       } else {
            finalWord = vars["word"];
       }

      // check if it's last word in whole text
      if(vars["currentWordIndexInText"] === this.indexOfLastWordInText) {
            this.endTest(vars["wordsTyped"], vars["timer"].getPassedTime());
       }

      let actualRow = this.getActualRow();
      let originalRow = this.wordsToCompare.slice(vars["lastWordIndex"] - ((this.wordsPerRow * 2) - 1), vars["lastWordIndex"] - (this.wordsPerRow - 1));
      let actualRowArr = actualRow.innerHTML.split(" ");
      let nextRow  =  document.querySelector("#next-row");

      let wordsMatch = this.wordsMatch(finalWord, originalRow[vars["currentWordIndex"]]);

      // update indexes
      vars["currentWordIndex"] += 1;
      vars["currentWordIndexInText"] += 1 ;

      // index on text without styles
      let inTextIndex = actualRowArr.indexOf(originalRow[vars["currentWordIndex"]]);

      let isLastWordInRow = originalRow[vars["currentWordIndex"]] == originalRow[originalRow.length];
      
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
      
      if(isLastWordInRow) {

        // update both rows and index in original text
        actualRow.innerHTML = nextRow.innerHTML;
        vars["lastWordIndex"] += this.wordsPerRow;
        actualRowArr = actualRow.innerHTML.split(" ");
        nextRow.innerHTML = this.outputManager.getRowOfWords(this.wordsToCompare, vars["lastWordIndex"]-(this.wordsPerRow-1), vars["lastWordIndex"]+1);
        
        vars["currentWordIndex"] = 0;
        styleManager.changeFocus(actualRow, actualRowArr, 0);

      } else if(!isLastWordInRow){
        
        // put focus on actual word
        styleManager.changeFocus(actualRow, actualRowArr, inTextIndex);
      }

      // send actual amount of words typed
      vars["timer"].setWordsTyped(vars["wordsTyped"]);
      vars["word"] = [];
      input.value = "";
   }
    
    // special characters ain't letters but their key contains letters
   else if(this.itsLetter(event.key) && !this.itsSpecialCharacter(event.key) && isMobile === false) {
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
    let regex = /\\n/;
    let text = outputManager.getText().replace(regex, " ");
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
    return wpm;
  }

  stopListening() {
    // get input element and RESET IT TO STOCK, no EVENT LISTENER.
    let input = document.querySelector("#writing-div input");
    let inputClone = input.cloneNode(true);
    input.parentNode.replaceChild(inputClone, input);
  }

  endTest(wordsTyped, passedTime) {
    this.stopListening();
    this.testVariables["timer"].stop();
    let score = this.getWPM(wordsTyped, passedTime);
    let input = document.querySelector("#writing-div input");
    this.outputManager.outputTestResult(input, score);
  }

  reset(updateText) {

    this.stopListening();
    this.restarted = true;

    // reset test variables
    this.testVariables["timer"].restart();
    this.testVariables["timer"].outputTimer(this.testVariables["timer"].getTime());
    this.testVariables["keyPressCount"] = 0;
    this.testVariables["currentWordIndex"] = 0;
    this.testVariables["currentWordIndexInText"] = 0;
    this.testVariables["wordsTyped"] = 0;
    this.testVariables["lastWordIndex"] = (this.wordsPerRow*2)-1;
    this.testVariables["word"] = [];

    let actRow = this.getActualRow();
    let nextRow = this.getNextRow();

    // Check whether it should cycle the text arr.
    if (updateText) {
        this.outputManager.updateActualArr();
    }

    // set new words to compare to
    this.wordsToCompare = this.getOutputWords(this.outputManager);
    this.indexOfLastWordInText = this.wordsToCompare.length-1;

    // output this new text
    let text = this.outputManager.getText().split(" ");
    actRow.innerHTML = this.outputManager.getRowOfWords(text, 0, this.wordsPerRow);
    nextRow.innerHTML = this.outputManager.getRowOfWords(text, this.wordsPerRow, this.wordsPerRow*2);
  }

  userIsOnMobile() {
    return navigator.userAgent.includes("Mobile");
  }

}