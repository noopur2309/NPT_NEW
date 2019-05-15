/** Global Var*/
var pr_amp = /&/g;
var pr_lt = /</g;
var pr_gt = />/g;
var pr_quot = /\"/g;

/**
 * Register an Event and Dispatch to its handler
 */
$("#onlineConsolidatedReportView").click(function() {
	
	var openOnlineReportID = window.open("PlanningReport1.pdf", "HelpFile",
	"HEIGHT=600,WIDTH=800,SCROLLBARS,RESIZABLE,TOOLBAR");
	
	openOnlineReportID.focus();
});

/**
 * Open a Modal for Config File View
 */
$("#configFileView").click(function() {

	$('#onlineReportModal').modal({
		backdrop : 'static',
		keyboard : true
	});	

});


/**Escape html special characters to html. */
function textToHtml(str) {
	return str.replace(pr_amp, '&amp;').replace(pr_lt, '&lt;').replace(pr_gt,
			'&gt;');
};

/**
 *  On change/Upload of file this function will be called
 */
$("#uploadedConfigFile")
		.on(
				'change',
				function(event) {

					var file = document.getElementById("uploadedConfigFile").files[0];

					if (file) {
						var reader = new FileReader();
						reader.readAsText(file, "UTF-8");
						reader.onload = function(evt) {
							// console.log(" File " +evt.target.result);							
							var text = textToHtml(vkbeautify
									.xml(evt.target.result));

							$("#xml").empty().append(text);

						}
						reader.onerror = function(evt) {
							document
									.getElementById("modalBodyOnlineReportContentPanel").innerHTML = "Error reading file!";
						}
					}

				});




/**
 * Upload the file sending it via Ajax at the Spring Boot server, here @EMS server
 * @date   : 21 Aug, 2017
 * @author : hp   
 * @note   : Since uploadFileToEMS() ajax call require CORS(Cross Origin Resource Sharing) Client need 
 *           to Add allow-control-allow-origin   
 */

$(document).ready(function() {
    //$("#upload-file-input").on("change", uploadFileToEMS);
	$("#configFileToEMSuploadButton").on("click", uploadFileToEMS);
  });

function loadingModalShowEMS(text)
{
	console.log("Inside loadingModalShowEMS");
	$("#loadingModal").keydown(false);
	$("#loadingModal").modal('show');
	$("#modalLoadingContent img").attr('src','images/uploading_giphy.gif');
 	$("#modalLoadingContent p").text(text);
}

function uploadFileToEMS() {
 
	console.log("uploadFileToEMS .. ")
	loadingModalShowEMS("Uploading File To EMS Server .. ");
	
	$.ajax({
    url: 'http://192.168.8.168:8080/uploadFile',
    type: "POST",
    data:  new FormData($("#upload-file-form")[0]) /* formDataNew */,
    enctype: 'multipart/form-data',
    processData: false,
    contentType: false,
    cache: false,
    //crossDomain: true,
    
    success: function () {
      // Handle upload success
        $("#upload-file-message").text("File succesfully uploaded");
    	$("#nptModal > img").attr('src','images/success.png');
    	$("#modalLoadingContent p").text("File Uploaded Successfully  ");
      
    },
    error: function () {
      // Handle upload error
      $("#upload-file-message").text(
          "File not uploaded (perhaps it's too much big)");
    	
 		$("#nptModal > img").attr('src','images/failure.png');
   		$("#modalLoadingContent p").text("File not uploaded (perhaps it's too much big)");
	  
    }
  });
} // function uploadFile
				