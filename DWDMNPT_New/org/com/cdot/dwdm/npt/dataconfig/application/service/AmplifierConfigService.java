package application.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.dao.AmplifierConfigDao;
import application.dao.PatchCordDao;
import application.dao.TpnPortInfoDao;
import application.model.AmplifierConfig;
import application.model.CardInfo;
import application.model.PatchCord;
import application.model.AmplifierConfig;
import application.model.TpnPortInfo;

@Service
public class AmplifierConfigService {
	@Autowired
	private AmplifierConfigDao dao;
	
	public List<AmplifierConfig> FindAll(int networkid)
	{
		return this.dao.findAll(networkid);
	}
	
	public List<AmplifierConfig> FindAll(int networkid, int nodeid)
	{
		return this.dao.findAll(networkid,nodeid);
	}
	
	public List<AmplifierConfig> FindAllCommonBrField(int networkidBf,int networkidGf,int nodeid)
	{
		return this.dao.findAllCommonBrField(networkidBf,networkidGf,nodeid);
	}
	
	public List<AmplifierConfig> FindAllAddedBrField(int networkidBf,int networkidGf,int nodeid)
	{
		return this.dao.findAllAddedBrField(networkidBf,networkidGf,nodeid);
	}
	
	public List<AmplifierConfig> FindAllModifiedBrField(int networkidBf,int networkidGf,int nodeid)
	{
		return this.dao.findAllModifiedBrField(networkidBf,networkidGf,nodeid);
	}
	
	public List<AmplifierConfig> FindAllDeletedBrField(int networkidBf,int networkidGf,int nodeid)
	{
		return this.dao.findAllDeletedBrField(networkidBf,networkidGf,nodeid);
	}
	
	public void Insert(AmplifierConfig info) throws SQLException{
		this.dao.insert(info);
		
	}
	
	public void Update(int networkid, int nodeid, int rack, int sbrack, int card, int status, float gain, int mode, int att) throws SQLException
	{
		this.dao.update(networkid, nodeid, rack, sbrack, card, status, gain, mode, att);
	}
	
	public void UpdateGain(int networkid, int nodeid, int rack, int sbrack, int card, float gain) throws SQLException
	{
		this.dao.updateGain(networkid, nodeid, rack, sbrack, card, gain);
	}
	
	public void DeleteByNetworkId(int networkid)
	{
		this.dao.deleteByNetworkId(networkid);
	}

}
