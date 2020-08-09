package task3;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import model.Config;
import model.DangKy;
import model.Log;
import model.LopHoc;
import model.MonHoc;
import model.Student;
import utils.SendEmail;
import utils.SystemContain;

public class UploadWarehouse {

	public static void uploadWarehouse(Config config, Log log, Connection conn) throws SQLException {
		String statusLog = log.getStatus();
		if (statusLog.equals(SystemContain.UPLOAD_STAGING)) {

			boolean load_succes = false;
			
			String nameLog = log.getNameLog();

			if (nameLog.equals(SystemContain.LOAD_STUDENT)) {
				load_succes = loadStudent(config, log.getId(), conn);
			} else if (nameLog.equals(SystemContain.LOAD_MONHOC)) {
				load_succes = loadMonHoc(config, log.getId(), conn);
			} else if (nameLog.equals(SystemContain.LOAD_LOPHOC)) {
				load_succes = loadLopHoc(config, log.getId(), conn);
			} else if (nameLog.equals(SystemContain.LOAD_DANGKY)) {
				load_succes = loadDangKy(config, log.getId(), conn);
			}

			// xoa du lieu o staging
			String sql_delete = "DELETE FROM " + config.getName_db_Staging() + "." + config.getName_table_staging()
					+ " WHERE id_log= ?";
			PreparedStatement pre = conn.prepareStatement(sql_delete);
			pre.setInt(1, log.getId());
			pre.execute();

			if (load_succes == true) {
				log.setStatus(SystemContain.UPLOAD_WAREHOUSE);
				java.util.Date nowDate = new java.util.Date();
				DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				log.setTime_Warehouse(formatDate.format(nowDate));
				log.setComment("File da duoc upload qua Warehouse");
				update(log, conn, config);

				SendEmail.send("File da duoc upload warehouse", "File da duoc upload warehouse");
			} else {
				log.setStatus(SystemContain.ERROR);
				java.util.Date nowDate = new java.util.Date();
				DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				log.setComment("File bi loi khi upload qua Warehouse");
				log.setTime_Warehouse(formatDate.format(nowDate));
				update(log, conn, config);

				SendEmail.send("File bi loi khi upload qua Warehouse", "File bi loi khi upload qua Warehouse");
			}
		}
	}

	private static boolean loadStudent(Config config, int id_log, Connection conn) {
		String nameDB = null;
		String tableDB = null;

		nameDB = config.getName_db_Staging();
		tableDB = config.getName_table_staging();

		ArrayList<Student> students = new ArrayList<Student>();
		students = getSinhVien(nameDB, tableDB, id_log, conn);

		int load_succes = 0;
		nameDB = config.getName_db_Warehouse();
		tableDB = config.getName_table_warehouse();
		for (Student sv : students) {
			boolean kt = insertSinhVien(sv, nameDB, tableDB, conn);
			if (kt == true)
				load_succes++;
		}
		if (load_succes == 0)
			return false;
		return true;
	}

	private static boolean loadMonHoc(Config config, int id_log, Connection conn) {
		String nameDB = null;
		String tableDB = null;

		nameDB = config.getName_db_Staging();
		tableDB = config.getName_table_staging();
		ArrayList<MonHoc> monhocs = new ArrayList<MonHoc>();
		monhocs = getMonHoc(nameDB, tableDB, id_log, conn);
		int load_succes = 0;
		nameDB = config.getName_db_Warehouse();
		tableDB = config.getName_table_warehouse();
		for (MonHoc sv : monhocs) {
			boolean kt = insertMonHoc(sv, nameDB, tableDB, conn);
			if (kt == true)
				load_succes++;
		}
		if (load_succes == 0)
			return false;
		return true;
	}

	private static boolean loadLopHoc(Config config, int id_log, Connection conn) {
		String nameDB = null;
		String tableDB = null;

		nameDB = config.getName_db_Staging();
		tableDB = config.getName_table_staging();
		ArrayList<LopHoc> lophocs = new ArrayList<LopHoc>();
		lophocs = getLopHoc(nameDB, tableDB, id_log, conn);
		int load_succes = 0;
		nameDB = config.getName_db_Warehouse();
		tableDB = config.getName_table_warehouse();
		for (LopHoc sv : lophocs) {
			boolean kt = insertLopHoc(sv, nameDB, tableDB, conn);
			if (kt == true)
				load_succes++;
		}
		if (load_succes == 0)
			return false;
		return true;
	}

