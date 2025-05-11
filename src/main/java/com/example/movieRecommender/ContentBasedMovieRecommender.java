package com.example.movieRecommender;

import java.util.*;

public class ContentBasedMovieRecommender {

    private static List<Movie> movieDatabase = new ArrayList<>();

    public static void invokeRecommender() {
        // Sample movies
        movieDatabase.add(new Movie("The Matrix", new String[]{"Action", "Sci-Fi"}));
        movieDatabase.add(new Movie("John Wick", new String[]{"Action", "Thriller"}));
        movieDatabase.add(new Movie("Inception", new String[]{"Action", "Sci-Fi", "Thriller"}));
        movieDatabase.add(new Movie("The Notebook", new String[]{"Romance", "Drama"}));
        movieDatabase.add(new Movie("Interstellar", new String[]{"Sci-Fi", "Drama"}));
        movieDatabase.add(new Movie("Titanic", new String[]{"Romance", "Drama"}));
        Scanner scanner = null;
        scanner = new Scanner(System.in);
        System.out.print("Enter a movie title you like: ");
        String inputTitle = scanner.nextLine();
        scanner.close();

        Movie inputMovie = findMovieByTitle(inputTitle);
        if (inputMovie == null) {
            System.out.println("Movie not found in database.");
            return;
        }

        List<Movie> recommendations = getRecommendations(inputMovie);
        System.out.println("Recommended movies based on genre similarity:");
        for (Movie movie : recommendations) {
            System.out.println("- " + movie.getTitle());
        }        
    }

    private static Movie findMovieByTitle(String title) {
        for (Movie movie : movieDatabase) {
            if (movie.getTitle().equalsIgnoreCase(title)) {
                return movie;
            }
        }
        return null;
    }

    private static List<Movie> getRecommendations(Movie inputMovie) {
        Map<Movie, Double> similarityScores = new HashMap<>();

        for (Movie movie : movieDatabase) {
            if (!movie.equals(inputMovie)) {
                double similarity = jaccardSimilarity(inputMovie.getGenres(), movie.getGenres());
                similarityScores.put(movie, similarity);
            }
        }

        return similarityScores.entrySet()
                .stream()
                .sorted(Map.Entry.<Movie, Double>comparingByValue().reversed())
                .limit(3) // top 3 similar movies
                .map(Map.Entry::getKey)
                .toList();
    }

    private static double jaccardSimilarity(Set<String> set1, Set<String> set2) {
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        return (double) intersection.size() / union.size();
    }
}
