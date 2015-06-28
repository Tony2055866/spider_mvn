package com.dao;

import org.hibernate.dialect.MySQL5Dialect;

import java.sql.Types;


public class MyDialect extends MySQL5Dialect {
	public MyDialect() {  
        super();  
        registerHibernateType(Types.LONGNVARCHAR, 65535, "text");  
    }  
}
