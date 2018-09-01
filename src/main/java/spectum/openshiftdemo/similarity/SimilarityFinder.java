package spectum.openshiftdemo.similarity;

import reactor.core.publisher.Flux;
import spectum.openshiftdemo.person.Person;

public interface SimilarityFinder {

	Flux<SimilarPair> getSimilarPairs(Flux<Person> persons);

}
