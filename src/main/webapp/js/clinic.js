function validateForm(form) {
    var isValid = true;
    form = $(form);
    var errorElement = initErrorElement();

    form.find(":input").each(function(index, element) {
        element = $(element);

        if (element.val() === "") {
            if (element.attr("name") === "name" || element.attr("name") === "newName") {
                invalidField(errorElement, element, "Name is empty.");
                isValid = false;
            } else if (element.attr("name") === "email" || element.attr("name") === "newEmail") {
                invalidField(errorElement, element, "Email is empty.");
                isValid = false;
            } else if (element.attr("name") === "phone" || element.attr("name") === "newPhone") {
                invalidField(errorElement, element, "Phone is empty.");
                isValid = false;
            } else if (element.attr("name") === "petName") {
                invalidField(errorElement, element, "Pet name is empty.");
                isValid = false;
            } else if (element.attr("name") === "clientName") {
                invalidField(errorElement, element, "Client name is empty.");
                isValid = false;
            } else if (element.attr("name") === "petAge" || element.attr("name") === "newAge") {
                invalidField(errorElement, element, "Pet age is empty.");
                isValid = false;
            } else if (element.attr("name") === "text") {
                invalidField(errorElement, element, "Text is empty.");
                isValid = false;
            }
        }

        if (element.attr("name") === "petAge" || element.attr("name") === "newAge") {
            if (element.val() !== "" && isNaN(parseInt(element.val())) || element.val() < 0) {
                invalidField(errorElement, element, "Pet age is wrong.");
                isValid = false;
            }
        }
    });

    if (!validatePetSex(form, errorElement, "petSex")) {
        isValid = false;
    }
    if (!validatePetSex(form, errorElement, "newSex")) {
        isValid = false;
    }

    return isValid;
}

function validatePetSex(form, errorElement, name) {
    var isValid = true;
    if (form.find("input[name=" + name + "]").length > 0 &&
        form.find("input[name=" + name + "]:checked").length === 0) {

        invalidField(errorElement, $("#" + name), "Pet sex is not defined.");
        isValid = false;
    }
    return isValid;
}

function initErrorElement() {
    var errorElement = $(".error");
    if (errorElement.length === 0) {
        errorElement = $("<p class='error'></p>");
        errorElement.insertAfter($('h2'));
    }
    errorElement.empty();
    return errorElement;
}

function invalidField(errorElement, element, message) {
    errorElement.append("<p>" + message + "</p>");

    var initialColor = element.css("backgroundColor");
    element
        .animate({ backgroundColor: "#8b0000" }, 350)
        .animate({ backgroundColor: initialColor }, 350);
}

function deleteClientPet(name) {
    if (!confirm("Really delete this client's pet?")) return;
    submitForm({ name: name }, "client/pet/delete?name=" + name);
}

function deleteClient(name) {
    if (!confirm("Really delete this client?")) return;
    submitForm({ name: name }, "client/delete?name=" + name);
}

function deleteRole(name) {
    if (!confirm("Really delete this role?")) return;
    submitForm({ name: name }, "role/delete?name=" + name);
}

function deleteMessage(name, id) {
    if (!confirm("Really delete this message?")) return;
    submitForm({ name: name, id: id }, "message/delete?name=" + name);
}

function submitForm(params, action, method) {
    var form = document.createElement('form');

    form.setAttribute("method", method || "post");
    if (action) {
        form.setAttribute("action", action);
    }

    for (var name in params) {
        if (!params.hasOwnProperty(name)) {
            continue;
        }
        var element = document.createElement("input");
        element.setAttribute("type", "hidden");
        element.setAttribute("name", name);
        element.setAttribute("value", params[name]);
        form.appendChild(element);
    }

    document.body.appendChild(form);
    form.submit();
}
