package com.codingexercise.producer.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Domain object representing attributes related to a Customer Event.
 * 
 * @author wdurrant
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventAttribute implements Serializable {

	/**
	 * Generated serial id.
	 */
	private static final long serialVersionUID = 8870214086797013170L;

	/**
	 * The customer account number.
	 */
	private String accountNum;
	
	/**
	 * Transaction amount.
	 */
	private String txAmount;
	
	/**
	 * Card member name.
	 */
	private String cardMemberName;
	
	/**
	 * Card product type.
	 */
	private String product;
	
    public EventAttribute() {}

	public EventAttribute(String accountNum, String txAmount, String cardMemberName, String product) {
		super();
		this.accountNum = accountNum;
		this.txAmount = txAmount;
		this.cardMemberName = cardMemberName;
		this.product = product;
	}

	public String getAccountNum() {
		return accountNum;
	}

	public String getTxAmount() {
		return txAmount;
	}

	public String getCardMemberName() {
		return cardMemberName;
	}

	public String getProduct() {
		return product;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountNum == null) ? 0 : accountNum.hashCode());
		result = prime * result + ((cardMemberName == null) ? 0 : cardMemberName.hashCode());
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		result = prime * result + ((txAmount == null) ? 0 : txAmount.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EventAttribute other = (EventAttribute) obj;
		if (accountNum == null) {
			if (other.accountNum != null)
				return false;
		} else if (!accountNum.equals(other.accountNum))
			return false;
		if (cardMemberName == null) {
			if (other.cardMemberName != null)
				return false;
		} else if (!cardMemberName.equals(other.cardMemberName))
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		if (txAmount == null) {
			if (other.txAmount != null)
				return false;
		} else if (!txAmount.equals(other.txAmount))
			return false;
		return true;
	}

}

