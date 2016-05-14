/* global FB */

window.fbAsyncInit = function () {
    FB.init({
        appId: '576632675844236',
        xfbml: true,
        version: 'v2.6'
    });
    FB.getLoginStatus(function (response) {
        if (response.status === 'connected') {
            document.getElementById('status').innerHTML = 'We are connected.';

        } else if (response.status === 'not_authorized') {
            document.getElementById('status').innerHTML = 'We are not logged in.';
        } else {
            document.getElementById('status').innerHTML = 'You are not logged into Facebook.';
        }
    });
};
(function (d, s, id) {
    var js, fjs = d.getElementsByTagName(s)[0];
    if (d.getElementById(id)) {
        return;
    }
    js = d.createElement(s);
    js.id = id;
    js.src = "//connect.facebook.net/en_US/sdk.js";
    fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));

// login with facebook with extra permissions
function login() {
    FB.login(function (response) {
        if (response.status === 'connected') {
            document.getElementById('status').innerHTML = 'We are connected.';
            FB.api('/me', 'GET', {fields: 'first_name,last_name,name,id,email'}, function (response) {
                document.getElementById('status').innerHTML = 'Registacija su Facebook paskyra sekminga!';
                myremote([{name:'email', value:response.email},{name:'first_name', value:response.first_name},{name:'last_name', value:response.last_name}]);
            });
        } else if (response.status === 'not_authorized') {
            document.getElementById('status').innerHTML = 'We are not logged in.';
        } else {
            document.getElementById('status').innerHTML = 'You are not logged into Facebook.';
        }
    }, {scope: 'email'});
    
}

// getting basic user info
function getInfo() {
    login();
    FB.api('/me', 'GET', {fields: 'first_name,last_name,name,id,email'}, function (response) {
        document.getElementById('status').innerHTML = response.first_name + response.last_name + response.email;
        myremote([{name:'email', value:response.email}]);
    });
    
}


