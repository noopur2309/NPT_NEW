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

import application.dao.NetworkDao;
import application.dao.TopologyDao;
import application.model.Circuit;
import application.model.Network;
import application.model.Topology;

@Service
public class TopologyService{

	@Autowired
	private TopologyDao topologyDao;
	
	public List<Topology> FindAll()
	{
		return this.topologyDao.findAll();
	}
	
	public List<Map<String, Object>> FindAllTopology()
	{
		return this.topologyDao.findAllTopology();
	}

	public void InsertTopology(Topology topology) throws SQLException{
		this.topologyDao.insertTopology(topology);
		
	}
	
	public void DeleteTopology(int networkid)
	{
		this.topologyDao.deleteByNetworkId(networkid);
	}
	
	public int Count()
	{
		return this.topologyDao.count();
	}
	
	public int Count(int networkId)
	{
		return this.topologyDao.count(networkId);
	}
	
	public List<Map<String, Object>> QueryTopologyWithNodeType()
	{
		return this.topologyDao.queryTopologyWithNodeType();
	}
	
	public List<Map<String, Object>> QueryTopologyWithNodeTypeWithNetowrkId(int networkId)
	{
		return this.topologyDao.queryTopologyWithNodeTypeWithNetworkId(networkId);
	}
	
	
	public Topology FindTopology(int networkid, int nodeid)
	{
		return this.topologyDao.findTopology(networkid, nodeid);
	}
	
	public void CopyTopologyDataInBrField(int networkidGrField, int networkidBrField) throws SQLException
	{
		this.topologyDao.copyTopologyDataInBrField(networkidGrField, networkidBrField);
	}

		
}