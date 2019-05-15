package application.controller;
import application.controller.ControlPath;
import application.controller.DataPath;
import application.dao.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.Date;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import application.MainMap;
import application.constants.MapConstants;
import application.constants.ResourcePlanConstants;
import application.model.*;
import application.service.*;

/**
 * @author hp
 * 
 * @brief This is a Main Web Controller for any Map related requests from NPT UI
 */
@Controller
@RequestMapping("/")
public class MapWebMainController {

	@Autowired
	private DbService dbService;

	/*
	 * @Autowired private TopologyProcessing topologyProcessing;
	 * 
	 * @Autowired private CircuitMatrixService circuitMatrixService;
	 */

	/**
	 * Graph Controller
	 * 
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/graphJsonFetch", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
	public @ResponseBody JSONObject jsonFetchFromGraphController(@RequestBody String data) {

		GraphController graphControllerObj = new GraphController();
		return graphControllerObj.GenerateReportModal(data, dbService);

	}

	
	/**
	 * @brief This Mapping called on Network Save from UI
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 * @throws SQLException
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
	public @ResponseBody int saveMap(@RequestBody String jsonStr) throws JSONException, SQLException {

		System.out.println("**************************** Map Request Received : *******************************");

		/* Calling Parser API */

		MapWebJsonParser mapWebJsonParser = new MapWebJsonParser();
		ResourcePlanning rp = new ResourcePlanning();

		int parsingResult = mapWebJsonParser.jsonParse(jsonStr, dbService);

		/** Map From Common API */
		HashMap<String, Object> networkInfoMap = MapWebCommonAPIs.NestedStaticClass.getNetworkInformation(dbService,
				jsonStr);
		int networkId = (int) networkInfoMap
				.get(MapConstants.NetworkId); /** Network Id for common use */
		EquipmentPreferences eqPref = new EquipmentPreferences(dbService);

		// org.json.JSONObject jsonMapDataObj = new
		// org.json.JSONObject(jsonStr);
		// org.json.JSONObject
		// networkInfoObj=(org.json.JSONObject)jsonMapDataObj.get("NetworkInfo");

		/**** initialize equipment preference on save map ****/
		if (dbService.getEquipmentPreferenceService().Count(networkId) == 0) {
			System.out.println("Case when equipment preference is not initiliazed");
			eqPref.InitializeEqPreferenceDb(networkId);
		} else {
			System.out.println("Case when equipment preference is already initiliazed");
			Set<Integer> currNodesSet = new HashSet<Integer>();

			// Get Current Nodes in network and add to currNodeSet
			List<Node> currentNodes = dbService.getNodeService().FindAll(networkId);
			for (Node n : currentNodes) {
				currNodesSet.add(n.getNodeId());
			}

			// Preference Initialized nodes
			List<Map<String, Object>> alreadyInitializedNodes = dbService.getEquipmentPreferenceService()
					.InitializedNodes(networkId);
			// System.out.println("alreadyInitializedNodes:"+alreadyInitializedNodes.size());

			// Just keep non initialized nodes in currnodesSet , remove already
			// initialized ones
			for (int i = 0; i < alreadyInitializedNodes.size(); i++) {
				// System.out.println("nodeObj
				// :"+Integer.parseInt(alreadyInitializedNodes.get(i).get("NodeId").toString()));
				int nodeid = Integer.parseInt(alreadyInitializedNodes.get(i).get("NodeId").toString());
				currNodesSet.remove(nodeid);
			}

			// System.out.println("currNodesSet:"+currNodesSet.size());
			for (int nodeid : currNodesSet) {
				// System.out.println("Initializing eq pref for Node:"+nodeid);
				Node nodeInfo = dbService.getNodeService().FindNode(networkId, nodeid);
				eqPref.fInsertEquipmentPreferencesDb(networkId, nodeid, nodeInfo.getNodeType());
			}
		}

		/** Finally Delete the redundant circuits */
		dbService.getCircuitService().DeleteRedundantCircuit(networkId);

		System.out.println("**************************** Map Request : Processed *******************************");

