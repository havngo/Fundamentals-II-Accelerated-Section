import tester.Tester;

// a campus tour
class CampusTour {
  int startTime; // minutes from midnight
  ITourLocation startingLocation;

  CampusTour(int startTime, ITourLocation startingLocation) {
    this.startTime = startTime;
    this.startingLocation = startingLocation;
  }

  // is this tour the same tour as the given one?
  boolean sameTour(CampusTour other) {
    return this.startTime == other.startTime 
        && this.startingLocation.sameRoute(other.startingLocation);
  }
}

// a spot on the tour
interface ITourLocation {

  // is this the same as a given tour?
  boolean sameRoute(ITourLocation other);
}

abstract class ATourLocation implements ITourLocation {
  String speech; // the speech to give at this spot on the tour

  ATourLocation(String speech) {
    this.speech = speech;
  }

  boolean sameEnd(TourEnd other) {
    return false;
  }

  boolean sameMandaTory(Mandatory other) {
    return false;
  }

  boolean sameBranch(BranchingTour other) {
    return false;
  }
}

// the end of the tour
class TourEnd extends ATourLocation {
  ICampusLocation location;

  TourEnd(String speech, ICampusLocation location) {
    super(speech);
    this.location = location;
  }

  // is this ending location the same as the other ending location?
  boolean sameEnd(TourEnd other) {
    return this.location.sameLoc(other.location);
  }
}

//a mandatory spot on the tour with the next place to go
class Mandatory extends ATourLocation {
  ICampusLocation location;
  ITourLocation next;

  Mandatory(String speech, ICampusLocation location, ITourLocation next) {
    super(speech);
    this.location = location;
    this.next = next;
  }

  // is this spot the same as the other spot
  boolean sameMandaTory(Mandatory other) {
    return this.location.sameLoc(other.location);
  }
}

// up to the tour guide where to go next
class BranchingTour extends ATourLocation {
  ITourLocation option1;
  ITourLocation option2;

  BranchingTour(String speech, ITourLocation option1, ITourLocation option2) {
    super(speech);
    this.option1 = option1;
    this.option2 = option2;
  }

  // is this spot the same as the other spot
  boolean sameBranch(BranchingTour other) {
    return false; ////////////////////////////////////////////
  }
}

// a spot on campus
interface ICampusLocation {

  // is this spot the same as the other spot?
  boolean sameLoc(ICampusLocation other);

  // is this spot the same as the other building?
  boolean sameBuilding(Building other);

  // is this spot the same as the other quad?
  boolean sameQuad(Quad other);
}

// a building on campus
class Building implements ICampusLocation {
  String name;
  Address address;

  Building(String name, Address address) {
    this.name = name;
    this.address = address;
  }

  // is this building the same building as the given one?
  public boolean sameBuilding(Building other) {
    return this.name.equals(other.name) 
        && this.address.sameAddress(other.address);
  }

  // is this building the same as the quad?
  public boolean sameQuad(Quad other) {
    return false;
  }

  // is this spot the same as the other spot?
  public boolean sameLoc(ICampusLocation other) {
    return other.sameBuilding(this);
  }
}

// represents an address
class Address {
  String street;
  int number;

  Address(String street, int number) {
    this.number = number;
    this.street = street;
  }

  // is this address the same address as the given one?
  boolean sameAddress(Address other) {
    return this.number == other.number
        && this.street.equals(other.street);
  }
}

// a quad surrounded by building(s) on campus
class Quad implements ICampusLocation {
  String name;
  ILoCampusLocation surroundings; // in clockwise order, starting north

  Quad(String name, ILoCampusLocation surroundings) {
    this.name = name;
    this.surroundings = surroundings;
  }

  //is this building the same building as the given one?
  public boolean sameBuilding(Building other) {
    return false;
  }

  // is this building the same as the quad?
  public boolean sameQuad(Quad other) {
    return this.name.equals(other.name) 
        && this.surroundings.sameList(other.surroundings);
  }

  // is this spot the same as the other spot?
  public boolean sameLoc(ICampusLocation other) {
    return other.sameQuad(this);
  }

}

// a list of spots on campus
interface ILoCampusLocation {
  
  boolean sameList(ILoCampusLocation other);
}

// an empty list of spots on campus
class MtLoCampusLocation implements ILoCampusLocation {
  
  public boolean sameList(ILoCampusLocation other) {
    return false;
  }
}

// a list of more than 1 spots on campus
class ConsLoCampusLocation implements ILoCampusLocation {
  ICampusLocation first;
  ILoCampusLocation rest;

  ConsLoCampusLocation(ICampusLocation first, ILoCampusLocation rest) {
    this.first = first;
    this.rest = rest;
  }
  
  public boolean sameList(ILoCampusLocation other) {
    return false;
  }
}

class ExamplesCampus {
  Address forysth = new Address("Forsyth Street", 115);
  Address forysth2 = new Address("Forsyth Street", 151);
  Address huntington = new Address("Huntington Avenue", 360);

  ICampusLocation shillman = new Building("Shillman Hall", forysth);
  ICampusLocation ryder = new Building("Ryder Hall", forysth2);
  ICampusLocation ruggles = new Building("Ruggle Station", new Address("Tremont Street", 1150));
  ICampusLocation centennial = new Quad("Centennial Common", new ConsLoCampusLocation(shillman, 
      new ConsLoCampusLocation(ruggles, new ConsLoCampusLocation(ryder, 
          new MtLoCampusLocation()))));

  ICampusLocation ell = new Building("Ell Hall", huntington);
  ICampusLocation dodge = new Building("Dodge Hall", huntington);
  ICampusLocation richards = new Building("Richards Hall", huntington);
  ICampusLocation krentzman = new Quad("Krentzman Quadrangle", new ConsLoCampusLocation(dodge, 
      new ConsLoCampusLocation(ell, new ConsLoCampusLocation(richards, 
          new MtLoCampusLocation()))));

  ITourLocation end = new TourEnd("This is the end of this tour.", krentzman);
  ITourLocation manda1 = new Mandatory("We'll visit the Ell Hall", ell, end);
  ITourLocation manda2 = new Mandatory("We'll visit the Shillman Hall", shillman, manda1);
  ITourLocation manda3 = new Mandatory("We'll visit the Ruggles", ruggles, manda1);
  ITourLocation branch1 = new BranchingTour("Ell Hall or Shillman", manda2, manda3);

  ITourLocation manda4 = new Mandatory("We'll visit the Centennial Common", centennial, branch1);
  ITourLocation branch2 = new BranchingTour("Centennial Common or End", manda4, end);

  CampusTour tour1 = new CampusTour(600, manda4);
  CampusTour tour2 = new CampusTour(9600, branch2);


}


























