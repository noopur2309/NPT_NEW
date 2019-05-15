/**
 * 
 */
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import application.MainMap;
import application.constants.MapConstants;
import application.model.Circuit;
import application.model.Circuit10gAgg;
import application.model.CircuitDemandMapping;
import application.model.Demand;
import application.model.DemandMapping;
import application.model.NetworkRoute;
import application.service.CircuitProcessing;
import application.service.DbService;
import application.model.Node;
import application.model.Circuitpotpmapping;
import application.dao.Circuitpotpmappingdao;
/**
 * @author hp
 * 
 * @brief This Service called when Operator wants to save the configured circuit
 *        before executing the RWA
 *
 */
public class MapWebCircuitInputSave {

	/**
	 * 
	 * @param jsonStr
	 * @param dbService
	 * @throws SQLException
	 */
	@SuppressWarnings("static-access")
	public int circuitInputSaveToDB(String jsonStr, DbService dbService) throws SQLException {
		/** Actual Parsing */
		try {
		
			/** Logger Start */
			MainMap.logger = MainMap.logger.getLogger(MapWebCircuitInputSave.class.getName());

			MainMap.logger.info("--------------------------JSON Parsing Start---------------------------------------");

			JSONObject jsonMapDataObj = new JSONObject(jsonStr);

			/** Map From Common API */
			HashMap<String, Object> networkInfoMap = MapWebCommonAPIs.NestedStaticClass.getNetworkInformation(dbService,
					jsonStr);

			JSONArray array = (JSONArray) jsonMapDataObj.get("circuitRows");/** get the cells array from json */
			MainMap.logger.info("Length of circuitRows array :- " + array.length());		

			    		
			int networkId = (int) networkInfoMap.get(MapConstants.NetworkId);
		
			Multimap<Integer, Circuit> demandIdCircuitArray = ArrayListMultimap.create();

			System.out.println("networkInfoMap : " + networkInfoMap);

			/**
			 * Find out the Circuit Processing Mode through flag : 				Flag Value 
			 *							 a) Normal Circuit Processing Mode <----> 0  [In GF & BF as well]
			 * 							 b) Demand Modification Mode       <----> 1
			 */
			int flagForCircuitProcessingMode = jsonMapDataObj.getInt("Mode");

			if (flagForCircuitProcessingMode == MapConstants.I_ZERO) {
				/**
				 * All Delete Call for Present mapping
				 */
				dbService.getCircuitService().DeleteCircuit(networkId);
				dbService.getCircuit10gAggService().DeleteCircuit10GAgg(networkId);
				dbService.getDemandService().DeleteDemand(networkId);
				dbService.getDemandService().DeleteInMemoryDemand(networkId);
				dbService.getCircuitDemandMappingService().DeleteCircuitDemandMapping(networkId);
				dbService.getCircuitService().DeleteInMemoryCircuit(networkId);
				dbService.getNetworkRouteService().DeleteNetworkRoute(networkId);
			}		

			
			/********************************** CIRCUIT INSERTION : START ***********************************************/
            int QoS = 0;
            int potpCircuitId = 0;
            
			/** Loop through all the Nodes */
			for (int i = 0; i < array.length(); i++) {
			
				JSONObject mainJsonObj = array.getJSONObject(i);
			
				/**
				 * In Case of GreenField Circuits which are converted into Brownfield,
				 * it Does Contain the actual Circuit Id.
				 * Whereas For BrownField It will have -1 so back end has to generate the ID for it.
				 */

				/** DB Call */
				MainMap.logger.info("Insert Same circuit for :- " + mainJsonObj.getInt("Multiplier"));

				int groupedCircuitId = 0;
				System.out.println("MainJSOn " +mainJsonObj);

				JSONArray groupedCircuitIdarray = (JSONArray) mainJsonObj.get("CircuitIdArray");
				MainMap.logger.info("groupedCircuitIdarray : " + groupedCircuitIdarray);

				for (int multiplier = 0; multiplier < mainJsonObj.getInt("Multiplier"); multiplier++) {
					MainMap.logger.info("multiplier :- " + multiplier);

					/** Prepare input for DB */
					Circuit circuitObj = new Circuit();
					circuitObj.setNetworkId(networkId);
					/// circuitObj.setCapacity(mainJsonObj.get("Capacity").toString());
					circuitObj.setSrcNodeId(mainJsonObj.getInt("Source Node"));
					circuitObj.setDestNodeId(mainJsonObj.getInt("Dest Node"));
					if(mainJsonObj.get("QoS").equals(true))
					{
					circuitObj.setQoS(MapConstants.I_ONE);
					 QoS = MapConstants.I_ONE;
					}
					else 
					{ 
						circuitObj.setQoS(MapConstants.I_ZERO);	
					 QoS = MapConstants.I_ZERO;
					}
					circuitObj.setProtectionType(mainJsonObj.get("Protection Type").toString());
					circuitObj.setTrafficType(mainJsonObj.get("Service Type").toString());
					circuitObj.setColourPreference(mainJsonObj.get("Color Preference").toString());
					circuitObj.setClientProtectionType(mainJsonObj.get("Protection Mechanism").toString());
					circuitObj.setChannelProtection(mainJsonObj.get("Channel Protection").toString());					
					circuitObj.setPathType(mainJsonObj.get("Path Type").toString());
					
					circuitObj.setLineRate(mainJsonObj.get("Line Rate").toString());

					System.out.println("**************** Client rate :"+mainJsonObj.get("Client Rate").toString());
					if(MapConstants.Capacity0GAgg.equalsIgnoreCase(mainJsonObj.get("Client Rate").toString())){
						circuitObj.setRequiredTraffic(MapConstants.LineRate10GAgg);
					}
					else{
						circuitObj.setRequiredTraffic(Float.parseFloat(mainJsonObj.get("Client Rate").toString()));
					}
						
					
					/**Newly Added parameter : 22 Feb, 2018 */
					circuitObj.setVendorlabel(mainJsonObj.get("Vendor Label").toString());
					circuitObj.setClientProtection(mainJsonObj.get("Client Protection").toString());
					circuitObj.setLambdaBlocking(mainJsonObj.get("Block Lambda").toString());
					circuitObj.setNodeInclusion(mainJsonObj.get("Node Inclusion").toString());

					try {
						groupedCircuitId = Integer.parseInt(groupedCircuitIdarray.getString(multiplier));
					}
					catch (JSONException e) {
						groupedCircuitId = MapConstants.I_MINUS_ONE;
					}	
					System.out.println("groupedCircuitId :  " + groupedCircuitId);

					circuitObj.setCircuitId(groupedCircuitId);
					
				if(QoS!=1)
                   {
					/**Insert Circuit Call based on Circuit Id Value : -1 or Actual one*/
					int circuitId = dbService.getCircuitService().InsertCircuit(circuitObj, networkInfoMap);
					
					circuitObj.setCircuitId(circuitId);
					System.out.println(" Circuit ID Final : " + circuitId);
                   
					/**10G AggregatorCircuits (if there)*/
					try {

						if(mainJsonObj.has("AggregatorCircuits")){
						
							JSONArray aggregatorCircuitsArray  = (JSONArray) mainJsonObj.get("AggregatorCircuits");
							MainMap.logger.debug("Aggregators Circuits: "+ aggregatorCircuitsArray);
						
							if(MapConstants.I_ZERO < aggregatorCircuitsArray.length()){

								/** Get 10G Agg. Circuit Object */
								for(int aggCirCount=0;  aggCirCount<aggregatorCircuitsArray.length(); aggCirCount++){
								
									JSONObject aggCirObj = aggregatorCircuitsArray.getJSONObject(aggCirCount);
									MainMap.logger.debug("Aggregators Circuit Object : "+ aggCirObj);
									
									/** Iterate through multiplier times */
									for(int aggCirMultiplierCnt=0; aggCirMultiplierCnt<aggCirObj.getInt("Multiplier"); aggCirMultiplierCnt++){
										
										Circuit10gAgg  circuit10gAggObj = new Circuit10gAgg();
										circuit10gAggObj.setNetworkId(networkId);
										circuit10gAggObj.setCircuitId(circuitId);
										circuit10gAggObj.setLineRate(MapConstants.Traffic10GAgg);/**For 10G Agg, fix linerate */
										circuit10gAggObj.setRequiredTraffic(Float.parseFloat(aggCirObj.get("ClientRate").toString()));
										circuit10gAggObj.setTrafficType(aggCirObj.getString("ServiceType"));
                                        
										/**Insert Circuit Call based on Circuit Id Value : -1 or Actual one*/
										dbService.getCircuit10gAggService().InsertCircuit10gAgg(circuit10gAggObj, networkInfoMap);
									}
								}
							}						

						}
						
					}
					
					
					
					 catch (Exception e) {
							e.printStackTrace();
						}
						

					
                   }
                   
                   
                   
                   else 
                   {    
                	   
                	      potpCircuitId = dbService.getCircuitPOTPMappingService().GetNextPotpCircuitIdForNetwork(networkId);
                	   
                	     if( !circuitObj.getTrafficType().equals("OTU2-LOWER"))
					
                	     {   
                	    	 Circuitpotpmapping PotpUnGroupedobj = new Circuitpotpmapping();
                	    	 int circuitId = dbService.getCircuitService().InsertCircuit(circuitObj, networkInfoMap);
                	    	 
                	    	 
                	    	 PotpUnGroupedobj .setNetworkId(networkId);
                	    	 PotpUnGroupedobj .setCircuitId(circuitId);
                	    	 PotpUnGroupedobj .setCircuitpotpType("UG");
                	    	 PotpUnGroupedobj .setpotpcircuitId(potpCircuitId);
							 dbService.getCircuitPOTPMappingService().InsertCircuitPOTP(PotpUnGroupedobj, networkInfoMap);
						
						
						
						
					                       }

                	   
                	   
                	   
						     if(mainJsonObj.has("POTPCircuits"))
						{
							System.out.println("inside POTP Circuits");
							
							
							JSONArray POTPCircuitsArray  = (JSONArray)mainJsonObj.get("POTPCircuits"); 
							
							
							if(MapConstants.I_ZERO <  POTPCircuitsArray.length())
							{
								for(int count = 0; count < POTPCircuitsArray.length(); count++)
								{  
									JSONObject POTPCirObj = POTPCircuitsArray.getJSONObject(count);
									
									circuitObj.setDestNodeId(Integer.parseInt(POTPCirObj.get("DestNodeId").toString()));
									circuitObj.setTrafficType(POTPCirObj.get("ServiceType").toString());
									circuitObj.setRequiredTraffic(Float.parseFloat(POTPCirObj.get("ClientRate").toString()));
									circuitObj.setProtectionType(POTPCirObj.get("ProtType").toString());
									circuitObj.setPathType(POTPCirObj.get("PathType").toString());
									circuitObj.setColourPreference(POTPCirObj.get("Colour").toString());
									circuitObj.setNodeInclusion(POTPCirObj.get("NodeInclusion").toString());
								    
									int circuitId = dbService.getCircuitService().InsertCircuit(circuitObj, networkInfoMap);
									Circuitpotpmapping Potpobj = new Circuitpotpmapping();
									
									Potpobj.setNetworkId(networkId);
									Potpobj.setCircuitId(circuitId);
									Potpobj.setCircuitpotpType("G");
								    Potpobj.setTributoryId(Integer.parseInt(POTPCirObj.get("TributoryId").toString()));
									Potpobj.setpotpcircuitId(potpCircuitId);
								    dbService.getCircuitPOTPMappingService().InsertCircuitPOTP(Potpobj, networkInfoMap);
								    
								     
								         }
								
								
						           }
							
							
		                     }
			
                  
                   
                      } 
					
                  				
									
									
				}
						
					
					

				
           
				MainMap.logger.debug("Final  demandIdCircuitArray :  " + demandIdCircuitArray);

			
	}
			/********************************** CIRCUIT INSERTION : END ***********************************************/
			
			/**
			 * Segregate GreenField and BrownField Circuits to Treat them Differently 
			 * 							1) GreenField: Don't go for demand generation since demand already exist 
			 * 							2) BrownField: demand generation call
			 */
			int circuitModeGeneration = 0; /** Default case */
			
			if (((String) networkInfoMap.get(MapConstants.NetworkType)).equalsIgnoreCase(MapConstants.GreenField)
					&& flagForCircuitProcessingMode == MapConstants.I_ZERO) { /**GreenField*/

				/** Final Circuit to Demand Generation */
				CircuitProcessing circuitProcessingObj = new CircuitProcessing();
				circuitProcessingObj.fcircuitProcessing(dbService, networkId, circuitModeGeneration,QoS, networkInfoMap);

			} 
			
			else if (((String) networkInfoMap.get(MapConstants.NetworkType)).equalsIgnoreCase(MapConstants.BrownField)
					&& flagForCircuitProcessingMode == MapConstants.I_ZERO) { /**BrownField*/

				System.out.println(" GreenField Id " + (int) networkInfoMap.get(MapConstants.GreenFieldId)
						+ " BrownField Id " + (int) networkInfoMap.get(MapConstants.BrownFieldId));

				List<Circuit> addedCircuitBrFieldList = dbService.getCircuitService().FindAddedCircuitsInBrField(
						(int) networkInfoMap.get(MapConstants.GreenFieldId),
						(int) networkInfoMap.get(MapConstants.BrownFieldId));
				List<Circuit> commonCircuitGrFieldList = dbService.getCircuitService().FindCommonCircuitsInGreenField(
						(int) networkInfoMap.get(MapConstants.GreenFieldId),
						(int) networkInfoMap.get(MapConstants.BrownFieldId));

				System.out.println("addedCircuitBrFieldList  : " + addedCircuitBrFieldList);
				System.out.println("commonCircuitGrFieldList : " + commonCircuitGrFieldList);

				/** Generate Demand[common circuits] and Insert INTO Memory [new circuits] */
				generateDemandForCommonCircuits(commonCircuitGrFieldList, networkInfoMap, dbService);
				insertIntoInMemoryCircuit(addedCircuitBrFieldList, dbService);
				
				if (((String)networkInfoMap.get(MapConstants.NetworkType)).equalsIgnoreCase(MapConstants.BrownField)){/**Check only for BrownField*/
					deleteDeletedCircuitRelatedMapping(networkInfoMap,dbService);	
				}
				/** Final : Leftover New Circuits to Demand Generation */
				circuitModeGeneration = MapConstants.I_ONE; /** For BrownField */
				CircuitProcessing circuitProcessingObj = new CircuitProcessing();
				circuitProcessingObj.fcircuitProcessing(dbService, networkId, circuitModeGeneration, QoS,networkInfoMap);

			}
			
			/**Demand Modification Mode*/
			
			/*else if (((String) networkInfoMap.get(MapConstants.NetworkType)).equalsIgnoreCase(MapConstants.BrownField)
					&& flagForCircuitProcessingMode == MapConstants.I_ONE) {

				*//** Update the Corresponding Data from DB *//*

				for (Map.Entry<Integer, Circuit> rowMapDemandIdCircuitObj : demandIdCircuitArray.entries()) {
					System.out.println(" rowMapDemandIdCircuitObj : " + rowMapDemandIdCircuitObj);

					*//** Demand Update *//*
					int demandIdToUpdate = rowMapDemandIdCircuitObj.getKey();
					Demand demandToUpdate = new Demand();
					demandToUpdate.setNetworkId(networkId);
					demandToUpdate.setDemandId(demandIdToUpdate);
					demandToUpdate.setRequiredTraffic(rowMapDemandIdCircuitObj.getValue().getRequiredTraffic());
					demandToUpdate.setCircuitSet(String.valueOf(rowMapDemandIdCircuitObj.getValue().getCircuitId()));
					dbService.getDemandService().UpdateDemand(demandToUpdate);

					*//** CircuitDemand Mapping Insert *//*
					CircuitDemgenerateDemandForCommonCircuitsandMapping circuitDemandMappingObj = new CircuitDemandMapping();
					circuitDemandMappingObj.setNetworkId(networkId);
					circuitDemandMappingObj.setDemandId(demandIdToUpdate);
					circuitDemandMappingObj.setCircuitId(rowMapDemandIdCircuitObj.getValue().getCircuitId());
					dbService.getCircuitDemandMappingService().InsertCircuitDemandMapping(circuitDemandMappingObj);

				}

			}*/
			
			

		
			
			
		}
		
		
		catch (JSONException e) {
			e.printStackTrace();
			return MapConstants.FAILURE;

		}
		
		MainMap.logger.info("--------------------------JSON Parsing End---------------------------------------");
		return MapConstants.SUCCESS;
	}

