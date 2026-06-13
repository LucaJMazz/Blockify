let uploadButton, 
imgResult, 
dropBox, 
scaleSlider, 
scaleBox, 
fileInput, 
dropBoxImage, 
navbar, 
scaleContainer, 
loadingBar, 
viewButton, 
clearButton, 
downloadLink,
howToUseDiv,
aboutDiv,
apiDiv;
let navbarButtons = [];
let server = "https://blockify-production.up.railway.app"
// let server = "http://localhost:8080";


document.addEventListener('DOMContentLoaded', () => {
    uploadButton = document.getElementById('uploadButton');
    imgResult = document.getElementById('imgResult');
    dropBox = document.querySelector('.drop-box');
    fileInput = dropBox.querySelector('input');
    scaleSlider = document.getElementById('scale-slider');
    scaleBox = document.getElementById('scale-reader');
    navbar = document.getElementById('navbar');
    scaleContainer = document.getElementsByClassName('scale-container')[0];
    loadingBar = document.getElementsByClassName('loading-bar')[0];

    howToUseDiv = document.getElementsByClassName('how-to-use')[0];
    aboutDiv = document.getElementsByClassName('about')[0];
    apiDiv = document.getElementsByClassName('api')[0];


    navbarButtons = navbar.querySelectorAll('button');
    navbarButtons[0].addEventListener('click', () => {howToUseDiv.scrollIntoView({ behavior: 'smooth', block: 'start' });})
    navbarButtons[1].addEventListener('click', () => {aboutDiv.scrollIntoView({ behavior: 'smooth', block: 'start' });})
    navbarButtons[2].addEventListener('click', () => {apiDiv.scrollIntoView({ behavior: 'smooth', block: 'start' });})


    downloadLink = document.getElementById('download-link');
    copyButton = document.getElementById('copy-button');
    viewButton = document.getElementById('view-button');
    clearButton = document.getElementById('clear-button');
    
    dropBoxImage = document
        .getElementsByClassName('dropbox-wrapper')[0]
        .getElementsByTagName('img')[0];

    setTimeout(() => {
        navbar.classList.remove('zero-scale');
        dropBox.classList.remove('zero-scale');
        scaleContainer.classList.remove('zero-scale');
    }, 200);

    uploadButton.addEventListener('click', sendData);
    viewButton.addEventListener('click', () => {
        window.open(imgResult.src, '_blank');
    });
    clearButton.addEventListener('click', () => {
        imgResult.src = "/assets/wool-background.png"
    });

    scaleSlider.addEventListener('input', () => {
        scaleBox.textContent = "Scale: "+scaleSlider.value;
    });

    dropBox.addEventListener('dragover', (e) => {
        e.preventDefault();
        dropBox.classList.add('drag-over');
    });
    dropBox.addEventListener('dragleave', () => {
        dropBox.classList.remove('drag-over');
    });
    dropBox.addEventListener('drop', (e) => {
        e.preventDefault();
        dropBox.classList.remove('drag-over');

        const files = e.dataTransfer.files;
        if (files.length > 0) {
            fileInput.files = files; // Links the dropped file to the hidden input
        }
    });
    fileInput.addEventListener('change', () => {
        const file = fileInput.files[0];
        if (file) {
            dropBoxImage.src = URL.createObjectURL(file);
        }
    });
});

async function sendData() {
	console.log('Polling');
    loadingBar.classList.remove('hidden');
    try {
        const formData = new FormData(); // Creates form data to send values to server
        formData.append("file", fileInput.files[0]);
        formData.append("scale", scaleSlider.value);

        const response = await fetch( `${server}/api/upload`, {
            method: 'POST', // Must explicitly define the method
            body: formData 
        });

        if (!response.ok) throw new Error(`Status: ${response.status}`);

        const blob = await response.blob();
        imgResult.src = URL.createObjectURL(blob);
        downloadLink.href = imgResult.src;
        console.log('Created:', blob);
        loadingBar.classList.add('hidden');
    } catch (error) {
        console.error('Error posting data:', error);
    }
}

