/**
 * @author hp
 * @date 1st Jun, 2018
 */
package application.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.annotation.security.DenyAll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.dao.OtnLspInformationDao;
import application.model.DemandMapping;
import application.model.OtnLspInformation;


@Service
public class OtnLspInformationService{

	@Autowired
	private OtnLspInformationDao OtnlspInformationDao;

	
	public List<OtnLspInformation> FindAll(int networkid)
	{
		return this.OtnlspInformationDao.findAll(networkid);
	}

	public OtnLspInformation FindLsp(int networkid, int demandId, String circuitId, String Path, String TrafficType,int RoutePriority, int typeOfOtnLsp)
	{
		return this.OtnlspInformationDao.findLsp(networkid, demandId, circuitId, Path,TrafficType, RoutePriority, typeOfOtnLsp);
	}

	public void InsertOtnLspInformation(OtnLspInformation OtnlspInformation) throws SQLException{
		this.OtnlspInformationDao.insertOtnLspInformation(OtnlspInformation);
		
	}

	public void UpdateOtnLspInformationFA(OtnLspInformation OtnlspInformation) throws SQLException{
		this.OtnlspInformationDao.updateOtnLspInformationFA(OtnlspInformation);		
	}	
	
	public void DeleteOtnLspInformation(int networkid)
	{
		this.OtnlspInformationDao.deleteByNetworkId(networkid);
	}

	public int FindMaxTunnelIdOtnLspInformation(int  brownFieldNetworkid, int  greenFieldNetworkid, String circuitId, String trafficType, int typeOfOtnLSP) throws SQLException{
		return this.OtnlspInformationDao.findMaxTunnelIdOtnLspInformation(brownFieldNetworkid,greenFieldNetworkid, circuitId, trafficType, typeOfOtnLSP);		
	}


	public void CopyOtnLspInformationInBrField(int networkidGrField, int networkidBrField ) throws SQLException{
		this.OtnlspInformationDao.copyOtnLspInformationInBrField(networkidGrField, networkidBrField);		
	}

	public List<OtnLspInformation> FindAddedOtnLspInBrField(int networkidGrField, int networkidBrField)
	{
		return this.OtnlspInformationDao.findAddedOtnLspInBrField(networkidGrField, networkidBrField);	
	}

	public List<OtnLspInformation> FindCommonOtnLspInBrField(int networkidGrField, int networkidBrField)
	{
		return this.OtnlspInformationDao.findCommonOtnLspInBrField(networkidGrField, networkidBrField);	
	}	

	public int Count(int networkId)
	{
		return this.OtnlspInformationDao.count(networkId);
	}

	public void DeleteSpecificOtnLsp(int networkid, DemandMapping demandMappingOBJ)
	{
		this.OtnlspInformationDao.deleteSpecificOtnLsp(networkid, demandMappingOBJ);
	}

	public  List<Map<String, Object>> FindDeletedOtnLspInBrField(int networkidGrField, int networkidBrField, int ...sourceNode )
	{
		return this.OtnlspInformationDao.findDeletedOtnLspInBrField(networkidGrField, networkidBrField, sourceNode);	
	}
	
	public  List<Map<String, Object>> FindModifiedOtnLspInBrField(int networkidGrField, int networkidBrField, int ...sourceNode )
	{
		return this.OtnlspInformationDao.findModifiedOtnLspInBrField(networkidGrField, networkidBrField, sourceNode);	
	}
	public OtnLspInformation FindLSP(int networkid, int demandId, int TunnelId, int LSPId) {
		return this.OtnlspInformationDao.findLSP(networkid, demandId, TunnelId, LSPId);
	}

	public void UpdateCircuitID(int networkid, int OtnLspTunnelId, String CircuitID) throws SQLException {
		this.OtnlspInformationDao.updatecircuitID(networkid, OtnLspTunnelId, CircuitID);
	}
	
	public  OtnLspInformation FindLspFor10GAgg(int networkId, int demandId, String CircuitId, String Path,
			String TrafficType, int RoutePriority, int typeOfOtnLSP)
	{
		return this.OtnlspInformationDao.findLspFor10GAgg( networkId,  demandId,  CircuitId,  Path,
				 TrafficType,  RoutePriority,  typeOfOtnLSP);	
	}	
	public List<OtnLspInformation> FindCircuitIdByTunnelId(int networkId, int OtnLspTunnelId)
	{
		return this.OtnlspInformationDao.findCircuitIdByTunnelId( networkId, OtnLspTunnelId);
	}
	public void DeleteSpecificOtnLsp(int networkId, int DemandId,int OtnLspTunnelId,int LSPId)
	{
		 this.OtnlspInformationDao.deleteSpecificOtnLsp( networkId,DemandId, OtnLspTunnelId,LSPId);
	}
	
	public OtnLspInformation FindLsp(int networkid, int demandId, String Path, int RoutePriority, int otnLspTunnelId, int lspId)
	{
		return this.OtnlspInformationDao.findLsp(networkid, demandId, Path, RoutePriority, otnLspTunnelId, lspId);
	}
	
	public int FindMaxFA(int networkid, OtnLspInformation otnLspInformationObj)
	{
		return this.OtnlspInformationDao.findMaxFA(networkid, otnLspInformationObj);
	}
		
}