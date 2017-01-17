
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.jsoup.*;

public class CollectorAST implements Runnable {
	private String baseUrl = "http://assunnahtrust.com" + "/wps/%e0%a6%aa%e0%a7%8d%e0%a6%b0%e0%a6%b6%e0%a7%8d%e0%a6%a"
			+ "8%e0%a7%8b%e0%a6%a4%e0%a7%8d%e0%a6%a4%e0%a6%b0?cpage=";
	String url = "";
	private Document doc = null;
	private boolean isAvialable = true;
	private boolean isTimeOut = false;

	int page = 0;

	@Override
	public void run() {
		// DatabaseHelper databaseHelper = new DatabaseHelper("qa.db");
		// databaseHelper.createNewDatabase();
		// databaseHelper.createQATable();
		for (int i = 42; i > 41; i--) {
			page = i;
			url = baseUrl + i;
			ArrayList<QuestionAnswerModel> result = getInfo();
			// databaseHelper.insertAll(result);
		}
		// databaseHelper.prepareDatabaseForLocalImport();
	}

	private ArrayList<QuestionAnswerModel> getInfo() {
		try {
			Response response = Jsoup.connect(url).ignoreContentType(false)
					.userAgent("Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/36.0")
					.referrer("http://www.google.com").timeout(10 * 1000).followRedirects(true).execute();
			doc = response.parse();
			// File data = new File("testFile.html");
			// doc = Jsoup.parse(data, "UTF-8");
			// doc = Jsoup.parse(new URL(url).openStream(), "ISO-8859-1", url);
			doc.outputSettings(new Document.OutputSettings().prettyPrint(false));
			doc.outputSettings(new Document.OutputSettings().charset("UTF-8"));
			doc.select("br").append("ifta");
			doc.select("p").prepend("iftaifta");
		} catch (IOException e) {
			e.printStackTrace();
			isTimeOut = true;
			return null;
		}
		Elements questionAndAnswerData = doc.select("div.col-md-12");
		Elements categoryData = doc.select("table");
		ArrayList<String> category = new ArrayList<>();
		for (Element e : categoryData) {
			category.add(getCategory(e));
		}
		// for(String s : category)
		// {
		// System.out.println(s);
		// }
		questionAndAnswerData = filter(questionAndAnswerData);
		ArrayList<QuestionAnswerModel> allQuestionAnswer = parseQuestionAnswers(questionAndAnswerData, category);
		for (QuestionAnswerModel q : allQuestionAnswer) {
			System.out.println(q);
			System.out.println();
		}
		return allQuestionAnswer;

	}

	private ArrayList<QuestionAnswerModel> parseQuestionAnswers(Elements data, ArrayList<String> category) {
		int size = data.size();
		String tempQ = "not parsed", tempA;
		String qId = null;
		ArrayList<QuestionAnswerModel> result = new ArrayList<QuestionAnswerModel>(size);
		// System.out.println(data.get(1));
		int j = 0;
		for (int i = 0; i < size; i += 2) {
			try {

				String qNo = "-";
				tempQ = data.get(i).text();
				tempA = data.get(i + 1).text();

				Pattern pattern = Pattern.compile("\\(+\\d*+\\)");
				Matcher matcher = pattern.matcher(tempQ.substring(0, 50));
				if (matcher.find()) {
					qNo = matcher.group().trim().replaceAll("\\(", "").replaceAll("\\)", "");
				}

				tempQ = tempQ.replaceAll("ifta ", "\n");
				tempQ = tempQ.replaceAll("ifta", "\n");

				tempA = tempA.replaceAll("ifta ", "\n");
				tempA = tempA.replaceAll("ifta", "\n");

				if (!qNo.equals("-")) {
					tempQ = tempQ.replace("\\(+\\d*+\\)", "");
					tempA = tempA.replace("\\(+\\d*+\\)", "");
				}

				result.add(new QuestionAnswerModel(qNo, tempQ.trim(), tempA.trim(), category.get(j++).trim()));
				tempA = tempQ = qId = "not aprsed";
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Error at page " + page + " Question " + tempQ);
				j++;
				continue;
			}
		}
		return result;
	}

	private Elements filter(Elements qDiv) {
		Elements result = new Elements();
		for (Element e : qDiv) {
			if (!e.text().equals("Close")) {
				result.add(e);
			}
		}
		return result;
	}

	private void printAllText(Elements data) {
		for (Element e : data) {
			System.out.println(e.text());
		}
	}

	private void printAllRaw(Elements data) {
		for (Element e : data) {
			System.out.println(e);
		}
	}

	private void printText(Elements data, int quantity) {
		if (quantity <= data.size()) {
			for (int i = 0; i < quantity; i++) {
				System.out.println(data.get(i).text());
			}
		} else {
			printAllText(data);
		}
	}

	private void printRaw(Elements data, int quantity) {
		if (quantity <= data.size()) {
			for (int i = 0; i < quantity; i++) {
				System.out.println(data.get(i));
			}
		} else {
			printAllRaw(data);
		}
	}

	public static String getCategory(Element table) {
		Element td = table.select("tr").last().select("td").last();
		if (td == null) {
			return "No category";
		} else {
			return td.ownText();
		}
	}
}
