package nn;

import java.io.*;

public abstract class NeuralNetwork implements Serializable {

    public final Activation Activation;
    public final Activation derivative;
    public double learnRate;
    public int inodes;
    public int onodes;
    public Matrix wih;
    public Matrix who;

    public NeuralNetwork(Activation Activation, Activation derivative) {
        this.Activation = Activation;
        this.derivative = derivative;
    }

    public abstract double[] query(double[] inputs_array);

    public abstract void train(double[] inputs_array, double[] target);

    public abstract void mutate();

    public static void saveNeuralNetwork(NeuralNetwork data, String name, String location) {
        try {
            File dir = new File(System.getProperty("user.dir") + "/" + location);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(System.getProperty("user.dir") + "/" + location + "/" + name + ".NN");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOut =
                    new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(data);
            out.flush();
            out.close();
            fileOut.flush();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }

    }

    public static NeuralNetwork loadNeuralNetwork(String name, String location) {
        NeuralNetwork loading = null;
        try {
            File file = new File(System.getProperty("user.dir") + "/" + location + "/" + name + ".NN");
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            loading = (NeuralNetwork) in.readObject();
            in.close();
            fileIn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loading;
    }

}
