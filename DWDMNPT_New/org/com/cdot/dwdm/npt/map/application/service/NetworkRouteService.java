/**
 * 
 */
/**
 * @author sunaina
 *
 */
package application.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.dao.NetworkRouteDao;
import application.dao.ViewDao;
import application.model.Circuit;
import application.model.NetworkRoute;
import application.model.RouteMapping;

@Service
public class NetworkRouteService{

	@Autowired
	private NetworkRouteDao networkRouteDao;
	
	@Autowired
	private ViewDao viewdao;
	
	public List<NetworkRoute> FindAll()
	{
		return this.networkRouteDao.findAll();
	}
	
	public List<Map<String, Object>> FindAllNetworkRoutes()
	{
		return this.networkRouteDao.findAllNetworkRoutes();
	}

	public void InsertNetworkRoute(NetworkRoute networkRoute) throws SQLException{
		this.networkRouteDao.insertNetworkRoute(networkRoute);
		
	}
	
	public void DeleteNetworkRoute(int networkid)
	{
		this.networkRouteDao.deleteByNetworkId(networkid);
	}
	
	public void DeleteNetworkRoute(int networkid,int demandid)
	{
		this.networkRouteDao.deleteByNetworkId(networkid,demandid);
	}
	
	public List<NetworkRoute> FindAllByNetworkId(int networkid)
	{
		return this.networkRouteDao.findAllByNetworkId(networkid);
	}
	
	public List<NetworkRoute> FindAllByDemandId(int networkid, int demandid)
	{
		return this.networkRouteDao.findAllByDemandId(networkid,demandid);
	}
	
	public List<NetworkRoute> FindAllByDemandIdUsable(int networkid, int demandid)
	{
		return this.networkRouteDao.findAllByDemandIdUsable(networkid,demandid);
	}
	
	public List<NetworkRoute> FindAllErrorPaths(int networkid)
	{
		return this.networkRouteDao.findAllErrorPaths(networkid);
	}
	
	
	public NetworkRoute FindAllByDemandId(int networkid, int demandid, int routePriority)
	{
		return this.networkRouteDao.findAllByDemandId(networkid, demandid, routePriority);
	}
	
	public List<Map<String, Object>> FindRoutesWithNodeEnds_cf(int networkid, int nodeid)
	{
		return this.networkRouteDao.findRoutesWithNodeEnds_cf(networkid, nodeid);
	}
	
	public int Count()
	{
		return this.networkRouteDao.count();
	}
	
	public int Count(int networkId)
	{
		return this.networkRouteDao.count(networkId);
	}
	
	public List <NetworkRoute> IsNodeOn10GRoute(int networkid, int nodeid)
	{
		return this.networkRouteDao.IsNodeOn10GRoute(networkid, nodeid);
	}
	
	public List <NetworkRoute> FindAddedRouteInBrField(int networkidGrField,int networkidBrField)
	{
		return this.networkRouteDao.findAddedRouteInBrField(networkidGrField, networkidBrField);
	}
	
	public List <NetworkRoute> FindDeletedRouteInBrField(int networkidGrField,int networkidBrField)
	{
		return this.networkRouteDao.findDeletedRouteInBrField(networkidGrField, networkidBrField);
	}
	
	public List <RouteMapping> FindAddedRouteDemMappedInBrField(int networkidGrField,int networkidBrField)
	{
		List<RouteMapping> mapping = this.networkRouteDao.findAddedRouteDemMappedInBrField(networkidGrField, networkidBrField);
		
		for (int i = 0; i < mapping.size(); i++) {
			String nodes[]=mapping.get(i).getPath().split(",");
			int Len=0,Cost=0;
			float Cd=0;
			double Pmd=0;
			String currentLineRate = mapping.get(i).getLineRate();			
			for (int j = 0; j < nodes.length-1; j++) {
				Len=Len+viewdao.findLinkLen(Integer.parseInt(nodes[j].toString()),Integer.parseInt(nodes[j+1].toString()),networkidBrField);	
				Cost=Cost+viewdao.findLinkCost(Integer.parseInt(nodes[j].toString()),Integer.parseInt(nodes[j+1].toString()),networkidBrField);
				
				if(currentLineRate.equalsIgnoreCase("10")){
					///System.out.println(" Here "+ 10);
					Cd=viewdao.findLinkCd(Integer.parseInt(nodes[j].toString()),Integer.parseInt(nodes[j+1].toString()),networkidBrField);
				}
				else{
					///System.out.println(" Here "+ 100);
					Cd=Cd+viewdao.findLinkCd(Integer.parseInt(nodes[j].toString()),Integer.parseInt(nodes[j+1].toString()),networkidBrField);
				}
				
				Pmd=Pmd+Math.pow(viewdao.findLinkPmd(Integer.parseInt(nodes[j].toString()),Integer.parseInt(nodes[j+1].toString()),networkidBrField), 2);
			}			
			mapping.get(i).setRouteLength(Len);			
			mapping.get(i).setRouteCost(Cost);		
			mapping.get(i).setRouteCd(Cd);		
			mapping.get(i).setRoutePmd(Math.sqrt(Pmd));		
		}
		return mapping;
	}
	
