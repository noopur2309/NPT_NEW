package application.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.dao.AmplifierConfigDao;
import application.dao.OpticalPowerCdcRoadmAddDao;
import application.dao.PatchCordDao;
import application.dao.TpnPortInfoDao;
import application.model.AmplifierConfig;
import application.model.CardInfo;
import application.model.OpticalPowerCdcRoadmAdd;
import application.model.PatchCord;
import application.model.TpnPortInfo;

@Service
public class OpticalPowerCdcRoadmAddService {
	@Autowired
	private OpticalPowerCdcRoadmAddDao dao;
	
	public List<OpticalPowerCdcRoadmAdd> FindAll(int networkid)
	{
		return this.dao.findAll(networkid);
	}
	
	public List<OpticalPowerCdcRoadmAdd> FindAll(int networkid, int nodeid)
	{
		return this.dao.findAll(networkid,nodeid);
	}
	
	public void Insert(OpticalPowerCdcRoadmAdd info) throws SQLException{
		this.dao.insert(info);
		
	}
	
//	public void Update(int networkid, int nodeid, int rack, int sbrack, int card, int status, float gain, int mode, int att) throws SQLException
//	{
//		this.dao.update(networkid, nodeid, rack, sbrack, card, status, gain, mode, att);
//	}
	
	public void DeleteByNetworkId(int networkid)
	{
		this.dao.deleteByNetworkId(networkid);
	}

}
