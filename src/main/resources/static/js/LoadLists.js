function getTaskLists() {
    fetch('https://localhost:8443/rest/lists/getListsByBoard/' + boardId)
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
        out += `<section class="task-container col-task-list text-dark task-list-bg rounded-3 ms-2 p-2 shadow" id=` + data._embedded.taskListList[i].id + `>
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
                            <button class="btn btn-light shadow-sm container-fluid" data-toggle="modal" data-target="#list`+data._embedded.taskListList[i].id+`" role="button">Add task</button></a>
                        </footer>
                        <div class="modal fade" id="list\`+data._embedded.taskListList[i].id+\`" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                                <div class="modal-dialog" role="document">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h5 class="modal-title">Modal title</h5>
                                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                <span aria-hidden="true">&times;</span>
                                            </button>
                                        </div>
                                        <div class="modal-body">
                                            <form action="/list/`+data._embedded.taskListList[i].id+`/addTask" method="post">
                                                <div class="form-group">
                                                    <label>Title</label>
                                                    <input type="text" th:field="*{title}" id="title" placeholder="Title"/>
                                                </div>
                                                <div class="form-group">
                                                    <label>Description</label>
                                                    <input type="text" th:field="*{description}" id="description" placeholder="New task description"/>
                                                </div>
                                                <input type="submit" value="Add board"/>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-primary">Save changes</button>
                                            <button type="button" class="btn btn-secondary"data-dismiss="modal">Close</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                    </section>`;


    }
    document.getElementById("task-list-container").innerHTML = out;
}

window.addEventListener("load", getTaskLists, false);