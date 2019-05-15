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
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import application.dao.IpSchemeNodeDao;
import application.dao.LinkDao;
import application.dao.CardInfoDao;
import application.model.IpSchemeNode;
import application.model.Link;
import application.model.Node;
import application.model.CardInfo;

@Service
public class IpSchemeNodeService{

	@Autowired
	private IpSchemeNodeDao ipSchemeNodeDao;
		
	
	public List<IpSchemeNode> FindAll(int networkId)
	{
		return this.ipSchemeNodeDao.findAll(networkId);
	}
	
	public void InsertIpSchemeNode(IpSchemeNode ipnode) throws SQLException{
		this.ipSchemeNodeDao.insertIpSchemeNode(ipnode);		
	}	
	
	public IpSchemeNode FindIpSchemeNode(int networkid, int nodeid)
	{
		return this.ipSchemeNodeDao.findIpSchemeNode(networkid, nodeid);
	}
	
	public void DeleteByNetworkId(int networkid)
	{
		this.ipSchemeNodeDao.deleteByNetworkId(networkid);
	}
	
	public int Count(int networkId)
	{
		return this.ipSchemeNodeDao.count(networkId);
	}
	public List<IpSchemeNode> FindIpSchemeNodewise(int networkId, int nodeid)
	{
		return this.ipSchemeNodeDao.findIpSchemeNodewise(networkId, nodeid);
	}
	
	 /**
	  *
	  * @brief  Query to Find out last generated ip in case of Brown Field 
	  *         [SELECT * FROM IpSchemeNode where NetworkId= ?  ORDER BY NetworkId DESC LIMIT 1 ;]  
	  * @date   16th Oct, 2017
	  * @author hp
	  */
	 public List<IpSchemeNode>  FindLastGeneratedNodeIP(int networkId){
		 System.out.println(" networkId "+ networkId);
		 return this.ipSchemeNodeDao.findLastGeneratedNodeIP(networkId);
	 }	


	public void CopyIpSchemeNodeInBrField(int networkidGrField, int networkidBrField ) throws SQLException{
		this.ipSchemeNodeDao.copyIpSchemeNodeInBrField(networkidGrField, networkidBrField);		
	}


	
}