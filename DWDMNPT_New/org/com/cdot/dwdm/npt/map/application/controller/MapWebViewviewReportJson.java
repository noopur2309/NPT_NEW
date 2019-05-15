
package application.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.couchbase.client.java.document.json.JsonObject;

import application.MainMap;
import application.constants.MapConstants;
import application.model.Bom;
import application.model.CardInfo;
import application.model.IpSchemeLink;
import application.model.IpSchemeNode;
import application.model.Node;
import application.model.OpticalPowerCdcRoadmAdd;
import application.model.OpticalPowerCdcRoadmDrop;
import application.service.DbService;
import application.service.IPv4;

/**
 * @author rashmi
 *
 *  @brief  This Class provide the API to generate JSON which has three parts of DB as follows:
 *          PART I:   Node Data 
 *          PART II:  Bom[View] Data
 *          PART III: Wavelength Configuration Data 
 *          PART IV:  IP Genetating  for NodeDATA
 *          PART V:   IP Genetating  for LinkDATA
 *          PART VI:  MPN Information 
 *          
 *         Finally return the same object to the Main Cotroller 
 *          
 */
public class MapWebViewviewReportJson
{
	@SuppressWarnings("unchecked")
	public JSONObject MapWebGenerateviewReportJsonReq(String jsonStr, DbService dbService)
	{
		System.out.println("Inside MapWebGenerateWevelengthJsonReq and Going to Return the JSon for wevelength View.. ");
		/**Map From Common API*/ 
		HashMap<String, Object> networkInfoMap = MapWebCommonAPIs.NestedStaticClass.getNetworkInformation(dbService,jsonStr); 
		int networkId= (int)networkInfoMap.get(MapConstants.NetworkId); /**Network Id for common use*/ 
		int greenFieldNetworkId=MapConstants.I_ZERO;

		if(((String)networkInfoMap.get(MapConstants.NetworkType)).equalsIgnoreCase(MapConstants.BrownField)){ 
			greenFieldNetworkId=(int)networkInfoMap.get(MapConstants.GreenFieldId); /**Network Id for common use*/        
		}
		/**
		 * JSON Preparation PART I: Node Data 
		 */
		JSONObject jsonFinalObj = new JSONObject();
		List<Node> nodes = dbService.getNodeService().FindAll(networkId);

		/**DB Call to get number of nodes*/
		/* Get node data , if green field then return Gf data 
		else if Bf , then check if Bf data is avlbl else return Gf data*/
		int nNodes 	= nodes.size();		
		jsonFinalObj.put("NodeCount",nNodes);

		if(nNodes==MapConstants.I_ZERO && greenFieldNetworkId!=MapConstants.I_ZERO)
		{
			nodes = dbService.getNodeService().FindAllNonILAsNetworkId(greenFieldNetworkId);
			nNodes = nodes.size();
		}

		JSONArray jsonArrayNodeObj = new JSONArray();

		for (int i = 0; i < nodes.size(); i++) {	
			jsonArrayNodeObj.add(nodes.get(i).getNodeId());					
		}

		jsonFinalObj.put("NodeData",jsonArrayNodeObj);				
		//System.out.println("After JSON Preparation PART I: jsonFinalObj Data  :- "+jsonFinalObj);

		/**
		 *  JSON Preparation PART I: Bom View Data 
		 */	
		jsonFinalObj.put("bomViewData",MapWebGetBomData(networkId,networkInfoMap,dbService));		
		//System.out.println("After JSON Preparation PART II: jsonFinalObj Data  :- "+jsonFinalObj);

		/**
		 *  JSON Preparation PART III: Wavelength[View] Data 
		 */			
		jsonFinalObj.put("wavelengthConfiguration",MapWebGetClientConfig(networkId,nodes,networkInfoMap,dbService));
		//System.out.println("After JSON Preparation PART III: jsonFinalObj Data  wavelengthConfiguration :- "+jsonFinalObj);

		/**
		 *  JSON Preparation PART IV:JSONArray[For node scheme and link scheme] and a JSONOBject 
		 */
		jsonFinalObj.put("IpSchemeNodeData",MapWebGetNodeIpScheme(networkId,networkInfoMap,dbService));
		//System.out.println("After JSON Preparation PART IV: jsonFinalObj Data  IpSchemeNodeData :- "+jsonFinalObj);
//		MainMap.logger.info("Final JSON Object Response :- "+ jsonFinalObj);

		/**
		 *  PART V:   IP Report Genetating  for LinkDATA
		 */
		jsonFinalObj.put("IpSchemeLinkData",MapWebGetLinkIpScheme(networkId,networkInfoMap,dbService));
		//System.out.println("After JSON Preparation PART V: jsonFinalObj Data  IpSchemeLinkData :- "+jsonFinalObj);
//		MainMap.logger.info("Final JSON Object Response :- "+ jsonFinalObj);    



		/**
		 *  JSON Preparation PART VI : MPN Information Add 
		 */
		jsonFinalObj.put("MPNInformation", MapWebGetMpnConfigData(networkId,nodes,networkInfoMap,dbService));
		//System.out.println("After JSON Preparation PART VI: jsonFinalObj Data  Final JSON to Return :- "+jsonFinalObj);

		/**
		 *  JSON Preparation PART VII : Inventory Information Add 
		 */
		jsonFinalObj.put("InventoryData", MapWebGetInventoryData(networkId,nodes,networkInfoMap,dbService));
		//System.out.println("After JSON Preparation PART VII: jsonFinalObj Data  Final JSON to Return :- "+jsonFinalObj);
		
		/**
		 *  JSON Preparation PART VIII : Optical Power  
		 */
		jsonFinalObj.put("OpticalPowerAddData", MapWebGetOpticalPowerData(networkId,nodes,networkInfoMap,dbService).get("opticalPowerAddDataJsonArr"));
		jsonFinalObj.put("OpticalPowerDropData", MapWebGetOpticalPowerData(networkId,nodes,networkInfoMap,dbService).get("opticalPowerDropDataJsonArr"));
		
//		System.out.println("After JSON Preparation PART VIII: jsonFinalObj Data  Final JSON to Return :- "+jsonFinalObj);
		
		return jsonFinalObj;
	}

