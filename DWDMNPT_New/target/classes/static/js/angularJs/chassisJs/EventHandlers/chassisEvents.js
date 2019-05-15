var isDraggable = 0;
var isLinkActive = 0;
var globalFlagForCabling=0;
var activeCard;

var dragStartPostion;
var highlighterOptions= {
	        name: 'stroke',
	        options: {
	            rx: 2,  
	            ry: 2,
	            attrs: {
	                'stroke-width': 3,
	                stroke: '#404040'
	            }
	        }
	    };
paperChassis.on({
	'cell:pointerdown': onElementClick,
	'cell:mouseover': onCellMouseOver,
	'cell:mouseout': onCellMouseOut,
});


/*Paper Events*/
function onElementClick(view,evt) {
	$(".chassis-cell-tooltip").remove();
	$(".joint-port-label").remove();
	if(view.model.get('attrs').nodeProperties.identifier == "Card")
		showToolPie(view);
	activeCard=view;
}

function onCellMouseOver(view,evt) {
	let template, div, isRelevant=0;
	let targetPort;
	if(view.model.isLink()){
		isLinkActive=1;
		activeLink=view;
		let activeSource=activeLink.model.getSourceElement();
		let activeTarget=activeLink.model.getTargetElement();
		let sourcePort=activeLink.model.get('attrs').nodeProperties.source.port;
		if(activeLink.model.get('attrs').nodeProperties.identifier!="External")
			targetPort=activeLink.model.get('attrs').nodeProperties.target.port;

		let image=activeSource.portProp(sourcePort,"attrs/image/xlink:href");
		image=image.replace("jpg","gif");
		activeSource.portProp(sourcePort,"attrs/image/xlink:href",image);
		activeLink.model.getSourceElement().getParentCell().findView(paperChassis).highlight(null,{highlighter:highlighterOptions});

		if(activeLink.model.get('attrs').nodeProperties.identifier!="External")
		{
			image=activeTarget.portProp(targetPort,"attrs/image/xlink:href");
			image=image.replace("jpg","gif");
			activeTarget.portProp(targetPort,"attrs/image/xlink:href",image);
			activeLink.model.getTargetElement().getParentCell().findView(paperChassis).highlight(null,{highlighter:highlighterOptions});
		}

		isRelevant=1;
		
		if(activeLink.model.get('attrs').nodeProperties.identifier!="External" &&
		   activeLink.model.get('attrs').nodeProperties.identifier!="MuxDemuxLink")
		{
			let sourceSlotId=activeLink.model.getSourceElement().getParentCell().findView(paperChassis).model.get('attrs').nodeProperties.slotId;
			let targetSlotId=activeLink.model.getTargetElement().getParentCell().findView(paperChassis).model.get('attrs').nodeProperties.slotId;
			if(sourceSlotId<targetSlotId){
				template = `<p class="chassis-cell-tooltip__para">${activeSource.portProp(sourcePort,"attrs/nodeProperties/text")}
					-${activeTarget.portProp(targetPort,"attrs/nodeProperties/text")}</p>`;
			}
			else{
				template = `<p class="chassis-cell-tooltip__para">${activeTarget.portProp(targetPort,"attrs/nodeProperties/text")}
					-${activeSource.portProp(sourcePort,"attrs/nodeProperties/text")}</p>`;
			}
		}
		else if(activeLink.model.get('attrs').nodeProperties.identifier=="MuxDemuxLink"){
			let direction=activeLink.model.getSourceElement().getParentCell().findView(paperChassis).model.get('attrs').nodeProperties.direction.toUpperCase();
			if(activeLink.model.get('attrs').nodeProperties.source.port=="portDown")
				template = `<p class="chassis-cell-tooltip__para">${activeSource.portProp(sourcePort,"attrs/nodeProperties/text")}
					(-- ${activeTarget.portProp(targetPort,"id")} --) ${direction} DEMUX TRAY</p>`;
			else if(activeLink.model.get('attrs').nodeProperties.source.port=="portUp")
				template = `<p class="chassis-cell-tooltip__para">${activeSource.portProp(sourcePort,"attrs/nodeProperties/text")}
					(-- ${activeTarget.portProp(targetPort,"id")}-- ) ${direction} MUX TRAY</p>`;
		}
		else{
			template = `<p class="chassis-cell-tooltip__para">${activeLink.model.get('attrs').nodeProperties.target}
						(${activeSource.portProp(sourcePort,"attrs/nodeProperties/text")})</p>`;
		}
	}
	else
	{
		if( view.model.get('attrs').nodeProperties.identifier == "Card" ||
		    view.model.get('attrs').nodeProperties.identifier == "Field")
		{
			isRelevant=1;
			if(view.model.get('attrs').nodeProperties.direction=="" ||
			   view.model.get('attrs').nodeProperties.direction=="Both")
				template = `<p class="chassis-cell-tooltip__para">${view.model.get('attrs').nodeProperties.typeName}</p>`;
			else
				template = `<p class="chassis-cell-tooltip__para">${view.model.get('attrs').nodeProperties.typeName}(${view.model.get('attrs').nodeProperties.direction.toUpperCase()})</p>`;
		}
		else if (view.model.get('attrs').nodeProperties.identifier == "portGroup"){ /*Green up-down Dual Ports*/
			isRelevant=1;
			template = `<p class="chassis-cell-tooltip__para">${view.model.getPort('portUp').attrs.nodeProperties.text}</p>
						<p class="chassis-cell-tooltip__para">${view.model.getPort('portDown').attrs.nodeProperties.text}</p>`;
		}
		else if (view.model.get('attrs').nodeProperties.identifier == "portGroupModel"){ /*All other Grouped Ports*/
			isRelevant=1;
			template = `<p class="chassis-cell-tooltip__para">${view.model.getPorts()[0].attrs.nodeProperties.text}</p>`;
		}
		else if (view.model.get('attrs').nodeProperties.identifier == "osc-sfp"){ /*OSC SFP Ports*/
			isRelevant=1;
			template = `<p class="chassis-cell-tooltip__para">${view.model.getPorts()[0].attrs.nodeProperties.text}</p>
						<p class="chassis-cell-tooltip__para">${view.model.getPorts()[1].attrs.nodeProperties.text}</p>`;
		}
		else if (view.model.get('attrs').nodeProperties.identifier == "sfp"){ /*MPN/TPN SFP Ports*/
			isRelevant=1;
			template = `<p class="chassis-cell-tooltip__para">${view.model.get('attrs').nodeProperties.text}</p>`;
		}
		else if (view.model.get('attrs').nodeProperties.identifier == "Port"){ /*MPN/TPN SFP Ports*/
			isRelevant=1;
			template = `<p class="chassis-cell-tooltip__para">${view.model.get('attrs').nodeProperties.text}</p>`;
		}
	}
	if(isRelevant==1){
		div = $('<div id="propertiesPopUp" class="chassis-cell-tooltip">').css({
			"left": evt.clientX + 10,
			"top": evt.clientY + 10
		});
		div.append(template);
		$(document.body).append(div);
	}
}


