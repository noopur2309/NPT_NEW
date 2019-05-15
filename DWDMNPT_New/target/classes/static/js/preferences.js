/************************************************************************
 * Module: Preferences
 * Functionality: This module will be used for taking Inventory, Parametric
 * 				  preference, along with Inventory Availibility/Faults
 * Author: Avinash
 *************************************************************************/

(function($) {
  "use strict";
  //initializeTabs();

  /************************************************************************
   * Tab Li tags
   ************************************************************************/

  const InventoryPrefStr = "Inventory Preference";
  const ParametricPrefStr = "Parametric Preference";
  const AvailableInventoryStr = "Available Inventory";
  const ResourceAllocationExceptionStr = "Resource Allocation Exception";
  // const parentSelector = ".preference-overlay-container";
  const parentSelector = ".body-overlay";

  var nodeArr = [
    { value: 1, text: "1" },
    { value: 2, text: "2" },
    { value: 3, text: "3" }
  ];

  const parameterArr = [
    { value: 0, text: "Demand(W)" },
    { value: 1, text: "Demand(P)" },
    { value: 2, text: "Direction" },
    { value: 3, text: "Circuit" },
    { value: 4, text: "Rack-Subrack" }
  ];

  var valueArr = [
    { value: 0, text: "none" },
    { value: 1, text: "1" },
    { value: 2, text: "2" },
    { value: 3, text: "3" },
    { value: 4, text: "4" },
    { value: 5, text: "5" },
    { value: 6, text: "6" },
    { value: 7, text: "7" },
    { value: 8, text: "8" },
    { value: 9, text: "9" }
  ];

  const categoryArr = [
    { value: 1, text: "MPN100G" },
    { value: 2, text: "MPN10G" },
    { value: 3, text: "TPN100G" },
    { value: 4, text: "TPN10G" },
    { value: 5, text: "WSS" },
    { value: 6, text: "YCABLE" },
    { value: 7, text: "CSCC" },
    { value: 8, text: "MPC" },
    { value: 9, text: "SFPETH" },
    { value: 10, text: "SFPNONETH" },
    { value: 11, text: "DEGREE" },
    { value: 12, text: "MPN100G-OPX" },
    { value: 13, text: "AMPLIFIER" },
    { value: 14, text: "CHASSIS TYPE" }
  ];

  const cardtypeArr = {
    MPN100G: [{ value: 0, text: "MPN(CGM)" }, { value: 1, text: "MPN(CGX)" }],
    TPN10G: [{ value: 0, text: "TPN 5x10G" }, { value: 1, text: "MPN 10G" }],
    TPN100G: [{ value: 0, text: "TPN(CGC" }],
    MPN10G: [{ value: 0, text: "MPN10G" }],
    WSS: [
      { value: 0, text: "WSS2x1x9" },
      { value: 1, text: "WSS2x1x20" },
      { value: 2, text: "WSS1x2" }
    ],
    YCABLE: [
      { value: 0, text: "YCable1x2Unit" },
      { value: 1, text: "YCable2x2Unit" }
    ],
    CSCC: [{ value: 0, text: "CSCC" }],
    MPC: [{ value: 0, text: "MPC" }],
    SFPETH: [
      { value: 0, text: "SFP ETH 40Km" },
      { value: 1, text: "SFP ETH 80Km" }
    ],
    SFPNONETH: [
      { value: 0, text: "SFP MULTIRATE 40Km" },
      { value: 1, text: "SFP MULTIRATE 80Km" }
    ],
    DEGREE: [
      { value: 0, text: "One Degree Higher" },
      { value: 1, text: "Two Degree Higher" },
      { value: 2, text: "Three Degree Higher" }
    ],
    "MPN100G-OPX": [
      { value: 0, text: "MPN OPX(CGM)" },
      { value: 1, text: "MPN OPX(CGX)" }
    ],
    AMPLIFIER: [{ value: 0, text: "PA/BA" }, { value: 1, text: "RAMAN" }],
    "CHASSIS TYPE": [
      { value: 0, text: "EMERSION" },
      { value: 1, text: "COMTEL" },
      { value: 2, text: "PENTAIR" }
    ]
  };

  const redundancyArr = [
    { value: "No", text: "No" },
    { value: "Yes", text: "Yes" }
  ];
  const RackArr = [
    { value: 1, text: "1" },
    { value: 2, text: "2" },
    { value: 3, text: "3" },
    { value: 4, text: "4" },
    { value: 5, text: "5" }
  ];
  const SubRackArr = [
    { value: 1, text: "1" },
    { value: 2, text: "2" },
    { value: 3, text: "3" }
  ];
  const RackSubRackArr = [
    { value: "1-1", text: "1-1" },
    { value: "1-2", text: "1-2" },
    { value: "1-3", text: "1-3" },
    { value: "2-1", text: "2-1" },
    { value: "2-2", text: "2-2" },
    { value: "2-3", text: "2-3" },
    { value: "3-1", text: "3-1" },
    { value: "3-2", text: "3-2" },
    { value: "3-3", text: "3-3" }
  ];
  const SlotArr = [
    { value: 1, text: "1" },
    { value: 2, text: "2" },
    { value: 3, text: "3" },
    { value: 4, text: "4" },
    { value: 5, text: "5" },
    { value: 6, text: "6" },
    { value: 7, text: "7" },
    { value: 8, text: "8" },
    { value: 9, text: "9" },
    { value: 10, text: "10" },
    { value: 11, text: "11" },
    { value: 12, text: "12" },
    { value: 13, text: "13" },
    { value: 14, text: "14" }
  ];
  const PortArr = [
    { value: 1, text: "1" },
    { value: 2, text: "2" },
    { value: 3, text: "3" },
    { value: 4, text: "4" },
    { value: 5, text: "5" },
    { value: 6, text: "6" },
    { value: 7, text: "7" },
    { value: 8, text: "8" },
    { value: 9, text: "9" },
    { value: 10, text: "10" }
  ];

  const ExceptionTypeArr = [
    { value: "Reservation", text: "Reservation" },
    { value: "Fault", text: "Fault" }
  ];
  const ExceptionCardTypeArr = [{ value: "Reserved", text: "Reserved" }];

  var inventoryprefData = [
    {
      NodeId: 1,
      Category: "MPN",
      CardType: "CGM",
      Redundancy: "Yes",
      Preference: 1
    },
    {
      NodeId: 1,
      Category: "MPN",
      CardType: "CGX",
      Redundancy: "No",
      Preference: 2
    },
    {
      NodeId: 1,
      Category: "MPN",
      CardType: "CGG",
      Redundancy: "Yes",
      Preference: 3
    },
    {
      NodeId: 1,
      Category: "ATCA",
      CardType: "Chassis",
      Redundancy: "Yes",
      Preference: 1
    },
    {
      NodeId: 1,
      Category: "ATCA",
      CardType: "Port",
      Redundancy: "Yes",
      Preference: 2
    },
    {
      NodeId: 2,
      Category: "MPN",
      CardType: "CGM",
      Redundancy: "Yes",
      Preference: 1
    },
    {
      NodeId: 2,
      Category: "MPN",
      CardType: "CGM",
      Redundancy: "Yes",
      Preference: 2
    },
    {
      NodeId: 2,
      Category: "MPN",
      CardType: "CGM",
      Redundancy: "Yes",
      Preference: 3
    }
  ];

  function fGetCardTypeArrforCategory(cat) {
    return cardtypeArr[cat];
  }

  function fGetCategory(cat) {
    for (var i = 0; i < categoryArr.length; i++) {
      if (categoryArr[i].text == cat) return categoryArr[i].value;
    }
  }

  function fGetParameter(param) {
    for (var i = 0; i < parameterArr.length; i++) {
      if (parameterArr[i].text == param) return parameterArr[i].value;
    }
  }

  // Debug
  function fGetParameterText(param) {
    for (var i = 0; i < parameterArr.length; i++) {
      if (parameterArr[i].value == param) return parameterArr[i].text;
    }
  }

  function fGetCardType(ct, cat) {
    let cardtypeValues = cardtypeArr[cat];

    for (var i = 0; i < cardtypeValues.length; i++) {
      if (cardtypeValues[i].text == ct) return cardtypeValues[i].value;
    }
  }

  function fGetExceptionCardType(ct) {
    for (var i = 0; i < ExceptionCardTypeArr.length; i++) {
      if (ExceptionCardTypeArr[i].text == ct)
        return ExceptionCardTypeArr[i].value;
    }
  }

  function fGetExceptionType(type) {
    for (var i = 0; i < ExceptionTypeArr.length; i++) {
      if (ExceptionTypeArr[i].text == type) return ExceptionTypeArr[i].value;
    }
  }

  function fGetParameterValueSrc(nodeId, parameter) {
    console.log("fGetParameterValueSrc---- parameter", parameter);
    if (parameter == "Demand(W)" || parameter == "Demand(P)") {
      return valueArr[nodeId]["Demand"];
    } else if (parameter == "Rack-Subrack") return RackSubRackArr;
    return valueArr[nodeId][parameter];
  }

  /************************************************************************
   * Function to initialize all tabs on modal load
   ************************************************************************/
  function initializeTabs(prefData) {
    const paramPrefDataArr = prefData.ParamPref;
    const stockPrefDataArr = prefData.StockPref;
    const allocationExcDataArr = prefData.AllocationExceptions;

    inventoryprefData = prefData.DefaultPref;
    nodeArr = prefData.Nodes;
    valueArr = prefData.NodeDetails;

    //Debug
    fPopulateInventoryPrefInputs();

    /****************Param Pref Table************************/
    let paramPrefTemplate = "";
    paramPrefDataArr.forEach(function(el) {
      paramPrefTemplate += fGetParamPrefRowTemplateDb();
    });
    $("#parametricPreferenceTable tbody")
      .empty()
      .append($(paramPrefTemplate));

    /****************Stock Pref Table************************/
    let stockPrefTemplate = "";
    stockPrefDataArr.forEach(function(el) {
      stockPrefTemplate += fgetAvailableInventoryRowTemplateDb();
    });

    $("#availableInvTable tbody")
      .empty()
      .append($(stockPrefTemplate));

    /****************Allocation Exception Table************************/
    let allocationExceptionTemplate = "";
    allocationExcDataArr.forEach(function(el) {
      allocationExceptionTemplate += fGetAllocationExcRowTemplateDb();
    });
    $("#invExceptionTable tbody")
      .empty()
      .append($(allocationExceptionTemplate));

    initializeEditablePref();
    // initializeInventoryPrefDataFromDb(inventoryprefData);
    initializeParametricPrefDataFromDb(paramPrefDataArr);
    initializeStockPrefDataFromDb(stockPrefDataArr);
    initializeAllocationExcDataFromDb(allocationExcDataArr);
  }

  /************************************************************************
   * Modal template for dynamic injection on Index.html onLoad
   ************************************************************************/
  var PreferenceModalTemplate = `<div id="preferenceModal" class="modal fade " role="dialog">
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
														PREFERENCES												
													</p>
													<button type="button" class="btn btn--danger pull-right"
															data-dismiss="modal">
															<i class="fa fa-times" aria-hidden="true"></i>
														</button>	
												</div>
											</div>
											<div class="col-md-12 col-sm-12 genericModalTabMenuPanel">
											
											<ul class="nav nav-pills ">
											    <li class="active"><a data-toggle="pill" href="#inventoryPreferences" id="inventoryPreferencesTrgr">${InventoryPrefStr}</a></li>
											    <li><a data-toggle="pill" href="#parametricPreference">${ParametricPrefStr}</a></li>
											    <li><a data-toggle="pill" href="#availableInventory">${AvailableInventoryStr}</a></li>
											    <li><a data-toggle="pill" href="#exceptionPreference">${ResourceAllocationExceptionStr}</a></li> 												
											</ul>

										    </div>
										    
											<div class="col-sm-12 col-md-12 genericModalBodyHeadPanel">
												<div class="row">
													<div class="col-md-8 col-md-offset-2">
														<form class="form-inline npt-form-inline d-flex" >
															<div class="form-group flex-grow-one">
																<label for="inventoryPrefNodeId">Select Node</label> 
																<p><select class="form-control" id="inventoryPrefNodeId"></select></p>
															</div>
															<div class="form-group flex-grow-one">
																<label for="inventoryPrefCategory">Select Category</label> 
																<p><select class="form-control" id="inventoryPrefCategory"></select></p>
															</div>
														</form>
											        </div>
											   </div>
											</div>
											
											<div class="col-sm-12 col-md-12 genericModalBodyContentPanel">
												 <div class="overlay-container preference-overlay-container">
													<div class="overlay">
														<div class="loader-container"><div class="loader"></div></div>
														<p class="overlay-text"></p>
													</div>
												</div>
												
												<div class="tab-content">
													<div id="inventoryPreferences" class="tab-pane fade in active">
														<div class="row">
															<div class="col-md-12">
																<table class="table" id="inventoryPreferenceTable" oncontextmenu="return false;">
																	<thead>
																		<tr>
																			<th>Select</th>
																			<th>Node</th>
																			<th>Category</th>
																			<th>Redundancy</th>
																			<th>Type</th>
																			<!-- <th>Preference Order</th>-->																			
																		</tr>
																	</thead>
																	<tbody>
																	</tbody>
																</table>
															</div>
														</div>
													</div>
													<div id="parametricPreference" class="tab-pane fade in ">
														
														<div class="row">
															<div class="col-md-12">
																<table class="table-bordered table" id="parametricPreferenceTable" oncontextmenu="return false;">
																	<thead>
																		<tr>
																			<th>Select</th>
																			<th>Node</th>
																			<th>Category</th>
																			<th>Type</th>
																			<th>Parameter</th>
																			<th>Parameter Value</th>		
																			<th>S.No (Optional)</th>
																			<th>Delete Preference</th>
																		</tr>
																	</thead>
																	<tbody>
																	</tbody>
																</table>
															</div>
														</div>
													</div>
													<div id="availableInventory" class="tab-pane fade in ">
														<div class="row">
															<div class="col-md-12">
																<table class="table-bordered table" id="availableInvTable" oncontextmenu="return false;">
																	<thead>
																		<tr>
																		<th>Select</th>
																		<th>Node</th>
																		<th>Category</th>
																		<th>Type</th>
																		<th>Quantity</th>	
																		<!-- <th>Reserved Location <br>(Optional)</th>	 -->
																		<th>Serial No (Optional)</th>																		
																		<th>Delete</th>
																		</tr>
																	</thead>
																	<tbody>
																	</tbody>
																</table>
															</div>
														</div>
													</div>
													<div id="exceptionPreference" class="tab-pane fade">
														<div class="row">
															<div class="col-md-12">
																<table class="table-bordered table" id="invExceptionTable" oncontextmenu="return false;">
																	<thead>
																		<tr>
																		<th>Select</th>
																		<th>Node</th>
																		<th>Exception Type</th>
																		<th>Card Type</th>
																		<th>Rack</th>
																		<th>SubRack</th>
																		<th>Slot</th>
																		<th>Port</th>	
																		<th>Serial No (Optional)</th>																		
																		<th>Delete</th>
																		</tr>
																	</thead>
																	<tbody>
																	</tbody>
																</table>
															</div>
														</div>
													</div>
												</div>
											</div>
										</div>
									</div>
									 <div class="modal-footer genericModalFooter">
										<div class="row">
											<div class="col-md-2 col-md-offset-5">
												<button class="btn btn--default" data-dismiss="modal"><i class="fa fa-times" aria-hidden="true"></i> Close</button>
											</div>
										</div>
									 </div>
							</div>

						</div>
					</div>`;

  /************************************************************************
   * On page load append modal template to index.html
   ************************************************************************/
  $(document.body).append(PreferenceModalTemplate);

  /************************************************************************
   * Json Data
   ************************************************************************/
  //const PreferenceData={
  //			"ParametricPref":{"Nodes":[1,2,3,4],"Category":[MPN,WSS,CGM],"CardType":[],"Parameters":[Demand,Direction]}
  //}

  /************************************************************************
   * Trigger for loading Configuration Modal
   ************************************************************************/
  $(document).delegate("#preferencesModalTrgr", "click", function() {
    console.log("inside preferencesModalTrgr click");
    $("#preferenceModal").modal("show");

    getPreferenceDataAjaxCall();

    //Selected tab
    fTabsSwitch(
      $(".genericModalTabMenuPanel ul li.active a")
        .eq(1)
        .text()
    );
    console.log(
      "Initial fTabsSwitch :",
      $(".genericModalTabMenuPanel ul li.active a")
        .eq(1)
        .text()
    );
  });

  /************************************************************************
   * Function to get preference data from DB
   ************************************************************************/
  function getPreferenceDataAjaxCall() {
    console.log("getPreferenceDataAjaxCall()");
    overlayOn("Fetching Data", ".body-overlay");
    let postJsonData = jsonPostObject();
    console.log("getNetworkPreferences Post Obj ::", postJsonData);

    serverPostMessage("getNetworkPreferences", postJsonData)
      .then(function(data) {
        console.log("Network Preference get data Success");
        console.log(data);
        initializeTabs(data);
        overlayOff("Success", parentSelector);
      })
      .catch(function(e) {
        console.log("fail", e);
        overlayOff("Failure", parentSelector);
      });
    // var networkPrefPostJsonData = jsonPostObject();
  }

  /************************************************************************
   * Trigger for li tab elements
   ************************************************************************/
  $(document).delegate(
    "#modalBodylinkNodeConfig > div > div.col-md-12.col-sm-12.genericModalTabMenuPanel > ul > li > a",
    "click",
    function() {
      console.log("Tab click::", $(this).html());
      var tabSelected = $(this).html();
      fTabsSwitch(tabSelected);
    }
  );

  function fTabsSwitch(tabSelected) {
    switch (tabSelected) {
      case InventoryPrefStr:
        console.log(InventoryPrefStr + "selected");
        fInventoryPrefHeadPanel();
        fPopulateInventoryPrefInputs();
        break;
      case ParametricPrefStr:
        console.log(ParametricPrefStr + "selected");
        fParametricPrefHeadPanel();
        break;
      case ResourceAllocationExceptionStr:
        console.log(ResourceAllocationExceptionStr + "selected");
        fAllocationExcHeadPanel();
        break;
      case AvailableInventoryStr:
        console.log(AvailableInventoryStr + "selected");
        fInventoryQuantHeadPanel();
        break;
      default:
        console.log("Default case");
    }
  }

  /************************************************************************
   * Trigger for adding new Parametric Preferences
   ************************************************************************/
  $(document).delegate("#deleteRow", "click", function() {
    console.log("inside deleteRow click");
    //Add new row in #parametricPreferenceTable
    $(this)
      .closest("tr")
      .remove();
  });

  /* ################################################################################
   * ---------------------------Inventory Preferences -----------------------------
   * ###############################################################################*/

  /************************************************************************
   * Trigger inventory pref submit btn
   ************************************************************************/

  //	$(document).delegate("#inventoryPrefGetTrgr",'click',function(){
  //		console.log("inside inventoryPrefGetTrgr click");
  //		//Add new row in #parametricPreferenceTable
  //		fUpdateInventoryPrefData();
  //	});

  $(document).delegate("#inventoryPrefCategory", "change", function() {
    console.log("Category Changed ...", $(this).val());
    //Add new row in #parametricPreferenceTable
    fUpdateInventoryPrefData();
  });

  $(document).delegate("#inventoryPrefNodeId", "change", function() {
    console.log("NodeId changed...", $(this).val());
    //Add new row in #parametricPreferenceTable
    fUpdateInventoryPrefData();
  });

  /************************************************************************
   * Trigger inventory pref set -- Save call to db
   ************************************************************************/
  $(document).delegate("#inventoryPrefSetTrgr", "click", function() {
    console.log("inside inventoryPrefSetTrgr click");

    //Save rows to DB
    fSaveInventoryPriorityPrefData();
  });

  /************************************************************************
   * Trigger inventory reset
   ************************************************************************/
  $(document).delegate("#inventoryPrefResetTrgr", "click", function() {
    console.log("inside inventoryPrefResetTrgr click");
    let node = $("#inventoryPrefNodeId").val();
    fResetInventoryPref(node);
  });

  /************************************************************************
   * Trigger inventory reset all
   ************************************************************************/
  $(document).delegate("#inventoryPrefResetAllTrgr", "click", function() {
    console.log("inside inventoryPrefResetTrgr click");
    fResetInventoryPref("All");
  });

  /************************************************************************
   * Priority change triggers
   ************************************************************************/
  $(document).delegate(".priorityUp", "click", function() {
    console.log("inside .priorityUp click");

    let upperTypeText = $(this)
      .parent()
      .prev()
      .html();
    let selfTypeText = $(this)
      .parent()
      .html();

    //If not the topmost priority
    if (upperTypeText !== undefined) {
      $(this)
        .parent()
        .prev()
        .html(selfTypeText);
      $(this)
        .parent()
        .html(upperTypeText);
    }
  });

  $(document).delegate(".priorityDown", "click", function() {
    console.log("inside .priorityDown click");
    let lowerTypeText = $(this)
      .parent()
      .next()
      .html();
    let selfTypeText = $(this)
      .parent()
      .html();

    //If not the topmost priority
    if (lowerTypeText !== undefined) {
      $(this)
        .parent()
        .next()
        .html(selfTypeText);
      $(this)
        .parent()
        .html(lowerTypeText);
    }
  });

  /************************************************************************
   * Function to add parametric pref buttons in head panel & footer
   ************************************************************************/
  function fInventoryPrefHeadPanel() {
    console.log("fInventoryPrefHeadPanel()");

    //const inputId=tabName=="InventoryPrefStr"?inventoryPrefNodeId:inventoryQuantNodeId;
    //const inputId=tabName=="InventoryPrefStr"?inventoryPrefNodeId:inventoryQuantNodeId;

    $("#preferenceModal .genericModalBodyHeadPanel").empty()
      .append(`<div class="row">
				<div class="col-md-8 col-md-offset-2">
					<form class="form-inline npt-form-inline d-flex" >
						<div class="form-group flex-grow-one">
							<label for="inventoryPrefNodeId">Select Node</label> 
							<p><select class="form-control" id="inventoryPrefNodeId"></select></p>
						</div>
						<div class="form-group flex-grow-one">
							<label for="inventoryPrefCategory">Select Category</label> 
							<p><select class="form-control" id="inventoryPrefCategory"></select></p>
						</div>
					</form>
		        </div>
		</div>`);

    $("#preferenceModal .genericModalFooter").empty().append(`<div class="row">
				<div class="col-md-12 d-flex justify-content-center">
				<button class="btn btn--default" type="button" id="inventoryPrefResetTrgr" ><i class="fa fa-refresh" aria-hidden="true"></i> Reset</button>
				<button class="btn btn--default" type="button" id="inventoryPrefResetAllTrgr" ><i class="fa fa-refresh" aria-hidden="true"></i> Reset All</button>
				<button class="btn btn--default" type="button" id="inventoryPrefSetDegreeTrgr" disabled><i class="fa fa-floppy-o" aria-hidden="true"></i> Set Degree</button>
				<button class="btn btn--default" type="button" id="inventoryPrefSetTrgr"><i class="fa fa-floppy-o" aria-hidden="true"></i> Save Preference</button>
				<button class="btn btn--default" type="button" id="inventoryPrefApplyToAllTrgr" disabled><i class="fa fa-floppy-o" aria-hidden="true"></i> Apply to all</button>
				<button class="btn btn--default" data-dismiss="modal"><i class="fa fa-times" aria-hidden="true"></i> Close</button>
				</div>
		</div>`);
  }

  /************************************************************************
   * Initialize tabs on ajax success response
   ************************************************************************/
  function fUpdateInventoryPrefData() {
    //Filter Data and get output and then initializ inventory pref tab
    let node = $("#inventoryPrefNodeId").val();
    let category = $("#inventoryPrefCategory").val();

    $("#preferenceModal #inventoryPreferenceTable tbody")
      .fadeOut("fast")
      .empty()
      .append(fAddInventoryprefData(inventoryprefData, node, category))
      .fadeIn("slow");
    initializeInventoryPrefDataFromDb(inventoryprefData);
  }

  /************************************************************************
   * Initialiaze Inventory preferences fetched from DB
   ************************************************************************/
  function initializeInventoryPrefDataFromDb(invPrefData) {
    console.log("## initializeInventoryPrefDataFromDb()");
    let node = $("#inventoryPrefNodeId").val();
    let category = $("#inventoryPrefCategory").val();
    let groupedInventoryData = _.groupBy(
      invPrefData.filter(obj => {
        if (category == "ALL") return obj.NodeId == node;
        else return obj.NodeId == node && obj.Category == category;
      }),
      "Category"
    );

    // console.log(
    //   "--  Grouped Data :",
    //   groupedInventoryData,
    //   "--  Redund Data :",
    //   $(".newRedundancyState").length,
    //   "--  Keys Data :",
    //   Object.keys(groupedInventoryData).length
    // );

    Object.keys(groupedInventoryData).map((key, index) => {
      // console.log(
      //   "Keys and Values",
      //   key,
      //   index,
      //   groupedInventoryData[key][0].Redundancy
      // );
      // console.log("Setting in default initializeInventoryPrefDataFromDb");
      $(".newRedundancyState:eq(" + index + ")")
        .attr("title", "Select Node ")
        .tooltip()
        .editable({
          type: "select",
          value: groupedInventoryData[key][0].Redundancy,
          source: redundancyArr,
          onblur: "submit",
          validate: function(value) {
            console.log("newRedundancyState Validation", $(this).html());
          }
        });
    });
  }

  /************************************************************************
   * Initialize tabs on ajax success response
   ************************************************************************/
  function fPopulateInventoryPrefInputs() {
    $("#inventoryPrefNodeId").empty();

    for (let i = 0; i < nodeArr.length; i++) {
      $("#inventoryPrefNodeId").append(
        $("<option>", {
          value: nodeArr[i].text,
          text: nodeArr[i].text
        })
      );
    }

    $("#inventoryPrefCategory").empty();

    for (let i = 0; i < categoryArr.length; i++) {
      $("#inventoryPrefCategory").append(
        $("<option>", {
          value: categoryArr[i].text,
          text: categoryArr[i].text
        })
      );
    }

    $("#inventoryPrefCategory").prepend(
      $("<option>", {
        value: "ALL",
        text: "ALL",
        selected: true
      })
    );

    fUpdateInventoryPrefData();
  }

  /************************************************************************
   * Get template to be appended to inventory pref table
   ************************************************************************/
  function fAddInventoryprefData(invPrefdata, nodeId, category) {
    var template = "";
    // Filter Data based on NodeId and Category
    var filteredData = _.groupBy(
      invPrefdata.filter(obj => {
        if (category == "ALL" && nodeId == "ALL") return obj;
        else if (category == "ALL") return obj.NodeId == nodeId;
        else return obj.NodeId == nodeId && obj.Category == category;
      }),
      "Category"
    );

    console.log("fAddInventoryprefData -- filteredData :", filteredData);
    _.each(Object.keys(filteredData), key => {
      let category = filteredData[key][0].Category;
      // let redundancy = filteredData[key][0].Redundancy;

      let categoryEntries = filteredData[key];
      template += `<tr style="border-bottom: 1px solid #dedbdb;">
      <td>
        <input type="checkbox" />
      </td>
      <td>${nodeId}</td>
      <td>${category}</td>
      <td>
        <span class="newRedundancyState" />
      </td>
      <td>
      <div class="priority-radio-buttons-wrapper">`;

      _.each(categoryEntries, obj => {
        console.log("Priority Field Object", JSON.stringify(obj));
        let id = obj.CardType + "_" + obj.Category;
        let name = "PriorityList" + "_" + obj.Category;

        if (obj.Priority == 1) {
          template += `
          <input type="radio" class="priority-radio-button" name='${name}' value='${
            obj.CardType
          }' id='${id}' checked />
          <label for='${id}'>${obj.CardType}</label>`;
        } else {
          template += `
        <input type="radio" class="priority-radio-button" name='${name}' value='${
            obj.CardType
          }' id='${id}' />
        <label for='${id}'>${obj.CardType}</label>`;
        }
      });

      template += `</div></td>
      </tr>`;
    });

    // for (var i = 0; i < filteredData.length; i++) {
    //   var id = filteredData[i].NodeId,
    //     cat = filteredData[i].Category,
    //     redund = filteredData[i].Redundancy,
    //     cardPrefPriorityData = [],
    //     cardPrefPriorityDataTemplate = "";

    //   //console.log("filteredData["+i+"]",filteredData[i]);
    //   var j = 1;
    //   while (
    //     i < filteredData.length &&
    //     filteredData[i].NodeId == id &&
    //     filteredData[i].Category == cat
    //   ) {
    //     let cType = filteredData[i].CardType;
    //     let redund = filteredData[i].Redundancy;
    //     let priority = filteredData[i].Priority;

    //     //				let nodeCatPropObj={};
    //     //				nodeCatPropObj.CardType=cType;
    //     //				nodeCatPropObj.Preference=pref;
    //     //				nodeCatPropObj.Redundancy=redund;

    //     let listItem = {};
    //     listItem.text = `<li class="list-group-item clearfix priorityListItem"><!-- <span class="badge badge-primary pull-left rank">${priority}</span> --><i class="fa fa-arrow-down pull-right cursor priorityDown" title="Move down in priority order"></i> <span class="cardType">${cType}</span> <i class="fa fa-arrow-up pull-right cursor priorityUp" title="Move up in priority order"></i></li>`;
    //     listItem.priority = priority;

    //     //cardPrefPriorityDataTemplate+=listItem.text;

    //     // Push object in a sorted manner based on priority
    //     cardPrefPriorityData.push(listItem);
    //     cardPrefPriorityData.sort(function(a, b) {
    //       return a.priority - b.priority;
    //     });

    //     // console.log("cardPrefPriorityData:", cardPrefPriorityData);

    //     i++;
    //     j++;
    //   }

    //   // console.log(
    //   //   "cat:",
    //   //   cat,
    //   //   "cardPrefPriorityDataTemplate:",
    //   //   cardPrefPriorityDataTemplate
    //   // );
    //   //As for loop is already incrementing the value of i
    //   i--;

    //   //Initialize Type column template
    //   cardPrefPriorityData.forEach(function(obj) {
    //     //Append list item
    //     cardPrefPriorityDataTemplate += obj.text;
    //   });

    //   // console.log("cardPrefRedData",cardPrefRedData);

    //   template += `<tr style="border-bottom: 1px solid #dedbdb;">
    // 		<td><input type="checkbox"/></td>
    // 		<td>${id}</td>
    // 		<td>${cat}</td>
    // 		<td><span class="newRedundancyState"></span></td>
    // 		<td><ul class="list-group" id="sortableInvPref">${cardPrefPriorityDataTemplate}</ul></td>
    // 		</tr>`;
    // }

    //console.log("template:",template);

    return $(template);
  }

  /************************************************************************
   * Function to add parametric pref buttons in head panel & footer
   ************************************************************************/
  function fSaveInventoryPriorityPrefData() {
    var $headers = $("#inventoryPreferenceTable th").not(
      "#inventoryPreferenceTable th:first-child"
    );
    var inventoryPrefRows = [];
    $("#inventoryPreferenceTable tbody tr").each(function(index) {
      //console.log("New Client Row",$(this));
      var $cells = $(this)
        .find("td")
        .not("td:first-child");
      inventoryPrefRows[index] = {};
      $cells.each(function(cellIndex) {
        if (String($($headers[cellIndex]).html()) == "Redundancy")
          inventoryPrefRows[index][$($headers[cellIndex]).html()] = $(this)
            .find(".editable")
            .html();
        else if (String($($headers[cellIndex]).html()) == "Type") {
          let cardTypesOrder = [];
          $(this)
            .find("input[type=radio]:checked + label")
            .each(function(index, value) {
              // console.log(value, $(this).text());
              cardTypesOrder.push($(this).text());
            });
          $(this)
            .find("input[type=radio] + label")
            .not("input[type=radio]:checked + label")
            .each(function(index, value) {
              // console.log("---", value, $(this).text());
              cardTypesOrder.push($(this).text());
            });
          console.log("*** cardTypesOrder", cardTypesOrder);
          inventoryPrefRows[index][
            $($headers[cellIndex]).html()
          ] = cardTypesOrder;
        } else
          inventoryPrefRows[index][$($headers[cellIndex]).html()] = $(
            this
          ).html();
      });
    });

    console.log("inventoryPrefRows::", inventoryPrefRows);

    overlayOn("Saving Inventory Priority", parentSelector);
    let inventoryPrefPostJsonData = jsonPostObject();
    inventoryPrefPostJsonData.InventoryPreferences = inventoryPrefRows;
    console.log(
      "inventoryPrefPostJsonData Post Obj ::",
      inventoryPrefPostJsonData
    );

    serverPostMessage("saveInventoryPriorityPref", inventoryPrefPostJsonData)
      .then(function(data) {
        console.log("InventortyPreferences Set Success");
        console.log(data);
        overlayOff("Saved Inventory Preferences Succesfully", parentSelector);
      })
      .catch(function(e) {
        overlayOff("Fetch Failure", parentSelector);
        console.log("fail", e);
      });
  }

  /************************************************************************
   * Reset inventory pref
   ************************************************************************/
  function fResetInventoryPref(type) {
    overlayOn("Resetting Inventory Preferences", parentSelector);
    let inventoryPrefResetPostJsonData = jsonPostObject();
    inventoryPrefResetPostJsonData.Type = type;
    console.log(
      "inventoryPrefResetPostJsonData Post Obj ::",
      inventoryPrefResetPostJsonData
    );

    serverPostMessage(
      "resetInventoryPriorityPref",
      inventoryPrefResetPostJsonData
    )
      .then(function(data) {
        console.log("InventortyPreferences Reset Success");
        console.log(data);
        inventoryprefData = data;
        fPopulateInventoryPrefInputs();
        overlayOff("Inventory Preferences Reset Succesful", parentSelector);
      })
      .catch(function(e) {
        overlayOff("Fetch Failure", parentSelector);
        console.log("fail", e);
      });
  }

  /* ################################################################################
   * ---------------------------Parametric Preferences -----------------------------
   * ###############################################################################*/

  /************************************************************************
   * Trigger for adding new Parametric Preferences
   ************************************************************************/
  $(document).delegate("#parametricAddPreferenceTrgr", "click", function() {
    console.log("inside inventoryPreferencesTrgr click");
    //Add new row in #parametricPreferenceTable
    fAddParametricPrefTableRow();
  });

  /************************************************************************
   * Trigger for saving Parametric Preferences
   ************************************************************************/
  $(document).delegate("#parametricPrefSetTrgr", "click", function() {
    console.log("inside inventoryPreferencesTrgr click");
    //Add new row in #parametricPreferenceTable
    fSaveParamPref();
    overlayOn("Saving Preference", parentSelector);
  });

  /************************************************************************
   * Delete selected rows from Available Inventory
   ************************************************************************/
  $(document).delegate("#parametricDeletePreferenceTrgr", "click", function() {
    let checkedRows = $(
      "#parametricPreferenceTable tbody tr td input:checkbox:checked"
    );
    _.each(checkedRows, function(input) {
      console.log(
        $(input)
          .parent()
          .parent()
          .remove()
      );
    });
  });

  /************************************************************************
   * Function to add parametric pref buttons in head panel & footer
   ************************************************************************/
  function fSaveParamPref() {
    var $headers = $("#parametricPreferenceTable th").not(
      "#parametricPreferenceTable th:first-child,#parametricPreferenceTable th:last-child"
    );
    var paramPrefRows = [];
    $("#parametricPreferenceTable tbody tr").each(function(index) {
      //console.log("New Client Row",$(this));
      var $cells = $(this)
        .find("td")
        .not("td:first-child,td:last-child");
      paramPrefRows[index] = {};
      $cells.each(function(cellIndex) {
        if ($($headers[cellIndex]).html() == "S.No (Optional)") {
          if (
            $(this)
              .find(".editable")
              .html() == "Empty"
          )
            paramPrefRows[index][$($headers[cellIndex]).html()] = "";
          else
            paramPrefRows[index][$($headers[cellIndex]).html()] = $(this)
              .find(".editable")
              .html();
        } else
          paramPrefRows[index][$($headers[cellIndex]).html()] = $(this)
            .find(".editable")
            .html();
      });
    });

    console.log("paramPrefRows::", paramPrefRows);

    overlayOn("Saving Inventory Priority", parentSelector);
    let paramPrefPostJsonData = jsonPostObject();
    paramPrefPostJsonData.ParamPreferences = paramPrefRows;
    console.log("saveParamPref Post Obj ::", paramPrefPostJsonData);

    serverPostMessage("saveParamPref", paramPrefPostJsonData)
      .then(function(data) {
        console.log("ParamPreferences Set Success");
        console.log(data);
        overlayOff("Saved Parametric Preferences Succesfully", parentSelector);
      })
      .catch(function(e) {
        overlayOff("Fetch Failure", parentSelector);
        console.log("fail", e);
      });
  }

  /************************************************************************
   * Function to add buttons for Inventory pref in head panel & footer
   ************************************************************************/
  function fParametricPrefHeadPanel() {
    console.log("fParametricPrefHeadPanel()", $(".genericModalBodyHeadPanel"));
    $("#preferenceModal .genericModalBodyHeadPanel").empty()
      .append(`<div class="row">
				<div class="col-md-8 col-md-offset-2 d-flex justify-content-center">
				<button class="btn btn--default" id="parametricAddPreferenceTrgr"><i class="fa fa-plus" aria-hidden="true"></i> Add Preference</button>
				<button class="btn btn--default" id="parametricDeletePreferenceTrgr"><i class="fa fa-trash" aria-hidden="true"></i> Delete Selected Preference</button>
				</div>
		</div>`);

    $("#preferenceModal .genericModalFooter").empty().append(`<div class="row">
				<div class="col-md-8 col-md-offset-2 d-flex justify-content-center">
				<button class="btn btn--default" type="button" id="parametricPrefSetTrgr" ><i class="fa fa-floppy-o" aria-hidden="true"></i> Set Prefernce</button>
				<button class="btn btn--default" data-dismiss="modal"><i class="fa fa-times" aria-hidden="true"></i> Close</button>
				</div>
		</div>`);
  }

  /************************************************************************
   * Function to add news parametric preferences
   ************************************************************************/
  function fAddParametricPrefTableRow() {
    console.log("AddParametricPrefTableRow()");
    //		let rowCount=$("#parametricPreferenceTable tbody tr").length;
    //		console.log("Number of rows :"+rowCount);

    let template = "";

    template += fGetParamPrefRowTemplate();
    //console.log(template);
    $("#parametricPreferenceTable tbody").append($(template));

    initializeEditablePref();
  }

  /************************************************************************
   * Returns template for a param pref row
   ************************************************************************/
  function fGetParamPrefRowTemplate() {
    return `<tr><td><input type="checkbox" id="deleteRowId" /> <span class="checkbox"></span></td>
		<td><span class='newNode'></span></td>
		<td><span class="newCategory"></span></td>
		<td><span class="newCardType"></span></td>
		<td><span class="newParameter"></span></td>
		<td><span class="newValue"></span></td>
		<td><span class="newSerialNo"></span></td>		
		<td><img id="deleteRow" src="images/General/Trash-26.png" class="img-rounded" alt="Delete"></td>
		</tr>`;
  }

  /************************************************************************
   * Returns template for a param pref row from DB
   ************************************************************************/
  function fGetParamPrefRowTemplateDb() {
    return `<tr><td><input type="checkbox" id="deleteRowId" /> <span class="checkbox"></span></td>
		<td><span class='paramNode'></span></td>
		<td><span class="paramCategory"></span></td>
		<td><span class="paramCardType"></span></td>
		<td><span class="paramParameter"></span></td>
		<td><span class="paramValue"></span></td>
		<td><span class="paramSerialNo"></span></td>		
		<td><img id="deleteRow" src="images/General/Trash-26.png" class="img-rounded" alt="Delete"></td>
		</tr>`;
  }

  /************************************************************************
   * Initialiaze parametric preferences fetched from DB
   ************************************************************************/
  function initializeParametricPrefDataFromDb(paramPrefData) {
    console.log("## initializeParametricPrefDataFromDb()");
    for (var i = 0; i < paramPrefData.length; i++) {
      $(".paramNode:eq(" + i + ")")
        .attr("title", "Select Node ")
        .tooltip()
        .editable({
          type: "select",
          value: paramPrefData[i].NodeId,
          source: nodeArr,
          onblur: "submit",
          validate: function(value) {
            console.log($(this).html());
            nodeValidationParamPref(value, $(this));
          }
        });

      $(".paramCategory:eq(" + i + ")")
        .attr("title", "Select Category")
        .tooltip()
        .editable({
          type: "select",
          value: fGetCategory(paramPrefData[i].Category),
          source: categoryArr,
          onblur: "submit",
          validate: function(value) {
            console.log($(this).html());
            categoryValidation(categoryArr[value - 1].text, $(this));
          }
        });

      //console.log("paramPrefData[i].Category::",paramPrefData[i].Category);
      $(".paramCardType:eq(" + i + ")")
        .attr("title", "Select Type ")
        .tooltip()
        .editable({
          type: "select",
          value: fGetCardType(
            paramPrefData[i].CardType,
            paramPrefData[i].Category
          ),
          source: fGetCardTypeArrforCategory(paramPrefData[i].Category),
          onblur: "submit",
          validate: function(value) {}
        });

      $(".paramParameter:eq(" + i + ")")
        .attr("title", "Select Parameter ")
        .tooltip()
        .editable({
          type: "select",
          value: fGetParameter(paramPrefData[i].Parameter),
          source: parameterArr,
          onblur: "submit",
          validate: function(value) {
            parameterValidation($(this), value);
          }
        });

      console.log(
        "paramPrefData[i].NodeId",
        paramPrefData[i].NodeId,
        "paramPrefData[i].Parameter",
        paramPrefData[i].Parameter,
        " paramPrefData[i].Value",
        paramPrefData[i].Value
      );
      console.log(
        "valueArr",
        fGetParameterValueSrc(
          paramPrefData[i].NodeId,
          paramPrefData[i].Parameter
        )
      );
      $(".paramValue:eq(" + i + ")")
        .attr("title", "Select Parameter Value")
        .tooltip()
        .editable({
          type: "select",
          value: paramPrefData[i].Value,
          source: fGetParameterValueSrc(
            paramPrefData[i].NodeId,
            paramPrefData[i].Parameter
          ),
          onblur: "submit",
          validate: function(value) {}
        });

      $(".paramSerialNo:eq(" + i + ")")
        .attr("title", "Enter Serial No")
        .tooltip()
        .editable({
          type: "text",
          value: paramPrefData[i].SerialNum,
          onblur: "submit",
          validate: function(value) {}
        });
    }
  }

  /************************************************************************
   * Initialiaze new parametric preferences
   ************************************************************************/
  function initializeEditablePref() {
    $.fn.editable.defaults.mode = "inline";

    console.log("## initializeEditablePref()");

    $(".newNode")
      .attr("title", "Select Node ")
      .tooltip()
      .editable({
        type: "select",
        value: nodeArr[0].value,
        source: nodeArr,
        onblur: "submit",
        validate: function(value) {
          //				nodeValidation($(this), value);

          console.log(
            $("#preferenceModal .genericModalTabMenuPanel li.active a").text(),
            " --- ",
            ParametricPrefStr,
            " Equal:",
            ParametricPrefStr ==
              $("#preferenceModal .genericModalTabMenuPanel li.active a").text()
          );
          if (
            ParametricPrefStr ==
            $("#preferenceModal .genericModalTabMenuPanel li.active a").text()
          ) {
            console.log(
              $("#preferenceModal .genericModalTabMenuPanel li.active a").text()
            );
            nodeValidationParamPref(value, $(this));
          }
        }
      });

    $(".newCategory")
      .attr("title", "Select Category")
      .tooltip()
      .editable({
        type: "select",
        value: categoryArr[0].value,
        source: categoryArr,
        onblur: "submit",
        validate: function(value) {
          console.log($(this).html());
          categoryValidation(categoryArr[value - 1].text, $(this));
        }
      });

    $(".newCardType")
      .attr("title", "Select Type ")
      .tooltip()
      .editable({
        type: "select",
        value: fGetCardTypeArrforCategory(categoryArr[0].text)[0].value,
        source: fGetCardTypeArrforCategory(categoryArr[0].text),
        onblur: "submit",
        validate: function(value) {}
      });

    $(".newParameter")
      .attr("title", "Select Parameter ")
      .tooltip()
      .editable({
        type: "select",
        value: parameterArr[0].value,
        source: parameterArr,
        onblur: "submit",
        validate: function(value) {
          parameterValidation($(this), value);
        }
      });

    // console.log(
    //   "valueArr[nodeArr[0].value]['Demand']",
    //   valueArr[nodeArr[0].value]["Demand"]
    // );
    $(".newValue")
      .attr("title", "Select Parameter Value")
      .tooltip()
      .editable({
        type: "select",
        value: valueArr[nodeArr[0].value]["Demand"]
          ? valueArr[nodeArr[0].value]["Demand"][0]["value"]
          : 0,
        //source: valueArr[nodeArr[0].value][parameterArr[0].text],
        source: valueArr[nodeArr[0].value]["Demand"],
        onblur: "submit",
        validate: function(value) {}
      });

    $(".newSerialNo")
      .attr("title", "Enter Serial No")
      .tooltip()
      .editable({
        type: "text",
        value: " ",
        onblur: "submit",
        validate: function(value) {}
      });

    $(".newQuantity")
      .attr("title", "Enter Quantity")
      .tooltip()
      .editable({
        type: "text",
        value: "1",
        onblur: "submit",
        validate: function(value) {
          if (value < 0) return "Enter value greater than 0";
        }
      });

    $(".newReservedRack")
      .attr("title", "Select rack ")
      .tooltip()
      .editable({
        type: "select",
        value: 3,
        source: nodeArr,
        onblur: "submit",
        validate: function(value) {}
      });

    $(".newReservedSubrack")
      .attr("title", "Select subrack ")
      .tooltip()
      .editable({
        type: "select",
        value: 1,
        source: nodeArr,
        onblur: "submit",
        validate: function(value) {}
      });

    $(".newReservedSlot")
      .attr("title", "Select slot ")
      .tooltip()
      .editable({
        type: "select",
        value: 2,
        source: nodeArr,
        onblur: "submit",
        validate: function(value) {}
      });

    // console.log("Setting in default newRedundancyState");
    // $(".newRedundancyState")
    //   .attr("title", "Set Redundancy ")
    //   .tooltip()
    //   .editable({
    //     type: "select",
    //     value: redundancyArr[0],
    //     source: redundancyArr,
    //     onblur: "submit",
    //     validate: function(value) {}
    //   });

    // Allocation exception initialialize

    $(".newExceptionType")
      .attr("title", "Enter Type")
      .tooltip()
      .editable({
        type: "select",
        value: ExceptionTypeArr[0].value,
        source: ExceptionTypeArr,
        onblur: "submit",
        validate: function(value) {}
      });

    $(".newRack")
      .attr("title", "Enter Rack")
      .tooltip()
      .editable({
        type: "select",
        value: 1,
        source: RackArr,
        onblur: "submit",
        validate: function(value) {}
      });

    $(".newSubRack")
      .attr("title", "Enter SubRack")
      .tooltip()
      .editable({
        type: "select",
        value: 1,
        source: SubRackArr,
        onblur: "submit",
        validate: function(value) {}
      });

    $(".newSlot")
      .attr("title", "Enter Slot")
      .tooltip()
      .editable({
        type: "select",
        value: 1,
        source: SlotArr,
        onblur: "submit",
        validate: function(value) {}
      });

    $(".newPort")
      .attr("title", "Enter Port")
      .tooltip()
      .editable({
        type: "select",
        value: 1,
        source: PortArr,
        onblur: "submit",
        validate: function(value) {}
      });

    $(".newExceptionCardType")
      .attr("title", "Select Card Type ")
      .tooltip()
      .editable({
        type: "select",
        value: fGetExceptionCardType(ExceptionCardTypeArr[0].text),
        source: ExceptionCardTypeArr,
        onblur: "submit",
        validate: function(value) {}
      });

    $.fn.editable.defaults.mode = "inline";
  }

  /************************************************************************
   * Category validation -- change Type based  on category
   ************************************************************************/
  function categoryValidation(cat, $this) {
    var $cardType = $this
      .parent()
      .next()
      .children();
    $cardType.editable("option", "source", fGetCardTypeArrforCategory(cat));
    $cardType.editable("setValue", 0);
  }

  function nodeValidation($this, nodeId) {
    //  var $cardType=$this.parent().next().children();
    //  $cardType.editable('option', 'source', fGetCardTypeArrforCategory(cat));
    //  $cardType.editable('setValue',0);
  }

  function nodeValidationParamPref(nodeId, $this) {
    var $parameter = $this
      .parent()
      .next()
      .next()
      .next()
      .children();
    let $paramVal = $this
      .parent()
      .next()
      .next()
      .next()
      .next()
      .children();

    let parameter = $parameter.text();
    let srcArr = fGetParameterValueSrc(nodeId, parameter);

    if (srcArr.length > 0) {
      $paramVal.editable("option", "source", srcArr);
      $paramVal.editable("setValue", srcArr[0].text);
    } else $paramVal.editable("setValue", 0);

    console.log(
      "nodeValidationParamPref --- Src Changed to:",
      srcArr,
      " for Ndoeid",
      nodeId
    );
  }

  function parameterValidation($this, parameter) {
    console.log("parameter", parameter);
    let nodeId = $this
      .parent()
      .parent()
      .children()
      .eq(1)
      .children()
      .html();
    console.log(
      "parameterValidation nodeId:",
      nodeId,
      " parameter:",
      parameter
    );
    let $paramValue = $this
      .parent()
      .next()
      .children();
    console.log("fGetParameterText(parameter)):", fGetParameterText(parameter));

    $paramValue.editable(
      "option",
      "source",
      fGetParameterValueSrc(nodeId, fGetParameterText(parameter))
    );
    if (fGetParameterValueSrc(nodeId, fGetParameterText(parameter)).length > 0)
      $paramValue.editable(
        "setValue",
        fGetParameterValueSrc(nodeId, fGetParameterText(parameter))[0].value
      );
    else $paramValue.editable("setValue", 0);
  }

  /* #############################################################################
   * >>>>>>>>>>>>>>>>>>>>>>    Available Inventory       <<<<<<<<<<<<<<<<<<<<<<<<<
   * #############################################################################*/

  /************************************************************************
   * Trigger for adding new Parametric Preferences
   ************************************************************************/
  $(document).delegate("#inventoryQuantAddItemTrgr", "click", function() {
    console.log("inside inventoryQuantAddItemTrgr click");
    //Add new row in #parametricPreferenceTable
    fAddAvailableInvTableRow();
  });

  /************************************************************************
   * Trigger for saving Stock Preferences
   ************************************************************************/
  $(document).delegate("#inventoryQuantSetTrgr", "click", function() {
    console.log("inside inventoryQuantSetTrgr click");
    //Add new row in #parametricPreferenceTable
    fSaveInventoryStockPref();
    overlayOn("Saving Preference", parentSelector);
  });

  /************************************************************************
   * Function to save stock pref
   ************************************************************************/
  function fSaveInventoryStockPref() {
    var $headers = $("#availableInvTable th").not(
      "#availableInvTable th:first-child,#parametricPreferenceTable th:last-child"
    );
    var stockPrefRows = [];
    $("#availableInvTable tbody tr").each(function(index) {
      //console.log("New Client Row",$(this));
      var $cells = $(this)
        .find("td")
        .not("td:first-child,td:last-child");
      stockPrefRows[index] = {};
      $cells.each(function(cellIndex) {
        console.log(String($($headers[cellIndex]).text()));
        console.log(String($($headers[cellIndex]).html()));
        if (
          String($($headers[cellIndex]).html()).indexOf(
            "Serial No (Optional)"
          ) != -1
        ) {
          if (
            $(this)
              .find(".editable")
              .html() == "Empty"
          )
            stockPrefRows[index][$($headers[cellIndex]).html()] = "";
          else
            stockPrefRows[index][$($headers[cellIndex]).html()] = $(this)
              .find(".editable")
              .html();

          // let serialNumArray = $(this).find('.editable');
          // console.log(serialNumArray);
          // let serialNums = '';
          // for (let s = 0; s < serialNumArray.length; s++) {
          // 	let serialNumText = $(serialNumArray[s]).text();
          // 	console.log("serialNumText", serialNumText);
          // 	if (s == 0) {
          // 		if (serialNumText != 'Empty')
          // 			serialNums += $(serialNumArray[s]).text();
          // 	}
          // 	else {
          // 		if (serialNumText != 'Empty')
          // 			serialNums += "," + $(serialNumArray[s]).text();
          // 	}

          // }
          // stockPrefRows[index][$($headers[cellIndex]).text()] = serialNums;
        }
        // else if (String($($headers[cellIndex]).html()).indexOf("Reserv") != -1) {
        // 	stockPrefRows[index]["Reserved"] = "";
        // }
        else
          stockPrefRows[index][$($headers[cellIndex]).html()] = $(this)
            .find(".editable")
            .html();
      });
    });

    console.log("stockPrefRows::", stockPrefRows);

    overlayOn("Saving Inventory Priority", parentSelector);
    let stockPrefPostJsonData = jsonPostObject();
    stockPrefPostJsonData.StockPreferences = stockPrefRows;
    console.log("stockPrefPostJsonData Post Obj ::", stockPrefPostJsonData);

    serverPostMessage("saveStockPref", stockPrefPostJsonData)
      .then(function(data) {
        console.log("StockPreferences Set Success");
        console.log(data);
        overlayOff(
          "Saved StockPreferences Preferences Succesfully",
          parentSelector
        );
      })
      .catch(function(e) {
        overlayOff("Fetch Failure", parentSelector);
        console.log("fail", e);
      });
  }

  /************************************************************************
   * Function to add news parametric preferences
   ************************************************************************/
  function fAddAvailableInvTableRow() {
    console.log("fAddAvailableInvTableRow()");
    let rowCount = $("#availableInvTable tbody tr").length;

    console.log("Number of rows :" + rowCount);

    let template = "";

    template += fgetAvailableInventoryRowTemplate();

    // template += `<tr><td><input type="checkbox" id="deleteRowId" /> <span class="checkbox"></span></td>`;
    // template += "<td><span class='newNode'></span></td>";
    // template += `<td><span class="newCategory"></span></td>
    //     <td><span class="newCardType"></span></td>
    // 	<td><span class="newQuantity"></span></td>
    // 	<td><span><button class="btn btn-sm btn--default addReservation" type="button" ><i class="fa fa-plus" aria-hidden="true">  Add New Reserved Location</i></button></span></td>
    // 	<td><span><button class="btn btn-sm btn--default addSerialNumber" type="button" ><i class="fa fa-plus" aria-hidden="true"> Add New S.No.</i></button></span></td>
    // 	<!-- <td><span class="newReservedRack"></span> | <span class="newReservedSubrack"></span> | <span class="newReservedSlot"></span></td> -->
    // 	<td><span class="newSerialNo"></span></td>
    // 	<td><img id="deleteRow" src="images/General/Trash-26.png" class="img-rounded" alt="Delete"></td>
    // 	</tr>`;
    //console.log(template);
    const newRow = $(template);
    //console.log(newRow);
    $("#availableInvTable tbody").append(newRow);

    initializeEditablePref();
  }

  /************************************************************************
   * Returns template for a param pref row
   ************************************************************************/
  function fgetAvailableInventoryRowTemplateDb() {
    return `<tr><td><input type="checkbox" id="deleteRowId" /> <span class="checkbox"></span></td>
			<td><span class='stockNode'></span></td>
			<td><span class="stockCategory"></span></td>
		    <td><span class="stockCardType"></span></td>
			<td><span class="stockQuantity"></span></td>
			<!-- <td><span><button class="btn btn-sm btn--default addReservation" type="button" ><i class="fa fa-plus" aria-hidden="true">  Add New Reserved Location</i></button></span></td>
			<td><span><button class="btn btn-sm btn--default addSerialNumber" type="button" ><i class="fa fa-plus" aria-hidden="true"> Add New S.No.</i></button></span></td>
			<td><span class="newReservedRack"></span> | <span class="newReservedSubrack"></span> | <span class="newReservedSlot"></span></td> -->
			<td><span class="stockSerialNo"></span></td>		
			<td><img id="deleteRow" src="images/General/Trash-26.png" class="img-rounded" alt="Delete"></td>
			</tr>`;
  }

  /************************************************************************
   * Returns template for a param pref row
   ************************************************************************/
  function fgetAvailableInventoryRowTemplate() {
    return `<tr><td><input type="checkbox" id="deleteRowId" /> <span class="checkbox"></span></td>
			<td><span class='newNode'></span></td>
			<td><span class="newCategory"></span></td>
		    <td><span class="newCardType"></span></td>
			<td><span class="newQuantity"></span></td>
			<!-- <td><span><button class="btn btn-sm btn--default addReservation" type="button" ><i class="fa fa-plus" aria-hidden="true">  Add New Reserved Location</i></button></span></td>
			<td><span><button class="btn btn-sm btn--default addSerialNumber" type="button" ><i class="fa fa-plus" aria-hidden="true"> Add New S.No.</i></button></span></td>
			<td><span class="newReservedRack"></span> | <span class="newReservedSubrack"></span> | <span class="newReservedSlot"></span></td> -->
			<td><span class="newSerialNo"></span></td>
			<td><img id="deleteRow" src="images/General/Trash-26.png" class="img-rounded" alt="Delete"></td>
			</tr>`;
  }

  /************************************************************************
   * Add serial number trigger
   ************************************************************************/
  $(document).delegate(".addSerialNumber", "click", function() {
    console.log("inside .addSerialNumber click");
    const rowIndex = $(this).closest("tr");
    const position = $(this).position();

    console.log("rowIndex:", rowIndex + " position:", position);

    //Add new row in #parametricPreferenceTable
    //		$(this).parent().append(`<div class="row" style="margin:auto;">
    //		   <div class="form-group col-md-6">
    //		     <label for="">Enter Serial No</label>
    //		     <input type="text" class="form-control">
    //		   </div>
    //
    //		  <div class="form-group btn-group col-md-6">
    //		    <button class="btn btn-priamry">Save</button>
    //		    <button class="btn btn--default">Cancel</button>
    //		  </div>
    //
    //		</div>`);

    let quantity = $(this)
      .parent()
      .parent()
      .prev()
      .prev()
      .find(".newQuantity,.stockQuantity")
      .html();
    console.log($(this).siblings().length + "  " + quantity);

    if ($(this).siblings().length >= parseInt(quantity)) {
      bootBoxWarningAlert(
        "Increase quantity if you want to add more serial numbers."
      );
    } else {
      $(this).parent().append(`<div class="newSerialNoRow"> 
			
			<div class="clearfix">
				<span class="newSerialNo"></span>  
				<span class="btn-group pull-right">
					<button class="btn btn-sm btn-warning addFaultOnSerialNumber" type="button" title="Add Fault"><i class="fa fa-plus" aria-hidden="true"></i></button>
					<button class="btn btn-sm btn--default deleteSerialNumRow" type="button" ><i class="fa fa-times" aria-hidden="true"></i></button>
				</span>	
			</div>
			
			<ul class="faultListOnSerialNum"></ul>
									
			</div>`);

      initializeEditablePref();
    }

    //   	<input type="checkbox" name="fault" id="fault"> <br>
    //		 <div class="form-group col-md-4">
    //	     <label for="">Rack</label>
    //	     <select name="" id="">
    //	       <option value="1">1</option>
    //	       <option value="1">2</option>
    //	       <option value="1">3</option>
    //	     </select>
    //	   </div>
    //	   <div class="form-group col-md-4">
    //	    <label for="">SubRack</label>
    //	    <select name="" id="">
    //	      <option value="1">1</option>
    //	      <option value="1">2</option>
    //	      <option value="1">3</option>
    //	    </select>
    //	  </div>
    //	  <div class="form-group col-md-4">
    //	    <label for="">CardId</label>
    //	    <select name="" id="">
    //	      <option value="1">1</option>
    //	      <option value="1">2</option>
    //	      <option value="1">3</option>
    //	    </select>
    //	  </div>
    //    fShowSerialNumInputDialog($(this));
  });

  /************************************************************************
   * Add reserved location trigger
   ************************************************************************/
  $(document).delegate(".addReservation", "click", function() {
    console.log("inside .addReservation click");
    const rowIndex = $(this).closest("tr");
    const position = $(this).position();

    console.log("rowIndex:", rowIndex + " position:", position);

    let quantity = $(this)
      .parent()
      .parent()
      .prev()
      .find(".newQuantity")
      .html();
    console.log($(this).siblings().length + "  " + quantity);

    if ($(this).siblings().length >= parseInt(quantity)) {
      bootBoxWarningAlert(
        "Increase quantity if you want to add more serial numbers."
      );
    } else {
      $(this).parent().append(`<div class="newSerialNoRow"> 
			
			<div class="clearfix">
				<span class="newSerialNo" ></span>  
				<span class="btn-group pull-right">
					<button class="btn btn-sm btn-warning addFaultOnSerialNumber" type="button" title="Add Fault"><i class="fa fa-plus" aria-hidden="true"></i></button>
					<button class="btn btn-sm btn--default deleteSerialNumRow" type="button" ><i class="fa fa-times" aria-hidden="true"></i></button>
				</span>	
			</div>
			
			<ul class="faultListOnSerialNum"></ul>
									
			</div>`);

      initializeEditablePref();
    }
  });

  /************************************************************************
   * Add fault to serial number event
   ************************************************************************/
  $(document).delegate(".addFaultOnSerialNumber", "click", function() {
    console.log($(this).parent());

    //Add fault the fault list
    $(this)
      .parent()
      .parent()
      .next(
        "ul.faultListOnSerialNum"
      ).append(`<li class="falultListItem"><strong>R</strong> - <span class="newReservedRack"></span> |
				<strong>S</strong> - <span class="newReservedSubrack"></span> |
				<strong>S</strong> - <span class="newReservedSlot"></span>  
		<span class="deleteFaultOnSerialNum"><i class="fa fa-trash" aria-hidden="true" ></i></span></li>`);

    var $faultList = $(this)
      .parent()
      .parent()
      .next("ul.faultListOnSerialNum");
    if ($faultList.children().length == 0) {
      $faultList.css({
        "border-top": "none"
      });
    } else {
      $faultList.css({
        "border-top": "1px solid #ddd"
      });
    }

    //Initialize values of newly added fault
    initializeEditablePref();
  });

  /************************************************************************
   * Delete selected rows from Available Inventory
   ************************************************************************/
  $(document).delegate("#inventoryQuantDeleteItemsTrgr", "click", function() {
    let checkedRows = $(
      "#availableInvTable tbody tr td input:checkbox:checked"
    );
    _.each(checkedRows, function(input) {
      console.log(
        $(input)
          .parent()
          .parent()
          .remove()
      );
    });
  });

  /************************************************************************
   * Delete fault from serial number fault list
   ************************************************************************/
  $(document).delegate(".deleteFaultOnSerialNum", "click", function() {
    //Check for fault count ; if 0 then remove top-border
    if (
      $(this)
        .parent()
        .siblings().length == 0
    ) {
      $(this)
        .parent()
        .parent()
        .css({
          "border-top": "none"
        });
    } else {
      $(this)
        .parent()
        .parent()
        .css({
          "border-top": "1px solid #ddd"
        });
    }

    //Delete fault from the fault list
    $(this)
      .parent()
      .remove();
  });

  /***********************************************************************
   * Delete Serial Number row
   ************************************************************************/
  $(document).delegate(".deleteSerialNumRow", "click", function() {
    //Delete fault from the fault list
    $(this)
      .parent()
      .parent()
      .parent()
      .remove();
  });

  /************************************************************************
   * Function to add inventory quant buttons in head panel & footer
   ************************************************************************/
  function fShowSerialNumInputDialog(serialNumInputTd) {
    console.log("fShowSerialNumInputDialog()");
  }

  /************************************************************************
   * Function to initialize stock table from DB data
   ************************************************************************/
  function initializeStockPrefDataFromDb(stockPrefDataArr) {
    console.log("## initializeStockPrefDataFromDb");
    for (let i = 0; i < stockPrefDataArr.length; i++) {
      $(".stockNode:eq(" + i + ")")
        .attr("title", "Select Node ")
        .tooltip()
        .editable({
          type: "select",
          value: stockPrefDataArr[i].NodeId,
          source: nodeArr,
          onblur: "submit",
          validate: function(value) {}
        });

      $(".stockCategory:eq(" + i + ")")
        .attr("title", "Select Category")
        .tooltip()
        .editable({
          type: "select",
          value: fGetCategory(stockPrefDataArr[i].Category),
          source: categoryArr,
          onblur: "submit",
          validate: function(value) {
            console.log($(this).html());
            categoryValidation(categoryArr[value - 1].text, $(this));
          }
        });

      $(".stockCardType:eq(" + i + ")")
        .attr("title", "Select Type ")
        .tooltip()
        .editable({
          type: "select",
          value: fGetCardType(
            stockPrefDataArr[i].CardType,
            stockPrefDataArr[i].Category
          ),
          source: fGetCardTypeArrforCategory(stockPrefDataArr[i].Category),
          onblur: "submit",
          validate: function(value) {}
        });

      $(".stockQuantity:eq(" + i + ")")
        .attr("title", "Enter Quantity ")
        .tooltip()
        .editable({
          type: "text",
          value: stockPrefDataArr[i].Quantity,
          onblur: "submit",
          validate: function(value) {}
        });

      $(".stockSerialNo:eq(" + i + ")")
        .attr("title", "Enter Serial No ")
        .tooltip()
        .editable({
          type: "text",
          value: stockPrefDataArr[i].SerialNum,
          onblur: "submit",
          validate: function(value) {}
        });
    }

    // var j = 0;
    // for (let s = 0; s < stockPrefDataArr.length; s++) {
    // 	if (!stockPrefDataArr[s].SerialNum == "") {
    // 		console.log("stockPrefDataArr[s].SerialNum", stockPrefDataArr[s].SerialNum)
    // 		$(".stockSerialNo:eq(" + j++ + ")").attr("title", 'Enter Serial No').tooltip().editable({
    // 			type: 'text',
    // 			value: stockPrefDataArr[s].SerialNum,
    // 			onblur: 'submit',
    // 			validate: function (value) {
    // 			}
    // 		});
    // 	}

    // }
  }

  /************************************************************************
   * Function to add inventory quant buttons in head panel & footer
   ************************************************************************/
  function fInventoryQuantHeadPanel() {
    console.log("fInventoryQuantHeadPanel()");

    $("#preferenceModal .genericModalBodyHeadPanel").empty()
      .append(`<div class="row">
				<div class="col-md-5 col-md-offset-4">
				<button class="btn btn--default" id="inventoryQuantAddItemTrgr"><i class="fa fa-plus" aria-hidden="true"></i> Add Item</button>
				<button class="btn btn--default" id="inventoryQuantDeleteItemsTrgr"><i class="fa fa-trash" aria-hidden="true"></i> Delete Selected Items</button>
				</div>
		</div>`);

    $("#preferenceModal .genericModalFooter").empty().append(`<div class="row">
				<div class="col-md-8 col-md-offset-2 d-flex justify-content-center">
				<button class="btn btn--default" type="button" id="inventoryQuantSetTrgr"><i class="fa fa-floppy-o" aria-hidden="true"></i> Set Inventory</button>
				<button class="btn btn--default" data-dismiss="modal"><i class="fa fa-times" aria-hidden="true"></i> Close</button>
				</div>
		</div>`);
  }

  /* #############################################################################
   * >>>>>>>>>>>>>>>>>>>>>>    Allocation Exception       <<<<<<<<<<<<<<<<<<<<<<<<<
   * #############################################################################*/

  /************************************************************************
   * Trigger for adding new exception
   ************************************************************************/
  $(document).delegate("#allocationAddExceptionTrgr", "click", function() {
    console.log("inside allocationAddExceptionTrgr click");
    //Add new row in #parametricPreferenceTable
    fAddAllocationExcTableRow();
  });

  /************************************************************************
   * Trigger for saving exceptions
   ************************************************************************/
  $(document).delegate("#allocationExcSetTrgr", "click", function() {
    console.log("inside allocationExcSetTrgr click");
    //Add new row in #parametricPreferenceTable
    fSaveAllocationExcPref();
    overlayOn("Saving Exceptions", parentSelector);
  });

  /************************************************************************
   * Delete selected rows from Inventory Exception
   ************************************************************************/
  $(document).delegate("#allocationDeleteExceptionTrgr", "click", function() {
    let checkedRows = $(
      "#invExceptionTable tbody tr td input:checkbox:checked"
    );
    _.each(checkedRows, function(input) {
      console.log(
        $(input)
          .parent()
          .parent()
          .remove()
      );
    });
  });

  /************************************************************************
   * Function to add new exception
   ***********************************************************************/
  function fAddAllocationExcTableRow() {
    console.log("fAddAocationExcTableRow()");
    let rowCount = $("#invExceptionTable tbody tr").length;

    console.log("Number of rows :" + rowCount);

    let template = "";
    template += fGetAllocationExcRowTemplate();

    // template += `<tr><td><input type="checkbox" id="deleteRowId" /> <span class="checkbox"></span></td>`;
    // template += "<td><span class='newNode'></span></td>";
    // template += "<td><span class='newExceptionType'></span></td>";
    // template += `<td><span class="newCardType"></span></td>
    // 	<td><span class="newRack"></span></td>
    // 	<td><span class="newSubRack"></span></td>
    // 	<td><span class="newSlot"></span></td>
    // 	<td><span class="newPort"></span></td>
    // 	<td><span class="newSerialNo"></span></td>
    // 	<td><img id="deleteRow" src="images/General/Trash-26.png" class="img-rounded" alt="Delete"></td>
    // 	</tr>`;
    //console.log(template);
    const newRow = $(template);
    //console.log(newRow);
    $("#invExceptionTable tbody").append(newRow);

    initializeEditablePref();
  }

  /***************************************************************
   * Function to save allocation exceptions to db
   ************************************************************************/
  function fSaveAllocationExcPref() {
    var $headers = $("#invExceptionTable th").not(
      "#invExceptionTable th:first-child,#invExceptionTable th:last-child"
    );
    var exceptionPrefRows = [];
    $("#invExceptionTable tbody tr").each(function(index) {
      //console.log("New Client Row",$(this));
      var $cells = $(this)
        .find("td")
        .not("td:first-child,td:last-child");
      exceptionPrefRows[index] = {};
      $cells.each(function(cellIndex) {
        if ($($headers[cellIndex]).html() == "Serial No (Optional)") {
          if (
            $(this)
              .find(".editable")
              .html() == "Empty"
          )
            exceptionPrefRows[index][$($headers[cellIndex]).html()] = "";
          else
            exceptionPrefRows[index][$($headers[cellIndex]).html()] = $(this)
              .find(".editable")
              .html();
        } else
          exceptionPrefRows[index][$($headers[cellIndex]).html()] = $(this)
            .find(".editable")
            .html();
      });
    });

    console.log("exceptionPrefRows::", exceptionPrefRows);

    overlayOn("Saving Inventory Priority", parentSelector);
    let exceptionPrefPostJsonData = jsonPostObject();
    exceptionPrefPostJsonData.ExceptionPreference = exceptionPrefRows;
    console.log(
      "exceptionPrefPostJsonData Post Obj ::",
      exceptionPrefPostJsonData
    );

    serverPostMessage("saveAllocationExcPref", exceptionPrefPostJsonData)
      .then(function(data) {
        console.log("Exception Set Success");
        console.log(data);
        overlayOff("Saved Exceptions Succesfully", parentSelector);
      })
      .catch(function(e) {
        overlayOff("Save Failure", parentSelector);
        console.log("fail", e);
      });
  }

  /************************************************************************
   * Add template for header and footer panel in case of Exception tab
   ************************************************************************/
  function fAllocationExcHeadPanel() {
    console.log("fAllocationExcHeadPanel()", $(".genericModalBodyHeadPanel"));

    let infoMessage = `<div class="col-md-12">
							<p class="text-center info-container">These exceptions will be used to add 'RESERVED CARD' on slots provided. </p>
						</div>`;

    $("#preferenceModal .genericModalBodyHeadPanel").empty()
      .append(`<div class="row">
				<div class="col-md-8 col-md-offset-2 d-flex justify-content-center">
				<button class="btn btn--default" id="allocationAddExceptionTrgr"><i class="fa fa-plus" aria-hidden="true"></i> Add Exception</button>
				<button class="btn btn--default" id="allocationDeleteExceptionTrgr"><i class="fa fa-trash" aria-hidden="true"></i> Delete Exception</button>
				</div>
				${infoMessage}
		</div>`);

    $("#preferenceModal .genericModalFooter").empty().append(`<div class="row">
				<div class="col-md-8 col-md-offset-2 d-flex justify-content-center">
				<button class="btn btn--default" type="button" id="allocationExcSetTrgr" ><i class="fa fa-floppy-o" aria-hidden="true"></i> Set Exceptions</button>
				<button class="btn btn--default" data-dismiss="modal"><i class="fa fa-times" aria-hidden="true"></i> Close</button>
				</div>
		</div>`);
  }

  /************************************************************************
   * Returns template for allocation exception row
   ************************************************************************/
  function fGetAllocationExcRowTemplate() {
    return `<tr><td><input type="checkbox" id="deleteRowId" /> <span class="checkbox"></span></td>
		<td><span class='newNode'></span></td>
		<td><span class='newExceptionType'></span></td>
		<td><span class="newExceptionCardType"></span></td>
			<td><span class="newRack"></span></td>
			<td><span class="newSubRack"></span></td>
			<td><span class="newSlot"></span></td>
			<td><span class="newPort"></span></td>
			<td><span class="newSerialNo"></span></td>		
			<td><img id="deleteRow" src="images/General/Trash-26.png" class="img-rounded" alt="Delete"></td>
			</tr>`;
  }

  /************************************************************************
   * Returns template for allocation exception row from DB
   ************************************************************************/
  function fGetAllocationExcRowTemplateDb() {
    return `<tr><td><input type="checkbox" id="deleteRowId" /> <span class="checkbox"></span></td>
		<td><span class='allocationExcNode'></span></td>
		<td><span class='allocationExcExceptionType'></span></td>
		<td><span class="allocationExcCardType"></span></td>
			<td><span class="allocationExcRack"></span></td>
			<td><span class="allocationExcSubRack"></span></td>
			<td><span class="allocationExcSlot"></span></td>
			<td><span class="allocationExcPort"></span></td>
			<td><span class="allocationExcSerialNo"></span></td>		
			<td><img id="deleteRow" src="images/General/Trash-26.png" class="img-rounded" alt="Delete"></td>
			</tr>`;
  }

  /************************************************************************
   * Initialiaze allocation exceptions fetched from DB
   ************************************************************************/
  function initializeAllocationExcDataFromDb(allocationExcDataArr) {
    console.log("## initializeAllocationExcDataFromDb()");
    for (var i = 0; i < allocationExcDataArr.length; i++) {
      $(".allocationExcNode:eq(" + i + ")")
        .attr("title", "Select Node ")
        .tooltip()
        .editable({
          type: "select",
          value: allocationExcDataArr[i].NodeId,
          source: nodeArr,
          onblur: "submit",
          validate: function(value) {
            console.log($(this).html());
            //nodeValidationParamPref(value, $(this));
          }
        });

      $(".allocationExcExceptionType:eq(" + i + ")")
        .attr("title", "Select Exception Type")
        .tooltip()
        .editable({
          type: "select",
          value: allocationExcDataArr[i].ExceptionType,
          source: ExceptionTypeArr,
          onblur: "submit",
          validate: function(value) {
            console.log($(this).html());
          }
        });

      $(".allocationExcCardType:eq(" + i + ")")
        .attr("title", "Select Card Type ")
        .tooltip()
        .editable({
          type: "select",
          value: allocationExcDataArr[i].CardType,
          source: ExceptionCardTypeArr,
          onblur: "submit",
          validate: function(value) {}
        });

      $(".allocationExcRack:eq(" + i + ")")
        .attr("title", "Select rack ")
        .tooltip()
        .editable({
          type: "select",
          value: RackArr[Number(allocationExcDataArr[i].Rack) - 1].value,
          source: RackArr,
          onblur: "submit",
          validate: function(value) {}
        });

      $(".allocationExcSubRack:eq(" + i + ")")
        .attr("title", "Select subrack ")
        .tooltip()
        .editable({
          type: "select",
          value: SubRackArr[Number(allocationExcDataArr[i].SubRack) - 1].value,
          source: SubRackArr,
          onblur: "submit",
          validate: function(value) {}
        });

      $(".allocationExcSlot:eq(" + i + ")")
        .attr("title", "Select slot ")
        .tooltip()
        .editable({
          type: "select",
          value: SlotArr[Number(allocationExcDataArr[i].Slot) - 1].value,
          source: SlotArr,
          onblur: "submit",
          validate: function(value) {}
        });

      $(".allocationExcPort:eq(" + i + ")")
        .attr("title", "Select Port ")
        .tooltip()
        .editable({
          type: "select",
          value: PortArr[Number(allocationExcDataArr[i].Port) - 1].value,
          source: PortArr,
          onblur: "submit",
          validate: function(value) {}
        });

      $(".allocationExcSerialNo:eq(" + i + ")")
        .attr("title", "Enter Serial No")
        .tooltip()
        .editable({
          type: "text",
          value: allocationExcDataArr[i].SerialNum,
          onblur: "submit",
          validate: function(value) {}
        });
    }
  }

  $("#preferenceModal .modal-dialog").resizable({
    //alsoResize: ".modal-dialog",
    minHeight: 300,
    minWidth: 300
  });
  $("#preferenceModal .modal-dialog").draggable();

  $("#preferenceModal").on("show.bs.modal", function() {
    $(this)
      .find(".modal-body")
      .css({
        "max-height": "100%"
      });
  });

  // $('#preferenceModal').modal({ backdrop: 'static', keyboard: true });//disable click on black area/ esc key
})(window.jQuery);
