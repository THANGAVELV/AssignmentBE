package bank.rabo.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Record {
	private Integer reference_No;
	private String acc_No;
	private String description;
	private BigDecimal start_Bal;
	private BigDecimal mutation;
	private BigDecimal end_Bal;

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


	@Override
	public String toString() {
		return "Record [reference_No=" + reference_No + ", acc_No=" + acc_No + ", description=" + description
				+ ", start_Bal=" + start_Bal + ", mutation=" + mutation + ", end_Bal=" + end_Bal  + "]";
	}
	
	
}
