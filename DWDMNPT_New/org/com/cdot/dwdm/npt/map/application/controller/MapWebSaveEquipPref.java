
/**
 * @author avinash
 */

package application.controller;

import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import application.MainMap;
import application.model.CardPreference;
import application.model.Circuit;
import application.service.CircuitProcessing;
import application.service.DbService;
import application.service.CardPreferenceService;


/**
 * @author hp
 * 
 * @brief This Service called when Operator wants to save card preferences using bom window
 *
 */
public class MapWebSaveEquipPref {

	/**
	 * 
	 * @param jsonStr
	 * @param dbService
	 * @throws SQLException 
	 */
	@SuppressWarnings("static-access")
	public int equipmentPrefSaveToDB(String jsonStr, DbService dbService) throws SQLException {
		
		/**Logger Start*/		
		 MainMap.logger = MainMap.logger.getLogger(MapWebSaveEquipPref.class.getName());
		
		MainMap.logger.info("--------------------------JSON Parsing Start---------------------------------------");
		
		 /**Delete Circuit & Demand & CircuitDemandMapping Info before any processing !*/
		 int networkId =  Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString());
		 ResourcePlanUtils resourcePlanningobj=new ResourcePlanUtils(dbService);
		 
		 /**Flush table*/
		 dbService.getCardPreferenceService().DeleteByNetworkId(networkId);
		 
		/**Actual Parsing*/
		JSONObject jsonObj = new JSONObject(jsonStr);
		try {
			JSONArray array = (JSONArray) jsonObj.get("prefData");//**get the cells array from json*//*
			MainMap.logger.info("Length of prefData array :- "+array.length());
			
			
			//**Loop through all the preferences*//*			
			for (int i = 0; i < array.length(); i++) {
				JSONObject mainJsonObj = array.getJSONObject(i);
												 	
				System.out.println("NetworkId :"+networkId+"NodeId :"+mainJsonObj.getInt("NodeId")+"cardFeature :"+mainJsonObj.get("cardFeature").toString()
						+"EqId :"+mainJsonObj.getInt("eqId")+"cardType :"+resourcePlanningobj.fgetCardTypeFromEId(mainJsonObj.getInt("eqId")));
				//**Prepare input for DB*//*
				CardPreference cardPrefObj = new CardPreference();
				cardPrefObj.setNetworkId(networkId);//**DBG => Temp value*//*
				cardPrefObj.setNodeId(mainJsonObj.getInt("NodeId"));
				cardPrefObj.setCardFeature(mainJsonObj.get("cardFeature").toString());
				cardPrefObj.setEquipmentId(mainJsonObj.getInt("eqId"));
				cardPrefObj.setCardType(resourcePlanningobj.fgetCardTypeFromEId(mainJsonObj.getInt("eqId")));
				cardPrefObj.setPreference(1);
				cardPrefObj.setPartPreference(1);
				//circuitObj.setSrcNodeId(mainJsonObj.getInt("Source Node"));	
				dbService.getCardPreferenceService().Insert(cardPrefObj);
			}
			return 1;
						
		}
		 catch (JSONException e) {
				e.printStackTrace();
			return 0;
		 }
	}

}
