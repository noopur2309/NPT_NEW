/*******************************************************************************
 * Function to disable default pointer down event in rappid on right click
 ******************************************************************************/
paper.options.guard = function(evt) {
  return evt.type === "mousedown" && evt.buttons === 2;
};

/*******************************************************************************
 * Function to set the properties of node from properties form
 ******************************************************************************/
//save/update element properties using user input
$("body").delegate("#nodePropertySave", "click", function() {
  //	e.preventDefault(e);
  //console.log(event);

  console.log("Node Property Form :", $("#propertyFormNode").serialize());
  //setNodeProperties();

  //    //store values of user input in variable
  //	var id = $("#eid").val(),
  //	cid = $("#cid").val(),
  //	sitename = $("#siteName").val(),
  //	stationname = $("#stationName").val(),
  //	gneflag = $("#gneFlag").val(),
  //	vlantag = $("#vlanTag").val();
  //	emsip = $("#emsip").val();
  //	capacity = $("#capacity").val();
  //	direction = $("#direction").val();
  //	ipv6 = $("#ipv6").val();
  //	subnet = $("#subnet").val();
  //	gateway = $("#gateway").val();
  //	//opticalreach = $("#opticalreach").val();
  //    //degree = $("#degree").val();
  //    console.log(id, sitename, stationname);
  //
  //    var nodeUserInput={'id':id,'cid':cid,'sitename':sitename,'stationname':stationname,'gneflag':gneflag,'vlantag':vlantag};
  //    console.log(nodeUserInput);
  //    //get cell using id
  //    var cell = graph.getCell(cid);
  //    //console.log("station:"+stationName);
  //    //console.log(cell);
  //
  //    var isNodePropertiesValid=validateNodeProperties(cell,nodeUserInput);
  //
  //    if(isNodePropertiesValid)
  //    	{
  //    	//set attributes of object/element/link properties defined
  //        cell.attributes.nodeProperties.nodeId = id;
  //        cell.attributes.nodeProperties.stationName = stationname;
  //        cell.attributes.nodeProperties.siteName = sitename;
  //        cell.attributes.nodeProperties.gneFlag = gneflag;
  //        cell.attributes.nodeProperties.vlanTag = vlantag;
  //        cell.attributes.nodeProperties.emsip = emsip;
  //        cell.attributes.nodeProperties.capacity = capacity;
  //        cell.attributes.nodeProperties.direction = direction;
  //        cell.attributes.nodeProperties.ipv6 = ipv6;
  //        cell.attributes.nodeProperties.subnet = subnet;
  //        cell.attributes.nodeProperties.gateway = gateway;
  //        //cell.attributes.nodeProperties.opticalReach = opticalreach;
  //        //cell.attributes.nodeProperties.degree = degree;
  //        //	cell.attr('attributes/siteName',siteName);
  //        //	cell.attr('attributes/stationName',stationName);
  //        //console.log(cell.attr('attributes/siteName'));
  //        //console.log(cell.attr('attributes/stationName'));
  //        //console.log('Station ka Nam'+cell.attributes.stationName);
  //
  //        //Update Node Tab
  //        nodeInfoTab();
  //        bootbox.dialog({
  //            message: '<p class="text-center text-success lead">Node Properties saved successfully</p>',
  //            closeButton: false
  //        });
  //    	closeBootBoxAlert();//close bootbox after 1 second - drag-drop
  //    	}
  //    else
  //    	{
  //    	console.log("Node Properties cannot be set");
  //    	}
  //    //alert("Changes saved");
});

/*******************************************************************************
 * Function to set the properties of link from properties form
 ******************************************************************************/
//save/update link properties using user input
$("body").delegate("#linkPropertySave", "click", function() {
  // e.preventDefault(e);
  // console.log(event);
  // setLinkProperties();
  // store values of user input in variable
  // var id = $("#lid").val(),
  // cid = $("#cid").val(),
  // fibertype = $("#fiberType").val(),
  // cd = $("#cd").val(),
  // pmd = $("#pmd").val(),
  // cdCoefficient= $("#cdCoefficient").val(),
  // pmdCoefficient= $("#pmdCoefficient").val(),
  // splice = $("#splice").val(),
  // lossPerSplice = $("#lossPerSplice").val(),
  // connector = $("#connector").val(),
  // lossPerConnector = $("#lossPerConnector").val(),
  // coefficient = $("#coefficient").val(),
  // calculatedSpanLoss = $("#calculatedSpanLoss").val(),
  // adjustableSpanLoss = $("#adjustedSpanLoss").val(),
  // spanlength = $("#spanLength").val(),
  // costmetric = $("#costMetric").val(),
  // opticalparameter = $("#opticalParameter").val(),
  // color = $("#color").val(),
  // srlg = $("#srlg").val();
  // console.log(color, spanlength, srlg);
  //
  // window.linkUserInput={'id':id,'cid':cid,'fibertype':fibertype,'cd':cd,'pmd':pmd,'cdCoefficient':cdCoefficient,'pmdCoefficient':pmdCoefficient,
  // 'costmetric':costmetric,'opticalparameter':opticalparameter,'color':color,'srlg':srlg,'spanlength':spanlength,
  // 'coefficient':coefficient,'calculatedSpanLoss':calculatedSpanLoss,'adjustableSpanLoss':adjustableSpanLoss,
  // 'splice':splice,'lossPerSplice':lossPerSplice,'connector':connector,'lossPerConnector':lossPerConnector};
  //
  // console.log(window.linkUserInput);
  // //get cell using id
  // var cell = graph.getCell(cid);
  // //console.log("station:"+stationName);
  // console.log(cell);
  // isLinkPropertiesValid=0;
  //
  //
  // /*if(adjustableSpanLoss == 0 || adjustableSpanLoss< minSpanLoss ||
  // adjustableSpanLoss>maxSpanLoss){
  // //bootBoxAlert("Span Loss(Adjusted) is : "+ adjustableSpanLoss+", Please
  // Readjust link parameters to make it between 20 to 35!");
  // bootBoxAlert("Link is not feasible one, Please Change the parameters to
  // make spanloss in range of 20 to 35 ");
  // $("#adjustedSpanLoss").focus();
  // console.log("After Link not complete Error!");
  // }
  // console.log("Checked link properties ..");*/
  //
  // if(validateLinkProperties())
  // {
  // if(/*spanlength>80 || */calculatedSpanLoss>35 )
  // validateIlaInsertion(cell,window.linkUserInput);
  // else isLinkPropertiesValid=1;
  // }
  // /*else if(spanlength>80 ){
  // validateIlaInsertion(cell,linkUserInput);
  // }*/
  //
  //
  //
  //
  // if(isLinkPropertiesValid)
  // {
  // setLinkColor(cell,color);
  // //set attributes of object/element/link properties defined
  // cell.attributes.linkProperties.linkId = id;
  // cell.attributes.linkProperties.spanLength = spanlength;
  // cell.attributes.linkProperties.fiberType = fibertype;
  // cell.attributes.linkProperties.cd = cd;
  // cell.attributes.linkProperties.pmd = pmd;
  // cell.attributes.linkProperties.cdCoefficient = cdCoefficient;
  // cell.attributes.linkProperties.pmdCoefficient = pmdCoefficient;
  // cell.attributes.linkProperties.splice = splice;
  // cell.attributes.linkProperties.lossPerSplice = lossPerSplice;
  // cell.attributes.linkProperties.connector = connector;
  // cell.attributes.linkProperties.lossPerConnector = lossPerConnector;
  // cell.attributes.linkProperties.coefficient = coefficient;
  // cell.attributes.linkProperties.calculatedSpanLoss = calculatedSpanLoss;
  // cell.attributes.linkProperties.adjustableSpanLoss = adjustableSpanLoss;
  // cell.attributes.linkProperties.costMetric = costmetric;
  // cell.attributes.linkProperties.opticalParameter = opticalparameter;
  // cell.attributes.linkProperties.srlg = srlg;
  // cell.attributes.linkProperties.color = color;
  // //cell.attr('attributes/stationName',stationName);
  //
  // //Update link Info Tab
  // linkInfoTab();
  //
  // bootbox.dialog({
  // message: '<p class="text-center text-success lead">Link Properties saved
  // successfully</p>',
  // closeButton: false
  // });
  // closeBootBoxAlert(); //close bootbox after 1 second - drag-drop
  // //alert("Changes saved");
  // }
  // console.log("Getting out of linkPropertySave");
});

