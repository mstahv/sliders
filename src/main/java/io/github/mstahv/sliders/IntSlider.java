package io.github.mstahv.sliders;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.RangeInput;
import com.vaadin.flow.component.html.Span;

/**
 * A slider selecting a value.
 */
public class IntSlider extends CustomField<Integer> implements
        HasSize, HasStyle {

    private final RangeInput input = new RangeInput();
    private final Span min = new Span();
    private final Span max = new Span(){{
        getStyle().setLeft("100%");
    }};
    private final Span valueLabel = new Span(){{
        setClassName("value-label");
    }};
    private final Div valueLabels;

    /**
     * Creates a new slider with the given label, min, max and initial value.
     *
     * @param label the label
     * @param minValue the minimum value
     * @param maxValue the maximum value
     * @param value the initial value
     */
    public IntSlider(String label, int minValue, int maxValue, int value) {
        super(0);
        addClassName("slider");
        setLabel(label);
        valueLabels = new Div(){{
            add(min, max);
            setClassName("labels");
        }};
        valueLabels.addClassName("labels");
        add(new Div(
                new Div(){{
                    add(valueLabel, input);
                    setClassName("wrapper");
                }},
                valueLabels
        ));

        setMin(minValue);
        setMax(maxValue);
        setValue(value);

        setWidth("100%");

        input.getElement().executeJs("""
        const minElement = $0;
        const maxElement = $1;
        const valueLabel = $2;
        const input = this;
        const adjustLabel = () => {
            const handleW = 15;
            const v = (input.value - input.min) / (input.max - input.min);
            const pixels = (input.offsetWidth - 2*handleW) * v - valueLabel.offsetWidth / 2 + handleW + 2;
            valueLabel.style.left = pixels + 'px';
            valueLabel.innerText = "" + input.value;
        };
        input.addEventListener('input', adjustLabel);
        setTimeout(() => adjustLabel(), 0);
        new ResizeObserver(() => adjustLabel()).observe(input.parentElement);
        """, min, max, valueLabel);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        baseCss();
    }

    /**
     * Injects the base CSS for the slider. Some of the styles are mandatory for the slider to work.
     * These can be overridden in your theme css or by overriding this method.
     */
    protected void baseCss() {
        // TODO figure out if one could conditionally inject Aura version of styles 🤔
        getUI().get().getPage().addStyleSheet("context://sliders-addon/styles.css");
    }

    /**
     * Sets the minimum value of the slider.
     *
     * @param minValue the minimum value
     */
    public void setMin(int minValue) {
        input.setMin(minValue);
        min.setText(String.valueOf(minValue));
    }

    /**
     * Sets the maximum value of the slider.
     *
     * @param maxValue the maximum value
     */
    public void setMax(int maxValue) {
        input.setMax(maxValue);
        max.setText(String.valueOf(maxValue));
    }

    /**
     * Returns the maximum value of the slider.
     *
     * @return max value
     */
    public double getMax() {
        return input.getMax();
    }

    @Override
    protected Integer generateModelValue() {
        // The magic I don't really like about CustomField, this gets called automatically for
        // some fields if any of their children happens to emit value change event. So be awere,
        // ANY input element within this component hierarchy will trigger this method!!!
        // for this component this is fine, but for more complex components I suggest to stop
        // propagation of all value change events from children. With a wrapper element can happen
        // e.g. like this (in constructor/onattach):
        // content.getElement().executeJs("""
        //    this.addEventListener("change", e => e.stopPropagation());
        // """);
        return input.getValue().intValue();
    }

    @Override
    protected void setPresentationValue(Integer newPresentationValue) {
        input.setValue(newPresentationValue.doubleValue());
        valueLabel.setText(String.valueOf(newPresentationValue));
    }

    public void setMaxLabel(String label) {
        max.setText(label);
    }

    @Override
    public void setValue(Integer value) {
        super.setValue(value);
        // Workaound for a Vaadin bug
        input.getElement().setProperty("value", value);
    }

    public void setMinLabel(String label) {
        min.setText(label);
    }

    public void addCustomLabel(int customLabelValue, String customLabel) {
        valueLabels.add(new ValueLabel(customLabelValue, customLabel));
    }

    private class ValueLabel extends Span {
        public ValueLabel(int customLabelValue, String customLabel) {
            setText(customLabel);
            getElement().setProperty("customLabelValue", customLabelValue);
            double pct = (customLabelValue - input.getMin()) / (input.getMax() - input.getMin()) * 100;
            getElement().getStyle().setLeft(pct + "%");
        }
    }
}
