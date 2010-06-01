package madn;

public class Simulation {

	/**
	 * @param args
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException {

		String teams[] = new String[]{"madn.dummyTeam","madn.zieheLetzten","madn.zieheErsten","madn.zieheLetzten"};

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
	 * aus 24 spielen, damit alle startpositionen durchlaufen werden.
	 * Es werden somit 24*anz Spiele simuliert!
	 * @param teams
	 * @param anznex
	 * @return result[i] = gewinne von team i
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	static int[] doSimulation(String teams[], int anz) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		if(teams.length!=4)
			throw new IllegalArgumentException("Es muessen 4 teams uebergeben werden!");

		Spiel spiel = new Spiel(teams[0],teams[1],teams[2],teams[3]);

		int gewinne[] = new int[4];
		int aktGewinner;

		PermutationGenerator perm = new PermutationGenerator(teams.length);
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
