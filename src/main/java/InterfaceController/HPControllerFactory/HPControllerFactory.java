package InterfaceController.HPControllerFactory;

import System.Sistema;

public class HPControllerFactory {
    private Sistema sys = Sistema.getInstance();

    public HPController getController(String type) {
        if(type.isEmpty())
            throw new IllegalArgumentException();
        else if(type.equals("search"))
            return new HPSController(sys.getStore(), sys.getStore().getEventStream());
        else if(type.equals("searchResult"))
            return new HPSRController(sys.getStore(), sys.getStore().getEventStream());
        else if(type.equals("default"))
            return new HPDController(sys.getStore(), sys.getStore().getEventStream());
        else if(type.equals("monitoring"))
            return new HPMController(sys.getStore(), sys.getStore().getEventStream());

        return null;
    }
}
