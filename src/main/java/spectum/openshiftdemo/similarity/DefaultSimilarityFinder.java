package spectum.openshiftdemo.similarity;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.annotations.VisibleForTesting;

import spectum.openshiftdemo.person.Person;

class DefaultSimilarityFinder implements SimilarityFinder {

	private final PairDistance pairDistance;

	DefaultSimilarityFinder(PairDistance pairDistance) {
		this.pairDistance = pairDistance;
	}

	@Override
	public List<SimilarPair> getSimilarPairs(Collection<Person> persons) {
		Collection<Pair> allPairs = getAllPairs(persons);
		Stream<SimilarPair> similarPairs = createSimilarPairs(allPairs);
		List<SimilarPair> similarPairsList = similarPairs.//
				sorted(Comparator.comparingInt(SimilarPair::getSimilarity)).//
				collect(Collectors.toList());
		return Collections.unmodifiableList(similarPairsList);
	}

	private Stream<SimilarPair> createSimilarPairs(Collection<Pair> allPairs) {
		return allPairs.stream().map(this::createSimilarPair);
	}

	private SimilarPair createSimilarPair(Pair pair) {
		Integer similarity = pairDistance.apply(pair);
		return new SimilarPair(pair.getPersonA(), pair.getPersonB(), similarity);
	}

	@VisibleForTesting
	Collection<Pair> getAllPairs(Collection<Person> persons) {
		Person[] personArray = persons.toArray(new Person[] {});
		Collection<Pair> pairs = new LinkedList<>();
		for (int y = 0; y < persons.size() - 1; y++) {
			for (int x = y + 1; x < persons.size(); x++) {
				Person a = personArray[y];
				Person b = personArray[x];
				Pair pair = new Pair(a, b);
				pairs.add(pair);
			}
		}
		return pairs;
	}

}
