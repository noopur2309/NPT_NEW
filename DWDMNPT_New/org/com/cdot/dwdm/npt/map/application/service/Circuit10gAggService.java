package application.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.dao.Circuit10gAggDao;
import application.model.AggregatorClientMap;
import application.model.Circuit10gAgg;
import application.model.PortInfo;


@Service
public class Circuit10gAggService{

	@Autowired
	private Circuit10gAggDao circuit10gAggDao;


	public int Count(int networkId)
	{
		return this.circuit10gAggDao.count(networkId);
	}

	public int Count(int networkId, int circuitId)
	{
		return this.circuit10gAggDao.count(networkId, circuitId);
	}

	public List<Circuit10gAgg> FindAllCircuit10gAgg(int networkId)
	{
		return this.circuit10gAggDao.findAllCircuit10gAgg(networkId);
	}	

	public List<Circuit10gAgg> FindAllCircuit10gAgg(int networkId, int circuitId)
	{
		return this.circuit10gAggDao.findAllCircuit10gAgg(networkId, circuitId);
	}	
	
	public Circuit10gAgg FindCircuit10gAgg(int networkid, int circuitId, int circuit10gAggId)
	{
		return this.circuit10gAggDao.findCircuit10gAgg(networkid, circuitId, circuit10gAggId);
	}
	
	public Circuit10gAgg FindCircuitByAggCircuitId(int networkid, int circuit10gAggId)
	{
		return this.circuit10gAggDao.findCircuitByAggCircuitId(networkid, circuit10gAggId);
	}

	public List<Map<String,Object>> FindGroupedCircuit10gAgg(int networkid, int cicuitId)
	{
		return this.circuit10gAggDao.findGroupedCircuit10gAgg(networkid, cicuitId);
	}

	public void InsertCircuit10gAgg(Circuit10gAgg circuit10gAgg, HashMap<String,Object> NetworkInfoMap) throws SQLException{
		this.circuit10gAggDao.insertCircuit10gAgg(circuit10gAgg, NetworkInfoMap);		
	}

	public void DeleteCircuit10GAgg(int networkid)
	{
		this.circuit10gAggDao.deleteByNetworkId(networkid);
	}

	public void CopyCircuit10GAggInBrField(int networkidGrField, int networkidBrField) throws SQLException
	{
		this.circuit10gAggDao.copyCircuit10GAggInBrField(networkidGrField, networkidBrField);
	}

	public List<Map<String,Object>> FindCommonCircuits10gAggInBrField(int networkidGrField, int networkidBrField,int circuitId)
	{
		return this.circuit10gAggDao.findCommonCircuitsInBrField(networkidGrField, networkidBrField,circuitId);
	}

	public List<Map<String,Object>> FindAddedCircuitsInBrField(int networkidGrField, int networkidBrField,int circuitId)
	{
		return this.circuit10gAggDao.findAddedCircuit10gAggInBrField(networkidGrField, networkidBrField,circuitId);
	}
	
	public void UpdateAggClientMappingView()
	{
		this.circuit10gAggDao.updateAggClientMappingView();
	}

	public List<Map<String,Object>> FindRoutesWithServiceBasedNodeEnds10GAgg(int networkId, int nodeId)
	{
		return this.circuit10gAggDao.findRoutesWithServiceBasedNodeEnds10GAgg(networkId, nodeId);
	}
	
	public AggregatorClientMap FindMpnDetailsFromAggClientMapping(PortInfo port)
	{
		return this.circuit10gAggDao.findMpnDetailsFromAggClientMapping(port);
	}


	
	// public List<Map<String,Object>> FindAllGrouped()
	// {
	// 	return this.circuitDao.findAllGrouped();
	// }
	// public List<Map<String,Object>> FindAllGrouped(int networkId)
	// {
	// 	return this.circuitDao.findAllGrouped(networkId);
	// }
	
