function postRequest(address, data) {
    $.ajax({
        type: "POST",
        url: address,
        data: JSON.stringify(data),
        success: callBack(),
        contentType: "application/json",
    })

    function callBack(response) {
        return jQuery.parseJSON(response);
    }
}

/*
* Will check with the server to see if the uuid cookie is valid.
* If not it will open the log in modal.
*/
function CH_verifyLogin() {
    var data = {};
    data["uuid"] = Cookies.get("uuid");
    data = JSON.stringify(data);

    $.ajax({
        type: "POST",
        url: server_address_verifyLogin,
        data: data,
        contentType: "application/json",
        success: function(response) {
            if (response["response"] == "valid uuid") {
                logkitty(response["response"], "success");
                logkitty("It's an older uuid sir, but it checks out", "success");
                valid_uuid = true;
                getTasks();
            } else {
                logkitty(response["response"], "error");
                valid_uuid = false;
                $('#modal_log_in').openModal({
                    dismissible: false
                });
            }
        }
    })
}

/*
* Is launched by pressing the Log In button
* in the log in modal.
* Will close the modal if it logs in successfully
* Will also launch refresh data, which will populate the
* task list
*/
function logIn() {
    var username = $('#login_username').val();
    var password = $('#login_password').val();

    $('#login_username').val("");
    $('#login_password').val("");

    var data = {};
    data["username"] = username;
    data["password"] = password;
    data = JSON.stringify(data);

    $.ajax({
        type: "POST",
        url: server_address_login,
        data: data,
        contentType: "application/json",
        success: function(response) {
            if (response["status"] == "success") {
                Cookies.set("uuid", response["data"]);
                logkitty("Successfully logged in", "success");
                $('#modal_log_in').closeModal();
                getTasks();
            } else if (response["status"] == "error") {
                logkitty("Failed to log in", "error");
                Cookies.set("uuid", response["data"]);
            }
        }
    })
}

function getTasks() {
    var uuid = Cookies.get("uuid");

    var data = {};
    data["uuid"] = uuid;
    data["scope"] = "personal";
    data = JSON.stringify(data);

    $.ajax({
        type: "POST",
        url: server_address_getTasks,
        data: data,
        contentType: "application/json",
        success: function(response) {
            logkitty("Got a reply from getTasks", "success");
            tasks_array = response["tasks"];
            for (var i = 0; i < tasks_array.length; i++) {
                switch (tasks_array[i]["mTaskStatus"]) {
                    case "To Do":
                        addTodoTask(tasks_array[i]);
                        break;
                    case "In Process":
                        addInProgressTask(tasks_array[i]);
                        break;
                    case "Done":
                        addDoneTask(tasks_array[i]);
                        break;
                }
            }
        }
    })
}

function deleteTask(task_id) {
    var data = {};
    data["mID"] = task_id;
    data["uuid"] = Cookies.get("uuid");
    data = JSON.stringify(data);

    $.ajax({
        type: "POST",
        url: server_address_deleteTask,
        data: data,
        contentType: "application/json",
        success: function(response) {
            if (response["response"] == "deleted task") {
                logkitty("Successfully deleted task", "success");
                $('#' + task_id).remove();
            } else {
                logkitty("Failed at deleted task", "error");
            }
        }
    })
}

function upgradeTask(task_id) {
    var data = {};
    data["mID"] = task_id;
    data["uuid"] = Cookies.get('uuid');
    data = JSON.stringify(data);

    $.ajax({
        type: "POST",
        url: server_address_upgradeTask,
        data: data,
        contentType: "application/json",
        success: function(response) {
            if (response["response"] == "Successfully upgraded task") {
                logkitty("Successfully upgraded task", "success");
                refreshTaskList();
            } else {
                logkitty("Failed at upgrading task", "error");
            }
        }
    })
}

function refreshTaskList() {
    logkitty("Refreshing tasks", "normal");

    $('.todo_div').empty();
    $('.todo_div').text("To Do");
    $('.in_progress_div').empty();
    $('.in_progress_div').text("To Do");
    $('.done_div').empty();
    $('.done_div').text("Done");
    getTasks();
}

function addTask() {
    var data = {};
    data["mTask"] = $('#add_task_title').val();
    data["mDescription"] = $('#add_task_description').val();
    data["mAssignee"] = $('#add_task_assignee').val();
    data["mDueDate"] = $('#add_task_due_date').val();
    data["uuid"] = Cookies.get("uuid");
    data["mOwner"] = "personal";
    data = JSON.stringify(data);

    $('#add_task_title').val("");
    $('#add_task_description').val("");
    $('#add_task_assignee').val("");
    $('#add_task_due_date').val("");

    $.ajax({
        type: "POST",
        url: server_address_newTask,
        data: data,
        contentType: "application/json",
        success: function(response) {
            if (response["status"] == "success") {
                $('#modal_add_task').closeModal();
                logkitty("Successfully added a new task", "success");
                refreshTaskList();
            } else {
                logkitty("Failed at adding a new task", "error");
            }
        }
    })
}

function refreshTask(task_id) {
    var data = {};
    data["mID"] = task_id;
    data["uuid"] = Cookies.get("uuid");
    data = JSON.stringify(data);

    $.ajax({
        type: "POST",
        url: server_address_refreshTask,
        data: data,
        contentType: "application/json",
        success: function(response) {
            if (response["status"] == "success") {
                logkitty("Successfully got task data from refreshTask", "success");
                var mTask = response["tasks"]["mTask"];
                var mDescription = response["tasks"]["mDescription"];
                var mAssignee = response["tasks"]["mAssignee"];
                var mDueDate = response["tasks"]["mDueDate"];
                var mID = response["tasks"]["mID"];
                var mTaskStatus = response["tasks"]["mTaskStatus"];

                $('#' + task_id + 'Title').text(mTask);
                $('#' + task_id + 'Info').text(mDescription + "<br/>" + mAssignee + "<br/>" + mDueDate)

                $('#edit_task_title').val(mTask);
                $('#edit_task_assignee').val(mAssignee);
                $('#edit_task_due_date').val(mDueDate);
                $('#edit_task_description').val(mDescription);

                logkitty("Successfully changed the edit task modal values", "success");
            } else {
                logkitty("Got " + response["status"] + " from refreshTask", "error");
            }
        }
    })
}

function editTask(task_id) {
    task_being_modified = task_id;
    refreshTask(task_id);
    $('#modal_edit_task').openModal({
        dismissible: false
    })
}

function saveModifiedTask() {
    var data = {};
    data["mID"] = task_being_modified;
    data["mTask"] = $('#edit_task_title').val();
    data["mDescription"] = $('#edit_task_description').val();
    data["mAssignee"] = $('#edit_task_assignee').val();
    data["mDueDate"] = $('#edit_task_due_date').val();
    data["uuid"] = Cookies.get("uuid");
    data = JSON.stringify(data);

    $('#edit_task_title').val("");
    $('#edit_task_description').val("");
    $('#edit_task_assignee').val("");
    $('#edit_task_due_date').val("");

    $.ajax({
        type: "POST",
        url: server_address_updateTask,
        data: data,
        contentType: "application/json",
        success: function(response) {
            if (response["response"] == "success") {
                $('#modal_edit_task').closeModal();
                logkitty("Successfully edited a task!", "success");
                refreshTaskList();
            } else {
                logkitty("Failed at editing a task", "error");
                logkitty("Just going to awkwardly close the modal now...", "error");
                $('#modal_edit_task').closeModal();
            }
        }
    })
}