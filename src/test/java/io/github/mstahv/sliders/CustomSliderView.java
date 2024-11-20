package io.github.mstahv.sliders;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route
public class CustomSliderView extends VerticalLayout {
    public CustomSliderView() {

        IntSlider doubleSlider = new IntSlider("Select a value", 0, 100, 69);

        doubleSlider.setMaxLabel("Most");
        doubleSlider.setMinLabel("Least");
        doubleSlider.addCustomLabel(50, "Middle");
        doubleSlider.addCustomLabel(25, "Quarter");
        doubleSlider.addCustomLabel(75, "3/4");

        add(doubleSlider);
        doubleSlider.addValueChangeListener(e -> {
            Notification.show("Value changed to " + e.getValue());
        });
    }
}
