/************************************************************************
 * Utility functions
 ************************************************************************/

/************************************************************************
 * //Create a node with `id` at the position `p`.
 ************************************************************************/

function insertIlaNode(/*id,*/ p) {
  // var Ilanode = (new joint.shapes.devs.Model({
  // 	//    	id:id,
  // 	markup: '<g class="rotatable"><g class="scalable"><path class="outer"/><text class="label"/><text class="props"/><g class="inPorts"/><g class="outPorts"/></g></g>',
  // 	type: nptGlobals.NodeTypeILA,
  // 	ports: portsVar,
  // 	position: {
  // 		x: p.x,
  // 		y: p.y
  // 	},
  // 	size: {
  // 		width: 40,
  // 		height: 40
  // 	},
  // 	attrs: {
  // 		path: { d: IlaNodePath, stroke: "#4f58a2", fill: "#f2f20099", 'stroke-width': 2 },
  // 		'.label': {
  // 			text: nptGlobals.NodeTypeILADisplayName,
  // 			fill: '#777',
  // 			'stroke-width': 1,
  // 			stroke: '#777',
  // 			'ref-y': 19,
  // 			'ref-x': 16
  // 		},
  // 		'.v-line': { 'font-size': '15px', 'stroke-width': '2' },
  // 		'.props': {
  // 			text: '',
  // 			fill: 'white',
  // 			/*'stroke-width':'',*/
  // 			stroke: '#763a3a',
  // 			'ref-y': -40,
  // 			'ref-x': 10,
  // 			dy: '2em'
  // 			/*'ref':'.outer',*/
  // 			/*'font-size':'15px'*/
  // 		},
  // 		'.props .v-line': {
  // 			'stroke-width': '2',
  // 			'font-size': '20px'
  // 		}

  // 	},
  // 	nodeProperties: {
  // 		nodeId: '1',
  // 		displayName: nptGlobals.NodeTypeILADisplayName,
  // 		stationName: 'new delhi',
  // 		siteName: 'chattarpur',
  // 		gneFlag: 0,
  // 		vlanTag: 1,
  // 		degree: 0,
  // 		directions: {
  // 			east: '0',
  // 			west: '0',
  // 			north: '0',
  // 			south: '0',
  // 			ne: '0',
  // 			nw: '0',
  // 			se: '0',
  // 			sw: '0'
  // 		},
  // 		numDirections: {
  // 			east: '0',
  // 			west: '0',
  // 			north: '0',
  // 			south: '0',
  // 			ne: '0',
  // 			nw: '0',
  // 			se: '0',
  // 			sw: '0'
  // 		},
  // 		faultNode: 0,
  // 		emsip: '0.0.0.0',
  // 		subnet: '0.0.0.0',
  // 		gateway: '0.0.0.0',
  // 		ipv6: 'fe80::2e0:4cff:fe36:90a/64',
  // 		capacity: '3',
  // 		direction: 'West',
  // 		opticalReach: 'Long Haul',
  // 		BrownFieldNode: '0' //1-Yes , 0-No
  // 	},
  // 	interactive: false
  // 	//ports: portsVar
  // })).addTo(graph);

  var Ilanode = new joint.shapes.devs.Ila({
    //    	id:id,

    position: {
      x: p.x,
      y: p.y
    },
    size: nodeDefaultSize

    // ports: portsVar
  }).addTo(graph);
  Ilanode.attr(".label/text", Ilanode.get("nodeProperties").nodeId);
  Ilanode.attr(".app-cell__title/text", "ILA");
  //console.log(node);
  // window.cellLocal = paper.findViewByModel(Ilanode);
  // addPortToDirection("east");
  // addPortToDirection("west");
  return Ilanode;
}

/************************************************************************
 * //Create a CDC node with `id` at the position `p`.
 ************************************************************************/

function insertRoadmNode(p) {
  var roadmNode = new joint.shapes.devs.roadm({
    position: {
      x: p.x,
      y: p.y
    },
    size: nodeDefaultSize,

    ports: portsVar
  }).addTo(graph);
  roadmNode.attr(".label/text", roadmNode.get("nodeProperties").nodeId);
  roadmNode.attr(".app-cell__title/text", "CDC");
  //console.log(node);
  return roadmNode;
}
/************************************************************************
 * //Create a CD Roadm node with `id` at the position `p`.
 ************************************************************************/

function insertCdRoadmNode(/*id,*/ p) {
  var cdroadmNode = new joint.shapes.devs.cdroadm({
    //    	id:id,

    position: {
      x: p.x,
      y: p.y
    },
    size: nodeDefaultSize,

    ports: portsVar
  }).addTo(graph);
  cdroadmNode.attr(
    ".label/text",
    /*"Ila"+*/ cdroadmNode.get("nodeProperties").nodeId
  );
  cdroadmNode.attr(".app-cell__title/text", "CD-R");
  //console.log(node);
  return cdroadmNode;
}

/************************************************************************
 * //Create a CD Roadm node with `id` at the position `p`.
 ************************************************************************/

function insertPotpNode(/*id,*/ p) {
  var potpNode = new joint.shapes.devs.potp({
    //    	id:id,

    position: {
      x: p.x,
      y: p.y
    },
    size: nodeDefaultSize,

    ports: portsVar
  }).addTo(graph);
  potpNode.attr(
    ".label/text",
    /*"Ila"+*/ potpNode.get("nodeProperties").nodeId
  );
  potpNode.attr(".app-cell__title/text", "POTP");
  //console.log(node);
  return potpNode;
}

/************************************************************************
 * //Create a TE node with `id` at the position `p`.
 ************************************************************************/

function insertTeNode(/*id,*/ p) {
  var TeNode = new joint.shapes.devs.TE({
    //    	id:id,
    position: {
      x: p.x,
      y: p.y
    },
    size: nodeDefaultSize,

    ports: portsVar
    //ports: portsVar
  }).addTo(graph);
  TeNode.attr(".label/text", /*"Ila"+*/ TeNode.get("nodeProperties").nodeId);
  TeNode.attr(".app-cell__title/text", "TE");
  //console.log(node);
  return TeNode;
}

/************************************************************************
 * //Create a Two Degree Roadm node with `id` at the position `p`.
 ************************************************************************/

function insertTwoDegreeRoadmNode(/*id,*/ p) {
  var TwoDegreeNode = new joint.shapes.devs.BSRoadm({
    //    	id:id,
    position: {
      x: p.x,
      y: p.y
    },
    size: nodeDefaultSize,

    ports: portsVar
    //ports: portsVar
  }).addTo(graph);
  TwoDegreeNode.attr(
    ".label/text",
    /*"Ila"+*/ TwoDegreeNode.get("nodeProperties").nodeId
  );
  TwoDegreeNode.attr(".app-cell__title/text", "B&S");
  //console.log(node);
  return TwoDegreeNode;
}

/************************************************************************
 * //Create a Two Degree Roadm node with `id` at the position `p`.
 ************************************************************************/

function insertHubNode(/*id,*/ p) {
  var HubNode = new joint.shapes.devs.hub({
    //    	id:id,
    position: {
      x: p.x,
      y: p.y
    },
    size: nodeDefaultSize,

    ports: portsVar
    //ports: portsVar
  }).addTo(graph);
  HubNode.attr(".label/text", /*"Ila"+*/ HubNode.get("nodeProperties").nodeId);
  HubNode.attr(".app-cell__title/text", "HUB");
  //console.log(node);
  return HubNode;
}

/************************************************************************
 * //Create a test node with `id` at the position `p`.
 ************************************************************************/

function insertTestNode(/*id,*/ p) {
  var TestNode = new joint.shapes.devs.TestNode({
    //    	id:id,
    position: {
      x: p.x,
      y: p.y
    },
    size: nodeDefaultSize
    //ports: portsVar
  }).addTo(graph);
  TestNode.attr(
    ".label/text",
    /*"Ila"+*/ TestNode.get("nodeProperties").nodeId
  );
  TestNode.attr(".app-cell__title/text", "TEST");
  //console.log(node);
  return TestNode;
}

/************************************************************************
 * Create a link between a source element with id `s` and target element with id `t`.
 ************************************************************************/

function insertLink(s, t) {
  console.log("Insert Link :: Link b/w Node " + s + " and Node " + t);
  var link = new joint.dia.Link({
    id: [s, t].sort().join(),
    source: {
      id: s
    },
    target: {
      id: t
    },
    z: -1,
    attrs:
      attrs.linkDefault /* attrs.IlalinkDefault{
			'.connection':{stroke:'#777','stroke-width':3,'stroke-opacity':0.5},
			'.connection-wrap':{stroke:'#E74C3C','stroke-width':10,'stroke-opacity':0.2},
			'.marker-source': { stroke: 'rbga(0,0,0,0)', fill: '#777', d: 'M 10 0 L 0 5 L 10 10 z','fill-opacity':0.5 },
			'.marker-target': { stroke: 'rbga(0,0,0,0)', fill: '#777', d: 'M 10 0 L 0 5 L 10 10 z','fill-opacity':0.5 },
			'.marker-arrowhead':{stroke: 'rbga(0,0,0,0)', fill: '#777', d: 'M 10 0 L 0 5 L 10 10 z','fill-opacity':0.5}
		}*/,
    linkProperties: {
      linkId: "1",
      spanLength: "80",
      fiberType: 1 /**Default Fiber Type is 1 : G.652.D*/,
      cdCoefficient: 19.6,
      pmdCoefficient: 0.2,
      cd: 1568,
      pmd: 1.79,
      splice: 1,
      lossPerSplice: 1,
      connector: 1,
      lossPerConnector: 1,
      coefficient: 0.275,
      calculatedSpanLoss: 24,
      adjustableSpanLoss: 24,
      costMetric: "3",
      opticalParameter: "3",
      srlg: "0",
      color: "4",
      scope: "P",
      isIlaLink: "1",
      faultLink: "0", //1-true 0-false
      SrcNodeDirection: "",
      DestNodeDirection: "",
      lineProtection: "0", //1-Yes , 0-No
      BrownFieldLink: "0", //1-Yes , 0-No
      linkType: "DEFAULT (PA/BA)", // 'Default'/'Raman Hybrid'/'Raman DRA'
      autoDelete: false
    }
  }).addTo(graph);

  //link.set('router',{name:'metro'});
  // link.set('router', { name: 'manhattan' });
  //link.set('connector', { name: 'normal' });
  // link.set('connector', { name: 'rounded' });
  return link;
}

/************************************************************************
 * Create a OmsProtected link between a source element with id `s` and target element with id `t`.
 ************************************************************************/

function insertOmsProtectedLink(s, t) {
  var link = new joint.dia.Link({
    id: [s, t].sort().join(),
    source: {
      id: s
    },
    target: {
      id: t
    },
    z: -1,
    attrs:
      attrs.linkDefaultLinePtc /* attrs.IlalinkDefault{
			'.connection':{stroke:'#777','stroke-width':3,'stroke-opacity':0.5},
			'.connection-wrap':{stroke:'#E74C3C','stroke-width':10,'stroke-opacity':0.2},
			'.marker-source': { stroke: 'rbga(0,0,0,0)', fill: '#777', d: 'M 10 0 L 0 5 L 10 10 z','fill-opacity':0.5 },
			'.marker-target': { stroke: 'rbga(0,0,0,0)', fill: '#777', d: 'M 10 0 L 0 5 L 10 10 z','fill-opacity':0.5 },
			'.marker-arrowhead':{stroke: 'rbga(0,0,0,0)', fill: '#777', d: 'M 10 0 L 0 5 L 10 10 z','fill-opacity':0.5}
		}*/,
    linkProperties: {
      linkId: "1",
      spanLength: "80",
      fiberType: 1 /**Default Fiber Type is 1 : G.652.D*/,
      cdCoefficient: 19.6,
      pmdCoefficient: 0.2,
      cd: 1568,
      pmd: 1.79,
      splice: 1,
      lossPerSplice: 1,
      connector: 1,
      lossPerConnector: 1,
      coefficient: 0.275,
      calculatedSpanLoss: 24,
      adjustableSpanLoss: 24,
      costMetric: "3",
      opticalParameter: "3",
      srlg: "0",
      color: "4",
      scope: "P",
      isIlaLink: "1",
      faultLink: "0", //1-true 0-false
      SrcNodeDirection: "",
      DestNodeDirection: "",
      lineProtection: "1", //1-Yes , 0-No
      BrownFieldLink: "0", //1-Yes , 0-No
      linkType: "DEFAULT (PA/BA)", // 'Default'/'Raman Hybrid'/'Raman DRA'
      autoDelete: false
    }
  }).addTo(graph);

  // link.set('router', { name: 'metro' });
  // link.set('connector', { name: 'rounded' });
  return link;
}

/************************************************************************
 * Hides/Removes the path used to show the shortest path
 ************************************************************************/
var pathLinks = [],
  highlightedPathNodes = [];
function hidePath() {
  // console.log("Removing links from path links Array........");
  // $('#path').text('');
  var colorOfLink;
  _.each(pathLinks, function(link) {
    //link.remove();
    var isLineProtected = link.get("linkProperties").lineProtection;
    if (isLineProtected == 1) link.attr(attrs.linkDefaultLinePtc);
    else link.attr(attrs.linkDefault);
    colorOfLink = link.get("linkProperties").color;
    //console.log("colorOfLink :: "+colorOfLink+"  Color ::"+getColorFromCode(colorOfLink))
    link.attr(".connection/stroke", getColorFromCode(colorOfLink));
    //link.set('labels', []);
    //console.log(link);
  });
}

function fHideOpticalPowerPathNodes() {
  var DefaultId;
  for (var i = 0; i < nptGlobals.opticalPowerPathNodesArr.length; i++) {
    DefaultId = getNodeById(nptGlobals.opticalPowerPathNodesArr[i]);
    var cell = graph.getCell(DefaultId);
    cell.attr(".props/text", " ");
  }
  nptGlobals.opticalPowerPathNodesArr = [];
}

var getColorFromCode = function(colorOfLink) {
  var color;
  console.log("colorOfLink :: " + colorOfLink);

  switch (colorOfLink) {
    case "1":
      color = "Violet";
      break;
    case "2":
      color = "Indigo";
      break;
    case "3":
      color = "Blue";
      break;
    case "4":
      color = "Green";
      break;
    case "5":
      color = "Yellow";
      break;
    case "6":
      color = "Orange";
      break;
    case "7":
      color = "Red";
      break;
  }
  return color;
};

/************************************************************************
 * Hides/Removes the highlighted Nodes used to show the path
 ************************************************************************/
function removeNodeHighlight() {
  // console.log("Removing links from path links Array........");
  // $('#path').text('');
  _.each(highlightedPathNodes, function(node) {
    //link.remove();
    if (node.get("nodeProperties").faultNode == 1)
      node.attr(attrs.elementFault);
    else if (node.get("type") == nptGlobals.NodeTypeTE)
      node.attr(attrs.TeDefault);
    else if (node.get("type") == nptGlobals.NodeTypeILA)
      node.attr(attrs.IlaDefault);
    else node.attr(attrs.elementDefault);
    //node.attr('.body/stroke', '#333');
    // node.attr('rect/stroke-width', '2');
    // node.attr('.label/stroke', '#333');

    // highlightedPathNodes.shift();
    //link.set('labels', []);
    //console.log(link);
  });

  highlightedPathNodes.pop();
  highlightedPathNodes.pop();
}

/************************************************************************
 * Show path from source node to dest node(Shortest Path)
 ************************************************************************/
