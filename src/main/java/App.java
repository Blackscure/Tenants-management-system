import dao.Sql2oApartmentDao;
import dao.Sql2oIssuesDao;
import dao.Sql2oTenantsDao;
import dao.Sql2oUserDao;
import models.*;
import org.sql2o.Connection;
import org.sql2o.*;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class App {
    public  static void main(String[] args) {
        ProcessBuilder process = new ProcessBuilder();
        Integer port;

        if (process.environment().get("PORT") != null) {
            port = Integer.parseInt(process.environment().get("PORT"));
        } else {
            port = 4567;
        }
        port(port);
        staticFileLocation("/public");
        Connection conn;
        Sql2oUserDao userDao;
        Sql2oApartmentDao apartmentDao;
        Sql2oIssuesDao issuesDao;
        Sql2oTenantsDao tenantsDao;
        String connectionString = "jdbc:postgresql://localhost:5432/tenants_manager";
        Sql2o sql2o = new Sql2o(connectionString, "postgres", "1234");
        userDao=new Sql2oUserDao(sql2o);
        apartmentDao=new Sql2oApartmentDao(sql2o);
        issuesDao=new Sql2oIssuesDao(sql2o);
        tenantsDao=new Sql2oTenantsDao(sql2o);
        conn=sql2o.open();

        get("/", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());

        post("/users", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            String name = request.queryParams("name");
            String email = request.queryParams("email");
            String username = request.queryParams("username");
            String password = request.queryParams("password");
            User user = new User(name,email,username,password);
            userDao.save(user);
            return new ModelAndView(model, "");
        }, new HandlebarsTemplateEngine());

        get("/users", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            List<User> user = userDao.getAll();
            model.put("user",user);
            return new ModelAndView(model, "");
        }, new HandlebarsTemplateEngine());

        get("/users/:id",(request, response) ->
        {
            Map<String,Object>model=new HashMap<String, Object>();
            int id = Integer.parseInt(request.params("id"));
            User found=userDao.findById(id);
            model.put("found",found);
            return new ModelAndView(model,"");
        },new HandlebarsTemplateEngine());





    }
}
