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
        out += `
            <section class="task-container col-task-list text-dark item-block-bg rounded-3 ms-2 p-2 shadow" id=` + data._embedded.taskListList[i].id + `>
                <header class="list-header mb-3">
                    <div class="list-header-name position-relative">
                        <div class="list-name-overlay"></div>
                        <textarea class="list-name form-control">` + data._embedded.taskListList[i].title + `</textarea>
                        <a class="position-absolute top-0 end-0 btn btn-close fs-6" href="/deleteList/`+data._embedded.taskListList[i].id+`"></a>
                    </div>
                </header>
                <section class="list-tasks list-column">`;
        for (j = 0; j < data._embedded.taskListList[i].tasks.length; j++) {
            out += `
                <article class="task-block mb-2 p-2 rounded-2 shadow-sm">
                    <div class="task-name position-relative"><span class="fs-5 mb-2 fw-bold text-break text-wrap">` + data._embedded.taskListList[i].tasks[j].title + `</span>
                        <a class="position-absolute top-0 end-0 btn btn-close fs-6" href="/deleteTask/`+data._embedded.taskListList[i].tasks[j].id+`"></a>
                    </div>
                    <div class="task-description text-break text-wrap fs-6">` + data._embedded.taskListList[i].tasks[j].description + `</div>
                </article>`;
            }
            out += `
                </section>
                    <footer class="list-footer">
                        <button role="button" class="add-task-button btn btn-light shadow-sm container-fluid" data-bs-toggle="modal" data-bs-target="#modal-create-task">Add task</button>
                    </footer>
                </section>`;
    }

    document.getElementById("task-list-container").innerHTML = out;
    setListId();

    $('.list-tasks').sortable({
        revert: true,
        connectWith: '.list-tasks',
        placeholder: "task-block-placeholder",
        update: function(event, ui) {
            // some action
        }
    }).disableSelection();
}

function setListId() {
    $( ".add-task-button" ).on( "click", function() {
        $("#listId").val(($(this).closest('section').attr('id')));
    })
}

window.addEventListener("load", getTaskLists, false);