
public class TBUnit {
	String unitName;
	@Key
	String unitCode;
	
	int status;
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	String remark;

	@Override
	public String toString() {
		return "TBUnit [unitCode=" + unitCode + ", unitName=" + unitName + ", status=" + status + ", remark=" + remark
				+ "]";
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
}