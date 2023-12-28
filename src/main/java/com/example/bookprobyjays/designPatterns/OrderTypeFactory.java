package com.example.bookprobyjays.designPatterns;

import com.example.bookprobyjays.designPatterns.impl.DealOrder;
import com.example.bookprobyjays.designPatterns.impl.RejectOrder;

public class OrderTypeFactory {
    public static OrderTypeService newInstance(String type){
        switch (type){
            case "reject":
                return new RejectOrder();
            case "deal":
                return new DealOrder();
            default:
                return new RejectOrder();
        }
    }
}
