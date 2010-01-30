package edu.uj.cognitive.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;

import edu.uj.cognitive.model.Keyword;
import edu.uj.cognitive.model.KeywordStatistics;

@Stateful
@Scope(ScopeType.SESSION)
@Name("keywordManager")
public class KeywordManagerBean implements KeywordManager
{
    @Logger private Log log;

    @In StatusMessages statusMessages;
    
    @PersistenceContext
	private EntityManager em;
    
    @Out(required = false, scope = ScopeType.PAGE)
	private boolean allKeywords = true;
    
    @Out(required = false, scope = ScopeType.PAGE)
    private boolean statisticKeywords;
    
   
	@DataModel
	private List<Keyword> keywordList;
	
	@DataModelSelection(value = "keywordList")
	@In(required=false)
	@Out(required=false)
	private Keyword selectedKeyword;
	
	
	@DataModel
	private List<KeywordStatistics> keywordStatisticsList;
	
	
	
	
	private String searchKeywords;
    
    public void keywordManager()
    {
        // implement your business logic here
        log.info("KeywordManager.keywordManager() action called");
        statusMessages.add("keywordManager");
    }

    // add additional action methods

    
    @SuppressWarnings("unchecked")
	@Factory("keywordList")
	public void list()
	{
    	allKeywords = true;
    	this.keywordList = this.em.createQuery("SELECT kw FROM Keyword kw ORDER BY kw.name").getResultList();
    	
    	
	}
    
    
    public boolean isAllKeywords() {
		return allKeywords;
	}

	@Override
	public void addKeyword() {
		// TODO Auto-generated method stub
		
	}

	public void setSearchKeywords(String searchKeywords) {
		this.searchKeywords = searchKeywords;
	}

	public String getSearchKeywords() {
		return searchKeywords;
	}

	@Override
	public void search() {
		String[] keywordsArray = searchKeywords.split(" ");
		
		keywordStatisticsList = new ArrayList<KeywordStatistics>();
		
		for (int i = 0; i < keywordsArray.length; i++) {
			
			List<Keyword> keywordListWithLike = this.em.createQuery("SELECT kw FROM Keyword kw WHERE kw.name LIKE :param ")
			.setParameter("param", keywordsArray[i] + "%")
			.getResultList();
			
			for (Iterator iterator = keywordListWithLike.iterator(); iterator
					.hasNext();) {
				Keyword keyword = (Keyword) iterator.next();
				
				KeywordStatistics keywordStatistics = new KeywordStatistics(); 
				keywordStatistics.setName(keyword.getName());
				
				long count = (Long)this.em.createQuery(" SELECT COUNT(p) FROM Publications p WHERE p.keywords.name = :param " )
				.setParameter("param", keyword.getName())
				.getSingleResult();
				
				keywordStatistics.setCount(count);
				
				keywordStatisticsList.add(keywordStatistics);
				
			}	
		}
		
		searchKeywords = "";
		statisticKeywords = true;
	}
    
	@Remove
	public void destroy()
	{
		this.searchKeywords = null;
		this.keywordList.clear();
		this.keywordList = null;
	}

	@Override
	public boolean isStatisticKeywords() {
		// TODO Auto-generated method stub
		return statisticKeywords;
	}
    
    
}
