package pt.isec.mei.das.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BuildResultDecoratedWithTimeInSeconds extends AbstractBuildResultDecorator {

  public BuildResultDecoratedWithTimeInSeconds(BuildResultDecorator decoratedBuildResult) {
    super(decoratedBuildResult);
  }

  public String getCompilationTimeSeconds() {

    BigDecimal timeInSeconds =
        BigDecimal.valueOf(decoratedBuildResult.getCompilationTimeInMs())
            .divide(BigDecimal.valueOf(1000), 3, RoundingMode.CEILING);

    return "COMPILATION TIME IN SECONDS: " + timeInSeconds;
  }
}
