package org.test;

import javax.servlet.annotation.WebServlet;

import org.test.design.impl.CustomerForm;

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
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of a html page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI
{

	/* main class to maintain data */
	private CustomerService m_customerService = CustomerService.getInstance();
	/* UI grid to show customer data */
	private Grid m_grid = new Grid();
	/* UI text filter to filter customer records */
	private TextField m_filter = new TextField();
	/* UI form to manipulate customer records */
	private CustomerForm m_form = new CustomerForm(this);

	@Override
	protected void init(VaadinRequest vaadinRequest)
	{
		// by default the the management form as invisible
		m_form.setVisible(false);

		// data grid attribute appearance order
		m_grid.setColumns("firstName", "lastName", "birthDate", "status", "email");

		// main page layout
		VerticalLayout layout = new VerticalLayout();

		// the filter controller, listen to the event of textChange
		m_filter.setInputPrompt("filter by name...");
		m_filter.addTextChangeListener(e -> {
			m_grid.setContainerDataSource(
					new BeanItemContainer<>(Customer.class, m_customerService.findAll(e.getText())));
		});
		// clear button for filter controller
		Button clearBtn = new Button(FontAwesome.TIMES);
		clearBtn.addClickListener(e -> {
			m_filter.clear();
			updateGridList();
		});
		// a css layout make the components joined seamlessly together
		CssLayout cssHLayout = new CssLayout();
		cssHLayout.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		// compose filter text with corresponding button
		cssHLayout.addComponents(m_filter, clearBtn);

		// data manager - add/delete records
		HorizontalLayout dataManageLayout = new HorizontalLayout(m_grid, m_form);
		dataManageLayout.setSpacing(true);
		dataManageLayout.setSizeFull();
		m_grid.setSizeFull();
		dataManageLayout.setExpandRatio(m_grid, 1);

		// combine grid with customer form actions
		m_grid.addSelectionListener(e->
		{
			if (e.getSelected().isEmpty())
			{
				m_form.setVisible(false);
			}
			else
			{
				Customer selectedCustomer = (Customer) e.getSelected().iterator().next();
				m_form.setCustomer(selectedCustomer);
			}
		});
		
		// create new customer
		Button addNewCustomerBtn = new Button("Add new customer");
		addNewCustomerBtn.addClickListener(e -> {
			// clear grid selection
			m_grid.select(null);
			m_form.setCustomer(new Customer());
		} );
		
		// whole page layout setup: add data management form to main form
		HorizontalLayout toolBar = new HorizontalLayout(cssHLayout, addNewCustomerBtn);
		toolBar.setSpacing(true);
		layout.addComponents(toolBar, dataManageLayout);

		updateGridList();
		layout.setMargin(true);
		layout.setSpacing(true);
		setContent(layout);
	}

	/**
	 * update data grid
	 */
	public void updateGridList()
	{
		List customers = m_customerService.findAll();
		m_grid.setContainerDataSource(new BeanItemContainer<>(Customer.class, customers));
	}

	// @WebServlet(urlPatterns = "/myui/*", name = "MyUIServlet", asyncSupported
	// = true)
	@WebServlet(value = { "/myui/*", "/VAADIN/*" })
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet
	{
	}
}
