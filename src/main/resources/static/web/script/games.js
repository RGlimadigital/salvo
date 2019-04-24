let app = new Vue({
  el: "#app",
  data: {
    data: [],
    teste: "Hola mundo",
    emailList: [],
    tHead: ["Name:", "Total:", "Won:", "Lost:", "Tied:"],
    dataScore: [],
    entrar: true,
    player: true,
    newGameList: [],
    showGames: [],
    loginPlayerId: [],

  },
  created: function () {
    this.getData();
  },

  methods: {
    logout: function () {
      fetch("http://localhost:8080/api/logout", {
          method: "POST"
        })
        .then(function (response) {
          if (response.ok) {
            window.location.reload();
          }
        })
        .catch(function (error) {
          console.log("Request failed:" + error.message);
        });
    },

    createGame: function () {
      fetch("http://localhost:8080/api/games", {
          method: "POST",
        })
        .then(function (response) {
          if (response.ok) {
            console.log("singUP: O fetch");
            window.location.href = `http://localhost:8080/web/games.html`

          }
        })
        .catch(function (error) {
          console.log("Request failed:" + error.message);
        });

    },



    singUp: function () {
      if (
        /^(([^<>()[\]\.,;:\s@\"]+(\.[^<>()[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i.test(
          document.querySelector("#usuario").value
        )
      ) {
        const playerToRegister = {
          userName: document.querySelector("#usuario").value,
          password: document.querySelector("#password").value
        };


        fetch("http://localhost:8080/api/players", {
            method: "POST",
            headers: {
              'Accept': 'application/json',
              'Content-Type': 'application/json'
            },
            body: JSON.stringify(playerToRegister)
          })
          .then(function (response) {
            if (response.ok) {
              console.log("singUP: O fetch");
              app.login();
            }
          })
          .catch(function (error) {
            console.log("Request failed:" + error.message);
          });
      } else {
        alert("La dirección de email es incorrecta!.");
      }
    },

    login: function () {
      if (
        /^(([^<>()[\]\.,;:\s@\"]+(\.[^<>()[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i.test(
          document.querySelector("#usuario").value
        )
      ) {
        let user = document.querySelector("#usuario").value;
        let pwd = document.querySelector("#password").value;
        const userPass = "user=" + user + "&pwd=" + pwd;

        fetch("http://localhost:8080/api/login", {
            method: "POST",
            headers: {
              "Content-Type": "application/x-www-form-urlencoded"
            },
            body: userPass
          })
          .then(function (response) {
            if (response.ok) {
              console.log("O login");
              window.location.reload(true);
            }
          })
          .catch(function (error) {
            console.log("Request failed:" + error.message);
          });
      } else {
        alert("La dirección de email es incorrecta!.");
      }
    },
    userTable: function () {},
    jGame: function (game) {
      const gamePlayerId = game.gamePlayers.find(gp => gp.player.id === app.data.player.id).id;
      window.location.href = `http://localhost:8080/web/game.html?gp=${gamePlayerId}`

      console.log(game.gamePlayers.find(gp => gp.player.id === app.data.player.id).id);

    },

    getData: function () {
      fetch("http://localhost:8080/api/games", {
          method: "GET"
        })
        .then(function (response) {
          if (response.ok) {
            return response.json();
          }
        })
        .then(function (json) {
          app.data = json;
          if (!app.data.player) {
            app.showGames = app.data.games;
            console.log(app.showGames)
          } else {
            app.showGames = app.data.games.filter(game =>
              game.gamePlayers.some(gp => gp.player.id === app.data.player.id)


            );
            console.log(app.showGames);
          }
        })
        .catch(function (error) {
          console.log("Request failed:" + error.message);
        });

      fetch("http://localhost:8080/api/leaderboard", {
          method: "GET"
        })
        .then(function (response) {
          if (response.ok) {
            return response.json();
          }
        })
        .then(function (json) {
          //funcao para colocar em ordem a array.
          //Aqui estamos comparando keys, no caso total.
          app.dataScore = json.sort((a, b) => b.total - a.total);
          console.log(app.dataScore);

          console.log(app.dataScore);
        })
        .catch(function (error) {
          console.log("Request failed:" + error.message);
        });
    }
  }
});