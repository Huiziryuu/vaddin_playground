package org.test;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

    private CustomerService m_customerService = CustomerService.getInstance();
    private Grid m_grid = new Grid();
    private TextField m_filter = new TextField();


    @Override
    protected void init(VaadinRequest vaadinRequest) {
    	// git commit test
        VerticalLayout layout = new VerticalLayout();
        m_filter.setInputPrompt("filter by name...");
        m_filter.addTextChangeListener(e -> {
            m_grid.setContainerDataSource(new BeanItemContainer<>(Customer.class,
                    m_customerService.findAll(e.getText())));
        });

        Button clearBtn = new Button(FontAwesome.TIMES);
        clearBtn.addClickListener(e -> {
            m_filter.clear();
            updateGridList();
        });

        CssLayout cssHLayout = new CssLayout();
        cssHLayout.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        cssHLayout.addComponents(m_filter, clearBtn);

        m_grid.setColumns("firstName", "lastName", "birthDate", "status", "email");
        layout.addComponents(cssHLayout, m_grid);

        updateGridList();
        layout.setMargin(true);
        layout.setSpacing(true);
        setContent(layout);
    }

    public void updateGridList() {
        List customers = m_customerService.findAll();
        m_grid.setContainerDataSource(new BeanItemContainer<>(Customer.class, customers));
    }

    //@WebServlet(urlPatterns = "/myui/*", name = "MyUIServlet", asyncSupported = true)
    @WebServlet(value = {"/myui/*", "/VAADIN/*"})
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
