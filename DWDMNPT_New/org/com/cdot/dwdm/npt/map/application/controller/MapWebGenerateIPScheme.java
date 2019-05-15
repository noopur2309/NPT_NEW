/**
 * 
 */
package application.controller;


import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import org.json.simple.parser.ParseException;

import application.MainMap;
import application.constants.MapConstants;
import application.model.IpSchemeLink;
import application.model.IpSchemeNode;
import application.model.Link;
import application.model.Node;
import application.service.DbService;
import application.service.IPv4;

/**
 * @author hp
 *
 */
public class MapWebGenerateIPScheme {
	
	
	
	/**
	 * @brif Node IP Generation : 1) LCT IP, 2)Router IP, 3)SCP IP, 4)MCP IP, 5)Rsrvd1, 6)Rsrvd2
	 * 
	 * @param jsonStr
	 * @param networkId
	 * @param blockNodeAdd
	 * @param dbService
	 * @throws SQLException
	 * @throws UnknownHostException
	 */	
	@SuppressWarnings("static-access")
	public void nodeIpGeneration(HashMap<String, Object> networkInfoMap, int networkId, String blockNodeAdd, DbService dbService) throws SQLException, UnknownHostException{
		
		/**
		 * Get Number of Nodes, Links and Other info as a List from the Present Saved Network
		 */
		int numOfNodes = dbService.getNodeService().Count(networkId);
		
		
		List<Node> listNode		 = dbService.getNodeService().FindAll(networkId);
		
		
				
		MainMap.logger.info("blockNodeAdd :- "+blockNodeAdd+", Node Count :- "+ numOfNodes);
		
		/**Delete Existing Data from DB*/
		///if(dbService.getNetworkService().GetByNetworkName("TestNetwork")!= null){
		MainMap.logger.info(" Going to Delete Existing IpSchemeNode in order to update it ..");
		dbService.getIpSchemeNodeService().DeleteByNetworkId(networkId);
					 
		/// }
		
		MainMap.logger.info("Pool : Node IP .... ");
		
		int fourthByteCounter =0;
		int thirdByteCounter =0;
		
		
		/**
		 * BrownField Case : 
		 *                   a) Copy as its data for the nodes which were part for the GreenField Network
		 *                   b) Generate the new ips from the last generated ip index for the Brownfield Network		 *                    
		 */
		if((boolean)networkInfoMap.get(MapConstants.NetworkFlag) == true){
			
			
			List<Node>  listCommonNode = dbService.getNodeService().FindCommonNodesInBrField((int)networkInfoMap.get(MapConstants.GreenFieldId), networkId); 
			
			if(listCommonNode!=null)
				copyCommonNodeIpScheme(listCommonNode, networkInfoMap, dbService);
			
			listNode = dbService.getNodeService().FindAddedNodesInBrField((int)networkInfoMap.get(MapConstants.GreenFieldId), networkId);
			
			@SuppressWarnings("unchecked")
			List<IpSchemeNode>  ipSchemeNodeLastObj = (List<IpSchemeNode>)networkInfoMap.get(MapConstants.LastGeneratedNodeIpSchemeObj);
			System.out.println(" ipSchemeNodeLastObj "+ ipSchemeNodeLastObj);
			System.out.println(" IPv4.integerToStringIP(ipSchemeNodeLastObj.get(0).getRsrvdIp1()) "+ IPv4.integerToStringIP(ipSchemeNodeLastObj.get(0).getRsrvdIp1()));
			System.out.println(" split "+ (IPv4.integerToStringIP(ipSchemeNodeLastObj.get(0).getRsrvdIp1())).split("\\."));
			
			String[] ipString                  =  IPv4.integerToStringIP(ipSchemeNodeLastObj.get(0).getRsrvdIp1()).split("\\.");
			
			
			for(String str : ipString){
				System.out.println("String : "+str);
			}
			
			thirdByteCounter  = Integer.parseInt(ipString[MapConstants.I_TWO]);
			fourthByteCounter = Integer.parseInt(ipString[MapConstants.I_THREE]) + MapConstants.I_THREE; /** Offset 3 added for next Range generation*/			
			
		}
			
		MainMap.logger.info(" thirdByteCounter : " + thirdByteCounter + " fourthByteCounter  " + fourthByteCounter );
		
		/**Insert into DB*/
		for(Node row : listNode){

			int nodeId	  = (int) row.getNodeId();
			
			if(fourthByteCounter > MapConstants.I_TWOFIVEFIVE){
				fourthByteCounter      = MapConstants.I_ZERO; /**Reset Counter Since One Network Block finished*/
				thirdByteCounter += MapConstants.I_ONE;/**Increment By One to go to Next Block*/
				MainMap.logger.info(" Updated Block Counter : thirdByteCounter :- "+thirdByteCounter+", fourthByteCounter :- "+fourthByteCounter);
			}
			
			String iPv4_String = blockNodeAdd+thirdByteCounter+"."+fourthByteCounter+MapConstants.subnetNodeMask;
			
			MainMap.logger.info("Network Id :- "+ networkId +", Node Id :- "+nodeId + ", iPv4_String :- "+iPv4_String + ", fourthByteCounter :- "+fourthByteCounter);	
			
			
			IPv4 ipv4NodeObj = new IPv4(iPv4_String);				
			
			/**IpV4 Lib Call to get available IP in given IP Block*/
			List<Integer> availableNodeIPs = ipv4NodeObj.getAvailableIPs(numOfNodes*MapConstants.IPPERNODE);
			
            /**IP Pool Print*/						
			for(int i=0;i <MapConstants.IPPERNODE; i++ ){
				
				/*MainMap.logger.info("String :- "+ipv4NodeObj.convertNumericIpToSymbolic(availableNodeIPs.get(i))+
						" => Integer :- " + availableNodeIPs.get(i));	*/			
			}
			
			/**Prepare Obj to Insert into DB*/
			IpSchemeNode ipSchemeNodeObj = new IpSchemeNode();
			ipSchemeNodeObj.setRouterIp(availableNodeIPs.get(MapConstants.I_ZERO));
			ipSchemeNodeObj.setLctIp(availableNodeIPs.get(MapConstants.I_TWO));/**DBG => Changed from ONE to TWO*/			
			ipSchemeNodeObj.setScpIp(availableNodeIPs.get(MapConstants.I_ZERO)); /**DBG => Changed from MapConstants.I_TWO*/
			ipSchemeNodeObj.setMcpIp(availableNodeIPs.get(MapConstants.I_ONE));/**DBG => Changed from THREE to ONE*/
			ipSchemeNodeObj.setRsrvdIp1(availableNodeIPs.get(MapConstants.I_FOUR));
			///ipSchemeNodeObj.setRsrvdIp2(availableNodeIPs.get(MapConstants.I_FIVE));
			
			ipSchemeNodeObj.setMcpSubnet(ipv4NodeObj.StringToIntIP(ipv4NodeObj.getNetmask()));			
							
			ipSchemeNodeObj.setMcpGateway(ipv4NodeObj.StringToIntIP(blockNodeAdd+thirdByteCounter+"."+MapConstants.I_ONE));/**DBG => Need to Check later*/		
			
			ipSchemeNodeObj.setNetworkId(networkId);
			ipSchemeNodeObj.setNodeId(nodeId);
			
			
		
			
			/**DB Call : Save the Generated Pool to DB*/			
			dbService.getIpSchemeNodeService().InsertIpSchemeNode(ipSchemeNodeObj);
			
			fourthByteCounter+=MapConstants.I_EIGHT;/**Update Counter by Seven to go to the Next Network*/			
		}
				
	}
	
	
	/**
	 * @brif Link IP Generation : 1) Link IP, 2)Link Source IP, 3)Link Destination IP
	 * @param dbService
	 * @throws SQLException
	 * @throws UnknownHostException
	 */	
	public void linkIpGeneration(HashMap<String, Object> networkInfoMap, int networkId, String blockLinkAdd, DbService dbService) throws SQLException, UnknownHostException{
		
		/**
		 * Get Number of Links and Other info as a List from the Present Saved Network
		 */		
		int numOfLinks = dbService.getLinkService().Count(networkId);
				
		List<Link> listLink 	     = dbService.getLinkService().FindAll(networkId);
		
				
		MainMap.logger.info("blockLinkAdd :- "+blockLinkAdd+", Link Count :- "+numOfLinks);
		
		/**Delete Existing Data from DB*/
		///if(dbService.getNetworkService().GetByNetworkName("TestNetwork")!= null){
		MainMap.logger.info(" Going to Delete Existing IpSchemeLink in order to update it ..");
		dbService.getIpSchemeLinkService().DeleteByNetworkId(networkId);
		 ///}
		
		MainMap.logger.info("Pool : Link IP .... ");
		
		int fourthByteCounter =0;
		int thirdByteCounter =0;
		
		
		/**
		 * BrownField Case : 
		 *                   a) Copy as its data for the links which were part for the GreenField Network
		 *                   b) Generate the new ips from the last generated ip index for the Brownfield Network		 *                    
		 */
		if((boolean)networkInfoMap.get(MapConstants.NetworkFlag) == true){
		

			List<Link>  listCommonLink = dbService.getLinkService().FindCommonLinksInBrField((int)networkInfoMap.get(MapConstants.GreenFieldId), networkId); 
			
			if(listCommonLink!=null)
				copyCommonLinkIpScheme(listCommonLink, networkInfoMap, dbService);
			
			listLink = dbService.getLinkService().FindAddedLinksInBrField((int)networkInfoMap.get(MapConstants.GreenFieldId), networkId);
			
			
			@SuppressWarnings("unchecked")
			List<IpSchemeLink>  ipSchemeLinkLastObj = (List<IpSchemeLink>)networkInfoMap.get(MapConstants.LastGeneratedLinkIpSchemeObj);
			System.out.println(" ipSchemeLinkLastObj "+ ipSchemeLinkLastObj);
			System.out.println(" IPv4.integerToStringIP(ipSchemeLinkLastObj.get(0).getDestIp()) "+ IPv4.integerToStringIP(ipSchemeLinkLastObj.get(0).getDestIp()));
			System.out.println(" split "+ (IPv4.integerToStringIP(ipSchemeLinkLastObj.get(0).getDestIp())).split("\\."));
			
			String[] ipString                  =  IPv4.integerToStringIP(ipSchemeLinkLastObj.get(0).getDestIp()).split("\\.");
			
			
			for(String str : ipString){
				System.out.println("String : "+str);
			}
			
			thirdByteCounter  = Integer.parseInt(ipString[MapConstants.I_TWO]);
			fourthByteCounter = Integer.parseInt(ipString[MapConstants.I_THREE]) + MapConstants.I_TWO; /** Offset 2 added for next Range generation*/			
			
		}
		
		/**Insert into DB*/
		for(Link row : listLink){

			int linkId	  = row.getLinkId();
			

			if(fourthByteCounter > MapConstants.I_TWOFIVEFIVE){
				fourthByteCounter      = MapConstants.I_ZERO; /**Reset Counter Since One Network Block finished*/
				thirdByteCounter += MapConstants.I_ONE;/**Increment By One to go to Next Block*/
				MainMap.logger.info(" Updated Block Counter : thirdByteCounter :- "+thirdByteCounter+", fourthByteCounter :- "+fourthByteCounter);
			}
			
			String iPv4_String = blockLinkAdd+thirdByteCounter+"."+fourthByteCounter+MapConstants.subnetLinkMask;
			
			MainMap.logger.info("Network Id :- "+ networkId +", Link Id :- "+linkId + ", iPv4_String :- "+iPv4_String + 
					", fourthByteCounter :- "+fourthByteCounter);	
			
			
			IPv4 ipv4LinkObj = new IPv4(iPv4_String);				
			
			/**IpV4 Lib Call to get available IP in given IP Block*/			
			List<Integer> availableLinkIPs = ipv4LinkObj.getAvailableIPs(numOfLinks*MapConstants.IPPERLINK);			
			
            /**IP Pool Print*/						
			for(int i=0;i <MapConstants.IPPERLINK; i++ ){
				
				MainMap.logger.info("String :- "+ipv4LinkObj.convertNumericIpToSymbolic(availableLinkIPs.get(i))+
						" => Integer :- " + availableLinkIPs.get(i));				
			}
			
			/**Prepare Obj to Insert into DB*/
			IpSchemeLink ipSchemeLinkObj = new IpSchemeLink();
			ipSchemeLinkObj.setLinkIp(IPv4.StringToIntIP(blockLinkAdd+thirdByteCounter+"."+fourthByteCounter));
			ipSchemeLinkObj.setSrcIp(availableLinkIPs.get(MapConstants.I_ZERO));
			ipSchemeLinkObj.setDestIp(availableLinkIPs.get(MapConstants.I_ONE));
			
			System.out.println(" IPv4.StringToIntIP(ipv4LinkObj.getNetmask() "+ IPv4.StringToIntIP(ipv4LinkObj.getNetmask()));
			ipSchemeLinkObj.setSubnetMask(IPv4.StringToIntIP(ipv4LinkObj.getNetmask()));
			
			ipSchemeLinkObj.setNetworkId(networkId);
			ipSchemeLinkObj.setLinkId(linkId);
			
					 
			
			/**DB Call : Save the Generated Pool to DB*/			
			dbService.getIpSchemeLinkService().InsertIpSchemeLink(ipSchemeLinkObj);
			
			fourthByteCounter+=MapConstants.I_FOUR;/**Update Counter by Seven to go to the Next Network*/			
		}
				
	}
	

