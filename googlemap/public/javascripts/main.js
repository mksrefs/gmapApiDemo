$(function() {
	var map;
	// create map
	var initlat = 35.704316;
	var initlng = 139.75664;
	var location = {
		lat : initlat,
		lng : initlng
	};
	var mapElement = document.getElementById('map');
	if(!mapElement){
		return false;
	}
	map = new google.maps.Map(mapElement, {
		zoom : 20,
		minZoom : 16,
		center : location
	});
	var markerList = [];
	// prot by dragging
	google.maps.event.addListener(map,"dragend",function() {
		google.maps.event.addListenerOnce(map, "idle",getlocation);
		function getlocation() {
			var center = map.getCenter();
			$('#lat').text(center.lat());
			$('#lng').text(center.lng());
			var bounds = map.getBounds();
			var latFrom = bounds.getSouthWest().lat();
			var latTo = bounds.getNorthEast().lat();
			var lngFrom = bounds.getSouthWest().lng();
			var lngTo = bounds.getNorthEast().lng();
			$.ajax({
				type : "GET",
				url : "/api/search",
				data : {
					latFrom : latFrom,
					latTo : latTo,
					lngFrom : lngFrom,
					lngTo : lngTo
				}
			})
			.done(function(data) {
				var previousMarkerList = markerList.concat();
				$("#addList").empty();
				for (var i = 0; i < data.length; i++) {
					var dataLat = data[i].propertyWorldLatitude;
					var dataLng = data[i].propertyWorldLongitude;
					var dataName = data[i].propertyName;
					var marker = new google.maps.Marker(
							{
								position : new google.maps.LatLng(dataLat,dataLng),
								map : map,
								title : dataName
							});
					markerList.push(marker);
					$("#addList").append("<li>"+ dataName+ "</li>").addClass("data");
				}
				previousMarkerList.forEach(function(marker, index) {
					marker.setVisible(false);
					//marker.setMap(null);
				});
				
			});
			
		}
		
	});
	google.maps.event.trigger(map,"dragend");
	// geocoder
	var geocoder = new google.maps.Geocoder();
	$('#submit').click(function() {
		geocodeAddress(geocoder, map);
	});
	function geocodeAddress(geocoder, resultsMap) {
		var address = $('#address').val();
		geocoder.geocode({'address' : address},function(results, status) {
			if (status === 'OK') {
				resultsMap.setCenter(results[0].geometry.location);
				var marker = new google.maps.Marker({
					map : resultsMap,
					position : results[0].geometry.location
				});
				markerList.push(marker);
			} else {
				alert('Geocode was not successful for the following reason: '+ status);
			}
		});
	}
	// random set marker
	$("#random").click(function() {
		lat = Math.floor(Math.random() * 80);
		lng = Math.floor(Math.random() * 150);
		var marker = new google.maps.Marker({
			position : new google.maps.LatLng(lat, lng),
			map : map
		});
		markerList.push(marker);
	});
	// delete marker
	$("#delete").click(function() {
		markerList.forEach(function(marker, index) {
			marker.setMap(null);
		});
	});
});