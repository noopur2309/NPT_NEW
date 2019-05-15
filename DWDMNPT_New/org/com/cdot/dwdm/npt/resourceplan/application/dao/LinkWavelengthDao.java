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

import application.model.Circuit;
import application.model.Link;
import application.model.LinkWavelength;
import application.model.LinkWavelengthMap;
import application.MainMap;
import application.constants.ResourcePlanConstants;
import application.model.CardInfo;

@Component
public class LinkWavelengthDao{
	@Autowired
    private JdbcTemplate jdbcTemplate;
	public static Logger logger = Logger.getLogger(ResourcePlanConstants.DatabasePrintStr);
	 public void queryLinks() {	        
	        String sql = "SELECT * FROM Link";
	        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
	        for (Map<String, Object> row : list) {	        	
	            logger.info(" NetworkId: "+row.get("NetworkId")+" LinkId: "+row.get("LinkId")+" SrcNodeId: "+ row.get("SrcNode")+" DestNodeId: "+row.get("DestNode"));
	        }
	    }
	 
	 public List<Map<String, Object>> findAllLinks() {
		logger.info("Find All Links");
	        String sql = "SELECT * FROM Link";
	        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
	        return list;
	    }
	 
	 @SuppressWarnings({ "unchecked", "rawtypes" })
		public List<Link> findAll(){
				String sql = "SELECT * FROM Link";
				List<Link> links  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Link.class));
				return links;
			}
	 
	 public LinkWavelength find(int networkid, int linkid){
			String sql = "SELECT * FROM LinkWavelength where NetworkId = ? and LinkId = ?";
			LinkWavelength info;			
			try
	        {
				info  = (LinkWavelength) jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper(LinkWavelength.class), networkid, linkid);		       
	        }
	        catch(EmptyResultDataAccessException e) {
	    		return null;
	    	}	        
			return info;
		}
	 
	 public void insert(final LinkWavelength link) throws SQLException 
	 {	     
	     String sql = "INSERT into LinkWavelength(NetworkId, LinkId, Wavelengths) VALUES (?, ?, ?)";
	     jdbcTemplate.update( sql, new Object[] { link.getNetworkId(), link.getLinkId(), link.getWavelengths() });
	 }
	 
	 public int count()
	 {		
		 logger.info("Count Link");
		  String sql = "select count(*) from Link"; 
		  return jdbcTemplate.queryForObject(sql, int.class);		  		        
	 }
	 
	 /*Function return SrcIp , DestIp, SrcNodeDirection of the links originating from a particular Node
	  * */
	 public List<Map<String, Object>> findSrcIpOfLink(int SrcNodeId) {
	        logger.info("findSrcIpOfLink");
	        String sql = "Select i.SrcIp , i.DestIp, l.SrcNodeDirection , l.linkid from IpSchemeLink i inner join Link l on i.LinkId=l.LinkId and i.NetworkId = l.NetworkId and l.SrcNode = ?;";
	        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,SrcNodeId);
	        return list;
	    }
	 
	 /*Function return SrcIp , DestIp, DestNodeDirection of the links terminating from a particular Node
	  * */
	 public List<Map<String, Object>> findDestIpOfLink(int DestNodeId) {
	        logger.info("findDestIpOfLink");
	        String sql = "Select i.SrcIp , i.DestIp,  l.DestNodeDirection , l.linkid from IpSchemeLink i inner join Link l on i.LinkId=l.LinkId and i.NetworkId = l.NetworkId and l.DestNode = ?;";
	        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,DestNodeId);
	        return list;
	    }
	 
	 public List<LinkWavelengthMap> linkWavelength(int networkid)
	 	{
			String sql = "SELECT NetworkId, SrcNode as NodeId , SrcNodeDirection as Direction , LinkId, SpanLoss from Link  where NetworkId=? union select NetworkId, DestNode as NodeId , DestNodeDirection as Direction, LinkId , SpanLoss from Link  where NetworkId = ? order by NodeId ";
			List<LinkWavelengthMap> list  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(LinkWavelengthMap.class),networkid,networkid);
			return list;
		}	
	 
	 public void deleteByNetworkId(int networkid) {	       
	        String sql = "delete from LinkWavelength where NetworkId = ?";
	        jdbcTemplate.update(sql, networkid);      
	    }

	 }
