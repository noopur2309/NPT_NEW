package application.dao;

import java.awt.image.ImagingOpException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import application.MainMap;
import application.constants.ResourcePlanConstants;
import application.model.Circuit;
import application.model.CircuitDemandMapping;
import application.model.Demand;

@Component
public class CircuitDemandMappingDao{
	@Autowired
    private JdbcTemplate jdbcTemplate;
	public static Logger logger = Logger.getLogger(ResourcePlanConstants.DatabasePrintStr);
	
	 public void queryCircuitDemandMappings() {
	        logger.info("Query CircuitDemandMapping");
	        String sql = "SELECT * FROM CircuitDemandMapping";
	        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
	        for (Map<String, Object> row : list) {	        	
	            logger.info(" NetworkId: "+row.get("NetworkId")+" Circuit Id: "+row.get("CircuitId")+" Demand Id: "+row.get("DemandId"));
	        }
	    }
	 
		 
	 @SuppressWarnings({ "unchecked", "rawtypes" })
	public List<CircuitDemandMapping> findAll(){
			String sql = "SELECT * FROM CircuitDemandMapping";
			List<CircuitDemandMapping> mapping  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(CircuitDemandMapping.class));
			return mapping;
		}
	 
	 public void insertCircuitDemandMapping(final CircuitDemandMapping circuitDemandMapping) throws SQLException {     
     String sql = "INSERT into CircuitDemandMapping(NetworkId, CircuitId, DemandId) VALUES ( ?,?,?)";
     ///logger.info("  circuitDemandMapping.getNetworkId() "+  circuitDemandMapping.getNetworkId());
     jdbcTemplate.update(
             sql,
             new Object[] { circuitDemandMapping.getNetworkId(), circuitDemandMapping.getCircuitId(), circuitDemandMapping.getDemandId() });
 }

	 public void deleteByNetworkId(int networkid) {
	        logger.info("Delete CircuitDemandMapping");
	        String sql = "delete from CircuitDemandMapping where NetworkId = ?";
	        jdbcTemplate.update(sql, networkid);      
	    }
	 
	 public void deleteCircuitDemandMapping(int networkid, int circuitid) {
	        logger.info("Delete CircuitDemandMapping");
	        String sql = "delete from CircuitDemandMapping where NetworkId = ? and CircuitId = ? ";
	        jdbcTemplate.update(sql, networkid, circuitid);      
	    }
	 
	 public int count()
	 {		
		 logger.info("Count CircuitDemandMapping");
		  String sql = "select count(*) from CircuitDemandMapping"; 
		  return jdbcTemplate.queryForObject(sql, int.class);		  		        
	 }
	 
	 public int count(int networkId)
	 {		
		 logger.info("Count CircuitDemandMapping for NetworkId ::"+networkId);
		  String sql = "select count(*) from CircuitDemandMapping where NetworkId=?"; 
		  return jdbcTemplate.queryForObject(sql, int.class,networkId);		  		        
	 }
	 
	 public CircuitDemandMapping findDemand(int networkid, int circuitid) {  
		 	CircuitDemandMapping d;
		 	logger.info(" networkid and circuitid "+ networkid +  " and "+ circuitid);
	        String sql = "select * from CircuitDemandMapping where NetworkId = ? and CircuitId = ? ";
	        try
	        {
	        	return d = (CircuitDemandMapping) jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper(CircuitDemandMapping.class), networkid, circuitid);	        			       
	        }
	        catch( EmptyResultDataAccessException e) {
	        	logger.error(e.getMessage());	        	
	    		return null;
	    	}			        
	    }
	 
	 /**
	  * @date 18th Dec, 2017
	  * @author hp
	  * @param networkid
	  * @param circuitid
	  * @return
	  */
	 public List<CircuitDemandMapping> findCircuits(int networkid, int demandId) {  
		 	List<CircuitDemandMapping> d;
		 	logger.info(" networkid and demandId "+ networkid +  " and "+ demandId);
	        String sql = "select * from CircuitDemandMapping where NetworkId = ? and DemandId = ? ";
	        try
	        {	        	
	        	return d = jdbcTemplate.query(sql,new BeanPropertyRowMapper(CircuitDemandMapping.class), networkid, demandId);	        			       
	        }
	        catch( EmptyResultDataAccessException e) {
	        	logger.error(e.getMessage());	        	
	    		return null;
	    	}			        
	    }

	 
	 public int findDemandCount(int networkid, int demandId) {  
		 	int d;
	        String sql = "select COUNT(*) from DemandMapping where NetworkId = ? and DemandId = ? ";
	        try
	        {
	        	return d = jdbcTemplate.queryForObject(sql, int.class, networkid, demandId);	        			       
	        }
	        catch( EmptyResultDataAccessException e) {
	        	logger.error(e.getMessage());	        	
	    		return 0;
	    	}			        
	    }

	 
	 public void copyCircuitDemandMappingDataInBrField(int networkidGrField, int networkidBrField ) throws SQLException {			
		 	
		    String sql = "insert into CircuitDemandMapping ( NetworkId, CircuitId, DemandId ) select ?, CircuitId, DemandId from CircuitDemandMapping where NetworkId = ? ";
		    logger.info("copyCircuitDemandMappingDataInBrField: "+sql); 	     
		    jdbcTemplate.update( sql,networkidBrField,networkidGrField);
			 	}

	

	 }
