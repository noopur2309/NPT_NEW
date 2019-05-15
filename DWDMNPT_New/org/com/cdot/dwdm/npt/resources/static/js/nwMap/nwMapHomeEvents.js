$(document).ready(function() {
	
	$("#saveMapId").click(function() {
		alert("Hi")
		saveMap();
	});
	$("#rwaAlgoBtn").click(function() {
		rwaExecutionRequest(); /*canvasActive();*/
	});
	$("#updateMap").click(function() {
		saveMap();
	});
	$("#generateConsolidatedReportTrigger").click(function() {
		console.log("Report Alert");
		generateConsolidatedReport();
	});
	$("#uploadConfigFileFromEMSTrigger").click(function() {
		console.log("Report Alert");
		uploadConfigFileFromEMSTrigger();
	});
	$("#savemap").click(function() {
		testAjax();
	});
	$("#testingMenuForDeveloperTrigger").click(function() {
		testingCall();
	}); /**DBG=> Just to added for testing, can be removed later*/
	$("#updateSchemaTrigger").click(function() {
		updateSchema();
	});
	$("#logoutTrigger").click(function() {
		logoutNpt();
	});
	$("#generatePatchCord").click(function() {
		generatePatchCord();
	});

	//Equipment data update from BOM file
	$("#updateEquipmentTrgr").click(function() {
		let postData = jsonPostObject();

		fShowClientServerCommunicationModal("Updating Equipment list of NPT. ");

		serverPostMessage("updateEquipment", postData)
		.then(function(data) {
			console.log("Bom data updated seccesfully :", data);
			fShowSuccessMessage("Bom data updated seccesfully.");
		})
		.catch(function(e) {
			fShowFailureMessage("Bom data updation failed.");
			console.log(e);
		});
	});

	$("#brownFieldConvertTrgr").click(function() {
		  if (
		    nptGlobals.getMapSavedStatus() &&
		    nptGlobals.getCircuitSavedStatus() &&
		    nptGlobals.getRwaExecutedStatus() &&
		    nptGlobals.getInventoryGeneratedStatus() &&
		    nptGlobals.getIpSchemeGeneratedStatus()
		  ) {
		    const messageAlert = `<div class="row clear-fix">
		    <div class="col-md-3 col-xs-3 col-lg-3 col-sm-3 pull-left">
		    <img src="images/warning.svg" alt="warning" class="img-responsive">
		    </div>
		    <div class="col-md-9 col-xs-9 col-lg-9 col-sm-9 pull-right">
		    <p class="lead">Are youn sure you want to convert this network to Brown Field ?</p>
		    </div>
		    </div>`;
		    const titleText = `<p class="text-center lead text-primary"><strong>Convert to Brown Field</strong></p>`;

		    bootBoxCustomDialog(messageAlert, titleText, brownFieldConversion);
		  } else {
		    showNetworkStatus();
		  }
		});

	$("#restoreGreenField").click(function() {
	  if (isBrownFieldNetwork()) {
	    const messageAlert = `<div class="row clear-fix">
				<div class="col-md-3 col-xs-3 col-lg-3 col-sm-3 pull-left">
				<img src="images/warning.svg" alt="warning" class="img-responsive">
				</div>
				<div class="col-md-9 col-xs-9 col-lg-9 col-sm-9 pull-right">
				<p class="lead">Restoring to green field will remove the brown field network. Do you still want to restore ?</p>
				</div>
				</div>`;
	    const titleText = `<p class="text-center lead text-primary"><strong>Restore Green Field</strong></p>`;

	    bootBoxCustomDialog(messageAlert, titleText, restoreGreenFieldAjaxCall);
	  } else {
	    bootBoxAlert("You are already in Green Field.", 2000);
	  }
	});
	
  /************************************************************************
   * Mousewheel zoomIn and zoomOut events
   ************************************************************************/
  $("#canvasId").on("mousewheel", function(e) {
    e.preventDefault();
    console.log(e);
    if (e.deltaY < 0) {
      paperUtil.zoomOut();
    } else {
      paperUtil.zoomIn();
    }
  });

  /************************************************************************
   *  zoomIn and zoomOut triggers
   ************************************************************************/
  $("#zoomInBtn").on("click", function() {
    // zoomInVar+=.2;
    paperUtil.zoomIn();
  });
  $("#zoomOutBtn").on("click", function() {
    // zoomInVar-=.2;
    paperUtil.zoomOut();
  });

  $("#resetZoom").on("click", function() {
    // zoomInVar-=.2;
    paperUtil.resetZoom();
  });


  /************************************************************************
   * Print
   ************************************************************************/
  $("#print").click(function() {
    paper.print();
  });

  /************************************************************************
   * Lock Unlock paper dragging
   ************************************************************************/
  $("#lockUnlockPaper").click(function() {
    if (
      $(this)
        .children()
        .first()
        .hasClass("fa-unlock")
    )
      paperUtil.lockPaper();
    else paperUtil.unlockPaper();
  });

  /************************************************************************
   * Horizontal scrolling of Circuit Table Area
   ************************************************************************/
  $("#circuits-view table").mousewheel(function(event, delta) {
    this.scrollLeft -= delta * 30;
    event.preventDefault();
  });

  /************************************************************************
   * Custom BootBox Modal minimize function
   ************************************************************************/

  $(".modalMinimize").on("click", function(e) {
    $modalCon = $(this)
      .closest(".modal")
      .attr("id");

    $apnData = $(this).closest(".modal");

    $modal = "#" + $modalCon;
    nptGlobals.LinkNodeConfigModalId = $modal;

    $($modal).hide();

    let maxDiv = $(
      `<div class='maximize-container'>
        <span class="maximize-modal-title">Link Node Configuration</span>
        <button type="button" class="btn btn--danger maximize-modal-close" data-dismiss="modal"> <i class="fa fa-times" aria-hidden="true"></i></button>
				<button type="button" class="btn btn--warning maximize-modal "> <i class='fa fa-clone'></i> </button>
      </div>`
    );

    $(".minmaxCon").append(maxDiv);

  });

  $(document).delegate(".maximize-modal-close", "click", function(e) {
    $(this)
      .parent()
      .remove();
    nptGlobals.LinkNodeConfigModalId = undefined;
  });

  $(document).delegate(".maximize-modal", "click", function(e) {
    console.log("Maximize  Modal :", $(this));
    maximizeLinkNodeConfigModal();
  });

  function maximizeLinkNodeConfigModal() {
    if (nptGlobals.LinkNodeConfigModalId)
      $(nptGlobals.LinkNodeConfigModalId).show();
    let $maximizeModal = $(".maximize-modal").parent();
    $maximizeModal.remove();
  }

  /************************************************************************
   * Check Flow Status of overall npt functions (Done / Not Done)
   ************************************************************************/
  $("#flowStatusCheck").click(function() {
    console.log(window.NptCurrentStateArr);
    showNetworkStatus();
  });

  /************************************************************************
   * Go back to dashboard
   ************************************************************************/
  $(".dashboard-redirect").click(function() {
    location.replace("NewDashboard.html");
    localStorage.setItem("UserName", sessionStorage.getItem("UserName"));
  });
});
