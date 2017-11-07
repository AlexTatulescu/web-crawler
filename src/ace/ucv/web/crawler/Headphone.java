package ace.ucv.web.crawler;

import java.util.List;

public class Headphone {

	private String title;
	private String id;
	private List<Price> priceList;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Price> getPriceList() {
		return priceList;
	}

	public void setPriceList(List<Price> priceList) {
		this.priceList = priceList;
	}

	public void addPriceInList(Price price) {
		this.priceList.add(price);
	}
}
