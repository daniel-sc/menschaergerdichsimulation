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
//package org.jgap.impl;

import org.jgap.*;
import org.jgap.event.*;
import org.jgap.impl.*;
import org.jgap.util.*;

/**
 * The DefaultConfiguration class simplifies the JGAP configuration
 * process by providing default configuration values for many of the
 * configuration settings. The following values must still be provided:
 * the sample Chromosome, population size, and desired fitness function.
 * All other settings may also be changed in the normal fashion for
 * those who wish to specify other custom values.
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @since 1.0
 */
public class myConfiguration
    extends Configuration implements ICloneable {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/** String containing the CVS revision. Read out via reflection!*/
  //private final static String CVS_REVISION = "$Revision: 1.26 $";

  public myConfiguration() {
    this("","");
  }

  /**
   * Constructs a new DefaultConfiguration instance with a number of
   * configuration settings set to default values. It is still necessary
   * to set the sample Chromosome, population size, and desired fitness
   * function. Other settings may optionally be altered as desired.
   *
   * @param a_id unique id for the configuration within the current thread
   * @param a_name informative name of the configuration, may be null
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
   */
  public myConfiguration(String a_id, String a_name) {
    super(a_id, a_name);
    try {
      setBreeder(new GABreeder());
      setRandomGenerator(new StockRandomGenerator());
      setEventManager(new EventManager());
      
      BestChromosomesSelector bestChromsSelector = new BestChromosomesSelector(
          this, 0.90d);
      
      bestChromsSelector.setDoubletteChromosomesAllowed(true);
      addNaturalSelector(bestChromsSelector, false);
//      StandardPostSelector sel = new StandardPostSelector(this);
//      addNaturalSelector(sel, false);
//      mySelector sel = new mySelector(this);
//      addNaturalSelector(sel, false);
//      addNaturalSelector(new BestChromosomesSelector(this),false);
      setMinimumPopSizePercent(0);
      
//      setSelectFromPrevGen(1.0d);
      setSelectFromPrevGen(0.9d);
      setKeepPopulationSizeConstant(true);
      setFitnessEvaluator(new DefaultFitnessEvaluator());
      //setChromosomePool(new ChromosomePool());
      
      //erst 'nachkommen erzeugen' dann 'mutieren':
      
      addGeneticOperator(new myMergeOperator(this, 4));
      addGeneticOperator(new myMutationOperator(this, 2/*12*/));
      
    }
    catch (InvalidConfigurationException e) {
      throw new RuntimeException(
          "Fatal error: DefaultConfiguration class could not use its "
          + "own stock configuration values. This should never happen. "
          + "Please report this as a bug to the JGAP team.");
    }
  }

  /**
   * @return deep clone of this instance
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public Object clone() {
    return super.clone();
  }
}
