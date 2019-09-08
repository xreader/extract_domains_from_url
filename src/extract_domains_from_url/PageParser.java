package extract_domains_from_url;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PageParser {

	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11";
	//TODO using HTML parse library or improved regex for better results
	private static final String HREF_REGEX = "<a\\s*class=\"[^\"]*\"\\s*href=\"https?:\\/\\/([^\"]*)\"";

	private String targetUrl;

	public PageParser(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	public List<Domain> extract() throws MalformedURLException, IOException {

		String content = getPageContent();

		List<String> urls = extractUrls(content);

		// extract domain names from urls
		urls = urls.stream().map(domain -> extractDomain(domain)).collect(Collectors.toList());

		// count duplicates
		List<Entry<String, Long>> resutls = urls.stream()
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).entrySet().stream()
				.collect(Collectors.toList());

		// convert to the list of domain objects
		List<Domain> domains = resutls.stream().map(result -> new Domain(result.getValue(), result.getKey()))
				.collect(Collectors.toList());

		return domains;
	}

	private String extractDomain(String url) {
		if (url.split("/").length == 0)
			return url;
		else
			return url.split("/")[0];
	}

	/**
	 * using regex to extract urls
	 * @param content
	 * @return
	 */
	private List<String> extractUrls(String content) {

		final Pattern pattern = Pattern.compile(HREF_REGEX, Pattern.MULTILINE);
		final Matcher matcher = pattern.matcher(content);

		List<String> domains = new ArrayList<String>();

		while (matcher.find()) {
			domains.add(matcher.group(1));
		}

		return domains;
	}

	private String getPageContent() throws IOException, MalformedURLException {
		URLConnection connection = new URL(this.targetUrl).openConnection();
		connection.setRequestProperty("User-Agent", USER_AGENT);
		connection.connect();

		BufferedReader r = new BufferedReader(
				new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));

		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = r.readLine()) != null) {
			sb.append(line);
		}

		return sb.toString();
	}

}
