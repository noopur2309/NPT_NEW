var dummyJsonOptical = {
  ConstantValues: {
    McsInsertionLoss: 17,
    AwgInsertionLoss: 5.5,
    interLeaverInsertertionLoss: 1.8,
    EdfaGain: 20.8,
    WssLoss: 7,
    SupyLoss: 0.8,
    MpnLaunchPower: 1,
    BA_Gain: 20.8,
    Mux_Demux_Loss: 5.5
  },
  /*
   * NodeOpticalInfoArray:[{NodeId:'1',Direction:'N',Wavelength:'3',SpanLoss:'3',NumOfMpns:'3'},
   * {NodeId:'1',Direction:'S',Wavelength:'2',SpanLoss:'8',NumOfMpns:'3'},
   * {NodeId:'2',Direction:'SE',Wavelength:'1',SpanLoss:'10',NumOfMpns:'3'},
   * {NodeId:'2',Direction:'SW',Wavelength:'4',SpanLoss:'2',NumOfMpns:'3'},
   * {NodeId:'3',Direction:'NW',Wavelength:'2',SpanLoss:'5',NumOfMpns:'3'},
   * {NodeId:'3',Direction:'NE',Wavelength:'5',SpanLoss:'4',NumOfMpns:'3'}],
   */
  OpticalOutput: {
    CDC_Roadm: {
      PA_In: 0,
      PA_Out: 0,
      WSS_Out: 0,
      EDFA_Out: 0,
      MCS_Out: 0,
      MCS_In_R: 0,
      MCS_Out_R: 0,
      EDFA_Out_R: 0,
      BA_Out_R: 0,
      WSS_Out_R: 0,
      MCS_In_R_Cum: 0,
      WSS_To_WSS: 0,
      WSS_from_WSS: 0
    },
    BroadAndSel_Roadm: {
      Rx_PA_In: 0,
      Rx_PA_Out: 0,
      Rx_WSS_Out: 0,
      Rx_DEMUX_Out: 0,
      Tx_MUX_In: "",
      Tx_MUX_Out: 0,
      Tx_WSS_Out: 0,
      Tx_BA_Out: 0,
      Rx_WSS_To_WSS: 0,
      Tx_WSS_from_WSS: 0
    },
    ILA: {
      SUPY_In: 0,
      ILA_In: 0,
      ILA_Out: 0,
      SUPY_Out: 0,
      Constants: {
        no_of_channel_link: 0,
        dBm: 1
      }
    },
    TE: {
      PA_In: 0,
      PA_Out: 0,
      WSS_Out: 0,
      Demux_Out: 0,
      BA_Out: 0,
      BA_In: 0,
      WSS_Out_R: 0,
      MUX_In: 0,
      MUX_Out: 0
    }
  },
  DemandMatrix: [
    {
      NetworkId: 1,
      DemandId: 1,
      RoutePriority: 1,
      Path: "1,2",
      PathType: "Disjoint",
      Traffic: 1,
      WavelengthNo: 40,
      Osnr: 26.58,
      "3RLocationHeadToTail": "0",
      "3RLocationTailToHead": "0"
    },
    {
      NetworkId: 1,
      DemandId: 1,
      RoutePriority: 2,
      Path: "1,5,2",
      PathType: "Disjoint",
      Traffic: 1,
      WavelengthNo: 40,
      Osnr: 23.57,
      "3RLocationHeadToTail": "0",
      "3RLocationTailToHead": "0"
    },
    {
      NetworkId: 1,
      DemandId: 1,
      RoutePriority: 3,
      Path: "1,3,4,2",
      PathType: "Disjoint",
      Traffic: 1,
      WavelengthNo: 40,
      Osnr: 21.81,
      "3RLocationHeadToTail": "0",
      "3RLocationTailToHead": "0"
    },
    {
      NetworkId: 1,
      DemandId: 2,
      RoutePriority: 1,
      Path: "4,5",
      PathType: "Disjoint",
      Traffic: 1,
      WavelengthNo: 40,
      Osnr: 26.58,
      "3RLocationHeadToTail": "0",
      "3RLocationTailToHead": "0"
    },
    {
      NetworkId: 1,
      DemandId: 2,
      RoutePriority: 2,
      Path: "4,2,5",
      PathType: "Disjoint",
      Traffic: 1,
      WavelengthNo: 39,
      Osnr: 23.57,
      "3RLocationHeadToTail": "0",
      "3RLocationTailToHead": "0"
    },
    {
      NetworkId: 1,
      DemandId: 2,
      RoutePriority: 3,
      Path: "4,3,5",
      PathType: "Disjoint",
      Traffic: 1,
      WavelengthNo: 39,
      Osnr: 23.57,
      "3RLocationHeadToTail": "0",
      "3RLocationTailToHead": "0"
    },
    {
      NetworkId: 1,
      DemandId: 3,
      RoutePriority: 1,
      Path: "1,2,4",
      PathType: "Disjoint",
      Traffic: 1,
      WavelengthNo: 41,
      Osnr: 23.57,
      "3RLocationHeadToTail": "0",
      "3RLocationTailToHead": "0"
    },
    {
      NetworkId: 1,
      DemandId: 3,
      RoutePriority: 2,
      Path: "1,3,4",
      PathType: "Disjoint",
      Traffic: 1,
      WavelengthNo: 41,
      Osnr: 23.57,
      "3RLocationHeadToTail": "0",
      "3RLocationTailToHead": "0"
    },
    {
      NetworkId: 1,
      DemandId: 3,
      RoutePriority: 3,
      Path: "1,5,4",
      PathType: "Disjoint",
      Traffic: 1,
      WavelengthNo: 39,
      Osnr: 23.57,
      "3RLocationHeadToTail": "0",
      "3RLocationTailToHead": "0"
    }
  ]
  // NodeArray:[{NodeId:1},{NodeId:2},{NodeId:3}]
};

var JSONObj_EAST = { "E ": "E", "A ": "A", "S ": "S", "T ": "T" };
var JSONObj_WEST = { "W ": "W", "E ": "E", "S ": "S", "T ": "T" };
/*
 * var JSONObj_ILA_Values = {"SUPY_input":-10.20, "ILA_input":-10.20,
 * "ILA_output":-10.20, "no_of_channel_link":0, "dBm":1, "SUPY_output":-10.20
 *  }; var JSONObj_ILA_Values1 = {"SUPY_input":-10.20, "ILA_input":-10.20,
 * "ILA_output":-10.20, "SUPY_output":-10.20
 *
 *  };
 */

var OpticalPoweCanvas;
var ctxOpticalPowerCanvas;
var currentNodeInfoObject;

// initial call
$("#opticalPowerCanvasLiId").on("click", function(e) {
  nptGlobals.setCanvasActive(_UNSET_VALUE);

  if (!nptGlobals.getInventoryGeneratedStatus()) {
    // &&
    // sessionStorage.getItem("NetworkState") == nptGlobals.GreenFieldStr)
    bootBoxDangerAlert(
      "Generate Inventory first in order to get Optical Power Calculations."
    );
    e.stopPropagation(); // Make Canvas Tab active
    canvasActive();
  } else {
    initializeOpticalPowerCalculations();
  }
});

$("#opticalPowerFormSubmitBtn").click(fShowOpticalPowerOnCanvas); // show
// optical
// power

$("#NodeIdInputOpticalPower").on("change", function() {
  console.log("Node Id Changed Optical Power");
  var NodeId = $(this).val();
  fAppendDirections(NodeId);
}); // Change Node Id Trigger

// Returns true if Optical Data is available.
function initializeOpticalPowerCalculations() {
  console.log(
    "isInventoryGenerated :" + nptGlobals.getInventoryGeneratedStatus()
  );

  console.log("initializeOpticalPowerCalculations");
  OpticalPowerCanvas = document.getElementById("opticalPowerCanvas");
  ctxOpticalPowerCanvas = OpticalPowerCanvas.getContext("2d");
  OpticalPowerCanvas.width = 860;
  OpticalPowerCanvas.height = 650;
  // window.isFirstTimeOpticalCalculations=1;
  if (window.isFirstTimeOpticalCalculations == 1)
    fGetOpticalPowerCalculationDataAjaxCall(); // ajax call for
  // Db data

  console.log("window.isFirstTime :" + window.isFirstTimeOpticalCalculations);

  /*
   * setTimeout(function(){
   * if(window.isFirstTimeOpticalCalculations==1) {
   * populateOpticalPowerInputs();
   * window.isFirstTimeOpticalCalculations=0; } }, 1000);
   */

  // fShowOpticalPowerOnCanvas();
  // drawAll();//Optical Power Canvas
}

var fGetOpticalPowerCalculationDataAjaxCall = function() {
  overlayOn("Fetching Data", ".body-overlay");
  let postJsonData = jsonPostObject();
  console.log("getOpticalPowerJsonDataRequest Post Obj ::", postJsonData);

  serverPostMessage("getOpticalPowerJsonDataRequest", postJsonData)
    .then(function(data) {
      overlayOff("Fetch Success", ".body-overlay");
      dummyJsonOptical.NodeOpticalInfoArray = data.NodeOpticalInfoArray;
      dummyJsonOptical.NodeArray = data.NodeArray;
      console.log(data);
      if (data.NodeOpticalInfoArray.length == 0)
        bootBoxAlert("No data is available.");
      else if (window.isFirstTimeOpticalCalculations == 1) {
        populateOpticalPowerInputs();
        // window.isFirstTimeOpticalCalculations=0;
      }
    })
    .catch(function(e) {
      overlayOff("Fetch Failure", ".body-overlay");
      console.log("fail", e);
    });

  // var getOpticalPowerPostData=jsonPostObject();

  // $.ajax({

  // 	type: "POST",
  //     contentType: "application/json",

  //     headers: {
  //         Accept : "application/json; charset=utf-8",
  //        "Content-Type": "application/json; charset=utf-8"
  //     } ,

  //    url: "/getOpticalPowerJsonDataRequest",

  //    timeout: 600000,

  //    data:JSON.stringify(getOpticalPowerPostData),

  //    success: function (data) {
  // 	   dummyJsonOptical.NodeOpticalInfoArray=data.NodeOpticalInfoArray;
  // 	   dummyJsonOptical.NodeArray=data.NodeArray;
  // 	   console.log(data);
  // 	   if(data.NodeOpticalInfoArray.length==0)
  // 		   bootBoxAlert("No data is available.")
  // 	   else if(window.isFirstTimeOpticalCalculations==1)
  // 		{
  // 		populateOpticalPowerInputs();
  // 		// window.isFirstTimeOpticalCalculations=0;
  // 		}
  //    },

  //    error: function (e) {
  // 	   console.log("fail");
  // 	   console.log(e);  }
  // 	});
};

var populateOpticalPowerInputs = function() {
  var $NodeIdInput = $("#NodeIdInputOpticalPower");
  var $DirectionInput = $("#DirectionInputOpticalPower");
  $DirectionInput.empty();
  $NodeIdInput.empty();

  var nodeIdArr = dummyJsonOptical.NodeArray;

  for (var i = 0; i < nodeIdArr.length; i++) {
    $NodeIdInput.append(
      `<option value="${nodeIdArr[i].NodeId}">${nodeIdArr[i].NodeId}</option>`
    );
  }

  fAppendDirections(nodeIdArr[0].NodeId); // update direction input
};

