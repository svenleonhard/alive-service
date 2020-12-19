package de.svenleonhard.alive;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("de.svenleonhard.alive");

        noClasses()
            .that()
            .resideInAnyPackage("de.svenleonhard.alive.service..")
            .or()
            .resideInAnyPackage("de.svenleonhard.alive.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..de.svenleonhard.alive.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
