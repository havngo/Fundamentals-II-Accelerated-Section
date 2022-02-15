import tester.Tester;

// list of strings
interface ILoString {

  // returns a list of trigrams from this list of strings
  ILoTrigram trigrams();

  // keeps building ILoTrigram given the information of the 2 prev strings
  ILoTrigram trigramsHelper(String first, String second);

}

// an empty list of strings
class MtLoString implements ILoString { 

  /* Everything in ILoString...
   * TEMPLATE:
   * Methods:
   * ... this.trigrams()...                     - ILoTrigram
   * ... this.trigramsHelper(String, String)... -ILoTrigram
   */

  // at the end of the ilotrigram trigrams() accumulation, we would like to return empty
  // to close the list
  public ILoTrigram trigrams() {

    /* EVERYTHING from MtLoString */

    return new MtLoTrigram();
  }

  // since there aren't enough strings to build a Trigram, return an empty list of Trigram
  public ILoTrigram trigramsHelper(String first, String second) {

    /* EVERYTHING from MtLoString, plus...
    // TEMPLATE:

     * Parameters:
     * ... first ...  - String
     * ... second ...  - String
     * */

    return new MtLoTrigram();
  }

}

// a non-empty list of strings
class ConsLoString implements ILoString {

  String first;
  ILoString rest;

  // constructor
  ConsLoString(String first, ILoString rest) {
    this.first = first;
    this.rest = rest;
  }

  /* Everything in ILoString...
   * TEMPLATE
   *  Fields:
   *  ...this.first... -String
   *  ...this.rest... -ILoString
   * Methods:
   * ... this.trigrams()...                     - ILoTrigram
   * ... this.trigramsHelper(String, String)... -ILoTrigram
   */

  // returns a list of trigrams from the list of strings
  public ILoTrigram trigrams() {    
    return this.rest.trigramsHelper("", this.first);   
  }

  // keeps building the ILoTrigram given the information of two previous strings
  // ACCUMULATOR STATEMENT: we're having two string accumulators to keep 
  // track of the first two strings of the Trigram that is building. When these two
  // strings are filled, a Trigram is made with the third string from the current first;
  // then the accumulators are updated to build the next Trigram. When the list reaches 
  // empty, the method returns an MtLoTrigram() and abolishes two strings.
  public ILoTrigram trigramsHelper(String first, String second) {

    /* EVERYTHING from ConsLoString, plus...
    // TEMPLATE:
     * Parameters:
     * ... first ...  - String
     * ... second ...  - String
     */
    if (first.equals("")) {
      return this.rest.trigramsHelper(second, this.first);
    } else {
      return new ConsLoTrigram(new Trigram(first, second, this.first), 
          this.rest.trigramsHelper(second, this.first));
    }
  }

}

// to represent a trigram
class Trigram {

  String first;
  String second;
  String third;

  //constructor
  Trigram(String first, String second, String third) {
    this.first = new Utils().checkWord(first, "The first 'word' is not a word.");
    this.second = new Utils().checkWord(second, "The second 'word' is not a word.");
    this.third = new Utils().checkWord(third, "The third 'word' is not a word.");
  }

  /* TEMPLATE
   *  Fields:
   *  ...this.first...  --String
   *  ...this.second... --String
   *  ...this.third...  -- String
   *  Methods:
   *  ...this.compareTo(Trigram)... -- int
   */

  // returns negative number if this comes before (lexi & case-insensitively) 
  // the given Trigram positive number if vice versa, and 0 if they are the same.
  int compareTo(Trigram other) {

    /* EVERYTHING from Trigram, plus...
    // TEMPLATE:

     * Parameters:
     * ... other ... - Trigram
     * 
     * Methods on Parameters:
     * ... other.compareTo(Trigram) ... -- int
     * */

    if (this.first.compareToIgnoreCase(other.first) == 0) {
      if (this.second.compareToIgnoreCase(other.second) == 0) {
        return this.third.compareToIgnoreCase(other.third);
      }
      return this.second.compareToIgnoreCase(other.second);
    } else {
      return (this.first.compareToIgnoreCase(other.first));
    }
  }
}

