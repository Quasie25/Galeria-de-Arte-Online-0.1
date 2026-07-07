(function () {
  var contenedor = document.getElementById('lista-favoritos');
  var elInvitado = document.getElementById('favoritos-invitado');
  var elVacio = document.getElementById('favoritos-vacio');

  if (!contenedor) {
    return;
  }

  fetch('../api/session')
    .then(function (r) { return r.json(); })
    .then(function (s) {
      if (!s.rol || s.rol === 'invitado') {
        elInvitado.style.display = 'block';
        return;
      }
      cargarFavoritos();
    })
    .catch(function () {
      elInvitado.style.display = 'block';
    });

  function cargarFavoritos() {
    fetch('../api/favoritos')
      .then(function (r) { return r.ok ? r.json() : Promise.reject(r.status); })
      .then(function (obras) {
        if (!obras.length) {
          elVacio.style.display = 'block';
          return;
        }
        obras.forEach(function (obra) {
          contenedor.appendChild(crearTarjeta(obra));
        });
      })
      .catch(function (err) {
        console.error('Error cargando favoritos:', err);
      });
  }

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

    var descripcion = document.createElement('p');
    descripcion.className = 'descripcion';
    descripcion.textContent = obra.descripcion;
    card.appendChild(descripcion);

    var precio = document.createElement('p');
    precio.className = 'precio';
    precio.textContent = '$' + Number(obra.precio).toLocaleString('es-CO');
    card.appendChild(precio);

    var acciones = document.createElement('div');
    acciones.className = 'acciones';

    var verDetalle = document.createElement('a');
    verDetalle.href = 'obra-detalle.html?id=' + obra.id;
    verDetalle.className = 'icon-link';
    verDetalle.setAttribute('aria-label', 'Ver detalle');
    verDetalle.innerHTML = '<i class="fa-solid fa-eye"></i>';
    acciones.appendChild(verDetalle);

    var quitar = document.createElement('i');
    quitar.className = 'fa-solid fa-heart icon-link btn-quitar-favorito';
    quitar.setAttribute('aria-label', 'Quitar de favoritos');
    quitar.title = 'Quitar de favoritos';
    quitar.addEventListener('click', function () {
      quitarFavorito(obra.id, card);
    });
    acciones.appendChild(quitar);

    card.appendChild(acciones);
    return card;
  }

  function quitarFavorito(obraId, elemento) {
    fetch('../api/favoritos?obraId=' + encodeURIComponent(obraId), { method: 'DELETE' })
      .then(function (r) { return r.ok ? r.json() : Promise.reject(r.status); })
      .then(function () {
        elemento.remove();
        if (!contenedor.children.length) {
          elVacio.style.display = 'block';
        }
      })
      .catch(function (err) {
        console.error('Error quitando favorito:', err);
      });
  }
})();