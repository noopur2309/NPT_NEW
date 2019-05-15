//Get the modal
var modal = document.getElementById("myModal");

app.controller("createMapController", function($scope, $http) {
  console.log("****************createMapController*****************");
  $scope.NetworkName = "";
  $scope.NumOfNodes = 0;
});

app.directive("networkNameDirective", function() {
  return {
    require: "ngModel",
    link: function(scope, element, attr, mCtrl) {
      function myValidation(value) {
        if (value == "") return value;

        let pattern = new RegExp("^[a-zA-Z0-9_]+$");
        console.log("Value", value);
        if (value.match(pattern) == null)
          mCtrl.$setValidity("networkNameValidity", false);
        else mCtrl.$setValidity("networkNameValidity", true);
        return value;
      }
      mCtrl.$parsers.push(myValidation);
    }
  };
});

$("#modalCreateMapBtn").on("click", function(event) {
  event.preventDefault();
  var errorFlag = false;
  //var spinner=$('<img src="images/Spinner.gif" class="img-responsive text-center">');
  //$(this).empty().append(spinner);
  $(this).html("Wait .....");
  $(this).attr("disabled", true);

  var formInputArr = $("#createMapInputForm").serializeArray();
  //console.log("Form Input Array");
  console.log(formInputArr);

  var createNetworkObj = new Object();
  for (var i = 0; i < formInputArr.length; i++) {
    let key = formInputArr[i].name,
      value = formInputArr[i].value;
    createNetworkObj[key] = value;
  }
  console.log("createNetworkObj::", createNetworkObj);
  createNetworkObj.NetworkName = String(
    createNetworkObj.NetworkName
  ).toUpperCase();

  sessionStorage.setItem("NetworkName", createNetworkObj.NetworkName);
  //sessionStorage.setItem("UserName","admin");//Debug
  //sessionStorage.setItem("UserName",sessionStorage.getItem("UserName"));//Debug
  sessionStorage.setItem("NetworkState", nptGlobals.GreenFieldStr); //Debug
  sessionStorage.setItem("Demands", 0); //Debug
  sessionStorage.setItem("Circuits", 0); //Debug
  sessionStorage.setItem("Traffic", 0); //Debug
  console.log("Session NetworkName :", sessionStorage.getItem("NetworkName"));
  console.log("Session UserName :", sessionStorage.getItem("UserName"));

  createNetworkObj.UserName = sessionStorage.getItem("UserName");

  let numOfNodes = Number(createNetworkObj.NumOfNodes);
  console.log(
    "numOfNodes:",
    numOfNodes,
    "nptGlobals.getNetworkMinNodes:",
    nptGlobals.getNetworkMinNodes(),
    "nptGlobals.getNetworkMaxNodes:",
    nptGlobals.getNetworkMaxNodes()
  );

  //Minimum node for Two Fibrering is 3
  //	if(numOfNodes<3 && createNetworkObj.Topology==nptGlobals.TopologyTwoFiberRingStr){
  if (
    numOfNodes < 0 &&
    createNetworkObj.Topology == nptGlobals.TopologyTwoFiberRingStr
  ) {
    $("#createNetworkError")
      .empty()
      .append(
        `<div class="alert alert-danger alert-dismissable">
				<a href="#" class="close" data-dismiss="alert"
				aria-label="close">&times;</a> <strong>Failure!</strong>
		Minimum number of nodes required for ${
      nptGlobals.TopologyTwoFiberRingStr
    } is 3.</div>`
      )
      .show();
    $("#modalCreateMapBtn").html(
      '<i class="fa fa-file" aria-hidden="true"></i> Create'
    );
    $("#modalCreateMapBtn").attr("disabled", false);
  }
  //Number of nodes is between permissible range
  else if (
    numOfNodes >= nptGlobals.getNetworkMinNodes() &&
    numOfNodes <= nptGlobals.getNetworkMaxNodes()
  ) {
    //fShowClientServerCommunicationModal("Updating Equipment list of NPT. ");

    serverPostMessage("createNetwork", createNetworkObj)
      .then(function(data) {
        console.log("success" + data);

        if (data == 1) {
          console.log(
            "map created with networkName :" + createNetworkObj.NetworkName
          );
          $("#createMapModal").modal("hide");

          //console.log("networkName"+networkName+" numOfNodes:"+numOfNodes+" topology: "+topology);

          nptGlobals.NetworkName = createNetworkObj.NetworkName;

          nptGlobals.NetworkTopology = createNetworkObj.Topology;
          sessionStorage.setItem("Topology", createNetworkObj.Topology); //Debug
          var topology = createNetworkObj.Topology;

          if (topology == nptGlobals.TopologyLinearStr)
            fCreateMapLinearInitialize(createNetworkObj.NumOfNodes, topology);
          else if (
            topology == nptGlobals.TopologyTwoFiberRingStr ||
            topology == nptGlobals.TopologyMeshStr ||
            topology == nptGlobals.TopologyHubRingStr
          )
            fCreateMapRingAndMeshInitialize(
              createNetworkObj.NumOfNodes,
              topology
            );

          //Add custom context menu on nodes
          customContextMenuInitialize();

          //Save default map to DB
          var saveMapPostData = new jsonPostObjectMapSave();
          fShowClientServerCommunicationModal("Saving Default Map...");
          serverPostMessage("save", saveMapPostData)
            .then(function(data) {
              console.log("Default map saved seccesfully :", data);
              fShowSuccessMessage("Default map saved seccesfully.");
              nptGlobals.setMapSaved(_SET_VALUE);
            })
            .catch(function(error) {
              fShowSuccessMessage("Default map data save failed.");
              console.log(error);
              nptGlobals.setMapSaved(_UNSET_VALUE);
            });
        } else if (data == 0) {
          console.log(
            "map couldn't be created with networkName :",
            nptGlobals.NetworkName
          );
          errorFlag = true;
          $("#modalCreateMapBtn").html(
            '<i class="fa fa-file" aria-hidden="true"></i> Create'
          );
          $("#modalCreateMapBtn").attr("disabled", false);
        }
        //			else if(data==2)
        //				{
        //				console.log("map couldn't be created with Zero nodes :"+networkName);
        //				errorFlag=true;
        //				$("#modalCreateMapBtn").attr("disabled",false);
        //				}

        if (errorFlag) {
          $("#createNetworkError")
            .empty()
            .append(
              `<div class="alert alert-danger alert-dismissable">
						<a href="#" class="close" data-dismiss="alert"
						aria-label="close">&times;</a> <strong>Failure!</strong>
				Network name already used.</div>`
            )
            .show();
        }
      })
      .catch(function(error) {
        fShowSuccessMessage("Create new map failed.");
        console.log(error);
      });
  } else {
    $("#createNetworkError")
      .empty()
      .append(
        `<div class="alert alert-danger alert-dismissable">
				<a href="#" class="close" data-dismiss="alert"
				aria-label="close">&times;</a> <strong>Failure!</strong>
		Maximum number of nodes supported in NPT is 15.</div>`
      )
      .show();
    $("#modalCreateMapBtn").html(
      '<i class="fa fa-file" aria-hidden="true"></i> Create'
    );
    $("#modalCreateMapBtn").attr("disabled", false);
  }
});

