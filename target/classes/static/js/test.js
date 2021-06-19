  class Test {

  constructor(time, outputManager) {
    this.timer = new TestTimer(time);
    this.outputManager = outputManager;
    this.inputManager = new InputManager(timer, this.outputManager);
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

  restartStats() {
    // TODO: implement restart button
    this.inputManager.reset();
  }

  restart() {
    this.inputManager.reset();
    this.timer.restart();
    this.timer = new TestTimer(this.timer.initialTime);
    this.setText(this.timer);
    this.inputManager.startListening(this.timer);
  }

}