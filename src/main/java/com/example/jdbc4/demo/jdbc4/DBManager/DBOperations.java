package com.example.jdbc4.demo.jdbc4.DBManager;

import com.example.jdbc4.demo.jdbc4.DAOs.Person;
import com.example.jdbc4.demo.jdbc4.Requests.CreateRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.sql.*;


import java.util.ArrayList;
import java.util.List;

public class DBOperations {

    public static volatile Connection connection;

    public static Connection getConnection() throws SQLException {

        if (connection == null) {

            synchronized (DBOperations.class) {

                if (connection == null) {
                    connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/person_4", "root", "root");
                    System.out.println("Connected to DB");
                }
            }
        }

        return connection;
    }


    // Singleton pattern : In this you are only initialising Data base
    // connection once the no of times get connected only one no need to connect again
    // other threads dont need to wait

    public static void closeConnection() throws SQLException{
        if(connection!=null){

            synchronized (DBOperations.class){
                if(connection!=null){
                    connection=null;

                }
            }
        }
    }

  public static void createTable(String name) throws SQLException {

        getConnection();

        Statement statement=connection.createStatement();
        boolean isCreated= statement.execute("CREATE TABLE "+name+" (id INT PRIMARY KEY AUTO_INCREMENT,name VARCHAR(20),age INT,address VARCHAR(50))");
        if(isCreated){
            System.out.println("table"+name+"is sucessfully created");
        }

        closeConnection();
    }


  public static void insertPerson(CreateRequest request) throws SQLException {
     getConnection();
        //PrepareStatement
      PreparedStatement preparedStatement=connection.prepareStatement("INSERT INTO person VALUES (null,?,?,?)");
      // Here the Question marks meaning is Dynamic Initalisation

      preparedStatement.setString(1,request.getName());
      preparedStatement.setInt(2,request.getAge());
      preparedStatement.setString(3,request.getAddress());

      int row_affected=preparedStatement.executeUpdate();

      if(row_affected>0){
          System.out.println("Sucessfully Inserted the record");
      }else{
          System.out.println("unable to insert the record");
      }
      closeConnection();


//Earlier when you have write the SQL queries then it was compiled by the Java Program
      // but now you are using the PrepareStatement it does not compile the Statement it
      // send it to the MYSql server
      // MySql server check the statment into the local Cache where this type of statment is present
      // or not if this type of statement exist it will only replace the values here it will not compile
      // the whole query
      // and if not present the Statement means it create the statement for the first time

      // so you can say the runtime/latency of your result will be very very less compared the normal statement



//      Here what are we doing we are creating the empty statement and we are excuting that
//        Person person=new Person(request.getName(),request.getAge(),request.getAddress());
//        Statement statement=connection.createStatement();
//        int row_affected=statement.executeUpdate("INSERT INTO person VALUES ()")
  }

    public static List<Person> getPersons() throws SQLException {

        // Getting the Persons from DB
        getConnection();

        Statement statement= connection.createStatement();

        ResultSet resultSet=statement.executeQuery("SELECT * FROM person");
        List<Person> persons=new ArrayList<>();


        while(resultSet.next()){

//            resultSet.next();
            String name=resultSet.getString(2);
            int age=resultSet.getInt(3);
            String address=resultSet.getString(4);

            Person person=new Person(name,age,address);

            System.out.println(person);

            persons.add(person);
        }

       closeConnection();

        return persons;

    }

}
