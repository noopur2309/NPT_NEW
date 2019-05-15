var dummyJson;

var waveLengthHeader = `<tr>
	<th>Sr. No.</th>
	<th>NodeId</th>
	<th>DemandId</th>
	<th>LineRate</th>
	<th>ProtectionType</th>
	<th>ClientProtectionType</th>		
	<th>CircuitId</th>
	<th>PortId</th>		
	<th>TrafficType</th>
	<th>Wavelength</th>
	<th>Direction</th>		
	<th>Protection  Status</th>
</tr>`;

var bomAllHeader = `<tr>
	<th></th>
	<th>Name</th>
	<th>Quantity</th>
	<th>Unit Price (INR)</th>
  <th>Total Price (INR)</th>
  <th>Unit Power (Watt)</th>
  <th>Total Power (Watt)</th>
  <th>Unit TypicalPower (Watt)</th>
	<th>Total Power (Watt)</th>
	<th>Part No.</th>
	<th>Revision Code</th>	
	</tr>`;
/*<th>Category</th>*/

var bomNodeHeader = `<tr>   
	<th></th> 
	<th>Name</th>
	<th>Quantity</th>
	<th>Unit Price (INR)</th>
  <th>Total Price (INR)</th>
  <th>Unit Power (Watt)</th>
  <th>Total Power (Watt)</th>
  <th>Unit TypicalPower (Watt)</th>
	<th>Total Power (Watt)</th>
	<th>Part No.</th>
	<th>Revision Code</th>	
	</tr>`;
/*<th>Category</th>*/

var InventoryHeader = `<tr>  
	<th></th>  
    <th>Node Id</th>
	<th>Rack</th>
	<th>Subrack</th>
	<th>CardId</th>
	<th>CardType</th>
	<th>Direction</th>
	<th>Wavelength</th>
	</tr>`;

var mpnInfoHeader = `<tr>
	<th>Node Id</th>
	<th>Demand</th>	
	<th>Physical Location</th>
	<th>Direction</th>
	<th>LineRate</th>
	<th>Utilized BW</th>
	<th>Remaining BW</th>	
	<th>Source</th>
	<th>Destination</th>
	<th>Wavelength</th>
	</tr>`;

var opticalPowerDropHeader = `<tr>
	<th></th>
	<th>Node Id</th>
	<th>Direction</th>
	<th>Num of Lambdas</th>		
	<th>PA Input</th>
	<th>PA Power Gain</th>
	<th>Wss Input</th>
	<th>Wss Attenuation</th>
	<th>Wss Power Loss</th>
	<th>Edary Input</th>
	<th>EDFA Power Gain</th>
	<th>MCS Input</th>
	<th>MCS Power Loss</th>
	<th>MPN Input</th>
	</tr>`;

var opticalPowerAddHeader = `<tr>
	<th></th>
	<th>Node Id</th>
	<th>Direction</th>
	<th>Num of Lambdas</th>		
	<th>MCS Input</th>
	<th>MCS Power Loss</th>
	<th>EDFA Input</th>
	<th>EDFA Power Gain</th>
	<th>WSS Input</th>
	<th>Wss Attenuation</th>
	<th>Wss Power Loss</th>
	<th>BA Input</th>
	<th>BA Power Gain</th>
	<th>BA Output</th>
	</tr>`;

/**
 * function to generate view Report file for current network node
 */
var currentJsonObj = [],
  isEquipmentData = 0;

$("#viewReportId").on("click", function() {
  //$('#bomModal').modal('show');
  console.log("viewReportModal Loaded");
  $("#viewReportModal").modal({ backdrop: "static", keyboard: true }); //disable click on black area/ esc key
  //equipmentDbAjaxCall();
  //finitializeBomView();

  viewReportDbAjaxCall();
});

/**
 * wavelength_configDbAjaxCall
 */
var viewReportDbAjaxCall = function() {
  let viewReportPostData = jsonPostObject();
  console.log(
    "viewReportPostData for " + viewReportPostData.NetworkInfo.NetworkName
  );

  //fShowClientServerCommunicationModal("Updating Equipment list of NPT. ");
  overlayOn("Fetching Data", ".body-overlay");

  serverPostMessage("getviewReportJsonDataRequest", viewReportPostData)
    .then(function(data) {
      dummyJson = data;
      //alert(dummyJson)
      console.log("success", data);
      // finitializeBomView1();
      fInitializeViewReport();
      //fUpdateEquipmentPreferences();
      overlayOff("Data Fetched Successfully ", ".body-overlay");
    })
    .catch(function(error) {
      console.log(error);
      overlayOff("Data Fetch Error ", ".body-overlay");
    });

  // $.ajax({

  // 	type: "POST",
  //     contentType: "application/json",

  //     headers: {
  //         Accept : "application/json; charset=utf-8",
  //        "Content-Type": "application/json; charset=utf-8"
  //     } ,

  //    url: "/getviewReportJsonDataRequest",

  //    data: JSON.stringify(viewReportPostData),
  //    dataType: 'json',

  //    timeout: 600000,

  //    success: function (data) {

  // 	   dummyJson=data;
  // 	   //alert(dummyJson)
  // 	   console.log(data);
  // 	   console.log("success");
  // 	  // finitializeBomView1();
  // 	   fInitializeViewReport();
  // 	   //fUpdateEquipmentPreferences();
  //    },

  //    error: function (e) {
  // 	   console.log("failed here");
  // 	   /*dummyJson=dummyJson;
  // 	   finitializeBomView1();
  // 	   fInitializeViewReport();*/
  // 	   console.log(e);  }
  // 	});
};

