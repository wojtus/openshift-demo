package spectum.openshiftdemo.similarity;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
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
		Flux<Person> personsAB = createTestPersons(1, 2, 3, 4);
		Flux<SimilarPair> similarPairs = defaultSimilarityFinder.getSimilarPairs(personsAB);
		StepVerifier.create(similarPairs).expectNextCount(6).expectComplete().verify();
	}

	Integer getIdDistanance(Pair pair) {
		Integer idA = Integer.valueOf(pair.getPersonA().getId());
		Integer idB = Integer.valueOf(pair.getPersonB().getId());
		return Math.abs(idA - idB);
	}

	@Test
	public void testGetAllPairs() throws Exception {
		Flux<Person> personsAB = createTestPersons(1, 2);
		Flux<Pair> allPairsAB = defaultSimilarityFinder.getAllPairs(personsAB);
		StepVerifier.create(allPairsAB).expectNextCount(1).expectComplete().verify();

		Flux<Person> personsABC = createTestPersons(1, 2, 3);
		Flux<Pair> allPairsABC = defaultSimilarityFinder.getAllPairs(personsABC);
		StepVerifier.create(allPairsABC).expectNextCount(3).expectComplete().verify();

		Flux<Person> personsABCD = createTestPersons(1, 2, 3, 4);
		Flux<Pair> allPairsABCD = defaultSimilarityFinder.getAllPairs(personsABCD);
		StepVerifier.create(allPairsABCD).expectNextCount(6).expectComplete().verify();

	}

	private Flux<Person> createTestPersons(int... values) {
		List<Person> persons = new LinkedList<>();
		for (int i = 0; i < values.length; i++) {
			Person p = new Person();
			String id = String.valueOf(values[i]);
			p.setId(id);
			persons.add(p);
		}
		return Flux.fromIterable(persons);
	}

}
