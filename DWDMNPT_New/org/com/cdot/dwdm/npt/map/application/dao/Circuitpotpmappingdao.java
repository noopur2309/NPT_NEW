package application.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import application.MainMap;
import application.constants.MapConstants;
import application.constants.ResourcePlanConstants;
import application.model.Circuitpotpmapping;
import application.model.DemandMapping;

@Component
public class Circuitpotpmappingdao{
	
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	public static Logger logger = Logger.getLogger(ResourcePlanConstants.DatabasePrintStr);
	
	
	public void insertCircuitPOTP(Circuitpotpmapping circuitPOTP, HashMap<String,Object> NetworkInfoMap) throws SQLException
  {
			
              
			 int id = 0;
			 int potpCircuitId;
			 
			 id = getNextIndexForNetwork(circuitPOTP.getNetworkId());
					 
			 logger.debug("insertCircuitPOTPId  id :  "+ id);
		    
			 String sql = "INSERT into Circuitpotpmapping(RowNo, NetworkId, potpCircuitId, CircuitId, CircuitpotpType, TributoryId)  VALUES (?,?,?,?,?,?)";
		     jdbcTemplate.update(
		             sql,
					 new Object[] { id,circuitPOTP.getNetworkId(), circuitPOTP.getpotpcircuitId(),circuitPOTP.getCircuitId(),circuitPOTP.getCircuitpotpType(),circuitPOTP.getTributoryId()});
		     
		     ///return id;
  }
	  
			
		
			
	
	public int getNextIndexForNetwork(int networkid) {
		System.out.println("getNextIndexIdForNetwork");
		
		String sql = "SELECT MAX(RowNo)+1 FROM Circuitpotpmapping where NetworkId = ?";
		System.out.println(sql);
		try
		{    System.out.println("here"+jdbcTemplate.queryForObject(sql, Integer.class, networkid));
			if(jdbcTemplate.queryForObject(sql, Integer.class, networkid)!= null)
				{
					return Integer.parseInt(jdbcTemplate.queryForObject(sql, Integer.class, networkid).toString());
				}
			else{
				return 1;
			}
		}
		catch(EmptyResultDataAccessException e) {
			return 1;
		}		
		
	}
	
	public int getNextPotpCircuitIdForNetwork(int networkid) {
		logger.debug("getNextPOTPCircuitIdForNetwork");
		String sql = "SELECT MAX(potpcircuitId)+1 FROM Circuitpotpmapping where NetworkId = ? ";
		
		try
		{   
			if(jdbcTemplate.queryForObject(sql, Integer.class, networkid)!= null)
				{
					return Integer.parseInt(jdbcTemplate.queryForObject(sql, Integer.class, networkid).toString());
				}
			
			else
			
			{
				return 1;
			}
		}
		
		catch(EmptyResultDataAccessException e)
		{
			return 1;
		}		
		
	}
	
	
	public List<Map<String,Object>> findGroupedPotpcircuits(int networkid,String CircuitpotpType) {
		logger.debug("for Grouped POTPCircuits");
		String sql = "select potpcircuitid,Group_concat(CircuitId Separator ',') AS CircuitSet from Circuitpotpmapping where Networkid = ? and CircuitpotpType = ? group by potpcircuitid; ";
		 List<Map<String,Object>> result= new ArrayList<Map<String,Object>>();
		try
		{   
		    result = jdbcTemplate.queryForList(sql,networkid,CircuitpotpType);
				       }
		
		catch(EmptyResultDataAccessException e)
		{
			return null;
		}		
		
		
		 return result;
	}
	
	
	
	}
	
	


