import tester.Tester;

interface ILoString {
  
  //reverse the list of string
  ILoString reverse();
  
  //converts this los to ConsLoString and/or MtLoString
  ILoString normalize();

  // normalizes the los itself and put a given (normalized) los at the end of this list.
  ILoString normalizeHelper(ILoString acc);

  // leftscans this list of string
  ILoString scanConcat();
  
  // concats the given string to this list
  ILoString scanConcatHelper(String acc);
}

class MtLoString implements ILoString {

  MtLoString() {};

  public ILoString reverse() {
    return new  MtLoString();
  }

  public ILoString normalize() {
    return new  MtLoString();
  }

  public ILoString normalizeHelper(ILoString acc) {
    return acc;
  }

  public ILoString scanConcat() {
    return new MtLoString();
  }

  public ILoString scanConcatHelper(String acc) {
    return this;
  }
}

class ConsLoString implements ILoString {
  String first;
  ILoString rest;

  ConsLoString(String first, ILoString rest) {
    this.first = first;
    this.rest = rest;
  }

  public ILoString reverse() {
    return new SnocLoString(this.rest.reverse(), this.first);
  }

  public ILoString normalize() {
    return new ConsLoString(this.first, this.rest.normalize());
  }

  public ILoString normalizeHelper(ILoString acc) {
    return new ConsLoString(this.first, this.rest.normalizeHelper(acc));
  }

  public ILoString scanConcat() {
    return this.scanConcatHelper("");
  }

  public ILoString scanConcatHelper(String acc) {
    return new ConsLoString(acc + this.first, this.rest.scanConcatHelper(acc + this.first));
  }
}

class SnocLoString implements ILoString {
  ILoString front;
  String last;

  SnocLoString(ILoString front, String last) {
    this.front = front;
    this.last = last;
  }

  public ILoString reverse() {
    return new ConsLoString(this.last, this.front.reverse());
  }

  public ILoString normalize() {
    return this.front.normalizeHelper(new ConsLoString(this.last, new MtLoString()));
  }

  public ILoString normalizeHelper(ILoString acc) {
    return this.front.normalizeHelper(new ConsLoString(this.last, acc));
  }

  public ILoString scanConcat() {
    return this.normalize().scanConcat();
  }

  public ILoString scanConcatHelper(String acc) {
    return this.normalize().scanConcatHelper(acc);
  }
}

class AppendLoString implements ILoString {
  ILoString left;
  ILoString right;

  AppendLoString(ILoString left, ILoString right) {
    this.left = left; 
    this.right = right;
  }

  public ILoString reverse() {
    return new AppendLoString(this.right.reverse(), this.left.reverse());
  }

  public ILoString normalize() {
    return this.left.normalizeHelper(this.right.normalize());    
  }

  // returns a normalized version of this, given los.
  public ILoString normalizeHelper(ILoString acc) {
    return this.left.normalizeHelper(this.right.normalizeHelper(acc));
  }

  public ILoString scanConcat() {
    return this.normalize().scanConcat();
  }

  public ILoString scanConcatHelper(String acc) {
    return this.normalize().scanConcatHelper(acc);
  }

}

class ExamplesString {
  ILoString mt = new MtLoString();
  ILoString cons = new ConsLoString("c", new ConsLoString("o",
      new ConsLoString("n", new ConsLoString("s", mt))));
  ILoString consSnoc = new ConsLoString("s", new ConsLoString("n",
      new ConsLoString("o", new ConsLoString("c", mt))));
  ILoString snoc = new SnocLoString(new SnocLoString(new SnocLoString(new SnocLoString(mt, "s"), "n"), "o"), "c");
  ILoString snocCons = new SnocLoString(new SnocLoString(new SnocLoString(new SnocLoString(mt, "c"), "o"), "n"), "s");
  ILoString append = new AppendLoString(cons, snoc);

  boolean testReverse(Tester t) {
    return t.checkExpect(cons.reverse(), snoc)
        && t.checkExpect(snoc.reverse(), cons)
        //&& t.checkExpect(consSnoc.reverse(), cons) //should it be true to cons?
        //&& t.checkExpect(snocCons.reverse(), snoc)
        && t.checkExpect(append, append);
  }

  boolean testNormalize(Tester t) {
    return t.checkExpect(cons.normalize(), cons) 
        && t.checkExpect(snoc.normalize(), consSnoc)
        && t.checkExpect(snocCons.normalize(), cons)
        && t.checkExpect(append.normalize(),  
            new ConsLoString("c", new ConsLoString("o", new ConsLoString("n", 
                new ConsLoString("s", new ConsLoString("s", new ConsLoString("n", 
                new ConsLoString("o", new ConsLoString("c", mt)))))))));
  }

  boolean testScanConcat(Tester t) {
    return t.checkExpect(cons.scanConcat(), 
        new ConsLoString("c", new ConsLoString("co", new ConsLoString("con", new ConsLoString("cons", mt)))))
        && t.checkExpect(snoc.scanConcat(),
            new ConsLoString("s", new ConsLoString("sn", new ConsLoString("sno", new ConsLoString("snoc", mt)))))
        && t.checkExpect(append.scanConcat(),
            new ConsLoString("c", new ConsLoString("co", new ConsLoString("con", new ConsLoString("cons", 
                new ConsLoString("conss", new ConsLoString("conssn", 
                    new ConsLoString("conssno", new ConsLoString("conssnoc", mt)))))))));
  }
}

