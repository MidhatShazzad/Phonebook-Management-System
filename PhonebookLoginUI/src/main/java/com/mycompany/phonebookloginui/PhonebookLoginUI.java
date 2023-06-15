
package com.mycompany.phonebookloginui;

/**
 *
 * @author Midhat
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PhonebookLoginUI extends JFrame {
    private JButton loginButton;
    private JButton displayContactsButton;

    public PhonebookLoginUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Login");
        setSize(300, 200);
        setLayout(new BorderLayout());

        JPanel loginPanel = createLoginPanel();
        add(loginPanel, BorderLayout.CENTER);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 10, 5, 10);
        constraints.anchor = GridBagConstraints.WEST;

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(15);
        addFormField(panel, constraints, usernameLabel, usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(15);
        addFormField(panel, constraints, passwordLabel, passwordField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (username.equals("Admin") && password.equals("Password")) {
                    launchPhonebookUI();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        displayContactsButton = new JButton("Display Contacts");
        displayContactsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayContactsFromFile();
            }
        });

        buttonPanel.add(loginButton);
        buttonPanel.add(displayContactsButton);

        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 2;
        panel.add(buttonPanel, constraints);

        return panel;
    }

    private void addFormField(JPanel panel, GridBagConstraints constraints, JLabel label, JTextField textField) {
        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 1;
        panel.add(label, constraints);

        constraints.gridx = 1;
        panel.add(textField, constraints);
    }

    private void launchPhonebookUI() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                PhonebookUI phonebookUI = new PhonebookUI();
                phonebookUI.setVisible(true);
                dispose();
            }
        });
    }

    private void displayContactsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("contacts.txt"))) {
            StringBuilder contactsBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                contactsBuilder.append(line).append("\n");
            }
            JOptionPane.showMessageDialog(null, contactsBuilder.toString(), "Contacts", JOptionPane.PLAIN_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading contacts from file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                PhonebookLoginUI loginUI = new PhonebookLoginUI();
                loginUI.setVisible(true);
            }
        });
    }
}

class PhonebookUI extends JFrame {
    private Phonebook phonebook;
    private JTextField nameField;
    private JTextField phoneNumberField;
    private JTextField emailField;
    private JList<Contact> contactList;
    private DefaultListModel<Contact> listModel;
    private JButton addButton;
    private JButton editButton;
    private JButton removeButton;

    public PhonebookUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Phonebook");
        setSize(400, 500);
        setLayout(new BorderLayout());

        phonebook = new Phonebook();

        listModel = new DefaultListModel<>();
        contactList = new JList<>(listModel);
        contactList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        contactList.addListSelectionListener(e -> handleContactSelection());

        JScrollPane scrollPane = new JScrollPane(contactList);
        add(scrollPane, BorderLayout.CENTER);

        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.SOUTH);

        loadContacts();
        displayContacts();
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 10, 5, 10);
        constraints.anchor = GridBagConstraints.WEST;

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField(20);
        addFormField(panel, constraints, nameLabel, nameField);

        JLabel phoneNumberLabel = new JLabel("Phone Number:");
        phoneNumberField = new JTextField(20);
        addFormField(panel, constraints, phoneNumberLabel, phoneNumberField);

        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField(20);
        addFormField(panel, constraints, emailLabel, emailField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        addButton = new JButton("Add Contact");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String phoneNumber = phoneNumberField.getText();
                String email = emailField.getText();

                Contact contact = new Contact(name, phoneNumber, email);
                phonebook.addContact(contact);

                nameField.setText("");
                phoneNumberField.setText("");
                emailField.setText("");

                saveContacts();
                displayContacts();
            }
        });

        editButton = new JButton("Edit Contact");
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = contactList.getSelectedIndex();
                if (selectedIndex != -1) {
                    String name = nameField.getText();
                    String phoneNumber = phoneNumberField.getText();
                    String email = emailField.getText();

                    Contact contact = new Contact(name, phoneNumber, email);
                    phonebook.editContact(selectedIndex, contact);

                    nameField.setText("");
                    phoneNumberField.setText("");
                    emailField.setText("");

                    saveContacts();
                    displayContacts();
                }
            }
        });
        editButton.setEnabled(false);

        removeButton = new JButton("Remove Contact");
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = contactList.getSelectedIndex();
                if (selectedIndex != -1) {
                    phonebook.removeContact(selectedIndex);

                    nameField.setText("");
                    phoneNumberField.setText("");
                    emailField.setText("");

                    saveContacts();
                    displayContacts();
                }
            }
        });
        removeButton.setEnabled(false);

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(removeButton);

        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 2;
        panel.add(buttonPanel, constraints);

        return panel;
    }

    private void addFormField(JPanel panel, GridBagConstraints constraints, JLabel label, JTextField textField) {
        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 1;
        panel.add(label, constraints);

        constraints.gridx = 1;
        panel.add(textField, constraints);
    }

    private void handleContactSelection() {
        int selectedIndex = contactList.getSelectedIndex();
        if (selectedIndex != -1) {
            Contact contact = contactList.getSelectedValue();
            if (contact != null) {
                nameField.setText(contact.getName());
                phoneNumberField.setText(contact.getPhoneNumber());
                emailField.setText(contact.getEmail());

                editButton.setEnabled(true);
                removeButton.setEnabled(true);
            }
        } else {
            nameField.setText("");
            phoneNumberField.setText("");
            emailField.setText("");

            editButton.setEnabled(false);
            removeButton.setEnabled(false);
        }
    }

    private void displayContacts() {
        listModel.clear();
        List<Contact> contacts = phonebook.getAllContacts();
        for (Contact contact : contacts) {
            listModel.addElement(contact);
        }
    }

    private void saveContacts() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("contacts.txt"))) {
            List<Contact> contacts = phonebook.getAllContacts();
            for (Contact contact : contacts) {
                writer.write(contact.getName() + ";" + contact.getPhoneNumber() + ";" + contact.getEmail());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadContacts() {
        try (BufferedReader reader = new BufferedReader(new FileReader("contacts.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    String name = parts[0];
                    String phoneNumber = parts[1];
                    String email = parts[2];
                    Contact contact = new Contact(name, phoneNumber, email);
                    phonebook.addContact(contact);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                PhonebookLoginUI loginUI = new PhonebookLoginUI();
                loginUI.setVisible(true);
            }
        });
    }
}

class Contact {
    private String name;
    private String phoneNumber;
    private String email;

    public Contact(String name, String phoneNumber, String email) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Phone: " + phoneNumber + ", Email: " + email;
    }
}

class Phonebook {
    private List<Contact> contacts;

    public Phonebook() {
        contacts = new ArrayList<>();
    }

    public void addContact(Contact contact) {
        contacts.add(contact);
    }

    public void editContact(int index, Contact contact) {
        if (index >= 0 && index < contacts.size()) {
            contacts.set(index, contact);
        }
    }

    public void removeContact(int index) {
        if (index >= 0 && index < contacts.size()) {
            contacts.remove(index);
        }
    }

    public List<Contact> getAllContacts() {
        return contacts;
    }
}