	@SuppressWarnings("unchecked")
	public JSONObject MapWebGetOpticalPowerData(int networkId, List<Node> nodes, HashMap<String, Object> networkInfoMap,
			DbService dbService) {
		// TODO Auto-generated method stub
		int greenFieldNetworkId=MapConstants.I_ZERO;
		if(((String)networkInfoMap.get(MapConstants.NetworkType)).equalsIgnoreCase(MapConstants.BrownField)){ 
			networkId=(int)networkInfoMap.get(MapConstants.GreenFieldId); /**Network Id for common use*/        
		}
		
		List<OpticalPowerCdcRoadmAdd> opticalPowerAddDataList = dbService.getCdcRoadmAddService().FindAll(networkId);
		List<OpticalPowerCdcRoadmDrop> opticalPowerDropDataList = dbService.getCdcRoadmDropService().FindAll(networkId);
		
		JSONArray opticalPowerAddDataJsonArr=new JSONArray();
		JSONArray opticalPowerDropDataJsonArr=new JSONArray();
		JSONObject opticalPowerDataJsonObj=new JSONObject();
		
		for(OpticalPowerCdcRoadmAdd opticalPowerAddDataObj:opticalPowerAddDataList)
		{
			//System.out.println("RowObject NodeId:"+opticalPowerAddDataObj);
			JSONObject opticalPowerAddObj=new JSONObject();
			opticalPowerAddObj.put("NodeId", opticalPowerAddDataObj.getNodeId());
			opticalPowerAddObj.put("Direction", opticalPowerAddDataObj.getDirection());
			opticalPowerAddObj.put("NumOfLambda", opticalPowerAddDataObj.getNoOfLambdas());
			opticalPowerAddObj.put("EdfaIn", opticalPowerAddDataObj.getAddEdfaIn());
			opticalPowerAddObj.put("EdfaOut", opticalPowerAddDataObj.getAddEdfaOut());
			opticalPowerAddObj.put("McsIn", opticalPowerAddDataObj.getAddMcsIn());
			opticalPowerAddObj.put("McsOut", opticalPowerAddDataObj.getAddMcsOut());
			opticalPowerAddObj.put("BaIn", opticalPowerAddDataObj.getBaIn());
			opticalPowerAddObj.put("BaOut", opticalPowerAddDataObj.getBaOut());
			opticalPowerAddObj.put("MpnLaunchPower", opticalPowerAddDataObj.getMpnLaunchPower());
			opticalPowerAddObj.put("WssAtten", opticalPowerAddDataObj.getTxWssAttenuation());
			opticalPowerAddObj.put("WssIn", opticalPowerAddDataObj.getTxWssIn());
			opticalPowerAddObj.put("WssOut", opticalPowerAddDataObj.getTxWssOut());
			opticalPowerAddObj.put("State", MapConstants.GreenField);
			opticalPowerAddDataJsonArr.add(opticalPowerAddObj);
		}
		
		for(OpticalPowerCdcRoadmDrop opticalPowerCdcRoadmDropObj:opticalPowerDropDataList)
		{
			//System.out.println("RowObject NodeId:"+opticalPowerCdcRoadmDropObj);
			JSONObject opticalPowerDropObj=new JSONObject();
			opticalPowerDropObj.put("NodeId", opticalPowerCdcRoadmDropObj.getNodeId());
			opticalPowerDropObj.put("Direction", opticalPowerCdcRoadmDropObj.getDirection());
			opticalPowerDropObj.put("NumOfLambda", opticalPowerCdcRoadmDropObj.getNoOfLambdas());
			opticalPowerDropObj.put("PaIn", opticalPowerCdcRoadmDropObj.getPaIn());
			opticalPowerDropObj.put("PaOut", opticalPowerCdcRoadmDropObj.getPaOut());
			opticalPowerDropObj.put("WssIn", opticalPowerCdcRoadmDropObj.getRxWssIn());
			opticalPowerDropObj.put("WssOut", opticalPowerCdcRoadmDropObj.getRxWssOut());
			opticalPowerDropObj.put("RxWssAttenuation", opticalPowerCdcRoadmDropObj.getRxWssAttenuation());
			opticalPowerDropObj.put("DropEdfaIn", opticalPowerCdcRoadmDropObj.getDropEdfaIn());
			opticalPowerDropObj.put("DropEdfaOut", opticalPowerCdcRoadmDropObj.getDropEdfaOut());
			opticalPowerDropObj.put("McsIn", opticalPowerCdcRoadmDropObj.getDropMcsIn());
			opticalPowerDropObj.put("McsOut", opticalPowerCdcRoadmDropObj.getDropMcsOut());
			opticalPowerDropObj.put("MpnIn", opticalPowerCdcRoadmDropObj.getMpnIn());
			opticalPowerDropObj.put("State", MapConstants.GreenField);
			opticalPowerDropDataJsonArr.add(opticalPowerDropObj);
		}
		
		opticalPowerDataJsonObj.put("opticalPowerAddDataJsonArr",opticalPowerAddDataJsonArr);
		opticalPowerDataJsonObj.put("opticalPowerDropDataJsonArr",opticalPowerDropDataJsonArr);
		
		
		return opticalPowerDataJsonObj;
	}


