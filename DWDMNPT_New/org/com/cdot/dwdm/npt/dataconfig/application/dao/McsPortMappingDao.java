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
import application.model.McsPortMapping;

@Component
public class McsPortMappingDao {
	@Autowired
    private JdbcTemplate jdbcTemplate;	
	public static Logger logger = Logger.getLogger(ResourcePlanConstants.DatabasePrintStr);
	
	 public List<McsPortMapping> findAll(){
			String sql = "SELECT * FROM McsPortMapping";
			MainMap.logger.info("findAll: "+sql);
			List<McsPortMapping> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(McsPortMapping.class));
			return info;
		}
	 
	 public List<McsPortMapping> findAll(int networkid, int nodeid){
			String sql = "SELECT * FROM McsPortMapping where NetworkId = ? and NodeId = ?";
			MainMap.logger.info("findAll: "+sql);
			List<McsPortMapping> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(McsPortMapping.class),networkid,nodeid);
			return info;
		}
	 
	 
	 public List<McsPortMapping> findAllCommonBrField(int networkidBf,int networkidGf,int nodeid){
		 	
		String sql = "select * from (select bf.NetworkId , bf.NodeId, bf.Rack,bf.Sbrack, bf.Card, bf.CardType,bf.CardSubType,bf.McsId,bf.McsAddPortInfo,bf.McsDropPortInfo,bf.AddTpnRackId,bf.AddTpnSubRackId,bf.AddTpnSlotId,bf.AddTpnLinePortNum "+
		",bf.DropTpnRackId,bf.DropTpnSubRackId,bf.DropTpnSlotId,bf.DropTpnLinePortNum,bf.McsSwitchPort from  "+
		"( select * from McsPortMapping where NetworkId=? and NodeId=?) as bf "+ 
        "left join ( select * from McsPortMapping where NetworkId=? and NodeId=?) as gf on "+ 
        "(gf.Rack = bf.Rack) and (gf.Sbrack = bf.Sbrack) and (gf.Card = bf.Card) and (gf.McsId = bf.McsId) and (gf.McsAddPortInfo = bf.McsAddPortInfo) and (gf.AddTpnRackId = bf.AddTpnRackId) and (gf.AddTpnSubRackId = bf.AddTpnSubRackId) and (gf.AddTpnSlotId = bf.AddTpnSlotId) "+
		"where bf.CardType = gf.CardType and "+
        "bf.CardSubType = gf.CardSubType and "+
		"bf.McsDropPortInfo = gf.McsDropPortInfo and "+ 
		"bf.AddTpnLinePortNum = gf.AddTpnLinePortNum and "+ 
		"bf.DropTpnRackId = gf.DropTpnRackId and "+
		"bf.DropTpnSubRackId = gf.DropTpnSubRackId and "+ 
		"bf.DropTpnSlotId = gf.DropTpnSlotId and "+
        "bf.DropTpnLinePortNum = gf.DropTpnLinePortNum and "+ 
        "bf.McsSwitchPort = gf.McsSwitchPort "+
		") as t ;";
			
			MainMap.logger.info(" McsPortMapping_findAllCommonBrField: "+sql);
			List<McsPortMapping> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(McsPortMapping.class),networkidBf,nodeid,networkidGf,nodeid);
			return info;
		}

		public List<McsPortMapping> findAllModifiedBrField(int networkidBf,int networkidGf,int nodeid){
			
			String sql = "select * from (select bf.NetworkId , bf.NodeId, bf.Rack,bf.Sbrack, bf.Card, bf.CardType,bf.CardSubType,bf.McsId,bf.McsAddPortInfo,bf.McsDropPortInfo,bf.AddTpnRackId,bf.AddTpnSubRackId,bf.AddTpnSlotId,bf.AddTpnLinePortNum "+ 
					",bf.DropTpnRackId,bf.DropTpnSubRackId,bf.DropTpnSlotId,bf.DropTpnLinePortNum,bf.McsSwitchPort from "+ 
					"( select * from McsPortMapping where NetworkId=? and NodeId=?) as bf "+
		            "left join ( select * from McsPortMapping where NetworkId=? and NodeId=?) as gf on "+ 
			        "(gf.Rack = bf.Rack) and (gf.Sbrack = bf.Sbrack) and (gf.Card = bf.Card) and (gf.McsId = bf.McsId) and (gf.McsAddPortInfo = bf.McsAddPortInfo) and (gf.AddTpnRackId = bf.AddTpnRackId) and (gf.AddTpnSubRackId = bf.AddTpnSubRackId) and (gf.AddTpnSlotId = bf.AddTpnSlotId) "+
					"where bf.CardType <> gf.CardType or "+
			        "bf.CardSubType <> gf.CardSubType or "+ 
					"bf.McsDropPortInfo <> gf.McsDropPortInfo or "+ 
					"bf.AddTpnLinePortNum <> gf.AddTpnLinePortNum or "+
					"bf.DropTpnRackId <> gf.DropTpnRackId or "+
					"bf.DropTpnSubRackId <> gf.DropTpnSubRackId or "+ 
					"bf.DropTpnSlotId <> gf.DropTpnSlotId or "+
			        "bf.DropTpnLinePortNum <> gf.DropTpnLinePortNum or "+ 
			        "bf.McsSwitchPort <> gf.McsSwitchPort "+
					") as t ; ";
			
			MainMap.logger.info(" McsPortMapping_findAllModifiedBrField: "+sql);
			List<McsPortMapping> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(McsPortMapping.class),networkidBf,nodeid,networkidGf,nodeid);
			return info;
		}
		
		public List<McsPortMapping> findAllAddedBrField(int networkidBf,int networkidGf,int nodeid){
		
			String sql=" select NetworkId, NodeId, Rack,Sbrack, Card, CardType,CardSubType,McsId,McsAddPortInfo,McsDropPortInfo,AddTpnRackId,AddTpnSubRackId,AddTpnSlotId,AddTpnLinePortNum "+
					",DropTpnRackId,DropTpnSubRackId,DropTpnSlotId,DropTpnLinePortNum,McsSwitchPort from (select bf.NetworkId , bf.NodeId, bf.Rack,bf.Sbrack, bf.Card, bf.CardType, bf.CardSubType, "+ 
					"bf.McsId,bf.McsAddPortInfo,bf.McsDropPortInfo,bf.AddTpnRackId,bf.AddTpnSubRackId,bf.AddTpnSlotId,bf.AddTpnLinePortNum "+
					",bf.DropTpnRackId,bf.DropTpnSubRackId,bf.DropTpnSlotId,bf.DropTpnLinePortNum,bf.McsSwitchPort,gf.NodeId as GfNodeId from "+ 
					"( select * from McsPortMapping where NetworkId=? and NodeId=?) as bf "+
					"left join ( select * from McsPortMapping where NetworkId=? and NodeId=?) as gf on "+ 
					"(gf.Rack = bf.Rack) and (gf.Sbrack = bf.Sbrack) and (gf.Card = bf.Card) and (gf.McsId = bf.McsId) and (gf.McsAddPortInfo = bf.McsAddPortInfo) and (gf.AddTpnRackId = bf.AddTpnRackId) and (gf.AddTpnSubRackId = bf.AddTpnSubRackId) and (gf.AddTpnSlotId = bf.AddTpnSlotId) "+ 
					") as t where GfNodeId is null;";
        
			MainMap.logger.info(" McsPortMapping_findAllAddedBrField: "+sql);
			List<McsPortMapping> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(McsPortMapping.class),networkidBf,nodeid,networkidGf,nodeid);
			return info;
		}

		public List<McsPortMapping> findAllDeletedBrField(int networkidBf,int networkidGf,int nodeid){
			
			String sql = "select NetworkId, NodeId, Rack,Sbrack, Card, CardType,CardSubType,McsId,McsAddPortInfo,McsDropPortInfo,AddTpnRackId,AddTpnSubRackId,AddTpnSlotId,AddTpnLinePortNum "+ 
					",DropTpnRackId,DropTpnSubRackId,DropTpnSlotId,DropTpnLinePortNum,McsSwitchPort from (select gf.NetworkId , gf.NodeId, gf.Rack,gf.Sbrack, gf.Card, gf.CardType,gf.CardSubType, "+ 
					"gf.McsId,gf.McsAddPortInfo,gf.McsDropPortInfo,gf.AddTpnRackId,gf.AddTpnSubRackId,gf.AddTpnSlotId,gf.AddTpnLinePortNum "+ 
					",gf.DropTpnRackId,gf.DropTpnSubRackId,gf.DropTpnSlotId,gf.DropTpnLinePortNum,gf.McsSwitchPort,bf.NodeId as BfNodeId from "+  
					"( select * from McsPortMapping where NetworkId=? and NodeId=?) as gf "+
					"left join ( select * from McsPortMapping where NetworkId=? and NodeId=?) as bf on "+ 
					"(gf.Rack = bf.Rack) and (gf.Sbrack = bf.Sbrack) and (gf.Card = bf.Card) and (gf.McsId = bf.McsId) and (gf.McsAddPortInfo = bf.McsAddPortInfo) and (gf.AddTpnRackId = bf.AddTpnRackId) and (gf.AddTpnSubRackId = bf.AddTpnSubRackId) and (gf.AddTpnSlotId = bf.AddTpnSlotId) "+ 
					") as t where BfNodeId is null;";
			
			MainMap.logger.info(" McsPortMapping_findAllDeletedBrField: "+sql);
			List<McsPortMapping> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(McsPortMapping.class),networkidGf,nodeid,networkidBf,nodeid);
			return info;
		}
	 
	 /**
	  * select distinct Rack, Sbrack, Card from McsPortMapping where NetworkId= ?;
	  * @param networkid
	  * @param nodeid
	  * @return
	  */
	 public List<McsPortMapping> findDistinctData(int networkid, int nodeid){
			String sql = "select distinct Rack, Sbrack, Card, CardType, CardSubType, McsId from McsPortMapping where NetworkId = ? and NodeId = ?;";
			MainMap.logger.info("findDistinctData: "+sql);
			List<McsPortMapping> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(McsPortMapping.class),networkid,nodeid);
			return info;
	 }	 
	 
	 
	 public List<McsPortMapping> findAllAddDeleteData(int networkid, int nodeid, int rackId, int sbrackId, int cardId){
			String sql = "SELECT * FROM McsPortMapping where NetworkId = ? and NodeId = ? and Rack = ? and Sbrack = ? and  Card = ?";
			MainMap.logger.info("findAllAddDeleteData: "+sql);
			List<McsPortMapping> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(McsPortMapping.class),networkid,nodeid,rackId,sbrackId,cardId);
			return info;
		}	 
	 
	 
	 public void insert(final McsPortMapping info) throws SQLException {         
     String sql = "INSERT into McsPortMapping(NetworkId, NodeId, Rack, Sbrack, Card, CardType, CardSubType, McsId, McsAddPortInfo, McsDropPortInfo, AddTpnRackId, AddTpnSubRackId, AddTpnSlotId, AddTpnLinePortNum, DropTpnRackId, DropTpnSubRackId, DropTpnSlotId, DropTpnLinePortNum,McsSwitchPort) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
//    logger.info("insert: "+sql+info.toString());
     jdbcTemplate.update(
             sql,
             new Object[] { info.getNetworkId(), info.getNodeId(), info.getRack(), info.getSbrack(), info.getCard(), info.getCardType(), info.getCardSubType(), info.getMcsId(), info.getMcsAddPortInfo(), info.getMcsDropPortInfo(), info.getAddTpnRackId(), info.getAddTpnSubRackId(), info.getAddTpnSlotId(), info.getAddTpnLinePortNum(), info.getDropTpnRackId(), info.getDropTpnSubRackId(), info.getDropTpnSlotId(), info.getDropTpnLinePortNum(),info.getMcsSwitchPort() });
	 }
	 
	 public int count()
	 {		 
		  String sql = "select count(*) from McsPortMapping"; 
		 logger.info("count: "+sql);
		  return jdbcTemplate.queryForObject(sql, int.class);		  		        
	 }	 
	 
	 public void deleteByNetworkId(int networkid) 
	 {
	        logger.info("Delete McsPortMapping");
	        String sql = "delete from McsPortMapping where NetworkId = ?";
	        jdbcTemplate.update(sql, networkid);      
	 }
	
}
