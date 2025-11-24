/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/main/resources/templates/**/*.html",
    "./src/main/resources/static/**/*.html",
    "./src/**/*.{js,ts}"
  ],

  // 👇 This line enables dark mode based on a CSS selector (like `.dark`)
  darkMode: "selector",

  theme: {
    extend: {},
  },
  plugins: [],
};
