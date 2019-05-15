/**Global Variables**/

/*General*/
var gridsize = 1;
var currentScale = 1;

/*For Six Slot*/
var sixSlotChassis = false;
var nodeTypeIla =2 ;
var sixSlotSubrackInnerHeight=165;
var sixSlotSubrackSlotWidth=220

/*For Graph*/
graphChassis = new joint.dia.Graph;
paperChassis = new joint.dia.Paper({
	el: document.getElementById("rack"),
	height: 2000,
	width:'100%',
    model: graphChassis,
	perpendicularLinks: true,
	interactive: false
});
/**Global Variables End**/

/**Subrack Model**/
/*subRack-Model*/
var ModelSubrack = new joint.shapes.basic.Generic({
	markup: '<rect stroke="transparent" stroke-width="30" rx="3" ry="3"/><image/>',
	size: { height: 245, width: 300 },
	attrs: {
		rect: {
			'ref-width': '100%',
			'ref-height': '100%',
		},
		image: {
			"xlink:href": "",
			'ref-width': '100%',
			'ref-height': '100%',
			ref: 'rect',
			preserveAspectRatio: 'none'
		},
		nodeProperties: {
			identifier:'Subrack',
			type: 'rack',
			typeName: 'rack',
		}

	}
});

/**Baytop Model**/
var ModelPdu = new joint.shapes.basic.Generic({
	markup: '<rect stroke="transparent" stroke-width="30" rx="3" ry="3"/><image/>',
	size: { height: 40, width: 150 },
	attrs: {
		rect: {
			'ref-width': '100%',
			'ref-height': '100%',
		},
		image: {
			"xlink:href": "images/chassis/pdu.jpg",
			'ref-width': '100%',
			'ref-height': '100%',
			ref: 'rect',
			preserveAspectRatio: 'none'
		},
		nodeProperties: {
			identifier:'Subrack',
			type: 'rack',
			typeName: 'rack',
		}

	}
});

/**Card Plate Model**/
/*Definition*/
joint.dia.Element.define('cdot.dwdm.cardPlate', {
	attrs: {
		body: {
			refWidth: '100%',
			refHeight: '100%',
			strokeWidth: 2,
			stroke: '#000000',
			fill: '#FFFFFF'
		},
		label: {
			textVerticalAnchor: 'middle',
			textAnchor: 'middle',
			refX: '50%',
			refY: '50%',
			fontSize: 14,
			fill: '#333333'
		},
		plateImage:{
			
		},
		logoImage:{
		},
		leftHandle:{
		},
		rigthHandle:{
		}
	}
}, {
    markup: [
    {
        tagName: 'rect',
        selector: 'body',
    }, 
    {
    	tagName: 'image',
    	selector: 'plateImage'
    },
    {
    	tagName: 'image',
    	selector: 'logoImage'
    }
    ,
    {
    	tagName: 'image',
    	selector: 'leftHandle'
    }
    ,
    {
    	tagName: 'image',
    	selector: 'rightHandle'
    },
    {
        tagName: 'text',
        selector: 'label'
    }
    ]
});
/*Customization*/
var ModelPlate = new joint.shapes.cdot.dwdm.cardPlate();
ModelPlate.resize(20,155);
ModelPlate.attr({
	body: {
		stroke: '#404040',
		fill: '#a6a6a6',
		event: 'cell:pointerdown'
	},
	label: {
		text: "",
		refX: '50%',
		refY: '95%',
		background: 'blue',
		fill: 'white',
		fontSize: '50%',
		fontWeight: 'Bold'
	},
	plateImage: {
		refHeight: "100%",
		refWidth:"100%",
		preserveAspectRatio: 'none',
		"xlink:href":"images/chassis/plate.jpg" 
	},
	logoImage: { 
		refX: '60%',
		refY: '0.1%',
		refHeight: "20%",
		refWidth:"25%",
		"xlink:href":"images/chassis/cdot-logo.png" 
	},
	leftHandle: { 
		refX: '0%', 
		refY: '0.1%',
		refHeight: "20%",
		refWidth:"25%",
		preserveAspectRatio: 'none',
		"xlink:href":"images/chassis/handle_left.jpg" 
	},
	rightHandle: { 
		refX: '0%',
		refY: '80%',
		refHeight: "20%",
		refWidth:"25%",
		preserveAspectRatio: 'none',
		"xlink:href":"images/chassis/handle_right.jpg" 
	},
	nodeProperties:{
		identifier:"Card",
		ports: {},
		portNames: {},
		typeName: {},
		slotWidth: {},
		rackId:{},
		subRackId:{},
		slotId:{}
	}
});

