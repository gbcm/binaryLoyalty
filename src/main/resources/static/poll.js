(function(window) {
    var ready = false;

    var http = new XMLHttpRequest();
    var document = window.document,
        $ = document.querySelector.bind(document);

    window.bootLand = function () {

    };

    window.onload = function () {
        var this_js_script = $('script[src*=poll]');
        var user_type = this_js_script.getAttribute('data-user_type');

        if (user_type === "SYSTEM" ) {
            ready = true;
        }

        setInterval(function ()
        {
            var gameCode =$('#game_code').innerText;
            var url = '/api/game/' + gameCode;

            http.open('GET', url, true);
            http.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');

            http.onreadystatechange = function () {
                if(http.readyState == 4 && http.status == 200) {
                    var game = JSON.parse(http.responseText);

                    if (game.state == 'STARTING') {
                        $("#game_status").innerText = 'Game starting in ' + game.countdown;
                        $("#start_button").style.display = 'none';

                        if (!ready) {
                            $("#ready_prompt").style.display = 'block';
                        }
                    } else if (game.state == 'STARTED') {
                        $("#game_status").innerText = '';

                        if (!ready) {
                            window.location.replace('/');
                        }
                    }
                }
            };
            http.send();

        }, 500);
    };

    window.startGame = function () {
        ready = true;
        $("#start_button").style.display = 'none';
        $("#game_status").innerText = "Game starting in 15";

        var gameCode =$('#game_code').innerText;
        var url = '/api/startGame/' + gameCode;

        http.open('POST', url, true);
        http.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
        http.send();
    };

    window.setReady = function () {
        $("#ready_prompt").style.display = 'none';
        ready = true;
    };

    window.leaveGame = function () {
        window.location.replace('/');
    };

})(window);