		return parsingResult;
	}

	/**
	 * @brief This Mapping called on Network Save from UI
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 * @throws SQLException
	 */
	@RequestMapping(value = "/createNetwork", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
	public @ResponseBody int createNetwork(@RequestBody String jsonStr) throws JSONException, SQLException {

		System.out.println(
				"**************************** Network Create Request Received : *******************************");

		/* Calling Parser API */
		System.out.println(jsonStr);
		org.json.JSONObject networkInfoObj = new org.json.JSONObject(jsonStr);
		Network networkModel = new Network();
		networkModel.setNetworkName(networkInfoObj.getString("NetworkName")/* "TestNetwork" */);
		networkModel.setTopology(networkInfoObj.getString("Topology"));
		networkModel.setUserName(networkInfoObj.getString("UserName"));
		// networkModel.setNetworkName(networkInfoObj.getString("NetworkName"));

		UUID uuid = UUID.randomUUID();
		String randomUUIDString = uuid.toString();

		System.out.println("Random UUID String = " + randomUUIDString);
		System.out.println("UUID version       = " + uuid.version());
		System.out.println("UUID variant       = " + uuid.variant());

		networkModel.setSubNetworkId(randomUUIDString);

		// Debug
		if (dbService.getNetworkService().GetByNetworkName(/* "TestNetwork" */networkModel.getNetworkName()) != null) {
			MainMap.logger.info("/createNetwork :: Going to Delete Existing network in order to update it .."
					+ dbService.getNetworkService().GetByNetworkName(networkModel.getNetworkName()).toString());
			// dbService.getNetworkService().DeleteWholeNetwork(Integer.parseInt(dbService.getNetworkService().GetByNetworkName(/*"TestNetwork"*/networkModel.getNetworkName()).toString()));/**DBG=>
			// Temp Entry*/
			return MapConstants.FAILURE;
		}

		dbService.getNetworkService().InsertNetwork(networkModel); // Debug

		// if(dbService.getNetworkService().NetworkNameExists(networkModel.getNetworkName(),
		// networkModel.getUserName())==null)
		// dbService.getNetworkService().InsertNetwork(networkModel);
		// else return MapConstants.FAILURE;

		// Debug
		UserList user = new UserList();
		user.setUsername("admin");
		user.setPassword("admin");
		user.setPrivilege("owner");
		if (dbService.getUserListService().CheckIfUserExists(user.getUsername(), user.getPassword()) == false)
			dbService.getUserListService().Insert(user);

		System.out.println(
				"**************************** Network Create Request : Processed *******************************");

		return MapConstants.SUCCESS;
	}

	/**
	 * @brief This Mapping called on login from UI
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 * @throws SQLException
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
	public @ResponseBody int loginNpt(@RequestBody String jsonStr) throws JSONException, SQLException {

		System.out.println("**************************** Map Request Received : *******************************");

		/* Calling User Match API */

		System.out.println("jsonStr ::" + jsonStr);
		org.json.JSONObject userInfoJsonObj = new org.json.JSONObject(jsonStr);

		String username = userInfoJsonObj.get("username").toString();
		String password = userInfoJsonObj.get("password").toString();
		boolean isExistingUser = dbService.getUserListService().CheckIfUserExists(username, password);

		if (isExistingUser)
			return MapConstants.SUCCESS;
		else
			return MapConstants.FAILURE;
	}

	/**
	 * @brief This Mapping called on login from UI
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 * @throws SQLException
	 */
	@RequestMapping(value = "/registerUser", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
	public @ResponseBody int registerUser(@RequestBody String jsonStr) throws JSONException, SQLException {

		System.out.println("**************************** Map Request Received : *******************************");

		/* Calling User Match API */

		System.out.println("registerUser jsonStr ::" + jsonStr);
		org.json.JSONObject userInfoJsonObj = new org.json.JSONObject(jsonStr);

		String username = userInfoJsonObj.get("username").toString();
		String password = userInfoJsonObj.get("password").toString();
		// String email=userInfoJsonObj.get("email").toString();

		UserList user = new UserList();
		user.setPassword(password);
		user.setUsername(username);
		user.setPrivilege("member");// debug
		if (!dbService.getUserListService().CheckIfUserExists(username)) {
			dbService.getUserListService().Insert(user);
			return MapConstants.SUCCESS;
		} else {
			return MapConstants.DUPLICATE_ENTRY;
		}

	}

	/**
	 * @brief This Mapping called on login from UI
	 * @param jsonStr	
	 * @return
	 * @throws JSONException
	 * @throws SQLException
	 */
	@RequestMapping(value = "/userNetworks", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
	public @ResponseBody JSONObject userNetworks(@RequestBody String jsonStr) throws JSONException, SQLException {

		System.out.println("**************************** Map Request Received : *******************************");

		/* Calling User Match API */

		System.out.println("jsonStr ::" + jsonStr);
		org.json.JSONObject userInfoJsonObj = new org.json.JSONObject(jsonStr);
		JSONArray finalReturnArray = new JSONArray();
		JSONObject FinalObject = new JSONObject();
		
		String username = userInfoJsonObj.get("username").toString();
		// String password=userInfoJsonObj.get("password").toString();
		// boolean
		// isExistingUser=dbService.getUserListService().CheckIfUserExists(username,
		// password);
		// if(isExistingUser)
		// {
		System.out.println("userNetworks for User ::" + username);

		List<Network> userNetworks = dbService.getNetworkService().FindNetworkByUserName(username);
		for (int i = 0; i < userNetworks.size(); i++) {
			JSONObject networkObj = new JSONObject();
			networkObj.put("NetworkName", userNetworks.get(i).getNetworkName());
			networkObj.put("BrownFieldId", userNetworks.get(i).getNetworkIdBrownField());
			networkObj.put("NetworkId", userNetworks.get(i).getNetworkId());
			networkObj.put("Update Date", userNetworks.get(i).getNetworkUpdateDate());
			networkObj.put("UserName", userNetworks.get(i).getUserName());
			networkObj.put("Topology", userNetworks.get(i).getTopology());
			finalReturnArray.add(networkObj);
		}
		// }

		/**
		 * Going to Execute MySqlThread
		 */
		
		
		FinalObject.put("usernetworks", finalReturnArray);
		
		NewDashboardController Graph = new NewDashboardController();
		 
		FinalObject.put("dashboard_Graphs", Graph.GenerateGraphModal(jsonStr, dbService));
		
		
		System.out.println("FinalObject : "+ FinalObject);

		return FinalObject;

	}

	/**
	 * @brief This Mapping called for fetching NetworkState from Db
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 * @throws SQLException
	 */
	@RequestMapping(value = "/NetworkState", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
	public @ResponseBody JSONObject networkState(@RequestBody String jsonStr) throws JSONException, SQLException {

		System.out.println(
				"**************************** NetworkState Request Received : *******************************");

		JSONObject returnObj = new JSONObject();

		/** Map From Common API */
		HashMap<String, Object> networkInfoMap = MapWebCommonAPIs.NestedStaticClass.getNetworkInformation(dbService,
				jsonStr);
		int networkId = (int) networkInfoMap
				.get(MapConstants.NetworkId); /** Network Id for common use */
		JSONObject taskCompletedObj = new JSONObject();

		int mapCount = dbService.getNodeService().Count(networkId);
		int circuitCount = dbService.getCircuitService().Count(networkId);
		int networkRouteCount = dbService.getNetworkRouteService().Count(networkId);
		int inventoryDataCount = dbService.getInventoryService().Count(networkId);

		taskCompletedObj.put("isMapSaved", mapCount > 0 ? 1 : 0);
		taskCompletedObj.put("isCircuitSaved", circuitCount > 0 ? 1 : 0);
		taskCompletedObj.put("isRwaExecuted", networkRouteCount > 0 ? 1 : 0);
		taskCompletedObj.put("isInventoryGenerated", inventoryDataCount > 0 ? 1 : 0);

		returnObj.put("NetworkFlowStatus", taskCompletedObj);

		if (((String) networkInfoMap.get(MapConstants.NetworkType)).equalsIgnoreCase(MapConstants.BrownField))
			returnObj.put("state", "BrownField");
		else
			returnObj.put("state", "GreenField");

		return returnObj;

	}

	/**
	 * @brief This Mapping called for instantiating Brown Field Network
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 * @throws SQLException
	 */
	@RequestMapping(value = "/instantiateBrownField", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
	public @ResponseBody JSONObject instantiateBrownField(@RequestBody String jsonStr)
			throws JSONException, SQLException {

		System.out.println(
				"**************************** NetworkState Request Received : *******************************");
		JSONObject returnObj = new JSONObject();

		/** Map From Common API */
		HashMap<String, Object> networkInfoMap = MapWebCommonAPIs.NestedStaticClass.getNetworkInformation(dbService,
				jsonStr);
		int networkId = (int) networkInfoMap
				.get(MapConstants.NetworkId); /** Network Id for common use */

		if (((String) networkInfoMap.get(MapConstants.NetworkType)).equalsIgnoreCase(MapConstants.BrownField)) {
			int networkidBrField = (int) networkInfoMap.get(
					MapConstants.NetworkId); /** Network Id for common use */
			int networkidGrField = (int) networkInfoMap.get(
					MapConstants.GreenFieldId); /** Network Id for common use */
			
			MapWebCommonAPIs.copyDataBetweenNetworks(networkidGrField, networkidBrField, dbService);
		}

		JSONObject taskCompletedObj = new JSONObject();

		int mapCount = dbService.getNodeService().Count(networkId);
		int circuitCount = dbService.getCircuitService().Count(networkId);
		int networkRouteCount = dbService.getNetworkRouteService().Count(networkId);
		int inventoryDataCount = dbService.getInventoryService().Count(networkId);
		int ipSchemeCount = dbService.getIpSchemeNodeService().Count(networkId);

		taskCompletedObj.put("isMapSaved", mapCount > 0 ? true : false);
		taskCompletedObj.put("isCircuitSaved", circuitCount > 0 ? true : false);
		taskCompletedObj.put("isRwaExecuted", networkRouteCount > 0 ? true : false);
		taskCompletedObj.put("isInventoryGenerated", inventoryDataCount > 0 ? true : false);
		taskCompletedObj.put("isIpSchemeGenerated", ipSchemeCount > 0 ? true : false);

		returnObj.put("NetworkFlowStatus", taskCompletedObj);

		if (((String) networkInfoMap.get(MapConstants.NetworkType)).equalsIgnoreCase(MapConstants.BrownField))
			returnObj.put("state", "BrownField");
		else
			returnObj.put("state", "GreenField");
		
		//Update Schema
		this.updateSqlTable();

		return returnObj;

		// return MapConstants.SUCCESS;

	}

	/**
	 * @brief This Mapping called for restoring a BF network to GF Network
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 * @throws SQLException
	 */
	@RequestMapping(value = "/restoreGreenField", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
	public @ResponseBody int restoreGreenField(@RequestBody String jsonStr) throws JSONException, SQLException {

		System.out.println(
				"**************************** NetworkState Request Received : *******************************");

		int result = MapConstants.SUCCESS;
		
		
		/** Map From Common API */
		HashMap<String, Object> networkInfoMap = MapWebCommonAPIs.NestedStaticClass.getNetworkInformation(dbService,
				jsonStr);


		String networkName = networkInfoMap.get(MapConstants.NetworkName).toString();
		String userName = networkInfoMap.get(MapConstants.UserName).toString()/* "admin" */;
		
		int networkId = (int) networkInfoMap.get(MapConstants.NetworkId); /** Network Id for common use */
		Timestamp updateTime = new Timestamp(System.currentTimeMillis());
		
		int greenFieldNetworkId = Integer.parseInt(networkInfoMap.get(MapConstants.GreenFieldId).toString());
		// System.out.println("Green Field Id in restoration Case: "+networkGfId);
		if(dbService.getNetworkService().IsBrownFieldNetwork(greenFieldNetworkId))
			dbService.getNetworkService().Update(greenFieldNetworkId,MapConstants.BrownFieldBfId,updateTime);
		else dbService.getNetworkService().Update(greenFieldNetworkId,MapConstants.GreenFieldBfId,updateTime);

		dbService.getNetworkService().DeleteWholeNetwork(networkId);/** DBG=> Temp Entry */
		dbService.getCardInfoService().DeleteAllCardInfo(networkId);
		dbService.getPatchCordService().DeletePatchCordInfo(networkId);
		dbService.getPortInfoService().DeleteAllPortInfo(networkId);
		
//		result = covertToBrownField(jsonStr);
		return result;

	}

	/**
	 * @brief This Mapping called on Network Save from UI
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 * @throws SQLException
	 */
	@RequestMapping(value = "/openNetwork", method = RequestMethod.POST, headers = "Accept=application/json",
			consumes = "application/json", produces = "application/json")
	public @ResponseBody JSONObject openNetwork(@RequestBody String jsonStr) throws JSONException, SQLException {
		System.out.println("jsonstr:"+jsonStr);
		System.out.println("**************************** Network Open Request Received : *******************************");

		/* Calling Parser API */
		String jsonMapStr = null;

		org.json.JSONObject networkInfoObj = new org.json.JSONObject(jsonStr);
		String networkName = networkInfoObj.getString("NetworkName"), userName = networkInfoObj.getString("UserName");

		int networkId = Integer.parseInt(dbService.getNetworkService().GetByNetworkName(networkName).toString());

		System.out.println("Open network ----- Map name  :"+networkName+" and Network Id :"+networkId);
		
		MapData mapData=dbService.getMapDataService().FindByNetworkId(networkId);
		jsonMapStr=mapData.getMap().toString();
		
		System.out.println("**************************** Network Open Request : Processed *******************************");

		JSONObject openNetworkReturnObj=new JSONObject();
		
		//null takes the control to error in ajax , Null is kept as string therefore to return
		if(jsonMapStr.equals(""))
		{
			//System.out.println("jsonMapStr is :"+jsonMapStr);
			openNetworkReturnObj.put("MapData", MapConstants.MapDataNull);
			int Circuits=dbService.getCircuitService().Count(networkId);
			openNetworkReturnObj.put("Circuits", Circuits);
			int Demands=dbService.getDemandService().Count(networkId);			
			openNetworkReturnObj.put("Demands", Demands);
			int Traffic=dbService.getDemandService().GetTotalCapacityForNetwork(networkId);
			openNetworkReturnObj.put("Traffic", Traffic);
//			return 	MapConstants.MapDataNull;
		}
		else {

			//System.out.println("jsonMapStr is :"+jsonMapStr);
			openNetworkReturnObj.put("MapData", jsonMapStr);
			int Circuits=dbService.getCircuitService().Count(networkId);
			openNetworkReturnObj.put("Circuits", Circuits);
			int Demands=dbService.getDemandService().Count(networkId);			
			openNetworkReturnObj.put("Demands", Demands);
			int Traffic=dbService.getDemandService().GetTotalCapacityForNetwork(networkId);
			openNetworkReturnObj.put("Traffic", Traffic);

//			return jsonMapStr;
		}
		return openNetworkReturnObj;
	}

	/**
	 * @brief This Mapping called to make a clone of the network
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 * @throws SQLException
	 */
	@RequestMapping(value = "/cloneNetwork", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
	public @ResponseBody int cloneNetwork(@RequestBody String jsonStr) throws JSONException, SQLException {
		System.out.println("jsonstr:" + jsonStr);
		System.out.println(
				"**************************** Clone Network Request Received : *******************************");

		org.json.JSONObject networkInfoObj = new org.json.JSONObject(jsonStr);
		String networkName = networkInfoObj.getString("NetworkName"), userName = networkInfoObj.getString("UserName");

		int networkId = Integer.parseInt(dbService.getNetworkService().GetByNetworkName(networkName).toString());

		System.out.println("got the map name  :" + networkName + " and network id :" + networkId);

		Network currNetwork = dbService.getNetworkService().GetNetworkInfoByNetworkName(networkName);
		String clonedNetworkName = currNetwork.getNetworkName() + "_Clone_"
				+ dbService.getNetworkService().GetNextNetworkId();
		currNetwork.setNetworkName(clonedNetworkName);
		currNetwork.setNetworkIdBrownField(0);

		// Insert Network Data
		dbService.getNetworkService().InsertNetwork(currNetwork);

		int clonedNetworkId = Integer
				.parseInt(dbService.getNetworkService().GetByNetworkName(clonedNetworkName).toString());

		// Insert Mapdata
		MapData currNetworkMap = dbService.getMapDataService().FindByNetworkId(networkId);
		currNetworkMap.setNetworkId(clonedNetworkId);
		dbService.getMapDataService().Delete(clonedNetworkId);
		dbService.getMapDataService().Insert(currNetworkMap);
		
		MapWebCommonAPIs.copyDataBetweenNetworks(networkId, clonedNetworkId, dbService);

		// Insert Circuit
//		dbService.getCircuitService().CopyCircuitDataInBrField(networkId, clonedNetworkId);

		System.out.println(
				"**************************** Clone Network Request : Processed *******************************");

		return MapConstants.SUCCESS;
	}

	/**
	 * @brief This Mapping called on Network Save from UI
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 * @throws SQLException
	 */
	@RequestMapping(value = "/deleteNetwork", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
	public @ResponseBody int deleteNetwork(@RequestBody String jsonStr) throws JSONException, SQLException {

		System.out.println(
				"**************************** Network Delete Request Received : *******************************");

		/** Map From Common API */
		HashMap<String, Object> networkInfoMap = MapWebCommonAPIs.NestedStaticClass.getNetworkInformation(dbService,
				jsonStr);

		int networkId = (int) networkInfoMap
				.get(MapConstants.NetworkId); /** Network Id for common use */
		int greenFieldNetworkId = MapConstants.I_ZERO;

		if (((String) networkInfoMap.get(MapConstants.NetworkType)).equalsIgnoreCase(MapConstants.BrownField)) {
			greenFieldNetworkId = (int) networkInfoMap.get(MapConstants.GreenFieldId); /** Network Id for common use */
		}

//		List<Node> nodes = dbService.getNodeService().FindAllNonILAsNetworkId(networkId);
		
		System.out.println("Delete network id :" + networkId);
		dbService.getNetworkService().DeleteWholeNetwork(networkId);
		dbService.getCardInfoService().DeleteAllCardInfo(networkId);
		dbService.getPatchCordService().DeletePatchCordInfo(networkId);
		dbService.getPortInfoService().DeleteAllPortInfo(networkId);
		Timestamp updateTime = new Timestamp(System.currentTimeMillis());
		
		if (((String) networkInfoMap.get(MapConstants.NetworkType)).equalsIgnoreCase(MapConstants.BrownField)) {
//			Network gfNetwork = dbService.getNetworkService().GetNetworkInfoByBFNetworkId(greenFieldNetworkId);
			System.out.println("greenFieldNetworkId"+greenFieldNetworkId);
			if(dbService.getNetworkService().IsBrownFieldNetwork(greenFieldNetworkId))
				dbService.getNetworkService().Update(greenFieldNetworkId,MapConstants.BrownFieldBfId,updateTime);
			else dbService.getNetworkService().Update(greenFieldNetworkId,MapConstants.GreenFieldBfId,updateTime);

		}	

		return MapConstants.SUCCESS;
	}

	/**
	 * @brief This Mapping called for Converting gf to Bf as well as Restoring a
	 *        Bf to Gf
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 * @throws SQLException
	 */
	@RequestMapping(value = "/covertToBrownField", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
	public @ResponseBody int covertToBrownField(@RequestBody String jsonStr) throws JSONException, SQLException {

		System.out.println(
				"**************************** BF Network Conversion Request Received : *******************************");


		/** Map From Common API */
		HashMap<String, Object> networkInfoMap = MapWebCommonAPIs.NestedStaticClass.getNetworkInformation(dbService,
				jsonStr);


		String networkName = networkInfoMap.get(MapConstants.NetworkName).toString();
		String userName = networkInfoMap.get(MapConstants.UserName).toString()/* "admin" */;


		if (dbService.getNetworkService().GetByNetworkName(networkName) != null) {
			MainMap.logger.info(" Fetch network data for NetworkId ::"+ dbService.getNetworkService().GetByNetworkName(networkName).toString());

			Network network = dbService.getNetworkService().GetNetworkInfoByNetworkName(networkName);
			int networkId = network.getNetworkId();
			int networkBfId = network.getNetworkIdBrownField();
			String topology = network.getTopology().toString();
			Timestamp updateTime = new Timestamp(System.currentTimeMillis());

			// If it has a brownField already , then delete it
//			if (networkBfId != MapConstants.GreenFieldBfId && networkBfId != MapConstants.BrownFieldBfId) {
//				// MainMap.logger.info("/createBFNetwork :: Going to Delete
//				// Existing network in order to create new BF .."+
//				// dbService.getNetworkService().GetByNetworkName("TestNetwork").toString());
//				dbService.getNetworkService().DeleteWholeNetwork(networkBfId);/** DBG=> Temp Entry */
//				dbService.getCardInfoService().DeleteAllCardInfo(networkBfId);
//				dbService.getPatchCordService().DeletePatchCordInfo(networkBfId);
//				dbService.getPortInfoService().DeleteAllPortInfo(networkBfId);
//				dbService.getNetworkService().Update(networkId, 0, updateTime);
//
//				Network networkAfterBFDeletion = dbService.getNetworkService().GetNetworkInfoByNetworkName(networkName);
//				networkBfId = networkAfterBFDeletion.getNetworkIdBrownField();
//			}

			// Green field case when there is no brown field yet :: (BfId==0)
//			if (networkBfId == MapConstants.GreenFieldBfId) {
//				// Get new BF network id
//				int newBfNetworkId = Integer.parseInt(dbService.getNetworkService().GetNextNetworkId().toString());
//
//				// Update the existing network with the BF id
//				dbService.getNetworkService().Update(networkId, newBfNetworkId, updateTime);
//
//				// Create a new BF network for above network
//				Network networkBf = new Network();
//				networkBf.setTopology(topology);
////				networkBf.setNetworkName((network.getNetworkName() + "_Brown-Field"));
//				networkBf.setNetworkName((network.getNetworkName()));
//				networkBf.setUserName(userName);
//				networkBf.setNetworkIdBrownField(MapConstants.BrownFieldBfId);
//				networkBf.setSubNetworkId(network.getSubNetworkId());
//				// System.out.println("Inserting new network BF
//				// :"+networkBf.getNetworkName());
//				dbService.getNetworkService().InsertNetwork(networkBf);
//
//				// Update MapData with brownFieldId - replacing the greenField value
//				
////				MapData mapData = dbService.getMapDataService().FindByNetworkId(networkId);
////				mapData.setNetworkId(newBfNetworkId);
////				dbService.getMapDataService().Update(mapData);
//			}
//			// Green Field Network restore case ; Frontend check for gf to gf
//			// restore ;
//			// Delete this bf network ; Update BFid to 0 in gf network of this
//			// bf
//			else if (networkBfId == MapConstants.BrownFieldBfId) {
//				int networkGfId = Integer.parseInt(networkInfoMap.get(MapConstants.GreenFieldId).toString());
//				// System.out.println("Green Field Id in restoration Case:
//				// "+networkGfId);
//				dbService.getNetworkService().Update(networkGfId, 0, updateTime);
//
//				dbService.getNetworkService().DeleteWholeNetwork(networkId);/** DBG=> Temp Entry */
//				dbService.getCardInfoService().DeleteAllCardInfo(networkId);
//				dbService.getPatchCordService().DeletePatchCordInfo(networkId);
//				dbService.getPortInfoService().DeleteAllPortInfo(networkId);
//			}
			
			
			// Get new BF network id
			int newBfNetworkId = Integer.parseInt(dbService.getNetworkService().GetNextNetworkId().toString());

			// Update the existing network with the BF id
			dbService.getNetworkService().Update(networkId, newBfNetworkId, updateTime);

			// Create a new BF network for above network
			Network networkBf = new Network();
			networkBf.setTopology(topology);
//			networkBf.setNetworkName((network.getNetworkName() + "_Brown-Field"));
			networkBf.setNetworkName((network.getNetworkName()));
			networkBf.setUserName(userName);
			networkBf.setNetworkIdBrownField(MapConstants.BrownFieldBfId);
			networkBf.setSubNetworkId(network.getSubNetworkId());
			dbService.getNetworkService().InsertNetwork(networkBf);
			
			// Update MapData with brownFieldId 			
			MapData mapData = dbService.getMapDataService().FindByNetworkId(networkId);
			mapData.setNetworkId(newBfNetworkId);
			dbService.getMapDataService().Update(mapData);

		} else {
			MainMap.logger.info("No data found for networkName : " + networkName);
		}

		// dbService.getNetworkService().InsertNetwork(networkModel);

		System.out.println("**************************** Map Request : Processed *******************************");

		return MapConstants.SUCCESS;
	}

	/**
	 * This Mapping Called on request of the RWA algorithm execution on current
	 * Network Topology. It further dispatch the service to RWAController.
	 * 
	 * @return
	 */
	@RequestMapping(value = "/rwaAlgo", method = RequestMethod.POST, headers = "Accept=application/json", produces = "application/json")
	@ResponseBody
	public int executeRwaAlgo(@RequestBody String jsonString) throws Exception {
		int responseRwa = 0;
		System.out.println(" jsonString " + jsonString);
		org.json.JSONObject jsonMapDataObj = new org.json.JSONObject(jsonString);
		org.json.JSONObject networkInfoObj = (org.json.JSONObject) jsonMapDataObj.get("NetworkInfo");

		MapWebRwaAlgoExeController mapWebRwaAlgoExeController = new MapWebRwaAlgoExeController();

		/**
		 * Check for Brown Field
		 */
		String networkName = networkInfoObj.getString("NetworkName");
		System.out.println("networkName :  " + networkName);
		int brownFieldId = dbService.getNetworkService().GetNetworkInfoByNetworkName(networkName)
				.getNetworkIdBrownField();

		System.out.println(" brownFieldId " + brownFieldId);
		boolean brownFieldFlag = false;
		if (brownFieldId == MapConstants.BrownFieldBfId) {
			brownFieldFlag = true;
		}

		responseRwa = mapWebRwaAlgoExeController.rwaExecute(Integer.parseInt(
				dbService.getNetworkService().GetByNetworkName(networkInfoObj.getString("NetworkName")).toString()),
				brownFieldFlag, dbService);
		System.out.println("Response of RWA ::" + responseRwa);
		return responseRwa;
	}
	
	/**
	 * This Mapping Called on request of the Input for Circuit creation on
	 * current Network Topology. It further dispatch the service to Circuit
	 * Input Creation Controller.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/circuitInput", method = RequestMethod.POST, headers = "Accept=application/json", /*
	 * consumes
	 * =
	 * "application/json",
	 */ produces = "application/json")
	@ResponseBody
	public JSONObject /* List<DummyClass> */ getCircuitInput(@RequestBody String jsonStr) throws Exception {

		System.out.println("Inside Circuit Input and Going to Return the JSon.. ");

		/** Map From Common API */
		HashMap<String, Object> networkInfoMap = MapWebCommonAPIs.NestedStaticClass.getNetworkInformation(dbService,
				jsonStr);
		/**
		 * JSON Preparation for circuit input based on Circuit Count
		 */
		JSONObject jsonObj = new JSONObject();

		int networkId = (int) networkInfoMap
				.get(MapConstants.NetworkId); /** Network Id for common use */
		int greenFieldNetworkId = MapConstants.I_ZERO;

		if (((String) networkInfoMap.get(MapConstants.NetworkType)).equalsIgnoreCase(MapConstants.BrownField)) {
			greenFieldNetworkId = (int) networkInfoMap.get(
					MapConstants.GreenFieldId); /** Network Id for common use */
		}

		// Get node data , if green field then return Gf data
		// else if Bf , then check if Bf data is avlbl else return Gf data
		List<Node> nodes = dbService.getNodeService().FindAllNonILAsNetworkId(networkId);
		int nNodes = nodes.size();

		if (nNodes == MapConstants.I_ZERO && greenFieldNetworkId != MapConstants.I_ZERO) {
			nodes = dbService.getNodeService().FindAllNonILAsNetworkId(greenFieldNetworkId);
			nNodes = nodes.size();
		}

		jsonObj.put("NodeCount", nNodes);
		JSONArray jsonArrayNodeObj = new JSONArray();
		for (int i = 0; i < nodes.size(); i++) {

			JSONObject jsonRowObj = new JSONObject();

			jsonRowObj.put("NodeId", nodes.get(i).getNodeId());
			jsonRowObj.put("NodeType", nodes.get(i).getNodeType());
			jsonRowObj.put("NodeSubType", nodes.get(i).getNodeSubType());

			jsonArrayNodeObj.add(jsonRowObj);

		}
		jsonObj.put("NodeData", jsonArrayNodeObj);

		/** DB Call to get number of circuits and nodes */

		int nCircuits = dbService.getCircuitService().Count(networkId);

		int multiplier,nextIndex;
		
		// If Bf has circuit data
		if (((String) networkInfoMap.get(MapConstants.NetworkType)).equalsIgnoreCase(MapConstants.BrownField)
				&& nCircuits != MapConstants.I_ZERO) {
			/** Brown Field Check ***/
			System.out.println("Brown field case ..");
			System.out.println("Green Field Network Id ::" + greenFieldNetworkId);

			List<Map<String, Object>> oldCircuitsArr = dbService.getCircuitService().FindCommonCircuitsInBrFieldGrouped(
					greenFieldNetworkId,
					networkId); /** Fetch Circuit Data from Circuit Table */
		
			JSONArray jsonArrayCircuitObj = new JSONArray();

			for (Map<String, Object> rowCircuitObj : oldCircuitsArr) {

				JSONObject jsonRowObj = new JSONObject();
                
				jsonRowObj.put("Multiplier", rowCircuitObj.get("CNT"));
				jsonRowObj.put("CircuitId", rowCircuitObj.get("CircuitSet"));
				jsonRowObj.put("SrcNodeId", rowCircuitObj.get("SrcNodeId"));
				jsonRowObj.put("DestNodeId", rowCircuitObj.get("DestNodeId"));
				jsonRowObj.put("TrafficType", rowCircuitObj.get("TrafficType"));
				jsonRowObj.put("RequiredTraffic", rowCircuitObj.get("RequiredTraffic"));
				jsonRowObj.put("ProtectionType", rowCircuitObj.get("ProtectionType"));
				jsonRowObj.put("ProtectionMechanismType", rowCircuitObj.get("ClientProtectionType"));
				jsonRowObj.put("ChannelProtection", rowCircuitObj.get("ChannelProtection"));
				jsonRowObj.put("ColourPreference", rowCircuitObj.get("ColourPreference"));
				jsonRowObj.put("PathType", rowCircuitObj.get("PathType"));
				jsonRowObj.put("lineRate", rowCircuitObj.get("LineRate"));
				jsonRowObj.put("state", MapConstants.OldState); // State added
																// for BF - In
																// case of GF ,
																// only New
																// state is
																// available

				/** Newly Added params : 22th Feb, 2018 */
				jsonRowObj.put("ClientProtection", rowCircuitObj.get("ClientProtection"));
				jsonRowObj.put("LambdaBlocking", rowCircuitObj.get("LambdaBlocking"));
				jsonRowObj.put("VendorLabel", rowCircuitObj.get("VendorLabel"));
				jsonRowObj.put("NodeInclusion", rowCircuitObj.get("NodeInclusion"));

				/**10G Agg Circuits (if any)*/
				if( MapConstants.I_ZERO < dbService.getCircuit10gAggService().Count(networkId, Integer.parseInt(
																				rowCircuitObj.get("CircuitSet").toString().split(",")[0]))){

					MainMap.logger.debug("Case : 10G Agg Old Circuits Input ");

					
					List<Map<String,Object>> circuit10GAggList =   dbService.getCircuit10gAggService().FindCommonCircuits10gAggInBrField(greenFieldNetworkId,networkId,Integer.parseInt(
																				rowCircuitObj.get("CircuitSet").toString().split(",")[0]));		

					jsonRowObj.put("AggregatorCircuits", MapWebCommonAPIs.ConvertCircuit10GAggToJSON(dbService, circuit10GAggList).toJSONString());
					jsonRowObj.put("RequiredTraffic", "10(Agg)");
				}

				
				
				
				jsonArrayCircuitObj.add(jsonRowObj);
				
				
				
				
				
				
				
			}

			
			List<Circuit> newCircuitsArr=dbService.getCircuitService().FindAddedCircuitsInBrField(greenFieldNetworkId, networkId);
			

			
			for(int index=0;index<newCircuitsArr.size();index++){
				JSONObject jsonRowObj = new JSONObject();
				
				multiplier=1;
				String circuitSet=new String();	   
				circuitSet=circuitSet.concat(newCircuitsArr.get(index).getCircuitId()+",");
				
				nextIndex=index+1;
				// Compare both circuits on grouping logic + same traffic type and increase multiplier for same circuits
				while(nextIndex<newCircuitsArr.size() && newCircuitsArr.get(index).compareCircuit(newCircuitsArr.get(nextIndex)))
				{
					multiplier++;
					circuitSet=circuitSet.concat(newCircuitsArr.get(nextIndex).getCircuitId()+",");
					//System.out.println("index+1 -- "+(index+1)+" --- Multiplier == "+multiplier);
					nextIndex++;
				}
				
				jsonRowObj.put("Multiplier", multiplier);
				jsonRowObj.put("CircuitId", circuitSet);
				jsonRowObj.put("SrcNodeId", newCircuitsArr.get(index).getSrcNodeId());
				jsonRowObj.put("DestNodeId", newCircuitsArr.get(index).getDestNodeId());
				jsonRowObj.put("TrafficType", newCircuitsArr.get(index).getTrafficType());
				jsonRowObj.put("RequiredTraffic", newCircuitsArr.get(index).getRequiredTraffic());
				jsonRowObj.put("ProtectionType", newCircuitsArr.get(index).getProtectionType());
				jsonRowObj.put("ProtectionMechanismType", newCircuitsArr.get(index).getClientProtectionType());
				jsonRowObj.put("ChannelProtection", newCircuitsArr.get(index).getChannelProtection());
				jsonRowObj.put("ColourPreference", newCircuitsArr.get(index).getColourPreference());
				jsonRowObj.put("PathType", newCircuitsArr.get(index).getPathType());
				jsonRowObj.put("lineRate", newCircuitsArr.get(index).getLineRate());
				jsonRowObj.put("state", MapConstants.NewState); // State added for BF - In case
												// of GF , only New state is
												// available

				/** Newly Added params : 22th Feb, 2018 */
				jsonRowObj.put("ClientProtection", newCircuitsArr.get(index).getClientProtection());
				jsonRowObj.put("LambdaBlocking", newCircuitsArr.get(index).getLambdaBlocking());
				jsonRowObj.put("VendorLabel", newCircuitsArr.get(index).getVendorlabel());
				jsonRowObj.put("NodeInclusion", newCircuitsArr.get(index).getNodeInclusion());


				/**10G Agg Circuits (if any)*/
				if( MapConstants.I_ZERO < dbService.getCircuit10gAggService().Count(networkId, Integer.parseInt(circuitSet.split(",")[0]))){

					MainMap.logger.debug("Case : 10G Agg Old Circuits Input ");

					
					List<Map<String,Object>> circuit10GAggList =   dbService.getCircuit10gAggService().
																	FindAddedCircuitsInBrField(greenFieldNetworkId, networkId,Integer.parseInt(circuitSet.split(",")[0]));		

					jsonRowObj.put("AggregatorCircuits", MapWebCommonAPIs.ConvertCircuit10GAggToJSON(dbService, circuit10GAggList).toJSONString());
					jsonRowObj.put("RequiredTraffic", "10(Agg)");
				}
				
				
				
				

				jsonArrayCircuitObj.add(jsonRowObj);
				index=nextIndex-1;
			}

			jsonObj.put("CircuitData", jsonArrayCircuitObj);
			jsonObj.put("CircuitCount", newCircuitsArr.size() + oldCircuitsArr.size()); // Old
																						// +
																						// new
																						// circuit
																						// data
																						// length

		} else {
			System.out.println("Green field case ..");
			String state = MapConstants.GreenFieldState;

			if (greenFieldNetworkId != MapConstants.I_ZERO) {
				System.out.println("Bf but circuit data not saved yet");
				networkId = greenFieldNetworkId;
				state = MapConstants.OldState;
			}

			/** Fetch Circuit Data from Circuit Table */
//			List<Map<String, Object>> circuitObj = dbService.getCircuitService().FindAllGrouped(networkId);
			List<Circuit> circuits = dbService.getCircuitService().FindAll(networkId);
		
			JSONArray jsonArrayCircuitObj = new JSONArray();
			
			for(int index=0;index<circuits.size();index++){
				
				if(!circuits.get(index).getTrafficType().equals("OTU0") && !circuits.get(index).getTrafficType().equals("OTU1"))
				
				{		
					
			    JSONObject jsonRowObj = new JSONObject();
				
				multiplier=1;
				String circuitSet=new String();	   
				circuitSet=circuitSet.concat(circuits.get(index).getCircuitId()+",");	   
				
				nextIndex=index+1;
				// Compare both circuits on grouping logic + same traffic type and increase multiplier for same circuits
				while(nextIndex<circuits.size() && circuits.get(index).compareCircuit(circuits.get(nextIndex)))
				{
					multiplier++;
					circuitSet=circuitSet.concat(circuits.get(nextIndex).getCircuitId()+",");
					//System.out.println("index+1 -- "+(index+1)+" --- Multiplier == "+multiplier);
					nextIndex++;
				}
				
				jsonRowObj.put("QoS", circuits.get(index).getQoS());	
				jsonRowObj.put("Multiplier", multiplier);
				jsonRowObj.put("CircuitId", circuitSet);
				jsonRowObj.put("SrcNodeId", circuits.get(index).getSrcNodeId());
				jsonRowObj.put("DestNodeId", circuits.get(index).getDestNodeId());
				jsonRowObj.put("TrafficType", circuits.get(index).getTrafficType());
				jsonRowObj.put("RequiredTraffic", circuits.get(index).getRequiredTraffic());
				jsonRowObj.put("ProtectionType", circuits.get(index).getProtectionType());
				jsonRowObj.put("ProtectionMechanismType", circuits.get(index).getClientProtectionType());
				jsonRowObj.put("ChannelProtection", circuits.get(index).getChannelProtection());
				jsonRowObj.put("ColourPreference", circuits.get(index).getColourPreference());
				jsonRowObj.put("PathType", circuits.get(index).getPathType());
				jsonRowObj.put("lineRate", circuits.get(index).getLineRate());
				jsonRowObj.put("state", state); // State added for BF - In case
												// of GF , only New state is
												// available

				/** Newly Added params : 22th Feb, 2018 */
				jsonRowObj.put("ClientProtection", circuits.get(index).getClientProtection());
				jsonRowObj.put("LambdaBlocking", circuits.get(index).getLambdaBlocking());
				jsonRowObj.put("VendorLabel", circuits.get(index).getVendorlabel());
				jsonRowObj.put("NodeInclusion", circuits.get(index).getNodeInclusion());

                
				
				/**10G Agg Circuits (if any)*/
				if( MapConstants.I_ZERO < dbService.getCircuit10gAggService().Count(networkId, circuits.get(index).getCircuitId())){

					MainMap.logger.debug("Case : 10G Agg Circuits Input ");

					
					List<Map<String,Object>> circuit10GAggList =   dbService.getCircuit10gAggService().
																	FindGroupedCircuit10gAgg(networkId, circuits.get(index).getCircuitId());		

					jsonRowObj.put("AggregatorCircuits", MapWebCommonAPIs.ConvertCircuit10GAggToJSON(dbService, circuit10GAggList).toJSONString());
					jsonRowObj.put("RequiredTraffic", "10(Agg)");
				}
                
                
                
				
				
				jsonArrayCircuitObj.add(jsonRowObj);
				index=nextIndex-1;
			
           
			}
				
          }
		   MapWebPOTPCircuitInput MapWebPOTPObj = new MapWebPOTPCircuitInput();
           JSONArray PotpCircuitList = MapWebPOTPObj.POTPCircuitInput(dbService,networkId);
		   //System.out.println("PotpCircuitList"+PotpCircuitList);
		   for(int j=0;j<PotpCircuitList.size();j++)
		   {
			   jsonArrayCircuitObj.add(PotpCircuitList.get(j));
			   
		   }

			jsonObj.put("CircuitData", jsonArrayCircuitObj);
			jsonObj.put("CircuitCount", jsonArrayCircuitObj.size()); // New circuit data
															// length / Old
															// Circuit Data
															// length

			MainMap.logger.info("Final jsonObj for Second/More time Circuit Tab Input :- " + jsonObj);
			
			
			

		}


		
		return jsonObj;
	}


	
	

	

	/**
	 * This Mapping Called on request of the Save the Created Circuit on current
	 * Network Topology. It further dispatch the service to Circuit Input Save
	 * Controller.
	 * json
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping(value = "/circuitInputSave", method = RequestMethod.POST, headers = "Accept=application/json", /*
			 * consumes
			 * =
			 * "application/json",
			 */ produces = "application/json")
			@ResponseBody
			public int saveCircuitInput(@RequestBody String jsonStr) throws SQLException {

				System.out.println("Inside Circuit Input and Going to Save .. with " + jsonStr);

				MapWebCircuitInputSave mapCircuitInputSaveObj = new MapWebCircuitInputSave();

				int result=mapCircuitInputSaveObj.circuitInputSaveToDB(jsonStr, dbService);
				
				return result;

	}


	/**
	 * This Mapping Called on request of the delete circuit from circuit view in
	 * BF. It further dispatch the service to Circuit/Demand delete controller.
	 * 
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping(value = "/circuitDeleteBf", method = RequestMethod.POST, headers = "Accept=application/json", /*
	 * consumes
	 * =
	 * "application/json",
	 */ produces = "application/json")
	@ResponseBody
	public int deleteCircuitBf(@RequestBody String jsonStr) throws SQLException {

		System.out.println("Inside Circuit Delete and Going to delete circuit with " + jsonStr);

		MapWebDeleteCircuitDemand mapCircuitDemandDeleteObj = new MapWebDeleteCircuitDemand();

		mapCircuitDemandDeleteObj.circuitDelete(jsonStr, dbService);
		
		return MapConstants.SUCCESS;

	}

	/**
	 * This Mapping called on request for View Demand Matrix which return two
	 * views to front end View1: RouteMapping View2: DemandMapping
	 * 
	 * @return
	 * @throws SQLException
	 * @throws UnknownHostException
	 */
	@RequestMapping(value = "/generateDemandMatrixRequest", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public JSONObject generateDemandMatrix(@RequestBody String jsonStr) throws SQLException, UnknownHostException {

		System.out.println("Inside Generate Demand Matrix, Going to call for the same");

		MapWebGenerateDemandMatrix mapWebGenerateDemandMatrixObj = new MapWebGenerateDemandMatrix();

		JSONObject jsonObj = new JSONObject();

		jsonObj = mapWebGenerateDemandMatrixObj.generateDemanadMatrix(jsonStr, dbService);

		System.out.println("Return JsonString :- " + jsonObj.toJSONString());

		return jsonObj;

	}

	/**
	 * This Mapping Called on request of the Generate the IP Scheme of current
	 * Network Topology. It further dispatch the service to
	 * MapWebGenerateIPScheme Controller.
	 * 
	 * @param jsonStr
	 * @return
	 * @throws SQLException
	 * @throws UnknownHostException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/ipSchemeGenerationRequest", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "text/plain")
	@ResponseBody
	public int generateIPScheme(@RequestBody String jsonStr) throws SQLException, UnknownHostException, ParseException {

		System.out.println(
				"Inside IP Scheme Generation Request and Going to Generate Save it for Private IP Block :- " + jsonStr);

		MapWebGenerateIPScheme generateIPSchemeObj = new MapWebGenerateIPScheme();

		int responseVal = generateIPSchemeObj.generateSaveIPScheme(jsonStr, dbService);

		System.out.println("generateIPScheme Request Processed .. ");

		return responseVal;

	}

	@RequestMapping(value = "/configFileGenerationRequest", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "text/plain")
	@ResponseBody
	public int generateConfigFile(@RequestBody String jsonStr) {

		System.out.println("Inside Generate Config File ");

		MapWebGenerateControlPathConfigFile generateControlPathConfigFileObj = new MapWebGenerateControlPathConfigFile();

		int responseVal = generateControlPathConfigFileObj.generateControlPathConfigFile(jsonStr, dbService);

		System.out.println("Generate Config File Request Processed .. ");

		return responseVal;

	}

	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/datpathConfigFileGenerationRequest", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "text/plain")
	@ResponseBody
	public int generateDataPathConfigFile(@RequestBody String jsonString) {

		System.out.println("Inside Generate Config File ");

		MapWebGenerateDataPathConfigFile generateControlPathConfigFileObj = new MapWebGenerateDataPathConfigFile();

		int responseVal = generateControlPathConfigFileObj.generateDataPathConfigFile(jsonString, dbService);

		System.out.println("Generate Config File Request Processed .. ");

		return responseVal;

	}
	
	/**
	 * 
	 * @param jsonString
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/combinedConfigFileGenerationTrigger", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "text/plain")
	@ResponseBody
	public int combinedConfigFileGenerationTrigger(@RequestBody String jsonString) throws IOException {

		MainMap.logger.debug("combinedConfigFileGenerationTrigger ");

		int responseVal = MapWebCommonAPIs.NestedStaticClass.GenerateCombinedConfigFiles(jsonString, dbService);

		MainMap.logger.debug("combinedConfigFileGenerationTrigger Request Processed");

		return responseVal;

	}
	

	/**
	 * 
	 * This Mapping Called on request of the View the Generated IP Scheme of
	 * current Network Topology. It further dispatch the service to
	 * MapWebGenerateIPScheme Controller.
	 * 
	 * @return
	 * @throws SQLException
	 * @throws UnknownHostException
	 */
	@RequestMapping(value = "/viewIpSchemeDataRequest", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public JSONObject viewIPSchemeRequest(String jsonStr) throws SQLException, UnknownHostException {

		System.out.println("Inside IP Scheme View Request ");
		JSONObject jsonObj;

		MapWebGenerateIPScheme viewGeneratedIPSchemeObj = new MapWebGenerateIPScheme();

		jsonObj = viewGeneratedIPSchemeObj.viewGeneratedIPSchemeReq(jsonStr, dbService);

		System.out.println("viewIPSchemeRequest Request Processed .. ");

		return jsonObj;

	}

	/**
	 * 
	 * @param
	 * @return
	 * @throws SQLException
	 * @throws UnknownHostException
	 */
	@RequestMapping(value = "/generateEquipment", method = RequestMethod.POST, headers = "Accept=application/json", /*
	 * consumes
	 * =
	 * "application/json",
	 */ produces = "application/json")
	@ResponseBody
	public JSONObject viewBOMExcelDataRequest(@RequestBody String jsonStr) throws SQLException {
		System.out.println("Inside viewBOMExcelDataRequest");
		ResourcePlanning rc = new ResourcePlanning(dbService);
		// ResourcePlanUtils rpu = new ResourcePlanUtils(dbService);

		org.json.JSONObject jsonMapDataObj = new org.json.JSONObject(jsonStr);
		org.json.JSONObject networkInfoObj = (org.json.JSONObject) jsonMapDataObj.get("NetworkInfo");
		String process = jsonMapDataObj.getString("Step");
		
		JSONObject responseObject=new JSONObject();

		int networkId = Integer.parseInt(
				dbService.getNetworkService().GetByNetworkName(networkInfoObj.getString("NetworkName")).toString());
		
		// If RWA doesn't have errors , then do Res. Allocation
		if(dbService.getNetworkRouteService().FindAllErrorPaths(networkId).isEmpty()){
			if (dbService.getNetworkService().IsBrownFieldNetwork(networkId)) {
				int gfNetworkId=Integer.parseInt(dbService.getNetworkService().GetGreenFieldNetworkId(networkId).toString());
				return rc.fResourceAllocationBF(gfNetworkId,process);
			} else {
				return rc.fResourceAllocationGF(networkId,process);
			}
			
//			dbService.getCdcRoadmAddService().DeleteByNetworkId(networkId);// DBG
//			dbService.getCdcRoadmDropService().DeleteByNetworkId(networkId);// DBG
//			MapWebSaveOpticalPowerDb mapWebSaveOpticalPowerDb = new MapWebSaveOpticalPowerDb();
//			mapWebSaveOpticalPowerDb.MapWebSaveOpticalPowerCDC(jsonStr, dbService);

		}
		else{
//			return "0";
			responseObject.put("Response", "RWA output has some errors.");
			responseObject.put("ResponseStatus", 0);
			return responseObject;
		}
				
		/*
		 * CablingMap cmap = new CablingMap(dbService);
		 * cmap.fgenerateCablingMap(networkId);
		 */

//		return "1";

	}

	/**
	 * 
	 * @param
	 * @return
	 * @throws SQLException
	 * @throws UnknownHostException
	 */
//	@RequestMapping(value = "/viewBomSaveEquipmentPref", method = RequestMethod.POST, headers = "Accept=application/json", /*
//	 * consumes
//	 * =
//	 * "application/json",
//	 */ produces = "application/json")
//	@ResponseBody
//	public JSONObject viewBomSaveEquipPrefUpdateDataRequest(@RequestBody String jsonStr)
//			throws SQLException, UnknownHostException {
//
//		int networkId = Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString());
//		int saveEquipmentPrefCheck = 0;
//		System.out.println("Inside viewBomSaveEquipmentPref");
//		// MapWebSaveEquipPref viewBomSaveEquipmentPrefObj = new
//		// MapWebSaveEquipPref(); //equipment pref db
//		// MapWebExcelHandler viewBOMExcelDataRequestObj = new
//		// MapWebExcelHandler(); //equipment db
//		// ResourcePlanUtils rpu = new ResourcePlanUtils(dbService);
//		// GenerateMcsMapping gmm = new GenerateMcsMapping(dbService, rpu);
//		//
//		// saveEquipmentPrefCheck=viewBomSaveEquipmentPrefObj.equipmentPrefSaveToDB(jsonStr,dbService);
//		//
//		ResourcePlanning rc = new ResourcePlanning(dbService);
//		// InitializeConfigurationData cd = new
//		// InitializeConfigurationData(dbService);
//		// if(dbService.getEquipmentService().Count()==0)
//		// {
//		// Equipment[] equipmentModelObjectArray;
//		// equipmentModelObjectArray=viewBOMExcelDataRequestObj.viewBOMExcelDataRequestReq(dbService);
//		// rc.fGenerateEquipmentDb(equipmentModelObjectArray);
//		// }
//		//
//		// long timeStart = System.currentTimeMillis();
//		// //deleting all previous card information
//		// dbService.getCardInfoService().DeleteAllCardInfo(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
//		// //deleting all previous port information
//		// dbService.getPortInfoService().DeleteAllPortInfo(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
//		// cd.fDeleteConfigurationData(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
//		// //Card assignment for Rack view
//		// rc.fInitAllNodesInNetwork(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
//		// rc.fAssignPairedMuxponderCards(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
//		// rc.fAssignRegenerators(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
//		// rc.fAssignMPCs(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
//		// rc.fAssignOLPs(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
//		// rc.fAssignMPCs(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
//		// rc.fassignDCMTray(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
//		// rc.fAssignMPCs(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
//		// rc.fAssignYcables(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
//		// rc.fAssignPortsOnCard(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
//		//
//		// //create view for cardinfo
//		// dbService.getCardInfoService().CreateViewAllCardInfo(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
//		// //create view for portinfo
//		// dbService.getPortInfoService().CreateViewAllPortInfo(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
//		//
//		//
//		// //patch cord generation
//		// dbService.getPatchCordService().DeletePatchCordInfo(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
//		// rc.fAssignPatchCords(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
//		//
//		// GenerateLinkWavelengthMap linkwmap = new
//		// GenerateLinkWavelengthMap(dbService);
//		// dbService.getLinkWavelengthService().DeleteByNetworkId(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
//		// dbService.getLinkWavelengthInfoService().DeleteByNetworkId(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
//		//// linkwmap.fgenerateLinkWavelengthMap(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
//		// linkwmap.fgenerateLinkWavelengthInformation(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
//		//
//		// //inventory generation
//		// dbService.getInventoryService().DeleteInventory(1);
//		// rc.fGenerateInventory(1);
//		// rc.getDbService().getViewServiceRp().FindBoM(1).toString();
//		//
//		// gmm.fgenerateMcsMap(networkId);
//		// long timeEnd = System.currentTimeMillis();
//		// System.out.println("Resource Allocation and Inventory Generation Time
//		// :"+(timeEnd-timeStart)+" MilliSeconds"+"**************");
//		// //initializing the configuration data of the cards
//		// cd.fInitializeConfigurationData(networkId);
//
//		if (dbService.getNetworkService().IsBrownFieldNetwork(networkId)) {
//			rc.fResourceAllocationBF(networkId);
//		} else {
//			rc.fResourceAllocationGF(networkId);
//		}
//
//		/*
//		 * CablingMap cmap = new CablingMap(dbService);
//		 * cmap.fgenerateCablingMap(Integer.parseInt(dbService.getNetworkService
//		 * ().GetByNetworkName("TestNetwork").toString()));
//		 */
//
//		/* *JSON Preparation Call */
//		MapWebGenerateBomJson mapWebGenerateBomJsonObj = new MapWebGenerateBomJson();
//		JSONObject jsonFinalBomRespObj = mapWebGenerateBomJsonObj.MapWebGenerateBomJsonReq(dbService);
//
//		System.out.println("Handle BOM File Request Processed after Equipment Preferences");
//		System.out.println("Returning response of viewBomSaveEquipmentPref");
//		// return saveEquipmentPrefCheck;
//		return jsonFinalBomRespObj;
//	}

	@RequestMapping(value = "/generatePatchCord", method = RequestMethod.POST, headers = "Accept=application/json", produces = "application/json")
	@ResponseBody
	public JSONObject patchCordAllocation(@RequestBody String jsonStr) throws SQLException, UnknownHostException {

		System.out.println("Processing generatePatchCord .....");
		JSONObject responseObject=new JSONObject();
		
		/** Map From Common API */
		HashMap<String, Object> networkInfoMap = MapWebCommonAPIs.NestedStaticClass.getNetworkInformation(dbService,
				jsonStr);
		int networkId = (int) networkInfoMap
				.get(MapConstants.NetworkId); /** Network Id for common use */
				
		/** JSON Preparation Call */
		dbService.getPatchCordService().DeletePatchCordInfo(networkId);
		
		PatchCordAllocation pc=new PatchCordAllocation(dbService);
		try {
			pc.fAssignPatchCords(networkId);
			System.out.println("generatePatchCord Request Processed");
			responseObject.put("Response", "Patch Cord Generated Successfully.");
			responseObject.put("ResponseStatus", 1);
			return responseObject;
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}		

	}
	
	@RequestMapping(value = "/getBOMJsonDataRequest", method = RequestMethod.GET, headers = "Accept=application/json", produces = "application/json")
	@ResponseBody
	public JSONObject getBOMJsonDataRequest() throws SQLException, UnknownHostException {

		System.out.println("Inside getBOMJsonDataRequest");

		/** JSON Preparation Call */
		MapWebGenerateBomJson mapWebGenerateBomJsonObj = new MapWebGenerateBomJson();
		JSONObject jsonFinalBomRespObj = mapWebGenerateBomJsonObj.MapWebGenerateBomJsonReq(dbService);

		System.out.println("getBOMJsonDataRequest Request Processed");

		return jsonFinalBomRespObj;

	}


	@RequestMapping(value = "/dwdmemsnbi", method = RequestMethod.GET, headers = "Accept=application/json", produces = "application/json")
	@ResponseBody
	public JSONObject getEMSAUthRequest() throws SQLException, UnknownHostException {

		System.out.println("Inside getEMSAUthRequest");

		/** JSON Preparation Call */
		
		JSONObject jsonFinalBomRespObj = new JSONObject();
		jsonFinalBomRespObj.put("responsecode", "200");																																																																																																																																																																																																																																																																																																																																																																																																																																									

		System.out.println("getBOMJsonDataRequest Request Processed");

		return jsonFinalBomRespObj;

	}



	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/generateEquipmentDBRequest", method = RequestMethod.POST, headers = "Accept=application/json", produces = "application/json")
	@ResponseBody
	public int generateEquipmentDBRequest(@RequestBody String jsonStr) throws SQLException, UnknownHostException {

		ResourcePlanning rc = new ResourcePlanning(dbService);
		MapWebExcelHandler viewBOMExcelDataRequestObj = new MapWebExcelHandler();

		/** Map From Common API */
		HashMap<String, Object> networkInfoMap = MapWebCommonAPIs.NestedStaticClass.getNetworkInformation(dbService,
				jsonStr);
		int networkId = (int) networkInfoMap
				.get(MapConstants.NetworkId); /** Network Id for common use */

		if (dbService.getEquipmentService()
				.Count() == 0) /** DB not populated yet */
		{
			Equipment[] equipmentModelObjectArray;
			equipmentModelObjectArray = viewBOMExcelDataRequestObj.viewBOMExcelDataRequestReq(dbService);
			rc.fGenerateEquipmentDb(equipmentModelObjectArray);
		}

		// if(dbService.getCardPreferenceService().Count(networkId)==0)
		// {
		// rc.fsetDefaultCardPreference(networkId);
		// }

		System.out.println(" DB Populated Successfully for Equipment and preferences ");
		return MapConstants.SUCCESS;

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/updateEquipment", method = RequestMethod.POST, headers = "Accept=application/json", produces = "application/json")
	@ResponseBody
	public int updateEquipmentDBRequest(@RequestBody String jsonStr) throws SQLException, UnknownHostException {

		ResourcePlanning rc = new ResourcePlanning(dbService);
		MapWebExcelHandler viewBOMExcelDataRequestObj = new MapWebExcelHandler();

		/** Map From Common API */
		HashMap<String, Object> networkInfoMap = MapWebCommonAPIs.NestedStaticClass.getNetworkInformation(dbService,
				jsonStr);
		int networkId = (int) networkInfoMap
				.get(MapConstants.NetworkId); /** Network Id for common use */

		dbService.getEquipmentService().DeleteEquipment();

		Equipment[] equipmentModelObjectArray;
		equipmentModelObjectArray = viewBOMExcelDataRequestObj.viewBOMExcelDataRequestReq(dbService);

		rc.fGenerateEquipmentDb(equipmentModelObjectArray);

		// if(dbService.getCardPreferenceService().Count(networkId)==0)
		// {
		// rc.fsetDefaultCardPreference(networkId);
		// }

		System.out.println(" DB Populated Successfully for Equipment and preferences ");
		return MapConstants.SUCCESS;

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getEquipmentJsonDataRequest", method = RequestMethod.GET, headers = "Accept=application/json", produces = "application/json")
	@ResponseBody
	public JSONObject getEquipmentJsonDataRequest() throws SQLException, UnknownHostException {

		System.out.println("Inside getEquipmentJsonDataRequest");

		/**
		 * JSON Preparation : Equipment Data
		 */
		JSONObject jsonFinalObj = new JSONObject();
		List<Equipment> equipment = dbService.getEquipmentService().FindAll();

		JSONArray jsonArrayEquipmentObj = new JSONArray();
		System.out.println("equipment Size : " + equipment.size());
		for (int i = 0; i < equipment.size(); i++) {

			JSONObject jsonEquipmentRowObj = new JSONObject();

			jsonEquipmentRowObj.put("Name", equipment.get(i).getName());
			jsonEquipmentRowObj.put("PartNo", equipment.get(i).getPartNo());
			jsonEquipmentRowObj.put("PowerConsumption", equipment.get(i).getPowerConsumption());
			jsonEquipmentRowObj.put("SlotSize", equipment.get(i).getSlotSize());
			jsonEquipmentRowObj.put("Details", equipment.get(i).getDetails());
			jsonEquipmentRowObj.put("Price", equipment.get(i).getPrice());
			jsonEquipmentRowObj.put("RevisionCode", equipment.get(i).getRevisionCode());
			jsonEquipmentRowObj.put("Category", getCategoryIndex(equipment.get(i).getCategory()));

			jsonArrayEquipmentObj.add(jsonEquipmentRowObj);

		}

		jsonFinalObj.put("equipmentDbData", jsonArrayEquipmentObj);

		System.out.println("getEquipmentJsonDataRequest Request Processed");

		return jsonFinalObj;

	}

	private Object getCategoryIndex(int equipmentcategory) {
		{

			String equipmentStr = new String();
			equipmentStr = "";
			switch (equipmentcategory) {
			case 0:
				equipmentStr = MapConstants.Category0;
				break;
			case 1:
				equipmentStr = MapConstants.Category1;
				break;
			case 2:
				equipmentStr = MapConstants.Category2;
				break;
			case 3:
				equipmentStr = MapConstants.Category3;
				break;
			case 4:
				equipmentStr = MapConstants.Category4;
				break;
			case 5:
				equipmentStr = MapConstants.Category5;
				break;
			case 6:
				equipmentStr = MapConstants.Category6;
				break;
			case 7:
				equipmentStr = MapConstants.Category7;
				break;
			case 8:
				equipmentStr = MapConstants.Category8;
				break;
			case 9:
				equipmentStr = MapConstants.Category9;
				break;
			case 10:
				equipmentStr = MapConstants.Category10;
				break;
			case 11:
				equipmentStr = MapConstants.Category11;
				break;
			case 12:
				equipmentStr = MapConstants.Category12;
				break;

			}

			// TODO Auto-generated method stub
			return equipmentStr;
		}

	}

	/**
	 * method for client information of a node
	 */

	/**
	 * 
	 * @param jsonStr
	 * @return
	 * @throws SQLException
	 * @throws UnknownHostException
	 */
	@RequestMapping(value = "/getviewReportJsonDataRequest", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "text/plain")
	@ResponseBody
	public JSONObject getWavelengthJsonDataRequest(@RequestBody String jsonStr)
			throws SQLException, UnknownHostException {

		System.out.println("Inside getWavelengthJsonDataRequest " + jsonStr);

		/** JSON Preparation Call */
		MapWebViewviewReportJson mapWebViewWavelengthJsonobj = new MapWebViewviewReportJson();
		JSONObject jsonFinalwavelengthRespObj = mapWebViewWavelengthJsonobj.MapWebGenerateviewReportJsonReq(jsonStr,
				dbService);

		System.out.println("getWavelengthJsonDataRequest Request Processed");

		return jsonFinalwavelengthRespObj;

	}

	/**
	 * ******************** This Mapping Called on request of the Chassis View
	 * for a Node Id. It further dispatch the service to
	 * MapWebGenerateChassisViewJsonReq Controller.
	 ******************** 
	 * @param nodeId
	 * @return
	 * @throws SQLException
	 * @throws UnknownHostException
	 */

	@RequestMapping(value = "/getChassisViewjsonDataRequest", method = RequestMethod.POST, headers = "Accept=application/json", produces = "application/json")
	@ResponseBody
	public JSONObject getChassisViewJsonDataRequest(@RequestBody String jsonStr)
			throws SQLException, UnknownHostException {

		/** Map From Common API */
		HashMap<String, Object> networkInfoMap = MapWebCommonAPIs.NestedStaticClass.getNetworkInformation(dbService,
				jsonStr);

		org.json.JSONObject jsonDataObj = new org.json.JSONObject(jsonStr);
		int nodeId = Integer.parseInt(jsonDataObj.get("selectedNode").toString().trim());

		System.out.println("getChassisViewJsonDataRequest Request NodeId :" + nodeId);

		/** JSON Preparation Call */
		MapWebGenerateChassisViewjson mapWebGenerateBomJsonObj = new MapWebGenerateChassisViewjson();
		JSONObject jsonFinalChassisViewRespObj = mapWebGenerateBomJsonObj.MapWebGenerateChassisViewJsonReq(nodeId,
				networkInfoMap, dbService);

		System.out.println("getChassisViewJsonDataRequest Request Processed");

		return jsonFinalChassisViewRespObj;

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getChassisJsonArrayDataRequest", method = RequestMethod.POST, headers = "Accept=application/json", produces = "application/json")
	@ResponseBody
	public JSONObject getChassisJsonArrayDataRequest(@RequestBody String jsonStr)
			throws SQLException, UnknownHostException {

		System.out.println("Inside getChassisJsonArrayDataRequest");

		/** Map From Common API */
		HashMap<String, Object> networkInfoMap = MapWebCommonAPIs.NestedStaticClass.getNetworkInformation(dbService,
				jsonStr);
		int networkId = (int) networkInfoMap
				.get(MapConstants.NetworkId); /** Network Id for common use */

		/** JSON Preparation Call */

		/**
		 * JSON Preparation PART I: Node Data
		 */
		JSONObject jsonFinalObj = new JSONObject();
		List<Node> nodes = dbService.getNodeService()
				.FindAll(networkId);/** DB Call to get number of nodes */

		JSONArray jsonArrayNodeObj = new JSONArray();

		for (int i = 0; i < nodes.size(); i++) {

			jsonArrayNodeObj.add(nodes.get(i).getNodeId());
		}

		jsonFinalObj.put("NodeData", jsonArrayNodeObj);

		System.out.println("After JSON Preparation : jsonFinalObj Data  :- " + jsonFinalObj);
		System.out.println("getBOMJsonDataRequest Request Processed");

		return jsonFinalObj;

	}

	/** DBG => For Chassis String Generation */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getChassisJsonAllNodeDataRequest", method = RequestMethod.GET, headers = "Accept=application/json", produces = "application/json")
	@ResponseBody
	public JSONObject getChassisJsonAllNodeDataRequest() throws SQLException, UnknownHostException {

		System.out.println("Inside getChassisJsonArrayAllNodeDataRequest");

		/** JSON Preparation Call */

		/**
		 * JSON Preparation PART I: Node Data
		 */
		JSONObject jsonFinalObj = new JSONObject();
		List<Node> nodes = dbService.getNodeService()
				.FindAll(1);/** DB Call to get number of nodes */

		JSONArray jsonArrayNodeObj = new JSONArray();
		/// JSONObject jsonFinalChassisViewRespObj = new JSONObject();

		for (int i = 0; i < nodes.size(); i++) {

			JSONObject jsonRowObj = new JSONObject();
			MapWebGenerateChassisViewjson mapWebGenerateBomJsonObj = new MapWebGenerateChassisViewjson();
			// jsonRowObj =
			// mapWebGenerateBomJsonObj.MapWebGenerateChassisViewJsonReq(nodes.get(i).getNodeId(),dbService);
			// //Debug
			jsonRowObj.put("NodeId", nodes.get(i).getNodeId());
			/// System.out.println(" jsonRowObj " + jsonRowObj);

			jsonArrayNodeObj.add(jsonRowObj);
			System.out.println(" jsonArrayNodeObj " + jsonArrayNodeObj);
		}

		jsonFinalObj.put("NodeData", jsonArrayNodeObj);

		// System.out.println("After JSON Preparation : jsonFinalObj Data :-
		// "+jsonFinalObj);

		// System.out.println("getBOMJsonDataRequest Request Processed");

		return jsonFinalObj;

	}

	/*
	 * ************ Optical Data Request for Optical Power Calculations per node
	 * This method is used to return data rquired to show Optical Power
	 * Calculations per node per direction on the main screen of NPT
	 * ******************
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getOpticalPowerJsonDataRequest", method = RequestMethod.POST, headers = "Accept=application/json", /*
	 * consumes
	 * =
	 * "application/json",
	 */ produces = "application/json")
	@ResponseBody
	public JSONObject getOpticalPowerJsonDataRequest(@RequestBody String jsonStr)
			throws SQLException, UnknownHostException {

		System.out.println("Inside getOpticalPowerJsonDataRequest");

		/** JSON Preparation Call */
		MapWebOpticalPowerData mapWebGetOpticalPowerJsonObj = new MapWebOpticalPowerData();
		JSONObject jsonFinalOpticalPowerRespObj = mapWebGetOpticalPowerJsonObj.MapWebGetOpticalPowerJsonReq(jsonStr,
				dbService);

		System.out.println("getOpticalPowerJsonDataRequest Request Processed");

		return jsonFinalOpticalPowerRespObj;

	}

	/*
	 * ************* Cabling Data Request for Cabling Diagram
	 * View******************
	 */
	@RequestMapping(value = "/getCablingInfoJsonDataRequest", method = RequestMethod.POST, headers = "Accept=application/json", produces = "application/json")
	@ResponseBody
	public JSONObject getCablingInfoJsonDataRequest(@RequestBody String jsonStr)
			throws SQLException, UnknownHostException {

		System.out.println("Inside getCablingInfoJsonDataRequest");

		/** JSON Preparation Call */
		mapWebGetCablingInfo mapWebGetCablingInfoJsonObj = new mapWebGetCablingInfo();
		JSONObject jsonFinalCablingInfoRespObj = mapWebGetCablingInfoJsonObj.MapWebGetCablingInfoJsonReq(jsonStr,
				dbService);

		System.out.println("getCablingInfoJsonDataRequest Request Processed");

		return jsonFinalCablingInfoRespObj;

	}

	/**
	 * This Mapping Called on request of Save configuration from detailed
	 * configuration modal.. It further dispatch the service to their respective
	 * card configuration save Controller.
	 * 
	 * @author avinash
	 * @return void
	 * @throws SQLException
	 */
	@RequestMapping(value = "/setConfiguration", method = RequestMethod.POST, headers = "Accept=application/json", /*
	 * consumes
	 * =
	 * "application/json",
	 */ produces = "application/json")
	@ResponseBody
	public int saveDetailedCardConfiguration(@RequestBody String jsonStr) throws SQLException {

		System.out.println("Inside saveDetailedCardConfiguration and Going to Save .. with " + jsonStr);

		MapWebDetailedConfiguration mapWebDetailedConfiguration = new MapWebDetailedConfiguration();
		int result = mapWebDetailedConfiguration.setDetailedConfiguration(jsonStr, dbService);
		return result;
	}

	/**
	 * This Mapping Called on request of Save configuration from detailed
	 * configuration modal.. It further dispatch the service to their respective
	 * card configuration save Controller.
	 * 
	 * @author avinash
	 * @return void
	 * @throws SQLException
	 */
	@RequestMapping(value = "/getConfiguration", method = RequestMethod.POST, headers = "Accept=application/json", /*
	 * consumes
	 * =
	 * "application/json",
	 */ produces = "application/json")
	@ResponseBody
	public JSONObject getDetailedCardConfiguration(@RequestBody String jsonStr) throws SQLException {

		// System.out.println("Inside getDetailedCardConfiguration and Going to
		// get circuit data .. with "+ jsonStr);

		JSONObject resultJson = null;
		MapWebDetailedConfiguration mapWebDetailedConfiguration = new MapWebDetailedConfiguration();
		resultJson = mapWebDetailedConfiguration.getDetailedConfiguration(jsonStr, dbService);

		System.out.println(resultJson.toString());
		return resultJson;

	}

	/** DBG => For Chassis String Generation */
	/**
	 * This Mapping is For Generating a Consolidated Report
	 * 
	 * @return
	 * @throws NumberFormatException
	 * @throws SQLException
	 */
	@RequestMapping(value = "/generateConsolidatedReportTrigger", method = RequestMethod.POST, 
			produces = MediaType.APPLICATION_PDF_VALUE)
	@ResponseBody
	public ResponseEntity<byte[]>  generateConsolidatedReportTrigger(@RequestBody String jsonStr)
			throws NumberFormatException, SQLException {
		/** Map From Common API */
		HashMap<String, Object> networkInfoMap = MapWebCommonAPIs.NestedStaticClass.getNetworkInformation(dbService,
				jsonStr);
		
		System.out.println("Downloading Network Map Image...");
		Object NetworkInfo = ((org.json.JSONObject)(new org.json.JSONObject(jsonStr))).get("NetworkInfo");
		String imgUriStr= ((org.json.JSONObject)NetworkInfo).get("ImgData").toString();
		byte[] imagedata = DatatypeConverter.parseBase64Binary(imgUriStr.substring(imgUriStr.indexOf(",") + 1));
		BufferedImage bufferedImage;
		try {
			bufferedImage = ImageIO.read(new ByteArrayInputStream(imagedata));
			ImageIO.write(bufferedImage, "png", new File(System.getProperty("java.io.tmpdir")+"/NetworkMap.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(" generating Report...");
		PlanningReportGeneration pr = new PlanningReportGeneration(dbService);
		pr.createReport(networkInfoMap);
		
		System.out.println("Sending Generated Report to client...");
		byte[] contents=null;
		try {
			contents = Files.readAllBytes(Paths.get(System.getProperty("user.home")+"/Downloads/PlanningReport.pdf"));
		} catch (IOException e) {
			e.printStackTrace();
		};

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.parseMediaType("application/pdf"));
	    String filename = "planningReport.pdf";
	    headers.setContentDispositionFormData("inline", filename);
	    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
	    ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
	    return response;
	}

	/**
	 * This Mapping Called on request of Save Parametric Preference.. It further
	 * dispatch the service to their respective preference save controller.
	 * 
	 * @author avinash
	 * @return void
	 * @throws SQLException
	 */
	@RequestMapping(value = "/saveParamPref", method = RequestMethod.POST, headers = "Accept=application/json", /*
	 * consumes
	 * =
	 * "application/json",
	 */ produces = "application/json")
	@ResponseBody
	public int saveParametricPreference(@RequestBody String jsonStr) throws SQLException {

		System.out.println("Inside saveParametricPreference and Going to Save data using " + jsonStr);

		/** Map From Common API */
		HashMap<String, Object> networkInfoMap = MapWebCommonAPIs.NestedStaticClass.getNetworkInformation(dbService,
				jsonStr);
		int networkId = (int) networkInfoMap
				.get(MapConstants.NetworkId); /** Network Id for common use */

		org.json.JSONObject jsonReqDataObject = new org.json.JSONObject(jsonStr);
		org.json.JSONArray paramPrefArray = (org.json.JSONArray) jsonReqDataObject.get("ParamPreferences");

		dbService.getParametricPreferenceService().DeleteByNetworkId(networkId);

		for (int i = 0; i < paramPrefArray.length(); i++) {
			org.json.JSONObject paramPrefDataObj = (org.json.JSONObject) paramPrefArray.get(i);
			ParametricPreference prefData = new ParametricPreference();
			prefData.setNetworkId(networkId);
			prefData.setNodeId(Integer.parseInt(paramPrefDataObj.getString("Node")));
			prefData.setCardType(paramPrefDataObj.getString("Type"));
			prefData.setParameter(paramPrefDataObj.getString("Parameter"));
			prefData.setCategory(paramPrefDataObj.getString("Category"));
			if(paramPrefDataObj.getString("Parameter").equals(ResourcePlanConstants.ParamDirection))
				prefData.setValue(MapConstants.fGetDirectionStr(Integer.parseInt(paramPrefDataObj.getString("Parameter Value"))));
			else prefData.setValue(paramPrefDataObj.getString("Parameter Value"));
			prefData.setSerialNo(paramPrefDataObj.getString("S.No (Optional)"));
			dbService.getParametricPreferenceService().Insert(prefData);
		}

		return MapConstants.SUCCESS;
	}
	
	/**
	 * This Mapping Called on request of Save Allocation Exception Preference.. It further
	 * dispatch the service to their respective preference save controller.
	 * 
	 * @author avinash
	 * @return void
	 * @throws SQLException
	 */
	@RequestMapping(value = "/saveAllocationExcPref", method = RequestMethod.POST, headers = "Accept=application/json", /*
	 * consumes
	 * =
	 * "application/json",
	 */ produces = "application/json")
	@ResponseBody
	public int saveAllocationExcPref(@RequestBody String jsonStr) throws SQLException {

		System.out.println("Inside saveAllocationExcPref and Going to Save data using " + jsonStr);

		/** Map From Common API */
		HashMap<String, Object> networkInfoMap = MapWebCommonAPIs.NestedStaticClass.getNetworkInformation(dbService,
				jsonStr);
		int networkId = (int) networkInfoMap
				.get(MapConstants.NetworkId); /** Network Id for common use */

		org.json.JSONObject jsonReqDataObject = new org.json.JSONObject(jsonStr);
		org.json.JSONArray exceptionPrefArray = (org.json.JSONArray) jsonReqDataObject.get("ExceptionPreference");

		dbService.getAllocationExceptionsService().DeleteByNetworkId(networkId);

		for (int i = 0; i < exceptionPrefArray.length(); i++) {
			org.json.JSONObject exceptionDataObj = (org.json.JSONObject) exceptionPrefArray.get(i);
			AllocationExceptions prefData = new AllocationExceptions();
			prefData.setNetworkId(networkId);
			prefData.setNodeId(Integer.parseInt(exceptionDataObj.getString("Node")));
			prefData.setCardType(exceptionDataObj.getString("Card Type"));
			prefData.setType(exceptionDataObj.getString("Exception Type"));
			String location=exceptionDataObj.getString("Rack")+"_"+exceptionDataObj.getString("SubRack")+"_"+exceptionDataObj.getString("Slot");
			prefData.setLocation(location);
			prefData.setPort(exceptionDataObj.getString("Port"));
			prefData.setSerialNo(exceptionDataObj.getString("Serial No (Optional)"));
			dbService.getAllocationExceptionsService().Insert(prefData);
		}

		return MapConstants.SUCCESS;
	}

	/**
	 * This Mapping Called on request of Save Parametric Preference.. It further
	 * dispatch the service to their respective preference save controller.
	 * 
	 * @author avinash
	 * @return void
	 * @throws SQLException
	 */
	@RequestMapping(value = "/saveStockPref", method = RequestMethod.POST, headers = "Accept=application/json", /*
	 * consumes
	 * =
	 * "application/json",
	 */ produces = "application/json")
	@ResponseBody
	public int saveStockPreference(@RequestBody String jsonStr) throws SQLException {

		System.out.println("Inside saveStockPreference and Going to Save data using " + jsonStr);

		/** Map From Common API */
		HashMap<String, Object> networkInfoMap = MapWebCommonAPIs.NestedStaticClass.getNetworkInformation(dbService,
				jsonStr);
		int networkId = (int) networkInfoMap
				.get(MapConstants.NetworkId); /** Network Id for common use */

		org.json.JSONObject jsonReqDataObject = new org.json.JSONObject(jsonStr);
		org.json.JSONArray stockPrefArray = (org.json.JSONArray) jsonReqDataObject.get("StockPreferences");

		dbService.getStockService().DeleteByNetworkId(networkId);

		for (int i = 0; i < stockPrefArray.length(); i++) {
			org.json.JSONObject stockPrefDataObj = (org.json.JSONObject) stockPrefArray.get(i);

			Stock stock = new Stock();
			stock.setNetworkId(networkId);
			stock.setNodeId(Integer.parseInt(stockPrefDataObj.getString("Node")));
			stock.setCardType(stockPrefDataObj.getString("Type"));
			stock.setQuantity(Integer.parseInt(stockPrefDataObj.getString("Quantity")));
			stock.setCategory(stockPrefDataObj.getString("Category"));
			stock.setUsedQuantity(0);
			stock.setStatus("");

			String serialNoArr[] = stockPrefDataObj.getString("Serial No (Optional)").split(",");
			for (String sNum : serialNoArr) {
				stock.setSerialNo(sNum);
				dbService.getStockService().Insert(stock);
			}

		}

		return MapConstants.SUCCESS;
	}

	/**
	 * This Mapping Called on request of Save Parametric Preference.. It further
	 * dispatch the service to their respective preference save controller.
	 * 
	 * @author avinash
	 * @return void
	 * @throws SQLException
	 */
	@RequestMapping(value = "/saveInventoryPriorityPref", method = RequestMethod.POST, headers = "Accept=application/json", /*
	 * consumes
	 * =
	 * "application/json",
	 */ produces = "application/json")
	@ResponseBody
	public int saveInventoryPreference(@RequestBody String jsonStr) throws SQLException {

		System.out.println("Inside saveInventoryPreference and Going to Save data using " + jsonStr);

		/** Map From Common API */
		HashMap<String, Object> networkInfoMap = MapWebCommonAPIs.NestedStaticClass.getNetworkInformation(dbService,
				jsonStr);
		int networkId = (int) networkInfoMap
				.get(MapConstants.NetworkId); /** Network Id for common use */

		org.json.JSONObject jsonReqDataObject = new org.json.JSONObject(jsonStr);
		org.json.JSONArray inventoryPrefArray = (org.json.JSONArray) jsonReqDataObject.get("InventoryPreferences");

		org.json.JSONObject Obj = (org.json.JSONObject) inventoryPrefArray.get(0);
		int nodeId = Integer.parseInt(Obj.getString("Node"));
		String category = Obj.getString("Category");

		// Node data delete case
		if (inventoryPrefArray.length() > 1) {
			dbService.getEquipmentPreferenceService().DeleteNodePref(networkId, nodeId);
		}
		// Category Specific for Node Delete
		else {
			dbService.getEquipmentPreferenceService().DeleteNodeCategoryPref(networkId, nodeId, category);
		}

		for (int i = 0; i < inventoryPrefArray.length(); i++) {
			org.json.JSONObject invPrefDataObj = (org.json.JSONObject) inventoryPrefArray.get(i);

			EquipmentPreference eqPrefObj = new EquipmentPreference();
			eqPrefObj.setNetworkId(networkId);
			eqPrefObj.setNodeId(Integer.parseInt(invPrefDataObj.getString("Node")));
			eqPrefObj.setRedundancy(invPrefDataObj.getString("Redundancy"));
			eqPrefObj.setCategory(invPrefDataObj.getString("Category"));

			org.json.JSONArray cardTypeArr = invPrefDataObj.getJSONArray("Type");
			for (int c = 0; c < cardTypeArr.length(); c++) {
				eqPrefObj.setCardType(cardTypeArr.get(c).toString().trim());
				eqPrefObj.setPreference(c + 1);
				dbService.getEquipmentPreferenceService().Insert(eqPrefObj);
			}
		}

		return MapConstants.SUCCESS;
	}
	
	
	/**
	 * This Mapping Called on request of reset inventory Preference.. It further
	 * dispatch the service to their respective preference save controller.
	 * 
	 * @author avinash
	 * @return void
	 * @throws SQLException
	 */
	@RequestMapping(value = "/resetInventoryPriorityPref", method = RequestMethod.POST, headers = "Accept=application/json", /*
	 * consumes
	 * =
	 * "application/json",
	 */ produces = "application/json")
	@ResponseBody
	public JSONArray resetInventoryPreference(@RequestBody String jsonStr) throws SQLException {

		System.out.println("Inside saveInventoryPreference and Going to Save data using " + jsonStr);

		/** Map From Common API */
		HashMap<String, Object> networkInfoMap = MapWebCommonAPIs.NestedStaticClass.getNetworkInformation(dbService,
				jsonStr);
		int networkId = (int) networkInfoMap
				.get(MapConstants.NetworkId); /** Network Id for common use */
		
		org.json.JSONObject jsonReqDataObject = new org.json.JSONObject(jsonStr);
		String requestString=jsonReqDataObject.getString("Type");
		
		EquipmentPreferences eq=new EquipmentPreferences(dbService);
		
		try {
			if(requestString.equals("All")) {
				dbService.getEquipmentPreferenceService().DeleteByNetworkId(networkId);
				eq.InitializeEqPreferenceDb(networkId);
			}else {
				int nodeid=Integer.parseInt(requestString);
				int nodetype=dbService.getNodeService().FindNode(networkId, nodeid).getNodeType();
				dbService.getEquipmentPreferenceService().DeleteByNetworkId(networkId,nodeid);
				eq.fInsertEquipmentPreferencesDb(networkId, nodeid, nodetype);
			}				
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		
		/***************Default Prefs************************/ 
		JSONArray defaultPrefArr=new JSONArray();		
		List<EquipmentPreference> eqPrefList=dbService.getEquipmentPreferenceService().FindAll(networkId);
		for(EquipmentPreference obj:eqPrefList){
			JSONObject eqPrefObj=new JSONObject();
			eqPrefObj.put("NetworkId",obj.getNetworkId());
			eqPrefObj.put("NodeId", obj.getNodeId());
			eqPrefObj.put("Category", obj.getCategory());
			eqPrefObj.put("CardType", obj.getCardType());
			eqPrefObj.put("Priority", obj.getPreference());
			eqPrefObj.put("Redundancy", obj.getRedundancy());	
			defaultPrefArr.add(eqPrefObj);
		}
				
		return defaultPrefArr;
	}

	/**
	 * This Mapping Called on request of get Preferences for Network.. * It
	 * further dispatch the service to their respective preference save
	 * controller.
	 * 
	 * @author avinash
	 * @return void
	 * @throws SQLException
	 */
	@RequestMapping(value = "/getNetworkPreferences", method = RequestMethod.POST, headers = "Accept=application/json", /*
	 * consumes
	 * =
	 * "application/json",
	 */ produces = "application/json")
	@ResponseBody
	public JSONObject getNetworkPreferences(@RequestBody String jsonStr) throws SQLException {

		System.out.println("Inside getNetworkPreferences and Going to get data for " + jsonStr);
		JSONObject resultJson = null;

		MapWebGetPreferences mapWebGetPreferences = new MapWebGetPreferences();
		resultJson = mapWebGetPreferences.getNetworkPreferences(jsonStr, dbService);

		return resultJson;
	}

	/**
	 * This Mapping Called on request of Mpn NodeId list .
	 * 
	 * @author avinash
	 * @return void
	 * @throws SQLException
	 * @throws UnknownHostException
	 */
	@RequestMapping(value = "/getMpnNodes", method = RequestMethod.POST, headers = "Accept=application/json", /*
	 * consumes
	 * =
	 * "application/json",
	 */ produces = "application/json")
	@ResponseBody
	public JSONObject getMonNodesList(@RequestBody String jsonStr) throws SQLException, UnknownHostException {

		// System.out.println("Inside getDetailedCardConfiguration and Going to
		// get circuit data .. with "+ jsonStr);

		JSONObject resultJson = null;
		MapWebDetailedConfiguration mapWebDetailedConfiguration = new MapWebDetailedConfiguration();
		resultJson = mapWebDetailedConfiguration.getMpnNodes(dbService, jsonStr);

		System.out.println(resultJson.toString());
		return resultJson;

	}
	
	
	@RequestMapping(value = "/updateSchema", method = RequestMethod.GET)
	@ResponseBody
	public String updateSqlTable() {
		
		String errorString="Error : Table/Schema not changed";
		String successString="Table/Schema Updated SuccessFully";

		System.out.println("Inside UpdateSchema ....");
		String sql= "ALTER TABLE `lambdalspinformation` \n" + 
				"ADD COLUMN `ForwardingAdj` INT(11) NULL AFTER `LspId`;\n";
		int responseCode = dbService.getNetworkService().UpdateSchema(sql,"lambdalspinformation");
		if(responseCode != MapConstants.SUCCESS)
			return errorString;
		
		/** Add PathLinkId after Path in NR table **/
		sql= "ALTER TABLE `NetworkRoute` \n" + 
				"ADD COLUMN `PathLinkId` VARCHAR(100) NULL AFTER `Path`;\n";
		responseCode = dbService.getNetworkService().UpdateSchema(sql,"NetworkRoute");
		
		if(responseCode!=MapConstants.SUCCESS)
			return errorString; 
		
		sql= "ALTER TABLE `NetworkRoute` \n" + 
				"ADD COLUMN `WavelengthSwitchFlag` VARCHAR(100) NULL AFTER `WavelengthNo`;\n";
		responseCode = dbService.getNetworkService().UpdateSchema(sql,"NetworkRoute");
		
		if(responseCode!=MapConstants.SUCCESS)
			return errorString; 
		
		sql= "ALTER TABLE `NetworkRoute` \n" + 
				"ADD COLUMN `WavelengthSet` VARCHAR(100) NULL AFTER `WavelengthNo`;\n";
		responseCode = dbService.getNetworkService().UpdateSchema(sql,"NetworkRoute");
		
		if(responseCode!=MapConstants.SUCCESS)
			return errorString; 
		 
		
		/** Add LinkType after Direction in Links table **/
		sql= "ALTER TABLE `Link` \n" + 
				"ADD COLUMN `LinkType` VARCHAR(100) NOT NULL DEFAULT 'DEFAULT (PA/BA)' AFTER `Direction`;\n";
		responseCode = dbService.getNetworkService().UpdateSchema(sql,"Link");
		if(responseCode != MapConstants.SUCCESS)
			return errorString;
		
		/** Update Spanlength type to float **/
		sql= "ALTER TABLE Link\r\n" + 
				"MODIFY COLUMN Length FLOAT; ";
		responseCode = dbService.getNetworkService().UpdateSchema(sql,"Link");
		if(responseCode != MapConstants.SUCCESS)
			return errorString;
		
		/** Add MpnPortNo after Direction in Ports table **/
		sql= "ALTER TABLE `Ports` \n" + 
				"ADD COLUMN `MpnPortNo` INT(11) NOT NULL DEFAULT '0' AFTER `Direction`;\n";
		responseCode = dbService.getNetworkService().UpdateSchema(sql,"Ports");
		if(responseCode != MapConstants.SUCCESS)
			return errorString;
		
		/** Add MpnPortNo after Direction in Ports table **/
		sql= "ALTER TABLE `Circuit` \n" + 
				"ADD COLUMN `QoS` INT(11) NOT NULL DEFAULT '0' AFTER `CircuitId`;\n";
		responseCode = dbService.getNetworkService().UpdateSchema(sql,"Circuit");
		if(responseCode != MapConstants.SUCCESS)
			return errorString;

		/** Update Views **/
		dbService.getNetworkRouteService().UpdateRouteMappingView();
		dbService.getCircuit10gAggService().UpdateAggClientMappingView();
				
		
		sql = "ALTER TABLE OtnLspInformation \n" + 
				"ADD COLUMN `ForwardingAdj` INT(11) NOT NULL DEFAULT '0'  AFTER `LspId`;\n" ;
		responseCode = dbService.getNetworkService().UpdateSchema(sql,"OtnLspInformation");
		
		
		sql="ALTER TABLE CircuitInMemoryTable \n" + 
				"ADD COLUMN `QoS` INT(11) NOT NULL DEFAULT '0' AFTER `CircuitId`;\n" ;
		responseCode = dbService.getNetworkService().UpdateSchema(sql,"CircuitInMemoryTable");
						
		return successString;

	}
	
	
	
	
	/**
	 * This Mapping Called on request from EMS .
	 * 
	 * @author surya
	 * @return JSONArray
	 * @throws SQLException.IOException
	 */
	 @CrossOrigin(origins = "*")
	@RequestMapping(value = "/xml_ControlPath", headers = "Content-Type= multipart/form-data", method = RequestMethod.POST, consumes = "multipart/form-data")
	@ResponseBody
	public JSONArray xmlreversepath(@RequestParam(value = "file", required = true) MultipartFile file) throws SQLException,IOException{

        
		HashMap<Integer,Object> DifferencearrayforDataPath = new  HashMap<Integer,Object> ();
		ArrayList<HashMap<Integer,Object>> DifferencearrayforControlPath = new ArrayList<HashMap<Integer,Object>> ();
		UnTarFile Untar = new UnTarFile();
		JSONArray FinalJSONArray = new JSONArray();
		JSONArray JSONA  = new JSONArray();
		JSONObject json = new JSONObject();
		MainMap.logger.info("filename.getName() " + file.getOriginalFilename());

		String home = System.getProperty("user.home");
		//String path2 = home+"/Downloads/ConfigPathFiles/ReversePath/";
		
		//Creating Directories if not present
		
		java.io.File ConfigPathFiles = new File(home+"/Downloads/ConfigPathFiles");
		
		if (!ConfigPathFiles.exists()) {
			if (ConfigPathFiles.mkdir()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
			}
		}
		
		
		java.io.File ReversePath = new File(ConfigPathFiles.getAbsolutePath()+"/ReversePath");
		if (!ReversePath.exists()) {
			if (ReversePath.mkdir()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
			}
		}
		
		java.io.File ControlPathFiles = new File(ReversePath.getAbsolutePath()+"/ControlPathFiles");
		if (!ControlPathFiles.exists()) {
			if (ControlPathFiles.mkdir()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
			}
		}
		
		java.io.File TAR = new File(ControlPathFiles.getAbsolutePath()+"/TAR");
		if (!TAR.exists()) {
			if (TAR.mkdir()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
			}
		}
		
		
		java.io.File XML = new File(ControlPathFiles.getAbsolutePath()+"/XML");
		if (!XML.exists()) {
			if (XML.mkdir()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
			}
		}
				
		
		
		
		
		//Storing File received from EMS to Local directory
		
		File path = new File(home+"/Downloads/ConfigPathFiles/ReversePath/ControlPathFiles/");
		
		try {

			File convFile = new File(file.getOriginalFilename());
			convFile.createNewFile();

			FileOutputStream fos = new FileOutputStream(path+"/"+convFile); 
			fos.write(file.getBytes());
			fos.close(); 

		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		
		
		
		//Untarring tar file received from EMS
		String Path = Untar.UnTarfile(file.getOriginalFilename(),"ControlPathFiles");
        
		
		//Processing each ControlPath file in Directory and Updating Changes to Database
		File folder = new File(Path);
		File[] listOfFiles = folder.listFiles();
		for(int j=0; j<listOfFiles.length;j++)	
		{  
			FinalJSONArray.add( ControlPath.parsingXML(listOfFiles[j].getName(),Path,dbService)); 

		}
		
		
		json.put("difference", FinalJSONArray);
		String Path1 = home + "/Downloads/ConfigPathFiles/ReversePath/ControlPathFiles";

        //Writing changes in a File
		try
		{
			FileWriter text = new FileWriter(Path1+"/"+"file.json") ;
			text.write(json.toJSONString());
			
			text.close();
		}   
		finally
		{    }
		
		
		return FinalJSONArray;




	}
	
	 @CrossOrigin(origins = "*")
		@RequestMapping(value = "/xml_DataPath", headers = "Content-Type= multipart/form-data", method = RequestMethod.POST, consumes = "multipart/form-data")
		@ResponseBody
		public JSONArray xmlreversepath_Datapath(@RequestParam(value = "file", required = true) MultipartFile file) throws SQLException,IOException{

	        
			HashMap<Integer,Object> DifferencearrayforDataPath = new  HashMap<Integer,Object> ();
			ArrayList<HashMap<Integer,Object>> DifferencearrayforControlPath = new ArrayList<HashMap<Integer,Object>> ();
			UnTarFile Untar = new UnTarFile();
			JSONArray FinalJSONArray = new JSONArray();
			JSONArray JSONA  = new JSONArray();
			JSONObject json = new JSONObject();
			System.out.println("filename.getName() " + file.getOriginalFilename());

			String home = System.getProperty("user.home");
			//String path2 = home+"/Downloads/ConfigPathFiles/ReversePath/";
			
			java.io.File ConfigPathFiles = new File(home+"/Downloads/ConfigPathFiles");
			System.out.println(home+"/Downloads/ConFigFiles" +!ConfigPathFiles.exists());
			
			
			if (!ConfigPathFiles.exists()) {
				if (ConfigPathFiles.mkdir()) {
					System.out.println("Directory is created!");
				} else {
					System.out.println("Failed to create directory!");
				}
			}
			
			
			java.io.File ReversePath = new File(ConfigPathFiles.getAbsolutePath()+"/ReversePath");
			if (!ReversePath.exists()) {
				if (ReversePath.mkdir()) {
					System.out.println("Directory is created!");
				} else {
					System.out.println("Failed to create directory!");
				}
			}
			
			java.io.File DataPathFiles = new File(ReversePath.getAbsolutePath()+"/DataPathFiles");
			if (!DataPathFiles.exists()) {
				if (DataPathFiles.mkdir()) {
					System.out.println("Directory is created!");
				} else {
					System.out.println("Failed to create directory!");
				}
			}
			
			java.io.File TAR = new File(DataPathFiles.getAbsolutePath()+"/TAR");
			if (!TAR.exists()) {
				if (TAR.mkdir()) {
					System.out.println("Directory is created!");
				} else {
					System.out.println("Failed to create directory!");
				}
			}
			
			
			java.io.File XML = new File(DataPathFiles.getAbsolutePath()+"/XML");
			if (!XML.exists()) {
				if (XML.mkdir()) {
					System.out.println("Directory is created!");
				} else {
					System.out.println("Failed to create directory!");
				}
			}
					
			
			
	       File path = new File(home+"/Downloads/ConfigPathFiles/ReversePath/DataPathFiles/");
			
			try {

				File convFile = new File(file.getOriginalFilename());
				convFile.createNewFile();

				FileOutputStream fos = new FileOutputStream(path+"/"+convFile); 
				fos.write(file.getBytes());
				fos.close(); 

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(file.getName());
			String Path = Untar.UnTarfile(file.getOriginalFilename(),"DataPathFiles"); //1 is for Datapath files

			//String Path = home + "/Downloads/ConfigPathFiles/ReversePath/Output/XML";
			File folder = new File(Path);
			File[] listOfFiles = folder.listFiles();
			DataPath datapath = new DataPath();
			for(int j=0; j<listOfFiles.length;j++)	
			{  
				FinalJSONArray.add(datapath.parsingDataPathXML(dbService,listOfFiles[j].getName(),Path)); 
				
			}
				
			
			/*for(int k=0;k<FinalJSONArray.size();k++)
			{   
				System.out.println("JSON"+FinalJSONArray.get(k));
			}

			json.put("difference", FinalJSONArray);


			try
			{
				FileWriter text = new FileWriter(Path+"/"+"file2.json") ;
				text.write(json.toJSONString());
				System.out.println(text.toString().length());
				text.close();
			}   
			finally
			{


			}*/
			
			
			
			/*
			 */


			return FinalJSONArray;




		}


	
	
	 
	 
/**
	 * This Mapping is reserve for the Testing Purpose from UI Under Help
	 * Sub-menu Call
	 * 
	 * @return
	 * @throws NumberFormatException
	 * @throws SQLException
	 */
	@RequestMapping(value = "/testingMenuForDeveloperTrigger", method = RequestMethod.GET)
	@ResponseBody
	public int testingForDeveloperTrigger() throws NumberFormatException, SQLException {

		int responseCode = 1; /**
								 * Call to the Testing API here and Mark the
								 * responseCode
								 */
		System.out.println(" testingForDeveloperTrigger().......................");

		ResourcePlanning rc = new ResourcePlanning(dbService);
		ResourcePlanUtils rpu = new ResourcePlanUtils(dbService);
		// GenerateLinkWavelengthMap linkwmap = new
		// GenerateLinkWavelengthMap(dbService);

		// ResourcePlanning rc = new ResourcePlanning(dbService);
		// GenerateLinkWavelengthMap linkwmap = new
		// GenerateLinkWavelengthMap(dbService);

		// rc.getDbService().getCardInfoService().DeleteAllCardInfo();
		// MainMap.logger.info(" Assigning System:");
		// dbService.getCardInfoService().DeleteAllCardInfo();
		// //Db data insertion for Chassis View
		// rc.fInitAllNodesInNetwork(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
		// rc.fAssignPairedMuxponderCards(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
		// rc.fAssignMPCs(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
		// rc.fAssignOLPs(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
		//
		// dbService.getInventoryService().DeleteInventory(1);
		// rc.fGenerateInventory(1);
		//

		// dbService.getLinkWavelengthService().DeleteByNetworkId(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
		// linkwmap.fgenerateLinkWavelengthMap(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
		//
		// dbService.getPatchCordService().DeletePatchCordInfo(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
		// rc.fAssignPatchCords(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));

		// pr.generateNodeTabledata();

		// dbService.getLinkWavelengthService().DeleteByNetworkId(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
		// linkwmap.fgenerateLinkWavelengthMap(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
		//
		// dbService.getPatchCordService().DeletePatchCordInfo(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
		// rc.fAssignPatchCords(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
		// System.out.println("MapWebMainController.PerNodeLinkData_cf()"
		// +dbService.getLinkService().PerNodeLinkData_cf(1, 1).toString());
		// System.out.println("MapWebMainController.FindRoutesWithNodeEnds_cf()"
		// +dbService.getNetworkRouteService().FindRoutesWithNodeEnds_cf(1, 3));
		// dbService.getCardInfoService().CreateViewAllCardInfo(1);

		// System.out.println("MapWebMainController.FgetTpnDataPerCircuit_cf()"
		// +dbService.getCardInfoService().FgetTpnDataPerCircuit_cf(1, 1));
		// System.out.println("MapWebMainController.FindIpOfSrcNodeEnd()"
		// +dbService.getIpSchemeLinkService().FindIpOfSrcNodeEnd_cf(1,1,2));
		// System.out.println("MapWebMainController.FindCountWorkingMpns()"
		// +dbService.getCardInfoService().FindCountWorkingMpns(1,1));
		// System.out.println("MapWebMainController.getLinkWavelengthInfoService()"
		// +dbService.getLinkWavelengthInfoService().FindNodeWiseAllLambdaInfo(1,1));
		//
		////////////////////////// Testing sunaina
		// ResourcePlanning rc = new ResourcePlanning(dbService);
		// InitializeConfigurationData cd = new
		// InitializeConfigurationData(dbService);
		// int networkidBF =
		// Integer.parseInt(dbService.getNetworkService().GetBrownFieldNetworkId(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString())).toString());
		// //deleting all previous card information
		// dbService.getCardInfoService().DeleteAllCardInfo(networkidBF);
		// //deleting all previous port information
		// dbService.getPortInfoService().DeleteAllPortInfo(networkidBF);
		// cd.fDeleteConfigurationData(networkidBF);
		//
		// rc.fPopulateBrFieldCardPortInfo(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
		// rc.fsetDefaultCardPreferenceBrField(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
		// rc.fPopulateBrFieldMcsMapInfo(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
		//
		// rc.fInitNodesInNetworkBrField(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
		// rc.fAssignPairedMuxponderCardsBrField(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
		// rc.fAssignRegeneratorsBrField(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
		// rc.fAssignMPCsBrField(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
		// rc.fAssignOLPsBrField(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
		// rc.fAssignMPCsBrField(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
		// rc.fassignDCMTrayBrField(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
		// rc.fAssignMPCsBrField(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
		// rc.fAssignYcablesBrField(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
		// rc.fAssignPortsOnCardBrField(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
		//
		//
		// //create view for cardinfo
		// dbService.getCardInfoService().CreateViewAllCardInfo(networkidBF);
		// //create view for portinfo
		// dbService.getPortInfoService().CreateViewAllPortInfo(networkidBF);
		//
		// //patch cord generation
		// dbService.getPatchCordService().DeletePatchCordInfo(networkidBF);
		// rc.fAssignPatchCords(networkidBF);
		//
		//
		// GenerateLinkWavelengthMap linkwmap = new
		// GenerateLinkWavelengthMap(dbService);
		// dbService.getLinkWavelengthService().DeleteByNetworkId(networkidBF);
		// dbService.getLinkWavelengthInfoService().DeleteByNetworkId(networkidBF);
		// linkwmap.fgenerateLinkWavelengthInformation(networkidBF);
		//
		// dbService.getInventoryService().DeleteInventory(networkidBF);
		// rc.fGenerateInventory(networkidBF);
		// rc.getDbService().getViewServiceRp().FindBoM(networkidBF).toString();
		//
		// GenerateMcsMapping gmm = new GenerateMcsMapping(dbService, rpu);
		// gmm.fgenerateMcsMapBrField(Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString()));
		//
		// cd.fInitializeConfigurationData(networkidBF);
		System.out.println("MapWebMainController.testingForDeveloperTrigger() 1,1 "
				+ dbService.getCardInfoService().CheckIfTableExists(1, 1));
		System.out.println("MapWebMainController.testingForDeveloperTrigger() 1,4 "
				+ dbService.getCardInfoService().CheckIfTableExists(1, 4));

		System.out.println("MapWebMainController.testingForDeveloperTrigger() FindCommonCardsInBrField "
				+ dbService.getCardInfoService().FindCommonCardsInBrField(1, 2, 4));

		/////////////////////////////////////////
		if (responseCode == 1)
			return MapConstants.SUCCESS;
		else
			return MapConstants.FAILURE;
	}

	/**
	 * This Mapping is reserve for the Testing Purpose from UI
	 * 
	 * @return
	 * @throws SQLException
	 */

	@RequestMapping("/saveTest")

	public String testMethod() throws SQLException {

		System.out.println("Now Testing");

List<Network> allNetwork=dbService.getNetworkService().FindAllNetworks();
		


allNetwork.forEach(network->{
	int networkId=network.getNetworkId();
		List<NetworkRoute> allPaths=dbService.getNetworkRouteService().FindAllByNetworkId(networkId);
		
		allPaths.forEach(route->{
			if(route.getPathLinkId()==null) {
				System.out.println("networkId ::"+networkId);
				String path=route.getPath();
				String[] pathArr=path.split(",");
				String linkSet="";
				for (int i = 0; i < pathArr.length-1; i++) {			
					int srcNode=Integer.parseInt(pathArr[i]);
					int destNode=Integer.parseInt(pathArr[i+1]);
					Link link=dbService.getLinkService().FindLink(networkId, srcNode, destNode);
					if(i==pathArr.length-2)
					linkSet=linkSet.concat(String.valueOf(link.getLinkId()));
					else linkSet=linkSet.concat(String.valueOf(link.getLinkId())+",");
				}
				dbService.getNetworkRouteService().UpdateLinkSet(networkId, route.getRoutePriority(), route.getPath(), route.getWavelengthNo(),
						route.getDemandId(), linkSet);
			}		
			
		});
});

		/*
		 * System.out.
		 * println("MapWebJsonParser.jsonParse()****************calling parser"
		 * ); Network network1 = new
		 * Network(0,"mynetwork1","Mesh","area1","service1"); Network network2 =
		 * new Network(0,"mynetwork2","Mesh","area1","service1"); Network
		 * network3 = new Network(0,"mynetwork3","Mesh","area1","service1");
		 * 
		 * System.out.println("MapWebMainController.testMethod()......Checking "
		 * ); if(dbService.getNetworkService().GetByNetworkName("mynetwork1")!=
		 * null) { System.out.println(" Network Found...."); } else {
		 * System.out.println(" Network NOT Found...."); }
		 * 
		 * try { dbService.getNetworkService().InsertNetwork(network1);
		 * dbService.getNetworkService().InsertNetwork(network2);
		 * dbService.getNetworkService().InsertNetwork(network3); } catch
		 * (SQLException e1) { // TODO Auto-generated catch block
		 * e1.printStackTrace(); } Node node11 = new Node(1,1,"node1station",
		 * "node1site",2,1,3,"192.168.8.101",0,500,"255.0.0.0","192.168.8.1",
		 * "ipv6","eightychannel",1,"reach"); Node node12 = new
		 * Node(1,2,"node2station",
		 * "node2site",2,1,2,"192.168.8.101",0,500,"255.0.0.0","192.168.8.1",
		 * "ipv6","eightychannel",1,"reach"); Node node13 = new
		 * Node(1,3,"node3station",
		 * "node3site",2,1,2,"192.168.8.101",0,500,"255.0.0.0","192.168.8.1",
		 * "ipv6","eightychannel",1,"reach"); Node node14 = new
		 * Node(1,4,"fromparser",
		 * "fromparser",2,1,2,"192.168.8.101",0,500,"255.0.0.0","192.168.8.1",
		 * "ipv6","eightychannel",1,"reach"); Node node21 = new
		 * Node(2,1,"fromparser",
		 * "fromparser",2,1,2,"192.168.8.101",0,500,"255.0.0.0","192.168.8.1",
		 * "ipv6","eightychannel",1,"reach"); Node node22 = new
		 * Node(2,2,"fromparser",
		 * "fromparser",2,1,2,"192.168.8.101",0,500,"255.0.0.0","192.168.8.1",
		 * "ipv6","eightychannel",1,"reach"); Node node31 = new
		 * Node(3,1,"fromparser",
		 * "fromparser",2,1,2,"192.168.8.101",0,500,"255.0.0.0","192.168.8.1",
		 * "ipv6","eightychannel",1,"reach");
		 * 
		 * 
		 * try { dbService.getNodeService().InsertNode(node11);
		 * dbService.getNodeService().InsertNode(node12);
		 * dbService.getNodeService().InsertNode(node13);
		 * dbService.getNodeService().InsertNode(node21);
		 * dbService.getNodeService().InsertNode(node22);
		 * dbService.getNodeService().InsertNode(node31);
		 * dbService.getNodeService().InsertNode(node14);
		 * 
		 * } catch (SQLException e) {
		 * 
		 * e.printStackTrace(); }
		 * 
		 * 
		 * Link link11 = new
		 * Link(1,1,1,2,5,100,800,72,1,1,1,1,1,1,1,1,1,1,2,1,2,"1","2","No");
		 * Link link23 = new
		 * Link(1,2,3,2,5,100,50,72,1,1,1,1,1,1,1,1,1,1,3,1,3,"1","2","No");
		 * Link link21 = new
		 * Link(2,1,1,2,5,100,600,82,1,1,1,1,1,1,1,1,1,1,1,1,1,"1","2","No");
		 * Link link13 = new
		 * Link(1,3,1,3,5,100,600,82,1,1,1,1,1,1,1,1,1,1,1,1,1,"1","2","No");
		 * Link link24 = new
		 * Link(1,4,2,4,5,100,600,82,1,1,1,1,1,1,1,1,1,1,1,1,1,"1","2","No");
		 * Link link43 = new
		 * Link(1,5,4,3,5,100,600,82,1,1,1,1,1,1,1,1,1,1,1,1,1,"1","2","No");
		 * 
		 * try { dbService.getLinkService().InsertLink(link11);
		 * System.out.println("MapWebMainController.testMethod()check1 ");
		 * dbService.getLinkService().InsertLink(link23);
		 * System.out.println("MapWebMainController.testMethod()check2 ");
		 * dbService.getLinkService().InsertLink(link21);
		 * System.out.println("MapWebMainController.testMethod()check3 ");
		 * dbService.getLinkService().InsertLink(link13);
		 * dbService.getLinkService().InsertLink(link24);
		 * dbService.getLinkService().InsertLink(link43); } catch (SQLException
		 * e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 * 
		 * Topology topologyNode1 = new
		 * Topology(Integer.parseInt(dbService.getNetworkService().
		 * GetByNetworkName("mynetwork1").toString()),node11.getNodeId(),node12.
		 * getNodeId(),0,0,0,0,0,0,0); try {
		 * dbService.getTopologyService().InsertTopology(topologyNode1); } catch
		 * (SQLException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 * System.out.println("Testing the topology.....");
		 * dbService.getTopologyService().QueryTopologyWithNodeType();
		 * 
		 * System.out.
		 * println("MapWebJsonParser.jsonParse()...before delete...network");
		 * List <Network> networkAll=dbService.getNetworkService().FindAll();
		 * System.out.println("Networks"); for (int i = 0; i <
		 * networkAll.size(); i++) {
		 * 
		 * System.out.println(""+ networkAll.get(i).toString()); } List <Node>
		 * nodesAll=dbService.getNodeService().FindAll(1);
		 * System.out.println("Nodes"); for (int i = 0; i < nodesAll.size();
		 * i++) { System.out.println(""+ nodesAll.get(i).toString()); }
		 * 
		 * List <Link> linkAll=dbService.getLinkService().FindAll();
		 * System.out.println("Links"); for (int i = 0; i < linkAll.size(); i++)
		 * { System.out.println(""+ linkAll.get(i).toString()); }
		 * 
		 * List <Topology> topologyAll=dbService.getTopologyService().FindAll();
		 * System.out.println("Topology"); for (int i = 0; i <
		 * topologyAll.size(); i++) { System.out.println(""+
		 * topologyAll.get(i).toString()); }
		 * 
		 * dbService.getNetworkService().DeleteNetwork(Integer.parseInt(
		 * dbService.getNetworkService().GetByNetworkName("mynetwork2").toString
		 * ()));
		 * 
		 * System.out.
		 * println("MapWebJsonParser.jsonParse()...after delete...network");
		 * 
		 * List <Network> networkAll1=dbService.getNetworkService().FindAll();
		 * System.out.println("Network"); for (int i = 0; i <
		 * networkAll1.size(); i++) { System.out.println(""+
		 * networkAll1.get(i).toString()); } List <Node>
		 * nodesAll1=dbService.getNodeService().FindAll(1);
		 * System.out.println("Node"); for (int i = 0; i < nodesAll1.size();
		 * i++) { System.out.println(""+ nodesAll1.get(i).toString()); }
		 * 
		 * List <Link> linkAll1=dbService.getLinkService().FindAll();
		 * System.out.println("Link"); for (int i = 0; i < linkAll1.size(); i++)
		 * { System.out.println(""+ linkAll1.get(i).toString()); }
		 * 
		 * List <Topology>
		 * topologyAll1=dbService.getTopologyService().FindAll();
		 * System.out.println("Topology"); for (int i = 0; i <
		 * topologyAll1.size(); i++) { System.out.println(""+
		 * topologyAll1.get(i).toString()); }
		 * 
		 * Circuit circuit1 = new
		 * Circuit(1,1,1,2,10,"4","1+1+R","1","1",SMConstants.DisjointStr,
		 * MapConstants.Line100G,"N0"); Circuit circuit2 = new
		 * Circuit(1,2,1,2,10,"4","1+1+R","1","1",SMConstants.DisjointStr,
		 * MapConstants.Line100G,"N0"); Circuit circuit3 = new
		 * Circuit(1,3,1,2,10,"4","1+1+R","1","1",SMConstants.DisjointStr,
		 * MapConstants.Line100G,"N0");
		 * 
		 * Circuit circuit4 = new
		 * Circuit(1,4,1,3,10,"4","1+1+R","1","1",SMConstants.DisjointStr,
		 * MapConstants.Line100G,"N0"); Circuit circuit5 = new
		 * Circuit(1,5,1,4,10,"4","1+1+R","1","1",SMConstants.DisjointStr,
		 * MapConstants.Line100G,"N0"); Circuit circuit6 = new
		 * Circuit(1,6,1,4,10,"3","1+1+R","1","1",SMConstants.DisjointStr,
		 * MapConstants.Line100G,"N0"); Circuit circuit7 = new
		 * Circuit(1,7,1,4,10,"3","1+1+R","1","1",SMConstants.DisjointStr,
		 * MapConstants.Line100G,"N0");
		 * 
		 * Circuit circuit8 = new
		 * Circuit(1,3,1,2,10,"4","1+1+R","1","1",SMConstants.DisjointStr,
		 * MapConstants.Line100G,"N0"); Circuit circuit9 = new
		 * Circuit(1,3,1,2,10,"4","1+1+R","1","1",SMConstants.DisjointStr,
		 * MapConstants.Line100G,"N0"); Circuit circuit10 = new
		 * Circuit(1,3,1,2,10,"4","1+1+R","1","1",SMConstants.DisjointStr,
		 * MapConstants.Line100G,"N0"); Circuit circuit11 = new
		 * Circuit(1,3,1,2,10,"4","1+1+R","1","1",SMConstants.DisjointStr,
		 * MapConstants.Line100G,"N0"); Circuit circuit12 = new
		 * Circuit(1,3,1,2,10,"4","1+1+R","1","1",SMConstants.DisjointStr,
		 * MapConstants.Line100G,"N0"); Circuit circuit13 = new
		 * Circuit(1,3,1,2,10,"4","1+1+R","1","1",SMConstants.DisjointStr,
		 * MapConstants.Line100G,"N0"); Circuit circuit14 = new
		 * Circuit(1,3,1,2,10,"4","1+1+R","1","1",SMConstants.DisjointStr,
		 * MapConstants.Line100G,"N0"); Circuit circuit15 = new
		 * Circuit(1,3,1,2,10,"4","1+1+R","1","1",SMConstants.DisjointStr,
		 * MapConstants.Line100G,"N0");
		 * 
		 * Circuit circuit16 = new
		 * Circuit(1,3,1,2,1,"4","1+1+R","1","1",SMConstants.DisjointStr,
		 * MapConstants.Line100G,""); Circuit circuit17 = new
		 * Circuit(1,3,1,2,1,"4","1+1+R","1","1",SMConstants.DisjointStr,
		 * MapConstants.Line100G,""); Circuit circuit18 = new
		 * Circuit(1,3,1,2,1,"4","1+1+R","1","1",SMConstants.DisjointStr,
		 * MapConstants.Line100G,""); Circuit circuit19 = new
		 * Circuit(1,3,1,2,1,"4","1+1+R","1","1",SMConstants.DisjointStr,
		 * MapConstants.Line100G,""); Circuit circuit20 = new
		 * Circuit(1,3,1,2,1,"4","1+1+R","1","1",SMConstants.DisjointStr,
		 * MapConstants.Line100G,""); Circuit circuit21 = new
		 * Circuit(1,3,1,2,1,"4","1+1+R","1","1",SMConstants.DisjointStr,
		 * MapConstants.Line100G,""); Circuit circuit22 = new
		 * Circuit(1,3,1,2,1,"4","1+1+R","1","1",SMConstants.DisjointStr,
		 * MapConstants.Line100G,""); Circuit circuit23 = new
		 * Circuit(1,3,1,2,1,"4","1+1+R","1","1",SMConstants.DisjointStr,
		 * MapConstants.Line100G,""); Circuit circuit24 = new
		 * Circuit(1,3,1,2,1,"4","1+1+R","1","1",SMConstants.DisjointStr,
		 * MapConstants.Line100G,""); Circuit circuit25 = new
		 * Circuit(1,3,1,2,1,"4","1+1+R","1","1",SMConstants.DisjointStr,
		 * MapConstants.Line100G,""); Circuit circuit26 = new
		 * Circuit(1,3,1,2,1,"4","1+1+R","1","1",SMConstants.DisjointStr,
		 * MapConstants.Line100G,""); Circuit circuit27 = new
		 * Circuit(1,3,1,3,1,"4","1+1+R","1","1",SMConstants.DisjointStr,
		 * MapConstants.Line100G,""); Circuit circuit28 = new
		 * Circuit(1,3,1,3,100,"4","1+1+R","1","1",SMConstants.DisjointStr,
		 * MapConstants.Line100G,""); Circuit circuit29 = new
		 * Circuit(1,3,1,3,100,"4","1+1+R","1","1",SMConstants.DisjointStr,
		 * MapConstants.Line100G,""); Circuit circuit30 = new
		 * Circuit(1,3,1,3,1,"4","1+1+R","1","1",SMConstants.DisjointStr,
		 * MapConstants.Line100G,"");
		 * 
		 * 
		 * try { dbService.getCircuitService().InsertCircuit(circuit1);
		 * dbService.getCircuitService().InsertCircuit(circuit2);
		 * dbService.getCircuitService().InsertCircuit(circuit3);
		 * dbService.getCircuitService().InsertCircuit(circuit4);
		 * dbService.getCircuitService().InsertCircuit(circuit5);
		 * dbService.getCircuitService().InsertCircuit(circuit6);
		 * dbService.getCircuitService().InsertCircuit(circuit7);
		 * dbService.getCircuitService().InsertCircuit(circuit8);
		 * dbService.getCircuitService().InsertCircuit(circuit9);
		 * dbService.getCircuitService().InsertCircuit(circuit10);
		 * dbService.getCircuitService().InsertCircuit(circuit11);
		 * dbService.getCircuitService().InsertCircuit(circuit12);
		 * dbService.getCircuitService().InsertCircuit(circuit13);
		 * dbService.getCircuitService().InsertCircuit(circuit14);
		 * dbService.getCircuitService().InsertCircuit(circuit15);
		 * 
		 * dbService.getCircuitService().InsertCircuit(circuit16);
		 * dbService.getCircuitService().InsertCircuit(circuit17);
		 * dbService.getCircuitService().InsertCircuit(circuit18);
		 * dbService.getCircuitService().InsertCircuit(circuit19);
		 * dbService.getCircuitService().InsertCircuit(circuit20);
		 * dbService.getCircuitService().InsertCircuit(circuit21);
		 * dbService.getCircuitService().InsertCircuit(circuit22);
		 * dbService.getCircuitService().InsertCircuit(circuit23);
		 * dbService.getCircuitService().InsertCircuit(circuit24);
		 * dbService.getCircuitService().InsertCircuit(circuit25);
		 * dbService.getCircuitService().InsertCircuit(circuit26);
		 * dbService.getCircuitService().InsertCircuit(circuit27);
		 * dbService.getCircuitService().InsertCircuit(circuit28);
		 * dbService.getCircuitService().InsertCircuit(circuit29);
		 * dbService.getCircuitService().InsertCircuit(circuit30);
		 * 
		 * } catch (SQLException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 * 
		 * dbService.getNetworkService().FindAll();
		 * dbService.getNodeService().FindAll(1);
		 * dbService.getLinkService().FindAll();
		 * dbService.getTopologyService().FindAll();
		 * 
		 * //circuit processing CircuitProcessing circuitProcessingObj = new
		 * CircuitProcessing();
		 * circuitProcessingObj.fcircuitProcessing(dbService,1);
		 * 
		 * NetworkRoute networkRoute1 = new
		 * NetworkRoute(1,1,1,"1,2,3","disjoint",50,5,"",60,"","",1,2);
		 * NetworkRoute networkRoute2 = new
		 * NetworkRoute(1,1,2,"1,3","disjoint",50,5,"",60,"","",1,2);
		 * NetworkRoute networkRoute3 = new
		 * NetworkRoute(1,2,1,"1,2,4,3","disjoint",50,5,"",60,"","",1,2);
		 * NetworkRoute networkRoute4 = new
		 * NetworkRoute(1,2,2,"1,2,4","disjoint",50,5,"",60,"","",1,2);
		 * 
		 * try {
		 * dbService.getNetworkRouteService().InsertNetworkRoute(networkRoute1);
		 * dbService.getNetworkRouteService().InsertNetworkRoute(networkRoute2);
		 * dbService.getNetworkRouteService().InsertNetworkRoute(networkRoute3);
		 * dbService.getNetworkRouteService().InsertNetworkRoute(networkRoute4);
		 * } catch (SQLException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 * System.out.
		 * println("Printing thr the main service from main controller....................................."
		 * ); dbService.getNetworkService().FindAll();
		 * dbService.getNodeService().FindAll(1);
		 * dbService.getLinkService().FindAll();
		 * dbService.getTopologyService().FindAll();
		 * dbService.getCircuitService().FindAll();
		 * dbService.getDemandService().FindAll();
		 * dbService.getNetworkRouteService().FindAll();
		 * System.out.println("*** the network count is "+dbService.
		 * getNetworkService().Count());
		 * System.out.println("*** the link count is "+dbService.getLinkService(
		 * ).Count());
		 * System.out.println("*** the node count is "+dbService.getNodeService(
		 * ).Count());
		 * System.out.println("*** the topology count is "+dbService.
		 * getTopologyService().Count());
		 * System.out.println("*** the demand count is "+dbService.
		 * getDemandService().Count());
		 * System.out.println("*** the Network Route count is "+dbService.
		 * getNetworkRouteService().Count());
		 * System.out.println("***** the nework id for name mynetwork1 is  "+
		 * dbService.getNetworkService().GetByNetworkName("mynetwork1") );
		 * 
		 * // Object data[][]=topologyProcessing.fFindDegree(dbService); // for
		 * (int i = 0; i < data.length; i++) { //
		 * System.out.println(" Node Id: "+ data[i][0].toString()); //
		 * System.out.println(" Degree: "+ data[i][1].toString()); // // }
		 * 
		 * IpSchemeLink iplink = new IpSchemeLink(1,1,192,234,566); IpSchemeNode
		 * ipnode = new IpSchemeNode(1,192,234,566,4,5,6,7,1,1);
		 * 
		 * dbService.getIpSchemeLinkService().InsertIpSchemeLink(iplink);
		 * dbService.getIpSchemeNodeService().InsertIpSchemeNode(ipnode);
		 * 
		 * List<RouteMapping> mapping =
		 * dbService.getViewService().FindRouteMapping(); for (int i = 0; i <
		 * mapping.size(); i++) {
		 * 
		 * 
		 * System.out.println("FindRouteMapping() "+mapping.get(i).toString());
		 * }
		 * 
		 * List<DemandMapping> mapping1 =
		 * dbService.getViewService().FindDemandMapping(); for (int i = 0; i <
		 * mapping1.size(); i++) {
		 * 
		 * System.out.println("FindDemandMapping() "+mapping1.get(i).toString())
		 * ; }
		 * 
		 * // circuitMatrixService.fInitiatePools(); ResourcePlanning rc = new
		 * ResourcePlanning(dbService);
		 * System.out.println(" Assigning System:"); Equipment[]
		 * equipmentModelObjectArray = null; // Start initiating nodes according
		 * to network routes obtained from RWA
		 * rc.fInitAllNodesInNetwork(Integer.parseInt(dbService.
		 * getNetworkService().GetByNetworkName("mynetwork1").toString()));
		 * //rc.fGenerateEquipmentDb(equipmentModelObjectArray);
		 * //rc.fGenerateInventory(Integer.parseInt(dbService.getNetworkService(
		 * ).GetByNetworkName("mynetwork1").toString()));
		 * rc.fAssignPairedMuxponderCards(Integer.parseInt(dbService.
		 * getNetworkService().GetByNetworkName("mynetwork1").toString()));
		 * rc.fScanRoutes(Integer.parseInt(dbService.getNetworkService().
		 * GetByNetworkName("mynetwork1").toString()));
		 * //rc.getDbService().getViewServiceRp().FindBoM(Integer.parseInt(
		 * dbService.getNetworkService().GetByNetworkName("mynetwork1").toString
		 * ())).toString();
		 * 
		 * 
		 */

		return "Successs Test!!";
	}

}
