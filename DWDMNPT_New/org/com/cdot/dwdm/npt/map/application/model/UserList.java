package application.model;

public class UserList {
	private String Username;
	private String Password;
	private String Privilege;
	public UserList() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getUsername() {
		return Username;
	}
	public void setUsername(String username) {
		Username = username;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public String getPrivilege() {
		return Privilege;
	}
	public void setPrivilege(String privilege) {
		Privilege = privilege;
	}
	@Override
	public String toString() {
		return "UserList [Username=" + Username + ", Password=" + Password + ", Privilege=" + Privilege + "]";
	}
	public UserList(String username, String password, String privilege) {
		super();
		Username = username;
		Password = password;
		Privilege = privilege;
	}
	
	
}
