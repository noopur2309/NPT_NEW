/* ********global Variables*****************    */
var color='black';
var elementCount=0;
var linkCount=0;
var station='hello';
var myArr=[];
var selected;

/************************************************************************
 ************************************************************************
Var define along with their div Id and property
 ************************************************************************
 ************************************************************************/
//General Canvas where shapes are dropped
var graph = new joint.dia.Graph,
paper = new joint.dia.Paper({
	el: $('#canvasId'),
	height: 950,
	width: 1080,
	model: graph,   
	snapLinks: {
		radius: 75
	},
	markAvailable: false,
	defaultLink:new joint.dia.Link({
		attrs: {
			'.connection':{stroke:'#777','stroke-width':3},
			'.connection-wrap':{stroke:'#E74C3C','stroke-width':10,'stroke-opacity':0.2},
			'.marker-source': { stroke: '#777', fill: '#777', d: 'M 10 0 L 0 5 L 10 10 z' },
			'.marker-target': { stroke: '#777', fill: '#777', d: 'M 10 0 L 0 5 L 10 10 z' },
			'.marker-arrowhead':{stroke: '#777', fill: '#777', d: 'M 10 0 L 0 5 L 10 10 z'}
		},
		linkProperties:{
			'spanLength':'',
			'fiberType':'',
			'costMetric':'',
			'opticalParameter':'',
			'srlg':''                  	
		}

	}),
	linkPinning: false,
	gridSize: 15,
	drawGrid: true,
	interactive: function(cellView) {
        if (cellView.model instanceof joint.dia.Link) {
            // Disable the default vertex add functionality on pointerdown.
            return { vertexAdd: false };
        }
        return true;
    }

}

);

/************************************************************************
 * Navbar var Define Section wise
 ************************************************************************/

//Presentational attributes.
var attrs = {
		elementDefault: {
			// text: { fill: '#fff', style: { 'text-shadow': '1px 1px 1px #999', 'text-transform': 'capitalize' } },
			rect: { stroke: 'black','stroke-width':'1' },
			'.label':{'stroke':'white'}
		},
		elementSelected: {
			rect: { stroke: '#d9534f','stroke-width':'8' },
			'.label':{'stroke':'#d9534f'}
		},
		elementHighlighted: {
			rect: { stroke: '#d9534f','stroke-width':'8','stroke-opacity':'0.7' },
			'.label':{'stroke':''}
		},
		linkDefault: {
			'.connection':{stroke:'#777','stroke-width':3},
			'.marker-arrowhead':{stroke: '#777', fill: '#777', d: 'M 10 0 L 0 5 L 10 10 z'}
		},
		linkDefaultDirected: {
			'.marker-target': { d: 'M 6 0 L 0 3 L 6 6 z' }
		},
		linkHighlighted: {
			'.connection': { stroke: 'yellow', 'stroke-width': 10, 'stroke-opacity':0.5 },
			'.marker-arrowhead':{fill:'yellow',d: 'M 14 0 L 0 7 L 14 14 z',stroke: 'rgba(0,0,0,0)'}
		}
};

//Create a node with `id` at the position `p`.
function n(id, p) {
	var node = (new joint.shapes.basic.Circle({
		id: id,
		position: { x: g.point(p).x, y: g.point(p).y },
		size: { width: 40, height: 40 },
		attrs: attrs.elementDefault
	})).addTo(graph);
	node.attr('text/text', id);
	return node;
}

//Create a link between a source element with id `s` and target element with id `t`.
function l(s, t) {
	var link=(new joint.dia.Link({
		id: [s,t].sort().join(),
		source: { id: s },
		target: { id: t },
		z: -1,
		attrs: attrs.linkDefault
	})).addTo(graph);
	return link;
}
//Navbar General Shapes from which you take shapes
var stencilGraphGS = new joint.dia.Graph,
    stencilPaperGS = new joint.dia.Paper({
            el: $('#generalshapesId'),
            height: 200,
            width: 300,
            model: stencilGraphGS,
            interactive: false,
        }

    );
