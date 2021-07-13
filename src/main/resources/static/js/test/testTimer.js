class TestTimer {

  // default time
  constructor(time, outputManager) {
    this.time = time-1;
    this.initialTime = this.time+1;
    this.wpm = 0;
    this.stopInterval = false;
    this.outputManager = outputManager;
  }

  setWPM(words) {
    this.wpm = words;
  }

  setTime(seconds) {
    this.time = seconds-1;
    this.initialTime = seconds;
  }

  getWpmInTime(words, time) {
    let wpm = Math.round(words * 60 / time);
    return wpm;
  }

  getInitialTime() {
    return this.initialTime;
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

  startSimpleTimer() {
    setInterval(() => {
        this.time--;
    }, 1000);
  }

  startTimer(writingInput, text) {

    let outputInterval = setInterval(() => {
      
      this.outputTimer(this.time);
      this.time--; 

      // when time is over
      if(this.time < 0 || this.stopInterval) {
        
        // stop interval and restart time to default value
        clearInterval(outputInterval);

        let endedByTime = this.time < 0 && this.stopInterval === false;
        // stopped by time
        if (endedByTime) {

            // restart eventListener by cloning input
            let input = document.querySelector("#writing-div input");
            let inputClone = input.cloneNode(true);
            input.parentNode.replaceChild(inputClone, input);

            this.outputManager.outputTestResult(input, this.wpm);
        }
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
    return timePassed;
  }

  stop() {
    this.stopInterval = true;
  }

  getOutputManager() {
    return this.outputManager;
  }
}
