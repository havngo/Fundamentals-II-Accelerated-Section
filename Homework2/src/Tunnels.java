import tester.*;                // The tester library
import javalib.worldimages.*;   // images, like RectangleImage or OverlayImages
import javalib.funworld.*;      // the abstract World class and the big-bang library
import javalib.worldcanvas.WorldCanvas;
import java.awt.Color;          // general colors (as triples of red,green,blue values)
// and predefined colors (Color.RED, Color.GRAY, etc.)

// ----------------------------------------------------------
interface ITunnelSystem { 

  // to draw the tunnelsystem
  WorldImage draw();

  // to determine if the tunnelsystem and its offshoots are safe
  boolean isCrowdSafe();

  //to determine if the tunnelsystem and its offshoots are safe based on the previous
  // tunnelsystem's capacity
  boolean isSafeHelper(int prevCap);

  // to combine two tunnelsystems and match their scale
  //ITunnelSystem combine(int firstLength, int secondLength,
  //int firstCapacity, int secondCapacity, String firstDirection, String secondDirection,
  // double firstScale, double secondScale, ITunnelSystem newerTunnels);

  ITunnelSystem combine(int firstLength, int secondLength, int firstCapacity, int secondCapacity,
      String firstDirection, String secondDirection, double firstScale, double secondScale, 
      ITunnelSystem newerTunnels);

  // helper method to rescale given the factor
  ITunnelSystem scale(double scale);

  // computes the width of the tunnel
  double getWidth();

  // computes the east-most position
  double getEast(double acc);

  // computes the west-most position
  double getWest(double acc);
}

//--------------

// represents an exit from the tunnels
class Exit implements ITunnelSystem {

  // the constructor
  Exit() {

  }

  /* TEMPLATE:
  Fields:
  N/A
   Methods:
  ... this.draw()...                  -- WorldImage
  ... this.isCrowdSafe()...                -- boolean
  ... this.isSafeHelper(ILoString)... -- boolean
   */

  // to draw the exit, an orange solid circle
  public WorldImage draw() {

    /* TEMPLATE: everything in the template for Exit */

    return new CircleImage(10, OutlineMode.SOLID, Color.ORANGE);
  }

  // the following of an exit, which is presumed to have infinite capacity, will always b true
  public boolean isCrowdSafe() {

    /* TEMPLATE: everything in the template for Exit */

    return true;
  }

  // the following of an exit, regardless of presumed capacity, will always be true;
  public boolean isSafeHelper(int prevCap) {

    /* TEMPLATE: everything in the template for Exit, plus
     * 
     * Fields of Parameters:
     * ... prevCap ...                -- int*/

    return true;
  }

  public ITunnelSystem scale(double scale) {
    return this;
  }

  public ITunnelSystem combine(int firstLength, int secondLength, int firstCapacity, 
      int secondCapacity, String firstDirection, String secondDirection, double firstScale,
      double secondScale, ITunnelSystem newerTunnels) {
    ITunnelSystem newerTunnel = newerTunnels.scale(secondScale);

    ITunnelSystem ret = new Fork(firstLength, secondLength, firstCapacity, secondCapacity, 
        firstDirection, secondDirection, this, newerTunnel);

    return ret;
  }

  public double getWidth() {
    return this.getEast(0.0) + this.getWest(0.0);
  }

  public double getEast(double acc) {
    return acc;
  }

  public double getWest(double acc) {
    return acc;
  }
}

//--------------

// represents a straight section of tunnel
class Hallway implements ITunnelSystem {

  // How long this tunnel section is
  int length;
  // The direction of this tunnel: one of the strings "N", "S", "E" or "W"
  String direction;
  // The maximum number of students who can pass through this hallway per second
  int capacity;
  // The rest of the tunnel system
  ITunnelSystem more; 

  // the constructor
  Hallway(int length, String direction, int capacity, ITunnelSystem more) {
    this.length = length;
    this.direction = direction;
    this.capacity = capacity;
    this.more = more;
  }

  /* TEMPLATE:
  Fields:
  ... this.length...                  -- int
  ... this.direction...               -- String
  ... this.capacity...                -- int
  ... this.more...                    -- ITunnelSystem
   Methods:
  ... this.draw()...                  -- WorldImage
  ... this.isCrowdSafe()...                -- boolean
  ... this.isSafeHelper(ILoString)... -- boolean
   */

