package com.webservice.repo;

import org.springframework.data.repository.CrudRepository;

import com.webservice.model.Summary;

public interface SummaryRepository extends CrudRepository<Summary, Long> {
    Summary findBycontextOfText(String contextOfText);
}