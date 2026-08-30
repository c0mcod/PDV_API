const API_URL = "http://localhost:8090/api/auth/login";

async function fazerLogin() {
    const nome = document.getElementById("nome").value;
    const password = document.getElementById("password").value;

    try {
        const response = await fetch(`${API_URL}`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ nome, password })
        });

        if (!response.ok) {
            alert("Usuário ou senha inválidos");
            return;
        }

        const token = await response.text();
        localStorage.setItem("token", token);

        // redireciona pra página desejada
        window.location.href = "/pages/inventory.html";

    } catch (erro) {
        alert("Erro ao conectar: " + erro.message);
    }
}

function limparToken() {
    localStorage.removeItem("token");
    mostrarTokenAtual();
}

function mostrarTokenAtual() {
    const token = localStorage.getItem("token");
    document.getElementById("tokenAtual").innerText = token
        ? "Token atual: " + token
        : "Nenhum token salvo.";
}

mostrarTokenAtual();