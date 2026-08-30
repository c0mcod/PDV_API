const API_BASE_URL = "http://localhost:8090";

const token = localStorage.getItem("token");
if (!token) {
    window.location.href = "/pages/login.html";
}

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

/* =======================
   PRODUTOS
======================= */

async function apiGetProducts(page = 0, size = 10, ativo = true) {
    const response = await fetch(`${API_BASE_URL}/produto/lista?page=${page}&size=${size}&ativo=${ativo}`, {
        headers: getAuthHeaders(false)
    });
    if (!response.ok) {
        throw new Error("Erro ao buscar produtos");
    }
    return response.json();
}

async function apiGetAllProducts(ativo = true) {
    const response = await fetch(`${API_BASE_URL}/produto/lista-todos?ativo=${ativo}`, {
        headers: getAuthHeaders(false)
    });
    if (!response.ok) {
        throw new Error("Erro ao buscar todos os produtos");
    }
    return response.json();
}

async function apiGetStatsProducts() {
    const response = await fetch(`${API_BASE_URL}/produto/stats`, {
        headers: getAuthHeaders(false)
    });
    if (!response.ok) {
        throw new Error("Erro ao buscar Stats");
    }
    return response.json();
}

async function apiAtualizarProduct(id, produto) {
    const response = await fetch(`${API_BASE_URL}/produto/atualiza/${id}`, {
        method: 'PUT',
        headers: getAuthHeaders(),
        body: JSON.stringify(produto)
    });
    if (!response.ok) throw new Error("Erro ao atualizar produto");
    return response.json();
}

async function apiRegistrarEntrada(produtoId, quantidade) {
    const response = await fetch(`${API_BASE_URL}/produto/${produtoId}/adicionar-estoque`, {
        method: "POST",
        headers: getAuthHeaders(),
        body: JSON.stringify({ quantidade })
    });

    if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || "Erro ao registrar entrada");
    }

    return response.json();
}

async function apiDeleteProducts(id) {
    const response = await fetch(`${API_BASE_URL}/produto/${id}`, {
        method: "DELETE",
        headers: getAuthHeaders(false)
    });

    if (!response.ok) {
        throw new Error("Erro ao deletar produto");
    }
    return;
}


async function apiCreateProduct(product) {
    const response = await fetch(`${API_BASE_URL}/produto`, {
        method: "POST",
        headers: getAuthHeaders(),
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
        const response = await fetch(`${API_BASE_URL}/produto/exportar/excel`, {
            headers: getAuthHeaders(false)
        });

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
        console.error('Erro:', error);
        alert('Erro ao exportar produtos');
    }
}

async function apiAtivarProduto(id) {
    const response = await fetch(`${API_BASE_URL}/produto/${id}/ativar-produto`, {
        method: "POST",
        headers: getAuthHeaders(false)
    });
    if (!response.ok) throw new Error("Erro ao ativar produto");
}

/* =======================
   USUÁRIOS
======================= */

async function apiGetUsuarios() {
    const response = await fetch(`${API_BASE_URL}/usuarios`, {
        headers: getAuthHeaders(false)
    });
    if (!response.ok) {
        throw new Error("Erro ao buscar usuários");
    }
    return response.json();
}

/* =======================
   VENDAS
======================= */

async function apiAbrirVenda(usuarioId) {
    const response = await fetch(`${API_BASE_URL}/venda/abrir?usuarioId=${usuarioId}`, {
        method: "POST",
        headers: getAuthHeaders(false)
    });

    if (!response.ok) {
        throw new Error("Erro ao abrir venda");
    }

    return response.json();
}

async function apiGetVenda(vendaId) {
    const response = await fetch(`${API_BASE_URL}/venda/${vendaId}`, {
        headers: getAuthHeaders(false)
    });
    if (!response.ok) throw new Error("Erro ao carregar venda");
    return response.json();
}

async function apiAdicionarItemVenda(vendaId, payload) {
    const response = await fetch(`${API_BASE_URL}/venda/${vendaId}/itens`, {
        method: "POST",
        headers: getAuthHeaders(),
        body: JSON.stringify(payload)
    });

    if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || "Erro ao adicionar item");
    }

    return response.json();
}

async function apiRemoverItemVenda(vendaId, itemId) {
    const response = await fetch(`${API_BASE_URL}/venda/${vendaId}/remover-item`, {
        method: "POST",
        headers: getAuthHeaders(),
        body: JSON.stringify({ itemId })
    });

    if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || "Erro ao remover item");
    }

    return response.json();
}

async function apiFinalizarVenda(vendaId, dadosPagamento) {
    const response = await fetch(`${API_BASE_URL}/venda/${vendaId}/finalizar`, {
        method: "POST",
        headers: getAuthHeaders(),
        body: JSON.stringify(dadosPagamento)
    });

    if (!response.ok) {
        const error = await response.json();
        throw new Error(error.mensagem || "Erro ao finalizar venda");
    }

    return response.json();
}

async function apiCancelarVenda(vendaId) {
    const response = await fetch(`${API_BASE_URL}/venda/${vendaId}/cancelar-venda`, {
        method: "POST",
        headers: getAuthHeaders(false)
    });

    if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || "Erro ao cancelar venda");
    }

    return response.json();
}

async function apiGetDetalhesVenda(vendaId) {
    const response = await fetch(`${API_BASE_URL}/historico-vendas/detalhes/${vendaId}`, {
        headers: getAuthHeaders(false)
    });
    if (!response.ok) {
        throw new Error("Erro ao buscar detalhes da venda");
    }
    return response.json();
}

