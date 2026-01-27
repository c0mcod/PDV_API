const btnIniciarVenda = document.querySelector(".btn-iniciar");

btnIniciarVenda.addEventListener("click", async () => {
  btnIniciarVenda.disabled = true;

  try {
    const response = await fetch(`${API_BASE_URL}/venda/abrir`, {
      method: "POST"
    });

    if (!response.ok) {
      throw new Error("Erro ao abrir venda");
    }

    const venda = await response.json();

    if (!venda.vendaId) {
      throw new Error("ID da venda não retornado");
    }

    // redireciona para o PDV com o ID da venda
    window.location.href = `/pages/sale.html?vendaId=${venda.vendaId}`;

  } catch (e) {
    alert(e.message);
    btnIniciarVenda.disabled = false;
  }
});
