let isGneFlagConfigModal = 0,
  isGnePriFlagConfigModal = nptGlobals.isPrimaryGneSet,
  isGneSecFlagConfigModal = nptGlobals.isSecondaryGneSet,
  isApplyAllEdit = 0,
  totalLinksBefore = 0,
  _IlaInsertionLinkConfigFlag = false,
  selectedRowIlaInsertionLinkConfig = -1,
  indexx;

const linkTypeSrc = nptGlobals.fGetLinkTypeSrc();
const gneFlagSrc = nptGlobals.fGetGneTypeSrc();

/************************************************************************
 * Node Info Tab Modal
 ************************************************************************/

function nodeInfoModalTab() {
  //var elements=[];

  console.log("Inside nodeInfoTab");
  var template = `<table class="table-striped tablesorter table" id="nodeInfoTable" oncontextmenu="return false;">
		<thead>
		<tr>
		<th>NodeId</th>
		<th>Node Type</th>
		<th>Station Name</th>
		<th>Site Name</th>
		<th>Capacity</th>		
		<th>Gne Flag</th>
		</tr>
		</thead>
		<tbody>`;
  _.each(graph.getElements(), function(el) {
    //console.log("Element"+el);
    if (el instanceof joint.dia.Link) {
      //Do nothing
    } else {
      console.log(el);
      template += `<tr>`;
      template += `<td><span class="badge">${
        el.attributes.nodeProperties.nodeId
      }</span></td>`;
      //template += `<td><span>${el.attributes.type.substring(5, 12)}</span></td>`;
      template += `<td><span>${
        el.attributes.nodeProperties.displayName
      }</span></td>`;
      /* //console.log(el.attributes.nodeProperties.degree);
            template += `<td>${el.attributes.nodeProperties.stationName}</td>`;
            //console.log(el.attributes.nodeProperties.gneFlag);
            template += `<td>${el.attributes.nodeProperties.siteName}</td>`;
            //console.log(el.attributes.nodeProperties.gneFlag);
            template += `<td>${el.attributes.nodeProperties.capacity}</td>`;
            //console.log(el.attributes.nodeProperties.gneFlag);
            template += `<td>${el.attributes.nodeProperties.gneFlag}</td>`;
            //console.log(el.attributes.nodeProperties.gneFlag);*/
      template += `<td><span class="stationNameNodeInfoTab"></span></td>
				<td><span class="siteNameNodeInfoTab"></span></td>
				<td><span class="capacityNodeInfoTab"></span></td>
				<td><span class="gneFlagNodeInfoTab"></span></td>
				</tr>`;
    }
  });

  template += `</tbody></table>`;
  // template += `<button class="btn btn-block" id="nodePropertiesUpdateLinkInfoTab">Update Node Properties</button>`;
  //var updateButtonTemplate=`<button class="btn" id="nodePropertiesUpdateNodeInfoTabBtn">Update Node Properties</button>`;
  //console.log(template);
  // $('#nodes-view').empty().append(template);
  $("#nodeConfigTab")
    .empty()
    .append(template);
  //$('#linkNodeConfigModal .genericModalFooter').append(updateButtonTemplate);

  initializeNodeInfoModalTab();
}

/************************************************************************
 * Link Info Tab Modal
 ************************************************************************/

function linkInfoModalTab() {
  //var elements=[];
  var src, dest;
  console.log("Inside LinkInfoTab");
  var template = `<table class="footable tablesorter table-striped table table-striped" id="linkInfoTable" oncontextmenu="return false;">
		<thead>
		<tr>
		<th>LinkId</th>
		<th>Source</th>
		<th>Source Info</th>
		<th>Target</th>
		<th>Target Info</th>
		<th>Span Length</th>
		<th>Span Loss</th>
		<th>Fiber Type</th>
		<th>Color</th>
		<th>Srlg</th>
		<th>Cost Metric</th>
		<th>No. of Splices</th>
		<th>Loss Per Splice(DB)</th>
		<th>Connector</th>
		<th>Loss Per Connector(DB)</th>
		<th>Coefficient</th>
		<th>OMS Protected Link</th>		
		<th>Link Type</th>		
		<th>ILA Insertion</th>		
		</tr>
		</thead>
		<tbody>`;
  _.each(graph.getLinks(), function(el) {
    console.log("Element" + el);

    template += `<tr>`;

    template += `<td><span class="badge-red badge">${
      el.attributes.linkProperties.linkId
    }</span></td>`;
    src = graph.getCell(el.attributes.source.id);

    template += `<td><span class="badge">${
      src.attributes.nodeProperties.nodeId
    }</span></td>`;
    template += `<td>${src.attributes.nodeProperties.stationName}-${
      src.attributes.nodeProperties.siteName
    }<span class="badge">${src.get("nodeProperties").displayName}</span></td>`;
    dest = graph.getCell(el.attributes.target.id);
    //console.log("Source"+el.attributes.target.id);
    template += `<td><span class="badge">${
      dest.attributes.nodeProperties.nodeId
    }</span></td>`;
    template += `<td>${dest.attributes.nodeProperties.stationName}-${
      dest.attributes.nodeProperties.siteName
    }<span class="badge">${dest.get("nodeProperties").displayName}</span></td>`;
    //console.log("Target"+el.attributes.target.id);
    /* template += `<td>${el.attributes.linkProperties.spanLength }</td>`;
            //console.log("Target"+el.attributes.target.id);
            template += `<td>${el.attributes.linkProperties.calculatedSpanLoss }</td>`;
            //console.log("Target"+el.attributes.target.id);
            template += `</tr>`;*/

    template += `<td><span class="spanLengthLinkInfoTab"></span></td>
				<td><span class="spanLossLinkInfoTab"></span></td>
				<td><span class="fiberTypeLinkInfoTab"></span></td>
				<td><span class="colorLinkInfoTab"></span></td>	
				<td><span class="srlgLinkInfoTab"></span></td>
				<td><span class="costMetricLinkInfoTab"></span></td>
				<td><span class="numOfSplicesLinkInfoTab"></span></td>
				<td><span class="lossPerSpliceLinkInfoTab"></span></td>
				<td><span class="numOfConnectorsLinkInfoTab"></span></td>
				<td><span class="lossPerConnectorLinkInfoTab"></span></td>
				<td><span class="coefficientLinkInfoTab"></span></td>	
				<td><span class="omsPtcLinkInfoTab"></span></td>
				<td><span class="linkTypeLinkInfoTab"></span></td>`;
    template += `<td><button id="insertIlaLinkNodeConfig" class="btn">Insert</button></td></tr>`;
  });

  template += `</tbody></table>`;
  //template += `<button class="btn btn-block" id="linkPropertiesUpdateLinkInfoTab">Update Link Properties</button>`;
  //console.log(template);
  //var updateLinkButtonTemplate=`<button class="btn" id="linkPropertiesUpdateLinkInfoTabBtn">Update Link Properties</button>`;
  //$('#links-view').empty().append(template);
  $("#linkConfigTab")
    .empty()
    .append(template);
  //console.log("linkInfoModalTab()"+updateLinkButtonTemplate);
  // $('#linkNodeConfigModal .genericModalFooter').empty().append(updateLinkButtonTemplate);
  initializeLinkInfoModalTab();
}

/************************************************************************
 * Link Info Tab X-editable initialization Modal
 ************************************************************************/

var initializeLinkInfoModalTab = function() {
  $.fn.editable.defaults.mode = "inline";
  var spanLength,
    spanLoss,
    fiberType,
    color,
    srlg,
    costMetric,
    numOfSplices,
    lossPerSplice,
    coefficient,
    connector,
    lossPerConnector,
    omsProtection;
  var linkArray = graph.getLinks();
  initializeValuesEditableFields(linkArray);
};

