package application.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import application.MainMap;
import application.constants.MapConstants;
import application.model.IpSchemeLink;
import application.model.IpSchemeNode;
import application.model.Link;
import application.model.LinkWavelengthInfo;
import application.model.NetworkRoute;
import application.model.Node;
import application.model.CardInfo;
import application.service.DbService;
import application.service.IPv4;

import org.apache.log4j.Logger;


/**
 * Graph Controller
 * @param data
 * @return
 */
public class GraphController{

	public static Logger logger = Logger.getLogger(GraphController.class.getName());
	DbService dbService;
	@SuppressWarnings("unchecked")
	public JSONObject GenerateReportModal(String data,DbService dbService) 
	{
		GraphController.logger.info("GenerateReportModal data : "+data);
		GraphController.logger = MainMap.logger.getLogger(GraphController.class.getName());
		
		JSONObject FinaldataObj = new JSONObject();
	
		/** Map From Common API */
		HashMap<String, Object> networkInfoMap = MapWebCommonAPIs.NestedStaticClass.getNetworkInformation(dbService,data);
		int networkId = (int) networkInfoMap.get(MapConstants.NetworkId); /** Network Id for common use */
		String networkName=  (String) networkInfoMap.get(MapConstants.NetworkName);
		
		String blockLinkAdd=MapConstants.block1LinkStartAddress;
		String blockNodeAdd=MapConstants.block1NodeStartAddress;
		
		FinaldataObj.put("NodeId_Price",NodeId_Price(networkId,dbService));
		FinaldataObj.put("NodeId_Wavelength", Wavelength_Node(networkId,dbService));
		//FinaldataObj.put("NodeId_Traffic", Traffic_Node(networkId,dbService));
		FinaldataObj.put("NodeId_Power", Power_Node(networkId,dbService));
		FinaldataObj.put("Node_Ip", Node_Ip(networkId,dbService,blockNodeAdd));
		FinaldataObj.put("Link_Wavelength",Wavelength_Link(networkId,dbService));
		FinaldataObj.put("Link_Traffic", Traffic_Link(networkId,dbService));
		FinaldataObj.put("Link_Ip", Link_Ip(networkId,dbService,blockLinkAdd));

		FinaldataObj.put("NodeId_WavelengthTraffic", WavelengthTraffic_Node(networkId,dbService));
		
		GraphController.logger.info("FinalDataObj:"+FinaldataObj);
		//System.out.println("FinalDataObj :"+FinaldataObj);

		return FinaldataObj;

	}
	//Fetching data for Node Id and Total Price from DB//
	@SuppressWarnings("unchecked")
	public JSONObject NodeId_Price(int networkId,DbService dbService) 
	{
		GraphController.logger.info("Node id and Total Price Function");
		int i=0;
		JSONObject priceObj = new JSONObject();
		JSONArray nodeid_for_price = new JSONArray();
		JSONArray price_array = new JSONArray();
		JSONObject objectforprice = new JSONObject();
		JSONObject object1forprice = new JSONObject();
		JSONArray xAxesforprice = new JSONArray();
		JSONArray yAxesforprice = new JSONArray();
		JSONObject finalLabelObjforprice = new JSONObject();
		JSONObject labelobjforprice = new JSONObject();
		JSONObject finaljsonObjectforprice = new JSONObject();
		JSONObject finalObjectforprice = new JSONObject();
		JSONObject finalObject1forprice = new JSONObject();
		JSONObject finaljsonObject1forprice = new JSONObject();
		JSONObject titleObjforprice = new JSONObject();
		JSONObject legendObjPrice = new JSONObject();
		
		GraphController.logger.info("Node Data");
		List <Node> node = dbService.getNodeService().FindNodeId(networkId);//Fetching Node Id's from Db of the respective Network ID//
		GraphController.logger.debug(node);
		List<Object> Totalprice=new ArrayList<Object>();
		//Fetching sum of Price from db//
		for(i=0;i<node.size();i++)
		{
			List<Map<String, Object>>  Price= dbService.getViewServiceRp().FindsumTotalPrice(networkId,node.get(i).getNodeId());

			for (Map<String, Object> abc : Price) 
			{													 								   
				for (Map.Entry<String, Object> entry : abc.entrySet()) 
				{
					Totalprice.add(entry.getValue());//adding the price from Db in arraylist form in Total Price//					
				}
			}
			GraphController.logger.info("Total Price");
			GraphController.logger.debug(Totalprice);
		
			//Adding the data(nodeId) in JSONArray form//
			for(i=0;i<node.size();i++) {
				int nodeId = node.get(i).getNodeId();
				nodeid_for_price.add(nodeId);

			}
		}
		//Data in JSON form used for plotting the graphs//

		priceObj.put("label", "Total Price");
		priceObj.put("data", Totalprice);
		priceObj.put("backgroundColor", MapConstants.GraphColor_Coral);

		price_array.add(priceObj);

		objectforprice.put("display", "true");
		objectforprice.put("labelString", "Node Id");
		object1forprice.put("scaleLabel", objectforprice);
		object1forprice.put("display", "true");
		xAxesforprice.add(object1forprice);

		labelobjforprice.put("display", "true");
		labelobjforprice.put("labelString", "Total Price($)");
		finalLabelObjforprice.put("scaleLabel", labelobjforprice);
		finalLabelObjforprice.put("display", "true");
		yAxesforprice.add(finalLabelObjforprice);

		finaljsonObjectforprice.put("labels",nodeid_for_price);
		finaljsonObjectforprice.put("datasets", price_array);
		finaljsonObject1forprice.put("xAxes", xAxesforprice);
		finaljsonObject1forprice.put("yAxes", yAxesforprice);

		titleObjforprice.put("display", "true");
		titleObjforprice.put("text","NodeWise Price Distribution");

		finalObject1forprice.put("scales",finaljsonObject1forprice);
		finalObject1forprice.put("title", titleObjforprice);
		legendObjPrice.put("display", true);
		finalObject1forprice.put("legend", legendObjPrice);
		finalObjectforprice.put("type", "bar");
		finalObjectforprice.put("data", finaljsonObjectforprice);
		finalObjectforprice.put("options", finalObject1forprice);

		return finalObjectforprice;
	}


	////
	/**
	 * @brief Fetching data for Node Id and Number of Wavelength from DB
	 * 
	 *         
	 * @param networkId
	 * @param dbService
	 * @return
	 */
	
