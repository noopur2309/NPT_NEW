/************************************************************************
 ************************************************************************
Var define along with their div Id and property
 ************************************************************************
 ************************************************************************/
//General Canvas where shapes are dropped
var graph = new joint.dia.Graph(),
  paper = new joint.dia.Paper({
    el: $("#canvasId"),
    height: 800,
    width: 1300,
    model: graph,
    snapLinks: {
      radius: 75
    },
    markAvailable: true,
    defaultLink: function() {
      var link = new joint.dia.Link({
        attrs: {
          ".connection": {
            stroke: "Green",
            "stroke-width": 2,
            "stroke-opacity": 0.5,
            fill: "rgba(0,0,0,0)",
            d: "M 10 0 L 0 5 L 10 10 z"
          },
          ".connection-wrap": {
            stroke: "#E74C3C",
            "stroke-width": 2,
            "stroke-opacity": 0.2,
            fill: "rgba(0,0,0,0)"
          },
          ".marker-source": {
            stroke: "rbga(0,0,0,0)",
            fill: "#777",
            d: "M 10 0 L 0 5 L 10 10 z",
            "fill-opacity": 0.5
          },
          ".marker-target": {
            stroke: "rbga(0,0,0,0)",
            fill: "#777",
            d: "M 10 0 L 0 5 L 10 10 z",
            "fill-opacity": 0.5
          },
          ".marker-arrowhead": {
            stroke: "rbga(0,0,0,0)",
            fill: "#777",
            d: "M 10 0 L 0 5 L 10 10 z",
            "fill-opacity": 0.5
          },
          ".link-tools": {
            stroke: "rbga(0,0,0,0)",
            fill: "rbga(0,0,0,0)",
            visibility: "hidden"
          }
          //				'.tool-options circle':{stroke: 'rbga(0,0,0,0)', fill: 'rbga(0,0,0,0)',visibility:'hidden'},
          //				'.tool-options path':{stroke: 'rbga(0,0,0,0)', fill: 'rbga(0,0,0,0)',visibility:'hidden'},
          //				'.tool-remove':{stroke: 'rbga(0,0,0,0)', fill: 'rbga(0,0,0,0)',visibility:'hidden'},
          //				'.tool-remove path':{stroke: 'rbga(0,0,0,0)', fill: 'rbga(0,0,0,0)',visibility:'hidden'}
        },
        labels: [
          {
            position: 0.5,
            attrs: {
              text: {
                text: "",
                stroke: "#d9534f",
                "font-size": "24px",
                "stroke-width": 4
              }
            }
          }
        ],
        linkProperties: {
          linkId: 1,
          fiberType: 1 /**Default Fiber Type is 1 : G.652.D*/,
          cdCoefficient: 19.6,
          pmdCoefficient: 0.2,
          cd: 1568,
          pmd: 1.79,
          spanLength: 80,
          splice: 1,
          lossPerSplice: 1,
          connector: 1,
          lossPerConnector: 1,
          coefficient: 0.275,
          calculatedSpanLoss: 24,
          adjustableSpanLoss: 24,
          costMetric: 3,
          opticalParameter: 3,
          srlg: 0,
          color: 4,
          scope: "P",
          isIlaLink: "0", //1-true 0-false
          faultLink: "0",
          labelText: "",
          SrcNodeDirection: "",
          DestNodeDirection: "",
          lineProtection: "0", //1-Yes , 0-No
          BrownFieldLink: 0, //1-Yes , 0-No
          linkType: "DEFAULT (PA/BA)", // 'Default'/'Raman Hybrid'/'Raman DRA'
          autoDelete: false
        }
      });
      //		link.set('router',{name:'metro'});
      //        link.set('router', { name: 'metro' });
      //      link.set('connector', { name: 'normal' });
      //   link.set('connector', { name: 'smooth' });
      return link;
    },
    linkPinning: false,
    gridSize: 15,
    drawGrid: true,
    interactive: function(cellView) {
      if (cellView.model instanceof joint.dia.Link) {
        // Disable the default vertex add functionality on pointerdown.
        return { vertexAdd: false };
      }

      //if(nptGlobals.NetworkTopology==nptGlobals.TopologyLinearStr || nptGlobals.NetworkTopology==nptGlobals.TopologyTwoFiberRingStr)
      //	return {linkPinning:false};

      return true;
    },
    //router:{name:'metro'},
    validateConnection: function(
      cellViewS,
      magnetS,
      cellViewT,
      magnetT,
      end,
      linkView
    ) {
      // console.log(cellViewS);
      // console.log(cellViewT);
      // console.log(magnetS);
      // console.log(magnetT);
      // console.log(end);
      // console.log(linkView);

      // Prevent linking from output ports to input ports within one element.
      if (cellViewS === cellViewT) return false;

      /*** Prevent linking nodes already having link between them ***/

      // let nodeConnectionsDestNode=cellViewT.model.get('nodeProperties').nodeConnections;
      // let srcNodeId=cellViewS.model.get('nodeProperties').nodeId;
      // for(i in nodeConnectionsDestNode )
      // 	if(nodeConnectionsDestNode[i]===srcNodeId) return false;

      if (cellViewS && cellViewT)
        return paperUtil.fValidateConnection(cellViewS, cellViewT, linkView);
      // return true;
    }
  });

//var paperScroller = new joint.ui.PaperScroller({
//	paper: paper,
//	cursor: 'grab'
//});
//
//$('#canvasId').append(paperScroller.render().el);
//
//var nav = new joint.ui.Navigator({
//    paperScroller: paperScroller,
//    width: 300,
//    height: 200,
//    padding: 5,
//    zoomOptions: { max: 2, min: 0.6 }
//});
//nav.$el.appendTo('#paper-navigator');
//nav.render();

//(function(){
//     paper.svg.setAttribute('pointer-events', 'none');
//     paper.svg.setAttribute('z-index', '1');
//	 paper.$grid.css('pointer-events', 'none');
//	 paper.$grid.css('display','none');
//}());
//
//
////const mapsApiKey='AIzaSyC75dumiC0em2MWC-rkoJBbdnyY_V9KlFM';
//var map,firsttime=true,prevZoom=5;
//function initMap() {
//	console.log("InitMap");
//    var uluru = {lat: -25.363, lng: 131.044};
//    map = new google.maps.Map(paper.$background[0], {
//      zoom: 4,
//      center: uluru
//    });
//    var marker = new google.maps.Marker({
//      position: uluru,
//      map: map
//    });
//
//    map.setOptions({ minZoom: 5, maxZoom: 15 });
//
//    map.addListener('zoom_changed', function() {
//    	console.log("Zoom Changed",map.getZoom());
//    	if(firsttime){
//    		paper.scale(1,1);
//    		firsttime=false;
//    	}
//    	let scale=paper.scale();
//    	if(map.getZoom()>prevZoom)
//    	paper.scale(scale.sx+1,scale.sy+1);
//    	else paper.scale(scale.sx-1,scale.sy-1);
//
//    	prevZoom=map.getZoom();
////      coordInfoWindow.setContent(createInfoWindowContent(chicago, map.getZoom()));
////      coordInfoWindow.open(map);
//    });
//  }

