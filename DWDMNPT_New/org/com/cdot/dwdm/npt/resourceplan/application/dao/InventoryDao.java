package application.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import application.constants.ResourcePlanConstants;
import application.model.Equipment;
import application.model.Inventory;

@Component
public class InventoryDao{
	@Autowired
    private JdbcTemplate jdbcTemplate;
	public static Logger logger = Logger.getLogger(ResourcePlanConstants.DatabasePrintStr);
	 
	 public Inventory findInventory(int networkid, int nodeid, int equipmentid) {
	        logger.info("Query Node: findInventory");
	        Inventory in;
	        String sql = "SELECT * FROM Inventory where NetworkId = ? and NodeId = ? and EquipmentId = ? ";	        
	        logger.info("For Node Id: "+nodeid+" Equipment Id: "+ equipmentid);
	        try
	        {
	        	in  = (Inventory) jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper(Inventory.class),networkid,nodeid,equipmentid);
	        }
	        catch(Exception e) {	        	
	        	logger.info("InventoryDao.findInventory()"+e.getMessage());	        	
//	        	e.printStackTrace();
	    		return null;
	    	}
	        			
			return in;			
	    }
		 
	 public List<Inventory> findAll(){
			String sql = "SELECT * FROM Inventory";
			List<Inventory> elist  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Inventory.class));
			return elist;
		}
	 
	 public void insertInventory(final Inventory e) throws SQLException {		
     String sql = "INSERT into Inventory(NetworkId, NodeId, EquipmentId, Quantity) VALUES (?, ?, ?, ?)";
     jdbcTemplate.update(
             sql,
             new Object[] { e.getNetworkId(), e.getNodeId(), e.getEquipmentId(), e.getQuantity()});
	 	}
	 
	 public void updateInventory(int networkid, int nodeid, int equipmentid, double quantity) throws SQLException {
//	     logger.info("updateInventory ");     
	     String sql = "Update Inventory set Quantity = ? where NetworkId = ? and NodeId = ? and EquipmentId = ?";
	     jdbcTemplate.update(
	             sql,
	             new Object[] { quantity, networkid, nodeid, equipmentid});
		 	}
	 
	 public int count()
	 {		
		 logger.info("Count Inventory");
		  String sql = "select count(*) from Inventory"; 
		  return jdbcTemplate.queryForObject(sql, int.class);		  		        
	 }
	 
	 public int count(int networkId)
	 {		
		 logger.info("Count Inventory");
		  String sql = "select count(*) from Inventory where NetworkId=?"; 
		  return jdbcTemplate.queryForObject(sql, int.class,networkId);		  		        
	 }
	 
	 public int findSlotRequirement(int networkid, int nodeid) {
	        logger.info("Query Node: findSlotRequirement");
	        Inventory in;
	        int slotsreq;
	        String sql = "select sum(e.SlotSize*i.Quantity) from Inventory i left join Equipment e on e.EquipmentId=i.EquipmentId and i.NetworkId=? and i.NodeId=?";	        
	        try
	        {
	        	slotsreq  =(int) jdbcTemplate.queryForObject(sql,int.class,networkid,nodeid);
	        }
	        catch(Exception e) {	        	
	        	logger.info("InventoryDao.findSlotRequirement()"+e.getMessage());	        	
//	        	e.printStackTrace();
	    		return (Integer) null;
	    	}
	        			
			return slotsreq;			
	    }
	 
	 public void deleteByNetworkId(int networkid) {
//	        logger.info("Delete Inventory");
	        String sql = "delete from Inventory where NetworkId = ?";
	        jdbcTemplate.update(sql, networkid);      
	    }
	 
	 public void deleteByNetworkId(int networkid,int nodeid) {
//	        logger.info("Delete Inventory");
	        String sql = "delete from Inventory where NetworkId = ? and NodeId=?";
	        jdbcTemplate.update(sql, networkid,nodeid);      
	    }
	 

	 }
