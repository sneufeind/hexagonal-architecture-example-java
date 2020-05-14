package todo;

import archtest.PackageType;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.junit.ArchUnitRunner;
import com.tngtech.archunit.lang.ArchRule;
import common.architecture.Port;
import io.hschwentner.dddbits.annotation.*;
import org.junit.runner.RunWith;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@RunWith(ArchUnitRunner.class)
@AnalyzeClasses(packages = {"todo"})
public class DddDomainTest {

    @ArchTest
    static final ArchRule domainEventDependsOnlyOnDomainModel =
            noClasses()
                    .that().resideInAPackage(PackageType.DOMAIN_EVENT.recursive())
                    .should().accessClassesThat().resideOutsideOfPackages(
                            PackageType.DOMAIN_EVENT.recursive(),
                            PackageType.DOMAIN_MODEL.recursive(),
                            PackageType.JAVA_ROOT.recursive()
                    );

    @ArchTest
    static final ArchRule domainExceptionsDependsOnlyOnDomainModel =
            noClasses()
                    .that().resideInAPackage(PackageType.DOMAIN_EXCEPTION.recursive())
                    .should().accessClassesThat().resideOutsideOfPackages(
                            PackageType.DOMAIN_EXCEPTION.recursive(),
                            PackageType.DOMAIN_MODEL.recursive(),
                            PackageType.JAVA_ROOT.recursive()
                    );

    @ArchTest
    static final ArchRule domainModelShouldBeAnntotated =
            classes()
                    .that().resideInAPackage(PackageType.DOMAIN_MODEL.recursive())
                    .should().beAnnotatedWith(ValueObject.class)
                    .orShould().beAnnotatedWith(DomainEntity.class)
                    .orShould().beAnnotatedWith(Aggregate.class)
                    .orShould().beAnnotatedWith(AggregateRoot.class);

    @ArchTest
    static final ArchRule domainModelDependsOnNothing =
            noClasses()
                    .that().resideInAPackage(PackageType.DOMAIN_MODEL.recursive())
                    .should().accessClassesThat().resideOutsideOfPackages(
                            PackageType.DOMAIN_MODEL.recursive(),
                            PackageType.JAVA_ROOT.recursive()
                    );

    @ArchTest
    static final ArchRule domainPortShouldBeAnnotatedInterfaces =
            classes()
                    .that().resideInAPackage(PackageType.DOMAIN_PORT.recursive())
                    .should().beInterfaces()
                    .andShould().beAnnotatedWith(Port.class);

    @ArchTest
    static final ArchRule domainServiceInterfaceShouldePlacedIn =
            classes()
                    .that().resideInAPackage(PackageType.DOMAIN_SERVICE.get())
                    .should().beInterfaces();

    @ArchTest
    static final ArchRule domainServiceImplMustBeAnnotatedWithDomainService =
            classes()
                    .that().resideInAnyPackage(PackageType.DOMAIN_SERVICE_IMPL.recursive())
                    .should().beAnnotatedWith(DomainService.class);

    @ArchTest
    static final ArchRule domainServiceImplMustImplementDomainServiceInterfaces =
            classes()
                    .that().resideInAnyPackage(PackageType.DOMAIN_SERVICE_IMPL.recursive())
                    .should().dependOnClassesThat().resideInAnyPackage(
                            PackageType.DOMAIN_ROOT.recursive(),
                            PackageType.JAVA_ROOT.recursive()
                    );
}
