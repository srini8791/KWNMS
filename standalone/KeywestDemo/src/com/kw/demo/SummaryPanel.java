package com.kw.demo;

import javax.swing.*;

public class SummaryPanel extends JPanel {

    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel radio1GPS;
    private javax.swing.JLabel radio1IP;
    private javax.swing.JLabel radio1MAC;
    private javax.swing.JLabel radio1Rate;
    private javax.swing.JLabel radio2GPS;
    private javax.swing.JLabel radio2IP;
    private javax.swing.JLabel radio2MAC;
    private javax.swing.JLabel radio2Rate;

    public SummaryPanel() {
        initComponents();
        prepareUI();
    }

    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        radio1IP = new javax.swing.JLabel();
        radio2IP = new javax.swing.JLabel();
        radio1MAC = new javax.swing.JLabel();
        radio2MAC = new javax.swing.JLabel();
        radio1GPS = new javax.swing.JLabel();
        radio2GPS = new javax.swing.JLabel();
        radio1Rate = new javax.swing.JLabel();
        radio2Rate = new javax.swing.JLabel();

        jLabel1.setText("IP Address");
        jLabel2.setText("MAC");
        jLabel3.setText("GPS");
        jLabel4.setText("Rate");
        radio1IP.setText("192.168.1.9");
        radio2IP.setText("192.168.1.8");
        radio1MAC.setText("00:00:00:00:00:00");
        radio2MAC.setText("00:00:00:00:00:00");
        radio1GPS.setText("12` 33` 1234N");
        radio2GPS.setText("12` 33` 1234N");
        radio1Rate.setText("300 MBPS");
        radio2Rate.setText("300 MBPS");
    }

    private void prepareUI() {
        radio1GPS.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        radio1GPS.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        radio2GPS.setToolTipText("");
        radio2GPS.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        radio2GPS.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        GraphPanel graphPanel = new GraphPanel();
        javax.swing.GroupLayout summaryPageLayout = new javax.swing.GroupLayout(this);
        this.setLayout(summaryPageLayout);
        summaryPageLayout.setHorizontalGroup(
                summaryPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(summaryPageLayout.createSequentialGroup()
                                .addGap(49, 49, 49)
                                .addGroup(summaryPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(graphPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(summaryPageLayout.createSequentialGroup()
                                                .addGroup(summaryPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel1)
                                                        .addComponent(jLabel2)
                                                        .addComponent(jLabel3)
                                                        .addComponent(jLabel4))
                                                .addGap(114, 114, 114)
                                                .addGroup(summaryPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(radio1IP)
                                                        .addComponent(radio1MAC)
                                                        .addComponent(radio1GPS)
                                                        .addComponent(radio1Rate))
                                                .addGap(144, 144, 144)
                                                .addGroup(summaryPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(radio2Rate)
                                                        .addComponent(radio2MAC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(radio2IP)
                                                        .addComponent(radio2GPS, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addContainerGap(123, Short.MAX_VALUE))
        );
        summaryPageLayout.setVerticalGroup(
                summaryPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(summaryPageLayout.createSequentialGroup()
                                .addGap(74, 74, 74)
                                .addGroup(summaryPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(radio1IP)
                                        .addComponent(radio2IP))
                                .addGap(30, 30, 30)
                                .addGroup(summaryPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(radio1MAC)
                                        .addComponent(radio2MAC))
                                .addGap(28, 28, 28)
                                .addGroup(summaryPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(summaryPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel3)
                                                .addComponent(radio2GPS, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addComponent(radio1GPS, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(summaryPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4)
                                        .addComponent(radio1Rate)
                                        .addComponent(radio2Rate))
                                .addGap(29, 29, 29)
                                .addComponent(graphPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }



    private class GraphPanel extends JPanel {

        public GraphPanel() {
            initialize();
        }

        private void initialize() {
            javax.swing.GroupLayout graphPanelLayout = new javax.swing.GroupLayout(this);
            this.setLayout(graphPanelLayout);
            graphPanelLayout.setHorizontalGroup(
                    graphPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGap(0, 584, Short.MAX_VALUE)
            );
            graphPanelLayout.setVerticalGroup(
                    graphPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGap(0, 217, Short.MAX_VALUE)
            );
        }

    }
}
