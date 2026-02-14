/* =========================
   INICIAR VENDA
========================= */

let vendaIniciando = false;

async function iniciarVenda() {
  if (vendaIniciando) return;
  vendaIniciando = true;

  try {
    const venda = await apiAbrirVenda();

    if (!venda.vendaId) {
      throw new Error("ID da venda não retornado");
    }

    window.location.href = `/pages/sale.html?vendaId=${venda.vendaId}`;

  } catch (e) {
    alert(e.message);
    vendaIniciando = false;
  }
}

// Iniciar venda ao pressionar Enter
document.addEventListener("keydown", (e) => {
  if (e.key === "Enter") {
    iniciarVenda();
  }
});

// ===================================
// BOTÃO DE COLAPSAR MENU
// ===================================
const toggleMenuBtn = document.getElementById('toggleMenu');
const navMenu = document.getElementById('navMenu');

toggleMenuBtn.addEventListener('click', () => {
    navMenu.classList.toggle('collapsed');
});