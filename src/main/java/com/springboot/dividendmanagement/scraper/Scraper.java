package com.springboot.dividendmanagement.scraper;

import com.springboot.dividendmanagement.model.Company;
import com.springboot.dividendmanagement.model.ScrapedResult;

public interface Scraper {
    ScrapedResult scrap(Company company);
    Company scrapCompanyByTicker(String ticker);
}
