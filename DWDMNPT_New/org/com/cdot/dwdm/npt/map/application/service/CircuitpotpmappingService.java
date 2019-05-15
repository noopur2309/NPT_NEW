package application.service;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.dao.Circuitpotpmappingdao;
import application.model.Circuit10gAgg;
import application.model.Circuitpotpmapping;

@Service
public class CircuitpotpmappingService {
	
	

	@Autowired
	private Circuitpotpmappingdao circuitPOTPDao;	
	
	

	public void InsertCircuitPOTP(Circuitpotpmapping circuitPOTP, HashMap<String,Object> NetworkInfoMap) throws SQLException{
		this.circuitPOTPDao.insertCircuitPOTP(circuitPOTP, NetworkInfoMap);		
	}
	
	
	
	public int  GetNextPotpCircuitIdForNetwork(int NetworkId) throws SQLException{
		return this.circuitPOTPDao. getNextPotpCircuitIdForNetwork(NetworkId);		
	}
	
	public List<Map<String,Object>> FindGroupedPotpcircuits(int networkid,String CircuitpotpType)
	
	{
		return this.circuitPOTPDao.findGroupedPotpcircuits(networkid, CircuitpotpType);
		
	}
	
	
	

}
