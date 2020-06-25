package com.example.pa4al.amqp;

import android.os.AsyncTask;
import android.os.Build;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
public class FetchAnalysisProgressionTask extends AsyncTask<String, Integer, Integer> {

    public interface LoadingTaskListener {
        void onResourceLoaded();
    }

    /*private final ProgressBar progressBar;
    private final LoadingTaskListener loadingTaskListener;*/
    private ConnectionFactory connectionFactory;

    public FetchAnalysisProgressionTask(/*ProgressBar progressBar, LoadingTaskListener loadingTaskListener*/) {
        /*this.progressBar = progressBar;
        this.loadingTaskListener = loadingTaskListener;
        this.connectionFactory = new ConnectionFactory();*/
        connectionFactory.setAutomaticRecoveryEnabled(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected Integer doInBackground(String... analysisId) {
        try {

            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("51.178.18.199:5672");
            Connection connection = factory.newConnection();

            Channel channel = connection.openChannel().orElseThrow(RuntimeException::new);

            /*
            Consumer consumer = new DefaultConsumer(channel);
            channel.basicConsume("analysis_" + analysisId + "_q", false, consumer);
            */

            //String queueName = channel.queueBind("analysis."+analysisId, EXCHANGE_NAME, )


            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");


            System.out.println("Analysis id is : " + analysisId[0] + channel.confirmSelect());
            publishProgress();

            /*
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received '" +
                        delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
                publishProgress(message);
            };
            channel.basicConsume("analysis_" + analysisId[0] + "_q", true, deliverCallback, consumerTag -> { });
            */


        } catch (IOException e) {
            e.printStackTrace();
        //} catch (InterruptedException e) {
        //    e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        //} catch (IOException e) {
        //    e.printStackTrace();
        }
        // Dummy Data until i can't connect to rabbitMQ
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    private static final String EXCHANGE_NAME = "type.id.tx";

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void readUpdates(String analysisId) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("51.178.18.199:5672");
        Connection connection = factory.newConnection();

        Channel channel = connection.openChannel().orElseThrow(RuntimeException::new);

        Consumer consumer = new DefaultConsumer(channel);
        channel.basicConsume("analysis_" + analysisId + "_q", false, consumer);

        //String queueName = channel.queueBind("analysis."+analysisId, EXCHANGE_NAME, )


        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
        channel.basicConsume("analysis_" + analysisId + "_q", true, deliverCallback, consumerTag -> { });
    }
}