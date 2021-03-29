package VaccinationCenter.GUI;

import Simulation.Interfaces.IObserver;
import VaccinationCenter.Employee.Doctor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class MainWindow extends JFrame implements IObserver {
    private JPanel rootPanel;
    private Controller app;
    private boolean pause;
    private boolean running;

    private JTextField seedTF;
    private JTextField replicationsTF;
    private JTextField adminWorkTF;
    private JTextField doctorsTF;
    private JButton runBTN;
    private JButton pauseBTN;
    private JTextField nursesTF;
    private JLabel pplInRegQL;
    private JLabel avgTimeInRegQL;
    private JLabel avgPplInRegQL;
    private JLabel empRegL;
    private JLabel availWorkL;
    private JLabel utilRegL;
    private JLabel pplInMedQL;
    private JLabel avgPplInMedQL;
    private JLabel avgTimeInMedQ;
    private JLabel empMedL;
    private JLabel availDocL;
    private JLabel utilDocL;
    private JLabel pplInVaccQL;
    private JLabel avgPplInVaccQL;
    private JLabel avgTimeInVaccQL;
    private JLabel empVaccL;
    private JLabel availNurL;
    private JLabel utilNurL;
    private JLabel pplInWRL;
    private JLabel avgPplInWRL;
    private JLabel avgTimeInWRL;
    private JCheckBox turboCB;
    private JTextField repTimeTB;

    public MainWindow(Controller app) {
        this.app = app;

        add(rootPanel);
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); //Windows Look and feel
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        setBounds(300,100, 1100, 750);
        this.setVisible(true);
        pause = false;
        running = false;
        pauseBTN.setEnabled(false);

        runBTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(running) {
                    app.stopSim();
                    runBTN.setText("Run");
                    running = false;
                    pauseBTN.setEnabled(false);
                } else {
                    runSimulation(Integer.parseInt(replicationsTF.getText()),
                            Integer.parseInt(seedTF.getText()), Integer.parseInt(adminWorkTF.getText()),
                            Integer.parseInt(doctorsTF.getText()), Integer.parseInt(nursesTF.getText()),
                            Integer.parseInt(repTimeTB.getText()));
                    pauseBTN.setEnabled(true);
                    runBTN.setText("Stop");
                    running = true;
                }
            }
        });
        pauseBTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(pause) {
                    pause = false;
                    pauseBTN.setText("Pause");
                    app.resumeSim();
                } else {
                    pause = true;
                    pauseBTN.setText("Resume");
                    app.pauseSim();
                }
            }
        });
    }

    private void runSimulation(int numberOfReplications, int seed,
                               int numOfAdminWorkers, int numOfDoctors, int numOfNurses, double repTime) {

        empRegL.setText("Admin workers: " + numOfAdminWorkers);
        empMedL.setText("Doctors: " + numOfDoctors);
        empVaccL.setText("Nursers: " + numOfNurses);
        app.init(numberOfReplications, seed, numOfAdminWorkers, numOfDoctors, numOfNurses, repTime);
        app.subscribeToSimCore(this);
        app.run();
    }

    @Override
    public void update(Object o) {
        if(o == null) return;
        //System.out.println("This is from Main Window: " + ((HashMap<String, Double >)o).get("ActualSimulationTime"));
        HashMap<String, Double> stats = (HashMap<String, Double >)o;
        if(turboCB.isSelected()) {
            avgTimeInRegQL.setText(String.format("Average time in Queue: %.4f", stats.get("SumTimeRegQueueGlobal") / stats.get("CompleteReplications")));
            avgPplInRegQL.setText(String.format("Average ppl in reg Queue: %.4f", stats.get("RegisteredCustomersGlobal") / stats.get("CompleteReplications")));
            utilRegL.setText(String.format("Utilization: %.4f ", stats.get("WorkerUtilizationGlobal") / stats.get("CompleteReplications") * 100) + "%");

            avgTimeInMedQ.setText(String.format("Average time in Queue: %.4f", stats.get("SumTimeMedQueueGlobal") / stats.get("CompleteReplications")));
            avgPplInMedQL.setText(String.format("Average ppl in Queue: %.4f", stats.get("ExaminedCustomersGlobal") / stats.get("CompleteReplications")));
            utilDocL.setText(String.format("Utilization: %.4f ", stats.get("DoctorUtilizationGlobal") / stats.get("CompleteReplications") * 100) + "%");

            avgTimeInVaccQL.setText(String.format("Average time in Queue: %.4f" , stats.get("SumTimeVaccQueueGlobal") / stats.get("CompleteReplications")));
            avgPplInVaccQL.setText(String.format("Average ppl in Queue: %.4f ", stats.get("VaccinatedCustomersGlobal") / stats.get("CompleteReplications")));
            utilNurL.setText(String.format("Utilization: %.4f ", stats.get("NurseUtilizationGlobal") / stats.get("CompleteReplications") * 100) + "%");

        } else {
            utilRegL.setText(String.format("Utilization: %.4f ", (stats.get("AdminWorkersUtilization") / stats.get("ActualSimulationTime") * 100)) + "%");
            availWorkL.setText("Available: " + stats.get("AdminWorkersAvailability"));
            pplInRegQL.setText("Ppl in Queue: " + stats.get("RegQueueSize"));
            avgTimeInRegQL.setText(String.format("Average time in Queue: %.4f", stats.get("SumTimeRegQueue") / stats.get("RegisteredCustomers")));
            avgPplInRegQL.setText(String.format("Average ppl in Queue: %.4f", stats.get("SumTimeRegQueue") / stats.get("ActualSimulationTime")));

            utilDocL.setText(String.format("Utilization: %.4f", stats.get("DoctorsUtilization") / stats.get("ActualSimulationTime") * 100) + "%");
            availDocL.setText("Available: " + stats.get("DoctorsAvailability"));
            pplInMedQL.setText("Ppl in Queue: " + stats.get("MedQueueSize"));
            avgTimeInMedQ.setText(String.format("Average time in Queue: %.4f", stats.get("SumTimeMedQueue") / stats.get("ExaminedCustomers")));
            avgPplInMedQL.setText(String.format("Average ppl in Queue: %.4f", stats.get("SumTimeMedQueue") / stats.get("ActualSimulationTime")));

            utilNurL.setText(String.format("Utilization: %.4f", stats.get("NursesUtilization") / stats.get("ActualSimulationTime") * 100) + "%");
            availNurL.setText("Available: " + stats.get("NursesAvailability"));
            pplInVaccQL.setText("Ppl in Queue: " + stats.get("VaccQueueSize"));
            avgTimeInVaccQL.setText(String.format("Average time in Queue: %.4f", stats.get("SumTimeVaccQueue") / stats.get("VaccinatedCustomers")));
            avgPplInVaccQL.setText(String.format("Average ppl in Queue: %.4f", stats.get("SumTimeVaccQueue") / stats.get("ActualSimulationTime")));

            pplInWRL.setText("Ppl in waiting room: " + stats.get("WaitingRoomSize"));
        /*avgTimeInVaccQL.setText("Average time in Queue: " + stats.get("SumTimeVaccQueue") / stats.get("VaccinatedCustomers"));
        avgPplInVaccQL.setText("Average ppl in reg Queue: " + stats.get("SumTimeVaccQueue")  / stats.get("ActualSimulationTime"));*/
        }
    }
}
