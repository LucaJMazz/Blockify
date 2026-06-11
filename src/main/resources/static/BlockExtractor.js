const fs = require('fs');
const path = require('path');
const sharp = require('sharp');

async function extractAverageColor(filePath) {
    const { data, info } = await sharp(filePath)
        .raw()
        .toBuffer({ resolveWithObject: true });

    let r = 0, g = 0, b = 0;
    const pixelCount = info.width * info.height;

    for (let i = 0; i < data.length; i += info.channels) {
        r += data[i];
        g += data[i + 1];
        b += data[i + 2];
    }

    r =  Math.round(r / pixelCount);
    g = Math.round(g / pixelCount);
    b = Math.round(b / pixelCount);
    let str = `new MinecraftBlock("${path.basename(filePath, path.extname(filePath))}", ${r}, ${g},  ${b})`;
    return str;
    //  {
    //     name: path.basename(filePath, path.extname(filePath)),
    //     r: Math.round(r / pixelCount),
    //     g: Math.round(g / pixelCount),
    //     b: Math.round(b / pixelCount),
    // };
}

// ✅ targetDir is just the path string
const targetDir = path.join(__dirname, 'block_textures');

async function printBlocks() {
    const files = fs.readdirSync(targetDir)   // readdirSync called here, once
        .filter(f => /\.(png|jpg|jpeg)$/i.test(f));

     // Loop through the filtered array
    for (const file of files) {
        const filePath = path.join(targetDir, file);
        console.log( await extractAverageColor(filePath) +",");
    }
}
printBlocks();