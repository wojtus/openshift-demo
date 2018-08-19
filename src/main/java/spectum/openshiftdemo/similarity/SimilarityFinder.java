package spectum.openshiftdemo.similarity;

import java.util.Collection;
import java.util.List;

import spectum.openshiftdemo.person.Person;

public interface SimilarityFinder {

	List<SimilarPair> getSimilarPairs(Collection<Person> persons);

}
