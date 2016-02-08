package jonas.tool.httpRequestCreator.Util;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import jonas.tool.httpRequestCreator.Activities.MainActivity;
import org.jsoup.select.Elements;
import jonas.tool.httpRequestCreator.Constants.ResponseBodyCssSelectorOptions;
import org.jsoup.nodes.Element;

public class CssSelectorExtract {
	public static String deminify (String inputHtml) {
		return Jsoup.parse(inputHtml).toString();
	}
	
	//slow...
	public static int count (String inputHtml, String cssSelector) {
		return Jsoup.parse(inputHtml).select(cssSelector).size();
	}
	
	//yikes, this is getting messy...
	public static String extract(String inputHtml, String cssSelector, String elementsToReturn, String outputStyle) {
		Document document = Jsoup.parse(inputHtml, MainActivity.response.request().url().toString());
		Elements elements = new Elements(); 
		
		if (elementsToReturn.equals(ResponseBodyCssSelectorOptions.ELEMENTS_TO_RETURN[0])) {//all
			elements.addAll(document.select(cssSelector));
		}else if (elementsToReturn.equals(ResponseBodyCssSelectorOptions.ELEMENTS_TO_RETURN[1])) {//first
			elements.add(document.select(cssSelector).first());
		}else if (elementsToReturn.equals(ResponseBodyCssSelectorOptions.ELEMENTS_TO_RETURN[2])) {//last
			elements.add(document.select(cssSelector).first());
		}
		
		StringBuilder out = new StringBuilder(inputHtml.length());

		if (outputStyle.equals(ResponseBodyCssSelectorOptions.OUPUT_OPTIONS[0])) {//text only, strip html
			for (Element e: elements) {
				out.append(e.text());
				out.append(System.lineSeparator());
			}
		} else if (outputStyle.equals(ResponseBodyCssSelectorOptions.OUPUT_OPTIONS[1])) {//full html
			for (Element e: elements) {
				out.append(e.outerHtml());
				out.append(System.lineSeparator());
			}
		}
		
		return out.toString();
	}
}
