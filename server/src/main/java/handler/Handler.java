package handler;
import com.google.gson.Gson;
import dataClasses.AuthData;
import dataaccess.UserDAO;
import io.javalin.http.Context;
import service.Register;

public class Handler {
    public final Register register;

    public Handler(UserDAO userDAO) {
        this.register = new Register(userDAO);
    }

    public void handleRegister(Context ctx) {
        RegisterRequest registerRequest = new Gson().fromJson(ctx.body(), RegisterRequest.class);
        AuthData registerResult = register.register(registerRequest);
        ctx.result(new Gson().toJson(registerResult));
        ctx.status(200);
    }

}