//function initMap() {
//    var chicago = new google.maps.LatLng(41.850, -87.650);
//
//    map = new google.maps.Map(paper.$background[0], {
//      center: chicago,
//      zoom: 3
//    });
//
//
//
//    var coordInfoWindow = new google.maps.InfoWindow();
//    coordInfoWindow.setContent(createInfoWindowContent(chicago, map.getZoom()));
//    coordInfoWindow.setPosition(chicago);
//    coordInfoWindow.open(map);
//
//    map.addListener('zoom_changed', function() {
//    	console.log("Zoom Changed",createInfoWindowContent(chicago, map.getZoom()));
////      coordInfoWindow.setContent(createInfoWindowContent(chicago, map.getZoom()));
////      coordInfoWindow.open(map);
//    });
//  }
//
//  var TILE_SIZE = 256;
//
//  function createInfoWindowContent(latLng, zoom) {
//    var scale = 1 << zoom;
//
//    var worldCoordinate = project(latLng);
//
//    var pixelCoordinate = new google.maps.Point(
//        Math.floor(worldCoordinate.x * scale),
//        Math.floor(worldCoordinate.y * scale));
//
//    var tileCoordinate = new google.maps.Point(
//        Math.floor(worldCoordinate.x * scale / TILE_SIZE),
//        Math.floor(worldCoordinate.y * scale / TILE_SIZE));
//
//    return pixelCoordinate;
//  }
//
//  // The mapping between latitude, longitude and pixels is defined by the web
//  // mercator projection.
//  function project(latLng) {
//    var siny = Math.sin(latLng.lat() * Math.PI / 180);
//
//    // Truncating to 0.9999 effectively limits latitude to 89.189. This is
//    // about a third of a tile past the edge of the world tile.
//    siny = Math.min(Math.max(siny, -0.9999), 0.9999);
//
//    return new google.maps.Point(
//        TILE_SIZE * (0.5 + latLng.lng() / 360),
//        TILE_SIZE * (0.5 - Math.log((1 + siny) / (1 - siny)) / (4 * Math.PI)));
//  }
//

//  paper.on('scale', function(sx) {
//	  console.log('Scaling map with paper :',sx);
//	  map.setZoom(3 + sx);
//	});

//var commandManager = new joint.dia.CommandManager({ graph: graph });
//
//$("#canvasId").resize(function() {
//	console.log("Canvas width change")
//   // var canvas = $('#canvasId');
//    paper.setDimensions($(this).width(), $(this).height());
//    paper.scale(0.5);
//});

let FaultAnalysisLinkColors = [
  "#20753b",
  "#57b7ff",
  "#f6861f",
  "#662770",
  "#a36314",
  "#babdb6"
];
let linkWidth = "4";
let IlaNodePath = "M 0 0 L 0 80 40 40 z";
let nodeLabelFontSize = "10px";
let highlightColor = "#eba02d";
let faultColor = "#fd1c42";
let nodeDefaultTextAttr = {
  x: 72,
  y: 114,
  fill: "white",
  stroke: "white",
  "stroke-width": 0.5
};

let nodeDefaultTitleTextAttr = {
  x: 64,
  y: 44,
  fill: "white",
  stroke: "white",
  "stroke-width": 0.5
};

let nodePotpTextAttr = {
  x: 67,
  y: 85,
  fill: "white",
  stroke: "white",
  "stroke-width": 0
};

let RamanDefaultTitleTextAttr = {
  x: 50,
  y: 44,
  fill: "white",
  stroke: "white",
  "stroke-width": 0.5
};

let nodeDefaultCircleAttr = {
  stroke: "white",
  "stroke-width": 0
  // 'cx':75,
  // 'cy':140
  // 'z-index':10
};

let nodeDefaultHighlightAttr = {
  stroke: "white",
  "stroke-width": 0
};

var nodeDefaultSize = {
  width: 75,
  height: 75
};

const nodePropertiesObj = {
  nodeId: "1",
  displayName: nptGlobals.NodeTypeCDROADMDisplayName,
  stationName: "new delhi",
  siteName: "chattarpur",
  gneFlag: 0,
  vlanTag: "1",
  degree: 0,
  directions: {
    east: "0",
    west: "0",
    north: "0",
    south: "0",
    ne: "0",
    nw: "0",
    se: "0",
    sw: "0"
  },
  numDirections: {
    east: "0",
    west: "0",
    north: "0",
    south: "0",
    ne: "0",
    nw: "0",
    se: "0",
    sw: "0"
  },
  nodeConnections: {
    1: 0,
    2: 0,
    3: 0,
    4: 0,
    5: 0,
    6: 0,
    7: 0,
    8: 0
  },
  faultNode: 0,
  emsip: "0.0.0.0",
  subnet: "255.255.255.0",
  gateway: "192.168.115.1",
  ipv6: "fe80::2e0:4cff:fe36:90a/64",
  capacity: 3,
  direction: "West",
  opticalReach: "Long Haul",
  BrownFieldNode: "0", //1-Yes , 0-No
  nodeLat: "26.91",
  nodeLng: "75.78",
  nodeType: "",
  nodeSubtype: 0,
  ramanLinks: 0
};
{
  /* <rect x="40" y="60" width="70" height="25" fill="#1d1919b3" stroke="rgba(0,0,0,0)" stroke-width="1"/> */
}

let nodeDefaultMarkup = `<g class="rotatable"><g class="scalable"> 			
<svg width='150px' height='150px' class="app-cell">			
  <circle cx='75' cy='75' r='50' fill='white' class="app-cell__highlight"/>		  
  <image class="app-cell__image"/>  
  <text font-size="12" class="app-cell__title"/>
  <circle r='11' cx='75' cy='112' fill='#34495e' class="app-cell__label--bkg"/>	
  <text class="label"/>
</svg></g></g>`;

let nodePotpMarkup = `<g class="rotatable"><g class="scalable"> 			
<svg width='150px' height='150px' class="app-cell">			
  <circle cx='75' cy='75' r='50' fill='white' class="app-cell__highlight"/>		  
  <image class="app-cell__image"/>  
  <text class="label node-label"/>
</svg></g></g>`;

