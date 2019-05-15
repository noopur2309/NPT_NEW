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
import application.dao.EquipmentPreferenceDao;
import application.dao.StockDao;
import application.model.CardPreference;
import application.model.EquipmentPreference;
import application.model.Stock;

@Service
public class StockService{

	@Autowired
	private StockDao dao;
	
	public void Insert(Stock info) throws SQLException{
		this.dao.insert(info);		
	}
	
	public Stock Find(int networkid, int nodeid, String category, String cardtype) 
	{
		return this.dao.find(networkid, nodeid, category, cardtype);	
	}
	
	public Stock Find(int networkid, int nodeid, String category, String cardtype, String SrNo) 
	{
		return this.dao.find(networkid, nodeid, category, cardtype, SrNo);	
	}
	
	public List<Stock> Find(int networkid) 
	{
		return this.dao.find(networkid);	
	}
	
	public Stock FindUnUsedSrNoStock(int networkid, int nodeid, String category, String cardtype) 
	{
		return this.dao.findUnUsedSrNoStock(networkid, nodeid, category, cardtype);	
	}
	
	public Stock FindUnUsedStockFreeFromParamPref(int networkid, int nodeid, String category, String cardtype) 
	{
		return this.dao.findUnUsedStockFreeFromParamPref(networkid, nodeid, category, cardtype);	
	}
	
	public Stock FindUnUsedStockWithoutSrNo(int networkid, int nodeid, String category, String cardtype) 
	{
		return this.dao.findUnUsedStockWithoutSrNo(networkid, nodeid, category, cardtype);	
	}
	
	public String FindPreferredExistingStock(int networkid, int nodeid, String category) 
	{
		return this.dao.findPreferredExistingStock(networkid, nodeid, category);	
	}
	
	public void UpdateQuantity(Stock info) throws SQLException 
	{
		this.dao.updateQuantity(info);	
	}
	
	public void UpdateUsedQuantity(Stock info) throws SQLException 
	{
		this.dao.updateUsedQuantity(info);	
	}
	
	public void UpdateStatus(Stock info) throws SQLException 
	{
		this.dao.updateStatus(info);	
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