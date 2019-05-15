/**
 * @author hp
 * @date 18th April, 2017
 * @brief This is the first entry point for angular control under 'chassisViewController'
 *        which automatically get called under ng-app
 */

/**Defining 'chassisView' angular app*/
var pathConstant = "/js/angularJs/Images/";
/**
 * Controller Start
 */
app.controller("chassisViewController", function($scope, $http) {
  /**
   * Called on Chassis View Tab click [switching view from canvas to chassis]
   */
  $("#chassisViewLiId").click(function(e) {
    console.log(_UNSET_VALUE);
    nptGlobals.setCanvasActive(_UNSET_VALUE);
    var getAllChassisPostData = new Object();
    getAllChassisPostData.NetworkInfo = new Object();
    getAllChassisPostData.NetworkInfo.NetworkName = sessionStorage.getItem(
      "NetworkName"
    );
    getAllChassisPostData.NetworkInfo.UserName = sessionStorage.getItem(
      "UserName"
    );
    getAllChassisPostData.NetworkInfo.Topology = window.NetworkTopology;

    if (!nptGlobals.getInventoryGeneratedStatus()) {
      bootBoxDangerAlert(
        "Generate Inventory first in order to see Chassis view."
      );
      e.stopPropagation();
      canvasActive();
    } else {
      $http({
        method: "post",
        url: "/getChassisJsonArrayDataRequest",
        data: getAllChassisPostData
      })
        .success(function(response) {
          setTimeout(function() {
            $scope.$apply(function() {
              console.log("http ajax response for angular ");
              console.log(response);
              $scope.names =
                response.NodeData; /**Get number of nodes which are part of the final equipment*/

              if (nptGlobals.getChassisViewClickedFirstTime()) {
                $scope.selectedNode = $scope.names[0];
                $scope.neChangeHandler();
              }

              nptGlobals.setChassisViewClickedFirstTime(false);
            });
          }, 200);
        })
        .error(function(error) {
          ///  bootbox.alert("Network Nodes Not Found!!" /*+ error*/);
        });
    }
  });

  $scope.testCall = function() {
    alert("Yuppe ... Inside Angular now !!");
  };

  /**
   * Called every time new NE selected from drop down
   */

  function showLoadDialogue() {
    let dialog = bootbox.dialog({
      title: "Chassis View",
      message:
        '<p><i class="fa fa-spin fa-spinner"></i> Preparing The Chassis...</p>',
      backdrop: true,
      onEscape: true
    });
    let data =
      '<p>Zoom In <i class="fa fa-search-plus"></i>  - Scroll Down</p>' +
      '<p>Zoom Out <i class="fa fa-search-minus"></i> - Scroll Up</p>' +
      '<p>Navigate <i class="fa fa-hand-grab-o"></i> - Hold and Drag</p>';
    dialog.init(function() {
      setTimeout(function() {
        dialog.find(".bootbox-body").html(data);
      }, 5000);
    });
  }

  $scope.neChangeHandler = function() {
    console.log("Item changed to " + $scope.names);
    showLoadDialogue();
    var getChassisPostData = new Object();
    getChassisPostData.NetworkInfo = new Object();
    getChassisPostData.NetworkInfo.NetworkName = sessionStorage.getItem(
      "NetworkName"
    );
    getChassisPostData.NetworkInfo.UserName = sessionStorage.getItem(
      "UserName"
    );
    getChassisPostData.NetworkInfo.Topology = window.NetworkTopology;

    getChassisPostData.selectedNode = $scope.selectedNode;

    /**carry out the ajax call for the response*/
    $http({
      method: "post",
      url: "/getChassisViewjsonDataRequest",
      data: getChassisPostData //$scope.selectedNode
    })
      .success(function(response) {
        setTimeout(function() {
          $scope.$apply(function() {
            console.log("http ajax response for angular ");
            console.log(response);
            $scope.chassisViewDbData = response;
            $scope.selectedRack = 1;
            let noOfRacks =
              $scope.chassisViewDbData.chassis.specs.rackList.length;
            $scope.racks = [...Array(noOfRacks).keys()];
            for (i in $scope.racks) {
              $scope.racks[i] = $scope.racks[i] + 1;
            }
            init($scope);
          });
        }, 200);
        ///$scope.hideProcessingIcon();
      })
      .error(function(error) {
        $scope.topologyViewDetails = {};
        $scope.topologyViewDetails.hovered;
        $scope.topologyViewDetails.dragging;
        $scope.topologyViewDetails.toopTipText;
        $scope.topologyViewDetails.mouseX;
        $scope.topologyViewDetails.mouseY;
      });
  };
});
