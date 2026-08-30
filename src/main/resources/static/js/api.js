const API_BASE_URL = "http://localhost:8090";

/* =======================
   AUTENTICAÇÃO
======================= */

// Monta os headers padrão, incluindo o token (se existir)
function getAuthHeaders(comJson = true) {
    const token = localStorage.getItem("token");
    const headers = {};

    if (comJson) {
        headers["Content-Type"] = "application/json";
    }

    if (token) {
        headers["Authorization"] = "Bearer " + token;
    }

    return headers;
}

// Wrapper central: toda chamada à API passa por aqui.
// Se a resposta for 401/403, lança "SESSAO_EXPIRADA" pra quem chamou tratar.
async function apiFetch(endpoint, options = {}) {
    const comJson = options.comJson !== false;
    const config = {
        ...options,
        headers: {
            ...getAuthHeaders(comJson),
            ...(options.headers || {})
        }
    };
    delete config.comJson;

    const response = await fetch(`${API_BASE_URL}${endpoint}`, config);

    if (response.status === 401 || response.status === 403) {
        throw new Error("SESSAO_EXPIRADA");
    }

    return response;
}

/* =======================
   PRODUTOS
======================= */

async function apiGetProducts(page = 0, size = 10, ativo = true) {
    const response = await apiFetch(`/produto/lista?page=${page}&size=${size}&ativo=${ativo}`, { comJson: false });
    if (!response.ok) {
        throw new Error("Erro ao buscar produtos");
    }
    return response.json();
}

async function apiGetAllProducts(ativo = true) {
    const response = await apiFetch(`/produto/lista-todos?ativo=${ativo}`, { comJson: false });
    if (!response.ok) {
        throw new Error("Erro ao buscar todos os produtos");
    }
    return response.json();
}

async function apiGetStatsProducts() {
    const response = await apiFetch(`/produto/stats`, { comJson: false });
    if (!response.ok) {
        throw new Error("Erro ao buscar Stats");
    }
    return response.json();
}

async function apiAtualizarProduct(id, produto) {
    const response = await apiFetch(`/produto/atualiza/${id}`, {
        method: 'PUT',
        body: JSON.stringify(produto)
    });
    if (!response.ok) throw new Error("Erro ao atualizar produto");
    return response.json();
}

async function apiRegistrarEntrada(produtoId, quantidade) {
    const response = await apiFetch(`/produto/${produtoId}/adicionar-estoque`, {
        method: "POST",
        body: JSON.stringify({ quantidade })
    });

    if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || "Erro ao registrar entrada");
    }

    return response.json();
}

async function apiDeleteProducts(id) {
    const response = await apiFetch(`/produto/${id}`, {
        method: "DELETE",
        comJson: false
    });

    if (!response.ok) {
        throw new Error("Erro ao deletar produto");
    }
    return;
}


async function apiCreateProduct(product) {
    const response = await apiFetch(`/produto`, {
        method: "POST",
        body: JSON.stringify(product)
    });

    if (!response.ok) {
        const error = await response.json();
        throw error;
    }

    return response.json();
}

async function apiExportarProdutos() {
    try {
        const response = await apiFetch(`/produto/exportar/excel`, { comJson: false });

        if (!response.ok) {
            throw new Error('Erro ao exportar');
        }

        const blob = await response.blob();

        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'produtos.xlsx';
        a.click();
        window.URL.revokeObjectURL(url);

    } catch (error) {
        if (error.message === "SESSAO_EXPIRADA") throw error;
        console.error('Erro:', error);
        alert('Erro ao exportar produtos');
    }
}

async function apiAtivarProduto(id) {
    const response = await apiFetch(`/produto/${id}/ativar-produto`, {
        method: "POST",
        comJson: false
    });
    if (!response.ok) throw new Error("Erro ao ativar produto");
}

/* =======================
   USUÁRIOS
======================= */

async function apiGetUsuarios() {
    const response = await apiFetch(`/usuarios`, { comJson: false });
    if (!response.ok) {
        throw new Error("Erro ao buscar usuários");
    }
    return response.json();
}

/* =======================
   VENDAS
======================= */

