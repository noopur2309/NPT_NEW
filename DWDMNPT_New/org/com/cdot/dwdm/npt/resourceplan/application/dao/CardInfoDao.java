package application.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import application.model.Circuit;
import application.model.Demand;
import application.model.NetworkRoute;
import application.model.Node;
import application.MainMap;
import application.constants.DataPathConfigFileConstants;
import application.constants.MapConstants;
import application.constants.ResourcePlanConstants;
import application.controller.ResourcePlanning;
import application.model.CardInfo;
import application.model.RouteMapping;

@Component
public class CardInfoDao{
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	public static Logger logger = Logger.getLogger(ResourcePlanConstants.DatabasePrintStr);
		 
	 public List<CardInfo> findCardInfoByCardType(int networkid,int nodeid, String cardtype) {
	       logger.info("Query Node: findCardInfoByCardType");
	        String NodeKey= ""+networkid+"_"+nodeid;
	        String tablename = "Cards";
	        String sql = "SELECT * FROM " +tablename+" where NetworkId = ? and NodeId=? and CardType = ?";
	       logger.info(sql);
	       logger.info("For Nodekey: "+ NodeKey+"For CardType: "+cardtype);
	        List<CardInfo> row  = jdbcTemplate.query(sql,new BeanPropertyRowMapper(CardInfo.class),networkid,nodeid,cardtype);			
			return row; 
	    }
	 
	 public List<CardInfo> findWssLevelTwoCards(int networkid,int nodeid) {
	       logger.info("Query Node: findWssLevelTwoCards");
	        String NodeKey= ""+networkid+"_"+nodeid;
//	        String tablename= "Cards";	
	       	String tablename= "Cards";	
	        String sql = "SELECT * FROM " +tablename+" where NetworkId=? and NodeId=? and CardType like '%WSS%' and Direction like '20%' order by Direction ASC";	   
	       logger.info(sql);
	        List<CardInfo> row  = jdbcTemplate.query(sql,new BeanPropertyRowMapper(CardInfo.class),networkid,nodeid);			
			return row; 
	    }
	 
	 public List<CardInfo> findWssLevelOneCards(int networkid,int nodeid) {
	       logger.info("Query Node: findWssLeveloneCards");
	        String NodeKey= ""+networkid+"_"+nodeid;
	        String tablename= "Cards";	
	        String sql = "SELECT * FROM " +tablename+" where NetworkId=? and NodeId=? and CardType like '%WSS%' and Direction like '10%' order by Direction ASC";	   
	       logger.info(sql);
	        List<CardInfo> row  = jdbcTemplate.query(sql,new BeanPropertyRowMapper(CardInfo.class),networkid,nodeid);			
			return row; 
	    }
	 
	 public CardInfo findOLPByCircuitId(int networkid,int nodeid, int circuitid) {
	       logger.info("Query Node: findCardInfoByCardType");
	        String NodeKey= ""+networkid+"_"+nodeid;
	        String tablename= "Cards";	
	        String sql = "SELECT * FROM " +tablename+" where NetworkId=? and NodeId=? and CircuitId = ? and CardType = ? ";	   
	       logger.info(sql);
	       logger.info("For Nodekey: "+ NodeKey+"For CircuitId: "+circuitid);
	        CardInfo row  = (CardInfo) jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper(CardInfo.class),networkid,nodeid,circuitid,ResourcePlanConstants.CardOlp);			
			return row; 
	    }
	 
	 /**
	  * finds the Mpn/Tpn for a particular circuit id in order of status
	  * @param networkid
	  * @param nodeid
	  * @param circuitid
	  * @return
	  */
	 public List <CardInfo> findMpnByCircuitId(int networkid,int nodeid, int circuitid) {
	       logger.info("Query Node: findCardInfoByCardType");
	        String NodeKey= ""+networkid+"_"+nodeid;
	        String tablename= "Cards";	
	        String sql = "SELECT * FROM " +tablename+" where NetworkId=? and NodeId=? and CircuitId = ? and (CardType LIKE 'MPN%' or CardType LIKE 'TPN%') order by Status ";	   
	       logger.info(sql);
	       logger.info("For Nodekey: "+ NodeKey+"For CircuitId: "+circuitid);
	        List <CardInfo> row  = jdbcTemplate.query(sql,new BeanPropertyRowMapper(CardInfo.class),networkid,nodeid,circuitid);			
			return row; 
	    }
	 
