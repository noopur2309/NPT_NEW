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
import application.model.*;
import application.model.NetworkRoute;
import application.model.Node;
import application.model.CardInfo;
import application.service.DbService;
import application.service.IPv4;

import org.apache.log4j.Logger;

/**
 * Graph Controller
 * 
 * @param data
 * @return
 */
public class NewDashboardController {

	public static Logger logger = Logger.getLogger(NewDashboardController.class.getName());
	DbService dbService;

	@SuppressWarnings("unchecked")
	public JSONObject GenerateGraphModal(String data, DbService dbService) {
		NewDashboardController.logger.info("GenerateReportModal data : " + data);
		NewDashboardController.logger = MainMap.logger.getLogger(NewDashboardController.class.getName());

		JSONObject FinaldataObj = new JSONObject();

		/** Map From Common API */
		/*
		 * HashMap<String, Object> networkInfoMap =
		 * MapWebCommonAPIs.NestedStaticClass.getNetworkInformation(dbService,data); int
		 * networkId = (int) networkInfoMap.get(MapConstants.NetworkId);
		 *//** Network Id for common use *//*
											 * String networkName= (String)
											 * networkInfoMap.get(MapConstants.NetworkName);
											 * 
											 * String blockLinkAdd=MapConstants.block1LinkStartAddress; String
											 * blockNodeAdd=MapConstants.block1NodeStartAddress;
											 */

		FinaldataObj.put("Network_Power", NetworkId_Power(dbService));
		FinaldataObj.put("Network_Price", NetworkId_Price(dbService));

		NewDashboardController.logger.info("FinalDataObj:" + FinaldataObj);


		return FinaldataObj;

	}

	// Fetching data for Node Id and Total Price from DB//
	@SuppressWarnings("unchecked")
	public JSONObject NetworkId_Power(DbService dbService) {
		GraphController.logger.info("Network Id and Total Power Function");
		int i = 0;
		JSONObject powerObj = new JSONObject();
	
		JSONArray power_array = new JSONArray();
		JSONObject objectforpower = new JSONObject();
		JSONObject object1forpower = new JSONObject();
		JSONArray xAxesforpower = new JSONArray();
		JSONArray yAxesforpower = new JSONArray();
		JSONObject finalLabelObjforpower = new JSONObject();
		JSONObject labelobjforpower = new JSONObject();
		JSONObject finaljsonObjectforpower = new JSONObject();
		JSONObject finalObjectforpower = new JSONObject();
		JSONObject finalObject1forpower = new JSONObject();
		JSONObject finaljsonObject1forpower = new JSONObject();
		JSONObject titleObjforpower = new JSONObject();
		JSONObject legendObjPower = new JSONObject();

		GraphController.logger.info("Node Data");
		List<Network> allNetworks = dbService.getNetworkService().FindAllNetworks();
		// Fetching Node Id's from Db of the respective Network ID//
		// GraphController.logger.debug(allNetworks);
		List<Object> Totalpower = new ArrayList<Object>();
		List<Object> NetworkNameArray = new ArrayList<Object>();
		// Fetching sum of Power from db//
		for (i = 0; i < allNetworks.size(); i++) {
			NetworkNameArray.add(dbService.getNetworkService().FindNetwork(allNetworks.get(i).getNetworkId()).getNetworkName());
			System.out.println("NetworkArray" + 	NetworkNameArray);
			Totalpower.add(dbService.getViewServiceRp().FindTotalPower(allNetworks.get(i).getNetworkId()));
			System.out.println("Totalpower" + Totalpower);

		}

		GraphController.logger.debug(Totalpower);

		// Data in JSON form used for plotting the graphs//

		powerObj.put("label", "Power(db)");
		powerObj.put("data", Totalpower);
		powerObj.put("backgroundColor", "#2BBBAD");
		powerObj.put("pointBackgroundColor","#00695c");
		powerObj.put("borderWidth","1");
		powerObj.put("pointBorderColor","#00695c");

		power_array.add(powerObj);

		objectforpower.put("display", "true");
		objectforpower.put("labelString", "Total Network");
		object1forpower.put("scaleLabel", objectforpower);
		object1forpower.put("display", "true");
		xAxesforpower.add(object1forpower);

		labelobjforpower.put("display", "true");
		labelobjforpower.put("labelString", "Total Power(db)");
		finalLabelObjforpower.put("scaleLabel", labelobjforpower);
		finalLabelObjforpower.put("display", "true");
		yAxesforpower.add(finalLabelObjforpower);

		finaljsonObjectforpower.put("labels", NetworkNameArray);
		finaljsonObjectforpower.put("datasets", power_array);
		finaljsonObject1forpower.put("xAxes", xAxesforpower);
		finaljsonObject1forpower.put("yAxes", yAxesforpower);

		titleObjforpower.put("display", "true");
		titleObjforpower.put("text", "NetworkWise Power Distribution");

		finalObject1forpower.put("scales", finaljsonObject1forpower);
		finalObject1forpower.put("title", titleObjforpower);
		legendObjPower.put("display", true);
		finalObject1forpower.put("legend", legendObjPower);
		finalObjectforpower.put("type", "line");
		finalObjectforpower.put("data", finaljsonObjectforpower);
		finalObjectforpower.put("options", finalObject1forpower);

	

		return finalObjectforpower;

	}
	
