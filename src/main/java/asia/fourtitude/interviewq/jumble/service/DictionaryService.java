package asia.fourtitude.interviewq.jumble.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DictionaryService {

    @Autowired
    private ResourceLoader resourceLoader;

    private Map<String,Integer> wordsMap;
    private Set<String> wordSets;
    private Set<Character> invalidCharacters;
    private List<String> wordList;

    public DictionaryService() throws IOException
    {
        this.resourceLoader = new DefaultResourceLoader();

        wordsMap = new HashMap<>();
        wordSets = new HashSet<>();
        wordList = new ArrayList<>();
        invalidCharacters = new HashSet<>();
        
        if (resourceLoader == null) {
            throw new IllegalStateException("ResourceLoader is not properly initialized.");
        }
        
        Resource resource = resourceLoader.getResource("classpath:/words.txt");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String tempWord = line.trim();
                wordsMap.put(tempWord,tempWord.length());
                wordSets.add(tempWord);
                wordList.add(tempWord);
            }
        }

        String invalidChars = ",./<>?;':\"[]{}\\|1234567890!@#$%^&*()`~";
        for(char c:invalidChars.toCharArray())
        {
            invalidCharacters.add(c);
        }
    }

    public boolean isValidWord(String word){
        return this.wordSets.contains(word);
    }
}
