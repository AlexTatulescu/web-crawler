package ace.ucv.web.crawler;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JSoupWebScrapperThread extends Thread {

	private String emagLink;
	private String celLink;

	public JSoupWebScrapperThread(String emagProduct, String celProduct) {
		this.emagLink = emagProduct;
		this.celLink = celProduct;
	}

	public void run() {

		try {
			parseCel();
			parseEmag();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}
	}

	public void parseCel() throws IOException {

		boolean alreadyInList = false;

		Headphone headphone = new Headphone();
		Price priceCel = new Price();

		Document doc = Jsoup.connect(this.celLink).get();

		Elements ids = doc.getElementsByAttribute("pid_prod");
		for (Element id : ids) {
			headphone.setId(id.text());
		}

		Elements prices = doc.select("span.productPrice");
		for (Element price : prices) {
			priceCel.setValue(price.text());
		}

		headphone.setTitle(this.celLink);
		priceCel.setLink(this.celLink);

		headphone.addPriceInList(priceCel);

		for (int i = 0; i < JSoupWebScrapperStarter.headphonesList.size(); i++) {
			if (JSoupWebScrapperStarter.headphonesList.get(i).getId().equals(headphone.getId())) {
				alreadyInList = true;
				JSoupWebScrapperStarter.headphonesList.get(i).addPriceInList(priceCel);
			}
		}

		if (alreadyInList == false) {
			JSoupWebScrapperStarter.headphonesList.add(headphone);
		}
	}

	public void parseEmag() throws IOException {

		boolean alreadyInList = false;

		Headphone headphone = new Headphone();
		Price priceEmag = new Price();

		Document doc = Jsoup.connect(this.emagLink).get();

		Elements ids = doc.select("input[name='product[]']");
		Elements prices = doc.select("p[class='product-new-price']");

		for (Element id : ids) {
			headphone.setId(id.text());
			;
		}

		for (Element price : prices) {
			if (price.childNodeSize() != 0)
				priceEmag.setValue((price.textNodes().get(0).toString().replaceAll("[^A-Za-z0-9]", "")));
		}

		headphone.setTitle(this.celLink);
		priceEmag.setLink(this.celLink);

		headphone.addPriceInList(priceEmag);

		for (int i = 0; i < JSoupWebScrapperStarter.headphonesList.size(); i++) {
			if (JSoupWebScrapperStarter.headphonesList.get(i).getId().equals(headphone.getId())) {
				alreadyInList = true;
				JSoupWebScrapperStarter.headphonesList.get(i).addPriceInList(priceEmag);
			}
		}

		if (alreadyInList == false) {
			JSoupWebScrapperStarter.headphonesList.add(headphone);
		}

	}

}
