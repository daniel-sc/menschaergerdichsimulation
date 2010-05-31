package madn;

public class Simulation {

	/**
	 * @param args
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		String teams[] = new String[]{"madn.dummyTeam","madn.zieheErsten","madn.zieheLetzten","madn.zieheLetzten"};
		Spiel spiel = new Spiel(teams[0],teams[1],teams[2],teams[3]);
		
		int gewinne[] = new int[4];
		int aktGewinner;
		
		PermutationGenerator perm = new PermutationGenerator(teams.length);
		int nextperm[];
		int oldperm[] = new int[teams.length];

		
		for(int i=0; i<100; i++) {
			for(int k=1; k<oldperm.length; k++)
				oldperm[k] = k;
			perm.reset();
			
			while(perm.hasMore()) {
				nextperm = perm.getNext();
				//spiel.rotiere(PermutationGenerator.difference(oldperm, nextperm));
				spiel.permutiere(nextperm);
				//spiel.reset();
				//oldperm = nextperm;
				aktGewinner = spiel.simuliereSpiel();
				int inv[] = PermutationGenerator.invert(nextperm); 
				gewinne[inv[aktGewinner]]++;
				//gewinne[aktGewinner]++;
			}
			System.out.print(".");
		}
		System.out.println();
		int gesamtzahl = 0;
		for (int i = 0; i < teams.length; i++) {
			gesamtzahl += gewinne[i];
		}
		System.out.println("Spiele insgesamt: "+gesamtzahl);
		for (int i = 0; i < teams.length; i++) {
			System.out.println("Gewinne team "+teams[i]+" \t"+gewinne[i]+" \t|| "+(gewinne[i]*100)/gesamtzahl+"%");
		}
	}

}
