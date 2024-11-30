package ord.techzonefun;

import static spark.Spark.*;
import com.google.gson.Gson;


public class SparkRestExample {
    public static void main(String[] args) {
        port(4567);
        final UserService userService = new UserServiceMapImpl();

        /*
        get("/your-route-path/", (request, response) -> {
                    your callback code
        });*/

        post("/users", (request, response) -> {
            response.type("application/json"); // tell the web that the data sending will be in Json format
            User user = new Gson().fromJson(request.body(), User.class); //transfer the data into a java object
            userService.addUser(user);

            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS));
            //format as StandardResponse and transfer into Json and send back to web
            //indicating its success
        });
        get("/users", (request, response) -> {
            response.type("application/json");
            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS,new Gson().toJsonTree(userService.getUsers())));
        });
        get("/users/:id", (request, response) -> {
            response.type("application/json");
            return new Gson().toJson(
                    new StandardResponse(StatusResponse.SUCCESS,new Gson().toJsonTree(userService.getUser(request.params(":id")))));
        });
        put("/users/:id", (request, response) -> {
            response.type("application/json");
            User toEdit = new Gson().fromJson(request.body(), User.class);
            User editedUser = userService.editUser(toEdit);

            if (editedUser != null) {
                return new Gson().toJson(
                        new StandardResponse(StatusResponse.SUCCESS,new Gson().toJsonTree(editedUser)));
            } else {
                return new Gson().toJson(
                        new StandardResponse(StatusResponse.ERROR,new Gson().toJson("User not found or error in edit")));
            }
        });
        delete("/users/:id", (request, response) -> {
            response.type("application/json");
            userService.deleteUser(request.params(":id"));
            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, "user deleted"));
        });
        options("/users/:id", (request, response) -> {
            response.type("application/json");
            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, (userService.userExist(request.params(":id"))) ? "User exists" : "User does not exists" ));
        });
    }
}