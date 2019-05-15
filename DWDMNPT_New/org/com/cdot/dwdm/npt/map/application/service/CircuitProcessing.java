/**
 * @author sunaina
 * @brief This class has logic for processing the circuits and create demands out of them for the RWA : Called on Save of Circuits
 */
package application.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import application.MainMap;
import application.constants.MapConstants;
import application.model.Circuit;
import application.model.CircuitCheck;
import application.model.CircuitDemandMapping;
import application.model.Demand;
import application.model.NetworkRoute;
import application.model.Circuitpotpmapping;
import java.util.HashMap;

@Service
public class CircuitProcessing {
	
	
	
	/**
	 * @brife Common Function To Process Circuit for Demand Generation 
	 *        circuitModeGeneration : 0 ==> Default Mode [GreenField]
	 *        						  1 ==> In case Of BrownField Newly Added Circuits
	 * @param dbService
	 * @param networkId
	 * @param circuitModeGeneration
	 * @throws SQLException
	 */
	public void fcircuitProcessing(DbService dbService,int networkId, int circuitModeGeneration,int QoS, HashMap<String, Object> networkInfoMap) throws SQLException
	{   
		ArrayList<Demand> demandList = new ArrayList<Demand>();
		List<Map<String,Object>> listx_10 = dbService.getCircuitService().GroupCircuitsFor10GMpn(networkId,circuitModeGeneration);
		List<Map<String,Object>> list10_100 = dbService.getCircuitService().GroupCircuits(networkId,MapConstants.Line100,MapConstants.G10,QoS,circuitModeGeneration);
		List<Map<String,Object>> list10_200 = dbService.getCircuitService().GroupCircuits(networkId,MapConstants.Line200,MapConstants.G10,QoS,circuitModeGeneration);
		List<Map<String,Object>> list10_10 = dbService.getCircuitService().FindCircuitsForTPN(networkId,MapConstants.Line10,MapConstants.G10,circuitModeGeneration);
		List<Map<String,Object>> list100_100 = dbService.getCircuitService().FindCircuitsForTPN(networkId,MapConstants.Line100,MapConstants.G100,circuitModeGeneration);
		List<Map<String,Object>> list10_100_POTP = dbService.getCircuitService().FindCircuitsForPOTP(networkId,MapConstants.Line100,QoS,circuitModeGeneration);
        
		MainMap.logger.info(" listx_10 " + listx_10 );
		MainMap.logger.info(" list10_100 " + list10_100 );
		MainMap.logger.info(" list10_200 " + list10_200 );
		MainMap.logger.info(" list10_10 " + list10_10 );
		MainMap.logger.info(" list10_100_POTP " + list10_100_POTP );
		
		
		
		/** for 10g client and 100 linerate for POTP=> Same for GreenField and Brown Field as well */
		for (int j = 0; j < list10_100_POTP.size(); j++) 
		{   
			Demand d = new Demand();	
			d.setNetworkId(networkId);
			d.setSrcNodeId(Integer.parseInt(list10_100_POTP.get(j).get("SrcNodeId").toString()));
			d.setDestNodeId(Integer.parseInt(list10_100_POTP.get(j).get("DestNodeId").toString()));
			d.setProtectionType(list10_100_POTP.get(j).get("ProtectionType").toString());	
			d.setClientProtectionType(list10_100_POTP.get(j).get("ClientProtectionType").toString());	
			d.setColourPreference(list10_100_POTP.get(j).get("ColourPreference").toString());				
			d.setPathType(list10_100_POTP.get(j).get("PathType").toString());
			d.setLineRate(list10_100_POTP.get(j).get("LineRate").toString());
			d.setCircuitSet(list10_100_POTP.get(j).get("CircuitId").toString());
			d.setRequiredTraffic(Float.parseFloat(list10_100_POTP.get(j).get("RequiredTraffic").toString()));
			d.setChannelProtection(list10_100_POTP.get(j).get("ChannelProtection").toString());
			d.setClientProtection(list10_100_POTP.get(j).get("ClientProtection").toString());
			d.setNodeInclusion(list10_100_POTP.get(j).get("NodeInclusion").toString());
			
			
			demandList.add(d);			
		
			}
		
		
		
		
		/** for 10g client and 10g linerate => Same for GreenField and Brown Field as well */
		for (int i = 0; i < list10_10.size(); i++) 
		{
			Demand d = new Demand();	
			d.setNetworkId(networkId);
			d.setSrcNodeId(Integer.parseInt(list10_10.get(i).get("SrcNodeId").toString()));
			d.setDestNodeId(Integer.parseInt(list10_10.get(i).get("DestNodeId").toString()));
			d.setProtectionType(list10_10.get(i).get("ProtectionType").toString());	
			d.setClientProtectionType(list10_10.get(i).get("ClientProtectionType").toString());	
			d.setColourPreference(list10_10.get(i).get("ColourPreference").toString());				
			d.setPathType(list10_10.get(i).get("PathType").toString());
			d.setLineRate(list10_10.get(i).get("LineRate").toString());
			d.setCircuitSet(list10_10.get(i).get("CircuitId").toString());
			d.setRequiredTraffic(Float.parseFloat(list10_10.get(i).get("RequiredTraffic").toString()));
			d.setChannelProtection(list10_10.get(i).get("ChannelProtection").toString());
			
			d.setClientProtection(list10_10.get(i).get("ClientProtection").toString());
			//d.setLambdaBlocking(list10_10.get(i).get("LambdaBlocking").toString());
			d.setNodeInclusion(list10_10.get(i).get("NodeInclusion").toString());
			
			
			demandList.add(d);			
		}
		
		
		
		
		//for 10g client and 100g linerate
		for (int i = 0; i < list10_100.size(); i++) 
		{
			int nCircuits =Integer.parseInt(list10_100.get(i).get("CircuitCount").toString());
			float TotalTraffic =Float.parseFloat(list10_100.get(i).get("TotalTraffic").toString());
			System.out.println(" TotalTraffic "+ TotalTraffic);
			
			if(circuitModeGeneration == MapConstants.I_ZERO) {/**GreenField : Direct Demand Creation without any further processing */
				
				
				if((TotalTraffic>100))/**After Grouping based on the Params if Added Traffic Exceed linerate which is 100G then Create Separate Demands */
				{	
					///System.out.println(" If TotalTraffic>100 .. ");
					int src=Integer.parseInt(list10_100.get(i).get("SrcNodeId").toString());
					int dest=Integer.parseInt(list10_100.get(i).get("DestNodeId").toString());
					List<Circuit> cirs = dbService.getCircuitService().FindCircuitsFor100GMPN(networkId, src, dest, list10_100.get(i).get("ProtectionType").toString(),list10_100.get(i).get("ClientProtectionType").toString(),list10_100.get(i).get("ColourPreference").toString(),list10_100.get(i).get("PathType").toString(),list10_100.get(i).get("ChannelProtection").toString() );
					///System.out.println(" Circuts : "+ cirs);
					
					ArrayList<CircuitCheck> circuitcheck = new ArrayList<CircuitCheck>();				
					for (int j = 0; j < cirs.size(); j++) {
						CircuitCheck c = new CircuitCheck();
						c.setCircuitId(cirs.get(j).getCircuitId());
						c.setRequiredTraffic(cirs.get(j).getRequiredTraffic());	
						c.setFlag(false);
						circuitcheck.add(c);
					}
					
					///System.out.println(" circuitcheck : "+ circuitcheck.toString());
					
					while(IsEntryPending(circuitcheck))
					{
						float sum =0;
						String str="";
					
						Demand d = new Demand();	
						d.setNetworkId(networkId);
						d.setSrcNodeId(Integer.parseInt(list10_100.get(i).get("SrcNodeId").toString()));
						d.setDestNodeId(Integer.parseInt(list10_100.get(i).get("DestNodeId").toString()));
						d.setProtectionType(list10_100.get(i).get("ProtectionType").toString());
						d.setClientProtectionType(list10_100.get(i).get("ClientProtectionType").toString());
						d.setColourPreference(list10_100.get(i).get("ColourPreference").toString());				
						d.setPathType(list10_100.get(i).get("PathType").toString());
						d.setLineRate(list10_100.get(i).get("LineRate").toString());
						d.setChannelProtection(list10_100.get(i).get("ChannelProtection").toString());
						
						d.setClientProtection(list10_100.get(i).get("ClientProtection").toString());
					  //d.setLambdaBlocking(list10_100.get(i).get("LambdaBlocking").toString());
					    d.setNodeInclusion(list10_100.get(i).get("NodeInclusion").toString());
						
						for (int j = 0; j < circuitcheck.size(); j++) 
						{								
							if(circuitcheck.get(j).isFlag()==false)
							{
								if(sum != 100)
								{
									sum = sum + circuitcheck.get(j).getRequiredTraffic();
									str=str.concat(""+circuitcheck.get(j).getCircuitId()).concat(",");									
									circuitcheck.get(j).setFlag(true);		
								}
								///System.out.println("CircuitProcessing.fcircuitProcessingnew() j: "+j+ " sum: "+sum);
							}
							else
							{
								continue;
							}
						}
						///System.out.println("CircuitProcessing.fcircuitProcessingnew()"+circuitcheck.toString());
						d.setRequiredTraffic(sum);
						d.setCircuitSet(str);
						demandList.add(d);
					}				
				}	
				else
				{
					///System.out.println(" Else .. ");
					Demand d = new Demand();	
					d.setNetworkId(networkId);
					d.setSrcNodeId(Integer.parseInt(list10_100.get(i).get("SrcNodeId").toString()));
					d.setDestNodeId(Integer.parseInt(list10_100.get(i).get("DestNodeId").toString()));
					d.setProtectionType(list10_100.get(i).get("ProtectionType").toString());	
					d.setClientProtectionType(list10_100.get(i).get("ClientProtectionType").toString());
					d.setColourPreference(list10_100.get(i).get("ColourPreference").toString());				
					d.setPathType(list10_100.get(i).get("PathType").toString());
					d.setLineRate(list10_100.get(i).get("LineRate").toString());
					d.setCircuitSet(list10_100.get(i).get("CircuitSet").toString());
					d.setRequiredTraffic(Float.parseFloat(list10_100.get(i).get("TotalTraffic").toString()));
					d.setChannelProtection(list10_100.get(i).get("ChannelProtection").toString());
					
					d.setClientProtection(list10_100.get(i).get("ClientProtection").toString());
					//d.setLambdaBlocking(list10_100.get(i).get("LambdaBlocking").toString());
					d.setNodeInclusion(list10_100.get(i).get("NodeInclusion").toString());
					
					demandList.add(d);	
				}
				
						
			}
			
			else if(circuitModeGeneration == MapConstants.I_ONE) {/**BrownField : As per the Description*/
				
				
				String[] circuitSetArray = list10_100.get(i).get("CircuitSet").toString().split(",");/** ex : CircuitSet : [1,2,3]*/
				System.out.println(" circuitSetArray "+ Arrays.toString(circuitSetArray));
				/**
				 * Very Important Variable for Best Fit Algo
				 */
				int LINERATE = MapConstants.I_HUNDREAD;
				int PER_GROUP_REQUIRED_TRAFFIC= MapConstants.I_TEN;
				
				/**
				 * Check for TrafficType too in future if require...
				 */
				Demand demandToUpdate = dbService.getDemandService().FindDemand(
						Integer.parseInt(list10_100.get(i).get("NetworkId").toString()),
						Integer.parseInt(list10_100.get(i).get("SrcNodeId").toString()),
						Integer.parseInt(list10_100.get(i).get("DestNodeId").toString()),						
						list10_100.get(i).get("ProtectionType").toString(),
						list10_100.get(i).get("ClientProtectionType").toString(),
						list10_100.get(i).get("ColourPreference").toString(), 
						list10_100.get(i).get("PathType").toString(), 
						Integer.parseInt(list10_100.get(i).get("LineRate").toString()), 
						list10_100.get(i).get("ChannelProtection").toString(),
						
						list10_100.get(i).get("ClientProtection").toString());
				
				
				if(demandToUpdate!=null) {/**Matching Demand found in Database*/
					
					int remainingCapacityInOldDemand =(Integer.parseInt(demandToUpdate.getLineRate().toString())-  
							(int)(demandToUpdate.getRequiredTraffic())); 
					MainMap.logger.info(" demandToUpdate : Linerate - Capacity "+ remainingCapacityInOldDemand);
                  
					int circuitIdTrackCount= Integer.parseInt(list10_100.get(i).get("CircuitCount").toString());/**Number of Total Circuit Id to copy from Set*/                   
                    
                                       
					if(remainingCapacityInOldDemand /*< MapConstants.I_HUNDREAD*/> MapConstants.I_ZERO
							&& remainingCapacityInOldDemand < MapConstants.I_HUNDREAD) {/**New Demands Can be alloted into*/
						
						/**
						 * Two Cases : 1) New Demand can me mapped completely : Update old demand and mapping or
						 *             2) New Demand can me mapped partially  : Update old demand and mapping with best fit and for rest create an new demand  
						 */
						
					
						/**
						 * Best Fit : Article  1 
						 */						
						int localCircuitIdTrackCount=0, requiredTrafficToSet=0;
						int oldCircuitCount=remainingCapacityInOldDemand/PER_GROUP_REQUIRED_TRAFFIC;
						int newCircuitCount=circuitSetArray.length;
						MainMap.logger.info("oldCircuitCount & newCircuitCount "+oldCircuitCount + " and "+newCircuitCount);
						
						/**Article 1(a) : New Circuits are less or equal to the remaining capacity */
						if(newCircuitCount <= oldCircuitCount) {
							localCircuitIdTrackCount= circuitSetArray.length;
							requiredTrafficToSet = (PER_GROUP_REQUIRED_TRAFFIC*circuitSetArray.length);
						}
						
						/**Article 1(b) : New Circuits are greater than  the remaining capacity */
						else if(newCircuitCount > oldCircuitCount) {
							localCircuitIdTrackCount = oldCircuitCount;
							requiredTrafficToSet =remainingCapacityInOldDemand;
						}						
						
						///System.out.println(" localCircuitIdTrackCount "+ localCircuitIdTrackCount);
												
						String firstCircuitSet ="", secondCircuitSet="";
						firstCircuitSet = makeSubArrayString(circuitSetArray,0,localCircuitIdTrackCount);
						///System.out.println(" firstCircuitSet  "+ firstCircuitSet );
						secondCircuitSet= makeSubArrayString(circuitSetArray,localCircuitIdTrackCount,circuitSetArray.length);											
						///System.out.println(" secondCircuitSet  "+ secondCircuitSet );
						
						/** Demand Update */
						demandToUpdate.setRequiredTraffic(requiredTrafficToSet);
						demandToUpdate.setCircuitSet(firstCircuitSet);
						dbService.getDemandService().UpdateDemand(demandToUpdate);
						
						
						/** CircuitDemand Mapping Insert */
						
						MainMap.logger.info("circuitSetArray : " +circuitSetArray
								+ " circuitSetArray.length - (remainingCapacityInOldDemand/10) "+ (circuitSetArray.length - (remainingCapacityInOldDemand/10)));
					
						int cdCount=0;
						
						for(cdCount=0; cdCount< localCircuitIdTrackCount;  cdCount++){
							System.out.println(" cdcount : "+ cdCount);
							System.out.println(" Circuit Id to Set from Set "+Integer.parseInt(circuitSetArray[cdCount]));
							CircuitDemandMapping circuitDemandMappingObj = new CircuitDemandMapping();
							circuitDemandMappingObj.setNetworkId(Integer.parseInt(list10_100.get(i).get("NetworkId").toString()));
							circuitDemandMappingObj.setDemandId(demandToUpdate.getDemandId());
							circuitDemandMappingObj.setCircuitId(Integer.parseInt(circuitSetArray[cdCount]));
							dbService.getCircuitDemandMappingService().InsertCircuitDemandMapping(circuitDemandMappingObj);
							
						}
						
					
						/** NetworkRoute */
						List<NetworkRoute> routeToUpdate = dbService.getNetworkRouteService().FindAllByDemandId(demandToUpdate.getNetworkId(),
																demandToUpdate.getDemandId());
						
						System.out.println(" ------------------ routeToUpdate "+routeToUpdate);
						
						if(routeToUpdate!= null) {/**RWA Executed before and routes exist => Update the Traffic*/
							
							for(NetworkRoute localRouteToUpdate : routeToUpdate) {
								System.out.println("---------------------requiredTrafficToSet "+requiredTrafficToSet);
								localRouteToUpdate.setTraffic(requiredTrafficToSet);
								dbService.getNetworkRouteService().UpdateCommonRouteDemMappedInBrField(localRouteToUpdate);
							}
							
						}
						
						///System.out.println(" Final cdCount "+ cdCount);
						
						
						/**
						 * Case 2 : Overflowing : purely towards new demand creation
						 */
						if(TotalTraffic > remainingCapacityInOldDemand) {/**Towards -> New Demand*/
							
							int capacityToDeal = (int) (TotalTraffic-remainingCapacityInOldDemand);
							int totalDemandToGenerate = (int) Math.ceil((double)capacityToDeal/MapConstants.I_HUNDREAD);
							int perDemandTraffic=0, localTotalTraffic=(int)capacityToDeal;
							localCircuitIdTrackCount=0;
							String localSecondCircuitSet=secondCircuitSet;
							
							while(totalDemandToGenerate!=MapConstants.I_ZERO) {
								
								if(localTotalTraffic<MapConstants.I_HUNDREAD)
									perDemandTraffic=(int) capacityToDeal%MapConstants.I_HUNDREAD;
								else
									perDemandTraffic=MapConstants.I_HUNDREAD;
								
								
								///System.out.println("start localCircuitIdTrackCount  "+ localCircuitIdTrackCount);
								///System.out.println(" and end : "+ (localCircuitIdTrackCount+(perDemandTraffic/10)));
								localSecondCircuitSet= makeSubArrayString(secondCircuitSet.split(","),localCircuitIdTrackCount,(localCircuitIdTrackCount+(perDemandTraffic/10)));
								
								
								
								Demand d = new Demand();	
								d.setNetworkId(networkId);
								d.setSrcNodeId(Integer.parseInt(list10_100.get(i).get("SrcNodeId").toString()));
								d.setDestNodeId(Integer.parseInt(list10_100.get(i).get("DestNodeId").toString()));
								d.setProtectionType(list10_100.get(i).get("ProtectionType").toString());	
								d.setClientProtectionType(list10_100.get(i).get("ClientProtectionType").toString());
								d.setColourPreference(list10_100.get(i).get("ColourPreference").toString());				
								d.setPathType(list10_100.get(i).get("PathType").toString());
								d.setLineRate(list10_100.get(i).get("LineRate").toString());
								d.setCircuitSet(localSecondCircuitSet);
								d.setRequiredTraffic(perDemandTraffic);
								d.setChannelProtection(list10_100.get(i).get("ChannelProtection").toString());
								
								d.setClientProtection(list10_100.get(i).get("ClientProtection").toString());								
								//d.setLambdaBlocking(list10_100.get(i).get("LambdaBlocking").toString());
								d.setNodeInclusion(list10_100.get(i).get("NodeInclusion").toString());
								
								demandList.add(d);
								
								
								totalDemandToGenerate--;
								localTotalTraffic-=perDemandTraffic;
								localCircuitIdTrackCount+=(perDemandTraffic/10);
								System.out.println(" localCircuitIdTrackCount : "+localCircuitIdTrackCount);
								
							}
						}
						
					}				
					
				}
				else {/**No Matching Found hence treat all demand as the new one*/
					
					int capacityToDeal = (int)TotalTraffic;
					int totalDemandToGenerate = (int) Math.ceil((double)capacityToDeal/LINERATE);
					int perDemandTraffic=0, localTotalTraffic=(int)capacityToDeal;
					int localCircuitIdTrackCount=0;					
					
					String localSecondCircuitSet="";
					
					/**
					 * Till all demand Settled up !!
					 */
					while(totalDemandToGenerate!=MapConstants.I_ZERO) {
						
						/**assign traffic per demand*/
						if(localTotalTraffic<LINERATE)
							perDemandTraffic=(int) capacityToDeal%LINERATE;/**Modulo to find out the traffic < linerate*/
						else
							perDemandTraffic=LINERATE;/**traffic with linerate can be assigned*/
						
						/**Local set to clip out the required circuits from the parent set*/
						System.out.println("localCircuitIdTrackCount "+localCircuitIdTrackCount);
						System.out.println("perDemandTraffic "+perDemandTraffic);
						System.out.println(" totalDemandToGenerate "+totalDemandToGenerate);
						
						localSecondCircuitSet= makeSubArrayString(circuitSetArray,localCircuitIdTrackCount
																,(localCircuitIdTrackCount+(perDemandTraffic/PER_GROUP_REQUIRED_TRAFFIC)));
						
						
							Demand d = new Demand();	
							d.setNetworkId(networkId);
							d.setSrcNodeId(Integer.parseInt(list10_100.get(i).get("SrcNodeId").toString()));
							d.setDestNodeId(Integer.parseInt(list10_100.get(i).get("DestNodeId").toString()));
							d.setProtectionType(list10_100.get(i).get("ProtectionType").toString());	
							d.setClientProtectionType(list10_100.get(i).get("ClientProtectionType").toString());
							d.setColourPreference(list10_100.get(i).get("ColourPreference").toString());				
							d.setPathType(list10_100.get(i).get("PathType").toString());
							d.setLineRate(list10_100.get(i).get("LineRate").toString());
							d.setCircuitSet(localSecondCircuitSet);
							d.setRequiredTraffic(perDemandTraffic);
							d.setChannelProtection(list10_100.get(i).get("ChannelProtection").toString());
							
							d.setClientProtection(list10_100.get(i).get("ClientProtection").toString());								
							//d.setLambdaBlocking(list10_100.get(i).get("LambdaBlocking").toString());
							d.setNodeInclusion(list10_100.get(i).get("NodeInclusion").toString());
							
							demandList.add(d);
							
							
							/**Readjusting var w.r.t. assigned demand*/
							totalDemandToGenerate--;
							localTotalTraffic-=perDemandTraffic;
							localCircuitIdTrackCount+=(perDemandTraffic/PER_GROUP_REQUIRED_TRAFFIC);
							
					}
				}
			}
			
		}
			
	
		
		//for 10g client and 200g linerate
		for (int i = 0; i < list10_200.size(); i++) 
		{
			int nCircuits =Integer.parseInt(list10_200.get(i).get("CircuitCount").toString());
			float TotalTraffic =Float.parseFloat(list10_200.get(i).get("TotalTraffic").toString());
			
			System.out.println(" TotalTraffic "+ TotalTraffic);
			
			if(circuitModeGeneration == MapConstants.I_ZERO) 
				{/**GreenField : Direct Demand Creation without any further processing */
				
				
					if((TotalTraffic>200))
					{
						int src=Integer.parseInt(list10_200.get(i).get("SrcNodeId").toString());
						int dest=Integer.parseInt(list10_200.get(i).get("DestNodeId").toString());
						List<Circuit> cirs = dbService.getCircuitService().FindCircuitsFor200GMPN(networkId, src, dest,  list10_200.get(i).get("ProtectionType").toString(),list10_200.get(i).get("ClientProtectionType").toString(),list10_200.get(i).get("ColourPreference").toString(),list10_200.get(i).get("PathType").toString(),list10_200.get(i).get("ChannelProtection").toString());
						ArrayList<CircuitCheck> circuitcheck = new ArrayList<CircuitCheck>();				
						for (int j = 0; j < cirs.size(); j++) {
							CircuitCheck c = new CircuitCheck();
							c.setCircuitId(cirs.get(j).getCircuitId());
							c.setRequiredTraffic(cirs.get(j).getRequiredTraffic());	
							c.setFlag(false);
							circuitcheck.add(c);
						}
						
						while(IsEntryPending(circuitcheck))
						{
							float sum =0;
							String str="";
							Demand d = new Demand();	
							d.setNetworkId(networkId);
							d.setSrcNodeId(Integer.parseInt(list10_200.get(i).get("SrcNodeId").toString()));
							d.setDestNodeId(Integer.parseInt(list10_200.get(i).get("DestNodeId").toString()));
							d.setProtectionType(list10_200.get(i).get("ProtectionType").toString());
							d.setClientProtectionType(list10_200.get(i).get("ClientProtectionType").toString());
							d.setColourPreference(list10_200.get(i).get("ColourPreference").toString());				
							d.setPathType(list10_200.get(i).get("PathType").toString());
							d.setLineRate(list10_200.get(i).get("LineRate").toString());
							d.setChannelProtection(list10_200.get(i).get("ChannelProtection").toString());
							
							d.setClientProtection(list10_200.get(i).get("ClientProtection").toString());								
							//d.setLambdaBlocking(list10_200.get(i).get("LambdaBlocking").toString());
							d.setNodeInclusion(list10_200.get(i).get("NodeInclusion").toString());
							
							
							for (int j = 0; j < circuitcheck.size(); j++) 
							{							
								if(circuitcheck.get(j).isFlag()==false)
								{
									if(sum != 200)
									{
										sum = sum + circuitcheck.get(j).getRequiredTraffic();
										str=str.concat(""+circuitcheck.get(j).getCircuitId()).concat(",");									
										circuitcheck.get(j).setFlag(true);		
									}
		//							System.out.println("CircuitProcessing.fcircuitProcessingnew() j: "+j+ " sum: "+sum);
								}
								else
								{
									continue;
								}
							}
		//					System.out.println("CircuitProcessing.fcircuitProcessingnew()"+circuitcheck.toString());
							d.setRequiredTraffic(sum);
							d.setCircuitSet(str);
							demandList.add(d);
						}				
					}	
					else
					{				
						Demand d = new Demand();	
						d.setNetworkId(networkId);
						d.setSrcNodeId(Integer.parseInt(list10_200.get(i).get("SrcNodeId").toString()));
						d.setDestNodeId(Integer.parseInt(list10_200.get(i).get("DestNodeId").toString()));
						d.setProtectionType(list10_200.get(i).get("ProtectionType").toString());	
						d.setClientProtectionType(list10_200.get(i).get("ClientProtectionType").toString());	
						d.setColourPreference(list10_200.get(i).get("ColourPreference").toString());				
						d.setPathType(list10_200.get(i).get("PathType").toString());
						d.setLineRate(list10_200.get(i).get("LineRate").toString());
						d.setCircuitSet(list10_200.get(i).get("CircuitSet").toString());
						d.setRequiredTraffic(Float.parseFloat(list10_200.get(i).get("TotalTraffic").toString()));
						d.setChannelProtection(list10_200.get(i).get("ChannelProtection").toString());
						
						d.setClientProtection(list10_200.get(i).get("ClientProtection").toString());								
						//d.setLambdaBlocking(list10_200.get(i).get("LambdaBlocking").toString());
						d.setNodeInclusion(list10_200.get(i).get("NodeInclusion").toString());
						
						demandList.add(d);	
						
					}
			}/**GreenField Case : END*/
			
			else if(circuitModeGeneration == MapConstants.I_ONE) {/**BrownField : As per the Description*/
				
				
				String[] circuitSetArray = list10_200.get(i).get("CircuitSet").toString().split(",");/** ex : CircuitSet : [1,2,3]*/
				
				/**
				 * Very Important Variable for Best Fit Algo
				 */
				int LINERATE = MapConstants.I_TWOHUNDREAD;
				int PER_GROUP_REQUIRED_TRAFFIC= MapConstants.I_TEN;
				
				/**
				 * Check for TrafficType too in future if require...
				 */
				Demand demandToUpdate = dbService.getDemandService().FindDemand(
																	Integer.parseInt(list10_200.get(i).get("NetworkId").toString()),
																	Integer.parseInt(list10_200.get(i).get("SrcNodeId").toString()),
																	Integer.parseInt(list10_200.get(i).get("DestNodeId").toString()),						
																	list10_200.get(i).get("ProtectionType").toString(),
																	list10_200.get(i).get("ClientProtectionType").toString(),
																	list10_200.get(i).get("ColourPreference").toString(), 
																	list10_200.get(i).get("PathType").toString(), 
																	Integer.parseInt(list10_200.get(i).get("LineRate").toString()), 
																	list10_200.get(i).get("ChannelProtection").toString(),
																	list10_200.get(i).get("ClientProtection").toString());
															
				
				if(demandToUpdate!=null) {/**Matching Demand found in Database*/
					
				
					int remainingCapacityInOldDemand =(LINERATE-(int)(demandToUpdate.getRequiredTraffic()));
							
					
					MainMap.logger.info(" demandToUpdate : Linerate - Capacity "+ remainingCapacityInOldDemand);                   
                    
                                       
					if(remainingCapacityInOldDemand > MapConstants.I_ZERO) {/**Positive : New Demands Can be alloted into*/
						
						/**
						 * Two Cases : 1) New Demand can me mapped completely : Update old demand and mapping or
						 *             2) New Demand can me mapped partially  : Update old demand and mapping with best fit and for rest create an new demand  
						 */
						
					
						/**
						 * Best Fit : Article  1 
						 */						
						int localCircuitIdTrackCount=0, requiredTrafficToSet=0;
						int oldCircuitCount=remainingCapacityInOldDemand/PER_GROUP_REQUIRED_TRAFFIC;
						int newCircuitCount=circuitSetArray.length;
						MainMap.logger.info("oldCircuitCount & newCircuitCount "+oldCircuitCount + " and "+newCircuitCount);
						
						/**Article 1(a) : New Circuits are less or equal to the remaining capacity */
						if(newCircuitCount <= oldCircuitCount) {
							localCircuitIdTrackCount= circuitSetArray.length;
							requiredTrafficToSet = (PER_GROUP_REQUIRED_TRAFFIC*circuitSetArray.length);
						}
						
						/**Article 1(b) : New Circuits are greater than  the remaining capacity */
						else if(newCircuitCount > oldCircuitCount) {
							localCircuitIdTrackCount = oldCircuitCount;
							requiredTrafficToSet =remainingCapacityInOldDemand;
						}						
						
						///System.out.println(" localCircuitIdTrackCount "+ localCircuitIdTrackCount);
												
						String firstCircuitSet ="", secondCircuitSet="";
						firstCircuitSet = makeSubArrayString(circuitSetArray,0,localCircuitIdTrackCount);
						///System.out.println(" firstCircuitSet  "+ firstCircuitSet );
						secondCircuitSet= makeSubArrayString(circuitSetArray,localCircuitIdTrackCount,circuitSetArray.length);											
						///System.out.println(" secondCircuitSet  "+ secondCircuitSet );									
						
						/** Demand Update */
						demandToUpdate.setRequiredTraffic(requiredTrafficToSet);
						demandToUpdate.setCircuitSet(firstCircuitSet);
						dbService.getDemandService().UpdateDemand(demandToUpdate);
						
						
						/** CircuitDemand Mapping Insert */
						for(int cdCount=0; cdCount< localCircuitIdTrackCount;  cdCount++){
							
							///System.out.println(" cdcount : "+ cdCount);
							///System.out.println(" Circuit Id to Set from Set "+Integer.parseInt(circuitSetArray[cdCount]));
							
							CircuitDemandMapping circuitDemandMappingObj = new CircuitDemandMapping();
							circuitDemandMappingObj.setNetworkId(Integer.parseInt(list10_200.get(i).get("NetworkId").toString()));
							circuitDemandMappingObj.setDemandId(demandToUpdate.getDemandId());
							circuitDemandMappingObj.setCircuitId(Integer.parseInt(circuitSetArray[cdCount]));
							dbService.getCircuitDemandMappingService().InsertCircuitDemandMapping(circuitDemandMappingObj);
							
						}
						
						/** NetworkRoute */
						List<NetworkRoute> routeToUpdate = dbService.getNetworkRouteService().FindAllByDemandId(demandToUpdate.getNetworkId(),
																demandToUpdate.getDemandId());
						
						if(routeToUpdate!= null) {/**RWA Executed before and routes exist => Update the Traffic*/
							
							for(NetworkRoute localRouteToUpdate : routeToUpdate) {
								System.out.println("---------------------requiredTrafficToSet "+requiredTrafficToSet);
								localRouteToUpdate.setTraffic(requiredTrafficToSet);
								dbService.getNetworkRouteService().UpdateCommonRouteDemMappedInBrField(localRouteToUpdate);
							}
							
						}
					
						
						///System.out.println(" Final cdCount "+ cdCount);
						
						
						/**
						 * CASE 2 : Overflowing - purely towards new demand creation
						 */
						if(TotalTraffic > remainingCapacityInOldDemand) {/**Towards -> New Demand*/
							
							int capacityToDeal = (int) (TotalTraffic-remainingCapacityInOldDemand);
							int totalDemandToGenerate = (int) Math.ceil((double)capacityToDeal/MapConstants.I_TWOHUNDREAD);
							int perDemandTraffic=0, localTotalTraffic=(int)capacityToDeal;
						
							localCircuitIdTrackCount=0;/**Reset Tracker*/
							
							String localSecondCircuitSet=secondCircuitSet;/**For Further Division based on demand*/
							
							/**
							 * Till all demand Settled up !!
							 */
							while(totalDemandToGenerate!=MapConstants.I_ZERO) {
								
								/**assign traffic per demand*/
								if(localTotalTraffic<LINERATE)
									perDemandTraffic=(int) capacityToDeal%LINERATE;/**Modulo to find out the traffice < linerate*/
								else
									perDemandTraffic=LINERATE;/**traffic with linrate can be assigned*/
								
								/**Local set to clip out the required circuits from the parent set*/
								localSecondCircuitSet= makeSubArrayString(secondCircuitSet.split(","),localCircuitIdTrackCount
										,(localCircuitIdTrackCount+(perDemandTraffic/PER_GROUP_REQUIRED_TRAFFIC)));
								
																
								Demand d = new Demand();	
								d.setNetworkId(networkId);
								d.setSrcNodeId(Integer.parseInt(list10_200.get(i).get("SrcNodeId").toString()));
								d.setDestNodeId(Integer.parseInt(list10_200.get(i).get("DestNodeId").toString()));
								d.setProtectionType(list10_200.get(i).get("ProtectionType").toString());	
								d.setClientProtectionType(list10_200.get(i).get("ClientProtectionType").toString());
								d.setColourPreference(list10_200.get(i).get("ColourPreference").toString());				
								d.setPathType(list10_200.get(i).get("PathType").toString());
								d.setLineRate(list10_200.get(i).get("LineRate").toString());
								d.setCircuitSet(localSecondCircuitSet);
								d.setRequiredTraffic(perDemandTraffic);
								d.setChannelProtection(list10_200.get(i).get("ChannelProtection").toString());
								
								d.setClientProtection(list10_200.get(i).get("ClientProtection").toString());								
								//d.setLambdaBlocking(list10_200.get(i).get("LambdaBlocking").toString());
								d.setNodeInclusion(list10_200.get(i).get("NodeInclusion").toString());
								
								demandList.add(d);
								
								
								/**Readjusting var w.r.t. assigned demand*/
								totalDemandToGenerate--;
								localTotalTraffic-=perDemandTraffic;
								localCircuitIdTrackCount+=(perDemandTraffic/PER_GROUP_REQUIRED_TRAFFIC);
								
								///System.out.println(" localCircuitIdTrackCount : "+localCircuitIdTrackCount);
								
							}
						}
						
					}				
					
				}
				else {/**No Matching Found hence treat all demand as the new one*/
					
					int capacityToDeal = (int) (TotalTraffic);
					int totalDemandToGenerate = (int) Math.ceil((double)capacityToDeal/LINERATE);
					int perDemandTraffic=0, localTotalTraffic=(int)capacityToDeal;
					int localCircuitIdTrackCount=0;					
					
					String localSecondCircuitSet="";
					
					/**
					 * Till all demand Settled up !!
					 */
					while(totalDemandToGenerate!=MapConstants.I_ZERO) {
						
						/**assign traffic per demand*/
						if(localTotalTraffic<LINERATE)
							perDemandTraffic=(int) capacityToDeal%LINERATE;/**Modulo to find out the traffic < linerate*/
						else
							perDemandTraffic=LINERATE;/**traffic with linerate can be assigned*/
						
						/**Local set to clip out the required circuits from the parent set*/
						System.out.println("localCircuitIdTrackCount "+localCircuitIdTrackCount);
						System.out.println("perDemandTraffic "+perDemandTraffic);
						System.out.println(" totalDemandToGenerate "+totalDemandToGenerate);
						
						localSecondCircuitSet= makeSubArrayString(circuitSetArray,localCircuitIdTrackCount
																,(localCircuitIdTrackCount+(perDemandTraffic/PER_GROUP_REQUIRED_TRAFFIC)));
						
						
							Demand d = new Demand();	
							d.setNetworkId(networkId);
							d.setSrcNodeId(Integer.parseInt(list10_200.get(i).get("SrcNodeId").toString()));
							d.setDestNodeId(Integer.parseInt(list10_200.get(i).get("DestNodeId").toString()));
							d.setProtectionType(list10_200.get(i).get("ProtectionType").toString());	
							d.setClientProtectionType(list10_200.get(i).get("ClientProtectionType").toString());
							d.setColourPreference(list10_200.get(i).get("ColourPreference").toString());				
							d.setPathType(list10_200.get(i).get("PathType").toString());
							d.setLineRate(list10_200.get(i).get("LineRate").toString());
							d.setCircuitSet(localSecondCircuitSet);
							d.setRequiredTraffic(perDemandTraffic);
							d.setChannelProtection(list10_200.get(i).get("ChannelProtection").toString());
							
							d.setClientProtection(list10_200.get(i).get("ClientProtection").toString());								
							//d.setLambdaBlocking(list10_200.get(i).get("LambdaBlocking").toString());
							d.setNodeInclusion(list10_200.get(i).get("NodeInclusion").toString());	
							
							
							demandList.add(d);
							
							
							/**Readjusting var w.r.t. assigned demand*/
							totalDemandToGenerate--;
							localTotalTraffic-=perDemandTraffic;
							localCircuitIdTrackCount+=(perDemandTraffic/PER_GROUP_REQUIRED_TRAFFIC);
							
					}
				}
			}/**BrownField : Case END */
			
			
		}
		
		/**
		 * For 10G Linerate Traffic having capacity of 1g/1.25g/2.5g
		 */
		for (int i = 0; i < listx_10.size(); i++) 
		{
			int nCircuits =Integer.parseInt(listx_10.get(i).get("CircuitCount").toString());
			float TotalTraffic =Float.parseFloat(listx_10.get(i).get("TotalTraffic").toString());
			System.out.println("CircuitProcessing.fcircuitProcessingnew() TotalTraffic "+TotalTraffic);
			System.out.println("CircuitProcessing.fcircuitProcessingnew() nCircuits "+nCircuits);
			System.out.println("CircuitProcessing.fcircuitProcessingnew() src: " + listx_10.get(i).get("SrcNodeId").toString() );
			System.out.println("CircuitProcessing.fcircuitProcessingnew() dest: " + listx_10.get(i).get("DestNodeId").toString() );
			
			
			/**
			 * Check for TrafficType too in future if require...
			 */
			Demand demandToUpdate = dbService.getDemandService().FindDemand(
																Integer.parseInt(listx_10.get(i).get("NetworkId").toString()),
																Integer.parseInt(listx_10.get(i).get("SrcNodeId").toString()),
																Integer.parseInt(listx_10.get(i).get("DestNodeId").toString()),						
																listx_10.get(i).get("ProtectionType").toString(),
																listx_10.get(i).get("ClientProtectionType").toString(),
																listx_10.get(i).get("ColourPreference").toString(), 
																listx_10.get(i).get("PathType").toString(), 
																Integer.parseInt(listx_10.get(i).get("LineRate").toString()), 
																listx_10.get(i).get("ChannelProtection").toString(),
																listx_10.get(i).get("ClientProtection").toString());
			
			
			System.out.println(" nCircuits "+nCircuits + " and TotalTraffic "+TotalTraffic);
			
			if(demandToUpdate!= null)
				TotalTraffic+=demandToUpdate.getRequiredTraffic();
			
			System.out.println("TotalTraffic after final update "+TotalTraffic);
			
			if((nCircuits<16)&&(TotalTraffic<=10))
			{
				//place on one 10G MPN
				Demand d = new Demand();	
				d.setNetworkId(networkId);
				d.setSrcNodeId(Integer.parseInt(listx_10.get(i).get("SrcNodeId").toString()));
				d.setDestNodeId(Integer.parseInt(listx_10.get(i).get("DestNodeId").toString()));
				d.setProtectionType(listx_10.get(i).get("ProtectionType").toString());
				d.setClientProtectionType(listx_10.get(i).get("ClientProtectionType").toString());	
				d.setColourPreference(listx_10.get(i).get("ColourPreference").toString());				
				d.setPathType(listx_10.get(i).get("PathType").toString());
				d.setLineRate(listx_10.get(i).get("LineRate").toString());	
				d.setCircuitSet(listx_10.get(i).get("CircuitSet").toString());
				d.setRequiredTraffic(Float.parseFloat(listx_10.get(i).get("TotalTraffic").toString()));
				d.setChannelProtection(listx_10.get(i).get("ChannelProtection").toString());
				
				d.setClientProtection(listx_10.get(i).get("ClientProtection").toString());
				//d.setLambdaBlocking(listx_10.get(i).get("LambdaBlocking").toString());
				d.setNodeInclusion(listx_10.get(i).get("NodeInclusion").toString());
				
			
				/**
				 * DemandToUpdate Will be only found in case of Brownfield
				 */
				
				if(demandToUpdate == null/* && circuitModeGeneration == MapConstants.I_ZERO*/) /** Greenfield OR Brownfield with no demand match */
					demandList.add(d);	
				else if(demandToUpdate!= null && circuitModeGeneration == MapConstants.I_ONE) {/** Brownfield */
					
					/** Demand */
					demandToUpdate.setRequiredTraffic(Float.parseFloat(listx_10.get(i).get("TotalTraffic").toString()));
					demandToUpdate.setCircuitSet(listx_10.get(i).get("CircuitSet").toString());
					dbService.getDemandService().UpdateDemand(demandToUpdate);
					
					
				
					/** CircuitDemandMapping Insert */
					String[] localCircuitSetArray = listx_10.get(i).get("CircuitSet").toString().split(",");
					for(int cdCount=0; cdCount< localCircuitSetArray.length;  cdCount++){
						
						CircuitDemandMapping circuitDemandMappingObj = new CircuitDemandMapping();
						circuitDemandMappingObj.setNetworkId(Integer.parseInt(listx_10.get(i).get("NetworkId").toString()));
						circuitDemandMappingObj.setDemandId(demandToUpdate.getDemandId());
						circuitDemandMappingObj.setCircuitId(Integer.parseInt(localCircuitSetArray[cdCount]));
						dbService.getCircuitDemandMappingService().InsertCircuitDemandMapping(circuitDemandMappingObj);
						
					}
					
					/** NetworkRoute */
					List<NetworkRoute> routeToUpdate = dbService.getNetworkRouteService().FindAllByDemandId(Integer.parseInt(listx_10.get(i).get("NetworkId").toString()),
															demandToUpdate.getDemandId());
					
					if(routeToUpdate!= null) {/**RWA Executed before and routes exist => Update the Traffic*/
						
						for(NetworkRoute localRouteToUpdate : routeToUpdate) {
							System.out.println("---------------------requiredTrafficToSet "+Float.parseFloat(listx_10.get(i).get("TotalTraffic").toString()));
							localRouteToUpdate.setTraffic(Float.parseFloat(listx_10.get(i).get("TotalTraffic").toString()));
							dbService.getNetworkRouteService().UpdateCommonRouteDemMappedInBrField(localRouteToUpdate);
						}
						
					}
					
					
				}				
			}
			else  if((TotalTraffic>10))
				{
					int src=Integer.parseInt(listx_10.get(i).get("SrcNodeId").toString());
					int dest=Integer.parseInt(listx_10.get(i).get("DestNodeId").toString());
					String prottype= (listx_10.get(i).get("ProtectionType").toString());				
					String colour = (listx_10.get(i).get("ColourPreference").toString());				
					String path = (listx_10.get(i).get("PathType").toString());
					String clientprot = (listx_10.get(i).get("ClientProtectionType").toString());				
					String channelProctection = (listx_10.get(i).get("ChannelProtection").toString());
					List<Circuit> cirs = dbService.getCircuitService().FindCircuitsFor10GMPN(networkId, src, dest,prottype,colour,path,clientprot,channelProctection,circuitModeGeneration);
					ArrayList<CircuitCheck> circuitcheck = new ArrayList<CircuitCheck>();				
				
					for (int j = 0; j < cirs.size(); j++) {
						CircuitCheck c = new CircuitCheck();
						c.setCircuitId(cirs.get(j).getCircuitId());
						c.setRequiredTraffic(cirs.get(j).getRequiredTraffic());	
						c.setFlag(false);
						circuitcheck.add(c);
					}
	
					boolean flag=true;
				
					//start grouping the circuits in bunch of 10G
					while(IsEntryPending(circuitcheck))
					{
						float sum =0;
						String str="";
						Demand d = new Demand();	
						d.setNetworkId(networkId);
						d.setSrcNodeId(Integer.parseInt(listx_10.get(i).get("SrcNodeId").toString()));
						d.setDestNodeId(Integer.parseInt(listx_10.get(i).get("DestNodeId").toString()));
						d.setProtectionType(listx_10.get(i).get("ProtectionType").toString());	
						d.setClientProtectionType(listx_10.get(i).get("ClientProtectionType").toString());
						d.setColourPreference(listx_10.get(i).get("ColourPreference").toString());				
						d.setPathType(listx_10.get(i).get("PathType").toString());
						d.setLineRate(listx_10.get(i).get("LineRate").toString());
						d.setChannelProtection(listx_10.get(i).get("ChannelProtection").toString());
						
						d.setClientProtection(listx_10.get(i).get("ClientProtection").toString());
						//d.setLambdaBlocking(listx_10.get(i).get("LambdaBlocking").toString());
						d.setNodeInclusion(listx_10.get(i).get("NodeInclusion").toString());
					
						if(demandToUpdate!=null && (flag == true)) {
							sum+=demandToUpdate.getRequiredTraffic();
						}
						
						System.out.println(" sum after addition from bf if exist ... "+sum);
						
						for (int j = 0; j < circuitcheck.size(); j++) 
						{						
							if(circuitcheck.get(j).isFlag()==false)
							{
								if(sum < 10)
								{
									sum = sum + circuitcheck.get(j).getRequiredTraffic();
									str=str.concat(""+circuitcheck.get(j).getCircuitId()).concat(",");									
									circuitcheck.get(j).setFlag(true);	
									System.out.println(" sum in each <10 "+ sum);
								}	
								else if(sum > 10) 
								{
									sum = sum - circuitcheck.get(j-1).getRequiredTraffic();	
									circuitcheck.get(j-1).setFlag(false);
									//chop last ','
									str=str.substring(0, str.length()-1);
									//chop last circuit
									str = str.substring(0, str.lastIndexOf(","));
									
									System.out.println(" sum in each >10 "+ sum);
									break;							
								}
	//							System.out.println("CircuitProcessing.fcircuitProcessingnew() j: "+j+ " sum: "+sum);
							}
							else
							{
								continue;
							}
						}
						
						
						System.out.println(" Final Sum : " +sum );
						
						
						if(demandToUpdate == null && circuitModeGeneration == MapConstants.I_ZERO) { /** Greenfield */
							System.out.println(" demand to update null after final sum");
							d.setRequiredTraffic(sum);
							d.setCircuitSet(str);
							demandList.add(d);								
						}
						else if(demandToUpdate!= null && circuitModeGeneration == MapConstants.I_ONE) {/** Brownfield */
							System.out.println(" demand to update not null after final sum");
							/** Demand */
							if(((sum)<=10) && (flag==true)) {
								
								System.out.println(" Under sum<=10");
								demandToUpdate.setRequiredTraffic(sum-demandToUpdate.getRequiredTraffic());
								demandToUpdate.setCircuitSet(str);
								dbService.getDemandService().UpdateDemand(demandToUpdate);
								flag=false;
								
								/** CircuitDemandMapping Insert */
								System.out.println(" str circuit set "+str);
								String[] localCircuitSetArray = str.split(",");
								for(int cdCount=0; cdCount< localCircuitSetArray.length;  cdCount++){
									
									CircuitDemandMapping circuitDemandMappingObj = new CircuitDemandMapping();
									circuitDemandMappingObj.setNetworkId(Integer.parseInt(listx_10.get(i).get("NetworkId").toString()));
									circuitDemandMappingObj.setDemandId(demandToUpdate.getDemandId());
									circuitDemandMappingObj.setCircuitId(Integer.parseInt(localCircuitSetArray[cdCount]));
									dbService.getCircuitDemandMappingService().InsertCircuitDemandMapping(circuitDemandMappingObj);
									
								}
								
								/** NetworkRoute */
								List<NetworkRoute> routeToUpdate = dbService.getNetworkRouteService().FindAllByDemandId(Integer.parseInt(listx_10.get(i).get("NetworkId").toString()),
																		demandToUpdate.getDemandId());
								
								if(routeToUpdate!= null) {/**RWA Executed before and routes exist => Update the Traffic*/
									
									for(NetworkRoute localRouteToUpdate : routeToUpdate) {
										System.out.println("---------------------requiredTrafficToSet "+Float.parseFloat(listx_10.get(i).get("TotalTraffic").toString()));
										localRouteToUpdate.setTraffic(Float.parseFloat(listx_10.get(i).get("TotalTraffic").toString()));
										dbService.getNetworkRouteService().UpdateCommonRouteDemMappedInBrField(localRouteToUpdate);
									}
									
								}
							
							}
							else {
								System.out.println("Else ");
								d.setRequiredTraffic(sum);
								d.setCircuitSet(str);
								demandList.add(d);
							}
							
						}						
						

					}			
				}
				
		

			

			
			MainMap.logger.info("Final demandList listx_10 : " + demandList);
		}

		/** for 100g client and 100g linerate => Same for GreenField and Brown Field as well */ 
		for (int i = 0; i < list100_100.size(); i++) 
		{
			Demand d = new Demand();	
			d.setNetworkId(networkId);
			d.setSrcNodeId(Integer.parseInt(list100_100.get(i).get("SrcNodeId").toString()));
			d.setDestNodeId(Integer.parseInt(list100_100.get(i).get("DestNodeId").toString()));
			d.setProtectionType(list100_100.get(i).get("ProtectionType").toString());
			d.setClientProtectionType(list100_100.get(i).get("ClientProtectionType").toString());
			d.setColourPreference(list100_100.get(i).get("ColourPreference").toString());				
			d.setPathType(list100_100.get(i).get("PathType").toString());
			d.setLineRate(list100_100.get(i).get("LineRate").toString());
			d.setCircuitSet(list100_100.get(i).get("CircuitId").toString());
			d.setRequiredTraffic(Float.parseFloat(list100_100.get(i).get("RequiredTraffic").toString()));
			d.setChannelProtection(list100_100.get(i).get("ChannelProtection").toString());
			
			d.setClientProtection(list100_100.get(i).get("ClientProtection").toString());
			//d.setLambdaBlocking(list100_100.get(i).get("LambdaBlocking").toString());
			d.setNodeInclusion(list100_100.get(i).get("NodeInclusion").toString());
			demandList.add(d);			
		}
		
		//insert the demands prepared
		int demandId=0;
		 for (int i = 0; i < demandList.size(); i++) 
		    {
				 try 
				 {				
					 /**Demand Insertion*/
					 demandId= dbService.getDemandService().InsertDemand(demandList.get(i), networkInfoMap);		
					 
					 /**In Memory Demand Insert For RWA Use : Fresh Demands*/
					 demandList.get(i).setDemandId(demandId);
					 dbService.getDemandService().InsertInMemoryDemand(demandList.get(i));
						
					 System.out.println("CircuitProcessing.fcircuitProcessing() demandList "+demandList.get(i));
					 
					 
					 Demand listdemands = dbService.getDemandService().FindDemand(networkId, demandId);
					 String circuits[]= listdemands.getCircuitSet().split(",");
					 for (int j = 0; j < circuits.length; j++) {
						 CircuitDemandMapping cd = new CircuitDemandMapping();
						 cd.setCircuitId(Integer.parseInt(circuits[j].toString()));
						 cd.setNetworkId(listdemands.getNetworkId());
						 cd.setDemandId(listdemands.getDemandId());
						 dbService.getCircuitDemandMappingService().InsertCircuitDemandMapping(cd);				
					 }
					 
				 } catch (SQLException e) 
				 {
					 e.printStackTrace();
				 }					
			}
		    
		/* if(circuitModeGeneration == MapConstants.I_ZERO){ *//**For Green Field Only*//*
			//insert the circuit demand mapping
			 List <Demand> listdemands = dbService.getDemandService().FindAllByNetworkId(networkId);
			 System.out.println(" listdemands "+ listdemands);
			 
			 for (int i = 0; i < listdemands.size(); i++) 
			 {			
				 String circuits[]= listdemands.get(i).getCircuitSet().split(",");
				 for (int j = 0; j < circuits.length; j++) {
					 CircuitDemandMapping cd = new CircuitDemandMapping();
					 cd.setCircuitId(Integer.parseInt(circuits[j].toString()));
					 cd.setNetworkId(listdemands.get(i).getNetworkId());
					 cd.setDemandId(listdemands.get(i).getDemandId());
					 dbService.getCircuitDemandMappingService().InsertCircuitDemandMapping(cd);				
				}
			 }
		 }*/
		
	}
	
	public boolean IsEntryPending(List<CircuitCheck> c)
	{	
		boolean flag = false;
		for (int i = 0; i < c.size(); i++) 
		{
			if(c.get(i).isFlag()==false)
			{
				flag =true;
			}							
		}
		///System.out.println("IsEntryPending()"+flag);
		return flag;
	}
	
	/**
	 * 
	 * @param sourceArray
	 * @param begin
	 * @param end
	 * @return
	 */
	public String makeSubArrayString(String [] sourceArray, int begin, int end) {
		
		String [] subArray = Arrays.copyOfRange(sourceArray, begin, end);
		String finalString =Arrays.toString(subArray);		
		finalString = finalString.substring(1, finalString.length()-1).replaceAll("\\s+","");
		System.out.println("makeSubArrayString :  "+ finalString);		
		return finalString;		
	}
	
	/**
	 * 
	 * @param whole
	 * @param parts
	 * @return
	 */
	private static int[] splitIntoParts(int whole, int parts) {
	    int[] arr = new int[parts];
	    for (int i = 0; i < arr.length; i++)
	        whole -= arr[i] = (whole + parts - i - 1) / (parts - i);
	    return arr;
	}
	
	
	
}