var validateCreateNetworkInput = function(formInput) {
  //	if(formInput.NetworkName.length==0)
  //	return "Error:";
};

/*$("body").delegate( "#modalCreateMapBtn", "click", function() {
	//e.preventDefault();
	var form=$("#createMapInputForm");
	console.log(form);
	$('#createMapModal').modal('hide');
});
 */
$("#createMapId").click(function() {
  ///alert('Create Map !')
  $("#createMapModal").modal("show");
});

/*$("#modalCreateMapBtn").on('click',function(event){
	event.preventDefault();
	consol.log($(this));
	console.log("Inside Create Map Form Submit");
	var form=$("#createMapInputForm").serialize();
	console.log(form);
	$('#createMapModal').modal('hide');
});*/

function fCreateMapLinearInitialize(numOfNodes, topology) {
  var position = { x: 50, y: 100 };
  var xPosInc = 150,
    yPosInc = 100;
  var currNode, nextNode, link;
  var currNodeIdLinear, nextNodeIdLinear;
  var currCellView, nextCellView;
  var dir1, dir2;
  if (numOfNodes == 1) {
    currNode = insertTeNode(position);
    // currCellView=paper.findViewByModel(currNode);
    // window.cellLocal=currCellView;
    // addPortToDirection("east");
    return;
  }

  for (var i = 1; i < numOfNodes; i++) {
    console.log("Value of i :" + i);

    if (i == 1) {
      //first time node insertion
      //currNode=insertRoadmNode(position);
      currNode = insertTeNode(position);
      // currCellView=paper.findViewByModel(currNode);
      // window.cellLocal=currCellView;
      //addPortToDirection("east");
      // addPortToDirection(nptGlobals.EastDirection);
    } else currNode = nextNode;

    console.log("currNode :", currNode);

    //increment position by xPosInc
    position.x = position.x + Number(xPosInc);

    if (i == numOfNodes - 1) {
      nextNode = insertTeNode(position);
      // nextCellView=paper.findViewByModel(nextNode);
      // window.cellLocal=nextCellView;
      //addPortToDirection("west");
      // addPortToDirection(nptGlobals.WestDirection);
    } else {
      //insert new node
      //nextNode=insertRoadmNode(position);
      nextNode = insertTwoDegreeRoadmNode(position);
      // nextCellView=paper.findViewByModel(nextNode);
      // window.cellLocal=nextCellView;
      //addPortToDirection("all");
      // addPortToDirection(nptGlobals.WestDirection);
      // addPortToDirection(nptGlobals.EastDirection);
    }

    // Get node Id for passsing to createlink()
    currNodeIdLinear = currNode.get("id");
    nextNodeIdLinear = nextNode.get("id");

    //create link b/w nodes
    link = insertLink(currNodeIdLinear, nextNodeIdLinear);

    // fUpdateSourceTargetProperties(currNode, nextNode);
    fUpdateLinkConnectionProperties(link);
  }

  //For attatching links to the ports in Map Canvas (GUI issue)
  // setallNodeAttributes();
  nodeInfoTab();
  linkInfoTab();
  networkInfoTab();
  //paper.scaleContentToFit();
  //window.ScaleVar=;
  //zoomOut();
  /*if(numOfNodes>8 && topology=='Linear' )
		{
		 console.log("Need to scale");
		 paper.scale(0.6);
		}*/

  let AllNodes = graph.getElements();
  _.each(AllNodes, function(node, index) {
    node.get("nodeProperties").nodeLat = GisMap.getLatLngList()[index].latitude;
    node.get("nodeProperties").nodeLng = GisMap.getLatLngList()[
      index
    ].longitude;
    GisMap.setlocation(node);
  });

  GisMap.mapZoomEvent();
}

