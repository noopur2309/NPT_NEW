package application.dao;
import java.util.*;

import java.sql.SQLException;
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
import application.model.LambdaLspInformation;
import application.model.NetworkRoute;
import application.model.Node;
import application.model.RouteMapping;

@Component
public class NodeDao{
	@Autowired
    private JdbcTemplate jdbcTemplate;
	public static Logger logger = Logger.getLogger(ResourcePlanConstants.DatabasePrintStr);
	
	 public void queryNodes() {
//	        logger.info("Query Node");
	        String sql = "SELECT * FROM Node";
	        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
	        for (Map<String, Object> row : list) {	        	
	            logger.info(" NetworkId: "+row.get("NetworkId")+" NodeId: "+row.get("NodeId")+" StationName: "+ row.get("StationName")+" SiteName: "+row.get("SiteName"));
	        }
	    }
	 public void updatedb(HashMap<String,Object> DifferenceObj,int NetWorkId,int NodeId)
	 {    
		 String sql;
	     String  sample = null;
	    
	    
	 Iterator<Map.Entry<String, Object>> itr = DifferenceObj.entrySet().iterator(); 
     
     while(itr.hasNext()) 
     { 
          Map.Entry<String, Object> entry = itr.next(); 
          
          if(sample==null)
          {  sample = entry.getKey()+"="+"'"+entry.getValue()+"'";
          
          }
          else 
          {
          sample = sample + entry.getKey()+"="+"'"+entry.getValue()+"'";
          }
          if(itr.hasNext())
          { sample = sample + ",";
          }
     }
        sql = "UPDATE Node SET " +" "+sample + " "+" where NetworkId =? and NodeId = ?;";
        
        logger.info("For Network Id: "+ NetWorkId+ " Node Id: "+NodeId);
        logger.info(sql);
        jdbcTemplate.update(sql,NetWorkId,NodeId);
        
      
      //System.out.println(sql);  
      
	 }
	 
