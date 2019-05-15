/**
 * 
 */
/**
 * @author Avinash
 *
 */
package application.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.dao.CardInfoDao;
import application.dao.LinkWavelengthDao;
import application.dao.McsMapDao;
import application.dao.WssMapDao;
import application.model.Circuit;
import application.model.LinkWavelength;
import application.model.LinkWavelengthMap;
import application.model.McsMap;
import application.model.WssMap;
import application.model.WssMap;
import application.model.CardInfo;

@Service
public class WssMapService{

	@Autowired
	private WssMapDao dao;
	
	public List<WssMap> Find(int networkid, int nodeid)
	{
		return this.dao.find(networkid, nodeid);
	}
	
	public List<WssMap> Find(int networkid, int nodeid,String tpnLoc)
	{
		return this.dao.find(networkid, nodeid,tpnLoc);
	}
	
	public List<WssMap> FindBySetNo(int networkid, int nodeid,int wssSetNo)
	{
		return this.dao.getWssInfoBySetNo(networkid, nodeid,wssSetNo);
	}
	public List<WssMap> FindWssL1EdfaInfoBySetNo(int networkid, int nodeid,int wssSetNo)
	{
		return this.dao.getWssL1EdfaInfoBySetNo(networkid, nodeid,wssSetNo);
	}
	public List<WssMap> FindDistinct(int networkid, int nodeid)
	{
		return this.dao.findDistinct(networkid, nodeid);
	}
	
	
	public List <Integer> FindWssSets(int networkid, int nodeid)
	{
		return this.dao.findWssSets(networkid, nodeid);
	}
		
	public List<WssMap> FindWss(int networkid, int nodeid, int wssSetNo, String WssLevel1CommonPort)
	{
		return this.dao.find(networkid, nodeid, wssSetNo, WssLevel1CommonPort);
	}
	
	public void Insert(WssMap info) throws SQLException{
		this.dao.insert(info);		
	}
	
	public List<String> FindCommonPortUsed(int networkid,int nodeid,int wssSetNo)
	{
		return this.dao.findCommonPortsUsed(networkid, nodeid, wssSetNo);
	}
	
	public List<String> FindCommonPortUsedLowerDegreeDir(int networkid,int nodeid,int wssSetNo)
	{
		return this.dao.findCommonPortsUsedLowerDegreeDir(networkid, nodeid, wssSetNo);
	}
	
	public List<String> FindCommonPortUsedUpperDegreeDir(int networkid,int nodeid,int wssSetNo)
	{
		return this.dao.findCommonPortsUsedUpperDegreeDir(networkid, nodeid, wssSetNo);
	}
	
	public List <WssMap> FindByCommonPort(int networkid,int nodeid,String WssLevel1CommonPort)
	{
		return this.dao.findByCommonPort(networkid, nodeid, WssLevel1CommonPort);
	}
	
	public void UpdateEdfaLoc(int networkid, int nodeid, int wssSetNo, String edfaloc, int edfaid) throws SQLException
	{
		this.dao.updateEdfaLoc(networkid, nodeid, wssSetNo, edfaloc, edfaid);
	}
	
	public void UpdateEdfaLoc(int networkid, int nodeid, int wssSetNo, int switchport, String edfaloc, int edfaid) throws SQLException
	{
		this.dao.updateEdfaLoc(networkid, nodeid, wssSetNo, switchport, edfaloc, edfaid);
	}
	
	public Object GetMaxSwitchPortId(int networkid,int nodeid,int WssSetNo)
	{
		return this.dao.getMaxSwitchPortId(networkid, nodeid, WssSetNo);
	}
	
	public Object GetMaxSwitchPortId(int networkid,int nodeid,CardInfo wss)
	{
		return this.dao.getMaxSwitchPortId(networkid, nodeid, wss);
	}
	
	public Object GetEdfaDirId(int networkid,int nodeid, String EdfaLoc)
	{
		return this.dao.getEdfaDirId(networkid, nodeid, EdfaLoc);
	}
	
	public void DeleteByNetworkId(int networkid)
	{
		this.dao.deleteByNetworkId(networkid);
	}
	
	public void DeleteWssPort(int networkid,int nodeid,int wssSetNo,int rack, int sbrack,int card,int WssLevel2SwitchPort)
	{
		this.dao.deleteWssPort(networkid, nodeid, wssSetNo, rack, sbrack, card, WssLevel2SwitchPort);
	}
	
	public WssMap FindMcsWithMaxWssSetNo(int networkid,int nodeid)
	{
		return this.dao.findMcsWithMaxWssSetNo(networkid, nodeid);
	}
	
	public Object CheckIdEdfaLocExists(int networkid,int nodeid, int wssSetNo, int wssLevel2SwitchPort)
	{
		return this.dao.checkIdEdfaLocExists(networkid, nodeid, wssSetNo, wssLevel2SwitchPort);
	}
	
	public WssMap CheckIdEdfaLocExists(int networkid,int nodeid, int wssSetNo)
	{
		return this.dao.checkIdEdfaLocExists(networkid, nodeid, wssSetNo);
	}
	
	public void insertWssMapDataInBrField(int networkid,int networkidBF) throws SQLException
	{
		this.dao.insertWssMapDataInBrField(networkid, networkidBF);
	}
	
	public List<WssMap> FindAllAddDropData(int networkid, int nodeid, int rackId, int sbrackId, int cardId)
	{
		return this.dao.findAllAddDropData(networkid, nodeid, rackId, sbrackId, cardId);
	}
	 
	public List<String> EdfaCardsUsed(int networkid,int nodeid)
	{
		return this.dao.edfaCardsUsed(networkid, nodeid);
	}
	
	

}