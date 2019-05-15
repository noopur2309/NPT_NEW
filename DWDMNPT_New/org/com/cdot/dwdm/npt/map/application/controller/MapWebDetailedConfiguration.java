package application.controller;

import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import application.MainMap;
import application.constants.MapConstants;
import application.model.Node;
import application.model.TpnPortInfo;
import application.service.CircuitProcessing;
import application.service.DbService;

/**
 * @author hp
 * 
 * @brief This Service called when Operator wants to save the mpn configuration 
 *
 */
public class MapWebDetailedConfiguration {


	/**
	 * 
	 * @param jsonStr
	 * @param dbService
	 * @throws SQLException 
	 */
	@SuppressWarnings("static-access")
	public JSONObject getDetailedConfiguration(String jsonStr,DbService dbService) throws SQLException {

		/**Logger Start*/		
		MainMap.logger = MainMap.logger.getLogger(MapWebDetailedConfiguration.class.getName());

		MainMap.logger.info("--------------------------JSON Parsing Start---------------------------------------");

		/**Map From Common API*/ 
		HashMap<String, Object> networkInfoMap = MapWebCommonAPIs.NestedStaticClass.getNetworkInformation(dbService,jsonStr); 
		int networkId= (int)networkInfoMap.get(MapConstants.NetworkId); /**Network Id for common use*/ 
		
		String cardType;
		int nodeId;
		JSONObject jsonReturnStr=null;
		
		/**Actual Parsing*/
		System.out.println(jsonStr);
		org.json.JSONObject jsonReqObj=new org.json.JSONObject(jsonStr);
		org.json.JSONObject ReqDetailsObj=(org.json.JSONObject)jsonReqObj.get("requestDetails");
		cardType=ReqDetailsObj.getString("CardType")/*"MPN"*/;
		nodeId=ReqDetailsObj.getInt("NodeId")/*1*/;

		System.out.println("CardtType :"+cardType +" NodeId:"+nodeId);

		if(cardType.equalsIgnoreCase(MapConstants.cardTypeMpn))
		{			
			jsonReturnStr=fGetMpnDetailedConfiguration(nodeId,networkId,dbService);
		}
		else if(cardType.equalsIgnoreCase(MapConstants.cardTypeWss))
		{

		}	

		return jsonReturnStr;
	}


	/**
	 * 
	 * @param jsonStr
	 * @param dbService
	 * @throws SQLException 
	 */
	@SuppressWarnings("static-access")
	public int setDetailedConfiguration(String jsonStr,DbService dbService) throws SQLException {

		/**Logger Start*/		
		MainMap.logger = MainMap.logger.getLogger(MapWebDetailedConfiguration.class.getName());

		MainMap.logger.info("--------------------------JSON Parsing Start---------------------------------------");
		
		/**Map From Common API*/ 
		HashMap<String, Object> networkInfoMap = MapWebCommonAPIs.NestedStaticClass.getNetworkInformation(dbService,jsonStr); 
		int networkId= (int)networkInfoMap.get(MapConstants.NetworkId); /**Network Id for common use*/ 
				
		int result=0;
		String cardType;
		/**Actual Parsing*/
		System.out.println(jsonStr);
		org.json.JSONObject jsonReqDataObject=new org.json.JSONObject(jsonStr);
		org.json.JSONObject mpnSetConfigDataObj=jsonReqDataObject.getJSONObject("mpnConfigUpdateData");
		cardType=mpnSetConfigDataObj.getJSONObject("SetInfo").getString("CardType")/*"MPN"*/;

		System.out.println("CardtType :"+cardType );

		if(cardType.equalsIgnoreCase(MapConstants.cardTypeMpn))
		{			
			result=fSetMpnDetailedConfigurationToDB(networkId,mpnSetConfigDataObj,dbService);
		}
		else if(cardType.equalsIgnoreCase(MapConstants.cardTypeWss))
		{

		}	

		return result;
	}



