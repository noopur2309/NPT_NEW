package application.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import application.MainMap;
import application.constants.ResourcePlanConstants;
import application.model.Equipment;
import application.model.ParametricPreference;

@Component
public class ParametricPreferenceDao{
	@Autowired
    private JdbcTemplate jdbcTemplate;
	public static Logger logger = Logger.getLogger(ResourcePlanConstants.DatabasePrintStr);
	 
	 public ParametricPreference find(int networkid, int nodeid, String category, String param, String value) {	      
		 try
		 {
	        String sql = "SELECT * FROM ParametricPreference where NetworkId = ? and NodeId = ? and Category = ? and Parameter = ? and Value = ? LIMIT 1 ";		        
	        ParametricPreference e  = (ParametricPreference) jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper(ParametricPreference.class),networkid, nodeid, category, param, value);			
			return e;
		 }
			catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}
	    }
	 

	 public List<ParametricPreference> findAll(int networkid) {	      
		 try
		 {
	        String sql = "SELECT * FROM ParametricPreference where NetworkId = ?";		        
	        List<ParametricPreference> list  = jdbcTemplate.query(sql,new BeanPropertyRowMapper(ParametricPreference.class),networkid);			
			return list;
		 }
			catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}
	    }
	 

	 public ParametricPreference checkPrefereceBySrNo(int networkid, int nodeid, String category, String type, String srno) {	      
		 try
		 {
	        String sql = "SELECT * FROM ParametricPreference where NetworkId = ? and NodeId = ? and Category = ? and CardType = ? and SerialNo = ?  ";		        
	        ParametricPreference e  = (ParametricPreference) jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper(ParametricPreference.class),networkid, nodeid, category, type, srno);			
			return e;
		 }
			catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}
	    }	 
	 

	 public Equipment findEquipmentById(int eid) {
	        logger.info("Query Equipment: findEquipmentById");
	        String sql = "SELECT * FROM Equipment where EquipmentId = ? ";	        
	        logger.info("For EquipmentId No: "+ eid);
	        Equipment e  = (Equipment) jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper(Equipment.class),eid);			
			return e; 
	    }
	 
	 public int findSlotSize(int eid) {	        
	        String sql = "SELECT SlotSize FROM Equipment where EquipmentId = ? ";	       
	        int e  = jdbcTemplate.queryForObject(sql,int.class,eid);			
			return e; 
	    }
		 
	 public List<Equipment> find(){
			String sql = "SELECT * FROM Equipment";
			List<Equipment> elist  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Equipment.class));
			return elist;
		}
	 
	 public void insert(final ParametricPreference e) throws SQLException {
     String sql = "INSERT into ParametricPreference(NetworkId, NodeId, Category, CardType, Parameter, Value, SerialNo) VALUES (?, ?, ?, ?, ?, ?, ?)";
     jdbcTemplate.update(
             sql,
             new Object[] { e.getNetworkId(), e.getNodeId(), e.getCategory(), e.getCardType(), e.getParameter(), e.getValue(), e.getSerialNo() });
	 	}
	 
	 public int count(int networkid)
	 {				
		  String sql = "select count(*) from ParametricPreference where NetworkId = ? "; 
		  return jdbcTemplate.queryForObject(sql, int.class, networkid);		  		        
	 }
	 
	 public void deleteByNetworkId(int networkid) 
	 {
	        System.out.println("Delete ParametricPreference");
	        String sql = "delete from ParametricPreference where NetworkId = ?";
	        jdbcTemplate.update(sql, networkid);      
	 }
	 
	 public void initializeParamPrefbrField(int networkIdGf,int networkIdBf) throws SQLException {
	     String sql = "INSERT into ParametricPreference (NetworkId, NodeId, Category, CardType, Parameter, Value, SerialNo) select ?, NodeId, Category, CardType, Parameter, Value, SerialNo from ParametricPreference where NetworkId=?";
	     jdbcTemplate.update(sql,networkIdBf,networkIdGf);
		 	}
	
	 }