function showPath(path, ColorofLink) {
  console.log("Adding links to path links Array and showing paths........");
  //hidePath();
  //$('#path').text(path.join(' -> '));

  for (var i = 0; i < path.length - 1; i++) {
    /*var curr = path[i];		
        var next = path[i + 1];*/
    var curr = getNodeById(path[i]);
    var next = getNodeById(path[i + 1]);
    // console.log("Value of I :"+ i + "Value of first Path[i] :"+curr+"Value of Second Path[i+1] :"+next);
    //  console.log("CurrId + NextId" + currId + nextId);
    // var curr = graph.getCell(currId);
    // var next =  graph.getCell(nextId);
    //  console.log("CurrObj"+curr+"NextObj"+next);
    if (next) {
      //var link = l(curr, next);
      var link = getLinkBetweenSrcandDestNodes(curr, next);
      // console.log(link);
      var isLineProtected = link.get("linkProperties").lineProtection;

      //var link = graph.getCell([curr, next].sort().join());
      //console.log("Link Btwn "+curr+" and "+next+" ---  "+link);

      //  console.log("Color of Link:"+ColorofLink);

      switch (ColorofLink) {
        case "Color1":
          {
            if (isLineProtected == 1)
              link.attr(attrs.LineProtectionHighlightedLink);
            else link.attr(attrs.HighlightedLinkFaultAnalysis);

            if (nptGlobals.reversedLink) {
              pathLinks.push(link.attr(attrs.ReversedlinkHighlightedColor1));
              nptGlobals.reversedLink = false;
            } else {
              pathLinks.push(link.attr(attrs.linkHighlightedColor1));
            }
          }
          break;
        case "Color2":
          {
            if (isLineProtected == 1)
              link.attr(attrs.LineProtectionHighlightedLink);
            else link.attr(attrs.HighlightedLinkFaultAnalysis);
            if (nptGlobals.reversedLink) {
              pathLinks.push(link.attr(attrs.ReversedlinkHighlightedColor2));
              nptGlobals.reversedLink = false;
            } else pathLinks.push(link.attr(attrs.linkHighlightedColor2));
          }
          break;
        case "Color3":
          {
            if (isLineProtected == 1)
              link.attr(attrs.LineProtectionHighlightedLink);
            if (nptGlobals.reversedLink) {
              pathLinks.push(link.attr(attrs.ReversedlinkHighlightedColor3));
              nptGlobals.reversedLink = false;
            } else pathLinks.push(link.attr(attrs.linkHighlightedColor3));
          }
          break;
        case "Color4":
          {
            if (isLineProtected == 1)
              link.attr(attrs.LineProtectionHighlightedLink);
            else link.attr(attrs.HighlightedLinkFaultAnalysis);

            if (nptGlobals.reversedLink) {
              pathLinks.push(link.attr(attrs.ReversedlinkHighlightedColor4));
              nptGlobals.reversedLink = false;
            } else pathLinks.push(link.attr(attrs.linkHighlightedColor4));
          }
          break;
        case "Color5":
          {
            if (isLineProtected == 1)
              link.attr(attrs.LineProtectionHighlightedLink);
            else link.attr(attrs.HighlightedLinkFaultAnalysis);

            if (nptGlobals.reversedLink) {
              pathLinks.push(link.attr(attrs.ReversedlinkHighlightedColor5));
              nptGlobals.reversedLink = false;
            } else pathLinks.push(link.attr(attrs.linkHighlightedColor5));
          }
          break;
        case "Color6":
          {
            if (isLineProtected == 1)
              link.attr(attrs.LineProtectionHighlightedLink);
            else link.attr(attrs.HighlightedLinkFaultAnalysis);

            if (nptGlobals.reversedLink) {
              pathLinks.push(link.attr(attrs.ReversedlinkHighlightedColor6));
              nptGlobals.reversedLink = false;
            } else pathLinks.push(link.attr(attrs.linkHighlightedColor6));
          }
          break;
      }
      // pathLinks.push(link.attr(attrs.linkHighlightedMagenta));
      // pathLinks.push(link);
    }
  }
}

function getLinkBetweenSrcandDestNodes(srcNode, destNode) {
  var link = null;
  _.each(graph.getLinks(), function(el) {
    if (
      el.attributes.source.id == srcNode &&
      el.attributes.target.id == destNode
    ) {
      //console.log("Match with SrcNode and DestNode");
      //console.log("Element set to link"+el);
      link = el;
    } else if (
      el.attributes.source.id == destNode &&
      el.attributes.target.id == srcNode
    ) {
      link = el;
      nptGlobals.reversedLink = true;
    }
  });
  return link;
}

/************************************************************************
 * Node Id Generator
 ************************************************************************/
function nodeUniqueIdGenerate(model, addNode) {
  //addNode {true (addition),false(removal)}
  //console.log(that.value+model);
  if (addNode) {
    if (nptGlobals.nodeAvailableArr.length > 0) {
      // Return nodeId from nextAvailable array if its not empty
      nptGlobals.nextAvailableId = nptGlobals.nodeAvailableArr.shift();
      return nptGlobals.nextAvailableId;
    }
    return nptGlobals.nextNodeId++; // Else return the next highest value of nodeId (Equal to element count)
  } else {
    // Add deleted nodeId to the nextAvailable array
    var nodeId = model.attributes.nodeProperties.nodeId;
    //Add to delete Node set for Live Analysis/Simulation
    //deletedNodeSet.add(nodeId);
    console.log("nodeId:" + nodeId);
    sortedPush(nptGlobals.nodeAvailableArr, nodeId);
  }
}

/************************************************************************
 * Link Id generator
 ************************************************************************/
function linkUniqueIdGenerate(model, addLink) {
  //addLink {true (addition),false(removal)}
  //console.log(that.value+model);
  if (addLink) {
    if (nptGlobals.linkAvailableArr.length > 0) {
      nptGlobals.nextAvailableId = nptGlobals.linkAvailableArr.shift();
      return nptGlobals.nextAvailableId;
    }
    return nptGlobals.nextLinkId++;
  } else {
    var linkId = model.attributes.linkProperties.linkId;
    console.log("linkId:" + linkId);
    //Add to delete Link set for Live Analysis/Simulation
    //deletedLinkSet.add(linkId);
    sortedPush(nptGlobals.linkAvailableArr, linkId);
  }
}

/************************************************************************
 * Utility function used to push data into the array while maintaining the sort order.
 ************************************************************************/

function sortedPush(array, value) {
  array.splice(_.sortedIndex(array, value), 0, value);
}

/************************************************************************
 * Link Info Tab
 ************************************************************************/
function getElementCid(id) {
  var cell = graph.getCell(id);
  return cell.cid;
}

/************************************************************************
 *  Set degree of a node in NodeProperties
 ************************************************************************/
function setDegreeofNodes() {
  console.log("Inside setDegreeofNodes()");
  _.each(graph.getElements(), function(el) {
    el.attributes.nodeProperties.degree = paperUtil.fGetNodeDegree(el);
  });
}

/************************************************************************
 *  Update Node Connection Properties / Backward Compatibility
 ************************************************************************/
function setNodeConnections() {
  // Initialize node connections
  _.each(graph.getElements(), node => {
    node.attributes.nodeProperties.nodeConnections = fGetNewNodeConnection();
  });

  console.log("Inside setNodeConnections()");
  _.each(graph.getLinks(), function(link) {
    let srcNode = link.getSourceElement();
    let destNode = link.getTargetElement();

    let source = link.get("source");
    let target = link.get("target");

    console.log(
      srcNode.attributes.nodeProperties.nodeId,
      " -- ",
      destNode.attributes.nodeProperties.nodeId,
      " -- ",
      source.dir,
      " -- ",
      target.dir
    );

    // Update SrcNode and DestNode directions in link properties
    link.attributes.linkProperties.SrcNodeDirection = source.dir;
    link.attributes.linkProperties.DestNodeDirection = target.dir;

    // Update SrcNode and DestNode nodeConnections for topology
    srcNode.attributes.nodeProperties.nodeConnections[source.dir] =
      target.NodeId;
    destNode.attributes.nodeProperties.nodeConnections[target.dir] =
      source.NodeId;

    console.log(srcNode.attributes.nodeProperties.nodeConnections);
    console.log(destNode.attributes.nodeProperties.nodeConnections);
    console.log("*************");
  });
}

function fGetNewNodeConnection() {
  return {
    1: 0,
    2: 0,
    3: 0,
    4: 0,
    5: 0,
    6: 0,
    7: 0,
    8: 0
  };
}

/************************************************************************
 * download map
 ************************************************************************/
function offerDownload(name, data) {
  var a = $("<a>");
  a.attr("download", name);
  a.attr(
    "href",
    "data:application/json," + encodeURIComponent(JSON.stringify(data))
  );
  a.attr("target", "_blank");
  a.hide();
  $("body").append(a);
  a[0].click();
  a.remove();
}

/************************************************************************
 * Show properties form when clicked on an element
 ************************************************************************/
function properties(selectedElement) {
  var template;
  var panelHeading = "<strong>Properties</strong>";

  //if link then load link template else load element template
  if (selectedElement.isLink()) {
    template = getLinkTemplate(selectedElement);
    //panelHeading=`<string>Link-${ selectedElement.attributes.linkProperties.linkId } Properties</strong>`
    //console.log(selectedElement.attributes.linkProperties.linkId);
    // $("#propertyCollapse").empty().append(panelHeading);
    // $(".demands-circuit-view__content").empty().append(template);
    // $(".demands-circuit-view__content").append('<button id="linkPropertySave" type="button" class="btn btn-lg btn-block">Set Link Properties</button>');
  } else {
    template = getElementTemplate(selectedElement);
    //console.log(selectedElement.attributes.nodeProperties.nodeId);
    // panelHeading=`<strong>Node-${ selectedElement.attributes.nodeProperties.nodeId } Properties</strong>`;
    //$("#propertyCollapse").empty().append(panelHeading);
    // $(".demands-circuit-view__content").empty().append(template);
    // $(".demands-circuit-view__content").append(`<button id="nodePropertySave" type="button" class="btn btn-lg btn-block">Set Element properties</button>`);

    propertyChecksForNode(selectedElement);
  }

  //console.log(template)
}

/************************************************************************
 * Properties form initial checks for topology
 ************************************************************************/
function propertyChecksForNode(selectedElement) {
  if (selectedElement.get("nodeProperties").gneFlag == 1) {
    /*$("#ipv6").attr('disabled', false);
   		  $("#emsip").attr('disabled', false); 
   		  $("#gateway").attr('disabled', false);
   		  $("#subnet").attr('disabled', false);*/
  }

  if (selectedElement.get("type") == nptGlobals.NodeTypeTwoDegreeRoadm) {
    $("#direction").attr("disabled", true);
  }

  if (
    selectedElement.get("type") != nptGlobals.NodeTypeTwoDegreeRoadm &&
    selectedElement.get("type") != nptGlobals.NodeTypeHub &&
    selectedElement.get("type") != nptGlobals.NodeTypeCDROADM
  ) {
    $(
      '<span id="notApplicable" style="color:red">(Not Applicable for this NE Type)</span>'
    ).insertBefore("#capacity");
    $("#capacity").attr("disabled", true);
  }
}

/************************************************************************
 * Set link color in set link properties (live color change show)
 ************************************************************************/
function setLinkColor(model, that) {
  //console.log("Input that");
  //console.log(that);
  var colorText = $("#color option[value=" + that + "]").text();
  //console.log(that.innerText + model);
  model.attr(".connection/stroke", colorText);
  /* model.attr('.marker-arrowhead/fill', colorText);
	 model.attr('.marker-source/fill', colorText);
	 model.attr('.marker-target/fill', colorText);*/
}

/************************************************************************
 * get Node by NPT custom Id
 ************************************************************************/
function getNodeById(id) {
  //console.log("Inside getNodeById with id ="+id)
  let ID = 0;
  _.each(graph.getElements(), function(el) {
    //console.log("Element"+el);
    if (el instanceof joint.dia.Link) {
      //Do nothing
    } else {
      // console.log(el);
      if (el.attributes.nodeProperties.nodeId == id) {
        //console.log("Success Ids match "+el.attributes.id)
        ID = el.id;
      }
    }
  });
  return ID;
}

/************************************************************************
 * initialize Link Properties
 ************************************************************************/
function initializeLinkProperties(model) {
  //console.log(model);
  var color = document.getElementById("color");
  //console.log(color);
  //console.log(model.attributes.linkProperties.color);
  if (model.attributes.linkProperties.color)
    //So that it doesn't overwrite the default value for that select input
    color.value = model.attributes.linkProperties.color;

  let linkType = document.getElementById("linkType");
  console.log(linkType);
  console.log(model.attributes.linkProperties.linkType);
  if (model.attributes.linkProperties.linkType)
    //So that it doesn't overwrite the default value for that select input
    linkType.value = model.attributes.linkProperties.linkType;
}

/************************************************************************
 * initialize Node Properties
 ************************************************************************/
function initializeNodeProperties(model) {
  console.log("initializeNodeProperties");
  var element = document.getElementById("gneFlag");
  console.log(
    model.attributes.nodeProperties.gneFlag,
    "gneFlag Element",
    element
  );
  if (model.attributes.nodeProperties.gneFlag & (element != null))
    //So that it doesn't overwrite the default value for that select input
    element.value = model.attributes.nodeProperties.gneFlag;

  element = document.getElementById("direction");
  console.log(
    model.attributes.nodeProperties.direction,
    " direction Element",
    element
  );
  if (model.attributes.nodeProperties.direction & (element != null))
    //So that it doesn't overwrite the default value for that select input
    element.value = model.attributes.nodeProperties.direction;

  // var element = document.getElementById('opticalreach');
  //console.log(model.attributes.nodeProperties.gneFlag);
  //if (model.attributes.nodeProperties.opticalreach) //So that it doesn't overwrite the default value for that select input
  //  element.value = model.attributes.nodeProperties.opticalreach;

  element = document.getElementById("capacity");
  console.log(
    model.attributes.nodeProperties.capacity,
    "capacity Element",
    element
  );
  if (model.attributes.nodeProperties.capacity & (element != null))
    //So that it doesn't overwrite the default value for that select input
    element.value = model.attributes.nodeProperties.capacity;
  var element = document.getElementById("gneFlag");
  //console.log(model.attributes.nodeProperties.gneFlag);
  if (model.attributes.nodeProperties.gneFlag)
    //So that it doesn't overwrite the default value for that select input
    element.value = model.attributes.nodeProperties.gneFlag;

  var element = document.getElementById("direction");
  //console.log(model.attributes.nodeProperties.gneFlag);
  if (model.attributes.nodeProperties.direction & (element != null))
    //So that it doesn't overwrite the default value for that select input
    element.value = model.attributes.nodeProperties.direction;

  // var element = document.getElementById('opticalreach');
  //console.log(model.attributes.nodeProperties.gneFlag);
  //if (model.attributes.nodeProperties.opticalreach) //So that it doesn't overwrite the default value for that select input
  //  element.value = model.attributes.nodeProperties.opticalreach;

  var element = document.getElementById("capacity");
  //console.log(model.attributes.nodeProperties.gneFlag);
  if (model.attributes.nodeProperties.capacity)
    //So that it doesn't overwrite the default value for that select input
    element.value = model.attributes.nodeProperties.capacity;

  /*var degree = document.getElementById('degree');
    //console.log(model.attributes.nodeProperties.degree);
    if (model.attributes.nodeProperties.degree) //So that it doesn't overwrite the default value for that select input
        degree.value = model.attributes.nodeProperties.degree;*/
}

/*****************************************************************************
 * *******Update Link Source and Target properties on link removal************
 * *************************************************************************/
var fUpdateConnectionsOnDelete = function(link) {
  console.log("Link :", link);
  var src = graph.getCell(link.source().id);
  var dest = graph.getCell(link.target().id);

  let srcDir = link.source().dir;
  let destDir = link.target().dir;

  // Update Src Node connected links direction
  fUpdateConnectedLinksOnChange(src, srcDir);
  // Update Dest Node connected links direction
  fUpdateConnectedLinksOnChange(dest, destDir);

  // Decrement ramanLinks count on nodes if Raman Link is deleted
  if (
    link.get("linkProperties").linkType == nptGlobals.getRamanDraLinkStr() ||
    link.get("linkProperties").linkType == nptGlobals.getRamanHybridLinkStr()
  ) {
    // Update direction of raman type links only if there were two raman links
    fUpdateRamanLinksOnDelete(src);
    fUpdateRamanLinksOnDelete(dest);
  }
};

/*****************************************************************************
 *  Update Dir of higher direction links when a link with lower direction
 *  is removed
 * *************************************************************************/
var fUpdateRamanLinksOnDelete = node => {
  let nodeId = node.get("nodeProperties").nodeId;
  console.log("fUpdateRamanLinksOnDelete():", nodeId);
  if (node.get("nodeProperties").ramanLinks > 1) {
    // Find other Raman links on this node
    let ramanLinksOnNode = _.filter(graph.getLinks(), link => {
      return (
        (link.source().NodeId == nodeId || link.target().NodeId == nodeId) &&
        link.get("linkProperties").linkType != nptGlobals.getDefaultLinkStr()
      );
    });
    console.log("ramanLink node::", ramanLinksOnNode);
    let otherLink = ramanLinksOnNode[0];
    // Change dir of other raman link on this node
    if (otherLink.source().NodeId == nodeId)
      otherLink.get("source").dir = otherLink.source().dir - 1;
    else otherLink.get("target").dir = otherLink.target().dir - 1;
  }

  node.get("nodeProperties").ramanLinks -= 1;
};

/*****************************************************************************
 *  Update Dir of other raman link ; if one of the raman link is deleted
 * *************************************************************************/
var fUpdateConnectedLinksOnChange = (node, dir) => {
  if (node) {
    let nodeId = node.get("nodeProperties").nodeId;
    let connectedLinks = graph.getConnectedLinks(node);
    _.each(connectedLinks, link => {
      // Change dir of only those links whose direction was more than the direction of the link removed

      if (link.source().NodeId == nodeId && link.source().dir > dir) {
        // Update direction of default type links
        if (
          link.get("linkProperties").linkType == nptGlobals.getDefaultLinkStr()
        )
          link.get("source").dir = link.source().dir - 1;
      } else if (link.target().NodeId == nodeId && link.target().dir > dir) {
        // Update direction of default type links
        if (
          link.get("linkProperties").linkType == nptGlobals.getDefaultLinkStr()
        )
          link.get("target").dir = link.target().dir - 1;
      }
    });
  }
};

/************************************************************************
 *  Validate Link Properties
 ************************************************************************/
