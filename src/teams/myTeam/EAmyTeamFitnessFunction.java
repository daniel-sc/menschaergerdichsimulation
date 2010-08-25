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
	
	/** Anzahl der Simulationen fuer jede Auswertung eines Chromosoms - 
	 * ZEITKRITISCH!! darf aber nicht zu klein sein, da eine einmalig
	 * zu hohe fitness sich 'durchschleift'! */
  static int ANZ_SIMULATIONEN = 20;

/** eclipse hat gemeckert.. */
	private static final long serialVersionUID = 1L;

/** String containing the CVS revision. Read out via reflection!*/
  //private final static String CVS_REVISION = "$Revision: 1.5 $";
  
  ArrayList<madn.Team> teams;
  
  /** das team mit dem getestet wird */
  myTeam teamEA;
  
  /** debug */
  private static int iterations = 0;
   
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
	  double gewonnen_prozent =Math.max(1, ((double)100*ergebnis[0])/(ANZ_SIMULATIONEN*24d)); 	  
	  //max weil 0 eigentlich nur durch rundungsfehler entstehen kann..
      
//	  System.out.println(iterations++ +". fitness! Age:"+a_subject.getAge()+
//			  " fitValDir:"+a_subject.getFitnessValueDirectly()
//			  +" berechneteFit:"+gewonnen_prozent);
	  
	  return gewonnen_prozent;
  }
  
  
  /**
 * @param a_potentialSolution
 * @return Die Parameter als myTeamParameters
 */
public static myTeamParameters getParameters(IChromosome a_potentialSolution) {
	  myTeamParameters result = new myTeamParameters();
	  for (int i=0; i<myTeamParameters.ANZAHL_PARAMETER; i++) {
		  result.setParameter(i, (Double) a_potentialSolution.getGene(i).getAllele());
	}
	  return result;
  }
}
