import tester.Tester;

// to represent different kinds of content
interface IContent {

  // computes the megabytes given a current value of mb.
  double mbCount(double acc);

  // returns a desciption of this IContent 
  String makeDescription();

  //
  ILoContent uniqueList(ILoContent output);

}

// ---------

//to represent text on a webpage
class Text implements IContent {
  String name;
  int numLines;
  boolean inMarkdown;

  // the constructor
  Text(String name, int numLines, boolean inMarkdown) {
    this.name = name;
    this.numLines = numLines;
    this.inMarkdown = inMarkdown;
  }
  /* TEMPLATE:
Fields:
... this.name ...            -- String
... this.numLines ...        -- int
... this.inMarkdown ...      -- boolean

Methods:
... this.mbCount(double)...            --double
... this.makeDescription(ILoString)... --ILoString
   */

  // returns the current value of the accumulator since text has no mb
  public double mbCount(double acc) { 
    return acc;
  }

  // returns an empty string because of no description
  public String makeDescription() {
    return "";
  }

  public ILoContent uniqueList(ILoContent output) {
    return output;
  }
}

//---------


//to represent a picture on a webpage
class Picture implements IContent {
  String name;
  String description;
  double megabytes;

  // the constructor
  Picture(String name, String description, double megabytes) {
    this.name = name;
    this.description = description;
    this.megabytes = megabytes;
  }
  /* TEMPLATE:
      Fields:
      ... this.name ...               -- String
      ... this.description ...        -- String
      ... this.megabytes ...          -- double

      Methods:
      ... this.mbCount(double)...            --double
      ... this.makeDescription(ILoString)... --ILoString
   */

  // adds this megabytes to the current accumulating value
  public double mbCount(double acc) {
    return this.megabytes + acc;
  }

  // describes this picture
  public String makeDescription() {
    return this.name + " (" + this.description + ")";
  }

  public ILoContent uniqueList( ILoContent output) {
    if (output.contains(this)) {
      return output;
    }
    else { 
      return new ConsLoContent(this, output);
    }
  }
}

//---------

// to represent a hyperlink on a webpage
class Hyperlink implements IContent {
  String text;
  Webpage destination;

  // the constructor
  Hyperlink(String text, Webpage destination) {
    this.text = text;
    this.destination = destination;
  }
  /* TEMPLATE:
  Fields:
  ... this.text ...                        -- String
  ... this.destination ...                 -- Webpage

   Methods:
  ... this.mbCount(double)...              -- double
  ... this.makeDescription(ILoString)...   -- ILoString

  Methods for fields:
   ... this.destination.totalMB() ...      -- Number
   ... this.destination.totalCredits() ... -- Number
   ... this.destination.pictureInfo() ...  -- String
   */

  // adds the total mb in the hyperlink's webpage to the current value/acc
  public double mbCount(double acc) {

    /* TEMPLATE: everything in the template for Hyperlink, plus
    Fields of parameters:
    ... acc ...                      -- double */

    return this.destination.totalMB() + acc;
  }

  //describes any pictures in the destination
  public String makeDescription() {
    return this.destination.pictureInfo();
  }

  public ILoContent uniqueList(ILoContent output) {
    if (this.destination.hasContentIn(output)) {
      return output;
    } else {
      return output;
    }
  }
}

//---------

// represent a list of contents
interface ILoContent {

  double totalMBHelper();

  String concatContent();

  String concatContentHelper(String prev);

  ILoContent uniqueHelper();

  boolean contains(IContent item);

  boolean overlaps(ILoContent hyperlinks);
}

//---------

// represents an empty list of content
class MtLoContent implements ILoContent {

  // the constructor
  MtLoContent() {}

  /* TEMPLATE:
  Fields:
  N/A

   Methods:
  ... this.totalMBHelper()...     --double
  ... this.toILoString()...       --ILoString
   */

  // in an empty list, there will be no MB found-- returns 0 if it is called.
  public double totalMBHelper() {

    /* TEMPLATE: everything in the template for MtLoContent */

    return 0.0; 
  }

  //returns an empty string-- base case
  public String concatContent() {
    return "";
  }

  // returns the given string since there is nothing else in the list
  public String concatContentHelper(String prev) {
    return prev;
  }

  public ILoContent uniqueHelper() {
    return new MtLoContent();
  }

  public boolean contains(IContent item) {
    return false;
  }

  public boolean overlaps(ILoContent hyperlinks) {
    return false;
  }
}

