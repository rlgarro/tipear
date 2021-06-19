class TestTimer {

  // default time
  constructor(time) {
    this.time = time-1;
    this.initialTime = this.time+1;
    this.wordsTyped = 0;
    this.stopInterval;
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
        this.time = this.initialTime;
        this.outputTimer(this.time);

        // show WPM info
        new OutputManager(text).outputTestResult(writingInput);
      }
    }, 1000);
  }

  restart() {
    this.stopInterval = true;
    this.time = this.initialTime;
  }
}
