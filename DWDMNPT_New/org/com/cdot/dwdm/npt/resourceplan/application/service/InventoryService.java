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

import application.dao.InventoryDao;
import application.dao.NodeDao;
import application.model.Circuit;
import application.model.Inventory;
import application.model.Node;

@Service
public class InventoryService{

	@Autowired
	private InventoryDao dao;
	
	public List<Inventory> FindAll()
	{
		return this.dao.findAll();
	}
	
	public Inventory FindInventory(int networkid, int nodeid, int equipmentid)
	{
		return this.dao.findInventory(networkid, nodeid, equipmentid);
	}
	
	
	public void InsertInventory(Inventory e) throws SQLException{
		this.dao.insertInventory(e);
		
	}
	
	public void UpdateInventory(int networkid, int nodeid, int equipmentid, double quantity) throws SQLException{
		this.dao.updateInventory(networkid, nodeid,equipmentid, quantity);		
	}
	
	public int Count()
	{
		return this.dao.count();
	}
	
	public int Count(int networkId)
	{
		return this.dao.count(networkId);
	}
	
	public int FindSlotRequirement(int networkid, int nodeid)
	{
		return this.dao.findSlotRequirement(networkid, nodeid);
	}
	
	public void DeleteInventory(int networkid)
	{
		this.dao.deleteByNetworkId(networkid);
	}
	
	public void DeleteInventory(int networkid,int nodeid)
	{
		this.dao.deleteByNetworkId(networkid,nodeid);
	}
}