/**
 * 
 */
package application.controller;



import java.sql.SQLException;
import java.util.*;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;

import application.MainMap;
import application.constants.MapConstants;
import application.constants.SMConstants;
import application.model.CircuitDemandMapping;
import application.model.Demand;
import application.model.Link;
import application.model.NetworkRoute;
import application.model.Node;
import application.model.SnmpModel;
import application.model.SnmpModelNetworkRoute;
import application.service.DbService;

import application.service.SnmpManager;

/**
 * @author hp
 * @brief  This Controller Called on request of the RWA algorithm execution on saved Network Topology.
 *         It fetches all the relevant data from DB using db service and prepare the snmp object in          
 *         Order to call SnmpService. 
 */
@Service
public class MapWebRwaAlgoExeController {


	/**
	 * @brief Fetch Data of Node, Topology and Link from DB, set to SnmpObj and then final call to snmp
	 * @param dbService
	 * @param netowrkId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	public int rwaExecute(int networkId, boolean brownFieldFlag, DbService dbService) throws Exception{
		
		/**
		 * Brown Field Check : In case of Brown Field NetworkIdBrownField would be -1
		 */
		int greenFieldNetworkId=MapConstants.BrownFieldBfId; /** Initially Set brownfield id to -1*/
		String networkFieldType = MapConstants.GreenField;
		
		if(brownFieldFlag == true){
			greenFieldNetworkId= dbService.getNetworkService().GetNetworkInfoByBFNetworkId(networkId)
					.getNetworkId();
			networkFieldType = MapConstants.BrownField;
		}
			
		
		MainMap.logger.info(" greenFieldNetworkId : " + greenFieldNetworkId);
		
		/**Logger Start*/		
		 MainMap.logger = MainMap.logger.getLogger(MapWebRwaAlgoExeController.class.getName());
		
		 MainMap.logger.info("--------------------------RWA Controller Start---------------------------------------");
		 MainMap.logger.info("DB Fetch Start ..");
		 
		 /**Changed to NetworkId based for Brown field support*/
		 List<Node> listNode		    			 = dbService.getNodeService().FindAll(networkId);
		 List<Map<String, Object>> listLink 	     = dbService.getLinkService().FindAllLinksWithNetworkId(networkId);
		 List<Map<String, Object>> listTopology  	 = dbService.getTopologyService().QueryTopologyWithNodeTypeWithNetowrkId(networkId);
		 List<Demand> listDemand 					 = /*dbService.getDemandService().FindAllByNetworkId(networkId);*/
				 					dbService.getDemandService().FindAllInMemoryDemandByNetworkId(networkId);
				 							
		 List<NetworkRoute> listModifiedDemand 			 = dbService.getNetworkRouteService().FindCommonRouteInBrField(greenFieldNetworkId, networkId);
				 										
		 List<NetworkRoute> listOldDemand = null;
		 
		 System.out.println("listModifiedDemand : "+listModifiedDemand);
		 
         if(listModifiedDemand==null) {/**No Routes Modified in BrownField*/
        	 listOldDemand =  dbService.getNetworkRouteService().FindAllByNetworkId(greenFieldNetworkId);
         }
         else if(listModifiedDemand!=null) {/**routes are modified in the present Brownfield*/
        	 listOldDemand = listModifiedDemand;
         }
				 										
         						
		 System.out.println(" listDemand "+ listDemand);
		 System.out.println(" Final listOldDemand "+ listOldDemand);
		 
		 int numOfNodes = listNode.size();	///dbService.getNodeService().Count();
		 int numOfLinks = listLink.size();  ///dbService.getLinkService().Count();
		 int numofDemands	= listDemand.size();///dbService.getDemandService().Count();
		 int numofOldDemands=0;///= listOldDemand.size();
		 int Count=0;		
		 
		 
		 
		 MainMap.logger.info("numOfNodes:- "+numOfNodes+" numOfLinks:- "+numOfLinks+ " numofDemands :- "+numofDemands);
		 
		 /**
		  * Snmp Object Creation 
		  */
		 for(NetworkRoute row : listOldDemand){
			 if(row.getWavelengthNo()!= MapConstants.I_ZERO){
				 numofOldDemands++;
			 }
		 }		 
	
		 SnmpModel snmpModelObj =  new SnmpModel(numOfNodes,numOfLinks,numofDemands,numofOldDemands, networkFieldType);
		 
		 ///MainMap.logger.info("Network Id :- " +Integer.parseInt(listNode.get(0).get("NetworkId").toString()));
		 
