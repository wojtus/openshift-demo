package spectum.openshiftdemo.similarity;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Component;

import spectum.openshiftdemo.person.Person;

@Component
class DefaultPairDistance implements PairDistance {

	@Override
	public Integer apply(Pair pair) {
		Integer foreNameDistance = foreNameDistance(pair);
		Integer secondNameDistance = surnameDistance(pair);
		Integer cityDistance = cityDistance(pair) / 10;
		return foreNameDistance + secondNameDistance + cityDistance;
	}

	private Integer cityDistance(Pair pair) {
		BiFunction<List<String>, List<String>, Integer> distanceFunction = this::jaccardDistance;
		Integer distance = propertyDistance(pair, Person::getCitiesOfLiving, distanceFunction,
				() -> Collections.emptyList());
		return distance;
	}

	Integer surnameDistance(Pair pair) {
		Integer distance = stringDistance(pair, Person::getSurname);
		return distance;
	}

	Integer foreNameDistance(Pair pair) {
		Integer distance = stringDistance(pair, Person::getForename);
		return distance;
	}

	<T> Integer jaccardDistance(Collection<T> citiesA, Collection<T> citiesB) {
		Set<T> intersection = new HashSet<>(citiesA);
		Set<T> citiesBSet = new HashSet<>(citiesB);
		intersection.retainAll(citiesBSet);

		Set<T> union = new HashSet<>();
		union.addAll(citiesA);
		union.addAll(citiesB);

		Integer distance = 100 * intersection.size() / union.size();
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