//	 public List<CardInfo> findAllWss(int networkid,int nodeid) {
//	       logger.info("Query Node: findAllWss");
//	        String nodekey = ""+networkid+nodeid;
//	        String sql = "SELECT * FROM CardInfo where NetworkId=? and NodeId=? and CardType LIKE 'Wss_%' ";	
//	       logger.info(sql);
//	       logger.info("For Nodekey: "+ nodekey+"For CardType: "+cardtype);
//	        List<CardInfo> row  = jdbcTemplate.query(sql,new BeanPropertyRowMapper(CardInfo.class),networkid,nodeid,cardtype);			
//			return row; 
//	    }
	 
	 public CardInfo findCardInfo(int networkid,int nodeid,int rack, int sbrack, int cardid) {
	       logger.info("Query Node: findCardInfo");
	        String NodeKey= ""+networkid+"_"+nodeid;
	        String tablename= "Cards";
	        try
	        {
		        String sql = "SELECT * FROM " +tablename+" where NetworkId=? and NodeId=? and Rack = ? and Sbrack = ? and Card=? ";	   	        
		       logger.info("For Nodekey: "+ NodeKey+"For CardId: "+cardid);
				CardInfo row  = (CardInfo) jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper(CardInfo.class),networkid,nodeid,rack, sbrack,cardid);			
				return row;
	        }
			catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}
	    }
	 
	 public CardInfo findCardInfoWithEq(int networkid,int nodeid,int rack, int sbrack, int cardid) {
	       logger.info("Query Node: findCardInfo");
	        String NodeKey= ""+networkid+"_"+nodeid;
	        String tablename= "Cards";
	        try
	        {
		        String sql = "SELECT * FROM " +tablename+" where NetworkId=? and NodeId=? and Rack = ? and Sbrack = ? and Card=? and EquipmentId !=0  ";	   	        
		       logger.info("For Nodekey: "+ NodeKey+"For CardId: "+cardid);
				CardInfo row  = (CardInfo) jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper(CardInfo.class),networkid,nodeid,rack, sbrack,cardid);			
				return row;
	        }
			catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}
	    }
	 
	 public CardInfo findCardInfo(int networkid,int nodeid,int demandid, String cardtype) {
	       logger.info("Query Node: findCardInfo");
	        String NodeKey= ""+networkid+"_"+nodeid;
	        String tablename= "Cards";
	        try
	        {
		        String sql = "SELECT * FROM " +tablename+" where NetworkId=? and NodeId=? and DemandId = ? and CardType = ? ";	   	        
		       logger.info("For Nodekey: "+ NodeKey+"For DemandId: "+demandid+" CardType: "+cardtype);
				CardInfo row  = (CardInfo) jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper(CardInfo.class),networkid,nodeid,demandid,cardtype);			
				return row;
	        }
			catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}
	    }
	 
	 public CardInfo findCardInfo(int networkid,int nodeid, String cardtype, String dir) {
	       logger.info("Query Node: findCardInfo");
	        String NodeKey= ""+networkid+"_"+nodeid;
	        String tablename= "Cards";	
	        try
	        {
		        String sql = "SELECT * FROM " +tablename+" where NetworkId=? and NodeId=?  and CardType = ? and Direction = ? ";	    
		       logger.info(sql);
		       logger.info("For Nodekey: "+ NodeKey+"For CardType: "+cardtype+"For Dir: "+dir);
				CardInfo row  = (CardInfo) jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper(CardInfo.class),networkid,nodeid,cardtype, dir);			
				return row; 
	        }
			catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}
	    }
	 
	 public List <CardInfo> findCard(int networkid,int nodeid, String cardtype, String dir) {
	       logger.info("Query Node: findCardInfo");
	        String NodeKey= ""+networkid+"_"+nodeid;
	        String tablename= "Cards";	
	        try
	        {
		        String sql = "SELECT * FROM " +tablename+" where NetworkId=? and NodeId=?  and CardType = ? and Direction = ? order by Rack, Sbrack, Card";	    
		       logger.info(sql);
		       logger.info("For Nodekey: "+ NodeKey+"For CardType: "+cardtype+"For Dir: "+dir);
		        List <CardInfo> row  = jdbcTemplate.query(sql,new BeanPropertyRowMapper(CardInfo.class),networkid,nodeid,cardtype, dir);			
				return row; 
	        }
			catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}
	    }
	 
	 public List<CardInfo> findMpnsByDir(int networkid,int nodeid, String dir) {
	       logger.info("Query Node: findMpnsByDir");
	        String NodeKey= ""+networkid+"_"+nodeid;
	        String tablename= "Cards";	
	        try
	        {
		        String sql = "SELECT * FROM " +tablename+" where NetworkId=? and NodeId=?  and (CardType LIKE 'MPN%' or CardType LIKE 'TPN%') and Direction = ? and Status !='A' ";	    
		       logger.info(sql);
		       logger.info("For Nodekey: "+ NodeKey+" For Dir: "+dir);
		        List<CardInfo> row  =  jdbcTemplate.query(sql,new BeanPropertyRowMapper(CardInfo.class),networkid,nodeid, dir);			
				return row; 
	        }
			catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}
	    }
	 
	 
	  
	 public int countMpn(int networkid,int nodeid, String dir) {
		   logger.info("countMpn");		
	       logger.info("Query Node: findCardInfo");
	        String NodeKey= ""+networkid+"_"+nodeid;
	        String tablename= "Cards";	
	        String sql = "SELECT COUNT(*) as cnt FROM " +tablename+" where NetworkId=? and NodeId=?  and (CardType LIKE 'MPN%' or CardType LIKE '%TPN%') and Direction = ? ";	    
//	       logger.info(sql);
//	       logger.info("For Nodekey: "+ NodeKey+"For Dir: "+dir);
	        return jdbcTemplate.queryForObject(sql, int.class,networkid,nodeid,dir);			
	    }
	 
	 public int countMpn(int networkid,int nodeid) {
		   logger.info("countMpn");		
	       logger.info("Query Node: findCardInfo");
	        String NodeKey= ""+networkid+"_"+nodeid;
	        String tablename= "Cards";	
	        String sql = "SELECT COUNT(*) as cnt FROM " +tablename+" where NetworkId=? and NodeId=?  and (CardType LIKE 'MPN%' or CardType LIKE '%TPN%')";	    
//	       logger.info(sql);
//	       logger.info("For Nodekey: "+ NodeKey+"For Dir: "+dir);
	        return jdbcTemplate.queryForObject(sql, int.class,networkid,nodeid);			
	    }
	 
	 public CardInfo findWss(int networkid,int nodeid, String dir) {	
		 	logger.info("findWss");		 
		 	String NodeKey= ""+networkid+"_"+nodeid;
	        String tablename= "Cards";	
	        try
	        {
//		        String sql = "SELECT * FROM " +tablename+" where NetworkId=? and NodeId=?  and CardType LIKE 'Wss%'  and Direction = ? ";
		        String sql = "SELECT * FROM " +tablename+" where NetworkId=? and NodeId=?  and CardType LIKE '%WSS%'  and Direction = ? ";
	//	       logger.info(sql);
		       logger.info("For Nodekey: "+ NodeKey+"For Dir: "+dir);
				CardInfo row  = (CardInfo) jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper(CardInfo.class),networkid,nodeid,dir);			
				return row; 
	        }
			 catch(EmptyResultDataAccessException e) {
		    		return null;
		    	}
	    }
	 
	 public CardInfo findMpn(int networkid,int nodeid, int demandid, String status) {	
		 	logger.info("findMpn");		 
		 	String NodeKey= ""+networkid+"_"+nodeid;
	        String tablename= "Cards";	
	        try
	        {
		        String sql = "SELECT * FROM " +tablename+" where NetworkId=? and NodeId=?  and (CardType LIKE 'MPN%' or CardType LIKE 'TPN%')  and DemandId = ? and Status = ?";	    
		       logger.info("For Nodekey: "+ NodeKey+"For DemandId: "+demandid+" Status: "+status);
				CardInfo row  = (CardInfo) jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper(CardInfo.class),networkid,nodeid,demandid, status);			
				return row; 
	        }
			 catch(EmptyResultDataAccessException e) {
		    		return null;
		    
			 
			 }
	    }
	 
	 public CardInfo findMpn(int networkid,int nodeid, int demandid,int circuitid, String status) {	
		 	logger.info("findMpn");		 
		 	String NodeKey= ""+networkid+"_"+nodeid;
	        String tablename= "Cards";	
	        try
	        {
		        String sql = "SELECT * FROM " +tablename+" where NetworkId=? and NodeId=?  and (CardType LIKE 'MPN%' or CardType LIKE 'TPN%')  and DemandId = ? and CircuitId = ? and Status = ?";	    
		       logger.info("For Nodekey: "+ NodeKey+"For DemandId: "+demandid+" Status: "+status);
				CardInfo row  = (CardInfo) jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper(CardInfo.class),networkid,nodeid,demandid,circuitid, status);			
				return row; 
	        }
			 catch(EmptyResultDataAccessException e) {
		    		return null;
		    
			 
			 }
	    }
	 
	 /**
	  * Query return both working and protection MPN/TPN for a particular demandid
	  * @param networkid
	  * @param nodeid
	  * @param demandid
	  * @return
	  */
	 
	 public List <CardInfo>  findMpn(int networkid,int nodeid, int demandid) {	
		 	logger.info("findMpn");		 
		 	String NodeKey= ""+networkid+"_"+nodeid;
	        String tablename= "Cards";	
	        try
	        {
		        String sql = "SELECT * FROM " +tablename+" where NetworkId=? and NodeId=?  and (CardType LIKE 'MPN%' or CardType LIKE 'TPN%')  and DemandId = ? ";	    
		       logger.info("For Nodekey: "+ NodeKey+"For Dir: "+demandid);
		        List <CardInfo>  row  = jdbcTemplate.query(sql,new BeanPropertyRowMapper(CardInfo.class),networkid,nodeid,demandid);			
				return row; 
	        }
			 catch(EmptyResultDataAccessException e) {
		    		return null;
		    
			 
			 }
	    }
	 
	 public List <CardInfo> findOLPsWithDemandId(int networkid,int nodeid) {	
		 	logger.info("findOLPsWithDemandId");		 
		 	String NodeKey= ""+networkid+"_"+nodeid;
	        String tablename= "Cards";	
	        try
	        {
		        String sql = "SELECT * FROM " +tablename+" where NetworkId=? and NodeId=?  and (CardType LIKE 'OLP%')  and DemandId !=0 ";	    
		       logger.info("For Nodekey: "+ NodeKey);
		        List <CardInfo> row  = jdbcTemplate.query(sql,new BeanPropertyRowMapper(CardInfo.class),networkid,nodeid);			
				return row; 
	        }
			 catch(EmptyResultDataAccessException e) {
		    		return null;
		    	}
	    }
	 
	 public List <CardInfo> findOLPsForLinkProt(int networkid,int nodeid) {	
		 	logger.info("findOLPsForLinkProt");		 
		 	String NodeKey= ""+networkid+"_"+nodeid;
	        String tablename= "Cards";	
	        try
	        {
		        String sql = "SELECT * FROM " +tablename+" where NetworkId=? and NodeId=? and (CardType LIKE 'OLP%')  and DemandId = 0 and (Direction != '' )";	    
		        List <CardInfo> row  = jdbcTemplate.query(sql,new BeanPropertyRowMapper(CardInfo.class),networkid,nodeid);			
				return row; 
	        }
			 catch(EmptyResultDataAccessException e) {
		    		return null;
		    	}
	    }
	 
	 
		 public  List<Object> findMpnDir(int networkid,int nodeid) {	
		 	logger.info("findMpnDir");		 
		 	String NodeKey= ""+networkid+"_"+nodeid;
	        String tablename= "Cards";	
	        List<Object> dirlist = new ArrayList<Object>();
	        try
	        {
		        String sql = "SELECT distinct Direction FROM " +tablename+" where NetworkId=? and NodeId=?  and (CardType LIKE 'MPN%' or CardType LIKE 'TPN%') ";	    
		       logger.info("For Nodekey: "+ NodeKey);
				SqlRowSet srs  = jdbcTemplate.queryForRowSet(sql, networkid,nodeid);		
				 int rowCount=0;
				 while (srs.next()) 
				 {
					 dirlist.add(srs.getString("Direction"));				     
//				     rowCount++;
				 }
				return dirlist; 
	        }
			 catch(EmptyResultDataAccessException e) {
		    		return null;
		    	}
	    }
	 
	 public List<CardInfo> findMpns(int networkid,int nodeid) {	
		 	logger.info("findMpns");		 
		 	String NodeKey= ""+networkid+"_"+nodeid;
	        String tablename= "Cards";	
	        String sql = "SELECT * FROM " +tablename+" where NetworkId=? and NodeId=?  and (CardType LIKE 'MPN%' or CardType LIKE 'TPN%' ) ";	    
//	       logger.info(sql);
	       logger.info("For Nodekey: "+ NodeKey);
	        List<CardInfo> row  = jdbcTemplate.query(sql,new BeanPropertyRowMapper(CardInfo.class),networkid,nodeid);						
			return row; 
	    }
	 
	 public List<CardInfo> findMpnsExcept5x10G(int networkid,int nodeid) {	
		 	logger.info("findMpns");		 
		 	String NodeKey= ""+networkid+"_"+nodeid;
	        String tablename= "Cards";	
	        String sql = "SELECT * FROM " +tablename+" where NetworkId=? and NodeId=? and (Status=? or Status=?) and (CardType LIKE 'MPN%' or CardType LIKE 'TPN%' ) and CardType NOT LIKE 'TPN 5x10G' ";	    
//	       logger.info(sql);
	       logger.info("For Nodekey: "+ NodeKey);
	        List<CardInfo> row  = jdbcTemplate.query(sql,new BeanPropertyRowMapper(CardInfo.class),networkid,nodeid,ResourcePlanConstants.Working,ResourcePlanConstants.Protection);						
			return row; 
	    }
	 
	 public List<CardInfo> findOcm(int networkid,int nodeid) {		 	
		 	String NodeKey= ""+networkid+"_"+nodeid;
	        String tablename= "Cards";	
	        String sql = "SELECT * FROM " +tablename+" where NetworkId=? and NodeId=?  and CardType LIKE 'Ocm%'   ";	    
	       logger.info("findOcm For Nodekey: "+ NodeKey);
	        List<CardInfo> row  = jdbcTemplate.query(sql,new BeanPropertyRowMapper(CardInfo.class),networkid,nodeid);			
			return row; 
	    }
	 
	 public List<CardInfo> findWss(int networkid,int nodeid) {		 		 
		 	String NodeKey= ""+networkid+"_"+nodeid;
	        String tablename= "Cards";	
	        String sql = "SELECT * FROM " +tablename+" where NetworkId=? and NodeId=?  and CardType LIKE '%WSS%'   ";	
	       logger.info("findWss For Nodekey: "+ NodeKey);
	        List<CardInfo> row  = jdbcTemplate.query(sql,new BeanPropertyRowMapper(CardInfo.class),networkid,nodeid);			
			return row; 
	    }
	 
	 public List<CardInfo> findDirectionWss(int networkid,int nodeid) {		 		 
//		 	String NodeKey= ""+networkid+"_"+nodeid;
	        String tablename= "Cards";	
	        String sql = "SELECT * FROM " +tablename+" where NetworkId=? and NodeId=?  and CardType LIKE '%WSS%'";	
//	       logger.info("findWss For Nodekey: "+ NodeKey);
	        List<CardInfo> row  = jdbcTemplate.query(sql,new BeanPropertyRowMapper(CardInfo.class),networkid,nodeid);		
	        System.out.println("Row before filtering"+row.size());
	        
	        List<CardInfo> filtered=row.stream()
	        .filter(c -> MapConstants.fGetDirectionStrVal(c.getDirection()) > ResourcePlanConstants.ZERO && MapConstants.fGetDirectionStrVal(c.getDirection()) <= ResourcePlanConstants.MaxNodeDegree ).collect(Collectors.toList());
			
	        System.out.println("Row after filtering"+filtered.size());
	        
	        return filtered; 
	    }
	 
	 public int countCardByType(int networkid,int nodeid, String cardtype) {	        
	        String NodeKey= ""+networkid+"_"+nodeid;
		 	String tablename= "Cards";		
	        String sql = "SELECT COUNT(*) FROM " +tablename+" where NetworkId=? and NodeId=? and CardType = ?";	   
	       logger.info("countCardByType: "+sql);
	       logger.info("For Nodekey: "+ NodeKey+"For CardType: "+cardtype);
			int n  = jdbcTemplate.queryForObject(sql, int.class,networkid,nodeid,cardtype);			
			return n; 
	    }
	 
	 
	 
	 public List<Map<String,Object>> countCardByTypeNEId(int networkid,int nodeid)
	 {
		 String NodeKey= ""+networkid+"_"+nodeid;
	 	 String tablename= "Cards";	
		 String sql = "SELECT  EquipmentId,  COUNT(1) as CNT FROM "+tablename+" where NetworkId=? and NodeId=? and EquipmentId != 0 GROUP BY EquipmentId HAVING COUNT(1) >= 1; ";
		 List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,networkid,nodeid);
		 return list;
	 }
	 
	public List<CardInfo> findAll(){
			String sql = "SELECT * FROM CardInfo";
			List<CardInfo> list  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(CardInfo.class));
			return list;
		}
	
	public List<CardInfo> findAll(int networkid,int nodeid){
		String NodeKey= ""+networkid+"_"+nodeid;
		String tablename= "Cards";
		String sql = "SELECT * FROM "+tablename+" where NetworkId=? and NodeId=?";
		List<CardInfo> list  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(CardInfo.class),networkid,nodeid);
		return list;
	}
	 
	 public void insert(final CardInfo info) throws SQLException {
     //logger.info("Insert CardInfo ");
     String tablename="Cards";
     String sql = "INSERT into " +tablename+ " (NetworkId,NodeId, Rack, Sbrack, Card, CardType, Direction, Wavelength, DemandId, EquipmentId, Status, CircuitId, SerialNo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
     jdbcTemplate.update(
             sql,
             new Object[] { info.getNetworkId(),info.getNodeId(), info.getRack(), info.getSbrack(), info.getCard(), info.getCardType(), info.getDirection(), info.getWavelength(), info.getDemandId(), info.getEquipmentId() , info.getStatus(), info.getCircuitId(), info.getSerialNo()});
 }
	 
	 public void updateMpn(final CardInfo info) throws SQLException {
	     //logger.info("Insert CardInfo ");
	     String tablename="CardInfo";
	     String sql = "Update " +tablename+ " set Direction = ?, Wavelength = ? where NetworkId=? and NodeId=? and Rack = ? and Sbrack = ? and Card =? ";
	     jdbcTemplate.update(
	             sql,
	             new Object[] { info.getDirection(), info.getWavelength(),info.getNetworkId(),info.getNodeId(),info.getRack(), info.getSbrack(), info.getCard() });
	 }
	 
	 public void createCardInfoTable(String NodeKey)
	 {		
		 String tablename= "Cards";		
		 String sql = " CREATE TABLE IF NOT EXISTS `DwdmNpt`.`"+tablename+"` ( `NodeKey` VARCHAR(45) NOT NULL, `Rack` INT NOT NULL, `Sbrack` INT NOT NULL, `Card` INT NOT NULL, `CardType` VARCHAR(45) NOT NULL,  `Direction` VARCHAR(45) NOT NULL, `Wavelength` INT NULL, `DemandId` INT NULL, `EquipmentId` INT NULL, `Status` VARCHAR(45) NOT NULL , `CircuitId` VARCHAR(45) NOT NULL  , `SerialNo` VARCHAR(45) NOT NULL, PRIMARY KEY (`NodeKey`, `Rack`, `Sbrack`, `Card`));";
		 jdbcTemplate.execute(sql);
	 }
	 
	 /*
	  * Function deletes all the cardinfo tables 
	  * */
	 public void deleteAllCardInfo(int networkid) {
	       logger.info("Delete deleteAllCardInfo");
	        String tablename = "Cards";
	        String sql = "Delete from "+tablename+" where NetworkId=? ";
//	        String sql = "SELECT table_name FROM information_schema.`TABLES` WHERE table_schema = 'DwdmNpt' AND table_name LIKE '" +namestr+"';";
	       logger.info(sql);
	       jdbcTemplate.update(sql,networkid);
//	        List<Map<String, Object>> names= jdbcTemplate.queryForList(sql);  
//	        
//	        for (Map<String, Object> row : names) {            
//	            String sql1 = "DROP TABLE "+row.get("table_name")+";"; 
//	           logger.info(sql1);
//	            jdbcTemplate.update(sql1);
//	        }
	    }
	 
	 
	 /*
	  * Function deletes the cardinfo table of a particular node 
	  * */
	 public void deleteCardInfo(int networkid, int  nodeid) {
	        MainMap.logger.info("Delete deleteAllCardInfo");
	        String NodeKey= ""+networkid+"_"+nodeid;
	        String tablename= "Cards";
	        String sql = "DROP from " +tablename+" where NetworkId=? and NodeId=?";
	        MainMap.logger.info(sql);
	        jdbcTemplate.update(sql,networkid,nodeid);	       
	    }
	 
	 public void deleteCard(int networkid, int nodeid, int rack , int sbrack,int card) {
		 	String NodeKey= ""+networkid+"_"+nodeid;
	        String tablename= "Cards";
	        String sql = "Delete FROM "+tablename+"  WHERE NetworkId=? and NodeId=? and Rack = ? and Sbrack = ? and Card =? ";
	       logger.info(sql);
	        jdbcTemplate.update(sql,networkid,nodeid,rack,sbrack,card);
	    }
	 
	 public void deleteMpcInSbrack(int networkid, int nodeid, int rack , int sbrack) {
		 	String NodeKey= ""+networkid+"_"+nodeid;
	        String tablename= "Cards";
	        String sql = "Delete FROM "+tablename+"  WHERE NetworkId=? and NodeId=? and Rack = ? and Sbrack = ? and CardType = ? ";
	       logger.info(sql);
	        jdbcTemplate.update(sql,networkid,nodeid,rack,sbrack,ResourcePlanConstants.CardMpc);
	    }
	 
	 public int count()
	 {		
		logger.info("Count CardInfo");
		  String sql = "select count(*) from CardInfo"; 
		  return jdbcTemplate.queryForObject(sql, int.class);		  		        
	 }
	 
	 public int count(int networkId)
	 {		
		logger.info("Count CardInfo");
		  String sql = "select count(*) from CardInfo where NetworkId=?"; 

		  try {
			  return jdbcTemplate.queryForObject(sql, int.class,networkId);	
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}	  		               
	 }
	 
	 
	 /*
	  * @brief returns the list of all racks ,sbracks used in a particular node
	  * */
	 public List<Map<String, Object>>  findSbracks(int networkid, int nodeid){
		 	String NodeKey= ""+networkid+"_"+nodeid;
		 	String tablename= "Cards";		
			String sql = "SELECT distinct Rack, Sbrack FROM "+ tablename +" where NetworkId=? and NodeId=? and Sbrack not in (Select distinct Sbrack from Cards where NetworkId=? and NodeId=? and (CardType='Odd Mux/ Demux Unit' or CardType='Even Mux/Demux Unit'))";
			List<Map<String, Object>>  list  = jdbcTemplate.queryForList(sql,networkid,nodeid,networkid,nodeid);
			return list;
		}
	 	 
	 /*
	  * @brief returns the list of all racks used in a particular node
	  * */
	 public List<Map<String, Object>>  findRacks(int networkid, int nodeid){
		 	String NodeKey= ""+networkid+"_"+nodeid;
		 	String tablename= "Cards";		
			String sql = "SELECT distinct Rack FROM "+ tablename+" where NetworkId=? and NodeId=?";
			List<Map<String, Object>>  list  = jdbcTemplate.queryForList(sql,networkid,nodeid);
			return list;
		}
	 
	 /*
	  * @brief returns the list of all sbracks used in a particular rack of a node
	  * */
	 public List<Map<String, Object>>  findSbracksInRack(int networkid, int nodeid, int rackid){
		 	String NodeKey= ""+networkid+"_"+nodeid;
		 	String tablename= "Cards";		
			String sql = "SELECT distinct Sbrack FROM "+ tablename+ " where NetworkId=? and NodeId=? and Rack = ? and Sbrack != 0 ";
			List<Map<String, Object>>  list  = jdbcTemplate.queryForList(sql,networkid,nodeid,rackid);
			return list;
		}
	 
	 /*
	  * @brief returns the list of all cards in particular sbrack of a node
	  * */
	 public List<CardInfo>  findCardsInSbrack(int networkid, int nodeid, int rackid, int sbrackid){
		 	String NodeKey= ""+networkid+"_"+nodeid;
		 	String tablename= "Cards";		
			String sql = "SELECT * FROM "+ tablename+ " where NetworkId=? and NodeId=? and Rack = ? and Sbrack = ? and EquipmentId != 0 ";
			List<CardInfo>  list  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(CardInfo.class),networkid,nodeid,rackid,sbrackid);
			return list;
		}
	 
	 /*
	  * @brief returns the list of a type of cards in particular sbrack of a node
	  * */
	 public List<CardInfo>  findCardsInSbrack(int networkid, int nodeid, int rackid, int sbrackid, String cardtype){
		 	String NodeKey= ""+networkid+"_"+nodeid;
		 	String tablename= "Cards";		
			String sql = "SELECT * FROM "+ tablename+ " where NetworkId=? and NodeId=? and Rack = ? and Sbrack = ?  and CardType = ? ";
			List<CardInfo>  list  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(CardInfo.class),networkid,nodeid,rackid,sbrackid,cardtype);
			return list;
		}
	 
	 /*
	  * @brief returns the count  of all sbracks used in a particular node
	  * */
	 public int findSbrackCount(int networkid, int nodeid){
		 	String NodeKey= ""+networkid+"_"+nodeid;
		 	String tablename= "Cards";		
			String sql = "SELECT COUNT(*) from (SELECT distinct Rack, Sbrack FROM "+ tablename+" where NetworkId=? and NodeId=?) as count where Sbrack not in (Select distinct Sbrack from Cards where NetworkId=? and NodeId=? and (CardType='Odd Mux/ Demux Unit' or CardType='Even Mux/Demux Unit')) ";
			return jdbcTemplate.queryForObject(sql, int.class,networkid,nodeid,networkid,nodeid);				
		}
	 
	 public int findRackCount(int networkid, int nodeid){
		 	String NodeKey= ""+networkid+"_"+nodeid;
		 	String tablename= "Cards";		
			String sql = "SELECT COUNT(*) from (SELECT distinct Rack FROM "+ tablename+" where NetworkId=? and NodeId=?) as count ";
			return jdbcTemplate.queryForObject(sql, int.class,networkid,nodeid);				
		}
	 public int findSbRackCountInRack(int networkid, int nodeid, int rackid){
		 	String NodeKey= ""+networkid+"_"+nodeid;
		 	String tablename= "Cards";		
			String sql = "SELECT COUNT(*) from (SELECT distinct Rack, Sbrack FROM "+ tablename+" where NetworkId=? and NodeId=?) as count where Rack = ? and Sbrack Sbrack not in (Select distinct Sbrack from cards where NetworkId=? and NodeId=? and (CardType='Odd Mux/ Demux Unit' or CardType='Even Mux/Demux Unit')) ";
			return jdbcTemplate.queryForObject(sql, int.class,networkid,nodeid,rackid,networkid,nodeid);				
		}
	 /**
	  * Api to find the last alloted rack and subrack entry for the node
	  */
	public List<Map<String, Object>> findMaxRackSubrackCount(int networkid, int nodeid, int rackid){
		String NodeKey= ""+networkid+"_"+nodeid;
		String tablename= "Cards";		
		String sql = "SELECT distinct Rack, Sbrack FROM "+ tablename+" where NetworkId=? and NodeId=? and Rack = ? order by Rack desc, Sbrack desc limit 1";

		try {
			List<Map<String, Object>>  list= jdbcTemplate.queryForList(sql,networkid,nodeid,rackid);
			System.out.println(" list =====> "+list);
			return list;
	   } catch (Exception e) {		   
		   return null;
	   }
	}
	 
	 public List<Map<String, Object>> fgetCircuitMatix(int networkid, int nodeid){
		 	String NodeKey= ""+networkid+"_"+nodeid;
		 	String tablename= "Cards";
//		 	String tablenamePort = "PortInfo_"+NodeKey;
		 	String tablenamePort = "Ports";
			String sql = "SELECT nr.WavelengthNo, p.Direction , c.NetworkId,c.NodeId, c.Rack, c.Sbrack, c.Card, p.CircuitId, cd.DemandId, cir.TrafficType, p.Port, " + 
					"cir.LineRate, cir.ClientProtectionType, cir.ProtectionType, cir.RequiredTraffic, c.Status FROM (select * from Cards where NetworkId=? and NodeId=?) c " + 
					"left join (select * from Ports where NetworkId=? and NodeId=?) p on c.NetworkId = p.NetworkId and c.Rack=p.Rack and c.Sbrack= p.Sbrack and c.Card=p.Card " + 
					"left join Circuit cir on p.CircuitId = cir.CircuitId and cir.NetworkId = p.NetworkId " + 
					"left join CircuitDemandMapping cd on p.CircuitId = cd.CircuitId and cd.NetworkId = p.NetworkId " + 
					"left join NetworkRoute nr on nr.NetworkId = cd.NetworkId and nr.DemandId = cd.DemandId and nr.RoutePriority = 1 "+
					"where ( c.CardType LIKE 'MPN%' or c.CardType LIKE 'TPN%') and c.Status = 'W' ; ";
			logger.info("SQL QUERY: "+sql);
			
			try {
				 List<Map<String, Object>>  list= jdbcTemplate.queryForList(sql,networkid,nodeid,networkid,nodeid);
				 return list;
			} catch (Exception e) {
				// TODO: handle exception
				return null;
			}
	
		}
	 
	 public List<Map<String, Object>> fgetWavelenthsNodewise_Rep(int networkid, int nodeid){
		 	String NodeKey= ""+networkid+"_"+nodeid;
		 	String tablename= "Cards";
//		 	String tablenamePort = "PortInfo_"+NodeKey;
		 	String tablenamePort = "Ports";
			String sql = "select  count(*) as Count, Direction from  " +tablenamePort+" p " + 
					"where ( p.NetworkId=? and p.NodeId=? and p.CardType LIKE 'MPN%' or p.CardType LIKE 'TPN%') " + 
					"group by  Direction;";
			logger.info("SQL QUERY: "+sql);
			
			try {
				 List<Map<String, Object>>  list= jdbcTemplate.queryForList(sql,networkid,nodeid);
				 return list;
			} catch (Exception e) {
				// TODO: handle exception
				return null;
			}
	
		}
	 
	 
	 
	 public int fgetNodeWavelength(int networkid, int nodeid){
		 	String NodeKey= ""+networkid+"_"+nodeid;
		 	String tablename= "Cards";
		 	String sql = "select count(Wavelength) from "+tablename+" where NetworkId=? and NodeId=? and Wavelength >0";

		 	return jdbcTemplate.queryForObject(sql, int.class,networkid,nodeid);		
			
			
		}
	 
	 public Integer findfreeSubrackSpaceInRack(int networkid,int nodeid, int rackid) {
//	       logger.info("Query Node: findfreeSubrackSpaceInRack");
	        String NodeKey= ""+networkid+"_"+nodeid;
		 	String tablename= "Cards";
		 	try
	        {	        
			 	String sql = "select MAX(Card) from "+ tablename+" where NetworkId=? and NodeId=? and Sbrack=0 and Rack = ? ";
			 	Integer row  = (Integer) jdbcTemplate.queryForObject(sql,int.class,networkid,nodeid,rackid);			
				return row; 
	        }
			 catch(EmptyResultDataAccessException e) {
		    		return null;
		    	}
	    }
	 
	 public CardInfo findfreeSubrackSpacenew(int networkid,int nodeid) {
	       logger.info("Query Node: findfreeSubrackSpace");
	        String NodeKey= ""+networkid+"_"+nodeid;
		 	String tablename= "Cards";
		 	try
	        {	
		 		String sql = "SELECT * FROM "+ tablename+" WHERE NetworkId=? and NodeId=? and Rack IN(SELECT t.Rack FROM "
		 				+ "(SELECT * FROM "+ tablename+" WHERE NetworkId=? and NodeId=? and Sbrack =0 AND Rack = "
		 						+ "( SELECT MAX(Rack) FROM "+ tablename+" WHERE NetworkId=? and NodeId=? and Sbrack =0) )as t)"
		 						+ "AND Card IN (SELECT MAX(t.Card) FROM (SELECT * FROM "+ tablename+" WHERE"
		 						+ " NetworkId=? and NodeId=? and Sbrack =0 AND Rack =( SELECT MAX(Rack) "
		 						+ "from "+ tablename+" WHERE NetworkId=? and NodeId=? and Sbrack =0) )as t) AND Sbrack =0";			 			
		 		CardInfo row  = (CardInfo) jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper(CardInfo.class),networkid,nodeid,networkid,nodeid,networkid,nodeid,networkid,nodeid,networkid,nodeid);			
				return row; 
	        }
			 catch(EmptyResultDataAccessException e) {
		    		return null;
		    	}	 	 
	    }
	 
	 /**
	  * Query returns the list of src and dest TPN details  for a particular circuit 
	  * having src on a particular node of a network
	  * @param networkid
	  * @param nodeid
	  * @return
	  */
	 
	 public List<Map<String, Object>> fgetTpnDataPerCircuit_cf(int networkid, int nodeid){
		 	String NodeKey= ""+networkid+"_"+nodeid;
		 	String tablename= "Cards";
//		 	String tablenameport= "PortInfo_"+NodeKey;
		 	String tablenameport= "Ports";
		 	String str1 = nodeid+",%";
		    String str2 = "%,"+nodeid;
		    String str3 = "%,"+nodeid+",%";
		    String networkstr = networkid+"%";
//			String sql = "SELECT n.DemandId, n.RoutePriority , n.Path, cdm.CircuitId , cir.SrcNodeId, cir.DestNodeId, cir.TrafficType, c.NodeKey, c.Rack,c.Sbrack,c.Card,c.CardType,t.Stream, t.PortId, t.ProtectionSubType, t.IsRevertive, t.FecType, t.FecStatus, t.TxSegment, t.OperatorSpecific, t.TimDectMode, t.TCMActStatus, t.TCMActValue, t.TxTCMMode, t.TxTCMPriority, t.GccType, t.GccStatus, t.GccValue from NetworkRoute n "
//					+ "left join CircuitDemandMapping cdm on cdm.DemandId=n.DemandId "
//					+ "left join CardInfo c on n.DemandId = c.DemandId"
//					+ " left join Circuit cir on cir.CircuitId = cdm.CircuitId and cir.NetworkId = n.NetworkId "
//					+ "left join TpnPortInfo t on c.Rack=t.Rack and c.Sbrack = t.Sbrack and c.Card=t.Card and t.NetworkId=n.NetworkId and t.NodeId=? "
//					+ "where n.NetworkId = ? and  n.Path LIKE ? and  PATH NOT LIKE ? and n.RoutePriority = 1 and t.Stream = 1 "
//					+ "and cdm.NetworkId = n.NetworkId GROUP BY n.DemandId, n.Path, n.RoutePriority, cir.CircuitId, c.NodeKey;";
//			
		    
		    String sql ="SELECT cir.CircuitId, cir.SrcNodeId, cir.DestNodeId, p.NodeKey, p.Rack, p.Sbrack, "
		    		+ "p.Card, p.Port, c.Status ,t.ProtectionSubType, t.IsRevertive, t.FecType, t.FecStatus, "
		    		+ "t.TxSegment, t.OperatorSpecific, t.TimDectMode, t.TCMActStatus, t.TCMActValue, "
		    		+ "t.TxTCMMode, t.TxTCMPriority, t.GccType, t.GccStatus, t.GccValue from Circuit cir "
		    		+ "left join "+ tablenameport+" p on p.CircuitId=cir.CircuitId and cir.NetworkId = p.NetworkId "
		    		+ "right join (select * from "+ tablename+" where NetworkId=? and NodeId=?) c on c.NetworkId=p.NetworkId and c.Rack=p.Rack and c.Sbrack=p.Sbrack and c.Card=p.Card and c.Status= ? "
		    		+ "left join TpnPortInfo t on p.NetworkId = t.NetworkId and p.NodeId= t.NodeId "
		    		+ "and p.Rack=t.Rack and p.Sbrack= t.Sbrack and p.Card=t.Card and p.Port=t.PortId and t.Stream=1 "
		    		+ "where (cir.SrcNodeId = ? or cir.DestNodeId = ?) and cir.NetworkId = ? order by CircuitId;"; 
			logger.info("SQL QUERY: "+sql);
//			List<Map<String, Object>>  list  = jdbcTemplate.queryForList(sql,  nodeid, networkid,str1,  str3);
			List<Map<String, Object>>  list  = jdbcTemplate.queryForList(sql,networkid,nodeid, ResourcePlanConstants.Working, nodeid, nodeid, networkid);
			return list;
		}
	 
	 public int findCountWorkingMpns(int networkid,int nodeid,String dir) {	
		 	logger.info("findMpns");		 
		 	String NodeKey= ""+networkid+"_"+nodeid;
	        String tablename= "Cards";	
	        String sql = "SELECT count(*) FROM " +tablename+" where NetworkId=? and NodeId=?  and (CardType LIKE 'MPN%' or CardType LIKE 'TPN%') and Direction=? ";	    
	       logger.info(sql);
	       logger.info("For Nodekey: "+ NodeKey);
	       return jdbcTemplate.queryForObject(sql, int.class,networkid,nodeid,dir);						
			 
	    }