	/**
	 * 
	 * @param jsonStr
	 * @param dbService
	 * @return response : SUCCESS :1, FAILURE :0
	 * @throws SQLException
	 * @throws UnknownHostException 
	 * @throws ParseException 
	 */
	@SuppressWarnings("static-access")
	public int generateSaveIPScheme(String jsonStr, DbService dbService) throws SQLException, UnknownHostException, ParseException {
		
		/**Logger Start*/		
		 MainMap.logger = Logger.getLogger(MapWebGenerateIPScheme.class.getName());
		
		MainMap.logger.info("--------------------------IP Scheme Generation Start---------------------------------------");
		
		/**Map From Common API*/ 
		HashMap<String, Object> networkInfoMap = MapWebCommonAPIs.NestedStaticClass.getNetworkInformation(dbService,jsonStr); 
		int networkId= (int)networkInfoMap.get(MapConstants.NetworkId); /**Network Id for common use*/
		
		boolean networkFlag= false; /**In case of Green Field*/ 
		
		/**
		 * find out green field id in case of brown field
		 */
		if(((String)networkInfoMap.get(MapConstants.NetworkType)).equalsIgnoreCase(MapConstants.BrownField)){
		
			MainMap.logger.info("generateSaveIPScheme :  GreenField Id "+ (int)networkInfoMap.get(MapConstants.GreenFieldId)
			  +" BrownField Id "+ (int)networkInfoMap.get(MapConstants.BrownFieldId));	
			networkFlag = true;
			
			/** Search for the last generated IP in case of Node and Link*/
			List<IpSchemeLink>  lastGeneratedLinkIpSchemeObj = dbService.getIpSchemeLinkService().FindLastGeneratedLinkIP((int)networkInfoMap.get(MapConstants.GreenFieldId));
			List<IpSchemeNode>  lastGeneratedNodeIpSchemeObj = dbService.getIpSchemeNodeService().FindLastGeneratedNodeIP((int)networkInfoMap.get(MapConstants.GreenFieldId));
			
			
			System.out.println(" lastGeneratedLinkIpSchemeObj : "+ lastGeneratedLinkIpSchemeObj);
			System.out.println(" lastGeneratedNodeIpSchemeObj : "+ lastGeneratedNodeIpSchemeObj);
			networkInfoMap.put(MapConstants.LastGeneratedNodeIpSchemeObj, lastGeneratedNodeIpSchemeObj);
			networkInfoMap.put(MapConstants.LastGeneratedLinkIpSchemeObj, lastGeneratedLinkIpSchemeObj);
						
		}
		
		
		networkInfoMap.put(MapConstants.NetworkFlag, networkFlag);
		
		MainMap.logger.info(" networkInfoMap : "+ networkInfoMap.toString());
		
		/**Parsing to Get Private Block Type*/
		org.json.JSONObject jsonObj = new org.json.JSONObject(jsonStr);
		int blockType = jsonObj.getInt("blockType");
		MainMap.logger.info(" Block Type :- " + blockType);
				
		/**Generate IP as per the Given Block*/
		if(blockType == MapConstants.I_ONE){/**Block 1 [24-bit]: 10.0.0.0 to 10.255.255.255*/
			
			
			IPv4 ipv4NodeObj = new IPv4("10.5.0.0/29");
			IPv4 ipv4LinkObj = new IPv4("10.7.0.0/30");
			Long intIpAdd = ipv4LinkObj.StringToIntIP("10.7.0.1");
			
			/*MainMap.logger.info(" BroadcastAddress :- "+ ipv4NodeObj.getBroadcastAddress() +
					  			", HostAddressRange :- "+ ipv4NodeObj.getHostAddressRange() +
					  			", CIDR :- "+ ipv4NodeObj.getCIDR() +
					  			", IP :- "+ ipv4NodeObj.getIP()   +
					  			", Netmask :- "+ ipv4NodeObj.getNetmask() +
					  			", NetmaskInBinary :- "+ ipv4NodeObj.getNetmaskInBinary() +
					  			", WildcardMask :- "+ ipv4NodeObj.getWildcardMask()   +
					  			", NumberOfHosts :- "+ ipv4NodeObj.getNumberOfHosts() +
					  			", HashCode :- " + ipv4NodeObj.hashCode() +
					  			", Int Address :- " + intIpAdd +
					  			", String Address :- " + ipv4LinkObj.integerToStringIP(intIpAdd)
 					  			);*/
			
						
			nodeIpGeneration(networkInfoMap, networkId, MapConstants.block1NodeStartAddress, dbService);
			linkIpGeneration(networkInfoMap, networkId, MapConstants.block1LinkStartAddress, dbService);
			
		}
		else if(blockType == MapConstants.I_TWO){/**Block 2 [20-bit]: 172.16.0.0 to 172.31.255.255*/
			
			nodeIpGeneration(networkInfoMap, networkId, MapConstants.block2NodeStartAddress, dbService);
			linkIpGeneration(networkInfoMap, networkId,MapConstants.block2LinkStartAddress, dbService);
		}
		else if(blockType == MapConstants.I_THREE){/**Block 3[16-bit]: 192.168.0.0 to 192.168.255.255*/
			
			nodeIpGeneration(networkInfoMap, networkId, MapConstants.block3NodeStartAddress, dbService);
			linkIpGeneration(networkInfoMap, networkId, MapConstants.block3LinkStartAddress, dbService);
		}
		
		
		
		/*List<IpSchemeNode> ipNodeDBObj =  dbService.getIpSchemeNodeService().FindAll();
		
		*//**IpSchemeNode Data*//*
		 for (IpSchemeNode row : ipNodeDBObj) {	
			 
			 MainMap.logger.info(" Lct IP :- "+IPv4.integerToStringIP(row.getLctIp())+", Router IP :- "+row.getRouterIp()+", SCP IP :- "+row.getScpIp()+
					 ", MCP IP :- "+row.getMcpIp()+ ", Rsrvd1 IP :- "+ row.getRsrvdIp1() +", Rsrvd2 IP :- "+row.getRsrvdIp2());		 
			
			 
		 }	*/
		
		
		
		MainMap.logger.info("--------------------------IP Scheme Generation End---------------------------------------");
		
		
		return MapConstants.SUCCESS;		
	}
	
	
	/**
	 * 
	 * @param jsonStr
	 * @param dbService
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public JSONObject viewGeneratedIPSchemeReq(String jsonStr, DbService dbService) throws SQLException{
	
		/**Map From Common API*/ 
		HashMap<String, Object> networkInfoMap = MapWebCommonAPIs.NestedStaticClass.getNetworkInformation(dbService,jsonStr); 
		int networkId= (int)networkInfoMap.get(MapConstants.NetworkId); /**Network Id for common use*/ 
		
		
		/**Create JSONArray[For node scheme and link scheme] and a JSONOBject*/ 
		JSONObject jsonObjRes = new JSONObject();
		JSONArray  jsonNodeArray = new JSONArray();
		JSONArray  jsonLinkArray = new JSONArray();
		
