import tester.Tester;
import java.awt.Color;
import javalib.worldimages.*;

// to represent a bouncing ball
class BouncingBall {
  Posn pos;
  Color color;
  int size;
  int dx; // how fast is the ball moving to the right?
  int dy; // how fast is the ball moving downward?
  // +y down, +x right

  // the constructor
  BouncingBall(Posn pos, Color color, int size, int dx, int dy) {
    this.pos = pos;
    this.color = color;
    this.size = size;
    this.dx = dx;
    this.dy = dy;
  }

  //In BouncingBall
  /* TEMPLATE:
  Fields:
  ... this.pos ...         -- Posn
  ... this.color ...       -- Color
  ... this.size ...        -- int
  ... this.dx ...          -- int
  ... this.dy ...          -- int

  Methods:
  ... this.area() ... -- double
  ... this.circumference()... -- double
  ... this.circumference() ... -- double
  ... this.distanceTo(BouncingBall) ... -- double
  ... this.overlaps(BouncingBall) ... -- boolean
  ... this.move() ...   -- BouncingBall
  ... this.bounceX() ... -- BouncingBall
  ... this.bounceY() ... -- BouncingBall
  ... this.collidesX(Posn, Posn) ... -- boolean
  ... this.collidesY(Posn, Posn) ... -- boolean

  Methods for fields:
  ... this.pos.mmm(??) ...    -- ??
  ... this.color.mmm(??) ...  -- ??
   */

  // returns the area of this BouncingBall
  double area() {
    return Math.PI * Math.pow(this.size, 2);
  }

  // returns the circumference method for BouncingBall
  double circumference() {
    return Math.PI * this.size * 2;
  }

  // returns the distance between each BouncingBall's center
  double distanceTo(BouncingBall that) {
    return Math.sqrt((Math.pow((this.pos.x - that.pos.x), 2)) 
        + (Math.pow((this.pos.y - that.pos.y), 2)));
  }

  // returns whether or not the given BouncingBball overlaps with a certain BouncingBall
  boolean overlaps(BouncingBall that) {
    return this.distanceTo(that) < (this.size + that.size);
  }

  // Returns a new BouncingBall that's just like this BouncingBall, but moved
  // by this BouncingBall's dx and dy
  BouncingBall move() {
    return new BouncingBall(new Posn(this.pos.x + this.dx, this.pos.y + this.dy), 
        this.color, this.size, this.dx, this.dy);
  }

  // Returns a new BouncingBall that represents this BouncingBall just after
  // it has bounced off a side wall. Does not actually move the ball.
  // This method will be called automatically when `collidesX` returns true
  BouncingBall bounceX() {
    return new BouncingBall(this.pos, this.color, this.size, 0 - this.dx, this.dy);
  }

  // Like bounceX, except for using the top or bottom walls
  BouncingBall bounceY() {
    return new BouncingBall(this.pos, this.color, this.size, this.dx, 0 - this.dy);
  }

  // Detects whether the ball is colliding with a side wall.
  // If the ball has already collided and negated their velocities, don't have it collide again.
  // This prevents the case where it spawns on a wall or corner and it infinitely collides. It's
  // one and done.

  boolean collidesX(Posn topLeft, Posn botRight) {
    return ((this.dx < 0) && (this.pos.x - this.size <= topLeft.x))
        || ((this.dx > 0) && (this.pos.x + this.size >= botRight.x));
  }

  // Detects whether the ball is colliding with a top or bottom wall.
  // The same constraints apply for collidesX as they do collidesY.
  // Recall that +y is downwards.

  boolean collidesY(Posn topLeft, Posn botRight) {
    return ((this.dy < 0) && (this.pos.y - this.size <= topLeft.y))
        || ((this.dy > 0) && (this.pos.y + this.size >= botRight.y));
  }
}

//Examples to test Big Bang with Bouncing Balls.
class ExamplesBouncingBalls {

  int WIDTH = 300;
  int HEIGHT = 300;

  // NOTE: We have provided BouncingWorld for you, in the starter code.
  // We'll see how it works in a few lectures
  boolean testBigBang(Tester t) {
    BouncingWorld w = new BouncingWorld(WIDTH, HEIGHT);
    return w.bigBang(WIDTH, HEIGHT, 0.01);
  }

  // bouncing ball examples
  BouncingBall b0 = new BouncingBall(new Posn(0, 0), Color.BLUE, 5, -1, 1);
  BouncingBall b = new BouncingBall(new Posn(0, 0), Color.BLUE, 5, 1, 1);
  BouncingBall b2 = new BouncingBall(new Posn(0, 10), Color.PINK, 5, 2, -3);
  BouncingBall b3 = new BouncingBall(new Posn(1, 10), Color.RED, 3, 100, -100);
  BouncingBall b4 = new BouncingBall(new Posn(-2, 3), Color.YELLOW, 10, -6, 3);
  BouncingBall b5 = new BouncingBall(new Posn(-2, 3), Color.YELLOW, 10, 6, 3);
  BouncingBall b6 = new BouncingBall(new Posn(1, 10), Color.BLACK, 3, -100, -100);
  BouncingBall b7 = new BouncingBall(new Posn(0, 0), Color.BLUE, 5, 1, -1);
  BouncingBall b8 = new BouncingBall(new Posn(-2, 3), Color.YELLOW, 10, -6, -3);
  BouncingBall b9 = new BouncingBall(new Posn(1, 10), Color.BLACK, 3, 100, 100);

