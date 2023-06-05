function getTaskLists() {
    fetch('/rest/lists/getListsByBoard/' + boardId)
        .then(response => response.json())
        .then((taskLists) => {
                console.log(taskLists)
                displayLists(taskLists);
            }
        );
}

function displayLists(data) {
    var i;
    var out = '';
    for (i = 0; i < data._embedded.taskListList.length; i++) {
        out += `<section class="task-container col-task-list text-dark item-block-bg rounded-3 ms-2 p-2 shadow" id=` + data._embedded.taskListList[i].id + `>
                    <header class="list-header mb-3" role="button">
                            <div class="list-header-name">
                                <div class="list-name-overlay"></div>
                                <textarea class="list-name form-control">` + data._embedded.taskListList[i].title + `</textarea>
                            </div>
                        </header>
                        <section class="list-tasks list-column">`;
        for (j = 0; j < data._embedded.taskListList[i].tasks.length; j++) {
            out += `<article class="task-block mb-2 p-2 rounded-2 shadow-sm" role="button">
                                <div class="task-name">` + data._embedded.taskListList[i].tasks[j].title + `</div>
                                <div class="task-description">` + data._embedded.taskListList[i].tasks[j].description + `</div>
                            </article>`;
        }
        out += `</section>
                        <footer class="list-footer">
                            <button role="button" class="add-task-button btn btn-light shadow-sm container-fluid" data-bs-toggle="modal" data-bs-target="#modal-create-task">Add task</button>
                        </footer>
                </section>`;


    }
    document.getElementById("task-list-container").innerHTML = out;
    setListId();

}

function setListId() {
    $( ".add-task-button" ).on( "click", function() {
        $("#listId").val(($(this).closest('section').attr('id')));
    })
}

window.addEventListener("load", getTaskLists, false);