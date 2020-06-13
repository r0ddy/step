let commentsVisible = 0;

async function getCommentsResponseFromServer(){
    let params = {
        numComments: 5
    };
    return getCommentsResponse(params);
}

async function getMoreCommentsResponseFromServer(){
    let additionalNumComments = document.querySelector("#numComments").value;
    console.log(additionalNumComments);
    let params = {
        numComments: commentsVisible + additionalNumComments
    };
    return getCommentsResponse(params);
}

async function getCommentsResponse(params){
    let request = await fetch("/comments?" + $.param(params));
    let commentsResponse = await request.json();
    return commentsResponse;
}

function populateList(params){
    let list = params.comments;
    let action = params.blobstoreUrl;
    commentsVisible = 0;
    list.forEach((item) => {
        // Create node for comment
        let itemNode = document.createElement("li");
        itemNode.id = "comment" + item.id;
        itemNode.innerText = item.text;
        itemNode.onclick = () => createResponseForm({
            action: action,
            id: item.id
        });

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

        // Create node for image
        if(item.hasOwnProperty("imageUrl")){
            let imageNode = document.createElement("img");
            imageNode.src = item.imageUrl;
            imageNode.class = "commentImage";
            listNode.appendChild(imageNode);
        }
        listNode.appendChild(itemNode);
        listNode.appendChild(responsesNode);
        listNode.appendChild(responseFormholderNode);
        commentsVisible++;
    });
}

async function loadComments(){
    let commentsResponse = await getCommentsResponseFromServer();
    populateList(commentsResponse);
    let commentForm = createCommentForm(commentsResponse);
    document.querySelector(".textBlurb").appendChild(commentForm);
}

async function loadMoreComments(params){
    document.querySelector("#responses").innerHTML = "";
    let commentsResponse = await getMoreCommentsResponseFromServer();
    populateList(commentsResponse);
}

function createCommentForm(params){
    let commentFormTemplate = document.querySelector("#postFormTemplate");
    let clone = document.importNode(commentFormTemplate.content, true);
    clone.querySelector("form").id = "commentForm";
    clone.querySelector("form").action = params.blobstoreUrl;
    clone.querySelector(".textBox").placeholder += "comment";
    return clone;
}

function createResponseForm(params){
    let id = params.id;
    // Remove responseForm if it exists
    let existingResponseForm = document.querySelector("#responseForm");
    if(existingResponseForm){
        existingResponseForm.parentElement.removeChild(existingResponseForm);
    }

    // Populate under comment with id
    let commentFormTemplate = document.querySelector("#postFormTemplate");
    let clone = document.importNode(commentFormTemplate.content, true);
    clone.querySelector("form").id = "responseForm";
    clone.querySelector("form").action = params.blobstoreUrl;
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

async function getUseResponseFromServer(){
    let request = await fetch("/user");
    let userResponse = await request.json();
    return userResponse;
}

async function loadUserAuthButton(){
    let userResponse = await getUseResponseFromServer();
    document.querySelector("a#userAuthLink").href = userResponse.url;
    document.querySelector("p#userAuthText").innerText = userResponse.loggedIn ? "Logout" : "Login";
}

function loadExperimentsBody(){
    loadComments();
    loadUserAuthButton();
}
