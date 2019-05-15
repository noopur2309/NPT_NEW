/*
 * $('#DemandMatrixTable').find('tr').slice(0,5).click( function(){
 * //console.log($(this).next()); console.log($(this)[0].rowIndex); var
 * path=$(this)["0"].cells["2"].innerText; var pathNodes=path.split(","); var
 * i=0; while(i<pathNodes.length) { console.log(pathNodes[i]); i++; }
 * if(selectedRow) removeCommonColumnsBkg(selectedRow) selectedRow=$(this);
 * selectedRow.css("background","#c4c4c4"); showPath(pathNodes);
 *
 * //alert('You clicked row '+ ($(this).index()+1) ); });
 */

/*
 * $('#DemandMatrixTable').click( function(e){
 *
 * var myCol = $(this).index(); var $tr = $(this).closest('tr'); var myRow =
 * $tr.index();
 *
 *
 *
 * var $pathTableHeader=$(e.target);
 *
 * });
 */

/*
 * $('td').click(function() { var myCol = $(this).index(); var $tr =
 * $(this).closest('tr'); var myRow = $tr.index();
 * console.log("Col:"+myCol+"Row:"+myRow); var allMainrows=$("#DemandMatrixTable
 * tbody tr").each(function(index) { if() console.log($(this)); });
 * if($(this[rowspan="4"])) { console.log(this); } //var
 * tr=$(allMainrows);allMainrows //console.log(allMainrows); });
 */
var selectedColumn = null,
  selectedRow = 0,
  circuitViewJsonObj = new Object(),
  CapacitySum = 0;
var src = null,
  dest = null;

$("body").delegate(".commonRow", "click", function() {
  $(".demands-circuit-view").css("display", "block");
  //$("#propertyCollapse").css("display","block");
  //$("div.tabcontents").css("height","304px");

  var workingCheck = 0,
    protectionCheck = 0,
    restorationCheck = 0,
    restorationColorFirst = 0;
  // $(this).css("background","#333");
  console.log(
    "clicked on commonRow class element and now calling showpath for all possible paths......"
  );
  var isRejectedPath;

  if (selectedColumn) removePathColumnHighlight();

  if (highlightedPathNodes.length != 0) removeNodeHighlight();

  removeCommonColumnsBkg();
  hidePath();
  commonRowEventView($(this));

  //	if(nptGlobals.faultElementsCount>0)
  //	{
  //	commonRowEventFaultAnalysis($(this));
  //	}
  //	else
  //	{

  //	console.log($(this)["0"].attributes["0"].value);
  //	var rowSpan=$(this)["0"].attributes["0"].value;
  //	var $tr = $(this).closest('tr');

  //	$tr.find('td.commonRow').css("background-color","#dff0d8");

  //	// $tr.css("background","#c4c4c4");
  //	// console.log($tr);

  //	var demandId=$tr.find('td.demandId').html();

  //	generateCircuitViewforDemandId(demandId); // generate circuit info for
  //	// particular demand
  //	isRejectedPath=$tr.find('td#pathColumn').hasClass('rejectedWithError');
  //	console.log("This Path is rejected :"+isRejectedPath);
  //	console.log($tr.find('td#pathColumn'));

  //	if(!isRejectedPath)
  //	{
  //	$tr.find('td#pathColumn').css({"color":nptGlobals.workingPathColor,"font-size":"18px"});
  //	console.log()
  //	}

  //	var path=$tr.find('td#pathColumn').html();
  //	src=$tr.find('td.srcNodeClass').html();
  //	dest=$tr.find('td.destNodeClass').html();

  //	highlightSrcandDestNode(src,dest);

  //	var pathNodesArr= getpathNodesArr(path);

  //	var ColorsArrayFaultAnalysis=["Color1","Color2","Color3","Color4"];

  //	if(!isRejectedPath)
  //	showPath(pathNodesArr,ColorsArrayFaultAnalysis[0]);
  //	else showPath(pathNodesArr,"Color6");

  //	for(var i=0;i<rowSpan-1;i++)
  //	{
  //	// console.log($tr.next());
  //	$tr=$tr.next();
  //	/*
  //	* if(selectedRow) selectedRow=$tr; removeCommonColumnsBkg();
  //	* selectedRow.css("background","#c4c4c4");
  //	*/
  //	// $tr.css("background","#c4c4c4");
  //	isRejectedPath=$tr.find('td#pathColumn').hasClass('rejectedWithError');
  //	//console.log($tr.find('td#pathColumn'));
  //	console.log("This Path is rejected :"+isRejectedPath);
  //	if(i==0)
  //	{
  //	if(!isRejectedPath)
  //	$tr.find('td#pathColumn').css({"color":nptGlobals.protectionPathColor,"font-size":"18px"});
  //	}
  //	else {
  //	if(restorationColorFirst)
  //	{
  //	if(!isRejectedPath)
  //	$tr.find('td#pathColumn').css({"color":nptGlobals.restorationPath2Color,"font-size":"18px"});
  //	}
  //	else {
  //	{
  //	if(!isRejectedPath)
  //	$tr.find('td#pathColumn').css({"color":nptGlobals.restorationPathColor,"font-size":"18px"});
  //	restorationColorFirst=1;
  //	}
  //	}
  //	}
  //	path=$tr.find('td#pathColumn').html();
  //	pathNodesArr= getpathNodesArr(path);
  //	if(!isRejectedPath)
  //	showPath(pathNodesArr,ColorsArrayFaultAnalysis[i+1]);
  //	else
  //	showPath(pathNodesArr,"Color6"); //For rejected paths Color6 is used , light grey ,disabled

  //	}
  //	}
});

function commonRowEventView(commonRow) {
  var workingCheck = 0,
    protectionCheck = 0,
    restorationCheck = 0;
  console.log(commonRow["0"].attributes["0"].value);
  var rowSpan = commonRow["0"].attributes["0"].value;
  var $tr = commonRow.closest("tr");

  $tr.find("td.commonRow").css("background-color", "#dff0d8");

  src = $tr.find("td.srcNodeClass").html();
  dest = $tr.find("td.destNodeClass").html();

  highlightSrcandDestNode(src, dest);
  // $tr.css("background","#c4c4c4");
  // console.log($tr);

  var demandId = $tr.find("td.demandId").html();

  // generate circuit info for particular demand
  generateCircuitViewforDemandId(demandId);

  for (var numOfPaths = 0; numOfPaths < rowSpan; numOfPaths++) {
    //		var classname=$($tr.find('td#pathColumn'))[0].className;
    //		console.log($($tr.find('td#pathColumn')));
    //		console.log($($tr.find('td#pathColumn'))[0].className);

    if ($tr.find("td#pathColumn").hasClass("rejectedWithError")) {
      $tr
        .find("td#pathColumn")
        .css({ color: nptGlobals.rejectedPathErrorColor, "font-size": "18px" });
      var path = $tr.find("td#pathColumn").html();
      var pathNodesArr = getpathNodesArr(path);
      showPath(pathNodesArr, "Color6");
    } else if ($tr.find("td#pathColumn").hasClass("fault")) {
      var path = $tr.find("td#pathColumn").html();
      var pathNodesArr = getpathNodesArr(path);
      showPath(pathNodesArr, "Color5");
    } else if ($tr.find("td#pathColumn").hasClass("noPathFoundClass")) {
      //$tr.find('td#pathColumn').css({"color":nptGlobals.rejectedPathErrorColor,"font-size":"18px"});
      //var path=$tr.find('td#pathColumn').html();
      //var pathNodesArr= getpathNodesArr(path);
      //showPath(pathNodesArr,"Color6");
      console.log("No paths found for this demand");
    } else {
      if (workingCheck == 0) {
        $tr
          .find("td#pathColumn")
          .css({ color: nptGlobals.workingPathColor, "font-size": "18px" });
        var path = $tr.find("td#pathColumn").html();
        var pathNodesArr = getpathNodesArr(path);
        showPath(pathNodesArr, "Color1");
        workingCheck = 1;
      } else if (protectionCheck == 0) {
        $tr
          .find("td#pathColumn")
          .css({ color: nptGlobals.protectionPathColor, "font-size": "18px" });
        var path = $tr.find("td#pathColumn").html();
        var pathNodesArr = getpathNodesArr(path);
        showPath(pathNodesArr, "Color2");
        protectionCheck = 1;
      } else {
        var path = $tr.find("td#pathColumn").html();
        var pathNodesArr = getpathNodesArr(path);
        if (restorationCheck == 0) {
          $tr.find("td#pathColumn").css({
            color: nptGlobals.restorationPathColor,
            "font-size": "18px"
          });
          showPath(pathNodesArr, "Color3");
          restorationCheck = 1;
        } else {
          $tr.find("td#pathColumn").css({
            color: nptGlobals.restorationPath2Color,
            "font-size": "18px"
          });
          showPath(pathNodesArr, "Color4");
        }
      }
    }
    $tr = $tr.next();
  }

  if (workingCheck == 0) bootBoxWarningAlert("No Path Exists");
  else if (protectionCheck == 0)
    bootBoxWarningAlert("No Protection Path Exists");
}

