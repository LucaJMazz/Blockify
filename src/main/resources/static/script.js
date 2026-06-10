let uploadButton, fileBox, heightBox, widthBox, imgResult;
//const server = "https://prude-runny-emphatic.ngrok-free.dev";
const server = "http://localhost:8080";

document.addEventListener('DOMContentLoaded', () => {
    uploadButton = document.getElementById('uploadButton');
    fileBox = document.getElementById('file');
    heightBox = document.getElementById('height');
    widthBox = document.getElementById('width');
    imgResult = document.getElementById('resultImg');

    uploadButton.addEventListener('click', sendData);
});

async function sendData() {
	console.log('Clicked');
    try {
        const formData = new FormData(); // Creates form data to send values to server
        formData.append("file", fileBox.files[0]);
        formData.append("width", widthB ox.value);
        formData.append("height", heightBox.value);

        const response = await fetch( `${server}/api/upload`, {
            method: 'POST', // Must explicitly define the method
            body: formData 
        });

        if (!response.ok) throw new Error(`Status: ${response.status}`);

        const blob = await response.blob();
        imgResult.src = URL.createObjectURL(blob);
        console.log('Created:', blob);
    } catch (error) {
        console.error('Error posting data:', error);
    }
}