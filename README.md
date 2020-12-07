# DigitalOcean-Spaces
* To start with. You have to create a digital Ocean account to have to their Spaces.
* Create an ACCESS_KEY_ID and SECRET_ACCESS_KEY which correspond to AWS_ACCESS_KEY_ID and AWS_SECRET_ACCESS_KEY respectively.

# Authenticate with Amazon S3 credentials
There are number of ways you could autheticate but I will explain the one I used in this project.


```
# Move this credentials file to (~/.aws/credentials)
# after you fill in your access and secret keys in the default profile
# WARNING: To avoid accidental leakage of your credentials,
#          DO NOT keep this file in your source directory.
[default]
aws_access_key_id=your_access_key_id
aws_secret_access_key=your_secret_access_key

```
Create .aws folder under C:\Users\user\ for windows users and move the save the above file as credentials. At the end the path will be ``` C:\Users\user\.aws\credentials```
For Linux users create the folder under your home directory.

With that in place you can access the credentials as follows; 

```AWSCredentials credentials = new ProfileCredentialsProvider().getCredentials();```

Now, we can go on and create a connection to the Space storage

``` 
AmazonS3 client = AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("https://nyc3.digitaloceanspaces.com","nyc3"))
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build(); ```


