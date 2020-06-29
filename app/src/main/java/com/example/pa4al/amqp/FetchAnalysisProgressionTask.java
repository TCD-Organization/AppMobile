package com.example.pa4al.amqp;

import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.pa4al.gson.GsonCustom;
import com.example.pa4al.model.AnalysisProgress;
import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;
public class FetchAnalysisProgressionTask extends AsyncTask<FetchAnalysisProgressionParameter, AnalysisProgress, AnalysisProgress> {

    private Connection connection;
    private Channel channel;
    private TextView progressionTextView;
    private String analysisId;

    public static final String EXCHANGE = "type.id.tx";

    public FetchAnalysisProgressionTask() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("51.178.18.199");
        factory.setPort(5672);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
        } catch (IOException e) {
            e.printStackTrace();
            closeConnection(channel, connection);
        } catch (TimeoutException e) {
            System.out.println("Server unreachable");
            e.printStackTrace();
            closeConnection(channel, connection);
        }

    }

        /*
        TimerTask readMessagesTask() {
            return new TimerTask() {
                public void run() {
                    if (channel.messageCount(FRONT_ANALYSIS_QUEUE) > 0) {

                        String test = channel.basicConsume(FRONT_ANALYSIS_QUEUE, true, consumer);
                        System.out.println("test" + test);
                    }
                }
            };
        }
        */

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected AnalysisProgress doInBackground(FetchAnalysisProgressionParameter... params) {
        progressionTextView = params[0].tv;
        analysisId = params[0].id;
        String FRONT_ANALYSIS_QUEUE = "analysis_"+ analysisId +"_q";

        try {
            channel.queueDeclare(FRONT_ANALYSIS_QUEUE, true, false, true, null);
            while (!isCancelled()) {
                if (channel.messageCount(FRONT_ANALYSIS_QUEUE) > 0) {
                    channel.basicConsume(FRONT_ANALYSIS_QUEUE, true, analysisProgressionConsumer());
                } else {
                    System.out.println("Message count for " + FRONT_ANALYSIS_QUEUE + " is 0");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            closeConnection(channel, connection);
        }
        // Dummy Data until i can't connect to rabbitMQ
        return null;
    }

    DefaultConsumer analysisProgressionConsumer() {
        return new DefaultConsumer(channel) {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties
                    , byte[] body) {
                try {
                    properties.builder().deliveryMode(2);
                    String analysisProgression = new String(body, StandardCharsets.UTF_8);
                    System.out.println("Progression: " + analysisProgression);
                    AnalysisProgress progress = new GsonCustom().create().fromJson(analysisProgression,
                            AnalysisProgress.class);
                    publishProgress(progress);
                    if (progress.getStatus().equals("FINISHED")) { // TODO : Replace with status
                        cancel(true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
    }

    private void closeConnection(Channel channel, Connection connection) {
        if (channel != null && channel.isOpen()) {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
        if (connection != null && connection.isOpen()) {
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPostExecute(AnalysisProgress result) {
        System.out.println(result);
        closeConnection(channel, connection);
    }


    @Override
    protected void onCancelled() {
        super.onCancelled();
        closeConnection(channel, connection);
    }

    @Override
    protected void onProgressUpdate(AnalysisProgress... values) {
        AnalysisProgress progress = values[0];
        progressionTextView.setText(progress.getStatus());
        System.out.println(progress);
    }
}