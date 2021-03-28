package VaccinationCenter.GUI;

import Simulation.Interfaces.IObserver;

import javax.swing.*;

public class MainWindow extends JFrame implements IObserver {
    private JPanel rootPanel;
    private Controller app;

    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;

    public MainWindow(Controller app) {
        this.app = app;
        app.subscribeToSimCore(this);
        add(rootPanel);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
        app.run();
    }

    @Override
    public void update(Object o) {
        System.out.println("This is from Main Window");
    }
}