public void createViewAllCardInfo(int networkid) {
   logger.info("Create View -----  createViewAllCardInfo");
    String sql2="";
//    String namestr = "CardInfo_"+networkid+"_%";
//    String sql = "SELECT table_name FROM information_schema.`TABLES` WHERE table_schema = 'DwdmNpt' AND table_name LIKE '" +namestr+"';";
//   logger.info(sql);
//    List<Map<String, Object>> names= jdbcTemplate.queryForList(sql);  
//    
//    names.toArray();
//    for (int i = 0; i <  names.toArray().length; i++) {
//    	logger.info("CardInfoDao.createViewAllCardInfo()"+names.get(i).get("table_name"));
//    	sql2 = sql2+"select * from " + names.get(i).get("table_name")+"  UNION ALL ";		
//	}
//    sql2=sql2.substring(0, sql2.length()-10);
    sql2="select * from Cards";
    logger.info("CardInfoDao.my query()"+sql2);
    sql2="CREATE OR REPLACE VIEW CardInfo AS "+sql2;
    jdbcTemplate.execute(sql2);
}

public boolean isSlotFree(int networkid,int nodeid,int rackid, int sbrackid, int cardid) {
    boolean flag = false;
    String NodeKey= ""+networkid+"_"+nodeid;
 	String tablename= "Cards";
 	String sql ;
 	try
    {	
 		sql = "SELECT * FROM "+ tablename+" WHERE NetworkId=? and NodeId=? and Rack = ? and Sbrack = ? and Card = ?";			 			
 		CardInfo row  = (CardInfo) jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper(CardInfo.class),networkid,nodeid,rackid, sbrackid, cardid);
 		
 		flag=false;
 		System.out.println("isSlotFree 1st Try Node:"+nodeid+" Rack:"+rackid+" Subrack:"+sbrackid+" Card:"+cardid);
		return flag;
    }
	 catch(EmptyResultDataAccessException e) {
		 	System.out.println("isSlotFree 1st Catch Node:"+nodeid+" Rack:"+rackid+" Subrack:"+sbrackid+" Card:"+cardid);
		 	try {
		 		sql="Select * from "+tablename+" where NetworkId=? and NodeId=? and Rack = ? and Sbrack = ? and (CardType='"+ResourcePlanConstants.Odd_Mux_Demux_Unit+"' or CardType='"+ResourcePlanConstants.Even_Mux_Demux_Unit+"')"; 
		 		List<CardInfo> muxDemuxList  = jdbcTemplate.query(sql,new BeanPropertyRowMapper(CardInfo.class),networkid,nodeid,rackid, sbrackid);
	 			
		 		if(muxDemuxList.isEmpty()){
		 			System.out.println("isSlotFree 2nd Catch Node:"+nodeid+" Rack:"+rackid+" Subrack:"+sbrackid+" Card:"+cardid);
		 			flag=true;
		 			return flag;
		 		}		 				
		 		
		 		flag=false;
	 			System.out.println("isSlotFree 2nd try Node:"+nodeid+" Rack:"+rackid+" Subrack:"+sbrackid+" Card:"+cardid);
	 			return flag;
			} catch (Exception e2) {
				// TODO: handle exception
				System.out.println("isSlotFree 3rd Catch Node:"+nodeid+" Rack:"+rackid+" Subrack:"+sbrackid+" Card:"+cardid);
				flag=true;
	 			return flag;
			} 
    	}	 	 
}

