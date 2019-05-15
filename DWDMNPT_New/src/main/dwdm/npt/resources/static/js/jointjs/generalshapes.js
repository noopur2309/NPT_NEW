var graph = new joint.dia.Graph;



//Canvas where shapes are dropped
var graph = new joint.dia.Graph,
  paper = new joint.dia.Paper({
    el: $('#canvasId'),
    height: 950,
    width:1080,
    model: graph      
  });

// Canvas from which you take shapes
var stencilGraph = new joint.dia.Graph,
  stencilPaper = new joint.dia.Paper({
    el: $('#generalshapesId'),
    height: 200,
    width:300,
    model: stencilGraph,
    interactive: false,
    gridSize: 15,
    drawGrid:true    
  });


var cb = new joint.shapes.basic.Circle({
    position: { x: 300, y: 70 },
    size: { width: 100, height: 40 },
    attrs: { text: { text: 'basic.Circle' } }
});

stencilPaper.addCell(cb);





stencilPaper.on('cell:pointerdown', function(cellView, e, x, y) {
	  $('body').append('<div id="flyPaper" style="position:fixed;z-index:100;opacity:.7;pointer-event:none;"></div>');
	  //alert(x+'and'+y);
	  var flyGraph = new joint.dia.Graph,
	    flyPaper = new joint.dia.Paper({
	      el: $('#flyPaper'),
	      model: flyGraph,
	      interactive: false
	    }),
	    flyShape = cellView.model.clone(),
	    pos = cellView.model.position(),
	  
	    offset = {
	      x: x - pos.x,
	      y: y - pos.y
	    };

	  //alert('posx'+pos.x+'pos.y'+pos.y);
	  //alert(offset.x+'offset xandy'+offset.y);
	  flyShape.position(0, 0);
	  flyGraph.addCell(flyShape);
	  $("#flyPaper").offset({
	    left: e.pageX - offset.x,
	    top: e.pageY - offset.y
	  });
	 
	  $(document).ready(function() {
	  $('body').on('mousemove.fly', function(e) {
		 // alert('mouse fly');
	    $("#flyPaper").offset({
	      left: e.pageX - offset.x,
	      top: e.pageY - offset.y
	    });
	  });
	 
	  });
	  
	  $('body').on('mouseup.fly', function(e) {
		 // alert('mouseup fly');
		  /*
		   * x and y : Page x and Y Coordinates
		   * target  : canvas [el width and top and height]
		   * 
		   */
	    var x = e.pageX,
	      y = e.pageY,
	      target = paper.$el.offset();
	      //alert(x+"and"+y);
	      //alert("target left"+target.left+'top'+target.top+'height'+target.height);      
	      //alert("paper width "+paper.$el.width()+'height'+paper.$el.height());
	      //alert("offset x"+offset.x+'and y'+offset.y);
	    // Dropped over paper ?
	    if (x > target.left && x < target.left + paper.$el.width() && y > target.top && y < target.top + paper.$el.height()) {
	    //alert("OBject In");
	      var s = flyShape.clone();
	      s.position(x - target.left - offset.x, y - target.top - offset.y);
	      //alert("final s.x"+x - target.left - offset.x+'y'+y - target.top - offset.y);
	      graph.addCell(s);
	    }
	    $('body').off('mousemove.fly').off('mouseup.fly');
	    flyShape.remove();
	    $('#flyPaper').remove();
	    //alert('done');
	  });
	});
