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

@Component
public class EquipmentDao{
	@Autowired
    private JdbcTemplate jdbcTemplate;
	public static Logger logger = Logger.getLogger(ResourcePlanConstants.DatabasePrintStr);
	 
	 public Equipment findEquipment(int partno) {
	        logger.info("Query Equipment: findEquipment");
	        String sql = "SELECT * FROM Equipment where PartNo = ? ";	        
	        System.out.println("For Part No: "+ partno);
	        Equipment e  = (Equipment) jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper(Equipment.class),partno);			
			return e; 
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
		 
	 public List<Equipment> findAll(){
			String sql = "SELECT * FROM Equipment";
			List<Equipment> elist  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Equipment.class));
			return elist;
		}
	 
	 public void insertEquipment(final Equipment e) throws SQLException {
//     System.out.println("Insert Equipment id "+e.getEquipmentId());     
     String sql = "INSERT into Equipment(EquipmentId, Name, PartNo, PowerConsumption, TypicalPower, SlotSize, Details, Price, Category, RevisionCode) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
     jdbcTemplate.update(
             sql,
             new Object[] { e.getEquipmentId(), e.getName(), e.getPartNo(), e.getPowerConsumption(), e.getTypicalPower(), e.getSlotSize(), e.getDetails(), e.getPrice(), e.getCategory(), e.getRevisionCode()});
	 	}
	 
	 public int count()
	 {		
		 System.out.println("Count Equipment");
		  String sql = "select count(*) from Equipment"; 
		  return jdbcTemplate.queryForObject(sql, int.class);		  		        
	 }
	 
	 public void deleteByNetworkId() 
	 {
	        System.out.println("Delete Equipment");
	        String sql = "delete from Equipment";
	        jdbcTemplate.update(sql);      
	 }
	
	 }
