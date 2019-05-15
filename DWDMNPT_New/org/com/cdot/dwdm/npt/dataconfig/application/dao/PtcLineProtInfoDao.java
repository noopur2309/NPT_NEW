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
import application.model.PtcLineProtInfo;
import application.model.PtcLineProtInfo;
import application.model.WssDirectionConfig;

@Component
public class PtcLineProtInfoDao {
	@Autowired
    private JdbcTemplate jdbcTemplate;
	public static Logger logger = Logger.getLogger(ResourcePlanConstants.DatabasePrintStr);
	
	 public List<PtcLineProtInfo> findAll(int networkid){
			String sql = "SELECT * FROM PtcLineProtInfo where NetworkId = ?";
			MainMap.logger.info("findAll: "+sql);
			List<PtcLineProtInfo> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(PtcLineProtInfo.class), networkid);
			return info;
		}
	 
	 public List<PtcLineProtInfo> findAll(int networkid, int nodeid){
			String sql = "SELECT * FROM PtcLineProtInfo where NetworkId = ? and NodeId = ?";
			MainMap.logger.info("findAll: "+sql);
			List<PtcLineProtInfo> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(PtcLineProtInfo.class),networkid,nodeid);
			return info;
		}
	 
	 public List<PtcLineProtInfo> findAllCommonBrField(int networkidBf,int networkidGf,int nodeid){
			String sql = "select * from (select bf.NetworkId , bf.NodeId , bf.ProtCardRackId, bf.ProtCardSubrackId, bf.ProtCardCardId, "+
					"bf.ProtCardCardType,bf.ProtCardCardSubType,bf.MpnRackId,bf.MpnSubrackId,bf.MpnCardId,bf.MpnCardType,bf.MpnCardSubType, "+
					"bf.ProtTopology,bf.ProtMechanism,bf.ProtStatus,bf.ProtType,bf.ActiveLine from "+
					"(select * from PtcLineProtInfo where NetworkId = ? and NodeId=? ) as bf "+
					"left join ( select * from PtcLineProtInfo where NetworkId = ? and nodeid=?) as gf on "+
					"(gf.NodeId = bf.NodeId) and (gf.ProtCardRackId = bf.ProtCardRackId) and (gf.ProtCardSubrackId = bf.ProtCardSubrackId) and (gf.ProtCardCardId = bf.ProtCardCardId) and (gf.ProtCardCardType = bf.ProtCardCardType) and (gf.ProtCardCardSubType = bf.ProtCardCardSubType) "+ 
					"where bf.MpnRackId = gf.MpnRackId and "+
					"bf.MpnSubrackId = gf.MpnSubrackId and "+
					"bf.MpnCardId = gf.MpnCardId and "+
					"bf.MpnCardType = gf.MpnCardType and "+ 
					"bf.MpnCardSubType = gf.MpnCardSubType and "+ 
					"bf.ProtTopology = gf.ProtTopology and "+
					"bf.ProtMechanism = gf.ProtMechanism and "+ 
					"bf.ProtStatus = gf.ProtStatus and "+ 
					"bf.ProtType = gf.ProtType and "+
					"bf.ActiveLine = gf.ActiveLine "+ 
					") As t ;";
			
			MainMap.logger.info(" findAllModified: "+sql);
			List<PtcLineProtInfo> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(PtcLineProtInfo.class),networkidBf,nodeid,networkidGf,nodeid);
			return info;
		}

		public List<PtcLineProtInfo> findAllModifiedBrField(int networkidBf,int networkidGf,int nodeid){
			String sql = "select * from (select bf.NetworkId , bf.NodeId , bf.ProtCardRackId, bf.ProtCardSubrackId, bf.ProtCardCardId, "+
					"bf.ProtCardCardType,bf.ProtCardCardSubType,bf.MpnRackId,bf.MpnSubrackId,bf.MpnCardId,bf.MpnCardType,bf.MpnCardSubType, "+
					"bf.ProtTopology,bf.ProtMechanism,bf.ProtStatus,bf.ProtType,bf.ActiveLine from "+
					"(select * from PtcLineProtInfo where NetworkId = ? and NodeId=? ) as bf "+
					"left join ( select * from PtcLineProtInfo where NetworkId = ? and nodeid=?) as gf on "+
					"(gf.NodeId = bf.NodeId) and (gf.ProtCardRackId = bf.ProtCardRackId) and (gf.ProtCardSubrackId = bf.ProtCardSubrackId) and (gf.ProtCardCardId = bf.ProtCardCardId) and (gf.ProtCardCardType = bf.ProtCardCardType) and (gf.ProtCardCardSubType = bf.ProtCardCardSubType) "+ 
					"where bf.MpnRackId <> gf.MpnRackId or  "+
					"bf.MpnSubrackId <> gf.MpnSubrackId or  "+
					"bf.MpnCardId <> gf.MpnCardId or "+
					"bf.MpnCardType <> gf.MpnCardType or "+ 
					"bf.MpnCardSubType <> gf.MpnCardSubType or "+ 
					"bf.ProtTopology <> gf.ProtTopology or "+
					"bf.ProtMechanism <> gf.ProtMechanism or "+ 
					"bf.ProtStatus <> gf.ProtStatus or "+ 
					"bf.ProtType <> gf.ProtType or "+
					"bf.ActiveLine <> gf.ActiveLine "+ 
					") As t ;";
			
			MainMap.logger.info(" findAllModified: "+sql);
			List<PtcLineProtInfo> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(PtcLineProtInfo.class),networkidBf,nodeid,networkidGf,nodeid);
			return info;
		}
		
		public List<PtcLineProtInfo> findAllAddedBrField(int networkidBf,int networkidGf,int nodeid){
			String sql = "select NetworkId , NodeId , ProtCardRackId, ProtCardSubrackId, ProtCardCardId, "+
					"ProtCardCardType,ProtCardCardSubType,MpnRackId,MpnSubrackId,MpnCardId,MpnCardType,MpnCardSubType, "+
					"ProtTopology,ProtMechanism,ProtStatus,ProtType,ActiveLine from (select bf.NetworkId , bf.NodeId , bf.ProtCardRackId, bf.ProtCardSubrackId, bf.ProtCardCardId, "+
					"bf.ProtCardCardType,bf.ProtCardCardSubType,bf.MpnRackId,bf.MpnSubrackId,bf.MpnCardId,bf.MpnCardType,bf.MpnCardSubType, "+
					"bf.ProtTopology,bf.ProtMechanism,bf.ProtStatus,bf.ProtType,bf.ActiveLine,gf.NetworkId as NetworkIdGf , "+
					"gf.NodeId as NodeIdGf from ( select * from PtcLineProtInfo where NetworkId = ? and NodeId=?) as bf "+
					"left join ( select * from PtcLineProtInfo where NetworkId = ? and NodeId=?) as gf on  "+
					"(gf.NodeId = bf.NodeId) and (gf.ProtCardRackId = bf.ProtCardRackId) and (gf.ProtCardSubrackId = bf.ProtCardSubrackId) and (gf.ProtCardCardId = bf.ProtCardCardId) and (gf.ProtCardCardType = bf.ProtCardCardType) and (gf.ProtCardCardSubType = bf.ProtCardCardSubType)) "+
					"as t where NodeIdGf is null and NetworkIdGf is null;";
			MainMap.logger.info(" findAllModified: "+sql);
			List<PtcLineProtInfo> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(PtcLineProtInfo.class),networkidBf,nodeid,networkidGf,nodeid);
			return info;
		}

		public List<PtcLineProtInfo> findAllDeletedBrField(int networkidBf,int networkidGf,int nodeid){
			String sql = "select NetworkId , NodeId , ProtCardRackId, ProtCardSubrackId, ProtCardCardId, "+
					"ProtCardCardType,ProtCardCardSubType,MpnRackId,MpnSubrackId,MpnCardId,MpnCardType,MpnCardSubType, "+
					"ProtTopology,ProtMechanism,ProtStatus,ProtType,ActiveLine from (select gf.NetworkId , gf.NodeId , gf.ProtCardRackId, gf.ProtCardSubrackId, gf.ProtCardCardId, "+
					"gf.ProtCardCardType,gf.ProtCardCardSubType,gf.MpnRackId,gf.MpnSubrackId,gf.MpnCardId,gf.MpnCardType,gf.MpnCardSubType, "+
					"gf.ProtTopology,gf.ProtMechanism,gf.ProtStatus,gf.ProtType,gf.ActiveLine,bf.NetworkId as NetworkIdBf , "+
					"bf.NodeId as NodeIdBf from ( select * from PtcLineProtInfo where NetworkId = ? and NodeId=?) as gf "+ 
					"left join ( select * from PtcLineProtInfo where NetworkId = ? and NodeId=?) as bf on  "+
					"(gf.NodeId = bf.NodeId) and (gf.ProtCardRackId = bf.ProtCardRackId) and (gf.ProtCardSubrackId = bf.ProtCardSubrackId) and (gf.ProtCardCardId = bf.ProtCardCardId) and (gf.ProtCardCardType = bf.ProtCardCardType) and (gf.ProtCardCardSubType = bf.ProtCardCardSubType)) "+
					"as t where NodeIdBf is null and NetworkIdBf is null;";
			MainMap.logger.info(" findAllModified: "+sql);
			List<PtcLineProtInfo> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(PtcLineProtInfo.class),networkidGf,nodeid,networkidBf,nodeid);
			return info;
		}
	 
	 
	 public void insert(final PtcLineProtInfo info) throws SQLException {         
     String sql = "INSERT into PtcLineProtInfo(NetworkId, NodeId, ProtCardRackId, ProtCardSubrackId, ProtCardCardId,ProtCardCardType,ProtCardCardSubType, MpnRackId, MpnSubrackId, MpnCardId,MpnCardType,MpnCardSubType, ProtTopology, ProtMechanism, ProtStatus, ProtType, ActiveLine) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//    logger.info("insert: "+sql+info.toString());
     jdbcTemplate.update(
             sql,
             new Object[] { info.getNetworkId(), info.getNodeId(), info.getProtCardRackId(), info.getProtCardSubrackId(), info.getProtCardCardId(),info.getProtCardCardType(),info.getProtCardCardSubType(),info.getMpnRackId(), info.getMpnSubrackId(), info.getMpnCardId(),info.getMpnCardType(),info.getMpnCardSubType(), info.getProtTopology(), info.getProtMechanism(), info.getProtStatus(), info.getProtType(), info.getActiveLine() });
	 }
	 
	 public int count()
	 {		 
		  String sql = "select count(*) from PtcLineProtInfo"; 
		 logger.info("count: "+sql);
		  return jdbcTemplate.queryForObject(sql, int.class);		  		        
	 }	 
	 
	 public void deleteByNetworkId(int networkid) 
	 {
	        logger.info("Delete PtcLineProtInfo");
	        String sql = "delete from PtcLineProtInfo where NetworkId = ?";
	        jdbcTemplate.update(sql, networkid);      
	 }
	
}