  // test the method area for the class BouncingBall
  boolean testArea(Tester t) {
    return t.checkInexact(b.area(), 78.5, 0.001) 
        && t.checkInexact(b3.area(), 28.27, 0.001)
        && t.checkInexact(b4.area(), 314.159, 0.001);
  }

  // test the method circumference for the class BouncingBall
  boolean testCircumference(Tester t) {
    return t.checkInexact(b.circumference(), 31.4, 0.001) 
        && t.checkInexact(b3.circumference(), 18.85, 0.001)
        && t.checkInexact(b4.circumference(), 62.83186, 0.001);
  }

  // test the method distanceTo for the class BouncingBall
  boolean testDistanceTo(Tester t) {
    return t.checkInexact(b.distanceTo(b2), 10.0, 0.001)
        && t.checkInexact(b2.distanceTo(b3), 1.0, 0.001)
        && t.checkInexact(b3.distanceTo(b4), 7.62, 0.001);
  }

  //test the method overlaps for the class BouncingBall
  boolean testOverlaps(Tester t) {
    return t.checkExpect(b.overlaps(b), true) 
        && t.checkExpect(b.overlaps(b3), false)
        && t.checkExpect(b4.overlaps(b), true)
        && t.checkExpect(b3.overlaps(b4), true);
  }

  //test the method move for the class BouncingBall
  boolean testMove(Tester t) {
    return t.checkExpect(b.move(), new BouncingBall(new Posn(1, 1), Color.BLUE, 5, 1, 1))
        && t.checkExpect(b2.move(), new BouncingBall(new Posn(2, 7), Color.PINK, 5, 2, -3))
        && t.checkExpect(b3.move(), new BouncingBall(new Posn(101, -90), Color.RED, 3, 100, -100))
        && t.checkExpect(b4.move(), new BouncingBall(new Posn(-8, 6), Color.YELLOW, 10, -6, 3));
  }

  //test the method bounceX for the class BouncingBall
  boolean testBounceX(Tester t) {
    return t.checkExpect(b.bounceX(), new BouncingBall(new Posn(0, 0), Color.BLUE, 5, -1, 1))
        && t.checkExpect(b2.bounceX(), new BouncingBall(new Posn(0, 10), Color.PINK, 5, -2, -3))
        && t.checkExpect(b3.bounceX(), new BouncingBall(new Posn(1, 10), Color.RED, 3, -100, -100))
        && t.checkExpect(b4.bounceX(), new BouncingBall(new Posn(-2, 3), Color.YELLOW, 10, 6, 3));
  }

  // test the method bounceY for the class BouncingBall
  boolean testBounceY(Tester t) {
    return t.checkExpect(b.bounceY(), new BouncingBall(new Posn(0, 0), Color.BLUE, 5, 1, -1))
        && t.checkExpect(b2.bounceY(), new BouncingBall(new Posn(0, 10), Color.PINK, 5, 2, 3))
        && t.checkExpect(b3.bounceY(), new BouncingBall(new Posn(1, 10), Color.RED, 3, 100, 100))
        && t.checkExpect(b4.bounceY(), new BouncingBall(new Posn(-2, 3), Color.YELLOW, 10, -6, -3));
  }

  // test the method collidesX for the class BouncingBall
  boolean testCollidesX(Tester t) {
    return t.checkExpect(b.collidesX(new Posn(0, 300), new Posn(300, 0)), false)
        // ball in left corner, positive dx
        && t.checkExpect(b0.collidesX(new Posn(0, 300), new Posn(300, 0)), true)
        // ball in left corner, negative dx
        && t.checkExpect(b2.collidesX(new Posn(-10, 300), new Posn(300, 300)), false)
        // ball roaming around in the middle
        && t.checkExpect(b4.collidesX(new Posn(-2, -10), new Posn(100, 600)), true)
        // ball on left edge, negative dx velocity
        && t.checkExpect(b5.collidesX(new Posn(-2, -10), new Posn(100, 600)), false)
        // ball on left edge, positive dx velocity
        && t.checkExpect(b6.collidesX(new Posn(-100, -20), new Posn(1, 600)), false)
        // ball on right edge, negative dx velocity
        && t.checkExpect(b3.collidesX(new Posn(-100, -20), new Posn(1, 600)), true);
    // ball on right edge, positive dx velocity
  }

  //test the method collidesY for the class BouncingBall
  boolean testCollidesY(Tester t) {
    return t.checkExpect(b.collidesY(new Posn(0, 0), new Posn(300, 300)), false)
        // ball in left corner, positive dy
        && t.checkExpect(b7.collidesY(new Posn(0, 0), new Posn(300, 300)), true)
        // ball in left corner, negative dy
        && t.checkExpect(b2.collidesY(new Posn(-10, 0), new Posn(300, 300)), false)
        // ball roaming around in the middle
        && t.checkExpect(b4.collidesY(new Posn(-100, 3), new Posn(200, 100)), false)
        // ball on top edge, positive dy velocity
        && t.checkExpect(b8.collidesY(new Posn(-100, 3), new Posn(200, 100)), true)
        // ball on top edge, negative dy velocity
        && t.checkExpect(b3.collidesY(new Posn(-100, -100), new Posn(100, 10)), false)
        // ball on bottom edge, negative dy velocity
        && t.checkExpect(b9.collidesY(new Posn(-100, -100), new Posn(100, 10)), true);
    // ball on bottom edge, positive dy velocity
  }
}