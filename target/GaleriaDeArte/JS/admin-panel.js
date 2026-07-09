(function () {
  var estadoActual = 'pendiente';

  // Tabs de estado
  document.querySelectorAll('.admin-tab').forEach(function (btn) {
    btn.addEventListener('click', function () {
      document.querySelectorAll('.admin-tab').forEach(function (b) { b.classList.remove('active'); });
      btn.classList.add('active');
      estadoActual = btn.getAttribute('data-estado');
      cargarObras(estadoActual);
    });
  });

  cargarObras('pendiente');

  function cargarObras(estado) {
    var grid = document.getElementById('obras-grid');
    grid.innerHTML = '<p class="cargando">Cargando obras...</p>';

    fetch('/api/admin/obras?estado=' + encodeURIComponent(estado))
      .then(function (r) { return r.ok ? r.json() : Promise.reject(r.status); })
      .then(function (obras) {
        if (!obras.length) {
          grid.innerHTML = '<p class="sin-obras">No hay obras en estado "' + estado + '".</p>';
          return;
        }
        grid.innerHTML = obras.map(renderCard).join('');

        // Botones aprobar
        grid.querySelectorAll('.btn-aprobar').forEach(function (btn) {
          btn.addEventListener('click', function () {
            aprobar(parseInt(btn.getAttribute('data-id')));
          });
        });

        // Botones rechazar
        grid.querySelectorAll('.btn-rechazar').forEach(function (btn) {
          btn.addEventListener('click', function () {
            var motivo = prompt('Motivo del rechazo (opcional):') || '';
            rechazar(parseInt(btn.getAttribute('data-id')), motivo);
          });
        });
      })
      .catch(function (err) {
        grid.innerHTML = '<p class="sin-obras">Error al cargar obras: ' + err + '</p>';
      });
  }

  function renderCard(obra) {
    var img = obra.imagenUrl
      ? '<img src="' + obra.imagenUrl + '" alt="' + esc(obra.titulo) + '" />'
      : '<div class="sin-imagen"><i class="fa-solid fa-image"></i></div>';

    var acciones = '';
    if (obra.estado === 'pendiente') {
      acciones = '<button class="btn-aprobar btn-publicar" data-id="' + obra.id + '">'
        + '<i class="fa-solid fa-check"></i> Aprobar</button>'
        + '<button class="btn-rechazar btn-favorito" data-id="' + obra.id + '">'
        + '<i class="fa-solid fa-xmark"></i> Rechazar</button>';
    } else if (obra.estado === 'aprobada') {
      acciones = '<span class="badge-estado aprobada"><i class="fa-solid fa-check-circle"></i> Aprobada</span>'
        + '<button class="btn-rechazar btn-favorito btn-eliminar-obra" data-id="' + obra.id + '">'
        + '<i class="fa-solid fa-xmark"></i> Rechazar</button>';
    } else {
      acciones = '<span class="badge-estado rechazada"><i class="fa-solid fa-ban"></i> Rechazada</span>'
        + '<button class="btn-aprobar btn-publicar" data-id="' + obra.id + '">'
        + '<i class="fa-solid fa-check"></i> Aprobar</button>';
    }

    return '<div class="card-obra admin-card" data-id="' + obra.id + '">'
      + img
      + '<h2>' + esc(obra.titulo) + '</h2>'
      + '<p class="obra-artista"><i class="fa-solid fa-user"></i> ' + esc(obra.artista) + '</p>'
      + '<p class="descripcion">' + esc(obra.descripcion) + '</p>'
      + '<p class="precio">$' + Number(obra.precio).toLocaleString('es-CO') + ' COP</p>'
      + '<div class="obra-acciones admin-acciones">' + acciones + '</div>'
      + '</div>';
  }

  function aprobar(id) {
    fetch('/api/admin/obra/aprobar?id=' + id, { method: 'POST' })
      .then(function (r) { return r.json(); })
      .then(function () { cargarObras(estadoActual); })
      .catch(function (e) { alert('Error al aprobar: ' + e); });
  }

  function rechazar(id, motivo) {
    fetch('/api/admin/obra/rechazar?id=' + id + '&motivo=' + encodeURIComponent(motivo), { method: 'POST' })
      .then(function (r) { return r.json(); })
      .then(function () { cargarObras(estadoActual); })
      .catch(function (e) { alert('Error al rechazar: ' + e); });
  }

  function esc(str) {
    if (!str) return '';
    return str.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
  }
})();
