   var dwdmDetails = {};
   
   /*Cards Definitions*/
   //PA/BA 
   ModelCardPaBa = ModelPlate.clone();
   ModelCardPaBa.attr('nodeProperties/ports',[modelPortPaBaBrxSad,modelPortPaBaBtxBtm,modelPortPaBaPrxSdp,modelPortPaBaPtxPtm]);
   ModelCardPaBa.attr('nodeProperties/portNames',["BRX","SAD","BTX","BTM","PRX","SDP","PTX","PTM"]);
   ModelCardPaBa.attr('nodeProperties/slotWidth', '1');
   
   //ILA 
   ModelCardIla = ModelPlate.clone();
   ModelCardIla.attr('nodeProperties/ports',[modelPortIlaIrxSdp,modelPortIlaSadItx,modelPortItm]);
   ModelCardIla.attr('nodeProperties/portNames',["IRX","SDP","SAD","ITX","ITM",""]);
   ModelCardIla.attr('nodeProperties/slotWidth', '1');

   //OPM(OCM1x2)
   ModelCardOpm1x2 = ModelPlate.clone();
   ModelCardOpm1x2.attr('nodeProperties/ports',[modelPortOpm1x2Orx,modelPortOpm1x2ch1_2]);
   ModelCardOpm1x2.attr('nodeProperties/portNames',["ORX","1-2"]);
   ModelCardOpm1x2.attr('nodeProperties/slotWidth', '1');
   
   //OPM(OCM1x8)
   ModelCardOpm1x8 = ModelPlate.clone();
   ModelCardOpm1x8.attr('nodeProperties/ports',[modelPortOpm1x8EastBtmPtm,modelPortOpm1x8EastMtmDtm,modelPortOpm1x8WestBtmPtm,modelPortOpm1x8WestMtmDtm]);
   ModelCardOpm1x8.attr('nodeProperties/portNames',["BTM(East)","PTM(East)","MTM(East)","DTM(East)","BTM(West)","PTM(West)","MTM(West)","DTM(West)"]);
   ModelCardOpm1x8.attr('nodeProperties/slotWidth', '1');
   
   //OPM(OCM1x16)
   ModelCardOpm1x16 = ModelPlate.clone();
   ModelCardOpm1x16.attr('nodeProperties/ports',[modelPortOpm1x16Orx,modelPortOpm1x16Stx,modelPortOpm1x16ch1_4,
	   											modelPortOpm1x16ch5_8,modelPortOpm1x16ch9_12,modelPortOpm1x16ch13_16]);
   ModelCardOpm1x16.attr('nodeProperties/portNames',["ORX","STX","1-4","5-8","9-13","15-16"]);
   ModelCardOpm1x16.attr('nodeProperties/slotWidth', '1');
   
   //WSS1X2(WSS1x2)
   ModelCardWss1x2 = ModelPlate.clone();
   ModelCardWss1x2.attr('nodeProperties/ports',[modelPortWss1x2WrxWad,modelPortWss1x2WtxSin,modelPortWss1x2WdpWxp]);
   ModelCardWss1x2.attr('nodeProperties/portNames',["WRX","WAD","WTX","SIN","WDP","WXP"]);
   ModelCardWss1x2.attr('nodeProperties/slotWidth', '1');
   
   //OLP
   ModelCardOlp = ModelPlate.clone();
   ModelCardOlp.attr('nodeProperties/ports',[modelPortNtxPtx, modelPortOrxOtx, modelPortNrxPrx]);
   ModelCardOlp.attr('nodeProperties/portNames',["NTX","PTX","ORX","OTX","NRX","PRX"]);
   ModelCardOlp.attr('nodeProperties/slotWidth', '1');
   
   //EDARY
   ModelCardEdary = ModelPlate.clone();
   ModelCardEdary.attr('nodeProperties/ports',[modelAddRxPortGroup,modelAddTxPortGroup,modelDropRxPortGroup,modelDropTxPortGroup]);
   ModelCardEdary.attr('nodeProperties/portNames',["Add Receive","Add Transmit","Drop Receive","Drop Transmit"]);
   
   //Otdr1x4
   ModelCardOtdr1x4 = ModelPlate.clone();
   ModelCardOtdr1x4.attr('nodeProperties/ports',[modelPortGroupOtdr1x4,modelPortOtdr1x4SrxOtx,modelPortOtdr1x4abcxyz]);
   ModelCardOtdr1x4.attr('nodeProperties/portNames',["Transmit","SRX","OTX","abc","xyz"]);
   
   //muxDemux tray
   ModelCardOddMuxDemux = tray.clone();
   ModelCardOddMuxDemux.attr('nodeProperties/portsMux',[modelPortMuxOcmTapAndOutput,modelPortMuxL1L3,modelPortMuxL5L7,modelPortMuxL9L11,modelPortMuxL13L15,modelPortMuxL17L19,modelPortMuxL21L23,modelPortMuxL25L27,modelPortMuxL29L31,modelPortMuxL33L35,modelPortMuxL37L39,
	   										 		    modelPortMuxTapAndInput,modelPortMuxL41L43,modelPortMuxL45L47,modelPortMuxL49L51,modelPortMuxL53L55,modelPortMuxL57L59,modelPortMuxL61L63,modelPortMuxL65L67,modelPortMuxL69L71,modelPortMuxL73L75,modelPortMuxL77L79]);
   ModelCardOddMuxDemux.attr('nodeProperties/portsDemux',[modelPortDemuxOcmTapAndOutput,modelPortDemuxL1L3,modelPortDemuxL5L7,modelPortDemuxL9L11,modelPortDemuxL13L15,modelPortDemuxL17L19,modelPortDemuxL21L23,modelPortDemuxL25L27,modelPortDemuxL29L31,modelPortDemuxL33L35,modelPortDemuxL37L39,
		    											  modelPortDemuxTapAndInput,modelPortDemuxL41L43,modelPortDemuxL45L47,modelPortDemuxL49L51,modelPortDemuxL53L55,modelPortDemuxL57L59,modelPortDemuxL61L63,modelPortDemuxL65L67,modelPortDemuxL69L71,modelPortDemuxL73L75,modelPortDemuxL77L79]);
   ModelCardOddMuxDemux.attr('nodeProperties/portNames',["OCM TAP,OUTPUT","1,3","5,7","9,11","13,15","17,19","21,23","25,27","29,31","33,35","37,39",
	   											   		 "TAP, INPUT","41,43","45,47","49,51","53,55","57,59","61,63","65,67","69,71","73,75","77,79"])
   
   ModelCardEvenMuxDemux = tray.clone();
   ModelCardEvenMuxDemux.attr('nodeProperties/portsMux',[modelPortMuxOcmTapAndOutput,modelPortMuxL2L4,modelPortMuxL6L8,modelPortMuxL10L12,modelPortMuxL14L16,modelPortMuxL18L20,modelPortMuxL22L24,modelPortMuxL26L28,modelPortMuxL30L32,modelPortMuxL34L36,modelPortMuxL38L40,
	   										     	     modelPortMuxTapAndInput,modelPortMuxL42L44,modelPortMuxL46L48,modelPortMuxL50L52,modelPortMuxL54L56,modelPortMuxL58L60,modelPortMuxL62L64,modelPortMuxL66L68,modelPortMuxL70L72,modelPortMuxL74L76,modelPortMuxL78L80]);
   ModelCardEvenMuxDemux.attr('nodeProperties/portsDemux',[modelPortDemuxOcmTapAndOutput,modelPortDemuxL2L4,modelPortDemuxL6L8,modelPortDemuxL10L12,modelPortDemuxL14L16,modelPortDemuxL18L20,modelPortDemuxL22L24,modelPortDemuxL26L28,modelPortDemuxL30L32,modelPortDemuxL34L36,modelPortDemuxL38L40,
     	  											  	   modelPortDemuxTapAndInput,modelPortDemuxL42L44,modelPortDemuxL46L48,modelPortDemuxL50L52,modelPortDemuxL54L56,modelPortDemuxL58L60,modelPortDemuxL62L64,modelPortDemuxL66L68,modelPortDemuxL70L72,modelPortDemuxL74L76,modelPortDemuxL78L80]);
   ModelCardEvenMuxDemux.attr('nodeProperties/portNames',["OCM TAP,OUTPUT","2,4","6,8","10,12","14,16","18,20","22,24","26,28","30,32","34,36","38,40",
	   											   		  "TAP, INPUT","42,44","46,48","50,52","54,56","58,60","62,64","66,68","70,72","74,76","78,80"])
   

  //WSS2x1x9
   ModelCardWss219 = ModelPlate.clone();
   ModelCardWss219.attr('nodeProperties/ports',[modelPortGroupWssTx1,modelPortGroupWssTx2,modelPortWssWtc,modelPortGroupWssRx1,modelPortGroupWssRx2,modelPortWssWrc]);
   ModelCardWss219.attr('nodeProperties/portNames',["Transmit","Transmit","WTC","Receive","Receive","WRC"]);
   
  //WSS2x1x20
   ModelCardWss2120 = ModelPlate.clone();
   ModelCardWss2120.attr('nodeProperties/ports',[modelPortGroupWss2120Tx1,modelPortGroupWss2120Tx2,modelPortGroupWss2120Tx3,modelPortGroupWss2120Tx4,modelPortGroupWss2120Tx5,
	   											     modelPortGroupWss2120Rx1,modelPortGroupWss2120Rx2,modelPortGroupWss2120Rx3,modelPortGroupWss2120Rx4,modelPortGroupWss2120Rx5,
	   											     modelPortWss2120WtcWrc]);
   ModelCardWss2120.attr('nodeProperties/portNames',["Transmit","Transmit","Transmit","Transmit","Transmit",
	   												 "Receive","Receive","Receive","Receive","Receive",
	   												 "WTC-WRC"]);
   
 //WSS8x12
   ModelCardWss812 = ModelPlate.clone();
   ModelCardWss812.attr('nodeProperties/ports',[modelPortGroupWss812Dc1,modelPortGroupWss812Dc2,modelPortGroupWss812Dc3,
	   											modelPortGroupWss812Dd1,modelPortGroupWss812Dd5,
	   											modelPortGroupWss812Ac1,modelPortGroupWss812Ac2,modelPortGroupWss812Ac3,
	   											modelPortGroupWss812Ad1,modelPortGroupWss812Ad2]);
   ModelCardWss812.attr('nodeProperties/portNames',["Drop Channels","Drop Channels","Drop Channels",
	   												"Drop Directions","Drop Directions",
	   												"Add Channels","Add Channels","Add Channels",
	   												"Add Directions","Add Directions"]);
   
  //MCS
   ModelCardMcs = ModelPlate.clone();
   ModelCardMcs.attr('nodeProperties/ports',[modelPortGroupDropChannel1,modelPortGroupDropChannel2,modelPortGroupDropChannel3,modelPortGroupDropChannel4,
	   										 modelPortGroupDropDirection1,modelPortGroupDropDirection2,
	   										 modelPortGroupAddChannel1,modelPortGroupAddChannel2,modelPortGroupAddChannel3,modelPortGroupAddChannel4,
   											 modelPortGroupAddDirection1,modelPortGroupAddDirection2]);
   ModelCardMcs.attr('nodeProperties/portNames',["Drop Channel","Drop Channel","Drop Channel","Drop Channel",
	   											 "Drop Directions","Drop Directions",
	   											 "Add Channel","Add Channel","Add Channel","Add Channel",
	   											 "Add Directions","Add Directions"]);
   //200G Mux
   ModelCard200gMux = ModelPlate.clone();
   //MPN(CGM)
   ModelCardMpnCgm = ModelPlate.clone();
   //MPN(CGX)
   ModelCardMpnCgx = ModelPlate.clone();
   //MPN 10G = XGM
   ModelCardXgm = ModelPlate.clone();
//   ILA (RAMAN-HYBRID)
//   RAMAN (HYBRID)
   
   //CSCC
   ModelCardCscc = ModelPlate.clone();
   
   //OSC
   ModelCardOsc = ModelPlate.clone();
   ModelCardOsc.attr('nodeProperties/ports',[osc_p1,osc_p2,osc_p3,osc_p4,osc_p5,osc_p6,osc_p7,osc_p8]);
   ModelCardOsc.attr('nodeProperties/portNames',["Supervisory Port 1","Supervisory Port 2","Supervisory Port 3","Supervisory Port 4","Supervisory Port 5","Supervisory Port 6","Supervisory Port 7","Supervisory Port 8"]);

   function transformForSixSlot(element) {
   	var center = element.getBBox().center();
   	var elements = [element];
   	var startX=element.getParentCell().position().x, startY=element.getParentCell().position().y+40;
   	let slotId=element.get('attrs').nodeProperties.slotId;
   	let slotWidth=element.get('attrs').nodeProperties.slotWidth;
   	console.log("slot "+slotId);
   	element.getEmbeddedCells({ deep: true }).reduce(function(acc, cell) {
   		if (cell.isElement()) acc.push(cell);
   		return acc;
   	}, elements);


   	let data = {
   			center: center,
   			elements: elements,
   			rotationStartAngles: elements.map(function(el) { return el.angle(); }),
   	}
   	data.elements.forEach(function(element, index) {
   		var rotationStartAngle = data.rotationStartAngles[index];
   		element.rotate(-90, true, {x:startX,y:startY});
   	}, this);
	element.translate(32,( (((slotId-1)*20)-18)/*Bring at zero level*/+(20*(7-slotId)+20*1)/*ShiftDown*/));
   }
   
   function  drawChassis(rack_id){
	   graphChassis.clear();
	   var TopPosX, TopPosY;
	   TopPosX = ($("#chassisCanvasId").width()/8) + 10;
	   TopPosY= 20;
       paperChassis.setOrigin(0,0);
       //check if its a six slot chassis
       sixSlotChassis=false;
       for (let i = 0; i < 3; i++) {
		   if(dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].subRackList[i].chassisType=="SIX-SLOT CHASSIS")
			   sixSlotChassis=true;
       }
       
       //draw ChassisParent
       let chassisParent = new joint.shapes.standard.Rectangle();