var fNodeIdChangeOpticalPower = function() {
  console.log("Node Id Changed Optical Power");
  var NodeId = $(this).val();
  var $DirectionInput = $("#DirectionInputOpticalPower");
  $DirectionInput.empty();
  fAppendDirections(NodeId); // update direction input
};

function fShowOpticalPowerOnCanvas() {
  console.log("Inside fShowOpticalPowerOnCanvas ");
  var nodeId = $("#NodeIdInputOpticalPower").val();
  var direction = $("#DirectionInputOpticalPower").val();
  console.log("NodeId :" + nodeId + " direction :" + direction);

  var nodeInfo = dummyJsonOptical.NodeOpticalInfoArray;

  for (var j = 0; j < nodeInfo.length; j++) {
    if (nodeInfo[j].NodeId == nodeId && nodeInfo[j].Direction == direction) {
      var nodeType = fGetNodeType(nodeId);
      if (
        nodeType == nptGlobals.numNodeTypeROADM ||
        nodeType == nptGlobals.numNodeTypeCDROADM
      )
        // algorithm
        // initial
        // CDC
        // Roadm
        // //Temporary
        // Change-Debug
        fGenerateOpticalPowerRepresentationCdcRoadm(nodeInfo[j]);
      else if (nodeType == nptGlobals.numNodeTypeILA)
        // algorithm initial
        // ILA
        fGenerateOpticalPowerRepresentationIla(nodeInfo[j]);
      else if (nodeType == nptGlobals.numNodeTypeTE)
        // algorithm initial
        // TE
        fGenerateOpticalPowerRepresentationTE(nodeInfo[j]);
      else if (nodeType == nptGlobals.numNodeTypeTwoDegreeRoadm)
        // algorithm
        // initial B
        // & Select
        // Roadm
        fGenerateOpticalPowerRepresentationTwoDegreeRoadm(nodeInfo[j]);
    }
  }
}

function fGetNodeType(nodeId) {
  var nodeIdArr = dummyJsonOptical.NodeArray;
  var nodeType = 2;
  for (var i = 0; i < nodeIdArr.length; i++) {
    if (nodeIdArr[i].NodeId == nodeId) nodeType = nodeIdArr[i].NodeType;
  }
  console.log("NodeType :" + nodeType);
  return nodeType;
}

var fAppendDirections = function(NodeId) {
  let $DirectionInput = $("#DirectionInputOpticalPower");
  $DirectionInput.empty();
  let nodeInfo = dummyJsonOptical.NodeOpticalInfoArray;
  nodeInfo.map(function(val, index) {
    console.log("val", val);
    if (val.NodeId == NodeId)
      $DirectionInput.append(
        `<option value="${val.Direction}">${val.Direction}</option>`
      );
  });
};

function fGenerateOpticalPowerRepresentationCdcRoadm(nodeInfoObject) {
  console.log("fGenerateOpticalPowerRepresentationCdcRoadm", nodeInfoObject);
  window.currentNodeInfoObject = nodeInfoObject;
  var totalChannels = nodeInfoObject.Wavelength;
  var spanLoss = nodeInfoObject.SpanLoss;
  var numOfMpns = nodeInfoObject.NumOfMpns;

  console.log("Current Node Info Obj ::", nodeInfoObject);
  console.log(
    "totalChannels :" +
      totalChannels +
      " spanLoss :" +
      spanLoss +
      " numOfMpns:" +
      numOfMpns
  );

  var Constants = dummyJsonOptical.ConstantValues;
  console.log("Constants :", Constants);

  if (totalChannels == 0) {
    bootBoxWarningAlert(
      "No wavelength is configured in " +
        nodeInfoObject.Direction +
        " direction of Node " +
        nodeInfoObject.NodeId,
      2000
    );
  } else {
    // console.log(Math.log(totalChannels) +"spanLoss
    // :"+spanLoss+"Constants.SupyLoss "+Constants.SupyLoss)
    // PA in
    var PA_In = 4.34 * Math.log(totalChannels) - spanLoss - Constants.SupyLoss;
    console.log("PA_In :" + PA_In);
    // PA out
    var PA_Out = PA_In + spanLoss + Constants.SupyLoss;
    console.log("PA_Out == PA_In :" + PA_Out);
    // wss
    var WSS_Out = PA_Out - Constants.WssLoss;
    console.log("WSS_Out=PA_Out-Constants.WssLoss :" + WSS_Out);
    if (WSS_Out > 2.5) WSS_Out = 2.5;
    // edfa
    var EDFA_Out = WSS_Out + Constants.EdfaGain;
    console.log("EDFA_Out=WSS_Out+Constants.EdfaGain :" + EDFA_Out);
    // mcs
    var MCS_Out = EDFA_Out - Constants.McsInsertionLoss;
    console.log("MCS_Out=EDFA_Out-Constants.McsInsertionLoss :" + MCS_Out);

    console.log("OpticalOutput");
    console.log(dummyJsonOptical.OpticalOutput.CDC_Roadm);

    // set values in json
    dummyJsonOptical.OpticalOutput.CDC_Roadm.PA_In =
      Math.round(PA_In * 100) / 100;
    dummyJsonOptical.OpticalOutput.CDC_Roadm.PA_Out =
      Math.round(PA_Out * 100) / 100;
    dummyJsonOptical.OpticalOutput.CDC_Roadm.WSS_Out =
      Math.round(WSS_Out * 100) / 100;
    dummyJsonOptical.OpticalOutput.CDC_Roadm.EDFA_Out =
      Math.round(EDFA_Out * 100) / 100;
    dummyJsonOptical.OpticalOutput.CDC_Roadm.MCS_Out =
      Math.round(MCS_Out * 100) / 100;

    var WSS_To_WSS = totalChannels - numOfMpns;
    console.log("wss to wss  " + WSS_To_WSS);
    var WSS_from_WSS = totalChannels - numOfMpns;
    console.log("wss from wss  " + WSS_from_WSS);

    var MCS_In_R = Constants.MpnLaunchPower;
    console.log("MCS_In_R=Constants.MpnLaunchPower :" + MCS_In_R);
    var MCS_In_R_Cumulative = 4.34 * Math.log(1.2589 * numOfMpns);
    console.log(
      "MCS_In_R_Cumulative=4.34*Math.log(1.2589 * numOfMpns) :" +
        MCS_In_R_Cumulative
    );
    MCS_Out = MCS_In_R_Cumulative - Constants.McsInsertionLoss;
    console.log(
      "MCS_Out=(MCS_In_R_Cumulative)-Constants.McsInsertionLoss :" + MCS_Out
    );
    EDFA_Out = MCS_Out + Constants.EdfaGain;
    console.log("EDFA_Out=MCS_Out+Constants.EdfaGain :" + EDFA_Out);
    WSS_Out = EDFA_Out - Constants.WssLoss;
    console.log("WSS_Out=EDFA_Out-Constants.WssLoss :" + WSS_Out);
    // var expected_BA_In = -4.34*Math.log(100 * totalChannels ); //
    // Ba in should be 20 dbm per channel

    var expected_BA_In = -20 + 4.34 * Math.log(1 * totalChannels); // Ba
    // in
    // should
    // be
    // 20
    // dbm
    // per
    // channel
    /*
     * if(WSS_Out!=-20) WSS_Out=-20;
     */
    console.log(
      "expected_BA_In = 4.34*Math.log(100 * totalChannels) :" + expected_BA_In
    );
    var attenuation_CDC_Roadm = expected_BA_In - WSS_Out;
    console.log(
      "attenuation_CDC_Roadm=expected_BA_In - WSS_Out :" + attenuation_CDC_Roadm
    );
    var BA_In = expected_BA_In;
    console.log("BA_In=expected_BA_In - WSS_Out :" + BA_In);
    var BA_Out = BA_In + 20;
    console.log("BA_Out=BA_In + 20 :" + BA_Out);
    //

    // set values in json
    dummyJsonOptical.OpticalOutput.CDC_Roadm.BA_Out_R =
      Math.round(BA_Out * 100) / 100;
    dummyJsonOptical.OpticalOutput.CDC_Roadm.WSS_Out_R =
      Math.round(BA_In * 100) / 100;
    dummyJsonOptical.OpticalOutput.CDC_Roadm.EDFA_Out_R =
      Math.round(EDFA_Out * 100) / 100;
    dummyJsonOptical.OpticalOutput.CDC_Roadm.MCS_Out_R =
      Math.round(MCS_Out * 100) / 100;
    dummyJsonOptical.OpticalOutput.CDC_Roadm.MCS_In_R =
      Math.round(MCS_In_R * 100) / 100;
    dummyJsonOptical.OpticalOutput.CDC_Roadm.WSS_To_WSS =
      Math.round(WSS_To_WSS * 100) / 100;
    dummyJsonOptical.OpticalOutput.CDC_Roadm.WSS_from_WSS =
      Math.round(WSS_from_WSS * 100) / 100;
    dummyJsonOptical.OpticalOutput.CDC_Roadm.MCS_In_R_Cum =
      Math.round(MCS_In_R_Cumulative * 100) / 100;

    console.log("OpticalOutput");
    console.log(dummyJsonOptical.OpticalOutput.CDC_Roadm);

    // window.isFirstTimeOpticalCalculations=0;
    drawAll(nodeInfoObject.NodeId);
  }
}

