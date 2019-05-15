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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.dao.IpSchemeLinkDao;
import application.model.IpSchemeLink;
import application.model.IpSchemeNode;

@Service
public class IpSchemeLinkService{

	@Autowired
	private IpSchemeLinkDao ipSchemeLinkDao;	
	
	
	public List<IpSchemeLink> FindAll(int networkId)
	{
		return this.ipSchemeLinkDao.findAll(networkId);
	}
	
	public void InsertIpSchemeLink(IpSchemeLink iplink) throws SQLException{
		this.ipSchemeLinkDao.insertIpSchemeLink(iplink);		
	}
	
	public void DeleteByNetworkId(int networkid)
	{
		this.ipSchemeLinkDao.deleteByNetworkId(networkid);
	}
	
	public int Count(int networkid)
	{
		return this.ipSchemeLinkDao.count(networkid);
	}

	public List<IpSchemeLink> FindIpSchemeLinkwise(int networkId, int linkid)
	{
		return this.ipSchemeLinkDao.findIpSchemeLinkwise(networkId, linkid);
	}

	
	public IpSchemeLink FindIpSchemeLink(int networkid, int nodeid)
	{
		return this.ipSchemeLinkDao.findIpSchemeLink(networkid, nodeid);
	}
	
	public Object FindIpOfSrcNodeEnd_cf(int networkid, int srcnode, int destnode)
	{
		return this.ipSchemeLinkDao.findIpOfSrcNodeEnd_cf(networkid, srcnode,destnode);
	}
	
	 /**
	  *
	  * @brief  Query to Find out last generated ip in case of Brown Field
	  * @date   16th Oct, 2017
	  * @author hp
	  */
	 public List<IpSchemeLink>  FindLastGeneratedLinkIP(int networkId){
		 return this.ipSchemeLinkDao.findLastGeneratedLinkIP(networkId);
	 }

	public void CopyIpSchemeLinkInBrField(int networkidGrField, int networkidBrField ) throws SQLException{
		this.ipSchemeLinkDao.copyIpSchemeLinkInBrField(networkidGrField, networkidBrField);		
	}

}