package application.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

import application.constants.MapConstants;
import application.constants.ResourcePlanConstants;
import application.constants.SMConstants;
import application.model.CardInfo;
import application.model.Demand;
import application.model.EquipmentPreference;
import application.model.McsMap;
import application.model.MuxDemuxPortInfo;
import application.model.PortInfo;
import application.model.WssMap;
import application.service.DbService;
import application.service.McsMapService;

public class GenerateMcsMapping {
	DbService dbService; 
	ResourcePlanUtils rpu;
	public static Logger logger = Logger.getLogger(GenerateMcsMapping.class.getName());
	public DbService getDbService() {
		return dbService;
	}
	public void setDbService(DbService dbService) {
		this.dbService = dbService;
	}
	public GenerateMcsMapping(DbService dbService, ResourcePlanUtils rpu) {
		super();
		this.dbService = dbService;
		this.rpu = rpu;
//		MainMap.logger = MainMap.logger.getLogger(GenerateMcsMapping.class.getName());
	} 
	
	public void fgenerateMcsWssMuxDemuxMap(int networkid) throws SQLException
	{
		
		List <Integer> nodelist=rpu.fgetNodesToConfigure(networkid);		 
		 for (int k = 0; k < nodelist.size(); k++) 
		 {			
			 int nodeid =nodelist.get(k);
			 int nodetype=dbService.getNodeService().FindNode(networkid, nodeid).getNodeType();
			 int nodedegree=dbService.getNodeService().FindNode(networkid, nodeid).getDegree();
			 
			 switch (nodetype) {
			 case MapConstants.cdRoadm:
			 {
				 EquipmentPreference WssAddDropSetType = dbService.getEquipmentPreferenceService().FindPreferredEq(networkid, nodeid, ResourcePlanConstants.CatAddDropWssCdRoadm);
					if(WssAddDropSetType.getCardType().toString().equals(ResourcePlanConstants.AddDropSingleCardSet)) {
						fAssignWssPortMappingSingleCardSet(networkid,nodeid,nodedegree);
						fUpdateWssMapForEdfa(networkid,nodeid,nodedegree);	
					}else {
						fAssignWssPortMappingDoubleCardSet(networkid,nodeid,nodedegree);
						fUpdateWssMapDoubleCardSetForEdfa(networkid,nodeid,nodedegree);
					}
//				 fAssignWssPortMapping(networkid,nodeid,nodedegree);
//				 fUpdateWssMapForEdfa(networkid,nodeid,nodedegree);	
			 }
			 break;
			 case MapConstants.roadm:
			 {
				 fAssignMcsPortMapping(networkid,nodeid,nodedegree);
				 fUpdateMcsMapForEdfa(networkid,nodeid,nodedegree);	
			 }
			 break;
			 case MapConstants.twoBselectRoadm:
			 {
				 fInitializeMuxDemuxPorts(networkid,nodeid,nodedegree);
			 }
			 break;

			 default:
				 break;
			 }	 
			 
		 }	
		 
		 	
	}
	
	public void fgenerateMcsWssMapBrField(int networkid) throws SQLException
	{
		
		int networkidBF = Integer.parseInt(dbService.getNetworkService().GetBrownFieldNetworkId(networkid).toString());
		List <Integer> nodelist=rpu.fgetNodesToConfigure(networkidBF);
		 for (int k = 0; k < nodelist.size(); k++) 
		 {			
			 int nodeid =nodelist.get(k);
			 int nodetype=dbService.getNodeService().FindNode(networkidBF, nodeid).getNodeType();
			 int nodedegree=dbService.getNodeService().FindNode(networkidBF, nodeid).getDegree();
			 
			 switch (nodetype) {
			 case MapConstants.cdRoadm:
			 {
				 EquipmentPreference WssAddDropSetType = dbService.getEquipmentPreferenceService().FindPreferredEq(networkidBF, nodeid, ResourcePlanConstants.CatAddDropWssCdRoadm);
					if(WssAddDropSetType.getCardType().toString().equals(ResourcePlanConstants.AddDropSingleCardSet)) {
						fAssignWssPortMappingSingleCardBrField(networkid,networkidBF,nodeid,nodedegree);
						fUpdateWssMapForEdfa(networkidBF,nodeid,nodedegree);	
						fRemoveUnusedWssCards(networkidBF,nodeid,ResourcePlanConstants.ONE);
					}else {
						fAssignWssPortMappingDoubleCardBrField(networkid,networkidBF,nodeid,nodedegree); 
						fUpdateWssMapDoubleCardSetForEdfa(networkidBF,nodeid,nodedegree);	
						fRemoveUnusedWssCards(networkidBF,nodeid,ResourcePlanConstants.TWO);
					}
//				 fAssignWssPortMappingBrField(networkid,networkidBF,nodeid,nodedegree);				 
			 }
				 break;
			 case MapConstants.roadm:
			 {
				 fgenerateMcsMapBrField(networkid,networkidBF,nodeid,nodedegree);
				 fUpdateMcsMapForEdfa(networkidBF,nodeid,nodedegree);	
			 }
				 break;

			 default:
				 break;
			 }	 
			 
		 }	
	}
	
	public void fInitializeMuxDemuxPorts(int networkid,int nodeid,int nodedegree) throws SQLException
	{
		 MuxDemuxPortInfo port = new MuxDemuxPortInfo();
		 port.setNetworkId(networkid);
		 port.setNodeId(nodeid);
		 
		 //East
		 // 	R	S	C
		 //OE (1	0	1) [1,3....79] M-D [1,3....79] Odd East Tray
		 //EE (1	0	2) [2,4,...80] M-D [2,4,...80] Even East Tray
		 		 
		 //West
		 //		R	S	C
		 //OW (1 	1	1)/(1	2	1)/(2	0	1) [1,3....79] M-D [1,3....79] Odd West Tray
		 //EW (1 	1	2)/(1	2	2)/(2	0	2) [2,4,...80] M-D [2,4,...80] Even West Tray
		 
		 List <CardInfo> eastOddTray = dbService.getCardInfoService().FindCards(networkid, nodeid, ResourcePlanConstants.Odd_Mux_Demux_Unit,"East");
		 List <CardInfo> eastEvenTray = dbService.getCardInfoService().FindCards(networkid, nodeid, ResourcePlanConstants.Even_Mux_Demux_Unit,"East");
		 List <CardInfo> westOddTray = dbService.getCardInfoService().FindCards(networkid, nodeid, ResourcePlanConstants.Odd_Mux_Demux_Unit,"West");
		 List <CardInfo> westEvenTray = dbService.getCardInfoService().FindCards(networkid, nodeid, ResourcePlanConstants.Even_Mux_Demux_Unit,"West");
		 List<PortInfo> linePorts=dbService.getPortInfoService().FindAllUniqueLinePorts(networkid, nodeid);
		 
		 
		 for(PortInfo linePort: linePorts){ //Iterate over each line port
			 //Find the corresponding MPN (wavelength)
			 CardInfo mpn = dbService.getMuxDemuxPortInfoService().GetMpnCardForLinePort(networkid,nodeid,
					 		linePort.getRack(),linePort.getSbrack(),linePort.getCard());
			 //Fill Port Details
			 if(linePort.getDirection().equalsIgnoreCase(MapConstants.EAST)) {
				 if(mpn.getWavelength() %2 == 0) {
					 port.setRack(eastEvenTray.get(0).getRack());
					 port.setSbrack(eastEvenTray.get(0).getSbrack());
					 port.setCardId(eastEvenTray.get(0).getCard());
					 port.setWavelength(mpn.getWavelength());
					 port.setPortNum(mpn.getWavelength()/2);
				 }
				 else{
					 port.setRack(eastOddTray.get(0).getRack());
					 port.setSbrack(eastOddTray.get(0).getSbrack());
					 port.setCardId(eastOddTray.get(0).getCard());
					 port.setWavelength(mpn.getWavelength());
					 port.setPortNum((mpn.getWavelength()+1)/2);
				 }
			 }
			 else if(linePort.getDirection().equalsIgnoreCase(MapConstants.WEST)) {
				 if(mpn.getWavelength() %2 == 0) {
					 port.setRack(westEvenTray.get(0).getRack());
					 port.setSbrack(westEvenTray.get(0).getSbrack());
					 port.setCardId(westEvenTray.get(0).getCard());
					 port.setWavelength(mpn.getWavelength());
					 port.setPortNum(mpn.getWavelength()/2);
				 }
				 else{
					 port.setRack(westOddTray.get(0).getRack());
					 port.setSbrack(westOddTray.get(0).getSbrack());
					 port.setCardId(westOddTray.get(0).getCard());
					 port.setWavelength(mpn.getWavelength());
					 port.setPortNum((mpn.getWavelength()+1)/2);
				 }
			 }
			 dbService.getMuxDemuxPortInfoService().Insert(port);
		 }
	}
	