  // to draw the hallway and all of its offshoots
  public WorldImage draw() {

    /* TEMPLATE: everything in the template for Hallway*/

    WorldImage baseRect =
        new VisiblePinholeImage(
            new RectangleImage(
                this.capacity, this.length, OutlineMode.OUTLINE, Color.BLUE).movePinhole(
                    0, this.length / 2));
    int dx = 0;
    int dy = this.length;

    if (this.direction.equals("E")) {
      baseRect = new RotateImage(baseRect, 270);
      dx = this.length;
      dy = 0;
    }
    else if (this.direction.equals("N")) {
      baseRect = new RotateImage(baseRect, 180);
      dx = 0;
      dy = -this.length;
    }
    else if (this.direction.equals("W")) {
      baseRect = new RotateImage(baseRect, 90);
      dx = -this.length;
      dy = 0;
    }
    return (new OverlayImage(baseRect, this.more.draw())).movePinhole(-1 * dx, -1 * dy);
  }

  // to determine if the hallway and its offshoots are safe-- if the space before it has less
  // space than the one its going into. this case is if the starting point is a hallway
  public boolean isCrowdSafe() {

    /* TEMPLATE: everything in the template for Hallway*/

    return this.more.isSafeHelper(this.capacity);
  }

  // to determine if the hallway and its offshoots are safe relative to the capacity before it.
  public boolean isSafeHelper(int prevCap) {

    /* TEMPLATE: everything in the template for Hallway, plus
     * 
     * Fields of Parameters:
     * ... prevCap ...                -- int*/

    return (prevCap <= this.capacity) && this.more.isSafeHelper(this.capacity);
  }

  public ITunnelSystem combine(int firstLength, int secondLength, int firstCapacity, 
      int secondCapacity, String firstDirection, String secondDirection, double firstScale,
      double secondScale, ITunnelSystem newerTunnels) {

    ITunnelSystem currTunnel = this.scale(firstScale);
    ITunnelSystem newerTunnel = newerTunnels.scale(secondScale);

    ITunnelSystem ret = new Fork(firstLength, secondLength, firstCapacity, secondCapacity, 
        firstDirection, secondDirection, currTunnel, newerTunnel);

    return ret;
  }

  public ITunnelSystem scale(double scale) {
    int newLength = (int) (this.length * scale);
    return new Hallway(newLength, this.direction, this.capacity, 
        this.more.scale(scale));
  }

  public double getWidth() {
    return this.getEast(0.0) + this.getWest(0.0);
  }

  // returns the east-most coordinate
  public double getEast(double acc) {
    if (this.direction.equals("E")) {
      acc += this.length;
    }
    return this.more.getEast(acc);
  }

  // returns the west-most coordinate
  public double getWest(double acc) {
    if (this.direction.equals("W")) {
      acc += this.length;
    }
    return this.more.getWest(acc);
  }
}

//--------------

// represents where one tunnel splits off from another
class Fork implements ITunnelSystem {
  // How long the two branches are
  int firstLength;
  int secondLength;
  // The number of students per second that can pass through the two hallways
  int firstCapacity;
  int secondCapacity;
  // The directions of both hallways: one of "N", "S", "E", or "W"
  String firstDirection;
  String secondDirection;
  // The remaining parts of the tunnel system
  ITunnelSystem first;
  ITunnelSystem second;

  // the constructor
  Fork(int firstLength, int secondLength, int firstCapacity, int secondCapacity, 
      String firstDirection, String secondDirection, ITunnelSystem first, ITunnelSystem second) {
    this.firstLength = firstLength;
    this.secondLength = secondLength;
    this.firstCapacity = firstCapacity;
    this.secondCapacity = secondCapacity;
    this.firstDirection = firstDirection;
    this.secondDirection = secondDirection;
    this.first = first;
    this.second = second;
  }

  /* TEMPLATE:
  Fields:
  ... this.firstLength...                  -- int
  ... this.secondLength...                 -- int
  ... this.firstCapacity...                -- int
  ... this.secondCapacity...               -- int
  ... this.firstDirection...               -- String
  ... this.secondDirection...              -- String
  ... this.first...                        -- ITunnelSystem
  ... this.second...                       -- ITunnelSystem

   Methods:
  ... this.draw()...                  -- WorldImage
  ... this.isCrowdSafe()...                -- boolean
  ... this.isSafeHelper(ILoString)... -- boolean
   */

