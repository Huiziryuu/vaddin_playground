package org.test.design.impl;

import org.test.Customer;
import org.test.CustomerService;
import org.test.CustomerStatus;
import org.test.MyUI;
import org.test.design.CustomerFormDesign;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction.KeyCode;

public class CustomerForm extends CustomerFormDesign
{
	/* service provided to support data manipulation */
	private CustomerService m_service = CustomerService.getInstance();
	/* One customer */
	private Customer m_customer;
	private MyUI m_myUI;

	/**
	 * Constructor
	 * 
	 * @param myUI
	 *            MyUI type class
	 */
	public CustomerForm(MyUI myUI)
	{
		this.m_myUI = myUI;
		// initial combox status
		this.status.addItems(CustomerStatus.values());
		// add key event for save button
		this.save.setClickShortcut(KeyCode.ENTER);

		// add listener for save and delete button
		this.save.addClickListener(e -> this.save());
		this.delete.addClickListener(e -> this.delete());
	}

	public void setCustomer(Customer customer)
	{
		this.m_customer = customer;
		// bind the form field with the passed parameter values
		BeanFieldGroup.bindFieldsUnbuffered(customer, this);
		delete.setVisible(customer.isPersisted());
		setVisible(true);
		this.firstName.selectAll();
	}

	private void delete()
	{
		m_service.delete(m_customer);
		m_myUI.updateGridList();
		setVisible(false);
	}

	private void save()
	{
		m_service.save(m_customer);
		m_myUI.updateGridList();
		setVisible(false);
	}
}
