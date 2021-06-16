class TestCursor {

  constructor () {
    this.p = document.querySelector("#text p");
  }

  changeFocus(element, elementTextArr, index) {
    elementTextArr[index] = "<span id='change-bg'>" + elementTextArr[index] + "</span>"; 
    element.innerHTML = elementTextArr.join(" ");
  }
  
  changeColor(word, content, isCorrect) {

    if(isCorrect) {
      word = "<span id='correct'>" + content + "</span>"; 
      return word;
    }

    word = "<span id='incorrect'>" + content + "</span>"; 
    return word;

  }
}