// Navbar Network Elements from which you take shapes
var stencilGraph = new joint.dia.Graph,
    stencilPaper = new joint.dia.Paper({
            el: $('#netelemId'),
            height: 200,
            width: 300,
            model: stencilGraph,
            interactive: false,
            gridSize: 15,
            drawGrid: true
        }

    );


/************************************************************************
 * Navbar Various Shapes Define Section wise
 ************************************************************************/
//Custom Link
//joint.shapes.NetworkLink.Link = joint.dia.Link.extend({

//defaults: {
//type: 'myclass.Link',
//attrs: { '.connection' : { 'stroke-width' :  5 }},
//smooth:true
//}
//});

//Network Elements

//roadm element

joint.shapes.devs.roadm = joint.shapes.devs.Model.extend({	

	markup: '<g class="rotatable"><g class="scalable"><rect class="body"/></g><image/><text class="label"/><g class="inPorts"/><g class="outPorts"/></g>',

	defaults: joint.util.deepSupplement({

		type: 'devs.roadm',
		size: {
			width: 50,
			height: 50
		},
		attrs: {
			rect: {
				'stroke': 'black',
				'stroke-width': 2,
				fill: {
					type: 'linearGradient',
					stops: [{
						offset: '0%',
						color: 'white'
					}, {
						offset: '50%',
						color: '#d1d1d1'
					}],
					attrs: {
						x1: '0%',
						y1: '0%',
						x2: '0%',
						y2: '100%'
					}
				}
			}, 
//			'.body': {
//			stroke: color
//			},
			'.label': {
				text: 'R',
				fill:'white',
				'stroke-width':2,
				stroke:'white',
				'ref-y': 30,
				'ref-x':30
			},
			'.inPorts circle': {
				fill: 'rgba(128, 128, 128, 0)',
				'stroke-width': 0
			},
			'.outPorts circle': {
				fill: 'rgba(128, 128, 128, 0)',
				'stroke-width': 0
			},
			image: {
				'xlink:href': 'images/images.jpg',
				width: 70,
				height: 70,
				'ref-x': .5,
				'ref-y': .5,
				ref: 'rect',
				'x-alignment': 'middle',
				'y-alignment': 'middle'
			},

		},
		nodeProperties:{
			stationName:'',
			siteName:'',
			gneFlag:'',
			vlanTag:'',
			degree:''
		}      

	}, joint.shapes.devs.Model.prototype.defaults)
});

joint.shapes.devs.roadmView = joint.shapes.devs.ModelView;

