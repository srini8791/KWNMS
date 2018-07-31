package com.kw.demo;

import javax.swing.*;

public class LinkTestPanel extends JPanel {

    public LinkTestPanel() {
        initComponents();
        prepareUI();
    }

    private void initComponents() {

    }

    private void prepareUI() {
        javax.swing.GroupLayout linkTestLayout = new javax.swing.GroupLayout(this);
        this.setLayout(linkTestLayout);
        linkTestLayout.setHorizontalGroup(
                linkTestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 756, Short.MAX_VALUE)
        );
        linkTestLayout.setVerticalGroup(
                linkTestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 478, Short.MAX_VALUE)
        );
    }
}