var initializeValuesEditableFields = function(linkArray) {
  for (i = 0; i < linkArray.length; i++) {
    let spanLength = linkArray[i].get("linkProperties").spanLength,
      spanLoss = linkArray[i].get("linkProperties").calculatedSpanLoss,
      fiberType = linkArray[i].get("linkProperties").fiberType,
      color = linkArray[i].get("linkProperties").color,
      srlg = linkArray[i].get("linkProperties").srlg,
      costMetric = linkArray[i].get("linkProperties").costMetric,
      numOfSplices = linkArray[i].get("linkProperties").splice,
      lossPerSplice = linkArray[i].get("linkProperties").lossPerSplice,
      coefficient = linkArray[i].get("linkProperties").coefficient,
      connector = linkArray[i].get("linkProperties").connector,
      lossPerConnector = linkArray[i].get("linkProperties").lossPerConnector,
      omsProtection = linkArray[i].get("linkProperties").lineProtection,
      linkType = linkArray[i].get("linkProperties").linkType;

    $(".spanLengthLinkInfoTab:eq(" + i + ")")
      .attr("title", "Edit ")
      .tooltip()
      .editable({
        type: "text",
        value: spanLength,
        /*validate:function(value,$(this))
			         {
			        	 return fValidateLinkSpanLength(value,$(this));		  
			         },*/
        onblur: "submit",
        validate: function(value) {
          console.log("Validate :");
          console.log("Value :" + value);
          //console.log($(this));
          //console.log($($(this)["0"].$element["0"]));
          //console.log($(this.val)
          return spanLenghthSpanLossValidation(value, $(this));
        },
        savenochange: true
      });

    $(".spanLossLinkInfoTab:eq(" + i + ")")
      .attr("title", "Edit ")
      .tooltip()
      .editable({
        type: "text",
        value: spanLoss,
        validate: function(value) {
          return fValidateLinkSpanLoss(value, $(this));
        },
        onblur: "submit"
      });

    $(".fiberTypeLinkInfoTab:eq(" + i + ")")
      .attr("title", "Edit ")
      .tooltip()
      .editable({
        type: "select",
        value: fiberType,
        source: [{ value: 1, text: "G.652.D" }, { value: 2, text: "G.655" }],
        validate: function(value) {
          // return nodeCapacityValidation(value,$(this));
        },
        onblur: "submit"
      });

    $(".colorLinkInfoTab:eq(" + i + ")")
      .attr("title", "Edit ")
      .tooltip()
      .editable({
        type: "select",
        value: color,
        source: [
          { value: 0, text: "Default" },
          { value: 1, text: "Violet" },
          { value: 2, text: "Indigo" },
          { value: 3, text: "Blue" },
          { value: 4, text: "Green" },
          { value: 5, text: "Yellow" },
          { value: 6, text: "Orange" },
          { value: 7, text: "Red" }
        ],
        onblur: "submit"
      });

    $(".srlgLinkInfoTab:eq(" + i + ")")
      .attr("title", "Edit ")
      .tooltip()
      .editable({
        type: "text",
        value: srlg,
        onblur: "submit"
      });

    $(".costMetricLinkInfoTab:eq(" + i + ")")
      .attr("title", "Edit ")
      .tooltip()
      .editable({
        type: "text",
        value: costMetric,
        onblur: "submit"
      });

    $(".numOfSplicesLinkInfoTab:eq(" + i + ")")
      .attr("title", "Edit ")
      .tooltip()
      .editable({
        type: "text",
        value: numOfSplices,
        onblur: "submit",
        validate: function(value) {
          return splicesSpanLossValidation(value, $(this));
        }
      });

    $(".lossPerSpliceLinkInfoTab:eq(" + i + ")")
      .attr("title", "Edit ")
      .tooltip()
      .editable({
        type: "text",
        value: lossPerSplice,
        onblur: "submit",
        validate: function(value) {
          return lossPerSpliceSpanLossValidation(value, $(this));
        }
      });

    $(".numOfConnectorsLinkInfoTab:eq(" + i + ")")
      .attr("title", "Edit ")
      .tooltip()
      .editable({
        type: "text",
        value: connector,
        onblur: "submit",
        validate: function(value) {
          return numOfConnectorSpanLossValidation(value, $(this));
        }
      });

    $(".lossPerConnectorLinkInfoTab:eq(" + i + ")")
      .attr("title", "Edit ")
      .tooltip()
      .editable({
        type: "text",
        value: lossPerConnector,
        onblur: "submit",
        validate: function(value) {
          return lossPerConnectorSpanLossValidation(value, $(this));
        }
      });

    $(".coefficientLinkInfoTab:eq(" + i + ")")
      .attr("title", "Edit ")
      .tooltip()
      .editable({
        type: "text",
        value: coefficient,
        onblur: "submit",
        validate: function(value) {
          return coefficientSpanLossValidation(value, $(this));
        }
      });

    $(".omsPtcLinkInfoTab:eq(" + i + ")")
      .attr("title", "Edit ")
      .tooltip()
      .editable({
        type: "select",
        value: omsProtection,
        source: yesNoSrc,
        onblur: "submit",
        validate: function(value) {
          return omsProtectionValidation(value, $(this));
        }
      });

    $(".linkTypeLinkInfoTab:eq(" + i + ")")
      .attr("title", "Edit ")
      .tooltip()
      .editable({
        type: "select",
        value: linkType,
        source: linkTypeSrc,
        onblur: "submit",
        validate: function(value) {
          console.log("&&&&&&&&&& Link Type Value Changed to ", value);
          return linkTypeValidation(value, $(this));
        }
      });
  }
  // console.log($(".clientProtection"));

  //$('.footable').footable();
};

/* ***********************************************************************
 * Validation for ILA insertion if Spanloss is greater than 35 or less than 20
 ************************************************************************/

var coefficientSpanLossValidation = function(value, $this) {
  var index = $this.parent().index();
  console.log("Index :" + index);
  var coefficient = $.trim(value);
  var spanLength = $this
      .parent()
      .parent()
      .children()
      .eq(5)
      .find("span")
      .html(),
    spanLoss = $this
      .parent()
      .parent()
      .children()
      .eq(6)
      .find("span")
      .html(),
    numOfSplices = $this
      .parent()
      .parent()
      .children()
      .eq(11)
      .find("span")
      .html(),
    lossPerSplice = $this
      .parent()
      .parent()
      .children()
      .eq(12)
      .find("span")
      .html(),
    connector = $this
      .parent()
      .parent()
      .children()
      .eq(13)
      .find("span")
      .html(),
    lossPerConnector = $this
      .parent()
      .parent()
      .children()
      .eq(14)
      .find("span")
      .html();

  console.log(
    " spanlength :" +
      spanLength +
      " spanLoss :" +
      spanLoss +
      " numOfSplices :" +
      numOfSplices +
      " lossPerSplice :" +
      lossPerSplice +
      " coefficient :" +
      coefficient +
      " connector :" +
      connector +
      " lossPerConnector :" +
      lossPerConnector
  );

  spanLoss =
    coefficient * spanLength +
    numOfSplices * lossPerSplice +
    connector * lossPerConnector;
  console.log("Calculated SpanLoss :" + spanLoss.toFixed(2));
  if (spanLoss > 35) tablesorter;
  {
    bootBoxAlert("Spanloss is greater than 35");
  }

  $this
    .parent()
    .parent()
    .children()
    .eq(6)
    .find("span")
    .editable("setValue", spanLoss.toFixed(2));
  //$this.parent().parent().children().eq(6).find('span').editable('submit');
};

/* ***********************************************************************
 * Validation for ILA insertion if Spanloss is greater than 35 or less than 20
 ************************************************************************/

var numOfConnectorSpanLossValidation = function(value, $this) {
  var index = $this.parent().index();
  console.log("Index :" + index);
  var connector = $.trim(value);
  var spanLength = $this
      .parent()
      .parent()
      .children()
      .eq(5)
      .find("span")
      .html(),
    spanLoss = $this
      .parent()
      .parent()
      .children()
      .eq(6)
      .find("span")
      .html(),
    numOfSplices = $this
      .parent()
      .parent()
      .children()
      .eq(11)
      .find("span")
      .html(),
    lossPerSplice = $this
      .parent()
      .parent()
      .children()
      .eq(12)
      .find("span")
      .html(),
    /*connector=$this.parent().parent().children().eq(13).find('span').html(),*/
    lossPerConnector = $this
      .parent()
      .parent()
      .children()
      .eq(14)
      .find("span")
      .html(),
    coefficient = $this
      .parent()
      .parent()
      .children()
      .eq(15)
      .find("span")
      .html();

  console.log(
    " spanlength :" +
      spanLength +
      " spanLoss :" +
      spanLoss +
      " numOfSplices :" +
      numOfSplices +
      " lossPerSplice :" +
      lossPerSplice +
      " coefficient :" +
      coefficient +
      " connector :" +
      connector +
      " lossPerConnector :" +
      lossPerConnector
  );

  spanLoss =
    coefficient * spanLength +
    numOfSplices * lossPerSplice +
    connector * lossPerConnector;
  console.log("Calculated SpanLoss :" + spanLoss.toFixed(2));
  if (spanLoss > 35) {
    bootBoxAlert("Spanloss is greater than 35");
  }

  $this
    .parent()
    .parent()
    .children()
    .eq(6)
    .find("span")
    .editable("setValue", spanLoss.toFixed(2));
  //$this.parent().parent().children().eq(6).find('span').editable('submit');
};