// Usage:
var portsVar = {

        groups: {
        	'north': {
                position: 'top',
                attrs: {
                	circle: { fill: '#ffffff', stroke: '#3c763d', 'stroke-width': 2, r: 8, magnet: true },
                	text: { text: 'Direction',
                		stroke: '#3c763d',
                		'stroke-width': 1
                	  }
                },
    			flag:0
            },
       	
	       	'south': {
	               position: 'bottom',
	               attrs: {
	                   circle: { fill: '#ffffff', stroke: '#3c763d', 'stroke-width': 2, r: 8, magnet: true },
	                   text: { text: 'Direction',
	                   		stroke: '#3c763d',
	                   		'stroke-width': 1
	                   	  }
	               },
	               flag:0 
	           },
	           
	       	'east': {
                position: 'right',
                attrs: {
                	circle: { fill: '#ffffff', stroke: '#3c763d', 'stroke-width': 2, r: 8, magnet: true },
                	text: { text: 'Direction',
                		stroke: '#3c763d',
                		'stroke-width': 1
                	  }
                },
                flag:0
            },
       	
	       	'west': {
	               position: 'left',
	               attrs: {
	                   circle: { fill: '#ffffff', stroke: '#3c763d', 'stroke-width': 2, r: 8, magnet: true },
	                   text: { text: 'Direction',
	                   		stroke: '#3c763d',
	                   		'stroke-width': 1
	                   	  }
	               },
	               flag:0
	           },

	           
        	'ne': {
              /*   position: 'right',*/
        		position:{name:'right',
        			 args: { dr: 0, dx: 0, dy: -20 }
        		},
                 attrs: {
                 	circle: { fill: '#ffffff', stroke: '#3c763d', 'stroke-width': 2, r: 8, magnet: true },
                 	text: { text: 'Direction',
                 		stroke: '#3c763d',
                 		'stroke-width': 1
                 	  }
                 },
                 flag:0
             },
        	
        	'nw': {
        		position:{
        			name:'left',        		
        			args: { dr: 0, dx: 0, dy: -20 }
        		},
                attrs: {
                    circle: { fill: '#ffffff', stroke: '#3c763d', 'stroke-width': 2, r: 8, magnet: true },
                    text: { text: 'Direction',
                    		stroke: '#3c763d',
                    		'stroke-width': 1
                    	  }
                },
                flag:0
            },
           
            'se': {
            	position:{
        			name:'right',        		
        			args: { dr: 0, dx: 0, dy: 20 }
        		},
                attrs: {
                    circle: { fill: '#ffffff', stroke: '#3c763d', 'stroke-width': 2, r: 8, magnet: true },
                    text: { text: 'Direction',
                    		stroke: '#3c763d',
                    		'stroke-width': 1
                    	  }
                },
                flag:0
            },
           
            'sw': {
            	position:{
        			name:'left',        		
        			args: { dr: 0, dx: 0, dy: 20 }
        		},
                attrs: {
                    circle: { fill: '#ffffff', stroke: '#3c763d', 'stroke-width': 2, r: 8, magnet: true },
                    text: { text: 'Direction',
                    		stroke: '#3c763d',
                    		'stroke-width': 1
                    	  }
                },
                flag:0
            },
           
           
        },
    
} 
var roadm = new joint.shapes.devs.roadm({
    position: {
        x: 40,
        y: 50
    },
    size: {
        width: 70,
        height: 70
    },
    
    ports: portsVar,
    
    ///inPorts: ['c'],
    ///outPorts: ['d']
});


//hub element

joint.shapes.devs.hub = joint.shapes.devs.Model.extend({

	markup: '<g class="rotatable"><g class="scalable"><rect class="body"/></g><image/><text class="label"/><g class="inPorts"/><g class="outPorts"/></g>',

	defaults: joint.util.deepSupplement({

		type: 'devs.hub',
		size: {
			width: 50,
			height: 50
		},
		attrs: {
			rect: {
				'stroke-width': 2,
				'stroke': 'black',
				fill: {
					type: 'linearGradient',
					stops: [{
						offset: '0%',
						color: 'white'
					}, {
						offset: '50%',
						color: '#d1d1d1'
					}],
					attrs: {
						x1: '0%',
						y1: '0%',
						x2: '0%',
						y2: '100%'
					}
				}
			},
//			'.body': {
//			stroke: color
//			},
			'.label': {
				text: 'H',
				fill:'white',
				'stroke-width':2,
				stroke:'white',
				'ref-y': 30,
				'ref-x':30
			},
			'.inPorts circle': {
				fill: 'rgba(128, 128, 128, 0)',
				'stroke-width': 0
			},
			'.outPorts circle': {
				fill: 'rgba(128, 128, 128, 0)',
				'stroke-width': 0
			},
			image: {
				'xlink:href': 'images/download.jpg',
				width: 70,
				height: 70,
				'ref-x': .5,
				'ref-y': .5,
				ref: 'rect',
				'x-alignment': 'middle',
				'y-alignment': 'middle'
			}                
		},
		nodeProperties:{
			stationName:'',
			siteName:'',
			gneFlag:'',
			vlanTag:'',
			degree:''
		}     

	}, joint.shapes.devs.Model.prototype.defaults)
});

joint.shapes.devs.hubView = joint.shapes.devs.ModelView;


var hub = new joint.shapes.devs.hub({
    position: {
        x: 140,
        y: 50
    },
    size: {
        width: 70,
        height: 70
    },
    ports: portsVar,
   /// inPorts: ['a'],
   /// outPorts: ['b']
});