/**Port Models For Grouped Green Lower and Upper Ports**/
/*Upper Port Model*/
var portUp = {
		markup: '<rect strokegit ="red" fill="gray"/><image/>',
		group: 'up',
		id:'portUp',
		args: {x:0,y:0}, 
		attrs: {
			image: {
				"xlink:href": "images/chassis/upper_port.jpg",
				ref: 'rect',
				width: "7px",
				height: "7px",
				preserveAspectRatio: 'none'
			},
			rect: {
				rx: 2,
				ry: 2,
				width: "7px",
				height: "7px"
			},
			nodeProperties: {
				text: 'portUp',
				'font-size': '10px'
			}
		},
};
/*Lower Port Model*/
var portDown = {
		markup: '<rect strokegit ="red" fill="gray"/><image/>',
		group: 'down',
		id: 'portDown',
		args: {}, 
		attrs: {
			image: {
				"xlink:href": "images/chassis/lower_port.jpg",
				ref: 'rect',
				width: "7px",
				height: "7px",
				preserveAspectRatio: 'none'
			},
			rect: {
				rx: 2,
				ry: 2,
				width: "7px",
				height: "7px"
			},
			nodeProperties: {
				text: 'portDown',
				'font-size': '10px'
			}
		},
};
/*Complete Port Group*/
var portGroup = new joint.shapes.standard.Rectangle({
	position:{x:10, y:10},
	size:{height:30,width:30},
	ports: {
		groups: {
			'up': {},
			'down': {}
		},
	},
	attrs:{
		fill: "gray",
		nodeProperties: {
			identifier: 'portGroup',
		}
	}
});


/**Generic Port Models For Grouped Ports**/
/*Can be Tx/Rx or any single Port*/
var portSingleModel = {
		markup: '<rect strokegit ="red" fill="gray"/><image/>',
		id: '',
		args: {x:0,y:0}, 
		attrs: {
			image: {
				"xlink:href": "",
				ref: 'rect',
				width: "",
				height: "",
				preserveAspectRatio: 'none'
			},
			rect: {
				width: "",
				height: "",
				stroke: "none"
				
			},
			nodeProperties: {
				text: '',
			}
		},
};
/*Complete Port Group*/
var portGroupModel = new joint.shapes.standard.Rectangle({
	position:{x:10, y:10},
	size:{height:6,width:6},
	attrs:{
		rect:{
			stroke: 'gray',
			'stroke-width':1
		},
		nodeProperties: {
			identifier: 'portGroupModel',
			portGroupNum:{},
		}
	}
});

/**Generic Port Model For Image based Ports**/
var portImageModel= new joint.shapes.standard.Rectangle({
	markup: '<rect strokegit ="red" fill="gray"/><image/>',
	position:{x:10, y:10},
	size:{height:6,width:6},
	attrs:{
		image: {
			"xlink:href": "",
			ref: 'rect',
			width: "10px",
			height: "10px",
			preserveAspectRatio: 'none'
		},
		rect: {
			rx: 1,
			ry: 1,
			width: "10px",
			height: "10px"
		},
		text:{
			text:''
		},
		nodeProperties: {
			identifier: '',
		}
	}
});

