package com.example.jdbc4.demo.jdbc4.controllers;

import com.example.jdbc4.demo.jdbc4.DAOs.Person;
import com.example.jdbc4.demo.jdbc4.DBManager.DBOperations;
import com.example.jdbc4.demo.jdbc4.Requests.CreateRequest;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
public class PersonController {

    @GetMapping(value="/getPersons")
    public List<Person> getPersons() throws SQLException {

        // Calling DAO  to get object from DB
       return DBOperations.getPersons();


    }

//    @GetMapping("/getPerson")
//    @RequestMapping(value="/getPersons",method = RequestMethod.GET)
//    Both Are the Same Meaning

    @RequestMapping(value = "/createTable",method=RequestMethod.POST)
    public void createTable(@RequestParam(value="name") String name) throws SQLException{
        DBOperations.createTable(name);
    }

    @RequestMapping(value="/insertPerson",method = RequestMethod.POST)
    public  void insertPerson(@RequestBody CreateRequest request) throws SQLException {
             DBOperations.insertPerson(request);
    }

    // we can use person object here if you use different pojo then it make you clean and
    // let say your project becomes very big here now if you use person object here
    // and you are doing operation here then your debugging process take alot of time
    // becoz you are directly playing with the DB object .

    // you Should use the Db object for the transcation from the Database
    // if some issue comes where you are setting property for some other object
    // then it is difficult to debug from where this object can be manipulated

    // If you are Doing this in DAO layer then it will not any issue




}