	public void fAssignMcsPortMapping(int networkid,int nodeid,int nodedegree) throws SQLException
	{
		 int switchprotid=0;
		 String NodeKey = networkid+"_"+nodeid;			 
		 List <CardInfo> mcs = dbService.getCardInfoService().FindCardInfoByCardType(networkid, nodeid, ResourcePlanConstants.CardMcs);
		 //String secDir="",path="";
		 
		 int j=0;
		 List<PortInfo> linePorts=dbService.getPortInfoService().FindAllUniqueLinePorts(networkid, nodeid);
 		 for (int i = 0; i < mcs.size(); i++) 
		 {		 
			 McsMap map = new McsMap();
			 map.setNetworkId(networkid);
			 map.setNodeId(nodeid);
			 map.setRack(mcs.get(i).getRack());
			 map.setSbrack(mcs.get(i).getSbrack());
			 map.setCard(mcs.get(i).getCard());
			 map.setMcsId(i+1);		 
			 
			 Object id =dbService.getMcsMapService().GetMaxSwitchPortId(networkid,nodeid,i+1);
			 if(id!=null)
				 switchprotid = Integer.parseInt(dbService.getMcsMapService().GetMaxSwitchPortId(networkid,nodeid,i+1).toString());
			 else
				 switchprotid=1;			 
			 
			 while(j<linePorts.size()){
				 map.setMcsSwitchPort(switchprotid++);
				 String mpnloc = rpu.locationStr(linePorts.get(j).getRack(),linePorts.get(j).getSbrack(),linePorts.get(j).getCard());
				 map.setTpnLoc(mpnloc);
				 map.setTpnLinePortNo(linePorts.get(j).getLinePort());		
				 map.setMcsCommonPort(linePorts.get(j).getDirection());
				 dbService.getMcsMapService().Insert(map);			
				 j++;
				 
				 if(j%ResourcePlanConstants.MaxSwitchPortsMcs==0)
					 break;
			 }
			 
			 
//			 List <CardInfo> mpneast = dbService.getCardInfoService().FindMpnsByDir(networkid,nodeid,MapConstants.EAST); 			 
//			 for (int j = 0; j < mpneast.size(); j++) 
//			 {					 
//				 map.setMcsSwitchPort(switchprotid+j);
//				 String mpnloc = rpu.locationStr(mpneast.get(j).getRack(),mpneast.get(j).getSbrack(),mpneast.get(j).getCard());
//				 map.setTpnLoc(mpnloc);
//				 map.setTpnLinePortNo(ResourcePlanConstants.primaryLinePort);		
//				 map.setMcsCommonPort(secDir);
//				 map.setMcsCommonPort(MapConstants.EAST);
//				 dbService.getMcsMapService().Insert(map);
//				 
//				 int demandid=mpneast.get(j).getDemandId();
//
//				 if(mpneast.get(j).getCardType().equals(ResourcePlanConstants.CardMuxponderOPXCGX) | mpneast.get(j).getCardType().equals(ResourcePlanConstants.CardMuxponderOPX)) {						
//					 map.setMcsSwitchPort(++switchprotid+j);
//					 fAssignSecondaryLine(networkid, nodeid, demandid, map,mpneast.get(j).getRack(),mpneast.get(j).getSbrack(),mpneast.get(j).getCard());
//				 }
//			 }
//			 
//			 List <CardInfo> mpnwest = dbService.getCardInfoService().FindMpnsByDir(networkid,nodeid,MapConstants.WEST);
//			 map.setMcsCommonPort(MapConstants.WEST);
//			 id =dbService.getMcsMapService().GetMaxSwitchPortId(networkid,nodeid,i+1);
//			 if(id!=null)
//				 switchprotid = Integer.parseInt(dbService.getMcsMapService().GetMaxSwitchPortId(networkid,nodeid,i+1).toString());
//			 else
//				 switchprotid=1;
//			 for (int j = 0; j < mpnwest.size(); j++) 
//			 {							 
//				 map.setMcsSwitchPort(switchprotid+j);
//				 String mpnloc = rpu.locationStr(mpnwest.get(j).getRack(),mpnwest.get(j).getSbrack(),mpnwest.get(j).getCard());
//				 map.setTpnLoc(mpnloc);
//				 map.setTpnLinePortNo(ResourcePlanConstants.primaryLinePort);		
//				 map.setMcsCommonPort(MapConstants.WEST);
//				 dbService.getMcsMapService().Insert(map);
//				 
//				 int demandid=mpnwest.get(j).getDemandId();
//
//				 if(mpnwest.get(j).getCardType().equals(ResourcePlanConstants.CardMuxponderOPXCGX) | mpnwest.get(j).getCardType().equals(ResourcePlanConstants.CardMuxponderOPX)) {						
//					 map.setMcsSwitchPort(++switchprotid+j);
//					 fAssignSecondaryLine(networkid, nodeid, demandid, map,mpnwest.get(j).getRack(),mpnwest.get(j).getSbrack(),mpnwest.get(j).getCard());
//				 }
//			 }
//			 
//			 List <CardInfo> mpnnorth = dbService.getCardInfoService().FindMpnsByDir(networkid,nodeid,MapConstants.NORTH);
//			 map.setMcsCommonPort(MapConstants.NORTH);
//			 id =dbService.getMcsMapService().GetMaxSwitchPortId(networkid,nodeid,i+1);
//			 if(id!=null)
//				 switchprotid = Integer.parseInt(dbService.getMcsMapService().GetMaxSwitchPortId(networkid,nodeid,i+1).toString());
//			 else
//				 switchprotid=1;
//			 for (int j = 0; j < mpnnorth.size(); j++) 
//			 {							 
//				 map.setMcsSwitchPort(switchprotid+j);
//				 String mpnloc = rpu.locationStr(mpnnorth.get(j).getRack(),mpnnorth.get(j).getSbrack(),mpnnorth.get(j).getCard());
//				 map.setTpnLoc(mpnloc);
//				 map.setTpnLinePortNo(ResourcePlanConstants.primaryLinePort);		
//				 map.setMcsCommonPort(MapConstants.NORTH);
//				 dbService.getMcsMapService().Insert(map);
//				 
//				 int demandid=mpnnorth.get(j).getDemandId();
//				 
//				 if(mpnnorth.get(j).getCardType().equals(ResourcePlanConstants.CardMuxponderOPXCGX) | mpnnorth.get(j).getCardType().equals(ResourcePlanConstants.CardMuxponderOPX)) {						
//					 map.setMcsSwitchPort(++switchprotid+j);
//					 fAssignSecondaryLine(networkid, nodeid, demandid, map,mpnnorth.get(j).getRack(),mpnnorth.get(j).getSbrack(),mpnnorth.get(j).getCard());
//				 }
//			 }
//			 
//			 List <CardInfo> mpnsouth = dbService.getCardInfoService().FindMpnsByDir(networkid,nodeid,MapConstants.SOUTH);
//			 map.setMcsCommonPort(MapConstants.SOUTH);
//			 id =dbService.getMcsMapService().GetMaxSwitchPortId(networkid,nodeid,i+1);
//			 if(id!=null)
//				 switchprotid = Integer.parseInt(dbService.getMcsMapService().GetMaxSwitchPortId(networkid,nodeid,i+1).toString());
//			 else
//				 switchprotid=1;
//			 for (int j = 0; j < mpnsouth.size(); j++) 
//			 {							 
//				 map.setMcsSwitchPort(switchprotid+j);
//				 String mpnloc = rpu.locationStr(mpnsouth.get(j).getRack(),mpnsouth.get(j).getSbrack(),mpnsouth.get(j).getCard());
//				 map.setTpnLoc(mpnloc);
//				 map.setTpnLinePortNo(ResourcePlanConstants.primaryLinePort);		
//				 map.setMcsCommonPort(MapConstants.SOUTH);
//				 dbService.getMcsMapService().Insert(map);
//				 
//				 int demandid=mpnsouth.get(j).getDemandId();
//
//				 if(mpnsouth.get(j).getCardType().equals(ResourcePlanConstants.CardMuxponderOPXCGX) | mpnsouth.get(j).getCardType().equals(ResourcePlanConstants.CardMuxponderOPX)) {						
//					 map.setMcsSwitchPort(++switchprotid+j);
//					 fAssignSecondaryLine(networkid, nodeid, demandid, map,mpnsouth.get(j).getRack(),mpnsouth.get(j).getSbrack(),mpnsouth.get(j).getCard());
//				 }
//			 }
//			 
//			 List <CardInfo> mpnne = dbService.getCardInfoService().FindMpnsByDir(networkid,nodeid,MapConstants.NE);
//			 map.setMcsCommonPort(MapConstants.NE);
//			 id =dbService.getMcsMapService().GetMaxSwitchPortId(networkid,nodeid,i+1);
//			 if(id!=null)
//				 switchprotid = Integer.parseInt(dbService.getMcsMapService().GetMaxSwitchPortId(networkid,nodeid,i+1).toString());
//			 else
//				 switchprotid=1;
//			 for (int j = 0; j < mpnne.size(); j++) 
//			 {							 
//				 map.setMcsSwitchPort(switchprotid+j);
//				 String mpnloc = rpu.locationStr(mpnne.get(j).getRack(),mpnne.get(j).getSbrack(),mpnne.get(j).getCard());
//				 map.setTpnLoc(mpnloc);
//				 map.setTpnLinePortNo(ResourcePlanConstants.primaryLinePort);		
//				 map.setMcsCommonPort(MapConstants.NE);
//				 dbService.getMcsMapService().Insert(map);
//				 
//				 int demandid=mpnne.get(j).getDemandId();
//
//				 if(mpnne.get(j).getCardType().equals(ResourcePlanConstants.CardMuxponderOPXCGX) | mpnne.get(j).getCardType().equals(ResourcePlanConstants.CardMuxponderOPX)) {						
//					 map.setMcsSwitchPort(++switchprotid+j);
//					 fAssignSecondaryLine(networkid, nodeid, demandid, map,mpnne.get(j).getRack(),mpnne.get(j).getSbrack(),mpnne.get(j).getCard());
//				 }
//			 }
//			 
//			 List <CardInfo> mpnnw = dbService.getCardInfoService().FindMpnsByDir(networkid,nodeid,MapConstants.NW);
//			 map.setMcsCommonPort(MapConstants.NW);
//			 id =dbService.getMcsMapService().GetMaxSwitchPortId(networkid,nodeid,i+1);
//			 if(id!=null)
//				 switchprotid = Integer.parseInt(dbService.getMcsMapService().GetMaxSwitchPortId(networkid,nodeid,i+1).toString());
//			 else
//				 switchprotid=1;
//			 for (int j = 0; j < mpnnw.size(); j++) 
//			 {							 
//				 map.setMcsSwitchPort(switchprotid+j);
//				 String mpnloc = rpu.locationStr(mpnnw.get(j).getRack(),mpnnw.get(j).getSbrack(),mpnnw.get(j).getCard());
//				 map.setTpnLoc(mpnloc);
//				 map.setTpnLinePortNo(ResourcePlanConstants.primaryLinePort);		
//				 map.setMcsCommonPort(MapConstants.NW);
//				 dbService.getMcsMapService().Insert(map);
//				 
//				 int demandid=mpnnw.get(j).getDemandId();
//
//				 if(mpnnw.get(j).getCardType().equals(ResourcePlanConstants.CardMuxponderOPXCGX) | mpnnw.get(j).getCardType().equals(ResourcePlanConstants.CardMuxponderOPX)) {						
//					 map.setMcsSwitchPort(++switchprotid+j);
//					 fAssignSecondaryLine(networkid, nodeid, demandid, map,mpnnw.get(j).getRack(),mpnnw.get(j).getSbrack(),mpnnw.get(j).getCard());
//				 }
//			 }
//			 
//			 List <CardInfo> mpnse = dbService.getCardInfoService().FindMpnsByDir(networkid,nodeid,MapConstants.SE);
//			 map.setMcsCommonPort(MapConstants.SE);
//			 id =dbService.getMcsMapService().GetMaxSwitchPortId(networkid,nodeid,i+1);
//			 if(id!=null)
//				 switchprotid = Integer.parseInt(dbService.getMcsMapService().GetMaxSwitchPortId(networkid,nodeid,i+1).toString());
//			 else
//				 switchprotid=1;
//			 for (int j = 0; j < mpnse.size(); j++) 
//			 {							 
//				 map.setMcsSwitchPort(switchprotid+j);
//				 String mpnloc = rpu.locationStr(mpnse.get(j).getRack(),mpnse.get(j).getSbrack(),mpnse.get(j).getCard());
//				 map.setTpnLoc(mpnloc);
//				 map.setTpnLinePortNo(ResourcePlanConstants.primaryLinePort);		
//				 map.setMcsCommonPort(MapConstants.SE);
//				 dbService.getMcsMapService().Insert(map);
//				 
//				 int demandid=mpnse.get(j).getDemandId();
//
//				 if(mpnse.get(j).getCardType().equals(ResourcePlanConstants.CardMuxponderOPXCGX) | mpnse.get(j).getCardType().equals(ResourcePlanConstants.CardMuxponderOPX)) {
//					 map.setMcsSwitchPort(++switchprotid+j);
//					 fAssignSecondaryLine(networkid, nodeid, demandid, map,mpnse.get(j).getRack(),mpnse.get(j).getSbrack(),mpnse.get(j).getCard());
//				 }
//			 }
//			 
//			 List <CardInfo> mpnsw = dbService.getCardInfoService().FindMpnsByDir(networkid,nodeid,MapConstants.SW);
//			 map.setMcsCommonPort(MapConstants.SW);
//			 id =dbService.getMcsMapService().GetMaxSwitchPortId(networkid,nodeid,i+1);
//			 if(id!=null)
//				 switchprotid = Integer.parseInt(dbService.getMcsMapService().GetMaxSwitchPortId(networkid,nodeid,i+1).toString());
//			 else
//				 switchprotid=1;
//			 for (int j = 0; j < mpnsw.size(); j++) 
//			 {							 
//				 map.setMcsSwitchPort(switchprotid+j);
//				 String mpnloc = rpu.locationStr(mpnsw.get(j).getRack(),mpnsw.get(j).getSbrack(),mpnsw.get(j).getCard());
//				 map.setTpnLoc(mpnloc);
//				 map.setTpnLinePortNo(ResourcePlanConstants.primaryLinePort);		
//				 map.setMcsCommonPort(MapConstants.SW);
//				 dbService.getMcsMapService().Insert(map);
//				 
//				 int demandid=mpnsw.get(j).getDemandId();
//
//				 if(mpnsw.get(j).getCardType().equals(ResourcePlanConstants.CardMuxponderOPXCGX) | mpnsw.get(j).getCardType().equals(ResourcePlanConstants.CardMuxponderOPX)) {
//					 map.setMcsSwitchPort(++switchprotid+j);
//					 fAssignSecondaryLine(networkid, nodeid, demandid, map,mpnsw.get(j).getRack(),mpnsw.get(j).getSbrack(),mpnsw.get(j).getCard());
//				 }
//			 }
			
		 }	
	}
	
//	public void fAssignSecondaryLine(int networkid,int nodeid,int demandid,McsMap map,int rack,int sbrack,int cardid) throws SQLException {
//		
//		String secDir=dbService.getPortInfoService().FindLineDirectionInfo(networkid, nodeid, rack, sbrack, cardid, ResourcePlanConstants.secondaryLinePort).getDirection();
//
//		System.out.println("fAssignSecondaryLine ---- Secondary Direction ::"+secDir);
//		
//		map.setTpnLinePortNo(ResourcePlanConstants.secondaryLinePort);
//		map.setMcsCommonPort(secDir);
//		dbService.getMcsMapService().Insert(map);
//
//	}
	
