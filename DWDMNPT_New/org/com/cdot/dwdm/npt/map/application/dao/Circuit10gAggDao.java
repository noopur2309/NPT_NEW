package application.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


import application.constants.MapConstants;
import application.constants.ResourcePlanConstants;
import application.model.AggregatorClientMap;
import application.model.Circuit;
import application.model.Circuit10gAgg;
import application.model.DemandMapping;
import application.model.PortInfo;

@Component
public class Circuit10gAggDao{
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	public static Logger logger = Logger.getLogger(ResourcePlanConstants.DatabasePrintStr);
	

	public int count(int networkId)
	{	
		 String sql = "select count(*) from Circuit10GAgg where NetworkId = ? "; 
		 return jdbcTemplate.queryForObject(sql, int.class, networkId);		  		        
	}

	public int count(int networkId, int circuitId)
	{		
		logger.info("Count Circuit10GAgg for NetworkId: "+networkId+" and CircuitId: "+circuitId );
		 String sql = "select count(*) from Circuit10gAgg where NetworkId=? and CircuitId=? "; 
		 return jdbcTemplate.queryForObject(sql, int.class,networkId, circuitId);
	}

	public List<Circuit10gAgg> findAllCircuit10gAgg(int networkId) {
		logger.debug("Find All Circuit10gAggDao");
		String sql = "SELECT * FROM Circuit10gAgg where NetworkId = ?  ";
		List<Circuit10gAgg> list = jdbcTemplate.query(sql,new BeanPropertyRowMapper(Circuit10gAgg.class), networkId);
		return list;
}
	 
