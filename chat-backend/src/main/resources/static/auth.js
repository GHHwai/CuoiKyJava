async function login() {

    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value.trim();

    const errorBox = document.getElementById("loginError");
    errorBox.innerText = "";

    // ❌ validate trước
    if (!email || !password) {
        errorBox.innerText = "Vui lòng nhập đầy đủ thông tin";
        return;
    }

    try {
        const res = await fetch("/api/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ email, password })
        });

        const data = await res.json().catch(() => null);

        if (!res.ok) {
            errorBox.innerText = "Đăng nhập thất bại";
            return;
        }

        if (!data) {
            errorBox.innerText = "Đăng nhập thất bại";
            return;
        }

        localStorage.setItem("token", data.token);
        localStorage.setItem("userId", data.userId);

        window.location.href = "/chat.html";

    } catch (err) {
        errorBox.innerText = "Không thể kết nối server";
    }
}
async function register() {

    const username = document.getElementById("username").value.trim();
    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value.trim();

    const errorBox = document.getElementById("registerError");
    errorBox.innerText = "";

    if (!username || !email || !password) {
        errorBox.innerText = "Vui lòng nhập đầy đủ thông tin";
        return;
    }

    try {
        const res = await fetch("/api/auth/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ username, email, password })
        });

        if (!res.ok) {
            errorBox.innerText = "Đăng ký thất bại";
            return;
        }

        window.location.href = "/login.html";

    } catch (err) {
        errorBox.innerText = "Không thể kết nối server";
    }
}
// chuyển sang register
function goRegister() {
    window.location.href = "/register.html";
}

// toggle password show/hide
function togglePassword(id, btn) {

    const input = document.getElementById(id);
    const icon = btn.querySelector(".eye-icon");

    if (input.type === "password") {
        input.type = "text";
        icon.innerHTML = "🙈";   // đang mở
    } else {
        input.type = "password";
        icon.innerHTML = "👁️";   // đang đóng
    }
}