	public void fAssignWssPortMappingDoubleCardSet(int networkid,int nodeid,int nodedegree) throws SQLException
	{
		 int wssPortId=0;
		 String NodeKey = networkid+"_"+nodeid;			 
		 List <CardInfo> wssCardsLevel2 = dbService.getCardInfoService().FindWssLevelTwoCards(networkid, nodeid);
		 List <CardInfo> wssCardsLevel1 = dbService.getCardInfoService().FindWssLevelOneCards(networkid, nodeid);
		 int j=0; // Counter for mpns
		 int wssMaxPorts;
		 
		 logger.info("fAssignWssPortMapping : nodeid:"+nodeid);
		 
		 // Channel Protection cards : OPX or OLP
		 List <PortInfo> mpns = dbService.getPortInfoService().FindAllChnlPtcLines(networkid, nodeid);
		 System.out.println("Channel ptc Cards Size"+mpns.size());
		 for (int i = 0; i < wssCardsLevel2.size(); i++) 
		 {			 
			 String wssCardType=wssCardsLevel2.get(i).getCardType();
			// On basis of card type get max ports
			 wssMaxPorts=rpu.fGetMaxSwitchPortsforCardType(wssCardType);	
			 logger.info("Max Wss Ports in port mapping :"+wssMaxPorts+" WssCardTye:"+wssCardType);
			 
			 WssMap wssMap = new WssMap();
			 wssMap.setNetworkId(networkid);
			 wssMap.setNodeId(nodeid);
			 wssMap.setRack(wssCardsLevel2.get(i).getRack());
			 wssMap.setSbrack(wssCardsLevel2.get(i).getSbrack());
			 wssMap.setCard(wssCardsLevel2.get(i).getCard());
			 wssMap.setCardType(wssCardsLevel2.get(i).getCardType());
			 wssMap.setCardSubtype(0);
			 wssMap.setWssLevel1Loc(wssCardsLevel1.get(i).getRack()+"_"+wssCardsLevel1.get(i).getSbrack()+"_"+wssCardsLevel1.get(i).getCard());
			 wssMap.setWssSetNo(i+1);
			 
			 logger.info("Number of MPNs for Node :"+nodeid+" are -> "+mpns.size());
	
			 wssMap.setWssLevel2CommonPort(""+1);//Debug
			 wssMap.setWssLevel1SwitchPort(""+1);//Debug
			 
			 // Get max port id
			 Object id =dbService.getWssMapService().GetMaxSwitchPortId(networkid,nodeid,i+1);
			 if(id!=null)
				 wssPortId = Integer.parseInt(dbService.getWssMapService().GetMaxSwitchPortId(networkid,nodeid,i+1).toString());
			 else
				 wssPortId=1;
			
			 //Loop through all mpns for this node and assign maxports with mcsid=i+1
			 // var k is used for remembering the last mpn assigned in previous set of wss
			 
			 while(j<mpns.size()){
				 wssMap.setWssLevel1CommonPort(mpns.get(j).getDirection());

				 String mpnloc = rpu.locationStr(mpns.get(j).getRack(),mpns.get(j).getSbrack(),mpns.get(j).getCard());
				 wssMap.setTpnLoc(mpnloc);
				 wssMap.setTpnLinePortNo(mpns.get(j).getLinePort());	

				 // use Set 2 level 2 wss card for secondary line in case of channel protection
				 if(mpns.get(j).getLinePort()==ResourcePlanConstants.SECONDARY_LINE_PORT){
					 String level1Loc=wssCardsLevel1.get(i+1).getRack()+"_"+wssCardsLevel1.get(i+1).getSbrack()+"_"+wssCardsLevel1.get(i+1).getCard();
					 int wssPortIdSec;
					 Object SecondSetMaxPortId =dbService.getWssMapService().GetMaxSwitchPortId(networkid,nodeid,i+2);
					 if(SecondSetMaxPortId!=null)
						 wssPortIdSec = Integer.parseInt(dbService.getWssMapService().GetMaxSwitchPortId(networkid,nodeid,i+2).toString());
					 else
						 wssPortIdSec=1;

					 WssMap secLineMap=new WssMap(networkid,nodeid,wssCardsLevel2.get(i+1).getRack(),wssCardsLevel2.get(i+1).getSbrack(),wssCardsLevel2.get(i+1).getCard(),
							 wssCardsLevel2.get(i+1).getCardType(),0,i+2,wssPortIdSec,""+1,level1Loc,""+1,mpns.get(j).getDirection(),mpnloc,mpns.get(j).getLinePort(),"",0);

					 dbService.getWssMapService().Insert(secLineMap);


				 }
				 else
				 {
					 wssMap.setWssLevel2SwitchPort(wssPortId++);
					 dbService.getWssMapService().Insert(wssMap);
				 }

				 j++;

				 if(j%wssMaxPorts==0)
					 break;
			 }
			
		 }	
		 
		 j=0;
		 // Client Protection mpn port mapping 
		 //List <CardInfo> mpns = dbService.getCardInfoService().findMpns(networkid,nodeid);
		 mpns = dbService.getPortInfoService().FindAllClientPtcLines(networkid, nodeid);
		 List<PortInfo> unptcMpnLines= dbService.getPortInfoService().FindAllClientUnPtcLines(networkid, nodeid);
		 mpns.addAll(unptcMpnLines);
		 System.out.println("Client ptc Cards Size"+mpns.size());
		 for (int i = 0; i < wssCardsLevel2.size(); i++) 
		 {			 
			 String wssCardType=wssCardsLevel2.get(i).getCardType();
			 
			 // On basis of card type get max ports
			 if(wssCardType.equalsIgnoreCase(ResourcePlanConstants.CardWss1x9))
				 wssMaxPorts=ResourcePlanConstants.MaxSwitchPortsWss2x1x9;
			 else wssMaxPorts=ResourcePlanConstants.MaxSwitchPortsWss2x1x20;		
			 logger.info("Max Wss Ports in port mapping :"+wssMaxPorts+" WssCardTye:"+wssCardType);
			 
			 WssMap wssMap = new WssMap();
			 wssMap.setNetworkId(networkid);
			 wssMap.setNodeId(nodeid);
			 wssMap.setRack(wssCardsLevel2.get(i).getRack());
			 wssMap.setSbrack(wssCardsLevel2.get(i).getSbrack());
			 wssMap.setCard(wssCardsLevel2.get(i).getCard());
			 wssMap.setCardType(wssCardsLevel2.get(i).getCardType());
			 wssMap.setCardSubtype(0);
			 wssMap.setWssLevel1Loc(wssCardsLevel1.get(i).getRack()+"_"+wssCardsLevel1.get(i).getSbrack()+"_"+wssCardsLevel1.get(i).getCard());
			 wssMap.setWssSetNo(i+1);
			 
			 logger.info("Number of MPNs for Node :"+nodeid+" are -> "+mpns.size());
	
			 wssMap.setWssLevel2CommonPort(""+1);//Debug
			 wssMap.setWssLevel1SwitchPort(""+1);//Debug
			 
			 // Get max port id
			 Object id =dbService.getWssMapService().GetMaxSwitchPortId(networkid,nodeid,i+1);
	 		 if(id!=null)
			 wssPortId = Integer.parseInt(dbService.getWssMapService().GetMaxSwitchPortId(networkid,nodeid,i+1).toString());
	 		 else
		 	 wssPortId=1;	
					
			//Loop through all mpns for this node and assign maxports with mcsid=i+1
			 while(j<mpns.size() && wssPortId<=wssMaxPorts){
				 wssMap.setWssLevel1CommonPort(mpns.get(j).getDirection());
				 wssMap.setWssLevel2SwitchPort(wssPortId++);
				 String mpnloc = rpu.locationStr(mpns.get(j).getRack(),mpns.get(j).getSbrack(),mpns.get(j).getCard());
				 wssMap.setTpnLoc(mpnloc);
				 wssMap.setTpnLinePortNo(mpns.get(j).getLinePort());	
				 
				 dbService.getWssMapService().Insert(wssMap);
				 
				 j++;
				 
//				 if(j%wssMaxPorts==0)
//					 break;
			 }
		 }	
	}	
	