function onCellMouseOut(view) {
	$(".chassis-cell-tooltip").remove();
	if(isLinkActive==1){
		let activeSource=activeLink.model.getSourceElement();
		let activeTarget=activeLink.model.getTargetElement();
		let sourcePort=activeLink.model.get('attrs').nodeProperties.source.port;
		
		let image=activeSource.portProp(sourcePort,"attrs/image/xlink:href");
		image=image.replace("gif","jpg");
		activeSource.portProp(sourcePort,"attrs/image/xlink:href",image);
		
		if(activeLink.model.get('attrs').nodeProperties.identifier!="External")
		{
			let targetPort=activeLink.model.get('attrs').nodeProperties.target.port;
			image=activeTarget.portProp(targetPort,"attrs/image/xlink:href");
			image=image.replace("gif","jpg");
			activeTarget.portProp(targetPort,"attrs/image/xlink:href",image);
		}
		
		activeLink.model.getSourceElement().getParentCell().findView(paperChassis).unhighlight(null,{highlighter:highlighterOptions})
		if(activeLink.model.get('attrs').nodeProperties.identifier!="External")
			activeLink.model.getTargetElement().getParentCell().findView(paperChassis).unhighlight(null,{highlighter:highlighterOptions});
		isLinkActive=0;
	}
}
/*Paper Events Ends*/