/* ***********************************************************************
 * Validation for ILA insertion if Spanloss is greater than 35 or less than 20
 ************************************************************************/

var lossPerConnectorSpanLossValidation = function(value, $this) {
  var index = $this.parent().index();
  console.log("Index :" + index);
  var lossPerConnector = $.trim(value);
  var spanLength = $this
      .parent()
      .parent()
      .children()
      .eq(5)
      .find("span")
      .html(),
    spanLoss = $this
      .parent()
      .parent()
      .children()
      .eq(6)
      .find("span")
      .html(),
    numOfSplices = $this
      .parent()
      .parent()
      .children()
      .eq(11)
      .find("span")
      .html(),
    lossPerSplice = $this
      .parent()
      .parent()
      .children()
      .eq(12)
      .find("span")
      .html(),
    connector = $this
      .parent()
      .parent()
      .children()
      .eq(13)
      .find("span")
      .html(),
    /*lossPerConnector=$this.parent().parent().children().eq(14).find('span').html(),*/
    coefficient = $this
      .parent()
      .parent()
      .children()
      .eq(15)
      .find("span")
      .html();

  console.log(
    " spanlength :" +
      spanLength +
      " spanLoss :" +
      spanLoss +
      " numOfSplices :" +
      numOfSplices +
      " lossPerSplice :" +
      lossPerSplice +
      " coefficient :" +
      coefficient +
      " connector :" +
      connector +
      " lossPerConnector :" +
      lossPerConnector
  );

  spanLoss =
    coefficient * spanLength +
    numOfSplices * lossPerSplice +
    connector * lossPerConnector;
  console.log("Calculated SpanLoss :" + spanLoss.toFixed(2));
  if (spanLoss > 35) {
    bootBoxAlert("Spanloss is greater than 35");
  }

  $this
    .parent()
    .parent()
    .children()
    .eq(6)
    .find("span")
    .editable("setValue", spanLoss.toFixed(2));
  //$this.parent().parent().children().eq(6).find('span').editable('submit');
};

/* ***********************************************************************
 * Validation for ILA insertion if Spanloss is greater than 35 or less than 20
 ************************************************************************/

var lossPerSpliceSpanLossValidation = function(value, $this) {
  var index = $this.parent().index();
  console.log("Index :" + index);
  var lossPerSplice = $.trim(value);
  var spanLength = $this
      .parent()
      .parent()
      .children()
      .eq(5)
      .find("span")
      .html(),
    spanLoss = $this
      .parent()
      .parent()
      .children()
      .eq(6)
      .find("span")
      .html(),
    numOfSplices = $this
      .parent()
      .parent()
      .children()
      .eq(11)
      .find("span")
      .html(),
    /*lossPerSplice=$this.parent().parent().children().eq(12).find('span').html(),*/

    connector = $this
      .parent()
      .parent()
      .children()
      .eq(13)
      .find("span")
      .html(),
    lossPerConnector = $this
      .parent()
      .parent()
      .children()
      .eq(14)
      .find("span")
      .html(),
    coefficient = $this
      .parent()
      .parent()
      .children()
      .eq(15)
      .find("span")
      .html();

  console.log(
    " spanlength :" +
      spanLength +
      " spanLoss :" +
      spanLoss +
      " numOfSplices :" +
      numOfSplices +
      " lossPerSplice :" +
      lossPerSplice +
      " coefficient :" +
      coefficient +
      " connector :" +
      connector +
      " lossPerConnector :" +
      lossPerConnector
  );

  spanLoss =
    coefficient * spanLength +
    numOfSplices * lossPerSplice +
    connector * lossPerConnector;
  console.log("Calculated SpanLoss :" + spanLoss.toFixed(2));
  if (spanLoss > 35) {
    bootBoxAlert("Spanloss is greater than 35");
  }

  $this
    .parent()
    .parent()
    .children()
    .eq(6)
    .find("span")
    .editable("setValue", spanLoss.toFixed(2));
  //$this.parent().parent().children().eq(6).find('span').editable('submit');
};

/* ***********************************************************************
 * Validation for ILA insertion if Spanloss is greater than 35 or less than 20
 ************************************************************************/

var splicesSpanLossValidation = function(value, $this) {
  var index = $this.parent().index();
  console.log("Index :" + index);
  var numOfSplices = $.trim(value);
  var spanLength = $this
      .parent()
      .parent()
      .children()
      .eq(5)
      .find("span")
      .html(),
    spanLoss = $this
      .parent()
      .parent()
      .children()
      .eq(6)
      .find("span")
      .html(),
    /*numOfSplices=$this.parent().parent().children().eq(11).find('span').html(),*/
    lossPerSplice = $this
      .parent()
      .parent()
      .children()
      .eq(12)
      .find("span")
      .html(),
    connector = $this
      .parent()
      .parent()
      .children()
      .eq(13)
      .find("span")
      .html(),
    lossPerConnector = $this
      .parent()
      .parent()
      .children()
      .eq(14)
      .find("span")
      .html(),
    coefficient = $this
      .parent()
      .parent()
      .children()
      .eq(15)
      .find("span")
      .html();

  console.log(
    " spanlength :" +
      spanLength +
      " spanLoss :" +
      spanLoss +
      " numOfSplices :" +
      numOfSplices +
      " lossPerSplice :" +
      lossPerSplice +
      " coefficient :" +
      coefficient +
      " connector :" +
      connector +
      " lossPerConnector :" +
      lossPerConnector
  );

  spanLoss =
    coefficient * spanLength +
    numOfSplices * lossPerSplice +
    connector * lossPerConnector;
  console.log("Calculated SpanLoss :" + spanLoss.toFixed(2));
  if (spanLoss > 35) {
    bootBoxAlert("Spanloss is greater than 35");
  }

  $this
    .parent()
    .parent()
    .children()
    .eq(6)
    .find("span")
    .editable("setValue", spanLoss.toFixed(2));
  // $this.parent().parent().children().eq(6).find('span').editable('submit');
};

/* ***********************************************************************
 * Spanlength ---- Validation for ILA insertion if Spanloss is greater than 35 or less than 20
 ************************************************************************/