	public void fAssignWssPortMappingSingleCardSet(int networkid,int nodeid,int nodedegree) throws SQLException
	{
		 int wssPortId=0;
		 String NodeKey = networkid+"_"+nodeid;			 
		 List <CardInfo> wssAddDropCards = dbService.getCardInfoService().FindWssLevelOneCards(networkid, nodeid);
		 int j=0; // Counter for mpns
		 
		 Object id;

		 /* Get card redundancy */		
		 EquipmentPreferences eqp=new EquipmentPreferences(dbService);
		 String WssRedundancy = eqp.fgetRedundancyReq(networkid, nodeid, ResourcePlanConstants.CatAddDropWssCdRoadm);

		 String wssCardType=ResourcePlanConstants.CardWss8x12;
		 int wssMaxPorts=ResourcePlanConstants.MaxSwitchPortsWss2x8x12;
		 logger.info("Max Wss Ports in port mapping :"+wssMaxPorts+" WssCardTye:"+wssCardType);
		 
		 logger.info("fAssignWssPortMapping : nodeid:"+nodeid);
		 
		 /***************** Channel Protection Lines : OPX or OLP *****************/
		 List <PortInfo> channelPtcLines= dbService.getPortInfoService().FindAllChnlPtcLines(networkid, nodeid);
		 System.out.println("Number of MPNs for Node "+nodeid+" with Channel Ptc are -> "+channelPtcLines.size());
		 if(channelPtcLines.size()>0)
		 {
			 for (int i = 0; i < wssAddDropCards.size(); i++) 
			 {			
				 int mcsId=i+1;
				 /** McsMap is also used for wss card mapping with mpn and edfa **/
				 McsMap wssMap = new McsMap();
				 wssMap.setNetworkId(networkid);
				 wssMap.setNodeId(nodeid);
				 wssMap.setRack(wssAddDropCards.get(i).getRack());
				 wssMap.setSbrack(wssAddDropCards.get(i).getSbrack());
				 wssMap.setCard(wssAddDropCards.get(i).getCard());
				 wssMap.setMcsId(mcsId);
				 				 
				 // Get max port id
				 id =dbService.getMcsMapService().GetMaxSwitchPortId(networkid,nodeid,mcsId);
				 if(id!=null)
					 wssPortId = Integer.parseInt(dbService.getMcsMapService().GetMaxSwitchPortId(networkid,nodeid,mcsId).toString());
				 else
					 wssPortId=1;	
				
				 //Loop through all mpns for this node and assign maxports with mcsid=i+1
				 // var k is used for remembering the last mpn assigned in previous set of wss
				 
				 while(j<channelPtcLines.size() && j<=wssMaxPorts*2){
					 wssMap.setMcsCommonPort(channelPtcLines.get(j).getDirection());

					 String mpnloc = rpu.locationStr(channelPtcLines.get(j).getRack(),channelPtcLines.get(j).getSbrack(),channelPtcLines.get(j).getCard());
					 wssMap.setTpnLoc(mpnloc);
					 wssMap.setTpnLinePortNo(channelPtcLines.get(j).getLinePort());	

					 // use Set 2 level 2 wss card for secondary line in case of channel protection
					 if(channelPtcLines.get(j).getLinePort()==ResourcePlanConstants.SECONDARY_LINE_PORT){					 
						 int SecondSetMaxPortId;
						 id =dbService.getMcsMapService().GetMaxSwitchPortId(networkid,nodeid,mcsId+1);
						 if(id!=null)
							 SecondSetMaxPortId = Integer.parseInt(dbService.getMcsMapService().GetMaxSwitchPortId(networkid,nodeid,mcsId+1).toString());
						 else
							 SecondSetMaxPortId=1;
						 
						 McsMap secLineMap=new McsMap(networkid,nodeid,wssAddDropCards.get(i+1).getRack(),wssAddDropCards.get(i+1).getSbrack(),wssAddDropCards.get(i+1).getCard(),
								 mcsId+1,SecondSetMaxPortId,wssMap.getMcsCommonPort(),mpnloc,channelPtcLines.get(j).getLinePort(),"",0);

						 dbService.getMcsMapService().Insert(secLineMap);
					 }
					 else
					 {
						 wssMap.setMcsSwitchPort(wssPortId++);
						 dbService.getMcsMapService().Insert(wssMap);
					 }

					 j++;
//
//					 if(j%wssMaxPorts==0)
//						 break;
				 }
				
			 }	
		 }		 
		 
		 
		 /***************** Client Protection mpn port mapping *****************/ 
		 
		 //List <CardInfo> mpns = dbService.getCardInfoService().findMpns(networkid,nodeid);
		 List<PortInfo> protectedClientLines = dbService.getPortInfoService().FindAllClientPtcLines(networkid, nodeid);
		 List<PortInfo> unprotectedClientLines = dbService.getPortInfoService().FindAllClientUnPtcLines(networkid, nodeid);
		 System.out.println("Client ptc mpns lines Size -> "+protectedClientLines.size()+" Client unptc mpns lines Size -> "+unprotectedClientLines.size());
		 
		 j=0;
		 for (int i = 0; i < wssAddDropCards.size(); i++) 
		 {	
			 int mcsId=i+1;
			 /** McsMap is also used for wss card mapping with mpn and edfa **/
			 McsMap wssMap = new McsMap();
			 wssMap.setNetworkId(networkid);
			 wssMap.setNodeId(nodeid);
			 wssMap.setRack(wssAddDropCards.get(i).getRack());
			 wssMap.setSbrack(wssAddDropCards.get(i).getSbrack());
			 wssMap.setCard(wssAddDropCards.get(i).getCard());
			 wssMap.setMcsId(mcsId);
			 
			 logger.info("Number of Unprotected MPNs Lines for Node :"+nodeid+" are -> "+protectedClientLines.size());
			 
			/** Get max port id **/
			 id =dbService.getMcsMapService().GetMaxSwitchPortId(networkid,nodeid,mcsId);
			 if(id!=null)
				 wssPortId = Integer.parseInt(dbService.getMcsMapService().GetMaxSwitchPortId(networkid,nodeid,mcsId).toString());
			 else
				 wssPortId=1;	
			 
			 /** Subtract channel ptc ports already assigned from max ports **/
//			 wssMaxPorts=wssMaxPorts-wssPortId+1;
			
			//Loop through all mpns for this node and assign maxports with mcsid=i+1
			 while(j<protectedClientLines.size() && wssPortId<=wssMaxPorts){
				 
				 wssMap.setMcsCommonPort(protectedClientLines.get(j).getDirection());
				 wssMap.setMcsSwitchPort(wssPortId++);
				 String mpnloc = rpu.locationStr(protectedClientLines.get(j).getRack(),protectedClientLines.get(j).getSbrack(),protectedClientLines.get(j).getCard());
				 wssMap.setTpnLoc(mpnloc);
				 wssMap.setTpnLinePortNo(protectedClientLines.get(j).getLinePort());	
				 
				 dbService.getMcsMapService().Insert(wssMap); 
				 
				 if(WssRedundancy.equals(ResourcePlanConstants.Yes)) {
					 j++;
					 /** Get max port id for set 2**/
					 int tempwssPortId;
					 id =dbService.getMcsMapService().GetMaxSwitchPortId(networkid,nodeid,mcsId+1);
					 if(id!=null)
						 tempwssPortId = Integer.parseInt(dbService.getMcsMapService().GetMaxSwitchPortId(networkid,nodeid,mcsId+1).toString());
					 else
						 tempwssPortId=1;	
					 
					 mpnloc = rpu.locationStr(protectedClientLines.get(j).getRack(),protectedClientLines.get(j).getSbrack(),protectedClientLines.get(j).getCard());
					 
					 McsMap secLineMap=new McsMap(networkid,nodeid,wssAddDropCards.get(i+1).getRack(),wssAddDropCards.get(i+1).getSbrack(),wssAddDropCards.get(i+1).getCard(),
							 mcsId+1,tempwssPortId,protectedClientLines.get(j).getDirection(),mpnloc,protectedClientLines.get(j).getLinePort(),"",0);
					 
//					 wssMap.setMcsCommonPort(protectedClientLines.get(j).getDirection());					 
//					 wssMap.setMcsSwitchPort(tempwssPortId);
//					 
//					 wssMap.setTpnLoc(mpnloc);
//					 wssMap.setTpnLinePortNo(protectedClientLines.get(j).getLinePort());
					 
					 dbService.getMcsMapService().Insert(secLineMap); 
				 }
				 
				 j++;
				 
//				 if(j%wssMaxPorts==0)
//					 break;
			 }
		 }	
		 
		 j=0;
		 for (int i = 0; i < wssAddDropCards.size(); i++) 
		 {	
			 int mcsId=i+1;
			 /** McsMap is also used for wss card mapping with mpn and edfa **/
			 McsMap wssMap = new McsMap();
			 wssMap.setNetworkId(networkid);
			 wssMap.setNodeId(nodeid);
			 wssMap.setRack(wssAddDropCards.get(i).getRack());
			 wssMap.setSbrack(wssAddDropCards.get(i).getSbrack());
			 wssMap.setCard(wssAddDropCards.get(i).getCard());
			 wssMap.setMcsId(mcsId);
			 
			 logger.info("Number of Unprotected MPNs Lines for Node :"+nodeid+" are -> "+unprotectedClientLines.size());
			 
			/** Get max port id **/
			 id =dbService.getMcsMapService().GetMaxSwitchPortId(networkid,nodeid,mcsId);
			 if(id!=null)
				 wssPortId = Integer.parseInt(dbService.getMcsMapService().GetMaxSwitchPortId(networkid,nodeid,mcsId).toString());
			 else
				 wssPortId=1;
			
			//Loop through all mpns for this node and assign maxports with mcsid=i+1
			 while(j<unprotectedClientLines.size() && wssPortId<=wssMaxPorts){
				 
				 wssMap.setMcsCommonPort(unprotectedClientLines.get(j).getDirection());
				 wssMap.setMcsSwitchPort(wssPortId++);
				 String mpnloc = rpu.locationStr(unprotectedClientLines.get(j).getRack(),unprotectedClientLines.get(j).getSbrack(),unprotectedClientLines.get(j).getCard());
				 wssMap.setTpnLoc(mpnloc);
				 wssMap.setTpnLinePortNo(unprotectedClientLines.get(j).getLinePort());	
				 
				 dbService.getMcsMapService().Insert(wssMap); 
				 
				 j++;
				
			 }
		 }	
	}	
	
	
	public void fAssignWssPortMappingSingleCardBrField(int networkid,int networkidBf,int nodeid,int nodedegree) throws SQLException
	{
		 int wssPortId=0;
		 String NodeKey = networkid+"_"+nodeid;			 
		 List <CardInfo> wssAddDropCards = dbService.getCardInfoService().FindWssLevelOneCards(networkidBf, nodeid);
		 int j=0; // Counter for mpns
		 
		 Object id;

		 /* Get card redundancy */		
		 EquipmentPreferences eqp=new EquipmentPreferences(dbService);
		 String WssRedundancy = eqp.fgetRedundancyReq(networkidBf, nodeid, ResourcePlanConstants.CatAddDropWssCdRoadm);

		 String wssCardType=ResourcePlanConstants.CardWss8x12;
		 int wssMaxPorts=ResourcePlanConstants.MaxSwitchPortsWss2x8x12;
		 logger.info("Max Wss Ports in port mapping :"+wssMaxPorts+" WssCardTye:"+wssCardType);
		 
		 logger.info("fAssignWssPortMapping : nodeid:"+nodeid);
		 
		 /** Deleted line ports **/
		 List<PortInfo> DeletedLinePorts=dbService.getPortInfoService().FindAllUniqueDeletedBrField(networkid,networkidBf, nodeid);
		 System.out.println("fAssignWssPortMappingBrField : DeletedLinePorts --> "+DeletedLinePorts.size());
		 for (int i = 0; i < DeletedLinePorts.size(); i++) 
		 {		 
			 String mpnloc = rpu.locationStr(DeletedLinePorts.get(i).getRack(),DeletedLinePorts.get(i).getSbrack(),DeletedLinePorts.get(i).getCard());
			 List<McsMap> mapList=dbService.getMcsMapService().Find(networkidBf, nodeid, mpnloc);
			 if(mapList!=null)
			 {
				 mapList.forEach((map)->{
					 dbService.getMcsMapService().DeleteMcsPort(networkidBf, nodeid, map.getMcsId(), map.getRack(), map.getSbrack(), map.getCard(), map.getMcsSwitchPort());
					 logger.info("fAssignWssPortMappingBrField: WSS Port deleted from MCS Map"+ map.toString());
				 });
			 }
			 
			 /* If two level card was used for dropping, then delete entry from WssMap */
			 List<WssMap> mapListWss=dbService.getWssMapService().Find(networkidBf, nodeid, mpnloc);
			 if(mapListWss!=null)
			 {
				 mapListWss.forEach((map)->{
					 dbService.getWssMapService().DeleteWssPort(networkidBf, nodeid, map.getWssSetNo(), map.getRack(), map.getSbrack(), map.getCard(), map.getWssLevel2SwitchPort());
					 logger.info("fAssignWssPortMappingBrField: WSS Port deleted from WSS  Map"+ map.toString());
				 });
			 }
		 }
		 
		 
		 /***************** Added ---- Channel Protection Lines : OPX or OLP *****************/
		 List <PortInfo> channelPtcLines= dbService.getPortInfoService().FindAllChnlPtcLinesAddedBrField(networkid,networkidBf, nodeid);
		 System.out.println("Number of MPNs for Node "+nodeid+" with Channel Ptc are -> "+channelPtcLines.size());
		 if(channelPtcLines.size()>0)
		 {
			 for (int i = 0; i < wssAddDropCards.size(); i++) 
			 {			
				 int mcsId=i+1;
				 /** McsMap is also used for wss card mapping with mpn and edfa **/
				 McsMap wssMap = new McsMap();
				 wssMap.setNetworkId(networkidBf);
				 wssMap.setNodeId(nodeid);
				 wssMap.setRack(wssAddDropCards.get(i).getRack());
				 wssMap.setSbrack(wssAddDropCards.get(i).getSbrack());
				 wssMap.setCard(wssAddDropCards.get(i).getCard());
				 wssMap.setMcsId(mcsId);
				 				 
				 // Get max port id
				 id =dbService.getMcsMapService().GetMaxSwitchPortId(networkidBf,nodeid,mcsId);
				 if(id!=null)
					 wssPortId = Integer.parseInt(dbService.getMcsMapService().GetMaxSwitchPortId(networkidBf,nodeid,mcsId).toString());
				 else
					 wssPortId=1;	
				
				 //Loop through all mpns for this node and assign maxports with mcsid=i+1
				 // var k is used for remembering the last mpn assigned in previous set of wss
				 
				 while(j<channelPtcLines.size() && j<=wssMaxPorts*2){
					 wssMap.setMcsCommonPort(channelPtcLines.get(j).getDirection());

					 String mpnloc = rpu.locationStr(channelPtcLines.get(j).getRack(),channelPtcLines.get(j).getSbrack(),channelPtcLines.get(j).getCard());
					 wssMap.setTpnLoc(mpnloc);
					 wssMap.setTpnLinePortNo(channelPtcLines.get(j).getLinePort());	

					 // use Set 2 level 2 wss card for secondary line in case of channel protection
					 if(channelPtcLines.get(j).getLinePort()==ResourcePlanConstants.SECONDARY_LINE_PORT){					 
						 int SecondSetMaxPortId;
						 id =dbService.getMcsMapService().GetMaxSwitchPortId(networkidBf,nodeid,mcsId+1);
						 if(id!=null)
							 SecondSetMaxPortId = Integer.parseInt(dbService.getMcsMapService().GetMaxSwitchPortId(networkidBf,nodeid,mcsId+1).toString());
						 else
							 SecondSetMaxPortId=1;
			
						 McsMap secLineMap=new McsMap(networkidBf,nodeid,wssAddDropCards.get(i+1).getRack(),wssAddDropCards.get(i+1).getSbrack(),wssAddDropCards.get(i+1).getCard(),
								 mcsId+1,SecondSetMaxPortId,wssMap.getMcsCommonPort(),mpnloc,channelPtcLines.get(j).getLinePort(),"",0);

						 dbService.getMcsMapService().Insert(secLineMap);
					 }
					 else
					 {
						 wssMap.setMcsSwitchPort(wssPortId++);
						 dbService.getMcsMapService().Insert(wssMap);
					 }

					 j++;
//
//					 if(j%wssMaxPorts==0)
//						 break;
				 }
				
			 }	
		 }		 
		 
		 
		 /***************** Client Protection mpn port mapping *****************/ 
		 
		 //List <CardInfo> mpns = dbService.getCardInfoService().findMpns(networkid,nodeid);
		 List<PortInfo> protectedClientLines = dbService.getPortInfoService().FindAllClientPtcLinesAddedBrField(networkid, networkidBf, nodeid);
		 List<PortInfo> unprotectedClientLines = dbService.getPortInfoService().FindAllClientUnPtcLinesAddedBrField(networkid, networkidBf, nodeid);
		 System.out.println("fAssignWssPortMappingBrField :: Client ptc mpns lines Size -> "+protectedClientLines.size()+" Client unptc mpns lines Size -> "+unprotectedClientLines.size());
		 
		 if(protectedClientLines.size()>0) {
			 j=0;
			 for (int i = 0; i < wssAddDropCards.size(); i++) 
			 {	
				 int mcsId=i+1;
				 /** McsMap is also used for wss card mapping with mpn and edfa **/
				 McsMap wssMap = new McsMap();
				 wssMap.setNetworkId(networkidBf);
				 wssMap.setNodeId(nodeid);
				 wssMap.setRack(wssAddDropCards.get(i).getRack());
				 wssMap.setSbrack(wssAddDropCards.get(i).getSbrack());
				 wssMap.setCard(wssAddDropCards.get(i).getCard());
				 wssMap.setMcsId(mcsId);
				 
				 logger.info("Number of Unprotected MPNs Lines for Node :"+nodeid+" are -> "+protectedClientLines.size());
				 
				/** Get max port id **/
				 id =dbService.getMcsMapService().GetMaxSwitchPortId(networkidBf,nodeid,mcsId);
				 if(id!=null)
					 wssPortId = Integer.parseInt(dbService.getMcsMapService().GetMaxSwitchPortId(networkidBf,nodeid,mcsId).toString());
				 else
					 wssPortId=1;	
				 System.out.println("protectedClientLines :: wssport id --> "+wssPortId);
				 				
				//Loop through all mpns for this node and assign maxports with mcsid=i+1
				 while(j<protectedClientLines.size() && wssPortId<=wssMaxPorts){
					 
					 wssMap.setMcsCommonPort(protectedClientLines.get(j).getDirection());
					 wssMap.setMcsSwitchPort(wssPortId++);
					 String mpnloc = rpu.locationStr(protectedClientLines.get(j).getRack(),protectedClientLines.get(j).getSbrack(),protectedClientLines.get(j).getCard());
					 wssMap.setTpnLoc(mpnloc);
					 wssMap.setTpnLinePortNo(protectedClientLines.get(j).getLinePort());	
					 
					 dbService.getMcsMapService().Insert(wssMap); 
					 
					 if(WssRedundancy.equals(ResourcePlanConstants.Yes)) {
						 /** Next line is protection line**/
						 j++;
						 
						 /** Get max port id for set 2**/
						 int tempwssPortId;
						 id =dbService.getMcsMapService().GetMaxSwitchPortId(networkidBf,nodeid,mcsId+1);
						 if(id!=null)
							 tempwssPortId = Integer.parseInt(dbService.getMcsMapService().GetMaxSwitchPortId(networkidBf,nodeid,mcsId+1).toString());
						 else
							 tempwssPortId=1;	
						 
						 mpnloc = rpu.locationStr(protectedClientLines.get(j).getRack(),protectedClientLines.get(j).getSbrack(),protectedClientLines.get(j).getCard());
						 
						 McsMap secLineMap=new McsMap(networkidBf,nodeid,wssAddDropCards.get(i+1).getRack(),wssAddDropCards.get(i+1).getSbrack(),wssAddDropCards.get(i+1).getCard(),
								 mcsId+1,tempwssPortId,protectedClientLines.get(j).getDirection(),mpnloc,protectedClientLines.get(j).getLinePort(),"",0);
						 
//						 wssMap.setMcsCommonPort(protectedClientLines.get(j).getDirection());					 
//						 wssMap.setMcsSwitchPort(tempwssPortId);
//						 
//						 wssMap.setTpnLoc(mpnloc);
//						 wssMap.setTpnLinePortNo(protectedClientLines.get(j).getLinePort());
						 
						 dbService.getMcsMapService().Insert(secLineMap); 
					 }
					 
					 j++;
					 
//					 if(j%wssMaxPorts==0)
//						 break;
				 }
			 }				  
		 }
		 
		 if(unprotectedClientLines.size()>0) {
			 j=0;
			 for (int i = 0; i < wssAddDropCards.size(); i++) 
			 {	
				 int mcsId=i+1;
				 /** McsMap is also used for wss card mapping with mpn and edfa **/
				 McsMap wssMap = new McsMap();
				 wssMap.setNetworkId(networkidBf);
				 wssMap.setNodeId(nodeid);
				 wssMap.setRack(wssAddDropCards.get(i).getRack());
				 wssMap.setSbrack(wssAddDropCards.get(i).getSbrack());
				 wssMap.setCard(wssAddDropCards.get(i).getCard());
				 wssMap.setMcsId(mcsId);
				 
				 logger.info("Number of Unprotected MPNs Lines for Node :"+nodeid+" are -> "+unprotectedClientLines.size());
				 
				/** Get max port id **/
				 id =dbService.getMcsMapService().GetMaxSwitchPortId(networkidBf,nodeid,mcsId);
				 if(id!=null)
					 wssPortId = Integer.parseInt(dbService.getMcsMapService().GetMaxSwitchPortId(networkidBf,nodeid,mcsId).toString());
				 else
					 wssPortId=1;	
				 
				 /** Subtract channel ptc ports already assigned from max ports **/
//				 wssMaxPorts=wssMaxPorts-wssPortId+1;
				
				//Loop through all mpns for this node and assign maxports with mcsid=i+1
				 while(j<unprotectedClientLines.size() && wssPortId<=wssMaxPorts){
					 
					 wssMap.setMcsCommonPort(unprotectedClientLines.get(j).getDirection());
					 wssMap.setMcsSwitchPort(wssPortId++);
					 String mpnloc = rpu.locationStr(unprotectedClientLines.get(j).getRack(),unprotectedClientLines.get(j).getSbrack(),unprotectedClientLines.get(j).getCard());
					 wssMap.setTpnLoc(mpnloc);
					 wssMap.setTpnLinePortNo(unprotectedClientLines.get(j).getLinePort());	
					 
					 dbService.getMcsMapService().Insert(wssMap); 
					 
					 j++;
					 
//					 if(j%wssMaxPorts==0)
//						 break;
				 }
			 }	
		 }
		 
	}	
	
