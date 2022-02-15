import tester.Tester;

// a piece of media
interface IMedia {

  // is this media really old?
  boolean isReallyOld();

  // are captions available in this language?
  boolean isCaptionAvailable(String language);

  // a string showing the proper display of the media
  String format();
}

// represents a movie
class Movie implements IMedia {
  String title;
  int year;
  ILoString captionOptions; // available captions

  Movie(String title, int year, ILoString captionOptions) {
    this.title = title;
    this.year = year;
    this.captionOptions = captionOptions;
  }

  public boolean isReallyOld() {
    return (this.year < 1930);
  }

  public boolean isCaptionAvailable(String language) {
    return this.captionOptions.contains(language);
  }

  public String format() {
    return this.title + " (" + this.year + ")";
  }
}

// represents a TV episode
class TVEpisode implements IMedia {
  String title;
  String showName;
  int seasonNumber;
  int episodeOfSeason;
  ILoString captionOptions; // available captions

  TVEpisode(String title, String showName, int seasonNumber, int episodeOfSeason,
      ILoString captionOptions) {
    this.title = title;
    this.showName = showName;
    this.seasonNumber = seasonNumber;
    this.episodeOfSeason = episodeOfSeason;
    this.captionOptions = captionOptions;
  }

  public boolean isReallyOld() {
    return false;
  }

  public boolean isCaptionAvailable(String language) {
    return this.captionOptions.contains(language);
  }

  public String format() {
    return this.title + " " 
        + this.seasonNumber + "." + this.episodeOfSeason
        + " - " + this.showName;
  }
}

// represents a YouTube video
class YTVideo implements IMedia {
  String title;
  String channelName;
  ILoString captionOptions; // available captions

  public YTVideo(String title, String channelName, ILoString captionOptions) {
    this.title = title;
    this.channelName = channelName;
    this.captionOptions = captionOptions;
  }

  public boolean isReallyOld() {
    return false;
  }

  public boolean isCaptionAvailable(String language) {
    return this.captionOptions.contains(language);
  }

  public String format() {
    return this.title + " by " + this.channelName;
  }

}

// lists of strings
interface ILoString {

  // does the list contain the given string?
  boolean contains(String other);
}

// an empty list of strings
class MtLoString implements ILoString {

  public boolean contains(String other) {
    return false;
  }
}

// a non-empty list of strings
class ConsLoString implements ILoString {
  String first;
  ILoString rest;

  ConsLoString(String first, ILoString rest) {
    this.first = first;
    this.rest = rest;
  }

  public boolean contains(String other) {
    return this.first.equals(other)
        || this.rest.contains(other);
  }
}

class ExamplesMedia {
  IMedia movie0 = new Movie("Chaplin", 1860, new MtLoString());
  IMedia movie1 = new Movie("The Truman Show", 1980, new ConsLoString("English", new MtLoString()));
  IMedia movie2 = new Movie("Goblin", 2016, new ConsLoString("Korean", new ConsLoString("English", new ConsLoString("Vietnamese", new MtLoString()))));

  IMedia tv1 = new TVEpisode("Friends", "The Last One", 10, 17, new ConsLoString("English", new ConsLoString("German", new MtLoString())));
  IMedia tv2 =  new TVEpisode("The Simpsons", "Who Shot Mr. Buns", 6, 25, new ConsLoString("English", new MtLoString()));

  IMedia yt1 = new YTVideo("Dijkstra’s Algorithm", "Computer Science", new MtLoString());
  IMedia yt2 = new YTVideo("Don't be a programmer", "Joma Tech", new ConsLoString("Java", new ConsLoString("Racket", new ConsLoString("C++", new MtLoString()))));
  
  boolean testIsReallyOld(Tester t) {
    return t.checkExpect(movie0.isReallyOld(), true)
        && t.checkExpect(movie2.isReallyOld(), false)
        && t.checkExpect(tv1.isReallyOld(), false)
        && t.checkExpect(yt1.isReallyOld(), false);
  } 
  
  boolean testIsCaptionAvail(Tester t) {
    return t.checkExpect(movie0.isCaptionAvailable("English"), false)
        && t.checkExpect(movie2.isCaptionAvailable("Vietnamese"), true)
        && t.checkExpect(tv1.isCaptionAvailable("German"), true)
        && t.checkExpect(yt2.isCaptionAvailable("Human Language"), false);
  } 
  
  boolean format(Tester t) {
    return t.checkExpect(movie0.format(), "Chaplin (1860)")
        && t.checkExpect(tv1.format(), "Friends 10.17 - The Last One")
        && t.checkExpect(tv2.format(), "The Simpsons 6.25 - Who Shot Mr. Buns")
        && t.checkExpect(yt2.format(), "Don't be a programmer by Joma Tech");
  } 
  

}




