async function apiGetHistoricoVendas(dataInicio, dataFim, operadorId, page, size) {
    let url = `${API_BASE_URL}/historico-vendas?dataInicio=${dataInicio}&dataFim=${dataFim}&page=${page}&size=${size}`;
    if (operadorId) url += `&operadorId=${operadorId}`;

    const response = await fetch(url, {
        headers: getAuthHeaders(false)
    });
    if (!response.ok) {
        throw new Error("Erro ao buscar histórico de vendas");
    }
    return response.json();
}

async function apiGetHistoricoStats(dataInicio, dataFim, operadorId) {
    const params = new URLSearchParams({ dataInicio, dataFim });
    if (operadorId) params.append("operadorId", operadorId);
    const res = await fetch(`${API_BASE_URL}/historico-vendas/stats?${params}`, {
        headers: getAuthHeaders(false)
    });
    if (!res.ok) throw new Error("Erro ao buscar stats");
    return res.json();
}

async function apiExportarHistoricoVendas(operadorId, dataInicio, dataFim) {
    try {
        let url = `${API_BASE_URL}/historico-vendas/exportar/excel?dataInicio=${dataInicio}&dataFim=${dataFim}`;
        if (operadorId) url += `&operadorId=${operadorId}`;

        const response = await fetch(url, {
            headers: getAuthHeaders(false)
        });

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
        console.error('Erro:', error);
        showNotificationError('Erro ao exportar histórico de vendas');
    }
}
/* =======================
   RELATÓRIOS
======================= */

async function apiGetProdutosVendidosKpi(periodo) {
    const response = await fetch(`${API_BASE_URL}/api/relatorios/kpis?periodo=${periodo}`, {
        headers: getAuthHeaders(false)
    });
    if(!response.ok) {
        throw new Error("Erro ao buscar KPI de produtos vendidos");
    }
    return response.json();
}

async function apiGetProdutosVendidosKpiPorData(dataInicio, dataFim) {
    const response = await fetch(`${API_BASE_URL}/api/relatorios/kpis?dataInicio=${dataInicio}&dataFim=${dataFim}`, {
        headers: getAuthHeaders(false)
    });
    if(!response.ok) throw new Error("Erro ao buscar KPI de produtos vendidos");
    return response.json();
}

async function apiGetVendasDiasemanaPorData(dataInicio, dataFim) {
    const response = await fetch(`${API_BASE_URL}/api/relatorios/vendas-dia-semana?dataInicio=${dataInicio}&dataFim=${dataFim}`, {
        headers: getAuthHeaders(false)
    });
    if(!response.ok) throw new Error("Erro ao buscar vendas por dia");
    return response.json();
}

async function apiGetTopProdutosPorData(dataInicio, dataFim, limite = 5) {
    const response = await fetch(`${API_BASE_URL}/api/relatorios/top-produtos?dataInicio=${dataInicio}&dataFim=${dataFim}&limite=${limite}`, {
        headers: getAuthHeaders(false)
    });
    if(!response.ok) throw new Error("Erro ao buscar top produtos");
    return response.json();
}

async function apiGetVendasCategoriaPorData(dataInicio, dataFim) {
    const response = await fetch(`${API_BASE_URL}/api/relatorios/vendas-categoria?dataInicio=${dataInicio}&dataFim=${dataFim}`, {
        headers: getAuthHeaders(false)
    });
    if(!response.ok) throw new Error("Erro ao buscar vendas por categoria");
    return response.json();
}

async function apiGetIndicadoresFinanceirosPorData(dataInicio, dataFim) {
    const response = await fetch(`${API_BASE_URL}/api/relatorios/indicadores-financeiros?dataInicio=${dataInicio}&dataFim=${dataFim}`, {
        headers: getAuthHeaders(false)
    });
    if(!response.ok) throw new Error("Erro ao buscar indicadores financeiros");
    return response.json();
}

async function apiGetVendasDiaSemana(periodo) {
    const response = await fetch(`${API_BASE_URL}/api/relatorios/vendas-dia-semana?periodo=${periodo}`, {
        headers: getAuthHeaders(false)
    });
    if(!response.ok) {
        throw new Error("Erro ao buscar vendas por dia");
    }
    return response.json();
}

async function apiGetTopProdutos(periodo, limite = 5) {
    const response = await fetch(`${API_BASE_URL}/api/relatorios/top-produtos?periodo=${periodo}&limite=${limite}`, {
        headers: getAuthHeaders(false)
    });
    if(!response.ok) {
        throw new Error("Erro ao buscar top produtos");
    }
    return response.json();
}

async function apiGetVendasCategoria(periodo) {
    const response = await fetch(`${API_BASE_URL}/api/relatorios/vendas-categoria?periodo=${periodo}`, {
        headers: getAuthHeaders(false)
    });
    if(!response.ok) {
        throw new Error("Erro ao buscar vendas por categoria");
    }
    return response.json();
}

async function apiGetIndicadoresFinanceiros(periodo) {
    const response = await fetch(`${API_BASE_URL}/api/relatorios/indicadores-financeiros?periodo=${periodo}`, {
        headers: getAuthHeaders(false)
    });
    if(!response.ok) {
        throw new Error("Erro ao buscar indicadores financeiros");
    }
    return response.json();
}

async function apiGetResumoEstoque() {
    const response = await fetch(`${API_BASE_URL}/api/relatorios/resumo-estoque`, {
        headers: getAuthHeaders(false)
    });
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