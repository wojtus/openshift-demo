package spectum.openshiftdemo.similarity;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.annotations.VisibleForTesting;

import spectum.openshiftdemo.person.Person;

class DefaultSimilarityFinder implements SimilarityFinder {

	@Override
	public List<SimilarPair> getSimilarPairs(Collection<Person> persons) {
		Collection<Pair> allPairs = getAllPairs(persons);
		Collection<SimilarPair> similarPairs = createSimilarPairs(allPairs);
		List<SimilarPair> similarPairsList = similarPairs.stream().//
				sorted(Comparator.comparingInt(SimilarPair::getSimilarity)).//
				collect(Collectors.toList());
		return Collections.unmodifiableList(similarPairsList);
	}

	private Collection<SimilarPair> createSimilarPairs(Collection<Pair> allPairs) {
		// TODO Auto-generated method stub
		return null;
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
