package application.controller;

import application.model.IpDetails;
import application.model.NeDetails;
import application.model.LinkReversePath;
import application.model.GneInfo;
import application.dao.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import application.MainMap;
import application.controller.*;
import application.service.DbService;

import org.w3c.dom.*;

import javax.xml.parsers.*;
import java.io.*;
import java.util.*;

public class ControlPath {
     
	
	
	/**
	 * @author surya
	 * @param FileName
	 * @param PATH
	 * @param dbService
	 * @return
	 */
	public static org.json.simple.JSONObject parsingXML(String FileName,String PATH,DbService dbService)

	{   
		HashMap<Integer,Object> NodeDifference = new HashMap<Integer,Object>();
		
		
		ArrayList<HashMap<String,Object> >  Difference = new ArrayList<HashMap<String,Object>> ();
		HashMap<String, Object> PreviousData = new HashMap<String, Object>();
		HashMap<String, Object> UpdatedData = new HashMap<String, Object>();
		application.model.Node nodeObjToCompareWith = new application.model.Node();
		List<NeDetails> NEArray = new ArrayList<NeDetails>();
		List<IpDetails> IPArray = new ArrayList<IpDetails>();
		List<GneInfo> GNEArray = new ArrayList<GneInfo>();
		List<LinkReversePath> linkArray = new ArrayList<LinkReversePath>();
		
		IpDetails Ip = new IpDetails();		
		GneInfo GNE = new GneInfo();
		NeDetails Ne = new NeDetails();
		LinkReversePath li = new LinkReversePath();
		int nodeid = 0;
		
		
		org.json.simple.JSONArray  JSONNodeArray = new org.json.simple.JSONArray();
		org.json.simple.JSONObject FinalNodeJSON = new org.json.simple.JSONObject();
		
		//System.out.println("*****Parsing In****");
		
		try

		{  
		    
		    org.json.simple.JSONObject DifferenceObj = new org.json.simple.JSONObject();
			
			
			
			
			
			String File = PATH+"/"+FileName;
		    File fXmlFile = new File(File); // input file
		    MainMap.logger.info("Parsing..."+File);

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(fXmlFile);
			Element root = document.getDocumentElement();
			 //System.out.println(root);
			String NetworkName = document.getElementsByTagName("NetworkName").item(0).getTextContent();
			Float Version = Float.parseFloat(document.getElementsByTagName("Version").item(0).getTextContent());
			MainMap.logger.info(NetworkName +":"+ NetworkName);

			NodeList nList = document.getElementsByTagName("NEDetails");
			int networkID = dbService.getNetworkService().GetNetworkInfoByNetworkName(NetworkName).getNetworkId();
			//System.out.println("******"+networkID);

			for (int i = 0; i < nList.getLength(); i++) // adding NEDetails in to NEArray
			{

				Node node = nList.item(i);
				Element eElement = (Element) node;
				
				
				Ne.setTopology(Integer.parseInt(eElement.getElementsByTagName("Topology").item(0).getTextContent()));
				Ne.setSiteName(eElement.getElementsByTagName("SiteName").item(0).getTextContent());
				Ne.setStationName(eElement.getElementsByTagName("StationName").item(0).getTextContent());
				Ne.setNeType(Integer.parseInt(eElement.getElementsByTagName("NEType").item(0).getTextContent()));
				Ne.setDegree(Integer.parseInt(eElement.getElementsByTagName("Degree").item(0).getTextContent()));
				Ne.setSystemCapacity(eElement.getElementsByTagName("SystemCapacity").item(0).getTextContent());
				Ne.setOpticalReach(eElement.getElementsByTagName("OpticalReach").item(0).getTextContent());
				Ne.setDirection(Integer.parseInt(eElement.getElementsByTagName("Direction").item(0).getTextContent()));
				Ne.setRouterId(Integer.parseInt(eElement.getElementsByTagName("RouterId").item(0).getTextContent()));
				//System.out.println(Integer.parseInt(eElement.getElementsByTagName("RouterId").item(0).getTextContent()));
				Ne.setSapi(Integer.parseInt(eElement.getElementsByTagName("SAPI").item(0).getTextContent()));
				Ne.setSubNetworkId(eElement.getElementsByTagName("SAPI").item(0).getTextContent());
				NEArray.add(Ne);
				
				
				nodeObjToCompareWith.setNodeId(Ne.getRouterId());
				nodeObjToCompareWith.setStationName(Ne.getStationName());
				nodeObjToCompareWith.setSiteName(Ne.getSiteName());
				nodeObjToCompareWith.setNodeType(Ne.getNeType());
				nodeObjToCompareWith.setDegree(Ne.getDegree());
				nodeObjToCompareWith.setCapacity(Ne.getSystemCapacity());
				nodeObjToCompareWith.setDirection(Ne.getDirection());
				nodeObjToCompareWith.setOpticalReach(Ne.getOpticalReach());
                nodeid = Ne.getRouterId();
			}
			

			NodeList nlist = document.getElementsByTagName("IPDetails");

			for (int i = 0; i < nlist.getLength(); i++) // adding IpDetails in to IpARRAY

			{
				Node kode = nlist.item(i);
				Element eElement = (Element) kode;

				Ip.setMcpIp(eElement.getElementsByTagName("MCPIP").item(0).getTextContent());
				Ip.setSubnet(eElement.getElementsByTagName("Subnet").item(0).getTextContent());
				Ip.setGateway(eElement.getElementsByTagName("Gateway").item(0).getTextContent());
				Ip.setLctIp(eElement.getElementsByTagName("LCTIP").item(0).getTextContent());
				Ip.setScpIp(eElement.getElementsByTagName("SCPIP").item(0).getTextContent());
				Ip.setRouterIp(eElement.getElementsByTagName("RouterIP").item(0).getTextContent());
				IPArray.add(Ip);

			}

			NodeList glist = document.getElementsByTagName("GNE_INFO");

			for (int i = 0; i < glist.getLength(); i++) // ADDING gne_INFO to GNEARRAY
			{
				Node gode = glist.item(i);
				Element eElement = (Element) gode;

				GNE.setGneFlag(Integer.parseInt(eElement.getElementsByTagName("GNE_FLAG").item(0).getTextContent()));
				GNE.setGneIp(eElement.getElementsByTagName("GNE_IP").item(0).getTextContent());
				GNE.setVlan(Integer.parseInt(eElement.getElementsByTagName("VLAN").item(0).getTextContent()));
				GNE.setSubnetMask(eElement.getElementsByTagName("SubnetMask").item(0).getTextContent());
				GNE.setGateway(eElement.getElementsByTagName("Gateway").item(0).getTextContent());
				GNE.setIpv6(eElement.getElementsByTagName("IPv6").item(0).getTextContent());
				GNEArray.add(GNE);
				nodeObjToCompareWith.setIsGne(GNE.getGneFlag());
				nodeObjToCompareWith.setIp(GNE.getGneIp());
				
				nodeObjToCompareWith.setVlanTag(GNE.getVlan());
				nodeObjToCompareWith.setEmsSubnet(GNE.getSubnetMask());
				nodeObjToCompareWith.setEmsGateway(GNE.getGateway());
				nodeObjToCompareWith.setIpV6Add(GNE.getIpv6());

			}

			// passing values to Node for comparing xml
			
			nodeObjToCompareWith.setNetworkId(networkID);
		    nodeObjToCompareWith.setNodeSubType(0);
			
			NodeList llist = document.getElementsByTagName("link");

			for (int i = 0; i < llist.getLength(); i++) {
				Node lode = llist.item(i);
				Element eElement = (Element) lode;

				li.setDirection(Integer.parseInt(eElement.getElementsByTagName("direction").item(0).getTextContent()));
				li.setLocalIP(eElement.getElementsByTagName("LocalIP").item(0).getTextContent());
				li.setRemoteIP(eElement.getElementsByTagName("RemoteIP").item(0).getTextContent());
				li.setSubnetMask(eElement.getElementsByTagName("SubnetMask").item(0).getTextContent());
				li.setRackId(Integer.parseInt(eElement.getElementsByTagName("RackId").item(0).getTextContent()));
				li.setSubRackId(Integer.parseInt(eElement.getElementsByTagName("SubRackId").item(0).getTextContent()));
				li.setCardId(Integer.parseInt(eElement.getElementsByTagName("CardId").item(0).getTextContent()));
				li.setPortId(Integer.parseInt(eElement.getElementsByTagName("PortId").item(0).getTextContent()));
				linkArray.add(li);

			}
			
           
			application.model.Node dbNode = dbService.getNodeService().FindNode(networkID, Ne.getRouterId());
			 //System.out.println("XML"+ dbNode.getStationName());
			//System.out.println(dbNode.getStationName()+":"+nodeObjToCompareWith.getStationName());
			JSONArray JSONNodeObj = new JSONArray();
		
			
			if (!nodeObjToCompareWith.getStationName().equals( dbNode.getStationName())) 
			{
				UpdatedData.put("StationName", nodeObjToCompareWith.getStationName());
				JSONArray DifferenceJSONArray = new JSONArray();
				org.json.simple.JSONObject  DifferenceJSONObj = new org.json.simple.JSONObject();
				
				DifferenceJSONArray.add(dbNode.getStationName());
				DifferenceJSONArray.add(nodeObjToCompareWith.getStationName());
                DifferenceJSONObj.put("StationName",DifferenceJSONArray );
                
                JSONNodeObj.add(DifferenceJSONObj);
                  
                
                
			}
			if (!nodeObjToCompareWith.getSiteName().equals(dbNode.getSiteName())) {
				UpdatedData.put("SiteName", nodeObjToCompareWith.getSiteName());
				JSONArray DifferenceJSONArray = new JSONArray();
				org.json.simple.JSONObject  DifferenceJSONObj = new org.json.simple.JSONObject();
				DifferenceJSONArray.add(dbNode.getSiteName());
				DifferenceJSONArray.add(nodeObjToCompareWith.getSiteName());
                DifferenceJSONObj.put("SiteName",DifferenceJSONArray );
                JSONNodeObj.add(DifferenceJSONObj);
                
			}
			if (nodeObjToCompareWith.getNodeType() != dbNode.getNodeType()) {
				UpdatedData.put("NodeType", nodeObjToCompareWith.getNodeType());
				JSONArray DifferenceJSONArray = new JSONArray();
				org.json.simple.JSONObject  DifferenceJSONObj = new org.json.simple.JSONObject();
				DifferenceJSONArray.add(dbNode.getNodeType());
				DifferenceJSONArray.add(nodeObjToCompareWith.getNodeType());
                DifferenceJSONObj.put("NodeType",DifferenceJSONArray );
                JSONNodeObj.add(DifferenceJSONObj);
                
			}
			if (nodeObjToCompareWith.getDegree() != dbNode.getDegree()) {
		        UpdatedData.put("Degree", nodeObjToCompareWith.getDegree());
				JSONArray DifferenceJSONArray = new JSONArray();
				org.json.simple.JSONObject  DifferenceJSONObj = new org.json.simple.JSONObject();
				DifferenceJSONArray.add(dbNode.getDegree());
				DifferenceJSONArray.add(nodeObjToCompareWith.getDegree());
                DifferenceJSONObj.put("Degree",DifferenceJSONArray );
                JSONNodeObj.add(DifferenceJSONObj);
                
			}
			if (!nodeObjToCompareWith.getIp().equals (dbNode.getIp())) {
				UpdatedData.put("Ip", nodeObjToCompareWith.getIp());
				JSONArray DifferenceJSONArray = new JSONArray();
				org.json.simple.JSONObject  DifferenceJSONObj = new org.json.simple.JSONObject();
				DifferenceJSONArray.add(dbNode.getIp());
			DifferenceJSONArray.add(nodeObjToCompareWith.getIp());
              DifferenceJSONObj.put("IP",DifferenceJSONArray );
              JSONNodeObj.add(DifferenceJSONObj);
              
			}
			if (!nodeObjToCompareWith.getCapacity().equals(dbNode.getCapacity())) {
				UpdatedData.put("Capacity", nodeObjToCompareWith.getCapacity());
				JSONArray DifferenceJSONArray = new JSONArray();
				org.json.simple.JSONObject  DifferenceJSONObj = new org.json.simple.JSONObject();
				DifferenceJSONArray.add(dbNode.getCapacity());
				DifferenceJSONArray.add(nodeObjToCompareWith.getCapacity());
                DifferenceJSONObj.put("Capacity",DifferenceJSONArray );
                JSONNodeObj.add(DifferenceJSONObj);
                
			}
			if (nodeObjToCompareWith.getDirection() != dbNode.getDirection()) {
				UpdatedData.put("Direction", nodeObjToCompareWith.getDirection());
				JSONArray DifferenceJSONArray = new JSONArray();
				org.json.simple.JSONObject  DifferenceJSONObj = new org.json.simple.JSONObject();
				DifferenceJSONArray.add(dbNode.getDirection());
				DifferenceJSONArray.add(nodeObjToCompareWith.getDirection());
                DifferenceJSONObj.put("Direction",DifferenceJSONArray );
                JSONNodeObj.add(DifferenceJSONObj);
                
			}
			if (!nodeObjToCompareWith.getOpticalReach().equals (dbNode.getOpticalReach())) {
				UpdatedData.put("OpticalReach", nodeObjToCompareWith.getOpticalReach());
				JSONArray DifferenceJSONArray = new JSONArray();
				org.json.simple.JSONObject  DifferenceJSONObj = new org.json.simple.JSONObject();
				DifferenceJSONArray.add(dbNode.getOpticalReach());
				DifferenceJSONArray.add(nodeObjToCompareWith.getOpticalReach());
                DifferenceJSONObj.put("OpticalReach",DifferenceJSONArray );
                JSONNodeObj.add(DifferenceJSONObj);
                
			}
			if (nodeObjToCompareWith.getIsGne() != dbNode.getIsGne()) {
				UpdatedData.put("IsGne", nodeObjToCompareWith.getIsGne());
				JSONArray DifferenceJSONArray = new JSONArray();
				org.json.simple.JSONObject  DifferenceJSONObj = new org.json.simple.JSONObject();
				DifferenceJSONArray.add(dbNode.getIsGne());
				DifferenceJSONArray.add(nodeObjToCompareWith.getIsGne());
                DifferenceJSONObj.put("IsGne",DifferenceJSONArray );
                JSONNodeObj.add(DifferenceJSONObj);
                
			}
			if (nodeObjToCompareWith.getVlanTag() != dbNode.getVlanTag()) {
				UpdatedData.put("VlanTag", nodeObjToCompareWith.getVlanTag());
			    JSONArray DifferenceJSONArray = new JSONArray();
			    org.json.simple.JSONObject  DifferenceJSONObj = new org.json.simple.JSONObject();
				DifferenceJSONArray.add(dbNode.getVlanTag());
				DifferenceJSONArray.add(nodeObjToCompareWith.getVlanTag());
                DifferenceJSONObj.put("VlanTag",DifferenceJSONArray );
                JSONNodeObj.add(DifferenceJSONObj);
                
			}
			if (!nodeObjToCompareWith.getEmsSubnet().equals(dbNode.getEmsSubnet())) {
				UpdatedData.put("EmsSubnet", nodeObjToCompareWith.getEmsSubnet());
				JSONArray DifferenceJSONArray = new JSONArray();
				org.json.simple.JSONObject  DifferenceJSONObj = new org.json.simple.JSONObject();
				DifferenceJSONArray.add(dbNode.getEmsSubnet());
				DifferenceJSONArray.add(nodeObjToCompareWith.getEmsSubnet());
                DifferenceJSONObj.put("EmsSubnet",DifferenceJSONArray );
                JSONNodeObj.add(DifferenceJSONObj);
                
				
			}
			if (!nodeObjToCompareWith.getEmsGateway().equals(dbNode.getEmsGateway())) {
				UpdatedData.put("EmsGateway", nodeObjToCompareWith.getEmsGateway());
				JSONArray DifferenceJSONArray = new JSONArray();
				org.json.simple.JSONObject  DifferenceJSONObj = new org.json.simple.JSONObject();
				DifferenceJSONArray.add(dbNode.getEmsGateway());
				DifferenceJSONArray.add(nodeObjToCompareWith.getEmsGateway());
                DifferenceJSONObj.put("EmsGateway",DifferenceJSONArray );
                JSONNodeObj.add(DifferenceJSONObj);
                
			}
			if (!nodeObjToCompareWith.getIpV6Add().equals(dbNode.getIpV6Add())) {
				UpdatedData.put("IpV6Add", nodeObjToCompareWith.getIpV6Add());
				JSONArray DifferenceJSONArray = new JSONArray();
				org.json.simple.JSONObject  DifferenceJSONObj = new org.json.simple.JSONObject();
				DifferenceJSONArray.add(dbNode.getIpV6Add());
				DifferenceJSONArray.add(nodeObjToCompareWith.getIpV6Add());
                DifferenceJSONObj.put("IpV6Add",DifferenceJSONArray );
                JSONNodeObj.add(DifferenceJSONObj);
                
		}
			if(!JSONNodeObj.isEmpty())
			{
			JSONNodeArray.add(JSONNodeObj);
			FinalNodeJSON.put("NetworkId", networkID);
			FinalNodeJSON.put("NodeId", nodeid);
			FinalNodeJSON.put("NodeArray",JSONNodeArray);
			System.out.println(FinalNodeJSON);
			}
			
			
		
		
		if(!UpdatedData.isEmpty())
		{
	NodeDao NodeDao = new NodeDao();
	dbService.getNodeService().Updatedb(UpdatedData, networkID, Ne.getRouterId());
		
		}  

		//System.out.println("JSONNODEOBJ"+JSONNodeObj);
		
		
		}
		
		catch (Exception e)

		{
			e.printStackTrace();

		}
		
		//System.out.println(NodeDifference);
		
		return FinalNodeJSON;

	
	}

}
