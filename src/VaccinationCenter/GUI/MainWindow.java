package VaccinationCenter.GUI;

import Simulation.Interfaces.IObserver;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
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
    private JPanel customersTab;
    private JList list2;
    private JTable empTable;
    private JLabel customersLabel;
    private JTextField customersTF;
    private JTextField exp2CustTF;
    private JTextField exp2RepTF;
    private JTable exp2Table;
    private JButton exp2BTN;
    private JPanel employeeTab;
    private JPanel chartPane;
    private JTextField maxDocTF;
    private JTextField repExp3TF;
    private JTextField minDocTF;
    private JButton exp3BTN;
    private JTable exp3Table;
    private JScrollPane exp3;
    private JButton refreshButton;
    private JLabel ciL;
    private JPanel chartTab;

    private int m_workers;
    private int m_doctors;
    private int m_nurses;
    private DefaultTableModel tableModel;
    private  XYSeries xyseries;
    private XYSeriesCollection datasetXY;
    JFreeChart chart;
    DefaultTableModel tableModelEXP3;
    DefaultTableModel tableModelEXP2;


    public MainWindow(Controller app) {
        this.app = app;

        add(rootPanel);
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); //Windows Look and feel
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        setBounds(300,100, 1250, 900);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
        pause = false;
        running = false;
        pauseBTN.setEnabled(false);
        systemTime = 8*60*60.0;

        String[] columnNames = {"Employee", "Count", "Utilization"};
        tableModel = new DefaultTableModel(columnNames, m_workers + m_nurses + m_doctors);
        tableModelEXP2 = new DefaultTableModel(columnNames, 3);

        //JTableHeader header = new JTableHeader(columnNames, 3);
        exp2Table.setModel(tableModel);
        //exp2Table.setAutoCreateRowSorter(true);

        m_workers = 1;
        m_doctors = 1;
        m_nurses = 1;

        String[] columnNamesEXP3 = {"Doctors", "Average Q length"};
        tableModelEXP3 = new DefaultTableModel(columnNamesEXP3, 0);
        //tableModelEXP2 = new DefaultTableModel(columnNamesEXP2, 3);


        //JTableHeader header = new JTableHeader(columnNames, 3);
        exp3Table.setModel(tableModelEXP3);

        xyseries = new XYSeries("Average examination queue length");
        datasetXY = new XYSeriesCollection(xyseries);
        chart = ChartFactory.createXYLineChart(
                "Average medical examination Queue length",
                "Doctors count",
                "Average queue len",
                datasetXY,
                PlotOrientation.VERTICAL,
                true, true, false
        );
        ChartPanel chartPanel = new ChartPanel(chart, false);
        chartPanel.setPreferredSize(new Dimension(450, 300));
        chartPane.add(chartPanel);

        runBTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                m_workers = Integer.parseInt(adminWorkTF.getText());
                m_doctors = Integer.parseInt(doctorsTF.getText());
                m_nurses = Integer.parseInt(nursesTF.getText());
                String[] columnNames = {"Employee", "Count", "Utilization"};
                tableModel = new DefaultTableModel(columnNames, m_workers + m_nurses + m_doctors);
                if(running) {
                    app.stopSim();
                    runBTN.setText("Run");
                    running = false;
                    pauseBTN.setEnabled(false);
                } else {
                    runSimulation(Integer.parseInt(replicationsTF.getText()),
                            Integer.parseInt(seedTF.getText()), Integer.parseInt(adminWorkTF.getText()),
                            Integer.parseInt(doctorsTF.getText()), Integer.parseInt(nursesTF.getText()),
                            Integer.parseInt(repTimeTB.getText()), slider1.getValue() + 1, Integer.parseInt(customersTF.getText()));
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
        exp3BTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runExperiment3();
            }
        });
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(int i = tableModelEXP3.getRowCount(); i > 0 ; i--) {
                    tableModelEXP3.removeRow(i - 1);
                }

            }
        });
        exp2BTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runExperiment2();
            }
        });
    }

    private void runExperiment3() {
        app.subscribeToSimCore(this);
        app.experiment3(Integer.parseInt(minDocTF.getText()), Integer.parseInt(maxDocTF.getText()),
                Integer.parseInt(repExp3TF.getText()), Integer.parseInt(customersTF.getText()),
                Integer.parseInt(adminWorkTF.getText()), Integer.parseInt(nursesTF.getText()),
                Integer.parseInt(repTimeTB.getText()));
    }
    private void runExperiment2() {
        LinkedList<String> list = app.experiment2(Integer.parseInt(doctorsTF.getText()),
                Integer.parseInt(exp2RepTF.getText()), Integer.parseInt(exp2CustTF.getText()),
                Integer.parseInt(adminWorkTF.getText()), Integer.parseInt(nursesTF.getText()),
                Integer.parseInt(repTimeTB.getText()));
        int i = 0;
        int j = 0;
        try {
            for (String string : list) {
                for (Object obj : parseExp2(string)) {
                    tableModelEXP2.setValueAt(obj, i, j);
                    j++;
                }
                j = 0;
                i++;
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        exp2Table.setModel(tableModelEXP2);
    }

    private void runSimulation(int numberOfReplications, int seed,
                               int numOfAdminWorkers, int numOfDoctors, int numOfNurses, double repTime,
                               int speed, int maxCustomers) {

        empRegL.setText("Admin workers: " + numOfAdminWorkers);
        empMedL.setText("Doctors: " + numOfDoctors);
        empVaccL.setText("Nursers: " + numOfNurses);

        app.init(numberOfReplications, seed, numOfAdminWorkers, numOfDoctors, numOfNurses, repTime, speed, maxCustomers);
        app.setTurbo(turboCB.isSelected());
        app.subscribeToSimCore(this);
        app.run();
    }

    @Override
    public void update(Object o) {
        if(o == null) return;

        if(app.getExp3run() ) {
            if(o instanceof HashMap) {
                refreshEXP3((HashMap<String, Double>) o);
            }
            return;
        }

        if(o instanceof HashMap) {
            refreshQueues((HashMap<String, Double>)o);
        }
        if(o instanceof LinkedList && !turboCB.isSelected()) {
            refreshEmployees((LinkedList<String>)o);
        }
        if(o instanceof ArrayList && !turboCB.isSelected()){
            refreshCustomers((ArrayList<String>)o);
        }

    }

    private void refreshQueues(HashMap<String, Double> stats) {
        double tmpTime = systemTime + stats.get("ActualSimulationTime");

        int seconds = (int)(tmpTime % 60);
        int minutes = (int)(tmpTime / 60);
        int hours = (minutes / 60) % 24;
        minutes = minutes % 60;

        replicationL.setText(String.format("Actual replication: %.0f" , stats.get("CompleteReplications") + 1));

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
            avgPplInWRL.setText(String.format("Avg waiting room size: %.4f ", stats.get("WaitingRoomSizeGlobal") / replications ));
            updateConfidanceInterval(stats.get("WaitingRoomSizeGlobal"), stats.get("WaitingRoomSizeGlobalSquared"), replications);


        } else {
            systemTimeL.setText(String.format("System time: %02d : %02d : %02d", hours, minutes, seconds));
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
            avgPplInWRL.setText(String.format("Avg waiting room size: %.4f ", stats.get("WaitingRoomSizeGlobal") / replications ));
        }
    }

    private void refreshEmployees(LinkedList<String> employees) {

        int i = 0;
        int j = 0;
        try {
            for (String string : employees) {
                for (Object obj : parseEmployeeToRow(string)) {
                    tableModel.setValueAt(obj, i, j);
                    j++;
                }
                j = 0;
                i++;
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        empTable.setModel(tableModel);

    }

    private void refreshCustomers(ArrayList<String> customers) {
        DefaultListModel<String> model = new DefaultListModel<>();
        for (String string : customers) {
            model.addElement(string);
        }
        list2.setModel(model);

    }

    private Object[] parseEmployeeToRow(String string) {
        //String phrase = "the music made   it   hard      to        concentrate";
        String delims = "[ ]+";
        String[] tokens = string.split(delims);
        return new Object[]{tokens[0], tokens[4], tokens[2]};
    }

    private Object[] parseExp2(String string) {
        //String phrase = "the music made   it   hard      to        concentrate";
        String delims = "[ ]+";
        String[] tokens = string.split(delims);
        return new Object[]{tokens[0], tokens[1], tokens[2]};
    }

    private void refreshEXP3(HashMap<String, Double> stats) {
        double replications = stats.get("CompleteReplications");
        double pplInQ = stats.get("ExaminedCustomersGlobal") / replications;
        int docs = (int)(double)stats.get("Doctors");
        //avgPplInMedQL.setText(String.format("Average ppl in Queue: %.4f", stats.get("ExaminedCustomersGlobal") / replications));
        System.out.println("" + docs + ", " + pplInQ);
        xyseries.add(docs, pplInQ);
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of Doctors");
        yAxis.setLabel("Average Queue length");
        xAxis.setAutoRangeIncludesZero(false);
        yAxis.setAutoRangeIncludesZero(false);
        XYPlot plot  = (XYPlot)chart.getPlot();
        xAxis.setTickUnit(new NumberTickUnit(1));
        plot.setRangeAxis(yAxis);
        plot.setDomainAxis(xAxis);

        tableModelEXP3.addRow(new Object[]{docs, pplInQ});
    }

    private void updateConfidanceInterval(double x, double x2, double rep) {
        if(rep < 30) {
            return;
        }
        //rep -= 1;
        double significantPart = 1.0 / ( rep - 1.0);
        double devLeft = significantPart * (x2/ rep);
        double devRight = Math.pow((significantPart * (x / rep)), 2);

        double deviation = Math.sqrt(devLeft  - devRight );
        double leftInterval = (x / rep) - ((deviation * 1.96) / Math.sqrt(rep));
        double rightInterval = (x / rep) + ((deviation * 1.96) / Math.sqrt(rep));
        ciL.setText(String.format("<%.4f , %.4f>", leftInterval, rightInterval));
    }
}
