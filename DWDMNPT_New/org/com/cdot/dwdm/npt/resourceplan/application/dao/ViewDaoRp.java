package application.dao;

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
import application.model.Bom;
import application.model.Circuit;
import application.model.Demand;
import application.model.DemandMapping;
import application.model.RouteMapping;

@Component
public class ViewDaoRp{
	@Autowired
    private JdbcTemplate jdbcTemplate;
	public static Logger logger = Logger.getLogger(ResourcePlanConstants.DatabasePrintStr);
	
	 
	 @SuppressWarnings({ "unchecked", "rawtypes" })
		public List<Bom> findBom(int networkid){
				String sql = "SELECT * FROM Bom where NetworkId = ? order by Category";			
				List<Bom> mapping  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Bom.class),networkid);
				return mapping;
			}
	 
	 public List<Bom> findBomNetworkWise(int networkid){
			String sql = "SELECT NetworkId,Name, sum(Quantity) as Quantity ,Price, sum(TotalPrice) as TotalPrice , PartNo, sum(PowerConsumption) as PowerConsumption,sum(TypicalPower) as TypicalPower,RevisionCode,Category, EquipmentId from Bom where NetworkId = ? group by EquipmentId order by Category;";			
			List<Bom> mapping  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Bom.class),networkid);
			return mapping;
		}
	 
	 
	 
	 
	 public List<Map<String, Object>>findsumTotalPrice(int networkid, int NodeId) {
		logger.info("Finding Total Price");
	        String sql = "SELECT SUM(TotalPrice) FROM Bom where NetworkId = ? group by NodeId";
	        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, networkid);
	        return list;
	    }
	 public List<Map<String, Object>>findTotalTypicalPower(int networkid, int NodeId) {
		MainMap.logger.info("Finding Typical Power");
	        String sql = "SELECT SUM(TypicalPower*Quantity) FROM Bom where NetworkId = ? group by NodeId";
	        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, networkid);
	        return list;
	    }
	 public List<Map<String, Object>>findTotalPowerConsumption(int networkid,int NodeId) {
		logger.info("Finding Total Power Cosumption");
	        String sql = "SELECT SUM(PowerConsumption*Quantity) FROM Bom where NetworkId = ? group by NodeId";
	        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, networkid);
	        return list;
	    }
	 /*Query to find out the maximum cost at a node //Tanuj*/
	 public int maxnodecost(int networkid)
	 {		
		logger.info("Finding maximum node cost");
		  String sql = "SELECT MAX(MaxPrice) AS MaxPrice FROM (SELECT SUM(TotalPrice) AS MaxPrice From Bom where NetworkId = ? "
		  		+ "Group by NodeId) As TMP "; 
		  return jdbcTemplate.queryForObject(sql, int.class, networkid);		  		        
	 }
	 
	 /*Query to find out the minimum cost at a node //Tanuj*/
	 public int minnodecost(int networkid)
	 {		
		logger.info("Finding minimum node cost");
		  String sql = "SELECT MIN(MinPrice) AS MinPrice FROM (SELECT SUM(TotalPrice) AS MinPrice From Bom where NetworkId =?  "
		  		+ "Group by NodeId) As TMP "; 
		  return jdbcTemplate.queryForObject(sql, int.class, networkid);		  		        
	 }
	 /*Query to find out the Total cost at a network //Tanuj*/
	 public int totalcost(int networkid)
	 {		
		logger.info("Finding total network cost");
		  String sql = "SELECT SUM(TotalPrice) From Bom where NetworkId = ? "; 
		  return jdbcTemplate.queryForObject(sql, int.class,networkid);		  		        
	 }
	 
	 
	 /**
	  * finds the list of added/new Bom entries in the brownfield 
	  * @param networkidGrField
	  * @param networkidBrField
	  * @param nodeid
	  * @return
	  */
	 public List <Bom> findAddedBomEntriesInBrField(int networkidGrField, int networkidBrField, int nodeid){       	              
	        String sql = "select * from " + 
	        		"	 (select bf.NetworkId , bf.StationName , bf.SiteName, bf.Name, bf.Quantity, bf.Price, bf.TotalPrice, " + 
	        		"	 bf.PowerConsumption, bf.TypicalPower ,bf.RevisionCode, bf.Category, gf.NetworkId as NetworkIdGf , " + 
	        		"	 gf.PartNo as PartNoGf from ( select * from Bom where NetworkId = ? and NodeId = ? ) as bf " + 
	        		"	 left join ( select * from Bom where NetworkId = ? and NodeId = ? ) as gf on  (gf.PartNo = bf.PartNo )) as t " + 
	        		"	 where t.NetworkIdGf IS NULL and t.PartNoGf IS NULL;";
	        List <Bom> route;		
	        try
	        {
	        	return route  =  jdbcTemplate.query(sql, new BeanPropertyRowMapper(Bom.class) ,networkidBrField, nodeid ,networkidGrField,nodeid);	       
	        }
	        catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}	
	    };
	    
	    /**
		  * finds the list of added/new Bom entries in the brownfield 
		  * @param networkidGrField
		  * @param networkidBrField		  
		  * @return
		  */
		 public List <Bom> findAddedBomEntriesInBrField(int networkidGrField, int networkidBrField){       	              
		        String sql = "select * from " + 
		        		"	 (select bf.NetworkId , bf.NodeId, bf.StationName , bf.SiteName, bf.Name, bf.Quantity, bf.Price, bf.TotalPrice, " + 
		        		"	 bf.PowerConsumption, bf.TypicalPower ,bf.RevisionCode, bf.Category, bf.PartNo, gf.NetworkId as NetworkIdGf , " + 
		        		"	 gf.PartNo as PartNoGf from ( select * from Bom where NetworkId = ? ) as bf " + 
		        		"	 left join ( select * from Bom where NetworkId = ? ) as gf on  (gf.NodeId = bf.NodeId and gf.PartNo = bf.PartNo )) as t " + 
		        		"	 where t.NetworkIdGf IS NULL and t.PartNoGf IS NULL;";
		        List <Bom> route;		
		        try
		        {
		        	return route  =  jdbcTemplate.query(sql, new BeanPropertyRowMapper(Bom.class) ,networkidBrField, networkidGrField);	       
		        }
		        catch(EmptyResultDataAccessException e) {
		    		return null;
		    	}	
		    };
		 
	 
	
	    
	 /**
	  * finds the list of Modified Bom entries in the brownfield 
	  * @param networkidGrField
	  * @param networkidBrField
	  * @param nodeid
	  * @return
	  */
	 public List <Bom> findModifiedBomEntriesInBrField(int networkidGrField, int networkidBrField, int nodeid){       	              
	        String sql = "select * from " + 
	        		"	 (select bf.NetworkId , bf.StationName , bf.SiteName, bf.Name, bf.Quantity, bf.Price, bf.TotalPrice, " + 
	        		"	 bf.PowerConsumption, bf.TypicalPower ,bf.RevisionCode, bf.Category, gf.NetworkId as NetworkIdGf , " + 
	        		"	 gf.PartNo as PartNoGf from ( select * from Bom where NetworkId = ? and NodeId = ? ) as bf " + 
	        		"	 left join ( select * from Bom where NetworkId = ? and NodeId = ? ) as gf " + 
	        		"	 on  (gf.PartNo = bf.PartNo  and gf.Quantity!=bf.Quantity)) as t " + 
	        		"	 where t.NetworkIdGf IS NOT NULL and t.PartNoGf IS NOT NULL;";
	        List <Bom> route;		
	        try
	        {
	        	return route  =  jdbcTemplate.query(sql, new BeanPropertyRowMapper(Bom.class) ,networkidBrField, nodeid ,networkidGrField,nodeid);	       
	        }
	        catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}	
	    };
	    
	    /**
		  * finds the list of Modified Bom entries in the brownfield 
		  * @param networkidGrField
		  * @param networkidBrField		  
		  * @return
		  */
		 public List <Bom> findModifiedBomEntriesInBrField(int networkidGrField, int networkidBrField){       	              
		        String sql = "select * from " + 
		        		"	 (select bf.NetworkId, bf.NodeId , bf.StationName , bf.SiteName, bf.Name, bf.Quantity, bf.Price, bf.TotalPrice, " + 
		        		"	 bf.PowerConsumption, bf.TypicalPower ,bf.RevisionCode, bf.Category, bf.PartNo, gf.NetworkId as NetworkIdGf , " + 
		        		"	 gf.PartNo as PartNoGf from ( select * from Bom where NetworkId = ?  ) as bf " + 
		        		"	 left join ( select * from Bom where NetworkId = ?  ) as gf " + 
		        		"	 on  (gf.NodeId = bf.NodeId and gf.PartNo = bf.PartNo  and gf.Quantity!=bf.Quantity)) as t " + 
		        		"	 where t.NetworkIdGf IS NOT NULL and t.PartNoGf IS NOT NULL;";
		        List <Bom> route;		
		        try
		        {
		        	return route  =  jdbcTemplate.query(sql, new BeanPropertyRowMapper(Bom.class) ,networkidBrField, networkidGrField);	       
		        }
		        catch(EmptyResultDataAccessException e) {
		    		return null;
		    	}	
		    };
	 
		    
	 /**
	  * finds the list of Common Bom entries in the brownfield whose quantities have 
	  * not been changed 
	  * @param networkidGrField
	  * @param networkidBrField
	  * @param nodeid
	  * @return
	  */
	 public List <Bom> findCommonBomEntriesInBrField(int networkidGrField, int networkidBrField, int nodeid){       	              
	        String sql = "select * from " + 
	        		"	 (select bf.NetworkId , bf.StationName , bf.SiteName, bf.Name, bf.Quantity, bf.Price, bf.TotalPrice, " + 
	        		"	 bf.PowerConsumption, bf.TypicalPower ,bf.RevisionCode, bf.Category, gf.NetworkId as NetworkIdGf , " + 
	        		"	 gf.PartNo as PartNoGf from ( select * from Bom where NetworkId = ? and NodeId = ? ) as bf " + 
	        		"	 left join ( select * from Bom where NetworkId = ? and NodeId = ? ) as gf on  (gf.PartNo = bf.PartNo and gf.Quantity=bf.Quantity) ) as t " + 
	        		"	 where t.NetworkIdGf IS NOT NULL and t.PartNoGf IS NOT NULL;";
	        List <Bom> route;		
	        try
	        {
	        	return route  =  jdbcTemplate.query(sql, new BeanPropertyRowMapper(Bom.class) ,networkidBrField, nodeid ,networkidGrField,nodeid);	       
	        }
	        catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}	
	    };
	    
	    /**
		  * finds the list of Common Bom entries in the brownfield whose quantities have 
		  * not been changed 
		  * @param networkidGrField
		  * @param networkidBrField		  
		  * @return
		  */
		 public List <Bom> findCommonBomEntriesInBrField(int networkidGrField, int networkidBrField){       	              
		        String sql = "select * from " + 
		        		"	 (select bf.NetworkId , bf.NodeId , bf.StationName , bf.SiteName, bf.Name, bf.Quantity, bf.Price, bf.TotalPrice, " + 
		        		"	 bf.PowerConsumption, bf.TypicalPower ,bf.RevisionCode, bf.Category, bf.PartNo, gf.NetworkId as NetworkIdGf , " + 
		        		"	 gf.PartNo as PartNoGf from ( select * from Bom where NetworkId = ? ) as bf " + 
		        		"	 left join ( select * from Bom where NetworkId = ?  ) as gf on  (gf.NodeId = bf.NodeId and gf.PartNo = bf.PartNo and gf.Quantity = bf.Quantity) ) as t " + 
		        		"	 where t.NetworkIdGf IS NOT NULL and t.PartNoGf IS NOT NULL;";
		        List <Bom> route;		
		        try
		        {
		        	return route  =  jdbcTemplate.query(sql, new BeanPropertyRowMapper(Bom.class) ,networkidBrField, networkidGrField);	       
		        }
		        catch(EmptyResultDataAccessException e) {
		    		return null;
		    	}	
		    };
	    
	    /**
		  * finds the list of Deleted Bom entries in the brownfield 
		  * @param networkidGrField
		  * @param networkidBrField
		  * @param nodeid
		  * @return
		  */
		 public List <Bom> findDeletedBomEntriesInBrField(int networkidGrField, int networkidBrField, int nodeid){       	              
		        String sql = "select * from " + 
		        		"	    (select gf.NetworkId , gf.StationName , gf.SiteName, gf.Name, gf.Quantity, gf.Price, gf.TotalPrice,  " + 
		        		"	    gf.PowerConsumption, gf.TypicalPower ,gf.RevisionCode, gf.Category, bf.NetworkId as NetworkIdBf ,  " + 
		        		"	    bf.PartNo as PartNoBf from ( select * from Bom where NetworkId = ? and NodeId = ? ) as gf  " + 
		        		"	    left join ( select * from Bom where NetworkId = ? and NodeId = ? ) as bf on  (gf.PartNo = bf.PartNo )) as t " + 
		        		"	    where t.NetworkIdBf IS NULL and t.PartNoBf IS NULL;";
		        List <Bom> route;		
		        try
		        {
		        	return route  =  jdbcTemplate.query(sql, new BeanPropertyRowMapper(Bom.class) ,networkidGrField,nodeid,networkidBrField, nodeid );	       
		        }
		        catch(EmptyResultDataAccessException e) {
		    		return null;
		    	}	
		    }; 
		    
		    /**
			  * finds the list of Deleted Bom entries in the brownfield 
			  * @param networkidGrField
			  * @param networkidBrField			  
			  * @return
			  */
			 public List <Bom> findDeletedBomEntriesInBrField(int networkidGrField, int networkidBrField){       	              
			        String sql = "select * from " + 
			        		"	    (select gf.NetworkId , gf.NodeId , gf.StationName , gf.SiteName, gf.Name, gf.Quantity, gf.Price, gf.TotalPrice,  " + 
			        		"	    gf.PowerConsumption, gf.TypicalPower ,gf.RevisionCode, gf.Category,gf.PartNo, bf.NetworkId as NetworkIdBf ,  " + 
			        		"	    bf.PartNo as PartNoBf from ( select * from Bom where NetworkId = ? ) as gf  " + 
			        		"	    left join ( select * from Bom where NetworkId = ? ) as bf on  (gf.NodeId = bf.NodeId and gf.PartNo = bf.PartNo )) as t " + 
			        		"	    where t.NetworkIdBf IS NULL and t.PartNoBf IS NULL;";
			        List <Bom> route;		
			        try
			        {
			        	return route  =  jdbcTemplate.query(sql, new BeanPropertyRowMapper(Bom.class) ,networkidGrField,networkidBrField );	       
			        }
			        catch(EmptyResultDataAccessException e) {
			    		return null;
			    	}	
	                }
			 
			 public int findTotalPower(int NetworkId)
			 {
				 String Sql = "select sum(PowerConsumption) from Bom where NetworkId = ? Group by NetworkId ";
				 
				 try {
					 return jdbcTemplate.queryForObject(Sql, int.class , NetworkId);
					 
				 }
				 
				 catch (EmptyResultDataAccessException e)
				 {
					 //System.out.println(e);
					 return -1;
				 }
				 
				 
			 }
			 public int findTotalPrice(int NetworkId)
			 {
				 String Sql = "select sum(TotalPrice) from Bom where NetworkId = ? Group by NetworkId ";
				 
				 try {
					 return jdbcTemplate.queryForObject(Sql, int.class , NetworkId);
					 
				 }
				 
				 catch (EmptyResultDataAccessException e)
				 {
					 //System.out.println(e);
					 return -1;
				 }
				 
				 
			 }
			 
			 
	 }
