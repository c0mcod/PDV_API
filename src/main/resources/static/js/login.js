const API_URL = "http://localhost:8090/api/auth/login";

document.getElementById("formLogin").addEventListener("submit", fazerLogin);

async function fazerLogin(e) {
    e.preventDefault();

    const nome = document.getElementById("nome").value;
    const password = document.getElementById("password").value;
    const erroDiv = document.getElementById("loginErro");

    erroDiv.classList.remove("visivel");
    erroDiv.textContent = "";

    try {
        const response = await fetch(API_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ nome, password })
        });

        if (!response.ok) {
            erroDiv.textContent = "Usuário ou senha inválidos.";
            erroDiv.classList.add("visivel");
            return;
        }

        const token = await response.text();
        localStorage.setItem("token", token);

        window.location.href = "../pages/awaiting.html";

    } catch (erro) {
        erroDiv.textContent = "Erro ao conectar com o servidor.";
        erroDiv.classList.add("visivel");
    }
}