package io.github.mstahv.sliders;

import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.RangeInput;
import com.vaadin.flow.component.html.Span;
import org.vaadin.firitin.components.html.VDiv;
import org.vaadin.firitin.components.html.VSpan;
import org.vaadin.firitin.fluency.ui.FluentHasSize;
import org.vaadin.firitin.fluency.ui.FluentHasStyle;
import org.vaadin.firitin.util.VStyleUtil;

/**
 * A slider selecting a value.
 */
public class IntSlider extends CustomField<Integer> implements
        FluentHasSize<IntSlider>, FluentHasStyle<IntSlider> {

    private final RangeInput input = new RangeInput();
    private final Span min = new Span();
    private final Span max = new VSpan().withStyle("left", "100%");
    private final Span valueLabel = new VSpan().withClassName("value-label");
    private final VDiv valueLabels;

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
        valueLabels = new VDiv(
                min,
                max
        ).withClassName("labels");
        add(new Div(
                new VDiv(
                        valueLabel,
                        input
                ).withClassName("wrapper"),
                valueLabels
        ));

        setMin(minValue);
        setMax(maxValue);
        setValue(value);

        baseCss();
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

    /**
     * Injects the base CSS for the slider. Some of the styles are mandatory for the slider to work.
     * These can be overridden in your theme css.
     */
    protected void baseCss() {
        VStyleUtil.injectAsFirst("""
        .slider>div {
            display: flex;
            box-sizing: border-box;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            min-width: 100px;
        }
        .slider .labels {
          display: block;
          position: relative;
          width: calc(100% - var(--lumo-size-xl)/2);
          span {
            font-size: var(--lumo-font-size-s);
            color: var(--lumo-contrast-90pct);
            height: var(--lumo-size-m);
            position: absolute;
            display: block;
            transform: translateX(-50%);
          }
        }
        .slider .wrapper {
            width:100%;
            position: relative;
        }
        .slider input[type=range] {
            width: 100%;
            -webkit-appearance: none;
            margin:0;
            margin-top: var(--lumo-space-xl);
        }
        .slider input:focus {
                outline: none;
            }
        .slider input::-webkit-slider-runnable-track {
                width: 100%;
                height: 1px;
                cursor: pointer;
                box-shadow: none;
                background: var(--lumo-contrast);
                border-radius: 0px;
                border: 0px solid #010101;
            }
        .slider input::-moz-range-track {
                width: 100%;
                height: 1px;
                cursor: pointer;
                box-shadow: none;
                background: var(--lumo-contrast);
                border-radius: 0px;
                border: 0px solid #010101;
            }
        .slider input::-webkit-slider-thumb {
                box-shadow: 0px var(--lumo-space-s) var(--lumo-space-s) var(--lumo-contrast-20pct);
                height: var(--lumo-size-l);
                width: var(--lumo-size-s);
                box-sizing: border-box;
                border-radius: var(--lumo-size-s);
                background: var(--lumo-primary-color);
                cursor: pointer;
                -webkit-appearance: none;
                margin-top: -22px;
            }
        .slider input::-moz-range-thumb{
                box-shadow: 0px var(--lumo-space-s) var(--lumo-space-s) var(--lumo-contrast-20pct);
                height: var(--lumo-size-l);
                width: var(--lumo-size-s);
                box-sizing: border-box;
                border-radius: var(--lumo-size-s);
                background: var(--lumo-primary-color);
                cursor: pointer;
                -webkit-appearance: none;
                margin-top: -22px;
          }
        .slider input::-moz-focus-outer {
            border: 0;
        }
            
        .slider .value-label {
            position: relative;
            display: block;
            width: var(--lumo-size-m);
            height: var(--lumo-size-m);
            border-radius: 50%;
            text-align: center;
            font-weight: bold;
            border: 1px solid var(--lumo-contrast-70pct);
            color: var(--lumo-contrast-90pct);
            font-size: var(--lumo-font-size-m);
            line-height:var(--lumo-size-m);
            padding-top: 0px;
        }
        """);
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
