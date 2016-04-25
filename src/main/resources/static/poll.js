(function(window) {
    var http = new XMLHttpRequest();
    var document = window.document,
        $ = document.querySelector.bind(document);

    var this_js_script = $('script[src*=poll]');
    var isSystemUser = this_js_script.getAttribute('data-user_type') === "SYSTEM";
    var gameJson = this_js_script.getAttribute('data-game_json');

    var viewModel = ko.viewmodel.fromModel(JSON.parse(gameJson), {
        arrayChildId:{
            "{root}.players":"userName"
        },

        extend:{
            "{root}": function(model) {
                model.starting = ko.computed(function() {
                    return model.state() === 'STARTING';
                });
                model.unstarted = ko.computed(function() {
                    return model.state() === 'UNSTARTED';
                });
                model.startingMsg = ko.computed(function() {
                    return 'Game starting in ' + model.countdown();
                });

                model.ready = ko.observable(isSystemUser);
            }
        }
    });

    window.onload = function () {
        ko.applyBindings(viewModel);

        function pollGameStatus() {
            http.open('GET', '/api/game/' + viewModel.gameCode(), true);
            http.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');

            http.onreadystatechange = function () {
                if (http.readyState == 4 && http.status == 200 && http.responseText != '') {
                    var gameObj = JSON.parse(http.responseText);

                    if (gameObj.state == 'STARTED' && !viewModel.ready()) {
                        window.location.replace('/');
                    }

                    ko.viewmodel.updateFromModel(viewModel, gameObj);
                }
            };
            http.send();

            setTimeout(pollGameStatus, 500);
        }

        pollGameStatus();
    };

    window.startGame = function () {
        viewModel.ready(true);

        http.open('POST', '/api/startGame/' + viewModel.gameCode(), true);
        http.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
        http.send();
    };

    window.setReady = function () {
        viewModel.ready(true);
    };

    window.leaveGame = function () {
        window.location.replace('/');
    };

})(window);