	// Fetching data for Node Id and Total Price from DB//
		@SuppressWarnings("unchecked")
		public JSONObject NetworkId_Price(DbService dbService) {
			GraphController.logger.info("Network Id and Total Price Function");
			int i = 0;
			JSONObject priceObj = new JSONObject();
		
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
			JSONObject legendObjprice = new JSONObject();

			GraphController.logger.info("Node Data");
			List<Network> allNetworks = dbService.getNetworkService().FindAllNetworks();
			// Fetching Node Id's from Db of the respective Network ID//
			// GraphController.logger.debug(allNetworks);
			List<Object> Totalprice = new ArrayList<Object>();
			List<Object> NetworkNameArray = new ArrayList<Object>();
			// Fetching sum of Power from db//
			for (i = 0; i < allNetworks.size(); i++) {
		NetworkNameArray.add(dbService.getNetworkService().FindNetwork(allNetworks.get(i).getNetworkId()).getNetworkName());
				
				System.out.println("NetworkArray" + 	NetworkNameArray);
				Totalprice.add(dbService.getViewServiceRp().FindTotalPrice(allNetworks.get(i).getNetworkId()));
				System.out.println("Totalprice" + Totalprice);

			}

			GraphController.logger.debug(Totalprice);

			// Data in JSON form used for plotting the graphs//

			priceObj.put("label", "Total Price");
			priceObj.put("data", Totalprice);
			priceObj.put("backgroundColor", "#880e4f");
			

			price_array.add(priceObj);

			objectforprice.put("display", "true");
			objectforprice.put("labelString", "Total Network");
			object1forprice.put("scaleLabel", objectforprice);
			object1forprice.put("display", "true");
			xAxesforprice.add(object1forprice);

			labelobjforprice.put("display", "true");
			labelobjforprice.put("labelString", "Total Price($)");
			finalLabelObjforprice.put("scaleLabel", labelobjforprice);
			finalLabelObjforprice.put("display", "true");
			yAxesforprice.add(finalLabelObjforprice);

			finaljsonObjectforprice.put("labels", NetworkNameArray);
			finaljsonObjectforprice.put("datasets", price_array);
			finaljsonObject1forprice.put("xAxes", xAxesforprice);
			finaljsonObject1forprice.put("yAxes", yAxesforprice);

			titleObjforprice.put("display", "true");
			titleObjforprice.put("text", "NetworkWise Price Distribution");

			finalObject1forprice.put("scales", finaljsonObject1forprice);
			finalObject1forprice.put("title", titleObjforprice);
			legendObjprice.put("display", true);
			finalObject1forprice.put("legend", legendObjprice);
			finalObjectforprice.put("type", "bar");
			finalObjectforprice.put("data", finaljsonObjectforprice);
			finalObjectforprice.put("options", finalObject1forprice);

			System.out.println("finalObjectforprice" + finalObjectforprice);

			return finalObjectforprice;

		}

}
