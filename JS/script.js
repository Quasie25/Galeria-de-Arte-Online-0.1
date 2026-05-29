// ── Menú desplegable ──────────────────────────────────────────
const menuIcon = document.querySelector('.menu-icon');
const menuDropdown = document.querySelector('.menu-dropdown');

if (menuIcon && menuDropdown) {
  menuIcon.addEventListener('click', (e) => {
    // stopPropagation evita que el click llegue al document y cierre el menú justo después de abrirlo
    e.stopPropagation();
    menuDropdown.classList.toggle('active');
  });

  // cerrar si se hace click fuera del menú
  document.addEventListener('click', (e) => {
    if (!menuIcon.contains(e.target) && !menuDropdown.contains(e.target)) {
      menuDropdown.classList.remove('active');
    }
  });
}

// ── Carrusel ──────────────────────────────────────────────────
let index = 0;

function carousel() {
  const slides = document.querySelectorAll('.slides');
  if (!slides || slides.length === 0) {
    return;
  }
  slides.forEach(slide => slide.style.display = 'none');
  index++;
  if (index > slides.length) { index = 1; }
  if (slides[index - 1]) slides[index - 1].style.display = 'block';
  setTimeout(carousel, 2000);
}

// iniciar carrusel solo si hay slides en la página
if (document.querySelectorAll('.slides').length > 0) {
  carousel();
}

// ── Carrito ───────────────────────────────────────────────────
document.addEventListener('DOMContentLoaded', () => {
  const menosBtns = document.querySelectorAll('.menos');
  const masBtns = document.querySelectorAll('.mas');
  const cantidades = document.querySelectorAll('.input-cantidad');
  const subtotalEl = document.getElementById('subtotal');
  const envioSelect = document.getElementById('envio');
  const envioEl = document.getElementById('envioValor');
  const totalEl = document.getElementById('total');

  function formatoCOP(valor) {
    return valor.toLocaleString('es-CO', {
      style: 'currency',
      currency: 'COP',
      minimumFractionDigits: 0,
    });
  }

  function actualizarTotales() {
    let subtotal = 0;
    document.querySelectorAll('.producto').forEach(producto => {
      const precio = parseInt(producto.querySelector('.precio').dataset.precio);
      const cantidad = parseInt(producto.querySelector('.input-cantidad').value);
      subtotal += precio * cantidad;
    });
    const envio = parseInt(envioSelect.value) || 0;
    subtotalEl.textContent = formatoCOP(subtotal);
    envioEl.textContent = formatoCOP(envio);
    totalEl.textContent = formatoCOP(subtotal + envio);
  }

  menosBtns.forEach(btn => {
    btn.addEventListener('click', e => {
      const input = e.target.parentElement.querySelector('.input-cantidad');
      let valor = parseInt(input.value);
      if (valor > 1) input.value = valor - 1;
      actualizarTotales();
    });
  });

  masBtns.forEach(btn => {
    btn.addEventListener('click', e => {
      const input = e.target.parentElement.querySelector('.input-cantidad');
      input.value = parseInt(input.value) + 1;
      actualizarTotales();
    });
  });

  cantidades.forEach(input => input.addEventListener('change', actualizarTotales));
  envioSelect.addEventListener('change', actualizarTotales);

  actualizarTotales();
});

// ── Panel de filtros ──────────────────────────────────────────
const abrirFiltro = document.getElementById('abrirFiltro');
const cerrarFiltro = document.getElementById('cerrarFiltro');
const panelFiltros = document.getElementById('panelFiltros');

if (abrirFiltro && cerrarFiltro && panelFiltros) {
  abrirFiltro.addEventListener('click', (e) => {
    e.stopPropagation();
    panelFiltros.classList.add('active');
  });

  cerrarFiltro.addEventListener('click', (e) => {
    panelFiltros.classList.remove('active');
  });

  // cierra panel si se hace click fuera
  document.addEventListener('click', (e) => {
    if (!panelFiltros.contains(e.target) && e.target !== abrirFiltro && !abrirFiltro.contains(e.target)) {
      panelFiltros.classList.remove('active');
    }
  });
}