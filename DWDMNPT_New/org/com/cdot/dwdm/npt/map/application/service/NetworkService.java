/**
 * 
 */
/**
 * @author sunaina
 *
 */
package application.service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.dao.NetworkDao;
import application.dao.CardInfoDao;
import application.model.Network;
import application.model.CardInfo;

@Service
public class NetworkService {

	@Autowired
	private NetworkDao networkdao;
	
	public List <Network> FindAll(int networkId)
	{
		return this.networkdao.findAll(networkId);
	}
	
	public Network FindNetwork(int networkId)
	{
		return this.networkdao.findNetwork(networkId);
	}
	
	public List<Network> FindAllNetworks()
	{
		return this.networkdao.findAllNetworks();
	}

	public void InsertNetwork(Network network) throws SQLException{
		this.networkdao.insertNetwork(network);		
	}
	
	public void DeleteNetwork(int networkid)
	{
		this.networkdao.deleteByNetworkId(networkid);
	}
	
	public void DeleteWholeNetwork(int networkid)
	{
		this.networkdao.deleteWholeNetwork(networkid);
	}
	
	public int Count()
	{
		return this.networkdao.count();
	}
	
	public Object GetByNetworkName(String networkName)
	{
		return this.networkdao.getByNetworkName(networkName);
	}
	
	public Object NetworkNameExists(String networkName,String userName)
	{
		return this.networkdao.networkNameExists(networkName,userName);
	}
	
	public Object GetNextNetworkId()
	{
		return this.networkdao.getNextNetworkId();
	}
	
	public Network GetNetworkInfoByNetworkName(String networkName)
	{
		return this.networkdao.getNetworkInfoByNetworkName(networkName);
	}
	
	public Network GetNetworkInfoByBFNetworkId(int networkId)
	{
		return this.networkdao.getNetworkInfoByBFNetworkId(networkId);
	}
	
	public List <Network> FindNetworkByUserName(String username)
	{
		return this.networkdao.findNetworkByUserName(username);
	}
	
	public void Update(int networkid, int NetworkIdBrownField,Timestamp NetworkBrownFieldUpdateDate) throws SQLException
	{
		this.networkdao.update(networkid, NetworkIdBrownField, NetworkBrownFieldUpdateDate);
	}
	
	public Object GetBrownFieldNetworkId(int networkidGrField)
	{
		return this.networkdao.getBrownFieldNetworkId(networkidGrField);
	}
	
	public Object GetGreenFieldNetworkId(int networkidBrField)
	{
		return this.networkdao.getGreenFieldNetworkId(networkidBrField);
	}
	
	public boolean IsBrownFieldNetwork(int networkid)
	{
		return this.networkdao.isBrownFieldNetwork(networkid);
	}
	
	public void UpdateBrFieldId(int networkid,int val)
	{
		this.networkdao.updateBrFieldId(networkid,val);
	}
	
	public int UpdateSchema(String sql,String tableName)
	{
		return this.networkdao.updateSchema(sql, tableName);
	} 	
	
}