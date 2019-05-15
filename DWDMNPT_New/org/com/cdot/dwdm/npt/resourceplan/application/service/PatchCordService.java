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
import application.dao.PatchCordDao;
import application.model.Circuit;
import application.model.PatchCord;
import application.model.CardInfo;

@Service
public class PatchCordService{

	@Autowired
	private PatchCordDao dao;
	
	/*
	 * NodeKey = networkid+"_"+nodeid
	 * */
	public List<PatchCord> FindAll(int networkid, int nodeid)
	{
		return this.dao.findAll(networkid, nodeid);
	}
	
	public void Insert(PatchCord info) throws SQLException{
		this.dao.insert(info);
		
	}

	public int Count(int networkId)
	{
		return this.dao.count(networkId);
	}
	
	
	public void CopyPatchCordDataInBrField(int networkidGrField, int networkidBrField ) throws SQLException
	{
		this.dao.copyPatchCordDataInBrField( networkidGrField,  networkidBrField );
	}
	
	public int Count(int networkid,int nodeid)
	{
		return this.dao.count(networkid,nodeid);
	}
	
	public int CountCardByType(int networkid,int nodeid, String cardtype)
	{
		return this.dao.countCardByType(networkid,nodeid,cardtype);
	}
	
	public List<Map<String,Object>> CountCordByTypeNEId(int networkid,int nodeid)
	{
		return this.dao.countCordByTypeNEId(networkid, nodeid);
	}
	
	/*
	 * 
	 * Used to create the patchcord table in db
	 * Table name will be PatchCord_NetworkId_NodeId
	 * 
	 * * */
	public void CreatePatchCordTable(String nodekey)
	{
		this.dao.createPatchCordTable(nodekey);
	}
	
	public void DeletePatchCordInfo(int networkid)
	{
		this.dao.deletePatchCordInfo(networkid);
	}
	public void DeletePatchCordInfoby(int networkid,int nodeid)
	{
		this.dao.deletePatchCordInfoby(networkid,nodeid);
	}
	
	public List<Map<String, Object>> TempFindAll(int networkid,int nodeid)
	{
		return this.dao.tempFindAll(networkid,nodeid);
	}
		
}