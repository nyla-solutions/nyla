package nyla.solutions.core.io.grep;

import java.io.File;

public record GrepResult(String results,File file) {
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GrepResult{");
        sb.append("results='").append(results).append('\'');
        sb.append(", file=").append(file);
        sb.append('}');
        return sb.toString();
    }
}
