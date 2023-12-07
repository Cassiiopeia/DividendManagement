package com.springboot.dividendmanagement.scheduler;

import com.springboot.dividendmanagement.model.Company;
import com.springboot.dividendmanagement.model.ScrapedResult;
import com.springboot.dividendmanagement.model.constants.CacheKey;
import com.springboot.dividendmanagement.persist.CompanyRepository;
import com.springboot.dividendmanagement.persist.DividendRepository;
import com.springboot.dividendmanagement.persist.entity.CompanyEntity;
import com.springboot.dividendmanagement.persist.entity.DividendEntity;
import com.springboot.dividendmanagement.scraper.YahooFinanceScraper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class ScraperScheduler {
    private final CompanyRepository companyRepository;
    private final YahooFinanceScraper yahooFinanceScraper;
    private final DividendRepository dividendRepository;

    // 일정 주기마다 수행
    @CacheEvict(value = CacheKey.KEY_FINANCE, allEntries = true)
    @Scheduled(cron = "${scheduler.scrap.yahoo}")
    public void yahooFinanceScheduling() {
        log.info("scraping schedular is started");
        // 저장된 회사 목록을 조회
        List<CompanyEntity> companies = companyRepository.findAll();

        // 회사마다 배당금 정보를 새로 스크래핑
        for (var company : companies) {
            log.info("scraping schedular is started -> " + company.getName());
            ScrapedResult scrapedResult = yahooFinanceScraper.scrap(
                    Company.builder()
                            .name(company.getName())
                            .ticker(company.getTicker())
                            .build()
            );

            // 스크래핑한 배당금 정보 중 데이터베이스에 없는 값은 저장
            scrapedResult.getDividendEntities().stream()
                    // Dividend -> Dividend Entity
                    .map(e -> new DividendEntity(company.getId(), e))
                    // save Dividend Element if not Exists
                    .forEach(e -> {
                        boolean exists =
                                dividendRepository.existsByCompanyIdAndDate(
                                        e.getCompanyId(), e.getDate());
                        if (!exists) {
                            dividendRepository.save(e);
                        }
                    });
            // 연속적으로 스크래핑 대상 사이트서버에 요청을 날리지 않도록 일시정지
            try {
                Thread.sleep(3000); // stop 3 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }
}