public void updateNodeKey(int networkid, int nodeid, int networkidBF ) throws SQLException {
	String NodeKey= ""+networkid+"_"+nodeid;
	String NodeKeyBF= ""+networkidBF+"_"+nodeid;
 	String tablename= "CardInfo_"+NodeKeyBF;	   
    String sql = "Update "+ tablename+" set NodeKey = ? where NodeKey = ? ";
   logger.info("updateNode: "+sql); 	     
    jdbcTemplate.update(
            sql,
            new Object[] { NodeKeyBF,NodeKey });
	 	
}

public void insertCardDataInBrField(int networkid, int nodeid, int networkidBF ) throws SQLException {
//	String NodeKey= ""+networkid+"_"+nodeid;
//	String NodeKeyBF= ""+networkidBF+"_"+nodeid;
// 	String tablenameBF= "CardInfo_"+NodeKeyBF;	  
 	String tablename= "Cards";	
    String sql = "insert into Cards (`NetworkId`,\r\n" + 
    		"`NodeId`,\r\n" + 
    		"`Rack`,\r\n" + 
    		"`Sbrack`,\r\n" + 
    		"`Card`,\r\n" + 
    		"`CardType`,\r\n" + 
    		"`Direction`,\r\n" + 
    		"`Wavelength`,\r\n" + 
    		"`DemandId`,\r\n" + 
    		"`EquipmentId`,\r\n" + 
    		"`Status`,\r\n" + 
    		"`CircuitId`,\r\n" + 
    		"`SerialNo`)\r\n" + 
    		"select ?,`NodeId`,\r\n" + 
    		"`Rack`,\r\n" + 
    		"`Sbrack`,\r\n" + 
    		"`Card`,\r\n" + 
    		"`CardType`,\r\n" + 
    		"`Direction`,\r\n" + 
    		"`Wavelength`,\r\n" + 
    		"`DemandId`,\r\n" + 
    		"`EquipmentId`,\r\n" + 
    		"`Status`,\r\n" + 
    		"`CircuitId`,\r\n" + 
    		"`SerialNo` from Cards where NetworkId=? and NodeId=?";
   logger.info("insertCardDataInBrField: "+sql); 	     
    jdbcTemplate.update( sql,networkidBF,networkid,nodeid);
	 	}  


