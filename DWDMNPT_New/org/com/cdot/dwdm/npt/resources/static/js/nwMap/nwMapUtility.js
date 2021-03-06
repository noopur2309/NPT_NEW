//Update NPT schema dynamically
function updateSchema() {
  fShowClientServerCommunicationModal("Updating Schema of NPT. ");
  serverGetMessage("updateSchema")
    .then(function(data) {
      console.log("Schema updated seccesfully :", data);
      fShowSuccessMessage(data);
    })
    .catch(function(e) {
      fShowFailureMessage("Schema updation failed.");
      console.log(e);
    });
}

//Logout NPT
function logoutNpt() {
  overlayOn("Logging Out.", ".body-overlay");
  setTimeout(() => {
    sessionStorage.clear();
    window.open("index.html", "_self");
  }, 1000);
}

//Patch Cord Allocation
function generatePatchCord() {
  let postData = jsonPostObject();
  fShowClientServerCommunicationModal("Allocating Patch Cord ... ");
  serverPostMessage("generatePatchCord", postData)
    .then(function(data) {
      console.log(data.Response);
      fShowSuccessMessage(data.Response);
    })
    .catch(function(e) {
      fShowFailureMessage("Patch Cord Allocation failed.");
      console.log(e);
    });
}


/************************************************************************
 * Test ajax call from testing call nav trigger
 ************************************************************************/
function testingCall() {
  fShowClientServerCommunicationModal("Testing Initiated .. ");

  $.ajax({
    type: "GET",
    contentType: "application/json;text/html",

    headers: {
      Accept: "application/json;text/html; charset=utf-8",
      "Content-Type": "application/json;text/html; charset=utf-8"
    },

    url: "/saveTest",

    timeout: 600000,

    success: function(data) {
      console.log("success" + data);

      $("#nptModal > img").attr("src", "images/success.png");
      $("#modalLoadingContent p").text("Test Case Executed Successfully :) ");
      disableCloseButton(false);
      fadeOutModal();
    },
    error: function(e) {
      $("#nptModal > img").attr("src", "images/success.png");
      $("#modalLoadingContent p").text("Test Case Executed Successfully :) ");
      //			$("#nptModal > img").attr('src', 'images/failure.png');
      //			$("#modalLoadingContent p").text("Error in Request Process :(  ");
      disableCloseButton(false);
    }
  });
}

/************************************************************************
 * Test ajax call
 ************************************************************************/
function testAjax() {
  console.log("textajax");
  var data = {};
  data["name"] = "bull";
  data["age"] = "9";
  console.log(JSON.stringify(data));
  var gr = JSON.parse(JSON.stringify(graph));
  gr["cells"].push({ type: "network", networkname: "TestNetwork" });
  // console.log('Graph = ', JSON.stringify(graph));
  console.log(JSON.stringify(gr));
  $.ajax({
    contentType: "application/json;charset=UTF-8",
    url: "/save",
    dataType: "json",
    type: "POST",
    cache: false, // Force requested pages not to be cached by the
    // browser
    processData: false, // Avoid making query string instead of JSON
    data: JSON.stringify(gr)
  })
    .done(function(data) {
      console.log("AJAX call was successfully executed ;)");
      console.log("data = ", graph);
    })
    .fail(function() {
      console.log("AJAX call failed :(");
    });
}

/************************************************************************
 * Generate consolidated report
 ************************************************************************/
function generateConsolidatedReport() {
	let nodes=graph.getElements();
	nodes.sort((a, b) => parseFloat(a.position().x) - parseFloat(b.position().x));
	let nodesInformation=[];
	for(let i in nodes){
		nodesInformation.push(nodes[i].attributes.nodeProperties);
	}
	let postData = jsonPostObject();
	paper.toJPEG(function(imageData){
		postData.NetworkInfo.ImgData=imageData;
		postData.NetworkInfo.nodesData=nodesInformation;
		console.log(postData);
		fShowClientServerCommunicationModal("Generating  Report.. ");
		$.ajax({
		    type : "POST",
		    url : '/generateConsolidatedReportTrigger',
		    data: JSON.stringify(postData),
		    contentType: "application/json; charset=utf-8",
		    xhrFields: {
		        responseType: 'blob'
		    },
		    error: function(XMLHttpRequest, error, errorThrown){  
		          	console.log(error);  
		          	console.log(errorThrown);  
		          },
           success: function(blob){
        	      fShowSuccessMessage("Report Generated succesfully.");
        	      var link=document.createElement('a');
        	      link.href=window.URL.createObjectURL(blob);
        	      link.download="Planning Report.pdf";
        	      document.body.appendChild(link);
        	      link.click();
        	      document.body.removeChild(link);
        	      
        	  }
		});
	});
}

