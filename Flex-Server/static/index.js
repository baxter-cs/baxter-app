$(document).ready(main)

// Server Addresses
var server_address = "http://192.168.0.13:7999";
var server_address_verifyLogin = server_address + "/verifyLogin";
var server_address_login = server_address + "/login";
var server_address_getTasks = server_address + "/getTasks";
var server_address_deleteTask = server_address + "/deleteTask";
var server_address_upgradeTask = server_address + "/upgradeTask";
var server_address_newTask = server_address + "/newTask";


// Dev Options
var logkitty_enabled = true;

//
var valid_uuid = false;


function main() {
    // Initialize Buttons
    $('#button_log_in').click(function() {
        logIn();
    })

    $('#addTaskButton').click(function() {
        $('#modal_add_task').openModal({
            dismissible: false
        })
    })

    $('#button_add_new_task').click(function() {
        addTask();
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
    $('#' + mID).append('<p class="task_bgTitle_todo task_title_div">' + mTask + '</p>');
    $('#' + mID).append('<p class="task_body_div task_bgBody_todo">' + mDescription + "<br/>" + mAssignee + "<br/>" + mDueDate + '</p>');
    $('#' + mID).append('<input type="button" value="Delete Task" class="deleteTaskButton">')
    $('#' + mID).append('<input type="button" value="Move to In Process" class="upgradeTaskButton">')
}

function addInProgressTask(task_object) {
    mTask = task_object["mTask"];
    mDescription = task_object["mDescription"];
    mAssignee = task_object["mAssignee"];
    mDueDate = task_object["mDueDate"];
    mTaskStatus = task_object["TaskStatus"];
    mID = task_object["mID"];

    $('.in_progress_div').append('<div id="' + mID + '"class="task_div"></div>');
    $('#' + mID).append('<p class="task_bgTitle_doing task_title_div">' + mTask + '</p>');
    $('#' + mID).append('<p class="task_body_div task_bgBody_doing">' + mDescription + "<br/>" + mAssignee + "<br/>" + mDueDate + '</p>');
    $('#' + mID).append('<input type="button" value="Delete Task" class="deleteTaskButton">')
    $('#' + mID).append('<input type="button" value="Move to Done" class="upgradeTaskButton">')
}

function addDoneTask(task_object) {
    mTask = task_object["mTask"];
    mDescription = task_object["mDescription"];
    mAssignee = task_object["mAssignee"];
    mDueDate = task_object["mDueDate"];
    mTaskStatus = task_object["TaskStatus"];
    mID = task_object["mID"];

    $('.done_div').append('<div id="' + mID + '"class="task_div"></div>');
    $('#' + mID).append('<p class="task_bgTitle_done task_title_div">' + mTask + '</p>');
    $('#' + mID).append('<p class="task_body_div task_bgBody_done">' + mDescription + "<br/>" + mAssignee + "<br/>" + mDueDate + '</p>');
    $('#' + mID).append('<input type="button" value="Delete Task" class="deleteTaskButton">')
}