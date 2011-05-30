package usecase.activerecord;

public class Person {
	
	private String name;
	private String prename;
	private int age;
	
	public Person() {}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrename() {
		return prename;
	}

	public void setPrename(String prename) {
		this.prename = prename;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
	public String toString() {
		return this.name + " " + this.prename + " (" + this.age + ")";
	}

	public void save() {
		new Database().save(this);
	}
}
