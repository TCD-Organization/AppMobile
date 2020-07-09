package com.example.pa4al.amqp;

import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;

import androidx.annotation.RequiresApi;

import com.example.pa4al.gson.GsonCustom;
import com.example.pa4al.model.Analysis;
import com.example.pa4al.ui.analyses.AnalysisListAdapter;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import static com.example.pa4al.utils.TimeToStringFormatter.timeToString;

public class FetchAnalysisProgressionTask extends AsyncTask<AnalysisListAdapter.AnalysesViewHolder, Analysis, Analysis> {

    private Connection connection;
    private Channel channel;
    private AnalysisListAdapter.AnalysesViewHolder holder;

    private boolean firstReception = true;

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
            e.printStackTrace();
            closeConnection(channel, connection);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected Analysis doInBackground(AnalysisListAdapter.AnalysesViewHolder... params) {
        holder = params[0];
        String FRONT_ANALYSIS_QUEUE = "analysis_"+ holder.mAnalysisItem.getId() +"_q";

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
                        holder.mLastingTime.setText(timeToString(0L));
                        cancel(true);
                    }
                    if(!analysisProgression.getStatus().equals("TO_START") && firstReception) {
                        holder.mAnalysisItem.setLasting_time(analysisProgression.getLasting_time());
                        holder.mLastingTime.setText(timeToString(analysisProgression.getLasting_time()));
                        holder.mProgressBar.setMax(analysisProgression.getTotal_steps());
                        holder.mStepMax.setText(String.valueOf(analysisProgression.getTotal_steps()));
                        startRemainingTimeCountdown();
                        firstReception = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
    }

    private void startRemainingTimeCountdown() {
        new Thread(new Runnable() {
            public void run() {
                while (!isCancelled() && holder.mAnalysisItem.getLasting_time() > 0) {
                    removeOneSecond();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            private void removeOneSecond() {
                holder.mAnalysisItem.setLasting_time(holder.mAnalysisItem.getLasting_time() - 1000);
                holder.mLastingTime.setText(timeToString(holder.mAnalysisItem.getLasting_time()));
            }

        }).start();
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
        holder.mAnalysisStatus.setText(progress.getStatus());
        holder.mStepName.setText(String.valueOf(progress.getStep_name()));
        holder.mStepNumber.setText(String.valueOf(progress.getStep_number()));
        holder.mProgressBar.setProgress(progress.getStep_number());
        // value for the first time

        System.out.println("Progress step number is now : " + progress.getStep_number());
    }
}