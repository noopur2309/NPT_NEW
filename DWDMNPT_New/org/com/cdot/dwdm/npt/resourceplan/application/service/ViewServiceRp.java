/**
 * 
 */
/**
 * @author sunaina
 *
 */
package application.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.dao.ViewDaoRp;
import application.model.Bom;



@Service
public class ViewServiceRp {

	@Autowired
	private ViewDaoRp viewdao;
			
	public List<Bom> FindBoM(int networkid)
	{
		List<Bom> mapping = this.viewdao.findBom(networkid);		
		return mapping;
	}
	
	public List<Bom> FindBomNetworkWise(int networkid)
	{
		List<Bom> mapping = this.viewdao.findBomNetworkWise(networkid);		
		return mapping;
	}
	
	
	public List<Map<String, Object>> FindsumTotalPrice(int networkid, int nodeid)
	{
		List<Map<String, Object>> mapping = this.viewdao.findsumTotalPrice(networkid, nodeid);		
		return mapping;
	}

	public List<Map<String, Object>> FindTotalTypicalPower(int networkid,int nodeid)
	{
		List<Map<String, Object>> mapping = this.viewdao.findTotalTypicalPower(networkid,nodeid);		
		return mapping;
	}

	public List<Map<String, Object>> FindTotalPowerConsumption(int networkid,int nodeid)
	{
		List<Map<String, Object>> mapping = this.viewdao.findTotalPowerConsumption(networkid, nodeid);		
		return mapping;
	}
	
	public int MaxTotalPrice(int networkid)
	{
		return this.viewdao.maxnodecost(networkid);
	}

	public int MinTotalPrice(int networkid)
	{
		return this.viewdao.minnodecost(networkid);
	}
	public int TotalCost(int networkid)
	{
		return this.viewdao.totalcost(networkid);
	}
	
	public List<Bom> FindAddedBomEntriesInBrField(int networkidGrField, int networkidBrField, int nodeid)
	{
		return this.viewdao.findAddedBomEntriesInBrField(networkidGrField, networkidBrField, nodeid);
	}
	
	public List<Bom> FindModifiedBomEntriesInBrField(int networkidGrField, int networkidBrField, int nodeid)
	{
		return this.viewdao.findModifiedBomEntriesInBrField(networkidGrField, networkidBrField, nodeid);
	}
	
	public List<Bom> FindCommonBomEntriesInBrField(int networkidGrField, int networkidBrField, int nodeid)
	{
		return this.viewdao.findCommonBomEntriesInBrField(networkidGrField, networkidBrField, nodeid);
	}

	public List<Bom> FindDeletedBomEntriesInBrField(int networkidGrField, int networkidBrField, int nodeid)
	{
		return this.viewdao.findDeletedBomEntriesInBrField(networkidGrField, networkidBrField, nodeid);
	}
	
	public List<Bom> FindAddedBomEntriesInBrField(int networkidGrField, int networkidBrField)
	{
		return this.viewdao.findAddedBomEntriesInBrField(networkidGrField, networkidBrField);
	}
	
	public List<Bom> FindModifiedBomEntriesInBrField(int networkidGrField, int networkidBrField)
	{
		return this.viewdao.findModifiedBomEntriesInBrField(networkidGrField, networkidBrField);
	}
	
	public List<Bom> FindCommonBomEntriesInBrField(int networkidGrField, int networkidBrField)
	{
		return this.viewdao.findCommonBomEntriesInBrField(networkidGrField, networkidBrField);
	}

	public List<Bom> FindDeletedBomEntriesInBrField(int networkidGrField, int networkidBrField)
	{
		return this.viewdao.findDeletedBomEntriesInBrField(networkidGrField, networkidBrField);
	}
	public int FindTotalPower(int networkId)
	{
		return  this.viewdao.findTotalPower(networkId);
	}
	public int FindTotalPrice(int networkId)
	{
		return  this.viewdao.findTotalPrice(networkId);
	}

	
}