//       chassisParent.attr('body/fill', 'transparent');chassisParent.attr('body/strokeWidth', '0');
       if(sixSlotChassis && dwdmDetails.chassisDetails.chassis.specs.nodeType!=nodeTypeIla )
    	   chassisParent.position(TopPosX,TopPosY).resize(sixSlotSubrackSlotWidth,sixSlotSubrackInnerHeight*3+80).attr('nodeProperties/text', 'Holder').addTo(graphChassis);
       else if(sixSlotChassis && dwdmDetails.chassisDetails.chassis.specs.nodeType==nodeTypeIla )
    	   chassisParent.position(TopPosX,TopPosY).resize(sixSlotSubrackSlotWidth,sixSlotSubrackInnerHeight*1+80).attr('nodeProperties/text', 'Holder').addTo(graphChassis);
       else 
    	   chassisParent.position(TopPosX,TopPosY).resize(300,825+80).attr('nodeProperties/text', 'Holder').addTo(graphChassis);
       
       //Draw PDUs
       let pdus=[];
       for (let i = 0, posY=TopPosY, posX=TopPosX; i < 4; i++) {
    	   if(sixSlotChassis)
    	   {
    		   ModelPdu.attributes.size.width=110;
    	   }
    	   else
    	   {
    		   ModelPdu.attributes.size.width=150;
    	   }
		   pdus[i] = ModelPdu.clone().position(posX,posY).addTo(graphChassis);
		   chassisParent.embed(pdus[i]);
		   if(i==0||i==2)
		   {
			   posX += ModelPdu.attributes.size.width;
		   }
		   else if(i==1)
		   {
			   posX -= ModelPdu.attributes.size.width;
			   posY += ModelPdu.attributes.size.height;
		   }
	   }
       
       var firstSubrackTopPosY= TopPosY+80, firstSubrackTopPosX=TopPosX;
	   //draw sub rack
	   let subracks=[];
	   let chassisType="";
	   let numOfSubracks;
       if(sixSlotChassis && dwdmDetails.chassisDetails.chassis.specs.nodeType==nodeTypeIla )
    	   numOfSubracks=1;
       else
    	   numOfSubracks=3;
       
	   for (let i = 0, posY=firstSubrackTopPosY, posX=firstSubrackTopPosX; i < numOfSubracks; i++) {
		   let subrack;
		   if(dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].subRackList[i].chassisType=="PENTAIR"){
			   chassisType="PENTAIR";
			   ModelSubrack.attributes.size.height=245;
			   ModelSubrack.attributes.size.width=300;
			   subrack = ModelSubrack.clone();
			   if(dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].subRackList[i].cardList.length!=0)
				   subrack.attr("image/xlink:href","images/chassis/pentair.png");
			   else
				   subrack.attr("image/xlink:href","images/chassis/pentair-empty.png");
		   }
		   else if (dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].subRackList[i].chassisType=="EMERSION"){
			   chassisType="EMERSION";
			   ModelSubrack.attributes.size.height=245;
			   ModelSubrack.attributes.size.width=300;
			   subrack = ModelSubrack.clone();
			   if(dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].subRackList[i].cardList.length!=0)
				   subrack.attr("image/xlink:href","images/chassis/rackimg.png");
			   else
				   subrack.attr("image/xlink:href","images/chassis/rackimg-empty.png");
		   }
		   else if (dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].subRackList[i].chassisType=="COMTEL"){
			   chassisType="COMTEL";
			   ModelSubrack.attributes.size.height=245;
			   ModelSubrack.attributes.size.width=300;
			   subrack = ModelSubrack.clone();
			   if(dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].subRackList[i].cardList.length!=0)
				   subrack.attr("image/xlink:href","images/chassis/rackimg.png");
			   else
				   subrack.attr("image/xlink:href","images/chassis/rackimg-empty.png");
		   }
		   else if (dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].subRackList[i].chassisType=="SIX-SLOT CHASSIS"){
			   chassisType="SIX-SLOT CHASSIS";
			   ModelSubrack.attributes.size.height=sixSlotSubrackInnerHeight;
			   ModelSubrack.attributes.size.width=sixSlotSubrackSlotWidth;
			   subrack = ModelSubrack.clone();
			   if(dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].subRackList[i].cardList.length!=0)
				   subrack.attr("image/xlink:href","images/chassis/leanpac.png");
			   else
				   subrack.attr("image/xlink:href","images/chassis/leanpac.png");
		   }
		   subracks[i] = subrack.position(posX,posY).addTo(graphChassis);
		   chassisParent.embed(subracks[i]);
		   posY += ModelSubrack.attributes.size.height;
	   }
	   
	   //insert cards in subracks
	   var fiberHooksHeight=40;
	   var card_y=firstSubrackTopPosY+fiberHooksHeight;
	   var cards_0 = [],cards_1 = [],cards_2 = [];
	   var card;
	   for (i in dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].subRackList) {
		   if(dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].subRackList[i].chassisType=="PENTAIR")
			   var card_start_x=firstSubrackTopPosX+8;
		   else if(dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].subRackList[i].chassisType=="EMERSION")
			   var card_start_x=firstSubrackTopPosX+3;
		   else if(dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].subRackList[i].chassisType=="COMTEL")
			   var card_start_x=firstSubrackTopPosX+3;
		   else if(dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].subRackList[i].chassisType=="SIX-SLOT CHASSIS")
		   {
			   if(dwdmDetails.chassisDetails.chassis.specs.nodeType==nodeTypeIla &&
				  dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].subRackList[i].cardList.length==0)
			   {
				   console.log("going to continue");
				   continue;
			   }

			   var card_start_x=firstSubrackTopPosX+8;
		   }
		   if(dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].subRackList[i].yCableFlag != 1)
		   {
			   let rackId,subRackId,slotId,cardType="",maxSlotPerCard,portList=[],direction;
			   let card_x;
			   let portNames,port_x,port_y;
			   let slotWidthUsed=1;
			   for (j in dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].subRackList[i].cardList ) {
				   if(chassisType!="SIX-SLOT CHASSIS" ||
					  (chassisType=="SIX-SLOT CHASSIS" &&
					  dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].subRackList[i].cardList[j].slotId<=6) )
				   {
				   cardType=dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].subRackList[i].cardList[j].typeName;
				   maxSlotPerCard=dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].subRackList[i].cardList[j].maxSlotPerCard;
				   rackId=dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].subRackList[i].cardList[j].rackId;
				   subRackId=dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].subRackList[i].cardList[j].subRackId;
				   slotId=dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].subRackList[i].cardList[j].slotId;
				   direction=dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].subRackList[i].cardList[j].direction;
				   card_x=card_start_x+(ModelPlate.size().width*(slotId-1));
				   if (cardType == "PA/BA"){
					   portNames = ModelCardPaBa.attr('nodeProperties/portNames');
					   port_x = card_x+7;
					   port_y = card_y+(ModelPlate.size().height/4);
					   card=ModelCardPaBa.clone().position(card_x,card_y);
					   card.attr('nodeProperties/typeName',cardType);
					   card.attr('label/text', cardType).position(card_x,card_y);
					   card.attr('nodeProperties/slotWidth',maxSlotPerCard);
					   card.attr('nodeProperties/rackId',rackId);
					   card.attr('nodeProperties/subRackId',subRackId);
					   card.attr('nodeProperties/slotId',slotId);
					   card.attr('nodeProperties/direction',direction);
					   card.attr('nodeProperties/lambda',"");
					   card.resize(ModelPlate.size().width*maxSlotPerCard,ModelPlate.size().height);
					   card.addTo(graphChassis);
					   let portEthernet = portEthernetModel.clone().position(card_x+7,card_y+ModelPlate.size().height-32).addTo(graphChassis);
					   let portDebug = portDebugModel.clone().position(card_x+7,card_y+ModelPlate.size().height-20).addTo(graphChassis);
					   let ports = card.attr('nodeProperties/ports');
					   for(k in ports){
						   let port;
						   portUp.attrs.nodeProperties.text=portNames[2*k];
						   portDown.attrs.nodeProperties.text=portNames[2*k+1];
						   port=portGroup.clone().resize(7,14).position(port_x,port_y).addPort(portDown).addPort(portUp).addTo(graphChassis);
						   ports[k]=port;
						   port_y+=20;
						   card.embed(port);
					   };
					   card.embed(portEthernet);
					   card.embed(portDebug);
				   }
				   else if (cardType == "ILA"){
					   portNames = ModelCardIla.attr('nodeProperties/portNames');
					   port_x = card_x+7;
					   port_y = card_y+(ModelPlate.size().height/4);
					   card=ModelCardIla.clone().position(card_x,card_y);
					   card.attr('nodeProperties/typeName',cardType);
					   card.attr('label/text', cardType).position(card_x,card_y);
					   card.attr('nodeProperties/slotWidth',maxSlotPerCard);
					   card.attr('nodeProperties/rackId',rackId);
					   card.attr('nodeProperties/subRackId',subRackId);
					   card.attr('nodeProperties/slotId',slotId);
					   card.attr('nodeProperties/direction',direction);
					   card.attr('nodeProperties/lambda',"");
					   card.resize(ModelPlate.size().width*maxSlotPerCard,ModelPlate.size().height);
					   card.addTo(graphChassis);
					   let portEthernet = portEthernetModel.clone().position(card_x+7,card_y+ModelPlate.size().height-32).addTo(graphChassis);
					   let portDebug = portDebugModel.clone().position(card_x+7,card_y+ModelPlate.size().height-20).addTo(graphChassis);
					   let ports = card.attr('nodeProperties/ports');
					   for(k in ports){
						   let port;
						   portUp.attrs.nodeProperties.text=portNames[2*k];
						   portDown.attrs.nodeProperties.text=portNames[2*k+1];
						   port=portGroup.clone().resize(7,14).position(port_x,port_y).addPort(portDown).addPort(portUp).addTo(graphChassis);
						   ports[k]=port;
						   port_y+=20;
						   card.embed(port);
					   };
					   card.embed(portEthernet);
					   card.embed(portDebug);
				   }
				   else if (cardType == "CSCC" || cardType=="MPC"){
					   card=ModelCardCscc.clone().position(card_x,card_y);
					   card.attr('nodeProperties/typeName',cardType);
					   card.attr('label/text', cardType).position(card_x,card_y);
					   card.attr('nodeProperties/slotWidth',maxSlotPerCard);
					   card.attr('nodeProperties/rackId',rackId);
					   card.attr('nodeProperties/subRackId',subRackId);
					   card.attr('nodeProperties/slotId',slotId);
					   card.attr('nodeProperties/direction',direction);
					   card.attr('nodeProperties/lambda',"");
					   card.resize(ModelPlate.size().width*maxSlotPerCard,ModelPlate.size().height);
					   card.addTo(graphChassis);
					   let cardHeight=ModelPlate.size().height;
					   let portDebugMcp = portDebugModel.clone().position(card_x+7,card_y+cardHeight/6).addTo(graphChassis);
					   let portDebugScp = portDebugModel.clone().position(card_x+7,card_y+cardHeight/6+8).addTo(graphChassis);
					   let portEthernet1 = portEthernetModel.clone().position(card_x+3,card_y+cardHeight/4+4).addTo(graphChassis);
					   let portEthernet2 = portEthernetModel.clone().position(card_x+3,card_y+cardHeight/4+4+8).addTo(graphChassis);
					   let portEthernet3 = portEthernetModel.clone().position(card_x+3+8,card_y+cardHeight/4+4).addTo(graphChassis);
					   let portEthernet4 = portEthernetModel.clone().position(card_x+3+8,card_y+cardHeight/4+4+8).addTo(graphChassis);
					   
					   let portTx=JSON.parse(JSON.stringify(portSingleModel));
					   portTx.attrs.image.width="6px"; portTx.attrs.image.height="3px"
					   portTx.attrs.rect.width="6px"; portTx.attrs.rect.height="3px"
					   portTx.attrs.image["xlink:href"]="images/chassis/port_sfp_tx.jpg";
					   portTx.id='Tx';

					   let portRx=JSON.parse(JSON.stringify(portSingleModel));
					   portRx.attrs.image.width="6px"; portRx.attrs.image.height="3px"
					   portRx.attrs.rect.width="6px"; portRx.attrs.rect.height="3px"
					   portRx.attrs.image["xlink:href"]="images/chassis/port_sfp_rx.jpg";
					   portRx.id='Rx';
					   portRx.args.y=3;
					   
					   let b1=portGroupModel.clone().attr("nodeProperties/identifier","sfp-cscc").position(card_x+7,card_y+cardHeight/2.5).addPort(portTx).addPort(portRx).addTo(graphChassis);
					   let b2=portGroupModel.clone().attr("nodeProperties/identifier","sfp-cscc").position(card_x+7,card_y+cardHeight/2.5+8).addPort(portTx).addPort(portRx).addTo(graphChassis);
					   
					   let f1=portGroupModel.clone().attr("nodeProperties/identifier","sfp-cscc").position(card_x+7,card_y+cardHeight/1.8).addPort(portTx).addPort(portRx).addTo(graphChassis);
					   let f2=portGroupModel.clone().attr("nodeProperties/identifier","sfp-cscc").position(card_x+7,card_y+cardHeight/1.8+8).addPort(portTx).addPort(portRx).addTo(graphChassis);
					   let f3=portGroupModel.clone().attr("nodeProperties/identifier","sfp-cscc").position(card_x+7,card_y+cardHeight/1.8+16).addPort(portTx).addPort(portRx).addTo(graphChassis);
					   let f4=portGroupModel.clone().attr("nodeProperties/identifier","sfp-cscc").position(card_x+7,card_y+cardHeight/1.8+24).addPort(portTx).addPort(portRx).addTo(graphChassis);
					   let f5=portGroupModel.clone().attr("nodeProperties/identifier","sfp-cscc").position(card_x+7,card_y+cardHeight/1.8+32).addPort(portTx).addPort(portRx).addTo(graphChassis);
					   let f6=portGroupModel.clone().attr("nodeProperties/identifier","sfp-cscc").position(card_x+7,card_y+cardHeight/1.8+40).addPort(portTx).addPort(portRx).addTo(graphChassis);
					   card.embed(portEthernet1); card.embed(portEthernet2); card.embed(portEthernet3); card.embed(portEthernet4);
					   card.embed(portDebugMcp);card.embed(portDebugScp);
					   card.embed(b1);card.embed(b2);
					   card.embed(f1);card.embed(f2);card.embed(f3);card.embed(f4);card.embed(f5);card.embed(f6);
				   }
				   else if (cardType == "OSC"){
					   card=ModelCardOsc.clone().position(card_x,card_y);
					   card.attr('nodeProperties/typeName',cardType);
					   card.attr('label/text', cardType).position(card_x,card_y);
					   card.attr('nodeProperties/slotWidth',maxSlotPerCard);
					   card.attr('nodeProperties/rackId',rackId);
					   card.attr('nodeProperties/subRackId',subRackId);
					   card.attr('nodeProperties/slotId',slotId);
					   card.attr('nodeProperties/direction',direction);
					   card.attr('nodeProperties/lambda',"");
					   card.resize(ModelPlate.size().width*maxSlotPerCard,ModelPlate.size().height);
					   card.addTo(graphChassis);
					   let cardHeight=ModelPlate.size().height;
					   let portEthernet = portEthernetModel.clone().position(card_x+7,card_y+cardHeight/5).addTo(graphChassis);
					   
					   let portTx=JSON.parse(JSON.stringify(portSingleModel));
					   portTx.attrs.image.width="6px"; portTx.attrs.image.height="3px"
					   portTx.attrs.rect.width="6px"; portTx.attrs.rect.height="3px"
					   portTx.attrs.image["xlink:href"]="images/chassis/port_sfp_tx_on.jpg";
					   portTx.id='Tx';

					   let portRx=JSON.parse(JSON.stringify(portSingleModel));
					   portRx.attrs.image.width="6px"; portRx.attrs.image.height="3px"
					   portRx.attrs.rect.width="6px"; portRx.attrs.rect.height="3px"
					   portRx.attrs.image["xlink:href"]="images/chassis/port_sfp_rx_on.jpg";
					   portRx.id='Rx';
					   portRx.args.y=3;
					   let ports = card.attr('nodeProperties/ports');
					   portNames = ModelCardOsc.attr('nodeProperties/portNames');
					   for(k in ports){
						   let port;
						   portTx.attrs.nodeProperties.text=portNames[k]+"(Tx)";portRx.attrs.nodeProperties.text=portNames[k]+"(Rx)";
						   if(0==k)
							   ports[0]=portGroupModel.clone().attr("nodeProperties/portGroupNum",k).attr("nodeProperties/identifier","osc-sfp").resize(6,6).position(card_x+7,card_y+cardHeight/2.5-16).addPort(portTx).addPort(portRx).addTo(graphChassis);
						   else if(1==k)
							   ports[1]=portGroupModel.clone().attr("nodeProperties/portGroupNum",k).attr("nodeProperties/identifier","osc-sfp").resize(6,6).position(card_x+7,card_y+cardHeight/2.5-8).addPort(portTx).addPort(portRx).addTo(graphChassis);
						   else if(2==k)
							   ports[2]=portGroupModel.clone().attr("nodeProperties/portGroupNum",k).attr("nodeProperties/identifier","osc-sfp").resize(6,6).position(card_x+7,card_y+cardHeight/2.5).addPort(portTx).addPort(portRx).addTo(graphChassis);
						   else if(3==k)
							   ports[3]=portGroupModel.clone().attr("nodeProperties/portGroupNum",k).attr("nodeProperties/identifier","osc-sfp").resize(6,6).position(card_x+7,card_y+cardHeight/2.5+8).addPort(portTx).addPort(portRx).addTo(graphChassis);
						   else if(4==k)
							   ports[4]=portGroupModel.clone().attr("nodeProperties/portGroupNum",k).attr("nodeProperties/identifier","osc-sfp").resize(6,6).position(card_x+7,card_y+cardHeight/1.8).addPort(portTx).addPort(portRx).addTo(graphChassis);
						   else if(5==k)
							   ports[5]=portGroupModel.clone().attr("nodeProperties/portGroupNum",k).attr("nodeProperties/identifier","osc-sfp").resize(6,6).position(card_x+7,card_y+cardHeight/1.8+8).addPort(portTx).addPort(portRx).addTo(graphChassis);
						   else if(6==k)
							   ports[6]=portGroupModel.clone().attr("nodeProperties/portGroupNum",k).attr("nodeProperties/identifier","osc-sfp").resize(6,6).position(card_x+7,card_y+cardHeight/1.8+16).addPort(portTx).addPort(portRx).addTo(graphChassis);
						   else if(7==k)
							   ports[7]=portGroupModel.clone().attr("nodeProperties/portGroupNum",k).attr("nodeProperties/identifier","osc-sfp").resize(6,6).position(card_x+7,card_y+cardHeight/1.8+24).addPort(portTx).addPort(portRx).addTo(graphChassis);
						   card.embed(ports[k]);
					   }
					   let portDebug = portDebugModel.clone().position(card_x+7,card_y+cardHeight-32).addTo(graphChassis);
					   card.embed(portEthernet);
					   card.embed(portDebug);
				   }
				   else if (cardType == "OCM1x2"){
					   port_x = card_x+7;
					   port_y = card_y+(ModelPlate.size().height/6);
					   card=ModelCardOpm1x2.clone().position(card_x,card_y);
					   card.attr('nodeProperties/typeName',cardType);
					   card.attr('label/text', "OPM").position(card_x,card_y);
					   card.attr('nodeProperties/slotWidth',maxSlotPerCard);
					   card.attr('nodeProperties/rackId',rackId);
					   card.attr('nodeProperties/subRackId',subRackId);
					   card.attr('nodeProperties/slotId',slotId);
					   card.attr('nodeProperties/direction',"Both");
					   card.attr('nodeProperties/lambda',"");
					   card.resize(ModelPlate.size().width*maxSlotPerCard,ModelPlate.size().height);
					   card.addTo(graphChassis);
					   let portEthernet = portEthernetModel.clone().position(card_x+7,card_y+ModelPlate.size().height-46).addTo(graphChassis);
					   let portDebug = portDebugModel.clone().position(card_x+7,card_y+ModelPlate.size().height-36).addTo(graphChassis);
					   let ports = card.attr('nodeProperties/ports');
					   let portNames = card.attr('nodeProperties/portNames');
					   for(k in ports){
						   if(k<1){
							   let port=portImageModel.clone();
							   port.attr("nodeProperties/identifier","Port")
							   port.attr("image/xlink:href","images/chassis/blue_single_big_right.jpg");
							   port.attr("image/width","8px"); port.attr("image/height","8px");
							   port.attr("rect/width","8px"); port.attr("rect/height","8px");
							   port.attr("nodeProperties/text",portNames[k]);
							   ports[k]=port.resize(8,8).position(port_x,port_y);
							   ports[k].addTo(graphChassis);
							   card.embed(port);
							   port_y+=24;
						   }
						   else{
							   ports[k]=portGroupModel.clone().resize(7,12).position(port_x,port_y);
							   let port=JSON.parse(JSON.stringify(portSingleModel));
							   port.attrs.image.width="7px"; port.attrs.image.height="6px"
							   port.attrs.rect.width="7px"; port.attrs.rect.height="6px"
							   port.attrs.image["xlink:href"]="images/chassis/green_single_small_up.jpg";
							   port.args.x=0;
							   for(let l=1;l<=2;l++){
								   port.attrs.nodeProperties.text=portNames[k];
								   port.id=portNames[k]+" "+l;
								   ports[k].addPort(port);
								   port.args.y+=6;
							   }
							   port_y+=18;
							   ports[k].addTo(graphChassis);
							   card.embed(ports[k])
						   }
					   };
					   card.embed(portEthernet);
					   card.embed(portDebug);
				   }
				   else if (cardType == "OCM1x8"){
					   portNames = ModelCardOpm1x8.attr('nodeProperties/portNames');
					   port_x = card_x+7;
					   port_y = card_y+(ModelPlate.size().height/4);
					   card=ModelCardOpm1x8.clone().position(card_x,card_y);
					   card.attr('nodeProperties/typeName',cardType);
					   card.attr('label/text', "OPM").position(card_x,card_y);
					   card.attr('nodeProperties/slotWidth',maxSlotPerCard);
					   card.attr('nodeProperties/rackId',rackId);
					   card.attr('nodeProperties/subRackId',subRackId);
					   card.attr('nodeProperties/slotId',slotId);
					   card.attr('nodeProperties/direction',"Both");
					   card.attr('nodeProperties/lambda',"");
					   card.resize(ModelPlate.size().width*maxSlotPerCard,ModelPlate.size().height);
					   card.addTo(graphChassis);
					   let portEthernet = portEthernetModel.clone().position(card_x+7,card_y+ModelPlate.size().height-32).addTo(graphChassis);
					   let portDebug = portDebugModel.clone().position(card_x+7,card_y+ModelPlate.size().height-20).addTo(graphChassis);
					   let ports = card.attr('nodeProperties/ports');
					   for(k in ports){
						   var port;
						   portUp.attrs.nodeProperties.text=portNames[2*k];
						   portDown.attrs.nodeProperties.text=portNames[2*k+1];
						   port=portGroup.clone().resize(7,14).position(port_x,port_y).addPort(portDown).addPort(portUp).addTo(graphChassis);
						   ports[k]=port;
						   port_y+=20;
						   card.embed(port);
					   };
					   card.embed(portEthernet);
					   card.embed(portDebug);
				   }
				   else if (cardType == "OCM1x16"){
					   port_x = card_x+7;
					   port_y = card_y+(ModelPlate.size().height/6);
					   card=ModelCardOpm1x16.clone().position(card_x,card_y);
					   card.attr('nodeProperties/typeName',cardType);
					   card.attr('label/text', "OPM").position(card_x,card_y);
					   card.attr('nodeProperties/slotWidth',maxSlotPerCard);
					   card.attr('nodeProperties/rackId',rackId);
					   card.attr('nodeProperties/subRackId',subRackId);
					   card.attr('nodeProperties/slotId',slotId);
					   card.attr('nodeProperties/direction',"Both");
					   card.attr('nodeProperties/lambda',"");
					   card.resize(ModelPlate.size().width*maxSlotPerCard,ModelPlate.size().height);
					   card.addTo(graphChassis);
					   let portEthernet = portEthernetModel.clone().position(card_x+7,card_y+ModelPlate.size().height-46).addTo(graphChassis);
					   let portDebug = portDebugModel.clone().position(card_x+7,card_y+ModelPlate.size().height-36).addTo(graphChassis);
					   let ports = card.attr('nodeProperties/ports');
					   let portNames = card.attr('nodeProperties/portNames');
					   for(k in ports){
						   if(k<2){
							   let port=portImageModel.clone();
							   port.attr("nodeProperties/identifier","Port")
							   port.attr("image/xlink:href","images/chassis/blue_single_big_right.jpg");
							   port.attr("image/width","8px"); port.attr("image/height","8px");
							   port.attr("rect/width","8px"); port.attr("rect/height","8px");
							   port.attr("nodeProperties/text",portNames[k]);
							   ports[k]=port.resize(8,8).position(port_x,port_y);
							   ports[k].addTo(graphChassis);
							   card.embed(port);
							   port_y+=14;
						   }
						   else{
							   ports[k]=portGroupModel.clone().resize(7,16).position(port_x,port_y);
							   let port=JSON.parse(JSON.stringify(portSingleModel));
							   port.attrs.image.width="7px"; port.attrs.image.height="4px"
							   port.attrs.rect.width="7px"; port.attrs.rect.height="4px"
							   port.attrs.image["xlink:href"]="images/chassis/green_single_small_up.jpg";
							   port.args.x=0;
							   for(let l=1;l<=4;l++){
								   port.attrs.nodeProperties.text=portNames[k];
								   port.id=portNames[k]+" "+l;
								   ports[k].addPort(port);
								   port.args.y+=4;
							   }
							   if(k<4)
							   port_y+=18;
							   else
							   port_y+=38;
							   ports[k].addTo(graphChassis);
							   card.embed(ports[k])
						   }
					   };
					   card.embed(portEthernet);
					   card.embed(portDebug);
				   }
				   else if (cardType == "WSS1x2"){
					   portNames = ModelCardWss1x2.attr('nodeProperties/portNames');
					   port_x = card_x+7;
					   port_y = card_y+(ModelPlate.size().height/4);
					   card=ModelCardWss1x2.clone().position(card_x,card_y)
					   card.attr('nodeProperties/typeName',cardType);
					   card.attr('label/text', "WSS").position(card_x,card_y);
					   card.attr('nodeProperties/slotWidth',maxSlotPerCard);
					   card.attr('nodeProperties/rackId',rackId);
					   card.attr('nodeProperties/subRackId',subRackId);
					   card.attr('nodeProperties/slotId',slotId);
					   card.attr('nodeProperties/direction',direction);
					   card.attr('nodeProperties/lambda',"");
					   card.resize(ModelPlate.size().width*maxSlotPerCard,ModelPlate.size().height);
					   card.addTo(graphChassis);
					   let portEthernet = portEthernetModel.clone().position(card_x+7,card_y+ModelPlate.size().height-32).addTo(graphChassis);
					   let portDebug = portDebugModel.clone().position(card_x+7,card_y+ModelPlate.size().height-20).addTo(graphChassis);
					   let ports = card.attr('nodeProperties/ports');
					   for(k in ports){
						   var port;
						   portUp.attrs.nodeProperties.text=portNames[2*k];
						   portDown.attrs.nodeProperties.text=portNames[2*k+1];
						   port=portGroup.clone().resize(7,14).position(port_x,port_y).addPort(portDown).addPort(portUp).addTo(graphChassis);
						   ports[k]=port;
						   port_y+=20;
						   card.embed(port);
					   };
					   card.embed(portEthernet);
					   card.embed(portDebug);
				   }
				   else if (cardType == "WSS2x1x9"){
					   portNames = ModelCardWss219.attr('nodeProperties/portNames');
					   port_x = card_x+7;
					   port_y = card_y+(ModelPlate.size().height/4);
					   card=ModelCardWss219.clone().position(card_x,card_y)
					   card.attr('nodeProperties/typeName',cardType);
					   card.attr('label/text', "WSS").position(card_x,card_y);
					   card.attr('nodeProperties/slotWidth',maxSlotPerCard);
					   card.attr('nodeProperties/rackId',rackId);
					   card.attr('nodeProperties/subRackId',subRackId);
					   card.attr('nodeProperties/slotId',slotId);
					   card.attr('nodeProperties/direction',direction);
					   card.attr('nodeProperties/lambda',"");
					   card.resize(ModelPlate.size().width*maxSlotPerCard,ModelPlate.size().height);
					   card.addTo(graphChassis);
					   let portEthernet = portEthernetModel.clone().position(card_x+7,card_y+ModelPlate.size().height-32).addTo(graphChassis);
					   let portDebug = portDebugModel.clone().position(card_x+7,card_y+ModelPlate.size().height-20).addTo(graphChassis);
					   let portGroupArray = card.attr('nodeProperties/ports');
					   for(k in portGroupArray){
						   if(k!=2 && k!=5)
						   {
							   portGroupArray[k]=portGroupModel.clone().resize(28,7).position(port_x,port_y);
							   let port=JSON.parse(JSON.stringify(portSingleModel));
							   port.attrs.image.width="7px"; port.attrs.image.height="7px"
							   port.attrs.rect.width="7px"; port.attrs.rect.height="7px"
							   port.attrs.image["xlink:href"]="images/chassis/blue_single.jpg";
							   port.args.x=0;
							   for(let l=1;l<=4;l++){
								   port.attrs.nodeProperties.text=portNames[k];
								   port.id=portNames[k]+" "+l;
								   portGroupArray[k].addPort(port);
								   port.args.x+=7;
							   }
							   portGroupArray[k].addTo(graphChassis);
						   }
						   else
						   {
							   let port=portImageModel.clone();
							   port.attr("image/xlink:href","images/chassis/blue_single_big_up.jpg");
							   port.attr("image/width","10px"); port.attr("image/height","10px");
							   port.attr("rect/width","10px"); port.attr("rect/height","10px");
							   port.attr("text/text",portNames[k]);
							   portGroupArray[k]=port.resize(10,10).position(card_x+(card.size().width/2.2),port_y);
							   portGroupArray[k].addTo(graphChassis);
						   }
						   if(k==2)
							   port_y+=16;
						   else
							   port_y+=14;
					   
						   card.embed(portGroupArray[k]);
					   };
					   card.embed(portEthernet);
					   card.embed(portDebug);
				   }
				   else if (cardType == "WSS2x1x20"){
					   portNames = ModelCardWss2120.attr('nodeProperties/portNames');
					   port_x = card_x+7;
					   port_y = card_y+(ModelPlate.size().height/6);
					   card=ModelCardWss2120.clone().position(card_x,card_y)
					   card.attr('nodeProperties/typeName',cardType);
					   card.attr('label/text', "WSS").position(card_x,card_y);
					   card.attr('nodeProperties/slotWidth',maxSlotPerCard);
					   card.attr('nodeProperties/rackId',rackId);
					   card.attr('nodeProperties/subRackId',subRackId);
					   card.attr('nodeProperties/slotId',slotId);
					   card.attr('nodeProperties/direction',direction);
					   card.attr('nodeProperties/lambda',"");
					   card.resize(ModelPlate.size().width*maxSlotPerCard,ModelPlate.size().height);
					   card.addTo(graphChassis);
					   let portEthernet = portEthernetModel.clone().position(card_x+7,card_y+ModelPlate.size().height-32).addTo(graphChassis);
					   let portDebug = portDebugModel.clone().position(card_x+7,card_y+ModelPlate.size().height-20).addTo(graphChassis);
					   let portGroupArray = card.attr('nodeProperties/ports');
					   for(k in portGroupArray){
						   if(k<10)
						   {
							   portGroupArray[k]=portGroupModel.clone().resize(28,7).position(port_x,port_y);
							   let port=JSON.parse(JSON.stringify(portSingleModel));
							   port.attrs.image.width="7px"; port.attrs.image.height="7px"
							   port.attrs.rect.width="7px"; port.attrs.rect.height="7px"
							   port.attrs.image["xlink:href"]="images/chassis/blue_single.jpg";
							   port.args.x=0;
							   for(let l=1;l<=4;l++){
								   port.attrs.nodeProperties.text=portNames[k];
								   port.id=portNames[k]+" "+l;
								   portGroupArray[k].addPort(port);
								   port.args.x+=7;
							   }
							   portGroupArray[k].addTo(graphChassis);
						   }
						   else
						   {  
								let portTx=JSON.parse(JSON.stringify(portSingleModel));
								portTx.attrs.image.width="3px"; portTx.attrs.image.height="5px"
								portTx.attrs.rect.width="3px"; portTx.attrs.rect.height="5px"
								portTx.attrs.image["xlink:href"]="images/chassis/blue_single_left.jpg";
								portTx.id='WTC';
								portTx.attrs.nodeProperties.text=portNames[k];

								let portRx=JSON.parse(JSON.stringify(portSingleModel));
								portRx.attrs.image.width="3px"; portRx.attrs.image.height="5px"
								portRx.attrs.rect.width="3px"; portRx.attrs.rect.height="5px"
								portRx.attrs.image["xlink:href"]="images/chassis/blue_single_right.jpg";
								portRx.id='WRC';
								portRx.args.x=3;
								portTx.attrs.nodeProperties.text=portNames[k];
								portGroupArray[k]=portGroupModel.clone().attr("nodeProperties/identifier","portGroupModel").position(port_x,port_y).resize(6,5).addPort(portTx).addPort(portRx);
								portGroupArray[k].addTo(graphChassis);
						   }
						   if(k<9){
							   if(k==4)
								   port_y+=12;
							   else
								   port_y+=9;
						   }
						   else{
							   port_y+=10;
							   port_x+=20;
						   }
						   
						   card.embed(portGroupArray[k]);
					   };
					   card.embed(portEthernet);
					   card.embed(portDebug);
				   }
				   else if (cardType == "WSS8x12"){
					   portNames = ModelCardWss812.attr('nodeProperties/portNames');
					   port_x = card_x+7;
					   port_y = card_y+(ModelPlate.size().height/6);
					   card=ModelCardWss812.clone().position(card_x,card_y);
					   card.attr('nodeProperties/typeName',cardType);
					   card.attr('label/text', "WSS").position(card_x,card_y);
					   card.attr('nodeProperties/slotWidth',maxSlotPerCard);
					   card.attr('nodeProperties/rackId',rackId);
					   card.attr('nodeProperties/subRackId',subRackId);
					   card.attr('nodeProperties/slotId',slotId);
					   card.attr('nodeProperties/direction',direction);
					   card.attr('nodeProperties/lambda',"");
					   card.resize(ModelPlate.size().width*maxSlotPerCard,ModelPlate.size().height);
					   card.addTo(graphChassis);
					   let portEthernet = portEthernetModel.clone().position(card_x+7,card_y+ModelPlate.size().height-32).addTo(graphChassis);
					   let portDebug = portDebugModel.clone().position(card_x+7,card_y+ModelPlate.size().height-20).addTo(graphChassis);
					   let portGroupArray = card.attr('nodeProperties/ports');
					   for(k in portGroupArray){
						   portGroupArray[k]=portGroupModel.clone().resize(28,7).position(port_x,port_y);
						   let port=JSON.parse(JSON.stringify(portSingleModel));
						   port.attrs.image.width="7px"; port.attrs.image.height="7px"
						   port.attrs.rect.width="7px"; port.attrs.rect.height="7px"
						   port.attrs.image["xlink:href"]="images/chassis/blue_single.jpg";
						   port.args.x=0;
						   for(let l=1;l<=4;l++){
							   port.attrs.nodeProperties.text=portNames[k];
							   port.id=portNames[k]+" "+l;
							   portGroupArray[k].addPort(port);
							   port.args.x+=7;
						   }
						   portGroupArray[k].addTo(graphChassis);

						   port_y+=9.5;
						   
						   card.embed(portGroupArray[k]);
					   };
					   card.embed(portEthernet);
					   card.embed(portDebug);
				   
				   }
				   else if (cardType == "OLP"){
					   portNames = ModelCardOlp.attr('nodeProperties/portNames');
					   port_x = card_x+7;
					   port_y = card_y+(ModelPlate.size().height/4);
					   card=ModelCardOlp.clone().position(card_x,card_y)
					   card.attr('nodeProperties/typeName',cardType);
					   card.attr('label/text', cardType).position(card_x,card_y);
					   card.attr('nodeProperties/slotWidth',maxSlotPerCard);
					   card.attr('nodeProperties/rackId',rackId);
					   card.attr('nodeProperties/subRackId',subRackId);
					   card.attr('nodeProperties/slotId',slotId);
					   card.attr('nodeProperties/direction',direction);
					   card.attr('nodeProperties/lambda',"");
					   card.resize(ModelPlate.size().width*maxSlotPerCard,ModelPlate.size().height);
					   card.addTo(graphChassis);
					   let portEthernet = portEthernetModel.clone().position(card_x+7,card_y+ModelPlate.size().height-32).addTo(graphChassis);
					   let portDebug = portDebugModel.clone().position(card_x+7,card_y+ModelPlate.size().height-20).addTo(graphChassis);
					   let ports = card.attr('nodeProperties/ports');
					   for(k in ports){
						   var port;
						   portUp.attrs.nodeProperties.text=portNames[2*k];
						   portDown.attrs.nodeProperties.text=portNames[2*k+1];
						   port=portGroup.clone().resize(7,14).position(port_x,port_y).addPort(portDown).addPort(portUp).addTo(graphChassis);
						   ports[k]=port;
						   port_y+=20;
						   card.embed(port);
					   };
					   card.embed(portEthernet);
					   card.embed(portDebug);
				   }
				   else if (cardType == "EDFA ARRAY"){
					   portNames = ModelCardEdary.attr('nodeProperties/portNames');
					   port_x = card_x+7;
					   port_y = card_y+(ModelPlate.size().height/4);
					   card=ModelCardEdary.clone().position(card_x,card_y)
					   card.attr('nodeProperties/typeName',cardType);
					   card.attr('label/text', "EDFA").position(card_x,card_y);
					   card.attr('nodeProperties/slotWidth',maxSlotPerCard);
					   card.attr('nodeProperties/rackId',rackId);
					   card.attr('nodeProperties/subRackId',subRackId);
					   card.attr('nodeProperties/slotId',slotId);
					   card.attr('nodeProperties/direction',direction);
					   card.attr('nodeProperties/lambda',"");
					   card.resize(ModelPlate.size().width*maxSlotPerCard,ModelPlate.size().height);
					   card.addTo(graphChassis);
					   let portEthernet = portEthernetModel.clone().position(card_x+7,card_y+ModelPlate.size().height-32).addTo(graphChassis);
					   let portDebug = portDebugModel.clone().position(card_x+7,card_y+ModelPlate.size().height-20).addTo(graphChassis);
					   let portGroupArray = card.attr('nodeProperties/ports');
					   for(k in portGroupArray){
						   portGroupArray[k]=portGroupModel.clone().resize(28,7).position(port_x,port_y);
						   let port=JSON.parse(JSON.stringify(portSingleModel));
						   port.attrs.image.width="7px"; port.attrs.image.height="7px"
						   port.attrs.rect.width="7px"; port.attrs.rect.height="7px"
						   port.attrs.image["xlink:href"]="images/chassis/blue_single.jpg";
						   port.args.x=0;
						   for(let l=1;l<=4;l++){
							   port.attrs.nodeProperties.text=portNames[k];
							   port.id=portNames[k]+" "+l;
							   portGroupArray[k].addPort(port);
							   port.args.x+=7;
						   }
						   portGroupArray[k].addTo(graphChassis);
						   port_y+=20;
						   card.embed(portGroupArray[k]);
					   };
					   card.embed(portEthernet);
					   card.embed(portDebug);
				   }
				   else if(cardType == "OTDR 1X4" || cardType=="OTDR 1X16"){
					   portNames = ModelCardOtdr1x4.attr('nodeProperties/portNames');
					   port_x = card_x+17;
					   port_y = card_y+(ModelPlate.size().height/4);
					   card=ModelCardOtdr1x4.clone().position(card_x,card_y)
					   card.attr('nodeProperties/typeName',cardType);
					   card.attr('label/text', "OTDR").position(card_x,card_y);
					   card.attr('nodeProperties/slotWidth',maxSlotPerCard);
					   card.attr('nodeProperties/rackId',rackId);
					   card.attr('nodeProperties/subRackId',subRackId);
					   card.attr('nodeProperties/slotId',slotId);
					   card.attr('nodeProperties/direction',direction);
					   card.attr('nodeProperties/lambda',"");
					   card.resize(ModelPlate.size().width*maxSlotPerCard,ModelPlate.size().height);
					   card.addTo(graphChassis);
					   let portEthernet = portEthernetModel.clone().position(card_x+7,card_y+ModelPlate.size().height-32).addTo(graphChassis);
					   let portDebug = portDebugModel.clone().position(card_x+7,card_y+ModelPlate.size().height-20).addTo(graphChassis);
					   let portGroupArray = card.attr('nodeProperties/ports');
					   
					   for(k in portGroupArray){
						   portGroupArray[k]=portGroupModel.clone().resize(7,28).position(port_x,port_y);
						   let port=JSON.parse(JSON.stringify(portSingleModel));
						   port.attrs.image.width="7px"; port.attrs.image.height="7px";
						   port.attrs.rect.width="7px"; port.attrs.rect.height="7px";
						   port.attrs.image["xlink:href"]="images/chassis/otdr.jpg";
						   port.args.x=0;
						   if(0==k){
							   for(let l=1;l<=4;l++){
								   port.attrs.nodeProperties.text=portNames[k];
								   port.id=portNames[k]+" "+l;
								   portGroupArray[k].addPort(port);
								   port.args.y+=7;
							   }
							   portGroupArray[k].addTo(graphChassis);
							   port_y+=36;
							   card.embed(portGroupArray[k]);
						   }
						   else
						   {
							   let port;
							   portUp.attrs.nodeProperties.text=portNames[(k==1?k:k*1+1)];
							   portDown.attrs.nodeProperties.text=portNames[(k==1?k*1+1:k*1+1+1)];
							   port=portGroup.clone().resize(7,14).position(port_x,port_y).addPort(portDown).addPort(portUp).addTo(graphChassis);
							   portGroupArray[k]=port;
							   port_y+=20;
						   }
					   };
					   card.embed(portEthernet);
					   card.embed(portDebug);
				   }
				   else if (cardType == "MCS"){
					   portNames = ModelCardMcs.attr('nodeProperties/portNames');
					   port_x = card_x+7;
					   port_y = card_y+(ModelPlate.size().height/5.5);
					   card=ModelCardMcs.clone().position(card_x,card_y)
					   card.attr('nodeProperties/typeName',cardType);
					   card.attr('label/text', cardType).position(card_x,card_y);
					   card.attr('nodeProperties/slotWidth',maxSlotPerCard);
					   card.attr('nodeProperties/rackId',rackId);
					   card.attr('nodeProperties/subRackId',subRackId);
					   card.attr('nodeProperties/slotId',slotId);
					   card.attr('nodeProperties/direction',direction);
					   card.attr('nodeProperties/lambda',"");
					   card.resize(ModelPlate.size().width*maxSlotPerCard,ModelPlate.size().height);
					   card.addTo(graphChassis);
					   let portEthernet = portEthernetModel.clone().position(card_x+7,card_y+ModelPlate.size().height-48).addTo(graphChassis);
					   let portDebug = portDebugModel.clone().position(card_x+7,card_y+ModelPlate.size().height-38).addTo(graphChassis);
					   let portGroupArray = card.attr('nodeProperties/ports');
					   for(k in portGroupArray){
						   portGroupArray[k]=portGroupModel.clone().resize(28,4).position(port_x,port_y);
						   let port=JSON.parse(JSON.stringify(portSingleModel));
						   port.attrs.image.width="7px"; port.attrs.image.height="4px";
						   port.attrs.rect.width="7px"; port.attrs.rect.height="4px";
						   port.attrs.image["xlink:href"]="images/chassis/blue_single.jpg";
						   port.args.x=0; 
						   for(let l=1;l<=4;l++){
							   port.attrs.nodeProperties.text=portNames[k];
							   port.id=portNames[k]+" "+l;
							   portGroupArray[k].addPort(port);
							   port.args.x+=7;
						   }
						   portGroupArray[k].addTo(graphChassis);
						   if(k!=9)
							   port_y+=8;
						   else
							   port_y+=24;
						   
						   card.embed(portGroupArray[k]);
					   };
					   card.embed(portEthernet);
					   card.embed(portDebug);
				   }
				   else if (cardType == "MPN 200G"){
					   card=ModelCard200gMux.clone();
					   /*set Attributes*/
					   card.attr('nodeProperties/typeName',cardType);
					   card.attr('label/text', "MPN").position(card_x,card_y);
					   card.attr('nodeProperties/slotWidth',maxSlotPerCard);
					   card.attr('nodeProperties/rackId',rackId);
					   card.attr('nodeProperties/subRackId',subRackId);
					   card.attr('nodeProperties/slotId',slotId);
					   card.attr('nodeProperties/direction',direction);
					   let lambda=dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].subRackList[i].cardList[j].LineLambda;
					   card.attr('nodeProperties/lambda',lambda);
					   card.resize(ModelPlate.size().width*maxSlotPerCard,ModelPlate.size().height);
					   portList=dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].subRackList[i].cardList[j].portList;
					   let ports = card.attr('nodeProperties/ports');
					   for(let k=1;k<=20; k++){
						   ports[k]=((typeof portList[k-1]==="undefined")?"0":portList[k-1]);
					   }
					   /*start Drawing*/
					   card.addTo(graphChassis);
					   
					   port_x = card_x+7+6;
					   port_y = card_y+ModelPlate.size().height/8;
					   let portup = JSON.parse(JSON.stringify(portUp));
					   let portdown = JSON.parse(JSON.stringify(portDown));

					   portup.attrs.image.width="4px";	portup.attrs.image.height="4px";
					   portup.attrs.rect.width="4px"; portup.attrs.rect.height="4px";
					   portdown.attrs.image.width="4px"; portdown.attrs.image.height="4px";
					   portdown.attrs.rect.width="4px"; portdown.attrs.rect.height="4px";

					   portup.attrs.nodeProperties.text="Tx Secondary";
					   portdown.attrs.nodeProperties.text="Rx Secondary";
					   let port_stdBy=portGroup.clone().resize(4,8).position(port_x+12,port_y+10).addPort(portdown).addPort(portup).addTo(graphChassis);
					   
					   port_y = card_y+ModelPlate.size().height/8+9;
					   portup.attrs.nodeProperties.text="Tx Primary";
					   portdown.attrs.nodeProperties.text="Rx Primary";
					   let port_act=portGroup.clone().resize(4,8).position(port_x+12,port_y+10).addPort(portdown).addPort(portup).addTo(graphChassis);
					   
					   let portDebug = portDebugModel.clone().position(card_x+10,card_y+ModelPlate.size().height/5).addTo(graphChassis);
					   
					   for(let k=1;k<=20;k++){
						   let portTx=JSON.parse(JSON.stringify(portSingleModel));
						   portTx.attrs.image.width="6px"; portTx.attrs.image.height="3px"
						   portTx.attrs.rect.width="6px"; portTx.attrs.rect.height="3px"
						   portTx.attrs.image["xlink:href"]="images/chassis/port_sfp_tx.jpg";
						   portTx.id='Tx';

						   let portRx=JSON.parse(JSON.stringify(portSingleModel));
						   portRx.attrs.image.width="6px"; portRx.attrs.image.height="3px"
						   portRx.attrs.rect.width="6px"; portRx.attrs.rect.height="3px"
						   portRx.attrs.image["xlink:href"]="images/chassis/port_sfp_rx.jpg";
						   portRx.id='Rx';
						   portRx.args.y=3;
						   let port;
						   if(k<10){
							   if(k%2!=0)
							   {
								   port_x = card_x+10+6;
								   port_y = card_y+ModelPlate.size().height/3.3+((k-1)/2)*6;
							   }
							   else
							   {
								   port_x = card_x+10;
							   }
						   }
						   else{
							   if(k%2!=0)
							   {
								   port_x = card_x+10+6;
								   port_y = card_y+ModelPlate.size().height/2.5+((k-1)/2)*6;
							   }
							   else
							   {
								   port_x = card_x+10;
							   }
						   }
						   if(ports[k]!=0){
							   portTx.attrs.image["xlink:href"]="images/chassis/port_sfp_tx_on.jpg";
							   portRx.attrs.image["xlink:href"]="images/chassis/port_sfp_rx_on.jpg";
							   port=portGroupModel.clone().attr("nodeProperties/portGroupNum",k).attr("nodeProperties/identifier","sfp").position(port_x,port_y).addPort(portTx).addPort(portRx);
							   port.attr("nodeProperties/text","SFP-"+k+" On");
							   port.addTo(graphChassis);
						   }
						   else{
							   port=portGroupModel.clone().attr("nodeProperties/portGroupNum",k).attr("nodeProperties/identifier","sfp").position(port_x,port_y).addPort(portTx).addPort(portRx);
							   port.attr("nodeProperties/text","SFP-"+k+" Off");
							   port.addTo(graphChassis);
						   }
						   card.embed(port);
					   }
					   ports[21]=port_act;
					   ports[22]=port_stdBy;
					   card.embed(portDebug);
					   card.embed(port_act);
					   card.embed(port_stdBy);
				   }
				   else if (cardType == "MPN(CGM)" || cardType == "MPN OPX(CGM)"){
					   card=ModelCardMpnCgm.clone();
					   /*set Attributes*/
					   card.attr('nodeProperties/typeName',"CGM");
					   card.attr('label/text', "CGM").position(card_x,card_y);
					   card.attr('nodeProperties/slotWidth',maxSlotPerCard);
					   card.attr('nodeProperties/rackId',rackId);
					   card.attr('nodeProperties/subRackId',subRackId);
					   card.attr('nodeProperties/slotId',slotId);
					   card.attr('nodeProperties/direction',direction);
					   let lambda=dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].subRackList[i].cardList[j].LineLambda;
					   card.attr('nodeProperties/lambda',lambda);
					   card.resize(ModelPlate.size().width*maxSlotPerCard,ModelPlate.size().height);
					   portList=dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].subRackList[i].cardList[j].portList;
					   let ports = card.attr('nodeProperties/ports');
					   for(let k=1;k<=10; k++){
						   ports[k]=((typeof portList[k-1]==="undefined")?"0":portList[k-1]);
					   }
					   /*start Drawing*/
					   card.addTo(graphChassis);
					   let portEthernet = portEthernetModel.clone().position(card_x+7,card_y+ModelPlate.size().height/2-16).addTo(graphChassis);
					   let portDebug = portDebugModel.clone().position(card_x+7,card_y+ModelPlate.size().height/2+10-16).addTo(graphChassis);
					   for(let k=1;k<=10;k++){
						   let portTx=JSON.parse(JSON.stringify(portSingleModel));
						   portTx.attrs.image.width="6px"; portTx.attrs.image.height="3px"
						   portTx.attrs.rect.width="6px"; portTx.attrs.rect.height="3px"
						   portTx.attrs.image["xlink:href"]="images/chassis/port_sfp_tx.jpg";
						   portTx.id='Tx';

						   let portRx=JSON.parse(JSON.stringify(portSingleModel));
						   portRx.attrs.image.width="6px"; portRx.attrs.image.height="3px"
						   portRx.attrs.rect.width="6px"; portRx.attrs.rect.height="3px"
						   portRx.attrs.image["xlink:href"]="images/chassis/port_sfp_rx.jpg";
						   portRx.id='Rx';
						   portRx.args.y=3;
						   let port;
						   if(k%2!=0)
						   {
							   port_x = card_x+7+6;
							   port_y = card_y+ModelPlate.size().height/2+8+((k-1)/2)*6;
						   }
						   else
						   {
							   port_x = card_x+7;
						   }
						   if(ports[k]!=0){
							   portTx.attrs.image["xlink:href"]="images/chassis/port_sfp_tx_on.jpg";
							   portRx.attrs.image["xlink:href"]="images/chassis/port_sfp_rx_on.jpg";
							   port=portGroupModel.clone().attr("nodeProperties/portGroupNum",k).attr("nodeProperties/identifier","sfp").position(port_x,port_y).addPort(portTx).addPort(portRx);
							   port.attr("nodeProperties/text","SFP-"+k+" On");
							   port.addTo(graphChassis);
						   }
						   else{
							   port=portGroupModel.clone().attr("nodeProperties/portGroupNum",k).attr("nodeProperties/identifier","sfp").position(port_x,port_y).addPort(portTx).addPort(portRx);
							   port.attr("nodeProperties/text","SFP-"+k+" Off");
							   port.addTo(graphChassis);
						   }
						   card.embed(port);
					   }
					   let portup = JSON.parse(JSON.stringify(portUp));
					   let portdown = JSON.parse(JSON.stringify(portDown));

					   portup.attrs.image.width="6px";	portup.attrs.image.height="6px";
					   portup.attrs.rect.width="6px"; portup.attrs.rect.height="6px";
					   portdown.attrs.image.width="6px"; portdown.attrs.image.height="6px";
					   portdown.attrs.rect.width="6px"; portdown.attrs.rect.height="6px";

					   portup.attrs.nodeProperties.text="Tx Secondary"+(cardType.includes("OPX")?"":("(N/A)"));
					   portdown.attrs.nodeProperties.text="Rx Secondary"+(cardType.includes("OPX")?"":("(N/A)"));
					   let port_stdBy=portGroup.clone().resize(6,12).position(port_x+12,port_y+10).addPort(portdown).addPort(portup).addTo(graphChassis);
					   portup.attrs.nodeProperties.text="Tx Primary";
					   portdown.attrs.nodeProperties.text="Rx Primary";
					   let port_act=portGroup.clone().resize(6,12).position(port_x+22,port_y+10).addPort(portdown).addPort(portup).addTo(graphChassis);
					   ports[11]=port_act;
					   ports[12]=port_stdBy;
					   card.embed(portEthernet);
					   card.embed(portDebug);
					   card.embed(port_act);
					   card.embed(port_stdBy);
					   
				   }/*
				   else if (cardType == "MPN(CGM)" || cardType == "MPN OPX(CGM)"){
					   card=ModelCardMpnCgmHorizontal.clone();
					   set Attributes
					   card.attr('nodeProperties/typeName',"CGM");
					   card.attr('label/text', "CGM").position(card_x,card_y);
					   card.attr('nodeProperties/slotWidth',maxSlotPerCard);
					   card.attr('nodeProperties/rackId',rackId);
					   card.attr('nodeProperties/subRackId',subRackId);
					   card.attr('nodeProperties/slotId',slotId);
					   card.attr('nodeProperties/direction',direction);
					   let lambda=dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].subRackList[i].cardList[j].LineLambda;
					   card.attr('nodeProperties/lambda',lambda);
					   card.resize(ModelPlateHorizontal.size().width,ModelPlateHorizontal.size().height*maxSlotPerCard);
					   portList=dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].subRackList[i].cardList[j].portList;
					   let ports = card.attr('nodeProperties/ports');
					   for(let k=1;k<=10; k++){
						   ports[k]=((typeof portList[k-1]==="undefined")?"0":portList[k-1]);
					   }
					   start Drawing
					   card.addTo(graphChassis);
//					   let portEthernet = portEthernetModel.clone().position(card_x+7,card_y+ModelPlate.size().height/2-16).addTo(graphChassis);
//					   let portDebug = portDebugModel.clone().position(card_x+7,card_y+ModelPlate.size().height/2+10-16).addTo(graphChassis);
					   for(let k=1;k<=10;k++){
						   let portTx=JSON.parse(JSON.stringify(portSingleModel));
						   portTx.attrs.image.width="8px"; portTx.attrs.image.height="4px"
						   portTx.attrs.rect.width="8px"; portTx.attrs.rect.height="4px"
						   portTx.attrs.image["xlink:href"]="images/chassis/port_sfp_tx.jpg";
						   portTx.id='Tx';

						   let portRx=JSON.parse(JSON.stringify(portSingleModel));
						   portRx.attrs.image.width="8px"; portRx.attrs.image.height="4px"
						   portRx.attrs.rect.width="8px"; portRx.attrs.rect.height="4px"
						   portRx.attrs.image["xlink:href"]="images/chassis/port_sfp_rx.jpg";
						   portRx.id='Rx';
						   portRx.args.y=4;
						   let port;
						   if(k%2!=0)
						   {
							   port_y = card_y+ModelPlateHorizontal.size().height*maxSlotPerCard-(14+8);
							   port_x = card_x+ModelPlateHorizontal.size().width/1.5+((k-1)/2)*8;
						   }
						   else
						   {
							   port_y = card_y+ModelPlateHorizontal.size().height*maxSlotPerCard-14;
						   }
						   if(ports[k]!=0){
							   portTx.attrs.image["xlink:href"]="images/chassis/port_sfp_tx_on.jpg";
							   portRx.attrs.image["xlink:href"]="images/chassis/port_sfp_rx_on.jpg";
							   port=portGroupModel.clone().resize(8,8).attr("nodeProperties/portGroupNum",k).attr("nodeProperties/identifier","sfp").position(port_x,port_y).addPort(portTx).addPort(portRx);
							   port.attr("nodeProperties/text","SFP-"+k+" On");
							   port.addTo(graphChassis);
							   port.rotate(-90,true)
						   }
						   else{
							   port=portGroupModel.clone().resize(8,8).attr("nodeProperties/portGroupNum",k).attr("nodeProperties/identifier","sfp").position(port_x,port_y).addPort(portTx).addPort(portRx);
							   port.attr("nodeProperties/text","SFP-"+k+" Off");
							   port.addTo(graphChassis);
							   port.rotate(-90,true)
						   }
						   card.embed(port);
					   }
					   let portup = JSON.parse(JSON.stringify(portUp));
					   let portdown = JSON.parse(JSON.stringify(portDown));

					   portup.attrs.image.width="6px";	portup.attrs.image.height="6px";
					   portup.attrs.rect.width="6px"; portup.attrs.rect.height="6px";
					   portdown.attrs.image.width="6px"; portdown.attrs.image.height="6px";
					   portdown.attrs.rect.width="6px"; portdown.attrs.rect.height="6px";

					   portup.attrs.nodeProperties.text="Tx Secondary"+(cardType.includes("OPX")?"":("(N/A)"));
					   portdown.attrs.nodeProperties.text="Rx Secondary"+(cardType.includes("OPX")?"":("(N/A)"));
//					   let port_stdBy=portGroup.clone().resize(6,12).position(port_x+12,port_y+10).addPort(portdown).addPort(portup).addTo(graphChassis);
					   portup.attrs.nodeProperties.text="Tx Primary";
					   portdown.attrs.nodeProperties.text="Rx Primary";
//					   let port_act=portGroup.clone().resize(6,12).position(port_x+22,port_y+10).addPort(portdown).addPort(portup).addTo(graphChassis);
//					   ports[11]=port_act;
//					   ports[12]=port_stdBy;
//					   card.embed(portEthernet);
//					   card.embed(portDebug);
//					   card.embed(port_act);
//					   card.embed(port_stdBy);
				   }*/
				   else if (cardType == "MPN(CGX)" || cardType == "MPN OPX(CGX)"){
					   card=ModelCardMpnCgx.clone();
					   /*set Attributes*/
					   card.attr('nodeProperties/typeName',"CGX");
					   card.attr('label/text', "CGX").position(card_x,card_y);
					   card.attr('nodeProperties/slotWidth',maxSlotPerCard);
					   card.attr('nodeProperties/rackId',rackId);
					   card.attr('nodeProperties/subRackId',subRackId);
					   card.attr('nodeProperties/slotId',slotId);
					   card.attr('nodeProperties/direction',direction);
					   let lambda=dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].subRackList[i].cardList[j].LineLambda;
					   card.attr('nodeProperties/lambda',lambda);
					   card.resize(ModelPlate.size().width*maxSlotPerCard,ModelPlate.size().height);
					   portList=dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].subRackList[i].cardList[j].portList;
					   let ports = card.attr('nodeProperties/ports');
					   for(let k=1;k<=10; k++){
						   ports[k]=((typeof portList[k-1]==="undefined")?"0":portList[k-1]);
					   }
					   /*start Drawing*/
					   card.addTo(graphChassis);
					   let portup = JSON.parse(JSON.stringify(portUp));
					   let portdown = JSON.parse(JSON.stringify(portDown));

					   portup.attrs.image.width="4px";	portup.attrs.image.height="4px";
					   portup.attrs.rect.width="4px"; portup.attrs.rect.height="4px";
					   portdown.attrs.image.width="4px"; portdown.attrs.image.height="4px";
					   portdown.attrs.rect.width="4px"; portdown.attrs.rect.height="4px";
					   
					   portup.attrs.nodeProperties.text="Tx Primary";
					   portdown.attrs.nodeProperties.text="Rx Primary";
					   let port_act=portGroup.clone().resize(4,8).position(card_x+12,card_y+12+10).addPort(portdown).addPort(portup).addTo(graphChassis);
					   portup.attrs.nodeProperties.text="Tx Secondary"+(cardType.includes("OPX")?"":("(N/A)"));
					   portdown.attrs.nodeProperties.text="Rx Secondary"+(cardType.includes("OPX")?"":("(N/A)"));
					   let port_stdBy=portGroup.clone().resize(4,8).position(card_x+12,card_y+12+21).addPort(portdown).addPort(portup).addTo(graphChassis);
					   
					   let portEthernet = portEthernetModel.clone().position(card_x+7,card_y+ModelPlate.size().height/3.6).addTo(graphChassis);
					   let portDebug = portDebugModel.clone().position(card_x+7,card_y+ModelPlate.size().height/3.6+10).addTo(graphChassis);
					   for(let k=1;k<=10;k++){
						   let portTx=JSON.parse(JSON.stringify(portSingleModel));
						   portTx.attrs.image.width="6px"; portTx.attrs.image.height="3px"
						   portTx.attrs.rect.width="6px"; portTx.attrs.rect.height="3px"
						   portTx.attrs.image["xlink:href"]="images/chassis/port_sfp_tx.jpg";
						   portTx.id='Tx';

						   let portRx=JSON.parse(JSON.stringify(portSingleModel));
						   portRx.attrs.image.width="6px"; portRx.attrs.image.height="3px"
						   portRx.attrs.rect.width="6px"; portRx.attrs.rect.height="3px"
						   portRx.attrs.image["xlink:href"]="images/chassis/port_sfp_rx.jpg";
						   portRx.id='Rx';
						   portRx.args.y=3;
						   let port;
						   port_x = card_x+7;
						   if(k<=4)
							   port_y = card_y+ModelPlate.size().height/2.5+(k-1)*8;
						   else
							   port_y = card_y+ModelPlate.size().height/2.5+(k-1)*8+4;
						   if(ports[k]!=0){
							   portTx.attrs.image["xlink:href"]="images/chassis/port_sfp_tx_on.jpg";
							   portRx.attrs.image["xlink:href"]="images/chassis/port_sfp_rx_on.jpg";
							   port=portGroupModel.clone().attr("nodeProperties/portGroupNum",k).attr("nodeProperties/identifier","sfp").position(port_x,port_y).addPort(portTx).addPort(portRx);
							   port.attr("nodeProperties/text","SFP-"+k+" On");
							   port.addTo(graphChassis);
						   }
						   else{
							   port=portGroupModel.clone().attr("nodeProperties/portGroupNum",k).attr("nodeProperties/identifier","sfp").position(port_x,port_y).addPort(portTx).addPort(portRx);
							   port.attr("nodeProperties/text","SFP-"+k+" Off");
							   port.addTo(graphChassis);
						   }
						   card.embed(port);
					   }
					   ports[11]=port_act;
					   ports[12]=port_stdBy;
					   card.embed(portEthernet);
					   card.embed(portDebug);
					   card.embed(port_act);
					   card.embed(port_stdBy);
				   }
				   else if (cardType == "TPN5x10G"){
					   card=ModelCardMpnCgx.clone();
					   /*set Attributes*/
					   card.attr('nodeProperties/typeName',"CGX");
					   card.attr('label/text', "10G-TPN").position(card_x,card_y);
					   card.attr('nodeProperties/slotWidth',maxSlotPerCard);
					   card.attr('nodeProperties/rackId',rackId);
					   card.attr('nodeProperties/subRackId',subRackId);
					   card.attr('nodeProperties/slotId',slotId);
					   card.attr('nodeProperties/direction',direction);
					   let lambda=dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].subRackList[i].cardList[j].LineLambda;
					   card.attr('nodeProperties/lambda',lambda);
					   card.resize(ModelPlate.size().width*maxSlotPerCard,ModelPlate.size().height);
					   portList=dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].subRackList[i].cardList[j].portList;
					   let ports = card.attr('nodeProperties/ports');
					   for(let k=1;k<=10; k++){
						   ports[k]=((typeof portList[k-1]==="undefined")?"0":portList[k-1]);
					   }
					   /*start Drawing*/
					   card.addTo(graphChassis);
					   let portup = JSON.parse(JSON.stringify(portUp));
					   let portdown = JSON.parse(JSON.stringify(portDown));

					   portup.attrs.image.width="4px";	portup.attrs.image.height="4px";
					   portup.attrs.rect.width="4px"; portup.attrs.rect.height="4px";
					   portdown.attrs.image.width="4px"; portdown.attrs.image.height="4px";
					   portdown.attrs.rect.width="4px"; portdown.attrs.rect.height="4px";
					   
					   portup.attrs.nodeProperties.text="Tx Primary (N/A)";
					   portdown.attrs.nodeProperties.text="Rx Primary (N/A)";
					   let port_act=portGroup.clone().resize(4,8).position(card_x+12,card_y+12+10).addPort(portdown).addPort(portup).addTo(graphChassis);
					   portup.attrs.nodeProperties.text="Tx Secondary (N/A)";
					   portdown.attrs.nodeProperties.text="Rx Secondary (N/A)";
					   let port_stdBy=portGroup.clone().resize(4,8).position(card_x+12,card_y+12+21).addPort(portdown).addPort(portup).addTo(graphChassis);
					   
					   let portEthernet = portEthernetModel.clone().position(card_x+7,card_y+ModelPlate.size().height/3.6).addTo(graphChassis);
					   let portDebug = portDebugModel.clone().position(card_x+7,card_y+ModelPlate.size().height/3.6+10).addTo(graphChassis);
					   for(let k=1;k<=10;k++){
						   let portTx=JSON.parse(JSON.stringify(portSingleModel));
						   portTx.attrs.image.width="6px"; portTx.attrs.image.height="3px"
						   portTx.attrs.rect.width="6px"; portTx.attrs.rect.height="3px"
						   portTx.attrs.image["xlink:href"]="images/chassis/port_sfp_tx.jpg";
						   portTx.id='Tx';

						   let portRx=JSON.parse(JSON.stringify(portSingleModel));
						   portRx.attrs.image.width="6px"; portRx.attrs.image.height="3px"
						   portRx.attrs.rect.width="6px"; portRx.attrs.rect.height="3px"
						   portRx.attrs.image["xlink:href"]="images/chassis/port_sfp_rx.jpg";
						   portRx.id='Rx';
						   portRx.args.y=3;
						   let port;
						   port_x = card_x+7;
						   if(k<=4)
							   port_y = card_y+ModelPlate.size().height/2.5+(k-1)*8;
						   else
							   port_y = card_y+ModelPlate.size().height/2.5+(k-1)*8+4;
						   if(ports[k]!=0){
							   portTx.attrs.image["xlink:href"]="images/chassis/port_sfp_tx_on.jpg";
							   portRx.attrs.image["xlink:href"]="images/chassis/port_sfp_rx_on.jpg";
							   port=portGroupModel.clone().attr("nodeProperties/portGroupNum",k).attr("nodeProperties/identifier","sfp").position(port_x,port_y).addPort(portTx).addPort(portRx);
							   port.attr("nodeProperties/text","SFP-"+k+" On");
							   port.addTo(graphChassis);
						   }
						   else{
							   port=portGroupModel.clone().attr("nodeProperties/portGroupNum",k).attr("nodeProperties/identifier","sfp").position(port_x,port_y).addPort(portTx).addPort(portRx);
							   port.attr("nodeProperties/text","SFP-"+k+" Off");
							   port.addTo(graphChassis);
						   }
						   card.embed(port);
					   }
					   ports[11]=port_act;
					   ports[12]=port_stdBy;
					   card.embed(portEthernet);
					   card.embed(portDebug);
					   card.embed(port_act);
					   card.embed(port_stdBy);
				   
				   }
				   else if(cardType == "MPN 10G"){
					   card=ModelCardXgm.clone();
					   /*set Attributes*/
					   card.attr('nodeProperties/typeName',"XGM");
					   card.attr('label/text', "XGM").position(card_x,card_y);
					   card.attr('nodeProperties/slotWidth',maxSlotPerCard);
					   card.attr('nodeProperties/rackId',rackId);
					   card.attr('nodeProperties/subRackId',subRackId);
					   card.attr('nodeProperties/slotId',slotId);
					   card.attr('nodeProperties/direction',direction);
					   let lambda=dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].subRackList[i].cardList[j].LineLambda;
					   card.attr('nodeProperties/lambda',lambda);
					   card.resize(ModelPlate.size().width*maxSlotPerCard,ModelPlate.size().height);
					   portList=dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].subRackList[i].cardList[j].portList;
					   let ports = card.attr('nodeProperties/ports');
					   for(let k=1;k<=20; k++){
						   ports[k]=((typeof portList[k-1]==="undefined")?"0":portList[k-1]);
					   }
					   /*start Drawing*/
					   card.addTo(graphChassis);
					   let portup = JSON.parse(JSON.stringify(portUp));
					   let portdown = JSON.parse(JSON.stringify(portDown));

					   let portEthernet = portEthernetModel.clone().position(card_x+7,card_y+ModelPlate.size().height/5).addTo(graphChassis);
					   let portDebug = portDebugModel.clone().position(card_x+7,card_y+ModelPlate.size().height/1.3).addTo(graphChassis);
					   for(let k=8;k>=1;k--){
						   let portTx=JSON.parse(JSON.stringify(portSingleModel));
						   portTx.attrs.image.width="6px"; portTx.attrs.image.height="3px"
						   portTx.attrs.rect.width="6px"; portTx.attrs.rect.height="3px"
						   portTx.attrs.image["xlink:href"]="images/chassis/port_sfp_tx.jpg";
						   portTx.id='Tx';

						   let portRx=JSON.parse(JSON.stringify(portSingleModel));
						   portRx.attrs.image.width="6px"; portRx.attrs.image.height="3px"
						   portRx.attrs.rect.width="6px"; portRx.attrs.rect.height="3px"
						   portRx.attrs.image["xlink:href"]="images/chassis/port_sfp_rx.jpg";
						   portRx.id='Rx';
						   portRx.args.y=3;
						   let port;
						   port_x = card_x+7;
						   if(k%2==0)
						   {
							   port_x = card_x+3+6;
							   port_y = card_y+ModelPlate.size().height/3.4+((8-k)/2)*6;
						   }
						   else
						   {
							   port_x = card_x+3;
						   }
						   if(ports[k-1]!=0){
							   portTx.attrs.image["xlink:href"]="images/chassis/port_sfp_tx_on.jpg";
							   portRx.attrs.image["xlink:href"]="images/chassis/port_sfp_rx_on.jpg";
							   port=portGroupModel.clone().attr("nodeProperties/portGroupNum",k).attr("nodeProperties/identifier","sfp").position(port_x,port_y).addPort(portTx).addPort(portRx);
							   port.attr("nodeProperties/text","SFP-"+k+" On");
							   port.addTo(graphChassis);
						   }
						   else{
							   port=portGroupModel.clone().attr("nodeProperties/portGroupNum",k).attr("nodeProperties/identifier","sfp").position(port_x,port_y).addPort(portTx).addPort(portRx);
							   port.attr("nodeProperties/text","SFP-"+k+" Off");
							   port.addTo(graphChassis);
						   }
						   card.embed(port);
					   }
					   
					   let portTx=JSON.parse(JSON.stringify(portSingleModel));
					   portTx.attrs.image.width="6px"; portTx.attrs.image.height="3px"
					   portTx.attrs.rect.width="6px"; portTx.attrs.rect.height="3px"
					   portTx.attrs.image["xlink:href"]="images/chassis/port_sfp_tx.jpg";
					   portTx.id='Tx';

					   let portRx=JSON.parse(JSON.stringify(portSingleModel));
					   portRx.attrs.image.width="6px"; portRx.attrs.image.height="3px"
					   portRx.attrs.rect.width="6px"; portRx.attrs.rect.height="3px"
					   portRx.attrs.image["xlink:href"]="images/chassis/port_sfp_rx.jpg";
					   portRx.id='Rx';
					   portRx.args.y=3;

					   port_x+=2;
					   port_y+=9;
					   let linePort1=portGroupModel.clone().attr("nodeProperties/portGroupNum",k).attr("nodeProperties/identifier","sfp").position(port_x,port_y).addPort(portTx).addPort(portRx);
					   linePort1.attr("nodeProperties/text","Line Port 1");
					   linePort1.addTo(graphChassis);
					   port_y+=9;
					   let linePort2=portGroupModel.clone().attr("nodeProperties/portGroupNum",k).attr("nodeProperties/identifier","sfp").position(port_x,port_y).addPort(portTx).addPort(portRx);
					   linePort2.attr("nodeProperties/text","Line Port 2");
					   linePort2.addTo(graphChassis);
					   
					   for(let k=16;k>=9;k--){
						   let portTx=JSON.parse(JSON.stringify(portSingleModel));
						   portTx.attrs.image.width="6px"; portTx.attrs.image.height="3px"
						   portTx.attrs.rect.width="6px"; portTx.attrs.rect.height="3px"
						   portTx.attrs.image["xlink:href"]="images/chassis/port_sfp_tx.jpg";
						   portTx.id='Tx';

						   let portRx=JSON.parse(JSON.stringify(portSingleModel));
						   portRx.attrs.image.width="6px"; portRx.attrs.image.height="3px"
						   portRx.attrs.rect.width="6px"; portRx.attrs.rect.height="3px"
						   portRx.attrs.image["xlink:href"]="images/chassis/port_sfp_rx.jpg";
						   portRx.id='Rx';
						   portRx.args.y=3;
						   let port;
						   port_x = card_x+7;
						   if(k%2==0)
						   {
							   port_x = card_x+3+6;
							   port_y = card_y+ModelPlate.size().height/1.7+((16-k)/2)*6;
						   }
						   else
						   {
							   port_x = card_x+3;
						   }
						   if(ports[k-1]!=0){
							   portTx.attrs.image["xlink:href"]="images/chassis/port_sfp_tx_on.jpg";
							   portRx.attrs.image["xlink:href"]="images/chassis/port_sfp_rx_on.jpg";
							   port=portGroupModel.clone().attr("nodeProperties/portGroupNum",k).attr("nodeProperties/identifier","sfp").position(port_x,port_y).addPort(portTx).addPort(portRx);
							   port.attr("nodeProperties/text","SFP-"+k+" On");
							   port.addTo(graphChassis);
						   }
						   else{
							   port=portGroupModel.clone().attr("nodeProperties/portGroupNum",k).attr("nodeProperties/identifier","sfp").position(port_x,port_y).addPort(portTx).addPort(portRx);
							   port.attr("nodeProperties/text","SFP-"+k+" Off");
							   port.addTo(graphChassis);
						   }
						   card.embed(port);
					   }
					   card.embed(portEthernet);
					   card.embed(portDebug);
				   }
				   else{
					   card=ModelPlate.clone();
					   card.attr('nodeProperties/slotWidth',maxSlotPerCard);
					   card.attr('nodeProperties/typeName',cardType);
					   card.attr('nodeProperties/rackId',rackId);
					   card.attr('nodeProperties/subRackId',subRackId);
					   card.attr('nodeProperties/slotId',slotId);
					   card.attr('nodeProperties/direction',direction);
					    if(cardType!="FILLER PLATE" && cardType !="RESV" ){
						   card.attr('label/text', cardType).position(card_x,card_y);
					   }
					   else if (cardType == "FILLER PLATE" ){
						   card.attr('label/text', "").position(card_x,card_y);
						   card.attr('logoImage/xlink:href', "").position(card_x,card_y);
					   }
					   
					   else if (cardType =="RESV"){
						   card.attr('label/text', cardType).position(card_x,card_y);
						   card.attr('logoImage/xlink:href', "").position(card_x,card_y);
					   }
					   card.resize(ModelPlate.size().width*maxSlotPerCard,ModelPlate.size().height);
					   card.addTo(graphChassis);
				   }
				   
				   if(sixSlotChassis && dwdmDetails.chassisDetails.chassis.specs.nodeType==nodeTypeIla)
					   subracks[0].embed(card)
				   else
					   subracks[i].embed(card);
				   
				   if(sixSlotChassis)
					   transformForSixSlot(card);
			   }
			   }
		   }
		   else{
			   for (j in dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].subRackList[i].cardList ) {
				   let passiveCard;
				   let rackId,subRackId,slotId,cardType="",maxSlotPerCard,portList=[],direction;
				   let portNames,ports;
				   cardType=dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].subRackList[i].cardList[j].typeName;
				   maxSlotPerCard=dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].subRackList[i].cardList[j].maxSlotPerCard;
				   rackId=dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].subRackList[i].cardList[j].rackId;
				   subRackId=dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].subRackList[i].cardList[j].subRackId;
				   slotId=dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].subRackList[i].cardList[j].slotId;
				   direction=dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].subRackList[i].cardList[j].direction;
				   subRackId=dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].subRackList[i].cardList[j].subRackId;

				   let tray_y = card_y+ModelPlate.attributes.size.height-30*slotId;
				   let base = new joint.shapes.standard.Rectangle();
				   base.position(firstSubrackTopPosX+3,tray_y).resize(280, 30).attr('label/text', 'Empty Tray').addTo(graphChassis);

				   if(cardType == "Even Mux/Demux Unit")
				   {
					   passiveCard=ModelCardEvenMuxDemux.clone().position(firstSubrackTopPosX ,tray_y);
					   portNames = ModelCardEvenMuxDemux.attr('nodeProperties/portNames');
				   }
				   else
				   {
					   passiveCard=ModelCardOddMuxDemux.clone().position(firstSubrackTopPosX ,tray_y);
					   portNames = ModelCardOddMuxDemux.attr('nodeProperties/portNames');
				   }
				   passiveCard.attr('nodeProperties/typeName',cardType);
				   passiveCard.attr('nodeProperties/rackId',rackId);
				   passiveCard.attr('nodeProperties/subRackId',subRackId);
				   passiveCard.attr('nodeProperties/slotId',slotId);
				   passiveCard.attr('nodeProperties/direction',direction);
				   passiveCard.attr('nodeProperties/lambda',"");
				   passiveCard.addTo(graphChassis);

				   let port_x=firstSubrackTopPosX+3;
				   let port_y=tray_y;
				   ports = passiveCard.attr('nodeProperties/portsMux');
				   //Draw MuxTray
				   for(k in ports){
					   if (k==11)
					   {    port_y+=12;
					   port_x=firstSubrackTopPosX+3;
					   }
					   let port1=JSON.parse(JSON.stringify(portSingleModel));
					   port1.attrs.image.width="6px"; port1.attrs.image.height="7.5px";
					   port1.attrs.rect.width="6px"; port1.attrs.rect.height="7.5px";
					   port1.attrs.image["xlink:href"]="images/chassis/blue_single.jpg"; 
					   port1.attrs.nodeProperties.text=portNames[k];
					   port1.args.x=0;
					   port1.id=portNames[k].split(",")[0];

					   let port2=JSON.parse(JSON.stringify(portSingleModel));
					   port2.attrs.image.width="6px"; port2.attrs.image.height="7.5px";
					   port2.attrs.rect.width="6px"; port2.attrs.rect.height="7.5px";
					   port2.attrs.image["xlink:href"]="images/chassis/blue_single.jpg"; 
					   port2.attrs.nodeProperties.text=portNames[k];
					   port2.args.x=5.5;
					   port2.id=portNames[k].split(",")[1];

					   let port=portGroupModel.clone().resize(11,8).position(port_x,port_y+5).addPort(port1).addPort(port2).addTo(graphChassis);
					   ports[k]=port;
					   port_x+=13.2;
					   passiveCard.embed(port);
				   };

				   //Draw De-MuxTray
				   ports = passiveCard.attr('nodeProperties/portsDemux');
				   port_x=firstSubrackTopPosX+3+148;
				   port_y=tray_y;
				   for(k in ports){
					   if (k==11)
					   {    port_y+=12;
					   port_x=firstSubrackTopPosX+3+148;
					   }
					   let port1=JSON.parse(JSON.stringify(portSingleModel));
					   port1.attrs.image.width="6px"; port1.attrs.image.height="7.5px";
					   port1.attrs.rect.width="6px"; port1.attrs.rect.height="7.5px";
					   port1.attrs.image["xlink:href"]="images/chassis/blue_single.jpg"; 
					   port1.attrs.nodeProperties.text=portNames[k];
					   port1.args.x=0;
					   port1.id=portNames[k].split(",")[0];

					   let port2=JSON.parse(JSON.stringify(portSingleModel));
					   port2.attrs.image.width="6px"; port2.attrs.image.height="7.5px";
					   port2.attrs.rect.width="6px"; port2.attrs.rect.height="7.5px";
					   port2.attrs.image["xlink:href"]="images/chassis/blue_single.jpg"; 
					   port2.attrs.nodeProperties.text=portNames[k];
					   port2.args.x=5.5;
					   port2.id=portNames[k].split(",")[1];

					   let port=portGroupModel.clone().resize(11,8).position(port_x,port_y+5).addPort(port1).addPort(port2).addTo(graphChassis);
					   ports[k]=port;
					   port_x+=13.5;
					   passiveCard.embed(port);
				   }
				   subracks[i].embed(passiveCard);
			   }  
		   }
		   card_y+=ModelSubrack.attributes.size.height;
		   
	   }
	   
	   
	   //Draw Subrack 0 for Ycable and Mux-Demux
	   if(chassisType!="SIX-SLOT CHASSIS"){
		   let subrack0_y = firstSubrackTopPosY + 3*(ModelSubrack.attributes.size.height);
		   let subrack0 = new joint.shapes.standard.Rectangle();
		   subrack0.position(firstSubrackTopPosX,subrack0_y).resize(300, 90).attr('NodeProperties/text', 'Subrack0').addTo(graphChassis);
		   chassisParent.embed(subrack0);

		   //Draw Empty Trays By Default
		   let tray_y = firstSubrackTopPosY + 3*(ModelSubrack.attributes.size.height);
		   let rect = new joint.shapes.standard.Rectangle();
		   rect.position(firstSubrackTopPosX,tray_y).resize(300, 30).attr('label/text', 'Empty Tray').addTo(graphChassis);
		   rect.clone().translate(0, tray.attributes.size.height).addTo(graphChassis);
		   rect.clone().translate(0, tray.attributes.size.height*2).addTo(graphChassis);

		   //draw Ycable and Mux-Demux
		   tray_y = 2 * tray.attributes.size.height + tray_y;
		   for (i in dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].yCableList) {
			   let passiveCard;
			   let rackId,slotId,cardType,maxSlotPerCard,portList=[],direction,subRackId;
			   rackId=""+$("#selectRack option:selected").text();
			   slotId=dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].yCableList[i].slotId;
			   cardType=dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].yCableList[i].typeName;
			   subRackId=0;
			   direction=dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].yCableList[i].direction;
			   if(cardType == "YCable1x2Unit")
			   {
				   passiveCard=tray.clone().position(firstSubrackTopPosX, tray_y).addTo(graphChassis);
				   passiveCard.attributes.attrs.nodeProperties.typeName = dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].yCableList[i].typeName;
				   for (j in dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].yCableList[i].portList)
					   show_ycable(firstSubrackTopPosX + 5, tray_y, i);//to show ycable data
			   }
			   else if(cardType == "Even Mux/Demux Unit" ||
					   cardType == "Odd Mux/ Demux Unit" )
			   {
				   let portNames,ports;
				   if(cardType == "Even Mux/Demux Unit")
				   {
					   passiveCard=ModelCardEvenMuxDemux.clone().position(firstSubrackTopPosX ,tray_y);
					   portNames = ModelCardEvenMuxDemux.attr('nodeProperties/portNames');
				   }
				   else
				   {
					   passiveCard=ModelCardOddMuxDemux.clone().position(firstSubrackTopPosX ,tray_y);
					   portNames = ModelCardOddMuxDemux.attr('nodeProperties/portNames');
				   }
//				   console.log(rackId+","+slotId+","+cardType+","+direction);
				   passiveCard.attr('nodeProperties/typeName',cardType);
				   passiveCard.attr('nodeProperties/rackId',rackId);
				   passiveCard.attr('nodeProperties/slotId',slotId);
				   passiveCard.attr('nodeProperties/direction',direction);
				   passiveCard.attr('nodeProperties/subRackId',subRackId);
				   passiveCard.attr('nodeProperties/lambda',"");

				   passiveCard.addTo(graphChassis);

				   let port_x=firstSubrackTopPosX+3;
				   let port_y=tray_y;
				   ports = passiveCard.attr('nodeProperties/portsMux');
				   //Draw MuxTray
				   for(k in ports){
					   if (k==11)
					   {    port_y+=12;
					   port_x=firstSubrackTopPosX+3;
					   }
					   let port1=JSON.parse(JSON.stringify(portSingleModel));
					   port1.attrs.image.width="6px"; port1.attrs.image.height="7.5px";
					   port1.attrs.rect.width="6px"; port1.attrs.rect.height="7.5px";
					   port1.attrs.image["xlink:href"]="images/chassis/blue_single.jpg"; 
					   port1.attrs.nodeProperties.text=portNames[k];
					   port1.args.x=0;
					   port1.id=portNames[k].split(",")[0];

					   let port2=JSON.parse(JSON.stringify(portSingleModel));
					   port2.attrs.image.width="6px"; port2.attrs.image.height="7.5px";
					   port2.attrs.rect.width="6px"; port2.attrs.rect.height="7.5px";
					   port2.attrs.image["xlink:href"]="images/chassis/blue_single.jpg"; 
					   port2.attrs.nodeProperties.text=portNames[k];
					   port2.args.x=5.5;
					   port2.id=portNames[k].split(",")[1];

					   let port=portGroupModel.clone().resize(11,8).position(port_x,port_y+5).addPort(port1).addPort(port2).addTo(graphChassis);
					   ports[k]=port;
					   port_x+=13.2;
					   passiveCard.embed(port);
				   };

				   //Draw De-MuxTray
				   ports = passiveCard.attr('nodeProperties/portsDemux');
				   port_x=firstSubrackTopPosX+3+150;
				   port_y=tray_y;
				   for(k in ports){
					   if (k==11)
					   {    port_y+=12;
					   port_x=firstSubrackTopPosX+3+150;
					   }
					   let port1=JSON.parse(JSON.stringify(portSingleModel));
					   port1.attrs.image.width="6px"; port1.attrs.image.height="7.5px";
					   port1.attrs.rect.width="6px"; port1.attrs.rect.height="7.5px";
					   port1.attrs.image["xlink:href"]="images/chassis/blue_single.jpg"; 
					   port1.attrs.nodeProperties.text=portNames[k];
					   port1.args.x=0;
					   port1.id=portNames[k].split(",")[0];

					   let port2=JSON.parse(JSON.stringify(portSingleModel));
					   port2.attrs.image.width="6px"; port2.attrs.image.height="7.5px";
					   port2.attrs.rect.width="6px"; port2.attrs.rect.height="7.5px";
					   port2.attrs.image["xlink:href"]="images/chassis/blue_single.jpg"; 
					   port2.attrs.nodeProperties.text=portNames[k];
					   port2.args.x=5.5;
					   port2.id=portNames[k].split(",")[1];

					   let port=portGroupModel.clone().resize(11,8).position(port_x,port_y+5).addPort(port1).addPort(port2).addTo(graphChassis);
					   ports[k]=port;
					   port_x+=13.5;
					   passiveCard.embed(port);
				   };
				   subrack0.embed(passiveCard);
			   }  
			   tray_y = tray_y - tray.attributes.size.height;
		   }
		   for (i in dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].yCableList){
			   if (i >= 3) {
				   tray.clone().position(firstSubrackTopPosX,firstSubrackTopPosY- ModelSubrack.attributes.size.height).addTo(graphChassis);
				   for (j in dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].yCableList[i].portList)
					   show_ycable(firstSubrackTopPosX + 5,firstSubrackTopPosY- ModelSubrack.attributes.size.height, i);//to show y cable data if cable greater than 3
			   }
		   }
	   }
	   
	   function show_ycable(x1, y1, i) {
		   var z;
		   for (z = 0; z < 10; z++) {

			   if (z in dwdmDetails.chassisDetails.chassis.specs.rackList[rack_id].yCableList[i].portList) {
				   ycable_on.clone().position(x1, y1 + 5).addTo(graphChassis);
				   x1 = x1 + 30;
			   }
			   else {
				   ycable_off.clone().position(x1, y1 + 5).addTo(graphChassis);
				   x1 = x1 + 30; 
			   }
		   }
	   }
   }
   
   init = function($scope) {
	    /*Helper Functions Definitions Start*/
	    function initNgChassisViewController() {
	    	console.log("initNgChassisViewController");
	    	resetParamBasedOnNeSelection();
	    	reOrderChassisDetails();
	    }
		
		function resetParamBasedOnNeSelection() {
			dwdmdetails={};
            dwdmDetails.chassisDetails = $scope.chassisViewDbData;
		}
	
	    function reOrderChassisDetails() {
	    	var i, j, k;/**Local Vars*/
	        for (i = 0; i < dwdmDetails.chassisDetails.chassis.specs.rackList.length; i++) {
	            for (j = 0; j < dwdmDetails.chassisDetails.chassis.specs.rackList[i].subRackList.length; j++) {
	                for (k = 0; k < dwdmDetails.chassisDetails.chassis.specs.rackList[i].subRackList[j].cardList.length; k++) {
	                	/**DBG => Port sorting yet to be done*/
	                	///CreateOrderedArrayList(dwdmDetails.chassisDetails.chassis.specs.rackList[i].subRackList[j].cardList[k].portList,"portId");
	                }
	                let cardList = dwdmDetails.chassisDetails.chassis.specs.rackList[i].subRackList[j].cardList;
	                cardList = CreateOrderedArrayList(cardList, "slotId");
	            }
	            let subRackList = dwdmDetails.chassisDetails.chassis.specs.rackList[i].subRackList;
	            subRackList=CreateReverseOrderedArrayList(subRackList, "subRackId");
	        }
	        let rackList = dwdmDetails.chassisDetails.chassis.specs.rackList;
	        CreateOrderedArrayList(rackList, "rackId");
	    };

	    function CreateOrderedArrayList(arrayList, tag) {
	        arrayList = arrayList
	            .sort(function(a, b) {
	                return a[tag] - b[tag]
	            });
	    };

	    function CreateReverseOrderedArrayList(arrayList, tag) {
	        arrayList = arrayList
	            .sort(function(a, b) {
	                return b[tag] - a[tag]
	            });
	    };
	    /*Helper Function Definitions ends*/
	    
	    //Initialize Parameters 
	    initNgChassisViewController();
	    setGrid(15, '#080808');
	    drawChassis(0); //draw chassis for Rack 1
	    removeLinksForAllCards(); //remove Links and ref_key from links global var
	    paperChassis.scale(2);
	    $('#rack').contextmenu(function() {
	        return false;
	    });
}