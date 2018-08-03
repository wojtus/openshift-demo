package spectum.openshiftdemo.person;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Person {

	@Id
	private String id;
	private String forename;
	private String surname;
	private LocalDate birthDate;
	private List<String> citiesOfLiving;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getForename() {
		return forename;
	}

	public void setForename(String name) {
		forename = name;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public List<String> getCitiesOfLiving() {
		return citiesOfLiving;
	}

	public void setCitiesOfLiving(List<String> citiesOfLiving) {
		this.citiesOfLiving = citiesOfLiving;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

}
