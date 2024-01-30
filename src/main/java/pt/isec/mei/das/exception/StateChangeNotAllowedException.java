package pt.isec.mei.das.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class StateChangeNotAllowedException extends RuntimeException {
  public StateChangeNotAllowedException(String message) {
    super(message);
  }
}
