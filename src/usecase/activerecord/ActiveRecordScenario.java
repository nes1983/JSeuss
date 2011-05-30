package usecase.activerecord;

import org.junit.Test;

public class ActiveRecordScenario {

	@Test
	public void testUsecaseScenario() {
		
		Person person = new Person();
		person.setName("Vogt");
		person.setPrename("Hans");
		person.setAge(41);
		person.save();
		
	}
}
