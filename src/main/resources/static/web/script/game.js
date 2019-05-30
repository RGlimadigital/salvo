var app = new Vue({
	el: '#app',
	data: {
		data: [],
		teste: 'Hola mundo',
		numeros: [ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 ],
		letras: [ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J' ],
		gamep: '',
		location: [],
		tags: [],
		ids: [],
		userNames: [],
		salvos: [],
		salvosGp2: [],
		dataSalvo: [],
		oponentShips: [],
		intersection: [],
		water: [],
		listaNum: [ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 ],
		fullCells: [],
		valor: '',
		direction: '',
		zoo: new Map([
			[ 'A', 1 ],
			[ 'B', 2 ],
			[ 'C', 3 ],
			[ 'D', 4 ],
			[ 'E', 5 ],
			[ 'F', 6 ],
			[ 'G', 7 ],
			[ 'H', 8 ],
			[ 'I', 9 ],
			[ 'J', 10 ]
		]),
		nova: [],
		ship: []
	},
	created: function() {
		this.getData();
	},
	methods: {
		//Tirei o changeBackGround e coloquei o makeClick
		makeClick(size) {
			this.valor = event.target.getAttribute('value');
			this.direction = event.target.getAttribute('direction');
			console.log(`Direction : ${this.direction}`);
			console.log(`value: ${this.valor}`);
			this.removeHover();
			this.tags.forEach((div) => div.setAttribute('data-size', size));
			this.makeHover();
			this.makeAclick();
		},

		makeHover() {
			[ 'mouseenter', 'mouseleave' ].forEach((evento) =>
				this.tags.forEach((div) => div.addEventListener(evento, this))
			);
		},

		removeHover() {
			[ 'mouseenter', 'mouseleave' ].forEach((evento) =>
				this.tags.forEach((div) => div.removeEventListener(evento, this))
			);
		},
		makeAclick() {
			[ 'mouseenter', 'mouseleave', 'click' ].forEach((evento) =>
				this.tags.forEach((div) => div.addEventListener(evento, this))
			);
		},
		removeClick() {
			[ 'mouseenter', 'mouseleave', 'click' ].forEach((evento) =>
				this.tags.forEach((div) => div.removeEventListener(evento, this))
			);
		},
		removeClick1() {
			[ 'click' ].forEach((evento) => this.tags.forEach((div) => div.removeEventListener(evento, this)));
		},

		handleEvent(evt) {
			let size = Number(event.target.getAttribute('data-size'));

			switch (event.type) {
				case 'mouseenter':
					this.completeShips(size, event.target, event.type);
					break;
				case 'mouseleave':
					this.completeShips(size, event.target, event.type);
					break;
				case 'click':
					this.fixeShips(size, event.target, event.type);
					break;
			}
		},

		completeShips(size, div, evento) {
			let id = div.id;
			let number = Number(id.substring(1));
			let letter = id.substring(0, 1);
			let limit = [];

			if (this.direction == 'horizontal') {
				11 - size >= number
					? (limit = this.numeros.slice(number - 1, number - 1 + size))
					: (limit = this.numeros.slice(-size));
				this.nova = limit.map((e) => letter + e);
			} else {
				11 - size >= this.zoo.get(letter)
					? (limit = this.letras.slice(this.zoo.get(letter) - 1, this.zoo.get(letter) - 1 + size))
					: (limit = this.letras.slice(-size));
				this.nova = limit.map((e) => e + number); //aqui temos um detalhe quando transladamos pra vertical.
			}

			this.nova.forEach((el) =>
				this.fullCells.forEach((full) => {
					if (el == full) {
						if (evento === 'mouseenter') {
							this.removeClick1();
							this.nova.forEach((el) => document.getElementById(el).classList.add('grey'));
						} else {
							this.makeAclick();
							this.nova.forEach((el) => document.getElementById(el).classList.remove('grey'));
						}
					}
				})
			);

			evento === 'mouseenter'
				? this.nova.forEach((el) => document.getElementById(el).classList.add('blue'))
				: this.nova.forEach((el) => document.getElementById(el).classList.remove('blue'));
		},

		fixeShips(size, div, evento) {
			let id = div.id;
			let number = Number(id.substring(1));
			let letter = id.substring(0, 1);
			let limit = [];

			console.log(`fixeShips ${this.direction}`);

			if (this.direction == 'horizontal') {
				11 - size >= number
					? (limit = this.numeros.slice(number - 1, number - 1 + size))
					: (limit = this.numeros.slice(-size));
				this.nova = limit.map((e) => letter + e);
			} else {
				11 - size >= this.zoo.get(letter)
					? (limit = this.letras.slice(this.zoo.get(letter) - 1, this.zoo.get(letter) - 1 + size))
					: (limit = this.letras.slice(-size));
				this.nova = limit.map((e) => e + number); //aqui temos um detalhe quando transladamos pra vertical.
			}

			this.nova.forEach((e) => this.fullCells.push(e));
			this.ship.push({ location: this.nova, type: this.valor });
			console.log(`Contador de navios: ${this.ship.length}`);
			this.ship.length >= 5 ? (btSend.style.display = 'block') : null;

			console.log(this.ship);
			console.log(`fullCels: ${this.fullCells}`);

			if (evento === 'click') {
				this.nova.forEach((el) => document.getElementById(el).classList.add(this.valor));
				this.removeClick();
				console.log(event.target);
				document.getElementById(this.valor).disabled = true;
				return;
			} else {
				this.nova.forEach((el) => document.getElementById(el).classList.remove('blue'));
			}
		},

		newBack: function() {
			event.target.classList.remove('mouseOver');
		},

		sendSalvo: function() {
			const gamePlayerId = new URLSearchParams(location.search).get('gp');

			fetch(`/api/games/players/${gamePlayerId}/ships`, {
				method: 'POST',
				credentials: 'include',
				headers: {
					Accept: 'application/json',
					'Content-Type': 'application/json'
				},
				body: JSON.stringify(this.ship)
			})
				.then(function(response) {
					if (response.ok) {
						console.log('O Salvo');
						window.location.reload(true);
					}
				})
				.catch(function(error) {
					console.log('Request failed:' + error.message);
				});
		},

		getData: function() {
			//Pego o elemento da url que estou querendo.
			this.gamep = new URLSearchParams(location.search).get('gp');
			console.log(this.gamep);

			fetch('http://localhost:8080/api/game_view/' + this.gamep, {
				method: 'GET'
			})
				.then(function(response) {
					if (response.ok) {
						return response.json();
					}
				})
				.then(function(json) {
					app.data = json;
					console.log(app.data);
					console.log(app.data.gameplayer[0].player.id);
					let chBox = document.getElementById('chBox');
					let chBoxVert = document.getElementById('chBoxVert');
					let btSend = document.getElementById('btSend');
					btSend.style.display = 'none';

					chBoxVert.addEventListener('change', function(event) {
						let btns = document.querySelectorAll('.btn');
						btns.forEach((e) => e.setAttribute('direction', 'vertical'));
						chBox.checked = false;
					});
					chBox.addEventListener('change', function(event) {
						let btns = document.querySelectorAll('.btn');
						btns.forEach((e) => e.setAttribute('direction', 'horizontal'));
						chBoxVert.checked = false;
					});

					//Sacando las posiciones:
					app.location = app.data.Ships.flatMap((ship) => ship.locations);
					console.log(app.location);
					app.location.forEach((id) => {
						let elemento = document.getElementById(id);
						elemento.style.background = 'blue';
					});

					//Sacando los Ids:
					app.tags = Array.from(document.querySelectorAll('.q'));
					console.log(app.tags);
					app.ids = app.tags.map((ids) => ids.id);
					console.log(app.ids);

					//Utilizando a classe

					app.userNames = app.data.gameplayer.flatMap((user) => user.player.userName);
					console.log(app.userNames);
					app.oponentShips = app.data.oponentShips.flatMap((oponent) => oponent.salvos);
					console.log(app.oponentShips);

					//Aqui estamos verificando a intersecao e pintando os tiros que acertaram o alvo.
					app.intersection = app.location.filter((position) => app.oponentShips.includes(position));
					app.intersection.forEach((target) => {
						let arrow = document.getElementById(target);
						arrow.style.background = 'red';
					});
					console.log(app.location);
					//Pintando os tiros que deram na agua de azul:
					app.water = app.oponentShips.filter((agua) => !app.intersection.includes(agua));
					console.log(app.water);

					app.water.forEach((id) => {
						let missedShot = document.getElementById(id);
						missedShot.style.background = '#81ecec';
					});

					//Sacando os salvos e os colocando um array para compararlos.
					app.salvos = app.data.salvos.flatMap((salvo) => salvo.salvos);
				})
				.catch(function(error) {
					console.log('Request failed:' + error.message);
				});
		}
	}
});
