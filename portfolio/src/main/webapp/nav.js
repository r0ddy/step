function getToRootDirectory(){
    let path = window.location.pathname;
    let directories = -1;
    for(let index = 0; index < path.length; index++){
        if(path[index] === "/"){
            directories++;
        }
    }
    let root = '../'.repeat(directories);
    return root;
}

function loadNavbar(){
    let root = getToRootDirectory();
    $("nav").load(root + "nav.html", () => {
        let navLinks = Array.from(document.querySelectorAll(".navLink"));
        navLinks.forEach((navLink) => {
            let filename = navLink.href.split('/').pop()
            navLink.href = root + filename;
        });
    });
}
