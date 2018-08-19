package spectum.openshiftdemo.similarity;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import spectum.openshiftdemo.person.Person;

@RunWith(MockitoJUnitRunner.class)
public class DefaultSimilarityFinderTest {

	private PairDistance pairDistance = this::getIdDistanance;
	private DefaultSimilarityFinder defaultSimilarityFinder;

	@Before
	public void initFinder() {
		defaultSimilarityFinder = new DefaultSimilarityFinder(pairDistance);
	}

	@Test
	public void testGetSimilarPairs() throws Exception {
		Collection<Person> personsAB = createTestPersons(1, 2, 3, 4);
		List<SimilarPair> similarPairs = defaultSimilarityFinder.getSimilarPairs(personsAB);
		assertEquals(6, similarPairs.size());
	}

	Integer getIdDistanance(Pair pair) {
		Integer idA = Integer.valueOf(pair.getPersonA().getId());
		Integer idB = Integer.valueOf(pair.getPersonB().getId());
		return Math.abs(idA - idB);
	}

	@Test
	public void testGetAllPairs() throws Exception {
		Collection<Person> personsAB = createTestPersons(1, 2);
		Collection<Pair> allPairsAB = defaultSimilarityFinder.getAllPairs(personsAB);
		assertEquals(1, allPairsAB.size());

		Collection<Person> personsABC = createTestPersons(1, 2, 3);
		Collection<Pair> allPairsABC = defaultSimilarityFinder.getAllPairs(personsABC);
		assertEquals(3, allPairsABC.size());

		Collection<Person> personsABCD = createTestPersons(1, 2, 3, 4);
		Collection<Pair> allPairsABCD = defaultSimilarityFinder.getAllPairs(personsABCD);
		assertEquals(6, allPairsABCD.size());

	}

	private List<Person> createTestPersons(int... values) {
		List<Person> persons = new LinkedList<>();
		for (int i = 0; i < values.length; i++) {
			Person p = new Person();
			String id = String.valueOf(values[i]);
			p.setId(id);
			persons.add(p);
		}
		return Collections.unmodifiableList(persons);
	}

}