async function apiAbrirVenda(usuarioId) {
    const response = await apiFetch(`/venda/abrir?usuarioId=${usuarioId}`, {
        method: "POST",
        comJson: false
    });

    if (!response.ok) {
        throw new Error("Erro ao abrir venda");
    }

    return response.json();
}

async function apiGetVenda(vendaId) {
    const response = await apiFetch(`/venda/${vendaId}`, { comJson: false });
    if (!response.ok) throw new Error("Erro ao carregar venda");
    return response.json();
}

async function apiAdicionarItemVenda(vendaId, payload) {
    const response = await apiFetch(`/venda/${vendaId}/itens`, {
        method: "POST",
        body: JSON.stringify(payload)
    });

    if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || "Erro ao adicionar item");
    }

    return response.json();
}

async function apiRemoverItemVenda(vendaId, itemId) {
    const response = await apiFetch(`/venda/${vendaId}/remover-item`, {
        method: "POST",
        body: JSON.stringify({ itemId })
    });

    if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || "Erro ao remover item");
    }

    return response.json();
}

async function apiFinalizarVenda(vendaId, dadosPagamento) {
    const response = await apiFetch(`/venda/${vendaId}/finalizar`, {
        method: "POST",
        body: JSON.stringify(dadosPagamento)
    });

    if (!response.ok) {
        const error = await response.json();
        throw new Error(error.mensagem || "Erro ao finalizar venda");
    }

    return response.json();
}

async function apiCancelarVenda(vendaId) {
    const response = await apiFetch(`/venda/${vendaId}/cancelar-venda`, {
        method: "POST",
        comJson: false
    });

    if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || "Erro ao cancelar venda");
    }

    return response.json();
}

async function apiGetDetalhesVenda(vendaId) {
    const response = await apiFetch(`/historico-vendas/detalhes/${vendaId}`, { comJson: false });
    if (!response.ok) {
        throw new Error("Erro ao buscar detalhes da venda");
    }
    return response.json();
}

async function apiGetHistoricoVendas(dataInicio, dataFim, operadorId, page, size) {
    let url = `/historico-vendas?dataInicio=${dataInicio}&dataFim=${dataFim}&page=${page}&size=${size}`;
    if (operadorId) url += `&operadorId=${operadorId}`;

    const response = await apiFetch(url, { comJson: false });
    if (!response.ok) {
        throw new Error("Erro ao buscar histórico de vendas");
    }
    return response.json();
}

async function apiGetHistoricoStats(dataInicio, dataFim, operadorId) {
    const params = new URLSearchParams({ dataInicio, dataFim });
    if (operadorId) params.append("operadorId", operadorId);
    const res = await apiFetch(`/historico-vendas/stats?${params}`, { comJson: false });
    if (!res.ok) throw new Error("Erro ao buscar stats");
    return res.json();
}

async function apiExportarHistoricoVendas(operadorId, dataInicio, dataFim) {
    try {
        let url = `/historico-vendas/exportar/excel?dataInicio=${dataInicio}&dataFim=${dataFim}`;
        if (operadorId) url += `&operadorId=${operadorId}`;

        const response = await apiFetch(url, { comJson: false });

        if (!response.ok) {
            throw new Error('Erro ao exportar');
        }

        const blob = await response.blob();
        const urlBlob = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = urlBlob;
        a.download = 'historico.xlsx';
        a.click();
        window.URL.revokeObjectURL(urlBlob);

    } catch (error) {
        if (error.message === "SESSAO_EXPIRADA") throw error;
        console.error('Erro:', error);
        showNotificationError('Erro ao exportar histórico de vendas');
    }
}
/* =======================
   RELATÓRIOS
======================= */

async function apiGetProdutosVendidosKpi(periodo) {
    const response = await apiFetch(`/api/relatorios/kpis?periodo=${periodo}`, { comJson: false });
    if(!response.ok) {
        throw new Error("Erro ao buscar KPI de produtos vendidos");
    }
    return response.json();
}

