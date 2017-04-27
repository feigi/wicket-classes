package de.core_concepts.wicket;

import de.o2.portal.ui.selfcare.AbstractSelfcareWicketTest;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HideEmptyFeedbackPanelBehaviorTest extends AbstractSelfcareWicketTest{

    HideEmptyFeedbackPanelBehavior testee = HideEmptyFeedbackPanelBehavior.getInstance();

    @Test
    public void testIsEnabled_CorrectComponent(){
        FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackPanel");

        tester.startComponentInPage(feedbackPanel);
        Assert.assertTrue(testee.isEnabled(feedbackPanel));
    }

    @Test
    public void testOnConfigure_CorrectComponent_withError(){
        FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackPanel");
        feedbackPanel.add(testee);
        feedbackPanel.error("Error");

        tester.startComponentInPage(feedbackPanel);
        tester.assertErrorMessages("Error");
        tester.assertVisible("feedbackPanel");
    }

    @Test
    public void testOnConfigure_CorrectComponent_withEmptyError(){
        FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackPanel");
        feedbackPanel.add(testee);
        feedbackPanel.error("");

        tester.startComponentInPage(feedbackPanel);
        tester.assertErrorMessages("");
        tester.assertInvisible("feedbackPanel");
    }

    @Test
    public void testOnConfigure_CorrectComponent_WithoutError(){
        FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackPanel");
        feedbackPanel.add(testee);

        tester.startComponentInPage(feedbackPanel);
        tester.assertInvisible("feedbackPanel");
    }

    @Test
    public void testIsEnabled_WrongComponent(){
        WebMarkupContainer container = new WebMarkupContainer("container");
        tester.startComponentInPage(container);
        Assert.assertFalse(testee.isEnabled(container));
    }
}