/***************************************************************************/
/**         Initialize View Report Modal                         **/
/***************************************************************************/
var fInitializeViewReport = function() {
  //console.log("Data ");
  console.log("Inside finitializeviewReport View");
  fInitializeNodeIdInput();
  var selectedNodeId = $("#viewReportNodeId")
    .find(":selected")
    .text();
  console.log("Selected Node Id:::", selectedNodeId);

  fGenerateBomDataForNode(selectedNodeId);

  fCreateWavelengthTabView(true, selectedNodeId);
  fCreateMpnInfoTabView(true, selectedNodeId);
  fCreateInventoryTabView(true, selectedNodeId);
  fCreateOpticalPowerTabView(true, selectedNodeId);

  fDisableSavepreferenceBtn(true); //disable save preference button

  /**
   * for Node IP
   */

  var nodeListArr1 = dummyJson.IpSchemeNodeData;
  //console.log("wavelengthArr")
  //console.log(wavelengthArr)

  $(".modalBodyviewReportContentPanelviewNodeReportTable tbody").empty();
  var str = "";
  for (var i = 0; i < nodeListArr1.length; i++) {
    str += `<tr>
 		<td>${nodeListArr1[i].NetworkId}</td>
 		<td>${nodeListArr1[i].NodeId}</td>
 		<td>${nodeListArr1[i].LctIp}</td>
 		<td>${nodeListArr1[i].RouterIp}</td>
 		<td>${nodeListArr1[i].ScpIp}</td>
 		<td>${nodeListArr1[i].McpIp}</td>
 		</tr>`;
    console.log(str);
  }
  $(".modalBodyviewReportContentPanelviewNodeReportTable tbody").append(str);
  $(".modalBodyviewReportContentPanelviewNodeReportTable").addClass(
    "table-striped"
  );
  //fDisableSavepreferenceBtn(true);//disable save preference button

  /**
   * for Link IP
   */
  var LinkListArr1 = dummyJson.IpSchemeLinkData;

  $(".modalBodyviewReportContentPanelviewLinkReportTable tbody").empty();
  var str = "";
  for (var i = 0; i < LinkListArr1.length; i++) {
    str += `<tr>
 		<td>${LinkListArr1[i].NetworkId}</td>
 		<td>${LinkListArr1[i].LinkId}</td>
 		<td>${LinkListArr1[i].LinkIp}</td>
 		<td>${LinkListArr1[i].SrcIp}</td>
 		<td>${LinkListArr1[i].DestIp}</td>
 		</tr>`;
    //console.log(str);
  }
  $(".modalBodyviewReportContentPanelviewLinkReportTable tbody").append(str);
  $(".modalBodyviewReportContentPanelviewLinkReportTable").addClass(
    "table-striped"
  );
  //fDisableSavepreferenceBtn(true);//disable save preference button
};

/***************************************************************************/
/**         Initialize View Report Modal                         **/
/***************************************************************************/
var fInitializeNodeIdInput = function() {
  var nodejsonArrayViewReport = dummyJson.NodeData;
  $("#viewReportNodeId").empty();

  for (var i = 0; i < nodejsonArrayViewReport.length; i++)
    $("#viewReportNodeId").append(
      `<option value="${nodejsonArrayViewReport[i]}">${
        nodejsonArrayViewReport[i]
      }</option>`
    );
  $("#viewReportNodeId").append(`<option value="All">All</option>`);
};

/***************************************************************************/
/**         Client Config Window Initialization                         **/
/***************************************************************************/

function fCreateWavelengthTabView(generateMode, nodeId) {
  var wavelengthArr;
  $(".modalBodyviewReportContentPanelwavelengthConfiTable thead")
    .empty()
    .append(waveLengthHeader);
  if (generateMode == true) {
    wavelengthArr = fGetNodeWiseDataViewReport(
      dummyJson.wavelengthConfiguration,
      nodeId
    );
  } else {
    wavelengthArr = dummyJson.wavelengthConfiguration;
  }
  if (wavelengthArr != 0) {
    //console.log("wavelengthArr")
    //console.log(wavelengthArr)
    var temp = 0,
      rowSpanValue = 0;
    $(".modalBodyviewReportContentPanelwavelengthConfiTable tbody").empty();
    var str = "";

    var demandIndex = 0;
    var className = "bomRowBkgColor1";
    for (var i = 0; i < wavelengthArr.length; i++) {
      var circuitIndex = 0;
      var demandId = wavelengthArr[i].DemandId;
      var nodeId = wavelengthArr[i].NodeId;
      //console.log("Demand id value :"+ demandId);
      temp = 0;
      var count = i + 1;
      while (count < wavelengthArr.length) {
        //console.log("count "+ count + " and "+ wavelengthArr.length);
        if (
          wavelengthArr[count].DemandId == demandId &&
          wavelengthArr[count].NodeId == nodeId
        ) {
          //alert("inside if ");
          count++;
          temp++;
          console.log("after if " + count + " and " + temp);
        } else break;
      }
      //console.log("Value of temp :"+temp);
      rowSpanValue = temp + 1;
      //console.log("rowSpanValue : "+ rowSpanValue);

      circuitIndex++;
      demandIndex++;

      if (wavelengthArr[i].Direction == "") wavelengthArr[i].Direction = "-";

      str += `<tr class=${className}>		
				<td rowspan='${rowSpanValue}' class='commonRow demandId'>${demandIndex}</td>
				<td rowspan='${rowSpanValue}' class='commonRow demandId'>${
        wavelengthArr[i].NodeId
      }</td>
				<td rowspan='${rowSpanValue}' class='commonRow demandId'>${
        wavelengthArr[i].DemandId
      }</td>
				<td rowspan='${rowSpanValue}' class='commonRow demandId'>${
        wavelengthArr[i].LineRate
      }</td>
				<td rowspan='${rowSpanValue}' class='commonRow demandId'>${
        wavelengthArr[i].ProtectionType
      }</td>
				<td rowspan='${rowSpanValue}' class='commonRow demandId'>${
        wavelengthArr[i].ClientProtectionType
      }</td>
				<td>${wavelengthArr[i].CircuitId}</td>
				<td>${wavelengthArr[i].PortId}</td>
				<td>${wavelengthArr[i].TrafficType}</td>
				<td>${wavelengthArr[i].Wavelength}</td>		
				<td>${wavelengthArr[i].Direction}</td>
				<td>${wavelengthArr[i].ProtectionStatus}</td>
				</tr>`;

      while (temp) {
        i++;
        circuitIndex++;
        str += `<tr class=${className}>
						<td>${wavelengthArr[i].CircuitId}</td>
						<td>${wavelengthArr[i].PortId}</td>
						<td>${wavelengthArr[i].TrafficType}</td>
						<td>${wavelengthArr[i].Wavelength}</td>		
						<td>${wavelengthArr[i].Direction}</td>
						<td>${wavelengthArr[i].ProtectionStatus}</td>
						</tr>`;

        temp--;
      }

      if (className == "bomRowBkgColor1") className = "bomRowBkgColor2";
      else className = "bomRowBkgColor1";

      circuitIndex = 0;
    }
  } else {
    str = `<p class="text-center">No matched data found for given Node Id</p>`;
  }

  //$(".modalBodyviewReportContentPanelwavelengthConfiTable").addClass('table-striped');
  $(".modalBodyviewReportContentPanelwavelengthConfiTable tbody")
    .empty()
    .append(str);
}

