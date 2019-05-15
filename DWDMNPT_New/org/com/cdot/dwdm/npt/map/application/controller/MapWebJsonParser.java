/**
 * @brief Main Parser Class : Called on Save Flow from UI
 */
package application.controller;



import java.sql.SQLException;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.couchbase.client.java.document.json.JsonObject;

import application.MainMap;
import application.constants.MapConstants;
import application.model.Link;
import application.model.MapData;
import application.model.Network;
import application.model.Node;
import application.model.Topology;
import application.service.DbService;

/**
 * @author hp
 * 
 * @brief This Parser Called on Map Save request from Web UI. It parse all the required filed
 *        from the Json string data and store it into the DB. 
 *
 */
/*@Service*/
public class MapWebJsonParser {	

/*	@Autowired
	private NodeService nodeService;
	
	@Autowired
	private NetworkService networkService;*/

		
	/**
	 * 
	 * @param jsonStr Json String from the Main Web Controller
	 * @return int         Success : 0 and Fail : 1
	 * @throws SQLException 
	 */	
	@SuppressWarnings("static-access")
	public int jsonParse(String jsonStr, DbService dbService) throws SQLException{
		
		MainMap.logger.info("--------------------------JSON Parsing Controller Start---------------------------------------");
		 
//		MainMap.logger.info("Json Str :- " + jsonStr);
		 
		/**Store JSON Object into DB*/	
		
		ResourcePlanUtils rpu=new ResourcePlanUtils(dbService);
		
		/**Logger Start*/		
		 MainMap.logger = MainMap.logger.getLogger(MapWebJsonParser.class.getName());
	/*	 Map.logger.info("Here is some DEBUG");
		 Map.logger.info("Here is some INFO "+Map.logger.getLevel()+ Map.logger.isDebugEnabled()+ Map.logger.isInfoEnabled());
		 Map.logger.warn("Here is some WARN");
		 Map.logger.error("Here is some ERROR");
		 Map.logger.fatal("Here is some FATAL");*/		 
		 
		 JSONObject jsonMapDataObj = new JSONObject(jsonStr);
		 System.out.println("jsonMapDataObj :"+jsonMapDataObj.get("NetworkInfo"));
//		 System.out.println("jsonMap url base64   :"+jsonMapDataObj.get("MapImage"));
		 
		 
		 /**
		  * Following API Convert the Base64 URL into Image
		  */
		 CanvasController canvasControllerObj = new CanvasController();
		 try {
			 canvasControllerObj.createCanvasImage(jsonMapDataObj.get("MapImage").toString());	 
		 }
		 catch (Exception e) {
			 System.out.println(" Exception While createCanvasImage : "+ e);
		}
		 
		 
		 JSONObject networkInfoObj=(JSONObject)jsonMapDataObj.get("NetworkInfo");
		 JSONObject jsonObj = (JSONObject)jsonMapDataObj.get("MapStr");
		// JSONArray jsonMapStateArr = (JSONArray)jsonMapDataObj.get("MapState");
		 
//		 JSONObject mapDataJsonObject=new JSONObject();
//		 mapDataJsonObject.put("MapStr", jsonMapDataObj.get("MapStr"));
//		 mapDataJsonObject.put("MapState", jsonMapDataObj.get("MapState"));
		 
		 System.out.println(networkInfoObj.toString());

		 /**Network Info Set and DB call to network Table*/		 
		 Network networkObj	= new Network();					
		 networkObj.setTopology(networkInfoObj.getString("Topology"));
		 networkObj.setArea(networkInfoObj.getString("NetworkName"));
		 networkObj.setServiceProvider(networkInfoObj.getString("NetworkName"));
		 networkObj.setNetworkName(networkInfoObj.getString("NetworkName"));
		
		 int networkId=Integer.parseInt(dbService.getNetworkService().GetByNetworkName(networkObj.getNetworkName()).toString());
		 
		 if(dbService.getNetworkService().GetByNetworkName(networkObj.getNetworkName())!= null){
			 MainMap.logger.info(" Going to Delete Existing network in order to update it .."+ dbService.getNetworkService().GetByNetworkName(/*"TestNetwork"*/networkObj.getNetworkName()).toString());
			 dbService.getNetworkService().DeleteNetwork(Integer.parseInt(dbService.getNetworkService().GetByNetworkName(/*"TestNetwork"*/networkObj.getNetworkName()).toString()));/**DBG=> Temp Entry*/			 
		 }
		 else{
			 dbService.getNetworkService().InsertNetwork(networkObj);			 
		 }			 
		 
		 /**Actual Parsing*/
		//JSONObject jsonObj = new JSONObject(jsonStr);
		
		/**Save Map JSON to DB */
	     MapData mapData=new MapData();
	     mapData.setNetworkId(Integer.parseInt(dbService.getNetworkService().GetByNetworkName(/*"TestNetwork"*/networkObj.getNetworkName()).toString())); // DBG
	     mapData.setMap(jsonObj.toString().trim());
//	     mapData.setMap(mapDataJsonObject.toString().trim());
	     dbService.getMapDataService().Insert(mapData);
	
		try {
			
			JSONArray array = (JSONArray) jsonObj.get("cells");/**get the cells array from json*/
			MainMap.logger.info("Length of Cell array :- "+array.length());
			
			int i = 0;
			
			for (i = 0; i < array.length(); i++) {/**loop through all the elements of the network*/
				
				JSONObject mainJsonObj = array.getJSONObject(i);
			
				
				MainMap.logger.info("Parsing Started of Type :- "+mainJsonObj.get("type").toString());
				
				

				/**
				 * Network Info Parsing Started
				 */				
//				if(mainJsonObj.get("type").toString().equalsIgnoreCase("network")){
//					
//					Map.logger.info("Network Parsing Started ..");
//				
//					/**Node Properties*/									
//					Map.logger.info("Network Name :- "+mainJsonObj.get("networkname"));
//					
//					/**Network Info Set and DB call to network Table*/
//					Network networkObj	= new Network();					
//					networkObj.setNetworkName(mainJsonObj.get("networkname").toString());
//					
//					networkService.InsertNetwork(networkObj);					
//						
//				}
				
				
				
				/**
				 * Node Parsing : Hub or Roadm or ILA
				 */
				if(mainJsonObj.get("type").toString().equalsIgnoreCase("devs.roadm") ||
						mainJsonObj.get("type").toString().equalsIgnoreCase("devs.hub") ||
						mainJsonObj.get("type").toString().equalsIgnoreCase("devs.ila") ||
						mainJsonObj.get("type").toString().equalsIgnoreCase("devs.TE")||
						mainJsonObj.get("type").toString().equalsIgnoreCase("devs.BSRoadm")||
						mainJsonObj.get("type").toString().equalsIgnoreCase("devs.cdroadm") ||
						mainJsonObj.get("type").toString().equalsIgnoreCase("devs.potp")){			
					
					
					
					MainMap.logger.info("Node  Parsing Started ..");
					
					Node nodeObj = new Node();					
					
					
					/**Node Properties*/
					JSONObject nodePropsJsonObj	 = (JSONObject)mainJsonObj.get("nodeProperties");
					
					
					JSONObject nodeConnectionsJsonObj	 = (JSONObject)nodePropsJsonObj.get("nodeConnections");
					System.out.println("-----nodeConnectionsJsonObj----"+nodeConnectionsJsonObj.toString());
//					JSONObject nodeDirectionPropsJsonObj	 = (JSONObject)nodePropsJsonObj.get("directions");
//					JSONObject nodeNumDirectionPropsJsonObj	 = (JSONObject)nodePropsJsonObj.get("numDirections");
//					JSONObject nodeDirectionsMapping=new JSONObject();
//					Iterator<?> keys = nodeDirectionPropsJsonObj.keys();
//					
//					while( keys.hasNext() ) {
//					    String key = (String)keys.next();
//					    nodeDirectionsMapping.put(key, 0);					    
//					}
//					
//					keys = nodeDirectionPropsJsonObj.keys();
//					while( keys.hasNext() ) {
//					    String key = (String)keys.next();
//					    int dirVal=Integer.parseInt(nodeNumDirectionPropsJsonObj.get(key).toString().trim());
//					    //nodeDirectionsMapping.put(key, 0);
//					    if(dirVal>0)
//					    {
//					    	String mappedDir=fGetDirectionStr(dirVal);
//						    nodeDirectionsMapping.put(mappedDir, nodeDirectionPropsJsonObj.get(key).toString()	);
//						    //System.out.println(mappedDir+" : Node Direction Mapping :"+nodeDirectionsMapping.get(mappedDir));
//					    }
//					    else
//					    {
//					    	//if already has been assigned , then don't overwrite it
//					    	if(Integer.parseInt(nodeDirectionsMapping.get(key).toString().trim())==0)
//					    	nodeDirectionsMapping.put(key, nodeNumDirectionPropsJsonObj.get(key));
//					    	//System.out.println(key+" : Node Direction Mapping :"+nodeDirectionsMapping.get(key));
//					    }
//					    
//					}
//					
//					keys = nodeDirectionsMapping.keys();
//					while( keys.hasNext() ) {
//					    String key = (String)keys.next();
//					    System.out.println(key+" : Node Direction Mapping :"+nodeDirectionsMapping.get(key));					    
//					}
					
					MainMap.logger.info("ROADM ID:- "+mainJsonObj.get("id").toString());
					MainMap.logger.info("ROADM Node ID:- "+nodePropsJsonObj.getInt("nodeId"));
					MainMap.logger.info("Station Name:- "+nodePropsJsonObj.get("stationName").toString());
					MainMap.logger.info("Site Name:- "+nodePropsJsonObj.get("siteName").toString());
					MainMap.logger.info("Gne Flag:- "+nodePropsJsonObj.get("gneFlag"));					
					MainMap.logger.info("Vlan Tag:- "+nodePropsJsonObj.get("vlanTag"));
					MainMap.logger.info("Degree:- "+nodePropsJsonObj.get("degree"));
					MainMap.logger.info("Directions:- "+nodePropsJsonObj.get("directions"));	
//					MainMap.logger.info("East:- "+nodeDirectionPropsJsonObj.get("east")+ " -- "+nodeDirectionPropsJsonObj.get("east"));
//					MainMap.logger.info("West:- "+nodeDirectionPropsJsonObj.get("west")+ " -- "+nodeDirectionPropsJsonObj.get("west"));
//					MainMap.logger.info("North:- "+nodeDirectionPropsJsonObj.get("north")+ " -- "+nodeDirectionPropsJsonObj.get("north"));
//					MainMap.logger.info("South:- "+nodeDirectionPropsJsonObj.get("south")+ " -- "+nodeDirectionPropsJsonObj.get("south"));
//					MainMap.logger.info("North-East:- "+nodeDirectionPropsJsonObj.get("ne")+ " -- "+nodeDirectionPropsJsonObj.get("ne"));
//					MainMap.logger.info("North-West:- "+nodeDirectionPropsJsonObj.get("nw")+ " -- "+nodeDirectionPropsJsonObj.get("nw"));
//					MainMap.logger.info("South-East:- "+nodeDirectionPropsJsonObj.get("se")+ " -- "+nodeDirectionPropsJsonObj.get("se"));
//					MainMap.logger.info("South-West:- "+nodeDirectionPropsJsonObj.get("sw")+ " -- "+nodeDirectionPropsJsonObj.get("sw"));
					
					MainMap.logger.info("East:- "+nodeConnectionsJsonObj.get(""+MapConstants._EAST)+ " -- "+nodeConnectionsJsonObj.get(""+MapConstants._EAST));
					MainMap.logger.info("West:- "+nodeConnectionsJsonObj.get(""+MapConstants._WEST)+ " -- "+nodeConnectionsJsonObj.get(""+MapConstants._WEST));
					MainMap.logger.info("North:- "+nodeConnectionsJsonObj.get(""+MapConstants._NORTH)+ " -- "+nodeConnectionsJsonObj.get(""+MapConstants._NORTH));
					MainMap.logger.info("South:- "+nodeConnectionsJsonObj.get(""+MapConstants._SOUTH)+ " -- "+nodeConnectionsJsonObj.get(""+MapConstants._SOUTH));
					MainMap.logger.info("North-East:- "+nodeConnectionsJsonObj.get(""+MapConstants._NE)+ " -- "+nodeConnectionsJsonObj.get(""+MapConstants._NE));
					MainMap.logger.info("North-West:- "+nodeConnectionsJsonObj.get(""+MapConstants._NW)+ " -- "+nodeConnectionsJsonObj.get(""+MapConstants._NW));
					MainMap.logger.info("South-East:- "+nodeConnectionsJsonObj.get(""+MapConstants._SE)+ " -- "+nodeConnectionsJsonObj.get(""+MapConstants._SE));
					MainMap.logger.info("South-West:- "+nodeConnectionsJsonObj.get(""+MapConstants._SW)+ " -- "+nodeConnectionsJsonObj.get(""+MapConstants._SW));
					
					
									
					
					if(mainJsonObj.get("type").toString().equalsIgnoreCase("devs.hub"))
						nodeObj.setNodeType(MapConstants.hub);
					else if(mainJsonObj.get("type").toString().equalsIgnoreCase("devs.roadm"))
						nodeObj.setNodeType(MapConstants.roadm);
					else if(mainJsonObj.get("type").toString().equalsIgnoreCase("devs.ila"))
						nodeObj.setNodeType(MapConstants.ila);
					else if(mainJsonObj.get("type").toString().equalsIgnoreCase("devs.TE"))
						nodeObj.setNodeType(MapConstants.te);									
					else if(mainJsonObj.get("type").toString().equalsIgnoreCase("devs.BSRoadm"))
						nodeObj.setNodeType(MapConstants.twoBselectRoadm);
					else if(mainJsonObj.get("type").toString().equalsIgnoreCase("devs.cdroadm"))
						nodeObj.setNodeType(MapConstants.cdRoadm);
					else if(mainJsonObj.get("type").toString().equalsIgnoreCase("devs.potp"))
						nodeObj.setNodeType(MapConstants.potp);
										
					/**Network Info Set and DB call to nodeproperties Table
					 * NetworkId, NodeId, StationName, SiteName, NodeType, NodeSubType, Degree,"
     		+ " Ip, IsGne, VlanTag, EmsSubnet, EmsGateway, IpV6Add, Capacity, Direction*/

					//nodeObj.setNetworkId(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));/**DBG => Temp entry*/
					nodeObj.setNetworkId(networkId);
					nodeObj.setNodeId(nodePropsJsonObj.getInt("nodeId"));
					nodeObj.setStationName(nodePropsJsonObj.get("stationName").toString());
					nodeObj.setSiteName(nodePropsJsonObj.get("siteName").toString());
					nodeObj.setDegree(nodePropsJsonObj.getInt("degree"));
					nodeObj.setIsGne(nodePropsJsonObj.getInt("gneFlag"));
					nodeObj.setVlanTag(nodePropsJsonObj.getInt("vlanTag"));					
					nodeObj.setIp(String.valueOf(nodePropsJsonObj.get("emsip")));
					nodeObj.setIpV6Add(String.valueOf(nodePropsJsonObj.get("ipv6")));
					nodeObj.setOpticalReach(nodePropsJsonObj.get("opticalReach").toString());
					nodeObj.setEmsSubnet(nodePropsJsonObj.get("subnet").toString());
					nodeObj.setNodeSubType(rpu.fGetIlaSubtype(nodePropsJsonObj.get("nodeSubtype").toString()));/**DBG => Temp*/
					nodeObj.setCapacity(nodePropsJsonObj.get("capacity").toString());
					nodeObj.setDirection(0);/**DBG => Temp*/
					nodeObj.setEmsGateway(nodePropsJsonObj.get("gateway").toString());/**DBG => Temp*/
					
					dbService.getNodeService().InsertNode(nodeObj);
			
					/**Topology matrix to Topology Table*/
					
					Topology topologyObj = new Topology();				
				
					/**Set all 8-Direction: where value in that direction represent the node id connected to it*/
//					topologyObj.setDir1(nodeDirectionPropsJsonObj.getInt("east"));
//					topologyObj.setDir2(nodeDirectionPropsJsonObj.getInt("west"));
//					topologyObj.setDir3(nodeDirectionPropsJsonObj.getInt("north"));
//					topologyObj.setDir4(nodeDirectionPropsJsonObj.getInt("south"));
//					topologyObj.setDir5(nodeDirectionPropsJsonObj.getInt("ne"));
//					topologyObj.setDir6(nodeDirectionPropsJsonObj.getInt("nw") );
//					topologyObj.setDir7(nodeDirectionPropsJsonObj.getInt("se"));
//					topologyObj.setDir8(nodeDirectionPropsJsonObj.getInt("sw") );
					
					topologyObj.setDir1(nodeConnectionsJsonObj.getInt(""+MapConstants._EAST));
					topologyObj.setDir2(nodeConnectionsJsonObj.getInt(""+MapConstants._WEST));
					topologyObj.setDir3(nodeConnectionsJsonObj.getInt(""+MapConstants._NORTH));
					topologyObj.setDir4(nodeConnectionsJsonObj.getInt(""+MapConstants._SOUTH));
					topologyObj.setDir5(nodeConnectionsJsonObj.getInt(""+MapConstants._NE));
					topologyObj.setDir6(nodeConnectionsJsonObj.getInt(""+MapConstants._NW));
					topologyObj.setDir7(nodeConnectionsJsonObj.getInt(""+MapConstants._SE));
					topologyObj.setDir8(nodeConnectionsJsonObj.getInt(""+MapConstants._SW));

					
				
					//topologyObj.setNetworkId(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
					topologyObj.setNetworkId(networkId);
					topologyObj.setNodeId(nodePropsJsonObj.getInt("nodeId"));
					
					dbService.getTopologyService().InsertTopology(topologyObj);
				
				}
				
							
				/**
				 * Link Parsing
				 */
				if(mainJsonObj.get("type").toString().equalsIgnoreCase("link")){
													
					JSONObject linkSourcePropsJsonObj		 	  = (JSONObject)mainJsonObj.get("source");
					JSONObject linkTargetPropsJsonObj		 	  = (JSONObject)mainJsonObj.get("target");
					JSONObject linkPropsJsonObj		 	  		  = (JSONObject)mainJsonObj.get("linkProperties");
					
					Link linkObj = new Link();
					
					
					MainMap.logger.info("Link Id:- "+linkPropsJsonObj.get("linkId").toString());
					MainMap.logger.info("Source Info Id:- "+linkSourcePropsJsonObj.get("NodeId").toString());
					MainMap.logger.info("Target Info Id:- "+linkTargetPropsJsonObj.get("NodeId").toString());
					MainMap.logger.info("Span Length:- "+linkPropsJsonObj.get("spanLength").toString());
					MainMap.logger.info("Fiber Type:- "+linkPropsJsonObj.get("fiberType").toString());
					MainMap.logger.info("Span Loss:- "+linkPropsJsonObj.get("adjustableSpanLoss").toString());
					MainMap.logger.info("Cost Metrix:- "+linkPropsJsonObj.get("costMetric").toString());	
					MainMap.logger.info("SRLG:- "+linkPropsJsonObj.get("srlg").toString());
					MainMap.logger.info("Color:- "+linkPropsJsonObj.get("color").toString());
										
					
					/**Link Info Set and DB call to Links Table*/
					//linkObj.setNetworkId(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
					linkObj.setNetworkId(networkId);
					linkObj.setLinkId(Integer.parseInt(linkPropsJsonObj.get("linkId").toString()));
					linkObj.setSrcNode(Integer.parseInt(linkSourcePropsJsonObj.get("NodeId").toString()));
					linkObj.setDestNode(Integer.parseInt(linkTargetPropsJsonObj.get("NodeId").toString()));
					linkObj.setColour(Integer.parseInt(linkPropsJsonObj.get("color").toString()));
					linkObj.setMetricCost(Integer.parseInt(linkPropsJsonObj.get("costMetric").toString()));
					linkObj.setFibreType(linkPropsJsonObj.getInt("fiberType"));
					linkObj.setCdCoff(Float.parseFloat(linkPropsJsonObj.get("cdCoefficient").toString()));
					linkObj.setCd(Float.parseFloat(linkPropsJsonObj.get("cd").toString()));
					linkObj.setPmdCoff(Float.parseFloat(linkPropsJsonObj.get("pmdCoefficient").toString()));
					linkObj.setPmd(Float.parseFloat(linkPropsJsonObj.get("pmd").toString()));
					linkObj.setSpanLoss(Float.parseFloat(linkPropsJsonObj.get("adjustableSpanLoss").toString()));					
					linkObj.setLength(Float.parseFloat(linkPropsJsonObj.get("spanLength").toString()));
					linkObj.setSrlgId(Integer.parseInt(linkPropsJsonObj.get("srlg").toString()));
					linkObj.setSrcNodeDirection(MapConstants.fGetDirectionStr(linkPropsJsonObj.getInt("SrcNodeDirection")));
					linkObj.setDestNodeDirection(MapConstants.fGetDirectionStr(linkPropsJsonObj.getInt("DestNodeDirection")));
					String omsPtc=linkPropsJsonObj.get("lineProtection").toString().equalsIgnoreCase("1")?"Yes":"No";
					linkObj.setOMSProtection(omsPtc);
					
					if(linkPropsJsonObj.has("linkType"))
					linkObj.setLinkType(linkPropsJsonObj.get("linkType").toString());
					else linkObj.setLinkType("Default");
					
					dbService.getLinkService().InsertLink(linkObj);					
					
				}
				
				
				
		}					
		
		} catch (JSONException e) {
			e.printStackTrace();
			throw e;
//			return MapConstants.FAILURE;
		}
	
		MainMap.logger.info("--------------------------JSON Parsing Controller End---------------------------------------");
		return MapConstants.SUCCESS;

	}
	
	
}