function validateLinkProperties(linkModel, linkUserInput) {
  // let isLinkPropertiesValid = true;
  // let linkId = linkUserInput.LinkId;
  // let linkCid = linkUserInput.cid;
  let spanLength = linkUserInput.spanlength;
  // let fiberType = linkUserInput.fibertype;
  // let splice = linkUserInput.splice;
  // let lossPerSplice = linkUserInput.lossPerSplice;
  // let connector = linkUserInput.connector;
  // let lossPerConnector = linkUserInput.lossPerConnector;
  let calculatedSpanLoss = linkUserInput.calculatedSpanLoss;
  // let adjustableSpanLoss = linkUserInput.adjustableSpanLoss;
  // let costMetric = linkUserInput.costmetric;
  // let opticalParameter = linkUserInput.opticalparameter;
  // let srlg = linkUserInput.srlg;
  // let color = linkUserInput.color;
  let linkType = linkUserInput.linkType;

  // let errorString = "";

  if (
    linkType == nptGlobals.getRamanDraLinkStr() &&
    calculatedSpanLoss > nptGlobals.getRamanDraMaxSpanloss()
  ) {
    bootBoxDangerAlert(
      "DRA link can have maximum spanloss of 13 without ILA(Raman Dra)."
    );
    $("#calculatedSpanLoss").focus();
    return false;
  }

  if (
    linkType == nptGlobals.getDefaultLinkStr() &&
    calculatedSpanLoss > nptGlobals.getDefaultLinkMaxSpanLoss()
  ) {
    bootBoxDangerAlert(
      "Default link can have maximum spanloss of 35 without ILA(PA/BA)."
    );
    $("#calculatedSpanLoss").focus();
    return false;
  }

  if (
    linkType == nptGlobals.getRamanHybridLinkStr() &&
    calculatedSpanLoss > nptGlobals.getRamanHybridMaxSpanloss()
  ) {
    bootBoxDangerAlert(
      "Raman Hybrid link can have maximum spanloss of 37 without ILA(Raman Hybrid)."
    );
    $("#calculatedSpanLoss").focus();
    return false;
  }

  //	if(linkType==nptGlobals.getRamanHybridLinkStr() && spanLength < 75){
  //		bootBoxDangerAlert("Raman Hybrid link should have minimum spanlength of 75 with or withouy ILA(Raman Hybrid).");
  //		$("#spanLength").focus();
  //		return false;
  //	}

  // if (adjustableSpanLoss < minSpanLoss || adjustableSpanLoss > maxSpanLoss) {
  // 	//errorString+="<p class=' lead errorForm'>Link is not feasible one, Please Change the parameters to make spanloss in range of 20 to 35</p><hr>";
  // 	//bootBoxAlert("Link is not feasible , Please Change the parameters to make spanloss in range of 20 to 35 ");
  // 	$("#adjustedSpanLoss").focus();
  // 	//console.log("After Link not complete Error!");
  // }

  // if (spanLength > spanLengthLimit || spanLength < 5) {
  // 	bootBoxWarningAlert(`Spanlength cannot be greater than ${spanLengthLimit} for a link`);
  // 	//bootBoxAlert("Spanlength cannot be greater than "+spanLengthLimit+" for a link");
  // 	$(this).val(spanLengthInitial);
  // 	isLinkPropertiesValid =false;

  // }

  if (spanLength < 5) {
    bootBoxDangerAlert(
      `Spanlength cannot be less than ${spanLengthLimit} for a link`
    );
    //bootBoxAlert("Spanlength cannot be greater than "+spanLengthLimit+" for a link");
    $(this).val(spanLengthInitial);
    return false;
  }

  // let linkModel = graph.getCell(linkCid);

  if (
    linkModel.getSourceElement().get("nodeProperties").ramanLinks > 2 ||
    linkModel.getTargetElement().get("nodeProperties").ramanLinks > 2
  ) {
    bootBoxDangerAlert("Cannot Add more than two Raman Links from a node");
    return false;
  }

  return true;
}

/************************************************************************
 *  Validate Node Properties
 ************************************************************************/

var SiteAndStationNameMap = new Map();

function validateNodeProperties(model, nodeUserInput) {
  console.log(model);
  //var gneFlag="attributes.nodeProperties.gneFlag";
  var sameStrFlag = true;
  var nodeId = model.get("nodeProperties").nodeId;
  console.log("nodeId :" + nodeId);

  let isGneSetValid = checkForGneSet(
    model.attributes.nodeProperties.gneFlag,
    nodeUserInput.gneflag
  );

  if (isGneSetValid) {
    if (nodeUserInput.gneflag == 1) nptGlobals.isPrimaryGneSet = true;
    else if (nodeUserInput.gneflag == 2) nptGlobals.isSecondaryGneSet = true;
  } else {
    bootBoxWarningAlert(
      "There can be only one primary and one secondary GNE node."
    );
    return false;
  }

  let stationName = nodeUserInput.stationname,
    siteName = nodeUserInput.sitename;

  let regEx = /\W\s*/g;
  let strStationNameMatch = stationName.match(regEx);
  if (strStationNameMatch != null) {
    bootBoxWarningAlert("StationName cannot have special characters.");
    return false;
  }
  let strSiteNameMatch = siteName.match(regEx);
  if (strSiteNameMatch != null) {
    bootBoxWarningAlert("SiteName cannot have special characters.");
    return false;
  }

  var siteStationNameStr = nodeUserInput.stationname + nodeUserInput.sitename;

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
  if (nodeUserInput.sitename == nodeUserInput.stationname) {
    bootBoxWarningAlert("Sitename and StationName cannot be same.");
    return false;
  } else if (
    /*SiteAndStationNameSet.has(siteStationNameStr) && */ !sameStrFlag
  ) {
    //if true then
    //console.log("No two Sitename and StationName combination can be same.");
    bootBoxWarningAlert(
      "No two Sitename and StationName combination can be same."
    );
    return false;
  } else {
    //SiteAndStationNameSet.add(siteStationNameStr);
    SiteAndStationNameMap.set(String(nodeId), siteStationNameStr);
  }

  return true;
}

/*//Spanlength of link
$("body").delegate("#spanLength", "change", function() {
	console.log("#spanLength")
	var val=$(this).val();
	var spanLengthLimit=640;
	var spanLengthInitial=40;
	var spanLengthIla=80;
	
	console.log("Input valu::"+val);
	if(val>spanLengthLimit || val<5)
		{
		bootBoxAlert("Spanlength cannot be greater than "+spanLengthLimit+" for a link");
	    $(this).val(spanLengthInitial);		
		}
	else if(val>spanLengthIla)
		{
		 
		}
	
})*/

/**
 * Cd, Pmd On Fiber Type Change, Span length change or Coefficient change
 */
var changeCdPmdLinkParams = function(spanLength, cdCoff, pmdCoff) {
  console.log("Fiber Type Changed to : " + $("#fiberType").val());
  console.log("spanLength, cdCoff, pmdCoff " + spanLength, cdCoff, pmdCoff);
  /**Change Params*/
  if ($("#fiberType").val() == 1) {
    /**G.652.D*/

    var cdValue = eval(cdCoff * spanLength).toFixed(2);
    var pmdValue = eval(pmdCoff * Math.sqrt(spanLength)).toFixed(2);
    console.log("cdValue : " + cdValue + " and pmdValue : " + pmdValue);
    $("#cd").val(cdValue);
    $("#pmd").val(pmdValue);
  } else if ($("#fiberType").val() == 2) {
    /**G.655*/

    var cdValue = eval(cdCoff * spanLength).toFixed(2);
    var pmdValue = eval(pmdCoff * Math.sqrt(spanLength)).toFixed(2);
    console.log("cdValue : " + cdValue + " and pmdValue : " + pmdValue);
    $("#cd").val(cdValue);
    $("#pmd").val(pmdValue);
  }
};

$("body").delegate("#fiberType", "change", function() {
  changeCdPmdLinkParams(
    $("#spanLength").val(),
    $("#cdCoefficient").val(),
    $("#pmdCoefficient").val()
  );
});

$("body").delegate("#cdCoefficient", "change", function() {
  changeCdPmdLinkParams(
    $("#spanLength").val(),
    $("#cdCoefficient").val(),
    $("#pmdCoefficient").val()
  );
});

$("body").delegate("#pmdCoefficient", "change", function() {
  changeCdPmdLinkParams(
    $("#spanLength").val(),
    $("#cdCoefficient").val(),
    $("#pmdCoefficient").val()
  );
});

/***
 * Calculate Spanloss in case of any change of following params
 */
$("body").delegate("#spanLength", "change", function() {
  calculateSpanLoss();
  changeCdPmdLinkParams(
    $("#spanLength").val(),
    $("#cdCoefficient").val(),
    $("#pmdCoefficient").val()
  );
});

$("body").delegate("#splice", "change", function() {
  calculateSpanLoss();
});

$("body").delegate("#lossPerSplice", "change", function() {
  calculateSpanLoss();
});

$("body").delegate("#connector", "change", function() {
  calculateSpanLoss();
});

$("body").delegate("#lossPerConnector", "change", function() {
  calculateSpanLoss();
});

$("body").delegate("#coefficient", "change", function() {
  calculateSpanLoss();
});

$("body").delegate("#calculatedSpanLoss", "change", function() {
  setAdjustableSpanLoss($("#calculatedSpanLoss").val());
});

// $("body").delegate("#linkType", "change", function () {
// 	setAdjustableSpanLoss($("#calculatedSpanLoss").val());
// });

/**
 * @brief formula to calculate spanloss : [(0.275 * fiberLength)+ (no. of splice * loss per splice ) + (no. of connector * loss per connector)]
 * @returns
 */
function calculateSpanLoss() {
  var spanLoss = eval(
    $("#coefficient").val() * $("#spanLength").val() +
      $("#splice").val() * $("#lossPerSplice").val() +
      $("#connector").val() * $("#lossPerConnector").val()
  ).toFixed(2);

  $("#calculatedSpanLoss").val(spanLoss);
  console.log("spanLoss :- " + spanLoss);

  // if (spanLoss > maxAllowedSpanLoss) {
  // 	bootbox.confirm({
  // 		size: "small",
  // 		title: "SpanLoss is More than 35!",
  // 		message: 'Link is not feasible one, Please Change the parameters to make spanloss in range of 20 to 35 ',

  // 		callback: function (result) { /* result = String containing user input if OK clicked or null if Cancel clicked */
  // 			/*if(result<minSpanLoss || result>maxSpanLoss){
  // 				alert('Please Enter Spanloss Between 20 to 35!');
  // 				calculateSpanLoss();
  // 			}
  // 			else if(result >=minSpanLoss && result <= maxSpanLoss){
  // 				setAdjustableSpanLoss(result);
  // 			}
  // 			else{
  // 				setAdjustableSpanLoss(minSpanLoss);
  // 			}*/

  // 			$("#adjustedSpanLoss").val(0);

  // 			//$("#adjustedSpanLoss").val(20);
  // 			$("#adjustedSpanLoss").focus();

  // 		}
  // 	});
  // }
  // else if (spanLoss <= maxSpanLoss) {
  // 	setAdjustableSpanLoss(spanLoss);
  // }
  setAdjustableSpanLoss(spanLoss);

  console.log("After Alert!");
  /*$("#calculatedSpanLoss").val(spanLoss);
	setAdjustableSpanLoss(spanLoss);*/
}

/**
 * @brief min span loss: minSpanLoss(20) and max : maxSpanLoss(35)
 */
function setAdjustableSpanLoss(spanLoss) {
  let linkType = $("#linkType").val();
  let maxAllowedSpanLoss = nptGlobals.getDefaultLinkMaxSpanLoss(),
    minAllowedSpanLoss = nptGlobals.getDefaultLinkMinSpanLoss();
  if (linkType == nptGlobals.getRamanHybridLinkStr()) {
    maxAllowedSpanLoss = nptGlobals.getRamanHybridMaxSpanloss();
    minAllowedSpanLoss = 19;
  } else if (linkType == nptGlobals.getRamanDraLinkStr()) {
    maxAllowedSpanLoss = nptGlobals.getRamanDraMaxSpanloss();
    minAllowedSpanLoss = nptGlobals.getRamanDraMaxSpanloss();
  }

  if (spanLoss > maxAllowedSpanLoss) {
    /**span loss greater then max value*/
    // $("#adjustedSpanLoss").val(0);
    $("#adjustedSpanLoss").val(maxAllowedSpanLoss);
  } else if (spanLoss < minAllowedSpanLoss) {
    /**span loss less then min value*/
    $("#adjustedSpanLoss").val(minAllowedSpanLoss);
  } else {
    /**span loss within the range*/
    $("#adjustedSpanLoss").val(spanLoss);
  }

  console.log(
    "After setAdjustableSpanLoss spanloss: " + $("#adjustedSpanLoss").val()
  );
}

/************************************************************************
 * Validate GNE
 ************************************************************************/
function checkForGneSet(prevGne, currGne) {
  // If set to 'NO' , then update primary/ secondary flag
  if (prevGne == 1) nptGlobals.isPrimaryGneSet = false;
  else if (prevGne == 2) nptGlobals.isSecondaryGneSet = false;

  //Primary Case
  if (currGne == 1) {
    if (nptGlobals.isPrimaryGneSet) return false;
  }
  //Secondary Case
  else if (currGne == 2) {
    if (nptGlobals.isSecondaryGneSet) return false;
  }

  return true;

  //   var isGneSetValid = 1;
  //   console.log("prevGne -->" + prevGne);
  //   console.log("currGne -->" + currGne);
  //   //console.log(gneInput);
  //   //gneFlagInputValue=gneInput.value;
  //   if (prevGne == 0 && currGne == 1) {
  //     if (nptGlobals.isGneFlag) {
  //       //			bootBoxAlert("GNE Flag can only be set for one Node");
  //       setGneFlagInputValue(0);
  //       isGneSetValid = 0;
  //       //gneInput.value=0;
  //     } else {
  //       nptGlobals.isGneFlag = 1;
  //       //gneInput.value=1;
  //     }
  //   } else if (prevGne == 1 && currGne == 0) {
  //     nptGlobals.isGneFlag = 0;
  //     //gneInput.value=0;
  //   } else {
  //     console.log("Do nothing");
  //     console.log("prevGne -->" + prevGne);
  //     console.log("currGne -->" + currGne);
  //   }
  //   //console.log("Vlaue of IsGne"+nptGlobals.isGneFlag);
  //   return isGneSetValid;
}

/************************************************************************
 * Set Gne Flag
 ************************************************************************/
function setGneFlagInputValue(value) {
  //console.log(model);
  var element = document.getElementById("gneFlag");
  element.value = value;
}

$("body").delegate("#gneFlag", "change", function() {
  var gneinputValue = $(this).val();

  if (gneinputValue == 0) {
    //gneInput.style.border="border","2px solid red";
    console.log("gneinputValue " + gneinputValue);
    //
    /*$("#ipv6").attr('disabled', true);
		 $("#emsip").attr('disabled', true); 
		 $("#gateway").attr('disabled', true);
		 $("#subnet").attr('disabled', true);*/
    //bootBoxAlert("You have already set a GNE Node and there can be only one GNE Node .");
  } else if (gneinputValue == 1) {
    console.log("gneinputValue " + gneinputValue);
    /*$("#ipv6").attr('disabled', false);
		$("#emsip").attr('disabled', false); 
		$("#gateway").attr('disabled', false);
		$("#subnet").attr('disabled', false);*/
  }
});

/************************************************************************
 * Set Gne Flag
 ************************************************************************/
function alertUserforWrongGneFlagInputValue(gneInput) {
  console.log("alertUserforWrongGneFlagInputValue");
  var gneinputValue = gneInput.value;
  console.log("gneinputValue " + gneinputValue);
  if (gneinputValue == 0) {
    //gneInput.style.border="border","2px solid red";
    /*$("#ipv6").attr('disabled', true);
		 $("#emsip").attr('disabled', true); 
		 $("#gateway").attr('disabled', true);
		 $("#subnet").attr('disabled', true);*/
    //bootBoxAlert("You have already set a GNE Node and there can be only one GNE Node .");
  } else if (!nptGlobals.isGneFlag && gneinputValue == 0) {
    console.log("Do nothing");
    /*$("#ipv6").attr('disabled', false);
		$("#emsip").attr('disabled', false); 
		$("#gateway").attr('disabled', false);
		$("#subnet").attr('disabled', false);*/
  }
}

/************************************************************************
 *  Validate Link Spanlength (Ila Insertion )
 ************************************************************************/
// function validateIlaInsertion(linkModel, linkUserInput) {
//   // var spanLength = linkUserInput.spanlength;
//   // var spanLoss = linkUserInput.calculatedSpanLoss;
//   // console.log("linkUserInput");
//   // console.log(linkUserInput);

//   IlaModule.spanLength = linkUserInput.spanLength;
//   IlaModule.spanLoss = linkUserInput.calculatedSpanLoss;
//   IlaModule.linkType = linkUserInput.linkType;
//   IlaModule.linkModel = linkModel;

//   //var minIla=Math.max(Math.floor(spanLength/80),Math.floor(spanLoss/35));
//   // let minIla;
//   // if (spanLoss > 35) {
//   //   minIla = Math.floor(spanLoss / 35);
//   // } else {
//   //   minIla = 1;
//   // }

//   // var dataObjectIlaInsertion = {
//   //   spanLength: spanLength,
//   //   spanLoss: spanLoss,
//   //   minIla: minIla,
//   //   calculatedSpanLoss: spanLoss
//   // };

//   var messageAlert = "",
//     titleText = `<p class="text-center lead">ILA insertion dialog</p>`;

