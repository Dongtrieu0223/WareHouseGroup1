package model;

public class Config {
	private String nameConfig;
	private String nameDatabase;
	private String userNameDatabase;
	private String passwordDatabase;
	private String querySQL;
	private String hostName;
	private int port;
	private String userNameAccount;
	private String passwordAccount;
	private String fileFormat;
	private String remoteDir;
	private String localDir;
	
	
	public String getFileFormat() {
		return fileFormat;
	}
	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}
	public String getRemoteDir() {
		return remoteDir;
	}
	public void setRemoteDir(String remoteDir) {
		this.remoteDir = remoteDir;
	}
	public String getLocalDir() {
		return localDir;
	}
	public void setLocalDir(String localDir) {
		this.localDir = localDir;
	}
	
	public String getNameDatabase() {
		return nameDatabase;
	}
	public void setNameDatabase(String nameDatabase) {
		this.nameDatabase = nameDatabase;
	}
	public String getUserNameDatabase() {
		return userNameDatabase;
	}
	public void setUserNameDatabase(String userNameDatabase) {
		this.userNameDatabase = userNameDatabase;
	}

	
	public String getPasswordDatabase() {
		return passwordDatabase;
	}
	public void setPasswordDatabase(String passwordDatabase) {
		this.passwordDatabase = passwordDatabase;
	}
	public String getQuerySQL() {
		return querySQL;
	}
	public void setQuerySQL(String querySQL) {
		this.querySQL = querySQL;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getUserNameAccount() {
		return userNameAccount;
	}
	public void setUserNameAccount(String userNameAccount) {
		this.userNameAccount = userNameAccount;
	}
	public String getPasswordAccount() {
		return passwordAccount;
	}
	public void setPasswordAccount(String passwordAccount) {
		this.passwordAccount = passwordAccount;
	}
	public String getNameConfig() {
		return nameConfig;
	}
	public void setNameConfig(String nameConfig) {
		this.nameConfig = nameConfig;
	}
	
}
