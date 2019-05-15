
  var isExternalValidateTrigger;

  $(document).ready(function() {
  	$.fn.editable.defaults.mode = "inline";
  });

  var isdefaultCircuitGenerator = false;

  const channelProtectionMechanismSrc = nptGlobals.fGetChannelPtcTypeSrc();
  const clientProtectionMechanismSrc = nptGlobals.fGetClientPtcTypeSrc();
  const lineRateSrc = nptGlobals.fGetLineRateTypeSrc();
  const clientRateSrc = nptGlobals.fGetClientRateTypeSrc();
  const ptcTypeSrc = nptGlobals.fGetPtcTypeSrc();
  const colorSrc = nptGlobals.fGetColorTypeSrc();
  const yesNoSrc = nptGlobals.fGetYesNoTypeSrc();
  const pathTypeSrc = nptGlobals.fGetPathTypeSrc();
  const ClientRateSrcForPotp = nptGlobals.fGetClientRateSrcPOTP();
  const ServiceTypeSrcForPotp = nptGlobals.fGetServiceTypeSrcPOTP();
  const TributoryIdsrc = nptGlobals.fGetTributoryIdSrc();

  //const serviceTypeSrc = [
  //// { value: 'None', text: 'None' },
  //{ value: nptGlobals.ServiceTypeEthernet, text: nptGlobals.ServiceTypeEthernet
  //},
  //{ value: nptGlobals.ServiceTypeEthernet100G, text:
  //nptGlobals.ServiceTypeEthernet100G },
  //{ value: nptGlobals.ServiceTypeOTU1, text: nptGlobals.ServiceTypeOTU1 },
  //{ value: nptGlobals.ServiceTypeOTU2, text: nptGlobals.ServiceTypeOTU2 },
  //{ value: nptGlobals.ServiceTypeOTU4, text: nptGlobals.ServiceTypeOTU4 },
  //{ value: nptGlobals.ServiceTypeFC_100, text: nptGlobals.ServiceTypeFC_100 },
  //{ value: nptGlobals.ServiceTypeFC_200, text: nptGlobals.ServiceTypeFC_200 },
  //{ value: nptGlobals.ServiceTypeFC_1200, text: nptGlobals.ServiceTypeFC_1200
  //},
  //{ value: nptGlobals.ServiceTypeSTM_16, text: nptGlobals.ServiceTypeSTM_16 },
  //{ value: nptGlobals.ServiceTypeSTM_64, text: nptGlobals.ServiceTypeSTM_64 }
  //];

  //=====Capacity Source =======//
  var nodeArray = [];

  const multiplierArray = [];

  for (var i = 1; i <= 30; i++) {
  	multiplierArray.push({ value: i, text: i });
  }
  multiplierArray.push({ value: 40, text: 40 });
  multiplierArray.push({ value: 400, text: 400 });
  //multiplierArray.push({value: 300, text: 300});
  //multiplierArray.push({value: 500, text: 500});

  const MultiplierInitializeObj = {
  		type: "select",
  		value: 1,
  		source: multiplierArray,
  		onblur: "submit",
  		validate: function(newValue) {
  			console.log(newValue);
  			oldValue = $(this)
  			.eq(0)
  			.html();
  			console.log(oldValue);
  		}
  };

  const CapacityInitializeObj = {
  		type: "select",
  		value: nptGlobals.ClientRate_10,
  		source: clientRateSrc,
  		onblur: "submit",
  		validate: function(value) {
  			console.log("in capacity validation"+$(this).html(),+value );
  			return capacityValidation(value, $(this));
  		}
  };

  const LineRateInitializeObj = {
  		type: "select",
  		value: nptGlobals.LineRate_100,
  		source: lineRateSrc,
  		onblur: "submit",
  		validate: function(value) {
  			return lineRateValidation(value, $(this));
  		}
  };

  const ColorInitializeObj = {
  		value: [],
  		source: colorSrc,
  		onblur: "submit"
  };

  const NodeInitializeObj = {
  		value: [],
  		source: [],
  		onblur: "submit"
  };

  const ServiceTypeInitializeObj = {
  		type: "select",
  		value: nptGlobals.fGetServiceTypeSrc(10, 100)[0].value,
  		source: nptGlobals.fGetServiceTypeSrc(10, 100),
  		onblur: "submit",
  		validate: function(value) {
  			return ServiceTypeValidation(value, $(this));
  		}
  };

  const PtcMechClientInitializeObj = {
  		type: "select",
  		value: nptGlobals.PtcMechYCable,
  		source: clientProtectionMechanismSrc,
  		onblur: "submit",
  		validate: function(value) {
  			return protectionMechanismValidation(value, $(this));
  		}
  };

  const PtcMechChannelInitializeObj = {
  		type: "select",
  		value: nptGlobals.PtcMechOPX,
  		source: channelProtectionMechanismSrc,
  		onblur: "submit",
  		validate: function(value) {

  			return protectionMechanismValidation(value, $(this));
  		}
  };

  const VendorLabelInitializeobj = {
  		type: "text",
  		value: "CDOT",
  		onblur: "submit",
  		validate: function(value) {}
  };

  const PathTypeInitializeObj = {
  		type: "select",
  		value: nptGlobals.DisjointPathStr,
  		source: pathTypeSrc,
  		onblur: "submit"
  };

  const lambdaBlkInitializeObj = {
  		type: "select",
  		value: nptGlobals.NoStr,
  		source: yesNoSrc,
  		onblur: "submit"
  };

  const clientPtcInitializeObj = {
  		type: "select",
  		value: nptGlobals.YesStr,
  		source: yesNoSrc,
  		onblur: "submit",
  		validate: function(value) {
  			return clientProtectionValidation(value, $(this));
  		}
  };

  const channelPtcInitializeObj = {
  		type: "select",
  		value: nptGlobals.NoStr,
  		source: yesNoSrc,
  		onblur: "submit",
  		validate: function(value) {
  			return channelProtectionValidation(value, $(this));
  		}
  };

  const ptcTypeInitializeObj = {
  		type: "select",
  		value: nptGlobals.PtcTypeOnePlusOne,
  		source: ptcTypeSrc,
  		onblur: "submit",
  		validate: function(value) {
  			return protectionTypeValidation(value, $(this));
  		}
  };

  $("#DemandTabTriggerBtn").click(function() {
  	console.log("Clicked DemandTabTriggerBtn");
  	getCircuitTableInput(false);
  	$.fn.editable.defaults.mode = "popup";
  	$(".footable").footable();
  });

  $("body").delegate("#SaveCircuitTableBtn", "click", function() {
  	setCircuitTableInput(0);
  });

  /** Added for ease of effort : Save circuit and Execute RWA in Single operation */
  $("body").delegate("#SaveCircuitExecuteRWATableRowBtn", "click", function() {
  	// rwaExecutionRequest();
  	setCircuitTableInput(1);
  });

  $("body").delegate("#AddCircuitTableRowBtn", "click", function() {
  	AddCircuitTableRow();

  	// overlayOn("Fetching
  	// Data",".side-bar-container__content--overlay-container");
  	let postJsonData = jsonPostObject();
  	console.log("Add Circuit Table Row Btn PostObj ::", postJsonData);

  	serverPostMessage("circuitInput", postJsonData)
  	.then(function(data) {
  		// overlayOff("Fetch Success",
  		// ".side-bar-container__content--overlay-container",'fold');
  		console.log("Circuit Data::", data);
  		initializeNewCircuitRow(data);
  	})
  	.catch(function(e) {
  		// overlayOff("Fetch Failure",
  		// ".side-bar-container__content--overlay-container",'fold');
  		console.log("fail", e);
  	});
  });

  $("body").delegate("#DefaultCircuitTableRowBtn", "click", function() {
  	console.log("DefaultCircuitTableRowBtn clicked");
  	// window.isdefaultCircuitGenerator=true;
  	getCircuitTableInput(true);
  	// window.isdefaultCircuitGenerator=false;
  });

  /** Check/Uncheck all checkboxes for delete operation */
  $("body").delegate("#checkAll", "change", function() {
	  let checkBoxes = $("#TrafficMatrixTable td input:checkbox").not(
			    "#TrafficMatrixTable td .switch input:checkbox"
			  );

//	  console.log("Checkboxes", checkBoxes);
	  checkBoxes.prop("checked", $(this).prop("checked"));
  });

  $("body").delegate(".switch_to_potp", "change", function() {
  	// console.log("Check All",$("#TrafficMatrixTable td input:checkbox"));
  	console.log("on click");
  	console.log("here", $(this))
  	let Qos = $(this).find('input[type=checkbox]')[0].checked;
  	console.log(Qos);

  	if($(this).find('input[type=checkbox]')[0].checked)
  	{   
  		var serviceTypeSrc = nptGlobals.fServiceTypeOTU2Lower(1);
  		console.log("Qos",serviceTypeSrc,$(this).next().next().next().children());
  		$ServiceType = $($(this).next().next().next().find('span.serviceType').get(0));
  		console.log("in condition",$ServiceType);
  		$ServiceType.editable("option", "source", serviceTypeSrc);
  		$ServiceType.editable("setValue",serviceTypeSrc[0].value ); 

  	}
  	else
  	{
  		var serviceTypeSrc = nptGlobals.fServiceTypeOTU2Lower(0);
  		console.log("Qos",serviceTypeSrc);
  		$ServiceType = $($(this).next().next().next().find('span.serviceType').get(0));
  		console.log("in condition",$ServiceType);
  		$ServiceType.editable("option", "source", serviceTypeSrc);
  		$ServiceType.editable("setValue", serviceTypeSrc[0].value); 

  		let aggregatorBtn = $($(this).next().next().next().find('span.service-type-btn').get(0));
  		console.log("Aggregator button ",aggregatorBtn);
  		aggregatorBtn.hide();

  	}

  	/* console.log(checkbox);
  	    checkbox.addEventListener('change', function () {
  	      if (checkbox.checked) {
  	        // do this
  	        console.log('Checked');
  	      } else {
  	        // do that
  	        console.log('Not checked');
  	      }*/


  	// });

  });


  /*
   * $("body").delegate("#switch_to_potp", "change", function() { console.log("on
   * click"); //console.log("Check All",$("#TrafficMatrixTable td
   * input:checkbox")); $('#switch_to_potp').click(function() {
   * 
   * if (this.checked) { // do this console.log('Checked'); } else { // do that
   * console.log('Not checked'); }
   * 
   * });
   * 
   * });
   */
  /**
   * Checkbox selected row delete
   */
  $("body").delegate("#DeleteCircuitTableRowBtn", "click", function() {
  	var checked = $("#TrafficMatrixTable td input:checkbox:checked").map(
  			function() {
  				return this;
  			}
  	);

  	console.log("Checked", checked.length);

  	console.log(
  			"Checked Rows:",
  			$(checked)
  			.parents("tr")
  			.not("thead tr")
  			.not(".uneditable-brown-field")
  	);
  	if (
  			$(checked)
  			.parents("tr")
  			.not("thead tr")
  			.not(".uneditable-brown-field").length == 0
  	) {
  		if (!isBrownFieldNetwork())
  			bootBoxDangerAlert("Select circuits to delete.");
  		else bootBoxDangerAlert("Only newly added circuits can be deleted.");
  	} else {
  		var dialog = new joint.ui.Dialog({
  			type: "alert",
  			width: 300,
  			title: "Confirm",
  			content: "Are you sure you want to delete circuits ?",
  			buttons: [
  				{ action: nptGlobals.YesStr, content: nptGlobals.YesStr },
  				{ action: nptGlobals.NoStr, content: nptGlobals.NoStr }
  				],
  				draggable: true
  		});

  		dialog.on("action:Yes", function() {
  			$(checked)
  			.parents("tr")
  			.not("thead tr")
  			.not(".uneditable-brown-field")
  			.remove();
  			dialog.close();
  		});

  		dialog.on("action:No", function() {
  			dialog.close();
  		});

  		dialog.open();
  	}
  });

  function AddCircuitTableRow() {
  	var rowCount = $("#TrafficMatrixTable tbody tr").length;

  	console.log("Number of rows :" + rowCount);

  	var template = "";

  	// var
  	// circuitRowLabelSrc=sessionStorage.getItem("NetworkState")==nptGlobals.GreenFieldStr?"gfAdd.png":"bfAdd.png";
  	let state =
  		sessionStorage.getItem("NetworkState") == nptGlobals.GreenFieldStr
  		? nptGlobals.GreenFieldRowClassStr
  				: nptGlobals.NewRowClassStr;
  	let circuitRowLabel = getLabelTagsFromState(state);

  	template += `<tr class=${state}><td><input type="checkbox" class="rowCheckBox" /> <span class="checkbox"></span>${circuitRowLabel}</td>`;

  	template += "<td class='hidden'>-1</td>";
  	template += `<td class ="switch_to_potp" >    <label class="switch">
  		<input type="checkbox" >
  		<span class="slider round"></span>
  		</label> </td>`;
  	template += "<td><span class='badge newsrcNode'></span></td>";
  	template += "<td><span class='badge newdestNode'></span></td>";
  	template += `<td><span class="serviceType"></span> 
  		<span class="service-type-btn">
  		<i class="fa fa-plus text-success cursor" title="Add OTU2 LOWER Service Type " aria-hidden="true" onClick="ServiceTypeOTU2LowerClientDialog($(this).parent())"></i>
  		</span>      
  		<span class="OTU2_Lower"></span> 
  		</td>`;
  	template += `<td><span class="newVendorLabel"></span></td>
  		<td>
  		<span class="newCapacity"></span>
  		<span class="capacity-client-btn">
  		<i class="fa fa-plus text-success cursor" title="Add Aggregator Clients" aria-hidden="true" onClick="aggregatorClientDialog($(this).parent())"></i>
  		</span>
  		<span class="agg-circuits"></span>
  		</td>
  		<td><span class="newLineRate"></span></td>
  		<td><span class="newMultiplier"></span></td>
  		<td><span class="newProtectionType"></span></td>
  		<td><span class="newClientProtection"></span></td>
  		<td><span class="newProtectionMechanism"></span></td>
  		<td><span class="newChannelProtection"></span></td>
  		<td><span class="newPathType"></span></td>
  		<td><span class="newLambdaBlocking"></span></td>
  		<td><span class="newColorPreference" data-type="checklist"></span></td>		
  		<td><span class="newNodeInclusion" data-type="checklist"></td>				
  		<td><i class="fa fa-trash fa-2x text-danger cursor" aria-hidden="true" onClick="deleteRow(this)"></i></td>
  		</tr>`;
  	console.log(template);
  	let newRow = $(template);
  	console.log(newRow);
  	$("#TrafficMatrixTable tbody").append(newRow);
  }

  /**
   * Ajax Call to Get Circuit Schema for Dynamic Table Generation
   */
  var getCircuitTableInput = function(defaultCall) {
  	overlayOn("Fetching Data", ".side-bar-container__content--overlay-container");
  	let postJsonData = jsonPostObject();
  	console.log("----- getCircuitTableInput() ----  PostObj ::", postJsonData);

  	serverPostMessage("circuitInput", postJsonData)
  	.then(function(data) {
  		overlayOff(
  				"Fetch Success",
  				".side-bar-container__content--overlay-container",
  				"fold"
  		);
  		nodeArray = data.NodeData;
  		// data={"CircuitCount":0,"NodeCount":5,"NodeData":[{"NodeType":2,"NodeId":1,"NodeSubType":0},{"NodeType":2,"NodeId":2,"NodeSubType":0},{"NodeType":2,"NodeId":3,"NodeSubType":0},{"NodeType":3,"NodeId":4,"NodeSubType":0},{"NodeType":2,"NodeId":5,"NodeSubType":0}]}
  		// ;

  		var template = "";
  		// template+=fgetCircuitButtonsTemplate();
  		template += ` <table class="footable table table-striped " id="TrafficMatrixTable">
  			<thead>
  			<th><input type="checkbox" id="checkAll"/>Select All</th>
  			<th class="switch_to_potp" >${"QoS"}</th>
  			<th data-defaultsign="_1">${nptGlobals.SrcNodeLabelStr}</th>
  			<th>${nptGlobals.DestNodeLabelStr}</th>
  			<th data-defaultsort="disabled">${nptGlobals.ServiceTypeLabelStr}</th>
  			<th data-defaultsort="disabled">${nptGlobals.VendorLabelStr}</th>
  			<th data-defaultsort="disabled">${nptGlobals.ClientRateLabelStr}</th>				
  			<th data-defaultsort="disabled">${nptGlobals.LineRateLabelStr}</th>	
  			<th data-defaultsort="disabled">${nptGlobals.MultiplierLabelStr}</th>			
  			<th data-defaultsort="disabled">${nptGlobals.ProtectionLabelStr}</th>
  			<th data-defaultsort="disabled">${nptGlobals.ClientPtcLabelStr}</th>
  			<th data-defaultsort="disabled">${nptGlobals.PtcMechLabelStr}</th>
  			<th data-defaultsort="disabled">${nptGlobals.ChannelPtcLabelStr}</th>
  			<th data-defaultsort="disabled">${nptGlobals.PathTypeLabelStr}</th>
  			<th data-defaultsort="disabled">${nptGlobals.LambdaBlockingLabelStr}</th>
  			<th data-defaultsort="disabled">${nptGlobals.ColorPrefLabelStr}</th>
  			<th data-defaultsort="disabled">${nptGlobals.NodeIncLabelStr}</th>
  			<th>${nptGlobals.DeleteLabelStr}</th>
  			</thead>
  			<tbody>`;

  		// console.log("Json Data ");
  		// console.log(data);
  		// console.log("Json success :- "+data.NodeCount);
  		var temp = 0;
  		var count = 1;
  		console.log("data.CircuitCount  ::" + data.CircuitCount);
  		// console.log("defaultCall ::"+defaultCall);
  		if (data.CircuitCount == 0 || defaultCall == true) {
  			// defaultCall : true or false , for default circuit trigger, default
  			// value:False
  			var NodeDataArr = data.NodeData;
  			console.log("NodeDataArr :", NodeDataArr);

  			if (NodeDataArr.length == 0) {
  				var MapNotSavedTemplate = `<p class="lead text-center text-warning" >NPT requires you to save network map first in order to generate a default circuit.</p>`;
  				$("#circuits-view")
  				.empty()
  				.append(MapNotSavedTemplate);
  			} else {
  				console.log(
  						"nptGlobals..NetworkTopology :" + nptGlobals.NetworkTopology
  				);

  				if (nptGlobals.NetworkTopology == nptGlobals.TopologyHubRingStr) {
  					// Only hub node can be a src/dest
  					defaultCircuitGenerationHubRing(NodeDataArr, template);
  				} else {
  					defaultCircuitGeneration(NodeDataArr, template);
  				}
  			}
  		} else {
  			console.log("DB Circuit Data ::", data);
  			var circuitJsonArray = data.CircuitData;
  			console.log("circuitJsonArray ::", circuitJsonArray);

  			_.each(circuitJsonArray, (circuit, i) => {
  				var imageId = "#deleteImgId".concat(count);
  				var state = circuit.state + "RowClass";

  				console.log("Row Received from db :", circuit);
  				console.log("POTP circuits Row Received from db :", circuit.POTPCircuits);
  				if(circuit.QoS == 1)
  				{ // console.log("in circuit");
  					circuit.QoS = "checked";
  				}
  				let label = getLabelTagsFromState(state);

  				// If greenField / Modified / Deleted Circuit , then update state to
  				// uneditable
  				if (
  						state == nptGlobals.OldRowClassStr ||
  						state == nptGlobals.DeletedRowClassStr ||
  						state == nptGlobals.ModifiedRowClassStr
  				)
  					state += " uneditable-brown-field";

  				// console.log("brownFieldDisableState", state);

  				template += `<tr class="${state}"><td><input type="checkbox" class="rowCheckBox" /> <span class="checkbox"></span>`;
  				template += label;
  				template += `</td>`;
  				template += `<td class="hidden">${circuit.CircuitId}</td>`;
  				template += `<td class = "switch_to_potp" > <label class="switch">
  					<input type="checkbox" ${circuit.QoS}>
  					<span class="slider round"></span>
  					</label></td>`;
  				template += `<td data-value='${
  					circuit.SrcNodeId
  					}'><span class='badge'>${circuit.SrcNodeId}</span></td>`;
  				template += `<td data-value='${
  					circuit.SrcNodeId
  					}'><span class='badge'>${circuit.DestNodeId}</span></td>`;
  				template += `<td>   <span class="serviceType"></span>
  					<span class="service-type-btn">
  					<i class="fa fa-plus text-success cursor" title="Add OTU2 LOWER Service Type " aria-hidden="true" onClick="ServiceTypeOTU2LowerClientDialog($(this).parent())"></i>
  					</span>      
  					<span class="OTU2_Lower">${fGetOTU2_lOWERServiceTypeTemplate()}</span> 
  					</td>`;
  				template += `<td><span class="vendorLabel brownFieldDisableState"></span></td>
  					<td>
  					<span class="capacity brownFieldDisableState"></span>
  					<span class="capacity-client-btn">
  					<i class="fa fa-plus text-success cursor" title="Add Aggregator Clients" aria-hidden="true" onClick="aggregatorClientDialog($(this).parent())"></i>
  					</span>
  					<span class="agg-circuits">${fGetAggregatorCircuitsTemplate()}</span>
  					</td>
  					<td><span class="lineRate brownFieldDisableState"></span></td>
  					<td><span class="multiplier brownFieldDisableState"></span></td>
  					<td><span class="protectionType brownFieldDisableState"></span></td>
  					<td><span class="clientProtection brownFieldDisableState"></span></td>
  					<td><span class="protectionMechanism brownFieldDisableState"></span></td>
  					<td><span class="channelProtection brownFieldDisableState"></span></td>
  					<td><span class="pathType brownFieldDisableState"></span></td>
  					<td><span class="lambdaBlocking brownFieldDisableState"></span></td>
  					<td><span class="ColorPreference brownFieldDisableState" data-type="checklist"></span></td>
  					<td><span class="nodeInclusion brownFieldDisableState" data-type="checklist"></span></td>
  					<td><i class="fa fa-trash fa-2x text-danger cursor" aria-hidden="true" onClick="deleteRow(this)"></i></td>
  					</tr>`;
  				count++;
  			});

  			template += `</tbody></table>`;
  			template += fgetCircuitButtonsTemplate();
  			// console.log(template);
  			let templateDiv = $(template);

  			$("#circuits-view")
  			.empty()
  			.append(templateDiv);
  			console.log("Circuit Table filling from DB Data.....");
  			initializeCircuitRowsFromDbData(circuitJsonArray);

  			fRenderAggregatorCircuits(circuitJsonArray);
  			fRenderOTU2_LOWERServiceTypeCircuits(circuitJsonArray);
  		}
  	})
  	.catch(function(e) {
  		overlayOff(
  				"Fetch Failure",
  				".side-bar-container__content--overlay-container",
  				"fold"
  		);
  		console.log("fail", e);
  	});
  };

  function fgetCircuitButtonsTemplate() {
  	let btnTemplate = "";
  	btnTemplate += `<div class="btn-group btn-group-lg btn-block circuit-button-group">`;
  	btnTemplate += `<button class=' circuit-button-group__btn' type='button'  id='AddCircuitTableRowBtn' title='Add Circuit'><i class='fa fa-plus' aria-hidden='true'></i></button>`;
  	btnTemplate += `<button class=' circuit-button-group__btn' type='button'  id='DeleteCircuitTableRowBtn' title='Delete Selected Circuits'><i class='fa fa-trash' aria-hidden='true'></i></button>`;
  	if (isBrownFieldNetwork())
  		btnTemplate += `<button class=' circuit-button-group__btn' type='button' disabled id='DefaultCircuitTableRowBtn' title=' Default Circuit '><i class='fa fa-refresh' aria-hidden='true'></i></button>`;
  	else
  		btnTemplate += `<button class=' circuit-button-group__btn' type='button'  id='DefaultCircuitTableRowBtn' title=' Default Circuit '><i class='fa fa-refresh' aria-hidden='true'></i></button>`;
  	btnTemplate += `<button class=' circuit-button-group__btn' type='button'  id='SaveCircuitTableBtn' title=' Save Circuit '><i class="fa fa-floppy-o" aria-hidden="true"></i></button>`;
  	btnTemplate += `<button class=' circuit-button-group__btn text-center' type='button'  id='SaveCircuitExecuteRWATableRowBtn' title=' Save Circuit & Execute RWA'> RWA </button>`;
  	btnTemplate += `</div>`;
  	return btnTemplate;
  }

  /**
   * Ajax Call to Set Circuit Schema for Demand Table Generation
   */
  function setCircuitTableInput(paramFlag) {
  	/** *******Parse Rows to JSON*************** */
  	var myRows = [],
  	multiplier;
  	let circuitSaveFlag = true;
  	var $headers = $("#TrafficMatrixTable th").not(
  			"#TrafficMatrixTable th:first-child"
  	);

  	console.log("Headersssssss",$headers);
  	// console.log("Headsers-----"+$($headers));
  	var $rows = $("#TrafficMatrixTable tbody > tr").each(function(index) {
  		// console.log("Row-----"+$(this));
  		$cells = $(this)
  		.find("> td")
  		.not("td:first-child,td:hidden");
  		// console.log("Columns\n -----",$cells);
  		myRows[index] = {};

  		$cells.each(function(cellIndex) {
  			// console.log($(this)[0].firstElementChild.innerText)
  			$("br").replaceWith("#"); /** Replace br tag with # */
  			// var headerStr=$($headers[cellIndex]).html();
  			// if(headerStr=="Channel Protection")
  			// myRows[index][$($headers[cellIndex]).html()] =
  			// $(this).find('span').editable('getValue').undefined;
  			myRows[index][$($headers[cellIndex]).html()] = $(
  					this
  			)[0].firstElementChild.innerText;

  			if ($($headers[cellIndex]).html() == nptGlobals.MultiplierLabelStr)
  				multiplier = $(this)[0].firstElementChild.innerText;
  			// console.log("Assignment
  			// "+myRows[index][$($headers[cellIndex]).html()])

  			//console.log("Header[0]",$headers[0]);
  			// Check for aggregator circuits
  			console.log("potp js");

  			if($($headers[cellIndex]).html() === nptGlobals.ServiceTypeLabelStr)
  			{     

  				console.log("potp save inside js");
  				let ServiceType = $(this)[0].firstElementChild.innerText;
  				let POTPCircuits = [];

  				if(ServiceType == nptGlobals.ServiceTypeOTU2_LOWER)
  				{
  					let OTU2LowerCircuits = $(this).find(".OTU2_Lower tr");
  					if(OTU2LowerCircuits.length > 0) {
  						_.each(OTU2LowerCircuits, (tr, i) => {
  							console.log("index----------- :", i);

  							let SrcNode = $(tr)
  							.find(".agg-SrcNode")
  							.text();
  							let DestNode = $(tr)
  							.find(".agg-DestNode")
  							.text();
  							let ClientRate = $(tr)
  							.find(".agg-ClientRate")
  							.text();
  							let ServiceType = $(tr)
  							.find(".agg-ServiceType")
  							.text();
  							let ProtType = $(tr)
  							.find(".agg-ProtType")
  							.text();
  							let PathType = $(tr)
  							.find(".agg-PathType")
  							.text();
  							let Color = $(tr)
  							.find(".agg-Color")
  							.text();
  							let NodeInclusion = $(tr)
  							.find(".agg-NodeInclusion")
  							.text();
  							let TributoryId = $(tr)
  							.find(".agg-TriId")
  							.text();

  							let POTPCircuitObj = {};

  							POTPCircuitObj["SrcNodeId"] = SrcNode;
  							POTPCircuitObj["DestNodeId"] = DestNode;
  							POTPCircuitObj["TributoryId"] = TributoryId;
  							POTPCircuitObj["ServiceType"] = ServiceType;
  							POTPCircuitObj["ClientRate"] = ClientRate;
  							POTPCircuitObj["ProtType"] = ProtType;
  							POTPCircuitObj["PathType"] = PathType;
  							POTPCircuitObj["Colour"]  = Color;
  							POTPCircuitObj["NodeInclusion"] = NodeInclusion;
  							POTPCircuits[i] =  POTPCircuitObj;
  						});
  						console.log(
  								"POTP Clients added :",
  								myRows[index]["POTPCircuits"],
  								POTPCircuits
  						);
  						myRows[index]["POTPCircuits"] = POTPCircuits;


  					}

  				}


  			}


  			if ($($headers[cellIndex]).html() == nptGlobals.ClientRateLabelStr) {
  				let capacityValue = $(this)[0].firstElementChild.innerText;
  				let aggCircuitArr = [];


  				if (capacityValue == nptGlobals.ClientRate_10_Agg) {
  					let aggCircuits = $(this).find(".agg-circuits tr");

  					if (aggCircuits.length > 0) {
  						_.each(aggCircuits, (tr, i) => {
  							console.log("index----------- :", i);
  							let clientrate = $(tr)
  							.find(".agg-client-rate")
  							.text();
  							let serviceType = $(tr)
  							.find(".agg-service-type")
  							.text();
  							let aggMultiplier = $(tr)
  							.find(".agg-multiplier")
  							.text();

  							let aggCircuitObj = {};
  							aggCircuitObj["ClientRate"] = clientrate;
  							aggCircuitObj["ServiceType"] = serviceType;
  							aggCircuitObj["Multiplier"] = aggMultiplier;
  							aggCircuitArr[i] = aggCircuitObj;
  						});
  						console.log(
  								"Aggregator Clients added :",
  								myRows[index]["AggregatorCircuits"],
  								aggCircuitArr
  						);
  						myRows[index]["AggregatorCircuits"] = aggCircuitArr;
  					} else {
  						bootBoxDangerAlert(
  								`One of the 10G Aggregator circuit doesn't have clients. Please add clients to it or change the client rate to proceed.`
  						);
  						circuitSaveFlag = false;
  					}
  				}
  			}
  			// console.log("hereeeeee",$($headers[cellIndex]).html());
  			if($($headers[cellIndex]).html().trim() == "QoS"){
  				myRows[index][$($headers[cellIndex]).html().trim()]=$(this).find('input[type=checkbox]')[0].checked;

  				console.log("Qos",$($headers[cellIndex]).html().trim(),$(this).find('input[type=checkbox]')[0].checked);
  			}
  		});

//  		debugger;

  		// BF save circuit ; Send already created Circuit Id's with ajax call for
  		// save
  		var circuitId = $(this)
  		.find("td.hidden")
  		.html();

  		if ($(this).hasClass(nptGlobals.OldRowClassStr))
  			myRows[index]["CircuitIdArray"] = circuitId.split(",");
  		else {
  			myRows[index]["CircuitIdArray"] = []; // New circuit case : -1
  			for (var i = 0; i < multiplier; i++)
  				myRows[index]["CircuitIdArray"].push("-1");
  		}
  	});

  	/**
  	 * ***********Put this in the object like you want and convert to JSON
  	 * (Note: jQuery will also do this for you on the Ajax request)********
  	 */
  	// var circuitEntryObj = {};
  	// circuitEntryObj.circuitRows = myRows;

  	if (circuitSaveFlag) {
  		let saveCircuitPostData = jsonPostObject();
  		saveCircuitPostData.circuitRows = myRows;
  		saveCircuitPostData.Mode = 0; // 0 :Normal Circuit Mode
  		console.log(
  				"Json Array of Circuit Table rows for sending to Backend\n",
  				saveCircuitPostData
  		);

  		fShowClientServerCommunicationModal("Saving Circuit");

  		serverPostMessage("circuitInputSave", saveCircuitPostData)
  		.then(function(data) {
  			console.log("Demand Set Successfully" + data);
  			//fShowSuccessMessage("Circuit Saved Successfully");
  			// window.isCircuitSaved = 1;
  			nptGlobals.setCircuitSaved(true);
  			nptGlobals.setRwaExecuted(false);
  			nptGlobals.setInventoryGenerated(false);
  			nptGlobals.setIpSchemeGenerated(false);
  			// fNptStateChange(isCircuitSavedStr);

  			// disableCloseButton(false);
  			if (paramFlag == 1) {
  				rwaExecutionRequest();
  				///equipmentDbAjaxCall();
  			} else if (paramFlag == 0) {
  				fShowSuccessMessage("Circuit Saved Successfully");
  			}
  		})
  		.catch(function(e) {
  			fShowFailureMessage("Circuit couldn't be Saved Successfully ");
  			console.log("Demand Set failure", e);
  		});
  	}

  }

  var initializeCircuitRowsFromDbData = function(circuitJsonArray) {
  	console.log("In Initialize");
  	for (i = 0; i < circuitJsonArray.length; i++) {
  		let flag = true;
  		for (arrayCount = 0; arrayCount < multiplierArray.length; arrayCount++) {
  			if (multiplierArray[arrayCount].value == circuitJsonArray[i].Multiplier)
  				flag = false;
  		}

  		if (flag)
  			multiplierArray.push({
  				value: circuitJsonArray[i].Multiplier,
  				text: circuitJsonArray[i].Multiplier
  			});

  		$(".multiplier:eq(" + i + ")")
  		.attr("title", "Multiplier to Circuit")
  		.tooltip()
  		.editable({
  			type: "select",
  			value: circuitJsonArray[i].Multiplier,
  			source: multiplierArray,
  			onblur: "submit",
  			validate: function(value) {
  				console.log(value);
  				console.log(
  						$(this)
  						.eq(0)
  						.html()
  				);
  			}
  		});

  		var colorValueArr = [];
  		var colorPrefStrArr = circuitJsonArray[i].ColourPreference.split("#");

  		for (var c = 0; c < colorPrefStrArr.length; c++)
  			colorValueArr.push(colorPrefStrArr[c]);

  		$(".ColorPreference:eq(" + i + ")")
  		.attr("title", "Save ColorPreference")
  		.tooltip()
  		.editable({
  			value: colorValueArr,
  			source: colorSrc,
  			onblur: "submit"
  		});

  		$(".capacity:eq(" + i + ")")
  		.attr("title", "Edit Capacity ")
  		.tooltip()
  		.editable({
  			type: "select",
  			value: circuitJsonArray[i].RequiredTraffic,
  			source: clientRateSrc,
  			onblur: "submit",
  			validate: function(value) {
  				return capacityValidation(value, $(this));
  			}
  		});
  		console.log("near aggregator");
  		// Aggregator add circuit '+' button
  		if (circuitJsonArray[i].AggregatorCircuits !== undefined) {
  			$(".capacity:eq(" + i + ")")
  			.next()
  			.show();
  		}

  		// console.log("circuitJsonArray[i].lineRate
  		// :"+circuitJsonArray[i].lineRate);
  		// console.log("lineRate
  		// -->"+window.LineRate[circuitJsonArray[i].lineRate]);
  		// console.log($(".lineRate:eq("+i+")"));

  		$(".lineRate:eq(" + i + ")")
  		.attr("title", "Edit LineRate ")
  		.tooltip()
  		.editable({
  			type: "select",
  			value: circuitJsonArray[i].lineRate,
  			source: lineRateSrc,
  			onblur: "submit",
  			validate: function(value) {
  				return lineRateValidation(value, $(this));
  			}
  		});


//  		console.log("in db validate ",circuitJsonArray[i].TrafficType === "OTU2-LOWER",circuitJsonArray[i].TrafficType,);

  		if(circuitJsonArray[i].TrafficType === nptGlobals.ServiceTypeOTU2_LOWER)
  		{
  			let ServiceTypeSrc = nptGlobals.fServiceTypeOTU2Lower(1);
  			console.log("in db validate if");

  			$(".serviceType:eq(" + i + ")")
  			.attr("title", "Edit ")
  			.tooltip()
  			.editable({
  				type: "select",
  				value: circuitJsonArray[i].TrafficType,
  				source: ServiceTypeSrc,
  				onblur: "submit",
  				validate: function(value) {
  					console.log("inside db ServiceType");
  					return ServiceTypeValidation(value, $(this));
  				}
  			});

  			if (circuitJsonArray[i].POTPCircuits !== undefined) {
  				$(".serviceType:eq(" + i + ")")
  				.next()
  				.show();
  			}




  		} 

  		else
  		{
  			console.log("in db validate else");
  			let ServiceTypeSource = nptGlobals.fGetServiceTypeSrc(
  					circuitJsonArray[i].RequiredTraffic,
  					circuitJsonArray[i].lineRate
  			);
  			console.log("ServiceTypeSource from Db data :", ServiceTypeSource);

  			$(".serviceType:eq(" + i + ")")
  			.attr("title", "Edit ")
  			.tooltip()
  			.editable({
  				type: "select",
  				value: circuitJsonArray[i].TrafficType,
  				source: ServiceTypeSource,
  				onblur: "submit",
  				validate: function(value) {
  					console.log("inside db ServiceType");
  					return ServiceTypeValidation(value, $(this));
  				}
  			});
  		}



  		$(".vendorLabel:eq(" + i + ")")
  		.attr("title", "Edit Label")
  		.tooltip()
  		.editable({
  			type: "text",
  			value: circuitJsonArray[i].VendorLabel,
  			onblur: "submit"
  		});

  		$(".pathType:eq(" + i + ")")
  		.attr("title", "Edit ")
  		.tooltip()
  		.editable({
  			type: "select",
  			value: circuitJsonArray[i].PathType,
  			source: pathTypeSrc,
  			onblur: "submit"
  		});

  		$(".protectionType:eq(" + i + ")")
  		.attr("title", "Edit ")
  		.tooltip()
  		.editable({
  			type: "select",
  			value: circuitJsonArray[i].ProtectionType,
  			source: ptcTypeSrc,
  			onblur: "submit",
  			validate: function(value) {
  				return protectionTypeValidation(value, $(this));
  			}
  		});

  		let currPtcmechSrc;
  		if (circuitJsonArray[i].ChannelProtection == nptGlobals.YesStr)
  			currPtcmechSrc = channelProtectionMechanismSrc.slice(0);
  		else currPtcmechSrc = clientProtectionMechanismSrc.slice(0);

  		$(".protectionMechanism:eq(" + i + ")")
  		.attr("title", "Edit ")
  		.tooltip()
  		.editable({
  			type: "select",
  			value: circuitJsonArray[i].ProtectionMechanismType,
  			source: currPtcmechSrc,
  			onblur: "submit",
  			validate: function(value) {
  				return protectionMechanismValidation(value, $(this));
  			}
  		});

  		$(".channelProtection:eq(" + i + ")")
  		.attr("title", "Edit ")
  		.tooltip()
  		.editable({
  			type: "select",
  			value: circuitJsonArray[i].ChannelProtection,
  			source: yesNoSrc,
  			onblur: "submit",
  			validate: function(value) {
  				return channelProtectionValidation(value, $(this));
  			}
  		});

  		$(".clientProtection:eq(" + i + ")")
  		.attr("title", "Edit ")
  		.tooltip()
  		.editable({
  			type: "select",
  			value: circuitJsonArray[i].ClientProtection,
  			source: yesNoSrc,
  			onblur: "submit",
  			validate: function(value) {
  				return clientProtectionValidation(value, $(this));
  			}
  		});

  		// $(".protectionMechanism:eq(" + i + ")").editable('option', 'source',
  		// currPtcmechSrc);
  		// $(".protectionMechanism:eq(" + i + ")").editable('setValue',
  		// circuitJsonArray[i].ProtectionMechanismType);// Prot mechanism set to OPX

  		// $(".clientProtection:eq(" + i + ")").editable('option', 'source',
  		// yesNoSrc);
  		// $(".clientProtection:eq(" + i + ")").editable('setValue',
  		// circuitJsonArray[i].ClientProtection);// Prot mechanism set to OPX

  		// $(".protectionMechanism:eq(" + i + ")").editable('option', 'source',
  		// yesNoSrc);
  		// $(".protectionMechanism:eq(" + i + ")").editable('setValue',
  		// circuitJsonArray[i].ChannelProtection);// Prot mechanism set to OPX

  		$(".lambdaBlocking:eq(" + i + ")")
  		.attr("title", "Edit ")
  		.tooltip()
  		.editable({
  			type: "select",
  			value: circuitJsonArray[i].LambdaBlocking,
  			source: yesNoSrc,
  			onblur: "submit"
  		});

  		// console.log("circuitJsonArray[" + i + "].NodeInclusion",
  		// circuitJsonArray[i].NodeInclusion);
  		let nodeIncValueArr = [];
  		// if(circuitJsonArray[i].NodeInclusion.includes("#"))
  		if (circuitJsonArray[i].NodeInclusion != "Empty") {
  			nodeIncValueArr = circuitJsonArray[i].NodeInclusion.split("#");
  		}
  		// console.log("nodeIncValueArr[" + i + "]", nodeIncValueArr);
  		let nonIlaNodes = fGetNonIlaNodesSourceArray();
  		$(".nodeInclusion:eq(" + i + ")")
  		.attr("title", "Node Inclusion Preference")
  		.tooltip()
  		.editable({
  			value: nodeIncValueArr,
  			source: nonIlaNodes.filter(obj => {
  				return (
  						obj.value != circuitJsonArray[i].SrcNodeId &&
  						obj.value != circuitJsonArray[i].DestNodeId
  				);
  			}),
  			onblur: "submit"
  		});

  		if (
  				(circuitJsonArray[i].ProtectionType == nptGlobals.PtcTypeUnprotected) |
  				(circuitJsonArray[i].ProtectionType == nptGlobals.PtcTypeOneIsToOne) |
  				(circuitJsonArray[i].ProtectionType == nptGlobals.PtcTypeOneIsToTwoR)
  		) {
  			// console.log("circuitJsonArray[",i,"].ProtectionType",circuitJsonArray[i].ProtectionType);
  			isExternalValidateTrigger = 1;

  			$(".clientProtection:eq(" + i + ")").editable(
  					"setValue",
  					nptGlobals.NoStr
  			); // Prot mechanism cannot be none if Protection is there.
  			$(".clientProtection:eq(" + i + ")").editable("option", "disabled", true); // Prot
  			// mechanism
  			// enabled
  			$(".clientProtection:eq(" + i + ")").tooltip("disable");

  			$(".protectionMechanism:eq(" + i + ")").editable(
  					"setValue",
  					nptGlobals.NoneStr
  			); // Prot mechanism cannot be none if Protection is there.
  			$(".protectionMechanism:eq(" + i + ")").editable(
  					"option",
  					"disabled",
  					true
  			); // Prot mechanism enabled
  			$(".protectionMechanism:eq(" + i + ")").tooltip("disable");

  			$(".channelProtection:eq(" + i + ")").editable(
  					"setValue",
  					nptGlobals.NoStr
  			); // Prot mechanism cannot be none if Protection is there.
  			$(".channelProtection:eq(" + i + ")").editable(
  					"option",
  					"disabled",
  					true
  			); // Prot mechanism enabled
  			$(".channelProtection:eq(" + i + ")").tooltip("disable");

  			// protectionTypeValidation(circuitJsonArray[i].ProtectionType,$($("span.protectionType:eq("
  			// + i + ")").get(0)));
  			// $(".protectionType:eq(" + i + ")").editable("validate");
  			isExternalValidateTrigger = 0;
  		}

  		console.log(
  				"Current Row:",
  				$($("#TrafficMatrixTable tbody tr:eq(" + i + ")")).hasClass(
  						"uneditable-brown-field"
  				)
  		);
  		let $currentTableRow = $("#TrafficMatrixTable tbody tr:eq(" + i + ")");
  		if ($currentTableRow.hasClass("uneditable-brown-field")) {
  			console.log(
  					circuitJsonArray[i].NodeInclusion,
  					"---",
  					circuitJsonArray[i].ColourPreference
  			);
  			if (circuitJsonArray[i].NodeInclusion == "Empty")
  				$(".nodeInclusion:eq(" + i + ")").text("Empty"); // Disable all
  			// fields
  			if (circuitJsonArray[i].ColourPreference == "Empty")
  				$(".ColorPreference:eq(" + i + ")").text("Empty"); // Disable all
  			// fields

  			let $currentTableRowEditableFields = $(
  					"#TrafficMatrixTable tbody tr:eq(" + i + ") .editable"
  			);
  			console.log("Has class uneditable");
  			$currentTableRowEditableFields.editable("option", "disabled", true); // Disable
  			// all
  			// fields
  			// $currentTableRowEditableFields.tooltip("option", "content","Edit old
  			// circuits in Demand Matrix.");// Disable all fields

  			$("#TrafficMatrixTable tbody tr:eq(" + i + ") .serviceType").editable(
  					"option",
  					"disabled",
  					false
  			);
  		}
  	}

  	// $('.footable').footable();
  	fTopologyChecks();
  };

  function initializeCircuitRows() {
  	$(".multiplier")
  	.attr("title", "Multiplier to Circuit")
  	.tooltip()
  	.editable(MultiplierInitializeObj);

  	$(".capacity")
  	.attr("title", "Edit Capacity ")
  	.tooltip()
  	.editable(CapacityInitializeObj);

  	$(".lineRate")
  	.attr("title", "Edit LineRate ")
  	.tooltip()
  	.editable(LineRateInitializeObj);

  	$(".ColorPreference")
  	.attr("title", "Save ColorPreference")
  	.tooltip()
  	.editable(ColorInitializeObj);
  	console.log("from here in initial");
  	$(".serviceType")
  	.attr("title", "Edit ")
  	.tooltip()
  	.editable(ServiceTypeInitializeObj);

  	$(".vendorLabel")
  	.attr("title", "Edit Label")
  	.tooltip()
  	.editable(VendorLabelInitializeobj);

  	$(".pathType")
  	.attr("title", "Edit ")
  	.tooltip()
  	.editable(PathTypeInitializeObj);

  	$(".protectionType")
  	.attr("title", "Edit ")
  	.tooltip()
  	.editable(ptcTypeInitializeObj);

  	$(".protectionMechanism")
  	.attr("title", "Edit ")
  	.tooltip()
  	.editable(PtcMechClientInitializeObj);

  	$(".channelProtection")
  	.attr("title", "Edit ")
  	.tooltip()
  	.editable(channelPtcInitializeObj);

  	$(".clientProtection")
  	.attr("title", "Edit ")
  	.tooltip()
  	.editable(clientPtcInitializeObj);

  	$(".lambdaBlocking")
  	.attr("title", "Edit ")
  	.tooltip()
  	.editable(lambdaBlkInitializeObj);

  	/** *** Node Inclusion **** */
  	let nonIlaNodes = fGetNonIlaNodesSourceArray();

  	$(".nodeInclusion")
  	.attr("title", "Edit ")
  	.tooltip()
  	.editable(NodeInitializeObj);

  	initializeNodeInclusionInput(nonIlaNodes);

  	$(".footable").footable();
  	fTopologyChecks();
  }

  function fGetNonIlaNodesSourceArray() {
  	let nonIlaNodes = [];
  	nodeArray.map(obj => {
  		if (obj.NodeType != nptGlobals.numNodeTypeILA) {
  			let newObj = {};
  			newObj.text = obj.NodeId;
  			newObj.value = obj.NodeId;
  			nonIlaNodes.push(newObj);
  		}
  	});

  	return nonIlaNodes;
  }

  function initializeNodeInclusionInput(nonIlaNodes) {
  	console.log("initializeNodeInclusionInput()");
  	$(".nodeInclusion").each(function(index, value) {
  		// console.log($(this));
  		let srcNode = $(this)
  		.parent()
  		.parent()
  		.children()
  		.eq(2)
  		.text();
  		let destNode = $(this)
  		.parent()
  		.parent()
  		.children()
  		.eq(3)
  		.text();
  		// console.log("srcNode::",srcNode);
  		// console.log(" destNode::",destNode);
  		let source = nonIlaNodes.filter(obj => {
  			return obj.value != srcNode && obj.value != destNode;
  		});
  		// console.log("Index :",index," Source",source)
  		$(this).editable("option", "source", source);
  		// $(this).editable('setValue',source[0].value);
  	});
  }

  function initializeAddedNodeInclusionInput(nonIlaNodes) {
  	console.log(initializeAddedNodeInclusionInput);
  	$(".newNodeInclusion").each(function(index, value) {
  		// console.log($(this));
  		let srcNode = $(this)
  		.parent()
  		.parent()
  		.children()
  		.eq(2)
  		.text();
  		let destNode = $(this)
  		.parent()
  		.parent()
  		.children()
  		.eq(3)
  		.text();
  		// console.log("srcNode::",srcNode);
  		// console.log(" destNode::",destNode);
  		let source = nonIlaNodes.filter(obj => {
  			return obj.value != srcNode && obj.value != destNode;
  		});
  		$(this).editable("option", "source", source);
  		// $(this).editable('setValue',source[0].value);
  	});
  }

  var getNodeDegree = function(destNodeId) {
  	var id = getNodeById(destNodeId);
  	console.log("getNodeDegree --- Nodeid :" + id);
  	var el = graph.getCell(id);
  	var neighbours = graph.getNeighbors(el);
  	return neighbours.length;
  };

  function initializeNewCircuitRow(data) {
  	var nodesArr = [];

  	console.log("Data in new Circuit rows");
  	console.log(data);
  	console.log(data.NodeData);

  	for (var i = -1; i < data.NodeData.length; i++) {
  		var nodeObj = {};

  		if (i == -1) {
  			// Case for Select Node Option
  			nodeObj.text = "Select Node";
  			nodeObj.value = 0;
  		} else {
  			nodeObj.value = data.NodeData[i].NodeId;
  			nodeObj.text = data.NodeData[i].NodeId;
  		}

  		nodesArr.push(nodeObj);

  		console.log(nodesArr);
  	}

  	$(".newMultiplier")
  	.attr("title", "Multiplier to Circuit")
  	.tooltip()
  	.editable(MultiplierInitializeObj);

  	$(".newdestNode")
  	.attr("title", "Select Dest Node ")
  	.tooltip()
  	.editable({
  		type: "select",
  		value: 0,
  		source: nodesArr,
  		onblur: "submit",
  		validate: function(value) {
  			// fDestNodeValidate(value,$(this));
  			// console.log($(this))
  			console.log("value in dest node" + $.trim(value));
  			console.log(
  					"value in src node" +
  					$(this)
  					.parent()
  					.prev()
  					.children()
  					.html()
  			);
  			var hubNodeId = 1;
  			if ($.trim(value) == "" || $.trim(value) == 0) {
  				return "This field is required";
  			}
  			if (
  					$.trim(value) ==
  						$(this)
  						.parent()
  						.prev()
  						.children()
  						.html()
  			) {
  				return "Source and Destination Cannot be same.";
  			} else if (
  					nptGlobals.NetworkTopology == nptGlobals.TopologyHubRingStr &&
  					$(this)
  					.parent()
  					.prev()
  					.children()
  					.html() != "Select Node"
  			) {
  				/*
  				 * if($.trim(value)==hubNodeId) { : } else
  				 * if($(this).parent().prev().children().html()!=hubNodeId) return
  				 * 'Hub node should be either Source or Destination node.';
  				 */
  				if (
  						$.trim(value) == hubNodeId ||
  						$(this)
  						.parent()
  						.prev()
  						.children()
  						.html() == hubNodeId
  				) {
  				} else {
  					return "Hub node should be either Source or Destination node.";
  				}
  			}

  			/*
  			 * selectedNodeArr[0]=$.trim(value);
  			 * console.log("selectedNodeArr[0]"+selectedNodeArr[0]);
  			 */
  		}
  	});

  	$(".newsrcNode")
  	.attr("title", "Select Src Node ")
  	.tooltip()
  	.editable({
  		type: "select",
  		value: 0,
  		source: nodesArr,
  		onblur: "submit",
  		validate: function(value) {
  			// fSourceNodeValidate(value,$(this));
  			// console.log($(this))
  			console.log("value in src node" + $.trim(value));
  			console.log(
  					"value in dest node" +
  					$(this)
  					.parent()
  					.next()
  					.children()
  					.html()
  			);
  			var hubNodeId = 1;
  			console.log(
  					$(this)
  					.parent()
  					.next()
  					.children()
  					.html()
  			);
  			if ($.trim(value) == "" || $.trim(value) == 0) {
  				return "This field is required";
  			} else if (
  					$.trim(value) ==
  						$(this)
  						.parent()
  						.next()
  						.children()
  						.html()
  			) {
  				return "Source and Destination Cannot be same.";
  			} else if (
  					nptGlobals.NetworkTopology == nptGlobals.TopologyHubRingStr &&
  					$(this)
  					.parent()
  					.next()
  					.children()
  					.html() != "Select Node"
  			) {
  				/*
  				 * if($.trim(value)==hubNodeId) {
  				 *  } else if($(this).parent().next().children().html()!=hubNodeId)
  				 * return 'Hub node should be either Source or Destination node.';
  				 */
  				if (
  						$.trim(value) == hubNodeId ||
  						$(this)
  						.parent()
  						.next()
  						.children()
  						.html() == hubNodeId
  				) {
  				} else {
  					return "Hub node should be either Source or Destination node.";
  				}
  			}

  			/*
  			 * console.log('nodesArr') console.log(nodesArr); for(var i=1;i<nodesArr.length;i++) {
  			 * if(nodesArr[i].value==$.trim(value)) { nodesArr.splice(i,1); break; } }
  			 * console.log("$('.newdestNode')"); console.log($(".newdestNode"))
  			 */
  		}
  	});

  	$(".newCapacity")
  	.attr("title", "Edit Capacity ")
  	.tooltip()
  	.editable(CapacityInitializeObj);

  	$(".newLineRate")
  	.attr("title", "Edit Capacity ")
  	.tooltip()
  	.editable(LineRateInitializeObj);

  	$(".newColorPreference")
  	.attr("title", "Save ColorPreference")
  	.tooltip()
  	.editable(ColorInitializeObj);
  	console.log("in new servicetype");
  	$(".serviceType")
  	.attr("title", "Edit ")
  	.tooltip()
  	.editable(ServiceTypeInitializeObj);


  	$(".newVendorLabel")
  	.attr("title", "Edit Label")
  	.tooltip()
  	.editable(VendorLabelInitializeobj);

  	$(".newPathType")
  	.attr("title", "Edit ")
  	.tooltip()
  	.editable(PathTypeInitializeObj);

  	$(".newProtectionType")
  	.attr("title", "Edit ")
  	.tooltip()
  	.editable(ptcTypeInitializeObj);

  	$(".newProtectionMechanism")
  	.attr("title", "Edit ")
  	.tooltip()
  	.editable(PtcMechClientInitializeObj);

  	$(".newChannelProtection")
  	.attr("title", "Edit ")
  	.tooltip()
  	.editable(channelPtcInitializeObj);

  	$(".newClientProtection")
  	.attr("title", "Edit ")
  	.tooltip()
  	.editable(clientPtcInitializeObj);

  	$(".newLambdaBlocking")
  	.attr("title", "Edit ")
  	.tooltip()
  	.editable(lambdaBlkInitializeObj);

  	/** *** Node Inclusion **** */
  	let nonIlaNodes = fGetNonIlaNodesSourceArray();

  	$(".newNodeInclusion")
  	.attr("title", "Edit ")
  	.tooltip()
  	.editable(NodeInitializeObj);

  	initializeAddedNodeInclusionInput(nonIlaNodes);

  	$(".footable").footable();
  	fTopologyChecks();
  }

  /*
   * var lineRateServiceTypeSource={
   * 1:[{value:0,text:'None'},{value:1,text:'STM64'},{value:2,text:nptGlobals.ServiceTypeOTU2},{value:3,text:nptGlobals.ServiceTypeFC_1200},{value:4,text:nptGlobals.ServiceTypeEthernet}],
   * 2:[{value:0,text:'None'},{value:1,text:nptGlobals.ServiceTypeOTU2},{value:2,text:nptGlobals.ServiceTypeEthernet}],
   * 3:[{value:0,text:'None'},{value:1,text:nptGlobals.ServiceTypeOTU2},{value:2,text:nptGlobals.ServiceTypeEthernet}] };
   */

  function lineRateValidation(value, $this) {
  	// console.log($(this))
  	// console.log("value of LineRate "+$.trim(value))
  	let lineRate = $.trim(value);
  	let capacity = $this
  	.parent()
  	.prev()
  	.children()
  	.html(); // Client-rate
  	// if (capacity == nptGlobals.ClientRate_10_Agg)
  	// capacity = nptGlobals.ClientRate_10_Agg;

  	var $ServiceType = $this
  	.parent()
  	.prev()
  	.prev()
  	.prev()
  	.children();
  	var $ProtectionType = $this
  	.parent()
  	.next()
  	.next()
  	.children();
  	var $ProtectionMechanism = $this
  	.parent()
  	.next()
  	.next()
  	.next()
  	.next()
  	.children();
  	var $channelProtection = $this
  	.parent()
  	.next()
  	.next()
  	.next()
  	.next()
  	.next()
  	.children();
  	var $clientProtection = $this
  	.parent()
  	.next()
  	.next()
  	.next()
  	.children();
  	// console.log($this);
  	// console.log("Capacity:"+capacity);

  	// LineRate 1:10G , 2:100G , 3:200G

  	if (
  			(lineRate == nptGlobals.LineRate_100 ||
  					lineRate == nptGlobals.LineRate_200) &&
  					capacity < 10
  	) {
  		return "Line Rate of 100G and 200G is not applicable for Capacity less than 10G";
  	}

  	// 10G Aggregator
  	if (
  			lineRate == nptGlobals.LineRate_10 &&
  			capacity == nptGlobals.ClientRate_10_Agg
  	) {
  		return "10G (Aggregator) Client rate is applicable only for line rate > 100G";
  	}

  	if (
  			lineRate != nptGlobals.LineRate_100 &&
  			capacity == nptGlobals.ClientRate_100
  	) {
  		return "100G Client rate is applicable only for 100G line rate";
  	}

  	if (lineRate == nptGlobals.LineRate_10) {
  		// console.log($this.parent().prev().children().html());

  		// set client protection to none for 10G lineRate
  		$ProtectionType.editable("setValue", nptGlobals.PtcTypeUnprotected);
  		$ProtectionType.editable("option", "disabled", true);
  		$ProtectionMechanism.editable("setValue", nptGlobals.NoneStr);
  		$ProtectionMechanism.editable("option", "disabled", true);

  		$channelProtection.editable("setValue", nptGlobals.NoStr);
  		$channelProtection.editable("option", "disabled", true);

  		$clientProtection.editable("setValue", nptGlobals.NoStr);
  		$clientProtection.editable("option", "disabled", true);

  		let serviceTypeSrc = nptGlobals.fGetServiceTypeSrc(capacity, lineRate);
  		$ServiceType.editable("option", "source", serviceTypeSrc);
  		$ServiceType.editable("setValue", serviceTypeSrc[0].value);
  	} else if (lineRate == 100) {
  		if (capacity == 4) {
  			$ServiceType.editable(
  					"option",
  					"source",
  					nptGlobals.fGetServiceTypeSrc(10, 100)
  			);
  			$ServiceType.editable(
  					"setValue",
  					nptGlobals.fGetServiceTypeSrc(10, 100)[0].value
  			);
  		}

  		if (nptGlobals.NetworkTopology != nptGlobals.TopologyLinearStr) {
  			$ProtectionType.editable("setValue", nptGlobals.PtcTypeOnePlusOne);
  			$ProtectionType.editable("option", "disabled", false);
  			$ProtectionMechanism.editable("setValue", nptGlobals.PtcMechYCable);
  			$ProtectionMechanism.editable("option", "disabled", false);

  			$channelProtection.editable("setValue", nptGlobals.NoStr);
  			$channelProtection.editable("option", "disabled", false);
  		}
  	} // case when lineRate == 100 and lineRate== 200
  	else {
  		// console.log($this.parent().prev().children().html());
  		// $this.parent().prev().prev().prev().children().editable('setValue',0);
  		$ServiceType.editable(
  				"option",
  				"source",
  				nptGlobals.fGetServiceTypeSrc(100, 200)
  		);
  		$ServiceType.editable(
  				"setValue",
  				nptGlobals.fGetServiceTypeSrc(100, 200)[0].value
  		);

  		if (nptGlobals.NetworkTopology != nptGlobals.TopologyLinearStr) {
  			$ProtectionType.editable("setValue", nptGlobals.PtcTypeOnePlusOne);
  			$ProtectionType.editable("option", "disabled", false);
  			$ProtectionMechanism.editable("setValue", nptGlobals.PtcMechYCable);
  			$ProtectionMechanism.editable("option", "disabled", false);

  			$channelProtection.editable("setValue", nptGlobals.NoStr);
  			$channelProtection.editable("option", "disabled", false);
  		}
  	}
  }

  function fSourceNodeValidate(value, that) {
  	// console.log($(this))
  	// console.log("value in src node"+$.trim(value));
  	// console.log("value in dest node"+that.parent().next().children().html());
  	var hubNodeId = 1;
  	if ($.trim(value) == "" || $.trim(value) == 0) {
  		return "This field is required";
  	} else if (
  			$.trim(value) ==
  				that
  				.parent()
  				.next()
  				.children()
  				.html()
  	) {
  		return "Source and Destination Cannot be same.";
  	} else if (
  			nptGlobals.NetworkTopology == nptGlobals.TopologyHubRingStr &&
  			that
  			.parent()
  			.next()
  			.children()
  			.html() != hubNodeId
  	) {
  		return "Hub node should be either Source or Destination node.";
  	}

  	/*
  	 * console.log('nodesArr') console.log(nodesArr); for(var i=1;i<nodesArr.length;i++) {
  	 * if(nodesArr[i].value==$.trim(value)) { nodesArr.splice(i,1); break; } }
  	 * console.log("$('.newdestNode')"); console.log($(".newdestNode"))
  	 */
  }

  function fDestNodeValidate(value, that) {
  	// console.log($(this))
  	// console.log("value in dest node"+$.trim(value))
  	// console.log("value in src node"+that.parent().prev().children().html());
  	var hubNodeId = 1;
  	console.log("destnation  node"+that.parent().prev().children().html());
  	if ( 
  			$.trim(value) ==
  				that
  				.parent()
  				.prev()
  				.children()
  				.html()
  	) { 
  		return "Source and Destination Cannot be same.";
  	} else if (
  			nptGlobals.NetworkTopology == nptGlobals.TopologyHubRingStr &&
  			that
  			.parent()
  			.prev()
  			.children()
  			.html() != hubNodeId
  	) {
  		return "Hub node should be either Source or Destination node.";
  	}

  	/*
  	 * selectedNodeArr[0]=$.trim(value);
  	 * console.log("selectedNodeArr[0]"+selectedNodeArr[0]);
  	 */
  }

  function capacityValidation(value, $this) {
  	console.log($this.html());
  	// console.log("value of LineRate "+$.trim(value))
  	let clientRate = $.trim(value),
  	lineRate = $this
  	.parent()
  	.next()
  	.children()
  	.html(),
  	$ServiceType = $this
  	.parent()
  	.prev()
  	.prev()
  	.children();
  	console.log("client rate" +clientRate);
  	let aggregatorBtn = $($this.parent().find(".capacity-client-btn"));
  	console.log(" ****** aggregatorBtn ***** ", aggregatorBtn);
  	if (clientRate == nptGlobals.ClientRate_10_Agg)
  		// Debug
  		aggregatorBtn.show();
  	// $(aggregatorBtn.get(0)).append(`<i class="fa fa-plus text-success cursor"
  	// aria-hidden="true"
  	// onClick="aggregatorClientDialog($(this).parent())"></i>`);
  	// $(aggregatorBtn.get(0)).empty();
  	else aggregatorBtn.hide();

  	// console.log("LineRate:"+lineRate);
  	if (
  			(clientRate == nptGlobals.ClientRate_1 ||
  					clientRate == nptGlobals.ClientRate_1_25 ||
  					clientRate == nptGlobals.ClientRate_2_5) &&
  					(lineRate == nptGlobals.LineRate_100 || lineRate == nptGlobals.LineRate_200)
  	) {
  		return "Line Rate of 100G and 200G is not applicable for Capacity less than 10G";
  	} else if (
  			clientRate == nptGlobals.ClientRate_100 &&
  			lineRate != nptGlobals.LineRate_100
  	) {
  		return "100G Client rate is applicable only for 100G line rate";
  	} else {
  		let serviceTypeSrc = nptGlobals.fGetServiceTypeSrc(clientRate, lineRate);
  		$ServiceType.editable("option", "source", serviceTypeSrc);
  		$ServiceType.editable("setValue", serviceTypeSrc[0].value);

  		// if (clientRate == 1) {
  		// $ServiceType.editable('option', 'source', lineRate1Gx10G);
  		// $ServiceType.editable('setValue', 2);
  		// }
  		// else if (clientRate == 1.25) {
  		// $ServiceType.editable('option', 'source', lineRate1_25Gx10G);
  		// $ServiceType.editable('setValue', 1);
  		// }
  		// else if (clientRate == 2.5) {
  		// $ServiceType.editable('option', 'source', lineRate2_5Gx10G);
  		// $ServiceType.editable('setValue', 3);
  		// }
  		// else if (clientRate == 10) {
  		// $ServiceType.editable('option', 'source', lineRate10Gx10G);
  		// $ServiceType.editable('setValue', 1);
  		// }
  		// else if (clientRate == 100) {
  		// $ServiceType.editable('option', 'source', lineRate100Gx200G);
  		// $ServiceType.editable('setValue', 10);
  		// }
  	}
  }
  function ServiceTypeValidation(value, $this) {
  	console.log("this" +$this.html());
  	// console.log("value of LineRate "+$.trim(value))
  	let ServiceType = $.trim(value);
      console.log($($this.parent().find(".service-type-btn")));
  	let aggregatorBtn = $($this.parent().find(".service-type-btn"));
  	console.log(" ****** aggregatorBtn ***** ", aggregatorBtn);

  	if (ServiceType === nptGlobals.ServiceTypeOTU2_LOWER)
  	{ // Debug
  		aggregatorBtn.show();
  	}

  	else
  	{
  		aggregatorBtn.hide();
  	}

  }

  function fServiceTypeValidationforPOTP(value, $this) {

  	let ClientRate = $.trim(value);

  	let Service = [] ;
  	$ServiceType = $this
  	.parent()
  	.prev()
  	.children();
  	 $TributoryId = $this.parent().next().children();
  	
  	console.log("in servicetypePOTP",$ServiceType);
      if(ClientRate === nptGlobals.ClientRate_1_25)
  	{   console.log("in 1.25");
  	Service[0] = nptGlobals.ServiceTypeOTU0;
  	$ServiceType.editable("option", "source", Service);
  	$ServiceType.editable("setValue", Service[0]); 
  	$TributoryId.editable("option","source",nptGlobals.fGetTributoryIdSrc(1.25));
  	$TributoryId.editable("setValue",nptGlobals.fGetTributoryIdSrc(1.25)[0].value);
  	
  	}
  	else if(ClientRate === nptGlobals.ClientRate_2_5)
  	{ 
  	console.log("in 2.5");   
  	Service[0] = nptGlobals.ServiceTypeOTU1;
  	console.log("Service" +Service);
  	$ServiceType.editable("option", "source", Service);
  	$ServiceType.editable("setValue", Service[0]); 
  	
  	$TributoryId.editable("option","source",nptGlobals.fGetTributoryIdSrc(2.5));
  	$TributoryId.editable("setValue",nptGlobals.fGetTributoryIdSrc(2.5)[0].value);
  	
     }

  }









  function protectionMechanismValidation(value, $this) {
  	// console.log($(this))
  	// console.log("value of Protection Mechanism "+$.trim(value))
  	const protectionType = $this
  	.parent()
  	.prev()
  	.prev()
  	.children()
  	.html();
  	const clientProt = $this
  	.parent()
  	.prev()
  	.children()
  	.html();

  	console.log("prot Type:", protectionType);
  	console.log("Prot Mech value :", $.trim(value));

  	if (
  			$.trim(value) == nptGlobals.NoStr &&
  			protectionType != nptGlobals.PtcTypeUnprotected
  	) {
  		return "With protection , protection mechanism cannot be UnProtected.";
  	}
  	// else if(($.trim(value)!=0) && clientProt!=nptGlobals.YesStr){
  	// return 'With no client protection , protection mechanism cannot be
  	// none.';
  	// }
  }

  function clientProtectionValidation(value, $this) {
  	let $channelProtection = $this
  	.parent()
  	.next()
  	.next()
  	.children();
  	let $protectionMechanism = $this
  	.parent()
  	.next()
  	.children();
  	value = $.trim(value);
  	// console.log("prot Type:"+protectionType);

  	console.log("clientProtectionValidation Value -- ", value);

  	// When ptc changes from none to some ptc , then initialize with initial
  	// values
  	if (
  			value == nptGlobals.NoStr &&
  			$channelProtection.text() == nptGlobals.NoStr
  	) {
  		$this.editable("setValue", nptGlobals.YesStr);
  		// return;
  	} else if (
  			value == nptGlobals.NoStr &&
  			$channelProtection.text() == nptGlobals.YesStr
  	) {
  		// return;
  	} else if (value == nptGlobals.YesStr) {
  		// Change Protection Mechanism Source
  		$protectionMechanism.editable(
  				"option",
  				"source",
  				clientProtectionMechanismSrc
  		);

  		$channelProtection.editable("setValue", nptGlobals.NoStr); // Prot
  		// mechanism
  		// cannot be
  		// none if
  		// Protection is
  		// there.
  		$protectionMechanism.editable("setValue", nptGlobals.PtcMechYCable); // Prot
  		// mechanism
  		// cannot
  		// be
  		// none
  		$protectionMechanism.editable("option", "disabled", false); // if client
  		// prot is there
  		// , enable prot
  		// mechanism
  	} else {
  		return "Both Client and Channel protection cannot be absent.";
  	}
  }

  function channelProtectionValidation(value, $this) {
  	let $clientProtection = $this
  	.parent()
  	.prev()
  	.prev()
  	.children();
  	let $protectionMechanism = $this
  	.parent()
  	.prev()
  	.children();
  	value = $.trim(value);
  	// console.log("prot Type:"+protectionType);

  	console.log("channelProtectionValidation Value -- ", value);

  	if (value == nptGlobals.YesStr) {
  		$clientProtection.editable("setValue", nptGlobals.NoStr); // Both channel
  		// and client
  		// prot cannot
  		// be Yes at the
  		// same time

  		$protectionMechanism.editable(
  				"option",
  				"source",
  				channelProtectionMechanismSrc
  		);
  		$protectionMechanism.editable("setValue", nptGlobals.PtcMechOPX); // Prot
  		// mechanism
  		// set
  		// to
  		// OPX
  		$protectionMechanism.editable("option", "disabled", false);

  		// $protectionMechanism.editable('setValue',0);// Prot mechanism set to none
  		// $protectionMechanism.editable('option', 'disabled', true);//if prot is
  		// none or channel prot is there , disable prot mechanism
  	} else {
  		return "Both Client and Channel protection cannot be absent.";
  	}
  }

  function protectionTypeValidation(value, $this) {
  	console.log("Ptc Type :: " + $.trim(value));

  	let ptcType = $.trim(value);
  	console.log("protectionTypeValidation $this", $this);
  	if (isExternalValidateTrigger == 1) {
  		$this = $($this.get(0));
  		// console.log($this.get(0));
  	}

  	var $currentRowColumns = $this
  	.parent()
  	.parent()
  	.children()
  	.not(".hidden");

  	var pathType = $currentRowColumns
  	.eq(13)
  	.children()
  	.html();
  	console.log("PathType",pathType);
  	var $channelProtection = $currentRowColumns.eq(12).children();
  	var $ProtectionMechanism = $currentRowColumns.eq(11).children();
  	var $ClientProtection = $currentRowColumns.eq(10).children();

  	// If TwoFiberRing/Mesh then either Channel or Client protection can exist
  	// if(nptGlobals.NetworkTopology==nptGlobals.TopologyTwoFiberRingStr ||
  	// nptGlobals.NetworkTopology==nptGlobals.TopologyMeshStr)
  	// {
  	// //Debug
  	// $channelProtection.editable('option', 'disabled', true);
  	// $channelProtection.editable('setValue',0); //channel protection
  	// }

  	if (
  			ptcType == nptGlobals.PtcTypeOnePlusOnePlusTwoR &&
  			pathType == nptGlobals.DisjointPathStr
  	) {
  		// case 1+1+2R
  		var destNodeId = $this
  		.parent()
  		.parent()
  		.children()
  		.eq(3)
  		.children()
  		.html();
  		// console.log("Destination Node id :" , destNodeId)
  		var destNodeDegree = getNodeDegree(destNodeId);
  		// console.log("destNodeId ::"+destNodeId +" Degree :"+destNodeDegree);

  		if (destNodeDegree <= 2)
  			return "Destination node has degree <= 2 and path type is Disjoint. Not Allowed.";
  	}

  	// When Protection is unprotected, 1:1R , 1:2R , Low priority
  	if (
  			ptcType == nptGlobals.PtcTypeUnprotected ||
  			ptcType == nptGlobals.PtcTypeOneIsToOne ||
  			ptcType == nptGlobals.PtcTypeOneIsToTwoR ||
  			ptcType == nptGlobals.PtcTypeLowPriority
  	) {
  		// If protection type is none , then Channel and Client protection don't
  		// exist and will be set to NO
  		$ClientProtection.editable("setValue", nptGlobals.NoStr); // Prot
  		// mechanism
  		// cannot be
  		// none if
  		// Protection is
  		// there.
  		$ClientProtection.editable("option", "disabled", true); // Prot mechanism
  		// enabled
  		$ClientProtection.tooltip("disable");

  		$channelProtection.editable("setValue", nptGlobals.NoStr); // Prot
  		// mechanism
  		// cannot be
  		// none if
  		// Protection is
  		// there.
  		$channelProtection.editable("option", "disabled", true); // Prot
  		// mechanism
  		// enabled
  		$channelProtection.tooltip("disable");

  		// If protection type is none ,then ptc mech will also be none and user
  		// cannot change it
  		$ProtectionMechanism.editable("setValue", nptGlobals.NoneStr); // Prot
  		// mechanism
  		// enabled
  		$ProtectionMechanism.editable("option", "disabled", true); // if prot is
  		// none ,
  		// disable
  		// client prot
  		$ProtectionMechanism.tooltip("disable");
  	}
  	// When Protection is 1+1+R , 1+1+2R ,1+1
  	else {
  		// Enable both client and channel ptc
  		// clientProtectionValidation($ClientProtection.val(),$ClientProtection);
  		$ClientProtection.editable("option", "disabled", false);
  		$ClientProtection.editable("setValue", nptGlobals.YesStr);
  		$channelProtection.editable("option", "disabled", false);
  		$channelProtection.editable("setValue", nptGlobals.NoStr);

  		$ProtectionMechanism.editable(
  				"option",
  				"source",
  				clientProtectionMechanismSrc
  		);
  		$ProtectionMechanism.editable("setValue", nptGlobals.PtcMechYCable);
  		$ProtectionMechanism.editable("option", "disabled", false);

  		// protectionMechanismValidation($ProtectionMechanism.text(),$ProtectionMechanism);

  		// $ProtectionMechanism.editable('setValue',1); // Prot mechanism cannot be
  		// none if Protection is there.
  		// $ProtectionMechanism.editable('option', 'disabled', false);// Prot
  		// mechanism enabled
  	}
  }

  function fTopologyChecks() {
  	console.log(
  			"fTopologyChecks -- Topoogy:" + sessionStorage.getItem("Topology")
  	);
  	if (sessionStorage.getItem("Topology") == nptGlobals.TopologyLinearStr) {
  		// Not applicable for linear topology
  		$(".protectionMechanism").editable("option", "disabled", true);
  		$(".protectionType").editable("option", "disabled", true);
  		$(".channelProtection").editable("option", "disabled", true);
  		$(".clientProtection").editable("option", "disabled", true);
  		$(".newProtectionMechanism").editable("option", "disabled", true);
  		$(".newProtectionType").editable("option", "disabled", true);
  		$(".newClientProtection").editable("option", "disabled", true);
  		$(".newChannelProtection").editable("option", "disabled", true);

  		// Set Values to None
  		$(".protectionMechanism").editable("setValue", nptGlobals.NoneStr); // client
  		// protection
  		$(".protectionType").editable("setValue", nptGlobals.PtcTypeUnprotected); // protection
  		// type
  		$(".channelProtection").editable("setValue", nptGlobals.NoStr); // channel
  		// protection
  		$(".clientProtection").editable("setValue", nptGlobals.NoStr); // client
  		// protection
  		$(".newProtectionMechanism").editable("setValue", nptGlobals.NoneStr);
  		$(".newProtectionType").editable("setValue", nptGlobals.PtcTypeUnprotected);
  		$(".newClientProtection").editable("setValue", nptGlobals.NoStr);
  		$(".newChannelProtection").editable("setValue", nptGlobals.NoStr);
  	} else if (
  			nptGlobals.NetworkTopology == nptGlobals.TopologyTwoFiberRingStr ||
  			nptGlobals.NetworkTopology == nptGlobals.TopologyMeshStr
  	) {
  		// debug
  		// $('.channelProtection').editable('option', 'disabled', true);
  		// $('.newChannelProtection').editable('option', 'disabled', true);
  	}
  }

  var defaultCircuitGeneration = function(NodeDataArr, template) {
  	console.log("defaultCircuitGeneration() called ...");
  	var count = 1;
  	var state =
  		sessionStorage.getItem("NetworkState") == nptGlobals.GreenFieldStr
  		? nptGlobals.GreenFieldRowClassStr
  				: nptGlobals.NewRowClassStr;
  	var circuitRowLabel = getLabelTagsFromState(state);

  	for (i = 0; i < NodeDataArr.length; i++) {
  		var temp = i + 1;

  		while (temp < NodeDataArr.length) {
  			// console.log("temp"+temp + "NodeDataArr[temp] "+NodeDataArr[temp]);
  			var imageId = "#deleteImgId".concat(count);
  			// console.log("Button Add"+ imageId);

  			template += `<tr class=${state}><td><input type="checkbox" class="rowCheckBox" /> <span class="checkbox"></span>`;
  			template += `${circuitRowLabel}`;
  			template += `</td>`;
  			template += "<td class='hidden'>-1</td>";
  			template += `<td class = "switch_to_potp" > <label class="switch">
  				<input  type="checkbox">
  				<span class="slider round"></span>
  				</label>
  				</td>`;

  			template += `<td data-value='${
  				NodeDataArr[i].NodeId
  				}'><span class='badge'>${NodeDataArr[i].NodeId}</span></td>`;
  			template += `<td data-value='${
  				NodeDataArr[temp].NodeId
  				}'><span class='badge'>${NodeDataArr[temp].NodeId}</span></td>`;
  			template += `<td><span class="serviceType"> </span>
  				<span class="service-type-btn">
  				<i class="fa fa-plus text-success cursor" title="Add OTU2 LOWER Service Type " aria-hidden="true" onClick="ServiceTypeOTU2LowerClientDialog($(this).parent())"></i>
  				</span>      
  				<span class="OTU2_Lower"></span> </td>`;
  			template += `<td><span class="vendorLabel"></span></td>
  				<td>
  				<span class="capacity"></span>
  				<span class="capacity-client-btn">
  				<i class="fa fa-plus text-success cursor" title="Add Aggregator Clients" aria-hidden="true" onClick="aggregatorClientDialog($(this).parent())"></i>
  				</span>
  				<span class="agg-circuits"></span>
  				</td>								
  				<td><span class="lineRate"></span></td>
  				<td><span class="multiplier"></span></td>
  				<td><span class="protectionType"></span></td>
  				<td><span class="clientProtection"></span></td>
  				<td><span class="protectionMechanism"></span></td>
  				<td><span class="channelProtection"></span></td>
  				<td><span class="pathType"></span></td>
  				<td><span class="lambdaBlocking"></span></td>
  				<td><span class="ColorPreference" data-type="checklist"></span></td>
  				<td><span class="nodeInclusion" data-type="checklist"></span></td>
  				<td><i class="fa fa-trash fa-2x text-danger cursor" aria-hidden="true" onClick="deleteRow(this)"></i></td>
  				</tr>`;

  			temp++;
  			count++;
  		}
  		// console.log("Node"+i+":-"+data.id[i]);
  	}

  	template += `</tbody></table></div>`;

  	template += fgetCircuitButtonsTemplate();

  	// template+=`<div class="btn-group btn-group-lg btn-block
  	// circuitButtonGroup">`;
  	// template+=`<button class='btn btn-lg ' type='button'
  	// id='AddCircuitTableRowBtn'><i class='fa fa-plus' aria-hidden='true'> Add
  	// Circuit </i></button>`;
  	// template+=`<button class='btn btn-lg ' type='button'
  	// id='DeleteCircuitTableRowBtn'><i class='fa fa-minus' aria-hidden='true'>
  	// Delete Circuit </i></button>`;
  	// template+=`<button class='btn btn-lg ' type='button'
  	// id='DefaultCircuitTableRowBtn'><i class='fa fa-refresh'
  	// aria-hidden='true'> Default Circuit </i></button>`;
  	// template+=`<button class='btn btn-lg ' type='button'
  	// id='SaveCircuitTableBtn'><i class="fa fa-floppy-o" aria-hidden="true">
  	// Save Circuit </i></button>`;
  	// template+=`<button class='btn btn-lg btn-full-width ' type='button'
  	// id='SaveCircuitExecuteRWATableRowBtn'><i class="fa fa-floppy-o"
  	// aria-hidden="true"> Save Circuit & Execute RWA</i></button>`;
  	// template+=`</div>`;
  	console.log("template in default"+template);
  	$("#circuits-view")
  	.empty()
  	.append(template);
  	// console.log(template);
  	console.log("Circuit Table filling Initial Data.....");

  	initializeCircuitRows();
  };

  var defaultCircuitGenerationHubRing = function(NodeDataArr, template) {
  	console.log("defaultCircuitGenerationHubRing() called ...");
  	var hubNodeId = NodeDataArr[0].NodeId;
  	var temp = 1,
  	count = 1;

  	var state =
  		sessionStorage.getItem("NetworkState") == nptGlobals.GreenFieldStr
  		? nptGlobals.GreenFieldRowClassStr
  				: nptGlobals.NewRowClassStr;
  	var circuitRowLabel = getLabelTagsFromState(state);

  	while (temp < NodeDataArr.length) {
  		// console.log("temp"+temp + "NodeDataArr[temp] "+NodeDataArr[temp]);
  		var imageId = "#deleteImgId".concat(count);
  		console.log("Button Add" + imageId);

  		template += `<tr class=${state}><td><input type="checkbox" class="rowCheckBox" /> <span class="checkbox"></span>`;
  		template += `${circuitRowLabel}`;
  		template += `</td>`;
  		template += "<td class='hidden'>-1</td>";
  		template += `<td  class = "switch_to_potp" > <label class="switch">
  			<input type="checkbox" checked>
  			<span class="slider round"></span>
  			</label> </td> `;

  		template += `<td data-value='${hubNodeId}'><span class='badge'>${hubNodeId}</span></td>`;
  		template += `<td data-value='${
  			NodeDataArr[temp].NodeId
  			}'><span class='badge'>${NodeDataArr[temp].NodeId}</span></td>`;
  		template += `<td><span class="serviceType"></span>
  			<span class="service-type-btn">
  			<i class="fa fa-plus text-success cursor" title="Add OTU2 LOWER Service Type " aria-hidden="true" onClick="ServiceTypeOTU2LowerClientDialog($(this).parent())"></i>
  			</span>      
  			<span class="OTU2_Lower"></span> 
  			</td>`;
  		template += `<td><span class="vendorLabel"></span></td>
  			<td>
  			<span class="capacity"></span>
  			<span class="capacity-client-btn">
  			<i class="fa fa-plus text-success cursor" title="Add Aggregator Clients" aria-hidden="true" onClick="aggregatorClientDialog($(this).parent())"></i>
  			</span>
  			<span class="agg-circuits"></span>
  			</td>								
  			<td><span class="lineRate"></span></td>
  			<td><span class="multiplier"></span></td>
  			<td><span class="protectionType"></span></td>
  			<td><span class="clientProtection"></span></td>
  			<td><span class="protectionMechanism"></span></td>
  			<td><span class="channelProtection"></span></td>
  			<td><span class="pathType"></span></td>
  			<td><span class="lambdaBlocking"></span></td>
  			<td><span class="ColorPreference" data-type="checklist"></span></td>
  			<td><span class="nodeInclusion" data-type="checklist"></td>
  			<td><i class="fa fa-trash fa-2x text-danger cursor" aria-hidden="true" onClick="deleteRow(this)"></i></td>
  			</tr>`;
  		temp++;
  		count++;
  	}
  	// console.log("Node"+i+":-"+data.id[i]);

  	// template+="<tr><td><button class='btn btn-lg btn-block' type='button'
  	// id='SaveCircuitTableBtn'>Save
  	// Circuit</button></td></tr></tbody></table>";
  	template += `</tbody></table></div>`;

  	template += fgetCircuitButtonsTemplate();

  	// template+=`<div class="btn-group btn-group-lg btn-block
  	// circuitButtonGroup">`;
  	// template+=`<button class='btn btn-lg ' type='button'
  	// id='AddCircuitTableRowBtn'><i class='fa fa-plus' aria-hidden='true'> Add
  	// Circuit </i></button>`;
  	// template+=`<button class='btn btn-lg ' type='button'
  	// id='DeleteCircuitTableRowBtn'><i class='fa fa-minus' aria-hidden='true'>
  	// Delete Circuit </i></button>`;
  	// template+=`<button class='btn btn-lg ' type='button'
  	// id='DefaultCircuitTableRowBtn'><i class='fa fa-refresh'
  	// aria-hidden='true'> Default Circuit </i></button>`;
  	// template+=`<button class='btn btn-lg ' type='button'
  	// id='SaveCircuitTableBtn'><i class="fa fa-floppy-o" aria-hidden="true">
  	// Save Circuit </i></button>`;
  	// template+=`<button class='btn btn-lg btn-full-width ' type='button'
  	// id='SaveCircuitExecuteRWATableRowBtn'><i class="fa fa-floppy-o"
  	// aria-hidden="true"> Save Circuit & Execute RWA</i></button>`;
  	// template+=`</div>`;
  	// template+=`<button class='btn btn-primary btn-block'type='button'
  	// id='SaveCircuitExecuteRWATableRowBtn'><i class='fa fa-check-square'
  	// aria-hidden='true'> Save Circuit & Execute RWA</i></button>`;

  	$("#circuits-view")
  	.empty()
  	.append(template);
  	// console.log(template);
  	console.log("Circuit Table filling Initial Data.....");

  	initializeCircuitRows();
  };

  function deleteRow($this) {
  	console.log($this);
  	if (
  			!$($this)
  			.closest("tr")
  			.hasClass("uneditable-brown-field")
  	) {
  		$($this)
  		.closest("tr")
  		.remove();
  	} else {
  		bootBoxDangerAlert(
  				"Can't delete green field client. Go to demand matrix to delete green field clients."
  		);
  	}
  }

  /** ****** Trigger Aggregator Clients Dialog ******** */
  function aggregatorClientDialog($this) {
  	console.log(
  			"aggregatorClientDialog Row Index :: ",
  			$this
  			.parent()
  			.parent()
  			.index(),$this.parent().parent()
  	);
  	let rowIndex = $this
  	.parent()
  	.parent()
  	.index();

  	let title = "Clients Info for 10G Aggregation";
  	let template = fGetAggregatorCircuitsTemplate(rowIndex);

  	bootbox
  	.dialog({
  		message: template,
  		title: title,
  		animation: true,
  		className: "cell-property-dialog",
  		backdrop: false,
  		buttons: {
  			"Add Client": {
  				// label: "Success!",
  				className: "btn btn--default aggregator-add-client-btn",
  				callback: function() {
  					addClientToAggregatorTable($(this));
  					return false;
  				}
  			},
  			Save: {
  				// label: "Success!",
  				className: "btn btn--default",
  				callback: function() {
  					let circuitsHtml = $(this)
  					.find(".aggregator-table tbody")
  					.html();

  					// Validate 10g max capacity for one 10g mux card
  					let circuitsObject = $(this).find(".aggregator-table tbody tr");
  					let totalCapacity = 0;
  					for (let i = 0; i < circuitsObject.length; i++) {
  						let row = $(circuitsObject.get(i));
  						let cap = row.find(".agg-client-rate").html();
  						let mul = row.find(".agg-multiplier").html();
  						// console.log("Cap:", cap, " Mul:", mul);
  						totalCapacity += Number(cap * mul);
  					}
  					// console.log("totalCapacity :", totalCapacity);
  					if (totalCapacity > 10) {
  						bootBoxDangerAlert("Max capacity can be 10G.");
  						return false;
  					}

  					saveAggregatorClients(rowIndex, circuitsHtml);
  					bootBoxSuccessAlert("Aggregator clients saved successfully.");
  					// return false;
  				}
  			},
  			Close: {
  				className: "btn btn--default",
  				callback: function() {}
  			}
  		}
  	})
  	.draggable();
  }

  /** ****** Aggregator Clients Template for a row******** */
  function fGetAggregatorCircuitsTemplate(index) {
  	// <div><button class="btn btn--default aggregator-add-client-btn"
  	// style="margin:auto;">Add Clients</button></div>
  	// <hr>
  	let template = `<table class="footable table table-fixed table-striped aggregator-table">
  		<thead>
  		<th>Client Rate</th>
  		<th>Service Type</th>			
  		<th>Multiplier</th>
  		<th>Delete</th>
  		</thead>
  		<tbody>`;
  	let alreadySetClients = $(
  			"#TrafficMatrixTable tbody > tr:eq(" + index + ") td .agg-circuits"
  	).html();
  	console.log("*********************** ", alreadySetClients);
  	template += alreadySetClients;
  	template += `</tbody>	
  		</table>`;
  	// console.log("fGetAggregatorCircuitsTemplate", template);
  	return template;
  }

  /** ****** Add Aggregator Clients ******** */
  $("body").delegate(".aggregator-add-client-btn", "click", function() {
  	console.log("add-client-btn clicked");
  	addClientToAggregatorTable();
  });




  function addClientToAggregatorTable(that) {
  	let template = fGetAggAddCircuitTemplate();

  	that.find(".aggregator-table tbody").append(template);
  	initializeAggClients(null);
  }
  function addClient1_25ToServiceTable(that,SrcNode) {
  	let template = fGetAddOTU2_LOWERServiceTypeCircuitTemplate();

  	that.find(".Service-type-table tbody").append(template);
  	initializeOTU_LOWERClients(null,SrcNode);
  }





  function fGetAggAddCircuitTemplate() {
  	let template = `<tr>
  		<td ><span class="agg-client-rate"></span></td>
  		<td ><span class="agg-service-type"></span></td>
  		<td ><span class="agg-multiplier"></span></td>
  		<td><i class="fa fa-trash fa-2x text-danger cursor" aria-hidden="true" onClick="deleteRow(this)"></i></td>
  		</tr>`;
  	return template;
  }





  /**
   * ****** Save Aggregator Clients in 10G Agg capacity
   * <td> ********
   */
  function saveAggregatorClients(rowIndex, circuits) {
  	console.log("saveAggregatorClients index ", rowIndex);
  	// console.log(circuits);

  	// console.log("circuits to save ", circuits);
  	$("#TrafficMatrixTable tbody > tr:eq(" + rowIndex + ") td .agg-circuits")
  	.empty()
  	.append(circuits);
  }




  /** ****** Initialize Aggregator Clients ******** */
  function initializeAggClients(data) {
  	console.log("initializeAggClients Data", data);
  	let circuitData = data;
  	if (circuitData !== null) {
  		console.log(circuitData.length);
  		_.each(circuitData, (tr, index) => {
  			let clientrate = $(tr)
  			.find(".agg-client-rate")
  			.text();
  			let serviceType = $(tr)
  			.find(".agg-service-type")
  			.text();
  			let multiplier = $(tr)
  			.find(".agg-multiplier")
  			.text();
  			console.log(tr, " ------", index, clientrate);
  			// $(tr).find(".agg-client-rate").editable('option', 'source',
  			// clientRateSrc.filter(clientrate => clientrate.value <= 10));
  			// $(tr).find(".agg-client-rate").editable('setValue', clientrate);
  			// $(tr).find(".agg-service-type").editable('option', 'source',
  			// nptGlobals.fGetServiceTypeSrc(clientrate, 10));
  			// $(tr).find(".agg-service-type").editable('setValue', serviceType);
  			// $(tr).find(".agg-multiplier").editable('option', 'source',
  			// multiplierArray.filter(elem => elem.value <= 16));
  			// $(tr).find(".agg-multiplier").editable('setValue', multiplier);
  			$(tr)
  			.find(".agg-client-rate")
  			.attr("title", "Edit ")
  			.tooltip()
  			.editable({
  				type: "select",
  				value: SrcNode,
  				source: clientRateSrc.filter(clientrate => clientrate.value < 10),
  				onblur: "submit",
  				validate: function(value) {
  					return aggClientRateValidation(value, $(this));
  				}
  			});
  			$(tr)
  			.find(".agg-service-type")
  			.attr("title", "Edit ")
  			.tooltip()
  			.editable({
  				type: "select",
  				value: serviceType,
  				source: nptGlobals.fGetServiceTypeSrc(clientrate, 10),
  				onblur: "submit"
  			});
  			$(tr)
  			.find(".agg-multiplier")
  			.attr("title", "Edit ")
  			.tooltip()
  			.editable({
  				type: "select",
  				value: multiplier,
  				source: multiplierArray.filter(elem => elem.value <= 16),
  				onblur: "submit"
  			});
  		});
  	} else {
  		$(".aggregator-table .agg-client-rate")
  		.attr("title", "Edit ")
  		.tooltip()
  		.editable({
  			type: "select",
  			value: 1,
  			source: clientRateSrc.filter(clientrate => clientrate.value < 10),
  			onblur: "submit",
  			validate: function(value) {
  				return aggClientRateValidation(value, $(this));
  			}
  		});

  		let serviceTypeSrc = nptGlobals.fGetServiceTypeSrc(1, 10);
  		$(".aggregator-table .agg-service-type")
  		.attr("title", "Edit ")
  		.tooltip()
  		.editable({
  			type: "select",
  			value: serviceTypeSrc[0].value,
  			source: serviceTypeSrc,
  			onblur: "submit"
  		});
  		$(".aggregator-table .agg-multiplier")
  		.attr("title", "Edit ")
  		.tooltip()
  		.editable({
  			type: "select",
  			value: 1,
  			source: multiplierArray.filter(elem => elem.value <= 16),
  			onblur: "submit"
  		});
  	}
  }

  /** ****** Aggregator ClientRate Validation ******** */
  function aggClientRateValidation(value, $this) {
  	// console.log($this);
  	// console.log("value of LineRate "+$.trim(value));
  	let clientRate = $.trim(value),
  	$ServiceType = $this
  	.parent()
  	.next()
  	.children();


  	let serviceTypeSrc = nptGlobals.fGetServiceTypeSrc(
  			clientRate,
  			nptGlobals.LineRate_10
  	);
  	$ServiceType.editable("option", "source", serviceTypeSrc);
  	$ServiceType.editable("setValue", serviceTypeSrc[0].value);
  }

  function fGetColumnsAggCircuitFromDb(serviceType, clientRate, multiplier) {
  	let template = `<td><span class="agg-client-rate editable editable-click" title="Edit " data-original-title="Edit ">${clientRate}</span></td>
  		<td><span class="agg-service-type editable editable-click" title="Edit " data-original-title="Edit ">${serviceType}</span></td>
  		<td><span class="agg-multiplier editable editable-click" title="Edit " data-original-title="Edit ">${multiplier}</span></td>
  		<td><i class="fa fa-trash fa-2x text-danger cursor" aria-hidden="true" onClick="deleteRow(this)"></i></td>`;
  	return template;
  }






  function fRenderAggregatorCircuits(circuitsData) {
  	console.log("**************fRenderAggregatorCircuits******************");
  	_.each(circuitsData, (circuit, i) => {
  		if (circuit.AggregatorCircuits !== undefined) {
  			console.log("********** Has aggregator circuits ***********", i);
  			let aggCircuits = JSON.parse(circuit.AggregatorCircuits);
  			// console.log("Agg circuits", aggCircuits);
  			let aggCircuitsTemplate = "";
  			_.each(aggCircuits, aggCircuit => {
  				// console.log("aggCircuit", aggCircuit);

  				aggCircuitsTemplate += `<tr>${fGetColumnsAggCircuitFromDb(
  					aggCircuit.ServiceType,
  					aggCircuit.ClientRate,
  					aggCircuit.Multiplier
  					)}</tr>`;
  			});

  			// console.log(aggCircuitsTemplate);

  			saveAggregatorClients(i, aggCircuitsTemplate);
  			console.log("Save Clients for Row :", i);
  		} else {
  			$(
  					"#TrafficMatrixTable tbody > tr:eq(" + i + ") td .agg-circuits"
  			).empty();
  		}
  	});
  }


  function fGetOTU2_lOWERServiceTypeTemplate(index) {
  	// <div><button class="btn btn--default aggregator-add-client-btn"
  	// style="margin:auto;">Add Clients</button></div>
  	// <hr>
  	let template = `<table class="footable table table-fixed table-striped Service-type-table">
  		<thead>
  		<th>Source Node</th>
  	    <th>Dest Node</th>	
  		<th>ServiceType</th>	
  		<th>ClientRate</th>	
  		<th>Tributory Id </th>	
  		<th>Protection Type</th>
  		<th>Path Type</th>
  		<th>Colour Preference</th>
  		<th>Node Inclusion</th>
          <th>Delete </th>
  		</thead>
  		<tbody>`;
  	let alreadySetClients = $(
  			"#TrafficMatrixTable tbody > tr:eq(" + index + ") td .OTU2_Lower"
  	).html();
  	console.log("*********************** ", alreadySetClients);
  	template += alreadySetClients;
  	template += `</tbody>	
  		</table>`;
  	// console.log("fGetAggregatorCircuitsTemplate", template);
  	return template;
  }

  $("body").delegate(".aggregator-add-client1.25-btn", "click", function() {
  	console.log("add-client1.25-btn clicked");
  	addClient1_25ToServiceTable();
  });

  $("body").delegate(".aggregator-add-client2.5-btn", "click", function() {
  	console.log("add-client2.5-btn clicked");
  	addClient2_5ToServiceTable();
  });


  function saveOTU2_LowerServiceClients(rowIndex, circuits) {
  	console.log("saveAggregatorClients index ", rowIndex);
  	// console.log(circuits);

  	// console.log("circuits to save ", circuits);
  	$("#TrafficMatrixTable tbody > tr:eq(" + rowIndex + ") td .OTU2_Lower")
  	.empty()
  	.append(circuits);
  }


  function fGetColumnsOTU2CircuitFromDb(SourceNode,DestNode,ServiceType,ClientRate,TributoryId,ProtType,PathType,ColourPreference,NodeInclusion) {
  	let template = `<td><span class="agg-SrcNode">${SourceNode}</span></td>
  	    <td><span class="agg-DestNode  editable editable-click" title="Edit " data-original-title="Edit ">${DestNode}</span></td>
  		<td><span class="agg-ServiceType editable editable-click" title="Edit " data-original-title="Edit ">${ServiceType}</span></td>
  		<td><span class="agg-ClientRate  editable editable-click" title="Edit " data-original-title="Edit ">${ClientRate}</span></td>
  		<td><span class="agg-TriId  editable editable-click" title="Edit " data-original-title="Edit ">${TributoryId}</span></td>
  		<td><span class="agg-ProtType  editable editable-click" title="Edit " data-original-title="Edit" >${ProtType}</span></td>
  		<td><span class="agg-PathType">${PathType}</span></td>
  		<td><span class="agg-Color" >${ColourPreference}</span></td>
  		<td><span class="agg-NodeInclusion">${NodeInclusion}</span></td>
  		<td><i class="fa fa-trash fa-2x text-danger cursor" aria-hidden="true" onClick="deleteRow(this)"></i></td>`;
  	return template;
  }


  function fGetAddOTU2_LOWERServiceTypeCircuitTemplate() {
  	let template = `<tr>
  		<td ><span class="agg-SrcNode"></span></td>
  		<td ><span  class="agg-DestNode"></span></td>
  		<td ><span class="agg-ServiceType"></span></td>
  		<td ><span class="agg-ClientRate"></span></td>
  		<td ><span  class="agg-TriId"></span></td>
  		<td ><span class="agg-ProtType"></span></td>
  		<td ><span class="agg-PathType"></span></td>
  		<td ><span class="agg-Color"></span></td>
  		<td ><span class="agg-NodeInclusion"></span></td>
  		<td><i class="fa fa-trash fa-2x text-danger cursor" aria-hidden="true" onClick="deleteRow(this)"></i></td>
  		</tr>`;
  	return template;
  }



  function ServiceTypeOTU2LowerClientDialog($this) {

  	console.log($this);
  	console.log($this.parent());
  	console.log($this.parent().parent());
  	console.log($this.parent().closest("td"));
  	console.log($this.parent().closest("tr"));
  	console.log($this.parent().closest("tr").find("td:eq(3)").text());



//  	console.log('td value ',  $this
//  	.parent().closest("tr"), 'and', $this
//  	.parent().closest("tr").find('td'));
//  	console.log(
//  	"ServiceTypeOtu2LowerClientDialog Row Index :: ",
//  	$this
//  	.parent()
//  	.parent()
//  	.index(),$this.parent().parent()
//  	);
  	let rowIndex = $this
  	.parent()
  	.parent()
  	.index();
  	let SrcNode = $this.parent().closest("tr").find("td:eq(3)").text();

  	let title = "Clients Info for OTU2_LOWER Service Type in POTP";
  	let template = fGetOTU2_lOWERServiceTypeTemplate(rowIndex);

  	bootbox
  	.dialog({
  		message: template,
  		title: title,
  		animation: true,
  		className: "cell-property-dialog",
  		backdrop: false,
  		buttons: {
  			"Add Client": {
  				// label: "Success!",
  				className: "btn btn--default aggregator-add-client1.25-btn",
  				callback: function() {
  					addClient1_25ToServiceTable($(this),SrcNode);
  					return false;
  				}
  			},


  			Save: {
  				// label: "Success!",
  				className: "btn btn--default",
  				callback: function() {
  					let circuitsHtml = $(this)
  					.find(".Service-type-table tbody")
  					.html();
  					console.log("for save");
  					// Validate 10g max capacity for one 10g mux card
  					/*let circuitsObject = $(this).find(".Service-type-table tbody tr");
  					let totalCapacity = 0;
  					for (let i = 0; i < circuitsObject.length; i++) {
  						let row = $(circuitsObject.get(i));
  						let cap = Number(row.find(".agg-ClientRate").html());

  						console.log("Cap:", cap);
  						totalCapacity += cap;
  					}


  					console.log("totalCapacity :", totalCapacity);
  					if (totalCapacity > 10) {
  						bootBoxDangerAlert("Max capacity can be 10G.");
  						return false;
  					}*/

  					saveOTU2_LowerServiceClients(rowIndex, circuitsHtml);
  					bootBoxSuccessAlert("Aggregator clients saved successfully.");
  					// return false;
  				}
  			},
  			Close: {
  				className: "btn btn--default",
  				callback: function() {}
  			}
  		}
  	})
  	.draggable();
  }


  function fRenderOTU2_LOWERServiceTypeCircuits(circuitsData) {
  	console.log("**************fRenderOTU2LowerCircuits******************");
  	_.each(circuitsData, (circuit, i) => {
  		if (circuit.POTPCircuits !== undefined) {

  			console.log("********** Has POTP circuits ***********", circuit.POTPCircuits);
  			let OTU2LCircuits = JSON.parse(circuit.POTPCircuits);
  			//console.log("in render",OTU2LCircuits);
  			// console.log("Agg circuits", aggCircuits);
  			let OTU2CircuitsTemplate = "";
  			_.each(OTU2LCircuits, OTU2Circuit => {
  				// console.log("aggCircuit", aggCircuit);

  				OTU2CircuitsTemplate += `<tr>${fGetColumnsOTU2CircuitFromDb(
  					OTU2Circuit.SrcNodeId,
  					OTU2Circuit.DestNodeId,
  					OTU2Circuit.TrafficType,
  					OTU2Circuit.RequiredTraffic,
  					OTU2Circuit.TributoryId,
  					OTU2Circuit.ProtectionType,
  					OTU2Circuit.PathType,
  					OTU2Circuit.ColourPreference,
  					OTU2Circuit.NodeInclusion
  					)} </tr>`;
  			});

  			// console.log(aggCircuitsTemplate);

  			saveOTU2_LowerServiceClients(i, OTU2CircuitsTemplate);
  			console.log("Save Clients for Row :", i);
  		} else {
  			$(
  					"#TrafficMatrixTable tbody > tr:eq(" + i + ") td .OTU2_Lower"
  			).empty();
  		}
  	});
  }



  function initializeOTU_LOWERClients(data,SrcNode) {
  	console.log("initialize OTU_LOWER Clients Data", data);

  	var NodeIdArray = [];
  	NodeIdArray[0] = "Select Node";
  	for(var i=0;i < nodeArray.length;i++)
  	{ 
  		NodeIdArray[i+1] = nodeArray[i].NodeId;

  	}



  	console.log("SrcNode",+SrcNode );
  	let circuitData = data;
  	if (circuitData !== null) {
  		console.log(circuitData.length);
  		_.each(circuitData, (tr, index) => {
  			let SrcNode = $(tr)
  			.find(".agg-SrcNode")
  			.text();
  			let DestNode = $(tr)
  			.find(".agg-DestNode")
  			.text();
  			let ServiceType = $(tr)
  			.find(".agg-ServiceType")
  			.text();
  			let ClientRate = $(tr)
  			.find(".agg-ClientRate")
  			.text();
  			let TributoryId = $(tr)
  			.find(".agg-TriId")
  			.text();
  			let ProtType = $(tr)
  			.find(".agg-ProtType")
  			.text();
  			let PathType = $(tr)
  			.find(".agg-PathType")
  			.text();
  			let Color = $(tr)
  			.find(".agg-Color")
  			.text();
  			let NodeInclusion = $(tr)
  			.find(".agg-NodeInclusion")
  			.text();


  			console.log(tr, " ------", index, clientrate);

  			$(tr)
  			.find(".agg-SrcNode")
  			.attr("title", "Edit ")
  			.tooltip()
  			.editable({
  				type: "text",
  				value: SrcNode,
  				source: SrcNode,
  				onblur: "submit",
  			});

  			$(tr)
  			.find(".agg-DestNode")
  			.attr("title", "Edit ")
  			.tooltip()
  			.editable({
  				type: "select",
  				value: DestNode,
  				source:NodeIdArray ,
  				onblur: "submit"
  			});
  			
  		
  	           $(tr)
  			.find(".agg-ServiceType")
  			.attr("title", "Edit ")
  			.tooltip()
  			.editable({
  				type: "select",
  				value: ServiceType,
  				source:ServiceTypeSrcForPotp ,
  				onblur: "submit"
  			});
  			$(tr)
  			.find(".agg-ClientRate")
  			.attr("title", "Edit ")
  			.tooltip()
  			.editable({
  				type: "select",
  				value: ClientRate,
  				source: ClientRateSrcForPotp,
  				onblur: "submit"
  			});
    
  			$(tr)
  			.find(".agg-TriId")
  			.attr("title", "Edit ")
  			.tooltip()
  			.editable({
  				type: "select",
  				value:  TributoryId,
  				source: TributoryIdsrc,
  				onblur: "submit",
  			});
  			

  			$(tr)
  			.find(".agg-ProtType")
  			.attr("title", "Edit ")
  			.tooltip()
  			.editable({
  				type: "text",
  				value: ProtType,
  				source: ptcTypeSrc,
  				onblur: "submit"
  			});


  			$(tr)
  			.find(".agg-PathType")
  			.attr("title", "Edit ")
  			.tooltip()
  			.editable({
  				type: "text",
  				value: PathType,
  				source: pathTypeSrc,	          
  				onblur: "submit"
  			});

  			$(tr).find(".agg-Color")
  			.attr("title", "Edit ")
  			.tooltip()
  			.editable({
  				type: "select",
  				value: Color,
  				source: colorSrc,
  				onblur: "submit"
  			});

  			$(tr).find(".agg-NodeInclusion")
  			.attr("title", "Edit ")
  			.tooltip()
  			.editable({
  				type: "text",
  				value: NodeInclusion,
  				onblur: "submit"

  			});




  		});
  	} else {




  		console.log("Node data"+nodeArray.NodeId);

  		let ProtType = "UnProtected";
  		let PathType = "disjoint";
  		let Color = "Empty";
  		let NodeInclusion = "None";



  		$(".Service-type-table  .agg-SrcNode")
  		.attr("title", "Edit ")
  		.editable({
  			type : "text",
  			value : SrcNode,
  			source: SrcNode
  		})
  		.editable("option", "disabled", true);



  		$(".Service-type-table .agg-DestNode")
  		.attr("title", "Edit ")
  		.tooltip()
  		.editable({
  			type: "select",
  			value: NodeIdArray[0],
  			source: NodeIdArray,
  			onblur: "submit",
  			validate: function(value) {
  				return fDestNodeValidate(value, $(this));	
  			}
  		});
  		
  		
  		
  		

  		$(".Service-type-table .agg-ClientRate")
  		.attr("title", "Edit ")
  		.tooltip()
  		.editable({
  			type: "select",
  			value: ClientRateSrcForPotp[0].value,
  			source: ClientRateSrcForPotp,
  			onblur: "submit",
  			validate: function(value) {
  				return fServiceTypeValidationforPOTP(value, $(this));	
  			}
  		});

  		$(".Service-type-table .agg-ServiceType")
  		.attr("title", "Edit ")
  		.tooltip()
  		.editable({
  			type: "select",
  			value: ServiceTypeSrcForPotp[0].value,
  			source: ServiceTypeSrcForPotp,
  			onblur: "submit"

  		});
  		

  		$(".Service-type-table  .agg-TriId")
  		.attr("title", "Edit ")
  		.tooltip()
  		.editable({
  			type : "select",
  			value : TributoryIdsrc[0].value,
  			source : TributoryIdsrc,
  			onblur : "submit"
  		});
  		
  		

  		$(".Service-type-table .agg-ProtType")
  		.attr("title", "Edit ")
  		.tooltip()
  		.editable({
  			type: "text",
  			value: nptGlobals.PtcTypeUnprotected,
  			source: ptcTypeSrc,
  			onblur: "submit"

  		});


  		$(".Service-type-table .agg-PathType")
  		.attr("title", "Edit ")
  		.tooltip()
  		.editable({
  			type: "select",
  			value: nptGlobals.DisjointPathStr,
  			source: pathTypeSrc,
  			onblur: "submit"

  		});


  		$(".Service-type-table .agg-Color")
  		.attr("title", "Edit ")
  		.tooltip()
  		.editable({
  			type: "select",
  			value: [],
  			source: colorSrc,
  			onblur: "submit"

  		});


  		$(".Service-type-table .agg-NodeInclusion")
  		.attr("title", "Edit ")
  		.tooltip()
  		.editable({
  			type: "select",
  			value: "Empty",
  			source: colorSrc,
  			onblur: "submit"

  		});


  	}
  }






