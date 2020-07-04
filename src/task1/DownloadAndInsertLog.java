package task1;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.chilkatsoft.CkGlobal;
import com.chilkatsoft.CkScp;
import com.chilkatsoft.CkSsh;

import model.Config;
import model.Log;
import utils.ConnectDB;
import utils.SendEmail;
import utils.SystemContain;

public class DownloadAndInsertLog {

	static {
		try {
			System.load ("C:/Users\\WIN10/Desktop/chilkat-9.5.0-jdk8-x64/chilkat.dll");
		} catch (UnsatisfiedLinkError e) {
			System.err.println("Native code library failed to load.\n" + e);
			System.exit(1);
		}
	}

	public static boolean downloadAndInsertLog(ArrayList<Config> configs) throws SQLException {
		Config config_getSource = null;
		for(Config item : configs) {
			if(item.getNameConfig().equals(SystemContain.CONFIG_SOURCE)) {
				config_getSource = item;
				break;			}
		}
		
		
		CkGlobal glob = new CkGlobal();
		glob.UnlockBundle("Waiting . . .");
		CkSsh ssh = new CkSsh();
		// Connect to an SSH server:
		String hostname = config_getSource.getHostName();
		int port = config_getSource.getPort();
		boolean success = ssh.Connect(hostname, port);
		
		// Wait a max of 5 seconds when reading responses..
		ssh.put_IdleTimeoutMs(5000);
		// Authenticate using login/password:
		success = ssh.AuthenticatePw(config_getSource.getUserNameAccount(), config_getSource.getPasswordAccount());
		if (success != true) {
			
			SendEmail.send("Authenticate ERROR", "Authenticate error");
			return false;
		}
		// Once the SSH object is connected and authenticated, use it
		// in the SCP object.
		CkScp scp = new CkScp();
		success = scp.UseSsh(ssh);
		if (success != true) {
			StringBuffer mess = new StringBuffer();
			mess.append("CkScp can not create");
			mess.append("Please try again");
			SendEmail.send("CkScp can not create", mess.toString());
			return false;
		}
		
		String fileFormat[]  = config_getSource.getFileFormat().split(",");
		
		for (String item : fileFormat) {
			scp.put_SyncMustMatch(item);
			String remoteDir = config_getSource.getRemoteDir();
			String localDir = config_getSource.getLocalDir();
			// Download synchronization modes:
			// mode=0: Download all files
			// mode=1: Download all files that do not exist on the local filesystem.
			// mode=2: Download newer or non-existant files.
			// mode=3: Download only newer files.
			// mode=5: Download only missing files or files with size differences.
			// mode=6: Same as mode 5, but also download newer files.
			int mode = 0;
			boolean bRecurse = false;
			success = scp.SyncTreeDownload(remoteDir, localDir, mode, bRecurse);
			if (success != true) {
				StringBuffer mess = new StringBuffer();
				mess.append("File can not download");
				mess.append("Please try again");
				SendEmail.send("DOWNLOAD file failed", mess.toString());
				return false;
			}
		}
		ssh.Disconnect();
		
		// insert Log
		
		String localFile = config_getSource.getLocalDir();
		Config config_insertLog = null;
		for(Config item : configs) {
			if(item.getNameConfig().equals(SystemContain.CONFIG_INSERT_LOG)) {
				config_insertLog = item;
				break;
			}
		}
		
		Connection conn = ConnectDB.getConnectDB(config_insertLog.getNameDatabase(), config_insertLog.getUserNameDatabase(),
				config_insertLog.getPasswordDatabase());
		File folder = new File(localFile);
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
			if (file.isFile()) {
				Log log = new Log();
				log.setUrlLocal(localFile + File.separator + file.getName());
				log.setStatus(SystemContain.DOWNLOAD_SUCCESS);

				insert(config_insertLog.getQuerySQL(), log, conn);
			}
		}
		conn.close();
		
		return true;

	}
	
	private static void insert(String sql, Log log, Connection conn) {
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, log.getUrlLocal());
			ps.setString(2, log.getStatus());
			
			ps.execute();
		} catch (SQLException e) {
		}
		
	}
}
