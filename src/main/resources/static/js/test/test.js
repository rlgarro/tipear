  class Test {

  constructor(time, outputManager, isMultiplayer) {
    this.timer = new TestTimer(time, outputManager);
    this.outputManager = outputManager;
    this.inputManager = new InputManager(this.timer, isMultiplayer);
    this.setText(this.timer);
  }

  setTime(time) {
    this.timer.setTime(time);
  }

  setText(timer) {
    this.outputManager.outputContent(timer);
  }

  start() {
    this.inputManager.startListening(this.timer, this.outputManager);
  }

  restartStats(updateText) {
    this.inputManager.reset(updateText);
    this.timer.restart();
    this.timer = new TestTimer(this.timer.initialTime, this.outputManager);
    this.setText(this.timer);
  }

  restart(updateText) {
    this.inputManager.reset(updateText);
    this.timer.restart();
    this.timer = new TestTimer(this.timer.initialTime, this.outputManager);
    this.setText(this.timer);
    this.inputManager.startListening(this.timer);
  }

}