		 snmpModelObj.setNetworkId(/*Integer.parseInt(listNode.get(0).get("NetworkId").toString())*/networkId);
		 snmpModelObj.setNumberOfNodes(numOfNodes);
		 snmpModelObj.setNumberofLinks(numOfLinks);
		 snmpModelObj.setNumberofOldDemands(numofOldDemands);
		 
		
		 Count = 0; /**reset counter*/
		 
		 /**Link Data*/
		 for (Map<String, Object> row : listLink) {	
			
			 MainMap.logger.info("Source Node :- "+Integer.parseInt(row.get("SrcNode").toString())+
					 			 "Destination Node :-"+Integer.parseInt(row.get("DestNode").toString())+
					 			 "Color :- "+Integer.parseInt(row.get("Colour").toString())+
					 			"Metric Cost :- "+Integer.parseInt(row.get("MetricCost").toString())+
					 			"Length :- "+Float.parseFloat(row.get("Length").toString())/*+
					 			"Span Loss :- "+Float.parseFloat(row.get("SpanLoss").toString())+
					 			"Capacity :- "+Integer.parseInt(row.get("Capacity").toString())+
					 			"Fiber Type :- "+Integer.parseInt(row.get("FiberType").toString())*/);
					 
			 
			 
			 snmpModelObj.snmpModelLink[Count].setSrc(Integer.parseInt(row.get("SrcNode").toString()));
			 snmpModelObj.snmpModelLink[Count].setTarget(Integer.parseInt(row.get("DestNode").toString()));
			 snmpModelObj.snmpModelLink[Count].setColor(Integer.parseInt(row.get("Colour").toString()));
			 snmpModelObj.snmpModelLink[Count].setCost(Integer.parseInt(row.get("MetricCost").toString()));
			 snmpModelObj.snmpModelLink[Count].setSpanLength(Float.parseFloat(row.get("Length").toString()));
			 snmpModelObj.snmpModelLink[Count].setSpanLoss(Float.parseFloat(row.get("SpanLoss").toString()));
			 snmpModelObj.snmpModelLink[Count].setSrlg(Integer.parseInt(row.get("SrlgId").toString()));
			 snmpModelObj.snmpModelLink[Count].setSrlg(Integer.parseInt(row.get("SrlgId").toString()));
			 snmpModelObj.snmpModelLink[Count].setLinkType(row.get("LinkType").toString());
			 snmpModelObj.snmpModelLink[Count].setLinkId(Integer.parseInt(row.get("LinkId").toString()));
			 /*snmpModelObj.snmpModelLink[Count].setCapacity(Integer.parseInt(row.get("Capacity").toString()));
			 snmpModelObj.snmpModelLink[Count].setFiberType(Integer.parseInt(row.get("FiberType").toString()));*/
				
			 Count++;
		 }		
		 
		 Count = 0; /**reset counter*/
		 
		 Node node=new Node();
		 
		 System.out.println(" listTopology "+ listTopology);
		 /**Topology Data*/
		 for (Map<String, Object> row : listTopology) {	
			 
			 MainMap.logger.info("Node Id :- "+row.get("NodeId")+
					 			"Node Type :- "+row.get("NodeType")+
					 			"East :- "+row.get("Dir1")+
					 			"West :- "+row.get("Dir2")+
					 			"North :- "+row.get("Dir3")+
					 			"South :- "+row.get("Dir4")+
					 			"NE :- "+row.get("Dir5")+
					 			"NW :- "+row.get("Dir6")+
					 			"SE :- "+row.get("Dir7")+
					 			"SW :- "+row.get("Dir8"));
			 System.out.println(" Count "+ Count);
			 snmpModelObj.snmpModelNode[Count].setNodeType(Integer.parseInt(row.get("NodeType").toString()));
			 node=dbService.getNodeService().FindNode(networkId, Integer.parseInt(row.get("NodeId").toString()));
			 snmpModelObj.snmpModelNode[Count].setNodeCapacity(Integer.parseInt(node.getCapacity().toString()));			 
			 snmpModelObj.snmpModelNode[Count].setNodeId(Integer.parseInt(row.get("NodeId").toString()));
			 snmpModelObj.snmpModelNode[Count].setEast(Integer.parseInt(row.get("Dir1").toString()));
			 snmpModelObj.snmpModelNode[Count].setWest(Integer.parseInt(row.get("Dir2").toString()));
			 snmpModelObj.snmpModelNode[Count].setNorth(Integer.parseInt(row.get("Dir3").toString()));
			 snmpModelObj.snmpModelNode[Count].setSouth(Integer.parseInt(row.get("Dir4").toString()));
			 snmpModelObj.snmpModelNode[Count].setNortheast(Integer.parseInt(row.get("Dir5").toString()));
			 snmpModelObj.snmpModelNode[Count].setNorthwest(Integer.parseInt(row.get("Dir6").toString()));
			 snmpModelObj.snmpModelNode[Count].setSoutheast(Integer.parseInt(row.get("Dir7").toString()));
			 snmpModelObj.snmpModelNode[Count].setSouthwest(Integer.parseInt(row.get("Dir8").toString()));
			 
			 Count++;
			 
		 }	
		 
		 
		 Count=0;
		 
