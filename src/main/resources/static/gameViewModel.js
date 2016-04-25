function GameViewModel() {
    var self = this;
    var data = {state: 'UNSTARTED', countdown: 0, players: []};

    self.state = ko.observable(data.state);
    self.countdown = ko.observable(data.countdown);
    self.players = ko.observableArray(data.players.map(function (obj) {
      return new UserInfo({userName: obj.userName, userType: obj.userType});
    }));

    self.starting = ko.computed(function() {
      return self.state() == 'STARTING';
    });

    self.startingMsg = ko.computed(function() {
      return 'Game starting in ' + self.countdown();
    });
  }

  function UserInfo(data) {
    this.userName = ko.observable(data.userName);
    this.userType = ko.observable(data.userType);
  }