/*******************************************************************************
 * Upload Configuration Files- Control and Data (.zip) to DB & Generic Ajax Call
 * @author hp
 * @date   15th Feb, 2018
 ******************************************************************************/
(imgName = ""), (imgData = "");

function uploadFileToEMS1() {
  var settings = {
    type: "POST",
    url: "http://196.1.106.11:8080/dwdmNbi/rest/configFile/upload",
    data: imgData,
    contentType: "multipart/form-data",
    global: false,
    async: false,
    dataType: "json"
  };

  $.ajax(settings).done(function(response) {
    console.log(response);
    alert(response);
  });
} 

function genericAjaxCall(ajaxPostObject) {
  console.log(
    "url " +
      ajaxPostObject.url +
      "NetworkInfo " +
      ajaxPostObject.NetworkInfo.NetworkName +
      " type " +
      ajaxPostObject.type
  );

  fShowClientServerCommunicationModal("Uploading EMS Data To NPT Server ...");

  $.ajax({
    type: ajaxPostObject.type,
    contentType: "application/json",

    headers: {
      Accept: "application/json; charset=utf-8",
      "Content-Type": "application/json; charset=utf-8"
    },

    url: ajaxPostObject.url,
    data: JSON.stringify(ajaxPostObject.NetworkInfo),
    dataType: "json",

    timeout: 600000,

    success: function(data) {
      console.log("success" + data);
      fShowSuccessMessage("Ajax Call Success  ");
    },
    complete: function() {},
    error: function(e) {
      console.log("fail" + e);
      fShowFailureMessage("Ajax Call Failed => " + e);
    }
  });
}

function readUrl(input) {
  if (input.files && input.files[0]) {
    let reader = new FileReader();
    reader.onload = e => {
      imgData = e.target.result;
      imgName = input.files[0].name;
      input.setAttribute("data-title", imgName);
      console.log("imgName " + imgName); ///+" e.target.result" +e.target.result);
      checkFileNameFormat(imgName);
    };
    reader.readAsDataURL(input.files[0]);
  }
}

function checkFileNameFormat() {
  // disable ok button and show the red starred message on dialog
  console.log("checkFileNameFormat");
}

function uploadZipFileToServer() {
  var ajaxPostObject = new Object();
  ajaxPostObject.NetworkInfo = new Object();
  ajaxPostObject.NetworkInfo.NetworkName = sessionStorage.getItem(
    "NetworkName"
  );

  ajaxPostObject.type = "POST";
  ajaxPostObject.url = "/uploadConfigFileFromEMSTrigger";

  ajaxPostObject.NetworkInfo.img = new Object();
  ajaxPostObject.NetworkInfo.img.imgName = imgName;
  ajaxPostObject.NetworkInfo.img.imgData = imgData;

  console.log("ajaxPostObject " + ajaxPostObject);
  console.log("uploadZipFileToServer imgName " + imgName); ///+ " imgData "+imgData);
  ///genericAjaxCall(ajaxPostObject);
  uploadFileToEMS1();

  //	delete imgName;
  //	delete imgData;
}

function uploadConfigFileFromEMSTrigger() {
  const messageAlert = `<div class="container">  
		   <div class="row">
		    <div class="col-sm-6 offset-sm-3">      
			<div class="form-group inputDnD">
			<label class="sr-only" for="inputFile">File Upload</label>
			<input type="file" class="form-control-file text-success font-weight-bold" id="inputFile"  onchange="readUrl(this)" data-title="Drag and drop a file">
		  </div>
		</div>  
	  </div>`;

  const titleText = `<p class="text-center lead text-primary"><strong>Upload Config File To EMS Server (tar.gz.)</strong></p>`;

  const authParam = `<div class="container" >  
	   <div class="row">
		<div class="col-sm-6 offset-sm-3" id="formGetAuth">   
			<form id="form_id" ">	
				<p>EMS Ip : <input type="text" name="emsIpAddress"  value="196.1.106.154:8080" id="emsIpAddress" style="margin-left:40px;"> </p>
				<p>
					Select a file :<input type="file" name="file" size="45" style="padding:15px; display:inline; " />
			   </p>
			</form>
	   </div>
		</div>  
	  </div>
   `;

  bootBoxCustomDialog(authParam, titleText, authForm);
}

