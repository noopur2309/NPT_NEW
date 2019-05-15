
$(document).ready(function () {

	console.log("Index.js loaded");

	/************************************************************************
	 * Auto load function on home page startup
	 ************************************************************************/
	(function () {

		//adjust height of sidebar and Network Element Bar 
		adjustSideBarHeightOnLOad();
		adjustWidthOnLoad();

		//console.log(localStorage.getItem("mapStr"));
		if (localStorage.getItem("openMap") == 0 || localStorage.getItem("dashboardOpenMapCall") == 0) {
			console.log(localStorage.getItem("mapStr"));

			if (localStorage.getItem("mapStr") === "undefined") {
				console.log("replacing location to dashboard");
				location.replace("NewDashboard.html");
			}
			else {
				console.log("Loading Modal ");
				$('#createMapModal').modal('show');
			}
		}
		else {
			$("#home-username").text(localStorage.getItem("UserName"));

			sessionStorage.setItem("UserName", localStorage.getItem("UserName"));
			sessionStorage.setItem("NetworkName", localStorage.getItem("NetworkName"));
			sessionStorage.setItem("Topology", localStorage.getItem("NetworkTopology"));

			nptGlobals.NetworkTopology = sessionStorage.getItem("Topology");
			nptGlobals.NetworkName = sessionStorage.getItem("NetworkName");

			if (localStorage.getItem("mapStr") != 'null') {
				console.log(localStorage.getItem("mapStr"));
				drawMapFromJSON(localStorage.getItem("mapStr"));

				//fShowClientServerCommunicationModal("NPT is initializing the network map .. ");           		

				//     			setTimeout(function(){initializeMapFromDashboard();},1000);   
			}
			//        	networkInfoTab();
			instantiateNetworkAjaxCall();
		}

		//Initial values, so that on page reloads to dashboard.html  
		localStorage.setItem("openMap", 0);
		localStorage.setItem("dashboardOpenMapCall", 0);
		localStorage.setItem("mapStr", undefined);
	}());


	/************************************************************************
	 * // load map from saved map file
	 ************************************************************************/
	$("#loadMap").on('change', function (event) {
		// var files=event.target.files;
		//		/var formData=new Formdata();
		// formData.append('file_name',$("#loadMap").prop('files')[0]);
		// var file=files[0];
		// console.log(file);
		// console.log(formData);
		var file = document.getElementById("loadMap").files[0];
		if (file) {
			var reader = new FileReader();
			reader.readAsText(file, "UTF-8");
			reader.onload = function (evt) {
				console.log(evt.target.result);
				console.log(JSON.parse(evt.target.result));
				json = JSON.parse(evt.target.result);
				graph.fromJSON(JSON.parse(json));
				nodeInfoTab();
				linkInfoTab();

			}
			reader.onerror = function (evt) {
				document.getElementById("netelemId").innerHTML = "error reading file";
			}
		}

	});


	/************************************************************************
	 * Mousewheel zoomIn and zoomOut events
	 ************************************************************************/
		$('#canvasId').on('mousewheel', function(e) {
			e.preventDefault();
			console.log(e);
			var numOfElements=graph.getElements().length;
			if (e.originalEvent.wheelDelta / 120 > 0) {
	
				if(numOfElements!=0)
				{
					console.log('scrolling up !', 'X: ', e.pageX, 'Y: ', e.pageY);
					zoomIn();
				}
			} else {
				//console.log('scrolling down !', 'X: ', e.pageX, 'Y: ', e.pageY);
				if(numOfElements!=0)
					zoomOut();
			}
		});

	/************************************************************************
	 *  zoomIn and zoomOut triggers 
	 ************************************************************************/
	$('#zoomInBtn').on('click', function () {
		// zoomInVar+=.2;
		zoomIn();
	});
	$('#zoomOutBtn').on('click', function () {
		// zoomInVar-=.2;
		zoomOut();
	});

	$('#resetZoom').on('click', function () {
		// zoomInVar-=.2;
		resetZoom();
	});

	/************************************************************************
	 *  zoomIn and zoomOut scale constants
	 ************************************************************************/
	var minScale = 0.6, maxScale = 1.5, scaleFactor = 0.2, scaleReset = 1;

	/************************************************************************
	 * ZoomIn function to zoom in on map canvas area 
	 ************************************************************************/
	//	function zoomIn() {
	//		var ScaleVar = V(paper.viewport).scale();
	//		var x=dragLastPosition.x,
	//		y=dragLastPosition.y;
	//		//dragStartPosition = { x: x * ScaleVar.sx, y: y * ScaleVar.sy};
	//		dragEvent(null,x,y);
	//		if(ScaleVar.sx<maxScale && ScaleVar.sy<maxScale)
	//		{
	//			ScaleVar.sx=ScaleVar.sx+scaleFactor;
	//			ScaleVar.sy=ScaleVar.sy+scaleFactor;
	//			paper.setOrigin(0, 0);
	//			paper.scale(ScaleVar.sx,ScaleVar.sy);
	//		}
	//		dragMouseLeaveEvent();
	//		console.log(" zoomIn: ScaleVar.sx "+ScaleVar.sx+" ScaleVar.sy"+ScaleVar.sy);
	//	}


	/************************************************************************
	 * ZoomOut function to zoom out on map canvas area 
	 ************************************************************************/
	//	function zoomOut() {
	//		var ScaleVar = V(paper.viewport).scale();
	//		var x=dragLastPosition.x,
	//			y=dragLastPosition.y;
	//		//dragStartPosition = { x: x * ScaleVar.sx, y: y * ScaleVar.sy};
	//		dragEvent(null,x,y);
	//		if(ScaleVar.sx>minScale && ScaleVar.sy>minScale)
	//		{
	//			ScaleVar.sx=ScaleVar.sx-scaleFactor;
	//			ScaleVar.sy=ScaleVar.sy-scaleFactor;
	//			paper.setOrigin(0, 0);
	//			paper.scale(ScaleVar.sx,ScaleVar.sy);
	//		}
	//		dragMouseLeaveEvent();
	//		console.log(" zoomOut: ScaleVar.sx "+ScaleVar.sx+" ScaleVar.sy"+ScaleVar.sy);
	//		$("svg#v-3").css({
	//			width:'100%'
	//		});
	//
	//	}

	/************************************************************************
	 * ResetZoom function to reset to original value on map canvas area 
	 ************************************************************************/
	//	function resetZoom() {
	//		console.log(" Reset Zoom");
	//		var ScaleVar = V(paper.viewport).scale();
	////		var x=dragLastPosition.x,
	////		y=dragLastPosition.y;
	////		dragStartPosition = { x: x * ScaleVar.sx, y: y * ScaleVar.sy};
	//		
	//		ScaleVar.sx=scaleReset;
	//		ScaleVar.sy=scaleReset;
	//		paper.scale(ScaleVar.sx,ScaleVar.sy);
	//		console.log(" Reset: ScaleVar.sx "+ScaleVar.sx+" ScaleVar.sy"+ScaleVar.sy);
	//		// paper.scaleContentToFit();
	//	}

	////	undo and redo
	//	var commandManager = new joint.dia.CommandManager({ graph: graph });
	//
	//	$('#undo-button').click(function() { commandManager.undo(); });
	//	$('#redo-button').click(function() { commandManager.redo(); });


	/************************************************************************
	 * Print 
	 ************************************************************************/
	$('#print').click(function () {
		paper.print()
	});

	/************************************************************************
	 * Horizontal scrolling of Circuit Table Area 
	 ************************************************************************/
	$("#circuits-view table").mousewheel(function (event, delta) {
		this.scrollLeft -= (delta * 30);
		event.preventDefault();
	});


	/************************************************************************
	 * Custom BootBox Modal minimize function
	 ************************************************************************/
	var $content, $modal, $apnData, $modalCon;

	$content = $(".min");
	$(".modalMinimize").on("click", function () {

		$modalCon = $(this).closest(".modal").attr("id");

		$apnData = $(this).closest(".modal");

		$modal = "#" + $modalCon;

		console.log($modalCon);
		console.log($apnData);
		console.log($modal);

		$(".modal-backdrop").addClass("display-none");

		$($modal).toggleClass("min");

		if ($($modal).hasClass("min")) {

			$(".minmaxCon").append($apnData);

			$(this).find("i").toggleClass('fa-minus').toggleClass('fa-clone');

		}
		else {

			// $(".container-fluid").append($apnData); 

			$(this).find("i").toggleClass('fa-clone').toggleClass('fa-minus');

		};

	});

	$("button[data-dismiss='modal']").click(function () {

		$(this).closest(".modal").removeClass("min");

		$(".container-fluid").removeClass($apnData);

		$(this).next('.modalMinimize').find("i").removeClass('fa fa-clone').addClass('fa fa-minus');

	});


	/************************************************************************
	 * Check Flow Status of overall npt functions (Done / Not Done)
	 ************************************************************************/
	$("#flowStatusCheck").click(function () {
		console.log(window.NptCurrentStateArr);
		//console.log(isMapSaved);
		showNetworkStatus();
	});

	/************************************************************************
	 * Go back to dashboard
	 ************************************************************************/
	$(".dashboard-redirect").click(function () {
		location.replace("NewDashboard.html");
		localStorage.setItem('UserName', sessionStorage.getItem('UserName'));
	});


});

