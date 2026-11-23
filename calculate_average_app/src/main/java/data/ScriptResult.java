package data;

public class ScriptResult {

    private int exitCode;
    private String output;

    public ScriptResult(int exitCode, String output) {
        this.exitCode = exitCode;
        this.output = output;
    }

    public boolean isSuccess() {
        return exitCode == 0;
    }

    public String getOutput() {
        return output;
    }
}
