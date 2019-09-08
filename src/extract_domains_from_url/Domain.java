package extract_domains_from_url;

public class Domain {

	private Long count;
	private String name;

	public Domain(long count, String name) {
		super();
		this.count = count;
		this.name = name;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Domain [name=" + name + ", count=" + count + "]";
	}
	
	

}
