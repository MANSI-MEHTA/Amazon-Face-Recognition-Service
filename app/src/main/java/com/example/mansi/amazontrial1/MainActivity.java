package com.example.mansi.amazontrial1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    AmazonS3 s3Client;
    String bucket = "Mansi";
    File uploadToS3 = new File("C:\\Users\\Mansi\\Desktop");
    TransferUtility transferUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
        public void s3credentialsProvider(){

            // Initialize the AWS Credential
            CognitoCachingCredentialsProvider cognitoCachingCredentialsProvider = new CognitoCachingCredentialsProvider(
                    getApplicationContext(),
                    "us-east-1:521c4556-b969-456d-93ec-a3fbc8bfe37f", // Identity Pool ID
                    Regions.US_EAST_1 // Region
            );
            createAmazonS3Client(cognitoCachingCredentialsProvider);
        }
    /**
     * Create a AmazonS3Client constructor and pass the credentialsProvider.
     * @param credentialsProvider
     */
    public void createAmazonS3Client(CognitoCachingCredentialsProvider credentialsProvider){

        // Create an S3 client
         s3Client = new AmazonS3Client(credentialsProvider);

        // Set the region of your S3 bucket
        s3Client.setRegion(Region.getRegion(Regions.US_EAST_1));
    }
    public void setTransferUtility(){

        transferUtility = new TransferUtility(s3Client,
                getApplicationContext());
    }
    /**
     * This method is used to upload the file to S3 by using TransferUtility class
     * @param view
     */
    public void uploadFileToS3(View view){

        TransferObserver transferObserver = transferUtility.upload(
               bucket ,          /* The bucket to upload to */
                "Screenshot.png",/* The key for the uploaded object */
                uploadToS3       /* The file where the data to upload exists */
        );

        transferObserverListener(transferObserver);
    }


    public void transferObserverListener(TransferObserver transferObserver){

        transferObserver.setTransferListener(new TransferListener(){

            @Override
            public void onStateChanged(int id, TransferState state) {
                Toast.makeText(getApplicationContext(), "State Change" + state,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                int percentage = (int) (bytesCurrent/bytesTotal * 100);
                Toast.makeText(getApplicationContext(), "Progress in %" + percentage,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e("error","error");
            }

        });
    }



}
