/**
 * 
 */
package application.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

import org.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;

import application.MainMap;
import application.MySqlThread;
import application.constants.MapConstants;
import application.model.Network;
import application.service.DbService;

/**
 * @author hp
 * @brief  This Class created to get common and repeatedly require info such as 
 * 			network ids (brown field and green field) throughout the network
 * @date   28/9/2017	
 *
 */
public class MapWebCommonAPIs {

	/**
	 * 
	 * @param jsonString
	 * @brief Following Mapping to Identify any network type:
	 * 
		 * --------------------------------------------------------
		 * |	     B.F.Id     |    Meaning					  |
		 * --------------------------------------------------------
		 * |          0			| Green Field(No BrownField exist)| 	 
		 * --------------------------------------------------------
		 * |          >0		| Green Field(BrownField exist)	  |
		 * --------------------------------------------------------
		 * |          -1		| Brown Field                     |
		 * --------------------------------------------------------
		 *               
	 * @return
	 */
	
	// Static nested class
	public static class NestedStaticClass{
		public static HashMap<String, Object> getNetworkInformation(DbService dbService, String jsonString){
			
			HashMap<String,Object>  networkInformationMap = new HashMap<String, Object>();
			System.out.println("Preparing Static Map : jsonString : "+ jsonString );

			JSONObject jsonMapDataObj = new JSONObject(jsonString);		
			JSONObject networkInfoObj=(JSONObject)jsonMapDataObj.get(MapConstants.NetworkInfo);
			 
			/**Find out GreenField and BrownField NetworkIds*/
			String networkName =networkInfoObj.getString(MapConstants.NetworkName);
			Network network=dbService.getNetworkService().GetNetworkInfoByNetworkName(networkName);
			
			/**Prepare Map to Return*/
			networkInformationMap.put(MapConstants.NetworkName,networkInfoObj.getString(MapConstants.NetworkName));
			networkInformationMap.put(MapConstants.UserName,networkInfoObj.getString(MapConstants.UserName));
			networkInformationMap.put(MapConstants.NetworkId, network.getNetworkId());		
			
			if(network.getNetworkIdBrownField()==MapConstants.BrownFieldBfId){/**If it's BrownField */
			
				networkInformationMap.put(MapConstants.NetworkType, String.valueOf(MapConstants.BrownField));
				
				Network gfNetwork=dbService.getNetworkService().GetNetworkInfoByBFNetworkId(network.getNetworkId());	        	
				networkInformationMap.put(MapConstants.GreenFieldId, gfNetwork.getNetworkId());
				networkInformationMap.put(MapConstants.BrownFieldId, network.getNetworkId());	        	
			}
			else if(network.getNetworkIdBrownField()==MapConstants.GreenFieldBfId){/**It's GreenField*/		
				networkInformationMap.put(MapConstants.NetworkType, MapConstants.GreenField);
				
				networkInformationMap.put(MapConstants.GreenFieldId,network.getNetworkId());			
			}
			
			return networkInformationMap;
				
		}	
		 
		/**
		 * Static Map to be used all across application which contains the list of Old Demands which were there in GreenField
		 */
		public static HashMap<Integer, Object> oldDemandObjMap = new HashMap<Integer , Object>();
		
		
		/**
		 * Global Lock to Run the Thread 
		 */
		public static boolean MySqlThreadLock = false;
		
		/**
		 * Thread Sleeping Time
		 */
		public static long MySqlThreadSleepTimer = (60000 * 60 * 5) ;/** seconds * minutes * hours */
		
