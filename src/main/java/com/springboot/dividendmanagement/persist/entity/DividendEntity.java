package com.springboot.dividendmanagement.persist.entity;

import com.springboot.dividendmanagement.model.Dividend;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity(name = "DIVIDENED")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class DividendEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long companyId;

    private LocalDateTime date;

    private String dividend;


    public DividendEntity(Long companyId, Dividend dividend) {
        this.companyId =  companyId;
        this.date = dividend.getDate();
        this.dividend = dividend.getDividend();
    }
}