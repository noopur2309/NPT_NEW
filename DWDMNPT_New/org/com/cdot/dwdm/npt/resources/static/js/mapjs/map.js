var osmGeocoder;
var GisMap = (function() {
  var markerNodeMap = new Map(),
    mymap,
    icon,
    scrollFlag = true,
    mapContextMenuCoords = { lat: "26.9124", lng: "75.7873" };
  const latLongList = [
    { latitude: "26.9124", longitude: "75.7873" }, //Jaipur
    { latitude: "28.6139", longitude: "77.2090" }, // Delhi
    { latitude: "26.8467", longitude: "80.9462" }, //Luckhnow
    { latitude: "28.9845", longitude: "77.7064" }, // Meerut
    { latitude: "29.9671", longitude: "77.5510" }, //Saharanpur
    { latitude: "27.1767", longitude: "78.0081" }, //Agra
    { latitude: "28.0229", longitude: "73.3119" }, //Bikaner
    { latitude: "26.2389", longitude: "73.0243" }, //Jodhpur
    { latitude: "25.2138", longitude: "75.8648" }, //kota
    { latitude: "25.4484", longitude: "78.5685" } //Jhansi
  ];

  const layer0 = L.tileLayer(
    "https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}",
    {
      maxZoom: 19,
      attribution:
        "Tiles &copy; Esri &mdash; Source: Esri, i-cubed, USDA, USGS, AEX, GeoEye, Getmapping, Aerogrid, IGN, IGP, UPR-EGP, and the GIS User Community"
    }
  );

  //map.createPane('labels');
  const layer1 = L.tileLayer(
    "https://maps.wikimedia.org/osm-intl/{z}/{x}/{y}.png",
    {
      attribution:
        '<a href="https://wikimediafoundation.org/wiki/Maps_Terms_of_Use">Wikimedia</a>',
      maxZoom: 16,
      minZoom: 4
    }
  );
  const layer2 = L.tileLayer(
    "https://server.arcgisonline.com/ArcGIS/rest/services/NatGeo_World_Map/MapServer/tile/{z}/{y}/{x}",
    {
      attribution:
        "Tiles &copy; Esri &mdash; National Geographic, Esri, DeLorme, NAVTEQ, UNEP-WCMC, USGS, NASA, ESA, METI, NRCAN, GEBCO, NOAA, iPC",
      maxZoom: 16,
      minZoom: 4
    }
  );

  const layer3 = L.tileLayer(
    "https://{s}.tile.openstreetmap.fr/osmfr/{z}/{x}/{y}.png",
    {
      maxZoom: 16,
      minZoom: 4,
      attribution:
        '&copy; Openstreetmap France | &copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    }
  );
  const layer4 = L.tileLayer(
    "https://server.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer/tile/{z}/{y}/{x}",
    {
      attribution:
        "Tiles &copy; Esri &mdash; Source: Esri, DeLorme, NAVTEQ, USGS, Intermap, iPC, NRCAN, Esri Japan, METI, Esri China (Hong Kong), Esri (Thailand), TomTom, 2012",
      maxZoom: 16,
      minZoom: 4
    }
  );
  const layer5 = L.tileLayer(
    "https://map1.vis.earthdata.nasa.gov/wmts-webmerc/VIIRS_CityLights_2012/default/{time}/{tilematrixset}{maxZoom}/{z}/{y}/{x}.{format}",
    {
      attribution:
        'Imagery provided by services from the Global Imagery Browse Services (GIBS), operated by the NASA/GSFC/Earth Science Data and Information System (<a href="https://earthdata.nasa.gov">ESDIS</a>) with funding provided by NASA/HQ.',
      bounds: [
        [-85.0511287776, -179.999999975],
        [85.0511287776, 179.999999975]
      ],
      maxZoom: 16,
      minZoom: 4,
      format: "jpg",
      time: "",
      tilematrixset: "GoogleMapsCompatible_Level"
    }
  );
  const layer6 = L.tileLayer(
    "https://stamen-tiles-{s}.a.ssl.fastly.net/terrain/{z}/{x}/{y}.{ext}",
    {
      attribution:
        'Map tiles by <a href="http://stamen.com">Stamen Design</a>, <a href="http://creativecommons.org/licenses/by/3.0">CC BY 3.0</a> &mdash; Map data &copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>',
      subdomains: "abcd",
      maxZoom: 16,
      minZoom: 4,
      ext: "png"
    }
  );

  const cities = L.tileLayer(
    "http://{s}.basemaps.cartocdn.com/light_only_labels/{z}/{x}/{y}.png",
    {
      attribution: "©OpenStreetMap, ©CartoDB",
      maxZoom: 16
    }
  );
  const overlay2 = L.tileLayer(
    "https://tiles-{s}.openinframap.org/telecoms/{z}/{x}/{y}.png",
    {
      maxZoom: 18,
      attribution:
        '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>, <a href="http://www.openinframap.org/about.html">About OpenInfraMap</a>'
    }
  );
  //power
  const overlay3 = L.tileLayer(
    "https://tiles-{s}.openinframap.org/power/{z}/{x}/{y}.png",
    {
      maxZoom: 18,
      attribution:
        '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>, <a href="http://www.openinframap.org/about.html">About OpenInfraMap</a>'
    }
  );
  const overlay4 = L.tileLayer(
    "https://tiles-{s}.openinframap.org/petroleum/{z}/{x}/{y}.png",
    {
      maxZoom: 18,
      attribution:
        '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>, <a href="http://www.openinframap.org/about.html">About OpenInfraMap</a>'
    }
  );
  const overlay5 = L.tileLayer(
    "https://{s}.tiles.openrailwaymap.org/standard/{z}/{x}/{y}.png",
    {
      maxZoom: 19,
      attribution:
        'Map data: &copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a> | Map style: &copy; <a href="https://www.OpenRailwayMap.org">OpenRailwayMap</a> (<a href="https://creativecommons.org/licenses/by-sa/3.0/">CC-BY-SA</a>)'
    }
  );
  const overlay6 = L.tileLayer(
    "https://stamen-tiles-{s}.a.ssl.fastly.net/toner-lines/{z}/{x}/{y}.{ext}",
    {
      attribution:
        'Map tiles by <a href="http://stamen.com">Stamen Design</a>, <a href="http://creativecommons.org/licenses/by/3.0">CC BY 3.0</a> &mdash; Map data &copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>',
      subdomains: "abcd",
      minZoom: 0,
      maxZoom: 20,
      ext: "png"
    }
  );
  const overlay7 = L.tileLayer(
    "https://map1.vis.earthdata.nasa.gov/wmts-webmerc/MODIS_Terra_Snow_Cover/default/{time}/{tilematrixset}{maxZoom}/{z}/{y}/{x}.{format}",
    {
      attribution:
        'Imagery provided by services from the Global Imagery Browse Services (GIBS), operated by the NASA/GSFC/Earth Science Data and Information System (<a href="https://earthdata.nasa.gov">ESDIS</a>) with funding provided by NASA/HQ.',
      bounds: [
        [-85.0511287776, -179.999999975],
        [85.0511287776, 179.999999975]
      ],
      minZoom: 1,
      maxZoom: 8,
      format: "png",
      time: "",
      tilematrixset: "GoogleMapsCompatible_Level",
      opacity: 0.75
    }
  );

  const baseMaps = {
    GeoLocator: layer0,
    Layer1: layer1,
    Layer2: layer2,
    Layer3: layer3,
    Layer4: layer4,
    Layer5: layer5,
    Layer6: layer6
  };

  const overlayMaps = {
    Cities: cities,
    Telicom: overlay2,
    Power: overlay3,
    Petroleum: overlay4,
    RailwayMap: overlay5,
    TonerLines: overlay6,
    NASAGIBS_ModisTerraSnowCover: overlay7
  };

  return {
    initialize: function() {
      let delhiLatLng = { lat: "28.6139", lng: "77.2090" };

      mymap = L.map("canvasId", {
        center: [delhiLatLng.lat, delhiLatLng.lng],
        zoom: 6,
        zoomSnap: 1,
        layers: [layer1],
        fullscreenControl: true,
        fullscreenControlOptions: {
          position: "topleft"
        }
      });

      console.log("+++++++++++++++++ My map +++++++++++++++++++++", mymap);
      L.control.scale().addTo(mymap);

      //L.control.layers(baseMaps,overlayMaps).addTo(map);
      L.control
        .layers(baseMaps, overlayMaps, { position: "topleft" })
        .addTo(mymap);
      osmGeocoder = new L.Control.OSMGeocoder({
        placeholder: "Search location..."
      });
      mymap.addControl(osmGeocoder);
      //var element = document.getElementById("myModal");
      //element.classList.add(osmGeocoder);
      L.control
        .fullscreen({
          position: "topleft", // change the position of the button can be topleft,
          // topright, bottomright or bottomleft, defaut
          // topleft
          title: "Show me the fullscreen !", // change the title of the button,
          // default Full Screen
          titleCancel: "Exit fullscreen mode", // change the title of the button
          // when fullscreen is on, default
          // Exit Full Screen
          content: null, // change the content of the button, can be HTML,
          // default null
          forceSeparateButton: true, // force seperate button to detach from zoom
          // buttons, default false
          forcePseudoFullscreen: true, // force use of pseudo full screen even if
          // full screen API is available, default
          // false
          fullscreenElement: false // Dom element to render in full screen, false
          // by default, fallback to map._container
        })
        .addTo(mymap);

      icon = L.icon({
        iconUrl: "images/marker.png",
        iconSize: [5, 5] // size of the icon
      });

      let AllNodes = graph.getElements();

      let index = 0;
      //Initially set node position with respect to map markers
      _.each(AllNodes, function(node) {
        let //nodelat = latLongList[index].latitude,
          nodelat = node.get("nodeProperties").nodeLat || 26.47,
          // nodelng = latLongList[index].longitude,
          nodelng = node.get("nodeProperties").nodeLng || 75.46;
        nodeId = node.get("nodeProperties").nodeId;
        index++;
        console.log("nodelat :", nodelat, " nodelng:", nodelng);
        node.get("nodeProperties").nodeLat = nodelat;
        node.get("nodeProperties").nodeLng = nodelng;

        let marker = new L.marker(
          [nodelat, nodelng],
          { opacity: 0.0 },
          { MappedNodeId: nodeId }
        ).addTo(mymap);

        markerNodeMap.set(node, marker);
      });
      console.log("*********markerNodeMap*********", markerNodeMap);
    },
    mapZoomEvent: function(evt) {
      console.log("mapZoomEvent :", evt, " flag :", scrollFlag);
      console.log("markerNodeMap :", markerNodeMap);
      if (scrollFlag) {
        scrollFlag = false;

        //Initially set node position with respect to map markers
        markerNodeMap.forEach((marker, Node) => {
          console.log("Node :", Node, " Marker :", marker);
          let Marker = marker._icon;
          console.log("Node :", Node, " Marker :", Marker);
          let markerLocations = $(Marker)
            .css("transform")
            .replace(/[^0-9\-.,]/g, "")
            .split(",");
          // console.log("markerLocations ::", markerLocations);
          let elemWidth = 75 * parseFloat(mymap.getZoom() / 16);
          let elemHeight = 75 * parseFloat(mymap.getZoom() / 16);
          Node.set("position", {
            x: markerLocations[4] - parseFloat(elemWidth / 2),
            y: markerLocations[5] - parseFloat(elemHeight / 2)
          });
          Node.resize(elemWidth, elemHeight);
          console.log(
            "Inside Zoom Marker Node Map",
            Node.get("nodeProperties").nodeId
          );
        });

        //      var cnt1=0;
        //      // map.dragging.enable();
        //
        // let AllNodes = graph.getElements();
        // console.log("AllNdoes :", AllNodes);
        // $(".leaflet-marker-icon").each(function(index, value) {
        //   console.log("Leaflet marker :", $(this), " Index:", index);
        //   //   cnt1++;
        //   //alert("marker id"+$(this).options.myCustomId);
        //   let elem = AllNodes[index];
        //   console.log(elem);
        //   var textVal = elem.cid;
        //   console.log("text val:" + textVal);
        //   console.log("index:" + (index + 1));
        //   if (textVal !== undefined /*&& textVal === (index+1)*/) {
        //     var object = $(this)
        //       .css("transform")
        //       .replace(/[^0-9\-.,]/g, "")
        //       .split(",");
        //     console.log(object);
        //     let elemWidth = 75 * parseFloat(mymap.getZoom() / 16);
        //     let elemHeight = 75 * parseFloat(mymap.getZoom() / 16);
        //     elem.set("position", {
        //       x: object[4] - parseFloat(elemWidth / 2),
        //       y: object[5] - parseFloat(elemHeight / 2)
        //     });
        //     elem.resize(elemWidth, elemHeight);
        //     console.log(elem.cid);
        //   }
        //   // allElement.forEach(elem => {
        //   //     console.log(elem);
        //   //     var textVal = elem.cid;
        //   //     console.log("text val:"+textVal);
        //   //     console.log("index:"+(index+1));
        //   //     if(textVal !== undefined /*&& textVal === (index+1)*/) {
        //   //         var object=$(this).css('transform').replace(/[^0-9\-.,]/g, '').split(',');
        //   //         console.log(object);
        //   //         elem.set('position', { x: object[4]-50, y: object[5]-30});
        //   //         console.log(elem.cid);

        //   //     }
        //   // });
        // });
        scrollFlag = true;
      }

      console.log("Zoom ::", mymap.getZoom());
      // paper.scale(0.0625*mymap.getZoom(),0.0625*mymap.getZoom());
    },
    mapDragEvent: function(evt) {
      var matrix = $(".leaflet-pane")
        .css("transform")
        .replace(/[^0-9\-.,]/g, "")
        .split(",");
      var mark1 = matrix[12] || matrix[4];
      var mark2 = matrix[13] || matrix[5];
      paper.setOrigin(mark1, mark2);
      console.log("map drag");
    },
    setlocation: function(el) {
      var nodelat = el.get("nodeProperties").nodeLat;
      var nodelng = el.get("nodeProperties").nodeLng;
      var nodeId = el.get("nodeProperties").nodeId;

      let newLatLng = new L.LatLng(nodelat, nodelng);

      if (markerNodeMap.has(el)) {
        console.log(markerNodeMap.get(el));
        let existingMarker = markerNodeMap.get(el);
        // existingMarker.setMarker(newLatLng);
        markerNodeMap.set(el, undefined);
        mymap.removeLayer(existingMarker); // remove marker
      }
      console.log("el.get(nodeProperties) ::", el.get("nodeProperties"));

      let marker = new L.marker(
        newLatLng,
        { opacity: 0 },
        { MappedNodeId: nodeId }
      ).addTo(mymap);

      markerNodeMap.set(el, marker);

      this.mapZoomEvent();
    },
    degreesToRadians: function(degrees) {
      return (degrees * Math.PI) / 180;
    },
    distanceBetweenCoordinates: function(lat1, lon1, lat2, lon2, unit) {
      unit = unit || "K";
      console.log("distanceBetweenCoordinates", lat1, lon1, lat2, lon2);

      // let srcMarker = L.marker([lat1, lon1]);
      // let latlng = L.latLng(lat2, lon2);
      // let distance = L.distanceTo(latlng);
      // console.log("distance in metres", distance, distance / 1000);

      if (lat1 == lat2 && lon1 == lon2) {
        return 0;
      } else {
        var radlat1 = (Math.PI * lat1) / 180;
        var radlat2 = (Math.PI * lat2) / 180;
        var theta = lon1 - lon2;
        var radtheta = (Math.PI * theta) / 180;
        var dist =
          Math.sin(radlat1) * Math.sin(radlat2) +
          Math.cos(radlat1) * Math.cos(radlat2) * Math.cos(radtheta);
        if (dist > 1) {
          dist = 1;
        }
        dist = Math.acos(dist);
        dist = (dist * 180) / Math.PI;
        dist = dist * 60 * 1.1515;
        if (unit == "K") {
          dist = dist * 1.609344;
        }
        if (unit == "N") {
          dist = dist * 0.8684;
        }
        console.log("distance in K", dist);
        return dist;
      }

      // var earthRadiusKm = 6371;

      // var dLat = GisMap.degreesToRadians(lat2 - lat1);
      // var dLon = GisMap.degreesToRadians(lon2 - lon1);

      // lat1 = GisMap.degreesToRadians(lat1);
      // lat2 = GisMap.degreesToRadians(lat2);

      // var a =
      //   Math.sin(dLat / 2) * Math.sin(dLat / 2) +
      //   Math.sin(dLon / 2) *
      //     Math.sin(dLon / 2) *
      //     Math.cos(lat1) *
      //     Math.cos(lat2);
      // var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
      // return earthRadiusKm * c;
    },
    DistanceFromCdot: function(lat1, lng1) {
      var lat2 = 28.494357;
      var lng2 = 77.169705;
      var distance = distanceInKmBetweenEarthCoordinates(
        lat1,
        lng1,
        lat2,
        lng2
      );
      alert("Distance Between C-DOT to new Server :  " + distance);
    },
    getMap: function() {
      return mymap;
    },
    setContextMenuCoords: function(obj) {
      mapContextMenuCoords = obj;
    },
    getContextMenuCoords: function() {
      return mapContextMenuCoords;
    },
    setElementLatLng: function(cell, LatLng) {
      cell.attributes.nodeProperties.nodeLat = LatLng.lat;
      cell.attributes.nodeProperties.nodeLng = LatLng.lng;
    },
    getLatLngList: function() {
      return latLongList;
    },
    getElementLatLng: function(el) {
      let nodelat = el.get("nodeProperties").nodeLat;
      let nodelng = el.get("nodeProperties").nodeLng;

      console.log("getElementLatLng", el, nodelat, nodelng);

      let LatLng = new L.LatLng(nodelat, nodelng);
      return LatLng;
    }
  };
})();

$(document).ready(function() {
  //run the code here
  //  // your code goes here
  //  console.log("Index js loaded");
  //var graph = new joint.dia.Graph;
  //var paper = new joint.dia.Paper({
  //  el: $('#canvasId'),
  //  // width: 600,
  //  // height: 400,
  //  gridSize: 10,
  //  model: graph
  //});
  //
  //var nodeOne = new joint.shapes.basic.Rect({
  //  position: { x: 50, y: 70 },
  //  size: { width: 50, height: 40 }
  //});
  //
  //let nodeTwo=nodeOne.clone();
  //
  //var nodeThree = new joint.shapes.basic.Circle({
  //  position: { x: 50, y: 70 },
  //  size: { width: 50, height: 40 }
  //});
  //let nodeFour=nodeThree.clone();
  //
  //graph.addCell(nodeOne);
  //graph.addCell(nodeTwo);
  //graph.addCell(nodeThree);
  //graph.addCell(nodeFour);

  //var mymap = L.map('canvasId').setView([51.505, -0.09], 13);

  //   var layer0 = L.tileLayer(
  //     "https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}",
  //     {
  //       maxZoom: 19,
  //       attribution:
  //         "Tiles &copy; Esri &mdash; Source: Esri, i-cubed, USDA, USGS, AEX, GeoEye, Getmapping, Aerogrid, IGN, IGP, UPR-EGP, and the GIS User Community"
  //     }
  //   );

  //   //map.createPane('labels');
  //   var layer1 = L.tileLayer(
  //     "https://maps.wikimedia.org/osm-intl/{z}/{x}/{y}.png",
  //     {
  //       attribution:
  //         '<a href="https://wikimediafoundation.org/wiki/Maps_Terms_of_Use">Wikimedia</a>',
  //       maxZoom: 16,
  //       minZoom: 4
  //     }
  //   );
  //   var layer2 = L.tileLayer(
  //     "https://server.arcgisonline.com/ArcGIS/rest/services/NatGeo_World_Map/MapServer/tile/{z}/{y}/{x}",
  //     {
  //       attribution:
  //         "Tiles &copy; Esri &mdash; National Geographic, Esri, DeLorme, NAVTEQ, UNEP-WCMC, USGS, NASA, ESA, METI, NRCAN, GEBCO, NOAA, iPC",
  //       maxZoom: 16,
  //       minZoom: 4
  //     }
  //   );

  //   var layer3 = L.tileLayer(
  //     "https://{s}.tile.openstreetmap.fr/osmfr/{z}/{x}/{y}.png",
  //     {
  //       maxZoom: 16,
  //       minZoom: 4,
  //       attribution:
  //         '&copy; Openstreetmap France | &copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
  //     }
  //   );
  //   var layer4 = L.tileLayer(
  //     "https://server.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer/tile/{z}/{y}/{x}",
  //     {
  //       attribution:
  //         "Tiles &copy; Esri &mdash; Source: Esri, DeLorme, NAVTEQ, USGS, Intermap, iPC, NRCAN, Esri Japan, METI, Esri China (Hong Kong), Esri (Thailand), TomTom, 2012",
  //       maxZoom: 16,
  //       minZoom: 4
  //     }
  //   );
  //   var layer5 = L.tileLayer(
  //     "https://map1.vis.earthdata.nasa.gov/wmts-webmerc/VIIRS_CityLights_2012/default/{time}/{tilematrixset}{maxZoom}/{z}/{y}/{x}.{format}",
  //     {
  //       attribution:
  //         'Imagery provided by services from the Global Imagery Browse Services (GIBS), operated by the NASA/GSFC/Earth Science Data and Information System (<a href="https://earthdata.nasa.gov">ESDIS</a>) with funding provided by NASA/HQ.',
  //       bounds: [
  //         [-85.0511287776, -179.999999975],
  //         [85.0511287776, 179.999999975]
  //       ],
  //       maxZoom: 16,
  //       minZoom: 4,
  //       format: "jpg",
  //       time: "",
  //       tilematrixset: "GoogleMapsCompatible_Level"
  //     }
  //   );
  //   var layer6 = L.tileLayer(
  //     "https://stamen-tiles-{s}.a.ssl.fastly.net/terrain/{z}/{x}/{y}.{ext}",
  //     {
  //       attribution:
  //         'Map tiles by <a href="http://stamen.com">Stamen Design</a>, <a href="http://creativecommons.org/licenses/by/3.0">CC BY 3.0</a> &mdash; Map data &copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>',
  //       subdomains: "abcd",
  //       maxZoom: 16,
  //       minZoom: 4,
  //       ext: "png"
  //     }
  //   );

  //   var cities = L.tileLayer(
  //     "http://{s}.basemaps.cartocdn.com/light_only_labels/{z}/{x}/{y}.png",
  //     {
  //       attribution: "©OpenStreetMap, ©CartoDB",
  //       maxZoom: 16
  //     }
  //   );
  //   var overlay2 = L.tileLayer(
  //     "https://tiles-{s}.openinframap.org/telecoms/{z}/{x}/{y}.png",
  //     {
  //       maxZoom: 18,
  //       attribution:
  //         '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>, <a href="http://www.openinframap.org/about.html">About OpenInfraMap</a>'
  //     }
  //   );
  //   //power
  //   var overlay3 = L.tileLayer(
  //     "https://tiles-{s}.openinframap.org/power/{z}/{x}/{y}.png",
  //     {
  //       maxZoom: 18,
  //       attribution:
  //         '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>, <a href="http://www.openinframap.org/about.html">About OpenInfraMap</a>'
  //     }
  //   );
  //   var overlay4 = L.tileLayer(
  //     "https://tiles-{s}.openinframap.org/petroleum/{z}/{x}/{y}.png",
  //     {
  //       maxZoom: 18,
  //       attribution:
  //         '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>, <a href="http://www.openinframap.org/about.html">About OpenInfraMap</a>'
  //     }
  //   );
  //   var overlay5 = L.tileLayer(
  //     "https://{s}.tiles.openrailwaymap.org/standard/{z}/{x}/{y}.png",
  //     {
  //       maxZoom: 19,
  //       attribution:
  //         'Map data: &copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a> | Map style: &copy; <a href="https://www.OpenRailwayMap.org">OpenRailwayMap</a> (<a href="https://creativecommons.org/licenses/by-sa/3.0/">CC-BY-SA</a>)'
  //     }
  //   );
  //   var overlay6 = L.tileLayer(
  //     "https://stamen-tiles-{s}.a.ssl.fastly.net/toner-lines/{z}/{x}/{y}.{ext}",
  //     {
  //       attribution:
  //         'Map tiles by <a href="http://stamen.com">Stamen Design</a>, <a href="http://creativecommons.org/licenses/by/3.0">CC BY 3.0</a> &mdash; Map data &copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>',
  //       subdomains: "abcd",
  //       minZoom: 0,
  //       maxZoom: 20,
  //       ext: "png"
  //     }
  //   );
  //   var overlay7 = L.tileLayer(
  //     "https://map1.vis.earthdata.nasa.gov/wmts-webmerc/MODIS_Terra_Snow_Cover/default/{time}/{tilematrixset}{maxZoom}/{z}/{y}/{x}.{format}",
  //     {
  //       attribution:
  //         'Imagery provided by services from the Global Imagery Browse Services (GIBS), operated by the NASA/GSFC/Earth Science Data and Information System (<a href="https://earthdata.nasa.gov">ESDIS</a>) with funding provided by NASA/HQ.',
  //       bounds: [
  //         [-85.0511287776, -179.999999975],
  //         [85.0511287776, 179.999999975]
  //       ],
  //       minZoom: 1,
  //       maxZoom: 8,
  //       format: "png",
  //       time: "",
  //       tilematrixset: "GoogleMapsCompatible_Level",
  //       opacity: 0.75
  //     }
  //   );

  //   var baseMaps = {
  //     GeoLocator: layer0,
  //     Layer1: layer1,
  //     Layer2: layer2,
  //     Layer3: layer3,
  //     Layer4: layer4,
  //     Layer5: layer5,
  //     Layer6: layer6
  //   };

  //   var overlayMaps = {
  //     Cities: cities,
  //     Telicom: overlay2,
  //     Power: overlay3,
  //     Petroleum: overlay4,
  //     RailwayMap: overlay5,
  //     TonerLines: overlay6,
  //     NASAGIBS_ModisTerraSnowCover: overlay7
  //   };

  //   let latLongList = [
  //     { latitude: "28.6139", longitude: "77.2090" },
  //     { latitude: "12.9716", longitude: "77.5946" },
  //     { latitude: "10.8505", longitude: "76.2711" },
  //     { latitude: "22.5726", longitude: "88.3639" }
  //   ];

  //   mymap = L.map("canvasId", {
  //     center: [28.4983112, 77.16738910000004],
  //     zoom: 10,
  //     zoomSnap: 1,
  //     layers: [layer4],
  //     fullscreenControl: true,
  //     fullscreenControlOptions: {
  //       position: "topleft"
  //     }
  //   });

  //   console.log("+++++++++++++++++ My map +++++++++++++++++++++", mymap);
  //   L.control.scale().addTo(mymap);

  //   //L.control.layers(baseMaps,overlayMaps).addTo(map);
  //   L.control.layers(baseMaps, overlayMaps, { position: "topleft" }).addTo(mymap);
  //   var osmGeocoder = new L.Control.OSMGeocoder({
  //     placeholder: "Search location..."
  //   });
  //   mymap.addControl(osmGeocoder);
  //   //var element = document.getElementById("myModal");
  //   //element.classList.add(osmGeocoder);
  //   L.control
  //     .fullscreen({
  //       position: "topleft", // change the position of the button can be topleft,
  //       // topright, bottomright or bottomleft, defaut
  //       // topleft
  //       title: "Show me the fullscreen !", // change the title of the button,
  //       // default Full Screen
  //       titleCancel: "Exit fullscreen mode", // change the title of the button
  //       // when fullscreen is on, default
  //       // Exit Full Screen
  //       content: null, // change the content of the button, can be HTML,
  //       // default null
  //       forceSeparateButton: true, // force seperate button to detach from zoom
  //       // buttons, default false
  //       forcePseudoFullscreen: true, // force use of pseudo full screen even if
  //       // full screen API is available, default
  //       // false
  //       fullscreenElement: false // Dom element to render in full screen, false
  //       // by default, fallback to map._container
  //     })
  //     .addTo(mymap);

  //var marker = L.marker([51.5, -0.09]).addTo(mymap);

  //   var Icon = L.icon({
  //     iconUrl: "images/plus.png",
  //     iconSize: [5, 5] // size of the icon
  //   });

  //   let markerNodeMap = nptGlobals.getGisMarkerToNodeMap();

  //   let AllNodes = graph.getElements();
  //   //Initially set node position with respect to map markers
  //   _.each(AllNodes, function(node, key) {
  //     let nodelat = node.get("nodeProperties").latitude,
  //       nodelng = node.get("nodeProperties").longitude,
  //       nodeId = node.get("nodeProperties").nodeId;
  //     let marker = L.marker(
  //       [nodelat, nodelng],
  //       { opacity: 0.0 },
  //       { MappedNodeId: nodeId }
  //     ).addTo(mymap);
  //     //	console.log(value);
  //     //	setlocation(value);
  //     markerNodeMap.set(node, marker);
  //   });
  //   console.log("*********markerNodeMap*********", markerNodeMap);

  //   let flag = true;

  //   mapZoomEvent();

  //   function mapZoomEvent(evt) {
  //     console.log("mapZoomEvent :", evt, " flag :", flag);
  //     console.log("markerNodeMap :", markerNodeMap);
  //     if (flag) {
  //       flag = false;

  //       //Initially set node position with respect to map markers
  //       _.each(markerNodeMap, function(Node, Marker) {
  //         let markerLocations = Marker.css("transform")
  //           .replace(/[^0-9\-.,]/g, "")
  //           .split(",");
  //         console.log("markerLocations ::", markerLocations);
  //         let elemWidth = 75 * parseFloat(mymap.getZoom() / 16);
  //         let elemHeight = 75 * parseFloat(mymap.getZoom() / 16);
  //         Node.set("position", {
  //           x: markerLocations[4] - parseFloat(elemWidth / 2),
  //           y: markerLocations[5] - parseFloat(elemHeight / 2)
  //         });
  //         Node.resize(elemWidth, elemHeight);
  //         console.log(
  //           "Inside Zoom Marker Node Map::",
  //           Node.get("nodeProperties").NodeId
  //         );
  //       });

  //       //      var cnt1=0;
  //       //      // map.dragging.enable();
  //       //
  //       //      let AllNodes=graph.getElements();
  //       //      console.log("AllNdoes :",AllNodes);
  //       //      $('.leaflet-marker-icon').each(function(index, value){
  //       //          console.log("Leaflet marker :",$(this)," Index:",index);
  //       //          cnt1++;
  //       //          //alert("marker id"+$(this).options.myCustomId);
  //       //          let elem=AllNodes[index];
  //       //          console.log(elem);
  //       //          var textVal = elem.cid;
  //       //          console.log("text val:"+textVal);
  //       //          console.log("index:"+(index+1));
  //       //          if(textVal !== undefined /*&& textVal === (index+1)*/) {
  //       //              var object=$(this).css('transform').replace(/[^0-9\-.,]/g, '').split(',');
  //       //              console.log(object);
  //       //              let elemWidth=75*(parseFloat(mymap.getZoom()/16));
  //       //              let elemHeight=75*(parseFloat(mymap.getZoom()/16));
  //       //              elem.set('position', { x: object[4]-(parseFloat(elemWidth/2)), y: object[5]-(parseFloat(elemHeight/2))});
  //       //              elem.resize(elemWidth,elemHeight);
  //       //              console.log(elem.cid);
  //       //
  //       //          }
  //       //          // allElement.forEach(elem => {
  //       //          //     console.log(elem);
  //       //          //     var textVal = elem.cid;
  //       //          //     console.log("text val:"+textVal);
  //       //          //     console.log("index:"+(index+1));
  //       //          //     if(textVal !== undefined /*&& textVal === (index+1)*/) {
  //       //          //         var object=$(this).css('transform').replace(/[^0-9\-.,]/g, '').split(',');
  //       //          //         console.log(object);
  //       //          //         elem.set('position', { x: object[4]-50, y: object[5]-30});
  //       //          //         console.log(elem.cid);
  //       //
  //       //          //     }
  //       //          // });
  //       //
  //       //      });
  //       flag = true;
  //     }

  //     console.log("Zoom ::", mymap.getZoom());
  //     // paper.scale(0.0625*mymap.getZoom(),0.0625*mymap.getZoom());
  //   }

  //   function mapDragEvent(evt) {
  //     var matrix = $(".leaflet-pane")
  //       .css("transform")
  //       .replace(/[^0-9\-.,]/g, "")
  //       .split(",");
  //     var mark1 = matrix[12] || matrix[4];
  //     var mark2 = matrix[13] || matrix[5];
  //     paper.setOrigin(mark1, mark2);
  //     console.log("map drag");
  //   }

  GisMap.initialize();
  GisMap.getMap().doubleClickZoom.disable();

  /**
   * Map Events
   */
  GisMap.getMap().on("zoom", function(e) {
    GisMap.mapZoomEvent(e);
  });

  GisMap.getMap().on("click", function(e) {
    console.log("Map Click");
  });

  GisMap.getMap().on("contextmenu", function(e, x, y) {
    console.log("Rigt Click", e.latlng);
    GisMap.setContextMenuCoords(e.latlng);
  });

  GisMap.getMap().on("drag", function(e) {
    GisMap.mapDragEvent(e);
  });

  GisMap.getMap().on("mouseup", function(e) {
    console.log("mouseup", e.latlng);
    GisMap.setContextMenuCoords(e.latlng);
  });

  // GisMap.getMap().on("dblclick", function(e) {
  //   console.log("dblclk");
  //   e.stopPropagation();
  //   return false;
  // });

  GisMap.getMap().on("moveend  ", function(e) {
    console.log("MoveEnd");
    GisMap.mapDragEvent(e);
  });

  GisMap.mapZoomEvent();

  /**
   * Paper Events
   */
  //paper.on('cell:pointerdown', function (cellView, evt, x, y) {
  //	console.log("Cell pointer Down")
  //	mymap.dragging.disable();
  //});
  //
  //paper.on('cell:pointerup', function (cellView, evt, x, y) {
  //	console.log("Cell pointer Up");
  //  mymap.dragging.enable();
  //});

  //   function setlocation(el) {
  //     var nodelat = el.get("nodeProperties").latitude;
  //     var nodelng = el.get("nodeProperties").longitude;
  //     var node_id = el.get("nodeProperties").nodeId;
  //     let marker = L.marker(
  //       [nodelat, nodelng],
  //       { opacity: 0.0 },
  //       { MappedNodeId: node_id }
  //     ).addTo(map);
  //   }

  //   $("#clearpaper").click(function() {
  //     //map.removeLayer(marker);
  //   });

  //   function degreesToRadians(degrees) {
  //     return (degrees * Math.PI) / 180;
  //   }

  //   function distanceInKmBetweenEarthCoordinates(lat1, lon1, lat2, lon2) {
  //     var earthRadiusKm = 6371;

  //     var dLat = degreesToRadians(lat2 - lat1);
  //     var dLon = degreesToRadians(lon2 - lon1);

  //     lat1 = degreesToRadians(lat1);
  //     lat2 = degreesToRadians(lat2);

  //     var a =
  //       Math.sin(dLat / 2) * Math.sin(dLat / 2) +
  //       Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
  //     var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  //     return earthRadiusKm * c;
  //   }
  //   function DistanceFromCdot(lat1, lng1) {
  //     var lat2 = 28.494357;
  //     var lng2 = 77.169705;
  //     var distance = distanceInKmBetweenEarthCoordinates(lat1, lng1, lat2, lng2);
  //     alert("Distance Between C-DOT to new Server :  " + distance);
  //   }
});
