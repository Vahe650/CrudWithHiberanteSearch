package com.springBoot2.config;

import com.springBoot2.model.Employer;
import org.apache.lucene.search.Query;
import org.hibernate.search.engine.ProjectionConstants;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class EmployeeSearchDao {

    @PersistenceContext
    private EntityManager entityManager;


   public List<Employer> getResults(String search) {
        FullTextEntityManager fullTextEntityManager
                = Search.getFullTextEntityManager(entityManager);

        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Employer.class)
                .get();

        org.apache.lucene.search.Query query = queryBuilder
                .keyword()
                .onField("name")
                .matching(search)
                .createQuery();

        org.hibernate.search.jpa.FullTextQuery jpaQuery
                = fullTextEntityManager.createFullTextQuery(query, Employer.class);

        List<Employer> results = jpaQuery.getResultList();
        return results;
    }
}