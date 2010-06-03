package madn;

import java.util.ArrayList;
import java.util.List;

/**
 * Steht fuer ein Mensch aerger dich nicht Spiel.
 * Kann mehrere Spiele durchfuehren via {@link #reset()} bzw {@link #rotiere(int[])}.
 * 
 * @author daniel
 *
 */
public class Spiel {
	Spielfeld feld;
	Spielfeld feld2; //sicherheitsfeld dass den spielern uebergeben wird.
	int anzTeams;// = 4; //kann spaeter angepasst werden
	List<Team> teams;
	List<Team> teams_org; //bakup fuer rotation
	int naechster = 0; //es startet spieler 0
	Boolean ende = false;
	int aktWurf = 0;

	/**
	 * Die parameter sind die Klassennamen der teams (fuer class.forName)
	 * alle parameter muessen gesetzt werden. Fuer keinen Spieler: {@link madn.dummyTeam}
	 * @param team1
	 * @param team2
	 * @param team3
	 * @param team4
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	Spiel(String team1, String team2, String team3, String team4) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		anzTeams = 4;
		if(team4.length()==0)
			anzTeams--;
		if(team3.length()==0)
			anzTeams--;
		if(team2.length()==0)
			anzTeams--;
		if(team1.length()==0) 
			System.out.println("ERROR: mindestens ein Team!");

		feld = new Spielfeld();
		feld2 = new Spielfeld();
		teams = new ArrayList<Team>(anzTeams);

		switch (anzTeams) {
		case 4:
			teams.add(0, (Team) Class.forName(team4).newInstance());
			teams.get(0).setTeamNr(3);
		case 3:
			teams.add(0, (Team) Class.forName(team3).newInstance());
			teams.get(0).setTeamNr(2);
		case 2:
			teams.add(0, (Team) Class.forName(team2).newInstance());
			teams.get(0).setTeamNr(1);
		case 1:
			teams.add(0, (Team) Class.forName(team1).newInstance());
			teams.get(0).setTeamNr(0);
			break;
		default:
			System.out.println("ERROR 48");
		break;
		}
		teams_org = new ArrayList<Team>(teams);
	}
	
	/**
	 * aendert nicht die aktuelle permutation der teams!
	 */
	void reset() {
		feld = new Spielfeld();
		feld2 = new Spielfeld();
	}
	/**
	 * resettet spiel und rotiert spieler
	 * @deprecated nicht getestet!
	 */
	void rotiere(int permutation[]) {
		if(permutation.length!=anzTeams)
			throw new IllegalArgumentException("Permutation muss die gleiche lange wie die Anzahl der Teams haben!");
		teams = PermutationGenerator.permute(teams,permutation);
		for(int i=0; i<anzTeams; i++)
			teams.get(i).setTeamNr(i);
		reset();
	}
	/**
	 * permutation auf die 1. aufstellung
	 * @param permutation
	 */
	void permutiere(int permutation[]) {
		if(permutation.length!=anzTeams)
			throw new IllegalArgumentException("Permutation muss die gleiche lange wie die Anzahl der Teams haben!");
		teams = PermutationGenerator.permute(teams_org,permutation);
		for(int i=0; i<anzTeams; i++)
			teams.get(i).setTeamNr(i);
		reset();
	}
	
	/** 
	 * gibt teamnr des gewinners zurueck.
	 * gibt nr des permutierten teams zurueck!
	 * @return
	 */
	int simuliereSpiel() {
		while(true) {
			do {
				aktWurf = wuerfel();

				if(feld.anzSpielerImStart(naechster)==4 && aktWurf!=6) {
					aktWurf = wuerfel();
					if(aktWurf!=6) {
						aktWurf = wuerfel();
						if(aktWurf!=6) break;
					}
				} //jetzt hat das team entweder eine figur auf dem feld oder eine 6
				feld2 = new Spielfeld(feld);
				Zug zug = teams.get(naechster).ziehen(feld2,aktWurf);
				if(zug==null) { //kann/will nicht ziehen
					zug = new Zug(aktWurf, naechster, 0, feld); //erstelle beliebigen zug mit korrekter wurf und team zahl
				}
				zug.wurf = aktWurf; //nicht dass einer schummelt ;) (das reicht!)
				feld.zieheSpieler(zug);
				
				if(feld.ende()>=0)
					return naechster;

			} while(aktWurf==6);

			naechster = (naechster+1)%anzTeams;
		}
	}

	int wuerfel() {
		return  (int) (Math.random()*6+1);
	}
}
