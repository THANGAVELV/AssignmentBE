package bank.rabo.model;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

public class Record {
	private BigInteger reference_No;
	private String acc_No;
	private String description;
	private BigDecimal start_Bal;
	private BigDecimal mutation;
	private BigDecimal end_Bal;
	private String duplicate;
	private String endBalInvalid;
	

	public Record() {
	}
	
	

	public Record(BigInteger reference_No, String acc_No, String description, BigDecimal start_Bal, BigDecimal mutation,
			BigDecimal end_Bal) {
		super();
		this.reference_No = reference_No;
		this.acc_No = acc_No;
		this.description = description;
		this.start_Bal = start_Bal;
		this.mutation = mutation;
		this.end_Bal = end_Bal;
	}



	public BigInteger getReference_No() {
		return reference_No;
	}

	public void setReference_No(BigInteger reference_No) {
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

	public BigDecimal getStart_Bal() {
		return start_Bal;
	}

	public void setStart_Bal(BigDecimal start_Bal) {
		this.start_Bal = start_Bal;
	}

	public BigDecimal getMutation() {
		return mutation;
	}

	public void setMutation(BigDecimal mutation) {
		this.mutation = mutation;
	}

	public BigDecimal getEnd_Bal() {
		return end_Bal;
	}

	public void setEnd_Bal(BigDecimal end_Bal) {
		this.end_Bal = end_Bal;
	}

	public String getDuplicate() {
		return duplicate;
	}

	public void setDuplicate(String duplicate) {
		this.duplicate = duplicate;
	}

	public String getEndBalInvalid() {
		return endBalInvalid;
	}

	public void setEndBalInvalid(String endBalInvalid) {
		this.endBalInvalid = endBalInvalid;
	}

	@Override
	public String toString() {
		return "Record [reference_No=" + reference_No + ", acc_No=" + acc_No + ", description=" + description
				+ ", start_Bal=" + start_Bal + ", mutation=" + mutation + ", end_Bal=" + end_Bal + "]";
	}

}
