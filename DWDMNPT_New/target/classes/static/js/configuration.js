
const tcmConfigSrc=[
	{ value: 0, text: nptGlobals.NoneStr },
	{ value: 1, text: nptGlobals.TcmPriorityOperational },
	{ value: 2, text: nptGlobals.TcmPriorityTransparent },
	{ value: 3, text: nptGlobals.TcmPriorityMonitor },
	{ value: 4, text: nptGlobals.TcmPriorityNonIntrusiveMonitor },
]

/************************************************************************
 * Modal template for dynamic injection on Index.html onLoad
 ************************************************************************/
var ConfigurationModalTemplate = `<div id="configurationModal" class="modal fade " role="dialog">
					<div class="modal-dialog genericModalDialog">

						<!-- Modal content-->
						<div class="modal-content genericModalContent"
							id="modalContentlinkNodeConfig">
							<!-- <div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						</div> -->
								<div class="modal-body genericModalBody"
									id="modalBodylinkNodeConfig">
									<div class="row">
										<div class="col-md-12 col-sm-12 zeroPadding">
											<div class="col-md-6 col-sm-6 genericModalBodyHeadPanelHeading">
												<p id="linkNodeConfigViewMainHeading">
													DETAILED NODE CONFIGURATION												
												</p>
												<button type="button" class="btn btn--danger pull-right"
														data-dismiss="modal">
														<i class="fa fa-times" aria-hidden="true"></i>
													</button>	
											</div>
										</div>
										<div class="col-md-12 col-sm-12 genericModalTabMenuPanel">
										
										<ul class="nav nav-pills ">
										    <li class="active"><a data-toggle="pill" href="#mpnConfigurationTab">MPN</a></li>
										    <!-- <li><a data-toggle="pill" href="#wssConfigurationTab">WSS</a></li>
										    <li><a data-toggle="pill" href="#mcsConfigurationTab">MCS</a></li>
										    <li><a data-toggle="pill" href="#ampConfigurationTab">AMP</a></li>
										    <li><a data-toggle="pill" href="#ocmConfigurationTab">OCM</a></li>-->													
										</ul>

									    </div>
										<div class="col-sm-12 col-md-12 genericModalBodyHeadPanel">
											<div class="col-md-12 col-sm-12">
												<form class="form-inline npt-form-inline" id="configurationModalInputForm">
												<div class="form-group">
													<label for="configurationInputNodeId">Select Node</label> <select
														class="form-control" id="configurationInputNodeId">
														<option value="0">All</option>
													</select>
												</div>
												<!-- <div class="form-group">
													<label for="configurationInputRack">Select Rack</label> <select
														class="form-control" id="configurationInputRack">
														<option value="0">All</option>
													</select>
												</div>
												<div class="form-group">
													<label for="configurationInputSubRack">Select SubRack</label> <select
														class="form-control" id="configurationInputSubRack">
														<option value="0">All</option>
													</select>
												</div>
												<div class="form-group">
													<label for="configurationInputCardId">Select SubRack</label> <select
														class="form-control" id="configurationInputCardId">
														<option value="0">All</option>
													</select>
												</div>
												<div class="form-group">
													<label for="configurationInputPort">Select Port</label> <select
														class="form-control" id="configurationInputPort">
														<option value="0">All</option>
													</select>
												</div> -->
												<div class="form-group">
													<button class="btn btn--default" type="button"
														id="getConfigurationInputFormTrigger">Get Configuration</button>
												</div>
											</form>
											</div>

										</div>
										<div class="col-sm-12 col-md-12 genericModalBodyContentPanel">
											<div class="tab-content">
												<div id="mpnConfigurationTab" class="tab-pane fade in active">
													<h3>Mpn Configuration</h3>
													<p>Click 'Get Configuration' for a Node Id.</p>
												</div>
												<div id="wssConfigurationTab" class="tab-pane fade">
													<h3>Wss Configuration</h3>
													<p>Click 'Get Configuration' for a Node Id.</p>
												</div>
											</div>
										</div>
									</div>
								</div>
								 <div class="modal-footer genericModalFooter">
								 </div>
						</div>

					</div>
				</div>`;


