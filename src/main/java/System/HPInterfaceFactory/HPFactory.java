package System.HPInterfaceFactory;

import State.StringCommand;

public class HPFactory {
    public HPInterface getHPInterface(String type) {
        if(type.isEmpty())
            throw new IllegalArgumentException();
        else if(type.equals("search"))
            return new HPSearch();
        else if(type.equals("searchResult"))
            return new HPSearchResult();
        else if(type.equals("default"))
            return new HPDismiss();
        else if(type.equals("monitoring"))
            return new HPMonitoring();

        return null;
    }
}
