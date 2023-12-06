package com.springboot.dividendmanagement.scraper;

import com.springboot.dividendmanagement.model.Company;
import com.springboot.dividendmanagement.model.Dividend;
import com.springboot.dividendmanagement.model.ScrapedResult;
import com.springboot.dividendmanagement.model.constants.Month;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class YahooFinanceScraper implements Scraper{

    private static final String STATISTICS_URL = "https://finance.yahoo.com/quote/" +
            "%s/history" + // ticker
            "?period1=%d" + // START_TIME
            "&period2=%d" + // now
            "&interval=1mo&filter=history" +
            "&frequency=1mo&includeAdjustedClose=true";
    private static final long START_TIME = 86400; // 60 * 60 * 24
    private static final String SUMMARY_URL = "https://finance.yahoo.com/quote/" +
            "%s" + // ticker
            "?p=%s"; // ticker

    @Override
    public ScrapedResult scrap(Company company) {
        ScrapedResult scarpResult = new ScrapedResult();
        scarpResult.setCompany(company);

        try {
            long now = System.currentTimeMillis() / 1000;
            String url = String.format(
                    STATISTICS_URL, company.getTicker(), START_TIME, now);
            Connection connection = Jsoup.connect(url);
            Document document = connection.get();

            Elements parsingDivs
                    = document.getElementsByAttributeValue(
                    "data-test", "historical-prices");
            Element tableEle = parsingDivs.get(0); // table 전체

            Element tbody = tableEle.children().get(1);

            List<Dividend> dividends = new ArrayList<>();

            for (Element e : tbody.children()) {
                String txt = e.text();
                if (!txt.endsWith("Dividend")) {
                    continue;
                }

                String[] splits = txt.split(" ");
                int month = Month.strToNumber(splits[0]);
                int day = Integer.parseInt(splits[1].replace(",", ""));
                int year = Integer.parseInt(splits[2]);
                String dividend = splits[3];

                if (month < 0) {
                    throw new RuntimeException("Unexpected Moth enum value -> "
                            + splits[0]);
                }

                dividends.add(Dividend.builder()
                        .date(LocalDateTime.of(year, month, day, 0, 0))
                        .dividend(dividend)
                        .build());


//                System.out.println(year + "/" + month + "/" + day + "/"
//                        + "->" + dividend);
            }
            scarpResult.setDividendEntities(dividends);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return scarpResult;
    }

    @Override
    public Company scrapCompanyByTicker(String ticker){
        String url = String.format(SUMMARY_URL, ticker, ticker);

        try {
            Document document = Jsoup.connect(url).get();
            Element titleEle = document.getElementsByTag("h1").get(0);
            String title = titleEle.text();
            title = title.replaceAll("\\s*\\([^\\)]*\\)\\s*", "").trim();
            return Company.builder()
                    .ticker(ticker)
                    .name(title)
                    .build();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