var nodeData = [1, 2, 3];
var cardType = nptGlobals.CardTypeMpn;

/************************************************************************
 * Fill nodeId for Mpn Tab in Configuration Modal
 ************************************************************************/

var fFillMpnNodeIdConfigurationModal = function () {
	var mpnNodeId/*=nodeData;*/;
	console.log("Inside fill nodes");
	console.log("CardType :", cardType);
	// var mpnConfigPostData=jsonPostObject();
	if (cardType == nptGlobals.CardTypeMpn) {
		overlayOn("Fetching Data",".body-overlay");
		let postJsonData = jsonPostObject();
		console.log("fFillMpnNodeIdConfigurationModal Post Obj ::", postJsonData);

		serverPostMessage("getMpnNodes", postJsonData)
			.then(function (data) {
				overlayOff("Fetch Success",".body-overlay");
				console.log("Mpn nodes Data Set Success", data);
				fillNodesInput(data);
			})
			.catch(function (e) {
				overlayOff("Fetch Failure",".body-overlay");
				console.log("fail", e);
			});

		// $.ajax({

		// 	type: "POST",
		//     contentType: "application/json",

		//     headers: {          
		//         Accept : "application/json; charset=utf-8",         
		//        "Content-Type": "application/json; charset=utf-8"   
		//     } ,       	 

		//     url: "/getMpnNodes",
		//     dataType: 'json',
		//     data: JSON.stringify(mpnConfigPostData),

		//    timeout: 600000,

		//    success: function (data) {
		// 	   console.log("Mpn nodes Data Set Success");
		// 	   console.log(data);
		// 	   fillNodesInput(data);
		// 	  // mpnNodeId=data;
		//    },

		//    error: function (e) {
		// 	   console.log("fail"+e);  }
		// 	});
	}
}

var fillNodesInput = function (data) {
	console.log("Inside fillNodes");
	var nodeSet = new Set();
	//Fetch unique nodeId and CardType from Complete Array
	for (var i = 0; i < data.NodeData.length; i++) {
		var nodeId = data.NodeData[i].NodeId;
		nodeSet.add(nodeId);
	}
	var mpnNodeId = Array.from(nodeSet);
	var options;
	for (var i = 0; i < mpnNodeId.length; i++) {
		options += `<option value="${mpnNodeId[i]}">${mpnNodeId[i]}</option>`;
	}
	$("#configurationModalInputForm #configurationInputNodeId").empty().append(options);
}

/************************************************************************
 * Fill data for Mpn Configuration in Mpn Tab (Modal)
 ************************************************************************/
