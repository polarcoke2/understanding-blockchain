
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contact;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import com.owlike.genson.Genson;
import org.json.JSONObject;

@Contract(
        name = "FabCounter",
        info = @Info(
                title = "FabCounter contract",
                description = "The hyperlegendary counter contract",
                version = "0.0.1-SNAPSHOT",
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"),
                contact = @Contact(
                        email = "f.counter@example.com",
                        name = "F counter",
                        url = "https://hyperledger.example.com")))
@Default
public class FabCounter implements ContractInterface{

    private final Genson genson = new Genson();

    private enum FabCounterErrors {
        COUNTER_NOT_FOUND,
        COUNTER_ALREADY_EXISTS,
        COUNTER_TYPE_NOT_SUPPORTIVE
    }

    @Transaction()
    public void createCounter(final Context ctx, final String name, final String type, final int number) {
        ChaincodeStub stub = ctx.getStub();

        String counterState = stub.getStringState(name);
        if (!counterState.isEmpty()) {
            String errorMessage = String.format("src.main.java.Counter %s already exists", name);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, FabCounterErrors.COUNTER_ALREADY_EXISTS.toString());
        }

        if (!type.equals("up") && !type.equals("down")) {
            String errorMessage = String.format("Type of src.main.java.Counter %s is %s", name, type);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, FabCounterErrors.COUNTER_TYPE_NOT_SUPPORTIVE.toString());
        }
        Counter counter = new Counter(name, type, number);
        counterState = genson.serialize(counter);
        stub.putStringState(name, counterState);
    }

    @Transaction()
    public void updateCounter(final Context ctx, final String name) {
        ChaincodeStub stub = ctx.getStub();

        String counterState = stub.getStringState(name);
        if (counterState.isEmpty()) {
            String errorMessage = String.format("src.main.java.Counter %s does not exist", name);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, FabCounterErrors.COUNTER_NOT_FOUND.toString());
        }

        Counter counter = genson.deserialize(counterState, Counter.class);
        int newNumber;
        if (counter.getType().equals("up")) {
            newNumber = counter.getNumber() + 1;
        } else if (counter.getType().equals("down")) {
            newNumber = counter.getNumber() - 1;
        } else {
            String errorMessage = String.format("Type of src.main.java.Counter %s is %s", name, counter.getType());
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, FabCounterErrors.COUNTER_TYPE_NOT_SUPPORTIVE.toString());
        }
        Counter newCar = new Counter(counter.getName(), counter.getType(), newNumber);
        String newCarState = genson.serialize(newCar);
        stub.putStringState(name, newCarState);
    }

    @Transaction()
    public String readCounter(final Context ctx, final String name) {
        ChaincodeStub stub = ctx.getStub();

        String counterState = stub.getStringState(name);
        if (counterState.isEmpty()) {
            String errorMessage = String.format("src.main.java.Counter %s does not exist", name);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, FabCounterErrors.COUNTER_NOT_FOUND.toString());
        }

        Counter counter = genson.deserialize(counterState, Counter.class);
        //JSONObject messageInJson = new JSONObject();
        //messageInJson.put("name", counter.getName());
        //messageInJson.put("value", counter.getNumber());

        //{ “name”: “cntr1”, “value”: 1 }
        return String.format("{ \"name\": %s, \"value\": %d }", counter.getName(), counter.getNumber());
    }
}
