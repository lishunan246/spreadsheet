package com.lishunan.spreadsheet;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by lishunan on 14-12-16.
 */
public class MainWindow extends JFrame implements ActionListener{
    protected JTable table;

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

        table=new JTable(2,2);
        table.setVisible(true);
        this.add(table);

        jMenuBar.setVisible(true);
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

                    String[][] data=new String[list.size()][list.get(0).size()];

                    DefaultTableModel tableModel = new DefaultTableModel();


                    for(int i=0;i<list.size();i++)
                    {
                        for(int j=0;j<list.get(i).size();j++)
                        {
                            if(list.get(i).get(j)==null)
                                data[i][j]="";
                            else
                                data[i][j]=list.get(i).get(j);

                            System.out.print(data[i][j]);
                        }
                    }
                    String[] head = {"q", "e", "r", "t"};
                    this.remove(table);
                    table=new JTable(data,head);

                    //table.setVisible(true);
                    //table.repaint();
                    //table.revalidate();
                    this.add(new JScrollPane(table));
                    this.revalidate();
//
//                    for(CSVRecord record:list)
//                    {
//                        System.out.println(record);
//                        System.out.println(record.getRecordNumber());
//                        System.out.println(record.size());
//
//                    }
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(this,"Can't open file");
                } catch (IOException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(this,"Can't parse file");
                }
            }
        }
    }
}
