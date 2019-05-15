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

import application.MainMap;
import application.constants.MapConstants;
import application.constants.ResourcePlanConstants;
import application.model.Circuit;
import application.model.DemandMapping;

@Component
public class CircuitDao{
	@Autowired
    private JdbcTemplate jdbcTemplate;
	public static Logger logger = Logger.getLogger(ResourcePlanConstants.DatabasePrintStr);
	
	 public void queryCircuits() {
//	        logger.info("Query Circuits");
	        String sql = "SELECT * FROM Circuit";
	        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
	        for (Map<String, Object> row : list) {	        	
	            logger.info(" NetworkId: "+row.get("NetworkId")+" Circuit Id: "+row.get("CircuitId"));
	        }
	    }
	 
	 public List<Map<String, Object>> findAllCircuits() {
//	        logger.info("Find All Circuits");
	        String sql = "SELECT * FROM Circuit";
	        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
	        return list;
	    }
	 
	 public Circuit findCircuit(int networkid, int id) {
//	        logger.info("Find All Circuits");
	        String sql = "SELECT * FROM Circuit where NetworkId = ? and CircuitId = ?";
	       try
	       { 
	      Circuit list = (Circuit) jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper(Circuit.class),networkid, id);
	            return list;
	       }
	       
	      
	       