//   //alert message
//   //if(spanLength>80 && spanLoss <35)
//   //	messageAlert=`Span length is more than <strong>80 kms</strong>. <br> Minimum <strong>${minIla}</strong> Ila needs to be placed between Node ${linkModel.get("source").NodeId} and Node ${linkModel.get("target").NodeId} if the link is to be homogeneously designed.`;
//   //else if(spanLength<80 && spanLoss >35)
//   //	messageAlert=`Span loss is more than <strong>35 Db</strong>. <br> Minimum <strong>${minIla}</strong> Ila needs to be placed between Node ${linkModel.get("source").NodeId} and Node ${linkModel.get("target").NodeId} if the link is to be homogeneously designed.`;
//   //else messageAlert=`Span length is more than  <strong>80 kms</strong> and Span Loss is also more than <strong>35 Db</strong>. <br>Minimum <strong>${minIla}</strong> Ila needs to be placed between Node ${linkModel.get("source").NodeId} and Node ${linkModel.get("target").NodeId} if the link is to be homogeneously designed.`;

//   messageAlert += `Span loss this link is <strong><mark>${
//     IlaModule.spanLoss
//   } dB</mark></strong>. <p>`;
//   //  Minimum <strong>${minIla}</strong> ILA needs to be placed between
//   // Node ${linkModel.get("source").NodeId} and Node ${linkModel.get("target").NodeId} if the link is to be homogeneously designed.</p>`;

//   messageAlert += `<br><br> <strong>Option 1 :</strong> Automatically add <mark>ILA(PA/BA)</mark> between the nodes.`;
//   messageAlert += `<br><br> <strong>Option 2 :</strong> Automatically add <mark>ILA (RAMAN HYBRID)</mark> between the nodes.`;
//   messageAlert += `<br><br> <strong>Option 3 :</strong> Automatically add <mark>ILA (RAMAN DRA)</mark> between the nodes.`;
//   // messageAlert += `<br><br> <strong>Option 4 :</strong> If <mark>Spanloss < 37</mark>, then we recommend using <mark>RAMAN(HYBRID)</mark> on source/destination node. No ILA is required in such case.`;
//   messageAlert += `<br><br> <strong>Option 4 :</strong> If you want to specifically design the link as per your requirements,
// 		 then you need to provide Spanlength values for all links alongwith ILA type in the next screen.`;

//   messageAlert += `<br><br>Press <span class="text-danger">Cancel</span>
// 		to adjust the link parameters . <br> Choose  <strong class="text-success">Any Option </strong> to add amplifier on this link or at source/destination nodes.`;

//   //confirm user input
//   //var result= confirm(messageAlert);

//   bootbox.dialog({
//     message: messageAlert,
//     title: titleText,
//     buttons: {
//       "Option 1": {
//         //label: "Success!",
//         className: "btn btn--default",
//         callback: function() {
//           // dataObjectIlaInsertion.option = "option 1";
//           IlaModule.userChoice = IlaModule.choiceOne;
//           insertIla();
//         }
//       },
//       "Option 2": {
//         className: "btn btn--default",
//         callback: function() {
//           // dataObjectIlaInsertion.option = "option 2";
//           IlaModule.userChoice = IlaModule.choiceTwo;
//           insertIla();
//         }
//       },
//       "Option 3": {
//         className: "btn btn--default",
//         callback: function() {
//           // dataObjectIlaInsertion.option = "option 3";
//           IlaModule.userChoice = IlaModule.choiceThree;
//           insertIla();
//         }
//       },
//       "Option 4": {
//         className: "btn btn--default",
//         callback: function() {
//           // dataObjectIlaInsertion.option = "option 4";
//           IlaModule.userChoice = IlaModule.choiceFour;
//           insertIla();
//         }
//       },
//       Cancel: {
//         className: "btn btn--default",
//         callback: function() {}
//       }
//     }
//   });
// }

/************************************************************************
 *  Insert Ila
 ************************************************************************/
// function insertIla() {
//   console.log("Inside insertIla");

//   // let spanLoss = dataObjectIlaInsertion.spanLoss;
//   let spanLoss = IlaModule.spanLoss;

//   IlaModule.numOfIla = 1;
//   if (IlaModule.userChoice == IlaModule.choiceOne) {
//     IlaModule.ilaType = IlaModule.linkType = nptGlobals.getDefaultLinkStr();
//     // dataObjectIlaInsertion.ilaType = nptGlobals.getDefaultLinkStr();
//     if (spanLoss > nptGlobals.getDefaultLinkMaxSpanLoss()) {
//       // ilaReq = Math.floor(spanLoss / nptGlobals.getDefaultLinkMaxSpanLoss());
//       IlaModule.numOfIla = Math.floor(
//         spanLoss / nptGlobals.getDefaultLinkMaxSpanLoss()
//       );
//     }
//   } else if (IlaModule.userChoice == IlaModule.choiceTwo) {
//     IlaModule.ilaType = IlaModule.linkType = nptGlobals.getRamanHybridLinkStr();
//     // dataObjectIlaInsertion.ilaType = nptGlobals.getRamanHybridLinkStr();
//     if (spanLoss > nptGlobals.getRamanHybridMaxSpanloss()) {
//       // ilaReq = Math.floor(spanLoss / nptGlobals.getRamanHybridMaxSpanloss());
//       IlaModule.numOfIla = Math.floor(
//         spanLoss / nptGlobals.getRamanHybridMaxSpanloss()
//       );
//     }
//   } else if (IlaModule.userChoice == IlaModule.choiceThree) {
//     IlaModule.ilaType = IlaModule.linkType = nptGlobals.getRamanDraLinkStr();
//     // dataObjectIlaInsertion.ilaType = nptGlobals.getRamanDraLinkStr();
//     if (spanLoss > nptGlobals.getRamanDraMaxSpanloss()) {
//       // ilaReq = Math.floor(spanLoss / nptGlobals.getRamanDraMaxSpanloss());
//       IlaModule.numOfIla = Math.floor(
//         spanLoss / nptGlobals.getRamanDraMaxSpanloss()
//       );
//     }
//   } else if (IlaModule.userChoice == IlaModule.choiceFour) {
//     // IlaModule.ilaType = IlaModule.linkType = IlaModule.linkUserInput.linkType;
//     // dataObjectIlaInsertion.ilaType = linkModel.get("linkProperties").linkType;
//     // ilaReq = Math.floor(spanLoss / nptGlobals.getDefaultLinkMaxSpanLoss());
//     IlaModule.numOfIla = Math.floor(
//       spanLoss / nptGlobals.getMaxSpanLoss(IlaModule.linkType)
//     );
//     if (IlaModule.numOfIla == 0) {
//       IlaModule.numOfIla = 1;
//     }
//   }

//   bootBoxWarningAlert("No. of Ila needed:", IlaModule.numOfIla);
//   // dataObjectIlaInsertion.minIla = ilaReq;

//   if (IlaModule.numOfIla > 0) {
//     //if users input is correct for ILA insertion
//     // Save original parameters for future reference

//     // nptGlobals.setIlaInsertionOriginalLinkParameters(
//     //   linkModel,
//     //   dataObjectIlaInsertion.minIla,
//     //   dataObjectIlaInsertion.spanLength,
//     //   dataObjectIlaInsertion.spanLoss
//     // );

//     getSpanlengthforIlaLinksFromUser();
//   } else {
//     // console.log(" No Ila needed");
//     bootBoxWarningAlert("No Ila needed");
//   }
// }

/************************************************************************
 *  Get user input for spanlength of ILA links to be inserted
 ************************************************************************/

// function getSpanlengthforIlaLinksFromUser() {
//   // var numOfIla = dataObjectIlaInsertion.minIla;
//   let numOfIla = IlaModule.numOfIla;

//   let numOfLinks = numOfIla + 1;
//   // var spanLengthInitial = Math.round(
//   //     dataObjectIlaInsertion.spanLength / numOfLinks
//   //   ),
//   //   spanLossInitial = Math.round(dataObjectIlaInsertion.spanLoss / numOfLinks),
//   //   spanLengthRemaining = dataObjectIlaInsertion.spanLength,
//   //   spanLossRemaining = dataObjectIlaInsertion.spanLoss;

//   var spanLengthInitial = Math.round(IlaModule.spanLength / numOfLinks),
//     spanLossInitial = Math.round(IlaModule.spanLoss / numOfLinks),
//     spanLengthRemaining = IlaModule.spanLength,
//     spanLossRemaining = IlaModule.spanLoss;

//   let offsetSpanLength,
//     offsetSpanloss = nptGlobals.getMaxSpanLoss(IlaModule.linkType);
//   if (IlaModule.linkType == nptGlobals.getDefaultLinkStr()) {
//     offsetSpanlength = 120;
//   } else if (IlaModule.linkType == nptGlobals.getRamanHybridLinkStr()) {
//     offsetSpanlength = 126;
//   } else {
//     offsetSpanlength = 40;
//   }
//   spanLengthInitial =
//     spanLengthInitial > offsetSpanLength
//       ? spanLengthInitial - 1
//       : spanLengthInitial;
//   spanLossInitial =
//     spanLossInitial > offsetSpanloss ? spanLossInitial - 1 : spanLossInitial;

//   let obj;
//   if (
//     IlaModule.userChoice == IlaModule.choiceOne ||
//     IlaModule.userChoice == IlaModule.choiceTwo ||
//     IlaModule.userChoice == IlaModule.choiceThree
//   ) {
//     // linkModel.get("linkProperties").linkType = dataObjectIlaInsertion.ilaType;
//     // linkModel.get("linkProperties").linkType = IlaModule.ilaType;

//     // Serialize initial (form) data into its object
//     let arr = [];
//     let j = 0;
//     for (var i = 0; i < numOfIla * 2; i += 2) {
//       obj = {};
//       obj["name"] = `spanLength_${j}`;
//       obj["value"] = spanLengthInitial;
//       arr.push(obj);

//       obj = {};
//       obj["name"] = `spanLoss_${j++}`;
//       obj["value"] = spanLossInitial;
//       arr.push(obj);

//       spanLengthRemaining -= spanLengthInitial;
//       spanLossRemaining -= spanLossInitial;
//       console.log(
//         "spanLengthRemaining",
//         spanLengthRemaining,
//         "spanLossRemaining",
//         spanLossRemaining
//       );
//     }

//     obj = {};
//     obj["value"] = Math.round(spanLengthRemaining * 100) / 100;
//     obj["name"] = `spanLength_${j}`;
//     arr.push(obj);

//     obj = {};
//     obj["name"] = `spanLoss_${j}`;
//     obj["value"] = Math.round(spanLossRemaining * 100) / 100;
//     arr.push(obj);

//     console.log("Array of Automatic Spanlengths ::", arr);

//     // dataObjectIlaInsertion.SpanlengthArray = arr;
//     IlaModule.SpanlengthArray = arr;
//     fIlaLinksInputFormSubmit();
//   } else if (IlaModule.userChoice == IlaModule.choiceFour) {
//     var title = ` <p class="lead text-center text-primary"><strong>Set spanlength Values for links</strong></p>
//     <hr>
//     <div class="row">
//       <div class="col-md-12 text-center">
//         <p><small>AMPLIFIER TYPE : </small><strong>${
//           IlaModule.ilaType
//         }</strong></p>
//       </div>
// 	  </div>`;
//     //  <div class="form-group form-inline">
//     // 	 <label for="ilaType">ILA Type:</label>
//     // 	<select class="form-control" name="ilaType" id="ilaType">
//     // 		<option >${nptGlobals.getDefaultLinkStr()}</option>
//     // 		<option >${nptGlobals.getRamanHybridLinkStr()}</option>
//     // 		<option >${nptGlobals.getRamanDraLinkStr()}</option>
//     // 	</select>
//     // 	</div> ;

//     let template = `<div id="ilaLinksInputDiv">`;
//     // template += fGetManualIlaInsertionTemplate(
//     //   dataObjectIlaInsertion.ilaType,
//     //   dataObjectIlaInsertion.minIla
//     // );
//     template += fGetManualIlaInsertionTemplate();
//     template += `</div>`;

//     bootbox.dialog({
//       message: template,
//       title: title,
//       buttons: {
//         Back: {
//           className: "btn btn--default",
//           callback: function() {
//             validateIlaInsertion(linkModel, window.linkUserInput);
//           }
//         },
//         "Reset to Default": {
//           className: "btn btn--default",
//           callback: function() {
//             getSpanlengthforIlaLinksFromUser();
//           }
//         },
//         "Insert Ila": {
//           className: "btn btn--default",
//           callback: function() {
//             fIlaLinksInputFormSubmit();
//           }
//         },
//         Cancel: {
//           className: "btn btn--default",
//           callback: function() {}
//         }
//       }
//     });

//     // spanLengthInputFieldsIndexSet.clear();
//     // spanLossInputFieldsIndexSet.clear();
//     IlaModule.spanLengthInputFieldsIndexSet.clear();
//     IlaModule.spanLossInputFieldsIndexSet.clear();
//     updateSpanLossInManualIlaInsertion();
//   }
// }

/************************************************************************
 *  Get manual template
 ************************************************************************/
// function fGetManualIlaInsertionTemplate() {
//   let numOfIla = IlaModule.numOfIla;
//   let numOfLinks = numOfIla + 1;
//   let linkModel = IlaModule.linkModel;
//   var srcNodeId = linkModel.get("source").NodeId;
//   var destNodeId = linkModel.get("target").NodeId;

//   var spanLengthInitial = Math.round(IlaModule.spanLength / numOfLinks),
//     spanLossInitial = Math.round(IlaModule.spanLoss / numOfLinks),
//     spanLengthRemaining = IlaModule.spanLength,
//     spanLossRemaining = IlaModule.spanLoss;

//   let offsetSpanLength,
//     offsetSpanloss = nptGlobals.getMaxSpanLoss(IlaModule.linkType);
//   if (IlaModule.linkType == nptGlobals.getDefaultLinkStr()) {
//     offsetSpanlength = 120;
//   } else if (IlaModule.linkType == nptGlobals.getRamanHybridLinkStr()) {
//     offsetSpanlength = 126;
//   } else {
//     offsetSpanlength = 40;
//   }
//   spanLengthInitial =
//     spanLengthInitial > offsetSpanLength
//       ? spanLengthInitial - 1
//       : spanLengthInitial;
//   spanLossInitial =
//     spanLossInitial > offsetSpanloss ? spanLossInitial - 1 : spanLossInitial;

//   let template = `<form id="ilaLinksInputForm">

// 		<div class="form-group first" >
// 		<p class="text-center strong"><strong>Node ${srcNodeId} to ILA 1</strong></p>
// 		<div class="row">
// 		<div class="col-md-6 col-sm-6">
// 		<label for="spanlength">Spanlength (kms)</label>
// 		<input  type="number" min="1" max="640" step="1" class="form-control spanLengthIlaLinks" placeholder="40"  value="${spanLengthInitial}" name="spanLength_0">
// 		</div>
// 		<div class="col-md-6 col-sm-6">
// 		<label for="spanloss">Spanloss (db)</label>
// 		<input  type="number" min="1" max="35" step="1" class="form-control spanLossIlaLinks" placeholder="40"  value="${spanLossInitial}" name="spanLoss_0" readonly>
// 		</div>	</div>
// 	</div>`;

//   spanLengthRemaining = spanLengthRemaining - spanLengthInitial;
//   spanLossRemaining = spanLossRemaining - spanLossInitial;

//   for (var i = 1; i < numOfIla; i++) {
//     template += `<div class="form-group" >
// 		<p class="text-center"><strong>ILA ${i} to ILA ${i + 1}</strong></p>
// 		<div class="row">
// 		<div class="col-md-6 col-sm-6">
// 		<label for="spanlength">Spanlength (kms)</label>
// 		<input  type="number" min="1" max="640" step="1" class="form-control spanLengthIlaLinks" placeholder="${spanLengthInitial}"  value="${spanLengthInitial}" name="spanLength_${i}">
// 		</div>
// 		<div class="col-md-6 col-sm-6">
// 		<label for="spanloss">Spanloss (db)</label>
// 		<input  type="number" min="1" max="35" step="1" class="form-control spanLossIlaLinks" placeholder="${spanLossInitial}"  value="${spanLossInitial}" name="spanLoss_${i}" readonly>
// 		</div>		</div>
// 	</div>`;
//     spanLengthRemaining = spanLengthRemaining - spanLengthInitial;
//     spanLossRemaining = spanLossRemaining - spanLossInitial;
//   }

//   template += `<div class="form-group" >
// 		<p class="text-center"><strong>ILA ${i} to Node ${destNodeId}</strong></p>
// 		<div class="row">
// 		<div class="col-md-6 col-sm-6">
// 		<label for="spanlength">Spanlength (kms)</label>
// 		<input  type="number" min="1" max="640" step="1" class="form-control spanLengthIlaLinks" placeholder="${spanLengthRemaining}"  value="${spanLengthRemaining}" name="spanLength_${i}">
// 		</div>
// 		<div class="col-md-6 col-sm-6">
// 		<label for="spanloss">Spanloss (db)</label>
// 		<input  type="number" min="1" max="35" step="1" class="form-control spanLossIlaLinks" placeholder="${spanLossRemaining}"  value="${spanLossRemaining}" name="spanLoss_${i}" readonly>
// 		</div>	</div>
// 	</div>

// 	</form>`;
//   return template;
// }

/************************************************************************
 *  Update Span Loss of Individual links based on link parameters and span lengths
 ************************************************************************/