	public void fAssignWssPortMappingDoubleCardBrField(int networkid,int networkidBf,int nodeid,int nodedegree) throws SQLException
	{
		 int wssPortId=0;
		 String NodeKey = networkid+"_"+nodeid;			 
		 List <CardInfo> wssCardsLevel2 = dbService.getCardInfoService().FindWssLevelTwoCards(networkidBf, nodeid);
		 List <CardInfo> wssCardsLevel1 = dbService.getCardInfoService().FindWssLevelOneCards(networkidBf, nodeid);
		 System.out.println("fAssignWssPortMappingDoubleCardBrField :: wssCardsLevel2 size -- "+wssCardsLevel2.size()+" wssCardsLevel1 size -- "+wssCardsLevel1.size());
		 int j=0; // Counter for mpns
		 
		 Object id;

		 /* Get card redundancy */		
		 EquipmentPreferences eqp=new EquipmentPreferences(dbService);

		 String wssCardType;
		 int wssMaxPorts;
		 		 
		 logger.info("fAssignWssPortMappingDoubleCardBrField : nodeid:"+nodeid);
		 
		 /** Deleted line ports **/
		 List<PortInfo> DeletedLinePorts=dbService.getPortInfoService().FindAllUniqueDeletedBrField(networkid,networkidBf, nodeid);
		 System.out.println("fAssignWssPortMappingDoubleCardBrField : DeletedLinePorts --> "+DeletedLinePorts.size());
		 for (int i = 0; i < DeletedLinePorts.size(); i++) 
		 {		 
			 String mpnloc = rpu.locationStr(DeletedLinePorts.get(i).getRack(),DeletedLinePorts.get(i).getSbrack(),DeletedLinePorts.get(i).getCard());
			 List<McsMap> mapList=dbService.getMcsMapService().Find(networkidBf, nodeid, mpnloc);
			 if(mapList!=null)
			 {
				 mapList.forEach((map)->{
					 dbService.getMcsMapService().DeleteMcsPort(networkidBf, nodeid, map.getMcsId(), map.getRack(), map.getSbrack(), map.getCard(), map.getMcsSwitchPort());
					 logger.info("fAssignWssPortMappingBrField: WSS Port deleted from MCS Map"+ map.toString());
				 });
			 }
			 
			 /* If two level card was used for dropping, then delete entry from WssMap */
			 List<WssMap> mapListWss=dbService.getWssMapService().Find(networkidBf, nodeid, mpnloc);
			 if(mapListWss!=null)
			 {
				 mapListWss.forEach((map)->{
					 dbService.getWssMapService().DeleteWssPort(networkidBf, nodeid, map.getWssSetNo(), map.getRack(), map.getSbrack(), map.getCard(), map.getWssLevel2SwitchPort());
					 logger.info("fAssignWssPortMappingBrField: WSS Port deleted from WSS  Map"+ map.toString());
				 });
			 }
		 }
		 
		 
		 /***************** Added ---- Channel Protection Lines : OPX or OLP *****************/
		 List <PortInfo> channelPtcLines= dbService.getPortInfoService().FindAllChnlPtcLinesAddedBrField(networkid,networkidBf, nodeid);
		 System.out.println("Number of MPNs for Node "+nodeid+" with Channel Ptc are -> "+channelPtcLines.size());
		 
		 if(channelPtcLines.size()>0)
		 {
			 j=0;
			 for (int i = 0; i < wssCardsLevel2.size(); i++) 
			 {							 
				 wssCardType=wssCardsLevel2.get(i).getCardType();
				 // On basis of card type get max ports
				 wssMaxPorts=rpu.fGetMaxSwitchPortsforCardType(wssCardType);	
				 logger.info("Max Wss Ports in port mapping :"+wssMaxPorts+" WssCardTye:"+wssCardType);

				 WssMap wssMap = new WssMap();
				 wssMap.setNetworkId(networkidBf);
				 wssMap.setNodeId(nodeid);
				 wssMap.setRack(wssCardsLevel2.get(i).getRack());
				 wssMap.setSbrack(wssCardsLevel2.get(i).getSbrack());
				 wssMap.setCard(wssCardsLevel2.get(i).getCard());
				 wssMap.setCardType(wssCardsLevel2.get(i).getCardType());
				 wssMap.setCardSubtype(0);
				 wssMap.setWssLevel1Loc(wssCardsLevel1.get(i).getRack()+"_"+wssCardsLevel1.get(i).getSbrack()+"_"+wssCardsLevel1.get(i).getCard());
				 wssMap.setWssSetNo(i+1);
				 
				 wssMap.setWssLevel2CommonPort(""+1);//Debug
				 wssMap.setWssLevel1SwitchPort(""+1);//Debug
				 				 
				 // Get max port id
				 id =dbService.getWssMapService().GetMaxSwitchPortId(networkidBf,nodeid,i+1);
				 if(id!=null)
					 wssPortId = Integer.parseInt(dbService.getWssMapService().GetMaxSwitchPortId(networkidBf,nodeid,i+1).toString());
				 else
					 wssPortId=1;	
				
				 //Loop through all mpns for this node and assign maxports with mcsid=i+1
				 
				 while(j<channelPtcLines.size() && wssPortId<=wssMaxPorts){
					 
					 wssMap.setWssLevel1CommonPort(channelPtcLines.get(j).getDirection());

					 String mpnloc = rpu.locationStr(channelPtcLines.get(j).getRack(),channelPtcLines.get(j).getSbrack(),channelPtcLines.get(j).getCard());
					 wssMap.setTpnLoc(mpnloc);
					 wssMap.setTpnLinePortNo(channelPtcLines.get(j).getLinePort());	
					 // use Set 2 level 2 wss card for secondary line in case of channel protection
					 if(channelPtcLines.get(j).getLinePort()==ResourcePlanConstants.SECONDARY_LINE_PORT){
						 String level1Loc=wssCardsLevel1.get(i+1).getRack()+"_"+wssCardsLevel1.get(i+1).getSbrack()+"_"+wssCardsLevel1.get(i+1).getCard();
						 int wssPortIdSec;
						 Object SecondSetMaxPortId =dbService.getWssMapService().GetMaxSwitchPortId(networkidBf,nodeid,i+2);
						 if(SecondSetMaxPortId!=null)
							 wssPortIdSec = Integer.parseInt(dbService.getWssMapService().GetMaxSwitchPortId(networkidBf,nodeid,i+2).toString());
						 else
							 wssPortIdSec=1;

						 WssMap secLineMap=new WssMap(networkidBf,nodeid,wssCardsLevel2.get(i+1).getRack(),wssCardsLevel2.get(i+1).getSbrack(),wssCardsLevel2.get(i+1).getCard(),
								 wssCardsLevel2.get(i+1).getCardType(),0,i+2,wssPortIdSec,""+1,level1Loc,""+1,channelPtcLines.get(j).getDirection(),mpnloc,channelPtcLines.get(j).getLinePort(),"",0);

						 dbService.getWssMapService().Insert(secLineMap);


					 }
					 else
					 {
						 wssMap.setWssLevel2SwitchPort(wssPortId++);
						 dbService.getWssMapService().Insert(wssMap);
					 }
					 
					 j++;

//					 if(j%wssMaxPorts==0)
//						 break;

				 }
				
			 }	
		 }		 
		 
		 
		 /***************** Client Protection mpn port mapping *****************/ 
		 
		 //List <CardInfo> mpns = dbService.getCardInfoService().findMpns(networkid,nodeid);
		 List<PortInfo> protectedClientLines = dbService.getPortInfoService().FindAllClientPtcLinesAddedBrField(networkid, networkidBf, nodeid);
		 List<PortInfo> unprotectedClientLines = dbService.getPortInfoService().FindAllClientUnPtcLinesAddedBrField(networkid, networkidBf, nodeid);
		 System.out.println("fAssignWssPortMappingDoubleCardBrField :: Client ptc mpns lines Size -> "+protectedClientLines.size()+" Client unptc mpns lines Size -> "+unprotectedClientLines.size());
		 
		 List<PortInfo> clientLines=protectedClientLines;
		 clientLines.addAll(unprotectedClientLines);
		 
		 if(clientLines.size()>0) {
			 j=0;
			 for (int i = 0; i < wssCardsLevel2.size(); i++) 
			 {	
				 wssCardType=wssCardsLevel2.get(i).getCardType();
				 // On basis of card type get max ports
				 wssMaxPorts=rpu.fGetMaxSwitchPortsforCardType(wssCardType);	
				 logger.info("Max Wss Ports in port mapping :"+wssMaxPorts+" WssCardTye:"+wssCardType);

				 WssMap wssMap = new WssMap();
				 wssMap.setNetworkId(networkidBf);
				 wssMap.setNodeId(nodeid);
				 wssMap.setRack(wssCardsLevel2.get(i).getRack());
				 wssMap.setSbrack(wssCardsLevel2.get(i).getSbrack());
				 wssMap.setCard(wssCardsLevel2.get(i).getCard());
				 wssMap.setCardType(wssCardsLevel2.get(i).getCardType());
				 wssMap.setCardSubtype(0);
				 wssMap.setWssLevel1Loc(wssCardsLevel1.get(i).getRack()+"_"+wssCardsLevel1.get(i).getSbrack()+"_"+wssCardsLevel1.get(i).getCard());
				 wssMap.setWssSetNo(i+1);
				 
				 wssMap.setWssLevel2CommonPort(""+1);//Debug
				 wssMap.setWssLevel1SwitchPort(""+1);//Debug
				 				 
				 logger.info("Number of Client Protected MPNs Lines for Node :"+nodeid+" are -> "+clientLines.size());
				 
				/** Get max port id **/
				 id =dbService.getWssMapService().GetMaxSwitchPortId(networkidBf,nodeid,i+1);
				 if(id!=null)
					 wssPortId = Integer.parseInt(dbService.getWssMapService().GetMaxSwitchPortId(networkidBf,nodeid,i+1).toString());
				 else
					 wssPortId=1;	
				 System.out.println("protectedClientLines :: wssport id --> "+wssPortId);
				 				
				//Loop through all mpns for this node and assign maxports with mcsid=i+1
				 while(j<clientLines.size() && wssPortId<=wssMaxPorts){
					 
					 wssMap.setWssLevel1CommonPort(clientLines.get(j).getDirection());

					 String mpnloc = rpu.locationStr(clientLines.get(j).getRack(),clientLines.get(j).getSbrack(),clientLines.get(j).getCard());
					 wssMap.setTpnLoc(mpnloc);
					 wssMap.setTpnLinePortNo(clientLines.get(j).getLinePort());	
					 
					 wssMap.setWssLevel2SwitchPort(wssPortId++);
					 dbService.getWssMapService().Insert(wssMap);
					 								 
					 j++;
					 
//					 if(j%wssMaxPorts==0)
//						 break;
				 }
			 }				  
		 }
		 
	}	
	
//	public void fgenerateMcsMapBrField(int networkid) throws SQLException
//	{		
//		logger.info("fgenerateMcsMapBrField");
//		int switchPortId;
//		int networkidBF = Integer.parseInt(dbService.getNetworkService().GetBrownFieldNetworkId(networkid).toString());
//		List <Integer> nodelist=rpu.fgetNodesToConfigure(networkidBF);
//		 for (int k = 0; k < nodelist.size(); k++) 
//		 {	
//			 
//			 int nodeid =nodelist.get(k);
//			 int nodedegree=dbService.getNodeService().FindNode(networkidBF, nodeid).getDegree();
//			 int nodetype=dbService.getNodeService().FindNode(networkidBF, nodeid).getNodeType();
//			 
//			 if(nodetype==MapConstants.roadm){
//				 List<Demand> demandsDel = dbService.getDemandService().FindDeletedDemandsInBrField(networkid, networkidBF, nodeid);
//				 System.out.println("demandsDel size"+demandsDel.size());
//				 for (int j = 0; j < demandsDel.size(); j++)
//				 {
//					 List <CardInfo> mpns = dbService.getCardInfoService().FindMpn(networkid, nodeid, demandsDel.get(j).getDemandId());
//					 for (int l = 0; l < mpns.size(); l++) 
//					 {	
//						 String tpnloc = rpu.locationStr(mpns.get(l).getRack(), mpns.get(l).getSbrack(), mpns.get(l).getCard());
////						 McsMap map = dbService.getMcsMapService().Find(networkidBF, nodeid, tpnloc);
//						 List<McsMap> mapList=dbService.getMcsMapService().Find(networkidBF, nodeid, tpnloc);
//						 if(mapList!=null)
//						 {
//							 mapList.forEach((map)->{
//								 dbService.getMcsMapService().DeleteMcsPort(networkidBF, nodeid, map.getMcsId(), map.getRack(), map.getSbrack(), map.getCard(), map.getMcsSwitchPort());
//								 logger.info("fgenerateMcsMapBrField: Mcs Port deleted "+ map.toString());
//							 });
////							 dbService.getMcsMapService().DeleteMcsPort(networkidBF, nodeid, map.getMcsId(), map.getRack(), map.getSbrack(), map.getCard(), map.getMcsSwitchPort());
////							 logger.info("fgenerateMcsMapBrField: Mcs Port deleted "+ map.toString());
//						 }
//					 }				
//				 }
//				 
//				 
//				 List<Demand> demandsNew = dbService.getDemandService().FindAddedDemandsInBrField(networkid, networkidBF,nodeid);
//				 System.out.println("demandsNew size"+demandsNew.size());
//				 for (int i = 0; i < demandsNew.size(); i++) 
//				 {
//					List <CardInfo> mpns = dbService.getCardInfoService().FindMpn(networkidBF, nodeid, demandsNew.get(i).getDemandId());
//					 
//					 for (int l = 0; l < mpns.size(); l++) 
//					 {			
//									 
//						 McsMap mcsMap = dbService.getMcsMapService().FindMcsWithMaxMcsId(networkidBF, nodeid);
//						 if(mcsMap!=null)
//						 {
//							 switchPortId = Integer.parseInt(dbService.getMcsMapService().GetMaxSwitchPortId(networkidBF, nodeid, mcsMap.getMcsId()).toString());
//							 if(switchPortId<=16)//as max 16 ports are available on one mcs
//							 {
//								 McsMap map = new McsMap();
//								 map.setNetworkId(networkidBF);
//								 map.setNodeId(nodeid);
//								 map.setRack(mcsMap.getRack());
//								 map.setSbrack(mcsMap.getSbrack());
//								 map.setCard(mcsMap.getCard());
//								 map.setMcsId(mcsMap.getMcsId());
//								 map.setMcsCommonPort(mpns.get(l).getDirection());
//								 map.setMcsSwitchPort(switchPortId);
//								 map.setTpnLoc(rpu.locationStr(mpns.get(l).getRack(), mpns.get(l).getSbrack(), mpns.get(l).getCard()));
//								 map.setTpnLinePortNo(ResourcePlanConstants.PRIMARY_LINE_PORT);
//								 dbService.getMcsMapService().Insert(map);
//								 logger.info("fgenerateMcsMapBrField: Mcs Port inserted "+ map.toString());
//							 }
//							 else
//							 {
//								 
//							 }
//						 }
//						 else
//						 {
//							 List <CardInfo> mcs = dbService.getCardInfoService().FindCardInfoByCardType(networkidBF, nodeid, ResourcePlanConstants.CardMcs);
//							 for (int j = 0; j < mcs.size(); j++) 
//							 {
//								 McsMap map = new McsMap();
//								 map.setNetworkId(networkidBF);
//								 map.setNodeId(nodeid);
//								 map.setRack(mcs.get(i).getRack());
//								 map.setSbrack(mcs.get(i).getSbrack());
//								 map.setCard(mcs.get(i).getCard());
//								 map.setMcsId(i+1);
//								 map.setMcsCommonPort(mpns.get(l).getDirection());
//								 Object id =dbService.getMcsMapService().GetMaxSwitchPortId(networkidBF,nodeid,i+1);
//								 if(id!=null)
//									 switchPortId = Integer.parseInt(dbService.getMcsMapService().GetMaxSwitchPortId(networkidBF,nodeid,i+1).toString());
//								 else
//									 switchPortId=1;
//								 
//								 map.setMcsSwitchPort(switchPortId);
//								 map.setTpnLoc(rpu.locationStr(mpns.get(l).getRack(), mpns.get(l).getSbrack(), mpns.get(l).getCard()));
//								 map.setTpnLinePortNo(ResourcePlanConstants.PRIMARY_LINE_PORT);		
//								 dbService.getMcsMapService().Insert(map);	
//								 logger.info("fgenerateMcsMapBrField: Mcs Port inserted "+ map.toString());
//							 }
//							 
//						 }
//					 }
//					
//				 }	
//				 
//				 fUpdateMcsMapForEdfa(networkidBF,nodeid,nodedegree); //Debug
//			 }			 		 			 
//			
//		 }		 
//		 
//	}

	
	public void fgenerateMcsMapBrField(int networkid,int networkidBf,int nodeid,int nodedegree) throws SQLException
	{	 
		 System.out.println("fgenerateMcsMapBrField --- NodeId -> "+nodeid);
		 
		 /** Deleted line ports **/
		 List<PortInfo> DeletedLinePorts=dbService.getPortInfoService().FindAllUniqueDeletedBrField(networkid,networkidBf, nodeid);
		 System.out.println("DeletedLinePorts "+DeletedLinePorts.size());
		 for (int i = 0; i < DeletedLinePorts.size(); i++) 
		 {		 
			 String mpnloc = rpu.locationStr(DeletedLinePorts.get(i).getRack(),DeletedLinePorts.get(i).getSbrack(),DeletedLinePorts.get(i).getCard());
			 List<McsMap> mapList=dbService.getMcsMapService().Find(networkidBf, nodeid, mpnloc);
			 if(mapList!=null)
			 {
				 mapList.forEach((map)->{
					 dbService.getMcsMapService().DeleteMcsPort(networkidBf, nodeid, map.getMcsId(), map.getRack(), map.getSbrack(), map.getCard(), map.getMcsSwitchPort());
					 logger.info("fgenerateMcsMapBrField: Mcs Port deleted "+ map.toString());
				 });
			 }
		 }

		 int j=0;
		 int switchPortId=0;	
		 
		 /** Added line ports **/
		 List<PortInfo> AddedLinePorts=dbService.getPortInfoService().FindAllUniqueAddedBrField(networkid,networkidBf, nodeid);
		 
		 /** If new line ports are added then add them to mcs map**/
		 if(AddedLinePorts.size()>0) {
			 List <CardInfo> mcs = dbService.getCardInfoService().FindCardInfoByCardType(networkidBf, nodeid, ResourcePlanConstants.CardMcs);
			 System.out.println("AddedLinePorts "+AddedLinePorts.size()+" Mcs Cards -> "+mcs.size());
			 for (int i = 0; i < mcs.size(); i++) 
			 {			 
				 McsMap map = new McsMap();
				 map.setNetworkId(networkidBf);
				 map.setNodeId(nodeid);
				 map.setRack(mcs.get(i).getRack());
				 map.setSbrack(mcs.get(i).getSbrack());
				 map.setCard(mcs.get(i).getCard());
				 map.setMcsId(i+1);
				 
				 while(j<AddedLinePorts.size()){
					 
					 switchPortId =Integer.parseInt(dbService.getMcsMapService().GetFirstFreeSwitchPortId(networkidBf,nodeid,i+1).toString());
					 
					 // If switch port > 16 then move to next mcs
					 if(switchPortId>ResourcePlanConstants.MaxSwitchPortsMcs)
						 break;
					 
					 map.setMcsSwitchPort(switchPortId);
					 String mpnloc = rpu.locationStr(AddedLinePorts.get(j).getRack(),AddedLinePorts.get(j).getSbrack(),AddedLinePorts.get(j).getCard());
					 map.setTpnLoc(mpnloc);
					 map.setTpnLinePortNo(AddedLinePorts.get(j).getLinePort());		
					 map.setMcsCommonPort(AddedLinePorts.get(j).getDirection());
					 dbService.getMcsMapService().Insert(map);
					 logger.info("fgenerateMcsMapBrField: Mcs Port inserted "+ map.toString());
					 j++;				 
				 }
				 
			 }
		 }
		 
//		 fUpdateMcsMapForEdfa(networkidBf,nodeid,nodedegree); //Debug		 
	}
	
//	public void fUpdateMcsMapForEdfa(int networkid,int nodeid,int nodedegree) throws SQLException
//	{
//		String NodeKey = networkid+"_"+nodeid;	
//		List <Integer> mcsIds = dbService.getMcsMapService().FindMcsIds(networkid, nodeid,true); // Find unique mcs ids
//		List<CardInfo> edfas = dbService.getCardInfoService().FindCardInfoByCardType(networkid, nodeid, ResourcePlanConstants.CardEdfa);
//		//			 System.out.println("McsId size::"+mcsIds.size()+" edfas size::"+edfas.size());
//		int edfaIndex=0;
//		for (int i = 0; i < mcsIds.size(); i++) 
//		{			
//			List<String> commonPorts = dbService.getMcsMapService().FindCommonPortUsed(networkid, nodeid, mcsIds.get(i));
//			//				System.out.println("commonPorts size for nodeid="+nodeid+"mcsId:"+ mcsIds.get(i)+" is="+commonPorts.size() );
//			if(commonPorts.size()<=4)
//			{
//				//					System.out.println("edfaIndex ::"+edfaIndex+" commonPorts.size < 4");
//				//List<CardInfo> edfas = dbService.getCardInfoService().FindCardInfoByCardType(networkid, nodeid, ResourcePlanConstants.CardEdfa);
//				String edfaloc =  ""+edfas.get(edfaIndex).getRack()+"_"+edfas.get(edfaIndex).getSbrack()+"_"+edfas.get(edfaIndex).getCard();
//
//				edfaIndex++;	
//				//int edfaid=1;
//				int edfaid=edfaIndex;
//
//				if(dbService.getMcsMapService().CheckIdEdfaLocExists(networkid, nodeid, mcsIds.get(i),1)==null)
//					dbService.getMcsMapService().UpdateEdfaLoc(networkid, nodeid, mcsIds.get(i), edfaloc, edfaid);// it will update all the rows with particular mcs id	
//			}
//			else
//			{
//				//					System.out.println("edfaIndex ::"+edfaIndex+" commonPorts.size > 4");
//				//List<CardInfo> edfas = dbService.getCardInfoService().FindCardInfoByCardType(networkid, nodeid, ResourcePlanConstants.CardEdfa);
//				String edfaloc1 =  ""+edfas.get(edfaIndex).getRack()+"_"+edfas.get(edfaIndex).getSbrack()+"_"+edfas.get(edfaIndex).getCard();
//
//				edfaIndex++;	
//				int edfaid1=edfaIndex;
//
//				//					int edfaid1=1;
//				//					edfaIndex++;
//				String edfaloc2 =  ""+edfas.get(edfaIndex).getRack()+"_"+edfas.get(edfaIndex).getSbrack()+"_"+edfas.get(edfaIndex).getCard();
//
//				edfaIndex++;	
//				int edfaid2=edfaIndex;
//
//				//					int edfaid2=2;
//				//					edfaIndex++;
//
//				for (int j = 0; j < commonPorts.size(); j++) 
//				{
//					if(j<4)
//					{
//						List <McsMap> rows = dbService.getMcsMapService().FindMcs(networkid, nodeid, mcsIds.get(i), commonPorts.get(j));
//						for (int l = 0; l < rows.size(); l++) 
//						{
//							if(dbService.getMcsMapService().CheckIdEdfaLocExists(networkid, nodeid, mcsIds.get(i), rows.get(l).getMcsSwitchPort())==null)
//								dbService.getMcsMapService().UpdateEdfaLoc(networkid, nodeid, mcsIds.get(i), rows.get(l).getMcsSwitchPort(), edfaloc1, edfaid1);								
//						}
//					}
//					else
//					{
//						List <McsMap> rows = dbService.getMcsMapService().FindMcs(networkid, nodeid, mcsIds.get(i), commonPorts.get(j));
//						for (int l = 0; l < rows.size(); l++) 
//						{
//							if(dbService.getMcsMapService().CheckIdEdfaLocExists(networkid, nodeid, mcsIds.get(i), rows.get(l).getMcsSwitchPort())==null)
//								dbService.getMcsMapService().UpdateEdfaLoc(networkid, nodeid, mcsIds.get(i), rows.get(l).getMcsSwitchPort(),edfaloc2, edfaid2);								
//						}							
//					}						
//				}				
//
//			}
//
//		}
//	}
	
	
	public void fUpdateMcsMapForEdfa(int networkid,int nodeid,int nodedegree) throws SQLException
	{
		String NodeKey = networkid+"_"+nodeid;	
		List <Integer> mcsIds = dbService.getMcsMapService().FindMcsIds(networkid, nodeid,true); // Find unique mcs ids
		List<CardInfo> edfas = dbService.getCardInfoService().FindCardInfoByCardType(networkid, nodeid, ResourcePlanConstants.CardEdfa);
		//			 System.out.println("McsId size::"+mcsIds.size()+" edfas size::"+edfas.size());
		Collections.sort(edfas,new Comparator<CardInfo>() {
			@Override
			public int compare(CardInfo a, CardInfo b){return sortBySubrack(a,b);} 
		}); 
		int edfaIndex=0;
		int edfaid=1,edfaid1,edfaid2;
		System.out.println("******************fUpdateMcsMapForEdfa ***************");
		System.out.println("nodeid ::"+nodeid+" nodedegree:"+nodedegree);
		for (int i = 0; i < mcsIds.size(); i++) 
		{			
			List<String> commonPortsLowerDegreeDir = dbService.getMcsMapService().FindCommonPortUsedLowerDegreeDir(networkid, nodeid, mcsIds.get(i));
			List<String> commonPortsUpperDegreeDir = dbService.getMcsMapService().FindCommonPortUsedUpperDegreeDir(networkid, nodeid, mcsIds.get(i));
			
			System.out.println("commonPortsLowerDegreeDir size::"+commonPortsLowerDegreeDir.size()
			+"  commonPortsUpperDegreeDir size::"+commonPortsUpperDegreeDir.size());
			
	
			if(commonPortsLowerDegreeDir.size()>0) {
				String edfaloc1 =  ""+edfas.get(edfaIndex).getRack()+"_"+edfas.get(edfaIndex).getSbrack()+"_"+edfas.get(edfaIndex).getCard();
				edfaIndex++;	
				edfaid1=edfaid;
				for (int j = 0; j < commonPortsLowerDegreeDir.size(); j++) 
				{
					List <McsMap> rows = dbService.getMcsMapService().FindMcs(networkid, nodeid, mcsIds.get(i), commonPortsLowerDegreeDir.get(j));
					for (int l = 0; l < rows.size(); l++) 
					{
						if(rows.get(l).getEdfaLoc()==null)
							dbService.getMcsMapService().UpdateEdfaLoc(networkid, nodeid, mcsIds.get(i), rows.get(l).getMcsSwitchPort(), edfaloc1, edfaid1);								
					}
				}
			}
			edfaid++;

			if(commonPortsUpperDegreeDir.size()>0) {
				String edfaloc2 =  ""+edfas.get(edfaIndex).getRack()+"_"+edfas.get(edfaIndex).getSbrack()+"_"+edfas.get(edfaIndex).getCard();
				edfaIndex++;	
				edfaid2=edfaid;

				for (int j = 0; j < commonPortsUpperDegreeDir.size(); j++) 
				{
					List <McsMap> rows = dbService.getMcsMapService().FindMcs(networkid, nodeid, mcsIds.get(i), commonPortsUpperDegreeDir.get(j));
					for (int l = 0; l < rows.size(); l++) 
					{
						if(rows.get(l).getEdfaLoc()==null)
							dbService.getMcsMapService().UpdateEdfaLoc(networkid, nodeid, mcsIds.get(i), rows.get(l).getMcsSwitchPort(),edfaloc2, edfaid2);								
					}													
				}	
			}
			edfaid++;
		}
	}
	
//	public void fUpdateWssMapForEdfa(int networkid,int nodeid,int nodedegree) throws SQLException
//	{
//		String NodeKey = networkid+"_"+nodeid;	
//				
//		List <Integer> wssSets = dbService.getMcsMapService().FindWssSets(networkid, nodeid); // Distinct Set Nos
//		List<CardInfo> edfas = dbService.getCardInfoService().FindCardInfoByCardType(networkid, nodeid, ResourcePlanConstants.CardEdfa);
//		int edfaIndex=0;
//		System.out.println("Wss Set size:: "+wssSets.size()+" edfas size:: "+edfas.size());
//		for (int i = 0; i < wssSets.size(); i++) 
//		{				 
//			List<String> commonPorts = dbService.getMcsMapService().FindCommonPortUsed(networkid, nodeid, wssSets.get(i));				
//			if(commonPorts.size()<=4)
//			{
//				//List<CardInfo> edfas = dbService.getCardInfoService().FindCardInfoByCardType(networkid, nodeid, ResourcePlanConstants.CardEdfa);
//				String edfaloc =  ""+edfas.get(edfaIndex).getRack()+"_"+edfas.get(edfaIndex).getSbrack()+"_"+edfas.get(edfaIndex).getCard();
//				edfaIndex++;
//				int edfaid=edfaIndex;
//				if(dbService.getMcsMapService().CheckIdEdfaLocExists(networkid, nodeid, wssSets.get(i),1)==null)
//					dbService.getMcsMapService().UpdateEdfaLoc(networkid, nodeid, wssSets.get(i), edfaloc, edfaid);				// it will update all the rows with particular mcs id	
//			}
//			else
//			{
//				//List<CardInfo> edfas = dbService.getCardInfoService().FindCardInfoByCardType(networkid, nodeid, ResourcePlanConstants.CardEdfa);
//				String edfaloc1 =  ""+edfas.get(edfaIndex).getRack()+"_"+edfas.get(edfaIndex).getSbrack()+"_"+edfas.get(edfaIndex).getCard();
//				edfaIndex++;
//				int edfaid1=edfaIndex;
//				String edfaloc2 =  ""+edfas.get(edfaIndex).getRack()+"_"+edfas.get(edfaIndex).getSbrack()+"_"+edfas.get(edfaIndex).getCard();
//				edfaIndex++;
//				int edfaid2=edfaIndex;
//				for (int j = 0; j < commonPorts.size(); j++) 
//				{
//					if(j<4)
//					{
//						List <McsMap> rows = dbService.getMcsMapService().FindWss(networkid, nodeid, wssSets.get(i), commonPorts.get(j));
//						for (int l = 0; l < rows.size(); l++) 
//						{
//							int switchPort=rows.get(l).getMcsSwitchPort();
//							if(dbService.getMcsMapService().CheckIdEdfaLocExists(networkid, nodeid, wssSets.get(i), switchPort)==null)
//								dbService.getMcsMapService().UpdateEdfaLoc(networkid, nodeid, wssSets.get(i), switchPort, edfaloc1, edfaid1);								
//						}
//					}
//					else
//					{
//						List <McsMap> rows = dbService.getMcsMapService().FindWss(networkid, nodeid, wssSets.get(i), commonPorts.get(j));
//						for (int l = 0; l < rows.size(); l++) 
//						{
//							int switchPort=rows.get(l).getMcsSwitchPort();
//							if(dbService.getMcsMapService().CheckIdEdfaLocExists(networkid, nodeid, wssSets.get(i), switchPort)==null)
//								dbService.getMcsMapService().UpdateEdfaLoc(networkid, nodeid, wssSets.get(i), switchPort,edfaloc2, edfaid2);								
//						}							
//					}						
//				}				
//
//			}
//
//		}
//	}
	
