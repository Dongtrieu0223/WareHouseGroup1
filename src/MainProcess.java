import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.stream.StreamSupport;

import model.Config;
import model.Log;
import task1.DownloadAndInsertLog;
import task2.UploadStaging;
import utils.ConnectDB;
import utils.ConvertModel;
import utils.SystemContain;

public class MainProcess {
	public static void main(String[] args) throws SQLException, IOException, ParseException {

		PreparedStatement pre = null;
		ResultSet rs = null;
		Connection conn = ConnectDB.getConnectDB("controll", "root", "");
		String sql = "SELECT * FROM config";
		pre = conn.prepareStatement(sql);

		rs = pre.executeQuery();

		ArrayList<Config> configs = new ArrayList<Config>();
		while (rs.next()) {
			configs.add(ConvertModel.convertToConfig(rs));
		}
		conn.close();
		// task 1
		DownloadAndInsertLog.downloadAndInsertLog(configs);
		// task 2 task 3
		
		//get all Log
		Config config_getAllLog = null;
		for (Config item : configs) {
			if(item.getNameConfig().equals(SystemContain.CONFIG_GET_LOG)) {
				config_getAllLog = item;
				break;
			}
		}
		conn = ConnectDB.getConnectDB(config_getAllLog.getNameDatabase(), config_getAllLog.getUserNameDatabase(),
				config_getAllLog.getPasswordDatabase());
		pre = conn.prepareStatement(config_getAllLog.getQuerySQL());

		rs = pre.executeQuery();

		ArrayList<Log> logs = new ArrayList<Log>();
		while (rs.next()) {
			logs.add(ConvertModel.convertToLog(rs));
		}
		conn.close();

		for (Log log : logs) {
			
			//excute task 2
			log.setStatus(UploadStaging.uploadStaging(configs, log));
			
			//excute task3
			if(log.getStatus().equals(SystemContain.UPLOAD_STAGING)){
			InsertWarehouse.uploadWarehouse(configs,log);
		}
	}
}
	public static String[] splitPath(String pathString) {
		Path path = Paths.get(pathString);
		return StreamSupport.stream(path.spliterator(), false).map(Path::toString).toArray(String[]::new);
	}

}