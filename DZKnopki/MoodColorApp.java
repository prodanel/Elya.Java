package DZKnopki;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MoodColorApp {
    private JFrame frame;
    private JLabel moodLabel;
    private JButton redButton, blueButton, greenButton;

    public MoodColorApp() {
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        frame = new JFrame("Цвет настроения");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(400, 300);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        redButton = new JButton("Красный");
        blueButton = new JButton("Синий");
        greenButton = new JButton("Зелёный");

        moodLabel = new JLabel("Выберите цвет настроения", SwingConstants.CENTER);
        moodLabel.setFont(new Font("Arial", Font.BOLD, 16));
        moodLabel.setOpaque(true);
        moodLabel.setBackground(Color.WHITE);

        buttonPanel.add(redButton);
        buttonPanel.add(blueButton);
        buttonPanel.add(greenButton);

        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.add(moodLabel, BorderLayout.SOUTH);

        setupListeners();

        frame.setVisible(true);
    }

    private void setupListeners() {
        ActionListener colorChangeListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton source = (JButton) e.getSource();
                String colorName = source.getText();

                switch (colorName) {
                    case "Красный":
                        frame.getContentPane().setBackground(Color.RED);
                        moodLabel.setText("Ваш цвет настроения: Красный");
                        break;
                    case "Синий":
                        frame.getContentPane().setBackground(Color.BLUE);
                        moodLabel.setText("Ваш цвет настроения: Синий");
                        break;
                    case "Зелёный":
                        frame.getContentPane().setBackground(Color.GREEN);
                        moodLabel.setText("Ваш цвет настроения: Зелёный");
                        break;
                }
            }
        };

        ActionListener consoleListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton source = (JButton) e.getSource();
                System.out.println("Кнопка \"" + source.getText() + "\" была нажата");
            }
        };

        MouseListener mouseHoverListener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {
                JButton button = (JButton) e.getSource();
                JOptionPane.showMessageDialog(frame,
                        "Ты навёл курсор на кнопку \"" + button.getText() + "\"!","Внимание!",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            @Override
            public void mouseExited(MouseEvent e) {}
        };

        JButton[] buttons = {redButton, blueButton, greenButton};

        for (JButton button : buttons) {
            button.addActionListener(colorChangeListener);
            button.addActionListener(consoleListener);
            button.addMouseListener(mouseHoverListener);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MoodColorApp();
            }
        });
    }
}