	// public int countCircuitsByClientProtPerNode(int networkid, int nodeid, String protType)
	// {
	// 	return this.circuitDao.countCircuitsByClientProtPerNode(networkid, nodeid, protType);
	// }
	
	// public List<Circuit> CircuitsByClientProtPerNode(int networkid, int nodeid, String protType)
	// {
	// 	return this.circuitDao.CircuitsByClientProtPerNode(networkid, nodeid, protType);
	// }

	
	
	// public void DeleteRedundantCircuit(int NetworkId) throws SQLException{
	// 	this.circuitDao.deleteRedundantCircuit(NetworkId);		
	// }
	
	// public void DeleteCircuit(int networkid)
	// {
	// 	this.circuitDao.deleteByNetworkId(networkid);
	// }
	
	// public void DeleteCircuit(int networkid, int circuitid)
	// {
	// 	this.circuitDao.deleteCircuit(networkid, circuitid);
	// }
	
	// public List<Map<String,Object>> GroupCircuitsFor10GMpn(int networkid, int circuitModeGeneration)
	// {
	// 	return this.circuitDao.groupCircuitsFor10GMpn(networkid, circuitModeGeneration);
	// }
	
	// public List<Map<String,Object>> GroupCircuits(int networkid, String lineRate, float clientRate, int circuitModeGeneration)
	// {
	// 	return this.circuitDao.groupCircuits(networkid, lineRate, clientRate, circuitModeGeneration);
	// }
	
	// public List<Circuit> FindCircuitsFor10GMPN(int networkid, int srcnode, int destnode,String prottype, String colour,String path,String clientprot, String channelProtection, int circuitModeGeneration)
	// {
	// 	return this.circuitDao.findCircuitsFor10GMPN(networkid, srcnode, destnode,prottype,colour,path,clientprot,channelProtection,circuitModeGeneration);
	// }
	
	// public List<Circuit> FindCircuitsFor100GMPN(int networkid, int srcnode, int destnode,String ProtectionType, String ClientProtectionType, String ColourPreference, String PathType, String ChannelProtection)
	// {
	// 	return this.circuitDao.findCircuitsFor100GMPN(networkid, srcnode, destnode,ProtectionType, ClientProtectionType, ColourPreference, PathType, ChannelProtection);
	// }
	
	// public List<Circuit> FindCircuitsFor200GMPN(int networkid, int srcnode, int destnode,String ProtectionType, String ClientProtectionType, String ColourPreference, String PathType, String ChannelProtection)
	// {
	// 	return this.circuitDao.findCircuitsFor200GMPN(networkid, srcnode, destnode,ProtectionType, ClientProtectionType, ColourPreference, PathType, ChannelProtection);
	// }
	
	// public List<Circuit> FindCircuitsWithClientProt(int networkid, int nodeid, String prottype)
	// {
	// 	return this.circuitDao.findCircuitsWithClientProt(networkid, nodeid, prottype);
	// }
	
	// public List<Map<String, Object>> FindCircuitsForTPN(int networkid, String lineRate, float clientRate, int circuitModeGeneration)
	// {
	// 	return this.circuitDao.findCircuitsForTPN(networkid, lineRate, clientRate, circuitModeGeneration);
	// }
	
	// public int Count()
	// {
	// 	return this.circuitDao.count();
	// }
	
	// public int Count(int networkId)
	// {
	// 	return this.circuitDao.count(networkId);
	// }
	
	// public int CircuitCapacity(int networkid)
	// {
	// 	return this.circuitDao.circuitCapacity(networkid);
	// }
	
	// public List<Circuit> FindAddedCircuitsInBrField(int networkidGrField, int networkidBrField)
	// {
	// 	return this.circuitDao.findAddedCircuitsInBrField(networkidGrField, networkidBrField);
	// }
	
