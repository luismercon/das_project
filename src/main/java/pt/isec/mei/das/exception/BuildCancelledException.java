package pt.isec.mei.das.exception;


public class BuildCancelledException extends RuntimeException {
  public BuildCancelledException(String message) {
    super(message);
  }
}
