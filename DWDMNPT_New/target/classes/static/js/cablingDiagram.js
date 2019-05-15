var CablingJsonData = {
  cablingViewData: [
    /*{
							'NodeKey' : '1_1',
							'EquipmentId' : 30,
							'CordType' : 'LC/UPC-LC/UPC, 2M LONG',
							'End1CardType' : 'WSS2x1x9',
							'End1Location' : '1_1_12',
							'End1Port' : 'FromEdfa',
							'End2CardType' : 'EDFA ARRAY',
							'End2Location' : '1_2_10',
							'End2Port' : 'ToWss',
							'Length' : 1,
							'DirectionEnd1' : 'se'
						},
						{
							'NodeKey' : '1_1',
							'EquipmentId' : 30,
							'CordType' : 'LC/UPC-LC/UPC, 2M LONG',
							'End1CardType' : 'WSS2x1x9',
							'End1Location' : '1_1_12',
							'End1Port' : 'ToEdfa',
							'End2CardType' : 'EDFA ARRAY',
							'End2Location' : '1_2_10',
							'End2Port' : 'FromWss',
							'Length' : 1,
							'DirectionEnd1' : 'se'
						},
						{
							'NodeKey' : '1_1',
							'EquipmentId' : 30,
							'CordType' : 'LC/UPC-LC/UPC, 2M LONG',
							'End1CardType' : 'WSS2x1x9',
							'End1Location' : '1_1_2',
							'End1Port' : 'FromEdfa',
							'End2CardType' : 'EDFA ARRAY',
							'End2Location' : '1_2_10',
							'End2Port' : 'ToWss',
							'Length' : 1,
							'DirectionEnd1' : 'ne'
						},						
		            	{
		            		'NodeKey' : '1_4',
		            		'EquipmentId' : 35,
		            		'CordType' : 'LC/APC-LC/PC, 2M LONG',
		            		'End1CardType' : 'PA/BA',
		            		'End1Location' : '1_2_14',
		            		'End1Port' : 'SUPY_DROP',
		            		'End2CardType' : 'SUPY',
		            		'End2Location' : '1_2_7',
		            		'End2Port' : 'Rx',
		            		'Length' : 1,
		            		'DirectionEnd1' : 'north'
		            	},		            	
		            	{
		            		'NodeKey' : '1_4',
		            		'EquipmentId' : 38,
		            		'CordType' : 'CAT6, 24 AWG UTP PATCHCORD WITH RJ-45 PLUG AT ENDS. 10MTR',
		            		'End1CardType' : 'CSCC',
		            		'End1Location' : '6',
		            		'End1Port' : 'ToEms',
		            		'End2CardType' : '',
		            		'End2Location' : '',
		            		'End2Port' : '',
		            		'Length' : 1,
		            		'DirectionEnd1' : ''
		            	},
		            	{
		            		'NodeKey' : '1_4',
		            		'EquipmentId' : 38,
		            		'CordType' : 'CAT6, 24 AWG UTP PATCHCORD WITH RJ-45 PLUG AT ENDS. 10MTR',
		            		'End1CardType' : 'CSCC',
		            		'End1Location' : '6',
		            		'End1Port' : 'ToLct',
		            		'End2CardType' : '',
		            		'End2Location' : '',
		            		'End2Port' : '',
		            		'Length' : 1,
		            		'DirectionEnd1' : ''
		            	},
		            	{
		            		'NodeKey' : '1_4',
		            		'EquipmentId' : 39,
		            		'CordType' : 'CAT6, 24 AWG UTP PATCHCORD WITH RJ-45 PLUG AT ENDS. 3MTR',
		            		'End1CardType' : 'CSCC',
		            		'End1Location' : '1_2_6',
		            		'End1Port' : 'ToMpc',
		            		'End2CardType' : 'MPC',
		            		'End2Location' : '1_1_6',
		            		'End2Port' : 'FromCscc',
		            		'Length' : 1,
		            		'DirectionEnd1' : ''
		            	},
		            	{
		            		'NodeKey' : '1_4',
		            		'EquipmentId' : 39,
		            		'CordType' : 'CAT6, 24 AWG UTP PATCHCORD WITH RJ-45 PLUG AT ENDS. 3MTR',
		            		'End1CardType' : 'CSCC',
		            		'End1Location' : '1_2_6',
		            		'End1Port' : 'ToMpc',
		            		'End2CardType' : 'MPC',
		            		'End2Location' : '1_1_9',
		            		'End2Port' : 'FromCscc',
		            		'Length' : 1,
		            		'DirectionEnd1' : ''
		            	},
		            	{
		            		'NodeKey' : '1_4',
		            		'EquipmentId' : 39,
		            		'CordType' : 'CAT6, 24 AWG UTP PATCHCORD WITH RJ-45 PLUG AT ENDS. 3MTR',
		            		'End1CardType' : 'CSCC',
		            		'End1Location' : '1_2_6',
		            		'End1Port' : 'ToMpc',
		            		'End2CardType' : 'MPC',
		            		'End2Location' : '1_3_6',
		            		'End2Port' : 'FromCscc',
		            		'Length' : 1,
		            		'DirectionEnd1' : ''
		            	}*/
  ]
};

