package todo;

import archtest.PackageType;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.junit.ArchUnitRunner;
import com.tngtech.archunit.lang.ArchRule;
import common.architecture.UseCase;
import org.junit.runner.RunWith;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@RunWith(ArchUnitRunner.class)
@AnalyzeClasses(packages = {"todo"})
public class DddApplicationTest {

    @ArchTest
    static final ArchRule useCasesMustBeAnnotatedWithUseCase =
            classes()
                    .that().resideInAnyPackage(PackageType.APPLICATION_SERVICE.recursive())
                    .should().beAnnotatedWith(UseCase.class);

    @ArchTest
    static final ArchRule applicationServiceMustImplementUseCases =
            classes()
                    .that().resideInAnyPackage(PackageType.DOMAIN_SERVICE_IMPL.recursive())
                    .should().dependOnClassesThat().resideInAnyPackage(
                            PackageType.DOMAIN_SERVICE.recursive(),
                            PackageType.JAVA_ROOT.recursive()
                    );
}
