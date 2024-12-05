const fs = require('fs');
const path = require('path');

// Directories
const inputDir = './'; // Adjust this path if the JSON files are not in the same folder as the script
const outputDir = './output';

// Ensure the output directory exists
if (!fs.existsSync(outputDir)) {
    fs.mkdirSync(outputDir);
}

// Get all JSON files in the directory
const jsonFiles = fs.readdirSync(inputDir).filter(file => file.endsWith('.json'));

jsonFiles.forEach(file => {
    const fileNameWithoutExt = path.basename(file, '.json');
    
    // Create the new JSON content
    const newContent = {
        "model": {
            "type": "minecraft:model",
            "model": `mochila:item/${fileNameWithoutExt}`
        }
    };

    // Write the new JSON file to the output directory
    const outputPath = path.join(outputDir, file);
    fs.writeFileSync(outputPath, JSON.stringify(newContent, null, 2), 'utf8');
    console.log(`Processed: ${file}`);
});

console.log('All files processed!');
