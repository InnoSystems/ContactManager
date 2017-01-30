package com.kaminski.cm1;

import java.io.Serializable;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * A sample program that demonstrates how to perform simple CRUD operations with
 * Hibernate framework.
 * 
 * @author www.codejava.net
 *
 */
public class ContactManager {

    public static void main(String[] args) {

	// A SessionFactory is set up once for an application!
	final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure() // configures
												  // settings
												  // from
												  // hibernate.cfg.xml
		.build();
	SessionFactory sessionFactory = null;
	try {
	    sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
	} catch (Exception e) {
	    // The registry would be destroyed by the SessionFactory, but we had
	    // trouble building the SessionFactory
	    // so destroy it manually.
	    StandardServiceRegistryBuilder.destroy(registry);
	    System.out.println("Exception on Create ContactManager " + e);
	}

	// opens a new session from the session factory
	Session session = sessionFactory.openSession();
	session.beginTransaction();
	// persists two new Contact objects
	Contact contact1 = new Contact("Nam", "hainatuatgmail.com", "Vietnam", "0904277091");
	session.persist(contact1);
	Contact contact2 = new Contact("Bill", "billatgmail.com", "USA", "18001900");
	Serializable id = session.save(contact2);
	System.out.println("created id: " + id);

	// loads a new object from database
	Contact contact3 = (Contact) session.get(Contact.class, new Integer(1));
	if (contact3 == null) {
	    System.out.println("There is no Contact object with id=1");
	} else {
	    System.out.println("Contact3's name: " + contact3.getName());
	}

	// loads an object which is assumed exists
	Contact contact4 = (Contact) session.load(Contact.class, new Integer(4));
	System.out.println("Contact4's name: " + contact4.getName());

	// updates a loaded instance of a Contact object
	Contact contact5 = (Contact) session.load(Contact.class, new Integer(5)); //h1
	contact5.setEmail("info1atcompany.com");
	contact5.setTelephone("1234567890");
	session.update(contact5);
	// updates a detached instance of a Contact object
	Contact contact6 = new Contact(3, "Jobs", "jobsatapplet.com", "Cupertino", "0123456789");
	session.update(contact6);

	// deletes an object
	Contact contact7 = new Contact();
	contact7.setId(7);
	session.delete(contact7);
	// deletes a loaded instance of an object
	Contact contact8 = (Contact) session.load(Contact.class, new Integer(8));
	session.delete(contact8);

	// commits the transaction and closes the session
	session.getTransaction().commit();
	session.close();
    }
}
