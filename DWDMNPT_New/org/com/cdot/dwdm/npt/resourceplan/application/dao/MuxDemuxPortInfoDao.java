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
import application.model.MuxDemuxPortInfo;
import application.model.PortInfo;
import application.model.YCablePortInfo;
import application.MainMap;
import application.constants.ResourcePlanConstants;
import application.model.CardInfo;

@Component
public class MuxDemuxPortInfoDao{
	@Autowired
    private JdbcTemplate jdbcTemplate;
	public static Logger logger = Logger.getLogger(ResourcePlanConstants.DatabasePrintStr);
	
	//Returns all line ports available on node
	public CardInfo getMpnCardForLinePort(int networkid, int nodeid, int rack, int subrack, int cardId){
		String NodeKey= ""+networkid+"_"+nodeid;
		String tablename="Cards";		
		String sql = "SELECT * FROM "+tablename+" where NetworkId = ? and NodeId=? and Rack = ? and Sbrack = ? and Card = ?";
		CardInfo card  = (CardInfo) jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper(CardInfo.class),networkid,nodeid,rack,subrack,cardId);
		return card;
	}

	public void insert(MuxDemuxPortInfo portInfo) throws SQLException 
	{	     
		String sql = "INSERT into MuxDemuxPortInfo(NetworkId, NodeId, Rack, Sbrack, CardId, Wavelength, PortNum) VALUES (?, ?, ?, ?, ?, ?,?)";
		jdbcTemplate.update( sql, new Object[] { portInfo.getNetworkId(), portInfo.getNodeId(), portInfo.getRack(), portInfo.getSbrack(),portInfo.getCardId(), portInfo.getWavelength(), portInfo.getPortNum() });
	}

	public void deleteByNetworkId(int networkid) 
	{	       
		String sql = "delete from MuxDemuxPortInfo where NetworkId = ?";
		jdbcTemplate.update(sql, networkid);      
	}
	
	public List<MuxDemuxPortInfo> findCardPorts(int networkid, int nodeid,int rack, int subrack, int cardid){		
		String sql = "SELECT * FROM MuxDemuxPortInfo where NetworkId = ? and NodeId = ? and Rack = ? and Sbrack= ? and CardId = ? ; ";
		List<MuxDemuxPortInfo> list  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(MuxDemuxPortInfo.class), networkid, nodeid, rack, subrack, cardid);
		return list;
	}
	
	public List<MuxDemuxPortInfo> findAll(int networkid, int nodeid, int cardid){		
		String sql = "SELECT * FROM MuxDemuxPortInfo where NetworkId = ? and NodeId = ? and CardId = ? ; ";
		List<MuxDemuxPortInfo> list  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(MuxDemuxPortInfo.class), networkid, nodeid, cardid);
		return list;
	}	

}

