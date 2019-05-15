package application.controller;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import application.MainMap;
import application.constants.DataPathConfigFileConstants;
import application.constants.MapConstants;
import application.constants.ResourcePlanConstants;
import application.model.CardInfo;
import application.model.IpSchemeNode;
import application.model.Network;
import application.model.Node;
import application.service.DbService;
import application.service.IPv4;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author hp
 *
 */
public class MapWebGenerateControlPathConfigFile {
	
	/**
	 * @brief This Method generate the config file(xml format) after taking inputs from the various tables
	 * @param dbService
	 */
	@SuppressWarnings({ "static-access", "unchecked" })
	public int generateControlPathConfigFile(String jsonStr, DbService dbService){
	
		 try{
			 	 /**Logger Start*/		
			 	 MainMap.logger = MainMap.logger.getLogger(MapWebGenerateControlPathConfigFile.class.getName());
			 	 
			 	 MainMap.logger.info("-----------------------generateControlPathConfigFile Start ... -----------------------");
			 	
			 	 
			 	/**Map From Common API*/ 
			 	 HashMap<String, Object> networkInfoMap = MapWebCommonAPIs.NestedStaticClass.getNetworkInformation(dbService,jsonStr); 
					
				int networkId=  (int) networkInfoMap.get(MapConstants.NetworkId);
			 	 
			 	/**Map to get integer value of Direction*/
			 	HashMap<String, String> NodeDirectionMap = new HashMap<String, String>();
			 	NodeDirectionMap.put("east", "1");
			 	NodeDirectionMap.put("west", "2");
			 	NodeDirectionMap.put("north", "3");
			 	NodeDirectionMap.put("south", "4");
			 	NodeDirectionMap.put("ne", "5");
			 	NodeDirectionMap.put("nw", "6");
			 	NodeDirectionMap.put("se", "7");
			 	NodeDirectionMap.put("sw", "8");
			 	 
			 	 
			 	 
			 	 System.out.println(" networkID " + networkId);
			 	 List<Node> nodes 	  = dbService.getNodeService().FindAll(networkId);
			 	 Network network = dbService.getNetworkService().FindNetwork(networkId);
			 	 
			 	 /**DBG => Will be changed once brown field introduced*/			 	 
			 	 String topology 	 = network.getTopology();
			 	 String subNetworkId = network.getSubNetworkId(); 
			 	 
			 	 /**DB Call to get number of circuits and nodes*/
			 	 for (int i = 0; i < nodes.size(); i++) {/**Call For Each Node*/			 		 
			 		  
			 		 /**Get Node Info*/
			 		Node nodeObj = dbService.getNodeService().FindNode(networkId,
			 				 nodes.get(i).getNodeId());			 		

			 		/**Get Link Info*/
			 		List<Map<String,Object>> finalLinkObj=null;
			 		List<Map<String,Object>> linkSrcObj = dbService.getLinkService().FindSrcIpOfLink(nodes.get(i).getNodeId(), networkId);
			 		List<Map<String,Object>> linkDestObj = dbService.getLinkService().FindDestIpOfLink(nodes.get(i).getNodeId(), networkId);
			 		
			 		///MainMap.logger.info("linkSrcObj " + linkSrcObj.toString() + "linkDestObj  " + linkDestObj.toString());
			 		
			 		
			 		List<Map<String,Object>> linkSwapDestObj = new ArrayList<>(linkDestObj);
			 		linkDestObj.clear();
			 		for(int iList=0; iList < linkSwapDestObj.size();iList++){
			 			String swapIp= linkSwapDestObj.get(iList).get("DestIp").toString();
			 			Map<String,Object> swapMap = new HashMap<>();
			 			swapMap.put("DestIp", linkSwapDestObj.get(iList).get("SrcIp").toString());
			 			swapMap.put("SrcIp", swapIp);
			 			swapMap.put("linkid", linkSwapDestObj.get(iList).get("linkid").toString());
			 			swapMap.put("DestNodeDirection", linkSwapDestObj.get(iList).get("DestNodeDirection").toString());
			 			///System.out.println("swapMap "+ swapMap.toString());
			 			linkDestObj.add(swapMap);			 				  
			 		}
			 		
			 		System.out.println(" Link Src Empty" + linkDestObj);
		 			
			 	
			 		MainMap.logger.info("linkDestObj " + linkDestObj.toString());
			 		finalLinkObj = new ArrayList<Map<String,Object>>(linkSrcObj);
		 			finalLinkObj.addAll(linkDestObj);			 			
			 		 
			 		/**Get IPSchemeNode Info*/
			 		IpSchemeNode ipSchemeNodeObj = dbService.getIpSchemeNodeService().FindIpSchemeNode(networkId,
			 				 nodes.get(i).getNodeId());	
			 		
			 		/**XML Building Started*/
			 		 DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			         Document doc = dBuilder.newDocument();
			         
			         // root element  (NetworkDetails)
			         Element rootElement = doc.createElement("NetworkDetails");
					 doc.appendChild(rootElement);
					 

					 // NetworkDetails->NetworkName  : New Field Added for EMS 
			         Element NetworkName  = doc.createElement("NetworkName");
			         NetworkName.appendChild( doc.createTextNode(dbService.getNetworkService().FindAll(nodeObj.getNetworkId()).get(0).getNetworkName()));
			         rootElement.appendChild(NetworkName);
			         
			         // NetworkDetails->Version
			         Element Version  = doc.createElement("Version");
			         Version.appendChild( doc.createTextNode("1.0"));
			         rootElement.appendChild(Version);
			         
			  			      	         
			         // NetworkDetails->NEDetails->
			         Element NEDetails  = doc.createElement("NEDetails");
			         rootElement.appendChild(NEDetails);
			  
			         /**
			          * NEDetails filling
			          */
			         
			         // NetworkDetails->NEDetails->Topology
			         Element Topology= doc.createElement("Topology");
			         Topology.appendChild( doc.createTextNode(			         			
			        		 String.valueOf(DataPathConfigFileConstants.topologyConstantsMap.get(topology))));
			         NEDetails.appendChild(Topology);

			         // NetworkDetails->NEDetails->SiteName
			         Element SiteName = doc.createElement("SiteName");
			         SiteName.appendChild( doc.createTextNode(nodeObj.getSiteName()));
			         NEDetails.appendChild(SiteName);

			    	 // NetworkDetails->NEDetails->StationName
			         Element StationName = doc.createElement("StationName");
			         StationName.appendChild( doc.createTextNode(nodeObj.getStationName()));
			         NEDetails.appendChild(StationName);
			             
			         // NetworkDetails->NEDetails->NEType
			         Element NEType = doc.createElement("NEType");
			         NEType.appendChild( doc.createTextNode(String.valueOf(nodeObj.getNodeType())));
			         NEDetails.appendChild(NEType);
			         
			         // NetworkDetails->NEDetails->Degree
			         Element Degree= doc.createElement("Degree");
			         Degree.appendChild( doc.createTextNode(String.valueOf(nodeObj.getDegree())));
			         NEDetails.appendChild(Degree);
			         
			         // NetworkDetails->NEDetails->SystemCapacity
			         Element SystemCapacity= doc.createElement("SystemCapacity");
			         SystemCapacity.appendChild( doc.createTextNode(nodeObj.getCapacity()));
			         NEDetails.appendChild(SystemCapacity);
			         
			         // NetworkDetails->NEDetails->OpticalReach
			         Element OpticalReach= doc.createElement("OpticalReach");
			         OpticalReach.appendChild( doc.createTextNode("Long Haul"));/**DBG => Hard coded to meet config file param*/
			         NEDetails.appendChild(OpticalReach);
			         
			         //  NetworkDetails->NEDetails->Direction
			         Element Direction= doc.createElement("Direction");
			         Direction.appendChild( doc.createTextNode(String.valueOf(nodeObj.getDirection())));/**DBG => Not known as of now*/
			         NEDetails.appendChild( Direction);
			         
			         //  NetworkDetails->NEDetails->NodeId
			         Element RouterId= doc.createElement("NodeId"); /**Changed to NodeId from RouterId */
			         RouterId.appendChild( doc.createTextNode(String.valueOf(nodeObj.getNodeId())));
			         NEDetails.appendChild( RouterId);

			         //  NetworkDetails->NEDetails->SAPI
			         Element SAPI= doc.createElement("SAPI");
			         SAPI.appendChild( doc.createTextNode("0"));
			         NEDetails.appendChild( SAPI);
			         
			         //  NetworkDetails->NEDetails->SubNetworkId  
			         Element SubNetworkId= doc.createElement("SubNetworkId");
			         SubNetworkId.appendChild(doc.createTextNode(subNetworkId));
			         NEDetails.appendChild( SubNetworkId);

			         /**
			          * IPDetails filling 
			          */
			         
			         // NetworkDetails->IPDetails
			         Element IPDetails  = doc.createElement("IPDetails");
			         rootElement.appendChild(IPDetails);
			         
			         // NetworkDetails->IPDetails->MCPIP
			         Element MCPIP= doc.createElement("MCPIP");
			         MCPIP.appendChild( doc.createTextNode(IPv4.integerToStringIP(ipSchemeNodeObj.getMcpIp())));
			         IPDetails.appendChild(MCPIP);
			         
			         // NetworkDetails->IPDetails->Subnet
			         Element Subnet= doc.createElement("Subnet");
			         Subnet.appendChild( doc.createTextNode(IPv4.integerToStringIP(ipSchemeNodeObj.getMcpSubnet())));
			         IPDetails.appendChild(Subnet);
			         
			         // NetworkDetails->IPDetails->Gateway
			         Element Gateway= doc.createElement("Gateway");
			         Gateway.appendChild( doc.createTextNode(IPv4.integerToStringIP(ipSchemeNodeObj.getMcpGateway())));
			         IPDetails.appendChild(Gateway);
			         			         
			         //  NetworkDetails->IPDetails->LCTIP
			         Element LCTIP= doc.createElement("LCTIP");
			         LCTIP.appendChild( doc.createTextNode(IPv4.integerToStringIP(ipSchemeNodeObj.getLctIp())));
			         IPDetails.appendChild(LCTIP);
			         
			         //  NetworkDetails->IPDetails->SCPIP
			         Element SCPIP= doc.createElement("SCPIP");
			         SCPIP.appendChild( doc.createTextNode(IPv4.integerToStringIP(ipSchemeNodeObj.getScpIp())));
			         IPDetails.appendChild(SCPIP);
			         
			         // NetworkDetails->IPDetails->RouterIP
			         Element RouterIP= doc.createElement("RouterIP");
			         RouterIP.appendChild( doc.createTextNode(IPv4.integerToStringIP(ipSchemeNodeObj.getRouterIp())));
			         IPDetails.appendChild(RouterIP);
			         
			         /**
			          * GNE_INFO filling
			          */
			         //NetworkDetails->GNE_INFO
			         Element GNE_INFO  = doc.createElement("GNE_INFO");
			    	 rootElement.appendChild(GNE_INFO);
			    	 

			    	 //NetworkDetails->GNE_INFO->GNE_FLAG
			         Element GNE_FLAG  = doc.createElement("GNE_FLAG");
			         GNE_FLAG.appendChild( doc.createTextNode(String.valueOf(nodeObj.getIsGne())));
			    	 GNE_INFO.appendChild(GNE_FLAG);
			    	 

			    	 //NetworkDetails->GNE_INFO->GNE_IP
			         Element GNE_IP  = doc.createElement("GNE_IP");
			         GNE_IP.appendChild( doc.createTextNode(nodeObj.getIp()));
			    	 GNE_INFO.appendChild(GNE_IP);			    	 

				    	
			    	 //NetworkDetails->GNE_INFO->VLAN
			         Element VLAN  = doc.createElement("VLAN");
			         VLAN.appendChild( doc.createTextNode(String.valueOf(nodeObj.getVlanTag())));
			    	 GNE_INFO.appendChild(VLAN);
			    	 
			    	//NetworkDetails->GNE_INFO->SubnetMask
			         Element SubnetMask  = doc.createElement("SubnetMask");
			         SubnetMask.appendChild( doc.createTextNode(nodeObj.getEmsSubnet()));
			         GNE_INFO.appendChild(SubnetMask);
			    	 
			    	//NetworkDetails->GNE_INFO->Gateway
			         Element EmsGateway  = doc.createElement("Gateway");
			         EmsGateway.appendChild( doc.createTextNode(nodeObj.getEmsGateway()));
			    	 GNE_INFO.appendChild(EmsGateway);
			    	 
			         //  NetworkDetails->GNE_FLAG->IPv6
			         Element IPv6= doc.createElement("IPv6");
			         IPv6.appendChild( doc.createTextNode(nodeObj.getIpV6Add()));
			         GNE_INFO.appendChild(IPv6);      
			        	       
			        	    	 

			    	 /**
			    	  * link filling 
			    	  */
			         
			         List<CardInfo>  supyCardInfoList =  dbService.getCardInfoService().FindCardInfoByCardType
				     	(networkId,	nodeObj.getNodeId(), ResourcePlanConstants.CardSupy);
			         System.out.println(" supyCardInfoList : "+supyCardInfoList);
			         
			         MainMap.logger.info("finalLinkObj:-"+ finalLinkObj.toString());
			         for (Map<String, Object> rowLinkObj : finalLinkObj) {
				    	 // NetworkDetails->link
				    	 Element link  = doc.createElement("link");
					     rootElement.appendChild(link);
					     int directionValueAsPort =0;
					     // NetworkDetails->link->direction
					     Element direction  = doc.createElement("direction");
					     if(rowLinkObj.get("SrcNodeDirection")!=null){
					    	 System.out.println(" direction src");
					    	 directionValueAsPort = Integer.parseInt(NodeDirectionMap.get(rowLinkObj.get("SrcNodeDirection")));
					    	 direction.appendChild( doc.createTextNode(NodeDirectionMap.get(rowLinkObj.get("SrcNodeDirection").toString())));
					     }
					     if(rowLinkObj.get("DestNodeDirection")!=null){
					    	 System.out.println(" direction dest");
					    	 directionValueAsPort = Integer.parseInt(NodeDirectionMap.get(rowLinkObj.get("DestNodeDirection")));
					    	 direction.appendChild( doc.createTextNode(NodeDirectionMap.get(rowLinkObj.get("DestNodeDirection").toString())));
					     }
					     
					     link.appendChild(direction);
					     
					     /**DBG => Temporary for Card Get till DB populated for the same*/
					     int index=0;
					    if(rowLinkObj.get("DestNodeDirection")!=null){
					    	
					    	index = MapConstants.I_ZERO; /**Only Seventh Slot Card*/
					    	
					    	 /*if(  directionValueAsPort <= MapConstants.I_FOUR){
						    	 index = MapConstants.I_ZERO; *//**Seventh Slot Card*//*  
						     }
						     else if(  directionValueAsPort > MapConstants.I_FOUR){
						    	 index = MapConstants.I_ONE;  *//**Eighth Slot Card*//* 
						     }*/
					     }
					    

					     // NetworkDetails->link->LocalIP
					     Element LocalIP  = doc.createElement("LocalIP");
					     LocalIP.appendChild(doc.createTextNode(IPv4.integerToStringIP(Long.parseLong(rowLinkObj.get("SrcIp").toString()))));
					     link.appendChild(LocalIP);
					     
					     // NetworkDetails->link->RemoteIP
					     Element RemoteIP  = doc.createElement("RemoteIP");
					     RemoteIP.appendChild(doc.createTextNode(IPv4.integerToStringIP(Long.parseLong(rowLinkObj.get("DestIp").toString()))));
					     link.appendChild(RemoteIP); 
					     
					     // NetworkDetails->link->SubnetMask
					     Element linkSubnetMask  = doc.createElement("SubnetMask");
					     linkSubnetMask.appendChild( doc.createTextNode("255.255.255.252"));///IPv4.integerToStringIP(Long.parseLong(rowLinkObj.get("SubnetMask").toString()))));
					     link.appendChild(linkSubnetMask);
					     
					     // NetworkDetails->link->RackId
					     Element linkRackId  = doc.createElement("RackId");
					     linkRackId.appendChild( doc.createTextNode(
					    		 String.valueOf(supyCardInfoList.get(index).getRack())));
					     link.appendChild(linkRackId);
					     
					     // NetworkDetails->link->SubRackId
					     Element linkSubRackId  = doc.createElement("SubRackId");					     
					     linkSubRackId.appendChild( doc.createTextNode(
					    		 String.valueOf(supyCardInfoList.get(index).getSbrack())));
					     link.appendChild(linkSubRackId);
					     
					     // NetworkDetails->link->CardId
					     Element linkCardId  = doc.createElement("CardId");
					     linkCardId.appendChild( doc.createTextNode(
					    		 String.valueOf(supyCardInfoList.get(index).getCard())));
					     link.appendChild(linkCardId);
					     
					     // NetworkDetails->link->PortId
					     Element linkPortId  = doc.createElement("PortId");
					     linkPortId.appendChild( doc.createTextNode(String.valueOf(directionValueAsPort)));/**DBG => As of now take the port as a direction, 
					     later on DB mapping required*/
					     link.appendChild(linkPortId);

			         }
				    
				     /**
				      * Prepare File Name to Write
				      */
			         String home = System.getProperty("user.home");
			         File fileDir = new File(home+"/Downloads/ConfigPathFiles");////ControlPath");
				        
			         if (!fileDir.exists()) {
			        	 if (fileDir.mkdir()) {
			        		 System.out.println("Directory is created!");
			        	 } else {
			        		 System.out.println("Failed to create directory!");
			        	 }
			         }
			         
			         fileDir = new File(home+"/Downloads/ConfigPathFiles/ControlPath");
				        
			         if (!fileDir.exists()) {
			        	 if (fileDir.mkdir()) {
			        		 System.out.println("Directory is created!");
			        	 } else {
			        		 System.out.println("Failed to create directory!");
			        	 }
			         }
				       
			      
			         /**
			          * Delete All Existed File
			          */
			         
			         if(i==0){
			        	 String[]entries = fileDir.list();
				         
				         for(String s: entries){
				             File currentFile = new File(fileDir.getPath(),s);
				             currentFile.delete();
				         }				         
			         }
			        
				     String xmlFileName = home+"/Downloads/ConfigPathFiles/ControlPath/"+
				    		 nodeObj.getSiteName()+"_"+nodeObj.getStationName()+"_"+nodeObj.getNodeId()+"_ControlPath.xml"; 
				     MainMap.logger.info("xmlFileName :- "+xmlFileName);
				     
				     
				    
				     
			         // write the content into xml file
			         TransformerFactory transformerFactory =
			         TransformerFactory.newInstance();
			         Transformer transformer =
			         transformerFactory.newTransformer();
			         transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			         transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			         DOMSource source = new DOMSource(doc);
			         /*StreamResult result = new StreamResult(new File(getClass().getResource("/ControlPathFiles/ControlPathSetup.xml").getFile()));*/
			         StreamResult result = new StreamResult(new File(xmlFileName));
			         transformer.transform(source, result);
			         
			         //String xmlString = result.getWriter().toString();
			        //System.out.println(xmlString);
			         
			         // Output to console for testing
			         /*StreamResult consoleResult =
			         new StreamResult(System.out);
			         transformer.transform(source, consoleResult);*/
			 		
			         
			 	 }
					
			 	
			 	
		         
		         MainMap.logger.info("-----------------------generateControlPathConfigFile End ... -----------------------");
		         
		         return MapConstants.SUCCESS;
		         
		      
		      } catch (Exception e) {
		         e.printStackTrace();
		         return MapConstants.FAILURE;
		      }
		
	}
}