/*
var roadm = new joint.shapes.devs.Model({
    position: { x: 40, y: 50 },
    size: { width: 60, height: 50 },
    inPorts: [''],
    outPorts: [''],
    attrs: {
        '.label': { text: 'ROADM', 'ref-x': .5, 'ref-y': .3},
        rect: { fill: '#2ECC71' },
        '.inPorts circle': { fill: '#16A085' },
        '.outPorts circle': { fill: '#E74C3C' }
    }
});

var hub = new joint.shapes.devs.Model({
    position: { x: 170, y: 50 },
    size: { width: 60, height: 50 },
    inPorts: [''],
    outPorts: [''],
    attrs: {
        '.label': { text: 'HUB', 'ref-x': .5, 'ref-y': .3 },
        rect: { fill: '#2ECC71' },
        '.inPorts circle': { fill: '#16A085' },
        '.outPorts circle': { fill: '#E74C3C' }
    }
});
*/




/************************************************************************
 * Add Objects into Respective Navbar Zone
 ************************************************************************/


//Network Elements 
stencilGraph.addCells([roadm, hub]);

//General Shapes
//stencilGraphGS.addCells([circle, image]);


//Network Links Adds
//stencilGraphLinks.addCells([link]);




/************************************************************************
 * Events to Handle and Placing on Canvas : For General Shapes
 ************************************************************************/



stencilPaperGS.on('cell:pointerdown', function(cellView, e, x, y) {

        $('body').append('<div id="flyPaper" style="position:fixed;z-index:100;opacity:.7;pointer-event:none;"></div>');

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
        flyShape.position(0, 0);
        flyGraph.addCell(flyShape);
        $("#flyPaper").offset({
            left: e.pageX - offset.x,
            top: e.pageY - offset.y
        });
        $('body').on('mouseup.fly', function(e) {

            /*
             * x and y : Page x and Y Coordinates
             * target  : canvas [el width and top and height]
             */
            var x = e.pageX,
                y = e.pageY,
                target = paper.$el.offset();

            // Dropped over paper ?
            if (x > target.left && x < target.left + paper.$el.width() && y > target.top && y < target.top + paper.$el.height()) {
                var s = flyShape.clone();
                s.position(x - target.left - offset.x, y - target.top - offset.y);
                graph.addCell(s);
            }
            $('body').off('mousemove.fly').off('mouseup.fly');
            flyShape.remove();
            $('#flyPaper').remove();

        });
    }

);


/*****************************************
 * *********paper events handling******************
 * **************************************** */
var elSelected;
paper.on('cell:pointerclick', function(cellView, evt, x, y) {
	selected = cellView.model;
	//console.log(cellView);
	//console.log(cellView.model);
	// console.log("station:"+station);
	//console.log(mod);
	//model.attr('attributes/siteName', 'hello');  
	//set properties of this particular element

    console.log("pehle wala"+elSelected);
    if (elSelected)
    	{
    	elSelected.attr('.body/stroke','black');
        elSelected.attr('rect/stroke-width','1');
        elSelected.attr('.label/stroke','white');
    	}
    (elSelected = cellView.model).attr('.body/stroke','#d9534f');
    elSelected.attr('rect/stroke-width','8');
    elSelected.attr('.label/stroke','#d9534f');
    console.log(elSelected);	

	//hidePath();
	properties(selected);    	
	//console.log(mod.id);
	//console.log(mod.attr('.body'));
	//color=model.attr('.body/stroke');
	//  model.attr('.body/stroke', 'rgb(92, 184, 92)');  


});

paper.on('blank:pointerclick', function(evt, x, y) { 
	//console.log('hello');
	//setColorOnClick();
	if (elSelected)
	{
		elSelected.attr('.body/stroke','black');
		elSelected.attr('rect/stroke-width','1');
		elSelected.attr('.label/stroke','white');
		elSelected=undefined;
	}

})

