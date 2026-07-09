(() => {
  const menuIcon = document.querySelector('.menu-icon');
  const menuDropdown = document.querySelector('.menu-dropdown');

  if (!menuIcon || !menuDropdown) {
    return;
  }

  menuIcon.addEventListener('click', (event) => {
    event.stopPropagation();
    menuDropdown.classList.toggle('active');
  });

  document.addEventListener('click', (event) => {
    if (!menuIcon.contains(event.target) && !menuDropdown.contains(event.target)) {
      menuDropdown.classList.remove('active');
    }
  });
})();