	 public List<Circuit10gAgg> findAllCircuit10gAgg(int networkId, int circuitId) {
	        logger.debug("Find All Circuit10gAggDao");
	        String sql = "SELECT * FROM Circuit10gAgg where NetworkId = ? and CircuitId = ? ";
	        List<Circuit10gAgg> list = jdbcTemplate.query(sql,new BeanPropertyRowMapper(Circuit10gAgg.class), networkId, circuitId);
	        return list;
	}
	
	 
	 public Circuit10gAgg findCircuit10gAgg(int networkid, int circuitId, int circuit10gAggId) {
	        logger.debug("Find All Circuit10gAgg");
	        String sql = "SELECT * FROM Circuit10gAgg where NetworkId = ? and CircuitId = ? and Circuit10gAggId = ? ";
			Circuit10gAgg list = (Circuit10gAgg) jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper(Circuit10gAgg.class),networkid, 
									circuitId, circuit10gAggId);
	        return list;
		}	
	 
	 public Circuit10gAgg findCircuitByAggCircuitId(int networkid, int circuit10gAggId) {
	        logger.debug("Find All Circuit10gAgg");
	        String sql = "SELECT * FROM Circuit10gAgg where NetworkId = ? and Circuit10gAggId = ? ";
			Circuit10gAgg cir = (Circuit10gAgg) jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper(Circuit10gAgg.class),networkid, 
									 circuit10gAggId);
	        return cir;
		}	
		

		public List<Map<String, Object>> findGroupedCircuit10gAgg(int networkid, int circuitId) {
	        logger.debug("findGroupedCircuit10gAgg");
			String sql = "SELECT count(*), NetworkId, CircuitId, RequiredTraffic, TrafficType, LineRate  FROM DwdmNpt.Circuit10gAgg "+
						 "where NetworkId =? and CircuitId = ? group by "+ 
						 "RequiredTraffic, TrafficType, LineRate, NetworkId, CircuitId ";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,networkid, circuitId);
	        return list;
	    }	


	
	public int getNextCircuit10gAggIdForNetwork(int networkid, int circuitId) {
		logger.debug("getNextCircuit10gAggIdForNetwork");
		
		String sql = "SELECT MAX(Circuit10gAggId)+1 FROM Circuit10gAgg where NetworkId = ?";
		try
		{
			if(jdbcTemplate.queryForObject(sql, Integer.class, networkid)!= null)
				{
					return Integer.parseInt(jdbcTemplate.queryForObject(sql, Integer.class, networkid).toString());
				}
			else{
				return 1;
			}
		}
		catch(EmptyResultDataAccessException e) {
			return 1;
		}		
		
	}
	
	/**
	 * Insert Circuit10gAgg into DB api which returns the inserted Circuit ID 
	 * @param circuit10gAgg
	 * @param networkInfoMap
	 * @return
	 * @throws SQLException
	 */
	 public void insertCircuit10gAgg(final Circuit10gAgg circuit10gAgg, HashMap<String,Object> networkInfoMap) throws SQLException {
		 
		logger.debug("insertCircuit10gAgg");
		
		/**DBG => Check for BF case */	
		 int id = 0;
		 id = getNextCircuit10gAggIdForNetwork(circuit10gAgg.getNetworkId(), circuit10gAgg.getCircuitId());
				 
		 logger.debug("insertCircuit10gAgg  id :  "+ id);
	    
		 String sql = "INSERT into Circuit10gAgg(NetworkId, CircuitId, Circuit10gAggId, RequiredTraffic, TrafficType, LineRate)  VALUES (?,?,?,?,?,?)";
	     jdbcTemplate.update(
	             sql,
				 new Object[] { circuit10gAgg.getNetworkId(), circuit10gAgg.getCircuitId(),id, 
								circuit10gAgg.getRequiredTraffic(), circuit10gAgg.getTrafficType(),
								 circuit10gAgg.getLineRate()});
	     
	     ///return id;
   }


   public void deleteByNetworkId(int networkid) {
		logger.info("Delete Circuit10gAgg :"+networkid);
		System.out.println("Delete Circuit10gAgg "+ networkid);
		String sql = "delete from Circuit10gAgg where NetworkId = ?";
		jdbcTemplate.update(sql, networkid);      
	}

	public void copyCircuit10GAggInBrField(int networkidGrField, int networkidBrField ) throws SQLException {			

		String sql = "insert into Circuit10gAgg ( NetworkId, CircuitId, Circuit10gAggId, RequiredTraffic, TrafficType, LineRate) "+
		  			 " select ?, CircuitId, Circuit10gAggId, RequiredTraffic, TrafficType, LineRate from Circuit10gAgg where NetworkId = ? ";
		logger.info("copyCircuit10GAggInBrField: "+sql); 	     
		jdbcTemplate.update( sql,networkidBrField,networkidGrField);
	}


	 /**
	  * finds the list of added/new circuits(10gAgg) of the brownfield
	  * @param networkidGrField
	  * @param networkidBrField
	  * @query select * from 
				(  select count(*), cbf.NetworkId , cbf.CircuitId , cbf.Circuit10gAggId, cbf.RequiredTraffic, cbf.TrafficType, 
				cbf.LineRate, c.NetworkId as NetworkIdGf ,c.CircuitId as CircuitIdGf, c.Circuit10gAggId as Circuit10gAggIdGf from 
                ( select * from Circuit10gAgg where NetworkId = 3 and Circuitid=3) as cbf 
				left join ( select * from Circuit10gAgg where NetworkId = 1  and Circuitid=3) as c on  
                (c.CircuitId = cbf.CircuitId  and c.Circuit10gAggId = cbf.Circuit10gAggId  )  group by RequiredTraffic )  as t 
				where t.NetworkIdGf IS NULL  and t.CircuitIdGf IS NULL and t.Circuit10gAggIdGf IS NULL  group by  t.RequiredTraffic,  t.TrafficType, 
						                    t.LineRate,  t.NetworkId,  t.CircuitId ;                                
	  * @return
	  */
	  public List<Map<String,Object>> findAddedCircuit10gAggInBrField(int networkidGrField, int networkidBrField,int circuitId){       	              
		
		  System.out.println("findAddedCircuit10gAggInBrField :: "+circuitId);
		String sql = "select count(*),   cbf.NetworkId, cbf.CircuitId,cbf.LineRate, group_concat(cbf.Circuit10gAggId SEPARATOR ',') as Circuit10gAggId, \n" + 
				"   cbf.RequiredTraffic, cbf.TrafficType from (\n" + 
				" ( select * from Circuit10gAgg where NetworkId = ? and Circuitid=?) as cbf \n" + 
				"				left join \n" + 
				" ( select * from Circuit10gAgg where NetworkId = ?  and Circuitid=?) as cgf \n" + 
				"                on\n" + 
				"  (cbf.Circuit10gAggId = cgf.Circuit10gAggId)   \n" + 
				"                \n" + 
				"                ) group by cbf.TrafficType, cbf.LineRate, cbf.RequiredTraffic; ";

		 List<Map<String,Object>> route;		
		try
		{
			return route  =  jdbcTemplate.queryForList(sql, networkidBrField, circuitId,networkidGrField,circuitId);	       

		}
		catch(EmptyResultDataAccessException e) {
			return null;
		}	
	}


	 /**
	  * finds the list of unaltered circuits of the brownfield
	  * @param networkidGrField
	  * @param networkidBrField
	  * @return
	  * @query select * from 
	        		(select count(*), cbf.NetworkId , cbf.CircuitId , cbf.Circuit10gAggId, cbf.RequiredTraffic, cbf.TrafficType, 
	        		cbf.LineRate, c.NetworkId as NetworkIdGf , 
	        		c.CircuitId as CircuitIdGf from ( select * from Circuit10gAgg where NetworkId = 101 ) as cbf 
	        		left join ( select * from Circuit10gAgg where NetworkId = 94 ) as c on  (c.CircuitId = cbf.CircuitId and c.Circuit10gAggId = 
					cbf.Circuit10gAggId)) as t  
	        		where t.NetworkIdGf IS NOT NULL and t.CircuitIdGf IS NOT NULL and t.Circuit10gAggId IS NOT NULL group by  t.RequiredTraffic,  t.TrafficType, 
                     t.LineRate,  t.NetworkId,  t.CircuitId ;
                                
	  */
	  public List<Map<String,Object>> findCommonCircuitsInBrField(int networkidGrField, int networkidBrField,int circuitId){ 
		  
		  System.out.println("findCommonCircuitsInBrField :: "+circuitId);
		String sql = "select * from "+
						" (select count(*), cbf.NetworkId , cbf.CircuitId , cbf.Circuit10gAggId, cbf.RequiredTraffic, cbf.TrafficType, "+
						" cbf.LineRate, c.NetworkId as NetworkIdGf , "+
						" c.CircuitId as CircuitIdGf from ( select * from Circuit10gAgg where NetworkId = ? and CircuitId=?) as cbf "+
						" left join ( select * from Circuit10gAgg where NetworkId = ? and CircuitId=? ) as c on  (c.CircuitId = cbf.CircuitId and c.Circuit10gAggId = "+
						" cbf.Circuit10gAggId) group by RequiredTraffic ) as t "+
						" where t.NetworkIdGf IS NOT NULL and t.CircuitIdGf IS NOT NULL and t.Circuit10gAggId IS NOT NULL group by  t.RequiredTraffic,  t.TrafficType, \r\n" + 
						"                     t.LineRate,  t.NetworkId,  t.CircuitId ";
						
		List<Map<String,Object>> route;	

		try
		{
			return route  =  jdbcTemplate.queryForList(sql, networkidBrField,circuitId, networkidGrField,circuitId);	       
		}
		catch(EmptyResultDataAccessException e) {
			return null;
		}	
	};



	/**
	 * @desc   To retrieve paths with a particular nodeid the service wise list of OTN LSP from 10G Agg Table
	 * @date   11th Dec,2018
	 * @query  
				 select one.NetworkId, one.DemandId, one.LineRate, one.Path, one.WavelengthNo, one.RoutePriority, 
			   			group_concat(cir.Circuit10gAggId order by cir.Circuit10gAggId asc) as CircuitId, group_concat(DISTINCT cir.CircuitId) as CircuitOriginalId, cir.TrafficType, cir.ProtectionType
			   			from 
			               (select nr.NetworkId, nr.DemandId, nr.LineRate, nr.Path, nr.WavelengthNo, nr.RoutePriority,
			               cd.CircuitId from (select * from NetworkRoute where NetworkId=6)
			               nr left join CircuitDemandMapping cd on nr.DemandId=cd.DemandId where 
			   			nr.NetworkId=cd.NetworkId and nr.Path LIKE '1,%' and nr.PATH NOT LIKE '%,1,%')
			                as one
			   			left join
			   			(SELECT Circuit.NetworkId, Circuit.CircuitId, Circuit.ProtectionType, Circuit10gAgg.TrafficType,Circuit10gAgg.Circuit10gAggId 
			   			 FROM Circuit10gAgg 
			   			 LEFT JOIN Circuit ON  Circuit.CircuitId = Circuit10gAgg.CircuitId and Circuit.NetworkId = Circuit10gAgg.NetworkId 
			   			 where Circuit.NetworkId= 6) cir on one.CircuitId=cir.CircuitId  
			   			 and one.NetworkId=cir.NetworkId  where cir.CircuitId IS NOT NULL group by  one.NetworkId, one.DemandId, one.LineRate, one.Path, one.WavelengthNo, 
			   	         one.RoutePriority,cir.TrafficType, cir.ProtectionType order by DemandId, TrafficType, RoutePriority;  
	 * @author hp
	 */
	public List<Map<String, Object>> findRoutesWithServiceBasedNodeEnds10GAgg(int networkid , int nodeid) {		 	
	   
		logger.info("findRoutesWithServiceBasedNodeEnds10GAgg");
		
	   String strWithStartNode = nodeid+",%";
	   String strWithMiddleNode = "%,"+nodeid+",%";
	   
	   String sql = "\n" + 
			   "select one.NetworkId, one.DemandId, one.LineRate, one.Path, one.WavelengthNo, one.RoutePriority, \n" + 
			   "			group_concat(cir.Circuit10gAggId order by cir.Circuit10gAggId asc) as CircuitId, group_concat(DISTINCT cir.CircuitId) as CircuitOriginalId, cir.TrafficType, cir.ProtectionType\n" + 
			   "			from \n" + 
			   "            (select nr.NetworkId, nr.DemandId, nr.LineRate, nr.Path, nr.WavelengthNo, nr.RoutePriority,\n" + 
			   "            cd.CircuitId from (select * from NetworkRoute where NetworkId=?)\n" + 
			   "            nr left join CircuitDemandMapping cd on nr.DemandId=cd.DemandId where  \n" + 
			   "			nr.NetworkId=cd.NetworkId and nr.Path LIKE ? and nr.PATH NOT LIKE ?)\n" + 
			   "             as one\n" + 
			   "			left join\n" + 
			   "			(SELECT Circuit.NetworkId, Circuit.CircuitId, Circuit.ProtectionType, Circuit10gAgg.TrafficType,Circuit10gAgg.Circuit10gAggId \n" + 
			   "			 FROM Circuit10gAgg "+
			   "			 LEFT JOIN Circuit ON  Circuit.CircuitId = Circuit10gAgg.CircuitId and Circuit.NetworkId = Circuit10gAgg.NetworkId "+
			   "			 where Circuit.NetworkId= ?) cir on one.CircuitId=cir.CircuitId  "+
			   "			 and one.NetworkId=cir.NetworkId  where cir.CircuitId IS NOT NULL group by  one.NetworkId, one.DemandId, one.LineRate, one.Path, one.WavelengthNo, "+
			   "	         one.RoutePriority,cir.TrafficType, cir.ProtectionType order by DemandId, TrafficType, RoutePriority"; 
	   
	   logger.info("findRoutesWithServiceBasedNodeEnds10GAgg"+sql);
	   
	   List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, networkid,strWithStartNode,strWithMiddleNode,networkid);
	  // System.out.println(" findRoutesWithServiceBasedNodeEnds10GAgg list ===> " + list);
	   return list;
   }

	 
 /******************************************************************Below : Changes req******************************************************************************************/
	 /**
	  * @brief Added in order to remove redundant and Redundant Circuit Delete and Circuit Preserve Logic 
	  * 	   circuits after Map update
	  * @param NetworkId
	  * @throws SQLException
	  */
	 public void deleteRedundantCircuit(int NetworkId) throws SQLException{
		 logger.info("Delete Redundant Circuit for NetworkId : "+NetworkId);
		 String sql = "delete  from Circuit10gAgg where ((Circuit.NetworkId = ?) and (Circuit.SrcNodeId not in (select Node.NodeId from Node where Node.NodeType!=3) or"
				  +" Circuit.DestNodeId  not in (select Node.NodeId from Node where Node.NodeType!=3)))";
		 
		 int deletedRowCount = jdbcTemplate.update(sql, NetworkId);
		 logger.info("deletedRowCount : "+deletedRowCount);
		 
	 }
	 
	
	 
	 /**
	  * 
	  * @param networkid
	  */
	 public void deleteInMemoryCircuitsByNetworkId(int networkid) {
		 logger.info("Delete InMemoryCircuit");
		 String sql = "delete from CircuitInMemoryTable where NetworkId = ?";
		 jdbcTemplate.update(sql,networkid);      
	 }
	 
	 

	 
	
	 

	 
	 /**
	  * @brief Modified for the Brownfield Case :  It return the max Circuit Id from Both the Network GF and BF 
	  * @param networkid
	  * @query SELECT MAX(CircuitId)+1 FROM Circuit where NetworkId = GreenFieldId or NetworkId = BrownFieldId;
	  * @return
	  */
	 public int getNextCircuitIdForBFNetwork(int networkid, HashMap<String, Object> networkInfoMap) {
	     
		 logger.info("getNextCircuitIdForBFNetwork" + networkid);
	        
	        logger.info(" GreenField Id "+ (int)networkInfoMap.get(MapConstants.GreenFieldId)
			  +" BrownField Id "+ (int)networkInfoMap.get(MapConstants.BrownFieldId));

	        
	        String sql = "SELECT MAX(CircuitId)+1 FROM Circuit where NetworkId = ? or NetworkId = ?";
	        try
	        {
	        	return Integer.parseInt(jdbcTemplate.queryForObject(sql, Integer.class, 
	        			(int)networkInfoMap.get(MapConstants.GreenFieldId),
	        			(int)networkInfoMap.get(MapConstants.BrownFieldId)).toString());		       
	        }
	        catch(EmptyResultDataAccessException e) {
	    		return 1;
	    	}	        
	 }
	 
	 
	 public List<Map<String,Object>> findAllGrouped(){
			String sql = "SELECT SrcNodeId, DestNodeId, TrafficType, RequiredTraffic,ProtectionType, ClientProtectionType, ColourPreference, PathType, LineRate,ChannelProtection , COUNT(1) as CNT FROM Circuit GROUP BY SrcNodeId, DestNodeId, TrafficType, RequiredTraffic, ProtectionType, ClientProtectionType, ColourPreference, PathType, LineRate, ChannelProtection HAVING COUNT(1) >= 1; ";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
			return list;
		}
	 
	 public List<Map<String,Object>> findAllGrouped(int networkId){
			String sql = "SELECT group_concat(CircuitId order by CircuitId asc SEPARATOR ',') as CircuitSet ,SrcNodeId, DestNodeId, TrafficType, RequiredTraffic,ProtectionType, ClientProtectionType, ColourPreference, PathType, LineRate,ChannelProtection ,ClientProtection,VendorLabel,LambdaBlocking,NodeInclusion, COUNT(1) as CNT FROM Circuit WHERE NetworkId=? GROUP BY SrcNodeId, DestNodeId, TrafficType, RequiredTraffic, ProtectionType, ClientProtectionType, ColourPreference, PathType, LineRate, ChannelProtection,ClientProtection,VendorLabel,LambdaBlocking,NodeInclusion HAVING COUNT(1) >= 1; ";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,networkId);
			return list;
		}
	 
	 public int countCircuitsByClientProtPerNode(int networkid,int nodeid,String ptype){		 	
			String sql = "SELECT COUNT(*) FROM Circuit where NetworkId = ? and (SrcNodeId = ? or DestNodeId = ? ) and ClientProtectionType = ? ";
			return jdbcTemplate.queryForObject(sql, Integer.class, networkid, nodeid,nodeid, ptype);			
		}
	 
	 public List <Circuit> CircuitsByClientProtPerNode(int networkid,int nodeid,String ptype){		 	
			String sql = "SELECT * FROM Circuit where NetworkId = ? and (SrcNodeId = ? or DestNodeId = ? ) and ClientProtectionType = ? and ClientProtection='Yes' ";
			return jdbcTemplate.query(sql, new BeanPropertyRowMapper(Circuit.class), networkid, nodeid,nodeid, ptype);			
		}
	 
	 public List<Map<String,Object>> groupCircuits(int networkid, String lineRate, float clientRate, int circuitModeGeneration){
		 
		 String circuitTable="";
		 if(circuitModeGeneration == MapConstants.I_ZERO){
			 circuitTable = "Circuit";/**Disk Memory Table for persistence storage*/
		 }
		 else if(circuitModeGeneration == MapConstants.I_ONE){
			 circuitTable = "CircuitInMemoryTable";/**Heap Table just for processing purpose*/
		 }
		 /**Client protection added for grouping */
		 String sql = "select NetworkId, SrcNodeId, DestNodeId,  ProtectionType, ColourPreference, PathType,LineRate, ClientProtectionType, NodeInclusion,ChannelProtection, "
		 		+ " ClientProtection, group_concat(CircuitId order by CircuitId asc SEPARATOR ',') as CircuitSet , count(1) as CircuitCount , sum(RequiredTraffic) as TotalTraffic  from "
				+ circuitTable+" "
		 		+ "where networkid =? and LineRate =? and RequiredTraffic = ? GROUP BY  NetworkId, SrcNodeId, DestNodeId, RequiredTraffic, "
		 		+ "ProtectionType, ClientProtectionType, ColourPreference, PathType, LineRate , ChannelProtection, ClientProtection,NodeInclusion;";
		 
		 ///logger.info(" sql " + sql);
		 List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,networkid,lineRate,clientRate);
		 return list;
		 
	 }
	 
	 public List<Map<String,Object>> groupCircuitsFor10GMpn(int networkid, int circuitModeGeneration){
		 
		 String circuitTable="";
		 if(circuitModeGeneration == MapConstants.I_ZERO){
			 circuitTable = "Circuit";/**Disk Memory Table for persistence storage*/
		 }
		 else if(circuitModeGeneration == MapConstants.I_ONE){
			 circuitTable = "CircuitInMemoryTable";/**Heap Table just for processing purpose*/
		 }
		 
		 String sql = "select NetworkId, SrcNodeId, DestNodeId,  ProtectionType, ColourPreference, NodeInclusion,PathType,"
		 		+ "LineRate, ClientProtectionType, ChannelProtection,ClientProtection, group_concat(CircuitId order by CircuitId asc SEPARATOR ',') as CircuitSet, count(1) as CircuitCount ,"
		 		+ " sum(RequiredTraffic) as TotalTraffic from "+circuitTable+" where LineRate = 10 "
		 		+ "and RequiredTraffic in (1,1.25,2.5) and NetworkId = ? GROUP BY NetworkId, SrcNodeId, DestNodeId,ProtectionType, ClientProtectionType, "
		 		+ "ColourPreference, PathType, LineRate, ChannelProtection, ClientProtection,NodeInclusion ;";
		 
		 logger.info("groupCircuitsFor10GMpn  sql "+sql);
		 List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,networkid);
		 return list;
		 
	 }
	 
	 public List<Circuit> findCircuitsFor10GMPN(int networkid, int srcnode, int destnode,String prottype, String colour,String path,String clientprot,String channelProtection,int circuitModeGeneration){

		 String circuitTable = "";
		 if(circuitModeGeneration == MapConstants.I_ZERO){
			 circuitTable = "Circuit";/**Disk Memory Table for persistence storage*/
		 }
		 else if(circuitModeGeneration == MapConstants.I_ONE){
			 circuitTable = "CircuitInMemoryTable";/**Heap Table just for processing purpose*/
		 }

		 	String sql = "select * from "+ circuitTable +" where NetworkId = ? and SrcNodeId=? and DestNodeId=? and ProtectionType = ? and ColourPreference =? and  PathType =? and ClientProtectionType =? and ChannelProtection = ? and LineRate = 10 and RequiredTraffic in (1,1.25,2.5) order by RequiredTraffic desc,Circuitid asc;";
			List<Circuit> circuits  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Circuit.class),networkid, srcnode,destnode,prottype,colour,path,clientprot,channelProtection);
			return circuits;
		}
	 	 
	 public List<Circuit> findCircuitsFor100GMPN(int networkid, int srcnode, int destnode,String ProtectionType, String ClientProtectionType, String ColourPreference, String PathType, String ChannelProtection  ){
			String sql = "select * from Circuit where NetworkId = ? and SrcNodeId=? and DestNodeId=? and ProtectionType = ? and ClientProtectionType = ? and ColourPreference = ? and PathType = ? and ChannelProtection = ? and LineRate = 100 and RequiredTraffic in (10) order by RequiredTraffic desc,Circuitid asc;";
			List<Circuit> circuits  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Circuit.class),networkid, srcnode,destnode,ProtectionType, ClientProtectionType, ColourPreference, PathType, ChannelProtection);
			logger.info(" findCircuitsFor100GMPN : sql "+ sql);
			logger.info(networkid + srcnode +  destnode + ProtectionType + ClientProtectionType+ColourPreference +PathType + ChannelProtection);
			return circuits;
		}
	 
	 public List<Circuit> findCircuitsFor200GMPN(int networkid, int srcnode, int destnode,String ProtectionType, String ClientProtectionType, String ColourPreference, String PathType, String ChannelProtection){
			String sql = "select * from Circuit where NetworkId = ? and SrcNodeId=? and DestNodeId=? and ProtectionType = ? and ClientProtectionType = ? and ColourPreference = ? and PathType = ? and ChannelProtection = ? and LineRate = 200 and RequiredTraffic in (10) order by RequiredTraffic desc,Circuitid asc;";
			List<Circuit> circuits  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Circuit.class),networkid, srcnode,destnode,ProtectionType, ClientProtectionType, ColourPreference, PathType, ChannelProtection);
			return circuits;
		}
	 
	 public List<Map<String, Object>> findCircuitsForTPN(int networkid, String lineRate, float clientRate, int circuitModeGeneration){
		 
		 	String circuitTable="";
		 	
		 	if(circuitModeGeneration == MapConstants.I_ZERO){
				 circuitTable = "Circuit";/**Disk Memory Table for persistence storage*/
			 }
			 else if(circuitModeGeneration == MapConstants.I_ONE){
				 circuitTable = "CircuitInMemoryTable";/**Heap Table just for processing purpose*/
			 }	 
		 
			String sql = "select * from "+circuitTable+" where NetworkId = ? and LineRate=? and RequiredTraffic=? ;";
			List<Map<String, Object>> circuits  = jdbcTemplate.queryForList(sql, networkid, lineRate,clientRate);
			return circuits;
		}
	
	 /*Query to display the Circuit Capacity //Tanuj*/
	 public int circuitCapacity(int networkid)
	 {		
		 logger.info("Finding Circuit capacity");
		  String sql = "SELECT SUM(RequiredTraffic) from Circuit where NetworkId = ?  "; 
		  return jdbcTemplate.queryForObject(sql, int.class, networkid);		  		        
	 }
	 
	 
	 public List<Circuit> findCircuitsWithClientProt(int networkid, int nodeid, String prottype){
			String sql = "select * from Circuit where NetworkId = ? and ( SrcNodeId=? or DestNodeId=? ) and ClientProtectionType = ? ;";
			List<Circuit> circuits  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Circuit.class),networkid, nodeid,nodeid,prottype);
			return circuits;
		}
	 
	
	 
	 /**
	  * finds the list of added/new circuits of the brownfield
	  * @param networkidGrField
	  * @param networkidBrField
	  * @return
	  */
	 public List <Circuit> findAddedCircuitsInBrField(int networkidGrField, int networkidBrField, int nodeid){       	              
	        String sql = "select * from "
	        		+ "(select cbf.NetworkId , cbf.CircuitId , cbf.SrcNodeId, cbf.DestNodeId, cbf.RequiredTraffic, cbf.TrafficType, cbf.ProtectionType, "
	        		+ "cbf.ClientProtectionType, cbf.ColourPreference, cbf.PathType ,cbf.LineRate, cbf.ChannelProtection,cbf.ClientProtection,cbf.VendorLabel,cbf.LambdaBlocking,cbf.NodeInclusion, c.NetworkId as NetworkIdGf , "
	        		+ "c.CircuitId as CircuitIdGf from ( select * from Circuit where NetworkId = ? and (SrcNodeId = ? or DestNodeId = ?)) as cbf "
	        		+ "left join ( select * from Circuit where NetworkId = ? and (SrcNodeId = ? or DestNodeId = ?)) as c on  (c.CircuitId = cbf.CircuitId )) as t "
	        		+ "where t.NetworkIdGf IS NULL and t.CircuitIdGf IS NULL;";
	        List <Circuit> route;		
	        try
	        {
	        	return route  =  jdbcTemplate.query(sql, new BeanPropertyRowMapper(Circuit.class) ,networkidBrField, nodeid, nodeid, networkidGrField, nodeid, nodeid);	       
	        }
	        catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}	
	    }
	 
	 /**
	  * finds the list of added/new circuits of the brownfield with their demand mapping
	  * @param networkidGrField
	  * @param networkidBrField
	  * @return
	  */
	 public List<DemandMapping> findAddedCircuitsDemMappedInBrField(int networkidGrField, int networkidBrField){       	              
	        String sql = "select tt.NetworkId , tt.CircuitId , tt.SrcNodeId, tt.DestNodeId, tt.RequiredTraffic, tt.TrafficType, tt.ProtectionType, tt.ClientProtectionType," + 
	        		"	 tt.ColourPreference, tt.PathType" + 
	        		"	,tt.LineRate, tt.ChannelProtection, cd.DemandId from " + 
	        		"	(select * from " + 
	        		"	(select cbf.NetworkId , cbf.CircuitId , cbf.SrcNodeId, cbf.DestNodeId, cbf.RequiredTraffic, cbf.TrafficType, cbf.ProtectionType, cbf.ClientProtectionType," + 
	        		"	 cbf.ColourPreference, cbf.PathType" + 
	        		"	,cbf.LineRate, cbf.ChannelProtection, c.NetworkId as NetworkIdGf , c.CircuitId as CircuitIdGf " + 
	        		"	from  " + 
	        		"	( select * from Circuit where NetworkId = ? ) as cbf " + 
	        		"	left join " + 
	        		"	( select * from Circuit where NetworkId = ?  ) as c" + 
	        		"	on  (c.CircuitId = cbf.CircuitId )) as t" + 
	        		"	where t.NetworkIdGf IS NULL and t.CircuitIdGf IS NULL) as tt" + 
	        		"	left join (select * from CircuitDemandMapping where NetworkId = ? ) as cd on cd.CircuitId = tt.CircuitId;";
	        List<DemandMapping> route;		
	        try
	        {
	        	return route  =  jdbcTemplate.query(sql, new BeanPropertyRowMapper(DemandMapping.class), networkidBrField,  networkidGrField, networkidBrField);	       
	        }
	        catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}	
	    }
	 
	 
	 
	 /**
	  * finds the list of added/new circuits of the brownfield with count
	  * @param networkidGrField
	  * @param networkidBrField
	  * @return
	  */
	 public List<Map<String,Object>> findAddedCircuitsInBrFieldGrouped(int networkidGrField, int networkidBrField){       	              
	        String sql = "select t.NetworkId, group_concat(t.CircuitId SEPARATOR ',') as CircuitSet, t.SrcNodeId, t.DestNodeId, t.RequiredTraffic, t.TrafficType, t.ProtectionType, " + 
	        		"t.ClientProtectionType, t.ColourPreference, t.PathType ,t.LineRate, t.ChannelProtection ,t.ClientProtection,t.NodeInclusion,t.VendorLabel,t.LambdaBlocking , COUNT(1) as CNT from " + 
	        		"(select cbf.NetworkId , cbf.CircuitId , cbf.SrcNodeId, cbf.DestNodeId, cbf.RequiredTraffic, cbf.TrafficType, cbf.ProtectionType, " + 
	        		"cbf.ClientProtectionType, cbf.ColourPreference, cbf.PathType ,cbf.LineRate, cbf.ChannelProtection,cbf.ClientProtection,cbf.VendorLabel,cbf.LambdaBlocking,cbf.NodeInclusion, c.NetworkId as NetworkIdGf , " + 
	        		"c.CircuitId as CircuitIdGf from ( select * from Circuit where NetworkId = ? ) as cbf " + 
	        		"left join ( select * from Circuit where NetworkId = ? ) as c on  (c.CircuitId = cbf.CircuitId )) as t " + 
	        		"where t.NetworkIdGf IS NULL and t.CircuitIdGf IS NULL group by t.NetworkId , t.SrcNodeId, t.DestNodeId, t.RequiredTraffic, t.TrafficType, t.ProtectionType, " + 
	        		"t.ClientProtectionType, t.ColourPreference, t.PathType ,t.LineRate, t.ChannelProtection,t.ClientProtection,t.NodeInclusion,t.VendorLabel,t.LambdaBlocking";
	        System.out.println("findAddedCircuitsInBrFieldGrouped Query :"+sql);
	        List<Map<String,Object>> route;		
	        try
	        {
	        	return route  =  jdbcTemplate.queryForList(sql, networkidBrField, networkidGrField);	       
	        }
	        catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}	
	    }
	 
	
	    
    /**
	  * finds the list of unaltered circuits of the GreenField
	  * @param networkidGrField
	  * @param networkidBrField
	  * @return
	  */
	 public List <Circuit> findCommonCircuitsInGreenField(int networkidGrField, int networkidBrField){       	              
	        String sql = "select * from "
	        		+ "(select cbf.NetworkId , cbf.CircuitId , cbf.SrcNodeId, cbf.DestNodeId, cbf.RequiredTraffic, cbf.TrafficType, cbf.ProtectionType, "
	        		+ "cbf.ClientProtectionType, cbf.ColourPreference, cbf.PathType ,cbf.LineRate, cbf.ChannelProtection,cbf.ClientProtection,cbf.VendorLabel,cbf.LambdaBlocking,cbf.NodeInclusion, c.NetworkId as NetworkIdGf , "
	        		+ "c.CircuitId as CircuitIdGf from ( select * from Circuit where NetworkId = ? ) as cbf "
	        		+ "left join ( select * from Circuit where NetworkId = ? ) as c on  (c.CircuitId = cbf.CircuitId )) as t "
	        		+ "where t.NetworkIdGf IS NOT NULL and t.CircuitIdGf IS NOT NULL;";
	        List <Circuit> route;		
	        try
	        {
	        	return route  =  jdbcTemplate.query(sql, new BeanPropertyRowMapper(Circuit.class) ,networkidGrField, networkidBrField);	       
	        }
	        catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}	
	    };	    
	    
	    /**
		  * finds the list of unaltered circuits of the brownfield
		  * @param networkidGrField
		  * @param networkidBrField
		  * @return
		  */
		 public List<DemandMapping> findCommonCircuitsDemMappedInBrField(int networkidGrField, int networkidBrField){       	              
		        String sql = "select tt.NetworkId , tt.CircuitId , tt.SrcNodeId, tt.DestNodeId, tt.RequiredTraffic, tt.TrafficType, tt.ProtectionType, tt.ClientProtectionType, "  
		        		+ "tt.ColourPreference, tt.PathType "  
		        		+ ",tt.LineRate, tt.ChannelProtection, cd.DemandId from " 
		        		+ "(select * from "  
		        		+ "(select cbf.NetworkId , cbf.CircuitId , cbf.SrcNodeId, cbf.DestNodeId, cbf.RequiredTraffic, cbf.TrafficType, cbf.ProtectionType, "
		        		+ "cbf.ClientProtectionType, cbf.ColourPreference, cbf.PathType ,cbf.LineRate, cbf.ChannelProtection, c.NetworkId as NetworkIdGf , "
		        		+ "c.CircuitId as CircuitIdGf from ( select * from Circuit where NetworkId = ? ) as cbf "
		        		+ "left join ( select * from Circuit where NetworkId = ? ) as c on  (c.CircuitId = cbf.CircuitId )) as t "
		        		+ "where t.NetworkIdGf IS NOT NULL and t.CircuitIdGf IS NOT NULL) as tt "
		        		+ "left join (select * from CircuitDemandMapping where NetworkId = ? ) as cd on cd.CircuitId = tt.CircuitId;";
		        List<DemandMapping> route;		
		        try
		        {
		        	return route  =  jdbcTemplate.query(sql, new BeanPropertyRowMapper(DemandMapping.class), networkidBrField, networkidGrField,networkidBrField);	       
		        }
		        catch(EmptyResultDataAccessException e) {
		    		return null;
		    	}	
		    };
	    
		 /**
		  * finds the list of unaltered circuits of the brownfield with count
		  * @param networkidGrField
		  * @param networkidBrField
		  * @return
		  */
		 public List<Map<String,Object>> findCommonCircuitsInBrFieldGrouped(int networkidGrField, int networkidBrField){       	              
//		        String sql = "select t.NetworkId , group_concat(t.CircuitId SEPARATOR ',') as CircuitSet, t.SrcNodeId, t.DestNodeId, t.RequiredTraffic, t.TrafficType, t.ProtectionType,"  
//		        		+ "t.ClientProtectionType, t.ColourPreference, t.PathType ,t.LineRate, t.ChannelProtection , COUNT(1) as CNT from "
//		        		+ "(select cbf.NetworkId , cbf.CircuitId , cbf.SrcNodeId, cbf.DestNodeId, cbf.RequiredTraffic, cbf.TrafficType, cbf.ProtectionType, "
//		        		+ "cbf.ClientProtectionType, cbf.ColourPreference, cbf.PathType ,cbf.LineRate, cbf.ChannelProtection, c.NetworkId as NetworkIdGf , "
//		        		+ "c.CircuitId as CircuitIdGf from ( select * from Circuit where NetworkId = ? ) as cbf "
//		        		+ "left join ( select * from Circuit where NetworkId = ? ) as c on  (c.CircuitId = cbf.CircuitId )) as t "
//		        		+ "where t.NetworkIdGf IS NOT NULL and t.CircuitIdGf IS NOT NULL group by t.NetworkId ,t.SrcNodeId, t.DestNodeId, t.RequiredTraffic, t.TrafficType, t.ProtectionType, "  
//		        		+ "t.ClientProtectionType, t.ColourPreference, t.PathType ,t.LineRate, t.ChannelProtection;";
		        String sql="select t.NetworkId , group_concat(t.CircuitId SEPARATOR ',') as CircuitSet, t.SrcNodeId, t.DestNodeId, t.RequiredTraffic, t.TrafficType," 
						+"t.ProtectionType,t.ClientProtectionType, t.ColourPreference, t.PathType ,t.LineRate, t.ChannelProtection,t.ClientProtection,t.NodeInclusion,t.VendorLabel,t.LambdaBlocking , COUNT(1) as CNT from" 
						+"(select cbf.NetworkId , cbf.CircuitId , cbf.SrcNodeId, cbf.DestNodeId, cbf.RequiredTraffic, cbf.TrafficType, cbf.ProtectionType, cbf.ClientProtectionType," 
						+"cbf.ColourPreference, cbf.PathType ,cbf.LineRate, cbf.ChannelProtection,cbf.ClientProtection,cbf.NodeInclusion,cbf.VendorLabel,cbf.LambdaBlocking, c.NetworkId as NetworkIdGf , c.CircuitId as CircuitIdGf from" 
						+"( select * from Circuit where NetworkId = ? ) as cbf left join ( select * from Circuit where NetworkId = ? ) as c on  (c.CircuitId = cbf.CircuitId )) as t where"
						+" t.NetworkIdGf IS NOT NULL and t.CircuitIdGf IS NOT NULL group by t.NetworkId ,t.SrcNodeId, t.DestNodeId, t.RequiredTraffic, t.TrafficType, t.ProtectionType, t.ClientProtectionType, t.ColourPreference, t.PathType ,t.LineRate, t.ChannelProtection,t.ClientProtection,t.NodeInclusion,t.VendorLabel,t.LambdaBlocking;";
		        System.out.println("findCommonCircuitsInBrFieldGrouped Query :"+sql);
		        List<Map<String,Object>> route;		
		        try
		        {
		        	return route  =  jdbcTemplate.queryForList(sql, networkidBrField, networkidGrField);	       
		        }
		        catch(EmptyResultDataAccessException e) {
		    		return null;
		    	}	
		    }
		 
		 
		 /**
		  * finds the list of Deleted Circuits in the brownfield 
		  * @param networkidGrField
		  * @param networkidBrField
		  * @param nodeid
		  * @return
		  */
		 public List <Circuit> findDeletedCircuitsInBrField(int networkidGrField, int networkidBrField, int nodeid){       
			 String sql = "select * from "
		        		+ "(select gf.NetworkId , gf.CircuitId , gf.SrcNodeId, gf.DestNodeId, gf.RequiredTraffic, gf.TrafficType, gf.ProtectionType, "
		        		+ "gf.ClientProtectionType, gf.ColourPreference, gf.PathType ,gf.LineRate, gf.ChannelProtection, bf.NetworkId as NetworkIdBf , "
		        		+ "bf.CircuitId as CircuitIdBf from ( select * from Circuit where NetworkId = ? and (SrcNodeId = ? or DestNodeId = ?) ) as gf "
		        		+ "left join ( select * from Circuit where NetworkId = ? and (SrcNodeId = ? or DestNodeId = ?) ) as bf on  (gf.CircuitId = bf.CircuitId )) as t "
		        		+ "where t.NetworkIdBf IS NULL and t.CircuitIdBf IS NULL;";
		        
		        List <Circuit> info;		
		        try
		        {
		        	return info  =  jdbcTemplate.query(sql, new BeanPropertyRowMapper(Circuit.class) ,networkidGrField,nodeid,nodeid,networkidBrField, nodeid ,nodeid);	       
		        }
		        catch(EmptyResultDataAccessException e) {
		    		return null;
		    	}	
		    }; 
		    
		    /**
			  * finds the list of Deleted Circuits in the brownfield demand mapped
			  * @param networkidGrField
			  * @param networkidBrField
			  * @param nodeid
			  * @return
			  */
			 public List<DemandMapping> findDeletedCircuitsDemMappedInBrField(int networkidGrField, int networkidBrField){       
				 String sql = "select tt.NetworkId , tt.CircuitId , tt.SrcNodeId, tt.DestNodeId, tt.RequiredTraffic, tt.TrafficType, tt.ProtectionType, tt.ClientProtectionType,   " + 
				 		" tt.ColourPreference, tt.PathType   " + 
				 		" ,tt.LineRate, tt.ChannelProtection, cd.DemandId from  " + 
				 		" (select * from " + 
				 		" (select gf.NetworkId , gf.CircuitId , gf.SrcNodeId, gf.DestNodeId, gf.RequiredTraffic, gf.TrafficType, gf.ProtectionType, " + 
				 		" gf.ClientProtectionType, gf.ColourPreference, gf.PathType ,gf.LineRate, gf.ChannelProtection, bf.NetworkId as NetworkIdBf , " + 
				 		" bf.CircuitId as CircuitIdBf from ( select * from Circuit where NetworkId = ?  ) as gf " + 
				 		" left join ( select * from Circuit where NetworkId = ? ) as bf on  (gf.CircuitId = bf.CircuitId )) as t " + 
				 		" where t.NetworkIdBf IS NULL and t.CircuitIdBf IS NULL) as tt " + 
				 		" left join (select * from CircuitDemandMapping where NetworkId = ? ) as cd on cd.CircuitId = tt.CircuitId;";
			        
				 List<DemandMapping> info;		
			        try
			        {
			        	return info  =  jdbcTemplate.query(sql, new BeanPropertyRowMapper(DemandMapping.class), networkidGrField,networkidBrField, networkidGrField);	       
			        }
			        catch(EmptyResultDataAccessException e) {
			    		return null;
			    	}	
			    }; 
			    
		    
		    
		    
		    /**
			  * finds the list of Deleted Circuits in the brownfield 
			  * @param networkidGrField
			  * @param networkidBrField
			  * @param nodeid
			  * @return
			  */
			 public  List<Map<String,Object>> findDeletedCircuitsInBrFieldGrouped(int networkidGrField, int networkidBrField){       
				 String sql = "select * from "
			        		+ "(select gf.NetworkId , group_concat(t.CircuitId SEPARATOR ',') as CircuitSet, gf.SrcNodeId, gf.DestNodeId, gf.RequiredTraffic, gf.TrafficType, gf.ProtectionType, "
			        		+ "gf.ClientProtectionType, gf.ColourPreference, gf.PathType ,gf.LineRate, gf.ChannelProtection, bf.NetworkId as NetworkIdBf , "
			        		+ "bf.CircuitId as CircuitIdBf from ( select * from Circuit where NetworkId = ? ) as gf "
			        		+ "left join ( select * from Circuit where NetworkId = ? ) as bf "
			        		+ "on  (gf.CircuitId = bf.CircuitId )) as t "
			        		+ "where t.NetworkIdBf IS NULL and t.CircuitIdBf IS NULL group by t.NetworkId ,t.SrcNodeId, t.DestNodeId, t.RequiredTraffic, t.TrafficType, t.ProtectionType, "
			        		+ "t.ClientProtectionType, t.ColourPreference, t.PathType ,t.LineRate, t.ChannelProtection;;";
			        
				 List<Map<String,Object>> info;		
			        try
			        {
			        	return info  =  jdbcTemplate.queryForList(sql, networkidGrField,networkidBrField);	       
			        }
			        catch(EmptyResultDataAccessException e) {
			    		return null;
			    	}	
			    }; 

	 /***
	  * 
	  *    @brief  These APIs are called to deal with In Memory Table(Name: CircuitInMemoryTable)
	  *            which is used for Circuit Processing
	  *    @date   29/9/17	
	  *    @author hp
	  */
	    
	  public void insertInMemoryCircuit(Circuit circuit) throws SQLException {

	      String sql = "INSERT into CircuitInMemoryTable(NetworkId, CircuitId, SrcNodeId, DestNodeId, RequiredTraffic, TrafficType, ProtectionType, ClientProtectionType, ColourPreference, PathType, LineRate, ChannelProtection,ClientProtection,LambdaBlocking,VendorLabel,NodeInclusion) VALUES ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	      jdbcTemplate.update(
	              sql,
	              new Object[] { circuit.getNetworkId(), circuit.getCircuitId(), circuit.getSrcNodeId(), circuit.getDestNodeId(), circuit.getRequiredTraffic(), circuit.getTrafficType(), circuit.getProtectionType(), circuit.getClientProtectionType(), circuit.getColourPreference(),circuit.getPathType(),circuit.getLineRate(),circuit.getChannelProtection(),circuit.getClientProtection(),circuit.getLambdaBlocking(),circuit.getVendorlabel(),circuit.getNodeInclusion() });
	  }
 
	 @SuppressWarnings({ "unchecked", "rawtypes" })
	 public List<Circuit> findAllInMemoryCircuit(){
		 String sql = "SELECT * FROM CircuitInMemoryTable";
		 List<Circuit> circuits  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Circuit.class));
		 return circuits;
	 }
	 
	 public void copyCircuitDataInBrField(int networkidGrField, int networkidBrField ) throws SQLException {			
		 	
		    String sql = "insert into Circuit ( NetworkId, CircuitId, SrcNodeId, DestNodeId, RequiredTraffic, TrafficType, ProtectionType, ClientProtectionType, ColourPreference, PathType, LineRate, ChannelProtection,ClientProtection,LambdaBlocking,VendorLabel,NodeInclusion ) select ?, CircuitId, SrcNodeId, DestNodeId, RequiredTraffic, TrafficType, ProtectionType, ClientProtectionType, ColourPreference, PathType, LineRate, ChannelProtection,ClientProtection,LambdaBlocking,VendorLabel,NodeInclusion from Circuit where NetworkId = ? ";
		    logger.info("copyCircuitDataInBrField: "+sql); 	     
		    jdbcTemplate.update( sql,networkidBrField,networkidGrField);
 	 }
	 
	 
	 
	 /**
	  * 
	  * @desc Api to find the modified circuits in brownfield
	  * @query 
	  *        select tt.NetworkId , tt.CircuitId , tt.SrcNodeId, tt.DestNodeId, tt.RequiredTraffic, tt.TrafficType, tt.ProtectionType, tt.ClientProtectionType,
	        			 tt.ColourPreference, tt.PathType
	        			,tt.LineRate, tt.ChannelProtection, cd.DemandId from 
	        			(select * from 
	        			(select cbf.NetworkId , cbf.CircuitId , cbf.SrcNodeId, cbf.DestNodeId, cbf.RequiredTraffic, cbf.TrafficType,
                        cbf.ProtectionType, cbf.ClientProtectionType,
	        			 cbf.ColourPreference, cbf.PathType
	        			,cbf.LineRate, cbf.ChannelProtection, c.NetworkId as NetworkIdGf , c.CircuitId as CircuitIdGf 
	        			from  
	        			( select * from Circuit where NetworkId = 2 ) as cbf 
	        			left join 
	        			( select * from Circuit where NetworkId = 1  ) as c
	        			on  (c.CircuitId = cbf.CircuitId ) where   c.RequiredTraffic<>cbf.RequiredTraffic or 
						c.TrafficType<>cbf.TrafficType or c.ProtectionType<>cbf.ProtectionType
                        or c.ClientProtectionType<>cbf.ClientProtectionType  
                        or c.ColourPreference<>cbf.ColourPreference 
                        or c.PathType<>cbf.PathType 
                        or c.LineRate<>cbf.LineRate 
                        or c.ChannelProtection<>cbf.ChannelProtection ) as t
	        			where t.NetworkIdGf IS NOT NULL and t.CircuitIdGf IS NOT NULL) as tt
	        			left join (select * from CircuitDemandMapping where NetworkId = 2 ) as cd on cd.CircuitId = tt.CircuitId;     

	  * @date 12th July, 2018
	  * @author hp
	  * @param networkidGrField
	  * @param networkidBrField
	  * @return
	  */
	 public List<DemandMapping> findModifiedCircuitsDemMappedInBrField(int networkidGrField, int networkidBrField){
		 
		 String sql = "select tt.NetworkId , tt.CircuitId , tt.SrcNodeId, tt.DestNodeId, tt.RequiredTraffic, tt.TrafficType, tt.ProtectionType, tt.ClientProtectionType," + 
		 		"	        			 tt.ColourPreference, tt.PathType" + 
		 		"	        			,tt.LineRate, tt.ChannelProtection, cd.DemandId from " + 
		 		"	        			(select * from " + 
		 		"	        			(select cbf.NetworkId , cbf.CircuitId , cbf.SrcNodeId, cbf.DestNodeId, cbf.RequiredTraffic, cbf.TrafficType," + 
		 		"                        cbf.ProtectionType, cbf.ClientProtectionType," + 
		 		"	        			 cbf.ColourPreference, cbf.PathType" + 
		 		"	        			,cbf.LineRate, cbf.ChannelProtection, c.NetworkId as NetworkIdGf , c.CircuitId as CircuitIdGf " + 
		 		"	        			from  " + 
		 		"	        			( select * from Circuit where NetworkId = ? ) as cbf " + 
		 		"	        			left join " + 
		 		"	        			( select * from Circuit where NetworkId = ?  ) as c" + 
		 		"	        			on  (c.CircuitId = cbf.CircuitId ) where   c.RequiredTraffic<>cbf.RequiredTraffic or " + 
		 		"						c.TrafficType<>cbf.TrafficType or c.ProtectionType<>cbf.ProtectionType" + 
		 		"                        or c.ClientProtectionType<>cbf.ClientProtectionType  " + 
		 		"                        or c.ColourPreference<>cbf.ColourPreference " + 
		 		"                        or c.PathType<>cbf.PathType " + 
		 		"                        or c.LineRate<>cbf.LineRate " + 
		 		"                        or c.ChannelProtection<>cbf.ChannelProtection ) as t" + 
		 		"	        			where t.NetworkIdGf IS NOT NULL and t.CircuitIdGf IS NOT NULL) as tt \n" + 
		 		"	        			left join (select * from CircuitDemandMapping where NetworkId = ? ) as cd on cd.CircuitId = tt.CircuitId;    \n ";
		 
		 
		 	List<DemandMapping> info;	
		 	
	        try
	        {
	        	return info  =  jdbcTemplate.query(sql, new BeanPropertyRowMapper(DemandMapping.class), networkidGrField,networkidBrField, networkidGrField);	       
	        }
	        catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}	
		 
		 
		 		
		 
	 }
	 
	 public void updateAggClientMappingView() {
	        logger.debug("Find All Circuit10gAggDao");
	        String sql = "select distinct P.NetworkId,P.NodeId,P.Rack,P.SbRack,P.Card,P.CardType,P.Port,P.LinePort,P.EquipmentId,P.CircuitId,\r\n" + 
	        		"	P.DemandId,P.Direction,P.MpnPortNo,C.Rack as MpnRack,C.SbRack as MpnSbRack,C.Card as MpnCard,C.LinePort as MpnLinePort,C.Wavelength as MpnWavelength,C.DemandId as MpnDemandId,C.CircuitId as MpnCircuitId from (select * from ports where MpnPortNo!=0) P left join \r\n" + 
	        		"	( select Z.LinePort,Z.Rack,Z.SbRack,Z.Card,Z.DemandId,Z.CircuitId,Z.Direction,Q.Wavelength,Q.NetworkId,Q.NodeId from (select * from ports where MpnPortNo=0) Z left join (select * from cards) Q \r\n" + 
	        		"	on Z.NetworkId=Q.NetworkId and Z.NodeId=Q.NodeId and Z.Rack=Q.Rack and Z.SbRack=Q.SbRack and Z.Card=Q.Card ) C on P.NetworkId=C.NetworkId and P.NodeId=C.NodeId and P.DemandId=C.DemandId and P.Direction=C.Direction where C.CircuitId in (Select distinct CircuitId from circuit10gagg where NetworkId=C.NetworkId);";
	        try {
				jdbcTemplate.update(sql);
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e);
			}
	        
	}
	 
public AggregatorClientMap findMpnDetailsFromAggClientMapping(PortInfo port){
		 
		 String sql = "select * from aggclientsmapping where NetworkId=? and NodeId=? and Rack=? and SbRack=? and Card=? and CircuitId=?;";
		 
		 
		 	AggregatorClientMap info;	
		 	
	        try
	        {
	        	return info  =  (AggregatorClientMap) jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper(AggregatorClientMap.class), port.getNetworkId(),port.getNodeId(), port.getRack(),port.getSbrack(),port.getCard(),port.getCircuitId());	       
	        }
	        catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}		 		
		 
	 }
}