function fGenerateOpticalPowerRepresentationTwoDegreeRoadm(nodeInfoObject) {
  console.log("fGenerateOpticalPowerRepresentationCdcRoadm", nodeInfoObject);
  window.currentNodeInfoObject = nodeInfoObject;
  var totalChannels = nodeInfoObject.Wavelength;
  var spanLoss = nodeInfoObject.SpanLoss;
  var numOfMpns = nodeInfoObject.NumOfMpns;

  console.log("Current Node Info Obj ::", nodeInfoObject);
  console.log(
    "totalChannels :" +
      totalChannels +
      " spanLoss :" +
      spanLoss +
      " numOfMpns:" +
      numOfMpns
  );

  var Constants = dummyJsonOptical.ConstantValues;
  console.log("Constants :", Constants);

  if (totalChannels == 0) {
    bootBoxWarningAlert(
      "No wavelength is configured in " +
        nodeInfoObject.Direction +
        " direction of Node " +
        nodeInfoObject.NodeId,
      2000
    );
  } else {
    // console.log(Math.log(totalChannels) +"spanLoss
    // :"+spanLoss+"Constants.SupyLoss "+Constants.SupyLoss)
    // PA in
    var PA_In = 10 * Math.log10(totalChannels) - spanLoss - Constants.SupyLoss;
    var PA_Out = PA_In + spanLoss + Constants.SupyLoss;
    var WSS_Out = PA_Out - Constants.WssLoss;
    if (WSS_Out > 2.5) WSS_Out = 2.5;
    var DEMUX_Out =
      WSS_Out -
      Constants.AwgInsertionLoss -
      Constants.interLeaverInsertertionLoss;
    var WSS_To_WSS = totalChannels - numOfMpns;
    var WSS_from_WSS = totalChannels - numOfMpns;

    // set values in json
    dummyJsonOptical.OpticalOutput.BroadAndSel_Roadm.Rx_PA_In =
      Math.round(PA_In * 100) / 100;
    dummyJsonOptical.OpticalOutput.BroadAndSel_Roadm.Rx_PA_Out =
      Math.round(PA_Out * 100) / 100;
    dummyJsonOptical.OpticalOutput.BroadAndSel_Roadm.Rx_WSS_Out =
      Math.round(WSS_Out * 100) / 100;
    dummyJsonOptical.OpticalOutput.BroadAndSel_Roadm.Rx_DEMUX_Out =
      Math.round(DEMUX_Out * 100) / 100;

    console.log(dummyJsonOptical.OpticalOutput.BroadAndSel_Roadm);

    var MUX_In_R_Cumulative = 10 * Math.log10(1.2589 * numOfMpns);
    var MUX_Out =
      MUX_In_R_Cumulative -
      Constants.AwgInsertionLoss -
      Constants.interLeaverInsertertionLoss;
    var WSS_Out = MUX_Out - Constants.WssLoss;

    // Ba in should be 20 dbm per channel
    var expected_BA_In = -20 + 10 * Math.log10(1 * totalChannels);
    var attenuation_BroadAndSel_Roadm = expected_BA_In - WSS_Out;
    var BA_In = expected_BA_In;
    var BA_Out = BA_In + 20;

    // set values in json
    dummyJsonOptical.OpticalOutput.BroadAndSel_Roadm.Tx_MUX_In =
      Math.round(MUX_In_R_Cumulative * 100) / 100;
    dummyJsonOptical.OpticalOutput.BroadAndSel_Roadm.Tx_MUX_Out =
      Math.round(MUX_Out * 100) / 100;
    dummyJsonOptical.OpticalOutput.BroadAndSel_Roadm.Tx_WSS_Out =
      Math.round(WSS_Out * 100) / 100;
    dummyJsonOptical.OpticalOutput.BroadAndSel_Roadm.Tx_BA_Out =
      Math.round(BA_Out * 100) / 100;

    console.log("OpticalOutput");
    console.log(dummyJsonOptical.OpticalOutput.BroadAndSel_Roadm);

    // window.isFirstTimeOpticalCalculations=0;
    drawAll_broadAndSel(nodeInfoObject.NodeId);
  }
}

var fGenerateOpticalPowerRepresentationIla = function(nodeInfoObject) {
  window.currentNodeInfoObject = nodeInfoObject;
  // to be done
  var totalChannels = nodeInfoObject.Wavelength;
  var spanLoss = nodeInfoObject.SpanLoss;
  var numOfMpns = 3; /* nodeInfoObject.NumOfMpns */

  if (totalChannels == 0) {
    bootBoxAlert(
      "No wavelength is configured in ",
      nodeInfoObject.Direction + " direction of Node ",
      nodeInfoObject.NodeId
    );
  } else {
    var Constants = dummyJsonOptical.ConstantValues;
    console.log("Constants :");
    console.log(Constants);
    var SUPY_In = 4.34 * Math.log(totalChannels) - spanLoss;
    console.log("SUPY_In=4.34*Math.log(totalChannels)-spanLoss :" + SUPY_In);
    // SUPY out
    var ILA_In = SUPY_In - Constants.SupyLoss;
    console.log("ILA_In=SUPY_In-Constants.SupyLoss :" + ILA_In);
    // ILA out
    var ILA_Out = ILA_In + spanLoss + Constants.SupyLoss;
    console.log("ILA_Out=SUPY_Out+spanLoss+Constants.SupyLoss :" + ILA_Out);

    // Supy
    var SUPY_Out = ILA_Out - Constants.SupyLoss;
    console.log("SUPY_Out=ILA_Out-Constants.SupyLoss :" + SUPY_Out);

    console.log("OpticalOutput");
    console.log(dummyJsonOptical.OpticalOutput.ILA);

    //

    // set values in json
    dummyJsonOptical.OpticalOutput.ILA.SUPY_In =
      Math.round(SUPY_In * 100) / 100;
    dummyJsonOptical.OpticalOutput.ILA.ILA_In = Math.round(ILA_In * 100) / 100;
    dummyJsonOptical.OpticalOutput.ILA.ILA_Out =
      Math.round(ILA_Out * 100) / 100;
    dummyJsonOptical.OpticalOutput.ILA.SUPY_Out =
      Math.round(SUPY_Out * 100) / 100;
    dummyJsonOptical.OpticalOutput.ILA.Constants.no_of_channel_link =
      nodeInfoObject.Wavelength;
    drawAll_Ila();
  }
};

function fGenerateOpticalPowerRepresentationTE(nodeInfoObject) {
  window.currentNodeInfoObject = nodeInfoObject;
  var totalChannels = nodeInfoObject.Wavelength;
  var spanLoss = nodeInfoObject.SpanLoss;
  var numOfMpns = nodeInfoObject.NumOfMpns;

  console.log(
    "totalChannels :" +
      totalChannels +
      " spanLoss :" +
      spanLoss +
      " numOfMpns:" +
      numOfMpns
  );

  var Constants = dummyJsonOptical.ConstantValues;
  console.log("Constants :");
  console.log(Constants);

  if (totalChannels == 0) {
    bootBoxAlert(
      "No wavelength is configured in ",
      nodeInfoObject.Direction + " direction of Node ",
      nodeInfoObject.NodeId
    );
  } else {
    // console.log(Math.log(totalChannels) +"spanLoss
    // :"+spanLoss+"Constants.SupyLoss "+Constants.SupyLoss)
    // PA in
    var PA_In = 4.34 * Math.log(totalChannels) - spanLoss - Constants.SupyLoss;
    console.log("PA_In :" + PA_In);
    // PA out
    var PA_Out = Number(PA_In) + Number(spanLoss) + Number(Constants.SupyLoss);
    console.log("PA_Out == PA_In + Constants.SupyLoss :" + PA_Out);
    // wss
    var WSS_Out = PA_Out - Constants.WssLoss;
    console.log("WSS_Out=PA_Out-Constants.WssLoss :" + WSS_Out);
    /*
     * if(WSS_Out>2.5) WSS_Out=2.5;
     */
    // edfa
    var DEMUX_Out = WSS_Out - Constants.Mux_Demux_Loss;
    console.log("DEMUX_Out=WSS_Out-Constants.Mux_Demux_Loss :" + DEMUX_Out);

    console.log("OpticalOutput TE");
    console.log(dummyJsonOptical.OpticalOutput);

    // set values in json
    dummyJsonOptical.OpticalOutput.TE.PA_In = Math.round(PA_In * 100) / 100;
    dummyJsonOptical.OpticalOutput.TE.PA_Out = Math.round(PA_Out * 100) / 100;
    dummyJsonOptical.OpticalOutput.TE.WSS_Out = Math.round(WSS_Out * 100) / 100;
    dummyJsonOptical.OpticalOutput.TE.DEMUX_Out =
      Math.round(DEMUX_Out * 100) / 100;

    var WSS_To_WSS = totalChannels - numOfMpns;
    console.log("wss to wss  " + WSS_To_WSS);
    var WSS_from_WSS = totalChannels - numOfMpns;
    console.log("wss  from wss  " + WSS_from_WSS);

    var MUX_In = Constants.MpnLaunchPower;
    console.log("MUX_In=Constants.MpnLaunchPower :" + MUX_In);
    var MUX_In_Cumulative = 4.34 * Math.log(1.2589 * numOfMpns);
    console.log(
      "MUX_In_Cumulative=4.34*Math.log(1.2589 * numOfMpns) :" +
        MUX_In_Cumulative
    );
    var MUX_Out = MUX_In_Cumulative - Constants.Mux_Demux_Loss;
    console.log(
      "MUX_Out=MUX_In_Cumulative-Constants.Mux_Demux_Loss :" + MUX_Out
    );
    var WSS_Out_R = MUX_Out - Constants.WssLoss;
    console.log(" WSS_Out_R=MUX_Out-Constants.WssLoss :" + WSS_Out_R);

    // var expected_BA_In = -4.34*Math.log(100 * totalChannels ); // Ba
    // in should be 20 dbm per channel

    var expected_BA_In_TE = -20 + 4.34 * Math.log(1 * totalChannels); // Ba
    // in
    // should
    // be
    // 20
    // dbm
    // per
    // channel
    /*
     * if(WSS_Out!=-20) WSS_Out=-20;
     */
    console.log(
      "expected_BA_In_TE = -20 +4.34*Math.log(1 * totalChannels ) :" +
        expected_BA_In_TE
    );
    var attenuation_TE = expected_BA_In_TE - WSS_Out_R;
    console.log(
      " attenuation_TE=expected_BA_In_TE - WSS_Out_R :" + attenuation_TE
    );
    var BA_In = expected_BA_In_TE;
    console.log("BA_In=expected_BA_In - WSS_Out :" + BA_In);
    var BA_Out = BA_In + 20;
    console.log("BA_Out=BA_In + 20 :" + BA_Out);
    //

    // set values in json
    dummyJsonOptical.OpticalOutput.TE.BA_Out = Math.round(BA_Out * 100) / 100;
    dummyJsonOptical.OpticalOutput.TE.WSS_Out_R =
      Math.round(WSS_Out_R * 100) / 100;
    dummyJsonOptical.OpticalOutput.TE.MUX_In =
      Math.round(MUX_In_Cumulative * 100) / 100;
    dummyJsonOptical.OpticalOutput.TE.MUX_Out = Math.round(MUX_Out * 100) / 100;

    console.log("OpticalOutput TE");
    console.log(dummyJsonOptical.OpticalOutput.TE);

    // window.isFirstTimeOpticalCalculations=0;
    drawAll_TE();
  }
}

function drawAll(nodeId) {
  ctxOpticalPowerCanvas.clearRect(
    0,
    0,
    OpticalPowerCanvas.width,
    OpticalPowerCanvas.height
  );
  console.log("draw all");
  var nodeType = fGetNodeType(nodeId);
  drawBack(nodeType);
  draw_All_rect();
  draw_triangle(270, 440, 240, 420, 240, 460);
  draw_triangle(240, 570, 270, 550, 270, 590);
  drawSUPY(142, 490, 6, 8);
  drawSUPY(142, 620, 1, 10);
  draw_Network();
  draw_All_IP_OP();
  write_Text();
  draw_All_Circes();
  console.log("draw all done");
}
function draw_All_rect() {
  draw_rect(420, 150, 120, 20); // 1
  draw_rect(620, 150, 120, 20); // 1
  draw_rect(400, 270, 340, 20); // MCS
  draw_rect(400, 370, 210, 20); // EDFA ARRAY
  draw_rect(360, 390, 20, 100); // wss1
  draw_rect(360, 520, 20, 100); // wss2
  draw_rect(50, 150, 250, 150); // details
}