/**
 * finds the list of added/new cards in the brownfield, compares cards on the basis of location and cardtype
 * @param networkidGrField
 * @param networkidBrField
 * @param nodeid
 * @return
 */
public List <CardInfo> findAddedCardsInBrField(int networkidGrField, int networkidBrField, int nodeid, String ... cardType)
{       	       
	String NodeKey= ""+networkidGrField+"_"+nodeid;
	String NodeKeyBF= ""+networkidBrField+"_"+nodeid;
 	String tablenameBF= "CardInfo_"+NodeKeyBF;	  
 	String tablename= "Cards";	
 	String sql;
// 	if(!checkIfTableExists(networkidGrField,nodeid))
//	{
// 		sql = "select * from "+ tablenameBF+";"; 	
//	
//	}
// 	else if(!checkIfTableExists(networkidBrField,nodeid))
// 	{
// 		return null;
// 	}
// 	else
// 	{
 			sql = "select * from " + 
       		"(select bf.NetworkId,bf.NodeId, bf.Rack, bf.Sbrack, bf.Card, bf.CardType, bf.Direction, bf.Wavelength, bf.DemandId, " + 
       		"bf.EquipmentId, bf.Status, bf.CircuitId," + 
       		"gf.NetworkId as NetworkIdGf,gf.NodeId as NodeIdGf, gf.Rack as RackGf,  gf.Sbrack as SbrackGf, gf.Card as CardGf, gf.CardType as CardTypeGf from  " + 
       		"( select * from "+ tablename+" where NetworkId=? and NodeId=?) as bf " + 
       		"left join " + 
       		"( select * from "+ tablename+" where NetworkId=? and NodeId=?) as gf  " + 
       		"on gf.Rack = bf.Rack  and gf.Sbrack = bf.Sbrack and gf.Card = bf.Card and gf.CardType = bf.CardType ) as t " + 
			   " where t.RackGf is NULL and t.SbrackGf is NULL and t.CardGf is NULL and t.CardTypeGf is NULL ";
			   
			if(cardType.length > MapConstants.I_ZERO){

			if(DataPathConfigFileConstants.MuxponderFilter.equalsIgnoreCase( cardType[0].toString())){ /**Filter the query result for MPN and TPN Cards */

				sql+=" and  (CardType LIKE 'MPN%' or CardType LIKE 'TPN%') ";						
			}
		} 
		
// 	}
       List <CardInfo> demands;		
       try
       {
       	return demands  =  jdbcTemplate.query(sql, new BeanPropertyRowMapper(CardInfo.class),networkidBrField,nodeid,networkidGrField,nodeid);	       
       }
       catch(EmptyResultDataAccessException e) {
   		return null;
   	}	
   }