/*
 * $("#color").on('change',function(){ setLinkColor(); });
 */

/*******************************************************************************
 * *********paper events handling******************
 * *****************************************/

paper.on("cell:pointerclick", function(cellView, evt, x, y) {
  // console.log("Previously selected Newtork Element" +
  // nptGlobals.elSelected);
  console.log(nptGlobals.elSelected);
  if (typeof nptGlobals.elSelected != "undefined") {
    if (nptGlobals.elSelected.isLink()) {
      colorOfLink = nptGlobals.elSelected.get("linkProperties").color;
      // console.log("colorOfLink :: "+colorOfLink+" Color
      // ::"+getColorFromCode(colorOfLink))
      var isLineProtected = nptGlobals.elSelected.get("linkProperties")
        .lineProtection;
      if (isLineProtected == 1)
        nptGlobals.elSelected.attr(attrs.linkDefaultLinePtc);
      else nptGlobals.elSelected.attr(attrs.linkDefault);
      setLinkColorFromLinkProperties(nptGlobals.elSelected);
      nptGlobals.elSelected.attr(
        ".connection/stroke",
        getColorFromCode(colorOfLink)
      );
    } else {
      // console.log("Previously Selected Element
      // :"+nptGlobals.elSelected.get('type'));
      // if (nptGlobals.elSelected.get('nodeProperties').faultNode == 1) {
      // nptGlobals.elSelected.attr(attrs.elementFault);
      // }
      // // else if (nptGlobals.elSelected.get('type') ==
      // nptGlobals.NodeTypeILA)
      // // nptGlobals.elSelected.attr(attrs.IlaDefault);
      // // else if (nptGlobals.elSelected.get('type') ==
      // nptGlobals.NodeTypeTE)
      // // nptGlobals.elSelected.attr(attrs.TeDefault);
      // else
      // nptGlobals.elSelected.attr(attrs.elementDefault);
      console.log("elselected", nptGlobals.elSelected);
      let elSelectedView = paper.findViewByModel(nptGlobals.elSelected);
      elSelectedView.unhighlight();
    }
  }

  nptGlobals.elSelected = cellView.model;
  if (!cellView.model.isLink()) {
    var halo = new joint.ui.Halo({ cellView: cellView });
    halo.render();
    cellView.highlight();
    console.log(
      /* "elSelected:", elSelected, */ " nodeConnections:",
      nptGlobals.elSelected.get("nodeProperties").nodeConnections,
      " Node Properties:",
      nptGlobals.elSelected.get("nodeProperties")
    );
  } else {
    nptGlobals.elSelected.attr(attrs.linkHighlighted);

    if (nptGlobals.elSelected.get("linkProperties").isIlaLink == "1") {
      // $("#srlg").attr('disabled','true');
      // $("#color").attr('disabled','true');
    }
    // initializeLinkProperties(elSelected);
    console.log(
      "Link elSelected:",
      nptGlobals.elSelected,
      // " Src Dir:",
      // nptGlobals.elSelected.get("linkProperties").SrcNodeDirection,
      // " Dest Dir:",
      // nptGlobals.elSelected.get("linkProperties").DestNodeDirection
      "Link Src:",
      nptGlobals.elSelected.get("source"),
      "Link Dest:",
      nptGlobals.elSelected.get("target")
    );
  }

  // hidePath();
  // properties(nptGlobals.elSelected);

  // if (nptGlobals.elSelected.isLink()) {
  // nptGlobals.elSelected.attr(attrs.linkHighlighted);

  // if (nptGlobals.elSelected.get('linkProperties').isIlaLink == '1') {
  // //$("#srlg").attr('disabled','true');
  // //$("#color").attr('disabled','true');
  // }
  // //initializeLinkProperties(nptGlobals.elSelected);
  // console.log("Link elSelected:", nptGlobals.elSelected," Link
  // Properties:",nptGlobals.elSelected.get('linkProperties'));
  // }
  // else {
  // // if (nptGlobals.elSelected.get('nodeProperties').faultNode == 0) {
  // // nptGlobals.elSelected.attr(attrs.elementSelected);
  // // }
  // //initializeNodeProperties(nptGlobals.elSelected);
  // console.log("elSelected:", nptGlobals.elSelected, " nodeConnections:",
  // nptGlobals.elSelected.get('nodeProperties').nodeConnections," Node
  // Properties:",nptGlobals.elSelected.get('nodeProperties'));
  // }
});

//function fMakeNodeMovable(ctrlIsPressed) {
//console.log("cntrlIsPressed:",cntrlIsPressed);
//_.each(graph.getElements(), function (el) {
//if (ctrlIsPressed) {
//el.attr('image/magnet', ctrlIsPressed);
//} else {
//// el.attr('image/magnet', ctrlIsPressed);
//el.removeAttr('image/magnet');
//}
//});
//}

$(document).on("keydown", function(event) {
  // if (event.which == "17") {
  // //console.log("Ctrl key pressed");
  // cntrlIsPressed = true;
  // fMakeNodeMovable(cntrlIsPressed);
  // }
  if (event.which == "46") {
    if (nptGlobals.elSelected) nptGlobals.elSelected.remove();
  }
});

//$(document).keyup(function (event) {
//console.log(event);
//cntrlIsPressed = false;
//fMakeNodeMovable(cntrlIsPressed);
//});

//var cntrlIsPressed = false;

paper.on("blank:pointerclick", function(evt, x, y) {
  // console.log('hello');
  // setColorOnClick();
  hidePath();
  removeNodeHighlight();
  removeCommonColumnsBkg();

  // Hide Info Box for Path
  fShowPathInfoBox(false);

  fRemovePaperContextmenu();

  fRemoveCircuitView();

  // Hide optical power text for Input and Output
  if (nptGlobals.opticalPowerPathNodesArr.length > 0)
    fHideOpticalPowerPathNodes();

  if (typeof nptGlobals.elSelected != "undefined") {
    if (nptGlobals.elSelected.isLink()) {
      colorOfLink = nptGlobals.elSelected.get("linkProperties").color;
      // console.log("colorOfLink :: "+colorOfLink+" Color
      // ::"+getColorFromCode(colorOfLink))
      var isLineProtected = nptGlobals.elSelected.get("linkProperties")
        .lineProtection;
      if (isLineProtected == 1)
        nptGlobals.elSelected.attr(attrs.linkDefaultLinePtc);
      else nptGlobals.elSelected.attr(attrs.linkDefault);
      setLinkColorFromLinkProperties(nptGlobals.elSelected);
      nptGlobals.elSelected.attr(
        ".connection/stroke",
        getColorFromCode(colorOfLink)
      );

      /*
       * if(nptGlobals.elSelected.get('linkProperties').isIlaLink=='0') {
       * //console.log("attrs.linkDefault cell:pointerclick");
       * nptGlobals.elSelected.attr(attrs.linkDefault);
       * setLinkColorFromLinkProperties(nptGlobals.elSelected);
       * //nptGlobals.elSelected.attr('.connection/stroke',getColorFromCode(colorOfLink)); }
       * else { //console.log("attrs.IlalinkDefault cell:pointerclick");
       * nptGlobals.elSelected.attr(attrs.IlalinkDefault);
       * setLinkColorFromLinkProperties(nptGlobals.elSelected);
       * //nptGlobals.elSelected.attr('.connection/stroke',getColorFromCode(colorOfLink)); }
       */
    } else {
      // // console.log(nptGlobals.elSelected.get('type'));
      // // nptGlobals.elSelected.attr('.body/stroke', 'black');
      // // nptGlobals.elSelected.attr('rect/stroke-width', '2');
      // // nptGlobals.elSelected.attr('.label/stroke', 'black');
      //
      // if (nptGlobals.elSelected.get('nodeProperties').faultNode == 1) {
      // nptGlobals.elSelected.attr(attrs.elementFault);
      // }
      // // else if (nptGlobals.elSelected.get('type') == nptGlobals.NodeTypeILA)
      // // nptGlobals.elSelected.attr(attrs.IlaDefault);
      // // else if (nptGlobals.elSelected.get('type') == nptGlobals.NodeTypeTE)
      // // nptGlobals.elSelected.attr(attrs.TeDefault);
      // else
      // nptGlobals.elSelected.attr(attrs.elementDefault);
      // nptGlobals.elSelected = undefined;

      // console.log("nptGlobals.elselected ::
      // ",nptGlobals.elSelected);
      let elSelectedView = paper.findViewByModel(nptGlobals.elSelected);
      elSelectedView.unhighlight();
    }
  }

  $("#DemandMatrixTable tbody #pathColumn")
    .filter(function() {
      return (
        $(this).css("color") != "rgb(51,51,51)" &&
        !$(this).hasClass("rejectedWithError")
      );
    })
    .css({ color: "black", "font-size": "14px" });
});