/***************************************************************************/
/**         MPN Config View                         **/
/***************************************************************************/
function fCreateMpnInfoTabView(generateMode, nodeId) {
  var mpnInfoArr;
  $(".modalBodyviewReportContentPanelMPNConfiTable thead")
    .empty()
    .append(mpnInfoHeader);

  if (generateMode == true) {
    mpnInfoArr = fGetNodeWiseDataViewReport(dummyJson.MPNInformation, nodeId);
  } else {
    mpnInfoArr = dummyJson.MPNInformation;
  }

  if (mpnInfoArr != 0) {
    var temp = 0,
      rowSpanValue = 0;
    $(".modalBodyviewReportContentPanelMPNConfiTable tbody").empty();
    var str = "";

    var demandIndex = 0;
    var className = "bomRowBkgColor1";

    for (var i = 0; i < mpnInfoArr.length; i++) {
      /**don't display pass-through location*/
      if (mpnInfoArr[i].PhysicalLocation == "Pass-through") continue;

      str += `<tr class=${className}>
					<td>${mpnInfoArr[i].NodeId}</td>
					<td>${mpnInfoArr[i].DemandId}</td>
					<td>${mpnInfoArr[i].PhysicalLocation}</td>
					<td>${mpnInfoArr[i].Direction}</td>					
					<td>${mpnInfoArr[i].LineRate}</td>
					<td>${mpnInfoArr[i].Traffic}</td>		
					<td>${mpnInfoArr[i].LineRate - mpnInfoArr[i].Traffic}</td>					
					<td>${mpnInfoArr[i].SrcNode}</td>
					<td>${mpnInfoArr[i].DestNode}</td>
					<td>${mpnInfoArr[i].Wavelength}</td>
					</tr>`;

      if (className == "bomRowBkgColor1") className = "bomRowBkgColor2";
      else className = "bomRowBkgColor1";
    }
  } else {
    str = `<p class="text-center">No matched data found for given Node Id</p>`;
  }

  //$(".modalBodyviewReportContentPanelwavelengthConfiTable").addClass('table-striped');
  $(".modalBodyviewReportContentPanelMPNConfiTable tbody")
    .empty()
    .append(str);
}

/***************************************************************************/
/**         Inventory View                     						    **/
/***************************************************************************/
function fCreateInventoryTabView(generateMode, nodeId) {
  console.log("Filling data in Inventory table :: fCreateInventoryTabView");
  var InventoryInfoArr, state, label;
  $(".modalBodyviewReportContentPanelInventoryTable thead")
    .empty()
    .append(InventoryHeader);

  if (generateMode == true) {
    InventoryInfoArr = fGetNodeWiseDataViewReport(
      dummyJson.InventoryData,
      nodeId
    );
  } else {
    InventoryInfoArr = dummyJson.InventoryData;
  }

  if (InventoryInfoArr != 0) {
    var className = "bomRowBkgColor1",
      str = "";

    for (var i = 0; i < InventoryInfoArr.length; i++) {
      state = InventoryInfoArr[i].State + "RowClass";
      label = getLabelTagsFromState(state);
      str += `<tr class="${className} ${state}">
					<td>${label}</td>
				    <td>${InventoryInfoArr[i].NodeId}</td>
					<td>${InventoryInfoArr[i].Rack}</td>
					<td>${InventoryInfoArr[i].SubRack}</td>
					<td>${InventoryInfoArr[i].CardId}</td>
					<td>${InventoryInfoArr[i].CardType}</td>		
					<td>${InventoryInfoArr[i].Direction}</td>
					<td>${InventoryInfoArr[i].Wavelength}</td>
					</tr>`;

      if (className == "bomRowBkgColor1") className = "bomRowBkgColor2";
      else className = "bomRowBkgColor1";
    }
  } else {
    str = `<p class="text-center">No matched data found for given Node Id</p>`;
  }

  //$(".modalBodyviewReportContentPanelwavelengthConfiTable").addClass('table-striped');
  $(".modalBodyviewReportContentPanelInventoryTable tbody")
    .empty()
    .append(str);
}

