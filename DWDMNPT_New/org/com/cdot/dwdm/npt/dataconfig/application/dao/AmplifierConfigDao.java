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
import application.model.AmplifierConfig;
import application.model.TpnPortInfo;

@Component
public class AmplifierConfigDao {
	@Autowired
    private JdbcTemplate jdbcTemplate;	
	public static Logger logger = Logger.getLogger(ResourcePlanConstants.DatabasePrintStr);
	
	
	 public List<AmplifierConfig> findAll(int networkid){
			String sql = "SELECT * FROM AmplifierConfig where NetworkId = ?";
			MainMap.logger.info("findAll: "+sql);
			List<AmplifierConfig> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(AmplifierConfig.class),networkid);
			return info;
		}
	 
	 public List<AmplifierConfig> findAll(int networkid, int nodeid){
			String sql = "SELECT * FROM AmplifierConfig where NetworkId = ? and NodeId = ?";
			MainMap.logger.info("findAll: "+sql);
			List<AmplifierConfig> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(AmplifierConfig.class),networkid,nodeid);
			return info;
		}
	 
	 
	 public List<AmplifierConfig> findAllCommonBrField(int networkidBf,int networkidGf,int nodeid){
		 	
			String sql = "select * from (select bf.NetworkId , bf.NodeId, bf.Rack,bf.Sbrack, bf.Card, bf.Direction,bf.AmpType,bf.AmpStatus,bf.GainLimit,bf.ConfigurationMode,bf.VoaAttenuation,bf.EdfaDirId "+
			"from ( select * from AmplifierConfig where NetworkId=? and NodeId=?) as bf "+		
	        "left join ( select * from AmplifierConfig where NetworkId=? and NodeId=?) as gf on "+ 
	        "(gf.Rack = bf.Rack) and (gf.Sbrack = bf.Sbrack) and (gf.Card = bf.Card) "+
			"where bf.Direction = gf.Direction and "+
	        "bf.AmpType = gf.AmpType and "+
			"bf.AmpStatus = gf.AmpStatus and "+  
			"bf.GainLimit = gf.GainLimit and "+
			"bf.ConfigurationMode = gf.ConfigurationMode and "+ 
			"bf.VoaAttenuation = gf.VoaAttenuation and "+
			"bf.EdfaDirId = gf.EdfaDirId "+
			") as t ;";
				
				MainMap.logger.info(" AmplifierConfig_findAllCommonBrField: "+sql);
				List<AmplifierConfig> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(AmplifierConfig.class),networkidBf,nodeid,networkidGf,nodeid);
				return info;
			}

			public List<AmplifierConfig> findAllModifiedBrField(int networkidBf,int networkidGf,int nodeid){
				
				String sql = "select * from (select bf.NetworkId , bf.NodeId, bf.Rack,bf.Sbrack, bf.Card, bf.Direction,bf.AmpType,bf.AmpStatus,bf.GainLimit,bf.ConfigurationMode,bf.VoaAttenuation,bf.EdfaDirId "+
						"from ( select * from AmplifierConfig where NetworkId=? and NodeId=?) as bf "+		
				        "left join ( select * from AmplifierConfig where NetworkId=? and NodeId=?) as gf on "+ 
				        "(gf.Rack = bf.Rack) and (gf.Sbrack = bf.Sbrack) and (gf.Card = bf.Card) "+
						"where bf.Direction <> gf.Direction or "+
				        "bf.AmpType <> gf.AmpType or "+
						"bf.AmpStatus <> gf.AmpStatus or "+  
						"bf.GainLimit <> gf.GainLimit or "+
						"bf.ConfigurationMode <> gf.ConfigurationMode or "+ 
						"bf.VoaAttenuation <> gf.VoaAttenuation or "+
						"bf.EdfaDirId <> gf.EdfaDirId "+
						") as t ;";
				
				MainMap.logger.info(" AmplifierConfig_findAllModifiedBrField: "+sql);
				List<AmplifierConfig> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(AmplifierConfig.class),networkidBf,nodeid,networkidGf,nodeid);
				return info;
			}
			
			public List<AmplifierConfig> findAllAddedBrField(int networkidBf,int networkidGf,int nodeid){
			
				String sql = "select NetworkId,NodeId, Rack,Sbrack, Card, Direction,AmpType,AmpStatus,GainLimit,ConfigurationMode,VoaAttenuation,EdfaDirId"+
						"from (select bf.NetworkId , bf.NodeId, bf.Rack,bf.Sbrack, bf.Card, bf.Direction,bf.AmpType,bf.AmpStatus,bf.GainLimit,bf.ConfigurationMode,bf.VoaAttenuation,bf.EdfaDirId,gf.NodeId as GfNodeId from  "+
						"( select * from AmplifierConfig where NetworkId=? and NodeId=?) as bf "+ 
			            "left join ( select * from AmplifierConfig where NetworkId=? NodeId=?) as gf on "+ 
				        " (gf.Rack = bf.Rack) and (gf.Sbrack = bf.Sbrack) and (gf.Card = bf.Card)"+
				        ") as t where GfNodeId is null;";
	        
				MainMap.logger.info(" AmplifierConfig_findAllAddedBrField: "+sql);
				List<AmplifierConfig> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(AmplifierConfig.class),networkidBf,nodeid,networkidGf,nodeid);
				return info;
			}

			public List<AmplifierConfig> findAllDeletedBrField(int networkidBf,int networkidGf,int nodeid){
				
				String sql = "select NetworkId,NodeId, Rack,Sbrack, Card, Direction,AmpType,AmpStatus,GainLimit,ConfigurationMode,VoaAttenuation,EdfaDirId"+
						"from (select gf.NetworkId , gf.NodeId, gf.Rack,gf.Sbrack, gf.Card, gf.Direction,gf.AmpType,gf.AmpStatus,gf.GainLimit,gf.ConfigurationMode,gf.VoaAttenuation,gf.EdfaDirId,bf.NodeId as BfNodeId from  "+
						"( select * from AmplifierConfig where NetworkId=? and NodeId=?) as gf "+ 
			            "left join ( select * from AmplifierConfig where NetworkId=? NodeId=?) as bf on "+ 
				        " (gf.Rack = bf.Rack) and (gf.Sbrack = bf.Sbrack) and (gf.Card = bf.Card)"+
				        ") as t where BfNodeId is null;";
				
				MainMap.logger.info(" AmplifierConfig_findAllDeletedBrField: "+sql);
				List<AmplifierConfig> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(AmplifierConfig.class),networkidGf,nodeid,networkidBf,nodeid);
				return info;
			}
	 
	 
	 public void insert(final AmplifierConfig info) throws SQLException {         
     String sql = "INSERT into AmplifierConfig(NetworkId, NodeId, Rack, Sbrack, Card, Direction, AmpType, AmpStatus, "
     		+ "GainLimit, ConfigurationMode, VoaAttenuation, EdfaDirId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//    logger.info("insert: "+sql+info.toString());
     jdbcTemplate.update(
             sql,
             new Object[] { info.getNetworkId(), info.getNodeId(), info.getRack(), info.getSbrack(), info.getCard(), 
            		 info.getDirection(), info.getAmpType(), info.getAmpStatus(), info.getGainLimit(), 
            		 info.getConfigurationMode(), info.getVoaAttenuation(), info.getEdfaDirId() });
	 }
	 
	 public int count()
	 {		 
		  String sql = "select count(*) from AmplifierConfig"; 
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
	        logger.info("Delete AmplifierConfig");
	        String sql = "delete from AmplifierConfig where NetworkId = ?";
	        jdbcTemplate.update(sql, networkid);      
	 }
	
}
