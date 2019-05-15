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
import application.dao.LinkWavelengthDao;
import application.dao.McsMapDao;
import application.model.Circuit;
import application.model.LinkWavelength;
import application.model.LinkWavelengthMap;
import application.model.McsMap;
import application.model.WssMap;
import application.model.CardInfo;

@Service
public class McsMapService{

	@Autowired
	private McsMapDao dao;
	
	public List <McsMap> Find(int networkid, int nodeid,boolean... optional)
	{
		return this.dao.find(networkid, nodeid,optional);
	}
	
	public List <Integer> FindMcsIds(int networkid, int nodeid)
	{
		return this.dao.findMcsIds(networkid, nodeid);
	}
	
	public List <Integer> FindMcsIds(int networkid, int nodeid,boolean unique)
	{
		return this.dao.findMcsIds(networkid, nodeid,unique);
	}
		
	public List<LinkWavelengthMap> LinkWavelength(int networkid)
	{
		return this.dao.linkWavelength(networkid);
	}
	
	public List <McsMap> FindMcs(int networkid, int nodeid, int mcsid, String commonport)
	{
		return this.dao.find(networkid, nodeid, mcsid, commonport);
	}
	
	public void Insert(McsMap info) throws SQLException{
		this.dao.insert(info);		
	}
	
	public int Count()
	{
		return this.dao.count();
	}
	
	public List<String> FindCommonPortUsed(int networkid,int nodeid,int mcsid)
	{
		return this.dao.findCommonPortsUsed(networkid, nodeid, mcsid);
	}
	
	public List<String> FindCommonPortUsedLowerDegreeDir(int networkid,int nodeid,int mcsid)
	{
		return this.dao.findCommonPortsUsedLowerDegreeDir(networkid, nodeid, mcsid);
	}
	
	public List<String> FindCommonPortUsedUpperDegreeDir(int networkid,int nodeid,int mcsid)
	{
		return this.dao.findCommonPortsUsedUpperDegreeDir(networkid, nodeid, mcsid);
	}
	
	public List <McsMap> FindByCommonPort(int networkid,int nodeid,String commonport)
	{
		return this.dao.findByCommonPort(networkid, nodeid, commonport);
	}
	
	public void UpdateEdfaLoc(int networkid, int nodeid, int mcsid, String edfaloc, int edfaid) throws SQLException
	{
		this.dao.updateEdfaLoc(networkid, nodeid, mcsid, edfaloc, edfaid);
	}
	
	public void UpdateEdfaLoc(int networkid, int nodeid, int mcsid, int switchport, String edfaloc, int edfaid) throws SQLException
	{
		this.dao.updateEdfaLoc(networkid, nodeid, mcsid, switchport, edfaloc, edfaid);
	}
	
	public Object GetMaxSwitchPortId(int networkid,int nodeid,int McsId)
	{
		return this.dao.getMaxSwitchPortId(networkid, nodeid, McsId);
	}
	
	public Object GetMaxSwitchPortId(int networkid,int nodeid,CardInfo card)
	{
		return this.dao.getMaxSwitchPortId(networkid, nodeid,card);
	}
	
	public Object GetFirstFreeSwitchPortId(int networkid,int nodeid,int McsId)
	{
		return this.dao.getFirstFreeSwitchPortId(networkid, nodeid, McsId);
	}
	
	public Object GetEdfaDirId(int networkid,int nodeid, String EdfaLoc)
	{
		return this.dao.getEdfaDirId(networkid, nodeid, EdfaLoc);
	}
	
	public void DeleteByNetworkId(int networkid)
	{
		this.dao.deleteByNetworkId(networkid);
	}
	
	public void DeleteMcsPort(int networkid,int nodeid,int mcsid,int rack, int sbrack,int card,int switchport)
	{
		this.dao.deleteMcsPort(networkid, nodeid, mcsid, rack, sbrack, card, switchport);
	}
	
	public McsMap FindMcsWithMaxMcsId(int networkid,int nodeid)
	{
		return this.dao.findMcsWithMaxMcsId(networkid, nodeid);
	}
	
	public List<McsMap> Find(int networkid,int nodeid, String TpnLoc)
	{
		return this.dao.find(networkid, nodeid, TpnLoc);
	}
	
	public Object CheckIdEdfaLocExists(int networkid,int nodeid, int mcsid, int switchport)
	{
		return this.dao.checkIdEdfaLocExists(networkid, nodeid, mcsid, switchport);
	}
	
	public McsMap CheckIdEdfaLocExists(int networkid,int nodeid, int mcsid)
	{
		return this.dao.checkIdEdfaLocExists(networkid, nodeid, mcsid);
	}
	
	public void insertMcsMapDataInBrField(int networkid,int networkidBF) throws SQLException
	{
		this.dao.insertMcsMapDataInBrField(networkid, networkidBF);
	}
	
	public List<String> EdfaCardsUsed(int networkid,int nodeid)
	{
		return this.dao.edfaCardsUsed(networkid, nodeid);
	}
	
	public List <Integer> FindWssSets(int networkid, int nodeid)
	{
		return this.dao.findWssSets(networkid, nodeid);
	}
	
	public List <McsMap> FindWss(int networkid, int nodeid, int WssSetNo, String WssCommonPort)
	{
		return this.dao.findWss(networkid, nodeid,WssSetNo,WssCommonPort);
	}
	
	public List<McsMap> FindDistinct(int networkid, int nodeid)
	{
		return this.dao.findDistinct(networkid, nodeid);
	}
	
	public List<McsMap> FindAllAddDropData(int networkid, int nodeid, int rackId, int sbrackId, int cardId)
	{
		return this.dao.findAllAddDropData(networkid, nodeid, rackId, sbrackId, cardId);
	}
	
	public List <McsMap> FindByCommonPortForDirection(int networkid, int nodeid, String edfaLoc, String dirEdfa1, String dirEdfa2)
	{
		return this.dao.findByCommonPortForDirection(networkid, nodeid, edfaLoc, dirEdfa1, dirEdfa2);
	}
	
	
	

}