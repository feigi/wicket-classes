package de.core_concepts.wicket;

import de.o2.portal.ui.selfcare.AbstractSelfcareWicketTest;
import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ObfuscateBehaviorTest extends AbstractSelfcareWicketTest {

    public static final String BODY = "mySuperDuperLabel";

    @Test(dataProvider = "labelMarkupProvider")
    public void test_ShowNone(Markup markup) throws Exception {
        Label label = new Label("testee", BODY);
        label.add(new ObfuscateBehavior().showNone());
        tester.startComponentInPage(label, markup);
        tester.assertTagBody("testee", "*****************");
    }

    @Test(dataProvider = "labelMarkupProvider")
    public void test_SlashInBodyText(Markup markup) throws Exception {
        Label label = new Label("testee", BODY + "/");
        label.add(new ObfuscateBehavior().showNone());
        tester.startComponentInPage(label, markup);
        tester.assertTagBody("testee", "******************");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void test_ShowFirst_Minus1() throws Exception {
        Label label = new Label("testee", BODY);
        label.add(new ObfuscateBehavior().showFirst(-1));
        tester.startComponentInPage(label);
    }

    @Test(dataProvider = "labelMarkupProvider")
    public void test_ShowFirst_0(Markup markup) throws Exception {
        Label label = new Label("testee", BODY);
        label.add(new ObfuscateBehavior().showFirst(0));
        tester.startComponentInPage(label, markup);
        tester.assertTagBody("testee", "*****************");
    }

    @Test(dataProvider = "labelMarkupProvider")
    public void test_ShowFirst_4(Markup markup) throws Exception {
        Label label = new Label("testee", BODY);
        label.add(new ObfuscateBehavior().showFirst(4));
        tester.startComponentInPage(label, markup);
        tester.assertTagBody("testee", "mySu*************");
    }

    @Test(dataProvider = "labelMarkupProvider")
    public void test_ShowFirst_17(Markup markup) throws Exception {
        Label label = new Label("testee", BODY);
        label.add(new ObfuscateBehavior().showFirst(17));
        tester.startComponentInPage(label, markup);
        tester.assertTagBody("testee", BODY);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void test_ShowLast_Minus1() throws Exception {
        Label label = new Label("testee", BODY);
        label.add(new ObfuscateBehavior().showLast(-1));
        tester.startComponentInPage(label);
    }

    @Test(dataProvider = "labelMarkupProvider")
    public void test_ShowLast_0(Markup markup) throws Exception {
        Label label = new Label("testee", BODY);
        label.add(new ObfuscateBehavior().showLast(0));
        tester.startComponentInPage(label, markup);
        tester.assertTagBody("testee", "*****************");
    }

    @Test(dataProvider = "labelMarkupProvider")
    public void test_ShowLast_4(Markup markup) throws Exception {
        Label label = new Label("testee", BODY);
        label.add(new ObfuscateBehavior().showLast(4));
        tester.startComponentInPage(label, markup);
        tester.assertTagBody("testee", "*************abel");
    }

    @Test(dataProvider = "labelMarkupProvider")
    public void test_ShowLast_17(Markup markup) throws Exception {
        Label label = new Label("testee", BODY);
        label.add(new ObfuscateBehavior().showLast(17));
        tester.startComponentInPage(label, markup);
        tester.assertTagBody("testee", BODY);
    }

    @Test(dataProvider = "labelMarkupProvider")
    public void test_ShowRange_InBounds(Markup markup) throws Exception {
        Label label = new Label("testee", BODY);
        label.add(new ObfuscateBehavior().showRange(2, 8));
        tester.startComponentInPage(label, markup);
        tester.assertTagBody("testee", "**SuperD*********");
    }

    @Test(dataProvider = "labelMarkupProvider")
    public void test_ShowRange_PartlyInBounds(Markup markup) throws Exception {
        Label label = new Label("testee", BODY);
        label.add(new ObfuscateBehavior().showRange(14, 19));
        tester.startComponentInPage(label, markup);
        tester.assertTagBody("testee", "**************bel");
    }

    @Test(dataProvider = "labelMarkupProvider")
    public void test_ShowRange_NotInBounds(Markup markup) throws Exception {
        Label label = new Label("testee", BODY);
        label.add(new ObfuscateBehavior().showRange(17, 21));
        tester.startComponentInPage(label, markup);
        tester.assertTagBody("testee", "*****************");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void test_ShowRange_IllegalArgument() throws Exception {
        Label label = new Label("testee", BODY);
        label.add(new ObfuscateBehavior().showRange(-1, 21));
        tester.startComponentInPage(label);
    }

    @Test(dataProvider = "labelMarkupProvider")
    public void test_ShowRange_TwoRangesInBounds(Markup markup) throws Exception {
        Label label = new Label("testee", BODY);
        label.add(new ObfuscateBehavior().showRange(2, 8).showRange(14, 16));
        tester.startComponentInPage(label, markup);
        tester.assertTagBody("testee", "**SuperD******be*");
    }

    @Test(dataProvider = "labelMarkupProvider")
    public void test_ShowMethodsCombined(Markup markup) throws Exception {
        Label label = new Label("testee", BODY);
        label.add(new ObfuscateBehavior().showFirst(2).showLast(4).showRange(6, 9));
        tester.startComponentInPage(label, markup);
        tester.assertTagBody("testee", "my****rDu****abel");
    }

    @Test(dataProvider = "labelMarkupProvider")
    public void test_ShowMethodsCombined2(Markup markup) throws Exception {
        Label label = new Label("testee", BODY);
        label.add(new ObfuscateBehavior().showLast(4).showRange(2, 4));
        tester.startComponentInPage(label, markup);
        tester.assertTagBody("testee", "**Su*********abel");
    }

    @DataProvider(name = "labelMarkupProvider")
    private Object[][] labelMarkupProvider() {
        return new Object[][]{
                {Markup.of("<label wicket:id='testee'></label>")},
                {Markup.of("<wicket:label wicket:id='testee'></wicket:label>")},
        };
    }

    @Test(dataProvider = "inputMarkupProvider")
    public void test_Input_ShowFirst_4(Markup markup) throws Exception {
        TextField textField = new TextField("testee", Model.of(BODY));
        textField.add(new ObfuscateBehavior().showFirst(4));
        tester.startComponentInPage(textField, markup);
        tester.assertAttributeHasValue("testee", "value", "mySu*************");
    }

    @DataProvider(name = "inputMarkupProvider")
    private Object[][] inputMarkupProvider() {
        return new Object[][]{
                {Markup.of("<input value='" + BODY + "' type='text' wicket:id='testee'/>")},
                {Markup.of("<input value='" + BODY + "\"' type='text' wicket:id='testee'/>")},
                {Markup.of("<input value=\"" + BODY + "\" type='text' wicket:id='testee'/>")}
        };
    }
}