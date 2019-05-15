/**
 * 
 */
package npt.map.web.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
/*
	@RequestMapping(value = "/save", method = RequestMethod.POST, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")

	public @ResponseBody String updateHosting(@RequestBody String obj) throws JSONException {
		System.out.println("**************************** Map data : *******************************");/// .getName()+
																										/// "
																										/// and
																										/// "+obj.getSName());
		JSONObject jsonObj = new JSONObject(obj);

		try {
			JSONArray array = (JSONArray) jsonObj.get("cells");
		
			int i = 0;
		
			for (i = 0; i < array.length(); i++) {
				JSONObject obj1 = array.getJSONObject(i);
				System.out.println("Here i go ..." + obj1.get("size").toString());
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
		
		 System.out.println("GNE Flag : "+obj.getGneFlag());
		 System.out.println("Vlan Tag : "+obj.getVlanTag());
		 
		System.out.println("**************************** Map data : *******************************");

		return null;
	}*/

	@RequestMapping("/save")
	public String testMethod() {
		System.out.println("DBG => Test In other Package ");
		return "Successs Test!!";
	}
}
