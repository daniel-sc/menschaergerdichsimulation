/**
 * 
 */
package teams.myTeam;

import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.InvalidConfigurationException;
import org.jgap.impl.IntegerGene;

/**
 * @author daniel
 * @deprecated die original implementation macht doch mehr sinn!
 */
public class myIntegerGene extends IntegerGene {
	
	private static final long serialVersionUID = 1L;
	
	public myIntegerGene(Configuration aConfig)
			throws InvalidConfigurationException {
		super(aConfig);
		// TODO Auto-generated constructor stub
	}

	public myIntegerGene() throws InvalidConfigurationException {
		super();
		// TODO Auto-generated constructor stub
	}

	public myIntegerGene(Configuration conf, int i, int parameterMaximum) throws InvalidConfigurationException {		
		super(conf, i, parameterMaximum);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Scales value uniformly, i.e. a_percentage of 1 sets the value to
	 * the upper bound and all positive percentages scale linearly between
	 * the current value and the upper bound. 
	 * Analogously for negative percentages.
	 * --> PROBABLY NOT GOOD <--
	 * @param a_index ignored (because there is only 1 atomic element)
	 * @param a_percentage percentage of mutation (greater than -1 and smaller
	 * than 1)
	 *
	 * @author Daniel Schreiber
	 * @since 1.1
	 */
	public void applyMutation(final int a_index, final double a_percentage) {
		int add;
		if(a_percentage >= 0) {			
			add = (int) Math.round(((long) getUpperBounds() - (long) intValue()) * a_percentage);			
		} else {
			add = (int) Math.round(((long) intValue() - (long) getLowerBounds()) * a_percentage); //negative!
		}
		//System.out.println("myIntegerGene.applyMutation: add="+add);
		setAllele(new Integer(intValue()+add));	
	}
	
	@Override
	protected Gene newGeneInternal() {
	    try {
	        myIntegerGene result = new myIntegerGene(getConfiguration(), 
	        		getLowerBounds(), getUpperBounds());
	        return result;
	      } catch (InvalidConfigurationException iex) {
	        throw new IllegalStateException(iex.getMessage());
	      }
	}

}
