import tester.Tester;

//-----------------------------------------------------------------

// an expression of the form a(i)x^(i)
class Monomial {
  int coefficient;
  int degree;

  // constructor
  Monomial(int coefficient, int degree) {
    if (degree < 0) {
      throw new IllegalArgumentException("Degress must be non-negative");
    }

    this.coefficient = coefficient;
    this.degree = degree;
  }

  /* TEMPLATE   
   * 
   */

  // substitutes x with the given value and evaluates it
  int substitute(int x) {
    return ((int) (Math.pow(x, this.degree))) * this.coefficient;
  }

  // returns neg number if this monomial comes before the given one
  // positive number if it comes after and 0 if they are the same degree
  int compareTo(Monomial other) {
    return this.degree - other.degree;
  }

  // is this monomial zero-coefficient?
  boolean isZeroCoef() {
    return this.coefficient == 0;
  }

  // integrates two monomials when sharing the same degree
  Monomial integrate(Monomial other) {
    return new Monomial(this.coefficient + other.coefficient, this.degree);
  }
  
  // multiplies this monomial with a given one
  Monomial mul(Monomial other) {
    return new Monomial(this.coefficient * other.coefficient, this.degree + other.degree);
  }
}

// a list of monomials
interface ILoMonomial {

  // evaluates the list with a given value
  int evaluateHelp(int x);
  // normalizes the list
  ILoMonomial normalize();
  
  // helper functions to assist normalize() method
  ILoMonomial sort();
  ILoMonomial insert(Monomial given);
  ILoMonomial filter0coef();
  
  // main and helper functions to add 2 lists of monomial together
  ILoMonomial adds(ILoMonomial other);
  ILoMonomial addHelp(Monomial that);
  
  // main and helper functions to multiply 2 lists of monomial together
  ILoMonomial multiplies(ILoMonomial other);
  ILoMonomial multiplyHelp(Monomial that);
  
  
}

// an empty list of monomials
class MtLoMonomial implements ILoMonomial {

  // nothing to evaluate
  public int evaluateHelp(int x) {
    return 0;
  }

  // nothing to normalize
  public ILoMonomial normalize() {
    return this;
  }

  // nothing to sort
  public ILoMonomial sort() {
    return this;
  }

  // inserts the given monomial
  public ILoMonomial insert(Monomial given) {
    return new ConsLoMonomial(given, this);
  }

  // nothing to filter out
  public ILoMonomial filter0coef() {
    return this;
  }

  // returns the given list
  public ILoMonomial adds(ILoMonomial other) {
    return other;
  }

  //returns the given list
  public ILoMonomial addHelp(Monomial other) {
    return new ConsLoMonomial(other, this);
  }
  
  //multiplies 2 lists of monomial together
  public ILoMonomial multiplies(ILoMonomial other) {
    return this;
  }
  
  // multiplies this list with a given monomial
  public ILoMonomial multiplyHelp(Monomial that) {
    return this;
  }
}

// a cons list of monomials
class ConsLoMonomial implements ILoMonomial {
  Monomial first;
  ILoMonomial rest;

  // constructor
  ConsLoMonomial(Monomial first, ILoMonomial rest) {
    this.first = first;
    this.rest= rest;
  }

  // evaluates this list with the given value
  public int evaluateHelp(int x) {
    return this.first.substitute(x) + this.rest.evaluateHelp(x);
  }

  // nothing to normalize
  public ILoMonomial normalize() {
    return this.sort().filter0coef();
  }

  // nothing to sort
  public ILoMonomial sort() {
    return this.rest.sort().insert(this.first);
  }

  // inserts the given monomial
  public ILoMonomial insert(Monomial given) {
    if (this.first.compareTo(given) >= 0) {
      return new ConsLoMonomial(given, this);
    } else {
      return new ConsLoMonomial(this.first, this.rest.insert(given));
    }
  }

  // filters out all zero coefficient monomials 
  public ILoMonomial filter0coef() {
    if (this.first.isZeroCoef()) {
      return this.rest.filter0coef();
    } else {
      return new ConsLoMonomial(this.first, this.rest.filter0coef()); 
    }
  }

  // integrates the given list with this list
  public ILoMonomial adds(ILoMonomial other) {
    return this.rest.adds(other.addHelp(this.first));
  }

  // adds a single monomial to this list
  public ILoMonomial addHelp(Monomial given) {
    if (this.first.compareTo(given) == 0) {
      return new ConsLoMonomial(this.first.integrate(given), this.rest);
    } else {
      return new ConsLoMonomial(this.first, this.rest.addHelp(given));
    }
  }
  
  //multiplies 2 lists of monomial together
  public ILoMonomial multiplies(ILoMonomial other) {
    return this.rest.multiplies(other).adds(other.multiplyHelp(this.first));
  }
  
  // multiplies this list with a given monomial
  public ILoMonomial multiplyHelp(Monomial that) {
    return new ConsLoMonomial(this.first.mul(that), this.rest.multiplyHelp(that));
  }
}

// a polynomial 
class Polynomial {
  ILoMonomial monomials;

  // constructor
  Polynomial(ILoMonomial monomials) {
    this.monomials = monomials.normalize();
  }

