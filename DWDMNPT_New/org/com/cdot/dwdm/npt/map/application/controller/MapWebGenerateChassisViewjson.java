
package application.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.xmlbeans.impl.jam.mutable.MSourcePosition;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import application.constants.MapConstants;
import application.constants.ResourcePlanConstants;
import application.model.Bom;
import application.model.CardInfo;
import application.model.Equipment;
import application.model.MuxDemuxPortInfo;
import application.model.Node;
import application.model.PortInfo;
import application.model.YCablePortInfo;
import application.service.DbService;

/**
 * @author hp
 * @brief  Require!  
 *         Finally return the same object to the Main Cotroller 
 *          
 */
public class MapWebGenerateChassisViewjson {

	/**
	 * 
	 * @param dbService
	 * @return JSONObject
	 */
	@SuppressWarnings("unchecked")
	public JSONObject MapWebGenerateChassisViewJsonReq(int nodeId,HashMap networkInfoMap , DbService dbService) {

		System.out.println("Inside MapWebGenerateChassisViewJsonReq and Going to Return the JSon for Chassis View.. NodeId : "+nodeId);
	
		JSONObject jsonFinalObj = new JSONObject();
     //   int networkId=Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString());
        int networkId= (int)networkInfoMap.get(MapConstants.NetworkId); /**Network Id for common use*/ 
		//int nodeId=1;//Integer.parseInt(nodeId);
		
		List<Map<String, Object>> rackList = dbService.getCardInfoService().FindRacks(networkId, nodeId);
		JSONArray jsonRackListArrayObj = new JSONArray();
		for (int r = 0; r < rackList.size(); r++) {
			JSONObject jsonRackListInfoObj = new JSONObject();
			System.out.println("Value of r ="+r+" Value of rackList("+r+") ="+rackList.get(r).get("Rack"));
			jsonRackListInfoObj.put("rackId", rackList.get(r).get("Rack"));
			System.out.println("Rack Id for fetching SubRack List :"+rackList.get(r).get("Rack").toString());
			List<Map<String, Object>> subRackList = dbService.getCardInfoService().FindSbracksInRack(networkId,nodeId,Integer.parseInt(rackList.get(r).get("Rack").toString()));
			System.out.println("SubRack List :"+subRackList);
			JSONArray jsonSubRackListArrayObj = new JSONArray();
			int subRackIndex=0,yCableBlockFlag=0;
			String PrefChassisType="";
			for(int s=1;s<=MapConstants.MaxSubRackPerRack;s++)
			/*for(int s=0;s<subRackList.size();s++)*/
			{
				//System.out.println("Value of r ="+r+" Value of rackList("+r+") ="+rackList.get(r).get("Rack")+" Value of subrackList("+s+") ="+subRackList.get(s).get("SbRack"));
				JSONObject jsonSubRackListInfoObj = new JSONObject();
				JSONArray jsonCardListArrayObj = new JSONArray();
				EquipmentPreferences eqp = new EquipmentPreferences(dbService);
				try {
					PrefChassisType = eqp.fGetPreferredEqType(networkId, nodeId, ResourcePlanConstants.CatChassisType,
							ResourcePlanConstants.ParamSubrack, ""+(r*1+1)+"-"+s)[0].toString();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				if(subRackList.isEmpty() || s!=Integer.parseInt(subRackList.get(subRackIndex).get("SbRack").toString()))
				{
					jsonSubRackListInfoObj.put("rackId",rackList.get(r).get("Rack"));//rackid
					jsonSubRackListInfoObj.put("subRackId",s);//subrackId
					jsonSubRackListInfoObj.put("chassisType",PrefChassisType);
					jsonSubRackListInfoObj.put("maxSlotPerSubRack",MapConstants.MaxSlotPerPerSubRack);
					jsonSubRackListInfoObj.put("yCableFlag",MapConstants.yCableFlag); /**By Default 0 means no y-cable block=> it's a normal SubRack*/
					jsonSubRackListInfoObj.put("cardList", jsonCardListArrayObj);
					jsonSubRackListArrayObj.add(jsonSubRackListInfoObj);
				}
				else
				{
					List<CardInfo> cardInfoList = dbService.getCardInfoService().FindCardsInSbrack(networkId, nodeId,Integer.parseInt(rackList.get(r).get("Rack").toString()),Integer.parseInt(subRackList.get(subRackIndex).get("SbRack").toString()));
					
					Map<Integer, Boolean> cardLocationMap = new HashMap<Integer, Boolean>(); 
					
					/**Initialize all cards location with false*/
					for(int counter=0; counter<MapConstants.MaxSlotPerPerSubRack; counter++){
						cardLocationMap.put(counter+1, false);
					}
					
					for(int c=0;c<cardInfoList.size();c++)
					{
						System.out.println("Value of c ="+c+" Value of cardInfoList("+c+") ="+cardInfoList.get(c).getCard());
						JSONObject jsonCardListInfoObj = new JSONObject();
						jsonCardListInfoObj.put("rackId",cardInfoList.get(c).getRack());
						jsonCardListInfoObj.put("subRackId",cardInfoList.get(c).getSbrack());	
						jsonCardListInfoObj.put("slotId",cardInfoList.get(c).getCard());	
						jsonCardListInfoObj.put("typeName",cardInfoList.get(c).getCardType());
						jsonCardListInfoObj.put("direction",cardInfoList.get(c).getDirection());
						int slotSize=dbService.getEquipmentService().FindSlotSize(cardInfoList.get(c).getEquipmentId());
						jsonCardListInfoObj.put("maxSlotPerCard",slotSize);
						
						//jsonCardListInfoObj.put("maxSlotPerCard", 1);
					
						
						if(cardInfoList.get(c).getCardType().equalsIgnoreCase(MapConstants.CardTypeYCable)   
							 || 
							 cardInfoList.get(c).getCardType().equalsIgnoreCase(ResourcePlanConstants.Odd_Mux_Demux_Unit)   
							 || 
							 cardInfoList.get(c).getCardType().equalsIgnoreCase(ResourcePlanConstants.Even_Mux_Demux_Unit)   							
						  )
						{
							yCableBlockFlag= MapConstants.SUCCESS;
						}
						else{
							yCableBlockFlag= MapConstants.FAILURE;
						}
						
						cardLocationMap.put(cardInfoList.get(c).getCard(), true); /**Hash Enable Card Slot with True*/
						
						if(slotSize == MapConstants.I_TWO){								
							cardLocationMap.put(cardInfoList.get(c).getCard()+1, true); /**Hash Enable 2-Slot Card with True*/
						}
						
						/**
						 * Fill Port Info
						 */
						JSONObject jsonPortListInfoObj = new JSONObject();
						JSONArray portListArray  = new JSONArray();
						JSONArray lambdaListArray  = new JSONArray();
						if(yCableBlockFlag == MapConstants.SUCCESS) {
							List<MuxDemuxPortInfo> muxDemuxPortInfoList = dbService.getMuxDemuxPortInfoService().FindCardPorts(
									networkId, nodeId,
									cardInfoList.get(c).getRack(),
									cardInfoList.get(c).getSbrack(),
									cardInfoList.get(c).getCard());

							

							if(!muxDemuxPortInfoList.isEmpty()) {						

								for(int port=0; port<muxDemuxPortInfoList.size(); port++) {
									portListArray.add(muxDemuxPortInfoList.get(port).getPortNum());
									lambdaListArray.add(muxDemuxPortInfoList.get(port).getWavelength());
								}

								System.out.println(" jsonPortListInfoObj  (mux/demux) :  "+jsonPortListInfoObj);
							}
							jsonCardListInfoObj.put("portList",portListArray);
							jsonCardListInfoObj.put("lambdaList",lambdaListArray);

						}
						else
						{
							List<PortInfo> portInfoList = dbService.getPortInfoService().FindPortInfo
									(networkId, nodeId, 
											cardInfoList.get(c).getRack(),
											cardInfoList.get(c).getSbrack(),
											cardInfoList.get(c).getCard());

							if(!portInfoList.isEmpty()) {						

								for(int port=0; port<portInfoList.size(); port++) {
									///jsonPortListInfoObj.put("Port",portInfoList.get(port).getPort());
									portListArray.add(portInfoList.get(port).getPort());
								}

								System.out.println(" jsonPortListInfoObj :  "+jsonPortListInfoObj);

							}
							jsonCardListInfoObj.put("portList",portListArray);
							jsonCardListInfoObj.put("LineLambda",cardInfoList.get(c).getWavelength());
						}
						
						/**Add into Array of Card Info containing Port Info*/
						jsonCardListArrayObj.add(jsonCardListInfoObj);
						
						System.out.println(" jsonCardListInfoObj :  "+jsonCardListInfoObj);
					}
					
					/**Push the Filler Tray where actual Card is not present*/
					// for (Map.Entry<Integer, Boolean> entry : cardLocationMap.entrySet()) {
					// 	if(entry.getValue().equals(false)){
					// 		System.out.println("entry.getKey() "+ entry.getKey());
							
					// 		JSONObject jsonCardListInfoObj = new JSONObject();
					// 		jsonCardListInfoObj.put("rackId",r);
					// 		jsonCardListInfoObj.put("subRackId",s);	
					// 		jsonCardListInfoObj.put("slotId",entry.getKey());	
					// 		jsonCardListInfoObj.put("typeName","Filler Tray");							
					// 		jsonCardListInfoObj.put("maxSlotPerCard",MapConstants.I_ONE);
							
					// 		jsonCardListArrayObj.add(jsonCardListInfoObj);
					// 	}
					// }
					
					
					jsonSubRackListInfoObj.put("rackId",rackList.get(r).get("Rack"));//rackid
					jsonSubRackListInfoObj.put("subRackId",subRackList.get(subRackIndex).get("SbRack"));//subrackId
					jsonSubRackListInfoObj.put("chassisType",PrefChassisType);//subrackId
					jsonSubRackListInfoObj.put("maxSlotPerSubRack",MapConstants.MaxSlotPerPerSubRack);
					jsonSubRackListInfoObj.put("yCableFlag",yCableBlockFlag);/**By Default 0 means no y-cable block=> it's a normal SubRack*/
					jsonSubRackListInfoObj.put("cardList", jsonCardListArrayObj);
					jsonSubRackListArrayObj.add(jsonSubRackListInfoObj);
					if(subRackIndex<subRackList.size()-1)
					subRackIndex++;
				} 	
				
			}
			
			List<CardInfo> passiveCardInfoList = dbService.getCardInfoService().FindCardsInSbrack(networkId, nodeId,Integer.parseInt(rackList.get(r).get("Rack").toString()),0);
			JSONArray jsonPassiveTrayListArrayObj = new JSONArray();
			for(int c=0;c<passiveCardInfoList.size();c++)
			{
				JSONObject jsonPassiveTrayObj = new JSONObject();
				int cardId =passiveCardInfoList.get(c).getCard();
				jsonPassiveTrayObj.put("typeName",passiveCardInfoList.get(c).getCardType());/**CardType*/
				jsonPassiveTrayObj.put("Card",cardId);/**Card*/
				jsonPassiveTrayObj.put("slotId",c+1);/**slotId*/
				jsonPassiveTrayObj.put("direction",passiveCardInfoList.get(c).getDirection());/**slotId*/
				
				
				/**
				 * Fill Port Info
				 */		
				int rack =Integer.parseInt(rackList.get(r).get("Rack").toString());
				List<YCablePortInfo> yCablePortInfoList = dbService.getYcablePortInfoService().FindAll(networkId, nodeId,cardId);
				List<MuxDemuxPortInfo> muxDemuxPortInfoList = dbService.getMuxDemuxPortInfoService().FindCardPorts(networkId, nodeId,rack,0,cardId);
						
				
				JSONArray  passiveTrayPortListArray  = new JSONArray();
				JSONArray  passiveTrayLambdaArray  = new JSONArray();
			
				if(!(yCablePortInfoList.isEmpty()) || !(muxDemuxPortInfoList.isEmpty())) { //If either of the tray isn't empty
					if(ResourcePlanConstants.YCable1x2Unit.equals(passiveCardInfoList.get(c).getCardType())) 
					{
						for(int port=0; port<yCablePortInfoList.size(); port++) {
							passiveTrayPortListArray.add(yCablePortInfoList.get(port).getYCablePort());
						}					
					}
					else 
					{
						for(int port=0; port<muxDemuxPortInfoList.size(); port++) {
							passiveTrayPortListArray.add(muxDemuxPortInfoList.get(port).getPortNum());
							passiveTrayLambdaArray.add(muxDemuxPortInfoList.get(port).getWavelength());
						}
					}					
				}
				
				jsonPassiveTrayObj.put("portList", passiveTrayPortListArray);
				jsonPassiveTrayObj.put("lambdaList", passiveTrayLambdaArray);

				jsonPassiveTrayListArrayObj.add(jsonPassiveTrayObj);
			}
			
			
			jsonRackListInfoObj.put("rackId",rackList.get(r).get("Rack"));//rackid
			jsonRackListInfoObj.put("maxSubRackPerRack", MapConstants.MaxSubRackPerRack);
			jsonRackListInfoObj.put("subRackList", jsonSubRackListArrayObj);
			jsonRackListInfoObj.put("maxOuterTrayPerRack",MapConstants.MaxYCablePerPerRack);//rackid
			jsonRackListInfoObj.put("yCableList",jsonPassiveTrayListArrayObj);
			jsonRackListArrayObj.add(jsonRackListInfoObj);
		}

		JSONObject jsonChassisObj = new JSONObject();
		JSONObject jsonSpecsObj = new JSONObject();	
		//specs object
		jsonSpecsObj.put("nodeStation",dbService.getNodeService().FindNode(networkId, nodeId).getStationName()); /**Station Name*/
		jsonSpecsObj.put("nodeSite",dbService.getNodeService().FindNode(networkId, nodeId).getSiteName()); /**Site Name*/
		jsonSpecsObj.put("maxRackPerChassis", /*MapConstants.MaxRackPerChassis*/rackList.size());		
		jsonSpecsObj.put("nodeType",dbService.getNodeService().FindNode(networkId, nodeId).getNodeType()); /**Node Type*/
        
		jsonSpecsObj.put("rackList",jsonRackListArrayObj);
		
		jsonChassisObj.put("specs", jsonSpecsObj);
		
		jsonFinalObj.put("chassis",jsonChassisObj);
		
		System.out.println("Final Chassis View json Object ::"+jsonFinalObj);

		return jsonFinalObj;			

	}
}
