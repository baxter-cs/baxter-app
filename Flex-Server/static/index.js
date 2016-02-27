$(document).ready(main)

var server_address = "http://192.168.0.13:7999";
var server_address_verifyLogin = server_address + "/verifyLogin";
var server_address_login = server_address + "/login";
var logcat_enabled = true;
var valid_uuid = false;
var log_in_modal_open = false;


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