	 public Node findNode(int networkid,int nodeid) {
//	        logger.info("Query Node: findNode");
	        String sql = "SELECT * FROM Node where NetworkId = ? and NodeId = ?";
			logger.info("For Network Id: "+ networkid+ " Node Id: "+nodeid);
	        try
	        {
	        	Node node  = (Node) jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper(Node.class),networkid,nodeid);			
	        	return node;
	        }
			catch(EmptyResultDataAccessException e)
	        {
		    		return null;
		    }
	    }
	 
	 public List<Map<String, Object>> findAllNodes(int networkId) {
//	        logger.info("Find All Nodes");
	        String sql = "SELECT * FROM Node where NetworkId = ? ";
	        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, networkId);
	        return list;
	    }
	 
	 public List<Node> findAll(int networkid){
			String sql = "SELECT * FROM Node where NetworkId = ?";
			List<Node> nodes  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Node.class),networkid);
			return nodes;
		}
	 
	 public List<Node> findAllNonIlaNodes(){
			String sql = "SELECT * FROM Node where NodeType!="+MapConstants.ila;
			List<Node> nodes  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Node.class));
			return nodes;
		}
	 
	 public List<Node> findAllNonILAsNetworkId(int networkId){
			String sql = "SELECT * FROM Node where NodeType!="+MapConstants.ila+" and NetworkId=?";
			List<Node> nodes  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Node.class),networkId);
			return nodes;
		}
	 
	 public void insertNode(final Node node) throws SQLException {
     logger.info("Insert node ");     
     String sql = "INSERT into Node(NetworkId, NodeId, StationName, SiteName, NodeType, NodeSubType, Degree, Ip, IsGne, "
     		+ "VlanTag, EmsSubnet, EmsGateway, IpV6Add, Capacity, Direction, OpticalReach) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, "
     		+ "?, ?, ?, ?, ?, ?,?)";
     jdbcTemplate.update(
             sql,
             new Object[] { node.getNetworkId(), node.getNodeId(), node.getStationName(), node.getSiteName(), node.getNodeType(), 
            		 node.getNodeSubType(), node.getDegree(), node.getIp(), node.getIsGne(), node.getVlanTag(), node.getEmsSubnet(), 
            		 node.getEmsGateway(), node.getIpV6Add(), node.getCapacity(), node.getDirection(), node.getOpticalReach() });
 }
	 
	 public int count(int networkId)
	 {		
		 logger.info("Count Node for NetworkId:"+networkId);
		  String sql = "select count(*) from Node where NetworkId = ? "; 
		  return jdbcTemplate.queryForObject(sql, int.class, networkId);		  		        
	 }
	 
	 /*query to count the total number of ILA's/RODAMS/TE's //Tanuj*/	 
	 public List<Node> findAllByNodeType(int networkId, int nodetype)
	 {		
		 logger.info("Count type of nodes");
		  String sql = "SELECT * from Node where NodeType = ? and NetworkId = ? ;"; 
		  List<Node> list= jdbcTemplate.query(sql,new BeanPropertyRowMapper(Node.class),nodetype,networkId);		
		  return list;
	 }
	 
	 /*query to count the total number of ILA's/RODAMS/TE's //Tanuj*/	 
	 public int nodeTypecount(int networkId, int nodetype)
	 {		
		 logger.info("Count type of nodes");
		  String sql = "SELECT COUNT(*) from Node where NodeType = ? and NetworkId = ? ;"; 
		  return jdbcTemplate.queryForObject(sql, int.class,nodetype,networkId);		  		        
	 }
	 /*query to find out the sitename by nodeid //Tanuj*/	 
	 public String findsitebynodeid(int nodeid, int networkId)
	 {		
		 logger.info("Finding the sitename by Nodeid and NetworkId");
		  String sql = "SELECT SiteName from Node where NodeId = ? and NetworkId= ?"; 
		  return jdbcTemplate.queryForObject(sql, String.class,nodeid, networkId);		  		        
	 }
	 /*query to find out the stationname by nodeid : @Hp*/	 
	 public String findstationbynodeid(int nodeid, int networkId)
	 {		
		 logger.info("Finding the StationName by Nodeid");
		  String sql = "SELECT  StationName from Node where NodeId = ? and NetworkId= ?";  
		  return jdbcTemplate.queryForObject(sql, String.class,nodeid, networkId);			  		        
	 }
	 
	 /**
	  * Query to find the nodes that are being modified in the brown field and have the samed ids but different characteristics
	  * @param networkidGrField
	  * @param networkidBrField
	  * @return
	  */
	 public List<Node> findModifiedNodesInBrField(int networkidGrField, int networkidBrField){
			String sql = " select * from (select cbf.NetworkId , cbf.NodeId , cbf.StationName, cbf.SiteName, cbf.NodeType, cbf.NodeSubType, cbf.Degree, cbf.Ip, cbf.IsGne, cbf.VlanTag"
					+ " ,cbf.EmsSubnet, cbf.EmsGateway, cbf.IpV6Add, cbf.Capacity, cbf.Direction, cbf.OpticalReach, c.NetworkId as NetworkIdGf , c.NodeId as NodeIdGf "
					+ "from ( select * from Node where NetworkId = ? ) as cbf left join ( select * from Node where NetworkId = ?  ) as c "
					+ "on  (c.NodeId = cbf.NodeId )and (c.Degree!=cbf.Degree or c.Capacity != cbf.Capacity or c.NodeType != cbf.NodeType or c.NodeSubType != cbf.NodeSubType)) "
					+ "as t where t.NetworkIdGf IS NOT NULL and t.NodeIdGf IS NOT NULL";
			List<Node> nodes  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Node.class),networkidBrField,networkidGrField);
			return nodes;
		}
	 
	 /**
	  * Query to find the nodes that are being modified in the brown field and have the samed ids but different characteristics
	  * @param networkidGrField
	  * @param networkidBrField
	  * @return
	  */
	 public List<Node> findDegreeModifiedNodesInBrField(int networkidGrField, int networkidBrField){
			String sql = " select * from (select cbf.NetworkId , cbf.NodeId , cbf.StationName, cbf.SiteName, cbf.NodeType, cbf.NodeSubType, cbf.Degree, cbf.Ip, cbf.IsGne, cbf.VlanTag"
					+ " ,cbf.EmsSubnet, cbf.EmsGateway, cbf.IpV6Add, cbf.Capacity, cbf.Direction, cbf.OpticalReach, c.NetworkId as NetworkIdGf , c.NodeId as NodeIdGf "
					+ "from ( select * from Node where NetworkId = ? ) as cbf left join ( select * from Node where NetworkId = ?  ) as c "
					+ "on  (c.NodeId = cbf.NodeId )and (c.Degree!=cbf.Degree and c.Capacity = cbf.Capacity and c.NodeType = cbf.NodeType and c.NodeSubType = cbf.NodeSubType)) "
					+ "as t where t.NetworkIdGf IS NOT NULL and t.NodeIdGf IS NOT NULL";
			List<Node> nodes  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Node.class),networkidBrField,networkidGrField);
			return nodes;
		}
	 
	 /**
	  * query finds out the nodes that are new or added in the brown field and not present in the 
	  * green field
	  * @param networkidGrField
	  * @param networkidBrField
	  * @return
	  */
	 public List<Node> findAddedNodesInBrField(int networkidGrField, int networkidBrField){
			String sql = " select * from (select cbf.NetworkId , cbf.NodeId , cbf.StationName, cbf.SiteName, cbf.NodeType, cbf.NodeSubType, cbf.Degree, cbf.Ip, cbf.IsGne, cbf.VlanTag "
					+ ",cbf.EmsSubnet, cbf.EmsGateway, cbf.IpV6Add, cbf.Capacity, cbf.Direction, cbf.OpticalReach, c.NetworkId as NetworkIdGf , c.NodeId as NodeIdGf from "
					+ "( select * from Node where NetworkId = ? ) as cbf left join ( select * from Node where NetworkId = ?  ) as c "
					+ "on c.NodeId = cbf.NodeId ) as t "
					+ "where t.NetworkIdGf is NULL and t.NodeIdGf is NULL";
			List<Node> nodes  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Node.class),networkidBrField,networkidGrField);
			return nodes;
		}
	 
	 
	 /**
	  * query finds out the nodes that are deleted in the brown field and not present in the 
	  * brown field
	  * @param networkidGrField
	  * @param networkidBrField
	  * @return
	  */
	 public List<Node> findDeletedNodesInBrField(int networkidGrField, int networkidBrField){
			String sql = " select * from (select gf.NetworkId , gf.NodeId , gf.StationName, gf.SiteName, gf.NodeType, gf.NodeSubType, gf.Degree, gf.Ip, gf.IsGne, gf.VlanTag "
					+ ",gf.EmsSubnet, gf.EmsGateway, gf.IpV6Add, gf.Capacity, gf.Direction, gf.OpticalReach, bf.NetworkId as NetworkIdBf , bf.NodeId as NodeIdBf from "
					+ "( select * from Node where NetworkId = ? ) as gf left join ( select * from Node where NetworkId = ?  ) as bf "
					+ "on gf.NodeId = bf.NodeId ) as t "
					+ "where t.NetworkIdBf is NULL and t.NodeIdBf is NULL";
			List<Node> nodes  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Node.class),networkidGrField,networkidBrField);
			return nodes;
		}
	 
	 /**
	  * query finds out the nodes that are common/ identical in the brown field and not present in the 
	  * green field
	  * @param networkidGrField
	  * @param networkidBrField
	  * @return
	  */
	 public List<Node> findCommonNodesInBrField(int networkidGrField, int networkidBrField){
			String sql = " select * from (select cbf.NetworkId , cbf.NodeId , cbf.StationName, cbf.SiteName, cbf.NodeType, cbf.NodeSubType, cbf.Degree, cbf.Ip, cbf.IsGne, cbf.VlanTag "
					+ ",cbf.EmsSubnet, cbf.EmsGateway, cbf.IpV6Add, cbf.Capacity, cbf.Direction, cbf.OpticalReach, c.NetworkId as NetworkIdGf , c.NodeId as NodeIdGf from "
					+ "( select * from Node where NetworkId = ? ) as cbf left join ( select * from Node where NetworkId = ?  ) as c "
					+ "on c.NodeId = cbf.NodeId ) as t "
					+ "where t.NetworkIdGf is NOT NULL and t.NodeIdGf is NOT NULL";
			List<Node> nodes  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Node.class),networkidBrField,networkidGrField);
			return nodes;
		}
	 
	 public void copyNodeDataInBrField(int networkidGrField, int networkidBrField ) throws SQLException {			
		 	
		    String sql = "insert into Node ( NetworkId, NodeId, StationName, SiteName, NodeType, NodeSubType, Degree, Ip, IsGne, VlanTag, EmsSubnet, EmsGateway, IpV6Add, Capacity, Direction, OpticalReach ) select ?, NodeId, StationName, SiteName, NodeType, NodeSubType, Degree, Ip, IsGne, VlanTag, EmsSubnet, EmsGateway, IpV6Add, Capacity, Direction, OpticalReach from Node where NetworkId = ? ";
		    logger.info("copyNodeDataInBrField: "+sql); 	     
		    jdbcTemplate.update( sql,networkidBrField,networkidGrField);
			 	}
	 
	 public List<Node> findnodeId(int networkId){
			String sql = "SELECT NodeId FROM Node where NetworkId =?";
			MainMap.logger.info("Finding Node Id");
			List<Node> nodes  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Node.class),networkId);
			return nodes;
		}
	 
	 public List<Integer> findnodeIdIntegers(int networkId){
			String sql = "SELECT NodeId FROM Node where NetworkId =?";
			MainMap.logger.info("Finding Node Id");
			List<Integer> nodes  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Integer.class),networkId);
			return nodes;
		}
	 

	 }