$("#viewCablingDiagram").on("click", function() {
  if (!nptGlobals.getInventoryGeneratedStatus())
    bootBoxDangerAlert(
      "Generate Inventory first in order to get Cabling Information."
    );
  else {
    // $(".modalBodyCablingContentPanelBomTable tbody").empty().append('<img class="img-responsive loadGif" src="../images/load.gif">');
    $("#cablingModal").modal({ backdrop: "static", keyboard: false }); //disable click on black area/ esc key $('#cablingModal').modal({ backdrop: 'static', keyboard: false });//disable click on black area/ esc key

    overlayOn("Fetching Data", ".body-overlay");
    cablingDbAjaxCall();
    //setTimeout(finitializeCablingView,1000);
    //finitializeCablingView();
  }
});

$("#CablingDiagramPrintBtn").on("click", function() {
  console.log("Print Clicked in Connectivity Table Modal");
  var printableJsonData = fGetPrintableJsonDataFromTable(
    ".modalBodyCablingContentPanelBomTable"
  );
  //printJS({printable: printableJsonData, properties: ['S.No.', 'NodeId','Equipment','Chord Type', 'Part Code','Source Card Type','Source Location','Source port','Target Card Type','Target Location','Target port','Length','Direction'], type: 'json'})

  var columns = printableJsonData.Header;
  /* [
	{title: "S.No.", dataKey: "S.No."},
    {title: "NodeId", dataKey: "NodeId"}, 
    {title: "Equipment", dataKey: "Equipment"},
    {title: "Chord Type", dataKey: "Chord Type"},
    {title: "Part Code", dataKey: "Part Code"}, 
    {title: "Source Card Type", dataKey: "Source Card Type"},
    {title: "Source Location", dataKey: "Source Location"},
    {title: "Source Port", dataKey: "Source Port"}, 
    {title: "Target Card Type", dataKey: "Target Card Type"},
    {title: "Target Location", dataKey: "Target Location"},
    {title: "Target Port", dataKey: "Target Port"},
    ];*/
  var rows = printableJsonData.Body;
  var fileName = "ConnectivityTable_" + sessionStorage.getItem("NetworkName");
  fSaveAsPdfFromJsonData(columns, rows, fileName);
});

/***************************************************************************/
/**         Function to fetch data from
 *          html table and return it as
 *          json
 *          Input: Table selector (Class/Id)  with ./# included           **/
