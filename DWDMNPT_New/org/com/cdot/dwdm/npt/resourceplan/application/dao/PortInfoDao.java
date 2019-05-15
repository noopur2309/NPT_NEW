package application.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sound.sampled.Port;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import application.model.PortInfo;
import application.model.TpnPortInfo;
import application.MainMap;
import application.constants.DataPathConfigFileConstants;
import application.constants.ResourcePlanConstants;
import application.model.AggregatorClientMap;
import application.model.CardInfo;

@Component
public class PortInfoDao{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	public static Logger logger = Logger.getLogger(ResourcePlanConstants.DatabasePrintStr);

	public Object getNextPortIdForNode(int networkid, int nodeid, int rack, int sbrack, int card) {
		String NodeKey= ""+networkid+"_"+nodeid;
		//	        String tablename= "PortInfo_"+NodeKey;
		String tablename= "Ports";
		String sql = "SELECT MAX(Port)+1 FROM "+tablename+" where NetworkId = ? and NodeId = ? and Rack =? and Sbrack = ? and Card = ?";
		try
		{
			return jdbcTemplate.queryForObject(sql, Integer.class, networkid,nodeid,rack,sbrack,card);		       
		}
		catch(EmptyResultDataAccessException e) {
			return null;
		}	        
	}



	public PortInfo findMax5x10GPort(int networkid,int nodeid) {
		logger.info("Query Node: findMax5x10GPort");
		String NodeKey= ""+networkid+"_"+nodeid;                                                                                               
		//	        String tablename= "PortInfo_"+NodeKey;	
		String tablename= "Ports";
		try
		{
			//		        String sql = "  Select * From "+tablename+" where port IN (Select Max(port) from (Select  * From "+tablename+" where NetworkId = ? and NodeId = ? and CardType LIKE ? ) as t ) ";
			String sql = "   Select Rack, Sbrack, Card, CardType, group_concat(Port) as Port " + 
					"From "+tablename+" where NetworkId = ? and NodeId = ? and CardType LIKE ? group by Rack,Sbrack,Card,CardType having count(*)!=5  ";

			logger.info(sql);
			logger.info("findMax5x10GPort For Nodekey: "+ NodeKey);
			Map<String, Object> row  = jdbcTemplate.queryForMap(sql,networkid,nodeid,ResourcePlanConstants.CardTPN5x10G);	
			if(row.get("Card")!=null) {
				PortInfo port = new PortInfo(networkid,nodeid,Integer.parseInt(row.get("Rack").toString()),Integer.parseInt(row.get("Sbrack").toString()),Integer.parseInt(row.get("Card").toString()),"",0,0,0,0,0,"",0);
				return port; 
			}
			else
				return null;
		}
		catch(EmptyResultDataAccessException e) {
			return null;
		}
	}

