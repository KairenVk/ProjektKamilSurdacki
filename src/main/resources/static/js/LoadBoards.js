function getBoards() {
    fetch('https://localhost:8443/rest/boards/getByUsername/'+username)
        .then(response => response.json())
        .then((boards) =>
            {
                console.log(boards)
                displayBoards(boards);
            }
        );
}

function displayBoards(data)     {
    var i;
    var out = '';
    for (i = 0; i < data._embedded.boardList.length; i++) {
        out += '<section class="task-container col-task-list text-dark task-list-bg rounded-3 ms-2 p-2 shadow" id='+data._embedded.boardList[i].id+'>'
            +'<header class="list-header mb-3" role="button">'
            +'<div class="list-header-name">'
            +'<div class="list-name-overlay"></div>'
            +'<a href='+data._embedded.boardList[i].id+'/boardView><span class="list-name form-control">'+data._embedded.boardList[i].title+'</span></a>'
            +'</div>'
            +'<span class="list-options">...</span>'
            +'</header>'
            +'<section class="list-tasks list-column">'
            +'</section>'
            +'</section>'
            +'</div>';

    }
    document.getElementById("boards-container").innerHTML=out;
}

window.addEventListener("load", getBoards(), false);