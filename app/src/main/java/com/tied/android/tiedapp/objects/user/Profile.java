package com.tied.android.tiedapp.objects.user;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Emmanuel on 5/28/2016.
 */
public class Profile implements Serializable{

    private String territories;
    private String sale_type;
    private String group_description;
    private String industries;
    private String rep_type;
    private ArrayList employers;

    public Profile() {
    }

    public Profile(String territories, String sale_type, String group_description, String industries, String rep_type, ArrayList employers) {
        this.territories = territories;
        this.sale_type = sale_type;
        this.group_description = group_description;
        this.industries = industries;
        this.rep_type = rep_type;
        this.employers = employers;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "territories='" + territories + '\'' +
                ", sale_type='" + sale_type + '\'' +
                ", group_description='" + group_description + '\'' +
                ", industries='" + industries + '\'' +
                ", rep_type='" + rep_type + '\'' +
                ", employers=" + employers +
                '}';
    }
}
