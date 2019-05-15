$("#generateChassisImagesTrigger").click(function() { 
	
	/*html2canvas(document.querySelector("#canvasId")).then(canvas => {
	    alert('inside html2canvas')
		document.body.appendChild(canvas)
		//$('#canvas').append(canvas);
	    console.log("==============canvas====================="+canvas.toDataURL())
		alert("url: "+canvas.toDataURL())
	});*/
	

	
/*	// get svg data
	var svg = `<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" version="1.1" id="v-2" width="800" height="780"><g id="v-3" class="joint-viewport"><g id="j_10" model-id="68d91c20-1b80-4e41-acf5-7eeb47a0ab1b" class="joint-theme-default joint-cell joint-type-devs joint-type-devs-roadm joint-element" data-type="devs.roadm" magnet="false" fill="#ffffff" stroke="none" transform="translate(409.921875,129)"><g class="rotatable" id="v-101" transform=""><g class="scalable" id="v-102" transform="scale(1,1.4)"><rect class="body" id="v-103" stroke="black" stroke-width="0" z-index="100" width="50" height="50" fill="rgba(0,0,0,0)"></rect></g><image id="v-108" xlink:href="images/servers.png" width="70" height="80" transform="translate(-10,-5)"></image><text class="label" id="v-104" y="0.8em" xml:space="preserve" fill="white" stroke-width="2" stroke="black" transform="translate(20,25)"><tspan id="v-111" dy="0em" x="0" class="v-line">1</tspan></text><text class="props" id="v-106" y="0.8em" display="none" xml:space="preserve" fill="white" stroke="#763a3a" transform="translate(10,-40)"><tspan id="v-112" dy="0em" x="0" class="v-line v-empty-line" stroke-width="2" font-size="15px" style="fill-opacity: 0; stroke-opacity: 0;">-</tspan></text><g class="inPorts"></g><g class="outPorts"></g><g class="joint-port" id="v-251" transform="matrix(1 0 0 1 0 35)"><circle class="joint-port-body" r="10" fill="#FFFFFF" stroke="#000000" id="v-249" port="all" port-group="all"></circle><text class="joint-port-label" fill="#000000" id="v-250" y=".3em" text-anchor="end" transform="matrix(1 0 0 1 -15 0)"></text></g><g class="joint-port" id="v-254" transform="matrix(1 0 0 1 25 0)"><circle class="joint-port-body" r="8" fill="#ffffff" stroke="#3c763d" id="v-252" port="north" port-group="north" stroke-width="2" magnet="true"></circle><text class="joint-port-label" fill="#000000" id="v-253" y="0" xml:space="preserve" stroke="#3c763d" stroke-width="1" text-anchor="middle" transform="matrix(1 0 0 1 0 -15)"><tspan id="v-276" dy="0em" x="0" class="v-line">N</tspan></text></g><g class="joint-port" id="v-257" transform="matrix(1 0 0 1 50 35)"><circle class="joint-port-body" r="8" fill="#ffffff" stroke="#3c763d" id="v-255" port="east" port-group="east" stroke-width="2" magnet="true"></circle><text class="joint-port-label" fill="#000000" id="v-256" y=".3em" xml:space="preserve" stroke="#3c763d" stroke-width="1" text-anchor="start" transform="matrix(1 0 0 1 15 0)"><tspan id="v-277" dy="0em" x="0" class="v-line">E</tspan></text></g><g class="joint-port" id="v-260" transform="matrix(1 0 0 1 0 35)"><circle class="joint-port-body" r="8" fill="#ffffff" stroke="#3c763d" id="v-258" port="west" port-group="west" stroke-width="2" magnet="true"></circle><text class="joint-port-label" fill="#000000" id="v-259" y=".3em" xml:space="preserve" stroke="#3c763d" stroke-width="1" text-anchor="end" transform="matrix(1 0 0 1 -15 0)"><tspan id="v-278" dy="0em" x="0" class="v-line">W</tspan></text></g><g class="joint-port" id="v-263" transform="matrix(1 0 0 1 25 70)"><circle class="joint-port-body" r="8" fill="#ffffff" stroke="#3c763d" id="v-261" port="south" port-group="south" stroke-width="2" magnet="true"></circle><text class="joint-port-label" fill="#000000" id="v-262" y=".6em" xml:space="preserve" stroke="#3c763d" stroke-width="1" text-anchor="middle" transform="matrix(1 0 0 1 0 15)"><tspan id="v-279" dy="0em" x="0" class="v-line">S</tspan></text></g><g class="joint-port" id="v-266" transform="matrix(1 0 0 1 50 0)"><circle class="joint-port-body" r="8" fill="#ffffff" stroke="#3c763d" id="v-264" port="ne" port-group="ne" stroke-width="2" magnet="true"></circle><text class="joint-port-label" fill="#000000" id="v-265" y="0" xml:space="preserve" stroke="#3c763d" stroke-width="1" text-anchor="middle" transform="matrix(1 0 0 1 0 -15)"><tspan id="v-280" dy="0em" x="0" class="v-line">NE</tspan></text></g><g class="joint-port" id="v-269" transform="matrix(1 0 0 1 0 0)"><circle class="joint-port-body" r="8" fill="#ffffff" stroke="#3c763d" id="v-267" port="nw" port-group="nw" stroke-width="2" magnet="true"></circle><text class="joint-port-label" fill="#000000" id="v-268" y=".3em" xml:space="preserve" stroke="#3c763d" stroke-width="1" text-anchor="end" transform="matrix(1 0 0 1 -15 0)"><tspan id="v-281" dy="0em" x="0" class="v-line">NW</tspan></text></g><g class="joint-port" id="v-272" transform="matrix(1 0 0 1 50 70)"><circle class="joint-port-body" r="8" fill="#ffffff" stroke="#3c763d" id="v-270" port="se" port-group="se" stroke-width="2" magnet="true"></circle><text class="joint-port-label" fill="#000000" id="v-271" y=".6em" xml:space="preserve" stroke="#3c763d" stroke-width="1" text-anchor="middle" transform="matrix(1 0 0 1 0 15)"><tspan id="v-282" dy="0em" x="0" class="v-line">SE</tspan></text></g><g class="joint-port" id="v-275" transform="matrix(1 0 0 1 0 70)"><circle class="joint-port-body" r="8" fill="#ffffff" stroke="#3c763d" id="v-273" port="sw" port-group="sw" stroke-width="2" magnet="true"></circle><text class="joint-port-label" fill="#000000" id="v-274" y=".6em" xml:space="preserve" stroke="#3c763d" stroke-width="1" text-anchor="middle" transform="matrix(1 0 0 1 0 15)"><tspan id="v-283" dy="0em" x="0" class="v-line">SW</tspan></text></g></g></g></g><defs id="v-4"></defs></svg>`; 
		
		///document.querySelector('#v-3');
	var xml = new XMLSerializer().serializeToString(svg);

	// make it base64
	var dataURL = btoa(xml);
	
	var b64Start = 'data:image/svg+xml;base64,';

	
	// prepend a "header"
	var image64 = b64Start + dataURL;
	
	alert("url: "+image64)*/
	
	var svgDoc = paper.svg;
	var serializer = new XMLSerializer();
	var svgString = serializer.serializeToString(svgDoc);
	alert('svgString '+svgString)
	console.log('svgString '+svgString)
	
	//generateCanvas();
	var json = JSON.stringify("hiten");
	$.ajax({
		type : "POST",
		contentType : "application/json",

		headers : {
			Accept : "application/json; charset=utf-8",
			"Content-Type" : "application/json; charset=utf-8"
		},

		url : "/generateChassisImagesTrigger",
		data : json,
		dataType : 'json',
		timeout : 600000,

		success : function(data) {
			console.log("success" + data);

			if (data == 1) {
				$("#modalLoadingContent img").attr('src',
						'images/success.png');
				$("#modalLoadingContent p").text(
						"Chassis Images Generated Successfully  ");
			} else if (data == 0) {
				$("#modalLoadingContent img").attr('src',
						'images/failure.png');
				$("#modalLoadingContent p").text(
						"Chassis Images Generated Failed ");
			}

		},
		error : function(e) {
			$("#modalLoadingContent img").attr('src',
					'images/failure.png');
			$("#modalLoadingContent p").text(
					"Error in Request Process :(  ");
		}
	});
	

});
var nodeJson;
var nodes;
var chassisData;

