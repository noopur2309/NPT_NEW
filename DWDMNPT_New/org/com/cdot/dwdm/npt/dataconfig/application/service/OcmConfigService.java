package application.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.dao.OcmConfigDao;
import application.dao.PatchCordDao;
import application.dao.TpnPortInfoDao;
import application.model.CardInfo;
import application.model.McsPortMapping;
import application.model.OcmConfig;
import application.model.PatchCord;
import application.model.TpnPortInfo;

@Service
public class OcmConfigService {
	@Autowired
	private OcmConfigDao dao;
	
	public List<OcmConfig> FindAll()
	{
		return this.dao.findAll();
	}
	
	public List<OcmConfig> FindAll(int networkid, int nodeid)
	{
		return this.dao.findAll(networkid,nodeid);
	}
	
	public void Insert(OcmConfig info) throws SQLException{
		this.dao.insert(info);
		
	}
	public List<OcmConfig> FindOcmId(int networkid, int nodeid,int rack,int subRack,int cardid)
	{
		return this.dao.findOcmId(networkid,nodeid,rack,subRack,cardid);
	}
	public void DeleteByNetworkId(int networkid)
	{
		this.dao.deleteByNetworkId(networkid);
	}

}
