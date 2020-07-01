package com.example.pa4al.amqp;

import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.pa4al.gson.GsonCustom;
import com.example.pa4al.model.Analysis;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;
public class FetchAnalysisProgressionTask extends AsyncTask<FetchAnalysisProgressionParameter, Analysis, Analysis> {

    private Connection connection;
    private Channel channel;
    private ProgressBar progressBar;
    private TextView stepNumber;
    private String analysisId;

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected Analysis doInBackground(FetchAnalysisProgressionParameter... params) {
        progressBar = params[0].progressBar;
        stepNumber = params[0].stepNumber;
        analysisId = params[0].id;
        String FRONT_ANALYSIS_QUEUE = "analysis_"+ analysisId +"_q";

        try {
            channel.queueDeclare(FRONT_ANALYSIS_QUEUE, true, false, true, null);
            while (!isCancelled()) {
                if (channel.messageCount(FRONT_ANALYSIS_QUEUE) > 0) {
                    channel.basicConsume(FRONT_ANALYSIS_QUEUE, true, analysisProgressionConsumer());
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
                    String analysisProgressionText = new String(body, StandardCharsets.UTF_8);
                    System.out.println("Progression: " + analysisProgressionText);
                    Analysis analysisProgression = new GsonCustom().create().fromJson(analysisProgressionText,
                            Analysis.class);
                    publishProgress(analysisProgression);
                    if (analysisProgression.getStatus().equals("FINISHED")) { // TODO : Replace with status
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
    protected void onPostExecute(Analysis result) {
        System.out.println(result);
        closeConnection(channel, connection);
    }


    @Override
    protected void onCancelled() {
        super.onCancelled();
        closeConnection(channel, connection);
    }

    @Override
    protected void onProgressUpdate(Analysis... values) {
        Analysis progress = values[0];
        progressBar.setProgress(progress.getStep_number());
        stepNumber.setText(progress.getStep_number());
        System.out.println("Progress step number is now : " + progress.getStep_number());
    }
}