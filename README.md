# movie-db
An app that help users in exploring the world of movies.

## Initialization
You need to get your own TMDB API Key to run this app, you can get it [here](https://www.themoviedb.org/faq/api).

Then open `app/build.gradle` and replace `${MOVIEDB_API_KEY}` with your API Key.

```
buildConfigField "String", "MOVIEDB_API_KEY", "${MOVIEDB_API_KEY}"
```

And in case you forgot.
If you experience an error while compiling, make sure to add parentheses before and after each double parentheses. Like so..

```
buildConfigField "String", "MOVIEDB_API_KEY", '"API_KEY"'
```