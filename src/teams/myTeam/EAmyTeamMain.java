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


import java.io.File;

import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.FitnessFunction;
import org.jgap.Gene;
import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.data.DataTreeBuilder;
import org.jgap.data.IDataCreators;
import org.jgap.impl.*;
import org.jgap.xml.XMLDocumentBuilder;
import org.jgap.xml.XMLManager;
import org.w3c.dom.Document;

/**
 * This class provides an implementation of the classic knapsack problem
 * using a genetic algorithm. The goal of the problem is to reach a given
 * volume (of a knapsack) by putting a number of items into the knapsack.
 * The closer the sum of the item volumes to the given volume the better.
 * <p>
 * For further descriptions, compare the "coins" example also provided.
 *
 * @author Klaus Meffert
 * @since 2.3
 */
public class EAmyTeamMain {
	private static final String XML_FILENAME = "myTeamJGAP.xml";

//	private static final int PARAMETER_MAXIMUM = 1000;
//
//	private static final int ANZAHL_PARAMETER = 7;

	/** String containing the CVS revision. Read out via reflection!*/
	//private final static String CVS_REVISION = "$Revision: 1.10 $";

	/**
	 * The total number of times we'll let the population evolve.
	 */
	private static int MAX_ALLOWED_EVOLUTIONS = 10;//140;

	private static int POPULATION_SIZE = 10;//50;

	/**
	 * Executes the genetic algorithm to determine the minimum number of
	 * items necessary to make up the given target volume. The solution will then
	 * be written to the console.
	 *
	 * @param a_knapsackVolume the target volume for which this method is
	 * attempting to produce the optimal list of items
	 *
	 * @throws Exception
	 *
	 * @author Klaus Meffert
	 * @throws InvalidConfigurationException 
	 * @since 2.3
	 */
	public static void findeBesteParameter() throws InvalidConfigurationException {
		// Start with a DefaultConfiguration, which comes setup with the
		// most common settings.
		// -------------------------------------------------------------
		Configuration conf = new myConfiguration();
		conf.setPreservFittestIndividual(true);
		
		//TODO: check: viel Mehraufwand?? ja!
		//conf.setAlwaysCaculateFitness(true);
		
		// Set the fitness function we want to use. We construct it with
		// the target volume passed in to this method.
		// ---------------------------------------------------------
		FitnessFunction myFunc = new EAmyTeamFitnessFunction();
		conf.setFitnessFunction(myFunc);
		
		//--> myConfiguration
		//conf.addGeneticOperator(new MutationOperator(conf,2));		
		//conf.addGeneticOperator(new CrossoverOperator(conf, .2));
		
		// Now we need to tell the Configuration object how we want our
		// Chromosomes to be setup. We do that by actually creating a
		// sample Chromosome and then setting it on the Configuration
		// object.
		
		myTeamParameters dummyParam = new myTeamParameters(); //nur fuer min/max
		
		Gene[] sampleGenes = new Gene[myTeamParameters.ANZAHL_PARAMETER];
		for (int i = 0; i < sampleGenes.length; i++) {
			sampleGenes[i] = new DoubleGene(conf, dummyParam.getMin(i), dummyParam.getMax(i));
		}
		IChromosome sampleChromosome = new Chromosome(conf, sampleGenes);
		conf.setSampleChromosome(sampleChromosome);
		
		// Finally, we need to tell the Configuration object how many
		// Chromosomes we want in our population. The more Chromosomes,
		// the larger number of potential solutions (which is good for
		// finding the answer), but the longer it will take to evolve
		// the population (which could be seen as bad).
		// ------------------------------------------------------------
		conf.setPopulationSize(POPULATION_SIZE);
		
		// Create random initial population of Chromosomes.
		// Here we try to read in a previous run via XMLManager.readFile(..)
		// for demonstration purpose!
		// -----------------------------------------------------------------
		Genotype population;
		try {
			Document doc = XMLManager.readFile(new File(XML_FILENAME));
			population = XMLManager.getGenotypeFromDocument(conf, doc);
			//TODO mit zufaelligen auffuellen??
			System.out.println("Alte Population aus Datei gelesen!");
		}
		catch (Exception fex) {
			population = Genotype.randomInitialGenotype(conf);
		}
	
		// Evolve the population. Since we don't know what the best answer
		// is going to be, we just evolve the max number of times.
		// ---------------------------------------------------------------
		
		
		for (int i = 0; i < MAX_ALLOWED_EVOLUTIONS; i++) {
			System.out.println("\nPopulation Nr. "+i+":");
			printPopulation(population);
			
			population.evolve();			
		}
		
		// Save progress to file. A new run of this example will then be able to
		// resume where it stopped before!
		// ---------------------------------------------------------------------

		// represent Genotype as tree with elements Chromomes and Genes
		// ------------------------------------------------------------
		try{
			DataTreeBuilder builder = DataTreeBuilder.getInstance();
			IDataCreators doc2 = builder.representGenotypeAsDocument(population);
			// create XML document from generated tree
			// ---------------------------------------
			XMLDocumentBuilder docbuilder = new XMLDocumentBuilder();
			Document xmlDoc = (Document) docbuilder.buildDocument(doc2);
			XMLManager.writeFile(xmlDoc, new File(XML_FILENAME));
		} catch(Exception e) {
			e.printStackTrace();
		}
		// Display the best solution we found.
		// -----------------------------------
		IChromosome bestSolutionSoFar = population.getFittestChromosome();
		System.out.println("The best solution has a fitness value of " +
				bestSolutionSoFar.getFitnessValueDirectly()+" mit folgenden Parametern:");
		printChromosom(bestSolutionSoFar, true);
	}


	/**
	 * Gibt die Population auf der Konsole aus.
	 * @param population
	 */
	public static void printPopulation(Genotype population) {
		System.out.println("ERSTER\tF_VORNE\tGEG_UEB\tVERFOLG\t" +
				"WEG_GEG\tINS_HAU\tSCHLAGEN");
		for (Object chromosom : population.getPopulation().getChromosomes()) {
			printChromosom((IChromosome) chromosom, 
					((IChromosome) chromosom).getFitnessValueDirectly()>=population.getFittestChromosome().getFitnessValueDirectly() ? true : false);
		}
	}
	
	public static void printChromosom(IChromosome ch, boolean best) {
		myTeamParameters param = EAmyTeamFitnessFunction.getParameters(ch); 
		for (Double val : param.getParameters()) {
			System.out.print(myTeamParameters.doubleToString(val, 1) + "\t");
		}
		System.out.print("%-Gew: " 
				+ myTeamParameters.doubleToString(ch.getFitnessValueDirectly(), 2)
				+ "\t");
		if(best)
			System.out.println("<- curr fittest");
		else
			System.out.println();
		
	}


	/**
	 * erwartet 3 Parameter: population_size, max_evolutions, anz_simulationen
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length==3) {
			POPULATION_SIZE = Integer.parseInt(args[0]);
			MAX_ALLOWED_EVOLUTIONS = Integer.parseInt(args[1]);
			EAmyTeamFitnessFunction.ANZ_SIMULATIONEN = Integer.parseInt(args[2]);
		} else {
			System.out.println("Aufruf: mensch.jar population_size max_evolutions anz_simulationen");
			System.out.println("Achtung: die Rechenzeit waechst linear mit jedem Parameter!");
			return;
		}
		
		try {
			findeBesteParameter();
		} catch (InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