/************************************************************************
 * Save map ajax call to save map to db
 ************************************************************************/

function saveMap() {
  console.log("Session NetworkName :" + sessionStorage.getItem("NetworkName"));
  console.log("Session UserName :" + sessionStorage.getItem("UserName"));

  let saveMapPostData = new jsonPostObjectMapSave();
  //Used On map load from dash board , State of the map saved with mapStr , e.g isMapSaved,isInventoryGenerated etc
  //	saveMapPostData.MapState=[{"isMapSaved":1},{"isCircuitSaved":0},{"isRwaExecuted":0},{"isInventoryGenerated":0}];
  //graphStr["cells"].push({"type":"network","networkname":"TestNetwork"});

  fShowClientServerCommunicationModal("Saving Network Map .. ");

  serverPostMessage("save", saveMapPostData)
    .then(function(data) {
      console.log("success" + data);
      fShowSuccessMessage("Map Saved Successfully ");
      ///setCircuitTableInput(1);

      // window.isFirstTimeOpticalCalculations=1;

      nptGlobals.setMapSaved(true);
      nptGlobals.setCircuitSaved(false);
      nptGlobals.setRwaExecuted(false);
      nptGlobals.setInventoryGenerated(false);
      nptGlobals.setIpSchemeGenerated(false);

      // window.isMapSaved = 1;
      // fNptStateChange(isMapSavedStr);
    })
    .catch(function(e) {
      fShowFailureMessage("Map couldn't be Saved");
      console.log("fail" + e);
    });
}

/************************************************************************
 * RWA execution db ajax call
 ************************************************************************/
function rwaExecutionRequest() {
  console.log("rwaExecutionRequest");
  fShowClientServerCommunicationModal("Executing RWA Algorithm ...");

  let postJsonData = jsonPostObject();

  serverPostMessage("rwaAlgo", postJsonData)
    .then(function(data) {
      console.log("rwaAlgo success" + data);
      //bootBoxAlert("RWA Algorithm Successfully Executed ..");
      if (data == 1) {
        nptGlobals.isFaultAnalysisReady = true;
        // window.isRwaExecuted = 1;
        nptGlobals.setRwaExecuted(true);
        nptGlobals.setInventoryGenerated(false);
        nptGlobals.setIpSchemeGenerated(false);
        // fNptStateChange(isRwaExecutedStr);
        fShowSuccessMessage("Successfully Executed the RWA Algorithm");
      } else if (data == 0) {
        fShowFailureMessage("Error in executing RWA Algorithm");
      } else if (data == 2) {
        nptGlobals.isFaultAnalysisReady = true;
        // window.isRwaExecuted = 1;
        nptGlobals.setRwaExecuted(true);
        nptGlobals.setInventoryGenerated(false);
        nptGlobals.setIpSchemeGenerated(false);
        // fNptStateChange(isRwaExecutedStr);
        fShowWarningMessage(
          "No change in network route . Check demand matrix tab for detailed analysis of network routes."
        );
      }

      window.isFirstTimeOpticalCalculations = 1;
    })
    .catch(function(e) {
      fShowFailureMessage("Error in executing RWA Algorithm");
      console.log("fail", e);
    });
}

/************************************************************************
 * brownFieldConversion db ajax call
 ************************************************************************/
function brownFieldConversion() {
  console.log("brownFieldConversion Request");

  let postJsonData = jsonPostObject();
  if (isBrownFieldConversionAllowed()) {
    fShowClientServerCommunicationModal(
      "Converting network to Brown Field ..."
    );
    serverPostMessage("covertToBrownField", postJsonData)
      .then(function(data) {
        console.log("success" + data);
        //bootBoxAlert("RWA Algorithm Successfully Executed ..");
        if (data == 1) {
          fShowSuccessMessage(
            "Network has been successfully saved as Brown Field. Taking you to back to dashboard.."
          );
        } else if (data == 0) {
          fShowFailureMessage("Error in saving as Brown Field");
        }

        setTimeout(function() {
          //fShowClientServerCommunicationModal("Taking you to back to dashboard..");
          location.replace("NewDashboard.html");
        }, 1000);
      })
      .catch(function(e) {
        fShowFailureMessage("Error in saving as Brown Field");
        console.log("fail", e);
      });
  } else {
    bootBoxAlert(showNetworkStatus(true), 4000);
    //fShowSuccessMessage(showNetworkStatus(true));
  }
}
/************************************************************************
 * Draw map from JSON
 ************************************************************************/
