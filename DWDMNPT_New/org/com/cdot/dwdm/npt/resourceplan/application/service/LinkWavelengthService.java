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
import application.model.Circuit;
import application.model.LinkWavelength;
import application.model.LinkWavelengthMap;
import application.model.CardInfo;

@Service
public class LinkWavelengthService{

	@Autowired
	private LinkWavelengthDao dao;
	
	public LinkWavelength Find(int networkid, int linkid)
	{
		return this.dao.find(networkid,linkid);
	}
		
	public List<LinkWavelengthMap> LinkWavelength(int networkid)
	{
		return this.dao.linkWavelength(networkid);
	}
	
//	public LinkWavelength FindCard(int networkid, int nodeid, int cardid)
//	{
//		return this.dao.findCardInfo(networkid, nodeid, cardid);
//	}
	
	public void Insert(LinkWavelength info) throws SQLException{
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
//	public LinkWavelength FindWavelength(int networkid, int linkid)
//	{
//		return this.dao.findWavelength(networkid,linkid);
//	}

}