function draw_All_Circes() {
  // 1st up
  draw_Circle(
    480,
    100,
    60,
    30,
    dummyJsonOptical.OpticalOutput.CDC_Roadm.MCS_In_R
  );
  // 2nd up
  draw_Circle(
    680,
    100,
    60,
    30,
    dummyJsonOptical.OpticalOutput.CDC_Roadm.MCS_In_R
  );
  // 2nd level circle
  draw_Circle(
    480,
    210,
    60,
    30,
    dummyJsonOptical.OpticalOutput.CDC_Roadm.MCS_Out
  );
  draw_Circle(
    680,
    210,
    60,
    30,
    dummyJsonOptical.OpticalOutput.CDC_Roadm.MCS_In_R_Cum
  );
  // 3rd level circle
  draw_Circle(
    462,
    325,
    80,
    30,
    dummyJsonOptical.OpticalOutput.CDC_Roadm.EDFA_Out
  );
  draw_Circle(
    554,
    315,
    80,
    30,
    dummyJsonOptical.OpticalOutput.CDC_Roadm.MCS_Out_R
  );
  // 4th and 5th level circle
  draw_Circle(
    300,
    440,
    40,
    30,
    dummyJsonOptical.OpticalOutput.CDC_Roadm.PA_Out
  );
  draw_Circle(
    310,
    570,
    40,
    30,
    dummyJsonOptical.OpticalOutput.CDC_Roadm.WSS_Out_R
  );
  draw_Circle(135, 440, 60, 40, dummyJsonOptical.OpticalOutput.CDC_Roadm.PA_In);
  draw_Circle(
    135,
    570,
    60,
    40,
    dummyJsonOptical.OpticalOutput.CDC_Roadm.BA_Out_R
  );
  // wss to EDFA
  draw_Circle(
    425,
    420,
    50,
    30,
    dummyJsonOptical.OpticalOutput.CDC_Roadm.WSS_Out
  );
  draw_Circle(
    425,
    550,
    50,
    30,
    dummyJsonOptical.OpticalOutput.CDC_Roadm.EDFA_Out_R
  );
}
function draw_All_IP_OP() {
  // draw_IP_OP(x1,y1,x2,y2,x3,y3);
  draw_IP_OP(360, 440, 355, 445, 355, 435);
  draw_IP_OP(240, 440, 235, 435, 235, 445);
  // supy
  draw_IP_OP(169, 490, 174, 485, 164, 485);
  draw_IP_OP(169, 620, 174, 615, 164, 615);
  draw_IP_OP(270, 570, 275, 565, 275, 575);

  // EDFA
  draw_IP_OP(450, 390, 445, 395, 455, 395);
  draw_IP_OP(650, 465, 650, 475, 655, 470);
  draw_IP_OP(380, 550, 385, 545, 385, 555);
  draw_IP_OP(380, 590, 385, 585, 385, 595);
  // frst up(1)
  draw_IP_OP(480, 55, 475, 60, 485, 60);
  // frst up (2)
  draw_IP_OP(680, 147, 675, 142, 685, 142);
  // level 2(1)
  draw_IP_OP(480, 173, 475, 178, 485, 178);
  // level 2(2)
  draw_IP_OP(680, 267, 675, 262, 685, 262);
  // level 3 (1)
  draw_IP_OP(440, 293, 435, 298, 445, 298);
  draw_IP_OP(455, 293, 450, 298, 460, 298);
  draw_IP_OP(470, 293, 465, 298, 475, 298);
  draw_IP_OP(485, 293, 480, 298, 490, 298);
  // level 3(2)
  draw_IP_OP(530, 367, 535, 362, 525, 362);
  draw_IP_OP(545, 367, 550, 362, 540, 362);
  draw_IP_OP(560, 367, 565, 362, 555, 362);
  draw_IP_OP(575, 367, 580, 362, 570, 362);
}
function drawBack(nodeType) {
  console.log("draw back");

  ctxOpticalPowerCanvas.fillStyle = "#fffcfc";
  ctxOpticalPowerCanvas.beginPath();
  ctxOpticalPowerCanvas.rect(5, 5, 790, 640);
  ctxOpticalPowerCanvas.closePath();
  ctxOpticalPowerCanvas.shadowBlur = 1;
  ctxOpticalPowerCanvas.shadowColor = "black";
  ctxOpticalPowerCanvas.fill();
  ctxOpticalPowerCanvas.lineWidth = 2;
  ctxOpticalPowerCanvas.strokeStyle = "#333";
  ctxOpticalPowerCanvas.stroke();
  ctxOpticalPowerCanvas.fillStyle = "black";
  ctxOpticalPowerCanvas.font = "30px Arial";
  if (nodeType == nptGlobals.numNodeTypeTwoDegreeRoadm)
    ctxOpticalPowerCanvas.fillText("Optical Power (B&S ROADM)", 210, 40);
  else ctxOpticalPowerCanvas.fillText("Optical Power (CDC-ROADM)", 210, 40);
}
function draw_Circle(cx, cy, width, height, ct) {
  ctxOpticalPowerCanvas.beginPath();
  ctxOpticalPowerCanvas.moveTo(cx, cy - height / 2); // A1

  ctxOpticalPowerCanvas.bezierCurveTo(
    cx + width / 2,
    cy - height / 2, // C1
    cx + width / 2,
    cy + height / 2, // C2
    cx,
    cy + height / 2
  ); // A2

  ctxOpticalPowerCanvas.bezierCurveTo(
    cx - width / 2,
    cy + height / 2, // C3
    cx - width / 2,
    cy - height / 2, // C4
    cx,
    cy - height / 2
  ); // A1
  ctxOpticalPowerCanvas.lineWidth = 3;
  ctxOpticalPowerCanvas.strokeStyle = "#5db777";
  ctxOpticalPowerCanvas.fillStyle = "#5db777";
  ctxOpticalPowerCanvas.stroke();
  // style for values
  ctxOpticalPowerCanvas.fillStyle = "black";
  ctxOpticalPowerCanvas.font = " bold 13pt  Times New Roman ";
  ctxOpticalPowerCanvas.fillText(ct, cx - 10, cy + 35);
}

function draw_rect(x, y, width, height) {
  ctxOpticalPowerCanvas.beginPath();
  ctxOpticalPowerCanvas.rect(x, y, width, height);
  ctxOpticalPowerCanvas.fillStyle = "#ffffff";

  ctxOpticalPowerCanvas.fill();
  ctxOpticalPowerCanvas.lineWidth = 0.5;
  ctxOpticalPowerCanvas.strokeStyle = "black";
  ctxOpticalPowerCanvas.fillStyle = "black";
  ctxOpticalPowerCanvas.stroke();
}

// function to draw the ILA
function draw_triangle(x1, y1, x2, y2, x3, y3) {
  ctxOpticalPowerCanvas.beginPath();
  ctxOpticalPowerCanvas.moveTo(x1, y1);
  ctxOpticalPowerCanvas.lineTo(x2, y2);
  ctxOpticalPowerCanvas.lineTo(x3, y3);
  ctxOpticalPowerCanvas.closePath();

  // the outline

  ctxOpticalPowerCanvas.lineWidth = 1;
  ctxOpticalPowerCanvas.strokeStyle = "#666666";
  ctxOpticalPowerCanvas.stroke();

  // the fill color
  ctxOpticalPowerCanvas.fillStyle = "black";
}
function drawSUPY(x, y, supy, height) {
  ctxOpticalPowerCanvas.beginPath();
  ctxOpticalPowerCanvas.rect(x, y, 60, 20);
  ctxOpticalPowerCanvas.fillStyle = "#ffffff";

  ctxOpticalPowerCanvas.fill();
  ctxOpticalPowerCanvas.lineWidth = 1;
  ctxOpticalPowerCanvas.strokeStyle = "black";
  ctxOpticalPowerCanvas.fillStyle = "black";
  ctxOpticalPowerCanvas.stroke();
  // text
  ctxOpticalPowerCanvas.font = "12px Arial";
  ctxOpticalPowerCanvas.fillText("SUPY", x + 10, y + 15);
}
function draw_Network() {
  console.log("draw networks");
  ctxOpticalPowerCanvas.beginPath();
  // ist part
  ctxOpticalPowerCanvas.moveTo(360, 440);
  ctxOpticalPowerCanvas.lineTo(270, 440);
  ctxOpticalPowerCanvas.moveTo(100, 440);
  ctxOpticalPowerCanvas.lineTo(240, 440);
  ctxOpticalPowerCanvas.moveTo(360, 570);
  ctxOpticalPowerCanvas.lineTo(270, 570);
  ctxOpticalPowerCanvas.moveTo(100, 570);
  ctxOpticalPowerCanvas.lineTo(240, 570);
  // supy network 1
  ctxOpticalPowerCanvas.moveTo(170, 440); //
  ctxOpticalPowerCanvas.lineTo(170, 490); //
  // supy netwrk 2
  ctxOpticalPowerCanvas.moveTo(170, 570); //
  ctxOpticalPowerCanvas.lineTo(170, 620); //
  // wss to EDFA 1
  ctxOpticalPowerCanvas.moveTo(380, 420); //
  ctxOpticalPowerCanvas.lineTo(450, 420); //
  ctxOpticalPowerCanvas.lineTo(450, 395); //
  // wss to other wss
  ctxOpticalPowerCanvas.moveTo(380, 470); //
  ctxOpticalPowerCanvas.lineTo(650, 470); //
  // wss to EDFA 2
  ctxOpticalPowerCanvas.moveTo(380, 550); //
  ctxOpticalPowerCanvas.lineTo(520, 550); //
  ctxOpticalPowerCanvas.lineTo(520, 390); //
  // wss from other wss
  ctxOpticalPowerCanvas.moveTo(380, 590); //
  ctxOpticalPowerCanvas.lineTo(650, 590); //
  // 3rd level 1st
  ctxOpticalPowerCanvas.moveTo(440, 370); //
  ctxOpticalPowerCanvas.lineTo(440, 300); //
  ctxOpticalPowerCanvas.moveTo(455, 370); //
  ctxOpticalPowerCanvas.lineTo(455, 300); //
  ctxOpticalPowerCanvas.moveTo(470, 370); //
  ctxOpticalPowerCanvas.lineTo(470, 300); //
  ctxOpticalPowerCanvas.moveTo(485, 370); //
  ctxOpticalPowerCanvas.lineTo(485, 300); //
  // 3rd level 2nd
  ctxOpticalPowerCanvas.moveTo(530, 360); //
  ctxOpticalPowerCanvas.lineTo(530, 290); //
  ctxOpticalPowerCanvas.moveTo(545, 360); //
  ctxOpticalPowerCanvas.lineTo(545, 290); //
  ctxOpticalPowerCanvas.moveTo(560, 360); //
  ctxOpticalPowerCanvas.lineTo(560, 290); //
  ctxOpticalPowerCanvas.moveTo(575, 360); //
  ctxOpticalPowerCanvas.lineTo(575, 290); //
  // level 2 (1)
  ctxOpticalPowerCanvas.moveTo(480, 175); //
  ctxOpticalPowerCanvas.lineTo(480, 270); //

  // level 2(2)
  ctxOpticalPowerCanvas.moveTo(680, 170); //
  ctxOpticalPowerCanvas.lineTo(680, 265); //

  // 1st up
  ctxOpticalPowerCanvas.moveTo(480, 60); //
  ctxOpticalPowerCanvas.lineTo(480, 150); //

  // 2 up
  ctxOpticalPowerCanvas.moveTo(680, 60); //
  ctxOpticalPowerCanvas.lineTo(680, 145); //

  ctxOpticalPowerCanvas.lineWidth = 2;
  ctxOpticalPowerCanvas.strokeStyle = "#fff";
  ctxOpticalPowerCanvas.stroke();
}
function write_Text() {
  console.log("currentNodeInfoObject", currentNodeInfoObject);
  var wavelength = window.currentNodeInfoObject.Wavelength;
  var spanLoss = window.currentNodeInfoObject.SpanLoss;
  var numOfMpn = window.currentNodeInfoObject.NumOfMpns;
  ctxOpticalPowerCanvas.fillStyle = "#102c49";
  ctxOpticalPowerCanvas.fill();
  ctxOpticalPowerCanvas.font = "  12pt  Arial ";
  ctxOpticalPowerCanvas.fillText("W", 365, 420);
  ctxOpticalPowerCanvas.fillText("S", 365, 450);
  ctxOpticalPowerCanvas.fillText("S", 365, 480);
  ctxOpticalPowerCanvas.fillText("W", 365, 550);
  ctxOpticalPowerCanvas.fillText("S", 365, 580);
  ctxOpticalPowerCanvas.fillText("S", 365, 610);
  // FDFA ARRAY
  ctxOpticalPowerCanvas.fillText("EDFA   ARRAY", 440, 385);
  ctxOpticalPowerCanvas.fillText("MCS", 530, 285);

  if (numOfMpn == 0) {
    ctxOpticalPowerCanvas.fillText("No MPN/TPN", 430, 165);
  } else if (numOfMpn == 1)
    ctxOpticalPowerCanvas.fillText("1 MPN/TPN", 430, 165);
  else {
    //ctxOpticalPowerCanvas.fillText("MPN1..."+"MPN"+numOfMpn,430,165);
    ctxOpticalPowerCanvas.fillText(numOfMpn + " MPN/TPN", 430, 165);
  }

  if (numOfMpn == 0) {
    ctxOpticalPowerCanvas.fillText("No MPN/TPN", 630, 165);
  } else if (numOfMpn == 1)
    ctxOpticalPowerCanvas.fillText("1 MPN/TPN", 630, 165);
  else {
    ctxOpticalPowerCanvas.fillText(numOfMpn + " MPN/TPN", 630, 165);
  }
  //

  ctxOpticalPowerCanvas.fillText("Unit=dBm", 75, 175);
  ctxOpticalPowerCanvas.fillText("No of λ in Link= " + wavelength, 75, 200);
  ctxOpticalPowerCanvas.fillText("MPN Launch Power=+1dBm", 75, 225);
  ctxOpticalPowerCanvas.fillText("Span Loss=" + spanLoss, 75, 250);
  ctxOpticalPowerCanvas.fillText(
    "No Of Add/Drop Channel =" + numOfMpn,
    75,
    275
  );
  //
  ctxOpticalPowerCanvas.font = "  10pt  Arial ";
  ctxOpticalPowerCanvas.fillText(
    "To other WSS" +
      "( λ= " +
      dummyJsonOptical.OpticalOutput.CDC_Roadm.WSS_To_WSS +
      ")",
    480,
    465
  );
  ctxOpticalPowerCanvas.fillText(
    "From other WSS" +
      "( λ= " +
      dummyJsonOptical.OpticalOutput.CDC_Roadm.WSS_from_WSS +
      ")",
    480,
    585
  );
}

