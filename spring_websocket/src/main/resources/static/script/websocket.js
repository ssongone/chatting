const socket = new WebSocket("ws://localhost:8080/chat"); // 서버의 WebSocket 엔드포인트에 연결합니다.
iam = "익명의 누군가";

socket.onmessage = function(event) {
    //const message = event.data;
    const response = JSON.parse(event.data);
    const sender = response.name;
    const message = response.message;
    const time = response.time;
    tmp = ""
    if (sender != "익명의 누군가" && sender == iam) {
        tmp = "(나)"
    }
    console.log(name);
    console.log(message);
    console.log(time);

    const listItem = document.createElement("li");
    listItem.textContent = sender +": " + tmp + message;

    document.getElementById("messageList").appendChild(listItem);
};

document.getElementById("messageInput").addEventListener("keydown", function(event) {
    if (event.key === "Enter") {
        const name = document.getElementById("name").value;
        if (name!="") {
            iam = name;
        }
        const message = document.getElementById("messageInput").value;
        const time = new Date().toLocaleString();

        // JSON 객체 생성
        const data = {
            name: iam,
            message: message,
            time: time
        };

        // JSON 객체를 문자열로 변환
        const jsonData = JSON.stringify(data);

        // WebSocket을 통해 서버로 데이터 전송
        socket.send(jsonData);

        event.target.value = "";
    }
});