function drawMapFromJSON(jsonStr) {
  console.log("Clearing graph and drawing map from JSON", jsonStr);
  graph.clear();
  graph.fromJSON(JSON.parse(jsonStr));

  //Add custom context menu on nodes
  customContextMenuInitialize();
}

/************************************************************************
 * Save Network State
 ************************************************************************/
var saveNetworkState = function(data) {
  console.log("NetworkStateAjaxCall -- Data::", data);
  updateNptCurrentStateArr(data.NetworkFlowStatus);
  sessionStorage.setItem("NetworkState", data.state);
  // Update Network State Variables
  setNetworkStateVariables(data.NetworkFlowStatus);
};

/************************************************************************
 *  Set Link Source and Target dir and NodeId / backward Compatibility
 ************************************************************************/
function fUpdateLinkFromNodeConnections() {
  console.log("Inside fUpdateLinkFromNodeConnections()");
  _.each(graph.getLinks(), link => {
    let srcNode = link.getSourceElement(),
      destNode = link.getTargetElement();

    let srcNodeId = srcNode.get("nodeProperties").nodeId,
      destNodeId = destNode.get("nodeProperties").nodeId;

    let srcNodeDir = link.get("linkProperties").SrcNodeDirection;
    let destNodeDir = link.get("linkProperties").DestNodeDirection;

    let source = link.get("source");
    let target = link.get("target");

    if (source.hasOwnProperty("NodeId") && target.hasOwnProperty("NodeId")) {
      console.log("Already updated ");
    } else {
      source["NodeId"] = srcNodeId;
      source["dir"] = srcNodeDir;

      target["NodeId"] = destNodeId;
      target["dir"] = destNodeDir;
    }
    console.log(
      source.NodeId,
      " -- ",
      target.NodeId,
      " -- ",
      source.dir,
      " -- ",
      target.dir
    );

    if (link.get("linkProperties").linkType != nptGlobals.getDefaultLinkStr()) {
      srcNode.attributes.nodeProperties.ramanLinks =
        (srcNode.attributes.nodeProperties.ramanLinks || 0) + 1;
      destNode.attributes.nodeProperties.ramanLinks =
        (destNode.attributes.nodeProperties.ramanLinks || 0) + 1;
    }
  });
}


/************************************************************************
 * Restore Green Field Data in a BrownField network
 ***********************************************************************/
var restoreGreenFieldAjaxCall = function() {
  let postJsonData = jsonPostObject();
  fShowClientServerCommunicationModal(
    "Restoring network with Green Field Data ..."
  );
  console.log("instantiateBrownFieldAjaxCall Post Obj ::", postJsonData);

  serverPostMessage("restoreGreenField", postJsonData)
    .then(function(data) {
      console.log("Restore Data::" + data);
      if (data == 1) {
        fShowSuccessMessage(
          "Network has been successfully restored with green field data."
        );
      } else if (data == 0) {
        fShowFailureMessage(
          "Network could not be successfully restored with green field data."
        );
      }
      location.replace("NewDashboard.html");
    })
    .catch(function(e) {
      console.log("fail", e);
    });
};

/************************************************************************
 * Method for reaching out to server via 'POST' method
 * Returns a promise
 * Parameters - Url,PostData
 ************************************************************************/
function serverPostMessage(Url, postData, optionalParam) {
  console.log("optionalParam: ", optionalParam);

  var header = {
    Accept: "application/json; text/html;text/plain; charset=utf-8",
    "Content-Type": "application/json; text/html;text/plain;charset=utf-8",
    "Cache-Control": "no-cache"
  };

  if (optionalParam === undefined) {
    Url = "/" + Url;
  } else {
    header.Authorization = optionalParam.emsSessionToken;
  }

  console.log("header ", header);

  return new Promise((resolve, reject) => {
    $.ajax({
      type: "POST",
      headers: header,
      url: Url,
      data: JSON.stringify(postData),
      dataType: "json",

      timeout: 600000,

      success: function(data) {
        console.log("Promise data returned successfully :", data);
        // pass data returned from server to resolve function
        resolve(data);
        //fShowSuccessMessage('Network has been successfully restored with green field data.');
        //}else reject();
      },
      error: function(e) {
        console.log("Promise returned error :", e);
        reject(e);
      }
    });
  });
}