// function updateSpanLossInManualIlaInsertion() {
//   var spanLengthLinks = $(".spanLengthIlaLinks").toArray();
//   var spanLossLinks = $(".spanLossIlaLinks").toArray();
//   console.log("spanLossLinks ::", spanLossLinks);
//   console.log("Ila User Input Link", window.linkUserInput);
//   let originalLinkProperties = window.linkUserInput;
//   spanLengthLinks.map(function(spanLengthInput, index) {
//     console.log(
//       "$spanLengthInput.val()",
//       $(spanLengthInput).val(),
//       " index",
//       index
//     );
//     $(spanLossLinks[index]).val(
//       eval(
//         originalLinkProperties.coefficient * $(spanLengthInput).val() +
//           originalLinkProperties.splice * originalLinkProperties.lossPerSplice +
//           originalLinkProperties.connector *
//             originalLinkProperties.lossPerConnector
//       ).toFixed(2)
//     );
//   });
// }

/************************************************************************
 *  Get user input for number of ILA's to be inserted
 ************************************************************************/

/*function getNumberOfIlaFromUser(linkModel,minNumOfIla)
{	
	var input;
	var div = $('<div id="UserInputIlaDiv" class="jumbotron">');
	var templateUserInputIla=`<form id="numOfIlaInputForm" class="form-inline">
		<div class="form-group text-center" >
		<label for="lid">Enter number of Ila required (Min :${minNumOfIla} and Max:7) :</label>
		<input  type="number" min="${minNumOfIla}" max="7" step="1" class="form-control" placeholder="40" id="numOfIla" value="${minNumOfIla}" name="numOfIla">
		</div><button class="btn btn-primary" type="button" onClick="getSpanlengthforIlaLinksFromUser(numOfIla,linkModel)" id="submitIlaUserInputFormBtn">Submit</button>
		</form>
		<button class="btn btn-danger" id="closeIlaUserInputFormBtn">Close</button>`;
	//console.log(template);
	$(div).append(templateUserInputIla);
	//$(document.body).append(div);

	var result=prompt("Enter number of Ila to be inserted (Min :"+minNumOfIla+" and Max:7)");
	if(result==null)
	{
		input=-1;//User presses cancel button
	}
	else 
	{
		if(result>=minNumOfIla && result<8)
		{
			//returns the number of ILA in range
			input=result;
		}	        		
		else 
		{
			//Wrong input
			//bootBoxAlert("Enter correct number for ILA insertion");
			alert("Enter correct number for ILA insertion");
			input=0;
		}
	}
     console.log("input ::"+input)
		return input;
}

$("body").delegate("#ilaLinksInputFormCancelBtn", "click", function() {
 $("#UserInputIlaDiv").remove();
})*/

/*$("body").delegate("#ilaLinksInputFormSubmitBtn", "click", function() {
	 var userInputForIlaLinks=$("#ilaLinksInputForm").serializeArray();
	 console.log(userInputForIlaLinks)
	 //var formInputJsonArr=input.parse();
	 
	 var userInputlengthSum=0;
	 var userInputlossSum=0;
	 console.log("userInputForIlaLinks.length ::"+userInputForIlaLinks.length )
	 for(var i=0;i<userInputForIlaLinks.length;i++)
		 {
		 console.log(userInputForIlaLinks[i].value);		 
		 userInputlengthSum= Number(userInputlengthSum) + Number(userInputForIlaLinks[i++].value);
		 console.log(userInputForIlaLinks[i].value);
		 userInputlossSum=Number(userInputlossSum) + Number(userInputForIlaLinks[i].value)
		 }
	 console.log("userInputlengthSum Value ::"+userInputlengthSum)
	 console.log("nptGlobals.IlaInsertion_SpanLength Value ::"+nptGlobals.IlaInsertion_SpanLength)
	 console.log("userInputlossSum Value ::"+userInputlossSum)
	 console.log("nptGlobals.IlaInsertion_SpanLoss Value ::"+nptGlobals.IlaInsertion_SpanLoss)
	 
	 if(userInputlengthSum==nptGlobals.IlaInsertion_SpanLength && userInputlossSum==nptGlobals.IlaInsertion_SpanLoss )
		 {
		    console.log("Insert ILA's");
			$("#UserInputIlaDiv").remove();
			$(dialog).modal('hide');
			InsertIlaBetweenNodes(userInputForIlaLinks);
		 }		 
	else
		{
		bootBoxAlert(`Total sum of spanLengths should be equal to <strong>${nptGlobals.IlaInsertion_SpanLength}</strong> and total sum of spanLosses should be equal to <strong>${nptGlobals.IlaInsertion_SpanLoss}</strong>`)
		} 
	 
	 
	})*/

// var spanLossInputFieldsIndexSet = new Set();

//spanloss of link for ILA insertion
// $("body").delegate(".spanLossIlaLinks", "input", function() {
//   console.log(".spanLengthIlaLinks");
//   var val = $(this).val();
//   var spanLossLimit = 35;
//   var spanLossInitial = 10;
//   var finalSpanLoss = val;
//   var spanLossSum = nptGlobals.IlaInsertion_SpanLoss;

//   console.log("Input value Spnaloss::" + val);
//   if (val > spanLossLimit) {
//     bootBoxAlert(
//       "Spanloss cannot be greater than " + spanLossLimit + " for a link"
//     );
//     //closeBootBoxAlert();
//     $(this).val(spanLossInitial);
//     finalSpanLoss = spanLossLimit;
//   } else {
//     var indexOfSpanLossInput = $(this)
//       .attr("name")
//       .split("_")[1];
//     console.log("Index:" + indexOfSpanLossInput);
//     spanLossInputFieldsIndexSet.add(indexOfSpanLossInput);

//     var spanLossInputs = $(".spanLossIlaLinks");
//     var numOfChangeFields = 0,
//       changeFieldsArr = [],
//       noChangeFieldsSum = 0;

//     // Span through all inputs for spanLoss and then find the inputs which are supposed to
//     // see the change (Not in set containing already changed value so that we do not change them again)
//     spanLossInputs.each(function(index) {
//       console.log("Element Index :" + index + " Value :" + $(this).val());
//       console.log(spanLossInputFieldsIndexSet);
//       console.log(
//         "Set me hai kYA? -- " + spanLossInputFieldsIndexSet.has($.trim(index))
//       );

//       if (spanLossInputFieldsIndexSet.has($.trim(index))) {
//         console.log("Ye wala pehle se hai");
//         noChangeFieldsSum += Number($(this).val()); // NO change fields
//       } else {
//         console.log("Ye wala pehle se nahi hai");
//         changeFieldsArr.push({ Index: index, Value: $(this) });
//         numOfChangeFields++;
//         //$(this).val(10);
//       }
//     });

//     console.log("numOfChangeFields:" + numOfChangeFields);
//     console.log("noChangeFieldsSum:" + noChangeFieldsSum);
//     console.log("spanLossSum:" + spanLossSum);
//     console.log("spanLossInputs.length:" + spanLossInputs.length);
//     console.log(changeFieldsArr);

//     var changeFieldsValue = eval(
//       (spanLossSum - noChangeFieldsSum) / numOfChangeFields
//     );

//     for (let el of changeFieldsArr) {
//       console.log(el);
//       el.Value.val(changeFieldsValue);
//     }

//     if (spanLossInputs.length == spanLossInputFieldsIndexSet.size) {
//       console.log("Clearing SpanLoss Set");
//       spanLossInputFieldsIndexSet.clear();
//     }
//   }
// });

/************************************************************************
 *  Get user input for spanlength of ILA links to be inserted
 ************************************************************************/
// function fIlaLinksInputFormSubmit() {
//   var userInputForIlaLinks,
//     spanLengthErroFlag = false;
//   if (
//     IlaModule.userChoice == IlaModule.choiceOne ||
//     IlaModule.userChoice == IlaModule.choiceTwo ||
//     IlaModule.userChoice == IlaModule.choiceThree
//   ) {
//     // userInputForIlaLinks = dataObjectIlaInsertion.SpanlengthArray;
//     userInputForIlaLinks = IlaModule.SpanlengthArray;
//   } else userInputForIlaLinks = $("#ilaLinksInputForm").serializeArray();

//   // Link Selected for ILA insertion
//   // let originalLink = nptGlobals.IlaInsertion_Link;
//   // originalLink.get("linkProperties").linkType = dataObjectIlaInsertion.linkType;

//   console.log("userInputForIlaLinks", userInputForIlaLinks);

//   var userInputlengthSum = 0;
//   var userInputlossSum = 0;
//   console.log("userInputForIlaLinks.length ::", userInputForIlaLinks.length);
//   let individualSpanlengthMaxLimit = 120;
//   let individualSpanlengthMinLimit = 10;

//   if (IlaModule.userChoice == IlaModule.choiceTwo) {
//     individualSpanlengthMinLimit = 10;
//     individualSpanlengthMaxLimit = 127;
//   } else if (IlaModule.userChoice == IlaModule.choiceThree) {
//     individualSpanlengthMinLimit = 10;
//     individualSpanlengthMaxLimit = 40;
//   }

//   let likSpanLenthError = "";
//   for (var i = 0; i < userInputForIlaLinks.length; i++) {
//     let spanLength = Number(userInputForIlaLinks[i++].value),
//       spanLoss = Number(userInputForIlaLinks[i].value);
//     console.log("spanLoss value from array:", spanLoss);
//     console.log("spanLength value from array:", spanLength);

//     // Individual Span Validation
//     if (
//       spanLength < individualSpanlengthMinLimit ||
//       spanLength > individualSpanlengthMaxLimit
//     ) {
//       spanLengthErroFlag = true;
//       likSpanLenthError = `Individual Span Length
// 			should be greater than <strong>${individualSpanlengthMinLimit}</strong> and less than
// 			<strong>${individualSpanlengthMaxLimit}</strong>`;
//       break;
//     }
//     //		 console.log(spanLength);
//     userInputlengthSum = Number(userInputlengthSum) + spanLength;
//     //		 console.log(spanLoss);
//     userInputlossSum = Number(userInputlossSum) + spanLoss;
//   }

//   console.log("userInputlengthSum Value ::", userInputlengthSum);
//   console.log("original spanLength Value ::", IlaModule.spanLength);
//   console.log("userInputlossSum Value ::", userInputlossSum);
//   console.log("original SPANLOSS Value ::", IlaModule.spanLoss);

//   userInputlossSum = Math.round(userInputlossSum * 100) / 100;
//   console.log(
//     "After rounding off - userInputlossSum Value ::" + userInputlossSum
//   );

//   //Check if spanlength values are in range
//   if (spanLengthErroFlag) {
//     // console.log(userInputForIlaLinks[0].value, " :: ", userInputForIlaLinks[2].value)
//     var template = `Individual Span Length
// 				should be greater than <strong>10</strong> and less than <strong>120</strong>`;
//     // var title = `<p class="text-danger">Invalid Input</p>`;

//     bootBoxDangerAlert(template);
//     getSpanlengthforIlaLinksFromUser();
//     // bootbox.dialog({
//     //   message: template,
//     //   title: title,
//     //   buttons: {
//     //     Ok: {
//     //       //label: "Success!",
//     //       className: "btn btn--default",
//     //       callback: function() {
//     //         getSpanlengthforIlaLinksFromUser(dataObjectIlaInsertion, linkModel);
//     //       }
//     //     }
//     //   }
//     // });
//   } else if (userInputlengthSum == IlaModule.spanLength) {
//     console.log("Insert ILA's");
//     bootBoxDangerAlert("Inserting Ila between nodes");
//     InsertIlaBetweenNodes(userInputForIlaLinks);
//   } else {
//     var template = `Total span-length
// 			should be equal to <strong>${IlaModule.spanLength}</strong>`;
//     // var title = `<p class="text-danger">Invalid Input</p>`;

//     bootBoxDangerAlert(template);
//     getSpanlengthforIlaLinksFromUser();

//     // bootbox.dialog({
//     //   message: template,
//     //   title: title,
//     //   buttons: {
//     //     Ok: {
//     //       //label: "Success!",
//     //       className: "btn btn--default",
//     //       callback: function() {
//     //         getSpanlengthforIlaLinksFromUser(dataObjectIlaInsertion, linkModel);
//     //       }
//     //     }
//     //   }
//     // });
//   }
// }

/************************************************************************
 * InsertIlaBetweenNodes() is to insert ILA after validation
 ************************************************************************/
// function InsertIlaBetweenNodes(userInputForIlaLinksArray) {
//   // let linkModel = nptGlobals.IlaInsertion_Link;
//   let linkModel = IlaModule.linkModel;
//   var ilaLinksArr = [];

//   let srcNodeId = linkModel.source().id,
//     destNodeId = linkModel.target().id;

//   let srcNode = graph.getCell(srcNodeId),
//     destNode = graph.getCell(destNodeId);

//   //Link removal will also update the node connections
//   //To preserve the previous connections on link removal
//   let srcInitialDir = linkModel.source().dir,
//     destInitialDir = linkModel.target().dir,
//     srcInitialNodeConnections = JSON.parse(
//       JSON.stringify(srcNode.get("nodeProperties").nodeConnections)
//     ),
//     destInitialNodeConnections = JSON.parse(
//       JSON.stringify(destNode.get("nodeProperties").nodeConnections)
//     );

//   console.log(
//     "srcInitialDir:",
//     srcInitialDir,
//     " destInitialDir:",
//     destInitialDir
//   );
//   console.log("Src Node Initial Connections", srcInitialNodeConnections);
//   console.log("Dest Node Initial Connections", destInitialNodeConnections);

//   console.log("Original Link before ILA insertion", linkModel);

//   console.log("srcNodeId :", srcNodeId, " destNodeId:", destNodeId);
//   //var isReversedLink=0;

//   var srcx, srcy, destx, desty, xDistance;
//   srcx = graph.getCell(srcNodeId).get("position").x;
//   srcy = graph.getCell(srcNodeId).get("position").y;
//   destx = graph.getCell(destNodeId).get("position").x;
//   desty = graph.getCell(destNodeId).get("position").y;
//   xDistance = eval("destx - srcx");
//   console.log("xDistance ::" + xDistance);

//   var userInputForIlaLinks = userInputForIlaLinksArray.filter(function(
//     el,
//     index
//   ) {
//     return el.name.includes("spanLength");
//   });

//   console.log(
//     "*********** userInputForIlaLinks Length **********",
//     userInputForIlaLinks.length,
//     userInputForIlaLinks
//   );
//   ilaLinksArr.push(linkModel.id);

//   //	let linkSubType=linkModel.get('linkProperties').linkType;
//   // let linkSubType = dataObjectIlaInsertion.ilaType;

//   for (var i = 0; i < userInputForIlaLinks.length - 1; i++) {
//     ilaLinksArr.pop();

//     console.log("numOfIla", Number(i + 1));

//     srcNodeId = linkModel.get("source").id;
//     destNodeId = linkModel.get("target").id;

//     var p = {};
//     let spanLengthSrcDest = IlaModule.spanLength;
//     var percentage = eval("userInputForIlaLinks[i].value/spanLengthSrcDest");
//     //console.log("percentage ::"+percentage)
//     var percentageLength = eval("xDistance * percentage");
//     //console.log("percentageLength ::"+percentageLength)
//     p.x = eval("srcx + percentageLength");
//     srcx = p.x;
//     //console.log("value of X coordinate ::"+p.x);
//     p.y = (srcy + desty) / 2;

//     //console.log("X and Y Coordinates --->"+p.x +"  "+p.y);
//     var ila = insertIlaNode(p);
//     ila.attributes.nodeProperties.nodeSubtype = IlaModule.ilaType;
//     console.log(
//       "Link type ::",
//       linkModel.get("linkProperties").linkType,
//       " Subtype of Ila::",
//       ila.get("nodeProperties").nodeSubtype
//     );

//     // var deletedLink = linkModel;
//     // if (i == 0) linkModel.attributes.linkProperties.autoDelete = true;
//     linkModel.target({ id: ila.id });
//     // debugger;
//     // linkModel.remove();
//     // delay(2000);
//     var firstLink = linkModel; //insertLink(srcNodeId, ila.id);
//     // debugger;
//     // firstLink.attributes.linkProperties.linkType =
//     //   dataObjectIlaInsertion.ilaType;

//     var firstLinkSrcNode = firstLink.getSourceElement();
//     var firstLinkDestNode = firstLink.getTargetElement();
//     console.log("firstLinkSrcNode :", firstLinkSrcNode);

//     // Update new connection link properties(Src and Dest Node Dir)
//     // fUpdateLinkConnectionProperties(firstLink);

//     /******  Keep the direction of link same for source node  *****/
//     if (i == 0) {
//       firstLink.get("target").dir = paperUtil.fGetNodeDegree(firstLinkDestNode);
//       firstLink.get("target").NodeId = firstLinkDestNode.get(
//         "nodeProperties"
//       ).nodeId;
//     } else {
//       firstLink.get("target").dir = paperUtil.fGetNodeDegree(firstLinkDestNode);
//       firstLink.get("target").NodeId = firstLinkDestNode.get(
//         "nodeProperties"
//       ).nodeId;

//       firstLink.get("source").dir = paperUtil.fGetNodeDegree(firstLinkSrcNode);
//       firstLink.get("source").NodeId = firstLinkSrcNode.get(
//         "nodeProperties"
//       ).nodeId;
//     }

//     firstLink.get("linkProperties").linkType = IlaModule.linkType;
//     ilaLinksArr.push(firstLink.id);

