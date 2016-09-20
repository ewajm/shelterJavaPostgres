import org.junit.*;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.sql2o.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OwnerTest{
  Owner myOwner;
  @Before
  public void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/shelter_test", null, null);
    myOwner = new Owner("Steve", "111-1111", "cat", "asshole");
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
  public void owner_instantiatesCorrectly(){
    assertTrue(myOwner instanceof Owner);
  }

  @Test
  public void getName_returnsName_String(){
    assertEquals("Steve", myOwner.getName());
  }

  @Test
  public void getPhone_returnsPhone_String(){
    assertEquals("111-1111", myOwner.getPhone());
  }

  @Test
  public void getTypePref_returnsTypePref_String(){
    assertEquals("cat", myOwner.getTypePref());
  }

  @Test
  public void getBreed_returnsBreed_String(){
    assertEquals("asshole", myOwner.getBreed());
  }

  @Test
  public void equals_returnsTrueIfFieldsAreTheSame(){
    Owner myOtherOwner = new Owner("Steve", "111-1111", "cat", "asshole");
    assertTrue(myOwner.equals(myOtherOwner));
  }

  @Test
  public void save_savesOwnerToDatabase(){
    myOwner.save();
    String sql = "SELECT * FROM owners WHERE name = 'Steve'";
    Owner fetchedOwner;
    try(Connection con = DB.sql2o.open()) {
      fetchedOwner = con.createQuery(sql).executeAndFetchFirst(Owner.class);
    }
    assertTrue(myOwner.equals(fetchedOwner));
  }

  @Test
  public void getId_ownersInstantiateWithAnID(){
    myOwner.save();
    assertTrue(myOwner.getId() > 0);
  }

  @Test
  public void getPets_returnsOwnedPets(){
    myOwner.save();
    Animal myOtherAnimal = new Animal("Steve", "2016-06-10", "M", "cat", "asshole");
    myOtherAnimal.save();
    myOtherAnimal.setOwnerId(myOwner.getId());
    Animal[] animals = new Animal[] { myOtherAnimal };
    assertTrue(myOwner.getPets().containsAll(Arrays.asList(animals)));
  }

  @Test
  public void all_returnsAllInstancesOfOwners_true(){
    myOwner.save();
    Owner myOtherOwner = new Owner("Bobette", "201-0610", "dog", "poodle");
    myOtherOwner.save();
    assertTrue(Owner.all().contains(myOwner));
    assertTrue(Owner.all().contains(myOtherOwner));
  }

  @Test
  public void find_returnsOwnerWithSameId_otherOwner(){
    myOwner.save();
    Owner myOtherOwner = new Owner("Bobette", "201-0610", "dog", "poodle");
    myOtherOwner.save();
    assertEquals(Owner.find(myOtherOwner.getId()), myOtherOwner);
  }
}
