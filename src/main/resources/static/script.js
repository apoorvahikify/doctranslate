function uploadFile() {
    const fileInput = document.getElementById('fileInput');
    const targetLang = document.getElementById('targetLang').value;

    if (!fileInput.files.length) {
        alert("Please choose a file!");
        return;
    }

    const formData = new FormData();
    formData.append("file", fileInput.files[0]);
    formData.append("targetLang", targetLang);

    fetch('/file', { method: 'POST', body: formData })
        .then(res => res.json())
        .then(data => {
            document.getElementById('outputResult').innerText = data.error || data.translatedText;
        })
        .catch(() => alert("Failed to process file translation."));
}

function translateText() {
    const text = document.getElementById('directText').value;
    const targetLang = document.getElementById('targetLang').value;

    if (!text.trim()) {
        alert("Enter some text!");
        return;
    }

    fetch('/translate', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ text: text, targetLang: targetLang })
    })
    .then(res => res.json())
    .then(data => {
        document.getElementById('outputResult').innerText = data.error || data.translatedText;
    })
    .catch(() => alert("Failed to process direct translation."));
}