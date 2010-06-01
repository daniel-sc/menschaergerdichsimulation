package madn;

//--------------------------------------
// Systematically generate permutations. 
//--------------------------------------
//from: http://www.merriampark.com/perm.htm

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class PermutationGenerator {

  private int[] a;
  private BigInteger numLeft;
  private BigInteger total;

  //-----------------------------------------------------------
  // Constructor. WARNING: Don't make n too large.
  // Recall that the number of permutations is n!
  // which can be very large, even when n is as small as 20 --
  // 20! = 2,432,902,008,176,640,000 and
  // 21! is too big to fit into a Java long, which is
  // why we use BigInteger instead.
  //----------------------------------------------------------
  public PermutationGenerator (int n) {
    if (n < 1) {
      throw new IllegalArgumentException ("Min 1");
    }
    a = new int[n];
    total = getFactorial (n);
    reset ();
  }

  //------
  // Reset
  //------
  public void reset () {
    for (int i = 0; i < a.length; i++) {
      a[i] = i;
    }
    numLeft = new BigInteger (total.toString ());
  }

  //------------------------------------------------
  // Return number of permutations not yet generated
  //------------------------------------------------
  public BigInteger getNumLeft () {
    return numLeft;
  }

  //------------------------------------
  // Return total number of permutations
  //------------------------------------
  public BigInteger getTotal () {
    return total;
  }

  //-----------------------------
  // Are there more permutations?
  //-----------------------------
  public boolean hasMore () {
    return numLeft.compareTo (BigInteger.ZERO) == 1;
  }

  //------------------
  // Compute factorial
  //------------------
  private static BigInteger getFactorial (int n) {
    BigInteger fact = BigInteger.ONE;
    for (int i = n; i > 1; i--) {
      fact = fact.multiply (new BigInteger (Integer.toString (i)));
    }
    return fact;
  }

  //--------------------------------------------------------
  // Generate next permutation (algorithm from Rosen p. 284)
  //--------------------------------------------------------
  public int[] getNext () {

    if (numLeft.equals (total)) {
      numLeft = numLeft.subtract (BigInteger.ONE);
      return a;
    }

    int temp;

    // Find largest index j with a[j] < a[j+1]
    int j = a.length - 2;
    while (a[j] > a[j+1]) {
      j--;
    }

    // Find index k such that a[k] is smallest integer
    // greater than a[j] to the right of a[j]
    int k = a.length - 1;
    while (a[j] > a[k]) {
      k--;
    }

    // Interchange a[j] and a[k]
    temp = a[k];
    a[k] = a[j];
    a[j] = temp;

    // Put tail end of permutation after jth position in increasing order

    int r = a.length - 1;
    int s = j + 1;

    while (r > s) {
      temp = a[s];
      a[s] = a[r];
      a[r] = temp;
      r--;
      s++;
    }

    numLeft = numLeft.subtract (BigInteger.ONE);
    return a;

  }
  /**
   * currently O(n^2), can probably made faster..
   * @param from
   * @param to
   * @return permutation p such that p(from(.))=to(.)
   * @author daniel
   */
  static int[] difference(int from[], int to[]) {
	  if(to.length!=from.length)
		  throw new IllegalArgumentException ("length must match!");
	  
	  int result[] = new int[to.length];
	  
	  for(int t=0; t<to.length; t++)  {
		  for(int f=0; f<from.length; f++) {
			  if(from[f]==to[t]) {
				  result[f] = t;
				  break;
			  }
		  }
	  }
	return result;
  }

/**
 * 
 * @param <E>
 * @param <E> type of objects
 * @param objects
 * @param permutation
 * @return the permutation of the objects
 */
public static <E> List<E> permute(List<E> objects, int[] permutation) {
	if(objects.size()!=permutation.length)
		throw new IllegalArgumentException("Arguments length must match!");
	
	//E result[] = objects.clone();
	List<E> result = new ArrayList<E>(objects);
	for(int i=0; i<objects.size(); i++)
		result.set(permutation[i], objects.get(i));
	return result;
}


public static int[] compose(int[] firstPerm, int[] lastPerm) {
	if(firstPerm.length!=lastPerm.length)
		throw new IllegalArgumentException("Arguments length must match!");
	
	int result[] = new int[firstPerm.length];
	for(int i=0; i<result.length; i++) {
		result[i] = lastPerm[firstPerm[i]];
	}
	return result;
}

public static int[] invert(int[] oldperm) {
	int result[] = new int[oldperm.length];
	for(int i=0; i<oldperm.length; i++) {
		result[oldperm[i]] = i;
	}
	return result;
}

public static void print(String pre, int[] perm) {
	System.out.print(pre+"\t");
	for(int i=0; i<perm.length; i++)
		System.out.print(perm[i]);
	System.out.println();
}

}
