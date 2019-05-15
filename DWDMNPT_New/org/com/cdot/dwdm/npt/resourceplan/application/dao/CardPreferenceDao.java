package application.dao;

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
import application.constants.ResourcePlanConstants;
import application.model.CardPreference;

@Component
public class CardPreferenceDao{
	@Autowired
    private JdbcTemplate jdbcTemplate;
	public static Logger logger = Logger.getLogger(ResourcePlanConstants.DatabasePrintStr);
	 
	public CardPreference findCardPreference(int networkid, int nodeid, int equipmentid) {
        String sql = "SELECT * FROM CardPreference where NetworkId = ? and NodeId = ? and  EquipmentId = ?";	
        logger.info("findCardPreference: "+sql);
        logger.info("For NetworkId "+networkid+" NodeId "+nodeid+" For EquipmentId "+equipmentid);
        CardPreference info  = (CardPreference) jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper(CardPreference.class),networkid, nodeid,equipmentid);			
		return info; 
    }	
	
	public Object findPreferenceByFeature(int networkid, int nodeid, String cardfeature) throws SQLException {
        String sql = "SELECT EquipmentId FROM CardPreference where NetworkId = ? and NodeId = ? and  CardFeature = ? and Preference = 1 ";	
        logger.info("findPreferenceByFeature: "+sql);
        logger.info("For NetworkId "+networkid+" NodeId "+nodeid+" For CardFeature "+cardfeature);
        Object id=0;        
        try
        {
        	return  id  = jdbcTemplate.queryForObject(sql,(int.class),networkid, nodeid, cardfeature);	       
        }
        catch(EmptyResultDataAccessException e) {
        	 logger.warn("findPreferenceByFeature"+e);
    		return null;
    	}	

    }	
	
	public int findPreferenceByType(int networkid, int nodeid, String cardtype) {
        String sql = "SELECT EquipmentId FROM CardPreference where NetworkId = ? and NodeId = ? and  CardType = ? and Preference = 1 ";	
        logger.info("findPreferenceByType: "+sql);
        logger.info("For NetworkId "+networkid+" NodeId "+nodeid+" For CardType "+cardtype);
        int id  = jdbcTemplate.queryForObject(sql,(int.class),networkid, nodeid, cardtype);			
		return id; 
    }
	
	public List <CardPreference> findPreference(int networkid) throws SQLException {
        String sql = "SELECT * FROM CardPreference where NetworkId = ? ";	
        logger.info("findPreferenceByFeature: "+sql);
        logger.info("For NetworkId "+networkid);
        List <CardPreference> id;        
        try
        {
        	return  id  = jdbcTemplate.query(sql,new BeanPropertyRowMapper(CardPreference.class),networkid);	       
        }
        catch(EmptyResultDataAccessException e) {
        	 logger.warn("findPreferenceByFeature"+e);
    		return null;
    	}
	}
	
	public List <CardPreference> findPreferenceByNode(int networkid, int nodeid) throws SQLException {
        String sql = "SELECT * FROM CardPreference where NetworkId = ? and NodeId = ? ";	
        
        List <CardPreference> id=null;        
        try
        {
        	logger.info("findPreferenceByNode: "+sql);
            logger.info("For NetworkId "+networkid+" NodeId "+nodeid);
        	return  id  = jdbcTemplate.query(sql,new BeanPropertyRowMapper(CardPreference.class),networkid, nodeid);	       
        }
        catch(EmptyResultDataAccessException e) {
        	logger.info("CardPreferenceDao.findPreferenceByNode() No data found ...");
        	 logger.error("findPreferenceByNode"+e);
    		return null;
    	}
	}
	
	
	 
 public List<CardPreference> findAll(){
		String sql = "SELECT * FROM CardPreference order by NodeId,CardFeature";
		logger.info("findAll: "+sql);
		List<CardPreference> infolist  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(CardPreference.class));
		return infolist;
	}
 
 
 public void insert(final CardPreference data) throws SQLException {         
 String sql = "INSERT into CardPreference(NetworkId, NodeId, CardFeature, CardType, EquipmentId, Preference) VALUES (?, ?, ?, ?, ?, ?)";
 logger.info("insert: "+sql+data.toString());
 jdbcTemplate.update(
         sql,
         new Object[] { data.getNetworkId(), data.getNodeId(),data.getCardFeature(), data.getCardType(), data.getEquipmentId(), data.getPreference()});
 }
 
 public int count()
 {		 
	  String sql = "select count(*) from CardPreference"; 
	  logger.info("count: "+sql);
	  return jdbcTemplate.queryForObject(sql, int.class);		  		        
 }
 
 public int count(int networkId)
 {		 
	  String sql = "select count(*) from CardPreference where Networkid=?"; 
	  logger.info("count: "+sql);
	  return jdbcTemplate.queryForObject(sql, int.class,networkId);		  		        
 }
 
public void deleteByEquipmentId(int networkid, int nodeid, int EquipmentId) {	        
        String sql = "delete from CardPreference where NetworkId = ? and NodeId = ? and EquipmentId = ?";
        logger.info("deleteByEquipmentId: "+sql); 
        logger.info("For NetworkId "+networkid+" NodeId "+nodeid+" For EquipmentId "+EquipmentId); 
        jdbcTemplate.update(sql, networkid, nodeid,EquipmentId);      
    }

public void deleteByCardFeature(int networkid, int nodeid, String cardfeature) {	        
    String sql = "delete from CardPreference where NetworkId = ? and NodeId = ? and CardFeature = ?";
    logger.info("deleteByCardFeature: "+sql); 
    logger.info("For NetworkId "+networkid+" NodeId "+nodeid+" For CardFeature "+cardfeature); 
    jdbcTemplate.update(sql, networkid, nodeid,cardfeature);      
}

public void deleteByNetworkId(int networkid) {	        
    String sql = "delete from CardPreference where NetworkId = ? ";
    logger.info("deleteByNetworkId: "+sql); 
    logger.info("For NetworkId "+networkid); 
    jdbcTemplate.update(sql, networkid);      
}

public void insertCardPreferenceDataInBrField(int networkid, int nodeid, int networkidBF ) throws SQLException {
	String NodeKey= ""+networkid+"_"+nodeid;
	String NodeKeyBF= ""+networkidBF+"_"+nodeid; 	
    String sql = "insert into CardPreference ( NetworkId, NodeId, CardFeature, CardType, EquipmentId, Preference ) select ?, NodeId, CardFeature, CardType, EquipmentId, Preference from CardPreference where NetworkId = ? and NodeId = ?";
    logger.info("insertCardPreferenceDataInBrField: "+sql); 	     
    jdbcTemplate.update( sql,networkidBF,networkid, nodeid);
	 	}    

}
