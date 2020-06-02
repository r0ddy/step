// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

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

function loadProjects(){
    let projects = [
        {
            image: {
                src: "images/animalbytes.png",
                alt: "A screenshot of AnimalBytes."
            },
            description: 
            "AnimalBytes is an app that I worked on during \
            Cornell's Digital Agriculture Hackathon. It's \
            a mobile app made to hourly check the emotional \
            status of various farm animals. This helps \
            farmers detect distress that may be related to \
            disease, quickly and efficiently."
        },
        {
            image: {
                src: "/images/pioneer.png",
                alt: "A screenshot of Pioneer."
            },
            description: 
            "Pioneer is an app that I made for the Program \
            for Research in Youth Development and Engagement \
            (PRYDE) at Cornell. I created a survey app for \
            researchers to anonymously collect diary entries \
            from youth. Researchers at PRYDE can then view all \
            their collected data on a dashboard to view \
            and export as they wish."
        },
        {
            image: {
                src: "/images/eve.png",
                alt: "A screenshot of Eve."
            },
            description: 
            "Eve is an app that I helped make as a part of \
            Cornell's Design & Tech Initiative (DTI). At DTI, \
            we make apps that help Cornellians in their everyday \
            lives. In particular, Eve is a central repository \
            for any event at Cornell. We help students filter and \
            identify events and then notify them of any events \
            they plan on attending."
        }  
    ];

    let projectTemplate = document.querySelector('#projectTemplate');
    projects.forEach((project) => {
        projectTemplate.content.querySelector('img.projectImage').src = project.image.src;
        projectTemplate.content.querySelector('img.projectImage').alt = project.image.alt;
        projectTemplate.content.querySelector('p.projectDescription').textContent= project.description;
        let clone = document.importNode(projectTemplate.content, /*deep_copy=*/ true);
        document.body.querySelector('.projects').appendChild(clone);
    });
}

let blogEntries = [
    {
        image: {
            src: "/images/google.jpg",
            alt: "Google logo."
        },
        text: {
            title: "Google Internship: Week 2",
            date: "May 26, 2020",
            body: 
            "The reason I start my blog with a post about my second week \
            at Google is because not much happened during my short week 1. \
            During my first week, I was just setting up my machine and \
            getting to know my hosts and pod. It was during my second week \
            that I learned more."
        },
        link: "/blog/0.html"
    },
    {
        image: {
            src: "/images/google.jpg",
            alt: "Google logo."
        },
        text: {
            title: "Google Internship: Week 3",
            date: "June 1, 2020",
            body: 
            "Week 3 has just begun. I am suppose to wrap up my week 2 \
            project (this website), but I think I might have to spend a \
            more time trying to complete my blog."
        },
        link: "/blog/1.html"
    }
];

function loadBlogEntries(){
    let blogEntryTemplate = document.querySelector('#blogEntryTemplate')
    blogEntries.forEach((blogEntry) => {
        blogEntryTemplate.content.querySelector('a.blogLink').href = blogEntry.link;
        blogEntryTemplate.content.querySelector('img.blogThumbnailImage').src = blogEntry.image.src;
        blogEntryTemplate.content.querySelector('img.blogThumbnailImage').alt = blogEntry.image.alt;
        blogEntryTemplate.content.querySelector('h4.blogSummaryTitle').textContent = blogEntry.text.title;
        blogEntryTemplate.content.querySelector('p.blogSummaryDate').textContent = blogEntry.text.date;
        blogEntryTemplate.content.querySelector('p.blogSummaryText').textContent = blogEntry.text.body;
        let clone = document.importNode(blogEntryTemplate.content, /*deep_copy=*/ true);
        document.body.querySelector('.textBlurb').appendChild(clone);
    });
}   

function loadBlog(blogIndex){
    $("div#content").load("blogTemplate.html", () => {
        let blogEntry = blogEntries[blogIndex];
        document.querySelector('h1.blogTitle').textContent = blogEntry.text.title;
        document.querySelector('img.blogImage').src = blogEntry.image.src;
        document.querySelector('img.blogImage').alt = blogEntry.image.alt;
        document.querySelector('p.blogDate').textContent = blogEntry.text.date;
        document.querySelector('p.blogBody').textContent = blogEntry.text.body;
    });
}

async function getTextFromServer(){
    let request = await fetch("/data");
    let text = await request.text(); 
    document.querySelector('p#textFromServer').textContent = text;
}
