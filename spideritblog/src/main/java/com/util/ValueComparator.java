package com.util;



import java.util.Comparator;
import java.util.Map;

import com.model.WpTermTaxonomy;


public class ValueComparator implements Comparator<Map.Entry<WpTermTaxonomy, Integer>>  
{  
    public int compare(Map.Entry<WpTermTaxonomy, Integer> mp1, Map.Entry<WpTermTaxonomy, Integer> mp2)   
    {
        return mp2.getValue() - mp1.getValue();  
    }
}
