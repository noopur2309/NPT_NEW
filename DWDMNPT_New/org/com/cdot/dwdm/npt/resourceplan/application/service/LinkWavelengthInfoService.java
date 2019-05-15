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
import application.dao.LinkWavelengthInfoDao;
import application.model.Circuit;
import application.model.LinkWavelength;
import application.model.LinkWavelengthInfo;
import application.model.LinkWavelengthMap;
import application.model.CardInfo;

@Service
public class LinkWavelengthInfoService{

	@Autowired
	private LinkWavelengthInfoDao dao;
	
	public LinkWavelengthInfo FindPathForlinkWavelength(int networkid,int LinkId, int DemandId)
	{
		return this.dao.findPathForlinkWavelength(networkid, LinkId, DemandId);
	}
	
	public LinkWavelengthInfo FindPathForlinkWavelength(int networkid,int LinkId, int DemandId, int Wavelength)
	{
		return this.dao.findPathForlinkWavelength(networkid, LinkId, DemandId, Wavelength);
	}
	
	public LinkWavelength Find(int networkid, int linkid)
	{
		return this.dao.find(networkid,linkid);
	}
		
	public List<LinkWavelengthInfo> FindAll(int networkid)
	{
		return this.dao.findAll(networkid);
	}
	public List<LinkWavelengthInfo> FindAllByLink(int networkid,int linkid)
	{
		return this.dao.findAllByLink(networkid, linkid);
	}
	
//	public LinkWavelength FindCard(int networkid, int nodeid, int cardid)
//	{
//		return this.dao.findCardInfo(networkid, nodeid, cardid);
//	}
	
	public void Insert(LinkWavelengthInfo info) throws SQLException{
		this.dao.insert(info);		
	}
	
	public int Count()
	{
		return this.dao.count();
	}
	
	public void DeleteByNetworkId(int networkid)
	{
		this.dao.deleteByNetworkId(networkid);
	}
	
	public int MaxWave()
	{
		return this.dao.maxwave();
	}
	public int MinWave()
	{
		return this.dao.minwave();
	}
	public int FindTotalWavelengths(int networkid, int linkid){
		return this.dao.findtotalwavelength(networkid, linkid);
	}
	
	public List<Map<String, Object>> FindNodeWiseAllLambdaInfo(int networkid,int nodeid)
	{
		return this.dao.findNodeWiseAllLambdaInfo(networkid, nodeid);
	}

}