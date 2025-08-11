package com.mycompany.easykanban;

import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import javax.swing.*;
import javax.swing.border.Border;

/**
 * Centralized UI theme and styling utility class for EasyKanban application.
 * Provides consistent colors, fonts, and styling methods across all UI components.
 */
public class UITheme {
    
    // Primary Color Palette - Modern Blue Theme
    public static final Color PRIMARY_BLUE = new Color(52, 152, 219);      // Bright blue
    public static final Color PRIMARY_DARK = new Color(41, 128, 185);      // Darker blue
    public static final Color SECONDARY_BLUE = new Color(174, 214, 241);   // Light blue
    public static final Color ACCENT_BLUE = new Color(133, 193, 233);      // Medium blue
    
    // Neutral Colors
    public static final Color WHITE = new Color(255, 255, 255);
    public static final Color LIGHT_GRAY = new Color(248, 249, 250);
    public static final Color MEDIUM_GRAY = new Color(108, 117, 125);
    public static final Color DARK_GRAY = new Color(52, 58, 64);
    public static final Color BLACK = new Color(33, 37, 41);
    
    // Status Colors
    public static final Color SUCCESS_GREEN = new Color(40, 167, 69);
    public static final Color WARNING_ORANGE = new Color(255, 193, 7);
    public static final Color ERROR_RED = new Color(220, 53, 69);
    public static final Color INFO_CYAN = new Color(23, 162, 184);
    
    // Fonts
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font SUBHEADER_FONT = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    
    // Component Dimensions
    public static final Dimension BUTTON_SIZE = new Dimension(140, 35);
    public static final Dimension INPUT_SIZE = new Dimension(200, 30);
    public static final Dimension PANEL_PADDING = new Dimension(20, 20);
    
    /**
     * Applies primary button styling with hover effects
     */
    public static void stylePrimaryButton(JButton button) {
        button.setBackground(PRIMARY_BLUE);
        button.setForeground(WHITE);
        button.setFont(BUTTON_FONT);
        button.setPreferredSize(BUTTON_SIZE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_DARK);
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_BLUE);
            }
        });
    }
    
    /**
     * Applies secondary button styling with hover effects
     */
    public static void styleSecondaryButton(JButton button) {
        button.setBackground(LIGHT_GRAY);
        button.setForeground(DARK_GRAY);
        button.setFont(BUTTON_FONT);
        button.setPreferredSize(BUTTON_SIZE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(MEDIUM_GRAY, 1));
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(MEDIUM_GRAY);
                button.setForeground(WHITE);
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(LIGHT_GRAY);
                button.setForeground(DARK_GRAY);
            }
        });
    }
    
    /**
     * Applies consistent input field styling
     */
    public static void styleInputField(JTextField field) {
        field.setFont(BODY_FONT);
        field.setPreferredSize(INPUT_SIZE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MEDIUM_GRAY, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        field.setBackground(WHITE);
        field.setForeground(BLACK);
    }
    
    /**
     * Applies consistent password field styling
     */
    public static void stylePasswordField(JPasswordField field) {
        field.setFont(BODY_FONT);
        field.setPreferredSize(INPUT_SIZE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MEDIUM_GRAY, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        field.setBackground(WHITE);
        field.setForeground(BLACK);
    }
    
    /**
     * Applies consistent label styling
     */
    public static void styleLabel(JLabel label) {
        label.setFont(BODY_FONT);
        label.setForeground(DARK_GRAY);
    }
    
    /**
     * Applies header label styling
     */
    public static void styleHeaderLabel(JLabel label) {
        label.setFont(HEADER_FONT);
        label.setForeground(PRIMARY_DARK);
    }
    
    /**
     * Applies title label styling
     */
    public static void styleTitleLabel(JLabel label) {
        label.setFont(TITLE_FONT);
        label.setForeground(BLACK);
    }
    
    /**
     * Applies clickable link label styling
     */
    public static void styleLinkLabel(JLabel label) {
        label.setFont(BODY_FONT);
        label.setForeground(PRIMARY_BLUE);
        label.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        // Add hover effect
        label.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                label.setForeground(PRIMARY_DARK);
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                label.setForeground(PRIMARY_BLUE);
            }
        });
    }
    
    /**
     * Applies consistent panel styling
     */
    public static void stylePanel(JPanel panel) {
        panel.setBackground(WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }
    
    /**
     * Applies primary panel styling with colored background
     */
    public static void stylePrimaryPanel(JPanel panel) {
        panel.setBackground(SECONDARY_BLUE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
    }
    
    /**
     * Applies sidebar panel styling
     */
    public static void styleSidebarPanel(JPanel panel) {
        panel.setBackground(LIGHT_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
    }
    
    /**
     * Creates a rounded border for components
     */
    public static Border createRoundedBorder(Color color, int thickness) {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, thickness),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        );
    }
    
    /**
     * Applies consistent frame styling
     */
    public static void styleFrame(JFrame frame, String title) {
        frame.setTitle(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.getContentPane().setBackground(WHITE);
        
        // Set application icon if available
        try {
            ImageIcon icon = new ImageIcon("icon.png");
            frame.setIconImage(icon.getImage());
        } catch (Exception e) {
            // Icon not found, continue without it
        }
    }
    
    /**
     * Shows a styled success message dialog
     */
    public static void showSuccessMessage(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Shows a styled error message dialog
     */
    public static void showErrorMessage(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Shows a styled warning message dialog
     */
    public static void showWarningMessage(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
    }
}