/**
 * * paper utility module
 */
var paperUtil = (function() {
  var minScale = 0.6,
    maxScale = 1.5,
    scaleFactor = 0.2,
    scaleReset = 1;
  return {
    /**
     * Get Node Degree | parameter : cellView or cellModel | return: Degree
     */
    fGetNodeDegree: function(cell) {
      let node = cell.model || cell;
      // console.log("Node :",node);
      let nodeDegree = 0;
      // let nodeId = node.attributes.nodeProperties.nodeId;
      let nodeId = node.id;
      let linksArr = _.filter(graph.getLinks(), function(link) {
        // let linkSourceId = link.source().NodeId;
        // let linkTargetId = link.target().NodeId;
        let linkSourceId = link.source().id;
        let linkTargetId = link.target().id;

        // console.log("Src id:",linkSourceId," Dest id:",linkTargetId);
        // if source or target
        return (linkSourceId == nodeId) | (linkTargetId == nodeId);
      });

      // console.log("linksArr : ",linksArr);
      nodeDegree = linksArr.length;

      // console.log("Ndoe Degree :", nodeDegree);
      return nodeDegree;
    },
    /**
     * Node Degree Checks, true if connection is possible, else false
     *  | parameter : nodetype.nodedegree | return: true/false
     */
    fValidateNodeConnectionUtility: function(nodeType, nodeDegree) {
      // console.log(
      //   "%^%^% fValidateNodeConnectionUtility : Type",
      //   nodeType,
      //   " nodeDef=gree :",
      //   nodeDegree
      // );
      switch (nodeType) {
        case nptGlobals.NodeTypeCDROADM:
        case nptGlobals.NodeTypeROADM:
        case nptGlobals.NodeTypeHub:
          {
            if (nodeDegree > nptGlobals.CDC_MAX_DEGREE) return false;
          }
          break;
        case nptGlobals.NodeTypeTE:
          {
            if (nodeDegree > nptGlobals.TE_MAX_DEGREE) return false;
          }
          break;
        case nptGlobals.NodeTypeILA:
        case nptGlobals.NodeTypeTwoDegreeRoadm:
          {
            if (nodeDegree > nptGlobals.BS_MAX_DEGREE) return false;
          }
          break;

        default:
          return true;
          break;
      }
      return true;
    },
    /**
     * Link Connection validation
     *  | parameter : src,dest,link | return: true/false
     */
    fValidateConnection: function(src, dest, link) {
      // console.log("src:",src);
      // console.log("dest:",dest);
      // console.log("link:",link);

      let srcModel = src.model,
        destModel = dest.model,
        srcType,
        srcDegree,
        destType,
        destDegree;

      if (src && dest) {
        srcType = srcModel.get("type");
        srcDegree = paperUtil.fGetNodeDegree(srcModel);

        destType = destModel.get("type");
        destDegree = paperUtil.fGetNodeDegree(destModel);

        // console.log(
        //   "srcType:",
        //   srcType,
        //   "srcDegree:",
        //   srcDegree,
        //   "destType:",
        //   destType,
        //   "destDegree:",
        //   destDegree
        // );

        if (
          this.fValidateNodeConnectionUtility(srcType, srcDegree) &&
          this.fValidateNodeConnectionUtility(destType, destDegree + 1)
        ) {
          return true;
        }
      }
    },
    /************************************************************************
     * adjustVertices for multiple between same jointjs elements
     ************************************************************************/
    adjustVertices: function(graph, cell) {
      // if `cell` is a view, find its model
      cell = cell.model || cell;

      if (cell instanceof joint.dia.Element) {
        // `cell` is an element

        _.chain(graph.getConnectedLinks(cell))
          .groupBy(function(link) {
            // the key of the group is the model id of the link's source or target
            // cell id is omitted
            return _.omit([link.source().id, link.target().id], cell.id)[0];
          })
          .each(function(group, key) {
            // if the member of the group has both source and target model
            // then adjust vertices
            if (key !== "undefined")
              paperUtil.adjustVertices(graph, _.first(group));
          })
          .value();

        return;
      }

      // `cell` is a link
      // get its source and target model IDs
      var sourceId = cell.get("source").id || cell.previous("source").id;
      var targetId = cell.get("target").id || cell.previous("target").id;

      // if one of the ends is not a model
      // (if the link is pinned to paper at a point)
      // the link is interpreted as having no siblings
      if (!sourceId || !targetId) return;

      // identify link siblings
      var siblings = _.filter(graph.getLinks(), function(sibling) {
        var siblingSourceId = sibling.source().id;
        var siblingTargetId = sibling.target().id;

        // if source and target are the same
        // or if source and target are reversed
        return (
          (siblingSourceId === sourceId && siblingTargetId === targetId) ||
          (siblingSourceId === targetId && siblingTargetId === sourceId)
        );
      });

      var numSiblings = siblings.length;
      switch (numSiblings) {
        case 0: {
          // the link has no siblings
          break;
        }
        case 1: {
          // there is only one link
          // no vertices needed
          cell.unset("vertices");
          break;
        }
        default: {
          // there are multiple siblings
          // we need to create vertices

          // find the middle point of the link
          var sourceCenter = graph
            .getCell(sourceId)
            .getBBox()
            .center();
          var targetCenter = graph
            .getCell(targetId)
            .getBBox()
            .center();
          var midPoint = g.Line(sourceCenter, targetCenter).midpoint();

          // find the angle of the link
          var theta = sourceCenter.theta(targetCenter);

          // constant
          // the maximum distance between two sibling links
          var GAP = 20;

          _.each(siblings, function(sibling, index) {
            // we want offset values to be calculated as 0, 20, 20, 40, 40, 60, 60 ...
            var offset = GAP * Math.ceil(index / 2);

            // place the vertices at points which are `offset` pixels perpendicularly away
            // from the first link
            //
            // as index goes up, alternate left and right
            //
            //  ^  odd indices
            //  |
            //  |---->  index 0 sibling - centerline (between source and target centers)
            //  |
            //  v  even indices
            var sign = index % 2 ? 1 : -1;

            // to assure symmetry, if there is an even number of siblings
            // shift all vertices leftward perpendicularly away from the centerline
            if (numSiblings % 2 === 0) {
              offset -= (GAP / 2) * sign;
            }

            // make reverse links count the same as non-reverse
            var reverse = theta < 180 ? 1 : -1;

            // we found the vertex
            var angle = g.toRad(theta + sign * reverse * 90);
            var vertex = g.Point.fromPolar(offset, angle, midPoint);

            // replace vertices array with `vertex`
            sibling.vertices([vertex]);
            sibling.set("connector", { name: "smooth" });
          });
        }
      }
    },
    /**
     * Get Link Siblings | parameter : cellView or cellModel of Link | return: Siblings Link Array
     */
    fGetLinkSiblings: function(cell) {
      let sourceId = cell.get("source").id || cell.previous("source").id;
      let targetId = cell.get("target").id || cell.previous("target").id;

      let siblings = [];
      // if one of the ends is not a model
      // (if the link is pinned to paper at a point)
      // the link is interpreted as having no siblings
      if (!sourceId || !targetId) return siblings;

      // identify link siblings
      return (siblings = _.filter(graph.getLinks(), function(sibling) {
        let siblingSourceId = sibling.source().id;
        let siblingTargetId = sibling.target().id;

        // if source and target are the same
        // or if source and target are reversed
        return (
          (siblingSourceId === sourceId && siblingTargetId === targetId) ||
          (siblingSourceId === targetId && siblingTargetId === sourceId)
        );
      }));
    },

    /************************************************************************
     * ZoomIn function to zoom in on map canvas area
     ************************************************************************/
    zoomIn: function() {
      //      paperScroller.zoom(0.2, { max: 2 });

      var ScaleVar = V(paper.viewport).scale();
      //      var x=dragLastPosition.x,
      //      y=dragLastPosition.y;
      //      //dragStartPosition = { x: x * ScaleVar.sx, y: y * ScaleVar.sy};
      //      dragEvent(null,x,y);
      if (ScaleVar.sx < maxScale && ScaleVar.sy < maxScale) {
        console.log(
          " Inside: ScaleVar.sx " + ScaleVar.sx + " ScaleVar.sy" + ScaleVar.sy,
          " this.scaleFactor:",
          this.scaleFactor
        );
        ScaleVar.sx = ScaleVar.sx + scaleFactor;
        ScaleVar.sy = ScaleVar.sy + scaleFactor;
        paper.setOrigin(0, 0);
        paper.scale(ScaleVar.sx, ScaleVar.sy);
      }
      dragMouseLeaveEvent();
      console.log(
        " zoomIn: ScaleVar.sx " + ScaleVar.sx + " ScaleVar.sy" + ScaleVar.sy
      );
    },

    /************************************************************************
     * ZoomOut function to zoom out on map canvas area
     ************************************************************************/
    zoomOut: function() {
      //      paperScroller.zoom(-0.2, { min: 0.6 });

      var ScaleVar = V(paper.viewport).scale();
      //      var x=dragLastPosition.x,
      //      y=dragLastPosition.y;
      //      //dragStartPosition = { x: x * ScaleVar.sx, y: y * ScaleVar.sy};
      //      dragEvent(null,x,y);
      if (ScaleVar.sx > minScale && ScaleVar.sy > minScale) {
        ScaleVar.sx = ScaleVar.sx - scaleFactor;
        ScaleVar.sy = ScaleVar.sy - scaleFactor;
        paper.setOrigin(0, 0);
        paper.scale(ScaleVar.sx, ScaleVar.sy);
      }
      dragMouseLeaveEvent();
      console.log(
        " zoomOut: ScaleVar.sx " + ScaleVar.sx + " ScaleVar.sy" + ScaleVar.sy
      );
      $("svg#v-3").css({
        width: "100%"
      });
    },

    /************************************************************************
     * ResetZoom function to reset to original value on map canvas area
     ************************************************************************/
    resetZoom: function() {
      //      paperScroller.zoomToFit({ padding: 10 });

      console.log(" Reset Zoom");
      var ScaleVar = V(paper.viewport).scale();
      //		var x=dragLastPosition.x,
      //		y=dragLastPosition.y;
      //		dragStartPosition = { x: x * ScaleVar.sx, y: y * ScaleVar.sy};

      ScaleVar.sx = scaleReset;
      ScaleVar.sy = scaleReset;
      paper.scale(ScaleVar.sx, ScaleVar.sy);
      console.log(
        " Reset: ScaleVar.sx " + ScaleVar.sx + " ScaleVar.sy" + ScaleVar.sy
      );
      // paper.scaleContentToFit();
    },
    /************************************************************************
     * Lock paper Scrolling
     ************************************************************************/
    lockPaper: function() {
      //      paperScroller.lock();
      $("#lockUnlockPaper")
        .empty()
        .append(
          `<i class="fa fa-lock" aria-hidden="true" ng-if="dragLock"></i>`
        );
    },
    /************************************************************************
     * UnLock paper Scrolling
     ************************************************************************/
    unlockPaper: function() {
      //      paperScroller.unlock();
      $("#lockUnlockPaper")
        .empty()
        .append(
          `<i class="fa fa-unlock" aria-hidden="true" ng-if="dragLock"></i>`
        );
    }
  }; // return end
})();