var spanLenghthSpanLossValidation = function(value, $this) {
  if (isApplyAllEdit == 1) $this = $($this["0"].$element["0"]);

  console.log("SpanLength Validation");
  let index = $this.parent().index();
  console.log("Index :" + index);
  let spanLength = $.trim(value);
  let /*spanLength=$this.parent().parent().children().eq(5).find('span').html(),*/
    spanLoss = $this
      .parent()
      .parent()
      .children()
      .eq(6)
      .find("span")
      .html(),
    numOfSplices = $this
      .parent()
      .parent()
      .children()
      .eq(11)
      .find("span")
      .html(),
    lossPerSplice = $this
      .parent()
      .parent()
      .children()
      .eq(12)
      .find("span")
      .html(),
    connector = $this
      .parent()
      .parent()
      .children()
      .eq(13)
      .find("span")
      .html(),
    lossPerConnector = $this
      .parent()
      .parent()
      .children()
      .eq(14)
      .find("span")
      .html(),
    coefficient = $this
      .parent()
      .parent()
      .children()
      .eq(15)
      .find("span")
      .html();
  (spanLoss = parseFloat(spanLoss)),
    (linkType = $this
      .parent()
      .parent()
      .children()
      .eq(17)
      .find("span")
      .html());

  console.log(
    " spanlength :" +
      spanLength +
      " spanLoss :" +
      spanLoss +
      " numOfSplices :" +
      numOfSplices +
      " lossPerSplice :" +
      lossPerSplice +
      " coefficient :" +
      coefficient +
      " connector :" +
      connector +
      " lossPerConnector :" +
      lossPerConnector
  );

  if (isApplyAllEdit != 1) {
    spanLoss =
      coefficient * spanLength +
      numOfSplices * lossPerSplice +
      connector * lossPerConnector;
    console.log("Calculated SpanLoss :" + spanLoss.toFixed(2));
  }

  if (linkType === nptGlobals.getDefaultLinkStr()) {
    if (spanLoss > nptGlobals.getDefaultLinkMaxSpanLoss()) {
      //		 bootBoxAlert("Spanloss is greater than 35");
      //		 console.log($this.parent().parent());
      $this
        .parent()
        .parent()
        .addClass("danger");
    } else {
      $this
        .parent()
        .parent()
        .removeClass("danger");
    }
  } else if (linkType === nptGlobals.getRamanHybridLinkStr()) {
    if (spanLoss > nptGlobals.getRamanHybridMaxSpanloss()) {
      $this
        .parent()
        .parent()
        .addClass("danger");
    } else {
      $this
        .parent()
        .parent()
        .removeClass("danger");
    }
  } else if (linkType === nptGlobals.getRamanDraLinkStr()) {
    if (spanLoss > nptGlobals.getRamanDraMaxSpanloss()) {
      $this
        .parent()
        .parent()
        .addClass("danger");
    } else {
      $this
        .parent()
        .parent()
        .removeClass("danger");
    }
  }

  //	if(spanLoss>35 && spanLoss<37)
  //	{
  //	 bootBoxAlert(`Spanloss is between 35 and 37 so you need to insert an ILA or you can choose 'Option 4' in insert ILA window to add Raman Hybrid at source and target nodes.`);
  //	 console.log($this.parent().parent());
  //	 $this.parent().parent().addClass('danger');
  //	}
  //	else if(spanLoss>37){
  //	 console.log($this.parent().parent());
  //	 $this.parent().parent().addClass('danger');
  //	}
  //	else {
  //		$this.parent().parent().removeClass('danger');
  //	}

  $this
    .parent()
    .parent()
    .children()
    .eq(6)
    .find("span")
    .editable("setValue", spanLoss.toFixed(2), true);
  //    /$this.parent().parent().children().eq(6).find('span').editable('submit');
};

/* ***********************************************************************
 * OMS Protected Line ---- Validation for Linear topology check
 ************************************************************************/

var omsProtectionValidation = function(omsValue, $this) {
  var index = $this.parent().index();
  console.log("----Oms Protected Line --- Index :" + index);

  omsValue = $.trim(omsValue);
  var srcNodeId = $this
      .parent()
      .parent()
      .children()
      .eq(1)
      .find("span")
      .html(),
    destNodeId = $this
      .parent()
      .parent()
      .children()
      .eq(2)
      .find("span")
      .html();

  console.log(" srcNodeId :" + srcNodeId + " destNodeId :" + destNodeId);

  if (
    omsValue == 1 &&
    nptGlobals.NetworkTopology != nptGlobals.TopologyLinearStr
  ) {
    return "Link can be OMS protected only in Linear Topology.";
  }
};

/* ***********************************************************************
 * OMS Protected Line ---- Validation for Linear topology check
 ************************************************************************/

var linkTypeValidation = function(linkType, $this) {
  //	let index=$this.parent().index();
  //	console.log("----linkTypeValidation --- Index :"+index);

  linkType = $.trim(linkType);
  let spanLossValue = $this
      .parent()
      .parent()
      .children()
      .eq(6)
      .find("span")
      .html(),
    srcNodeId = $this
      .parent()
      .parent()
      .children()
      .eq(1)
      .find("span")
      .html(),
    destNodeId = $this
      .parent()
      .parent()
      .children()
      .eq(3)
      .find("span")
      .html();

  let srcNodeType = graph.getCell(getNodeById(srcNodeId)).get("type"),
    destNodeType = graph.getCell(getNodeById(destNodeId)).get("type");

  console.log(
    "linkTypeValidation:: spanLoss :",
    spanLossValue,
    " srcNodeId :",
    srcNodeId,
    " destNodeId :",
    destNodeId,
    " srcNodeType :",
    srcNodeType,
    " destNodeType :",
    destNodeType
  );

  if (
    srcNodeType == nptGlobals.NodeTypeILA ||
    destNodeType == nptGlobals.NodeTypeILA
  ) {
    return "Link type cannot be changed for link connected to ILA.";
  }

  if (linkType === nptGlobals.getDefaultLinkStr()) {
    if (spanLossValue > nptGlobals.getDefaultLinkMaxSpanLoss()) {
      $this
        .parent()
        .parent()
        .addClass("danger");
    } else {
      $this
        .parent()
        .parent()
        .removeClass("danger");
    }
  } else if (linkType === nptGlobals.getRamanHybridLinkStr()) {
    if (spanLossValue > nptGlobals.getRamanHybridMaxSpanloss()) {
      $this
        .parent()
        .parent()
        .addClass("danger");
    } else {
      $this
        .parent()
        .parent()
        .removeClass("danger");
    }
  } else if (linkType === nptGlobals.getRamanDraLinkStr()) {
    if (spanLossValue > nptGlobals.getRamanDraMaxSpanloss()) {
      $this
        .parent()
        .parent()
        .addClass("danger");
    } else {
      $this
        .parent()
        .parent()
        .removeClass("danger");
    }
  }
};

/* ***********************************************************************
 * OMS Protected Line ---- Validation for Linear topology check
 ************************************************************************/

var fValidateLinkSpanLoss = function(value, $this) {
  let spanLossValue = $.trim(value),
    linkType = $this
      .parent()
      .parent()
      .children()
      .eq(17)
      .find("span")
      .html();
  console.log("fValidateLinkSpanLoss ---- ", linkType);

  if (linkType === nptGlobals.getDefaultLinkStr()) {
    if (spanLossValue > nptGlobals.getDefaultLinkMaxSpanLoss()) {
      //		 bootBoxAlert("Spanloss is greater than 35");
      //		 console.log($this.parent().parent());
      $this
        .parent()
        .parent()
        .addClass("danger");
    } else {
      $this
        .parent()
        .parent()
        .removeClass("danger");
    }
  } else if (linkType === nptGlobals.getRamanHybridLinkStr()) {
    if (spanLossValue > nptGlobals.getRamanHybridMaxSpanloss()) {
      $this
        .parent()
        .parent()
        .addClass("danger");
    } else {
      $this
        .parent()
        .parent()
        .removeClass("danger");
    }
  } else if (linkType === nptGlobals.getRamanDraLinkStr()) {
    if (spanLossValue > nptGlobals.getRamanDraMaxSpanloss()) {
      $this
        .parent()
        .parent()
        .addClass("danger");
    } else {
      $this
        .parent()
        .parent()
        .removeClass("danger");
    }
  }

  //	if(spanLossValue>37 || spanLossValue<20)
  //	{
  //	 //bootBoxAlert("Spanloss is greater than 35");
  //	 console.log($this.parent().parent());
  //	 $this.parent().parent().addClass('danger');
  //	}else if(spanLossValue>35 && spanLossValue<37)
  //	{
  //	 bootBoxAlert(`Spanloss is between 35 and 37 so you need to insert an ILA or you can choose 'Option 4' in insert ILA window to add Raman Hybrid at source and target nodes.`);
  //	 console.log($this.parent().parent());
  //	 $this.parent().parent().addClass('danger');
  //	}
  //	else {
  //		$this.parent().parent().removeClass('danger');
  //	}
};

/************************************************************************
 * Node Info Tab X-editable initialization Modal
 ************************************************************************/

