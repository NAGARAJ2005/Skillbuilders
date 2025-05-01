const images = [
  'images/body-images/image-1.jpg',
  'images/body-images/image-2.jpg',
  'images/body-images/image-3.jpg'
];

let currentIndex = 0;

function changeImage() {
  const dynamicImage = document.querySelector('.dynamic-image');
  currentIndex = (currentIndex + 1) % images.length;
  dynamicImage.src = images[currentIndex];
}

setInterval(changeImage, 2000);