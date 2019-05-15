package application.dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;
import com.mysql.jdbc.exceptions.jdbc4.MySQLDataException;

import application.model.Circuit;
import application.model.Network;
import application.MainMap;
import application.constants.MapConstants;
import application.constants.ResourcePlanConstants;
import application.model.CardInfo;

@Component
public class NetworkDao{
	@Autowired
    private JdbcTemplate jdbcTemplate;
	public static Logger logger = Logger.getLogger(ResourcePlanConstants.DatabasePrintStr);
	
	 public void queryNetworks() {
	        logger.info("Query Network");
	        String sql = "SELECT * FROM Network";
	        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
	        for (Map<String, Object> row : list) {	        	
	            logger.info(" NetworkId: "+row.get("NetworkId")+" NetworkName: "+row.get("NetworkName"));
	        }
	    }
	 
	 public List<Network> findAllNetworks() {
	        logger.info("Find All Networks");
	        String sql = "SELECT * FROM Network";
	        List<Network> network  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Network.class));
	        return network;
	    }
	 
	 public Network findNetwork(int networkId) {
	        logger.info("Find Network"+networkId);
	        String sql = "SELECT * FROM Network where NetworkId=?";
	        Network network  = (Network) jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper(Network.class),networkId); 
	        return network;
	    }
	 
	 @SuppressWarnings({ "unchecked", "rawtypes" })
		public List<Network> findAll(int networkId){
				String sql = "SELECT * FROM Network where NetworkId = ? ;";
				List<Network> network  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Network.class), networkId);
				return network;
			}
	 
	 public void insertNetwork(Network network) throws SQLException {     
     int id = 0;
     Object val = getNextNetworkId();
     if(val!=null)
     {
    	 id=Integer.parseInt(val.toString());
     }
     else
     {    	 
    	 id=1;
     }
     logger.info("NetworkDao.insertNetwork()"+"trying to insert at "+id +" for Network Name :"+network.getNetworkName());
     String sql = "INSERT into Network(NetworkId, SubNetworkId, NetworkName, Topology, Area, ServiceProvider, NetworkIdBrownField, NetworkUpdateDate, NetworkBrownFieldUpdateDate, UserName) VALUES ( ?,?,?, ?,?, ?, ?,?, ?,?)";
     jdbcTemplate.update(sql,new Object[] { id,network.getSubNetworkId(), network.getNetworkName(),network.getTopology(), network.getArea(), network.getServiceProvider(), network.getNetworkIdBrownField(),network.getNetworkUpdateDate(),network.getNetworkBrownFieldUpdateDate(), network.getUserName() });
     
     String mapStr="";
     sql = "INSERT into MapData(NetworkId,Map) VALUES ( ?,?)";
     jdbcTemplate.update(sql,new Object[] { id, mapStr});
 
	 
	 }

	 public void deleteByNetworkId(int networkid) {
	        logger.info("Delete Network deleteByNetworkId:"+networkid);
	        /*String sql = "delete from Network where NetworkId = ?";
	        jdbcTemplate.update(sql, networkid);      
	        */
	        
	        String sql = "delete from Node where NetworkId = ?";
	        jdbcTemplate.update(sql, networkid);
	        

	        sql = "delete from Link where NetworkId = ?";
	        jdbcTemplate.update(sql, networkid);
	        

	        sql = "delete from Topology where NetworkId = ?";
	        jdbcTemplate.update(sql, networkid);
	        
	        sql = "delete from MapData where NetworkId = ?";
	        jdbcTemplate.update(sql, networkid);
	    }
	 
	 
	 public void deleteWholeNetwork(int networkid) {
	        logger.info("Delete Network");
	        String sql = "delete from Network where NetworkId = ?";
	        jdbcTemplate.update(sql, networkid);      
	        
	        
//	        sql = "delete from Node where NetworkId = ?";
//	        jdbcTemplate.update(sql, networkid);
//	        
//
//	        sql = "delete from Link where NetworkId = ?";
//	        jdbcTemplate.update(sql, networkid);
//	        
//
//	        sql = "delete from Topology where NetworkId = ?";
//	        jdbcTemplate.update(sql, networkid);
//	        
//	        sql = "delete from MapData where NetworkId = ?";
//	        jdbcTemplate.update(sql, networkid);
	        
	    }
	 
	 public int count()
	 {		
		 logger.info("Count Network");
		  String sql = "select count(*) from Network"; 
		  return jdbcTemplate.queryForObject(sql, int.class);		  		        
	 }
	 
