package todo;

import archtest.OnionArchitecture;
import archtest.PackageType;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchIgnore;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.junit.ArchUnitRunner;
import com.tngtech.archunit.lang.ArchRule;
import common.architecture.UseCase;
import io.hschwentner.dddbits.annotation.Aggregate;
import io.hschwentner.dddbits.annotation.AggregateRoot;
import io.hschwentner.dddbits.annotation.DomainEntity;
import io.hschwentner.dddbits.annotation.ValueObject;
import org.junit.runner.RunWith;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@RunWith(ArchUnitRunner.class)
@AnalyzeClasses(packages = {"todo"})
public class DddArchitectureTest {

    @ArchTest
    static final ArchRule followsOnionArchitectureStructuredRule =
            new OnionArchitecture()
                    .applicationPackage(PackageType.APPLICATION_ROOT.recursive())
                    .domainPackage(PackageType.DOMAIN_ROOT.recursive())
                    .infrastructurePackage(PackageType.INFRASTRUCTURE_ROOT.recursive());

    @ArchTest
    static final ArchRule usecasesShouldBeAnnotatedInterfaces =
            classes()
                    .that().resideInAPackage(PackageType.APPLICATION_ROOT.get())
                    .should().beInterfaces()
                    .andShould().beAnnotatedWith(UseCase.class);

    @ArchTest
    static final ArchRule usecaseShouldNotAccessApplicationServices =
            noClasses()
                    .that().resideInAPackage(PackageType.APPLICATION_ROOT.get())
                    .should().accessClassesThat().resideInAPackage(PackageType.APPLICATION_SERVICE.get());

    @ArchIgnore
    @ArchTest
    static final ArchRule valueObjectMayNotUseEntitiesOrAggregates =
            noClasses()
                    .that().areAnnotatedWith(ValueObject.class)
                    .should().accessClassesThat().areAnnotatedWith(DomainEntity.class)
                    .orShould().accessClassesThat().areNotAnnotatedWith(Aggregate.class)
                    .orShould().accessClassesThat().areNotAnnotatedWith(AggregateRoot.class);

}