$("body").delegate("#pathColumn", "click", function() {
  // $(this).css("background","#333");
  console.log("clicked on path column and now calling showpath......");
  removeCommonColumnsBkg();
  hidePath();
  fShowPathInfoBox(false); //Hide Path Info

  if (highlightedPathNodes.length != 0) removeNodeHighlight();

  if (selectedColumn) removePathColumnHighlight();

  selectedColumn = $(this);
  var isRejectedPath = selectedColumn.hasClass("rejectedWithError");
  console.log(selectedColumn);
  console.log("This Path is rejected :" + isRejectedPath);
  // selectedColumn.css("background","#44c4c4");
  selectedColumn.css({
    "border-bottom": "4px solid #777",
    "border-right": "4px solid #777"
  });

  // $(this).css("background","#44c4c4");

  var path = $(this).html();
  if (!$(this).hasClass("noPathFoundClass")) {
    var pathNodesArr = getpathNodesArr(path);
    //optical power for path
    fShowOpticalPowerForPath(pathNodesArr);

    highlightSrcandDestNode(
      pathNodesArr[0],
      pathNodesArr[pathNodesArr.length - 1]
    );

    for (
      var pathNodeNum = 0;
      pathNodeNum < pathNodesArr.length;
      pathNodeNum++
    ) {
      if (pathNodeNum != 0 && pathNodeNum != pathNodesArr.length - 1)
        highlightIntermediateNodes(pathNodesArr[pathNodeNum]);
    }
    //console.log("IsFault"+$(this).hasClass('fault'));
    //console.log("IsRejected"+$(this).hasClass('rejectedWithError'));
    //var classname=$(this)[0].className.split(' ');
    //console.log("classname ::"+classname[1])

    //if(classname.length>1 && classname[1]=='fault')
    if ($(this).hasClass("fault")) showPath(pathNodesArr, "Color5");
    else if ($(this).hasClass("rejectedWithError"))
      showPath(pathNodesArr, "Color6");
    else showPath(pathNodesArr, "Color4");
  }

  //Highlight regenerator node
  var regeneratorNodes = $(this)
    .next()
    .next()
    .html();

  //If there are regenerator nodes , then Highlight the nodes
  if (regeneratorNodes[0] != "-") {
    console.log("Regenartor Nodes Array :");
    console.log(regeneratorNodes);
    regeneratorNodesArr = regeneratorNodes.split(",");
    fHighlightRegeneratorNode(regeneratorNodesArr);
  }

  fShowPathInfoBox(true); //Show Path Info Box
});

//On click event of regenerator location will highlight all
//the nodes in reg Loc array

$("body").delegate("#regenLoc", "click", function() {
  // $(this).css("background","#333");
  var path = $(this).html();
  if (path == "-") {
    //If there is no regenerator
    console.log("No regenerator needed");
  } else {
    console.log("clicked on path column and now calling showpath......");
    removeCommonColumnsBkg();
    hidePath();

    if (highlightedPathNodes.length != 0) removeNodeHighlight();

    if (selectedColumn) removePathColumnHighlight();

    selectedColumn = $(this);
    // selectedColumn.css("background","#44c4c4");
    selectedColumn.css({
      "border-bottom": "4px solid #777",
      "border-right": "4px solid #777"
    });

    // $(this).css("background","#44c4c4");

    console.log("path" + path);
    if (path == "") var pathNodesArr = [];
    else var pathNodesArr = getpathNodesArr(path);

    for (
      var pathNodeNum = 0;
      pathNodeNum < pathNodesArr.length;
      pathNodeNum++
    ) {
      var Node = graph.getCell(getNodeById(pathNodesArr[pathNodeNum]));
      Node.attr(attrs.elementHighlighted);
      highlightedPathNodes.push(Node);
    }
  }
});

function fShowOpticalPowerForPath(path) {
  console.log("fShowOpticalPowerForPath(path)");
  if (nptGlobals.opticalPowerPathNodesArr.length > 0)
    fHideOpticalPowerPathNodes();
  var currNode, nextNode, currId, nextId, currDefaultId, nextDefaultId;
  var inPower,
    outPower,
    spanLoss = 0;
  var initialPower = "+1 to +5";
  //console.log("Path Array :");
  //console.log(path);
  for (var i = 0; i < path.length; i++) {
    if (i == 0) inPower = initialPower;
    else inPower = -spanLoss;
    outPower = 0;

    currDefaultId = getNodeById(path[i]);
    nptGlobals.opticalPowerPathNodesArr.push(path[i]);
    currNode = graph.getCell(currDefaultId);
    // console.log('Current Node :');
    // console.log(currNode);

    if (i + 1 < path.length) {
      nextDefaultId = getNodeById(path[i + 1]);
      //currDefaultId=curr.get('id');
      //nextDefaultId=next.get('id');
      var link = getLinkBetweenSrcandDestNodes(currDefaultId, nextDefaultId);
      //  console.log('Link between curr and next :');
      //  console.log(link);
      spanLoss = link.get("linkProperties").adjustableSpanLoss;
      //   console.log("spanLoss:"+spanLoss);
    }

    setPropertiesLabelNode(currNode, inPower, outPower);
    //setPropertiesLabelNode(curr,inPower,outPower);
  }
}

function setPropertiesLabelNode(cell, inP, outP) {
  console.log("Ndoe type in setPropertiesLabelNode():");
  console.log(cell.get("type"));
  var powerText = `In: ${inP} dbm \nOut: ${outP} dbm`;
  cell.attr(".props/text", powerText);
}

