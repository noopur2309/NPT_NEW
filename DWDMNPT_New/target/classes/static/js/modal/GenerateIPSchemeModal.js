/**
 * @author hp
 *
 * @date   3rd Feb, 2017
 * @brief  JS to Generate and View IP scheme
 * @desc   Flow 1) Generate IP 			  : Get Private IP Block -> Async Call to Controller -> Return Success/Failure
 *         Flow 2) View IP   			  : Async Call to Controller -> DB Call -> Return IP Scheme
 *         Flow 3) Generate Config File   : For each node in xml format; Async Call to Controller -> DB Call -> Return IP Scheme
 */

/**Flow 1 : Events*/
$("#generateMapIpSchemeId").click(function() {
  //alert("Going to Take Generate IP scheme")
  //bootBoxAlert("Generate IP Scheme Modal");

  if (nptGlobals.getMapSavedStatus()) {
    bootbox.prompt({
      title:
        "Please Provide the Private IP Block [In which IP need to be Generated]",
      inputType: "select",
      message: '<p><i class="fa fa-spin fa-spinner"></i> Loading...</p>',

      inputOptions: [
        {
          text: "Choose one...",
          value: ""
        },
        {
          text: "24-bit block [10.5.0.0 - 10.255.255.255]",
          value: "1"
        },
        {
          text: "20-bit block [172.16.0.0 - 172.31.255.255]",
          value: "2"
        },
        {
          text: "16-bit block [192.168.0.0 - 192.168.255.255]",
          value: "3"
        }
      ],
      callback: function(result) {
        console.log("IP scheme Selected :" + result);
        if (result == null) {
          console.log("Going to Validate the Modal");
          return true;
        } else if (result == "") {
          bootBoxAlert("First Select IP Scheme to Generate!");
          return false;
        } else {
          ipSchemeGenerationRequest(result);
          return true;
        }
      }
    });
  } else {
    bootBoxAlert("Please Create and Save Network First!");
  }
});

function ipSchemeGenerationRequest(schemeBlock) {
  console.log("ipSchemeGenerationRequest with BlockType :- ", schemeBlock);
  //  var data = {"blockType":schemeBlock};

  //   overlayOn("Generating IP Scheme for network.",".body-overlay");
  fShowClientServerCommunicationModal("Generating IP Scheme for network.");
  let postJsonData = jsonPostObject();
  postJsonData.blockType = schemeBlock;

  console.log("ipSchemeGenerationRequest Post Obj ::", postJsonData);

  serverPostMessage("ipSchemeGenerationRequest", postJsonData)
    .then(function(data) {
      fShowSuccessMessage("IP Scheme generated successfully");
      nptGlobals.setIpSchemeGenerated(true);
    })
    .catch(function(e) {
      nptGlobals.setIpSchemeGenerated(false);
      fShowFailureMessage("IP Scheme couldn't be generated.");
      console.log("fail", e);
    });
}

