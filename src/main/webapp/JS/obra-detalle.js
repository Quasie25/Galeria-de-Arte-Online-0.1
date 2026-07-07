(function () {
  // Leer id de la URL
  var params = new URLSearchParams(window.location.search);
  var obraId = params.get('id');
  if (!obraId) return;

  fetch('/api/obra?id=' + encodeURIComponent(obraId))
    .then(function (r) { return r.ok ? r.json() : Promise.reject(r.status); })
    .then(function (obra) {
      var img = document.getElementById('obra-imagen');
      if (img && obra.imagenUrl) { img.src = obra.imagenUrl; img.alt = obra.titulo; }

      setText('obra-titulo', obra.titulo);
      setText('obra-artista', obra.artista);
      setText('obra-descripcion', obra.descripcion);
      setText('obra-precio', '$ ' + Number(obra.precio).toLocaleString('es-CO') + ' COP');

      // Botón carrito
      var btnCarrito = document.getElementById('btn-agregar-carrito');
      if (btnCarrito) {
        btnCarrito.addEventListener('click', function () {
          // Integración simple con carrito.js: guardar en sessionStorage
          var carrito = JSON.parse(sessionStorage.getItem('carrito') || '{}');
          carrito[obra.id] = (carrito[obra.id] || 0) + 1;
          sessionStorage.setItem('carrito', JSON.stringify(carrito));
          btnCarrito.textContent = '✓ Agregado al carrito';
          btnCarrito.disabled = true;
        });
      }

      // Mostrar sección artista si corresponde
      fetch('/api/session')
        .then(function (r) { return r.json(); })
        .then(function (s) {
          if (s.rol === 'artista') {
            var editar = document.getElementById('btn-editar');
            if (editar) editar.href = 'subir-obra.html?id=' + obra.id;
          }
        })
        .catch(function () { });
    })
    .catch(function (err) {
      setText('obra-titulo', 'Obra no encontrada');
      console.error('Error cargando obra:', err);
    });

  function setText(id, val) {
    var el = document.getElementById(id);
    if (el) el.textContent = val || '';
  }
})();