	private static boolean loadDangKy(Config config, int id_log, Connection conn) {
		String nameDB = null;
		String tableDB = null;

		nameDB = config.getName_db_Staging();
		tableDB = config.getName_table_staging();
		ArrayList<DangKy> lstDangKy = new ArrayList<DangKy>();
		lstDangKy = getDangKy(nameDB, tableDB, id_log, conn);
		int load_succes = 0;
		nameDB = config.getName_db_Warehouse();
		tableDB = config.getName_table_warehouse();
		for (DangKy dk : lstDangKy) {
			boolean kt = insertDangKy(dk, nameDB, tableDB, conn);
			if (kt == true)
				load_succes++;
		}
		if (load_succes == 0)
			return false;
		return true;
	}

	private static ArrayList<Student> getSinhVien(String nameDB, String tableDB, int id_log, Connection conn) {
		ArrayList<Student> students = null;
		String sql = "SELECT * from " + nameDB + "." + tableDB + " WHERE id_log = ?";
		PreparedStatement pre = null;
		ResultSet rs = null;

		try {
			students = new ArrayList<Student>();

			pre = conn.prepareStatement(sql);
			pre.setInt(1, id_log);
			rs = pre.executeQuery();

			while (rs.next()) {
				if (convertToStudent(rs) != null) {
					students.add(convertToStudent(rs));
				}
			}

		} catch (SQLException e) {
			return null;
		}

		return students;
	}

	private static ArrayList<MonHoc> getMonHoc(String nameDB, String tableDB, int id_log, Connection conn) {
		ArrayList<MonHoc> lstMonhoc = null;
		String sql = "SELECT * from " + nameDB + "." + tableDB + " WHERE id_log = ?";
		PreparedStatement pre = null;
		ResultSet rs = null;

		try {
			lstMonhoc = new ArrayList<MonHoc>();

			pre = conn.prepareStatement(sql);
			pre.setInt(1, id_log);
			rs = pre.executeQuery();

			while (rs.next()) {
				if (convertToMonHoc(rs) != null) {
					lstMonhoc.add(convertToMonHoc(rs));
				}
			}

		} catch (SQLException e) {
			return null;
		}

		return lstMonhoc;
	}

	private static ArrayList<LopHoc> getLopHoc(String nameDB, String tableDB, int id_log, Connection conn) {
		ArrayList<LopHoc> lstLophoc = null;
		String sql = "SELECT * from " + nameDB + "." + tableDB + " WHERE id_log = ?";
		PreparedStatement pre = null;
		ResultSet rs = null;

		try {
			lstLophoc = new ArrayList<LopHoc>();

			pre = conn.prepareStatement(sql);
			pre.setInt(1, id_log);
			rs = pre.executeQuery();

			while (rs.next()) {
				if (convertToClass(rs) != null) {
					lstLophoc.add(convertToClass(rs));
				}
			}

		} catch (SQLException e) {
			return null;
		}

		return lstLophoc;
	}

	private static ArrayList<DangKy> getDangKy(String nameDB, String tableDB, int id_log, Connection conn) {
		ArrayList<DangKy> lstDangky = null;
		String sql = "SELECT * from " + nameDB + "." + tableDB + " WHERE id_log = ?";
		PreparedStatement pre = null;
		ResultSet rs = null;

		try {
			lstDangky = new ArrayList<DangKy>();

			pre = conn.prepareStatement(sql);
			pre.setInt(1, id_log);
			rs = pre.executeQuery();

			while (rs.next()) {
				if (convertToDangKy(rs) != null) {
					lstDangky.add(convertToDangKy(rs));
				}
			}

		} catch (SQLException e) {
			return null;
		}

		return lstDangky;
	}

	public static Student convertToStudent(ResultSet rs) throws SQLException {
		Student student = null;
		try {
			student = new Student();

			student.setIdLog((rs.getInt("id_log")));
			student.setStt(rs.getInt("STT"));
			student.setMssv(Integer.parseInt((rs.getString("MSSV"))));
			student.setHo(rs.getString("ho"));
			student.setTen(rs.getString("ten"));
			student.setDob(Date.valueOf(rs.getString("dob")));
			student.setLop(rs.getString("lop"));
			student.setTenlop(rs.getString("tenlop"));
			student.setSdt(rs.getString("sdt"));
			student.setEmail(rs.getString("email"));
			student.setQuequan(rs.getString("quequan"));
			student.setGhichu(rs.getString("ghichu"));
		} catch (Exception e) {
			return null;
		}

		return student;
	}

