/**
 * 
 */
package npt.map.application.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;



/**
 * @author hp
 *
 */
@Controller
@RequestMapping("/")
public class MapWebMainController {

	@RequestMapping(value = "/save", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")

	public @ResponseBody String updateHosting(@RequestBody String obj) throws JSONException {
		System.out.println("**************************** Map data : *******************************");/// .getName()+
																										/// "
																										/// and
																										/// "+obj.getSName());
		JSONObject jsonObj = new JSONObject(obj);
		NetworkController nw = new NetworkController();

		try {
			JSONArray array = (JSONArray) jsonObj.get("cells");
		
			int i = 0,j=0;
			String nodeProps;
			System.out.println("length"+array.length());
			for (i = 0; i < array.length(); i++) {
				JSONObject obj1 = array.getJSONObject(i);
				System.out.println("Here i go ..." + obj1.get("size").toString());
				System.out.println("Here i go ..." + obj1.get("nodeProperties").toString());
				
				 nodeProps = obj1.get("nodeProperties").toString();
				 
				JSONObject nodeProps1 = (JSONObject)obj1.get("nodeProperties");
				System.out.println("node props "+nodeProps1.get("siteName"));
				
				System.out.println("Final: "+nodeProps.substring(38,40));
				
				System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
				
				Network network = null;
				  
				network = new Network(nodeProps1.get("siteName").toString());
				networkDao.save(network);
				
				/*
				JSONObject jsonObjNode = new JSONObject(nodeProps);
				JSONArray nodeArray = (JSONArray) jsonObjNode.get("nodeProperties");
				
				System.out.println("node array len"+nodeArray.length());
				
				for(j=0;j<nodeArray.length();j++){
					JSONObject obj2 = nodeArray.getJSONObject(i);
					System.out.println("Here i go Again ..." + obj2.get("stationName").toString());
					System.out.println("Here i go Again ..." + obj2.get("siteName").toString());

					Network network = null;
				  
					network = new Network(obj2.get("stationName").toString());
					networkDao.save(network);
				}
*/
				
				
				
				//nw.create("TestBench");
				
			}
			
			
			
			// System.out.println(array.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Obj " + obj);

		// System.out.println("Station Name : "+obj.getStationName());
		// System.out.println("Site Name : "+obj.getSiteName());
		// System.out.println("Cells : "+obj.getCells());
		
		 /*System.out.println("GNE Flag : "+obj.getGneFlag());
		 System.out.println("Vlan Tag : "+obj.getVlanTag());
		 */
		System.out.println("**************************** Map data : *******************************");

		return null;
	}

	@RequestMapping("/saveTest")
	public String testMethod() {
		System.out.println("DBG => Test In other Package ");
		return "Successs Test!!";
	}
	
	@Autowired  
	   UserDao userDaoMap;
	
	  // ------------------------
	  // PRIVATE FIELDS
	  // ------------------------

	  @Autowired
	  private NetworkDao networkDao;
}
