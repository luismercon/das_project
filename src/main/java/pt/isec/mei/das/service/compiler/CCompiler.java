package pt.isec.mei.das.service.compiler;

import java.io.File;
import java.io.IOException;

public class CCompiler implements Compiler {
    @Override
    public Process compile(String sourceFilePath, String executablePath, File outputDirectory) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("gcc", sourceFilePath, "-o", executablePath);
        processBuilder.redirectErrorStream(true);
        processBuilder.directory(outputDirectory);
        return processBuilder.start();
    }
}
