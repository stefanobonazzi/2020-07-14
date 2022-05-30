package it.polito.tdp.PremierLeague.model;

public class Event implements Comparable<Event> {
	
	private Match match;

	public Event(Match match) {
		super();
		this.match = match;
	}

	public Match getMatch() {
		return match;
	}

	public void setMatch(Match match) {
		this.match = match;
	}

	@Override
	public int compareTo(Event o) {
		return this.match.getDate().compareTo(o.getMatch().getDate());
	}
	
}
