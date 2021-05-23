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
    final String clientId ="abcxyz";
    final String username ="CongTuVu";
    final String password ="aio_AwHu03uY2aZmyeg92ojd3vB2zXn4";

    public MqttAndroidClient mqttAndroidClient;

    public MQTTServer(Context context) {
        this.mqttAndroidClient = new MqttAndroidClient(context, this.serverUri, this.clientId);
        connect();
        this.mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                Log.w("mqtt", serverURI);
            }

            @Override
            public void connectionLost(Throwable cause) {
                Log.w("mqtt", "lost connection");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
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

    public void subscribeToTopic(String subscriptionTopic) {
        try {
            mqttAndroidClient.subscribe(subscriptionTopic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.w("Mqtt", "Subscribed!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w("Mqtt", "Subscribedfail");
                }
            });

        } catch (MqttException e) {
            Log.e("Exceptionstsubscribing", e.toString());
            e.printStackTrace();
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
