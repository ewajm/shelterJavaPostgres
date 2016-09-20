import org.sql2o.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class Owner{
  private String name;
  private String phone;
  private String type_pref;
  private String breed;
  private int id;

  public Owner(String name, String phone, String type_pref, String breed){
    this.name = name;
    this.phone = phone;
    this.type_pref = type_pref;
    this.breed = breed;
  }

  public String getName(){
    return name;
  }

  public String getPhone(){
    return phone;
  }

  public String getTypePref(){
    return type_pref;
  }

  public String getBreed(){
    return breed;
  }

  public int getId(){
    return id;
  }

  @Override
  public boolean equals(Object otherOwner) {
    if(!(otherOwner instanceof Owner)){
      return false;
    } else {
      Owner newOwner = (Owner) otherOwner;
      return this.name.equals(newOwner.name) && this.phone.equals(newOwner.phone) &&
        this.type_pref.equals(newOwner.type_pref) && this.breed.equals(newOwner.breed) && this.id==newOwner.id;
    }
  }

  public void save(){
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO owners(name, phone, type_pref, breed) VALUES (:name, :phone, :type_pref, :breed)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .addParameter("phone", this.phone)
        .addParameter("type_pref", this.type_pref)
        .addParameter("breed", this.breed)
        .executeUpdate()
        .getKey();
    }
  }

  public List<Animal> getPets(){
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM animals WHERE owner_id =:id";
      return con.createQuery(sql)
      .addParameter("id", this.id)
      .executeAndFetch(Animal.class);
    }
  }

  public static List<Owner> all(){
    String sql="SELECT * FROM owners";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Owner.class);
    }
  }

  public static Owner find(int id){
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT * FROM owners WHERE id=:id";
      Owner owner = con.createQuery(sql)
      .addParameter("id", id)
      .executeAndFetchFirst(Owner.class);
      return owner;
    }
  }

}
