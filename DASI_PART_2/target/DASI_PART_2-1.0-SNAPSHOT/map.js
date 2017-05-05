var map;

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
                                    console.log("yo");
					var restaurants = data.restaurants;
					var i;
					for (i = 0; i < restaurants.length; i++) {
						var resto = restaurants[i];

						var marker = new google.maps.Marker({
							map: map,
							position: {lat: resto.latitude, lng: resto.longitude},
							title: resto.denomination,
                                                        icon: {url: './image/snake.png', scaledSize: new google.maps.Size(32, 32)}
						});

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
	}
}