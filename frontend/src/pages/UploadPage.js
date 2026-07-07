/**
 * pages/UploadPage.js
 */
(function () {
  const state = MainLayout.mount({ active: '/upload' });
  if (!state) return;

  const fileDrop = document.getElementById('file-drop');
  const fileInput = document.getElementById('file-input');
  const fileLabel = document.getElementById('file-drop-label');
  let selectedFile = null;

  fileDrop.addEventListener('click', () => fileInput.click());
  fileDrop.addEventListener('dragover', (e) => { e.preventDefault(); });
  fileDrop.addEventListener('drop', (e) => {
    e.preventDefault();
    if (e.dataTransfer.files.length) {
      selectedFile = e.dataTransfer.files[0];
      showFile(selectedFile);
    }
  });
  fileInput.addEventListener('change', () => {
    if (fileInput.files.length) {
      selectedFile = fileInput.files[0];
      showFile(selectedFile);
    }
  });
  function showFile(file) {
    fileDrop.classList.add('has-file');
    fileLabel.innerHTML = `&#10003; ${RenderHelpers.escapeHtml(file.name)}`;
    document.getElementById('row-file').classList.remove('invalid');
  }

  const form = document.getElementById('upload-form');
  const submitBtn = document.getElementById('upload-submit');

  function validate() {
    let ok = true;
    const name = document.getElementById('contractName').value.trim();
    document.getElementById('row-contractName').classList.toggle('invalid', !name);
    if (!name) ok = false;
    if (!selectedFile) { document.getElementById('row-file').classList.add('invalid'); ok = false; }
    return ok;
  }

  form.addEventListener('submit', (e) => {
    e.preventDefault();
    if (!validate()) return;

    const values = {
      contractName: document.getElementById('contractName').value.trim(),
      contractType: document.getElementById('contractType').value,
      description: document.getElementById('description').value.trim(),
      firstParty: document.getElementById('firstParty').value.trim(),
      secondParty: document.getElementById('secondParty').value.trim(),
      startDate: document.getElementById('startDate').value,
      endDate: document.getElementById('endDate').value,
      contractValue: parseFloat(document.getElementById('contractValue').value) || 0,
      jurisdiction: document.getElementById('jurisdiction').value
    };

    submitBtn.disabled = true;
    submitBtn.textContent = 'Uploading and analyzing…';

    contractService.uploadContract(values, selectedFile)
      .then(() => {
        message.success('Contract uploaded and analysis started successfully!');
        window.location.href = './Dashboard.html';
      })
      .catch((error) => {
        if (!error.response) {
          message.error('Network error. Please try again.');
        } else {
          message.error('Failed to upload contract');
        }
      })
      .finally(() => {
        submitBtn.disabled = false;
        submitBtn.textContent = '\u2191 Upload and Analyze Contract';
      });
  });
})();
