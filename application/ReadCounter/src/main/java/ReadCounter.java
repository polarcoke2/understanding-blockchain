
import java.nio.file.Path;
import java.nio.file.Paths;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;

public class CreateCounter {

    public static void main(String args[]) throws Exception {

        Path walletPath = Paths.get("wallet");
        Wallet wallet = Wallet.newFileSystemWallet(walletPath);
        // load a CCP
        Path networkConfigPath = Paths.get("..", "test-network", "organizations", "peerOrganizations", "org2.example.com", "connection-org2.json");

        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, "appUser").networkConfig(networkConfigPath).discovery(true);

        // create a gateway connection
        try (Gateway gateway = builder.connect()) {

            // get the network and contract
            Network network = gateway.getNetwork("mychannel");
            Contract contract = network.getContract("FabCounter");

            byte[] result;

            if (args.length != 1) {
                String errorMessage = String.format("The argument length is wrong. It should have one arguments," +
                        "but is given %d!", args.length);
                System.out.println(errorMessage);
                Throw new Exception(errorMessage);
            }
            //contract.submitTransaction("readCounter", args[0]);

            result = contract.evaluateTransaction("readCounter", args[0]);
            System.out.println(new String(result));
        }
    }
}