	@SuppressWarnings("unchecked")
	public JSONObject Wavelength_Node(int networkId,DbService dbService)
	{
		
		GraphController.logger.info("Node id and Wavelength Function");
		int i=0;
		JSONArray nodeid_for_wavelength = new JSONArray();
		JSONArray eastdata_from_db = new JSONArray();
		JSONArray westdata_from_db = new JSONArray();
		JSONArray northdata_from_db = new JSONArray();
		JSONArray southdata_from_db = new JSONArray();
		JSONArray southEastdata_from_db = new JSONArray();
		JSONArray northEastdata_from_db = new JSONArray();
		JSONArray southWestdata_from_db = new JSONArray();
		JSONArray northWestdata_from_db = new JSONArray();
		JSONObject eastObj = new JSONObject();
		JSONObject westObj = new JSONObject();
		JSONObject southObj = new JSONObject();
		JSONObject northObj = new JSONObject();
		JSONObject northEastObj = new JSONObject();
		JSONObject northWestObj = new JSONObject();
		JSONObject southEastObj = new JSONObject();
		JSONObject southWestObj = new JSONObject();
		JSONArray direction = new JSONArray();
		JSONObject objectforwavelength = new JSONObject();
		JSONObject object1forwavelength = new JSONObject();
		JSONArray xAxesforwavelength = new JSONArray();
		JSONArray yAxesforwavelength = new JSONArray();
		JSONObject finalLabelObjforwavelength = new JSONObject();
		JSONObject labelobjforwavelength = new JSONObject();
		JSONObject finaljsonObjectforwavelength = new JSONObject();
		JSONObject finalObjectforwavelength = new JSONObject();
		JSONObject finalObject1forwavelength = new JSONObject();
		JSONObject finaljsonObject1forwavelength = new JSONObject();
		JSONObject titleObjforwavelength = new JSONObject();
		JSONObject legendObjWavelength = new JSONObject();
		

		List <Node> node = dbService.getNodeService().FindNodeId(networkId);//Fetching Node Id's from Db of the respective Network ID//
		for(i=0;i<node.size();i++)
		{

			int nodeid=node.get(i).getNodeId();
			nodeid_for_wavelength.add(nodeid);
			
			int wavelengthEast = dbService.getCardInfoService().FindingCountWorkingMpnsForGraphs(networkId, nodeid,MapConstants.EAST);
			
			eastdata_from_db.add(wavelengthEast);
			
			int wavelengthWest = dbService.getCardInfoService().FindingCountWorkingMpnsForGraphs(networkId, nodeid,MapConstants.WEST);
			
			westdata_from_db.add(wavelengthWest);
			
			int wavelengthNorth = dbService.getCardInfoService().FindingCountWorkingMpnsForGraphs(networkId, nodeid,MapConstants.NORTH);
			
			northdata_from_db.add(wavelengthNorth);
			
			int wavelengthSouth = dbService.getCardInfoService().FindingCountWorkingMpnsForGraphs(networkId, nodeid,MapConstants.SOUTH);
			
			southdata_from_db.add(wavelengthSouth);
			
			int wavelengthNE = dbService.getCardInfoService().FindingCountWorkingMpnsForGraphs(networkId, nodeid,MapConstants.NE);
			
			northEastdata_from_db.add(wavelengthNE);
			
			int wavelengthNW = dbService.getCardInfoService().FindingCountWorkingMpnsForGraphs(networkId, nodeid,MapConstants.NW);
		
			northWestdata_from_db.add(wavelengthNW);
			
			int wavelengthSE = dbService.getCardInfoService().FindingCountWorkingMpnsForGraphs(networkId, nodeid,MapConstants.SE);
			
			southEastdata_from_db.add(wavelengthSE);
			
			int wavelengthSW = dbService.getCardInfoService().FindingCountWorkingMpnsForGraphs(networkId, nodeid,MapConstants.SW);
			
			southWestdata_from_db.add(wavelengthSW);
			
		}

		GraphController.logger.info("East data from database");
		GraphController.logger.debug(eastdata_from_db);
		GraphController.logger.info("West data from database");
		GraphController.logger.debug(westdata_from_db);
		GraphController.logger.info("North data from database");
		GraphController.logger.debug(northdata_from_db);
		GraphController.logger.info("South data from database");
		GraphController.logger.debug(southdata_from_db);
		GraphController.logger.info("North East data from database");
		GraphController.logger.debug(northEastdata_from_db);
		GraphController.logger.info("North West data from database");
		GraphController.logger.debug(northWestdata_from_db);
		GraphController.logger.info("South East data from database");
		GraphController.logger.debug(southEastdata_from_db);
		GraphController.logger.info("South West data from database");
		GraphController.logger.debug(southWestdata_from_db);
		
		eastObj.put("label", "East");
		eastObj.put("backgroundColor",MapConstants.GraphColor_aquamarine1);
		eastObj.put("data",eastdata_from_db);
		direction.add(eastObj);

		westObj.put("label", "West");
		westObj.put("backgroundColor",MapConstants.GraphColor_BlueViolet);
		westObj.put("data", westdata_from_db);
		direction.add(westObj);

		northObj.put("label", "North");
		northObj.put("backgroundColor", MapConstants.GraphColor_thistle2);
		northObj.put("data", northdata_from_db);
		direction.add(northObj);

		southObj.put("label", "South");
		southObj.put("backgroundColor", MapConstants.GraphColor_DarkOliveGreen1);
		southObj.put("data", southdata_from_db);
		direction.add(southObj);

		southEastObj.put("label", "South East");
		southEastObj.put("backgroundColor",MapConstants.GraphColor_violet);
		southEastObj.put("data", southEastdata_from_db);
		direction.add(southEastObj);

		southWestObj.put("label", "South West");
		southWestObj.put("backgroundColor",MapConstants.GraphColor_DeepPink2);
		southWestObj.put("data", southWestdata_from_db);
		direction.add(southWestObj);


		northEastObj.put("label", "North East");
		northEastObj.put("backgroundColor", MapConstants.GraphColor_gold1);
		northEastObj.put("data", northEastdata_from_db);
		direction.add(northEastObj);

		northWestObj.put("label", "North West");
		northWestObj.put("backgroundColor", MapConstants.GraphColor_LightSlateBlue);
		northWestObj.put("data", northWestdata_from_db);
		direction.add(northWestObj);

		
		objectforwavelength.put("display", "true");
		objectforwavelength.put("labelString","NodeId");
		object1forwavelength.put("scaleLabel",objectforwavelength);
		object1forwavelength.put("stacked",true);
		object1forwavelength.put("display", "true");
		xAxesforwavelength.add(object1forwavelength);

		labelobjforwavelength.put("display","true");
		labelobjforwavelength.put("labelString","Number Of Wavelengths");
		finalLabelObjforwavelength.put("scaleLabel",labelobjforwavelength);
		finalLabelObjforwavelength.put("stacked", true);
		finalLabelObjforwavelength.put("display","true");

		yAxesforwavelength.add(finalLabelObjforwavelength);

		finaljsonObjectforwavelength.put("labels", nodeid_for_wavelength);
		finaljsonObjectforwavelength.put("datasets", direction);
		finaljsonObject1forwavelength.put("xAxes", xAxesforwavelength);
		finaljsonObject1forwavelength.put("yAxes", yAxesforwavelength);
		finalObjectforwavelength.put("type","bar" );
		finalObject1forwavelength.put("scales", finaljsonObject1forwavelength);
		titleObjforwavelength.put("display", "true");
		titleObjforwavelength.put("text","Wavelength Population On Network");
		legendObjWavelength.put("display", true);
		finalObject1forwavelength.put("legend", legendObjWavelength);
		finalObject1forwavelength.put("title", titleObjforwavelength);
		finalObjectforwavelength.put("options", finalObject1forwavelength);

		finalObjectforwavelength.put("data",finaljsonObjectforwavelength);


		return finalObjectforwavelength;
	}


