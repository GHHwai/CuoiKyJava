let allUsers = [];
const token = localStorage.getItem("token");
let stompClient = null;
let currentConversationId = null;
const userId = localStorage.getItem("userId");

// ================= CONNECT WS =================
function connect() {

    const socket = new SockJS("http://localhost:8080/ws");
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function () {
        console.log("WebSocket connected");
    });
}

connect();

// ================= LOAD CONVERSATIONS =================
function loadConversations() {

    fetch("/api/conversations")
        .then(res => res.json())
        .then(data => {

            const list = document.getElementById("conversationList");
            list.innerHTML = "";

            data.forEach(c => {

                const div = document.createElement("div");
                div.className = "chat-item";
                div.innerText = c.name;

                div.onclick = () => openConversation(c.id, c.name);

                list.appendChild(div);
            });
        });
}

loadConversations();

// ================= OPEN CHAT =================
function openConversation(id, name) {

    currentConversationId = id;

    document.getElementById("chatName").innerText = name;

    document.getElementById("messages").innerHTML = "";

    loadMessages(id);

    subscribeConversation(id);
}

// ================= LOAD MESSAGES =================
function loadMessages(conversationId) {

    fetch("/api/messages/" + conversationId)
        .then(res => res.json())
        .then(data => {

            data.forEach(showMessage);
        });
}

// ================= SUBSCRIBE WS =================
function subscribeConversation(id) {

    stompClient.subscribe("/topic/conversation/" + id, function (msg) {
        showMessage(JSON.parse(msg.body));
    });
}

// ================= SEND MESSAGE =================
function sendMessage() {

    const content = document.getElementById("msg").value;

    if (!content || !currentConversationId) return;

    stompClient.send("/app/chat.send", {}, JSON.stringify({
        conversationId: currentConversationId,
        content: content
    }));

    document.getElementById("msg").value = "";
}

// ================= RENDER MESSAGE =================
function showMessage(msg) {

    const box = document.getElementById("messages");

    const isMe = String(msg.senderId) === String(userId);

    const div = document.createElement("div");

    div.className = "msg " + (isMe ? "me" : "you");
    div.innerText = msg.content;

    box.appendChild(div);

    box.scrollTop = box.scrollHeight;
}
// ================= OPEN ADD FRIEND MODAL =================
async function openAddFriendModal() {

    const modal = new bootstrap.Modal(
        document.getElementById("addFriendModal")
    );

    modal.show();

    await loadUsers();
}
async function loadUsers() {

    try {

        const res = await fetch("/api/users", {
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        console.log("STATUS:", res.status);

        if (!res.ok) {
            const text = await res.text();
            console.error("API ERROR:", text);
            return;
        }

        const data = await res.json();

        console.log("USERS:", data);

        allUsers = data;
        renderUsers(allUsers);

    } catch (err) {
        console.error("FETCH ERROR:", err);
    }
}
function renderUsers(users) {

    const list = document.getElementById("userList");
    list.innerHTML = "";

    users.forEach(u => {

        const div = document.createElement("div");

        div.className = "d-flex justify-content-between border p-2 mb-2";

        div.innerHTML = `
            <div>
                <b>${u.username}</b><br>
                <small>${u.email}</small>
            </div>

            <button class="btn btn-sm btn-primary">
                Kết bạn
            </button>
        `;

        div.querySelector("button").onclick =
            () => sendFriendRequest(u.id);

        list.appendChild(div);
    });
}
function filterUsers() {

    const keyword = document.getElementById("searchUser").value.toLowerCase();

    const filtered = allUsers.filter(u =>
        u.username.toLowerCase().includes(keyword) ||
        u.email.toLowerCase().includes(keyword)
    );

    renderUsers(filtered);
}
async function sendFriendRequest(receiverId) {

    if (!receiverId) {
        console.error("receiverId bị null");
        return;
    }

    const res = await fetch("/api/friends/request", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
        },
        body: JSON.stringify({ receiverId })
    });

    if (!res.ok) {
        const text = await res.text();
        console.error("ERROR:", text);
    }
}