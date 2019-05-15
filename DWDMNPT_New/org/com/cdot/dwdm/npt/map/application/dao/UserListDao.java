package application.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import application.model.Circuit;
import application.model.MapData;
import application.model.Network;
import application.model.UserList;
import application.constants.ResourcePlanConstants;
import application.model.CardInfo;

@Component
public class UserListDao{
	@Autowired
    private JdbcTemplate jdbcTemplate;
	public static Logger logger = Logger.getLogger(ResourcePlanConstants.DatabasePrintStr);
	
	
	 @SuppressWarnings({ "unchecked", "rawtypes" })
		public List<UserList> findAll(){
				String sql = "SELECT * FROM UserList";
				List<UserList> row  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(UserList.class));
				return row;
			}
	 
	 public void insert(final UserList list) throws SQLException {     
     String sql = "INSERT into UserList(Username, Password, Privilege) VALUES ( ?,?,?)";
     jdbcTemplate.update(
             sql,
             new Object[] { list.getUsername(), list.getPassword(), list.getPrivilege() });
	 }

	 public void deleteByNetworkId(int networkid) {	        
	        String sql = "delete from UserList where NetworkId = ?";
	        jdbcTemplate.update(sql, networkid);	      
	 }
	 
	 public int count()
	 {		
		 logger.info("Count UserList");
		  String sql = "select count(*) from UserList"; 
		  return jdbcTemplate.queryForObject(sql, int.class);		  		        
	 }
	 
	 public boolean checkIfUserExists(String User , String passwd)
	 {			
		 boolean flag = false;
		 UserList user;
		  String sql = "select * from UserList where UserName = ?  and Password = ? "; 		  
		  try
		  {
			  user = (UserList) jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper(UserList.class), User, passwd);
		  }
		  catch (EmptyResultDataAccessException e) 
		  {
			  user = null;
		  }
		  
		  if(user!=null)
		  {
			  flag = true;
			  return flag;
		  }
		  else
		  {
			  flag = false;
			  return flag;
		  }
	 }
	 
	 public boolean checkIfUserExists(String User)
	 {			
		 boolean flag = false;
		 UserList user;
		  String sql = "select * from UserList where UserName = ?"; 		  
		  try
		  {
			  user = (UserList) jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper(UserList.class), User);
		  }
		  catch (EmptyResultDataAccessException e) 
		  {
			  user = null;
		  }
		  
		  if(user!=null)
		  {
			  flag = true;
			  return flag;
		  }
		  else
		  {
			  flag = false;
			  return flag;
		  }
	 }
	 
	 
}