	/**
	 * 
	 * @param jsonStr
	 * @param dbService
	 * @throws SQLException 
	 */
	@SuppressWarnings("static-access")
	public JSONObject fGetMpnDetailedConfiguration(int nodeId,int networkId,DbService dbService) throws SQLException {

		/**Logger Start*/		
		MainMap.logger = MainMap.logger.getLogger(MapWebDetailedConfiguration.class.getName());

		MainMap.logger.info("--------------------------JSON Parsing Start---------------------------------------");

		/**Delete Circuit & Demand & CircuitDemandMapping Info before any processing !*/
		//int networkId =  Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString());
		JSONObject jsonFinalObj = new JSONObject();	

		try {

			List<Map<String, Object>> circuitDataList=dbService.getCardInfoService().FgetTpnDataPerCircuit_cf(networkId, nodeId);

			System.out.println("List Size: "+circuitDataList.size()+ circuitDataList.get(0).get("CircuitId"));
			System.out.println(circuitDataList.toString());
			JSONArray jsonCircuitInfoArrayObj = new JSONArray();
			/**Loop through all the Nodes*/			
			for (int i = 0; i < circuitDataList.size(); i++) {
				JSONObject jsonCircuitInfoObj = new JSONObject();

				//jsonCircuitInfoObj.put("DemandId", circuitDataList.get(i).get("DemandId").toString());	
				jsonCircuitInfoObj.put("Status", circuitDataList.get(i).get("Status").toString());	
				jsonCircuitInfoObj.put("CircuitId", circuitDataList.get(i).get("CircuitId").toString());	
				jsonCircuitInfoObj.put("SrcNodeId", circuitDataList.get(i).get("SrcNodeId").toString());	
				jsonCircuitInfoObj.put("DestNodeId", circuitDataList.get(i).get("DestNodeId").toString());	
				//jsonCircuitInfoObj.put("TrafficType", circuitDataList.get(i).get("TrafficType").toString());	
				jsonCircuitInfoObj.put("NodeKey", circuitDataList.get(i).get("NodeKey").toString());	
				jsonCircuitInfoObj.put("Rack", circuitDataList.get(i).get("Rack"));		
				jsonCircuitInfoObj.put("SubRack", circuitDataList.get(i).get("Sbrack"));		
				jsonCircuitInfoObj.put("Card", circuitDataList.get(i).get("Card"));		
				jsonCircuitInfoObj.put("CardType", circuitDataList.get(i).get("CardType"));		
				//jsonCircuitInfoObj.put("CardSubType", circuitDataList.get(i).get("CardSubType"));		
				jsonCircuitInfoObj.put("PortId", circuitDataList.get(i).get("Port"));		
				jsonCircuitInfoObj.put("ProtectionSubType", circuitDataList.get(i).get("ProtectionSubType"));		
				//jsonCircuitInfoObj.put("NodeId", circuitDataList.get(i).get("NodeId"));		
				jsonCircuitInfoObj.put("OperatorSpecific", circuitDataList.get(i).get("OperatorSpecific"));		
				jsonCircuitInfoObj.put("TCMActValue", circuitDataList.get(i).get("TCMActValue"));		
				jsonCircuitInfoObj.put("TxTCMPriority", circuitDataList.get(i).get("TxTCMPriority"));	
				jsonCircuitInfoObj.put("TxTCMMode", circuitDataList.get(i).get("TxTCMMode"));
				jsonCircuitInfoObj.put("IsRevertive", circuitDataList.get(i).get("IsRevertive"));		
				jsonCircuitInfoObj.put("FecType", circuitDataList.get(i).get("FecType"));		
				jsonCircuitInfoObj.put("FecStatus", circuitDataList.get(i).get("FecStatus"));		
				jsonCircuitInfoObj.put("TxSegment", circuitDataList.get(i).get("TxSegment"));		
				jsonCircuitInfoObj.put("TimDectMode", circuitDataList.get(i).get("TimDectMode"));		
				jsonCircuitInfoObj.put("TCMActStatus", circuitDataList.get(i).get("TCMActStatus"));		
				jsonCircuitInfoObj.put("GccType", circuitDataList.get(i).get("GccType"));		
				jsonCircuitInfoObj.put("GccStatus", circuitDataList.get(i).get("GccStatus"));		
				jsonCircuitInfoObj.put("GccValue", circuitDataList.get(i).get("GccValue"));	

				jsonCircuitInfoArrayObj.add(jsonCircuitInfoObj);

			}

			jsonFinalObj.put("CircuitData",jsonCircuitInfoArrayObj);

		}
		catch (JSONException e) {
			e.printStackTrace();

		}


		return jsonFinalObj;
	}


