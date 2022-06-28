package com.example.tech11.User;

import com.example.tech11.H2DBUTIL;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Transactional
public class UserService {
    private static final String INSERT_USERS_SQL = "INSERT INTO users" +
            " (firstname,lastname, email, birthday, passwordHash) VALUES " +
            " ( ?,?, ?, ?, ?);";

    private static final String SELECT_ALL_USER_SQL = "SELECT * FROM users";

    private static final String GET_USER_SQL = "SELECT * FROM USERS WHERE id=?";

    private static final String UPDATE_USER_SQL = "UPDATE USERS SET firstname=?,lastname=?,email=?,birthday=?,passwordHash=? where id=?";




    public Response createUser(User user) throws ClassNotFoundException, NoSuchAlgorithmException {

        boolean is_inserted = false;
                try (Connection connection = H2DBUTIL.getConnection();
                     PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)){

                    preparedStatement.setString(1, user.getFirstname());
                    preparedStatement.setString(2,user.getLastname());
                    preparedStatement.setString(3, user.getEmail());
                    preparedStatement.setDate(4,  user.getBirthday());
                    preparedStatement.setString(5, generatePasswordHash(user.getPasswordHash()));

                    System.out.println(preparedStatement);
                    // Step 3: Execute the query or update query
                   is_inserted = preparedStatement.executeUpdate() > 0;






        }catch (SQLException e){
            H2DBUTIL.printSQLException(e);
        }




        return is_inserted ? Response.ok().build() : Response.notAcceptable(null).build();

    }


    public List<User> getAll() throws ParseException, ClassNotFoundException {
        List<User> final_user_list = new ArrayList<>();
        try (Connection connection = H2DBUTIL.getConnection()){



            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_ALL_USER_SQL);

            System.out.println(resultSet);

            while (resultSet.next()){
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setFirstname(resultSet.getString("firstname"));
                user.setLastname(resultSet.getString("lastname"));
                user.setEmail(resultSet.getString("email"));
                user.setBirthday(resultSet.getDate("birthday"));
                user.setPasswordHash(resultSet.getString("passwordHash"));

                final_user_list.add(user);
            }

          statement.close();
            resultSet.close();



        }catch (SQLException e){
            H2DBUTIL.printSQLException(e);
        }
      return final_user_list;
    }


    public User getUser(Integer userId) throws ClassNotFoundException, ParseException {
        User user  = new User();
        try (Connection connection = H2DBUTIL.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_SQL)){

            System.out.println("User id:"+userId);
            preparedStatement.setInt(1,userId);
            ResultSet resultSet = preparedStatement.executeQuery();



            while (resultSet.next()){

                user.setFirstname(resultSet.getString("firstname"));
                user.setLastname(resultSet.getString("lastname"));
                user.setEmail(resultSet.getString("email"));
                user.setBirthday(resultSet.getDate("birthday"));
                user.setPasswordHash(resultSet.getString("passwordHash"));


            }

            preparedStatement.close();
            resultSet.close();



        }catch (SQLException e){

            H2DBUTIL.printSQLException(e);
        }
        return user;

    }


    public Response updateUser(Integer userId, String firstname, String lastname, String email, Date birthday, String password) throws ClassNotFoundException, ParseException {
        User user = new User();
        boolean is_updated = false;
        try (Connection connection = H2DBUTIL.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_SQL)){

//            System.out.println("User id:"+userId);
            preparedStatement.setInt(1,userId);
            ResultSet resultSet = preparedStatement.executeQuery();



            while (resultSet.next()){
                user.setFirstname(resultSet.getString("firstname"));
                user.setLastname(resultSet.getString("lastname"));
                user.setEmail(resultSet.getString("email"));
                user.setBirthday(resultSet.getDate("birthday"));
                user.setPasswordHash(resultSet.getString("passwordHash"));


            }

            preparedStatement.close();
            resultSet.close();



            //Run updates

            if (firstname!=null){

                user.setFirstname(firstname);
            }

            if (lastname!=null){
                user.setLastname(lastname);
            }

            if(email!=null){
                user.setEmail(email);
            }

            if(birthday!=null){
                user.setBirthday(birthday);
            }

            if(password!=null){
                user.setPasswordHash(password);
            }


            //Run Update SQL
            PreparedStatement updateStatement = connection.prepareStatement(UPDATE_USER_SQL);
            updateStatement.setString(1,user.getFirstname());
            updateStatement.setString(2,user.getLastname());
            updateStatement.setString(3,user.getEmail());
            updateStatement.setDate(4,user.getBirthday());
            updateStatement.setString(5,user.getPasswordHash());
            updateStatement.setInt(6,userId);

            is_updated=updateStatement.executeUpdate()>0;

            updateStatement.close();



        }catch (SQLException e){

            H2DBUTIL.printSQLException(e);
        }
        return is_updated ? Response.ok().build(): Response.notAcceptable(null).build();
    }




    private String generatePasswordHash(String password) throws NoSuchAlgorithmException {

        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(password.getBytes());
        byte [] hashResult = messageDigest.digest();

        StringBuilder hash = new StringBuilder();

        for (byte b : hashResult){
            hash.append(String.format("%02x",b));

        }

        return hash.toString();

    }
}