/**Flow 2 : Events*/
$("#viewIpSchemeId").click(function() {
  console.log("Inside View IP shceme");

  overlayOn("Generating IP Scheme for network.", ".body-overlay");
  let postJsonData = jsonPostObject();
  console.log("ipSchemeGenerationRequest Post Obj ::", postJsonData);

  serverPostMessage("viewIpSchemeDataRequest", postJsonData)
    .then(function(data) {
      overlayOff("IP Scheme generated successfully", ".body-overlay");
      var template = "";
      template += `<div class="tabbable" id="ipSchemeViewModal"> <!-- Only required for left/right tabs -->
	    		<ul class="nav nav-tabs">
	    		<li class="active"><a href="#tab1" data-toggle="tab">NODE IP-Scheme</a></li>
	    		<li><a href="#tab2" data-toggle="tab">LINK IP-Scheme</a></li>
	    		</ul>
	    		<div class="tab-content">
	    		<div class="tab-pane active" id="tab1">
	    		<table class="footable table" id="nodeIpv4TableId">
	    		<thead>					
	    		<th>Network Id</th>
	    		<th>Node Id</th>
	    		<th>Lct IP</th>
	    		<th>Router IP</th>
	    		<th>SCP IP</th>
	    		<th>MCP IP</th>						
	    		</thead>
	    		<tbody>
	    		</tbody></table>    	  		
	    		</div>
	    		<div class="tab-pane" id="tab2">	    		
	    		<table class="footable table" id="linkIpv4TableId">
	    		<thead>					
	    		<th>Network Id</th>
	    		<th>Link Id</th>
	    		<th>Link IP</th>
	    		<th>Source IP</th>
	    		<th>Destination IP</th>	    								
	    		</thead>
	    		<tbody>
	    		</tbody></table> 
	    		</div>
	    		</div>
	    		</div>`;

      /**
       * Get Data from JSON Arrays : 1) IpSchemeNode
       * 							   2)IpSchemeLink
       */

      var templateNodeBody = "";

      var jsonNodeArr = data.IpSchemeNode;
      console.log("jsonNodeArr length :- " + jsonNodeArr.length);

      for (i = 0; i < jsonNodeArr.length; i++) {
        /**Node Data*/
        console.log(
          "Inside For :- " +
            i +
            ", " +
            jsonNodeArr[i].NetworkId +
            ", " +
            jsonNodeArr[i].NodeId +
            ", " +
            jsonNodeArr[i].LctIp +
            ", " +
            jsonNodeArr[i].RouterIp +
            ", " +
            jsonNodeArr[i].ScpIp +
            ", " +
            jsonNodeArr[i].McpIp
        );
        templateNodeBody += "<tr><td>" + jsonNodeArr[i].NetworkId + "</td>";
        templateNodeBody += "<td>" + jsonNodeArr[i].NodeId + "</td>";
        templateNodeBody += "<td>" + jsonNodeArr[i].LctIp + "</td>";
        templateNodeBody += "<td>" + jsonNodeArr[i].RouterIp + "</td>";
        templateNodeBody += "<td>" + jsonNodeArr[i].ScpIp + "</td>";
        templateNodeBody += "<td>" + jsonNodeArr[i].McpIp + "</td>";
        templateNodeBody += "</tr>";
      }

      var templateLinkBody = "";

      var jsonLinkArr = data.IpSchemeLink;
      console.log("jsonLinkArr length :- " + jsonLinkArr.length);

      for (i = 0; i < jsonLinkArr.length; i++) {
        /**Link Data*/
        console.log(
          "Inside For :- " +
            i +
            ", " +
            jsonLinkArr[i].NetworkId +
            ", " +
            jsonLinkArr[i].LinkId +
            ", " +
            jsonLinkArr[i].LinkIp +
            ", " +
            jsonLinkArr[i].SrcIp +
            ", " +
            jsonLinkArr[i].DestIp
        );
        templateLinkBody += "<tr><td>" + jsonLinkArr[i].NetworkId + "</td>";
        templateLinkBody += "<td>" + jsonLinkArr[i].LinkId + "</td>";
        templateLinkBody += "<td>" + jsonLinkArr[i].LinkIp + "</td>";
        templateLinkBody += "<td>" + jsonLinkArr[i].SrcIp + "</td>";
        templateLinkBody += "<td>" + jsonLinkArr[i].DestIp + "</td>";
        templateLinkBody += "</tr>";
      }

      /**Inject table data for active class*/
      /*    	console.log("template :- "+template);
	    	console.log ("Final templateNodeBody body :- " + templateNodeBody);
	    	console.log ("Final templateLinkBody body :- " + templateLinkBody);
	    	console.log ("Final template :- " + template + " -- And -- " + $((template)));*/

      bootbox.dialog({
        title: "IP Scheme [Node & Link]",
        message: $(template)
        /*	    	    draggable: true,*/
      });

      $("#nodeIpv4TableId tbody").append(templateNodeBody);
      $("#linkIpv4TableId tbody").append(templateLinkBody);

      $(".modal-dialog").resizable({
        //alsoResize: ".modal-dialog",
        minHeight: 300,
        minWidth: 300
      });
      $(".modal-dialog").draggable();

      console.log("success" + data);
      //alert("Successfully Executed IP Scheme Data Fetch ..");
    })
    .catch(function(e) {
      overlayOff("IP Scheme couldn't be generated.", ".body-overlay");
      console.log("fail", e);
    });
});