	public static MonHoc convertToMonHoc(ResultSet rs) throws SQLException {
		MonHoc monhoc = null;
		try {
			monhoc = new MonHoc();

			monhoc.setId((rs.getInt("id")));
			monhoc.setSTT(rs.getInt("STT"));
			monhoc.setMaMH(rs.getInt("maMH"));
			monhoc.setTenMH(rs.getString("tenMH"));
			monhoc.setTC(rs.getInt("TC"));
			monhoc.setKhoa_BoMon(rs.getString("khoa_BoMon"));
			monhoc.setId_log(rs.getInt("id_log"));

		} catch (Exception e) {
			return null;
		}

		return monhoc;
	}

	public static LopHoc convertToClass(ResultSet rs) throws SQLException {
		LopHoc lophoc = null;
		try {
			lophoc = new LopHoc();
			lophoc.setIdLog((rs.getInt("id_log")));
			lophoc.setMaLophoc((rs.getString("maLopHoc")));
			lophoc.setMaMonHoc(Integer.parseInt(rs.getString("maMH")));
			lophoc.setNamHoc(Integer.parseInt(rs.getString("namHoc")));

		} catch (Exception e) {
			return null;
		}

		return lophoc;
	}

	public static DangKy convertToDangKy(ResultSet rs) throws SQLException {
		DangKy dk = null;
		try {
			dk = new DangKy();
			dk.setIdLog((rs.getInt("id_log")));
			dk.setMaDangKy((rs.getString("maDangKy")));
			dk.setMaSinhvien(Integer.parseInt(rs.getString("maSinhvien")));
			dk.setMaLophoc(Integer.parseInt(rs.getString("maLophoc")));
			dk.setThoigianDK(Date.valueOf(rs.getString("thoigianDK")));

		} catch (Exception e) {
			return null;
		}

		return dk;
	}