	@SuppressWarnings("unchecked")
	public JSONObject MapWebGetBomData(int networkId,HashMap<String, Object> networkInfoMap, DbService dbService)
	{

		//JSONObject jsonIndexBomViewObj = new JSONObject();
		List<Bom> bomData = dbService.getViewServiceRp().FindBoM(networkId);

		JSONArray jsonArrayBomNetworkViewObj = new JSONArray();
		JSONObject jsonBomFinalObj = new JSONObject();

		JSONArray jsonArrayBomViewObj = new JSONArray();
		JSONObject jsonBomRowObj = new JSONObject();

		int greenFieldNetworkId=MapConstants.I_ZERO;
		if(((String)networkInfoMap.get(MapConstants.NetworkType)).equalsIgnoreCase(MapConstants.BrownField)){ 
			greenFieldNetworkId=(int)networkInfoMap.get(MapConstants.GreenFieldId); /**Network Id for common use*/        
		}

		//If Bf has Bom data
		if(((String)networkInfoMap.get(MapConstants.NetworkType)).equalsIgnoreCase(MapConstants.BrownField) && bomData.size()!=MapConstants.I_ZERO)
		{ 	

			List<Bom> bomDataOld = dbService.getViewServiceRp().FindCommonBomEntriesInBrField(greenFieldNetworkId, networkId);
			System.out.println("bomDataOld Size : "+ bomDataOld.size());

			for (int i = 0; i < bomDataOld.size(); i++) {	 				
				jsonBomRowObj = new JSONObject();	
				//int currentId = bomData.get(i).getNodeId();
				System.out.println("bomData.get(i).getNodeId()" + bomDataOld.get(i).getNodeId());
				jsonBomRowObj.put("NodeId", bomDataOld.get(i).getNodeId());
				jsonBomRowObj.put("StationName", bomDataOld.get(i).getStationName());
				jsonBomRowObj.put("SiteName", bomDataOld.get(i).getSiteName());
				jsonBomRowObj.put("Name", bomDataOld.get(i).getName());
				jsonBomRowObj.put("Quantity", bomDataOld.get(i).getQuantity());
				jsonBomRowObj.put("Price", bomDataOld.get(i).getPrice());			
				jsonBomRowObj.put("TotalPrice", bomDataOld.get(i).getTotalPrice());
				jsonBomRowObj.put("PartNo", bomDataOld.get(i).getPartNo());
				jsonBomRowObj.put("PowerConsumption", bomDataOld.get(i).getPowerConsumption()*bomDataOld.get(i).getQuantity());
				jsonBomRowObj.put("TypicalPowerConsumption", bomDataOld.get(i).getTypicalPower()*bomDataOld.get(i).getQuantity()); //to be updated 
				jsonBomRowObj.put("RevisionCode", bomDataOld.get(i).getRevisionCode());
				jsonBomRowObj.put("Category", bomDataOld.get(i).getCategory());
				jsonBomRowObj.put("State", MapConstants.OldState);
				jsonArrayBomViewObj.add(jsonBomRowObj);

				System.out.println(" Value of i:"+i+"jsonBomRowObj ::\n"+jsonBomRowObj);
			}

			List<Bom> bomDataNew = dbService.getViewServiceRp().FindAddedBomEntriesInBrField(greenFieldNetworkId,networkId);
			System.out.println("bomDataNew Size : "+ bomDataNew.size());
			for (int i = 0; i < bomDataNew.size(); i++) {		
				jsonBomRowObj = new JSONObject();	
				//int currentId = bomData.get(i).getNodeId();
				System.out.println("bomData.get(i).getNodeId()" + bomDataNew.get(i).getNodeId());
				jsonBomRowObj.put("NodeId", bomDataNew.get(i).getNodeId());
				jsonBomRowObj.put("StationName", bomDataNew.get(i).getStationName());
				jsonBomRowObj.put("SiteName", bomDataNew.get(i).getSiteName());
				jsonBomRowObj.put("Name", bomDataNew.get(i).getName());
				jsonBomRowObj.put("Quantity", bomDataNew.get(i).getQuantity());
				jsonBomRowObj.put("Price", bomDataNew.get(i).getPrice());			
				jsonBomRowObj.put("TotalPrice", bomDataNew.get(i).getTotalPrice());
				jsonBomRowObj.put("PartNo", bomDataNew.get(i).getPartNo());
				jsonBomRowObj.put("PowerConsumption", bomDataNew.get(i).getPowerConsumption()*bomDataNew.get(i).getQuantity());
				jsonBomRowObj.put("TypicalPowerConsumption", bomDataNew.get(i).getTypicalPower()*bomDataNew.get(i).getQuantity()); //to be updated 
				jsonBomRowObj.put("RevisionCode", bomDataNew.get(i).getRevisionCode());
				jsonBomRowObj.put("Category", bomDataNew.get(i).getCategory());	
				jsonBomRowObj.put("State", MapConstants.NewState);	
				jsonArrayBomViewObj.add(jsonBomRowObj);

				System.out.println(" Value of i:"+i+"jsonBomRowObj ::\n"+jsonBomRowObj);
			}

			List<Bom> bomDataModified = dbService.getViewServiceRp().FindModifiedBomEntriesInBrField(greenFieldNetworkId,networkId);
			System.out.println("bomDataModified Size : "+ bomDataModified.size());
			for (int i = 0; i < bomDataModified.size(); i++) {		
				jsonBomRowObj = new JSONObject();	
				//int currentId = bomData.get(i).getNodeId();
				System.out.println("bomData.get(i).getNodeId()" + bomDataModified.get(i).getNodeId());
				jsonBomRowObj.put("NodeId", bomDataModified.get(i).getNodeId());
				jsonBomRowObj.put("StationName", bomDataModified.get(i).getStationName());
				jsonBomRowObj.put("SiteName", bomDataModified.get(i).getSiteName());
				jsonBomRowObj.put("Name", bomDataModified.get(i).getName());
				jsonBomRowObj.put("Quantity", bomDataModified.get(i).getQuantity());
				jsonBomRowObj.put("Price", bomDataModified.get(i).getPrice());			
				jsonBomRowObj.put("TotalPrice", bomDataModified.get(i).getTotalPrice());
				jsonBomRowObj.put("PartNo", bomDataModified.get(i).getPartNo());
				jsonBomRowObj.put("PowerConsumption", bomDataModified.get(i).getPowerConsumption()*bomDataModified.get(i).getQuantity());
				jsonBomRowObj.put("TypicalPowerConsumption", bomDataModified.get(i).getTypicalPower()*bomDataModified.get(i).getQuantity()); //to be updated 
				jsonBomRowObj.put("RevisionCode", bomDataModified.get(i).getRevisionCode());
				jsonBomRowObj.put("Category", bomDataModified.get(i).getCategory());	
				jsonBomRowObj.put("State", MapConstants.ModifiedState);	
				jsonArrayBomViewObj.add(jsonBomRowObj);

				System.out.println(" Value of i:"+i+"jsonBomRowObj ::\n"+jsonBomRowObj);
			}
			
			List<Bom> bomDataDeleted = dbService.getViewServiceRp().FindDeletedBomEntriesInBrField(greenFieldNetworkId,networkId);
			System.out.println("bomDataModified Size : "+ bomDataDeleted.size());
			for (int i = 0; i < bomDataDeleted.size(); i++) {		
				jsonBomRowObj = new JSONObject();	
				//int currentId = bomData.get(i).getNodeId();
				System.out.println("bomData.get(i).getNodeId()" + bomDataDeleted.get(i).getNodeId());
				jsonBomRowObj.put("NodeId", bomDataDeleted.get(i).getNodeId());
				jsonBomRowObj.put("StationName", bomDataDeleted.get(i).getStationName());
				jsonBomRowObj.put("SiteName", bomDataDeleted.get(i).getSiteName());
				jsonBomRowObj.put("Name", bomDataDeleted.get(i).getName());
				jsonBomRowObj.put("Quantity", bomDataDeleted.get(i).getQuantity());
				jsonBomRowObj.put("Price", bomDataDeleted.get(i).getPrice());			
				jsonBomRowObj.put("TotalPrice", bomDataDeleted.get(i).getTotalPrice());
				jsonBomRowObj.put("PartNo", bomDataDeleted.get(i).getPartNo());
				jsonBomRowObj.put("PowerConsumption", bomDataDeleted.get(i).getPowerConsumption()*bomDataDeleted.get(i).getQuantity());
				jsonBomRowObj.put("TypicalPowerConsumption", bomDataDeleted.get(i).getTypicalPower()*bomDataDeleted.get(i).getQuantity()); //to be updated 
				jsonBomRowObj.put("RevisionCode", bomDataDeleted.get(i).getRevisionCode());
				jsonBomRowObj.put("Category", bomDataDeleted.get(i).getCategory());	
				jsonBomRowObj.put("State", MapConstants.DeleteState);	
				jsonArrayBomViewObj.add(jsonBomRowObj);

				System.out.println(" Value of i:"+i+"jsonBomRowObj ::\n"+jsonBomRowObj);
			}

			List<Bom> NetworkAggregatedBomData = dbService.getViewServiceRp().FindBomNetworkWise(networkId);
			System.out.println("NetworkAggregatedBomData Size : "+ NetworkAggregatedBomData.size());
			for (int i = 0; i < NetworkAggregatedBomData.size(); i++) {		
				jsonBomRowObj = new JSONObject();	
				//int currentId = bomData.get(i).getNodeId();
				System.out.println("bomData.get(i).getNodeId()" + NetworkAggregatedBomData.get(i).getNodeId());
				jsonBomRowObj.put("StationName", NetworkAggregatedBomData.get(i).getStationName());
				jsonBomRowObj.put("SiteName", NetworkAggregatedBomData.get(i).getSiteName());
				jsonBomRowObj.put("Name", NetworkAggregatedBomData.get(i).getName());
				jsonBomRowObj.put("Quantity", NetworkAggregatedBomData.get(i).getQuantity());
				jsonBomRowObj.put("Price", NetworkAggregatedBomData.get(i).getPrice());			
				jsonBomRowObj.put("TotalPrice", NetworkAggregatedBomData.get(i).getPrice()*NetworkAggregatedBomData.get(i).getQuantity());
				jsonBomRowObj.put("PartNo", NetworkAggregatedBomData.get(i).getPartNo());
				jsonBomRowObj.put("PowerConsumption", NetworkAggregatedBomData.get(i).getPowerConsumption()*NetworkAggregatedBomData.get(i).getQuantity());
				jsonBomRowObj.put("TypicalPowerConsumption", NetworkAggregatedBomData.get(i).getTypicalPower()*NetworkAggregatedBomData.get(i).getQuantity()); //to be updated 
				jsonBomRowObj.put("RevisionCode", NetworkAggregatedBomData.get(i).getRevisionCode());
				jsonBomRowObj.put("Category", NetworkAggregatedBomData.get(i).getCategory());	
				jsonBomRowObj.put("State", MapConstants.GreenFieldState);	
				jsonArrayBomNetworkViewObj.add(jsonBomRowObj);

				System.out.println(" Value of i:"+i+"jsonBomRowObj ::\n"+jsonBomRowObj);
			}

		}
		else 
		{
			System.out.println("Green field case BOM..");
			String state=MapConstants.GreenFieldState;

			if(greenFieldNetworkId!=MapConstants.I_ZERO)
			{
				System.out.println("Bf but circuit data not saved yet");
				networkId=greenFieldNetworkId;
				state=MapConstants.OldState;
			}


			bomData = dbService.getViewServiceRp().FindBoM(networkId);
			System.out.println("GF bomData Size : "+ bomData.size());
			for (int i = 0; i < bomData.size(); i++) {		

				jsonBomRowObj = new JSONObject();			

				//int currentId = bomData.get(i).getNodeId();
				System.out.println("bomData.get(i).getNodeId()" + bomData.get(i).getNodeId());
				jsonBomRowObj.put("NodeId", bomData.get(i).getNodeId());
				jsonBomRowObj.put("StationName", bomData.get(i).getStationName());
				jsonBomRowObj.put("SiteName", bomData.get(i).getSiteName());
				jsonBomRowObj.put("Name", bomData.get(i).getName());
				jsonBomRowObj.put("Quantity", bomData.get(i).getQuantity());
				jsonBomRowObj.put("Price", bomData.get(i).getPrice());			
				jsonBomRowObj.put("TotalPrice", bomData.get(i).getTotalPrice());
				jsonBomRowObj.put("PartNo", bomData.get(i).getPartNo());
				jsonBomRowObj.put("Unit Power", bomData.get(i).getPowerConsumption());
				jsonBomRowObj.put("Unit TypicalPower", bomData.get(i).getTypicalPower());
				jsonBomRowObj.put("PowerConsumption", bomData.get(i).getPowerConsumption()*bomData.get(i).getQuantity());
				jsonBomRowObj.put("TypicalPowerConsumption", bomData.get(i).getTypicalPower()*bomData.get(i).getQuantity()); //to be updated 
				jsonBomRowObj.put("RevisionCode", bomData.get(i).getRevisionCode());
				jsonBomRowObj.put("Category", bomData.get(i).getCategory());	
				jsonBomRowObj.put("State", state);	
				jsonArrayBomViewObj.add(jsonBomRowObj);

				System.out.println(" Value of i:"+i+"jsonBomRowObj ::\n"+jsonBomRowObj);
			}

			List<Bom> NetworkAggregatedBomData = dbService.getViewServiceRp().FindBomNetworkWise(networkId);
			System.out.println("GF NetworkAggregatedBomData Size : "+ NetworkAggregatedBomData.size());
			for (int i = 0; i < NetworkAggregatedBomData.size(); i++) {		
				jsonBomRowObj = new JSONObject();	
				//int currentId = bomData.get(i).getNodeId();
				System.out.println("bomData.get(i).getNodeId()" + NetworkAggregatedBomData.get(i).getNodeId());
				jsonBomRowObj.put("StationName", NetworkAggregatedBomData.get(i).getStationName());
				jsonBomRowObj.put("SiteName", NetworkAggregatedBomData.get(i).getSiteName());
				jsonBomRowObj.put("Name", NetworkAggregatedBomData.get(i).getName());
				jsonBomRowObj.put("Quantity", NetworkAggregatedBomData.get(i).getQuantity());
				jsonBomRowObj.put("Price", NetworkAggregatedBomData.get(i).getPrice());			
				jsonBomRowObj.put("TotalPrice", NetworkAggregatedBomData.get(i).getTotalPrice());
				jsonBomRowObj.put("PartNo", NetworkAggregatedBomData.get(i).getPartNo());
				jsonBomRowObj.put("PowerConsumption", NetworkAggregatedBomData.get(i).getPowerConsumption());
				jsonBomRowObj.put("TypicalPowerConsumption", NetworkAggregatedBomData.get(i).getTypicalPower()); //to be updated 
				jsonBomRowObj.put("RevisionCode", NetworkAggregatedBomData.get(i).getRevisionCode());
				jsonBomRowObj.put("Category", NetworkAggregatedBomData.get(i).getCategory());	
				jsonBomRowObj.put("State", state);	
				jsonArrayBomNetworkViewObj.add(jsonBomRowObj);

				System.out.println(" Value of i:"+i+"jsonBomRowObj ::\n"+jsonBomRowObj);
			}
		}

		jsonBomFinalObj.put("NetworkWiseData", jsonArrayBomNetworkViewObj);
		jsonBomFinalObj.put("NodeWiseData", jsonArrayBomViewObj);
		return jsonBomFinalObj;	 
	}

