package com.currencyexchange.controller;

import com.currencyexchange.bean.ExchangeValue;
import com.currencyexchange.repository.ExchangeValueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class CurrencyExchangeController {
    @Autowired
    private Environment environment;

    @Autowired
    private ExchangeValueRepository repository;

    private Logger logger= LoggerFactory.getLogger(this.getClass());
    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public ExchangeValue retrieveExchangeValue(@PathVariable String from , @PathVariable String to){
        ExchangeValue exchangeValue=new ExchangeValue(1000L,from,to,  BigDecimal.valueOf(65));

        exchangeValue=repository.findByFromAndTo(from,to);

        exchangeValue.setPort(Integer.parseInt(environment.getProperty("local.server.port")));
        logger.info("{}",exchangeValue);
        return exchangeValue;


    }
}
