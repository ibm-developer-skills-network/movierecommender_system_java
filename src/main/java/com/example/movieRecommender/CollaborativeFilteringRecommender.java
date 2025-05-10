package com.example.movieRecommender;

import java.util.*;

public class CollaborativeFilteringRecommender {

    // User -> (Movie -> Rating)
    private static final Map<String, Map<String, Double>> userRatings = new HashMap<>();

    public static void main(String[] args) {
        initializeRatings();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your name (e.g., Alice, Bob, Charlie, Dave, Eve): ");
        String targetUser = scanner.nextLine();
        scanner.close();
        if (!userRatings.containsKey(targetUser)) {
            System.out.println("User not found in the dataset.");
            return;
        }

        List<String> recommendations = getRecommendations(targetUser);

        System.out.println("\nMovie recommendations for " + targetUser + ":");
        if (recommendations.isEmpty()) {
            System.out.println("No new recommendations available.");
        } else {
            for (String movie : recommendations) {
                System.out.println("- " + movie);
            }
        }
    }

    private static void initializeRatings() {
        userRatings.put("Alice", Map.of(
            "Inception", 5.0, "Titanic", 3.0, "Avatar", 4.5, "Joker", 4.0, "Interstellar", 4.5,
            "Frozen", 2.5, "Iron Man", 5.0, "Up", 3.0, "Coco", 3.5, "Avengers", 4.5
        ));

        userRatings.put("Bob", Map.of(
            "Inception", 4.0, "Titanic", 2.0, "Avatar", 4.0, "Interstellar", 5.0, "Coco", 4.5,
            "Frozen", 3.0, "Avengers", 5.0, "Black Panther", 4.0, "Joker", 4.5, "Toy Story", 3.0
        ));

        userRatings.put("Charlie", Map.of(
            "Titanic", 5.0, "Joker", 5.0, "Interstellar", 4.0, "Coco", 3.5, "Up", 4.0,
            "Frozen", 3.5, "Toy Story", 4.5, "Lion King", 5.0, "Shrek", 4.0, "Minions", 3.0
        ));

        userRatings.put("Dave", Map.of(
            "Iron Man", 4.5, "Avengers", 4.0, "Black Panther", 5.0, "Inception", 3.5,
            "Joker", 4.0, "Shrek", 4.0, "Minions", 3.5, "Frozen", 2.0, "Lion King", 4.5, "Toy Story", 4.0
        ));

        userRatings.put("Eve", Map.of(
            "Coco", 5.0, "Up", 5.0, "Toy Story", 5.0, "Frozen", 4.0, "Minions", 4.5,
            "Shrek", 4.5, "Lion King", 5.0, "Joker", 3.0, "Titanic", 4.0, "Iron Man", 4.0
        ));
    }

    private static List<String> getRecommendations(String targetUser) {
        Map<String, Double> scores = new HashMap<>();
        Map<String, Double> totalSim = new HashMap<>();

        for (String otherUser : userRatings.keySet()) {
            if (otherUser.equals(targetUser)) continue;

            double similarity = pearsonSimilarity(userRatings.get(targetUser), userRatings.get(otherUser));
            if (similarity <= 0) continue;

            for (Map.Entry<String, Double> entry : userRatings.get(otherUser).entrySet()) {
                String movie = entry.getKey();

                // Ignore movies already rated by the target user
                if (userRatings.get(targetUser).containsKey(movie)) continue;

                scores.put(movie, scores.getOrDefault(movie, 0.0) + entry.getValue() * similarity);
                totalSim.put(movie, totalSim.getOrDefault(movie, 0.0) + similarity);
            }
        }

        // Normalize scores
        Map<String, Double> rankings = new HashMap<>();
        for (String movie : scores.keySet()) {
            rankings.put(movie, scores.get(movie) / totalSim.get(movie));
        }

        // Sort by highest predicted rating
        return rankings.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .limit(5) // Limit top 5 recommendations
                .toList();
    }

    private static double pearsonSimilarity(Map<String, Double> ratings1, Map<String, Double> ratings2) {
        Set<String> common = new HashSet<>(ratings1.keySet());
        common.retainAll(ratings2.keySet());

        int n = common.size();
        if (n == 0) return 0;

        double sum1 = 0, sum2 = 0;
        double sum1Sq = 0, sum2Sq = 0;
        double pSum = 0;

        for (String movie : common) {
            double r1 = ratings1.get(movie);
            double r2 = ratings2.get(movie);

            sum1 += r1;
            sum2 += r2;
            sum1Sq += r1 * r1;
            sum2Sq += r2 * r2;
            pSum += r1 * r2;
        }

        double numerator = pSum - (sum1 * sum2 / n);
        double denominator = Math.sqrt((sum1Sq - (sum1 * sum1) / n) * (sum2Sq - (sum2 * sum2) / n));

        return denominator == 0 ? 0 : numerator / denominator;
    }
}
