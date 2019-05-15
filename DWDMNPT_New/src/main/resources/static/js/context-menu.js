/************************************************************************
 * Contextmenu 
 ************************************************************************/

var cellLocal=-1; /**Local Variable to Update every time with the current cellview which can be used in other event*/
var Direction = ["N","E","W","S","NE","NW","SE","SW"]; /**8 Different Direction for port */
var DirectionFlag = [0,0,0,0,0,0,0,0]; /**Flag to check if port is set or not*/
/*
 * contextMenu.js Api  
 */
/*var menu = [{
    name: 'create',
    img: 'images/port.png',
    title: 'create button',
    fun: function () {
        alert('i am add button')
    }
}, {
    name: 'update',
    img: 'images/update.png',
    title: 'update button',
    subMenu: [{
        name: 'merge',
        title: 'It will merge row',
       // img:'images/merge.png',
        fun: function () {
            alert('It will merge row')
        }
    }, {
        name: 'replace',
        title: 'It will replace row',
        img:'images/replace.png',
        subMenu: [{
            name: 'replace top 100',
          //  img:'images/top.png',
            fun:function(){
            alert('It will replace top 100 rows');
            }

        }, {
            name: 'replace all',
           // img:'images/all.png',
            fun:function(){
            alert('It will replace all rows');
            }
        }]
    }]
}, {
    name: 'delete',
  //  img: 'images/delete.png',
    title: 'delete button',
    subMenu: [{
        'name': 'soft delete',
        img:'images/soft_delete.png',
        fun:function(){
        alert('You can recover back');
        }
    }, {
        'name': 'hard delete',
      //  img:'images/hard_delete.png',
        fun:function(){
        alert('It will delete permanently');
        }
    }]

}];
 */

/*
 * Context menu containing : 1) Add Port    -> 8 Direction
 * 							  2) Remove Port -> 8 Direction
 * 							  3) Delete Node	
 */
