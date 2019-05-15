package application.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import application.MainMap;
import application.constants.ResourcePlanConstants;
import application.model.Node;
import application.model.PatchCord;
import application.service.DbService;

@Service
public class CablingMap {
	DbService dbService; 	
	public static Logger logger = Logger.getLogger(CablingMap.class.getName());
	
	public CablingMap(DbService dbService) {	
		super();				
		this.dbService = dbService;		
	}


	public DbService getDbService() {
		return dbService;
	}


	public void setDbService(DbService dbService) {
		this.dbService = dbService;
	}
	
	public List<PatchCord> fgenerateCablingMap(int networkid)
	{
		List<Node> nodes = dbService.getNodeService().FindAll(networkid);
		List<PatchCord> cordsAll = new ArrayList<PatchCord>();
		
		for (int i = 0; i < nodes.size(); i++) {
			List <PatchCord> cords = dbService.getPatchCordService().FindAll(nodes.get(i).getNetworkId(), nodes.get(i).getNodeId());
			cordsAll.addAll(cords);			
		}		
		logger.info("CablingMap.fgenerateCablingMap() "+cordsAll.toString());
		return cordsAll;
		
	}

}