function generateCircuitViewforDemandId(demandId) {
  var template = "";
  //	template+=`<div class="alert alert-danger alert-dismissable fade in">
  //    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
  //    Select circuits to delete.
  //     </div>`;
  template += `<table class=" table table-bordered CircuitViewDemandTable">
		<thead>
		<tr>`;
  //template+=`<th><input type="checkbox" id="checkAllCircuitView"/>Select All</th>`;
  template += `<th></th>`;
  template += `<th>DemandId</th>
	<th>CircuitId</th>
		<th>Source Node</th>
		<th>Dest Node</th>
		<th>Service Type </th>
		<th>Capacity (G)</th>
		<th>Line Rate</th>
		<th>Multiplier</th>
		<th>Client Protection Type</th>
		<th>Protection Mechanism</th>
		<th>Channel Protection</th>
		<th>Path Type </th>
		<th>Color Preference</th>
		</tr>
		</thead>
		<tbody>`;
  // var
  // jsonStr={"view2":[{"ColourPreference":"Indigo#Green#Yellow","SrcNodeId":1,"ProtectionType":"1+1","NetowrkId":1,"ClientProtectionType":"OLP","DemandId":1,"CircuitId":1,"DestNodeId":2,"RequiredTraffic":10,"TrafficType":"Ethernet"},{"ColourPreference":"Indigo#Green#Yellow","SrcNodeId":1,"ProtectionType":"1+1","NetowrkId":1,"ClientProtectionType":"OLP","DemandId":3,"CircuitId":2,"DestNodeId":3,"RequiredTraffic":10,"TrafficType":"Ethernet"},{"ColourPreference":"Indigo#Green#Yellow","SrcNodeId":2,"ProtectionType":"1+1","NetowrkId":1,"ClientProtectionType":"OLP","DemandId":2,"CircuitId":3,"DestNodeId":3,"RequiredTraffic":10,"TrafficType":"Ethernet"}]}
  // ;
  var jsonArr = nptGlobals.DemandMatrixViewData.view2;
  CapacitySum = 0;
  let circuitsArr = jsonArr.filter(function(demand) {
    if (demand.DemandId == demandId) return true;
  });

  console.log("CircuitsArr", circuitsArr);

  for (var i = 0; i < circuitsArr.length; i++) {
    var state = circuitsArr[i].State + "RowClass";
    console.log("Row State", state);
    circuitViewJsonObj = circuitsArr[i];
    //			circuitViewJsonObj=fAppendDataFromViewOne(circuitsArr[i].DemandId);
    let label = getLabelTagsFromState(state);
    template += `<tr class="${state} CircuitViewOldCircuitRow"><td>`;
    if (state == nptGlobals.DeletedRowClassStr) template += `${label}`;
    else
      template += `${label}<input type="checkbox" class="deleteRowIdCircuitView"/>`;
    template += `</td><td>${circuitsArr[i].DemandId}</td>`;
    template += `<td class="OldCircuitId">${circuitsArr[i].CircuitId}</td>`;
    template += `<td>${circuitsArr[i].SrcNodeId}</td>`;
    template += `<td>${circuitsArr[i].DestNodeId}</td>`;
    template += `<td class="circuit-view-traffic-type">${
      circuitsArr[i].TrafficType
    }</td>`;
    template += `<td>${circuitsArr[i].RequiredTraffic}</td>`;
    template += `<td>${circuitsArr[i].LineRate}</td>`;
    template += `<td>1</td>`;
    template += `<td>${circuitsArr[i].ProtectionType}</td>`;
    template += `<td>${circuitsArr[i].ProtectionMechanism}</td>`;
    template += `<td>${circuitsArr[i].ChannelProtection}</td>`;
    template += `<td>${circuitsArr[i].PathType}</td>`;
    template += `<td>${circuitsArr[i].ColourPreference}</td></tr>`;

    CapacitySum += parseInt(circuitsArr[i].RequiredTraffic);
  }

  console.log("Capacity sum :" + CapacitySum);
  template += "</tbody></table>";

  if (sessionStorage.getItem("NetworkState") == nptGlobals.BrownFieldStr) {
    // template += `<button type="button" id="SaveDemandCircuitViewTableRowBtn"  class="btn-sm btn btn--default"><i class="fa fa-floppy-o" aria-hidden="true"></i> Save New Circuits</button>`;
    //		template+=`<button type="button" id="SaveModifiedCircuitViewTableRowBtn"  class="btn btn-sm btn--default"><i class="fa fa-floppy-o" aria-hidden="true"></i> Save Circuits</button>`;
    template += `<button type="button" id="DeleteCircuitCircuitViewTableRowBtn"  class="btn btn-sm btn--default"><i class="fa fa-floppy-o" aria-hidden="true"></i> Delete Circuits</button><hr>`;

    // template += `<h4>Add Client:</h4><div id="CircuitViewAddClientFormDiv"><form >
    //   <div class="form-group col-md-4">
    //     <label for="ServiceTypeCircuitView">Service Type</label>
    //     <select type="select" class="form-control" id="ServiceTypeCircuitView" name="ServiceTypeCircuitView">
    // 	<option >Ethernet</option>
    // 	<option >OTU2</option>
    // 	</select>
    //   </div>
    //   <div class="form-group col-md-4">
    //     <label for="CapacityCircuitView">Capacity</label>
    //     <select type="select" class="form-control" id="CapacityCircuitView" name="CapacityCircuitView">
    //     <option >1</option>
    // 	<option >1.25</option>
    // 	<option >2.5</option>
    // 	<option >10</option>
    // 	<option >100</option>
    // 	</select>
    // 	<p class="errorCapacityCircuitView text-danger"></p>
    //   </div>
    //   <div class="form-group col-md-4">
    //     <label for="MultiplierCircuitView">Multiplier</label>
    // 	<input type="number" min="1" max="10" class="form-control" name="MultiplierCircuitView"	onfocus="this.placeholder = ''"	placeholder="Multiplier" id="MultiplierCircuitView" value="1"></input>
    //   </div>
    //   <button type="button"  id="AddClientCircuitViewTableRowBtn" class="btn"><i class='fa fa-plus' aria-hidden='true'> </i> Add New Client</button>

    // </form>
    // </div>`;
    //	template+=``;
  }

  $(".demands-circuit-view__content")
    .empty()
    .append(template);

  console.log("@@@@@@@@@@@@ ", $("#demands-circuit").height());
  $("#DemandMatrixTable").css({
    "margin-bottom": $("#demands-circuit").height()
  });

  /**
   * Editable service type
   */
  // initializeCircuitViewEditable(circuitsArr);
}

function initializeCircuitViewEditable(circuitsArr) {
  circuitsArr.map(function(circuit, index) {
    // console.log("Index:",index,"Circuit:",circuit)
    let ServiceTypeSrc = nptGlobals.fGetServiceTypeSrc(
      circuit.RequiredTraffic,
      circuit.LineRate
    );
    // console.log("Service type Src",ServiceTypeSrc);
    $(".circuit-view-traffic-type:eq(" + index + ")")
      .attr("title", "Edit ")
      .editable({
        type: "select",
        value: circuit.TrafficType,
        source: ServiceTypeSrc,
        onblur: "submit"
      });
  });
}

/* *******************
 *Function to get Circuit Data for a demand Id
 *************************/
var fAppendDataFromViewOne = function(DemandId) {
  console.log("fAppendDataFromViewOne called with DemandId :" + DemandId);
  var DemandViewOneArr = nptGlobals.DemandMatrixViewData.view1;
  console.log("DemandViewOneArr :", DemandViewOneArr);
  for (key in DemandViewOneArr) {
    console.log("DemandViewOneArr Object :", DemandViewOneArr[key]);
    if (parseInt(DemandViewOneArr[key].DemandId) == parseInt(DemandId)) {
      circuitViewJsonObj.LineRate = DemandViewOneArr[key].lineRate;
      circuitViewJsonObj.PathType = DemandViewOneArr[key].PathType;
    }
  }
};

/* *******************
 *Function to add new client to demand
 *************************/
// function fAddNewClient() {
// 	var template = "";
// 	var CircuitViewAddClientFormData = $("#CircuitViewAddClientFormDiv form").serializeArray();
// 	console.log("CircuitViewAddClientFormData::", CircuitViewAddClientFormData);
// 	console.log("circuitViewJsonObj::", circuitViewJsonObj);

// 	var Capacity = parseInt(CircuitViewAddClientFormData[1].value), Multiplier = parseInt(CircuitViewAddClientFormData[2].value)
// 		, ServiceType = CircuitViewAddClientFormData[0].value;
// 	var capacityInput = Capacity * Multiplier;
// 	console.log("capacityInput ::" + capacityInput);
// 	CapacitySum += parseInt(capacityInput);
// 	console.log("Capacity sum :" + CapacitySum);

// 	if (CapacitySum > 100) {
// 		CapacitySum -= parseInt(capacityInput);
// 		bootBoxWarningAlert("Total Capacity cannot be greater than 100G.Create new circuits from the Circuit tab", 1000);
// 	}
// 	else {
// 		var state = nptGlobals.NewRowClassStr;
// 		console.log("Row State", state);
// 		template += `<tr class="NewClientCircuitView ${state}" title="Double click to delete newly added client."><td>`;
// 		template += getLabelTagsFromState(state);
// 		template += `</td><td>${circuitViewJsonObj.DemandId}</td>
// 			<td>${circuitViewJsonObj.SrcNodeId}</td>
// 			<td>${circuitViewJsonObj.DestNodeId}</td>
// 			<td>${ServiceType}</td>
// 			<td>${Capacity}</td>
// 			<td>${circuitViewJsonObj.LineRate}</td>
// 			<td>${Multiplier}</td>
// 			<td>${circuitViewJsonObj.ProtectionType}</td>
// 			<td>${circuitViewJsonObj.ProtectionMechanism}</td>
// 			<td>${circuitViewJsonObj.ChannelProtection}</td>
// 			<td>${circuitViewJsonObj.PathType}</td>
// 			<td>${circuitViewJsonObj.ColourPreference}</td>
// 			</tr>`;

// 		$(".CircuitViewDemandTable tbody").append(template);
// 	}
// };

/* *******************
 *Function to add new client to demand
 *************************/
// function fSaveNewCircuitsinDemand() {
// 	console.log("Inside fSaveNewDemands ");
// 	var $headers = $(".CircuitViewDemandTable th").not('.CircuitViewDemandTable th:first-child');
// 	var NewClientRows = [], multiplier;
// 	$(".CircuitViewDemandTable tbody tr.NewClientCircuitView").each(function (index) {
// 		//console.log("New Client Row",$(this));
// 		var $cells = $(this).find("td").not('td:first-child');
// 		NewClientRows[index] = {};
// 		$cells.each(function (cellIndex) {
// 			NewClientRows[index][$($headers[cellIndex]).html()] = $(this).html();
// 			if ($($headers[cellIndex]).html() == "Multiplier")
// 				multiplier = $(this).html();
// 		});
// 		NewClientRows[index]["CircuitIdArray"] = []; // New circuit case : -1
// 		for (var i = 0; i < multiplier; i++)
// 			NewClientRows[index]["CircuitIdArray"].push("-1");
// 	});

