package application.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.couchbase.client.java.document.json.JsonObject;

import application.MainMap;
import application.constants.MapConstants;
import application.model.DemandMapping;
import application.model.Link;
import application.model.MapData;
import application.model.Network;
import application.model.Node;
import application.model.Topology;
import application.service.DbService;

/**
 * @author avinash
 * 
 * @brief This Parser Called on delete Circuit/Demand from Circuit view in case of Brown 
 * Field Network. 
 *
 */
/*@Service*/

public class MapWebDeleteCircuitDemand {
	
	/**
	 * 
	 * @param jsonStr
	 * @param dbService
	 * @throws SQLException 
	 */
	@SuppressWarnings("static-access")
	public void circuitDelete(String jsonStr, DbService dbService) throws SQLException {
		
		/**Logger Start*/		
		 MainMap.logger = MainMap.logger.getLogger(MapWebDeleteCircuitDemand.class.getName());
		
		MainMap.logger.info("--------------------------JSON Parsing Start---------------------------------------");
		
		JSONObject jsonDataObj = new JSONObject(jsonStr);	
		
		/**Map From Common API*/ 
		HashMap<String, Object> networkInfoMap = MapWebCommonAPIs.NestedStaticClass.getNetworkInformation(dbService, jsonStr);		  
		 
		int networkId = (int) networkInfoMap.get(MapConstants.NetworkId);		
		int greenFieldNetworkId=MapConstants.I_ZERO;
		if(((String)networkInfoMap.get(MapConstants.NetworkType)).equalsIgnoreCase(MapConstants.BrownField)){ 
			greenFieldNetworkId=(int)networkInfoMap.get(MapConstants.GreenFieldId); /**Network Id for common use*/        
		}
		
		//int circuitObjCount = dbService.getCircuitService().Count(networkId);
		//if(circuitObjCount==MapConstants.I_ZERO)
		//	networkId=greenFieldNetworkId;
					
		try {
			JSONArray circuitRowsArray = (JSONArray) jsonDataObj.get("circuitRows");/**get the cells array from json*/
			MainMap.logger.info("Length of circuitRows array :- "+circuitRowsArray.length());
			
			/**Loop through all the Nodes*/			
			for (int i = 0; i < circuitRowsArray.length(); i++) {
				JSONObject mainJsonObj = circuitRowsArray.getJSONObject(i);
				int circuitId=Integer.parseInt(mainJsonObj.get("CircuitId").toString());					
				System.out.println("Deleting Circuit Id::"+circuitId);
				dbService.getCircuitService().DeleteCircuit(networkId, circuitId);
				dbService.getCircuitDemandMappingService().DeleteCircuitDemandMapping(networkId, circuitId);
								
			}
			
			int demandId=Integer.parseInt(circuitRowsArray.getJSONObject(0).get("DemandId").toString());
			int demandMappingCount=dbService.getCircuitDemandMappingService().FindDemandCount(networkId, demandId);
			System.out.println("demandMappingCount--------"+demandMappingCount);
		  
			MapWebCircuitInputSave mWWebCircuitInputSave = new MapWebCircuitInputSave();
			mWWebCircuitInputSave.deleteDeletedCircuitRelatedMapping(networkInfoMap,dbService);
			
			  if(demandMappingCount==0)
			    {
			    	dbService.getDemandService().DeleteDemand(networkId, demandId);
			    	dbService.getNetworkRouteService().DeleteNetworkRoute(networkId, demandId);
			    	dbService.getLambdaLspInformationSerivce().DeleteLambdaLspInformation(networkId, demandId);
			    }
				
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	

}