// represents a list of trigrams
interface ILoTrigram {

  // counts how many of a trigram is in the list.
  int count(Trigram given);

  // returns the most common trigram in the list of trigram
  Trigram mostCommonTrigram();

  // helper function that compares the current trigram's commonality with the commonalities
  // of the rest of the elements in the list of trigrams
  Trigram moreCommonTrigram(Trigram curr, ILoTrigram origin);
}

//represents an empty list of trigrams
class MtLoTrigram implements ILoTrigram {

  /* Everything in ILoTrigram, plus...
   * TEMPLATE:
   * Methods:
   * ... this.count(Trigram) ... - int
   * ... this.mostCommonTrigram() ... - Trigram
   * ... this.moreCommonTrigram(Trigram, ILoTrigram)... - Trigram
   */

  // if you're trying to find the most common trigram in an empty list of trigram,
  // there will be no such trigram in the list-- thus, returns a trigram of
  // "no such trigram"
  public Trigram mostCommonTrigram() {

    /* EVERYTHING from MtLoTrigram */

    return new Trigram("NO", "SUCH", "TRIGRAM");
  }

  // a given trigram is found 0 times in a list of trigram
  public int count(Trigram given) {

    /* EVERYTHING from MtLoGram, plus...
    // TEMPLATE:

     * Parameters:
     * ... given ... - Trigram
     * 
     * Methods on Parameters:
     * ... given.compareTo(Trigram) ... -- int
     * */

    return 0;
  }

  // if the end of an mtlotrigram is reached, the more common trigram will be the
  // curr-- this spits out the ultimate most common trigram in the list

  public Trigram moreCommonTrigram(Trigram curr, ILoTrigram origin) {

    /* EVERYTHING from MtLoGram, plus...
    // TEMPLATE:

     * Parameters:
     * ... curr ... - Trigram
     * ... origin ... - ILoTrigram
     * 
     * Methods on Parameters:
     * ... curr.compareTo(Trigram) ... -- int
     * ... origin.count(Trigram) ... -- int
     * ... orign.mostCommonTrigram() ... -- Trigram
     * ... origin.moreCommonTrigram(Trigram, ILoTrigram) ... -- Trigram
     * */

    return curr;
  }
}

//represents a cons of trigrams
class ConsLoTrigram implements ILoTrigram {

  Trigram first;
  ILoTrigram rest;

  /* Everything in ConsLoTrigram, plus...
   * TEMPLATE:
   * Fields:
   * ... this.first ... - Trigram
   * ... this.rest ... - ILoTrigram
   * 
   * Methods:
   * ... this.mostCommonTrigram() ... - Trigram
   * ... this.count(Trigram) ... - int
   * ... this.moreCommonTrigram(Trigram, ILoTrigram)... - Trigram
   * 
   * Methods on Fields:
   * ... this.rest.mostCommonTrigram() ... - Trigram
   * ... this.rest.count(Trigram) ... - int
   * ... this.rest.moreCommonTrigram(Trigram, ILoTrigram)... - Trigram
   */

  // constructor
  ConsLoTrigram(Trigram first, ILoTrigram rest) {
    this.first = first;
    this.rest = rest;
  }

  // counts how many of a given trigram is in the list of trigrams.
  public int count(Trigram given) {

    /* EVERYTHING from ConsLoGram, plus...
    // TEMPLATE:

     * Parameters:
     * ... given ... - Trigram
     * 
     * Methods on Parameters:
     * ... given.compareTo(Trigram) ... -- int
     * */

    if (this.first.compareTo(given) == 0) {
      return 1 + this.rest.count(given);
    } else {
      return this.rest.count(given);
    }
  }

