function validateForm(form) {
    var isValid = true;
    form = $(form);
    var errorElement = initErrorElement(form);

    form.find(":input").each(function(index, element) {
        element = $(element);
        if (element.attr("type") === "text" && element.val() === "") {
            if (element.attr("name") === "name" || element.attr("name") === "newName") {
                invalidField(errorElement, element, "Name is empty.");
                isValid = false;
            } else if  (element.attr("name") === "petName") {
                invalidField(errorElement, element, "Pet name is empty.");
                isValid = false;
            }
        }
    });

    return isValid;
}

function initErrorElement(form) {
    var errorElement = $(".error");
    if (errorElement.length === 0) {
        errorElement = $("<p class='error'></p>");
        errorElement.insertBefore(form);
    }
    return errorElement;
}

function invalidField(errorElement, element, message) {
    errorElement.text(message);

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
