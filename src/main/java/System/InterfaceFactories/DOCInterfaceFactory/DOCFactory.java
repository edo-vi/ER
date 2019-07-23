package System.InterfaceFactories.DOCInterfaceFactory;

public class DOCFactory {
    public DOCInterface getInterface(String type) {
        if(type.isEmpty())
            throw new IllegalArgumentException();
        else if(type.equals("search"))
            return new DOCSearch();
        else if(type.equals("searchResult"))
            return new DOCSearchResult();
        else if(type.equals("default"))
            return new DOCDefault();
        else if(type.equals("monitoring"))
            return new DOCMonitoring();
        else if(type.equals("addPrescription"))
            return new DOCAddPrescription();

        return null;
    }
}