/*Drag and Scale*/
paperChassis.on('blank:pointerdown', function (event, x, y) {
	let scale = V(paperChassis.viewport).scale();
	dragStartPosition = { x: x * scale.sx, y: y * scale.sy };
	isDraggable = 1;
});

paperChassis.on('cell:pointerup blank:pointerup', function() {
	dragStartPosition=null;
});

$("#chassisViewCanvasDiv").mousemove(function (event) {
	if (isDraggable == 1 && (dragStartPosition!=null))
		paperChassis.setOrigin(event.offsetX - dragStartPosition.x,	event.offsetY - dragStartPosition.y);
}); 
$("#selectRack").on('change',function(){
	let selectedRack = $("#selectRack option:selected").text();
	removeLinksForAllCards();
	drawChassis(selectedRack-1);
})


$('#chassisViewCanvasDiv').on('wheel', function(e) {
	e.preventDefault();
	var numOfElements=graphChassis.getElements().length;
	if (e.originalEvent.deltaY > 0) {
		if(numOfElements!=0) //UP
			zoomingOut();
	} 
	else {
		if(numOfElements!=0) //Down
			zoomingIn();
	}
});

function zoomingIn() {
	let ScaleVar;
	let scaleFactor=0.2
	if ($("#chassisViewCanvasDiv").is(':visible'))
	{
		ScaleVar = V(paperChassis.viewport).scale();
		if(ScaleVar.sx<5 && ScaleVar.sy<5)
		{
			ScaleVar.sx=ScaleVar.sx+scaleFactor;
			ScaleVar.sy=ScaleVar.sy+scaleFactor;
			paperChassis.scale(ScaleVar.sx,ScaleVar.sy);
		}
	}
//	console.log(" zoomIn: ScaleVar.sx "+ScaleVar.sx+" ScaleVar.sy"+ScaleVar.sy);
}

function zoomingOut() {
	var ScaleVar;
	let scaleFactor=0.2
	if ($("#chassisViewCanvasDiv").is(':visible'))
	{
		ScaleVar = V(paperChassis.viewport).scale();
		if(ScaleVar.sx>1 && ScaleVar.sy>1)

		{
			ScaleVar.sx=ScaleVar.sx-scaleFactor;
			ScaleVar.sy=ScaleVar.sy-scaleFactor;
			paperChassis.scale(ScaleVar.sx,ScaleVar.sy);
		}
	}
//	console.log(" zoomOut: ScaleVar.sx "+ScaleVar.sx+" ScaleVar.sy"+ScaleVar.sy);
}
	
function resetZoom() {
	let scaleFactor=0.2
	if ($("#chassisViewCanvasDiv").is(':visible'))
	{
		let ScaleVar = V(paperChassis.viewport).scale();
		ScaleVar.sx=1.5
		ScaleVar.sy=1.5
		paperChassis.scale(ScaleVar.sx,ScaleVar.sy);
	}
//	console.log(" Reset: ScaleVar.sx "+ScaleVar.sx+" ScaleVar.sy"+ScaleVar.sy);
}

/************************************************************************
 *  zoomIn and zoomOut triggers 
 ************************************************************************/
/*$('#zoomInBtn').on('click', function () {
	// zoomInVar+=.2;
	zoomingIn();
});
$('#zoomOutBtn').on('click', function () {
	// zoomInVar-=.2;
	zoomingOut();
});

$('#resetZoom').on('click', function () {
	// zoomInVar-=.2;
	resetZoom();
});*/
/*Drag and Scale Ends*/


/*Tool Pie (Halo) Customization and Event Handling*/
new joint.ui.Tooltip({
	rootTarget: document.body,
	target: '[data-tooltip]',
	direction: 'auto',
	padding: 10
});

