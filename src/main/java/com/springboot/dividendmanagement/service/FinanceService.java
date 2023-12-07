package com.springboot.dividendmanagement.service;

import com.springboot.dividendmanagement.model.Company;
import com.springboot.dividendmanagement.model.Dividend;
import com.springboot.dividendmanagement.model.ScrapedResult;
import com.springboot.dividendmanagement.model.constants.CacheKey;
import com.springboot.dividendmanagement.persist.CompanyRepository;
import com.springboot.dividendmanagement.persist.DividendRepository;
import com.springboot.dividendmanagement.persist.entity.CompanyEntity;
import com.springboot.dividendmanagement.persist.entity.DividendEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class FinanceService {
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    @Cacheable(key = "#companyName", value = CacheKey.KEY_FINANCE)
    public ScrapedResult getDividendByCompanyName(String companyName) {
        log.info("search company -> " + companyName);
        //1. 회사명을 기준으로 회사정보 조회
        CompanyEntity companyEntity = companyRepository.findByName(companyName)
                .orElseThrow(() -> new RuntimeException("존재하지않는 회사명입니다."));
        //2. 조회된 회사 ID로 해당금 조회
        List<DividendEntity> dividendEntities
                = dividendRepository.findAllByCompanyId(companyEntity.getId());
        //3. 결과 조합 후 반환
        List<Dividend> dividends = new ArrayList<>();
        for (var entity : dividendEntities) {
            dividends.add(Dividend.builder()
                    .date(entity.getDate())
                    .dividend(entity.getDividend())
                    .build());
        }
        return new ScrapedResult(Company.builder()
                .name(companyEntity.getTicker())
                .ticker(companyEntity.getName())
                .build(),
                dividends);
    }
}
