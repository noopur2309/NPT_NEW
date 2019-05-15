package application.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.couchbase.client.java.document.json.JsonObject;

import application.MainMap;
import application.constants.MapConstants;
import application.constants.ResourcePlanConstants;
import application.model.AllocationExceptions;
import application.model.Circuit;
import application.model.Demand;
import application.model.EquipmentPreference;
import application.model.Node;
import application.model.ParametricPreference;
import application.model.Stock;
import application.service.DbService;

public class MapWebGetPreferences {
	
	/**
	 * 
	 * @param jsonStr
	 * @param dbService
	 * @throws SQLException 
	 */
	@SuppressWarnings("static-access")
	public JSONObject getNetworkPreferences(String jsonStr,DbService dbService) throws SQLException {

		/**Logger Start*/		
		MainMap.logger = MainMap.logger.getLogger(MapWebDetailedConfiguration.class.getName());

		MainMap.logger.info("--------------------------JSON Parsing Start---------------------------------------");

		/**Map From Common API*/ 
		HashMap<String, Object> networkInfoMap = MapWebCommonAPIs.NestedStaticClass.getNetworkInformation(dbService,jsonStr); 
		int networkId= (int)networkInfoMap.get(MapConstants.NetworkId); /**Network Id for common use*/ 
		
		JSONObject finalJsonObj=new JSONObject();
		
		/***************Node Array************************/ 
//		List<Node> nodes=dbService.getNodeService().FindAllNonILAsNetworkId(networkId);
		List<Node> nodes=dbService.getNodeService().FindAll(networkId);
		int nNodes = nodes.size();
		JSONArray nodeArr=new JSONArray();		
		for(int i=0;i<nNodes;i++)
		{
			JSONObject nodeObj=new JSONObject();
			nodeObj.put("text", nodes.get(i).getNodeId());
			nodeObj.put("value", nodes.get(i).getNodeId());
			nodeArr.add(nodeObj);
		}
		
		finalJsonObj.put("Nodes", nodeArr);
		
		/***************Node Array************************/ 
		JSONObject nodeDetailMainObj=new JSONObject();		
		for(int i=0;i<nNodes;i++)
		{
			JSONObject nodeDetailObj=new JSONObject();
			
			List <Demand> nodeDemandList=dbService.getDemandService().FindAllByNodeId(networkId, nodes.get(i).getNodeId());	
			JSONArray demandArr=new JSONArray();		
			for(Demand d:nodeDemandList){
				JSONObject demandObj=new JSONObject();
				demandObj.put("value", d.getDemandId());
				demandObj.put("text", d.getDemandId());
				demandArr.add(demandObj);
			}
			
			List <Circuit> nodeCircuitList=dbService.getCircuitService().FindAll(networkId, nodes.get(i).getNodeId());	
			JSONArray circuitArr=new JSONArray();		
			for(Circuit c:nodeCircuitList){
				JSONObject circuitObj=new JSONObject();
				circuitObj.put("value", c.getCircuitId());
				circuitObj.put("text", c.getCircuitId());
				circuitArr.add(circuitObj);
			}
			
			JSONArray directionArr=new JSONArray();		
			for(int d=1;d<=8;d++){
				JSONObject dirObj=new JSONObject();
				dirObj.put("value", d);
				dirObj.put("text", d);
				directionArr.add(dirObj);
			}
			
			nodeDetailObj.put("Demand",demandArr);
			nodeDetailObj.put("Circuit", circuitArr);
			nodeDetailObj.put("Direction",directionArr);
			nodeDetailMainObj.put(nodes.get(i).getNodeId(),nodeDetailObj);
		}
		
		finalJsonObj.put("NodeDetails", nodeDetailMainObj);
		
		/***************Default Prefs************************/ 
		JSONArray defaultPrefArr=new JSONArray();		
		List<EquipmentPreference> eqPrefList=dbService.getEquipmentPreferenceService().FindAll(networkId);
		for(EquipmentPreference obj:eqPrefList){
			JSONObject eqPrefObj=new JSONObject();
			eqPrefObj.put("NetworkId",obj.getNetworkId());
			eqPrefObj.put("NodeId", obj.getNodeId());
			eqPrefObj.put("Category", obj.getCategory());
			eqPrefObj.put("CardType", obj.getCardType());
			eqPrefObj.put("Priority", obj.getPreference());
			eqPrefObj.put("Redundancy", obj.getRedundancy());	
			defaultPrefArr.add(eqPrefObj);
		}
		finalJsonObj.put("DefaultPref",defaultPrefArr);
		
		/***************Stock Preferences************************/ 
		List<Stock> stockList=dbService.getStockService().Find(networkId);		
		JSONArray stockPrefArr=new JSONArray();		
		for(Stock obj:stockList){
			JSONObject prefObj=new JSONObject();
			prefObj.put("NetworkId", obj.getNetworkId());
			prefObj.put("NodeId", obj.getNodeId());
			prefObj.put("Category", obj.getCategory());
			prefObj.put("CardType", obj.getCardType());
			prefObj.put("Quantity", obj.getQuantity());
			prefObj.put("SerialNum", obj.getSerialNo());			
			stockPrefArr.add(prefObj);
		}
		
		finalJsonObj.put("StockPref", stockPrefArr);
		
		/***************Allocation Exceptions************************/ 
		List<AllocationExceptions> allocationExceptionsList=dbService.getAllocationExceptionsService().Find(networkId);
		JSONArray allocationExceptionsArr=new JSONArray();		
		if(allocationExceptionsList!=null){
			for(AllocationExceptions obj:allocationExceptionsList){
				System.out.println(obj.toString());
				JSONObject prefObj=new JSONObject();
				prefObj.put("NetworkId", obj.getNetworkId());
				prefObj.put("NodeId", obj.getNodeId());
				
				String []loc=obj.getLocation().toString().split("_");;
				prefObj.put("Rack", Integer.parseInt(loc[0]));
				
				prefObj.put("SubRack", Integer.parseInt(loc[1]));
				prefObj.put("Slot", Integer.parseInt(loc[2]));
				prefObj.put("CardType", obj.getCardType());
				prefObj.put("ExceptionType", obj.getType());
				prefObj.put("SerialNum", obj.getSerialNo());
				prefObj.put("Port",obj.getPort());
				
				allocationExceptionsArr.add(prefObj);
			}
		}		
		
		finalJsonObj.put("AllocationExceptions", allocationExceptionsArr);
		
		
		/***************Parametric Preferences************************/ 
		List<ParametricPreference> paramList=dbService.getParametricPreferenceService().FindAll(networkId);		
		JSONArray paramPrefArr=new JSONArray();		
		for(ParametricPreference obj:paramList){
			JSONObject prefObj=new JSONObject();
			prefObj.put("NetworkId", obj.getNetworkId());
			prefObj.put("NodeId", obj.getNodeId());
			prefObj.put("Category", obj.getCategory());
			prefObj.put("CardType", obj.getCardType());
			prefObj.put("Parameter", obj.getParameter());
			if(obj.getParameter().equals(ResourcePlanConstants.ParamDirection))
			prefObj.put("Value", MapConstants.fGetDirectionStrVal(obj.getValue()));
			else prefObj.put("Value", obj.getValue());
			prefObj.put("SerialNum", obj.getSerialNo());			
			paramPrefArr.add(prefObj);
		}
		
		finalJsonObj.put("ParamPref", paramPrefArr);
		
		/**Actual Parsing*/
		//System.out.println("finalJsonObj::"+finalJsonObj);
		
		return finalJsonObj;
	}

}
