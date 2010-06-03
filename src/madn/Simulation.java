package madn;

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
	 */
	static int[] doSimulation(String teams[], int anz) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		int anzTeams = 0;
		String teams_neu[] = new String[4];
		for(int i=0; i<4; i++)
			teams_neu[i] = "";
		
		for(int i=0; i<Math.min(4, teams.length); i++)
			if( teams[i].length() > 0 ) {
				anzTeams++;
				teams_neu[i] = teams[i];
			}
			else
				break;
		
		
		
		Spiel spiel = new Spiel(teams_neu[0],teams_neu[1],teams_neu[2],teams_neu[3]);

		int gewinne[] = new int[anzTeams];
		int aktGewinner;

		PermutationGenerator perm = new PermutationGenerator(anzTeams);
		int nextperm[];
		int inv[];

		while(perm.hasMore()) {
			nextperm = perm.getNext();
			inv = PermutationGenerator.invert(nextperm); 
			spiel.permutiere(nextperm);
			for(int i=0; i<100; i++) {
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
