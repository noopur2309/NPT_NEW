package application.dao;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import application.model.Circuit;
import application.model.Demand;
import application.model.NetworkRoute;
import application.model.PortInfo;
import application.MainMap;
import application.constants.ResourcePlanConstants;
import application.model.CardInfo;
import application.model.RouteMapping;
import application.model.YCablePortInfo;

@Component
public class YCablePortInfoDao{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	public static Logger logger = Logger.getLogger(ResourcePlanConstants.DatabasePrintStr);

	public Object getNextPortIdForNode(int networkid, int nodeid, int rack, int sbrack, int card) {

		String sql = "SELECT MAX(YCablePort)+1 FROM YCablePortInfo where NetworkId = ? and NodeId = ? and YCableRack =? and YCableSbrack = ? and YCableCard = ? ";
		try
		{
			return jdbcTemplate.queryForObject(sql, Integer.class, networkid,nodeid,rack,sbrack,card);		       
		}
		catch(EmptyResultDataAccessException e) {
			return null;
		}
	}

	public int findUsedPortCount(int networkid, int nodeid, int rack, int sbrack, int card) {

		String sql = "SELECT count(YCablePort) FROM YCablePortInfo where NetworkId = ? and NodeId = ? and YCableRack =? and YCableSbrack = ? and YCableCard = ? ";        
		return jdbcTemplate.queryForObject(sql, int.class,networkid,nodeid,rack,sbrack,card);	        	        
	}   

	public void insertYCablePortInfoInBrField(int networkid, int networkidBF ) throws SQLException {			

		String sql = "insert into YCablePortInfo ( NetworkId, NodeId, YCableRack, YCableSbrack, YCableCard, YCablePort, MpnLocN, MpnPortN, MpnLocP, MpnPortP ) select ?, NodeId, YCableRack, YCableSbrack, YCableCard, YCablePort, MpnLocN, MpnPortN, MpnLocP, MpnPortP from YCablePortInfo where NetworkId = ? ";
		logger.info("insertYCablePortInfoInBrField: "+sql); 	     
		jdbcTemplate.update( sql,networkidBF,networkid);
	} 
	 public int count(int networkId)
	 {		
		 logger.info("Count  YCablePortInfo");
		  String sql = "select count(*) from  YCablePortInfo where NetworkId=?"; 
		  return jdbcTemplate.queryForObject(sql, int.class,networkId);		  		        
	 }

	 public void copyYCablePortInfoInBrField(int networkidGrField, int networkidBrField ) throws SQLException {			
		 	
		    String sql = "insert into YCablePortInfo (NetworkId, NodeId, YCableRack, YCableSbrack, YCableCard, YCablePort, MpnLocN, MpnPortN, MpnLocP, MpnPortP ) select ?, NodeId, YCableRack, YCableSbrack, YCableCard, YCablePort, MpnLocN, MpnPortN, MpnLocP, MpnPortP  from YCablePortInfo where NetworkId = ? ";
		    logger.info("copyYCablePortInfoInBrField: "+sql); 	     
		    jdbcTemplate.update( sql,networkidBrField,networkidGrField);
			 	}
	 
	public void deleteYCablePort(int networkid,int nodeid, String mpnLoc, int mpnPort) {
		logger.info("Query Node: findYCablePort");
		String NodeKey= ""+networkid+"_"+nodeid;

		String sql = "Delete FROM YCablePortInfo where NetworkId = ? and NodeId = ? and MpnLocN = ? and MpnPortN = ? ";                
		logger.info("For Nodekey: "+ NodeKey+"For MpnLocN: "+mpnLoc+" For MpnPortN: "+mpnPort);
		jdbcTemplate.update(sql,networkid,nodeid,mpnLoc,mpnPort);		 
	}