//	 @SuppressWarnings("null")
//	public Object getByNetworkName(String networkName) {
//	        logger.info("getByNetworkName");
//	        String sql = "select NetworkId from Network where NetworkName = ?";
//	        try
//	        {
//	        	return jdbcTemplate.queryForObject(sql, Integer.class, networkName);	       
//	        }
//	        catch(EmptyResultDataAccessException e) {
//	    		return null;
//	    	}	        
//	    }
	 
	 @SuppressWarnings("null")
	 public Object getByNetworkName(String networkName) {
		 logger.info("getByNetworkName");
		 String sql = "select NetworkId from Network where NetworkName = ?  and NetworkIdBrownField=-1";
		 Object networkId;
		 try
		 {
			 // Check If Bf exists
			 networkId= jdbcTemplate.queryForObject(sql, Integer.class, networkName);
			 System.out.println("BF result:"+networkId.toString());
		 }
		 catch(EmptyResultDataAccessException e) {
			 // If bf doesn't exist , then get the gf instance
			 sql="select NetworkId from Network where NetworkName = ? and NetworkIdBrownField=0";
			 try {
				 networkId= jdbcTemplate.queryForObject(sql, Integer.class, networkName);
				 System.out.println("Emprt result:"+networkId.toString());
				
			} catch (EmptyResultDataAccessException e2) {
				// TODO: handle exception
				return null;
			}
			
		 }	     
		 return networkId;
	 }
	 	 
	 @SuppressWarnings("null")
		public Object networkNameExists(String networkName,String userName) {
		        logger.info("getByNetworkName");
		        String sql = "select NetworkId from Network where NetworkName = ? and UserName=?";
		        try
		        {
		        	return jdbcTemplate.queryForObject(sql, Integer.class, networkName,userName);	       
		        }
		        catch(EmptyResultDataAccessException e) {
		    		return null;
		    	}	        
		    }
	 