//paper.on('blank:mouseover', function(cellView, evt, x, y) {
//$("#propertiesPopUp,").remove();
//});

paper.on("cell:mouseover", function(cellView, evt, x, y) {
  // console.log('mouseover');

  if (nptGlobals.elSelected) {
    // var path = graph.shortestPath(nptGlobals.elSelected, cellView.model);
    // showPath(path);
    // cellView.model.attr(attrs.elementHighlighted);
  }

  var scale = V(paper.viewport).scale();
  // console.log("x:"+x+" y:"+y+" evnt.x:"+evt.x+" evnt.y:"+evt.y);

  var upArrow = `<span id="upArrow"><i class="fa fa-caret-up" aria-hidden="true"></i></span>`;

  if (cellView.model.isLink()) {
    let template = `<p class="cell-tooltip__para">Link Id : ${
      cellView.model.get("linkProperties").linkId
    }</p>
			<p class="cell-tooltip__para">SpanLength : ${
        cellView.model.get("linkProperties").spanLength
      }</p>
			<p class="cell-tooltip__para">SD: ${cellView.model.source().dir} DD: ${
      cellView.model.target().dir
    }</p>
			<p class="cell-tooltip__para">SN: ${cellView.model.get("source").NodeId} DN: ${
      cellView.model.get("target").NodeId
    }</p>`;

    // console.log(template);
    let div = $('<div id="propertiesPopUp" class="cell-tooltip">').css({
      left: evt.clientX + 10,
      top: evt.clientY + 10
    });

    div.append(template);

    // console.log(div);
    $(document.body).append(div);
    $("#propertiesPopUp").show("find");
  } else {
    let gneFlag;
    if (cellView.model.get("nodeProperties").gneFlag == 1) gneFlag = "Yes";
    else gneFlag = "No";

    let template = `<p class="cell-tooltip__para">Node Id : ${
      cellView.model.get("nodeProperties").nodeId
    }</p>
	  <p class="cell-tooltip__para">Station Name : ${
      cellView.model.get("nodeProperties").stationName
    }</p>
			<p class="cell-tooltip__para">Site Name : ${
        cellView.model.get("nodeProperties").siteName
      }</p>
			<p class="cell-tooltip__para">Gne : ${gneFlag}</p>`;
    // console.log(template);
    let div = $('<div id="propertiesPopUp" class="cell-tooltip">').css({
      left: evt.clientX + 10,
      top: evt.clientY + 10
    });

    div.append(template);

    // console.log(div);
    $(document.body).append(div);
    $("#propertiesPopUp").show("find");
  }
});

paper.on("cell:mouseout", function(cellView, evt, x, y) {
  // console.log('mouseout');
  // cellView.model.attr('.inPorts circle/fill', 'rgba(128, 128, 128, 0)');
  // cellView.model.attr('.outPorts circle/fill', 'rgba(128, 128, 128, 0)');
  // cellView.model.attr(attrs.elementDefault);
  // hidePath();

  // $("#propertiesPopUp").remove();
  $(".cell-tooltip").remove();
  // fRemovePaperContextmenu();

  if (cellView.model.isLink()) {
    // $('.tool-options path').hide().fadeOut("slow");
    /*
     * $('.tool-remove path').hide().fadeOut("slow");
     * $('.tool-remove').hide().fadeOut("slow");
     */
  } else {
    /*
     * $(".joint-port circle").css({fill: 'rgba(92, 184, 92, 0)',
     * 'fill-opacity': .8});
     */
  }
});

paper.on("cell:pointerdblclick", function(cellView, evt, x, y) {
  fInitiateLinkNodePropertyDialog(cellView.model);
});

function fInitiateLinkNodePropertyDialog(cellModel) {
  var cellPropertyEditTemplate, title;
  if (cellModel.isLink()) {
    console.log("Edit Link Properties");
    cellPropertyEditTemplate = getLinkTemplate(cellModel);

    let srcNode = graph.getCell(cellModel.get("source").id),
      destNode = graph.getCell(cellModel.get("target").id);

    let srcStationName = srcNode.get("nodeProperties").stationName,
      destStationName = destNode.get("nodeProperties").stationName;

    let spanLength = cellModel.get("linkProperties").spanLength,
      spanLoss = cellModel.get("linkProperties").calculatedSpanLoss;

    title = `Link ${
      cellModel.get("linkProperties").linkId
    } <p class="muted small">( ${srcStationName} to ${destStationName} )</p>
			<p class="muted small"> SpanLength : ${spanLength} | SpanLoss : ${spanLoss}</p>`;

    bootbox
      .dialog({
        message: cellPropertyEditTemplate,
        title: title,
        animation: true,
        className: "cell-property-dialog",
        backdrop: false,
        buttons: {
          Save: {
            // label: "Success!",
            className: "btn btn--default",
            callback: function() {
              // console.log("Saving Cell Properties");
              let formData = $("#propertyFormLink").serializeArray();
              console.log("Link Property Form :", formData);
              return setLinkProperties();
            }
          },
          "Insert ILA": {
            // label: "Success!",
            className: "btn btn--default",
            callback: function() {
              let linkFormData = fGetLinkPropertiesFormInput();
              let linkUserInput = linkFormData;
              IlaModule.validateIlaInsertion(cellModel, linkUserInput);
            }
          },
          Cancel: {
            className: "btn btn--default",
            callback: function() {}
          }
        }
      })
      .draggable();
    // initializeLinkProperties(cellModel);
  } else {
    console.log("Edit Node Properties");
    cellPropertyEditTemplate = getElementTemplate(cellModel);
    title = "Node " + cellModel.get("nodeProperties").nodeId;

    bootbox
      .dialog({
        message: cellPropertyEditTemplate,
        title: title,
        animation: true,
        className: "cell-property-dialog",
        backdrop: false,
        buttons: {
          Save: {
            // label: "Success!",
            className: "btn btn--default",
            callback: function() {
              let formData = $("#propertyFormNode").serializeArray();
              console.log("Node Property Form :", formData);
              setNodeProperties();
            }
          },
          Cancel: {
            className: "btn btn--default",
            callback: function() {}
          }
        }
      })
      .draggable();
    initializeNodeProperties(cellModel);
    propertyChecksForNode(cellModel);
  }
}

