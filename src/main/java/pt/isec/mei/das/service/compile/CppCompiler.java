package pt.isec.mei.das.service.compile;

import java.io.File;
import java.io.IOException;

public class CppCompiler implements Compiler {
    @Override
    public Process compile(String sourceFilePath, String executablePath, File outputDirectory) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("g++", sourceFilePath, "-o", executablePath);
        processBuilder.redirectErrorStream(true);
        processBuilder.directory(outputDirectory);
        return processBuilder.start();
    }
}
