class OutputManager {

  constructor (textArraysInfo, actualArrIndex, wordsPerRow, stompClient, hasArgs) {
    if(hasArgs) {
        this.textsInfo = textArraysInfo;
        this.actualArr = this.textsInfo[actualArrIndex];
        this.actualArrIndex = actualArrIndex;
        this.textId = this.actualArr["id"];
        this.wordsPerRow = wordsPerRow;
        this.sc = stompClient;
        this.roomId = null;
        this.messagesInfo = {
            sender: "",
            type: "",
            senderId: 0,
        }

        // actual text being used
        this.text = this.actualArr["content"];
        }
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

    this.getEditedText(this.text, this.wordsPerRow);

    timer.outputTimer(timer.initialTime);

  }

  getEditedText(text, wordsPerRow) {
    // output edited styled text and timer
    let editedText = text.split(" ");
    let firstWord = editedText[0];
    let secondRow = this.getRowOfWords(editedText, wordsPerRow, wordsPerRow*2);

    editedText[0] = "<span id='change-bg'>" + firstWord + "</span>";
    let firstRow = this.getRowOfWords(editedText, 0, wordsPerRow);

    let firstRowDiv = document.querySelector("#actual-row");
    let secondRowDiv = document.querySelector("#next-row");

    firstRowDiv.innerHTML = firstRow;
    secondRowDiv.innerHTML = secondRow;

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
    document.getElementById("test-div").style.height = "400px";

    if (this.sc === null) {
        document.getElementById("song-title").innerHTML = this.actualArr["title"] + " by " + this.actualArr["author"];
        document.querySelector("#quote-from h3").innerHTML += " " + this.actualArr["category"] + ":";
        document.getElementById("score-span").innerHTML = score.toString() + "WPM";

        let scoreInp = document.getElementById("score")
        if (scoreInp !== null) {
            // add value to the inputs that sends the test
            scoreInp.value = score.toString();
            document.getElementById("author").value = this.actualArr["author"];
            document.getElementById("title").value = this.actualArr["title"];
        }
    } else {
        // show client's race result win/loose/draw
        let clientScore = parseInt(document.querySelector("#clientPlayer span").innerHTML.split("W")[0]);
        let rivalScore = parseInt(document.querySelector("#rivalPlayer span").innerHTML.split("W")[0]);
        let resultH2 = document.querySelector("#result-metrics h2");
        if(clientScore > rivalScore) {
            resultH2.style.color = "#b8bb26";
            resultH2.innerHTML = "You won!";
        } else if (clientScore < rivalScore) {
            resultH2.style.color = "#fb4934";
            resultH2.innerHTML = "You lost!";
        } else {
            resultH2.style.color = "#b8bb26";
            resultH2.innerHTML = "It's a draw!";
        }

    }
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

   setMessagesInfo() {

        // get data for STOMP message
        let roomInfo = document.getElementById("room-info").innerHTML.split(",");
        this.messagesInfo["sender"] = document.getElementById("clientPlayer").childNodes[0].innerHTML;
        this.messagesInfo["senderId"] = roomInfo[0];
        this.roomId = roomInfo[1];

   }

    sendWPM(wpm) {
        // update client's wpm
        document.querySelector("#clientPlayer span").innerHTML = wpm+"WPM";


        let message = {sender: this.messagesInfo["sender"], senderId: this.messagesInfo["senderId"], content: wpm, type: "WPM"};
        this.sc.send("/race/topic/message/" +this.roomId, {}, JSON.stringify(message));
    }

    sendFinished() {
        let message = {sender: this.messagesInfo["sender"], senderId: this.messagesInfo["senderId"], content: "", type: "FINISHED"};
        this.sc.send("/race/topic/message/" +this.roomId, {}, JSON.stringify(message));
    }

}