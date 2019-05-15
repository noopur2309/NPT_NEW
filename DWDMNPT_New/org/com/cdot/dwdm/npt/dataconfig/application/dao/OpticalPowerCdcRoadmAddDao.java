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
import application.model.TpnPortInfo;

@Component
public class OpticalPowerCdcRoadmAddDao {
	@Autowired
    private JdbcTemplate jdbcTemplate;	
	public static Logger logger = Logger.getLogger(ResourcePlanConstants.DatabasePrintStr);
	
	 public List<OpticalPowerCdcRoadmAdd> findAll(int networkid){
			String sql = "SELECT * FROM OpticalPowerCdcRoadmAdd where NetworkId = ?";
			MainMap.logger.info("findAll: "+sql);
			List<OpticalPowerCdcRoadmAdd> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(OpticalPowerCdcRoadmAdd.class),networkid);
			return info;
		}
	 
	 public List<OpticalPowerCdcRoadmAdd> findAll(int networkid, int nodeid){
			String sql = "SELECT * FROM OpticalPowerCdcRoadmAdd where NetworkId = ? and NodeId = ?";
			MainMap.logger.info("findAll: "+sql);
			List<OpticalPowerCdcRoadmAdd> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(OpticalPowerCdcRoadmAdd.class),networkid,nodeid);
			return info;
		}
	 
	 
	 public void insert(final OpticalPowerCdcRoadmAdd info) throws SQLException {         
     String sql = "INSERT into OpticalPowerCdcRoadmAdd(NetworkId, NodeId, Direction, NoOfLambdas, BaIn, BaOut, TxWssIn, TxWssOut, TxWssAttenuation, AddEdfaIn, AddEdfaOut, AddMcsIn, AddMcsOut, "
     		+ "MpnLaunchPower) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//    logger.info("insert: "+sql+info.toString());
     jdbcTemplate.update(
             sql,
             new Object[] { info.getNetworkId(), info.getNodeId(), info.getDirection(), info.getNoOfLambdas(), info.getBaIn(), 
            		 info.getBaOut(), info.getTxWssIn(), info.getTxWssOut(), info.getTxWssAttenuation(),info.getAddEdfaIn(),info.getAddEdfaOut(), 
            		 info.getAddMcsIn(), info.getAddMcsOut(), info.getMpnLaunchPower() });
	 }
	 
	 public int count()
	 {		 
		  String sql = "select count(*) from AmplifierConfig"; 
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
	        logger.info("Delete OpticalPowerCdcRoadmAdd");
	        String sql = "delete from OpticalPowerCdcRoadmAdd where NetworkId = ?";
	        jdbcTemplate.update(sql, networkid);      
	 }
	
}
