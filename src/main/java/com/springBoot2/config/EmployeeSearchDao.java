package com.springBoot2.config;

import com.springBoot2.model.Employer;
import com.springBoot2.model.Task;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
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

        Query keywordQuery = getQueryBuilderForEmployers()
                .keyword()
                .onField("name")
                .matching(text)
                .createQuery();

        List<Employer> results = getJpaQueryForEmployer(keywordQuery).getResultList();

        return results;
    }

    public List<Employer> searchEmployerNameByFuzzyQuery(String text) {
        Sort sort = getQueryBuilderForEmployers()
                .sort()
                .byField("sortName").asc() // Descending order
                .createSort();
        Query fuzzyQuery = getQueryBuilderForEmployers()
                .keyword()
                .fuzzy()
                .withEditDistanceUpTo(2)
                .withPrefixLength(0)
                .onFields("name", "surname")
                .matching(text)
                .createQuery();
        FullTextQuery fullTextQuery = getJpaQueryForEmployer(fuzzyQuery).setSort(sort);
        return ((List<Employer>) fullTextQuery.getResultList());
    }

    public List<Task> searchTasksByEmployerName(String text) {
        Query fuzzyQuery = getQueryBuilderForEmployersForTasks()
                .keyword()
                .fuzzy()
                .withEditDistanceUpTo(2)
                .withPrefixLength(0)
                .onFields("employer.name","title")
                .matching(text)
                .createQuery();

        List<Task> results = getJpaQueryForTask(fuzzyQuery).getResultList();

        return results;
    }

    public List<Employer> searchEmployerNameByWildcardQuery(String text) {

        Query wildcardQuery = getQueryBuilderForEmployers()
                .keyword()
                .wildcard()
                .onField("name")
                .matching(text)
                .createQuery();

        List<Employer> results = getJpaQueryForEmployer(wildcardQuery).getResultList();

        return results;
    }

    public List<Employer> searchEmployerDescriptionByPhraseQuery(String text) {

        Query phraseQuery = getQueryBuilderForEmployers()
                .phrase()
                .withSlop(1)
                .onField("description")
                .sentence(text)
                .createQuery();

        List<Employer> results = getJpaQueryForEmployer(phraseQuery).getResultList();

        return results;
    }

    public List<Employer> searchEmployerNameAndDescriptionBySimpleQueryStringQuery(String text) {

        Query simpleQueryStringQuery = getQueryBuilderForEmployers()
                .simpleQueryString()
                .onFields("name", "description")
                .matching(text)
                .createQuery();

        List<Employer> results = getJpaQueryForEmployer(simpleQueryStringQuery).getResultList();

        return results;
    }

    public List<Employer> searchEmployerNameByRangeQuery(int low, int high) {

        Query rangeQuery = getQueryBuilderForEmployers()
                .range()
                .onField("memory")
                .from(low)
                .to(high)
                .createQuery();

        List<Employer> results = getJpaQueryForEmployer(rangeQuery).getResultList();

        return results;
    }

    public List<Object[]> searchEmployerNameByMoreLikeThisQuery(Employer entity) {

        Query moreLikeThisQuery = getQueryBuilderForEmployers()
                .moreLikeThis()
                .comparingField("name")
                .toEntity(entity)
                .createQuery();

        List<Object[]> results = getJpaQueryForEmployer(moreLikeThisQuery).setProjection(ProjectionConstants.THIS, ProjectionConstants.SCORE)
                .getResultList();

        return results;
    }

    public List<Employer> searchEmployerNameAndDescriptionByKeywordQuery(String text) {

        Query keywordQuery = getQueryBuilderForEmployers()
                .keyword()
                .onFields("name", "surname")
                .matching(text)
                .createQuery();

        List<Employer> results = getJpaQueryForEmployer(keywordQuery).getResultList();

        return results;
    }

    public List<Object[]> searchEmployerNameAndDescriptionByMoreLikeThisQuery(Employer entity) {

        Query moreLikeThisQuery = getQueryBuilderForEmployers()
                .moreLikeThis()
                .comparingField("name")
                .toEntity(entity)
                .createQuery();

        List<Object[]> results = getJpaQueryForEmployer(moreLikeThisQuery).setProjection(ProjectionConstants.THIS, ProjectionConstants.SCORE)
                .getResultList();

        return results;
    }

    public List<Employer> searchEmployerNameAndDescriptionByCombinedQuery(String manufactorer, int memoryLow, int memoryTop, String extraFeature, String exclude) {

        Query combinedQuery = getQueryBuilderForEmployers()
                .bool()
                .must(getQueryBuilderForEmployers().keyword()
                        .onField("name")
                        .matching(manufactorer)
                        .createQuery())
                .must(getQueryBuilderForEmployers()
                        .range()
                        .onField("memory")
                        .from(memoryLow)
                        .to(memoryTop)
                        .createQuery())
                .should(getQueryBuilderForEmployers()
                        .phrase()
                        .onField("description")
                        .sentence(extraFeature)
                        .createQuery())
                .must(getQueryBuilderForEmployers()
                        .keyword()
                        .onField("name")
                        .matching(exclude)
                        .createQuery())
                .not()
                .createQuery();

        List<Employer> results = getJpaQueryForEmployer(combinedQuery).getResultList();

        return results;
    }

    private FullTextQuery getJpaQueryForEmployer(org.apache.lucene.search.Query luceneQuery) {

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

        return fullTextEntityManager.createFullTextQuery(luceneQuery, Employer.class);
    }


    private FullTextQuery getJpaQueryForTask(org.apache.lucene.search.Query luceneQuery) {

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

        return fullTextEntityManager.createFullTextQuery(luceneQuery, Task.class);
    }

    private QueryBuilder getQueryBuilderForEmployers() {

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

        return fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Employer.class)
                .get();
    }


    private QueryBuilder getQueryBuilderForEmployersForTasks() {

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

        return fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Task.class)
                .get();
    }


}