/***************************************************************************/
/**         Optical Power View                         					**/
/***************************************************************************/
function fCreateOpticalPowerTabView(generateMode, nodeId) {
  console.log("Filling data in Optical table :: fCreateOpticalPowerTabView");
  var OpticalPowAddInfoArr, OpticalPowDropInfoArr, state, label;

  if (generateMode == true) {
    OpticalPowAddInfoArr = fGetNodeWiseDataViewReport(
      dummyJson.OpticalPowerAddData,
      nodeId
    );
    OpticalPowDropInfoArr = fGetNodeWiseDataViewReport(
      dummyJson.OpticalPowerDropData,
      nodeId
    );
  } else {
    OpticalPowAddInfoArr = dummyJson.OpticalPowerAddData;
    OpticalPowDropInfoArr = dummyJson.OpticalPowerDropData;
  }

  if (OpticalPowAddInfoArr != 0) {
    var className = "bomRowBkgColor1",
      str = "";
    $(".modalBodyviewReportContentPanelOpticalPowerAddTable thead")
      .empty()
      .append(opticalPowerAddHeader);

    for (var i = 0; i < OpticalPowAddInfoArr.length; i++) {
      state = OpticalPowAddInfoArr[i].State + "RowClass";
      label = getLabelFromState(state);

      mcsPowerLoss = -(
        OpticalPowAddInfoArr[i].EdfaIn - OpticalPowAddInfoArr[i].McsIn
      ).toFixed(2);
      edfaPowerGain = (
        OpticalPowAddInfoArr[i].WssIn - OpticalPowAddInfoArr[i].EdfaIn
      ).toFixed(2);
      wssPowerLoss = -(
        OpticalPowAddInfoArr[i].BaIn -
        OpticalPowAddInfoArr[i].WssAtten -
        OpticalPowAddInfoArr[i].WssIn
      ).toFixed(2);
      baPowerGain = (
        OpticalPowAddInfoArr[i].BaOut - OpticalPowAddInfoArr[i].BaIn
      ).toFixed(2);
      wssAtten = -OpticalPowAddInfoArr[i].WssAtten.toFixed(2);
      if (OpticalPowAddInfoArr[i].NumOfLambda != 0) {
        str += `<tr class="${className} ${state}">
						<td>${label}</td>
						<td>${OpticalPowAddInfoArr[i].NodeId}</td>
						<td>${OpticalPowAddInfoArr[i].Direction}</td>
						<td>${OpticalPowAddInfoArr[i].NumOfLambda}</td>
						<td>${OpticalPowAddInfoArr[i].McsIn}</td>
						<td>${mcsPowerLoss}</td>
						<td>${OpticalPowAddInfoArr[i].EdfaIn}</td>
						<td>${edfaPowerGain}</td>
						<td>${OpticalPowAddInfoArr[i].WssIn}</td>
						<td>${wssAtten}</td>
						<td>${wssPowerLoss}</td>		
						<td>${OpticalPowAddInfoArr[i].BaIn}</td>
						<td>${baPowerGain}</td>
						<td>${OpticalPowAddInfoArr[i].BaOut}</td>		
						</tr>`;
      }
      if (className == "bomRowBkgColor1") className = "bomRowBkgColor2";
      else className = "bomRowBkgColor1";
    }
    $(".modalBodyviewReportContentPanelOpticalPowerAddTable tbody")
      .empty()
      .append(str);
  } else {
    str = `<p class="text-center">No matched data found for given Node Id</p>`;
    $(".modalBodyviewReportContentPanelOpticalPowerAddTable tbody")
      .empty()
      .append(str);
  }

  if (OpticalPowDropInfoArr != 0) {
    var className = "bomRowBkgColor1",
      str = "";
    $(".modalBodyviewReportContentPanelOpticalPowerDropTable thead")
      .empty()
      .append(opticalPowerDropHeader);
    for (var i = 0; i < OpticalPowDropInfoArr.length; i++) {
      state = OpticalPowDropInfoArr[i].State + "RowClass";
      label = getLabelFromState(state);

      paPowerGain = (
        OpticalPowDropInfoArr[i].WssIn - OpticalPowDropInfoArr[i].PaIn
      ).toFixed(2);
      wssPowerLoss = -(
        OpticalPowDropInfoArr[i].DropEdfaIn -
        OpticalPowDropInfoArr[i].RxWssAttenuation -
        OpticalPowDropInfoArr[i].WssIn
      ).toFixed(2);
      edfaPowerGain = (
        OpticalPowDropInfoArr[i].McsIn - OpticalPowDropInfoArr[i].DropEdfaIn
      ).toFixed(2);
      mcsPowerLoss = -(
        OpticalPowDropInfoArr[i].MpnIn - OpticalPowDropInfoArr[i].McsIn
      ).toFixed(2);
      if (OpticalPowDropInfoArr[i].NumOfLambda != 0) {
        str += `<tr class="${className} ${state}">
						<td>${label}</td>
						<td>${OpticalPowAddInfoArr[i].NodeId}</td>
						<td>${OpticalPowDropInfoArr[i].Direction}</td>
						<td>${OpticalPowDropInfoArr[i].NumOfLambda}</td>
						<td>${OpticalPowDropInfoArr[i].PaIn}</td>
						<td>${paPowerGain}</td>
						<td>${OpticalPowDropInfoArr[i].WssIn}</td>
						<td>${OpticalPowDropInfoArr[i].RxWssAttenuation}</td>
						<td>${wssPowerLoss}</td>
						<td>${OpticalPowDropInfoArr[i].DropEdfaIn}</td>
						<td>${edfaPowerGain}</td>
						<td>${OpticalPowDropInfoArr[i].McsIn}</td>		
						<td>${mcsPowerLoss}</td>
						<td>${OpticalPowDropInfoArr[i].MpnIn}</td>

						</tr>`;
      }
      if (className == "bomRowBkgColor1") className = "bomRowBkgColor2";
      else className = "bomRowBkgColor1";
    }
    $(".modalBodyviewReportContentPanelOpticalPowerDropTable tbody")
      .empty()
      .append(str);
  } else {
    str = `<p class="text-center">No matched data found for given Node Id</p>`;
    $(".modalBodyviewReportContentPanelOpticalPowerDropTable tbody")
      .empty()
      .append(str);
  }
  $("#modalBodyviewReportContentPanelOpticalPowerAddBtn").click();
}