	public List<CardInfo> findCardInfoByCardType(int networkid,int nodeid, String cardtype) {
		logger.info("Query Node: findCardInfoByCardType");
		String NodeKey= ""+networkid+"_"+nodeid;
		String tablename= "CardInfo_"+NodeKey;	
		String sql = "SELECT * FROM " +tablename+" where NodeKey = ? and CardType = ?";	   
		logger.info(sql);
		logger.info("For Nodekey: "+ NodeKey+"For CardType: "+cardtype);
		List<CardInfo> row  = jdbcTemplate.query(sql,new BeanPropertyRowMapper(CardInfo.class),NodeKey,cardtype);			
		return row; 
	}

	
	
	
	
	//	 public List<CardInfo> findAllWss(int networkid,int nodeid) {
	//	        logger.info("Query Node: findAllWss");
	//	        String nodekey = ""+networkid+nodeid;
	//	        String sql = "SELECT * FROM CardInfo where NodeKey = ? and CardType LIKE 'Wss_%' ";	
	//	        logger.info(sql);
	//	        logger.info("For Nodekey: "+ nodekey+"For CardType: "+cardtype);
	//	        List<CardInfo> row  = jdbcTemplate.query(sql,new BeanPropertyRowMapper(CardInfo.class),nodekey,cardtype);			
	//			return row; 
	//	    }

	public List<PortInfo> findPortInfo(int networkid,int nodeid, int rack, int sbrack, int cardid) {
		logger.info("Query Node: findPortInfo");
		String NodeKey= ""+networkid+"_"+nodeid;
		String tablename= "PortInfo_"+NodeKey;
		try
		{
			String sql = "SELECT * FROM " +tablename+" where NodeKey = ? and Rack=? and Sbrack=? and Card=?";	        
			logger.info("For Nodekey: "+ NodeKey+"For CardId: "+cardid);
			List<PortInfo> row  = jdbcTemplate.query(sql,new BeanPropertyRowMapper(PortInfo.class),NodeKey,rack,sbrack,cardid);
			return row; 
		}
		catch(EmptyResultDataAccessException e) {
			return null;
		}
	}

