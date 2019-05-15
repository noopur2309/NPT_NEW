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
import application.dao.PortInfoDao;
import application.model.Circuit;
import application.model.PortInfo;
import application.model.TpnPortInfo;
import application.model.AggregatorClientMap;
import application.model.CardInfo;

@Service
public class PortInfoService{

	@Autowired
	private PortInfoDao dao;
		
	/*
	 * NodeKey = networkid+"_"+nodeid
	 * */
	public List<PortInfo> FindAll(int networkid, int nodeid)
	{
		return this.dao.findAll(networkid,nodeid);
	}
	
	public PortInfo FindMax5x10GPort(int networkid, int nodeid)
	{
		return this.dao.findMax5x10GPort(networkid, nodeid);
	}
	
	public List <PortInfo> FindPortInfo(int networkid, int nodeid, int rack, int sbrack, int cardid,boolean ...opxFlag)
	{
		return this.dao.findPortInfo(networkid, nodeid, rack, sbrack, cardid,opxFlag);
	}
	
	public PortInfo FindLineDirectionInfo(int networkid, int nodeid, int rack, int sbrack, int cardid,int lineport)
	{
		return this.dao.findLineDirectionInfo(networkid, nodeid, rack, sbrack, cardid,lineport);
	}
	
	public List <PortInfo> FindPortInfo(int networkid, int nodeid, int demandid)
	{
		return this.dao.findPortInfo(networkid, nodeid, demandid);
	}
	public List<PortInfo> FindPortInfobyPortID(int networkid, int nodeid, int portid,int demandid)
	{
		return this.dao.findPortInfobyPortid(networkid, nodeid,portid, demandid);
	}
	public List<Integer> FindPorts(int networkid, int nodeid,int Rack , int Sbrack, int demandid)
	{
		return this.dao.findPorts(networkid, nodeid, Rack, Sbrack, demandid);
	}
	
	public int Count(int networkId)
	{
		return this.dao.count(networkId);
	}
	
	public void CopyPortInfoDataInBrField(int networkidGrField, int networkidBrField ) throws SQLException
	{
		this.dao.copyPortInfoDataInBrField( networkidGrField,  networkidBrField );
	}
	
	public List <PortInfo> FindDistinctPortInfoByDemandId(int networkid, int nodeid, int demandid)
	{
		return this.dao.findDistinctPortInfoByDemandId(networkid, nodeid, demandid);
	}
	
	public PortInfo FindPortInfo(int networkid, int nodeid, int rack, int sbrack, int card, int circuitid)
	{
		return this.dao.findPortInfo(networkid, nodeid, rack, sbrack, card, circuitid);
	}
	
	public List<PortInfo> FindPortInfoList(int networkid, int nodeid, int rack, int sbrack, int card, int circuitid)
	{
		return this.dao.findPortInfoList(networkid, nodeid, rack, sbrack, card, circuitid);
	}
	
	public PortInfo FindDcmModuleByDir(int networkid, int nodeid, String dir)
	{
		return this.dao.findDcmModuleByDir(networkid, nodeid, dir);
	}
	
	public PortInfo FindUnassignedDcmModule(int networkid, int nodeid)
	{
		return this.dao.findUnassignedDcmModule(networkid, nodeid);
	}
	
	public void UpdateDcmModule(int networkid, int nodeid, int rack , int sbrack, int card, int port , String dir)
	{
		this.dao.updateDcmModule(networkid, nodeid, rack, sbrack, card, port, dir);
	}	
		

	public void Insert(PortInfo info) throws SQLException{
		this.dao.insert(info);
		
	}
	
	public Integer GetFirstFreePortId(PortInfo port)
	{
		return this.dao.getFirstFreePortId(port);
	}
	
	public int Count()
	{
		return this.dao.count();
	}

	
	public void DeletePort(int networkid, int nodeid, int rack, int sbrack, int card, int portid)
	{
		this.dao.deletePort(networkid, nodeid, rack, sbrack, card, portid);
	}
	public int FindCircuitIdforPortID(int networkid, int nodeid,int Rack, int Sbrack, int Card,int PortId)
	{
		return this.dao.findCircuitIdforPortID(networkid, nodeid, Rack, Sbrack, Card, PortId);
	}
	
	public void DeleteDcmPort(int networkid, int nodeid, String dir)
	{
		this.dao.deleteDcmPort(networkid, nodeid, dir);
	}
	
	public void DeletePort(int networkid, int nodeid, int demandid)
	{
		this.dao.deletePort(networkid, nodeid, demandid);
	}
	
	
	public List<Map<String,Object>> CountPortByTypeNEId(int networkid,int nodeid)
	{
		return this.dao.countPortByTypeNEId(networkid, nodeid);
	}
	
	/*
	 * 
	 * Used to create the card info table in db
	 * Table name will be CardInfo_NetworkId_NodeId
	 * 
	 * * */
	public void CreatePortInfoTable(String nodekey)
	{
		this.dao.createPortInfoTable(nodekey);
	}
	
	public void DeleteAllPortInfo(int networkid)
	{
		this.dao.deleteAllPortInfo(networkid);
	}	
	
