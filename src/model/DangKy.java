package model;

import java.sql.Date;

public class DangKy {
	private int maSinhvien, maLophoc, idLog;
	private String maDangKy;
	private Date thoigianDK, dt_expire;
	public int getMaSinhvien() {
		return maSinhvien;
	}
	public void setMaSinhvien(int maSinhvien) {
		this.maSinhvien = maSinhvien;
	}
	public int getMaLophoc() {
		return maLophoc;
	}
	public void setMaLophoc(int maLophoc) {
		this.maLophoc = maLophoc;
	}
	public int getIdLog() {
		return idLog;
	}
	public void setIdLog(int idLog) {
		this.idLog = idLog;
	}
	public String getMaDangKy() {
		return maDangKy;
	}
	public void setMaDangKy(String maDangKy) {
		this.maDangKy = maDangKy;
	}
	public Date getThoigianDK() {
		return thoigianDK;
	}
	public void setThoigianDK(Date thoigianDK) {
		this.thoigianDK = thoigianDK;
	}
	public Date getDt_expire() {
		return dt_expire;
	}
	public void setDt_expire(Date dt_expire) {
		this.dt_expire = dt_expire;
	}
	
	
}
