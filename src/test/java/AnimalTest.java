import org.junit.*;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.sql2o.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AnimalTest{
  Animal myAnimal;
  @Before
  public void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/shelter_test", null, null);
    myAnimal = new Animal("Steve", "2016-06-10", "M", "cat", "asshole");
  }

  @After
  public void tearDown() {
    try(Connection con = DB.sql2o.open()) {
      String sql_animals = "DELETE FROM animals *;";
      String sql_owners = "DELETE FROM owners *;";
      con.createQuery(sql_owners).executeUpdate();
      con.createQuery(sql_animals).executeUpdate();
    }
  }

  @Test
  public void animal_instantiatesCorrectly() {
    assertTrue(myAnimal instanceof Animal);
  }

  @Test
  public void getAdmittance_returnsAdmittance_Date(){
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    Date date=null;
    try{
      date = formatter.parse("2016-06-10");
    } catch(Exception e){
      System.out.print("aieee");
    }
    assertEquals(date, myAnimal.getAdmittance());
  }

  @Test
  public void getGender_returnsGender_String(){
    assertEquals("M", myAnimal.getGender());
  }

  @Test
  public void getType_returnsType_String(){
    assertEquals("cat", myAnimal.getType());
  }

  @Test
  public void getBreed_returnsBreed_String(){
    assertEquals("asshole", myAnimal.getBreed());
  }

  @Test
  public void getOwnerId_instantiatesToNeg1_Neg1(){
    assertEquals(-1, myAnimal.getOwnerId());
  }

  @Test
  public void getOwnerId_changesWhenSet_1(){
    myAnimal.setOwnerId(1);
    assertEquals(1, myAnimal.getOwnerId());
  }

  @Test
  public void equals_returnsTrueIfFieldsAreTheSame(){
    Animal myOtherAnimal = new Animal("Steve", "2016-06-10", "M", "cat", "asshole");
    assertTrue(myAnimal.equals(myOtherAnimal));
  }

  @Test
  public void save_savesAnimalToDatabase(){
    myAnimal.save();
    String sql = "SELECT * FROM animals WHERE name = 'Steve'";
    Animal fetchedAnimal;
    try(Connection con = DB.sql2o.open()) {
      fetchedAnimal = con.createQuery(sql).executeAndFetchFirst(Animal.class);
    }
    assertTrue(myAnimal.equals(fetchedAnimal));
  }

  @Test
  public void all_returnsAllInstancesOfAnimals_true(){
    myAnimal.save();
    Animal myOtherAnimal = new Animal("Bobette", "2016-06-10", "F", "dog", "poodle");
    myOtherAnimal.save();
    assertTrue(Animal.all().contains(myAnimal));
    assertTrue(Animal.all().contains(myOtherAnimal));
  }

  @Test
  public void sort_sortsByGivenColumns_true(){
    myAnimal.save();
    Animal myOtherAnimal = new Animal("Bobette", "2016-06-10", "F", "dog", "poodle");
    myOtherAnimal.save();
    assertEquals(myAnimal, Animal.all().get(0));
    assertEquals(myOtherAnimal, Animal.sort("name").get(0));
  }

  @Test
  public void getId_animalsInstantiateWithAnID(){
    myAnimal.save();
    assertTrue(myAnimal.getId() > 0);
  }

  @Test
  public void find_returnsAnimalWithSameId_otherAnimal(){
    myAnimal.save();
    Animal myOtherAnimal = new Animal("Bobette", "2016-06-10", "F", "dog", "poodle");
    myOtherAnimal.save();
    assertEquals(Animal.find(myOtherAnimal.getId()), myOtherAnimal);
  }

  @Test
  public void getTypes_returnsAnimalTypes_True(){
    myAnimal.save();
    Animal myOtherAnimal = new Animal("Bobette", "2016-06-10", "F", "dog", "poodle");
    myOtherAnimal.save();
    assertTrue(Animal.getTypes().contains("cat"));
    assertTrue(Animal.getTypes().contains("dog"));
  }

  @Test
  public void getTypes_doesNotSaveDuplicates_True(){
    myAnimal.save();
    Animal myOtherAnimal = new Animal("Bobette", "2016-06-10", "F", "cat", "poodle");
    myOtherAnimal.save();
    assertEquals(1, Animal.getTypes().size());
  }

  @Test
  public void getBreeds_returnsAnimalBreeds_True(){
    myAnimal.save();
    Animal myOtherAnimal = new Animal("Bobette", "2016-06-10", "F", "dog", "poodle");
    myOtherAnimal.save();
    assertTrue(Animal.getBreeds().contains("asshole"));
    assertTrue(Animal.getBreeds().contains("poodle"));
  }

  @Test
  public void getBreeds_doesNotSaveDuplicates_True(){
    myAnimal.save();
    Animal myOtherAnimal = new Animal("Bobette", "2016-06-10", "F", "cat", "asshole");
    myOtherAnimal.save();
    assertEquals(1, Animal.getBreeds().size());
  }
}