/**
 * finds the list of Deleted Cards in the brownfield 
 * @param networkidGrField
 * @param networkidBrField
 * @param nodeid
 * @return
 */
public List <CardInfo> findDeletedCardsInBrField(int networkidGrField, int networkidBrField, int nodeid, String ... cardType)
{  
	String NodeKey= ""+networkidGrField+"_"+nodeid;
	String NodeKeyBF= ""+networkidBrField+"_"+nodeid;
 	String tablenameBF= "CardInfo_"+NodeKeyBF;	  
 	String tablename= "Cards";
 	String sql;
 	
// 	if(!checkIfTableExists(networkidGrField,nodeid))
//	{
//		return null;
//	
//	}
// 	else if(!checkIfTableExists(networkidBrField,nodeid))
// 	{
// 		//if table does not exist in brown field then return all green field data of the node as deleted data
// 		sql = "select * from "+ tablename+";";
// 	}
// 	else
// 	{
 		sql = "select * from " + 
	 		"(select gf.NetworkId,gf.NodeId, gf.Rack, gf.Sbrack, gf.Card, gf.CardType, gf.Direction, gf.Wavelength, gf.DemandId, gf.EquipmentId, " + 
	 		"gf.Status, gf.CircuitId, " + 
	 		"bf.NetworkId as NetworkIdBf,bf.NodeId as NodeIdBf, bf.Rack as RackBf,  bf.Sbrack as SbrackBf, bf.Card as CardBf, bf.CardType as CardTypeBf from   " + 
	 		"( select * from "+ tablename+" where NetworkId=? and NodeId=? ) as gf  " + 
	 		"left join " + 
	 		"( select * from "+ tablename+" where NetworkId=? and NodeId=? ) as bf  " + 
	 		"on gf.Rack = bf.Rack  and gf.Sbrack = bf.Sbrack and gf.Card = bf.Card and gf.CardType = bf.CardType ) as t " + 
			 " where t.RackBf is NULL and t.SbrackBf is NULL and t.CardBf is NULL and t.CardTypeBf is NULL ";
			 

		if(cardType.length > MapConstants.I_ZERO){

		if(DataPathConfigFileConstants.MuxponderFilter.equalsIgnoreCase( cardType[0].toString())){ /**Filter the query result for MPN and TPN Cards */

			sql+=" and  (CardType LIKE 'MPN%' or CardType LIKE 'TPN%') ";			
			System.out.println(" sql " + sql);			
		}
	} 
// 	}	       
	   List <CardInfo> info;		
	   try
	   {
	   	return info  =  jdbcTemplate.query(sql, new BeanPropertyRowMapper(CardInfo.class),networkidGrField,nodeid,networkidBrField,nodeid);	       
	   }
	   catch(EmptyResultDataAccessException e) {
		return null;
	   }	
 	
   }
   
   
   /**
	  * finds the list of unaltered cards of the brownfield
	  * @param networkidGrField
	  * @param networkidBrField
	  * @param nodeid
	  * @param cardType
 	  * @return
	  */
	 public List<CardInfo> findCommonCardsInBrField(int networkidGrField, int networkidBrField, int nodeid, String ... cardType)
	 {
		 String NodeKey= ""+networkidGrField+"_"+nodeid;
			String NodeKeyBF= ""+networkidBrField+"_"+nodeid;
		 	String tablenameBF= "CardInfo_"+NodeKeyBF;	  
		 	String tablename= "Cards";
		 	
//		 	if(!checkIfTableExists(networkidGrField,nodeid)||!checkIfTableExists(networkidBrField,nodeid))
//		 	{
//		 		return null;
//		 	}
//		 	else
//		 	{
		        String sql = "select * from " + 
		        		"(select bf.NetworkId,bf.NodeId, bf.Rack, bf.Sbrack, bf.Card, bf.CardType, bf.Direction, bf.Wavelength, bf.DemandId, " + 
		        		"bf.EquipmentId, bf.Status, bf.CircuitId, gf.NetworkId as NetworkIdGf,gf.NodeId as NodeIdGf, gf.Rack as RackGf,  gf.Sbrack as SbrackGf, gf.Card as CardGf, gf.CardType as CardTypeGf from   " + 
		        		"( select * from "+ tablename+" where NetworkId=? and NodeId=? ) as bf  " + 
		        		"left join " + 
		        		"( select * from "+ tablename+" where NetworkId=? and NodeId=?) as gf  " + 
		        		"on gf.Rack = bf.Rack  and gf.Sbrack = bf.Sbrack and gf.Card = bf.Card and gf.CardType = bf.CardType ) as t " + 
						" where t.RackGf is NOT NULL and t.SbrackGf is NOT NULL and t.CardGf is NOT NULL and t.CardTypeGf is NOT NULL ";
						

				if(cardType.length > MapConstants.I_ZERO){

					if(DataPathConfigFileConstants.MuxponderFilter.equalsIgnoreCase( cardType[0].toString())){ /**Filter the query result for MPN and TPN Cards */

						sql+=" and  (CardType LIKE 'MPN%' or CardType LIKE 'TPN%') ";						
					}
				} 


		        List<CardInfo> route;		
		        try
		        {
		        	return route  =  jdbcTemplate.query(sql, new BeanPropertyRowMapper(CardInfo.class),networkidBrField,nodeid,networkidGrField,nodeid);	       
		        }
		        catch(EmptyResultDataAccessException e) {
		    		return null;
		    	}	
		 	}
	 
	 public boolean checkIfTableExists(int networkid, int nodeid)
	 {
		boolean flag=true;
		String NodeKey= ""+networkid+"_"+nodeid;		 		  
	 	String tablename= "Cards";
        String sql = "SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='DwdmNpt' AND TABLE_NAME='"+ tablename +"' ;";
        List<CardInfo> info;		
        try
        {
        	info  =  jdbcTemplate.query(sql, new BeanPropertyRowMapper(CardInfo.class));	       
        	if(info.size()>0)
        	{
        		flag = true;
        	}
        	else
        	{
        		flag = false;
        	}
        	logger.info("checkIfTableExists: network: "+networkid+" node: "+nodeid+ "" +flag);
        	return flag;
        }
        catch(EmptyResultDataAccessException e) {
    		return flag = false;
    	}	
    }
	 
	 public List<CardInfo> findNonMPCCardsInSbrack(int networkid,int nodeid, int rack, int sbrack){
			String NodeKey= ""+networkid+"_"+nodeid;
			String tablename= "Cards";
			String sql = "SELECT * FROM "+tablename+" where NetworkId=? and NodeId=? and Rack = ? and Sbrack = ? and CardType NOT LIKE ? ";
			
			List<CardInfo> list;		
	        try
	        {
	        	return list  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(CardInfo.class),networkid,nodeid,rack, sbrack,ResourcePlanConstants.CardMpc);       
	        }
	        catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}	
		}

	 public int findingCountWorkingMpnsForGraphs(int networkid,int nodeid,String dir) {	
		 	logger.info("findMpns");		 
		 	String NodeKey= ""+networkid+"_"+nodeid;
	        String tablename= "Cards";	
	        String sql = "SELECT count(*) FROM " +tablename+" where NetworkId=? and NodeId=?  and (CardType LIKE 'MPN%' or CardType LIKE 'TPN%') and Direction=? and Status LIKE 'W' ";	    
	       logger.info(sql);
	       logger.info("For Nodekey: "+ NodeKey);
	       return jdbcTemplate.queryForObject(sql, int.class,networkid,nodeid,dir);						
			 
		}
		
	public List<CardInfo> findWorkingMpnsForDirectionGraphs(int networkId, int nodeid) {
		
			logger.info("findMpns");		 
			String NodeKey= ""+networkId+"_"+nodeid;
			String tablename= "Cards";
			String sql = "SELECT * FROM " +tablename+" where NetworkId=? and NodeId=? and (CardType LIKE 'MPN%' or CardType LIKE 'TPN%')  and Status LIKE 'W' ";	
			System.out.println("sql : "+sql );
		    logger.info(sql);			
			return jdbcTemplate.query(sql, new BeanPropertyRowMapper(CardInfo.class),networkId,nodeid);	
	}
	
	public List<Map<String, Object>> tempFindAll(int networkid,int nodeid) {
		String NodeKey= ""+networkid+"_"+nodeid;
		String tablename= "CardInfo_"+NodeKey;
		String sql = "SELECT * FROM "+tablename+" where Nodekey=?";
		List<Map<String, Object>> list;
		try {
			list  = jdbcTemplate.queryForList(sql,NodeKey);
		} catch (Exception e) {
			// TODO: handle exception
			list=null;
		}
		return list;
	}
	
	public List<CardInfo> findAmplifierCardCount(int networkId, int nodeid) {
		
		logger.info("findAmplifierCardCount");		 
		String NodeKey= ""+networkId+"_"+nodeid;
//		String tablename= "CardInfo_"+NodeKey;
		String tablename= "Cards";
		String sql = "SELECT * FROM " +tablename+" where NetworkId=? and Nodeid=? and (CardType LIKE 'RAMAN%' or CardType=?)";	
		System.out.println("sql : "+sql );
	    logger.info(sql);			
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper(CardInfo.class),networkId,nodeid,ResourcePlanConstants.CardPaBa);	
}
	