function draw_IP_OP(x1, y1, x2, y2, x3, y3) {
  ctxOpticalPowerCanvas.beginPath();
  ctxOpticalPowerCanvas.moveTo(x1, y1);
  ctxOpticalPowerCanvas.lineTo(x2, y2);
  ctxOpticalPowerCanvas.lineTo(x3, y3);
  ctxOpticalPowerCanvas.closePath();

  // the outline

  ctxOpticalPowerCanvas.lineWidth = 1;
  ctxOpticalPowerCanvas.strokeStyle = "#666666";
  ctxOpticalPowerCanvas.stroke();

  // the fill color
  ctxOpticalPowerCanvas.fillStyle = "green";
  ctxOpticalPowerCanvas.fill();
}

//Functions for drawing TwoDegreeRoadm(optical Power)
function drawAll_broadAndSel(nodeId) {
  ctxOpticalPowerCanvas.clearRect(
    0,
    0,
    OpticalPowerCanvas.width,
    OpticalPowerCanvas.height
  );
  console.log("draw all");
  var nodeType = fGetNodeType(nodeId);
  drawBack(nodeType);
  draw_All_rect_broadAndSel();
  draw_triangle(270, 440, 240, 420, 240, 460); //PA
  draw_triangle(240, 570, 270, 550, 270, 590); //BA
  drawSUPY(142, 490, 6, 8);
  drawSUPY(142, 620, 1, 10);
  draw_Network_broadAndSel();
  draw_All_IP_OP_broadAndSel();
  write_Text_broadAndSel();
  draw_All_Circes_broadAndSel();
  console.log("draw all done");
}

function draw_All_rect_broadAndSel() {
  draw_rect(420, 150, 120, 20); // TPN
  draw_rect(620, 150, 120, 20); // TPN
  draw_rect(400, 270, 340, 20); // MUX/DEMUX
  draw_rect(360, 390, 20, 100); // wss1
  draw_rect(360, 520, 20, 100); // wss2
  draw_rect(50, 150, 250, 150); // details
}

function draw_Network_broadAndSel() {
  console.log("draw networks");
  ctxOpticalPowerCanvas.beginPath();
  //BA and PA
  ctxOpticalPowerCanvas.moveTo(360, 440);
  ctxOpticalPowerCanvas.lineTo(270, 440);
  ctxOpticalPowerCanvas.moveTo(100, 440);
  ctxOpticalPowerCanvas.lineTo(240, 440);
  ctxOpticalPowerCanvas.moveTo(360, 570);
  ctxOpticalPowerCanvas.lineTo(270, 570);
  ctxOpticalPowerCanvas.moveTo(100, 570);
  ctxOpticalPowerCanvas.lineTo(240, 570);
  // supy PA
  ctxOpticalPowerCanvas.moveTo(170, 440); //
  ctxOpticalPowerCanvas.lineTo(170, 490); //
  // supy BA
  ctxOpticalPowerCanvas.moveTo(170, 570); //
  ctxOpticalPowerCanvas.lineTo(170, 620); //
  // wss to DEMUX
  ctxOpticalPowerCanvas.moveTo(380, 420); //
  ctxOpticalPowerCanvas.lineTo(480, 420); //
  ctxOpticalPowerCanvas.lineTo(480, 290); //
  // wss to other wss
  ctxOpticalPowerCanvas.moveTo(380, 470); //
  ctxOpticalPowerCanvas.lineTo(650, 470); //
  // MUX to wss
  ctxOpticalPowerCanvas.moveTo(380, 550); //
  ctxOpticalPowerCanvas.lineTo(680, 550); //
  ctxOpticalPowerCanvas.lineTo(680, 290); //
  // wss from other wss
  ctxOpticalPowerCanvas.moveTo(380, 590); //
  ctxOpticalPowerCanvas.lineTo(650, 590); //

  // DEMUX To TPN
  ctxOpticalPowerCanvas.moveTo(480, 175); //
  ctxOpticalPowerCanvas.lineTo(480, 270); //

  // TPN To MUX
  ctxOpticalPowerCanvas.moveTo(680, 170); //
  ctxOpticalPowerCanvas.lineTo(680, 265); //

  // MPN Rx Launch
  ctxOpticalPowerCanvas.moveTo(480, 60); //
  ctxOpticalPowerCanvas.lineTo(480, 150); //

  // MPN Tx Launch
  ctxOpticalPowerCanvas.moveTo(680, 60); //
  ctxOpticalPowerCanvas.lineTo(680, 145); //

  ctxOpticalPowerCanvas.lineWidth = 2;
  ctxOpticalPowerCanvas.strokeStyle = "#fff";
  ctxOpticalPowerCanvas.stroke();
}

function draw_All_IP_OP_broadAndSel() {
  //WSS_RX
  draw_IP_OP(360, 440, 355, 445, 355, 435);
  //PA_IN
  draw_IP_OP(240, 440, 235, 435, 235, 445);
  //PA_SUPY
  draw_IP_OP(169, 490, 174, 485, 164, 485);
  //BA_SUP
  draw_IP_OP(169, 620, 174, 615, 164, 615);
  //BA_IN
  draw_IP_OP(270, 570, 275, 565, 275, 575);

  //WSS_TO_DEMUX
  draw_IP_OP(475, 390, 485, 390, 480, 385);
  //WSS_TO_FIELD
  draw_IP_OP(650, 465, 650, 475, 655, 470);

  //MUX_TO_WSS
  draw_IP_OP(380, 550, 385, 545, 385, 555);
  //FIELD_TO_WSS
  draw_IP_OP(645, 590, 650, 595, 650, 585);
  //TPN_10G_OUT
  draw_IP_OP(480, 55, 475, 60, 485, 60);
  //TPN_10G_IN
  draw_IP_OP(680, 147, 675, 142, 685, 142);
  //DEMUX_TO_TPN
  draw_IP_OP(480, 173, 475, 178, 485, 178);
  //TPN_TO_MUX
  draw_IP_OP(680, 267, 675, 262, 685, 262);
}

function draw_All_Circes_broadAndSel() {
  draw_Circle(480, 100, 60, 30, dummyJsonOptical.ConstantValues.MpnLaunchPower);
  draw_Circle(680, 100, 60, 30, dummyJsonOptical.ConstantValues.MpnLaunchPower);

  draw_Circle(
    480,
    210,
    60,
    30,
    dummyJsonOptical.OpticalOutput.BroadAndSel_Roadm.Rx_DEMUX_Out
  );
  draw_Circle(
    680,
    210,
    60,
    30,
    dummyJsonOptical.OpticalOutput.BroadAndSel_Roadm.Tx_MUX_In
  );

  draw_Circle(
    425,
    420,
    50,
    30,
    dummyJsonOptical.OpticalOutput.BroadAndSel_Roadm.Rx_WSS_Out
  );
  draw_Circle(
    425,
    550,
    50,
    30,
    dummyJsonOptical.OpticalOutput.BroadAndSel_Roadm.Tx_MUX_Out
  );
  draw_Circle(
    300,
    440,
    40,
    30,
    dummyJsonOptical.OpticalOutput.BroadAndSel_Roadm.Rx_PA_Out
  );
  draw_Circle(
    310,
    570,
    40,
    30,
    dummyJsonOptical.OpticalOutput.BroadAndSel_Roadm.Tx_WSS_Out
  );

  draw_Circle(
    135,
    440,
    60,
    40,
    dummyJsonOptical.OpticalOutput.BroadAndSel_Roadm.Rx_PA_In
  );
  draw_Circle(
    135,
    570,
    60,
    40,
    dummyJsonOptical.OpticalOutput.BroadAndSel_Roadm.Tx_BA_Out
  );
}

