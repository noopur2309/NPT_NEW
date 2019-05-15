package application.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import application.model.Circuit;
import application.model.MapData;
import application.model.Network;
import application.constants.ResourcePlanConstants;
import application.model.CardInfo;

@Component
public class MapDataDao{
	@Autowired
    private JdbcTemplate jdbcTemplate;
	public static Logger logger = Logger.getLogger(ResourcePlanConstants.DatabasePrintStr);
	
	 @SuppressWarnings({ "unchecked", "rawtypes" })
		public List<MapData> findAll(){
				String sql = "SELECT * FROM MapData";
				List<MapData> row  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(MapData.class));
				return row;
			}
	 
	 public void insert(final MapData map) throws SQLException {     
     int id = 0;     
     String sql = "INSERT into MapData(NetworkId, Map) VALUES ( ?,?)";
     jdbcTemplate.update(
             sql,
             new Object[] { map.getNetworkId(),map.getMap() });
	 }
	 
	 public void update(MapData map) throws SQLException {     
	     String sql = "update MapData set Map=? where NetworkId=?";
	     jdbcTemplate.update(
	             sql,
	             new Object[] { map.getMap().toString(),map.getNetworkId()});
		 }
	 
	 @SuppressWarnings("null")
	 public MapData findByNetworkId(int networkid) {	        
	        String sql = "Select * from MapData where NetworkId = ?";
	        logger.info(sql);
	        try
	        {
	        	return (MapData)jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper(MapData.class),networkid);      
	        }
	        catch(EmptyResultDataAccessException e) {
	        	logger.info("findByNetworkId Error:"+e);
	    		return null;
	    	} 	      
	 }

	 public void deleteByNetworkId(int networkid) {	        
	        String sql = "delete from MapData where NetworkId = ?";
	        jdbcTemplate.update(sql, networkid);	      
	 }
	 	 
	 public int count()
	 {		
		 logger.info("Count MapData");
		  String sql = "select count(*) from MapData"; 
		  return jdbcTemplate.queryForObject(sql, int.class);		  		        
	 }	 
	 
	 public int count(int networkId)
	 {		
		 logger.info("Count MapData");
		  String sql = "select count(*) from MapData where NetworkId=?"; 
		  return jdbcTemplate.queryForObject(sql, int.class,networkId);		  		        
	 }
}