/***************************************************************************/
var fGetPrintableJsonDataFromTable = function(tableSelector) {
  var printableJsonData = {},
    Header = [],
    Body = [],
    $headers = $(tableSelector + " th");
  console.log("Headers Obj ::", $headers);

  $headers.each(function(cellIndex) {
    //console.log($($(this)[cellIndex]).html());
    //console.log("$headers::"+$(this).html()=="");
    Header[cellIndex] = {};
    Header[cellIndex]["title"] = $($headers[cellIndex]).html();
    Header[cellIndex]["dataKey"] = $($headers[cellIndex]).html();
  });

  //When First Column is Label Tag , Remove first header
  if (Header[0]["title"] == "" && Header[0]["dataKey"] == "") Header.shift();

  console.log("Header Array :", Header);
  $(tableSelector + " tbody tr").each(function(index) {
    //console.log("New Client Row",$(this));
    var $cells = $(this).find("td");
    Body[index] = {};
    $cells.each(function(cellIndex) {
      //If its not a GF/BF label column
      if ($(this).has("img").length == 0)
        Body[index][$($headers[cellIndex]).html()] = $(this).html();
    });
  });

  printableJsonData.Body = Body;
  printableJsonData.Header = Header;
  console.log("Returning printableJsonData:", printableJsonData);
  return printableJsonData;
};

/***************************************************************************/
/**         Save as Pdf from json data 
 *          Input: Columns - Header Fields , Rows - Content Object array   
/***************************************************************************/
var fSaveAsPdfFromJsonData = function(columns, rows, fileName) {
  // Only pt supported (not mm or in)

  var PdfPropertyObj = fGetPdfSpecificProperties(columns, rows);
  var pdf = new jsPDF("p", "pt");
  pdf.setFontSize(10);
  pdf.setTextColor(100);
  pdf.setFontType("bold");

  if (fileName.includes("BOM")) {
    console.log("BOM file download.....");
    pdf.text(
      100,
      15,
      `Network Name : ${sessionStorage.getItem("NetworkName")}`
    );

    pdf.text(
      315,
      15,
      "Total Cost : " + sessionStorage.getItem("TotalCost") + " INR"
    );

    pdf.text(
      100,
      35,
      "Total Power : " + sessionStorage.getItem("TotalPower") + " Watt"
    );
    pdf.text(
      315,
      35,
      "Typical Power : " + sessionStorage.getItem("TypicalPower") + " Watt"
    );

    //	let nodeName=fileName.split("_")[4];
    //	nodeName=nodeName.replace("Node","");
    //	console.log("NodeName",nodeName);
    //	if(nodeName!='All')
    //		pdf.text(420,15, "NodeId : "+nodeName+" $");

    dottedLine(pdf, 0, 50, 700, 50, 2);
  }
  pdf.autoTable(columns, rows, {
    theme: "grid",
    startY: 60,
    /*tableWidth:'auto',*/
    showHeader: "everyPage",
    margin: 25,
    styles: {
      // overflow: 'ellipsize',
      fontSize: PdfPropertyObj.FontSize,
      halign: "center",
      valign: "middle",
      //	      rowHeight: 300,
      columnWidth: PdfPropertyObj.ColWidth /*84*/,
      pageBreak: "avoid"
    },
    headerStyles: {
      //columnWidth: 50,
      //    	fontSize: DefaultFontSize,
      fontStyle: "bold",
      //cellPadding: ,
      overflow: "linebreak"
      //overflow: 'auto',
    },
    bodyStyles: {
      fontStyle: "normal",
      overflow: "linebreak"
      //  lineWidth: 20,
      //   columnWidth: 150,
      //      fontSize: DefaultFontSize,
      //		      rowHeight: 300,
      //columnWidth: 200
    }
    //	    columnStyles: {
    //	        1: {columnWidth: 10},
    //	        2:{columnWidth:30},
    //	        3: {columnWidth: 50},
    //	        4: {columnWidth: 50},
    //	        5:{columnWidth:50},
    //	        6: {columnWidth: 50},
    //	        7: {columnWidth: 50}
    //	       }
    //	    createdCell: function (cell, data) {
    //
    //	    },
    //	    columnStyles: {
    //	     columnWidth: '200',rowHeight:'100'
    //	    }
  });
  //pdf.autoTable(columns, rows);
  pdf.save(fileName + ".pdf");

  //	 var doc = new jsPDF();
  //	    doc.text("From HTML", 14, 16);
  //	    var elem = document.getElementById("CablingContentPanelBomTable");
  //	    var res = doc.autoTableHtmlToJson(elem);
  //	    doc.autoTable(res.columns, res.data, {startY: 20});
  //	    doc.save('table.pdf');
};

