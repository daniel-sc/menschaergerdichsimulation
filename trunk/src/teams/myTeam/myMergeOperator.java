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

import java.util.*;
import org.jgap.*;

/**
 * The crossover operator randomly selects two Chromosomes from the
 * population and "mates" them by randomly picking a gene and then
 * swapping that gene and all subsequent genes between the two
 * Chromosomes. The two modified Chromosomes are then added to the
 * list of candidate Chromosomes.
 *
 * If you work with CompositeGene's, this operator expects them to contain
 * genes of the same type (e.g. IntegerGene). If you have mixed types, please
 * provide your own crossover operator.
 *
 * This CrossoverOperator supports both fixed and dynamic crossover rates.
 * A fixed rate is one specified at construction time by the user. This
 * operation is performed 1/m_crossoverRate as many times as there are
 * Chromosomes in the population. Another possibility is giving the crossover
 * rate as a percentage. A dynamic rate is one determined by this class on the
 * fly if no fixed rate is provided.
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @author Chris Knowles
 * @since 1.0
 */
public class myMergeOperator
    extends BaseGeneticOperator implements Comparable {
  /** String containing the CVS revision. Read out via reflection!*/
  //private final static String CVS_REVISION = "$Revision: 1.45 $";

  /**
   * The current crossover rate used by this crossover operator (mutual
   * exclusive to m_crossoverRatePercent and m_crossoverRateCalc).
   */
  private int m_crossoverRate;

  /**
   * Crossover rate in percentage of population size (mutual exclusive to
   * m_crossoverRate and m_crossoverRateCalc).
   */
  private double m_crossoverRatePercent;

//  /**
//   * Calculator for dynamically determining the crossover rate (mutual exclusive
//   * to m_crossoverRate and m_crossoverRatePercent)
//   */
//  private IUniversalRateCalculator m_crossoverRateCalc;
//
//  /**
//   * true: x-over before and after a randomly chosen x-over point
//   * false: only x-over after the chosen point.
//   */
//  private boolean m_allowFullCrossOver;

  /**
   * true: also x-over chromosomes with age of zero (newly created chromosomes)
   */
  private boolean m_xoverNewAge;

  /**
   * Constructs a new instance of this CrossoverOperator without a specified
   * crossover rate, this results in dynamic crossover rate being turned off.
   * This means that the crossover rate will be fixed at populationsize/2.<p>
   * Attention: The configuration used is the one set with the static method
   * Genotype.setConfiguration.
   *
   * @throws InvalidConfigurationException
   *
   * @author Chris Knowles
   * @since 2.0
   */
  public myMergeOperator()
      throws InvalidConfigurationException {
    super(Genotype.getStaticConfiguration());
    init();
  }

  /**
   * Constructs a new instance of this CrossoverOperator without a specified
   * crossover rate, this results in dynamic crossover rate being turned off.
   * This means that the crossover rate will be fixed at populationsize/2.
   *
   * @param a_configuration the configuration to use
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public myMergeOperator(final Configuration a_configuration)
      throws InvalidConfigurationException {
    super(a_configuration);
    init();
  }

  /**
   * Initializes certain parameters.
   *
   * @author Klaus Meffert
   * @since 3.3.2
   */
  protected void init() {
    // Set the default crossoverRate.
    // ------------------------------
    m_crossoverRate = 6;
    m_crossoverRatePercent = -1;
//    setCrossoverRateCalc(null);
//    setAllowFullCrossOver(true);
    setXoverNewAge(true);
  }


  /**
   * Constructs a new instance of this CrossoverOperator with the given
   * crossover rate.
   *
   * @param a_configuration the configuration to use
   * @param a_desiredCrossoverRate the desired rate of crossover
   * @throws InvalidConfigurationException
   *
   * @author Chris Knowles
   * @author Klaus Meffert
   * @since 3.0 (since 2.0 without a_configuration)
   */
  public myMergeOperator(final Configuration a_configuration,
                           final int a_desiredCrossoverRate)
      throws InvalidConfigurationException {
    this(a_configuration, a_desiredCrossoverRate, true);
  }

  /**
   * Constructs a new instance of this CrossoverOperator with the given
   * crossover rate. No new chromosomes are x-overed.
   *
   * @param a_configuration the configuration to use
   * @param a_desiredCrossoverRate the desired rate of crossover
   * @param a_allowFullCrossOver true: x-over before AND after x-over point,
   * false: only x-over after x-over point
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.3.2
   */
  public myMergeOperator(final Configuration a_configuration,
                           final int a_desiredCrossoverRate,
                           boolean a_allowFullCrossOver)
      throws InvalidConfigurationException {
    this(a_configuration, a_desiredCrossoverRate, a_allowFullCrossOver, false);
  }

  /**
   * Constructs a new instance of this CrossoverOperator with the given
   * crossover rate.
   *
   * @param a_configuration the configuration to use
   * @param a_desiredCrossoverRate the desired rate of crossover
   * @param a_allowFullCrossOver true: x-over before AND after x-over point,
   * false: only x-over after x-over point
   * @throws InvalidConfigurationException
   * @param a_xoverNewAge true: also x-over chromosomes with age of zero (newly
   * created chromosomes)
   *
   * @author Klaus Meffert
   * @since 3.3.2
   */
  public myMergeOperator(final Configuration a_configuration,
                           final int a_desiredCrossoverRate,
                           final boolean a_allowFullCrossOver,
                           final boolean a_xoverNewAge)
      throws InvalidConfigurationException {
    super(a_configuration);
    if (a_desiredCrossoverRate < 1) {
      throw new IllegalArgumentException("Crossover rate must be greater zero");
    }
    m_crossoverRate = a_desiredCrossoverRate;
    m_crossoverRatePercent = -1;
//    setCrossoverRateCalc(null);
//    setAllowFullCrossOver(a_allowFullCrossOver);
    setXoverNewAge(a_xoverNewAge);
  }

  /**
   * Constructs a new instance of this CrossoverOperator with the given
   * crossover rate. No new chromosomes are x-overed.
   *
   * @param a_configuration the configuration to use
   * @param a_crossoverRatePercentage the desired rate of crossover in
   * percentage of the population
   * @throws InvalidConfigurationException
   *
   * @author Chris Knowles
   * @author Klaus Meffert
   * @since 3.0 (since 2.0 without a_configuration)
   */
  public myMergeOperator(final Configuration a_configuration,
                           final double a_crossoverRatePercentage)
      throws InvalidConfigurationException {
    this(a_configuration, a_crossoverRatePercentage, true);
  }

  /**
   * Constructs a new instance of this CrossoverOperator with the given
   * crossover rate. No new chromosomes are x-overed.
   *
   * @param a_configuration the configuration to use
   * @param a_crossoverRatePercentage the desired rate of crossover in
   * percentage of the population
   * @param a_allowFullCrossOver true: x-over before AND after x-over point,
   * false: only x-over after x-over point
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.3.2.
   */
  public myMergeOperator(final Configuration a_configuration,
                           final double a_crossoverRatePercentage,
                           boolean a_allowFullCrossOver)
      throws InvalidConfigurationException {
    this(a_configuration, a_crossoverRatePercentage, a_allowFullCrossOver, false);
  }

  /**
   * Constructs a new instance of this CrossoverOperator with the given
   * crossover rate.
   *
   * @param a_configuration the configuration to use
   * @param a_crossoverRatePercentage the desired rate of crossover in
   * percentage of the population
   * @param a_allowFullCrossOver true: x-over before AND after x-over point,
   * false: only x-over after x-over point
   * @param a_xoverNewAge true: also x-over chromosomes with age of zero (newly
   * created chromosomes)
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.3.2.
   */
  public myMergeOperator(final Configuration a_configuration,
                           final double a_crossoverRatePercentage,
                           final boolean a_allowFullCrossOver,
                           final boolean a_xoverNewAge)
      throws InvalidConfigurationException {
    super(a_configuration);
    if (a_crossoverRatePercentage <= 0.0d) {
      throw new IllegalArgumentException("Crossover rate must be greater zero");
    }
    m_crossoverRatePercent = a_crossoverRatePercentage;
    m_crossoverRate = -1;
//    setCrossoverRateCalc(null);
//    setAllowFullCrossOver(a_allowFullCrossOver);
    setXoverNewAge(a_xoverNewAge);
  }

  /**
   * Does the crossing over.
   *
   * @param a_population the population of chromosomes from the current
   * evolution prior to exposure to crossing over
   * @param a_candidateChromosomes the pool of chromosomes that have been
   * selected for the next evolved population
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 2.0
   */
  public void operate(final Population a_population,
                      final List a_candidateChromosomes) {
		System.out.println("merge_sta: candiates.length="+a_candidateChromosomes.size()
				+" population="+a_population.size());
    // Work out the number of crossovers that should be performed.
    // -----------------------------------------------------------
    int size = Math.min(getConfiguration().getPopulationSize(),
                        a_population.size());
    int numCrossovers = 0;
    if (m_crossoverRate > 0) {
      numCrossovers = size / m_crossoverRate;
    }
    else {
      numCrossovers = (int) (size * m_crossoverRatePercent);
    }
    RandomGenerator generator = getConfiguration().getRandomGenerator();
    IGeneticOperatorConstraint constraint = getConfiguration().
        getJGAPFactory().getGeneticOperatorConstraint();
    // For each crossover, grab two random chromosomes, pick a random
    // locus (gene location), and then swap that gene and all genes
    // to the "right" (those with greater loci) of that gene between
    // the two chromosomes.
    // --------------------------------------------------------------
    int index1, index2;
    for (int i = 0; i < numCrossovers; i++) {
      index1 = generator.nextInt(size);
      index2 = generator.nextInt(size);
      
      IChromosome result = (IChromosome) a_population.getChromosome(index1).clone();
      IChromosome otherChrom = a_population.getChromosome(index2);
      //bei jeden Gen wird zufaellig entschieden ob er von dem einen
      //oder dem anderen Chromosom uebernommen wird:
      for(int j=0; j<result.size(); j++) {
    	  if(myMutationOperator.getRandomBool()) {
    		  result.getGene(j).setAllele(otherChrom.getGene(j).getAllele());
    	  }
      }   
      a_candidateChromosomes.add(result);
    }
    System.out.println("merge_end: candiates.length="+a_candidateChromosomes.size());
  }

  /**
   * Compares the given object to this one.
   *
   * @param a_other the instance against which to compare this instance
   * @return a negative number if this instance is "less than" the given
   * instance, zero if they are equal to each other, and a positive number if
   * this is "greater than" the given instance
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public int compareTo(final Object a_other) {
    /**@todo consider Configuration*/
	  myMergeOperator op = (myMergeOperator) a_other;
    if (a_other == null) {
      return 1;
    }
    if (m_crossoverRate != op.m_crossoverRate) {
      if (m_crossoverRate > op.m_crossoverRate) {
        return 1;
      }
      else {
        return -1;
      }
    }
    if (m_xoverNewAge != op.m_xoverNewAge) {
      if (m_xoverNewAge) {
        return 1;
      }
      else {
        return -1;
      }
    }
    // Everything is equal. Return zero.
    // ---------------------------------
    return 0;
  }

  /**
   * @return the crossover rate set
   *
   * @author Klaus Meffert
   * @since 3.3.2
   */
  public int getCrossOverRate() {
    return m_crossoverRate;
  }

  /**
   * @return the crossover rate set
   *
   * @author Klaus Meffert
   * @since 3.3.2
   */
  public double getCrossOverRatePercent() {
    return m_crossoverRatePercent;
  }

  /**
   * @param a_xoverNewAge true: also x-over chromosomes with age of zero (newly
   * created chromosomes)
   *
   * @author Klaus Meffert
   * @since 3.3.2
   */
  public void setXoverNewAge(boolean a_xoverNewAge) {
    m_xoverNewAge = a_xoverNewAge;
  }

  /**
   * @return a_xoverNewAge true: also x-over chromosomes with age of zero (newly
   * created chromosomes)
   *
   * @author Klaus Meffert
   * @since 3.3.2
   */
  public boolean isXoverNewAge() {
    return m_xoverNewAge;
  }
}
