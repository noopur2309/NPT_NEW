/**
 * 
 */
/**
 * @author sunaina
 *
 */
package application.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.dao.CardInfoDao;
import application.dao.PortInfoDao;
import application.dao.YCablePortInfoDao;
import application.model.Circuit;
import application.model.PortInfo;
import application.model.YCablePortInfo;
import application.model.CardInfo;

@Service
public class YCablePortInfoService{

	@Autowired
	private YCablePortInfoDao dao;
	
	public int FindUsedPortCount(int networkid, int nodeid, int rack, int sbrack, int card)
	{
		return this.dao.findUsedPortCount(networkid, nodeid, rack, sbrack, card);
	}
	
	public void InsertYCablePortInfoInBrField(int networkid,int networkidBF) throws SQLException
	{
		this.dao.insertYCablePortInfoInBrField(networkid, networkidBF);
	}
	
	public void DeleteYCablePort(int networkid, int nodeid, String mpnLoc,int mpnPort)
	{
		this.dao.deleteYCablePort(networkid, nodeid, mpnLoc, mpnPort);
	}
	
	public int Count(int networkId)
	{
		return this.dao.count(networkId);
	}
	public void CopyYCablePortInfoInBrField(int networkidGrField, int networkidBrField ) throws SQLException
	{
		this.dao.copyYCablePortInfoInBrField( networkidGrField,  networkidBrField );
	}
	
	public List<CardInfo> FindAll()
	{
		return this.dao.findAll();
	}	
	
	public List<YCablePortInfo> FindAll(int networkid, int nodeid, int cardid)
	{
		return this.dao.findAll(networkid, nodeid, cardid);
	}
	
	public List<YCablePortInfo> FindAll(int networkid, int nodeid)
	{
		return this.dao.findAll(networkid, nodeid);
	}
	
	public List <PortInfo> FindPortInfo(int networkid, int nodeid, int rack, int sbrack, int cardid)
	{
		return this.dao.findPortInfo(networkid, nodeid, rack, sbrack, cardid);
	}
	
	public PortInfo FindPortInfo(int networkid, int nodeid, int rack, int sbrack, int card, int circuitid)
	{
		return this.dao.findPortInfo(networkid, nodeid, rack, sbrack, card, circuitid);
	}
	
	public PortInfo FindDcmModuleByDir(int networkid, int nodeid, String dir)
	{
		return this.dao.findDcmModuleByDir(networkid, nodeid, dir);
	}
	
	public PortInfo FindUnassignedDcmModule(int networkid, int nodeid)
	{
		return this.dao.findUnassignedDcmModule(networkid, nodeid);
	}
	
	public void UpdateDcmModule(int networkid, int nodeid, int rack , int sbrack, int card, int port , String dir)
	{
		this.dao.updateDcmModule(networkid, nodeid, rack, sbrack, card, port, dir);
	}
	
	
	public CardInfo FindWss(int networkid, int nodeid, String dir)
	{
		return this.dao.findWss(networkid, nodeid, dir);
	}
	
	public int CountMpn(int networkid, int nodeid, String dir)
	{
		return this.dao.countMpn(networkid, nodeid, dir);
	}
	
	
	public List<CardInfo> FindCardInfoByCardType(int networkid, int nodeid, String cardtype)
	{
		return this.dao.findCardInfoByCardType(networkid, nodeid, cardtype);
	}
	
	
	
	public List<CardInfo> findMpns(int networkid, int nodeid)
	{
		return this.dao.findMpns(networkid, nodeid);
	}
		

	public void Insert(YCablePortInfo info) throws SQLException{
		this.dao.insert(info);
		
	}
	
	public void DeleteCard(int networkid, int nodeid, int rack, int sbrack, int card)
	{
		this.dao.deleteCard(networkid, nodeid, rack, sbrack, card);
	}
	
	public void DeletePort(int networkid, int nodeid, int rack, int sbrack, int card, int portid)
	{
		this.dao.deletePort(networkid, nodeid, rack, sbrack, card, portid);
	}
	
	public int CountCardByType(int networkid,int nodeid, String cardtype)
	{
		return this.dao.countCardByType(networkid,nodeid,cardtype);
	}
	
	public List<Map<String,Object>> CountPortByTypeNEId(int networkid,int nodeid)
	{
		return this.dao.countPortByTypeNEId(networkid, nodeid);
	}	
	
	public void DeleteYCablePortInfo(int networkid)
	{
		this.dao.deleteYCablePortInfo(networkid);
	}
	public void Deleteycableport(int networkid, int nodeid,  int portid)
	{
		this.dao.deleteycableport(networkid,nodeid,portid);
		
		}
}