function toggleDisplay(isWriter) {
    if (isWriter) {
        document.getElementById('updateBtn').style.display = 'inline';
        document.getElementById('deleteBtn').style.display = 'inline';
    } else {
        document.getElementById('updateBtn').style.display = 'none';
        document.getElementById('deleteBtn').style.display = 'none';
    }
}