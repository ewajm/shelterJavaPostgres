import org.sql2o.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Animal{
  private String name;
  private Date admittance;
  private String gender;
  private String type;
  private String breed;
  private int id;
  private int owner_id;

  public Animal(String name, String admittance, String gender, String type, String breed){
    this.name = name;
    this.gender = gender;
    this.type = type;
    this.breed= breed;
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    try {
    this.admittance = formatter.parse(admittance);
  } catch (Exception e){
      System.out.print("whoops");
    }
    this.owner_id = -1;
  }

  public String getName(){
    return name;
  }

  public Date getAdmittance(){
    return admittance;
  }

  public String getGender(){
    return gender;
  }

  public String getType(){
    return type;
  }

  public String getBreed(){
    return breed;
  }

  public int getId(){
    return id;
  }

  public int getOwnerId(){
    return owner_id;
  }

  public void setOwnerId(int owner_id){
    this.owner_id = owner_id;
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE animals SET owner_id = :owner_id WHERE id = :id";
      con.createQuery(sql)
        .addParameter("owner_id", owner_id)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  @Override
  public boolean equals(Object otherAnimal) {
    if(!(otherAnimal instanceof Animal)){
      return false;
    } else {
      Animal newAnimal = (Animal) otherAnimal;
      return this.name.equals(newAnimal.name) && this.admittance.equals(newAnimal.admittance) &&
        this.gender.equals(newAnimal.gender) && this.type.equals(newAnimal.type) && this.getBreed().equals(newAnimal.getBreed()) && this.id == newAnimal.id;
    }
  }

  public void save(){
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO animals(name, admittance, gender, type, breed, owner_id) VALUES (:name, :admittance, :gender, :type, :breed, :owner_id)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .addParameter("admittance", this.admittance)
        .addParameter("gender", this.gender)
        .addParameter("type", this.type)
        .addParameter("breed", this.breed)
        .addParameter("owner_id", this.owner_id)
        .executeUpdate()
        .getKey();
    }
  }

  public static List<Animal> all(){
    String sql="SELECT * FROM animals";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Animal.class);
    }
  }

  public static List<Animal> sort(String sortby){
    String sql="SELECT * FROM animals ORDER BY :sortby";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).addParameter("sortby", sortby).executeAndFetch(Animal.class);
    }
  }

  public static Set<String> getTypes(){
    Set<String> typeSet = new TreeSet<String>();
    for(Animal animal: Animal.all()){
      typeSet.add(animal.type);
    }
    return typeSet;
  }

  public static Set<String> getBreeds(){
    Set<String> breedSet = new TreeSet<String>();
    for(Animal animal: Animal.all()){
      breedSet.add(animal.breed);
    }
    return breedSet;
  }

  public static Animal find(int id){
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT * FROM animals WHERE id=:id";
      Animal animal = con.createQuery(sql)
      .addParameter("id", id)
      .executeAndFetchFirst(Animal.class);
      return animal;
    }
  }
}
