package System.HPInterfaceFactory;

import State.StringCommand;

public class HPFactory {
    public HPInterface getHPInterface(String type) {
        if(type.isEmpty())
            throw new IllegalArgumentException();
        else if(type.equals("search"))
            return new HPSearch<StringCommand>();
        else if(type.equals("searchResult"))
            return new HPSearchResult<StringCommand>();
        else if(type.equals("default"))
            return new HPDismiss<StringCommand>();
        else if(type.equals("monitoring"))
            return new HPMonitoring<StringCommand>();

        return null;
    }
}
