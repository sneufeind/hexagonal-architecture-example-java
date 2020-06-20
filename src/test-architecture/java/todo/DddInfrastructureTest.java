package todo;

import archtest.PackageType;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.junit.ArchUnitRunner;
import com.tngtech.archunit.lang.ArchRule;
import common.architecture.Adapter;
import common.architecture.Port;
import org.junit.Ignore;
import org.junit.runner.RunWith;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@Ignore("make a difference between in and out ports/adapters and exclude model classes") //FIXME
@RunWith(ArchUnitRunner.class)
@AnalyzeClasses(packages = {"todo"})
public class DddInfrastructureTest {

    @ArchTest
    static final ArchRule adapterMustBeAnnotatedWithAdapter =
            classes()
                    .that().resideInAnyPackage(PackageType.INFRASTRUCTURE_ADAPTER.recursive())
                    .should().beAnnotatedWith(Adapter.class);

    @ArchTest
    static final ArchRule adapterMustImplementPort =
            classes()
                    .that().resideInAnyPackage(PackageType.INFRASTRUCTURE_ADAPTER.recursive())
                    .should().dependOnClassesThat().resideInAPackage(PackageType.DOMAIN_PORT.recursive())
                    .andShould().dependOnClassesThat().areAnnotatedWith(Port.class);

}