		MainMap.logger.info("--------------------------View IP Scheme Request Start---------------------------------------");
		
		/**Fetch Data from Node and Link IP Scheme*/
		List<IpSchemeNode> listIpSchemeNode =  dbService.getIpSchemeNodeService().FindAll(networkId);
		List<IpSchemeLink> listIpSchemeLink =  dbService.getIpSchemeLinkService().FindAll(networkId);
		
		/**
		 * Loop through to fill JSON Array
		 */
		for(int i=0; i<listIpSchemeNode.size(); i++){/**Node IP Scheme*/
			
			JSONObject jsonRowObj = new JSONObject();
			
			
			jsonRowObj.put("NetworkId", listIpSchemeNode.get(i).getNetworkId());
			jsonRowObj.put("NodeId", listIpSchemeNode.get(i).getNodeId());
			jsonRowObj.put("LctIp", IPv4.integerToStringIP(listIpSchemeNode.get(i).getLctIp()));
			jsonRowObj.put("RouterIp", IPv4.integerToStringIP(listIpSchemeNode.get(i).getRouterIp()));
			jsonRowObj.put("ScpIp", IPv4.integerToStringIP(listIpSchemeNode.get(i).getScpIp()));
			jsonRowObj.put("McpIp", IPv4.integerToStringIP(listIpSchemeNode.get(i).getMcpIp()));
			jsonRowObj.put("RsrvdIp1", IPv4.integerToStringIP(listIpSchemeNode.get(i).getRsrvdIp1()));
			jsonRowObj.put("RsrvdIp2", IPv4.integerToStringIP(listIpSchemeNode.get(i).getRsrvdIp2()));
			
			jsonNodeArray.add(jsonRowObj);			
		}
		
		
		for(int i=0; i<listIpSchemeLink.size(); i++){/**Link IP Scheme*/
			
			JSONObject jsonRowObj = new JSONObject();
			
			
			jsonRowObj.put("NetworkId", listIpSchemeLink.get(i).getNetworkId());
			jsonRowObj.put("LinkId", listIpSchemeLink.get(i).getLinkId());
			jsonRowObj.put("LinkIp", IPv4.integerToStringIP(listIpSchemeLink.get(i).getLinkIp()));
			jsonRowObj.put("SrcIp", IPv4.integerToStringIP(listIpSchemeLink.get(i).getSrcIp()));
			jsonRowObj.put("DestIp", IPv4.integerToStringIP(listIpSchemeLink.get(i).getDestIp()));
			
			jsonLinkArray.add(jsonRowObj);
		}
		
		
		/**Combine both the array into One object*/
		jsonObjRes.put("IpSchemeNode", jsonNodeArray);
		jsonObjRes.put("IpSchemeLink", jsonLinkArray);
		
