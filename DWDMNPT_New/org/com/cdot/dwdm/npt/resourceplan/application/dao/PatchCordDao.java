package application.dao;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import application.model.Circuit;
import application.model.NetworkRoute;
import application.model.PatchCord;
import application.MainMap;
import application.constants.ResourcePlanConstants;
import application.model.CardInfo;
import application.model.RouteMapping;

@Component
public class PatchCordDao{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	public static Logger logger = Logger.getLogger(ResourcePlanConstants.DatabasePrintStr);

	public int countCardByType(int networkid,int nodeid, String cardtype) {	        
		String NodeKey= ""+networkid+"_"+nodeid;
//		String tablename= "CardInfo_"+NodeKey;		
		String tablename= "PatchCordInfo";
		String sql = "SELECT COUNT(*) FROM " +tablename+" where NetworkId = ? and NodeId=? and CardType = ?";	   
		logger.info("countCardByType: "+sql);
		logger.info("For Nodekey: "+ NodeKey+"For CardType: "+cardtype);
		int n  = jdbcTemplate.queryForObject(sql, int.class,networkid,nodeid,cardtype);			
		return n; 
	}

	public List<Map<String,Object>> countCordByTypeNEId(int networkid,int nodeid)
	{
		String NodeKey= ""+networkid+"_"+nodeid;
//		String tablename= "PatchCord_"+NodeKey;	
		String tablename= "PatchCordInfo";
		String sql = "SELECT  EquipmentId,  COUNT(1) as CNT FROM "+tablename+" where NetworkId=? and NodeId=? GROUP BY EquipmentId HAVING COUNT(1) >= 1; ";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,networkid,nodeid);
		return list;
	}

	public List<PatchCord> findAll(int networkid, int nodeid){
		String NodeKey= ""+networkid+"_"+nodeid;
//		String tablename= "PatchCord_"+NodeKey;
		String tablename= "PatchCordInfo";
		String sql = "SELECT * FROM "+tablename+" where NetworkId=? and NodeId=?";
		List<PatchCord> list  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(PatchCord.class),networkid,nodeid);
		return list;
	}

	public void insert(final PatchCord info) throws SQLException {
		//MainMap.logger.info("Insert PatchCord ");
		//     String tablename="PatchCord_"+info.getNodeKey();
		String tablename= "PatchCordInfo";
		String sql = "INSERT into " +tablename+ " (NetworkId,NodeId, EquipmentId, CordType, End1CardType, End1Location, End1Port, End2CardType, End2Location, End2Port, Length, DirectionEnd1, DirectionEnd2 ) VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(
				sql,
				new Object[] { info.getNetworkId(),info.getNodeId(), info.getEquipmentId(), info.getCordType(), info.getEnd1CardType(), info.getEnd1Location(), info.getEnd1Port(), info.getEnd2CardType(), info.getEnd2Location(), info.getEnd2Port(), info.getLength(), info.getDirectionEnd1(), info.getDirectionEnd2() });
	}

	 public void copyPatchCordDataInBrField(int networkidGrField, int networkidBrField ) throws SQLException {			
		 	
		    String sql = "insert into PatchCordInfo(NetworkId,NodeId, EquipmentId, CordType, End1CardType, End1Location, End1Port, End2CardType, End2Location,End2Port, Length, DirectionEnd1, DirectionEnd2 ) select ?, NodeId, EquipmentId, CordType, End1CardType, End1Location, End1Port, End2CardType, End2Location,End2Port, Length, DirectionEnd1, DirectionEnd2  from PatchCordInfo where NetworkId = ? ";
		    logger.info("copyPatchCordDataInBrField: "+sql); 	     
		    jdbcTemplate.update( sql,networkidBrField,networkidGrField);
			 	}
	 
	 
	 public int count(int networkId)
	 {		
		 logger.info("Count PatchCordInfo");
		  String sql = "select count(*) from PatchCordInfo where NetworkId=?"; 
		  return jdbcTemplate.queryForObject(sql, int.class,networkId);		  		        
	 }
	
	
	public void createPatchCordTable(String NodeKey)
	{		
		String tablename= "PatchCord_"+NodeKey;		 
		String sql = " CREATE TABLE IF NOT EXISTS `DwdmNpt`.`"+tablename+"` (  `NodeKey` VARCHAR(45) NOT NULL, `EquipmentId` INT NOT NULL, `CordType` VARCHAR(150) NULL, `End1CardType` VARCHAR(45) NULL, `End1Location` VARCHAR(45) NOT NULL, `End1Port` VARCHAR(45) NOT NULL, `End2CardType` VARCHAR(45) NULL, `End2Location` VARCHAR(45) NOT NULL, `End2Port` VARCHAR(45) NOT NULL, `Length` INT NULL, `DirectionEnd1` VARCHAR(45) NULL, `DirectionEnd2` VARCHAR(45) NULL, PRIMARY KEY (`NodeKey`, `EquipmentId`, `End1Location`, `End1Port`, `End2Location`, `End2Port`));";
		jdbcTemplate.execute(sql);
	}

	public int count(int networkid, int nodeid)
	{		
		logger.info("Count PatchCord");
		String NodeKey= ""+networkid+"_"+nodeid;
		//		  String tablename= "PatchCord_"+NodeKey;
		String tablename= "PatchCordInfo";
		//		  String sql = "select count(*) from "+ tablename;
		String sql = "select count(*) from "+ tablename+" where NetworkId=? and NodeId=?";
		return jdbcTemplate.queryForObject(sql, int.class,networkid,nodeid);		  		        
	}

	public void deletePatchCordInfo(int networkid) {
		logger.info("Delete deletePatchCordInfo");

		String tableName="PatchCordInfo";
		String sql = "Delete from "+tableName+" where NetworkId = ?" ;
		jdbcTemplate.update(sql,networkid);
		//	        String namestr = "PatchCord_"+networkid+"_%";
		//	        String sql = "SELECT table_name FROM information_schema.`TABLES` WHERE table_schema = 'DwdmNpt' AND table_name LIKE '" +namestr+"';";
		//	        logger.info(sql);
		//	        List<Map<String, Object>> names= jdbcTemplate.queryForList(sql);  
		//	        
		//	        for (Map<String, Object> row : names) {            
		//	            String sql1 = "DROP TABLE "+row.get("table_name")+";"; 
		//	            logger.info(sql1);
		//	            jdbcTemplate.update(sql1);
		//	        }
	}
	
	public void deletePatchCordInfoby(int networkid,int nodeid) {
		logger.info("Delete deletePatchCordInfo by using NetworkId and NodeId");

		String tableName="PatchCordInfo";
		String sql = "Delete from "+tableName+" where NetworkId = ? and NodeId = ?" ;
		jdbcTemplate.update(sql,networkid,nodeid);
	
	}
	
	public List<Map<String, Object>> tempFindAll(int networkid,int nodeid) {
		String NodeKey= ""+networkid+"_"+nodeid;
		String tablename= "PatchCord_"+NodeKey;
		String sql = "SELECT * FROM "+tablename+" where Nodekey=?";
		List<Map<String, Object>> list;
		try {
			list  = jdbcTemplate.queryForList(sql,NodeKey);
		} catch (Exception e) {
			// TODO: handle exception
			list=null;
		}
		return list;
	}

}