$("body").delegate("#ilaType", "change", function() {
  let ilaType = $(this).val(),
    numOfIla = 1,
    spanLoss = nptGlobals.IlaInsertion_SpanLoss;
  console.log("Ila type chnaged to ::", ilaType);

  if (ilaType == nptGlobals.getDefaultLinkStr()) {
    numOfIla = Math.floor(spanLoss / nptGlobals.getDefaultLinkMaxSpanLoss());
  } else if (ilaType == nptGlobals.getRamanHybridLinkStr()) {
    numOfIla = Math.floor(spanLoss / nptGlobals.getRamanHybridMaxSpanloss());
  } else if (ilaType == nptGlobals.getRamanDraLinkStr()) {
    numOfIla = Math.floor(spanLoss / nptGlobals.getRamanDraMaxSpanloss());
  }

  console.log(
    "ilaType::",
    ilaType,
    " numOfIla :: ",
    numOfIla,
    " SpanLoss::",
    spanLoss
  );
  let template = fGetManualIlaInsertionTemplate(ilaType, numOfIla);

  $("#ilaLinksInputDiv")
    .empty()
    .append(template);
});

paper.on("blank:contextmenu", function(evt, x, y) {
  fRemovePaperContextmenu();
  console.log("Context menu Add Nodes:", nptGlobals.contextMenuAddNodePos);
  const title = `<li class="paper-context-menu__ul--title text-center">Add Node</li>`;
  const addCdcRoadm = `<li onClick="insertRoadmNode(nptGlobals.contextMenuAddNodePos);fRemovePaperContextmenu()" class="paper-context-menu__ul--list-item" ><a href="#" data-toggle="tooltip" title="Add CDC Roadm"> ${
    nptGlobals.NodeTypeROADMDisplayName
  }</a></li>`;
  const addCdRoadm = `<li onClick="insertCdRoadmNode(nptGlobals.contextMenuAddNodePos);fRemovePaperContextmenu()" class="paper-context-menu__ul--list-item" ><a  href="#" data-toggle="tooltip" title="Add CD Roadm"> ${
    nptGlobals.NodeTypeCDROADMDisplayName
  }</a></li>`;
  const addTe = `<li onClick="insertTeNode(nptGlobals.contextMenuAddNodePos);fRemovePaperContextmenu()" class="paper-context-menu__ul--list-item" ><a  href="#" data-toggle="tooltip" title="Add TE"> ${
    nptGlobals.NodeTypeTEDisplayName
  }</a></li>`;
  const addIla = `<li onClick="insertIlaNode(nptGlobals.contextMenuAddNodePos);fRemovePaperContextmenu()" class="paper-context-menu__ul--list-item" ><a  href="#" data-toggle="tooltip" title="Add ILA"> ${
    nptGlobals.NodeTypeILADisplayName
  }</a></li>`;
  const addBsRoadm = `<li onClick="insertTwoDegreeRoadmNode(nptGlobals.contextMenuAddNodePos);fRemovePaperContextmenu()" class="paper-context-menu__ul--list-item" ><a  href="#" data-toggle="tooltip" title="Add BS Roadm"> ${
    nptGlobals.NodeTypeTwoDegreeRoadmDisplayName
  }</a></li>`;
  const addHub = `<li onClick="insertHubNode(nptGlobals.contextMenuAddNodePos);fRemovePaperContextmenu()" class="paper-context-menu__ul--list-item" ><a  href="#" data-toggle="tooltip" title="Add Hub"> ${
    nptGlobals.NodeTypeHubDisplayName
  }</a></li>`;
  const addPotp = `<li onClick="insertPotpNode(nptGlobals.contextMenuAddNodePos);fRemovePaperContextmenu()" class="paper-context-menu__ul--list-item" ><a  href="#" data-toggle="tooltip" title="Add Potp"> ${
    nptGlobals.NodeTypePotpDisplayName
  }</a></li>`;
  // var addTestNode = `<li onClick="insertTestNode(nptGlobals.contextMenuAddNodePos);fRemovePaperContextmenu()" class="paper-context-menu__ul--list-item" ><a  href="#" data-toggle="tooltip" title="Add Hub"> TestNode</a></li>`;

  // create div as tooltip around the node
  var div = $(
    '<div id="cell-add-context-menu" class="paper-context-menu">'
  ).css({
    left: evt.clientX,
    top: evt.clientY
  });

  console.log("evt", evt, " x::", x + " y::", y);
  nptGlobals.contextMenuAddNodePos.x = x;
  nptGlobals.contextMenuAddNodePos.y = y;

  var ulList = $('<ul class="paper-context-menu__ul text-center">');

  ulList.append(title);
  ulList.append(addCdcRoadm);
  ulList.append(addCdRoadm);
  ulList.append(addTe);
  ulList.append(addIla);
  ulList.append(addBsRoadm);
  ulList.append(addHub);
  ulList.append(addPotp);
  // ulList.append(addTestNode);

  div.append(ulList);

  // append tooltip to body,hence to node
  $(document.body).append(div);
});

function fRemovePaperContextmenu() {
  $("#cell-add-context-menu").remove();
  $("#toolTip").remove();
  $("#linkToolTip").remove();

  // Properties popup
  // $("#propertiesPopUp").remove();
}

paper.on("cell:pointerdown", function(cellView, evt, x, y) {
  $("#propertiesPopUp").remove();
  //  console.log("Map_____-", GisMap.getMap());
  GisMap.getMap().dragging.disable();
  nptGlobals.cellPointerDownPosition.x = x;
  nptGlobals.cellPointerDownPosition.y = y;
});

/*
 * link.on('change:target', function() { linkInfoTab(); })
 */

var reverseLinkSrc, reverseLinkDest, reverseLink;

paper.on("link:connect", function(cellView, evt, x, y) {
  console.log("link:connect");
  // console.log(cellView.model);

  let link = cellView.model;
  linkInfoTab();
  let src,
    dest,
    srcDefaultId = link.get("source").id,
    destDefaultId = link.get("target").id;

  src = graph.getCell(srcDefaultId);
  dest = graph.getCell(destDefaultId);

  // Update link connections in node properties 'nodeConnections' object
  fUpdateLinkConnectionProperties(link);

  let srcLat = src.get("nodeProperties").nodeLat,
    srcLong = src.get("nodeProperties").nodeLng,
    destLat = dest.get("nodeProperties").nodeLat,
    destLong = dest.get("nodeProperties").nodeLng;
  let spanLength = GisMap.distanceBetweenCoordinates(
    srcLat,
    srcLong,
    destLat,
    destLong
  );
  console.log("SpanLength in km", spanLength);
  //	link.get("linkProperties").spanLength = spanLength.toFixed(2);

  paperUtil.adjustVertices(graph, src);
  paperUtil.adjustVertices(graph, dest);

  setallLinkAttributes();
  linkInfoTab();
  networkInfoTab();

  GisMap.getMap().dragging.enable();
});

/*******************************************************************************
 * Update new link properties like Src & Dest Node Directions , NodeId and Did
 * parameter - link model
 ******************************************************************************/
function fUpdateLinkConnectionProperties(link) {
  let src = graph.getCell(link.attributes.source.id);
  let dest = graph.getCell(link.attributes.target.id);
  let srcDegree = paperUtil.fGetNodeDegree(src);
  let destDegree = paperUtil.fGetNodeDegree(dest);

  let srcRamanLinks = src.get("nodeProperties").ramanLinks,
    destRamanLinks = dest.get("nodeProperties").ramanLinks;

  console.log("srcRamanLinks", srcRamanLinks, "destRamanLinks", destRamanLinks);

  console.log(
    "******",
    { unchangedNodeId },
    { linkDisconnectFlag },
    link.source().id,
    link.target().id
  );

  // Update NodeId and Dir in link properties
  if (linkDisconnectFlag && unchangedNodeId == link.source().id) {
    console.log("Don't Update source");
  } else {
    link.get("source").NodeId = src.attributes.nodeProperties.nodeId;
    link.get("source").dir = srcDegree - srcRamanLinks;
  }

  if (linkDisconnectFlag && unchangedNodeId == link.target().id) {
    console.log("Don't update target");
  } else {
    link.get("target").NodeId = dest.attributes.nodeProperties.nodeId;
    link.get("target").dir = destDegree - destRamanLinks;
  }

  linkDisconnectFlag = false;

  console.log(
    "fUpdateLinkConnectionProperties() :",
    link.get("linkProperties").linkId
  );
}

