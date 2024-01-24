package pt.isec.mei.das.service.compile;

import java.io.File;
import java.io.IOException;

public interface Compiler {
    Process compile(String sourceFilePath, String executablePath, File outputDirectory) throws IOException, InterruptedException;
}
