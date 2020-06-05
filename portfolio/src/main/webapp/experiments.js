let commentsVisible = 0;

async function getCommentsFromServer(){
    let params = {
        numComments: 5
    };
    return getComments(params);
}

async function getMoreCommentsFromServer(){
    let additionalNumComments = document.querySelector("#numComments").value;
    console.log(additionalNumComments);
    let params = {
        numComments: commentsVisible + additionalNumComments
    };
    return getComments(params);
}

async function getComments(params){
    let request = await fetch("/comments?" + $.param(params));
    let comments = await request.json();
    return comments;
}

function populateList(list){
    commentsVisible = 0;
    list.forEach((item) => {
        // Create node for comment
        let itemNode = document.createElement("li");
        itemNode.id = "comment" + item.id;
        itemNode.innerText = item.text;
        itemNode.onclick = () => createResponseForm(item.id);

        // Create node for responses to this comment
        let responsesNode = document.createElement("ul");
        responsesNode.id = "responses" + item.id;

        // Create node for response form holder for this comment
        let responseFormholderNode = document.createElement("div");
        responseFormholderNode.id = "responseHolder" + item.id;
        
        // Find which list this comment and its responses should go under
        let listNodeSelector = "#responses";
        // if comment has no parent its selector is just responses, otherwise append id
        if(item.hasOwnProperty("parentId")){
            listNodeSelector += item.parentId;
        }

        // Add all corresponding comment elements to list
        let listNode = document.querySelector(listNodeSelector);
        listNode.appendChild(itemNode);
        listNode.appendChild(responsesNode);
        listNode.appendChild(responseFormholderNode);
        commentsVisible++;
    });
}

async function loadComments(){
    let comments = await getCommentsFromServer();
    populateList(comments);
    let commentForm = createCommentForm();
    document.querySelector(".textBlurb").appendChild(commentForm);
}

async function loadMoreComments(){
    document.querySelector("#responses").innerHTML = "";
    let comments = await getMoreCommentsFromServer();
    populateList(comments);
}

function createCommentForm(){
    let commentFormTemplate = document.querySelector("#postFormTemplate");
    let clone = document.importNode(commentFormTemplate.content, true);
    clone.querySelector("form").id = "commentForm";
    clone.querySelector(".textBox").placeholder += "comment";
    return clone;
}

function createResponseForm(id){
    // Remove responseForm if it exists
    let existingResponseForm = document.querySelector("#responseForm");
    if(existingResponseForm){
        existingResponseForm.parentElement.removeChild(existingResponseForm);
    }

    // Populate under comment with id
    let commentFormTemplate = document.querySelector("#postFormTemplate");
    let clone = document.importNode(commentFormTemplate.content, true);
    clone.querySelector("form").id = "responseForm";
    clone.querySelector(".textBox").placeholder += "response";

    // Create hidden parentId input
    let parentIdNode = document.createElement("input");
    parentIdNode.type = "number";
    parentIdNode.name = "parentId";
    parentIdNode.value = id;
    parentIdNode.hidden = true;

    clone.querySelector("form").appendChild(parentIdNode);
    document.querySelector("div#responseHolder" + id).appendChild(clone);
}