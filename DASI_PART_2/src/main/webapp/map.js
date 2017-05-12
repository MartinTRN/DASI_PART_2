var map;
var restoMarkers = [];
var clientsMarkers = [];
var livreurMarkers = [];


function makeInfoWindow(title) {
	return new google.maps.InfoWindow({
		content: '<div>Information: ' + title + '</div>'
	});
}

function attachInfoWindow(marker, infowindow, htmlDescription) {
	marker.addListener('click', function() {

		infowindow.setContent(htmlDescription);
		infowindow.open(map, this);
	});
}

function initMap() {
	map = new google.maps.Map(document.getElementById('map'), {
		center: {lat: 45.7601424, lng: 4.8961779},
		zoom: 13
	});

	var infowindow = makeInfoWindow('');

	var marker = new google.maps.Marker({
		map: map,
		position: {lat: 45.782122, lng: 4.872735},
		title: "Département IF, INSA de Lyon",
		icon: {url: './image/bird.png', scaledSize: new google.maps.Size(32, 32)}
	});

	marker.addListener('click', function() {

		infowindow.setContent('<div>Information: ' + marker.title + '</div>');
		infowindow.open(map, marker);
	});

	var marker2 = new google.maps.Marker({
		map: map,
		position: {lat: 45.782592, lng: 4.878238},
		title: "Entrée principale, INSA de Lyon",
		icon: {url: './image/snake.png', scaledSize: new google.maps.Size(32, 32)}
	});

	marker2.addListener('click', function() {

		infowindow.setContent('<div>Information: ' + marker2.title + '</div>');
		infowindow.open(map, marker2);
	});
}

function chargerRestos() {
	if(document.getElementById("voirRestaurants").checked) {
		var infowindow = makeInfoWindow('');

		$.ajax({
			url: './ActionServlet',
			type: 'POST',
			data: {
				action: 'listRestos'
			},
			dataType: 'json'
		})
				.done(function(data) {
					var restaurants = data.restaurants;

					restoMarkers = [];

					var i;
					for (i = 0; i < restaurants.length; i++) {
						var resto = restaurants[i];

						var marker = new google.maps.Marker({
							map: map,
							position: {lat: resto.latitude, lng: resto.longitude},
							title: resto.denomination
						});

						restoMarkers.push(marker);

						attachInfoWindow(
								marker, infowindow,
								'<div><strong><a href="./restaurant.html?' + resto.id + '">' + resto.denomination + '</a></strong><br/>' + resto.adresse + '<br/>' + resto.description + '</div>'
						);
					}

				})
				.fail(function() {
					//
				})
				.always(function() {
					//
				});
	} else {
		var i;
		for (i = 0; i < restoMarkers.length; i++) {
			restoMarkers[i].setMap(null);
		}
	}
}

function chargerClients() {
	if(document.getElementById("voirClients").checked) {
		var infowindow = makeInfoWindow('');

		$.ajax({
			url: './ActionServlet',
			type: 'POST',
			data: {
				action: 'listClients'
			},
			dataType: 'json'
		})
				.done(function(data) {
					var clients = data.clients;

					clientsMarkers = [];

					var i;
					for (i = 0; i < clients.length; i++) {
						var client = clients[i];

						var marker = new google.maps.Marker({
							map: map,
							position: {lat: client.latitude, lng: client.longitude},
							title: client.nom
						});

						clientsMarkers.push(marker);

						attachInfoWindow(
								marker, infowindow,
								'<div><strong>' + client.nom + '</strong><br/>' + client.mail + '<br/>' + client.adresse + '</div>'
						);
					}

				})
				.fail(function() {
					//
				})
				.always(function() {
					//
				});
	} else {
		var i;
		for (i = 0; i < clientsMarkers.length; i++) {
			clientsMarkers[i].setMap(null);
		}
	}
}

function chargerLivreurs() {
	if(document.getElementById("voirLivreurs").checked) {
		var infowindow = makeInfoWindow('');

		$.ajax({
			url: './ActionServlet',
			type: 'POST',
			data: {
				action: 'listLivreurs'
			},
			dataType: 'json'
		})
				.done(function(data) {
					var livreurs = data.livreurs;

					livreurMarkers = [];

					var i;
					for (i = 0; i < livreurs.length; i++) {
						var livreur = livreurs[i];

						var marker = new google.maps.Marker({
							map: map,
							position: {lat: livreur.latitude, lng: livreur.longitude},
							title: livreur.id
						});

						livreurMarkers.push(marker);

						attachInfoWindow(
								marker, infowindow,
								'<div><strong>' + livreur.id + '</strong><br/></div>'
						);
					}

				})
				.fail(function() {
					//
				})
				.always(function() {
					//
				});
	} else {
		var i;
		for (i = 0; i < livreurMarkers.length; i++) {
			livreurMarkers[i].setMap(null);
		}
	}
}
