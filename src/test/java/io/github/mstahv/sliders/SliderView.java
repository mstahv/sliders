package io.github.mstahv.sliders;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route
public class SliderView extends VerticalLayout {
    public SliderView() {

        IntSlider intSlider = new IntSlider("Select a value", 0, 100, 0);
        add(intSlider);
        intSlider.addValueChangeListener(e -> {
            Notification.show("Value changed to " + e.getValue());
        });
    }
}