function write_Text_broadAndSel() {
  console.log("currentNodeInfoObject", currentNodeInfoObject);
  var wavelength = window.currentNodeInfoObject.Wavelength;
  var spanLoss = window.currentNodeInfoObject.SpanLoss;
  var numOfMpn = window.currentNodeInfoObject.NumOfMpns;
  ctxOpticalPowerCanvas.fillStyle = "#102c49";
  ctxOpticalPowerCanvas.fill();
  ctxOpticalPowerCanvas.font = "  12pt  Arial ";
  ctxOpticalPowerCanvas.fillText("W", 365, 420);
  ctxOpticalPowerCanvas.fillText("S", 365, 450);
  ctxOpticalPowerCanvas.fillText("S", 365, 480);
  ctxOpticalPowerCanvas.fillText("W", 365, 550);
  ctxOpticalPowerCanvas.fillText("S", 365, 580);
  ctxOpticalPowerCanvas.fillText("S", 365, 610);
  ctxOpticalPowerCanvas.fillText("DEMUX / MUX TRAY", 530, 285);

  if (numOfMpn == 0) {
    ctxOpticalPowerCanvas.fillText("No MPN/TPN", 430, 165);
  } else if (numOfMpn == 1)
    ctxOpticalPowerCanvas.fillText("1 MPN/TPN", 430, 165);
  else {
    //ctxOpticalPowerCanvas.fillText("MPN1..."+"MPN"+numOfMpn,430,165);
    ctxOpticalPowerCanvas.fillText(numOfMpn + " MPN/TPN", 430, 165);
  }

  if (numOfMpn == 0) {
    ctxOpticalPowerCanvas.fillText("No MPN/TPN", 630, 165);
  } else if (numOfMpn == 1)
    ctxOpticalPowerCanvas.fillText("1 MPN/TPN", 630, 165);
  else {
    ctxOpticalPowerCanvas.fillText(numOfMpn + " MPN/TPN", 630, 165);
  }
  //

  ctxOpticalPowerCanvas.fillText("Unit=dBm", 75, 175);
  ctxOpticalPowerCanvas.fillText("No of λ in Link= " + wavelength, 75, 200);
  ctxOpticalPowerCanvas.fillText("MPN Launch Power=+1dBm", 75, 225);
  ctxOpticalPowerCanvas.fillText("Span Loss=" + spanLoss, 75, 250);
  ctxOpticalPowerCanvas.fillText(
    "No Of Add/Drop Channel =" + numOfMpn,
    75,
    275
  );
  //
  ctxOpticalPowerCanvas.font = "  10pt  Arial ";
  ctxOpticalPowerCanvas.fillText(
    "To other WSS" +
      "( λ= " +
      dummyJsonOptical.OpticalOutput.CDC_Roadm.WSS_To_WSS +
      ")",
    480,
    465
  );
  ctxOpticalPowerCanvas.fillText(
    "From other WSS" +
      "( λ= " +
      dummyJsonOptical.OpticalOutput.CDC_Roadm.WSS_from_WSS +
      ")",
    480,
    585
  );
}

// Functions for drawing Ila (Optical power)
function drawAll_Ila() {
  ctxOpticalPowerCanvas.clearRect(
    0,
    0,
    OpticalPowerCanvas.width,
    OpticalPowerCanvas.height
  );
  console.log("draw all");
  drawBack_Ila();
  draw_Network_Ila();
  draw_OCM_Ila();
  drawAll_ILA_Ila(310, 80, 335, 110, 310, 140);
  drawAll_ILA_Ila(310, 310, 335, 280, 335, 340);
  drawAllSUPY_Ila();
  draw_All_IP_OP_Ila();
  write_Text_Ila(120, 520, 170, 170);
  draw_All_Circes_Ila();
  draw_rect_Ila();
  console.log("draw all done");
}
function draw_All_Circes_Ila() {
  draw_Circle(65, 110, 60, 35, dummyJsonOptical.OpticalOutput.ILA.SUPY_In); // left
  // most
  // circle
  draw_Circle(65, 310, 60, 35, dummyJsonOptical.OpticalOutput.ILA.SUPY_Out); // left
  // most
  // circlr
  draw_Circle(590, 110, 60, 35, dummyJsonOptical.OpticalOutput.ILA.SUPY_Out); // right
  // most
  // up
  draw_Circle(590, 310, 60, 35, dummyJsonOptical.OpticalOutput.ILA.SUPY_In); // right
  // most
  // down
  draw_Circle(240, 110, 60, 35, dummyJsonOptical.OpticalOutput.ILA.ILA_In); // 2 up
  draw_Circle(240, 310, 60, 35, dummyJsonOptical.OpticalOutput.ILA.ILA_Out); // 2
  // down
  draw_Circle(430, 110, 60, 35, dummyJsonOptical.OpticalOutput.ILA.ILA_Out); // 3 up
  draw_Circle(430, 310, 60, 35, dummyJsonOptical.OpticalOutput.ILA.ILA_In); // 3
  // down
}
function draw_All_IP_OP_Ila() {
  draw_IP_OP(100, 110, 95, 105, 95, 115); // 1
  draw_IP_OP(635, 110, 630, 105, 630, 115); // 2
  draw_IP_OP(335, 178, 340, 172, 330, 172); // 3
  draw_IP_OP(310, 232, 305, 237, 315, 237); // 4
  draw_IP_OP(550, 310, 555, 305, 555, 315); // 5
  draw_IP_OP(25, 310, 30, 305, 30, 315); // 6
}
function drawAllSUPY_Ila() {
  drawSUPY(100, 100, 45, 172, 70, 172, 105, 115);
  drawSUPY(500, 100, 370, 172, 395, 172, 505, 115);
  drawSUPY(100, 300, 45, 172, 70, 172, 105, 315);
  drawSUPY(500, 300, 370, 172, 395, 172, 505, 315);
}

function drawBack_Ila() {
  console.log("draw back");

  ctxOpticalPowerCanvas.fillStyle = "#fffcfc";
  ctxOpticalPowerCanvas.beginPath();
  ctxOpticalPowerCanvas.rect(5, 5, 790, 640);
  ctxOpticalPowerCanvas.closePath();
  ctxOpticalPowerCanvas.shadowBlur = 1;
  ctxOpticalPowerCanvas.shadowColor = "black";
  ctxOpticalPowerCanvas.fill();
  ctxOpticalPowerCanvas.lineWidth = 2;
  ctxOpticalPowerCanvas.strokeStyle = "#333";
  ctxOpticalPowerCanvas.stroke();
  ctxOpticalPowerCanvas.fillStyle = "black";
  ctxOpticalPowerCanvas.font = "30px Arial";
  // ctxOpticalPowerCanvas.fillText("Optical Power (CDC-ROADM)",210,4
  ctxOpticalPowerCanvas.fillText(" Optical Power Of ILA", 170, 40);
}

function draw_Network_Ila() {
  ctxOpticalPowerCanvas.beginPath();
  ctxOpticalPowerCanvas.moveTo(25, 110);
  ctxOpticalPowerCanvas.lineTo(100, 110);

  ctxOpticalPowerCanvas.moveTo(150, 110);
  ctxOpticalPowerCanvas.lineTo(310, 110);

  ctxOpticalPowerCanvas.moveTo(335, 110);
  ctxOpticalPowerCanvas.lineTo(510, 110);
  ctxOpticalPowerCanvas.moveTo(335, 110);
  ctxOpticalPowerCanvas.lineTo(335, 170);

  ctxOpticalPowerCanvas.moveTo(550, 110);
  ctxOpticalPowerCanvas.lineTo(635, 110);

  //
  ctxOpticalPowerCanvas.moveTo(25, 310);
  ctxOpticalPowerCanvas.lineTo(100, 310);

  ctxOpticalPowerCanvas.moveTo(150, 310);
  ctxOpticalPowerCanvas.lineTo(310, 310);
  ctxOpticalPowerCanvas.moveTo(310, 310);
  ctxOpticalPowerCanvas.lineTo(310, 235);

  ctxOpticalPowerCanvas.moveTo(335, 310);
  ctxOpticalPowerCanvas.lineTo(510, 310);

  ctxOpticalPowerCanvas.moveTo(550, 310);
  ctxOpticalPowerCanvas.lineTo(635, 310);

  ctxOpticalPowerCanvas.lineWidth = 3;
  ctxOpticalPowerCanvas.strokeStyle = "#fff";
  ctxOpticalPowerCanvas.stroke();
}
function draw_OCM_Ila() {
  ctxOpticalPowerCanvas.beginPath();
  ctxOpticalPowerCanvas.rect(300, 180, 50, 50);
  ctxOpticalPowerCanvas.fillStyle = "#C8C8C8";
  ctxOpticalPowerCanvas.fill();

  ctxOpticalPowerCanvas.lineWidth = 1;
  ctxOpticalPowerCanvas.strokeStyle = "black";
  ctxOpticalPowerCanvas.fillStyle = "black";

  // text
  ctxOpticalPowerCanvas.font = "15px Arial";
  ctxOpticalPowerCanvas.fillText("OCM", 310, 200);
}
function drawAll_ILA_Ila(x1, y1, x2, y2, x3, y3) {
  // the ILA 1
  ctxOpticalPowerCanvas.beginPath();
  ctxOpticalPowerCanvas.moveTo(x1, y1);
  ctxOpticalPowerCanvas.lineTo(x2, y2);
  ctxOpticalPowerCanvas.lineTo(x3, y3);
  ctxOpticalPowerCanvas.closePath();

  // the outline

  ctxOpticalPowerCanvas.lineWidth = 1;
  ctxOpticalPowerCanvas.strokeStyle = "#666666";
  ctxOpticalPowerCanvas.stroke();

  // the fill color
  ctxOpticalPowerCanvas.fillStyle = "#C8C8C8";
  ctxOpticalPowerCanvas.fill();
}
function draw_IP_OP_Ila(x1, y1, x2, y2, x3, y3) {
  ctxOpticalPowerCanvas.beginPath();
  ctxOpticalPowerCanvas.moveTo(x1, y1);
  ctxOpticalPowerCanvas.lineTo(x2, y2);
  ctxOpticalPowerCanvas.lineTo(x3, y3);
  ctxOpticalPowerCanvas.closePath();

  // the outline

  ctxOpticalPowerCanvas.lineWidth = 1;
  ctxOpticalPowerCanvas.strokeStyle = "#666666";
  ctxOpticalPowerCanvas.stroke();

  // the fill color
  ctxOpticalPowerCanvas.fillStyle = "black";
  ctxOpticalPowerCanvas.fill();
}
function write_Text_Ila(EAST, WEST, y, y1) {
  ctxOpticalPowerCanvas.fillStyle = "black";
  ctxOpticalPowerCanvas.fill();
  ctxOpticalPowerCanvas.font = " bold 15pt  Times New Roman ";

  for (i in JSONObj_EAST) {
    ctxOpticalPowerCanvas.fillText(JSONObj_EAST[i], EAST, y);
    y = y + 30;
  }
  for (i in JSONObj_WEST) {
    ctxOpticalPowerCanvas.fillText(JSONObj_WEST[i], WEST, y1);
    y1 = y1 + 30;
  }
}
function draw_Circle_Ila(cx, cy, width, height, ct) {
  ctxOpticalPowerCanvas.beginPath();
  ctxOpticalPowerCanvas.fillStyle = "fff";
  ctxOpticalPowerCanvas.fill();
  ctxOpticalPowerCanvas.moveTo(cx, cy - height / 2); // A1

  ctxOpticalPowerCanvas.bezierCurveTo(
    cx + width / 2,
    cy - height / 2, // C1
    cx + width / 2,
    cy + height / 2, // C2
    cx,
    cy + height / 2
  ); // A2

  ctxOpticalPowerCanvas.bezierCurveTo(
    cx - width / 2,
    cy + height / 2, // C3
    cx - width / 2,
    cy - height / 2, // C4
    cx,
    cy - height / 2
  ); // A1
  ctxOpticalPowerCanvas.lineWidth = 3;
  ctxOpticalPowerCanvas.strokeStyle = "#3c763d";
  ctxOpticalPowerCanvas.fillStyle = "#fff";
  ctxOpticalPowerCanvas.fill();
  ctxOpticalPowerCanvas.stroke();
  // style for values
  ctxOpticalPowerCanvas.fillStyle = "black";
  ctxOpticalPowerCanvas.font = " bold 13pt  Times New Roman ";
  ctxOpticalPowerCanvas.fillText(ct, cx - 17, cy + 5);
}
function draw_Port_Ila(x, y) {
  ctxOpticalPowerCanvas.beginPath();
  ctxOpticalPowerCanvas.arc(x, y, 2, 0, 2 * Math.PI);
  ctxOpticalPowerCanvas.stroke();
  ctxOpticalPowerCanvas.fillStyle = "black";
  ctxOpticalPowerCanvas.fill();
}
function drawSUPY_Ila(x, y, a, b, a1, b1, supy, height) {
  ctxOpticalPowerCanvas.beginPath();
  ctxOpticalPowerCanvas.rect(x, y, 50, 20);
  ctxOpticalPowerCanvas.fillStyle = "#C8C8C8";

  ctxOpticalPowerCanvas.fill();
  ctxOpticalPowerCanvas.lineWidth = 1;
  ctxOpticalPowerCanvas.strokeStyle = "black";
  ctxOpticalPowerCanvas.fillStyle = "black";
  ctxOpticalPowerCanvas.stroke();

  // text
  ctxOpticalPowerCanvas.font = "12px Arial";
  ctxOpticalPowerCanvas.fillText("SUPY", supy, height);
}

