/**
 * 
 */
/**
 * 
 */
module galton_board {
	requires java.desktop;
	requires aws.lambda.java.core;
	requires com.fasterxml.jackson.databind;
	requires software.amazon.awssdk.services.lambda;
	requires software.amazon.awssdk.awscore;
	requires software.amazon.awssdk.core;
	requires software.amazon.awssdk.utils;
	requires software.amazon.awssdk.regions;
	requires software.amazon.awssdk.http;
	requires software.amazon.awssdk.http.urlconnection;
	opens aws to com.fasterxml.jackson.databind;
}