	// public List<Circuit> FindAddedCircuitsInBrField(int networkidGrField, int networkidBrField, int nodeid)
	// {
	// 	return this.circuitDao.findAddedCircuitsInBrField(networkidGrField, networkidBrField, nodeid);
	// }
	
	// public List<Circuit> FindCommonCircuitsInBrField(int networkidGrField, int networkidBrField)
	// {
	// 	return this.circuitDao.findCommonCircuitsInBrField(networkidGrField, networkidBrField);
	// }
	
	// public List<Circuit> FindDeletedCircuitsInBrField(int networkidGrField, int networkidBrField, int nodeid)
	// {
	// 	return this.circuitDao.findDeletedCircuitsInBrField(networkidGrField, networkidBrField, nodeid);
	// }	

	//  /***
	//   * 
	//   *    @brief  These APIs are called to deal with In Memory Table(Name: CircuitInMemoryTable)
	//   *            which is used for Circuit Processing
	//   *    @date   29/9/17	
	//   *    @author hp
	//   */
	//  public void InsertInMemoryCircuit(Circuit circuit) throws SQLException {
	// 	 this.circuitDao.insertInMemoryCircuit(circuit);
	//   }

	//  public List<Circuit> FindAllInMemoryCircuit(){		
	// 	return this.circuitDao.findAllInMemoryCircuit();
	//  }
	
	//  public List<Circuit> FindCommonCircuitsInGreenField(int networkidGrField, int networkidBrField)
	//  {
	// 	 return this.circuitDao.findCommonCircuitsInGreenField(networkidGrField, networkidBrField);
	//  }
	 
	//  public void DeleteInMemoryCircuit(int networkid)
	//  {
	// 	 this.circuitDao.deleteInMemoryCircuitsByNetworkId(networkid);
	//  }
		
	//  /**************************************************************************/
	 
	// public  List<Map<String,Object>> FindAddedCircuitsInBrFieldGrouped(int networkidGrField, int networkidBrField)
	// {
	// 	return this.circuitDao.findAddedCircuitsInBrFieldGrouped(networkidGrField, networkidBrField);
	// }
	
	// public  List<DemandMapping> FindAddedCircuitsDemMappedInBrField(int networkidGrField, int networkidBrField)
	// {
	// 	return this.circuitDao.findAddedCircuitsDemMappedInBrField(networkidGrField, networkidBrField);
	// }
	
	// public  List<Map<String,Object>> FindCommonCircuitsInBrFieldGrouped(int networkidGrField, int networkidBrField)
	// {
	// 	return this.circuitDao.findCommonCircuitsInBrFieldGrouped(networkidGrField, networkidBrField);
	// }
	
	// public  List<DemandMapping> FindCommonCircuitsDemMappedInBrField(int networkidGrField, int networkidBrField)
	// {
	// 	return this.circuitDao.findCommonCircuitsDemMappedInBrField(networkidGrField, networkidBrField);
	// }
	
	// public  List<Map<String,Object>> FindDeletedCircuitsInBrFieldGrouped(int networkidGrField, int networkidBrField)
	// {
	// 	return this.circuitDao.findDeletedCircuitsInBrFieldGrouped(networkidGrField, networkidBrField);
	// }
	
	// public  List<DemandMapping> FindDeletedCircuitsDemMappedInBrField(int networkidGrField, int networkidBrField)
	// {
	// 	return this.circuitDao.findDeletedCircuitsDemMappedInBrField(networkidGrField, networkidBrField);
	// }
	
	// public  void CopyCircuitDataInBrField(int networkidGrField, int networkidBrField) throws SQLException
	// {
	// 	this.circuitDao.copyCircuitDataInBrField(networkidGrField, networkidBrField);
	// }
	
	// public  List<DemandMapping> FindModifiedCircuitsDemMappedInBrField(int networkidGrField, int networkidBrField)
	// {
	// 	return this.circuitDao.findModifiedCircuitsDemMappedInBrField(networkidGrField, networkidBrField);
	// }
	
	

}