	/**
	 * @brief This function simply copy the Demand from Old Network To New Network
	 * @param CommonCircuitBrFieldList
	 * @param networkInfoMap
	 * @param dbService
	 */
	public void generateDemandForCommonCircuits(List<Circuit> CommonCircuitGrFieldList,
			HashMap<String, Object> networkInfoMap, DbService dbService) {

		///MainMap.logger.info(" generateDemandForCommonCircuits ");
		MapWebCommonAPIs.NestedStaticClass.oldDemandObjMap.clear(); /** Flush the Global Map first */

		for (Circuit circuitRowObj : CommonCircuitGrFieldList) {

			MainMap.logger.info(" circuitRowObj : " + circuitRowObj);
			// Demand oldDemandObj = dbService.getDemandService().FindDemand(circuitRowObj);
			
			CircuitDemandMapping cd = dbService.getCircuitDemandMappingService()
					.FindDemand(circuitRowObj.getNetworkId(), circuitRowObj.getCircuitId());
			
			Demand oldDemandObj = dbService.getDemandService().FindDemand(cd.getNetworkId(), cd.getDemandId());

			MainMap.logger.info(" oldDemandObj :" + oldDemandObj);

			oldDemandObj.setNetworkId((int) networkInfoMap
					.get(MapConstants.BrownFieldId));/**Assign BrownField Network Id to the Old Demand Map*/

			/***
			 * Prepare a Map to Maintain Uniqueness while inserting Circuit Demands with Multiplier
			 */
			if (!MapWebCommonAPIs.NestedStaticClass.oldDemandObjMap.containsKey(oldDemandObj.getDemandId())) {
				MapWebCommonAPIs.NestedStaticClass.oldDemandObjMap.put(oldDemandObj.getDemandId(), oldDemandObj);
			}
		}

		
		/**
		 * Insert Old Existed Demand, CircuitDemandMapping from Green Field To Brown
		 * Field without any modification
		 */

		Iterator<Entry<Integer, Object>> oldDemandHashMapIterator = MapWebCommonAPIs.NestedStaticClass.oldDemandObjMap
				.entrySet().iterator();

		/*System.out.println(" MapWebCommonAPIs.NestedStaticClass.oldDemandObjMap "
				+ MapWebCommonAPIs.NestedStaticClass.oldDemandObjMap);
*/
		while (oldDemandHashMapIterator.hasNext()) {

			@SuppressWarnings("rawtypes")
			Map.Entry oldDemandHashMapIteratorPair = (Map.Entry) oldDemandHashMapIterator.next();
		
			System.out
					.println(oldDemandHashMapIteratorPair.getKey() + " and " + oldDemandHashMapIteratorPair.getValue());

			Demand oldDemandObjToCopy = (Demand) oldDemandHashMapIteratorPair.getValue();

			///System.out.println(" oldDemandObjToCopy " + oldDemandObjToCopy);

			try {
				/** Demand */
				dbService.getDemandService().InsertCommonGreenFieldDemand(oldDemandObjToCopy);

				String circuits[] = oldDemandObjToCopy.getCircuitSet().split(",");
				/*System.out.println("Circuits[] " + circuits.toString());
				System.out.println(" oldDemandObjToCopy " + oldDemandObjToCopy);
				 */
				
				/** CircuitDemandMapping */
				for (int j = 0; j < circuits.length; j++) {
					CircuitDemandMapping cd = new CircuitDemandMapping();
					cd.setCircuitId(Integer.parseInt(circuits[j].toString()));
					cd.setNetworkId(oldDemandObjToCopy.getNetworkId());
					cd.setDemandId(oldDemandObjToCopy.getDemandId());
					///System.out.println(" Circuit Demand To Insert is " + cd);
					dbService.getCircuitDemandMappingService().InsertCircuitDemandMapping(cd);
				}

				/** NetworkRoute : Shifted back while RWA Output Insertion Process */
				
				 oldDemandObjToCopy.setNetworkId((int)networkInfoMap.get(MapConstants.
						 							GreenFieldId));
				 	
				 /**
				  * Assign GreenField Network Id to the Old Demand Map for Search
				  */
						 
				 List<NetworkRoute> oldNetworkRoutes =
						 dbService.getNetworkRouteService().FindAllByDemandId
						 (oldDemandObjToCopy.getNetworkId(),oldDemandObjToCopy.getDemandId());
						  
				System.out.println("oldNetworkRoutes "+oldNetworkRoutes);
						  
				for(NetworkRoute networkRouteRowObj : oldNetworkRoutes){
					 networkRouteRowObj.setNetworkId((int)networkInfoMap.get(MapConstants.
							 BrownFieldId));
					 dbService.getNetworkRouteService().InsertNetworkRoute(networkRouteRowObj); }
						 

			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * @brief This function is used to Insert Newly Added Circuits into In Memory
	 *        Circuit Table for Later Processing
	 * @param AddedCircuitBrFieldList
	 * @param dbService
	 */
	public void insertIntoInMemoryCircuit(List<Circuit> AddedCircuitBrFieldList, DbService dbService) {

		///MainMap.logger.info(" insertIntoInMemoryCircuit ");
		for (Circuit circuitRowObj : AddedCircuitBrFieldList) {

			try {
				/**
				 * Circuit Insert int InMemory only if Demand Not Exist corresponding to it ..!
				 */
				/*System.out.println(" checkkkk  Null " + dbService.getCircuitDemandMappingService()
						.FindDemand(circuitRowObj.getNetworkId(), circuitRowObj.getCircuitId()));*/

				if (/*
					 * dbService.getCircuitDemandMappingService().FindDemand(circuitRowObj.
					 * getNetworkId(), circuitRowObj.getCircuitId()) == null
					 */
				dbService.getCircuitDemandMappingService().FindDemand(circuitRowObj.getNetworkId(),
						circuitRowObj.getCircuitId()) == null) {
					///System.out.println(" Going to Insert for inmemory even its not null");
					dbService.getCircuitService().InsertInMemoryCircuit(circuitRowObj);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		MainMap.logger.info("In Memory circuits : " + dbService.getCircuitService().FindAllInMemoryCircuit());
	}
	
	/**
	 * This function Remove the deleted Circuit mapping from Demand as well as CircuitDemandMapping and NetworkRoute Too!
	 * @param networkInfoMap
	 * @param dbService
	 */
	public void deleteDeletedCircuitRelatedMapping(HashMap<String, Object> networkInfoMap,DbService dbService) {
		
		
		List<DemandMapping> demandMappingObject = dbService.getCircuitService().FindDeletedCircuitsDemMappedInBrField((int) networkInfoMap
				.get(MapConstants.GreenFieldId), (int) networkInfoMap
				.get(MapConstants.BrownFieldId));/**List of Deleted Circuits with Demand*/
		
		System.out.println("demandMappingObject : "+demandMappingObject.toString());
	
		
		
		/*
		 * Ex: [DemandMapping [NetworkId=2, CircuitId=6, DemandId=1, SrcNodeId=1, DestNodeId=2, RequiredTraffic=10, TrafficType=Ethernet, ProtectionType=1+1+R, ClientProtectionType=Y-Cable, ColourPreference=Empty, PathType=Disjoint, LineRate=100, ChannelProtection=No]]
		 */
				
		for(int i=0;i<demandMappingObject.size();i++) {
		
			/**Delete Corresponding LSP information from DB*/	
			deleteOrModifiedLspInformation(demandMappingObject.get(i), networkInfoMap, dbService);


			Demand updateDemandCircuitSet = dbService.getDemandService().FindDemand((int)networkInfoMap.get(MapConstants.NetworkId),
					demandMappingObject.get(i).getDemandId());/**Demand From Which Circuits need to be matched against*/
			
			
			if(updateDemandCircuitSet!=null) {
				
				String[] CircuitSet=updateDemandCircuitSet.getCircuitSet().split(",");///replace(String.valueOf(demandMappingObject.get(i).getCircuitId()),"");
				String finalCircuitSet="";
				float deletedCircuitRequiredTraffic=0;
			
				
				
				for(String temp : CircuitSet) {
					///System.out.println(" Each temp "+ temp);
				
					if(temp.equalsIgnoreCase(String.valueOf(demandMappingObject.get(i).getCircuitId()))) {
					
						dbService.getCircuitDemandMappingService().DeleteCircuitDemandMapping(updateDemandCircuitSet.getNetworkId(), Integer.parseInt(temp));
						deletedCircuitRequiredTraffic+=demandMappingObject.get(i).getRequiredTraffic();
						continue;
						
					}
					else {					
						
						finalCircuitSet+=temp+",";					
					}
						
				}
				
				/*System.out.println("finalCircuitSet : "+finalCircuitSet);
				System.out.println("requiredTraffic : "+requiredTraffic);*/
				
				/**
				 * Remove Garbage
				 */
				String updateCircuitSet="";
				String[] tempCircuitSet = finalCircuitSet.split(","); 
				for(int tempCounter=0; tempCounter<tempCircuitSet.length;tempCounter++) {

					if(tempCircuitSet[tempCounter].equals(""))
						continue;
					else if(tempCounter < (tempCircuitSet.length-1))
						updateCircuitSet+=tempCircuitSet[tempCounter]+",";
					else
						updateCircuitSet+=tempCircuitSet[tempCounter];
				}
				
				
				/*MainMap.logger.info(" deletedCircuitRequiredTraffic "+ deletedCircuitRequiredTraffic);
				MainMap.logger.info(" updateCircuitSet "+ updateCircuitSet);
				System.out.println(" updateDemandCircuitSet.getRequiredTraffic() "+updateDemandCircuitSet.getRequiredTraffic());*/
				
				float setTraffic= (updateDemandCircuitSet.getRequiredTraffic() - deletedCircuitRequiredTraffic);
				
				updateDemandCircuitSet.setCircuitSet(updateCircuitSet);
				updateDemandCircuitSet.setRequiredTraffic(updateDemandCircuitSet.getRequiredTraffic() - deletedCircuitRequiredTraffic);
				dbService.getDemandService().UpdateDemandForCircuitSet(updateDemandCircuitSet);
				///System.out.println(" outer updateDemandCircuitSet.getRequiredTraffic() - deletedCircuitRequiredTraffic "+ (updateDemandCircuitSet.getRequiredTraffic() - deletedCircuitRequiredTraffic));
				
				/**
				 * Update NetworRoute correspondingly .. : Do or Die
				 */
				List<NetworkRoute> networkRouteObj = dbService.getNetworkRouteService().FindAllByDemandId
						((int)networkInfoMap.get(MapConstants.NetworkId), demandMappingObject.get(i).getDemandId());
				for(int localRouteCount=0; localRouteCount<networkRouteObj.size();localRouteCount++) {
					
					///System.out.println("setTraffic "+setTraffic);
					
					
					networkRouteObj.get(localRouteCount).setTraffic(setTraffic);
					dbService.getNetworkRouteService().UpdateCommonRouteDemMappedInBrFieldDirect(networkRouteObj.get(localRouteCount));	
				}
				
				
			}
			
			
		}
		
		
		/** Delete Modified Circuits OTNLSPs too*/
		
		/**Add modified circuits too*/
		demandMappingObject.clear();
		demandMappingObject.addAll(dbService.getCircuitService().FindModifiedCircuitsDemMappedInBrField((int) networkInfoMap.get(MapConstants.GreenFieldId), (int) networkInfoMap
				.get(MapConstants.BrownFieldId)));
		
		for(DemandMapping d : demandMappingObject) {
			deleteOrModifiedLspInformation(d, networkInfoMap, dbService);
		}
		
		/**Delete Deleted Demand LambdaLSPs*/
//		List<Integer> listOfAllNode = dbService.getNodeService().FindnodeIdIntegers((int) networkInfoMap.get(MapConstants.NetworkId));
//		
//		for(int nodeid :listOfAllNode ) {
//			
//			dbService.getDemandService().FindDeletedDemandsInBrField((int) networkInfoMap.get(MapConstants.GreenFieldId), (int) networkInfoMap
//					.get(MapConstants.BrownFieldId), nodeid);
//			
//		}
		
				
		
		
		
		
	}


	/**
	 * This function Remove the deleted Circuit/Demand Related Lsp [Otn & ]
	 * @param demandMappingOBJ
	 * @param dbService
	 * @author hp
	 * @date 5th Jun, 2018
	 */
	public void deleteOrModifiedLspInformation(DemandMapping demandMappingOBJ, HashMap<String, Object> networkInfoMap, DbService dbService) {

		try {

			/**Delete OtnLsp info */
			dbService.getOtnLspInformationSerivce().DeleteSpecificOtnLsp(
							(int) networkInfoMap.get(MapConstants.BrownFieldId),		
							demandMappingOBJ);

		} catch (Exception e) {

			System.out.println("Error Log from deleteLspInformation : "+e);
		}
	}

		

}
