console.log("Dark Script loaded.....")

let currentTheme=getTheme();
console.log(currentTheme);
changeTheme();

// todo
function changeTheme()
{
// set to web page
     document.querySelector('html').classList.add(currentTheme);


// set the listener to change theme button
const changeThemeButton=document.querySelector('#theme_change_button')
changeThemeButton.querySelector("span").textContent=
       currentTheme=="light"?"Dark":"Light";
changeThemeButton.addEventListener("click",(event)=>{
console.log("change theme clicked....");
document.querySelector("html").classList.remove(currentTheme);
  if(currentTheme==="dark")
  {
       //set theme to light
       currentTheme='light';
  }
  else
  {
      //set theme to dark
      currentTheme='dark';
  }
  //update to localStorage
  setTheme(currentTheme);
  document.querySelector("html").classList.add(currentTheme);
  //Change the text of button
  changeThemeButton.querySelector("span").textContent=
  currentTheme=="light"?"Dark":"Light";

});

}

//set theme to localstorage
function setTheme(theme)
{
  localStorage.setItem("theme",theme);
}

// get theme from localstorage
function getTheme()
{
   let theme=localStorage.getItem("theme");
   return theme ? theme:"light";
}
