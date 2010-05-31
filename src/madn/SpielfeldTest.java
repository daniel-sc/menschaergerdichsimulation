package madn;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

public class SpielfeldTest extends TestCase {
	
	Spielfeld feld;
	PermutationGenerator perm;

	protected void setUp() throws Exception {
		super.setUp();
		feld = new Spielfeld();
		perm = new PermutationGenerator(4);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testPerm() {
		int from[] = perm.getNext();
		perm.getNext();
		int to[] = perm.getNext();
		int to_bak[] = to.clone();
		int diff[] = PermutationGenerator.difference(from, to);
		for(int i=0; i<4; i++) {
			assertEquals(diff[from[i]], to[i]);
			assertEquals(PermutationGenerator.permute(to,PermutationGenerator.invert(to))[i], i);
			assertEquals(PermutationGenerator.permute(PermutationGenerator.invert(to),to)[i], i);
		}
		
		PermutationGenerator.permute(to, to);
		for(int i=0; i<4; i++) {
			assertEquals(to_bak[i], to[i]);
		}
		
//		perm.reset();
//		while(perm.hasMore()) {
//			int p[] = perm.getNext();
//			for(int i=0; i<p.length; i++)
//				System.out.print(""+p[i]);
//			System.out.println();
//		}
		
	}
	
	public void testZug() {
		Zug zug1 = new Zug(6,1,0,feld);
		Zug zug2 = new Zug(6,1,0,-1,10,false,-1);
		assertTrue(zug1.equals(zug2));
		assertTrue(zug2.equals(zug1));
		Set<Zug> set = new HashSet<Zug>(4);
		set.add(zug1);
		assertTrue(set.contains(zug1));
		assertTrue(set.contains(zug2));
	}

	@SuppressWarnings("deprecation")
	public void testZieheSpieler() {	
		//raussetzten:
		assertTrue(feld.zieheSpieler(1, 0, 6));
		assertTrue(feld.zieheSpieler(2, 0, 6));
		assertTrue(feld.zieheSpieler(3, 0, 6));
		assertTrue(feld.zieheSpieler(0, 0, 6));
		
		assertEquals(feld.spielfeld_figur[0], 0);
		assertEquals(feld.spielfeld_team[0], 0);
		
		assertEquals(feld.spielfeld_figur[20], 0);
		assertEquals(feld.spielfeld_team[20], 2);
		
		assertEquals(feld.spielfeld_figur[5], -1);
		assertEquals(feld.spielfeld_team[5], -1);
		
		assertEquals(feld.anzSpielerImStart(1),3);
		assertEquals(feld.anzSpielerImStart(2),3);
		assertEquals(feld.anzSpielerImStart(3),3);
		
		assertTrue(feld.zieheSpieler(0, 0, 5));
		assertTrue(feld.zieheSpieler(0, 0, 5));
		assertTrue(feld.zieheSpieler(0, 0, 5));
		assertTrue(feld.zieheSpieler(0, 0, 5));
		assertTrue(feld.zieheSpieler(0, 0, 5));
		assertTrue(feld.zieheSpieler(0, 0, 5));
		assertTrue(feld.zieheSpieler(0, 0, 5));
		assertTrue(feld.spielfeld_team[35]==0);
		assertTrue(feld.zieheSpieler(0, 0, 4));
		assertFalse(feld.zieheSpieler(0, 0, 5));
		assertTrue(feld.zieheSpieler(0, 0, 4));
		
		assertEquals(feld.anzSpielerImStart(1),4);
		assertEquals(feld.anzSpielerImStart(2),4);
		assertEquals(feld.anzSpielerImStart(3),4);
		
		assertTrue(feld.zieheSpieler(1, 0, 6));
		assertTrue(feld.zieheSpieler(1, 0, 5));
		assertTrue(feld.zieheSpieler(1, 0, 5));
		assertTrue(feld.zieheSpieler(1, 0, 5));
		assertTrue(feld.zieheSpieler(1, 0, 5));
		assertTrue(feld.zieheSpieler(1, 0, 5));
		assertTrue(feld.zieheSpieler(1, 0, 5));
		assertTrue(feld.zieheSpieler(1, 0, 5));
		assertTrue(feld.spielfeld_team[5]==1);
		assertTrue(feld.zieheSpieler(1, 0, 4));
		assertFalse(feld.zieheSpieler(1, 0, 5));
		assertTrue(feld.zieheSpieler(1, 0, 4));
		
		feld.zieheSpieler(1, 1, 6);
		for(int i=0; i<50; i++)
			feld.zieheSpieler(1, 1, 1);
		feld.zieheSpieler(1, 2, 6);
		for(int i=0; i<50; i++)
			feld.zieheSpieler(1, 2, 1);
		feld.zieheSpieler(1, 3, 6);
		assertEquals(feld.ende(), -1);
		for(int i=0; i<40; i++)
			feld.zieheSpieler(1, 3, 1);
		System.out.println(feld.ende());
		assertEquals(feld.ende(), 1);
		assertEquals(feld.anzSpielerImHaus(1), 4);
		
		HashSet<Integer> set = new HashSet<Integer>(4);
		set.add(5);
		set.add(34);
		assertFalse(set.contains(78));
		assertTrue(set.contains(5));
	}

//	public void testSpielerImStart() {
//		fail("Not yet implemented");
//	}

}
