package application.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import application.constants.ResourcePlanConstants;
import application.model.IpSchemeLink;
import application.model.IpSchemeNode;

@Component
public class IpSchemeLinkDao{
	@Autowired
    private JdbcTemplate jdbcTemplate;
	public static Logger logger = Logger.getLogger(ResourcePlanConstants.DatabasePrintStr);
		 
	 @SuppressWarnings({ "unchecked", "rawtypes" })
		 
	 public List<IpSchemeLink> findAll(int networkId){
			String sql = "SELECT * FROM IpSchemeLink where NetworkId = ? ";
			List<IpSchemeLink> ipnode  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(IpSchemeLink.class), networkId);
			return ipnode;
		}
	 
	 
	 public void insertIpSchemeLink(final IpSchemeLink ipSchemeLink) throws SQLException 
	 {
	     logger.info("Insert IpSchemeLink ");
	     String sql = "INSERT into IpSchemeLink(NetworkId, LinkId, LinkIp, SrcIp, DestIp, SubnetMask) VALUES (?, ?, ?, ?, ?, ?)";
	     jdbcTemplate.update(
	             sql,
	             new Object[] { ipSchemeLink.getNetworkId(),ipSchemeLink.getLinkId(), ipSchemeLink.getLinkIp(), ipSchemeLink.getSrcIp(), ipSchemeLink.getDestIp(), ipSchemeLink.getSubnetMask()});
	 }
	 
	 public void deleteByNetworkId(int networkid) {
	        logger.info("Delete IpSchemeLink");
	        String sql = "delete from IpSchemeLink where NetworkId = ?";
	        jdbcTemplate.update(sql, networkid);      
	    }
	 
	 public int count(int networkid)
	 {		
		 logger.info("Count IpSchemeLink");
		  String sql = "select count(*) from IpSchemeLink where NetworkId = ?"; 
		  return jdbcTemplate.queryForObject(sql, int.class, networkid);		  		        
	 }
	 /*Query to find the ip Scheme LinkWise*/
	 public List<IpSchemeLink> findIpSchemeLinkwise(int networkId, int linkid){
			String sql = "SELECT * FROM IpSchemeLink where LinkId = ? and NetworkId = ? ;";
			List<IpSchemeLink> ipnode  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(IpSchemeLink.class),linkid, networkId);
			return ipnode;
		}

	
	 public IpSchemeLink findIpSchemeLink(int networkid, int linkId) {
	        logger.info("Query Node: findIpSchemeLink");
	        String sql = "SELECT * FROM IpSchemeLink where NetworkId = ? and LinkId = ?";	   
	        logger.info(sql);
	        logger.info("For Network Id: "+ networkid+ " LinkId: "+linkId);
	        IpSchemeLink link  = (IpSchemeLink) jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper(IpSchemeLink.class),networkid,linkId);			
			return link; 
	    }
	 
	 /**
	  * finds the IP of a particular link on a node
	  * @param networkid
	  * @param srcnode
	  * @param destnode
	  * @return
	  */
	 public Object findIpOfSrcNodeEnd_cf(int networkid, int srcnode , int destnode) {
	        logger.info("Query Node: findIpOfANodeEnd");
	        String sql = "SELECT data.SrcIp "
	        		+ "from (SELECT l.NetworkId, l.LinkId, l.SrcNode, l.DestNode, ipl.SrcIp, ipl.DestIp from Link l "
	        		+ "left join IpSchemeLink ipl on l.LinkId=ipl.LinkId and l.NetworkId = ipl.NetworkId) as data "
	        		+ "where data.NetworkId = ? and data.SrcNode = ? and data.DestNode = ?  "
	        		+ "UNION "
	        		+ "select data.DestIp from (select l.NetworkId, l.LinkId, l.SrcNode, l.DestNode, ipl.SrcIp, ipl.DestIp from Link l "
	        		+ "left join IpSchemeLink ipl on l.LinkId=ipl.LinkId and l.NetworkId = ipl.NetworkId) as data "
	        		+ "where data.NetworkId = ? and data.DestNode = ? and data.SrcNode = ? ;";	   
	        logger.info(sql);
	        logger.info("For Network Id: "+ networkid+ " Src Id: "+srcnode + " Dest Id: "+destnode);
	        Object link  =  jdbcTemplate.queryForObject(sql,long.class, networkid,srcnode,destnode,networkid,srcnode,destnode);			
			return link; 
	    }
	 
	 /**
	  *
	  * @brief  Query to Find out last generated ip in case of Brown Field 
	  *         [SELECT * FROM IpSchemeLink where NetworkId= ?  ORDER BY NetworkId DESC LIMIT 1 ;]  
	  * @date   16th Oct, 2017
	  * @author hp
	  */
	 public List<IpSchemeLink>  findLastGeneratedLinkIP(int networkId){
		 String sql = "SELECT * FROM IpSchemeLink where NetworkId = ?  ORDER BY DestIp DESC LIMIT 1;";
		 List<IpSchemeLink>  ipLink  =  jdbcTemplate.query(sql, new BeanPropertyRowMapper(IpSchemeLink.class),networkId);
		 return ipLink;
	 }


	 public void copyIpSchemeLinkInBrField(int networkidGrField, int networkidBrField ) throws SQLException {				 	
	     
		 String sql = "insert into IpSchemeLink (NetworkId, LinkId, LinkIp, SrcIp, DestIp, SubnetMask) "+
					" select ?, LinkId, LinkIp, SrcIp, DestIp, SubnetMask "+
					" from IpSchemeLink where NetworkId = ? ";
		logger.info("copyIpSchemeLinkInBrField: "+sql); 	     
		jdbcTemplate.update( sql,networkidBrField,networkidGrField);
	}

}
