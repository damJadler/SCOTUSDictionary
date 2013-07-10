package com.damjadler;

public class Case {
	
	public String caseName;
	public String difficultParty;
	
	public Case(String fullName, String partyName)
	{
		caseName=fullName;
		difficultParty=partyName;
	}

	public String getCaseName() {
		return caseName;
	}

	public void setCaseName(String caseName) {
		this.caseName = caseName;
	}

	public String getDifficultParty() {
		return difficultParty;
	}

	public void setDifficultParty(String difficultParty) {
		this.difficultParty = difficultParty;
	}

}
