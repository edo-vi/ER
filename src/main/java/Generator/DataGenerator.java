package Generator;
import Entities.Recovery;
import System.Sistema;

public class DataGenerator {
    public static void main(String... args) {
        DataThread data = new DataThread(Sistema.getInstance().getStore());
        data.start();
    }
}