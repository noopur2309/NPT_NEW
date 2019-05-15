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
import application.model.TpnPortInfo;
import application.model.VoaConfigInfo;

@Component
public class VoaConfigInfoDao {
	@Autowired
    private JdbcTemplate jdbcTemplate;	
	public static Logger logger = Logger.getLogger(ResourcePlanConstants.DatabasePrintStr);
	
	 public List<VoaConfigInfo> findAll(int networkid){
			String sql = "SELECT * FROM VoaConfigInfo where NetworkId = ?";
			MainMap.logger.info("findAll: "+sql);
			List<VoaConfigInfo> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(VoaConfigInfo.class),networkid);
			return info;
		}
	 
	 public List<VoaConfigInfo> findAll(int networkid, int nodeid){
			String sql = "SELECT * FROM VoaConfigInfo where NetworkId = ? and NodeId = ?";
			MainMap.logger.info("findAll: "+sql);
			List<VoaConfigInfo> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(VoaConfigInfo.class),networkid,nodeid);
			return info;
		}
	 
	 
	 public void insert(final VoaConfigInfo info) throws SQLException {         
     String sql = "INSERT into VoaConfigInfo(NetworkId, NodeId, Rack, Sbrack, Card, Direction, ChannelType, AttenuationConfigMode, Attenuation) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
//    logger.info("insert: "+sql+info.toString());
     jdbcTemplate.update(
             sql,
             new Object[] { info.getNetworkId(), info.getNodeId(), info.getRack(), info.getSbrack(), info.getCard(), 
            		 info.getDirection(), info.getChannelType(), info.getAttenuationConfigMode(), info.getAttenuation() });
	 }
	 
	 public int count()
	 {		 
		  String sql = "select count(*) from VoaConfigInfo"; 
		 logger.info("count: "+sql);
		  return jdbcTemplate.queryForObject(sql, int.class);		  		        
	 }
	 
	 public void update(int networkid, int nodeid, int rack, int sbrack, int card,  int status, float gain, int mode , int att) throws SQLException {
		   
	     String sql = "Update AmplifierConfig set AmpStatus = ?, GainLimit = ?,ConfigurationMode = ?,VoaAttenuation = ? where NetworkId = ? and NodeId =? and Rack = ? and Sbrack = ? and Card = ?";
	    logger.info("updateNode: "+sql); 	     
	     jdbcTemplate.update(
	             sql,
	             new Object[] { status, gain, mode, att, networkid, nodeid, rack, sbrack, card});
		 	}
	 
	 public void updateGain(int networkid, int nodeid, int rack, int sbrack, int card,  float gain) throws SQLException {
		   
	     String sql = "Update AmplifierConfig set GainLimit = ? where NetworkId = ? and NodeId =? and Rack = ? and Sbrack = ? and Card = ?";
	    logger.info("updateNode: "+sql); 	     
	     jdbcTemplate.update(
	             sql,
	             new Object[] { gain, networkid, nodeid, rack, sbrack, card});
		 	}
	 
	 public void deleteByNetworkId(int networkid) 
	 {
	        logger.info("Delete VoaConfigInfo");
	        String sql = "delete from VoaConfigInfo where NetworkId = ?";
	        jdbcTemplate.update(sql, networkid);      
	 }
	
}
