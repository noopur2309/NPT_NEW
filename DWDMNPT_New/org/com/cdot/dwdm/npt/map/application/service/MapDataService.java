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
import application.dao.CardInfoDao;
import application.dao.MapDataDao;
import application.model.Network;
import application.model.CardInfo;
import application.model.MapData;

@Service
public class MapDataService {

	@Autowired
	private MapDataDao dao;
	
	public List <MapData> FindAll()
	{
		return this.dao.findAll();
	}
	
	public void Insert(MapData map) throws SQLException{
		this.dao.insert(map);
		
	}
	
	public void Update(MapData map) throws SQLException{
		this.dao.update(map);
		
	}
	
	public void Delete(int networkid)
	{
		this.dao.deleteByNetworkId(networkid);
	}
	
	public MapData FindByNetworkId(int networkid)
	{
		return this.dao.findByNetworkId(networkid);
	}
	
	
	public int Count()
	{
		return this.dao.count();
	}
	
	public int Count(int networkId)
	{
		return this.dao.count(networkId);
	}

}