let stompClient = null;

function connect() {

    const token = localStorage.getItem("token");

    const socket = new SockJS("http://localhost:8080/ws");

    stompClient = Stomp.over(socket);

    stompClient.connect(
        {
            Authorization: "Bearer " + token
        },
        function () {

            console.log("WebSocket connected");

        }
    );
}

connect();