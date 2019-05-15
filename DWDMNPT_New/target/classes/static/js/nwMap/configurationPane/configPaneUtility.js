/************************************************************************
 * Initialize Configuration Pane
 ************************************************************************/
function initializeConfigurationPaneFromMap() {
  console.log("initializeConfigurationFromMap");

  let linkId,
    nodeId,
    linkArr = [],
    nodeArr = [],
    expectedId = 1;
  _.each(graph.getCells(), function(el) {
    console.log("Element");
    ///setlocation(el); => DBG GIS
    console.log(el);
    if (el.isLink()) {
      //Get Link Id
      linkId = Number(el.get("linkProperties").linkId);
      sortedPush(linkArr, linkId);
    } else {
      //console.log(el);
      nodeId = Number(el.get("nodeProperties").nodeId);
      sortedPush(nodeArr, nodeId);

      console.log("****************", Number(el.get("nodeProperties").gneFlag));
      if (Number(el.get("nodeProperties").gneFlag) === 1)
        nptGlobals.isPrimaryGneSet = true;
      else if (Number(el.get("nodeProperties").gneFlag) === 2)
        nptGlobals.isSecondaryGneSet = true;
    }
  });

  console.log("NodeArray::");
  console.log(nodeArr);

  console.log("LinkArray::");
  console.log(linkArr);

  nptGlobals.nodeCount = nodeArr.length;
  if (nodeArr.length > 0)
    nptGlobals.nextNodeId = Math.max.apply(null, nodeArr) + 1;
  for (var i = 0; i < nodeArr.length; i++) {
    if (nodeArr[i] != expectedId) {
      //nodeArr.shift();
      nptGlobals.nodeAvailableArr.push(expectedId);
      i--;
    }
    expectedId++;
  }

  expectedId = 1;

  nptGlobals.linkCount = linkArr.length;
  if (linkArr.length > 0)
    nptGlobals.nextLinkId = Math.max.apply(null, linkArr) + 1;
  for (var i = 0; i < linkArr.length; i++) {
    if (linkArr[i] != expectedId) {
      nptGlobals.linkAvailableArr.push(expectedId);
      i--;
    }
    expectedId++;
  }

  networkInfoTab();
  nodeInfoTab();
  linkInfoTab();

  if (isBrownFieldNetwork()) {
    $("footer .row").css({
      background: "#9e4e3e"
    });
  }

  // Temporary Change for Previous Networks
  // Update Link Source and Target
  fUpdateLinkFromNodeConnections();

  //fShowSuccessMessage("Network map is ready.");
  overlayOff("NPT is excited to plan your network.", ".body-overlay");
}

/************************************************************************
 * Network Info Tab
 ************************************************************************/

