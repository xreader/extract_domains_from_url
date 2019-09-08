package extract_domains_from_url;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public class Main {

	private static final String TARGET_URL = "https://wawalove.pl";

	public static void main(String[] args) throws MalformedURLException, IOException {

		List<Domain> domains = new PageParser(TARGET_URL).extract();
		
		domains.forEach(System.out::println);

	}

}