	public void CreateViewAllPortInfo(int networkid)
	{
		 this.dao.createViewAllPortInfo(networkid);
	}
	
	public void UpdateNodeKey(int networkid, int nodeid, int networkidBF) throws SQLException
	{
		 this.dao.updateNodeKey(networkid, nodeid, networkidBF);
	}
	
	public void InsertPortDataInBrField(int networkid, int nodeid, int networkidBF) throws SQLException
	{
		 this.dao.insertPortDataInBrField(networkid, nodeid, networkidBF);
	}
	
	/**
	 * 
	 * @desc Find out the Card Related Info from the Circuit Id
	 * @date 6th Feb,2018
	 * @author hp
	 * @param networkid
	 * @param nodeid
	 * @param circuitid
	 * @return
	 */
	public List<PortInfo> FindCircuitPortInfo(int networkid, int nodeid, int circuitid, int typeOfOtnLSP)
	{
		return this.dao.findCircuitPortInfo(networkid, nodeid, circuitid,typeOfOtnLSP);
	}
	
	/*Brown Field difference queries*/
	
	public List<PortInfo> FindAllAddedBrField(int networkid,int networkIdBf,int nodeid)
	{
		return this.dao.findAllAddedBrField(networkid,networkIdBf ,nodeid);
	}
	
	public List<PortInfo> FindAllDeletedBrField(int networkid,int networkIdBf,int nodeid)
	{
		return this.dao.findAllDeletedBrField(networkid,networkIdBf ,nodeid);
	}
	
	public List<PortInfo> FindAllModifiedBrField(int networkid,int networkIdBf,int nodeid)
	{
		return this.dao.findAllModifiedBrField(networkid,networkIdBf ,nodeid);
	}
	
	public List<PortInfo> FindAllCommonBrField(int networkid,int networkIdBf,int nodeid)
	{
		return this.dao.findAllCommonBrField(networkid,networkIdBf ,nodeid);
	}
	
	public List<PortInfo> FindAllUniqueAddedBrField(int networkid,int networkIdBf,int nodeid)
	{
		return this.dao.findAllUniqueAddedBrField(networkid,networkIdBf ,nodeid);
	}
	
	public List<PortInfo> FindAllUniqueDeletedBrField(int networkid,int networkIdBf,int nodeid)
	{
		return this.dao.findAllUniqueDeletedBrField(networkid,networkIdBf ,nodeid);
	}
	
	public List<PortInfo> FindAllUniqueModifiedBrField(int networkid,int networkIdBf,int nodeid)
	{
		return this.dao.findAllUniqueModifiedBrField(networkid,networkIdBf ,nodeid);
	}
	
	public List<PortInfo> FindAllUniqueCommonBrField(int networkid,int networkIdBf,int nodeid)
	{
		return this.dao.findAllUniqueCommonBrField(networkid,networkIdBf ,nodeid);
	}
	
	

	
	public List<PortInfo> FindAllUniqueLinePorts(int networkid, int nodeid)
	{
		return this.dao.findAllUniqueLinePorts(networkid, nodeid);
	}
	
	public List<PortInfo> FindAllUniqueTenGAggLinePorts(int networkid, int nodeid)
	{
		return this.dao.findAllUniqueTenGAggLinePorts(networkid, nodeid);
	}
	
	public List<PortInfo> FindAllChnlPtcLines(int networkid, int nodeid)
	{
		
		return this.dao.findAllChnlPtcLines(networkid,nodeid);
	}
	
	public List<PortInfo> FindAllChnlPtcLinesAddedBrField(int networkid,int networkidBf, int nodeid)
	{
		
		return this.dao.findAllChnlPtcLinesAddedBrField(networkid,networkidBf,nodeid);
	}
	
	public List<PortInfo> FindAllClientPtcLines(int networkid, int nodeid)
	{
		
		return this.dao.findAllClientPtcLines(networkid,nodeid);
	}
	
	public List<PortInfo> FindAllClientPtcLinesAddedBrField(int networkid,int networkidBf, int nodeid)
	{
		
		return this.dao.findAllClientPtcLinesAddedBrField(networkid,networkidBf,nodeid);
	}
	
	public List<PortInfo> FindAllClientUnPtcLines(int networkid, int nodeid)
	{
		
		return this.dao.findAllClientUnPtcLines(networkid,nodeid);
	}
	
	public List<PortInfo> FindAllClientUnPtcLinesAddedBrField(int networkid, int networkidBf,int nodeid)
	{
		
		return this.dao.findAllClientUnPtcLinesAddedBrField(networkid,networkidBf,nodeid);
	}
	
	public List<Map<String, Object>> TempFindAll(int networkid,int nodeid)
	{
		return this.dao.tempFindAll(networkid,nodeid);
	}
	public List<AggregatorClientMap> FindAggregatorLinePortMapping(int networkid, int nodeid)
	{
		return this.dao.findAggregatorLinePortMapping(networkid,nodeid);
	}
	public List <PortInfo> FindMpnToFdfMap(int networkid, int nodeid)
	{
		return this.dao.findMpnToFdfMapping(networkid, nodeid);
	}
	
}