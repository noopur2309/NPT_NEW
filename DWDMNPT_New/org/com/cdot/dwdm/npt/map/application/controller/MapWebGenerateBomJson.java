package application.controller;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import application.constants.MapConstants;
import application.model.Bom;
import application.model.CardPreference;
import application.model.Equipment;
import application.model.Node;
import application.service.DbService;

/**
 * @author hp
 * @brief  This Class provide the API to generate JSON which has three parts of DB as follows:
 *          PART I:   Node Data 
 *          PART II:  Bom[View] Data
 *          PART III: Equipment Data 
 *          
 *         Finally return the same object to the Main Cotroller 
 *          
 */
public class MapWebGenerateBomJson {

	/**
	 * 
	 * @param dbService
	 * @return JSONObject
	 */
	@SuppressWarnings("unchecked")
	public JSONObject MapWebGenerateBomJsonReq(DbService dbService) {
		
		System.out.println("Inside MapWebGenerateBomJsonReq and Going to Return the JSon for BOM View.. ");
		int networkId=Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString());
		/**
		 * JSON Preparation PART I: Node Data 
		 */
		JSONObject jsonFinalObj = new JSONObject();
		List<Node> nodes = dbService.getNodeService().FindAll(networkId);
		
		/**DB Call to get number of nodes*/
		int nNodes 		= nodes.size();		
		jsonFinalObj.put("NodeCount",nNodes);
					
			
		JSONArray jsonArrayNodeObj = new JSONArray();
		
		for (int i = 0; i < nodes.size(); i++) {			
			
			jsonArrayNodeObj.add(nodes.get(i).getNodeId());					
		}
						
		jsonFinalObj.put("NodeData",jsonArrayNodeObj);
				
		System.out.println("After JSON Preparation PART I: jsonFinalObj Data  :- "+jsonFinalObj);
		
		
		/**
		 * JSON Preparation PART II: Bom[View] Data 
		 */		
		JSONObject jsonIndexBomViewObj = new JSONObject();
		
		
		List<Bom> bomData = dbService.getViewServiceRp().FindBoM(1);/**********Temp*********/
		
		
		int nodeId = MapConstants.I_MINUS_ONE;
		JSONArray jsonArrayBomViewObj = new JSONArray();
		System.out.println("bomData Size : "+ bomData.size());
		
		for (int i = 0; i < bomData.size(); i++) {		
			
			JSONObject jsonBomRowObj = new JSONObject();
			
			
			int currentId = bomData.get(i).getNodeId();
			System.out.println("bomData.get(i).getNodeId()" + bomData.get(i).getNodeId());
			jsonBomRowObj.put("NodeId", bomData.get(i).getNodeId());
			jsonBomRowObj.put("StationName", bomData.get(i).getStationName());
			jsonBomRowObj.put("SiteName", bomData.get(i).getSiteName());
			jsonBomRowObj.put("Name", bomData.get(i).getName());
			jsonBomRowObj.put("Quantity", bomData.get(i).getQuantity());
			jsonBomRowObj.put("Price", bomData.get(i).getPrice());			
			jsonBomRowObj.put("TotalPrice", bomData.get(i).getTotalPrice());
			jsonBomRowObj.put("PartNo", bomData.get(i).getPartNo());
			jsonBomRowObj.put("PowerConsumption", bomData.get(i).getPowerConsumption()*bomData.get(i).getQuantity());
			jsonBomRowObj.put("TypicalPowerConsumption", 50*bomData.get(i).getQuantity()); //to be updated 
			jsonBomRowObj.put("RevisionCode", bomData.get(i).getRevisionCode());
			jsonBomRowObj.put("Category", bomData.get(i).getCategory());	
			jsonArrayBomViewObj.add(jsonBomRowObj);
			
			System.out.println(" Value of i:"+i+"jsonBomRowObj ::\n"+jsonBomRowObj);
			/*
			if(currentId != nodeId && i != MapConstants.I_ZERO){*//**Node Id changed*//*
				System.out.println("first case with : - " + nodeId);
				jsonIndexBomViewObj.put(nodeId, jsonArrayBomViewObj);
				System.out.println("first case with : jsonIndexBomViewObj "+jsonIndexBomViewObj);
				nodeId = MapConstants.I_MINUS_ONE;
				//jsonArrayBomViewObj.clear();
			   
				jsonArrayBomViewObj.add(jsonBomRowObj);
				
			}else{*//**Node Id same, keep on adding the array*//*			
				System.out.println("else");
				jsonArrayBomViewObj.add(jsonBomRowObj);
				System.out.println("jsonArrayBomViewObj : "+jsonArrayBomViewObj);
			}
			
			if(nodeId == MapConstants.I_MINUS_ONE){*//**Update nodeId with the currentId after adding jsonIndexBomViewObj*//*
				System.out.println("nodeId == MapConstants.I_MINUS_ONE: nodeId " +nodeId + " and currentId : - "+currentId );
				nodeId = currentId;
				System.out.println("jsonIndexBomViewObj NodeId Change\n"+jsonIndexBomViewObj);
			}
						
			if(	i == (bomData.size()-1) ){*//**Final Case where jsonIndexBomViewObj has to update since no more changes are expected*//*
				System.out.println("Final Case: - "+ currentId);
				jsonIndexBomViewObj.put(currentId, jsonArrayBomViewObj);
				System.out.println("jsonIndexBomViewObj Final Case\n"+jsonIndexBomViewObj);
			}*/
			
//			System.out.println("BOM Size : "+ bomData.size() + " and I : "+i + "jsonIndexBomViewObj : "+ jsonIndexBomViewObj
//					+" bomData.get(i).getNodeId() " + bomData.get(i).getNodeId());
		}
		