	@SuppressWarnings("unchecked")
	public JSONArray MapWebGetInventoryData(int networkId,List<Node> nodes,HashMap<String, Object> networkInfoMap, DbService dbService)
	{

		JSONArray jsonArrayInventory = new JSONArray();
		JSONObject jsonInventoryObj = new JSONObject();
		int greenFieldNetworkId=MapConstants.I_ZERO;
		if(((String)networkInfoMap.get(MapConstants.NetworkType)).equalsIgnoreCase(MapConstants.BrownField)){ 
			greenFieldNetworkId=(int)networkInfoMap.get(MapConstants.GreenFieldId); /**Network Id for common use*/        
		}
		//List<CardInfo> cardInfoData = dbService.getCardInfoService().FindAll(networkId,currentNode);	
		//int nodeId1 = MapConstants.I_MINUS_ONE;	
		for(int nodeCount=0;nodeCount<nodes.size();nodeCount++){
			int currentNode = nodes.get(nodeCount).getNodeId();

			List<CardInfo> cardInfoData = dbService.getCardInfoService().FindAll(networkId,currentNode);	

			if(((String)networkInfoMap.get(MapConstants.NetworkType)).equalsIgnoreCase(MapConstants.BrownField) && cardInfoData.size()!=MapConstants.I_ZERO)
			{

				List<CardInfo> cardInfoDataOld = dbService.getCardInfoService().FindCommonCardsInBrField(greenFieldNetworkId, networkId, currentNode);
				if(cardInfoDataOld!=null)
				{
					System.out.println("MapWebGetInventoryData:: cardInfoDataOld Size : "+ cardInfoDataOld.size());
					for (CardInfo rowCardInfoObj : cardInfoDataOld) {	
						jsonInventoryObj=new JSONObject();
						jsonInventoryObj.put("NodeId",currentNode );
						jsonInventoryObj.put("Rack", rowCardInfoObj.getRack());
						jsonInventoryObj.put("SubRack", rowCardInfoObj.getSbrack());
						jsonInventoryObj.put("CardId", rowCardInfoObj.getCard());			
						jsonInventoryObj.put("CardType", rowCardInfoObj.getCardType());
						jsonInventoryObj.put("Direction", rowCardInfoObj.getDirection());
						
						if(rowCardInfoObj.getWavelength()==MapConstants.I_ZERO) {
							jsonInventoryObj.put("Wavelength",MapConstants.WavelengthNotApplicable);
						}
						else {
							jsonInventoryObj.put("Wavelength",rowCardInfoObj.getWavelength());
						}
						
						jsonInventoryObj.put("State", MapConstants.OldState);


						//System.out.println("MapWebGetInventoryData:: jsonInventoryObj added to jsonArrayInventory::\n"+jsonInventoryObj);
						jsonArrayInventory.add(jsonInventoryObj);				
					}	
				}else System.out.println("cardinfodataOld is null");


				List<CardInfo> cardInfoDataAdded = dbService.getCardInfoService().FindAddedCardsInBrField(greenFieldNetworkId, networkId, currentNode);
				if(cardInfoDataAdded!=null)
				{
					System.out.println("MapWebGetInventoryData:: cardInfoDataAdded Size : "+ cardInfoDataAdded.size());
					for (CardInfo rowCardInfoObj : cardInfoDataAdded) {	
						jsonInventoryObj=new JSONObject();
						jsonInventoryObj.put("NodeId",currentNode );
						jsonInventoryObj.put("Rack", rowCardInfoObj.getRack());
						jsonInventoryObj.put("SubRack", rowCardInfoObj.getSbrack());
						jsonInventoryObj.put("CardId", rowCardInfoObj.getCard());			
						jsonInventoryObj.put("CardType", rowCardInfoObj.getCardType());
						jsonInventoryObj.put("Direction", rowCardInfoObj.getDirection());
						
						if(rowCardInfoObj.getWavelength()==MapConstants.I_ZERO) {
							jsonInventoryObj.put("Wavelength",MapConstants.WavelengthNotApplicable);
						}
						else {
							jsonInventoryObj.put("Wavelength",rowCardInfoObj.getWavelength());
						}
						jsonInventoryObj.put("State", MapConstants.NewState);					

						//System.out.println("MapWebGetInventoryData:: jsonInventoryObj added to jsonArrayInventory::\n"+jsonInventoryObj);
						jsonArrayInventory.add(jsonInventoryObj);				
					}
				}else System.out.println("cardInfoDataAdded is null");
				
				List<CardInfo> cardInfoDataDeleted = dbService.getCardInfoService().FindDeletedCardsInBrField(greenFieldNetworkId, networkId, currentNode);
				if(cardInfoDataDeleted!=null)
				{
					System.out.println("MapWebGetInventoryData:: cardInfoDataAdded Size : "+ cardInfoDataDeleted.size());
					for (CardInfo rowCardInfoObj : cardInfoDataDeleted) {	
						jsonInventoryObj=new JSONObject();
						jsonInventoryObj.put("NodeId",currentNode );
						jsonInventoryObj.put("Rack", rowCardInfoObj.getRack());
						jsonInventoryObj.put("SubRack", rowCardInfoObj.getSbrack());
						jsonInventoryObj.put("CardId", rowCardInfoObj.getCard());			
						jsonInventoryObj.put("CardType", rowCardInfoObj.getCardType());
						jsonInventoryObj.put("Direction", rowCardInfoObj.getDirection());
						if(rowCardInfoObj.getWavelength()==MapConstants.I_ZERO) {
							jsonInventoryObj.put("Wavelength",MapConstants.WavelengthNotApplicable);
						}
						else {
							jsonInventoryObj.put("Wavelength",rowCardInfoObj.getWavelength());
						}
						jsonInventoryObj.put("State", MapConstants.DeleteState);					

						//System.out.println("MapWebGetInventoryData:: jsonInventoryObj added to jsonArrayInventory::\n"+jsonInventoryObj);
						jsonArrayInventory.add(jsonInventoryObj);				
					}
				}else System.out.println("cardInfoDataDeleted is null");
				
			}		
			else{
				System.out.println("Green field case Inventory..");
				String state=MapConstants.GreenFieldState;

				if(greenFieldNetworkId!=MapConstants.I_ZERO)
				{
					System.out.println("Bf but circuit data not saved yet");
					networkId=greenFieldNetworkId;
					state=MapConstants.OldState;
					cardInfoData = dbService.getCardInfoService().FindAll(networkId,currentNode);
				}
				if(cardInfoData!=null)
				{	
					System.out.println("MapWebGetInventoryData:: CardInfoData Size : "+ cardInfoData.size());
					for (CardInfo rowCardInfoObj : cardInfoData) {	
						jsonInventoryObj=new JSONObject();
						jsonInventoryObj.put("NodeId",currentNode );
						jsonInventoryObj.put("Rack", rowCardInfoObj.getRack());
						jsonInventoryObj.put("SubRack", rowCardInfoObj.getSbrack());
						jsonInventoryObj.put("CardId", rowCardInfoObj.getCard());			
						jsonInventoryObj.put("CardType", rowCardInfoObj.getCardType());
						jsonInventoryObj.put("Direction", rowCardInfoObj.getDirection());

						if(rowCardInfoObj.getWavelength()==MapConstants.I_ZERO) {
							jsonInventoryObj.put("Wavelength",MapConstants.WavelengthNotApplicable);
						}
						else {
							jsonInventoryObj.put("Wavelength",rowCardInfoObj.getWavelength());
						}
						
						jsonInventoryObj.put("State", state);
						jsonInventoryObj.put("NodesSize", nodes.size());

						System.out.println("MapWebGetInventoryData:: jsonInventoryObj added to jsonArrayInventory::\n"+jsonInventoryObj);
						jsonArrayInventory.add(jsonInventoryObj);				

					}
				}else System.out.println("cardInfoDataAdded is null");
			}
		}
		return jsonArrayInventory;
	}

