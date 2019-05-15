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

import application.dao.OpticalDataDao;
import application.model.OpticalData;

@Service
public class OpticalDataService{

	@Autowired
	private OpticalDataDao dao;
	
	public List<OpticalData> FindAll()
	{
		return this.dao.findAll();
	}
	
	public OpticalData FindOpticalData(int equipmentid)
	{
		return this.dao.findOpticalData(equipmentid);
	}		

	public void Insert(OpticalData info) throws SQLException{
		this.dao.insert(info);		
	}
	
	public int Count()
	{
		return this.dao.count();
	}	
	
	public void DeleteByEquipmentId(int equipmentId)
	{
		this.dao.deleteByEquipmentId(equipmentId);
	}	
}