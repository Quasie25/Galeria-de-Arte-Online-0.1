/**
 * componentes.js
 * Maneja navbar, footer, sesión y control de acceso en todas las páginas.
 * En cada HTML solo se necesita:
 *   <div id="navbar-placeholder"></div>   ← donde va el navbar
 *   <div id="footer-placeholder"></div>   ← donde va el footer
 *   <script src="../JS/componentes.js"></script>  ← primer script
 */

(function () {

  /* ── PÁGINA ACTUAL ───────────────────────────────────────────── */
  var pagina = window.location.pathname.split('/').pop();

  /* ── FOOTER (igual en todas las páginas) ─────────────────────── */
  var FOOTER_HTML =
    '<footer class="footer">' +
    '<div class="footer-container">' +
    '<div class="footer-section">' +
    '<i class="fas fa-store footer-logo"></i>' +
    '<p>Plataforma para exhibir, comprar y vender obras de arte físicas de manera fácil y segura.</p>' +
    '<p>Síguenos en nuestras redes</p>' +
    '<div class="social-icons">' +
    '<i class="fab fa-facebook"></i>' +
    '<i class="fab fa-instagram"></i>' +
    '<i class="fab fa-twitter"></i>' +
    '<i class="fab fa-youtube"></i>' +
    '</div>' +
    '</div>' +
    '<div class="footer-section">' +
    '<h3>Explorar</h3>' +
    '<div class="footer-links">' +
    '<a href="explorar-obras.html" class="footer-link">Galería</a>' +
    '<a href="sobre-nosotros.html" class="footer-link">Sobre Nosotros</a>' +
    '<a href="ayuda.html" class="footer-link">Ayuda</a>' +
    '</div>' +
    '</div>' +
    '<div class="footer-section">' +
    '<h3>Boletín semanal</h3>' +
    '<p>Recibe novedades y obras destacadas directamente en tu correo.</p>' +
    '<div class="newsletter">' +
    '<input type="email" placeholder="Tu correo aquí" />' +
    '<button>Suscribirse</button>' +
    '</div>' +
    '</div>' +
    '</div>' +
    '</footer>';

  /* ── NAV-RIGHT: íconos según rol y página actual ─────────────── */
  /* CAMBIO 1: ahora recibe 'rol' como parámetro                    */
  function navRight(rol) {

    // Sin sesión: mostrar botones de login y registro
    if (!rol || rol === 'invitado') {
      return (
        '<div class="nav-right">' +
        '<a href="login.html" class="login-link">Inicio de sesión</a>' +
        '<a href="registro.html" class="btn-register">Registro</a>' +
        '</div>'
      );
    }

    // Admin: sin carrito ni favoritos
    if (rol === 'admin') {
      return '<div class="nav-right"></div>';
    }

    // En carrito: favoritos + perfil (sin botón de carrito)
    if (pagina === 'carrito.html') {
      return (
        '<div class="nav-right">' +
        '<a href="favoritos.html" class="icon-link" aria-label="Favoritos">' +
        '<i class="fa-solid fa-heart"></i>' +
        '</a>' +
        '<a href="perfil.html" class="icon-link" aria-label="Perfil">' +
        '<i class="fa-solid fa-user"></i>' +
        '</a>' +
        '</div>'
      );
    }

    // En perfil: favoritos + carrito (sin botón de perfil)
    if (pagina === 'perfil.html') {
      return (
        '<div class="nav-right">' +
        '<a href="favoritos.html" class="icon-link" aria-label="Favoritos">' +
        '<i class="fa-solid fa-heart"></i>' +
        '</a>' +
        '<a href="carrito.html" class="icon-link" aria-label="Carrito">' +
        '<i class="fa-solid fa-cart-shopping"></i>' +
        '</a>' +
        '</div>'
      );
    }

    // Resto de páginas: favoritos + carrito + perfil
    return (
      '<div class="nav-right">' +
      '<a href="favoritos.html" class="icon-link" aria-label="Favoritos">' +
      '<i class="fa-solid fa-heart"></i>' +
      '</a>' +
      '<a href="carrito.html" class="icon-link" aria-label="Carrito">' +
      '<i class="fa-solid fa-cart-shopping"></i>' +
      '</a>' +
      '<a href="perfil.html" class="icon-link" aria-label="Perfil">' +
      '<i class="fa-solid fa-user"></i>' +
      '</a>' +
      '</div>'
    );
  }

  /* ── MENÚ DESPLEGABLE según rol ──────────────────────────────── */
  function menuDropdown(rol) {

    // Menú para invitados (sin sesión)
    if (!rol || rol === 'invitado') {
      return (
        '<div class="menu-dropdown">' +
        '<a href="explorar-obras.html">Explorar Obras</a>' +
        '<a href="sobre-nosotros.html">Sobre Nosotros</a>' +
        '<a href="ayuda.html">Ayuda</a>' +
        '<hr style="border-color:rgba(255,255,255,0.15);margin:8px 16px">' +
        '<a href="login.html">Iniciar sesión</a>' +
        '<a href="registro.html">Registrarse</a>' +
        '</div>'
      );
    }

    // Menú para usuarios logueados
    var items = '<a href="explorar-obras.html">Explorar Obras</a>';
    items += '<a href="mis-compras.html">Mis Compras</a>';

    if (rol === 'artista') {
      items += '<a href="subir-obra.html">Publicar Obra</a>';
    }

    if (rol === 'admin') {
      items += '<a href="admin-panel.html" style="color:#c77dff;font-weight:700">Panel Admin</a>';
    }

    items += '<a href="sobre-nosotros.html">Sobre Nosotros</a>';
    items += '<a href="ayuda.html">Ayuda</a>';
    items += '<hr style="border-color:rgba(255,255,255,0.15);margin:8px 16px">';

    if (rol !== 'admin') {
      items += '<a href="perfil.html">Mi perfil</a>';
    }

    items += '<a href="#" id="btn-logout">Cerrar sesión</a>';

    return '<div class="menu-dropdown">' + items + '</div>';
  }

  /* ── INYECTAR NAVBAR ─────────────────────────────────────────── */
  /* CAMBIO 2: navRight ahora recibe el rol                         */
  function inyectarNavbar(rol) {
    var placeholder = document.getElementById('navbar-placeholder');
    if (!placeholder) return;

    var html =
      '<nav class="navbar">' +
      '<div class="nav-left">' +
      '<a href="explorar-obras.html" class="nav-logo">Galería de Arte</a>' +
      '<i class="fa-solid fa-bars menu-icon"></i>' +
      '</div>' +
      navRight(rol) +
      '</nav>' +
      menuDropdown(rol);

    placeholder.outerHTML = html;
    activarMenuHamburguesa();
  }

  /* ── MENÚ HAMBURGUESA ────────────────────────────────────────── */
  function activarMenuHamburguesa() {
    var icon = document.querySelector('.menu-icon');
    var dropdown = document.querySelector('.menu-dropdown');
    if (!icon || !dropdown) return;

    var iconNuevo = icon.cloneNode(true);
    icon.parentNode.replaceChild(iconNuevo, icon);

    iconNuevo.addEventListener('click', function (e) {
      e.stopPropagation();
      dropdown.classList.toggle('active');
    });

    document.addEventListener('click', function (e) {
      if (!iconNuevo.contains(e.target) && !dropdown.contains(e.target)) {
        dropdown.classList.remove('active');
      }
    });
  }

  /* ── APLICAR data-rol a elementos del contenido ─────────────── */
  function aplicarRol(rol) {
    document.querySelectorAll('[data-rol]').forEach(function (el) {
      var permitidos = el.dataset.rol.split(',').map(function (r) { return r.trim(); });
      el.style.display = permitidos.includes(rol) ? '' : 'none';
    });
  }

  /* ── INYECTAR FOOTER ─────────────────────────────────────────── */
  var footerPlaceholder = document.getElementById('footer-placeholder');
  if (footerPlaceholder) footerPlaceholder.outerHTML = FOOTER_HTML;

  /* ── FLUJO PRINCIPAL: sesión → navbar → permisos ─────────────── */
  // Paso 1: navbar de invitado inmediatamente (evita parpadeo)
  inyectarNavbar('invitado');

  // Paso 2: consultar sesión real y actualizar si hace falta
  fetch('../api/session')
    .then(function (r) { return r.ok ? r.json() : { rol: 'invitado' }; })
    .catch(function () { return { rol: 'invitado' }; })
    .then(function (s) {
      var rol = s.rol || 'invitado';

      // CAMBIO 3: actualizar TANTO el dropdown COMO los íconos de la derecha
      if (rol !== 'invitado') {
        var dropdown = document.querySelector('.menu-dropdown');
        if (dropdown) dropdown.outerHTML = menuDropdown(rol);

        var navRightEl = document.querySelector('.nav-right');
        if (navRightEl) navRightEl.outerHTML = navRight(rol);

        activarMenuHamburguesa();
      }

      // Mostrar/ocultar elementos según rol
      aplicarRol(rol);

      // Redirigir si la página requiere login y el usuario no está logueado
      if (document.body.dataset.proteger === 'true' && rol === 'invitado') {
        window.location.href = 'login.html?error=sesion';
      }
    });

  /* ── CERRAR SESIÓN ───────────────────────────────────────────── */
  document.addEventListener('click', function (e) {
    if (e.target && e.target.id === 'btn-logout') {
      e.preventDefault();
      fetch('../api/session', { method: 'DELETE' })
        .finally(function () {
          window.location.href = 'login.html';
        });
    }
  });

})();