	@SuppressWarnings("unchecked")
	public JSONArray MapWebGetClientConfig(int networkId,List<Node> nodes,HashMap<String, Object> networkInfoMap, DbService dbService)
	{
		JSONArray jsonArrayWevelengthViewObj = new JSONArray();
		//		if(((String)networkInfoMap.get(MapConstants.NetworkType)).equalsIgnoreCase(MapConstants.BrownField)){ 
		//			networkId=(int)networkInfoMap.get(MapConstants.GreenFieldId); /**Network Id for common use*/        
		//		}
		//int nodeId1 = MapConstants.I_MINUS_ONE;	
		for(int nodeCount=0;nodeCount<nodes.size();nodeCount++){
			int currentNode = nodes.get(nodeCount).getNodeId();

			List<Map<String, Object>> wavelengthData = dbService.getCardInfoService().FgetCircuitMatix(networkId,currentNode);	
			if(wavelengthData!=null){
				//System.out.println("WavelengthData Size : "+ wavelengthData.size());
				for (Map<String, Object> rowwevelengthObj : wavelengthData) {

					if(rowwevelengthObj.get("Status").toString().equalsIgnoreCase("P"))
						continue;/**Only Working are passed as per the instruction*/

					JSONObject jsonWavelengthRowObj = new JSONObject();
					jsonWavelengthRowObj.put("NodeId",currentNode );
					jsonWavelengthRowObj.put("Wavelength", rowwevelengthObj.get("WavelengthNo"));
					jsonWavelengthRowObj.put("DemandId", rowwevelengthObj.get("DemandId"));
					jsonWavelengthRowObj.put("CircuitId", rowwevelengthObj.get("CircuitId"));			
					jsonWavelengthRowObj.put("TrafficType", rowwevelengthObj.get("TrafficType"));
					jsonWavelengthRowObj.put("ProtectionType", rowwevelengthObj.get("ProtectionType"));
					jsonWavelengthRowObj.put("ClientProtectionType", rowwevelengthObj.get("ClientProtectionType"));
					jsonWavelengthRowObj.put("Direction",rowwevelengthObj.get("Direction")); //to be updated 
					jsonWavelengthRowObj.put("LineRate", rowwevelengthObj.get("LineRate"));
					/**Added later for view report */
					jsonWavelengthRowObj.put("ProtectionStatus", rowwevelengthObj.get("Status"));
					jsonWavelengthRowObj.put("PortId", rowwevelengthObj.get("Port"));

					jsonArrayWevelengthViewObj.add(jsonWavelengthRowObj);

					//System.out.println(" Value of i:"+rowwevelengthObj+"jsonBomRowObj ::\n"+jsonWavelengthRowObj);
				}		
			}
			
		}
		return jsonArrayWevelengthViewObj;

	}