/***************************************************************************/
/**         Node Id change trigger for bom data                           **/
/***************************************************************************/
$("#viewReportNodeId").on("change", function() {
  /* var ref_this = $("#modalBodyviewReportTabMenu").find(".activeFocus");
     alert(ref_this.data("id"));*/

  var NodeId = $(this).val(); //fGenerateBomDataForCompleteNetwork
  ///alert("NodeId "+ NodeId);

  if (NodeId == "All") {
    fGenerateBomDataForCompleteNetwork();
    /**Wavelength Configuration data for all the node*/
    fCreateWavelengthTabView(false, 0);

    /**MPN data for all the node*/
    fCreateMpnInfoTabView(false, 0);

    /**Inventory data for all the node*/
    fCreateInventoryTabView(false, 0);

    /**Optical data for all the node*/
    fCreateOpticalPowerTabView(false, NodeId);
  } else {
    fGenerateBomDataForNode(NodeId);
    /**Wavelength Configuration Data for a specific node*/
    fCreateWavelengthTabView(true, NodeId);

    /**MPN data for all the node*/
    fCreateMpnInfoTabView(true, NodeId);

    /**Inventory data for all the node*/
    fCreateInventoryTabView(true, NodeId);

    /**Optical data for all the node*/
    fCreateOpticalPowerTabView(true, NodeId);
  }

  fNodeChangeTabRefresh();

  console.log("NodeId ::" + NodeId);
});

/***************************************************************************/
/**         Tab menu trigger for view Report view                         **/
/***************************************************************************/
// Tab menu trigger for BOM data
$("#modalBodyviewReportTabMenuBomTrigger").on("click", function() {
  /**make select option selectable*/
  $("#viewReportNodeId").attr("disabled", false);

  fSlideUpAllTabs();
  $("#modalBodyviewReportContentPanelBom").slideDown();
  fDisableSavepreferenceBtn(true);

  // console.log($(this));
  //	fActiveClassToggleForViewReportTabMenu($(this));

  isEquipmentData = 0;

  $("#viewReportViewMainHeading").html("Bill Of Material (BOM)");
  loadExcelJsonEventFunction();
});

// Tab menu trigger for WavelengthConfiguration Data
$("#modalBodyviewReportTabMenuwavelengthConfiTrigger").on("click", function() {
  /**make select option selectable*/
  $("#viewReportNodeId").attr("disabled", false);

  fSlideUpAllTabs();
  $("#modalBodyviewReportContentPanelwavelengthConfi").slideDown();
  $("#viewReportViewMainHeading").html("Client Configuration");

  //console.log($(this));
  //    fActiveClassToggleForViewReportTabMenu($(this));

  fDisableSavepreferenceBtn(true);

  isEquipmentData = 1;
  loadExcelJsonEventFunction();
});

/**
 * This Tab shows the configuration for MPNs
 * like Per Node MPN and its Physical Location,
 * Total used & Remaining BW, Demand, Source and Destination
 */
$("#modalBodyviewReportTabMenuMPNInfoConfiTrigger").on("click", function() {
  console.log("Inside Bandwidth Analysis");

  /**make select option selectable*/
  $("#viewReportNodeId").attr("disabled", false);

  /**Slide Up, Down & Set Title */
  fSlideUpAllTabs();
  $("#modalBodyviewReportContentPanelMPNConfi").slideDown();
  $("#viewReportViewMainHeading").html("Client  Information");

  /**Fill In the Information*/
  //	fActiveClassToggleForViewReportTabMenu($(this));
  isEquipmentData = 1;
  loadExcelJsonEventFunction();
});

//Tab menu trigger for IPGenerationNode Data
$("#modalBodyviewReportTabMenugenerateNodeIPTrigger").on("click", function() {
  /**make select option unselectable*/
  $("#viewReportNodeId").attr("disabled", true);

  fSlideUpAllTabs();
  $("#modalBodyviewReportContentPanelviewNodeReport").slideDown();
  $("#viewReportViewMainHeading").html("Node IPScheme");

  //console.log($(this));
  //	fActiveClassToggleForViewReportTabMenu($(this));

  fDisableSavepreferenceBtn(true);

  currentJsonObj = dummyJson.IpSchemeNodeData;
  isEquipmentData = 1;
  loadExcelJsonEventFunction();
});

