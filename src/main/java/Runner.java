
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;

public class Runner {
    private static String bucketName = "icon-school-manager";
    private static String folderName = "SG";
    private static String fileLocation = "/home/longbridge/Documents/test";

    public static void main(String[] args) {
        AWSCredentials awsCredentials = new ProfileCredentialsProvider().getCredentials();
        AmazonS3 client = AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("https://nyc3.digitaloceanspaces.com","nyc3"))
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();

        String fileLocation = "/home/habib/Documents/test";
        String fileToUpload = "testFile.xlsx";
        String newFile = "airforce.jpg";
        String completePath = fileLocation+"/"+fileToUpload;
        File file = new File(completePath);
//        String fileToUpload = "Screenshot.png";
        String fileNameUpstream = folderName+"/"+fileToUpload;
        modifyFile(bucketName, folderName, fileToUpload, newFile, client);
        deleteObject(bucketName, folderName, fileToUpload, client);
    }


    public static void createFolder(String bucketName, String folderName, AmazonS3 client) throws Exception {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(0);

        InputStream emptyContent = new ByteArrayInputStream(new byte[0]);

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderName+"/", emptyContent, metadata);
        try{
            PutObjectResult putObjectResult = client.putObject(putObjectRequest);
        }catch (Exception exception){
            throw new Exception(exception.getMessage());
        }

    }

    public static void createObjectInFolder(String bucketName, String fileName, File file, AmazonS3 client){
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,fileName,file)
                .withCannedAcl(CannedAccessControlList.PublicRead);
        try{
            client.putObject(putObjectRequest);
            System.out.println(fileName + " Successfully uploaded");
            listObjects(bucketName,folderName,client);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
    public  static String createBucket(String bucketName, AmazonS3 client){
        Bucket bucket = client.createBucket(bucketName);
        return bucket.getName();
    }

    public  static void modifyFile(String bucketName, String folderName, String fileName, String newFileName, AmazonS3 client){
        deleteObject(bucketName, folderName, fileName, client);
        String fileNameUpstream = folderName+"/"+newFileName;
        String completePath = fileLocation+"/"+newFileName;
        File file = new File(completePath);
        createObjectInFolder(bucketName,fileNameUpstream,file, client);
    }


    public static void deleteObject(String bucketName, String folderName, String objectName, AmazonS3 client){
        try {
            ObjectListing objectListing = client.listObjects(bucketName, folderName);
            for(S3ObjectSummary file : objectListing.getObjectSummaries()){
                if(file.getKey().equalsIgnoreCase(folderName+"/"+objectName)){
                    client.deleteObject(bucketName, file.getKey());
                    System.out.println(file.getKey() + " successfully deleted");
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void downloadObject(String bucketName, String objectName, AmazonS3 client){

    }

    public  static void deleteBucket(String bucketName, AmazonS3 client){

    }

    public static void listObjects(String bucketName, String folderName, AmazonS3 client){
        ObjectListing fileList  = client.listObjects(bucketName, folderName);
        System.out.println(fileList);
        for (S3ObjectSummary file: fileList.getObjectSummaries()){
            System.out.println("- " + file.getKey());
        }
    }
}