var mpnConfigTab = function (data) {
	console.log(JSON.stringify(data));
	var circuitData = data.CircuitData;

	var template = `<table class="table-bordered table" id="mpnConfigurationTable" oncontextmenu="return false;">
		<thead>
		<tr>
		<th>Circuit</th>
		<th>Source Node</th>
		<th>Dest Node</th>
		<th>Configured Tpn</th>
		<th>Port</th>		
		<th>Tpn Location(R-S-C)</th>
		<th>Operator Specific</th>
		<th>TxTCM Mode</th>
		<th>TCM Act Value</th>
		<th>TxTCM Priority</th>		
		</tr>
		</thead>
		<tbody>`;

	var rowSpan, tempIndex, currentCircuitId;
	for (var i = 0; i < circuitData.length; i++) {
		var tpnInfo = circuitData[i].Rack + "-" + circuitData[i].SubRack + "-" + circuitData[i].Card;
//		var tpnNodeId = circuitData[i].NodeKey.split("_")[1]; //split by underscore
		var tpnNodeId = circuitData[i]["NodeId"]; //split by underscore
		var configuredTpn = "";
		//tempIndex=i;
		//currentCircuitId=circuitData[tempIndex++].CircuitId;
		//while(circuitData[tempIndex++].CircuitId==currentCircuitId)
		//	tempIndex++;

		//rowSpan=tempIndex-i;

		//console.log("rowSpan: "+rowSpan)

		template += `<tr>`;
		template += `<td><span class="badge badge-red">${circuitData[i].CircuitId}</span></td>
        	<td><span class="badge">${circuitData[i].SrcNodeId}</span></td>
        	<td><span class="badge">${circuitData[i].DestNodeId}</span></td>`;
		if (tpnNodeId == circuitData[i].SrcNodeId)
			configuredTpn = "Source Node";
		else configuredTpn = "Destination Node";
		template += `<td><span>${configuredTpn}</span></td>
        	<td><span class="badge badge-green">${circuitData[i].PortId}</span></td>
        	<td><span>${tpnInfo}</span></td>`;
		//Dynamic Fields
		template += `<td><span class="operatorSpecificMpnTab"></span></td>
			<td><span class="tcmModeMpnTab"></span></td>
			<td><span class="tcmValueMpnTab"></span></td>
			<td><span class="tcmPriorityMpnTab"></span></td>
			</tr>`;
	}

	template += `</tbody></table>`;

	$('#mpnConfigurationTab').empty().append(template);
	var updateButtonTemplate = `<button type="button" class="btn btn--default" id="mpnConfigurationFormSubmitTrgr">Save MPN Configuration</button>`;
	$('#configurationModal .genericModalFooter').empty().append(updateButtonTemplate);
	console.log("Now Initializing mpn config Data");
	initializeMpnConfigTab(circuitData);
}


/************************************************************************
 * Mpn Tab initialization in Configuration Modal
 ************************************************************************/

var initializeMpnConfigTab = function (mpnData) {
	$.fn.editable.defaults.mode = 'inline';
	var operatorSpecific, tcmMode, tcmValue, tcmPriority;

	for (i = 0; i < mpnData.length; i++) {
		operatorSpecific = mpnData[i].OperatorSpecific;
		tcmMode = mpnData[i].TxTCMMode;
		tcmValue = mpnData[i].TCMActValue;
		tcmPriority = mpnData[i].TxTCMPriority;

		$(".operatorSpecificMpnTab:eq(" + i + ")").attr("title", 'Edit ').tooltip().editable(
			{
				type: 'text',
				value: operatorSpecific,
				onblur: 'submit'
			});

		$(".tcmModeMpnTab:eq(" + i + ")").attr("title", 'Edit ').tooltip().editable(
			{
				type: 'select',
				value: tcmMode,
				source: tcmConfigSrc,
				onblur: 'submit'
			});

		$(".tcmValueMpnTab:eq(" + i + ")").attr("title", 'Edit ').tooltip().editable(
			{
				ttype: 'text',
				value: tcmValue,
				onblur: 'submit'

			});

		$(".tcmPriorityMpnTab:eq(" + i + ")").attr("title", 'Edit Priority \nNote:TcmPriority[0] is the highest priority and TcmPriority[7] is the lowest').tooltip().editable(
			{
				type: 'text',
				value: tcmPriority,
				onblur: 'submit',
				validate: function (value) {
					return fValidateTcmPriority(value, $(this));
				}
				//showbuttons:false
			});
	}
	// console.log($(".clientProtection"));

	//Called once for inline editing
	$('.footable').footable();

}

var fValidateTcmPriority = function (value, el) {
	value = $.trim(value);

	var priorityStr = value.split("-");
	var destNode, tpnNode;
	destNode = el.parent().parent().children().eq(2).find('span').html();
	tpnNode = el.parent().parent().children().eq(3).find('span').html();
	console.log("Dest Node:" + destNode + " tpnNode :" + tpnNode);
	if (priorityStr[7] != 8 && destNode != tpnNode) //source node
		return "TcmPriority[7] is fixed as Sectional Monitoring (SM) at source node";
	else if (priorityStr[0] != 8 && destNode == tpnNode) //sink node
		return "TcmPriority[0] is fixed as Sectional Monitoring (SM) at sink node";
}


/************************************************************************
 * Update Mpn Configuration  from Configuration Modal Mpn Tab Update Trigger
 ************************************************************************/
