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

import application.dao.EquipmentDao;
import application.model.Equipment;

@Service
public class EquipmentService{

	@Autowired
	private EquipmentDao dao;
	
	public List<Equipment> FindAll()
	{
		return this.dao.findAll();
	}
	
	public Equipment FindEquipment(int partno)
	{
		return this.dao.findEquipment(partno);
	}
	
	public Equipment findEquipmentById(int eid)
	{
		return this.dao.findEquipmentById(eid);
	}
	
	public int FindSlotSize(int eid)
	{
		return this.dao.findSlotSize(eid);
	}
		
	public void InsertEquipment(Equipment e) throws SQLException{
		this.dao.insertEquipment(e);
		
	}
	
	public int Count()
	{
		return this.dao.count();
	}
	

	public void DeleteEquipment()
	{
		this.dao.deleteByNetworkId();
	}	
}