public List<CardInfo> findIlaCardCount(int networkId, int nodeid) {
		
		logger.info("findIlaCardCount");		 
		String NodeKey= ""+networkId+"_"+nodeid;
//		String tablename= "CardInfo_"+NodeKey;
		String tablename= "Cards";
		String sql = "SELECT * FROM " +tablename+" where NetworkId=? and NodeId=? and (CardType LIKE 'ILA%')";	
		System.out.println("sql : "+sql );
	    logger.info(sql);			
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper(CardInfo.class),networkId,nodeid);	
}

//public List<CardInfo> findCardsInDirectionSetOne(int networkId, int nodeid,String cardtype) {
//	
//	logger.info("findCardsInDirectionSetOne");		 
//	String NodeKey= ""+networkId+"_"+nodeid;
////	String tablename= "CardInfo_"+NodeKey;
//	String tablename= "Cards";
//	String sql = "SELECT * FROM " +tablename+" where NetworkId=? and Nodeid=? and CardType=? and ( Direction='east' | Direction='west' |"
//			+ " Direction='north' | Direction='south') ";	
//	System.out.println("sql : "+sql );
//    logger.info(sql);			
//	return jdbcTemplate.query(sql, new BeanPropertyRowMapper(CardInfo.class));	
//}
//
//public List<CardInfo> findCardsInDirectionSetTwo(int networkId, int nodeid,String cardtype) {
//	
//	logger.info("findCardsInDirectionSetTwo");		 
//	String NodeKey= ""+networkId+"_"+nodeid;
//	String tablename= "CardInfo_"+NodeKey;
//	String sql = "SELECT * FROM " +tablename+" where CardType=? and ( Direction='ne' | Direction='nw' |"
//			+ " Direction='sn' | Direction='sw') ";	
//	System.out.println("sql : "+sql );
//    logger.info(sql);			
//	return jdbcTemplate.query(sql, new BeanPropertyRowMapper(CardInfo.class));	
//}
public List<CardInfo> findAllCardsPaBa(int networkid,int nodeid) {
    logger.info("Query Node: findCardInfoByCardType");
     String NodeKey= ""+networkid+"_"+nodeid;
     String tablename = "Cards";
     String sql = "SELECT * FROM " +tablename+" where NetworkId=? and Nodeid=? and (CardType LIKE '%PA/BA%' or CardType LIKE '%RAMAN%')";
    logger.info(sql);
    logger.info("For Nodekey: "+ NodeKey);
     List<CardInfo> row  = jdbcTemplate.query(sql,new BeanPropertyRowMapper(CardInfo.class),networkid,nodeid);			
		return row; 
 }
public List<CardInfo> findAllCardsIla(int networkid,int nodeid) {
    logger.info("Query Node: findCardInfoByCardType");
     String NodeKey= ""+networkid+"_"+nodeid;
     String tablename = "Cards";
     String sql = "SELECT * FROM " +tablename+" where NetworkId=? and Nodeid=? and (CardType LIKE '%ILA%')";
    logger.info(sql);
    logger.info("For Nodekey: "+ NodeKey);
     List<CardInfo> row  = jdbcTemplate.query(sql,new BeanPropertyRowMapper(CardInfo.class),networkid,nodeid);			
		return row; 
 }
}

