package application.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.dao.PatchCordDao;
import application.dao.PtcClientProtInfoDao;
import application.dao.TpnPortInfoDao;
import application.model.CardInfo;
import application.model.OcmConfig;
import application.model.PatchCord;
import application.model.PtcClientProtInfo;
import application.model.TpnPortInfo;

@Service
public class PtcClientProtInfoService {
	@Autowired
	private PtcClientProtInfoDao dao;
	
	public List<PtcClientProtInfo> FindAll(int networkid)
	{
		return this.dao.findAll(networkid);
	}
	
	public void UpdateDb(HashMap<String,Object> Difference, int networkId, int nodeid)
	{
		this.dao.updatedb(Difference, networkId, nodeid);
	}
	

	public application.model.PtcClientProtInfo FindClient( int networkId, int nodeid,int ActMpnRackId, int ActMpnSubrackId,int ActMpnCardid,int ActMpnPort)
	{
		return this.dao.findClient(networkId, nodeid, ActMpnRackId, ActMpnSubrackId, ActMpnCardid, ActMpnPort);
	}
	
		
	public List<PtcClientProtInfo> FindAll(int networkid, int nodeid)
	{
		return this.dao.findAll(networkid,nodeid);
	}
	
	public List<PtcClientProtInfo> FindAllCommonBrField(int networkidBf,int networkidGf,int nodeid)
	{
		return this.dao.findAllCommonBrField(networkidBf,networkidGf,nodeid);
	}
	
	public List<PtcClientProtInfo> FindAllAddedBrField(int networkidBf,int networkidGf,int nodeid)
	{
		return this.dao.findAllAddedBrField(networkidBf,networkidGf,nodeid);
	}
	
	public List<PtcClientProtInfo> FindAllModifiedBrField(int networkidBf,int networkidGf,int nodeid)
	{
		return this.dao.findAllModifiedBrField(networkidBf,networkidGf,nodeid);
	}
	
	public List<PtcClientProtInfo> FindAllDeletedBrField(int networkidBf,int networkidGf,int nodeid)
	{
		return this.dao.findAllDeletedBrField(networkidBf,networkidGf,nodeid);
	}
	
	public void Insert(PtcClientProtInfo ptcClients) throws SQLException{
		this.dao.insert(ptcClients);
		
	}
	
	
	public void DeleteByNetworkId(int networkid)
	{
		this.dao.deleteByNetworkId(networkid);
	}
	
	public void DeleteClient(int networkid,int nodeid,int ActMpnRackId, int ActMpnSubrackId,int ActMpnCardid,int ActMpnPort)
	{
		this.dao.deleteClient(networkid,nodeid,ActMpnRackId,ActMpnSubrackId,ActMpnCardid,ActMpnPort );
	}
	
	

}
