/**
 * 
 */
package application.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import application.MainMap;
import application.constants.MapConstants;
import application.model.DemandMapping;

import application.model.RouteMapping;
import application.service.DbService;

/**
 * @author hp
 * @date 13 Feb, 2017
 * @brief
 *
 */
public class MapWebGenerateDemandMatrix {

	@SuppressWarnings({ "unchecked", "static-access" })
	public JSONObject generateDemanadMatrix(String jsonStr,DbService dbService) throws SQLException {

		/**Logger Start*/		
		 MainMap.logger = MainMap.logger.getLogger(MapWebCircuitInputSave.class.getName());
		
		MainMap.logger.info("--------------------------Demand Matrix Generation Start---------------------------------------");
		
		/**Map From Common API*/ 
		HashMap<String, Object> networkInfoMap = MapWebCommonAPIs.NestedStaticClass.getNetworkInformation(dbService,jsonStr); 
			
		int networkId=  (int) networkInfoMap.get(MapConstants.NetworkId);
		int greenFieldNetworkId=MapConstants.I_ZERO;
		String state=MapConstants.GreenFieldState;
		
		System.out.println("NetworkId:"+networkId+"network type -- "+(String)networkInfoMap.get(MapConstants.NetworkType)+" GF ID"+(int)networkInfoMap.get(MapConstants.GreenFieldId));
		if(((String)networkInfoMap.get(MapConstants.NetworkType)).equalsIgnoreCase(MapConstants.BrownField)){ 
			greenFieldNetworkId=(int)networkInfoMap.get(MapConstants.GreenFieldId); /**Network Id for common use*/        
		}
		
		
		 
		/**Insert Dummy data : To NetworkRoute*/
       /*	NetworkRoute networkRoute1 = new NetworkRoute(1,1,1,"1-2-3",50,5,60,1,2);
		NetworkRoute networkRoute2 = new NetworkRoute(1,1,2,"1-2",50,5,60,1,2);
	
		dbService.getNetworkRouteService().InsertNetworkRoute(networkRoute1);
		dbService.getNetworkRouteService().InsertNetworkRoute(networkRoute2);*/
		
		/*JSONObject jsonView1Obj = new JSONObject();
		jsonView1Obj.put("type","view1");*/
		JSONArray jsonArrayView1Obj = new JSONArray();
		
		/**Fetch View1*/
		
		//List<RouteMapping> routeMappingObj = dbService.getViewService().FindRouteMapping();
		List<RouteMapping> routeMappingObj = dbService.getViewService().FindRouteMapping(networkId);
		
		//If brownField Network , then fetch BF data if exists else use GF NetworkId to get GF data
		if(((String)networkInfoMap.get(MapConstants.NetworkType)).equalsIgnoreCase(MapConstants.BrownField) && routeMappingObj.size()!=MapConstants.I_ZERO){ 
			System.out.println("bf id"+networkId+" GF ID"+greenFieldNetworkId);
			List<RouteMapping> routeMappingObjOld = dbService.getNetworkRouteService().FindCommonRouteDemMappedInBrField(greenFieldNetworkId, networkId);
			System.out.println("routeMappingObjOld:"+ routeMappingObjOld.toString());
			for(int i=0; i<routeMappingObjOld.size(); i++){			
				
				JSONObject jsonRowObj = new JSONObject();
				jsonRowObj.put("NetowrkId", routeMappingObjOld.get(i).getNetworkId());
				jsonRowObj.put("DemandId", routeMappingObjOld.get(i).getDemandId());
				jsonRowObj.put("SrcNodeId", routeMappingObjOld.get(i).getSrcNodeId());
				jsonRowObj.put("DestNodeId", routeMappingObjOld.get(i).getDestNodeId());
				jsonRowObj.put("Traffic", routeMappingObjOld.get(i).getTraffic());
				jsonRowObj.put("ProtectionType", routeMappingObjOld.get(i).getProtectionType());
				jsonRowObj.put("Path", routeMappingObjOld.get(i).getPath());
				jsonRowObj.put("PathType", routeMappingObjOld.get(i).getPathType());
				jsonRowObj.put("WavelengthNo", routeMappingObjOld.get(i).getWavelengthNo());
				jsonRowObj.put("Osnr", routeMappingObjOld.get(i).getOsnr());
				jsonRowObj.put("Spanlength", routeMappingObjOld.get(i).getRouteLength());
				jsonRowObj.put("Cost", routeMappingObjOld.get(i).getRouteCost());
				jsonRowObj.put("RoutePriority", routeMappingObjOld.get(i).getRoutePriority());
				jsonRowObj.put("cd", routeMappingObjOld.get(i).getRouteCd());
				jsonRowObj.put("pmd", routeMappingObjOld.get(i).getRoutePmd());		
				jsonRowObj.put("regeneratorLoc", routeMappingObjOld.get(i).getRegeneratorLoc());	
				jsonRowObj.put("lineRate", routeMappingObjOld.get(i).getLineRate());
				jsonRowObj.put("error", routeMappingObjOld.get(i).getError());
				jsonRowObj.put("State", MapConstants.OldState);
				
				jsonArrayView1Obj.add(jsonRowObj);			
			}
			
			List<RouteMapping> routeMappingObjAdded = dbService.getNetworkRouteService().FindAddedRouteDemMappedInBrField(greenFieldNetworkId, networkId);
			System.out.println("routeMappingObjAdded:"+ routeMappingObjAdded.toString());
			for(int i=0; i<routeMappingObjAdded.size(); i++){			
				
				JSONObject jsonRowObj = new JSONObject();
				jsonRowObj.put("NetowrkId", routeMappingObjAdded.get(i).getNetworkId());
				jsonRowObj.put("DemandId", routeMappingObjAdded.get(i).getDemandId());
				jsonRowObj.put("SrcNodeId", routeMappingObjAdded.get(i).getSrcNodeId());
				jsonRowObj.put("DestNodeId", routeMappingObjAdded.get(i).getDestNodeId());
				jsonRowObj.put("Traffic", routeMappingObjAdded.get(i).getTraffic());
				jsonRowObj.put("ProtectionType", routeMappingObjAdded.get(i).getProtectionType());
				jsonRowObj.put("Path", routeMappingObjAdded.get(i).getPath());
				jsonRowObj.put("PathType", routeMappingObjAdded.get(i).getPathType());
				jsonRowObj.put("WavelengthNo", routeMappingObjAdded.get(i).getWavelengthNo());
				jsonRowObj.put("Osnr", routeMappingObjAdded.get(i).getOsnr());
				jsonRowObj.put("Spanlength", routeMappingObjAdded.get(i).getRouteLength());
				jsonRowObj.put("Cost", routeMappingObjAdded.get(i).getRouteCost());
				jsonRowObj.put("RoutePriority", routeMappingObjAdded.get(i).getRoutePriority());
				jsonRowObj.put("cd", routeMappingObjAdded.get(i).getRouteCd());
				jsonRowObj.put("pmd", routeMappingObjAdded.get(i).getRoutePmd());		
				jsonRowObj.put("regeneratorLoc", routeMappingObjAdded.get(i).getRegeneratorLoc());	
				jsonRowObj.put("lineRate", routeMappingObjAdded.get(i).getLineRate());
				jsonRowObj.put("error", routeMappingObjAdded.get(i).getError());
				jsonRowObj.put("State", MapConstants.NewState);
				
				jsonArrayView1Obj.add(jsonRowObj);			
			}
			
			
			List<RouteMapping> routeMappingObjDeleted = dbService.getNetworkRouteService().FindDeletedRouteDemMappedInBrField(greenFieldNetworkId, networkId);
			System.out.println("routeMappingObjDeleted:"+ routeMappingObjDeleted.toString());
			for(int i=0; i<routeMappingObjDeleted.size(); i++){			
				
				JSONObject jsonRowObj = new JSONObject();
				jsonRowObj.put("NetowrkId", routeMappingObjDeleted.get(i).getNetworkId());
				jsonRowObj.put("DemandId", routeMappingObjDeleted.get(i).getDemandId());
				jsonRowObj.put("SrcNodeId", routeMappingObjDeleted.get(i).getSrcNodeId());
				jsonRowObj.put("DestNodeId", routeMappingObjDeleted.get(i).getDestNodeId());
				jsonRowObj.put("Traffic", routeMappingObjDeleted.get(i).getTraffic());
				jsonRowObj.put("ProtectionType", routeMappingObjDeleted.get(i).getProtectionType());
				jsonRowObj.put("Path", routeMappingObjDeleted.get(i).getPath());
				jsonRowObj.put("PathType", routeMappingObjDeleted.get(i).getPathType());
				jsonRowObj.put("WavelengthNo", routeMappingObjDeleted.get(i).getWavelengthNo());
				jsonRowObj.put("Osnr", routeMappingObjDeleted.get(i).getOsnr());
				jsonRowObj.put("Spanlength", routeMappingObjDeleted.get(i).getRouteLength());
				jsonRowObj.put("Cost", routeMappingObjDeleted.get(i).getRouteCost());
				jsonRowObj.put("RoutePriority", routeMappingObjDeleted.get(i).getRoutePriority());
				jsonRowObj.put("cd", routeMappingObjDeleted.get(i).getRouteCd());
				jsonRowObj.put("pmd", routeMappingObjDeleted.get(i).getRoutePmd());		
				jsonRowObj.put("regeneratorLoc", routeMappingObjDeleted.get(i).getRegeneratorLoc());	
				jsonRowObj.put("lineRate", routeMappingObjDeleted.get(i).getLineRate());
				jsonRowObj.put("error", routeMappingObjDeleted.get(i).getError());
				jsonRowObj.put("State", MapConstants.DeleteState);
				
				jsonArrayView1Obj.add(jsonRowObj);			
			}
			
			
			List<RouteMapping> routeMappingObjModified = dbService.getNetworkRouteService().FindModifiedRouteDemMappedInBrField(greenFieldNetworkId, networkId);
			System.out.println("routeMappingObjModified:"+ routeMappingObjModified.toString());
			for(int i=0; i<routeMappingObjModified.size(); i++){			
				
				JSONObject jsonRowObj = new JSONObject();
				jsonRowObj.put("NetowrkId", routeMappingObjModified.get(i).getNetworkId());
				jsonRowObj.put("DemandId", routeMappingObjModified.get(i).getDemandId());
				jsonRowObj.put("SrcNodeId", routeMappingObjModified.get(i).getSrcNodeId());
				jsonRowObj.put("DestNodeId", routeMappingObjModified.get(i).getDestNodeId());
				jsonRowObj.put("Traffic", routeMappingObjModified.get(i).getTraffic());
				jsonRowObj.put("ProtectionType", routeMappingObjModified.get(i).getProtectionType());
				jsonRowObj.put("Path", routeMappingObjModified.get(i).getPath());
				jsonRowObj.put("PathType", routeMappingObjModified.get(i).getPathType());
				jsonRowObj.put("WavelengthNo", routeMappingObjModified.get(i).getWavelengthNo());
				jsonRowObj.put("Osnr", routeMappingObjModified.get(i).getOsnr());
				jsonRowObj.put("Spanlength", routeMappingObjModified.get(i).getRouteLength());
				jsonRowObj.put("Cost", routeMappingObjModified.get(i).getRouteCost());
				jsonRowObj.put("RoutePriority", routeMappingObjModified.get(i).getRoutePriority());
				jsonRowObj.put("cd", routeMappingObjModified.get(i).getRouteCd());
				jsonRowObj.put("pmd", routeMappingObjModified.get(i).getRoutePmd());		
				jsonRowObj.put("regeneratorLoc", routeMappingObjModified.get(i).getRegeneratorLoc());	
				jsonRowObj.put("lineRate", routeMappingObjModified.get(i).getLineRate());
				jsonRowObj.put("error", routeMappingObjModified.get(i).getError());
				jsonRowObj.put("State", MapConstants.ModifiedState);
				
				jsonArrayView1Obj.add(jsonRowObj);			
			}
			
			
		}
		else
		{
			state=MapConstants.GreenFieldState;
			//Get GF data if no BF data exists (BF Case Only )
			if(greenFieldNetworkId!=MapConstants.I_ZERO)
			{
				networkId=greenFieldNetworkId;				
				state=MapConstants.OldState;
			}
			
			routeMappingObj = dbService.getViewService().FindRouteMapping(networkId);
			System.out.println("routeMappingObj:"+ routeMappingObj.toString());
			System.out.println("State for routeMapping Gf"+state);
			for(int i=0; i<routeMappingObj.size(); i++){			
				
				JSONObject jsonRowObj = new JSONObject();
				jsonRowObj.put("NetowrkId", routeMappingObj.get(i).getNetworkId());
				jsonRowObj.put("DemandId", routeMappingObj.get(i).getDemandId());
				jsonRowObj.put("SrcNodeId", routeMappingObj.get(i).getSrcNodeId());
				jsonRowObj.put("DestNodeId", routeMappingObj.get(i).getDestNodeId());
				jsonRowObj.put("Traffic", routeMappingObj.get(i).getTraffic());
				jsonRowObj.put("ProtectionType", routeMappingObj.get(i).getProtectionType());
				jsonRowObj.put("Path", routeMappingObj.get(i).getPath());
				jsonRowObj.put("PathType", routeMappingObj.get(i).getPathType());
				jsonRowObj.put("WavelengthNo", routeMappingObj.get(i).getWavelengthNo());
				jsonRowObj.put("Osnr", routeMappingObj.get(i).getOsnr());
				jsonRowObj.put("Spanlength", routeMappingObj.get(i).getRouteLength());
				jsonRowObj.put("Cost", routeMappingObj.get(i).getRouteCost());
				jsonRowObj.put("RoutePriority", routeMappingObj.get(i).getRoutePriority());
				jsonRowObj.put("cd", routeMappingObj.get(i).getRouteCd());
				jsonRowObj.put("pmd", routeMappingObj.get(i).getRoutePmd());		
				jsonRowObj.put("regeneratorLoc", routeMappingObj.get(i).getRegeneratorLoc());	
				jsonRowObj.put("lineRate", routeMappingObj.get(i).getLineRate());
				jsonRowObj.put("error", routeMappingObj.get(i).getError());
				jsonRowObj.put("State", state);
				
				jsonArrayView1Obj.add(jsonRowObj);			
			}
		}
		
		
		
		
		/*jsonView1Obj.put("view1", jsonArrayView1Obj);		
		MainMap.logger.info("Json View_1 Object JSON String: "+ jsonView1Obj.toJSONString());*/
		
		
		
		JSONArray jsonArrayView2Obj = new JSONArray();		
		/**Fetch View2*/
		List<DemandMapping> demandMappingObjList = dbService.getViewService().FindDemandMapping(networkId);
		if(((String)networkInfoMap.get(MapConstants.NetworkType)).equalsIgnoreCase(MapConstants.BrownField) && demandMappingObjList.size()!=MapConstants.I_ZERO){ 
			System.out.println("bf id"+networkId+" GF ID"+greenFieldNetworkId);
			
			
			List<DemandMapping> demandMappingObjCommonBf = dbService.getCircuitService().FindCommonCircuitsDemMappedInBrField(greenFieldNetworkId, networkId);
			System.out.println("demandMappingObjCommonBf Size::"+demandMappingObjCommonBf.size());
			for(DemandMapping demandMappingObj:demandMappingObjCommonBf){

				JSONObject jsonRowObj = new JSONObject();
				jsonRowObj.put("NetworkId", demandMappingObj.getNetworkId());
				jsonRowObj.put("DemandId", demandMappingObj.getDemandId());
				jsonRowObj.put("CircuitId", demandMappingObj.getCircuitId());
				jsonRowObj.put("SrcNodeId", demandMappingObj.getSrcNodeId());
				jsonRowObj.put("DestNodeId", demandMappingObj.getDestNodeId());
				jsonRowObj.put("RequiredTraffic", demandMappingObj.getRequiredTraffic());
				jsonRowObj.put("TrafficType", demandMappingObj.getTrafficType());
				jsonRowObj.put("ProtectionType", demandMappingObj.getProtectionType());
				jsonRowObj.put("ProtectionMechanism", demandMappingObj.getClientProtectionType());
				jsonRowObj.put("ChannelProtection", demandMappingObj.getChannelProtection());
				jsonRowObj.put("ColourPreference", demandMappingObj.getColourPreference());
				jsonRowObj.put("State", MapConstants.OldState);
				jsonRowObj.put("LineRate", demandMappingObj.getLineRate());
				jsonRowObj.put("PathType", demandMappingObj.getPathType());

				jsonArrayView2Obj.add(jsonRowObj);			
			}
			
//			List<DemandMapping> demandMappingObjModifiedBf = dbService.getCircuitService().FindAddedCircuitsDemMappedInBrField(greenFieldNetworkId, networkId);
//			for(DemandMapping demandMappingObj:demandMappingObjModifiedBf){
//
//				JSONObject jsonRowObj = new JSONObject();
//				jsonRowObj.put("NetworkId", demandMappingObj.getNetworkId());
//				jsonRowObj.put("DemandId", demandMappingObj.getDemandId());
//				jsonRowObj.put("CircuitId", demandMappingObj.getCircuitId());
//				jsonRowObj.put("SrcNodeId", demandMappingObj.getSrcNodeId());
//				jsonRowObj.put("DestNodeId", demandMappingObj.getDestNodeId());
//				jsonRowObj.put("RequiredTraffic", demandMappingObj.getRequiredTraffic());
//				jsonRowObj.put("TrafficType", demandMappingObj.getTrafficType());
//				jsonRowObj.put("ProtectionType", demandMappingObj.getProtectionType());
//				jsonRowObj.put("ProtectionMechanism", demandMappingObj.getClientProtectionType());
//				jsonRowObj.put("ChannelProtection", demandMappingObj.getChannelProtection());
//				jsonRowObj.put("ColourPreference", demandMappingObj.getColourPreference());
//				jsonRowObj.put("State", MapConstants.ModifiedSate);
//				jsonRowObj.put("LineRate", demandMappingObj.getLineRate());
//				jsonRowObj.put("PathType", demandMappingObj.getPathType());
//
//				jsonArrayView2Obj.add(jsonRowObj);			
//			}
			
			List<DemandMapping> demandMappingObjDeletedBf = dbService.getCircuitService().FindDeletedCircuitsDemMappedInBrField(greenFieldNetworkId, networkId);
			System.out.println("demandMappingObjDeletedBf Size::"+demandMappingObjDeletedBf.size());
			for(DemandMapping demandMappingObj:demandMappingObjDeletedBf){

				JSONObject jsonRowObj = new JSONObject();
				jsonRowObj.put("NetworkId", demandMappingObj.getNetworkId());
				jsonRowObj.put("DemandId", demandMappingObj.getDemandId());
				jsonRowObj.put("CircuitId", demandMappingObj.getCircuitId());
				jsonRowObj.put("SrcNodeId", demandMappingObj.getSrcNodeId());
				jsonRowObj.put("DestNodeId", demandMappingObj.getDestNodeId());
				jsonRowObj.put("RequiredTraffic", demandMappingObj.getRequiredTraffic());
				jsonRowObj.put("TrafficType", demandMappingObj.getTrafficType());
				jsonRowObj.put("ProtectionType", demandMappingObj.getProtectionType());
				jsonRowObj.put("ProtectionMechanism", demandMappingObj.getClientProtectionType());
				jsonRowObj.put("ChannelProtection", demandMappingObj.getChannelProtection());
				jsonRowObj.put("ColourPreference", demandMappingObj.getColourPreference());
				jsonRowObj.put("State", MapConstants.DeleteState);
				jsonRowObj.put("LineRate", demandMappingObj.getLineRate());
				jsonRowObj.put("PathType", demandMappingObj.getPathType());

				jsonArrayView2Obj.add(jsonRowObj);			
			}
			
			List<DemandMapping> demandMappingObjAddedBf = dbService.getCircuitService().FindAddedCircuitsDemMappedInBrField(greenFieldNetworkId, networkId);
			System.out.println("demandMappingObjAddedBf Size::"+demandMappingObjAddedBf.size());
			for(DemandMapping demandMappingObj:demandMappingObjAddedBf){

				JSONObject jsonRowObj = new JSONObject();
				jsonRowObj.put("NetworkId", demandMappingObj.getNetworkId());
				jsonRowObj.put("DemandId", demandMappingObj.getDemandId());
				jsonRowObj.put("CircuitId", demandMappingObj.getCircuitId());
				jsonRowObj.put("SrcNodeId", demandMappingObj.getSrcNodeId());
				jsonRowObj.put("DestNodeId", demandMappingObj.getDestNodeId());
				jsonRowObj.put("RequiredTraffic", demandMappingObj.getRequiredTraffic());
				jsonRowObj.put("TrafficType", demandMappingObj.getTrafficType());
				jsonRowObj.put("ProtectionType", demandMappingObj.getProtectionType());
				jsonRowObj.put("ProtectionMechanism", demandMappingObj.getClientProtectionType());
				jsonRowObj.put("ChannelProtection", demandMappingObj.getChannelProtection());
				jsonRowObj.put("ColourPreference", demandMappingObj.getColourPreference());
				jsonRowObj.put("State", MapConstants.NewState);
				jsonRowObj.put("LineRate", demandMappingObj.getLineRate());
				jsonRowObj.put("PathType", demandMappingObj.getPathType());

				jsonArrayView2Obj.add(jsonRowObj);			
			}
		}
		else	
		{
			state=MapConstants.GreenFieldState;
			//Get GF data if no BF data exists (BF Case Only )
			if(greenFieldNetworkId!=MapConstants.I_ZERO)
			{
				networkId=greenFieldNetworkId;				
				state=MapConstants.OldState;
			}
						
			demandMappingObjList = dbService.getViewService().FindDemandMapping(networkId);
			System.out.println("Green Field DemandMapping Size::"+demandMappingObjList.size());
			System.out.println("State of Data::"+state);
			for(DemandMapping demandMappingObj:demandMappingObjList){				
				JSONObject jsonRowObj = new JSONObject();
				jsonRowObj.put("NetworkId", demandMappingObj.getNetworkId());
				jsonRowObj.put("DemandId", demandMappingObj.getDemandId());
				jsonRowObj.put("CircuitId", demandMappingObj.getCircuitId());
				jsonRowObj.put("SrcNodeId", demandMappingObj.getSrcNodeId());
				jsonRowObj.put("DestNodeId", demandMappingObj.getDestNodeId());
				jsonRowObj.put("RequiredTraffic", demandMappingObj.getRequiredTraffic());
				jsonRowObj.put("TrafficType", demandMappingObj.getTrafficType());
				jsonRowObj.put("ProtectionType", demandMappingObj.getProtectionType());
				jsonRowObj.put("ProtectionMechanism", demandMappingObj.getClientProtectionType());
				jsonRowObj.put("ChannelProtection", demandMappingObj.getChannelProtection());
				jsonRowObj.put("ColourPreference", demandMappingObj.getColourPreference());
				jsonRowObj.put("State", state);
				jsonRowObj.put("LineRate", demandMappingObj.getLineRate());
				jsonRowObj.put("PathType", demandMappingObj.getPathType());
											
				jsonArrayView2Obj.add(jsonRowObj);			
			}
		}		
				
		
		
		/*jsonView2Obj.put("view2", jsonArrayView2Obj);
		
		MainMap.logger.info("Json View_2 Object JSON String: "+ jsonView2Obj.toJSONString());*/
		
		/**Final JSON Object Creation*/
		JSONObject jsonFinalObj = new JSONObject();
		
		/*JSONArray  jsonFinalArrayObj = new JSONArray();
		
		jsonFinalArrayObj.add(jsonView1Obj);
		jsonFinalArrayObj.add(jsonView2Obj);*/
		
		jsonFinalObj.put("view1", jsonArrayView1Obj);
		jsonFinalObj.put("view2", jsonArrayView2Obj);
		
		
		MainMap.logger.info("Final JSON Object String :- "+ jsonFinalObj.toJSONString());
		
		MainMap.logger.info("--------------------------Demand Matrix Generation End---------------------------------------");
		
		
		return jsonFinalObj;
	}


}
