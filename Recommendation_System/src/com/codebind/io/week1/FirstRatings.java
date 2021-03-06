package com.codebind.io.week1;

import edu.duke.*;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.csv.*;

public class FirstRatings {

    private static final String Data_Path = "";

    public List<Movie> loadMovies(String fileName) {
	List<Movie> movieList = new ArrayList<Movie>();
	FileResource fileResource = new FileResource(Data_Path + fileName);
	for (CSVRecord record : fileResource.getCSVParser()) {
	    Movie movie = new Movie(record.get("id"), record.get("title"), record.get("year"), record.get("genre"),
		    record.get("director"), record.get("country"), record.get("poster"),
		    Integer.valueOf(record.get("minutes")));
	    movieList.add(movie);
	}
	return movieList;
    }

    public List<Movie> filterByGenre(String targetGenre, List<Movie> allMovies) {
	return allMovies.stream().filter(movie -> movie.getGenres().contains(targetGenre)).collect(Collectors.toList());
    }

    public List<Movie> filterByGreaterThanMinimumLength(int minutes, List<Movie> allMovies) {
	return allMovies.stream().filter(movie -> movie.getMinutes() > minutes).collect(Collectors.toList());
    }

    public Map<String, Integer> moviesPerDirector(List<Movie> allMovies) {
	Map<String, Integer> counters = new HashMap<>();
	for (Movie movie : allMovies) {
	    String[] directorArray = movie.getDirector().split(", ");
	    for (String director : directorArray) {
		if (!counters.containsKey(director)) {
		    counters.put(director, 1);
		} else {
		    counters.put(director, counters.get(director) + 1);
		}
	    }
	}
	return counters;
    }

    public List<Rater> loadRaters(String filename) {
	final List<Rater> resultList = new ArrayList<>();
	final FileResource fileResource = new FileResource(Data_Path + filename);
	// rater_id,movie_id,rating,time
	for (CSVRecord record : fileResource.getCSVParser()) {
	    String raterID = record.get("rater_id");
	    String movieID = record.get("movie_id");
	    Double rating = Double.valueOf(record.get("rating"));

	    int raterIndex = raterIndex(resultList, raterID);
	    if (raterIndex == -1) {
		Rater currentRater = new Rater(raterID);
		currentRater.addRating(movieID, rating);
		resultList.add(currentRater);
	    } else {
		resultList.get(raterIndex).addRating(movieID, rating);
	    }
	}
	return resultList;
    }

    private int raterIndex(List<Rater> raterList, String raterID) {
	int result = -1;
	for (int i = 0; i < raterList.size(); i++) {
	    if (raterList.get(i).getID().equals(raterID)) {
		result = i;
	    }
	}
	return result;
    }

    public int getRatingsForRater(int raterID, List<Rater> raterList) {
	int result = 0;
	for (Rater rater : raterList) {
	    if (rater.getID().equals(String.valueOf(raterID))) {
		result = rater.numRatings();
	    }
	}
	return result;
    }

    public int getMaxRatings(List<Rater> raterList) {
	int maxRatings = 0;
	for (Rater rater : raterList) {
	    if (rater.numRatings() > maxRatings) {
		maxRatings = rater.numRatings();
	    }
	}
	return maxRatings;
    }

    public List<Rater> getRatersWithNumRatings(int targetRatings, List<Rater> raterList) {
	return raterList.stream().filter(rater -> rater.numRatings() == targetRatings).collect(Collectors.toList());
    }

    public int getNumRatingsForMovie(String movieID, List<Rater> raterList) {
	int ratingsCount = 0;
	for (Rater rater : raterList) {
	    if (rater.hasRating(movieID)) {
		ratingsCount++;
	    }
	}
	return ratingsCount;
    }

    public int getNumOfRatedMovies(List<Rater> raterList) {
	List<String> movieList = new ArrayList<>();
	for (Rater rater : raterList) {
	    for (String movie : rater.getItemsRated()) {
		if (!movieList.contains(movie)) {
		    movieList.add(movie);
		}
	    }
	}
	return movieList.size();
    }

}