function addCablingHandle(toolPie,view){
	toolPie.removeHandle('rem-cabling');
	toolPie.addHandle({
		name: 'cabling',
		icon: 'images/chassis/cabling.png',
		attrs: {
			'.handle': {
				'data-tooltip-class-name': 'small',
				'data-tooltip': 'View Cabling',
				'data-tooltip-position': 'right',
				'data-tooltip-padding': 15
			}
		}
	});
	toolPie.toggleState();
	toolPie.on('action:cabling:pointerdown', function(evt) {
		evt.stopPropagation();
		if(0==globalFlagForCabling){
			showConfirmDialog(view);
		}
		else if(1==globalFlagForCabling){
    		removeLinksForAllCards();
		}
		drawLinks(view);
		toolPie.remove();
	});
}

function removeCablingHandle(toolPie,view){
	toolPie.removeHandle('cabling');
	toolPie.addHandle({
        name: 'rem-cabling',
        icon: 'images/chassis/rem-cabling.png',
        attrs: {
            '.handle': {
                'data-tooltip-class-name': 'small',
                'data-tooltip': 'Remove Cabling',
                'data-tooltip-position': 'right',
                'data-tooltip-padding': 15
            }
        }
    });
	toolPie.toggleState();
	toolPie.on('action:rem-cabling:pointerdown', function(evt) {
        evt.stopPropagation();
        removeLinks(view);
		toolPie.remove();
    });
}

function showToolPie(view) {
	var model = view.model;
	toolPie = new joint.ui.Halo({
		type: 'pie',
		pieSliceAngle: '180',
		cellView: view,
		tinyThreshold: 0,
	});
	/*Remove Default Handles*/
	toolPie.removeHandles();
	/*Add Info Handle*/
	toolPie.addHandle({
        name: 'Info',
        icon: 'images/chassis/info.png',
        attrs: {
            '.handle': {
                'data-tooltip-class-name': 'small',
                'data-tooltip': 'View Card Info',
                'data-tooltip-position': 'right',
                'data-tooltip-padding': 15
            }
        }
    });
	/*Info Handle Event Handling*/
	toolPie.on('action:Info:pointerdown', function(evt) {
        evt.stopPropagation(); 
        toolPie.toggleState();
        let data =`<p>Card Name: ${view.model.get('attrs').nodeProperties.typeName}</p>`;
        data+=`<p>Rack: ${view.model.get('attrs').nodeProperties.rackId}</p>`;
        data+=`<p>Subrack: ${view.model.get('attrs').nodeProperties.subRackId}</p>`;
        data+=`<p>Slot Id: ${view.model.get('attrs').nodeProperties.slotId}</p>` ;
        if(view.model.get('attrs').nodeProperties.direction != ""){
        	data+=`<p>Dirction: ${view.model.get('attrs').nodeProperties.direction.toUpperCase()}</p>` ;
        }
		if(view.model.get('attrs').nodeProperties.lambda != "")
        	data+=`<p>Wavelength: ${view.model.get('attrs').nodeProperties.lambda}</p>` ;

        bootbox.alert({ 
        	  size: "big",
        	  title: `${view.model.get('attrs').nodeProperties.typeName}`,
        	  message: data, 
        	  callback: function(){ /* your callback code */ }
        	})
	});
	toolPie.on('action:Info:pointerup', function(evt) {
        evt.stopPropagation();
        toolPie.remove();
	});
	/*Add Cabling Handles*/
	if(typeof(links[view.model.get('attrs').nodeProperties.typeName])!=="undefined" &&
	   (typeof(links[view.model.get('attrs').nodeProperties.typeName]["east"])!=="undefined" ||
	    typeof(links[view.model.get('attrs').nodeProperties.typeName]["west"])!=="undefined" ||
	    typeof(links[view.model.get('attrs').nodeProperties.typeName]["both"])!=="undefined" ))
	{
		let Node=$("#nodeIdInputChassisViewForm select option:selected").text();
		let Rack=$("#rackIdInputChassisViewForm select option:selected").text();
		let Subrack=view.model.get('attrs').nodeProperties.subRackId;
		let SlotId=view.model.get('attrs').nodeProperties.slotId;
		
	    let ref_key=Node+"_"+Rack+"_"+Subrack+"_"+SlotId;
	    
		switch(view.model.get('attrs').nodeProperties.typeName){
		case "OCM1x8":
		{
			if( links[view.model.get('attrs').nodeProperties.typeName]["east"]["cablingActiveFor"].includes(ref_key)  &&
				links[view.model.get('attrs').nodeProperties.typeName]["west"]["cablingActiveFor"].includes(ref_key) )
			{
				removeCablingHandle(toolPie,view);
			}
			else
			{
				addCablingHandle(toolPie,view);
			}
		}
		break;
		case "CGX":
		case "CGM":
		{
			let cardType=view.model.get('attrs').nodeProperties.typeName;
			let direction=view.model.get('attrs').nodeProperties.direction;
			let oddEven=(view.model.get('attrs').nodeProperties.lambda%2)?"Odd":"Even";
			if( links[cardType][direction][oddEven]["cablingActiveFor"].includes(ref_key) )
			{
				removeCablingHandle(toolPie,view);
			}
			else
			{
				addCablingHandle(toolPie,view);
			}
		}
		break;
		default:
		{
			if(links[view.model.get('attrs').nodeProperties.typeName]
					[view.model.get('attrs').nodeProperties.direction]["cablingActiveFor"].includes(ref_key))
			{
				removeCablingHandle(toolPie,view);
			}
			else
			{
				addCablingHandle(toolPie,view);
			}
		}
		}
	}
	toolPie.render();
	$(".joint-halo.pie .pie-toggle.e").css({"top":"calc(20% - 15px)"});
	$(".handles").css({"top":"calc(20% - 50px)"});
	$(".pie-toggle").css({"background-image":"url('images/chassis/settings.png')","background-color":'#A0A0A0'});
	$(".handles .handle .slice").css({"fill":'#BEBEBE'});
	$(".handles").css({"background-color":'#BEBEBE'});
}

