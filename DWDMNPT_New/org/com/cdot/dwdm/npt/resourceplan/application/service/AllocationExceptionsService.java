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

import application.dao.AllocationExceptionsDao;
import application.dao.CardPreferenceDao;
import application.model.AllocationExceptions;
import application.model.CardPreference;

@Service
public class AllocationExceptionsService{

	@Autowired
	private AllocationExceptionsDao dao;
	
	public List<AllocationExceptions> Find(int networkid) throws SQLException
	{
		return this.dao.find(networkid);
	}
		
	public AllocationExceptions Find(int networkid, int nodeid, String cardtype, String srno) throws SQLException
	{
		return this.dao.find(networkid, nodeid, cardtype, srno);
	}
	
	public List<AllocationExceptions> FindExceptions(int networkid, int nodeid, String type) throws SQLException
	{
		return this.dao.findExceptions(networkid, nodeid, type);
	}
	
//	public void UpdateStatus(AllocationExceptions info) throws SQLException
//	{
//		this.dao.updateStatus(info);
//	}
	
	public void Insert(AllocationExceptions info) throws SQLException
	{
		this.dao.insert(info);
	}
		
	public void DeleteByNetworkId(int networkid) 
	{
		this.dao.deleteByNetworkId(networkid);		
	}
	
	public int Count(int networkId)
	{
		return this.dao.count(networkId);
	}
	
	public void InitializeParamPrefbrField(int networkId,int networkidBf) throws SQLException
	{
		this.dao.initializeParamPrefbrField(networkId,networkidBf);
	}
	
	public List<AllocationExceptions> FindAddedExceptionsBrField(int networkidGf,int networkidBf, int nodeid) throws SQLException
	{
		return this.dao.findAddedExceptionsBrField(networkidGf,networkidBf , nodeid);
	}
	
	public List<AllocationExceptions> FindDeletedExceptionsBrField(int networkidGf,int networkidBf, int nodeid) throws SQLException
	{
		return this.dao.findDeletedExceptionsBrField(networkidGf,networkidBf , nodeid);
	}
		
	
}