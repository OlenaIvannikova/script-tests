package data;

public class ScriptResult {
    private int exitCode;
    private String output;

    public ScriptResult(int exitCode, String output) {
        this.exitCode = exitCode;
        this.output = output;
    }

    public String getOutput() {
        return output;
    }

    public int getExitCode() {
        return exitCode;
    }
}