  // returns the most common trigram in the cons trigram by kickstarting recursion
  // with the first element of the list and the whole list itself
  public Trigram mostCommonTrigram() {

    /* EVERYTHING from ConsLoTrigram */

    return this.moreCommonTrigram(this.first, this);
  }

  // comparing the first of the list's commonality against the given Triagram

  // ACCUMULATOR STATEMENT: The accumulator here keeps track of the *temporary* most
  // common trigram. It is compared with the first Trigram in this list and is updated
  // if the first Trigram of the list is more common. It is the answer to the method 
  public Trigram moreCommonTrigram(Trigram curr, ILoTrigram origin) {

    /* EVERYTHING from ConsLoGram, plus...
    // TEMPLATE:

     * Parameters:
     * ... curr ... - Trigram
     * ... origin ... - ILoTrigram
     * 
     * Methods on Parameters:
     * ... curr.compareTo(Trigram) ... -- int
     * ... origin.count(Trigram) ... -- int
     * ... origin.mostCommonTrigram() ... -- Trigram
     * ... origin.moreCommonTrigram(Trigram, ILoTrigram) ... -- Trigram
     * */

    if (origin.count(this.first) > origin.count(curr)) {
      return this.rest.moreCommonTrigram(this.first, origin);
    }
    else if (origin.count(this.first) == origin.count(curr)) {
      if (this.first.compareTo(curr) <= 0) {
        return this.rest.moreCommonTrigram(this.first, origin);
      } else {
        return this.rest.moreCommonTrigram(curr, origin);
      }
    }
    else {
      return this.rest.moreCommonTrigram(curr, origin);
    }
  }
}

//to throw our exceptions
class Utils {

  //are the two given double values the same, and are they positive?
  //if not, throw the given message

  /* // TEMPLATE:
   * ...checkWord(String, String) -- String
   */

  String checkWord(String word, String msg) {

    /* EVERYTHING from Utils, plus...
    // TEMPLATE:

     * Parameters:
     * ... word ... - String
     * ... msg ... - String  */

    if (word.equals("")) {
      throw new IllegalArgumentException(msg);
    }
    else {
      return word;
    }
  }
}

//to test our trigrams methods against trigram examples
class ExamplesTrigram {

  // trigrams
  Trigram fruits = new Trigram("apple", "banna", "cherry");
  Trigram fruitsZebra = new Trigram("zebra", "banna", "cherry");
  Trigram names = new Trigram("ula", "ha", "accel"); // our identities revealed!
  Trigram fruitsCaps = new Trigram("APPLE", "BANNA", "CHERRY"); 
  Trigram fruitsOneCaps = new Trigram("Apple", "banna", "cherry"); 

  // docs, aka list of strings
  ILoString fruitDoc = new ConsLoString("apple", new ConsLoString("banna", 
      new ConsLoString("cherry", new MtLoString())));
  ILoString fruitDamDoc = new ConsLoString("apple", new ConsLoString("banna", 
      new ConsLoString("cherry", new ConsLoString("damm", new MtLoString()))));
  ILoString countryRoads = new ConsLoString("almost", new ConsLoString("heaven",
      new ConsLoString("west", new ConsLoString("virginia", new MtLoString()))));
  ILoString empty = new MtLoString();

  // lots, aka lists of trigrams
  ILoTrigram lot1 = new ConsLoTrigram(fruits, new ConsLoTrigram(fruitsZebra, 
      new ConsLoTrigram(names, new ConsLoTrigram(names, new MtLoTrigram()))));
  ILoTrigram lot2 = new ConsLoTrigram(fruitsZebra, new ConsLoTrigram(fruits, 
      new ConsLoTrigram(fruits, new ConsLoTrigram(fruits, new MtLoTrigram()))));
  ILoTrigram lot3 = new ConsLoTrigram(fruits, new ConsLoTrigram(fruits, 
      new ConsLoTrigram(fruitsZebra, new MtLoTrigram())));
  ILoTrigram lot4 = new ConsLoTrigram(fruits, new MtLoTrigram());
  ILoTrigram lot5 = new ConsLoTrigram(fruits, new ConsLoTrigram(fruitsZebra, 
      new ConsLoTrigram(fruitsZebra, new MtLoTrigram())));
  ILoTrigram lot6 = new ConsLoTrigram(fruits, 
      new ConsLoTrigram(fruitsZebra, new MtLoTrigram()));