function showConfirmDialog(view){
	bootbox.prompt({
	    title: "Do you want to Remove the cabling from other cards. This ensures more clarity",
	    size:"big",
	    inputType: 'checkbox',
	    inputOptions: [
	        {
	            text: 'Remember My Preference',
	            value: '1',
	        },
	    ],
	    buttons: {
	    	cancel: {
	    		label: '<i class="fa fa-times"></i> Cancel'
	    	},
	    	confirm: {
	    		label: '<i class="fa fa-check"></i> Confirm',
	    	}
	    },
	    callback: function (result) {	
	    	if(result!=null)
	    	{
	    		if(1==result){
	    			globalFlagForCabling=1;
	    			removeLinksForAllCards();
	    			drawLinks(view);
	    		}
	    	}
	    }
	});
}

/*Tool Pie (Halo) Customization and Event Handling Ends*/

/*
linkArr[i].link.vertices([
	//Left source => x+(something)
	new g.Point(sourcePorts[srcPortGroupNumber].position().x+35, sourcePorts[srcPortGroupNumber].position().y),
	]);*/

function drawLinks(view){
	let cardName=view.model.get('attrs').nodeProperties.typeName;
	let direction=view.model.get('attrs').nodeProperties.direction;
	let sourcePorts, linkDescObj, linkArr;
	let srcCardSlot, srcPortGroupNumber, srcPortPos;
	let tgtCardSlot, tgtPortGroupNumber, tgtPortPos;
	
	let Node=$("#nodeIdInputChassisViewForm select option:selected").text();
	let Rack=$("#rackIdInputChassisViewForm select option:selected").text();
	let Subrack=view.model.get('attrs').nodeProperties.subRackId;
	let SlotId=view.model.get('attrs').nodeProperties.slotId;
	let oddEven;
	if(cardName.includes("CGX") || cardName.includes("CGM"))
		oddEven=(view.model.get('attrs').nodeProperties.lambda%2)?"Odd":"Even";
	
    let ref_key=Node+"_"+Rack+"_"+Subrack+"_"+SlotId;
	if( (direction == "Both" &&
			links[cardName]["east"]["cablingActiveFor"].includes(ref_key)==false &&
			links[cardName]["west"]["cablingActiveFor"].includes(ref_key)==false		)	||/*For Ocm*/ 
			(typeof(links[cardName][direction]["cablingActiveFor"])!="undefined" &&
			 links[cardName][direction]["cablingActiveFor"].includes(ref_key)==false	)	|| /*For all other cards without lambda*/ 
			(links[cardName][direction][oddEven]["cablingActiveFor"].includes(ref_key)==false ) /*For Cards like CGM and CGX*/ 
	  )
	{
		switch(cardName)
		{
			case "OCM1x8":
			{
				links[cardName]["east"]["cablingActiveFor"].push(ref_key);
				links[cardName]["west"]["cablingActiveFor"].push(ref_key);
				linkDescObj=links[cardName]["east"];
				useLinkDescObjToAdd(linkDescObj,view,"east");
				linkDescObj=links[cardName]["west"];
				useLinkDescObjToAdd(linkDescObj,view,"west");
			}
			break;
			case "CGX":
			case "CGM":
			{
				let oddEven=(view.model.get('attrs').nodeProperties.lambda%2)?"Odd":"Even";
				linkDescObj=links[cardName][direction][oddEven];
				links[cardName][direction][oddEven]["cablingActiveFor"].push(ref_key);
				useLinkDescObjToAdd(linkDescObj,view,direction);
			}
			break;
			default:
			{
				linkDescObj=links[cardName][direction];
				links[cardName][direction]["cablingActiveFor"].push(ref_key);
				useLinkDescObjToAdd(linkDescObj,view,direction);
			}
		}
		removeCablingHandle(toolPie,view)
	}
}

