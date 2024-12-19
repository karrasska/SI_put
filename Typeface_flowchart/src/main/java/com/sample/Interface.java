package com.sample;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.kie.api.runtime.KieSession;

public class Interface {
    private KieSession kSession;
    private JFrame frame;
    private JLabel questionLabel;
    private JPanel buttonPanel;

    public Interface(KieSession kSession) {
        this.kSession = kSession;

        // Tworzenie g³ównego okna
        frame = new JFrame("Expert System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 200);
        frame.setLayout(new BorderLayout());

        // Label na pytania
        questionLabel = new JLabel("Question will appear here", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        frame.add(questionLabel, BorderLayout.CENTER);

        // Panel na przyciski odpowiedzi
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        frame.add(buttonPanel, BorderLayout.SOUTH);
    }

    // Metoda do rozpoczêcia GUI
    public void start() {
        frame.setVisible(true);
        kSession.setGlobal("gui", this); // Udostêpnienie GUI dla regu³ 
        kSession.fireAllRules();              // Uruchomienie wnioskowania
    }

    // Wyœwietlenie pytania z opcjami odpowiedzi
    public void displayQuestion(String question, String... options) {
        // Ustawianie pytania
        questionLabel.setText(question);

        // Usuwanie starych przycisków
        buttonPanel.removeAll();

        // Tworzenie przycisków dla opcji
        for (String option : options) {
            JButton button = new JButton(option);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Wstawienie odpowiedzi jako fakt typu Answer i kontynuacja wnioskowania
                    kSession.insert(new Answer(question, option));
                    kSession.fireAllRules();
                }
            });
            buttonPanel.add(button);
        }

        // Odœwie¿anie interfejsu
        buttonPanel.revalidate();
        buttonPanel.repaint();
    }
    
    public void shutdown() {
        if (frame != null) {
            frame.dispose(); 
            System.out.println("GUI has been shut down.");
        } else {
            System.out.println("GUI is not initialized.");
        }
    }
   
    public void displayImage(String imageName) {
        String imagePath = "images/" + imageName + ".png";

        // Attempt to load the image resource
        java.net.URL imageUrl = getClass().getClassLoader().getResource(imagePath);

        if (imageUrl == null) {
            System.err.println("Image not found: " + imagePath);
            return;
        }
        frame.getContentPane().removeAll();
        try {
            final ImageIcon icon = new ImageIcon(imageUrl);
            JLabel imageLabel = new JLabel(icon);

            JPanel imagePanel = new JPanel();
            imagePanel.setLayout(new BorderLayout());
            imagePanel.add(imageLabel, BorderLayout.CENTER);

            frame.getContentPane().add(imagePanel, BorderLayout.CENTER);

            
            frame.revalidate();
            frame.repaint();
        } catch (Exception e) {
            System.err.println("Error displaying image: " + e.getMessage());
            e.printStackTrace();
        }
    }
}


