
import java.util.Objects;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import com.owlike.genson.annotation.JsonProperty;

@DataType()
public final class Counter {

    @Property()
    private final String name;

    @Property()
    private final String type;

    @Property()
    private int number;

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getNumber() {
        return number;
    }

    public Counter(@JsonProperty("name") final String name,
                   @JsonProperty("type") final String type,
                   @JsonProperty("number") final int number) {
        this.name = name;
        this.type = type;
        this.number = number;
    }

    @Override
    public boolean equals(final Object obj) {
        if(this == obj) {
            return true;
        }

        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }

        Counter other = (Counter) obj;

        return Objects.deepEquals(new String[] {getName(), getType(), String.valueOf(getNumber())},
                new String[] {other.getName(), other.getType(), String.valueOf(other.getNumber())});
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getType());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode())
                + "[name: " + name + " type: " + type + " number: " + number + "]";
    }
}