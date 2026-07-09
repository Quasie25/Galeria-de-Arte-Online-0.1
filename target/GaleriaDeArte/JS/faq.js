(() => {
  const preguntas = document.querySelectorAll('.faq-question');
  if (preguntas.length === 0) {
    return;
  }

  preguntas.forEach((btn) => {
    btn.addEventListener('click', () => {
      const item = btn.parentElement;
      const isOpen = item.classList.contains('open');

      document
        .querySelectorAll('.faq-item.open')
        .forEach((i) => i.classList.remove('open'));

      if (!isOpen) {
        item.classList.add('open');
      }
    });
  });
})();
