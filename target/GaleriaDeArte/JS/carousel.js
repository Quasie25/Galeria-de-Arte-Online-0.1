(() => {
  const slides = document.querySelectorAll('.slides');
  if (!slides || slides.length === 0) {
    return;
  }

  let index = 0;

  const mostrarSlide = () => {
    slides.forEach((slide) => {
      slide.style.display = 'none';
    });
    index += 1;
    if (index > slides.length) {
      index = 1;
    }
    if (slides[index - 1]) {
      slides[index - 1].style.display = 'block';
    }
    setTimeout(mostrarSlide, 2000);
  };

  mostrarSlide();
})();
