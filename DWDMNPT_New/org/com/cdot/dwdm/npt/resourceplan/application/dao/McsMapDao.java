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
import application.model.McsMap;
import application.model.WssMap;
import application.MainMap;
import application.constants.ResourcePlanConstants;
import application.model.CardInfo;

@Component
public class McsMapDao{
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

	public List <McsMap> find(int networkid, int nodeid,boolean... optional){
		String sql=null;
		if(optional.length!=0)
		{
			sql = "select distinct NetworkId,NodeId,Rack,Sbrack,Card,McsCommonPort, EdfaLoc from McsMap  where NetworkId = ? and NodeId = ? "; 
		}else
		{
			sql = "SELECT * FROM McsMap where NetworkId = ? and NodeId = ?  "; 
		}

		List <McsMap> info;			
		try
		{
			info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(McsMap.class), networkid, nodeid);		       
		}
		catch(EmptyResultDataAccessException e) {
			return null;
		}	        
		return info;
	}

	public List <Integer> findMcsIds(int networkid, int nodeid){
		String sql = "SELECT McsId FROM McsMap where NetworkId = ? and NodeId = ?  ";
		List <Integer> info;			
		try
		{
			info  = jdbcTemplate.queryForList(sql, Integer.class, networkid, nodeid);		       
		}
		catch(EmptyResultDataAccessException e) {
			return null;
		}	        
		return info;
	}

	public List <Integer> findMcsIds(int networkid, int nodeid,boolean unique){
		String sql = "SELECT distinct McsId FROM McsMap where NetworkId = ? and NodeId = ?  ";
		List <Integer> info;			
		try
		{
			info  = jdbcTemplate.queryForList(sql, Integer.class, networkid, nodeid);		       
		}
		catch(EmptyResultDataAccessException e) {
			return null;
		}	        
		return info;
	}

	public List <McsMap> find(int networkid, int nodeid, int mcsid, String commonport){
		String sql = "SELECT * FROM McsMap where NetworkId = ? and NodeId = ? and McsId = ? and McsCommonPort = ? ";
		List <McsMap> info;			
		try
		{
			info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(McsMap.class), networkid, nodeid, mcsid, commonport);		       
		}
		catch(EmptyResultDataAccessException e) {
			return null;
		}	        
		return info;
	}

	public List <McsMap> findByCommonPort(int networkid, int nodeid, String commonport){
		String sql = "SELECT * FROM McsMap where NetworkId = ? and NodeId = ? and McsCommonPort = ? ";
		List <McsMap> info;			
		try
		{
			info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(McsMap.class), networkid, nodeid, commonport);		       
		}
		catch(EmptyResultDataAccessException e) {
			return null;
		}	        
		return info;
	}

	public List<McsMap> find(int networkid, int nodeid, String TpnLoc){
		String sql = "SELECT * FROM McsMap where NetworkId = ? and NodeId = ? and TpnLoc = ?  ";
		List<McsMap> info;			
		try
		{
			info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(McsMap.class), networkid, nodeid, TpnLoc);		       
		}
		catch(EmptyResultDataAccessException e) {
			return null;
		}	        
		return info;
	}

	public void insert(final McsMap map) throws SQLException 
	{	     
		String sql = "INSERT into McsMap(NetworkId, NodeId, Rack, Sbrack, Card, McsId, McsSwitchPort, McsCommonPort, TpnLoc, TpnLinePortNo, EdfaLoc, EdfaId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update( sql, new Object[] { map.getNetworkId(), map.getNodeId(), map.getRack(), map.getSbrack(), map.getCard(), map.getMcsId(), map.getMcsSwitchPort(), map.getMcsCommonPort(), map.getTpnLoc(), map.getTpnLinePortNo(), map.getEdfaLoc(), map.getEdfaId() });
	}

	public Object getMaxSwitchPortId(int networkid, int nodeid, int McsId)
	{		
		logger.info("getMaxSwitchPortId");
		logger.info("For Node: "+nodeid+" McsId: "+McsId);
		String sql = "select MAX(McsSwitchPort)+1 from McsMap where NetworkId = ? and NodeId = ? and McsId = ? ";
		try
		{
			return jdbcTemplate.queryForObject(sql, Integer.class, networkid, nodeid, McsId);
		}		
		catch(EmptyResultDataAccessException e) {
			return  null;
		}	
	}

	public Object getMaxSwitchPortId(int networkid, int nodeid,CardInfo card)
	{		
		logger.info("getMaxSwitchPortId");
		logger.info("For Node: "+nodeid+" Card: "+ card.getRack()+"-"+card.getSbrack()+"-"+card.getCard());
		String sql = "select MAX(McsSwitchPort)+1 from McsMap where NetworkId = ? and NodeId = ? and Rack=? and Sbrack=? and Card=? ";
		try
		{
			return jdbcTemplate.queryForObject(sql, Integer.class, networkid, nodeid, card.getRack(),card.getSbrack(),card.getCard());
		}		
		catch(EmptyResultDataAccessException e) {
			return  null;
		}	
	}

	public Object getFirstFreeSwitchPortId(int networkid, int nodeid, int McsId)
	{		
		logger.info("getFirstFreeSwitchPortId");
		logger.info("For Node: "+nodeid+" McsId: "+McsId);
		String sql = "select McsSwitchPort+1 from McsMap M1 where NetworkId=? and NodeId=? and McsId=? and McsSwitchPort+1 NOT IN ( "
				+ "select McsSwitchPort from McsMap M2 where M2.McsSwitchPort=M1.McsSwitchPort+1 and NetworkId=? and NodeId=? and McsId=? ) ORDER BY McsSwitchPort ASC LIMIT 1";

		try {
			return jdbcTemplate.queryForObject(sql, Integer.class, networkid, nodeid, McsId,networkid, nodeid, McsId);
		} catch (EmptyResultDataAccessException e) {
			// TODO: handle exception
			return 1;
		}

	}



	public List<String> findCommonPortsUsed(int networkid, int nodeid, int mcsid)
	{		
		logger.info("Count Link");
		String sql = " select distinct McsCommonPort from McsMap where NetworkId = ? and NodeId = ? and McsId=?;"; 
		return jdbcTemplate.queryForList(sql, String.class,networkid, nodeid, mcsid );		  		        
	}
	
	public List<String> findCommonPortsUsedLowerDegreeDir(int networkid, int nodeid, int mcsid)
	{		
		logger.info("Count Link");
		String sql = " select distinct McsCommonPort from McsMap where NetworkId = ? and NodeId = ? and McsId=? and (McsCommonPort='east' or McsCommonPort='west' or McsCommonPort='north' or McsCommonPort='south');"; 
		return jdbcTemplate.queryForList(sql, String.class,networkid, nodeid, mcsid );		  		        
	}
	
	public List<String> findCommonPortsUsedUpperDegreeDir(int networkid, int nodeid, int mcsid)
	{		
		logger.info("Count Link");
		String sql = " select distinct McsCommonPort from McsMap where NetworkId = ? and NodeId = ? and McsId=? and (McsCommonPort='ne' or McsCommonPort='nw' or McsCommonPort='se' or McsCommonPort='sw');"; 
		return jdbcTemplate.queryForList(sql, String.class,networkid, nodeid, mcsid );		  		        
	}

	public void updateEdfaLoc(int networkid, int nodeid, int mcsid, String edfaloc, int edfaid) throws SQLException {

		String sql = "Update McsMap set EdfaLoc = ?, EdfaId = ? where NetworkId = ? and NodeId =? and McsId = ? ";
		logger.info("updateNode: "+sql); 	     
		jdbcTemplate.update(
				sql,
				new Object[] { edfaloc , edfaid, networkid, nodeid, mcsid});
	}


	public void updateEdfaLoc(int networkid, int nodeid, int mcsid, int switchport, String edfaloc, int edfaid) throws SQLException {

		String sql = "Update McsMap set EdfaLoc = ?, EdfaId = ? where NetworkId = ? and NodeId =? and McsId = ? and McsSwitchPort = ? ";
		logger.info("updateNode: EdfaLoc:"+edfaid+" EdfaId:"+edfaid+" NetworkId:"+networkid+" NodeId:"+nodeid+" McsId:"+mcsid+" McsSwitchPort:"+switchport); 	     
		jdbcTemplate.update(
				sql,
				new Object[] { edfaloc , edfaid, networkid, nodeid, mcsid, switchport});
	}



	public Object getEdfaDirId(int networkid, int nodeid, String EdfaLoc)
	{		
		logger.info("getEdfaDirId");		
		String sql = "SELECT concat(McsId,EdfaId) as EdfaDirId from McsMap where NetworkId = ? and NodeId = ? and EdfaLoc = ? group by EdfaDirId ;";
		try
		{
			return jdbcTemplate.queryForObject(sql, Integer.class, networkid, nodeid, EdfaLoc);
		}		
		catch(EmptyResultDataAccessException e) {
			return  null;
		}	
	}

	public int count()
	{		
		logger.info("Count Link");
		String sql = "select count(*) from Link"; 
		return jdbcTemplate.queryForObject(sql, int.class);		  		        
	}

	/*Function return SrcIp , DestIp, SrcNodeDirection of the links originating from a particular Node
	 * */


	public List<LinkWavelengthMap> linkWavelength(int networkid)
	{
		String sql = "SELECT NetworkId, SrcNode as NodeId , SrcNodeDirection as Direction , LinkId, SpanLoss from Link union select NetworkId, DestNode as NodeId , DestNodeDirection as Direction, LinkId , SpanLoss from Link  where NetworkId = ? order by NodeId ";
		List<LinkWavelengthMap> list  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(LinkWavelengthMap.class),networkid);
		return list;
	}	

	public void deleteByNetworkId(int networkid) {	       
		String sql = "delete from McsMap where NetworkId = ?";
		jdbcTemplate.update(sql, networkid);      
	}

	public void deleteMcsPort(int networkid,int nodeid,int mcsid,int rack, int sbrack,int card,int switchport) {	       
		String sql = "delete from McsMap where NetworkId = ? and NodeId = ? and McsId = ? and Rack = ? and Sbrack = ? and Card = ? and McsSwitchPort = ?";
		jdbcTemplate.update(sql, networkid, nodeid,mcsid,rack, sbrack,card,switchport);      
	}

	/**
	 * Finds the max McsId being used. McsSwitchPort is kept as one as McsId is same for the McsSwitchPort 
	 * @param networkid
	 * @param nodeid
	 * @return
	 */
	public McsMap findMcsWithMaxMcsId(int networkid, int nodeid){
		String sql = "SELECT NetworkId, NodeId, Rack, Sbrack, Card, McsId FROM McsMap where NetworkId = ? and NodeId = ? and McsId = (SELECT MAX(McsId) from McsMap where NetworkId = ? and NodeId = ?) group by NetworkId, NodeId, Rack, Sbrack, Card, McsId ;";
		McsMap info;			
		try
		{
			info  = (McsMap) jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper(McsMap.class), networkid, nodeid, networkid, nodeid);		       
		}
		catch(EmptyResultDataAccessException e) {
			return null;
		}	        
		return info;
	}

	public Object checkIdEdfaLocExists(int networkid, int nodeid, int mcsid, int switchport)
	{		
		logger.info("checkIdEdfaLocEsists");		
		String sql = "SELECT * from McsMap where NetworkId = ? and NodeId = ? and McsId = ? and McsSwitchPort = ? and (EdfaLoc = NULL) ";
		try
		{
			return jdbcTemplate.queryForObject(sql, Integer.class, networkid, nodeid, mcsid, switchport);
		}		
		catch(EmptyResultDataAccessException e) {
			return  null;
		}	
	}

	public McsMap checkIdEdfaLocExists(int networkid, int nodeid, int mcsid)
	{		
		logger.info("checkIdEdfaLocEsists");		
		String sql = "SELECT * from McsMap where NetworkId = ? and NodeId = ? and McsId = ? and (EdfaLoc IS NULL) ";
		try
		{
			return (McsMap) jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper(McsMap.class), networkid, nodeid, mcsid);
		}		
		catch(EmptyResultDataAccessException e) {
			return  null;
		}	
	}

	public List<String> edfaCardsUsed(int networkid, int nodeid)
	{		
		logger.info("checkIdEdfaLocEsists");		
		String sql = "SELECT distinct EdfaLoc from McsMap where NetworkId = ? and NodeId = ? ";
		return jdbcTemplate.queryForList(sql, String.class, networkid, nodeid);			
	}

	public List <Integer> findWssSets(int networkid, int nodeid){
		String sql = "SELECT distinct McsId FROM McsMap where NetworkId = ? and NodeId = ?  ";
		List <Integer> info;			
		try
		{
			info  = jdbcTemplate.queryForList(sql, Integer.class, networkid, nodeid);		       
		}
		catch(EmptyResultDataAccessException e) {
			return null;
		}	        
		return info;
	}


	public void insertMcsMapDataInBrField(int networkid, int networkidBF ) throws SQLException {			

		String sql = "insert into McsMap ( NetworkId, NodeId, Rack, Sbrack, Card, McsId, McsSwitchPort, McsCommonPort, TpnLoc, TpnLinePortNo, EdfaLoc, EdfaId ) select ?, NodeId, Rack, Sbrack, Card, McsId, McsSwitchPort, McsCommonPort, TpnLoc, TpnLinePortNo, EdfaLoc, EdfaId from McsMap where NetworkId = ? ";
		logger.info("insertMcsMapDataInBrField: "+sql); 	     
		jdbcTemplate.update( sql,networkidBF,networkid);
	} 

	public List <McsMap> findWss(int networkid, int nodeid, int WssSetNo, String WssCommonPort){
		String sql = "SELECT * FROM McsMap where NetworkId = ? and NodeId = ? and McsId = ? and McsCommonPort = ? ";
		List <McsMap> info;			
		try
		{
			info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(McsMap.class), networkid, nodeid, WssSetNo, WssCommonPort);		       
		}
		catch(EmptyResultDataAccessException e) {
			return null;
		}	        
		return info;
	}

	
	public List <McsMap> findDistinct(int networkid, int nodeid){
		String sql = "SELECT  distinct Rack, Sbrack, Card, McsId  FROM McsMap where NetworkId = ? and NodeId = ?  ";
		List <McsMap> info;			
		try
        {
			info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(McsMap.class), networkid, nodeid);		       
        }
        catch(EmptyResultDataAccessException e) {
    		return null;
    	}	        
		return info;
	}
	
	public List<McsMap> findAllAddDropData(int networkid, int nodeid, int rackId, int sbrackId, int cardId){
		String sql = "SELECT * FROM McsMap  where NetworkId = ? and NodeId = ? and Rack = ? and Sbrack = ? and  Card = ?";
		MainMap.logger.info("findAllAddDropData: "+sql);
		List<McsMap> info  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(McsMap .class),networkid,nodeid,rackId,sbrackId,cardId);
		return info;
	}
	
	
	public List <McsMap> findByCommonPortForDirection(int networkid, int nodeid, String edfaLoc, String dirEdfa1, String dirEdfa2){
		
		String sql = "select * from McsMap   where NetworkId= ?  and NodeId = ? and EdfaLoc=?  and (McsCommonPort=? or McsCommonPort=?) ";
		
		MainMap.logger.info("findByCommonPortForDirection: "+sql);
		
		List<McsMap> info;
		
		try {
			
			info = jdbcTemplate.query(sql, new BeanPropertyRowMapper(McsMap .class),networkid,nodeid,edfaLoc,dirEdfa1,dirEdfa2);	
		}
		catch(EmptyResultDataAccessException e) {
			return null;
		}	
		
		
		
		return info;
	}
}