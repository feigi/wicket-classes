package de.core_concepts.wicket;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

import java.util.List;

/**
 * This behavior iterates the feedback messages of the feedbackPanel
 * it was added to. If all feedback messages are blank it sets
 * the feedbackPanel invisible, otherwise visible.
 */
public class HideEmptyFeedbackPanelBehavior extends Behavior {

    private static HideEmptyFeedbackPanelBehavior INSTANCE = new HideEmptyFeedbackPanelBehavior();

    private HideEmptyFeedbackPanelBehavior() {}

    public static HideEmptyFeedbackPanelBehavior getInstance() {
        return INSTANCE;
    }

    @Override
    public void onConfigure(Component component) {
        super.onConfigure(component);
        FeedbackPanel feedbackPanel = (FeedbackPanel) component;
        feedbackPanel.setVisible(hasNonEmptyFeedback(feedbackPanel));
    }

    @Override
    public boolean isEnabled(Component component) {
        return FeedbackPanel.class.isAssignableFrom(component.getClass());
    }

    /**
     * Iterates the feedback messages the panel
     * will potentially render and checks the
     * message strings on whether they are empty.
     *
     * @param feedbackPanel
     * @return true if there is at least one non-empty message
     * false otherwise.
     */
    private boolean hasNonEmptyFeedback(FeedbackPanel feedbackPanel) {
        List<FeedbackMessage> messages = feedbackPanel.getFeedbackMessagesModel().getObject();
        for (FeedbackMessage message : messages) {
            if (StringUtils.isNotBlank(message.getMessage().toString())) {
                return true;
            }
        }
        return false;
    }
}