package com.example.anagram;

import java.io.IOException;
import java.util.Arrays;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class AnagramServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		//outputting html
		resp.setContentType("text/html");
		
		// access is needed to the google user service
		UserService us = UserServiceFactory.getUserService();
		User u = us.getCurrentUser();
		
		PersistenceManager pm = null;
		WordList wordlist;
		MemcacheService ms = MemcacheServiceFactory.getMemcacheService();
		
		String user_input = req.getParameter("search_input");
		
		if (user_input.length() > 0) {
			
			Key key = KeyFactory.createKey("WordList", getStringOrdered(user_input));
			
			String cacheKey = "WordList:" + getStringOrdered(user_input);
			
			wordlist = (WordList) ms.get(cacheKey);
			
			if ( wordlist != null) {
				// in cache so just set the feedback
				req.setAttribute("response", "Anagrams found with given input");
				req.setAttribute("list", wordlist.getWordList());				
			} else {
				
				// not in cache, add it if found
				try {
					pm = PMF.get().getPersistenceManager();
					wordlist = pm.getObjectById(WordList.class,key);
					
					req.setAttribute("response", "Anagrams found with the given input:");
					req.setAttribute("list", wordlist.getWordList());
					ms.put(cacheKey, wordlist);
					pm.close();
					
				} catch(JDOObjectNotFoundException e) {
					req.setAttribute("response", "No Anagrams were Found with the given input. Try Again!");
				}				
				
			}
			
		} else {
			req.setAttribute("response", "Invalid Anagram Search. Try Again!");
		}
		
		req.setAttribute("user", u);
		
		RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/root.jsp");
		rd.forward(req, resp);		
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		PersistenceManager pm = null;
		WordList wordList;
		String user_input = req.getParameter("add_input");
		MemcacheService ms = MemcacheServiceFactory.getMemcacheService();
		
		//access is needed to the google user service
		UserService us = UserServiceFactory.getUserService();
		User u = us.getCurrentUser();
				
		if (user_input.length() > 0) {
			
			Key key = KeyFactory.createKey("WordList", getStringOrdered(user_input));

			try {
				pm = PMF.get().getPersistenceManager();
				wordList = pm.getObjectById(WordList.class,key);
				
				if (wordList.addWord(user_input)) {
					pm.makePersistent(wordList);
					req.setAttribute("response", "Anagram: " + user_input + " was added to the list");	
					
					// updates the object by adding a new word to the list the cache can be deleted
					//from the old object list
					String cacheKey = "WordList:" + getStringOrdered(user_input);
					ms.delete(cacheKey);
				} else {
					req.setAttribute("response", "Anagram " + user_input +" is already on the list. Try again!");
				}
				

			} catch(JDOObjectNotFoundException e) {
				
				// key does not exist. Create a new word
				wordList = new WordList(key);
				wordList.addWord(user_input);
				pm.makePersistent(wordList);
				req.setAttribute("response", "New Anagram: " + user_input + " has been created");
			}
			
			pm.close();


		} else {
			req.setAttribute("response", "No Anagram was informed!!!");
		}
		
		RequestDispatcher rd = getServletContext().getRequestDispatcher("/WEB-INF/root.jsp");
		req.setAttribute("user", u);
		rd.forward(req, resp);
	}
	
	// function to order the key alphabetically
	public String getStringOrdered(String string) {
		
		char[] charArr = string.toCharArray(); 
		Arrays.sort(charArr); 
		String result = new String(charArr); 
		return result;
		
	}

}