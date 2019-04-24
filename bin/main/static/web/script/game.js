var app = new Vue({
                       el: "#app",
                          data: {
                              data: [],
                              teste: "Hola mundo",
                              numeros: [1,2,3,4,5,6,7,8,9,10],
                              letras: ['A', 'B','C','D','E','F','G','H','I','J'],
                              gamep: '',
                              location:[],
                              td:[],
                              ids:[],
                              userNames:[],
                              salvos:[],
                              salvosGp2:[],
                              dataSalvo:[],
                              oponentShips: [],
                              intersection: [],
                              water:[]
                              
                          },
                          created: function(){


                            this.getData();
                            



                          },
                          methods:{

                          changeBackground: function(event){




                          },

                          getData: function(){
                            //Pego o elemento da url que estou querendo.
                         this.gamep = new URLSearchParams(location.search).get("gp")
                         console.log(this.gamep);



                         fetch('http://localhost:8080//api/game_view/'+this.gamep ,{
                         		method:"GET",

                         }).then(function(response){
                         		if (response.ok){
                         	return response.json();
                         }

                         }).then(function(json){
                         	app.data = json;
                           console.log(app.data)
                           console.log(app.data.gameplayer[0].player.userName);

                          //Sacando las posiciones:
                          app.location= app.data.Ships.flatMap(ship => ship.locations); 
                          console.log(app.location);
                          app.location.forEach(id => {
                            let elemento = document.getElementById(id);
                            elemento.style.background = 'blue';
                          });
                          //Sacando los Ids:
                          app.td = Array.from(document.querySelectorAll('td'));
                          console.log(app.td);
                          app.ids = app.td.map(ids => ids.id);
                          console.log(app.ids);

                          app.userNames = app.data.gameplayer.flatMap(user => user.player.userName);
                          console.log(app.userNames);
                          app.oponentShips =  app.data.oponentShips.flatMap(oponent => oponent.salvos);
                          console.log(app.oponentShips);

                          //Aqui estamos verificando a intersecao e pintando os tiros que acertaram o alvo.
                          app.intersection = app.location.filter(position => app.oponentShips.includes(position));
                          app.intersection.forEach(target =>{
                            let arrow = document.getElementById(target);
                            arrow.style.background = 'red';
                          })
                          console.log(app.location);
                          console.log(app.intersection);

                          //Pintando os tiros que deram na agua de azul:
                          app.water = app.oponentShips.filter(agua => !app.intersection.includes(agua));
                          console.log(app.water);

                          app.water.forEach(id =>{
                            let missedShot = document.getElementById(id);
                            missedShot.style.background = '#81ecec';
                          })



                          //Sacando os salvos e os colocando um array para compararlos.
                          app.salvos = app.data.salvos.flatMap(salvo => salvo.salvos);
                           

                         }).catch(function (error){
                         	 console.log("Request failed:" + error.message);
                         });


                          }

                          }




                  })