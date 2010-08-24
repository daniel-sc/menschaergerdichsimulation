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


import java.io.*;

import org.jgap.*;
import org.jgap.data.*;
import org.jgap.impl.*;
import org.jgap.xml.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

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

	private static final int PARAMETER_MAXIMUM = 1000;

	private static final int ANZAHL_PARAMETER = 7;

	/** String containing the CVS revision. Read out via reflection!*/
	//private final static String CVS_REVISION = "$Revision: 1.10 $";

	/**
	 * The total number of times we'll let the population evolve.
	 */
	private static final int MAX_ALLOWED_EVOLUTIONS = 20;//140;

	private static final int POPULATION_SIZE = 20;//50;

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
		Configuration conf = new DefaultConfiguration();
		conf.setPreservFittestIndividual(true);
		// Set the fitness function we want to use. We construct it with
		// the target volume passed in to this method.
		// ---------------------------------------------------------
		FitnessFunction myFunc = new EAmyTeamFitnessFunction();
		conf.setFitnessFunction(myFunc);
		// Now we need to tell the Configuration object how we want our
		// Chromosomes to be setup. We do that by actually creating a
		// sample Chromosome and then setting it on the Configuration
		// object. As mentioned earlier, we want our Chromosomes to each
		// have as many genes as there are different items available. We want the
		// values (alleles) of those genes to be integers, which represent
		// how many items of that type we have. We therefore use the
		// IntegerGene class to represent each of the genes. That class
		// also lets us specify a lower and upper bound, which we set
		// to senseful values (i.e. maximum possible) for each item type.
		// --------------------------------------------------------------
		Gene[] sampleGenes = new Gene[ANZAHL_PARAMETER];
		for (int i = 0; i < sampleGenes.length; i++) {
			sampleGenes[i] = new IntegerGene(conf, 0,PARAMETER_MAXIMUM);
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
			System.out.println("Alte Population aus Datei gelesen!");
		}
		catch (Exception fex) {
			population = Genotype.randomInitialGenotype(conf);
		}
		//population = Genotype.randomInitialGenotype(conf); //macht keinen sinn - oder??
		// Evolve the population. Since we don't know what the best answer
		// is going to be, we just evolve the max number of times.
		// ---------------------------------------------------------------
		for (int i = 0; i < MAX_ALLOWED_EVOLUTIONS; i++) {
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
				bestSolutionSoFar.getFitnessValue()+" mit folgenden Parametern:");
		for (Gene gene : bestSolutionSoFar.getGenes()) {
			System.out.println((Integer) gene.getAllele());
		}

	}

	/**
	 * Main method. A single command-line argument is expected, which is the
	 * volume to create (in other words, 75 would be equal to 75 ccm).
	 *
	 * @param args first and single element in the array = volume of the knapsack
	 * to fill as a double value
	 *
	 * @author Klaus Meffert
	 * @since 2.3
	 */
	public static void main(String[] args) {
		try {
			findeBesteParameter();
		} catch (InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
