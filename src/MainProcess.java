import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import model.Config;
import model.Log;
import task1.DownloadAndInsertLog;
import task2.UploadStaging;
import task3.UploadWarehouse;
import utils.ConnectDB;
import utils.ConvertModel;
import utils.SystemContain;

public class MainProcess extends TimerTask{
	private String[] args;
	
	public MainProcess(String[] args) {
		this.args = args;
	}
	
	@Override
	public void run(){
		PreparedStatement pre = null;
		ResultSet rs = null;
		String sql = null;
		
		Connection conn = ConnectDB.getConnectDB();
		for (String id_Config : args) {
			System.out.println(id_Config);

			sql = "SELECT * FROM control.config WHERE id_config = " + id_Config;
			Config config = new Config();
			try {
				pre = conn.prepareStatement(sql);
				rs = pre.executeQuery();
				while (rs.next()) {
					config = ConvertModel.convertToConfig(rs);
				}
				
				DownloadAndInsertLog.downloadAndInsertLog(config, conn);
				
				String nameLog = config.getName_Config();
				sql = "SELECT * FROM control.log WHERE name_log = '" + nameLog+"'";
				pre = conn.prepareStatement(sql);
				
				rs = pre.executeQuery();
				ArrayList<Log> logs = new ArrayList<Log>();
				while (rs.next()) {
					logs.add(ConvertModel.convertToLog(rs));
				}
				for (Log log : logs) {
					log.setStatus(UploadStaging.uploadStaging(config, log, conn));
					UploadWarehouse.uploadWarehouse(config, log, conn);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	public static void main(String[] args)  {
		MainProcess process = new MainProcess(args);
		Timer timer = new Timer();
	    timer.schedule(process, 0, 30000);

	}

}