//     console.log("Loop :", i, " firstLink::", firstLink);
//     // console.log(
//     //   "firstLinkSrcNode::",
//     //   firstLinkSrcNode.attributes.nodeProperties.nodeConnections
//     // );
//     // console.log(
//     //   "firstLinkDestNode::",
//     //   firstLinkDestNode.attributes.nodeProperties.nodeConnections
//     // );

//     setNewLinkPropertiesWithIla(firstLink, userInputForIlaLinksArray, i);

//     var secondLink = insertLink(ila.id, destNodeId);

//     var secondLinkSrcNode = secondLink.getSourceElement();
//     var secondLinkDestNode = secondLink.getTargetElement();

//     // Update new connection link properties (Src and Dest Node Dir)
//     // fUpdateLinkConnectionProperties(secondLink);

//     secondLink.get("source").dir = paperUtil.fGetNodeDegree(secondLinkSrcNode);
//     secondLink.get("source").NodeId = secondLinkSrcNode.get(
//       "nodeProperties"
//     ).nodeId;

//     /******  Keep the direction of link same for dest node  *****/
//     if (i == userInputForIlaLinks.length - 2) {
//       secondLink.get("target").dir = destInitialDir;
//     } else
//       secondLink.get("target").dir = paperUtil.fGetNodeDegree(
//         secondLinkDestNode
//       );

//     secondLink.get("target").NodeId = secondLinkDestNode.get(
//       "nodeProperties"
//     ).nodeId;

//     secondLink.get("linkProperties").linkType = IlaModule.linkType;
//     ilaLinksArr.push(secondLink.id);

//     console.log("Loop :", i, " secondLink::", secondLink);
//     console.log(
//       "secondLinkSrcNode::",
//       secondLinkSrcNode.attributes.nodeProperties.nodeConnections
//     );
//     console.log(
//       "secondLinkDestNode::",
//       secondLinkDestNode.attributes.nodeProperties.nodeConnections
//     );

//     setNewLinkPropertiesWithIla(secondLink, userInputForIlaLinksArray, i + 1);

//     //Ila insertion in case of Line Protected link
//     var isLineProtected = IlaModule.linkModel.get("linkProperties")
//       .lineProtection;
//     if (isLineProtected == 1) {
//       //firstLink.attr(attrs.linkDefaultLinePtc);
//       firstLink.get("linkProperties").lineProtection = 1;
//       //secondLink.attr(attrs.linkDefaultLinePtc);
//       secondLink.get("linkProperties").lineProtection = 1;
//     }

//     linkModel = secondLink;
//     console.log("Second Link linkModel::", linkModel);
//   }

//   console.log("ilaLinksArr", ilaLinksArr);

//   setallNodeAttributes();
//   // setallLinkAttributes();
//   setAllIlaLinksProperties(ilaLinksArr);

//   if (window.ilaInsertionFromLinkConfig) {
//     updateLinkNodeConfigTab();
//   }
// }

/************************************************************************
 * Find key based on value in json object
 ************************************************************************/
function findKey(obj, value) {
  _.findKey(obj, function(v) {
    return v == value;
  });
}

function delay(time) {
  setTimeout(function() {
    console.log("Delayed for " + time / 1000 + " seconds.");
  }, time);
}

// var setAllIlaLinksProperties = function(ilaLinksArr, linkUserInput) {
//   for (var i = 0; i < ilaLinksArr.length; i++) {
//     let link = graph.getCell(ilaLinksArr[i]);
//     console.log(link);
//     console.log("Color for Ila Link :" + linkUserInput.color, " i :", i);
//     setLinkColor(link, linkUserInput.color);
//     //set attributes of object/element/link properties defined
//     //link.attributes.linkProperties.linkId = id;
//     //link.attributes.linkProperties.spanLength = linkUserInput.spanlength;
//     link.attributes.linkProperties.fiberType = linkUserInput.fibertype;

//     if (link.attributes.linkProperties.fiberType == 1) {
//       /**G.652.D*/
//       console.log("pmdCoff :" + linkUserInput.pmdCoefficient);
//       console.log("CdCoff :" + linkUserInput.cdCoefficient);
//       console.log("New SpanLength :" + link.get("linkProperties").spanLength);
//       var cdValue = eval(
//         linkUserInput.cdCoefficient * link.get("linkProperties").spanLength
//       ).toFixed(2);
//       var pmdValue = eval(
//         linkUserInput.pmdCoefficient *
//           Math.sqrt(link.get("linkProperties").spanLength)
//       ).toFixed(2);
//       console.log("cdValue : " + cdValue + " and pmdValue : " + pmdValue);
//       link.attributes.linkProperties.pmd = pmdValue;
//       link.attributes.linkProperties.cd = cdValue;
//     } else if (link.attributes.linkProperties.fiberType == 2) {
//       /**G.655*/
//       console.log("pmdCoff :" + linkUserInput.pmdCoefficient);
//       console.log("SpanLength :" + nptGlobals.IlaInsertion_SpanLength);
//       console.log("New SpanLength :" + link.get("linkProperties").spanLength);
//       var cdValue = eval(
//         linkUserInput.cdCoefficient * link.get("linkProperties").spanLength
//       ).toFixed(2);
//       var pmdValue = eval(
//         linkUserInput.pmdCoefficient *
//           Math.sqrt(link.get("linkProperties").spanLength)
//       ).toFixed(2);
//       console.log("cdValue : " + cdValue + " and pmdValue : " + pmdValue);
//       link.attributes.linkProperties.pmd = pmdValue;
//       link.attributes.linkProperties.cd = cdValue;
//     }

//     //link.attributes.linkProperties.splice = linkUserInput.splice;
//     //link.attributes.linkProperties.lossPerSplice = linkUserInput.lossPerSplice;
//     //link.attributes.linkProperties.connector = linkUserInput.connector;
//     //link.attributes.linkProperties.lossPerConnector = linkUserInput.lossPerConnector;
//     //link.attributes.linkProperties.calculatedSpanLoss = linkUserInput.calculatedSpanLoss;
//     //link.attributes.linkProperties.adjustableSpanLoss = linkUserInput.adjustableSpanLoss;
//     link.attributes.linkProperties.costMetric = linkUserInput.costmetric;
//     //link.attributes.linkProperties.opticalParameter = linkUserInput.opticalparameter;
//     link.attributes.linkProperties.srlg = linkUserInput.srlg;
//     link.attributes.linkProperties.color = linkUserInput.color;
//     //cell.attr('attributes/stationName',stationName);

//     //Update link Info Tab
//     linkInfoTab();
//   }
//   setallLinkAttributes();
// };

/************************************************************************
 * * This function is used to join links to ports ; Otherwise they get
 * connected to the node when a new link is created using insertLink function
 ************************************************************************/
var setallNodeAttributes = function() {
  _.each(graph.getElements(), function(node) {
    if (node.get("type") == nptGlobals.NodeTypeILA) {
      //selecting the last ila node to set ports and middle label
      // node.attr(attrs.elementSelected);
      let subtype = node.get("nodeProperties").nodeSubtype;
      console.log("Subtype of ILA ::", subtype);

      switch (subtype) {
        case nptGlobals.getRamanDraLinkStr():
          node.attr(attrs.IlaRamanDra);
          node.attr(".app-cell__title/text", "RAMAN-D");
          break;
        case nptGlobals.getRamanHybridLinkStr():
          node.attr(attrs.IlaRamanHybrid);
          node.attr(".app-cell__title/text", "RAMAN-H");
          break;
        default:
          node.attr(attrs.IlaDefault);
          node.attr(".app-cell__title/text", "ILA");
          break;
      }
    } else if (
      node.get("type") == nptGlobals.NodeTypeROADM ||
      node.get("type") == nptGlobals.NodeTypeTwoDegreeRoadm ||
      node.get("type") == nptGlobals.NodeTypeHub
    ) {
      //selecting the last ila node to set ports and middle label
      node.attr(attrs.elementSelected);
      node.attr(attrs.elementDefault);
    } else if (
      node.get("type") == nptGlobals.NodeTypeTE ||
      node.get("type") == nptGlobals.NodeTypeCDROADM
    ) {
      //selecting the last ila node to set ports and middle label
      node.attr(attrs.elementSelected);
      node.attr(attrs.TeDefault);
    }

    node.attr(".label/text", /*"Ila"+*/ node.get("nodeProperties").nodeId);
  });
};

/************************************************************************
 * This function is used to join links to ports ; Otherwise they get
 * connected to the node when a new link is created using insertLink function
 ************************************************************************/
var setallLinkAttributes = function() {
  _.each(graph.getLinks(), function(link) {
    if (link.get("linkProperties").lineProtection == 0) {
      link.attr(attrs.linkHighlighted);
      link.attr(attrs.linkDefault);
      setLinkColorFromLinkProperties(link);
    } else {
      //selecting the last ila link to set ports and middle label
      link.attr(attrs.linkHighlighted);
      link.attr(attrs.linkDefaultLinePtc);
    }
  });
};

/************************************************************************
 * Bring link to front
 ************************************************************************/

var fLinktoFront = function() {
  _.each(graph.getLinks(), function(link) {
    link.toFront();
  });
};

/************************************************************************
 * Show Info box for Path and different nodes
 ************************************************************************/
function fShowPathInfoBox(flag) {
  // if (flag == true) {
  // 	var div = `<div id="pathInfoBox"><ul>
  // 	 <li><img src="../images/check-box-empty-red.svg">  Source and Destination Node</li>
  // 	 <li><img src="../images/check-box-empty -blue.svg">  Intermediate Path Node</li>
  // 	 <li><img src="../images/check-box-empty -green.svg">  Regenerator Node</li>
  // 	 </ul></div>`;
  // 	$(div).hide().appendTo(document.body).fadeIn(300, "linear");
  // }
  // else {
  // 	$("#pathInfoBox").fadeOut(300, "linear", function () { $(this).remove(); });
  // }
}

/************************************************************************
 *Make canvas tab active
 ************************************************************************/
function canvasActive() {
  $("#menuDivInsideCanvas .nav li").removeClass("active");
  $("#menuDivInsideCanvas .nav li:first-child").addClass("active");
  $("#canvasIdContainer .tab-content > div").removeClass("active");
  $("#canvasIdContainer .tab-content #canvasId").addClass("active");
  nptGlobals.setCanvasActive(_SET_VALUE);
  console.log(nptGlobals.getCanvasActive());
}

/************************************************************************
 * Set New Link Properties With Ila
 ************************************************************************/
/*function setNewLinkPropertiesWithIla(linkModel,spanLengthValue,i) */
// function setNewLinkPropertiesWithIla(linkModel, userInputForIlaLinksArray, i) {
//   console.log("setNewLinkPropertiesWithIla linkModel ::", linkModel);

//   let src = graph.getCell(linkModel.attributes.source.id),
//     dest = graph.getCell(linkModel.attributes.target.id);

//   console.log(
//     "userInputForIlaLinksArray[(i*2)].value ::" +
//       userInputForIlaLinksArray[i * 2].value
//   );
//   console.log(
//     "userInputForIlaLinksArray[((i*2)+1)].value; ::" +
//       userInputForIlaLinksArray[i * 2 + 1].value
//   );

//   linkModel.get("linkProperties").spanLength =
//     userInputForIlaLinksArray[i * 2].value;
//   linkModel.get("linkProperties").calculatedSpanLoss =
//     userInputForIlaLinksArray[i * 2 + 1].value;
//   linkModel.get("linkProperties").adjustableSpanLoss =
//     userInputForIlaLinksArray[i * 2 + 1].value;
//   linkModel.get("linkProperties").fiberType = window.linkUserInput.fibertype;
//   linkModel.get("linkProperties").lossPerSplice =
//     window.linkUserInput.lossPerSplice;
//   linkModel.get("linkProperties").splice = window.linkUserInput.splice;
//   linkModel.get("linkProperties").connector = window.linkUserInput.connector;
//   linkModel.get("linkProperties").lossPerConnector =
//     window.linkUserInput.lossPerConnector;
//   linkModel.get("linkProperties").costMetric = window.linkUserInput.costMetric;
//   linkModel.get("linkProperties").srlg = window.linkUserInput.srlg;
//   linkModel.get("linkProperties").color = window.linkUserInput.color;
//   linkModel.get("linkProperties").opticalParameter =
//     window.linkUserInput.opticalParameter;
//   // linkModel.get("linkProperties").linkType = window.linkUserInput.linkType;
//   // linkModel.get('linkProperties').isIlaLink=1;
//   //linkModel.attr(attrs.IlalinkDefault);
//   //console.log(linkModel);
// }

/************************************************************************
 * Function to get the template for setting link properties form
 ************************************************************************/
