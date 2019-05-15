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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.dao.CardPreferenceDao;
import application.dao.ParametricPreferenceDao;
import application.model.CardPreference;
import application.model.ParametricPreference;

@Service
public class ParametricPreferenceService{

	@Autowired
	private ParametricPreferenceDao dao;
	
	public ParametricPreference Find(int networkid, int nodeid, String category, String param, String value ) throws SQLException
	{
		return this.dao.find(networkid, nodeid, category, param, value);

	}	
	
	public List<ParametricPreference> FindAll(int networkid) throws SQLException
	{
		return this.dao.findAll(networkid);
	}	

	
	public ParametricPreference CheckPrefereceBySrNo(int networkid, int nodeid, String category, String type, String srno ) throws SQLException
	{
		return this.dao.checkPrefereceBySrNo(networkid, nodeid, category, type, srno);
	}


	public void Insert(ParametricPreference info) throws SQLException{
		this.dao.insert(info);		
	}	
	
	
	public void DeleteByNetworkId(int networkid) 
	{
		this.dao.deleteByNetworkId(networkid);		
	}	
	
	public int Count(int networkId)
	{
		return this.dao.count(networkId);
	}
	
	public void InitializeParamPrefbrField(int networkId,int networkidBf) throws SQLException
	{
		this.dao.initializeParamPrefbrField(networkId,networkidBf);
	}
	
	
	
	
}