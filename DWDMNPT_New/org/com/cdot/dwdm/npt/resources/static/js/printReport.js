/**
 * @brief Single File to Handle all Model Report Printing Operations
 * @date  11:43 AM : Jan 19, 2018
 */

var currentSelectedTab = "modalBodyviewReportTabMenuBomTrigger",
  currentSelectedNode = "1",
  currentTitle = "Bill of Material for System",
  currentJsonObj = {};

$("#ViewReportSaveAsXlsBtn").click(function() {
  loadExcelJsonEventFunction();
  //   console.log("Print Excel");
  console.log("currentSelectedNode :", currentSelectedNode);
  //   console.log(
  //     "currentJsonObj:",
  //     currentJsonObj,
  //     " currentTitle:",
  //     currentTitle
  //   );
  let headers = {};

  console.log("Keys ", Object.keys(currentJsonObj[0]));

  var fileTitle = "BOM_NODE_" + currentSelectedNode; // or 'my-unique-title'
  for (var key in currentJsonObj[0]) {
    headers[key] = key.toString().toUpperCase();
  }

  // headers["NodeId"] = "NODEID";
  // headers["StationName"] = "STATIONNAME";
  // headers["SiteName"] = "SITENAME";
  // headers["Name"] = "NAME";
  // headers["Quantity"] = "QUANTITY";
  // headers["Price"] = "PRICE";
  // headers["TotalPrice"] = "TOTALPRICE";
  // headers["Unit Power"] = "Unit Power".toUpperCase();
  // headers["Unit TypicalPower"] = "Unit TypiclPower".toUpperCase();
  // headers["PowerConsumption"] = "PowerConsumption".toUpperCase();
  // headers["TypicalPowerConsumption"] = "TypicalPowerConsumption".toUpperCase();

  let csvData = _.clone(currentJsonObj);

  csvData = csvData.map(item => {
    item.Name = item.Name.replace(/,/g, "");
    return item;
  });

  console.log("headers", headers, "csvData", csvData);

  exportCSVFile(headers, csvData, fileTitle); // call the exportCSVFile() function to process the JSON and trigger the download
  //   printExcelFile(currentJsonObj, currentTitle, true);
});

/**
 *
 * To prepare json data for Excel Print as per the input
 *
 * Note: to find out the active id when its not in $(this) =>
 *       $("#modalBodyviewReportTabMenu .btn.activeFocus")[0].id
 */