//Presentational attributes.
var attrs = {
  elementDefault: {
    text: nodeDefaultTextAttr,
    ".app-cell__highlight": nodeDefaultHighlightAttr

    //rect: { stroke: 'black','stroke-width':'2' },
    // '.label':{'stroke':'black','ref-x':20},
    // '.body':{stroke:'black','stroke-width':'0'},
    // '.v-line':{'font-size':nodeLabelFontSize,'stroke-width':'2'},
    /*'.props': {fill:'white','stroke-width':'',stroke:'#763a3a','ref-y': -40,'ref-x':10,'ref':'.outer','font-size':'15px'},
			'.props .v-line': {	'stroke-width':'2','font-size':'15px'}*/
  },
  IlaDefault: {
    // text: {	x:72,y:123,	fill:'white','stroke': 'white', 'stroke-width': 1},
    // '.app-cell__highlight':{'stroke': 'white','stroke-width': 0,'fill':'rgba(0,0,0,0)'}

    text: nodeDefaultTextAttr,
    ".app-cell__highlight": nodeDefaultHighlightAttr
    // text: { fill: '#fff', style: { 'text-shadow': '1px 1px 1px #999', 'text-transform': 'capitalize' } },
    // path: { d: IlaNodePath,stroke:"#4f58a2",fill:"#f5f5f5",'stroke-width':2,'stroke-dasharray':"0 0"},
    // '.label':{	fill:'#777','stroke-width':1,stroke:'#777','ref-y': 19,'ref-x':16,'font-size':'15px'},
    // '.v-line':{'font-size':nodeLabelFontSize,'stroke-width':'2'},
    // '.props': {fill:'white','stroke-width':'',stroke:'#763a3a','ref-y': -40,'ref-x':10,'ref':'.outer','font-size':'15px'},
    // '.props .v-line': {	'stroke-width':'2','font-size':'20px'}
  },
  IlaRamanHybrid: {
    text: nodeDefaultTextAttr,
    ".app-cell__highlight": nodeDefaultHighlightAttr,
    ".app-cell__title": RamanDefaultTitleTextAttr
  },
  IlaRamanDra: {
    text: nodeDefaultTextAttr,
    ".app-cell__highlight": nodeDefaultHighlightAttr,
    ".app-cell__title": RamanDefaultTitleTextAttr
  },
  TeDefault: {
    text: nodeDefaultTextAttr,
    ".app-cell__highlight": nodeDefaultHighlightAttr
    //rect: { stroke: 'black','stroke-width':'2' },
    // '.label':{'stroke':'white','ref-x':20},
    // '.body':{stroke:'black','stroke-width':'0'},
    // '.v-line':{'font-size':nodeLabelFontSize,'stroke-width':'1'},
    /*'.props': {fill:'white','stroke-width':'',stroke:'#763a3a','ref-y': -40,'ref-x':10,'ref':'.outer','font-size':'15px'},
			'.props .v-line': {	'stroke-width':'2','font-size':'15px'}*/
  },
  CdRoadmDefault: {
    text: nodeDefaultTextAttr,
    ".app-cell__highlight": nodeDefaultHighlightAttr
    //rect: { stroke: 'black','stroke-width':'2' },
    // '.label':{'stroke':'white','ref-x':20},
    // '.body':{stroke:'black','stroke-width':'0'},
    // '.v-line':{'font-size':nodeLabelFontSize,'stroke-width':'1'},
    /*'.props': {fill:'white','stroke-width':'',stroke:'#763a3a','ref-y': -40,'ref-x':10,'ref':'.outer','font-size':'15px'},
			'.props .v-line': {	'stroke-width':'2','font-size':'15px'}*/
  },
  elementSelected: {
    text: { fill: highlightColor, stroke: highlightColor },
    ".app-cell__highlight": { stroke: highlightColor, "stroke-width": "10" }
  },
  elementHighlighted: {
    ".label": { stroke: "#d9534f" },
    ".body": { stroke: "#d9534f", "stroke-width": "3" }
  },
  elementFault: {
    text: { fill: faultColor, stroke: faultColor },
    ".app-cell__highlight": { stroke: faultColor, "stroke-width": "15" },
    ".label": { text: "X" }
  },
  // elementFault: {
  // 	//rect: { stroke: '#d9534f','stroke-width':'','stroke-opacity':'1'},
  // 	path: { d: IlaNodePath,stroke:"red",fill:"#f5f5f5",'stroke-width':3,'stroke-dasharray':"10 2"},
  // 	'.label':{'stroke':'#d9534f',text:'X','ref-x':17},
  // 	'.v-line':{'font-size':'30px','stroke-width':'5'}
  // },
  HighlightedIntermediateNode: {
    //rect: { stroke: 'blue','stroke-width':'','stroke-opacity':'1'},
    ".label": { stroke: "#337ab7" },
    ".body": { stroke: "#337ab7", "stroke-width": "3" }
  },
  HighlightedRegeneratorNode: {
    //rect: { stroke: 'blue','stroke-width':'','stroke-opacity':'1'},
    ".label": { stroke: "#3acb7c" },
    ".body": { stroke: "#3acb7c", "stroke-width": "5" }
  },
  linkDefault: {
    ".connection": {
      stroke: "Green",
      "stroke-width": linkWidth,
      "stroke-opacity": 0.5,
      "stroke-dasharray": "0 0",
      fill: "rgba(0,0,0,0)"
    },
    //'.connection':{stroke:'Yellow','stroke-width':10,'stroke-opacity':0.5,'stroke-dasharray':"0 0",fill:'rgba(0,0,0,0)'},
    ".connection-wrap": {
      stroke: "#E74C3C",
      "stroke-width": 2,
      "stroke-opacity": 0.2,
      fill: "rgba(0,0,0,0)"
    },
    ".marker-source": {
      stroke: "rbga(0,0,0,0)",
      fill: "#333",
      d: "M 10 0 L 0 5 L 10 10 z",
      "fill-opacity": 0.5
    },
    ".marker-target": {
      stroke: "rbga(0,0,0,0)",
      fill: "#333",
      d: "M 10 0 L 0 5 L 10 10 z",
      "fill-opacity": 0.5
    },
    ".marker-arrowhead": {
      stroke: "rbga(0,0,0,0)",
      fill: "#333",
      d: "M 10 0 L 0 5 L 10 10 z",
      "fill-opacity": 0.5
    },
    ".link-tools": {
      stroke: "rbga(0,0,0,0)",
      fill: "rbga(0,0,0,0)",
      visibility: "hidden"
    }
  },
  IlalinkDefault: {
    ".connection": {
      stroke: "#333",
      "stroke-width": linkWidth,
      "stroke-opacity": 0.5,
      "stroke-dasharray": "0 0"
    },
    ".connection-wrap": {
      stroke: "#E74C3C",
      "stroke-width": 2,
      "stroke-opacity": 0.2
    },
    ".marker-source": {
      stroke: "rbga(0,0,0,0)",
      fill: "#333",
      d: "M 10 0 L 0 5 L 10 10 z",
      "fill-opacity": 0.5
    },
    ".marker-target": {
      stroke: "rbga(0,0,0,0)",
      fill: "#333",
      d: "M 10 0 L 0 5 L 10 10 z",
      "fill-opacity": 0.5
    },
    ".marker-arrowhead": {
      stroke: "rbga(0,0,0,0)",
      fill: "#333",
      d: "M 10 0 L 0 5 L 10 10 z",
      "fill-opacity": 0.5
    }
  },
  linkDefaultLinePtc: {
    ".connection": {
      stroke: "Green",
      "stroke-width": 10,
      "stroke-opacity": 0.5,
      fill: "rgba(0,0,0,0)",
      d: "M 10 0 L 0 5 L 10 10 z",
      "stroke-dasharray": "0 0"
    },
    ".connection-wrap": {
      stroke: "#E74C3C",
      "stroke-width": 2,
      "stroke-opacity": 0.2,
      fill: "rgba(0,0,0,0)"
    },
    ".marker-source": {
      stroke: "rbga(0,0,0,0)",
      fill: "#777",
      d: "M 10 0 L 0 5 L 10 10 z",
      "fill-opacity": 0.5
    },
    ".marker-target": {
      stroke: "rbga(0,0,0,0)",
      fill: "#777",
      d: "M 10 0 L 0 5 L 10 10 z",
      "fill-opacity": 0.5
    },
    ".marker-arrowhead": {
      stroke: "#3c763d",
      fill: "rgba(0,0,0,0)",
      "stroke-width": 2,
      d: "M 30 10 L 20 10 L 20 4 L 11 13 L 20 22 L 20 16 L 30 16z",
      "fill-opacity": 0.5
    }
  },
  linkFaultAnalysis: {},
  linkDefaultDirected: {
    ".marker-target": { d: "M 6 0 L 0 3 L 6 6 z" }
  },
  linkHighlighted: {
    //'.connection':{stroke:"#d9534f",d:"M 30 140 320 140",'stroke-width':"5" ,'stroke-dasharray':"0 0"},
    ".marker-arrowhead": {
      fill: "#d9534f",
      d: "M 14 0 L 0 7 L 14 14 z",
      stroke: "rgba(0,0,0,0)"
    },
    //'.marker-arrowhead':{fill:'magenta',d: 'M 0 0 L 0 0 L 0 0 z',stroke: 'rgba(0,0,0,0)'},
    ".marker-target": {
      stroke: "rbga(0,0,0,0)",
      fill: "#d9534f",
      d: "M 20 0 L 0 10 L 20 20 z",
      "fill-opacity": 1
    },
    ".marker-source": {
      stroke: "rbga(0,0,0,0)",
      fill: "#d9534f",
      d: "M 20 0 L 0 10 L 20 20 z",
      "fill-opacity": 1
    }
  },
  linkHighlightedColor1: {
    ".connection": {
      stroke: FaultAnalysisLinkColors[0],
      d: "M 30 140 320 140",
      /* 'stroke-width':"4"  , */ "stroke-dasharray": "10 2",
      "stroke-opacity": 1
    },
    //'.marker-arrowhead':{fill:'magenta',d: 'M 14 0 L 0 7 L 14 14 z',stroke: 'rgba(0,0,0,0)'},
    ".marker-arrowhead": {
      fill: "magenta",
      d: "M 0 0 L 0 0 L 0 0 z",
      stroke: "rgba(0,0,0,0)"
    },
    ".marker-target": {
      stroke: "rbga(0,0,0,0)",
      fill: FaultAnalysisLinkColors[0],
      d: "M 14 0 L 0 7 L 14 14 z",
      "fill-opacity": 1
    },
    ".marker-source": {
      stroke: "rbga(0,0,0,0)",
      fill: FaultAnalysisLinkColors[0],
      d: "M 0, 0 m -5, 0 a 5,5 0 1,0 10,0 a 5,5 0 1,0 -10,0",
      "fill-opacity": 1
    }
  },
  linkHighlightedColor2: {
    ".connection": {
      stroke: FaultAnalysisLinkColors[1],
      d: "M 30 140 320 140",
      /* 'stroke-width':"4"  , */ "stroke-dasharray": "10 2",
      "stroke-opacity": 1
    },
    //'.marker-arrowhead':{fill:'magenta',d: 'M 14 0 L 0 7 L 14 14 z',stroke: 'rgba(0,0,0,0)'},
    ".marker-arrowhead": {
      fill: "magenta",
      d: "M 0 0 L 0 0 L 0 0 z",
      stroke: "rgba(0,0,0,0)"
    },
    ".marker-target": {
      stroke: "rbga(0,0,0,0)",
      fill: FaultAnalysisLinkColors[1],
      d: "M 14 0 L 0 7 L 14 14 z",
      "fill-opacity": 1
    },
    ".marker-source": {
      stroke: "rbga(0,0,0,0)",
      fill: FaultAnalysisLinkColors[1],
      d: "M 0, 0 m -5, 0 a 5,5 0 1,0 10,0 a 5,5 0 1,0 -10,0",
      "fill-opacity": 1
    }
  },
  linkHighlightedColor3: {
    ".connection": {
      stroke: FaultAnalysisLinkColors[2],
      d: "M 30 140 320 140",
      /* 'stroke-width':"4"  , */ "stroke-dasharray": "10 2",
      "stroke-opacity": 1
    },
    //'.marker-arrowhead':{fill:'magenta',d: 'M 14 0 L 0 7 L 14 14 z',stroke: 'rgba(0,0,0,0)'},
    ".marker-arrowhead": {
      fill: "magenta",
      d: "M 0 0 L 0 0 L 0 0 z",
      stroke: "rgba(0,0,0,0)"
    },
    ".marker-target": {
      stroke: "rbga(0,0,0,0)",
      fill: FaultAnalysisLinkColors[2],
      d: "M 14 0 L 0 7 L 14 14 z",
      "fill-opacity": 1
    },
    ".marker-source": {
      stroke: "rbga(0,0,0,0)",
      fill: FaultAnalysisLinkColors[2],
      d: "M 0, 0 m -5, 0 a 5,5 0 1,0 10,0 a 5,5 0 1,0 -10,0",
      "fill-opacity": 1
    }
  },
  linkHighlightedColor4: {
    ".connection": {
      stroke: FaultAnalysisLinkColors[3],
      d: "M 30 140 320 140",
      /* 'stroke-width':"4"  , */ "stroke-dasharray": "10 2",
      "stroke-opacity": 1
    },
    //'.marker-arrowhead':{fill:'magenta',d: 'M 14 0 L 0 7 L 14 14 z',stroke: 'rgba(0,0,0,0)'},
    ".marker-arrowhead": {
      fill: "magenta",
      d: "M 0 0 L 0 0 L 0 0 z",
      stroke: "rgba(0,0,0,0)"
    },
    ".marker-target": {
      stroke: "rbga(0,0,0,0)",
      fill: FaultAnalysisLinkColors[3],
      d: "M 14 0 L 0 7 L 14 14 z",
      "fill-opacity": 1
    },
    ".marker-source": {
      stroke: "rbga(0,0,0,0)",
      fill: FaultAnalysisLinkColors[3],
      d: "M 0, 0 m -5, 0 a 5,5 0 1,0 10,0 a 5,5 0 1,0 -10,0",
      "fill-opacity": 1
    }
  },
  linkHighlightedColor5: {
    ".connection": {
      stroke: "red",
      d: "M 30 140 320 140",
      /* 'stroke-width':"4"  , */ "stroke-dasharray": "10 2",
      "stroke-opacity": 1
    },
    //'.marker-arrowhead':{fill:'magenta',d: 'M 14 0 L 0 7 L 14 14 z',stroke: 'rgba(0,0,0,0)'},
    ".marker-arrowhead": {
      fill: "magenta",
      d: "M 0 0 L 0 0 L 0 0 z",
      stroke: "rgba(0,0,0,0)"
    },
    ".marker-target": {
      stroke: "rbga(0,0,0,0)",
      fill: "red",
      d: "M 14 0 L 0 7 L 14 14 z",
      "fill-opacity": 1
    }
    //	'.marker-source': { stroke: 'rbga(0,0,0,0)', fill: FaultAnalysisLinkColors[0], d: 'M 0, 0 m -5, 0 a 5,5 0 1,0 10,0 a 5,5 0 1,0 -10,0','fill-opacity':1  }
  },
  linkHighlightedColor6: {
    ".connection": {
      stroke: FaultAnalysisLinkColors[5],
      d: "M 30 140 320 140",
      /* 'stroke-width':"4"  , */ "stroke-dasharray": "10 2",
      "stroke-opacity": 1
    },
    //'.marker-arrowhead':{fill:'magenta',d: 'M 14 0 L 0 7 L 14 14 z',stroke: 'rgba(0,0,0,0)'},
    ".marker-arrowhead": {
      fill: "magenta",
      d: "M 0 0 L 0 0 L 0 0 z",
      stroke: "rgba(0,0,0,0)"
    },
    ".marker-target": {
      stroke: "rbga(0,0,0,0)",
      fill: FaultAnalysisLinkColors[5],
      d: "M 14 0 L 0 7 L 14 14 z",
      "fill-opacity": 1
    },
    ".marker-source": {
      stroke: "rbga(0,0,0,0)",
      fill: FaultAnalysisLinkColors[5],
      d: "M 0, 0 m -5, 0 a 5,5 0 1,0 10,0 a 5,5 0 1,0 -10,0",
      "fill-opacity": 1
    }
  },
  ReversedlinkHighlightedColor1: {
    ".connection": {
      stroke: FaultAnalysisLinkColors[0],
      d: "M 30 140 320 140",
      /* 'stroke-width':"4"  , */ "stroke-dasharray": "10 2",
      "stroke-opacity": 1
    },
    //'.marker-arrowhead':{fill:'magenta',d: 'M 14 0 L 0 7 L 14 14 z',stroke: 'rgba(0,0,0,0)'},
    ".marker-arrowhead": {
      fill: "magenta",
      d: "M 0 0 L 0 0 L 0 0 z",
      stroke: "rgba(0,0,0,0)"
    },
    ".marker-source": {
      stroke: "rbga(0,0,0,0)",
      fill: FaultAnalysisLinkColors[0],
      d: "M 14 0 L 0 7 L 14 14 z",
      "fill-opacity": 1
    },
    ".marker-target": {
      stroke: "rbga(0,0,0,0)",
      fill: FaultAnalysisLinkColors[0],
      d: "M 0, 0 m -5, 0 a 5,5 0 1,0 10,0 a 5,5 0 1,0 -10,0",
      "fill-opacity": 1
    }
  },
  ReversedlinkHighlightedColor2: {
    ".connection": {
      stroke: FaultAnalysisLinkColors[1],
      d: "M 30 140 320 140",
      /* 'stroke-width':"4"  , */ "stroke-dasharray": "10 2",
      "stroke-opacity": 1
    },
    //'.marker-arrowhead':{fill:'magenta',d: 'M 14 0 L 0 7 L 14 14 z',stroke: 'rgba(0,0,0,0)'},
    ".marker-arrowhead": {
      fill: "magenta",
      d: "M 0 0 L 0 0 L 0 0 z",
      stroke: "rgba(0,0,0,0)"
    },
    ".marker-source": {
      stroke: "rbga(0,0,0,0)",
      fill: FaultAnalysisLinkColors[1],
      d: "M 14 0 L 0 7 L 14 14 z",
      "fill-opacity": 1
    },
    ".marker-target": {
      stroke: "rbga(0,0,0,0)",
      fill: FaultAnalysisLinkColors[1],
      d: "M 0, 0 m -5, 0 a 5,5 0 1,0 10,0 a 5,5 0 1,0 -10,0",
      "fill-opacity": 1
    }
  },
  ReversedlinkHighlightedColor3: {
    ".connection": {
      stroke: FaultAnalysisLinkColors[2],
      d: "M 30 140 320 140",
      /*'stroke-width':"4" ,*/ "stroke-dasharray": "10 2",
      "stroke-opacity": 1
    },
    //'.marker-arrowhead':{fill:'magenta',d: 'M 14 0 L 0 7 L 14 14 z',stroke: 'rgba(0,0,0,0)'},
    ".marker-arrowhead": {
      fill: "magenta",
      d: "M 0 0 L 0 0 L 0 0 z",
      stroke: "rgba(0,0,0,0)"
    },
    ".marker-source": {
      stroke: "rbga(0,0,0,0)",
      fill: FaultAnalysisLinkColors[2],
      d: "M 14 0 L 0 7 L 14 14 z",
      "fill-opacity": 1
    },
    ".marker-target": {
      stroke: "rbga(0,0,0,0)",
      fill: FaultAnalysisLinkColors[2],
      d: "M 0, 0 m -5, 0 a 5,5 0 1,0 10,0 a 5,5 0 1,0 -10,0",
      "fill-opacity": 1
    }
  },
  ReversedlinkHighlightedColor4: {
    ".connection": {
      stroke: FaultAnalysisLinkColors[3],
      d: "M 30 140 320 140",
      /*'stroke-width':"4" ,*/ "stroke-dasharray": "10 2",
      "stroke-opacity": 1
    },
    //'.marker-arrowhead':{fill:'magenta',d: 'M 14 0 L 0 7 L 14 14 z',stroke: 'rgba(0,0,0,0)'},
    ".marker-arrowhead": {
      fill: "magenta",
      d: "M 0 0 L 0 0 L 0 0 z",
      stroke: "rgba(0,0,0,0)"
    },
    ".marker-source": {
      stroke: "rbga(0,0,0,0)",
      fill: FaultAnalysisLinkColors[3],
      d: "M 14 0 L 0 7 L 14 14 z",
      "fill-opacity": 1
    },
    ".marker-target": {
      stroke: "rbga(0,0,0,0)",
      fill: FaultAnalysisLinkColors[3],
      d: "M 0, 0 m -5, 0 a 5,5 0 1,0 10,0 a 5,5 0 1,0 -10,0",
      "fill-opacity": 1
    }
  },
  ReversedlinkHighlightedColor5: {
    ".connection": {
      stroke: "red",
      d: "M 30 140 320 140",
      /*'stroke-width':"4" ,*/ "stroke-dasharray": "10 2",
      "stroke-opacity": 1
    },
    //'.marker-arrowhead':{fill:'magenta',d: 'M 14 0 L 0 7 L 14 14 z',stroke: 'rgba(0,0,0,0)'},
    ".marker-arrowhead": {
      fill: "magenta",
      d: "M 0 0 L 0 0 L 0 0 z",
      stroke: "rgba(0,0,0,0)"
    },
    ".marker-source": {
      stroke: "rbga(0,0,0,0)",
      fill: "red",
      d: "M 14 0 L 0 7 L 14 14 z",
      "fill-opacity": 1
    }
    //'.marker-target': { stroke: 'rbga(0,0,0,0)', fill: FaultAnalysisLinkColors[0], d: 'M 0, 0 m -5, 0 a 5,5 0 1,0 10,0 a 5,5 0 1,0 -10,0','fill-opacity':1  }
  },
  ReversedlinkHighlightedColor6: {
    ".connection": {
      stroke: FaultAnalysisLinkColors[5],
      d: "M 30 140 320 140",
      /*'stroke-width':"4" ,*/ "stroke-dasharray": "10 2",
      "stroke-opacity": 1
    },
    //'.marker-arrowhead':{fill:'magenta',d: 'M 14 0 L 0 7 L 14 14 z',stroke: 'rgba(0,0,0,0)'},
    ".marker-arrowhead": {
      fill: "magenta",
      d: "M 0 0 L 0 0 L 0 0 z",
      stroke: "rgba(0,0,0,0)"
    },
    ".marker-source": {
      stroke: "rbga(0,0,0,0)",
      fill: FaultAnalysisLinkColors[5],
      d: "M 14 0 L 0 7 L 14 14 z",
      "fill-opacity": 1
    },
    ".marker-target": {
      stroke: "rbga(0,0,0,0)",
      fill: FaultAnalysisLinkColors[5],
      d: "M 0, 0 m -5, 0 a 5,5 0 1,0 10,0 a 5,5 0 1,0 -10,0",
      "fill-opacity": 1
    }
  },
  LineProtectionHighlightedLink: {
    ".connection": { "stroke-width": 7, "stroke-dasharray": "8 4" }
  },
  HighlightedLinkFaultAnalysis: {
    ".connection": { "stroke-width": 4, "stroke-dasharray": "8 4" }
  }
};

