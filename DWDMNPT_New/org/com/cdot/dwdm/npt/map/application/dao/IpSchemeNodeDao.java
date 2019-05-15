package application.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import application.constants.ResourcePlanConstants;
import application.model.IpSchemeNode;
import application.model.Node;

@Component
public class IpSchemeNodeDao{
	@Autowired
    private JdbcTemplate jdbcTemplate;
	public static Logger logger = Logger.getLogger(ResourcePlanConstants.DatabasePrintStr);
 
	 
	 public List<IpSchemeNode> findAll(int networkId){
		 String sql = "SELECT * FROM IpSchemeNode where NetworkId = ?";
		 List<IpSchemeNode> ipnode  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(IpSchemeNode.class), networkId);
		 return ipnode;
	 }
	 
	 public void insertIpSchemeNode(final IpSchemeNode ipSchemeNode) throws SQLException 
	 {
	     logger.info("Insert IpSchemeNode ");
	     String sql = "INSERT into IpSchemeNode(NetworkId, NodeId, LctIp, RouterIp, ScpIp, McpIp, McpSubnet, McpGateway, RsrvdIp1, RsrvdIp2) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	     jdbcTemplate.update(
	             sql,
	             new Object[] { ipSchemeNode.getNetworkId(), ipSchemeNode.getNodeId(), ipSchemeNode.getLctIp(), ipSchemeNode.getRouterIp(), ipSchemeNode.getScpIp(), ipSchemeNode.getMcpIp(), ipSchemeNode.getMcpSubnet(), ipSchemeNode.getMcpGateway(), ipSchemeNode.getRsrvdIp1(), ipSchemeNode.getRsrvdIp2() });
	 }
	 
	 public void deleteByNetworkId(int networkid) {
	        logger.info("Delete IpSchemeNode");
	        String sql = "delete from IpSchemeNode where NetworkId = ?";
	        jdbcTemplate.update(sql, networkid);      
	    }
	 
	 public IpSchemeNode findIpSchemeNode(int networkid, int nodeid) {
	        logger.info("Query Node: findIpSchemeNode");
	        String sql = "SELECT * FROM IpSchemeNode where NetworkId = ? and NodeId = ?";	   
	        logger.info(sql);
	        logger.info("For Network Id: "+ networkid+ " Node Id: "+nodeid);
	        IpSchemeNode node  = (IpSchemeNode) jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper(IpSchemeNode.class),networkid,nodeid);			
			return node; 
	    }
	 
	 public int count(int netowrkId)
	 {		
		 logger.info("Count IpSchemeNode");
		  String sql = "select count(*) from IpSchemeNode where NetworkId = ?"; 
		  return jdbcTemplate.queryForObject(sql, int.class, netowrkId);		  		        
	 }
	 /*Query to find the ip Scheme NodeWise*/
	 public List<IpSchemeNode> findIpSchemeNodewise(int networkId, int nodeid){
			String sql = "SELECT * FROM IpSchemeNode where nodeid = ? and NetworkId = ? ";
			List<IpSchemeNode> ipnode  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(IpSchemeNode.class),nodeid, networkId);
			return ipnode;
	 }	
	 
	 /**
	  *
	  * @brief  Query to Find out last generated ip in case of Brown Field 
	  *         [SELECT * FROM IpSchemeNode where NetworkId= ?  ORDER BY RsrvdIp1 DESC LIMIT 1 ;]  
	  * @date   16th Oct, 2017
	  * @author hp
	  */
	 public List<IpSchemeNode>  findLastGeneratedNodeIP(int networkId){
		 	logger.info("networkId  " + networkId);
			String sql = "SELECT * FROM IpSchemeNode where NetworkId = ?  ORDER BY RsrvdIp1 DESC LIMIT 1;";
			List<IpSchemeNode>  ipnode  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(IpSchemeNode.class),networkId);
			return ipnode;
	 }	

	 public void copyIpSchemeNodeInBrField(int networkidGrField, int networkidBrField ) throws SQLException {			
		 	
		String sql = "insert into IpSchemeNode (NetworkId, NodeId, LctIp, RouterIp, ScpIp, McpIp, McpSubnet, McpGateway, RsrvdIp1, RsrvdIp2) "+
					" select ?, NodeId, LctIp, RouterIp, ScpIp, McpIp, McpSubnet, McpGateway, RsrvdIp1, RsrvdIp2 "+
					" from IpSchemeNode where NetworkId = ? ";
		logger.info("copyIpSchemeNodeInBrField: "+sql); 	     
		jdbcTemplate.update( sql,networkidBrField,networkidGrField);
	}

	 
	 
	 
	 

	 }
