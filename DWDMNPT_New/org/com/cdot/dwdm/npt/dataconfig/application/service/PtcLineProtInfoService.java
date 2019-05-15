package application.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.dao.PatchCordDao;
import application.dao.PtcLineProtInfoDao;
import application.dao.TpnPortInfoDao;
import application.model.CardInfo;
import application.model.PatchCord;
import application.model.PtcLineProtInfo;
import application.model.TpnPortInfo;

@Service
public class PtcLineProtInfoService {
	@Autowired
	private PtcLineProtInfoDao dao;
	
	public List<PtcLineProtInfo> FindAll(int networkid)
	{
		return this.dao.findAll(networkid);
	}
	
	public List<PtcLineProtInfo> FindAll(int networkid, int nodeid)
	{
		return this.dao.findAll(networkid,nodeid);
	}
	
	public List<PtcLineProtInfo> FindAllCommonBrField(int networkidBf,int networkidGf,int nodeid)
	{
		return this.dao.findAllCommonBrField(networkidBf,networkidGf,nodeid);
	}
	
	public List<PtcLineProtInfo> FindAllAddedBrField(int networkidBf,int networkidGf,int nodeid)
	{
		return this.dao.findAllAddedBrField(networkidBf,networkidGf,nodeid);
	}
	
	public List<PtcLineProtInfo> FindAllModifiedBrField(int networkidBf,int networkidGf,int nodeid)
	{
		return this.dao.findAllModifiedBrField(networkidBf,networkidGf,nodeid);
	}
	
	public List<PtcLineProtInfo> FindAllDeletedBrField(int networkidBf,int networkidGf,int nodeid)
	{
		return this.dao.findAllDeletedBrField(networkidBf,networkidGf,nodeid);
	}
	
	public void Insert(PtcLineProtInfo info) throws SQLException{
		this.dao.insert(info);
		
	}
	
	public void DeleteByNetworkId(int networkid)
	{
		this.dao.deleteByNetworkId(networkid);
	}

}
