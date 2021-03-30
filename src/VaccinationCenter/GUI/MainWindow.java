package VaccinationCenter.GUI;

import Simulation.Interfaces.IObserver;
import VaccinationCenter.Employee.Doctor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class MainWindow extends JFrame implements IObserver {
    private JPanel rootPanel;
    private Controller app;
    private boolean pause;
    private boolean running;
    private double systemTime;
    //DefaultListModel<String> model;

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
    private JLabel replicationL;
    private JLabel systemTimeL;
    private JSlider slider1;
    private JLabel simSpeedL;
    private JTabbedPane QueueStats;
    private JList<String> list1;
    private JPanel employeesTab;
    private JPanel customersTab;
    private JList list2;

    public MainWindow(Controller app) {
        this.app = app;

        add(rootPanel);
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); //Windows Look and feel
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        setBounds(300,100, 1100, 900);
        this.setVisible(true);
        pause = false;
        running = false;
        pauseBTN.setEnabled(false);
        systemTime = 8*60*60.0;
        simSpeedL.setText("Simulation speed: " + slider1.getValue());
        /*model = new DefaultListModel<>();

        model.addElement("test");
        list1 = new JList<>(model);

        list1.setModel(model);*/
        //list1.setCellRenderer(new DefaultListCellRenderer());
        //list1.setVisible(true);

        /*JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(list2);
        list2.setLayoutOrientation(JList.VERTICAL);
*/
        //JScrollPane s = new JScrollPane(list2);
        //s.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        //s.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

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
                            Integer.parseInt(repTimeTB.getText()), slider1.getValue());
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
        slider1.addComponentListener(new ComponentAdapter() {
        });
    }

    private void runSimulation(int numberOfReplications, int seed,
                               int numOfAdminWorkers, int numOfDoctors, int numOfNurses, double repTime, int speed) {

        empRegL.setText("Admin workers: " + numOfAdminWorkers);
        empMedL.setText("Doctors: " + numOfDoctors);
        empVaccL.setText("Nursers: " + numOfNurses);

        app.init(numberOfReplications, seed, numOfAdminWorkers, numOfDoctors, numOfNurses, repTime, speed);
        app.setTurbo(turboCB.isSelected());
        app.subscribeToSimCore(this);
        app.run();
    }

    @Override
    public void update(Object o) {
        if(o == null) return;
        if(o instanceof HashMap) {
            //System.out.println("map");
            refreshQueues((HashMap<String, Double>)o);
        }
        if(o instanceof LinkedList) {
            refreshEmployees((LinkedList<String>)o);
        }
        if(o instanceof ArrayList) {
            refreshCustomers((ArrayList<String>)o);
        }

        //System.out.println("This is from Main Window: " + ((HashMap<String, Double >)o).get("ActualSimulationTime"));

    }

    public void refreshQueues(HashMap<String, Double> stats) {
        //HashMap<String, Double> stats = (HashMap<String, Double >)o;
        //systemTime += stats.get("ActualSimulationTime");
        double tmpTime = systemTime + stats.get("ActualSimulationTime");

        int minutes = (int)(tmpTime / 60);
        int hours = (minutes / 60) % 24;
        minutes = minutes % 60;

        replicationL.setText(String.format("Replication: %.0f" , stats.get("CompleteReplications") + 1));
        systemTimeL.setText(String.format("System time: %02d : %02d", hours, minutes));
        //todo
        double replications = stats.get("CompleteReplications");
        if(replications == 0.0) { replications = 1.0; }
        if(turboCB.isSelected()) {
            avgTimeInRegQL.setText(String.format("Average time in Queue: %.4f", stats.get("SumTimeRegQueueGlobal") / replications));
            avgPplInRegQL.setText(String.format("Average ppl in reg Queue: %.4f", stats.get("RegisteredCustomersGlobal") / replications));
            utilRegL.setText(String.format("Utilization: %.4f ", stats.get("WorkerUtilizationGlobal") / replications * 100) + "%");

            avgTimeInMedQ.setText(String.format("Average time in Queue: %.4f", stats.get("SumTimeMedQueueGlobal") / replications));
            avgPplInMedQL.setText(String.format("Average ppl in Queue: %.4f", stats.get("ExaminedCustomersGlobal") / replications));
            utilDocL.setText(String.format("Utilization: %.4f ", stats.get("DoctorUtilizationGlobal") / replications * 100) + "%");

            avgTimeInVaccQL.setText(String.format("Average time in Queue: %.4f" , stats.get("SumTimeVaccQueueGlobal") / replications));
            avgPplInVaccQL.setText(String.format("Average ppl in Queue: %.4f ", stats.get("VaccinatedCustomersGlobal") / replications));
            utilNurL.setText(String.format("Utilization: %.4f ", stats.get("NurseUtilizationGlobal") / replications * 100) + "%");

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
        }
    }

    public void refreshEmployees(LinkedList<String> employees) {
        DefaultListModel<String> model = new DefaultListModel<>();
        //System.out.println("List");
        for (String string : employees) {
            model.addElement(string);
        }
        //model.addElement("test");
        list1.setModel(model);
        list1.setCellRenderer(new DefaultListCellRenderer());
        list1.setVisible(true);

    }

    public void refreshCustomers(ArrayList<String> customers) {
        DefaultListModel<String> model = new DefaultListModel<>();
        //System.out.println("List");
        for (String string : customers) {
            model.addElement(string);
        }
        //model.addElement("test");
        list2.setModel(model);

    }
 }
