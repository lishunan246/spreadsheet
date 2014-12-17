package com.lishunan.spreadsheet;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 * Created by lishunan on 14-12-16.
 */
public class MainWindow extends JFrame implements ActionListener{
    protected JTable table;
    protected JScrollPane jScrollPane = new JScrollPane();

    MainWindow()
    {
        this.setTitle("Spread Sheet");
        this.setSize(800,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //add menuBar
        JMenuBar jMenuBar=new JMenuBar();

        //add File menu
        JMenu jMenu=new JMenu("File");
        jMenuBar.add(jMenu);

        // add open button
        JMenuItem jMenuItem=new JMenuItem("Open");
        jMenuItem.setActionCommand("open");
        jMenuItem.addActionListener(this);
        jMenu.add(jMenuItem);

        jMenuItem = new JMenuItem("Save");
        jMenuItem.setActionCommand("save");
        jMenuItem.addActionListener(this);
        jMenu.add(jMenuItem);

        jMenuBar.setVisible(true);
        this.add(jScrollPane);
        this.setJMenuBar(jMenuBar);
        this.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if("open".equals(e.getActionCommand()))
        {
            JFileChooser jFileChooser=new JFileChooser();
            if(jFileChooser.showOpenDialog(this)==JFileChooser.APPROVE_OPTION)
            {
                File file=jFileChooser.getSelectedFile();

                FileReader reader;
                CSVFormat format=CSVFormat.DEFAULT;
                try {
                    reader = new FileReader(file);
                    CSVParser csvParser=new CSVParser(reader,format);
                    java.util.List<CSVRecord> list=csvParser.getRecords();
                    csvParser.close();

                    String[][] data=new String[list.size()][list.get(0).size()];

                    for(int i=0;i<list.size();i++)
                    {
                        for(int j=0;j<list.get(i).size();j++)
                        {
                            if(list.get(i).get(j)==null)
                                data[i][j]="";
                            else
                                data[i][j]=list.get(i).get(j);
                        }
                    }
                    String[] head = new String[list.get(0).size()];
                    for (int i = 0; i < list.get(0).size(); i++) {
                        head[i] = String.valueOf(i);
                    }
                    
                    DefaultTableModel tableModel = new DefaultTableModel(data, head);
                    table = new JTable(tableModel);
                    this.remove(jScrollPane);
                    jScrollPane = new JScrollPane(table);
                    this.add(jScrollPane);
                    this.revalidate();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(this,"Can't open file");
                } catch (IOException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(this,"Can't parse file");
                }
            }
        } else if ("save".equals(e.getActionCommand())) {
            JFileChooser jFileChooser = new JFileChooser();
            if (jFileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = jFileChooser.getSelectedFile();
                FileWriter writer;
                try {
                    TableModel tableModel = table.getModel();

                    writer = new FileWriter(file);
                    CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);


                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        String[] strings = new String[tableModel.getColumnCount()];
                        for (int j = 0; j < tableModel.getColumnCount(); j++) {
                            strings[j] = (String) tableModel.getValueAt(i, j);

                        }
                        csvPrinter.printRecord(strings);
                    }
                    writer.flush();
                    writer.close();
                    csvPrinter.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
