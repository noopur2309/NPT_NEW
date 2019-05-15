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
import application.model.McsPortMapping;
import application.model.WssDirectionConfig;
import application.model.WssDirectionConfig;
import application.model.WssDirectionConfig;

@Component
public class WssDirectionConfigDao {
	@Autowired
    private JdbcTemplate jdbcTemplate;	
	public static Logger logger = Logger.getLogger(ResourcePlanConstants.DatabasePrintStr);
	
	 public List<WssDirectionConfig> findAll(int networkid){
			String sql = "SELECT * FROM WssDirectionConfig where NetworkId = ?";
			MainMap.logger.info("findAll: "+sql);
			List<WssDirectionConfig> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(WssDirectionConfig.class), networkid);
			return info;
		}
	 
	 public List<WssDirectionConfig> findAll(int networkid, int nodeid){
			String sql = "SELECT * FROM WssDirectionConfig where NetworkId = ? and NodeId = ?";
			MainMap.logger.info("findAll: "+sql);
			List<WssDirectionConfig> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(WssDirectionConfig.class),networkid,nodeid);
			return info;
		}
	 
	 
	 public void insert(final WssDirectionConfig info) throws SQLException {         
     String sql = "INSERT into WssDirectionConfig(NetworkId, NodeId, Rack, Sbrack, Card, CardType, CardSubType, WssDirection, WssDirectionType, LaserStatus, AttenuationConfigMode, FixedAttenuation, PreEmphasisTriggerPowerDiff, Attenuation) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//    logger.info("insert: "+sql+info.toString());
     jdbcTemplate.update(
             sql,
             new Object[] { info.getNetworkId(), info.getNodeId(), info.getRack(), info.getSbrack(), info.getCard(), info.getCardType(), info.getCardSubType(), info.getWssDirection(),  info.getWssDirectionType(), info.getLaserStatus(), info.getAttenuationConfigMode(), info.getFixedAttenuation(), info.getPreEmphasisTriggerPowerDiff(),  info.getAttenuation() });
	 }
	 
	 
	 public int count()
	 {		 
		  String sql = "select count(*) from WssDirectionConfig"; 
		 logger.info("count: "+sql);
		  return jdbcTemplate.queryForObject(sql, int.class);		  		        
	 }	 
	 
	 public void updateAttenuation(int networkid, int nodeid, int rack, int sbrack, int card, float wSS_attenuation_Drop) throws SQLException {
		   
	     String sql = "Update WssDirectionConfig set Attenuation = ? where NetworkId = ? and NodeId =? and Rack = ? and Sbrack = ? and Card = ?";
	    logger.info("updateNode: "+sql); 	     
	     jdbcTemplate.update(
	             sql,
	             new Object[] { wSS_attenuation_Drop, networkid, nodeid, rack, sbrack, card});
		 	}
	 
	 public void update(int networkid, int nodeid, int rack, int sbrack, int card,  int dirType, int LaserStatus, int AttenuationConfigMode , int FixedAttenuation, int PreEmphasisTriggerPowerDiff , int Attenuation) throws SQLException {
		   
	     String sql = "Update WssDirectionConfig set WssDirectionType = ?, LaserStatus = ?,AttenuationConfigMode = ?,FixedAttenuation = ? ,PreEmphasisTriggerPowerDiff = ?,Attenuation = ? where NetworkId = ? and NodeId =? and Rack = ? and Sbrack = ? and Card = ?";
	    logger.info("updateNode: "+sql); 	     
	     jdbcTemplate.update(
	             sql,
	             new Object[] { dirType, LaserStatus, AttenuationConfigMode , FixedAttenuation, PreEmphasisTriggerPowerDiff , Attenuation, networkid, nodeid, rack, sbrack, card});
		 	}
	 
	 public void deleteByNetworkId(int networkid) 
	 {
	        logger.info("Delete WssDirectionConfig");
	        String sql = "delete from WssDirectionConfig where NetworkId = ?";
	        jdbcTemplate.update(sql, networkid);      
	 }
	 
	 public List<WssDirectionConfig> findAllCommonBrField(int networkidBf,int networkidGf,int nodeid){
			String sql = "select * from (select bf.NetworkId , bf.NodeId , bf.Rack, bf.Sbrack, bf.Card, "+
					"bf.CardType,bf.CardSubType,bf.WssDirection,bf.WssDirectionType,bf.LaserStatus,bf.AttenuationConfigMode,bf.FixedAttenuation, "+
					"bf.PreEmphasisTriggerPowerDiff,bf.Attenuation from "+
					"(select * from WssDirectionConfig where NetworkId = ? and NodeId=?) as bf "+
					"left join ( select * from WssDirectionConfig where NetworkId = ? and NodeId=?) as gf on "+
					"(gf.NodeId = bf.NodeId) and (gf.Rack = bf.Rack) and (gf.Sbrack = bf.Sbrack) and (gf.Card = bf.Card) "+ 
					"where bf.WssDirection = gf.WssDirection and  "+
					"bf.WssDirectionType = gf.WssDirectionType and  "+
					"bf.LaserStatus = gf.LaserStatus and "+
					"bf.AttenuationConfigMode = gf.AttenuationConfigMode and "+ 
					"bf.FixedAttenuation = gf.FixedAttenuation and "+ 
					"bf.PreEmphasisTriggerPowerDiff = gf.PreEmphasisTriggerPowerDiff and "+
					"bf.Attenuation = gf.Attenuation "+
					") As t ;";
			
			MainMap.logger.info(" findAllModified: "+sql);
			List<WssDirectionConfig> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(WssDirectionConfig.class),networkidBf,nodeid,networkidGf,nodeid);
			return info;
		}

		public List<WssDirectionConfig> findAllModifiedBrField(int networkidBf,int networkidGf,int nodeid){
			String sql = "select * from (select bf.NetworkId , bf.NodeId , bf.Rack, bf.Sbrack, bf.Card, "+
					"bf.CardType,bf.CardSubType,bf.WssDirection,bf.WssDirectionType,bf.LaserStatus,bf.AttenuationConfigMode,bf.FixedAttenuation, "+
					"bf.PreEmphasisTriggerPowerDiff,bf.Attenuation from "+
					"(select * from WssDirectionConfig where NetworkId = ? and NodeId=?) as bf "+
					"left join ( select * from WssDirectionConfig where NetworkId = ? and NodeId=?) as gf on "+
					"(gf.NodeId = bf.NodeId) and (gf.Rack = bf.Rack) and (gf.Sbrack = bf.Sbrack) and (gf.Card = bf.Card) "+ 
					"where bf.WssDirection <> gf.WssDirection or  "+
					"bf.WssDirectionType <> gf.WssDirectionType or  "+
					"bf.LaserStatus <> gf.LaserStatus or "+
					"bf.AttenuationConfigMode <> gf.AttenuationConfigMode or "+ 
					"bf.FixedAttenuation <> gf.FixedAttenuation or "+ 
					"bf.PreEmphasisTriggerPowerDiff <> gf.PreEmphasisTriggerPowerDiff or "+
					"bf.Attenuation <> gf.Attenuation "+
					") As t ;";
			
			MainMap.logger.info(" findAllModified: "+sql);
			List<WssDirectionConfig> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(WssDirectionConfig.class),networkidBf,nodeid,networkidGf,nodeid);
			return info;
		}
		
		public List<WssDirectionConfig> findAllAddedBrField(int networkidBf,int networkidGf,int nodeid){
			String sql = "select NetworkId , NodeId ,Rack, Sbrack, Card, "+
					"CardType,CardSubType,WssDirection,WssDirectionType,LaserStatus,AttenuationConfigMode,FixedAttenuation, "+
					"PreEmphasisTriggerPowerDiff,Attenuation from (select bf.NetworkId , bf.NodeId , bf.Rack, bf.Sbrack, bf.Card, "+
					"bf.CardType,bf.CardSubType,bf.WssDirection,bf.WssDirectionType,bf.LaserStatus,bf.AttenuationConfigMode,bf.FixedAttenuation, "+
					"bf.PreEmphasisTriggerPowerDiff,bf.Attenuation,gf.NetworkId as NetworkIdGf , "+
					"gf.NodeId as NodeIdGf from ( select * from WssDirectionConfig where NetworkId = ? and NodeId=?) as bf "+
					"left join ( select * from WssDirectionConfig where NetworkId = ? and NodeId=?) as gf on  "+
					"(gf.NodeId = bf.NodeId) and (gf.Rack = bf.Rack) and (gf.Sbrack = bf.Sbrack) and (gf.Card = bf.Card)) "+
					"as t where t.NodeIdGf is null and t.NetworkIdGf is null;";
			MainMap.logger.info(" findAllModified: "+sql);
			List<WssDirectionConfig> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(WssDirectionConfig.class),networkidBf,nodeid,networkidGf,nodeid);
			return info;
		}

		public List<WssDirectionConfig> findAllDeletedBrField(int networkidBf,int networkidGf,int nodeid){
			String sql = "select NetworkId , NodeId ,Rack, Sbrack, Card, "+
					"CardType,CardSubType,WssDirection,WssDirectionType,LaserStatus,AttenuationConfigMode,FixedAttenuation, "+
					"PreEmphasisTriggerPowerDiff,Attenuation from (select gf.NetworkId , gf.NodeId , gf.Rack, gf.Sbrack, gf.Card, "+
					"gf.CardType,gf.CardSubType,gf.WssDirection,gf.WssDirectionType,gf.LaserStatus,gf.AttenuationConfigMode,gf.FixedAttenuation, "+
					"gf.PreEmphasisTriggerPowerDiff,gf.Attenuation,bf.NetworkId as NetworkIdBf , "+
					"bf.NodeId as NodeIdBf from ( select * from WssDirectionConfig where NetworkId = ? and NodeId=?) as gf "+ 
					"left join ( select * from WssDirectionConfig where NetworkId = ? and NodeId=?) as bf on  "+
					"(gf.NodeId = bf.NodeId) and (gf.Rack = bf.Rack) and (gf.Sbrack = bf.Sbrack) and (gf.Card = bf.Card)) "+
					"as t where t.NodeIdBf is null and t.NetworkIdBf is null;";
			MainMap.logger.info(" findAllModified: "+sql);
			List<WssDirectionConfig> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(WssDirectionConfig.class),networkidGf,nodeid,networkidBf,nodeid);
			return info;
		}
	
}