		 /**Demand Data*/
		 for (Demand row : listDemand) {	
			 
			/* MainMap.logger.info("Network Id :- "+row.get("NetworkId")+
					 			" Demand Id :- "+row.get("DemandId")+
					 			" Source Node :- "+row.get("SrcNodeId")+
					 			" Dest Node :- "+row.get("DestNodeId")+
					 			" Required traffic :- "+row.get("RequiredTraffic")+
					 			" Protection Type :- "+row.get("ProtectionType")+
					 			" Color preferences :- "+row.get("ColourPreference")+
					 			"  Line Rate :- "+row.get("LineRate"));*/
			 
			 snmpModelObj.snmpModelDemand[Count].setNetworkId(row.getNetworkId());
			 snmpModelObj.snmpModelDemand[Count].setDemandId(row.getDemandId());
			 snmpModelObj.snmpModelDemand[Count].setSrcNode(row.getSrcNodeId());
			 snmpModelObj.snmpModelDemand[Count].setDestNode(row.getDestNodeId());
			 snmpModelObj.snmpModelDemand[Count].setRequiredTraffic(row.getRequiredTraffic());
			 
			 /******This condition has changed. Client and Channel protection
			  * 	fields will be send seperately to RWA.       *******/
			 
			 //If Client ptc is none and channel protection is Yes , send 'Channel' as protection to RWA
//			 if(row.getProtectionType().toString().equalsIgnoreCase(SMConstants.PtcTypeNoneStr) &&
//					 row.getChannelProtection().toString().equalsIgnoreCase(MapConstants.Yes))
//				 snmpModelObj.snmpModelDemand[Count].setProtectionType(SMConstants.channelPtcTypeStr);
//			 else
//				 snmpModelObj.snmpModelDemand[Count].setProtectionType(row.getProtectionType());
			 
			 snmpModelObj.snmpModelDemand[Count].setProtectionType(row.getProtectionType());
			 
		   //snmpModelObj.snmpModelDemand[Count].setProtectionType(row.get("ProtectionType").toString());
			 snmpModelObj.snmpModelDemand[Count].setColorPreference(row.getColourPreference());
			 snmpModelObj.snmpModelDemand[Count].setPathType(row.getPathType());
			 snmpModelObj.snmpModelDemand[Count].setLineRate(row.getLineRate());
			 snmpModelObj.snmpModelDemand[Count].setChannelProtection(row.getChannelProtection());
			 snmpModelObj.snmpModelDemand[Count].setClientProtection(row.getClientProtection());
			 snmpModelObj.snmpModelDemand[Count].setLambdaBlocking(row.getLambdaBlocking());
			 snmpModelObj.snmpModelDemand[Count].setNodeInclusion(row.getNodeInclusion());
			 
			 Count++;
			 
		 }	
		 
