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
import application.service.DbService;

/**
 * @author hp
 * @brief  This Class provide the API to generate JSON for Optical Power Measurement which has two parts of DB as follows:
 *          PART I:   Node Data 
 *          PART II:  Optical Data
 *                 
 *         Finally return the same object to the Main Cotroller 
 *          
 */
public class MapWebOpticalPowerData {

	/**
	 * 
	 * @param dbService
	 * @return JSONObject
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public JSONObject MapWebGetOpticalPowerJsonReq(String jsonStr, DbService dbService) throws SQLException {
       
		/**Map From Common API*/ 
		HashMap<String, Object> networkInfoMap = MapWebCommonAPIs.NestedStaticClass.getNetworkInformation(dbService,jsonStr); 
				
		JSONObject jsonFinalObj = new JSONObject();
        // int networkId=Integer.parseInt(dbService.getNetworkService().GetByNetworkName("TestNetwork").toString());
        int networkId= (int)networkInfoMap.get(MapConstants.NetworkId); /**Network Id for common use*/ 
		
        System.out.println("Inside MapWebGetOpticalPowerJsonReq and Going to Return the JSon for Optical Power Measurement for NetworkId : : "+networkId);
        
        GenerateLinkWavelengthMap generateLinkWavelengthMap=new GenerateLinkWavelengthMap(dbService);       
		try {
			List<LinkWavelengthMap> opticalDataList = generateLinkWavelengthMap.fsendLinkWavelengthMap(networkId);
			JSONArray jsonOpticalDataArrayObj = new JSONArray();
			for(int i=0;i<opticalDataList.size();i++)
			{
				JSONObject jsonTempObj = new JSONObject();
				jsonTempObj.put("NodeId",opticalDataList.get(i).getNodeId());
				jsonTempObj.put("Wavelength",opticalDataList.get(i).getnWaves());
				jsonTempObj.put("SpanLoss",opticalDataList.get(i).getSpanLoss());
				jsonTempObj.put("Direction",MapConstants.fGetDirectionStrVal(opticalDataList.get(i).getDirection()));
				System.out.println("Num Of MPNS"+opticalDataList.get(i).getnMpns());
				jsonTempObj.put("NumOfMpns",opticalDataList.get(i).getnMpns());
				jsonTempObj.put("LinkId",opticalDataList.get(i).getLinkId());
				jsonTempObj.put("NumOfWaves",opticalDataList.get(i).getWavelengths());		
				jsonOpticalDataArrayObj.add(jsonTempObj);
			}
			jsonFinalObj.put("NodeOpticalInfoArray", jsonOpticalDataArrayObj);
		} catch (Exception e) {
			// TODO: handle exception
			JSONArray jsonOpticalDataArrayObj = new JSONArray();
			jsonFinalObj.put("NodeOpticalInfoArray", jsonOpticalDataArrayObj);
		}		
		
		List<Node> nodeList = dbService.getNodeService().FindAll(networkId);
		
		JSONArray jsonNodeDataArrayObj = new JSONArray();
		for(int i=0;i<nodeList.size();i++)
		{
			JSONObject jsonNodeDataObj = new JSONObject();
			jsonNodeDataObj.put("NodeType",nodeList.get(i).getNodeType());		
			jsonNodeDataObj.put("NodeId",nodeList.get(i).getNodeId());		
			jsonNodeDataArrayObj.add(jsonNodeDataObj);
		}
		jsonFinalObj.put("NodeArray", jsonNodeDataArrayObj);
		
		//System.out.println("Final OpticalPower View json Object ::"+jsonFinalObj);

		return jsonFinalObj;			

	}
}
