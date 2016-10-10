package controllers;

import java.util.Collections;

import javax.inject.Inject;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.dictionary.Dictionary;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.WordValidator;
import views.html.index;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

	@Inject
	FormFactory formFactory;
	
	@Inject
	WordValidator wordValidator;

	private String randomAlphabetic;
    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     * @throws JWNLException 
     */
    public Result index() throws JWNLException {
    	String randomWord = Dictionary.getDefaultResourceInstance().getRandomIndexWord(POS.values()[RandomUtils.nextInt(0, 3)]).getLemma().toUpperCase();
    	randomAlphabetic = randomizeWord(randomWord);
        return ok(index.render("Your new application is ready.", randomAlphabetic));
    }
    
    private String randomizeWord(String randomWord) {
    	// Only get first part if words more than 1
    	String firstPart = randomWord.split(" ")[0];
    	Logger.info("Original:" + firstPart);
    	char[] randomize = new char[firstPart.length()];
        for (int i = 0; i < firstPart.length(); i++) {
            char si = firstPart.substring(i, i + 1).toCharArray()[0];
            int loc = RandomUtils.nextInt(0, firstPart.length());
            randomize[loc] = si;
//            if(loc > randomizeStr.length() - 1){
//            	randomizeStr.append(si);
//            }else{
//            }
        }
		return String.valueOf(randomize);
	}

	/**
     * Validate a word is correct or not
     */
    public Result validateWord(){
    	DynamicForm dynamicForm = formFactory.form().bindFromRequest();
        String word = dynamicForm.get("word-candidate");
        
        if(!StringUtils.containsAny(randomAlphabetic, word.toUpperCase())){
        	return internalServerError(Json.toJson(Collections.singletonMap("result", false)));
        }
		Logger.info("Username is: " + word);
    	return ok(Json.toJson(Collections.singletonMap("result", wordValidator.validate(word))));
    }

}
