(() => {
  const elementosConRol = document.querySelectorAll('[data-rol]');
  if (elementosConRol.length === 0) {
    return;
  }

  const aplicarRol = (rol) => {
    elementosConRol.forEach((elemento) => {
      const rolesPermitidos = (elemento.dataset.rol || '')
        .split(',')
        .map((item) => item.trim())
        .filter(Boolean);
      const permitido = rolesPermitidos.length === 0 || rolesPermitidos.includes(rol);
      elemento.style.display = permitido ? '' : 'none';
    });
  };

  fetch('../api/session')
    .then((response) => {
      if (!response.ok) {
        throw new Error('No fue posible obtener el rol de la sesión.');
      }
      return response.json();
    })
    .then((data) => {
      aplicarRol(data.rol || 'invitado');
    })
    .catch((error) => {
      console.error(error);
    });
})();
