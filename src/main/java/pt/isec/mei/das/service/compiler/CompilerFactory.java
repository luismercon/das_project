package pt.isec.mei.das.service.compiler;


public class CompilerFactory {
    public static Compiler getCompiler(String programmingLanguage) {
        if (programmingLanguage.equalsIgnoreCase("C")) {
            return new CCompiler();
        } else if (programmingLanguage.equalsIgnoreCase("C++")) {
            return new CppCompiler();
        } else {
            throw new IllegalArgumentException("Unsupported language");
        }
    }
}