var contextmenu = [
                   {
                	    name: 'Add Port',
                	    img: 'images/General/Ethernet On-104.png',
                	    title: 'Add Port',
                	    subMenu: [{
                	        name: 'North',
                	        title: 'North Direction',
                	        img:'images/Direction/Add/North Filled-100.png',
                	        fun: function () {
                	            ///alert('It will merge row')
                	            addPortToDirection("north");
                	        },                	       
                	    
                	    },
                	    {
                	        name: 'East',
                	        title: 'East Direction',
                	        img:'images/Direction/Add/East Filled-100.png',
                	        fun: function () {
                	            ///alert('It will merge row')
                	        	addPortToDirection("east");
                	        }
                	    },
                	    
                	    {
                	        name: 'West',
                	        title: 'West Direction',
                	        img:'images/Direction/Add/West Filled-100.png',
                	        fun: function () {
                	            ///alert('It will merge row')
                	        	addPortToDirection("west");
                	        }
                	    },
                	    
                	    {
                	        name: 'South',
                	        title: 'South Direction',
                	        img:'images/Direction/Add/South Filled-100.png',
                	        fun: function () {
                	            ///alert('It will merge row')
                	        	addPortToDirection("south");
                	        }
                	    },
                	    
                	    {
                	        name: 'North-East',
                	        title: 'North-East Direction',
                	        img:'images/Direction/Add/North East Filled-100.png',
                	        fun: function () {
                	            ///alert('It will merge row')
                	        	addPortToDirection("ne");
                	        }
                	    },
                	    
                	    {
                	        name: 'North-West',
                	        title: 'North-West Direction',
                	        img:'images/Direction/Add/North West Filled-100.png',
                	        fun: function () {
                	            ///alert('It will merge row')
                	        	addPortToDirection("nw");
                	        }
                	    },
                	    
                	    {
                	        name: 'South-East',
                	        title: 'South-East Direction',
                	        img:'images/Direction/Add/South East Filled-100.png',
                	        fun: function () {
                	            ///alert('It will merge row')
                	        	addPortToDirection("se");
                	        }
                	    },
                	    
                	    {
                	        name: 'South-West',
                	        title: 'South-West Direction',
                	        img:'images/Direction/Add/South West Filled-100.png',
                	        fun: function () {
                	            ///alert('It will merge row')
                	        	addPortToDirection("sw");
                	        }
                	    },
                	    ]
                	}, 	
                	
              {

                	    name: 'Remove Port',
                	    img: 'images/General/Ethernet Off-104.png',
                	    title: 'Remove Port',
                	    subMenu: [{
                	        name: 'North',
                	        title: 'North Direction',
                	        img:'images/Direction/Remove/North Filled-100.png',
                	        fun: function () {                	            
                	            removePortToDirection("north");
                	        }
                	    },
                	    {
                	        name: 'East',
                	        title: 'South-West Direction',
                	        img:'images/Direction/Remove/East Filled-100.png',
                	        fun: function () {                	            
                	            removePortToDirection("east");
                	        }
                	    },
                	    
                	    {
                	        name: 'West',
                	        title: 'West Direction',
                	        img:'images/Direction/Remove/West Filled-100.png',
                	        fun: function () {
                	        	removePortToDirection("west");
                	        }
                	    },
                	    
                	    {
                	        name: 'South',
                	        title: 'South-West Direction',
                	        img:'images/Direction/Remove/South Filled-100.png',
                	        fun: function () {
                	        	removePortToDirection("south");
                	        }
                	    },
                	    
                	    {
                	        name: 'North-East',
                	        title: 'North-East Direction',
                	        img:'images/Direction/Remove/North East Filled-100.png',
                	        fun: function () {
                	        	removePortToDirection("ne");
                	        }
                	    },
                	    
                	    {
                	        name: 'North-West',
                	        title: 'North-West Direction',
                	        img:'images/Direction/Remove/North West Filled-100.png',
                	        fun: function () {
                	        	removePortToDirection("nw");
                	        }
                	    },
                	    
                	    {
                	        name: 'South-East',
                	        title: 'South-East Direction',
                	        img:'images/Direction/Remove/South East Filled-100.png',
                	        fun: function () {
                	        	removePortToDirection("se");
                	        }
                	    },
                	    
                	    {
                	        name: 'South-West',
                	        title: 'South-West Direction',
                	        img:'images/Direction/Remove/South West Filled-100.png',
                	        fun: function () {
                	        	removePortToDirection("sw");
                	        }
                	    },
                	    ]
                			
              },	
                	
             {
                name: 'Delete Node',
                img: 'images/General/Delete-96.png',
                title: 'Delete Element',
                fun: function () {
                	DeleteNode();
                }
             },
                   
 ];
	
	
/**
 * 
 */
function DeleteNode(){
	cellLocal.model.remove();
}
	
/*
//Calling context menu*/
///$('#clearpaper').contextMenu(contextmenu);


/*
 * Blank paper Click Event
 */
paper.on('blank:contextmenu', function(cellView, event, x, y) {
    ///alert('Inside Blank Paper')
});



/*
 * Context Menu Event
 */
paper.on('cell:contextmenu', function(cellView, event, x, y) {
	
	    // Avoid the real one
	    //event.preventDefault();

	 /* // Show contextmenu
	    $(".custom-menu").finish().toggle(100).
	
	    // In the right position (the mouse)
	    css({
	        top: event.pageY + "px",
	        left: event.pageX + "px"
	    });
	 */

    
	    //Calling context menu
	  
		if(cellLocal!= -1 && cellLocal.model.portData.groups.north.flag == 1){
			///alert('north 1')
			cellView.model.portData.groups.north.flag = 1;
		}
		else if(cellLocal!= -1 && cellLocal.model.portData.groups.east.flag == 1){
			///alert('north 1')
			cellView.model.portData.groups.east.flag = 1;
		}
		
	    cellLocal = cellView;	    
	    console.log(cellLocal)
	    
	    //alert('celll context')  
	    //alert(cellView.id.id);
	    
	    var x = cellView.el.innerHTML;
	    var y = x.split("image id=")[1];
	    var z = y.split('"')[1];
	    /*var menuTrgr=$('#menuTrigger');
	     
	    menuTrgr.contextMenu('menu','#demoMenu',{
	        displayAround:'trigger',
	        horAdjust:-menuTrgr.width()
	    });*/
	    
	    //var x = cellView.el.innerHTML.getElementById('image');
	    
	    var hash = "#"; 
	    var final = hash.concat(z);
	    ///alert(final)
	    
	    $(final).contextMenu(contextmenu,{mouseClick:"right",triggerOn:"click"});
	    //$("#j_20").contextMenu(contextmenu);

});

	    