$(document).delegate("#mpnConfigurationFormSubmitTrgr", 'click', function (e) {
	//console.log("Node Property Save Modal");
	var myRows = [];
	var $headers = $("#mpnConfigurationTable th");
	//console.log("Headers-----");
	console.log($($headers));
	var $rows = $("#mpnConfigurationTable tbody tr").each(function (index) {
		//console.log("Row-----"+$(this));
		$cells = $(this).find("td");
		console.log("Columns\n -----");
		console.log($($cells));
		myRows[index] = {};
		$cells.each(function (cellIndex) {
			//console.log("Table column")
			//console.log($(this).find('span').hasClass('editable'))
			if ($(this).find('span').hasClass('editable') == true) {
				myRows[index][$($headers[cellIndex]).html()] = $(this).find('span').editable('getValue').undefined;
			}
			else {
				myRows[index][$($headers[cellIndex]).html()] = $(this).find('span').html();
			}

		});
	});


	/*************Put this in the object like you want and convert to JSON (Note: jQuery will also do this for you on the Ajax request)*********/
	var postJsonData = jsonPostObject();
	var mpnConfigurationUpdateData = {};
	mpnConfigurationUpdateData.ConfigurationData = myRows;
	mpnConfigurationUpdateData.SetInfo = { "CardType": cardType };
	postJsonData.mpnConfigUpdateData = mpnConfigurationUpdateData;
	//console.log(JSON.parse(myObj));
	console.log("Json Array of Node Config Modal\n" + JSON.stringify(mpnConfigurationUpdateData));

	ajaxSetConfigurationDataRequest(postJsonData);

});

/************************************************************************
 * On page load append modal template to index.html
 ************************************************************************/
(function () {
	//console.log(" LinkNodeConfig Modal Added to index.html");
	$(document.body).append(ConfigurationModalTemplate);
	//$("#linkNodeConfigModal").modal("show");
}());

/************************************************************************
 * Trigger for getting Configuration data from DB Modal 
 ************************************************************************/