/**Port Model for Debug Ports**/
var portDebugModel = new joint.shapes.standard.Rectangle({
	markup: '<rect strokegit ="red" fill="gray"/><image/>',
	position:{x:10, y:10},
	size:{height:6,width:6},
	attrs:{
		image: {
			"xlink:href": "images/chassis/port_debug.jpg",
			ref: 'rect',
			width: "6px",
			height: "6px",
			preserveAspectRatio: 'none'
		},
		rect: {
			rx: 1,
			ry: 1,
			width: "6px",
			height: "6px"
		},
		nodeProperties:{
			identifier:""
		}
	}
});

/**Port Model for Ethernet RJ45 Ports**/
var portEthernetModel = new joint.shapes.standard.Rectangle({
	markup: '<rect strokegit ="red" fill="gray"/><image/>',
	position:{x:10, y:10},
	size:{height:8,width:8},
	attrs:{
		image: {
			"xlink:href": "images/chassis/port_ethernet.jpg",
			ref: 'rect',
			width: "8px",
			height: "8px",
			preserveAspectRatio: 'none'
		},
		rect: {
			rx: 1,
			ry: 1,
			width: "8px",
			height: "8px"
		},
		nodeProperties:{
			identifier:""
		}
	}
});

/**Y Cable Related Stuff Starts**/
var ycable_on = new joint.shapes.basic.Generic({
	markup: '<rect stroke="black" stroke-width="1" rx="3" ry="3"/><image/>',
	size: { height: 20, width: 20 },
	attrs: {
		rect: {
			'ref-width': '100%',
			'ref-height': '100%',
		},
		image: {
			"xlink:href": "images/chassis/ycable_on.png",
			width: 20,
			height: 20,
			ref: 'rect',
			preserveAspectRatio: 'none'
		},
		nodeProperties: {
			type: 'ycable_on',
			typeName: 'ycable_on',
		}

	}
});
var ycable_off = new joint.shapes.basic.Generic({
	markup: '<rect stroke="black" stroke-width="1" rx="3" ry="3"/><image/>',
	size: { height: 20, width: 20 },
	attrs: {
		rect: {
			'ref-width': '100%',
			'ref-height': '100%',
		},
		image: {
			"xlink:href": "images/chassis/ycable_off.png",
			width: 20,
			height: 20,
			ref: 'rect',
			preserveAspectRatio: 'none'
		},
		nodeProperties: {
			type: 'ycable_off',
			typeName: 'ycable_off',
		}

	}
});



var tray=new joint.shapes.cdot.dwdm.cardPlate();
tray.resize(300,30);
tray.attr({
	body: {
		stroke: '#404040',
		fill: '#a6a6a6',
		event: 'cell:pointerdown'
	},
	label: {
		text: "",
		refX: '50%',
		refY: '95%',
		background: 'blue',
		fill: 'white',
		fontSize: '50%',
		fontWeight: 'Bold'
	},
	plateImage: {
		refHeight: "100%",
		refWidth:"100%",
		preserveAspectRatio: 'none',
		"xlink:href":"images/chassis/traybg.jpg" 
	},
	logoImage: { 
		refX: '60%',
		refY: '1%',
		refHeight: "20%",
		refWidth:"25%",
		"xlink:href":"" 
	},
	nodeProperties:{
		identifier:"Card",
		ports: {},
		portNames: {},
		typeName: {},
		slotWidth: {},
		direction: {},
	}
});
	
/*Y Cable Related Stuff Ends*/

/*Field Models*/
joint.dia.Element.define('cdot.dwdm.npt.Field', 
	{
		attrs: {
			c: {
				strokeWidth: 1,
				stroke: '#000000',
				fill: 'blue'
			},
		}
	},
	{
		markup: 
			[
				{
				tagName: 'circle',
				selector: 'c'
				}
			]
	}
);

var fieldModel=new joint.shapes.cdot.dwdm.npt.Field();
fieldModel.attr({
    c: {
        refRCircumscribed: '6',
        refCx: '8',
        refCy: '8'
    },
    nodeProperties:{
    	identifier:"Field",
    	direction:"",
    	typeName:""
    }
});