	public void fUpdateWssMapForEdfa(int networkid,int nodeid,int nodedegree) throws SQLException
	{
		String NodeKey = networkid+"_"+nodeid;	
				
		List <Integer> wssSets = dbService.getMcsMapService().FindWssSets(networkid, nodeid); // Distinct Set Nos
		List<CardInfo> edfas = dbService.getCardInfoService().FindCardInfoByCardType(networkid, nodeid, ResourcePlanConstants.CardEdfa);
		
		edfas.forEach(edfa->{
			System.out.println("Before ******* Edfa rack:"+edfa.getRack()+"Edfa subrack:"+edfa.getSbrack()+"Edfa card:"+edfa.getCard());
		});
		
		Collections.sort(edfas,new Comparator<CardInfo>() {
			@Override
			public int compare(CardInfo a, CardInfo b){return sortBySubrack(a,b);} 
		}); 
		

		edfas.forEach(edfa->{
			System.out.println("After ******* Edfa rack:"+edfa.getRack()+"Edfa subrack:"+edfa.getSbrack()+"Edfa card:"+edfa.getCard());
		});
		int edfaIndex=0;
		int edfaid=1,edfaid1,edfaid2;
		System.out.println("Wss Set size:: "+wssSets.size()+" edfas size:: "+edfas.size());
		for (int i = 0; i < wssSets.size(); i++) 
		{				 
			List<String> commonPortsLowerDegreeDir = dbService.getMcsMapService().FindCommonPortUsedLowerDegreeDir(networkid, nodeid, wssSets.get(i));
			List<String> commonPortsUpperDegreeDir = dbService.getMcsMapService().FindCommonPortUsedUpperDegreeDir(networkid, nodeid, wssSets.get(i));
			
			System.out.println("NodeId:"+nodeid+" commonPortsLowerDegreeDir size::"+commonPortsLowerDegreeDir.size()
			+"  commonPortsUpperDegreeDir size::"+commonPortsUpperDegreeDir.size());
			
	
			if(commonPortsLowerDegreeDir.size()>0) {
				String edfaloc1 =  ""+edfas.get(edfaIndex).getRack()+"_"+edfas.get(edfaIndex).getSbrack()+"_"+edfas.get(edfaIndex).getCard();
				edfaIndex++;	
				edfaid1=edfaid;
				for (int j = 0; j < commonPortsLowerDegreeDir.size(); j++) 
				{
					List <McsMap> rows = dbService.getMcsMapService().FindMcs(networkid, nodeid, wssSets.get(i), commonPortsLowerDegreeDir.get(j));
					for (int l = 0; l < rows.size(); l++) 
					{
//						if(dbService.getMcsMapService().CheckIdEdfaLocExists(networkid, nodeid, wssSets.get(i), rows.get(l).getMcsSwitchPort())==null)
						if(rows.get(l).getEdfaLoc()==null || rows.get(l).getEdfaLoc().equals(""))
							{
								dbService.getMcsMapService().UpdateEdfaLoc(networkid, nodeid, wssSets.get(i), rows.get(l).getMcsSwitchPort(), edfaloc1, edfaid1);								
							}
						else System.out.println("Edfa location Not Null....");
					}
				}
			}
			edfaid++;

			if(commonPortsUpperDegreeDir.size()>0) {
				String edfaloc2 =  ""+edfas.get(edfaIndex).getRack()+"_"+edfas.get(edfaIndex).getSbrack()+"_"+edfas.get(edfaIndex).getCard();
				edfaIndex++;	
				edfaid2=edfaid;

				for (int j = 0; j < commonPortsUpperDegreeDir.size(); j++) 
				{
					List <McsMap> rows = dbService.getMcsMapService().FindMcs(networkid, nodeid, wssSets.get(i), commonPortsUpperDegreeDir.get(j));
					for (int l = 0; l < rows.size(); l++) 
					{
//						if(dbService.getMcsMapService().CheckIdEdfaLocExists(networkid, nodeid, wssSets.get(i), rows.get(l).getMcsSwitchPort())==null)
						if(rows.get(l).getEdfaLoc()==null || rows.get(l).getEdfaLoc().equals(""))
							dbService.getMcsMapService().UpdateEdfaLoc(networkid, nodeid, wssSets.get(i), rows.get(l).getMcsSwitchPort(),edfaloc2, edfaid2);								
					}													
				}	
			}
			edfaid++;					

		}
	}
	
