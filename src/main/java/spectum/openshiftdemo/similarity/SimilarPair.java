package spectum.openshiftdemo.similarity;

import spectum.openshiftdemo.person.Person;

public class SimilarPair extends Pair {

	private final Integer similarity;

	SimilarPair(Person personA, Person personB, Integer similarity) {
		super(personA, personB);
		this.similarity = similarity;
	}

	public Integer getSimilarity() {
		return similarity;
	}

}
