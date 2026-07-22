(() => {
  const preguntas = document.querySelectorAll('.faq-question');
  if (preguntas.length === 0) {
    return;
  }

  preguntas.forEach((btn) => {
    btn.addEventListener('click', () => {
      const item = btn.parentElement;
      const isOpen = item.classList.contains('active');

      document
        .querySelectorAll('.faq-item.active')
        .forEach((i) => i.classList.remove('active'));

      if (!isOpen) {
        item.classList.add('active');
      }
    });
  });
})();