var initializeNodeInfoModalTab = function() {
  // Set gne flag variables for this window, use these local var until user saves the properties
  isGnePriFlagConfigModal = nptGlobals.isPrimaryGneSet;
  isGneSecFlagConfigModal = nptGlobals.isSecondaryGneSet;

  $.fn.editable.defaults.mode = "inline";
  var stationName, siteName, capacity, gneFlag;
  var elementsArray = graph.getElements(),
    nodeArray = [];
  for (i = 0; i < elementsArray.length; i++) {
    if (!elementsArray[i].isLink()) {
      nodeArray.push(elementsArray[i]);
      console.log(nodeArray[i].get("nodeProperties").stationName);
    }
  }

  for (i = 0; i < nodeArray.length; i++) {
    stationName = nodeArray[i].get("nodeProperties").stationName;
    siteName = nodeArray[i].get("nodeProperties").siteName;
    capacity = nodeArray[i].get("nodeProperties").capacity;
    gneFlag = nodeArray[i].get("nodeProperties").gneFlag;

    $(".stationNameNodeInfoTab:eq(" + i + ")")
      .attr("title", "Edit ")
      .tooltip()
      .editable({
        type: "text",
        value: stationName,
        onblur: "submit",
        validate: function(value) {
          return fValidateStationName(value, $(this));
        }
      });

    $(".siteNameNodeInfoTab:eq(" + i + ")")
      .attr("title", "Edit ")
      .tooltip()
      .editable({
        type: "text",
        value: siteName,
        onblur: "submit",
        validate: function(value) {
          return fValidateSiteName(value, $(this));
        }
      });

    $(".capacityNodeInfoTab:eq(" + i + ")")
      .attr("title", "Edit ")
      .tooltip()
      .editable({
        type: "select",
        value: capacity,
        source: [
          { value: 1, text: "Even 40 Channel" },
          { value: 2, text: "Odd 40 Channel" },
          { value: 3, text: "80 Channel" }
        ],
        validate: function(value) {
          return fValidateNodeCapacity(value, $(this));
        },
        onblur: "submit"
      });

    $(".gneFlagNodeInfoTab:eq(" + i + ")")
      .attr("title", "Edit ")
      .tooltip()
      .editable({
        type: "select",
        value: gneFlag,
        source: gneFlagSrc,
        validate: function(value) {
          return fValidateGneFlagInput(value, $(this));
        },
        onblur: "submit"
        //showbuttons:false
      });
  }
  // console.log($(".clientProtection"));

  //Called once for inline editing
  $(".footable").footable();
};

/* ***********************************************************************
 * Site Name Validation
 ************************************************************************/

var fValidateSiteName = function(siteName, $this) {
  var index = $this.parent().index();

  siteName = $.trim(siteName);
  var stationName = $this
      .parent()
      .parent()
      .children()
      .eq(2)
      .find("span")
      .html(),
    nodeId = $this
      .parent()
      .parent()
      .children()
      .eq(0)
      .find("span")
      .html();

  console.log(" siteName :" + siteName + " stationName :" + stationName);

  var siteStationNameStr = stationName + siteName;
  var sameStrFlag = true;

  //False if same name comb is related to other Node
  SiteAndStationNameMap.forEach(function(value, key) {
    if (value == siteStationNameStr && key != String(nodeId))
      sameStrFlag = false;
  });

  console.log("siteStationNameStr :" + siteStationNameStr);
  console.log("sameStrFlag :" + sameStrFlag);
  /*console.log("SiteAndStationNameSet");
	console.log(SiteAndStationNameSet);*/

  console.log("SiteAndStationNameMap");
  console.log(SiteAndStationNameMap);

  if (siteName == stationName)
    return "Sitename and StationName cannot be same.";
  else if (/*SiteAndStationNameSet.has(siteStationNameStr) && */ !sameStrFlag) {
    //if true then
    console.log("No two Sitename and StationName combination can be same.");
    return "No two Sitename and StationName combination can be same.";
  } else {
    //SiteAndStationNameSet.add(siteStationNameStr);
    SiteAndStationNameMap.set(String(nodeId), siteStationNameStr);
  }
};

/* ***********************************************************************
 * Site Name Validation
 ************************************************************************/

var fValidateStationName = function(stationName, $this) {
  var index = $this.parent().index(),
    stationName = $.trim(stationName),
    siteName = $this
      .parent()
      .parent()
      .children()
      .eq(5)
      .find("span")
      .html(),
    nodeId = $this
      .parent()
      .parent()
      .children()
      .eq(0)
      .find("span")
      .html();

  console.log(" siteName :" + siteName + " stationName :" + stationName);

  var siteStationNameStr = stationName + siteName;
  var sameStrFlag = true;

  //False if same name comb is related to other Node
  SiteAndStationNameMap.forEach(function(value, key) {
    if (value == siteStationNameStr && key != String(nodeId))
      sameStrFlag = false;
  });

  console.log("siteStationNameStr :" + siteStationNameStr);
  console.log("sameStrFlag :" + sameStrFlag);
  /*console.log("SiteAndStationNameSet");
	console.log(SiteAndStationNameSet);*/

  console.log("SiteAndStationNameMap");
  console.log(SiteAndStationNameMap);

  if (siteName == stationName)
    return "Sitename and StationName cannot be same.";
  else if (/*SiteAndStationNameSet.has(siteStationNameStr) && */ !sameStrFlag) {
    //if true then
    console.log("No two Sitename and StationName combination can be same.");
    return "No two Sitename and StationName combination can be same.";
  } else {
    //SiteAndStationNameSet.add(siteStationNameStr);
    SiteAndStationNameMap.set(String(nodeId), siteStationNameStr);
  }
};

/************************************************************************
 * Validate Node Properties
 ************************************************************************/
var fValidateNodeCapacity = function(value, $this) {
  let nodeType = $this
    .parent()
    .prev()
    .prev()
    .prev()
    .children()
    .html();
  console.log("fValidateNodeCapacity() --- nodeType ::", nodeType);
  if (
    (value == 1 || value == 2) &&
    (nodeType == nptGlobals.NodeTypeROADMDisplayName ||
      nodeType == nptGlobals.NodeTypeTEDisplayName)
  ) {
    return "Only 80 channel path is allowed for TE/CDC ROADM (8 Degree)";
  }
};

/************************************************************************
 * Validate GNE
 ************************************************************************/
var fValidateGneFlagInput = function(value, $this) {
  console.log(
    "isGneFlagConfigModal :" + isGneFlagConfigModal + " Value :" + value
  );

  //Primary Case
  if (value == 1) {
    if (isGnePriFlagConfigModal) return "There can be only one primary gne.";
  }
  //Secondary Case
  else if (value == 2) {
    if (isGneSecFlagConfigModal) return "There can be only one secondary gne.";
  }

  if (value == 1) isGnePriFlagConfigModal = true;
  if (value == 2) isGneSecFlagConfigModal = true;

  //   let gneFlag = checkForGneSet(0, value);
  //   console.log("gneFlag :::", gneFlag);

  //   if (!gneFlag)
  //     return "There can be only one Primary and Secondary GNE in a network";

  //   if (value == 1) nptGlobals.isPrimaryGneSet = true;
  //   if (value == 2) nptGlobals.isSecondaryGneSet = true;

  //   if (isGneFlagConfigModal == 0) {
  //     isGneFlagConfigModal = value;
  //   } else {
  //     if (value == 0) {
  //       isGneFlagConfigModal = 0;
  //     } else return "Another node selected as GNE";
  //   }
};

/************************************************************************
 * Update Node Properties from Config Modal Update Trigger
 ************************************************************************/
$(document).delegate("#nodePropertiesUpdateNodeInfoTabBtn", "click", function(
  e
) {
  //console.log("Node Property Save Modal");
  var myRows = [];
  var $headers = $("#nodeInfoTable th");
  //console.log("Headers-----");
  console.log($($headers));
  var $rows = $("#nodeInfoTable tbody tr").each(function(index) {
    //console.log("Row-----"+$(this));
    $cells = $(this).find("td");
    console.log("Columns\n -----");
    console.log($($cells));
    myRows[index] = {};
    $cells.each(function(cellIndex) {
      //console.log("Table column")
      //console.log($(this).find('span').hasClass('editable'))
      if (
        $(this)
          .find("span")
          .hasClass("editable") == true
      ) {
        myRows[index][$($headers[cellIndex]).html()] = $(this)
          .find("span")
          .editable("getValue").undefined;
      } else {
        myRows[index][$($headers[cellIndex]).html()] = $(this)
          .find("span")
          .html();
      }
    });
  });

  /*************Put this in the object like you want and convert to JSON (Note: jQuery will also do this for you on the Ajax request)*********/
  var nodePropertySaveObject = {};
  nodePropertySaveObject.properties = myRows;
  //console.log(JSON.parse(myObj));
  console.log(
    "Json Array of Node Config Modal\n" + JSON.stringify(nodePropertySaveObject)
  );
  fUpdateAllNodePropertiesFromConfigModal(nodePropertySaveObject);
});

/************************************************************************
 * Update Link Properties from Config Modal Update Trigger
 ************************************************************************/