  // to draw the fork and all of its offshoots
  public WorldImage draw() {

    /* TEMPLATE: everything in the template for Fork*/

    Hallway hall1 = new Hallway(firstLength, firstDirection, firstCapacity, first);
    Hallway hall2 = new Hallway(secondLength, secondDirection, secondCapacity, second);

    WorldImage hall1p = hall1.draw();
    WorldImage hall2p = hall2.draw();

    return (new OverlayImage(hall1p, hall2p));
  }

  // to determine if the fork and its offshoots are safe-- if the space before it has less
  // space than the total capacity of the fork. this case is if the starting point is a fork
  public boolean isCrowdSafe() {

    /* TEMPLATE: everything in the template for Fork*/

    return this.first.isSafeHelper(this.firstCapacity)
        && this.second.isSafeHelper(this.secondCapacity);
  }

  // to determine if the fork and its offshoots are safe relative to the capacity before it.
  public boolean isSafeHelper(int prevCap) {

    /* TEMPLATE: everything in the template for Fork, plus
     * 
     * Fields of Parameters:
     * ... prevCap ...                -- int*/

    return (prevCap <= this.firstCapacity + this.secondCapacity)
        && this.first.isSafeHelper(this.firstCapacity)
        && this.second.isSafeHelper(this.secondCapacity);
  }

  public ITunnelSystem combine(int firstLength, int secondLength, int firstCapacity, 
      int secondCapacity, String firstDirection, String secondDirection, double firstScale,
      double secondScale, ITunnelSystem newerTunnels) {

    ITunnelSystem currTunnel = this.scale(firstScale);
    ITunnelSystem newerTunnel = newerTunnels.scale(secondScale);

    ITunnelSystem ret = new Fork(firstLength, secondLength, firstCapacity, secondCapacity, 
        firstDirection, secondDirection, currTunnel, newerTunnel);

    return ret;
  }

  public ITunnelSystem scale(double scale) {
    int new1stLength = (int) (this.firstLength * scale);
    int new2ndLength = (int) (this.secondLength * scale);

    return new Fork(new1stLength, new2ndLength, this.firstCapacity, this.secondCapacity, 
        this.firstDirection, this.secondDirection, 
        this.first.scale(scale), this.second.scale(scale));
  }

  // returns the length between the leftmost and the eastmost entrance/exist
  public double getWidth() {
    return this.getEast(0.0) + this.getWest(0.0); 
  }

  // returns the east-most position
  public double getEast(double acc) {
    return Math.max(this.first.getEast(acc), this.second.getEast(acc));
  }
  
  // returns the west-most postion
  public double getWest(double acc) {
    return Math.max(this.first.getWest(acc), this.second.getWest(acc));
  }


  // 
  //  public double getWidthHelper(double curr) {
  //    ITunnelSystem fst = new Hallway(firstLength, firstDirection, firstCapacity, first);
  //    ITunnelSystem snd = new Hallway(secondLength, secondDirection, secondCapacity, second);
  //
  //    double pos1 = fst.getWidthHelper(curr);
  //    double pos2 = snd.getWidthHelper(curr);
  //
  //    double temp = 0;
  //
  //    if (pos1 * pos2 < 0) {
  //      if (pos1 < 0) {
  //        temp = pos2 - pos1;
  //      } else {
  //        temp = pos1 - pos2;
  //      }
  //    } else {
  //      if (pos1 > pos2) {
  //        temp = pos1;
  //      }
  //      else {
  //        temp = pos2;
  //      }
  //    }
  //    return temp;   
  //  }

}

//--------------

// To text examples of TunnelSystems and their methods.
class ExamplesTunnels {

  WorldCanvas c = new WorldCanvas(500, 500);

  ITunnelSystem exit = new Exit();

  ITunnelSystem tunnel1 =
      new Hallway(100, "N", 30,
          (new Hallway(100, "E", 30,
              (new Hallway(100, "N", 40,
                  (new Hallway(100, "W", 10, new Exit())))))));

  ITunnelSystem tunnel2 =
      new Hallway(100, "N", 1,
          (new Hallway(100, "W", 80,
              (new Hallway(100, "S", 90,
                  (new Hallway(100, "E", 100, new Exit())))))));

  ITunnelSystem tunnel3 =
      (new Hallway(100, "N", 20,
          (new Fork(30, 30, 10, 10, "N", "E",
              (new Hallway(100, "W", 20,
                  (new Hallway(100, "S", 90,
                      (new Hallway(100, "E", 100, new Exit())))))),
              (new Hallway(100, "W", 80,
                  (new Hallway(100, "S", 90,
                      (new Hallway(100, "E", 100, new Exit()))))))))));

