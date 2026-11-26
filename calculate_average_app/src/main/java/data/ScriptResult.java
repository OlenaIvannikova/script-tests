package data;

public class ScriptResult {

    private int exitCode;
    private String output;
    private long executionTimeMillis;

    public ScriptResult(int exitCode, String output, long executionTimeMillis) {
        this.exitCode = exitCode;
        this.output = output;
        this.executionTimeMillis = executionTimeMillis;
    }

    public boolean isSuccess() {
        return exitCode == 0;
    }

    public String getOutput() {
        return output;
    }

    public int getExitCode() {
        return exitCode;
    }

    public long getExecutionTimeMillis() {
        return executionTimeMillis;
    }
}