$(document).delegate("#getConfigurationInputFormTrigger", 'click', function () {
	console.log("inside getConfigurationInputFormTrigger click");
	var nodeId = $("#configurationInputNodeId").val();
	var getConfigPostData = jsonPostObject();
	getConfigPostData.requestDetails = { NodeId: nodeId, CardType: cardType }; //cardType defined as global variable and is set when tab menu is clicked 
	ajaxGetConfigurationDataRequest(getConfigPostData);
	//data={"CircuitData":[{"SubRack":1,"OperatorSpecific":null,"SrcNodeId":"1","DemandId":"1","PortId":null,"Rack":1,"CardType":"MPN(CGM)","FecType":null,"CircuitId":"1","NodeKey":"1_1","TxTCMMode":null,"TrafficType":"Ethernet","GccValue":null,"Path":"1,2","TxTCMPriority":null,"TimDectMode":null,"GccType":null,"IsRevertive":null,"Card":1,"TCMActStatus":null,"TxSegment":null,"GccStatus":null,"TCMActValue":null,"ProtectionSubType":null,"DestNodeId":"2","FecStatus":null},{"SubRack":1,"OperatorSpecific":null,"SrcNodeId":"1","DemandId":"1","PortId":null,"Rack":1,"CardType":"MPN(CGM)","FecType":null,"CircuitId":"1","NodeKey":"1_2","TxTCMMode":null,"TrafficType":"Ethernet","GccValue":null,"Path":"1,2","TxTCMPriority":null,"TimDectMode":null,"GccType":null,"IsRevertive":null,"Card":1,"TCMActStatus":null,"TxSegment":null,"GccStatus":null,"TCMActValue":null,"ProtectionSubType":null,"DestNodeId":"2","FecStatus":null},{"SubRack":1,"OperatorSpecific":null,"SrcNodeId":"1","DemandId":"2","PortId":null,"Rack":1,"CardType":"MPN(CGM)","FecType":null,"CircuitId":"2","NodeKey":"1_1","TxTCMMode":null,"TrafficType":"Ethernet","GccValue":null,"Path":"1,2,3","TxTCMPriority":null,"TimDectMode":null,"GccType":null,"IsRevertive":null,"Card":10,"TCMActStatus":null,"TxSegment":null,"GccStatus":null,"TCMActValue":null,"ProtectionSubType":null,"DestNodeId":"3","FecStatus":null},{"SubRack":1,"OperatorSpecific":null,"SrcNodeId":"1","DemandId":"2","PortId":null,"Rack":1,"CardType":"MPN(CGM)","FecType":null,"CircuitId":"2","NodeKey":"1_3","TxTCMMode":null,"TrafficType":"Ethernet","GccValue":null,"Path":"1,2,3","TxTCMPriority":null,"TimDectMode":null,"GccType":null,"IsRevertive":null,"Card":1,"TCMActStatus":null,"TxSegment":null,"GccStatus":null,"TCMActValue":null,"ProtectionSubType":null,"DestNodeId":"3","FecStatus":null},{"SubRack":2,"OperatorSpecific":null,"SrcNodeId":"1","DemandId":"3","PortId":null,"Rack":1,"CardType":"MPN(CGM)","FecType":null,"CircuitId":"3","NodeKey":"1_1","TxTCMMode":null,"TrafficType":"Ethernet","GccValue":null,"Path":"1,2,3,4","TxTCMPriority":null,"TimDectMode":null,"GccType":null,"IsRevertive":null,"Card":10,"TCMActStatus":null,"TxSegment":null,"GccStatus":null,"TCMActValue":null,"ProtectionSubType":null,"DestNodeId":"4","FecStatus":null},{"SubRack":1,"OperatorSpecific":null,"SrcNodeId":"1","DemandId":"3","PortId":null,"Rack":1,"CardType":"MPN(CGM)","FecType":null,"CircuitId":"3","NodeKey":"1_4","TxTCMMode":null,"TrafficType":"Ethernet","GccValue":null,"Path":"1,2,3,4","TxTCMPriority":null,"TimDectMode":null,"GccType":null,"IsRevertive":null,"Card":1,"TCMActStatus":null,"TxSegment":null,"GccStatus":null,"TCMActValue":null,"ProtectionSubType":null,"DestNodeId":"4","FecStatus":null},{"SubRack":3,"OperatorSpecific":null,"SrcNodeId":"1","DemandId":"4","PortId":null,"Rack":1,"CardType":"MPN(CGM)","FecType":null,"CircuitId":"4","NodeKey":"1_1","TxTCMMode":null,"TrafficType":"Ethernet","GccValue":null,"Path":"1,2,3,4,5","TxTCMPriority":null,"TimDectMode":null,"GccType":null,"IsRevertive":null,"Card":1,"TCMActStatus":null,"TxSegment":null,"GccStatus":null,"TCMActValue":null,"ProtectionSubType":null,"DestNodeId":"5","FecStatus":null},{"SubRack":1,"OperatorSpecific":null,"SrcNodeId":"1","DemandId":"4","PortId":null,"Rack":1,"CardType":"MPN(CGM)","FecType":null,"CircuitId":"4","NodeKey":"1_5","TxTCMMode":null,"TrafficType":"Ethernet","GccValue":null,"Path":"1,2,3,4,5","TxTCMPriority":null,"TimDectMode":null,"GccType":null,"IsRevertive":null,"Card":1,"TCMActStatus":null,"TxSegment":null,"GccStatus":null,"TCMActValue":null,"ProtectionSubType":null,"DestNodeId":"5","FecStatus":null}]};
	//mpnConfigTab(data);
});

/************************************************************************
 * Trigger for setting cardType MPN and filling nodeId input field in Config Modal 
 ************************************************************************/
$(document).delegate("a[href='#mpnConfigurationTab']", 'click', function () {
	console.log("inside a #mpnConfigurationTab click");
	cardType = nptGlobals.CardTypeMpn; //set Card type to Mpn
	fFillMpnNodeIdConfigurationModal(); //Fill node id input field
	console.log("CardType :" + cardType);
});

