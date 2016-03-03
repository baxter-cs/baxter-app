$(document).ready(main)

// Server Addresses
var flex_server_address = "http://192.168.0.8:7999";
var BAuth_server_address = "http://192.168.0.8:1754";

var server_address_login = BAuth_server_address + "/login";

var server_address_verifyLogin = flex_server_address + "/verifyLogin";
var server_address_getTasks = flex_server_address + "/getTasks";
var server_address_deleteTask = flex_server_address + "/deleteTask";
var server_address_upgradeTask = flex_server_address + "/upgradeTask";
var server_address_newTask = flex_server_address + "/newTask";
var server_address_refreshTask = flex_server_address + "/refreshTask";
var server_address_updateTask = flex_server_address + "/updateTask";


// Dev Options
var logkitty_enabled = true;

//
var valid_uuid = false;
var task_being_modified = 0;


function main() {
    // Initialize Buttons
    $('#button_log_in').click(function() {
        logIn();
    })

    $('#logOutButton').click(function() {
        Cookies.set("uuid", "invalid");
        location.reload();
    })

    $('#addTaskButton').click(function() {
        $('#modal_add_task').openModal({
            dismissible: false
        })
    })

    $('#button_add_new_task').click(function() {
        addTask();
    })

    $('#button_save_edit_task').click(function() {
        saveModifiedTask();
    })

    $('body').on('click', 'input.deleteTaskButton', function(event) {
        taskID = $(this).parent().attr('id');
        logkitty("Trying to delete task #" + taskID, "normal");
        deleteTask(taskID);
    })

    $('body').on('click', 'input.upgradeTaskButton', function(event) {
        taskID = $(this).parent().attr('id');
        logkitty("Trying to upgrade the status of task #" + taskID, "normal");
        upgradeTask(taskID);
    })

    $('body').on('click', 'input.editTaskButton', function(event) {
        taskID = $(this).parent().attr('id');
        logkitty("Trying to edit task #" + taskID, "normal");
        editTask(taskID);
    })

    // Log In
    CH_verifyLogin();

}

function logkitty(message, type) {
    if (logkitty_enabled) {
        switch (type) {
            case "error":
                console.log("%clogkitty: " + message, "color: red;");
                break;
            case "success":
                console.log("%clogkitty: " + message, "color: green;");
                break;
            default:
                console.log("%clogkitty: " + message, "color: black;");
                break;
        }
    }
}

function addTodoTask(task_object) {
    mTask = task_object["mTask"];
    mDescription = task_object["mDescription"];
    mAssignee = task_object["mAssignee"];
    mDueDate = task_object["mDueDate"];
    mTaskStatus = task_object["TaskStatus"];
    mID = task_object["mID"];

    $('.todo_div').append('<div id="' + mID + '"class="task_div"></div>');
    $('#' + mID).append('<p id="' + mID + 'Title" class="task_bgTitle_todo task_title_div">' + mTask + '</p>');
    $('#' + mID).append('<p id="' + mID + 'Info" class="task_body_div task_bgBody_todo">' + mDescription + "<br/>" + mAssignee + "<br/>" + mDueDate + '</p>');
    $('#' + mID).append('<input type="button" value="Delete Task" class="deleteTaskButton">')
    $('#' + mID).append('<input type="button" value="Move to In Process" class="upgradeTaskButton">')
    $('#' + mID).append('<input type="button" value="Edit Task" class="editTaskButton">')
}

function addInProgressTask(task_object) {
    mTask = task_object["mTask"];
    mDescription = task_object["mDescription"];
    mAssignee = task_object["mAssignee"];
    mDueDate = task_object["mDueDate"];
    mTaskStatus = task_object["TaskStatus"];
    mID = task_object["mID"];

    $('.in_progress_div').append('<div id="' + mID + '"class="task_div"></div>');
    $('#' + mID).append('<p id="' + mID + 'Title" class="task_bgTitle_doing task_title_div">' + mTask + '</p>');
    $('#' + mID).append('<p id="' + mID + 'Info" class="task_body_div task_bgBody_doing">' + mDescription + "<br/>" + mAssignee + "<br/>" + mDueDate + '</p>');
    $('#' + mID).append('<input type="button" value="Delete Task" class="deleteTaskButton">')
    $('#' + mID).append('<input type="button" value="Move to Done" class="upgradeTaskButton">')
    $('#' + mID).append('<input type="button" value="Edit Task" class="editTaskButton">')
}

function addDoneTask(task_object) {
    mTask = task_object["mTask"];
    mDescription = task_object["mDescription"];
    mAssignee = task_object["mAssignee"];
    mDueDate = task_object["mDueDate"];
    mTaskStatus = task_object["TaskStatus"];
    mID = task_object["mID"];

    $('.done_div').append('<div id="' + mID + '"class="task_div"></div>');
    $('#' + mID).append('<p id="' + mID + 'Title" class="task_bgTitle_done task_title_div">' + mTask + '</p>');
    $('#' + mID).append('<p id="' + mID + 'Info" class="task_body_div task_bgBody_done">' + mDescription + "<br/>" + mAssignee + "<br/>" + mDueDate + '</p>');
    $('#' + mID).append('<input type="button" value="Delete Task" class="deleteTaskButton">')
    $('#' + mID).append('<input type="button" value="Edit Task" class="editTaskButton">')
}