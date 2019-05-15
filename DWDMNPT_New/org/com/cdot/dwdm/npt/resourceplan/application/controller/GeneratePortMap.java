package application.controller;

import java.sql.SQLException;

import org.springframework.stereotype.Service;

import application.MainMap;
import application.service.DbService;

/**
 * 
 * @author sunaina
 * @brief generates the map of wavelengths on the links for the entire network
 *
 */
@Service
public class GeneratePortMap {
	
	DbService dbService; 
	ResourcePlanUtils rpu; 
	
	public GeneratePortMap() {
		super();				
		 MainMap.logger = MainMap.logger.getLogger(GeneratePortMap.class.getName());		
	}
	
	public GeneratePortMap(DbService dbService) {
		super();
		this.dbService = dbService;			
		MainMap.logger = MainMap.logger.getLogger(GeneratePortMap.class.getName());
	}

	public DbService getDbService() {
		return dbService;
	}

	public void setDbService(DbService dbService) {
		this.dbService = dbService;
	}
	
	public void fgenerateYcablePortMap(int networkid) throws SQLException
	{
//		 List <Integer> nodelist=rpu.fgetNodesToConfigure(networkid);		 
//		 for (int k = 0; k < nodelist.size(); k++) 
//		 {	
//			 int nodeid =nodelist.get(k);
//			 String NodeKey = networkid+"_"+nodeid;
//			 
//			 List<Circuit> Ycirs = dbService.getCircuitService().FindCircuitsWithYCableProt(networkid, nodeid, MapConstants.YCableProtection);
//				for (int i = 0; i < Ycirs.size(); i++) 
//				{
//					int cirId = Ycirs.get(i).getCircuitId();
//					
//					List<PortInfo> ports = dbService.getPortInfoService().FindPortInfo(networkid, nodeid, cirId);
//					for (int j = 0; j < ports.size(); j++) 
//					{
//						int r = ports.get(j).getRack();
//						int s = ports.get(j).getSbrack();
//						int c = ports.get(j).getCard();
//						
//						CardPortMap cpm = new CardPortMap();
//						
//						//mpn location
//						cpm.setEnd2Location(rpu.locationStr(r,s,c));
//						cpm.setEnd2CardType(end2CardType);
//					}
//					
//					
//				}
//		 }
		
	}
		

}