		//jsonIndexBomViewObj.put("bomViewData",jsonArrayBomViewObj);
		jsonFinalObj.put("bomViewData",jsonArrayBomViewObj);
		System.out.println("After JSON Preparation PART II: jsonFinalObj Data  :- "+jsonFinalObj);
		
		
		/**
		 * JSON Preparation PART III: Equipment Data 
		 */	
		List<Equipment>  equipment 	= dbService.getEquipmentService().FindAll();
		
		JSONArray jsonArrayEquipmentObj = new JSONArray();
		System.out.println("equipment Size : "+ equipment.size());
		for (int i = 0; i < equipment.size(); i++) {		
				
			JSONObject jsonEquipmentRowObj = new JSONObject();			
					
			jsonEquipmentRowObj.put("Name", equipment.get(i).getName());
			jsonEquipmentRowObj.put("PartNo", equipment.get(i).getPartNo());
			jsonEquipmentRowObj.put("PowerConsumption", equipment.get(i).getPowerConsumption());
			jsonEquipmentRowObj.put("SlotSize", equipment.get(i).getSlotSize());
			jsonEquipmentRowObj.put("Details", equipment.get(i).getDetails());
			jsonEquipmentRowObj.put("Price", equipment.get(i).getPrice());
			jsonEquipmentRowObj.put("RevisionCode", equipment.get(i).getRevisionCode());
			jsonEquipmentRowObj.put("Category", equipment.get(i).getCategory());
			
			jsonArrayEquipmentObj.add(jsonEquipmentRowObj);
					
		}
						
		jsonFinalObj.put("equipmentDbData",jsonArrayEquipmentObj);
		
		
		/**
		 * JSON Preparation PART IV: Equipment Preference Data 
		 */	
		List<CardPreference>  equipmentPref 	= dbService.getCardPreferenceService().FindAll();
		
		JSONArray jsonArrayEquipmentPrefObj = new JSONArray();
		System.out.println("equipment preference Size : "+ equipmentPref.size());
		for (int i = 0; i < equipmentPref.size(); i++) {		
				
			JSONObject jsonEquipmentPrefRowObj = new JSONObject();			
					
			jsonEquipmentPrefRowObj.put("NodeId", equipmentPref.get(i).getNodeId());
			jsonEquipmentPrefRowObj.put("CardFeature", equipmentPref.get(i).getCardFeature());
			jsonEquipmentPrefRowObj.put("CardType", equipmentPref.get(i).getCardType());
			jsonEquipmentPrefRowObj.put("EquipmentId", equipmentPref.get(i).getEquipmentId());
			jsonEquipmentPrefRowObj.put("Preference", equipmentPref.get(i).getPreference());
			//jsonEquipmentRowObj.put("Partpreference", equipmentPref.get(i).getPrice());
			
			jsonArrayEquipmentPrefObj.add(jsonEquipmentPrefRowObj);
					
		}
						
		jsonFinalObj.put("equipmentPrefDbData",jsonArrayEquipmentPrefObj);
		//System.out.println("After JSON Preparation PART III: jsonFinalObj Data  :- "+jsonFinalObj);
		
		
		return jsonFinalObj;			
	
	}
}
