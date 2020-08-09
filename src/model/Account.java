package model;

public class Account {
	private String userName;
	private String password;
	public Account(String userName, String password) {
		super();
		this.userName = userName;
		this.password = password;
	}
	
	public Account() {}
	public String getUserName() {
		return userName;
	}
	public String getPassword() {
		return password;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
