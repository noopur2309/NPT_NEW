package application.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import application.model.Circuit;
import application.model.Demand;
import application.model.DemandMapping;
import application.model.Network;
import application.model.NetworkRoute;
import application.model.RouteMapping;
import application.MainMap;
import application.constants.MapConstants;
import application.constants.ResourcePlanConstants;
import application.model.CardInfo;

@Component
public class NetworkRouteDao{
	@Autowired
    private JdbcTemplate jdbcTemplate;
	public static Logger logger = Logger.getLogger(ResourcePlanConstants.DatabasePrintStr);
	
	 public void queryNetworkRoutes() {
	        logger.info("Query NetworkRoutes");
	        String sql = "SELECT * FROM NetworkRoute";
	        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
	        for (Map<String, Object> row : list) {	        	
	            logger.info(" NetworkId: "+row.get("NetworkId")+" Demand Id: "+row.get("DemandId")+" Route Priority: "+row.get("RoutePriority"));
	        }
	    }
	 
	 public List<NetworkRoute> findAll(){
			String sql = "SELECT * FROM NetworkRoute";
			@SuppressWarnings({ "unchecked", "rawtypes" })
			List<NetworkRoute> routes  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(NetworkRoute.class));
			return routes;
		}
	 
	 public List<Map<String, Object>> findAllNetworkRoutes() {
	        logger.info("Find All NetworkRoutes");
	        String sql = "SELECT * FROM NetworkRoute";
	        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
	        return list;
	    }
	 
	 public void insertNetworkRoute(final NetworkRoute networkRoute) throws SQLException {
//     logger.info("Insert NetworkRoute ");
     String sql = "INSERT into NetworkRoute(NetworkId, DemandId, RoutePriority, Path,PathLinkId, PathType, Traffic, WavelengthNo, LineRate,  Osnr, RegeneratorLoc, RegeneratorLocOsnr, Error, 3RLocationHeadToTail, 3RLocationTailToHead) VALUES ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

     jdbcTemplate.update(
             sql,
             new Object[] { networkRoute.getNetworkId(), networkRoute.getDemandId(), networkRoute.getRoutePriority(), networkRoute.getPath(),networkRoute.getPathLinkId(), networkRoute.getPathType(), networkRoute.getTraffic(), networkRoute.getWavelengthNo(), networkRoute.getLineRate(), networkRoute.getOsnr(), networkRoute.getRegeneratorLoc(), networkRoute.getRegeneratorLocOsnr(), networkRoute.getError(), networkRoute.getThreeRLocationHeadToTail(), networkRoute.getThreeRLocationTailToHead() });
	 }

	 public void deleteByNetworkId(int networkid) {
	        logger.info("Delete NetworkRoute");
	        String sql = "delete from NetworkRoute where NetworkId = ?";
	        jdbcTemplate.update(sql, networkid);      
	    }
	 
	 public void deleteByNetworkId(int networkid,int demandid) {
	        logger.info("Delete NetworkRoute");
	        String sql = "delete from NetworkRoute where NetworkId = ? and DemandId=?";
	        jdbcTemplate.update(sql, networkid,demandid);      
	    }
	 
	 public List<NetworkRoute> findAllByNetworkId(int networkid) {
//	        logger.info("Query NetworkRoute findAllByNetworkId");
	        String sql = "SELECT * FROM NetworkRoute where NetworkId = ?";
	        @SuppressWarnings({ "rawtypes", "unchecked" })
			List<NetworkRoute> routes  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(NetworkRoute.class),networkid);
			return routes; 
	    }
	 
	 	 
	 public List<NetworkRoute> findAllByDemandId(int networkid,int demandid) {
//	        logger.info("Query NetworkRoute findAllByDemandId");
	        String sql = "SELECT * FROM NetworkRoute where NetworkId = ?  and DemandId = ?";
	        List<NetworkRoute> routes;
	        try
	        {
	        	 return routes  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(NetworkRoute.class),networkid,demandid);
	        }
	        catch(EmptyResultDataAccessException e) {
	    		return null;
	    	} 
	    }
	 
	 public List<NetworkRoute> findAllByDemandIdUsable(int networkid,int demandid) {
//	        logger.info("Query NetworkRoute findAllByDemandId");
	        String sql = "SELECT * FROM NetworkRoute where NetworkId = ?  and DemandId = ? and (RoutePriority = 1 or RoutePriority = 2) ";
	        List<NetworkRoute> routes;
	        try
	        {
	        	 return routes  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(NetworkRoute.class),networkid,demandid);
	        }
	        catch(EmptyResultDataAccessException e) {
	    		return null;
	    	} 
	    }
	 
	 public List<NetworkRoute> findAllErrorPaths(int networkid) {
//	        logger.info("Query NetworkRoute findAllByDemandId");
	        String sql = "SELECT * FROM NetworkRoute where NetworkId = ?  and (RoutePriority < 1 or RoutePriority >4) ";
	        List<NetworkRoute> routes;
	        try
	        {
	        	 return routes  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(NetworkRoute.class),networkid);
	        }
	        catch(EmptyResultDataAccessException e) {
	    		return null;
	    	} 
	    }
	 
	 public NetworkRoute findAllByDemandId(int networkid, int demandid, int routePriority) {
//	        logger.info("Query NetworkRoute findAllByDemandId");
	        logger.info("findAllByDemandId: Network "+networkid+" DemandId "+demandid+" RoutePriority "+routePriority);
	        String sql = "SELECT * FROM NetworkRoute where NetworkId = ?  and DemandId = ? and RoutePriority = ?";
	        NetworkRoute route;		
	        try
	        {
	        	return route  = (NetworkRoute) jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper(NetworkRoute.class) ,networkid, demandid, routePriority);	       
	        }
	        catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}	
	    }
	 
	 public List <NetworkRoute> IsNodeOn10GRoute(int networkid, int nodeid) {
	        logger.info("Query NetworkRoute findAllByDemandId");
	        String str = "%"+nodeid+"%";	       
	        String sql = "SELECT * FROM NetworkRoute where NetworkId = ?  and Path LIKE  ? and LineRate = 10 ";
	        List <NetworkRoute> route;		
	        try
	        {
	        	return route  =  jdbcTemplate.query(sql, new BeanPropertyRowMapper(NetworkRoute.class) ,networkid, str);	       
	        }
	        catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}	
	    }
	
	 
	 public int count()
	 {		
		 logger.info("Count NetworkRoutes");
		  String sql = "select count(*) from NetworkRoute"; 
		  return jdbcTemplate.queryForObject(sql, int.class);		  		        
	 }
	 
	 public int count(int networkId)
	 {		
		 logger.info("Count NetworkRoutes");
		  String sql = "select count(*) from NetworkRoute where NetworkId=?"; 
		  return jdbcTemplate.queryForObject(sql, int.class,networkId);		  		        
	 }
	 
	 /**
	  * Finding paths with a particular nodeid as srcnode
	  * @param networkid
	  * @param nodeid
	  * @return
	  */
	 public List<Map<String, Object>> findRoutesWithNodeEnds_cf(int networkid , int nodeid) {		 	
	        logger.info("findRoutesWithNodeEnds_cf");
	        String str1 = nodeid+",%";
//	        String str2 = "%,"+nodeid;
	        String str3 = "%,"+nodeid+",%";
	        
	        String sql = "SELECT DemandId, Path, WavelengthNo, RoutePriority, LineRate from NetworkRoute where NetworkId = ? and  Path LIKE ? and PATH NOT LIKE ? order by DemandId;";
	        logger.info("findRoutesWithNodeEnds_cf"+sql);
	        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, networkid,str1, str3);
	        return list;
	    }
	 
	 
	 /**
	  * finds the list of added/new network routes of the brownfield
	  * @param networkidGrField
	  * @param networkidBrField
	  * @return
	  */
	 public List <NetworkRoute> findAddedRouteInBrField(int networkidGrField, int networkidBrField){       	              
	        String sql = "select * from (select nrbf.NetworkId , nrbf.DemandId , nrbf.RoutePriority, nrbf.Path, nrbf.PathType, nrbf.Traffic, nrbf.WavelengthNo, nrbf.LineRate, "
	        		+ "nrbf.Osnr, nrbf.RegeneratorLoc ,nrbf.RegeneratorLocOsnr, nrbf.Error, nr.NetworkId as NetworkIdGf , nr.DemandId as DemandIdGf "
	        		+ "from ( select * from NetworkRoute where NetworkId = ? ) as nrbf "
	        		+ " left join "
	        		+ "( select * from NetworkRoute where NetworkId = ?  ) as nr on  "
	        		+ "(nr.DemandId = nrbf.DemandId and nr.RoutePriority = nrbf.RoutePriority )) as t "
	        		+ "where t.NetworkIdGf IS NULL and t.DemandIdGf IS NULL ;";
	        List <NetworkRoute> route;		
	        try
	        {
	        	return route  =  jdbcTemplate.query(sql, new BeanPropertyRowMapper(NetworkRoute.class) ,networkidBrField, networkidGrField);	       
	        }
	        catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}	
	    }
	 
	 /**
	  * finds the list of added/new network routes of the brownfield with demand mapping
	  * to be used in Demand view generation in GUI
	  * @param networkidGrField
	  * @param networkidBrField
	  * @return
	  */
	 public List <RouteMapping> findAddedRouteDemMappedInBrField(int networkidGrField, int networkidBrField){       	              
	        String sql = "select tt.NetworkId , tt.DemandId , tt.Traffic, tt.RoutePriority, tt.Path, tt.PathType, tt.WavelengthNo, tt.Osnr, " + 
	        		"		tt.PathType ,tt.LineRate, tt.RegeneratorLoc, tt.Error, d.SrcNodeId, d.DestNodeId , d.ProtectionType from  " + 
	        		"		(select * from  " + 
	        		"		(select bf.NetworkId , bf.DemandId , bf.Traffic, bf.RoutePriority, bf.Path, bf.PathType, bf.WavelengthNo, bf.Osnr,  " + 
	        		"		bf.LineRate, bf.RegeneratorLoc, bf.Error, gf.NetworkId as NetworkIdGf , gf.DemandId as DemandIdGf from   " + 
	        		"		( select * from NetworkRoute where NetworkId = ? ) as bf  " + 
	        		"		left join  " + 
	        		"		( select * from NetworkRoute where NetworkId = ?  ) as gf " + 
	        		"		on  (gf.DemandId = bf.DemandId and gf.RoutePriority = bf.RoutePriority )) as t " + 
	        		"		where t.NetworkIdGf IS NULL and t.DemandIdGf IS NULL) as tt " + 
	        		"		left join (select * from Demand where NetworkId = ? ) as d on d.DemandId = tt.DemandId;";
	        List <RouteMapping> route;		
	        try
	        {
	        	return route  =  jdbcTemplate.query(sql, new BeanPropertyRowMapper(RouteMapping.class) ,networkidBrField, networkidGrField, networkidBrField);	       
	        }
	        catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}	
	    }
	 
	 /**
	  * finds the list of deleted network routes int the brownfield
	  * @param networkidGrField
	  * @param networkidBrField
	  * @return
	  */
	 public List <NetworkRoute> findDeletedRouteInBrField(int networkidGrField, int networkidBrField){       	              
	        String sql = "select * from (select gf.NetworkId , gf.DemandId , gf.RoutePriority, gf.Path, gf.PathType, gf.Traffic, " + 
	        		"gf.WavelengthNo, gf.LineRate,gf.Osnr, gf.RegeneratorLoc ,gf.RegeneratorLocOsnr, gf.Error, bf.NetworkId as NetworkIdBf , " + 
	        		"bf.DemandId as DemandIdBf " + 
	        		"from ( select * from NetworkRoute where NetworkId = ? ) as gf " + 
	        		"left join " + 
	        		"( select * from NetworkRoute where NetworkId = ?  ) as bf " + 
	        		"on  (gf.DemandId = bf.DemandId and gf.RoutePriority = bf.RoutePriority )) as t " + 
	        		"where t.NetworkIdBf IS NULL and t.DemandIdBf IS NULL ;";
	        List <NetworkRoute> route;		
	        try
	        {
	        	return route  =  jdbcTemplate.query(sql, new BeanPropertyRowMapper(NetworkRoute.class) ,networkidGrField, networkidBrField);	       
	        }
	        catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}	
	    }
	 
	 /**
	  * finds the list of deleted network routes in the brownfield with demand mapping
	  * to be used in Demand view generation in GUI
	  * @param networkidGrField
	  * @param networkidBrField
	  * @return
	  */
	 public List <RouteMapping> findDeletedRouteDemMappedInBrField(int networkidGrField, int networkidBrField){       	              
	        String sql = "select tt.NetworkId , tt.DemandId , tt.Traffic, tt.RoutePriority, tt.Path, tt.PathType, tt.WavelengthNo, tt.Osnr, " + 
	        		"	tt.PathType ,tt.LineRate, tt.RegeneratorLoc, tt.Error, d.SrcNodeId, d.DestNodeId , d.ProtectionType from  " + 
	        		"	(select * from  " + 
	        		"	(select gf.NetworkId , gf.DemandId , gf.Traffic, gf.RoutePriority, gf.Path, gf.PathType, gf.WavelengthNo, gf.Osnr,  " + 
	        		"	gf.LineRate, gf.RegeneratorLoc, gf.Error, bf.NetworkId as NetworkIdBf , bf.DemandId as DemandIdBf from   " + 
	        		"	( select * from NetworkRoute where NetworkId = ? ) as gf  " + 
	        		"	left join  " + 
	        		"	( select * from NetworkRoute where NetworkId = ?  ) as bf " + 
	        		"	on  (gf.DemandId = bf.DemandId and gf.RoutePriority = bf.RoutePriority )) as t " + 
	        		"	where t.NetworkIdBf IS NULL and t.DemandIdBf IS NULL) as tt " + 
	        		"	left join (select * from Demand where NetworkId = ? ) as d on d.DemandId = tt.DemandId;";
	        List <RouteMapping> route;		
	        try
	        {
	        	return route  =  jdbcTemplate.query(sql, new BeanPropertyRowMapper(RouteMapping.class) , networkidGrField, networkidBrField, networkidGrField);	       
	        }
	        catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}	
	    }
	 
	 /**
	  * finds the list of unaltered network routes in the brownfield with demand mapping
	  * to be used in Demand view generation in GUI
	  * @param networkidGrField
	  * @param networkidBrField
	  * @return
	  */
	 public List<NetworkRoute> findCommonRouteInBrField(int networkidGrField, int networkidBrField){       	              
	        String sql = "select * from (select nrbf.NetworkId , nrbf.DemandId , nrbf.RoutePriority, nrbf.Path, nrbf.PathType, nrbf.Traffic, nrbf.WavelengthNo, nrbf.LineRate, " + 
	        		" nrbf.Osnr, nrbf.RegeneratorLoc ,nrbf.RegeneratorLocOsnr, nrbf.Error, nr.NetworkId as NetworkIdGf , nr.DemandId as DemandIdGf " + 
	        		" from ( select * from NetworkRoute where NetworkId = ? ) as nrbf " + 
	        		" left join " + 
	        		" ( select * from NetworkRoute where NetworkId = ?  ) as nr on  " + 
	        		" (nr.DemandId = nrbf.DemandId and nr.RoutePriority = nrbf.RoutePriority )) as t " + 
	        		" where t.NetworkIdGf IS NOT NULL and t.DemandIdGf IS NOT NULL ;";
	        List<NetworkRoute> route;		
	        try
	        {
	        	return route  =  jdbcTemplate.query(sql, new BeanPropertyRowMapper(NetworkRoute.class), networkidBrField, networkidGrField);	       
	        }
	        catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}	
	    };
	    
	 
	 /**
	  * finds the list of unaltered network routes in the brownfield with demand mapping
	  * to be used in Demand view generation in GUI
	  * @param networkidGrField
	  * @param networkidBrField
	  * @return
	  */
	 public List<RouteMapping> findCommonRouteDemMappedInBrField(int networkidGrField, int networkidBrField){       	              
	        String sql = "select tt.NetworkId , tt.DemandId , tt.Traffic, tt.RoutePriority, tt.Path, tt.PathType, tt.WavelengthNo, tt.Osnr, " + 
	        		"	tt.PathType ,tt.LineRate, tt.RegeneratorLoc, tt.Error, d.SrcNodeId, d.DestNodeId , d.ProtectionType from  " + 
	        		"	(select * from  " + 
	        		"	(select bf.NetworkId , bf.DemandId , bf.Traffic, bf.RoutePriority, bf.Path, bf.PathType, bf.WavelengthNo, bf.Osnr,  " + 
	        		"	bf.LineRate, bf.RegeneratorLoc, bf.Error, gf.NetworkId as NetworkIdGf , gf.DemandId as DemandIdGf from   " + 
	        		"	( select * from NetworkRoute where NetworkId = ? ) as bf  " + 
	        		"	left join  " + 
	        		"	( select * from NetworkRoute where NetworkId = ?  ) as gf " + 
	        		"	on  (gf.DemandId = bf.DemandId and gf.RoutePriority = bf.RoutePriority )) as t " + 
	        		"	where t.NetworkIdGf IS NOT NULL and t.DemandIdGf IS NOT NULL) as tt " + 
	        		"	left join (select * from Demand where NetworkId = ? ) as d on d.DemandId = tt.DemandId;";
	        List<RouteMapping> route;		
	        try
	        {
	        	return route  =  jdbcTemplate.query(sql, new BeanPropertyRowMapper(RouteMapping.class), networkidBrField, networkidGrField, networkidBrField);	       
	        }
	        catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}	
	    };
	    
	    /**
		  * finds the list of modified network routes of the brownfield
		  * @param networkidGrField
		  * @param networkidBrField
		  * @return
		  */
		 public List <NetworkRoute> findModifiedRouteInBrField(int networkidGrField, int networkidBrField){       	              
		        String sql = "select * from (select nrbf.NetworkId , nrbf.DemandId , nrbf.RoutePriority, nrbf.Path, nrbf.PathType, nrbf.Traffic, nrbf.WavelengthNo, "
		        		+ "nrbf.LineRate, nrbf.Osnr, nrbf.RegeneratorLoc ,nrbf.RegeneratorLocOsnr, nrbf.Error, nr.NetworkId as NetworkIdGf , nr.DemandId as DemandIdGf " + 
		        		"from ( select * from NetworkRoute where NetworkId = ? ) as nrbf " + 
		        		" left join " + 
		        		"( select * from NetworkRoute where NetworkId = ?  ) as nr on  " + 
		        		"(nr.DemandId = nrbf.DemandId and nr.RoutePriority = nrbf.RoutePriority ) and (nr.Path!=nrbf.Path or " +
		        		"nr.PathType != nrbf.PathType or nr.Traffic != nrbf.Traffic or nr.WavelengthNo != nrbf.WavelengthNo or "+
		        		"nr.LineRate != nrbf.LineRate or nr.Osnr != nrbf.Osnr or nr.RegeneratorLoc != nrbf.RegeneratorLoc or "+
		        		"nr.RegeneratorLocOsnr != nrbf.RegeneratorLocOsnr )) as t " + 
		        		"where t.NetworkIdGf IS NOT NULL and t.DemandIdGf IS NOT NULL ;";
		        List <NetworkRoute> route;		
		        try
		        {
		        	return route  =  jdbcTemplate.query(sql, new BeanPropertyRowMapper(NetworkRoute.class) ,networkidBrField, networkidGrField);	       
		        }
		        catch(EmptyResultDataAccessException e) {
		    		return null;
		    	}	
		    }
		 
		 
		 /**
		  * finds the list of path modified network routes of the brownfield i.e. routes in which different path or different 
		  * wavelength have been assigned to a particular routepriority
		  * @param networkidGrField
		  * @param networkidBrField
		  * @return
		  */
		 public List <NetworkRoute> findPathModifiedRouteInBrField(int networkidGrField, int networkidBrField){       	              
		        String sql = "select * from (select nrbf.NetworkId , nrbf.DemandId , nrbf.RoutePriority, nrbf.Path, nrbf.PathType, nrbf.Traffic, nrbf.WavelengthNo, "
		        		+ "nrbf.LineRate, nrbf.Osnr, nrbf.RegeneratorLoc ,nrbf.RegeneratorLocOsnr, nrbf.Error, nr.NetworkId as NetworkIdGf , nr.DemandId as DemandIdGf " + 
		        		"from ( select * from NetworkRoute where NetworkId = ? ) as nrbf " + 
		        		" left join " + 
		        		"( select * from NetworkRoute where NetworkId = ?  ) as nr on  " + 
		        		"(nr.DemandId = nrbf.DemandId and nr.RoutePriority = nrbf.RoutePriority ) and (nr.Path!=nrbf.Path or " +
		        		" or nr.WavelengthNo != nrbf.WavelengthNo )) as t " + 
		        		"where t.NetworkIdGf IS NOT NULL and t.DemandIdGf IS NOT NULL ;";
		        List <NetworkRoute> route;		
		        try
		        {
		        	return route  =  jdbcTemplate.query(sql, new BeanPropertyRowMapper(NetworkRoute.class) ,networkidBrField, networkidGrField);	       
		        }
		        catch(EmptyResultDataAccessException e) {
		    		return null;
		    	}	
		    }
		 
		 /**
		  * finds the list of Modified network routes in the brownfield with demand mapping
		  * to be used in Demand view generation in GUI
		  * @param networkidGrField
		  * @param networkidBrField
		  * @return
		  */
		 public List<RouteMapping> findModifiedRouteDemMappedInBrField(int networkidGrField, int networkidBrField){       	              
		        String sql = "select tt.NetworkId , tt.DemandId , tt.Traffic, tt.RoutePriority, tt.Path, tt.PathType, tt.WavelengthNo, tt.Osnr, " + 
		        		"	tt.PathType ,tt.LineRate, tt.RegeneratorLoc, tt.Error, d.SrcNodeId, d.DestNodeId , d.ProtectionType from  " + 
		        		"	(select * from  " + 
		        		"	(select bf.NetworkId , bf.DemandId , bf.Traffic, bf.RoutePriority, bf.Path, bf.PathType, bf.WavelengthNo, bf.Osnr,  " + 
		        		"	bf.LineRate, bf.RegeneratorLoc, bf.Error, gf.NetworkId as NetworkIdGf , gf.DemandId as DemandIdGf from   " + 
		        		"	( select * from NetworkRoute where NetworkId = ? ) as bf  " + 
		        		"	left join  " + 
		        		"	( select * from NetworkRoute where NetworkId = ?  ) as gf " + 
		        		"	on  (gf.DemandId = bf.DemandId and gf.RoutePriority = bf.RoutePriority ) and (gf.Path!=bf.Path or " + 
		        		"	gf.PathType != bf.PathType or gf.Traffic != bf.Traffic or gf.WavelengthNo != bf.WavelengthNo or " + 
		        		"	gf.LineRate != bf.LineRate or gf.Osnr != bf.Osnr or gf.RegeneratorLoc != bf.RegeneratorLoc or " + 
		        		"	gf.RegeneratorLocOsnr != bf.RegeneratorLocOsnr )) as t " + 
		        		"	where t.NetworkIdGf IS NOT NULL and t.DemandIdGf IS NOT NULL) as tt " + 
		        		"	left join (select * from Demand where NetworkId = ? ) as d on d.DemandId = tt.DemandId;";
		        List<RouteMapping> route;		
		        try
		        {
		        	return route  =  jdbcTemplate.query(sql, new BeanPropertyRowMapper(RouteMapping.class), networkidBrField, networkidGrField, networkidBrField);	       
		        }
		        catch(EmptyResultDataAccessException e) {
		    		return null;
		    	}	
		    };
		    
	    
	    
    /**
	 * To Update the Traffic from the matching demand in case of Brownfield 
	 */
	    
	 public void updateCommonRouteDemMappedInBrField(NetworkRoute networkRouteToUpdate){       	              
	       
		 
		 logger.info("updateCommonRouteDemMappedInBrField ..");		 
		 logger.info(" Traffic "+ networkRouteToUpdate.getTraffic() + " nid : "+
		 networkRouteToUpdate.getNetworkId() + " did "+ networkRouteToUpdate.getDemandId()+
		 " routepriority : "+ networkRouteToUpdate.getRoutePriority());
		
		 String sql = "update NetworkRoute set Traffic=Traffic+?  where NetworkId= ? and DemandId= ? and RoutePriority = ? ;";
		     		
	        try
	        {
	        	jdbcTemplate.update(sql, networkRouteToUpdate.getTraffic(), networkRouteToUpdate.getNetworkId(), networkRouteToUpdate.getDemandId(),
	        						networkRouteToUpdate.getRoutePriority());	       
	        }
	        catch(Exception e) {
	        	
	        }	
	 };   
	 
	 public void updateCommonRouteDemMappedInBrFieldDirect(NetworkRoute networkRouteToUpdate){       	              
	       
		 
		 logger.info("updateCommonRouteDemMappedInBrField ..");		 
		 logger.info(" Traffic "+ networkRouteToUpdate.getTraffic() + " nid : "+
		 networkRouteToUpdate.getNetworkId() + " did "+ networkRouteToUpdate.getDemandId()+
		 " routepriority : "+ networkRouteToUpdate.getRoutePriority());
		
		 String sql = "update NetworkRoute set Traffic= ?  where NetworkId= ? and DemandId= ? and RoutePriority = ? ;";
		     		
	        try
	        {
	        	jdbcTemplate.update(sql, networkRouteToUpdate.getTraffic(), networkRouteToUpdate.getNetworkId(), networkRouteToUpdate.getDemandId(),
	        						networkRouteToUpdate.getRoutePriority());	       
	        }
	        catch(Exception e) {
	        	
	        }	
	 };
	 
	 

	/**
	 * @desc   To retrieve paths with a particular nodeid the service wise list of OTN LSP
	 * @date   6th Feb,2018
	 * @author hp
	 * @query 
	 * 
	 * select one.NetworkId, one.DemandId, one.LineRate, one.Path, one.WavelengthNo, one.RoutePriority,
	        					group_concat(cir.CircuitId order by cir.CircuitId asc) as CircuitId, cir.TrafficType, cir.ProtectionType
	        					from
	        		            (select nr.NetworkId, nr.DemandId, nr.LineRate, nr.Path, nr.WavelengthNo, nr.RoutePriority,
	        		            cd.CircuitId from (select * from NetworkRoute where NetworkId=6)
	        		            nr left join CircuitDemandMapping cd on nr.DemandId=cd.DemandId where nr.NetworkId=cd.NetworkId 
	        					and  nr.NetworkId=cd.NetworkId and nr.Path LIKE '1,%' and nr.PATH NOT LIKE '%,1,%')
	        		             as one
	        					left join
	        					(select * from   Circuit  where NetworkId=6) cir on one.CircuitId=cir.CircuitId 
	        					and one.NetworkId=cir.NetworkId  group by  one.NetworkId, one.DemandId, one.LineRate, one.Path, one.WavelengthNo, 
	        		            one.RoutePriority,cir.TrafficType, cir.ProtectionType order by DemandId, TrafficType, RoutePriority; 
	 */
	 public List<Map<String, Object>> findRoutesWithServiceBasedNodeEnds_cf(int networkid , int nodeid) {		 	
	   
		 	logger.info("findRoutesWithServiceBasedNodeEnds_cf");
		 	
	        String strWithStartNode = nodeid+",%";
	        String strWithMiddleNode = "%,"+nodeid+",%";
	        
	        String sql = "\n" + 
	        		"select one.NetworkId, one.DemandId, one.LineRate, one.Path, one.WavelengthNo, one.RoutePriority, \n" + 
	        		"			group_concat(cir.CircuitId order by cir.CircuitId asc) as CircuitId, cir.TrafficType, cir.ProtectionType\n" + 
	        		"			from \n" + 
	        		"            (select nr.NetworkId, nr.DemandId, nr.LineRate, nr.Path, nr.WavelengthNo, nr.RoutePriority,\n" + 
	        		"            cd.CircuitId from (select * from NetworkRoute where NetworkId=?)\n" + 
	        		"            nr left join CircuitDemandMapping cd on nr.DemandId=cd.DemandId where nr.NetworkId=cd.NetworkId \n" + 
	        		"			and  nr.NetworkId=cd.NetworkId and nr.Path LIKE ? and nr.PATH NOT LIKE ?)\n" + 
	        		"             as one\n" + 
	        		"			left join\n" + 
	        		"			(select * from   Circuit  where NetworkId=?) cir on one.CircuitId=cir.CircuitId \n" + 
	        		"			and one.NetworkId=cir.NetworkId  group by  one.NetworkId, one.DemandId, one.LineRate, one.Path, one.WavelengthNo, \n" + 
	        		"            one.RoutePriority,cir.TrafficType, cir.ProtectionType order by DemandId, TrafficType, RoutePriority;";
	        
	        logger.info("findRoutesWithServiceBasedNodeEnds_cf"+sql);
	        
	        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, networkid,strWithStartNode, strWithMiddleNode, networkid);
	        return list;
	    }

	 
	 public void copyNetworkRouteDataInBrField(int networkidGrField, int networkidBrField ) throws SQLException {			

		 String sql = "insert into NetworkRoute ( NetworkId, DemandId, RoutePriority, Path,PathLinkId, PathType, Traffic, WavelengthNo, LineRate,  Osnr, RegeneratorLoc, RegeneratorLocOsnr, Error, 3RLocationHeadToTail, 3RLocationTailToHead) select ?, DemandId, RoutePriority, Path, PathLinkId, PathType, Traffic, WavelengthNo, LineRate,  Osnr, RegeneratorLoc, RegeneratorLocOsnr, Error, 3RLocationHeadToTail, 3RLocationTailToHead from NetworkRoute where NetworkId = ? ";
		 logger.info("copyNetworkRouteDataInBrField: "+sql); 	     
		 jdbcTemplate.update( sql,networkidBrField,networkidGrField);
	 }

	 /**
	  * Find all distinct working lambda for given routepriority
	  */	
	 public List<Integer> findDistinctLambda(int networkId, int routePriority){
		String sql = "select distinct WavelengthNo from NetworkRoute where NetworkId=? and  RoutePriority=? ";		
		List<Integer> routes  = jdbcTemplate.queryForList(sql, Integer.class, networkId, routePriority);		
		return routes;
	}
	 
	 public void updateRouteMappingView() {
		 String sql="CREATE OR REPLACE VIEW RouteMapping AS "+
				    "SELECT n.NetworkId, n.DemandId , d.SrcNodeId, d.DestNodeId, n.Traffic, "+
				    "d.ProtectionType,d.ChannelProtection, n.RoutePriority, n.Path,n.PathLinkId, "+
				    "n.PathType, n.WavelengthNo , n.Osnr , n.LineRate , n.RegeneratorLoc, "+
				    "n.Error FROM NetworkRoute n "+
				    "LEFT JOIN Demand d "+
				    "ON n.NetworkId=d.NetworkId and n.DemandId = d.DemandId";
		 try {
			jdbcTemplate.update(sql);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
	 }



	 /**
	 * @desc   To retrieve paths with a purely non 10g Agg based OTN LSP
	 * @date   19th Dec,2018
	 * @query 
	 *            select one.NetworkId, one.DemandId, one.LineRate, one.Path, one.WavelengthNo, one.RoutePriority,
	        					group_concat(cir.CircuitId order by cir.CircuitId asc) as CircuitId, cir.TrafficType, cir.ProtectionType
	        					from
	        		            (select nr.NetworkId, nr.DemandId, nr.LineRate, nr.Path, nr.WavelengthNo, nr.RoutePriority,
	        		            cd.CircuitId from (select * from NetworkRoute where NetworkId=6)
	        		            nr left join CircuitDemandMapping cd on nr.DemandId=cd.DemandId where nr.NetworkId=cd.NetworkId 
	        					and  nr.NetworkId=cd.NetworkId and nr.Path LIKE '1,%' and nr.PATH NOT LIKE '%,1,%')
	        		             as one
	        					left join
	        					(SELECT  * FROM Circuit where CircuitId NOT IN
                                  (SELECT   Circuit10gAgg.CircuitId  FROM Circuit10gAgg	WHERE Circuit10gAgg.NetworkId=6 ) and Circuit.NetworkId=6) 
                                  cir on one.CircuitId=cir.CircuitId 
	        					and one.NetworkId=cir.NetworkId where cir.CircuitId is not null group by  one.NetworkId, one.DemandId, one.LineRate, one.Path, one.WavelengthNo, 
	        		            one.RoutePriority,cir.TrafficType, cir.ProtectionType 
                                order by DemandId, TrafficType, RoutePriority; 
   
	 * @author hp
	 */
	public List<Map<String, Object>> findRoutesWithServiceBasedNodeEnds_PureNon10gAgg(int networkid , int nodeid) {		 	
	   
		logger.info("findRoutesWithServiceBasedNodeEnds_PureNon10gAgg");
		
	   String strWithStartNode = nodeid+",%";
	   String strWithMiddleNode = "%,"+nodeid+",%";
	   
	   String sql = "\n" + 
			   "select one.NetworkId, one.DemandId, one.LineRate, one.Path, one.WavelengthNo, one.RoutePriority, \n" + 
			   "			group_concat(cir.CircuitId order by cir.CircuitId asc) as CircuitId, cir.TrafficType, cir.ProtectionType\n" + 
			   "			from \n" + 
			   "            (select nr.NetworkId, nr.DemandId, nr.LineRate, nr.Path, nr.WavelengthNo, nr.RoutePriority,\n" + 
			   "            cd.CircuitId from (select * from NetworkRoute where NetworkId=?)\n" + 
			   "            nr left join CircuitDemandMapping cd on nr.DemandId=cd.DemandId where nr.NetworkId=cd.NetworkId \n" + 
			   "			and  nr.NetworkId=cd.NetworkId and nr.Path LIKE ? and nr.PATH NOT LIKE ?)\n" + 
			   "             as one\n" + 
			   "			left join\n" + 
			   "			(SELECT  * FROM Circuit where CircuitId NOT IN\n" + 
			   "                                  (SELECT   Circuit10gAgg.CircuitId  FROM Circuit10gAgg	WHERE Circuit10gAgg.NetworkId=? ) and Circuit.NetworkId=?) cir on one.CircuitId=cir.CircuitId \n" + 
			   "			and one.NetworkId=cir.NetworkId  where cir.CircuitId is not null group by  one.NetworkId, one.DemandId, one.LineRate, one.Path, one.WavelengthNo, \n" + 
			   "            one.RoutePriority,cir.TrafficType, cir.ProtectionType order by DemandId, TrafficType, RoutePriority;";
	   
	   logger.info("findRoutesWithServiceBasedNodeEnds_PureNon10gAgg"+sql);
	   
	   List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, networkid,strWithStartNode, strWithMiddleNode,networkid, networkid);
	   return list;
   }

