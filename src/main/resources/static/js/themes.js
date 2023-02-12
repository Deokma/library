// const button = document.getElementById('toggle-button');
// const div = document.querySelector('div');
//
// button.addEventListener('click', function() {
//     if (div.classList.contains('light-theme')) {
//         console.log("light")
//         div.classList.remove('light-theme');
//         div.classList.add('dark-theme');
//
//     } else {
//         console.log("dark")
//         div.classList.remove('dark-theme');
//         div.classList.add('light-theme');
//
//     }
//     console.log("213124")
// });

const toggleButton = document.getElementById('toggle-button');
const div = document.querySelector('div');
const theme = document.getElementById('themecss');

toggleButton.addEventListener('click', function () {
    if (div.classList.contains('light-theme')) {
        console.log("light")
        div.classList.remove('light-theme');
        div.classList.add('dark-theme');
        localStorage.setItem('theme', 'dark-theme');
    } else {
        console.log("dark")
        div.classList.remove('dark-theme');
        div.classList.add('light-theme');
        localStorage.setItem('theme', 'light-theme');
    }
});

const element = document.getElementById('element');
const storedTheme = localStorage.getItem('theme');
if (storedTheme) {
    div.classList.add(storedTheme);
}