//Navbar Network Elements from which you take shapes
var stencilGraph = new joint.dia.Graph(),
  stencilPaper = new joint.dia.Paper({
    el: $("#netelemId"),
    // height: 5,
    // width: 100,
    model: stencilGraph,
    interactive: false
    /*gridSize: 15,
            drawGrid: true*/
  });

//Navbar General Shapes from which you take shapes
//var stencilGraphGS = new joint.dia.Graph,
//stencilPaperGS = new joint.dia.Paper({
//	el: $('#generalshapesId'),
//	height: 200,
//	width: 300,
//	model: stencilGraphGS,
//	interactive: false,
//});

var portCircleAttr = {
  fill: "#69696914",
  stroke: "#3c763d",
  "stroke-width": 0,
  r: 8,
  magnet: true
};

//Usage:
var portsVar = {
  groups: {
    north: {
      position: "top",
      attrs: {
        circle: portCircleAttr,
        text: { text: "Direction", stroke: "#3c763d", "stroke-width": 1 },
        ".joint-port-label": { stroke: "rbga(0,0,0,0)", fill: "rbga(0,0,0,0)" }
      },
      flag: 0
    },

    south: {
      position: "bottom",
      attrs: {
        circle: portCircleAttr,
        text: { text: "Direction", stroke: "#3c763d", "stroke-width": 1 },
        ".joint-port-label": { stroke: "rbga(0,0,0,0)", fill: "rbga(0,0,0,0)" }
      },
      flag: 0
    },

    east: {
      position: "right",
      attrs: {
        circle: portCircleAttr,
        text: { text: "Direction", stroke: "#3c763d", "stroke-width": 1 },
        ".joint-port-label": { stroke: "rbga(0,0,0,0)", fill: "rbga(0,0,0,0)" }
      },
      flag: 0
    },

    west: {
      position: "left",
      attrs: {
        circle: portCircleAttr,
        text: { text: "Direction", stroke: "#3c763d", "stroke-width": 1 },
        ".joint-port-label": { stroke: "rbga(0,0,0,0)", fill: "rbga(0,0,0,0)" }
      },
      flag: 0
    },

    ne: {
      /*   position: 'right',*/
      position: { name: "right", args: { dr: 0, dx: 0, dy: -35 } },
      attrs: {
        circle: portCircleAttr,
        text: { text: "Direction", stroke: "#3c763d", "stroke-width": 1 },
        ".joint-port-label": { stroke: "rbga(0,0,0,0)", fill: "rbga(0,0,0,0)" }
      },
      flag: 0
    },

    nw: {
      position: {
        name: "left",
        args: { dr: 0, dx: 0, dy: -35 }
      },
      attrs: {
        circle: portCircleAttr,
        text: { text: "Direction", stroke: "#3c763d", "stroke-width": 1 },
        ".joint-port-label": { stroke: "rbga(0,0,0,0)", fill: "rbga(0,0,0,0)" }
      },
      flag: 0
    },

    se: {
      position: {
        name: "right",
        args: { dr: 0, dx: 0, dy: 35 }
      },
      attrs: {
        circle: portCircleAttr,
        text: { text: "Direction", stroke: "#3c763d", "stroke-width": 1 },
        ".joint-port-label": { stroke: "rbga(0,0,0,0)", fill: "rbga(0,0,0,0)" }
      },
      flag: 0
    },

    sw: {
      position: {
        name: "left",
        args: { dr: 0, dx: 0, dy: 35 }
      },
      attrs: {
        circle: portCircleAttr,
        text: { text: "Direction", stroke: "#3c763d", "stroke-width": 1 },
        //'.joint-port-body':{stroke: 'rbga(0,0,0,0)', fill: 'rbga(0,0,0,0)','stroke-width':0},
        ".joint-port-label": { stroke: "rbga(0,0,0,0)", fill: "rbga(0,0,0,0)" }
      },
      flag: 0
    }
  }
};

