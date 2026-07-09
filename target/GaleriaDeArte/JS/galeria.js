/**
 * galeria.js
 * Carga las obras desde /api/obras y las dibuja en la galería.
 * También conecta el panel de filtros con la búsqueda real.
 */

(function () {
  var contenedor = document.getElementById('contenido-individuales');
  var inputQ = document.getElementById('filtro-q');
  var inputPrecio = document.getElementById('filtro-precio');
  var labelPrecio = document.getElementById('filtro-precio-label');
  var btnFiltrar = document.getElementById('btn-aplicar-filtro');
  var badgeConteo = document.getElementById('badge-individuales');

  if (!contenedor) return;

  // Carga inicial sin filtros
  cargarObras('', '');

  // Botón "Aplicar filtro"
  if (btnFiltrar) {
    btnFiltrar.addEventListener('click', function () {
      var q = inputQ ? inputQ.value.trim() : '';
      var precioMax = inputPrecio ? inputPrecio.value : '';
      cargarObras(q, precioMax);
    });
  }

  // Actualiza la etiqueta del slider de precio en tiempo real
  if (inputPrecio && labelPrecio) {
    inputPrecio.addEventListener('input', function () {
      labelPrecio.textContent =
        'Hasta $' + Number(inputPrecio.value).toLocaleString('es-CO');
    });
  }

  /* ── CARGA Y RENDERIZADO ──────────────────────────────────────── */
  function cargarObras(q, precioMax) {
    contenedor.innerHTML = '<p>Cargando obras...</p>';

    var url = '../api/obras';
    var params = [];
    if (q) params.push('q=' + encodeURIComponent(q));
    if (precioMax) params.push('precioMax=' + encodeURIComponent(precioMax));
    if (params.length) url += '?' + params.join('&');

    fetch(url)
      .then(function (r) { return r.ok ? r.json() : Promise.reject(r.status); })
      .then(function (obras) {
        contenedor.innerHTML = '';

        if (badgeConteo) badgeConteo.textContent = obras.length;

        if (!obras.length) {
          contenedor.innerHTML =
            '<p>No se encontraron obras con esos filtros. ' +
            '<a href="explorar-obras.html">Ver todas</a></p>';
          return;
        }

        obras.forEach(function (obra) {
          contenedor.appendChild(crearTarjeta(obra));
        });
      })
      .catch(function (err) {
        console.error('Error cargando obras:', err);
        contenedor.innerHTML = '<p>Error al cargar las obras. Intenta de nuevo.</p>';
      });
  }

  /* ── TARJETA ──────────────────────────────────────────────────── */
  function crearTarjeta(obra) {
    var card = document.createElement('div');
    card.className = 'card-obra';
    card.dataset.id = obra.id;

    var img = document.createElement('img');
    img.src = obra.imagenUrl || '';
    img.alt = obra.titulo;
    card.appendChild(img);

    var h2 = document.createElement('h2');
    h2.textContent = obra.titulo;
    card.appendChild(h2);

    var autor = document.createElement('div');
    autor.className = 'autor';
    autor.innerHTML = '<i class="fa-solid fa-user"></i> <span></span>';
    autor.querySelector('span').textContent = obra.artista;
    card.appendChild(autor);

    var desc = document.createElement('p');
    desc.className = 'descripcion';
    desc.textContent = obra.descripcion;
    card.appendChild(desc);

    var precio = document.createElement('p');
    precio.className = 'precio';
    precio.textContent = '$' + Number(obra.precio).toLocaleString('es-CO');
    card.appendChild(precio);

    var acciones = document.createElement('div');
    acciones.className = 'acciones';

    // Botón ver detalle
    var verDetalle = document.createElement('a');
    verDetalle.href = 'obra-detalle.html?id=' + obra.id;
    verDetalle.className = 'icon-link';
    verDetalle.setAttribute('aria-label', 'Ver detalle');
    verDetalle.innerHTML = '<i class="fa-solid fa-eye"></i>';
    acciones.appendChild(verDetalle);

    // Botón favorito
    var corazon = document.createElement('i');
    corazon.className = 'fa-solid fa-heart icon-link';
    corazon.setAttribute('aria-label', 'Guardar en favoritos');
    corazon.title = 'Guardar en favoritos';
    corazon.addEventListener('click', function () {
      fetch('../api/favoritos?obraId=' + obra.id, { method: 'POST' })
        .then(function (r) { return r.ok ? r.json() : Promise.reject(r.status); })
        .then(function () {
          corazon.style.color = 'var(--color-primary, #e74c3c)';
          corazon.title = 'Ya está en favoritos';
        })
        .catch(function (err) {
          if (err === 401) {
            window.location.href = 'login.html';
          } else {
            console.error('Error guardando favorito:', err);
          }
        });
    });
    acciones.appendChild(corazon);

    card.appendChild(acciones);
    return card;
  }

})();