package io.github.snankara.shop.bootstrap.archunit;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class DependencyRuleTest {

    private static final String ROOT_PACKAGE = "io.github.snankara.shop";
    private static final String MODEL_PACKAGE = "model";
    private static final String APPLICATION_PACKAGE = "application";
    private static final String PORT_PACKAGE = "application.port";
    private static final String SERVICE_PACKAGE = "application.service";
    private static final String ADAPTER_PACKAGE = "adapter";
    private static final String BOOTSTRAP_PACKAGE = "bootstrap";

    @Test
    void checkDependencyRule(){
        String importPackages = ROOT_PACKAGE + "..";
        //get all java classes
        JavaClasses classesToBeCheck = new ClassFileImporter().importPackages(importPackages);

        checkNoDependencyFromTo(MODEL_PACKAGE, APPLICATION_PACKAGE, classesToBeCheck);
        checkNoDependencyFromTo(MODEL_PACKAGE, ADAPTER_PACKAGE, classesToBeCheck);
        checkNoDependencyFromTo(MODEL_PACKAGE, BOOTSTRAP_PACKAGE, classesToBeCheck);

        checkNoDependencyFromTo(APPLICATION_PACKAGE, ADAPTER_PACKAGE, classesToBeCheck);
        checkNoDependencyFromTo(APPLICATION_PACKAGE, BOOTSTRAP_PACKAGE, classesToBeCheck);

        checkNoDependencyFromTo(PORT_PACKAGE, SERVICE_PACKAGE, classesToBeCheck);

        checkNoDependencyFromTo(ADAPTER_PACKAGE, SERVICE_PACKAGE, classesToBeCheck);
        checkNoDependencyFromTo(ADAPTER_PACKAGE, BOOTSTRAP_PACKAGE, classesToBeCheck);
    }

    private void checkNoDependencyFromTo(String fromPackage, String toPackage, JavaClasses classesToBeCheck) {
        noClasses()
                .that()
                .resideInAPackage(fullyQualifiedName(fromPackage))
                .should()
                .dependOnClassesThat()
                .resideInAPackage(fullyQualifiedName(toPackage))
                .check(classesToBeCheck);
    }

    private String fullyQualifiedName(String packageName) {
        return ROOT_PACKAGE + "." + packageName + "..";
    }
}