/**link Model**/
var linkModel = new joint.dia.Link({
	attrs: {
		'.connection': { stroke: '#ff4000' },
		'.marker-source': { fill: 'white', d: 'M 6 0 L 0 3 L 6 6 z' },
		'.marker-target': { fill: 'white', d: 'M 6 0 L 0 3 L 6 6 z' },
		'.marker-arrowhead-group-source, .marker-arrowhead-group-target':{
			display: 'none' 
		},
		'.link-tools': {
			display: 'none'
		},
		'.marker-vertex, .marker-vertex-remove, .marker-vertex-remove-area ': {
			display: 'none'
		},
		nodeProperties:{
			source:{},
			target:{},
			identifier:{}
		}
	}
});
linkModel.router('normal');
linkModel.connector('rounded');

/*Cards*/
var ModelCardPaBa,ModelCardOpm, ModelCardWss1x2,ModelCardMpnCgm,ModelCardOlp,ModelCardCscc,ModelCardMpnCgm,ModelCardMpnCgx,
ModelCardOtdr1x4,ModelCardMuxDemux;

/**Ports**/
var modelPortPaBaBrxSad, modelPortPaBaBtxBtm, modelPortPaBaPrxSdp, modelPortPaBaPtxPtm;
var modelPortIlaIrxSdp,modelPortIlaSadItx,modelPortItm;
var modelPortOpm1x2Orx,modelPortOpm1x2ch1_2;
var modelPortOpm1x8EastBtmPtm,modelPortOpm1x8EastMtmDtm,modelPortOpm1x8WestBtmPtm,modelPortOpm1x8WestMtmDtm;
var modelPortOpm1x16Orx,modelPortOpm1x16Stx,modelPortOpm1x16ch1_4,modelPortOpm1x16ch5_8,modelPortOpm1x16ch9_12,
	modelPortOpm1x16ch13_16;
var modelPortWss1x2WrxWad,modelPortWss1x2WtxSin,modelPortWss1x2WdpWxp;
var modelPortNtxPtx, modelPortOrxOtx, modelPortNrxPrx;
var modelAddRxPortGroup,modelAddTxPortGroup,modelDropRxPortGroup,modelDropTxPortGroup;
var modelPortGroupWssTx1,modelPortGroupWssTx2,modelPortWssWtc,modelPortGroupWssRx1,modelPortGroupWssRx2,modelPortWssWrc;
var modelPortGroupWss2120Tx1,modelPortGroupWss2120Tx2,modelPortGroupWss2120Tx3,modelPortGroupWss2120Tx4,modelPortGroupWss2120Tx5,
    modelPortGroupWss2120Rx1,modelPortGroupWss2120Rx2,modelPortGroupWss2120Rx3,modelPortGroupWss2120Rx4,modelPortGroupWss2120Rx5,
	modelPortWss2120WtcWrc;
var modelPortGroupWss812Dc1,modelPortGroupWss812Dc2,modelPortGroupWss812Dc3,
	modelPortGroupWss812Dd1,modelPortGroupWss812Dd5,
	modelPortGroupWss812Ac1,modelPortGroupWss812Ac2,modelPortGroupWss812Ac3,
	modelPortGroupWss812Ad1,modelPortGroupWss812Ad2;
var modelPortGroupDropChannel1,modelPortGroupDropChannel2,modelPortGroupDropChannel3,modelPortGroupDropChannel4,
	modelPortGroupDropDirection1,modelPortGroupDropDirection2,
	modelPortGroupAddChannel1,modelPortGroupAddChannel2,modelPortGroupAddChannel3,modelPortGroupAddChannel4,
	modelPortGroupAddDirection1,modelPortGroupAddDirection2;