	/*@SuppressWarnings("unchecked")
	public JSONObject Traffic_Node(int networkId,DbService dbService) {
		
		String dir;
    	int wvln ;
    	int Linkid;
    	 float traffic;
    	 int nodeid;
    	
    	 JSONArray node_id_for_traffic = new JSONArray();
    	 JSONArray nodeid_traffic_array = new JSONArray();
    	 JSONArray direction_traffic = new JSONArray();
    	 JSONObject TrafficeastObj =new JSONObject();
    	 JSONObject finalObjectfortraffic = new JSONObject();
    	 JSONObject TrafficwestObj =new JSONObject();
    	 JSONObject TrafficsouthObj =new JSONObject();
    	 JSONObject TrafficnorthObj =new JSONObject();
    	 JSONObject TrafficnortheastObj =new JSONObject();
    	 JSONObject TrafficsoutheastObj =new JSONObject();
    	 JSONObject TrafficsouthwestObj =new JSONObject();
    	 JSONObject TrafficnorthwestObj =new JSONObject();
    	 JSONArray Trafficdirection = new JSONArray();
    	 JSONObject finalObject1fortraffic = new JSONObject();
    	 JSONObject objectfortraffic = new JSONObject();
    	 JSONObject object1fortraffic = new JSONObject();
    	 JSONArray xAxesfortraffic = new JSONArray();
    	 JSONArray yAxesfortraffic = new JSONArray();
    	 JSONObject labelobjfortraffic = new JSONObject();
    	 JSONObject finalLabelObjfortraffic = new JSONObject();
    	 JSONObject finaljsonObjectfortraffic = new JSONObject();
    	 JSONObject finaljsonObject1fortraffic = new JSONObject();
    	 JSONObject titleObjfortraffic = new JSONObject();
    	 
    	 
    	 
    	List<Node> node= dbService.getNodeService().FindAll(networkId);
    	
		  for(int i=0;i<node.size();i++){
			  //List<Map<String, Object>> eastTraffic = dbService.getLinkWavelengthInfoService().FindDirectionWiseTraffic(networkId,node.get(i).getNodeId(),MapConstants._EAST);
			  List<Map<String,Object>> lw = dbService.getLinkWavelengthInfoService().FindNodeWiseAllLambdaInfo(networkId, node.get(i).getNodeId());
			  System.out.println("lw"+lw);
			  for (Map<String, Object> lmap : lw) {
				  
				  dir =(String) lmap.get("Direction");
				  wvln = (int)lmap.get("Wavelength");
				   Linkid = (int)lmap.get("linkid");
				  traffic = (float)lmap.get("Traffic");
				  
				  System.out.println("Direction  "+dir);
				  //System.out.println("wavelength "+wvln);
				  //System.out.println("Linkid   "+Linkid);
				  System.out.println("Traffic   "+traffic);
				  nodeid_traffic_array.add(traffic);
				  direction_traffic.add(dir);
			  }
			  for(i=0;i<node.size();i++) {
					int nodeid_traffic = node.get(i).getNodeId();
					node_id_for_traffic.add(nodeid_traffic);
				}
		  }
		  

			TrafficnorthObj.put("label", "north");
			TrafficnorthObj.put("backgroundColor", MapConstants.GraphColor_thistle2);
			TrafficnorthObj.put("data", nodeid_traffic_array);
			Trafficdirection.add(TrafficnorthObj);

			TrafficsouthObj.put("label", "south");
			TrafficsouthObj.put("backgroundColor", MapConstants.GraphColor_DarkOliveGreen1);
			TrafficsouthObj.put("data", nodeid_traffic_array);
			Trafficdirection.add(TrafficsouthObj);

//			TrafficsoutheastObj.put("label", direction_traffic.get(j));
//			TrafficsoutheastObj.put("backgroundColor",MapConstants.GraphColor_violet);
//			TrafficsoutheastObj.put("data", nodeid_traffic_array);
//			Trafficdirection.add(TrafficsoutheastObj);
//j++;
//			TrafficsouthwestObj.put("label", direction_traffic.get(j));
//			TrafficsouthwestObj.put("backgroundColor",MapConstants.GraphColor_DeepPink2);
//			TrafficsouthwestObj.put("data", nodeid_traffic_array);
//			Trafficdirection.add(TrafficsouthwestObj);
//j++;
//
//			TrafficnortheastObj.put("label", direction_traffic.get(j));
//			TrafficnortheastObj.put("backgroundColor", MapConstants.GraphColor_gold1);
//			TrafficnortheastObj.put("data", nodeid_traffic_array);
//			Trafficdirection.add(TrafficnortheastObj);
//j++;
//			TrafficnorthwestObj.put("label", direction_traffic.get(j));
//			TrafficnorthwestObj.put("backgroundColor", MapConstants.GraphColor_LightSlateBlue);
//			TrafficnorthwestObj.put("data", nodeid_traffic_array);
//			Trafficdirection.add(TrafficnorthwestObj);
//		  }
//			
			objectfortraffic.put("display", "true");
			objectfortraffic.put("labelString","NodeId");
			object1fortraffic.put("scaleLabel",objectfortraffic);
			//object1fortraffic.put("stacked",true);
			object1fortraffic.put("display", "true");
			xAxesfortraffic.add(object1fortraffic);

			labelobjfortraffic.put("display","true");
			labelobjfortraffic.put("labelString","Traffic(Gbps)");
			finalLabelObjfortraffic.put("scaleLabel",labelobjfortraffic);
			//finalLabelObjforwavelength.put("stacked", true);
			finalLabelObjfortraffic.put("display","true");

			yAxesfortraffic.add(finalLabelObjfortraffic);

			finaljsonObjectfortraffic.put("labels", node_id_for_traffic);
			finaljsonObjectfortraffic.put("datasets", Trafficdirection);
			finaljsonObject1fortraffic.put("xAxes", xAxesfortraffic);
			finaljsonObject1fortraffic.put("yAxes", yAxesfortraffic);
			finalObjectfortraffic.put("type","bar" );
			finalObject1fortraffic.put("scales", finaljsonObject1fortraffic);
			titleObjfortraffic.put("display", "true");
			titleObjfortraffic.put("text","Node Id v/s Traffic");
			finalObject1fortraffic.put("title", titleObjfortraffic);
			finalObjectfortraffic.put("options", finalObject1fortraffic);

			finalObjectfortraffic.put("data",finaljsonObjectfortraffic);


			return finalObjectfortraffic;
		 // return null;
	}*/