function draw_rect_Ila() {
  ctxOpticalPowerCanvas.beginPath();
  ctxOpticalPowerCanvas.rect(200, 400, 300, 100);
  ctxOpticalPowerCanvas.fillStyle = "#C8C8C8";

  ctxOpticalPowerCanvas.fill();
  ctxOpticalPowerCanvas.lineWidth = 1;
  ctxOpticalPowerCanvas.strokeStyle = "black";
  ctxOpticalPowerCanvas.fillStyle = "black";
  ctxOpticalPowerCanvas.stroke();

  // text
  ctxOpticalPowerCanvas.font = "18px Arial";
  ctxOpticalPowerCanvas.fillText(
    "No of channels in link = " +
      dummyJsonOptical.OpticalOutput.ILA.Constants.no_of_channel_link,
    220,
    430
  );
  ctxOpticalPowerCanvas.fillText(
    "dBm = " + dummyJsonOptical.OpticalOutput.ILA.Constants.dBm + " unit",
    220,
    460
  );
}

function drawAll_TE() {
  console.log("draw all");
  drawBack_TE();
  drawSUPY_TE(100, 250, 6, 8);
  drawSUPY_TE(100, 400, 6, 8);
  draw_All_rect_TE();
  draw_Network_TE();
  MUX_DEMUX_TE();
  draw_All_IP_OP_TE();
  draw_All_circle_TE();
  draw_ILA_TE(200, 120, 200, 180, 230, 150); // ILA up
  draw_ILA_TE(200, 320, 230, 290, 230, 350); // ILA down
  draw_All_rect_TE();
  write_Text_TE();

  console.log("draw all done");
}
function draw_All_circle_TE() {
  // up
  draw_Circle(90, 150, 45, 30, dummyJsonOptical.OpticalOutput.TE.PA_In); // ILA_INPUT
  draw_Circle(280, 150, 45, 30, dummyJsonOptical.OpticalOutput.TE.PA_Out); // ILA_output
  draw_Circle(410, 150, 45, 30, dummyJsonOptical.OpticalOutput.TE.WSS_Out); // WSS_output
  draw_Circle(550, 150, 45, 30, dummyJsonOptical.OpticalOutput.TE.DEMUX_Out); // DEMUX_output
  // down
  draw_Circle(90, 320, 45, 30, dummyJsonOptical.OpticalOutput.TE.BA_Out); // MUX_input
  draw_Circle(280, 320, 45, 30, dummyJsonOptical.OpticalOutput.TE.WSS_Out_R); // MUX_output
  draw_Circle(410, 320, 45, 30, dummyJsonOptical.OpticalOutput.TE.MUX_Out); // WSS_output
  draw_Circle(550, 320, 45, 30, dummyJsonOptical.OpticalOutput.TE.MUX_In); // ILA_output
}
function draw_All_rect_TE() {
  draw_rect(335, 100, 30, 100); // wss1
  draw_rect(335, 270, 30, 100); // wss down
  draw_rect(610, 135, 130, 30); // Npn up
  draw_rect(610, 305, 130, 30); // npn down
  draw_rect(70, 450, 270, 150); // info
}
function draw_All_IP_OP_TE() {
  draw_IP_OP(195, 145, 200, 150, 195, 155); // 3
  draw_IP_OP(130, 250, 125, 245, 135, 245); // 2
  draw_IP_OP(130, 400, 125, 395, 135, 395); // 2 down
  draw_IP_OP(330, 145, 335, 150, 330, 155); // 3
  draw_IP_OP(465, 145, 470, 150, 465, 155); // 4
  draw_IP_OP(605, 145, 610, 150, 605, 155); // 5
  draw_IP_OP(780, 145, 785, 150, 780, 155); // 6
  draw_IP_OP(45, 320, 50, 325, 50, 315); // 1 down
  draw_IP_OP(230, 320, 235, 325, 235, 315); // 3 down
  draw_IP_OP(365, 320, 370, 325, 370, 315); // 4 down
  draw_IP_OP(510, 320, 515, 325, 515, 315); // 5 down
  draw_IP_OP(740, 320, 745, 325, 745, 315); // 6 down
}
function drawBack_TE() {
  console.log("draw back");
  ctxOpticalPowerCanvas.fillStyle = "#fffcfc";
  ctxOpticalPowerCanvas.beginPath();
  ctxOpticalPowerCanvas.rect(5, 5, 790, 640);
  ctxOpticalPowerCanvas.closePath();
  ctxOpticalPowerCanvas.shadowBlur = 1;
  ctxOpticalPowerCanvas.shadowColor = "black";
  ctxOpticalPowerCanvas.fill();
  ctxOpticalPowerCanvas.lineWidth = 2;
  ctxOpticalPowerCanvas.strokeStyle = "#333";
  ctxOpticalPowerCanvas.stroke();
  ctxOpticalPowerCanvas.fillStyle = "black";
  ctxOpticalPowerCanvas.font = "30px Arial";
  ctxOpticalPowerCanvas.fillText("Optical Power (TE)", 210, 40);
}
/**
 * function draw supy
 */
function drawSUPY_TE(x, y, supy, height) {
  ctxOpticalPowerCanvas.beginPath();
  ctxOpticalPowerCanvas.rect(x, y, 60, 20);
  ctxOpticalPowerCanvas.fillStyle = "#ffffff";

  ctxOpticalPowerCanvas.fill();
  ctxOpticalPowerCanvas.lineWidth = 1;
  ctxOpticalPowerCanvas.strokeStyle = "black";
  ctxOpticalPowerCanvas.fillStyle = "black";
  ctxOpticalPowerCanvas.stroke();
  // text
  ctxOpticalPowerCanvas.font = "12px Arial";
  ctxOpticalPowerCanvas.fillText("SUPY", x + 10, y + 15);
}
/**
 * function draw_ILA
 */
function draw_ILA_TE(x1, y1, x2, y2, x3, y3) {
  ctxOpticalPowerCanvas.beginPath();
  ctxOpticalPowerCanvas.moveTo(x1, y1);
  ctxOpticalPowerCanvas.lineTo(x2, y2);
  ctxOpticalPowerCanvas.lineTo(x3, y3);
  ctxOpticalPowerCanvas.closePath();

  // the outline

  ctxOpticalPowerCanvas.lineWidth = 1;
  ctxOpticalPowerCanvas.strokeStyle = "#666666";
  ctxOpticalPowerCanvas.stroke();

  // the fill color
  ctxOpticalPowerCanvas.fillStyle = "black";
}
function draw_Network_TE() {
  console.log("draw networks");
  ctxOpticalPowerCanvas.beginPath();
  // 1
  ctxOpticalPowerCanvas.moveTo(50, 150);
  ctxOpticalPowerCanvas.lineTo(200, 150);
  // 1 down
  ctxOpticalPowerCanvas.moveTo(50, 320);
  ctxOpticalPowerCanvas.lineTo(200, 320);
  // 2
  ctxOpticalPowerCanvas.moveTo(130, 150);
  ctxOpticalPowerCanvas.lineTo(130, 250);
  // 2 down
  ctxOpticalPowerCanvas.moveTo(130, 320);
  ctxOpticalPowerCanvas.lineTo(130, 400);
  // 3
  ctxOpticalPowerCanvas.moveTo(230, 150);
  ctxOpticalPowerCanvas.lineTo(330, 150);
  // 3 down
  ctxOpticalPowerCanvas.moveTo(230, 320);
  ctxOpticalPowerCanvas.lineTo(335, 320);
  // 4
  ctxOpticalPowerCanvas.moveTo(365, 150);
  ctxOpticalPowerCanvas.lineTo(465, 150);
  // 4 down
  ctxOpticalPowerCanvas.moveTo(365, 320);
  ctxOpticalPowerCanvas.lineTo(470, 320);
  // 5
  ctxOpticalPowerCanvas.moveTo(510, 150); //
  ctxOpticalPowerCanvas.lineTo(610, 150); //
  // 5 down
  ctxOpticalPowerCanvas.moveTo(510, 320); //
  ctxOpticalPowerCanvas.lineTo(610, 320); //
  // 6
  ctxOpticalPowerCanvas.moveTo(740, 150); //
  ctxOpticalPowerCanvas.lineTo(780, 150); //
  // 6 down
  ctxOpticalPowerCanvas.moveTo(740, 320); //
  ctxOpticalPowerCanvas.lineTo(780, 320); //

  // DeMUX
  /*
   * ctxOpticalPowerCanvas.moveTo(470, 95);//
   * ctxOpticalPowerCanvas.lineTo(470, 210);//
   * ctxOpticalPowerCanvas.lineTo(510, 190);//
   * ctxOpticalPowerCanvas.lineTo(510, 120);//
   * ctxOpticalPowerCanvas.lineTo(470, 95);//
   *
   * //MUX ctxOpticalPowerCanvas.moveTo(470, 370);//
   * ctxOpticalPowerCanvas.lineTo(470, 430);//
   * ctxOpticalPowerCanvas.lineTo(510, 450);//
   * ctxOpticalPowerCanvas.lineTo(510, 350);//
   * ctxOpticalPowerCanvas.lineTo(470, 370);//
   */

  ctxOpticalPowerCanvas.lineWidth = 2;
  ctxOpticalPowerCanvas.strokeStyle = "#fff";
  ctxOpticalPowerCanvas.stroke();
}
function draw_rect_TE(x, y, width, height) {
  ctxOpticalPowerCanvas.beginPath();
  ctxOpticalPowerCanvas.rect(x, y, width, height);
  ctxOpticalPowerCanvas.fillStyle = "#ffffff";
  ctxOpticalPowerCanvas.fill();
  ctxOpticalPowerCanvas.lineWidth = 0.5;
  ctxOpticalPowerCanvas.strokeStyle = "black";
  ctxOpticalPowerCanvas.fillStyle = "black";
  ctxOpticalPowerCanvas.stroke();
}
function draw_IP_OP_TE(x1, y1, x2, y2, x3, y3) {
  ctxOpticalPowerCanvas.beginPath();
  ctxOpticalPowerCanvas.moveTo(x1, y1);
  ctxOpticalPowerCanvas.lineTo(x2, y2);
  ctxOpticalPowerCanvas.lineTo(x3, y3);
  ctxOpticalPowerCanvas.closePath();

  // the outline

  ctxOpticalPowerCanvas.lineWidth = 1;
  ctxOpticalPowerCanvas.strokeStyle = "#666666";
  ctxOpticalPowerCanvas.stroke();

  // the fill color
  ctxOpticalPowerCanvas.fillStyle = "green";
  ctxOpticalPowerCanvas.fill();
}

