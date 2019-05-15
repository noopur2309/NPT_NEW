package application.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import application.MainMap;
import application.constants.DataPathConfigFileConstants;
import application.constants.MapConstants;
import application.constants.ResourcePlanConstants;
import application.model.TpnPortInfo;
import application.model.TpnPortInfo;

@Component
public class TpnPortInfoDao {
	@Autowired
    private JdbcTemplate jdbcTemplate;	
	public static Logger logger = Logger.getLogger(ResourcePlanConstants.DatabasePrintStr);
	
	 public List<TpnPortInfo> findAll(int networkid){
			String sql = "SELECT * FROM TpnPortInfo where NetworkId = ?";
//			MainMap.logger.info("findAll: "+sql);
			List<TpnPortInfo> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(TpnPortInfo.class),networkid);
			return info;
		}
	 
	 public List<TpnPortInfo> findAll(int networkid, int nodeid,int RackId,int Sbrack,int PortId,int Stream)
	 {
		 String sql = "SELECT * FROM TpnPortInfo where NetworkId = ? and NodeId = ? and Rack = ? and Sbrack =? and PortId =? and Stream = ?";
//		MainMap.logger.info("findAll: "+sql);
	  
		 try {
		 List<TpnPortInfo> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(TpnPortInfo.class),networkid,nodeid,RackId,Sbrack,PortId,Stream);
		return info;
		 }
		 catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}	
	}
	 
	 public List<TpnPortInfo> findAll(int networkid, int nodeid){
			String sql = "SELECT * FROM TpnPortInfo where NetworkId = ? NodeId = ?";
//			MainMap.logger.info("findAll: "+sql);
			List<TpnPortInfo> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(TpnPortInfo.class),networkid,nodeid);
			return info;
		}
	 
	 public List<TpnPortInfo> findAll(int networkid, int nodeid,int stream){
			String sql = "SELECT * FROM TpnPortInfo where NetworkId = ? and  NodeId = ? and Stream = ? ";
//			MainMap.logger.info("findAll: "+sql);
			List<TpnPortInfo> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(TpnPortInfo.class),networkid,nodeid,stream);
			return info;
		}
	 
	 
	 public void insert(final TpnPortInfo info) throws SQLException {         
     String sql = "INSERT into TpnPortInfo(NetworkId, NodeId, Rack, Sbrack, Card, Stream, CardType, CardSubType, PortId, "
     		+ "ProtectionSubType, IsRevertive, FecType, FecStatus, TxSegment, OperatorSpecific, TimDectMode, "
     		+ "TCMActStatus, TCMActValue, TxTCMMode, TxTCMPriority, GccType, GccStatus, GccValue) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//    logger.info("insert: "+sql+info.toString());
     jdbcTemplate.update(
             sql,
             new Object[] { info.getNetworkId(), info.getNodeId(), info.getRack(), info.getSbrack(), info.getCard(),info.getStream(), 
            		 info.getCardType(), info.getCardSubType(), info.getPortId(), info.getProtectionSubType(), 
            		 info.getIsRevertive(), info.getFecType(), info.getFecStatus(), info.getTxSegment(), info.getOperatorSpecific(), 
            		 info.getTimDectMode(), info.getTCMActStatus(), info.getTCMActValue(), info.getTxTCMMode(), 
            		 info.getTxTCMPriority(), info.getGccType(), info.getGccStatus(), info.getGccValue() });
	 }
	 
	 public int count()
	 {		 
		  String sql = "select count(*) from TpnPortInfo"; 
		 logger.info("count: "+sql);
		  return jdbcTemplate.queryForObject(sql, int.class);		  		        
	 }	 
	 
	 public void update(int networkid, int nodeid, int rack, int sbrack, int card,  int stream, String OperatorSpecific, int TxTCMMode,int TCMActValue,String TxTCMPriority) throws SQLException {
		   
	     String sql = "Update TpnPortInfo set OperatorSpecific = ? , TxTCMMode = ?, TCMActValue = ?, TxTCMPriority = ? where NetworkId = ? and NodeId =? and Rack = ? and Sbrack = ? and Card = ? and Stream = ? ";
	    logger.info("updateNode: "+sql); 	     
	     jdbcTemplate.update(
	             sql,
	             new Object[] { OperatorSpecific, TxTCMMode, TCMActValue, TxTCMPriority,networkid, nodeid, rack, sbrack, card, stream});
		 	}
	 
	 public void deleteByNetworkId(int networkid) 
	 {
	        logger.info("Delete TpnPortInfo");
	        String sql = "delete from TpnPortInfo where NetworkId = ?";
	        jdbcTemplate.update(sql, networkid);      
	 }

	 public void deleteClient(int networkid,int NodeId,int Rack, int Sbrack,int Card,int Stream,int PortId) 
	 {
	        logger.info("Delete TpnPortInfo");
	        String sql = "delete from TpnPortInfo where NetworkId = ? and NodeId = ? and Rack = ? and Sbrack = ? and Card = ? and Stream = ? and PortId = ? ";
	        jdbcTemplate.update(sql, networkid,NodeId, Rack, Sbrack,Card,Stream,PortId);      
	 }
	 
	 public List<TpnPortInfo> findAll(int networkid, int nodeid, int rack, int sbrack, int card){
			String sql = "SELECT * FROM TpnPortInfo where NetworkId = ? NodeId = ? and Rack = ? and Sbrack = ? and Card = ?";
			MainMap.logger.info("findAll: "+sql);
			List<TpnPortInfo> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(TpnPortInfo.class),networkid,nodeid,rack, sbrack, card);
			return info;
		}
	 
	 public List<TpnPortInfo> findAllCommonBrField(int networkidBf,int networkidGf,int nodeid){
		 	String BfPortInfoTable="TpnPortInfo_"+networkidBf+"_"+nodeid;
			String GfPortInfoTable="TpnPortInfo_"+networkidGf+"_"+nodeid;
			
			String sql = "select * from (select bf.Nodekey , bf.Rack, bf.Sbrack, bf.Card, bf.CardType,bf.Port,bf.LinePort,bf.EquipmentId,bf.CircuitId,bf.DemandId,bf.Direction from "+ 
			"( select * from "+BfPortInfoTable+" ) as bf "+ 			 
             "left join ( select * from "+GfPortInfoTable+" ) as gf on "+              
             "(gf.Nodekey = bf.Nodekey) and (gf.Rack = bf.Rack) and (gf.Sbrack = bf.Sbrack) and (gf.Card = bf.Card) and (gf.Port = bf.Port) "+ 
             "where bf.CardType = gf.CardType and "+
             "bf.LinePort = gf.LinePort and "+
             "bf.EquipmentId = gf.EquipmentId and "+ 
             "bf.CircuitId = gf.CircuitId and "+
             "bf.DemandId = gf.DemandId and "+
             "bf.Direction = gf.Direction "+
             ") as t ;";
			
			MainMap.logger.info(" TpnPortInfo_findAllCommonBrField: "+sql);
			List<TpnPortInfo> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(TpnPortInfo.class));
			return info;
		}

		public List<TpnPortInfo> findAllModifiedBrField(int networkidBf,int networkidGf,int nodeid){
			String BfPortInfoTable="TpnPortInfo_"+networkidBf+"_"+nodeid;
			String GfPortInfoTable="TpnPortInfo_"+networkidGf+"_"+nodeid;
			
			String sql = "select * from (select bf.Nodekey , bf.Rack, bf.Sbrack, bf.Card, bf.CardType,bf.Port,bf.LinePort,bf.EquipmentId,bf.CircuitId,bf.DemandId,bf.Direction from "+ 
					"( select * from "+BfPortInfoTable+" ) as bf "+ 			 
		             "left join ( select * from "+GfPortInfoTable+" ) as gf on "+              
		             "(gf.Nodekey = bf.Nodekey) and (gf.Rack = bf.Rack) and (gf.Sbrack = bf.Sbrack) and (gf.Card = bf.Card) and (gf.Port = bf.Port) "+ 
		             "where bf.CardType <> gf.CardType or "+
		             "bf.LinePort <> gf.LinePort or "+
		             "bf.EquipmentId <> gf.EquipmentId or "+ 
		             "bf.CircuitId <> gf.CircuitId or "+
		             "bf.DemandId <> gf.DemandId or "+
		             "bf.Direction <> gf.Direction "+
		             ") as t ;";
			
			MainMap.logger.info(" TpnPortInfo_findAllModifiedBrField: "+sql);
			List<TpnPortInfo> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(TpnPortInfo.class));
			return info;
		}
		
		public List<TpnPortInfo> findAllAddedBrField(int networkidBf,int networkidGf,int nodeid){
			String BfPortInfoTable="TpnPortInfo_"+networkidBf+"_"+nodeid;
			String GfPortInfoTable="TpnPortInfo_"+networkidGf+"_"+nodeid;
			
			String sql = "select Nodekey , Rack, Sbrack,Card, CardType,Port,LinePort,EquipmentId,CircuitId,DemandId,Direction from (select bf.Nodekey , bf.Rack, bf.Sbrack, bf.Card, bf.CardType,bf.Port,bf.LinePort, "+
			"bf.EquipmentId,bf.CircuitId,bf.DemandId,bf.Direction,gf.NodeKey as GfNodeKey from "+
			"( select * from "+BfPortInfoTable+" ) as bf "+ 			 
             "left join ( select * from "+GfPortInfoTable+" ) as gf on "+              
             "(gf.Rack = bf.Rack) and (gf.Sbrack = bf.Sbrack) and (gf.Card = bf.Card) and (gf.Port = bf.Port) "+ 
             ") as t where GfNodeKey is null;";
           
			MainMap.logger.info(" TpnPortInfo_findAllAddedBrField: "+sql);
			List<TpnPortInfo> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(TpnPortInfo.class));
			return info;
		}

		public List<TpnPortInfo> findAllDeletedBrField(int networkidBf,int networkidGf,int nodeid){
			String BfPortInfoTable="TpnPortInfo_"+networkidBf+"_"+nodeid;
			String GfPortInfoTable="TpnPortInfo_"+networkidGf+"_"+nodeid;
			
			String sql = "select Nodekey , Rack, Sbrack,Card, CardType,Port,LinePort,EquipmentId,CircuitId,DemandId,Direction from (select gf.Nodekey , gf.Rack, gf.Sbrack, gf.Card, gf.CardType,gf.Port,gf.LinePort,gf.EquipmentId,gf.CircuitId,gf.DemandId,gf.Direction,bf.NodeKey as BfNodeKey from "+ 
			"( select * from "+GfPortInfoTable+" ) as gf "+ 			 
             "left join ( select * from "+BfPortInfoTable+" ) as bf on "+              
             "(gf.Rack = bf.Rack) and (gf.Sbrack = bf.Sbrack) and (gf.Card = bf.Card) and (gf.Port = bf.Port) "+ 
             ") as t where BfNodeKey is null;";
			
			MainMap.logger.info(" TpnPortInfo_findAllDeletedBrField: "+sql);
			List<TpnPortInfo> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(TpnPortInfo.class));
			return info;
		}
	 
	 /**
	  * To find out the portList for particular circuit
	  * @date  6th Feb, 2018
	  * @author hp
	  * @param networkid
	  * @param nodeid
	  * @param rack
	  * @param sbrack
	  * @param card
	  * @param portId
	  * @param Stream
	  * @return
	  */
	 public List<TpnPortInfo> findAll(int networkid, int nodeid, int rack, int sbrack, int card, int portId, int Stream, int... typeOtnLsp){
			String sql = "select * from TpnPortInfo where NetworkId=? and NodeId=? and Rack=? and Sbrack=? and Card=? and PortId=? and Stream=?";
			
			if(typeOtnLsp.length != MapConstants.I_ZERO){

				if(typeOtnLsp[0] == DataPathConfigFileConstants.XGMClientSideOtnLsp){
					String cardType = "\""+ ResourcePlanConstants.CardMuxponder10G+"\"";
					sql += " and CardType = "+cardType;
				}
				

			}


			MainMap.logger.info("findAll: "+sql);
			List<TpnPortInfo> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(TpnPortInfo.class),networkid,nodeid,rack, sbrack, card, portId, Stream);
			return info;
	}



	public List<TpnPortInfo> findAll10GLine(int networkid, int nodeid, int rack, int sbrack, int card, int Stream){
		String sql = 
					"select TpnPortInfo.NetworkId, TpnPortInfo.NodeId, TpnPortInfo.Rack, TpnPortInfo.Sbrack, TpnPortInfo.Card, "+
					" TpnPortInfo.Stream, TpnPortInfo.CardType,TpnPortInfo.CardSubType,Ports.LinePort as PortId, "+
					" TpnPortInfo.PRotectionSubType, TpnPortInfo.IsRevertive, TpnPortInfo.FecType, TpnPortInfo.FecStatus, "+
					" TpnPortInfo.TxSegment,TpnPortInfo.OperatorSpecific,TpnPortInfo.TimDectMode,TpnPortInfo.TCMActStatus, "+
					" TpnPortInfo.TCMActValue,TpnPortInfo.TxTCMMode,TpnPortInfo.TxTCMPriority,TpnPortInfo.GccType, "+
					" TpnPortInfo.GccStatus,TpnPortInfo.GccValue "+
					
					" from TpnPortInfo "+
					" LEFT JOIN "+
					" Ports  "+

					"    on   TpnPortInfo.Rack=Ports.Rack and TpnPortInfo.Sbrack = Ports.Sbrack  and  TpnPortInfo.Card=Ports.Card  and "+
					" 	TpnPortInfo.CardType = Ports.CardType "+
					"   where TpnPortInfo.NetworkId = ? and TpnPortInfo.NodeId= ? and TpnPortInfo.Rack= ? and TpnPortInfo.Sbrack=? and TpnPortInfo.Card=? "+
					" 	and TpnPortInfo.CardType='"+ResourcePlanConstants.CardMuxponder10G+"' and TpnPortInfo.Stream=? ";
						
		MainMap.logger.info("findAll10GLine: "+sql);
		System.out.println(networkid+";"+nodeid+";"+rack+";"+sbrack+";"+card+";"+Stream);		
		List<TpnPortInfo> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(TpnPortInfo.class),networkid,nodeid,rack, sbrack, card,
													 Stream);
		System.out.println("---------INFO---------"+info);
		return info;
	}



	
}
