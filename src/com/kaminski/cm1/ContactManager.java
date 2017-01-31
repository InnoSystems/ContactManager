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

    // A SessionFactory is set up once for an application!
    final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
    private SessionFactory sessionFactory = null;
    public Session session = null;

    private ContactManager() {
	try {
	    sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
	} catch (Exception e) {
	    // The registry would be destroyed by the SessionFactory, but we had
	    // trouble building the SessionFactory
	    // so destroy it manually.
	    StandardServiceRegistryBuilder.destroy(registry);
	    System.out.println("Exception on CREATE ContactManager " + e);
	}
	// opens a new session from the session factory
	session = sessionFactory.openSession();
    }

    private void checkContact(Contact contact, int id) {
	if (contact == null) {
	    System.out.printf("There is no Contact object with id=%s\n", id);
	} else {
	    System.out.printf("Contact's name: %s, id: %s\n", contact.getName(), contact.getId());
	    System.out.println(contact);
	}
    }

    public static ContactManager produceContactManager() {
	return new ContactManager();
    }

    public void action1() {
	System.out.println("action1()");
	session.beginTransaction();
	Contact contact1 = new Contact("Nam", "hainatuatgmail.com", "Vietnam", "0904277091");
	session.persist(contact1);
	session.flush();
	session.getTransaction().commit();
    }

    public void action2() {
	System.out.println("action2()");
	session.beginTransaction();
	Contact contact2 = new Contact("Bill", "billatgmail.com", "USA", "18001900");
	Serializable id = session.save(contact2);
	System.out.println("created id: " + id);
	session.flush();
	session.getTransaction().commit();
    }

    public void action3() {
	System.out.println("action3()");
	// loads a new object from database
	Contact contact3 = (Contact) session.get(Contact.class, new Integer(1));
	checkContact(contact3, 1);
    }

    public void action4() {
	System.out.println("action4()");
	// 1. session.load()
	// It will always return a “proxy” (Hibernate term) without hitting the
	// database. In Hibernate, proxy is an object with the given identifier
	// value, its properties are not initialized yet, it just look like a
	// temporary fake object.
	// If no row found , it will throws an ObjectNotFoundException.
	Contact contact4 = (Contact) session.load(Contact.class, new Integer(1));
	System.out.println("Contact4's name: " + contact4.getName());
	checkContact(contact4, 1);
    }

    public void action5() {
	System.out.println("action5()");
	// updates a loaded instance of a Contact object
	session.beginTransaction();
	Contact contact5 = (Contact) session.load(Contact.class, new Integer(1)); // h1
	checkContact(contact5, 1);
	contact5.setEmail("info1atcompany.com");
	contact5.setTelephone("1234567890");
	System.out.println("After Update: " + contact5);
	session.update(contact5);
	session.flush();
	session.getTransaction().commit();
    }

    public void action6() {
	System.out.println("action6()");
	// updates a detached instance of a Contact object
	session.beginTransaction();
	Contact contact6 = new Contact(13, "Jobs", "jobsatapplet.com", "Cupertino", "0123456789");
	Serializable id = session.save(contact6);
	System.out.println("created id: " + id);
	session.flush();
	session.getTransaction().commit();

	contact6.setTelephone("1111111111");
	session.update(contact6);
    }

    public void action7() {
	System.out.println("action7()");
	// deletes a loaded instance of an object
	session.beginTransaction();
	Contact contact2_Bill = (Contact) session.load(Contact.class, new Integer(2));
	session.delete(contact2_Bill);
	session.flush();
	session.getTransaction().commit();
    }

    public static void main(String[] args) {

	ContactManager cMgr = produceContactManager();

	cMgr.action1();

	cMgr.action2();

	cMgr.action3();

	cMgr.action4();

	cMgr.action5();

	cMgr.action6();

	cMgr.action7();

	cMgr.session.close();
    }
}