/**
 * 	
 * @param networkid
 * @param nodeid
 * @return
 * @query select one.NetworkId, one.DemandId, one.LineRate, one.Path, one.WavelengthNo, one.RoutePriority, 
								group_concat(cir.CircuitId order by cir.CircuitId asc) as CircuitId, cir.TrafficType, cir.ProtectionType
								from 
					            (select nr.NetworkId, nr.DemandId, nr.LineRate, nr.Path, nr.WavelengthNo, nr.RoutePriority,
					            cd.CircuitId from (select * from NetworkRoute where NetworkId=6)
					            nr left join CircuitDemandMapping cd on nr.DemandId=cd.DemandId where nr.NetworkId=cd.NetworkId 
								and  nr.NetworkId=cd.NetworkId and nr.Path LIKE '1,%' and nr.PATH NOT LIKE '%,1,%')
					             as one
								left join
								(select * from   Circuit  where CircuitId IN 
					                     ( SELECT   Circuit.CircuitId  FROM Circuit 
					                     LEFT JOIN Circuit10gAgg ON Circuit.CircuitId = Circuit10gAgg.CircuitId 
					                        WHERE Circuit10gAgg.Circuit10gAggId  IS NOT NULL and Circuit.NetworkId= 6 and Circuit10gAgg.NetworkId=6)) cir on one.CircuitId=cir.CircuitId 
								and one.NetworkId=cir.NetworkId where cir.CircuitId is not null group by  one.NetworkId, one.DemandId, one.LineRate, one.Path, one.WavelengthNo, 
					            one.RoutePriority,cir.TrafficType, cir.ProtectionType order by DemandId, TrafficType, RoutePriority;
 */
   public List<Map<String, Object>> findRoutesWithServiceBasedNodeEnds_Pure10gAgg(int networkid , int nodeid) {		 	
	   
			logger.info("findRoutesWithServiceBasedNodeEnds_Pure10gAgg");
				
			String strWithStartNode = nodeid+",%";
			String strWithMiddleNode = "%,"+nodeid+",%";
			
			String sql = "\n" + 
					"select one.NetworkId, one.DemandId, one.LineRate, one.Path, one.WavelengthNo, one.RoutePriority, \n" + 
					"								cir.CircuitId  as CircuitId, cir.TrafficType, cir.ProtectionType\n" + 
					"								from \n" + 
					"					            (select nr.NetworkId, nr.DemandId, nr.LineRate, nr.Path, nr.WavelengthNo, nr.RoutePriority,\n" + 
					"					            cd.CircuitId from (select * from NetworkRoute where NetworkId=?)\n" + 
					"					            nr left join CircuitDemandMapping cd on nr.DemandId=cd.DemandId where nr.NetworkId=cd.NetworkId \n" + 
					"								and  nr.NetworkId=cd.NetworkId and nr.Path LIKE ? and nr.PATH NOT LIKE ?)\n" + 
					"					             as one\n" + 
					"								left join\n" + 
					"								(select * from   Circuit  where CircuitId IN \n" + 
					"					                     ( SELECT   Circuit.CircuitId  FROM Circuit \n" + 
					"					                     LEFT JOIN Circuit10gAgg ON Circuit.CircuitId = Circuit10gAgg.CircuitId \n" + 
					"					                        WHERE Circuit10gAgg.Circuit10gAggId  IS NOT NULL and Circuit.NetworkId= ? and Circuit10gAgg.NetworkId=? )) cir on one.CircuitId=cir.CircuitId \n" + 
					"								and one.NetworkId=cir.NetworkId where cir.CircuitId is not null;";
			
			logger.info("findRoutesWithServiceBasedNodeEnds_Pure10gAgg"+sql);
			
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, networkid,strWithStartNode, strWithMiddleNode, networkid, networkid);
			return list;
}

	    
	 
	 public void updateLinkSet(int networkId,int rp,String path,int wav,int demandid,String linkSet) {
		 String sql="Update NetworkRoute set PathLinkId=? where NetworkId=? and DemandId=? and RoutePriority=? and Path=? and WavelengthNo=?";
		 try {
			jdbcTemplate.update(sql,linkSet,networkId,demandid,rp,path,wav);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
	 }
	 
	 public void updateTrafficForDemand(int NetworkId,int DemandId, float Traffic)
	 {
       String sql = "Update NetworkRoute set Traffic = ? where NetworkId = ? and DemandId = ?";
       
		 try {
			 
			 jdbcTemplate.update(sql,Traffic,NetworkId,DemandId);
		 }
		 catch(Exception e ) {
			 
			 System.out.println(e);							 
		 }
		 
		 
		 
	 }


	 	 
}