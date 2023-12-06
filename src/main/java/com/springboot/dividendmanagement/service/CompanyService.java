package com.springboot.dividendmanagement.service;

import com.springboot.dividendmanagement.model.Company;
import com.springboot.dividendmanagement.model.ScrapedResult;
import com.springboot.dividendmanagement.persist.CompanyRepository;
import com.springboot.dividendmanagement.persist.DividendRepository;
import com.springboot.dividendmanagement.persist.entity.CompanyEntity;
import com.springboot.dividendmanagement.persist.entity.DividendEntity;
import com.springboot.dividendmanagement.scraper.Scraper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompanyService {
    private final Scraper yahooFinanceScraper;
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;
    public Company save(String ticker){
        boolean exists = companyRepository.existsByTicker(ticker);
        if(exists){
            throw new RuntimeException("already exists ticker -> "+ ticker);
        }
        return storeCompanyAndDividend(ticker);
    }

    private Company storeCompanyAndDividend(String ticker){
        // ticker 를 기준으로 회사를 스크래핑
        Company company = yahooFinanceScraper.scrapCompanyByTicker(ticker);
        if (ObjectUtils.isEmpty(company)){
            throw new RuntimeException("failed to scarp ticker -> "+ ticker);
        }

        // 해당 회사가 존재할 경우, 회사의 배당금 정보를 스크래핑
        ScrapedResult scrapedResult = yahooFinanceScraper.scrap(company);

        // 스크래핑 결과
        CompanyEntity companyEntity
                = companyRepository.save(new CompanyEntity(company));

        List<DividendEntity> dividendEntities = scrapedResult.getDividendEntities().stream()
                .map(e -> new DividendEntity(companyEntity.getId(), e))
                .collect(Collectors.toList());

        dividendRepository.saveAll(dividendEntities);
        return company;
    }

}