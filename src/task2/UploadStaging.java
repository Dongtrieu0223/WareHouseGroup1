package task2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import model.Config;
import model.Log;
import utils.ConnectDB;
import utils.SendEmail;
import utils.SystemContain;

public class UploadStaging {

	@SuppressWarnings({ "incomplete-switch", "deprecation" })
	public static String uploadStaging(ArrayList<Config> configs, Log log)
			throws IOException, SQLException, ParseException {

		if (log.getStatus().equals(SystemContain.DOWNLOAD_SUCCESS)) {
			Config config_uploadStaging = null;
			for (Config item : configs) {
				if (item.getNameConfig().equals(SystemContain.CONFIG_UPLOAD_STAGING)) {
					config_uploadStaging = item;
					break;
				}
			}
			// Đọc một file XSL.
			File file = new File(log.getUrlLocal());
			FileInputStream inputStream = new FileInputStream(file);

			@SuppressWarnings("resource")
			Workbook workbook = new XSSFWorkbook(inputStream);
			Sheet sheet = workbook.getSheetAt(0);
			Connection conn = null;
			conn =ConnectDB.getConnectDB(config_uploadStaging.getNameDatabase(), config_uploadStaging.getUserNameDatabase(), config_uploadStaging.getPasswordDatabase());
			
			String sql = config_uploadStaging.getQuerySQL();
			
			// Lấy ra Iterator cho tất cả các dòng của sheet hiện tại.
			Iterator<Row> rowIterator = sheet.iterator();
			PreparedStatement ps = null;
			rowIterator.next();
			boolean isStop = false;
			while (rowIterator.hasNext()) {
				if (isStop == true) {
					break;
				}
				Row row = rowIterator.next();
				ps = conn.prepareStatement(sql);
				ps.setString(1, "");
				ps.setString(2, "");
				ps.setString(3, "");
				ps.setString(4, "");
				ps.setString(5, "");
				ps.setString(6, "");
				ps.setString(7, "");
				ps.setString(8, "");
				ps.setString(9, "");
				ps.setString(10, "");
				int columnIndex = 0;
				// Lấy Iterator cho tất cả các cell của dòng hiện tại.
				Iterator<Cell> cellIterator = row.cellIterator();
				
				while (cellIterator.hasNext()) {
					int mssvCheck = 0;
					if(columnIndex > 10) columnIndex = 0;

					Cell nextCell = cellIterator.next();
					
					CellType cellType = nextCell.getCellTypeEnum();
					if (columnIndex == 1) {
						switch (cellType) {
						case NUMERIC:
							mssvCheck = (int) nextCell.getNumericCellValue();
							break;
						case STRING:
							mssvCheck = Integer.parseInt(nextCell.getStringCellValue());
							break;
						}

						if (mssvCheck == 0) {
							isStop = true;
							break;
						}
					} else if (isStop == true) {
						isStop = true;
						break;
					}

					switch (columnIndex) {
					case 1:

						int mssv = 0;
						switch (cellType) {

						case NUMERIC:
							mssv = (int) nextCell.getNumericCellValue();
							break;
						case STRING:
							mssv = Integer.parseInt(nextCell.getStringCellValue());
							break;
						}

						if (mssv == 0) {
							isStop = true;
						} else {
							ps.setString(1, String.valueOf(mssv));
						}
						

						break;
					case 2:
						try {
							String ho = nextCell.getStringCellValue();
							ps.setString(2, ho);
						} catch (Exception e) {
							ps.setString(2, "");
						}
						break;
					case 3:
						try {
							String ten = nextCell.getStringCellValue();
							ps.setString(3, ten);
						} catch (Exception e) {
							ps.setString(3, "");
						}

						break;
					case 4:
						if (nextCell.getCellTypeEnum() == CellType.STRING) {

							try {
								String dob = nextCell.getStringCellValue();
								DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
								Date d = df.parse(dob);
								ps.setString(4, getString(d));
							} catch (Exception e) {
								ps.setString(4, "");
							}

						} else {

							try {
								Date dOB = (Date) nextCell.getDateCellValue();
								ps.setString(4, getString(dOB));
							} catch (Exception e) {
								ps.setString(4, "");
							}
						}

						break;
					case 5:
						try {
							String lop = nextCell.getStringCellValue();
							ps.setString(5, lop);
						} catch (Exception e) {
							ps.setString(5, "");
						}

						break;
					case 6:
						try {
							String tenLop = nextCell.getStringCellValue();
							ps.setString(6, tenLop);
						} catch (Exception e) {
							ps.setString(6, "");
						}

						break;
					case 7:
						try {
							int sdt = 0;
							if (nextCell.getCellTypeEnum() == CellType.NUMERIC) {
								sdt = (int) nextCell.getNumericCellValue();
							} else if (nextCell.getCellTypeEnum() == CellType.STRING) {
								sdt = Integer.parseInt(nextCell.getStringCellValue());
							}
							ps.setString(7, String.valueOf(sdt));
						} catch (Exception e) {
							ps.setString(7, String.valueOf(""));
						}

						break;
					case 8:
						try {
							String email = nextCell.getStringCellValue();
							ps.setString(8, email);
						} catch (Exception e) {
							ps.setString(8, "");
						}

						break;
					case 9:

						try {
							String queQuan = nextCell.getStringCellValue();
							ps.setString(9, queQuan);
						} catch (Exception e) {
							ps.setString(9, "");
						}

						break;
					case 10:
						try {
							String ghiChu = nextCell.getStringCellValue();
							ps.setString(10, ghiChu);
						} catch (Exception e) {
							ps.setString(9, "");
						}
						break;
					}
					columnIndex++;
					
				}
				
				if (isStop != true) {
					ps.setInt(11, log.getId());
					ps.execute();
				}

			}
			conn.close();
			Config config_updateLog = null;
			for (Config item : configs) {
				if (item.getNameConfig().equals(SystemContain.CONFIG_UPDATE_LOG)) {
					config_updateLog = item;
					break;
				}
			}
			conn = ConnectDB.getConnectDB(config_updateLog.getNameDatabase(), config_updateLog.getUserNameDatabase(), config_updateLog.getPasswordDatabase());
			log.setStatus(SystemContain.UPLOAD_STAGING);
			update(config_updateLog.getQuerySQL(), log, conn);
			conn.close();
			return SystemContain.UPLOAD_STAGING;
		}
		File file = new File(log.getUrlLocal());
		SendEmail.send("File ERROR","File " + file.getName() + " error");
		return SystemContain.ERROR;
	}

	private static String getString(Date d) {
		return new SimpleDateFormat("yyyy-MM-dd").format(d);
	}

	private static void update(String sql, Log log, Connection conn) {
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, log.getStatus());
			ps.setInt(2, log.getId());
			ps.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}

	}
	
	

}
