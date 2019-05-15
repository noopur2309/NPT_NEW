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
import application.dao.UserListDao;
import application.dao.CardInfoDao;
import application.model.Network;
import application.model.UserList;
import application.model.CardInfo;

@Service
public class UserListService {

	@Autowired
	private UserListDao dao;
	
	public List <UserList> FindAll()
	{
		return this.dao.findAll();
	}	
	
	public void Insert(UserList list) throws SQLException{
		this.dao.insert(list);		
	}
	
	public void Delete(int networkid)
	{
		this.dao.deleteByNetworkId(networkid);
	}
	
	public int Count()
	{
		return this.dao.count();
	}
	
	public boolean CheckIfUserExists(String User, String passwd)
	{
		return this.dao.checkIfUserExists(User, passwd);
	}
	
	public boolean CheckIfUserExists(String User)
	{
		return this.dao.checkIfUserExists(User);
	}
	
	
}