function getLinkTemplate(selected) {
  //console.log(selected);
  var src = graph.getCell(selected.attributes.source.id),
    dest = graph.getCell(selected.attributes.target.id);

  let srcDestDirFlag = true,
    linkTypeFlag = true,
    linkType = selected.get("linkProperties").linkType,
    color = selected.get("linkProperties").color;
  console.log("******Color***** ", color);
  if (
    src.attributes.type.indexOf("Ila") > 0 ||
    dest.attributes.type.indexOf("Ila") > 0
  ) {
    srcDestDirFlag = true;
    linkTypeFlag = false;
  }

  let srcDir = selected.get("linkProperties").SrcNodeDirection,
    destDir = selected.get("linkProperties").DestNodeDirection;

  console.log(
    "Link template flags ::   dir flag --> ",
    srcDestDirFlag,
    " type flag --> ",
    linkTypeFlag
  );
  console.log(src.attributes.type, " <---> ", dest.attributes.type);
  //console.log(selected.attributes.attrs.connection.stroke);
  /**
     * Optical Param Removed/Replaced by spanloss
     * <div class="form-group">
		<label for="opticalParameter">Optical Parameter:</label>
		<input type="text" class="form-control" id="opticalParameter" value="${selected.attributes.linkProperties.opticalParameter}">
		</div>
     */
  var template = `<form id="propertyFormLink" >
	    <div class="form-group" >
		<input name="cid" type="hidden" class="form-control" id="cid" value="${
      selected.cid
    }"> 
		</div>
		<div class="form-group" >
		<!-- <label for="lid">Link Id:</label> -->
		<input name="linkId" type="hidden" class="form-control" id="lid" value="${
      selected.attributes.linkProperties.linkId
    }" readonly>
		</div>
		<div class="form-group">
		<!-- <label for="linkNode1">Source Node:</label> -->
		<input type="hidden" class="form-control" id="linkNode1" placeholder="Link Source Node" value="${
      src.attributes.nodeProperties.nodeId
    }" readonly>
		</div>
		<div class="form-group">
		<!-- <label for="linkNode2">Destination Node:</label> -->
		<input type="hidden" class="form-control" id="linkNode2" placeholder="Link Destination Node" value="${
      dest.attributes.nodeProperties.nodeId
    }" readonly>
		</div>
		<div class="form-group">
		<label for="linkType">Link Type:</label>		
		${
      linkTypeFlag
        ? '<select class="form-control" id="linkType">'
        : '<select class="form-control" id="linkType" disabled>'
    }
			${
        linkType == nptGlobals.getDefaultLinkStr()
          ? "<option selected>" + nptGlobals.getDefaultLinkStr() + "</option>"
          : "<option >" + nptGlobals.getDefaultLinkStr() + "</option>"
      }
			${
        linkType == nptGlobals.getRamanHybridLinkStr()
          ? "<option selected>" +
            nptGlobals.getRamanHybridLinkStr() +
            "</option>"
          : "<option >" + nptGlobals.getRamanHybridLinkStr() + "</option>"
      }	
			${
        linkType == nptGlobals.getRamanDraLinkStr()
          ? "<option selected>" + nptGlobals.getRamanDraLinkStr() + "</option>"
          : "<option >" + nptGlobals.getRamanDraLinkStr() + "</option>"
      }	
			<!-- <option >${nptGlobals.getDefaultLinkStr()}</option>
			<option >${nptGlobals.getRamanHybridLinkStr()}</option>
			<option >${nptGlobals.getRamanDraLinkStr()}</option> -->
		</select>
		</div>
    	<div class="form-group">
		<label for="fiberType">Fiber Type:</label>		
		<select class="form-control" id="fiberType">
		<option value="1">G.652.D</option>
		<option value="2">G.655</option>
		</select>
		</div>
		<div class="form-group">
		<label for="cdCoff">Chromatic Dispersion(CD) Coefficient (ps/nm.km):</label>
		<input  type="number" min="0" max="100" step="10" class="form-control" placeholder="20" id="cdCoefficient" value="${
      selected.attributes.linkProperties.cdCoefficient
    }">
		</div>
		<div class="form-group">
		<label for="cd">Chromatic Dispersion(CD) (ps/nm):</label>
		<input  type="number" min="0" max="100" step="10" class="form-control" placeholder="20" id="cd" value="${
      selected.attributes.linkProperties.cd
    }">
		</div>
		<div class="form-group">
		<label for="pmdCoff">Polarization Mode Dispersion(PMD) Coefficient (ps/sqrt(km)):</label>
		<input  type="number" min="0" max="100" step="10" class="form-control" placeholder="20" id="pmdCoefficient" value="${
      selected.attributes.linkProperties.pmdCoefficient
    }">
		</div>
		<div class="form-group">
		<label for="pmd">Polarization Mode Dispersion(PMD) (ps):</label>
		<input  type="number" min="0" max="100" step="10" class="form-control" placeholder="20" id="pmd" value="${
      selected.attributes.linkProperties.pmd
    }">
		</div>
		<div class="form-group">
		<label for="spanLength">Span Length(Km):</label>
		<input  type="number" min="0" max="500" step="10" class="form-control" placeholder="20" id="spanLength" value="${
      selected.attributes.linkProperties.spanLength
    }">
		</div>
		<div class="form-group">
		<label for="splice">No. of Splices:</label>
		<input  type="number"  min="1" max="50" step="1" class="form-control" placeholder="1" id="splice" value="${
      selected.attributes.linkProperties.splice
    }">
		</div>
		<div class="form-group">
		<label for="lossPerSplice">Per Splice Loss(DB):</label>
		<input  type="number"  min="0.1" max="1" step="0.1" class="form-control" placeholder="1" id="lossPerSplice" value="${
      selected.attributes.linkProperties.lossPerSplice
    }">
		</div>
		<div class="form-group">
		<label for="connector">No. of Connectors(System one Excluded):</label>
		<input  type="number"  min="1" max="10" step="1" class="form-control" placeholder="1" id="connector" value="${
      selected.attributes.linkProperties.connector
    }">
		</div>
		<div class="form-group">
		<label for="lossPerConnector">Per Connector Loss(DB):</label>
		<input  type="number" min="0.1" max="1" step="0.1" class="form-control" placeholder="1" id="lossPerConnector" value="${
      selected.attributes.linkProperties.lossPerConnector
    }">
		</div>
		<div class="form-group">
		<label for="lossPerConnector">Coefficient:</label>
		<input  type="number" min="0.1" max="1" step="0.1" class="form-control" placeholder="1" id="coefficient" value="${
      selected.attributes.linkProperties.coefficient
    }">
		</div>
		<div class="form-group">
    	<form>
   		<label for="spanLoss">Calculated Span Loss(DB):</label>   		
    		<input type="number" min="0" class="form-control" id="calculatedSpanLoss" value="${
          selected.attributes.linkProperties.calculatedSpanLoss
        }">
		</form>
		</div>
		<div class="form-group">
    	<form>
   		<label for="spanLoss">Adjustable Span Loss(DB) [Min: 20, Max: 35]:</label>   		
    		<input type="number" min="13" max="37" step="1" class="form-control" id="adjustedSpanLoss" value="${
          selected.attributes.linkProperties.adjustableSpanLoss
        }" readonly>
		</form>
		</div>		
		<div class="form-group">
		<label for="costMetric">Cost Metric(INR):</label>
		<input type="text" class="form-control" id="costMetric" value="${
      selected.attributes.linkProperties.costMetric
    }" >
		</div>		
		<div class="form-group">
		<label for="color">Color:</label>
		<select class="form-control" id="color" ">
			${
        color == 0
          ? '<option value="0" selected>Default</option>'
          : '<option value="0">Default</option>'
      }
			${
        color == 1
          ? '<option value="1" selected>Violet</option>'
          : '<option value="1">Violet</option>'
      }
			${
        color == 2
          ? '<option value="2" selected>Indigo</option>'
          : '<option value="2">Indigo</option>'
      }
			${
        color == 3
          ? '<option value="3" selected>Blue</option>'
          : '<option value="3">Blue</option>'
      }
			${
        color == 4
          ? '<option value="4" selected>Green</option>'
          : '<option value="4">Green</option>'
      }
			${
        color == 5
          ? '<option value="5" selected>Yellow</option>'
          : '<option value="5">Yellow</option>'
      }
			${
        color == 6
          ? '<option value="6" selected>Orange</option>'
          : '<option value="6">Orange</option>'
      }
			${
        color == 7
          ? '<option value="7" selected>Red</option>'
          : '<option value="7">Red</option>'
      }
			<!-- <option value="0">Default</option>
			<option value="1">Violet</option>
			<option value="2">Indigo</option>
			<option value="3">Blue</option>
			<option value="4">Green</option>
			<option value="5">Yellow</option>
			<option value="6">Orange</option>
			<option value="7">Red</option> -->
		</select>
		</div>
		<div class="form-group">
		<label for="srlg">SRLG:</label>
		<input type="number" min="0" max=""10" step="1" class="form-control" id="srlg" value="${
      selected.attributes.linkProperties.srlg
    }">
		</div>	
		<div class="form-group">
		<label for="srcDirection">Source Node Direction:</label>	
			${
        srcDestDirFlag
          ? '<select class="form-control" id="srcDirection">'
          : '<select class="form-control" id="srcDirection" disabled>'
      }	
				${srcDir == 1 ? "<option selected>1</option>" : "<option >1</option>"}		
				${srcDir == 2 ? "<option selected>2</option>" : "<option >2</option>"}		
				${srcDir == 3 ? "<option selected>3</option>" : "<option >3</option>"}		
				${srcDir == 4 ? "<option selected>4</option>" : "<option >4</option>"}		
				${srcDir == 5 ? "<option selected>5</option>" : "<option >5</option>"}		
				${srcDir == 6 ? "<option selected>6</option>" : "<option >6</option>"}		
				${srcDir == 7 ? "<option selected>7</option>" : "<option >7</option>"}		
				${srcDir == 8 ? "<option selected>8</option>" : "<option >8</option>"}		
			</select>
		</div>
		<div class="form-group">
		<label for="destDirection">Target Node Direction:</label>		
			${
        srcDestDirFlag
          ? '<select class="form-control" id="destDirection">'
          : '<select class="form-control" id="destDirection" disabled>'
      }
				${destDir == 1 ? "<option selected>1</option>" : "<option >1</option>"}		
				${destDir == 2 ? "<option selected>2</option>" : "<option >2</option>"}		
				${destDir == 3 ? "<option selected>3</option>" : "<option >3</option>"}		
				${destDir == 4 ? "<option selected>4</option>" : "<option >4</option>"}		
				${destDir == 5 ? "<option selected>5</option>" : "<option >5</option>"}		
				${destDir == 6 ? "<option selected>6</option>" : "<option >6</option>"}		
				${destDir == 7 ? "<option selected>7</option>" : "<option >7</option>"}		
				${destDir == 8 ? "<option selected>8</option>" : "<option >8</option>"}		
			</select>
		</div>
		</form>`;

  //  console.log(template);

  /*
      <select class="form-control" id="color" onChange="setLinkColor(nptGlobals.elSelected,this)">
      template+=`" >
    	</div>
    	<div class="form-group form-inline">
    	<label for="color">Color:</label>
    	<input type="color" class="form-control" id="color" value="`;
    template+=selected.attr('.connection/stroke');
    <option  value="Indigo">Indigo</option>
		<option value="Blue">Blue</option>
		<option  value="Green">Green</option>
		<option value="Yellow">Yellow</option>
		<option  value="Orange">Orange</option>
		<option  value="Red" selected>Red</option>
		onChange="setLinkColor(nptGlobals.elSelected,this)"*/

  return template;
}

/************************************************************************
 * Function to get the template for setting node properties form
 ************************************************************************/
function getElementTemplate(selectedNode) {
  console.log("getElementTemplate() called!!");
  var template = `<form id="propertyFormNode" >
	    <div class="form-group" >
		<input name="cid" type="hidden" class="form-control" id="cid" value="${
      selectedNode.cid
    }"> 
		</div>
		<div class="form-group" >
		<label for="eid">Node Id:</label>
		<input name="NodeId" type="text" class="form-control" id="eid" value="${
      selectedNode.attributes.nodeProperties.nodeId
    }" readonly >
		</div>
		<div class="form-group">
		<label for="nodeType">Node Type:</label>
		<input name="NodeType" type="text" class="form-control" id="nodeType" value="${
      selectedNode.get("nodeProperties").displayName
    }" readonly>
		</div>
		<div class="form-group">
		<label for="siteName">Site Name:</label>
		<input name="SiteName" type="text" class="form-control" id="siteName" value="${
      selectedNode.attributes.nodeProperties.siteName
    }">
		</div>
		<div class="form-group">
		<label for="stationName">Station Name:</label>
		<input name="StationName" type="text" class="form-control" id="stationName" value="${
      selectedNode.attributes.nodeProperties.stationName
    }">
		</div>
		<div class="form-group">
		<label for="gneFlag">GNE Flag(For EMS Connectivity):</label>
		<select name="GneFlag" class="form-control" id="gneFlag" >
		<option value="0" selected>No</option>
		<option value="1">Primary GNE</option>
		<option value="2">Secondary GNE</option>
		</select>
		</div>
		<div class="form-group">
		<label for="vlanTag">VLAN Tag(For In-Band EMS Communication):</label>
		<input name="vlanTag" type="text" class="form-control" id="vlanTag" value="${
      selectedNode.attributes.nodeProperties.vlanTag
    }">
		</div>
		<div class="form-group">
		<label for="emsip">EMS-IP</label>
		<input name="emsip" type="text" class="form-control" id="emsip" value="${
      selectedNode.attributes.nodeProperties.emsip
    }" >
		</div>
		<div class="form-group">
		<label for="subnet">Subnet</label>
		<input name="subnet" type="text" class="form-control" id="subnet" value="${
      selectedNode.attributes.nodeProperties.subnet
    }" >
		</div>
		<div class="form-group">
		<label for="gateway">Gateway</label>
		<input name="gateway" type="text" class="form-control" id="gateway" value="${
      selectedNode.attributes.nodeProperties.gateway
    }" >
		</div>
		<div class="form-group">
		<label for="ipv6">IPv6</label>
		<input name="ipv6" type="text" class="form-control" id="ipv6" value="${
      selectedNode.attributes.nodeProperties.ipv6
    }" >
		</div>
		<div class="form-group">
		<label for="Latitude">Latitude</label>
		<input name="lat" type="text" class="form-control" id="lat" value="${
      selectedNode.attributes.nodeProperties.nodeLat
    }" >
		</div>
		<div class="form-group">
		<label for="lng ">Longitude</label>
		<input name="lng" type="text" class="form-control" id="lng" value="${
      selectedNode.attributes.nodeProperties.nodeLng
    }" >
		</div>
		<div class="form-group">
		<label for="capacity">Capacity</label>
		<select name="capacity" class="form-control" id="capacity" value="${
      selectedNode.attributes.nodeProperties.capacity
    }">
		<option value="1">Even 40 Channel</option>
	    <option value="2">Odd 40 Channel</option>
		<option value="3">80 Channel</option>
		</select>
		</div>`;

  /**Direction Commented for tar solve*/
  /*<div class="form-group">
	<label for="direction">Direction</label>
	<select name="direction" class="form-control" id="direction">
	<option value="East" selected>East</option>
	<option  value="West">West</option>
	</select>
	</div>`;
	<div class="form-group">
	<label for="degree">Degree:</label>
	<select class="form-control" id="degree">
	<option value="1" selected>1</option>
	<option  value="2">2</option>
	<option value="3">3</option>
	<option  value="4">4</option>
	<option value="5">5</option>
	<option  value="6">6</option>
	<option value="7">7</option>
	<option  value="8">8</option>
	</select>
	</div>
	*/
  template += `</form>`;
  // console.log("template for nodeProp"+template);
  return template;
}

function setNodeProperties() {
  //store values of user input in variable
  var id = $("#eid").val(),
    cid = $("#cid").val(),
    sitename = $("#siteName").val(),
    stationname = $("#stationName").val(),
    gneflag = $("#gneFlag").val(),
    vlantag = $("#vlanTag").val(),
    emsip = $("#emsip").val(),
    capacity = $("#capacity").val(),
    direction = $("#direction").val(),
    ipv6 = $("#ipv6").val(),
    subnet = $("#subnet").val(),
    gateway = $("#gateway").val(),
    latitude = $("#lat").val(),
    longitude = $("#lng").val();
  //opticalreach = $("#opticalreach").val();
  //degree = $("#degree").val();
  console.log(id, sitename, stationname);

  var nodeUserInput = {
    id: id,
    cid: cid,
    sitename: sitename,
    stationname: stationname,
    gneflag: gneflag,
    vlantag: vlantag
  };
  console.log(nodeUserInput);
  //get cell using id
  var cell = graph.getCell(cid);
  //console.log("station:"+stationName);
  //console.log(cell);

  //	var isNodePropertiesValid = validateNodeProperties(cell, nodeUserInput);

  if (validateNodeProperties(cell, nodeUserInput)) {
    //set attributes of object/element/link properties defined
    cell.attributes.nodeProperties.nodeId = id;
    cell.attributes.nodeProperties.stationName = stationname;
    cell.attributes.nodeProperties.siteName = sitename;
    cell.attributes.nodeProperties.gneFlag = gneflag;
    cell.attributes.nodeProperties.vlanTag = vlantag;
    cell.attributes.nodeProperties.emsip = emsip;
    cell.attributes.nodeProperties.capacity = capacity;
    cell.attributes.nodeProperties.direction = direction;
    cell.attributes.nodeProperties.ipv6 = ipv6;
    cell.attributes.nodeProperties.subnet = subnet;
    cell.attributes.nodeProperties.gateway = gateway;
    cell.attributes.nodeProperties.nodeLat = latitude;
    cell.attributes.nodeProperties.nodeLng = longitude;
    GisMap.setlocation(cell);
    // GisMap.mapZoomEvent();
    //cell.attributes.nodeProperties.opticalReach = opticalreach;
    //cell.attributes.nodeProperties.degree = degree;
    //	cell.attr('attributes/siteName',siteName);
    //	cell.attr('attributes/stationName',stationName);
    //console.log(cell.attr('attributes/siteName'));
    //console.log(cell.attr('attributes/stationName'));
    //console.log('Station ka Nam'+cell.attributes.stationName);

    bootBoxAlert("Node Properties saved successfully");
    //Update Node Tab
    nodeInfoTab();
  } else {
    fInitiateLinkNodePropertyDialog(cell);
    console.log("Node Properties cannot be set");
  }
  //alert("Changes saved");
}

function setLinkProperties() {
  let linkPropertiesFormData = fGetLinkPropertiesFormInput();
  // IlaModule.linkUserInput = linkPropertiesFormData;
  let linkUserInput = JSON.parse(JSON.stringify(linkPropertiesFormData));

  //get cell using id
  var linkModel = graph.getCell(linkPropertiesFormData.cid);
  console.log("setLinkProperties:", linkModel);
  let prevLinkType = linkModel.attributes.linkProperties.linkType;

  if (validateLinkProperties(linkModel, linkUserInput)) {
    setLinkColor(linkModel, linkPropertiesFormData.color);

    // If Changed from Raman type to default
    if (
      linkPropertiesFormData.linkType == nptGlobals.getDefaultLinkStr() &&
      linkModel.get("linkProperties").linkType != nptGlobals.getDefaultLinkStr()
    ) {
      bootBoxAlert("Link Changed from Raman to default");
      fUpdateRamanLinksOnDelete(linkModel.getSourceElement());
      fUpdateRamanLinksOnDelete(linkModel.getTargetElement());
      fUpdateLinkConnectionProperties(linkModel);
    }

    // save form properties
    _.merge(linkModel.attributes.linkProperties, linkPropertiesFormData);

    setOpticalReachAsPerSpanLength(linkModel.get("id"));

    //Update link Info Tab
    linkInfoTab();
    setallLinkAttributes();
    // If changed to RAMAN link ; updated link model
    if (
      prevLinkType == nptGlobals.getDefaultLinkStr() &&
      linkModel.attributes.linkProperties.linkType !=
        nptGlobals.getDefaultLinkStr()
    ) {
      convertLinkType(linkModel);
    }

    bootBoxAlert("Link Properties saved successfully");
    return true;
  } else {
    return false;
    // fInitiateLinkNodePropertyDialog(linkModel);
  }
}

var convertLinkType = linkModel => {
  console.log(
    "convertLinkType():",
    linkModel.attributes.linkProperties.linkType
  );

  console.log("Update Direction");
  let srcNode = linkModel.getSourceElement(),
    destNode = linkModel.getTargetElement();

  srcNode.get("nodeProperties").ramanLinks += 1;
  destNode.get("nodeProperties").ramanLinks += 1;

  let srcNodeRamanLinks = srcNode.get("nodeProperties").ramanLinks,
    destNodeRamanLinks = destNode.get("nodeProperties").ramanLinks;

  console.log(
    "srcNodeRamanLinks:",
    srcNodeRamanLinks,
    "destNodeRamanLinks:",
    destNodeRamanLinks
  );

  let initialSrcDir = linkModel.source().dir;
  let initialDestDir = linkModel.target().dir;

  // linkModel.get("source").dir = 6 + srcNodeRamanLinks;
  // linkModel.get("target").dir = 6 + destNodeRamanLinks;

  if (srcNode.get("type") != nptGlobals.NodeTypeILA) {
    linkModel.get("source").dir = 6 + srcNodeRamanLinks;
    fUpdateConnectedLinksOnChange(srcNode, initialSrcDir);
  }
  if (destNode.get("type") != nptGlobals.NodeTypeILA) {
    linkModel.get("target").dir = 6 + destNodeRamanLinks;
    fUpdateConnectedLinksOnChange(destNode, initialDestDir);
  }
};

