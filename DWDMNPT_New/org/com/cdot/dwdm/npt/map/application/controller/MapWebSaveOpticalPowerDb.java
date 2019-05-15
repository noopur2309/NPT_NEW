package application.controller;

import java.sql.SQLException;
import java.util.List;

import org.apache.xmlbeans.impl.jam.mutable.MSourcePosition;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import application.constants.MapConstants;
import application.constants.ResourcePlanConstants;
import application.controller.GenerateLinkWavelengthMap;
import application.model.CardInfo;
import application.model.LinkWavelengthMap;
import application.model.Node;
import application.model.OpticalPowerCdcRoadmAdd;
import application.model.OpticalPowerCdcRoadmDrop;
import application.service.DbService;
import application.service.OpticalPowerCdcRoadmAddService;
import application.service.OpticalPowerCdcRoadmDropService;

public class MapWebSaveOpticalPowerDb {
	
	
	/**
	 * 
	 * @param dbService
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public void MapWebSaveOpticalPowerCDC(int networkId, DbService dbService) throws SQLException {

		System.out.println("Inside MapWebSaveOpticalPowerCDC and Going to save Optical Power Measurement for CDC");
//		org.json.JSONObject jsonMapDataObj = new org.json.JSONObject(jsonStr);
//		org.json.JSONObject networkInfoObj=(org.json.JSONObject)jsonMapDataObj.get("NetworkInfo");
		
//		int networkId=Integer.parseInt(dbService.getNetworkService().GetByNetworkName(networkInfoObj.getString("NetworkName")).toString());
		
        GenerateLinkWavelengthMap generateLinkWavelengthMap=new GenerateLinkWavelengthMap(dbService);       
		List<LinkWavelengthMap> opticalDataList = generateLinkWavelengthMap.fsendLinkWavelengthMap(networkId);
		
		double PA_in_Drop=0,PA_out_Drop=0,WSS_in_Drop=0,WSS_out_Drop=0,WSS_attenuation_Drop=0,EDFA_in_Drop=0,
		EDFA_out_Drop=0,MCS_in_Drop=0,MCS_out_Drop=0,MPN_in_Drop=0,PA_Gain=0;
		
		double BA_in_Add=0,BA_out_Add=0,WSS_in_Add=0,WSS_out_Add=0,WSS_attenuation_Add=0,EDFA_in_Add=0,
		EDFA_out_Add=0,MCS_in_Add=0,MCS_out_Add=0,MPN_in_Add=0;
		
		double MCS_LOSS=17,EDFA_GAIN=20.8,MPN_LAUNCH_POWER=1,SUPY_LOSS=0.8,WSS_LOSS=7,INVALID_VALUE=999;
		
		JSONArray jsonOpticalDataArrayObj = new JSONArray();
		for(int i=0;i<opticalDataList.size();i++)
		{
			System.out.println("Node Id :"+opticalDataList.get(i).getNodeId());
			System.out.println("Link Id :"+opticalDataList.get(i).getLinkId());
			System.out.println("nWaves :"+opticalDataList.get(i).getnWaves());
			System.out.println("nMpns:"+opticalDataList.get(i).getnMpns());
			System.out.println("Direction:"+opticalDataList.get(i).getDirection());
			System.out.println("Wavelengths :"+opticalDataList.get(i).getWavelengths());
			System.out.println("Spanloss :"+opticalDataList.get(i).getSpanLoss());
			
			Node node=dbService.getNodeService().FindNode(networkId, opticalDataList.get(i).getNodeId());
			
			if(node.getNodeType()==MapConstants.roadm)
			{
				OpticalPowerCdcRoadmDrop opticalPowerCdcRoadmDrop=new OpticalPowerCdcRoadmDrop();
				OpticalPowerCdcRoadmAdd opticalPowerCdcRoadmAdd=new OpticalPowerCdcRoadmAdd();
				
				System.out.println("nWaves Drop :"+opticalDataList.get(i).getnWaves());
				PA_in_Drop=(4.34*Math.log(opticalDataList.get(i).getnWaves())-opticalDataList.get(i).getSpanLoss()-SUPY_LOSS);//supy loss
				PA_in_Drop=Math.round(PA_in_Drop*100.0)/100.0;
				System.out.println("PA_in_Drop :"+PA_in_Drop);
				PA_Gain=opticalDataList.get(i).getSpanLoss();
				WSS_in_Drop=PA_out_Drop=PA_in_Drop+PA_Gain;//+ supyLoss
				PA_out_Drop=Math.round(PA_out_Drop*100.0)/100.0;
				WSS_in_Drop=Math.round(WSS_in_Drop*100.0)/100.0;
				System.out.println("WSS_in_Drop :"+WSS_in_Drop);
				EDFA_in_Drop=WSS_out_Drop=WSS_in_Drop-WSS_LOSS;// - wss loss
				EDFA_in_Drop=Math.round(EDFA_in_Drop*100.0)/100.0;
				WSS_out_Drop=Math.round(WSS_out_Drop*100.0)/100.0;
				if(WSS_out_Drop>2.5)
				 {
					WSS_attenuation_Drop=WSS_out_Drop-2.5;
					EDFA_in_Drop=WSS_out_Drop=2.5;
				 }
				else WSS_attenuation_Drop=0;//attenuation
				System.out.println("WSS_attenuation_Drop :"+WSS_attenuation_Drop);
				MCS_in_Drop=EDFA_out_Drop=EDFA_in_Drop+EDFA_GAIN;//+edfa gain
				MCS_in_Drop=Math.round(MCS_in_Drop*100.0)/100.0;
				EDFA_out_Drop=Math.round(EDFA_out_Drop*100.0)/100.0;
				
				MPN_in_Drop=MCS_out_Drop=MCS_in_Drop-MCS_LOSS;//- insertion loss mcs
				MPN_in_Drop=Math.round(MPN_in_Drop*100.0)/100.0;
				MCS_out_Drop=Math.round(MCS_out_Drop*100.0)/100.0;
				
				opticalPowerCdcRoadmDrop.setNetworkId(networkId);
				opticalPowerCdcRoadmDrop.setNodeId(opticalDataList.get(i).getNodeId());
				System.out.println("Direction Drop :"+opticalDataList.get(i).getDirection());
				opticalPowerCdcRoadmDrop.setDirection(opticalDataList.get(i).getDirection());
				opticalPowerCdcRoadmDrop.setNoOfLambdas(opticalDataList.get(i).getnWaves());
				opticalPowerCdcRoadmDrop.setPaIn((float)PA_in_Drop);
				opticalPowerCdcRoadmDrop.setPaOut((float)PA_out_Drop);
				opticalPowerCdcRoadmDrop.setRxWssIn((float)WSS_in_Drop);
				opticalPowerCdcRoadmDrop.setRxWssOut((float)WSS_out_Drop);
				opticalPowerCdcRoadmDrop.setRxWssAttenuation((float)WSS_attenuation_Drop);
				opticalPowerCdcRoadmDrop.setDropEdfaIn((float)EDFA_in_Drop);
				opticalPowerCdcRoadmDrop.setDropEdfaOut((float)EDFA_out_Drop);
				opticalPowerCdcRoadmDrop.setDropMcsIn((float)MCS_in_Drop);
				opticalPowerCdcRoadmDrop.setDropMcsOut((float)MCS_out_Drop);
				opticalPowerCdcRoadmDrop.setMpnIn((float)MPN_in_Drop);
				
				dbService.getCdcRoadmDropService().Insert(opticalPowerCdcRoadmDrop);	
				CardInfo cardInfo=dbService.getCardInfoService().FindCard(networkId, opticalDataList.get(i).getNodeId(), ResourcePlanConstants.CardPaBa, opticalDataList.get(i).getDirection());
				if(cardInfo!=null)
				dbService.getAmplifierConfigService().UpdateGain(networkId, opticalDataList.get(i).getNodeId(), cardInfo.getRack(), cardInfo.getSbrack(), cardInfo.getCard(), (float)PA_Gain);
				
				CardInfo wssCardType=dbService.getCardInfoService().FindWss(networkId, opticalDataList.get(i).getNodeId(), opticalDataList.get(i).getDirection());
				if(wssCardType!=null)
				cardInfo=dbService.getCardInfoService().FindCard(networkId, opticalDataList.get(i).getNodeId(), wssCardType.getCardType(), opticalDataList.get(i).getDirection());
				
				if(cardInfo!=null)
				dbService.getWssDirectionConfigService().Update(networkId, opticalDataList.get(i).getNodeId(), cardInfo.getRack(), cardInfo.getSbrack(), cardInfo.getCard(), (float)WSS_attenuation_Drop);
				
				MPN_in_Add=opticalDataList.get(i).getnMpns()==0?0:MPN_LAUNCH_POWER;//mpn launch power
				MCS_in_Add=opticalDataList.get(i).getnMpns()==0?INVALID_VALUE:(4.34*Math.log(1.2589 * opticalDataList.get(i).getnMpns()));
				MCS_in_Add=Math.round(MCS_in_Add*100.0)/100.0;
				
				EDFA_in_Add=MCS_out_Add=MCS_in_Add==INVALID_VALUE?INVALID_VALUE:(MCS_in_Add-MCS_LOSS);// - mcs insertion loss
				EDFA_in_Add=Math.round(EDFA_in_Add*100.0)/100.0;
				MCS_out_Add=Math.round(MCS_out_Add*100.0)/100.0;
				
				WSS_in_Add=EDFA_out_Add=EDFA_in_Add==INVALID_VALUE?INVALID_VALUE:(EDFA_in_Add+EDFA_GAIN);//+ edfa gain
				WSS_in_Add=Math.round(WSS_in_Add*100.0)/100.0;
				EDFA_out_Add=Math.round(EDFA_out_Add*100.0)/100.0;
				
				WSS_out_Add=WSS_in_Add==INVALID_VALUE?INVALID_VALUE:(WSS_in_Add-WSS_LOSS);// - wss insertion loss
				WSS_out_Add=Math.round(WSS_out_Add*100.0)/100.0;
				WSS_in_Add=Math.round(WSS_in_Add*100.0)/100.0;			
				
				BA_in_Add=WSS_out_Add==INVALID_VALUE?INVALID_VALUE:(-20 +4.34*Math.log(1 * opticalDataList.get(i).getnWaves()));
				BA_in_Add=Math.round(BA_in_Add*100.0)/100.0;
				WSS_out_Add=Math.round(WSS_out_Add*100.0)/100.0;			
				
				WSS_attenuation_Add=BA_in_Add==INVALID_VALUE?INVALID_VALUE:(BA_in_Add-WSS_out_Add);
				BA_out_Add=BA_in_Add==INVALID_VALUE?INVALID_VALUE:(BA_in_Add+20);
				
				System.out.println("MPN_in_Add :"+MPN_in_Add);
				opticalPowerCdcRoadmAdd.setNetworkId(networkId);
				opticalPowerCdcRoadmAdd.setNodeId(opticalDataList.get(i).getNodeId());
				System.out.println("Direction Add :"+opticalDataList.get(i).getDirection());
				opticalPowerCdcRoadmAdd.setDirection(opticalDataList.get(i).getDirection());
				opticalPowerCdcRoadmAdd.setNoOfLambdas(opticalDataList.get(i).getnWaves());
				opticalPowerCdcRoadmAdd.setBaIn((float)BA_in_Add);
				opticalPowerCdcRoadmAdd.setBaOut((float)BA_out_Add);
				opticalPowerCdcRoadmAdd.setTxWssIn((float)WSS_in_Add);
				opticalPowerCdcRoadmAdd.setTxWssOut((float)WSS_out_Add);
				opticalPowerCdcRoadmAdd.setTxWssAttenuation((float)WSS_attenuation_Add);
				opticalPowerCdcRoadmAdd.setAddEdfaIn((float)EDFA_in_Add);
				opticalPowerCdcRoadmAdd.setAddEdfaOut((float)EDFA_out_Add);
				opticalPowerCdcRoadmAdd.setAddMcsIn((float)MCS_in_Add);
				opticalPowerCdcRoadmAdd.setAddMcsOut((float)MCS_out_Add);
				opticalPowerCdcRoadmAdd.setMpnLaunchPower((float)MPN_in_Add);
					
	            dbService.getCdcRoadmAddService().Insert(opticalPowerCdcRoadmAdd);
			}		
			
		}
		
		/*jsonFinalObj.put("NodeOpticalInfoArray", jsonOpticalDataArrayObj);
		
		List<Node> nodeList = dbService.getNodeService().FindAll(networkId);
		
		JSONArray jsonNodeDataArrayObj = new JSONArray();
		for(int i=0;i<nodeList.size();i++)
		{
			JSONObject jsonNodeDataObj = new JSONObject();
			jsonNodeDataObj.put("NodeType",nodeList.get(i).getNodeType());		
			jsonNodeDataObj.put("NodeId",nodeList.get(i).getNodeId());		
			jsonNodeDataArrayObj.add(jsonNodeDataObj);
		}
		jsonFinalObj.put("NodeArray", jsonNodeDataArrayObj);
		
		System.out.println("Final OpticalPower View json Object ::"+jsonFinalObj);	*/		

	}

}