// 	console.log("NewClientRows::", NewClientRows);
// 	fSaveNewCircuitsinDemandAjaxCall(NewClientRows);
// }

/* *****************************************************************
 *Function to save new client to demand table and Circuit table **
 ********************************************************************/
// function fSaveNewCircuitsinDemandAjaxCall(NewClientRows) {
// 	var saveCircuitPostData = jsonPostObject();
// 	var demandId = NewClientRows[0]["DemandId"];

// 	saveCircuitPostData.circuitRows = NewClientRows;
// 	saveCircuitPostData.Mode = 1; // 1 : New Client Add in Circuit View

// 	//console.log(JSON.parse(myObj));
// 	console.log("Json Array of Circuit Table rows for sending to Backend\n" + JSON.stringify(saveCircuitPostData));
// 	//console.log(json);

// 	fShowClientServerCommunicationModal("Adding new client in demand " + demandId);
// 	disableCloseButton(true);

// 	//Ajax request
// 	$.ajax({

// 		type: "POST",
// 		contentType: "application/json",
// 		headers: {
// 			Accept: "application/json; charset=utf-8",
// 			"Content-Type": "application/json; charset=utf-8"
// 		},
// 		url: "/circuitInputSave",
// 		cache: false, // Force requested pages not to be cached by the browser
// 		processData: false, // Avoid making query string instead of JSON
// 		data: JSON.stringify(saveCircuitPostData),
// 		timeout: 600000,
// 		success: function (data) {
// 			console.log("Circuit was successfully added to Demand ", demandId + " ", data);
// 			fShowSuccessMessage("Circuit was successfully added to Demand " + demandId);

// 			getDemandOutputMatrix();
// 			$(".demands-circuit-view__content").empty().append("<p class='warning'>Click on a demand to get updated circuits.</p>")
// 			//generate circuit info for particular demand
// 			//generateCircuitViewforDemandId(demandId);

// 		},
// 		error: function (e) {
// 			fShowFailureMessage("Circuit couldn't be Saved.");
// 			console.log("Client save failure" + e);
// 		}
// 	});
// }

/* *******************
 *Function to delete complete demand
 *************************/
//function fDeleteDemandCircuitView(demandId){
//	var deleteDemandPostData=jsonPostObject();
//	deleteDemandPostData.DemandId=demandId;
//
//	console.log("deleteDemandPostObj::",JSON.stringify(deleteDemandPostData));
//	//Ajax request
//	$.ajax({
//		type: "POST",
//		contentType: "application/json",
//		headers: {
//			Accept : "application/json; charset=utf-8",
//			"Content-Type": "application/json; charset=utf-8"
//		} ,
//		url: "/DeleteDemand",
//		cache: false, // Force requested pages not to be cached by the browser
//		processData: false, // Avoid making query string instead of JSON
//		data: JSON.stringify(deleteDemandPostData),
//		timeout: 600000,
//		success: function (data) {
//			console.log("Demand Deleted Successfully"+data);
//			bootBoxAlert("Demand deleted successfully");
//		},
//		error: function (e) {
//			console.log("Demand Delete failure"+e);
//			bootBoxAlert("Demand could not be deleted successfully");
//		}
//	});
//}

/* *******************
 *Function to delete client from demand
 *************************/
function fDeleteCircuitFromDemand() {
  console.log("Inside fDeleteCircuitFromDemand ");
  var $headers = $(".CircuitViewDemandTable th").not(
    ".CircuitViewDemandTable th:first-child"
  );
  var ClientRows = [],
    multiplier;
  $("tr.CircuitViewClientSelected").each(function(index) {
    //console.log("New Client Row",$(this));
    var $cells = $(this)
      .find("td")
      .not("td:first-child,td:hidden");
    ClientRows[index] = {};
    $cells.each(function(cellIndex) {
      ClientRows[index][$($headers[cellIndex]).html()] = $(this).html();
      if ($($headers[cellIndex]).html() == "Multiplier")
        multiplier = $(this).html();
    });
    if ($(this).hasClass("CircuitViewOldCircuitRow")) {
      //console.log("Old Circuit Id::"+$(this).find('.OldCircuitId').html());
      //ClientRows[index]["CircuitIdArray"]=$(this).find('.OldCircuitId').html().split(",");
      ClientRows[index]["CircuitId"] = $(this)
        .find(".OldCircuitId")
        .html();
    } else {
      //ClientRows[index]["CircuitIdArray"]=[]; // New circuit case : -1
      //for(var i=0;i<multiplier;i++)
      //ClientRows[index]["CircuitIdArray"].push("-1");
      ClientRows[index]["CircuitId"] = -1;
    }
    //console.log("Circuit to be deleted Id:"+ClientRows[index]["CircuitId"]);
  });

  console.log("Length of Selected Rows::", ClientRows.length);
  console.log("Selected Rows::", ClientRows);

  if (ClientRows.length > 0) {
    let content = "Are you sure you want to delete circuits from this demand?";
    openDialog(
      nptGlobals.getDialogTypes().alert,
      content,
      fDeleteCircuitFromDemandAjaxCall,
      ClientRows
    );
    // var dialog = new joint.ui.Dialog({
    // 	type:'alert',
    // 	width: 300,
    // 	title: 'Confirm',
    // 	content: 'Are you sure you want to delete circuits from this demand?',
    // 	buttons:[
    // 		{action:'Yes',content:'Yes'},
    // 		{action:'No',content:'No'},
    // 	],
    // 	draggable:true
    // });

    // dialog.on('action:Yes', function() {
    // 	fDeleteCircuitFromDemandAjaxCall(ClientRows);
    // 	dialog.close();
    // });

    // dialog.on('action:No',function() {
    // 	dialog.close();
    // });
    // dialog.open();
  } else bootBoxWarningAlert("Select a client to delete from this demand.");

  //	var totalCircuitCount=$(".CircuitViewOldCircuitRow").length,
  //	totalSelectedCircuitCount=ClientRows.length;
  //	console.log("totalCircuitCount::"+totalCircuitCount+" totalSelectedCircuitCount::",totalSelectedCircuitCount);
  //	console.log("NewClientRows::",ClientRows);
  //	if(totalCircuitCount==totalSelectedCircuitCount)
  //		{
  //		console.log("Delete demand too");
  //		// fDeleteDemandCircuitView();
  //		}
}

/* *******************
 *Function to save modified client from demand
 *************************/
function fSaveModifiedCircuitCircuitView() {
  console.log("Inside fSaveModifiedCircuitCircuitView ");
  var $headers = $(".CircuitViewDemandTable th").not(
    ".CircuitViewDemandTable th:first-child"
  );
  var ClientRows = [],
    multiplier;
  $(".CircuitViewDemandTable tr").each(function(index) {
    //console.log("New Client Row",$(this));
    var $cells = $(this)
      .find("td")
      .not("td:first-child,td:hidden");
    ClientRows[index] = {};
    $cells.each(function(cellIndex) {
      ClientRows[index][$($headers[cellIndex]).html()] = $(this).html();
      if ($($headers[cellIndex]).html() == "Multiplier")
        multiplier = $(this).html();
    });
    if ($(this).hasClass("CircuitViewOldCircuitRow")) {
      //console.log("Old Circuit Id::"+$(this).find('.OldCircuitId').html());
      //ClientRows[index]["CircuitIdArray"]=$(this).find('.OldCircuitId').html().split(",");
      ClientRows[index]["CircuitId"] = $(this)
        .find(".OldCircuitId")
        .html();
    } else {
      //ClientRows[index]["CircuitIdArray"]=[]; // New circuit case : -1
      //for(var i=0;i<multiplier;i++)
      //ClientRows[index]["CircuitIdArray"].push("-1");
      ClientRows[index]["CircuitId"] = -1;
    }
    //console.log("Circuit to be deleted Id:"+ClientRows[index]["CircuitId"]);
  });

  console.log("Length of Selected Rows::", ClientRows.length);
  console.log("fSaveModifiedCircuitCircuitView Selected Rows::", ClientRows);
}

/* *****************************************************************
 *Function to delete client from demand : Ajax Call with circuit rows **
 ********************************************************************/