var jsonObje = {
	"nodes" : []
}
var scope = angular.element($("#chassisViewCanvasDiv")).scope();
function generateCanvas() {
	console.log("Started..............");

	/**
	 * Fetch the Data for Each Node
	 */

	/**
	 * Click the Tab
	 */
	 var button = document.getElementById("chassisViewLiId");	
	 button.click();	
	canvasProcessing();
}

function canvasProcessing() {
	
		myneChangeHandler();
		
		sleepFor(200);
//		alert(nodeJson);
		
		
	
		 sleepFor(200);
		for(var i=1;i<nodeJson.NodeData.length;i++){
			var eachNode = nodeJson.NodeData[i];
			var nodeid = eachNode.NodeId;
			var chassisData = eachNode;
			chassis(chassisData);
			CanvasFetch(i);
		}
	/**For converting Json to String*/
	var json = JSON.stringify(jsonObje);
	console.log(json);

	/**Ajax post for sending the json created to the controller*/
	
			$.ajax({
				type : "POST",
				contentType : "application/json",

				headers : {
					Accept : "application/json; charset=utf-8",
					"Content-Type" : "application/json; charset=utf-8"
				},

				url : "/generateChassisImagesTrigger",
				data : json,
				dataType : 'json',
				timeout : 600000,

				success : function(data) {
					console.log("success" + data);

					if (data == 1) {
						$("#modalLoadingContent img").attr('src',
								'images/success.png');
						$("#modalLoadingContent p").text(
								"Chassis Images Generated Successfully  ");
					} else if (data == 0) {
						$("#modalLoadingContent img").attr('src',
								'images/failure.png');
						$("#modalLoadingContent p").text(
								"Chassis Images Generated Failed ");
					}

				},
				error : function(e) {
					$("#modalLoadingContent img").attr('src',
							'images/failure.png');
					$("#modalLoadingContent p").text(
							"Error in Request Process :(  ");
				}
			});

}