function fCreateMapRingAndMeshInitialize(numOfNodes, topology) {
  var position = { x: 500, y: 500 };
  var centre = { x: 500, y: 350 };
  var radius;
  if (numOfNodes < 5) radius = 200;
  else radius = 200 + numOfNodes * 10;
  var thetaInc = 360 / numOfNodes;
  var theta = 0,
    radian = 0;
  var newpoint_x, newpoint_y;

  var currNode, nextNode, link, lastNode;
  var currNodeCid, nextNodeCid;
  var currCellView, nextCellView;
  var dir1, dir2;

  if (numOfNodes == 1) {
    //currNode=insertTeNode(position);
    if (topology == nptGlobals.TopologyTwoFiberRingStr)
      currNode = insertTwoDegreeRoadmNode(position);
    else if (topology == nptGlobals.TopologyHubRingStr)
      currNode = insertTwoDegreeRoadmNode(position);
    /*if (topology==nptGlobals.TopologyMeshStr)*/ else
      currNode = insertRoadmNode(position);
    return;
  }

  for (var i = 1; i <= numOfNodes; i++) {
    console.log("Value of i :" + i);

    if (i == 1) {
      //first time node insertion
      radian = Math.PI * (theta / 180);
      newpoint_x = Math.cos(radian) * radius + centre.x;
      newpoint_y = Math.sin(radian) * radius + centre.y;
      console.log(
        "newpoint_x :" +
          newpoint_x +
          " newpoint_y" +
          newpoint_y +
          " radian:" +
          radian
      );
      theta += thetaInc;
      position.x = newpoint_x;
      position.y = newpoint_y;
      //currNode=insertRoadmNode(position);

      if (topology == nptGlobals.TopologyTwoFiberRingStr)
        currNode = insertTwoDegreeRoadmNode(position);
      else if (topology == nptGlobals.TopologyHubRingStr)
        currNode = insertHubNode(position);
      /*if(topology==nptGlobals.TopologyMeshStr)*/ else
        currNode = insertRoadmNode(position);

      lastNode = currNode;
    } else currNode = nextNode;

    radian = Math.PI * (theta / 180);
    newpoint_x = Math.cos(radian) * radius + centre.x;
    newpoint_y = Math.sin(radian) * radius + centre.y;
    console.log(
      "newpoint_x :" +
        newpoint_x +
        " newpoint_y" +
        newpoint_y +
        " radian:" +
        radian
    );
    theta += thetaInc;
    position.x = newpoint_x;
    position.y = newpoint_y;

    //case for end of ring where next node is the first node
    if (i == numOfNodes) {
      nextNode = lastNode;
    } else {
      //insert new node
      //nextNode=insertRoadmNode(position);
      if (
        topology == nptGlobals.TopologyTwoFiberRingStr ||
        topology == nptGlobals.TopologyHubRingStr
      )
        nextNode = insertTwoDegreeRoadmNode(position);
      /*if(topology==nptGlobals.TopologyMeshStr)*/ else
        nextNode = insertRoadmNode(position);
    }

    // Get node Id for passsing to createlink()
    currNodeCid = currNode.get("id");
    nextNodeCid = nextNode.get("id");

    /* Changed on suggestion of Sarbari Mam
     * OMS ptc removed for Two Fiber Ring */

    //		if(topology==nptGlobals.TopologyTwoFiberRingStr)
    //			link=insertOmsProtectedLink(currNodeCid,nextNodeCid);
    //		else
    link = insertLink(currNodeCid, nextNodeCid);

    // fUpdateSourceTargetProperties(currNode, nextNode);
    fUpdateLinkConnectionProperties(link);
  }

  //For attatching links to the ports in Map Canvas (GUI issue)
  // setallNodeAttributes();

  //fLinktoFront();
  nodeInfoTab();
  linkInfoTab();
  networkInfoTab();
  //paper.scaleContentToFit();
  //window.zoomVar=1;
  //zoomOut();
  if (numOfNodes > 8 && topology == nptGlobals.TopologyLinearStr) {
    console.log("Need to scale");
    paper.scaleContentToFit();
  }
  let AllNodes = graph.getElements();
  _.each(AllNodes, function(node, index) {
    node.get("nodeProperties").nodeLat = GisMap.getLatLngList()[index].latitude;
    node.get("nodeProperties").nodeLng = GisMap.getLatLngList()[
      index
    ].longitude;
    GisMap.setlocation(node);
  });

  GisMap.mapZoomEvent();
}