/**
 * Draws a dotted line on a jsPDF doc between two points.
 * Note that the segment length is adjusted a little so
 * that we end the line with a drawn segment and don't
 * overflow.
 */
function dottedLine(doc, xFrom, yFrom, xTo, yTo, segmentLength) {
  // Calculate line length (c)
  var a = Math.abs(xTo - xFrom);
  var b = Math.abs(yTo - yFrom);
  var c = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));

  // Make sure we have an odd number of line segments (drawn or blank)
  // to fit it nicely
  var fractions = c / segmentLength;
  var adjustedSegmentLength =
    Math.floor(fractions) % 2 === 0
      ? c / Math.ceil(fractions)
      : c / Math.floor(fractions);

  // Calculate x, y deltas per segment
  var deltaX = adjustedSegmentLength * (a / c);
  var deltaY = adjustedSegmentLength * (b / c);

  var curX = xFrom,
    curY = yFrom;
  while (curX <= xTo && curY <= yTo) {
    doc.line(curX, curY, curX + deltaX, curY + deltaY);
    curX += 2 * deltaX;
    curY += 2 * deltaY;
  }
}

function fConverttoRupees(costInDollar) {
  let dollarRate = 71;
  let costInRupees = costInDollar * parseInt(dollarRate);
  costInRupees = costInRupees.toString();
  let lastThree = costInRupees.substring(costInRupees.length - 3);
  let otherNumbers = costInRupees.substring(0, costInRupees.length - 3);
  if (otherNumbers != "") lastThree = "," + lastThree;
  let res = otherNumbers.replace(/\B(?=(\d{2})+(?!\d))/g, ",") + lastThree;
  return res;
}

function fFormatRupees(costInRupees) {
  costInRupees = costInRupees.toString();
  let lastThree = costInRupees.substring(costInRupees.length - 3);
  let otherNumbers = costInRupees.substring(0, costInRupees.length - 3);
  if (otherNumbers != "") lastThree = "," + lastThree;
  let res = otherNumbers.replace(/\B(?=(\d{2})+(?!\d))/g, ",") + lastThree;
  return res;
}

var fGetPdfSpecificProperties = function(columns, rows) {
  var PropertyObj = new Object();
  var DefaultFontSize = 8,
    DefaultPageWidth = 545;
  if (columns.length <= 4) {
    DefaultFontSize = 11;
  } else if (columns.length <= 6) {
    DefaultFontSize = 10;
  } else if (columns.length <= 8) {
    DefaultFontSize = 9;
  }

  PropertyObj.ColWidth = Number(DefaultPageWidth) / Number(columns.length);
  PropertyObj.FontSize = DefaultFontSize;
  console.log(
    "fGetPdfSpecificProperties returning: fontSize->" +
      DefaultFontSize +
      " ColWidth->" +
      PropertyObj.ColWidth
  );
  return PropertyObj;
};

$("#cablingModal .modal-dialog").resizable({
  //alsoResize: ".modal-dialog",
  minHeight: 300,
  minWidth: 300
});
$("#cablingModal .modal-dialog").draggable();

$("#cablingModal").on("show.bs.modal", function() {
  $(this)
    .find(".modal-body")
    .css({
      "max-height": "100%"
    });
});

