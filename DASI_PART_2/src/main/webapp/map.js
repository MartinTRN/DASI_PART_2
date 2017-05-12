var map;
var directionsService;
var directionsDisplay;
var restoMarkers = [];
var clientsMarkers = [];
var livreurMarkers = [];
var itineraires = [];


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
	directionsService = new google.maps.DirectionsService;
	directionsDisplay = new google.maps.DirectionsRenderer;

	map = new google.maps.Map(document.getElementById('map'), {
		center: {lat: 45.7601424, lng: 4.8961779},
		zoom: 13
	});

	directionsDisplay.setMap(map);

	var infowindow = makeInfoWindow('');
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

function chargerTab() {
	var table = document.getElementById('tab').getElementsByTagName('tbody')[1];
	while (table.firstChild) {
    table.removeChild(table.firstChild);
	}

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

					var i;
					for (i = 0; i < livreurs.length; i++) {
						var livreur = livreurs[i];

						var newRow   = table.insertRow(table.rows.length);

						var nomCell  = newRow.insertCell(-1);
						nomCell.appendChild(document.createTextNode(livreur.nom));

						var statutCell  = newRow.insertCell(-1);
						statutCell.appendChild(document.createTextNode(livreur.statut));

						var departCell  = newRow.insertCell(-1);
						departCell.appendChild(document.createTextNode(livreur.adresseDepart));

						var arriveeCell  = newRow.insertCell(-1);
						arriveeCell.appendChild(document.createTextNode(livreur.adresseArrivee));

						var clientCell  = newRow.insertCell(-1);
						clientCell.appendChild(document.createTextNode(livreur.client));

						var commandeCell  = newRow.insertCell(-1);
						commandeCell.appendChild(document.createTextNode(livreur.commande));

						var idL = livreur.id;

						if(livreur.statut == 1) {
								var itiCell  = newRow.insertCell(-1);
								var btn = document.createElement('input');
									btn.type = "button";
									btn.value = "Voir l'itinéraire";
									btn.onclick = (function(idL) {return function() {chargerIti(idL);}})(idL);
								itiCell.appendChild(btn);
						} else {
								var itiCell  = newRow.insertCell(-1);
								itiCell.appendChild(document.createTextNode("-"));
						}
					}

				})
				.fail(function() {
					//
				})
				.always(function() {
					//
				});
}

function chargerIti(idLivreur) {
		$.ajax({
			url: './ActionServlet',
			type: 'POST',
			data: {
				action: 'recupItineraire',
				idLivreur: idLivreur
			},
			dataType: 'json'
		})
				.done(function(data) {
					var iti = data.itineraire;
					var itiu = iti[0];

					itineraires = [];
					itineraires.push({
              location: {lat: itiu.latitudeResto, lng: itiu.longitudeResto},
              stopover: true
            });

					directionsService.route({
		          origin: {lat: itiu.latitudeLivreur, lng: itiu.longitudeLivreur},
		          destination: {lat: itiu.latitudeClient, lng: itiu.longitudeClient},
		          waypoints: itineraires,
							optimizeWaypoints: true,
							travelMode: 'DRIVING'
      		}, function(response, status) {
		          if (status == 'OK') {
		            directionsDisplay.setDirections(response);
		          } else {
		            window.alert('Directions request failed due to ' + status);
		          }
        	});

				})
				.fail(function() {
					//
				})
				.always(function() {
					//
				});
}