	/**
	 * 
	 * @param jsonStr
	 * @param dbService
	 * @throws SQLException 
	 */
	@SuppressWarnings("static-access")
	public int fSetMpnDetailedConfigurationToDB(int networkId,org.json.JSONObject jsonReqDataObject, DbService dbService) throws SQLException {

		//**Logger Start*//	
		MainMap.logger = MainMap.logger.getLogger(MapWebDetailedConfiguration.class.getName());

		MainMap.logger.info("--------------------------JSON Parsing Start---------------------------------------");

		//**Delete Circuit & Demand & CircuitDemandMapping Info before any processing !*//
		//int networkId =  Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString());

		try {
			org.json.JSONArray array= (org.json.JSONArray)jsonReqDataObject.get("ConfigurationData");//**get the cells array from json*//
			MainMap.logger.info("Length of configuration array :- "+array.length());

			//**Loop through all the Nodes*//			
			for (int i = 0; i < array.length(); i++) {
				org.json.JSONObject mainJsonObj = (org.json.JSONObject)array.get(i);

				///MainMap.logger.info("Id :- "+ mainJsonObj.get("Id").toString()); 
				MainMap.logger.info("Source Node :- "+ mainJsonObj.get("Source Node").toString()); 
				MainMap.logger.info("Dest Node :- "+ mainJsonObj.get("Dest Node").toString()); 
				MainMap.logger.info("Port :- "+ mainJsonObj.get("Port").toString()); 
				MainMap.logger.info("Tpn NodeId :- "+ mainJsonObj.get("Configured Tpn").toString());				
				MainMap.logger.info("Tpn Info(R-S-C) :- "+ mainJsonObj.get("Tpn Location(R-S-C)").toString());
				MainMap.logger.info("Operator Specific :- "+ mainJsonObj.get("Operator Specific").toString());
				MainMap.logger.info("TxTCM Mode. :- "+ mainJsonObj.get("TxTCM Mode").toString());
				MainMap.logger.info("TCM Act Value :- "+ mainJsonObj.get("TCM Act Value").toString());
				MainMap.logger.info("TxTCM Priority :- "+ mainJsonObj.get("TxTCM Priority").toString());
				String[] tpnInfo=mainJsonObj.get("Tpn Location(R-S-C)").toString().split("-");
				int rack=Integer.parseInt(tpnInfo[0]);
				int sbrack=Integer.parseInt(tpnInfo[1]);
				int card=Integer.parseInt(tpnInfo[2]);
				int nodeId;
				if(mainJsonObj.get("Configured Tpn").toString().equalsIgnoreCase("Source Node"))
					nodeId=Integer.parseInt(mainJsonObj.get("Source Node").toString());
				else nodeId=Integer.parseInt(mainJsonObj.get("Dest Node").toString());

				String OperatorSpecific=mainJsonObj.get("Operator Specific").toString();
				int TxTCMMode=Integer.parseInt(mainJsonObj.get("TxTCM Mode").toString());
				int TCMActValue=Integer.parseInt(mainJsonObj.get("TCM Act Value").toString());
				String TxTCMPriority=mainJsonObj.get("TxTCM Priority").toString();
				int stream=1;
				int circuitId=Integer.parseInt(mainJsonObj.get("Circuit").toString());

				//**DB Call*//
				dbService.getTpnPortInfoService().Update(networkId, nodeId, rack, sbrack, card,stream, OperatorSpecific, TxTCMMode, TCMActValue,TxTCMPriority);

				stream=0;		
				int destNodeId=Integer.parseInt(mainJsonObj.get("Dest Node").toString());
				int nextCircuitId=0,prevCircuitId=0;
				org.json.JSONObject nextJsonObj=null,prevJsonObj=null;
				if(i+1<array.length())
				{
					nextJsonObj = (org.json.JSONObject)array.get(i+1);
					nextCircuitId=Integer.parseInt(nextJsonObj.get("Circuit").toString());
				}
				if(i-1>0)
				{
					prevJsonObj = (org.json.JSONObject)array.get(i-1);
					prevCircuitId=Integer.parseInt(prevJsonObj.get("Circuit").toString());
				}

				if(destNodeId==nodeId)
				{
					nodeId=Integer.parseInt(mainJsonObj.get("Source Node").toString());
				}
				else
				{
					nodeId=Integer.parseInt(mainJsonObj.get("Dest Node").toString());
				}

				if(circuitId==nextCircuitId)
				{
					tpnInfo=nextJsonObj.get("Tpn Info(R-S-C)").toString().split("-");
					rack=Integer.parseInt(tpnInfo[0]);
					sbrack=Integer.parseInt(tpnInfo[1]);
					card=Integer.parseInt(tpnInfo[2]);
					dbService.getTpnPortInfoService().Update(networkId, nodeId, rack, sbrack, card,stream, OperatorSpecific, TxTCMMode, TCMActValue,TxTCMPriority);

				}
				else if(circuitId==prevCircuitId)
				{
					tpnInfo=prevJsonObj.get("Tpn Info(R-S-C)").toString().split("-");
					rack=Integer.parseInt(tpnInfo[0]);
					sbrack=Integer.parseInt(tpnInfo[1]);
					card=Integer.parseInt(tpnInfo[2]);
					dbService.getTpnPortInfoService().Update(networkId, nodeId, rack, sbrack, card,stream, OperatorSpecific, TxTCMMode, TCMActValue,TxTCMPriority);
				}

			}
			return 1;
		}
		catch (JSONException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 
	 * @param jsonStr
	 * @param dbService
	 * @throws SQLException 
	 */
	@SuppressWarnings("static-access")
	public JSONObject getMpnNodes(DbService dbService,String jsonStr) throws SQLException, UnknownHostException  {


		System.out.println("Inside getMpnNodeList");


		/**JSON Preparation Call*/
		//int networkId =  Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString());

		/**Map From Common API*/ 
		HashMap<String, Object> networkInfoMap = MapWebCommonAPIs.NestedStaticClass.getNetworkInformation(dbService,jsonStr); 
		int networkId= (int)networkInfoMap.get(MapConstants.NetworkId); /**Network Id for common use*/ 

		/**
		 * JSON Preparation PART I: Node Data 
		 */
		JSONObject jsonFinalObj = new JSONObject();
		List<TpnPortInfo> nodes=dbService.getTpnPortInfoService().FindAll(networkId);

		JSONArray jsonArrayNodeObj = new JSONArray();

		for (int i = 0; i < nodes.size(); i++) {	
			JSONObject jsonNodeObj = new JSONObject();
			jsonNodeObj.put("NodeId", nodes.get(i).getNodeId());
			jsonArrayNodeObj.add(jsonNodeObj);					
		}

		jsonFinalObj.put("NodeData",jsonArrayNodeObj);

		System.out.println("After JSON Preparation : jsonFinalObj Data  :- "+jsonFinalObj);		

		System.out.println("getMpnNodeList Request Processed");

		return jsonFinalObj;

	}

}