/************************************************************************
 * Method for reaching out to server via 'GET' method
 * Returns a promise
 * Parameters - Url
 ************************************************************************/
function serverGetMessage(Url, optionalParam) {
  if (optionalParam === undefined) {
    Url = "/" + Url;
  }

  return new Promise((resolve, reject) => {
    $.ajax({
      type: "GET",
      contentType: "application/json",
      Accept: "application/json;text/plain;text/html; charset=utf-8",
      url: Url,
      timeout: 600000,

      success: function(data) {
        console.log("Promise data returned successfully :", data);
        // pass data returned from server to resolve function
        resolve(data);
        //fShowSuccessMessage('Network has been successfully restored with green field data.');
        //}else reject();
      },
      error: function(e) {
        console.log("Promise returned error :", e);
        reject(e);
      }
    });
  });
}

/************************************************************************
 * Disable close button while the process is still going on
 ************************************************************************/
var disableCloseButton = function(val) {
  $(".loadingModalCloseBtn").prop("disabled", val);
};

/************************************************************************
 * Fade Out Modal after 600 second of the successful execution
 ************************************************************************/
var fadeOutModal = function() {
  $("#loadingModal")
    .delay(100)
    .fadeOut();
  setTimeout(function() {
    $("#loadingModal").modal("hide");
  }, 600);
};

/************************************************************************
 *  Loading Icon for Data Refresh - ( Input: Selector )
 ************************************************************************/
function fContentLoading(selector) {
  console.log("fContentLoading :: Selector :" + selector);
  var content = `<p class="text-center contentLoader"><img src="../images/Spinner.gif"></p>`;
  $(selector)
    .empty()
    .append(content);
}

/************************************************************************
 * Custom BootBox alert --- Time(ms) is optional , Default time is 1000ms
 ************************************************************************/
function bootBoxAlert(msg, time) {
  let delayTime = 5000;
  // The longer method of displaying a flash message.
  new joint.ui.FlashMessage({
    // title: 'Alert',
    type: "info",
    content: msg,
    closeAnimation: { delay: delayTime }
  }).open();

  // console.log("Message :" + msg);
  // bootbox.dialog({
  // 	message: '<p class="text-center text-success lead">' + msg + '</p>',
  // 	closeButton: false
  // });
  // closeBootBoxAlert(time);//close bootbox after 1 second - drag-drop
}

/************************************************************************
 * Custom BootBox alert --- Time(ms) is optional , Default time is 1000ms
 ************************************************************************/
function bootBoxDangerAlert(msg, time) {
  let delayTime = 5000;
  // The longer method of displaying a flash message.
  new joint.ui.FlashMessage({
    // title: 'Alert',
    type: "alert",
    content: msg,
    closeAnimation: { delay: delayTime }
  }).open();
}

/************************************************************************
 * Custom BootBox success flashmessage --- Time(ms) is optional , Default time is 1000ms
 ************************************************************************/
function bootBoxSuccessAlert(msg, time) {
  let delayTime = 5000;
  // The longer method of displaying a flash message.
  new joint.ui.FlashMessage({
    // title: 'Alert',
    type: "success",
    content: msg,
    closeAnimation: { delay: delayTime }
  }).open();
}

/************************************************************************
 * Custom Warning BootBox alert --- Time(ms) is optional , Default time is 1000ms
 ************************************************************************/
function bootBoxWarningAlert(msg, time) {
  let delayTime = 5000;
  new joint.ui.FlashMessage({
    // title: 'Warning',
    type: "warning",
    content: msg,
    closeAnimation: { delay: delayTime, duration: 5000 }
  }).open();

  // console.log("Message :" + msg);
  // bootbox.dialog({
  // 	message: '<p class="text-center text-danger lead">' + msg + '</p>',
  // 	closeButton: false
  // });
  // closeBootBoxAlert(time);//close bootbox after 1 second - drag-drop
}

