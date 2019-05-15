package application.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.dao.McsPortMappingDao;
import application.dao.PatchCordDao;
import application.dao.McsPortMappingDao;
import application.model.AmplifierConfig;
import application.model.CardInfo;
import application.model.McsPortMapping;
import application.model.PatchCord;
import application.model.McsPortMapping;

@Service
public class McsPortMappingService {
	@Autowired
	private McsPortMappingDao dao;
	
	public List<McsPortMapping> FindAll()
	{
		return this.dao.findAll();
	}
	
	public List<McsPortMapping> FindAll(int networkid, int nodeid)
	{
		return this.dao.findAll(networkid,nodeid);
	}
	
	public List<McsPortMapping> FindAllCommonBrField(int networkidBf,int networkidGf,int nodeid)
	{
		return this.dao.findAllCommonBrField(networkidBf,networkidGf,nodeid);
	}
	
	public List<McsPortMapping> FindAllAddedBrField(int networkidBf,int networkidGf,int nodeid)
	{
		return this.dao.findAllAddedBrField(networkidBf,networkidGf,nodeid);
	}
	
	public List<McsPortMapping> FindAllModifiedBrField(int networkidBf,int networkidGf,int nodeid)
	{
		return this.dao.findAllModifiedBrField(networkidBf,networkidGf,nodeid);
	}
	
	public List<McsPortMapping> FindAllDeletedBrField(int networkidBf,int networkidGf,int nodeid)
	{
		return this.dao.findAllDeletedBrField(networkidBf,networkidGf,nodeid);
	}
	
	public List<McsPortMapping> FindDistinctData(int networkid, int nodeid)
	{
		return this.dao.findDistinctData(networkid,nodeid);
	}	
	
	public List<McsPortMapping> FindAllAddDeleteData(int networkid, int nodeid, int rackId, int sbrackId, int cardId)
	{
		return this.dao.findAllAddDeleteData(networkid, nodeid, rackId, sbrackId, cardId);
	}
	
	public void Insert(McsPortMapping info) throws SQLException{
		this.dao.insert(info);
		
	}
	
	public void DeleteByNetworkId(int networkid)
	{
		this.dao.deleteByNetworkId(networkid);
	}

}
