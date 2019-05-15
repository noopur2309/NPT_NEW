/**
 * 
 */
/**
 * @author sunaina
 *
 */
package application.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.dao.CardInfoDao;
import application.dao.MuxDemuxPortInfoDao;
import application.dao.PortInfoDao;
import application.model.Circuit;
import application.model.McsMap;
import application.model.MuxDemuxPortInfo;
import application.model.PortInfo;
import application.model.TpnPortInfo;
import application.model.YCablePortInfo;
import application.model.CardInfo;

@Service
public class MuxDemuxPortInfoService{

	@Autowired
	private MuxDemuxPortInfoDao dao;
	
	public void Insert(MuxDemuxPortInfo info) throws SQLException{
		this.dao.insert(info);		
	}
	
	public void DeleteByNetworkId(int networkid)
	{
		this.dao.deleteByNetworkId(networkid);
	}
	
	public List<MuxDemuxPortInfo> FindAll(int networkid, int nodeid, int cardid)
	{
		return this.dao.findAll(networkid, nodeid, cardid);
	}
	
	public List<MuxDemuxPortInfo> FindCardPorts(int networkid, int nodeid, int rack, int subRack, int cardid)
	{
		return this.dao.findCardPorts(networkid, nodeid,rack, subRack, cardid);
	}
	
	public CardInfo GetMpnCardForLinePort(int networkid, int nodeid, int rack, int subrack, int cardId){
		return this.dao.getMpnCardForLinePort(networkid, nodeid, rack, subrack, cardId);
	}
	
	
		
}
