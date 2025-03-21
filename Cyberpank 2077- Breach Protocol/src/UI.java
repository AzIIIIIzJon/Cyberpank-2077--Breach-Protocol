import javax.swing.*;

import java.awt.*;

public class UI extends JFrame{
    public static final Color BASE = new Color(22,22,22);
    private static final UI instance = new UI();

    private UI(){
        int width  = 1000, height = 750;
        // Define the look and behaviour of the window
        setBackground(BASE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Breach Protocol");
        setResizable(false);
        setSize(width, height);
        setLocationRelativeTo(null);

        // add the panel to this window
        Panel panel = Panel.getInstance();
        panel.setSize(width, height);
        add(panel);

        // connect the Controls to this window
        addKeyListener(Controls.getInstance());

        // become visible and request mouse and key focus
        setVisible(true);
        requestFocus();
    }

    public static UI getInstance() { return instance; }
}