function cablingDbAjaxCall() {
  let postJsonData = jsonPostObject();
  console.log("cablingDbAjaxCall Post Obj ::", postJsonData);

  serverPostMessage("getCablingInfoJsonDataRequest", postJsonData)
    .then(function(data) {
      overlayOff("Fetch Success", ".body-overlay");
      CablingJsonData = data;
      console.log("success with data:", data);
      finitializeCablingView();
    })
    .catch(function(e) {
      overlayOff("Fetch Failure", ".body-overlay");
      console.log("fail" + e);
    });
}

function finitializeCablingView() {
  var nodeSet = new Set();
  var cardTypeSet = new Set();
  var cablingInfoArr = CablingJsonData.cablingViewData;
  var nodeId;
  var nodejsonArray, cardTypejsonArray;

  $("#cablingNodeId").empty();
  $("#cablingCardType").empty();

  //Fetch unique nodeId and CardType from Complete Array
  for (var i = 0; i < cablingInfoArr.length; i++) {
    //		nodeKey=cablingInfoArr[i].NodeKey.split("_");
    //		nodeId=nodeKey[1];
    nodeId = cablingInfoArr[i].NodeId;
    nodeSet.add(nodeId);
    cardTypeSet.add(cablingInfoArr[i].End1CardType);
    if (String(cablingInfoArr[i].End2CardType).length > 0)
      cardTypeSet.add(cablingInfoArr[i].End2CardType);
  }
  //console.log(nodeSet);

  nodejsonArray = Array.from(nodeSet); // array of unique id's
  cardTypejsonArray = Array.from(cardTypeSet); //aray of unique End1CardType

  $("#cablingNodeId").append(`<option value="All">All</option>`);
  for (var i = 0; i < nodejsonArray.length; i++)
    $("#cablingNodeId").append(
      `<option value="${nodejsonArray[i]}">${nodejsonArray[i]}</option>`
    );

  $("#cablingCardType").append(`<option value="All">All</option>`);
  for (var i = 0; i < cardTypejsonArray.length; i++)
    $("#cablingCardType").append(
      `<option value="${cardTypejsonArray[i]}">${cardTypejsonArray[i]}</option>`
    );

  appendCablingViewDataToTable(cablingInfoArr);
}

/***************************************************************************/
/**         Function to fetch filtered data based
 *          on Inputs (NodeId and Cardtype ) and
 *          return Array                                                  **/
/***************************************************************************/
function fGetFilteredCablingDataFromJson(nodeId, cardType) {
  var cablingInfoArr = CablingJsonData.cablingViewData;
  let resultArray;
  if (nodeId == "All" && cardType == "All") {
    console.log("Returning All data from Cabling View Data");
    resultArray = cablingInfoArr;
  } else if (nodeId == "All") {
    console.log("Else case when NodeId=='All' ");
    //Fetch unique nodeId and CardType from Complete Array
    resultArray = cablingInfoArr.filter(obj => {
      return cardType == obj.End1CardType || cardType == obj.End2CardType;
    });
  } else if (cardType == "All") {
    console.log("Else case when CardType=='All'");
    //Fetch unique nodeId and CardType from Complete Array
    resultArray = cablingInfoArr.filter(obj => {
      return nodeId == obj.NodeId;
    });
  } else {
    console.log("Else case when NodeId!='All' and CardType!='All'");
    //Fetch unique nodeId and CardType from Complete Array
    resultArray = cablingInfoArr.filter(obj => {
      return (
        nodeId == obj.NodeId &&
        (cardType == obj.End1CardType || cardType == obj.End2CardType)
      );
    });
  }
  return resultArray;
}

/***************************************************************************/
/**         Function to append data to the cabling
 *          view table from array passed as parameter                     **/