	@SuppressWarnings("unchecked")
	public JSONArray MapWebGetMpnConfigData(int networkId,List<Node> nodes,HashMap<String, Object> networkInfoMap, DbService dbService)
	{
		JSONArray  jsonMpnInfoArray = new JSONArray();
		//		if(((String)networkInfoMap.get(MapConstants.NetworkType)).equalsIgnoreCase(MapConstants.BrownField)){ 
		//			networkId=(int)networkInfoMap.get(MapConstants.GreenFieldId); /**Network Id for common use*/        
		//		}
		/**Fetch Data from Node and Link IP Scheme*/
		for (int i = 0; i < nodes.size(); i++) {

			List<Map<String, Object>> listMpnInforArrayList = dbService.getLinkWavelengthInfoService().FindNodeWiseAllLambdaInfo(networkId, nodes.get(i).getNodeId());
			
			if(listMpnInforArrayList!=null)
			{
				//System.out.println("listMpnInforArrayList : "+listMpnInforArrayList + " For I : "+ i);
				for(int listMpnInforArrayListCounter=0;listMpnInforArrayListCounter< listMpnInforArrayList.size(); 
						listMpnInforArrayListCounter++){
					JSONObject jsonMpnInfoRowObj = new JSONObject();
					//System.out.println("listMpnInforArrayListCounter : "+listMpnInforArrayListCounter);
					jsonMpnInfoRowObj.put("NodeId", nodes.get(i).getNodeId());
					jsonMpnInfoRowObj.put("Direction", listMpnInforArrayList.get(listMpnInforArrayListCounter).get("Direction"));
					jsonMpnInfoRowObj.put("Linkid", listMpnInforArrayList.get(listMpnInforArrayListCounter).get("Linkid"));
					jsonMpnInfoRowObj.put("Wavelength", listMpnInforArrayList.get(listMpnInforArrayListCounter).get("Wavelength"));
					jsonMpnInfoRowObj.put("LineRate", listMpnInforArrayList.get(listMpnInforArrayListCounter).get("LineRate"));
					jsonMpnInfoRowObj.put("Traffic", listMpnInforArrayList.get(listMpnInforArrayListCounter).get("Traffic"));
					jsonMpnInfoRowObj.put("Path", listMpnInforArrayList.get(listMpnInforArrayListCounter).get("Path"));
					jsonMpnInfoRowObj.put("DemandId", listMpnInforArrayList.get(listMpnInforArrayListCounter).get("DemandId"));
					jsonMpnInfoRowObj.put("RoutePriority", listMpnInforArrayList.get(listMpnInforArrayListCounter).get("RoutePriority"));
					jsonMpnInfoRowObj.put("Status", listMpnInforArrayList.get(listMpnInforArrayListCounter).get("Status"));

					String cardLocation=String.valueOf(listMpnInforArrayList.get(listMpnInforArrayListCounter).get("Rack"))+"-"+
							String.valueOf(listMpnInforArrayList.get(listMpnInforArrayListCounter).get("Sbrack"))+"-"+
							String.valueOf(listMpnInforArrayList.get(listMpnInforArrayListCounter).get("Card"));

					if(listMpnInforArrayList.get(listMpnInforArrayListCounter).get("Rack")==null){
						cardLocation="Pass-through";
						continue;
					}
					jsonMpnInfoRowObj.put("PhysicalLocation", cardLocation);				 				 

					int srcNodeId = dbService.getDemandService().FindDemand(networkId, Integer.parseInt(listMpnInforArrayList.get(listMpnInforArrayListCounter).get("DemandId").toString()))
							.getSrcNodeId();
					int destNodeId = dbService.getDemandService().FindDemand(networkId, Integer.parseInt(listMpnInforArrayList.get(listMpnInforArrayListCounter).get("DemandId").toString()))
							.getDestNodeId();

					jsonMpnInfoRowObj.put("SrcNode", srcNodeId);
					jsonMpnInfoRowObj.put("DestNode", destNodeId);

					//System.out.println("listMpnInforArray :  "+ listMpnInforArrayList);

					jsonMpnInfoArray.add(jsonMpnInfoRowObj);
				}	
			}
			

		}
		return jsonMpnInfoArray;

	}

