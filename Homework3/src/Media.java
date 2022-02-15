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

abstract class AMedia implements IMedia {
 String title;
 ILoString captionOptions;
 
 AMedia(String title, ILoString captionOptions) {
   this.title = title;
   this.captionOptions = captionOptions;
 }
 
 /* TEMPLATE:
  * Fields:
  * ... this.title ... - String
  * ... this.captionOptions... - ILoString
  * Methods:
  * ... this.isReallyOld() ... - boolean
  * ... this.isCaptionAvailable(String) ... - boolean
  * ... this.format()... - String
  */
 
 public boolean isReallyOld() {
   return false;
 }

 public boolean isCaptionAvailable(String language) {
   return this.captionOptions.contains(language);
 }

 public abstract String format();
 
}

// represents a movie
class Movie extends AMedia {
 int year;

 Movie(String title, int year, ILoString captionOptions) {
   super(title, captionOptions);
   this.year = year;
 }
 
 /* TEMPLATE: everything in AMedia class plus...
  * Fields:
  * ... this.year ... - int
  * Methods:
  * ... this.isReallyOld() ... - boolean
  * ... this.isCaptionAvailable(String) ... - boolean
  * ... this.format()... - String
  */

 // overriding
 public boolean isReallyOld() {
   return (this.year < 1930);
 }

 public String format() {
   return this.title + " (" + this.year + ")";
 }
}

// represents a TV episode
class TVEpisode extends AMedia {
 String showName;
 int seasonNumber;
 int episodeOfSeason;

 TVEpisode(String title, String showName, int seasonNumber, int episodeOfSeason,
     ILoString captionOptions) {
   super(title, captionOptions);
   this.showName = showName;
   this.seasonNumber = seasonNumber;
   this.episodeOfSeason = episodeOfSeason;
 }
 
 /* TEMPLATE: everything in AMedia class plus...
  * Fields: 
  * ... this.showName ... - String
  * ... this.seasonNumber... - int
  * ... this.episodeOfSeason... - int
  * Methods:
  * ... this.isReallyOld() ... - boolean
  * ... this.isCaptionAvailable(String) ... - boolean
  * ... this.format()... - String
  */

 public String format() {
   return this.showName + " " 
       + this.seasonNumber + "." + this.episodeOfSeason
       + " - " + this.title;
 }
}

// represents a YouTube video
class YTVideo extends AMedia {
 String channelName;

 public YTVideo(String title, String channelName, ILoString captionOptions) {
   super(title, captionOptions);
   this.channelName = channelName;
 }
 
 /* TEMPLATE: everything in AMedia class plus...
  * Fields:
  * ... this.channelName ... - String
  * Methods:
  * ... this.isReallyOld() ... - boolean
  * ... this.isCaptionAvailable(String) ... - boolean
  * ... this.format()... - String
  */

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
 
 /* TEMPLATE
  *  Fields:
  *  ...this.first... -String
  *  ...this.rest... -ILoString
  *  Methods:
  *  ...this.contains(String)... -boolean
  */

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
 IMedia movie2 = new Movie("Goblin", 2016, new ConsLoString("Korean", new ConsLoString("English", 
     new ConsLoString("Vietnamese", new MtLoString()))));

 IMedia tv1 = new TVEpisode("Friends", "The Last One", 10, 17, new ConsLoString("English", 
     new ConsLoString("German", new MtLoString())));
 IMedia tv2 =  new TVEpisode("The Simpsons", "Who Shot Mr. Buns", 6, 25, 
     new ConsLoString("English", new MtLoString()));

 IMedia yt1 = new YTVideo("Dijkstra Algorithm", "Computer Science", new MtLoString());
 IMedia yt2 = new YTVideo("Don't be a programmer", "Joma Tech", new ConsLoString("Java", 
     new ConsLoString("Racket", new ConsLoString("C++", new MtLoString()))));
 
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
       && t.checkExpect(tv1.format(), "The Last One 10.17 - Friends")
       && t.checkExpect(tv2.format(), "Who Shot Mr. Buns 6.25 - The Simpsons")
       && t.checkExpect(yt2.format(), "Don't be a programmer by Joma Tech");
 } 
}






