function networkInfoTab() {
  //var elements=[];
  //	var temp = `<p class='text-center network'>Network Name :${sessionStorage.getItem("NetworkName")}</p>`;
  //	temp += `<div class="col-md-6 col-sm-6 countBox"><p class="img-circle text-center">LINK COUNT <br> <span class="big-number">${nptGlobals.linkCount}
  //		</span></p></div>
  //		<div class="col-md-6 col-sm-6 countBox"><p class="img-circle text-center">ELEMENT COUNT <br><span class="big-number">${nptGlobals.nodeCount}
  //		</span></p></div>`;
  var temp = `<div class="network-details ">
		<h3 class="network-details__content">${sessionStorage.getItem(
      "NetworkName"
    )}</h3>
		<p class=" network-details__label">Network Name</p>
		</div>

		<div class="network-details">
		<h3 class="network-details__content">${nptGlobals.nodeCount}</h3>
		<p class=" network-details__label">Nodes</p>
		</div>

		<div class="network-details">
		<h3 class="network-details__content">${nptGlobals.linkCount}</h3>
		<p class=" network-details__label">Links</p>
		</div>

		<div class="network-details">
		<h3 class="network-details__content">${sessionStorage.getItem("Topology")}</h3>
		<p class=" network-details__label">Topology</p>
		</div>
		<div class="network-details">
		<h3 class="network-details__content">${sessionStorage.getItem(
      "TotalSpanLength"
    )}</h3>
		<p class=" network-details__label">SpanLength</p>
		</div>`;

  // <div class="network-details">
  // <h3 class="network-details__content">${sessionStorage.getItem("Circuits")}</h3>
  // <p class=" network-details__label">Circuits</p>
  // </div>
  // <div class="network-details">
  // <h3 class="network-details__content">${sessionStorage.getItem("Demands")}</h3>
  // <p class=" network-details__label">Demands</p>
  // </div>
  // <div class="network-details">
  // <h3 class="network-details__content">${sessionStorage.getItem("Traffic")}</h3>
  // <p class=" network-details__label">Traffic</p>
  // </div>`;

  {
    /* <div class="label-card--networkInfo count-box">
	<h4 class="label-card-title--networkInfo">Node Count</h4>
	<p class="label-card-text--networkInfo big-number">${nptGlobals.nodeCount}</p>
	</div>

	<div class="label-card--networkInfo count-box">
	<h4 class="label-card-title--networkInfo">Link Count</h4>
	<p class="label-card-text--networkInfo big-number">${nptGlobals.linkCount}</p>
	</div>
	
	<div class="label-card--networkInfo network-details">
	<h4 class="label-card-title--networkInfo">Topology</h4>
	<p class="label-card-text--networkInfo">${sessionStorage.getItem("Topology")}</p>
	</div> */
  }
  $("#network-view")
    .empty()
    .append(temp);
}

/************************************************************************
 * Node Info Tab
 ************************************************************************/

function nodeInfoTab() {
  console.log("NodeInfoTab()");
  //var elements=[];
  nptGlobals.nodeCount = 0;
  var state,
    label,
    //  nodeCount = 0,
    template = "";
  console.log("Inside nodeInfoTab");

  template += `<table class="table sidebar-link-node-info-table">
		<thead>
		<tr>
		<td></td>
		<td>NodeId</td>
		<td>Node Type</td>
		<td>Station Name</td>
		<td>Site Name</td>
		<td>Capacity</td>		
    <td>Gne Flag</td>
    <td>Ems IP</td>
		</tr>
		</thead>
		<tbody>`;

  _.each(graph.getElements(), function(el) {
    if (el.get("nodeProperties").BrownFieldNode == 1) {
      state = nptGlobals.NewRowClassStr;
    } else {
      //Link added in BrownField or GreenField
      if (sessionStorage.getItem("NetworkState") == nptGlobals.BrownFieldStr)
        state = nptGlobals.OldRowClassStr;
      else state = nptGlobals.GreenFieldRowClassStr;
    }

    console.log("State of row :", state);
    label = getLabelTagsFromState(state);
    console.log(el);
    template += `<tr class="${state}">`;
    template += `<td>${label}</td>`;
    template += `<td><span class="badge">${
      el.attributes.nodeProperties.nodeId
    }</span></td>`;
    template += `<td>${el.attributes.nodeProperties.displayName}</td>`;
    template += `<td>${el.attributes.nodeProperties.stationName}</td>`;
    template += `<td>${el.attributes.nodeProperties.siteName}</td>`;
    template += `<td>${nptGlobals.getCapacityString(
      el.attributes.nodeProperties.capacity
    )}</td>`;
    template += `<td>${
      el.attributes.nodeProperties.gneFlag ? "Yes" : "No"
    }</td>`;
    template += `<td>${el.attributes.nodeProperties.emsip}</td>`;
    template += `</tr>`;
    nptGlobals.nodeCount++;
  });

  console.log("nodeCount::", nptGlobals.nodeCount);
  if (nptGlobals.nodeCount == 0) {
    var Message = `<p class="lead text-center text-warning" >Add nodes/links to the map tab to get started.</p>`;
    $("#nodes-view")
      .empty()
      .append(Message);
  } else {
    template += `</tbody></table>`;
    // template += `<button class="btn btn-block" id="nodePropertiesUpdateLinkInfoTab">Update Node Properties</button>`;
    template += `<button class="btn btn-block" id="linkNodeConfigModalTrigger">Edit <i class="fa fa-pencil" aria-hidden="true"></i></button>`;
    $("#nodes-view")
      .empty()
      .append(template);
  }
  //console.log(template);
}

