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


    public List<Employer> searchEmployerNameByKeywordQuery(String text) {

        Query keywordQuery = getQueryBuilder()
                .keyword()
                .onField("name")
                .matching(text)
                .createQuery();

        List<Employer> results = getJpaQuery(keywordQuery).getResultList();

        return results;
    }

    public List<Employer> searchEmployerNameByFuzzyQuery(String text) {

        Query fuzzyQuery = getQueryBuilder()
                .keyword()
                .fuzzy()
                .withEditDistanceUpTo(2)
                .withPrefixLength(0)
                .onField("name")
                .matching(text)
                .createQuery();

        List<Employer> results = getJpaQuery(fuzzyQuery).getResultList();

        return results;
    }

    public List<Employer> searchEmployerNameByWildcardQuery(String text) {

        Query wildcardQuery = getQueryBuilder()
                .keyword()
                .wildcard()
                .onField("name")
                .matching(text)
                .createQuery();

        List<Employer> results = getJpaQuery(wildcardQuery).getResultList();

        return results;
    }

    public List<Employer> searchEmployerDescriptionByPhraseQuery(String text) {

        Query phraseQuery = getQueryBuilder()
                .phrase()
                .withSlop(1)
                .onField("description")
                .sentence(text)
                .createQuery();

        List<Employer> results = getJpaQuery(phraseQuery).getResultList();

        return results;
    }

    public List<Employer> searchEmployerNameAndDescriptionBySimpleQueryStringQuery(String text) {

        Query simpleQueryStringQuery = getQueryBuilder()
                .simpleQueryString()
                .onFields("name", "description")
                .matching(text)
                .createQuery();

        List<Employer> results = getJpaQuery(simpleQueryStringQuery).getResultList();

        return results;
    }

    public List<Employer> searchEmployerNameByRangeQuery(int low, int high) {

        Query rangeQuery = getQueryBuilder()
                .range()
                .onField("memory")
                .from(low)
                .to(high)
                .createQuery();

        List<Employer> results = getJpaQuery(rangeQuery).getResultList();

        return results;
    }

    public List<Object[]> searchEmployerNameByMoreLikeThisQuery(Employer entity) {

        Query moreLikeThisQuery = getQueryBuilder()
                .moreLikeThis()
                .comparingField("name")
                .toEntity(entity)
                .createQuery();

        List<Object[]> results = getJpaQuery(moreLikeThisQuery).setProjection(ProjectionConstants.THIS, ProjectionConstants.SCORE)
                .getResultList();

        return results;
    }

    public List<Employer> searchEmployerNameAndDescriptionByKeywordQuery(String text) {

        Query keywordQuery = getQueryBuilder()
                .keyword()
                .onFields("name", "surname")
                .matching(text)
                .createQuery();

        List<Employer> results = getJpaQuery(keywordQuery).getResultList();

        return results;
    }

    public List<Object[]> searchEmployerNameAndDescriptionByMoreLikeThisQuery(Employer entity) {

        Query moreLikeThisQuery = getQueryBuilder()
                .moreLikeThis()
                .comparingField("name")
                .toEntity(entity)
                .createQuery();

        List<Object[]> results = getJpaQuery(moreLikeThisQuery).setProjection(ProjectionConstants.THIS, ProjectionConstants.SCORE)
                .getResultList();

        return results;
    }

    public List<Employer> searchEmployerNameAndDescriptionByCombinedQuery(String manufactorer, int memoryLow, int memoryTop, String extraFeature, String exclude) {

        Query combinedQuery = getQueryBuilder()
                .bool()
                .must(getQueryBuilder().keyword()
                        .onField("name")
                        .matching(manufactorer)
                        .createQuery())
                .must(getQueryBuilder()
                        .range()
                        .onField("memory")
                        .from(memoryLow)
                        .to(memoryTop)
                        .createQuery())
                .should(getQueryBuilder()
                        .phrase()
                        .onField("description")
                        .sentence(extraFeature)
                        .createQuery())
                .must(getQueryBuilder()
                        .keyword()
                        .onField("name")
                        .matching(exclude)
                        .createQuery())
                .not()
                .createQuery();

        List<Employer> results = getJpaQuery(combinedQuery).getResultList();

        return results;
    }

    private FullTextQuery getJpaQuery(org.apache.lucene.search.Query luceneQuery) {

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

        return fullTextEntityManager.createFullTextQuery(luceneQuery, Employer.class);
    }

    private QueryBuilder getQueryBuilder() {

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

        return fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Employer.class)
                .get();
    }

}