function useLinkDescObjToAdd(linkDescObj,view,targetDirection){
	let sourcePorts=view.model.get('attrs').nodeProperties.ports;
	for(targetCard in linkDescObj){
		if(targetCard.indexOf("cabling")==-1){
			linkArr=linkDescObj[targetCard];
			for(i in linkArr){
				linkArr[i].link=linkModel.clone();
				if( view.model.get('attrs').nodeProperties.typeName.includes("CGM")||
					view.model.get('attrs').nodeProperties.typeName.includes("CGX") )
				{
					linkArr[i].link.router('orthogonal');
				}
				else
				{
					linkArr[i].link.router('normal');
				}
				srcCardSlot=view.model.get('attrs').nodeProperties.slotId;
				srcPortGroupNumber=linkArr[i].source.portGroupId;
				srcPortPos=linkArr[i].source.port;
				if(targetCard=="OCM1x8")
					tgtCardSlot=findCardSlot(targetCard,"Both");
				else
					tgtCardSlot=findCardSlot(targetCard,targetDirection);
				if(tgtCardSlot!=-1 || targetCard.includes("Field")){
					if(targetCard=="OCM1x8"){
						targetPorts=findCardPorts(targetCard,"Both");
						tgtPortGroupNumber=linkArr[i].target.portGroupId;
						tgtPortPos=linkArr[i].target.port;
					}
					else if (targetCard.includes("Field")){
						let field=fieldModel.clone();
						let pos_x;
						if(sourcePorts[srcPortGroupNumber].getParentCell().get('attrs').nodeProperties.slotId>7)	
							pos_x=sourcePorts[srcPortGroupNumber].getParentCell().getParentCell().position().x+ModelSubrack.attributes.size.width+100;
						else
							pos_x=sourcePorts[srcPortGroupNumber].getParentCell().getParentCell().position().x-100;
						let pos_y=sourcePorts[srcPortGroupNumber].position().y-5;
						field.position(pos_x,pos_y);
						field.attr("nodeProperties/typeName","FIELD");
						field.addTo(graphChassis);
						targetPorts=[field];
						tgtPortGroupNumber=0;
						tgtPortPos={id:""}
					}
					else if (targetCard.includes("Mux")){
						if(srcPortPos.id=="portUp"){
							let slotNum=view.model.get('attrs').nodeProperties.slotId;
							let wavelength=view.model.get('attrs').nodeProperties.lambda;
							targetPorts=findMuxPorts(targetCard,targetDirection);
							tgtPortGroupNumber=findMuxPortGroupId(targetPorts,wavelength);
							tgtPortPos={"id":""+wavelength};
							linkArr[i].link.vertices([
								new g.Point(sourcePorts[srcPortGroupNumber].getParentCell().getParentCell().position().x-150+10*slotNum,
										sourcePorts[srcPortGroupNumber].position().y),
								]);
							linkArr[i].link.attr("nodeProperties/identifier","MuxDemuxLink");

						}
						else if (srcPortPos.id=="portDown"){
							let slotNum=view.model.get('attrs').nodeProperties.slotId;
							let wavelength=view.model.get('attrs').nodeProperties.lambda;
							targetPorts=findDemuxPorts(targetCard,targetDirection);
							tgtPortGroupNumber=findMuxPortGroupId(targetPorts,wavelength);
							tgtPortPos={"id":""+wavelength}
							linkArr[i].link.vertices([
								new g.Point(sourcePorts[srcPortGroupNumber].getParentCell().getParentCell().position().x+ModelSubrack.attributes.size.width+150-10*slotNum,
										sourcePorts[srcPortGroupNumber].position().y+7),
								]);
							linkArr[i].link.attr("nodeProperties/identifier","MuxDemuxLink");

						}
					}
					else{
						targetPorts=findCardPorts(targetCard,targetDirection);
						tgtPortGroupNumber=linkArr[i].target.portGroupId;
						tgtPortPos=linkArr[i].target.port;
					}
					
//					Uncomment for Debugging
//					console.log(srcCardSlot+","+srcPortGroupNumber+","+srcPortPos.id);
//					console.log(tgtCardSlot+","+tgtPortGroupNumber+","+tgtPortPos.id);
					
					linkArr[i].link.source({id:sourcePorts[srcPortGroupNumber], port:srcPortPos.id});
					linkArr[i].link.attr("nodeProperties/source",{id:sourcePorts[srcPortGroupNumber].id, port:srcPortPos.id});
					if(targetCard.includes("Field")){
						linkArr[i].link.target({id:targetPorts[tgtPortGroupNumber]});
						linkArr[i].link.attr("nodeProperties/target",linkArr[i].target);
						linkArr[i].link.attr("nodeProperties/identifier","External");
					}
					else{
						linkArr[i].link.target({id:targetPorts[tgtPortGroupNumber], port:tgtPortPos.id});
						linkArr[i].link.attr("nodeProperties/target",{id:targetPorts[tgtPortGroupNumber].id, port:tgtPortPos.id});
					}
					linkArr[i].link.addTo(graphChassis);
				}
				$(".connection-wrap").css({"stroke-width":"4"});
			}
		}
	}	
}
function findMuxPortGroupId(portGroupArr,wavelength){
	for(let i in portGroupArr){
		if(portGroupArr[i].getPorts()[0].attrs.nodeProperties.text.includes(wavelength)){
			return i;
		}
	}	
}