function fGetLinkPropertiesFormInput() {
  var id = $("#lid").val(),
    cid = $("#cid").val(),
    fibertype = $("#fiberType").val(),
    cd = $("#cd").val(),
    pmd = $("#pmd").val(),
    cdCoefficient = $("#cdCoefficient").val(),
    pmdCoefficient = $("#pmdCoefficient").val(),
    splice = $("#splice").val(),
    lossPerSplice = $("#lossPerSplice").val(),
    connector = $("#connector").val(),
    lossPerConnector = $("#lossPerConnector").val(),
    coefficient = $("#coefficient").val(),
    calculatedSpanLoss = $("#calculatedSpanLoss").val(),
    adjustableSpanLoss = $("#adjustedSpanLoss").val(),
    spanlength = $("#spanLength").val(),
    costmetric = $("#costMetric").val(),
    opticalparameter = $("#opticalParameter").val(),
    color = $("#color").val(),
    srlg = $("#srlg").val(),
    linkType = $("#linkType").val();
  srcDirection = $("#srcDirection").val();
  destDirection = $("#destDirection").val();

  console.log(color, spanlength, srlg);

  let linkUserInput = {
    linkId: id,
    cid: cid,
    fiberType: fibertype,
    cdCoefficient: cdCoefficient,
    pmdCoefficient: pmdCoefficient,
    cd: cd,
    pmd: pmd,
    spanLength: spanlength,
    splice: splice,
    lossPerSplice: lossPerSplice,
    connector: connector,
    lossPerConnector: lossPerConnector,
    coefficient: coefficient,
    calculatedSpanLoss: calculatedSpanLoss,
    adjustableSpanLoss: adjustableSpanLoss,
    costMetric: costmetric,
    opticalParameter: opticalparameter,
    color: color,
    srlg: srlg,
    linkType: linkType,
    SrcNodeDirection: Number(srcDirection),
    DestNodeDirection: Number(destDirection)
  };
  return linkUserInput;
}

/************************************************************************
 * Function to set Link Color From LinkProperties COLOR VALUE
 ************************************************************************/
function setLinkColorFromLinkProperties(el) {
  colorOfLink = el.get("linkProperties").color;
  el.attr(".connection/stroke", getColorFromCode(colorOfLink));
}

/************************************************************************
 * Function to set Link Direction From LinkProperties
 ************************************************************************/
function setLinkSrcDestDirection(link, srcD, destD) {
  console.log("*********setLinkSrcDestDirection**********");
  console.log("Src D", srcD, " Dest D:", destD);

  let linkCurrentSrcDir = link.get("linkProperties").SrcNodeDirection,
    linkCurrentDestDir = link.get("linkProperties").DestNodeDirection;
  let srcId = link.get("source").NodeId,
    destId = link.get("target").NodeId;

  console.log(
    "linkCurrentSrcDir",
    linkCurrentSrcDir,
    " linkCurrentDestDir",
    linkCurrentDestDir,
    " srcId",
    srcId,
    " destId",
    destId
  );

  link.get("linkProperties").SrcNodeDirection = srcD;
  link.get("linkProperties").DestNodeDirection = destD;

  let srcNode = graph.getCell(getNodeById(srcId)),
    destNode = graph.getCell(getNodeById(destId));

  let srcNodeType = srcNode.get("type"),
    destNodeType = destNode.get("type");

  if (srcNodeType != nptGlobals.NodeTypeILA) {
    srcNode.get("nodeProperties").nodeConnections[linkCurrentSrcDir] = 0;
    srcNode.get("nodeProperties").nodeConnections[srcD] = destId;
    fTemporaryUpdateNodeConenctions(srcNode);
  }

  if (destNodeType != nptGlobals.NodeTypeILA) {
    destNode.get("nodeProperties").nodeConnections[linkCurrentDestDir] = 0;
    destNode.get("nodeProperties").nodeConnections[destD] = srcId;
    fTemporaryUpdateNodeConenctions(destNode);
  }
}

/************************************************************************
 * Function to update node connections
 ************************************************************************/
function fTemporaryUpdateNodeConenctions(node) {
  console.log("*********fTemporaryUpdateNodeConenctions**********");

  console.log(
    "Before NodeConnections :: ",
    node.get("nodeProperties").nodeConnections
  );

  let values = _.values(node.get("nodeProperties").nodeConnections);

  for (var i = 0; i < 5; i++) {
    if (values[i] == 0 && values[i + 1] != 0) {
      values[i] = values[i + 1];
      values[i + 1] = 0;
    }
  }

  let newNodeConnectionObj = {};
  values.map(function(val, i) {
    let key = Number(i) + 1;
    // console.log("index:",i," val:",val," key:",key);

    newNodeConnectionObj[key] = val;
  });

  node.get("nodeProperties").nodeConnections = newNodeConnectionObj;

  console.log(" New NodeConnections :: ", newNodeConnectionObj);
  console.log(
    "After NodeConnections :: ",
    node.get("nodeProperties").nodeConnections
  );

  //	values = _.values(destNode.get("nodeProperties").nodeConnections);
  //
  //	for(var i=0;i<4;i++){
  //		if(values[i]==0 && values[i+1]!=0)
  //			values[i]=values[i+1];
  //	}
  //
  //	newNodeConnectionObj={};
  //	values.map(function(val,i){
  //		let key=Number(i)+1;
  //		// console.log("index:",i," val:",val," key:",key);
  //
  //		newNodeConnectionObj[key]=val;
  //	});
  //
  //	destNode.get("nodeProperties").nodeConnections=newNodeConnectionObj;
}

/************************************************************************
 * Function to zoomIn and zoomOut
 ************************************************************************/
var graphScale = 1;
//var svgPaperoriginalWidth=1;

var paperScale = function(sx, sy) {
  paper.scale(sx, sy);
};

//var zoomOut = function() {
//    graphScale -= 0.1;
//    if(graphScale>5)
//    	{
//    	paperScale(graphScale, graphScale);
//        $("#svgPaperInCanvas").width($(this).width()*graphScale);
//        $("#svgPaperInCanvas").height($(this).height()*graphScale);
//        /*$("svg #v-2").width($("#svgPaperInCanvas").width());
//        $("svg #v-2").height($("#svgPaperInCanvas").height());*/
//    	}
//};
//
//var zoomIn = function() {
//    graphScale += 0.1;
//    if(graphScale<1.5)
//    	{
//    	paperScale(graphScale, graphScale);
//        $("#svgPaperInCanvas").width($(this).width()*graphScale);
//        $("#svgPaperInCanvas").height($(this).height()*graphScale);
//        /*$("svg #v-2").width($("#svgPaperInCanvas").width());
//        $("svg #v-2").height($("#svgPaperInCanvas").height());*/
//    	}
//};
//
//var resetZoom = function() {
//    graphScale = 1;
//    paperScale(graphScale, graphScale);
//};

var zoomOut = function() {
  graphScale -= 0.1;
  if (graphScale > 0.5) {
    paperScale(graphScale, graphScale);
    $("#svgPaperInCanvas").width($(this).width() * graphScale);
    $("#svgPaperInCanvas").height($(this).height() * graphScale);
    /*$("svg #v-2").width($("#svgPaperInCanvas").width());
        $("svg #v-2").height($("#svgPaperInCanvas").height());*/
  }
};

var zoomIn = function() {
  graphScale += 0.1;
  if (graphScale < 1.5) {
    paperScale(graphScale, graphScale);
    $("#svgPaperInCanvas").width($(this).width() * graphScale);
    $("#svgPaperInCanvas").height($(this).height() * graphScale);
    /*$("svg #v-2").width($("#svgPaperInCanvas").width());
        $("svg #v-2").height($("#svgPaperInCanvas").height());*/
  }
};

var resetZoom = function() {
  graphScale = 1;
  paperScale(graphScale, graphScale);
};

var closeBootBoxAlert = function(time) {
  var timeInterval = 1000;
  if (time != undefined) timeInterval = time;
  window.setTimeout(function() {
    bootbox.hideAll();
  }, timeInterval);
};

/************************************************************************
 * Adjust SideBar height on page Load
 * Also Used when window resizes
 ************************************************************************/
function adjustSideBarHeightOnLOad() {
  //console.log("--------------- adjustSideBarHeightOnLOad --------------------");
  let initialSideBarHeight = $("#sidebar").height(),
    networkelemIdHeight = $("#netelemId").height(),
    networkelemPanelHeight = $("#networkElementPanel").height(),
    sideBarOffset = $("#sidebar").offset(),
    networkelemIdOffset = $("#netelemId").offset(),
    footerHeight = $("footer").height(),
    windowHeight = $(window).height(),
    tabContentsSideBarHeight = $("#sidebar #tabularMenu").height(),
    menuInsideCanvasHeight = $("#menuDivInsideCanvas").outerHeight(),
    networkElemPanelHeadingHeight = $(
      "#networkElementPanel .panel-heading"
    ).height();

  //console.log("initialSideBarHeight"+initialSideBarHeight+"networkelemIdHeight"+networkelemIdHeight);
  //console.log("footerHeight" + footerHeight + "windowHeight" + windowHeight);
  //console.log("networkelemPanelHeight"+networkelemPanelHeight);
  //console.log("sideBarOffset", sideBarOffset);
  // console.log("networkelemIdOffset");
  //console.log(networkelemIdOffset);
  $("#canvasId").width("100%");
  $("#canvasId").outerHeight(
    windowHeight - sideBarOffset.top - footerHeight - menuInsideCanvasHeight
  );
  $("#netelemId").height(
    windowHeight -
      sideBarOffset.top -
      footerHeight -
      networkElemPanelHeadingHeight
  );
  $("#sidebar").height(windowHeight - sideBarOffset.top - footerHeight);
  $("#networkElementPanel").height(
    windowHeight - sideBarOffset.top - footerHeight
  );
  $("#sidebar #tabularMenu").height($("#sidebar").height());

  setSidebarContainerContentHeight();
  //$(".demands-circuit-view__content").outerHeight($('#sidebar').height()-tabContentsSideBarHeight-$('#propertyCollapse').height()-15);
  /*var networkelemIdHeight=$('#netelemId').height();*/
}

/************************************************************************
 * Adjust width on page Load
 ************************************************************************/
function adjustWidthOnLoad() {
  let min = $(window).width() / 3,
    max = $(window).width() - $(window).width() * 0.25;
  let canvasWidth;
  canvasWidth =
    ((max - $("#canvasIdContainer").offset().left) / $(window).width()) * 100;

  let sideBarWidth = 100 - canvasWidth - 9.33;

  $("#sidebar").css("width", sideBarWidth + "%");
  $("#canvasIdContainer").css("width", canvasWidth + "%");

  //console.log("adjustWidthOnLoad -- SideBarWidth:",$("#sidebar").width());

  let networkElContainerWidth = $(".network-elements-container").width();
  console.log(
    "################ networkElContainerWidth#################33",
    networkElContainerWidth
  );
  $("#netelemId").width(networkElContainerWidth);
}



var getLabelFromState = function(state) {
  var template = "";
  console.log("getLabelFromState :: State sending --" + state);
  if (
    state == nptGlobals.NewRowClassStr ||
    state == nptGlobals.ModifiedRowClassStr
  )
    template += `<span class="RowTag"><img title="Brown Field" alt="Green Field" src="../images/bfAdd.png"></span>`;
  else if (
    state == nptGlobals.OldRowClassStr ||
    state == nptGlobals.GreenFieldRowClassStr ||
    state == nptGlobals.DeletedRowClassStr
  )
    template += `<span class="RowTag"><img title="Green Field" alt="Brown Field" src="../images/gfAdd.png"></span>`;
  console.log("getLabelFromState :: State sending --" + template);
  return template;
};

var getLabelTagsFromState = function(state) {
  //console.log("getLabelTagsFromState :: State received --"+state);
  var template = "";
  if (state == nptGlobals.NewRowClassStr)
    template += `<span class="RowTag"><img title="New" alt="New" src="../images/newLabel.svg"></span>`;
  else if (state == nptGlobals.OldRowClassStr)
    template += `<span class="RowTag"><img title="Old" alt="Old" src="../images/oldLabel.svg"></span>`;
  //else if(state==nptGlobals.GreenFieldRowClassStr)
  //template+=`<span class="RowTag"><img title="Green Field" alt="Green Field" src="../images/gfAdd.png"><span class="badge">GF</span></span>`;
  else if (state == nptGlobals.DeletedRowClassStr)
    template += `<span class="RowTag"><img title="Deleted" alt="Deleted" src="../images/deletedLabel.svg"></span>`;
  else if (state == nptGlobals.ModifiedRowClassStr)
    template += `<span class="RowTag"><img title="Modified" alt="Modified" src="../images/modifiedLabel.svg"></span>`;
  //	if(state== nptGlobals.NewRowClassStr)
  //		template+=`<span class="badge">New</span>`;
  //	else if(state== nptGlobals.OldRowClassStr)
  //		template+=`<span class="badge">Old</span>`;
  //	else if(state== nptGlobals.GreenFieldRowClassStr)
  //		template+=``;
  //	else if(state== nptGlobals.DeletedRowClassStr)
  //		template+=`<span class="badge">Deleted</span>`;
  //	else if(state== nptGlobals.ModifiedRowClassStr)
  //		template+=`<span class="badge">Modified</span>`;

  //console.log("getLabelTagsFromState :: State sending --"+template);
  return template;
};

/************************************************************************
 * Initialize Map
 ************************************************************************/
//function defaultNodeLinkAttributes()
//{
//	console.log("initializeMapFromDashboard");
//	var linkId,nodeId,linkArr=[],nodeArr=[],expectedId=1;
//	_.each(graph.getC(), function(el) {
//		console.log("Element");
//		console.log(el);
//		if (el.isLink()) {
//			//Get Link Id
//			el.attr(attrs.linkDefault);
//		} else {
//			//console.log(el);
//			var nodeId=el.get('nodeProperties').nodeId;
//			sortedPush(nodeArr, nodeId);
//		}
//
//    });
//
//}

/************************************************************************
 * Active tab --> Input : id of tab
 ************************************************************************/
function activeTab(tab) {
  console.log('Trigger Link:: .nav-tabs a[href="#' + tab + '"]');
  console.log("Setting Active tab :: #" + tab);
  $('.nav-tabs a[href="#' + tab + '"]').tab("show");
}

$('.nav-tabs a[href="#canvasId"]').on("click", function() {
  console.log("Clicked Canvas tab");
});

/************************************************************************
 * Confirmation Dialog
 ************************************************************************/
function bootBoxCustomDialog(body, title, callBack) {
  bootbox.dialog({
    message: body,
    title: title,
    buttons: {
      Ok: {
        //label: "Success!",
        className: "btn btn--default",
        callback: function() {
          callBack();
        }
      },
      Cancel: {
        className: "btn btn--default",
        callback: function() {
          console.log("User clicked cancel.");
        }
      }
    }
  });
}

/************************************************************************
 * OverLay functions for loading/refreshing/fetching
 ************************************************************************/
function overlayOn(msg, parentSelector) {
  console.log("Overlay On :", parentSelector);
  $(parentSelector).show();
  console.log($(parentSelector + " .overlay"));
  $(parentSelector + " .overlay").css({ display: "block" });
  $(parentSelector + " .overlay")
    .find("p")
    .text(msg);
}

function overlayOff(msg, parentSelector, effect) {
  console.log("Overlay Off :", parentSelector);
  $(parentSelector + " .overlay")
    .find("p")
    .text(msg);

  let slideEffect = effect || "slide";
  console.log("slideEffect:", slideEffect);

  setTimeout(function() {
    $(parentSelector + " .overlay").hide(slideEffect);
    $(parentSelector).hide(slideEffect);
    //$(parentSelector + " .overlay").css({ 'display': 'none' });
  }, 500);
}

/**
 * Function to set the Optical Reach value as per the set value of SpanLength
 */

setOpticalReachAsPerSpanLength = function(linkCellId) {
  ///console.log('setOpticalReachAsPerSpanLength : ', linkCellId, "SpanLength : ",graph.getCell(linkCellId).attributes.linkProperties.spanLength);

  if (graph.getCell(linkCellId).attributes.linkProperties.spanLength > 80) {
    console.log("very long haul");
    graph.getCell(
      graph.getCell(linkCellId).get("source")
    ).attributes.nodeProperties.opticalReach = "Very Long Haul";
    graph.getCell(
      graph.getCell(linkCellId).get("target")
    ).attributes.nodeProperties.opticalReach = "Very Long Haul";
  } else {
    console.log(" long haul");
    graph.getCell(
      graph.getCell(linkCellId).get("source")
    ).attributes.nodeProperties.opticalReach = "Long Haul";
    graph.getCell(
      graph.getCell(linkCellId).get("target")
    ).attributes.nodeProperties.opticalReach = "Long Haul";
  }
};

/************************************************************************
 * Open Rappid dialog
 * parameter - type,callback,callback arguments
 ************************************************************************/
function openDialog() {
  let args = Array.from(arguments);
  const type = args[0],
    content = args[1],
    callback = args[2];

  const callbackArgs = args.slice(3);

  var dialog = new joint.ui.Dialog({
    type: type,
    width: 300,
    title: "Confirm",
    content: content,
    buttons: [
      { action: "Yes", content: "Yes" },
      { action: "No", content: "No" }
    ],
    draggable: true
  });

  dialog.on("action:Yes", function() {
    if (callbackArgs.length > 0) callback(...callbackArgs);
    else callback();
    dialog.close();
  });

  dialog.on("action:No", function() {
    dialog.close();
  });
  dialog.open();
}
