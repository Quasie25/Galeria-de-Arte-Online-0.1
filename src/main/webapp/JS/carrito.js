(() => {
  const subtotalEl = document.getElementById('subtotal');
  const envioSelect = document.getElementById('envio');
  const envioEl = document.getElementById('envioValor');
  const totalEl = document.getElementById('total');
  const carritoVacioEl = document.getElementById('carrito-vacio');

  if (!subtotalEl || !envioSelect || !envioEl || !totalEl) {
    return;
  }

  const formatoCOP = (valor) =>
    valor.toLocaleString('es-CO', {
      style: 'currency',
      currency: 'COP',
      minimumFractionDigits: 0,
    });

  const obtenerItems = () => {
    const items = [];
    document.querySelectorAll('.producto').forEach((producto) => {
      const id = Number(producto.dataset.id);
      const inputCantidad = producto.querySelector('.input-cantidad');
      const cantidad = inputCantidad ? Number(inputCantidad.value) : 0;
      if (!Number.isNaN(id) && id > 0 && cantidad > 0) {
        items.push({ id, cantidad });
      }
    });
    return items;
  };

  const construirPayload = () => {
    const items = obtenerItems();
    const envio = Number.parseInt(envioSelect.value, 10) || 0;
    const body = new URLSearchParams();
    body.set(
      'items',
      items.map((item) => `${item.id}:${item.cantidad}`).join(',')
    );
    body.set('envio', envio.toString());
    return body;
  };

  const actualizarTotales = async () => {
    const response = await fetch('../api/carrito', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: construirPayload(),
    });

    if (!response.ok) {
      throw new Error('No fue posible validar el carrito.');
    }

    const data = await response.json();
    subtotalEl.textContent = formatoCOP(data.subtotal || 0);
    envioEl.textContent = formatoCOP(data.envio || 0);
    totalEl.textContent = formatoCOP(data.total || 0);

    if (carritoVacioEl) {
      carritoVacioEl.style.display = data.carritoVacio ? 'block' : 'none';
    }
  };

  const manejarError = (error) => {
    console.error(error);
  };

  document.querySelectorAll('.menos').forEach((btn) => {
    btn.addEventListener('click', () => {
      const contenedor = btn.closest('.cantidad-control');
      if (!contenedor) {
        return;
      }
      const input = contenedor.querySelector('.input-cantidad');
      if (!input) {
        return;
      }
      let valor = Number(input.value);
      if (Number.isNaN(valor)) {
        valor = 1;
      }
      if (valor > 1) {
        input.value = valor - 1;
      }
      actualizarTotales().catch(manejarError);
    });
  });

  document.querySelectorAll('.mas').forEach((btn) => {
    btn.addEventListener('click', () => {
      const contenedor = btn.closest('.cantidad-control');
      if (!contenedor) {
        return;
      }
      const input = contenedor.querySelector('.input-cantidad');
      if (!input) {
        return;
      }
      let valor = Number(input.value);
      if (Number.isNaN(valor)) {
        valor = 0;
      }
      input.value = valor + 1;
      actualizarTotales().catch(manejarError);
    });
  });

  document.querySelectorAll('.btn-eliminar').forEach((btn) => {
    btn.addEventListener('click', () => {
      const producto = btn.closest('.producto');
      if (producto) {
        producto.remove();
      }
      actualizarTotales().catch(manejarError);
    });
  });

  envioSelect.addEventListener('change', () => {
    actualizarTotales().catch(manejarError);
  });

  const comprarBtn = document.querySelector('.btn-comprar');
  if (comprarBtn) {
    comprarBtn.addEventListener('click', () => {
      fetch('../api/checkout/validar', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: construirPayload(),
      })
        .then((response) => {
          if (!response.ok) {
            throw new Error('No fue posible validar la compra.');
          }
          return response.json();
        })
        .then((data) => {
          if (!data.valido) {
            alert('No fue posible validar la compra. Revisa tu carrito.');
          }
        })
        .catch((error) => {
          console.error(error);
          alert('No fue posible validar la compra. Intenta de nuevo.');
        });
    });
  }

  actualizarTotales().catch(manejarError);
})();
