package basic;

public class Graphdata{
    private String country;
    private String continent;
    private String countryCode;
    private int year;
    private int objectCount;

    public Graphdata(String countryName, String theCode, String continentName, int theYear, int theObjectCount){
        country = countryName;
        countryCode = theCode;
        continent = continentName;
        year = theYear;
        objectCount = theObjectCount;
    }

    public String getCountry(){
        return country;
    }

    public String getCode(){
        return countryCode;
    }
    
    public String getContinent(){
        return continent;
    }

    public int getYear (){
        return year;
    }

    public int getObjects (){
        return objectCount;
    }

}