loadExcelJsonEventFunction = function() {
  currentSelectedNode = $("#viewReportNodeId").val();
  currentSelectedTab = $(
    "#viewReportModal #modalBodyviewReportTabMenu li.active"
  ).attr("id");
  console.log(
    "currentSelectedTab activeFocus id::",
    currentSelectedTab,
    " currentSelectedNode::",
    currentSelectedNode
  );

  switch (currentSelectedTab) {
    case "modalBodyviewReportTabMenuMPNInfoConfiTrigger":
      currentTitle = "MPN Information For NodeId : " + currentSelectedNode;

      if (currentSelectedNode == "All")
        /**case for all*/
        currentJsonObj = dummyJson.MPNInformation;
      /**Particular node selected*/ else
        currentJsonObj = fGetNodeWiseDataViewReport(
          dummyJson.MPNInformation,
          currentSelectedNode
        );

      break;

    case "modalBodyviewReportTabMenugenerateNodeIPTrigger":
      currentTitle = "Ip Scheme For Nodes";
      currentJsonObj = dummyJson.IpSchemeNodeData;

      break;

    case "modalBodyviewReportTabMenugenerateLinkIPTrigger":
      currentTitle = "Ip Scheme For Links";
      currentJsonObj = dummyJson.IpSchemeLinkData;

      break;

    case "modalBodyviewReportTabMenuBomTrigger":
      currentTitle =
        "Bill of Material for System For NodeId : " + currentSelectedNode;

      if (currentSelectedNode == "All")
        currentJsonObj = dummyJson.bomViewData.NetworkWiseData;
      else
        currentJsonObj = fGetNodeWiseDataViewReport(
          dummyJson.bomViewData.NodeWiseData,
          currentSelectedNode
        );

      break;

    case "modalBodyviewReportTabMenuwavelengthConfiTrigger":
      currentTitle =
        "Wavelength Configuration For NodeId : " + currentSelectedNode;

      if (currentSelectedNode == "All")
        currentJsonObj = dummyJson.wavelengthConfiguration;
      else
        currentJsonObj = fGetNodeWiseDataViewReport(
          dummyJson.wavelengthConfiguration,
          currentSelectedNode
        );

      break;

    case "modalBodyviewReportTabMenuInventoryTrigger":
      currentTitle = "Inventory Details For NodeId : " + currentSelectedNode;

      if (currentSelectedNode == "All")
        currentJsonObj = dummyJson.InventoryData;
      else
        currentJsonObj = fGetNodeWiseDataViewReport(
          dummyJson.InventoryData,
          currentSelectedNode
        );

      break;

    case "modalBodyviewReportContentPanelOpticalPowerAddBtn":
    case "modalBodyviewReportTabMenuOpticalPowerTrigger":
      currentTitle = "Optical Power For NodeId : " + currentSelectedNode;

      if (currentSelectedNode == "All")
        currentJsonObj = dummyJson.OpticalPowerAddData;
      else
        currentJsonObj = fGetNodeWiseDataViewReport(
          dummyJson.OpticalPowerAddData,
          currentSelectedNode
        );

      break;

    case "modalBodyviewReportContentPanelOpticalPowerDropBtn":
      currentTitle = "Optical Power For NodeId : " + currentSelectedNode;

      if (currentSelectedNode == "All")
        currentJsonObj = dummyJson.OpticalPowerDropData;
      else
        currentJsonObj = fGetNodeWiseDataViewReport(
          dummyJson.OpticalPowerDropData,
          currentSelectedNode
        );

      break;

    default:
      console.log("not identified tab");
      break;
  }

  // currentJsonObj=currentJsonObj.map((obj)=>{
  // 	obj["NetworkName"]=sessionStorage.getItem("NetworkName");
  // });

  console.log(
    "loadExcelJsonEventFunction " +
      currentSelectedTab +
      " for node " +
      currentSelectedNode
  );
};

$("#modalBodyviewReportTabMenu .btn").click(
  loadExcelJsonEventFunction
); /**tab clicked*/
$("#viewReportNodeId").on(
  "change",
  loadExcelJsonEventFunction
); /**node changed*/

/**
 * To Print In PDF Format
 * @returns
 */
$("#ViewReportSaveAsPdfBtn").on("click", function() {
  console.log("Print Pdf");
  var TableSelector,
    viewReportModalBodyTabs = $(
      "#viewReportModal .genericModalBodyContentPanel"
    ).children(),
    printableJsonData,
    columns,
    rows,
    fileName;

  console.log("viewReportModalBodyTabs::", viewReportModalBodyTabs);
  viewReportModalBodyTabs.each(function() {
    if ($(this).is(":visible")) {
      console.log("viewReportModalBodyTabs::", $(this).find("table"));
      TableSelector = $(this)
        .find("table")
        .attr("class")
        .split(" ")
        .filter(function(className) {
          if (className.includes("ContentPanelBom"))
            console.log("Filter function ::" + className);
          return className.includes("ContentPanel");
        })[0];
    }
  });

  console.log("TableSelector :", TableSelector);
  printableJsonData = fGetPrintableJsonDataFromTable("." + TableSelector);
  //printJS({printable: printableJsonData, properties: ['S.No.', 'NodeId','Equipment','Chord Type', 'Part Code','Source Card Type','Source Location','Source port','Target Card Type','Target Location','Target port','Length','Direction'], type: 'json'})

  console.log("printableJsonData :", printableJsonData);
  columns = printableJsonData.Header;
  rows = printableJsonData.Body;
  fileName =
    $("#viewReportViewMainHeading")
      .html()
      .replace(/\s/g, "") +
    "_" +
    sessionStorage.getItem("NetworkName") +
    "_Node" +
    $("#viewReportNodeId").val();
  fSaveAsPdfFromJsonData(columns, rows, fileName);
});