	public void fUpdateWssMapDoubleCardSetForEdfa(int networkid,int nodeid,int nodedegree) throws SQLException
	{
		String NodeKey = networkid+"_"+nodeid;	
				
		List <Integer> wssSets = dbService.getWssMapService().FindWssSets(networkid, nodeid); // Distinct Set Nos
		List<CardInfo> edfas = dbService.getCardInfoService().FindCardInfoByCardType(networkid, nodeid, ResourcePlanConstants.CardEdfa);
		Collections.sort(edfas,new Comparator<CardInfo>() {
			@Override
			public int compare(CardInfo a, CardInfo b){return sortBySubrack(a,b);} 
		});  
		int edfaIndex=0;
		int edfaid=1,edfaid1,edfaid2;
		System.out.println("Wss Set size:: "+wssSets.size()+" edfas size:: "+edfas.size());
		for (int i = 0; i < wssSets.size(); i++) 
		{				 
//			List<String> commonPorts = dbService.getWssMapService().FindCommonPortUsed(networkid, nodeid, wssSets.get(i));	
			List<String> commonPortsLowerDegreeDir = dbService.getWssMapService().FindCommonPortUsedLowerDegreeDir(networkid, nodeid, wssSets.get(i));
			List<String> commonPortsUpperDegreeDir = dbService.getWssMapService().FindCommonPortUsedUpperDegreeDir(networkid, nodeid, wssSets.get(i));
			
			System.out.println("NodeId:"+nodeid+" commonPortsLowerDegreeDir size::"+commonPortsLowerDegreeDir.size()
			+"  commonPortsUpperDegreeDir size::"+commonPortsUpperDegreeDir.size());
//			if(commonPorts.size()<=4)
//			{
//				//List<CardInfo> edfas = dbService.getCardInfoService().FindCardInfoByCardType(networkid, nodeid, ResourcePlanConstants.CardEdfa);
//				String edfaloc =  ""+edfas.get(edfaIndex).getRack()+"_"+edfas.get(edfaIndex).getSbrack()+"_"+edfas.get(edfaIndex).getCard();
//				edfaIndex++;
//				int edfaid=edfaIndex;
//				if(dbService.getWssMapService().CheckIdEdfaLocExists(networkid, nodeid, wssSets.get(i),1)==null)
//					dbService.getWssMapService().UpdateEdfaLoc(networkid, nodeid, wssSets.get(i), edfaloc, edfaid);				// it will update all the rows with particular mcs id	
//			}
//			else
//			{
//				//List<CardInfo> edfas = dbService.getCardInfoService().FindCardInfoByCardType(networkid, nodeid, ResourcePlanConstants.CardEdfa);
//				String edfaloc1 =  ""+edfas.get(edfaIndex).getRack()+"_"+edfas.get(edfaIndex).getSbrack()+"_"+edfas.get(edfaIndex).getCard();
//				edfaIndex++;
//				int edfaid1=edfaIndex;
//				String edfaloc2 =  ""+edfas.get(edfaIndex).getRack()+"_"+edfas.get(edfaIndex).getSbrack()+"_"+edfas.get(edfaIndex).getCard();
//				edfaIndex++;
//				int edfaid2=edfaIndex;
//				for (int j = 0; j < commonPorts.size(); j++) 
//				{
//					if(j<4)
//					{
//						List<WssMap> rows = dbService.getWssMapService().FindWss(networkid, nodeid, wssSets.get(i), commonPorts.get(j));
//						for (int l = 0; l < rows.size(); l++) 
//						{
//							int switchPort=rows.get(l).getWssLevel2SwitchPort();
//							if(dbService.getWssMapService().CheckIdEdfaLocExists(networkid, nodeid, wssSets.get(i), switchPort)==null)
//								dbService.getWssMapService().UpdateEdfaLoc(networkid, nodeid, wssSets.get(i), switchPort, edfaloc1, edfaid1);								
//						}
//					}
//					else
//					{
//						List<WssMap> rows = dbService.getWssMapService().FindWss(networkid, nodeid, wssSets.get(i), commonPorts.get(j));
//						for (int l = 0; l < rows.size(); l++) 
//						{
//							int switchPort=rows.get(l).getWssLevel2SwitchPort();
//							if(dbService.getWssMapService().CheckIdEdfaLocExists(networkid, nodeid, wssSets.get(i), switchPort)==null)
//								dbService.getWssMapService().UpdateEdfaLoc(networkid, nodeid, wssSets.get(i), switchPort,edfaloc2, edfaid2);								
//						}							
//					}						
//				}				
//
//			}
			
			if(commonPortsLowerDegreeDir.size()>0) {
				String edfaloc1 =  ""+edfas.get(edfaIndex).getRack()+"_"+edfas.get(edfaIndex).getSbrack()+"_"+edfas.get(edfaIndex).getCard();
				edfaIndex++;	
				edfaid1=edfaid;
				for (int j = 0; j < commonPortsLowerDegreeDir.size(); j++) 
				{
					List <WssMap> rows = dbService.getWssMapService().FindWss(networkid, nodeid, wssSets.get(i), commonPortsLowerDegreeDir.get(j));
					
					for (int l = 0; l < rows.size(); l++) 
					{
						int switchPort=rows.get(l).getWssLevel2SwitchPort();
//						if(dbService.getWssMapService().CheckIdEdfaLocExists(networkid, nodeid, wssSets.get(i), switchPort)==null)
						if(rows.get(l).getEdfaLoc()==null || rows.get(l).getEdfaLoc().equals(""))
							dbService.getWssMapService().UpdateEdfaLoc(networkid, nodeid, wssSets.get(i), switchPort, edfaloc1, edfaid1);								
					}
				}
			}
			edfaid++;

			if(commonPortsUpperDegreeDir.size()>0) {
				String edfaloc2 =  ""+edfas.get(edfaIndex).getRack()+"_"+edfas.get(edfaIndex).getSbrack()+"_"+edfas.get(edfaIndex).getCard();
				edfaIndex++;	
				edfaid2=edfaid;

				for (int j = 0; j < commonPortsUpperDegreeDir.size(); j++) 
				{
					List <WssMap> rows = dbService.getWssMapService().FindWss(networkid, nodeid, wssSets.get(i), commonPortsUpperDegreeDir.get(j));
					for (int l = 0; l < rows.size(); l++) 
					{
						int switchPort=rows.get(l).getWssLevel2SwitchPort();
//						if(dbService.getWssMapService().CheckIdEdfaLocExists(networkid, nodeid, wssSets.get(i), switchPort)==null)
						if(rows.get(l).getEdfaLoc()==null || rows.get(l).getEdfaLoc().equals(""))
							dbService.getWssMapService().UpdateEdfaLoc(networkid, nodeid, wssSets.get(i), switchPort,edfaloc2, edfaid2);								
					}													
				}	
			}
			edfaid++;					

		}

	}
	
