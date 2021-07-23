var stompClient = null;
var url = "";
var roomId;
var roomOcc = 1;
var clientPlayer = new Player(getRandomIntInRange(1, 3000));
var rivalPlayer = new Player();
var test = null;
var roomText = null;

window.addEventListener("load", () => {


    document.getElementById("room-info").innerHTML = clientPlayer.getId();
    roomId = getRoom();

    // get room's text
    let id = getTextId(9);
    getTextById(id);

    // info about: 1-clientId 2-roomId 3-clientEndedRace 4-rivalEndedRace
    document.getElementById("room-info").innerHTML += ","+roomId+",false,false";
    url = "/race/topic/" + roomId;

    // send to room via enter or via button press
    let usernameInp = document.querySelector("#getUserName input");
    let usernameBtn = document.querySelector("#getUserName button");
    usernameInp.addEventListener("keypress", (e) => {
        if(e.key.toLowerCase() === "enter")
            sendToRoom(usernameInp);
    });
    usernameBtn.addEventListener("click", () => {
        sendToRoom(usernameInp);
    });

});

function getTextId(amountOfTexts) {
    if (roomId > amountOfTexts) {
        return roomId - (amountOfTexts * (Math.floor(roomId / amountOfTexts)));
    }
    return roomId;
}

function getTextById(id) {
    let url = "http://localhost:8080/texts/"+id;
    fetch(url).then(r => {
        if(r.ok) {
            r.text().then(text => {
                roomText = JSON.parse(text).content;
            });
        }
    });
}

function sendToRoom(usernameInp) {

    clientPlayer.setName(usernameInp.value);
    showTest();
    connect();

    // send alert with url
    alert("room url: localhost:8080/race/" + roomId + "\n (maximum capacity per room: 2)");

    let readyBtn = document.querySelector("#clientPlayer button");

    // whenever client's ready change status and send to channel
    readyBtn.addEventListener("click", () => {
        if(!clientPlayer.playerIsReady()) {
            readyBtn.style.backgroundColor = "green";
            clientPlayer.setReady(true);
        } else {
            readyBtn.style.backgroundColor = "red";
            clientPlayer.setReady(false);
        }
        let message = {
            sender: clientPlayer.getName(),
            type: "READY",
            content: clientPlayer.playerIsReady(),
            senderId: clientPlayer.getId(),
        };
        stompClient.send("/race/topic/message/"+roomId, {}, JSON.stringify(message));
    });

    // show player status
    document.querySelector("#clientPlayer p").innerHTML = clientPlayer.getName();
}

function showTest() {
    document.getElementById("getUserName").style.display = "none";
    document.getElementById("test-div").style.display = "block";
    document.getElementById("setStatus").style.display = "flex";
    new OutputManager(null, null, null, null, null, false).getEditedText(roomText, 15);
}

function connect() {
    let socket = new SockJS("/race-websocket");
    stompClient  = Stomp.over(socket);

    // when connected get rival player's name and send yours
    stompClient.connect({}, onConnected);
}

function onConnected() {
    stompClient.subscribe(url, messageReceived);

    let message = {
        sender: clientPlayer.getName(),
        content: clientPlayer.getName() + " just connected..",
        senderId: clientPlayer.getId(),
        type: "CONNECT"
    }

    stompClient.send("/race/topic/"+roomId+"/new", {}, JSON.stringify(message));
}