	//Fetching data from Node id and Power from DB//
	@SuppressWarnings("unchecked")

	public JSONObject Power_Node(int networkId,DbService dbService)
	{
		GraphController.logger.info("Node id and Power Consumption Function");
		int i=0;
		JSONArray node_id_for_power = new JSONArray();
		JSONArray power = new JSONArray();
		JSONObject powerConsumptionObj = new JSONObject();
		JSONObject typicalPowerObj = new JSONObject();
		JSONObject objectforPower = new JSONObject();
		JSONObject object1forPower = new JSONObject();
		JSONArray xAxesforPower = new JSONArray();
		JSONArray yAxesforPower = new JSONArray();
		JSONObject finalLabelObjforPower = new JSONObject();
		JSONObject labelobjforPower = new JSONObject();
		JSONObject finaljsonObjectforPower = new JSONObject();
		JSONObject finalObjectforPower = new JSONObject();
		JSONObject finalObject1forPower = new JSONObject();
		JSONObject finaljsonObject1forPower = new JSONObject();
		JSONObject titleObjforPower = new JSONObject();
		JSONObject legendObjforPower =new JSONObject();

		List <Node> node = dbService.getNodeService().FindNodeId(networkId);//Fetching Node Id's from Db of the respective Network ID//
		List<Object> Typicalpower=new ArrayList<Object>();
		List<Object> Totalpower=new ArrayList<Object>();
		

		for(i = 0; i < node.size(); i++){
			//fetching typical power data from the bom table
			List<Map<String, Object>> typicalpower= dbService.getViewServiceRp().FindTotalTypicalPower(networkId,node.get(i).getNodeId());
			//fetching total power data from the bom table
			List<Map<String, Object>> totalpower= dbService.getViewServiceRp().FindTotalPowerConsumption(networkId,node.get(i).getNodeId());
			//creating two lists to store the typical power and total power to be passed to the graph


			//traversing the typical power list to find out the typical power
			for (Map<String, Object> abc : typicalpower) 
			{							  						 								   
				for (Map.Entry<String, Object> entry : abc.entrySet()) 
				{
					//				    	   			nodeid.add(entry.getKey());
					Typicalpower.add(entry.getValue());
					
				}
			}
			GraphController.logger.info("Typicalpower");
			GraphController.logger.debug(Typicalpower);
			//traversing the total power list to find out the total power
			for (Map<String, Object> abc : totalpower) 
			{							  						 								   
				for (Map.Entry<String, Object> entry : abc.entrySet()) 
				{
					//				    	   			nodeid.add(entry.getKey());
					Totalpower.add(entry.getValue());

				}
			}
			GraphController.logger.info("Totalpower");
			GraphController.logger.debug(Totalpower);
			
			for(i=0;i<node.size();i++) {
				int nodeid=node.get(i).getNodeId();
				node_id_for_power.add(nodeid);
			}
		}
		typicalPowerObj.put("label", "Typical Power");
		typicalPowerObj.put("backgroundColor", MapConstants.GraphColor_aquamarine1);
		typicalPowerObj.put("data", Typicalpower);
		power.add(typicalPowerObj);

		powerConsumptionObj.put("label", "Power Consumption");
		powerConsumptionObj.put("backgroundColor", MapConstants.GraphColor_DeepPink2);
		powerConsumptionObj.put("data", Totalpower);
		power.add(powerConsumptionObj);

		objectforPower.put("display", "true");
		objectforPower.put("labelString","Power(Watt)");
		object1forPower.put("scaleLabel",objectforPower);
		object1forPower.put("stacked",true);
		object1forPower.put("display", "true");
		xAxesforPower.add(object1forPower);

		labelobjforPower.put("display","true");
		labelobjforPower.put("labelString","Node Id");
		finalLabelObjforPower.put("scaleLabel",labelobjforPower);
		finalLabelObjforPower.put("stacked", true);
		finalLabelObjforPower.put("display","true");

		yAxesforPower.add(finalLabelObjforPower);

		finaljsonObjectforPower.put("labels", node_id_for_power);
		finaljsonObjectforPower.put("datasets", power);
		finaljsonObject1forPower.put("xAxes", xAxesforPower);
		finaljsonObject1forPower.put("yAxes", yAxesforPower);
		finalObjectforPower.put("type","horizontalBar" );
		finalObject1forPower.put("scales", finaljsonObject1forPower);
		titleObjforPower.put("display", "true");
		titleObjforPower.put("text","Typical Power And Total Power");
		finalObject1forPower.put("title", titleObjforPower);
		legendObjforPower.put("display", true);
		finalObject1forPower.put("legend", legendObjforPower);
		finalObjectforPower.put("options", finalObject1forPower);

		finalObjectforPower.put("data",finaljsonObjectforPower);

		return finalObjectforPower;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject Node_Ip(int networkId, DbService dbService, String blockNodeAdd)
	{
		JSONArray arrayforNodeIp = new JSONArray();
		JSONArray NodeIpv4 = new JSONArray();
		JSONObject NodeIpObj = new JSONObject();
		JSONArray NodeIp_array = new JSONArray();
		JSONObject objectforNodeIp = new JSONObject();
		JSONObject object1forNodeIp = new JSONObject();
		JSONArray xAxesforNodeIp = new JSONArray();
		JSONObject labelobjforNodeIp = new JSONObject();
		JSONArray yAxesforNodeIp = new JSONArray();
		JSONObject finalLabelObjforNodeIp = new JSONObject();
		JSONObject finaljsonObjectforNodeIp = new JSONObject();
		JSONObject finaljsonObject1forNodeIp = new JSONObject();
		JSONObject titleObjforNodeIp = new JSONObject();
		JSONObject finalObject1forNodeIp = new JSONObject();
		JSONObject finalObjectforNodeIp = new JSONObject();
		JSONObject tickObjNodeIp = new JSONObject();
		JSONObject legendObjIp = new JSONObject();
		
		GraphController.logger.info("Blocklinkadd:"+blockNodeAdd);
		int fourthByteCounter =0;
		int thirdByteCounter =0;
		
		int numOfNodes = dbService.getNodeService().Count(networkId);
		
		List<Node> listNode		 = dbService.getNodeService().FindAll(networkId);
		for(Node row : listNode){

			int nodeId	  = (int) row.getNodeId();
			arrayforNodeIp.add(nodeId);
			
			
			if(fourthByteCounter > MapConstants.I_TWOFIVEFIVE){
				fourthByteCounter      = MapConstants.I_ZERO; /**Reset Counter Since One Network Block finished*/
				thirdByteCounter += MapConstants.I_ONE;/**Increment By One to go to Next Block*/
				MainMap.logger.info(" Updated Block Counter : thirdByteCounter :- "+thirdByteCounter+", fourthByteCounter :- "+fourthByteCounter);
			}
			
			String iPv4_String = blockNodeAdd+thirdByteCounter+"."+fourthByteCounter+MapConstants.subnetNodeMask;
			
			MainMap.logger.info("Network Id :- "+ networkId +", Node Id :- "+nodeId + ", iPv4_String :- "+iPv4_String + ", fourthByteCounter :- "+fourthByteCounter);	
			NodeIpv4.add(iPv4_String);
			
			IPv4 ipv4NodeObj = new IPv4(iPv4_String);				
			
			/**IpV4 Lib Call to get available IP in given IP Block*/
			List<Integer> availableNodeIPs = ipv4NodeObj.getAvailableIPs(numOfNodes*MapConstants.IPPERNODE);
			
          
			
			/**Prepare Obj to Insert into DB*/
			IpSchemeNode ipSchemeNodeObj = new IpSchemeNode();
			ipSchemeNodeObj.setLctIp(availableNodeIPs.get(MapConstants.I_TWO));/**DBG => Changed from ONE to TWO*/			
			
			fourthByteCounter+=MapConstants.I_EIGHT;/**Update Counter by Seven to go to the Next Network*/			
		}
		
		NodeIpObj.put("label", "Lct Ip");
		NodeIpObj.put("data", arrayforNodeIp);
		NodeIpObj.put("backgroundColor", MapConstants.GraphColor_violet);

		NodeIp_array.add(NodeIpObj);

		objectforNodeIp.put("display", "true");
		objectforNodeIp.put("labelString", "Lct Ip");
		object1forNodeIp.put("scaleLabel", objectforNodeIp);
		object1forNodeIp.put("display", "true");
		tickObjNodeIp.put("minRotation","10");
		object1forNodeIp.put("ticks",tickObjNodeIp);
		xAxesforNodeIp.add(object1forNodeIp);

		labelobjforNodeIp.put("display", "true");
		labelobjforNodeIp.put("labelString", "Node Id");
		finalLabelObjforNodeIp.put("scaleLabel", labelobjforNodeIp);
		finalLabelObjforNodeIp.put("display", "true");
		yAxesforNodeIp.add(finalLabelObjforNodeIp);

		finaljsonObjectforNodeIp.put("labels",NodeIpv4);
		finaljsonObjectforNodeIp.put("datasets", NodeIp_array);
		finaljsonObject1forNodeIp.put("xAxes", xAxesforNodeIp);
		finaljsonObject1forNodeIp.put("yAxes", yAxesforNodeIp);

		titleObjforNodeIp.put("display", "true");
		titleObjforNodeIp.put("text","Node Id v/s Lct Ip");

		finalObject1forNodeIp.put("scales",finaljsonObject1forNodeIp);
		finalObject1forNodeIp.put("title", titleObjforNodeIp);
		legendObjIp.put("display", true);
		finalObject1forNodeIp.put("legend", legendObjIp);
		finalObjectforNodeIp.put("type", "bar");
		finalObjectforNodeIp.put("data", finaljsonObjectforNodeIp);
		finalObjectforNodeIp.put("options", finalObject1forNodeIp);

		return finalObjectforNodeIp;
	}
	
	//Fetching Data for Link Id and Link Ip//
	
	@SuppressWarnings("unchecked")
	public JSONObject Link_Ip(int networkId, DbService dbService,String blockLinkAdd)
	{
		int i=0;
		JSONArray arrayforIpLinkId = new JSONArray();
		JSONArray LinkIpv4 = new JSONArray();
		JSONObject IpObj = new JSONObject();
		JSONArray LinkIp_array = new JSONArray();
		JSONObject objectforLinkIp = new JSONObject();
		JSONObject object1forLinkIp = new JSONObject();
		JSONArray xAxesforLinkIp = new JSONArray();
		JSONObject labelobjforLinkIp = new JSONObject();
		JSONArray yAxesforLinkIp = new JSONArray();
		JSONObject finalLabelObjforLinkIp = new JSONObject();
		JSONObject finaljsonObjectforLinkIp = new JSONObject();
		JSONObject finaljsonObject1forLinkIp = new JSONObject();
		JSONObject titleObjforLinkIp = new JSONObject();
		JSONObject finalObject1forLinkIp = new JSONObject();
		JSONObject finalObjectforLinkIp = new JSONObject();
		JSONObject tickObjLinkIp = new JSONObject();
		JSONObject legendObjIp = new JSONObject();
		
		GraphController.logger.info("Blocklinkadd:"+blockLinkAdd);
		int fourthByteCounter =0;
		int thirdByteCounter =0;
		
		int numOfLinks = dbService.getLinkService().Count(networkId);
		List<Link> listLink = dbService.getLinkService().FindAll(networkId);
		
		for(Link row : listLink){

			int linkid	  = row.getLinkId();
			arrayforIpLinkId.add(linkid);
			
			if(fourthByteCounter > MapConstants.I_TWOFIVEFIVE){
				fourthByteCounter      = MapConstants.I_ZERO; /**Reset Counter Since One Network Block finished*/
				thirdByteCounter += MapConstants.I_ONE;/**Increment By One to go to Next Block*/
				MainMap.logger.info(" Updated Block Counter : thirdByteCounter :- "+thirdByteCounter+", fourthByteCounter :- "+fourthByteCounter);
			}
			
			String iPv4_String = blockLinkAdd+thirdByteCounter+"."+fourthByteCounter+MapConstants.subnetLinkMask;
			
			MainMap.logger.info("Network Id :- "+ networkId +", Link Id :- "+linkid + ", iPv4_String :- "+iPv4_String + 
					", fourthByteCounter :- "+fourthByteCounter);
			LinkIpv4.add(iPv4_String);
			
			
			IPv4 ipv4LinkObj = new IPv4(iPv4_String);				
			
			/**IpV4 Lib Call to get available IP in given IP Block*/			
			List<Integer> availableLinkIPs = ipv4LinkObj.getAvailableIPs(numOfLinks*MapConstants.IPPERLINK);			
			
            /**IP Pool Print*/						
			for(i=0;i <MapConstants.IPPERLINK; i++ ){
				
				MainMap.logger.info("String :- "+ipv4LinkObj.convertNumericIpToSymbolic(availableLinkIPs.get(i))+
						" => Integer :- " + availableLinkIPs.get(i));				
			}
			
			fourthByteCounter+=MapConstants.I_FOUR;/**Update Counter by Seven to go to the Next Network*/			
		}
		IpObj.put("label", "Link Ip");
		IpObj.put("data", arrayforIpLinkId);
		IpObj.put("backgroundColor", MapConstants.GraphColor_LightSlateBlue);

		LinkIp_array.add(IpObj);

		objectforLinkIp.put("display", "true");
		objectforLinkIp.put("labelString", "Link Ip");
		object1forLinkIp.put("scaleLabel", objectforLinkIp);
		object1forLinkIp.put("display", "true");
		tickObjLinkIp.put("minRotation","10");
		object1forLinkIp.put("ticks",tickObjLinkIp);
		xAxesforLinkIp.add(object1forLinkIp);

		labelobjforLinkIp.put("display", "true");
		labelobjforLinkIp.put("labelString", "Link Id");
		finalLabelObjforLinkIp.put("scaleLabel", labelobjforLinkIp);
		finalLabelObjforLinkIp.put("display", "true");
		yAxesforLinkIp.add(finalLabelObjforLinkIp);

		finaljsonObjectforLinkIp.put("labels",LinkIpv4);
		finaljsonObjectforLinkIp.put("datasets", LinkIp_array);
		finaljsonObject1forLinkIp.put("xAxes", xAxesforLinkIp);
		finaljsonObject1forLinkIp.put("yAxes", yAxesforLinkIp);

		titleObjforLinkIp.put("display", "true");
		titleObjforLinkIp.put("text","Link Id v/s Link Ip");
		legendObjIp.put("display", true);
		finalObject1forLinkIp.put("scales",finaljsonObject1forLinkIp);
		finalObject1forLinkIp.put("title", titleObjforLinkIp);
		finalObject1forLinkIp.put("legend", legendObjIp);
		finalObjectforLinkIp.put("type", "bar");
		finalObjectforLinkIp.put("data", finaljsonObjectforLinkIp);
		finalObjectforLinkIp.put("options", finalObject1forLinkIp);

		return finalObjectforLinkIp;

	}

	//Fetching data for Link Id and Wavelength from DB//

	@SuppressWarnings("unchecked")
	public JSONObject Wavelength_Link(int networkId,DbService dbService)
	{
		GraphController.logger.info("Link id and Wavelength Function");
		int i=0;
		JSONArray arrayforLinkId = new JSONArray();
		JSONArray arrayforWavelength = new JSONArray();
		JSONObject linkidObj = new JSONObject();
		JSONArray arrayforLinkIdWavelength = new JSONArray();
		JSONObject xaxesWavelengthLabel = new JSONObject();
		JSONObject LinkWavelengthxscaleLabel = new JSONObject();
		JSONArray LinkWavelengthxAxes = new JSONArray();
		JSONObject yaxesLinkWavelengthLabel = new JSONObject();
		JSONObject LinkWavelengthyscaleLabel = new JSONObject();
		JSONObject arrayForLinkWavelengthGraph = new JSONObject();
		JSONArray LinkWavelengthyAxes = new JSONArray();
		JSONObject labelForLinkWavelengthGraph = new JSONObject();
		JSONObject LinkWavelengthtitleObj = new JSONObject();
		JSONObject LinkWavelengthscalefinalObject = new JSONObject();
		JSONObject finalLinkWavelengthObject = new JSONObject();
		JSONObject legendObjLinkWavelength = new JSONObject();
		
		
		List <Link> link=dbService.getLinkService().FindLinkId(networkId);
		
		
		for(i=0;i<link.size();i++)
		{

			int linkid=link.get(i).getLinkId();
			arrayforLinkId.add(linkid);

			int wavelength = dbService.getLinkWavelengthInfoService().FindTotalWavelengths(networkId, linkid);
			arrayforWavelength.add(wavelength);
		}
		
		GraphController.logger.info("Wavelengths");
		GraphController.logger.debug(arrayforWavelength);
		
		linkidObj.put("label","Wavelengths");
		linkidObj.put("backgroundColor",MapConstants.GraphColor_VioletRed1);
		linkidObj.put("data",arrayforWavelength);
		arrayforLinkIdWavelength.add(linkidObj);

		xaxesWavelengthLabel.put("display", "true");
		xaxesWavelengthLabel.put("labelString", "Link Id");
		LinkWavelengthxscaleLabel.put("scaleLabel", xaxesWavelengthLabel);
		LinkWavelengthxscaleLabel.put("display", "true");
		LinkWavelengthxAxes.add(LinkWavelengthxscaleLabel);

		yaxesLinkWavelengthLabel.put("display", "true");
		yaxesLinkWavelengthLabel.put("labelString", "Number of Wavelengths");

		LinkWavelengthyscaleLabel.put("scaleLabel", yaxesLinkWavelengthLabel);
		LinkWavelengthyscaleLabel.put("display", "true");
		LinkWavelengthyAxes.add(LinkWavelengthyscaleLabel);

		arrayForLinkWavelengthGraph.put("labels",arrayforLinkId);
		arrayForLinkWavelengthGraph.put("datasets", arrayforLinkIdWavelength);
		labelForLinkWavelengthGraph.put("xAxes", LinkWavelengthxAxes);
		labelForLinkWavelengthGraph.put("yAxes", LinkWavelengthyAxes);

		LinkWavelengthtitleObj.put("display", "true");
		LinkWavelengthtitleObj.put("text","Wavelength Population On Links");
		legendObjLinkWavelength.put("display", true);
		LinkWavelengthscalefinalObject.put("scales",labelForLinkWavelengthGraph);
		LinkWavelengthscalefinalObject.put("title", LinkWavelengthtitleObj);
		LinkWavelengthscalefinalObject.put("legend", legendObjLinkWavelength);
		finalLinkWavelengthObject.put("type", "bar");
		finalLinkWavelengthObject.put("data", arrayForLinkWavelengthGraph);
		finalLinkWavelengthObject.put("options", LinkWavelengthscalefinalObject);

		return finalLinkWavelengthObject;

	}

	//Fetching Link Id and Traffic from DB//
	@SuppressWarnings("unchecked")
	public JSONObject Traffic_Link(int networkId,DbService dbService)
	{
		GraphController.logger.info("Link id and Traffic Function");
		int Wavelength= 0,i=0;
		float LinkIdforTraffic = 0;
		JSONArray LinkId_Traffic = new JSONArray();
		JSONArray Traffic_Link = new JSONArray();
		JSONArray Wavelength_Link_Traffic = new JSONArray();
		JSONObject objectforTraffic = new JSONObject();
		JSONObject object1forTraffic = new JSONObject();
		JSONArray xAxesforTraffic = new JSONArray();
		JSONArray yAxesforTraffic = new JSONArray();
		JSONObject finalLabelObjforTraffic = new JSONObject();
		JSONObject labelobjforTraffic = new JSONObject();
		JSONObject finaljsonObjectforTraffic = new JSONObject();
		JSONObject finalObjectforTraffic = new JSONObject();
		JSONObject finalObject1forTraffic = new JSONObject();
		JSONObject finaljsonObject1forTraffic = new JSONObject();
		JSONObject titleObjforTraffic = new JSONObject();
		JSONArray WavelengthArray_Link_Traffic = new JSONArray();
		JSONObject legendObjLinkTraffic = new JSONObject();
		
		String []color= {"#ffc0cb","#8470ff","#ffd700","#eed2ee","#7fffd4","#ee6363"};
		List<Link> link = dbService.getLinkService().FindAll(networkId);
		

		for(i=0;i<link.size();i++)
		{
			List <LinkWavelengthInfo> linkWvInfo=dbService.getLinkWavelengthInfoService().FindAllByLink(networkId, link.get(i).getLinkId());

			for(i=0;i<linkWvInfo.size();i++)
			{

				Wavelength = linkWvInfo.get(i).getWavelength();
				
				Wavelength_Link_Traffic.add(Wavelength);
				
				LinkIdforTraffic = linkWvInfo.get(i).getTraffic();
				Traffic_Link.add(LinkIdforTraffic);
			}
			GraphController.logger.info("Link_Wavelength");
			GraphController.logger.debug(Wavelength_Link_Traffic);
			GraphController.logger.info("Link_Traffic");
			GraphController.logger.debug(Traffic_Link);
			for(i=0;i<link.size();i++) {
				int linkid=link.get(i).getLinkId();
				LinkId_Traffic.add(linkid);

			}			
			
		}
	

		for(i=0;i<Wavelength_Link_Traffic.size();i++)
		{
			JSONObject Wavelength_Link_TrafficObj = new JSONObject();
			Wavelength_Link_TrafficObj.put("label", Wavelength_Link_Traffic.get(i));
			Wavelength_Link_TrafficObj.put("backgroundColor", color[i%color.length]);
			Wavelength_Link_TrafficObj.put("data", Traffic_Link);
			WavelengthArray_Link_Traffic.add(Wavelength_Link_TrafficObj);
		}

		objectforTraffic.put("display", "true");
		objectforTraffic.put("labelString","Link Id");
		object1forTraffic.put("scaleLabel",objectforTraffic);
		object1forTraffic.put("stacked",true);
		object1forTraffic.put("display", "true");
		xAxesforTraffic.add(object1forTraffic);

		labelobjforTraffic.put("display","true");
		labelobjforTraffic.put("labelString","Traffic(Gbps)");
		finalLabelObjforTraffic.put("scaleLabel",labelobjforTraffic);
		finalLabelObjforTraffic.put("stacked", true);
		finalLabelObjforTraffic.put("display","true");

		yAxesforTraffic.add(finalLabelObjforTraffic);

		finaljsonObjectforTraffic.put("labels", LinkId_Traffic);
		finaljsonObjectforTraffic.put("datasets", WavelengthArray_Link_Traffic);
		finaljsonObject1forTraffic.put("xAxes", xAxesforTraffic);
		finaljsonObject1forTraffic.put("yAxes", yAxesforTraffic);
		finalObjectforTraffic.put("type","bar" );
		legendObjLinkTraffic.put("display", true);
		finalObject1forTraffic.put("legend", legendObjLinkTraffic);
		finalObject1forTraffic.put("scales", finaljsonObject1forTraffic);
		titleObjforTraffic.put("display", "true");
		titleObjforTraffic.put("text","Wavelength Population On Network");
		finalObject1forTraffic.put("title", titleObjforTraffic);
		finalObjectforTraffic.put("options", finalObject1forTraffic);
		
		finalObjectforTraffic.put("data",finaljsonObjectforTraffic);

		return finalObjectforTraffic;

	}


	/**
	 * @brief This Function return Per Node Directionwise wavlength inforamtion
	 *         
	 * @param networkId
	 * @param dbService
	 * @author hp
	 * @date 31st July, 2018
	 * @return
	 */
	
	@SuppressWarnings("unchecked")
	public JSONObject WavelengthTraffic_Node(int networkId,DbService dbService)
	{
		
		GraphController.logger.info("Node id and WavelengthTraffic Function");

		JSONObject finalObjectToReturn = new JSONObject();
		JSONObject dataObject = new JSONObject();
		JSONArray labelsArray = new JSONArray();
		JSONArray datasetsArray = new JSONArray();
		JSONObject optionsobject  = new JSONObject();
		
		



		


		List <Node> node = dbService.getNodeService().FindNodeId(networkId);

		List<CardInfo>  cardInfoListObj= new ArrayList<CardInfo>();

		/** JSONOBJECT -> data -> labels */
		for(int i=0;i<node.size();i++)
		{	
			labelsArray.add("Node " + node.get(i).getNodeId());

			cardInfoListObj.addAll(dbService.getCardInfoService().
										FindWorkingMpnsDirectionForGraphs(networkId, node.get(i).getNodeId())); /** Colllect list for all the nodes */
		}
		
		GraphController.logger.debug("All  cardInfoListObj : "+cardInfoListObj);
		
		try {
			List<Integer> workingLambdaList =   dbService.getNetworkRouteService().FindDistinctLambda(networkId, MapConstants.I_ONE);	

		GraphController.logger.debug(" workingLambdaList : "+ workingLambdaList);


		workingLambdaList.forEach( lambda -> { /**for each unique lambda value */

			GraphController.logger.debug(" lambda : "+ lambda);
			
			
			for(int direction=MapConstants.I_ONE; direction<=MapConstants.I_EIGHT; direction++) {/** lambda per direction*/
				
				String directionVal = MapConstants.fGetDirectionStr(direction);
				GraphController.logger.debug(" directionVal "+ directionVal);

				List<CardInfo> directionWiseCardInfoListObj = cardInfoListObj.stream().				
																	filter(rowElement -> rowElement.getDirection().equalsIgnoreCase(directionVal)
																		                && rowElement.getWavelength() == lambda
																	     ) 
																	.collect(Collectors.toList());
			
				GraphController.logger.debug(" directionWiseCardInfoListObj : "+directionWiseCardInfoListObj );

				if(!directionWiseCardInfoListObj.isEmpty()){


					JSONObject datasetsInsertObject = new JSONObject();					
					datasetsInsertObject.put("label", directionVal+"-"+lambda); 	/** JSONOBJECT -> data -> datasets -> label */
					datasetsInsertObject.put("backgroundColor", MapConstants.GraphColor_array[(int)(Math.random() *MapConstants.GraphColor_array.length)]); 	/** JSONOBJECT -> data -> datasets -> backgroundColor */


					JSONArray datasetsData   = new JSONArray(); 
					float [] dataIntArrayForJson = new float[node.size()];


					directionWiseCardInfoListObj.forEach(cardInfoObject -> {
//						System.out.println(Integer.parseInt(cardInfoObject.getNodeKey().split("_")[1]));
						System.out.println(cardInfoObject.getNodeId());
						System.out.println(cardInfoObject.getWavelength());
						NetworkRoute networkRouteObj =   dbService.getNetworkRouteService().FindAllByDemandId(networkId, cardInfoObject.getDemandId(),MapConstants.I_ONE);
						dataIntArrayForJson[cardInfoObject.getNodeId()-1] = networkRouteObj.getTraffic();
						System.out.println("dataIntArrayForJson "+Arrays.toString(dataIntArrayForJson));
					});
					
					for(float dataInt : dataIntArrayForJson){
						datasetsData.add(dataInt);
					}
					
					System.out.println("datasetsData "+ datasetsData);

					datasetsInsertObject.put("stack", direction);					
					datasetsInsertObject.put("data", datasetsData);
					datasetsArray.add(datasetsInsertObject);					

					System.out.println("datasetsArray : "+datasetsArray);
					
				}


			}

		});


	
		/**Option Object Creation */			
			optionsobject.put("responsive",true);
			
			JSONObject legendobject   = new JSONObject();
			legendobject.put("display",true);
			optionsobject.put("legend", legendobject);
			
			JSONObject titleobject   = new JSONObject();
			titleobject.put("display",true);
			titleobject.put("text" ,"Nodewise Directionwise Wavelength and Traffic Chart");

			
			JSONObject tooltipobject   = new JSONObject();
			tooltipobject.put("mode","single");
			tooltipobject.put("intersect",true);
			optionsobject.put("tooltip",tooltipobject);
			
			JSONObject hoverobject   = new JSONObject();
			hoverobject.put("mode", "nearest");
			hoverobject.put("intersect", true);

			
				
			/**for x axis*/			
			JSONObject scalesobject   = new JSONObject();
			JSONArray  xAxisArray =new JSONArray();
			JSONObject xAxisobject   = new JSONObject();
			xAxisobject.put("stacked", true);
			xAxisobject.put("display", true);
			JSONObject scaleLabel1object   = new JSONObject();
			scaleLabel1object.put("display", true);
			scaleLabel1object.put("labelString" , "Direction/ Nodeid");
			
		


			xAxisobject.put("scaleLabel",scaleLabel1object );
			xAxisArray.add( xAxisobject);
			scalesobject.put("xAxis", xAxisArray);
		

				
			/**for y axis*/			
			JSONArray  yAxisArray =new JSONArray();
			JSONObject yAxisobject   = new JSONObject();
			yAxisobject.put("display", true);
			yAxisobject.put("stacked", true);
		
			JSONObject scaleLabelobject   = new JSONObject();
			scaleLabelobject.put("display", true);
			scaleLabelobject.put("labelString", "Total traffic(g)");		
		
			/** final*/
			yAxisobject.put("scaleLabel",scaleLabelobject);
			yAxisArray.add( yAxisobject);
			scalesobject.put("yAxis", yAxisArray);
			

			optionsobject.put("scales", scalesobject );
			optionsobject.put("title",titleobject);
			
			
			
				
			


		
			
			dataObject.put("labels", labelsArray);
			dataObject.put("datasets", datasetsArray);

			finalObjectToReturn.put("type", "bar");
			finalObjectToReturn.put("data", dataObject);
			finalObjectToReturn.put("options",	 optionsobject);				
			
			GraphController.logger.debug(" WavelengthTraffic_Node => finalObjectToReturn : "+finalObjectToReturn);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return finalObjectToReturn;
	}
	


}
