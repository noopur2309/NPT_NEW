(function($) {
  "use strict"; // Start of use strict

  sessionStorage.setItem("UserName", localStorage.getItem("UserName"));
  // sessionStorage.setItem("UserName","admin");
  var networkTopology;
  console.log(
    "dashboard js loaded for User ::",
    sessionStorage.getItem("UserName")
  );
  var time = Date();
  time = time.split(" ");
  //console.log(time);
  var updateTime =
    time[4] + " on " + time[0] + "," + time[2] + " " + time[1] + "," + time[3];
  $(".panel .panel-footer")
    .empty()
    .html(updateTime);

  // Configure tooltips for collapsed side navigation
  $('.navbar-sidenav [data-toggle="tooltip"]').tooltip({
    template:
      '<div class="tooltip navbar-sidenav-tooltip" role="tooltip"><div class="arrow"></div><div class="tooltip-inner"></div></div>'
  });

  // Toggle the side navigation
  $("#sidenavToggler").click(function(e) {
    e.preventDefault();
    $("body").toggleClass("sidenav-toggled");
    $(".navbar-sidenav .nav-link-collapse").addClass("collapsed");
    $(
      ".navbar-sidenav .sidenav-second-level, .navbar-sidenav .sidenav-third-level"
    ).removeClass("show");
  });

  // Force the toggled class to be removed when a collapsible nav link is clicked
  $(".navbar-sidenav .nav-link-collapse").click(function(e) {
    e.preventDefault();
    $("body").removeClass("sidenav-toggled");
  });

  // Prevent the content wrapper from scrolling when the fixed side navigation hovered over
  $(
    "body.fixed-nav .navbar-sidenav, body.fixed-nav .sidenav-toggler, body.fixed-nav .navbar-collapse"
  ).on("mousewheel DOMMouseScroll", function(e) {
    var e0 = e.originalEvent,
      delta = e0.wheelDelta || -e0.detail;
    this.scrollTop += (delta < 0 ? 1 : -1) * 30;
    e.preventDefault();
  });

  // Scroll to top button appear
  $(document).scroll(function() {
    var scrollDistance = $(this).scrollTop();
    if (scrollDistance > 100) {
      $(".scroll-to-top").fadeIn();
    } else {
      $(".scroll-to-top").fadeOut();
    }
  });

  // Configure tooltips globally
  $('[data-toggle="tooltip"]').tooltip();

  // Smooth scrolling using jQuery easing
  $(document).on("click", "a.scroll-to-top", function(event) {
    var $anchor = $(this);
    $("html, body")
      .stop()
      .animate(
        {
          scrollTop: $($anchor.attr("href")).offset().top
        },
        500,
        "easeInOutExpo"
      );
    event.preventDefault();
  });

  // Call the dataTables jQuery plugin
  $(document).ready(function() {
    $("#dataTable").DataTable();
  });

  // Call the dataTables jQuery plugin
  $(document).ready(function() {
    $(".main-").DataTable();
  });

  //Get user networks
  $(document).ready(function() {
    console.log(
      "Fetching data for networks of user ::",
      sessionStorage.getItem("UserName")
    );
    // fInitializeDashboard();
    fGetUserNetworksAjaxCall();
    $("#userNameBc").html(sessionStorage.getItem("UserName"));
  });

  //  var fLoadFetchModal=function(text,img)
  //  {
  //	  //if(flag==undefined) flag=true;
  //	  if(typeof img==undefined) img==null;
  //
  //	  var imageTag=`<img src="images/${img}" id="loading-modal-dashboard-img" class="" style="margin:auto;">`;
  //
  //	  if(img==null)
  //		  $("#loading-modal-dashboard-img").remove();
  //	  else
  //	      $("#loading-modal-dashboard-img").parent().empty().append(imageTag);
  //
  //	  $("#loading-modal-dashboard-text").empty().html(text);
  //
  //  }

  var fGetUserNetworksAjaxCall = function() {
    //	  $("#loadingModalDashboard").modal('show');
    //	  fLoadFetchModal("Fetching Network Data","loading.gif");
    var jsonStr = { username: sessionStorage.getItem("UserName") };
    $.ajax({
      type: "POST",
      contentType: "application/json",

      headers: {
        Accept: "application/json; charset=utf-8",
        "Content-Type": "application/json; charset=utf-8"
      },

      url: "/userNetworks",
      data: JSON.stringify(jsonStr),
      dataType: "json",

      timeout: 600000,

      success: function(data) {
        console.log("User network data");
        console.log(data);
        var ctx = document.getElementById("myLineChart").getContext('2d');
        var myChart = new Chart(ctx,data.dashboard_Graphs.Network_Power);
        
        var ctx = document.getElementById("mybarChart").getContext('2d');
        var myChart = new Chart(ctx,data.dashboard_Graphs.Network_Price);
  
        //data=JSON.parse(data);
        if (data.length > 0) {
          //fLoadFetchModal("NPT has retrieved "+data.length+" network maps.");
//          fShowUserNetworkDashboard(data);
          fShowUserNewDashboard(data.usernetworks);
          fShowUserSearchDashboard(data.usernetworks);
    
          
        } else {
          console.log("Naye lagte ho");
//          fShowUserNetworkDashboard(data);
          
          fShowUserNewDashboard(data.usernetworks);
          fShowUserSearchDashboard(data.usernetworks);
          //fLoadFetchModal(sessionStorage.getItem("UserName")+", you have no network maps saved with NPT. Start creating new network maps from homepage of your dashboard.");
        }

        //				setTimeout(function(){
        //					  $("#loadingModalDashboard").modal('hide');
        //				  },500);
      },
      complete: function() {
        /*$("#nptModal > img").attr('src','images/success.png');
			   $("#modalLoadingContent p").text("Map Saved Successfully ");*/
      },
      error: function(e) {
        console.log("error",e);
      }
    });
  };

//  var fShowUserNetworkDashboard = function(data) {
//    $(".dashboard-main-div .card-body").empty();
//    var template = `<div class="m-3 network-map-div-el hide">
//		  <div class="card p-5">
//		  <!-- <img class="card-img-top" src="..." alt="Card image cap"> -->
//		  <div class="card-block">
//		  <h4 class="card-title">Create New Network</h4>
//		  <a class="card-text">
//		  <img src="images/plus.png" class="img-responsive" id="createNewNetwork">
//		  </a>
//		  </div>
//		  </div>  
//		  </div>`;
//
//    var templateDivBox = $(template);
//    console.log(templateDivBox);
//    templateDivBox
//      .appendTo(".dashboard-main-div .card-body")
//      .hide()
//      .slideDown("slow");
//    // $(".dashboard-main-div .card-body").empty().append(template).hide().slideDown();
//    template = "";
//    for (var i = 0; i < data.length; i++) {
//      var state =
//        data[i].BrownFieldId == 0
//          ? nptGlobals.GreenFieldStr
//          : nptGlobals.BrownFieldStr;
//      var cardStateClass =
//        data[i].BrownFieldId == 0 ? "green-field-card" : "brown-field-card";
//      template += `<div class="m-3 network-map-div-el hide ${cardStateClass}">
//			  <div class="card p-3">
//			  <!-- <img class="card-img-top" src="..." alt="Card image cap"> -->
//			  <div class="card-block">
//			  <h4 class="card-title">${data[i].NetworkName}</h4>
//			  <p class="card-text">
//					<ul class="list-inline">
//						<li class="list-inline-item text-center p-1"><span class="title">N.Id</span><br>${
//              data[i].NetworkId
//            }</li>
//					  <li class="list-inline-item text-center p-1"><span class="title">State</span><br>${state}</li>
//					  <li class="list-inline-item text-center p-1"><span class="title">Topology</span><br><span class="card-network-topology">${
//              data[i].Topology
//            }</li>
//					  <li class="list-inline-item text-center p-1"><span class="title">Cost</span><br>7,000$</li>
//				  </ul>
//			  </p>
//			  <a href="#"  class="btn btn-success">Open Map</a>
//			  <a href="#"  class="btn btn-danger">Delete</a>
//			  <a href="#"  class="btn btn-warning">Clone</a>
//			  </div>
//			  </div>  
//			  </div>`;
//      templateDivBox = $(template);
//      // console.log(templateDivBox);
//      templateDivBox
//        .appendTo(".dashboard-main-div .card-body")
//        .hide()
//        .slideDown("slow");
//      template = "";
//    }
//    console.log(template);
//    var time = Date();
//    time = time.split(" ");
//    //console.log(time);
//    var updateTime =
//      time[4] +
//      " on " +
//      time[0] +
//      "," +
//      time[2] +
//      " " +
//      time[1] +
//      "," +
//      time[3];
//    // var templateDiv=$(template);
//
//    //templateDivBox.appendTo(".dashboard-main-div .card-body").hide().slideDown();
//    //$(".dashboard-main-div .card-body").append(template).hide().slideDown();
//    $(".dashboard-main-div .card-footer span")
//      .empty()
//      .html(updateTime);
//  };
//  
  
//  For New dashboard
  
  var fShowUserNewDashboard = function(data) {
	    $(" .panel .panel-body  .row ").empty();
	    var template = `    <div class="col-md-3">
	                              <div class="card">
                                             <img class="card-img-top img-responsive" src="newassets/add.jpeg.png" alt="add">
                                           <div class="card-body">
                                               <a href="#" class="btn btn-success" data-toggle="modal" data-target="#myModal" id="createNewNetwork">Create New Network</a>
                                     </div>
                                       </div>
                                       </div>`;

	    var templateDivBox = $(template);
	    console.log(templateDivBox);
	    templateDivBox
	      .appendTo(" .panel .panel-body .row ")
	      .hide()
	      .slideDown("slow");
	   
	    template = "";
	    console.log({data});
	    for (var i = 0; i < data.length; i++) {
	      var state =
	        data[i].BrownFieldId == 0
	          ? nptGlobals.GreenFieldStr
	          : nptGlobals.BrownFieldStr;
	      var cardStateClass =
	        data[i].BrownFieldId == 0 ? "green-field-card" : "brown-field-card";
	      template += ` <div class="col-md-3">
            <div class="card  ${cardStateClass}">
                   <img class="card-img-top img-responsive" src="newassets/MeshTopology.png"  alt="Sans &amp; Sans-Serif">
                      
            <div class="card-body card-body-cascade text-center">

   
         <a href="#" class="btn btn-success">${data[i].NetworkName}</a>
      
    
          <br>
            <a href="#"  target="_self" data-toggle="tooltip"><i class="glyphicon glyphicon-eye-open"> </i></a>
            <a href="#" class="Clone" title="Clone" data-toggle="tooltip"><i class="glyphicon glyphicon-pencil"> </i></a>
            <a href="#" class="Delete" title="Delete" data-toggle="tooltip"><i class="glyphicon glyphicon-trash"> </i></a>
            
            <a href="#" class="btn btn-info"><span class="card-network-topology"></span>${data[i].Topology}</a>
          <a href="#" class="btn btn-info"><span class="card-network-topology"></span>${data[i].NetworkId}</a>
            </div>
            </div>
            </div>
	      `;
	      templateDivBox = $(template);
	      // console.log(templateDivBox);
	      templateDivBox
	        .appendTo(" .panel .panel-body .row")
	        .hide()
	        .slideDown("slow");
	      template = "";
	    }
	    console.log({template});
	    var time = Date();
	    time = time.split(" ");
	    //console.log(time);
	    var updateTime =
	      time[4] +
	      " on " +
	      time[0] +
	      "," +
	      time[2] +
	      " " +
	      time[1] +
	      "," +
	      time[3];
	    // var templateDiv=$(template);

	    //templateDivBox.appendTo(".dashboard-main-div .card-body").hide().slideDown();
	    //$(".dashboard-main-div .card-body").append(template).hide().slideDown();
	    $(".panel .panel-footer")
	      .empty()
	      .html(updateTime);
	  };

  // Create new network
  $(document).ready(function() {
    $("body").delegate("#createNewNetwork", "click", function() {
      //document.location.href = "home.html";
      localStorage.setItem("openMap", 0);
      localStorage.setItem("dashboardOpenMapCall", 0);
      localStorage.setItem("mapStr", "createMap");
      window.open("home.html", "_self");
      // location.replace("home.html");
    });
  });

  //Refresh Call for Getting user networks
  $(document).ready(function() {
    console.log("Fetching data for user networks");
    $("body").delegate(
      ".panel .panel-heading a",
      "click",
      function() {
        console.log("Fetching data for user networks");
        fGetUserNetworksAjaxCall();
      }
    );
  });

  // Open Map For New Dashboard trigger
  $(document).ready(function() {
    $("body").delegate(".card-body a", "click", function() {
      //document.location.href = "home.html";
      console.log("Open Clicked");
      var buttonText = $(this).attr('class'),
        networkNameStr,
        networkTopologyStr;
      console.log("ButtonText",buttonText);

      networkNameStr = $(this)
        .parent()
        .find("a")
        .html();
      console.log(
    	      " networkNameStr",
    	        networkNameStr);
      networkTopologyStr = $(this)
        .parent()
        .find(".card-network-topology")
        .html();
      console.log(
        "networkTopology::" + networkTopologyStr,
        " networkNameStr",
        networkNameStr
      );
      networkTopology = networkTopologyStr;

      if (buttonText == "btn btn-success") {
        fOpenMapDashboard(networkNameStr, sessionStorage.getItem("UserName"));
      } else if (buttonText == "Delete") {
        fDeleteMapDashboard(networkNameStr, sessionStorage.getItem("UserName"));
      } else if (buttonText == "Clone") {
        fCloneMapDashboard(networkNameStr, sessionStorage.getItem("UserName"));
      }
    });
  });
  
  
  
  
  
  
  // Open Map trigger
//  $(document).ready(function() {
//    $("body").delegate(".network-map-div-el a", "click", function() {
//      //document.location.href = "home.html";
//      console.log("Open Clicked");
//      var buttonText = $(this).html(),
//        networkNameStr,
//        networkTopologyStr;
//
//      networkNameStr = $(this)
//        .parent()
//        .find("h4")
//        .html();
//      networkTopologyStr = $(this)
//        .parent()
//        .find(".card-network-topology")
//        .html();
//      console.log(
//        "networkTopology::" + networkTopologyStr,
//        " networkNameStr",
//        networkNameStr
//      );
//      networkTopology = networkTopologyStr;
//
//      if (buttonText == "Open Map") {
//        fOpenMapDashboard(networkNameStr, sessionStorage.getItem("UserName"));
//      } else if (buttonText == "Delete") {
//        fDeleteMapDashboard(networkNameStr, sessionStorage.getItem("UserName"));
//      } else if (buttonText == "Clone") {
//        fCloneMapDashboard(networkNameStr, sessionStorage.getItem("UserName"));
//      }
//    });
//  });

  var fOpenMapDashboard = function(networkNameStr, userName) {
    var jsonStr = {
      UserName: userName,
      NetworkName: networkNameStr
    };

    $.ajax({
      type: "POST",
      contentType: "application/json",

      headers: {
        Accept: "application/json; charset=utf-8",
        "Content-Type": "application/json; charset=utf-8"
      },

      url: "/openNetwork",
      data: JSON.stringify(jsonStr),
      dataType: "json",

      timeout: 600000,

      success: function(data) {
        console.log("User Map JSON");
        console.log(data);
        //data=JSON.parse(data);
        if (data.MapData === "null") {
          console.log("Map has not been saved yet.No data to draw map.");
          localStorage.setItem("mapStr", data.MapData);
        } else localStorage.setItem("mapStr", data.MapData);

        //console.log(window.openMap);
        //window.history.pushState('page2', 'Title', '/home.html');

        localStorage.setItem("UserName", userName);
        localStorage.setItem("NetworkName", networkNameStr);
        localStorage.setItem("Circuits", data.Circuits);
        localStorage.setItem("Demands", data.Demands);
        localStorage.setItem("Traffic", data.Traffic);
        console.log("..networkTopology..", networkTopology);
        localStorage.setItem("NetworkTopology", networkTopology);
        localStorage.setItem("openMap", 1);
        localStorage.setItem("dashboardOpenMapCall", 1);
        //location.replace("home.html");
        window.open("home.html", "_self");
      },
      error: function(e) {
        console.log(e);
        console.log("error opening map", e);
      }
    });
  };

  var fCloneMapDashboard = function(networkNameStr, userName) {
    var jsonStr = { UserName: userName, NetworkName: networkNameStr };

    fShowLoader("Cloning Network ....");
    $.ajax({
      type: "POST",
      contentType: "application/json",

      headers: {
        Accept: "application/json; charset=utf-8",
        "Content-Type": "application/json; charset=utf-8"
      },

      url: "/cloneNetwork",
      data: JSON.stringify(jsonStr),
      dataType: "json",

      timeout: 600000,

      success: function(data) {
        console.log("Clone network Successfull::", data);
        fHideLoader();
        fGetUserNetworksAjaxCall();
      },
      error: function(e) {
        console.log("Clone network error::", e);
      }
    });
  };

  var fDeleteMapDashboard = function(networkNameStr, userName) {
    //var jsonStr={"UserName":userName,"NetworkName":networkNameStr};
    var jsonPostData = new Object();
    jsonPostData.NetworkInfo = new Object();
    jsonPostData.NetworkInfo.NetworkName = networkNameStr;
    jsonPostData.NetworkInfo.UserName = userName;

    let promptInput = confirm("Are you sure you want to delete this network?");
    console.log("Prompt Input::", promptInput);
    if (promptInput) {
      fShowLoader("Deleting Network ....");
      $.ajax({
        type: "POST",
        contentType: "application/json",

        headers: {
          Accept: "application/json; charset=utf-8",
          "Content-Type": "application/json; charset=utf-8"
        },

        url: "/deleteNetwork",
        data: JSON.stringify(jsonPostData),
        dataType: "json",

        timeout: 600000,

        success: function(data) {
          console.log(data);
          //data=JSON.parse(data);
          if (data == 1) {
            console.log("Map has been successfully deleted.");

            //					fLoadFetchModal("Map has been successfully deleted.",'checked.png');
            //					setTimeout(function(){
            //						fLoadFetchModal("Map has been successfully deleted. Retrieving ",'checked.png');
            //					},500);
            //					setTimeout(fGetUserNetworksAjaxCall,500);
            fHideLoader();
            fGetUserNetworksAjaxCall();
          } else if (data == 0) {
            console.log("Map couldn't be deleted successfully deleted.");

            //					fLoadFetchModal("Map can't be deleted successfully deleted.",'cancel.png');
            //					setTimeout(function(){
            //						  $("#loadingModalDashboard").modal('hide');
            //					  },500);
          }
        },
        error: function(e) {
          console.log(e);
          console.log("error");
        }
      });
    }
  };
  
  
  
//For Search Table For Newdashboard

var fShowUserSearchDashboard = function(data) {
	    $(".row .col-md-12 .panel .panel-body .table").empty();
	    var template = `<thead>
       <tr>
                                                    <th>Name</th>
                                                    <th>State</th>
                                                    <th>Cost</th>
                                                    <th>N.Id</th>
                                                    <th>Topology</th>
                                                    <th>Status</th>
                                                    <th>Actions</th>
                                                </tr>
    </thead>`;

	    var templateDivBox = $(template);
	    console.log(templateDivBox);
	    templateDivBox
	      .appendTo(" .row .col-md-12 .panel .panel-body .table ")
	      .hide()
	      .slideDown("slow");
	   
	    template = "";
	    console.log({data});
	    for (var i = 0; i < data.length; i++) {
	      var state =
	        data[i].BrownFieldId == 0
	          ? nptGlobals.GreenFieldStr
	          : nptGlobals.BrownFieldStr;
	      var cardStateClass =
	        data[i].BrownFieldId == 0 ? "green-field-card" : "brown-field-card";
	      template += ` <tbody id="myTable">
              <tr class="success">
                                                   <td><a href="#">${data[i].NetworkName}</a></td>
                                                    <td>${cardStateClass}</td>
                                                    <td>$122</td>
                                                    <td>${data[i].NetworkId}</td>
                                                    <td>${data[i].Topology}</td>
                                                    <td><span class="label label-success">COMPLETED</span></td>
                                                    <td>
    <a href="#" class="OpenMap" title="OpenMap" data-toggle="tooltip"><i class="glyphicon glyphicon-eye-open"> </i></a>
    <a href="#" class="Clone" title="Clone" data-toggle="tooltip"><i class="glyphicon glyphicon-pencil"> </i></a>
    <a href="#" class="Delete" title="Delete" data-toggle="tooltip"><i class="glyphicon glyphicon-trash"> </i></a>
                            </td>
                                                </tr>
	      `;
	      templateDivBox = $(template);
	      // console.log(templateDivBox);
	      templateDivBox
	        .appendTo(".row .col-md-12 .panel .panel-body .table")
	        .hide()
	        .slideDown("slow");
	      template = "";
	    }
	    console.log({template});
	    var time = Date();
	    time = time.split(" ");
	    //console.log(time);
	    var updateTime =
	      time[4] +
	      " on " +
	      time[0] +
	      "," +
	      time[2] +
	      " " +
	      time[1] +
	      "," +
	      time[3];
	    // var templateDiv=$(template);

	    //templateDivBox.appendTo(".dashboard-main-div .card-body").hide().slideDown();
	    //$(".dashboard-main-div .card-body").append(template).hide().slideDown();
	    $(".panel .panel-footer")
	      .empty()
	      .html(updateTime);
	  };
  
	// Open Map For Search 
	  $(document).ready(function() {
	    $("body").delegate(" tbody > tr > td a", function() {
	      //document.location.href = "home.html";
	      console.log("Open Clicked");
	      var buttonText = $(this).attr('class'),
	        networkNameStr,
	        networkTopologyStr;
	      console.log("ButtonText",buttonText);

	      networkNameStr = $(this)
	        .parent()
	        .find("a")
	        .html();
	      console.log(
	    	      " networkNameStr",
	    	        networkNameStr);
	      networkTopologyStr = $(this)
	        .parent()
	        .find(".card-network-topology")
	        .html();
	      console.log(
	        "networkTopology::" + networkTopologyStr,
	        " networkNameStr",
	        networkNameStr
	      );
	      networkTopology = networkTopologyStr;

	      if (buttonText == "btn btn-success") {
	        fOpenMapDashboard(networkNameStr, sessionStorage.getItem("UserName"));
	      } else if (buttonText == "Delete") {
	        fDeleteMapDashboard(networkNameStr, sessionStorage.getItem("UserName"));
	      } else if (buttonText == "Clone") {
	        fCloneMapDashboard(networkNameStr, sessionStorage.getItem("UserName"));
	      }
	    });
	  });
	  
	  

  function fShowLoader(msg) {
    let newDiv = $(`<div class="full-cover">
			<img src="../images/load.gif" />
			<p class="lead">${msg}</p>
		</div>`);

    $("body").append(newDiv);
  }

  function fHideLoader() {
    $(".full-cover").remove();
  }
})(jQuery); // End of use strict