function write_Text_TE() {
  var wavelength = window.currentNodeInfoObject.Wavelength;
  var spanLoss = window.currentNodeInfoObject.SpanLoss;
  var numOfMpn = window.currentNodeInfoObject.NumOfMpns;
  ctxOpticalPowerCanvas.fillStyle = "#102c49";
  // ctxOpticalPowerCanvas .fill();
  ctxOpticalPowerCanvas.font = "  12pt  Arial ";
  ctxOpticalPowerCanvas.fillText("W", 340, 140);
  ctxOpticalPowerCanvas.fillText("S", 340, 160);
  ctxOpticalPowerCanvas.fillText("S", 340, 180);
  ctxOpticalPowerCanvas.fillText("W", 340, 310);
  ctxOpticalPowerCanvas.fillText("S", 340, 330);
  ctxOpticalPowerCanvas.fillText("S", 340, 350);

  ctxOpticalPowerCanvas.fillText("D", 480, 120);
  ctxOpticalPowerCanvas.fillText("E", 480, 140);
  ctxOpticalPowerCanvas.fillText("M", 480, 160);
  ctxOpticalPowerCanvas.fillText("U", 480, 180);
  ctxOpticalPowerCanvas.fillText("X", 480, 200);

  ctxOpticalPowerCanvas.fillText("M", 480, 310);
  ctxOpticalPowerCanvas.fillText("U", 480, 330);
  ctxOpticalPowerCanvas.fillText("X", 480, 350);

  ctxOpticalPowerCanvas.fillText("MPN", 650, 155);
  ctxOpticalPowerCanvas.fillText("MPN", 650, 325);
  /*
   * if(numOfMpn==1) ctxOpticalPowerCanvas.fillText("λ1",650,155);
   * else {
   * ctxOpticalPowerCanvas.fillText("MPN1..."+"MPN"+numOfMpn,600,155); }
   * if(numOfMpn==1) ctxOpticalPowerCanvas.fillText("λ1",650,325);
   * else {
   * ctxOpticalPowerCanvas.fillText("MPN1..."+"MPN"+numOfMpn,630,325); }
   */
  // info
  /*
   * ctxOpticalPowerCanvas.fillText("Unit=dBm",105,480);
   * ctxOpticalPowerCanvas.fillText("No of λ in Link= ",105,505);
   * ctxOpticalPowerCanvas.fillText("MPN Launch Power=+1dBm",105,530);
   * ctxOpticalPowerCanvas.fillText("Span Loss=",105,555);
   * ctxOpticalPowerCanvas.fillText("No Of Add/Drop Channel
   * =",105,580)
   */

  ctxOpticalPowerCanvas.fillText("Unit=dBm", 105, 480);
  ctxOpticalPowerCanvas.fillText("No of λ in Link= " + wavelength, 105, 505);
  ctxOpticalPowerCanvas.fillText("MPN Launch Power=+1dBm", 105, 530);
  ctxOpticalPowerCanvas.fillText("Span Loss=" + spanLoss, 105, 555);
  ctxOpticalPowerCanvas.fillText(
    "No Of Add/Drop Channel =" + numOfMpn,
    105,
    580
  );
  //
  /*
   * ctxOpticalPowerCanvas.font = " 10pt Arial ";
   * ctxOpticalPowerCanvas.fillText("To other WSS"+ "( λ= " +
   * dummyJsonOptical.OpticalOutput.CDC_Roadm.WSS_To_WSS+
   * ")",480,465); ctxOpticalPowerCanvas.fillText("From other
   * WSS" + "( λ= " +
   * dummyJsonOptical.OpticalOutput.CDC_Roadm.WSS_from_WSS+
   * ")",480,585);
   */
}
function MUX_DEMUX_TE() {
  ctxOpticalPowerCanvas.beginPath();
  ctxOpticalPowerCanvas.moveTo(470, 95); //
  ctxOpticalPowerCanvas.lineTo(470, 210); //
  ctxOpticalPowerCanvas.lineTo(510, 190); //
  ctxOpticalPowerCanvas.lineTo(510, 120); //
  ctxOpticalPowerCanvas.lineTo(470, 95); //

  ctxOpticalPowerCanvas.moveTo(470, 290); //
  ctxOpticalPowerCanvas.lineTo(470, 350); //
  ctxOpticalPowerCanvas.lineTo(510, 370); //
  ctxOpticalPowerCanvas.lineTo(510, 270); //
  ctxOpticalPowerCanvas.lineTo(470, 290); //

  ctxOpticalPowerCanvas.lineWidth = 1;
  ctxOpticalPowerCanvas.strokeStyle = "#fff";
  ctxOpticalPowerCanvas.stroke();
}

function draw_Circle_TE(cx, cy, width, height, ct) {
  ctxOpticalPowerCanvas.beginPath();
  ctxOpticalPowerCanvas.moveTo(cx, cy - height / 2); // A1

  ctxOpticalPowerCanvas.bezierCurveTo(
    cx + width / 2,
    cy - height / 2, // C1
    cx + width / 2,
    cy + height / 2, // C2
    cx,
    cy + height / 2
  ); // A2

  ctxOpticalPowerCanvas.bezierCurveTo(
    cx - width / 2,
    cy + height / 2, // C3
    cx - width / 2,
    cy - height / 2, // C4
    cx,
    cy - height / 2
  ); // A1
  ctxOpticalPowerCanvas.lineWidth = 3;
  ctxOpticalPowerCanvas.strokeStyle = "#5db777";
  ctxOpticalPowerCanvas.fillStyle = "#5db777";
  ctxOpticalPowerCanvas.stroke();
  // style for values
  ctxOpticalPowerCanvas.fillStyle = "black";
  ctxOpticalPowerCanvas.font = " bold 13pt  Times New Roman ";
  ctxOpticalPowerCanvas.fillText(ct, cx - 10, cy + 35);
} /** ************************************************************************ */ /** Node Id change trigger for bom data * */ /** ************************************************************************ */

// initializeOpticalPowerCalculations();

/*
 * $("#viewOpticalPowerPerDemand").click(function() { //console.log()
 * $('#cablingModal').modal({ backdrop: 'static', keyboard: false });
 *
 * var data=dummyJsonOptical.DemandMatrix; var demandIdSet=new Set();
 *
 * for(var i=0;i<data.length;i++) { demandIdSet.add(data[i].DemandId); }
 *
 * console.log("demandIdSet") console.log(demandIdSet) var
 * demandIdSetArr=Array.from(demandIdSet);
 *
 * var formInput=`<div class="form-group"> <label
 * for="opticalDemandId">Select Demand Id</label> <select
 * class="form-control" id="opticalDemandId"> <option value="0">All</option>
 * </select> </div> <div class="form-group"> <button class="btn"
 * type="button" id="opticalPowerPerDemandFormFilterTrigger">Submit</button>
 * </div>`;
 *
 * $("#cablingViewMainHeading").html('OPTICAL POWER PER DEMAND');
 *
 * $("#cablingInputForm").empty().append(formInput);
 *
 * $("#opticalDemandId").empty(); for(var i=0;i<demandIdSetArr.length;i++)
 * $("#opticalDemandId").append(`<option
 * value="${demandIdSetArr[i]}">${demandIdSetArr[i]}</option>`);
 *
 * var header=`<tr> <th>Wavelength No</th> <th>Route Priority</th>
 * <th>Path</th> <th>Node Id</th> <th>Input</th> <th>Output</th>
 * </tr>`; $(".modalBodyCablingContentPanelBomTable
 * thead").empty().append(header);
 * fGetOpticalPowerCalculationDataForDemand(demandIdSetArr[0]); });
 *
 *
 *
 * var fGetOpticalPowerCalculationDataForDemand=function(demandId) { var
 * data=dummyJsonOptical.DemandMatrix; console.log(data); //var
 * demandData={}; var str,path,pathArr,className="workingOpticalPower";
 * for(var i=0;i<data.length;i++) { if(data[i].DemandId==demandId) {
 * path=data[i].Path; pathArr=path.split(','); console.log(pathArr);
 * rowSpan=pathArr.length; console.log(rowSpan); str+=`<tr class="${className}">
 * <td rowspan="${rowSpan}">${data[i].WavelengthNo}</td>
 * <td rowspan="${rowSpan}">${window.routePriority[data[i].Routepriority]}</td>
 * <td rowspan="${rowSpan}">${data[i].Path}</td> <td>${pathArr[0]}</td>
 * <td>${pathArr[0]}</td> <td>0</td></tr>`; for(var j=1;j<pathArr.length;j++) {
 * str+=`<tr class="${className}"><td>${pathArr[j]}</td> <td>${pathArr[j]}</td>
 * <td>0</td></tr>`; }
 *
 * if(className=='workingOpticalPower')
 * className='protectionOpticalPower'; else
 * if(className=='protectionOpticalPower')
 * className='restorationOpticalPower';
 *  }
 *  } $(".modalBodyCablingContentPanelBomTable
 * tbody").empty().append(str);
 * $("#modalBodyCablingContentPanelBom").slideDown(); }
 *
 */
/*
 */
/*
 */
/*
 * $("body"
 * ).delegate("#opticalPowerPerDemandFormFilterTrigger","click",function(){
 *
 * var
 * demandId=$("#opticalDemandId").val();
 *
 * $("#modalBodyCablingContentPanelBom").slideUp();
 * //var
 * filteredDataArr=fGetFilteredCablingDataFromJson(nodeId,cardType);
 *
 * fGetOpticalPowerCalculationDataForDemand(demandId);
 *
 * console.log("demandId
 * ::"+demandId);
 *
 * });
 */
