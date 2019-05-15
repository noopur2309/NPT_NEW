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
import application.model.CardPreference;

@Service
public class CardPreferenceService{

	@Autowired
	private CardPreferenceDao dao;
	
	public List<CardPreference> FindAll()
	{
		return this.dao.findAll();
	}
	
	public List<CardPreference> FindPreference(int networkid) throws SQLException
	{
		return this.dao.findPreference(networkid);
	}
	
	public List<CardPreference> FindPreferenceByNode(int networkid, int nodeid) throws SQLException
	{
		return this.dao.findPreferenceByNode(networkid, nodeid);
	}
	
	public CardPreference findCardPreference(int networkid, int nodeid, int equipmentid)
	{
		return this.dao.findCardPreference(networkid, nodeid, equipmentid);
	}
	
	public Object FindPreferenceByFeature(int networkid, int nodeid, String cardfeature) throws SQLException 
	{
		return this.dao.findPreferenceByFeature(networkid, nodeid, cardfeature);
	}
	
	public int FindPreferenceByType(int networkid, int nodeid, String cardtype)
	{
		return this.dao.findPreferenceByType(networkid, nodeid, cardtype);
	}

	public void Insert(CardPreference info) throws SQLException{
		this.dao.insert(info);		
	}
	
	public void DeleteByCardFeature(int networkid, int nodeid, String cardfeature) 
	{
		this.dao.deleteByCardFeature(networkid, nodeid, cardfeature);		
	}
	
	public void DeleteByNetworkId(int networkid) 
	{
		this.dao.deleteByNetworkId(networkid);		
	}
	
	public int Count()
	{
		return this.dao.count();
	}
	
	public int Count(int networkId)
	{
		return this.dao.count(networkId);
	}
	
	public void InsertCardPreferenceDataInBrField(int networkid, int nodeid, int networkidBF) throws SQLException
	{
		this.dao.insertCardPreferenceDataInBrField(networkid, nodeid, networkidBF);
	}
	
	
}