		/**
		 * Run thread to Keep Mysql server interactive
		 * @param  dbService 
		 * @return void 
		 */		
		public static void SpawnMySqlThread(DbService dbService) {
			 MySqlThread mySqlThreadObj = new  MySqlThread("MySqlThread", dbService);
			 mySqlThreadObj.start();
		}
		
		
		/**
		 * This API Generate Both the ConfigPath Files into TimeStamp based Directory 
		 * @param jsonStr
		 * @param dbService
		 * @return
		 * @throws IOException 
		 * @date 30th July, 2018
		 * @author hp
		 */
		public static int GenerateCombinedConfigFiles(String jsonStr, DbService dbService) {
			
			
			/**Delete Operations*/
				File controlPathFileDir = new File(MapConstants.ControlConfigDirectory);				
				try {
					FileUtils.forceDelete(controlPathFileDir);/**delete all control path files*/
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}	

				
				try {
					controlPathFileDir = new File(MapConstants.DataConfigDirectory);					
					FileUtils.forceDelete(controlPathFileDir);/**delete all data path files*/
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				

			/**Control Path*/			
				
				MapWebGenerateControlPathConfigFile generateControlPathConfigFileObj = new MapWebGenerateControlPathConfigFile();

				int responseVal = generateControlPathConfigFileObj.generateControlPathConfigFile(jsonStr, dbService);

				MainMap.logger.debug("Generate Conmtrol Config File Request Processed .. ");
				
				if(responseVal==MapConstants.FAILURE)
					return responseVal;

			
			
			/**Data Path*/
				
				MapWebGenerateDataPathConfigFile generateDataPathConfigFileObj = new MapWebGenerateDataPathConfigFile();

				responseVal = generateDataPathConfigFileObj.generateDataPathConfigFile(jsonStr, dbService);

				MainMap.logger.debug("Generate Data Config File Request Processed .. ");

				if(responseVal==MapConstants.FAILURE)
					return responseVal;
			
				
			/**Copy Both Directory into the new one*/
		         
		         DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
		     	 Date date = new Date();
		     	 MainMap.logger.debug(dateFormat.format(date)); //30_07_2018_02_07_00
		         
		         String destFileDirName = getNetworkInformation(dbService, jsonStr).get(MapConstants.NetworkName).toString();
		         destFileDirName+="$"+dateFormat.format(date); /** NetworkName$dd_MM_yyyy_HH_mm_ss  */
		        
		         MainMap.logger.debug(" destFileDirName : "+destFileDirName);		         
		         
		         
		       /**Prepare Destination folder with ControlPath and DataPath Directory & Final Copy Call*/
		         try {
		        	 File destFileDir = new File(MapConstants.ConfigDirectory+"/"+destFileDirName);
			         FileUtils.forceMkdir(destFileDir);
			      		         
			         File localSrcFileDir = new File(MapConstants.ControlConfigDirectory); 
			         
					 File localDestDir = new File(destFileDir+"/ControlPath");				 
					 FileUtils.forceMkdir(localDestDir);
					 
					 FileUtils.copyDirectory(localSrcFileDir, localDestDir);
					 
					 localSrcFileDir = new File(MapConstants.DataConfigDirectory);
					 localDestDir    = new File(destFileDir+"/DataPath");
					 	
					 FileUtils.forceMkdir(localDestDir);
					 
					 FileUtils.copyDirectory(localSrcFileDir, localDestDir);
					 
					 return MapConstants.SUCCESS;
		         }
		         catch (IOException e) {

		        	 e.printStackTrace();
		        	 return MapConstants.FAILURE; 
				}
			
		}		
	
	}
	
	
	public static void copyDataBetweenNetworks(int networkidGrField,int networkidBrField,DbService dbService) throws SQLException {
		if (dbService.getNodeService().Count(networkidBrField) == MapConstants.I_ZERO)
			dbService.getNodeService().CopyNodeDataInBrField(networkidGrField, networkidBrField);

		if (dbService.getLinkService().Count(networkidBrField) == MapConstants.I_ZERO)
			dbService.getLinkService().CopyLinkDataInBrField(networkidGrField, networkidBrField);

		if (dbService.getTopologyService().Count(networkidBrField) == MapConstants.I_ZERO)
			dbService.getTopologyService().CopyTopologyDataInBrField(networkidGrField, networkidBrField);

		if (dbService.getCircuitService().Count(networkidBrField) == MapConstants.I_ZERO)
			dbService.getCircuitService().CopyCircuitDataInBrField(networkidGrField, networkidBrField);

		if (dbService.getDemandService().Count(networkidBrField) == MapConstants.I_ZERO)
			dbService.getDemandService().CopyDemandDataInBrField(networkidGrField, networkidBrField);

		if (dbService.getCircuitDemandMappingService().Count(networkidBrField) == MapConstants.I_ZERO)
			dbService.getCircuitDemandMappingService().CopyCircuitDemandMappingDataInBrField(networkidGrField,
					networkidBrField);

		if (dbService.getEquipmentPreferenceService().Count(networkidBrField) == MapConstants.I_ZERO) {
			dbService.getEquipmentPreferenceService().InitializeEqPreferencesBrField(networkidGrField,
					networkidBrField);
		}

		if (dbService.getParametricPreferenceService().Count(networkidBrField) == MapConstants.I_ZERO) {
			dbService.getParametricPreferenceService().InitializeParamPrefbrField(networkidGrField,
					networkidBrField);
		}
		
		if (dbService.getStockService().Count(networkidBrField) == MapConstants.I_ZERO) {
			dbService.getStockService().InitializeParamPrefbrField(networkidGrField,
					networkidBrField);
		}
		

		if (dbService.getAllocationExceptionsService().Count(networkidBrField) == MapConstants.I_ZERO) {
			dbService.getAllocationExceptionsService().InitializeParamPrefbrField(networkidGrField,
					networkidBrField);
		}
		
		
		/**Lambda Lsp information copy */
		if (dbService.getLambdaLspInformationSerivce().Count(networkidBrField) == MapConstants.I_ZERO) {
			dbService.getLambdaLspInformationSerivce().CopyLambdaLspInformationInBrField(networkidGrField,
					networkidBrField);
		}

		/**Otn Lsp information copy */
		if (dbService.getOtnLspInformationSerivce().Count(networkidBrField) == MapConstants.I_ZERO) {
			dbService.getOtnLspInformationSerivce().CopyOtnLspInformationInBrField(networkidGrField,
					networkidBrField);
		}


		/**Node IP information copy */
		if (dbService.getIpSchemeNodeService().Count(networkidBrField) == MapConstants.I_ZERO) {
			dbService.getIpSchemeNodeService().CopyIpSchemeNodeInBrField(networkidGrField,
					networkidBrField);
		}

		/**Link IP information copy */
		if (dbService.getIpSchemeLinkService().Count(networkidBrField) == MapConstants.I_ZERO) {
			dbService.getIpSchemeLinkService().CopyIpSchemeLinkInBrField(networkidGrField,
					networkidBrField);
		}	
		
		
		 if(dbService.getNetworkRouteService().Count(networkidBrField)==MapConstants.I_ZERO)
			 dbService.getNetworkRouteService().CopyNetworkRouteDataInBrField(networkidGrField, networkidBrField);
		
		/**Circuit10gAgg copy */
		if ( MapConstants.I_ZERO == dbService.getCircuit10gAggService().Count(networkidBrField)) {
			dbService.getCircuit10gAggService().CopyCircuit10GAggInBrField(networkidGrField,
					networkidBrField);
		}	
	}


	/**
	 * Common API to Get JSONArray for 10G Agg Circuits 
	 * @param dbService
	 * @param circuit10GAggList
	 * @date Nov 1, 2018
	 * @autho hp
	 * @return
	 */
	public static JSONArray ConvertCircuit10GAggToJSON(DbService dbService,  List<Map<String,Object>> circuit10GAggList){

			JSONArray AggregatorCircuitsArray  = new JSONArray();					

			MainMap.logger.debug("	circuit10GAggList : "+ circuit10GAggList);															

			if(!circuit10GAggList.isEmpty()){

				for (Map<String, Object> listObj : circuit10GAggList) {
					
					JSONObject  listJsonObj = new JSONObject();
					listJsonObj.put("ClientRate", listObj.get("RequiredTraffic"));
					listJsonObj.put("ServiceType", listObj.get("TrafficType"));
					listJsonObj.put("Multiplier", listObj.get("Count(*)"));

					AggregatorCircuitsArray.add(listJsonObj);							
				}
			}

			MainMap.logger.debug("AggregatorCircuitsArray : "+AggregatorCircuitsArray);

			return AggregatorCircuitsArray;
	}
}
