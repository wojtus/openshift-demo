package spectum.openshiftdemo.similarity;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.text.similarity.LevenshteinDistance;

import spectum.openshiftdemo.person.Person;

class DefaultPairDistance implements PairDistance {

	@Override
	public Integer apply(Pair pair) {
		Integer foreNameDistance = foreNameDistance(pair);
		Integer secondNameDistance = surnameDistance(pair);
		return foreNameDistance + secondNameDistance;
	}

	Integer surnameDistance(Pair pair) {
		Integer distance = stringDistance(pair, Person::getSurname);
		return distance;
	}

	Integer foreNameDistance(Pair pair) {
		Integer distance = stringDistance(pair, Person::getForename);
		return distance;
	}

	private Integer stringDistance(Pair pair, Function<Person, CharSequence> property) {
		BiFunction<CharSequence, CharSequence, Integer> distanceFunction = new LevenshteinDistance()::apply;
		Integer distance = propertyDistance(pair, property, distanceFunction, () -> "");
		return distance;
	}

	<T> Integer propertyDistance(Pair pair, Function<Person, T> property, BiFunction<T, T, Integer> distanceFunction,
			Supplier<T> emptySupplier) {
		return Optional.ofNullable(pair).map(p -> {
			Optional<Person> personAOptional = Optional.ofNullable(p.getPersonA());
			Optional<Person> personBOptional = Optional.ofNullable(p.getPersonB());
			T propertyA = personAOptional.map(property).orElseGet(emptySupplier);
			T propertyB = personBOptional.map(property).orElseGet(emptySupplier);
			Integer distance = distanceFunction.apply(propertyA, propertyB);
			return distance;
		}).orElseThrow(() -> new IllegalArgumentException("No distance for empty pair"));
	}

}