	@SuppressWarnings("unchecked")
	public JSONArray MapWebGetNodeIpScheme(int networkId,HashMap<String, Object> networkInfoMap, DbService dbService)
	{
		JSONObject jsonObjRes = new JSONObject();
		JSONArray  jsonNodeArray = new JSONArray();
		//		if(((String)networkInfoMap.get(MapConstants.NetworkType)).equalsIgnoreCase(MapConstants.BrownField)){ 
		//			networkId=(int)networkInfoMap.get(MapConstants.GreenFieldId); /**Network Id for common use*/        
		//		}

		MainMap.logger.info("--------------------------View IP Scheme Request Start---------------------------------------");

		/**Fetch Data from Node and Link IP Scheme*/
		List<IpSchemeNode> listIpSchemeNode = dbService.getIpSchemeNodeService().FindAll(networkId);


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
			//jsonRowObj.put("RsrvdIp1", IPv4.integerToStringIP(listIpSchemeNode.get(i).getRsrvdIp1()));
			// jsonRowObj.put("RsrvdIp2", IPv4.integerToStringIP(listIpSchemeNode.get(i).getRsrvdIp2()));

			jsonNodeArray.add(jsonRowObj);
		}


		/**Combine both the array into One object*/
		// jsonObjRes.put("IpSchemeNode", jsonNodeArray);
		// jsonObjRes.put("IpSchemeLink", jsonLinkArray);      

