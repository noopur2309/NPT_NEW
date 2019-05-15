package application.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import application.MainMap;
import application.constants.ResourcePlanConstants;
import application.model.Link;
import application.model.LinkWavelength;
import application.model.LinkWavelengthInfo;
import application.model.LinkWavelengthMap;
import application.model.NetworkRoute;
import application.service.DbService;


/**
 * 
 * @author sunaina
 * @brief generates the map of wavelengths on the links for the entire network
 *
 */
@Service
public class GenerateLinkWavelengthMap {
	
	DbService dbService; 
	ResourcePlanUtils rpu; 
	public static Logger logger = Logger.getLogger(GenerateLinkWavelengthMap.class.getName());
	
	public GenerateLinkWavelengthMap() {
		super();				
//		Logger logger = MainMap.logger.getLogger(GenerateLinkWavelengthMap.class.getName());		
	}
	
	public GenerateLinkWavelengthMap(DbService dbService) {
		super();
		this.dbService = dbService;			
//		Logger logger = MainMap.logger.getLogger(GenerateLinkWavelengthMap.class.getName());
	}

	public DbService getDbService() {
		return dbService;
	}

	public void setDbService(DbService dbService) {
		this.dbService = dbService;
	}
	
	public void fgenerateLinkWavelengthInformation(int networkid) throws SQLException
	{
		fgenerateLinkWavelengthMap(networkid);
		fpopulateLinkWavelengthInfo(networkid);		
	}
	
	public void fgenerateLinkWavelengthMap(int networkid) throws SQLException
	{		
		logger.info("fgenerateLinkWavelengthMap");
		//List<Link> links = dbService.getLinkService().FindAll();
		List<Link> links = dbService.getLinkService().FindAllwithNetworkId(networkid);
		for (int i = 0; i < links.size(); i++) 
		{
			String lnA = ""+links.get(i).getSrcNode()+","+links.get(i).getDestNode();
			String lnB = ""+links.get(i).getDestNode()+","+links.get(i).getSrcNode();
			List<NetworkRoute> routesAll = dbService.getNetworkRouteService().FindAllByNetworkId(networkid);
			String path ="";
			LinkWavelength lw = new LinkWavelength(networkid,links.get(i).getLinkId(),"");
			for (int j = 0; j < routesAll.size(); j++) 
			 {
				 path = routesAll.get(j).getPath();	
				 if((routesAll.get(j).getRoutePriority()==ResourcePlanConstants.ONE)|(routesAll.get(j).getRoutePriority()==ResourcePlanConstants.TWO))
				 {
					 if(path.contains(lnA)|path.contains(lnB))	
					 {
						 routesAll.get(j).getWavelengthNo();					
						 String str="".concat(""+routesAll.get(j).getWavelengthNo()).concat(",");	
						 lw.setWavelengths(lw.getWavelengths().concat(str));				 
					 }
				 }
			 }
			String str = lw.getWavelengths();
			 if (str != null && str.length() > 0 && str.charAt(str.length()-1)==',') 
			 {
				 lw.setWavelengths(str.substring(0, str.length()-1));//trim the last ','
				 dbService.getLinkWavelengthService().Insert(lw);	  
			 }
		}		
	}
	
	public List<LinkWavelengthMap> fsendLinkWavelengthMap(int networkid) throws SQLException
	{
		List<LinkWavelengthMap> lwm = new ArrayList<LinkWavelengthMap>();
		lwm=dbService.getLinkWavelengthService().LinkWavelength(networkid);
		for (int i = 0; i < lwm.size(); i++) 
		{			
			LinkWavelength lw  = dbService.getLinkWavelengthService().Find(networkid,lwm.get(i).getLinkId());
			if(lw!=null)
			{									
				lwm.get(i).setWavelengths(lw.getWavelengths());			
				lwm.get(i).setnWaves(lwm.get(i).getWavelengths().split(",").length);
			}
			lwm.get(i).setnMpns(dbService.getCardInfoService().CountMpn(networkid,lwm.get(i).getNodeId(),lwm.get(i).getDirection()));
		}
		
		logger.info("lwm"+lwm.toString());
		return lwm;
		
	}
	