/*******************************************************************************
 * Find direction in which target node is connected with node Parameters - node ,
 * targetNode
 ******************************************************************************/
function fGetLinkDirection(node, targetNode) {
  return (
    Number(
      _.indexOf(
        _.values(node.get("nodeProperties").nodeConnections),
        targetNode.get("nodeProperties").nodeId
      )
    ) + 1
  );
}

/* var dragStartPosition; */

var isDraggable = 0;
var dragScaleVar = V(paper.viewport).scale(),
  dragLastPosition = { x: dragScaleVar.sx, y: dragScaleVar.sy };
console.log("dragLastPosition::", dragLastPosition);

paper.on("blank:pointerdown" /* paperScroller.startPanning */, function(
  event,
  x,
  y
) {
  dragEvent(event, x, y);
});

function dragEvent(event, x, y) {
  let scale = V(paper.viewport).scale();
  dragStartPosition = { x: x * scale.sx, y: y * scale.sy };
  dragLastPosition = dragStartPosition;
  isDraggable = 1;
  // dragStartPosition = { x: x, y: y};
}

paper.on("cell:pointerup", function(cellView, evt, x, y) {
  GisMap.getMap().dragging.enable();

  paperUtil.adjustVertices(graph, cellView);
  console.log("paper mouse up");
  let cell = cellView.model || cellView;
  if (cell.isElement()) {
    let mouseUpMapCords = GisMap.getContextMenuCoords();
    console.log("mouseUpMapCords----", mouseUpMapCords);
    cell.get("nodeProperties").nodeLat = mouseUpMapCords.lat;
    cell.get("nodeProperties").nodeLng = mouseUpMapCords.lng;

    // If its a drag event and not a click event
    if (
      nptGlobals.cellPointerDownPosition.x != x ||
      nptGlobals.cellPointerDownPosition.y != y
    )
      GisMap.setlocation(cell);
    // GisMap.mapZoomEvent();
  }
  //  dragMouseLeaveEvent();
});

paper.on("blank:pointerup", function() {
  dragMouseLeaveEvent();
});

function dragMouseLeaveEvent() {
  // /console.log("Now leaving the mouse click button");
  delete dragStartPosition;
  isDraggable = 0;
}

$("#canvasId").mousemove(function(event) {
  if (isDraggable == 1 && dragStartPosition)
    paper.setOrigin(
      event.offsetX - dragStartPosition.x,
      event.offsetY - dragStartPosition.y
    );
});

/************************************************************************
 ************************************************************************/
/**
 * ************change Of Port then Node Properties Directions
 * Change***************************
 */

/*
 * var changeOfPortNodePropertiesDirectionsChange=function(src,dest,dir1,dir2) {
 * var
 * destId=dest.attributes.nodeProperties.nodeId,srcId=src.attributes.nodeProperties.nodeId;
 * var directions=src.get('nodeProperties').directions; for(var i in directions) {
 * if(i!=dir1 && directions[i]==destId)
 * src.attributes.nodeProperties.directions[i] = "0"; }
 *
 * console.log(directions);
 *
 *
 * directions=dest.get('nodeProperties').directions; for(var i in directions) {
 * if(i!=dir2 && directions[i]==srcId)
 * dest.attributes.nodeProperties.directions[i] = "0"; }
 * console.log(directions); }
 */

/************************************************************************
 ************************************************************************/
/** ************Cell add or remove events*************************** */

graph.on("add", function(cell) {
  // console.log('hello');
  if (cell instanceof joint.dia.Link) {
    GisMap.getMap().dragging.disable();
    nptGlobals.linkCount += 1;
    cell.attributes.linkProperties.linkId = linkUniqueIdGenerate(cell, true);
    console.log("linkId:" + cell.attributes.linkProperties.linkId);

    // Link added in BrownField or GreenField
    if (sessionStorage.getItem("NetworkState") == nptGlobals.BrownFieldStr)
      cell.get("linkProperties").BrownFieldLink = 1;

    // paperUtil.adjustVertices(graph,cell);

    // updateLinkProperty();
    // linkInfoTab();
  } else {
    console.log(cell.get("type"));
    if (
      cell.attributes.type == nptGlobals.NodeTypeROADM ||
      cell.attributes.type == nptGlobals.NodeTypeTwoDegreeRoadm ||
      cell.attributes.type == nptGlobals.NodeTypeHub ||
      cell.attributes.type == nptGlobals.NodeTypeILA ||
      cell.attributes.type == nptGlobals.NodeTypeTE ||
      cell.attributes.type == nptGlobals.NodeTypeCDROADM ||
      cell.attributes.type == nptGlobals.NodeTypePotp
    ) {
      console.log("Graph node add *****", nptGlobals.nodeIdForReplacingNode);
      if (nptGlobals.nodeIdForReplacingNode != 0) {
        // check if the node is
        // removed for replacing
        // with another type of
        // node
        cell.attributes.nodeProperties.nodeId =
          nptGlobals.nodeIdForReplacingNode;
        cell.attr(".label/text", cell.attributes.nodeProperties.nodeId);
        console.log("nodeId:" + cell.attributes.nodeProperties.nodeId);
      } else {
        nptGlobals.nodeCount += 1;
        cell.attributes.nodeProperties.nodeId = nodeUniqueIdGenerate(
          cell,
          true
        );
        cell.attributes.nodeProperties.stationName =
          "Station" + cell.attributes.nodeProperties.nodeId;
        cell.attributes.nodeProperties.siteName =
          "Site" + cell.attributes.nodeProperties.nodeId;
        // var label=cell.attr('.label/text')
        cell.attr(".label/text", cell.attributes.nodeProperties.nodeId);
        console.log("nodeId:" + cell.attributes.nodeProperties.nodeId);
      }

      // Link added in BrownField or GreenField
      if (sessionStorage.getItem("NetworkState") == nptGlobals.BrownFieldStr)
        cell.get("nodeProperties").BrownFieldNode = 1;
    }
    var siteStationNameStr =
      cell.attributes.nodeProperties.stationName +
      cell.attributes.nodeProperties.siteName;
    console.log("Graph add node to Map :" + siteStationNameStr);
    SiteAndStationNameMap.set(
      String(cell.attributes.nodeProperties.nodeId),
      siteStationNameStr
    );
    nptGlobals.nodeIdForReplacingNode = 0;
    nodeInfoTab();

    // Add/Initialize Context Menu on node
    addContextMenuToNode(cell);

    // let title = `Long Lat Info`;
    // let cellPropertyEditTemplate = ` <form>
    // 		<div class="radio">
    // 		<label><input type="radio" name="option" checked>Option 1</label>
    // 		</div>
    // 		<div class="form-group">
    // 		<label for="location">Location:</label>
    // 		<input type="text" class="form-control" id="location">
    // 		</div>
    // 		<hr>
    // 		<div class="radio">
    // 		<label><input type="radio" name="option">Option 2</label>
    // 		</div>
    // 		<div class="form-group">
    // 		<label for="latitude">Latitude:</label>
    // 		<input type="number" class="form-control" id="latitude">
    // 		</div>
    // 		<label for="longitude">Longitude:</label>
    // 		<input type="number" class="form-control" id="longitude">
    // 		</div>
    // 		</form> `;

    // bootbox
    //   .dialog({
    //     message: cellPropertyEditTemplate,
    //     title: title,
    //     animation: true,
    //     className: "cell-property-dialog",
    //     backdrop: false,
    //     buttons: {
    //       Save: {
    //         // label: "Success!",
    //         className: "btn btn--default",
    //         callback: function() {
    //           // console.log("Saving Cell Properties");
    //           let formData = $("#propertyFormLink").serializeArray();
    //           console.log("Link Property Form :", formData);
    //           setLinkProperties();
    //         }
    //       },
    //       Cancel: {
    //         className: "btn btn--default",
    //         callback: function() {
    //           cell.remove();
    //         }
    //       }
    //     }
    //   })
    //   .draggable();

    console.log("GisMap.getContextMenuCoords()", GisMap.getContextMenuCoords());

    GisMap.setElementLatLng(cell, GisMap.getContextMenuCoords());
    // cell.attributes.nodeProperties.nodeLat = GisMap.getContextMenuCoords().lat;
    // cell.attributes.nodeProperties.nodeLng = GisMap.getContextMenuCoords().lng;

    console.log("Cell Laty lng", GisMap.getElementLatLng(cell));

    GisMap.setlocation(cell);
    // GisMap.mapZoomEvent();
  }

  // updateLinkProperty();
  networkInfoTab();
});

