package application.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import application.MainMap;
import application.constants.MapConstants;
import application.constants.ResourcePlanConstants;
import application.model.Circuit;
import application.model.NetworkRoute;
import application.model.Node;
import application.model.OpticalData;
import application.model.RouteMapping;

@Component
public class OpticalDataDao{
	@Autowired
    private JdbcTemplate jdbcTemplate;
	public static Logger logger = Logger.getLogger(ResourcePlanConstants.DatabasePrintStr);
		 
	 public OpticalData findOpticalData(int equipmentid) {
	        String sql = "SELECT * FROM OpticalData where NetworkId = ? and EquipmentId = ?";	
	       logger.info("findOpticalData: "+sql);
	       logger.info("For EquipmentId: "+equipmentid);
	        OpticalData info  = (OpticalData) jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper(OpticalData.class),equipmentid);			
			return info; 
	    }	 
		 
	 public List<OpticalData> findAll(){
			String sql = "SELECT * FROM OpticalData";
			MainMap.logger.info("findAll: "+sql);
			List<OpticalData> infolist  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(OpticalData.class));
			return infolist;
		}
	 
	 
	 public void insert(final OpticalData data) throws SQLException {         
     String sql = "INSERT into OpticalData(EquipmentId, PIn, PInMax, PInMin, POut, POutMax, POutMin, Loss, Gain) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    logger.info("insertOpticalData: "+sql+data.toString());
     jdbcTemplate.update(
             sql,
             new Object[] { data.getEquipmentId(), data.getPIn(), data.getPInMax(), data.getPInMin(), data.getPOut(), data.getPOutMax(), data.getPOutMin(), data.getLoss(), data.getGain() });
     }
	 
	 public int count()
	 {		 
		  String sql = "select count(*) from OpticalData"; 
		 logger.info("count: "+sql);
		  return jdbcTemplate.queryForObject(sql, int.class);		  		        
	 }
	 
	public void deleteByEquipmentId(int EquipmentId) {	        
	        String sql = "delete from OpticalData where EquipmentId = ?";
	       logger.info("deleteByEquipmentId: "+sql); 
	       logger.info("For EquipmentId : "+EquipmentId); 
	        jdbcTemplate.update(sql, EquipmentId);      
	    }
	 }
