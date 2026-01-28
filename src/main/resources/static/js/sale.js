/* =========================
   VERIFICAÇÃO INICIAL
========================= */

const params = new URLSearchParams(window.location.search);
const vendaIdAtual = params.get("vendaId");

if (!vendaIdAtual) {
  window.location.href = "/pages/awaiting.html";
}

/* =========================
   ESTADO DA APLICAÇÃO
========================= */

let produtos = [];
let itensVenda = [];

/* =========================
   ELEMENTOS DO DOM
========================= */

const produtoSelect = document.getElementById("produtoSelect");
const quantidadeInput = document.getElementById("quantidadeInput");
const precoUnitarioInput = document.getElementById("precoUnitarioInput");
const precoTotalInput = document.getElementById("precoTotalInput");
const produtoTitulo = document.getElementById("produtoTitulo");

const btnAdicionarItem = document.getElementById("btnAdicionarItem");
const listaItensVenda = document.getElementById("listaItensVenda");
const subtotalVenda = document.getElementById("subtotalVenda");

const btnFinalizarVenda = document.getElementById("btnFinalizarVenda");
const btnCancelarVenda = document.getElementById("btnCancelarVenda");

/* =========================
   INICIALIZAÇÃO
========================= */

document.addEventListener("DOMContentLoaded", async () => {
  try {
    produtos = await apiGetProducts();
    carregarSelectProdutos(produtos);
  } catch (e) {
    alert("Erro ao inicializar o PDV");
    console.error(e);
  }
});

/* =========================
   PRODUTOS - SELECT
========================= */

function carregarSelectProdutos(produtos) {
  produtos.forEach(produto => {
    const option = document.createElement("option");
    option.value = produto.id;
    option.textContent = `${produto.nome}`;
    produtoSelect.appendChild(option);
  });
}

produtoSelect.addEventListener("change", () => {
  const produto = obterProdutoSelecionado();
  if (!produto) {
    limparProdutoDestaque();
    return;
  }
  atualizarProdutoDestaque(produto);
});

quantidadeInput.addEventListener("input", () => {
  const produto = obterProdutoSelecionado();
  if (produto) {
    atualizarProdutoDestaque(produto);
  }
});

/* =========================
   PRODUTO EM DESTAQUE
========================= */

function obterProdutoSelecionado() {
  const id = Number(produtoSelect.value);
  return produtos.find(p => p.id === id);
}

function atualizarProdutoDestaque(produto) {
  const quantidade = Number(quantidadeInput.value);
  produtoTitulo.textContent = `${quantidade} x ${produto.nome}`;
  precoUnitarioInput.value = `R$ ${produto.preco.toFixed(2)}`;
  precoTotalInput.value = `R$ ${(produto.preco * quantidade).toFixed(2)}`;
}

function limparProdutoDestaque() {
  produtoTitulo.textContent = "Selecione um produto";
  precoUnitarioInput.value = "";
  precoTotalInput.value = "";
}

/* =========================
   ADICIONAR ITEM
========================= */

btnAdicionarItem.addEventListener("click", async () => {
  const produto = obterProdutoSelecionado();
  const quantidade = Number(quantidadeInput.value);

  if (!produto) {
    alert("Selecione um produto");
    return;
  }

  if (quantidade <= 0) {
    alert("Quantidade inválida");
    return;
  }

  try {
    const response = await apiAdicionarItemVenda(vendaIdAtual, {
      idProduto: produto.id,
      quantidade
    });

    itensVenda.push({
      itemId: response.itemId,
      productId: produto.id,
      quantity: quantidade
    });

    renderizarItens();
    atualizarSubtotal();
    quantidadeInput.value = 1;

  } catch (e) {
    alert(e.message);
  }
});

/* =========================
   REMOVER ITEM
========================= */

async function removerItem(index) {
  const item = itensVenda[index];

  try {
    await apiRemoverItemVenda(vendaIdAtual, item.itemId);
    itensVenda.splice(index, 1);
    renderizarItens();
    atualizarSubtotal();
  } catch (e) {
    alert(e.message);
  }
}

/* =========================
   RENDERIZAR ITENS
========================= */

function renderizarItens() {
  listaItensVenda.innerHTML = "";

  itensVenda.forEach((item, index) => {
    const produto = produtos.find(p => p.id === item.productId);

    const div = document.createElement("div");
    div.className = "item-row";

    div.innerHTML = `
      <div class="item-numero">#${index + 1}</div>
      <div>
        <div class="item-nome">${produto.nome}</div>
        <div class="item-codigo">${produto.codigo}</div>
      </div>
      <div class="item-qtd">${item.quantity} un</div>
      <div class="item-preco">R$ ${produto.preco.toFixed(2)}</div>
      <div class="item-total" style="font-weight: 700;">
        R$ ${(produto.preco * item.quantity).toFixed(2)}
      </div>
      <button class="btn-remove-item">✕</button>
    `;

    div.querySelector(".btn-remove-item")
       .addEventListener("click", () => removerItem(index));

    listaItensVenda.appendChild(div);
  });
}

/* =========================
   SUBTOTAL
========================= */

function atualizarSubtotal() {
  let total = 0;

  itensVenda.forEach(item => {
    const produto = produtos.find(p => p.id === item.productId);
    total += produto.preco * item.quantity;
  });

  subtotalVenda.textContent = `R$ ${total.toFixed(2)}`;
}

/* =========================
   FINALIZAR VENDA
========================= */

btnFinalizarVenda.addEventListener("click", async () => {
  if (itensVenda.length === 0) {
    alert("Nenhum item na venda");
    return;
  }

  try {
    await apiFinalizarVenda(vendaIdAtual);
    window.location.href = "/pages/awaiting.html";
  } catch (e) {
    alert(e.message);
  }
});

/* =========================
   CANCELAR VENDA
========================= */

btnCancelarVenda.addEventListener("click", async () => {
  if (!confirm("Cancelar a venda atual?")) return;

  try {
    await apiCancelarVenda(vendaIdAtual);
    window.location.href = "/pages/awaiting.html";
  } catch (e) {
    alert(e.message);
  }
});