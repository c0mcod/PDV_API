// ===================================
// ESTADO
// ===================================

let todosOsOperadores = [];

// ===================================
// INICIALIZAÇÃO
// ===================================

document.addEventListener("DOMContentLoaded", async () => {
    await carregarOperadores();
    configurarEventos();
});

// ===================================
// TRATAMENTO DE SESSÃO EXPIRADA
// ===================================

function tratarErro(e, mensagemPadrao) {
    if (e.message === "SESSAO_EXPIRADA") {
        localStorage.removeItem("token");
        window.location.href = "../pages/login.html";
        return;
    }
    showNotificationError(mensagemPadrao);
    console.error(e);
}

// ===================================
// CARREGAR OPERADORES
// ===================================

async function carregarOperadores() {
    try {
        todosOsOperadores = await apiGetUsuarios();
        renderizarTabela(todosOsOperadores);
        atualizarStats(todosOsOperadores);
    } catch (e) {
        tratarErro(e, "Erro ao carregar operadores.");
    }
}

// ===================================
// RENDERIZAR TABELA
// ===================================

function renderizarTabela(operadores) {
    const tbody = document.getElementById("tabelaOperadores");

    if (!operadores || operadores.length === 0) {
        tbody.innerHTML = `<tr><td colspan="4" style="text-align:center; padding: 40px; color: #868e96;">Nenhum operador encontrado.</td></tr>`;
        return;
    }

    tbody.innerHTML = operadores.map(op => `
        <tr>
            <td><strong>${op.nome}</strong></td>
            <td>${op.username}</td>
            <td>
                <span class="status-badge ${op.ativo ? 'status-ativo' : 'status-inativo'}">
                    ${op.ativo ? 'Ativo' : 'Inativo'}
                </span>
            </td>
            <td>
                <div class="action-buttons">
                    <button class="btn-action btn-editar" onclick="abrirModalEditar(${op.usuarioId})">Editar</button>
                    ${op.ativo
        ? `<button class="btn-action btn-desativar" onclick="desativarOperador(${op.usuarioId})">Desativar</button>`
        : `<button class="btn-action btn-ativar" onclick="ativarOperador(${op.usuarioId})">Ativar</button>`
    }
                </div>
            </td>
        </tr>
    `).join("");
}

// ===================================
// STATS
// ===================================

function atualizarStats(operadores) {
    const ativos = operadores.filter(op => op.ativo).length;
    const inativos = operadores.filter(op => !op.ativo).length;
    document.getElementById("statTotal").textContent = operadores.length;
    document.getElementById("statAtivos").textContent = ativos;
    document.getElementById("statInativos").textContent = inativos;
}

// ===================================
// BUSCA
// ===================================

function filtrar() {
    const termo = document.getElementById("searchBox").value.toLowerCase();
    const status = document.getElementById("filtroStatus").value;

    const filtrados = todosOsOperadores.filter(op => {
        const bateTexto = op.nome.toLowerCase().includes(termo) || op.username.toLowerCase().includes(termo);
        const bateStatus = status === "todos" || (status === "ativos" && op.ativo) || (status === "inativos" && !op.ativo);
        return bateTexto && bateStatus;
    });

    renderizarTabela(filtrados);
}

function configurarEventos() {
    document.getElementById("searchBox").addEventListener("input", filtrar);
    document.getElementById("filtroStatus").addEventListener("change", filtrar);

    document.getElementById("btnNovoOperador").addEventListener("click", abrirModalCriar);
    document.getElementById("btnFecharModal").addEventListener("click", fecharModal);
    document.getElementById("btnCancelarModal").addEventListener("click", fecharModal);
    document.getElementById("formOperador").addEventListener("submit", salvarOperador);

    document.getElementById("toggleMenu").addEventListener("click", () => {
        document.getElementById("navMenu").classList.toggle("collapsed");
    });
}

// ===================================
// MODAL
// ===================================

function abrirModalCriar() {
    document.getElementById("modalOperadorTitulo").textContent = "Novo Operador";
    document.getElementById("operadorId").value = "";
    document.getElementById("operadorNome").value = "";
    document.getElementById("operadorUsername").value = "";
    document.getElementById("modalOperador").style.display = "flex";
    document.getElementById("operadorNome").focus();
}

function abrirModalEditar(id) {
    const op = todosOsOperadores.find(o => o.usuarioId === id);
    if (!op) return;

    document.getElementById("modalOperadorTitulo").textContent = "Editar Operador";
    document.getElementById("operadorId").value = op.usuarioId;
    document.getElementById("operadorNome").value = op.nome;
    document.getElementById("operadorUsername").value = op.username;
    document.getElementById("modalOperador").style.display = "flex";
    document.getElementById("operadorNome").focus();
}

function fecharModal() {
    document.getElementById("modalOperador").style.display = "none";
}

// ===================================
// SALVAR (CRIAR OU EDITAR)
// ===================================

async function salvarOperador(e) {
    e.preventDefault();

    const id = document.getElementById("operadorId").value;
    const payload = {
        nome: document.getElementById("operadorNome").value,
        username: document.getElementById("operadorUsername").value
    };

    try {
        if (id) {
            const response = await apiFetch(`/usuarios/${id}`, {
                method: "PUT",
                body: JSON.stringify(payload)
            });
            if (!response.ok) throw new Error("Erro ao atualizar operador");
            showNotificationSuccess("Operador atualizado com sucesso!");
        } else {
            const response = await apiFetch(`/usuarios`, {
                method: "POST",
                body: JSON.stringify(payload)
            });
            if (!response.ok) throw new Error("Erro ao criar operador");
            showNotificationSuccess("Operador criado com sucesso!");
        }

        fecharModal();
        await carregarOperadores();
    } catch (err) {
        tratarErro(err, err.message);
    }
}

// ===================================
// ATIVAR / DESATIVAR
// ===================================

async function ativarOperador(id) {
    try {
        const response = await apiFetch(`/usuarios/${id}/ativar`, { method: "PATCH", comJson: false });
        if (!response.ok) throw new Error("Erro ao ativar operador");
        showNotificationSuccess("Operador ativado com sucesso!");
        await carregarOperadores();
    } catch (e) {
        tratarErro(e, e.message);
    }
}

async function desativarOperador(id) {
    try {
        const response = await apiFetch(`/usuarios/${id}/desativar`, { method: "PATCH", comJson: false });
        if (!response.ok) throw new Error("Erro ao desativar operador");
        showNotificationSuccess("Operador desativado com sucesso!");
        await carregarOperadores();
    } catch (e) {
        tratarErro(e, e.message);
    }
}