//Tab menu trigger for IPGenerationLink Data
$("#modalBodyviewReportTabMenugenerateLinkIPTrigger").on("click", function() {
  /**make select option unselectable*/
  $("#viewReportNodeId").attr("disabled", true);

  fSlideUpAllTabs();
  $("#modalBodyviewReportContentPanelviewLinkReport").slideDown();
  $("#viewReportViewMainHeading").html("Link IPScheme");

  //console.log($(this));
  //	fActiveClassToggleForViewReportTabMenu($(this));

  fDisableSavepreferenceBtn(true);

  currentJsonObj = dummyJson.IpSchemeLinkData;
  isEquipmentData = 1;
  loadExcelJsonEventFunction();
});

//Tab menu trigger for CablingDiagram Data
$("#modalBodyviewReportTabMenucablingDiagramTrigger").on("click", function() {
  /**make select option selectable*/
  $("#viewReportNodeId").attr("disabled", false);

  fSlideUpAllTabs();
  $("#modalBodyviewReportContentPanelCablingDiagram").slideDown();
  $("#viewReportViewMainHeading").html("Cabling Diagram");

  //console.log($(this));
  //	fActiveClassToggleForViewReportTabMenu($(this));

  fDisableSavepreferenceBtn(true);

  isEquipmentData = 1;
  loadExcelJsonEventFunction();
});

//Tab menu trigger for Inventory Data
$("#modalBodyviewReportTabMenuInventoryTrigger").on("click", function() {
  /**make select option selectable*/
  $("#viewReportNodeId").attr("disabled", false);

  fSlideUpAllTabs();
  $("#modalBodyviewReportContentPanelInventory").slideDown();
  $("#viewReportViewMainHeading").html("Inventory");

  //console.log($(this));
  //	fActiveClassToggleForViewReportTabMenu($(this));

  fDisableSavepreferenceBtn(true);
  loadExcelJsonEventFunction();
});

//Tab menu trigger for Optical Data
$("#modalBodyviewReportTabMenuOpticalPowerTrigger").on("click", function() {
  /**make select option selectable*/
  $("#viewReportNodeId").attr("disabled", false);

  fSlideUpAllTabs();
  $("#modalBodyviewReportContentPanelOpticalPower").slideDown();
  $("#viewReportViewMainHeading").html("Optical Power");

  //console.log($(this));
  //	fActiveClassToggleForViewReportTabMenu($(this));

  fDisableSavepreferenceBtn(true);
});

var fActiveClassToggleForViewReportTabMenu = function($el) {
  //console.log($el.siblings())
  $el.siblings().each(function() {
    $(this).removeClass("activeFocus");
  });
  $el.addClass("activeFocus");
};

var fNodeChangeTabRefresh = function() {
  var viewReportModalBodyTabs = $(
    "#viewReportModal .genericModalBodyContentPanel"
  ).children();
  console.log("viewReportModalBodyTabs::", viewReportModalBodyTabs);
  viewReportModalBodyTabs.each(function() {
    if ($(this).is(":visible")) {
      //console.log("slide up and down "+$(this).children())
      $(this)
        .slideUp()
        .slideDown();
    }
    //		else
    //		{
    //			console.log("other tabs ")
    //			$(this).slideDown().slideUp();
    //		}
  });
};

var fSlideUpAllTabs = function(id) {
  var viewReportModalBodyTabs = $(
    "#viewReportModal .genericModalBodyContentPanel"
  ).children();
  console.log("viewReportModalBodyTabs::", viewReportModalBodyTabs);
  viewReportModalBodyTabs.each(function() {
    if ($(this).is(":visible")) {
      $(this).slideUp();
    }
    //		else
    //		{
    //			$(this).slideDown().slideUp();
    //		}
  });
};

/**
/***************************************************************************/
/**         Enable/Disable the save preference button                    **/
/***************************************************************************/
var fDisableSavepreferenceBtn = function(val) {
  $("#savePreferencesviewReport").prop("disabled", val);
  $("#ViewReportSaveAsXlsBtn").prop("disabled", !val);
  $("#ViewReportSaveAsPdfBtn").prop("disabled", !val);
};

/***************************************************************************/
/**         get bom data for a ( NodeId == 1,2,3 etc ) option             **/
/***************************************************************************/
var fGetNodeWiseDataViewReport = function(DataArray, NodeId) {
  var NodeWiseDataArr = [];
  //alert("fGetNodeWiseDataViewReport function called")
  //console.log("DataArray"+DataArray);
  for (var j = 0; j < DataArray.length; j++) {
    if (DataArray[j].NodeId == NodeId) NodeWiseDataArr.push(DataArray[j]);
  }
  //console.log("NodeWiseDataArr"+NodeWiseDataArr);
  return NodeWiseDataArr;
};

