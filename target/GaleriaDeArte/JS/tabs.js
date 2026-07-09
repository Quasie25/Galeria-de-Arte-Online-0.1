(function () {
  var tabs = document.querySelectorAll('.tab');
  if (!tabs.length) return;

  tabs.forEach(function (tab) {
    tab.addEventListener('click', function () {
      // Activar tab
      tabs.forEach(function (t) { t.classList.remove('active'); });
      tab.classList.add('active');

      // Mostrar contenido correspondiente
      var nombre = tab.getAttribute('data-tab');
      document.querySelectorAll('.tab-content').forEach(function (sec) {
        sec.style.display = 'none';
      });
      var objetivo = document.getElementById('contenido-' + nombre);
      if (objetivo) objetivo.style.display = 'flex';
    });
  });
})();