	public List <RouteMapping> FindDeletedRouteDemMappedInBrField(int networkidGrField,int networkidBrField)
	{
		List<RouteMapping> mapping = this.networkRouteDao.findDeletedRouteDemMappedInBrField(networkidGrField, networkidBrField);
		
		for (int i = 0; i < mapping.size(); i++) {
			String nodes[]=mapping.get(i).getPath().split(",");
			int Len=0,Cost=0;
			float Cd=0;
			double Pmd=0;
			String currentLineRate = mapping.get(i).getLineRate();			
			for (int j = 0; j < nodes.length-1; j++) {
				Len=Len+viewdao.findLinkLen(Integer.parseInt(nodes[j].toString()),Integer.parseInt(nodes[j+1].toString()),networkidBrField);	
				Cost=Cost+viewdao.findLinkCost(Integer.parseInt(nodes[j].toString()),Integer.parseInt(nodes[j+1].toString()),networkidBrField);
				
				if(currentLineRate.equalsIgnoreCase("10")){
					///System.out.println(" Here "+ 10);
					Cd=viewdao.findLinkCd(Integer.parseInt(nodes[j].toString()),Integer.parseInt(nodes[j+1].toString()),networkidBrField);
				}
				else{
					///System.out.println(" Here "+ 100);
					Cd=Cd+viewdao.findLinkCd(Integer.parseInt(nodes[j].toString()),Integer.parseInt(nodes[j+1].toString()),networkidBrField);
				}
				
				Pmd=Pmd+Math.pow(viewdao.findLinkPmd(Integer.parseInt(nodes[j].toString()),Integer.parseInt(nodes[j+1].toString()),networkidBrField), 2);
			}			
			mapping.get(i).setRouteLength(Len);			
			mapping.get(i).setRouteCost(Cost);		
			mapping.get(i).setRouteCd(Cd);		
			mapping.get(i).setRoutePmd(Math.sqrt(Pmd));		
		}
		return mapping;
	}
	
	public List <RouteMapping> FindCommonRouteDemMappedInBrField(int networkidGrField,int networkidBrField)
	{		
		List<RouteMapping> mapping = this.networkRouteDao.findCommonRouteDemMappedInBrField(networkidGrField, networkidBrField);
		
		for (int i = 0; i < mapping.size(); i++) {
			String nodes[]=mapping.get(i).getPath().split(",");
			int Len=0,Cost=0;
			float Cd=0;
			double Pmd=0;
			String currentLineRate = mapping.get(i).getLineRate();			
			for (int j = 0; j < nodes.length-1; j++) {
				Len=Len+viewdao.findLinkLen(Integer.parseInt(nodes[j].toString()),Integer.parseInt(nodes[j+1].toString()),networkidBrField);	
				Cost=Cost+viewdao.findLinkCost(Integer.parseInt(nodes[j].toString()),Integer.parseInt(nodes[j+1].toString()),networkidBrField);
				
				if(currentLineRate.equalsIgnoreCase("10")){
					///System.out.println(" Here "+ 10);
					Cd=viewdao.findLinkCd(Integer.parseInt(nodes[j].toString()),Integer.parseInt(nodes[j+1].toString()),networkidBrField);
				}
				else{
					///System.out.println(" Here "+ 100);
					Cd=Cd+viewdao.findLinkCd(Integer.parseInt(nodes[j].toString()),Integer.parseInt(nodes[j+1].toString()),networkidBrField);
				}
				
				Pmd=Pmd+Math.pow(viewdao.findLinkPmd(Integer.parseInt(nodes[j].toString()),Integer.parseInt(nodes[j+1].toString()),networkidBrField), 2);
			}			
			mapping.get(i).setRouteLength(Len);			
			mapping.get(i).setRouteCost(Cost);		
			mapping.get(i).setRouteCd(Cd);		
			mapping.get(i).setRoutePmd(Math.sqrt(Pmd));		
		}
		return mapping;
	}
	
	public List <NetworkRoute> FindCommonRouteInBrField(int networkidGrField,int networkidBrField)
	{
		return this.networkRouteDao.findCommonRouteInBrField(networkidGrField, networkidBrField);
	}
	
	public List <NetworkRoute> FindModifiedRouteInBrField(int networkidGrField,int networkidBrField)
	{
		return this.networkRouteDao.findModifiedRouteInBrField(networkidGrField, networkidBrField);
	}
	