	public PortInfo findLineDirectionInfo(int networkid,int nodeid, int rack,int subrack,int card,int linePort) {
		logger.info("Query Node: findCardInfoByCardType");
		String NodeKey= ""+networkid+"_"+nodeid;
		//	        String tablename= "PortInfo_"+NodeKey;	
		String tablename= "Ports";
		String sql = "SELECT distinct Direction FROM " +tablename+" where NetworkId = ? and NodeId = ? and Rack = ? and Sbrack=? and Card=? and LinePort = ? ";	   
		logger.info(sql);
		PortInfo row= (PortInfo)jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper(PortInfo.class),networkid,nodeid,rack,subrack,card,linePort);			
		return row; 
	}

	public List<PortInfo> findMpnPortInfo(int networkid,int nodeid) {
		logger.info("Query Node: findMpnPortInfo");
		String NodeKey= ""+networkid+"_"+nodeid;
		//	        String tablename= "PortInfo_"+NodeKey;	
		String tablename= "Ports";
		String sql = "SELECT * FROM " +tablename+" where NetworkId = ? and NodeId = ? and ( CardType LIKE 'MPN_%' or CardType LIKE 'TPN_%' ) and CircuitId is ";	   
		logger.info(sql);
		logger.info("For Nodekey: "+ NodeKey);
		List<PortInfo> row  = jdbcTemplate.query(sql,new BeanPropertyRowMapper(PortInfo.class),networkid,nodeid);			
		return row; 
	}

	//	 public List<CardInfo> findAllWss(int networkid,int nodeid) {
	//	       logger.info("Query Node: findAllWss");
	//	        String nodekey = ""+networkid+nodeid;
	//	        String sql = "SELECT * FROM CardInfo where NetworkId = ? and NodeId = ? and CardType LIKE 'Wss_%' ";	
	//	       logger.info(sql);
	//	       logger.info("For Nodekey: "+ nodekey+"For CardType: "+cardtype);
	//	        List<CardInfo> row  = jdbcTemplate.query(sql,new BeanPropertyRowMapper(CardInfo.class),networkid,nodeid,cardtype);			
	//			return row; 
	//	    }

	public List<PortInfo> findPortInfo(int networkid,int nodeid, int rack, int sbrack, int cardid, boolean ...optional) {
		logger.info("Query Node: findPortInfo");
		String NodeKey= ""+networkid+"_"+nodeid;
		//	        String tablename= "PortInfo_"+NodeKey;
		String tablename= "Ports";
		try
		{
			String sql = "SELECT * FROM " +tablename+" where NetworkId = ? and NodeId = ? and Rack=? and Sbrack=? and Card=?";
			if(optional.length==1) {

				sql = "SELECT  distinct Port,CircuitId FROM " +tablename+" where NetworkId = ? and NodeId = ? and Rack=? and Sbrack=? and Card=?"; 
			}
			else if(optional.length==2)
			{
				sql = "SELECT  distinct LinePort FROM " +tablename+" where NetworkId = ? and NodeId = ? and Rack=? and Sbrack=? and Card=?"; 
			}

			logger.info("For Nodekey: "+ NodeKey+"For CardId: "+cardid);
			List<PortInfo> row  = jdbcTemplate.query(sql,new BeanPropertyRowMapper(PortInfo.class),networkid,nodeid,rack,sbrack,cardid);
			return row; 
		}
		catch(EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<PortInfo> findPortInfo(int networkid,int nodeid, int demandid) {
		logger.info("Query Node: findPortInfo");
		String NodeKey= ""+networkid+"_"+nodeid;
		//	        String tablename= "PortInfo_"+NodeKey;
		String tablename= "Ports";
		try
		{
			String sql = "SELECT * FROM " +tablename+" where NetworkId = ? and NodeId = ? and DemandId = ?";	        
			logger.info("For Nodekey: "+ NodeKey+"For DemandId: "+demandid);
			List<PortInfo> row  = jdbcTemplate.query(sql,new BeanPropertyRowMapper(PortInfo.class),networkid,nodeid,demandid);
			return row; 
		}
		catch(EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<PortInfo> findPortInfobyPortid(int networkid,int nodeid,int PortId, int demandid) {
		logger.info("Query Node: findPortInfo");
		String NodeKey= ""+networkid+"_"+nodeid;
		//	        String tablename= "PortInfo_"+NodeKey;
		String tablename= "Ports";
		try
		{
			String sql = "SELECT * FROM " +tablename+" where NetworkId = ? and NodeId = ? and Port = ? and DemandId = ?";	        
			logger.info("For Nodekey: "+ NodeKey+"For DemandId: "+demandid);
			List<PortInfo> row  = jdbcTemplate.query(sql,new BeanPropertyRowMapper(PortInfo.class),networkid,nodeid,PortId,demandid);
			return row; 
		}
		catch(EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<Integer> findPorts(int networkid, int nodeid,int Rack , int Sbrack, int demandid)
	{
		
		logger.info("Query Node: findPortInfo");
	String NodeKey= ""+networkid+"_"+nodeid;
	//	        String tablename= "PortInfo_"+NodeKey;
	String tablename= "Ports";
	try
	{
		String sql = "SELECT distinct(Port)  FROM " +tablename+" where NetworkId = ? and NodeId = ? and Rack = ? and Sbrack = ? and DemandId = ?";	        
		logger.info("For Nodekey: "+ NodeKey+"For DemandId: "+demandid);
		List<Integer> row  = jdbcTemplate.queryForList(sql,Integer.class,networkid,nodeid,Rack,Sbrack,demandid);
		return row; 
	}
	catch(EmptyResultDataAccessException e) {
		return null;
	}
}
	
	
	
	
	public List<PortInfo> findDistinctPortInfoByDemandId(int networkid,int nodeid, int demandid) {
		logger.info("Query Node: findPortInfo");
		String NodeKey= ""+networkid+"_"+nodeid;
		//	        String tablename= "PortInfo_"+NodeKey;
		String tablename= "Ports";
		try
		{
			String sql = "SELECT * FROM " +tablename+" where NetworkId = ? and NodeId = ? and DemandId = ?";	        
			//		        String sql = "SELECT distinct Rack,Sbrack,Card,CardType,LinePort,Direction,DemandId FROM "+tablename+" where NetworkId = ? and NodeId = ? and DemandId=?";
			logger.info("For Nodekey: "+ NodeKey+"For DemandId: "+demandid);
			List<PortInfo> row  = jdbcTemplate.query(sql,new BeanPropertyRowMapper(PortInfo.class),networkid,nodeid,demandid);
			return row; 
		}
		catch(EmptyResultDataAccessException e) {
			return null;
		}
	}

	public PortInfo findDcmModuleByDir(int networkid,int nodeid, String dir) {
		logger.info("Query Node: findPortInfo");
		String NodeKey= ""+networkid+"_"+nodeid;
		//	        String tablename= "PortInfo_"+NodeKey;
		String tablename= "Ports";
		try
		{
			String sql = "SELECT * FROM " +tablename+" where NetworkId = ? and NodeId = ? and CardType = ? and Direction = ? ";	        
			logger.info("For Nodekey: "+ NodeKey+"For Dir: "+dir);
			PortInfo row  = (PortInfo) jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper(PortInfo.class),networkid,nodeid,ResourcePlanConstants.DCM_Tray_Unit, dir);
			return row; 
		}     
		catch(EmptyResultDataAccessException e) {
			return null;
		}
	}	 


	public PortInfo findUnassignedDcmModule(int networkid,int nodeid) {
		logger.info("Query Node: findUnassignedDcmModule");
		String NodeKey= ""+networkid+"_"+nodeid;
		//	        String tablename= "PortInfo_"+NodeKey;
		String tablename= "Ports";
		try
		{
			String sql = "SELECT * FROM " +tablename+" where NetworkId = ? and NodeId = ? and CardType = ? and Direction = '' and Port = (SELECT MIN(Port) from "+tablename+ " where NetworkId = ? and NodeId = ? and CardType = ? and Direction = '' )";	        
			logger.info("For Nodekey: "+ NodeKey);
			PortInfo row  = (PortInfo) jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper(PortInfo.class),networkid,nodeid, ResourcePlanConstants.DCM_Tray_Unit,networkid,nodeid,ResourcePlanConstants.DCM_Tray_Unit);
			return row; 
		}     
		catch(EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void updateDcmModule(int networkid,int nodeid,int rack, int sbrack, int card,int port,String dir) {
		logger.info("Query Node: updateDcmModule");
		String NodeKey= ""+networkid+"_"+nodeid;
		//	        String tablename= "PortInfo_"+NodeKey;
		String tablename= "Ports";

		String sql = "Update " +tablename+" set Direction = ? where NetworkId = ? and NodeId = ? and CardType = ? and Rack = ? and Sbrack = ? and Card = ? and Port = ? ";	        
		logger.info("For Nodekey: "+ NodeKey);
		jdbcTemplate.update(sql,new Object[] {dir,networkid,nodeid, ResourcePlanConstants.DCM_Tray_Unit,rack, sbrack, card, port });	        
	}

	public PortInfo findPortInfo(int networkid,int nodeid, int rack, int sbrack, int cardid, int Circuitid) {
		logger.info("Query Node: findPortInfo");
		String NodeKey= ""+networkid+"_"+nodeid;
		//	        String tablename= "PortInfo_"+NodeKey;	
		String tablename= "Ports";
		try
		{
			String sql = "SELECT * FROM " +tablename+" where NetworkId=? and NodeId=? and Rack=? and Sbrack=? and Card=? and Circuitid = ? ";	    
			logger.info(sql);
			logger.info("For Nodekey: "+ NodeKey+" Rack: "+rack+"Sbrack: "+sbrack+" Card: "+cardid+"For Circuitid: "+Circuitid);
			PortInfo row  = (PortInfo) jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper(PortInfo.class),networkid,nodeid,rack,sbrack,cardid,Circuitid);			
			return row; 
		}
		catch(EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<PortInfo> findPortInfoList(int networkid,int nodeid, int rack, int sbrack, int cardid, int Circuitid) {
		logger.info("Query Node: findPortInfo");
		String NodeKey= ""+networkid+"_"+nodeid;
		//	        String tablename= "PortInfo_"+NodeKey;	
		String tablename= "Ports";
		try
		{
			String sql = "SELECT * FROM " +tablename+" where NetworkId=? and NodeId=? and Rack=? and Sbrack=? and Card=? and Circuitid = ? ";	    
			logger.info(sql);
			logger.info("For Nodekey: "+ NodeKey+" Rack: "+rack+"Sbrack: "+sbrack+" Card: "+cardid+"For Circuitid: "+Circuitid);
			List<PortInfo> row  =jdbcTemplate.query(sql,new BeanPropertyRowMapper(PortInfo.class),networkid,nodeid,rack,sbrack,cardid,Circuitid);			
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
		//	        String tablename= "CardInfo_"+NodeKey;	
		String tablename= "Cards";
		String sql = "SELECT COUNT(*) as cnt FROM " +tablename+" where NetworkId = ? and NodeId = ?  and CardType LIKE 'MPN%' and Direction = ? ";	    
		//	       logger.info(sql);
		logger.info("For Nodekey: "+ NodeKey+"For Dir: "+dir);
		return jdbcTemplate.queryForObject(sql, int.class,networkid,nodeid,dir);			
	}

	public List<PortInfo> findAllChnlPtcLines(int networkid, int nodeid){
		String NodeKey= ""+networkid+"_"+nodeid;
		//		 	String tablename= "PortInfo_"+NodeKey;		
		String tablename= "Ports";
		String sql = "SELECT distinct Rack,Sbrack,Card,CardType,LinePort,Direction,DemandId FROM "+tablename+" where NetworkId= ? and NodeId=? and DemandId in (select DemandId from Demand where NetworkId=? and ChannelProtection='Yes') and MpnPortNo=0";
		//			MainMap.logger.info("findAll: "+sql);
		List<PortInfo> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(PortInfo.class),networkid,nodeid,networkid);
		return info;
	}

	public List<PortInfo> findAllChnlPtcLinesAddedBrField(int networkidGf, int networkidBf,int nodeid){
		//		 	String NodeKey= ""+networkid+"_"+nodeid;
		//		 	String tablename= "PortInfo_"+NodeKey;		
		String BfPortInfoTable="PortInfo_"+networkidBf+"_"+nodeid;
		String GfPortInfoTable="PortInfo_"+networkidGf+"_"+nodeid;
		String tablename= "Ports";

		String sql = "select distinct Rack,Sbrack,Card,CardType,LinePort,Direction,DemandId from (select bf.NetworkId,bf.NodeId, bf.Rack, bf.Sbrack, bf.Card, bf.CardType,bf.Port,bf.LinePort, "+
				"bf.EquipmentId,bf.CircuitId,bf.DemandId,bf.Direction,gf.NetworkId as GfNetworkId from "+
				"( select * from "+tablename+"  where NetworkId = ? and NodeId = ? and ( CardType like '%MPN%' or CardType like '%TPN%')) as bf "+ 			 
				"left join ( select * from "+tablename+"  where NetworkId = ? and NodeId = ? and ( CardType like '%MPN%' or CardType like '%TPN%') ) as gf on "+              
				"(gf.Rack = bf.Rack) and (gf.Sbrack = bf.Sbrack) and (gf.Card = bf.Card) and (gf.LinePort = bf.LinePort) "+ 
				") as t where GfNetworkId is null and DemandId in (select DemandId from Demand where NetworkId=? and ChannelProtection='Yes');";
		//			MainMap.logger.info("findAll: "+sql);
		List<PortInfo> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(PortInfo.class),networkidBf,nodeid,networkidGf,nodeid,networkidBf);
		return info;
	}

	//Returns all line ports available on node
	public List<PortInfo> findAllUniqueLinePorts(int networkid, int nodeid){
		String NodeKey= ""+networkid+"_"+nodeid;
		//		 	String tablename= "PortInfo_"+NodeKey;		
		String tablename= "Ports";
		System.out.println("findAllUniqueLinePorts tablename:"+tablename+" NodeKey:"+NodeKey); 
		String sql = "SELECT distinct Rack,Sbrack,Card,CardType,LinePort,Direction,DemandId FROM "+tablename+" where NetworkId = ? and NodeId = ? and (CardType like '%MPN%' or CardType like 'TPN%') and MpnPortNo=0";
		//MainMap.logger.info("findAll: "+sql);
		List<PortInfo> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(PortInfo.class),networkid,nodeid);
		System.out.println(info.size());
		return info;
	}
	
	//Returns all line 10G aggregated line ports available on node
	public List<PortInfo> findAllUniqueTenGAggLinePorts(int networkid, int nodeid){
		String NodeKey= ""+networkid+"_"+nodeid;
		//		 	String tablename= "PortInfo_"+NodeKey;		
		String tablename= "Ports";
		System.out.println("findAllUniqueTenGAggLinePorts tablename:"+tablename+" NodeKey:"+NodeKey); 
		String sql = "SELECT distinct Rack,Sbrack,Card,CardType,LinePort,Direction,DemandId FROM ports where NetworkId = ? and NodeId = ? and CardType like 'MPN 10G' and DemandId in (select DemandId from demandmapping where NetworkId=? and CircuitId in (select CircuitId from circuit10gagg where Networkid=?))";
		//MainMap.logger.info("findAll: "+sql);
		List<PortInfo> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(PortInfo.class),networkid,nodeid,networkid,networkid);
		System.out.println(info.size());
		return info;
	}

	public List<PortInfo> findAllClientPtcLines(int networkid, int nodeid){
		String NodeKey= ""+networkid+"_"+nodeid;
		//		 	String tablename= "PortInfo_"+NodeKey;		
		String tablename= "Ports";
		String sql = "SELECT distinct Rack,Sbrack,Card,CardType,LinePort,Direction,DemandId FROM "+
				tablename+" where NetworkId = ? and NodeId = ? and DemandId in (select DemandId from Demand where NetworkId=? and MpnPortNo=0 and ClientProtection='Yes' and ProtectionType like '1+%') order by DemandId";
		//MainMap.logger.info("findAll: "+sql);
		List<PortInfo> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(PortInfo.class),networkid,nodeid,networkid);
		return info;
	}

	public List<PortInfo> findAllClientPtcLinesAddedBrField(int networkidGf, int networkidBf,int nodeid){
		//		 	String NodeKey= ""+networkid+"_"+nodeid;

		//		 	String BfPortInfoTable="PortInfo_"+networkidBf+"_"+nodeid;
		//			String GfPortInfoTable="PortInfo_"+networkidGf+"_"+nodeid;
		String tablename= "Ports";

		String sql = "select distinct Rack,Sbrack,Card,CardType,LinePort,Direction,DemandId from (select bf.NetworkId ,bf.NodeId, bf.Rack, bf.Sbrack, bf.Card, bf.CardType,bf.Port,bf.LinePort, "+
				"bf.EquipmentId,bf.CircuitId,bf.DemandId,bf.Direction,gf.NetworkId as GfNetworkId,gf.NodeId as GfNodeId from "+
				"( select * from "+tablename+"  where NetworkId = ? and NodeId = ? and (CardType like '%MPN%' or CardType like '%TPN%')) as bf "+ 			 
				"left join ( select * from "+tablename+"  where NetworkId = ? and NodeId = ? and (CardType like '%MPN%' or CardType like '%TPN%') ) as gf on "+              
				"(gf.Rack = bf.Rack) and (gf.Sbrack = bf.Sbrack) and (gf.Card = bf.Card) and (gf.LinePort = bf.LinePort) "+ 
				") as t where GfNetworkId is null and DemandId in (select DemandId from Demand where NetworkId=? and ClientProtection='Yes' and ProtectionType like '1+%');";
		//			MainMap.logger.info("findAll: "+sql);
		List<PortInfo> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(PortInfo.class),networkidBf,nodeid,networkidGf,nodeid,networkidBf);
		return info;
	}

	public List<PortInfo> findAllClientUnPtcLines(int networkid, int nodeid){
		String NodeKey= ""+networkid+"_"+nodeid;
		//		 	String tablename= "PortInfo_"+NodeKey;		
		String tablename= "Ports";
		String sql = "SELECT distinct Rack,Sbrack,Card,CardType,LinePort,Direction,DemandId FROM "+tablename+" where NetworkId = ? and NodeId = ? and MpnPortNo=0 and DemandId in (select DemandId from Demand where NetworkId=? and ((ClientProtection='No' and ChannelProtection='No') or ProtectionType not like '1+%' )) order by Demandid";
		//MainMap.logger.info("findAll: "+sql);
		List<PortInfo> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(PortInfo.class),networkid,nodeid,networkid);
		return info;
	}

	public List<PortInfo> findAllClientUnPtcLinesAddedBrField(int networkidGf, int networkidBf,int nodeid){
		//		 	String NodeKey= ""+networkid+"_"+nodeid;

		//		 	String BfPortInfoTable="PortInfo_"+networkidBf+"_"+nodeid;
		//			String GfPortInfoTable="PortInfo_"+networkidGf+"_"+nodeid;
		String tablename= "Ports";

		String sql = "select distinct Rack,Sbrack,Card,CardType,LinePort,Direction,DemandId from (select bf.NetworkId ,bf.NodeId, bf.Rack, bf.Sbrack, bf.Card, bf.CardType,bf.Port,bf.LinePort, "+
				"bf.EquipmentId,bf.CircuitId,bf.DemandId,bf.Direction,gf.NetworkId as GfNetworkId,gf.NodeId as GfNodeId from "+
				"( select * from "+tablename+"  where NetworkId = ? and NodeId = ? and (CardType like '%MPN%' or CardType like '%TPN%') ) as bf "+ 			 
				"left join ( select * from "+tablename+"  where NetworkId = ? and NodeId = ? and (CardType like '%MPN%' or CardType like '%TPN%') ) as gf on "+              
				"(gf.Rack = bf.Rack) and (gf.Sbrack = bf.Sbrack) and (gf.Card = bf.Card) and (gf.LinePort = bf.LinePort) "+ 
				") as t where GfNetworkId is null and DemandId in (select DemandId from Demand where NetworkId=? and ((ClientProtection='No' and ChannelProtection='No') or ProtectionType not like '1+%' ));";
		//			MainMap.logger.info("findAll: "+sql);
		List<PortInfo> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(PortInfo.class),networkidBf,nodeid,networkidGf,nodeid,networkidBf);
		return info;
	}


	public List<PortInfo> findAll(int networkid, int nodeid){
		String NodeKey= ""+networkid+"_"+nodeid;
		//		String tablename= "PortInfo_"+NodeKey;
		String tablename= "Ports";
		String sql = "SELECT * FROM "+tablename+" where NetworkId=? and NodeId=?" ;
		List<PortInfo> list  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(PortInfo.class),networkid,nodeid);
		return list;
	}	

	public List<Map<String,Object>> countPortByTypeNEId(int networkid,int nodeid)
	{
		String NodeKey= ""+networkid+"_"+nodeid;
		//	 	 String tablename= "PortInfo_"+NodeKey;	
		String tablename= "Ports";
		String sql = "SELECT  EquipmentId,  COUNT(1) as CNT FROM "+tablename+" where NetworkId=? and NodeId=? GROUP BY EquipmentId HAVING COUNT(1) >= 1; ";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,networkid,nodeid);
		return list;
	}

	public void insert(final PortInfo port) throws SQLException {    
		int id = 0; 
		//    String NodeKey = port.getNodeKey();
		//    String k[]=NodeKey.split("_");
		//    String tablename= "PortInfo_"+NodeKey;	
		String tablename= "Ports";
		if(port.getPort()==0)
		{
			Object val = getNextPortIdForNode(port.getNetworkId(),port.getNodeId(),port.getRack(), port.getSbrack(), port.getCard());
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
			id=port.getPort();
		}

		/*Why this check?*/
		if(port.getLinePort()==0 && port.getCardType().contains("TPN"))
		{
			port.setLinePort(id+5);
		}

		String sql = "INSERT into "+tablename+" (NetworkId,NodeId, Rack, Sbrack, Card, CardType, Port, LinePort, EquipmentId, CircuitId,DemandId,Direction,MpnPortNo) VALUES ( ?,?,?,?,?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.update(
				sql,
				new Object[] { port.getNetworkId(),port.getNodeId(),port.getRack(), port.getSbrack(), port.getCard(),port.getCardType(), id, port.getLinePort(), port.getEquipmentId(), port.getCircuitId(), port.getDemandId(), port.getDirection(), port.getMpnPortNo() });
	}
	public int count(int networkId)
	 {		
		 logger.info("Count Ports");
		  String sql = "select count(*) from Ports where NetworkId=?"; 
		  return jdbcTemplate.queryForObject(sql, int.class,networkId);		  		        
	 }
	
	
	 public void copyPortInfoDataInBrField(int networkidGrField, int networkidBrField ) throws SQLException {			
		 	
		    String sql = "insert into Ports (NetworkId, NodeId, Rack, Sbrack, Card, CardType, Port, LinePort, EquipmentId, CircuitId,DemandId,Direction,MpnPortNo) select ?, NodeId, Rack, Sbrack, Card, CardType, Port, LinePort, EquipmentId, CircuitId,DemandId,Direction,MpnPortNo  from Ports where NetworkId = ? ";
		    logger.info("copyPortInfoDataInBrField: "+sql); 	     
		    jdbcTemplate.update( sql,networkidBrField,networkidGrField);
			 	}
	 

	public Integer getFirstFreePortId(PortInfo port)
	{		
		logger.info("getFirstFreePortId");
		//		String NodeKey = port.getNodeKey();
		//	    String tablename= "PortInfo_"+NodeKey;	

		String tablename= "Ports";
		System.out.println("tablename for first free port"+tablename+" for port:"+port.toString());

		//		 String sql = "select Port+1 from "+tablename+" M1 where NetworkId=? and NodeId=? and Rack=? and Sbrack=? and Card=? and Port+1 NOT IN ( "
		//		 		+ "select Port from "+tablename+" M2 where NetworkId=? and NodeId=? and M2.Port=M1.Port+1 and Rack=? and Sbrack=? and Card=? ) ORDER BY Port ASC LIMIT 1";
		//		 System.out.println("Sql:"+sql);

		String sql="select distinct(Port) from Ports where NetworkId=? and NodeId=? and Rack=? and Sbrack=? and Card=? ";

		try {
			List<Integer> portids=jdbcTemplate.queryForList(sql, Integer.class, port.getNetworkId(),port.getNodeId(),port.getRack(), port.getSbrack(), port.getCard());
			int portNum=1,i=0;
			while(i<portids.size() && portids.get(i++)==portNum) {
				portNum++;
			}

			return portNum; 

		} catch (EmptyResultDataAccessException e) {
			// TODO: handle exception
			return 1;
		}		 

	}

	//Check
	public void createPortInfoTable(String NodeKey)
	{		
		String tablename= "PortInfo_"+NodeKey;	
		//		 String tablename= "Ports";
		String sql = " CREATE TABLE IF NOT EXISTS `DwdmNpt`.`"+tablename+"` ( `NodeKey` VARCHAR(45) NOT NULL, `Rack` INT NOT NULL,`Sbrack` INT NOT NULL, `Card` INT NOT NULL, `CardType` VARCHAR(45) NOT NULL, `Port` INT NOT NULL, `LinePort` INT NOT NULL, `EquipmentId` INT NULL, `CircuitId` INT NULL, `DemandId` INT NULL, `Direction` VARCHAR(45) NOT NULL,  PRIMARY KEY (`NodeKey`, `Rack`, `Sbrack`, `Card`, `Port` , `LinePort`));";
		jdbcTemplate.execute(sql);
	}

	/*
	 * Function deletes all the PortInfo tables 
	 * */
	public void deleteAllPortInfo(int networkid) {
		logger.info("Delete deleteAllPortInfo");
		//	        String namestr = "PortInfo_"+networkid+"_%";
		String tablename= "Ports";
		//	        String sql = "SELECT table_name FROM information_schema.`TABLES` WHERE table_schema = 'DwdmNpt' AND table_name LIKE '" +namestr+"';";
		String sql="DELETE from "+tablename+" where NetworkId=?"; 
		jdbcTemplate.update(sql,networkid);

		//	        logger.info(sql);
		//	        List<Map<String, Object>> names= jdbcTemplate.queryForList(sql,networkid);  
		//	        
		//	        for (Map<String, Object> row : names) {            
		//	            String sql1 = "DROP TABLE "+row.get("table_name")+";"; 
		//	           logger.info(sql1);
		//	            jdbcTemplate.update(sql1);
		//	        }
	}	 

	public void deletePort(int networkid, int nodeid, int rack , int sbrack,int card, int portid) {
		String NodeKey= ""+networkid+"_"+nodeid;
		//	        String tablename= "PortInfo_"+NodeKey;
		String tablename= "Ports";
		String sql = "Delete FROM "+tablename+"  where NetworkId = ? and NodeId = ? and Rack = ? and Sbrack = ? and Card =? and Port = ? ";
		logger.info(sql);
		jdbcTemplate.update(sql,networkid,nodeid,rack,sbrack,card,portid);
	}
	
	
	public int findCircuitIdforPortID(int networkid, int nodeid, int Rack,int Sbrack,int Card,int PortId)
	{
		String sql = "Select CircuitId from Ports where NetworkId = ? and NodeId= ? and Rack = ? and Sbrack = ? and Card = ? and Port = ?";
		try
		{
		int circuitId = jdbcTemplate.queryForObject(sql,int.class,networkid, nodeid, Rack, Sbrack, Card, PortId);
		return circuitId;
		}
		catch(EmptyResultDataAccessException e) {
			return -1;
		}
		
		
	}

	public void deleteDcmPort(int networkid, int nodeid, String dir) {
		String NodeKey= ""+networkid+"_"+nodeid;
		//	        String tablename= "PortInfo_"+NodeKey;
		String tablename= "Ports";
		String sql = "Delete FROM "+tablename+"  where NetworkId = ? and NodeId = ? and CardType = ? and Direction = ? ";
		logger.info(sql);
		jdbcTemplate.update(sql,networkid,nodeid,ResourcePlanConstants.DCM_Tray_Unit, dir);
	}

	/**
	 * Query will delete all the ports related to that demand id
	 * @param networkid
	 * @param nodeid
	 * @param demandid
	 */
	public void deletePort(int networkid, int nodeid, int demandid) {
		String NodeKey= ""+networkid+"_"+nodeid;
		//	        String tablename= "PortInfo_"+NodeKey;
		String tablename= "Ports";
		String sql = "Delete FROM "+tablename+"  where NetworkId = ? and NodeId = ? and DemandId = ? ";	       
		jdbcTemplate.update(sql,networkid,nodeid,demandid);
	}

	public int count()
	{		
		logger.info("Count CardInfo");
		String sql = "select count(*) from CardInfo"; 
		return jdbcTemplate.queryForObject(sql, int.class);		  		        
	} 

	public void createViewAllPortInfo(int networkid) {
		logger.info("createViewAllPortInfo");
		String sql2="";
		String namestr = "PortInfo_"+networkid+"_%";
		String sql = "SELECT table_name FROM information_schema.`TABLES` WHERE table_schema = 'DwdmNpt' AND table_name LIKE '" +namestr+"';";
		logger.info(sql);
		List<Map<String, Object>> names= jdbcTemplate.queryForList(sql);  

		names.toArray();
		for (int i = 0; i <  names.toArray().length; i++) {
			logger.info("CardInfoDao.createViewAllPortInfo()"+names.get(i).get("table_name"));
			sql2 = sql2+"select * from " + names.get(i).get("table_name")+"  UNION ALL ";		
		}
		sql2=sql2.substring(0, sql2.length()-10);
		logger.info("createViewAllPortInfo.my query()"+sql2);
		sql2="CREATE OR REPLACE VIEW PortInfo AS "+sql2;
		jdbcTemplate.execute(sql2);
	}

	public void updateNodeKey(int networkid, int nodeid, int networkidBF ) throws SQLException {
		String NodeKey= ""+networkid+"_"+nodeid;
		String NodeKeyBF= ""+networkidBF+"_"+nodeid;
		String tablename= "PortInfo_"+NodeKeyBF;	
		//		 	String tablename= "Ports";
		String sql = "Update "+ tablename+" set NodeKey = ? where NetworkId = ? and NodeId = ? ";
		logger.info("updatePortInfo: "+sql); 	     
		jdbcTemplate.update(
				sql,
				new Object[] { NodeKeyBF,NodeKey });

	}

	public void insertPortDataInBrField(int networkid, int nodeid, int networkidBF ) throws SQLException {
		String NodeKey= ""+networkid+"_"+nodeid;
		String NodeKeyBF= ""+networkidBF+"_"+nodeid;
		String tablenameBF= "PortInfo_"+NodeKeyBF;	  
		//		 	String tablename= "PortInfo_"+NodeKey;	
		String tablename= "Ports";
		String sql = "insert into "+ tablename+"(`NetworkId`,\r\n" + 
				"`NodeId`,\r\n" + 
				"`Rack`,\r\n" + 
				"`Sbrack`,\r\n" + 
				"`Card`,\r\n" + 
				"`CardType`,\r\n" + 
				"`Port`,\r\n" + 
				"`LinePort`,\r\n" + 
				"`EquipmentId`,\r\n" + 
				"`CircuitId`,\r\n" + 
				"`DemandId`,\r\n" + 
				"`Direction` ) "
				+ "select ?,\r\n" + 
				"`NodeId`,\r\n" + 
				"`Rack`,\r\n" + 
				"`Sbrack`,\r\n" + 
				"`Card`,\r\n" + 
				"`CardType`,\r\n" + 
				"`Port`,\r\n" + 
				"`LinePort`,\r\n" + 
				"`EquipmentId`,\r\n" + 
				"`CircuitId`,\r\n" + 
				"`DemandId`,\r\n" + 
				"`Direction` from "+tablename+" where NetworkId = ? and NodeId=?";
		logger.info("insertPortDataInBrField: "+sql); 	     
		jdbcTemplate.update( sql,networkidBF,networkid,nodeid);
	} 

	/**
	 * 
	 * @desc Find out the Card Related Info from the Circuit Id
	 * @date 6th Feb,2018
	 * @author hp
	 * @param networkid
	 * @param nodeid
	 * @param circuitid
	 * @return
	 */
	public List<PortInfo> findCircuitPortInfo(int networkid,int nodeid, int Circuitid, int typeOfOtnLSP) {

		logger.info("Query Node: findCircuitPortInfo "+ networkid+","+nodeid+","+Circuitid+","+typeOfOtnLSP);
		String NodeKey= ""+networkid+"_"+nodeid;
		//	        String tablename= "PortInfo_"+NodeKey;	
		String tablename= "Ports";

		try
		{
			String sql = "";	    

			if(typeOfOtnLSP >= DataPathConfigFileConstants.MPNClientSideOtnLsp) {
				sql = "SELECT  NetworkId, NodeId, Rack, Sbrack, Card, CardType,  Port, LinePort, EquipmentId, CircuitId, DemandId, Direction, MpnPortNo  FROM " +
					  tablename+" where NetworkId = ? and NodeId = ? and Circuitid = ? and CardType!='"+ResourcePlanConstants.CardMuxponder10G+"' limit 1";
			}			
			else if(typeOfOtnLSP < DataPathConfigFileConstants.MPNClientSideOtnLsp){
				sql = "SELECT NetworkId, NodeId, Rack, Sbrack, Card, CardType,Port, LinePort, EquipmentId, CircuitId, DemandId, Direction, MpnPortNo FROM " +
						tablename+" where NetworkId = ? and NodeId = ? and Circuitid = ? and CardType='"+ResourcePlanConstants.CardMuxponder10G+"' limit 1";	    
			}

			logger.info(sql);		       
			List<PortInfo> row  =  jdbcTemplate.query(sql,new BeanPropertyRowMapper(PortInfo.class),networkid,nodeid,Circuitid);			
			return row; 
		}
		catch(EmptyResultDataAccessException e) {
			return null;
		}
	}


	public List<PortInfo> findAllCommonBrField(int networkidBf,int networkidGf,int nodeid){
		//		 	String BfPortInfoTable="PortInfo_"+networkidBf+"_"+nodeid;
		//			String GfPortInfoTable="PortInfo_"+networkidGf+"_"+nodeid;
		String tablename= "Ports";
		String sql = "select * from (select bf.NetworkId ,bf.NodeId , bf.Rack, bf.Sbrack, bf.Card, bf.CardType,bf.Port,bf.LinePort,bf.EquipmentId,bf.CircuitId,bf.DemandId,bf.Direction from "+ 
				"( select * from "+tablename+" where  NetworkId = ? and NodeId = ? ) as bf "+ 			 
				"left join ( select * from "+tablename+" where  NetworkId = ? and NodeId = ? ) as gf on "+              
				"(gf.Rack = bf.Rack) and (gf.Sbrack = bf.Sbrack) and (gf.Card = bf.Card) and (gf.Port = bf.Port) "+ 
				"where bf.CardType = gf.CardType and "+
				"bf.LinePort = gf.LinePort and "+
				"bf.EquipmentId = gf.EquipmentId and "+ 
				"bf.CircuitId = gf.CircuitId and "+
				"bf.DemandId = gf.DemandId and "+
				"bf.Direction = gf.Direction "+
				") as t ;";

		MainMap.logger.info(" PortInfo_findAllCommonBrField: "+sql);
		List<PortInfo> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(PortInfo.class),networkidBf,nodeid,networkidGf,nodeid);
		return info;
	}

	public List<PortInfo> findAllModifiedBrField(int networkidBf,int networkidGf,int nodeid){
		//			String BfPortInfoTable="PortInfo_"+networkidBf+"_"+nodeid;
		//			String GfPortInfoTable="PortInfo_"+networkidGf+"_"+nodeid;
		String tablename= "Ports";
		String sql = "select * from (select bf.NetworkId ,bf.NodeId, bf.Rack, bf.Sbrack, bf.Card, bf.CardType,bf.Port,bf.LinePort,bf.EquipmentId,bf.CircuitId,bf.DemandId,bf.Direction from "+ 
				"( select * from "+tablename+" where  NetworkId = ? and NodeId = ? ) as bf "+ 			 
				"left join ( select * from "+tablename+" where  NetworkId = ? and NodeId = ? ) as gf on "+              
				"(gf.Rack = bf.Rack) and (gf.Sbrack = bf.Sbrack) and (gf.Card = bf.Card) and (gf.Port = bf.Port) "+ 
				"where bf.CardType <> gf.CardType and "+
				"bf.LinePort <> gf.LinePort and "+
				"bf.EquipmentId <> gf.EquipmentId and "+ 
				"bf.CircuitId <> gf.CircuitId and "+
				"bf.DemandId <> gf.DemandId and "+
				"bf.Direction <> gf.Direction "+
				") as t ;";

		MainMap.logger.info(" PortInfo_findAllModifiedBrField: "+sql);
		List<PortInfo> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(PortInfo.class),networkidBf,nodeid,networkidGf,nodeid);
		return info;
	}

	public List<PortInfo> findAllAddedBrField(int networkidBf,int networkidGf,int nodeid){
		String BfPortInfoTable="PortInfo_"+networkidBf+"_"+nodeid;
		String GfPortInfoTable="PortInfo_"+networkidGf+"_"+nodeid;
		String tablename= "Ports";
		String sql = "select NetworkId, NodeId, Rack, Sbrack,Card, CardType,Port,LinePort,EquipmentId,CircuitId,DemandId,Direction from (select bf.NetworkId, bf.NodeId, bf.Rack, bf.Sbrack, bf.Card, bf.CardType,bf.Port,bf.LinePort, "+
				"bf.EquipmentId,bf.CircuitId,bf.DemandId,bf.Direction,gf.NetworkId as GfNetworkId from "+
				"( select * from "+tablename+" where  NetworkId = ? and NodeId = ? ) as bf "+ 			 
				"left join ( select * from "+tablename+" where  NetworkId = ? and NodeId = ? ) as gf on "+              
				"(gf.Rack = bf.Rack) and (gf.Sbrack = bf.Sbrack) and (gf.Card = bf.Card) and (gf.Port = bf.Port) "+ 
				") as t where GfNetworkId is null;";

		MainMap.logger.info(" PortInfo_findAllAddedBrField: "+sql);
		List<PortInfo> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(PortInfo.class),networkidBf,nodeid,networkidGf,nodeid);
		return info;
	}

	public List<PortInfo> findAllDeletedBrField(int networkidBf,int networkidGf,int nodeid){
		//			String BfPortInfoTable="PortInfo_"+networkidBf+"_"+nodeid;
		//			String GfPortInfoTable="PortInfo_"+networkidGf+"_"+nodeid;
		String tablename= "Ports";
		String sql = "select NetworkId, NodeId, Rack, Sbrack,Card, CardType,Port,LinePort,EquipmentId,CircuitId,DemandId,Direction from (select gf.NetworkId, gf.NodeId, gf.Rack, gf.Sbrack, gf.Card, gf.CardType,gf.Port,gf.LinePort,gf.EquipmentId,gf.CircuitId,gf.DemandId,gf.Direction,bf.NetworkId as BfNetworkId from "+ 
				"( select * from "+tablename+" where  NetworkId = ? and NodeId = ? ) as gf "+ 			 
				"left join ( select * from "+tablename+" where  NetworkId = ? and NodeId = ? ) as bf on "+              
				"(gf.Rack = bf.Rack) and (gf.Sbrack = bf.Sbrack) and (gf.Card = bf.Card) and (gf.Port = bf.Port) "+ 
				") as t where BfNetworkId is null;";

		MainMap.logger.info(" PortInfo_findAllDeletedBrField: "+sql);
		List<PortInfo> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(PortInfo.class),networkidGf,nodeid,networkidBf,nodeid);
		return info;
	}


	/*Find All unique lineports added/deleted/common/modified in br field*/

	public List<PortInfo> findAllUniqueCommonBrField(int networkidGf,int networkidBf,int nodeid){
		//		 	String BfPortInfoTable="PortInfo_"+networkidBf+"_"+nodeid;
		//			String GfPortInfoTable="PortInfo_"+networkidGf+"_"+nodeid;
		String tablename= "Ports";
		String sql = "select distinct Rack,Sbrack,Card,CardType,LinePort,Direction,DemandId from (select bf.NetworkId, bf.NodeId, bf.Rack, bf.Sbrack, bf.Card, bf.CardType,bf.Port,bf.LinePort,bf.EquipmentId,bf.CircuitId,bf.DemandId,bf.Direction from "+ 
				"( select * from "+tablename+" where NetworkId = ? and NodeId = ? and ( CardType like '%MPN%' or CardType like '%TPN%')) as bf "+ 			 
				"left join ( select * from "+tablename+" where NetworkId = ? and NodeId = ? and ( CardType like '%MPN%' or CardType like '%TPN%')) as gf on "+              
				"(gf.Rack = bf.Rack) and (gf.Sbrack = bf.Sbrack) and (gf.Card = bf.Card) and (gf.LinePort = bf.LinePort) "+ 
				"where bf.CardType = gf.CardType and "+
				"bf.LinePort = gf.LinePort and "+
				"bf.EquipmentId = gf.EquipmentId and "+ 
				"bf.CircuitId = gf.CircuitId and "+
				"bf.DemandId = gf.DemandId and "+
				"bf.Direction = gf.Direction "+
				") as t ;";

		MainMap.logger.info(" PortInfo_findAllCommonBrField: "+sql);
		List<PortInfo> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(PortInfo.class),networkidBf,nodeid,networkidGf,nodeid);
		return info;
	}

	public List<PortInfo> findAllUniqueModifiedBrField(int networkidGf,int networkidBf,int nodeid){
		//			String BfPortInfoTable="PortInfo_"+networkidBf+"_"+nodeid;
		//			String GfPortInfoTable="PortInfo_"+networkidGf+"_"+nodeid;
		String tablename= "Ports";
		String sql = "select distinct Rack,Sbrack,Card,CardType,LinePort,Direction,DemandId from (select bf.NetworkId, bf.NodeId, bf.Rack, bf.Sbrack, bf.Card, bf.CardType,bf.Port,bf.LinePort,bf.EquipmentId,bf.CircuitId,bf.DemandId,bf.Direction from "+ 
				"( select * from "+tablename+" where NetworkId = ? and NodeId = ? and ( CardType like '%MPN%' or CardType like '%TPN%')) as bf "+ 			 
				"left join ( select * from "+tablename+" where NetworkId = ? and NodeId = ? and ( CardType like '%MPN%' or CardType like '%TPN%')) as gf on "+              
				"(gf.Rack = bf.Rack) and (gf.Sbrack = bf.Sbrack) and (gf.Card = bf.Card) and (gf.LinePort = bf.LinePort) "+ 
				"where bf.CardType <> gf.CardType and "+
				"bf.LinePort <> gf.LinePort and "+
				"bf.EquipmentId <> gf.EquipmentId and "+ 
				"bf.CircuitId <> gf.CircuitId and "+
				"bf.DemandId <> gf.DemandId and "+
				"bf.Direction <> gf.Direction "+
				") as t ;";

		MainMap.logger.info(" PortInfo_findAllModifiedBrField: "+sql);
		List<PortInfo> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(PortInfo.class),networkidBf,nodeid,networkidGf,nodeid);
		return info;
	}

	public List<PortInfo> findAllUniqueAddedBrField(int networkidGf,int networkidBf,int nodeid){
		//			String BfPortInfoTable="PortInfo_"+networkidBf+"_"+nodeid;
		//			String GfPortInfoTable="PortInfo_"+networkidGf+"_"+nodeid;
		String tablename= "Ports";
		String sql = "select distinct Rack,Sbrack,Card,CardType,LinePort,Direction,DemandId from (select  bf.NetworkId, bf.NodeId, bf.Rack, bf.Sbrack, bf.Card, bf.CardType,bf.Port,bf.LinePort, "+
				"bf.EquipmentId,bf.CircuitId,bf.DemandId,bf.Direction,gf.NetworkId as GfNetworkId from "+
				"( select * from "+tablename+"  where NetworkId = ? and NodeId = ? and ( CardType like '%MPN%' or CardType like '%TPN%') and MpnPortNo=0) as bf "+ 			 
				"left join ( select * from "+tablename+"  where NetworkId = ? and NodeId = ? and ( CardType like '%MPN%' or CardType like '%TPN%') and MpnPortNo=0) as gf on "+              
				"(gf.Rack = bf.Rack) and (gf.Sbrack = bf.Sbrack) and (gf.Card = bf.Card) and (gf.LinePort = bf.LinePort) "+ 
				") as t where GfNetworkId is null;";

		//			MainMap.logger.info(" PortInfo_findAllAddedBrField: "+sql);
		List<PortInfo> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(PortInfo.class),networkidBf,nodeid,networkidGf,nodeid);
		return info;
	}

	public List<PortInfo> findAllUniqueDeletedBrField(int networkidGf,int networkidBf,int nodeid){
		//			String BfPortInfoTable="PortInfo_"+networkidBf+"_"+nodeid;
		//			String GfPortInfoTable="PortInfo_"+networkidGf+"_"+nodeid;
		String tablename= "Ports";
		String sql = "select distinct Rack,Sbrack,Card,CardType,LinePort,Direction,DemandId from (select gf.NetworkId, gf.NodeId, gf.Rack, gf.Sbrack, gf.Card, gf.CardType,gf.Port,gf.LinePort,gf.EquipmentId,gf.CircuitId,gf.DemandId,gf.Direction,bf.NetworkId as BfNetworkId from "+ 
				"( select * from "+tablename+" where NetworkId = ? and NodeId = ? and ( CardType like '%MPN%' or CardType like '%TPN%')) as gf "+ 			 
				"left join ( select * from "+tablename+" where NetworkId = ? and NodeId = ? and ( CardType like '%MPN%' or CardType like '%TPN%')) as bf on "+              
				"(gf.Rack = bf.Rack) and (gf.Sbrack = bf.Sbrack) and (gf.Card = bf.Card) and (gf.LinePort = bf.LinePort) "+ 
				") as t where BfNetworkId is null;";

		//			MainMap.logger.info(" PortInfo_findAllDeletedBrField: "+sql);
		List<PortInfo> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(PortInfo.class),networkidGf,nodeid,networkidBf,nodeid);
		return info;
	}

	public List<Map<String, Object>> tempFindAll(int networkid,int nodeid) {
		String NodeKey= ""+networkid+"_"+nodeid;
		String tablename= "PortInfo_"+NodeKey;
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
	
	public List<AggregatorClientMap> findAggregatorLinePortMapping(int networkid, int nodeid){
//		String NodeKey= ""+networkid+"_"+nodeid;
		//		String tablename= "PortInfo_"+NodeKey;
//		String tablename= "Ports";
		String sql = "SELECT distinct Rack,SbRack,Card,LinePort,MpnPortNo,MpnRack,MpnSbRack,MpnCard FROM AggClientsMapping where NetworkId=? and NodeId=?";
		List<AggregatorClientMap> list  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(AggregatorClientMap.class),networkid,nodeid);
		return list;
	}	
	public List<PortInfo> findMpnToFdfMapping(int networkid,int nodeid) {
		logger.info("Query Node: findMpnToFdfMapping");
		String NodeKey= ""+networkid+"_"+nodeid;
		//	        String tablename= "PortInfo_"+NodeKey;
		String tablename= "Ports";
		try
		{
			String sql = "select  distinct NetworkId,NodeId,Rack,Sbrack,Card,Port,CircuitId,CardType,MpnPortNo  from (SELECT r.NetworkId,r.NodeId,r.Rack,r.SbRack,r.Card,r.Port,r.LinePort,r.CircuitId,r.CardType,r.MpnPortNo,t.NetworkId as AggNetworkId FROM \r\n" + 
					"(( select * from Ports where NetworkId = ? and NodeId=? ) as r \r\n" + 
					"left join ( select * from AggClientsMapping where NetworkId = ? and NodeId=? ) as t\r\n" + 
					"on r.Rack=t.MpnRack and r.SbRack=t.MpnSbRack and r.Card=t.MpnCard and r.Port=t.MpnPortNo )) as q where q.AggNetworkId IS NULL and q.CardType like '%MPN%' ;";
			
			List<PortInfo> row  = jdbcTemplate.query(sql,new BeanPropertyRowMapper(PortInfo.class),networkid,nodeid,networkid,nodeid);
			return row; 
		}
		catch(EmptyResultDataAccessException e) {
			return null;
		}
	}

}
