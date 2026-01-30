// ===================================
// INTEGRAÇÃO INVENTÁRIO COM API
// ===================================

let todosOsProdutos = [];

// Inicializar quando a página carregar
document.addEventListener('DOMContentLoaded', async () => {
    await carregarProdutos();
    configurarEventos();
    configurarModal();
});

// ===================================
// CARREGAR PRODUTOS DO BACKEND
// ===================================
async function carregarProdutos() {
    try {
        const produtos = await apiGetProducts();
        todosOsProdutos = produtos;
        renderizarProdutos(produtos);
        atualizarEstatisticas(produtos);
    } catch (error) {
        console.error('Erro ao carregar produtos:', error);
        alert('Erro ao carregar produtos do servidor');
    }
}

// ===================================
// RENDERIZAR TABELA DE PRODUTOS
// ===================================
function renderizarProdutos(produtos) {
    const tbody = document.querySelector('.products-table tbody');

    if (!produtos || produtos.length === 0) {
        tbody.innerHTML = '<tr><td colspan="7" style="text-align: center;">Nenhum produto encontrado</td></tr>';
        return;
    }

    tbody.innerHTML = produtos.map(produto => `
        <tr>
            <td>
                <div class="product-code">${produto.codigo}</div>
            </td>
            <td>
                <div class="product-name">${produto.nome}</div>
            </td>
            <td>
                <span class="category-badge">${produto.categoria}</span>
            </td>
            <td>${produto.quantidadeEstoque} ${produto.unidade}</td>
            <td>
                <span class="stock-status ${getStatusClass(produto.quantidadeEstoque)}">
                    ${getStatusTexto(produto.quantidadeEstoque)}
                </span>
            </td>
            <td>
                <div class="price-value">R$ ${formatarPreco(produto.preco)}</div>
            </td>
            <td>
                <div class="action-buttons">
                    <button class="btn-action btn-detalhes" onclick="verDetalhes(${produto.id})">Ver</button>
                    <button class="btn-action btn-editar" onclick="editarProduto(${produto.id})">Editar</button>
                    <button class="btn-action btn-excluir" onclick="excluirProduto(${produto.id})">Excluir</button>
                </div>
            </td>
        </tr>
    `).join('');
}

// ===================================
// ATUALIZAR ESTATÍSTICAS
// ===================================
function atualizarEstatisticas(produtos) {
    const total = produtos.length;
    const valorTotal = produtos.reduce((soma, p) => soma + (p.preco * p.quantidadeEstoque), 0);
    const produtosBaixos = produtos.filter(p => p.quantidadeEstoque > 5 && p.quantidadeEstoque <= 15).length;
    const produtosCriticos = produtos.filter(p => p.quantidadeEstoque <= 5).length;

    // Atualizar cards
    const cards = document.querySelectorAll('.stat-card');
    if (cards[0]) cards[0].querySelector('.stat-value').textContent = total;
    if (cards[1]) cards[1].querySelector('.stat-value').textContent = `R$ ${formatarPreco(valorTotal)}`;
    if (cards[2]) cards[2].querySelector('.stat-value').textContent = produtosBaixos;
    if (cards[3]) cards[3].querySelector('.stat-value').textContent = produtosCriticos;
}

// ===================================
// FUNÇÕES AUXILIARES
// ===================================
function getStatusClass(quantidade) {
    if (quantidade <= 5) return 'stock-critico';
    if (quantidade <= 15) return 'stock-baixo';
    return 'stock-ok';
}

function getStatusTexto(quantidade) {
    if (quantidade <= 5) return 'Crítico';
    if (quantidade <= 15) return 'Estoque Baixo';
    return 'Em Estoque';
}

function formatarPreco(preco) {
    return preco.toFixed(2).replace('.', ',');
}

// ===================================
// BUSCA DE PRODUTOS
// ===================================
function configurarEventos() {
    const searchBox = document.querySelector('.search-box');
    if (searchBox) {
        searchBox.addEventListener('input', (e) => {
            const termo = e.target.value.toLowerCase();
            const produtosFiltrados = todosOsProdutos.filter(p =>
                p.nome.toLowerCase().includes(termo) ||
                p.codigo.toLowerCase().includes(termo) ||
                p.categoria.toLowerCase().includes(termo)
            );
            renderizarProdutos(produtosFiltrados);
        });
    }
}

// ===================================
// AÇÕES DOS BOTÕES
// ===================================
function verDetalhes(produtoId) {
    const produto = todosOsProdutos.find(p => p.id === produtoId);
    if (produto) {
        alert(`Produto: ${produto.nome}\nCódigo: ${produto.codigo}\nEstoque: ${produto.quantidadeEstoque} ${produto.unidade}\nPreço: R$ ${formatarPreco(produto.preco)}\nCategoria: ${produto.categoria}`);
    }
}

function editarProduto(produtoId) {
    alert(`Funcionalidade de edição será implementada para o produto ID: ${produtoId}`);
    // Aqui você pode abrir um modal ou redirecionar para página de edição
}

async function excluirProduto(produtoId) {
    if (!confirm('Tem certeza que deseja excluir este produto?')) {
        return;
    }

    alert('Funcionalidade de exclusão será implementada');
    // Implementar quando tiver o endpoint de DELETE no backend
    // await fetch(`${API_BASE_URL}/produto/${produtoId}`, { method: 'DELETE' });
    // await carregarProdutos();
}

// ===================================
// FUNÇÕES DO MODAL
// ===================================
function configurarModal() {
    const modal = document.getElementById('modalProduto');
    const btnNovo = document.getElementById('btnNovoProduto');
    const btnFechar = document.querySelector('.modal-close');
    const btnCancelar = document.querySelector('.btn-cancelar');
    const form = document.getElementById('formProduto');

    // Abrir modal
    btnNovo.addEventListener('click', () => {
        modal.style.display = 'block';
        form.reset();
    });

    // Fechar modal
    btnFechar.addEventListener('click', () => {
        modal.style.display = 'none';
    });

    btnCancelar.addEventListener('click', () => {
        modal.style.display = 'none';
    });

    // Fechar ao clicar fora
    window.addEventListener('click', (e) => {
        if (e.target === modal) {
            modal.style.display = 'none';
        }
    });

    // Submit do formulário
    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        await salvarProduto();
    });
}

async function salvarProduto() {
    const form = document.getElementById('formProduto');
    const modal = document.getElementById('modalProduto');

    const produto = {
        nome: form.nome.value,
        preco: parseFloat(form.preco.value),
        codigo: form.codigo.value,
        estoqueMinimo: parseFloat(form.estoqueMinimo.value),
        quantidadeEstoque: parseFloat(form.quantidadeEstoque.value),
        unidade: form.unidade.value,
        categoria: form.categoria.value
    };

    try {
        await apiCreateProduct(produto);
        alert('Produto cadastrado com sucesso!');
        modal.style.display = 'none';
        form.reset();
        await carregarProdutos();
    } catch (error) {
        console.error('Erro ao salvar produto:', error);
        alert('Erro ao salvar produto: ' + error.message);
    }
}