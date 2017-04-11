function deleteClientPet(name, contextPath) {
    if (!confirm("Really delete this client's pet?")) return;
    submitForm({ name: name }, contextPath + "/client/pet/delete?name=" + name);
}

function deleteClient(name, contextPath) {
    if (!confirm("Really delete this client?")) return;
    submitForm({ name: name }, contextPath + "/client/delete?name=" + name);
}

function submitForm(params, action) {
    var form = document.createElement('form');

    form.setAttribute("method", "post");
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