var osc_p1,osc_p2,osc_p3,osc_p4,osc_p5,osc_p6,osc_p7,osc_p8;
var modelPortGroupOtdr1x4,modelPortOtdr1x4SrxOtx,modelPortOtdr1x4abcxyz;
var osc_p1,osc_p2,osc_p3,osc_p4,osc_p5,osc_p6,osc_p7,osc_p8;
var modelPortMuxOcmTapAndOutput,modelPortMuxTapAndInput;
var modelPortDemuxOcmTapAndOutput,modelPortDemuxTapAndInput;
var	modelPortMuxL1L3,modelPortMuxL5L7,modelPortMuxL9L11,modelPortMuxL13L15,modelPortMuxL17L19,modelPortMuxL21L23,
	modelPortMuxL25L27,modelPortMuxL29L31,modelPortMuxL33L35,modelPortMuxL37L39,modelPortMuxL41L43,modelPortMuxL45L47,modelPortMuxL49L51,modelPortMuxL53L55,
	modelPortMuxL57L59,modelPortMuxL61L63,modelPortMuxL65L67,modelPortMuxL69L71,modelPortMuxL73L75,modelPortMuxL77L79;
	
var modelPortMuxL2L4,modelPortMuxL6L8,modelPortMuxL10L12,modelPortMuxL14L16,modelPortMuxL18L20,modelPortMuxL22L24,
    modelPortMuxL26L28,modelPortMuxL30L32,modelPortMuxL34L36,modelPortMuxL38L40,modelPortMuxL42L44,modelPortMuxL46L48,modelPortMuxL50L52,modelPortMuxL54L56,
    modelPortMuxL58L60,modelPortMuxL62L64,modelPortMuxL66L68,modelPortMuxL70L72,modelPortMuxL74L76,modelPortMuxL78L80;

var	modelPortDemuxL1L3,modelPortDemuxL5L7,modelPortDemuxL9L11,modelPortDemuxL13L15,modelPortDemuxL17L19,modelPortDemuxL21L23,
	modelPortDemuxL25L27,modelPortDemuxL29L31,modelPortDemuxL33L35,modelPortDemuxL37L39,modelPortDemuxL41L43,modelPortDemuxL45L47,modelPortDemuxL49L51,modelPortDemuxL53L55,
	modelPortDemuxL57L59,modelPortDemuxL61L63,modelPortDemuxL65L67,modelPortDemuxL69L71,modelPortDemuxL73L75,modelPortDemuxL77L79;

var modelPortDemuxL2L4,modelPortDemuxL6L8,modelPortDemuxL10L12,modelPortDemuxL14L16,modelPortDemuxL18L20,modelPortDemuxL22L24,
	modelPortDemuxL26L28,modelPortDemuxL30L32,modelPortDemuxL34L36,modelPortDemuxL38L40,modelPortDemuxL42L44,modelPortDemuxL46L48,modelPortDemuxL50L52,modelPortDemuxL54L56,
	modelPortDemuxL58L60,modelPortDemuxL62L64,modelPortDemuxL66L68,modelPortDemuxL70L72,modelPortDemuxL74L76,modelPortDemuxL78L80;
	
	

