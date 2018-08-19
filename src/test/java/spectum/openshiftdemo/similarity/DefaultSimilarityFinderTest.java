package spectum.openshiftdemo.similarity;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;

import spectum.openshiftdemo.person.Person;

public class DefaultSimilarityFinderTest {

	private final DefaultSimilarityFinder defaultSimilarityFinder = new DefaultSimilarityFinder();

	@Test
	public void testGetSimilarPairs() throws Exception {
		throw new RuntimeException("not yet implemented");
	}

	@Test
	public void testGetAllPairs() throws Exception {

		Person a = new Person();
		a.setId("a");
		Person b = new Person();
		b.setId("b");
		Person c = new Person();
		c.setId("c");
		Person d = new Person();
		d.setId("d");
		Collection<Person> personsAB = Arrays.asList(a, b);
		Collection<Pair> allPairsAB = defaultSimilarityFinder.getAllPairs(personsAB);
		assertEquals(1, allPairsAB.size());

		Collection<Person> personsABC = Arrays.asList(a, b, c);
		Collection<Pair> allPairsABC = defaultSimilarityFinder.getAllPairs(personsABC);
		assertEquals(3, allPairsABC.size());

		Collection<Person> personsABCD = Arrays.asList(a, b, c, d);
		Collection<Pair> allPairsABCD = defaultSimilarityFinder.getAllPairs(personsABCD);
		assertEquals(6, allPairsABCD.size());

	}

}