$(document).delegate("#linkPropertiesUpdateLinkInfoTabBtn", "click", function(
  e
) {
  //console.log("Link Property Save Modal");

  var numOfRowsWithClassDanger = 0;
  var myRows = [];
  var $linkheaders = $("#linkNodeConfigModal #linkInfoTable thead tr th");
  console.log("Headers-----");
  console.log($($linkheaders));
  var $rows = $("#linkNodeConfigModal #linkInfoTable tbody tr").each(function(
    index
  ) {
    //console.log("Row-----"+$(this));
    if ($(this).hasClass("danger")) {
      numOfRowsWithClassDanger++;
    }
    $cells = $(this).find("td");
    console.log("Columns\n -----");
    console.log($($cells));
    myRows[index] = {};
    $cells.each(function(cellIndex) {
      //console.log("Table column")
      //console.log($(this).find('span').hasClass('editable'))
      if (
        $(this)
          .find("span")
          .hasClass("editable") == true
      ) {
        myRows[index][$($linkheaders[cellIndex]).html()] = $(this)
          .find("span")
          .editable("getValue").undefined;
      } else {
        myRows[index][$($linkheaders[cellIndex]).html()] = $(this)
          .find("span")
          .html();
      }
    });
  });

  /*************Put this in the object like you want and convert to JSON (Note: jQuery will also do this for you on the Ajax request)*********/
  var linkPropertySaveObject = {};
  linkPropertySaveObject.properties = myRows;
  //console.log(JSON.parse(myObj));
  console.log(
    "Json Array of Link Config Modal\n" + JSON.stringify(linkPropertySaveObject)
  );
  if (numOfRowsWithClassDanger == 0)
    fUpdateAllLinkPropertiesFromConfigModal(linkPropertySaveObject);
  else
    bootBoxWarningAlert(
      "Link Properties could not be saved.An ILA needs to be inserted between the nodes where rows are marked red to adjust spanloss.",
      3000
    );
});

/************************************************************************
 * Update Node Properties from Config Modal
 ************************************************************************/
var fUpdateAllNodePropertiesFromConfigModal = function(nodePropertySaveObject) {
  var nodeInfoArr = nodePropertySaveObject.properties;
  var nodeId;
  for (var i = 0; i < nodeInfoArr.length; i++) {
    nodeId = nodeInfoArr[i].NodeId;
    var cell = graph.getCell(getNodeById(nodeId));
    cell.get("nodeProperties").stationName = nodeInfoArr[i]["Station Name"];
    cell.get("nodeProperties").siteName = nodeInfoArr[i]["Site Name"];
    cell.get("nodeProperties").capacity = nodeInfoArr[i]["Capacity"];
    cell.get("nodeProperties").gneFlag = nodeInfoArr[i]["Gne Flag"];
  }
  //Update Node Tab
  nodeInfoTab();
  bootBoxAlert("Node Properties saved successfully", 1000);
  //alert("Changes saved");
};

/************************************************************************
 * Update Link Properties from Config Modal
 ************************************************************************/
var fUpdateAllLinkPropertiesFromConfigModal = function(linkPropertySaveObject) {
  var linkInfoArr = linkPropertySaveObject.properties;
  var srcNodeId, destNodeId;
  for (var i = 0; i < linkInfoArr.length; i++) {
    srcNodeId = linkInfoArr[i]["Source"];
    destNodeId = linkInfoArr[i]["Target"];
    console.log("srcNodeId :" + srcNodeId + " destNodeId :" + destNodeId);
    var link = getLinkBetweenSrcandDestNodes(
      getNodeById(srcNodeId),
      getNodeById(destNodeId)
    );
    console.log(link);
    link.get("linkProperties").spanLength = linkInfoArr[i]["Span Length"];
    link.get("linkProperties").calculatedSpanLoss = linkInfoArr[i]["Span Loss"];
    link.get("linkProperties").adjustableSpanLoss = linkInfoArr[i]["Span Loss"];
    link.get("linkProperties").fiberType = linkInfoArr[i]["Fiber Type"];
    link.get("linkProperties").color = linkInfoArr[i]["Color"];
    link.get("linkProperties").srlg = linkInfoArr[i]["Srlg"];
    link.get("linkProperties").costMetric = linkInfoArr[i]["Cost Metric"];
    link.get("linkProperties").lineProtection =
      linkInfoArr[i]["OMS Protected Link"];

    setOpticalReachAsPerSpanLength(link.get("id"));
  }
  //Update Node Tab
  linkInfoTab();
  setallLinkAttributes();
  bootBoxAlert("Link Properties saved successfully", 1000);
  //alert("Changes saved");
};

/************************************************************************
 * Modal template for dynamic injection on Index.html onLoad
 ************************************************************************/
var linkNodeConfigModalTemplate = `<div id="linkNodeConfigModal" class="modal fade "  role="dialog">
					<div class="modal-dialog genericModalDialog">

						<!-- Modal content-->
						 <div class="modal-content genericModalContent"
							id="modalContentlinkNodeConfig">
							<!-- <div class="modal-header">					    
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						</div> 	-->
						<div class="col-md-12 col-sm-12 modal-header zeroPadding">
											<div class="col-md-6 col-sm-6 genericModalBodyHeadPanelHeading">
												<p id="linkNodeConfigViewMainHeading">	LINK NODE CONFIGURATION </p>														
												<button type="button" class="pull-right btn btn--danger" data-dismiss="modal"> <i class="fa fa-times" aria-hidden="true"></i></button>
												<button type="button" class="pull-right btn btn--warning modalMinimize"> <i class='fa fa-minus'></i> </button> 
											</div>
						</div>
					
								<div class="modal-body genericModalBody"
									id="modalBodylinkNodeConfig">
									<div class="row">
										
										<div class="col-sm-12 col-md-12 genericModalBodyHeadPanel">
											<div class="col-md-12 col-sm-12">
												<ul class="nav nav-pills">
													<li class="active"><a data-toggle="pill"
														href="#linkConfigTab">Links</a></li>
													<li><a data-toggle="pill" href="#nodeConfigTab">Nodes</a></li>
												</ul>
										</div>
										<div class="col-md-12">
											<p class="text-center info-container">1.Right-click on any field to apply the same value to all other fields of that column.\n 2.Rows marked red needs ILA insertion.</p>
										</div>

										</div>
										<div class="col-sm-12 col-md-12 genericModalBodyContentPanel">
											<div class="tab-content">
												<div id="linkConfigTab" class="tab-pane fade in active">
													<h3>Links</h3>
													<p>Some content in menu Links.</p>
												</div>
												<div id="nodeConfigTab" class="tab-pane fade">
													<h3>Nodes</h3>
													<p>Some content in menu Nodes.</p>
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

/************************************************************************
 * On page load append modal template to index.html
 ************************************************************************/
(function() {
  //console.log(" LinkNodeConfig Modal Added to index.html");
  $(document.body).append(linkNodeConfigModalTemplate);
  //$("#linkNodeConfigModal").modal("show");
  var updateButtonTemplate = `<button class="btn btn--default" id="nodePropertiesUpdateNodeInfoTabBtn">Update Node Properties</button>`;

  var resetButtonTemplate = `<button class="btn btn--default id="resetPropertDRAGiesUpdateNodeInfoTabBtn">Reset Defaults</button>`;

  var updateLinkButtonTemplate = `<button class="btn btn--default" id="linkPropertiesUpdateLinkInfoTabBtn">Update Link Properties</button>`;

  $("#linkNodeConfigModal .genericModalFooter")
    .append(updateButtonTemplate)
    .append(updateLinkButtonTemplate);
  //.append(resetButtonTemplate);
})();

/************************************************************************
 * Trigger for loading Config Modal for Link and Node
 ************************************************************************/
$(document).delegate("#linkNodeConfigModalTrigger", "click", function() {
  console.log("inside linkNodeConfigModalTrigger click");
  //$("#linkNodeConfigModal").modal("show");

  if (nptGlobals.LinkNodeConfigModalId) {
    if (nptGlobals.LinkNodeConfigModalId)
      $(nptGlobals.LinkNodeConfigModalId).show();
    let $maximizeModal = $(".maximize-modal").parent();
    $maximizeModal.remove();
  } else {
    $("#linkNodeConfigModal").modal({
      backdrop: false,
      keyboard: true
    });

    $("#linkNodeConfigModal").show();

    $("#linkNodeConfigModal .modal-dialog")
      //    	.resizable({
      //    	    minWidth: 600,
      //    	    minHeight:600,
      //    	    //autoHide: true,
      //    	    containment:'parent',
      //    	  //  alsoResize:'#linkNodeConfigModal .genericModalContent,#linkNodeConfigModal .genericModalFooter'
      //    	})
      .draggable({
        handle:
          "#modalContentlinkNodeConfig > div.col-md-12.col-sm-12.modal-header.zeroPadding"
      });
    canvasActive(); // Makes canvas tab active
    linkInfoModalTab();
    nodeInfoModalTab();
  }
});

