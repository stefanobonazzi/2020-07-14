package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private Graph<Team, DefaultWeightedEdge> graph;
	private PremierLeagueDAO dao;
	private List<Team> teams;
	private Map<Integer, Team> mTeams;
	private List<Match> matches;
	private Simulator sim;
	private List<Team> classifica;
	
	public Model() {
		this.dao = new PremierLeagueDAO();
	}
	
	public void creaGrafo() {
		this.graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		this.teams = this.dao.listAllTeams();
		
		Graphs.addAllVertices(this.graph, this.teams);
		
		this.mTeams = dao.mapAllTeams();
		this.matches = dao.listAllMatches();
		for(Match m: matches) {
			Team home = mTeams.get(m.getTeamHomeID());
			Team away = mTeams.get(m.getTeamAwayID());
			if(this.graph.vertexSet().contains(home) && this.graph.vertexSet().contains(away)) {
				if(m.getReaultOfTeamHome() == 1) {
					home.setPoints(3);
				} else if(m.getReaultOfTeamHome() == -1) {
					away.setPoints(3);
				} else {
					home.setPoints(1);
					away.setPoints(1);
				}
			}
		}
		
		for(Team t1: mTeams.values()) {
			for(Team t2: mTeams.values()) {
				if(!t1.equals(t2)) {
					DefaultWeightedEdge edge = this.graph.getEdge(t1, t2);
					if(edge == null && this.graph.getEdge(t2, t1) == null) {
						int points1 = t1.getPoints();
						int points2 = t2.getPoints();
						double diff;
						if(points1>points2) {
							diff = points1-points2;
							edge = this.graph.addEdge(t1, t2);
							this.graph.setEdgeWeight(edge, diff);
							List<Team> l = new ArrayList<Team>();
							l.add(t2);
							Graphs.addOutgoingEdges(this.graph, t1, l);
						} else if(points1<points2) {
							diff = points2-points1;
							edge = this.graph.addEdge(t2, t1);
							this.graph.setEdgeWeight(edge, diff);
							List<Team> l = new ArrayList<Team>();
							l.add(t1);
							Graphs.addOutgoingEdges(this.graph, t2, l);
						} 
					}
				}
			}
		}
	}
	
	public String getClassifica(Team tt) {
		classifica = new ArrayList<>(mTeams.values());
		Collections.sort(classifica);
		
		String s = "SQUADRE MIGLIORI:\n";
		Team team = mTeams.get(tt.getTeamID());
		for(Team t: classifica) {
			if(!t.equals(team)) {
				if(t.getPoints() > team.getPoints()) {
					s += t+"\n";
				} else if(t.getPoints() < team.getPoints()) {
					if(!s.contains("\nSQUADRE PEGGIORI:\n"))
						s += "\nSQUADRE PEGGIORI:\n";
					s += t+"\n";
				}
			}
		}
		
		return s;
	}
	
	public List<Team> getAllTeams() {
		return this.dao.listAllTeams();
	}

	public Graph<Team, DefaultWeightedEdge> getGraph() {
		return graph;
	}

	public String simula(int n, int x) {
		sim = new Simulator(n, x, this.matches, this.mTeams, this.classifica);
		sim.init();
		sim.run();
		double media_reporter = sim.getMedia_reporter();
		int partite_critiche = sim.getPartite_critiche();
		
		return "Media reporter a partita: "+media_reporter+"\nNumero di partite critiche: "+partite_critiche;
	}
	
}
