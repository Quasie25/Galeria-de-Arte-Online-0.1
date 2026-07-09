(() => {
  const abrirFiltro = document.getElementById('abrirFiltro');
  const cerrarFiltro = document.getElementById('cerrarFiltro');
  const panelFiltros = document.getElementById('panelFiltros');

  if (!abrirFiltro || !cerrarFiltro || !panelFiltros) {
    return;
  }

  abrirFiltro.addEventListener('click', (event) => {
    event.stopPropagation();
    panelFiltros.classList.add('active');
  });

  cerrarFiltro.addEventListener('click', () => {
    panelFiltros.classList.remove('active');
  });

  document.addEventListener('click', (event) => {
    if (!panelFiltros.contains(event.target) && event.target !== abrirFiltro && !abrirFiltro.contains(event.target)) {
      panelFiltros.classList.remove('active');
    }
  });
})();
