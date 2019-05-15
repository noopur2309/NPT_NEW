/**
 * 
 */
/**
 * @author sunaina
 *
 */
package application.service;
import java.util.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.dao.NodeDao;
import application.model.Circuit;
import application.model.Node;

@Service
public class NodeService{

	@Autowired
	private NodeDao nodedao;
	
	public List<Node> FindAll(int networkid)
	{
		return this.nodedao.findAll(networkid);
	}
	
	public List<Node> FindAllNonILAs()
	{
		return this.nodedao.findAllNonIlaNodes();
	}
	
	public List<Node> FindAllNonILAsNetworkId(int networkId)
	{
		return this.nodedao.findAllNonILAsNetworkId(networkId);
	}
	
	public Node FindNode(int networkid, int nodeid)
	{
		return this.nodedao.findNode(networkid, nodeid);
	}
	
	public void Updatedb(HashMap<String,Object> Difference, int networkId, int nodeid)
	{
		this.nodedao.updatedb(Difference, networkId, nodeid);
	}
	
	public List<Map<String, Object>> FindAllNodes(int networkId)
	{
		return this.nodedao.findAllNodes(networkId);
	}

	public void InsertNode(Node node) throws SQLException{
		this.nodedao.insertNode(node);
		
	}
	
	public int Count(int networkId)
	{
		return this.nodedao.count(networkId);
	}
	
	public int NodeTypeCount(int networkId, int nodetype)
	{
		return this.nodedao.nodeTypecount(networkId,nodetype);
	}
	public String FindSiteByNodeId(int nodeid, int networkId)
	{
		return this.nodedao.findsitebynodeid(nodeid, networkId);
	}
	public String FindStationByNodeId(int nodeid, int networkId)
	{
		return this.nodedao.findstationbynodeid(nodeid, networkId);
	}
	
	public List<Node> FindAddedNodesInBrField(int networkidGrField, int networkidBrField)
	{
		return this.nodedao.findAddedNodesInBrField(networkidGrField,networkidBrField);
	}
	
	public List<Node> FindDeletedNodesInBrField(int networkidGrField, int networkidBrField)
	{
		return this.nodedao.findDeletedNodesInBrField(networkidGrField, networkidBrField);
	}
	
	public List<Node> FindModifiedNodesInBrField(int networkidGrField, int networkidBrField)
	{
		return this.nodedao.findModifiedNodesInBrField(networkidGrField, networkidBrField);
	}
	
	public List<Node> FindDegreeModifiedNodesInBrField(int networkidGrField, int networkidBrField)
	{
		return this.nodedao.findDegreeModifiedNodesInBrField(networkidGrField, networkidBrField);
	}
	
	public List<Node> FindCommonNodesInBrField(int networkidGrField, int networkidBrField)
	{
		return this.nodedao.findCommonNodesInBrField(networkidGrField, networkidBrField);
	}
	
	public void CopyNodeDataInBrField(int networkidGrField, int networkidBrField) throws SQLException
	{
		this.nodedao.copyNodeDataInBrField(networkidGrField, networkidBrField);
	}
	public List<Node> FindNodeId(int networkId)
	{
		return this.nodedao.findnodeId(networkId);
	}
	
	public List<Integer> FindnodeIdIntegers(int networkId)
	{
		return this.nodedao.findnodeIdIntegers(networkId);
	}

	public List<Node> FindAllByNodeType(int networkId,int nodeType)
	{
		return this.nodedao.findAllByNodeType(networkId,nodeType);
	}
	

	
}