import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class MovieCollection
{
    private ArrayList<Movie> movies;
    private ArrayList<String> allCastMembers;
    private ArrayList<String> allGenres;
    private Scanner scanner;

    public MovieCollection(String fileName)
    {
        importMovieList(fileName);
        loadCastMembers();
        loadGenres();
        scanner = new Scanner(System.in);
    }

    public ArrayList<Movie> getMovies()
    {
        return movies;
    }

    public void menu()
    {
        String menuOption = "";

        System.out.println("Welcome to the movie collection!");
        System.out.println("Total: " + movies.size() + " movies");

        while (!menuOption.equals("q"))
        {
            System.out.println("------------ Main Menu ----------");
            System.out.println("- search (t)itles");
            System.out.println("- search (k)eywords");
            System.out.println("- search (c)ast");
            System.out.println("- see all movies of a (g)enre");
            System.out.println("- list top 50 (r)ated movies");
            System.out.println("- list top 50 (h)igest revenue movies");
            System.out.println("- (q)uit");
            System.out.print("Enter choice: ");
            menuOption = scanner.nextLine();

            if (!menuOption.equals("q"))
            {
                processOption(menuOption);
            }
        }
    }

    private void processOption(String option)
    {
        if (option.equals("t"))
        {
            searchTitles();
        }
        else if (option.equals("c"))
        {
            searchCast();
        }
        else if (option.equals("k"))
        {
            searchKeywords();
        }
        else if (option.equals("g"))
        {
            listGenres();
        }
        else if (option.equals("r"))
        {
            listHighestRated();
        }
        else if (option.equals("h"))
        {
            listHighestRevenue();
        }
        else
        {
            System.out.println("Invalid choice!");
        }
    }

    private void searchTitles()
    {
        System.out.print("Enter a title search term: ");
        String searchTerm = scanner.nextLine();

        // prevent case sensitivity
        searchTerm = searchTerm.toLowerCase();

        // arraylist to hold search results
        ArrayList<Movie> results = new ArrayList<Movie>();

        // search through ALL movies in collection
        for (int i = 0; i < movies.size(); i++)
        {
            String movieTitle = movies.get(i).getTitle();
            movieTitle = movieTitle.toLowerCase();

            if (movieTitle.indexOf(searchTerm) != -1)
            {
                //add the Movie objest to the results list
                results.add(movies.get(i));
            }
        }

        // sort the results by title
        sortResults(results);

        // now, display them all to the user
        for (int i = 0; i < results.size(); i++)
        {
            String title = results.get(i).getTitle();

            // this will print index 0 as choice 1 in the results list; better for user!
            int choiceNum = i + 1;

            System.out.println("" + choiceNum + ". " + title);
        }

        System.out.println("Which movie would you like to learn more about?");
        System.out.print("Enter number: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        Movie selectedMovie = results.get(choice - 1);

        displayMovieInfo(selectedMovie);

        System.out.println("\n ** Press Enter to Return to Main Menu **");
        scanner.nextLine();
    }

    private void sortResults(ArrayList<Movie> listToSort)
    {
        for (int j = 1; j < listToSort.size(); j++)
        {
            Movie temp = listToSort.get(j);
            String tempTitle = temp.getTitle();

            int possibleIndex = j;
            while (possibleIndex > 0 && tempTitle.compareTo(listToSort.get(possibleIndex - 1).getTitle()) < 0)
            {
                listToSort.set(possibleIndex, listToSort.get(possibleIndex - 1));
                possibleIndex--;
            }
            listToSort.set(possibleIndex, temp);
        }
    }

    private void displayMovieInfo(Movie movie)
    {
        System.out.println();
        System.out.println("Title: " + movie.getTitle());
        System.out.println("Tagline: " + movie.getTagline());
        System.out.println("Runtime: " + movie.getRuntime() + " minutes");
        System.out.println("Year: " + movie.getYear());
        System.out.println("Directed by: " + movie.getDirector());
        System.out.println("Cast: " + movie.getCast());
        System.out.println("Overview: " + movie.getOverview());
        System.out.println("User rating: " + movie.getUserRating());
        System.out.println("Box office revenue: " + movie.getRevenue());
    }

    private void searchCast()
    {
        System.out.print("Enter a cast member to search: ");
        String castMemberSearchTerm = scanner.nextLine().toLowerCase();

        ArrayList<String> castMembers = new ArrayList<>();

        for (String castMember : allCastMembers) {
            if (castMember.contains(castMemberSearchTerm)) {
                castMembers.add(castMember);
            }
        }

        sortStringArrayList(castMembers);

        for (int i = 0; i < castMembers.size(); i++) {
            String member = castMembers.get(i);
            int choiceNum = i + 1;

            System.out.println("" + choiceNum + ". " + member);
        }

        System.out.println("Which cast member would you like to learn more about?");
        System.out.print("Enter number: ");

        int choice = scanner.nextInt();
        scanner.nextLine();
        String castMemberToSearch = castMembers.get(choice - 1);

        ArrayList<Movie> moviesFeaturingCastMember = getMoviesFeaturingCastMember(castMemberToSearch);

        for (int i = 0; i < moviesFeaturingCastMember.size(); i++) {
            String movieTitle = moviesFeaturingCastMember.get(i).getTitle();
            int choiceNum = i + 1;

            System.out.println("" + choiceNum + ". " + movieTitle);
        }

        System.out.println("Which movie would you like to learn more about?");
        System.out.print("Enter number: ");

        choice = scanner.nextInt();
        scanner.nextLine();

        Movie selectedMovie = moviesFeaturingCastMember.get(choice - 1);
        displayMovieInfo(selectedMovie);

        System.out.println("\n ** Press Enter to Return to Main Menu **");
        scanner.nextLine();
    }

    private ArrayList<Movie> getMoviesFeaturingCastMember(String castMemberToSearch) {
        ArrayList<Movie> results = new ArrayList<>();

        for (Movie movie : movies) {
            String[] movieCastMembers = movie.getCast().split("\\|");
            String asString = Arrays.toString(movieCastMembers);

            if (asString.toLowerCase().contains(castMemberToSearch)) {
                results.add(movie);
            }
        }
        return results;
    }


    private void searchKeywords()
    {
        System.out.print("Enter a keyword to search: ");
        String keyword = scanner.nextLine();

        keyword = keyword.toLowerCase();

        ArrayList<Movie> results = new ArrayList<>();

        for (Movie movie : movies) {
            String movieKeywords = movie.getKeywords().toLowerCase();

            if (movieKeywords.contains(keyword)) {
                results.add(movie);
            }
        }

        sortResults(results);

        for (int i = 0; i < results.size(); i++) {
            String title = results.get(i).getTitle();
            int choiceNum = i + 1;

            System.out.println("" + choiceNum + ". " + title);
        }

        System.out.println("Which movie would you like to learn more about?");
        System.out.print("Enter number: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        Movie selectedMovie = results.get(choice - 1);

        displayMovieInfo(selectedMovie);

        System.out.println("\n ** Press Enter to Return to Main Menu **");
        scanner.nextLine();
    }

    private void listGenres()
    {
        sortStringArrayList(allGenres);

        for (int i = 0; i < allGenres.size(); i++) {
            String genre = allGenres.get(i);
            int choiceNum = i + 1;

            System.out.println("" + choiceNum + ". " + genre);
        }

        System.out.println("Which genre would you like to sort by?");
        System.out.print("Enter number: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        String genre = allGenres.get(choice - 1);
        
        ArrayList<Movie> filteredMoviesByGenre = filterMoviesByGenre(genre);


        for (int i = 0; i < filteredMoviesByGenre.size(); i++) {
            String movieTitle = filteredMoviesByGenre.get(i).getTitle();
            int choiceNum = i + 1;

            System.out.println("" + choiceNum + ". " + movieTitle);
        }

        System.out.println("Which movie would you like to learn more about?");
        System.out.print("Enter number: ");

        choice = scanner.nextInt();
        scanner.nextLine();

        Movie selectedMovie = filteredMoviesByGenre.get(choice - 1);
        displayMovieInfo(selectedMovie);

        System.out.println("\n ** Press Enter to Return to Main Menu **");
        scanner.nextLine();
    }

    private ArrayList<Movie> filterMoviesByGenre(String genre) {
        ArrayList<Movie> result = new ArrayList<>();
        
        for (Movie movie : movies) {
            String[] movieGenres = movie.getGenres().split("\\|");

            String asString = Arrays.toString(movieGenres);
            if (asString.toLowerCase().contains(genre)) {
                result.add(movie);
            }
        }
        return result;
    }

    private void listHighestRated()
    {
        filterMoviesByRatingDescending();
        ArrayList<Movie> top50RatedMovies = new ArrayList<>();

        for (int i = movies.size() - 1; i > movies.size() - 51; i--) {
            top50RatedMovies.add(movies.get(i));
        }

        for (int i = 0; i < top50RatedMovies.size(); i++) {
            String movieTitle = top50RatedMovies.get(i).getTitle();
            double movieRating = top50RatedMovies.get(i).getUserRating();
            int choiceNum = i + 1;

            System.out.println("" + choiceNum + ". " + movieTitle + ": " + movieRating);
        }

        System.out.println("Which movie would you like to learn more about?");
        System.out.print("Enter number: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        Movie selectedMovie = top50RatedMovies.get(choice - 1);
        displayMovieInfo(selectedMovie);

        System.out.println("\n ** Press Enter to Return to Main Menu **");
        scanner.nextLine();
    }

    private void filterMoviesByRatingDescending() {
        for (int i = 0; i < movies.size(); i++) {
            int pos = i;

            for (int j = i; j < movies.size(); j++) {
                if (movies.get(j).getUserRating() < movies.get(pos).getUserRating())
                    pos = j;
            }
            Movie min = movies.get(pos);
            movies.set(pos, movies.get(i));
            movies.set(i, min);
        }
    }

    private void listHighestRevenue()
    {

    }

    private void importMovieList(String fileName)
    {
        try
        {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();

            movies = new ArrayList<Movie>();

            while ((line = bufferedReader.readLine()) != null)
            {
                String[] movieFromCSV = line.split(",");

                String title = movieFromCSV[0];
                String cast = movieFromCSV[1];
                String director = movieFromCSV[2];
                String tagline = movieFromCSV[3];
                String keywords = movieFromCSV[4];
                String overview = movieFromCSV[5];
                int runtime = Integer.parseInt(movieFromCSV[6]);
                String genres = movieFromCSV[7];
                double userRating = Double.parseDouble(movieFromCSV[8]);
                int year = Integer.parseInt(movieFromCSV[9]);
                int revenue = Integer.parseInt(movieFromCSV[10]);

                Movie nextMovie = new Movie(title, cast, director, tagline, keywords, overview, runtime, genres, userRating, year, revenue);
                movies.add(nextMovie);
            }
            bufferedReader.close();
        }
        catch(IOException exception)
        {
            // Print out the exception that occurred
            System.out.println("Unable to access " + exception.getMessage());
        }
    }

    private void loadCastMembers() {
        allCastMembers = new ArrayList<>();

        for (Movie movie : movies) {
            String[] movieCastMembers = movie.getCast().split("\\|");

            for (String member : movieCastMembers) {
                member = member.toLowerCase();
                if (!allCastMembers.contains(member)) {
                    allCastMembers.add(member);
                }
            }

        }
    }

    private void loadGenres() {
        allGenres = new ArrayList<>();

        for (Movie movie : movies) {
            String[] movieGenres = movie.getGenres().split("\\|");

            for (String genre : movieGenres) {
                genre = genre.toLowerCase();
                if (!allGenres.contains(genre)) {
                    allGenres.add(genre);
                }
            }
        }
    }

    private void sortStringArrayList(ArrayList<String> strings) {
        for (int i = 1; i < strings.size(); i++) {
            String temp = strings.get(i);

            int possibleIdx = i;
            while (possibleIdx > 0 && temp.compareTo(strings.get(possibleIdx - 1)) < 0) {
                strings.set(possibleIdx, strings.get(possibleIdx - 1));
                possibleIdx--;
            }
            strings.set(possibleIdx, temp);
        }
    }
}