	public PortInfo findDcmModuleByDir(int networkid,int nodeid, String dir) {
		logger.info("Query Node: findPortInfo");
		String NodeKey= ""+networkid+"_"+nodeid;
		String tablename= "PortInfo_"+NodeKey;
		try
		{
			String sql = "SELECT * FROM " +tablename+" where NodeKey = ? and CardType = ? and Direction = ? ";	        
			logger.info("For Nodekey: "+ NodeKey+"For Dir: "+dir);
			PortInfo row  = (PortInfo) jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper(PortInfo.class),NodeKey,ResourcePlanConstants.DCM_Tray_Unit, dir);
			return row; 
		}     
		catch(EmptyResultDataAccessException e) {
			return null;
		}
	}

	public PortInfo findUnassignedDcmModule(int networkid,int nodeid) {
		logger.info("Query Node: findUnassignedDcmModule");
		String NodeKey= ""+networkid+"_"+nodeid;
		String tablename= "PortInfo_"+NodeKey;
		try
		{
			String sql = "SELECT * FROM " +tablename+" where NodeKey = ? and CardType = ? and Direction = '' and Port = (SELECT MIN(Port) from "+tablename+ " where NodeKey = ? and CardType = ? and Direction = '' )";	        
			logger.info("For Nodekey: "+ NodeKey);
			PortInfo row  = (PortInfo) jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper(PortInfo.class),NodeKey, ResourcePlanConstants.DCM_Tray_Unit,NodeKey,ResourcePlanConstants.DCM_Tray_Unit);
			return row; 
		}     
		catch(EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void updateDcmModule(int networkid,int nodeid,int rack, int sbrack, int card,int port,String dir) {
		logger.info("Query Node: updateDcmModule");
		String NodeKey= ""+networkid+"_"+nodeid;
		String tablename= "PortInfo_"+NodeKey;

		String sql = "Update " +tablename+" set Direction = ? where NodeKey = ? and CardType = ? and Rack = ? and Sbrack = ? and Card = ? and Port = ? ";	        
		logger.info("For Nodekey: "+ NodeKey);
		jdbcTemplate.update(sql,new Object[] {dir,NodeKey, ResourcePlanConstants.DCM_Tray_Unit,rack, sbrack, card, port });	        
	}

	public PortInfo findPortInfo(int networkid,int nodeid, int rack, int sbrack, int cardid, int Circuitid) {
		logger.info("Query Node: findPortInfo");
		String NodeKey= ""+networkid+"_"+nodeid;
		String tablename= "PortInfo_"+NodeKey;	
		try
		{
			String sql = "SELECT * FROM " +tablename+" where Rack=? and Sbrack=? and Card=? and Circuitid = ? ";	    
			logger.info(sql);
			logger.info("For Nodekey: "+ NodeKey+" Rack: "+rack+"Sbrack: "+sbrack+" Card: "+cardid+"For Circuitid: "+Circuitid);
			PortInfo row  = (PortInfo) jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper(PortInfo.class),rack,sbrack,cardid,Circuitid);			
			return row; 
		}
		catch(EmptyResultDataAccessException e) {
			return null;
		}
	}


	public int countMpn(int networkid,int nodeid, String dir) {
		MainMap.logger.info("countMpn");		
		logger.info("Query Node: findCardInfo");
		String NodeKey= ""+networkid+"_"+nodeid;
		String tablename= "CardInfo_"+NodeKey;	
		String sql = "SELECT COUNT(*) as cnt FROM " +tablename+" where NodeKey = ?  and CardType LIKE 'MPN%' and Direction = ? ";	    
		//	       logger.info(sql);
		logger.info("For Nodekey: "+ NodeKey+"For Dir: "+dir);
		return jdbcTemplate.queryForObject(sql, int.class,NodeKey,dir);			
	}

	public CardInfo findWss(int networkid,int nodeid, String dir) {	
		MainMap.logger.info("findWss");		 
		String NodeKey= ""+networkid+"_"+nodeid;
		String tablename= "CardInfo_"+NodeKey;	
		String sql = "SELECT * FROM " +tablename+" where NodeKey = ?  and CardType LIKE 'Wss_%'  and Direction = ? ";	    
		//	       logger.info(sql);
		logger.info("For Nodekey: "+ NodeKey+"For Dir: "+dir);
		CardInfo row  = (CardInfo) jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper(CardInfo.class),NodeKey,dir);			
		return row; 
	}

	public List<CardInfo> findMpns(int networkid,int nodeid) {	
		MainMap.logger.info("findWss");		 
		String NodeKey= ""+networkid+"_"+nodeid;
		String tablename= "CardInfo_"+NodeKey;	
		String sql = "SELECT * FROM " +tablename+" where NodeKey = ?  and CardType LIKE 'MPN_%' or 'TPN_%' ";	    
		//	       logger.info(sql);
		logger.info("For Nodekey: "+ NodeKey);
		List<CardInfo> row  = jdbcTemplate.query(sql,new BeanPropertyRowMapper(CardInfo.class),NodeKey);						
		return row; 
	}

	public int countCardByType(int networkid,int nodeid, String cardtype) {	        
		String NodeKey= ""+networkid+"_"+nodeid;
		String tablename= "CardInfo_"+NodeKey;		
		String sql = "SELECT COUNT(*) FROM " +tablename+" where NodeKey = ? and CardType = ?";	   
		logger.info("countCardByType: "+sql);
		logger.info("For Nodekey: "+ NodeKey+"For CardType: "+cardtype);
		int n  = jdbcTemplate.queryForObject(sql, int.class,NodeKey,cardtype);			
		return n; 
	}



	public List<Map<String,Object>> countCardByTypeNEId(int networkid,int nodeid)
	{
		String NodeKey= ""+networkid+"_"+nodeid;
		String tablename= "CardInfo_"+NodeKey;	
		String sql = "SELECT  EquipmentId,  COUNT(1) as CNT FROM "+tablename+" GROUP BY EquipmentId HAVING COUNT(1) >= 1; ";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return list;
	}

	public List<CardInfo> findAll(){
		String sql = "SELECT * FROM CardInfo";
		List<CardInfo> list  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(CardInfo.class));
		return list;
	}


	public List<YCablePortInfo> findAll(int networkid, int nodeid, int cardid){		
		String sql = "SELECT * FROM YCablePortInfo where NetworkId = ? and NodeId = ? and YCableCard = ? ; ";
		List<YCablePortInfo> list  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(YCablePortInfo.class), networkid, nodeid, cardid);
		return list;
	}	

	public List<YCablePortInfo> findAll(int networkid, int nodeid){		
		String sql = "SELECT * FROM YCablePortInfo where NetworkId = ? and NodeId = ? ; ";
		List<YCablePortInfo> list  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(YCablePortInfo.class), networkid, nodeid);
		return list;
	}	

	public List<Map<String,Object>> countPortByTypeNEId(int networkid,int nodeid)
	{
		String NodeKey= ""+networkid+"_"+nodeid;
		String tablename= "PortInfo_"+NodeKey;	
		String sql = "SELECT  EquipmentId,  COUNT(1) as CNT FROM "+tablename+"  GROUP BY EquipmentId HAVING COUNT(1) >= 1; ";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return list;
	}

	public void insert(final YCablePortInfo port) throws SQLException {    
		int id = 0; 

		if(port.getYCablePort()==0)
		{
			Object val = getNextPortIdForNode(port.getNetworkId(),port.getNodeId(),port.getYCableRack(), port.getYCableSbrack(), port.getYCableCard());
			if(val!=null)
			{
				id=Integer.parseInt(val.toString());
			}
			else
			{    	 
				id=1;
			}
		}
		else
		{
			id=port.getYCablePort();
		}
		String sql = "INSERT into YCablePortInfo (NetworkId, NodeId, YCableRack, YCableSbrack, YCableCard, YCablePort, MpnLocN, MpnPortN, MpnLocP, MpnPortP) VALUES ( ?,?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.update(
				sql,
				new Object[] { port.getNetworkId(),port.getNodeId(), port.getYCableRack(),port.getYCableSbrack(),port.getYCableCard(),id,port.getMpnLocN(), port.getMpnPortN(), port.getMpnLocP(),port.getMpnPortP() });
	}	 


	/*
	 * Function deletes all the YCablePortInfo  
	 * */
	public void deleteYCablePortInfo(int networkid) {
		logger.info("Delete deleteYCablePortInfo");	       
		String sql = "Delete from YCablePortInfo where NetworkId = ? ;";
		logger.info(sql);
		jdbcTemplate.update(sql,networkid);
	}

	public void deleteCard(int networkid, int nodeid, int rack , int sbrack,int card) {
		String NodeKey= ""+networkid+"_"+nodeid;
		String tablename= "CardInfo_"+NodeKey;
		String sql = "Delete FROM "+tablename+"  WHERE NodeKey = ? and Rack = ? and Sbrack = ? and Card =? ";
		logger.info(sql);
		jdbcTemplate.update(sql,NodeKey,rack,sbrack,card);
	}

	public void deletePort(int networkid, int nodeid, int rack , int sbrack,int card, int portid) {
		String NodeKey= ""+networkid+"_"+nodeid;
		String tablename= "PortInfo_"+NodeKey;
		String sql = "Delete FROM "+tablename+"  WHERE NodeKey = ? and Rack = ? and Sbrack = ? and Card =? and Port = ? ";
		logger.info(sql);
		jdbcTemplate.update(sql,NodeKey,rack,sbrack,card,portid);
	}
	
	public void deleteycableport(int networkid, int nodeid,  int portid)
	{
		String sql = "Delete from YCablePortInfo WHERE NetworkId = ? and NodeId = ? and MpnPortN = ? ";
		
		jdbcTemplate.update(sql,networkid,nodeid,portid);
	   
		
	}
	
	
}
