package application.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import application.MainMap;
import application.constants.ResourcePlanConstants;
import application.model.Circuit;
import application.model.Network;
import application.model.NetworkRoute;
import application.model.Topology;

@Component
public class TopologyDao{
	@Autowired
    private JdbcTemplate jdbcTemplate;
	public static Logger logger = Logger.getLogger(ResourcePlanConstants.DatabasePrintStr);
	
	 public void queryTopology() {
	        logger.info("Query Topology");
	        String sql = "SELECT * FROM Topology";
	        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
	        for (Map<String, Object> row : list) {	        	
	            logger.info(" NetworkId: "+row.get("NetworkId")+" NodeId: "+row.get("NodeId")+" Dir 1: "+ row.get("Dir1")+" Dir 2: "+row.get("Dir2")+" Dir 3: "+row.get("Dir3")+" Dir 4: "+row.get("Dir4")+" Dir 5: "+row.get("Dir5")+" Dir 6: "+row.get("Dir6")+" Dir 7: "+row.get("Dir7")+" Dir 8: "+row.get("Dir8"));
	        }
	    }
	 
	 public List<Map<String, Object>> findAllTopology() {
	        logger.info("Find All Topologies");
	        String sql = "SELECT * FROM Topology";
	        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
	        return list;
	    }
	 
	 public List<Topology> findAll(){
			String sql = "SELECT * FROM Topology";
			@SuppressWarnings("unchecked")
			List<Topology> topology  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Topology.class));
			return topology;
		}
	 
	 public void insertTopology(final Topology topology) throws SQLException 
	 {
     logger.info("Insert Topology ");
     String sql = "INSERT into Topology(NetworkId, NodeId, Dir1, Dir2, Dir3, Dir4, Dir5, Dir6, Dir7, Dir8) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
     jdbcTemplate.update(
             sql,
             new Object[] { topology.getNetworkId(), topology.getNodeId(),topology.getDir1(),topology.getDir2(),topology.getDir3(),topology.getDir4(),topology.getDir5(),topology.getDir6(),topology.getDir7(),topology.getDir8()  });
	 }

	 public void deleteByNetworkId(int networkid) {
	        logger.info("Delete Topology");
	        String sql = "delete from Topology where NetworkId = ?";
	        jdbcTemplate.update(sql, networkid);      
	    }
	 	 
	 public int count()
	 {		
		 logger.info("Count Topology");
		  String sql = "select count(*) from Topology"; 
		  return jdbcTemplate.queryForObject(sql, int.class);		  		        
	 }
	 
	 public int count(int networkId)
	 {		
		 logger.info("Count Topology for NetworkId:"+networkId);
		  String sql = "select count(*) from Topology where NetworkId=?"; 
		  return jdbcTemplate.queryForObject(sql, int.class,networkId);		  		        
	 }
	 
	 public List<Map<String, Object>> queryTopologyWithNodeType() {
		 logger.info("Query queryTopologyWithNodeType..");
	        String sql = "SELECT t.NetworkId, t.NodeId, n.NodeType, n.NodeSubType, t.Dir1, t.Dir2, t.Dir3, t.Dir4, t.Dir5, t.Dir6, t.Dir7, t.Dir8 FROM Topology t, Node n where t.NodeId=n.NodeId";
	        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
	        for (Map<String, Object> row : list) {	        	
	            logger.info(" NetworkId: "+row.get("NetworkId")+" NodeId: "+row.get("NodeId")+" Node Type: "+row.get("NodeType")+" Node Sub Type: "+row.get("NodeSubType")+" Dir 1: "+ row.get("Dir1")+" Dir 2: "+row.get("Dir2")+" Dir 3: "+row.get("Dir3")+" Dir 4: "+row.get("Dir4")+" Dir 5: "+row.get("Dir5")+" Dir 6: "+row.get("Dir6")+" Dir 7: "+row.get("Dir7")+" Dir 8: "+row.get("Dir8"));
	        }
			return list;
	    }
	 
	 public List<Map<String, Object>> queryTopologyWithNodeTypeWithNetworkId(int networkId) {
		 logger.info("Query queryTopologyWithNodeType..");
	        String sql = "SELECT t.NetworkId, t.NodeId, n.NetworkId, n.NodeType, n.NodeSubType, t.Dir1, t.Dir2, t.Dir3, t.Dir4, t.Dir5, t.Dir6, t.Dir7, t.Dir8 FROM Topology t, Node n where t.NodeId=n.NodeId"
	        		     + " and t.NetworkId=? and n.NetworkId=?";
	        logger.info("sql "+ sql);
	        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,networkId,networkId);
	        for (Map<String, Object> row : list) {	        	
	            logger.info(" NetworkId: "+row.get("NetworkId")+" NodeId: "+row.get("NodeId")+" Node Type: "+row.get("NodeType")+" Node Sub Type: "+row.get("NodeSubType")+" Dir 1: "+ row.get("Dir1")+" Dir 2: "+row.get("Dir2")+" Dir 3: "+row.get("Dir3")+" Dir 4: "+row.get("Dir4")+" Dir 5: "+row.get("Dir5")+" Dir 6: "+row.get("Dir6")+" Dir 7: "+row.get("Dir7")+" Dir 8: "+row.get("Dir8"));
	        }
			return list;
	    }
	 
	 public Topology findTopology(int networkid, int nodeid) {		 
	        String sql = "SELECT * FROM Topology where NetworkId = ? and NodeId = ?";	        
	        Topology topo  = (Topology) jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper(Topology.class) ,networkid, nodeid);	        
			return topo;
	    }
	 
	 public void copyTopologyDataInBrField(int networkidGrField, int networkidBrField ) throws SQLException {			
		 	
		    String sql = "insert into Topology ( NetworkId, NodeId, Dir1, Dir2, Dir3, Dir4, Dir5, Dir6, Dir7, Dir8 ) select ?, NodeId, Dir1, Dir2, Dir3, Dir4, Dir5, Dir6, Dir7, Dir8 from Topology where NetworkId = ? ";
		    logger.info("copyTopologyDataInBrField: "+sql); 	     
		    jdbcTemplate.update( sql,networkidBrField,networkidGrField);
			 	}
	 
}
