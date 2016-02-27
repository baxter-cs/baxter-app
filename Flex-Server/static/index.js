$(document).ready(main)

// Server Addresses
var server_address = "http://192.168.0.13:7999";
var server_address_verifyLogin = server_address + "/verifyLogin";
var server_address_login = server_address + "/login";
var server_address_getTasks = server_address + "/getTasks";

// Dev Options
var logcat_enabled = true;

//
var valid_uuid = false;


function main() {
    // Initialize Buttons
    $('#button_log_in').click(function() {
        logIn();
    })

    // Log In
    CH_verifyLogin();

}

function logcat(message, type) {
    if (logcat_enabled) {
        switch (type) {
            case "error":
                console.log("%cLogcat: " + message, "color: red;");
                break;
            case "success":
                console.log("%cLogcat: " + message, "color: green;");
                break;
            default:
                console.log("%cLogcat: " + message, "color: black;");
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
}