/**
 * function printExcelFile
 * @param JSONData
 * @param ReportTitle
 * @param ShowLabel
 * @returns
 */
function printExcelFile(currentJsonObj, ReportTitle, ShowLabel) {
  ///console.log("printExcelFile JSONData : "+currentJsonObj	)
  //If JSONData is not an object then JSON.parse will parse the JSON string in an Object
  var arrData =
    typeof currentJsonObj != "object"
      ? JSON.parse(currentJsonObj)
      : currentJsonObj;

  console.dir("printExcelFile", arrData);

  var CSV = "";
  //Set Report title in first row or line

  CSV += ReportTitle + "\r\n\n";

  //This condition will generate the Label/Header
  if (ShowLabel) {
    let row = "";

    //This loop will extract the label from 1st index of on array
    for (let index in arrData[0]) {
      //Now convert each value to string and comma-seprated
      row += index + ",";
    }

    row = row.slice(0, -1);

    //append Label row with line break
    CSV += row + "\r\n";
  }

  //1st loop is to extract each row
  for (var i = 0; i < arrData.length; i++) {
    let row = "";

    //2nd loop will extract each column and convert it in string comma-seprated
    for (let index in arrData[i]) {
      row += '"' + arrData[i][index] + '",';
    }

    // let nextRow = row.split(",");
    // console.log("Row before slicing for ", i, "th object is", nextRow);
    row.slice(0, row.length - 1);
    // console.log("Row after slicing for ", i, "th object is", row);
    //add a line break after each row
    CSV += nextRow + "\r\n";
  }

  if (CSV == "") {
    alert("Invalid data");
    return;
  }

  //Generate a file name
  var fileName = sessionStorage.getItem("NetworkName") + "_";
  //this will remove the blank-spaces from the title and replace it with an underscore
  fileName += ReportTitle.replace(/ /g, "_");

  //Initialize file format you want csv or xls
  var uri = "data:text/csv;charset=utf-8," + escape(CSV);

  // Now the little tricky part.
  // you can use either>> window.open(uri);
  // but this will not work in some browsers
  // or you will not get the correct file extension

  //this trick will generate a temp <a /> tag
  var link = document.createElement("a");
  link.href = uri;

  //set the visibility hidden so it will not effect on your web-layout
  link.style = "visibility:hidden";
  link.download = fileName + ".xls";

  //this part will append the anchor tag and remove it after automatic click
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
}

function convertToCSV(objArray) {
  var array = typeof objArray != "object" ? JSON.parse(objArray) : objArray;
  var str = "";

  for (var i = 0; i < array.length; i++) {
    var line = "";
    for (var index in array[i]) {
      if (line != "") line += ",";

      line += array[i][index];
    }

    str += line + "\r\n";
  }

  return str;
}

function exportCSVFile(headers, items, fileTitle) {
  if (headers) {
    items.unshift(headers);
  }

  // Convert Object to JSON
  var jsonObject = JSON.stringify(items);

  var csv = this.convertToCSV(jsonObject);

  var exportedFilenmae = fileTitle + ".csv" || "export.csv";

  var blob = new Blob([csv], { type: "text/csv;charset=utf-8;" });
  if (navigator.msSaveBlob) {
    // IE 10+
    navigator.msSaveBlob(blob, exportedFilenmae);
  } else {
    var link = document.createElement("a");
    if (link.download !== undefined) {
      // feature detection
      // Browsers that support HTML5 download attribute
      var url = URL.createObjectURL(blob);
      link.setAttribute("href", url);
      link.setAttribute("download", exportedFilenmae);
      link.style.visibility = "hidden";
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    }
  }
}