/************************************************************************
 * Navbar Various Shapes Define Section wise
 ************************************************************************/
//Custom Link
//joint.shapes.NetworkLink.Link = joint.dia.Link.extend({

//defaults: {
//type: 'myclass.Link',
//attrs: { '.connection' : { 'stroke-width' :  5 }},
//smooth:true
//}
//});

//Network Elements

//roadm element

joint.shapes.devs.roadm = joint.shapes.basic.Generic.extend({
  // markup: '<g class="rotatable"><g class="scalable"><rect class="body"/></g><image/><text class="label"/><text class="props"/><g class="inPorts"/><g class="outPorts"/></g>',

  markup: nodeDefaultMarkup,
  defaults: joint.util.deepSupplement(
    {
      type: nptGlobals.NodeTypeROADM,
      size: nodeDefaultSize,
      attrs: {
        circle: nodeDefaultCircleAttr,
        ".app-cell__highlight": nodeDefaultHighlightAttr,
        text: nodeDefaultTextAttr,
        ".app-cell__title": nodeDefaultTitleTextAttr,
        image: {
          "xlink:href":
            "data:image/png;base64,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",
          width: 100,
          //width:50,
          height: 100,
          "ref-x": 0.5,
          "ref-y": 0.5,
          ref: "circle",
          "x-alignment": "middle",
          "y-alignment": "middle"
        }
      },
      nodeProperties: fGetNodePropertiesObj(nptGlobals.NodeTypeROADM)
    },
    joint.shapes.basic.Generic.prototype.defaults
  )
});

