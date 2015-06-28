package com.dao;

import java.sql.Types;

import org.hibernate.dialect.MySQL5Dialect;


public class MyDialect extends MySQL5Dialect {
	public MyDialect() {  
        super();  
        registerHibernateType(Types.LONGNVARCHAR, 65535, "text");  
    }  
}