/************************************************************************
 * Trigger for setting cardType WSS and filling nodeId input field in Config Modal 
 ************************************************************************/
$(document).delegate("a[href='#wssConfigurationTab']", 'click', function () {
	console.log("inside a #wssConfigurationTab click");
	cardType = window.CardTypeWss; //set Card type to Wss
	fFillMpnNodeIdConfigurationModal(); //Fill node id input field
	console.log("CardType :" + cardType);
});

/************************************************************************
 * Trigger for loading Configuration Modal 
 ************************************************************************/
$(document).delegate("#configurationModalTrgr", 'click', function () {
	console.log("inside configurationModalTrgr click");
	$("#configurationModal").modal("show");
	if (cardType == nptGlobals.CardTypeMpn)
		fFillMpnNodeIdConfigurationModal();
	else if (cardType == nptGlobals.CardTypeWss)
		fFillWssNodeIdConfigurationModal();
	//mpnConfigTab();//Load Fields

});

/************************************************************************
 * Ajax Call for saving configuration   
 * 
 ************************************************************************/
function ajaxSetConfigurationDataRequest(Data) {
	//If no type provided , then ajax 'GET' is called  
	/*if(typeof Type === "undefined") { Type = window.ajaxPosttRequest; }
	if(typeof Url === "undefined") { Url = "/saveConfiguration"; }
	if(typeof Data === "undefined") { Data = null; }
	*/

	$.ajax({

		type: "POST",
		contentType: "application/json",

		headers: {
			Accept: "application/json; charset=utf-8",
			"Content-Type": "application/json; charset=utf-8"
		},


		url: "/setConfiguration",
		data: JSON.stringify(Data),
		dataType: 'json',

		timeout: 600000,

		success: function (data) {
			console.log("Configuration Data Set Success", data);
			bootBoxAlert(cardType + " Configuration saved successfully.");
		},

		error: function (e) {
			console.log("fail" + e);
			bootBoxWarningAlert(cardType + " Configuration save was unsuccessfull.");
		}
	});

}

/************************************************************************
 * Ajax Call for fetching configuration   
 * 
 * Function Arguements: saveCardConfigurationToDb(Data,Optional)
 * 
 * Note: Call this function with Type field if ajax call type is POST
 ************************************************************************/
