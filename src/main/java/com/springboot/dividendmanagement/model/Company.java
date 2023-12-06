package com.springboot.dividendmanagement.model;

import lombok.*;
import org.springframework.web.bind.annotation.RestController;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Company {
    private String ticker;
    private String name;
}