		 Count = 0;
		 
		 
		 /**Old Demand Data*/
		 for (NetworkRoute row : listOldDemand) {	 

			 System.out.println(" Row start for old demand ");
			 if(row.getWavelengthNo()!= MapConstants.I_ZERO)/**Check out for the one which Lambda been assigned*/
			 {
			
				 Demand oldDemandObj = dbService.getDemandService().FindDemand(/*row.getNetworkId()*/networkId, row.getDemandId());
				 															/**Find Demand from the new network which demand id exist in Green Field*/
				 
				 /**
				  * Rest of the data been taken from the BrownField NetworkRoute instead of the GreenField because demand might have changed !
				  */
				 NetworkRoute oldToNewMappingObj = dbService.getNetworkRouteService().FindAllByDemandId(networkId,row.getDemandId(),row.getRoutePriority());
				 
				 snmpModelObj.snmpModelOldDemand[Count].setNetworkId(oldToNewMappingObj.getNetworkId());
				 snmpModelObj.snmpModelOldDemand[Count].setDemandId(oldToNewMappingObj.getDemandId());
				 snmpModelObj.snmpModelOldDemand[Count].setRoutePriority(oldToNewMappingObj.getRoutePriority());
				 snmpModelObj.snmpModelOldDemand[Count].setPath(oldToNewMappingObj.getPath());
				 snmpModelObj.snmpModelOldDemand[Count].setLinkIdSet(oldToNewMappingObj.getPathLinkId());
				 snmpModelObj.snmpModelOldDemand[Count].setPathType(oldToNewMappingObj.getPathType());			
				 snmpModelObj.snmpModelOldDemand[Count].setTraffic(oldToNewMappingObj.getTraffic());
				 snmpModelObj.snmpModelOldDemand[Count].setWavelengthNo(oldToNewMappingObj.getWavelengthNo());
				 snmpModelObj.snmpModelOldDemand[Count].setLineRate(oldToNewMappingObj.getLineRate());
				 snmpModelObj.snmpModelOldDemand[Count].setOsnr(oldToNewMappingObj.getOsnr());
				 snmpModelObj.snmpModelOldDemand[Count].setRegeneratorLoc(oldToNewMappingObj.getRegeneratorLoc());
				 snmpModelObj.snmpModelOldDemand[Count].setRegeneratorLocOsnr(oldToNewMappingObj.getRegeneratorLocOsnr());
				 snmpModelObj.snmpModelOldDemand[Count].setThreeRLocationHeadToTail(oldToNewMappingObj.getThreeRLocationHeadToTail());
				 snmpModelObj.snmpModelOldDemand[Count].setLineRate(oldToNewMappingObj.getLineRate());
				 snmpModelObj.snmpModelOldDemand[Count].setProtectionType(
						 	oldDemandObj.getProtectionType());
				 			/**DBG => Need to check later for multiple entries*/
				 
				 
				 Count++;
			 }
			 else{
				 System.out.println(" With Wavelength Zero : DemandId  "+ row.getDemandId());
			 }
			 
		 }	
		
		 Count = 0;/**Reset Counter*/
		 
		 /**Final Call to RWA Execution*/
		 SnmpManager snmpManagerObj = new SnmpManager();
		 SnmpModelNetworkRoute snmpModelNetworkRouteresObj = new SnmpModelNetworkRoute();
		 
		 if(numofDemands==MapConstants.I_ZERO)
			 return MapConstants.ZERO_DEMAND_RWA;
		 else snmpModelNetworkRouteresObj =  snmpManagerObj.SnmpRwaExecution(snmpModelObj,dbService);
		 
		
		 
		 /**
		  * Insert Old NetworkRoute from Green Field using Static Global HashMap To Brown Field without any modification : Shifted Again back while CircuitSave
		  */ 
		 
//		 dbService.getNetworkRouteService().DeleteNetworkRoute(networkId); /**First Flush the table for any previous output stored*/
//		 
//		 Iterator<Entry<Integer, Object>> oldDemandHashMapIterator = MapWebCommonAPIs.NestedStaticClass.oldDemandObjMap.entrySet().iterator();
//		
//		 System.out.println(" MapWebCommonAPIs.NestedStaticClass.oldDemandObjMap "+ MapWebCommonAPIs.NestedStaticClass.oldDemandObjMap);
//	
//		 while(oldDemandHashMapIterator.hasNext()){
//		
//			@SuppressWarnings("rawtypes")
//			Map.Entry oldDemandHashMapIteratorPair = (Map.Entry)oldDemandHashMapIterator.next();
//			System.out.println(oldDemandHashMapIteratorPair.getKey() + " and " + oldDemandHashMapIteratorPair.getValue());
//			
//			Demand oldDemandObjToCopy = (Demand) oldDemandHashMapIteratorPair.getValue();
//			
//			System.out.println(" oldDemandObjToCopy "+ oldDemandObjToCopy);
//			System.out.println(" greenFieldNetworkId " + greenFieldNetworkId);
//			try 
//			{	
//				/**NetworkRoute*/
//				oldDemandObjToCopy.setNetworkId(greenFieldNetworkId);/**Assign 
//				 GreenField Network Id to the Old Demand Map for Search*/
//				
//				List<NetworkRoute> oldNetworkRoutes = dbService.getNetworkRouteService().FindAllByDemandId
//					      (oldDemandObjToCopy.getNetworkId(),oldDemandObjToCopy.getDemandId());		
//					
//				System.out.println("oldNetworkRoutes "+oldNetworkRoutes);				
//				
//				for(NetworkRoute networkRouteRowObj : oldNetworkRoutes){
//					networkRouteRowObj.setNetworkId(networkId);
//					dbService.getNetworkRouteService().InsertNetworkRoute(networkRouteRowObj);
//				}
//				
//				
//			} catch (SQLException e) 
//			{
//				e.printStackTrace();
//			}	
//			
//		}		 
		 
