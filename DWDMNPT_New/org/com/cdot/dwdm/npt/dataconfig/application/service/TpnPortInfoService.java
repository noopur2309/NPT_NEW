package application.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.dao.TpnPortInfoDao;
import application.model.PtcLineProtInfo;
import application.model.TpnPortInfo;

@Service
public class TpnPortInfoService {
	@Autowired
	private TpnPortInfoDao dao;
	
	public List<TpnPortInfo> FindAll(int networkid)
	{
		return this.dao.findAll(networkid);
	}
	
	public List<TpnPortInfo> FindAll(int networkid, int nodeid)
	{
		
		return this.dao.findAll(networkid,nodeid);
	}
	public List<TpnPortInfo> FindAll(int networkid, int nodeid,int RackId,int Sbrack,int PortId,int Stream)
	{
		
		return this.dao.findAll(networkid,nodeid,RackId,Sbrack,PortId,Stream);
	}
	
	
	public List<TpnPortInfo> FindAll(int networkid, int nodeid, int stream)
	{
		
		return this.dao.findAll(networkid,nodeid,stream);
	}
	
	public void Insert(TpnPortInfo info) throws SQLException{
		this.dao.insert(info);
		
	}
	

	public void Update(int networkid, int nodeid, int rack, int sbrack, int card,int stream, String OperatorSpecific, int TxTCMMode,int TCMActValue,String TxTCMPriority) throws SQLException

	{
		this.dao.update(networkid, nodeid, rack, sbrack, card,stream, OperatorSpecific, TxTCMMode,TCMActValue,TxTCMPriority);
	}
	
	public void DeleteByNetworkId(int networkid)
	{
		this.dao.deleteByNetworkId(networkid);
	}
	public void DeleteClient(int networkid,int NodeId,int Rack, int Sbrack,int Card,int Stream,int PortId)
	{
		this.dao.deleteClient(networkid,NodeId, Rack, Sbrack,Card,Stream,PortId);
	}
	
	public List<TpnPortInfo> FindAll(int networkid, int nodeid, int rack, int sbrack, int card)
	{		
		return this.dao.findAll(networkid,nodeid,rack, sbrack, card);
	}
	
	public List<TpnPortInfo> FindAllCommonBrField(int networkidBf,int networkidGf,int nodeid)
	{
		return this.dao.findAllCommonBrField(networkidBf,networkidGf,nodeid);
	}
	
	public List<TpnPortInfo> FindAllAddedBrField(int networkidBf,int networkidGf,int nodeid)
	{
		return this.dao.findAllAddedBrField(networkidBf,networkidGf,nodeid);
	}
	
	public List<TpnPortInfo> FindAllModifiedBrField(int networkidBf,int networkidGf,int nodeid)
	{
		return this.dao.findAllModifiedBrField(networkidBf,networkidGf,nodeid);
	}
	
	public List<TpnPortInfo> FindAllDeletedBrField(int networkidBf,int networkidGf,int nodeid)
	{
		return this.dao.findAllDeletedBrField(networkidBf,networkidGf,nodeid);
	}
	 
	/**
	  * To find out the portList for particular circuit
	  * @date  6th Feb, 2018
	  * @author hp
	  * @param networkid
	  * @param nodeid
	  * @param rack
	  * @param sbrack
	  * @param card
	  * @param portId
	  * @param Stream
	  * @return
	  */
	public List<TpnPortInfo> FindAll(int networkid, int nodeid, int rack, int sbrack, int card, int portId, int Stream, int... typeOtnLsp)
	{		
		return this.dao.findAll(networkid,nodeid,rack, sbrack, card, portId, Stream, typeOtnLsp);
	}


	public List<TpnPortInfo> FindAll10GLine(int networkid, int nodeid, int rack, int sbrack, int card, int Stream)
	{		
		return this.dao.findAll10GLine(networkid,nodeid,rack, sbrack, card,Stream);
	}

}
