package io.github.zkhan93.pm.util;

/**
 * To store Some Constant values
 * Created by n193211 on 4/7/2016.
 */
public interface Constants {
    interface URL {
        String BASE = "http://api.themoviedb.org/3/movie/";
        String POP_MOVIES = BASE + "popular";
        String TOP_MOVIES = BASE + "top_rated";
        String IMG_BASE = "http://image.tmdb.org/t/p/";
    }

    interface PARAMS {
        String API_KEY = "api_key";
    }

    interface VALUES {
        String API_KEY = "2fd87bede1a929a29d3d5ada6b2bb1db";
    }

    String DATE_FORMAT = "yyyy-MM-dd";

    //possible JSON keys we will be using
    interface JSON_KEYS {
        String RESULT = "results";

        interface MOVIE {
            //JSON keys for movie object received from themoviedb api
            String POSTER_PATH = "poster_path";
            String TITLE = "title";
            String OVERVIEW = "overview";
            String RELEASE_DATE = "release_date";
            String VOTE_AVERAGE = "vote_average";
        }
    }
}
