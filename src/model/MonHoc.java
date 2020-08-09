package model;

public class MonHoc {
	private int id, STT, maMH, TC, id_log;
	public int getId_log() {
		return id_log;
	}
	public void setId_log(int id_log) {
		this.id_log = id_log;
	}
	private String tenMH, khoa_BoMon, sk_date_dim, dt_expire;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSTT() {
		return STT;
	}
	public void setSTT(int sTT) {
		STT = sTT;
	}
	public int getMaMH() {
		return maMH;
	}
	public void setMaMH(int maMH) {
		this.maMH = maMH;
	}
	public int getTC() {
		return TC;
	}
	public void setTC(int tC) {
		TC = tC;
	}
	public String getTenMH() {
		return tenMH;
	}
	public void setTenMH(String tenMH) {
		this.tenMH = tenMH;
	}
	public String getKhoa_BoMon() {
		return khoa_BoMon;
	}
	public void setKhoa_BoMon(String khoa_BoMon) {
		this.khoa_BoMon = khoa_BoMon;
	}
	public String getSk_date_dim() {
		return sk_date_dim;
	}
	public void setSk_date_dim(String sk_date_dim) {
		this.sk_date_dim = sk_date_dim;
	}
	public String getDt_expire() {
		return dt_expire;
	}
	public void setDt_expire(String dt_expire) {
		this.dt_expire = dt_expire;
	}
	
	
}