		 /**Store the response back to db*/
		 if(snmpModelNetworkRouteresObj != null){
			
			 System.out.println(" networkId to flush for NetworkRoute : "+networkId);
			 
			 
			 /**Retrieve Path Info and Store*/
			 int rwapPathCount  = snmpModelNetworkRouteresObj.getPathOutputCount();
			
			 for(int i=0; i<rwapPathCount; i++){
				 
				 MainMap.logger.info("Count :- "+i);
				 MainMap.logger.info(
						 
								 " Networkid :- "+ snmpModelNetworkRouteresObj.snmpModelNetworkRoutePathInfo[i].getNetworkId() +
								 " Demandid :- "+ snmpModelNetworkRouteresObj.snmpModelNetworkRoutePathInfo[i].getDemandId() +
								 " Route Priority :- "+ snmpModelNetworkRouteresObj.snmpModelNetworkRoutePathInfo[i].getRoutePriority() +
								 " Traffic :- "+ snmpModelNetworkRouteresObj.snmpModelNetworkRoutePathInfo[i].getTraffic()+
						 		 " Path :- "+ snmpModelNetworkRouteresObj.snmpModelNetworkRoutePathInfo[i].getPath() +
						 		 " PathType :- "+ snmpModelNetworkRouteresObj.snmpModelNetworkRoutePathInfo[i].getPathType() +
					             " Osnr :- "+ snmpModelNetworkRouteresObj.snmpModelNetworkRoutePathInfo[i].getOsnr() +
					             " Wavelength :- "+ snmpModelNetworkRouteresObj.snmpModelNetworkRoutePathInfo[i].getWavelengthNo() +
					             " Regenerator Location :- "+ snmpModelNetworkRouteresObj.snmpModelNetworkRoutePathInfo[i].getRegeneratorLocations()+
					             " Error String :- "+ snmpModelNetworkRouteresObj.snmpModelNetworkRoutePathInfo[i].getError());
				 
				 
				 NetworkRoute  networkRouteObj = new NetworkRoute();
				 
				 networkRouteObj.setNetworkId(snmpModelNetworkRouteresObj.snmpModelNetworkRoutePathInfo[i].getNetworkId());
				 networkRouteObj.setDemandId(snmpModelNetworkRouteresObj.snmpModelNetworkRoutePathInfo[i].getDemandId());
				 networkRouteObj.setRoutePriority(snmpModelNetworkRouteresObj.snmpModelNetworkRoutePathInfo[i].getRoutePriority());
				 networkRouteObj.setTraffic(snmpModelNetworkRouteresObj.snmpModelNetworkRoutePathInfo[i].getTraffic());
				 
				 networkRouteObj.setPath(snmpModelNetworkRouteresObj.snmpModelNetworkRoutePathInfo[i].getPath());
				 networkRouteObj.setPathLinkId(snmpModelNetworkRouteresObj.snmpModelNetworkRoutePathInfo[i].getPathLinkId());
				 networkRouteObj.setPathType(snmpModelNetworkRouteresObj.snmpModelNetworkRoutePathInfo[i].getPathType());
				 networkRouteObj.setOsnr(snmpModelNetworkRouteresObj.snmpModelNetworkRoutePathInfo[i].getOsnr());
				 networkRouteObj.setWavelengthNo(snmpModelNetworkRouteresObj.snmpModelNetworkRoutePathInfo[i].getWavelengthNo());
				 networkRouteObj.setLineRate(snmpModelNetworkRouteresObj.snmpModelNetworkRoutePathInfo[i].getLineRate());
				 networkRouteObj.setRegeneratorLoc(snmpModelNetworkRouteresObj.snmpModelNetworkRoutePathInfo[i].getRegeneratorLocations());
				 networkRouteObj.setError(snmpModelNetworkRouteresObj.snmpModelNetworkRoutePathInfo[i].getError());
				 

				 /**
				  * DB Service : 2) Store RWA O/P Info 
				  */			 			
				 dbService.getNetworkRouteService().InsertNetworkRoute(networkRouteObj);
			 }
			 
			 MainMap.logger.info("--------------------------RWA Controller End : SUCCESS---------------------------------------");
			 return MapConstants.SUCCESS;
			 
					 
		 }
		 else{
			 MainMap.logger.info(" RWA Output Is Null .. ");
			 MainMap.logger.info("--------------------------RWA Controller End : FAILURE---------------------------------------");
			 return MapConstants.FAILURE;
		 }		 
		
	}
}