//---------

// represents a non-empty list of content
class ConsLoContent implements ILoContent {
  IContent first;
  ILoContent rest;

  // the constructor
  ConsLoContent(IContent first, ILoContent rest) {
    this.first = first;
    this.rest = rest;
  }
  /* TEMPLATE:
  Fields:
  ... this.first ...       -- IContent
  ... this.rest ...        -- ILoContent

   Methods:
  ... this.totalMBHelper()...     --double
  ... this.toILoString()...       --ILoString
   */

  // a helper function that specifically adds the number of MB in each Content found in
  // the cons. 
  // This one looks like fold.
  public double totalMBHelper() {

    /* TEMPLATE: everything in the template for ConsLoContent */

    return this.first.mbCount(this.rest.totalMBHelper());
  }

  //concats all picture info in this list of content
  public String concatContent() {
    return this.rest.concatContentHelper(this.first.makeDescription());
  }

  // concats the given string to picture info from rest of the list, 
  // separated by a comma if needed
  public String concatContentHelper(String prev) {
    if (prev.length() > 0) {
      return prev + ", " + this.rest.concatContentHelper(this.first.makeDescription());
    } else {
      return this.rest.concatContentHelper(this.first.makeDescription());
    }
  }

  public ILoContent uniqueHelper() {
    return this.first.uniqueList(this.rest.uniqueHelper());
  }

  public boolean contains(IContent item) {
    return this.first.equals(item) 
        || this.rest.contains(item);
  }

  public boolean overlaps(ILoContent contents) {
    return contents.contains(first)
        || this.rest.overlaps(contents);
  }
}

//---------

// represents a webpage with its name and content. 
class Webpage {
  String name;
  ILoContent content;

  // the constructor
  Webpage(String name, ILoContent content) {
    this.name = name;
    this.content = content;
  }
  /* TEMPLATE:
  Fields:
  ... this.name ...         -- String
  ... this.content ...      -- ILoContent

  Methods:
  ... this.totalMB() ...    -- double
  ... this.totalCredit()... -- int
  ... this.pictureInfo()... -- String
   */

  // to calculate the total MB of pictures found in a website and in the pictures of its
  // branching webpages.
  double totalMB() {

    /* TEMPLATE: everything in the template for Webpage */

    return this.content.uniqueHelper().totalMBHelper();
  }

  // to take the total MB calculated, round it off, and then multiply it by
  // the cost per MB (50 credits.)
  int totalCredits() {

    /* TEMPLATE: everything in the template for Webpage */

    return 50 * (int)(Math.ceil(this.totalMB()));
  }

  // to create a list of string of all the picture infos, then concatenate them into a string
  String pictureInfo() {

    /* TEMPLATE: everything in the template for Webpage */

    return this.content.uniqueHelper().concatContent();
  }

  // returns a unique version of this webpage
  // Webpage uniquefy() {
  //  return new Webpage(name, this.content.uniqueHelper());
  //}
  boolean hasContentIn(ILoContent hyperlinks) {
    return this.content.overlaps(hyperlinks);
  }

}

//---

//To test examples of webpages in the Webpages class.
class ExamplesWebpages {  

  Webpage assgmt1 = new Webpage("Assignment 1", 
      new ConsLoContent(new Picture("Submission", "submission screenshot", 13.7), 
          new MtLoContent()));

  Webpage assgmt = new Webpage("Assignments", 
      new ConsLoContent(new Text("Pair Programming", 10, false), 
          new ConsLoContent(new Text("Expectations", 15, false), 
              new ConsLoContent(new Hyperlink("First Assignment", assgmt1),
                  new MtLoContent()))));
  //---

  IContent fstAssignment = new Hyperlink("First Assignment", assgmt1);
  IContent javaLogo = new Picture("Java", "HD Java logo", 4);
  IContent week1 = new Text("Week 1", 10, true);
  ConsLoContent syllabusCons = new ConsLoContent(javaLogo,
      new ConsLoContent(week1,
          new ConsLoContent(fstAssignment, new MtLoContent())));

  Webpage syllabus = new Webpage("Syllabus", syllabusCons);