	public List <NetworkRoute> FindPathModifiedRouteInBrField(int networkidGrField,int networkidBrField)
	{
		return this.networkRouteDao.findPathModifiedRouteInBrField(networkidGrField, networkidBrField);
	}
	
	public List <RouteMapping> FindModifiedRouteDemMappedInBrField(int networkidGrField,int networkidBrField)
	{
		List<RouteMapping> mapping = this.networkRouteDao.findModifiedRouteDemMappedInBrField(networkidGrField, networkidBrField);
		
		for (int i = 0; i < mapping.size(); i++) {
			String nodes[]=mapping.get(i).getPath().split(",");
			int Len=0,Cost=0;
			float Cd=0;
			double Pmd=0;
			String currentLineRate = mapping.get(i).getLineRate();
			///System.out.println("currentLineRate : "+currentLineRate + " for "+ i);
			for (int j = 0; j < nodes.length-1; j++) {
				Len=Len+viewdao.findLinkLen(Integer.parseInt(nodes[j].toString()),Integer.parseInt(nodes[j+1].toString()),networkidBrField);	
				Cost=Cost+viewdao.findLinkCost(Integer.parseInt(nodes[j].toString()),Integer.parseInt(nodes[j+1].toString()),networkidBrField);
				
				if(currentLineRate.equalsIgnoreCase("10")){
					///System.out.println(" Here "+ 10);
					Cd=viewdao.findLinkCd(Integer.parseInt(nodes[j].toString()),Integer.parseInt(nodes[j+1].toString()),networkidBrField);
				}
				else{
					///System.out.println(" Here "+ 100);
					Cd=Cd+viewdao.findLinkCd(Integer.parseInt(nodes[j].toString()),Integer.parseInt(nodes[j+1].toString()),networkidBrField);
				}
				
				Pmd=Pmd+Math.pow(viewdao.findLinkPmd(Integer.parseInt(nodes[j].toString()),Integer.parseInt(nodes[j+1].toString()),networkidBrField), 2);
			}			
			mapping.get(i).setRouteLength(Len);			
			mapping.get(i).setRouteCost(Cost);		
			mapping.get(i).setRouteCd(Cd);		
			mapping.get(i).setRoutePmd(Math.sqrt(Pmd));		
		}
		return mapping;
		
	}	
	
	/**
	 * To Update the Traffic from the matching demand in case of Brownfield 
	 */
	public void UpdateCommonRouteDemMappedInBrField(NetworkRoute networkRouteObj)
	{
		this.networkRouteDao.updateCommonRouteDemMappedInBrField(networkRouteObj);
	}
	
	public void UpdateCommonRouteDemMappedInBrFieldDirect(NetworkRoute networkRouteObj)
	{
		this.networkRouteDao.updateCommonRouteDemMappedInBrFieldDirect(networkRouteObj);
	}
	
	/**
	 * @desc   To retrieve the service wise list of OTN LSP
	 * @date   6th Feb,2018
	 * @author hp
	 */
	public List<Map<String, Object>> FindRoutesWithServiceBasedNodeEnds_cf(int networkid, int nodeid)
	{
		return this.networkRouteDao.findRoutesWithServiceBasedNodeEnds_cf(networkid, nodeid);
	}
	
	public List<Map<String, Object>> FindRoutesWithServiceBasedNodeEnds_PureNon10gAgg(int networkid, int nodeid)
	{
		return this.networkRouteDao.findRoutesWithServiceBasedNodeEnds_PureNon10gAgg(networkid, nodeid);
	}

	public List<Map<String, Object>> FindRoutesWithServiceBasedNodeEnds_Pure10gAgg(int networkid, int nodeid)
	{
		return this.networkRouteDao.findRoutesWithServiceBasedNodeEnds_Pure10gAgg(networkid, nodeid);
	}
	
	public void CopyNetworkRouteDataInBrField(int networkidGrField, int networkidBrField) throws SQLException
	{
		this.networkRouteDao.copyNetworkRouteDataInBrField(networkidGrField, networkidBrField);
	}

	public List<Integer> FindDistinctLambda(int networkid, int routePriority) throws SQLException
	{
		return this.networkRouteDao.findDistinctLambda(networkid,routePriority);
	}
	
	/**
	 * To Update the View
	 */
	public void UpdateRouteMappingView()
	{
		this.networkRouteDao.updateRouteMappingView();
	}
		
	/**
	 * To Update the View
	 */
	public void UpdateLinkSet(int networkId,int rp,String path,int wav,int demandid,String linkSet)
	{
		this.networkRouteDao.updateLinkSet(networkId,rp,path,wav,demandid,linkSet);
	}
	
	public void UpdateTrafficForDemand(int networkId, int DemandId, float Traffic) {
		
		this .networkRouteDao.updateTrafficForDemand(networkId, DemandId, Traffic);	
	}
	
}