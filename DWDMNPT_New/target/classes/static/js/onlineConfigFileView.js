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