  //---
  IContent assgnmHL = new Hyperlink("Course Assignments", assgmt);
  IContent syllabusHL = new Hyperlink("Course Syllabus", syllabus);
  IContent codingBG = new Picture("Coding Background", "digital rain from the Matrix", 30.2);
  IContent eclipseLogo = new Picture("Eclipse", "Eclipse logo", 0.13);
  IContent instrContact = new Text("Instructor Contact", 1, false);
  IContent courseGoals = new Text("Course Goals", 5, true);
  ConsLoContent f2hpCons = new ConsLoContent(courseGoals,
      (new ConsLoContent(instrContact,
          (new ConsLoContent(eclipseLogo,
              (new ConsLoContent(codingBG,
                  (new ConsLoContent(syllabusHL, 
                      new ConsLoContent(assgnmHL, new MtLoContent()))))))))));

  Webpage homepage = new Webpage("Fundies 2 Homepage", f2hpCons);

  //---

  IContent bostonWeather = new Hyperlink("Boston Weather (?)", assgmt1);
  IContent nyWeather = new Hyperlink("New York Weather (??)", assgmt);
  IContent laWeather = new Hyperlink("Los Angeles Weather (...?)", syllabus);
  IContent dallasWeather = new Hyperlink("Dallas Weather (this ain't a weather site...)", homepage);
  IContent cloud = new Picture("Cloud", "Condensed elevated cold water", 20);
  IContent sun = new Picture("Sun", "A really really really big star", 100);
  IContent todaysWeather = new Text("Weather Warning! The weather today is...", 2, true);
  ConsLoContent weatherCons = new ConsLoContent(todaysWeather,
      (new ConsLoContent(sun,
          (new ConsLoContent(cloud,
              (new ConsLoContent(dallasWeather,
                  (new ConsLoContent(laWeather,
                      (new ConsLoContent(nyWeather,
                          (new ConsLoContent(bostonWeather, (new MtLoContent()))))))))))))));

  Webpage weather = new Webpage("The Weather (???)", weatherCons);

  boolean testPictureInfo(Tester t) {
    return t.checkExpect(homepage.pictureInfo(), 
        "Eclipse (Eclipse logo), Coding Background (digital rain from the Matrix), "
            + "Java (HD Java logo), Submission (submission screenshot), "
            + "Submission (submission screenshot)");
  } 

  // To test the totalCredits method
  boolean testTotalCredits(Tester t) {
    return t.checkExpect(assgmt.totalCredits(), 700)
        && t.checkExpect(homepage.totalCredits(), 3100)
        && t.checkExpect(weather.totalCredits(), 11350)
        && t.checkExpect(syllabus.totalCredits(), 900)
        && t.checkExpect(assgmt1.totalCredits(), 700);
  }

  // To test the totalMB method
  boolean testTotalMB(Tester t) {
    return t.checkInexact(assgmt.totalMB(), 13.7, 0.001)
        && t.checkInexact(homepage.totalMB(), 61.73, 0.001)
        && t.checkInexact(weather.totalMB(), 226.82, 0.001)
        && t.checkInexact(syllabus.totalMB(), 17.7, 0.001)
        && t.checkInexact(assgmt1.totalMB(), 13.7, 0.001);
  }

  // To test the totalMB method
  boolean testTotalMBHelper(Tester t) {
    return t.checkInexact(weatherCons.totalMBHelper(), 226.82, 0.001)
        && t.checkInexact(f2hpCons.totalMBHelper(), 61.73, 0.001)
        && t.checkInexact(syllabusCons.totalMBHelper(), 17.7, 0.001);
  }

  //To test the mbCount method
  boolean testMBCount(Tester t) {
    return t.checkInexact(bostonWeather.mbCount(0.0), 13.7, 0.001)
        // a hyperlink with acc 0
        && t.checkInexact(dallasWeather.mbCount(10.0), 71.73, 0.001)
        // a hyperlink with an accumulated number
        && t.checkInexact(cloud.mbCount(0.0), 20.0, 0.001)
        // a picture with an acc 0
        && t.checkInexact(sun.mbCount(5.5), 105.5, 0.001)
        // a picture with an accumulated number
        && t.checkInexact(todaysWeather.mbCount(0.0), 0.0, 0.001)
        // a text with an acc 0
        && t.checkInexact(week1.mbCount(1.93), 1.93, 0.001);
    // a text with an accumulated number
  }

  //to test the makeDescription method
  boolean testMakeDescription(Tester t) {
    return t.checkExpect(sun.makeDescription(), "Sun (A really really really big star)")
        && t.checkExpect(todaysWeather.makeDescription(), "")
        && t.checkExpect(bostonWeather.makeDescription(), "Submission (submission screenshot)");
  }

}