  // to test the compareTo() method
  boolean testCompareTo(Tester t) {
    return t.checkExpect(fruits.compareTo(fruitsZebra) < 0, true)
        && t.checkExpect(fruitsZebra.compareTo(fruits) > 0, true)
        && t.checkExpect(fruits.compareTo(fruits) == 0, true)
        && t.checkExpect(fruits.compareTo(fruitsCaps) == 0, true)  // ignores case
        && t.checkExpect(fruits.compareTo(fruitsOneCaps) == 0, true)  // ignores case
        && t.checkExpect(fruitsZebra.compareTo(fruitsOneCaps) < 0, false); // ignores case
  }

  //to test the trigrams() method
  boolean testTrigrams(Tester t) {
    return t.checkExpect(fruitDoc.trigrams(), new ConsLoTrigram(fruits, new MtLoTrigram()))
        && t.checkExpect(countryRoads.trigrams(),
            new ConsLoTrigram(new Trigram("almost", "heaven", "west"),
                new ConsLoTrigram(new Trigram("heaven", "west", "virginia"),
                    new MtLoTrigram())))
        && t.checkExpect(fruitDamDoc.trigrams(),
            new ConsLoTrigram(fruits,
                new ConsLoTrigram(new Trigram("banna", "cherry", "damm"),
                    new MtLoTrigram())))
        && t.checkExpect(new MtLoString().trigrams(), new MtLoTrigram())
        && t.checkExpect(new ConsLoString("apple", new MtLoString()).trigrams(),
            new MtLoTrigram())
        && t.checkExpect(new ConsLoString("apple", new ConsLoString("grapes", 
            new MtLoString())).trigrams(),  new MtLoTrigram());
  }

  // to test the mostCommon() method
  boolean testMostCommon(Tester t) {
    return t.checkExpect(lot1.mostCommonTrigram(), names)
        && t.checkExpect(lot2.mostCommonTrigram(), fruits)
        && t.checkExpect(lot3.mostCommonTrigram(), fruits) // lot2, different order
        && t.checkExpect(lot4.mostCommonTrigram(), fruits)
        && t.checkExpect(lot5.mostCommonTrigram(), fruitsZebra)
        && t.checkExpect(lot6.mostCommonTrigram(), fruits) // takes the earliest one in compareto
        && t.checkExpect(new MtLoTrigram().mostCommonTrigram(), 
            new Trigram("NO","SUCH","TRIGRAM"));
  }
  
  // to test the count() method
  boolean testCount(Tester t) {
    return t.checkExpect(lot3.count(fruits), 2)
        && t.checkExpect(lot3.count(fruitsZebra), 1)
        && t.checkExpect(new ConsLoTrigram(fruits, 
            new ConsLoTrigram(fruits, new MtLoTrigram())).count(fruits), 2)
        && t.checkExpect(new ConsLoTrigram(fruits, new MtLoTrigram()).count(fruitsZebra), 0)
        && t.checkExpect(new MtLoTrigram().count(fruits), 0);
  }

  // to test constructor exceptions on trigram
  boolean testConstructorException(Tester t) {
    return t.checkConstructorException(
        new IllegalArgumentException("The first 'word' is not a word."),
        "Trigram", "", "hey", "what")
        && t.checkConstructorException(
            new IllegalArgumentException("The second 'word' is not a word."),
            "Trigram", "hi", "", "what")
        && t.checkConstructorException(
            new IllegalArgumentException("The third 'word' is not a word."),
            "Trigram", "hi", "hey", "");
  } 
}