paper.on('cell:mouseover', function(cellView, evt, x, y) {

	// console.log('mouseover');
	//   console.log(cellView.model);

//	cellView.model.attr('.inPorts circle/fill', 'black');
//	cellView.model.attr('.outPorts circle/fill', 'black');
	if (elSelected) {
		var path = graph.shortestPath(selected, cellView.model);
		showPath(path);
		//cellView.model.attr(attrs.elementHighlighted);
	}
	
	if(cellView.model.isLink())
		{
//		$('.tool-options path').show().fadeIn("slow");
		$('.tool-remove path').show().fadeIn("slow");
		$('.tool-remove').show().fadeIn("slow");
		}

});

paper.on('cell:mouseout', function(cellView, evt, x, y) {
	//console.log('mouseout');
//	cellView.model.attr('.inPorts circle/fill', 'rgba(128, 128, 128, 0)');
//	cellView.model.attr('.outPorts circle/fill', 'rgba(128, 128, 128, 0)');
	//cellView.model.attr(attrs.elementDefault);
	hidePath();
	if(cellView.model.isLink())
	{
//	$('.tool-options path').hide().fadeOut("slow");
	$('.tool-remove path').hide().fadeOut("slow");
	$('.tool-remove').hide().fadeOut("slow");
	}
});

paper.on('cell:pointerdblclick', function(cellView, evt, x, y) {
	// cellView.model.remove();
	//properties(cellView.model);
	console.log(cellView.model);
	cellView.model.set('attributes/stationName','wow');
	console.log('Station ka Nam'+cellView.model.attributes.stationName);
});

var pathLinks = [];

function hidePath() {

	// $('#path').text('');
	_.each(pathLinks, function(link) {
		link.remove();
		//        /link.attr(attrs.linkDefault);
		//link.set('labels', []);
		//console.log(link);
	});
}

function showPath(path) {

	//$('#path').text(path.join(' -> '));
	for (var i = 0; i < path.length; i++) {
		var curr = path[i];
		var next = path[i + 1];
		//console.log(curr+next);
		if (next) {
			var link=l(curr,next);
			//var link = graph.getCell([curr, next].sort().join());
			console.log(link);
//			link.label(0, { position: .5, attrs: {
//			text: { text: ' ' + (i + 1) + ' ', 'font-size': 10, fill: 'white' },
//			rect: { rx: 8, ry: 8, fill: 'black', stroke: 'black', 'stroke-width': 5 }
//			} });
//			link.attr('.connection/stroke','#777');
//			link.attr('.connection/stroke-width','10');
//			link.attr('.connection/stroke-opacity','0.7');
			//link.attr()
			//link.attr('.label/stroke','green');
			//console.log(attrs.linkHighlighted);
			pathLinks.push(link.attr(attrs.linkHighlighted));
		}
	}
}

graph.on('add', function(cell) { 
	//console.log('hello');
	if(cell instanceof joint.dia.Link)
	{
		linkCount+=1;
		//linkInfoTab();
	}		
	else
	{
		elementCount+=1;
		nodeInfoTab();
	}

	var temp=`<p class="text-primary">LINK COUNT : `;
	temp+=linkCount;
	temp+=`</p>`;
	temp+=`<p class="text-primary">ELEMENT COUNT:`;
	temp+=elementCount;
	temp+=`</p>`;
	$("#view1").empty().append(temp);
})

graph.on('remove', function(cell) { 
	//console.log('hello');
	
	if(cell instanceof joint.dia.Link)
	{
		linkCount-=1;
		while(1)
			{
			if(	cell.attributes.target.id!=undefined)
				{
				linkInfoTab();
				console.log("breaking");
				break;
				}			
			}
		
	}		
	else
	{
		elementCount-=1;
		nodeInfoTab();	
	}

	var temp=`<p class="text-primary">LINK COUNT : `;
	temp+=linkCount;
	temp+=`</p>`;
	temp+=`<p class="text-primary">ELEMENT COUNT:`;
	temp+=elementCount;
	temp+=`</p>`;
	$("#view1").empty().append(temp);
})



