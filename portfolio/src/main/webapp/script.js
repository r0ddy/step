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

$(function(){
    $("nav").load("nav.html");    
});

function loadProjects(){
    let projects = [
        {
            image: {
                src: "/images/animalbytes.png",
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
        let clone = document.importNode(projectTemplate.content, true); // where true means deep copy
        document.body.querySelector('.projects').appendChild(clone);
    });
}
