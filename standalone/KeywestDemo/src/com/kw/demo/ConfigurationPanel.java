package com.kw.demo;

import com.hsq.kw.packet.KeywestPacket;
import com.hsq.kw.packet.vo.Configuration;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ConfigurationPanel extends JPanel {

    private javax.swing.JTextField bandwidth;
    private javax.swing.JTextField mode;
    private javax.swing.JTextField channel;
    private javax.swing.JTextField ipType;
    private javax.swing.JButton apply;
    private javax.swing.JButton refresh;
    private javax.swing.JLabel ssid;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;

    public ConfigurationPanel() {
        initComponents();
        prepareUI();
    }

    private void initComponents() {
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        bandwidth = new javax.swing.JTextField(20);
        ssid = new javax.swing.JLabel();
        mode = new javax.swing.JTextField(20);
        channel = new javax.swing.JTextField(20);
        ipType = new javax.swing.JTextField(20);
        apply = new javax.swing.JButton();
        refresh = new javax.swing.JButton();
        refresh.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    sendConfigRequest();
                } catch (IOException e1) {
                }
            }
        });

        jLabel5.setText("SSID");
        jLabel6.setText("Channel Bandwidth");
        jLabel7.setText("Mode");
        jLabel8.setText("Channel");
        jLabel9.setText("IP Address Type");
        ssid.setText("jLabel10");
        refresh.setText("Refresh");
        apply.setText("Apply");

    }

    public void sendConfigRequest() throws IOException {
        byte[] buffer = new byte[1307];

        DatagramSocket socket = new DatagramSocket();
        KeywestPacket packet = new ResponseHandler().sendConfigRequest();
        byte[] requestArr = packet.toByteArray();
        System.out.println("Sending packet from " + socket.getLocalPort());
        DatagramPacket dPacket = new DatagramPacket(requestArr, requestArr.length, InetAddress.getByName("localhost"), 9876);
        socket.send(dPacket);
        System.out.println("packet sent");
        DatagramPacket rPacket = new DatagramPacket(buffer, buffer.length);
        socket.receive(rPacket);
        KeywestPacket rKwPacket = new KeywestPacket(rPacket.getData());
        System.out.println(rKwPacket);
        Configuration config = new Configuration(rKwPacket);
        socket.close();
        ssid.setText(config.getSsid());
        bandwidth.setText(String.valueOf(config.getChannelBW()));
        mode.setText(String.valueOf(config.getMode()));
        channel.setText(config.getChannel()+"");
        //ssid.setText(rKwPacket.);
    }

    private void prepareUI() {
        javax.swing.GroupLayout configurationLayout = new javax.swing.GroupLayout(this);
        this.setLayout(configurationLayout);
        configurationLayout.setHorizontalGroup(
                configurationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(configurationLayout.createSequentialGroup()
                                .addGap(241, 241, 241)
                                .addGroup(configurationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(configurationLayout.createSequentialGroup()
                                                .addGroup(configurationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel6)
                                                        .addComponent(jLabel5)
                                                        .addComponent(jLabel7)
                                                        .addComponent(jLabel8)
                                                        .addComponent(jLabel9))
                                                .addGap(82, 82, 82)
                                                .addGroup(configurationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(ipType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(channel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(mode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(ssid)
                                                        .addComponent(bandwidth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(configurationLayout.createSequentialGroup()
                                                .addGap(81, 81, 81)
                                                .addComponent(refresh)
                                                .addGap(52, 52, 52)
                                                .addComponent(apply)))
                                .addContainerGap(252, Short.MAX_VALUE))
        );
        configurationLayout.setVerticalGroup(
                configurationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(configurationLayout.createSequentialGroup()
                                .addGap(108, 108, 108)
                                .addGroup(configurationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(configurationLayout.createSequentialGroup()
                                                .addGap(11, 11, 11)
                                                .addComponent(jLabel5)
                                                .addGap(20, 20, 20))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, configurationLayout.createSequentialGroup()
                                                .addComponent(ssid)
                                                .addGap(31, 31, 31)))
                                .addGroup(configurationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel6)
                                        .addComponent(bandwidth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(24, 24, 24)
                                .addGroup(configurationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel7)
                                        .addComponent(mode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(33, 33, 33)
                                .addGroup(configurationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel8)
                                        .addComponent(channel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(29, 29, 29)
                                .addGroup(configurationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel9)
                                        .addComponent(ipType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
                                .addGroup(configurationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(refresh)
                                        .addComponent(apply))
                                .addGap(66, 66, 66))
        );

    }
}
