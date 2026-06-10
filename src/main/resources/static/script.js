let uploadButton, fileBox, scaleBox, imgResult;
//const server = "https://prude-runny-emphatic.ngrok-free.dev";

let server = "http://localhost:8080";
/**
 * For production: Tests if local host is active, else route to railway server
 */
async function testLocalHost() {
	try {
		
		const response = await fetch( `${server}/api/status`, {
			method: 'GET',
		});
		
		if (!response.ok) throw new Error(`Status: ${response.status}`);
		
		const data = await response.json();
		if (data.status == 'online')
			return true;
		
	} catch (error) {
		return false;
	}
}
if (!testLocalHost) {
	server = "https://blockify-production.up.railway.app";
}
console.log('running server: ', server)

document.addEventListener('DOMContentLoaded', () => {
    uploadButton = document.getElementById('uploadButton');
    fileBox = document.getElementById('file');
    scaleBox = document.getElementById('scale');
    imgResult = document.getElementById('resultImg');

    uploadButton.addEventListener('click', sendData);
});

async function sendData() {
	console.log('Polling');
    try {
        const formData = new FormData(); // Creates form data to send values to server
        formData.append("file", fileBox.files[0]);
        formData.append("scale", scaleBox.value);

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