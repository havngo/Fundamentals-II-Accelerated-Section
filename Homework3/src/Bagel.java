import tester.Tester;

// represents a bagel recipe
class BagelRecipe {
  double flour; // these are all weights
  double water;
  double yeast;
  double salt;
  double malt;

  // constructor
  BagelRecipe(double flour, double water, double yeast, double salt, double malt) {

    this.flour = new Utils().checkSame(flour, water,
        "The water weight is not equal to the flour weight.");
    this.water = water;
    this.yeast = new Utils().checkSame(yeast, malt,
        "The yeast weight is not equal to the malt weight.");
    this.salt = new Utils().checkSame(salt + yeast, flour / 20.0,
        "The salt and yeast combined are not 1/20th of the"
            + " flour's weight.");
    this.malt = malt;
  }

  // constructor, only given weight of flour and yeast
  BagelRecipe(double flour, double yeast) {
    this(flour, flour, yeast, ((flour / 20.0) - yeast), yeast);
  }

  // volume constructor, only given volume of flour, yeast, and salt
  BagelRecipe(double flour, double yeast, double salt) {
    this(4.25 * flour, 4.25 * flour , yeast / 48.0 * 5.0, salt / 48.0 * 10.0, yeast / 48.0 * 5.0);
  }

  /* TEMPLATE:
   * Fields:
   * ... this.flour ... -- double
   * ... this.water ... -- double
   * ... this.yeast ... -- double
   * ... this.salt ...  -- double
   * ... this.malt ...  -- double
   * Methods:
   * ... this.sameRecipe(BagelRecipe) ... - boolean
   */

  // does this bagel have the same recipe as the given bagel by 0.001?
  boolean sameRecipe(BagelRecipe other) {

    /* TEMPLATE on Parameter:
     * Fields:
     * ... this.flour ... -- double
     * ... this.water ... -- double
     * ... this.yeast ... -- double
     * ... this.salt ...  -- double
     * ... this.malt ...  -- double
     * Methods:
     * ... this.sameRecipe(BagelRecipe) ... - boolean
     * 
     */

    return 
        Math.abs(this.flour - other.flour) < 0.001
        && Math.abs(this.water - other.water) < 0.001
        && Math.abs(this.yeast - other.yeast) < 0.001
        && Math.abs(this.salt - other.salt) < 0.001
        && Math.abs(this.malt - other.malt) < 0.001;
  }
}

// to throw our exceptions
class Utilss {

  // are the two given double values the same, and are they positive?
  // if not, throw the given message
  double checkSame(double val1, double val2, String msg) {
    if (val1 <= 0) {
      throw new IllegalArgumentException("These weights are at or below 0.");
    }
    else if (Math.abs(val1 - val2) > 0.001) {
      throw new IllegalArgumentException(msg);
    }
    else {
      return val1;
    }
  }
}

class ExamplesBagelRecipe {

  BagelRecipe b1 = new BagelRecipe(40.0, 40.0, 1.0, 1.0, 1.0); 
  BagelRecipe b2 = new BagelRecipe(40.0, 1.0); // same as b1
  BagelRecipe b4 = new BagelRecipe(100.0, 100.0, 2.0, 3.0, 2.0); // different entirely

  // to test some faulty bagels
  boolean testConstructorException(Tester t) {
    return t.checkConstructorException(
        new IllegalArgumentException("The water weight is not equal to the flour weight."),
        "BagelRecipe", 3.0, 1.0, 0.1, 0.3, 0.1)
        && t.checkConstructorException(
            new IllegalArgumentException("The yeast weight is not equal to the malt weight."),
            "BagelRecipe", 4.0, 4.0, 0.2, 0.3, 0.1)
        && t.checkConstructorException(
            new IllegalArgumentException("The salt and yeast combined are not 1/20th of the"
                + " flour's weight."),
            "BagelRecipe", 40.0, 40.0, 2.0, 1000.0, 2.0)
        && t.checkConstructorException(
            new IllegalArgumentException("These weights are at or below 0."),
            "BagelRecipe", -1.2, -2.3, -3.4, -4.5, -5.6)
        && t.checkConstructorException(
            new IllegalArgumentException("These weights are at or below 0."),
            "BagelRecipe", 0.0, 0.0, 0.0);
  } 

  //to test the sameRecipe() method
  boolean testSameRecipe(Tester t) {
    return t.checkExpect(b1.sameRecipe(b1), true)
        && t.checkExpect(b1.sameRecipe(b2), true)
        && t.checkExpect(b2.sameRecipe(b1), true)
        && t.checkExpect(b4.sameRecipe(b1), false)
        && t.checkExpect(b2.sameRecipe(b4), false);
  } 
}