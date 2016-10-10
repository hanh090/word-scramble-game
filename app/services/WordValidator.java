package services;

import java.util.Objects;

import javax.inject.Singleton;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.dictionary.Dictionary;

@Singleton
public class WordValidator {

	private Dictionary dictionary;
	public WordValidator() {
		try {
			loadDictionary();
		} catch (JWNLException e) {
			e.printStackTrace();
		}
	}
	private void loadDictionary() throws JWNLException {
		dictionary = Dictionary.getDefaultResourceInstance();
	}
	public boolean validate(String word){
		try {
			for (POS pos : POS.values()) {
				IndexWord indexWord = dictionary.getIndexWord(pos, word);
				if(Objects.nonNull(indexWord)){
					return true;
				}
			}
		} catch (JWNLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void main(String[] args) throws JWNLException {
		Dictionary defaultResourceInstance = Dictionary.getDefaultResourceInstance();
		IndexWord indexWord = defaultResourceInstance.getIndexWord(POS.NOUN, "dog");
		System.out.println(indexWord.getKey());
	}
}