/************************************************************************
 * JS for showing diagram object notification
 ************************************************************************/
/**************Download map on click****************************/

$("#saveMapId").click(function() {
    //console.log('Im herer');
    //$('#json').empty();
    var jsonStr = JSON.stringify(graph);
    
    //download file
    offerDownload('map', jsonStr);
    //if(js[0].position.x>10)
    //console.log("wow");
    graph.clear();
    console.log('executing clear....');
    graph.clear();
    graph.fromJSON(JSON.parse(jsonStr));
    console.log(graph.getElements());

    //for showing formatted json code

    /*regeStr = '', // A EMPTY STRING TO EVENTUALLY HOLD THE FORMATTED STRINGIFIED OBJECT
    f = {
            brace: 0
        }; // AN OBJECT FOR TRACKING INCREMENTS/DECREMENTS,
           // IN PARTICULAR CURLY BRACES (OTHER PROPERTIES COULD BE ADDED)

regeStr = jsonStr.replace(/({|}[,]*|[^{}:]+:[^{}:,]*[,{]*)/g, function (m, p1) {
var rtnFn = function() {
        return '<div style="text-indent: ' + (f['brace'] * 20) + 'px;">' + p1 + '</div>';
    },
    rtnStr = 0;
    if (p1.lastIndexOf('{') === (p1.length - 1)) {
        rtnStr = rtnFn();
        f['brace'] += 1;
    } else if (p1.indexOf('}') === 0) {
         f['brace'] -= 1;
        rtnStr = rtnFn();
    } else {
        rtnStr = rtnFn();
    }
    return rtnStr;
});
$('#json').append(regeStr);*/

});

//get cell cid by using its id


function getElementCid(id)
{
	var cell=graph.getCell(id);
	return cell.cid;
}

function getLinkTemplate(selected)
{
	console.log(selected);
	//console.log(selected.attributes.attrs.connection.stroke);
	var template = `<form id="propertyForm" >
		<div class="form-group" >
		<label for="lid">lid:</label>
		<input type="text" class="form-control" id="lid" value="`;
	template+=selected.cid;
	template+=`"readonly>
		</div>
		<div class="form-group">
		<label for="linkNode1">Linking Node 1:</label>
		<input type="text" class="form-control" id="linkNode1" placeholder="Link Source Node" value="`;
	template+=getElementCid(selected.attributes.source.id);
	template+=`" readonly>
		</div>
		<div class="form-group">
		<label for="linkNode2">Linking Node 2:</label>
		<input type="text" class="form-control" id="linkNode2" placeholder="Link Destination Node" value="`;
	template+=getElementCid(selected.attributes.target.id);
	template+=`" readonly>
		</div>
		<div class="form-group">
		<label for="spanLength">Span Length:</label>
		<input  type="number" min="0" max="100" step="10" class="form-control" placeholder="20" id="spanLength" value="`;
	template+=selected.attributes.linkProperties.spanLength;
	template+=`" >
		</div>
		<div class="form-group">
		<label for="fiberType">Fiber Type:</label>
		<input type="text" class="form-control" placeholder="20" id="fiberType" value="`;
	template+=selected.attributes.linkProperties.fiberType;
	template+=`" >
		</div>
		<div class="form-group">
		<label for="costMetric">Cost Metric:</label>
		<input type="text" class="form-control" id="costMetric" value="`;
	template+=selected.attributes.linkProperties.costMetric;
	template+=`" >
		</div>
		<div class="form-group">
		<label for="opticalParameter">Optical Parameter:</label>
		<input type="text" class="form-control" id="opticalParameter" value="`;
	template+=selected.attributes.linkProperties.opticalParameter;
	template+=`" >
		</div>
		<div class="form-group form-inline">
		<label for="color">Color:</label>
		<input type="color" class="form-control" id="color" value="`;
	template+=selected.attr('.connection/stroke');
	template+=`" >
		</div>
		<div class="form-group">
		<label for="colosr">Color:</label>
		<select class="form-control" id="colsor">
		<option>Blue</option>
		<option>Green</option>
		<option>Black</option>
		<option>Pink</option>
		</select>
		</div>
		<div class="form-group">
		<label for="srlg">SRLG:</label>
		<input type="number" min="0" max=""10" step="1" class="form-control" id="srlg" value="`;
	template+=selected.attributes.linkProperties.srlg;
	template+=`" >
		</div>
		<button id="linkPropertySave" type="button" class="btn btn-default btn-warning">Set Link Properties</button>

		</form>`;
	return template;
}