	public void fpopulateLinkWavelengthInfo(int networkId) throws SQLException
	{
		logger.info("fpopulateLinkWavelengthInfo");
		//List<Link> links = dbService.getLinkService().FindAll();
//		List<Link> links = dbService.getLinkService().FindAllwithNetworkId(networkId);
//		
//		for (int i = 0; i < links.size(); i++) 
//		{
//			String lnA = ","+links.get(i).getSrcNode()+","+links.get(i).getDestNode()+",";
//			String lnB = ","+links.get(i).getDestNode()+","+links.get(i).getSrcNode()+",";
			List<NetworkRoute> routesAll = dbService.getNetworkRouteService().FindAllByNetworkId(networkId);
//			String path ="";
			LinkWavelengthInfo lwf = new LinkWavelengthInfo();
			lwf.setNetworkId(networkId);
			for (int j = 0; j < routesAll.size(); j++) 
			 {
				System.out.println("routesAll.get("+j+").getRoutePriority() :"+routesAll.get(j).getRoutePriority());
				if((routesAll.get(j).getRoutePriority()==ResourcePlanConstants.ONE)|(routesAll.get(j).getRoutePriority()==ResourcePlanConstants.TWO)|(routesAll.get(j).getRoutePriority()==ResourcePlanConstants.THREE)|(routesAll.get(j).getRoutePriority()==ResourcePlanConstants.FOUR))
				{
					
					 String[] linkIdSet=routesAll.get(j).getPathLinkId().split(",");
//					 path = routesAll.get(j).getPath();	
//					 path=","+path+",";
//	//				 System.out.println(".........."+path+"This is path...............");
//	//				 System.out.println("Link A...."+lnA);
//	//				 System.out.println("Link B...."+lnB);
//					 if(path.contains(lnA)|path.contains(lnB))	
//					 {
//	//					 System.out.println("Found Link...");
//						 lwf.setLinkId(links.get(i).getLinkId());
//						 lwf.setDemandId( routesAll.get(j).getDemandId());
//						 lwf.setWavelength( routesAll.get(j).getWavelengthNo());		
//						 lwf.setLineRate(routesAll.get(j).getLineRate());
//						 lwf.setTraffic(routesAll.get(j).getTraffic());
//						 lwf.setPath(routesAll.get(j).getPath());
//						 lwf.setRoutePriority(routesAll.get(j).getRoutePriority());
//						 if(routesAll.get(j).getRoutePriority()==1)
//							 lwf.setStatus(ResourcePlanConstants.Working);
//						 else if(routesAll.get(j).getRoutePriority()==2)
//							 lwf.setStatus(ResourcePlanConstants.Protection);
//						 if(lwf.getWavelength()!=0)	 
//						 dbService.getLinkWavelengthInfoService().Insert(lwf);	  
//					 }
					 
					 for (String linkId : linkIdSet) {
						 lwf.setLinkId(Integer.parseInt(linkId));
						 lwf.setDemandId( routesAll.get(j).getDemandId());
						 lwf.setWavelength( routesAll.get(j).getWavelengthNo());		
						 lwf.setLineRate(routesAll.get(j).getLineRate());
						 lwf.setTraffic(routesAll.get(j).getTraffic());
						 lwf.setPath(routesAll.get(j).getPath());
						 lwf.setRoutePriority(routesAll.get(j).getRoutePriority());
						 if(routesAll.get(j).getRoutePriority()==1)
							 lwf.setStatus(ResourcePlanConstants.Working);
						 else if(routesAll.get(j).getRoutePriority()==2)
							 lwf.setStatus(ResourcePlanConstants.Protection);
						 if(lwf.getWavelength()!=0)	 
							 System.out.println("***************************");
						 System.out.println("LinkId :"+linkId+ "routesAll.get(j) :"+routesAll.get(j).toString());
						 dbService.getLinkWavelengthInfoService().Insert(lwf);	  
					}
				}
			 }			
//		}		
	}	

}
