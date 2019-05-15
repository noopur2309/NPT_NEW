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

import application.dao.LinkDao;
import application.dao.CardInfoDao;
import application.model.Link;
import application.model.Node;
import scala.collection.generic.BitOperations.Int;
import application.model.CardInfo;

@Service
public class LinkService{

	@Autowired
	private LinkDao linkdao;
	
	public List<Link> FindAll(int networkId)
	{
		return this.linkdao.findAll(networkId);
	}
	
	public List<Link> FindAllRamanLinks(int networkId,int nodeid)
	{
		return this.linkdao.findAllRamanLinks(networkId,nodeid);
	}
	
	public List<Link> FindAllLinks(int networkId,int nodeid)
	{
		return this.linkdao.findAllLinks(networkId,nodeid);
	}
	
	public List<Link> FindAllwithNetworkId(int networkId)
	{
		return this.linkdao.findAllwithNetworkId(networkId);
	}
	
	
	public List<Map<String, Object>> FindAllLinks(int networkId)
	{
		return this.linkdao.findAllLinks(networkId);
	}
	public List<Map<String, Object>> FindAllLinksWithNetworkId(int networkId)
	{
		return this.linkdao.findAllLinksWithNetworkId(networkId);
	}

	public void InsertLink(Link link) throws SQLException{
		this.linkdao.insertLink(link);
		
	}
	
	public int Count(int networkId)
	{
		return this.linkdao.count(networkId);
	}
	
	public String FindLinkDirection(int networkid,int srcnode, int destnode)
	{
		return this.linkdao.findLinkDirection(networkid,srcnode, destnode);
	}
	
	public int FindLinkInDirection(int networkid,int node, String dir)
	{
		return this.linkdao.findLinkInDirection(networkid,node, dir);
	}
	
	public Link FindLink(int networkid,int srcnode, int destnode)
	{
		return this.linkdao.findLink(networkid,srcnode, destnode);
	}
	
	public Link FindLink(int networkid,int linkId)
	{
		return this.linkdao.findLink(networkid,linkId);
	}
	
	public List<Map<String, Object>> FindSrcIpOfLink(int SrcNodeId, int networkId)
	{
		return this.linkdao.findSrcIpOfLink(SrcNodeId, networkId);
	}
	
	public List<Map<String, Object>> FindDestIpOfLink(int DestNodeId, int networkId)
	{
		return this.linkdao.findDestIpOfLink(DestNodeId, networkId);
	}
	
	public List<Map<String, Object>> PerNodeLinkData_cf(int networkid, int NodeId)
	{
		return this.linkdao.perNodeLinkData_cf(networkid, NodeId);
	}
	
	public List<Map<String, Object>> PerLinkWavelengths_cf(int networkid, int NodeId, int LinkId)
	{
		return this.linkdao.perLinkWavelengths_cf(networkid, NodeId, LinkId);
	}
	
	public List<Link> FindOMSProtectedLinksOnANode(int networkid, int nodeid)
	{
		return this.linkdao.findOMSProtectedLinksOnANode(networkid, nodeid);
	}
	
	public List<Link> FindAddedLinksOnNodeInBrField(int networkidGrField, int networkidBrField, int nodeid)
	{
		return this.linkdao.findAddedLinksOnNodeInBrField(networkidGrField, networkidBrField, nodeid);
	}
	
	public List<Link> FindAddedLinksInBrField(int networkidGrField, int networkidBrField)
	{
		return this.linkdao.findAddedLinksInBrField(networkidGrField, networkidBrField);
	}
	
	public List<Link> FindCommonLinksInBrField(int networkidGrField, int networkidBrField)
	{
		return this.linkdao.findCommonLinksInBrField(networkidGrField, networkidBrField);
	}
	
	public List<Link> FindModifiedLinksInBrField(int networkidGrField, int networkidBrField)
	{
		return this.linkdao.findModifiedLinksInBrField(networkidGrField, networkidBrField);
	}
	
	public List<Link> FindModifiedLinksInBrField(int networkidGrField, int networkidBrField,int nodeid)
	{
		return this.linkdao.findModifiedLinksInBrField(networkidGrField, networkidBrField, nodeid);
	}
	
	public List<Link> findDeletedLinksInBrField(int networkidGrField, int networkidBrField)
	{
		return this.linkdao.findDeletedLinksInBrField(networkidGrField, networkidBrField);
	}
	
	public List<Link> findDeletedLinksInBrField(int networkidGrField, int networkidBrField, int nodeid)
	{
		return this.linkdao.findDeletedLinksInBrField(networkidGrField, networkidBrField, nodeid);
	}	
	
	public void CopyLinkDataInBrField(int networkidGrField, int networkidBrField) throws SQLException
	{
		this.linkdao.copyLinkDataInBrField(networkidGrField, networkidBrField);
	}

	public List<Link> FindLinkId(int networkId)
	{
		return this.linkdao.findlinkId(networkId);
	}

	
}