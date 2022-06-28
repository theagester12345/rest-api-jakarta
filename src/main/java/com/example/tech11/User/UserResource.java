package com.example.tech11.User;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.text.ParseException;
import java.util.List;

@Path("/user")
public class UserResource {
    @Inject
    private UserService userService;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getAllUsers() throws ParseException, ClassNotFoundException {


        return userService.getAll();
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public User getUser(@PathParam("id") Integer userId) throws ClassNotFoundException, ParseException {
        return userService.getUser(userId);
    }


    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response handleUserRegisterRequest (User user) throws ClassNotFoundException, NoSuchAlgorithmException {
        return userService.createUser(user);
    }


    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{id}")
    public Response updateUser(@PathParam("id") Integer userId,
                               @QueryParam("firstname")String firstname,
                               @QueryParam("lastname") String lastname,
                               @QueryParam("email") String email,
                               @QueryParam("birthday") Date birthday,
                               @QueryParam("password") String password) throws ClassNotFoundException, ParseException {
        return userService.updateUser(userId,firstname,lastname,email,birthday,password);
    }
}
