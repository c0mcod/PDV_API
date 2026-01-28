/* =========================
   ELEMENTOS DO DOM
========================= */

const btnIniciarVenda = document.querySelector(".btn-iniciar");

/* =========================
   INICIAR VENDA
========================= */

btnIniciarVenda.addEventListener("click", async () => {
  btnIniciarVenda.disabled = true;

  try {
    const venda = await apiAbrirVenda();

    if (!venda.vendaId) {
      throw new Error("ID da venda não retornado");
    }

    window.location.href = `/pages/sale.html?vendaId=${venda.vendaId}`;

  } catch (e) {
    alert(e.message);
    btnIniciarVenda.disabled = false;
  }
});