/************************************************************************
 * Custom Warning BootBox alert --- Time(ms) is optional , Default time is 1000ms
 ************************************************************************/
function bootBoxNetworkStatus(msg, time) {
  console.log("Message :" + msg);
  //	bootbox.dialog({
  //	message: '<p class="text-center ">'+msg+'</p>',
  //	closeButton: false,
  //	className:'networkStatusBootBox'
  //	});

  //var div=`<div class="networkStatusBootBox"><p class="text-center">${msg}</p></div>`;
  //closeBootBoxAlert(time);//close bootbox after 1 second - drag-drop

  $("#networkStatusDiv").append(msg);
  $("#networkStatusDiv")
    .slideUp(300)
    .delay(800)
    .fadeIn(400)
    .remove();
}

/************************************************************************
 * Show success message on modal for ajax calls
 ************************************************************************/
var fShowSuccessMessage = function(msg) {
  console.log("fShowSuccessMessage()");
  var imgSrc = "images/success.png";
  $("#modalLoadingContent img").attr("src", imgSrc);
  $("#modalLoadingContent p").text(msg);
  disableCloseButton(false);
  // fadeOutModal();
};

/************************************************************************
 * Show failure message on modal for ajax calls
 ************************************************************************/
var fShowFailureMessage = function(msg) {
  console.log("fShowFailureMessage()");
  var imgSrc = "images/failure.png";
  $("#modalLoadingContent img").attr("src", imgSrc);
  $("#modalLoadingContent p").text(msg);
  disableCloseButton(false);
  //fadeOutModal();
};

/************************************************************************
 * Show Warning message on modal for ajax calls
 ************************************************************************/
var fShowWarningMessage = function(msg) {
  console.log("fShowWarningMessage()");
  var imgSrc = "images/warning.svg";
  $("#modalLoadingContent img").attr("src", imgSrc);
  $("#modalLoadingContent p").text(msg);
  disableCloseButton(false);
  //fadeOutModal();
};

/************************************************************************
 * Json Post Object Constructor
 ************************************************************************/
var jsonPostObject = function() {
  var obj = new Object();
  obj.NetworkInfo = new Object();
  obj.NetworkInfo.NetworkName = sessionStorage.getItem("NetworkName");
  obj.NetworkInfo.UserName = sessionStorage.getItem("UserName");
  obj.NetworkInfo.Topology = sessionStorage.getItem("Topology");
  return obj;
};

var jsonPostObjectMapSave = function() {
  // Update Node Connection Object
  // Contains nodeId to dir mapping
  setDegreeofNodes();
  setNodeConnections();

  var saveMapPostData = jsonPostObject();
  var jsonStr = JSON.stringify(graph);
  var graphStr = graph.toJSON();
  var jsonGraph = JSON.stringify(graphStr);

  //Save Map Image
  //paper.scaleContentToFit();
  var svg = document.getElementById("v-2");
  //svgEl.appendChild(svg);
  console.log("svg:", svg);
  var s = new XMLSerializer().serializeToString(svg);
  var encodedData = window.btoa(s);
  var imgURI = "data:image/svg+xml;base64," + encodedData;
  //commandManager.undo();

  //var w=window.open('about:blank','image from canvas');
  //w.document.write("<img src='"+imgURI+"' alt='from canvas'/>");
  saveMapPostData.MapImage = imgURI;

  graphStr = JSON.parse(JSON.stringify(graph));

  saveMapPostData.MapStr = graphStr;

  return saveMapPostData;
};

var isBrownFieldNetwork = function() {
  console.log(
    "isBrownFieldNetwork() => ",
    sessionStorage.getItem("NetworkState")
  );
  if (sessionStorage.getItem("NetworkState") == nptGlobals.BrownFieldStr)
    return true;
  else return false;
};

/************************************************************************
 * Check Flow Status of overall npt functions (Done / Not Done)
 ************************************************************************/
var fNptStateChange = function(state) {
  console.log("fNptStateChange()");
  var length = NptCurrentStateArr.length;
  for (var i = length - 1; i >= 0; i--) {
    //console.log(Object.getOwnPropertyNames(NptCurrentStateArr[i]));

    //Get property name of object
    var propertyName = Object.getOwnPropertyNames(NptCurrentStateArr[i])[0];

    //Make all state above this state invalid (0)
    if (state != propertyName) {
      NptCurrentStateArr[i][propertyName] = 0;
    } else {
      NptCurrentStateArr[i][state] = 1;
      break;
    }
  }

  //Final state after changes of NptStateArr
  console.log(NptCurrentStateArr);

  showNetworkStatus();
};