/*
 * If the document is clicked somewhere
 */
$(document).bind("mousedown", function(e) {	
    // If the clicked element is not the menu
    if (!$(e.target).parents(".custom-menu").length > 0) {

        // Hide it
        $(".custom-menu").hide(100);
    }
});



/**
 * 
 */
function removePortToDirection(param){	
		
		///alert('hasPort '+cellLocal.model.hasPort("northId"));
		
		if(cellLocal.model.hasPort(param) == 1){				
				
				cellLocal.model.removePort(param);	
				
				
		}
		else if(cellLocal.model.hasPort(param) == 0){ 
			
			alert(param+' Doesnt Exist !!');
		}	
		
	
}

/**
 * 
 * @param param
 * @returns
 */
function addPortToDirection(param){
		var dirId = -1;
	
		if(param == "north")
			dirId = 0;
		else if(param == "east")
			dirId = 1;
		else if(param == "west")
			dirId = 2;
		else if(param == "south")
			dirId = 3;
		else if(param == "ne")
			dirId = 4;
		else if(param == "nw")
			dirId = 5;
		else if(param == "se")
			dirId = 6;
		else if(param == "sw")
			dirId = 7;
		
		///alert('hasPort '+cellLocal.model.hasPort("northId"));
		
		if(cellLocal.model.hasPort(param) == 0){				
				
				cellLocal.model.addPort({
					z:'auto',
					id:param,
					group: param,		
		            attrs: { text: {text: Direction[dirId]}},
		            label: {
	                    position: {
	                        name: 'outside',	                      
	                    }
	                }
		        });	
				
				
		}
		else if(cellLocal.model.hasPort(param) == 1){ 
			
			alert(param+' already exist !!');
		}		
	
	
		
}

/*
 * If the context menu element is clicked
 */
$(".custom-menu li").click(function() {

    // This is the triggered action name
    switch ($(this).attr("data-action")) {

        // A case for each action. Your actions here
        case "portaddId": /// alert("Add Port called");

            ///alert(cellLocal.model.portData.ports.length);
            if (cellLocal.model.portData.ports.length > 1) {
                alert('Only 2 Direction Allowed');
            } else {
                /*			cellLocal.model.addPort({
                				
                				attrs : {
                				
                					circle : {
                						magnet : true,						
                						stroke : '#3c763d',
                						'stroke-width' : 2,
                						fill : '#ffffff'
                					},
                					text : {
                						text : Direction[cellLocal.model.portData.ports.length],
                						stroke : '#3c763d',
                						'stroke-width' : 1
                					}
                				},
                				markup : '<circle r="7" />'

                			});			
                			
                		}*/
                cellLocal.model.addPort({
                    group: 'blacks',

                    attrs: {
                        text: {
                            text: Direction[cellLocal.model.portData.ports.length],
                            stroke: '#3c763d',
                            'stroke-width': 1
                        }
                    },
                });
            }
            ///cellLocal.model.addPort({ markup: '<rect width="15" height="15" position:"top",stroke : "#3c763d", fill="#3c763d"/>', label: { markup: '<text fill="#000000"/>' }});

            break;

        case "portremoveId": /// alert("Port Remove called"); 
            cellLocal.model.removePort(cellLocal.model.getPorts()[0]);
            ///alert(cellLocal.model.portData.ports[0].position.name)
            ///cellLocal.model.portData.ports[0].position.name = 'top';		
            break;

        case "deleteId": /// alert("Delete called");
            cellLocal.remove();
            break;
    }

    // Hide it AFTER the action was triggered
    $(".custom-menu").hide(100);
});