	private static void update(Log log, Connection conn, Config config) {
		String nameDB = config.getName_db_Control();
		String nameTableDB = config.getName_table_log();
		String sql = "UPDATE " + nameDB + "." + nameTableDB
				+ " SET status = ?, comment = ?, time_uploadWarehouse = ? WHERE id_log = ?";
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, log.getStatus());
			ps.setString(2, log.getComment());
			ps.setString(3, log.getTime_Warehouse());
			ps.setInt(4, log.getId());
			ps.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}

	}

	private static boolean insertSinhVien(Student sv, String nameDB, String nameTableDB, Connection conn) {
		String sql_getDateDim = "Select date_sk from warehouse.date_dim Where full_date = ?";
		String sql = null;
		PreparedStatement pre = null;
		ResultSet rs = null;
		sql = "SELECT * from " + nameDB + "." + nameTableDB + " WHERE MSSV = ?";

		try {

			pre = conn.prepareStatement(sql);
			pre.setInt(1, sv.getMssv());
			rs = pre.executeQuery();

			while (rs.next()) {
				sql = "UPDATE " + nameDB + "." + nameTableDB + " SET dt_expire = ? WHERE id = ?";
				PreparedStatement ps;
				try {
					ps = conn.prepareStatement(sql);
					ps.setDate(1, Date.valueOf("2003-12-12"));
					ps.setInt(2, rs.getInt(1));
					ps.execute();
				} catch (SQLException x) {
				}
			}

		} catch (SQLException e) {

		}

		sql = "INSERT INTO " + nameDB + "." + nameTableDB
				+ " (MSSV, ho, ten, dob, dob_sk, lop, tenlop, sdt, email, quequan, ghichu, dt_expire) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

		try {
			pre = conn.prepareStatement(sql);
			pre.setInt(1, sv.getMssv());
			pre.setString(2, sv.getHo());
			pre.setString(3, sv.getTen());
			pre.setDate(4, sv.getDob());

			PreparedStatement ps = conn.prepareStatement(sql_getDateDim);
			ps.setString(1, String.valueOf(sv.getDob()));
			ResultSet r = ps.executeQuery();
			while (r.next()) {
				pre.setInt(5, r.getInt(1));
			}

			pre.setString(6, sv.getLop());
			pre.setString(7, sv.getTenlop());
			pre.setString(8, sv.getSdt());
			pre.setString(9, sv.getEmail());
			pre.setString(10, sv.getQuequan());
			pre.setString(11, sv.getGhichu());

			pre.setDate(12, null);
			pre.execute();
		} catch (SQLException e) {
			return false;
		}
		return true;
	}

	private static boolean insertMonHoc(MonHoc sv, String nameDB, String nameTableDB, Connection conn) {
		String sql = null;
		PreparedStatement pre = null;
		ResultSet rs = null;
		sql = "SELECT * from " + nameDB + "." + nameTableDB + " WHERE STT = ?";

		try {

			pre = conn.prepareStatement(sql);
			pre.setInt(1, sv.getSTT());
			rs = pre.executeQuery();

			while (rs.next()) {
				sql = "UPDATE " + nameDB + "." + nameTableDB + " SET dt_expire = ? WHERE id = ?";
				PreparedStatement ps;
				try {
					ps = conn.prepareStatement(sql);
					ps.setDate(1, Date.valueOf("2003-12-12"));
					ps.setInt(2, rs.getInt(1));
					ps.execute();
				} catch (SQLException e) {
				}
			}

		} catch (SQLException e) {

		}

		sql = "INSERT INTO " + nameDB + "." + nameTableDB
				+ " (STT , maMH , tenMH , TC , khoa_BoMon , dt_expire) VALUES (?,?,?,?,?,?)";
		try {
			pre = conn.prepareStatement(sql);
			pre.setInt(1, sv.getSTT());
			pre.setInt(2, sv.getMaMH());
			pre.setString(3, sv.getTenMH());
			pre.setInt(4, sv.getTC());
			pre.setString(5, sv.getKhoa_BoMon());
			pre.setDate(6, null);

			pre.execute();

		} catch (SQLException e) {
			return false;
		}
		return true;
	}

	private static boolean insertLopHoc(LopHoc lophoc, String nameDB, String nameTableDB, Connection conn) {
		String sql = null;
		PreparedStatement pre = null;
		ResultSet rs = null;
		sql = "SELECT * from " + nameDB + "." + nameTableDB + " WHERE maLophoc = ?";

		try {

			pre = conn.prepareStatement(sql);
			pre.setString(1, lophoc.getMaLophoc());
			rs = pre.executeQuery();

			while (rs.next()) {
				sql = "UPDATE " + nameDB + "." + nameTableDB + " SET dt_expire = ? WHERE id = ?";
				PreparedStatement ps;
				try {
					ps = conn.prepareStatement(sql);
					ps.setDate(1, Date.valueOf("2003-12-12"));
					ps.setInt(2, rs.getInt(1));
					ps.execute();
				} catch (SQLException e) {
				}
			}

		} catch (SQLException e) {

		}

		sql = "INSERT INTO " + nameDB + "." + nameTableDB + " (maLopHoc , maMH , namHoc , dt_expire) VALUES (?,?,?,?)";
		try {
			pre = conn.prepareStatement(sql);
			pre.setString(1, lophoc.getMaLophoc());
			pre.setInt(2, lophoc.getMaMonHoc());
			pre.setInt(3, lophoc.getNamHoc());

			pre.setDate(4, null);

			pre.execute();

		} catch (SQLException e) {
			return false;
		}
		return true;
	}

	private static boolean insertDangKy(DangKy dangKy, String nameDB, String nameTableDB, Connection conn) {
		String sql_getDateDim = "Select date_sk from warehouse.date_dim Where full_date = ?";
		String sql = null;
		PreparedStatement pre = null;
		ResultSet rs = null;
		sql = "SELECT * from " + nameDB + "." + nameTableDB + " WHERE maDangKy = ?";

		try {

			pre = conn.prepareStatement(sql);
			pre.setString(1, dangKy.getMaDangKy());
			rs = pre.executeQuery();

			while (rs.next()) {
				sql = "UPDATE " + nameDB + "." + nameTableDB + " SET dt_expire = ? WHERE id = ?";
				PreparedStatement ps;
				try {
					ps = conn.prepareStatement(sql);
					ps.setDate(1, Date.valueOf("2003-12-12"));
					ps.setInt(2, rs.getInt(1));
					ps.execute();
				} catch (SQLException e) {
				}
			}

		} catch (SQLException e) {

		}

		sql = "INSERT INTO " + nameDB + "." + nameTableDB
				+ " (maDangKy, maSinhvien , maLophoc, thoigianDK, thoigianDK_sk, dt_expire) VALUES (?,?,?,?,?,?)";
		try {
			pre = conn.prepareStatement(sql);
			pre.setString(1, dangKy.getMaDangKy());
			pre.setInt(2, dangKy.getMaSinhvien());
			pre.setInt(3, dangKy.getMaLophoc());
			pre.setDate(4, dangKy.getThoigianDK());

			PreparedStatement ps = conn.prepareStatement(sql_getDateDim);
			ps.setString(1, String.valueOf(dangKy.getThoigianDK()));
			ResultSet r = ps.executeQuery();
			while (r.next()) {
				pre.setInt(5, r.getInt(1));
			}

			pre.setDate(6, null);

			pre.execute();

		} catch (SQLException e) {
			return false;
		}
		return true;
	}
}