/**Links' Descriptor**/
var links={
		"CGX":{
			"east":
			{	
				"Odd":{
					"cablingActiveFor":[],
					"Odd Mux/ Demux Unit":[
						{
							link:{},
							source:{ CardSlot: {}, portGroupId: 11, port: portUp },
							target:{ CardSlot: {}, portGroupId: 0, port: 0	}
						},
						{
							link:{},
							source:{ CardSlot: {}, portGroupId: 11, port: portDown },
							target:{ CardSlot: {}, portGroupId: 0, port: 0	}
						},
						]
				},
				"Even":{
					"cablingActiveFor":[],
					"Even Mux/Demux Unit":[
						{
							link:{},
							source:{ CardSlot: {}, portGroupId: 11, port: portUp },
							target:{ CardSlot: {}, portGroupId: 0, port: 0	}
						},
						{
							link:{},
							source:{ CardSlot: {}, portGroupId: 11, port: portDown },
							target:{ CardSlot: {}, portGroupId: 0, port: 0	}
						},
						]
				}
			},
			"west":
			{
				"Odd":{
					"cablingActiveFor":[],
					"Odd Mux/ Demux Unit":[
						{
							link:{},
							source:{ CardSlot: {}, portGroupId: 11, port: portUp },
							target:{ CardSlot: {}, portGroupId: 0, port: 0	}
						},
						{
							link:{},
							source:{ CardSlot: {}, portGroupId: 11, port: portDown },
							target:{ CardSlot: {}, portGroupId: 0, port: 0	}
						},
						]
				},
				"Even":{
					"cablingActiveFor":[],
					"Even Mux/Demux Unit":[
						{
							link:{},
							source:{ CardSlot: {}, portGroupId: 11, port: portUp },
							target:{ CardSlot: {}, portGroupId: 0, port: 0	}
						},
						{
							link:{},
							source:{ CardSlot: {}, portGroupId: 11, port: portDown },
							target:{ CardSlot: {}, portGroupId: 0, port: 0	}
						},
						]
				}

			}
		},
		"CGM":{
			"east":
			{	
				"Odd":{
					"cablingActiveFor":[],
					"Odd Mux/ Demux Unit":[
						{
							link:{},
							source:{ CardSlot: {}, portGroupId: 11, port: portUp },
							target:{ CardSlot: {}, portGroupId: 0, port: 0	}
						},
						{
							link:{},
							source:{ CardSlot: {}, portGroupId: 11, port: portDown },
							target:{ CardSlot: {}, portGroupId: 0, port: 0	}
						},
						]
				},
				"Even":{
					"cablingActiveFor":[],
					"Even Mux/Demux Unit":[
						{
							link:{},
							source:{ CardSlot: {}, portGroupId: 11, port: portUp },
							target:{ CardSlot: {}, portGroupId: 0, port: 0	}
						},
						{
							link:{},
							source:{ CardSlot: {}, portGroupId: 11, port: portDown },
							target:{ CardSlot: {}, portGroupId: 0, port: 0	}
						},
						]
				}
			},
			"west":
			{
				"Odd":{
					"cablingActiveFor":[],
					"Odd Mux/ Demux Unit":[
						{
							link:{},
							source:{ CardSlot: {}, portGroupId: 11, port: portUp },
							target:{ CardSlot: {}, portGroupId: 0, port: 0	}
						},
						{
							link:{},
							source:{ CardSlot: {}, portGroupId: 11, port: portDown },
							target:{ CardSlot: {}, portGroupId: 0, port: 0	}
						},
						]
				},
				"Even":{
					"cablingActiveFor":[],
					"Even Mux/Demux Unit":[
						{
							link:{},
							source:{ CardSlot: {}, portGroupId: 11, port: portUp },
							target:{ CardSlot: {}, portGroupId: 0, port: 0	}
						},
						{
							link:{},
							source:{ CardSlot: {}, portGroupId: 11, port: portDown },
							target:{ CardSlot: {}, portGroupId: 0, port: 0	}
						},
						]
				}

			}
		},
		"PA/BA":
		{
			"east":
			{
			"cablingActiveFor":[],
			"OCM1x8":[
					{
						link:{},
						source:{ CardSlot: {}, portGroupId: 1, port: portDown },
						target:{ CardSlot: {}, portGroupId: 0, port: portUp	}
					},
					{
						link:{},
						source:{ CardSlot: {}, portGroupId: 3, port: portDown },
						target:{ CardSlot: {}, portGroupId: 0, port: portDown }
					}
					],
			"WSS1x2":[
					{
						link:{},
						source:{ CardSlot: {}, portGroupId: 0, port: portUp },
						target:{ CardSlot: {}, portGroupId: 1, port: portUp	}
					},
					{
						link:{},
						source:{ CardSlot: {}, portGroupId: 3, port: portUp },
						target:{ CardSlot: {}, portGroupId: 1, port: portDown	}
					}
				    ],
			"OSC":[
				{
					link:{},
					source:{ CardSlot: {}, portGroupId: 0, port: portDown },
					target:{ CardSlot: {}, portGroupId: 0, port: {id:"Tx"}}
				},
				{
					link:{},
					source:{ CardSlot: {}, portGroupId: 2, port: portDown },
					target:{ CardSlot: {}, portGroupId: 0, port: {id:"Rx"} }
				}
			    ],
			 "Field1":[
				 {
				   link:{},
				   source:{ CardSlot: {}, portGroupId: 1, port: portUp },
				   target: "To Field",
				 }
				 ],
			"Field2":[
				 {
					 link:{},
					 source:{ CardSlot: {}, portGroupId: 2, port: portUp },
					 target: "From Field",
				 },
				 ]
			},
			"west":
			{
			"cablingActiveFor":[],
			"OCM1x8":[
					{
						link:{},
						source:{ CardSlot: {}, portGroupId: 1, port: portDown },
						target:{ CardSlot: {}, portGroupId: 2, port: portUp	}
					},
					{
						link:{},
						source:{ CardSlot: {}, portGroupId: 3, port: portDown },
						target:{ CardSlot: {}, portGroupId: 2, port: portDown }
					}
					],
			"WSS1x2":[
				{
					link:{},
					source:{ CardSlot: {}, portGroupId: 0, port: portUp },
					target:{ CardSlot: {}, portGroupId: 1, port: portUp	}
				},
				{
					link:{},
					source:{ CardSlot: {}, portGroupId: 3, port: portUp },
					target:{ CardSlot: {}, portGroupId: 1, port: portDown	}
				}
			    ],
			"OSC":[
				{
					link:{},
					source:{ CardSlot: {}, portGroupId: 0, port: portDown },
					target:{ CardSlot: {}, portGroupId: 1, port: {id:"Tx"}}
				},
				{
					link:{},
					source:{ CardSlot: {}, portGroupId: 2, port: portDown },
					target:{ CardSlot: {}, portGroupId: 1, port: {id:"Rx"} }
				}
			    ],
			 "Field1":[
				 {
				   link:{},
				   source:{ CardSlot: {}, portGroupId: 1, port: portUp },
				   target: "To Field",
				 }
				 ],
			"Field2":[
				 {
					 link:{},
					 source:{ CardSlot: {}, portGroupId: 2, port: portUp },
					 target: "From Field",
				 },
				 ]
			}
		},
		"WSS1x2":
		{
			"east":
			{
			"cablingActiveFor":[],
			"PA/BA":[
					{
						link:{},
						source:{ CardSlot: {}, portGroupId: 1, port: portUp }, //WTX
						target:{ CardSlot: {}, portGroupId: 0, port: portUp	}  //BRX
					},
					{
						link:{},
						source:{ CardSlot: {}, portGroupId: 1, port: portDown }, //SIN
						target:{ CardSlot: {}, portGroupId: 3, port: portUp } //PTX
					}
					],
			"Field1":[
				{
					link:{},
					source:{ CardSlot: {}, portGroupId: 0, port: portUp }, 
					target: "From Service Provider"  
				},
				],
			"Field2":[
				{
					link:{},
					source:{ CardSlot: {}, portGroupId: 2, port: portDown }, 
					target: "To Service Provider"  
				},
				],
			},
			"west":
			{
			"cablingActiveFor":[],
			"PA/BA":[
					{
						link:{},
						source:{ CardSlot: {}, portGroupId: 1, port: portUp }, //WTX
						target:{ CardSlot: {}, portGroupId: 0, port: portUp	}  //BRX
					},
					{
						link:{},
						source:{ CardSlot: {}, portGroupId: 1, port: portDown }, //SIN
						target:{ CardSlot: {}, portGroupId: 3, port: portUp } //PTX
					}
					],
			"Field1":[
				{
					link:{},
					source:{ CardSlot: {}, portGroupId: 0, port: portUp }, 
					target: "From Service Provider"  
				},
				],
			"Field2":[
				{
					link:{},
					source:{ CardSlot: {}, portGroupId: 2, port: portDown }, 
					target: "To Service Provider"  
				},
				],
			},
		},
		"OCM1x8":
		{
			"east":{
				"cablingActiveFor":[],
				"PA/BA":[
					{
						link:{},
						source:{ CardSlot: {}, portGroupId: 0, port: portUp }, //BTM East
						target:{ CardSlot: {}, portGroupId: 1, port: portDown }  //BTM
					},
					{
						link:{},
						source:{ CardSlot: {}, portGroupId: 0, port: portDown }, //PTM East
						target:{ CardSlot: {}, portGroupId: 3, port: portDown } //PTM
					}
					],
			},
			"west":{
				"cablingActiveFor":[],
				"PA/BA":[
					{
						link:{},
						source:{ CardSlot: {}, portGroupId: 2, port: portUp }, //BTM West
						target:{ CardSlot: {}, portGroupId: 1, port: portDown }  //BTM
					},
					{
						link:{},
						source:{ CardSlot: {}, portGroupId: 2, port: portDown }, //PTM West
						target:{ CardSlot: {}, portGroupId: 3, port: portDown } //PTM
					}
					],
			}
		},
};

