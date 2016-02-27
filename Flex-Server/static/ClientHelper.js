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
                logcat(response["response"], "success");
                logcat("It's an older uuid sir, but it checks out", "success");
                valid_uuid = true;
            } else {
                logcat(response["response"], "error");
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
            if (response["response"] == "logged in") {
                Cookies.set("uuid", response["data"]);
                logcat("Successfully logged in", "success");
                $('#modal_log_in').closeModal();
            } else if (response["response"] == "log in failed") {
                logcat("Failed to log in", "error");
                Cookies.set("uuid", response["data"]);
            }
        }
    })
}