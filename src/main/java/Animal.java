import org.sql2o.*;
import java.time.LocalDateTime;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;
import java.util.Date;

public class Animal{
  private String name;
  private Date admittance;
  private String gender;
  private String type;
  private String breed;
  private int id;

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

  @Override
  public boolean equals(Object otherAnimal) {
    if(!(otherAnimal instanceof Animal)){
      return false;
    } else {
      Animal newAnimal = (Animal) otherAnimal;
      return this.name.equals(newAnimal.name) && this.admittance.equals(newAnimal.admittance) &&
        this.gender.equals(newAnimal.gender) && this.type.equals(newAnimal.type) && this.getBreed().equals(newAnimal.getBreed());
    }
  }

  public void save(){
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO animals(name, admittance, gender, type, breed) VALUES (:name, :admittance, :gender, :type, :breed)";
      con.createQuery(sql)
        .addParameter("name", this.name)
        .addParameter("admittance", this.admittance)
        .addParameter("gender", this.gender)
        .addParameter("type", this.type)
        .addParameter("breed", this.breed)
        .executeUpdate();
    }
  }

  public static List<Animal> all(){
    String sql="SELECT * FROM animals";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Animal.class);
    }
  }
}
