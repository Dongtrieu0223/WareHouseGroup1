package utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import model.Config;
import model.Log;

public class ConvertModel {
	public static Config convertToConfig(ResultSet rs) throws SQLException {
		Config config = new Config();
		config.setNameConfig(rs.getString("name_config"));
		config.setNameDatabase(rs.getString("name_database"));
		config.setUserNameDatabase(rs.getString("username_database"));
		config.setPasswordDatabase(rs.getString("password_database"));
		if (rs.getString("query") != null) {
			config.setQuerySQL(rs.getString("query"));
		} else {
			config.setQuerySQL("");
		}
		if( rs.getString("hostname") != null) {
			config.setHostName(rs.getString("hostname"));
		} else {
			config.setHostName("");
		}
		config.setPort(rs.getInt("port"));
		

		if (rs.getString("username_account") != null) {
			config.setUserNameAccount(rs.getString("username_account"));
		} else {
			config.setUserNameAccount("");
		}

		if (rs.getString("password_account") != null) {
			config.setPasswordAccount(rs.getString("password_account"));
		} else {
			config.setPasswordAccount("");
		}

		if (rs.getString("file_format") != null) {
			config.setFileFormat(rs.getString("file_format"));
		} else {
			config.setFileFormat("");
		}

		if (rs.getString("remote_dir") != null) {
			config.setRemoteDir(rs.getString("remote_dir"));
		} else {
			config.setRemoteDir("");
		}

		if (rs.getString("local_dir") != null) {
			config.setLocalDir(rs.getString("local_dir"));
		} else {
			config.setLocalDir("");
		}

		return config;
	}

	public static Log convertToLog(ResultSet rs) throws SQLException {
		Log log = new Log();
		log.setId(rs.getInt("id_log"));

		if (rs.getString("urlLocal") != null) {
			log.setUrlLocal(rs.getString("urlLocal"));
		} else {
			log.setUrlLocal("");
		}

		if (rs.getString("status") != null) {
			log.setStatus(rs.getString("status"));
		} else {
			log.setStatus("");
		}

		return log;
	}

	

}