function getElementTemplate(selected)
{
	var template = `<form id="propertyForm" >
		<div class="form-group" >
		<label for="email">eid:</label>
		<input type="text" class="form-control" id="eid" value="`;
	template+=selected.cid;
	template+=`"readonly>
		</div>
		<div class="form-group">
		<label for="pwd">Node Type:</label>
		<input type="text" class="form-control" id="nodeType" value="`;
	template+=selected.attributes.type.substring(5,10);
	template+=`" readonly>
		</div>
		<div class="form-group">
		<label for="pwd">Site Name:</label>
		<input type="text" class="form-control" id="siteName" value="`;
	template+=selected.attributes.nodeProperties.siteName;
	template+=`">
		</div>
		<div class="form-group">
		<label for="pwd">Station Name:</label>
		<input type="text" class="form-control" id="stationName" value="`;
	template+=selected.attributes.nodeProperties.stationName;
	template+=`">
		</div>
		<div class="form-group">
		<label for="pwd">GNE Flag:</label>
		<input type="number" min="0" max="1" step"1" class="form-control" id="gneFlag" required value="`;
	template+=selected.attributes.nodeProperties.gneFlag;
	template+=`">
		</div>
		<div class="form-group">
		<label for="pwd">VLAN Tag:</label>
		<input type="text" class="form-control" id="vlanTag" value="`;
	template+=selected.attributes.nodeProperties.vlanTag;
	template+=`">
		</div>
		<div class="form-group">
		<label for="pwd">Degree:</label>
		<input type="number" min="0" max="8" step"1" class="form-control" id="degree" value="`;
	template+=selected.attributes.nodeProperties.degree;
	template+=`">
		</div>
		<button id="elementPropertySave" type="button" class="btn btn-default btn-primary">Set Element properties</button>

		</form>`;
	return template;
}

function properties(selected) {
   // console.log(model);
    //var strin = JSON.stringify(model);
   // console.log(strin);
   // var string = JSON.parse(strin);
    //console.log(string);
	var template;
	
	//if link then load link template else load element template
	if(selected.isLink())
		{
		template=getLinkTemplate(selected);
		}
	else {
		template=getElementTemplate(selected);
	}
    
   // console.log(template)    
	//append template html to property div
    $("#propertyBody").empty().append(template);
}

//save/update element properties using user input
$( "body" ).delegate( "#elementPropertySave", "click", function() {
//	e.preventDefault(e);
	//console.log(event);
	
	//store values of user input in variable
	var id=$("#eid").val(),
	sitename=$("#siteName").val(),
	stationname=$("#stationName").val(),
	gneflag=$("#gneFlag").val(),
	vlantag=$("#vlanTag").val(),
	degree=$("#degree").val();
    console.log(id,sitename,stationname);
	
    //get cell using id
    var cell=graph.getCell(id);
	//console.log("station:"+stationName);
	//console.log(cell);
	
	//set attributes of object/element/link properties defined
	cell.attributes.nodeProperties.stationName=stationname;
	cell.attributes.nodeProperties.siteName=sitename;
	cell.attributes.nodeProperties.gneFlag=gneflag;
	cell.attributes.nodeProperties.vlanTag=vlantag;
	cell.attributes.nodeProperties.degree=degree;
//	cell.attr('attributes/siteName',siteName);
//	cell.attr('attributes/stationName',stationName);	
	//console.log(cell.attr('attributes/siteName'));
	//console.log(cell.attr('attributes/stationName'));
	//console.log('Station ka Nam'+cell.attributes.stationName);
	
	//Update Node Tab
	nodeInfoTab();

	alert("Changes saved");
	});

