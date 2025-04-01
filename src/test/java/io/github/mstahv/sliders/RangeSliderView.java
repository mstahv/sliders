package io.github.mstahv.sliders;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route
public class RangeSliderView extends VerticalLayout {
    public RangeSliderView() {

        RangeSliderConfig rangeSliderConfig = new RangeSliderConfig();
        rangeSliderConfig.setMin(0.0);
        rangeSliderConfig.setMax(100.0);
        rangeSliderConfig.setStep(1.0);
        rangeSliderConfig.setValue(new RangeSliderValue(10, 90));

        RangeSlider rangeSlider = new RangeSlider(rangeSliderConfig);
        add(rangeSlider);
        rangeSlider.addValueChangeListener(e -> {
            Notification.show("Value changed to " + e.getValue());
        });
    }
}
