package software.msouti.popularmovies.Model;

public class ListMovieCP {
    String  Id;
    String Poster_path;
    String Title;
    String Release_date;
    String Vote_average;
    String Overview;


    public ListMovieCP(String id, String poster_path, String title, String release_date, String vote_average, String overview) {
        Id = id;
        Poster_path = poster_path;
        Title = title;
        Release_date = release_date;
        Vote_average = vote_average;
        Overview = overview;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getPoster_path() {
        return Poster_path;
    }

    public void setPoster_path(String poster_path) {
        Poster_path = poster_path;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getRelease_date() {
        return Release_date;
    }

    public void setRelease_date(String release_date) {
        Release_date = release_date;
    }

    public String getVote_average() {
        return Vote_average;
    }

    public void setVote_average(String vote_average) {
        Vote_average = vote_average;
    }

    public String getOverview() {
        return Overview;
    }

    public void setOverview(String overview) {
        Overview = overview;
    }



}
