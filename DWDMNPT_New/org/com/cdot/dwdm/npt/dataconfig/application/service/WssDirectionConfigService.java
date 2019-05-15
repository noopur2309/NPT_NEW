package application.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.dao.McsPortMappingDao;
import application.dao.PatchCordDao;
import application.dao.WssDirectionConfigDao;
import application.dao.WssDirectionConfigDao;
import application.model.CardInfo;
import application.model.McsPortMapping;
import application.model.PatchCord;
import application.model.WssDirectionConfig;
import application.model.WssDirectionConfig;

@Service
public class WssDirectionConfigService {
	@Autowired
	private WssDirectionConfigDao dao;
	
	public List<WssDirectionConfig> FindAll(int networkid)
	{
		return this.dao.findAll(networkid);
	}
	
	public List<WssDirectionConfig> FindAll(int networkid, int nodeid)
	{
		return this.dao.findAll(networkid,nodeid);
	}
	
	public void Insert(WssDirectionConfig info) throws SQLException{
		this.dao.insert(info);
		
	}
	
	public void Update(int networkid, int nodeid, int rack, int sbrack, int card, int status, int dirType, int LaserStatus, int AttenuationConfigMode, int FixedAttenuation, int PreEmphasisTriggerPowerDiff, int Attenuation) throws SQLException
	{
		this.dao.update(networkid, nodeid, rack, sbrack, card, dirType, LaserStatus, AttenuationConfigMode, FixedAttenuation, PreEmphasisTriggerPowerDiff, Attenuation);
	}
	
	public void Update(int networkid, int nodeid, int rack, int sbrack, int card, float wSS_attenuation_Drop) throws SQLException
	{
		this.dao.updateAttenuation(networkid, nodeid, rack, sbrack, card, wSS_attenuation_Drop);
	}
	
	public void DeleteByNetworkId(int networkid)
	{
		this.dao.deleteByNetworkId(networkid);
	}
	
	
	public List<WssDirectionConfig> FindAllCommonBrField(int networkidBf,int networkidGf,int nodeid)
	{
		return this.dao.findAllCommonBrField(networkidBf,networkidGf,nodeid);
	}
	
	public List<WssDirectionConfig> FindAllAddedBrField(int networkidBf,int networkidGf,int nodeid)
	{
		return this.dao.findAllAddedBrField(networkidBf,networkidGf,nodeid);
	}
	
	public List<WssDirectionConfig> FindAllModifiedBrField(int networkidBf,int networkidGf,int nodeid)
	{
		return this.dao.findAllModifiedBrField(networkidBf,networkidGf,nodeid);
	}
	
	public List<WssDirectionConfig> FindAllDeletedBrField(int networkidBf,int networkidGf,int nodeid)
	{
		return this.dao.findAllDeletedBrField(networkidBf,networkidGf,nodeid);
	}

}
