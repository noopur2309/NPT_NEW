package application.controller;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import application.MainMap;
import application.constants.MapConstants;
import application.model.Circuit;
import application.service.DbService;
import application.model.Circuitpotpmapping;

public class MapWebPOTPCircuitInput {
	
	@SuppressWarnings("static-access")
	public JSONArray POTPCircuitInput(DbService dbService,int NetworkId)
	{
		JSONArray FinalPOTPCircuitsArray = new JSONArray();
		
		
		
		List<Map<String,Object>> CircuitPOTPMaps = dbService.getCircuitPOTPMappingService().FindGroupedPotpcircuits(NetworkId, "G");
		
		for(int count = 0; count < CircuitPOTPMaps.size();count ++)
			
		{
			
			String CircuitSet = CircuitPOTPMaps.get(count).get("CircuitSet").toString();
			String Circuits[] = CircuitSet.split(",");
			JSONObject CircuitObject =  new JSONObject();
		    JSONArray ListOfPOTPobjects = new JSONArray();
		    int srcNodeId = 0;
			for(int index = 0; index < Circuits.length ; index++)
			{ 
			  JSONObject POTPObject =  new JSONObject();
			  Circuit POTPCircuit = dbService.getCircuitService().FindCircuit(NetworkId, Integer.parseInt(Circuits[index]));
			  srcNodeId = POTPCircuit.getSrcNodeId();
			  POTPObject.put("SrcNodeId", POTPCircuit.getSrcNodeId());
			  POTPObject.put("DestNodeId", POTPCircuit.getDestNodeId());
			  POTPObject.put("TrafficType", POTPCircuit.getTrafficType());	
			  POTPObject.put("RequiredTraffic", POTPCircuit.getRequiredTraffic());
			  POTPObject.put("ProtectionType", POTPCircuit.getProtectionType());
			  POTPObject.put("PathType", POTPCircuit.getPathType());
			  POTPObject.put("ColourPreference", POTPCircuit.getColourPreference());
			  POTPObject.put("NodeInclusion", POTPCircuit.getNodeInclusion());
			  
			  ListOfPOTPobjects.add(POTPObject); 
			
			}
			
			
			
			
		 
          	CircuitObject.put("QoS", MapConstants.I_ONE);
          	CircuitObject.put("Multiplier", MapConstants.I_ONE);
          	CircuitObject.put("CircuitId", CircuitSet);
          	CircuitObject.put("SrcNodeId",srcNodeId);
          	CircuitObject.put("DestNodeId", -1 );
          	CircuitObject.put("TrafficType", "OTU2-LOWER");
          	CircuitObject.put("RequiredTraffic", 10);
          	CircuitObject.put("ProtectionType", "UnProtected");
          	CircuitObject.put("ColourPreference", "Empty");
          	CircuitObject.put("PathType", "Disjoint");
          	CircuitObject.put("NodeInclusion","None");
          	CircuitObject.put("lineRate", 100);
          	CircuitObject.put("ProtectionMechanismType", "Y-Cable" );
          	CircuitObject.put("ChannelProtection", "No");
          	CircuitObject.put("State",MapConstants.GreenFieldState);
          	CircuitObject.put("ClientProtection", "Yes" );
          	CircuitObject.put("LambdaBlocking", "No" );
          	CircuitObject.put("VendorLabel", "CDOT" );
          	CircuitObject.put("POTPCircuits", ListOfPOTPobjects.toJSONString());
          	
             FinalPOTPCircuitsArray.add(CircuitObject);
          	
	     }
		
		
		
				
				
		return FinalPOTPCircuitsArray;			
	}
	
	

}
