package controllers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.dictionary.Dictionary;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import entity.WordDefinition;
import play.Logger;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.WordService;
import views.html.index;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

	@Inject
	FormFactory formFactory;
	
	@Inject
	WordService wordService;

	private String randomAlphabetic;

	private List<List<WordDefinition>> allCorrectWords;
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
    	this.allCorrectWords = wordService.getAllCorrectWords(randomAlphabetic);
        return ok(index.render("Your new application is ready.", randomAlphabetic));
    }
    
    private String randomizeWord(String randomWord) {
    	// Only get first part if words more than 1
    	String firstPart = randomWord.split(" ")[0];
    	Logger.info("Original:" + firstPart);
    	char[] orginal = firstPart.toCharArray();
    	StringBuffer randomizeStr = new StringBuffer(StringUtils.leftPad("", firstPart.length()));
        for (int i = 0; i < orginal.length; i++) {
            char si = orginal[i];
            int loc = RandomUtils.nextInt(0, firstPart.length());
            randomizeStr.insert(loc, si);
        }
		return StringUtils.removePattern(randomizeStr.toString(), "\\s+");
	}

	/**
     * Validate a word is correct or not
     */
    public Result validateWord(){
    	DynamicForm dynamicForm = formFactory.form().bindFromRequest();
        String word = dynamicForm.get("word-candidate");
        Logger.info("The word is: " + word);
        if(!StringUtils.containsAny(randomAlphabetic, word.toUpperCase())){
        	return badRequest(Json.toJson(Collections.singletonMap("result", false)));
        }
    	boolean validate = wordService.validate(word);
    	Map<String, Object> response = new HashMap<String, Object>();
    	response.put("result", validate);
    	if(validate){
    		response.put("definitions", wordService.getDefinitions(word));
    	}
		return ok(Json.toJson(response));
    }
    
    public Result suggest(){
    	Map<String, Object> response = new HashMap<String, Object>();
    	response.put("result", true);
    	response.put("definitions", this.allCorrectWords.get(RandomUtils.nextInt(0, allCorrectWords.size())));
		return ok(Json.toJson(response));
    }

}