function fDeleteCircuitFromDemandAjaxCall(ClientRows) {
  if (ClientRows.length > 0) {
    overlayOn(
      "Deleting circuit from demand",
      ".side-bar-container__content--overlay-container"
    );

    let deleteCircuitPostData = jsonPostObject();
    deleteCircuitPostData.circuitRows = ClientRows;
    deleteCircuitPostData.Mode = 1; // 1 : New Client Add in Circuit View
    console.log(
      "Json Post Object for Circuit Delete from Demand in BF ",
      JSON.stringify(deleteCircuitPostData)
    );

    serverPostMessage("circuitDeleteBf", deleteCircuitPostData)
      .then(function(data) {
        overlayOff(
          "Circuit delete success Success",
          ".side-bar-container__content--overlay-container",
          "fold"
        );
        console.log("Circuit Data::", data);
        getDemandOutputMatrix();
        $(".demands-circuit-view__content")
          .empty()
          .append(
            "<p class='warning'>Click on a demand to get updated circuits.</p>"
          );
      })
      .catch(function(e) {
        overlayOff(
          "Circuit delete Failure",
          ".side-bar-container__content--overlay-container",
          "fold"
        );
        console.log("Delete Circuit BF failure", e);
      });
  } else {
    bootBoxDangerAlert("Select circuits to delete.");
  }
}

/* *******************
 * Save New Clients Button trigger in Circuit View
 *************************/
// $("body").delegate("#SaveDemandCircuitViewTableRowBtn", "click", fSaveNewCircuitsinDemand);

/* *******************
 * Save New Clients Button trigger in Circuit View
 *************************/
$("body").delegate(
  ".demands-circuit-view__btn-close",
  "click",
  fCloseCircuitView
);

/* *******************
 * Save New Clients Button trigger in Circuit View
 *************************/
//$("body" ).delegate("#DeleteDemandCircuitViewTableRowBtn","click",fDeleteDemandCircuitView);

/* *******************
 * Delete Clients Button trigger in Circuit View
 *************************/
$("body").delegate(
  "#DeleteCircuitCircuitViewTableRowBtn",
  "click",
  fDeleteCircuitFromDemand
);

/* *******************
 * Save Clients Button trigger in Circuit View
 *************************/
$("body").delegate(
  "#SaveModifiedCircuitViewTableRowBtn",
  "click",
  fSaveModifiedCircuitCircuitView
);

/* *******************
 * Add Client Button trigger in Circuit View
 *************************/
// $("body").delegate("#AddClientCircuitViewTableRowBtn", "click", fAddNewClient.bind($(this)));

/**Check/Uncheck all checkboxes for delete operation*/
$("body").delegate("#checkAllCircuitView", "change", function() {
  //console.log($(this).prop("checked"));
  //console.log($(".CircuitViewDemandTable input:checkbox"));
  $(".deleteRowIdCircuitView").prop("checked", $(this).prop("checked"));
  if ($(this).prop("checked"))
    $(".deleteRowIdCircuitView")
      .closest("tr")
      .addClass("CircuitViewClientSelected");
  else
    $(".deleteRowIdCircuitView")
      .closest("tr")
      .removeClass("CircuitViewClientSelected");
});

/* *******************
 * New Client Delete
 *************************/
// $("body").delegate(".NewClientCircuitView", "dblclick", function () {
// 	$(this).remove();
// 	//$(this).tooltip();
// });

/* *******************
 * New Client MouseOut
 *************************/
//$("body" ).delegate(".NewClientCircuitView","mouseout",function(){
//	$(this).css({'background-color':'rgb(223, 240, 216)'});
//});

/* *******************
 * New Client remove by clicking on the row
 *************************/
$("body").delegate(".deleteRowIdCircuitView", "change", function() {
  if ($(this).prop("checked"))
    $(this)
      .closest("tr")
      .addClass("CircuitViewClientSelected");
  else
    $(this)
      .closest("tr")
      .removeClass("CircuitViewClientSelected");
});

/* *******************
 * New Client addition : Capacity Change Trigger
 *************************/
$("body").delegate("#CapacityCircuitView", "change", function() {
  var lineRate = circuitViewJsonObj.LineRate,
    capacity = $("#CapacityCircuitView").val(),
    ServiceTypeSource;

  if (
    (capacity == "1" || capacity == "1.25" || capacity == "2.5") &&
    (lineRate == "100" || lineRate == "200")
  )
    $(".errorCapacityCircuitView")
      .empty()
      .append("Capacity can be 10G or 100G for linerate 100G or 200G");
  else {
    $(".errorCapacityCircuitView").empty();
    console.log(
      "CapacityCircuitView OnChange with lineRate:" +
        lineRate +
        " capacity:" +
        capacity
    );
    //Get Service types
    if (capacity == 1 && lineRate == "10") ServiceTypeSource = lineRate1Gx10G;
    else if (capacity == 1.25 && lineRate == "10")
      ServiceTypeSource = lineRate1_25Gx10G;
    else if (capacity == 2.5 && lineRate == "10")
      ServiceTypeSource = lineRate2_5Gx10G;
    else if (capacity == 10 && lineRate == "10")
      ServiceTypeSource = lineRate10Gx10G;
    else ServiceTypeSource = lineRate100Gx200G;

    $("#ServiceTypeCircuitView").empty();
    console.log("ServiceTypeSource:", ServiceTypeSource);
    //Add these options to Service Type Input
    $.each(ServiceTypeSource, function(index, obj) {
      if (obj.text != "None") {
        $("#ServiceTypeCircuitView").append(
          $("<option>", {
            //value: obj.text,
            text: obj.text
          })
        );
      }
    });
  }
});

function fCloseCircuitView() {
  $(".demands-circuit-view").css("display", "none");
  $("#DemandMatrixTable").css("margin-bottom", "10px");
}

function highlightSrcandDestNode(src, dest) {
  var srcNode = graph.getCell(getNodeById(src));
  var destnode = graph.getCell(getNodeById(dest));

  srcNode.attr(attrs.elementHighlighted);
  /*
   * srcNode.attr('.body/stroke', '#d9534f');
   * srcNode.attr('rect/stroke-width', '7'); srcNode.attr('.label/stroke',
   * '#d9534f');
   */

  destnode.attr(attrs.elementHighlighted);
  /*
   * destnode.attr('.body/stroke', '#d9534f');
   * destnode.attr('rect/stroke-width','7'); destnode.attr('.label/stroke',
   * '#d9534f');
   */

  highlightedPathNodes.push(srcNode);
  highlightedPathNodes.push(destnode);
}

function highlightIntermediateNodes(nodeId) {
  var node = graph.getCell(getNodeById(nodeId));
  node.attr(attrs.HighlightedIntermediateNode);

  highlightedPathNodes.push(node);
}

function fHighlightRegeneratorNode(regeneratorNodesArr) {
  for (var i = 0; i < regeneratorNodesArr.length; i++) {
    var node = graph.getCell(getNodeById(regeneratorNodesArr[i]));
    node.attr(attrs.HighlightedRegeneratorNode);

    highlightedPathNodes.push(node);
  }
}

function getpathNodesArr(path) {
  console.log("Creating node path array...");
  var pathNodes = path.split(",");
  var i = 0;
  while (i < pathNodes.length) {
    //console.log(pathNodes[i]);
    i++;
  }
  return pathNodes;
}

function removePathColumnHighlight() {
  console.log("removing Path Column Background.....");
  // selectedColumn.css("background","white");
  selectedColumn.css("border", "none");
}

function removeCommonColumnsBkg() {
  console.log("Removing Tr Background....");
  // selectedRow.css("background","white");

  /*
   * $('#DemandMatrixTable tbody tr').filter(function() { return
   * $(this).css('background-color') == 'rgb(196, 196, 196)';
   * }).css("background","white");
   */

  /*
   * $('#DemandMatrixTable tbody tr').filter(function() { var match =
   * 'rgb(196, 196, 196)'; // match background-color: black
   *
   * true = keep this element in our wrapped set false = remove this element
   * from our wrapped set
   *
   * return ( $(this).css('background') == match );
   *
   * }).css('background', 'white');
   */

  $("#DemandMatrixTable tbody .commonRow")
    .filter(function() {
      return $(this).css("background-color") == "rgb(223, 240, 216)";
    })
    .css("background", "white");

  $("#DemandMatrixTable tbody #pathColumn")
    .filter(function() {
      return (
        $(this).css("color") != "rgb(51,51,51)" &&
        !$(this).hasClass("rejectedWithError")
      );
    })
    .css({ color: "black", "font-size": "14px" });

  /*
   * $('#DemandMatrixTable tbody #pathColumn').filter(function() { return
   * $(this).css('color') == nptGlobals.protectionPathColor; }).css("color","black");
   *
   * $('#DemandMatrixTable tbody #pathColumn').filter(function() { return
   * $(this).css('color') == nptGlobals.restorationPathColor; }).css("color","black");
   */
}

