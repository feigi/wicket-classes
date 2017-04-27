package de.core_concepts.wicket

import de.o2.portal.ui.selfcare.AbstractSelfcareWicketTest;
import org.apache.wicket.model.Model;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ChainedBooleanModelTest extends AbstractSelfcareWicketTest {

    @Test
    public void testAnd() throws Exception {
        boolean expected = true && true;
        boolean actual = new ChainedBooleanModel(Model.of(true)).And(Model.of(true)).getObject();
        assertEquals(actual, expected);

        expected = true && false;
        actual = new ChainedBooleanModel(Model.of(true)).And(Model.of(false)).getObject();
        assertEquals(actual, expected);

        expected = false && true;
        actual = new ChainedBooleanModel(Model.of(false)).And(Model.of(true)).getObject();
        assertEquals(actual, expected);

        expected = false && false;
        actual = new ChainedBooleanModel(Model.of(false)).And(Model.of(false)).getObject();
        assertEquals(actual, expected);
    }

    @Test
    public void testOr() throws Exception {
        boolean expected = true || true;
        boolean actual = new ChainedBooleanModel(Model.of(true)).Or(Model.of(true)).getObject();
        assertEquals(actual, expected);

        expected = true || false;
        actual = new ChainedBooleanModel(Model.of(true)).Or(Model.of(false)).getObject();
        assertEquals(actual, expected);

        expected = false || true;
        actual = new ChainedBooleanModel(Model.of(false)).Or(Model.of(true)).getObject();
        assertEquals(actual, expected);

        expected = false || false;
        actual = new ChainedBooleanModel(Model.of(false)).Or(Model.of(false)).getObject();
        assertEquals(actual, expected);
    }

    @Test
    public void testNot() throws Exception {
        boolean expected = !true;
        boolean actual = new ChainedBooleanModel(Model.of(true)).Not().getObject();
        assertEquals(actual, expected);

        expected = !false;
        actual = new ChainedBooleanModel(Model.of(false)).Not().getObject();
        assertEquals(actual, expected);
    }

    @Test
    public void testA() throws Exception {
        boolean expected = !true && false || true;
        boolean actual = new ChainedBooleanModel(Model.of(true)).Not().And(Model.of(false)).Or(Model.of(true)).getObject();
        assertEquals(actual, expected);
    }

    @Test
    public void testB() throws Exception {
        boolean expected = true && true && !true;
        boolean actual = new ChainedBooleanModel(Model.of(true)).And(Model.of(true)).Or(Model.of(true)).Not().getObject();
        assertEquals(actual, expected);
    }

    @Test
    public void testC() throws Exception {
        boolean expected = false || false || true;
        boolean actual = new ChainedBooleanModel(Model.of(false)).Or(Model.of(false)).Or(Model.of(true)).getObject();
        assertEquals(actual, expected);
    }

    @DataProvider(name = "chainedChained")
    private Object[][] getDataProvider() {
        return new Object[][]{
                {true, true, true},
                {true, true, false},
                {true, false, true},
                {false, true, true},
                {true, false, false},
                {false, true, false},
                {false, false, false},
                {false, false, true},
        };
    }

    @Test(dataProvider = "chainedChained")
    public void testChained(boolean first, boolean second, boolean third) {
        boolean expected = first && (!second || third);
        boolean actual = new ChainedBooleanModel(Model.of(first)).
                And(
                        new ChainedBooleanModel(Model.of(second)).Not().
                                Or(Model.of(third))
                ).getObject();
        assertEquals(actual, expected);
    }


    @DataProvider(name = "chainedChained2")
    private Object[][] getDataProvider2() {
        return new Object[][]{
                {true, true, true},
                {true, true, false},
                {true, false, true},
                {false, true, true},
                {true, false, false},
                {false, true, false},
                {false, false, false},
                {false, false, true},
        };
    }

    @Test(dataProvider = "chainedChained2")
    public void testChained2(boolean first, boolean second, boolean third) {
        boolean expected = !first || (second && !third);
        // !(second && third)

        boolean actual = new ChainedBooleanModel(
                Model.of(first)).Not().
                Or(
                        new ChainedBooleanModel(Model.of(third)).Not().
                                And(Model.of(second))
                ).getObject();
        assertEquals(actual, expected);
    }
}