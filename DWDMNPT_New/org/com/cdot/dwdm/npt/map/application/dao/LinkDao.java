package application.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import application.model.Circuit;
import application.model.Link;
import application.model.Node;
import application.MainMap;
import application.constants.ResourcePlanConstants;
import application.model.CardInfo;

@Component
public class LinkDao{
	@Autowired
    private JdbcTemplate jdbcTemplate;
	public static Logger logger = Logger.getLogger(ResourcePlanConstants.DatabasePrintStr);
	
	 public void queryLinks() {
	        logger.info("Query Link");
	        String sql = "SELECT * FROM Link";
	        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
	        for (Map<String, Object> row : list) {	        	
	            logger.info(" NetworkId: "+row.get("NetworkId")+" LinkId: "+row.get("LinkId")+" SrcNodeId: "+ row.get("SrcNode")+" DestNodeId: "+row.get("DestNode"));
	        }
	    }
	 
	 public List<Map<String, Object>> findAllLinks(int networkId) {
	        logger.info("Find All Links");
	        String sql = "SELECT * FROM Link where NetworkId = ?";
	        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, networkId);
	        return list;
	    }
	 
	 public List<Map<String, Object>> findAllLinksWithNetworkId(int networkId) {
	        logger.info("Find All Links");
	        String sql = "SELECT * FROM Link where NetworkId=?";
	        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,networkId);
	        return list;
	    }
	 
	 @SuppressWarnings({ "unchecked", "rawtypes" })
		public List<Link> findAll(int networkId){
				String sql = "SELECT * FROM Link where NetworkId = ? ;";
				List<Link> links  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Link.class),networkId);
				return links;
			}
	 
	 @SuppressWarnings({ "unchecked", "rawtypes" })
		public List<Link> findAllRamanLinks(int networkId,int nodeid){
				String sql = "SELECT * FROM Link where NetworkId = ? and (SrcNode=? or DestNode=?) and LinkType like 'RAMAN%';";
				List<Link> links  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Link.class),networkId,nodeid,nodeid);
				return links;
			}
	 
	 @SuppressWarnings({ "unchecked", "rawtypes" })
		public List<Link> findAllLinks(int networkId,int nodeid){
				String sql = "SELECT * FROM Link where NetworkId = ? and (SrcNode=? or DestNode=?);";
				List<Link> links  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Link.class),networkId,nodeid,nodeid);
				return links;
			}
	 
	 @SuppressWarnings({ "unchecked", "rawtypes" })
		public List<Link> findAllwithNetworkId(int networkId){
				String sql = "SELECT * FROM Link where NetworkId=?";
				List<Link> links  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Link.class),networkId);
				return links;
			}
	 
	 public void insertLink(final Link link) throws SQLException 
	 {
	     logger.info("Insert link ");
	     String sql = "INSERT into Link(NetworkId, LinkId, SrcNode, DestNode, Colour, MetricCost, Length, SpanLoss, Capacity, "
	     		+ "FibreType, SrlgId, NSplices, LossPerSplice, NConnector, LossPerConnector, CalculatedSpanLoss, SpanLossCoff, "
	     		+ "CdCoff, Cd, PmdCoff, Pmd,SrcNodeDirection, DestNodeDirection,OMSProtection,LinkType) VALUES "
	     		+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	     jdbcTemplate.update(
	             sql,
	             new Object[] { link.getNetworkId(), link.getLinkId(), link.getSrcNode(), link.getDestNode(), link.getColour(), link.getMetricCost(), link.getLength(), link.getSpanLoss(), link.getCapacity(), link.getFibreType(), link.getSrlgId(), link.getNSplices(), link.getLossPerSplice(), link.getNConnector(), link.getLossPerConnector(), link.getCalculatedSpanLoss(), link.getSpanLossCoff(), link.getCdCoff(), link.getCd(), link.getPmdCoff(), link.getPmd(),link.getSrcNodeDirection(), link.getDestNodeDirection(),link.getOMSProtection(), link.getLinkType() });
	 }
	 
	 public int count(int networkId)
	 {		
		 logger.info("Count Link for NetworkId:"+networkId);
		  String sql = "select count(*) from Link where NetworkId = ? "; 
		  return jdbcTemplate.queryForObject(sql, int.class, networkId);		  		        
	 }
	 
	 /*Function return SrcIp , DestIp, SrcNodeDirection of the links originating from a particular Node
	  * 
	  * Select  i.NetworkId, i.SrcIp , i.DestIp,  l.NetworkId, l.SrcNodeDirection , l.linkid , i.SubnetMask from IpSchemeLink i inner join Link l
	  *  on i.LinkId=l.LinkId and l.SrcNode = ?  where i.NetworkId=? and l.NetworkId=?;
	  * */
	 public List<Map<String, Object>> findSrcIpOfLink(int SrcNodeId, int networkId) {
	        logger.info("findSrcIpOfLink");
	        String sql = "Select  i.NetworkId, i.SrcIp , i.DestIp, l.NetworkId, l.SrcNodeDirection , l.linkid , i.SubnetMask from IpSchemeLink i inner join Link l on i.LinkId=l.LinkId and i.NetworkId = l.NetworkId and l.SrcNode = ? where i.NetworkId = ? ;";
	        
	        
	        
	        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,SrcNodeId, networkId);
	        return list;
	    }
	 
	 /*Function return SrcIp , DestIp, DestNodeDirection of the links terminating from a particular Node
	  * */
	 public List<Map<String, Object>> findDestIpOfLink(int DestNodeId, int networkId) {
	        logger.info("findDestIpOfLink");
	        String sql = "Select i.NetworkId, i.SrcIp , i.DestIp,  l.NetworkId, l.DestNodeDirection , l.linkid , i.SubnetMask from IpSchemeLink i inner join Link l on i.LinkId=l.LinkId and i.NetworkId = l.NetworkId and l.DestNode = ? where  i.NetworkId = ? ;";
	        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,DestNodeId, networkId);
	        return list;
	    }
	 
	 public String findLinkDirection(int networkid,int srcnode, int destnode)
	 {		
		 logger.info("Count Link");
		 logger.info("For Link "+srcnode+" , "+destnode);
		  String sql = "SELECT SrcNodeDirection as Direction from Link where SrcNode = ? and DestNode = ? and NetworkId = ? UNION"
		  		+ " SELECT DestNodeDirection as Direction from Link where DestNode = ? and SrcNode = ? and NetworkId = ?"; 
		  return jdbcTemplate.queryForObject(sql, String.class, srcnode, destnode, networkid, srcnode, destnode, networkid);		  		        
	 }
	 
	 public int findLinkInDirection(int networkid,int node, String dir)
	 {		
		 logger.info("findLinkInDirection");
		 logger.info("For Link "+node);
		  String sql = "SELECT LinkId from Link where NetworkId=? and ( ( SrcNode = ? and SrcNodeDirection = ? ) or ( DestNode = ? and DestNodeDirection = ? ) )"; 
		  try {
			  return jdbcTemplate.queryForObject(sql, Integer.class,networkid, node, dir,node, dir);	
		} catch (Exception e) {
			// TODO: handle exception
			return 0;	
		}		  	  		        
	 }
	 

	 public List<Map<String, Object>> perNodeLinkData_cf(int networkid, int NodeId) {
	        logger.info("perNodeLinkData_cf");
	        String sql = "SELECT SrcNodeDirection as Direction , SrlgId, MetricCost, Colour , LinkId from Link where NetworkId = ?  and SrcNode = ?"
	        		+ " UNION ALL SELECT DestNodeDirection as Direction , SrlgId, MetricCost, Colour ,  LinkId from Link where NetworkId = ?  and DestNode = ?;";
	        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, networkid, NodeId, networkid, NodeId);
	        return list;
	    }

	 public Link findLink(int networkid, int srcnode, int destnode)
	 {		
		 logger.info("Count Link");
		  String sql = "SELECT LinkId from Link where NetworkId = ? and SrcNode = ? and DestNode = ? UNION"
		  		+ " SELECT LinkId from Link where NetworkId =?  and DestNode = ? and SrcNode = ? "; 
		  return (Link) jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper(Link.class), networkid, srcnode, destnode, networkid, srcnode, destnode);		  		        
	 }
	 
	 public Link findLink(int networkid, int linkId)
	 {		
		 logger.info("findLink ");
		  String sql = "SELECT * from Link where NetworkId = ? and LinkId = ?"; 
		  return (Link) jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper(Link.class), networkid, linkId);		  		        
	 }
	 
	 
	 /**
	  * Query gives the data per wavelength for a particular link of a particular node
	  * @param networkid
	  * @param NodeId
	  * @param LinkId
	  * @return
	  */
	 public List<Map<String, Object>> perLinkWavelengths_cf(int networkid, int NodeId, int LinkId) {
	        logger.info("perNodeLinkData_cf");
	        String sql = "SELECT l.LinkId, l.SrcNodeDirection as LocalNodeDirection  , l.DestNodeDirection as RemoteNodeDirection ,"
	        		+ " lwf.DemandId, lwf.LineRate, lwf.Traffic, lwf.Wavelength from Link l "
	        		+ "	left join LinkWavelengthInfo lwf on l.LinkId=lwf.LinkId and l.NetworkId = lwf.NetworkId "
	        		+ "where l.NetworkId = ?  and l.SrcNode = ? and l.LinkId= ? "
	        		+ "UNION "
	        		+ "SELECT l.LinkId, l.DestNodeDirection as LocalNodeDirection  , l.SrcNodeDirection as RemoteNodeDirection , "
	        		+ "lwf.DemandId, lwf.LineRate, lwf.Traffic, lwf.Wavelength from Link l "
	        		+ "left join LinkWavelengthInfo lwf on l.LinkId=lwf.LinkId and l.NetworkId = lwf.NetworkId "
	        		+ "where l.NetworkId = ?  and l.DestNode = ? and l.LinkId= ? order by DemandId ;"; 
	        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, networkid, NodeId, LinkId, networkid, NodeId, LinkId);
	        return list;
	    }
	 
	 
	 public List<Link> findOMSProtectedLinksOnANode(int networkid, int nodeid){
			String sql = "SELECT * FROM Link where NetworkId = ? and ( SrcNode = ? or DestNode = ? ) and OMSProtection = 'Yes' ";
			List<Link> links  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Link.class), networkid, nodeid, nodeid);
			return links;
		}
	 
	 public List<Link> findAddedLinksOnNodeInBrField(int networkidGrField,int networkidBrField, int nodeid){
			String sql = "select * from (select bf.NetworkId , bf.LinkId , bf.SrcNode, bf.DestNode, bf.Colour, bf.MetricCost, bf.Length, bf.SpanLoss, bf.Capacity" + 
					" , bf.FibreType, bf.SrlgId, bf.NSplices, bf.LossPerSplice, bf.NConnector, bf.LossPerConnector, bf.CalculatedSpanLoss , bf.SpanLossCoff," + 
					" bf.CdCoff, bf.Cd, bf.PmdCoff, bf.Pmd, bf.SrcNodeDirection, bf.DestNodeDirection, bf.OMSProtection,bf.Linktype, gf.NetworkId as NetworkIdGf " + 
					" , gf.LinkId as LinkIdGf from " + 
					" ( select * from Link where NetworkId = ? and (SrcNode =? or DestNode=?) ) as bf " + 
					" left join ( select * from Link where NetworkId = ? and (SrcNode =? or DestNode=?) ) as gf " + 
					" on gf.LinkId = bf.LinkId ) as t " + 
					" where t.NetworkIdGf is NULL and t.LinkIdGf is NULL;";
			logger.info(sql);
			logger.info(" Network: "+networkidBrField+" For Node: "+nodeid);
			List<Link> links  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Link.class), networkidBrField,nodeid, nodeid, networkidGrField, nodeid, nodeid);
			return links;
		}
	 
	 public List<Link> findAddedLinksInBrField(int networkidGrField,int networkidBrField){
			String sql = "select * from (select bf.NetworkId , bf.LinkId , bf.SrcNode, bf.DestNode, bf.Colour, bf.MetricCost, bf.Length, bf.SpanLoss, bf.Capacity" + 
					" , bf.FibreType, bf.SrlgId, bf.NSplices, bf.LossPerSplice, bf.NConnector, bf.LossPerConnector, bf.CalculatedSpanLoss , bf.SpanLossCoff," + 
					" bf.CdCoff, bf.Cd, bf.PmdCoff, bf.Pmd, bf.SrcNodeDirection, bf.DestNodeDirection, bf.OMSProtection,bf.Linktype, gf.NetworkId as NetworkIdGf " + 
					" , gf.LinkId as LinkIdGf from " + 
					" ( select * from Link where NetworkId = ? ) as bf " + 
					" left join ( select * from Link where NetworkId = ? ) as gf " + 
					" on gf.LinkId = bf.LinkId ) as t " + 
					" where t.NetworkIdGf is NULL and t.LinkIdGf is NULL;";
			logger.info(sql);
			logger.info(" Network: "+networkidBrField);
			List<Link> links  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Link.class), networkidBrField, networkidGrField);
			return links;
		}
	 
	 public List<Link> findCommonLinksInBrField(int networkidGrField,int networkidBrField){
			String sql = "select * from (select bf.NetworkId , bf.LinkId , bf.SrcNode, bf.DestNode, bf.Colour, bf.MetricCost, bf.Length, bf.SpanLoss, bf.Capacity" + 
					" , bf.FibreType, bf.SrlgId, bf.NSplices, bf.LossPerSplice, bf.NConnector, bf.LossPerConnector, bf.CalculatedSpanLoss , bf.SpanLossCoff," + 
					" bf.CdCoff, bf.Cd, bf.PmdCoff, bf.Pmd, bf.SrcNodeDirection, bf.DestNodeDirection, bf.OMSProtection,bf.Linktype, gf.NetworkId as NetworkIdGf " + 
					" , gf.LinkId as LinkIdGf from " + 
					" ( select * from Link where NetworkId = ? ) as bf " + 
					" left join ( select * from Link where NetworkId = ? ) as gf " + 
					" on gf.LinkId = bf.LinkId ) as t " + 
					" where t.NetworkIdGf is NOT NULL and t.LinkIdGf is NOT NULL;";
			logger.info(sql);
			logger.info(" Network: "+networkidBrField);
			List<Link> links  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Link.class), networkidBrField, networkidGrField);
			return links;
		}
	 
	 
	 public List<Link> findModifiedLinksInBrField(int networkidGrField,int networkidBrField){
			String sql = "select * from (select bf.NetworkId , bf.LinkId , bf.SrcNode, bf.DestNode, bf.Colour, bf.MetricCost, bf.Length, bf.SpanLoss, bf.Capacity" + 
					" , bf.FibreType, bf.SrlgId, bf.NSplices, bf.LossPerSplice, bf.NConnector, bf.LossPerConnector, bf.CalculatedSpanLoss , bf.SpanLossCoff," + 
					" bf.CdCoff, bf.Cd, bf.PmdCoff, bf.Pmd, bf.SrcNodeDirection, bf.DestNodeDirection, bf.OMSProtection,bf.Linktype, gf.NetworkId as NetworkIdGf " + 
					" , gf.LinkId as LinkIdGf from " + 
					" ( select * from Link where NetworkId = ? ) as bf " + 
					" left join ( select * from Link where NetworkId = ? ) as gf " + 
					" on gf.LinkId = bf.LinkId ) as t " + 
					" where t.NetworkIdGf is NOT NULL and t.LinkIdGf is NOT NULL;";
			logger.info(sql);
			logger.info(" Network: "+networkidBrField);
			List<Link> links  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Link.class), networkidBrField, networkidGrField);
			return links;
		}
	 
	 public List<Link> findModifiedLinksInBrField(int networkidGrField,int networkidBrField,int nodeid){
			String sql = "select * from (select bf.NetworkId , bf.LinkId , bf.SrcNode, bf.DestNode, bf.Colour, bf.MetricCost, bf.Length, bf.SpanLoss, bf.Capacity" + 
					" , bf.FibreType, bf.SrlgId, bf.NSplices, bf.LossPerSplice, bf.NConnector, bf.LossPerConnector, bf.CalculatedSpanLoss , bf.SpanLossCoff," + 
					" bf.CdCoff, bf.Cd, bf.PmdCoff, bf.Pmd, bf.SrcNodeDirection, bf.DestNodeDirection, bf.OMSProtection,bf.Linktype, gf.NetworkId as NetworkIdGf " + 
					" , gf.LinkId as LinkIdGf from " + 
					" ( select * from Link where NetworkId = ? and (SrcNode =? or DestNode=?) ) as bf " + 
					" left join ( select * from Link where NetworkId = ? and (SrcNode =? or DestNode=?)) as gf " + 
					" on gf.LinkId = bf.LinkId ) as t " + 
					" where t.NetworkIdGf is NOT NULL and t.LinkIdGf is NOT NULL;";
			logger.info(sql);
			logger.info(" Network: "+networkidBrField);
			List<Link> links  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Link.class), networkidBrField,nodeid,nodeid, networkidGrField,nodeid,nodeid);
			return links;
		}
	 
	 public List<Link> findDeletedLinksInBrField(int networkidGrField,int networkidBrField, int nodeid ){
			String sql = "select * from (select gf.NetworkId , gf.LinkId , gf.SrcNode, gf.DestNode, gf.Colour, gf.MetricCost, gf.Length, gf.SpanLoss, gf.Capacity" + 
					" , gf.FibreType, gf.SrlgId, gf.NSplices, gf.LossPerSplice, gf.NConnector, gf.LossPerConnector, gf.CalculatedSpanLoss , gf.SpanLossCoff," + 
					" gf.CdCoff, gf.Cd, gf.PmdCoff, gf.Pmd, gf.SrcNodeDirection, gf.DestNodeDirection, gf.OMSProtection, bf.NetworkId as NetworkIdBf " + 
					" , bf.LinkId as LinkIdBf from " + 
					" ( select * from Link where NetworkId = ? and (SrcNode =? or DestNode=?) ) as gf " + 
					" left join ( select * from Link where NetworkId = ? and (SrcNode =? or DestNode=?) ) as bf " + 
					" on gf.LinkId = bf.LinkId ) as t " + 
					" where t.NetworkIdBf is NULL and t.LinkIdBf is NULL;";
			MainMap.logger.info(sql);
			MainMap.logger.info(" Network: "+networkidBrField);
			List<Link> links  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Link.class), networkidGrField,nodeid, nodeid, networkidBrField, nodeid, nodeid);
			return links;
		}
	 
	 public List<Link> findDeletedLinksInBrField(int networkidGrField,int networkidBrField){
			String sql = "select * from (select gf.NetworkId , gf.LinkId , gf.SrcNode, gf.DestNode, gf.Colour, gf.MetricCost, gf.Length, gf.SpanLoss, gf.Capacity" + 
					" , gf.FibreType, gf.SrlgId, gf.NSplices, gf.LossPerSplice, gf.NConnector, gf.LossPerConnector, gf.CalculatedSpanLoss , gf.SpanLossCoff," + 
					" gf.CdCoff, gf.Cd, gf.PmdCoff, gf.Pmd, gf.SrcNodeDirection, gf.DestNodeDirection, gf.OMSProtection, bf.NetworkId as NetworkIdBf " + 
					" , bf.LinkId as LinkIdBf from " + 
					" ( select * from Link where NetworkId = ? ) as gf " + 
					" left join ( select * from Link where NetworkId = ? ) as bf " + 
					" on gf.LinkId = bf.LinkId ) as t " + 
					" where t.NetworkIdBf is NULL and t.LinkIdBf is NULL;";
			logger.info(sql);
			logger.info(" Network: "+networkidBrField);
			List<Link> links  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Link.class), networkidGrField, networkidBrField);
			return links;
		}
	 
	 public void copyLinkDataInBrField(int networkidGrField, int networkidBrField ) throws SQLException {			
		 	
		    String sql = "insert into Link ( NetworkId, LinkId, SrcNode, DestNode, Colour, MetricCost, Length, SpanLoss, Capacity, FibreType, SrlgId, NSplices, LossPerSplice, NConnector, LossPerConnector, CalculatedSpanLoss, SpanLossCoff, CdCoff, Cd, PmdCoff, Pmd,SrcNodeDirection, DestNodeDirection,OMSProtection,Linktype) select ?, LinkId, SrcNode, DestNode, Colour, MetricCost, Length, SpanLoss, Capacity, FibreType, SrlgId, NSplices, LossPerSplice, NConnector, LossPerConnector, CalculatedSpanLoss, SpanLossCoff, CdCoff, Cd, PmdCoff, Pmd,SrcNodeDirection, DestNodeDirection,OMSProtection,LinkType from Link where NetworkId = ? ";
		    logger.info("copyLinkDataInBrField: "+sql); 	     
		    jdbcTemplate.update( sql,networkidBrField,networkidGrField);
			 	}

	 public List<Link> findlinkId(int networkId){
			String sql = "SELECT LinkId FROM Link where NetworkId =?";
			List<Link> link  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Link.class),networkId);
			return link;
		}
	 
	 
	 
	 
	 
	
	 }