// message handler
function messageReceived(payload) {
    let message = JSON.parse(payload.body);

    // act only if message's not from same client
    if (parseInt(message.senderId) !== clientPlayer.getId()) {
        let newMessage = {sender: clientPlayer.getName(), senderId: clientPlayer.getId(), content: "", type: ""};
        let rivPlayerEl = document.querySelector("#rivalPlayer");
        switch (message.type) {
            // send name and status, if room is full kick them
            case "CONNECT":
                roomOcc +=1;
                newMessage["content"] = clientPlayer.playerIsReady();
                if (roomOcc >= 3) {
                    //send disconnect order for senderId
                    newMessage["type"] = "DISCONNECT_ORDER";
                    newMessage["content"] = message.senderId;
                } else {
                    rivalPlayer.setId(message.senderId);
                    newMessage["type"] = "STATUS";
                    rivPlayerEl.style.display = "flex";
                    rivPlayerEl.childNodes[1].innerHTML = message.sender;
                }
                stompClient.send("/race/topic/message/"+roomId, {}, JSON.stringify(newMessage));
                break;

            case "WPM":
                document.querySelector("#rivalPlayer span").innerHTML = message.content + "WPM";
                break;

            case "DISCONNECT":
                // only if message was about rival
                if(parseInt(message.senderId) === parseInt(rivalPlayer.getId())) {
                    roomOcc = 1;
                    backToLobby();
                    rivPlayerEl.style.display = "none";
                    test.restartStats(false);
                }
                break;

            case "DISCONNECT_ORDER":
                if (parseInt(message.content) === clientId)
                    document.location.href = "/";
                    alert("The room was already full");
                    break;
                break;

            case "READY":
                let rivReady = rivalPlayer.playerIsReady();
                rivReady = (message.content === 'true');
                setRivalPlayerStatus(rivReady);

                // send signal to start test if both ready
                if (clientPlayer.playerIsReady() && rivReady) {
                    newMessage["type"] = "BOTH_READY";
                    stompClient.send("/race/topic/message/"+roomId, {}, JSON.stringify(newMessage));

                    // start test
                    startTest();
                }
                break;

            case "STATUS":
                //update rival player status
                rivalPlayer.setId(message.senderId);
                rivPlayerEl.style.display = "flex";
                rivPlayerEl.childNodes[1].innerHTML = message.sender;
                rivalPlayer.setName(message.sender);
                rivalPlayer.setReady(message.content === 'true');
                setRivalPlayerStatus(rivalPlayer.playerIsReady());
                break;

            case "BOTH_READY":
                startTest();
                break;

            case "FINISHED":
                let roomInfoContentArr = document.getElementById("room-info").innerHTML.split(",");
                let clientEndedRace = (roomInfoContentArr[2] === "true");

                // if both ended the race output result
                if(clientEndedRace) {
                    let inp = document.getElementById("writing-input");
                    let scoreStr = document.querySelector("#clientPlayer span").innerHTML;
                    let score = parseInt(scoreStr.slice(0, scoreStr.indexOf("W")));
                    new OutputManager(null, null, null, null, false).outputTestResult(inp, score);
                }

                // update rival race status to end
                document.querySelector("#rivalPlayer span").style.color = "#b8bb26";
                roomInfoContentArr[3] = "true";
                document.getElementById("room-info").innerHTML = roomInfoContentArr.join(",");
                break;
        }
    }
}

function setRivalPlayerStatus(playerIsReady) {
    let rivPlayerButton = document.querySelector("#rivalPlayer button");
    rivalPlayer.setReady(playerIsReady);

    if (rivalPlayer.playerIsReady()) {
        rivPlayerButton.style.backgroundColor = "green";
    } else {
        rivPlayerButton.style.backgroundColor = "red";
    }
}

function disconnect() {
    if(stompClient !== null)
        stompClient.disconnect
}

function getRoom() {
    // get room by url or creating one
    let roomId = document.getElementById("room-span").innerHTML;
    if (roomId === "")
        return getRandomIntInRange(0, 1000);
    return roomId;
}

function getRandomIntInRange(lower, upper) {
    return Math.floor(Math.random() * (upper - lower + 1)) + lower;
}


/* TEST FUNCTIONALITIES */
function startTest() {
    // hide button show score spans
    document.getElementById("ready-client").style.display = "none";
    document.getElementById("ready-rival").style.display = "none";
    let percentageSpans = document.querySelectorAll(".percentage-span");
    percentageSpans.forEach(el => el.style.display = "block");

    let time = 120;
    let wordsPerRow = 15;
    let texts = [
        {
            "id": 0,
            "content": roomText,
            "title": "somerandomtitle",
            "author": "somerandomband",
            "category": "somerandomcategory"
        }
    ];
    test = new Test(time, new OutputManager(texts, 0, wordsPerRow, stompClient, true), true);
    test.start();

    // 'back to lobby' button
    document.getElementById("retry-test").addEventListener("click", backToLobby);
}

// styles from test to lobby
function backToLobby() {

    // restart variables
    clientPlayer.setReady(false);
    rivalPlayer.setReady(false);

    // show status buttons, writing input, text
    document.getElementById("ready-client").style.display = "block";
    document.getElementById("ready-rival").style.display = "block";
    document.getElementById("writing-div").style.display = "flex";
    document.getElementById("text").style.display = "block";

    // hide scores, test-div, test result
    let percentageSpans = document.querySelectorAll(".percentage-span");
    percentageSpans.forEach(el => el.style.display = "none");
    document.getElementById("test-div").style.height = "50%";
    document.getElementById("test-result").style.display = "none";

    // buttons background-color
    document.querySelector("#ready-client").style.backgroundColor = "red";
    document.querySelector("#ready-rival").style.backgroundColor = "red";

    // scores colors
    document.querySelector("#clientPlayer span").style.color = "#fb4934";
    document.querySelector("#rivalPlayer span").style.color = "#fb4934";

    // restart info on html
    document.getElementById("room-info").innerHTML = clientPlayer.getId();
    document.getElementById("room-info").innerHTML += ","+roomId+",false,false";
    document.querySelector("#clientPlayer span").innerHTML = "0WPM";
    document.querySelector("#rivalPlayer span").innerHTML = "0WPM";

    new OutputManager(null, null, null, null, null, false).getEditedText(roomText, 15);

}
