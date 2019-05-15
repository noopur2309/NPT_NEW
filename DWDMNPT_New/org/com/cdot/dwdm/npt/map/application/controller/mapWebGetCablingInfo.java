

package application.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.xmlbeans.impl.jam.mutable.MSourcePosition;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import application.constants.MapConstants;
import application.model.Bom;
import application.model.CardInfo;
import application.model.Equipment;
import application.model.LinkWavelengthMap;
import application.model.Node;
import application.model.PatchCord;
import application.service.DbService;
import application.service.EquipmentService;

/**
 * @author hp
 * @brief  This Class provide the API to generate JSON for Optical Power Measurement which has two parts of DB as follows:
 *          PART I:   Node Data 
 *          PART II:  Optical Data
 *                 
 *         Finally return the same object to the Main Cotroller 
 *          
 */
public class mapWebGetCablingInfo {

	/**
	 * 
	 * @param dbService
	 * @return JSONObject
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public JSONObject MapWebGetCablingInfoJsonReq( String jsonStr,DbService dbService) throws SQLException {

		System.out.println("Inside MapWebGetCablingInfoJsonReq and Going to Return the JSon for Cabling Diagram");

		JSONObject jsonFinalObj = new JSONObject();

		/**Map From Common API*/ 
		HashMap<String, Object> networkInfoMap = MapWebCommonAPIs.NestedStaticClass.getNetworkInformation(dbService,jsonStr); 
		int networkId= (int)networkInfoMap.get(MapConstants.NetworkId); /**Network Id for common use*/ 

		//int networkId=Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString());
		//int nodeId=1;//Integer.parseInt(nodeId);

		CablingMap cmap = new CablingMap(dbService);
		List<PatchCord> patchCordList=cmap.fgenerateCablingMap(networkId);

		@SuppressWarnings("unused")
		Equipment equipment=new Equipment();

		System.out.println("patchCordList.size():"+patchCordList.size());

		EquipmentService equipmentService= dbService.getEquipmentService();
		JSONArray jsonCablingDataArrayObj = new JSONArray();
		for(int i=0;i<patchCordList.size();i++)
		{
			JSONObject jsonTempObj = new JSONObject();
//			jsonTempObj.put("NodeKey",patchCordList.get(i).getNodeKey());
			jsonTempObj.put("NetworkId",patchCordList.get(i).getNetworkId());
			jsonTempObj.put("NodeId",patchCordList.get(i).getNodeId());
			equipment=equipmentService.findEquipmentById(patchCordList.get(i).getEquipmentId());
			jsonTempObj.put("EquipmentId",equipment.getName());
			jsonTempObj.put("CordType",patchCordList.get(i).getCordType());
			jsonTempObj.put("PartNo",equipment.getPartNo());
			jsonTempObj.put("End1CardType",patchCordList.get(i).getEnd1CardType());
			jsonTempObj.put("End1Location",patchCordList.get(i).getEnd1Location());

			//jsonTempObj.put("End1Port",patchCordList.get(i).getEnd1Port());
			jsonTempObj.put("End1Port",hasDirectionString(patchCordList.get(i).getEnd1Port(), patchCordList.get(i).getEnd1CardType(), patchCordList.get(i).getDirectionEnd1()));
			jsonTempObj.put("End2CardType",patchCordList.get(i).getEnd2CardType());	
			jsonTempObj.put("End2Location",patchCordList.get(i).getEnd2Location());

			//jsonTempObj.put("End2Port",patchCordList.get(i).getEnd2Port());	
			jsonTempObj.put("End2Port",hasDirectionString(patchCordList.get(i).getEnd2Port(), patchCordList.get(i).getEnd2CardType(), patchCordList.get(i).getDirectionEnd2()));
			jsonTempObj.put("Length",patchCordList.get(i).getLength());
			jsonTempObj.put("DirectionEnd1",patchCordList.get(i).getDirectionEnd1());	
			jsonTempObj.put("DirectionEnd2",patchCordList.get(i).getDirectionEnd2());	
			jsonCablingDataArrayObj.add(jsonTempObj);
		}
		jsonFinalObj.put("cablingViewData", jsonCablingDataArrayObj);		

		//System.out.println("Final Cabling Info View json Object ::"+jsonFinalObj);

		return jsonFinalObj;			

	}

	public String getPortNumberFromDirection(String dir) {
		String portStr="";

		switch(dir){
		case MapConstants.EAST:
			portStr="Port_"+1;
			break;
		case MapConstants.WEST:
			portStr="Port_"+2;
			break;
		case MapConstants.NORTH:
			portStr="Port_"+3;
			break;
		case MapConstants.SOUTH:
			portStr="Port_"+4;
			break;
		case MapConstants.NE:
			portStr="Port_"+5;
			break;
		case MapConstants.NW:
			portStr="Port_"+6;
			break;
		case MapConstants.SE:
			portStr="Port_"+7;
			break;
		case MapConstants.SW:
			portStr="Port_"+8;
			break;	
		}	
		System.out.println("portStr:"+portStr);
		return portStr;

	}

	public String hasDirectionString(String endPort,String cardType,String dir) {
		//System.out.println("1endPort:"+endPort+" dir:"+dir);
		String portNumStr=getPortNumberFromDirection(dir);
		if(endPort.equals("") || portNumStr.equals("") || cardType.contains("PA/BA"))
		{
			System.out.println("2endPort:"+endPort+" dir:"+dir);
			return endPort;
		}
		if(cardType.contains("OCM"))
		{
			return (endPort+"_"+portNumStr);
		}
		if(endPort.contains(MapConstants.EAST) || endPort.contains(MapConstants.WEST) || endPort.contains(MapConstants.NORTH) 
				|| endPort.contains(MapConstants.SOUTH) || endPort.contains(MapConstants.NE) || endPort.contains(MapConstants.NW) || 
				endPort.contains(MapConstants.SE) || endPort.contains(MapConstants.SW) ) {

			String changedEndPort=endPort.replace(dir, portNumStr);	
			System.out.println("3endPort:"+changedEndPort+" dir:"+dir);
			return changedEndPort;
		}
		else return endPort;

	}
}
