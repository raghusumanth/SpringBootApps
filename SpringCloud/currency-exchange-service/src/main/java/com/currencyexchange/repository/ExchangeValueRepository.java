package com.currencyexchange.repository;

import com.currencyexchange.bean.ExchangeValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeValueRepository extends JpaRepository<ExchangeValue,Long> {

    ExchangeValue findByFromAndTo(String from,String to);
}