/************************************************************************
 * Apply to all fields on Right click  (contextmenu)
 ************************************************************************/

var tdIndex, value;
$(document).delegate("#linkNodeConfigModal .editable", "click", function(e) {
  //console.log("Editable buttons clicked")
  // $("#linkNodeConfigModal .editable-buttons .editable-submit").empty().html("Apply to all");
  $("#applyToAllDiv").remove();
});

/************************************************************************
 * Apply to all div popup link  (contextmenu)
 ************************************************************************/
$(document).delegate(
  "#linkNodeConfigModal #nodeConfigTab .editable",
  "contextmenu",
  function(e) {
    e.preventDefault();
    indexx = $(this)
      .closest("td")
      .index();
    // var trIndex=$(this).closest('td').parent().index();
    value = $(this)
      .closest("td")
      .find("span")
      .editable("getValue").undefined;
    console.log(value);
    console.log("col index :" + indexx);
    if (indexx != 2) {
      var div = $(
        "<div id='applyToAllDiv'><button class='btn' id='applyToAllBtnNode'>Apply to all rows.</button></div>"
      ).css({
        position: "absolute",
        left: e.clientX,
        top: e.clientY,
        "z-index": 1100
      });
      $(document.body).append(div);
    } else {
      console.log("Station Name will be unique , so no apply to all");
    }
  }
);

/************************************************************************
 * Apply to all div popup node  (contextmenu)
 ************************************************************************/
$(document).delegate(
  "#linkNodeConfigModal #linkConfigTab .editable",
  "contextmenu",
  function(e) {
    e.preventDefault();
    indexx = $(this)
      .closest("td")
      .index();
    // var trIndex=$(this).closest('td').parent().index();
    value = $(this)
      .closest("td")
      .find("span")
      .editable("getValue").undefined;
    console.log(value);
    console.log("col index :" + indexx);
    var div = $(
      "<div id='applyToAllDiv'><button class='btn' id='applyToAllBtnLink'>Apply to all</button></div>"
    ).css({
      position: "absolute",
      left: e.clientX,
      top: e.clientY,
      "z-index": 1100
    });
    $(document.body).append(div);
  }
);

/************************************************************************
 * Apply to all fields on  click  link (contextmenu)
 ************************************************************************/
$(document).delegate("#applyToAllBtnLink", "click", function(e) {
  var $rows = $("#linkInfoTable tbody tr").each(function(index) {
    //console.log($(this).find("td").eq(indexx).find('span').html(value));
    //$(this).find("td").eq(indexx).find('span').html(value);
    $(this)
      .find("td")
      .eq(indexx)
      .find("span")
      .editable("setValue", value);
    //$(this).find("td").eq(indexx).find('span').editable("validate");
    //console.log($(this).find("td").eq(indexx).find('span'));
    //		/$(this).find("td").eq(indexx).find('.editable').editable("validate");
  });
  $("#applyToAllDiv").remove();
  console.log($("#linkNodeConfigModal #linkConfigTab .spanLengthLinkInfoTab"));
  isApplyAllEdit = 1;
  $("#linkNodeConfigModal #linkConfigTab .spanLengthLinkInfoTab").editable(
    "validate"
  );
  isApplyAllEdit = 0;
});

/************************************************************************
 * Apply to all fields click Node  (contextmenu)
 ************************************************************************/
$(document).delegate("#applyToAllBtnNode", "click", function(e) {
  var $rows = $("#nodeInfoTable tbody tr").each(function(index) {
    //console.log($(this).find("td").eq(indexx).find('span').html(value));
    //$(this).find("td").eq(indexx).find('span').html(value);
    $(this)
      .find("td")
      .eq(indexx)
      .find("span")
      .editable("setValue", value);
  });
  $("#applyToAllDiv").remove();
});

/************************************************************************
 * Apply to all fields click Node  (contextmenu)
 ************************************************************************/
$(document).delegate("#linkInfoTable tr", "click", function(e) {
  var linkId = $(this)
    .find("td")
    .eq(0)
    .html();
  console.log("LinkId::", linkId);
});

/************************************************************************
 * Insert Ila trigger - Link Node Config Modal
 ************************************************************************/
$(document).delegate(
  "#linkNodeConfigModal #insertIlaLinkNodeConfig",
  "click",
  function(e) {
    console.log("Insert ILA link node config");
    e.preventDefault();
    canvasActive(); // Makes canvas tab active

    rowIndex = $(this)
      .closest("td")
      .parent()
      .index();
    console.log("Row index :" + rowIndex);

    selectedRowIlaInsertionLinkConfig = rowIndex;

    var $columns = $(this)
      .closest("td")
      .parent()
      .children(); // Array of td in this tr

    totalLinksBefore = graph.getLinks().length;
    console.log("Graph Links :" + totalLinksBefore);

    var linkId = $columns
        .eq(0)
        .find("span")
        .html(),
      srcNodeId = $columns
        .eq(1)
        .find("span")
        .html(),
      destNodeId = $columns
        .eq(3)
        .find("span")
        .html(),
      spanlength = $columns
        .eq(5)
        .find("span")
        .html(),
      calculatedSpanLoss = $columns
        .eq(6)
        .find("span")
        .html(),
      fibertype = $columns
        .eq(7)
        .find("span")
        .editable("getValue").undefined,
      color = $columns
        .eq(8)
        .find("span")
        .editable("getValue").undefined,
      srlg = $columns
        .eq(9)
        .find("span")
        .html(),
      costmetric = $columns
        .eq(10)
        .find("span")
        .html(),
      splice = $columns
        .eq(11)
        .find("span")
        .html(),
      lossPerSplice = $columns
        .eq(12)
        .find("span")
        .html(),
      connector = $columns
        .eq(13)
        .find("span")
        .html(),
      lossPerConnector = $columns
        .eq(14)
        .find("span")
        .html(),
      coefficient = $columns
        .eq(15)
        .find("span")
        .html(),
      linkType = $columns
        .eq(17)
        .find("span")
        .html();

    var linkCell = getLinkBetweenSrcandDestNodes(
      getNodeById(srcNodeId),
      getNodeById(destNodeId)
    );
    console.log("Link where ILA will be inserted from Link Node Config Tab");
    console.log(linkCell);

    linkUserInput = {
      id: "",
      cid: "",
      fibertype: fibertype,
      cd: "",
      pmd: "",
      cdCoefficient: "",
      pmdCoefficient: "",
      costmetric: costmetric,
      opticalparameter: "",
      color: color,
      srlg: srlg,
      spanlength: spanlength,
      coefficient: coefficient,
      calculatedSpanLoss: calculatedSpanLoss,
      adjustableSpanLoss: calculatedSpanLoss,
      splice: splice,
      lossPerSplice: lossPerSplice,
      connector: connector,
      lossPerConnector: lossPerConnector,
      linkType: linkType,
      updateLinkConfigTab: true // Check to update node-link config table data
    };

    IlaModule.validateIlaInsertion(linkCell, linkUserInput);
  }
);

