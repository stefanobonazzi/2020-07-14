package it.polito.tdp.PremierLeague.model;

public class Team implements Comparable<Team> {
	Integer teamID;
	String name;
	private int points;
	private int n;

	public Team(Integer teamID, String name) {
		super();
		this.teamID = teamID;
		this.name = name;
		this.points = 0;
	}
	
	public Integer getTeamID() {
		return teamID;
	}
	public void setTeamID(Integer teamID) {
		this.teamID = teamID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = this.points + points;
	}
	public int getN() {
		return n;
	}
	public void setN(int n) {
		this.n = n;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((teamID == null) ? 0 : teamID.hashCode());
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
		Team other = (Team) obj;
		if (teamID == null) {
			if (other.teamID != null)
				return false;
		} else if (!teamID.equals(other.teamID))
			return false;
		return true;
	}

	@Override
	public int compareTo(Team o) {
		return -(this.points-o.getPoints());
	}
	
}