function findCardSlot(cardName,cardDirection){
	let subracks=[];
	subracks=activeCard.model.getParentCell().getParentCell().getEmbeddedCells();
	for(let i in subracks){
		let cards=subracks[i].getEmbeddedCells();
		for(let i in cards){
			if(cards[i].get('attrs').nodeProperties.direction!=""){
				if(cards[i].get('attrs').nodeProperties.typeName==cardName && cards[i].get('attrs').nodeProperties.direction==cardDirection ){
					return cards[i].get('attrs').nodeProperties.slotId;
				}
			}
			else{
				if(cards[i].get('attrs').nodeProperties.typeName==cardName){
					return cards[i].get('attrs').nodeProperties.slotId;
				}			
			}
		}
	}
	return -1;
}


function findCardPorts(cardName,cardDirection){
	let subracks=[];
	subracks=activeCard.model.getParentCell().getParentCell().getEmbeddedCells();
	for(let i in subracks){
		let cards=subracks[i].getEmbeddedCells();
		for(let i in cards){
			if(cards[i].get('attrs').nodeProperties.direction!=""){
				if(cards[i].get('attrs').nodeProperties.typeName==cardName && cards[i].get('attrs').nodeProperties.direction==cardDirection ){
					return cards[i].get('attrs').nodeProperties.ports;
				}
			}
			else{
				if(cards[i].get('attrs').nodeProperties.typeName==cardName){
					return cards[i].get('attrs').nodeProperties.ports;
				}			
			}
		}
	}
}

