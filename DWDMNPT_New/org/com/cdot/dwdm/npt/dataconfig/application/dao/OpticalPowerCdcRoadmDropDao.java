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
import application.model.AmplifierConfig;
import application.model.OpticalPowerCdcRoadmAdd;
import application.model.OpticalPowerCdcRoadmDrop;
import application.model.TpnPortInfo;

@Component
public class OpticalPowerCdcRoadmDropDao {
	@Autowired
    private JdbcTemplate jdbcTemplate;	
	public static Logger logger = Logger.getLogger(ResourcePlanConstants.DatabasePrintStr);
	
	 public List<OpticalPowerCdcRoadmDrop> findAll(int networkid){
			String sql = "SELECT * FROM OpticalPowerCdcRoadmDrop where NetworkId = ?";
			MainMap.logger.info("findAll: "+sql);
			List<OpticalPowerCdcRoadmDrop> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(OpticalPowerCdcRoadmDrop.class),networkid);
			return info;
		}
	 
	 public List<OpticalPowerCdcRoadmDrop> findAll(int networkid, int nodeid){
			String sql = "SELECT * FROM OpticalPowerCdcRoadmAdd where NetworkId = ? and NodeId = ?";
			MainMap.logger.info("findAll: "+sql);
			List<OpticalPowerCdcRoadmDrop> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(OpticalPowerCdcRoadmDrop.class),networkid,nodeid);
			return info;
		}
	 
	 
	 public void insert(final OpticalPowerCdcRoadmDrop info) throws SQLException {         
     String sql = "INSERT into OpticalPowerCdcRoadmDrop(NetworkId, NodeId, Direction, NoOfLambdas, PaIn, PaOut, RxWssIn, RxWssOut, RxWssAttenuation, DropEdfaIn, DropEdfaOut, DropMcsIn, DropMcsOut, "
     		+ "MpnIn) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//    logger.info("insert: "+sql+info.toString());
     jdbcTemplate.update(
             sql,
             new Object[] { info.getNetworkId(), info.getNodeId(), info.getDirection(), info.getNoOfLambdas(), info.getPaIn(), 
            		 info.getPaOut(), info.getRxWssIn(), info.getRxWssOut(), info.getRxWssAttenuation(),info.getDropEdfaIn(),info.getDropEdfaOut(), 
            		 info.getDropMcsIn(), info.getDropMcsOut(), info.getMpnIn() });
	 }
	 
	 public int count()
	 {		 
		  String sql = "select count(*) from OpticalPowerCdcRoadmDrop"; 
		 logger.info("count: "+sql);
		  return jdbcTemplate.queryForObject(sql, int.class);		  		        
	 }
	 
//	 public void update(int networkid, int nodeid, int rack, int sbrack, int card,  int status, float gain, int mode , int att) throws SQLException {
//		   
//	     String sql = "Update AmplifierConfig set AmpStatus = ?, GainLimit = ?,ConfigurationMode = ?,VoaAttenuation = ? where NetworkId = ? and NodeId =? and Rack = ? and Sbrack = ? and Card = ?";
//	    logger.info("updateNode: "+sql); 	     
//	     jdbcTemplate.update(
//	             sql,
//	             new Object[] { status, gain, mode, att, networkid, nodeid, rack, sbrack, card});
//		 	}
	 
	 public void deleteByNetworkId(int networkid) 
	 {
	        logger.info("Delete OpticalPowerCdcRoadmDrop");
	        String sql = "delete from OpticalPowerCdcRoadmDrop where NetworkId = ?";
	        jdbcTemplate.update(sql, networkid);      
	 }
	
}