//joint.shapes.devs.roadmView = joint.shapes.devs.ModelView;

var roadm = new joint.shapes.devs.roadm({
  position: {
    x: 10,
    y: 45
  },
  size: nodeDefaultSize,
  ports: portsVar

  ///inPorts: ['c'],
  ///outPorts: ['d']
});

roadm.attr(".label/text", "R");
roadm.attr(".app-cell__title/text", "CDC");

//Hub element

joint.shapes.devs.hub = joint.shapes.basic.Generic.extend({
  // markup: '<g class="rotatable"><g class="scalable"><rect class="body"/></g><image/><text class="label"/><text class="props"/><g class="inPorts"/><g class="outPorts"/></g>',

  markup: nodeDefaultMarkup,
  defaults: joint.util.deepSupplement(
    {
      type: nptGlobals.NodeTypeHub,
      size: nodeDefaultSize,
      attrs: {
        circle: nodeDefaultCircleAttr,
        ".app-cell__highlight": nodeDefaultHighlightAttr,
        text: nodeDefaultTextAttr,
        ".app-cell__title": nodeDefaultTitleTextAttr,
        image: {
          "xlink:href":
            "data:image/png;base64,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",
          width: 100,
          //width:50,
          height: 100,
          "ref-x": 0.5,
          "ref-y": 0.5,
          ref: "circle",
          "x-alignment": "middle",
          "y-alignment": "middle"
        }
      },
      nodeProperties: fGetNodePropertiesObj(nptGlobals.NodeTypeHub)
    },
    joint.shapes.basic.Generic.prototype.defaults
  )
});

//joint.shapes.devs.roadmView = joint.shapes.devs.ModelView;

var hub = new joint.shapes.devs.hub({
  position: {
    x: 10,
    y: 570
  },
  size: nodeDefaultSize,

  ports: portsVar

  ///inPorts: ['c'],
  ///outPorts: ['d']
});

hub.attr(".label/text", "H");
hub.attr(".app-cell__title/text", "HUB");

joint.shapes.devs.Ila = joint.shapes.basic.Generic.extend({
  //  markup: `<g class="rotatable"><g class="scalable">
  //  <svg width='150px' height='150px'>
  //  <circle cx='75' cy='75' r='60' class="highlight"/>
  //    <image/>
  //    <circle r='13' cx='75' cy='120' fill='#34495e' />
  //    <text class="label"/>
  //  </svg></g></g>`,
  markup: nodeDefaultMarkup,

  // markup: nodeDefaultMarkup,
  defaults: joint.util.deepSupplement(
    {
      type: nptGlobals.NodeTypeILA,
      size: nodeDefaultSize,
      attrs: {
        circle: nodeDefaultCircleAttr,
        ".app-cell__highlight": nodeDefaultHighlightAttr,
        text: nodeDefaultTextAttr,
        ".app-cell__title": nodeDefaultTitleTextAttr,
        // circle: nodeDefaultCircleAttr,
        // '.app-cell__highlight':{
        // 	'stroke': 'white',
        // 	'stroke-width': 0,
        // 	'fill':'rgba(0,0,0,0)'
        // },
        // text:{
        // 	x:72,
        // 	y:123,
        // 	fill:'white',
        // 	'stroke': 'white',
        // 	'stroke-width': 1,
        // },
        image: {
          "xlink:href":
            "data:image/png;base64,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",
          width: 100,
          //width:50,
          height: 100,
          "ref-x": 0.5,
          "ref-y": 0.5,
          ref: "circle",
          "x-alignment": "middle",
          "y-alignment": "middle",
          stroke: "black",
          "stroke-width": 3
        }
      },
      nodeProperties: fGetNodePropertiesObj(nptGlobals.NodeTypeILA)
    },
    joint.shapes.basic.Generic.prototype.defaults
  )
});

var Ilanode = new joint.shapes.devs.Ila({
  position: {
    x: 10,
    y: 150
  },
  size: nodeDefaultSize

  // ports: portsVar,

  ///inPorts: ['c'],
  ///outPorts: ['d']
});

