package application.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import application.MainMap;
import application.constants.MapConstants;
import application.constants.ResourcePlanConstants;
import application.constants.SMConstants;
import application.model.Demand;

@Component
public class DemandDao{
	@Autowired
    private JdbcTemplate jdbcTemplate;
	public static Logger logger = Logger.getLogger(ResourcePlanConstants.DatabasePrintStr);
	
	
	 public void queryDemands() {
	        logger.info("Query Demands");
	        String sql = "SELECT * FROM Demand";
	        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
	        for (Map<String, Object> row : list) {	        	
	            logger.info(" NetworkId: "+row.get("NetworkId")+" Demand Id: "+row.get("DemandId"));
	        }
	    }
	 
	 public List<Map<String, Object>> findAllDemands() {
	        logger.info("Find All Demands");
	        String sql = "SELECT * FROM Demand";
	        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
	        return list;
	    }
	 
	 public List<Demand> findAll(){
			String sql = "SELECT * FROM Demand";
			List<Demand> demands  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Demand.class));
			return demands;
		}
	 
	 public List<Demand> findAllByNetworkId(int networkid){
			String sql = "SELECT * FROM Demand where NetworkId = ?";
			List<Demand> demands  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Demand.class),networkid);
			return demands;
		}
	 
	 public List<Demand> findAllByNodeId(int networkid,int nodeid){
			String sql = "SELECT * FROM Demand where NetworkId = ? and (SrcNodeId =? or DestNodeId = ?) ";
			List<Demand> demands  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Demand.class),networkid, nodeid, nodeid);
			return demands;
		}
	 
	 public int insertDemand(final Demand demand, HashMap<String, Object> networkInfoMap) throws SQLException {
     logger.info("Insert Demand ");
     int id = 0;
     Object val=null;
     
     if(((String)networkInfoMap.get(MapConstants.NetworkType)).equalsIgnoreCase(MapConstants.GreenField)){
    	 val = getNextDemandIdForNetwork(demand.getNetworkId());	 
     }
     else if(((String)networkInfoMap.get(MapConstants.NetworkType)).equalsIgnoreCase(MapConstants.BrownField)){     
    	 val = getNextDemandIdForBFNetwork(demand.getNetworkId(), networkInfoMap);
     }
     
     if(val!=null)
     {
    	 id=Integer.parseInt(val.toString());
     }
     else
     {    	 
    	 id=1;
     }  
     logger.info(" Id in insertDemand "+ id);
     
     String sql = "INSERT into Demand(NetworkId, DemandId, SrcNodeId, DestNodeId, RequiredTraffic, ProtectionType, ColourPreference, CircuitSet,PathType,LineRate,ClientProtectionType, ChannelProtection,ClientProtection,LambdaBlocking,NodeInclusion) VALUES ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
     jdbcTemplate.update(
             sql,
             new Object[] { demand.getNetworkId(),id, demand.getSrcNodeId(), demand.getDestNodeId(), demand.getRequiredTraffic(), demand.getProtectionType(), demand.getColourPreference(), demand.getCircuitSet(), demand.getPathType(),demand.getLineRate(),demand.getClientProtectionType(), demand.getChannelProtection(),demand.getClientProtection(),demand.getLambdaBlocking(),demand.getNodeInclusion() });
     
     return id; /**Added to keep track of the demand inserted*/
 }
	 
	 
	 public void insertCommonGreenFieldDemand(Demand demand) throws SQLException {
	     logger.info("Insert CommonGreenFieldDemand ");	    
	     
	     String sql = "INSERT into Demand(NetworkId, DemandId, SrcNodeId, DestNodeId, RequiredTraffic, ProtectionType, ColourPreference, CircuitSet,PathType,LineRate,ClientProtectionType, ChannelProtection,ClientProtection,LambdaBlocking,NodeInclusion) VALUES ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	     jdbcTemplate.update(
	             sql,
	             new Object[] { demand.getNetworkId(),demand.getDemandId(), demand.getSrcNodeId(), demand.getDestNodeId(), demand.getRequiredTraffic(), demand.getProtectionType(), demand.getColourPreference(), demand.getCircuitSet(), demand.getPathType(),demand.getLineRate(),demand.getClientProtectionType(), demand.getChannelProtection(),demand.getClientProtection(),demand.getLambdaBlocking(),demand.getNodeInclusion() });	     
	     
	 }

	 public void deleteByNetworkId(int networkid) {
	        logger.info("Delete Demand");
	        String sql = "delete from Demand where NetworkId = ?";
	        jdbcTemplate.update(sql, networkid);      
	    }
	 
	 public void deleteDemand(int networkid, int demandid) {
	        logger.info("Delete Demand");
	        String sql = "delete from Demand where NetworkId = ? and DemandId = ?";
	        jdbcTemplate.update(sql, networkid, demandid);      
	    }
	 
	 public int count()
	 {		
		 logger.info("Count Demand");
		  String sql = "select count(*) from Demand"; 
		  return jdbcTemplate.queryForObject(sql, int.class);		  		        
	 }
	 
	 public int count(int networkId)
	 {		
		 logger.info("Count Demand for NetworkId:"+networkId);
		  String sql = "select count(*) from Demand where NetworkId=?"; 
		  return jdbcTemplate.queryForObject(sql, int.class,networkId);		  		        
	 }
	 
	 public int count(int networkid, int nodeid)
	 {		
		 logger.info("Count Demand");
		  String sql = "select count(*) from Demand where Networkid = ? and ( SrcNodeId = ? or DestNodeId = ? )"; 
		  return jdbcTemplate.queryForObject(sql, int.class, networkid, nodeid, nodeid);		  		        
	 }
	 
		public Demand FindDemand(int networkid, int demandid) {  
//	        logger.info("FindDemand");
	        Demand d;
	        String sql = "select * from Demand where NetworkId = ? and DemandId = ? ";
//	        logger.info("DemandDao.FindDemand() "+ sql);
	        logger.info(" Nid and did : "+ networkid + " and "+demandid);
	        try
	        {
	        	d = (Demand) jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper(Demand.class), networkid, demandid);	        			       
	        }
	        catch(Exception e) {
	        	//e.printStackTrace();
	    		return null;
	    	}			
			return d;	        
	    }
		
		@SuppressWarnings("unchecked")
		public Demand FindDemand(int networkid, int src, int dest, String protType, 
				String clientProtType, String colorPref, String pathType, int lineRate, String channelProtection ,String clientProtection ) {  
			
			Demand d;
	        String sql = "select * from Demand where NetworkId = ? and SrcNodeId = ?  and DestNodeId = ? and ProtectionType = ? "
	        		+ " and ColourPreference = ?  and PathType = ? and LineRate = ? and ClientProtectionType = ? and ChannelProtection = ?  and ClientProtection = ? and RequiredTraffic < 100 ;";
	        
	        logger.info(" Sql to find Demand = "+ sql);
	        
	        try
	        {
	        	d = (Demand) jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper(Demand.class), networkid, src, dest, protType, 
	        											colorPref, pathType, lineRate, clientProtType, channelProtection,clientProtection );
	        	return d;
	        }
	        catch(EmptyResultDataAccessException e) {	        	
	    		return null;
	    	}			
				        
	    }
	 
	 
	 public Object getNextDemandIdForNetwork(int networkid) {
//	        logger.info("getNextDemandIdForNetwork");
	        String sql = "SELECT MAX(DemandId)+1 FROM Demand where NetworkId = ? ";
	        try
	        {
	        	return jdbcTemplate.queryForObject(sql, Integer.class, networkid);		       
	        }
	        catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}	        
	    }
	 
	 public int getTotalCapacityForNetwork(int networkid) {
//	        logger.info("getNextDemandIdForNetwork");
	        String sql = "SELECT SUM(RequiredTraffic) FROM Demand where NetworkId = ? ";
	        try
	        {
	        	return jdbcTemplate.queryForObject(sql, Integer.class, networkid);		       
	        }
	        catch(EmptyResultDataAccessException e) {
	    		return 0;
	    	}
	        catch(NullPointerException e){
	        	return 0;
	        }
	    }
	 
	 public Object getNextDemandIdForBFNetwork(int networkid, HashMap<String,Object> networkInfoMap) {
//	        logger.info("getNextDemandIdForNetwork");
	        String sql = "SELECT MAX(DemandId)+1 FROM Demand where NetworkId = ? or NetworkId = ? ";
	        try
	        {
	        	return jdbcTemplate.queryForObject(sql, Integer.class, 
	        			(int)networkInfoMap.get(MapConstants.GreenFieldId),
	        			(int)networkInfoMap.get(MapConstants.BrownFieldId));		       
	        }
	        catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}	        
	    }

	 
	 public Map<String, Object> findDemandsFor10GTpn(int networkid,int nodeid){
			String sql = "SELECT GROUP_CONCAT(SrcNodeId SEPARATOR ',') as Src, GROUP_CONCAT(DestNodeId SEPARATOR ',') as Dest, GROUP_CONCAT(DemandId SEPARATOR ',') as Demands from Demand where "
					+ "CircuitSet IN (select CircuitId from Circuit where RequiredTraffic= ? and LineRate = ? and NetworkId=?) and NetworkId = ? and  (SrcNodeId = ? or DestNodeId = ?) ;";
			Map<String, Object>  demands  = jdbcTemplate.queryForMap(sql, MapConstants.G10, MapConstants.G10, networkid,networkid, nodeid, nodeid);
			return demands;
		}
	 
	 public Map<String, Object> findDemandsFor100GTpn(int networkid,int nodeid){
			String sql = "SELECT GROUP_CONCAT(SrcNodeId SEPARATOR ',') as Src, GROUP_CONCAT(DestNodeId SEPARATOR ',') as Dest, GROUP_CONCAT(DemandId SEPARATOR ',') as Demands from Demand where "
					+ "CircuitSet IN (select CircuitId from Circuit where RequiredTraffic= ? and LineRate = ? and NetworkId=?) and NetworkId = ? and  (SrcNodeId = ? or DestNodeId = ?) ;";
			Map<String, Object>  demands;
			try
	        {
				demands  = jdbcTemplate.queryForMap(sql, MapConstants.G100, MapConstants.G100, networkid,networkid, nodeid, nodeid);
				return demands;
	        }		
			catch(Exception e) {
				logger.error(e.getMessage());
				e.printStackTrace();
	    		return null;
	    	}
		}
	 


	 
	 /**
	  * finds the list of added/new demands of the brownfield
	  * @param networkidGrField
	  * @param networkidBrField
	  * @return
	  */
	 public List <Demand> findAddedDemandsInBrField(int networkidGrField, int networkidBrField){       	              
	        String sql = "select * from " + 
	        		"(select bf.NetworkId, bf.DemandId, bf.SrcNodeId, bf.DestNodeId, bf.RequiredTraffic, bf.ProtectionType, bf.ColourPreference, bf.CircuitSet, " + 
	        		"bf.PathType, bf.LineRate, bf.ClientProtectionType, bf.ChannelProtection, bf.NetworkId as NetworkIdBf, bf.DemandId as DemandIdBf, "+
	        		"gf.NetworkId as NetworkIdGf, gf.DemandId as DemandIdGf from  " + 
	        		"( select * from Demand where NetworkId = ? ) as bf " + 
	        		"left join " + 
	        		"( select * from Demand where NetworkId = ?  ) as gf " + 
	        		"on gf.DemandId = bf.DemandId ) as t " + 
	        		"where t.DemandIdGf is NULL and t.NetworkIdGf is NULL" + 
	        		";";
	        List <Demand> route;		
	        try
	        {
	        	return route  =  jdbcTemplate.query(sql, new BeanPropertyRowMapper(Demand.class) ,networkidBrField, networkidGrField);	       
	        }
	        catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}	
	    }	 
	 
	 
	 /**
	  * finds the list of added/new demands of the brownfield
	  * @param networkidGrField
	  * @param networkidBrField
	  * @return
	  */
	 public List <Demand> findAddedDemandsInBrField(int networkidGrField, int networkidBrField, int nodeid)
	 {       	              
	        String sql = "select * from " + 
	        		"(select bf.NetworkId, bf.DemandId, bf.SrcNodeId, bf.DestNodeId, bf.RequiredTraffic, bf.ProtectionType, bf.ColourPreference, bf.CircuitSet, " + 
	        		"bf.PathType, bf.LineRate, bf.ClientProtectionType, bf.ChannelProtection, bf.NetworkId as NetworkIdBf, bf.DemandId as DemandIdBf, "+
	        		"gf.NetworkId as NetworkIdGf, gf.DemandId as DemandIdGf from  " + 
	        		"( select * from Demand where NetworkId = ? and (SrcNodeId = ? or DestNodeId = ?) ) as bf " + 
	        		"left join " + 
	        		"( select * from Demand where NetworkId = ? and (SrcNodeId = ? or DestNodeId = ?) ) as gf " + 
	        		"on gf.DemandId = bf.DemandId ) as t " + 
	        		"where t.DemandIdGf is NULL and t.NetworkIdGf is NULL" + 
	        		";";
	        List <Demand> demands;		
	        try
	        {
	        	return demands  =  jdbcTemplate.query(sql, new BeanPropertyRowMapper(Demand.class) ,networkidBrField, nodeid, nodeid,networkidGrField, nodeid, nodeid);	       
	        }
	        catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}	
	    }
	 
	 /**
	  * finds the demands in brown field with modified circuitset only
	  * @param networkidGrField
	  * @param networkidBrField
	  * @param nodeid
	  * @return
	  */
	 public List<Demand> findModifiedCircuitSetDemandsInBrField(int networkidGrField, int networkidBrField,int nodeid){
		 String sql = "select * from "
		 		+ "(select bf.NetworkId, bf.DemandId, bf.SrcNodeId, bf.DestNodeId, bf.RequiredTraffic, bf.ProtectionType, bf.ColourPreference, bf.CircuitSet,"
		 		+ " bf.PathType, bf.LineRate, bf.ClientProtectionType, bf.ChannelProtection, bf.NetworkId as NetworkIdBf, bf.DemandId as DemandIdBf, "
		 		+ " gf.NetworkId as NetworkIdGf, gf.DemandId as DemandIdGf from"
		 		+ " ( select * from Demand where NetworkId = ? and (SrcNodeId = ? or DestNodeId = ?)) as bf "
		 		+ " left join "
		 		+ " ( select * from Demand where NetworkId = ? and (SrcNodeId = ? or DestNodeId = ?) ) as gf "
		 		+ " on gf.DemandId = bf.DemandId and gf.SrcNodeId = bf.SrcNodeId and gf.DestNodeId = bf.DestNodeId "
		 		+ " and gf.ProtectionType = bf.ProtectionType and gf.ColourPreference = bf.ColourPreference and gf.PathType =bf.PathType and gf.LineRate = bf.LineRate"
		 		+ " and gf.ClientProtectionType = bf.ClientProtectionType and gf.ChannelProtection = bf.ChannelProtection and gf.CircuitSet!=bf.CircuitSet ) as t "
		 		+ " where t.DemandIdGf is NOT NULL and t.NetworkIdGf is NOT NULL;";

			List<Demand> demands  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Demand.class),networkidBrField,nodeid, nodeid,networkidGrField,nodeid,nodeid);
			return demands;
		}
	 
	 /**
	  * finds the list of deleted demands of the brownfield
	  * @param networkidGrField
	  * @param networkidBrField
	  * @param nodeid
	  * @return
	  */
	 public List <Demand> findDeletedDemandsInBrField(int networkidGrField, int networkidBrField, int nodeid)
	 {       	              
	        String sql = "select * from " + 
	        		"(select gf.NetworkId, gf.DemandId, gf.SrcNodeId, gf.DestNodeId, gf.RequiredTraffic, gf.ProtectionType, gf.ColourPreference, gf.CircuitSet, " + 
	        		"gf.PathType, gf.LineRate, gf.ClientProtectionType, gf.ChannelProtection, bf.NetworkId as NetworkIdBf, bf.DemandId as DemandIdBf from  " + 
	        		"( select * from Demand where NetworkId = ? and (SrcNodeId = ? or DestNodeId = ?)) as gf " + 
	        		"left join " + 
	        		"( select * from Demand where NetworkId = ? and (SrcNodeId = ? or DestNodeId = ?)) as bf " + 
	        		"on gf.DemandId = bf.DemandId ) as t " + 
	        		"where t.DemandIdBf is NULL and t.NetworkIdBf is NULL ;";
	        List <Demand> demands;		
	        logger.info(sql);
	        try
	        {
	        	return demands  =  jdbcTemplate.query(sql, new BeanPropertyRowMapper(Demand.class),networkidGrField,nodeid, nodeid,networkidBrField,nodeid,nodeid );	       
	        }
	        catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}	
	    }
	 
	 
	 /**
	  * @brief  This Section added to support InMemory Demand tables for RWA Newly added Demand Input and Other APIs
	  * @author hp
	  * @date   4th Oct, 2017
	  */
		
	 public void insertInMemoryDemand(Demand demand) throws SQLException {
	     
		 logger.info("Insert InMemory Demand ");
	      
	     String sql = "INSERT into DemandInMemoryTable(NetworkId, DemandId, SrcNodeId, DestNodeId, RequiredTraffic, ProtectionType, ColourPreference, CircuitSet,PathType,LineRate,ClientProtectionType, ChannelProtection,ClientProtection,LambdaBlocking,NodeInclusion) VALUES ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	     jdbcTemplate.update(
	             sql,
	             new Object[] { demand.getNetworkId(),demand.getDemandId(), demand.getSrcNodeId(), demand.getDestNodeId(), demand.getRequiredTraffic(), demand.getProtectionType(), demand.getColourPreference(), demand.getCircuitSet(), demand.getPathType(),demand.getLineRate(),demand.getClientProtectionType(), demand.getChannelProtection(),demand.getClientProtection(),demand.getLambdaBlocking(),demand.getNodeInclusion() });	     
	     
	 }
	 
	 public List<Demand> findAllInMemoryDemandByNetworkId(int networkid){
			String sql = "SELECT * FROM DemandInMemoryTable where NetworkId = ? order by DemandId";
			List<Demand> demands  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Demand.class),networkid);
			return demands;
	 }
	 
	 public void deleteInMemoryDemandTableByNetworkId(int networkid) {
	        logger.info("Delete InMemoryDemandTable");
	        String sql = "delete from DemandInMemoryTable where NetworkId = ?";
	        jdbcTemplate.update(sql, networkid);      
	 }
	 
	 public void updateDemand(Demand d){
		 logger.info("updateDemand ..");
		 String circuitSet = ","+d.getCircuitSet();
		 logger.info(" circuitSet "+ circuitSet + " nid : "+ d.getNetworkId() + " did "+ d.getDemandId());
		 String sql = "update Demand set RequiredTraffic=RequiredTraffic+? ,  CircuitSet=concat(CircuitSet, ? ) where NetworkId= ? and DemandId= ? ;";
		 jdbcTemplate.update(sql, d.getRequiredTraffic(), circuitSet, d.getNetworkId(), d.getDemandId()); 
		 
	 }
	 
	 public void updateDemandForCircuitSet(Demand d){
		 logger.info("updateDemand ..");
		 ///String circuitSet = ","+d.getCircuitSet();
		 logger.info(" circuitSet "+ d.getCircuitSet() + " nid : "+ d.getNetworkId() + " did "+ d.getDemandId()+ " d.getRequiredTraffic() "+d.getRequiredTraffic());
		 String sql = "update Demand set RequiredTraffic = ? ,  CircuitSet= ?  where NetworkId= ? and DemandId= ? ;";
		 jdbcTemplate.update(sql, d.getRequiredTraffic(), d.getCircuitSet(), d.getNetworkId(), d.getDemandId()); 
		 
	 }
	 
	 public void updateCircuitSet(int networkid, int demandid, String circuitSet){		 	 
		 logger.info(" updateDemand For circuitSet "+ circuitSet + " NetworkId : "+ networkid + " Demandid "+ demandid);
		 String sql = "update Demand set  CircuitSet = ? where NetworkId = ? and DemandId = ? ;";
		 jdbcTemplate.update(sql, circuitSet, networkid, demandid); 
		 
	 }
	 
	 public void copyDemandDataInBrField(int networkidGrField, int networkidBrField ) throws SQLException {			
		 	
		    String sql = "insert into Demand ( NetworkId, DemandId, SrcNodeId, DestNodeId, RequiredTraffic, ProtectionType, ColourPreference, CircuitSet,PathType,LineRate,ClientProtectionType, ChannelProtection,ClientProtection,LambdaBlocking,NodeInclusion ) select ?, DemandId, SrcNodeId, DestNodeId, RequiredTraffic, ProtectionType, ColourPreference, CircuitSet,PathType,LineRate,ClientProtectionType, ChannelProtection,ClientProtection,LambdaBlocking,NodeInclusion from Demand where NetworkId = ? ";
		    logger.info("copyDemandDataInBrField: "+sql); 	     
		    jdbcTemplate.update( sql,networkidBrField,networkidGrField);
			}
	 
	 public List<Demand> checkDemandForAddDropCardSetCount(int networkid, int demandid ) throws SQLException {			
		 	
		    String sql = "SELECT * FROM Demand where NetworkId = ? and DemandId=? and (ChannelProtection='Yes')";
		    logger.info("copyDemandDataInBrField: "+sql); 	     
		    List<Demand> demands;
		    logger.info(sql);
	        try
	        {
	        	return demands  =  jdbcTemplate.query( sql,new BeanPropertyRowMapper(Demand.class),networkid,demandid);	       
	        }
	        catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}
		}
	 
	 
}

