package com.springboot.dividendmanagement.scraper;

import com.springboot.dividendmanagement.model.Company;
import com.springboot.dividendmanagement.model.ScrapedResult;

public class NaverFinanceScraper implements  Scraper{
    @Override
    public ScrapedResult scrap(Company company) {
        return null;
    }

    @Override
    public Company scrapCompanyByTicker(String ticker) {
        return null;
    }
}