//save/update link properties using user input
$( "body" ).delegate( "#linkPropertySave", "click", function() {
//	e.preventDefault(e);
	//console.log(event);

	//store values of user input in variable
	var id=$("#lid").val(),
	fibertype=$("#fiberType").val(),
	spanlength=$("#spanLength").val(),
	costmetric=$("#costMetric").val(),
	opticalparameter=$("#opticalParameter").val(),
	color=$("#color").val(),
	srlg=$("#srlg").val();
	console.log(color,spanlength,srlg);

	//get cell using id
	var cell=graph.getCell(id);
	//console.log("station:"+stationName);
	console.log(cell);

	//set attributes of object/element/link properties defined
	cell.attributes.linkProperties.spanLength=spanlength;
	cell.attributes.linkProperties.fiberType=fibertype;
	cell.attributes.linkProperties.costMetric=costmetric;
	cell.attributes.linkProperties.opticalParameter=opticalparameter;
	cell.attributes.linkProperties.srlg=srlg;
	cell.attr('.connection/stroke',color);
//	cell.attr('attributes/stationName',stationName);	

	//Update link Info Tab
	linkInfoTab();

	alert("Changes saved");
	});

$("#color").on('change',function(){
	
});

/************************************************************************
 * download map
 ************************************************************************/
function offerDownload(name, data) {
    var a = $('<a>');
    a.attr('download', name);
    a.attr('href', 'data:application/json,' + encodeURIComponent(JSON.stringify(data)));
    a.attr('target', '_blank');
    a.hide();
    $('body').append(a);
    a[0].click();
    a.remove();
}

/************************************************************************
 * Clear Paper button
 ************************************************************************/

$("#clearpaper").click(function() {
    graph.clear();
});

/************************************************************************
 * Node Info Tab
 ************************************************************************/

function nodeInfoTab()
{
	//var elements=[];

	//console.log("Inside nodeInfoTab");
	var template=`<table class="table-bordered table ">
		<thead>
		<tr>
		<td>NodeId</td>
		<td>Gne Flag</td>
		<td>Degree</td>
		</tr>
		</thead>
		<tbody>`;
	_.each(graph.getElements(), function(el) {	
		//console.log("Element"+el);
		if(el instanceof joint.dia.Link)

		{
			//Do nothing
		}
		else
		{
			template+='<tr>';
			template+='<td>'+el.cid+'</td>';
			template+='<td>'+el.attributes.nodeProperties.gneFlag+'</td>';
			//console.log(el.attributes.nodeProperties.gneFlag);
			template+='<td>'+el.attributes.nodeProperties.degree+'</td>';
			//console.log(el.attributes.nodeProperties.degree);
			template+='</tr>';  
		}

	}); 

	template+='</tbody></table>';
	//console.log(template);
	$('#view2').empty().append(template);
}



/************************************************************************
 * Link Info Tab
 ************************************************************************/

function linkInfoTab()
{
	//var elements=[];

	console.log("Inside LinkInfoTab");
	var template=` <table class="table-bordered table ">
		<thead>
		<tr>
		<td>LinkId</td>
		<td>LinkNode1</td>
		<td>LinkNode2</td>
		</tr>
		</thead>
		<tbody>`;
	_.each(graph.getLinks(), function(el) {	
		    console.log("Element"+el);
			template+='<tr>';
			template+='<td>'+el.cid+'</td>';
			template+='<td>'+getElementCid(el.attributes.source.id)+'</td>';
			console.log("Source"+el.attributes.source.id);
			template+='<td>'+getElementCid(el.attributes.target.id)+'</td>';
			console.log("Target"+el.attributes.target.id);
			template+='</tr>';  
	}); 

	template+='</tbody></table>';
	//console.log(template);
	$('#view3').empty().append(template);
}

/************************************************************************
 * Events to Handle and Placing on Canvas : For Network Elements
 ************************************************************************/

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
    }

);