var updateLinkNodeConfigTab = function() {
  var totalLinks,
    previousLinkId,
    $tr,
    linkArray,
    template = "",
    i;
  console.log(
    "selectedRowIlaInsertionLinkConfig :" + selectedRowIlaInsertionLinkConfig
  );
  $tr = $("#linkNodeConfigModal #linkConfigTab tbody")
    .children()
    .eq(selectedRowIlaInsertionLinkConfig);
  console.log($tr);
  console.log(
    $tr
      .children()
      .eq(0)
      .find("span.badge")
      .html()
  );
  previousLinkId = $tr
    .children()
    .eq(0)
    .find("span.badge")
    .html();
  console.log("previousLinkId :" + previousLinkId);
  $tr.remove(); // remove the Row where ILA needs to be inserted

  linkArray = graph.getLinks().filter(function(element) {
    //console.log(element);
    //console.log("Previous Link Id:"+previousLinkId);
    return (
      element.get("linkProperties").linkId == previousLinkId ||
      element.get("linkProperties").linkId > totalLinksBefore
    );
  });

  console.log("linkArray :: ", linkArray);

  for (i = 0; i < linkArray.length; i++) {
    console.log("LinkId ::", linkArray[0].attributes.linkProperties.linkId);
    template += `<tr>`;

    template += `<td><span class="badge-red badge">${
      linkArray[i].attributes.linkProperties.linkId
    }</span></td>`;
    src = graph.getCell(linkArray[i].attributes.source.id);

    template += `<td><span class="badge">${
      src.attributes.nodeProperties.nodeId
    }</span></td>`;
    template += `<td>${src.attributes.nodeProperties.stationName}-${
      src.attributes.nodeProperties.siteName
    }<span class="badge">${src.get("type").substring(5)}</span></td>`;

    dest = graph.getCell(linkArray[i].attributes.target.id);
    //console.log("Source"+el.attributes.target.id);
    template += `<td><span class="badge">${
      dest.attributes.nodeProperties.nodeId
    }</span></td>`;
    template += `<td>${dest.attributes.nodeProperties.stationName}-${
      dest.attributes.nodeProperties.siteName
    }<span class="badge">${dest.get("type").substring(5)}</span></td>`;
    //console.log("Target"+el.attributes.target.id);
    /* template += `<td>${el.attributes.linkProperties.spanLength }</td>`;
	        //console.log("Target"+el.attributes.target.id);
	        template += `<td>${el.attributes.linkProperties.calculatedSpanLoss }</td>`;
	        //console.log("Target"+el.attributes.target.id);
	        template += `</tr>`;*/

    template += `<td><span class="spanLengthLinkInfoTab"></span></td>
				<td><span class="spanLossLinkInfoTab"></span></td>
				<td><span class="fiberTypeLinkInfoTab"></span></td>
				<td><span class="colorLinkInfoTab"></span></td>	
				<td><span class="srlgLinkInfoTab"></span></td>
				<td><span class="costMetricLinkInfoTab"></span></td>
				<td><span class="numOfSplicesLinkInfoTab"></span></td>
				<td><span class="lossPerSpliceLinkInfoTab"></span></td>
				<td><span class="numOfConnectorsLinkInfoTab"></span></td>
				<td><span class="lossPerConnectorLinkInfoTab"></span></td>
				<td><span class="coefficientLinkInfoTab"></span></td>	
				<td><span class="omsPtcLinkInfoTab"></span></td>		
	        	<td><span class="linkTypeLinkInfoTab"></span></td>`;
    template += `<td><button id="insertIlaLinkNodeConfig" class="btn">Insert</button></td></tr>`;
  }

  $("#linkNodeConfigModal #linkConfigTab tbody").append(template);

  for (j = 0; j < linkArray.length; j++) {
    i = j + totalLinksBefore - 1;
    console.log("Value of J :" + j + " and Value of i:" + i);
    let spanLength = linkArray[j].get("linkProperties").spanLength,
      spanLoss = linkArray[j].get("linkProperties").calculatedSpanLoss,
      fiberType = linkArray[j].get("linkProperties").fiberType,
      color = linkArray[j].get("linkProperties").color,
      srlg = linkArray[j].get("linkProperties").srlg,
      costMetric = linkArray[j].get("linkProperties").costMetric,
      numOfSplices = linkArray[j].get("linkProperties").splice,
      lossPerSplice = linkArray[j].get("linkProperties").lossPerSplice,
      coefficient = linkArray[j].get("linkProperties").coefficient,
      connector = linkArray[j].get("linkProperties").connector,
      lossPerConnector = linkArray[j].get("linkProperties").lossPerConnector,
      omsProtection = linkArray[j].get("linkProperties").lineProtection,
      linkType = linkArray[j].get("linkProperties").linkType;

    $(".spanLengthLinkInfoTab:eq(" + i + ")")
      .attr("title", "Edit ")
      .tooltip()
      .editable({
        type: "text",
        value: spanLength,
        /*validate:function(value,$(this))
				         {
				        	 return fValidateLinkSpanLength(value,$(this));		  
				         },*/
        onblur: "submit",
        validate: function(value) {
          console.log("Validate :");
          console.log("Value :" + value);
          //console.log($(this));
          //console.log($($(this)["0"].$element["0"]));
          //console.log($(this.val)
          return spanLenghthSpanLossValidation(value, $(this));
        },
        savenochange: true
      });

    $(".spanLossLinkInfoTab:eq(" + i + ")")
      .attr("title", "Edit ")
      .tooltip()
      .editable({
        type: "text",
        value: spanLoss,
        validate: function(value) {
          return fValidateLinkSpanLoss(value, $(this));
        },
        onblur: "submit"
      });

    $(".fiberTypeLinkInfoTab:eq(" + i + ")")
      .attr("title", "Edit ")
      .tooltip()
      .editable({
        type: "select",
        value: fiberType,
        source: [{ value: 1, text: "G.652.D" }, { value: 2, text: "G.655" }],
        validate: function(value) {
          // return nodeCapacityValidation(value,$(this));
        },
        onblur: "submit"
      });

    $(".colorLinkInfoTab:eq(" + i + ")")
      .attr("title", "Edit ")
      .tooltip()
      .editable({
        type: "select",
        value: color,
        source: [
          { value: 0, text: "Default" },
          { value: 1, text: "Violet" },
          { value: 2, text: "Indigo" },
          { value: 3, text: "Blue" },
          { value: 4, text: "Green" },
          { value: 5, text: "Yellow" },
          { value: 6, text: "Orange" },
          { value: 7, text: "Red" }
        ],
        onblur: "submit"
      });

    $(".srlgLinkInfoTab:eq(" + i + ")")
      .attr("title", "Edit ")
      .tooltip()
      .editable({
        type: "text",
        value: srlg,
        onblur: "submit"
      });

    $(".costMetricLinkInfoTab:eq(" + i + ")")
      .attr("title", "Edit ")
      .tooltip()
      .editable({
        type: "text",
        value: costMetric,
        onblur: "submit"
      });

    $(".numOfSplicesLinkInfoTab:eq(" + i + ")")
      .attr("title", "Edit ")
      .tooltip()
      .editable({
        type: "text",
        value: numOfSplices,
        onblur: "submit",
        validate: function(value) {
          return splicesSpanLossValidation(value, $(this));
        }
      });

    $(".lossPerSpliceLinkInfoTab:eq(" + i + ")")
      .attr("title", "Edit ")
      .tooltip()
      .editable({
        type: "text",
        value: lossPerSplice,
        onblur: "submit",
        validate: function(value) {
          return lossPerSpliceSpanLossValidation(value, $(this));
        }
      });

    $(".numOfConnectorsLinkInfoTab:eq(" + i + ")")
      .attr("title", "Edit ")
      .tooltip()
      .editable({
        type: "text",
        value: connector,
        onblur: "submit",
        validate: function(value) {
          return numOfConnectorSpanLossValidation(value, $(this));
        }
      });

    $(".lossPerConnectorLinkInfoTab:eq(" + i + ")")
      .attr("title", "Edit ")
      .tooltip()
      .editable({
        type: "text",
        value: lossPerConnector,
        onblur: "submit",
        validate: function(value) {
          return lossPerConnectorSpanLossValidation(value, $(this));
        }
      });

    $(".coefficientLinkInfoTab:eq(" + i + ")")
      .attr("title", "Edit ")
      .tooltip()
      .editable({
        type: "text",
        value: coefficient,
        onblur: "submit",
        validate: function(value) {
          return coefficientSpanLossValidation(value, $(this));
        }
      });

    $(".omsPtcLinkInfoTab:eq(" + i + ")")
      .attr("title", "Edit ")
      .tooltip()
      .editable({
        type: "select",
        value: omsProtection,
        source: yesNoSrc,
        onblur: "submit",
        validate: function(value) {
          return omsProtectionValidation(value, $(this));
        }
      });

    $(".linkTypeLinkInfoTab:eq(" + i + ")")
      .attr("title", "Edit ")
      .tooltip()
      .editable({
        type: "select",
        value: linkType,
        source: linkTypeSrc,
        onblur: "submit",
        validate: function(value) {
          console.log("&&&&&&&&&& Link Type Value Changed to ", value);
          return linkTypeValidation(value, $(this));
        }
      });
  }

  setallLinkAttributes();
};