/************************************************************************
 * Show Flow Status of overall npt functions (Done / Not Done)
 ************************************************************************/
var showNetworkStatus = function(flag) {
  console.log("showNetworkStatus()");
  var template = `<h2 class="text-center"><strong>Network Status</strong></h2><hr><table class=" table table-bordered statusCheckTable ">
		<thead class="text-center">
		<tr>
		<th>Task</th>
		<th>Status</th>
		</tr>
		</thead>
		<tbody>`;

  // for (var i = 0; i < NptCurrentStateArr.length; i++) {
  //   //console.log(Object.getOwnPropertyNames(NptCurrentStateArr[i]));

  //   //Get property name of object
  //   var key = Object.getOwnPropertyNames(NptCurrentStateArr[i])[0];
  //   var value = NptCurrentStateArr[i][key];
  //   var taskTitle = fGetTaskname(key);
  //   template += `<tr>
  // 		<td>${taskTitle}</td>`;
  //   if (value == 1) template += `<td><img src="../images/checked.png"></td>`;
  //   else template += `<td><img src="../images/cancel.png"></td>`;
  //   template += `</tr>`;
  // }

  let doneImg = `<img src="../images/checked.png">`;
  let notDoneImg = `<img src="../images/cancel.png">`;

  template += `<tr>
			<td>Map Saved</td>`;
  if (nptGlobals.getMapSavedStatus()) {
    template += `<td>${doneImg}</td></tr>`;
  } else {
    template += `<td>${notDoneImg}</td></tr>`;
  }

  template += `<tr>
			<td>Circuit Saved</td>`;
  if (nptGlobals.getCircuitSavedStatus()) {
    template += `<td>${doneImg}</td></tr>`;
  } else {
    template += `<td>${notDoneImg}</td></tr>`;
  }

  template += `<tr>
			<td>RWA Saved</td>`;
  if (nptGlobals.getRwaExecutedStatus()) {
    template += `<td>${doneImg}</td></tr>`;
  } else {
    template += `<td>${notDoneImg}</td></tr>`;
  }

  template += `<tr>
			<td>Inventory Saved</td>`;
  if (nptGlobals.getInventoryGeneratedStatus()) {
    template += `<td>${doneImg}</td></tr>`;
  } else {
    template += `<td>${notDoneImg}</td></tr>`;
  }

  template += `<tr>
			<td>IP Scheme Saved</td>`;
  if (nptGlobals.getIpSchemeGeneratedStatus()) {
    template += `<td>${doneImg}</td></tr>`;
  } else {
    template += `<td>${notDoneImg}</td></tr>`;
  }

  template += `</tbody>
		</table>`;

  template +=
    "<p class='text-center text-danger'>Complete all above mentioned tasks to convert your network to brown field.</p>";

  if (flag) return template;

  bootBoxAlert(template, 2000);

  //$(".statusCheckTable tbody").empty().append(template);
  //$('#statusCheckModal').modal('show');
};

/************************************************************************
 * Show Flow Status of overall npt functions (Done / Not Done)
 ************************************************************************/
var fGetTaskname = function(key) {
  var taskName = "";
  switch (key) {
    case isMapSavedStr:
      taskName = "Map Save";
      break;
    case isCircuitSavedStr:
      taskName = "Circuit Save";
      break;
    case isRwaExecutedStr:
      taskName = "RWA Executed";
      break;
    case isInventoryGeneratedStr:
      taskName = "Inventory Generated";
      break;
  }
  return taskName;
};

/************************************************************************
 * BrownFieldConversion Check
 ************************************************************************/
var isBrownFieldConversionAllowed = function() {
  for (var i = 0; i < NptCurrentStateArr.length; i++) {
    //console.log(Object.getOwnPropertyNames(NptCurrentStateArr[i]));

    //Get property name of object
    var key = Object.getOwnPropertyNames(NptCurrentStateArr[i])[0];
    var value = NptCurrentStateArr[i][key];

    if (value == 0) return false;
  }
  return true;
};

/************************************************************************
 * Update Npt Current State Arr holding status of network for BF conversion
 ************************************************************************/