function fRemoveCircuitView() {
  $("#demands-circuit").hide("slide");
}

/************************************************************************
 * Demand Matrix View
 ************************************************************************/
$("#trafficmatrixTabTriggerBtn").on("click", function() {
  console.log("trafficmatrixTabTriggerBtn clicked!!");
  getDemandOutputMatrix();
});

/************************************************************************
 * Ajax call to get RWA output from DB
 ************************************************************************/
var getDemandOutputMatrix = function() {
  console.log(
    "Inside getDemandOutputMatrix() nptGlobals.DemandMatrixViewData::",
    nptGlobals.DemandMatrixViewData
  );

  overlayOn("Fetching Data", ".side-bar-container__content--overlay-container");
  let postJsonData = jsonPostObject();
  console.log("generateDemandMatrixRequest Post Obj ::", postJsonData);

  serverPostMessage("generateDemandMatrixRequest", postJsonData)
    .then(function(data) {
      overlayOff(
        "Fetch Success",
        ".side-bar-container__content--overlay-container",
        "fold"
      );
      console.log("Demand Matrix Fetch Data", data);
      // var data={"view1":[{"Path":"1-2-3","SrcNodeId":1,"ProtectionType":"1+1","NetowrkId":1,"Traffic":50,"DemandId":1,"WavelengthNo":5,"Osnr":60.0,"Spanlength":20,"Cost":6,"DestNodeId":2},{"Path":"1-2","SrcNodeId":1,"ProtectionType":"1+1","NetowrkId":1,"Traffic":50,"DemandId":1,"WavelengthNo":5,"Osnr":60.0,"Spanlength":20,"Cost":6,"DestNodeId":2},{"Path":"1-4-3","SrcNodeId":1,"ProtectionType":"1+1","NetowrkId":1,"Traffic":50,"DemandId":1,"WavelengthNo":5,"Osnr":60.0,"Spanlength":20,"Cost":6,"DestNodeId":2},{"Path":"1-4-2","SrcNodeId":2,"ProtectionType":"1:1","NetowrkId":1,"Traffic":50,"DemandId":1,"WavelengthNo":5,"Osnr":60.0,"Spanlength":20,"Cost":6,"DestNodeId":3},{"Path":"1-2-3","SrcNodeId":2,"ProtectionType":"1:1","NetowrkId":1,"Traffic":50,"DemandId":2,"WavelengthNo":5,"Osnr":60.0,"Spanlength":20,"Cost":6,"DestNodeId":3},{"Path":"1-2-3","SrcNodeId":3,"ProtectionType":"1+1","NetowrkId":1,"Traffic":50,"DemandId":2,"WavelengthNo":5,"Osnr":60.0,"Spanlength":20,"Cost":6,"DestNodeId":4},{"Path":"1-2-3","SrcNodeId":3,"ProtectionType":"1+1","NetowrkId":1,"Traffic":50,"DemandId":2,"WavelengthNo":5,"Osnr":60.0,"Spanlength":20,"Cost":6,"DestNodeId":4},{"Path":"1-2-3","SrcNodeId":3,"ProtectionType":"1+1","NetowrkId":1,"Traffic":50,"DemandId":2,"WavelengthNo":5,"Osnr":60.0,"Spanlength":20,"Cost":6,"DestNodeId":4},{"Path":"1-2","SrcNodeId":1,"ProtectionType":"None","NetowrkId":1,"Traffic":50,"DemandId":3,"WavelengthNo":5,"Osnr":60.0,"Spanlength":10,"Cost":3,"DestNodeId":3}]};

      nptGlobals.DemandMatrixViewData = data;
      var template = "";
      var jsonArr = data.view1;
      var temp = 0,
        rowSpanValue = 0;

      jsonArr = _(jsonArr)
        .chain()
        .sortBy(function(route) {
          return route.RoutePriority;
        })
        .sortBy(function(route) {
          return route.DemandId;
        })
        .sortBy(function(route) {
          return route.State;
        })
        .value();

      console.log("View 1 Data in jsonArr var :", jsonArr);
      for (var i = 0; i < jsonArr.length; i++) {
        // do something with obj[i]
        console.log("Value of i :" + i);
        console.log(jsonArr[i]);
        // var ptcType=jsonArr[i].ProtectionType;
        var demandId = jsonArr[i].DemandId;
        var state = jsonArr[i].State;
        console.log("Demand id value :" + demandId);

        var errorState = jsonArr[i].RoutePriority,
          errorString = "";
        //errorState: 0=no paths ,-1=less paths ,5=paths with error
        if (errorState == nptGlobals.NoPathRwaErrorState) {
          errorString = jsonArr[i].error;
          console.log("Error String for 0 case :" + errorString);
        } else if (errorState == nptGlobals.LessPathRwaErrorState) {
          errorString = jsonArr[i].error;
          console.log("Error String for -1 case :" + errorString);
          errorState = jsonArr[i].RoutePriority;
          i++;
        }

        temp = 0;
        var count = i + 1;
        while (count < jsonArr.length) {
          if (
            jsonArr[count].DemandId == demandId &&
            jsonArr[count].State == state
          ) {
            count++;
            temp++;
          } else break;
        }

        console.log("Value of temp :" + temp);
        rowSpanValue = temp + 1;
        //Status of current state of the result row
        var state = jsonArr[i].State + "RowClass";
        console.log("State received for this Demand ::", state);
        if (errorState == nptGlobals.NoPathRwaErrorState) {
          template += `<tr><td rowspan='${rowSpanValue}' class='commonRow'>`;
          template += getLabelTagsFromState(state);
          template += `</td><td rowspan='${rowSpanValue}' class='commonRow demandId'>${
            jsonArr[i].DemandId
          }</td>`;
          template += `<td rowspan='${rowSpanValue}' class='commonRow srcNodeClass'>${
            jsonArr[i].SrcNodeId
          }</td>`;
          template += `<td rowspan='${rowSpanValue}' class='commonRow destNodeClass'>${
            jsonArr[i].DestNodeId
          }</td>`;

          console.log("Error State Value in No Path Case:", errorState);
          if (errorState == nptGlobals.LessPathRwaErrorState)
            template += `<td rowspan='${rowSpanValue}' class='commonRow '>${
              jsonArr[i].ProtectionType
            }<p class="text-danger smallText text-center ">(${errorString})</p></td>`;
          else
            template += `<td rowspan='${rowSpanValue}' class='commonRow '>${
              jsonArr[i].ProtectionType
            }</td>`;

          template += `<td rowspan='${rowSpanValue}' class='commonRow '>${
            jsonArr[i].Traffic
          }</td>`;
          template += `<td class='working noPathFoundClass' id='pathColumn'>-</td>`;
          template += `<td class='working noPathFoundClass text-center' id='rwaError'>${
            jsonArr[i].error
          }</td>`;
          template += `<td class='working noPathFoundClass'>-</td>`;
          template += `<td class='working noPathFoundClass'>-</td>`;
          template += `<td class='working noPathFoundClass'>-</td>`;
          template += `<td class='working noPathFoundClass'>-</td>`;
          template += `<td class='working noPathFoundClass'>-</td>`;
          template += `<td class='working noPathFoundClass'>-</td>`;
          template += `<td class='working noPathFoundClass'>-</td>`;
          template += `<td class='working noPathFoundClass'>-</td>`;
          template += `<td class='working noPathFoundClass'>-</td>`;
        } else if (errorState >= nptGlobals.RejectedPathRwaErrorState) {
          //case when path returned with issues
          template += `<tr><td rowspan='${rowSpanValue}' class='commonRow'>`;
          template += getLabelTagsFromState(state);
          template += `</td><td rowspan='${rowSpanValue}' class='commonRow demandId'>${
            jsonArr[i].DemandId
          }</td>`;
          template += `<td rowspan='${rowSpanValue}' class='commonRow srcNodeClass'>${
            jsonArr[i].SrcNodeId
          }</td>`;
          template += `<td rowspan='${rowSpanValue}' class='commonRow destNodeClass'>${
            jsonArr[i].DestNodeId
          }</td>`;

          console.log("Error State Value in Rejected Path Case:", errorState);
          if (errorState == nptGlobals.LessPathRwaErrorState)
            template += `<td rowspan='${rowSpanValue}' class='commonRow '>${
              jsonArr[i].ProtectionType
            }<p class=".bg-warning text-muted smallText text-center">(${errorString})</p></td>`;
          else
            template += `<td rowspan='${rowSpanValue}' class='commonRow '>${
              jsonArr[i].ProtectionType
            }</td>`;

          template += `<td rowspan='${rowSpanValue}' class='commonRow '>${
            jsonArr[i].Traffic
          }</td>`;
          template += `<td class='rejectedWithError' id='pathColumn'>${
            jsonArr[i].Path
          }</td>`;
          //if(errorState!=-1)
          template += `<td class='rejectedWithError text-center' id='rwaError'>${
            jsonArr[i].error
          }</td>`;
          template += `<td class='rejectedWithError text-center' id='regenLoc'>${
            jsonArr[i].regeneratorLoc
          }</td>`;
          template += `<td class='rejectedWithError' id='pathType'>${
            jsonArr[i].PathType
          }</td>`;
          template += `<td class='rejectedWithError'>${
            jsonArr[i].WavelengthNo
          }</td>`;
          template += `<td class='rejectedWithError'>${jsonArr[i].Osnr}</td>`;
          template += `<td class='rejectedWithError'>${
            jsonArr[i].lineRate
          }</td>`;
          template += `<td class='rejectedWithError'>${
            jsonArr[i].Spanlength
          }</td>`;
          template += `<td class='rejectedWithError'>${jsonArr[i].Cost}</td><`;
          template += `<td class='rejectedWithError'>${jsonArr[i].cd.toFixed(
            2
          )}</td>`;
          template += `<td class='rejectedWithError'>${jsonArr[i].pmd.toFixed(
            2
          )}</td></tr>`;
        } else {
          template += `<tr><td rowspan='${rowSpanValue}' class='commonRow'>`;
          template += getLabelTagsFromState(state);
          template += `</td><td rowspan='${rowSpanValue}' class='commonRow demandId'>${
            jsonArr[i].DemandId
          }</td>`;
          template += `<td rowspan='${rowSpanValue}' class='commonRow srcNodeClass'>${
            jsonArr[i].SrcNodeId
          }</td>`;
          template += `<td rowspan='${rowSpanValue}' class='commonRow destNodeClass'>${
            jsonArr[i].DestNodeId
          }</td>`;

          console.log("Error State Value in No error Case:", errorState);
          if (errorState == nptGlobals.LessPathRwaErrorState)
            template += `<td rowspan='${rowSpanValue}' class='commonRow '>${
              jsonArr[i].ProtectionType
            }<p class=".bg-warning text-muted smallText text-center">(${errorString})</p></td>`;
          else
            template += `<td rowspan='${rowSpanValue}' class='commonRow '>${
              jsonArr[i].ProtectionType
            }</td>`;

          template += `<td rowspan='${rowSpanValue}' class='commonRow '>${
            jsonArr[i].Traffic
          }</td>`;
          template += `<td class='working' id='pathColumn'>${
            jsonArr[i].Path
          }</td>`;
          template += `<td class='working text-center' id='rwaError'>${
            jsonArr[i].error
          }</td>`;
          template += `<td class='working text-center' id='regenLoc'>${
            jsonArr[i].regeneratorLoc
          }</td>`;
          template += `<td class='working' id='pathType'>${
            jsonArr[i].PathType
          }</td>`;
          template += `<td class='working'>${jsonArr[i].WavelengthNo}</td>`;
          template += `<td class='working'>${jsonArr[i].Osnr}</td>`;
          template += `<td class='working'>${jsonArr[i].lineRate}</td>`;
          template += `<td class='working'>${jsonArr[i].Spanlength}</td>`;
          template += `<td class='working'>${jsonArr[i].Cost}</td>`;
          template += `<td class='working'>${jsonArr[i].cd.toFixed(2)}</td>`;
          template += `<td class='working'>${jsonArr[i].pmd.toFixed(
            2
          )}</td></tr>`;
        }

        // count=i;
        while (temp) {
          // /console.log(jsonArr[i].Routepriority+ "Value of I :"+i);
          i++;
          if (jsonArr[i].RouNewClientCircuitViewSelectedtePriority == 2) {
            console.log(jsonArr[i].RoutePriority + "Value of I :" + i);
            template += `<tr><td class='protection' id='pathColumn'>${
              jsonArr[i].Path
            }</td>`;
            //if(errorState!=-1)
            template += `<td class='protection text-center' id='rwaError'>${
              jsonArr[i].error
            }</td>`;
            template += `<td class='protection text-center' id='regenLoc'>${
              jsonArr[i].regeneratorLoc
            }</td>`;
            template += `<td class='protection' id='pathType'>${
              jsonArr[i].PathType
            }</td>`;
            template += `<td class='protection'>${
              jsonArr[i].WavelengthNo
            }</td>`;
            template += `<td class='protection'>${jsonArr[i].Osnr}</td>`;
            template += `<td class='protection'>${jsonArr[i].lineRate}</td>`;
            template += `<td class='protection'>${jsonArr[i].Spanlength}</td>`;
            template += `<td class='protection'>${jsonArr[i].Cost}</td><`;
            template += `<td class='protection'>${jsonArr[i].cd.toFixed(
              2
            )}</td>`;
            template += `<td class='protection'>${jsonArr[i].pmd.toFixed(
              2
            )}</td></tr>`;
          } else if (
            jsonArr[i].RoutePriority >= nptGlobals.RejectedPathRwaErrorState
          ) {
            //case when path returned with issues
            console.log(jsonArr[i].RoutePriority + "Value of I :" + i);
            template += `<tr><td class='rejectedWithError' id='pathColumn'>${
              jsonArr[i].Path
            }</td>`;
            //if(errorState!=-1)
            template += `<td class='rejectedWithError text-center' id='rwaError'>${
              jsonArr[i].error
            }</td>`;
            template += `<td class='rejectedWithError text-center' id='regenLoc'>${
              jsonArr[i].regeneratorLoc
            }</td>`;
            template += `<td class='rejectedWithError' id='pathType'>${
              jsonArr[i].PathType
            }</td>`;
            template += `<td class='rejectedWithError'>${
              jsonArr[i].WavelengthNo
            }</td>`;
            template += `<td class='rejectedWithError'>${jsonArr[i].Osnr}</td>`;
            template += `<td class='rejectedWithError'>${
              jsonArr[i].lineRate
            }</td>`;
            template += `<td class='rejectedWithError'>${
              jsonArr[i].Spanlength
            }</td>`;
            template += `<td class='rejectedWithError'>${
              jsonArr[i].Cost
            }</td><`;
            template += `<td class='rejectedWithError'>${jsonArr[i].cd.toFixed(
              2
            )}</td>`;
            template += `<td class='rejectedWithError'>${jsonArr[i].pmd.toFixed(
              2
            )}</td></tr>`;
          } //Restoration for 3 and 4 Values
          else {
            template += `<tr><td class='restoration' id='pathColumn'>${
              jsonArr[i].Path
            }</td>`;
            //if(errorState!=-1 )
            template += `<td class='restoration text-center' id='rwaError'>${
              jsonArr[i].error
            }</td>`;
            template += `<td class='restoration text-center' id='regenLoc'>${
              jsonArr[i].regeneratorLoc
            }</td>`;
            template += `<td class='restoration' id='pathType'>${
              jsonArr[i].PathType
            }</td>`;
            template += `<td class='restoration'>${
              jsonArr[i].WavelengthNo
            }</td>`;
            template += `<td class='restoration'>${jsonArr[i].Osnr}</td>`;
            template += `<td class='restoration'>${jsonArr[i].lineRate}</td>`;
            template += `<td class='restoration'>${jsonArr[i].Spanlength}</td>`;
            template += `<td class='restoration'>${jsonArr[i].Cost}</td>`;
            template += `<td class='restoration'>${jsonArr[i].cd.toFixed(
              2
            )}</td>`;
            template += `<td class='restoration'>${jsonArr[i].pmd.toFixed(
              2
            )}</td></tr>`;
          }

          temp--;
        }

        // i=count;
        // console.log("Value of i at end:"+i);
      }
      //console.log("Value of template :"+template);
      $("#demands-view tbody")
        .empty()
        .append(template);
      //fContentLoading("#demands-view tbody");

      console.log("Demand Matrix Table filling from DB Data.....");
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

function fGetDemandMatrixTemplate(demandMatrixArr) {
  let template = "";
  for (var i = 0; i < 4; i++) {
    template += `<div class="demand-item col-md-12 col-sm-12">
    <div class="demand-item__header ">
      <span class="demand-field">
        <p class="demand-field__value">1</p>
        <label for="" class="demand-field__label">DemandId</label>
      </span>
      <span class="demand-field">
        <p class="demand-field__value">1</p>
        <label for="" class="demand-field__label">Src</label>
      </span>
      <span class="demand-field">
        <p class="demand-field__value">2</p>
        <label for="" class="demand-field__label">Dest</label>
      </span>
      <span class="demand-field">
        <p class="demand-field__value">1+1</p>
        <label for="" class="demand-field__label">Protection</label>
      </span>
      <span class="demand-field">
        <p class="demand-field__value">70</p>
        <label for="" class="demand-field__label">Traffic</label>
      </span>
      <span class="demand-field">
        <p class="demand-field__value">NA</p>
        <label for="" class="demand-field__label">Regenerator Loc</label>
      </span>
      <span class="demand-field">
        <p class="demand-field__value">Disjoint</p>
        <label for="" class="demand-field__label">Path Type</label>
      </span>
      <span class="demand-field">
        <p class="demand-field__value">22.26</p>
        <label for="" class="demand-field__label">OSNR</label>
      </span>
      <span class="demand-field">
        <p class="demand-field__value">100</p>
        <label for="" class="demand-field__label">Line Rate</label>
      </span>
      <span class="demand-field">
        <p class="demand-field__value">120</p>
        <label for="" class="demand-field__label">Spanlength</label>
      </span>
      <span class="demand-field">
        <p class="demand-field__value">3</p>
        <label for="" class="demand-field__label">Cost</label>
      </span>
    </div>
    <div class="demand-item__content">
      <label for="" class="col-md-1 demand-item__content--label"
        >Paths</label
      >

      <ul class="demand-paths-list">
        <li class="demand-path-list__item">
          <label for="" class="demand-path-list__item--label">
            Working
          </label>
          <span class="demand-path-list__item--path">
            <span>1</span>
            <span class="demand-path-list__item--wavelength">
              <i class="fa fa-arrows-h" aria-hidden="true"></i>
              <span>40</span>
            </span>
            <span>2</span>
            <span class="demand-path-list__item--wavelength">
              <i class="fa fa-arrows-h" aria-hidden="true"></i>
              <span>40</span>
            </span>
            <span>3</span>
            <span class="demand-path-list__item--wavelength">
              <i class="fa fa-arrows-h" aria-hidden="true"></i>
              <span>40</span>
            </span>
            <span>4</span>
            <span class="demand-path-list__item--wavelength">
              <i class="fa fa-arrows-h" aria-hidden="true"></i>
              <span>40</span>
            </span>
            <span>5</span>
            <span class="demand-path-list__item--wavelength">
              <i class="fa fa-arrows-h" aria-hidden="true"></i>
              <span>40</span>
            </span>
            <span>6</span>
            <span class="demand-path-list__item--wavelength">
              <i class="fa fa-arrows-h" aria-hidden="true"></i>
              <span>40</span>
            </span>
            <span>1</span>
            <span class="demand-path-list__item--wavelength">
              <i class="fa fa-arrows-h" aria-hidden="true"></i>
              <span>40</span>
            </span>
            <span>2</span>
            <span class="demand-path-list__item--wavelength">
              <i class="fa fa-arrows-h" aria-hidden="true"></i>
              <span>40</span>
            </span>
            <span>3</span>
            <span class="demand-path-list__item--wavelength">
              <i class="fa fa-arrows-h" aria-hidden="true"></i>
              <span>40</span>
            </span>
            <span>4</span>
            <span class="demand-path-list__item--wavelength">
              <i class="fa fa-arrows-h" aria-hidden="true"></i>
              <span>40</span>
            </span>
            <span>5</span>
            <span class="demand-path-list__item--wavelength">
              <i class="fa fa-arrows-h" aria-hidden="true"></i>
              <span>40</span>
            </span>
            <span>6</span>
            <span class="demand-path-list__item--wavelength">
              <i class="fa fa-arrows-h" aria-hidden="true"></i>
              <span>40</span>
            </span>
            <span>1</span>
            <span class="demand-path-list__item--wavelength">
              <i class="fa fa-arrows-h" aria-hidden="true"></i>
              <span>40</span>
            </span>
            <span>2</span>
            <span class="demand-path-list__item--wavelength">
              <i class="fa fa-arrows-h" aria-hidden="true"></i>
              <span>40</span>
            </span>
            <span>3</span>
          </span>
        </li>
        <li class="demand-path-list__item">
          <label for="" class="demand-path-list__item--label">
            Protection
          </label>
          <span class="demand-path-list__item--path">
            <span>1</span>
            <span><i class="fa fa-arrows-h" aria-hidden="true"></i> </span>
            <span>2</span>
            <span><i class="fa fa-arrows-h" aria-hidden="true"></i> </span>
            <span>3</span>
            <span><i class="fa fa-arrows-h" aria-hidden="true"></i> </span>
            <span>4</span>
            <span><i class="fa fa-arrows-h" aria-hidden="true"></i> </span>
            <span>5</span>
            <span><i class="fa fa-arrows-h" aria-hidden="true"></i> </span>
            <span>6</span>
          </span>
        </li>
        <li class="demand-path-list__item">
          <label for="" class="demand-path-list__item--label">
            Restoration 1
          </label>
          <span class="demand-path-list__item--path">
            <span>1</span>
            <span><i class="fa fa-arrows-h" aria-hidden="true"></i> </span>
            <span>2</span>
            <span><i class="fa fa-arrows-h" aria-hidden="true"></i> </span>
            <span>3</span>
            <span><i class="fa fa-arrows-h" aria-hidden="true"></i> </span>
            <span>4</span>
            <span><i class="fa fa-arrows-h" aria-hidden="true"></i> </span>
            <span>5</span>
            <span><i class="fa fa-arrows-h" aria-hidden="true"></i> </span>
            <span>6</span>
          </span>
        </li>
        <li class="demand-path-list__item">
          <label for="" class="demand-path-list__item--label">
            Restoration 2
          </label>
          <span class="demand-path-list__item--path">
            <span>1</span>
            <span><i class="fa fa-arrows-h" aria-hidden="true"></i> </span>
            <span>2</span>
            <span><i class="fa fa-arrows-h" aria-hidden="true"></i> </span>
            <span>3</span>
            <span><i class="fa fa-arrows-h" aria-hidden="true"></i> </span>
            <span>4</span>
            <span><i class="fa fa-arrows-h" aria-hidden="true"></i> </span>
            <span>5</span>
            <span><i class="fa fa-arrows-h" aria-hidden="true"></i> </span>
            <span>6</span>
          </span>
        </li>
      </ul>
    </div>
    <div class="demand-item__summary col-md-12">
      <label for="" class="demand-item__summary--label">
        RWA Summary
      </label>
      <span class="text-success">RWA successfully found paths.</span>
    </div>
    <div class="demand-item__footer col-md-12">
      <span>Circuits</span>
      <a class="pull-right "
        ><i class="fa fa-angle-down" aria-hidden="true"></i>
      </a>
      <div class="demand-item__circuit-view">
        <hr />
        <table class="table table-bordered">
          <thead>
            <th>Circuit Id</th>
            <th>Src Node</th>
            <th>Dest Node</th>
          </thead>
          <tbody>
            <tr>
              <td>1</td>
              <td>2</td>
              <td>3</td>
            </tr>
            <tr>
              <td>1</td>
              <td>2</td>
              <td>3</td>
            </tr>
            <tr>
              <td>1</td>
              <td>2</td>
              <td>3</td>
            </tr>
            <tr>
              <td>1</td>
              <td>2</td>
              <td>3</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>`;
  }

  return template;
}

$(".side-bar-container__menu li > a ").click(function() {
  console.log("clicked" + $(this));

  if ($(this).attr("href") !== "demands-view") {
    /**other than demand matrix reset it */
    $("#demands-circuit").css("display", "none");
    // $("div.tabcontents").css("height","100%");
  }
});

// $(".demand-item__footer").on("click", function() {
$("body").delegate(".demand-item__footer", "click", function() {
  console.log("Footer clicked");
  $(this)
    .find(".demand-item__circuit-view")
    .toggle("slow");
});
