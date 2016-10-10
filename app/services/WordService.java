package services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Singleton;

import entity.WordDefinition;
import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.data.Synset;
import net.sf.extjwnl.dictionary.Dictionary;

@Singleton
public class WordService {

	private Dictionary dictionary;

	public WordService() {
		try {
			loadDictionary();
		} catch (JWNLException e) {
			e.printStackTrace();
		}
	}

	private void loadDictionary() throws JWNLException {
		dictionary = Dictionary.getDefaultResourceInstance();
	}

	public boolean validate(String word) {
		try {
			for (POS pos : POS.values()) {
				IndexWord indexWord = dictionary.getIndexWord(pos, word);
				if (Objects.nonNull(indexWord)) {
					return true;
				}
			}
		} catch (JWNLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public List<WordDefinition> getDefinitions(String word) {
		List<WordDefinition> result = new ArrayList<WordDefinition>();
		try {
			for (POS pos : POS.values()) {
				IndexWord indexWord = dictionary.getIndexWord(pos, word);
				if (Objects.nonNull(indexWord)) {
					List<Synset> synsetOffsets = indexWord.getSenses();
					for (Synset synset : synsetOffsets) {
						WordDefinition definition = new WordDefinition();
						definition.setValue(word);
						definition.setType(pos.name());
						definition.setGloss(synset.getGloss());
						result.add(definition);
					}
				}
			}
		} catch (JWNLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public List<List<WordDefinition>> getAllCorrectWords(String word) {
		List<List<WordDefinition>> result = new ArrayList<List<WordDefinition>>();
		List<String> allPossibleStr = permutation(word);
		for (String possibleWord : allPossibleStr) {
			if (validate(possibleWord)) {
				result.add(getDefinitions(possibleWord));
			}
		}

		return result;
	}

	public static void main(String[] args) throws JWNLException {
		WordService wordService = new WordService();
		wordService.permutation("dog");
	}

	private List<String> permutation(String str) {
		List<String> result = new ArrayList<String>();
		permutation("", str, result);
		return result;
	}

	private void permutation(String prefix, String str, List<String> result) {
		if (str.isEmpty()) {
			System.out.println(prefix);
			result.add(prefix);

		} else {
			// Generate pemutation for prefix
			for (int i = 0; i < str.length(); i++) {
				permutation(prefix + str.charAt(i),
						str.substring(i + 1, str.length()), result);
			}
			permutation(prefix, "", result);
		}
		
		

	}
}