var updateNptCurrentStateArr = function(stateObj) {
  console.log(
    "updateNptCurrentStateArr() :: State Obj received from DB ",
    stateObj
  );
  console.log("NptCurStateArr", NptCurrentStateArr);
  for (var i = 0; i < NptCurrentStateArr.length; i++) {
    //console.log(Object.getOwnPropertyNames(NptCurrentStateArr[i]));
    //Get property name of object
    var key = Object.getOwnPropertyNames(NptCurrentStateArr[i])[0];
    NptCurrentStateArr[i][key] = stateObj[key];
    //console.log(stateObj[key])
  }
  console.log("After :: NptCurStateArr", NptCurrentStateArr);
};

/************************************************************************
 * Update Npt Current State Arr holding status of network for BF conversion
 ************************************************************************/
var setNetworkStateVariables = function(stateObj) {
  console.log(
    "setNetworkStateVariables() :: State Obj received from DB ",
    stateObj
  );

  let {
    isMapSaved,
    isCircuitSaved,
    isRwaExecuted,
    isInventoryGenerated,
    isIpSchemeGenerated
  } = stateObj;

  nptGlobals.setMapSaved(isMapSaved);
  nptGlobals.setCircuitSaved(isCircuitSaved);
  nptGlobals.setRwaExecuted(isRwaExecuted);
  nptGlobals.setInventoryGenerated(isInventoryGenerated);
  nptGlobals.setIpSchemeGenerated(isIpSchemeGenerated);

  // setMapSaved(stateObj.isMapSaved);
  // setCircuitSaved(stateObj.isCircuitSaved);
  // setRwaExecuted(stateObj.isRwaExecuted);
  // setInventoryGenerated(stateObj.isInventoryGenerated);
};

function authForm() {
  var emsSessionToken;

  var emsIp = $("#emsIpAddress").val();
  console.log("emsIp", emsIp);

  serverGetMessage(
    "http://" +
      emsIp +
      "/dwdmemsnbi/open/EmsSessionFactory/getEmsSession?user=cdot&password=cdot123&client=nmsuser203",
    true
  )
    .then(function(data) {
      console.log("Success Session Open Data:", data);

      fShowClientServerCommunicationModal("Updloading File to Ems Server... ");

      emsSessionToken = data.emsSessionToken;

      ///alert(emsSessionToken);

      if (data.responseCode === 200 || data.responseCode === 300) {
        /**Success Response*/

        /**File Upload */
        var fileData = new FormData($("#form_id")[0]);

        $.ajax({
          async: true,
          url: "http://" + emsIp + "/dwdmemsnbi/secure/configFile/upload ",
          method: "POST",
          processData: false,
          contentType: false,
          data: fileData,
          headers: {
            "Cache-Control": "no-cache",
            Accept: "application/json",
            Authorization: data.emsSessionToken
          },

          success: function(data) {
            console.log("Success File Upload  " + JSON.stringify(data));
            fShowSuccessMessage("File Uploaded to EMS Server Successfully");
          },

          error: function(err) {
            console.log("Error File Upload " + err);
            fShowFailureMessage("File Uploaded to EMS Server failed");
          },

          complete: function() {
            ///alert(emsSessionToken)
            /**Close the session */
            $.ajax({
              async: true,
              url: "http://" + emsIp + "/dwdmemsnbi/secure/Session/endSession",
              method: "POST",
              dataType: "JSON",
              headers: {
                "Cache-Control": "no-cache",
                Accept: "application/json",
                "Content-Type": "application/json",
                Authorization: emsSessionToken
              },

              success: function(data) {
                console.log("End Session Success " + JSON.stringify(data));
              },

              error: function(err) {
                console.log("End Session Error! " + err);
              }
            });
          }
        });
      }
    })
    .catch(function(e) {
      console.log("Fail Session Open Data ", e);
    });
}

/***************************************************************************/
/**         fShowClientServerCommunicationModal                                              **/
/***************************************************************************/
function fShowClientServerCommunicationModal(text) {
  //console.log("Inside fShowClientServerCommunicationModal");
  $("#loadingModal").keydown(false);
  $("#loadingModal").modal("show");
  $("#modalLoadingContent img").attr("src", "images/loading.gif");
  $("#modalLoadingContent p").text(text);
  disableCloseButton(true);
}