function findMuxPorts(cardName,cardDirection){
	let subracks=[];
	subracks=activeCard.model.getParentCell().getParentCell().getEmbeddedCells();
	for(let i in subracks){
		let cards=subracks[i].getEmbeddedCells();
		for(let i in cards){
			if(cards[i].get('attrs').nodeProperties.typeName==cardName && cards[i].get('attrs').nodeProperties.direction==cardDirection ){
				return cards[i].get('attrs').nodeProperties.portsMux;
			}
		}
	}
}

function findDemuxPorts(cardName,cardDirection){
	let subracks=[];
	subracks=activeCard.model.getParentCell().getParentCell().getEmbeddedCells();
	for(let i in subracks){
		let cards=subracks[i].getEmbeddedCells();
		for(let i in cards){
			if(cards[i].get('attrs').nodeProperties.typeName==cardName && cards[i].get('attrs').nodeProperties.direction==cardDirection ){
				return cards[i].get('attrs').nodeProperties.portsDemux;
			}
		}
	}
}

function removeLinks(view){
	let linksToRemove=[];
	let linkDescObj;
	let cardName=view.model.get('attrs').nodeProperties.typeName;
	let direction=view.model.get('attrs').nodeProperties.direction;
	let Node=$("#nodeIdInputChassisViewForm select option:selected").text();
	let Rack=$("#rackIdInputChassisViewForm select option:selected").text();
	let Subrack=view.model.get('attrs').nodeProperties.subRackId;
	let SlotId=view.model.get('attrs').nodeProperties.slotId;
	let oddEven;
	if(cardName.includes("CGX") || cardName.includes("CGM"))
		oddEven=(view.model.get('attrs').nodeProperties.lambda%2)?"Odd":"Even";
	
    let ref_key=Node+"_"+Rack+"_"+Subrack+"_"+SlotId;
	if( (direction == "Both" &&
		links[cardName]["east"]["cablingActiveFor"].includes(ref_key) &&
		links[cardName]["west"]["cablingActiveFor"].includes(ref_key)		)	||/*For Ocm*/ 
		(typeof(links[cardName][direction]["cablingActiveFor"])!="undefined" &&
		  links[cardName][direction]["cablingActiveFor"].includes(ref_key)	)	|| /*For all other cards without lambda*/ 
		(links[cardName][direction][oddEven]["cablingActiveFor"].includes(ref_key)) /*For Cards like CGM and CGX*/ 
	  )
	{
		switch(cardName)
		{
			case "OCM1x8":{
				links[cardName]["east"]["cablingActiveFor"].splice(links[cardName]["east"]["cablingActiveFor"].indexOf(ref_key),1);
				links[cardName]["west"]["cablingActiveFor"].splice(links[cardName]["west"]["cablingActiveFor"].indexOf(ref_key),1);

				linkDescObj=links[cardName]["east"];
				useLinkDescObjForRemoval(linkDescObj);

				linkDescObj=links[cardName]["west"];
				useLinkDescObjForRemoval(linkDescObj);

			}
			break;
			case "CGX":
			case "CGM":
			{
				linkDescObj=links[cardName][direction][oddEven];
				links[cardName][direction][oddEven]["cablingActiveFor"].splice(links[cardName][direction][oddEven]["cablingActiveFor"].indexOf(ref_key),1);
				useLinkDescObjForRemoval(linkDescObj,view,direction);
			}
			break;
			default:
			{
				linkDescObj=links[cardName][direction];
				links[cardName][direction]["cablingActiveFor"].splice(links[cardName][direction]["cablingActiveFor"].indexOf(ref_key),1);
				useLinkDescObjForRemoval(linkDescObj);
			}
		}
	}
	addCablingHandle(toolPie,view)
}

function useLinkDescObjForRemoval(linkDescObj)
{
	for(card in linkDescObj){
		if(card.indexOf("cabling")==-1){
			linksToRemove=linkDescObj[card];
			for(i in linksToRemove){
				if(linksToRemove[i].link.getTargetElement() instanceof joint.shapes.cdot.dwdm.npt.Field)
					linksToRemove[i].link.getTargetElement().remove();
				linksToRemove[i].link.remove();
			}
		}
	}
}