graph.on("remove", function(cell) {
  // console.log('hello');
  if (nptGlobals.elSelected) {
    console.log("on delete set elSelected to undefined");
    nptGlobals.elSelected = undefined;
  }

  if (cell instanceof joint.dia.Link) {
    nptGlobals.linkCount -= 1;
    linkUniqueIdGenerate(cell, false); // false for remove

    // Update Node directions after link removal from that port
    fUpdateConnectionsOnDelete(cell);

    // Remove vertices, if only one link is left between the two nodes
    // connecting this link
    let siblings = paperUtil.fGetLinkSiblings(cell);
    console.log("siblings.length :", siblings.length);
    if (siblings.length == 1) siblings[0].unset("vertices");

    linkInfoTab();
  } else {
    if (
      cell.attributes.type == nptGlobals.NodeTypeROADM ||
      cell.attributes.type == nptGlobals.NodeTypeCDROADM ||
      cell.attributes.type == nptGlobals.NodeTypeTwoDegreeRoadm ||
      cell.attributes.type == "devs.hub" ||
      cell.attributes.type == nptGlobals.NodeTypeILA ||
      cell.attributes.type == nptGlobals.NodeTypeTE
    ) {
      if (nptGlobals.nodeIdForReplacingNode == 0) {
        // check if the node is
        // removed for replacing
        // with another type of
        // node
        nptGlobals.nodeCount -= 1;
        nodeUniqueIdGenerate(cell, false);
      }
      // nptGlobals.nodeIdForReplacingNode=0;
      var siteStationNameStr =
        cell.attributes.nodeProperties.stationName +
        cell.attributes.nodeProperties.siteName;
      console.log("Graph remove node from Map :" + siteStationNameStr);
      SiteAndStationNameMap.delete(
        String(cell.attributes.nodeProperties.nodeId)
      );
      nodeInfoTab();
    }
  }
  // updateLinkProperty();
  networkInfoTab();
});

//Holds the value of srcNode in case of target change and vice-versa
let unchangedNodeId = undefined,
  changedNodeId = undefined,
  changedNodeDir = undefined,
  linkDisconnectFlag = false;

graph.on("change:source", function(link, collection, opt) {
  console.log("graph.on(change:source)");

  unchangedNodeId = link.get("target").id;

  // var sourceId = link.get("source").id;
  // var targetId = link.get("target").id;
  // // console.log("Src Node:",graph.getCell(sourceId));
  // // console.log("Dest Node:",graph.getCell(targetId));

  // if (opt.ui && sourceId && targetId && opt.updateConnectionOnly) {
  //   console.log("Change Source");
  //   let srcNode = graph.getCell(sourceId);
  //   let destNode = graph.getCell(targetId);
  //   unchangedNodeId = destNode;

  //   // console.log("Src Node:", graph.getCell(sourceId));
  //   // console.log("Dest Node:", graph.getCell(targetId));
  //   // console.log("target port:", link.get('target').port);
  //   // console.log("source port:", link.get('source').port);
  //   // console.log("Link:", link);
  //   // console.log("collection:", collection);
  //   // console.log("opt:", opt);
  // }
});

graph.on("change:target", function(link, collection, opt) {
  console.log("graph.on(change:target)");

  unchangedNodeId = link.get("source").id;

  // let destNode = link.getTargetElement();
  // // console.log({ sourceId }, { targetId });
  // // console.log({ opt }, { collection });

  // if (linkChangeFlag) {
  //   changedNodeId = destNode;
  //   // changedNodeDir = link.get("target").dir;
  // }
  // // if (targetId) {
  // //   console.log("Change Target");
  // //   fUpdateConnectionsOnDelete(link);
  // //   // let srcNode = graph.getCell(sourceId);
  // //   // let destNode = graph.getCell(targetId);
  // //   unchangedNode = srcNode;
  // //   console.log({ srcNode });
  // //   console.log({ destNode });
  // // }
});

paper.on("link:disconnect", function(linkView, evt, elementViewDisconnected) {
  console.log("====== link:disconnect =======", { linkView }, { evt });
  changedNodeId = elementViewDisconnected.model.id;

  console.log({ changedNodeId });
  let linkDirections = [];
  _.each(graph.getLinks(), link => {
    if (link.source().id == changedNodeId) {
      console.log("link.source() :", link.source());
      linkDirections.push(link.source().dir);
    } else if (link.target().id == changedNodeId) {
      console.log("link.target() :", link.target());
      linkDirections.push(link.target().dir);
    }
  });

  console.log({ linkDirections });

  linkDirections.sort();
  let i = 1;
  while (i < linkDirections.length) {
    if (i != linkDirections[i - 1]) break;
    i++;
  }
  changedNodeDir = i;
  fUpdateConnectedLinksOnChange(graph.getCell(changedNodeId), changedNodeDir);

  linkDisconnectFlag = true;

  // fUpdateConnectedLinksOnChange(changedNodeId, changedNodeDir);

  // var sourceDefaultId = linkView.model.get("source").id;
  // var targetDefaultId = linkView.model.get("target").id;

  // let srcNode = graph.getCell(sourceDefaultId);
  // let srcNodeId = graph.getCell(sourceDefaultId).get("nodeProperties").nodeId;
  // let destNodeId = graph.getCell(targetDefaultId).get("nodeProperties").nodeId;

  // let changeNodeId = elementViewDisconnected.model.get("nodeProperties").nodeId;

  // // console.log("Link srcNodeId:",srcNodeId," Link DestNodeId",destNodeId,"
  // // changedNdoeid",changeNodeId);

  // console.log("Unchanged Nodeid:", unchangedNode.get("nodeProperties").nodeId);

  // // update unchanged node connections
  // UpdateNodeConnections(unchangedNode, changeNodeId);

  // // update node connections where link is disconnected
  // UpdateNodeConnections(
  //   elementViewDisconnected.model,
  //   unchangedNode.get("nodeProperties").nodeId
  // );
  // // UpdateNodeConnections(elementViewDisconnected.model,srcNodeId);

  // console.log(
  //   "elementViewDisconnected:",
  //   elementViewDisconnected.model.get("id")
  // );
});