Ilanode.attr(".label/text", "I");
Ilanode.attr(".app-cell__title/text", "ILA");

// var Ilanode = new joint.shapes.devs.Model({
// //	id:id,
// 	markup: '<g class="rotatable"><g class="scalable"><path class="outer"/><text class="label"/><text class="props"/><g class="inPorts"/><g class="outPorts"/></g></g>',
// 	type:nptGlobals.NodeTypeILA,
// 	//inPorts: ['west'],
// 	//outPorts: ['east'],
// 	ports: portsVar,
// 	position: {
// 		x: 35,
// 		y: 165
// 	},
// 	size: {
// 		width: 40,
// 		height: 40
// 	},
// 	attrs: {
// 		path: { d: IlaNodePath,stroke:"#4f58a2",fill:"#f2f20099",'stroke-width':2},
// 		'.label':{
// 			text: nptGlobals.NodeTypeILADisplayName,
// 			fill:'#777',
// 			'stroke-width':1,
// 			stroke:'#777',
// 			'ref-y': 19,
// 			'ref-x':16
// 			},
// 			'.v-line':{'font-size':'15px','stroke-width':'2'},
// 			'.props': {
// 				text: '',
// 				fill:'white',
// 				/*'stroke-width':'',*/
// 				stroke:'#763a3a',
// 				'ref-y': -40,
// 				'ref-x':10,
// 				dy:'2em'
// 				/*'ref':'.outer',*/
// 				/*'font-size':'15px'*/
// 			},
// 			'.props .v-line': {
// 				'stroke-width':'2',
// 				'font-size':'20px'
// 			}

// 	},
// 	nodeProperties:{
// 		nodeId:'1',
// 		displayName:nptGlobals.NodeTypeILADisplayName,
// 		stationName:'new delhi',
// 		siteName:'chattarpur',
// 		gneFlag:0,
// 		vlanTag:'1',
// 		degree:0,
// 		directions:{
// 			east:'0',
// 			west:'0',
// 			north:'0',
// 			south:'0',
// 			ne:'0',
// 			nw:'0',
// 			se:'0',
// 			sw:'0'
// 		},
// 		numDirections:{
// 			east:'0',
// 			west:'0',
// 			north:'0',
// 			south:'0',
// 			ne:'0',
// 			nw:'0',
// 			se:'0',
// 			sw:'0'
// 		},
// 		faultNode:'0',
// 		emsip:'0.0.0.0',
// 		subnet:'255.255.255.0',
// 		gateway:'192.168.115.1',
// 		ipv6:'fe80::2e0:4cff:fe36:90a/64',
// 		capacity:3,
// 		direction:'West',
// 		opticalReach:'Long Haul',
// 		BrownFieldNode:'0' //1-Yes , 0-No
// 	}

// 	//ports: portsVar
// });

//TE element

joint.shapes.devs.TE = joint.shapes.basic.Generic.extend({
  // markup: '<g class="rotatable"><g class="scalable"><rect class="body"/></g><image/><text class="label"/><text class="props"/><g class="inPorts"/><g class="outPorts"/></g>',

  markup: nodeDefaultMarkup,
  defaults: joint.util.deepSupplement(
    {
      type: nptGlobals.NodeTypeTE,
      size: nodeDefaultSize,
      attrs: {
        circle: nodeDefaultCircleAttr,
        ".app-cell__highlight": nodeDefaultHighlightAttr,
        text: nodeDefaultTextAttr,
        ".app-cell__title": nodeDefaultTitleTextAttr,
        image: {
          "xlink:href":
            "data:image/png;base64,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",
          width: 100,
          //width:50,
          height: 100,
          "ref-x": 0.5,
          "ref-y": 0.5,
          ref: "circle",
          "x-alignment": "middle",
          "y-alignment": "middle",
          stroke: "black",
          "stroke-width": 3
        }
      },
      nodeProperties: fGetNodePropertiesObj(nptGlobals.NodeTypeTE)
    },
    joint.shapes.basic.Generic.prototype.defaults
  )
});

//TEst element

joint.shapes.devs.TestNode = joint.shapes.basic.Generic.extend({
  markup: nodeDefaultMarkup,

  defaults: joint.util.deepSupplement(
    {
      type: nptGlobals.NodeTypeTE,
      size: nodeDefaultSize,
      attrs: {
        circle: nodeDefaultCircleAttr,
        ".app-cell__highlight": nodeDefaultHighlightAttr,
        text: nodeDefaultTextAttr,
        ".app-cell__title": nodeDefaultTitleTextAttr,
        image: {
          "xlink:href":
            "data:image/png;base64,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",
          width: 100,
          //width:50,
          height: 100,
          "ref-x": 0.5,
          "ref-y": 0.5,
          ref: "circle",
          "x-alignment": "middle",
          "y-alignment": "middle",
          stroke: "black",
          "stroke-width": 3
        }
      },
      nodeProperties: fGetNodePropertiesObj(nptGlobals.NodeTypeTE)
    },
    joint.shapes.basic.Generic.prototype.defaults
  )
});

//joint.shapes.devs.roadmView = joint.shapes.devs.ModelView;

function fGetNodePropertiesObj(nodeType) {
  let npObj = JSON.parse(JSON.stringify(nodePropertiesObj));
  switch (nodeType) {
    case nptGlobals.NodeTypeTE:
      npObj.displayName = nptGlobals.NodeTypeTEDisplayName;
      break;
    case nptGlobals.NodeTypeHub:
      npObj.displayName = nptGlobals.NodeTypeHubDisplayName;
      break;
    case nptGlobals.NodeTypeROADM:
      npObj.displayName = nptGlobals.NodeTypeROADMDisplayName;
      break;
    case nptGlobals.NodeTypeCDROADM:
      npObj.displayName = nptGlobals.NodeTypeCDROADMDisplayName;
      break;
    case nptGlobals.NodeTypeTwoDegreeRoadm:
      npObj.displayName = nptGlobals.NodeTypeTwoDegreeRoadmDisplayName;
      break;
    case nptGlobals.NodeTypeILA:
      npObj.displayName = nptGlobals.NodeTypeILADisplayName;
      break;
    case nptGlobals.NodeTypePotp:
      npObj.displayName = nptGlobals.NodeTypePotpDisplayName;
      break;
    default:
      npObj.displayName = "Not Defined";
      break;
  }

  return npObj;
}

var TENode = new joint.shapes.devs.TE({
  position: {
    x: 10,
    y: 255
  },
  size: nodeDefaultSize,
  ports: portsVar

  ///inPorts: ['c'],
  ///outPorts: ['d']
});

TENode.attr(".label/text", "T");
TENode.attr(".app-cell__title/text", "TE");

//Two Degree Roadm element

joint.shapes.devs.BSRoadm = joint.shapes.basic.Generic.extend({
  // markup: '<g class="rotatable"><g class="scalable"><rect class="body"/></g><image/><text class="label"/><text class="props"/><g class="inPorts"/><g class="outPorts"/></g>',

  markup: nodeDefaultMarkup,
  defaults: joint.util.deepSupplement(
    {
      type: nptGlobals.NodeTypeTwoDegreeRoadm,
      size: nodeDefaultSize,
      attrs: {
        circle: nodeDefaultCircleAttr,
        ".app-cell__highlight": nodeDefaultHighlightAttr,
        text: nodeDefaultTextAttr,
        ".app-cell__title": nodeDefaultTitleTextAttr,
        image: {
          "xlink:href":
            "data:image/png;base64,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",
          width: 100,
          //width:50,
          height: 100,
          "ref-x": 0.5,
          "ref-y": 0.5,
          ref: "circle",
          "x-alignment": "middle",
          "y-alignment": "middle"
        }
      },
      nodeProperties: fGetNodePropertiesObj(nptGlobals.NodeTypeTwoDegreeRoadm)
    },
    joint.shapes.basic.Generic.prototype.defaults
  )
});