function ajaxGetConfigurationDataRequest(Data) {
	$.ajax({

		type: "POST",
		contentType: "application/json",

		headers: {
			Accept: "application/json; charset=utf-8",
			"Content-Type": "application/json; charset=utf-8"
		},


		url: "/getConfiguration",
		data: JSON.stringify(Data),
		timeout: 600000,

		success: function (data) {

			console.log(Data.CardType, " get config data ", data);
			// data={"CircuitData":[{"SubRack":1,"OperatorSpecific":null,"SrcNodeId":"1","DemandId":"1","PortId":null,"Rack":1,"CardType":"MPN(CGM)","FecType":null,"CircuitId":"1","NodeKey":"1_1","TxTCMMode":null,"TrafficType":"Ethernet","GccValue":null,"Path":"1,2","TxTCMPriority":null,"TimDectMode":null,"GccType":null,"IsRevertive":null,"Card":1,"TCMActStatus":null,"TxSegment":null,"GccStatus":null,"TCMActValue":null,"ProtectionSubType":null,"DestNodeId":"2","FecStatus":null},{"SubRack":1,"OperatorSpecific":null,"SrcNodeId":"1","DemandId":"1","PortId":null,"Rack":1,"CardType":"MPN(CGM)","FecType":null,"CircuitId":"1","NodeKey":"1_2","TxTCMMode":null,"TrafficType":"Ethernet","GccValue":null,"Path":"1,2","TxTCMPriority":null,"TimDectMode":null,"GccType":null,"IsRevertive":null,"Card":1,"TCMActStatus":null,"TxSegment":null,"GccStatus":null,"TCMActValue":null,"ProtectionSubType":null,"DestNodeId":"2","FecStatus":null},{"SubRack":1,"OperatorSpecific":null,"SrcNodeId":"1","DemandId":"2","PortId":null,"Rack":1,"CardType":"MPN(CGM)","FecType":null,"CircuitId":"2","NodeKey":"1_1","TxTCMMode":null,"TrafficType":"Ethernet","GccValue":null,"Path":"1,2,3","TxTCMPriority":null,"TimDectMode":null,"GccType":null,"IsRevertive":null,"Card":10,"TCMActStatus":null,"TxSegment":null,"GccStatus":null,"TCMActValue":null,"ProtectionSubType":null,"DestNodeId":"3","FecStatus":null},{"SubRack":1,"OperatorSpecific":null,"SrcNodeId":"1","DemandId":"2","PortId":null,"Rack":1,"CardType":"MPN(CGM)","FecType":null,"CircuitId":"2","NodeKey":"1_3","TxTCMMode":null,"TrafficType":"Ethernet","GccValue":null,"Path":"1,2,3","TxTCMPriority":null,"TimDectMode":null,"GccType":null,"IsRevertive":null,"Card":1,"TCMActStatus":null,"TxSegment":null,"GccStatus":null,"TCMActValue":null,"ProtectionSubType":null,"DestNodeId":"3","FecStatus":null},{"SubRack":2,"OperatorSpecific":null,"SrcNodeId":"1","DemandId":"3","PortId":null,"Rack":1,"CardType":"MPN(CGM)","FecType":null,"CircuitId":"3","NodeKey":"1_1","TxTCMMode":null,"TrafficType":"Ethernet","GccValue":null,"Path":"1,2,3,4","TxTCMPriority":null,"TimDectMode":null,"GccType":null,"IsRevertive":null,"Card":10,"TCMActStatus":null,"TxSegment":null,"GccStatus":null,"TCMActValue":null,"ProtectionSubType":null,"DestNodeId":"4","FecStatus":null},{"SubRack":1,"OperatorSpecific":null,"SrcNodeId":"1","DemandId":"3","PortId":null,"Rack":1,"CardType":"MPN(CGM)","FecType":null,"CircuitId":"3","NodeKey":"1_4","TxTCMMode":null,"TrafficType":"Ethernet","GccValue":null,"Path":"1,2,3,4","TxTCMPriority":null,"TimDectMode":null,"GccType":null,"IsRevertive":null,"Card":1,"TCMActStatus":null,"TxSegment":null,"GccStatus":null,"TCMActValue":null,"ProtectionSubType":null,"DestNodeId":"4","FecStatus":null},{"SubRack":3,"OperatorSpecific":null,"SrcNodeId":"1","DemandId":"4","PortId":null,"Rack":1,"CardType":"MPN(CGM)","FecType":null,"CircuitId":"4","NodeKey":"1_1","TxTCMMode":null,"TrafficType":"Ethernet","GccValue":null,"Path":"1,2,3,4,5","TxTCMPriority":null,"TimDectMode":null,"GccType":null,"IsRevertive":null,"Card":1,"TCMActStatus":null,"TxSegment":null,"GccStatus":null,"TCMActValue":null,"ProtectionSubType":null,"DestNodeId":"5","FecStatus":null},{"SubRack":1,"OperatorSpecific":null,"SrcNodeId":"1","DemandId":"4","PortId":null,"Rack":1,"CardType":"MPN(CGM)","FecType":null,"CircuitId":"4","NodeKey":"1_5","TxTCMMode":null,"TrafficType":"Ethernet","GccValue":null,"Path":"1,2,3,4,5","TxTCMPriority":null,"TimDectMode":null,"GccType":null,"IsRevertive":null,"Card":1,"TCMActStatus":null,"TxSegment":null,"GccStatus":null,"TCMActValue":null,"ProtectionSubType":null,"DestNodeId":"5","FecStatus":null}]};
			if (Data.requestDetails.CardType == nptGlobals.CardTypeMpn)
				mpnConfigTab(data);
		},

		error: function (e) {
			console.log("fail" + e);
			bootBoxWarningAlert(CardType + " Configuration fetch was unsuccessfull.");
		}
	});
}







