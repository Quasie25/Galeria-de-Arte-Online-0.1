(function () {
  var params = new URLSearchParams(window.location.search);
  var error = params.get('error');
  var el = document.getElementById('login-error');
  if (!el || !error) return;
  var mensajes = {
    'credenciales': 'Correo o contraseña incorrectos.',
    'campos': 'Por favor completa todos los campos.',
    'servidor': 'Error del servidor. Intenta de nuevo.'
  };
  if (mensajes[error]) {
    el.textContent = mensajes[error];
    el.style.display = 'block';
  }
})();