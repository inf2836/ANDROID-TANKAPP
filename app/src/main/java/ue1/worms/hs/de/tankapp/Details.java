package ue1.worms.hs.de.tankapp;

public class Details {
    String name ;
    String diesel;
    String e_5;
    String e_10;

    public Details(String name, String diesel, String e_5,String e_10){
        this.name=name;
        this.diesel=diesel;
        this.e_5=e_5;
        this.e_10=e_10;
    }

    public Details(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiesel() {
        return diesel;
    }

    public void setDiesel(String diesel) {
        this.diesel = diesel;
    }

    public String getE_5() {
        return e_5;
    }

    public void setE_5(String e_5) {
        this.e_5 = e_5;
    }

    public String getE_10() {
        return e_10;
    }

    public void setE_10(String e_10) {
        this.e_10 = e_10;
    }
}
