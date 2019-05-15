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
import application.model.LinkWavelengthInfo;
import application.model.LinkWavelengthMap;
import application.MainMap;
import application.constants.ResourcePlanConstants;
import application.model.CardInfo;

@Component
public class LinkWavelengthInfoDao{
	@Autowired
    private JdbcTemplate jdbcTemplate;
	public static Logger logger = Logger.getLogger(ResourcePlanConstants.DatabasePrintStr);
		 
	 
	 @SuppressWarnings({ "unchecked", "rawtypes" })
		public List<LinkWavelengthInfo> findAll(int networkid){
				String sql = "SELECT * FROM LinkWavelengthInfo where NetworkId = ?";
				List<LinkWavelengthInfo> links  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(LinkWavelengthInfo.class),networkid);
				return links;
			}
	 public List<LinkWavelengthInfo> findAllByLink(int networkid, int linkid){
			String sql = "SELECT * FROM LinkWavelengthInfo where NetworkId = ? and LinkId = ?";
			List<LinkWavelengthInfo> links  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(LinkWavelengthInfo.class),networkid,linkid);
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
	 
	 public void insert(final LinkWavelengthInfo link) throws SQLException 
	 {	     
	     String sql = "INSERT into LinkWavelengthInfo(NetworkId, LinkId, Wavelength, DemandId, LineRate, Traffic, Path, RoutePriority,Status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	     jdbcTemplate.update( sql, new Object[] { link.getNetworkId(), link.getLinkId(), link.getWavelength(), link.getDemandId(), link.getLineRate(), link.getTraffic(), link.getPath(),link.getRoutePriority(),link.getStatus() });
	 }
	 
	 public int count()
	 {		
		 logger.info("Count LinkWavelengthInfo");
		  String sql = "select count(*) from LinkWavelengthInfo"; 
		  return jdbcTemplate.queryForObject(sql, int.class);		  		        
	 }
	 
	 public LinkWavelengthInfo findPathForlinkWavelength(int networkid, int LinkId, int DemandId)
	 	{
		 	logger.info("findPathForlinkWavelength : nid, lid, did : "+ networkid + ", "+LinkId+ ","+ DemandId);
			String sql = "SELECT * from LinkWavelengthInfo  where NetworkId = ? and LinkId = ? and DemandId = ? ";
			LinkWavelengthInfo list  = (LinkWavelengthInfo) jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper(LinkWavelengthInfo.class),networkid,LinkId,DemandId);
			return list;
		}	
	 
	 public LinkWavelengthInfo findPathForlinkWavelength(int networkid, int LinkId, int DemandId, int wavelength)
	 	{
			String sql = "SELECT * from LinkWavelengthInfo  where NetworkId = ? and LinkId = ? and DemandId = ? and Wavelength = ?  ";
			LinkWavelengthInfo list  = (LinkWavelengthInfo) jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper(LinkWavelengthInfo.class),networkid,LinkId,DemandId,wavelength);
			return list;
		}	
	 
	 /*Function return SrcIp , DestIp, SrcNodeDirection of the links originating from a particular Node
	  * */
	 public List<Map<String, Object>> findSrcIpOfLink(int SrcNodeId) {
	        logger.info("findSrcIpOfLink");
	        String sql = "Select i.SrcIp , i.DestIp, l.SrcNodeDirection , l.linkid from IpSchemeLink i inner join Link l on i.LinkId=l.LinkId and l.SrcNode = ?;";
	        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,SrcNodeId);
	        return list;
	    }
	 
	 /*Function return SrcIp , DestIp, DestNodeDirection of the links terminating from a particular Node
	  * */
	 public List<Map<String, Object>> findDestIpOfLink(int DestNodeId) {
	        logger.info("findDestIpOfLink");
	        String sql = "Select i.SrcIp , i.DestIp,  l.DestNodeDirection , l.linkid from IpSchemeLink i inner join Link l on i.LinkId=l.LinkId and l.DestNode = ?;";
	        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,DestNodeId);
	        return list;
	    }
	 
	 public List<LinkWavelengthMap> linkWavelength(int networkid)
	 	{
			String sql = "SELECT NetworkId, SrcNode as NodeId , SrcNodeDirection as Direction , LinkId, SpanLoss from Link union select NetworkId, DestNode as NodeId , DestNodeDirection as Direction, LinkId , SpanLoss from Link  where NetworkId = ? order by NodeId ";
			List<LinkWavelengthMap> list  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(LinkWavelengthMap.class),networkid);
			return list;
		}	
	 
	 public void deleteByNetworkId(int networkid) {	       
	        String sql = "delete from LinkWavelengthInfo where NetworkId = ?";
	        jdbcTemplate.update(sql, networkid);      
	    }
	 /*Query to find out the maximum Wavelength at a node //Tanuj*/
	 public int maxwave()
	 {		
		logger.info("Finding maximum wavelength");
		  String sql = "SELECT MAX(MaxWave) AS MaxWave FROM (SELECT COUNT(Wavelength)AS MaxWave From LinkWavelengthInfo where  (RoutePriority = 1 or RoutePriority = 2) Group by LinkId) as tmp";
		  		
		  return jdbcTemplate.queryForObject(sql, int.class);		  		        
	 }
	 /*Query to find out the maximum Wavelength at a node //Tanuj*/
	 public int minwave()
	 {		
		logger.info("Finding minimum wavelength");
		  String sql = "SELECT MIN(MinWave) AS MinWave FROM (SELECT COUNT(Wavelength)AS MinWave From LinkWavelengthInfo where  (RoutePriority = 1 or RoutePriority = 2) Group by LinkId) as tmp";
		  		
		  return jdbcTemplate.queryForObject(sql, int.class);		  		        
	 }
	 /*Query to find out the total wavelengths in a link //Tanuj*/
	 public int findtotalwavelength(int networkid, int linkid)
	 {		
		logger.info("Finding Total wavelength on a link");
		  String sql = "SELECT COUNT(Wavelength) from LinkWavelengthInfo where LinkId = ? and NetworkId = ? and (RoutePriority = 1 or RoutePriority = 2)";
		  		
		  return jdbcTemplate.queryForObject(sql, int.class,linkid, networkid);		  		        
	 }
	 
	 public List<Map<String, Object>> findNodeWiseAllLambdaInfo(int networkid, int nodeid) {
	        logger.info("findDestIpOfLink");
	        String NodeKey= ""+networkid+"_"+nodeid;
//	        String tablename= "CardInfo_"+NodeKey;
	        String tablename= "Cards";
	        String sql = "SELECT l.SrcNodeDirection as Direction , l.linkid, lwf.Wavelength, lwf.LineRate, lwf.Traffic, "
	        		+ "lwf.Path, lwf.DemandId as DemandId, lwf.RoutePriority, lwf.Status, c.Rack, c.Sbrack, c.Card, c.Status from Link l "
	        		+ "left join LinkWavelengthInfo lwf on "
	        		+ "lwf.LinkId=l.LinkId and lwf.NetworkId=l.NetworkId "
	        		+ "left join (select * from Cards where NetworkId=? and NodeId=?) c on "
	        		+ "c.DemandId=lwf.DemandId and c.Status = lwf.Status "
	        		+ "where l.SrcNode = ? and l.NetworkId = ? and lwf.RoutePriority!=3 and lwf.RoutePriority!=4 "
	        		+ "UNION "
	        		+ "SELECT l.DestNodeDirection as Direction , l.linkid, lwf.Wavelength, lwf.LineRate, lwf.Traffic, "
	        		+ "lwf.Path, lwf.DemandId as DemandId, lwf.RoutePriority, lwf.Status, c.Rack, c.Sbrack, c.Card, c.Status from Link l "
	        		+ "left join LinkWavelengthInfo lwf on "
	        		+ "lwf.LinkId=l.LinkId and lwf.NetworkId=l.NetworkId "
	        		+ "left join (select * from Cards where NetworkId=? and NodeId=?) c on "
	        		+ "c.DemandId=lwf.DemandId and c.Status = lwf.Status "
	        		+ "where l.DestNode = ? and l.NetworkId = ? and lwf.RoutePriority!=3 and lwf.RoutePriority!=4 "
	        		+ "order by DemandId "; 
	        try {
	        	List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,networkid,nodeid,nodeid,networkid,networkid,nodeid,nodeid,networkid);
		        return list;
			} catch (Exception e) {
				// TODO: handle exception
				return null;
			}
	        
	    }
	public  List<Map<String, Object>> findDirectionWiseTraffic(int networkId, int nodeId,String dir) {
		String sql = "SELECT l.SrcNodeDirection as Direction,lwf.Traffic from link l "
				+ "left join LinkWavelengthInfo lwf on "
        		+ "lwf.LinkId=l.LinkId and lwf.NetworkId=l.NetworkId ";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,nodeId,networkId,nodeId,networkId);
        return list;
		
	}
	
	 
	

	 }
