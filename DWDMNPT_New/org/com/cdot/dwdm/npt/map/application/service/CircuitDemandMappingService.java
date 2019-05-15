/**
 * @author sunaina
 *
 */
package application.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.dao.CircuitDemandMappingDao;
import application.model.CircuitDemandMapping;

@Service
public class CircuitDemandMappingService{

	@Autowired
	private CircuitDemandMappingDao circuitDemandMappingDao;
	
	public List<CircuitDemandMapping> FindAll()
	{
		return this.circuitDemandMappingDao.findAll();
	}

	public void InsertCircuitDemandMapping(CircuitDemandMapping circuitDemandMapping) throws SQLException{
		this.circuitDemandMappingDao.insertCircuitDemandMapping(circuitDemandMapping);
		
	}
	
	public void DeleteCircuitDemandMapping(int networkid)
	{
		this.circuitDemandMappingDao.deleteByNetworkId(networkid);
	}
	
	public void DeleteCircuitDemandMapping(int networkid, int circuitid)
	{
		this.circuitDemandMappingDao.deleteCircuitDemandMapping(networkid, circuitid);
	}
	
	public int Count()
	{
		return this.circuitDemandMappingDao.count();
	}
	
	public int Count(int networkId)
	{
		return this.circuitDemandMappingDao.count(networkId);
	}
	
	public CircuitDemandMapping FindDemand(int networkid, int circuitid)
	{
		return this.circuitDemandMappingDao.findDemand(networkid, circuitid);
	}
	
	public List<CircuitDemandMapping> FindCircuits(int networkid, int demandId)
	{
		return this.circuitDemandMappingDao.findCircuits(networkid, demandId);
	}

	public int FindDemandCount(int networkid, int demandId)
	{
		return this.circuitDemandMappingDao.findDemandCount(networkid, demandId);
	}	

	public void CopyCircuitDemandMappingDataInBrField(int networkidGrField, int networkidBrField) throws SQLException
	{
		this.circuitDemandMappingDao.copyCircuitDemandMappingDataInBrField(networkidGrField, networkidBrField);
	}
	
}