/***************************************************************************/
function appendCablingViewDataToTable(cablingInfoArr) {
  $("#modalBodyCablingContentPanelBom").fadeOut("fast");
  if (cablingInfoArr.length != 0) {
    var nodeId,
      str = "",
      nodekey = [];
    var header = `<tr>
		    <th>S.No.</th>
			<th>NodeId</th>
			<th>Equipment</th>
			<th>Chord Type</th>
			<th>Part Code</th>
			<th>Source Card Type</th>
			<th>Source Location</th>
			<th>Source Port</th>
			<th>Target Card Type</th>
			<th>Target Location</th>
			<th>Target Port</th>			
			</tr>`;
    //<th>Direction</th>
    $(".modalBodyCablingContentPanelBomTable thead")
      .empty()
      .append(header);

    for (var i = 0; i < cablingInfoArr.length; i++) {
      //			nodeKey=cablingInfoArr[i].NodeKey.split("_");
      //			console.log(nodeKey);
      //			nodeId=nodeKey[1];
      nodeId = cablingInfoArr[i].NodeId;
      str += `<tr>
			    <td>${i + 1}</td>
				<td>${nodeId}</td>
				<td>${cablingInfoArr[i].EquipmentId}</td>
				<td>${cablingInfoArr[i].CordType}</td>
				<td>${cablingInfoArr[i].PartNo}</td>
				<td>${cablingInfoArr[i].End1CardType}</td>
				<td>${cablingInfoArr[i].End1Location}</td>
				<td>${cablingInfoArr[i].End1Port}</td>
				<td>${cablingInfoArr[i].End2CardType}</td>
				<td>${cablingInfoArr[i].End2Location}</td>
				<td>${cablingInfoArr[i].End2Port}</td>
				</tr>`;
      //			/<td>${cablingInfoArr[i].DirectionEnd1}</td>
      //console.log(str);
    }
    $(".modalBodyCablingContentPanelBomTable tbody")
      .empty()
      .append(str);
  } else {
    str = "No matched data found for given Node Id and Card Type";
    $(".modalBodyCablingContentPanelBomTable thead").empty();
    $(".modalBodyCablingContentPanelBomTable tbody")
      .empty()
      .append(str);
  }

  $("#modalBodyCablingContentPanelBom").fadeIn("slow");
}

/***************************************************************************/
/**         Node Id change trigger for bom data                           **/
/***************************************************************************/

//$("#cablingModalFormFilterTrigger").on('click',function(){
////	e.preventDefault();
//
//	var nodeId=$("#cablingNodeId").val();
//	var cardType=$("#cablingCardType").val();
//
//	$("#modalBodyCablingContentPanelBom").slideUp();
//	var filteredDataArr=fGetFilteredCablingDataFromJson(nodeId,cardType);
//
//	appendCablingViewDataToTable(filteredDataArr);
//
//	console.log("nodeId ::"+nodeId);
//	console.log("cardType ::"+cardType);
//
//});

$("#cablingNodeId").on("change", function() {
  //	e.preventDefault();

  var nodeId = $("#cablingNodeId").val();
  var cardType = $("#cablingCardType").val();

  // $("#modalBodyCablingContentPanelBom").slideUp();
  var filteredDataArr = fGetFilteredCablingDataFromJson(nodeId, cardType);

  appendCablingViewDataToTable(filteredDataArr);

  // console.log("nodeId ::" + nodeId);
  // console.log("cardType ::" + cardType);
});

$("#cablingCardType").on("change", function() {
  //	e.preventDefault();

  var nodeId = $("#cablingNodeId").val();
  var cardType = $("#cablingCardType").val();

  // $("#modalBodyCablingContentPanelBom").slideUp();
  var filteredDataArr = fGetFilteredCablingDataFromJson(nodeId, cardType);

  appendCablingViewDataToTable(filteredDataArr);

  // console.log("nodeId ::" + nodeId);
  // console.log("cardType ::" + cardType);
});
