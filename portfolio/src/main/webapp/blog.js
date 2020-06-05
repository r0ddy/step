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