		MainMap.logger.info("Final JSON Object Response :- "+ jsonObjRes);
		
		MainMap.logger.info("--------------------------View IP Scheme Request End---------------------------------------");
		
		
		
		return jsonObjRes;
	}
	
	
	/**
	 * Copy Common IP from Green Field to Brown Field
	 */
	public void copyCommonNodeIpScheme(List<Node> listCommonNode, HashMap<String, Object> networkInfoMap,  DbService dbService) throws SQLException {
		
		for(int count=0; count<listCommonNode.size(); count++) {			
			IpSchemeNode ipSchemeNodeObj = dbService.getIpSchemeNodeService().FindIpSchemeNode((int)networkInfoMap.get(MapConstants.GreenFieldId),
					listCommonNode.get(count).getNodeId());
			ipSchemeNodeObj.setNetworkId((int) networkInfoMap.get(MapConstants.NetworkId));
			dbService.getIpSchemeNodeService().InsertIpSchemeNode(ipSchemeNodeObj);
		}
	}
	/**
	 * Copy Common IP from Green Field to Brown Field
	 */
	public void copyCommonLinkIpScheme(List<Link> listCommonLink, HashMap<String, Object> networkInfoMap,  DbService dbService) throws SQLException {
		
		for(int count=0; count<listCommonLink.size(); count++) {			
			IpSchemeLink ipSchemeLinkObj = dbService.getIpSchemeLinkService().FindIpSchemeLink((int)networkInfoMap.get(MapConstants.GreenFieldId),
					listCommonLink.get(count).getLinkId());
			ipSchemeLinkObj.setNetworkId((int) networkInfoMap.get(MapConstants.NetworkId));
			dbService.getIpSchemeLinkService().InsertIpSchemeLink(ipSchemeLinkObj);
		}
	}
	
	
}
