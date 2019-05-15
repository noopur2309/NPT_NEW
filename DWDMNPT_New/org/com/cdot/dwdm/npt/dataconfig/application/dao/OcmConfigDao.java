package application.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import application.MainMap;
import application.constants.ResourcePlanConstants;
import application.model.McsPortMapping;
import application.model.OcmConfig;

@Component
public class OcmConfigDao {
	@Autowired
    private JdbcTemplate jdbcTemplate;	
	public static Logger logger = Logger.getLogger(ResourcePlanConstants.DatabasePrintStr);
	
	 public List<OcmConfig> findAll(){
			String sql = "SELECT * FROM OcmConfig";
			MainMap.logger.info("findAll: "+sql);
			List<OcmConfig> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(OcmConfig.class));
			return info;
		}
	 
	 public List<OcmConfig> findAll(int networkid, int nodeid){
			String sql = "SELECT * FROM OcmConfig where NetworkId = ? and NodeId = ?";
			MainMap.logger.info("findAll: "+sql);
			List<OcmConfig> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(OcmConfig.class),networkid,nodeid);
			return info;
		}
	 public List<OcmConfig> findOcmId(int networkid, int nodeid,int rack,int subRack,int cardId){
			String sql = "SELECT * FROM OcmConfig where NetworkId = ? and NodeId = ? and Rack = ? and Sbrack = ? and Card = ?";
			MainMap.logger.info("findOcmId: "+sql);
			List<OcmConfig> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(OcmConfig.class),networkid,nodeid,rack,subRack,cardId);
			return info;
		}
	 
	 public void insert(final OcmConfig info) throws SQLException {         
     String sql = "INSERT into OcmConfig(NetworkId, NodeId, Rack, Sbrack, Card, CardType, CardSubType, OcmId) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
//    logger.info("insert: "+sql+info.toString());
     jdbcTemplate.update(
             sql,
             new Object[] { info.getNetworkId(), info.getNodeId(), info.getRack(), info.getSbrack(), info.getCard(), info.getCardType(), info.getCardSubType(), info.getOcmId() });
	 }
	 
	 public int count()
	 {		 
		  String sql = "select count(*) from OcmConfig"; 
		 logger.info("count: "+sql);
		  return jdbcTemplate.queryForObject(sql, int.class);		  		        
	 }
	 
	 public void update(int networkid, int nodeid, int rack, int sbrack, int card,  int OcmId) throws SQLException {
		   
	     String sql = "Update OcmConfig set OcmId = ?  where NetworkId = ? and NodeId =? and Rack = ? and Sbrack = ? and Card = ?";
	    logger.info("updateNode: "+sql); 	     
	     jdbcTemplate.update(
	             sql,
	             new Object[] { OcmId, networkid, nodeid, rack, sbrack, card});
		 	}
	 
	 public void deleteByNetworkId(int networkid) 
	 {
	        logger.info("Delete OcmConfig");
	        String sql = "delete from OcmConfig where NetworkId = ?";
	        jdbcTemplate.update(sql, networkid);      
	 }
	
}