/***************************************************************************/
/**         Generate bom data for a given NodeId                          **/
/***************************************************************************/
var fGenerateBomDataForNode = function(NodeId) {
  //var NodeDataForBomArr=dummyJson.bomViewData[NodeId];
  var NodeDataForBomArrComplete = dummyJson.bomViewData.NodeWiseData,
    TotalCost = 0,
    TotalPower = 0,
    TotalTypicalPower = 0,
    state,
    label,
    nodeInfoObj = fGetNodeInfo(NodeDataForBomArrComplete, NodeId);

  console.log("NodeInfoobj::", nodeInfoObj);
  var StationName = nodeInfoObj["StationName"],
    SiteName = nodeInfoObj["SiteName"];

  if (NodeDataForBomArrComplete.length == 0) console.log("Bom Data Empty");

  var NodeDataForBomArr = [];
  NodeDataForBomArr = fGetNodeWiseDataViewReport(
    NodeDataForBomArrComplete,
    NodeId
  );

  //console.log("NodeDataForBomArr",NodeDataForBomArr);

  isEquipmentData = 0;

  $(".modalBodyviewReportContentPanelBomTable thead")
    .empty()
    .append(bomNodeHeader);
  $(".modalBodyviewReportContentPanelBomTable tbody").empty();
  var str = "";
  for (var i = 0; i < NodeDataForBomArr.length; i++) {
    //console.log("NodeDataForBomArr ",i+" Value::",NodeDataForBomArr[i].State);
    state = NodeDataForBomArr[i].State + "RowClass";
    label = getLabelTagsFromState(state);
    str += `<tr>
			<td>${label}</td>
			<td>${NodeDataForBomArr[i].Name}</td>
			<td class="${state}">${NodeDataForBomArr[i].Quantity}</td>
			<td class="${state}">${fFormatRupees(NodeDataForBomArr[i].Price)}</td>
      <td class="${state}">${fFormatRupees(
      NodeDataForBomArr[i].TotalPrice
    )}</td>
      <td class="${state}">${NodeDataForBomArr[i].UnitPower}</td>
      <td class="${state}">${NodeDataForBomArr[i].TotalPowerConsumption}</td>
      <td class="${state}">${NodeDataForBomArr[i].UnitTypicalPower}</td>
      <td class="${state}">${
      NodeDataForBomArr[i].TotalTypicalPowerConsumption
    }</td>      
			<td>${NodeDataForBomArr[i].PartNo}</td>
			<td>${NodeDataForBomArr[i].RevisionCode}</td>			
			</tr>`;

    /*<td>${NodeDataForBomArr[i].Category}</td>*/

    TotalCost = TotalCost + Number(NodeDataForBomArr[i].TotalPrice);
    TotalPower =
      TotalPower + Number(NodeDataForBomArr[i].TotalPowerConsumption);
    TotalTypicalPower =
      TotalTypicalPower +
      Number(NodeDataForBomArr[i].TotalTypicalPowerConsumption);
  }
  //console.log("value of i at last : "+i + "\n str value :"+str);

  $("#viewReportSiteName")
    .parent()
    .parent()
    .slideUp()
    .remove();
  $("#viewReportStationName")
    .parent()
    .parent()
    .slideUp()
    .remove();

  $(".modalBodyviewReportContentPanelBomTable").addClass("table-striped");
  $(".modalBodyviewReportContentPanelBomTable tbody").append(str);

  $("#viewReportNetworkName").html(sessionStorage.getItem("NetworkName"));

  var template = `<div class="label-card">
		<h4 class="label-card-title">Site Name</h4>
		<p class="label-card-text">
		<span id="viewReportSiteName">${SiteName.toUpperCase()}</span>
		</p>
		</div>

		<div class="label-card">
		<h4 class="label-card-title">Station Name</h4>
		<p class="label-card-text">
		<span id="viewReportStationName">${StationName.toUpperCase()}</span>
		</p>
		</div>`;
  $(".node-info-div-view-report").prepend(template);

  $("#viewReportTotalCost").html(fFormatRupees(TotalCost) + " INR");
  sessionStorage.setItem("TotalCost", TotalCost);
  $("#viewReportPowerCons").html(TotalPower);
  sessionStorage.setItem("TotalPower", TotalPower);
  $("#viewReportPowerConsTypical").html(TotalTypicalPower);
  sessionStorage.setItem("TypicalPower", TotalTypicalPower);

  console.log("NodeId:::", NodeId);
};

/***************************************************************************/
/**         Generate bom data for a given NodeId                          **/
/***************************************************************************/
var fGetNodeInfo = function(DataArray, NodeId) {
  var ResultObj = new Object();
  ResultObj.StationName = "";
  ResultObj.SiteName = "";
  for (var j = 0; j < DataArray.length; j++) {
    if (DataArray[j].NodeId == NodeId) {
      ResultObj.StationName = DataArray[j].StationName;
      ResultObj.SiteName = DataArray[j].SiteName;
      return ResultObj;
    }
  }
  return ResultObj;
};

