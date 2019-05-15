package application.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import application.MainMap;
import application.constants.ResourcePlanConstants;
import application.model.Circuit;
import application.model.McsPortMapping;
import application.model.PtcClientProtInfo;
import application.model.PtcLineProtInfo;
import application.model.WssDirectionConfig;

@Component
public class PtcClientProtInfoDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;	
	public static Logger logger = Logger.getLogger(ResourcePlanConstants.DatabasePrintStr);

	public List<PtcClientProtInfo> findAll(int networkid){
		String sql = "SELECT * FROM PtcClientProtInfo where NetworkId = ?";
		MainMap.logger.info("findAll: "+sql);
		List<PtcClientProtInfo> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(PtcClientProtInfo.class), networkid);
		return info;
	}

	public List<PtcClientProtInfo> findAll(int networkid, int nodeid){
		String sql = "SELECT * FROM PtcClientProtInfo where NetworkId = ? and NodeId = ?";
		MainMap.logger.info("findAll: "+sql);
		List<PtcClientProtInfo> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(PtcClientProtInfo.class),networkid,nodeid);
		return info;
	}
	
	public PtcClientProtInfo findClient(int networkid, int nodeid,int ActMpnRackId,int ActMpnSubrackId,int ActMpnCardId,int ActMpnPort){
		String sql = "SELECT * FROM PtcClientProtInfo where NetworkId = ? and NodeId = ? and ActMpnRackId = ? and ActMpnSubrackId = ? and ActMpncardId = ? and ActMpnPort = ?";
		MainMap.logger.info("findClient: "+sql);
		try
		
		{
		
		PtcClientProtInfo info  = (PtcClientProtInfo) jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper(PtcClientProtInfo.class),networkid,nodeid,ActMpnRackId,ActMpnSubrackId,ActMpnCardId,ActMpnPort);
		return info;
		}
		
		catch(EmptyResultDataAccessException e) {
    		return null;
    	}	
		
	}
	
	
	
	
	
	 public void updatedb(HashMap<String,Object> DifferenceObj,int NetWorkId,int NodeId)
	 {  
		 
		 
		String sql;
	    String  sample = null;
	  
	    
	 Iterator<Map.Entry<String, Object>> itr = DifferenceObj.entrySet().iterator(); 
     
    	 
    	 while(itr.hasNext()) 
    	 
     { 
          Map.Entry<String, Object> entry = itr.next(); 
          if(sample==null)
          {  sample = entry.getKey()+"="+"'"+entry.getValue()+"'";
          
          }
          else 
          {
          sample = sample + entry.getKey()+"="+"'"+entry.getValue()+"'";
          }
          if(itr.hasNext())
          { sample = sample + ",";
          }
     }
        sql = "UPDATE ptcclientprotinfo SET " +" "+sample + " "+" where NetworkId =? and NodeId = ? ;";
        
        logger.info("For Network Id: "+ NetWorkId+ " Node Id: "+NodeId);
        logger.info(sql);
     jdbcTemplate.update(sql,NetWorkId,NodeId);
        
      
     
      
	 }
	 

	public List<PtcClientProtInfo> findAllCommonBrField(int networkidBf,int networkidGf,int nodeid){
		String sql = "select * from (select bf.NetworkId , bf.NodeId , bf.ActMpnRackId, bf.ActMpnSubrackId, bf.ActMpnCardId, "+
				"bf.ActMpnCardType,bf.ActMpnCardSubType,bf.ActMpnPort,bf.ProtMpnRackId,bf.ProtMpnSubrackId,bf.ProtMpnCardId,bf.ProtMpnCardType, "+
				"bf.ProtMpnCardSubType,bf.ProtMpnPort,bf.ProtCardRackId,bf.ProtCardSubrackId,bf.ProtCardCardId,bf.ProtTopology,bf.ProtMechanism,bf.ProtStatus, bf.ProtType ,bf.ActiveLine from "+
				"(select * from PtcClientProtInfo where NetworkId = ? and NodeId=? ) as bf "+
				"left join ( select * from PtcClientProtInfo where NetworkId = ? and NodeId=?) as gf on "+
				"(gf.NodeId = bf.NodeId) a\n" + 
				"	public application.dao.PtcClientProtInfo findClient(int networkid, int nodeid,int ActMpnRackId,int ActMpnSubrackId,int ActMpnCardId,int ActMpnPort){\n" + 
				"		String sql = \"SELECT * FROM PtcClientProtInfo where NetworkId = ? and NodeId = ? and ActMpnRackId = ? and ActMpnSubrackId = ? and ActMpncardId = ? and ActMpnPort = ?\";\n" + 
				"		MainMap.logger.info(\"findClient: \"+sql);\n" + 
				"		application.dao.PtcClientProtInfo info  = (application.dao.PtcClientProtInfo)jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper(application.dao.PtcClientProtInfo.class),networkid,nodeid,ActMpnRackId,ActMpnSubrackId,ActMpnCardId,ActMpnPort);\n" + 
				"		return info;\n" + 
				"	}nd (gf.ActMpnRackId = bf.ActMpnRackId) and (gf.ActMpnSubrackId = bf.ActMpnSubrackId) and (gf.ActMpnCardId = bf.ActMpnCardId) and (gf.ActMpnPort = bf.ActMpnPort) "+ 
				"where bf.ActMpnCardType = gf.ActMpnCardType and "+
				"bf.ActMpnCardSubType = gf.ActMpnCardSubType and "+
				"bf.ActMpnPort = gf.ActMpnPort and "+
				"bf.ProtMpnRackId = gf.ProtMpnRackId and "+ 
				"bf.ProtMpnSubrackId = gf.ProtMpnSubrackId and "+ 
				"bf.ProtMpnCardId = gf.ProtMpnCardId and "+
				"bf.ProtMpnCardType = gf.ProtMpnCardType and "+ 
				"bf.ProtMpnCardSubType = gf.ProtMpnCardSubType and "+ 
				"bf.ProtMpnPort = gf.ProtMpnPort and "+
				"bf.ProtCardRackId = gf.ProtCardRackId and "+ 
				"bf.ProtCardSubrackId = gf.ProtCardSubrackId and "+ 
				"bf.ProtCardCardId = gf.ProtCardCardId and "+
				"bf.ProtTopology = gf.ProtTopology and "+
				"bf.ProtMechanism = gf.ProtMechanism and "+
				"bf.ProtStatus = gf.ProtStatus and "+ 
				"bf.ProtType = gf.ProtType and "+
				"bf.ActiveLine = gf.ActiveLine "+ 
				") As t ;";
//		MainMap.logger.info(" findAllModified: "+sql);
		List<PtcClientProtInfo> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(PtcClientProtInfo.class),networkidBf,nodeid,networkidGf,nodeid);
		return info;
	}

	public List<PtcClientProtInfo> findAllModifiedBrField(int networkidBf,int networkidGf,int nodeid){
		String sql = "select * from (select bf.NetworkId , bf.NodeId , bf.ActMpnRackId, bf.ActMpnSubrackId, bf.ActMpnCardId, "+
				"bf.ActMpnCardType,bf.ActMpnCardSubType,bf.ActMpnPort,bf.ProtMpnRackId,bf.ProtMpnSubrackId,bf.ProtMpnCardId,bf.ProtMpnCardType, "+
				"bf.ProtMpnCardSubType,bf.ProtMpnPort,bf.ProtCardRackId,bf.ProtCardSubrackId,bf.ProtCardCardId,bf.ProtTopology,bf.ProtMechanism,bf.ProtStatus, bf.ProtType ,bf.ActiveLine from  "+
				"(select * from PtcClientProtInfo where NetworkId = ? and NodeId=? ) as bf  "+
				"left join ( select * from PtcClientProtInfo where NetworkId = ? and NodeId=?) as gf on  "+
				"(gf.NodeId = bf.NodeId) and (gf.ActMpnRackId = bf.ActMpnRackId) and (gf.ActMpnSubrackId = bf.ActMpnSubrackId) and (gf.ActMpnCardId = bf.ActMpnCardId) and (gf.ActMpnPort = bf.ActMpnPort) "+ 
				"where bf.ActMpnCardType <> gf.ActMpnCardType or  "+
				"bf.ActMpnCardSubType <> gf.ActMpnCardSubType or  "+
				"bf.ActMpnPort <> gf.ActMpnPort or  "+
				"bf.ProtMpnRackId <> gf.ProtMpnRackId or "+ 
				"bf.ProtMpnSubrackId <> gf.ProtMpnSubrackId or "+ 
				"bf.ProtMpnCardId <> gf.ProtMpnCardId or  "+
				"bf.ProtMpnCardType <> gf.ProtMpnCardType or "+ 
				"bf.ProtMpnCardSubType <> gf.ProtMpnCardSubType or "+ 
				"bf.ProtMpnPort <> gf.ProtMpnPort or  "+
				"bf.ProtCardRackId <> gf.ProtCardRackId or "+ 
				"bf.ProtCardSubrackId <> gf.ProtCardSubrackId or "+ 
				"bf.ProtCardCardId <> gf.ProtCardCardId or "+
				"bf.ProtTopology <> gf.ProtTopology or  "+
				"bf.ProtMechanism <> gf.ProtMechanism or  "+
				"bf.ProtStatus <> gf.ProtStatus or "+ 
				"bf.ProtType <> gf.ProtType or  "+
				"bf.ActiveLine <> gf.ActiveLine "+
				") As t ;";
//		MainMap.logger.info(" findAllModified: "+sql);
		List<PtcClientProtInfo> info;		
        try
        {
        	return info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(PtcClientProtInfo.class),networkidBf,nodeid,networkidGf,nodeid);	       
        }catch(EmptyResultDataAccessException e) {
    		return null;
    	}	
        
	}
	
	public List<PtcClientProtInfo> findAllAddedBrField(int networkidBf,int networkidGf,int nodeid){

		 String sql="select * from (select bf.NetworkId , bf.NodeId , bf.ActMpnRackId, bf.ActMpnSubrackId, bf.ActMpnCardId, "+
				"bf.ActMpnCardType,bf.ActMpnCardSubType,bf.ActMpnPort,bf.ProtMpnRackId,bf.ProtMpnSubrackId,bf.ProtMpnCardId,bf.ProtMpnCardType, "+
				"bf.ProtMpnCardSubType,bf.ProtMpnPort,bf.ProtCardRackId,bf.ProtCardSubrackId,bf.ProtCardCardId,bf.ProtTopology,bf.ProtMechanism,bf.ProtStatus, bf.ProtType ,bf.ActiveLine,gf.NetworkId as NetworkIdGf , "+
				"gf.NodeId as NodeIdGf from ( select * from PtcClientProtInfo where NetworkId = ? and NodeId= ? ) as bf "+ 
				"left join ( select * from PtcClientProtInfo where NetworkId = ? and NodeId= ?) as gf on "+  
                "gf.NodeId = bf.NodeId and gf.ActMpnRackId = bf.ActMpnRackId and gf.ActMpnSubrackId = bf.ActMpnSubrackId and gf.ActMpnCardId = bf.ActMpnCardId and gf.ActMpnPort = bf.ActMpnPort ) "+
				"as t where NodeIdGf is null and NetworkIdGf is null;";
		
		 //MainMap.logger.info(" findAllModified: "+sql);

		List<PtcClientProtInfo> info;		
        try
        {
        	return info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(PtcClientProtInfo.class),networkidBf,nodeid,networkidGf,nodeid);	       
        }
        catch(EmptyResultDataAccessException e) {
    		return null;
    	}	
	}

	public List<PtcClientProtInfo> findAllDeletedBrField(int networkidBf,int networkidGf,int nodeid){
		String sql = "select NetworkId , NodeId , ActMpnRackId, ActMpnSubrackId, ActMpnCardId, "+
				"ActMpnCardType,ActMpnCardSubType,ActMpnPort,ProtMpnRackId,ProtMpnSubrackId,ProtMpnCardId,ProtMpnCardType, "+
				"ProtMpnCardSubType,ProtMpnPort,ProtCardRackId,ProtCardSubrackId,ProtCardCardId,ProtTopology,ProtMechanism,ProtStatus, ProtType ,ActiveLine from (select gf.NetworkId , gf.NodeId , gf.ActMpnRackId, gf.ActMpnSubrackId, gf.ActMpnCardId, "+
				"gf.ActMpnCardType,gf.ActMpnCardSubType,gf.ActMpnPort,gf.ProtMpnRackId,gf.ProtMpnSubrackId,gf.ProtMpnCardId,gf.ProtMpnCardType, "+
				"gf.ProtMpnCardSubType,gf.ProtMpnPort,gf.ProtCardRackId,gf.ProtCardSubrackId,gf.ProtCardCardId,gf.ProtTopology,gf.ProtMechanism,gf.ProtStatus, gf.ProtType ,gf.ActiveLine,bf.NetworkId as NetworkIdBf , "+
				"bf.NodeId as NodeIdBf from ( select * from PtcClientProtInfo where NetworkId = ? and NodeId=?) as gf "+ 
				"left join ( select * from PtcClientProtInfo where NetworkId = ? and NodeId=?) as bf on  "+
                "(gf.NodeId = bf.NodeId) and (gf.ActMpnRackId = bf.ActMpnRackId) and (gf.ActMpnSubrackId = bf.ActMpnSubrackId) and (gf.ActMpnCardId = bf.ActMpnCardId) and (gf.ActMpnPort = bf.ActMpnPort) ) "+
				"as t where NodeIdBf is null and NetworkIdBf is null;";
//		MainMap.logger.info(" findAll Deleted: "+sql);
		List<PtcClientProtInfo> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(PtcClientProtInfo.class),networkidGf,nodeid,networkidBf,nodeid);
		return info;
	}


	public void insert(final PtcClientProtInfo info) throws SQLException {         
		String sql = "INSERT into PtcClientProtInfo(NetworkId, NodeId, ActMpnRackId, ActMpnSubrackId, ActMpnCardId, "
				+ "ActMpnCardType, ActMpnCardSubType, ActMpnPort, ProtMpnRackId, ProtMpnSubrackId, ProtMpnCardId, "
				+ "ProtMpnCardType, ProtMpnCardSubType, ProtMpnPort, ProtCardRackId, ProtCardSubrackId, ProtCardCardId, ProtTopology, "
				+ "ProtMechanism, ProtStatus, ProtType, ActiveLine) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		//    logger.info("insert: "+sql+info.toString());
		jdbcTemplate.update(
				sql,
				new Object[] { info.getNetworkId(), info.getNodeId(), info.getActMpnRackId(), info.getActMpnSubrackId(), 
						info.getActMpnCardId(), info.getActMpnCardType(), info.getActMpnCardSubType(), info.getActMpnPort(), 
						info.getProtMpnRackId(), info.getProtMpnSubrackId(), info.getProtMpnCardId(), info.getProtMpnCardType(),
						info.getProtMpnCardSubType(), info.getProtMpnPort(), info.getProtCardRackId(), info.getProtCardSubrackId(), info.getProtCardCardId(), 
						info.getProtTopology(), info.getProtMechanism(), info.getProtStatus(), info.getProtType(), 
						info.getActiveLine() });
	}
	

	public int count()
	{		 
		String sql = "select count(*) from PtcClientProtInfo"; 
		logger.info("count: "+sql);
		return jdbcTemplate.queryForObject(sql, int.class);		  		        
	}

	public void deleteByNetworkId(int networkid) 
	{
		logger.info("Delete PtcClientProtInfo");
		String sql = "delete from PtcClientProtInfo where NetworkId = ?";
		jdbcTemplate.update(sql, networkid);      
	}

	public void deleteClient(int networkid,int nodeid,int ActMpnRackId, int ActMpnSubrackId,int ActMpnCardid,int ActMpnPort)
	{
		logger.info("Delete PtcClient");	
	  String sql = "Delete from PtcClientProtInfo where NetworkId = ? and NodeId = ? and ActMpnRackId = ? and ActMpnSubrackId = ? and ActMpnCardId = ? and ActMpnPort = ?";
      jdbcTemplate.update(sql, networkid, nodeid,ActMpnRackId,ActMpnSubrackId,ActMpnCardid, ActMpnPort);
		
		
	}
	
	
	
}
