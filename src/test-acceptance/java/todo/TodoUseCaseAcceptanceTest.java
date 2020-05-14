package todo;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test-acceptance/resources/todo",
        glue = "todo",
        plugin = {"pretty", "html:build/reports/tests/test-acceptance", "json:build/reports/tests/test-acceptance/results.json"},
        strict = true
)
public class TodoUseCaseAcceptanceTest {
}
