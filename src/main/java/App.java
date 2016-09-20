import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("animals", Animal.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String sortby = request.queryParams("sortby").toLowerCase();
      model.put("animals", Animal.sort(sortby));
      switch(sortby){
        case "type":
          model.put("sortby", Animal.getTypes());
          break;
        case "breed":
          model.put("sortby", Animal.getBreeds());
          break;
      }
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/owners", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("owners", Owner.all());
      model.put("template", "templates/owners.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/animals/new", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/animal-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/owners/new", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("types", Animal.getTypes());
      model.put("breeds", Animal.getBreeds());
      model.put("template", "templates/owner-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/animals", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name");
      String gender = request.queryParams("gender");
      String type = request.queryParams("type");
      String breed = request.queryParams("breed");
      String admittance = request.queryParams("admittance");
      Animal animal = new Animal(name, admittance, gender, type, breed);
      animal.save();
      model.put("success", animal);
      model.put("animals", Animal.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/owners", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name");
      String phone = request.queryParams("phone");
      String types = request.queryParams("types");
      String breeds = request.queryParams("breeds");
      Owner owner = new Owner(name, phone, types, breeds);
      owner.save();
      model.put("success", owner);
      model.put("owners", Owner.all());
      model.put("template", "templates/owners.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("animals/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params("id"));
      Animal animal = Animal.find(id);
      model.put("owners", Owner.all());
      model.put("animal", animal);
      model.put("template", "templates/animal.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("animals/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params("id"));
      int owner_id = Integer.parseInt(request.queryParams("owner_id"));
      Animal animal = Animal.find(id);
      animal.setOwnerId(owner_id);
      model.put("success", true);
      model.put("animal", animal);
      model.put("template", "templates/animal.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());
  }
}
