package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Simulator {

	private int n;
	private int x;
	private List<Match> matches;
	private Map<Integer, Team> mTeams;
	private List<Team> classifica;
	
	private PriorityQueue<Event> queue;
	
	private int partite_critiche;
	private double somma_reporter;
	
	public Simulator(int n, int x, List<Match> matches, Map<Integer, Team> mTeams, List<Team> classifica) {
		this.n = n;
		this.x = x;
		this.matches = matches;
		this.mTeams = mTeams;
		this.classifica = classifica;
		Collections.sort(this.classifica);
		this.partite_critiche = 0;
		this.somma_reporter = 0;
	}
	
	public void init() {
		this.queue = new PriorityQueue<Event>();
		
		for(Team t: mTeams.values()) {
			t.setN(n);
		}
		
		for(Match m: matches) {
			queue.add(new Event(m));
		}
	}

	public void run() {
		while (!queue.isEmpty()) {
			Event e = this.queue.poll();
			this.processEvent(e);
		}	
	}

	private void processEvent(Event e) {
		if(mTeams.get(e.getMatch().getTeamHomeID()).getN()+mTeams.get(e.getMatch().getTeamAwayID()).getN() < this.x) {
			this.partite_critiche++;
		}
		
		this.somma_reporter += mTeams.get(e.getMatch().getTeamHomeID()).getN()+mTeams.get(e.getMatch().getTeamAwayID()).getN();
		
		int result = e.getMatch().getReaultOfTeamHome();
		if(result == 0)
			return;
		
		Team winner;
		Team loser;
		if(result == 1) {
			winner = mTeams.get(e.getMatch().getTeamHomeID());
			loser = mTeams.get(e.getMatch().getTeamAwayID());
		} else {
			winner = mTeams.get(e.getMatch().getTeamAwayID());
			loser = mTeams.get(e.getMatch().getTeamHomeID());
		}
		
		if(Math.random() >= 0.5) {
			if(winner.getN() > 0) {
				List<Team> better = new ArrayList<Team>();
				for(Team t: classifica) {
					if(t.getPoints() > winner.getPoints()) {
						better.add(t);
					}
				}
				
				if(better.size() > 0) {
					int p = (int) (Math.random()*better.size());
					Team next = better.get(p);
					winner.setN(winner.getN()-1);
					next.setN(next.getN()+1);
				}
			}
		}
		
		if(Math.random() <= 0.2) {
			if(loser.getN() > 0) {
				List<Team> worse = new ArrayList<Team>();
				for(Team t: classifica) {
					if(t.getPoints() < loser.getPoints()) {
						worse.add(t);
					}
				}
				
				if(worse.size() > 0) {
					int p = (int) (Math.random()*worse.size());
					Team next = worse.get(p);
					int nr = (int) (Math.random()*loser.getN());
					winner.setN(loser.getN()-nr);
					next.setN(next.getN()+nr);
				}
			}
		}
	}

	public int getPartite_critiche() {
		return partite_critiche;
	}

	public double getMedia_reporter() {
		return somma_reporter/matches.size();
	}

}