//joint.shapes.devs.roadmView = joint.shapes.devs.ModelView;

var roadmTwoDegree = new joint.shapes.devs.BSRoadm({
  position: {
    x: 10,
    y: 360
  },
  size: nodeDefaultSize,
  ports: portsVar
  ///inPorts: ['c'],
  ///outPorts: ['d']
});
roadmTwoDegree.attr(".label/text", "B");
roadmTwoDegree.attr(".app-cell__title/text", "B&S");

//roadm element

joint.shapes.devs.cdroadm = joint.shapes.basic.Generic.extend({
  // markup: '<g class="rotatable"><g class="scalable"><rect class="body"/></g><image/><text class="label"/><text class="props"/><g class="inPorts"/><g class="outPorts"/></g>',

  markup: nodeDefaultMarkup,
  defaults: joint.util.deepSupplement(
    {
      type: nptGlobals.NodeTypeCDROADM,
      size: nodeDefaultSize,
      attrs: {
        circle: nodeDefaultCircleAttr,
        ".app-cell__highlight": nodeDefaultHighlightAttr,
        text: nodeDefaultTextAttr,
        ".app-cell__title": nodeDefaultTitleTextAttr,
        image: {
          "xlink:href":
            "data:image/png;base64,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",
          width: 100,
          //width:50,
          height: 100,
          "ref-x": 0.5,
          "ref-y": 0.5,
          ref: "circle",
          "x-alignment": "middle",
          "y-alignment": "middle"
        }
      },
      nodeProperties: fGetNodePropertiesObj(nptGlobals.NodeTypeCDROADM)
    },
    joint.shapes.basic.Generic.prototype.defaults
  )
});

//joint.shapes.devs.roadmView = joint.shapes.devs.ModelView;

var cdroadm = new joint.shapes.devs.cdroadm({
  position: {
    x: 10,
    y: 465
  },
  size: nodeDefaultSize,

  ports: portsVar

  //inPorts: ['c'],
  //outPorts: ['d']
});

cdroadm.attr(".label/text", "C");
cdroadm.attr(".app-cell__title/text", "CD-R");

joint.shapes.devs.potp = joint.shapes.basic.Generic.extend({
  // markup: '<g class="rotatable"><g class="scalable"><rect class="body"/></g><image/><text class="label"/><text class="props"/><g class="inPorts"/><g class="outPorts"/></g>',

  markup: nodePotpMarkup,
  defaults: joint.util.deepSupplement(
    {
      type: nptGlobals.NodeTypePotp,
      size: nodeDefaultSize,
      attrs: {
        ".app-cell__highlight": nodeDefaultHighlightAttr,
        text: nodePotpTextAttr,
        // ".app-cell__title": nodePotpTitleTextAttr,
        ".node-label .v-line": {
          "font-size": "22px",
          "font-family": "Ultra",
          "font-weight": "100"
        },
        image: {
          "xlink:href":
            "data:image/png;base64,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",
          width: 100,
          //width:50,
          height: 100,
          "ref-x": 0.5,
          "ref-y": 0.5,
          ref: "circle",
          "x-alignment": "middle",
          "y-alignment": "middle"
        }
      },
      nodeProperties: fGetNodePropertiesObj(nptGlobals.NodeTypePotp)
    },
    joint.shapes.basic.Generic.prototype.defaults
  )
});

//joint.shapes.devs.roadmView = joint.shapes.devs.ModelView;

var potp = new joint.shapes.devs.potp({
  position: {
    x: 10,
    y: 685
  },
  size: nodeDefaultSize,
  ports: portsVar
});

potp.attr(".label/text", "P");
potp.attr(".app-cell__title/text", "POTP");

var roadmBkg = new joint.shapes.devs.Model({
  position: {
    x: 25,
    y: 65
  },
  size: {
    width: 80,
    height: 80
  },
  attrs: {
    rect: {
      fill: "rgba(0,0,0,0",
      stroke: "rgba(0,0,0,0"
      // 'stroke-width':0.5,
      // 'stroke-opacity':0.2,
      // 'stroke-dasharray':'5,5'
    },
    ".v-line": {
      text: nptGlobals.NodeTypeROADMDisplayName,
      dy: "10em",
      ref: "rect",
      "stroke-width": "0.2"
    },
    ".label": {
      text: nptGlobals.NodeTypeROADMDisplayName,
      fill: "#777",
      "stroke-width": 1,
      stroke: "#777",
      "ref-y": 85,
      "ref-x": 40,
      "font-size": "18px"
    }
  }
});

var twoDegRoadmBkg = roadmBkg.clone();
console.log("twoDegRoadmBkg");
console.log(twoDegRoadmBkg);
twoDegRoadmBkg.attributes.position.y = 380;
twoDegRoadmBkg.attributes.attrs[".label"].text = "B & Select ROADM";

var ilaBkg = roadmBkg.clone();
//console.log("twoDegRoadmBkg");
//console.log(twoDegRoadmBkg);
ilaBkg.attributes.position.y = 170;
ilaBkg.attributes.attrs[".label"].text = "ILA";

var teBkg = roadmBkg.clone();
//console.log("twoDegRoadmBkg");
//console.log(twoDegRoadmBkg);
teBkg.attributes.position.y = 275;
teBkg.attributes.attrs[".label"].text = "TE";

var hubBkg = roadmBkg.clone();
//console.log("twoDegRoadmBkg");
//console.log(twoDegRoadmBkg);
hubBkg.attributes.position.y = 590;
hubBkg.attributes.attrs[".label"].text = "HUB";

var cdRoadmBkg = roadmBkg.clone();
//console.log("twoDegRoadmBkg");
//console.log(twoDegRoadmBkg);
cdRoadmBkg.attributes.position.y = 485;
cdRoadmBkg.attributes.attrs[".label"].text = "CD Roadm";

var potpBkg = roadmBkg.clone();
//console.log("twoDegRoadmBkg");
//console.log(twoDegRoadmBkg);
potpBkg.attributes.position.y = 485;
potpBkg.attributes.attrs[".label"].text = nptGlobals.NodeTypePotpDisplayName;

/*
var roadm = new joint.shapes.devs.Model({
    position: { x: 40, y: 50 },
    size: { width: 60, height: 50 },
    inPorts: [''],
    outPorts: [''],
    attrs: {
        '.label': { text: 'ROADM', 'ref-x': .5, 'ref-y': .3},
        rect: { fill: '#2ECC71' },
        '.inPorts circle': { fill: '#16A085' },
        '.outPorts circle': { fill: '#E74C3C' }
    }
});

var hub = new joint.shapes.devs.Model({
    position: { x: 170, y: 50 },
    size: { width: 60, height: 50 },
    inPorts: [''],
    outPorts: [''],
    attrs: {
        '.label': { text: 'HUB', 'ref-x': .5, 'ref-y': .3 },
        rect: { fill: '#2ECC71' },
        '.inPorts circle': { fill: '#16A085' },
        '.outPorts circle': { fill: '#E74C3C' }
    }
});
 */

/************************************************************************
 * Add Objects into Respective Navbar Zone
 ************************************************************************/

//Network Elements
stencilGraph.addCells([
  /*roadmBkg,ilaBkg,teBkg,twoDegRoadmBkg,cdRoadmBkg,hubBkg,*/ roadm,
  Ilanode,
  TENode,
  roadmTwoDegree,
  cdroadm,
  hub,
  potp
]); /**ILA node commented : can be use for future reference */

//General Shapes
//stencilGraphGS.addCells([circle, image]);

//Network Links Adds
//stencilGraphLinks.addCells([link]);
