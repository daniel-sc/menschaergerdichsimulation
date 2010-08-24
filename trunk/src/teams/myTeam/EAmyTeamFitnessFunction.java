package teams.myTeam;
/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */


import java.util.ArrayList;


import madn.*;

import org.jgap.*;

/**
 * Fitness function for the knapsack example.
 *
 * @author Klaus Meffert
 * @since 2.3
 */
public class EAmyTeamFitnessFunction extends FitnessFunction {
	
	/** Anzahl der Simulationen fuer jede Auswertung eines Chromosoms - ZEITKRITISCH!! */
  private static final int ANZ_SIMULATIONEN = 20;

/** eclipse hat gemeckert.. */
	private static final long serialVersionUID = 1L;

/** String containing the CVS revision. Read out via reflection!*/
  //private final static String CVS_REVISION = "$Revision: 1.5 $";
  
  ArrayList<madn.Team> teams;
  
  /** das team mit dem getestet wird */
  myTeam teamEA;
   
  public EAmyTeamFitnessFunction() {
	teamEA = new myTeam();
	teams = new ArrayList<madn.Team>(4);
	teams.add(teamEA);
	teams.add(new zieheErsten());
	teams.add(new zieheErsten());
	teams.add(new zieheLetzten());
  }

  /**
   * Determine the fitness of the given Chromosome instance. The higher the
   * return value, the more fit the instance. This method should always
   * return the same fitness value for two equivalent Chromosome instances.
   *
   * @param a_subject the Chromosome instance to evaluate
   * @return a positive double reflecting the fitness rating of the given
   * Chromosome
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public double evaluate(IChromosome a_subject) {
	  teamEA.setParameters(getParameters(a_subject));
	  int ergebnis[] = Simulation.doSimulation(teams, ANZ_SIMULATIONEN);
	  
	  //max weil 0 eigentlich nur durch rundungsfehler entstehen kann..
      return Math.max(1, 100*ergebnis[0]/(ANZ_SIMULATIONEN*24));
  }
  
  
  /**
 * @param a_potentialSolution
 * @return Die Parameter als eine int-Array
 */
public static int[] getParameters(IChromosome a_potentialSolution) {
	  int[] result = new int[a_potentialSolution.size()];
	  for (int i=0; i<result.length; i++) {
		result[i] =  (Integer) a_potentialSolution.getGene(i).getAllele();
	}
	  return result;
  }
}