/************************************************************************
 * Link Info Tab
 ************************************************************************/

function linkInfoTab() {
  console.log("linkInfoTab()");
  //var elements=[];
  var src, dest, state;
  nptGlobals.linkCount = 0;
  let totalSpanLength = 0;

  var template = ` <table class="table sidebar-link-node-info-table" id="linkInfoTable">
		<thead>
		<tr>
		<td></td>
		<th>LinkId</th>
		<th>Source</th>
		<th>Source Info</th>
		<th>Target</th>
		<th>Target Info</th>
		<th>Span Length</th>
		<th>Span Loss</th>
		</tr>
		</thead>
		<tbody>`;
  _.each(graph.getLinks(), function(el) {
    console.log("Element" + el);

    totalSpanLength += Number(el.attributes.linkProperties.spanLength);

    if (el.get("linkProperties").BrownFieldLink == 1)
      state = nptGlobals.NewRowClassStr;
    else {
      //Link added in BrownField or GreenField
      if (sessionStorage.getItem("NetworkState") == nptGlobals.BrownFieldStr)
        state = nptGlobals.OldRowClassStr;
      else state = nptGlobals.GreenFieldRowClassStr;
    }
    template += `<tr class="${state}">`;
    template += `<td>` + getLabelTagsFromState(state) + `</td>`;
    template += `<td><span class="badge-red badge">${
      el.attributes.linkProperties.linkId
    }</span></td>`;
    src = graph.getCell(el.attributes.source.id);

    template += `<td><span class="badge">${
      src.attributes.nodeProperties.nodeId
    }</span></td>`;
    template += `<td>${src.attributes.nodeProperties.stationName}-${
      src.attributes.nodeProperties.siteName
    }<br><span class="badge">${
      src.attributes.nodeProperties.displayName
    }</span></td>`;
    dest = graph.getCell(el.attributes.target.id);
    //console.log("Source"+el.attributes.target.id);
    template += `<td><span class="badge">${
      dest.attributes.nodeProperties.nodeId
    }</span></td>`;
    template += `<td>${dest.attributes.nodeProperties.stationName}-${
      dest.attributes.nodeProperties.siteName
    }<br><span class="badge">${
      dest.attributes.nodeProperties.displayName
    }</span></td>`;
    //console.log("Target"+el.attributes.target.id);
    template += `<td>${el.attributes.linkProperties.spanLength}</td>`;
    //console.log("Target"+el.attributes.target.id);
    template += `<td>${el.attributes.linkProperties.calculatedSpanLoss}</td>`;
    //console.log("Target"+el.attributes.target.id);
    template += `</tr>`;

    template += `</tr>`;
    nptGlobals.linkCount++;
  });

  console.log(
    "linkCount::",
    nptGlobals.linkCount + " totalSpanLength::",
    totalSpanLength
  );
  sessionStorage.setItem("TotalSpanLength", totalSpanLength);

  if (nptGlobals.linkCount == 0) {
    var Message = `<p class="lead text-center text-warning" >Add nodes/links to the map tab to get started.</p>`;
    $("#links-view")
      .empty()
      .append(Message);
  } else {
    template += `</tbody></table>`;
    // template += `<button class="btn btn-block" id="linkPropertiesUpdateLinkInfoTab">Update Link Properties</button>`;
    template += `<button class="btn btn-block" id="linkNodeConfigModalTrigger">Edit <i class="fa fa-pencil" aria-hidden="true"></i></button>`;
    //console.log(template);
    $("#links-view")
      .empty()
      .append(template);
  }
}