async function apiGetProdutosVendidosKpiPorData(dataInicio, dataFim) {
    const response = await apiFetch(`/api/relatorios/kpis?dataInicio=${dataInicio}&dataFim=${dataFim}`, { comJson: false });
    if(!response.ok) throw new Error("Erro ao buscar KPI de produtos vendidos");
    return response.json();
}

async function apiGetVendasDiasemanaPorData(dataInicio, dataFim) {
    const response = await apiFetch(`/api/relatorios/vendas-dia-semana?dataInicio=${dataInicio}&dataFim=${dataFim}`, { comJson: false });
    if(!response.ok) throw new Error("Erro ao buscar vendas por dia");
    return response.json();
}

async function apiGetTopProdutosPorData(dataInicio, dataFim, limite = 5) {
    const response = await apiFetch(`/api/relatorios/top-produtos?dataInicio=${dataInicio}&dataFim=${dataFim}&limite=${limite}`, { comJson: false });
    if(!response.ok) throw new Error("Erro ao buscar top produtos");
    return response.json();
}

async function apiGetVendasCategoriaPorData(dataInicio, dataFim) {
    const response = await apiFetch(`/api/relatorios/vendas-categoria?dataInicio=${dataInicio}&dataFim=${dataFim}`, { comJson: false });
    if(!response.ok) throw new Error("Erro ao buscar vendas por categoria");
    return response.json();
}

async function apiGetIndicadoresFinanceirosPorData(dataInicio, dataFim) {
    const response = await apiFetch(`/api/relatorios/indicadores-financeiros?dataInicio=${dataInicio}&dataFim=${dataFim}`, { comJson: false });
    if(!response.ok) throw new Error("Erro ao buscar indicadores financeiros");
    return response.json();
}

async function apiGetVendasDiaSemana(periodo) {
    const response = await apiFetch(`/api/relatorios/vendas-dia-semana?periodo=${periodo}`, { comJson: false });
    if(!response.ok) {
        throw new Error("Erro ao buscar vendas por dia");
    }
    return response.json();
}

async function apiGetTopProdutos(periodo, limite = 5) {
    const response = await apiFetch(`/api/relatorios/top-produtos?periodo=${periodo}&limite=${limite}`, { comJson: false });
    if(!response.ok) {
        throw new Error("Erro ao buscar top produtos");
    }
    return response.json();
}

async function apiGetVendasCategoria(periodo) {
    const response = await apiFetch(`/api/relatorios/vendas-categoria?periodo=${periodo}`, { comJson: false });
    if(!response.ok) {
        throw new Error("Erro ao buscar vendas por categoria");
    }
    return response.json();
}

async function apiGetIndicadoresFinanceiros(periodo) {
    const response = await apiFetch(`/api/relatorios/indicadores-financeiros?periodo=${periodo}`, { comJson: false });
    if(!response.ok) {
        throw new Error("Erro ao buscar indicadores financeiros");
    }
    return response.json();
}

async function apiGetResumoEstoque() {
    const response = await apiFetch(`/api/relatorios/resumo-estoque`, { comJson: false });
    if(!response.ok) {
        throw new Error("Erro ao buscar resumo do estoque");
    }
    return response.json();
}

// Modal de erro
function showNotificationError(mensagem) {
    document.getElementById('notificationErrorMessage').textContent = mensagem;
    document.getElementById('notificationErrorModal').style.display = 'flex';

    const handler = (e) => {
        if (e.key === 'Escape') {
            closeNotificationErrorModal();
            document.removeEventListener('keydown', handler);
        }
    };
    document.addEventListener('keydown', handler);
}

// Modal de sucesso
function showNotificationSuccess(mensagem) {
    document.getElementById('notificationSuccessMessage').textContent = mensagem;
    document.getElementById('notificationSuccessModal').style.display = 'flex';

    const handler = (e) => {
        if (e.key === 'Escape') {
            closeNotificationSuccessModal();
            document.removeEventListener('keydown', handler);
        }
    };
    document.addEventListener('keydown', handler);
}

function closeNotificationErrorModal() {
    document.getElementById('notificationErrorModal').style.display = 'none';
}

function closeNotificationSuccessModal() {
    document.getElementById('notificationSuccessModal').style.display = 'none';
}