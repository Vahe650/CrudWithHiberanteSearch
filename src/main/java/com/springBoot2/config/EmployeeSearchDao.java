package com.springBoot2.config;

import com.springBoot2.model.Employer;
import org.apache.lucene.search.Query;
import org.hibernate.search.engine.ProjectionConstants;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;


@Repository
@Transactional
public class EmployeeSearchDao {

    @PersistenceContext
    private EntityManager entityManager;


   public List<Employer> getResults(String text) {
        FullTextEntityManager fullTextEntityManager
                = Search.getFullTextEntityManager(entityManager);

       QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Employer.class)
               .get();

       // a very basic query by keywords
       org.apache.lucene.search.Query query = queryBuilder.keyword().onFields("name", "surname").matching(text)
               .createQuery();

       // wrap Lucene query in an Hibernate Query object
       org.hibernate.search.jpa.FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(query, Employer.class);

       // execute search and return results (sorted by relevance as default)
       @SuppressWarnings("unchecked")
       List<Employer> results = jpaQuery.getResultList();

       return results;
    }
}