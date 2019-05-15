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

import application.constants.ResourcePlanConstants;
import application.model.AllocationExceptions;
import application.model.Equipment;
import application.model.EquipmentPreference;

@Component
public class EquipmentPreferenceDao{
	@Autowired
    private JdbcTemplate jdbcTemplate;
	public static Logger logger = Logger.getLogger(ResourcePlanConstants.DatabasePrintStr);
	 	 	 
	 public Equipment findEquipmentById(int eid) {
	        logger.info("Query Equipment: findEquipmentById");
	        String sql = "SELECT * FROM Equipment where EquipmentId = ? ";	        
	        logger.info("For EquipmentId No: "+ eid);
	        Equipment e  = (Equipment) jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper(Equipment.class),eid);			
			return e; 
	    }
	 
	 public String findRedundancy(int networkid, int nodeid, String category) {	        
	        String sql = "SELECT Redundancy FROM EquipmentPreference where NetworkId = ? and NodeId = ? and Category = ? and Preference=1";	       
	        String e  = jdbcTemplate.queryForObject(sql,String.class,networkid, nodeid, category);			
			return e; 
	    }
	 
	 /**
	  * finds the All preferences for network
	  * @param networkid
	  * @return
	  */
	 public List<EquipmentPreference> findAll(int networkid){
		 try
		 {
			String sql = "SELECT * from EquipmentPreference where NetworkId = ? order by NodeId,Category,Preference; ";
			List<EquipmentPreference> list  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(EquipmentPreference.class),networkid);
			return list;
		 }
		 catch(EmptyResultDataAccessException e) {
			return null;
		 }
		}
		 
	 /**
	  * finds the highest preference Eq type in particular category
	  * @param networkid
	  * @param nodeid
	  * @param category
	  * @return
	  */
	 public EquipmentPreference findPreferredEq(int networkid, int nodeid, String category ){
		 try
		 {
			String sql = "SELECT * from EquipmentPreference where Preference = (" + 
					"SELECT MIN(Preference) from EquipmentPreference where NetworkId = ? and NodeId = ? and Category = ? ) and NetworkId = ? and NodeId= ? and Category = ? ; ";
			EquipmentPreference e  = (EquipmentPreference) jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper(EquipmentPreference.class),networkid, nodeid, category, networkid, nodeid, category);
			return e;
		 }
		 catch(EmptyResultDataAccessException e) {
			return null;
		 }
		}
	 
	 public void insert(final EquipmentPreference ep) throws SQLException {
//     System.out.println("Insert Equipment id "+e.getEquipmentId());     
     String sql = "INSERT into EquipmentPreference(NetworkId, NodeId, Category, CardType, Preference, Redundancy) VALUES (?, ?, ?, ?, ?, ?)";
     jdbcTemplate.update(
             sql,
             new Object[] { ep.getNetworkId(), ep.getNodeId(), ep.getCategory(), ep.getCardType(), ep.getPreference(),  ep.getRedundancy()});
	 	}
	 
	 public void initializeEqPreferencesBrField(int gfNetworkId,int bfNetworkId) throws SQLException {
//	     System.out.println("Insert Equipment id "+e.getEquipmentId());
		 String sql = "insert into EquipmentPreference ( NetworkId, NodeId, Category, CardType, Preference, Redundancy ) select ?, NodeId, Category, CardType, Preference, Redundancy from EquipmentPreference where NetworkId = ?";
	     jdbcTemplate.update(sql,bfNetworkId,gfNetworkId);
	}
	 
	 public int count(int networkid)
	 {		
		 System.out.println("Count EquipmentPreference");
		  String sql = "select count(*) from EquipmentPreference where NetworkId = ?"; 
		  return jdbcTemplate.queryForObject(sql, int.class, networkid);		  		        
	 }
	 
	 public void deleteNodePref(int networkid,int nodeid) 
	 {
	        System.out.println("Delete deleteNodePref");
	        String sql = "delete from EquipmentPreference where NetworkId = ? and NodeId = ?";
	        jdbcTemplate.update(sql, networkid,nodeid);      
	 }
	 
	 public void deleteNodeCategoryPref(int networkid,int nodeid,String category) 
	 {
	        System.out.println("Delete deleteNodeCategoryPref");
	        String sql = "delete from EquipmentPreference where NetworkId = ? and NodeId = ? and Category = ?";
	        jdbcTemplate.update(sql, networkid,nodeid,category);      
	 }
	 
	 public void deleteByNetworkId(int networkid) 
	 {
	        System.out.println("Delete EquipmentPreference");
	        String sql = "delete from EquipmentPreference where NetworkId = ?";
	        jdbcTemplate.update(sql, networkid);      
	 }
	 
	 public void deleteByNetworkId(int networkid,int nodeid) 
	 {
	        System.out.println("Delete EquipmentPreference");
	        String sql = "delete from EquipmentPreference where NetworkId = ? and NodeId = ?";
	        jdbcTemplate.update(sql, networkid,nodeid);      
	 }
	 
	 //get Nodes whose inventory preference have been initialized
	 public List<Map<String, Object>> initializedNodes(int networkid)
	 {		
		  System.out.println("Get initializedNodes");
		  String sql = "select distinct(NodeId) from EquipmentPreference where NetworkId = ?"; 
		  return jdbcTemplate.queryForList(sql,networkid);		  		        
	 }
	
	 }
