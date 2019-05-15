/**
 * @author hp
 * @date 24th May, 2018
 */
package application.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.annotation.security.DenyAll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.dao.LambdaLspInformationDao;
import application.model.LambdaLspInformation;


@Service
public class LambdaLspInformationService{

	@Autowired
	private LambdaLspInformationDao LambdalspInformationDao;

	
	public List<LambdaLspInformation> FindAll(int networkid)
	{
		return this.LambdalspInformationDao.findAll(networkid);
	}

	public LambdaLspInformation FindLsp(int networkid, int demandId, String Path)
	{
		return this.LambdalspInformationDao.findLsp(networkid, demandId, Path);
	}
	
	public LambdaLspInformation FindLSP(int networkid, int ForwardingAdjacency)
	{
		return this.LambdalspInformationDao.findLSP( networkid, ForwardingAdjacency);
	}

	public void InsertLambdaLspInformation(LambdaLspInformation LambdalspInformation) throws SQLException{
		this.LambdalspInformationDao.insertLambdaLspInformation(LambdalspInformation);
		
	}

//	public void UpdateLambdaLspInformation(LambdaLspInformation LambdalspInformation) throws SQLException{
//		this.LambdalspInformationDao.updateLambdaLspInformation(LambdalspInformation);
//		
//	}	
	public void DeleteLambdaLspInformation(int networkid)
	{
		this.LambdalspInformationDao.deleteByNetworkId(networkid);
	}
	
	public void DeleteLambdaLspInformation(int networkid, int demandid)
	{
		this.LambdalspInformationDao.deleteLambdaLspInformation(networkid, demandid);
	}

	public Map<String,Object> FindMaxTunnelIdLambdaLspInformation(int  networkid) throws SQLException{
		return this.LambdalspInformationDao.findMaxTunnelIdLambdaLspInformation(networkid);		
	}


	public void CopyLambdaLspInformationInBrField(int networkidGrField, int networkidBrField ) throws SQLException{
		this.LambdalspInformationDao.copyLambdaLspInformationInBrField(networkidGrField, networkidBrField);		
	}

	public List<LambdaLspInformation> FindAddedLambdaLspInBrField(int networkidGrField, int networkidBrField)
	{
		return this.LambdalspInformationDao.findAddedLambdaLspInBrField(networkidGrField, networkidBrField);	
	}

		
	public int Count(int networkId)
	{
		return this.LambdalspInformationDao.count(networkId);
	}

	public int FindMaxTunnelIdLambdaLspInformation(int  brownFieldNetworkid, int  greenFieldNetworkid, int demandId/*, int  wavelengthNo*/) throws SQLException{
		return this.LambdalspInformationDao.findMaxTunnelIdLambdaLspInformation(brownFieldNetworkid,greenFieldNetworkid, demandId/*, wavelengthNo*/);		
	}
	
	public List<LambdaLspInformation> FindDeletedLambdaLspInBrField(int networkidGrField, int networkidBrField, int ...sourceNodeId)
	{
		return this.LambdalspInformationDao.findDeletedLambdaLspInBrField(networkidGrField, networkidBrField, sourceNodeId);	
	}
	
	public int UpdateSchema(String sql)
	{
		return this.LambdalspInformationDao.updateSchema(sql);
	} 
	
	public int FindMaxForwardingAdjLambdaLspInformation(int  brownFieldNetworkid, int  greenFieldNetworkid) throws SQLException{
		return this.LambdalspInformationDao.findMaxForwardingAdjLambdaLspInformation(brownFieldNetworkid,greenFieldNetworkid);		
	}
		
}