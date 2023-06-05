function getBoards() {
    fetch('/rest/boards/getByUsername/'+username)
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
        out += '<section class="text-dark board-list-block item-block-bg rounded-3 ms-2 p-2 shadow" id='+data._embedded.boardList[i].id+'>'
            +'<div class="board-list-block-content text-break lh-lg text-wrap fs-5 text-dark"><a href="/board/'+data._embedded.boardList[i].id+'">'+data._embedded.boardList[i].title+'</a></div>'
            +'</section>';

    }
    document.getElementById("boards-container").innerHTML=out;
}

window.addEventListener("load", getBoards(), false);