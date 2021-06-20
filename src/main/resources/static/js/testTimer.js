class TestTimer {

  // default time
  constructor(time, outputManager) {
    this.time = time-1;
    this.initialTime = this.time+1;
    this.wordsTyped = 0;
    this.stopInterval;
    this.outputManager = outputManager;
  }

  setWordsTyped(words) {
    this.wordsTyped = words;
  }

  setTime(seconds) {
    this.time = seconds-1;
    this.initialTime = seconds;
  }

  getWPM() {
    let wpm = Math.floor((this.wordsTyped * this.initialTime) / 60);
    return wpm;
  }
 
  getTime() {
    return this.time;
  }

  outputTimer(time) {

    // get units
    let minutes = Math.floor(time / 60);
    let seconds = time % 60;
    let htmlTimer = document.querySelector("#timer span");

    // output time correctly
    if (seconds < 10) {
      htmlTimer.innerHTML = `${minutes}:0${seconds}`;

    } else {
      htmlTimer.innerHTML = `${minutes}:${seconds}`;

    }
  }

  startTimer(writingInput, text) {
    
    // rest 1 to the timer every second
    let outputInterval = setInterval(() => {
      
      /* check if it should stop
      if(this.stopInterval) {
        clearInterval(outputInterval);
      }
      */

      this.outputTimer(this.time);
      this.time--; 

      // when time is over
      if(this.time < 0 || this.stopInterval) {
        
        // stop interval and restart time to default value
        clearInterval(outputInterval);

        console.log(this.time);
        console.log(this.stopInterval);
        console.log(this.time === 0 && this.stopInterval === false);
        // stopped by time
        if (this.time === 0 && this.stopInterval === false) {
            let score = this.getWPM();
            new InputManager().stopListening();
            this.outputManager.outputTestResult(input, score);
        }
        // show WPM info
        //new OutputManager(text).outputTestResult(writingInput);
      }
    }, 1000);
  }

  restart() {
    this.stop();
    this.time = this.initialTime;
  }

  getPassedTime() {
    // TODO: remove variable when sure it works
    let timePassed = this.initialTime - this.time+1;
    console.log(timePassed);
    return timePassed;
  }

  stop() {
    this.stopInterval = true;
  }

  getOutputManager() {
    return this.outputManager;
  }

}