  // convenient constructor
  Polynomial() {
    this.monomials = new MtLoMonomial();
  }

  // evaluates the polynomial at the given value
  int evaluate(int x) {
    return this.monomials.evaluateHelp(x);
  }

  // adds two polynomials in the mathematical sense
  Polynomial add(Polynomial other) {
    return new Polynomial(this.monomials.adds(other.monomials));
  }
  
  // is this Polynomial the same as the given Polynomial
  boolean samePolynomial(Polynomial other) {
    return true;
//    return this.monomials.sameILoMonomial(other.monomials);
  }
  
  // multiplies two polynomials
  Polynomial multiply(Polynomial other) {
    return new Polynomial(this.monomials.multiplies(other.monomials));
  }
  
}


class ExamplesPolynomial {
  Monomial c1 = new Monomial(1, 0);
  Monomial c5 = new Monomial(5, 0);
  Monomial x2 = new Monomial(2,1);
  Monomial x3pow2 = new Monomial(3, 2);
  Monomial xpow3 = new Monomial(1, 3);

  ILoMonomial mt = new MtLoMonomial();
  ILoMonomial list0 = new ConsLoMonomial(new Monomial(1, 1), new MtLoMonomial());
  ILoMonomial list1 = new ConsLoMonomial(x2, new ConsLoMonomial(x3pow2, new MtLoMonomial()));
  ILoMonomial list2 = new ConsLoMonomial(xpow3, new ConsLoMonomial(x3pow2, 
      new ConsLoMonomial(c1, new MtLoMonomial())));
  ILoMonomial list3 = new ConsLoMonomial(x2, new ConsLoMonomial(x3pow2, 
      new ConsLoMonomial(x2, new MtLoMonomial())));
  ILoMonomial list4 = new ConsLoMonomial(c5, new ConsLoMonomial(x3pow2, new MtLoMonomial()));


  Polynomial p0 = new Polynomial(mt);     // '()
  Polynomial p1 = new Polynomial(list1);  // 2x + 3x^2
  Polynomial p2 = new Polynomial(list2);  // 1 + 3x^2 + x^3
  Polynomial p3 = new Polynomial(list3);  // 2x + 3x^2 + 2x
  Polynomial p4 = new Polynomial(list4);  // 5 + 3x^2
  Polynomial p5 = new Polynomial(list0);  // x

  boolean testEvaluate(Tester t) {
    return t.checkExpect(p0.evaluate(9999), 0)
        && t.checkExpect(p1.evaluate(0), 0)
        && t.checkExpect(p2.evaluate(1), 5)
        && t.checkExpect(p3.evaluate(3), 39)
        && t.checkExpect(p4.evaluate(4), 53);
  }

  boolean testNormalize(Tester t) {
    return t.checkExpect(mt.normalize(), mt)
        && t.checkExpect(list1.normalize(), list1)
        && t.checkExpect(list2.normalize(), new ConsLoMonomial(c1, new ConsLoMonomial(x3pow2, 
            new ConsLoMonomial(xpow3, new MtLoMonomial()))))
        && t.checkExpect(list3.normalize(), new ConsLoMonomial(x2, new ConsLoMonomial(x2, 
            new ConsLoMonomial(x3pow2, new MtLoMonomial()))))
        && t.checkExpect(list4.normalize(), list4);
  }

  boolean testAdd(Tester t) {
    return t.checkExpect(p1.add(p0), p1)
        && t.checkExpect(p1.add(p1), new Polynomial(new ConsLoMonomial(new Monomial(4,1), 
            new ConsLoMonomial(new Monomial(6, 2), new MtLoMonomial()))))
        && t.checkExpect(p3.add(p5), new Polynomial(new ConsLoMonomial(new Monomial(5,1), 
            new ConsLoMonomial(new Monomial(3, 2), new MtLoMonomial()))))
        && t.checkExpect(p2.add(p4), new Polynomial(new ConsLoMonomial(new Monomial(6,0), 
            new ConsLoMonomial(new Monomial(6, 2), new ConsLoMonomial(new Monomial(1, 3), 
                new MtLoMonomial())))));
  }
  

  
  boolean testMultiply(Tester t) {
    return t.checkExpect(p1.multiply(p0), p0)
        && t.checkExpect(p0.multiply(p1), p0)
        && t.checkExpect(p1.multiply(new Polynomial(new 
            ConsLoMonomial(c1, new MtLoMonomial()))), p1)
        && t.checkExpect(p5.multiply(p5), new Polynomial(new ConsLoMonomial(new Monomial(1,2), 
            new MtLoMonomial())))
        && t.checkExpect(p1.multiply(p5), new Polynomial(new ConsLoMonomial(new Monomial(2,2), 
            new ConsLoMonomial(new Monomial(3, 3), new MtLoMonomial()))))
        && t.checkExpect(p2.multiply(p4), new Polynomial(new ConsLoMonomial(new Monomial(5,0), 
            new ConsLoMonomial(new Monomial(18, 2), new ConsLoMonomial(new Monomial(5, 3), 
                new ConsLoMonomial(new Monomial(9, 4), new ConsLoMonomial(new Monomial(3, 5), 
                new MtLoMonomial())))))));
  }
}















