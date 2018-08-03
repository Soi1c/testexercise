function login() {
	var url = serverAddress + "/login";
	var data = '{ "email":"' + document.getElementById('email').value +
		'", "password":"' + document.getElementById('password').value + '" }';
	var xhttp = new XMLHttpRequest();
	xhttp.open('POST', url, false);
	xhttp.withCredentials = true;
	xhttp.onload = function () {
		console.log(xhttp.responseText);
	};
	xhttp.send(data);
}