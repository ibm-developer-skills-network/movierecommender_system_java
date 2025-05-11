package com.example.movieRecommender;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Scanner;

@SpringBootApplication
public class MovieRecommender {
    public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Welcome to Movie Recommender System!" 
							+ "\nPress 1 for ContentBasedFiltering"
							+ "\nPress 2 for CollaborativeFiltering"
							+ "\nPress any other key to exit"
							);
		String response = scanner.nextLine();
		if(response.equals("1")) {
			ContentBasedMovieRecommender.invokeRecommender();
		} else if (response.equals("2")) {
			CollaborativeFilteringMovieRecommender.invokeRecommender();
		} else {
			System.out.println("Bye!");
		}
	}	
}
