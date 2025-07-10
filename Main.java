import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Main {
    public static void main(String[] args) throws IOException {
        String text1 = Files.readString(Paths.get("file1.txt")).toLowerCase();
        String text2 = Files.readString(Paths.get("file2.txt")).toLowerCase();

        // Step 1: Clean and tokenize text
        List<String> tokens1 = cleanAndTokenize(text1);
        List<String> tokens2 = cleanAndTokenize(text2);

        // Step 2: Build word frequency maps
        Map<String, Integer> freq1 = buildFrequencyMap(tokens1);
        Map<String, Integer> freq2 = buildFrequencyMap(tokens2);

        // Step 3: Calculate cosine similarity
        double similarity = computeCosineSimilarity(freq1, freq2);
        System.out.printf("Similarity Score: %.2f%%\n", similarity * 100);

        // Step 4: Find common words
        Set<String> common = new HashSet<>(tokens1);
        common.retainAll(tokens2);
        System.out.println("Matched Words: " + common);

        // Step 5: Export results
        String result = "Similarity: " + (similarity * 100) + "%\n" +
                        "Matched Words: " + common + "\n";
        Files.writeString(Paths.get("result.txt"), result);
        System.out.println("Result saved to result.txt");
    }

    static List<String> cleanAndTokenize(String text) {
        // Remove punctuation, split words, and filter stopwords
        String[] stopwords = { "is", "a", "the", "of", "and", "to", "in", "that", "which", "from", "includes" };
        Set<String> stopSet = new HashSet<>(Arrays.asList(stopwords));
        return Arrays.stream(text.replaceAll("[^a-z ]", "").split("\\s+"))
                     .filter(word -> !stopSet.contains(word))
                     .collect(Collectors.toList());
    }

    static Map<String, Integer> buildFrequencyMap(List<String> tokens) {
        Map<String, Integer> map = new HashMap<>();
        for (String token : tokens) {
            map.put(token, map.getOrDefault(token, 0) + 1);
        }
        return map;
    }

    static double computeCosineSimilarity(Map<String, Integer> freq1, Map<String, Integer> freq2) {
        Set<String> allWords = new HashSet<>();
        allWords.addAll(freq1.keySet());
        allWords.addAll(freq2.keySet());

        double dotProduct = 0, norm1 = 0, norm2 = 0;
        for (String word : allWords) {
            int v1 = freq1.getOrDefault(word, 0);
            int v2 = freq2.getOrDefault(word, 0);
            dotProduct += v1 * v2;
            norm1 += v1 * v1;
            norm2 += v2 * v2;
        }
        return norm1 == 0 || norm2 == 0 ? 0 : dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }
}