/**Function to get the canvas from the view by its id(chassisFrontCanvas)*/
function CanvasFetch(i) {
	var scope = angular.element($("#chassisViewCanvasDiv")).scope();

	
	var chassisString = document.getElementById('chassisFrontCanvas');
	var dataURL = chassisString.toDataURL();
	

	/**Making Json to Store The String*/
	var nodesid = {
		nodeId : i,
		"racklist" : [],

	}
	var racks = {
		rackId : 1,
		"String" : dataURL,

	}
	nodesid.racklist.push(racks);
	jsonObje.nodes.push(nodesid);

}
function sleepFor(sleepDuration) {
	var now = new Date().getTime();
	while (new Date().getTime() < now + sleepDuration) { /* do nothing */
	}
}
/**function for drawing chassis on the canvas*/
function chassis(chassisDbData) {
	var scope = angular.element($("#chassisViewCanvasDiv")).scope();
	scope.rackViewIndex = 1;
	/**DBG=> Sample data for all nodes */
	/**for node 1*/
	/*if (i==1){
		scope.chassisViewDbData  ={"chassis":{"specs":{"nodeType":2,"maxRackPerChassis":1,"rackList":[{"maxSubRackPerRack":3,"subRackList":[{"yCableFlag":0,"cardList":[],"maxSlotPerSubRack":14,"subRackId":1,"rackId":1},{"yCableFlag":0,"cardList":[{"typeName":"ILA","maxSlotPerCard":1,"slotId":1,"subRackId":2,"rackId":1},{"typeName":"VOIP","maxSlotPerCard":1,"slotId":4,"subRackId":2,"rackId":1},{"typeName":"OCM1x2","maxSlotPerCard":1,"slotId":5,"subRackId":2,"rackId":1},{"typeName":"CSCC","maxSlotPerCard":1,"slotId":6,"subRackId":2,"rackId":1},{"typeName":"SUPY","maxSlotPerCard":1,"slotId":7,"subRackId":2,"rackId":1},{"typeName":"SUPY","maxSlotPerCard":1,"slotId":8,"subRackId":2,"rackId":1},{"typeName":"CSCC","maxSlotPerCard":1,"slotId":9,"subRackId":2,"rackId":1},{"typeName":"ILA","maxSlotPerCard":1,"slotId":14,"subRackId":2,"rackId":1}],"maxSlotPerSubRack":14,"subRackId":2,"rackId":1},{"yCableFlag":0,"cardList":[],"maxSlotPerSubRack":14,"subRackId":3,"rackId":1}],"yCableList":[],"maxOuterTrayPerRack":14,"rackId":1}]}}};

	}
	/**for node 2*/
	/*else if(i==2){
		scope.chassisViewDbData  ={"chassis":{"specs":{"nodeType":8,"maxRackPerChassis":1,"rackList":[{"maxSubRackPerRack":3,"subRackList":[{"yCableFlag":0,"cardList":[{"typeName":"MPN(CGM)","maxSlotPerCard":2,"slotId":1,"subRackId":1,"rackId":1},{"typeName":"MPC","maxSlotPerCard":1,"slotId":6,"subRackId":1,"rackId":1},{"typeName":"MCS","maxSlotPerCard":2,"slotId":7,"subRackId":1,"rackId":1},{"typeName":"MPC","maxSlotPerCard":1,"slotId":9,"subRackId":1,"rackId":1}],"maxSlotPerSubRack":14,"subRackId":1,"rackId":1},{"yCableFlag":0,"cardList":[{"typeName":"PA\/BA","maxSlotPerCard":1,"slotId":1,"subRackId":2,"rackId":1},{"typeName":"WSS2x1x9","maxSlotPerCard":2,"slotId":2,"subRackId":2,"rackId":1},{"typeName":"VOIP","maxSlotPerCard":1,"slotId":4,"subRackId":2,"rackId":1},{"typeName":"OCM1x16","maxSlotPerCard":2,"slotId":5,"subRackId":2,"rackId":1},{"typeName":"CSCC","maxSlotPerCard":1,"slotId":6,"subRackId":2,"rackId":1},{"typeName":"SUPY","maxSlotPerCard":1,"slotId":7,"subRackId":2,"rackId":1},{"typeName":"SUPY","maxSlotPerCard":1,"slotId":8,"subRackId":2,"rackId":1},{"typeName":"CSCC","maxSlotPerCard":1,"slotId":9,"subRackId":2,"rackId":1},{"typeName":"EDFA ARRAY","maxSlotPerCard":2,"slotId":10,"subRackId":2,"rackId":1}],"maxSlotPerSubRack":14,"subRackId":2,"rackId":1},{"yCableFlag":0,"cardList":[],"maxSlotPerSubRack":14,"subRackId":3,"rackId":1}],"yCableList":[{"typeName":"YCable1x2Unit","slotId":1}],"maxOuterTrayPerRack":14,"rackId":1}]}}};

	}*/
	/**for node 3*/
	/*else if (i==3){
		scope.chassisViewDbData  ={"chassis":{"specs":{"nodeType":1,"maxRackPerChassis":1,"rackList":[{"maxSubRackPerRack":3,"subRackList":[{"yCableFlag":0,"cardList":[{"typeName":"MPN(CGM)","maxSlotPerCard":2,"slotId":1,"subRackId":1,"rackId":1},{"typeName":"MPC","maxSlotPerCard":1,"slotId":6,"subRackId":1,"rackId":1},{"typeName":"MPC","maxSlotPerCard":1,"slotId":9,"subRackId":1,"rackId":1}],"maxSlotPerSubRack":14,"subRackId":1,"rackId":1},{"yCableFlag":0,"cardList":[{"typeName":"PA\/BA","maxSlotPerCard":1,"slotId":1,"subRackId":2,"rackId":1},{"typeName":"WSS1x2","maxSlotPerCard":1,"slotId":2,"subRackId":2,"rackId":1},{"typeName":"VOIP","maxSlotPerCard":1,"slotId":4,"subRackId":2,"rackId":1},{"typeName":"OCM1x8","maxSlotPerCard":1,"slotId":5,"subRackId":2,"rackId":1},{"typeName":"CSCC","maxSlotPerCard":1,"slotId":6,"subRackId":2,"rackId":1},{"typeName":"SUPY","maxSlotPerCard":1,"slotId":7,"subRackId":2,"rackId":1},{"typeName":"CSCC","maxSlotPerCard":1,"slotId":9,"subRackId":2,"rackId":1}],"maxSlotPerSubRack":14,"subRackId":2,"rackId":1},{"yCableFlag":0,"cardList":[],"maxSlotPerSubRack":14,"subRackId":3,"rackId":1}],"yCableList":[{"typeName":"Odd Mux\/ Demux Unit for 2D TE & 2D ROADM","slotId":1},{"typeName":"Even Mux\/Demux Unit  for 2D TE & 2D ROADM","slotId":2},{"typeName":"YCable1x2Unit","slotId":3}],"maxOuterTrayPerRack":14,"rackId":1}]}}};

	}*/

	scope.chassisViewDbData = chassisDbData;
	/**DBG=> Sample data for all nodes  combined*/
//	scope.chassisViewDbData = {"chassis":{"specs":{"nodeType":8,"maxRackPerChassis":1,"rackList":[{"maxSubRackPerRack":3,"subRackList":[{"yCableFlag":0,"cardList":[{"typeName":"MPN(CGM)","maxSlotPerCard":2,"slotId":1,"subRackId":1,"rackId":1},{"typeName":"MPN(CGM)","maxSlotPerCard":2,"slotId":4,"subRackId":1,"rackId":1},{"typeName":"MPC","maxSlotPerCard":1,"slotId":6,"subRackId":1,"rackId":1},{"typeName":"MCS","maxSlotPerCard":2,"slotId":7,"subRackId":1,"rackId":1},{"typeName":"MPC","maxSlotPerCard":1,"slotId":9,"subRackId":1,"rackId":1},{"typeName":"MPN(CGM)","maxSlotPerCard":2,"slotId":10,"subRackId":1,"rackId":1},{"typeName":"MPN(CGM)","maxSlotPerCard":2,"slotId":13,"subRackId":1,"rackId":1}],"maxSlotPerSubRack":14,"subRackId":1,"rackId":1},{"yCableFlag":0,"cardList":[{"typeName":"PA/BA","maxSlotPerCard":1,"slotId":1,"subRackId":2,"rackId":1},{"typeName":"WSS2x1x9","maxSlotPerCard":2,"slotId":2,"subRackId":2,"rackId":1},{"typeName":"VOIP","maxSlotPerCard":1,"slotId":4,"subRackId":2,"rackId":1},{"typeName":"OCM1x16","maxSlotPerCard":2,"slotId":5,"subRackId":2,"rackId":1},{"typeName":"CSCC","maxSlotPerCard":1,"slotId":6,"subRackId":2,"rackId":1},{"typeName":"SUPY","maxSlotPerCard":1,"slotId":7,"subRackId":2,"rackId":1},{"typeName":"SUPY","maxSlotPerCard":1,"slotId":8,"subRackId":2,"rackId":1},{"typeName":"CSCC","maxSlotPerCard":1,"slotId":9,"subRackId":2,"rackId":1},{"typeName":"EDFA ARRAY","maxSlotPerCard":2,"slotId":10,"subRackId":2,"rackId":1},{"typeName":"WSS2x1x9","maxSlotPerCard":2,"slotId":12,"subRackId":2,"rackId":1},{"typeName":"PA/BA","maxSlotPerCard":1,"slotId":14,"subRackId":2,"rackId":1}],"maxSlotPerSubRack":14,"subRackId":2,"rackId":1},{"yCableFlag":0,"cardList":[],"maxSlotPerSubRack":14,"subRackId":3,"rackId":1}],"yCableList":[{"typeName":"YCable1x2Unit","slotId":1}],"maxOuterTrayPerRack":14,"rackId":1}]}}};
	scope.trackVar = {}; //Default initialisation for all tracking variables
	scope.trackVar.selectedRack = 1;
	init(scope);
	sleepFor(200);

}
/**function for fetching data from the database for chassis generation*/
function myneChangeHandler() {

	var scope = angular.element($("#chassisViewCanvasDiv")).scope();


	/**carry out the ajax call for the response*/
	$.ajax({
	    type: "get",
	    contentType: "application/json",
	
	    headers: {          
	        Accept : "application/json; charset=utf-8",         
	       "Content-Type": "application/json; charset=utf-8"   
	    } ,    
	 
	    url: "/getChassisJsonAllNodeDataRequest",

	    dataType: 'json',
	   timeout: 600000,
	
	   success: function (data) {

			   console.log(JSON.stringify(data));
			   nodeJson =data;
			   nodes = data.NodeData;
			  	   },
	   error: function (e) {
			    
		   }
		});
}
