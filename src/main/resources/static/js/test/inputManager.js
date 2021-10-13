class InputManager {

  constructor (testTimer, isMultiplayer) {
    this.outputManager  = testTimer.getOutputManager();
    this.wordsToCompare = this.getOutputWords(this.outputManager);
    this.indexOfLastWordInText = this.wordsToCompare.length-1;
    this.multiplayer = isMultiplayer;
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
      wpm: 0,
    }
  }

  startListening(timer) {
    let input = document.querySelector("#writing-input");
    let text  = document.querySelector("#text");

    this.testVariables["timer"] = timer;
    if (this.multiplayer === true) {
        this.outputManager.setMessagesInfo();
        this.testVariables["keyPressCount"] = 1;
        this.testVariables["timer"].startTimer(input, text);
    }

    let isMobile = this.userIsOnMobile();
    if (isMobile) {
        isMobile = true;
        input.addEventListener("input", (event) => this.mainLogic(event, input, text, this.testVariables, isMobile));
    } else {
        input.addEventListener("keydown", (event) => this.mainLogic(event, input, text, this.testVariables, isMobile));
    }
  }

    startOnFirstKeyPress(vars, input) {
        // start the timer only on first keypress
        if (vars["keyPressCount"] === 0) {
          vars["timer"].startTimer(input, text);
          vars["keyPressCount"] = 1;
          input.placeholder = "";
        }
    }

  deleteLetterOrWordByDevice(vars, isMobile) {
       let lastIsSpaceOnMob = false;
       let lastIsSpace = false;
       let lastIsCommaOrPoint = false;
       let wordLength = vars["word"].length;

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
        } else {
          vars["word"] = event.data;
          lastIsSpaceOnMob = vars["word"].includes(" ");
          lastIsCommaOrPoint = /[,.]/.test(vars["word"].charAt(wordLength-1));
        }
        return [lastIsSpaceOnMob, lastIsSpace, lastIsCommaOrPoint];
  }

  addLetterByDevice(finalWord, isMobile, vars) {
       if(isMobile === false) {
           // get written word and rows of text
           finalWord = vars["word"].join("");
       } else {
            finalWord = vars["word"].slice(0, wordLength);
       }
       return vars;
  }

  handleLastWordInMultiplayer(vars) {
    let rivalEndedRace = (document.getElementById("room-info").innerHTML.split(",")[3] === "true");

    // update clients racing status
    let roomInfoContentArr = document.getElementById("room-info").innerHTML.split(",");
    roomInfoContentArr[2] = "true";
    document.getElementById("room-info").innerHTML = roomInfoContentArr.join(",");

    if(rivalEndedRace) {
        document.querySelector("#clientPlayer span").style.color = "#b8bb26";
        this.outputManager.sendFinished();
        this.endTest(vars["wpm"], true);
    }
    else  {
        document.querySelector("#clientPlayer span").style.color = "#b8bb26";
        this.outputManager.sendFinished();
        this.endTest(vars["wpm"], false);
    }
  }

  mainLogic(event, input, text, vars, isMobile) {

    if(this.restarted) {
      vars = this.testVariables;
    }

    let wordLength = vars["word"].length;

   this.startOnFirstKeyPress(vars, input);

   let lastWordCasesArr = this.deleteLetterOrWordByDevice(vars, isMobile);
   let lastIsSpaceOnMob = lastWordCasesArr[0];
   let lastIsSpace = lastWordCasesArr[1];
   let lastIsCommaOrPoint = lastWordCasesArr[1];

   let hasLetters = /[a-zA-Z]/.test(vars["word"]);

   let finishedTypingWord = (lastIsSpace && wordLength > 0) || (lastIsSpaceOnMob && hasLetters) || (lastIsCommaOrPoint && hasLetters);
   if(finishedTypingWord) {

       let finalWord = !isMobile ? vars["word"].join("") : vars["word"].slice(0, wordLength);
       vars = this.addLetterByDevice(finalWord, isMobile, vars);

      // check if it's last word in whole text
      if(vars["currentWordIndexInText"] === this.indexOfLastWordInText) {
            if(this.multiplayer) {
                this.handleLastWordInMultiplayer(vars);
            }
            else {
                this.endTest(vars["wpm"], true);
            }
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

      } else if(wordsMatch === false) {
        
        // change color of previous word to red
        actualRowArr[inTextIndex-1] = styleManager.changeColor(actualWord, wordContent, false);

      }
      // update client's and timer's wpm
      let wpm = vars["timer"].getWpmInTime(vars["wordsTyped"], vars["timer"].getInitialTime() - vars["timer"].getTime());
      vars["wpm"] = wpm;
      vars["timer"].setWPM(wpm);

      // send wpm to socket if multiplayer
      if(this.multiplayer === true) {
        document.querySelector("#clientPlayer span").innerHTML = wpm+"WPM";
        this.outputManager.sendWPM(wpm);
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
      vars["timer"].setWPM(vars["wpm"]);
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

  stopListening() {
    // get input element and reset it to stock, no event listener.
    let input = document.querySelector("#writing-div input");
    let inputClone = input.cloneNode(true);
    input.parentNode.replaceChild(inputClone, input);
  }

  endTest(score, raceEnded) {
    this.stopListening();
    this.testVariables["timer"].stop();
    let input = document.querySelector("#writing-div input");

    if (!this.multiplayer || raceEnded) {
        this.outputManager.outputTestResult(input, score);
    }
  }

  reset(updateText) {
    this.stopListening();
    this.restarted = true;

    this.resetTestVariables();
    let actRow = this.getActualRow();
    let nextRow = this.getNextRow();

    // Check whether it should cycle the text arr.
    if (updateText) {
        this.outputManager.updateActualArr();
    }

    this.setNewComparisonText();
    this.outputNewText(actRow, nextRow);
  }

  userIsOnMobile() {
    return navigator.userAgent.includes("Mobile");
  }

    outputNewText(actRow, nextRow) {
        // output this new text
        let text = this.outputManager.getText().split(" ");
        actRow.innerHTML = this.outputManager.getRowOfWords(text, 0, this.wordsPerRow);
        nextRow.innerHTML = this.outputManager.getRowOfWords(text, this.wordsPerRow, this.wordsPerRow*2);
    }

    setNewComparisonText() {
        // set new words to compare to
        this.wordsToCompare = this.getOutputWords(this.outputManager);
        this.indexOfLastWordInText = this.wordsToCompare.length-1;
    }

    resetTestVariables() {
        // reset test variables
        this.testVariables["timer"].restart();
        this.testVariables["timer"].outputTimer(this.testVariables["timer"].getTime());
        this.testVariables["keyPressCount"] = 0;
        this.testVariables["currentWordIndex"] = 0;
        this.testVariables["currentWordIndexInText"] = 0;
        this.testVariables["wordsTyped"] = 0;
        this.testVariables["wpm"] = 0;
        this.testVariables["lastWordIndex"] = (this.wordsPerRow*2)-1;
        this.testVariables["word"] = [];
    }
}

