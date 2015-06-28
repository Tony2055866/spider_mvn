package com.model;



/**
 * Test entity. @author MyEclipse Persistence Tools
 */

public class Test  implements java.io.Serializable {


    // Fields    

     private Integer id;
     private String test;


    // Constructors

    /** default constructor */
    public Test() {
    }

    
    /** full constructor */
    public Test(String test) {
        this.test = test;
    }

   
    // Property accessors

    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public String getTest() {
        return this.test;
    }
    
    public void setTest(String test) {
        this.test = test;
    }
   








}