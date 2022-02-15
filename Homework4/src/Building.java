//import tester.Tester;
//
//class Building {
//  String name;
//  Address address;
//  
//  Building(String name, Address address) {
//    this.name = name;
//    this.address = address;
//  }
//  
//  // is this building the same building as the given one?
//  boolean sameBuilding(Building other) {
//    return this.name.equals(other.name) 
//        && this.address.sameAddress(other.address);
//  }
//}
//
//class Address {
//  String street;
//  int number;
//  
//  Address(String street, int number) {
//    this.number = number;
//    this.street = street;
//  }
//  
//  // is this address the same address as the given one?
//  boolean sameAddress(Address other) {
//    return this.number == other.number
//        && this.street.equals(other.street);
//  }
//}
//
//class ExamplesBuilding {
//  // TODO: add examples + tests
//  Address forysth = new Address("Forsyth Street", 115);
//  Address forysth2 = new Address("Forsyth Street", 115);
//  Address huntington = new Address("Huntington Avenue", 360);
//  
//  Building shillman = new Building("Shillman Hall", forysth);
//  Building shillman2 = new Building("Shillman Hall", forysth2);
//  Building ell = new Building("Ell Hall", huntington);
//  
//  boolean testSameAddress(Tester t) {
//    return t.checkExpect(forysth.sameAddress(forysth), true)
//        && t.checkExpect(forysth2.sameAddress(forysth), true)
//        && t.checkExpect(forysth.sameAddress(huntington), false)
//        && t.checkExpect(huntington.sameAddress(forysth2), false);
//  }
//  
//  boolean testSameBuilding(Tester t) {
//    return t.checkExpect(shillman.sameBuilding(shillman), true)
//        && t.checkExpect(shillman2.sameBuilding(shillman), true)
//        && t.checkExpect(ell.sameBuilding(shillman), false)
//        && t.checkExpect(shillman2.sameBuilding(ell), false);
//  }
//  
//  
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
