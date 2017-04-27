package de.core_concepts.wicket;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.transformer.AbstractTransformerBehavior;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.HashSet;
import java.util.Set;

public class ObfuscateBehavior extends AbstractTransformerBehavior {

    private int showLast;
    private Set<Integer> ranges = new HashSet<>();
    private IModel<Boolean> activeModel = Model.of(Boolean.TRUE);

    public static ObfuscateBehavior showNone() {
        return new ObfuscateBehavior();
    }

    public ObfuscateBehavior showLast(int count) {
        Validate.inclusiveBetween(0, Integer.MAX_VALUE, count);
        showLast = count;
        return this;
    }

    public ObfuscateBehavior showFirst(int count) {
        Validate.inclusiveBetween(0, Integer.MAX_VALUE, count);
        for (int j = 0; j < count; j++) {
            ranges.add(j);
        }
        return this;
    }

    public ObfuscateBehavior showRange(int start, int end) {
        Validate.inclusiveBetween(0, Integer.MAX_VALUE, start);
        Validate.inclusiveBetween(0, Integer.MAX_VALUE, end);
        for (int i = start; i < end; i++) {
            ranges.add(i);
        }
        return this;
    }

    @Override
    public boolean isEnabled(Component component) {
        return activeModel.getObject() && (component instanceof Label || component instanceof TextField);
    }

    @Override
    public CharSequence transform(Component component, CharSequence output) throws Exception {

        StringBuilder outputString = new StringBuilder(output);
        String valueString = output.toString(); // If not wrapped in markup
        int valueStart = 0;
        int valueEnd = output.length();

        if (output.charAt(0) == '<') {
            if (isTextfield(valueString)) {
                int index = valueString.indexOf("value=");
                if (valueStart != -1) {
                    valueStart = index + "value".length() + 2;
                }
                char quote = valueString.charAt(valueStart - 1);
                valueEnd = valueString.indexOf(quote, valueStart);
            } else {
                valueStart = valueString.indexOf('>') + 1;
                valueEnd = valueString.lastIndexOf('<');
            }

            valueString = valueString.substring(valueStart, valueEnd);
        }

        return outputString.replace(valueStart, valueEnd, obfuscate(valueString)).toString();
    }

    private boolean isTextfield(String output) {
        return output.matches("<input.*type=['\"]text['\"].*");
    }

    public String obfuscate(String input) {
        if (StringUtils.isBlank(input)) {
            return "";
        }
        StringBuilder inputBuilder = new StringBuilder(input);
        for (int i = 0; i < input.length() - showLast; i++) {
            if (!ranges.contains(i)) {
                inputBuilder.replace(i, i + 1, "*");
            }
        }
        return inputBuilder.toString();
    }

    public void setActiveModel(IModel<Boolean> activeModel) {
        this.activeModel = activeModel;
    }
}
