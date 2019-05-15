/**
 * 
 */
/**
 * @author sunaina
 *
 */
package application.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.dao.ViewDao;
import application.model.DemandMapping;
import application.model.Link;
import application.model.RouteMapping;

@Service
public class ViewService {

	@Autowired
	private ViewDao viewdao;
	
	public List<Map<String, Object>> FindRouteMapping1()
	{
		return this.viewdao.findRouteMapping1();
	}
	
	public List<RouteMapping> FindChannelProtectionRoutes(int networkId,int nodeId)
	{
		return this.viewdao.findChannelProtectionRoutes(networkId, nodeId);
	}
	
	public List<RouteMapping> FindClientProtectionRoutes(int networkId,int nodeId)
	{
		return this.viewdao.findClientProtectionRoutes(networkId, nodeId);
	}
	
	public List<RouteMapping> FindRouteMappingCommonBrField(int networkId,int networkIdBf,int nodeId)
	{
		return this.viewdao.findRouteMappingCommonBrField(networkId,networkIdBf, nodeId);
	}
	
	public List<RouteMapping> FindRouteMappingAddedBrField(int networkId,int networkIdBf,int nodeId)
	{
		return this.viewdao.findRouteMappingAddedBrField(networkId,networkIdBf, nodeId);
	}
	
	public List<RouteMapping> FindRouteMappingDeletedBrField(int networkId,int networkIdBf,int nodeId)
	{
		return this.viewdao.findRouteMappingDeletedBrField(networkId,networkIdBf, nodeId);
	}
	
//	public List<RouteMapping> FindRouteMappingModifiedBrField(int networkId,int networkIdBf,int nodeId)
//	{
//		return this.viewdao.findRouteMappingModifiedBrField(networkId,networkIdBf, nodeId);
//	}
	
	public List<RouteMapping> FindRouteMapping(int networkId)
	{
		List<RouteMapping> mapping = this.viewdao.findRouteMapping(networkId);
		for (int i = 0; i < mapping.size(); i++) {
			String nodes[]=mapping.get(i).getPath().split(",");
			String links[]=mapping.get(i).getPathLinkId().split(",");
			int Cost=0;
			float Cd=0,Len=0;
			double Pmd=0;
			String currentLineRate = mapping.get(i).getLineRate();
			///System.out.println("currentLineRate : "+currentLineRate + " for "+ i);
			for (int j = 0; j < nodes.length-1; j++) {
				Link l=viewdao.findLink(Integer.parseInt(nodes[j].toString()),Integer.parseInt(nodes[j+1].toString()),networkId, Integer.parseInt(links[j]));
				
//				Len=Len+viewdao.findLinkLen(Integer.parseInt(nodes[j].toString()),Integer.parseInt(nodes[j+1].toString()),networkId);	
//				Cost=Cost+viewdao.findLinkCost(Integer.parseInt(nodes[j].toString()),Integer.parseInt(nodes[j+1].toString()),networkId);
				Len=Len+l.getLength();	
				Cost=Cost+l.getMetricCost();
				
				if(currentLineRate.equalsIgnoreCase("10")){
					///System.out.println(" Here "+ 10);
//					Cd=viewdao.findLinkCd(Integer.parseInt(nodes[j].toString()),Integer.parseInt(nodes[j+1].toString()),networkId);
					Cd=l.getCd();
				}
				else{
					///System.out.println(" Here "+ 100);
//					Cd=Cd+viewdao.findLinkCd(Integer.parseInt(nodes[j].toString()),Integer.parseInt(nodes[j+1].toString()),networkId);
					Cd=Cd+l.getCd();
				}
				
//				Pmd=Pmd+Math.pow(viewdao.findLinkPmd(Integer.parseInt(nodes[j].toString()),Integer.parseInt(nodes[j+1].toString()),networkId), 2);
				Pmd=Pmd+Math.pow(l.getPmd(), 2);
			}			
			mapping.get(i).setRouteLength(Len);			
			mapping.get(i).setRouteCost(Cost);		
			mapping.get(i).setRouteCd(Cd);		
			mapping.get(i).setRoutePmd(Math.sqrt(Pmd));		
		}
		return mapping;
	}
		
	public List<DemandMapping> FindDemandMapping(int networkId)
	{
		List<DemandMapping> mapping = this.viewdao.findDemandMapping(networkId);		
		return mapping;
	}
	
	/*
	 * 
	 * Used to create the card info table in db
	 * Table name will be CardInfo_NetworkId_NodeId
	 * 
	 * * */
	public void CreateCardInfoTable(String nodekey)
	{
		this.viewdao.createCardInfoTable(nodekey);
	}

	
}