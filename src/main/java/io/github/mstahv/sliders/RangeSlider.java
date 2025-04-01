package io.github.mstahv.sliders;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.customfield.CustomField;
import org.parttio.vaadinjsloader.JSLoader;
import org.vaadin.addons.velocitycomponent.AbstractVelocityJsComponent;
import org.vaadin.addons.velocitycomponent.VElement;

import java.util.Map;

/**
 * A slider selecting a range of values, wrapping JS component from https://github.com/n3r4zzurr0/range-slider-input
 */
public class RangeSlider extends CustomField<RangeSliderValue> {

    private final RangeSliderInternal rangeSliderInternal;

    public RangeSlider() {
        this(new RangeSliderConfig());
    }
    public RangeSlider(RangeSliderConfig config) {
        rangeSliderInternal = new RangeSliderInternal(config){
            @Override
            protected void setValue(RangeSliderValue value) {
                super.setValue(value);
                updateValue();
            }
        };
        add(rangeSliderInternal);
        setWidthFull();
    }

    @Override
    protected RangeSliderValue generateModelValue() {
        return rangeSliderInternal.getValue();
    }

    @Override
    protected void setPresentationValue(RangeSliderValue rangeSliderValue) {
        rangeSliderInternal.setValue(rangeSliderValue.min(), rangeSliderValue.max());

    }

    /**
     * This is the actual component, wrapped in a custom field for generic form field features.
     */
    @Tag("div")
    public static class RangeSliderInternal extends AbstractVelocityJsComponent {

        private final RangeSliderConfig config;
        private RangeSliderValue value;

        public RangeSliderInternal() {
            this(new RangeSliderConfig());
        }


        public RangeSliderInternal(RangeSliderConfig config) {
            this.config = config;
        }

        public RangeSliderConfig getConfig() {
            return config;
        }

        @Override
        protected void onAttach(AttachEvent attachEvent) {
            super.onAttach(attachEvent);
            loadJs(attachEvent.getUI());
            velocityJs("""
                const c = $Json.of($this.config);
                c.onInput = (value, userInteraction) => {
                    if (userInteraction) {
                        this.dispatchEvent(new CustomEvent('range-slider-value', { detail: {min: value[0], max: value[1]}}));;
                    }
                };
                setTimeout(() => {
                    const slider = rangeSlider(this, c);
                    this._slider = slider;
                    this._config = c;
                }, 0);
                """, Map.of("aa", config));
            VElement.of(getElement()).on(RangeSliderValue.class, this::setValue).debounce(150);
        }

        protected void setValue(RangeSliderValue value) {
            this.value = value;
        }

        // Override with empty implementation if you wish to bundle JS with npm to your
        // frontend bundle
        protected void loadJs(UI ui) {
            JSLoader.loadUnpkg(ui, "range-slider-input", "2");
        }

        public void setValue(double min, double max) {
            if(isAttached()) {
                getElement().executeJs("this._slider.setValue([$0, $1])", min, max);
            } else {
                config.setValue(new RangeSliderValue(min, max));
            }
        }


        public RangeSliderValue getValue() {
            if(value != null) {
                return value;
            } else {
                return config.getValue();
            }
        }
    }
}