		MainMap.logger.info("--------------------------View IP Scheme Request End---------------------------------------");
		return jsonNodeArray;
	}

	@SuppressWarnings("unchecked")
	public JSONArray MapWebGetLinkIpScheme(int networkId,HashMap<String, Object> networkInfoMap, DbService dbService)
	{
		//		if(((String)networkInfoMap.get(MapConstants.NetworkType)).equalsIgnoreCase(MapConstants.BrownField)){ 
		//			networkId=(int)networkInfoMap.get(MapConstants.GreenFieldId); /**Network Id for common use*/        
		//		}
		JSONArray  jsonLinkArray = new JSONArray();
		/**Fetch Data from Node and Link IP Scheme*/
		List<IpSchemeLink> listIpSchemeLink = dbService.getIpSchemeLinkService().FindAll(networkId);
		/**
		 * Loop through to fill JSON Array
		 */
		for(int i=0; i<listIpSchemeLink.size(); i++){/**Link IP Scheme*/

			JSONObject jsonRowObj = new JSONObject();


			jsonRowObj.put("NetworkId", listIpSchemeLink.get(i).getNetworkId());
			jsonRowObj.put("LinkId", listIpSchemeLink.get(i).getLinkId());
			jsonRowObj.put("LinkIp", IPv4.integerToStringIP(listIpSchemeLink.get(i).getLinkIp()));
			jsonRowObj.put("SrcIp", IPv4.integerToStringIP(listIpSchemeLink.get(i).getSrcIp()));
			jsonRowObj.put("DestIp", IPv4.integerToStringIP(listIpSchemeLink.get(i).getDestIp()));

			jsonLinkArray.add(jsonRowObj);
		}
		return jsonLinkArray;
	}

	//	public String fGetBrownField(int greenFieldNetworkId)
	//	{
	//		String state=MapConstants.GreenFieldSate; 	    
	// 	    
	// 	    if(greenFieldNetworkId!=MapConstants.I_ZERO)
	// 	    	{
	// 	    	 System.out.println("Bf but circuit data not saved yet");
	// 	    	 int networkId=greenFieldNetworkId;
	// 	    	 state=MapConstants.OldState;
	// 	    	}
	//		return state;
	//	}


}