/**Helper Functions**/
function setGrid(size, color, offset) {
//	Set grid size on the JointJS paper object (joint.dia.Paper instance)
	paperChassis.options.gridsize = gridsize;
//	Draw a grid into the HTML 5 canvas and convert it to a data URI image
	var canvas = $('<canvas/>', { width: size, height: size });
	canvas[0].width = size;
	canvas[0].height = size;
	var context = canvas[0].getContext('2d');
	context.beginPath();
	context.rect(1, 1, 1, 1);
	context.fillStyle = color;
	context.fill();
//	Finally, set the grid background image of the paper container element.
	var gridBackgroundImage = canvas[0].toDataURL('image/png');
	$(paperChassis.el.childNodes[0]).css('background-image', 'url("' + gridBackgroundImage + '")');
	if(typeof(offset) != 'undefined'){  
		$(paperChassis.el.childNodes[0]).css('background-position', offset.x + 'px ' + offset.y + 'px');
	}
}

function removeLinksForAllCards(){
	danglingLinksArr=[];
	for(let sourceCard in links){
		for(let direction in links[sourceCard]){
			if(sourceCard=="CGX" || sourceCard=="CGM"){
				for(let oddEven in links[sourceCard][direction]){
					for(let targetCard in links[sourceCard][direction][oddEven]){
						if(targetCard.indexOf("cabling")==-1){
							for(let i in links[sourceCard][direction][oddEven][targetCard]){
								if(typeof(links[sourceCard][direction][oddEven][targetCard][i].link)!="undefined" &&
										_.isEmpty(links[sourceCard][direction][oddEven][targetCard][i].link)==false){
									if(links[sourceCard][direction][oddEven][targetCard][i].link.getTargetElement() instanceof joint.shapes.cdot.dwdm.npt.Field)
										links[sourceCard][direction][oddEven][targetCard][i].link.getTargetElement().remove();
									links[sourceCard][direction][oddEven][targetCard][i].link.remove();
								}
								links[sourceCard][direction][oddEven]["cablingActiveFor"]=[];
							}
						}
					}
				}
			}
			else{
				for(let targetCard in links[sourceCard][direction]){
					if(targetCard.indexOf("cabling")==-1){
						for(let i in links[sourceCard][direction][targetCard]){
							if(typeof(links[sourceCard][direction][targetCard][i].link)!="undefined"&&
									_.isEmpty(links[sourceCard][direction][targetCard][i].link)==false){
								if(links[sourceCard][direction][targetCard][i].link.getTargetElement() instanceof joint.shapes.cdot.dwdm.npt.Field)
									links[sourceCard][direction][targetCard][i].link.getTargetElement().remove();
								links[sourceCard][direction][targetCard][i].link.remove();
							}
							links[sourceCard][direction]["cablingActiveFor"]=[];
						}
					}
				}
			}
		}
	}
	danglingLinksArr=graphChassis.getLinks();
	console.log(danglingLinksArr);
	for(let i in danglingLinksArr)
	{
		danglingLinksArr[i].remove();
	}
}