	@SuppressWarnings("unchecked")
	void fRemoveUnusedWssCards(int networkidBf,int nodeid,int wssSetType) {
		if(wssSetType==ResourcePlanConstants.ONE) {
			List <CardInfo> wssAddDropCards = dbService.getCardInfoService().FindWssLevelOneCards(networkidBf, nodeid);
			wssAddDropCards.forEach(wss->{
				if(dbService.getMcsMapService().GetMaxSwitchPortId(networkidBf, nodeid, wss)==null)
					dbService.getCardInfoService().DeleteCard(networkidBf, nodeid, wss.getRack(), wss.getSbrack(), wss.getCard());
			});
			
			/** Get all edfas assigned**/
			List<CardInfo> alledfas=dbService.getCardInfoService().FindCards(networkidBf, nodeid, ResourcePlanConstants.CardEdfa, "");
			
			/** Get all edfas used in mcsmap**/
			List<String> edfasUsed=dbService.getMcsMapService().EdfaCardsUsed(networkidBf, nodeid);
			System.out.println("edfasUsed size ::: -----"+ edfasUsed.size());
			
			/** Create a list(CardInfo) of edfas used in wss map**/
//			ArrayList<CardInfo> edfaUsedCardInfoList=new ArrayList<CardInfo>();
			
			edfasUsed.forEach(edfa->{
				System.out.println("Edfa ::: -----"+ edfa);
				String []location=edfa.toString().split("_");
				int rack=Integer.parseInt(location[0]),sbrack=Integer.parseInt(location[1]),card=Integer.parseInt(location[2]);
				CardInfo edfaCard=dbService.getCardInfoService().FindCard(networkidBf, nodeid, rack, sbrack, card);
				alledfas.remove(edfaCard);
//				edfaUsedCardInfoList.add(edfaCard);
			});
			
			System.out.println("alledfas size ::: -----"+ alledfas.size());
//			System.out.println("edfaUsedCardInfoList size ::: -----"+ edfaUsedCardInfoList.size());
			
			/** Remove used from all edfas list**/
//			alledfas.removeAll(edfaUsedCardInfoList);
			System.out.println("alledfas size ::: -----"+ alledfas.size());
			
			/** Delete unused edfas**/
			alledfas.forEach(edfa->{
				if(edfa.getSbrack()!=ResourcePlanConstants.MainSbrack)
					dbService.getCardInfoService().DeleteCard(networkidBf, nodeid, edfa.getRack(), edfa.getSbrack(), edfa.getCard());
			});
		}
		else {

			List <CardInfo> wssLevel2AddDropCards = dbService.getCardInfoService().FindWssLevelTwoCards(networkidBf, nodeid);
			wssLevel2AddDropCards.forEach(wss->{
				if(dbService.getWssMapService().GetMaxSwitchPortId(networkidBf, nodeid, wss)==null)
					{
						dbService.getCardInfoService().DeleteCard(networkidBf, nodeid, wss.getRack(), wss.getSbrack(), wss.getCard());
						CardInfo levelOneCard=dbService.getCardInfoService().FindCard(networkidBf, nodeid, wss.getCardType(), ""+(Integer.parseInt(wss.getDirection())-100));
						dbService.getCardInfoService().DeleteCard(networkidBf, nodeid, levelOneCard.getRack(), levelOneCard.getSbrack(), levelOneCard.getCard());						
					}
			});
			
			/** Get all edfas assigned**/
			List<CardInfo> alledfas=dbService.getCardInfoService().FindCardInfoByCardType(networkidBf, nodeid, ResourcePlanConstants.CardEdfa);
			System.out.println("Two Level WSS :::::: alledfas size ::: -----"+ alledfas.size());
			/** Get all edfas used in wssmap**/
			List<String> edfasUsed=dbService.getWssMapService().EdfaCardsUsed(networkidBf, nodeid);
			System.out.println("Two Level WSS :::::: Used Edfa size ::: -----"+ edfasUsed.size());
			edfasUsed.forEach(edfa->{
				String []location=edfa.toString().split("_");
				int rack=Integer.parseInt(location[0]),sbrack=Integer.parseInt(location[1]),card=Integer.parseInt(location[2]);
				CardInfo edfaCard=dbService.getCardInfoService().FindCard(networkidBf, nodeid, rack, sbrack, card);
				alledfas.remove(edfaCard);
			});
			
			System.out.println("Two Level WSS :::::: All Edfa after delete ::: -----"+ alledfas.size());
			/** Delete unused edfas**/
			alledfas.forEach(edfa->{
				if(edfa.getSbrack()!=ResourcePlanConstants.MainSbrack)
					dbService.getCardInfoService().DeleteCard(networkidBf, nodeid, edfa.getRack(), edfa.getSbrack(), edfa.getCard());
			});
		}
				
	}
	
	public int sortBySubrack(CardInfo a,CardInfo b) {
		int reverse=-1;
		// all comparison
		int compareRack = a.getRack()-b.getRack();
		int compareSubRack = a.getSbrack()-b.getSbrack();
		int compareCard = a.getCard()-b.getCard();

		// 3-level comparison using if-else block
		if(compareRack == 0) {
			if(compareSubRack == 0) {
				return reverse*compareCard;
			}
			else {
				if(a.getSbrack()==2)
					return reverse*1;
				else if(b.getSbrack()==2)
					return reverse*-1;

				if(a.getSbrack()==3)
					return reverse*1;
				else if(b.getSbrack()==3)
					return reverse*-1;

				if(a.getSbrack()==1)
					return reverse*1;
				else if(b.getSbrack()==1)
					return reverse*-1;

			}
		}
		else {
			return reverse*compareRack;
		}

		return compareRack; 
	}

}