function UpdateNodeConnections(node, changedNodeId) {
  console.log(
    "UpdateNodeConnections: Node",
    node.get("nodeProperties").nodeId,
    " Id:",
    changedNodeId
  );
  let values = _.values(node.get("nodeProperties").nodeConnections);
  let index = _.indexOf(values, changedNodeId);
  // console.log("Values Array :",values," index:",index);
  // If index found then shift all elements to left
  if (index != -1) {
    // // console.log("Index found at :",index);
    // while(index<values.length-1 && values[index]!=0 ){
    // values[index]=values[index+1];
    // index++;
    // }
    // values[values.length-1]=0;

    // // console.log("After Index Values Array :",values);
    // let newNodeConnectionObj={};
    // values.map(function(val,i){
    // let key=Number(i)+1;
    // // console.log("index:",i," val:",val," key:",key);

    // newNodeConnectionObj[key]=val;
    // });

    let newNodeConnectionObj = fUpdateNodeConnectionsUtil(values, index);
    // console.log("newNodeConnectionObj :",newNodeConnectionObj);
    node.get("nodeProperties").nodeConnections = newNodeConnectionObj;
  }

  // let index =
  // Object.values(node.attributes.nodeProperties.nodeConnections).indexOf(changedNodeId);
  // console.log("Index of changed node:",changedNodeId);
  // if (index != -1) {
  // for (let key in node.attributes.nodeProperties.nodeConnections) {
  // if ((key-1)>index && key!=8)
  // {
  // node.attributes.nodeProperties.nodeConnections[key]=node.attributes.nodeProperties.nodeConnections[key+1];
  // }
  // }
  // }
}

function fUpdateNodeConnectionsUtil(values, index) {
  console.log("%%%%%%%%%%%%% Values Array :", values, " index:", index);
  while (index < values.length - 1 && values[index] != 0) {
    values[index] = values[index + 1];
    index++;
  }
  values[values.length - 1] = 0;

  // console.log("After Index Values Array :",values);
  let newNodeConnectionObj = {};
  values.map(function(val, i) {
    let key = Number(i) + 1;
    // console.log("index:",i," val:",val," key:",key);

    newNodeConnectionObj[key] = val;
  });

  console.log("newNodeConnectionObj :", newNodeConnectionObj);
  return newNodeConnectionObj;
}

//graph.on('change', function(element, collection) {

//if(!element.isLink()){
//console.log("--------------------change------------------------------");
//console.log("element:",element);
//console.log("collection:",collection);
//}

//});

/*******************************************************************************

 * JS for showing diagram object notification
 ******************************************************************************/
/** ************Download map on click*************************** */

$("#saveMapId").click(function() {
  // console.log('Im herer');
  // $('#json').empty();
  var jsonStr = JSON.stringify(graph);

  // //download file
  // offerDownload('map', jsonStr);
  // //if(js[0].position.x>10)
  // //console.log("wow");
  // graph.clear();
  // console.log('executing clear....');
  // graph.clear();
  // graph.fromJSON(JSON.parse(jsonStr));
  // setallNodeAttributes();
  // console.log(graph.getElements());
  //
  // //for showing formatted json code
  //
  // regeStr = '', // A EMPTY STRING TO EVENTUALLY HOLD THE FORMATTED
  // STRINGIFIED OBJECT
  // f = {
  // brace: 0
  // }; // AN OBJECT FOR TRACKING INCREMENTS/DECREMENTS,
  // // IN PARTICULAR CURLY BRACES (OTHER PROPERTIES COULD BE ADDED)
  //
  // regeStr = jsonStr.replace(/({|}[,]*|[^{}:]+:[^{}:,]*[,{]*)/g, function
  // (m, p1) {
  // var rtnFn = function() {
  // return '<div style="text-indent: ' + (f['brace'] * 20) + 'px;">' + p1 +
  // '</div>';
  // },
  // rtnStr = 0;
  // if (p1.lastIndexOf('{') === (p1.length - 1)) {
  // rtnStr = rtnFn();
  // f['brace'] += 1;
  // } else if (p1.indexOf('}') === 0) {
  // f['brace'] -= 1;
  // rtnStr = rtnFn();
  // } else {
  // rtnStr = rtnFn();
  // }
  // return rtnStr;
  // });
  // $('#json').append(regeStr);
});

//get cell cid by using its id

/*******************************************************************************

 * Clear Paper button
 ******************************************************************************/

$("#clearpaper").click(function() {
  graph.clear();
  nptGlobals.isGneFlag = 0;
  nptGlobals.isFaultAnalysisReady = false;
});

/*******************************************************************************
 * Spread map nodes .. transition from old to new Gis Map
 ******************************************************************************/

$("#gis-map-setup").click(function() {
  let AllNodes = graph.getElements();
  _.each(AllNodes, function(node, index) {
    if (index >= GisMap.getLatLngList().length) {
      node.get("nodeProperties").nodeLat = GisMap.getLatLngList()[0].latitude;
      node.get("nodeProperties").nodeLng = GisMap.getLatLngList()[0].longitude;
    } else {
      node.get("nodeProperties").nodeLat = GisMap.getLatLngList()[
        index
      ].latitude;
      node.get("nodeProperties").nodeLng = GisMap.getLatLngList()[
        index
      ].longitude;
    }
    GisMap.setlocation(node);
  });

  GisMap.mapZoomEvent();
});

/*******************************************************************************

 * Print Paper button
 ******************************************************************************/

$("#print-paper").click(function() {
  let paperElements = [];
  _.each(graph.getElements(), function(el) {
    paperElements.push(el);
  });

  console.log("paperElements", paperElements);

  paper.print({
    padding: 10,
    sheet: { width: 297, height: 210 },
    poster: false,
    margin: 1,
    marginUnit: "in",
    ready: function(paperElements, readyToPrint) {
      readyToPrint(paperElements);
    }
  });
});

/*******************************************************************************

 * Events to Handle and Placing on Canvas : For General Shapes
 ******************************************************************************/

//stencilPaperGS.on('cell:', function(cellView, e, x, y) {

//$('body').append('<div id="flyPaper"
//style="position:fixed;z-index:100;opacity:.7;pointer-event:none;"></div>');

//var flyGraph = new joint.dia.Graph,
//flyPaper = new joint.dia.Paper({
//el: $('#flyPaper'),
//model: flyGraph,
//interactive: false
//}),
//flyShape = cellView.model.clone(),
//pos = cellView.model.position(),
//offset = {
//x: x - pos.x,
//y: y - pos.y
//};
//flyShape.position(0, 0);
//flyGraph.addCell(flyShape);
//$("#flyPaper").offset({
//left: e.pageX - offset.x,
//top: e.pageY - offset.y
//});
//$('body').on('mouseup.fly', function(e) {

///*
//* x and y : Page x and Y Coordinates
//* target : canvas [el width and top and height]
//*/
//var x = e.pageX,
//y = e.pageY,
//target = paper.$el.offset();

//// Dropped over paper ?
//if (x > target.left && x < target.left + paper.$el.width() && y > target.top
//&& y < target.top + paper.$el.height()) {
//var s = flyShape.clone();
//s.position(x - target.left - offset.x, y - target.top - offset.y);
//graph.addCell(s);
//}
//$('body').off('mousemove.fly').off('mouseup.fly');
//flyShape.remove();
//$('#flyPaper').remove();

//});
//}

//);

/*******************************************************************************
 * Events to adjust height of sidebars
 ******************************************************************************/
$(window).resize(function() {
  adjustSideBarHeightOnLOad();
});

/*******************************************************************************
 * Events to set flag for Canvas Active or Not
 ******************************************************************************/
$("#canvasViewLiId").on("click", function() {
  console.log();
  nptGlobals.setCanvasActive(_SET_VALUE);
});

