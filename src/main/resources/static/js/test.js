  class Test {

  constructor(time) {
    this.timer = new TestTimer(time);
    this.inputManager = new InputManager(timer);
    this.outputManager = new OutputManager();
    this.setText(this.timer);
  }

  setTime(time) {
    this.timer.setTime(time);
  }

  setText(timer) {
    this.outputManager.outputContent(timer);
  }

  start() {
    this.inputManager.startListening(this.timer);
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
