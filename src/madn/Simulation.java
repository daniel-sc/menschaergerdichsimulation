package madn;

import java.util.ArrayList;
import java.util.List;

/**
 * fuehrt (viele) Simulationen von Spielen durch. 
 * @author daniel
 *
 */
public class Simulation {

	/**
	 * @param args
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException {

		String teams[] = new String[]{"madn.zieheErsten",
				"madn.zieheLetzten",
				"madn.zieheErsten",
				"madn.dummyTeam"};

		int gewinne[] = doSimulation(teams, 100);

		int gesamtzahl = 0;
		for (int i = 0; i < teams.length; i++) {
			gesamtzahl += gewinne[i];
		}
		System.out.println("Spiele insgesamt: "+gesamtzahl);
		for (int i = 0; i < teams.length; i++) {
			System.out.println("Gewinne team "+teams[i]+" \t"+gewinne[i]+" \t|| "+(gewinne[i]*100)/gesamtzahl+"%");
		}
	}

	/**
	 * fuehrt anz spiel-simulationen aus, dabei besteht eine Simulation
	 * aus teams.length! spielen, damit alle startpositionen durchlaufen werden.
	 * Es werden somit teams.lenght!*anz Spiele simuliert!
	 * @param teams Array mit den klassennamen der teams. 0 < laenge < 5.
	 * Eintraege nach dem ersten leerstring werden ignoriert.
	 * @param anz
	 * @return result[i] = gewinne von team i
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @see {@link #doSimulation(List, int)}
	 */
	static int[] doSimulation(String teams[], int anz) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		ArrayList<Team> teams_neu = new ArrayList<Team>(teams.length);
		
		for(int i=0; i<Math.min(4, teams.length); i++)
			if( teams[i].length() > 0 ) {
				teams_neu.add(i, (Team) Class.forName(teams[i]).newInstance());
			}
			else
				break;
		
		
		
		return doSimulation(teams_neu, anz);
	}
	
	/**
	 * fuehrt anz spiel-simulationen aus, dabei besteht eine Simulation
	 * aus teams.size()! spielen, damit alle startpositionen durchlaufen werden.
	 * Es werden somit teams.size()!*anz Spiele simuliert!
	 * @param teams
	 * @param anz
	 * @return result[i] = gewinne von team.get(i)
	 */
	static int[] doSimulation(List<Team> teams, int anz) {
		int anzTeams = teams.size();	
		
		
		Spiel spiel = new Spiel(teams);

		int gewinne[] = new int[anzTeams];
		int aktGewinner;

		PermutationGenerator perm = new PermutationGenerator(anzTeams);
		int nextperm[];
		int inv[];

		while(perm.hasMore()) {
			nextperm = perm.getNext();
			inv = PermutationGenerator.invert(nextperm); 
			spiel.permutiere(nextperm);
			for(int i=0; i<anz; i++) {
				spiel.reset();
				aktGewinner = spiel.simuliereSpiel();
				
				gewinne[inv[aktGewinner]]++;
			}
			System.out.print(".");
		}


		System.out.println();
		return gewinne;
	}

}
