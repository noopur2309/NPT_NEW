/**
 * 
 */
/**
 * @author sunaina
 *
 */
package application.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.dao.DemandDao;
import application.dao.NetworkDao;
import application.dao.CardInfoDao;
import application.model.Circuit;
import application.model.Demand;
import application.model.Network;
import application.model.CardInfo;

@Service
public class DemandService{

	@Autowired
	private DemandDao demanddao;
	
	public List<Demand> FindAll()
	{
		return this.demanddao.findAll();	
	}
	
	public List<Demand> FindAllByNetworkId(int networkid)
	{
		return this.demanddao.findAllByNetworkId(networkid);	
	}
	
	public List<Demand> FindAllByNodeId(int networkid, int nodeid)
	{
		return this.demanddao.findAllByNodeId(networkid, nodeid);	
	}
	
	public List<Map<String, Object>> FindAllDemands()
	{
		return this.demanddao.findAllDemands();
	}

	public int InsertDemand(Demand demand, HashMap<String, Object> networkInfoMap) throws SQLException{
		return this.demanddao.insertDemand(demand, networkInfoMap);		 
	}
	
	public void DeleteDemand(int networkid)
	{
		this.demanddao.deleteByNetworkId(networkid);
	}
	
	public void DeleteDemand(int networkid, int demandid)
	{
		this.demanddao.deleteDemand(networkid, demandid);
	}
	
	public int Count()
	{
		return this.demanddao.count();
	}
	
	public int Count(int newtworkId)
	{
		return this.demanddao.count(newtworkId);
	}
	
	public int Count(int networkid, int nodeid)
	{
		return this.demanddao.count(networkid, nodeid);
	}
	
//	public Demand FindDemand(Circuit c)
//	{
//		return this.demanddao.FindDemand(c);
//	}
	
	public Demand FindDemand(int networkid, int demandid)
	{
		return this.demanddao.FindDemand(networkid,demandid);
	}
	public Demand FindDemand(int networkid, int src, int dest, String protType, 
			String clientProtType, String colorPref, String pathType, int lineRate, String channelProtection,String clientProtection )
	{
		return this.demanddao.FindDemand(networkid,  src,  dest,  protType, 
				 clientProtType,  colorPref,  pathType,  lineRate,  channelProtection , clientProtection  );
	}
	
	public int GetTotalCapacityForNetwork(int networkid)
	{
		return this.demanddao.getTotalCapacityForNetwork(networkid);
	}

	public List<Demand> FindAddedDemandsInBrField(int networkidGrField, int networkidBrField)
	{
		return this.demanddao.findAddedDemandsInBrField(networkidGrField, networkidBrField);	
	}
		
	public List<Demand> FindAddedDemandsInBrField(int networkidGrField, int networkidBrField, int nodeid)
	{
		return this.demanddao.findAddedDemandsInBrField(networkidGrField, networkidBrField, nodeid);	
	}
	
	public List<Demand> FindModifiedCircuitSetDemandsInBrField(int networkidGrField, int networkidBrField, int nodeid)
	{
		return this.demanddao.findModifiedCircuitSetDemandsInBrField(networkidGrField, networkidBrField, nodeid);	
	}
	
	public List<Demand> FindDeletedDemandsInBrField(int networkidGrField, int networkidBrField, int nodeid)
	{
		return this.demanddao.findDeletedDemandsInBrField(networkidGrField, networkidBrField, nodeid);	
	}
	

	
	/**
	 * @brief  This Section added to support InMemory Demand tables for RWA Newly added Demand Input & Other APIs
	 * @author hp
	 * @date   4th Oct, 2017
	 */
	
	public void InsertInMemoryDemand(Demand demand) throws SQLException{
		this.demanddao.insertInMemoryDemand(demand);		 
	}
	
	public List<Demand> FindAllInMemoryDemandByNetworkId(int networkid)
	{
		return this.demanddao.findAllInMemoryDemandByNetworkId(networkid);	
	}
	
	public void InsertCommonGreenFieldDemand(Demand demand) throws SQLException{
		this.demanddao.insertCommonGreenFieldDemand(demand);		 
	}
	
	public void DeleteInMemoryDemand(int networkid)
	{
		this.demanddao.deleteInMemoryDemandTableByNetworkId(networkid);
	}	

	public void UpdateDemand(Demand d){
		this.demanddao.updateDemand(d);
	}
	
	public void UpdateDemandForCircuitSet(Demand d){
		this.demanddao.updateDemandForCircuitSet(d);
	}
	
	public void UpdateCircuitSet(int networkid, int demandid, String circuitSet){
		this.demanddao.updateCircuitSet(networkid, demandid, circuitSet);
	}
	
	

	public Map<String, Object> FindDemandsFor10GTpn(int networkid, int nodeid)
	{
		return this.demanddao.findDemandsFor10GTpn(networkid, nodeid);
	}
	
	public Map<String, Object> FindDemandsFor100GTpn(int networkid, int nodeid)
	{
		return this.demanddao.findDemandsFor100GTpn(networkid, nodeid);
	}
	
	public void CopyDemandDataInBrField(int networkidGrField, int networkidBrField) throws SQLException
	{
		this.demanddao.copyDemandDataInBrField(networkidGrField, networkidBrField);
	}
	
	public List<Demand> CheckDemandForAddDropSetCount(int networkId,int demandId) throws SQLException{
		return this.demanddao.checkDemandForAddDropCardSetCount(networkId, demandId);
	}
	

}