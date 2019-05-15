package application.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import application.model.Circuit;
import application.model.Link;
import application.model.LinkWavelength;
import application.model.LinkWavelengthMap;
import application.model.McsMap;
import application.model.WssMap;
import application.MainMap;
import application.constants.ResourcePlanConstants;
import application.model.CardInfo;

@Component
public class WssMapDao{
	@Autowired
    private JdbcTemplate jdbcTemplate;
	public static Logger logger = Logger.getLogger(ResourcePlanConstants.DatabasePrintStr);
		 
	 public List <WssMap> find(int networkid, int nodeid){
			String sql = "SELECT * FROM WssMap where NetworkId = ? and NodeId = ?  ";
			List <WssMap> info;			
			try
	        {
				info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(WssMap.class), networkid, nodeid);		       
	        }
	        catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}	        
			return info;
		}
	 
	 public List <WssMap> find(int networkid, int nodeid,String tpnLoc){
			String sql = "SELECT * FROM WssMap where NetworkId = ? and NodeId = ? and TpnLoc = ? ";
			List <WssMap> info;			
			try
	        {
				info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(WssMap.class), networkid, nodeid,tpnLoc);		       
	        }
	        catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}	        
			return info;
		}
	 
	 public List <Integer> findWssSets(int networkid, int nodeid){
			String sql = "SELECT distinct WssSetNo FROM WssMap where NetworkId = ? and NodeId = ?  ";
			List <Integer> info;			
			try
	        {
				info  = jdbcTemplate.queryForList(sql, Integer.class, networkid, nodeid);		       
	        }
	        catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}	        
			return info;
		}
	 
	 public List <WssMap> find(int networkid, int nodeid, int WssSetNo, String WssLevel1CommonPort){
			String sql = "SELECT * FROM WssMap where NetworkId = ? and NodeId = ? and WssSetNo = ? and WssLevel1CommonPort = ? ";
			List <WssMap> info;			
			try
	        {
				info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(WssMap.class), networkid, nodeid, WssSetNo, WssLevel1CommonPort);		       
	        }
	        catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}	        
			return info;
		}
	 
	 public List <WssMap> findByCommonPort(int networkid, int nodeid, String WssLevel1CommonPort){
			String sql = "SELECT * FROM WssMap where NetworkId = ? and NodeId = ? and WssLevel1CommonPort = ? ";
			List <WssMap> info;			
			try
	        {
				info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(WssMap.class), networkid, nodeid, WssLevel1CommonPort);		       
	        }
	        catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}	        
			return info;
		}
	 

	 
	 public void insert(final WssMap map) throws SQLException 
	 {	     
	     String sql = "INSERT into WssMap(NetworkId, NodeId, Rack, Sbrack, Card, CardType, CardSubtype, WssSetNo, WssLevel2SwitchPort, WssLevel2CommonPort, TpnLoc, TpnLinePortNo,WssLevel1Loc,WssLevel1SwitchPort,WssLevel1CommonPort, EdfaLoc, EdfaId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	     jdbcTemplate.update( sql, new Object[] { map.getNetworkId(), map.getNodeId(), map.getRack(), map.getSbrack(), map.getCard(), map.getCardType(),map.getCardSubtype(), map.getWssSetNo(), map.getWssLevel2SwitchPort(), map.getWssLevel2CommonPort(), map.getTpnLoc(), map.getTpnLinePortNo(), map.getWssLevel1Loc(), map.getWssLevel1SwitchPort(), map.getWssLevel1CommonPort(), map.getEdfaLoc(), map.getEdfaId() });
	 }
	 
	 public Object getMaxSwitchPortId(int networkid, int nodeid,int WssSetNo)
	 {		
		logger.info("getMaxSwitchPortId");
		logger.info("For Node: "+nodeid+" WssSet: "+WssSetNo);
		 String sql = "select MAX(WssLevel2SwitchPort)+1 from WssMap where NetworkId = ? and NodeId = ? and WssSetNo=? ";
		 try
		 {
			 return jdbcTemplate.queryForObject(sql, Integer.class, networkid, nodeid,WssSetNo);
		 }		
		 catch(NullPointerException e) {
			 return  1;
		 }
	 }	
	 
	 public Object getMaxSwitchPortId(int networkid, int nodeid,CardInfo Wss)
	 {		
		logger.info("getMaxSwitchPortId");
		logger.info("For Node: "+nodeid+" WssSet: "+Wss.toString());
		 String sql = "select MAX(WssLevel2SwitchPort)+1 from WssMap where NetworkId = ? and NodeId = ? and Rack=? and Sbrack=? and Card=?";
		 try
		 {
			 return jdbcTemplate.queryForObject(sql, Integer.class, networkid,nodeid,Wss.getRack(),Wss.getSbrack(),Wss.getCard());
		 }		
		 catch(NullPointerException e) {
			 return  1;
		 }
	 }	
	 
	 public List<String> edfaCardsUsed(int networkid, int nodeid)
	 {		
		 	logger.info("checkIdEdfaLocEsists");		
			String sql = "SELECT distinct EdfaLoc from WssMap where NetworkId = ? and NodeId = ? ";
			List<String> list= jdbcTemplate.queryForList(sql, String.class, networkid, nodeid);	
			return list;
	 }
	 
	 public List<WssMap> getWssInfoBySetNo(int networkid, int nodeid, int WssSetNo)
	 {		
		logger.info("getWssInfoBySetNo");
		logger.info("For Node: "+nodeid+" WssSetNo: "+WssSetNo);
		 String sql = "select * from WssMap where NetworkId = ? and NodeId = ? and WssSetNo = ? ";
		 List <WssMap> info;	
		 try
	        {
			 info= jdbcTemplate.query(sql,new BeanPropertyRowMapper(WssMap.class), networkid, nodeid, WssSetNo);
	        }		
	        catch(EmptyResultDataAccessException e) {
	    		return  null;
	    	}	
		 return info;
	 }
	 public List<WssMap> getWssL1EdfaInfoBySetNo(int networkid, int nodeid, int WssSetNo)
	 {		
		logger.info("getWssInfoBySetNo");
		logger.info("For Node: "+nodeid+" WssSetNo: "+WssSetNo);
		 String sql = "select distinct WssLevel1CommonPort,WssLevel1Loc,EdfaLoc from WssMap where NetworkId = ? and NodeId = ? and WssSetNo = ? ";
		 List <WssMap> info;	
		 try
	        {
			 info= jdbcTemplate.query(sql,new BeanPropertyRowMapper(WssMap.class), networkid, nodeid, WssSetNo);
	        }		
	        catch(EmptyResultDataAccessException e) {
	    		return  null;
	    	}	
		 return info;
	 }
	 
	 public List<String> findCommonPortsUsed(int networkid, int nodeid, int WssSetNo)
	 {		
		logger.info("Count Link");
		  String sql = " select distinct WssLevel1CommonPort from WssMap where NetworkId = ? and NodeId = ? and WssSetNo=?;"; 
		  return jdbcTemplate.queryForList(sql, String.class,networkid, nodeid, WssSetNo );		  		        
	 }
	 
	 public List<String> findCommonPortsUsedLowerDegreeDir(int networkid, int nodeid, int WssSetNo)
	 {		
		logger.info("Count Link");
		  String sql = " select distinct WssLevel1CommonPort from WssMap where NetworkId = ? and NodeId = ? and WssSetNo=? and (WssLevel1CommonPort='east' or WssLevel1CommonPort='west' or WssLevel1CommonPort='north' or WssLevel1CommonPort='south');"; 
		  return jdbcTemplate.queryForList(sql, String.class,networkid, nodeid, WssSetNo );		  		        
	 }
	 
	 public List<String> findCommonPortsUsedUpperDegreeDir(int networkid, int nodeid, int WssSetNo)
	 {		
		logger.info("Count Link");
		  String sql = " select distinct WssLevel1CommonPort from WssMap where NetworkId = ? and NodeId = ? and WssSetNo=? and (WssLevel1CommonPort='ne' or WssLevel1CommonPort='nw' or WssLevel1CommonPort='se' or WssLevel1CommonPort='sw');"; 
		  return jdbcTemplate.queryForList(sql, String.class,networkid, nodeid, WssSetNo );		  		        
	 }
	 
	 public void updateEdfaLoc(int networkid, int nodeid, int WssSetNo, String edfaloc, int edfaid) throws SQLException {
		   
	     String sql = "Update WssMap set EdfaLoc = ?, EdfaId = ? where NetworkId = ? and NodeId =? and WssSetNo = ? ";
	    logger.info("updateNode: "+sql); 	     
	     jdbcTemplate.update(
	             sql,
	             new Object[] { edfaloc , edfaid, networkid, nodeid, WssSetNo});
		 	}
	 
	 
	 public void updateEdfaLoc(int networkid, int nodeid, int WssSetNo, int switchport, String edfaloc, int edfaid) throws SQLException {
		   
	     String sql = "Update WssMap set EdfaLoc = ?, EdfaId = ? where NetworkId = ? and NodeId =? and WssSetNo = ? and WssLevel2SwitchPort = ? ";
	    logger.info("updateNode: EdfaLoc:"+edfaid+" EdfaId:"+edfaid+" NetworkId:"+networkid+" NodeId:"+nodeid+" WssSetNo:"+WssSetNo+" WssLevel2SwitchPort:"+switchport); 	     
	     jdbcTemplate.update(
	             sql,
	             new Object[] { edfaloc , edfaid, networkid, nodeid, WssSetNo, switchport});
		 	}
	 
	 
	 
	 public Object getEdfaDirId(int networkid, int nodeid, String EdfaLoc)
	 {		
		logger.info("getEdfaDirId");		
		 String sql = "SELECT concat(WssSetNo,EdfaId) as EdfaDirId from WssMap where NetworkId = ? and NodeId = ? and EdfaLoc = ? group by EdfaDirId ;";
		 try
	        {
			 	return jdbcTemplate.queryForObject(sql, Integer.class, networkid, nodeid, EdfaLoc);
	        }		
	        catch(EmptyResultDataAccessException e) {
	    		return  null;
	    	}	
	 }

	 
	 public void deleteByNetworkId(int networkid) {	       
	        String sql = "delete from WssMap where NetworkId = ?";
	        jdbcTemplate.update(sql, networkid);      
	    }
	 
	 public void deleteWssPort(int networkid,int nodeid,int WssSetNo,int rack, int sbrack,int card,int wssLevel2SwitchPort) {	       
	        String sql = "delete from WssMap where NetworkId = ? and NodeId = ? and WssSetNo = ? and Rack = ? and Sbrack = ? and Card = ? and WssLevel2SwitchPort = ?";
	        jdbcTemplate.update(sql, networkid, nodeid,WssSetNo,rack, sbrack,card,wssLevel2SwitchPort);      
	    }
	 
	 /**
	  * Finds the max WssSetNo being used. WssLevel2SwitchPort is kept as one as WssSetNo is same for the WssLevel2SwitchPort 
	  * @param networkid
	  * @param nodeid
	  * @return
	  */
	 public WssMap findMcsWithMaxWssSetNo(int networkid, int nodeid){
			String sql = "SELECT NetworkId, NodeId, Rack, Sbrack, Card, WssSetNo FROM WssMap where NetworkId = ? and NodeId = ? and WssSetNo = (SELECT MAX(WssSetNo) from WssMap where NetworkId = ? and NodeId = ?) group by NetworkId, NodeId, Rack, Sbrack, Card, WssSetNo ;";
			WssMap info;			
			try
	        {
				info  = (WssMap) jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper(WssMap.class), networkid, nodeid, networkid, nodeid);		       
	        }
	        catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}	        
			return info;
		}
	 
	 public Object checkIdEdfaLocExists(int networkid, int nodeid, int WssSetNo, int wssLevel2SwitchPort)
	 {		
		logger.info("checkIdEdfaLocEsists");		
		 String sql = "SELECT * from WssMap where NetworkId = ? and NodeId = ? and WssSetNo = ? and WssLevel2SwitchPort = ? and (EdfaLoc = NULL) ";
		 try
	        {
			 	return jdbcTemplate.queryForObject(sql, Integer.class, networkid, nodeid, WssSetNo, wssLevel2SwitchPort);
	        }		
	        catch(EmptyResultDataAccessException e) {
	    		return  null;
	    	}	
	 }
	 
	 public WssMap checkIdEdfaLocExists(int networkid, int nodeid, int WssSetNo)
	 {		
		logger.info("checkIdEdfaLocEsists");		
		 String sql = "SELECT * from WssMap where NetworkId = ? and NodeId = ? and WssSetNo = ? and (EdfaLoc IS NULL) ";
		 try
	        {
			 	return (WssMap) jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper(WssMap.class), networkid, nodeid, WssSetNo);
	        }		
	        catch(EmptyResultDataAccessException e) {
	    		return  null;
	    	}	
	 }
	 
	 public void insertWssMapDataInBrField(int networkid, int networkidBF ) throws SQLException {			
		 	
		    String sql = "insert into WssMap (NetworkId, NodeId, Rack, Sbrack, Card,CardType,CardSubtype, WssSetNo, WssLevel2SwitchPort, WssLevel2CommonPort, TpnLoc, TpnLinePortNo,WssLevel1Loc,WssLevel1SwitchPort,WssLevel1CommonPort, EdfaLoc, EdfaId ) select ?, NodeId, Rack, Sbrack, Card,CardType,CardSubtype, WssSetNo, WssLevel2SwitchPort, WssLevel2CommonPort, TpnLoc, TpnLinePortNo,WssLevel1Loc,WssLevel1SwitchPort,WssLevel1CommonPort, EdfaLoc, EdfaId from WssMap where NetworkId = ? ";
		   logger.info("insertWssMapDataInBrField: "+sql); 	     
		    jdbcTemplate.update( sql,networkidBF,networkid);
			 	}
	 
	 public List<WssMap> findAllAddDropData(int networkid, int nodeid, int rackId, int sbrackId, int cardId){
			String sql = "SELECT * FROM WssMap  where NetworkId = ? and NodeId = ? and Rack = ? and Sbrack = ? and  Card = ?";
			MainMap.logger.info("findAllAddDropData: "+sql);
			List<WssMap> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(WssMap .class),networkid,nodeid,rackId,sbrackId,cardId);
			return info;
	}
	 
	 public List <WssMap> findDistinct(int networkid, int nodeid){
			String sql = "SELECT  distinct Rack, Sbrack, Card, CardType, CardSubType, WssSetNo  FROM WssMap where NetworkId = ? and NodeId = ?  ";
			List <WssMap> info;			
			try
	        {
				info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(WssMap.class), networkid, nodeid);		       
	        }
	        catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}	        
			return info;
		}
	

	 }

