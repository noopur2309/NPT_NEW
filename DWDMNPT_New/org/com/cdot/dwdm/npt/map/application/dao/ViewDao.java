package application.dao;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import application.constants.ResourcePlanConstants;
import application.model.DemandMapping;
import application.model.Link;
import application.model.RouteMapping;

@Component
public class ViewDao{
	@Autowired
    private JdbcTemplate jdbcTemplate;
	public static Logger logger = Logger.getLogger(ResourcePlanConstants.DatabasePrintStr);
	
	
	public List<Map<String, Object>> findRouteMapping1() {
	        logger.info("Find findRouteMapping");
	        String sql = "SELECT * FROM RouteMapping";
	        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
	        return list;
	    }
	 
	 @SuppressWarnings({ "unchecked", "rawtypes" })
	 public List<RouteMapping> findRouteMapping(int networkId){
				String sql = "SELECT * FROM RouteMapping where NetworkId=? order by DemandId ASC, RoutePriority ASC";
				List<RouteMapping> mapping  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(RouteMapping.class),networkId);
				return mapping;
			}	 
	 
	 @SuppressWarnings({ "unchecked", "rawtypes" })
	 public List<RouteMapping> findChannelProtectionRoutes(int networkId,int nodeId){
				String sql = "SELECT * FROM RouteMapping where NetworkId=? and (SrcNodeId=? or DestNodeId=?) and RoutePriority=1 and ChannelProtection='Yes'";
				List<RouteMapping> mapping  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(RouteMapping.class),networkId,nodeId,nodeId);
				return mapping;
	}
	 
	 @SuppressWarnings({ "unchecked", "rawtypes" })
	 public List<RouteMapping> findClientProtectionRoutes(int networkId,int nodeId){
				String sql = "SELECT * FROM RouteMapping where NetworkId=? and (SrcNodeId=? or DestNodeId=?) and RoutePriority=1 and ChannelProtection='No' and ProtectionType like '1+%'";
				List<RouteMapping> mapping  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(RouteMapping.class),networkId,nodeId,nodeId);
				return mapping;
	}
	 
	 @SuppressWarnings({ "unchecked", "rawtypes" })
	 public List<RouteMapping> findRouteMappingCommonBrField(int networkId,int networkIdBf,int nodeId){
				String sql = "select * from (select bf.NetworkId , bf.DemandId , bf.SrcNodeId , bf.DestNodeId , bf.Traffic, bf.ProtectionType, bf.ChannelProtection, \r\n" + 
						"				bf.RoutePriority,bf.Path,bf.PathType,bf.WavelengthNo,bf.Osnr,bf.LineRate,bf.RegeneratorLoc, \r\n" + 
						"				bf.Error from \r\n" + 
						"				(select * from routemapping where NetworkId = ? and (SrcNodeId=? or DestNodeId=?) and RoutePriority=1 and ChannelProtection='Yes') as bf \r\n" + 
						"				left join ( select * from routemapping where NetworkId = ? and (SrcNodeId=? or DestNodeId=?)) as gf on \r\n" + 
						"				(gf.SrcNodeId = bf.SrcNodeId) and (gf.DestNodeId = bf.DestNodeId) and (gf.DemandId = bf.DemandId) and (gf.RoutePriority = bf.RoutePriority)\r\n" + 
						"				where bf.Traffic = gf.Traffic and \r\n" + 
						"				bf.ProtectionType = gf.ProtectionType and \r\n" + 
						"				bf.ChannelProtection = gf.ChannelProtection and \r\n" + 
						"				bf.PathType = gf.PathType and \r\n" + 
						"				bf.Path = gf.Path and \r\n" + 
						"				bf.WavelengthNo = gf.WavelengthNo and \r\n" + 
						"				bf.Osnr = gf.Osnr and \r\n" + 
						"				bf.LineRate = gf.LineRate and \r\n" + 
						"				bf.RegeneratorLoc = gf.RegeneratorLoc and \r\n" + 
						"				bf.Error = gf.Error\r\n" + 
						"				) As t ;";
				List<RouteMapping> mapping  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(RouteMapping.class),networkIdBf,nodeId,nodeId,networkId,nodeId,nodeId);
				return mapping;
	}
	 
	 @SuppressWarnings({ "unchecked", "rawtypes" })
	 public List<RouteMapping> findRouteMappingAddedBrField(int networkId,int networkIdBf,int nodeId){
				String sql = "select * from (select bf.NetworkId , bf.DemandId , bf.SrcNodeId , bf.DestNodeId , bf.Traffic, bf.ProtectionType, bf.ChannelProtection, \r\n" + 
						"				bf.RoutePriority,bf.Path,bf.PathType,bf.WavelengthNo,bf.Osnr,bf.LineRate,bf.RegeneratorLoc, \r\n" + 
						"				bf.Error,gf.NetworkId as NetworkIdGf from (select * from routemapping where NetworkId = ? and (SrcNodeId=? or DestNodeId=?) and RoutePriority=1 and ChannelProtection='Yes') as bf \r\n" + 
						"				left join ( select * from routemapping where NetworkId = ? and (SrcNodeId=? or DestNodeId=?)) as gf on \r\n" + 
						"                (gf.SrcNodeId = bf.SrcNodeId) and (gf.DestNodeId = bf.DestNodeId) and (gf.DemandId = bf.DemandId) and (gf.RoutePriority = bf.RoutePriority))\r\n" + 
						"				as t where NetworkIdGf is null;";
				List<RouteMapping> mapping  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(RouteMapping.class),networkIdBf,nodeId,nodeId,networkId,nodeId,nodeId);
				return mapping;
	}
	 
	 @SuppressWarnings({ "unchecked", "rawtypes" })
	 public List<RouteMapping> findRouteMappingDeletedBrField(int networkId,int networkIdBf,int nodeId){
				String sql = " select * from (select gf.NetworkId , gf.DemandId , gf.SrcNodeId , gf.DestNodeId , gf.Traffic, gf.ProtectionType, gf.ChannelProtection, \r\n" + 
						"				gf.RoutePriority,gf.Path,gf.PathType,gf.WavelengthNo,gf.Osnr,gf.LineRate,gf.RegeneratorLoc, \r\n" + 
						"				gf.Error,bf.NetworkId as NetworkIdBf,bf.DemandId as DemandIdBf from (select * from routemapping where NetworkId = ? and (SrcNodeId=? or DestNodeId=?) and RoutePriority=1 and ChannelProtection='Yes') as gf \r\n" + 
						"				left join ( select * from routemapping where NetworkId = ? and (SrcNodeId=? or DestNodeId=?)) as bf on \r\n" + 
						"                (gf.SrcNodeId = bf.SrcNodeId) and (gf.DestNodeId = bf.DestNodeId) and (gf.DemandId = bf.DemandId) and (gf.RoutePriority = bf.RoutePriority))\r\n" + 
						"				as t  where NetworkIdBf is null and DemandIdBf is null;";
				List<RouteMapping> mapping  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(RouteMapping.class),networkId,nodeId,nodeId,networkIdBf,nodeId,nodeId);
				return mapping;
	}
	 
	 public Link findLink(int srcnode, int destnode,int networkId,int linkId) {
	        String sql = "SELECT Length FROM Link where ((SrcNode = ? and DestNode = ?) or (SrcNode =? and DestNode =?)) and NetworkId=? and LinkId=? ";
	        Link len = (Link) jdbcTemplate.queryForObject(sql,  new BeanPropertyRowMapper(Link.class), srcnode, destnode, destnode, srcnode,networkId,linkId);
	        return len;
	    }
	 
	 public int findLinkLen(int srcnode, int destnode,int networkId) {
	        String sql = "SELECT Length FROM Link where ((SrcNode = ? and DestNode = ?) or (SrcNode =? and DestNode =?)) and NetworkId=? ";
	        int len = jdbcTemplate.queryForObject(sql, Integer.class, srcnode, destnode, destnode, srcnode,networkId);
	        return len;
	    }
	 public int findLinkLenWithNetworkId(int srcnode, int destnode, int networkId) {
	        String sql = "SELECT Length FROM Link where (SrcNode = ? and DestNode = ?) or (SrcNode =? and DestNode =?) "
	        		+ " where NetworkId=?";
	        int len = jdbcTemplate.queryForObject(sql, Integer.class, srcnode, destnode, destnode, srcnode, networkId);
	        return len;
	    }
	 
	 public int findLinkCost(int srcnode, int destnode,int networkId) {
	        String sql = "SELECT MetricCost FROM Link where ((SrcNode = ? and DestNode = ?) or (SrcNode =? and DestNode =?)) and NetworkId=? ";
	        int len = jdbcTemplate.queryForObject(sql, Integer.class, srcnode, destnode, destnode, srcnode,networkId);
	        return len;
	    }
	 
	 public int findLinkCd(int srcnode, int destnode,int networkId) {
	        String sql = "SELECT Cd FROM Link where ((SrcNode = ? and DestNode = ?) or (SrcNode =? and DestNode =?)) and NetworkId=? ";
	        int cd = jdbcTemplate.queryForObject(sql, Integer.class, srcnode, destnode, destnode, srcnode,networkId);
	        return cd;
	    }
	 
	 public int findLinkPmd(int srcnode, int destnode,int networkId) {
	        String sql = "SELECT Pmd FROM Link where ((SrcNode = ? and DestNode = ?) or (SrcNode =? and DestNode =?)) and NetworkId=? ";
	        int pmd = jdbcTemplate.queryForObject(sql, Integer.class, srcnode, destnode, destnode, srcnode,networkId);
	        return pmd;
	    }
	 
	 @SuppressWarnings({ "unchecked", "rawtypes" })
		public List<DemandMapping> findDemandMapping(int networkId){
				String sql = "SELECT * FROM DemandMapping where NetworkId=?";
				List<DemandMapping> mapping  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(DemandMapping.class),networkId);
				return mapping;
			}	
	 
	 
	 public void createCardInfoTable(String NodeKey)
	 {		
		 String tablename= "CardInfo_"+NodeKey;		
		 String sql = " CREATE TABLE IF NOT EXISTS `DwdmNpt`.`"+tablename+"` ( `NodeKey` VARCHAR(45) NOT NULL, `Rack` INT NOT NULL, `Sbrack` INT NOT NULL, `Card` INT NOT NULL, `CardType` INT NULL,  `Direction` INT NULL, `Wavelength` INT NULL, PRIMARY KEY (`NodeKey`, `Rack`, `Sbrack`, `Card`));";
		 jdbcTemplate.execute(sql);
	 }
	 
	 }
