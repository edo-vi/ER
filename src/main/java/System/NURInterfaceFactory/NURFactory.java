package System.NURInterfaceFactory;

public class NURFactory {
    public NURInterface getInterface(String type) {
        if(type.isEmpty())
            throw new IllegalArgumentException();
        else if(type.equals("search"))
            return new NURSearch();
        else if(type.equals("searchResult"))
            return new NURSearchResult();
        else if(type.equals("default"))
            return new NURDefault();
        else if(type.equals("monitoring"))
            return new NURMonitoring();
        else if(type.equals("addPatient"))
            return new NURAddPatient();

        return null;
    }
}