//	 @SuppressWarnings("null")
//		public Network getNetworkInfoByNetworkName(String networkName) {
//		        logger.info("getNetworkInfoByNetworkName");
//		        String sql = "select * from Network where NetworkName = ?";
//		        try
//		        {
//		        	return (Network) jdbcTemplate.queryFornetworkdaoObject(sql, new BeanPropertyRowMapper(Network.class), networkName);	       
//		        }
//		        catch(EmptyResultDataAccessException e) {
//		    		return null;
//		    	}	        
//		    }
	 
	 @SuppressWarnings("null")
	 public Network getNetworkInfoByNetworkName(String networkName) {
		 logger.info("getNetworkInfoByNetworkName");
		 String sql = "select * from Network where NetworkName = ? and NetworkIdBrownField=-1";
		 Network network;
		 try
		 {
			 // Check If Bf exists
			 network= (Network) jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper(Network.class), networkName);

		 }	
		 catch(EmptyResultDataAccessException e) {
			 //If bf doesn't exist , then get the gf instance
			 		
				 sql="select * from Network where NetworkName = ? and NetworkIdBrownField=0";
				 try {
					 network= (Network) jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper(Network.class), networkName);
				} catch (EmptyResultDataAccessException e2) {
					// TODO: handle exception
					return null;
				}				 
		 }	      
		 return network;
	 }
	 
	 
	 @SuppressWarnings("null")
		public Network getNetworkInfoByBFNetworkId(int networkId) {
		        logger.info("getNetworkInfoByBFNetworkId");
		        String sql = "select * from Network where NetworkIdBrownField = ?";
		        try
		        {
		        	return (Network) jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper(Network.class), networkId);	       
		        }
		        catch(EmptyResultDataAccessException e) {
		    		return null;
		    	}	        
		    }
	 
	
		public Object getNextNetworkId() {
		        logger.info("getNextNetworkId");
//		        String sql = "SELECT MAX(NetworkId)+1 FROM Network";
		        String sql="select NetworkId + 1\r\n" + 
		        		"from Network t\r\n" + 
		        		"where not exists (select 1 from Network t2 where t2.NetworkId = t.NetworkId + 1)\r\n" + 
		        		"order by NetworkId limit 1";
		        try
		        {
		        	return jdbcTemplate.queryForObject(sql, Integer.class);		       
		        }
		        catch(EmptyResultDataAccessException e) {
		    		return null;
		    	}	        
		    }
		
		public List<Network> findNetworkByUserName(String username){
			int greenFieldValue=MapConstants.GreenFieldBfId,brownFieldValue=MapConstants.BrownFieldBfId;
			String sql = "SELECT * FROM Network where UserName = ? and (NetworkIdBrownField=? or NetworkIdBrownField=?)";
			List<Network> network  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Network.class),username,greenFieldValue,brownFieldValue);
			return network;
		}
		
		public void update(int networkid, int NetworkIdBrownField, Timestamp NetworkBrownFieldUpdateDate) throws SQLException {
			   
		     String sql = "Update Network set NetworkIdBrownField = ?, NetworkBrownFieldUpdateDate = ? where NetworkId = ? ";
		     logger.info("updateNetwork: "+sql); 	     
		     jdbcTemplate.update(
		             sql,
		             new Object[] { NetworkIdBrownField, NetworkBrownFieldUpdateDate, networkid});
			 	}
		
		public Object getBrownFieldNetworkId(int networkidGrField) {
	        logger.info("getBrownFieldNetworkId");
	        String sql = "SELECT NetworkIdBrownField FROM Network where NetworkId = ?";
	        try
	        {
	        	return jdbcTemplate.queryForObject(sql, Integer.class,networkidGrField);		       
	        }
	        catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}	        
	    }
		
		public Object getGreenFieldNetworkId(int networkidBrField) {
	        logger.info("getGreenFieldNetworkId");
	        String sql = "SELECT NetworkId FROM Network where NetworkIdBrownField = ?";
	        try
	        {
	        	return jdbcTemplate.queryForObject(sql, Integer.class,networkidBrField);		       
	        }
	        catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}	        
	    }
		
		public boolean isBrownFieldNetwork(int networkid) {
	        logger.info("isBrownFieldNetwork");
	        boolean flag = true;
	        String sql = "select NetworkId from Network where NetworkIdBrownField=?";
	        try
	        {
	        	jdbcTemplate.queryForObject(sql, Integer.class,networkid);	        	
	        }
	        catch(EmptyResultDataAccessException e) {
	        	return flag =false;	    		
	    	}
			return flag;	        
	    }
		
		public void updateBrFieldId(int networkid,int val) {
	        logger.info("updateBrFieldId");
	        boolean flag = true;
	        String sql = "Update Network set NetworkIdBrownField=? where NetworkId=?";
	        try
	        {
	        	jdbcTemplate.update(sql,val,networkid);
//	        	jdbcTemplate.query(sql,networkid);	        	
	        }
	        catch(EmptyResultDataAccessException e) {
	        	logger.error(e.toString());	    		
	    	}        
	    }
		
		@SuppressWarnings("unchecked")
		 public int updateSchema(String sql,String tableName){		
			 System.out.println("updateSchema : "+sql);
			 try {
				 int returnValue=MapConstants.SUCCESS;
				 try {
					 jdbcTemplate.update(sql);
					 logger.debug(tableName+" updateSchema : Schema Updated Successfully ... ");
					 System.out.println(tableName+" updateSchema : Schema Updated Successfully ... ");
					 return returnValue;
				} catch (BadSqlGrammarException e) {
					// TODO: handle exception
					logger.debug(" updateSchema : Schema already Updated ");
					 System.out.println(" updateSchema : Schema already Updated ");
					 return returnValue;
				}					 
			 }
			 catch(Exception e ) {				 
				 e.printStackTrace();
				 System.out.println(e.getClass().getCanonicalName());
				 return 0;
			 } 

		 }
	 

	 }
