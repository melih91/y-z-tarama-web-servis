package com.Mlh;

import javax.xml.ws.Endpoint;

public class Publish {

	public static void main(String[] args) {
		Endpoint endpoint = Endpoint.publish("http://localhost:7879/call", new CallWebServiceImp());
		System.out.println(endpoint.isPublished());
	}
}
