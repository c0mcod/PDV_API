/* =========================
   INICIAR VENDA
========================= */

let vendaIniciando = false;

async function iniciarVenda() {
  if (vendaIniciando) return;
  vendaIniciando = true;

  document.body.style.cursor = 'wait';

  try {
    const venda = await apiAbrirVenda();
    if (!venda.vendaId) throw new Error("ID da venda não retornado");
    window.location.href = `/pages/sale.html?vendaId=${venda.vendaId}`;

  } catch (e) {
    alert(e.message);
    vendaIniciando = false;
    document.body.style.cursor = 'default';
  }
}

// Evento de Teclado (Enter)
document.addEventListener("keydown", (e) => {
  if (e.key === "Enter") {
    iniciarVenda();
  }
});

/* =========================
   NAVEGAÇÃO ADMIN
========================= */
const btnAdmin = document.getElementById('btnAdmin');

if (btnAdmin) {
  btnAdmin.addEventListener('click', () => {
    window.location.href = '../pages/inventory.html';
  });
}