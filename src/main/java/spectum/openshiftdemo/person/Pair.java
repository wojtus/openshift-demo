package spectum.openshiftdemo.person;

class Pair {

	private final Person personA;
	private final Person personB;

	Pair(Person personA, Person personB) {
		this.personA = personA;
		this.personB = personB;
	}

	public Person getPersonA() {
		return personA;
	}

	public Person getPersonB() {
		return personB;
	}

}