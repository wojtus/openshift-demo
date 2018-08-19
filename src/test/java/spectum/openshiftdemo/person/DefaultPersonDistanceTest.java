package spectum.openshiftdemo.person;

import static org.junit.Assert.assertEquals;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.junit.Test;

public class DefaultPersonDistanceTest {

	private DefaultPersonDistance defaultPersonDistance = new DefaultPersonDistance();

	@Test
	public void testApply() throws Exception {
		Person personA = new Person();
		personA.setForename("Max");
		personA.setSurname("Kowalski");
		Person personB = new Person();
		personB.setForename("Maxi");
		personB.setSurname("Kowal");
		Pair pair = new Pair(personA, personB);

		Integer distance = defaultPersonDistance.apply(pair);
		assertEquals(4, distance.intValue());
	}

	@Test
	public void testForeNameDistance() throws Exception {
		Person personA = new Person();
		Person personB = new Person();
		Pair pair = createTestPair(personA, personB, //
				() -> "Max", () -> "Max", //
				personA::setForename, personB::setForename);
		Integer distance = defaultPersonDistance.foreNameDistance(pair);
		assertEquals(0, distance.intValue());
	}

	@Test
	public void testPropertyDistance() throws Exception {
		Person personA = new Person();
		personA.setId("0");
		Person personB = new Person();
		personB.setId("1");

		Pair pair = new Pair(personA, personB);
		BiFunction<String, String, Integer> distanceFunction = this::getIdDistance;
		Supplier<String> emptySupplier = () -> String.valueOf(3);
		assertEquals(1, defaultPersonDistance.propertyDistance(pair, Person::getId, distanceFunction, emptySupplier)
				.intValue());

		personB.setId(null);
		assertEquals(3, defaultPersonDistance.propertyDistance(pair, Person::getId, distanceFunction, emptySupplier)
				.intValue());
	}

	@Test
	public void testSurnameDistance() throws Exception {
		Person personA = new Person();
		Person personB = new Person();
		Pair pair = createTestPair(personA, personB, //
				() -> "Max", () -> "Maxi", //
				personA::setSurname, personB::setSurname);
		Integer distance = defaultPersonDistance.surnameDistance(pair);
		assertEquals(1, distance.intValue());
	}

	private Integer getIdDistance(String a, String b) {
		return Math.abs(Integer.valueOf(a) - Integer.valueOf(b));
	}

	private <T> Pair createTestPair(Person personA, Person personB, Supplier<T> supplierA, Supplier<T> supplierB,
			Consumer<T> consumerA, Consumer<T> consumerB) {
		T valueA = supplierA.get();
		consumerA.accept(valueA);
		T valueB = supplierB.get();
		consumerB.accept(valueB);
		Pair pair = new Pair(personA, personB);
		return pair;
	}

}
