package nn;

public enum Activation {

    TAHN, DTAHN, SIGMOID, DSIGMOID, RELU, DRELU, MUTATE;

    private Activation counterPart = null;

    private void getCounterPart() {
        switch (this) {
            case TAHN:
                counterPart = DTAHN;
                break;
            case DTAHN:
                counterPart = TAHN;
                break;
            case SIGMOID:
                counterPart = DSIGMOID;
                break;
            case DSIGMOID:
                counterPart = SIGMOID;
                break;
            case RELU:
                counterPart = DRELU;
                break;
            case DRELU:
                counterPart = RELU;
                break;
            case MUTATE:
                counterPart = MUTATE;
                break;
        }
    }

    public Activation getDerivative(){
        if(counterPart == null)
            getCounterPart();
        return counterPart;
    }
}
