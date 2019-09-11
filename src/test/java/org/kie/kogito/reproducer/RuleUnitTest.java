package org.kie.kogito.reproducer;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

import javax.inject.Inject;
import javax.inject.Named;

import org.drools.core.reteoo.ReteDumper;
import org.drools.core.ruleunit.impl.AbstractRuleUnitInstance;
import org.junit.jupiter.api.Test;
import org.kie.api.KieBase;
import org.kie.api.runtime.KieSession;
import org.kie.kogito.rules.RuleUnit;
import org.kie.kogito.rules.RuleUnitInstance;
import org.kie.kogito.rules.impl.SessionMemory;

import io.quarkus.test.junit.QuarkusTest;

/**
 * RuleUnitTest
 */
@QuarkusTest
public class RuleUnitTest {

    @Inject
    @Named("ruleUnitA")
    RuleUnit<SessionMemory> ruleUnitA;

    /*
    @Inject
    @Named("ruleUnitB")
    RuleUnit<SessionMemory> ruleUnitB;
    */

    @Test
    public void testRuleUnitA() throws Exception {
        SessionMemory sessionMemory = new SessionMemory();
        sessionMemory.add("Hello");
        RuleUnitInstance instanceA = ruleUnitA.createInstance(sessionMemory);
        Field kieSessionField = AbstractRuleUnitInstance.class.getDeclaredField("runtime");

        kieSessionField.setAccessible(true);
        KieSession kieSession = (KieSession) kieSessionField.get(instanceA);
        KieBase kieBase = kieSession.getKieBase();
        ReteDumper.dumpRete(kieBase);

        int rulesFired = instanceA.fire();
        assertEquals(1, rulesFired);
    }

}