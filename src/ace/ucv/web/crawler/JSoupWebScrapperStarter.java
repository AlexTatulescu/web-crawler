package ace.ucv.web.crawler;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JSoupWebScrapperStarter {

	private final static int THREADS = 114;
	public volatile static List<Headphone> headphonesList = new ArrayList<Headphone>();

	public static void main(String[] args) throws IOException {

		Thread[] threads = new Thread[THREADS];
		String emagUrl = "http://www.cel.ro/casti/";
		String celUrl = "https://www.emag.ro/casti-pc/c";
		List<String> celLinksList = paginationCel(celUrl);
		List<String> emagLinksList = paginationEmag(emagUrl);

		for (int i = 0; i < THREADS; i++) {
			threads[i] = new JSoupWebScrapperThread(emagLinksList.get(i), celLinksList.get(i));
		}

		for (int i = 0; i < THREADS; i++) {
			threads[i].start();
		}

		for (int i = 0; i < THREADS; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	private static List<String> paginationCel(String celLink) throws IOException {

		List<String> headPhonesLinks = new ArrayList<String>();

		Document paginationPage = Jsoup.connect(celLink).get();

		Elements pagination = paginationPage.select(".pageresults > a");
		for (Element e : pagination) {
			String url = e.attr("abs:href");
			Document doc = Jsoup.connect(url).get();
			Elements linksAndUrls = doc.select("a[class=\"productListing-data-b product_link product_name\"]");
			for (Element link : linksAndUrls) {
				headPhonesLinks.add(link.attr("abs:href"));
			}
		}

		return headPhonesLinks;
	}

	private static List<String> paginationEmag(String emagLink) throws IOException {

		List<String> headPhonesLinks = new ArrayList<String>();

		Document paginationPage = Jsoup.connect(emagLink).get();

		Elements pagination = paginationPage.select(".pagination > li > a");
		for (Element e : pagination) {
			String url = e.attr("abs:href");
			Document doc = Jsoup.connect(url).get();
			Elements linksAndUrls = doc.select("a[class='product-title js-product-url']");
			for (Element link : linksAndUrls) {
				headPhonesLinks.add(link.attr("abs:href"));
			}
		}

		return headPhonesLinks;
	}

}
