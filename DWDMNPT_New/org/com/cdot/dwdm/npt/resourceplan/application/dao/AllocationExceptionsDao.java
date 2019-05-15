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
import application.model.AllocationExceptions;
import application.model.CardInfo;
import application.model.Equipment;

@Component
public class AllocationExceptionsDao{
	@Autowired
    private JdbcTemplate jdbcTemplate;
	public static Logger logger = Logger.getLogger(ResourcePlanConstants.DatabasePrintStr);
	 
	 public AllocationExceptions find(int networkid, int nodeid, String cardtype, String srno) {	  
		 try
		 {
	        String sql = "SELECT * FROM AllocationExceptions where NetworkId = ? and NodeId = ? and CardType = ? and SerialNo = ? ";	        
	        logger.info("Find AllocationException: "+"networkid: "+ networkid+" nodeid: "+ nodeid+" cardtype: "+ cardtype+" srno: "+ srno);
	        AllocationExceptions e  = (AllocationExceptions) jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper(AllocationExceptions.class),networkid, nodeid,cardtype,srno);			
			return e; 
		 }
		 catch(EmptyResultDataAccessException e) {
			return null;
		 }
	    }
	 
	 public List<AllocationExceptions> find(int networkid) {	  
		 try
		 {
	        String sql = "SELECT * FROM AllocationExceptions where NetworkId = ?";	        
	        logger.info("Find AllocationException: "+"networkid: "+ networkid);
	        List<AllocationExceptions> e  =jdbcTemplate.query(sql,new BeanPropertyRowMapper(AllocationExceptions.class),networkid);			
			return e; 
		 }
		 catch(EmptyResultDataAccessException e) {
			return null;
		 }
	    }
	 
	 public List<AllocationExceptions> findExceptions(int networkid, int nodeid, String type) {	  
		 try
		 {
	        String sql = "SELECT * FROM AllocationExceptions where NetworkId = ? and NodeId = ? and Type = ? ";	        
	        logger.info("findExceptions: "+"networkid: "+ networkid+" nodeid: "+ nodeid+" type: "+ type);
	        List<AllocationExceptions> e  =  jdbcTemplate.query(sql,new BeanPropertyRowMapper(AllocationExceptions.class),networkid, nodeid,type);			
			return e; 
		 }
		 catch(EmptyResultDataAccessException e) {
			return null;
		 }
	    }
	 
//	 public void updateStatus(AllocationExceptions info) throws SQLException {		     
//	     String sql = "Update AllocationExceptions  set Status = ? where NetworkId = ? and NodeId = ? and CardType = ? and SerialNo =? ";
//	     jdbcTemplate.update(
//	             sql,
//	             new Object[] { info.getStatus(), info.getNetworkId(), info.getNodeId(), info.getCardType(), info.getSerialNo() });
//	 }
		 
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
	 
	 public void insert(final AllocationExceptions e) throws SQLException {
//     System.out.println("Insert Equipment id "+e.getEquipmentId());     
     String sql = "INSERT into AllocationExceptions(NetworkId, NodeId, SerialNo, CardType, Location, Port,Type) VALUES (?, ?, ?, ?, ?, ?,?)";
     jdbcTemplate.update(
             sql,
             new Object[] { e.getNetworkId(), e.getNodeId(), e.getSerialNo(), e.getCardType(), e.getLocation(), e.getPort(),e.getType()});
	 	}
	 
	
	 
	 public int count(int networkId)
	 {		
		 System.out.println("Count Equipment");
		  String sql = "select count(*) from AllocationExceptions where NetworkId=?"; 
		  return jdbcTemplate.queryForObject(sql, int.class,networkId);		  		        
	 }
	 
	 public void deleteByNetworkId(int networkid) 
	 {
	        System.out.println("Delete AllocationExceptions");
	        String sql = "delete from AllocationExceptions where NetworkId = ?";
	        jdbcTemplate.update(sql, networkid);      
	 }
	 
	 public void initializeParamPrefbrField(int networkIdGf,int networkIdBf) throws SQLException {
	     String sql = "INSERT into AllocationExceptions (NetworkId, NodeId,SerialNo, CardType, Location, Port,Type) select ?, NodeId,SerialNo, CardType, Location, Port,Type from AllocationExceptions where NetworkId=?";
	     jdbcTemplate.update(sql,networkIdBf,networkIdGf);
		 	}
	 
	 
	 public List<AllocationExceptions> findAddedExceptionsBrField(int networkidGf,int networkidBf, int nodeid) {	  
		 try
		 {			 
			 
	        String sql = "SELECT * FROM (select bf.NetworkId , bf.NodeId , bf.SerialNo, bf.CardType, bf.Location, "+
						"bf.Port,bf.Type ,gf.NetworkId as NetworkIdGf ,"+
						"gf.NodeId as NodeIdGf from ( select * from AllocationExceptions where NetworkId = ? and NodeId=?) as bf "+
						"left join ( select * from AllocationExceptions where NetworkId = ? and NodeId=?) as gf on  "+
						"(gf.NodeId = bf.NodeId) and (gf.SerialNo = bf.SerialNo) and (gf.CardType = bf.CardType) and (gf.Location = bf.Location)) "+
						"as t where NodeIdGf is null and NetworkIdGf is null;";
						
	        logger.info("findAddedExceptionsBrField: "+"networkid: "+ networkidBf+" nodeid: "+ nodeid);
	        List<AllocationExceptions> e  =  jdbcTemplate.query(sql,new BeanPropertyRowMapper(AllocationExceptions.class),networkidBf, nodeid,networkidGf,nodeid);			
			return e; 
		 }
		 catch(EmptyResultDataAccessException e) {
			return null;
		 }
	    }
	 
	 public List<AllocationExceptions> findDeletedExceptionsBrField(int networkidGf,int networkidBf, int nodeid) {	  
		 try
		 {
			 String sql = "SELECT * FROM (select gf.NetworkId , gf.NodeId , gf.SerialNo, gf.CardType, gf.Location, "+
						"gf.Port,gf.Type ,bf.NetworkId as NetworkIdBf ,"+
						"bf.NodeId as NodeIdBf from ( select * from AllocationExceptions where NetworkId = ? and NodeId=?) as gf "+
						"left join ( select * from AllocationExceptions where NetworkId = ? and NodeId=?) as bf on  "+
						"(gf.NodeId = bf.NodeId) and (gf.SerialNo = bf.SerialNo) and (gf.CardType = bf.CardType) and (gf.Location = bf.Location)) "+
						"as t where NodeIdBf is null and NetworkIdBf is null;";     
	        System.out.println("Delete sql exceptions in bf --- "+sql);
	        logger.info("findDeletedExceptionsBrField: "+"networkid: "+ networkidBf+" nodeid: "+ nodeid);
	        List<AllocationExceptions> e  =  jdbcTemplate.query(sql,new BeanPropertyRowMapper(AllocationExceptions.class),networkidGf, nodeid,networkidBf,nodeid);			
			return e; 
		 }
		 catch(EmptyResultDataAccessException e) {
			return null;
		 }
	    }
	
	 }