/*
 * paper.$el.on('mousewheel DOMMouseScroll', function onMouseWheel(e) {
 * //function onMouseWheel(e){ e.preventDefault(); e = e.originalEvent;
 *
 * var delta = Math.max(-1, Math.min(1, (e.wheelDelta || -e.detail))) / 50; var
 * offsetX = (e.offsetX || e.clientX - $(this).offset().left);
 *
 * var offsetY = (e.offsetY || e.clientY - $(this).offset().top); var p =
 * offsetToLocalPoint(offsetX, offsetY); var newScale =
 * V(paper.viewport).scale().sx + delta; console.log(' delta' + delta + ' ' +
 * 'offsetX' + offsetX + 'offsety--' + offsetY + 'p' + p.x + 'newScale' +
 * newScale) if (newScale > 0.4 && newScale < 2) { paper.setOrigin(0, 0);
 * paper.scale(newScale, newScale, p.x, p.y); } });
 *
 * function offsetToLocalPoint(x, y) { var svgPoint =
 * paper.svg.createSVGPoint(); svgPoint.x = x; svgPoint.y = y;
 *
 * var pointTransformed =
 * svgPoint.matrixTransform(paper.viewport.getCTM().inverse()); return
 * pointTransformed; }
 */

/*******************************************************************************
 * Events to Handle and Placing on Canvas : For Network Elements
 ******************************************************************************/
stencilPaper.on("cell:pointerdown", function(cellView, e, x, y) {
  console.log(
    "type :",
    cellView.model.get("type"),
    nptGlobals,
    " nptGlobals.getCanvasActive()",
    nptGlobals.getCanvasActive()
  );
  let nodeType = cellView.model.get("type");
  if (
    nptGlobals.getCanvasActive() &&
    (nptGlobals.NetworkTopology == nptGlobals.TopologyMeshStr) |
      (nptGlobals.NetworkTopology == nptGlobals.TopologyTwoFiberRingStr) |
      (nptGlobals.NetworkTopology == nptGlobals.TopologyHubRingStr) &&
    nodeType != "devs.Model"
  ) {
    // &&
    // cellView.model.get('type')!=nptGlobals.NodeTypeHub)
    $("body").append(
      '<div id="flyPaper" style="position:fixed;z-index:407;opacity:.7;pointer-event:none;"></div>'
    );

    var flyGraph = new joint.dia.Graph(),
      flyPaper = new joint.dia.Paper({
        el: $("#flyPaper"),
        width: 100,
        height: 100,
        model: flyGraph,
        interactive: false
      }),
      flyShape = cellView.model.clone(),
      pos = cellView.model.position(),
      offset = {
        x: x - pos.x,
        y: y - pos.y + 100
      };

    flyShape.resize(40, 40);
    flyPaper.scaleContentToFit();

    // console.log("Paper Offset::",paper.pageOffset());
    // console.log("pos.x" + pos.x + " pos.y:" + pos.y);
    // console.log("x" + x + " y:" + y);

    flyShape.position(0, 0);
    flyGraph.addCell(flyShape);
    $("#flyPaper").offset({
      left: e.pageX - offset.x,
      top: e.pageY - offset.y
    });
    $(document).ready(function() {
      $("body").on("mousemove.fly", function(e) {
        // alert('mouse fly');
        $("#flyPaper").offset({
          left: e.pageX - offset.x,
          top: e.pageY - offset.y
        });
      });
    });
    $("body").on("mouseup.fly", function(e) {
      /*
       * x and y : Page x and Y Coordinates target : canvas [el width and
       * top and height]
       *
       */
      var pageX = e.pageX,
        pageY = e.pageY,
        target = paper.$el.offset();

      // Dropped over paper
      if (
        pageX > target.left &&
        pageX < target.left + paper.$el.width() &&
        pageY > target.top &&
        pageY < target.top + paper.$el.height()
      ) {
        // var s = flyShape.clone();

        // let Scale = paper.scale();
        // var xEl=x-target.left;
        // var yEl=y - target.top;

        // s.position(xEl,yEl);

        // console.log("s.position",s.position);
        // graph.addCell(s);

        let canvasIdOffset = $("#canvasId").offset();
        let paperScale = paper.scale();

        let pos = { x: "", y: "" };
        pos.x = pageX - canvasIdOffset.left;
        pos.y = pageY - canvasIdOffset.top;
        console.log(
          "Node to be added TYpe:",
          nodeType,
          " insert at pos:",
          pos,
          " - canvasIdOffset:",
          canvasIdOffset,
          " paperScale",
          paperScale
        );

        /** DBG => GIS */
        // //position in Div
        // let pointXY = L.point(pos.x, pos.y);
        // // convert to lat/lng space
        // point = map.layerPointToLatLng(pointXY);
        // //add marker on map
        // let marker=L.marker([point.lat,point.lng],{ opacity:
        // 0.0},{myCustomId:(nptGlobals.nodeCount+1)}).addTo(map);
        // DistanceFromCdot(point.lat,point.lng);

        switch (nodeType) {
          case nptGlobals.NodeTypeCDROADM:
            insertCdRoadmNode(pos);
            break;
          case nptGlobals.NodeTypeHub:
            insertHubNode(pos);
            break;
          case nptGlobals.NodeTypeILA:
            insertIlaNode(pos);
            break;
          case nptGlobals.NodeTypeTE:
            insertTeNode(pos);
            break;
          case nptGlobals.NodeTypeROADM:
            insertRoadmNode(pos);
            break;
          case nptGlobals.NodeTypeTwoDegreeRoadm:
            insertTwoDegreeRoadmNode(pos);
            break;
          case nptGlobals.NodeTypePotp:
            insertPotpNode(pos);
            break;

          default:
            break;
        }

        // var node=graph.getCell(s.get('id'));
        // console.log("Node type added on canvas
        // :",node.get('type')==nptGlobals.NodeTypeCDROADM,"
        // ::",nptGlobals.NodeTypeCDROADM);

        // if(node.get('type')==nptGlobals.NodeTypeROADM ||
        // node.get('type')==nptGlobals.NodeTypeCDROADM)
        // {
        // window.cellLocal=paper.findViewByModel(node);
        // console.log("Cell Local ::",window.cellLocal);
        // addPortToDirection("all");
        // }
        // else if(node.get('type')==nptGlobals.NodeTypeTwoDegreeRoadm
        // || node.get('type')==nptGlobals.NodeTypeHub
        // || node.get('type')==nptGlobals.NodeTypeILA)
        // {
        // window.cellLocal=paper.findViewByModel(node);
        // addPortToDirection("east");
        // addPortToDirection("west");
        // }
        // else if(node.get('type')==nptGlobals.NodeTypeTE ) {
        // window.cellLocal=paper.findViewByModel(node);
        // addPortToDirection("east");
        // removePortToDirection("east");
        // }
        // console.log("node.get('type')",node.get('type'));
      }
      $("body")
        .off("mousemove.fly")
        .off("mouseup.fly");
      flyShape.remove();
      $("#flyPaper").remove();
      // alert('done');
    });
  } else {
    console.log("Current Topology :" + nptGlobals.NetworkTopology);

    if (!nptGlobals.getCanvasActive()) {
      console.log(
        "nptGlobals.getCanvasActive():",
        nptGlobals.getCanvasActive()
      );
      bootBoxWarningAlert("Canvas tab is not active.", 2000);
    } else if (
      nptGlobals.NetworkTopology == nptGlobals.TopologyLinearStr ||
      nptGlobals.NetworkTopology == nptGlobals.TopologyTwoFiberRingStr ||
      nptGlobals.NetworkTopology == nptGlobals.TopologyHubRingStr
    ) {
      console.log("Drag and Drop disabled for Ring and Linear");
      bootBoxWarningAlert(
        "Drag and Drop is disabled for TwoFibreRing and Linear topology. No more nodes/links can be added.",
        2000
      );
    }
    /*
     * else if(cellView.model.get('type')!=nptGlobals.NodeTypeHub) {
     * console.log("Drag and Drop disabled for Hub Node");
     * bootBoxWarningAlert('Hub node can only be added by using Hub Ring
     * topology through create map menu.',2000); }
     */
  }
});
