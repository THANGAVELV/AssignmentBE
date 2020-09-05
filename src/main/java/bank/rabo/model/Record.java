package bank.rabo.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Record {
	private Integer reference_No;
	private String acc_No;
	private String description;
	private Double start_Bal;
	private Double mutation;
	private Double end_Bal;
	private String reason;
	public Integer getReference_No() {
		return reference_No;
	}
	public void setReference_No(Integer reference_No) {
		this.reference_No = reference_No;
	}
	public String getAcc_No() {
		return acc_No;
	}
	public void setAcc_No(String acc_No) {
		this.acc_No = acc_No;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Double getStart_Bal() {
		return start_Bal;
	}
	public void setStart_Bal(Double start_Bal) {
		this.start_Bal = start_Bal;
	}
	public Double getMutation() {
		return mutation;
	}
	public void setMutation(Double mutation) {
		this.mutation = mutation;
	}
	public Double getEnd_Bal() {
		return end_Bal;
	}
	public void setEnd_Bal(Double end_Bal) {
		this.end_Bal = end_Bal;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	
}