/***************************************************************************/
/**         Generate bom data for a ( NodeId == All ) option              **/
/***************************************************************************/
var fGenerateBomDataForCompleteNetwork = function() {
  //var NodeDataForBomArr=dummyJson.bomViewData[NodeId];
  var NodeDataForBomArrComplete = dummyJson.bomViewData.NetworkWiseData,
    bomArrLength = NodeDataForBomArrComplete.length,
    TotalCost = 0,
    TotalPower = 0,
    TypicalPower = 0,
    state,
    label,
    currNodeId;
  //console.log("NodeDataForBomArrComplete")
  //console.log(NodeDataForBomArrComplete)

  isEquipmentData = 0;

  $(".modalBodyviewReportContentPanelBomTable thead")
    .empty()
    .append(bomAllHeader);
  $(".modalBodyviewReportContentPanelBomTable tbody").empty();
  var str = "";
  var className = "bomRowBkgColor1";
  for (var i = 0; i < bomArrLength; i++) {
    //console.log("value of i at strating ::"+i)
    currNodeId = NodeDataForBomArrComplete[i].NodeId;
    state = NodeDataForBomArrComplete[i].State + "RowClass";
    label = getLabelTagsFromState(state);
    //		//var j=i+1;
    //		while(i<bomArrLength && currNodeId==NodeDataForBomArrComplete[i].NodeId)
    //		{
    str += `<tr >
			<td>${label}</td>
			<td>${NodeDataForBomArrComplete[i].Name}</td>
			<td class="${state}">${NodeDataForBomArrComplete[i].Quantity}</td>
			<td class="${state}">${fFormatRupees(NodeDataForBomArrComplete[i].Price)}</td>
			<td class="${state}">${fFormatRupees(
      NodeDataForBomArrComplete[i].TotalPrice
    )}</td>
    <td class="${state}">${NodeDataForBomArrComplete[i].UnitPower}</td>
    <td class="${state}">${
      NodeDataForBomArrComplete[i].TotalPowerConsumption
    }</td>
    <td class="${state}">${NodeDataForBomArrComplete[i].UnitTypicalPower}</td>
    <td class="${state}">${
      NodeDataForBomArrComplete[i].TotalTypicalPowerConsumption
    }</td>          
			<td>${NodeDataForBomArrComplete[i].PartNo}</td>
			<td>${NodeDataForBomArrComplete[i].RevisionCode}</td>			
			</tr>`;

    /*<td>${NodeDataForBomArrComplete[i].Category}</td>*/
    TotalCost = TotalCost + Number(NodeDataForBomArrComplete[i].TotalPrice);
    TotalPower =
      TotalPower + Number(NodeDataForBomArrComplete[i].TotalPowerConsumption);
    TypicalPower =
      TypicalPower +
      Number(NodeDataForBomArrComplete[i].TotalTypicalPowerConsumption);
    //		i++;
    //		}
    //		i--;
    // if (className == "bomRowBkgColor1") className = "bomRowBkgColor2";
    // else className = "bomRowBkgColor1";
    //console.log("value of i at last : "+i + "\n str value :"+str);
  }

  $(".modalBodyviewReportContentPanelBomTable").removeClass("table-striped");
  $(".modalBodyviewReportContentPanelBomTable tbody").append(str);

  $("#viewReportNetworkName").html(sessionStorage.getItem("NetworkName"));
  $("#viewReportSiteName")
    .parent()
    .parent()
    .slideUp()
    .remove();
  $("#viewReportStationName")
    .parent()
    .parent()
    .slideUp()
    .remove();
  $("#viewReportTotalCost").html(fFormatRupees(TotalCost));
  sessionStorage.setItem("TotalCost", TotalCost);
  $("#viewReportPowerCons").html(TotalPower);
  sessionStorage.setItem("TotalPower", TotalPower);
  $("#viewReportPowerConsTypical").html(TypicalPower);
  sessionStorage.setItem("TypicalPower", TypicalPower);
  console.log(
    "value of bomPowerCons ::" +
      TotalPower +
      "value of totalCost :: " +
      TotalCost
  );

  $(".modalBodyviewReportContentPanelBomTable").tablesorter();
};

// $("#downloadPdfviewReportViewData").click(function(){
// 	var bomData=dummyJson.bomViewData;
// 	console.log(bomData);
// 	//printJS({printable: bomData, properties: ['NodeId', 'Name', 'Quantity','Price','TotalPrice','PartNo','Revision Code','Category'], type: 'json'});});
// })

$("#viewReportModal .modal-dialog").resizable({
  //alsoResize: ".modal-dialog",
  minHeight: 300,
  minWidth: 300
});
$("#viewReportModal .modal-dialog").draggable();

$("#viewReportModal").on("show.bs.modal", function() {
  $(this)
    .find(".modal-body")
    .css({
      "max-height": "100%"
    });
});
/*$("#bomModal").draggable({
      handle: "#modalBodyBomHeadPanelHeading"
  });*/

var fSaveAsPdfBomData = function() {
  if (isEquipmentData)
    printJS({
      printable: currentJsonObj,
      properties: [
        "Name",
        "PartNo",
        "PowerConsumption",
        "SlotSize",
        "Details",
        "Price",
        "RevisionCode",
        "Category"
      ],
      type: "json"
    });
  else
    printJS({
      printable: currentJsonObj,
      properties: [
        "NodeId",
        "StationName",
        "SiteName",
        "Name",
        "Quantity",
        "Price",
        "TotalPrice",
        "PartNo",
        "RevisionCode",
        "Category"
      ],
      type: "json"
    });
};

$("#modalBodyviewReportContentPanelOpticalPowerAddBtn").on("click", function() {
  //	$(this).addClass('activeFocus');
  //	$("#modalBodyviewReportContentPanelOpticalPowerDropBtn").removeClass('activeFocus');
  $(this).addClass("active");
  $("#modalBodyviewReportContentPanelOpticalPowerDropBtn").removeClass(
    "active"
  );
  $(".modalBodyviewReportContentPanelOpticalPowerAddTable").show();
  $(".modalBodyviewReportContentPanelOpticalPowerDropTable").hide();

  loadExcelJsonEventFunction();
});

$("#modalBodyviewReportContentPanelOpticalPowerDropBtn").on(
  "click",
  function() {
    //	$(this).addClass('activeFocus');
    $(this).addClass("active");
    //	$("#modalBodyviewReportContentPanelOpticalPowerAddBtn").removeClass('activeFocus');
    $("#modalBodyviewReportContentPanelOpticalPowerAddBtn").removeClass(
      "active"
    );
    $(".modalBodyviewReportContentPanelOpticalPowerDropTable").show();
    $(".modalBodyviewReportContentPanelOpticalPowerAddTable").hide();

    loadExcelJsonEventFunction();
  }
);