  ITunnelSystem tunnel4 = 
      (new Hallway(100, "S", 20, new Hallway(100, "W", 20, 
          new Fork(100, 100, 20, 20, "N", "S", new Hallway(100, "W", 20, new Exit()), 
              new Hallway(100, "E", 20, new Exit())))));

  ITunnelSystem tunnel5 = 
      (new Hallway(100, "S", 20, new Hallway(100, "W", 20, 
          new Fork(100, 100, 20, 20, "N", "S", new Hallway(100, "W", 20, 
              new Hallway(100, "S", 20, new Hallway(100, "W", 20, new Exit()))), 
              new Hallway(100, "W", 20, new Exit())))));

  //to test the Draw method !!!!!!!!!!! TODO: ADD PLAUSIBLE EXAMPLES, FIGURE OUT FORKS
  boolean testDraw(Tester t) {

    return c.drawScene(new WorldScene(500, 500).placeImageXY(tunnel4.draw(), 250, 250)) && c.show();
  }

  // to test the isSafe method
  boolean testIsSafe(Tester t) {
    return t.checkExpect(tunnel1.isCrowdSafe(), false)
        && t.checkExpect(tunnel2.isCrowdSafe(), true)
        && t.checkExpect(tunnel3.isCrowdSafe(), true);
    // it's all good if a fork's sum is equal to the capacity of the hallway before it!
    // doesn't matter that the two sectors have a smaller capacity
  }

  // to test the isSafeHelper method
  boolean testIsSafeHelper(Tester t) {
    return t.checkExpect(tunnel1.isSafeHelper(40000), false)
        && t.checkExpect(tunnel1.isSafeHelper(1), false)
        && t.checkExpect(tunnel2.isSafeHelper(10), false)
        && t.checkExpect(tunnel2.isSafeHelper(5), false)
        && t.checkExpect(tunnel2.isSafeHelper(1), true)
        && t.checkExpect(tunnel3.isSafeHelper(1), true)
        && t.checkExpect(tunnel3.isSafeHelper(10), true);
  }

  //to test the combine method TODO: fill with examples
  boolean testCombine(Tester t) {
    return t.checkExpect(exit.combine(100, 100, 10, 10, "S", "W", 1, 12, new Exit()),
        new Fork(100, 100, 10, 10, "S", "W", new Exit(), new Exit()))
        && t.checkExpect(tunnel3.combine(100, 100, 10, 10, "S", "W", 1, 12, 
            new Hallway(100, "N", 40, (new Hallway(100, "W", 10, new Exit())))),
            new Fork(100, 100, 10, 10, "S", "W", tunnel3, 
                new Hallway(1200, "N", 40, (new Hallway(1200, "W", 10, new Exit())))))
        && t.checkExpect(tunnel1.combine(100, 100, 10, 10, "S", "W", 2, 5, 
            new Hallway(100, "N", 40, (new Hallway(100, "W", 10, new Exit())))),
            new Fork(100, 100, 10, 10, "S", "W", 
                new Hallway(200, "N", 30,
                    (new Hallway(200, "E", 30,
                        (new Hallway(200, "N", 40,
                            (new Hallway(200, "W", 10, new Exit()))))))), 
                new Hallway(500, "N", 40, (new Hallway(500, "W", 10, new Exit())))));
  }

  //to test the getWidth method TODO: fill with examples
  boolean testGetWidth(Tester t) {

    ITunnelSystem test = new Hallway(100, "S", 20, new Fork(20, 30, 1, 1, "W", "E", 
        new Exit(), new Exit()));
    ITunnelSystem test2 = new Hallway(10, "W", 20, new Fork(20, 30, 1, 1, "N", "S", 
        new Hallway(10, "W", 1, new Exit()), 
        new Hallway(20, "W", 1, new Exit())));
    ITunnelSystem test3 = new Hallway(100, "W", 10, new Hallway(100, "N", 10, 
        new Hallway(100, "E", 10, new Hallway(100, "E", 10, new Exit()))));

    return t.checkInexact(test.getWidth(), 50.0, 0.001)
        && t.checkInexact(tunnel5.getWidth(), 300.0, 0.001)
        && t.checkInexact(tunnel4.getWidth(), 200.0, 0.001)
        && t.checkInexact(test2.getWidth(), 30.0, 0.001)
        && t.checkInexact(test3.getWidth(), 100.0, 0.001);
  }
}