package spectum.openshiftdemo.similarity;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.google.common.annotations.VisibleForTesting;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import spectum.openshiftdemo.person.Person;

@Component
class DefaultSimilarityFinder implements SimilarityFinder {

	private final PairDistance pairDistance;

	DefaultSimilarityFinder(PairDistance pairDistance) {
		this.pairDistance = pairDistance;
	}

	@Override
	public Flux<SimilarPair> getSimilarPairs(Flux<Person> persons) {
		Flux<Pair> allPairs = getAllPairs(persons);
		Flux<SimilarPair> similarPairs = createSimilarPairs(allPairs);
		Flux<SimilarPair> sortedSimilarPairs = similarPairs.//
				sort(Comparator.comparingInt(SimilarPair::getSimilarity));
		return sortedSimilarPairs;
	}

	private Flux<SimilarPair> createSimilarPairs(Flux<Pair> allPairs) {
		return allPairs.map(this::createSimilarPair);
	}

	private SimilarPair createSimilarPair(Pair pair) {
		Integer similarity = pairDistance.apply(pair);
		return new SimilarPair(pair.getPersonA(), pair.getPersonB(), similarity);
	}

	@VisibleForTesting
	Flux<Pair> getAllPairs(Flux<Person> persons) {
		Mono<List<Person>> personList = persons.collectList();
		Flux<Pair> flux = personList.map(this::getAllPairs).flatMapIterable(Function.identity());
		return flux;
	}

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
