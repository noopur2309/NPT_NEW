$(document).ready(function() {
  console.log("nwMapHome.js loaded");

  /************************************************************************
   * Auto load function on home page startup
   ************************************************************************/
  (function(nptGlobals) {
    //adjust height of sidebar and Network Element Bar
    adjustSideBarHeightOnLOad();
    adjustWidthOnLoad();

    //console.log(localStorage.getItem("mapStr"));
    if (localStorage.getItem("openMap") == 0 ||
    	localStorage.getItem("dashboardOpenMapCall") == 0)
    {
      console.log(localStorage.getItem("mapStr"));
      if (localStorage.getItem("mapStr") === "undefined") {
        console.log("replacing location to dashboard");
        location.replace("NewDashboard.html");
      } else {
        console.log("Loading Modal ");
        $("#createMapModal").modal("show");
      }
    }
    else 
    {
      $("#home-username").text(localStorage.getItem("UserName"));
      sessionStorage.setItem("UserName", localStorage.getItem("UserName"));
      sessionStorage.setItem(
        "NetworkName",
        localStorage.getItem("NetworkName")
      );
      sessionStorage.setItem(
        "Topology",
        localStorage.getItem("NetworkTopology")
      );

      nptGlobals.NetworkTopology = sessionStorage.getItem("Topology");
      nptGlobals.NetworkName = sessionStorage.getItem("NetworkName");
      console.log(
        "********** Network Topology************** :: ",
        nptGlobals.NetworkTopology
      );

      if (localStorage.getItem("mapStr") != "null") {
        console.log(localStorage.getItem("mapStr"));
        drawMapFromJSON(localStorage.getItem("mapStr"));
      }
      fInitializeConfigurationPane();
    }

    //Initial values, so that on page reloads to NewDashboard.html
    localStorage.setItem("openMap", 0);
    localStorage.setItem("dashboardOpenMapCall", 0);
    localStorage.setItem("mapStr", undefined);
  })(nptGlobals);

  /************************************************************************
   * Instantiate BrownField/GreenField Network on Open Map from Dashboard
   ************************************************************************/
   function fInitializeConfigurationPane() {
    let postJsonData = jsonPostObject();
    overlayOn("Initializing Network", ".body-overlay");
    
    serverPostMessage("instantiateBrownField", postJsonData)
      .then(function(data) {
        console.log("Network Load from Db Initialized");
        saveNetworkState(data);
        initializeConfigurationPaneFromMap();
        overlayOff("Network Initialized Succesfully", ".body-overlay");
      })
      .catch(function(e) {
        console.log("fail", e);
        overlayOff("Network Initialization failed", ".body-overlay");
      });
  };
});
  