	       catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}	
	    }
	 
	 @SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Circuit> findAll(int networkId){
			String sql = "SELECT * FROM Circuit where NetworkId = ? ;";
			List<Circuit> circuits  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Circuit.class),networkId);
			return circuits;
		}

	 
	 @SuppressWarnings({ "unchecked", "rawtypes" })
		public List<Circuit> findAll(int networkId,int nodeId){
				String sql = "SELECT * FROM Circuit where NetworkId = ? and (SrcNodeId = ? || DestNodeId = ? );";
				List<Circuit> circuits  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Circuit.class),networkId,nodeId,nodeId);
				return circuits;
			}
	 
	 @SuppressWarnings({ "unchecked", "rawtypes" })
		public List<DemandMapping> findAllTenGAggCircuits(int networkId,int demandId){
				String sql = "SELECT * FROM DemandMapping where NetworkId = ? and DemandId=? and CircuitId in (Select CircuitId from Circuit10gAgg where NetworkId=?);";
				List<DemandMapping> circuits  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(DemandMapping.class),networkId,demandId,networkId);
				return circuits;
			}
	 
	 @SuppressWarnings({ "unchecked", "rawtypes" })
		public List<DemandMapping> findAllTenGAggCircuitsAddedBrField(int networkId,int networkIdBf,int demandId){
				String sql = "select * from  (select cbf.NetworkId , cbf.CircuitId ,cbf.DemandId ,cbf.SrcNodeId, cbf.DestNodeId, cbf.RequiredTraffic, cbf.TrafficType, cbf.ProtectionType, cbf.ClientProtectionType, "+
	        			" cbf.ColourPreference, cbf.PathType "+
	        			",cbf.LineRate, cbf.ChannelProtection,c.NetworkId as NetworkIdGf , "+
	        			"c.CircuitId as CircuitIdGf from ( select * from demandmapping where NetworkId = ? and DemandId=?) as cbf "+
	        			"left join ( select * from demandmapping where NetworkId = ? and DemandId=?) as c on  (c.CircuitId = cbf.CircuitId and c.DemandId = cbf.DemandId )) as t "+ 
	        			"where t.NetworkIdGf IS NULL and t.CircuitIdGf IS NULL and t.CircuitId in (Select CircuitId from circuit10gagg where NetworkId=t.NetworkId)";
				List<DemandMapping> circuits  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(DemandMapping.class),networkIdBf,demandId,networkId,demandId);
				return circuits;
			}
	 
	 @SuppressWarnings({ "unchecked", "rawtypes" })
		public List<DemandMapping> findAllTenGAggCircuitsDeletedBrField(int networkId,int demandId){
				String sql = "SELECT * FROM DemandMapping where NetworkId = ? and DemandId=? and CircuitId in (Select CircuitId from Circuit10gAgg where NetworkId=?);";
				List<DemandMapping> circuits  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(DemandMapping.class),networkId,demandId,networkId);
				return circuits;
			}
	 
	 @SuppressWarnings({ "unchecked", "rawtypes" })
		public List<DemandMapping> findAllTenGAggCircuitsModifiedBrField(int networkId,int demandId){
				String sql = "SELECT * FROM DemandMapping where NetworkId = ? and DemandId=? and CircuitId in (Select CircuitId from Circuit10gAgg where NetworkId=?);";
				List<DemandMapping> circuits  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(DemandMapping.class),networkId,demandId,networkId);
				return circuits;
			}
	 
	/**
	 * Insert Circuit into DB api which returns the inserted Circuit ID 
	 * @param circuit
	 * @return
	 * @throws SQLException
	 */
	 public int insertCircuit(final Circuit circuit, HashMap<String,Object> networkInfoMap) throws SQLException {
		 
		 /**
		  * Check for the Circuit Id First : 
		  * 			  a) Green Field Case [CircuitId = -1] : Insert Circuits by generating the CircuitId 
		  *   	          b) Brown Field Case : 
		  *             				 I) [Circuit Id = Non-Zero]  Copy The Green Field Circuits as it is into BrownField Circuit Table if a corresponding mapping doesn't exist! 
		  *                             II) [CircuitId  = -1]        Newly Added Circuit Insert by Generating the Circuit Id Which Should be Maximum from Both the
		  *                                                          Networks' Circuit Ids 	
		  */
		 int id = 0;
		 
		 
		 if((circuit.getCircuitId() == MapConstants.I_MINUS_ONE)
				 && ((String)networkInfoMap.get(MapConstants.NetworkType)).equalsIgnoreCase(MapConstants.GreenField)){
			 logger.info("GreenField Circuit Case");
		
			 id = getNextCircuitIdForNetwork(circuit.getNetworkId());
			 
		     
		 }
		 else  if(((String)networkInfoMap.get(MapConstants.NetworkType)).equalsIgnoreCase(MapConstants.BrownField)){
			
			 if(circuit.getCircuitId() == MapConstants.I_MINUS_ONE){
				 logger.info(" BrownField Circuit Case - I ");
				 id = getNextCircuitIdForBFNetwork(circuit.getNetworkId(), networkInfoMap);
			 }
			 else if(circuit.getCircuitId() != MapConstants.I_MINUS_ONE){/**Circuit Id Does Exist*/
				 logger.info(" BrownField Circuit Case - II ");
				 id = circuit.getCircuitId();
		     }			 
		 }		 
		 String sql = "INSERT into Circuit(NetworkId, CircuitId, SrcNodeId, DestNodeId,QoS, RequiredTraffic, TrafficType, ProtectionType, ClientProtectionType, ColourPreference, PathType, LineRate, ChannelProtection,ClientProtection,LambdaBlocking,VendorLabel,NodeInclusion) VALUES ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	     jdbcTemplate.update(
	             sql,
	             new Object[] { circuit.getNetworkId(), id, circuit.getSrcNodeId(), circuit.getDestNodeId(),circuit.getQoS(), circuit.getRequiredTraffic(), circuit.getTrafficType(), circuit.getProtectionType(), circuit.getClientProtectionType(), circuit.getColourPreference(),circuit.getPathType(),circuit.getLineRate(),circuit.getChannelProtection(),circuit.getClientProtection(),circuit.getLambdaBlocking(),circuit.getVendorlabel(),circuit.getNodeInclusion() });
	     
	     return id;
 }
	 
	 /**
	  * @brief Added in order to remove redundant and Redundant Circuit Delete and Circuit Preserve Logic 
	  * 	   circuits after Map update
	  * @param NetworkId
	  * @throws SQLException
	  */
	 public void deleteRedundantCircuit(int NetworkId) throws SQLException{
		 logger.info("Delete Redundant Circuit for NetworkId : "+NetworkId);
		 String sql = "delete  from Circuit where ((Circuit.NetworkId = ?) and (Circuit.SrcNodeId not in (select Node.NodeId from Node where Node.NodeType!=3) or"
				  +" Circuit.DestNodeId  not in (select Node.NodeId from Node where Node.NodeType!=3)))";
		 
		 int deletedRowCount = jdbcTemplate.update(sql, NetworkId);
		 logger.info("deletedRowCount : "+deletedRowCount);
		 
	 }

	 public void deleteByNetworkId(int networkid) {
	        logger.info("Delete Circuit");
	        String sql = "delete from Circuit where NetworkId = ?";
	        jdbcTemplate.update(sql, networkid);      
	    }
	 
	 public void deleteCircuit(int networkid, int circuitid) {
	        logger.info("Delete Circuit :"+circuitid);
	        String sql = "delete from Circuit where NetworkId = ? and CircuitId = ? ";
	        jdbcTemplate.update(sql, networkid, circuitid);      
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
	 
	 
	 public int count()
	 {		
		 logger.info("Count Circuit");
		  String sql = "select count(*) from Circuit"; 
		  return jdbcTemplate.queryForObject(sql, int.class);		  		        
	 }
	 
	 public int count(int networkId)
	 {		
		 logger.info("Count Circuit for NetworkId:"+networkId);
		  String sql = "select count(*) from Circuit where NetworkId=?"; 
		  return jdbcTemplate.queryForObject(sql, int.class,networkId);		  		        
	 }
	 

	 public int getNextCircuitIdForNetwork(int networkid) {
	        logger.info("getNextCircuitIdForNetwork");
	        
	        String sql = "SELECT MAX(CircuitId)+1 FROM Circuit where NetworkId = ?";
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
	 
	 public List<Map<String,Object>> groupCircuits(int networkid, String lineRate, float clientRate,int QoS, int circuitModeGeneration){
		 
		 String circuitTable="";
		 if(circuitModeGeneration == MapConstants.I_ZERO){
			 circuitTable = "Circuit";/**Disk Memory Table for persistence storage*/
		 }
		 else if(circuitModeGeneration == MapConstants.I_ONE){
			 circuitTable = "CircuitInMemoryTable";/**Heap Table just for processing purpose*/
		 }
		 /**Client protection added for grouping */
		 String sql = "select VendorLabel, NetworkId, SrcNodeId, DestNodeId,  ProtectionType, ColourPreference, PathType,LineRate, ClientProtectionType, NodeInclusion,ChannelProtection, "
		 		+ " ClientProtection, group_concat(CircuitId order by CircuitId asc SEPARATOR ',') as CircuitSet , count(1) as CircuitCount , sum(RequiredTraffic) as TotalTraffic  from "
				+ circuitTable+" "
		 		+ "where networkid =? and LineRate =? and RequiredTraffic = ? and QoS = 0 GROUP BY  NetworkId, SrcNodeId, DestNodeId, RequiredTraffic, "
		 		+ "ProtectionType, ClientProtectionType, ColourPreference, PathType, LineRate , ChannelProtection, ClientProtection,NodeInclusion, VendorLabel,QoS;";
		 
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
		 
		 String sql = "select NetworkId, VendorLabel, SrcNodeId, DestNodeId,  ProtectionType, ColourPreference, NodeInclusion,PathType,"
		 		+ "LineRate, ClientProtectionType, ChannelProtection,ClientProtection, group_concat(CircuitId order by CircuitId asc SEPARATOR ',') as CircuitSet, count(1) as CircuitCount ,"
		 		+ " sum(RequiredTraffic) as TotalTraffic from "+circuitTable+" where LineRate = 10 "
		 		+ "and RequiredTraffic in (1,1.25,2.5) and NetworkId = ? GROUP BY NetworkId, SrcNodeId, DestNodeId,ProtectionType, ClientProtectionType, "
		 		+ "ColourPreference, PathType, LineRate, ChannelProtection, ClientProtection,NodeInclusion,VendorLabel ;";
		 
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
	
	 
	 /**
	  * @author surya
	  * @param networkid
	  * @param LineRate
	  * @param QoS
	  * @param circuitModeGeneration
	  * @return
	  */
	 public List<Map<String, Object>>  findCircuitsForPOTP(int networkid, String LineRate,int QoS, int circuitModeGeneration)
	 {
		 
		 String circuitTable = "";
		 	
		 	if(circuitModeGeneration == MapConstants.I_ZERO){
				 circuitTable = "Circuit";/**Disk Memory Table for persistence storage*/
			 }
			 else if(circuitModeGeneration == MapConstants.I_ONE){
				 circuitTable = "CircuitInMemoryTable";/**Heap Table just for processing purpose*/
			 }	 
		     
			String sql = "select * from Circuit where NetworkId = ? and LineRate=? and QoS = ?;";
			
			List<Map<String, Object>> circuits  = jdbcTemplate.queryForList(sql, networkid, LineRate,1);
			
		 
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
			String sql = "select * from Circuit where NetworkId = ? and ( SrcNodeId=? or DestNodeId=? ) and ClientProtectionType = ? and CircuitId not in (Select CircuitId from Circuit10gAgg where NetworkId=?);";
			List<Circuit> circuits  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Circuit.class),networkid, nodeid,nodeid,prottype,networkid);
			return circuits;
		}
	 
	 /**
	  * finds the list of added/new circuits of the brownfield
	  * @param networkidGrField
	  * @param networkidBrField
	  * @return
	  */
	 public List <Circuit> findAddedCircuitsInBrField(int networkidGrField, int networkidBrField){       	              
	        String sql = "select * from "
	        		+ "(select cbf.NetworkId , cbf.CircuitId , cbf.SrcNodeId, cbf.DestNodeId, cbf.RequiredTraffic, cbf.TrafficType, cbf.ProtectionType, "
	        		+ "cbf.ClientProtectionType, cbf.ColourPreference, cbf.PathType ,cbf.LineRate, cbf.ChannelProtection,cbf.ClientProtection,cbf.VendorLabel,cbf.LambdaBlocking,cbf.NodeInclusion,c.NetworkId as NetworkIdGf , "
	        		+ "c.CircuitId as CircuitIdGf from ( select * from Circuit where NetworkId = ? ) as cbf "
	        		+ "left join ( select * from Circuit where NetworkId = ? ) as c on  (c.CircuitId = cbf.CircuitId )) as t "
	        		+ "where t.NetworkIdGf IS NULL and t.CircuitIdGf IS NULL;";
	        List <Circuit> route;		
	        try
	        {
	        	return route  =  jdbcTemplate.query(sql, new BeanPropertyRowMapper(Circuit.class) ,networkidBrField, networkidGrField);	       
	        }
	        catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}	
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
	  * finds the list of unaltered circuits of the brownfield
	  * @param networkidGrField
	  * @param networkidBrField
	  * @return
	  */
	 public List <Circuit> findCommonCircuitsInBrField(int networkidGrField, int networkidBrField){       	              
	        String sql = "select * from "
	        		+ "(select cbf.NetworkId , cbf.CircuitId , cbf.SrcNodeId, cbf.DestNodeId, cbf.RequiredTraffic, cbf.TrafficType, cbf.ProtectionType, "
	        		+ "cbf.ClientProtectionType, cbf.ColourPreference, cbf.PathType ,cbf.LineRate, cbf.ChannelProtection,cbf.ClientProtection,cbf.VendorLabel,cbf.LambdaBlocking,cbf.NodeInclusion, c.NetworkId as NetworkIdGf , "
	        		+ "c.CircuitId as CircuitIdGf from ( select * from Circuit where NetworkId = ? ) as cbf "
	        		+ "left join ( select * from Circuit where NetworkId = ? ) as c on  (c.CircuitId = cbf.CircuitId )) as t "
	        		+ "where t.NetworkIdGf IS NOT NULL and t.CircuitIdGf IS NOT NULL;";
	        List <Circuit> route;		
	        try
	        {
	        	return route  =  jdbcTemplate.query(sql, new BeanPropertyRowMapper(Circuit.class) ,networkidBrField, networkidGrField);	       
	        }
	        catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}	
	    };
	    
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
			  * @query 
			  * 		select tt.NetworkId , tt.CircuitId , tt.SrcNodeId, tt.DestNodeId, tt.RequiredTraffic, tt.TrafficType, tt.ProtectionType, tt.ClientProtectionType,    
				 		 tt.ColourPreference, tt.PathType   
				 		 ,tt.LineRate, tt.ChannelProtection, cd.DemandId from  
				 		 (select * from 
				 		 (select gf.NetworkId , gf.CircuitId , gf.SrcNodeId, gf.DestNodeId, gf.RequiredTraffic, gf.TrafficType, gf.ProtectionType,  
				 		 gf.ClientProtectionType, gf.ColourPreference, gf.PathType ,gf.LineRate, gf.ChannelProtection, bf.NetworkId as NetworkIdBf ,  
				 		 bf.CircuitId as CircuitIdBf from ( select * from Circuit where NetworkId = ?  ) as gf 
				 		 left join ( select * from Circuit where NetworkId = ? ) as bf on  (gf.CircuitId = bf.CircuitId )) as t  
				 		 where t.NetworkIdBf IS NULL and t.CircuitIdBf IS NULL) as tt 
				 		 left join (select * from CircuitDemandMapping where NetworkId = ? ) as cd on cd.CircuitId = tt.CircuitId;
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
			        	info  =  jdbcTemplate.query(sql, new BeanPropertyRowMapper(DemandMapping.class), networkidGrField,networkidBrField, networkidGrField);
			        	System.out.println( " Info .. "+ info);
			        	return info;
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

	      String sql = "INSERT into CircuitInMemoryTable(NetworkId, CircuitId,QoS, SrcNodeId, DestNodeId, RequiredTraffic, TrafficType, ProtectionType, ClientProtectionType, ColourPreference, PathType, LineRate, ChannelProtection,ClientProtection,LambdaBlocking,VendorLabel,NodeInclusion) VALUES ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	      //temporary values 0 for Qos to work in brown field
	      jdbcTemplate.update(
	              sql,
	              new Object[] { circuit.getNetworkId(), circuit.getCircuitId(), 0,circuit.getSrcNodeId(), circuit.getDestNodeId(), circuit.getRequiredTraffic(), circuit.getTrafficType(), circuit.getProtectionType(), circuit.getClientProtectionType(), circuit.getColourPreference(),circuit.getPathType(),circuit.getLineRate(),circuit.getChannelProtection(),circuit.getClientProtection(),circuit.getLambdaBlocking(),circuit.getVendorlabel(),circuit.getNodeInclusion() });
	  }
 
	 @SuppressWarnings({ "unchecked", "rawtypes" })
	 public List<Circuit> findAllInMemoryCircuit(){
		 String sql = "SELECT * FROM CircuitInMemoryTable";
		 List<Circuit> circuits  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Circuit.class));
		 return circuits;
	 }
	 
	 public void copyCircuitDataInBrField(int networkidGrField, int networkidBrField ) throws SQLException {			
		 	
		    String sql = "insert into Circuit ( NetworkId, CircuitId, QoS, SrcNodeId, DestNodeId, RequiredTraffic, TrafficType, ProtectionType, ClientProtectionType, ColourPreference, PathType, LineRate, ChannelProtection,ClientProtection,LambdaBlocking,VendorLabel,NodeInclusion ) select ?, CircuitId, SrcNodeId, DestNodeId, RequiredTraffic, TrafficType, ProtectionType, ClientProtectionType, ColourPreference, PathType, LineRate, ChannelProtection,ClientProtection,LambdaBlocking,VendorLabel,NodeInclusion from Circuit where NetworkId = ? ";
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
	 
}
