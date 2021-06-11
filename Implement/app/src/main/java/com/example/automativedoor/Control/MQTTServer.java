package com.example.automativedoor.Control;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.Charset;

public class MQTTServer {
    final String serverUri ="tcp://io.adafruit.com:1883";
    String username;
    String password;
    String clientId;
    String topic;

    public MqttAndroidClient mqttAndroidClient;

    public MQTTServer(Context context, String clientId, String username, String password, String topic) {
        this.clientId = clientId;
        this.username = username;
        this.password = password;
        this.topic = topic;
        this.mqttAndroidClient = new MqttAndroidClient(context, this.serverUri, this.clientId);
        this.mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                Log.w("mqtt", clientId);
            }

            @Override
            public void connectionLost(Throwable cause) {
                Log.w("Lost mqtt", "connection");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
        connect();
    }

    public void setCallback(MqttCallbackExtended callback) {
        this.mqttAndroidClient.setCallback(callback);
    }

    private void connect() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setUserName(this.username);
        mqttConnectOptions.setPassword(this.password.toCharArray());
        try {
            this.mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                    if (!topic.isEmpty()) subcribeToTopic();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w("Mqtt", "Failedtoconnectto:" + serverUri + exception.toString());
                }
            });

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void subcribeToTopic() {
        try {
            mqttAndroidClient.subscribe(this.topic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.w("Mqtt", "Subscribed! " + topic);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w("Mqtt", "Subscribedfail " + topic);
                }
            });

        } catch (MqttException e) {
            Log.e("Exceptionstsubscribing", e.toString());
            e.printStackTrace();
        }
    }

    public void unsubscribeToTopic(String topic) {
        try {
            mqttAndroidClient.unsubscribe(topic);
        } catch (MqttException e) {
            Log.e("Topic not exist", e.toString());
        }
    }

    public void publishMqtt(String message, String feedPath) {
        MqttMessage msg = new MqttMessage();
        msg.setId(1234);
        msg.setQos(0);
        msg.setRetained(true);

        byte[] b = message.getBytes(Charset.forName("UTF-8"));
        msg.setPayload(b);

        try {
            this.mqttAndroidClient.publish(feedPath, msg);
            this.mqttAndroidClient.unsubscribe(feedPath);
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }
    }

}
