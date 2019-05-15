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

import application.dao.CardPreferenceDao;
import application.dao.EquipmentPreferenceDao;
import application.model.CardPreference;
import application.model.EquipmentPreference;

@Service
public class EquipmentPreferenceService{

	@Autowired
	private EquipmentPreferenceDao dao;
	
	public void Insert(EquipmentPreference info) throws SQLException{
		this.dao.insert(info);		
	}
	
	public List<EquipmentPreference> FindAll(int networkid) 
	{
		return this.dao.findAll(networkid);	
	}
	
	public EquipmentPreference FindPreferredEq(int networkid, int nodeid, String category) 
	{
		return this.dao.findPreferredEq(networkid, nodeid, category);	
	}
	
	public String FindRedundancy(int networkid, int nodeid, String category) 
	{
		return this.dao.findRedundancy(networkid, nodeid, category);	
	}
		
	public void DeleteByNetworkId(int networkid) 
	{
		this.dao.deleteByNetworkId(networkid);		
	}
	
	public void DeleteByNetworkId(int networkid,int nodeid) 
	{
		this.dao.deleteByNetworkId(networkid,nodeid);		
	}
	
	public void DeleteNodeCategoryPref(int networkid,int nodeid,String category) 
	{
		this.dao.deleteNodeCategoryPref(networkid,nodeid,category);		
	}
	
	public void DeleteNodePref(int networkid,int nodeid) 
	{
		this.dao.deleteNodePref(networkid,nodeid);		
	}
	
	public int Count(int networkid)
	{
		return this.dao.count(networkid);
	}
	
	public List<Map<String, Object>> InitializedNodes(int networkid)
	{
		return this.dao.initializedNodes(networkid);
	}
	
	public void InitializeEqPreferencesBrField(int gfNetworkId,int bfNetworkId) throws SQLException{
		this.dao.initializeEqPreferencesBrField(gfNetworkId,bfNetworkId);		
	}
	

	
	
}